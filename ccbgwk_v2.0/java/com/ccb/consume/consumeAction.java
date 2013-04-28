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
import java.text.SimpleDateFormat;
import java.util.*;

public class consumeAction extends Action {

    // ��¼ÿ�η��������ֵ����ݼ�¼��
    private int sendInfoCount = 0;
    private static final Log logger = LogFactory.getLog(consumeAction.class);
    private String pubAreaCode = "";
    private String strRemark = "";
    private String strJudeFlag = "";


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
        //��ȡ��¼�û��������ţ�ֻ�з��е��û���Ȩ�޲鿴����֧�е����� 2012-11-26
        pubAreaCode = this.getOperator().getDeptid();
        //��ȡ��ע��������ݣ������admin���в鿴����֧�����ݵ�Ȩ�� 2012-11-26
        strRemark = this.getOperator().getFillstr150();
        //��ȡ�ж����� 2012-11-26
        strJudeFlag = PropertyManager.getProperty("pub.plat.admin.jude.flag");
        // ��ѯ���г�ʼ״̬��δ������������
        List cardList = null;
        HashMap mapConsume=null;
        int updateInfoCount = 0;
        try {
            //�����в����ֵ����Ѽ�¼��ȡ���� 2012-05-13 linyong
            mapConsume=queryInfoByStatusMap(RtnTagKey.SEND_INIT);
//            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        String areaCode="";
        this.sendInfoCount = 0;
        //�������й���ԱȨ��ʱ��ֻ�ܲ鿴��֧�е�����
        if(!strJudeFlag.equals(strRemark)){
            areaCode=pubAreaCode;
            //��������������룬��ȡ��Ҫ���͵������б� 2012-05-13 linyong
            cardList = (List)mapConsume.get(areaCode);
            if (cardList != null && cardList.size() > 0) {
                this.sendInfoCount = cardList.size();
                List rtnlist = null;
                try {
                    rtnlist = sendConsumeInfoByStatus(areaCode,cardList, RtnTagKey.SEND_INIT);
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
                        // �����м�������������� 2012-12-14 linyong
                        updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_FAIL,areaCode);
                    } catch (Exception e) {
                        logger.error("�������ݺ���±�������Ϊʧ��״̬ʧ�ܣ���鿴ϵͳ��־��");
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
                        return -1;
                    }

                } else {
                    // ��������Ϣ
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        if (result == null){
                            result = (String) m1.get("RESULT");
                        }
                        // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // �����м�������������� 2012-12-14 linyong
                                updateInfoCount += updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_SUCCESS,areaCode);
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
                            } else {
                                logger.error("Server response message:" + (String) m1.get("message"));
                            }
                        }
                    }
                }
            }
        }else{
            String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
            for (int j =0;j<strArr.length;j++){
                areaCode=strArr[j];
                //��������������룬��ȡ��Ҫ���͵������б� 2012-05-13 linyong
                cardList = (List)mapConsume.get(areaCode);
                if (cardList != null && cardList.size() > 0) {
                    this.sendInfoCount =this.sendInfoCount + cardList.size();
                    List rtnlist = null;
                    try {
                        rtnlist = sendConsumeInfoByStatus(areaCode,cardList, RtnTagKey.SEND_INIT);
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
                            // �����м�������������� 2012-12-14 linyong
                            updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_FAIL,areaCode);
                        } catch (Exception e) {
                            logger.error("�������ݺ���±�������Ϊʧ��״̬ʧ�ܣ���鿴ϵͳ��־��");
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
                            return -1;
                        }

                    } else {
                        // ��������Ϣ
                        for (int i = 0; i < rtnlist.size(); i++) {
                            Map m1 = (Map) rtnlist.get(i);
                            String result = (String) m1.get(RtnTagKey.RESULT);
                            if (result == null){
                                result = (String) m1.get("RESULT");
                            }
                            // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                            if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                                try {
                                    // �����м�������������� 2012-12-14 linyong
                                    updateInfoCount += updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_SUCCESS,areaCode);
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
                                } else {
                                    logger.error("Server response message:" + (String) m1.get("message"));
                                }
                            }
                        }
                    }
                }
            }
        }

        //���ط��������ͷ��ͳɹ���¼��
        if (updateInfoCount != -1) {
            String send_update_count = String.valueOf(this.sendInfoCount) + "_" + String.valueOf(updateInfoCount);
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

    private void writeObject(List tempList,String areaCode) {
        try {

            FileOutputStream outStream = new FileOutputStream("D:/CardResponse"+areaCode+".txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(tempList);
            for(int i=0;i<tempList.size();i++){
                Map m = new HashMap();
                m = (Map)tempList.get(i);
                objectOutputStream.writeObject(m);
            }
            outStream.close();
            System.out.println("successful");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  ���������쳣״̬��δ���ͳɹ��������ݵ�������
    public int writeConsumeExpInfo() {
        //��ȡ��¼�û��������ţ�ֻ�з��е��û���Ȩ�޲鿴����֧�е����� 2012-11-26
        pubAreaCode = this.getOperator().getDeptid();
        //��ȡ��ע��������ݣ������admin���в鿴����֧�����ݵ�Ȩ�� 2012-11-26
        strRemark = this.getOperator().getFillstr150();
        //��ȡ�ж����� 2012-11-26
        strJudeFlag = PropertyManager.getProperty("pub.plat.admin.jude.flag");
        // ��ѯ���г�ʼ״̬��δ������������
        List cardList = null;
        HashMap mapConsume=null;
        int updateInfoCount = 0;
        String[] status = new String[]{RtnTagKey.SEND_FAIL, RtnTagKey.SEND_TIME_OUT};
        try {
            //�����в����ֵ����Ѽ�¼��ȡ���� 2012-05-13 linyong
            mapConsume=queryInfoByStatusMap(status);
//            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }
        String areaCode="";
        this.sendInfoCount = 0;
        //�������й���ԱȨ��ʱ��ֻ�ܲ鿴��֧�е�����
        if(!strJudeFlag.equals(strRemark)){
            areaCode=pubAreaCode;
            //��������������룬��ȡ��Ҫ���͵������б� 2012-05-13 linyong
            cardList = (List)mapConsume.get(areaCode);
            if (cardList != null && cardList.size() > 0) {
                this.sendInfoCount = cardList.size();
                List rtnlist = null;
                try {
                    rtnlist = sendConsumeInfoByStatusArr(areaCode,cardList, status);
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
                        // �����м�������������� 2012-12-14 linyong
                        updateAllStatusArrToStatus(status, RtnTagKey.SEND_FAIL,areaCode);
                    } catch (Exception e) {
                        logger.error("�������ݺ���±�������Ϊʧ��״̬ʧ�ܣ���鿴ϵͳ��־��");
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
                        return -1;
                    }
                } else {
                    // ��������Ϣ
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        if (result == null){
                            result = (String) m1.get("RESULT");
                        }
                        // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // �����м�������������� 2012-12-14 linyong
                                updateInfoCount += updateAllStatusArrToStatus(status, RtnTagKey.SEND_SUCCESS,areaCode);
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
                            } else {
                                logger.error("Server response message:" + (String) m1.get("message"));
                            }
                        }
                    }
                }
            }
        }else{
            String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
            for (int j =0;j<strArr.length;j++){
                areaCode=strArr[j];
                //��������������룬��ȡ��Ҫ���͵������б� 2012-05-13 linyong
                cardList = (List)mapConsume.get(areaCode);
                if (cardList != null && cardList.size() > 0) {
                    this.sendInfoCount =this.sendInfoCount + cardList.size();
                    List rtnlist = null;
                    try {
                        rtnlist = sendConsumeInfoByStatusArr(areaCode,cardList, status);
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
                            // �����м�������������� 2012-12-14 linyong
                            updateAllStatusArrToStatus(status, RtnTagKey.SEND_FAIL,areaCode);
                        } catch (Exception e) {
                            logger.error("�������ݺ���±�������Ϊʧ��״̬ʧ�ܣ���鿴ϵͳ��־��");
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
                            return -1;
                        }

                    } else {
                        // ��������Ϣ
                        for (int i = 0; i < rtnlist.size(); i++) {
                            Map m1 = (Map) rtnlist.get(i);
                            String result = (String) m1.get(RtnTagKey.RESULT);
                            if (result == null){
                                result = (String) m1.get("RESULT");
                            }
                            // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                            if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                                try {
                                    // �����м�������������� 2012-12-14 linyong
                                    updateInfoCount += updateAllStatusArrToStatus(status, RtnTagKey.SEND_SUCCESS,areaCode);
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
                                } else {
                                    logger.error("Server response message:" + (String) m1.get("message"));
                                }
                            }
                        }
                    }
                }
            }
        }

        //���ط��������ͷ��ͳɹ���¼��
        if (updateInfoCount != -1) {
            String send_update_count = String.valueOf(this.sendInfoCount) + "_" + String.valueOf(updateInfoCount);
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

    private List sendConsumeInfoByStatus(String areaCode,List cardList, String status) throws Exception {
        return sendConsumeInfoByStatusArr(areaCode,cardList, new String[]{status});
    }

    // ����ĳ��״̬���������ݵ�������
    private List sendConsumeInfoByStatusArr(String areaCode,List cardList, String[] statusArr) throws Exception {
        List rtnlist = null;
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
        //����������������ȡ�������еı���
        strBank = PropertyManager.getProperty("ccb.code."+areaCode);
        longtuVer = PropertyManager.getProperty("longtu.version."+areaCode);
        admdivCode = PropertyManager.getProperty("admdiv.code."+areaCode);
        finOrgCode = PropertyManager.getProperty("fin.org.code."+areaCode);
        //���ݲ�ͬ�Ĵ����ȡ��Ӧ��ҵ��ϵͳ��ʶ 2012-10-29
        applicationid = PropertyManager.getProperty("application.id."+areaCode);
        if ("".equals(applicationid)){
            applicationid="BANK.CCB";
        }
        if (cardList.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = df.format(new Date());
            String year = strDate.substring(0, 4);
//            BankService service = FaspServiceAdapter.getBankService();
            //���������������������Ӧ�����ֵĽӿ�
            service = GwkBurlapServiceFactory.getInstance().getBankService(areaCode);
            try{
                if("v1".equals(longtuVer)){
                    rtnlist = service.writeConsumeInfo(applicationid, strBank, year, finOrgCode, cardList);
                }else if("v2".equals(longtuVer)){
                    rtnlist = service.writeConsumeInfo(applicationid, strBank, year,admdivCode,finOrgCode, cardList);
                }
                logger.info(strDate+" �����Ϊ:"+areaCode+"�Ĳ����ַ���������Ϣ�ɹ���");
            } catch (Exception ex){
                logger.error(strDate+" �����Ϊ:"+areaCode+"�Ĳ����ַ���������Ϣʧ�ܣ�"+ex.getMessage());
            }
//            rtnlist = service.writeConsumeInfo("BANK.CCB", strBank, year, "405", cardList);
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
                + " join ls_cardbaseinfo cbi on csi.account = cbi.account "
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
            m.put("id", lsh);
            m.put("account", account);
            m.put("cardname", cardname);
            m.put("busidate", busidate);
            m.put("busimoney", busimoney);
            m.put("businame", businame);
            m.put("limitdate", limitdate);
            cardList.add(m);
        }
        if (rs != null) {
            rs.close();
            rs = null;
        }
        return cardList;
    }

    // ��ѯ����ĳһ״̬����������
    private HashMap queryInfoByStatusMap(String status) {
        return queryInfoByStatusMap(new String[]{status});
    }

    // ��ѯ����ĳ��״̬����������
    // ��δע��
    // �����в����ֵ����Ѽ�¼��ȡ����
    private HashMap queryInfoByStatusMap(String[] status) {
        HashMap mapConsume = new HashMap();
        String strCode = PropertyManager.getProperty("finance.codeset");
        String[] strArr = strCode.split(",");
        String areaCode="";
        String areaName="";
        for (int j=0;j<strArr.length;j++){
            StringBuffer wheresqlbfr = new StringBuffer(" where csi.status in ('");
            int statusCount = status.length;
            for (int i = 0; i < statusCount; i++) {
                wheresqlbfr.append(status[i]);
                if (i + 1 < statusCount) {
                    wheresqlbfr.append("','");
                }
            }
            wheresqlbfr.append("')");

            areaCode=strArr[j];
//            areaName=PropertyManager.getProperty("finance.name."+areaCode);
//            wheresqlbfr.append(" and cbi.gatheringbankacctname='"+areaName+"' ");
            // ʹ��areacode 2012-12-14
            wheresqlbfr.append(" and csi.areacode='"+areaCode+"' ");
            wheresqlbfr.append(" and cbi.status = '1'  ");
            wheresqlbfr.append(" order by lsh");
            String wheresql = new String(wheresqlbfr);
            String selectsql = "select lsh,csi.account as account,csi.cardname as cardname,busidate,busimoney,businame,limitdate,tx_cd from ls_consumeinfo csi "
                    + " join ls_cardbaseinfo cbi on csi.account = cbi.account "
                    + wheresql;

            System.out.println(selectsql);

            RecordSet rs = null;
            List cardList = new ArrayList();
            rs = dc.executeQuery(selectsql);
            while (rs.next()) {

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
                m.put("id", lsh);
                m.put("account", account);
                m.put("cardname", cardname);
                m.put("busidate", busidate);
                m.put("busimoney", busimoney);
                m.put("businame", businame);
                m.put("limitdate", limitdate);
                cardList.add(m);
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if(cardList.size()>0){
                mapConsume.put(areaCode,cardList);
            }
        }
        return mapConsume;
    }

    // ����ĳһ��״̬Ϊ��״̬(10-��ʼ״̬,11-����ʧ��,12-���ͳ�ʱ,20-���ͳɹ�)
    private int updateAllStatusToStatus(String oldStatus, String newStatus) throws Exception {
        //String updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' where status='" + oldStatus + "' ";
        return updateAllStatusArrToStatus(new String[]{oldStatus}, newStatus);
    }

    // ����ĳһ��״̬Ϊ��״̬(10-��ʼ״̬,11-����ʧ��,12-���ͳ�ʱ,20-���ͳɹ�)
    // �����м����������� 2012-12-14 linyong
    private int updateAllStatusToStatus(String oldStatus, String newStatus,String areaCode) throws Exception {
        //String updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' where status='" + oldStatus + "' ";
        return updateAllStatusArrToStatus(new String[]{oldStatus}, newStatus,areaCode);
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

    // ����ĳ����״̬Ϊ��״̬(10-��ʼ״̬,11-����ʧ��,12-���ͳ�ʱ,20-���ͳɹ�)
    // ���������������Ϊ�ѷ��� 2012-12-14
    private int updateAllStatusArrToStatus(String[] oldStatus, String newStatus,String areaCode) throws Exception {
        StringBuffer wheresqlbfr = new StringBuffer(" where status in ('");
        int statusCount = oldStatus.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(oldStatus[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("')");
        wheresqlbfr.append(" and areacode = '"+areaCode+"'");
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

    public int sendAllConsumeInfo() {
        // ��ѯ���г�ʼ״̬��δ������������
        List cardList = null;
        HashMap mapConsume=null;
        int updateInfoCount = 0;
        try {
            //�����в����ֵ����Ѽ�¼��ȡ���� 2012-05-13 linyong
            mapConsume=queryInfoByStatusMap(RtnTagKey.SEND_INIT);
//            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��", e);
            return -1;
        }
        String areaCode="";
        String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
        for (int j =0;j<strArr.length;j++){
            areaCode=strArr[j];
            //��������������룬��ȡ��Ҫ���͵������б� 2012-05-13 linyong
            cardList = (List)mapConsume.get(areaCode);
            if (cardList != null && cardList.size() > 0) {
                List rtnlist = null;
                try {
                    rtnlist = sendConsumeInfoByStatus(areaCode,cardList, RtnTagKey.SEND_INIT);
                } catch (Exception e) {
                    logger.error("����������Ϣ���ִ�����鿴ϵͳ��־��", e);
                    return -1;
                }
                // �ж��Ƿ��з������ݣ����ޣ����������״̬Ϊ����ʧ��
                if (rtnlist == null || rtnlist.size() <= 0) {
                    try {
                        // �����м�������������� 2012-12-14 linyong
                        updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_FAIL,areaCode);
                    } catch (Exception e) {
                        logger.error("�������ݺ���±�������Ϊʧ��״̬ʧ�ܣ���鿴ϵͳ��־��");
                    }
                    return -1;
                } else {
                    // ��������Ϣ
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        if (result == null){
                            result = (String) m1.get("RESULT");
                        }
                        // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // �����м�������������� 2012-12-14 linyong
                                updateInfoCount += updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_SUCCESS,areaCode);
                            } catch (Exception e) {
                                logger.error("�������ݺ���±�������Ϊʧ��״̬�����쳣����鿴ϵͳ��־��");
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
                                    return -1;
                                }
                            } else {
                                logger.error("Server response message:" + (String) m1.get("message"));
                            }
                        }
                    }
                }
            }
        }

        //���ط��������ͷ��ͳɹ���¼��
        if (updateInfoCount != -1) {
        } else {
            logger.error("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
            return -1;
        }
        return 0;
    }

    public int sendAllConsumeExpInfo() {
        // ��ѯ���г�ʼ״̬��δ������������
        List cardList = null;
        HashMap mapConsume=null;
        int updateInfoCount = 0;
        String[] status = new String[]{RtnTagKey.SEND_FAIL, RtnTagKey.SEND_TIME_OUT};
        try {
            //�����в����ֵ����Ѽ�¼��ȡ���� 2012-05-13 linyong
            mapConsume=queryInfoByStatusMap(status);
//            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��", e);
            return -1;
        }
        String areaCode="";
        String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
        for (int j =0;j<strArr.length;j++){
            areaCode=strArr[j];
            //��������������룬��ȡ��Ҫ���͵������б� 2012-05-13 linyong
            cardList = (List)mapConsume.get(areaCode);
            if (cardList != null && cardList.size() > 0) {
                List rtnlist = null;
                try {
                    rtnlist = sendConsumeInfoByStatusArr(areaCode,cardList,status);
                } catch (Exception e) {
                    logger.error("����������Ϣ���ִ�����鿴ϵͳ��־��", e);
                    return -1;
                }
                // �ж��Ƿ��з������ݣ����ޣ����������״̬Ϊ����ʧ��
                if (rtnlist == null || rtnlist.size() <= 0) {
                    try {
                        // �����м�������������� 2012-12-14 linyong
                        updateAllStatusArrToStatus(status, RtnTagKey.SEND_FAIL,areaCode);
                    } catch (Exception e) {
                        logger.error("�������ݺ���±�������Ϊʧ��״̬ʧ�ܣ���鿴ϵͳ��־��");
                    }
                    return -1;
                } else {
                    // ��������Ϣ
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        if (result == null){
                            result = (String) m1.get("RESULT");
                        }
                        // ͨ���ж�result��ֵ�ж��Ƿ��ͳɹ�����ȫ���ɹ��򷵻�һ��result==success��ֵ
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // �����м�������������� 2012-12-14 linyong
                                updateInfoCount += updateAllStatusArrToStatus(status, RtnTagKey.SEND_SUCCESS,areaCode);
                            } catch (Exception e) {
                                logger.error("�������ݺ���±�������Ϊʧ��״̬�����쳣����鿴ϵͳ��־��");
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
                                    return -1;
                                }
                            } else {
                                logger.error("Server response message:" + (String) m1.get("message"));
                            }
                        }
                    }
                }
            }
        }

        //���ط��������ͷ��ͳɹ���¼��
        if (updateInfoCount != -1) {
        } else {
            logger.error("�������ݺ���±�������״̬ʧ�ܣ���鿴ϵͳ��־��");
            return -1;
        }
        return 0;
    }
}