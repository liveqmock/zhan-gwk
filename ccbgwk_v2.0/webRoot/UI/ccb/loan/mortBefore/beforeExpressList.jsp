<!--
/*********************************************************************
* ��������: ��Ѻ����ǰ��Ϣ����;��ݵ�Ѻ��Ϣ�Ǽ�
* �� ��: leonwoo
* ��������: 2010/01/16
* �� �� ��:
* �޸�����:
* �� Ȩ: ��˾
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@page import="com.ccb.util.CcbLoanConst" %>
<%@include file="/global.jsp" %>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>

    <script language="javascript" src="beforeExpressList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/common.js"></script>
    <script language="javascript" src="/UI/support/DataWindow.js"></script>

    <script language="javascript" src="/UI/support/suggest/js/ajaxSuggestions.js"></script>
    <style type="text/css" media="screen">
        @import url("/UI/support/suggest/css/ajax-suggestions.css");
    </style>

    <script type="text/javascript">
        // ��pulldownֵ���Ƶ�input��
        function setPullToInput(elm) {
            document.getElementById("cust_name").value = elm.innerText;
            document.getElementById("cust_name").focus();
        }

    </script>
</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("loanRegistedTab");
    dbGrid.setGridType("edit");

    String sql = "select a.nbxh,b.MORTECENTERCD,"
            + "(select deptname from ptdept where deptid=a.bankid) as deptname,"
            + "a.cust_name,a.RT_ORIG_LOAN_AMT,b.RELEASECONDCD,b.mortid,b.mortdate,b.BOXID,b.EXPRESSNOTE,b.recVersion,b.loanid"
            + " from ln_loanapply a,ln_mortinfo b where "
            + " a.loanid = b.loanid and b.mortstatus='" + CcbLoanConst.MORT_FLOW_REGISTED + "'  "
            + " and (b.sendflag <> '0' or sendflag is null)  "
            + "  "
            // ��Ѻ�������Ĺ���
            + " and b.MORTECENTERCD in(select mortecentercd from ln_morttype where typeflag='1' and deptid='"+deptId+"') ";

    dbGrid.setfieldSQL(sql);

    dbGrid.setField("nbxh", "text", "2", "nbxh", "false", "0");
    dbGrid.setField("��������", "dropdown", "6", "MORTECENTERCD", "true", "MORTECENTERCD");
    dbGrid.setField("����", "center", "7", "deptname", "true", "0");
    dbGrid.setField("����", "text", "5", "cust_name", "true", "0");
    dbGrid.setField("���", "money", "7", "RT_ORIG_LOAN_AMT", "true", "0");
    dbGrid.setField("�ſʽ", "dropdown", "7", "RELEASECONDCD", "true", "RELEASECONDCD");
    dbGrid.setField("��Ѻ���", "center", "7", "mortid", "true", "0");
    dbGrid.setField("��Ѻ����", "center", "7", "mortdate", "true", "0");
    dbGrid.setField("���", "center", "6", "boxid", "true", "0");
    dbGrid.setField("��ע", "text", "7", "EXPRESSNOTE", "true", "0");
    // ���ֶ����������·�ֹ��������ؼ��ֶΣ�����ɾ����
    dbGrid.setField("�汾��", "text", "4", "recVersion", "false", "0");
    dbGrid.setField("loanid", "text", "4", "loanid", "false", "0");
//    dbGrid.setWhereStr(" order by a.cust_py asc ");
    dbGrid.setWhereStr(" and a.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) order by b.mortecentercd, a.bankid, b.mortid ");
    dbGrid.setpagesize(100);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setCheck(true);
    dbGrid.setbuttons("����excel=expExcel,�鿴����=loanQuery,�鿴��Ѻ=query,���=editRecord,�������=batchEdit,moveFirst,prevPage,nextPage,moveLast");
%>
<body bgcolor="#ffffff" onload="body_resize()" onresize="body_resize();" class="Bodydefault">

<fieldset>
    <legend>
        ��ѯ����
    </legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
        <!-- ����ʹ�� -->
        <input type="hidden" id="expressType" name="expressType" value=""/>
            <tr height="20">

                <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                    ���������
                </td>
                <td width="20%" align="right" nowrap="nowrap" class="data_input">
                    <input type="text" id="cust_name" name="cust_name" style="width:90%">
                           <%--class="ajax-suggestion url-getPull.jsp">--%>
                </td>

                <td width="10%" nowrap="nowrap" class="lbl_right_padding">��������</td>
                <td width="20%" nowrap="nowrap" class="data_input">
                    <%
                        ZtSelect zs = new ZtSelect("mortecentercd", "mortecentercd", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        //zs.addAttr("isNull", "false");
                        out.print(zs);
                    %>
                </td>
                <td width="10%" nowrap="nowrap" class="lbl_right_padding">����</td>
                <td width="30%" nowrap="nowrap" class="data_input">
                    <%
                        zs = new ZtSelect("bankid", "", omgr.getOperator().getDeptid());
                        zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                                + " start with deptid = '" + omgr.getOperator().getDeptid() + "'"
                                + " connect by prior deptid = parentdeptid");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        //zs.addOption("", "");
                        //zs.addAttr("isNull", "false");
                        out.print(zs);
                    %>
                </td>


                <td align="center" nowrap="nowrap">
                    <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button"
                           onClick="cbRetrieve_Click(document.queryForm)" onMouseOver="button_onmouseover()"
                           onMouseOut="button_onmouseout()" value="����">
                    <input name="Input" class="buttonGrooveDisable" type="reset" value="����"
                           onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()">
                </td>
            </tr>
        </form>
    </table>
</fieldset>
<fieldset>
    <legend>
        ��Ѻ��Ϣ
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
        ����
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
