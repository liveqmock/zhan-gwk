package gateway.mbs.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:51:11
 * To change this template use File | Settings | File Templates.
 */
public class ServletUtil {

    public static Log log = LogFactory.getLog(ServletUtil.class);

    public static String HTML_ENCODING = "text/html;charset=utf-8";

    public static String XML_ENCODING = "text/xml;charset=utf-8";

    /**
     * ����HTML header���ı����ͼ�����Ϊ"text/html;charset=gb2312";
     */
    public static void setHTMLContentType(HttpServletResponse response) {
        response.setContentType(HTML_ENCODING);
    }

    /**
     * ����HTML header���ı����ͼ�����Ϊ"text/xml;charset=gb2312";
     */
    public static void setXMLContentType(HttpServletResponse response) {
        response.setContentType(XML_ENCODING);
    }

    /**
     * ��������ͻ���
     *
     * @param response
     * @param ems
     * @throws IOException
     */
    public static void outView(HttpServletResponse response, String ems) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(ems);
        out.flush();
        out.close();
    }

    /**
     * ��request�����в�����paramName��������null, �����ڣ��򷵻��Ѿ�ȥ�����˿ո���ַ���
     *
     * @param request
     * @param paramName
     * @return
     */
    public static String removeSpace(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (null == value) {
            return null;
        } else {
            return value.trim();
        }
    }

    public static Integer getIntParameter(HttpServletRequest request,String paramName){
        String valueStr=removeSpace(request,paramName);
        if(null==valueStr){
            return null;
        }else{
            return Integer.valueOf(valueStr);
        }
    }

    /**
     * ��request��ȡ���ֵ������checkBox
     *
     * @param request
     * @param paramName
     * @return
     */
    public static String[] removeMultiSpace(HttpServletRequest request, String paramName) {
        String[] values = request.getParameterValues(paramName);
        if (null == values) {
            return null;
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }
        return values;
    }



    /**
     * ��javascript��ʾ�����Ϣ������ת��ָ��ҳ��
     *
     * @param msg ��ʾ��Ϣ  
     * @param url
     *            ��ת����·��
     * @return
     * @throws IOException
     */
    public final static void outTips(HttpServletResponse response, String msg, String url) throws IOException {
        StringBuffer tips = new StringBuffer();
        tips.append("<script language=\"javascript\">");
        if (msg != null) {
            tips.append("alert('" + msg.replaceAll("\\\'", "\\\\\'") + "');");
        }
        if (url != null) {
            tips.append("window.location.href='" + url + "';");
        } else {
            tips.append("window.history.back();");
        }
        tips.append("</script>");
        ServletUtil.outView(response, tips.toString());
    }

    /**
     * ��request��ȡ����ʼҳ������Ϊxtable��Ĭ�ϵĲ�����Ϊpage������������page
     * �����������page��������page����û�У�������1
     *
     * @param request
     * @return
     */
    public static PageQuery getPageQuery(HttpServletRequest request) {
        String startIndexStr = ServletUtil.removeSpace(request, "startIndex");
        int startIndex;
        try{
            startIndex=Integer.parseInt(startIndexStr);
        }catch(Exception e){
            startIndex=0;
        }
        String pageSizeStr=ServletUtil.removeSpace(request, "results");
        int pageSize;
        try{
            pageSize=Integer.parseInt(pageSizeStr);
        }catch(Exception e){
            pageSize=0;
        }
        return new PageQuery(startIndex,pageSize);
    }

    /**
     * ����ҳ�治û����
     */
    public static void setNoCache(HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    }

}