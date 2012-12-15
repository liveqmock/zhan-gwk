package com.ccb.cardinfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

/**
 * <p>Title: ��̨ҵ�����</p>
 * <p/>
 * <p>Description: ��̨ҵ�����</p>
 * <p/>
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * @author
 * @version 1.0
 */


public class ReadCardbaseAction extends Action {

    // ��¼��odsb��ȡ���Ŀ���Ϣ��¼��
    private int odsbReadCrdTotalCnt = 0;

    // ��¼��odsb��ȡ���ĸ������ݼ�¼��
    private int odsbReadUpdateCrdCnt = 0;

    // ��¼��odsb��ȡ�����������ݼ�¼��
    private int odsbReadNewCrdCnt = 0;

    // ��¼��odsb��ȡ�����������ݼ�¼��
    private int odsbReadNullCrdCnt = 0;

    // ��¼ÿ�η��������ֵ����ݼ�¼��
    private int sendInfoCount = 0;

    private static final Log logger = LogFactory.getLog(ReadCardbaseAction.class);


    //4.2.4.1	������Ϣ����
    /*
   	<list>
    <map>
    <!������-->
    <string> ACCOUNT </string><string>billcode</string>
    <!���ֿ��� -->
    <string> CARDNAME </string><string>2008</string>
    <!��Ԥ�㵥λ-->
    <string> Bdgagency</string><string>value</string>
    <!�������˻���-->
    <string> GATHERINGBANKACCTNAME </string><string>3</string>
    <!�������˻�������-->
    <string> GATHERINGBANKNAME </string><double>0.0</double>
	<!�����˻���-->
	<string> GATHERINGBANKACCTCODE </string><string>value</string>
	<!�����֤��-->
	<string> IDNUMBER </string><string>value</string>
	<!����;-->
	<string> DIGEST </string><string>value</string>
	<!����������-->
	<string> BANK</string><string>value</string>
	<!����������-->
	<string> CREATEDATE </string><string>value</string>
	<!����Ч��ʼ����-->
	<string> Startdate</string><string>value</string>
	<!����Ч��ֹ����-->
	<string>enddate</string><string>value</string>
	<!�����ݲ�������	-->
	<string> action</string><string>value</string>
</map>
<!-- �� -->
</list>
    */
    // 1����odsb��ȡ���񿨱�(BF_AGT_CRD_CRT)��ȡ���ݱ��浽gwk.odsb_crd_crt����
    // 2���Աȸ���gwk��ls_cardbaseinfo�����������ޱ仯 �����������Ӧ�ֶΣ�����action����Ϊ�޸�('1')������״̬(SENTFLAG)����Ϊδ����('0')
    // 3�����������ֶΣ���д��ls_cardbaseinfo��actionΪ����('0'),δ����״̬
    // 4��������ע������action����Ϊɾ��('2')��״̬Ϊδ����

    public int readCrdCrts() {
        try {
            // ��odsb��ȡ���񿨱�(BF_AGT_CRD_CRT)��ȡ���ݱ��浽gwk.odsb_crd_crt����
            this.odsbReadCrdTotalCnt = insertAllCrdCrt();
        } catch (Exception e) {
            logger.error("��ȡODSB������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("��ȡODSB������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        try {
            // ��ȡ����������Ϣ
            this.odsbReadNewCrdCnt = checkNewCrdCrt();
        } catch (Exception e) {
            logger.error("����ODSB����������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("����ODSB����������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        try {
            // ����ע��������Ϣ
            this.odsbReadNullCrdCnt = checkNullCrdCrt();
        } catch (Exception e) {
            logger.error("����ODSB��ע��������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("����ODSB��ע��������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        try {
            this.odsbReadUpdateCrdCnt = checkUpdateCrdCrt();
        } catch (Exception e) {
            logger.error("���¹�����Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("���¹�����Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        StringBuffer show_content = new StringBuffer();
        show_content.append(this.odsbReadNewCrdCnt).append("_");
        show_content.append(this.odsbReadNullCrdCnt).append("_");
        show_content.append(this.odsbReadUpdateCrdCnt).append("_");
        this.res.setFieldName("importData");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(new String(show_content));
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }
    //��odsb��ȡ���񿨱�(BF_AGT_CRD_CRT)��ȡ���ݱ��浽gwk.odsb_crd_crt����

    private int insertAllCrdCrt() throws Exception {
        String sql = " insert into odsb_crd_crt  " +
                " select * from  odsbdata.BF_AGT_CRD_CRT@odsb_remote  " +
                " where crd_no like '62836600%' ";
        int crd_rtn = 0;
        //dc.executeUpdate(" truncate table odsb_crd_crt ");
        crd_rtn = dc.executeUpdate(sql);
        return crd_rtn;
    }

    // �鿴�����������û������и���ls_cardbaseinfo��action�ֶΣ�������������

    private int checkNewCrdCrt() throws Exception {
        int newCnt = 0;
        // ����odsb��ȡ�������е������˻���
        String allSql = "select distinct occ.crd_no as account from odsb_crd_crt occ join ls_personalinfo gwy" +
                " on rtrim(occ.embosser_name3) = rtrim(gwy.perid) where occ.crd_no not in (select account from ls_cardbaseinfo)";
//        +                        " and occ.crd_sts in ('801','802')";     
        RecordSet rs = dc.executeQuery(allSql);
        // �����������ݵ��˻���
        if (rs != null) {
            StringBuffer accountBuffer = new StringBuffer();
            while (rs.next()) {
                accountBuffer.append("'");
                String newAccount = rs.getString("account");
                accountBuffer.append(newAccount);
                accountBuffer.append("'");
                if (!rs.isLast()) {
                    accountBuffer.append(",");
                }
            }
            String accounts = new String(accountBuffer).trim();
            if (!"".equals(accounts)) {
                // �������˻����浽ls_cardbaseinfo,�ֶ�gatheringbankacctname��������������
                // ���areacode�ֶ� 2012-12-14
                String insertCrdBsSql = "insert into ls_cardbaseinfo (select o.crd_no,p.pername,p.deptcode,p.areacode," +
                        "'��������','37101986827059123456',p.perid,'���񿨿���',8015,o.open_card_dt,o.open_card_dt," +
                        "substr(o.open_card_dt,0,4)+3||substr(o.open_card_dt,5,6) as enddate ,'0',null,'AUTO',sysdate,'0'" +
                        ",null,to_char(sysdate,'yyyy-mm-dd'),to_char(sysdate,'HH24:MI:SS'),0,'1',p.areacode from ls_personalinfo p" +
                        " left join odsb_crd_crt o on p.perid = o.embosser_name3 where o.crd_no in (" + accounts + "))";
                newCnt = dc.executeUpdate(insertCrdBsSql);
                //�����¼�ɹ��Ժ󣬸�����������������gatheringbankacctname��bank�����ֶ� 2012-05-13 linyong
                //��ѯgatheringbankacctname�����ֵļ�¼
                // ���areacode�ֶ� 2012-12-14
                String strSql = "select t.gatheringbankacctname,t.account,t.idnumber,t.areacode From ls_cardbaseinfo t "+
                        "where trim(translate(nvl(t.gatheringbankacctname,'X'),'0123456789',' '))is null ";
                rs = dc.executeQuery(strSql);
                //��ȡ���ݼ��Ժ���б���������
                if (rs!=null){
                    while(rs.next()){
                        strSql="update ls_cardbaseinfo set gatheringbankacctname='"+
                                PropertyManager.getProperty("finance.name." + rs.getString("areacode"))+
                                "',bank="+PropertyManager.getProperty("ccb.code."+rs.getString("areacode"))+
                                " where account='"+rs.getString("account")+"' and idnumber='"+rs.getString("idnumber")+"' ";
                        int rtn = dc.executeUpdate(strSql);
                    }
                }
            }
        }
        return newCnt;
    }

    // �鿴����ע�����û������и���ls_cardbaseinfo��action�ֶΣ�������������

    private int checkNullCrdCrt() throws Exception {
        int nullCnt = 0;
        // ����odsb��ȡ�������еķ����˻���
        String sql = "select distinct occ.crd_no as account from odsb_crd_crt occ right join ls_personalinfo gwy" +
                " on occ.embosser_name3 = gwy.perid where" +
                " occ.crd_sts in ('809','810')";
        RecordSet rs = dc.executeQuery(sql);
        // ��������˻���
        if (rs != null) {
            StringBuffer accountBuffer = new StringBuffer();
            while (rs.next()) {
                accountBuffer.append("'");
                String newAccount = rs.getString("account");
                accountBuffer.append(newAccount);
                accountBuffer.append("'");
                if (!rs.isLast()) {
                    accountBuffer.append(",");
                }
            }
            String accounts = new String(accountBuffer).trim();
            if (!"".equals(accounts)) {
                // ��ע���˻����浽ls_cardbaseinfo
                String updateCrdBsSql = "update ls_cardbaseinfo l set l.action='2',l.status='128',l.sentflag='0'" +
                        " where l.account in (" + accounts + "))";
                nullCnt = dc.executeUpdate(updateCrdBsSql);
            }
        }
        return nullCnt;
    }

    // ѡ����ɽ�����ֵĹ��񿨳ֿ��˵Ŀ���Ϣ���Ա��������ޱ仯���޸�ls_cardbaseinfo
    // TODO ��������Ϣ�Ա�

    private int checkUpdateCrdCrt() throws Exception {
        int updateCnt = 0;
        return updateCnt;
    }
}