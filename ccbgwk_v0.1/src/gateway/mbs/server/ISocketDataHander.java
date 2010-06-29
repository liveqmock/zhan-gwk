package gateway.mbs.server;

import org.xsocket.connection.IConnectExceptionHandler;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;

public interface ISocketDataHander extends IDataHandler,
        IConnectExceptionHandler, IConnectHandler, IDisconnectHandler {

}