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
public class T2000Action implements RequestAction {
    private Log logger = LogFactory.getLog(this.getClass());

    private static final String txnCode = "2000";
    private RequestData requestData;
    private List<RequestData> requestDataList;
    private ResponseData responseData = new ResponseData();

    //特色平台响应包总笔数
    private int totalCount = 0;
    //特色平台响应包总金额
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

    public void process() {

        int count = 0;
        for (RequestData requestData : requestDataList) {
            count++;
            if (count == 1) {
                String body = requestData.getBodyData();
                logger.debug("交易2000BODY:" + body);

                voucherid = body.substring(0, 20).trim();
                year = body.substring(20, 24).trim();
                paybackdate = body.substring(24, 32).trim();

                totalCount = Integer.parseInt(body.substring(32, 38).trim());
                totalAmt = Double.parseDouble(body.substring(38, 51).trim());

                body = body.substring(52, body.length());
                getPaybackInfoListFromRequestDataBody(body);
            }
        }

        //check
        if (checkRequestData()) {
            saveDataToDB();
            setResponseDataHeader("000");
        } else {
            setResponseDataHeader("031");   //总分金额错
        }
    }

    private void getPaybackInfoListFromRequestDataBody(String body) {

        int ptr = 0;
        //while (ptr < body.length()) {
        //TODO
        for (int i=0; i< totalCount; i++) {
            LSPAYBACKINFO info = new LSPAYBACKINFO();
            //TODO
            //info.setAreacode("266100");
            //info.setVoucherid(voucherid);
            info.setAccount(body.substring(ptr, ptr + 20).trim());
            ptr += 20;
            info.setAmt(Double.parseDouble(body.substring(ptr, ptr + 11).trim()));
            ptr += 11;
            String result = body.substring(ptr, ptr + 20).trim();
            ptr += 20;
            if ("000000".equals(result)) { //扣款成功
                info.setStatus("01"); //扣款成功
            } else {
                info.setStatus("02"); //扣款失败
                info.setRemark(result);
            }

            paybackInfos.add(info);
        }
    }

    private boolean checkRequestData() {

        int count = 0;
        double amt = 0.00;

        for (LSPAYBACKINFO info : paybackInfos) {
            count++;
            amt += info.getAmt();
        }
        if (amt != totalAmt) {
            //返回301错误 金额不符
            return false;
        } else {
            return true;
        }
    }

    private void saveDataToDB() {
        LSPAYBACKINFO paybackinfo = new LSPAYBACKINFO();
        for (LSPAYBACKINFO info : paybackInfos) {
            String sqlWhere = " where account = '" + info.getAccount() + "' and amt=" + info.getAmt();
            paybackinfo = (LSPAYBACKINFO) paybackinfo.findFirstByWhere(sqlWhere);
            if (paybackinfo != null) {
                paybackinfo.setPaybackdate(info.getPaybackdate());
                paybackinfo.setStatus(info.getStatus());
                paybackinfo.setRemark(info.getRemark());
                paybackinfo.updateByWhere(sqlWhere);
            }
        }

    }

    private void setResponseDataHeader(String errCode) {
        //包头处理
        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("0"); //无后续包
        responseData.setTxnCode(txnCode);
        responseData.setErrCode(errCode);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");

        //包长度设置
        responseData.setLength(65);
        String strlength = StringUtils.leftPad(String.valueOf(65), 4, ' ');
        responseData.setPkgLength(strlength);

    }


}