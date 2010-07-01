package gateway.mbs.xsocketserver;

import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

import java.io.IOException;
import java.net.UnknownHostException;

public class TXSocketServer {

	private static  int PORT;
	IServer srv;
	//SwingChatServer chatServer;

	public TXSocketServer(int port) throws UnknownHostException, IOException {
		PORT = port;
//		this.srv = new Server(PORT, new ProtocolHandlerTest());
		this.srv = new Server(PORT, new ServerDataHandler());
		//this.chatServer = chatServer;


	}
	public void start() throws UnknownHostException, IOException {

		srv.start();
		System.out.println("server listening on :"+PORT+"\r\n");
	}

    public static void main(String[] args) {
        try {
            TXSocketServer server = new TXSocketServer(8090);
            server.start();
            //server.srv.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}