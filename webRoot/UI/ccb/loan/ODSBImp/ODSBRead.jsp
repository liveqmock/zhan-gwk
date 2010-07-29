<!--
/*********************************************************************
* 功能描述: Odsb 数据读入
* 开发日期: 2010/06/16
* 修 改 人:
* 修改日期:
* 版 权: 公司
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@page import="com.ccb.util.CcbLoanConst" %>
<%@include file="/global.jsp" %>

<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>

    <script language="javascript" src="ODSBRead.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
</head>
<%
    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("ActionTable");
    dbGrid.setGridType("edit");
    String sql = "select lsh,status,cardname,account,busidate,inac_date,busimoney,limitdate," +
            " tx_cd,ref_number,businame,txlog " +
            " from ls_consumeinfo ";
    dbGrid.setfieldSQL(sql);
    dbGrid.setWhereStr(" where 1<>1 ");

    dbGrid.setField("流水号", "text", "16", "lsh", "true", "0");
    dbGrid.setField("通讯状态", "dropdown", "8", "status", "true", "CONSUMESTATUS");
    dbGrid.setField("持卡人", "text", "8", "cardname", "true", "0");
    dbGrid.setField("公务卡卡号", "text", "16", "account", "true", "0");
    dbGrid.setField("交易日期", "text", "12", "busidate", "true", "0");
    dbGrid.setField("入帐日期", "text", "12", "inac_date", "true", "0");
    dbGrid.setField("金额", "money", "12", "busimoney", "true", "0");
    dbGrid.setField("最迟还款日", "text", "12", "limitdate", "true", "0");
    dbGrid.setField("交易类型", "text", "8", "tx_cd", "true", "0");
    dbGrid.setField("交易参考编号", "text", "20", "ref_number", "true", "0");
    dbGrid.setField("消费地点", "text", "18", "businame", "true", "0");
    dbGrid.setField("通讯日志", "text", "8", "txlog", "true", "0");

    dbGrid.setpagesize(15);
    dbGrid.setdataPilotID("datapilot");

    dbGrid.setbuttons("moveFirst,prevPage,nextPage,moveLast");

%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<div class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px">
        <%--<fieldset>--%>
        <legend>ODSB读取</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <form id="queryForm" name="queryForm">
                <!-- 仅作为平台打包数据之用 -->
                <input type="hidden" id="operType" name="operType" value="readFromODSB"/>
                <input type="hidden" id="newLsh" name="newLsh" value=""/>
                <tr height="20">

                    <%--<td width="70%" class="data_input">从ODSB库读取数据</td>--%>
                    <td align="center" nowrap="nowrap">
                        <input type="button" id="button" onClick="readFromODSB()" value="ODSB消费数据读取">
                    </td>
                </tr>
            </form>
        </table>
        <br>
    </fieldset>
</div>
<div id="divResultInfo" class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px;margin-top:0px">

        <legend>ODSB数据读取结果</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="0" width="50%">
            <tr>
                <td width="40%" class="lbl_right_padding">ODSB读取数据记录数</td>
                <td width="60%" class="data_input"  id="_cell_importCnt"></td>
            </tr>
        </table>
        <br>
        <table width="100%">
        <tr>
            <td>
                <%=dbGrid.getDBGrid()%>
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
</div>
</body>
</html>
