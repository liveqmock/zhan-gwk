package gateway.mbs.xsocketserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;
import pub.AutorunInterface;

import java.io.IOException;
import java.net.UnknownHostException;

public class TXSocketServer implements AutorunInterface {
    private static final Log logger = LogFactory.getLog(TXSocketServer.class);
	private static  int PORT = 8000;
	IServer srv;

	public TXSocketServer() throws UnknownHostException, IOException {

		this.srv = new Server(PORT, new ServerDataHandler());


	}
	public void start() throws UnknownHostException, IOException {

		srv.start();
		System.out.println("server listening on :"+PORT+"\r\n");
		logger.debug("server listening on :"+PORT+"\r\n");
	}

    public boolean startServer(){
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

    public boolean stopServer(){
        //server.srv.close();
        return true;
    }

    public static void main(String[] args) {
        try {
            TXSocketServer server = new TXSocketServer();
            server.start();
            //server.srv.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

}