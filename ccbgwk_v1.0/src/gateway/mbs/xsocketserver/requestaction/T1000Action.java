package gateway.mbs.xsocketserver.requestaction;

import com.ccb.dao.LSPAYBACKINFO;
import gateway.mbs.xsocketserver.RequestAction;
import gateway.mbs.xsocketserver.domain.RequestData;
import gateway.mbs.xsocketserver.domain.ResponseData;
import gov.mof.fasp.service.CommonQueryService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:27:07
 * To change this template use File | Settings | File Templates.
 */
public class T1000Action implements RequestAction {
    private Log logger = LogFactory.getLog(this.getClass());

    /*
    ����Э�� ÿ�����Ȳ�����1024�ֽ�
    ÿ��������Ϣ����Ϊ51�ֽ�
    �ʣ��ݶ�ÿ����¼�� Ϊ10
     */
    private static final int recordNum = 10;

    /*
    ��ǰϵͳ����
     */
    private String currDate;


    private RequestData requestData;
    private ResponseData responseData = new ResponseData();

    private List<ResponseData> responseDataList = new ArrayList();

    //��Ӧ�������־
    boolean isMultiResponsePkg = false;

    //��Ӧ��ͷ����
    static final private int headLength = 65;

    //���ʻ����¼���� �� ����20 ���� 20 ��� 11��
    static final private int oneRecordLength = 51;

    //��Ӧ������ �ܼƲ��ֳ��� ���ܱ��� 6  �ܽ��13
    static final private int bodyHeadFieldlength = 19;

    //��ɫƽ̨��Ӧ���ܱ���
    private int totalCount = 0;
    //��ɫƽ̨��Ӧ���ܽ��
    private long totalAmt = 0;

    public T1000Action(RequestData requestData) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.currDate = sdf.format(new Date());
        this.requestData = requestData;
    }

    public List<ResponseData> getResponseDataList() {
        return responseDataList;
    }


    //=================

    public void process() {
        //
        String body = requestData.getBodyData();
        logger.info("����1000 BODY:" + body);

        String year = body.substring(0, 4).trim();
        String voucherid = body.substring(4, 24).trim();

        if (year.equals("")) {
            year = currDate.substring(0, 4);
        }
        List rtnlist = null;
        try {
            rtnlist = queryConsumeInfo(voucherid, year);
        } catch(Exception e) {
            logger.error("����1000��������ʱ����:");
            logger.error(e);
            //012 ͨѶ��ʱ
            setErrorResponseData("012");
            responseDataList.add(this.responseData);
        }

        if (rtnlist != null) {
            if (rtnlist.size() > 0) {
                List<LSPAYBACKINFO> paybackinfos = getPaybackInfoList(rtnlist);
                generateResponseData(paybackinfos);
                savePaybackinfoToDB(paybackinfos);
            } else {
                //���ݲ����� ���ؿհ�: ��Ӧ��=100
                logger.error("����1000�������ݲ�����:֧����� " + voucherid + ",��� " + year);
                setErrorResponseData("100");
                responseDataList.add(this.responseData);
                
            }
        }
        logger.info(voucherid + " " + year);

    }
    //ȡ������
    private List queryConsumeInfo(String voucherid, String year) {
        CommonQueryService service = FaspServiceAdapter.getCommonQueryService();
        Map m = new HashMap();
        m.put("VOUCHERID", voucherid);

        List rtnlist = null;
        try {
            rtnlist = service.getQueryListBySql("BANK.CCB", "queryConsumeInfo", m, year);
        } catch (Exception e) {
            logger.error(e);
            //TODO
            throw new RuntimeException(e);
        }
        return rtnlist;
    }
    /**
     * �Բ����ֻ�ȡ������Ϣ
     * �¼��ֶ�(�������,������)�趨ֵ 2010/07/13 haiyu
     *  setYear
     * @param burlapRtnList
     * @return
     */
    private List<LSPAYBACKINFO> getPaybackInfoList(List burlapRtnList) {
        String branchId = requestData.getBranchId();  //������
        String areaCode = requestData.getAreaCode();  //�����ֱ��
        List<LSPAYBACKINFO> paybackInfos = new ArrayList();

        for (int i = 0; i < burlapRtnList.size(); i++) {
            Map m = (Map) burlapRtnList.get(i);

            String voucherid = (String) m.get("VOUCHERID");
            String account = (String) m.get("ACCOUNT");
            String cardname = (String) m.get("CARDNAME");
            String amtstr = (String) m.get("Amt");
            String year = (String)m.get("year");
            long amt = (long) Double.parseDouble(amtstr) * 100;

            LSPAYBACKINFO paybackInfo = new LSPAYBACKINFO();
            paybackInfo.setVoucherid(voucherid.trim());
            paybackInfo.setAccount(account.trim());
            paybackInfo.setCardname(cardname.trim());
            paybackInfo.setAmt((double) amt / 100);
            paybackInfo.setQuerydate(currDate);
            //�¼��ֶ�(�����ֱ��,�������,������)�趨ֵ 2010/07/13 haiyu
            paybackInfo.setYear(year);
            paybackInfo.setBranchid(branchId);
            paybackInfo.setAreacode(areaCode);
            paybackInfos.add(paybackInfo);
            totalCount++;
            totalAmt += amt;
        }
        return paybackInfos;
    }


    private void savePaybackinfoToDB(List<LSPAYBACKINFO> paybackInfos) {

        for (LSPAYBACKINFO info : paybackInfos) {
            info.insert();
        }

    }


    /**
     * ����ȫ����Ӧ���б�
     * 
     * @param paybackInfos �Բ����ֲ�ѯ���صĿۿ������б�
     */
    private void generateResponseData(List<LSPAYBACKINFO> paybackInfos) {
        //ÿ�����ݰ��м�¼����             
        int count = 0;
        int listsize = paybackInfos.size();
        if (listsize > this.recordNum) {
            isMultiResponsePkg = true;
        } else {
            isMultiResponsePkg = false;
        }
        String body = null;
        if (isMultiResponsePkg) {
            //��һ����
            body = getResponseDataBodyContent(paybackInfos, 0, this.recordNum);
            responseDataList.add(generateOneReponseDataPkg(recordNum,true, body));

            int loopcount = (listsize - recordNum) / recordNum;

            if ((listsize - recordNum) % recordNum == 0) {
                loopcount--;
            }
            //�м�� ѭ��
            count = recordNum;
            for (int i = 0; i < loopcount; i++) {
                body = getResponseDataBodyContent(paybackInfos, count, count + recordNum);
                responseDataList.add(generateOneReponseDataPkg(recordNum,false, body));
                count += this.recordNum;
            }

            //���һ����
            int lastct = listsize - count;
            body = getResponseDataBodyContent(paybackInfos, count, listsize);
            ResponseData responseData = generateOneReponseDataPkg(lastct,false, body);
            //����Ϊ�޺�������־ !!
            setMultiPkgFlag(false, responseData);
            responseDataList.add(responseData);

        } else {
            body = getResponseDataBodyContent(paybackInfos, 0, listsize);
            responseDataList.add(generateOneReponseDataPkg(listsize,true, body));
        }
    }

    private void setResponseDataHeader(ResponseData responseData) {
        //��ͷ����
        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        if (isMultiResponsePkg) {
            responseData.setNextFlag("1"); //�к�����
        } else {
            responseData.setNextFlag("0"); //�޺�����
        }
        responseData.setTxnCode("1000");
        responseData.setErrCode("000");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");

        //������δ����
    }
    /*
       ��Ӧ��	����
       000	���׳ɹ�
       010	��Ϣ����ʧ��
       011	��Ϣ����ʧ��
       012	ͨѶ��ʱ
       100	���ݲ�����
       200	�������ڴ�����(���׳�ͻ)
       201	���״���ʱ
       210	�������������
       301	����
       603	�������Ҫ������������Ϣû������
       604	�˴ν������ڴ����벻Ҫ�ط�
       900	ϵͳ����
       901	���ݿ����
    */

    private void setErrorResponseData(String errCode) {
        //��ͷ����
        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("0"); //�޺�����
        responseData.setTxnCode("1000");
        responseData.setErrCode(errCode);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");
        //������
        responseData.setLength(headLength);
        String strlength = StringUtils.leftPad(String.valueOf(headLength), 4, ' ');
        responseData.setPkgLength(strlength);
    }

    /**
     * ���� ����Ե�����Ӧ���ж����־
     *
     * @param isExistNextPkgFlag true:�к�����
     * @param responseData
     */

    private void setMultiPkgFlag(boolean isExistNextPkgFlag, ResponseData responseData) {

        if (isExistNextPkgFlag) {
            responseData.setNextFlag("1"); //�к�����
        } else {
            responseData.setNextFlag("0"); //�޺�����
        }

    }

    /**
     * ���ɵ�����Ӧ���еİ������ݣ��������ܱ������ܽ�
     *
     * @param paybackInfos ���������е�����LIST һ��Ϊ10-15����¼
     * @param beginIdx     (from 0)
     * @return
     */
    private String getResponseDataBodyContent(List<LSPAYBACKINFO> paybackInfos, int beginIdx, int endIdx) {
        StringBuffer sb = new StringBuffer();
        for (int i = beginIdx; i < endIdx; i++) {
            LSPAYBACKINFO info = paybackInfos.get(i);
            //ѭ��: ����20 ���� 20 ��� 11
            String cardNo = StringUtils.leftPad(info.getAccount().trim(), 20, ' ');
            String nametmp = info.getCardname().trim();
            byte[] nameBytes = nametmp.getBytes();
            int nameByteLength = nameBytes.length;
            int nameStrLength = nametmp.length();
            String name = StringUtils.leftPad(nametmp, 20 - (nameByteLength - nameStrLength), ' ');
            String amt = StringUtils.leftPad(String.valueOf(info.getAmt()).trim(), 11, ' ');
            sb.append(cardNo).append(name).append(amt);
        }
        return sb.toString();
    }

    /**
     * ���ɵ�����Ӧ��
     * generateOneReponseDataPkg������������׷�ӣ����㳤�ȣ�2010/07/13 haiyu
     * @param isFirstPkg �Ƿ�Ϊ�׸���Ӧ��
     * @param bodyStr    ��Ӧ�����е�����
     * @return
     */
    private ResponseData generateOneReponseDataPkg(int count, boolean isFirstPkg, String bodyStr) {
        ResponseData responseData = new ResponseData();
        int length = 0;
        //��ͷ����
        setResponseDataHeader(responseData);

        //���崦��

        if (isFirstPkg) {
            //����
            String recordCount = StringUtils.leftPad(String.valueOf(this.totalCount), 6, ' ');
            //�ϼƽ��
            double amt = this.totalAmt / 100;
            String amtSum = StringUtils.leftPad(String.valueOf(amt), 13, ' ');

            responseData.setBodyData(recordCount + amtSum + bodyStr);
            //��ͷ���ȴ���
            length = this.headLength + this.bodyHeadFieldlength + this.oneRecordLength *count;
        } else {
            responseData.setBodyData(bodyStr);
            //��ͷ���ȴ���
            length = this.headLength + this.oneRecordLength * count;
        }

        responseData.setLength(length);
        String strlength = StringUtils.leftPad(String.valueOf(length), 4, ' ');
        responseData.setPkgLength(strlength);

        return responseData;
    }

}
