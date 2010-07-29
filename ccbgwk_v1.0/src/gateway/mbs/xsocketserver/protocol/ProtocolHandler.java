package gateway.mbs.xsocketserver.protocol;

import gateway.mbs.xsocketserver.domain.RequestData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:38:02
 * To change this template use File | Settings | File Templates.
 */
public class ProtocolHandler {
    private Log log = LogFactory.getLog(this.getClass());

    private byte[] buffer;
    private String strData;    //haiyu 2010/07/21
    private RequestData requestData = new RequestData();
    private int[] headFieldLengths = {4, 3, 10, 8, 1, 4, 3, 14, 2, 16};
    private int headerLength = 65;

    //haiyu 2010/07/21
    public ProtocolHandler(String reqStrData) throws UnsupportedEncodingException {
        this.strData = reqStrData;
        dealRequestString();
    }
    //haiyu 2010/07/21
    private void dealRequestString() throws UnsupportedEncodingException {
        int bufferpos = 0;
        for (int i = 0; i < headFieldLengths.length; i++) {
            String tempStr = strData.substring(bufferpos,bufferpos + headFieldLengths[i]);
            bufferpos += headFieldLengths[i];
            switch (i) {
                case 0: //包长度
                    requestData.setPkgLength(tempStr);
                    //TODO:
                    requestData.setLength(Integer.parseInt(requestData.getPkgLength().trim()));
                    break;
                case 1:
                    requestData.setAreaCode(tempStr);
                    break;
                case 2:
                    requestData.setBranchId(tempStr);
                    break;
                case 3:
                    requestData.setOperId(tempStr);
                    break;
                case 4:
                    requestData.setNextFlag(tempStr);
                    break;
                case 5:
                    requestData.setTxnCode(tempStr);
                    break;
                case 6:
                    requestData.setErrCode(tempStr);
                    break;
                case 7:
                    requestData.setTxnTime(tempStr);
                    break;
                case 8:
                    requestData.setVersion(tempStr);
                    break;
                case 9:
                    requestData.setMac(tempStr);
                    break;
                default:
            }
        }
        //处理包体
        try{
            String bodyString = strData.substring(headerLength,strData.length());
            requestData.setBodyData(bodyString);
        } catch (ArrayIndexOutOfBoundsException ex) {
            log.error("交易2000包体获取错误:");
            ex.printStackTrace();
        }
    }

    public String getTxncode() {
        return this.requestData.getTxnCode();
    }
    public String getNextFlag() {
        return this.requestData.getNextFlag();
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }


    //util

    public static byte[] intToByteArray(int i) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);
        System.out.println("i:" + i);
        out.writeInt(i);
        byte[] b = buf.toByteArray();
        System.out.println("i:" + b);
        out.close();
        buf.close();
        return b;
    }

    public static int ByteArrayToInt(byte b[]) throws Exception {
        int temp = 0, a = 0;
        ByteArrayInputStream buf = new ByteArrayInputStream(b);

        return buf.read();
    }
}
