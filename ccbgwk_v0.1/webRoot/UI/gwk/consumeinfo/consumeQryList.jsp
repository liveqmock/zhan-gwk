<!--
/*********************************************************************
* 功能描述: 消费信息查询
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
    <%--<script language="javascript" src="coopprojList.js"></script>--%>
    <script language="javascript" src="consumeQryList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>

</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    //test
/*
    ConnectionManager cm = ConnectionManager.getInstance();
    DatabaseConnection dc = cm.get();

    StringBuilder deptAll = new StringBuilder(" ");
    try {
        RecordSet chrs = dc.executeQuery("select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid");
        while (chrs != null && chrs.next()) {
            deptAll.append("'" + chrs.getString("deptid") + "',");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
//         if ( isAutoRelease )
        cm.release();
    }
    deptAll = deptAll.deleteCharAt(deptAll.length() - 1);
*/

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("ActionTable");
    dbGrid.setGridType("edit");


    String sql = "select lsh,status,cardname,account,busidate,inac_date,busimoney,limitdate," +
            " tx_cd,ref_number,businame,txlog " +
            " from ls_consumeinfo " +
            " where 1=1 ";
    dbGrid.setfieldSQL(sql);
    dbGrid.setWhereStr(" order by lsh ");

    dbGrid.setField("流水号", "text", "10", "lsh", "true", "0");
    dbGrid.setField("通讯状态", "dropdown", "8", "status", "true", "CONSUMESTATUS");
    dbGrid.setField("持卡人", "text", "8", "cardname", "true", "0");
    dbGrid.setField("卡号", "text", "12", "account", "true", "0");
    dbGrid.setField("交易日期", "text", "8", "busidate", "true", "0");
    dbGrid.setField("入帐日期", "text", "8", "inac_date", "true", "0");
    dbGrid.setField("金额", "money", "8", "busimoney", "true", "0");
    dbGrid.setField("最迟还款日", "text", "8", "limitdate", "true", "0");
    dbGrid.setField("交易码", "text", "8", "tx_cd", "true", "0");
    dbGrid.setField("交易参考编号", "text", "15", "ref_number", "true", "0");
    dbGrid.setField("消费地点", "text", "15", "businame", "true", "0");
    dbGrid.setField("通讯日志", "text", "20", "txlog", "true", "0");

    dbGrid.setbuttons("导出Excel=excel,查看详细=queryConsume,查看项目=query,moveFirst,prevPage,nextPage,moveLast");

%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
    <legend> 查询条件</legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <!-- 编号  该隐藏字段是为了删除之用 -->
            <input type="hidden" id="lsh" value="">
            <!-- 系统日志表使用 -->
            <input type="hidden" id="busiNode"/>
            <tr>
                <td width="15%" class="lbl_right_padding"> 卡号</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="account" size="30" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 持卡人</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="cardname" size="60" style="width:91% ">
                </td>
            </tr>
            <tr>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 交易码</td>
                <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3"><input type="text" id="tx_cd"
                                                                                        size="60" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 通讯状态</td>
                <td width="30%" class="data_input" colspan="3"><%
                    ZtSelect zs = new ZtSelect("status", "consumestatus", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>
                </td>
                <td width="10%" align="right" nowrap="nowrap"><input name="cbRetrieve" type="button"
                                                                     class="buttonGrooveDisable" id="button"
                                                                     onClick="queryClick()"
                                                                     onMouseOver="button_onmouseover()"
                                                                     onMouseOut="button_onmouseout()" value="检 索">
                </td>
            </tr>
            <tr>

                <td width="15%" nowrap="nowrap" class="lbl_right_padding">消费日期起</td>
                <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="busidate1" name="busidate1"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:80%"></td>
                <td width="2%" nowrap="nowrap" class="lbl_right_padding">至</td>
                <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="busidate2" name="busidate2"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:80%">
                </td>
                <td width="15%" nowrap="nowrap" class="lbl_right_padding">入帐日期起</td>
                <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="inac_date1" name="inac_date1"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:80%"></td>
                <td width="2%" nowrap="nowrap" class="lbl_right_padding">至</td>
                <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="inac_date2" name="inac_date2"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:80%">
                </td>


                <td width="10%" align="right" nowrap="nowrap"><input name="Input"
                                                                     class="buttonGrooveDisable"
                                                                     type="reset"
                                                                     value="重 填"
                                                                     onMouseOver="button_onmouseover()"
                                                                     onMouseOut="button_onmouseout()">
                </td>
            </tr>

        </form>
    </table>

</fieldset>
<fieldset>
    <legend> 合作项目信息</legend>
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
