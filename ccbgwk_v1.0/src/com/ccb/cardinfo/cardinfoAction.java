package com.ccb.cardinfo;

import com.ccb.consume.RtnTagKey;
import gov.mof.fasp.service.BankService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gwk
 * Date: 2010-8-5
 * Time: 15:53:23
 * To change this template use File | Settings | File Templates.
 */
public class cardinfoAction extends Action {

     // ��¼ÿ�η��������ֵ����ݼ�¼��
    private int sendInfoCount = 0;
    // ��ʱ��־
//    private boolean isTimeOut = false;
    //log
    private static final Log logger = LogFactory.getLog(cardinfoAction.class);
     /*   ����Ϣ
    <list>
    <map>
    <!������-->
    <string> ACCOUNT </string><string>value</string>
    <!���ֿ��� -->
    <string> CARDNAME </string><string>value</string>
    <!��Ԥ�㵥λ-->
    <string> BDGAGENCY </string><string>value</string>
    <!�������ʻ���-->
    <string> GATHERINGBANKACCTNAME </string>value</double>
    <!�������˻�������-->
    <string> GATHERINGBANKNAME </string><string>value</string>
    <!�����˻���-->
    <string> GATHERINGBANKACCTCODE </string><string>value</string>
    <!�����֤��-->
    <string> IDNUMBER </string><string>value</string>
    <!����;-->
    <string> DIGEST </string><string>value</string>
    <!���������У��������б���)-->
    <string> BANK </string><string>value</string>
    <!����������-->
    <string> CREATEDATE </string><string>value</string>
    <!����Ч��ʼ����-->
    <string> STARTDATE </string><string>value</string>
    <!����Ч��ֹ����-->
    <string> ENDDATE </string><string>value</string>
    <!�����ݲ�������-->
    <string> ACTION </string><string>value</string>
    
    </list>

     */
    public int writeCardInfo() {
        //����flag=0��δ���ͣ�������
       List cardList = queryInfoBySentFlag();
        int updateInfoCount = sendCardInfoByStatus(cardList);
        //���ͳɹ���¼��
        if (updateInfoCount != -1) {
            String send_update_count =  String.valueOf(updateInfoCount);
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue(send_update_count);
            this.res.setType(4);
            this.res.setResult(true);
        }
        return updateInfoCount;
    }
    //��ѯ δ���Ͳ��ҿ�״̬=1������
    public List queryInfoBySentFlag() {
         this.sendInfoCount = 0;

        String selectsql = "select account, cardname, bdgagency, gatheringbankacctname, gatheringbankname," +
                " gatheringbankacctcode, idnumber, digest, bank, createdate, startdate, enddate, action " +
                " from ls_cardbaseinfo where sentflag = '0' and status = '1' order by account";
        RecordSet rs = null;
        List cardList = new ArrayList();
        try {
            rs = dc.executeQuery(selectsql);
            while (rs.next()) {
                this.sendInfoCount++;
                Map m = new HashMap();
                String account = rs.getString("account").trim();
                String cardname = rs.getString("cardname").trim();
                String bgcy = rs.getString("cardname");
                String gatherbankacctname = rs.getString("gatheringbankacctname");
                String gatherbankname = rs.getString("gatheringbankname");
                String gatherbankacctcode = rs.getString("gatheringbankacctcode");
                String idnumber = rs.getString("idnumber");
                String digest = rs.getString("digest");
                String bank = rs.getString("bank");
                String createdate = rs.getString("createdate");
                String startdate = rs.getString("startdate");
                String enddate = rs.getString("enddate");
                String action = rs.getString("action");

                m.put("ACCOUNT", account);
                m.put("CARDNAME", cardname);
                m.put("BDGAGENCY", bgcy);
                m.put("GATHERINGBANKACCTNAME", gatherbankacctname);
                m.put("GATHERINGBANKNAME", gatherbankname);
                m.put("GATHERINGBANKACCTCODE", gatherbankacctcode);
                m.put("IDNUMBER", idnumber);
                m.put("DIGEST", digest);
                m.put("BANK", bank);
                m.put("CREATEDATE", createdate);
                m.put("STARTDATE", startdate);
                m.put("ENDDATE", enddate);
                m.put("ACTION", action);
                cardList.add(m);
            }

        } catch (Exception e) {
            logger.error("��ȡ����Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("��ȡ����Ϣ���ִ�����鿴ϵͳ��־��");
            return null;
        }
        finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        }
        return cardList;
    }
    //���Ϳ���Ϣ��״̬��δע����
    public int sendCardInfoByStatus(List cardList) {
        int updateInfoCount = 0;
        try {
            if (cardList.size() > 0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = df.format(new Date());
                String year = strDate.substring(0, 4);
//                BankService service = FaspServiceAdapter.getBankService();
                List rtnlist = null;
//                rtnlist = service.writeOfficeCard("BANK.CCB", "8015", year, "405", cardList);
                String te = "hh";
                rtnlist = new ArrayList();
                rtnlist.add(te);
                // 60��ʱ����������ؽ��Ϊ�գ������������ϢΪ���ͳ�ʱ
                if ( rtnlist == null || rtnlist.size() <= 0) {
//                    updateAllStatusArrToStatus(statusArr, RtnTagKey.SEND_FAIL);
//                    return -1;
                } else {
                    // ��������Ϣ
                    for (int i = 0; i < rtnlist.size(); i++) {
//                        Map m1 = (Map) rtnlist.get(i);
//                        String result = (String) m1.get(RtnTagVal.RESULT);
                        String result = "success";
                        // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                        if (RtnTagVal.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            updateInfoCount += updateAllsentFlagArr( RtnTagVal.SEND_SUCCESS);
                        } else {
                            // �ж��Ƿ����ظ���ˮ�ź��ʺţ����У�����ļ�¼״̬Ϊ���ͳɹ�
//                            String sameid = (String) m1.get(RtnTagVal.SAMEID);
//                            String sameaccount = (String) m1.get(RtnTagVal.SAMEACCOUNT);
                             String sameid = "1";
                            String sameaccount = "2";
                            if ((sameid != null && !"".equals(sameid.trim()))
                                    && (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                updateInfoCount += updateSameidRecordFlag(sameid, sameaccount);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("����������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("����������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        return updateInfoCount;
    }
    //����״̬Ϊ�ɹ�״̬
    public int updateAllsentFlagArr(String status) {

        String updateStatusOKSql = null;
        updateStatusOKSql = "update ls_cardbaseinfo set sentflag='1',txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' �ѷ���' " +
                " where sentflag='0' and status='"+ status+"'";
        int rtn = dc.executeUpdate(updateStatusOKSql);
        dc.commit();
        if (rtn < 0) {
            logger.error("���Ϳ���Ϣ�������ؼ�¼״̬���ִ�����鿴ϵͳ��־��");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("���Ϳ���Ϣ�������ؼ�¼״̬���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        return rtn;
    }
    //�����ظ���������
    public int updateSameidRecordFlag(String sameid,String sameacct) {
         String updateStatusOKSql = null;
        updateStatusOKSql = "update ls_cardbaseinfo set sentflag='1',txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' �ѷ���' " +
                " where account='" + sameacct +"' and idnumber='"+ sameid +"'";
        int rtn = dc.executeUpdate(updateStatusOKSql);
        dc.commit();
        if (rtn < 0) {
            logger.error("���Ϳ���Ϣ�������ؼ�¼״̬���ִ�����鿴ϵͳ��־��");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("���Ϳ���Ϣ�������ؼ�¼״̬���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        return rtn;
    }
}
