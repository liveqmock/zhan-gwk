<%--
*******************************************************************
*    ��������:    ���������ۺ�ҳ��
*    ������ҳ:    �ൺ��
*    ���ݲ���:
*    ��   ��:
*    ��������:    2003-05-29
*    �� �� ��:
*    �޸�����:
*    ��   Ȩ:   	  leonwoo
*
*
*******************************************************************
--%>
<%@ page contentType="text/html; charset=gb2312" %>
<%@page import="pub.platform.security.OperatorManager" %>
<%@page import="pub.platform.form.config.SystemAttributeNames" %>
<%@page import="pub.platform.db.DBGrid" %>
<%@page import="pub.platform.html.ZtSelect" %>
<%@page import="java.util.*" %>
<%@page import="pub.platform.db.RecordSet" %>
<%@page import="pub.platform.db.ConnectionManager" %>
<%@page import="pub.platform.db.DatabaseConnection" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="pub.platform.utils.BusinessDate" %>
<%@page import="pub.platform.db.DBUtil"%>

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="../../css/ccb.css" rel="stylesheet" type="text/css">

</head>
<body style="margin:0px;padding:0px" class="Bodydefault">
<%--<br/>--%>
<div class="queryPanalDiv">
    <fieldset style="padding:20px 20px 20px 20px">
    <legend>��������Ϣ</legend>
    <br>
    <table align="center" width="100%" border="0">
        <tr valign="middle">
            <!--  <td width="5%" height="100">&nbsp;</td> -->
            <td valign="middle"><table align="center" width="100%" border="1">
                    <tr style="background-color: #CFDBEF;color:#0000AA;text-align:center;height: 28px">
                        <td width="50%" style="padding:5px">�������嵥</td>
                        <td width="20%" style="padding:5px">��ʼʱ��</td>
                        <td width="30%" style="padding:5px">����ʱ��</td>
                    </tr>
                </table></td>
        </tr>
    </table>
    </fieldset>
    <br/>
    <fieldset style="padding:20px 20px 20px 20px">
    <legend>���״�����Ϣ</legend>
    <br>
    <table align="center" width="100%" border="0">
        <tr valign="middle">
            <td valign="middle"><table align="center" width="100%" border="1">
                    <tr style="background-color: #CFDBEF;color: #0000AA;text-align:center;height: 28px">
                        <td width="50%" style="padding:5px">������ϸ</td>
                        <td width="50%" style="padding:5px">�� ��</td>
                    </tr>
                </table></td>
        </tr>
    </table>
    </fieldset>
</div>
</body>
</html>
