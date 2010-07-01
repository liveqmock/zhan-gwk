package gateway.mbs.server;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-6-28
 * Time: 15:58:39
 * To change this template use File | Settings | File Templates.
 */
public class ProtocolHandler implements ISocketDataHandler {

    public boolean onData(INonBlockingConnection connection) throws IOException {

        //包头定义
        byte type = -1;
        int version = -1;
        int signature = -1;

        int dataLength = 0;
        String txcode = null;
        ////////////
        // "transaction" start  
        //

        // mark read position
        connection.markReadPosition();

        try {
            byte[] bDataLength = connection.readBytesByLength(4);
            dataLength = Integer.getInteger(new String(bDataLength));
//            type = connection.readBytesByLength();
            version = connection.readByte();
            signature = connection.readByte();
//            version = connection.readInt();
//            signature = connection.readInt();
//            dataLength = connection.readInt();

            connection.removeReadMark();

        } catch (BufferUnderflowException bue) {
            connection.resetToReadMark();
            return true;
        }

        //
        // "transaction" end
        ///////////////

        if (type == 1) {
            connection.setHandler(new ContentHandler(this, dataLength, signature));
        } else {
            //...
        }

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
        System.out.println("client[" + connection.getRemoteAddress() + "] is connected...\r\n");
        return true;
    }

    public boolean onDisconnect(INonBlockingConnection connection)
            throws IOException {
        System.out.println("client[" + connection.getRemotePort() + "] is disconnected...\r\n");
        return true;
    }


}


class ContentHandler implements IDataHandler {

    private int remaining = 0;
    private ProtocolHandler hdl = null;

    public ContentHandler(ProtocolHandler hdl, int dataLength, int signature) {
        this.hdl = hdl;
        remaining = dataLength;
        //...
    }

    public boolean onData(INonBlockingConnection nbc) throws IOException {
        int available = nbc.available();

        int lengthToRead = remaining;
        if (available < remaining) {
            lengthToRead = available;
        }

        ByteBuffer[] buffers = nbc.readByteBufferByLength(lengthToRead);
        remaining -= lengthToRead;

        // processing the data
        // ...

        if (remaining == 0) {
            nbc.setAttachment(hdl);
            nbc.write("ACCEPTED\r\n");
        }

        return true;
    }
}