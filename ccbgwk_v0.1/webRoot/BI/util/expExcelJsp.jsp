<%@ page contentType="application/x-download; charset=UTF-8" %>
<%@ page import="pub.platform.db.DBXML" %>
<%@ page import="pub.platform.utils.Basic" %>
<%@ page import="jxl.Workbook" %>
<%@ page import="jxl.write.WritableWorkbook" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="java.io.OutputStream" %>
<%
    Log logger = LogFactory.getLog("expExcelJsp.jsp");

    try {
        DBXML testxml = new DBXML();
        String xmnlStr = request.getParameter("xx");
//  String rexml = testxml.getDropDownXML(Basic.decode(xmnlStr));

        request.setCharacterEncoding("GBK");
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=" + "export" + ".xls");


        OutputStream outputStream = response.getOutputStream();

        WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
        //..... write the excel
        String rexml = testxml.doExcelHou(Basic.decode(xmnlStr), workbook);

        workbook.write();
        workbook.close();
        outputStream.close();

        response.flushBuffer();

    } catch (Exception e) {
        logger.error(e);
    }


%>
<%--<%=rexml%>--%>
