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
import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    //2010/07/20 haiyu add
    private String dataPkg = "";

    public boolean onData(INonBlockingConnection connection)
            throws IOException, BufferUnderflowException,
            ClosedChannelException, MaxReadSizeExceededException {
        AtomicReference<ProtocolHandler> protocol;
        int reqLen = 0;
        byte[] data = connection.readBytesByLength(connection.available());
        logger.debug("data from[" + connection.getRemoteAddress() + "]:");

        System.out.println("\r\n");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println("\r\n");
        //通讯包处理
        //TODO 先读取前4个字节 核对包长度  若长度不足 需 继续接收数据 ！！
        if (dataPkg.equals("")) {
            dataPkg = getDealBufferLength(data);
            if (dataPkg.length() >= 4) {
                 reqLen = Integer.parseInt(dataPkg.substring(0,4).trim()); //取得包长度
                 //整包全部接收完毕
                 if (dataPkg.length() == reqLen) {
                     //包解析
                     runStrData(dataPkg,connection);
                     //字节数组清空
                     dataPkg = "";
                 }
            }
        } else if (dataPkg.length() < 4) {
            dataPkg += getDealBufferLength(data);
             if (dataPkg.length() >= 4) {
                 reqLen = Integer.parseInt(dataPkg.substring(0,4).trim()); //取得包长度
                 //整包全部接收完毕
                 if (dataPkg.length() == reqLen) {
                     //包解析
                     runStrData(dataPkg,connection);
                     //字节数组清空
                     dataPkg = "";
                 }
            }
        } else {
            dataPkg += getDealBufferLength(data);
            reqLen = Integer.parseInt(dataPkg.substring(0,4).trim()); //取得包长度
            //整包全部接收完毕
            if (dataPkg.length() == reqLen) {
                //包解析
                runStrData(dataPkg,connection);
                //字节数组清空
                dataPkg = "";
            }
        }
        return true;
    }
     //转换字符串
    public String getDealBufferLength(byte[] buffer) {
        String strData = "";
        try{
            byte[] pkgLenByte = new byte[buffer.length];
            System.arraycopy(buffer, 0, pkgLenByte, 0, pkgLenByte.length);
            strData = new String(pkgLenByte,"ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return "";
        }
        return strData;
    }
    //包处理 并返回响应包
    public void runStrData(String strData ,INonBlockingConnection connection)
            throws IOException, BufferUnderflowException {
        ProtocolHandler protocol = new ProtocolHandler(strData);
        String txncode = protocol.getTxncode();

        RequestData requestData = protocol.getRequestData();
        requestData.getLength();
        requestData.getPkgLength();

        ResponseHandler responseHandler = new ResponseHandler();

        if ("1000".equals(txncode)) {
            logger.debug("交易1000开始.");
            try{
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
                    logger.info(new String(responseByte, "ISO-8859-1"));
                    //reponse处理
                    connection.write(responseByte);

                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("交易1000处理错误.");
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
                T2000Action action = new T2000Action();
                action.setRequestDataList(requestDataList);
                action.noProcess();
                 ResponseData responseData = action.getResponseData();
                byte[] responseByte = responseHandler.getBytesReponseData(responseData);
                logger.info("交易2000多包请求:" + requestDataList.size());
                connection.write(responseByte);
            }
        }
    }

    public boolean onConnectException(INonBlockingConnection connection,
                                      IOException ioe) throws IOException {
        logger.info("error connect please try again...\r\n");
        return true;
    }

    public boolean onConnect(INonBlockingConnection connection)
            throws IOException, BufferUnderflowException,
            MaxReadSizeExceededException {
        logger.info("客户端[" + connection.getRemoteAddress() + "] is connected...\r\n");
        return true;
    }

    public boolean onDisconnect(INonBlockingConnection connection)
            throws IOException {
        logger.info("客户端[" + connection.getRemotePort() + "] is disconnected...\r\n");
        return true;
    }

}
