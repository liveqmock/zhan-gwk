package gateway.mbs.response;

import gateway.mbs.protocol.Protocol;
import gateway.mbs.protocol.domain.User;
import gateway.mbs.util.XmlUtil;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:44:21
 * To change this template use File | Settings | File Templates.
 */
public class SendALlUserList {
    public static String action = "getAllUserListBack";
    public static String userXmlTag="UserList";

    private String getAllUsernameXml(List users) {   
        StringBuffer result = new StringBuffer();
        result.append(XmlUtil.XML_HEADER);
        result.append(XmlUtil.getStartTag(action));
        for (int i = 0; i < users.size(); i++) {
            User tempUser = (User) users.get(i);
            result.append(XmlUtil.getStartTag(userXmlTag)+tempUser.getUserNameDetail()+XmlUtil.getEndTag(userXmlTag));
        }
        return result.append(XmlUtil.getEndTag(action)).toString();
    }

    public void deal(Protocol protocol) {
        for (int i = 0; i < protocol.getUsers().size(); i++) {
            try {
                User currentUser = (User) protocol.getUsers().get(i);
                protocol.sendStringToSingleClient(currentUser, this.getAllUsernameXml(protocol.getUsers()).replaceAll(
                        currentUser.getUserNameDetailWithExcapeRegExp(), "??"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}