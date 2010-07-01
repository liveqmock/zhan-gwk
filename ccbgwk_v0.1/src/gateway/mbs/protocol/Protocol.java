package gateway.mbs.protocol;

import gateway.mbs.protocol.domain.RequestData;
import gateway.mbs.protocol.domain.User;
import gateway.mbs.protocol.requestaction.ActionRegister;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 8:10:39
 * To change this template use File | Settings | File Templates.
 */
/**
 * @author
 *
 * Protocol类用于把客户端传来的字符串转化为服务端的能识别的请求:RequestData
 */
public class Protocol {
    private Log log = LogFactory.getLog(this.getClass());

    private List users;

    private User user;

    private static List actions = ActionRegister.getActions();

    private boolean hasSendLoginMessage = false;

    public Protocol(List users, User user) {
        this.users = users;
        this.user = user;
    }
    /**
     * 处理客户的请求
     */
    public void deal() throws UnsupportedEncodingException, IOException {
        RequestData requestData = this.getClientRequestData(user);
        log.info("用户：" + this.user.getUserNameDetail() + " 传来请求：" + requestData.getRequestXmlString());
        log.info("从客户端收到的消息：" + requestData.getRequestXmlString());
        for (int i = 0; i < actions.size(); i++) {
            RequestAction action = (RequestAction) actions.get(i);
            if (action.canDeal(requestData)) {
                action.dealReqeust(requestData, this);
                if (!hasSendLoginMessage) {
                    //new SendMsgToAll().sendSystemMsg("用户：" + user.getUserNameDetail() + " 登录了。",this);
                    this.hasSendLoginMessage = true;
                }
                return;
            }
        }
        log.info("服务端无法处理客户端传来的字符串：" + requestData.getRequestXmlString());
    }

    /**
     * 从请求字符串中判断命令类型
     */
    private String getClientActionFromLine(String line) {
        Pattern pattern= Pattern.compile("<(.*?)>");
        Matcher matcher=pattern.matcher(line);
        if(matcher.find()){
            return matcher.group(1);
        }else{
            return null;
        }
    }

    /**
     * 获取客户端数据
     */
    private RequestData getClientRequestData(User user) throws UnsupportedEncodingException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(user.getSocket().getInputStream(), "utf8"));
        String requestStr = reader.readLine();
        if (requestStr == null)
            throw new RuntimeException("不能从socket获取数据，客户可能已退出");
        return new RequestData(this.getClientActionFromLine(requestStr), requestStr);
    }

    /**
     * 向客户发送信息
     */
    public void sendStringToAllClient(String str) {
        for (int i = 0; i < users.size(); i++) {
            try {
                User user = (User) users.get(i);
                this.sendStringToSingleClient(user, str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 向单个客户发送信息
     */
    public void sendStringToSingleClient(User user, String str) {
        try {
            IOUtils.write(str.replaceAll(user.getUserNameDetailWithExcapeRegExp(), "你") + "\0", user.getSocket()
                    .getOutputStream(), "utf8");
            user.getSocket().getOutputStream().flush();
            log.info("服务端发出信息：" + str);
        } catch (IOException e) {
            log.info("用户:" + user.getUserNameDetail() + "在被发消息前退出了程序");
            throw new RuntimeException(e);
        }
    }

    public List getUsers() {
        return users;
    }

    public User getUser() {
        return user;
    }
    public boolean isHasSendLoginMessage() {
        return hasSendLoginMessage;
    }
    public void setHasSendLoginMessage(boolean hasSendLoginMessage) {
        this.hasSendLoginMessage = hasSendLoginMessage;
    }
}