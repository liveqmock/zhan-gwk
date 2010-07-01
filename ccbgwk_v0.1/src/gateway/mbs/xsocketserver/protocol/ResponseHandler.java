package gateway.mbs.xsocketserver.protocol;

import gateway.mbs.xsocketserver.domain.RequestData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 13:15:21
 * To change this template use File | Settings | File Templates.
 */
public class ResponseHandler {
    private Log log = LogFactory.getLog(this.getClass());
    public byte[] getBytesReponseData(RequestData response)  {

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
        strBuffer.append(response.getBodyData());

        byte[] buffer = strBuffer.toString().getBytes();
        return buffer;
    }

}
