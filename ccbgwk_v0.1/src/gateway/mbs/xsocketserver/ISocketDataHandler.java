package gateway.mbs.xsocketserver;

import org.xsocket.connection.IConnectExceptionHandler;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;

public interface ISocketDataHandler extends IDataHandler,
        IConnectExceptionHandler, IConnectHandler, IDisconnectHandler {

}