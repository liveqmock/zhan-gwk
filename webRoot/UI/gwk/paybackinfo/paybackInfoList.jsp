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
    <script language="javascript" src="paybackInfoList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>

</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("ActionTable");
    dbGrid.setGridType("edit");


    String sql = "select voucherid,account,cardname,amt,querydate,operid,operdate,status,paybackdate,remark from ls_paybackinfo paybackinfo" +
            " where 1=1 ";

    dbGrid.setfieldSQL(sql);
    dbGrid.setWhereStr(" order by voucherid ");

    dbGrid.setField("支付凭证编号", "text", "8", "voucherid", "true", "0");
    dbGrid.setField("公务卡卡号", "text", "8", "account", "true", "0");
    dbGrid.setField("公务卡制卡人", "text", "8", "cardname", "true", "0");
    dbGrid.setField("划款金额", "text", "6", "amt", "true", "0");
    dbGrid.setField("查询日期", "text", "6", "querydate", "true", "0");
    dbGrid.setField("操作人员", "text", "5", "operid", "true", "0");
    dbGrid.setField("操作时间", "text", "6", "operdate", "true", "0");
    dbGrid.setField("还款状态", "dropdown", "5", "status", "true", "PAYBACKSTATUS");
    dbGrid.setField("成功还款日期", "text", "8", "paybackdate", "true", "0");
     dbGrid.setField("备注", "text", "8", "remark", "true", "0");
    dbGrid.setpagesize(50);
    dbGrid.setdataPilotID("datapilot");

    dbGrid.setbuttons("导出Excel=excel,moveFirst,prevPage,nextPage,moveLast");

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
                <td width="15%" class="lbl_right_padding"> 支付凭证编号</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="voucherid" size="30" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 公务卡卡号</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="account" size="60" style="width:91% ">
                </td>
                 <td width="10%" align="right" nowrap="nowrap"><input name="cbRetrieve" type="button"
                                                                     class="buttonGrooveDisable" id="button"
                                                                     onClick="queryClick()"
                                                                     onMouseOver="button_onmouseover()"
                                                                     onMouseOut="button_onmouseout()" value="检 索">
                </td>
            </tr>
            <tr>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 公务卡制卡人</td>
                <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3"><input type="text" id="cardname"
                                                                                        size="60" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 还款状态</td>
                <td width="30%" class="data_input" colspan="3"><%
                    ZtSelect zs = new ZtSelect("status", "paybackstatus", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>
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
    <legend>还款信息明细</legend>
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
