<!--
/*********************************************************************
* ��������: ��Ѻ���׺���Ϣ����;ȡ�û�ִ����    (ȡ�û�ִ�Ǽ�)
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
<script language="javascript" src="receiptDateList.js"></script>
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
    dbGrid.setGridID("loanRegistedTab");
    dbGrid.setGridType("edit");

    String sql = "select b.MORTECENTERCD,"
            + "(select deptname from ptdept where deptid=a.bankid) as deptname,"
            + "a.loanid,a.cust_name,a.RT_ORIG_LOAN_AMT,b.mortid,b.RECEIPTDATE,b.RECEIPTID, "
            + " a.nbxh "
            + " from ln_loanapply a,ln_mortinfo b where  "
            + " a.loanid = b.loanid " +
            " and (b.releasecondcd ='02' or b.releasecondcd ='04') "  +
            " and (b.mortstatus='" + CcbLoanConst.MORT_FLOW_REGISTED +
            "' or b.mortstatus='" + CcbLoanConst.MORT_FLOW_SENT +
            "' )  "
            + "  ";
            // ��Ѻ�������Ĺ���
//            + " and b.MORTECENTERCD  in('10','08','09','11','12')";
//            + " and b.MORTECENTERCD not in('10','08','09','11','12')";

    dbGrid.setfieldSQL(sql);

    dbGrid.setField("��������", "dropdown", "8", "MORTECENTERCD", "true", "MORTECENTERCD");
    dbGrid.setField("����", "text", "8", "deptname", "true", "0");

    dbGrid.setField("�����������", "text", "12", "loanid", "true", "0");
    dbGrid.setField("���������", "text", "8", "cust_name", "true", "0");
    dbGrid.setField("������", "money", "8", "RT_ORIG_LOAN_AMT", "true", "0");
    dbGrid.setField("��Ѻ���", "text", "8", "mortid", "true", "-1");
    dbGrid.setField("ȡ�ػ�ִ����", "text", "10", "RECEIPTDATE", "true", "0");
    dbGrid.setField("��ִ���", "text", "8", "RECEIPTID", "true", "0");
//    dbGrid.setField("����", "text", "6", "deptname", "true", "0");
    dbGrid.setField("nbxh", "text", "6", "nbxh", "false", "0");
//    dbGrid.setWhereStr(" order by a.cust_py asc ");
    dbGrid.setWhereStr(" and a.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) order by b.mortecentercd, a.bankid, b.mortid ");
    dbGrid.setpagesize(30);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setbuttons("�鿴����=loanQuery,�鿴��Ѻ=query,��ִ�Ǽ�=editRecord,moveFirst,prevPage,nextPage,moveLast");
%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
<legend> ��ѯ���� </legend>
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
      <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding"> ��������� </td>
      <td width="20%" align="right" nowrap="nowrap" class="data_input"><input type="text" id="cust_name" name="cust_name" style="width:90%">
        <%--class="ajax-suggestion url-getPull.jsp">--%>
      </td>
      <td width="10%" nowrap="nowrap" class="lbl_right_padding">��������</td>
      <td width="20%" nowrap="nowrap" class="data_input"><%
                        ZtSelect zs = new ZtSelect("mortecentercd", "mortecentercd", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        zs.addAttr("isNull", "false");
                        out.print(zs);
                    %>
      </td>
      <td width="10%" nowrap="nowrap" class="lbl_right_padding">����</td>
      <td width="30%" nowrap="nowrap" class="data_input"><%
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
      <td align="center" nowrap="nowrap"><input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button"
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
<legend> ��Ѻ��Ϣ </legend>
<table width="100%">
  <tr>
    <td><%=dbGrid.getDBGrid()%> </td>
  </tr>
</table>
</fieldset>
<FIELDSET>
<LEGEND> ���� </LEGEND>
<table width="100%" class="title1">
  <tr>
    <td align="right"><%=dbGrid.getDataPilot()%> </td>
  </tr>
</table>
</FIELDSET>
<div id="search-result-suggestions">
  <div id="search-results"> </div>
</div>
</body>
</html>
