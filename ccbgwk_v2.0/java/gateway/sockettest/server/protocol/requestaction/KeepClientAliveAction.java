package gateway.sockettest.server.protocol.requestaction;

import gateway.sockettest.server.domain.RequestData;
import gateway.sockettest.server.protocol.Protocol;
import gateway.sockettest.server.protocol.RequestAction;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:20:18
 * To change this template use File | Settings | File Templates.
 */
public class KeepClientAliveAction implements RequestAction {
    private static String action = "keepAliveSignal";
    public boolean canDeal(RequestData requestData) {
        return action.equals(requestData.getCAction());
    }
    public void dealReqeust(RequestData requestData, Protocol protocol) {
        // do nothing
    }
}