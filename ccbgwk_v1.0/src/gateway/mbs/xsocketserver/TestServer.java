package gateway.mbs.xsocketserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xsocket.connection.IServer;
import pub.AutorunInterface;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-11
 * Time: 23:03:23
 * To change this template use File | Settings | File Templates.
 */
public class TestServer implements AutorunInterface {
    private static final Log logger = LogFactory.getLog(TestServer.class);
    private static int PORT;
    IServer srv;

    public boolean startServer() {
        System.out.println("started");
        return true;
    }

    public boolean stopServer() {
        System.out.println("stopped");
        return true;
    }

    public TestServer() {
        System.out.println("123");
    }

    public static void main(String[] args) {
        System.out.println("123");
    }
}
