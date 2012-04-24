package gateway.mbs.xsocketserver.protocol;

import gateway.mbs.xsocketserver.domain.RequestData;
import gateway.mbs.xsocketserver.domain.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 13:15:21
 * To change this template use File | Settings | File Templates.
 */
public class ResponseHandler {
    private Log log = LogFactory.getLog(this.getClass());
    public byte[] getBytesReponseData(ResponseData response)  {

//        byte[] buffer = new byte[response.getLength()];

        StringBuffer strBuffer  = new  StringBuffer();
        strBuffer.append(response.getPkgLength());
        strBuffer.append(response.getAreaCode());
        strBuffer.append(response.getBranchId());
        strBuffer.append(response.getOperId());
        strBuffer.append(response.getNextFlag());
        strBuffer.append(response.getTxnCode());
        strBuffer.append(response.getErrCode());
        strBuffer.append(response.getTxnTime());
        strBuffer.append(response.getVersion());
        strBuffer.append(response.getMac());
        //2010/07/15 haiyu T2000发送空包判断
        if (response.getBodyData() != null){
            strBuffer.append(response.getBodyData());
        }
        byte[] buffer = strBuffer.toString().getBytes();
        return buffer;
    }

    /**
     * 生成错误信息包
     * @param requestData
     * @param errCode
     * @param errMsg
     * @return
     */
    public byte[] getBytesErrorResponseData(RequestData requestData,String errCode, String errMsg) {

        ResponseData responseData  = new ResponseData();
        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("0"); //无后续包
        responseData.setTxnCode("1000");
        responseData.setErrCode(errCode);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");
        //包体

        responseData.setBodyData(errMsg);

        //长度处理
        int length = 65 + errMsg.length();
        responseData.setLength(length);
        String strlength = StringUtils.leftPad(String.valueOf(length), 4, ' ');
        responseData.setPkgLength(strlength);

        return  getBytesReponseData(responseData);
    }

}
