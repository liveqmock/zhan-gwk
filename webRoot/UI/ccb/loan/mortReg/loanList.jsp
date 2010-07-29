<!--
/*********************************************************************
* ��������: ��Ѻ����ǰ��Ϣ����δ���е�Ѻ�ǼǵĴ����б�
* �� ��: leonwoo
* ��������: 2010/01/16
* �� �� ��:
* �޸�����:
* �� Ȩ: ��˾
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>
    <script language="javascript" src="loanList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
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
    dbGrid.setGridID("loanTab");
    dbGrid.setGridType("edit");

    String sql = "select a.nbxh,a.loanid,a.cust_name,a.RT_ORIG_LOAN_AMT,a.CUST_OPEN_DT,a.APLY_DT,"
            + " (select deptname from ptdept where deptid=a.bankid) as deptname "
            + " from ln_loanapply a where "
            + "  not exists (select 1 from ln_mortinfo b where b.loanid=a.loanid)  "
            + " and ("
            + "(a.cust_open_dt >= '2010-01-01' or a.aply_dt >= '2010-01-01' ) "
            + " and a.loanstate <> '2' "
            + " and a.apprstate not in ('08', '09', '07') "
            + " or  a.aply_dt is null"
            + " )"
            + " and a.deptid is not null ";

    dbGrid.setfieldSQL(sql);

    dbGrid.setField("�ڲ����", "text", "8", "nbxh", "false", "-1");
    dbGrid.setField("�����������", "center", "12", "loanid", "true", "0");
    dbGrid.setField("���������", "text", "8", "cust_name", "true", "0");
    dbGrid.setField("������", "money", "8", "RT_ORIG_LOAN_AMT", "true", "0");
    dbGrid.setField("��������", "center", "8", "CUST_OPEN_DT", "true", "0");
    dbGrid.setField("��������", "center", "8", "APLY_DT", "true", "0");
    dbGrid.setField("����", "center", "10", "deptname", "true", "0");
    //dbGrid.setField("�ļ��Ƿ���ʾ", "dropdown", "8", "isvisibled", "true", "BOOLTYPE");
    //dbGrid.setField("�ſ���ʽ", "dropdown", "8", "file_type", "true", "impawnLoanType");

    dbGrid.setWhereStr(" and a.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) order by a.CUST_OPEN_DT desc,APLY_DT desc,a.cust_py asc ");
    dbGrid.setpagesize(30);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setbuttons("�鿴����=query,��ӵ�Ѻ=appendRecod,moveFirst,prevPage,nextPage,moveLast");
%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
    <legend> ��ѯ����</legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <tr height="20">
                <%--

                          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
                                          ���������
                          </td>
                          <td width="70%" align="right" nowrap="nowrap" class="data_input" >
                            <input type="text" id="cust_name" size="40" class="ajax-suggestion url-getPull.jsp">
                          </td>
                --%>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> ���������</td>
                <td width="35%" align="right" nowrap="nowrap" class="data_input"><input type="text" id="cust_name"
                                                                                        name="cust_name"
                                                                                        style="width:90%"
                                                                                        class="ajax-suggestion url-getPull.jsp">
                </td>
                <%--

                    <td width="15%" nowrap="nowrap" class="lbl_right_padding">��������</td>
                    <td width="35%" nowrap="nowrap" class="data_input">
                        <%
                            ZtSelect zs = new ZtSelect("mortecentercd", "mortecentercd", "");
                            zs.addAttr("style", "width: 90%");
                            zs.addAttr("fieldType", "text");
                            zs.addOption("", "");
                //            zs.addAttr("isNull", "false");
                            out.print(zs);
                        %>
                    </td>
                --%>
                <td width="15%" nowrap="nowrap" class="lbl_right_padding">����</td>
                <td width="35%" nowrap="nowrap" class="data_input"><%
                    ZtSelect zs = new ZtSelect("bankid", "", omgr.getOperator().getDeptid());
                    zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                            + " start with deptid = '" + omgr.getOperator().getDeptid() + "'"
                            + " connect by prior deptid = parentdeptid");
                    zs.addAttr("style", "width: 90%");
                    zs.addAttr("fieldType", "text");
                    //zs.addOption("", "");
//            zs.addAttr("isNull", "false");
                    out.print(zs);
                %>
                </td>
                <td align="center" nowrap="nowrap"><input name="cbRetrieve" type="button" class="buttonGrooveDisable"
                                                          id="button" onClick="cbRetrieve_Click(document.queryForm)"
                                                          onMouseOver="button_onmouseover()"
                                                          onMouseOut="button_onmouseout()" value="����">
                    <input name="Input" class="buttonGrooveDisable" type="reset" value="����"
                           onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()">
                </td>
            </tr>
        </form>
    </table>
</fieldset>
<fieldset>
    <legend> ������Ϣ</legend>
    <table width="100%">
        <tr>
            <td><%=dbGrid.getDBGrid()%>
            </td>
        </tr>
    </table>
</fieldset>
<FIELDSET>
    <LEGEND> ����</LEGEND>
    <table width="100%" class="title1">
        <tr>
            <td align="right"><%=dbGrid.getDataPilot()%>
            </td>
        </tr>
    </table>
</FIELDSET>
<div id="search-result-suggestions">
    <div id="search-results"></div>
</div>
</body>
</html>
