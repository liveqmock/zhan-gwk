package com.ccb.cardinfo;

import com.ccb.consume.RtnTagKey;
import gateway.financebureau.BankService;
import gateway.financebureau.GwkBurlapServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * To change this template use File | Settings | File Templates.
 */
public class SendCardbaseAction extends Action {
    // ��¼���͵Ŀ���Ϣ��¼��
    private int sendCrdTotalCnt = 0;

    // ��¼���ͳɹ��Ŀ���Ϣ��¼��
    private int sendCrdSucCnt = 0;

    private static final Log logger = LogFactory.getLog(SendCardbaseAction.class);
    private String pubAreaCode = "";
    private String strRemark = "";
    private String strJudeFlag = "";

    // ����δ���͵Ŀ���Ϣ
    public int sendCrdbaseInfos() throws MalformedURLException {
        //��ȡ��¼�û��������ţ�ֻ�з��е��û���Ȩ�޲鿴����֧�е����� 2012-11-26
        pubAreaCode = this.getOperator().getDeptid();
        //��ȡ��ע��������ݣ������admin���в鿴����֧�����ݵ�Ȩ�� 2012-11-26
        strRemark = this.getOperator().getFillstr150();
        //��ȡ�ж����� 2012-11-26
        strJudeFlag = PropertyManager.getProperty("pub.plat.admin.jude.flag");
        // ��ѯls_cardbaseinfo����δ���͵�����ʹ�õĿ�����
        List cardList = null;
        HashMap mapSendCardInfo = null;
        try {
            mapSendCardInfo = querySendCrdsMap();
//            cardList = querySendCrds();
        } catch (Exception e) {
            logger.error("��ѯ��δ���͵Ŀ���Ϣ����ʱ�����쳣����鿴ϵͳ��־��");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("��ѯ��δ���͵Ŀ���Ϣ����ʱ�����쳣����鿴ϵͳ��־��");
            return -1;
        }
        String areaCode="";
        this.sendCrdTotalCnt =0;
        //�������й���ԱȨ��ʱ��ֻ�ܲ鿴��֧�е�����
        if(!strJudeFlag.equals(strRemark)){
            List rtnlist = null;
            areaCode=pubAreaCode;
            //��������������룬��ȡ��Ҫ���͵ļ�¼ 2012-05-13 linyong
            cardList = (List)mapSendCardInfo.get(areaCode);
            if(cardList != null && cardList.size() > 0){
                this.sendCrdTotalCnt = cardList.size();
                rtnlist = this.sendCrdInfos(areaCode,cardList);
            }
            if (rtnlist != null && rtnlist.size() > 0) {
                // ��������Ϣ
                for (int i = 0; i < rtnlist.size(); i++) {
                    Map m1 = (Map) rtnlist.get(i);
                    String result = (String) m1.get(RtnTagKey.RESULT);
                    // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                    if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                        try {
                            // ���±�������״̬
                            // �����м�������������� 2012-12-14 linyong
                            this.sendCrdSucCnt += updateSentFlag("1",areaCode,"");
                        } catch (Exception e) {
                            logger.error("�������ݺ���±�������Ϊ�ѷ���״̬�����쳣����鿴ϵͳ��־��");
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage("�������ݺ���±�������Ϊ�ѷ���״̬�����쳣����鿴ϵͳ��־��");
                            return -1;
                        }
                    } else {
                        // �ж��Ƿ����ظ��ʺź����֤�ţ����У�����ļ�¼״̬Ϊ�ѷ���
                        String sameid = (String) m1.get("sameidnumber");
                        String sameaccount = (String) m1.get("sameaccount");
                        if ((sameid != null && !"".equals(sameid.trim()))
                                && (sameaccount != null && !"".equals(sameaccount.trim()))) {
                            try {
                                this.sendCrdSucCnt += updateSentFlag(sameid, sameaccount);
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
        } else {
            String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
            for (int j =0;j<strArr.length;j++){
                List rtnlist = null;
                areaCode=strArr[j];
                //��������������룬��ȡ��Ҫ���͵ļ�¼ 2012-05-13 linyong
                cardList = (List)mapSendCardInfo.get(areaCode);

                if(cardList != null && cardList.size() > 0){
                    this.sendCrdTotalCnt =this.sendCrdTotalCnt + cardList.size();
                    rtnlist = this.sendCrdInfos(areaCode,cardList);
                }
                if (rtnlist != null && rtnlist.size() > 0) {
                    // ��������Ϣ
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // ���±�������״̬
                                // �����м�������������� 2012-12-14 linyong
                                this.sendCrdSucCnt += updateSentFlag("1",areaCode,"");
                            } catch (Exception e) {
                                logger.error("�������ݺ���±�������Ϊ�ѷ���״̬�����쳣����鿴ϵͳ��־��");
                                this.res.setType(0);
                                this.res.setResult(false);
                                this.res.setMessage("�������ݺ���±�������Ϊ�ѷ���״̬�����쳣����鿴ϵͳ��־��");
                                return -1;
                            }
                        } else {
                            // �ж��Ƿ����ظ��ʺź����֤�ţ����У�����ļ�¼״̬Ϊ�ѷ���
                            String sameid = (String) m1.get("sameidnumber");
                            String sameaccount = (String) m1.get("sameaccount");
                            if ((sameid != null && !"".equals(sameid.trim()))
                                    && (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                try {
                                    this.sendCrdSucCnt += updateSentFlag(sameid, sameaccount);
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
        }
        String send_update_count = String.valueOf(this.sendCrdTotalCnt)+ "_" + String.valueOf(this.sendCrdSucCnt);
        this.res.setFieldName("rtnCnt");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(send_update_count);
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }

    private void writeObject(List tempList,String areaCode) {
        try {

            FileOutputStream outStream = new FileOutputStream("D:/CardResponse"+areaCode+".txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            for(int i=0;i<tempList.size();i++){
                Map m = new HashMap();
                m = (Map)tempList.get(i);
                objectOutputStream.writeUnshared(m);
            }
            outStream.close();
            System.out.println("successful");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ��ѯδ���͵�����������
    private List querySendCrds() {
        List cardList = new ArrayList();
        String querySql = "select * from ls_cardbaseinfo where sentflag = '0'";
        RecordSet rs = dc.executeQuery(querySql);
        if (rs != null) {
            while (rs.next()) {
                this.sendCrdTotalCnt++;
                Map m = new HashMap();

                String account = rs.getString("account").trim();
                String cardname = rs.getString("cardname").trim();
                String bdgagency = rs.getString("bdgagency").trim();
                String GATHERINGBANKACCTNAME = rs.getString("GATHERINGBANKACCTNAME").trim();
                String GATHERINGBANKNAME = rs.getString("GATHERINGBANKNAME").trim();
                String GATHERINGBANKACCTCODE = rs.getString("GATHERINGBANKACCTCODE").trim();
                String IDNUMBER = rs.getString("IDNUMBER").trim();
                String DIGEST = rs.getString("DIGEST").trim();
                String BANK = rs.getString("BANK").trim();
                String CREATEDATE = rs.getString("CREATEDATE").trim();
                String Startdate = rs.getString("Startdate").trim();
                String enddate = rs.getString("enddate").trim();
                String action = rs.getString("action").trim();


                m.put("ACCOUNT", account);
                m.put("CARDNAME", cardname);
                m.put("Bdgagency", bdgagency);
                m.put("GATHERINGBANKACCTNAME", GATHERINGBANKACCTNAME);
                m.put("GATHERINGBANKNAME", GATHERINGBANKNAME);
                m.put("GATHERINGBANKACCTCODE", GATHERINGBANKACCTCODE);
                m.put("IDNUMBER", IDNUMBER);
                m.put("DIGEST", DIGEST);
                m.put("BANK", BANK);
                m.put("CREATEDATE", CREATEDATE);
                m.put("Startdate", Startdate);
                m.put("enddate", enddate);
                m.put("action", action);
                cardList.add(m);
            }
        }
        return cardList;
    }
    // ��ѯδ���͵����������� 2012-05-13 linyong
    private HashMap querySendCrdsMap() {
        HashMap mapSendCardInfo = new HashMap();
        String strCode = PropertyManager.getProperty("finance.codeset");
        String[] strArr = strCode.split(",");
        String areaCode="";
        String areaName="";
        for (int i=0; i<strArr.length;i++){
            areaCode=strArr[i];
//            areaName=PropertyManager.getProperty("finance.name."+areaCode);
            List cardList = new ArrayList();
//            String querySql = "select * from ls_cardbaseinfo where sentflag = '0' and gatheringbankacctname='"+areaName+"'";
            // ʹ��areacode 2012-12-14
            String querySql = "select * from ls_cardbaseinfo where sentflag = '0' and areacode='"+areaCode+"'";
            RecordSet rs = dc.executeQuery(querySql);
            if (rs != null) {
                while (rs.next()) {
                    Map m = new HashMap();
                    String account = rs.getString("account").trim();
                    String cardname = rs.getString("cardname").trim();
                    String bdgagency = rs.getString("bdgagency").trim();
                    String gatheringbankacctname = rs.getString("gatheringbankacctname").trim();
                    String gatheringbankname = rs.getString("gatheringbankname").trim();
                    String gatheringbankacctcode = rs.getString("gatheringbankacctcode").trim();
                    String idnumber = rs.getString("idnumber").trim();
                    String digest = rs.getString("digest").trim();
                    String bank = rs.getString("bank").trim();
                    String createdate = rs.getString("createdate").trim();
                    createdate = createdate.substring(0, 4) + createdate.substring(5, 7) + createdate.substring(8, 10);
                    String startdate = rs.getString("startdate").trim();
                    startdate = startdate.substring(0, 4) + startdate.substring(5, 7) + startdate.substring(8, 10);
                    String enddate = rs.getString("enddate").trim();
                    enddate = enddate.substring(0, 4) + enddate.substring(5, 7) + enddate.substring(8, 10);
                    String action = rs.getString("action").trim();


                    m.put("account", account);
                    m.put("cardname", cardname);
                    m.put("bdgagency", bdgagency);
                    m.put("gatheringbankacctname", gatheringbankacctname);
                    m.put("gatheringbankname", gatheringbankname);
                    m.put("gatheringbankacctcode", gatheringbankacctcode);
                    m.put("idnumber", idnumber);
                    m.put("digest", digest);
                    m.put("bank", bank);
                    m.put("createdate", createdate);
                    m.put("startdate", startdate);
                    m.put("enddate", enddate);
                    m.put("action", action);
                    cardList.add(m);
                }
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if(cardList.size()>0){
                mapSendCardInfo.put(areaCode,cardList);
            }
        }
        return mapSendCardInfo;
    }
    // ���Ϳ���Ϣ��������
    private List sendCrdInfos(String areaCode,List cardList) throws MalformedURLException {
        List rtnList = null;
        BankService service=null;
        //���д���
        String strBank = "";
        //��ͼ�ӿڰ汾�� 2012-10-29
        String longtuVer = "";
        //ҵ��ϵͳ��ʾ 2012-10-29
        String applicationid = "";
        //������������ 2012-10-29
        String admdivCode="";
        //�������� 2012-10-29
        String finOrgCode="";
        //����������������ȡ�������еı��� 2012-05-13 linyong
        strBank = PropertyManager.getProperty("ccb.code."+areaCode);
        longtuVer = PropertyManager.getProperty("longtu.version."+areaCode);
        admdivCode = PropertyManager.getProperty("admdiv.code."+areaCode);
        finOrgCode = PropertyManager.getProperty("fin.org.code."+areaCode);

        //���ݲ�ͬ�Ĵ����ȡ��Ӧ��ҵ��ϵͳ��ʶ 2012-10-29
        applicationid = PropertyManager.getProperty("application.id."+areaCode);
        if ("".equals(applicationid)){
            applicationid="BANK.CCB";
        }

        if (cardList != null && cardList.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = df.format(new Date());
            String year = strDate.substring(0, 4);
            //BankService service = FaspServiceAdapter.getBankService();
            // ���� areaCode
            try{
                service = GwkBurlapServiceFactory.getInstance().getBankService(areaCode);
                if("v1".equals(longtuVer)){
                    rtnList = service.writeOfficeCard(applicationid, strBank, year, finOrgCode, cardList);
                }else if("v2".equals(longtuVer)){
                    rtnList = service.writeOfficeCard(applicationid, strBank, year, admdivCode,finOrgCode, cardList);
                }
                logger.info(strDate+" �����Ϊ:"+areaCode+"�Ĳ����ַ��Ϳ���Ϣ�ɹ���");
            } catch (Exception ex){
                logger.error(strDate+" �����Ϊ:"+areaCode+"�Ĳ����ַ��Ϳ���Ϣʧ�ܣ�"+ex.getMessage());
            }
            //rtnList = SendConsumeInfoTest.sendConsumeInfoRtn();
        }
        return rtnList;
    }

    // ����Ϊ�ѷ���
    private int updateSentFlag(String status){
        int rtnCnt = 0;
        String updateSql = "update ls_cardbaseinfo set sentflag = '1' where status = '"+status+"' and sentflag = '0'";
        rtnCnt = dc.executeUpdate(updateSql);
        return rtnCnt;
    }
    //���������������Ϊ�ѷ��� 2012-12-14
    private int updateSentFlag(String status,String areaCode,String tmpStr){
        int rtnCnt = 0;
        String updateSql = "update ls_cardbaseinfo set sentflag = '1' where status = '"+status+"' and sentflag = '0'"+
                " and areaCode='"+areaCode+"'";
        rtnCnt = dc.executeUpdate(updateSql);
        return rtnCnt;
    }
     private int updateSentFlag(String sameid,String sameaccount){
        int rtnCnt = 0;
        String updateSql = "update ls_cardbaseinfo set sentflag = '1' where account = '"+sameaccount+
                           "' and idnumber = '"+sameid+"'and sentflag = '0'";
        rtnCnt = dc.executeUpdate(updateSql);
        return rtnCnt;
    }

    public int sendAllCardInfo() throws MalformedURLException {
        // ��ѯls_cardbaseinfo����δ���͵�����ʹ�õĿ�����
        List cardList = null;
        HashMap mapSendCardInfo = null;
        try {
            mapSendCardInfo = querySendCrdsMap();
//            cardList = querySendCrds();
        } catch (Exception e) {
            logger.error("��ѯ��δ���͵Ŀ���Ϣ����ʱ�����쳣����鿴ϵͳ��־��");
            return -1;
        }
        String areaCode="";
        String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
        for (int j =0;j<strArr.length;j++){
            List rtnlist = null;
            areaCode=strArr[j];
            //��������������룬��ȡ��Ҫ���͵ļ�¼ 2012-05-13 linyong
            cardList = (List)mapSendCardInfo.get(areaCode);

            if(cardList != null && cardList.size() > 0){
                rtnlist = this.sendCrdInfos(areaCode,cardList);
            }
            if (rtnlist != null && rtnlist.size() > 0) {
                // ��������Ϣ
                for (int i = 0; i < rtnlist.size(); i++) {
                    Map m1 = (Map) rtnlist.get(i);
                    String result = (String) m1.get(RtnTagKey.RESULT);
                    // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                    if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                        try {
                            // ���±�������״̬
                            // �����м�������������� 2012-12-14 linyong
                            this.sendCrdSucCnt += updateSentFlag("1",areaCode,"");
                        } catch (Exception e) {
                            logger.error("�������ݺ���±�������Ϊ�ѷ���״̬�����쳣����鿴ϵͳ��־��");
                            return -1;
                        }
                    } else {
                        // �ж��Ƿ����ظ��ʺź����֤�ţ����У�����ļ�¼״̬Ϊ�ѷ���
                        String sameid = (String) m1.get("sameidnumber");
                        String sameaccount = (String) m1.get("sameaccount");
                        if ((sameid != null && !"".equals(sameid.trim()))
                                && (sameaccount != null && !"".equals(sameaccount.trim()))) {
                            try {
                                this.sendCrdSucCnt += updateSentFlag(sameid, sameaccount);
                            } catch (Exception e) {
                                logger.error("�������ݺ�����ظ����͵�����ʱ�����쳣����鿴ϵͳ��־��");
                                return -1;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }
}
