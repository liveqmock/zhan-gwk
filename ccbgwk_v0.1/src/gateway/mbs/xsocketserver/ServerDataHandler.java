package gateway.mbs.xsocketserver;

import gateway.mbs.xsocketserver.domain.RequestData;
import gateway.mbs.xsocketserver.protocol.ProtocolHandler;
import gateway.mbs.xsocketserver.protocol.ResponseHandler;
import gateway.mbs.xsocketserver.requestaction.T1000Action;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-6-28
 * Time: 15:58:39
 * To change this template use File | Settings | File Templates.
 */
public class ServerDataHandler implements ISocketDataHandler {

		public boolean onData(INonBlockingConnection connection)
				throws IOException, BufferUnderflowException,
                ClosedChannelException, MaxReadSizeExceededException {
            
			byte[] data = connection.readBytesByLength(connection.available());

			System.out.println("date from["+connection.getRemoteAddress()+"]:");

            for (int i = 0; i < data.length; i++) {
				System.out.println(data[i] + " ");
			}
			System.out.println("\r\n");

            //ͨѶ������
            //TODO �ȶ�ȡǰ4���ֽ� �˶԰�����
            ProtocolHandler protocol = new ProtocolHandler(data);
            String txncode = protocol.getTxncode();

            RequestData requestData = protocol.getRequestData();
            RequestData responseData = new RequestData();

            if ("1000".equals(txncode)) {
                T1000Action action = new  T1000Action();
                action.dealReqeust(protocol.getRequestData());
                action.dealReponse(requestData,responseData);
            }

            ResponseHandler response = new   ResponseHandler();
            byte[] responseByte = response.getBytesReponseData(responseData);
            
            //reponse����
            connection.write(responseByte);

//            connection.write(data);
			return true;
		}

		public boolean onConnectException(INonBlockingConnection connection,
				IOException ioe) throws IOException {
			System.out.println("error connect please try again...\r\n");
			return true;
		}

		public boolean onConnect(INonBlockingConnection connection)
				throws IOException, BufferUnderflowException,
                MaxReadSizeExceededException {
			System.out.println("client["+connection.getRemoteAddress()+"] is connected...\r\n");
			return true;
		}

		public boolean onDisconnect(INonBlockingConnection connection)
				throws IOException {
			System.out.println("client["+connection.getRemotePort()+"] is disconnected...\r\n");
			return true;
		}

}