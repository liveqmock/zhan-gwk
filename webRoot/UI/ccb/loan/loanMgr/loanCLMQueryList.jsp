<!--
/*********************************************************************
* ��������: �ͻ�������������ѯ
* �� ��:
* ��������:
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
<script language="javascript" src="loanCLMQueryList.js"></script>
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

    //Ĭ����ʾû�б���������ݺ��Լ����������
    // ֻ��ʾ���е�����
    String sql = "select a.nbxh,"
            + " (select deptname from ptdept where deptid=a.bankid)as deptname, "
            + " (select opername from ptoper where operid=a.custmgr_id) as custmgr_id,"
            + " a.loanid,a.cust_name,a.RT_ORIG_LOAN_AMT,a.CUST_OPEN_DT,a.APLY_DT,a.RT_TERM_INCR,a.recversion"
            + " from ln_loanapply a "
            + " where "
//            + "not exists (select 1 from ln_mortinfo b where b.loanid=a.loanid) and "
            +"  a.bankid='"+omgr.getOperator().getDeptid()+"'"
            +" and a.cust_open_dt>='2010-01-01' ";

    dbGrid.setfieldSQL(sql);
    dbGrid.setField("�ڲ����", "center", "15", "nbxh", "false", "-1");
    dbGrid.setField("����", "center", "10", "deptname", "true", "0");
    dbGrid.setField("�ͻ�����", "center", "10", "custmgr_id", "true", "0");
    dbGrid.setField("�����������", "text", "16", "loanid", "true", "0");
    dbGrid.setField("���������", "center", "10", "cust_name", "true", "0");
    dbGrid.setField("������", "money", "10", "RT_ORIG_LOAN_AMT", "true", "0");
    dbGrid.setField("��������", "center", "10", "CUST_OPEN_DT", "true", "0");
    dbGrid.setField("��������", "center", "10", "APLY_DT", "true", "0");
    dbGrid.setField("��������", "center", "10", "RT_TERM_INCR", "true", "0");
    dbGrid.setField("�汾��", "text", "10", "recversion", "false", "0");

    dbGrid.setWhereStr(" order by a.CUST_OPEN_DT desc,APLY_DT desc,a.cust_py asc ");
    dbGrid.setpagesize(30);
    dbGrid.setdataPilotID("datapilot");
    //dbGrid.setCheck(true);
    dbGrid.setbuttons("����Excel=excel,�鿴����=query,moveFirst,prevPage,nextPage,moveLast");
%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
<legend> ��ѯ����</legend>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <form id="queryForm" name="queryForm">
    <tr>
      <td width="10%" nowrap="nowrap" class="lbl_right_padding"> ���������</td>
      <td width="20%" nowrap="nowrap" class="data_input"><input type="text" id="cust_name" style="width:90% " ></td>
      <td width="10%" nowrap="nowrap" class="lbl_right_padding">����</td>
      <td width="20%" nowrap="nowrap" class="data_input"><%
                        ZtSelect zs = new ZtSelect("BANKID", "", omgr.getOperator().getDeptid());
                        zs.setSqlString("select deptid, LPad('��', (level - 1) * 2, '��') || deptname  from ptdept"
                                + " start with deptid = '"+omgr.getOperator().getDeptid()+"'"
                                + " connect by prior deptid = parentdeptid");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addAttr("onchange", "reSelect()");
                        //zs.setDefValue("371980000");
                        //zs.addOption("", "");
//                        zs.addAttr("isNull", "false");
                        out.print(zs);
                    %>
      </td>
      <td width="10%" nowrap="nowrap" class="lbl_right_padding">�ͻ�����</td>
      <td width="20%" nowrap="nowrap" class="data_input"><%
                        zs = new ZtSelect("CUSTMGR_ID", "", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
//                        zs.addAttr("isNull", "false");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
      <td  nowrap="nowrap"><input name="cbRetrieve" type="button"
                                                                                  class="buttonGrooveDisable"
                                                                                  id="button"
                                                                                  onClick="cbRetrieve_Click(document.queryForm)"
                                                                                  value="����">
      </td>
      <td  align="right" nowrap="nowrap"><input name="Input" class="buttonGrooveDisable" type="reset" value="����">
      </td>
    </tr>
    <%--
            <tr>
                <td width="100%" colspan="4" align="right" nowrap="nowrap"><input name="cbRetrieve" type="button"
                                                                                  class="buttonGrooveDisable"
                                                                                  id="button"
                                                                                  onClick="cbRetrieve_Click(document.queryForm)"
                                                                                  value="����">
                    <input name="Input" class="buttonGrooveDisable" type="reset" value="����">
                </td>
            </tr>--%>
  </form>
</table>
</fieldset>
<fieldset>
<legend> ������Ϣ</legend>
<table width="100%">
  <tr>
    <td><%=dbGrid.getDBGrid()%> </td>
  </tr>
</table>
</fieldset>
<FIELDSET>
<LEGEND> ����</LEGEND>
<table width="100%" class="title1">
  <tr>
    <td align="right"><%=dbGrid.getDataPilot()%> </td>
  </tr>
</table>
</FIELDSET>
<div id="search-result-suggestions">
  <div id="search-results"></div>
</div>
</body>
</html>
