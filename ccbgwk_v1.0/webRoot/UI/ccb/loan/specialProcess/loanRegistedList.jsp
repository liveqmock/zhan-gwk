<!--
/*********************************************************************
* 功能描述: 抵押办妥前信息管理，已进行抵押登记的贷款列表
* 作 者: leonwoo
* 开发日期: 2010/01/16
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

    <script language="javascript" src="loanRegistedList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/suggest/js/ajaxSuggestions.js"></script>
    <style type="text/css" media="screen">
        @import url("/UI/support/suggest/css/ajax-suggestions.css");
    </style>
    <script type="text/javascript">
        // 把pulldown值复制到input中
        function setPullToInput(elm) {
            document.getElementById("cust_name").value = elm.innerText;
            document.getElementById("cust_name").focus();
        }
    </script>
</head>
<%
    String menuAction = "";
    if (request.getParameter(CcbLoanConst.MENU_ACTION) != null) {
        menuAction = request.getParameter(CcbLoanConst.MENU_ACTION);
    }
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("loanRegistedTab");
    dbGrid.setGridType("edit");
    // 右连接，以抵押信息表为主表
    String sql = "select b.mortecentercd, "
            + " (select deptname from ptdept where deptid=a.bankid) as deptname,"
            + " a.loanid,a.cust_name,a.RT_ORIG_LOAN_AMT,b.mortid,b.mortdate,b.keepcont,b.mortstatus,"
            + " a.nbxh "
            + " from ln_loanapply a right join ln_mortinfo b  on a.loanid=b.loanid where  1=1 "
            + "  ";
    dbGrid.setfieldSQL(sql);

    dbGrid.setField("交易中心", "dropdown", "10", "MORTECENTERCD", "true", "MORTECENTERCD");
    dbGrid.setField("机构", "center", "10", "deptname", "true", "0");

    dbGrid.setField("贷款申请序号", "text", "16", "loanid", "true", "0");
    dbGrid.setField("借款人姓名", "center", "10", "cust_name", "true", "0");
    dbGrid.setField("贷款金额", "money", "10", "RT_ORIG_LOAN_AMT", "true", "0");
    dbGrid.setField("抵押编号", "text", "10", "mortid", "true", "0");
    dbGrid.setField("抵押日期", "center", "10", "mortdate", "true", "0");
    dbGrid.setField("保管内容", "dropdown", "12", "keepcont", "true", "KEEPCONT");
    dbGrid.setField("抵押状态", "dropdown", "10", "mortstatus", "true", "MORTSTATUS");
    dbGrid.setField("内部序号", "center", "15", "nbxh", "false", "0");
//    dbGrid.setWhereStr(" order by b.mortdate desc,a.cust_py asc ");
    dbGrid.setWhereStr(" and a.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) order by b.mortecentercd, a.bankid, b.mortid ");

    dbGrid.setpagesize(50);
    dbGrid.setdataPilotID("datapilot");
    if (menuAction.equals(CcbLoanConst.MENU_SELECT)) {
        dbGrid.setbuttons("导出Excel=excel,查看贷款=loanQuery,查看抵押=query,moveFirst,prevPage,nextPage,moveLast");
    } else {
        dbGrid.setbuttons("导出Excel=excel,查看贷款=loanQuery,查看抵押=query,编辑抵押=editRecord,删除抵押=deleteRecord,moveFirst,prevPage,nextPage,moveLast");
    }
%>
<body bgcolor="#ffffff" onload="body_resize()" onresize="body_resize();" class="Bodydefault">

<fieldset>
    <legend>
        查询条件
    </legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <!-- 抵押编号  该隐藏字段是为了删除之用 -->
            <input type="hidden" id="mortID" name="mortID" value=""/>
            <input type="hidden" id="loanID" name="loanID" value=""/>
            <!-- 系统日志之用 -->
            <input type="hidden" id="busiNode" name="busiNode" value=""/>
            <tr height="20">
                    <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                        借款人姓名
                    </td>
                    <td width="20%" align="right" nowrap="nowrap" class="data_input">
                        <input type="text" id="cust_name" name="cust_name" style="width:90%"
                               class="ajax-suggestion url-getPull.jsp">
                    </td>

                    <td width="10%" nowrap="nowrap" class="lbl_right_padding">交易中心</td>
                    <td width="20%" nowrap="nowrap" class="data_input">
                        <%
                            ZtSelect zs = new ZtSelect("mortecentercd", "mortecentercd", "");
                            zs.addAttr("style", "width: 90%");
                            zs.addAttr("fieldType", "text");
                            zs.addOption("", "");
                            zs.addAttr("isNull", "false");
                            out.print(zs);
                        %>
                    </td>
                    <td width="10%" nowrap="nowrap" class="lbl_right_padding">机构</td>
                    <td width="30%" nowrap="nowrap" class="data_input">
                        <%
                            zs = new ZtSelect("bankid", "", omgr.getOperator().getDeptid());
                            zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                                    + " start with deptid = '" + omgr.getOperator().getDeptid() + "'"
                                    + " connect by prior deptid = parentdeptid");
                            zs.addAttr("style", "width: 90%");
                            zs.addAttr("fieldType", "text");
                            zs.addAttr("isNull", "false");
                            out.print(zs);
                        %>
                    </td>

                

                <td align="center" nowrap="nowrap">
                    <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button"
                           onClick="cbRetrieve_Click(document.queryForm)" onMouseOver="button_onmouseover()"
                           onMouseOut="button_onmouseout()" value="检索">
                    <input name="Input" class="buttonGrooveDisable" type="reset" value="重填"
                           onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()">
                </td>
            </tr>
        </form>
    </table>
</fieldset>

<fieldset>
    <legend>
        抵押信息
    </legend>
    <table width="100%">
        <tr>
            <td>
                <%=dbGrid.getDBGrid()%>
            </td>
        </tr>
    </table>
</fieldset>
<FIELDSET>
    <LEGEND>
        操作
    </LEGEND>
    <table width="100%" class="title1">
        <tr>
            <td align="right">
                <%=dbGrid.getDataPilot()%>
            </td>
        </tr>
    </table>
</FIELDSET>

<div id="search-result-suggestions">
    <div id="search-results">
    </div>
</div>
</body>
</html>
