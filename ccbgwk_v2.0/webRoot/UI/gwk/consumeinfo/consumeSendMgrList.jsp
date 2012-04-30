<!--
/*********************************************************************
* 功能描述: 消费信息发送
* 作者:
* 开发日期: 2010/06/10
* 修改人:
* 修改日期:
* 版权:
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%@page import="pub.platform.security.OperatorManager" %>
<%@page import="pub.platform.form.config.SystemAttributeNames" %>
<%@page import="pub.platform.db.DBGrid" %>
<%@page import="pub.platform.html.ZtSelect" %>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>
    <script language="javascript" src="consumeSendMgrList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>

</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("ActionTable");
    dbGrid.setGridType("edit");
/*
    String sql = "select lsh,status,cardname,account,busidate,inac_date,busimoney,limitdate," +
            " tx_cd,ref_number,businame,txlog " +
            " from ls_consumeinfo " +
            " where 1=1  ";
*/
    String sql = " select * from (select (select b.areacode " +
            "          from ls_personalinfo b " +
            "         where trim(b.perid) = " +
            "               (select a.idnumber " +
            "                  from ls_cardbaseinfo a " +
            "                 where a.account = t.account)) as areacode, " +
            " lsh,status,cardname,account,busidate,inac_date,busimoney,limitdate," +
            " tx_cd,ref_number,businame,txlog " +
            " from ls_consumeinfo t) " +
            " where 1=1 ";
    
    dbGrid.setfieldSQL(sql);
    dbGrid.setWhereStr(" and status in ('11','12') order by areacode, lsh ");

    dbGrid.setField("地区", "dropdown", "6", "areacode", "true", "AREACODE");
    dbGrid.setField("流水号", "text", "10", "lsh", "true", "0");
    dbGrid.setField("通讯状态", "dropdown", "8", "status", "true", "CONSUMESTATUS");
    dbGrid.setField("持卡人", "text", "8", "cardname", "true", "0");
    dbGrid.setField("公务卡卡号", "text", "12", "account", "true", "0");
    dbGrid.setField("交易日期", "text", "8", "busidate", "true", "0");
    dbGrid.setField("入帐日期", "text", "8", "inac_date", "true", "0");
    dbGrid.setField("金额", "money", "8", "busimoney", "true", "0");
    dbGrid.setField("最迟还款日", "text", "8", "limitdate", "true", "0");
    dbGrid.setField("交易类型", "text", "8", "tx_cd", "true", "0");
    dbGrid.setField("交易参考编号", "text", "15", "ref_number", "true", "0");
    dbGrid.setField("消费地点", "text", "15", "businame", "true", "0");
    dbGrid.setField("通讯日志", "text", "20", "txlog", "true", "0");

    dbGrid.setpagesize(40);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setCheck(false);
   
    dbGrid.setbuttons("全部发送=Send,导出Excel=excel,moveFirst,prevPage,nextPage,moveLast");

%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<form id="queryForm" name="queryForm"/>
<fieldset>
    <legend> 公务卡消费异常信息明细</legend>
    <table width="100%">
        <tr>
            <td><%=dbGrid.getDBGrid()%>
            </td>
        </tr>
    </table>
</fieldset>
<FIELDSET>
    <LEGEND> 操作</LEGEND>
    <table width="100%" class="title1">
        <tr>
            <td align="right"><%=dbGrid.getDataPilot()%>
            </td>
        </tr>
    </table>
</FIELDSET>

</body>
</html>
