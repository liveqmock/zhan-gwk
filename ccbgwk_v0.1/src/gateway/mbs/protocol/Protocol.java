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
 * Protocol�����ڰѿͻ��˴������ַ���ת��Ϊ����˵���ʶ�������:RequestData
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
     * ����ͻ�������
     */
    public void deal() throws UnsupportedEncodingException, IOException {
        RequestData requestData = this.getClientRequestData(user);
        log.info("�û���" + this.user.getUserNameDetail() + " ��������" + requestData.getRequestXmlString());
        log.info("�ӿͻ����յ�����Ϣ��" + requestData.getRequestXmlString());
        for (int i = 0; i < actions.size(); i++) {
            RequestAction action = (RequestAction) actions.get(i);
            if (action.canDeal(requestData)) {
                action.dealReqeust(requestData, this);
                if (!hasSendLoginMessage) {
                    //new SendMsgToAll().sendSystemMsg("�û���" + user.getUserNameDetail() + " ��¼�ˡ�",this);
                    this.hasSendLoginMessage = true;
                }
                return;
            }
        }
        log.info("������޷�����ͻ��˴������ַ�����" + requestData.getRequestXmlString());
    }

    /**
     * �������ַ������ж���������
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
     * ��ȡ�ͻ�������
     */
    private RequestData getClientRequestData(User user) throws UnsupportedEncodingException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(user.getSocket().getInputStream(), "utf8"));
        String requestStr = reader.readLine();
        if (requestStr == null)
            throw new RuntimeException("���ܴ�socket��ȡ���ݣ��ͻ��������˳�");
        return new RequestData(this.getClientActionFromLine(requestStr), requestStr);
    }

    /**
     * ��ͻ�������Ϣ
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
     * �򵥸��ͻ�������Ϣ
     */
    public void sendStringToSingleClient(User user, String str) {
        try {
            IOUtils.write(str.replaceAll(user.getUserNameDetailWithExcapeRegExp(), "��") + "\0", user.getSocket()
                    .getOutputStream(), "utf8");
            user.getSocket().getOutputStream().flush();
            log.info("����˷�����Ϣ��" + str);
        } catch (IOException e) {
            log.info("�û�:" + user.getUserNameDetail() + "�ڱ�����Ϣǰ�˳��˳���");
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