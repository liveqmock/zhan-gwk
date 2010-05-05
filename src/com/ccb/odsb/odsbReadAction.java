package com.ccb.odsb;

/**
 * <p>Title: ��̨ҵ�����</p>
 *
 * <p>Description: ��̨ҵ�����</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: ��˾</p>
 *
 * @author leonwoo
 * @version 1.0
 */

import com.ccb.mortgage.MortUtil;
import com.ccb.util.SeqUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.DBUtil;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.BusinessDate;

import java.sql.PreparedStatement;

public class odsbReadAction extends Action {
    private static final Log logger = LogFactory.getLog(odsbReadAction.class);
    // ����������
    final String CHECKAMT_SQL = "select count(*) checkResult "
            + " from LN_LOANAPPLY a inner join ln_odsb_loanapply b on a.loanid=b.loanid "
            + " where a.NEEDADDCD= '1' and (trim(a.cust_name) <> trim(b.cust_name) "
            + " or a.rt_orig_loan_amt <> b.rt_orig_loan_amt " + " or trim(a.bankid) <> trim(b.bankid)) ";
    // �����������
    final String CHECKPROJ_SQL = "select count(*) checkProj from ln_odsb_loanapply a where not "
            + " exists(select 1 from ln_coopproj b where b.proj_no=a.proj_no) and trim(a.proj_no) is not null ";

    /**
     * <p/>
     * ��ODSB��������
     *
     * @return
     */
    public int readFromODSB() {
        int rtn = 0;
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                rtn = dc.executeUpdate(" truncate table ln_odsb_coopproj ");
                rtn = dc.executeUpdate(" insert into ln_odsb_coopproj  select * from  ODSBDATA.BF_AGT_LNP_COOP_CAPROJINFO@odsb a ");
                if (rtn < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                //int rtn = dc.executeUpdate(" truncate table ln_odsb_loanapply  ");
//                rtn = dc.executeUpdate(" delete from  ln_odsb_loanapply  ");
                rtn = dc.executeUpdate(" truncate table ln_odsb_loanapply ");
                //odsb table name= ODSBDATA.BF_AGT_LNP_CTRT_APPLY
//                rtn = dc.executeUpdate(" insert into ln_odsb_loanapply  select * from  ODSBDATA.BF_AGT_LNP_CTRT_APPLY@odsb a "
//                        + " where a.APLY_DT>='2010-01-01' or a.CUST_OPEN_DT>='2010-01-01' ");
                rtn = dc.executeUpdate(" insert into ln_odsb_loanapply  select * from  ODSBDATA.BF_AGT_LNP_CTRT_APPLY@odsb a ");
                if (rtn < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        }
        //���ض����¼��
        this.res.setFieldName("importCnt");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(String.valueOf(rtn));
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }

    /**
     * ���ODSB��ȡ������
     */

    public int ODSBCheck() {
        String checkResult = "0";
        String checkProj = "0";
        try {
            // �ٸ���ln_loanapply�С����������ϱ�־��Ϊ��1���ļ�¼�� ��ln_odsb_loanapply����ͬ��������ŵļ�¼�Ƚ�
            // �������������������������в�ͬ������excel��ʾ��
            RecordSet rs = dc.executeQuery(CHECKAMT_SQL);
            while (rs.next()) {
                checkResult = rs.getString("checkResult");
            }
            // �ڲ�ѯln_odsb_loanapply�е�proj_no�Ƿ��ڵ�Ѻϵͳ�д��ڣ�������Ŀ��������������򵯳�excel��
            rs = dc.executeQuery(CHECKPROJ_SQL);
            while (rs.next()) {
                checkProj = rs.getString("checkProj");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("302"));
            return -1;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        // �����
        this.res.setFieldName("checkResult;checkProj");
        this.res.setFieldType("text;text");
        this.res.setEnumType("0;0");
        this.res.setFieldValue(checkResult + ";" + checkProj);
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }

    /**
     * ����ODSB���´�������
     * <br>// --- ����ln_loanapply�С����������ϱ�־����Ϊ��0�� �����ݼ�¼������ʱ���������ln_odsb_loanapply��
     * <br>// ����ln_loanapply�����Ϣ������������ţ��ͻ���������������ln_mortinfo�������Ѻ�����գ�
     */
    public int ODSBUpdate() {
        // ���¼�¼��
        int updateCnt = 0;
        // ���µ�Ѻ�����ڼ�¼��
        int updateMortDateCnt = 0;
        // ������¼��
        int addCnt = 0;
        // ���´��������ϼ�¼��
        int updateNeedCDCnt = 0;
        try {
            for (int i = 0; i < this.req.getRecorderCount(); i++) {

                // ��ϵͳ��
                // ϵͳ����������finally����
                dc.setAuto(false);
                dc.executeUpdate("update SYS_LOCK set SYSLOCKSTATUS ='1'");
                dc.commit();
                
                //---------------------------------------------���м�飬����в�һ���򷵻ز���ʾ��Ϣ---------------------
                // �ٸ���ln_loanapply�С����������ϱ�־��Ϊ��1���ļ�¼�� ��ln_odsb_loanapply����ͬ��������ŵļ�¼�Ƚ�
                // �������������������������в�ͬ������excel��ʾ��
                
                logger.debug("check(������������������) begin:"+BusinessDate.getTodaytime());
                RecordSet rs = dc.executeQuery(CHECKAMT_SQL);
                while (rs.next()) {
                    if (rs.getInt("checkResult") > 0) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("307"));
                        return -1;
                    }
                }
                logger.debug("check(������������������) end:"+BusinessDate.getTodaytime());
                // �ڲ�ѯln_odsb_loanapply�е�proj_no�Ƿ��ڵ�Ѻϵͳ�д��ڣ�������Ŀ��������������򵯳�excel��
                logger.debug("check(������Ŀ��) begin:"+BusinessDate.getTodaytime());
                rs = dc.executeQuery(CHECKPROJ_SQL);
                while (rs.next()) {
                    if (rs.getInt("checkProj") > 0) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("308"));
                        return -1;
                    }
                }
                logger.debug("check(������Ŀ��) end:"+BusinessDate.getTodaytime());
                dc.setAuto(false);
                //----------------------------------------------��������------------------------------------------------
                logger.debug("�������£�����������־��Ϊ0 begin:"+BusinessDate.getTodaytime());
                updateCnt = dc.executeUpdate(""
                        + "update ("
                        + "select /*+ BYPASS_UJVC */"
                        + "a.LN_ACCT_NO," //�����˺�
                        + "a.ODS_SRC_DT," //Դϵͳҵ������
                        + "a.CUST_NO,"    //�ͻ����
                        + "a.CRLMT_NO,"   //��ȱ��
                        + "a.CURR_CD,"    //����
                        + "a.LN_PROD_COD,"//��Ʒ����
                        + "a.LN_TYP,"     //��������
                        + "a.GUARANTY_TYPE,"//������ʽ
                        + "a.APLY_DT,"     //��������
                        + "a.RT_ORIG_LOAN_AMT,"//������
                        + "a.RT_TERM_INCR,"  //��������
                        + "a.PAY_TYPE,"      //���ʽ
                        + "a.PROJ_NO,"       //��Ŀ���
                        + "a.RATECODE,"      //���ʴ���
                        + "a.BASICINTERATE," //�����׼����
                        + "a.RATEACT,"       //�����������
                        + "a.RATECALEVALUE," //��������ֵ(���ʸ�������)
                        + "a.INTERATE,"      //ִ������
                        + "a.CUST_OPEN_DT,"  //��������
                        + "a.EXPIRING_DT,"   //��������
                        + "a.BANKID,"        //������
                        + "a.OPERID,"        //������
                        + "a.APPRSTATE,"     //����״̬
                        + "a.LOANSTATE,"     //����״̬
                        + "a.CORPID,"        //���������
                        + "a.CUST_NAME,"     //�ͻ�����
                        + "a.releasedate,"   //�ÿ�������

                        + "b.LN_ACCT_NO LN_ACCT_NO2,"
                        + "b.ODS_SRC_DT ODS_SRC_DT2,"
                        + "b.CUST_NO CUST_NO2,"
                        + "b.CRLMT_NO CRLMT_NO2,"
                        + "b.CURR_CD CURR_CD2,"
                        + "b.LN_PROD_COD LN_PROD_COD2,"
                        + "b.LN_TYP LN_TYP2,"
                        + "b.GUARANTY_TYPE GUARANTY_TYPE2,"
                        + "b.APLY_DT APLY_DT2,"
                        + "b.RT_ORIG_LOAN_AMT RT_ORIG_LOAN_AMT2,"
                        + "b.RT_TERM_INCR RT_TERM_INCR2,"
                        + "b.PAY_TYPE PAY_TYPE2,"
                        + "b.PROJ_NO PROJ_NO2,"
                        + "b.RATECODE RATECODE2,"
                        + "b.BASICINTERATE BASICINTERATE2,"
                        + "b.RATEACT RATEACT2,"
                        + "b.RATECALEVALUE RATECALEVALUE2,"
                        + "b.INTERATE INTERATE2,"
                        + "b.CUST_OPEN_DT CUST_OPEN_DT2,"
                        + "b.EXPIRING_DT EXPIRING_DT2,"
                        + "b.BANKID BANKID2,"
                        + "b.OPERID OPERID2,"
                        + "b.APPRSTATE APPRSTATE2,"
                        + "b.LOANSTATE LOANSTATE2,"
                        + "b.CORPID CORPID2,"
                        + "b.CUST_NAME CUST_NAME2"
                        + " from ln_loanapply a,ln_odsb_loanapply b"
                        + " where a.loanid=b.loanid"
                        + " and a.NEEDADDCD <>'0'"
                        + " )"
                        + " set"
                        + " LN_ACCT_NO = LN_ACCT_NO2,"
                        + " ODS_SRC_DT = ODS_SRC_DT2,"
                        + " CUST_NO = CUST_NO2,"
                        + " CRLMT_NO = CRLMT_NO2,"
                        + " CURR_CD = CURR_CD2,"
                        + " LN_PROD_COD = LN_PROD_COD2,"
                        + " LN_TYP = LN_TYP2,"
                        + " GUARANTY_TYPE = GUARANTY_TYPE2,"
                        + " APLY_DT = APLY_DT2,"
                        + " RT_ORIG_LOAN_AMT = RT_ORIG_LOAN_AMT2,"
                        + " RT_TERM_INCR = RT_TERM_INCR2,"
                        + " PAY_TYPE = PAY_TYPE2,"
                        + " PROJ_NO = PROJ_NO2,"
                        + " RATECODE = RATECODE2,"
                        + " BASICINTERATE = BASICINTERATE2,"
                        + " RATEACT = RATEACT2,"
                        + " RATECALEVALUE = RATECALEVALUE2,"
                        + " INTERATE = INTERATE2,"
                        + " CUST_OPEN_DT = CUST_OPEN_DT2,"
                        + " EXPIRING_DT = EXPIRING_DT2,"
                        + " BANKID = BANKID2,"
                        + " OPERID = OPERID2,"
                        + " APPRSTATE = APPRSTATE2,"
                        + " LOANSTATE = LOANSTATE2,"
                        + " CORPID = CORPID2,"
                        + " CUST_NAME = CUST_NAME2,"
                        + " releasedate=CUST_OPEN_DT2"
                        + "");
                dc.commit();
                if (updateCnt < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                logger.debug("�������£�����������־��Ϊ0 end:"+BusinessDate.getTodaytime());
                //--------------------------------------- ���µ�Ѻ������-------------------------------------------------
                logger.debug("���µ�Ѻ������  begin:"+BusinessDate.getTodaytime());
                rs = dc.executeQuery(""
                        + " select b.RELEASECONDCD, b.mortDate, a.loanid, b.MORTECENTERCD"
                        + " from ln_loanapply a"
                        + " inner join ln_mortinfo b on a.loanid = b.loanid"
                        + " where a.NEEDADDCD <> '0'"
                        + "");
                // �ſʽ
                String p_releaseCD = "";
                // ��Ѻ����
                String p_mortDate = "";
                // ��Ѻ���
                String p_loanid = "";
                // ��Ѻ����
                String p_center = "";
                // ��Ѻ������
                String morteExpireDate = "";
                // Ԥ�������
                PreparedStatement pst = null;
                dc.setAuto(true);
                pst = dc.getPreparedStatement("update ln_mortInfo set mortExpireDate = ?, mortoverrtndate = ? where loanid = ?");
                while (rs.next()) {
                    updateMortDateCnt++;
                    p_releaseCD = rs.getString("RELEASECONDCD");
                    if (p_releaseCD == null) {
                        p_releaseCD = "";
                    }
                    p_mortDate = rs.getString("mortDate");
                    if (p_mortDate == null) {
                        p_mortDate = "";
                    }
                    p_loanid = rs.getString("loanid");
                    p_center = rs.getString("MORTECENTERCD");
                    if (p_center == null) {
                        p_center = "";
                    }
                    //--- ���µ�Ѻ������---
                    morteExpireDate = MortUtil.getMortExpireDate(p_releaseCD, p_mortDate, dc, p_loanid, p_center);

/*
                    pst.setString(1, morteExpireDate);
                    pst.setString(2, p_loanid);
                    pst.addBatch();
*/
                    pst.setString(1, morteExpireDate);
                    pst.setString(3, p_loanid);
                    //20100403  zhan
                    //���ΪǩԼ�� ͬʱ���µ�Ѻ����������
                    if (p_releaseCD.equals("03") || p_releaseCD.equals("06")) {
                        pst.setString(2, morteExpireDate);
                    } else {
                        pst.setString(2, "");
                    }
                    pst.addBatch();


                    // ÿ1000���ύһ������
                    if (updateMortDateCnt % 1000 == 0 ) {
                      System.out.println(String.valueOf(updateMortDateCnt)+" add commited");
                      pst.executeBatch();
                      pst.clearParameters();
                      pst.clearBatch();
                    }                   
                }
                // ��1000�ı���
                if (pst != null) {
                  System.out.println(String.valueOf(updateMortDateCnt) + " update commited");
                  pst.executeBatch();
                  pst.clearParameters();
                  pst.clearBatch();
                }
                logger.debug("���µ�Ѻ������  end:"+BusinessDate.getTodaytime());
                //---------------------------------------------˳������ʱ��������� ׷��������---------------------------
                // �µ��������NEEDADDCD Ϊ2
                // ÿ���µ������������һ��Ψһ��nbxh
                logger.debug("�������������  begin:"+BusinessDate.getTodaytime());
                pst = null;
                String strSQL = "";
                rs = dc.executeQuery(" select loanid,cust_name from ln_odsb_loanapply a where not "
                        + " exists(select 1 from ln_loanapply b where b.loanid = a.loanid)");
                // ��¼�������ʼ��
                addCnt  =0;
                strSQL =""
                  + "insert into ln_loanapply("
                  + "         loanid,"
                  + "         LN_ACCT_NO,"
                  + "         ODS_SRC_DT,"
                  + "         CUST_NO,"
                  + "         CRLMT_NO,"
                  + "         CURR_CD,"
                  + "         LN_PROD_COD,"
                  + "         LN_TYP,"
                  + "         GUARANTY_TYPE,"
                  + "         APLY_DT,"
                  + "         RT_ORIG_LOAN_AMT,"
                  + "         RT_TERM_INCR,"
                  + "         PAY_TYPE,"
                  + "         PROJ_NO,"
                  + "         RATECODE,"
                  + "         BASICINTERATE,"
                  + "         RATEACT,"
                  + "         RATECALEVALUE,"
                  + "         INTERATE,"
                  + "         CUST_OPEN_DT,"
                  + "         EXPIRING_DT,"
                  + "         BANKID,"
                  + "         OPERID,"
                  + "         APPRSTATE,"
                  + "         LOANSTATE,"
                  + "         CORPID,"
                  + "         CUST_NAME,"
                  + "         releasedate,"
                  + "         nbxh,"
                  + "         NEEDADDCD,"
                  + "         cust_py,"
                  + "         RECVERSION)"
                  //-----ѡ������������-----
                  + "         select "
                  + "         b.loanid,"
                  + "         b.LN_ACCT_NO,"
                  + "         b.ODS_SRC_DT,"
                  + "         b.CUST_NO,"
                  + "         b.CRLMT_NO,"
                  + "         b.CURR_CD,"
                  + "         b.LN_PROD_COD,"
                  + "         b.LN_TYP,"
                  + "         b.GUARANTY_TYPE,"
                  + "         b.APLY_DT,"
                  + "         b.RT_ORIG_LOAN_AMT,"
                  + "         b.RT_TERM_INCR,"
                  + "         b.PAY_TYPE,"
                  + "         b.PROJ_NO,"
                  + "         b.RATECODE,"
                  + "         b.BASICINTERATE,"
                  + "         b.RATEACT,"
                  + "         b.RATECALEVALUE,"
                  + "         b.INTERATE,"
                  + "         b.CUST_OPEN_DT,"
                  + "         b.EXPIRING_DT,"
                  + "         b.BANKID,"
                  + "         b.OPERID,"
                  + "         b.APPRSTATE,"
                  + "         b.LOANSTATE,"
                  + "         b.CORPID,"
                  + "         b.CUST_NAME,"
                  + "         b.CUST_OPEN_DT,"
                  + "         ?,"
                  + "         '2',"
                  + "         ?,"
                  + "         0"
                  + "         from ln_odsb_loanapply b"
                  + "         where b.loanid = ? "
                  + "";
                dc.setAuto(true);
                pst = dc.getPreparedStatement(strSQL);
                while (rs.next()) {
                    // ������¼����1
                    addCnt++;                    
                    pst.setString(1, SeqUtil.getNbxh());
                    pst.setString(2, DBUtil.getSpell(dc, rs.getString("cust_name")));
                    pst.setString(3, rs.getString("loanid"));
                    pst.addBatch();
                    // ÿ1000���ύһ������
                    if(addCnt % 1000 == 0){
                      System.out.println(String.valueOf(addCnt)+" add commited");
                      //logger.debug(String.valueOf(addCnt)+" add commited");
                      pst.executeBatch();     
                      pst.clearParameters();
                      pst.clearBatch();                     
                    }
                }
                // ��1000�ı���
                // logger.debug(String.valueOf(addCnt)+" add commited");
                System.out.println(String.valueOf(addCnt) + " add commited");
                pst.executeBatch();
                pst.clearParameters();
                pst.clearBatch();
                logger.debug("�������������  end:"+BusinessDate.getTodaytime());
                logger.debug("������������״̬��־λ  begin:"+BusinessDate.getTodaytime());
                //�������״̬Ϊ2����������״̬Ϊ0����������Ϊ���벻����
                updateNeedCDCnt = dc.executeUpdate(""
                        + " update ( select /*+ BYPASS_UJVC */"
                        + "    a.NEEDADDCD,"
                        + "    decode(trim(b.LOANSTATE),'2',0,2) needaddcd2"
                        + "    from ln_loanapply a,ln_odsb_loanapply b"
                        + "    where a.loanid=b.loanid"
                        + "    )"
                        + "    set NEEDADDCD=needaddcd2"
                        + "");
                dc.commit();
                if (updateNeedCDCnt < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                logger.debug("������������״̬��־λ  end:"+BusinessDate.getTodaytime());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
           // �쳣����½���
            dc.setAuto(true);
            dc.executeUpdate("update SYS_LOCK set SYSLOCKSTATUS ='0'");
            e.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        } finally {
            // ���ϵͳ��
            dc.setAuto(true);
            dc.executeUpdate("update SYS_LOCK set SYSLOCKSTATUS ='0'");
            // �ر�rs����
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
        }
        // ����ֵ
        //���ظ��¼�¼����������¼�������µ�Ѻ�����ռ�¼�������´��������ϱ�־��¼��
        this.res.setFieldName("updateCnt;addCnt;updateMortDateCnt;updateNeedCDCnt");
        this.res.setFieldType("text;text;text;text");
        this.res.setEnumType("0;0;0;0");
        this.res.setFieldValue(String.valueOf(updateCnt) + ";" + String.valueOf(addCnt) + ";"
                + String.valueOf(updateMortDateCnt) + ";" + String.valueOf(updateNeedCDCnt));
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }
}
