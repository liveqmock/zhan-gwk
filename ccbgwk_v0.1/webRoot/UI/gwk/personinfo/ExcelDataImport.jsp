<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-6-23
  Time: 9:55:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="com.ccb.personinfo.ReadExcel" %>
<%--<%@page import="gwk.ReadExcel"%>--%>
<%
    ReadExcel re = new ReadExcel();
    String pathStr = request.getParameter("himport");
//    String pathStr = "";
    //上传到服务器
//    if (!pathStr.equals("") && pathStr != null){
        String d = re.getData(pathStr);
        if (d.equals("0")){
            %>
                <script type="text/javascript">alert("导入成功。");
                    document.location.href = "ExcelDataImport.html";
                </script>
            <%
        } else if (d.equals("-1")) {
            %>
                <script type="text/javascript">alert("导入失败，请重新导入。");
                    document.location.href = "ExcelDataImport.html";
                </script>
            <%
        } else {
            %>
                <div>
                    <p style="color:red;font-weight:bold;">以下身份证ID在数据库中已存在，请确认后再重新插入：</p>
                    <%=d%>
                    <p align="center">
                        <input type="button" style="width:60px;" value="返回" onclick="document.location.href='ExcelDataImport.html';">
                    </p>
                </div>
            <%
//        }
    }

%>
<html>
  <head><title>读取Excell值测试</title></head>
  <body>
  <div>
      
  </div>

  </body>
</html>