package gateway.mbs.server.protocol.requestaction;

import gateway.mbs.server.domain.RequestData;
import gateway.mbs.server.protocol.Protocol;
import gateway.mbs.server.protocol.RequestAction;
import gateway.mbs.server.protocol.response.SendAllUserList;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:20:53
 * To change this template use File | Settings | File Templates.
 */
public class SendAllUserListAction implements RequestAction {
    private static String action = "getAllUserList";
    private SendAllUserList sendAllUserList = new SendAllUserList();

    /*
     * (non-Javadoc)
     *
     * @see cn.humanmonth.home.chat.server.protocol.Action#canDeal(cn.humanmonth.home.chat.server.domain.RequestData)
     */
    public boolean canDeal(RequestData requestData) {
        return action.equals(requestData.getCAction());
    }

    /*
     * (non-Javadoc)
     *
     * @see cn.humanmonth.home.chat.server.protocol.Action#dealReqeust(cn.humanmonth.home.chat.server.domain.RequestData,
     *      cn.humanmonth.home.chat.server.protocol.Protocol)
     */
    public void dealReqeust(RequestData requestData, Protocol protocol) {
        sendAllUserList.deal(protocol);
    }

}
 