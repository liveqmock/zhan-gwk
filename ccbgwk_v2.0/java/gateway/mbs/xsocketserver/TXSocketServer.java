package gateway.mbs.xsocketserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

//public class TXSocketServer implements AutorunInterface {
public class TXSocketServer  {
    public final static  TXSocketServer INSTANCE = new TXSocketServer();
    private static final Log logger = LogFactory.getLog(TXSocketServer.class);
    private static int PORT = 8000;
    IServer srv;

    private   TXSocketServer(){
    }

/*
    public TXSocketServer() throws UnknownHostException, IOException {
        this.srv = new Server(PORT, new ServerDataHandler());
    }
*/

    

    private void init() throws IOException {
        this.srv = new Server(PORT, new ServerDataHandler());
    }

    public void start() throws UnknownHostException, IOException {
        init();
        srv.start();
        logger.info("server:" + srv.getLocalAddress() + ":" + PORT);
        Map<String, Class> maps = srv.getOptions();
        if (maps != null) {
            for (Map.Entry<String, Class> entry : maps.entrySet()) {
                System.out.println("key= " + entry.getKey() + " value =" + entry.getValue().getName());
            }
        }
        logger.info("server info: " + srv.getStartUpLogMessage());
    }

    public boolean startServer() {
        try {
            TXSocketServer server = new TXSocketServer();
            server.start();
            return true;
            //server.srv.close();
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
    }

    public boolean stop() throws IOException {
        srv.close();
        return true;
    }

    public static void main(String[] args) {
        try {
            TXSocketServer server = TXSocketServer.INSTANCE;
            server.start();
            //server.srv.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

}