<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-6-23
  Time: 9:55:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="com.ccb.personinfo.ReadExcel" %>
<%@ page import="com.jspsmart.upload.*" %>
<%
    SmartUpload su = new SmartUpload();
    su.setForcePhysicalPath(true);
    su.initialize(pageContext);
    su.setMaxFileSize(10000000);     //�����ϴ��ļ���С
    su.setTotalMaxFileSize(20000);
    su.setAllowedFilesList("xls");
    su.setDeniedFilesList("exe,bat,jsp,htm,html");
//
//    String fm = (String)su.getRequest().getParameter("aaa");
////    String fm = request.getParameter("_post");
//    if (fm != null){
        String savePath = "D:\\ccbgwk\\";

        try{
            su.upload();
        }catch(SmartUploadException ex){
            ex.getMessage();
        }
        com.jspsmart.upload.Request req = su.getRequest();
        //save
        com.jspsmart.upload.File f = su.getFiles().getFile(0);
        try{
            f.saveAs(savePath + f.getFileName());
        }catch(SmartUploadException ex1){
            ex1.printStackTrace();
        }

        ReadExcel re = new ReadExcel();
        String pathStr = savePath + f.getFileName();
        String d = re.getData(pathStr);
        if (d.equals("0")){
            %>
                <script type="text/javascript">alert("����ɹ���");
                    document.location.href = "PerInfoManagement.jsp";
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
                        <!--������Ϣ��ѯͬһ��ҳ�洦��ʱ��
                        <input type="button" style="width:60px;" value="�ر�" onclick="window.opener=null;window.open('','_self');window.close();">-->
                         <!--��������ʱ��-->
                        <input type="button" style="width:60px;" value="����" onclick="document.location.href = 'ExcelDataImport.html';">
                    </p>
                </div>
        <%
        }
//}
        %>