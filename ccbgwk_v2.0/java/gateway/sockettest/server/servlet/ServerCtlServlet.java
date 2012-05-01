package gateway.sockettest.server.servlet;

import gateway.sockettest.server.Server;
import gateway.sockettest.util.ServletUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 这个类为附助类，用于在Web环境中启动Server类
 * Date: 2010-7-1
 * Time: 9:23:06
 * To change this template use File | Settings | File Templates.
 */

public class ServerCtlServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {   
    static final long serialVersionUID = 1L;
    private Log log= LogFactory.getLog(this.getClass());
    private static Server server;
    private static int port;
    private static Exception e;

    /*
     * (non-Java-doc)
     *
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public ServerCtlServlet() {
        super();
    }

    /*
     * (non-Java-doc)
     *
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.setNoCache(response);
        PrintWriter writer = response.getWriter();
        if (e == null) {
            writer.print(port);
        } else {
            writer.print(e.getMessage());
        }
        log.info("请求得到的端口为："+port);
        writer.close();
    }

    /*
     * (non-Java-doc)
     *
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.GenericServlet#init()
     */
    public void init() throws ServletException {
        super.init();
        server = new Server();
        try {
            port = server.init();
        } catch (IOException e) {
            ServerCtlServlet.e = e;
            e.printStackTrace();
        }
      log.info("服务器启动成功，端口为："+port);
    }
}
