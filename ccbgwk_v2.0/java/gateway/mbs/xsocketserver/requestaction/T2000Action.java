package gateway.mbs.xsocketserver.requestaction;

import com.ccb.dao.LSPAYBACKINFO;
import gateway.mbs.xsocketserver.RequestAction;
import gateway.mbs.xsocketserver.domain.RequestData;
import gateway.mbs.xsocketserver.domain.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.utils.BusinessDate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:27:07
 * To change this template use File | Settings | File Templates.
 */
public class T2000Action implements RequestAction {
    private Log logger = LogFactory.getLog(this.getClass());

    private static final String txnCode = "2000";
    private RequestData requestData;
    private List<RequestData> requestDataList;
    private ResponseData responseData = new ResponseData();

    //��ɫƽ̨��Ӧ���ܱ���
    private int totalCount = 0;
    //��ɫƽ̨��Ӧ���ܽ��
    private double totalAmt = 0.00;
    private String voucherid;
    private String year;
    private String paybackdate; //yyyy-MM-dd

    private List<LSPAYBACKINFO> paybackInfos = new ArrayList();

    public T2000Action() {

    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setRequestDataList(List<RequestData> requestDataList) {
        this.requestDataList = requestDataList;
    }

    /**
     * �����������,��������,���ش�����Ϣ��301
     * 1 �ֺܷ˶�
     * 2  ���ݿ���ϸ�˶�
     * body�ַ�����ȡ�޸ģ�52 -> 51�� 2010/07/13 haiyu
     */
    public void process() {
        String bodyAll = "";       //���а��İ��壨��һ������[����]��[�ܽ��]���⣩���
        int count = 0;
        for (RequestData requestData : requestDataList) {
            count++;
            String body = requestData.getBodyData();
            if (count == 1) {
                this.requestData = requestData; //2010/07/15 haiyu 
            }
             bodyAll += body;
        }
        logger.info("����2000 ���:" + bodyAll);
        year = bodyAll.substring(0, 4).trim();
        voucherid = bodyAll.substring(4, 24).trim();
        paybackdate = bodyAll.substring(24, 32).trim();

        totalCount = Integer.parseInt(bodyAll.substring(32, 38).trim());
        totalAmt = Double.parseDouble(bodyAll.substring(38, 51).trim());
        String tempBody = "";
        int temp = 51;
        for (int i = 1;i <= totalCount;i++) {
            tempBody = bodyAll.substring(temp,temp+51);
            getPaybackInfoListFromRequestDataBody(tempBody);  //��ȡ������ 
            temp = temp + 51;
            logger.info("����2000 ��"+i+"��:" + tempBody);
        }
        //check
        if (checkTotalRequestData()) {
            if (checkExistRequestData()) {
                saveDataToDB();
                setResponseDataHeader("000");
            } else {
                setResponseDataHeader("100");   //�������ݲ�����
            }
        } else {
            setResponseDataHeader("301");   //�ֽܷ���
        }
    }

    /**
     * �������� ��ͻ��˷��ػ�ִ:
     * 2010/07/15 haiyu
     */

    public void noProcess(){
        RequestData reqData = (RequestData)requestDataList.get(0);
        this.requestData = reqData;
        setResponseDataHeader("000");
    }
    private void getPaybackInfoListFromRequestDataBody(String body) {

        int ptr = 0;
        LSPAYBACKINFO info = new LSPAYBACKINFO();
        info.setAccount(body.substring(ptr, ptr + 20).trim());
        ptr += 20;
        info.setAmt(Double.parseDouble(body.substring(ptr, ptr + 11).trim()));
        ptr += 11;
        String result = body.substring(ptr, ptr + 20).trim();
        ptr += 20;
        if ("000000".equals(result)) { //�ۿ�ɹ�
            info.setStatus("01"); //�ۿ�ɹ�
        }
        paybackInfos.add(info);
    }

    /**
     * �ܷ�(�ܽ��)���
     * remark:��ȡ���е�ÿ����¼�Ľ��֮�� �� ���еı�ͷ�е��ܽ����� ��
     * @return
     */
    private boolean checkTotalRequestData() {

        int count = 0;
        double amt = 0.00;

        for (LSPAYBACKINFO info : paybackInfos) {
            count++;
            amt += info.getAmt();
        }
        if (amt != totalAmt) {
            //����301���� ����
            logger.error("����T2000:�ֺܷ˶Բ���.");
            return false;
        } else {
            return true;
        }
    }

    //TODO

    /**
     * 2010/07/13 haiyu
     * ���ݴ�����񡡼��
     * remark��
     *     ��ѯ������
     *        ��������֧����ţ�����֧����ȣ��������ţ��������
     *
     */
    private boolean checkExistRequestData() {
        boolean exist = true;
        LSPAYBACKINFO paybackinfo = new LSPAYBACKINFO();
        for (LSPAYBACKINFO info : paybackInfos) {
            String sqlWhere = " where voucherid = '" + voucherid + "' and year = '" + year +
                    "' and account = '" + info.getAccount() + "' and amt=" + info.getAmt();
            logger.info("����T2000:"+sqlWhere);
            paybackinfo = (LSPAYBACKINFO) paybackinfo.findFirstByWhere(sqlWhere);
            if (paybackinfo == null){
                if (exist){
                    exist = false;
                }
                logger.error("����T2000�����ݼ�顡���������ݿ��в����ڡ�"+ "֧����ţ�"+voucherid +"����ȣ�"+year+
                        "�����ţ�"+info.getAccount() +"����"+info.getAmt());
            }
        }
        return exist;
    }

    /**
     *  2010/07/13 haiyu
     * ��ѯ����׷�ӣ�  ��������֧����ţ�����֧����ȣ��������ţ��������
     */
    private void saveDataToDB() {
        LSPAYBACKINFO paybackinfo = new LSPAYBACKINFO();
        for (LSPAYBACKINFO info : paybackInfos) {
            String sqlWhere = " where voucherid = '" + voucherid + "' and year = '" + year +
                    "' and account = '" + info.getAccount() + "' and amt=" + info.getAmt();
            paybackinfo = (LSPAYBACKINFO) paybackinfo.findFirstByWhere(sqlWhere);
            if (paybackinfo != null) {
                paybackinfo.setPaybackdate(info.getPaybackdate());
                paybackinfo.setStatus(info.getStatus());
                paybackinfo.setRemark(info.getRemark());
                paybackinfo.setOperdate(BusinessDate.getTodaytime());    //haiyu 2010/07/21
                paybackinfo.setPaybackdate(BusinessDate.getToday());
                paybackinfo.updateByWhere(sqlWhere);
            }else{
                logger.error("����T2000:����ɹ�״̬���³�������,δ�ҵ���Ӧ��ϸ��¼." + sqlWhere);
                //TODO  ���ش���ƽ̨�쳣
            }
        }

    }

    private void setResponseDataHeader(String errCode) {
        //��ͷ����
        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("0"); //�޺�����
        responseData.setTxnCode(txnCode);
        responseData.setErrCode(errCode);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");

        //����������
        responseData.setLength(65);
        String strlength = StringUtils.leftPad(String.valueOf(65), 4, ' ');
        responseData.setPkgLength(strlength);

    }


}