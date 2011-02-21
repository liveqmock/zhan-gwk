package pub.platform.advance.utils;

import gateway.mbs.xsocketserver.TXSocketServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class TxSocketManager extends HttpServlet {

    static {
        System.out.println("////////////////////////////////////////");
        System.out.println("loading TxSocketManager.class");
        System.out.println("////////////////////////////////////////");
    }

    private static final long serialVersionUID = -5534543207744847503L;

    // ��ʼ��
    public void init() throws ServletException {
        //System.out.println();
        printLine();
        System.out.println("���ڼ���Socket Server......");
        try {
            TXSocketServer server = TXSocketServer.INSTANCE;
            server.start();
            System.out.println("����Socket Server ��ɣ�");
        } catch (Exception e) {
            e.printStackTrace();
        }

        printLine();
    }

    // ����
    public void destroy() {
        printLine();
        try {
            TXSocketServer server = TXSocketServer.INSTANCE;
            server.stop();
            System.out.println("Socket Server ��ֹͣ��");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("�رյ�����ҵ������Ϣ������");
        printLine();
    }

    public void shutdown() {
        destroy();
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("txshutdown".equals(request.getParameter("action"))) {
            System.out.println("********  shutdown  **********");
            shutdown();
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    private static void printLine() {
        System.out.println("//////////////////////////" + new Date() + "//////////////////////////");
    }

}


