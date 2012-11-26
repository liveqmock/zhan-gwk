package gateway.sockettest.server;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:25:44
 * To change this template use File | Settings | File Templates.
 */

import gateway.sockettest.server.protocol.Protocol;
import gateway.sockettest.server.domain.User;
import gateway.sockettest.server.protocol.response.SendAllUserList;
import gateway.sockettest.server.protocol.response.SendMsgToAll;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;

/**
 * ÿ��Processor������һ��User
 */
public class Processor extends Thread {
    private List users;
    private User user;
    private Protocol protocol;
    private Log log = LogFactory.getLog(this.getClass());

    public Processor(List users, User user) {
        this.users = users;
        this.user = user;
        this.protocol = new Protocol(users, user);
    }

    public void run() {
        // �����Ƿ�Ҫ��ȫ��֤���������������ӵ���֤����
        protocol.sendStringToSingleClient(protocol.getUser(), "1234");
        while (true) {
            try {
                protocol.deal();
            } catch (Exception e) {
                log.info(e.getMessage());
                users.remove(user);
                protocol.getUsers().remove(user);
                try {
                    user.getSocket().close();
                } catch (IOException e1) {
                }
                new SendAllUserList().deal(protocol);
                if (this.protocol.isHasSendLoginMessage()) {
                    new SendMsgToAll().sendSystemMsg("�û�:" + user.getUserNameDetail() + "���˳�", protocol);
                    log.info("�û�:" + user.getUsername() + "���˳���");
                }
                return;
            }
        }
    }
}