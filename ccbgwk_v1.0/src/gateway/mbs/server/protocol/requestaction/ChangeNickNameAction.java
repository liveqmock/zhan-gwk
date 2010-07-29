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
 * �����û������û��سƵ�����
 */
public class ChangeNickNameAction implements RequestAction {

    private Log log= LogFactory.getLog(this.getClass());

    private static String action = "changeNickname";

    private SendALlUserList sendAllUserList = new SendALlUserList();

    private SendMsgToAll sendMsgToAll=new SendMsgToAll();
    public boolean canDeal(RequestData requestData) {
        log.info("changeNaickName�����ã������actionΪ:"+requestData.getCAction());
        return action.equals(requestData.getCAction());
    }

    public void dealReqeust(RequestData requestData, Protocol protocol) {
        protocol.getUser().setNickName(requestData.getRequestString());
        this.sendAllUserList.deal(protocol);
        this.sendMsgToAll.sendSystemMsg("�û���"+protocol.getUser().getUserNameDetail()+"���������ǳ�", protocol);
    }

}