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
    //�ϴ���������
//    if (!pathStr.equals("") && pathStr != null){
        String d = re.getData(pathStr);
        if (d.equals("0")){
            %>
                <script type="text/javascript">alert("����ɹ���");
                    document.location.href = "ExcelDataImport.html";
                </script>
            <%
        } else if (d.equals("-1")) {
            %>
                <script type="text/javascript">alert("����ʧ�ܣ������µ��롣");
                    document.location.href = "ExcelDataImport.html";
                </script>
            <%
        } else {
            %>
                <div>
                    <p style="color:red;font-weight:bold;">�������֤ID�����ݿ����Ѵ��ڣ���ȷ�Ϻ������²��룺</p>
                    <%=d%>
                    <p align="center">
                        <input type="button" style="width:60px;" value="����" onclick="document.location.href='ExcelDataImport.html';">
                    </p>
                </div>
            <%
//        }
    }

%>
<html>
  <head><title>��ȡExcellֵ����</title></head>
  <body>
  <div>
      
  </div>

  </body>
</html>