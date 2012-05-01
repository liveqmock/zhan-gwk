package gateway.mbs.server.protocol.requestaction;

import gateway.mbs.server.domain.RequestData;
import gateway.mbs.server.protocol.Protocol;
import gateway.mbs.server.protocol.RequestAction;
import gateway.mbs.server.protocol.response.SendMsgToAll;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:21:27
 * To change this template use File | Settings | File Templates.
 */
public class SendMsgToAllAction implements RequestAction {
    private static String action = "sendMsgToAll";
    private SendMsgToAll sendMsgToAll = new SendMsgToAll();

    public void dealReqeust(RequestData requestData, Protocol protocol) {
        sendMsgToAll.sendMsgToAll(requestData, protocol);
    }

    public boolean canDeal(RequestData requestData) {
        return requestData.getCAction().indexOf(action) != -1;
    }

}   