package gateway.mbs.client;

import org.xsocket.connection.IConnectExceptionHandler;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;

public interface SocketDataHandler extends IDataHandler,
        IConnectExceptionHandler, IConnectHandler, IDisconnectHandler {

}