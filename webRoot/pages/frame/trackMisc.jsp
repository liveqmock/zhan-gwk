<%--
*******************************************************************
*    功能描述:    跟踪提醒综合页面
*    连接网页:    青岛市
*    传递参数:
*    作   者:
*    开发日期:    2003-05-29
*    修 改 人:
*    修改日期:
*    版   权:   	  leonwoo
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
    <legend>服务处理信息</legend>
    <br>
    <table align="center" width="100%" border="0">
        <tr valign="middle">
            <!--  <td width="5%" height="100">&nbsp;</td> -->
            <td valign="middle"><table align="center" width="100%" border="1">
                    <tr style="background-color: #CFDBEF;color:#0000AA;text-align:center;height: 28px">
                        <td width="50%" style="padding:5px">服务处理清单</td>
                        <td width="20%" style="padding:5px">起始时间</td>
                        <td width="30%" style="padding:5px">结束时间</td>
                    </tr>
                </table></td>
        </tr>
    </table>
    </fieldset>
    <br/>
    <fieldset style="padding:20px 20px 20px 20px">
    <legend>交易处理信息</legend>
    <br>
    <table align="center" width="100%" border="0">
        <tr valign="middle">
            <td valign="middle"><table align="center" width="100%" border="1">
                    <tr style="background-color: #CFDBEF;color: #0000AA;text-align:center;height: 28px">
                        <td width="50%" style="padding:5px">交易明细</td>
                        <td width="50%" style="padding:5px">笔 数</td>
                    </tr>
                </table></td>
        </tr>
    </table>
    </fieldset>
</div>
</body>
</html>
