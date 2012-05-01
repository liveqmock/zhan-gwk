package gateway.sockettest.client;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.NonBlockingConnection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

public class NBSocketClient {
    INonBlockingConnection bc;
    SwingChatClient chatClient;

    public NBSocketClient(String host, int port, SwingChatClient chatClient) throws IOException {
        this.bc = new NonBlockingConnection(host, port, new ClientDataHander());
        bc.setConnectionTimeoutMillis(NonBlockingConnection.MAX_TIMEOUT_MILLIS);
        this.chatClient = chatClient;
    }

    public void sendData(String text) throws IOException {
/*
		String[] req = text.split(" ");
		byte[] b = new byte[req.length];
		for (int i = 0; i < req.length; i++) {
			b[i] = Byte.parseByte(req[i]);
		}
*/
        //byte[] b = new byte[]{R.sid.radio,R.aid.radio.IOCTL_RADIO_AUTOSCAN};
        //ByteBuffer[] buffer = new ByteBuffer[b.length];

        byte[] b = text.getBytes();
        bc.write(b);
    }

    public class ClientDataHander implements SocketDataHandler {
        //@Override
        public boolean onData(INonBlockingConnection cbc) throws IOException,
                BufferUnderflowException, ClosedChannelException,
                MaxReadSizeExceededException {

            byte[] data = cbc.readBytesByLength(cbc.available());
            chatClient.area.append("come from server:");
            for (int i = 0; i < data.length; i++) {
                chatClient.area.append(data[i] + " ");
            }
            chatClient.area.append("\r\n");
            return true;
        }

        //@Override
        public boolean onConnectException(INonBlockingConnection connection,
                                          IOException ioe) throws IOException {
            System.out.println("client error");
            return true;
        }

        //@Override
        public boolean onConnect(INonBlockingConnection connection)
                throws IOException, BufferUnderflowException,
                MaxReadSizeExceededException {
            chatClient.area.setText("client[" + connection.getRemoteAddress() + "] connected...\r\n");
            return true;
        }

        //@Override
        public boolean onDisconnect(INonBlockingConnection connection)
                throws IOException {
            chatClient.area.append("client[" + connection.getRemoteAddress() + "] disconnected...\r\n");
            return true;
        }
    }
}