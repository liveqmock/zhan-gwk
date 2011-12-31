package gateway.mbs.server;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:24:47
 * To change this template use File | Settings | File Templates.
 */

import gateway.mbs.server.domain.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * �����������ڼ�����
 */
public class Server extends Thread {
    private Log log = LogFactory.getLog(this.getClass());
    private ServerSocket server;
    /**
     * �������ߵĿͻ�
     */
    private List users = new ArrayList();

    /**
     * ����ָ�������Ķ˿�
     */
    public void init(int port) throws IOException {
        server = new ServerSocket(port);
        log.info("server open��port is " + server.getLocalPort());
        this.start();
    }

    /**
     * ��������˿�
     */
    public int init() throws IOException {
        server = new ServerSocket(0);
        this.start();
        log.info("server open��port is " + server.getLocalPort());
        return server.getLocalPort();
    }

    public void run() {
        while (true) {
            try {
                Socket client = server.accept();
                log.info("client access:" + client.getRemoteSocketAddress());
                User user = new User(client);   
                users.add(user);
                new Processor(users, user).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
//        server.init(); //����˿�
        server.init(8001);
    }
}