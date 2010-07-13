package gateway.mbs.xsocketserver;

import gateway.mbs.xsocketserver.domain.RequestData;
import gateway.mbs.xsocketserver.domain.ResponseData;
import gateway.mbs.xsocketserver.protocol.ProtocolHandler;
import gateway.mbs.xsocketserver.protocol.ResponseHandler;
import gateway.mbs.xsocketserver.requestaction.T1000Action;
import gateway.mbs.xsocketserver.requestaction.T2000Action;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-6-28
 * Time: 15:58:39
 * To change this template use File | Settings | File Templates.
 */
public class ServerDataHandler implements ISocketDataHandler {

    private Log logger = LogFactory.getLog(this.getClass());

    private List<RequestData> requestDataList = new ArrayList();

    public boolean onData(INonBlockingConnection connection)
            throws IOException, BufferUnderflowException,
            ClosedChannelException, MaxReadSizeExceededException {

        byte[] data = connection.readBytesByLength(connection.available());

        logger.debug("date from[" + connection.getRemoteAddress() + "]:");

        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println("\r\n");

        //通讯包处理
        //TODO 先读取前4个字节 核对包长度  若长度不足 需 继续接收数据 ！！

        ProtocolHandler protocol = new ProtocolHandler(data);
        String txncode = protocol.getTxncode();

        RequestData requestData = protocol.getRequestData();
        //RequestData responseData = new RequestData();
        ResponseHandler responseHandler = new ResponseHandler();

        if ("1000".equals(txncode)) {
            T1000Action action = new T1000Action(requestData);
            action.process();
            List<ResponseData> responseDataList = action.getResponseDataList();

            for (ResponseData responseData : responseDataList) {

                byte[] responseByte = responseHandler.getBytesReponseData(responseData);

                for (int i = 0; i < responseByte.length; i++) {
                    System.out.print(responseByte[i] + " ");
                }
                System.out.println("\r\n");
                System.out.println(new String(responseByte, "ISO-8859-1"));

                //reponse处理
                connection.write(responseByte);

            }
        }

        if ("2000".equals(txncode)) {
            logger.debug("交易2000开始.");
            String nextFlag = protocol.getNextFlag();
            if ("0".equals(nextFlag)) { //无后续包
                /*
                   处理 requestDataList
                   发送回执报文
                */
                try {
                    requestDataList.add(requestData);
                    T2000Action action = new T2000Action();
                    action.setRequestDataList(requestDataList);


                    action.process();

                    ResponseData responseData = action.getResponseData();
                    byte[] responseByte = responseHandler.getBytesReponseData(responseData);

                    for (int i = 0; i < responseByte.length; i++) {
                        System.out.print(responseByte[i] + " ");
                    }
                    System.out.println("\r\n");
                    System.out.println(new String(responseByte, "ISO-8859-1"));

                    logger.info(new String(responseByte, "ISO-8859-1"));

                    connection.write(responseByte);
                    logger.debug("交易2000正常结束.");
                } catch (Exception e) {
                    logger.error("交易2000处理错误：",e);
                    //TODO 异常处理
                } finally {
                    requestDataList = new ArrayList();
                }
            } else {
                requestDataList.add(requestData);
            }
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
        System.out.println("客户端[" + connection.getRemoteAddress() + "] is connected...\r\n");
        return true;
    }

    public boolean onDisconnect(INonBlockingConnection connection)
            throws IOException {
        System.out.println("客户端[" + connection.getRemotePort() + "] is disconnected...\r\n");
        return true;
    }

}
