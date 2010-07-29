package gateway.mbs.server.protocol.requestaction;

import gateway.mbs.server.domain.RequestData;
import gateway.mbs.server.protocol.Protocol;
import gateway.mbs.server.protocol.RequestAction;
import gateway.mbs.server.protocol.response.SendALlUserList;
import gateway.mbs.server.protocol.response.SendMsgToAll;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:18:39
 * To change this template use File | Settings | File Templates.
 */
/**
 * @author    
 *
 * 处理用户更改用户呢称的请求
 */
public class ChangeNickNameAction implements RequestAction {

    private Log log= LogFactory.getLog(this.getClass());

    private static String action = "changeNickname";

    private SendALlUserList sendAllUserList = new SendALlUserList();

    private SendMsgToAll sendMsgToAll=new SendMsgToAll();
    public boolean canDeal(RequestData requestData) {
        log.info("changeNaickName被调用，请求的action为:"+requestData.getCAction());
        return action.equals(requestData.getCAction());
    }

    public void dealReqeust(RequestData requestData, Protocol protocol) {
        protocol.getUser().setNickName(requestData.getRequestString());
        this.sendAllUserList.deal(protocol);
        this.sendMsgToAll.sendSystemMsg("用户“"+protocol.getUser().getUserNameDetail()+"”更改了昵称", protocol);
    }

}