package com.ccb.odsb;

/**
 * <p>Title: ��̨ҵ�����</p>
 *
 * <p>Description: ��̨ҵ�����</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 */

import com.ccb.dao.LSCONSUMEINFO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
     * 1���Ա��ر��м����Ѵ�ODSB��ȡ�õ����������������
     * 2����ODSB���л�ȡ�����������֮�����������
     * 3��������ˮ�ţ����뵽�������ѱ���
     *
     * @return
     */
    public int readFromODSB() {
        int rtn = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df_time = new SimpleDateFormat("HH:mm:ss");

        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                String sql = "select max(inac_date) as inac_date from ls_consumeinfo ";
                String last_inac_date = null;
                RecordSet rs = dc.executeQuery(sql);
                while (rs.next()) {
                    last_inac_date = rs.getString("inac_date");
                }

                if (last_inac_date == null) {
                    last_inac_date = "1899-01-01";
                }

                sql = " insert into odsb_crd_crt_trad  " +
                        " select * from  odsbdata.BF_EVT_CRD_CRT_TRAD@odsb a, ls_cardbaseinfo b " +
                        " where a.crd_no = b.account and a.inac_date > '" + last_inac_date + "' ";


                rtn = dc.executeUpdate(" truncate table odsb_crd_crt_trad ");
                rtn = dc.executeUpdate(sql);
                if (rtn < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }

                //������Ϣ��
                sql = " insert into odsb_crd_crt  " +
                        " select * from  odsbdata.BF_AGT_CRD_CRT@odsb  " +
                        " where crd_no like '6283660015%' ";

                int crd_rtn = 0;
                dc.executeUpdate(" truncate table odsb_crd_crt ");
                crd_rtn = dc.executeUpdate(sql);
                if (crd_rtn < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }

                //�����ر�
                sql = " select a.*,b.cardname,c.stmt_day from odsb_crd_crt_trad a, ls_cardbaseinfo b, odsb_crd_crt c " +
                        " where a.crd_no=b.account and a.crd_no=c.crd_no ";
                rs = dc.executeQuery(sql);
                LSCONSUMEINFO consume = new LSCONSUMEINFO();
                Date date = new Date();
                String strdate = df.format(date);
                String strtime = df_time.format(date);
                while (rs.next()) {
                    //��ˮ��
                    consume.setLsh("111");

                    consume.setAccount(rs.getString("crd_no"));
                    consume.setBusidate(rs.getString("tx_day"));
                    consume.setBusimoney(rs.getDouble("inac_amt"));
                    consume.setBusiname(rs.getString("filler_2"));
                    consume.setCardname(rs.getString("cardname"));

                    //��ٻ����մ���
                    String stmt_day = rs.getString("stmt_day");
                    String inac_day = rs.getString("inac_date");
                    String limitdate = getLimitDate(inac_day, stmt_day);
                    consume.setLimitdate(limitdate);

                    consume.setRemark("��ODSB��ʼ����");
                    consume.setOperid("9999");
                    consume.setOperdate(df.format(date));
                    consume.setStatus("10");
                    consume.setTx_cd(rs.getString("tx_cd"));
                    consume.setRef_number(rs.getString("ref_number"));
                    consume.setCmbi_merch_no(rs.getString("cmbi_merch_no"));
                    consume.setInac_date(inac_day);
                    consume.setInac_amt(rs.getDouble("inac_amt"));
                    consume.setTxlog("��ODSB��ʼ����");
                    consume.setOdsbdate(strdate);
                    consume.setOdsbtime(strtime);
                    consume.setRecversion(0);
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

    private String getLimitDate(String inac_date, String stmt_day) {
        if (inac_date == null || stmt_day == null) {
            throw new RuntimeException("��������");
        }
        int i_inac_date_day = Integer.parseInt(inac_date.substring(8, 10));
        int  i_stmt_day = Integer.parseInt(stmt_day);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String rtndate = null;
        Calendar c = new GregorianCalendar();
        if (i_stmt_day <= i_inac_date_day) {
            //�������뻹������ͬ��֮ǰ����ٻ�������Ϊ�����պ�20����Ȼ��
            c.set(Calendar.DATE, i_stmt_day );
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, 20);
        }else{
            //�������ڻ�����֮����ٻ�������Ϊ�¸��µ��ʵ��ռ�20��
            c.set(Calendar.DATE, i_stmt_day );
            c.add(Calendar.DATE, 20);
        }
        rtndate = df.format(c.getTime());
        return rtndate;
    }
}
