package gateway.mbs.server.protocol.requestaction;

import gateway.mbs.server.domain.RequestData;
import gateway.mbs.server.protocol.Protocol;
import gateway.mbs.server.protocol.RequestAction;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:20:18
 * To change this template use File | Settings | File Templates.
 */
public class KeepClientAliveAction implements RequestAction {
    private static String action = "keepAliveSignal";
    /* (non-Javadoc)
     * @see cn.humanmonth.chat.server.protocol.RequestAction#canDeal(cn.humanmonth.chat.server.domain.RequestData)
     */
    public boolean canDeal(RequestData requestData) {
        return action.equals(requestData.getCAction());
    }

    /* (non-Javadoc)
     * @see cn.humanmonth.chat.server.protocol.RequestAction#dealReqeust(cn.humanmonth.chat.server.domain.RequestData, cn.humanmonth.chat.server.protocol.Protocol)
     */
    public void dealReqeust(RequestData requestData, Protocol protocol) {
        // do nothing

    }

}   