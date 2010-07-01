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
    private RequestData requestData = new RequestData();
    private int[] headFieldLengths = {4, 3, 10, 8, 1, 4, 3, 14, 2, 16};
    private int headerLength = 65;

    public ProtocolHandler(byte[] requestBuffer) throws UnsupportedEncodingException {
        this.buffer = requestBuffer;
        dealRequestBuffer();
    }

    private void dealRequestBuffer() throws UnsupportedEncodingException {
        int bufferpos = 0;
        for (int i = 0; i < headFieldLengths.length; i++) {
            byte[] bField = new byte[headFieldLengths[i]];
            System.arraycopy(buffer, bufferpos, bField, 0, bField.length);
            bufferpos += bField.length;
            switch (i) {
                case 0: //包长度
                    requestData.setPkgLength(new String(bField, "ISO-8859-1"));
                    //TODO:
                    requestData.setLength(Integer.parseInt(requestData.getPkgLength()));
                    break;
                case 1:
                    requestData.setAreaCode(new String(bField, "ISO-8859-1"));
                    break;
                case 2:
                    requestData.setBranchId(new String(bField, "ISO-8859-1"));
                    break;
                case 3:
                    requestData.setOperId(new String(bField, "ISO-8859-1"));
                    break;
                case 4:
                    requestData.setNextFlag(new String(bField, "ISO-8859-1"));
                    break;
                case 5:
                    requestData.setTxnCode(new String(bField, "ISO-8859-1"));
                    break;
                case 6:
                    requestData.setErrCode(new String(bField, "ISO-8859-1"));
                    break;
                case 7:
                    requestData.setTxnTime(new String(bField, "ISO-8859-1"));
                    break;
                case 8:
                    requestData.setVersion(new String(bField, "ISO-8859-1"));
                    break;
                case 9:
                    requestData.setMac(new String(bField, "ISO-8859-1"));
                    break;
                default:
            }
        }

        //处理包体
        byte[] bodyBuffer = new byte[requestData.getLength()- headerLength];
        System.arraycopy(buffer, headerLength, bodyBuffer, 0, bodyBuffer.length);

        String bodyString = new String(bodyBuffer,"ISO-8859-1");
        requestData.setBodyData(bodyString);
    }



    public String getTxncode() {
        return this.requestData.getTxnCode();
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
