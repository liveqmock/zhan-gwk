package com.ccb.consume;

/**
 * <p>Title: ��̨ҵ�����</p>
 *
 * <p>Description: ��̨ҵ�����</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * @author
 * @version 1.0
 */

import com.ccb.dao.LNTASKINFO;
import gov.mof.fasp.service.BankService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.text.SimpleDateFormat;
import java.util.*;

public class consumeAction extends Action {

    // ��¼ÿ�η��������ֵ����ݼ�¼��
    private int sendInfoCount = 0;

    private static final Log logger = LogFactory.getLog(consumeAction.class);


    //4.2.4.2	������Ϣ����
    /*
    <list>
    <map>
    <!��������ˮ��-->
    <string> ID </string><string>value</string>
    <!������-->
    <string> ACCOUNT </string><string>value</string>
    <!���ֿ��� -->
    <string> CARDNAME </string><string>2008</string>
    <!����������-->
    <string> BUSIDATE </string><string>yymmdd</string>
    <!�����ѽ��-->
    <string> BUSIMONEY </string><double>0.0</double>
    <!�����ѵص�-->
    <string> BUSINAME </string><string>value</string>
    <!����ٻ�����-->
    <string> Limitdate </string><string>value</string>
    </list>

     */
    //  ��ѯ���г�ʼ״̬��δ�����������ݾ�����������
    public int writeConsumeInfo() {
        // ��ѯ���г�ʼ״̬��δ������������
        List cardList = null;
        int updateInfoCount = 0;
        try {
            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
         if(cardList != null && cardList.size() > 0){
        List rtnlist = null;
        try {

            rtnlist = sendConsumeInfoByStatus(cardList, RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("����������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("����������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        // �ж��Ƿ��з������ݣ����ޣ����������״̬Ϊ����ʧ��
        if (rtnlist == null || rtnlist.size() <= 0) {
            try {
                updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_FAIL);
            } catch (Exception e) {
                logger.error("�������ݺ���±�������Ϊʧ��״̬ʧ�ܣ���鿴ϵͳ��־��");
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
            }
            return -1;
        } else {
            // ��������Ϣ
            for (int i = 0; i < rtnlist.size(); i++) {
                Map m1 = (Map) rtnlist.get(i);
                String result = (String) m1.get(RtnTagKey.RESULT);
                // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                    try {
                        updateInfoCount += updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_SUCCESS);
                    } catch (Exception e) {
                        logger.error("�������ݺ���±�������Ϊʧ��״̬�����쳣����鿴ϵͳ��־��");
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("�������ݺ���±�������״̬�����쳣����鿴ϵͳ��־��");
                        return -1;
                    }
                } else {
                    // �ж��Ƿ����ظ���ˮ�ź��ʺţ����У�����ļ�¼״̬Ϊ���ͳɹ�
                    String sameid = (String) m1.get(RtnTagKey.SAMEID);
                    String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                    if ((sameid != null && !"".equals(sameid.trim()))
                            || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                        try {
                            updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
                        } catch (Exception e) {
                            logger.error("�������ݺ�����ظ����͵�����ʱ�����쳣����鿴ϵͳ��־��");
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage("�������ݺ���±�������ʱ�����쳣����鿴ϵͳ��־��");
                            return -1;
                        }
                    }
                }
            }
        }
         }
        //���ط��������ͷ��ͳɹ���¼��
        if (updateInfoCount != -1) {
            String send_update_count = String.valueOf(this.getSendInfoCount()) + "_" + String.valueOf(updateInfoCount);
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue(send_update_count);
            this.res.setType(4);
            this.res.setResult(true);
        } else {
            logger.error("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
            return -1;
        }

        return 0;
    }

    //  ���������쳣״̬��δ���ͳɹ��������ݵ�������
    public int writeConsumeExpInfo() {
        List cardList = null;
        String[] status = new String[]{RtnTagKey.SEND_FAIL, RtnTagKey.SEND_TIME_OUT};
        int updateInfoCount = 0;
        try {
            cardList = queryInfoByStatusArr(status);
        } catch (Exception e) {
            logger.error("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        if(cardList != null && cardList.size() > 0){
        List rtnlist = null;
        try {
            rtnlist = sendConsumeInfoByStatusArr(cardList, status);
        } catch (Exception e) {
            logger.error("����������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("����������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        // �ж��Ƿ��з������ݣ����ޣ����������״̬Ϊ����ʧ��
        if (rtnlist == null || rtnlist.size() <= 0) {
            try {
                updateAllStatusArrToStatus(status, RtnTagKey.SEND_FAIL);
            } catch (Exception e) {
                logger.error("�������ݺ���±�������Ϊʧ��״̬ʧ�ܣ���鿴ϵͳ��־��");
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
            }
            return -1;
        } else {
            // ��������Ϣ
            for (int i = 0; i < rtnlist.size(); i++) {
                Map m1 = (Map) rtnlist.get(i);
                String result = (String) m1.get(RtnTagKey.RESULT);
                // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                    try {
                        updateInfoCount += updateAllStatusArrToStatus(status, RtnTagKey.SEND_SUCCESS);
                    } catch (Exception e) {
                        logger.error("�������ݺ���±�������Ϊ�ɹ�״̬�����쳣����鿴ϵͳ��־��");
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("�������ݺ���±�������Ϊ�ɹ�״̬�����쳣����鿴ϵͳ��־��");
                        return -1;
                    }
                } else {
                    // �ж��Ƿ����ظ���ˮ�ź��ʺţ����У�����ļ�¼״̬Ϊ���ͳɹ�
                    String sameid = (String) m1.get(RtnTagKey.SAMEID);
                    String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                    if ((sameid != null && !"".equals(sameid.trim()))
                            || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                        try {
                            updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
                        } catch (Exception e) {
                            logger.error("�������ݺ�����ظ����͵�����ʱ�����쳣����鿴ϵͳ��־��");
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage("�������ݺ�����ظ����͵�����ʱ�����쳣����鿴ϵͳ��־��");
                            return -1;
                        }
                    }
                }
            }
        }
        }
        //���ط��������ͷ��ͳɹ���¼��
        if (updateInfoCount != -1) {
            String send_update_count = String.valueOf(this.getSendInfoCount()) + "_" + String.valueOf(updateInfoCount);
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue(send_update_count);
            this.res.setType(4);
            this.res.setResult(true);
        } else {
            logger.error("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
            return -1;
        }

        return 0;

    }

    private List sendConsumeInfoByStatus(List cardList, String status) throws Exception {
        return sendConsumeInfoByStatusArr(cardList, new String[]{status});
    }

    // ����ĳ��״̬���������ݵ�������
    private List sendConsumeInfoByStatusArr(List cardList, String[] statusArr) throws Exception {
        List rtnlist = null;
        if (cardList.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = df.format(new Date());
            String year = strDate.substring(0, 4);
            BankService service = FaspServiceAdapter.getBankService();
            rtnlist = service.writeConsumeInfo("BANK.CCB", "8015", year, "405", cardList);
        }

        return rtnlist;
    }

    // ��ѯ����ĳһ״̬����������
    private List queryInfoByStatus(String status) {
        return queryInfoByStatusArr(new String[]{status});
    }

    // ��ѯ����ĳ��״̬����������
    // ��δע��
    private List queryInfoByStatusArr(String[] status) {

        this.sendInfoCount = 0;
        StringBuffer wheresqlbfr = new StringBuffer(" where csi.status in ('");
        int statusCount = status.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(status[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("') and cbi.status = '1' order by lsh ");
        String wheresql = new String(wheresqlbfr);
        String selectsql = "select lsh,csi.account as account,csi.cardname as cardname,busidate,busimoney,businame,limitdate,tx_cd from ls_consumeinfo csi "
                          +" join ls_cardbaseinfo cbi on csi.account = cbi.account "
                          + wheresql;

        System.out.println(selectsql);
        
        RecordSet rs = null;
        List cardList = new ArrayList();
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            this.sendInfoCount++;
            Map m = new HashMap();
            String lsh = rs.getString("lsh");
            String account = rs.getString("account").trim();
            String cardname = rs.getString("cardname").trim();
            String busidate = rs.getString("busidate");
            busidate = busidate.substring(0, 4) + busidate.substring(5, 7) + busidate.substring(8, 10);
            Double busimoney = rs.getDouble("busimoney");
            String businame = rs.getString("businame").trim();
            String limitdate = rs.getString("limitdate");
            limitdate = limitdate.substring(0, 4) + limitdate.substring(5, 7) + limitdate.substring(8, 10);
            String tx_cd = rs.getString("tx_cd");
            if (busimoney <= 0) {
                limitdate = "";
            }
            if ("43".equals(tx_cd)) {
                busimoney = -busimoney;
            }
            m.put("ID", lsh);
            m.put("ACCOUNT", account);
            m.put("CARDNAME", cardname);
            m.put("BUSIDATE", busidate);
            m.put("BUSIMONEY", busimoney);
            m.put("BUSINAME", businame);
            m.put("Limitdate", limitdate);
            cardList.add(m);
        }
        if (rs != null) {
            rs.close();
            rs = null;
        }
        return cardList;
    }

    // ����ĳһ��״̬Ϊ��״̬(10-��ʼ״̬,11-����ʧ��,12-���ͳ�ʱ,20-���ͳɹ�)
    private int updateAllStatusToStatus(String oldStatus, String newStatus) throws Exception {
        //String updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' where status='" + oldStatus + "' ";
        return updateAllStatusArrToStatus(new String[]{oldStatus}, newStatus);
    }


    // ����ĳ����״̬Ϊ��״̬(10-��ʼ״̬,11-����ʧ��,12-���ͳ�ʱ,20-���ͳɹ�)
    private int updateAllStatusArrToStatus(String[] oldStatus, String newStatus) throws Exception {
        StringBuffer wheresqlbfr = new StringBuffer(" where status in ('");
        int statusCount = oldStatus.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(oldStatus[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("')");
        String wheresql = new String(wheresqlbfr);
        String updateStatusOKSql = null;
        if (RtnTagKey.SEND_TIME_OUT.equals(newStatus.trim())) {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' ���ͳ�ʱ' " + wheresql;
        } else if (RtnTagKey.SEND_FAIL.equals(newStatus.trim())) {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' ����ʧ��' " + wheresql;
        } else {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' ���ͳɹ�' " + wheresql;
        }
        int rtn = dc.executeUpdate(updateStatusOKSql);
        dc.commit();
        return rtn;
    }

    //�����ظ����͵���ˮ�Ż����ʺţ���ԭ״̬��Ϊ���ͳɹ�
    private int updateSameIdRecordStatus(String sameid, String sameaccount) {
        String updateSameIdOKSql = "update ls_consumeinfo set status='20' where lsh='"
                + sameid + "' and account='" + sameaccount + "'";
        logger.info(updateSameIdOKSql);
        int rtn = dc.executeUpdate(updateSameIdOKSql);
        return rtn;
    }

    // ���ط��������ֵļ�¼��
    public int getSendInfoCount() {
        return this.sendInfoCount;
    }
}