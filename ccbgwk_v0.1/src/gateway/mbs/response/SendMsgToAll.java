package gateway.mbs.response;

import gateway.mbs.protocol.Protocol;
import gateway.mbs.protocol.domain.RequestData;
import gateway.mbs.util.XmlUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:45:11
 * To change this template use File | Settings | File Templates.
 */
public class SendMsgToAll {
    private static String action = "sendMsgToAllBack";

    public void sendMsgToAll(RequestData requestData, Protocol protocol) {
        protocol.sendStringToAllClient(XmlUtil.getStartTag(action) + "(" + protocol.getUser().getUserNameDetail() + "):" + requestData.getRequestString() + XmlUtil.getEndTag(action));
    }

    public void sendSystemMsg(String msg, Protocol protocol) {
        protocol.sendStringToAllClient(XmlUtil.getStartTag(action) + "(????????):" + msg + XmlUtil.getEndTag(action));
    }

}   