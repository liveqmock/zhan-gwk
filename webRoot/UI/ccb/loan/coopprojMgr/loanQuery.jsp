<!--
/*********************************************************************
* ��������: ���֤�������ѯ
* �� ��:
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

    <script language="javascript" src="loanQuery.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
</head>
<%
    //    String proj_nbxh = "";
//    if (request.getParameter("proj_nbxh") != null) {
//        proj_nbxh = request.getParameter("proj_nbxh");
//    }
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("loanRegistedTab");
    dbGrid.setGridType("edit");

    // �����ӣ��Ե�Ѻ��Ϣ��Ϊ����
    String sql = "select " +
            "       (select deptname from ptdept where deptid=a.bankid)as bankid ," +
            "       c.proj_name," +
            "       a.cust_name," +
            "       a.rt_orig_loan_amt," +
            "       (select code_desc from ln_odsb_code_desc where code_type_id='053' and code_id=a.LN_TYP) as ln_typ_name," +
            "       c.assuamtpercent," +
            "       (a.rt_orig_loan_amt * c.assuamtpercent/100) as assuamttemp," +
            "       c.assuamt," +
            "       c.bankflag," +
            "       a.proj_no," +
            "       b.mortid," +
            "       a.loanid," +
            "       a.nbxh," +
            "       c.proj_nbxh" +
            "  from ln_loanapply a, ln_mortinfo b, ln_coopproj c" +
            " where a.loanid = b.loanid" +
            "   and a.proj_no = c.proj_no" +
            "   and (b.mortstatus = '10' or b.mortstatus = '20')"
			+"  ";

    dbGrid.setfieldSQL(sql);

//    dbGrid.setField("��������", "dropdown", "10", "MORTECENTERCD", "true", "MORTECENTERCD");
    dbGrid.setField("����", "center", "10", "bankid", "true", "0");
    dbGrid.setField("������Ŀ", "text", "25", "proj_name", "true", "0");
    dbGrid.setField("���������", "center", "10", "cust_name", "true", "0");
    dbGrid.setField("������", "money", "10", "rt_orig_loan_amt", "true", "0");
    dbGrid.setField("��������", "center", "10", "ln_typ_name", "true", "0");
    dbGrid.setField("��֤�����", "money", "10", "assuamtpercent", "true", "0");
    dbGrid.setField("Ӧ����֤��", "money", "10", "assuamttemp", "true", "0");
    dbGrid.setField("��֤���˺�", "center", "12", "assuamt", "true", "0");
    dbGrid.setField("��Ŀ��������", "dropdown", "10", "bankflag", "true", "BANKFLAG");
    dbGrid.setField("������Ŀ���", "text", "15", "proj_no", "true", "0");
    dbGrid.setField("��Ѻ���", "text", "10", "mortid", "true", "0");
    dbGrid.setField("���������", "text", "15", "loanid", "true", "0");
    dbGrid.setField("�����ڲ����", "text", "12", "nbxh", "false", "0");
    dbGrid.setField("��Ŀ�ڲ����", "text", "12", "proj_nbxh", "false", "0");

//    dbGrid.setWhereStr(" order by b.mortdate desc,a.cust_py asc ");
    dbGrid.setWhereStr(" and a.bankid in(select deptid from ptdept start with deptid='"+omgr.getOperator().getDeptid()+"' connect by prior deptid=parentdeptid) order by a.bankid, c.proj_no, a.loanid ");

    dbGrid.setpagesize(50);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setbuttons("����Excel=excel,�鿴����=loanQuery,�鿴��Ѻ=query,�鿴��Ŀ=projQuery,moveFirst,prevPage,nextPage,moveLast");
%>
<body bgcolor="#ffffff" onload="body_resize()" onresize="body_resize();" class="Bodydefault">

<fieldset>
    <legend>
        ��ѯ����
    </legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <input type="hidden" id="mortID" name="mortID" value="">
            <input type="hidden" id="loanID" name="loanID" value="">
            
            <tr height="20">
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">����</td>
                <td width="30%" nowrap="nowrap" class="data_input">
                    <%
                        ZtSelect zs = new ZtSelect("bankid", "", omgr.getOperator().getDeptid());
                        zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                                + " start with deptid = '" + omgr.getOperator().getDeptid() + "'"
                                + " connect by prior deptid = parentdeptid");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        out.print(zs);
                    %>
                </td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">�ſʽ</td>
                <td width="30%" nowrap="nowrap" class="data_input" >
                    <%
                        zs = new ZtSelect("releasecondcd", "releasecondcd", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        //zs.addAttr("isNull","false");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
                </td>


            <td align="center" nowrap="nowrap">
                <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button"
                       onClick="cbRetrieve_Click(document.queryForm)" onMouseOver="button_onmouseover()"
                       onMouseOut="button_onmouseout()" value="����">
            </td>
            </tr>

            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤������Ƿ�Ϊ��</td>
                <td width="30%" class="data_input"><%
                    zs = new ZtSelect("booltype", "booltype", "");
                    zs.addAttr("style", "width: 90%");
                    zs.addAttr("fieldType", "text");
                    //zs.addAttr("isNull","false");
                    zs.addOption("", "");
                    out.print(zs);
                %>
                </td>

                <td width="20%" nowrap="nowrap" class="lbl_right_padding">��������</td>
                <td width="30%" nowrap="nowrap" class="data_input">
                    <%
                        zs = new ZtSelect("ln_typ", "", "");
                        zs.setSqlString("select code_id,code_desc from ln_odsb_code_desc where code_type_id='053' order by code_id");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
                </td>

                <%--

                                <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                                    ���������
                                </td>
                                <td width="20%" align="right" nowrap="nowrap" class="data_input">
                                    <input type="text" id="cust_name" name="cust_name" style="width:90%"
                                           class="ajax-suggestion url-getPull.jsp">
                                </td>

                                <td width="10%" nowrap="nowrap" class="lbl_right_padding">��������</td>
                                <td width="20%" nowrap="nowrap" class="data_input">
                                    <%
                                         zs = new ZtSelect("mortecentercd", "mortecentercd", "");
                                        zs.addAttr("style", "width: 90%");
                                        zs.addAttr("fieldType", "text");
                                        zs.addOption("", "");
                                        zs.addAttr("isNull", "false");
                                        out.print(zs);
                                    %>
                                </td>

                --%>


                <td align="center" nowrap="nowrap">
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
