<!--
/*********************************************************************
* ��������: ����¼�� ���ݲ��ã�
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
    <script language="javascript" src="loanAddList.js"></script>
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
    String commSql = "select a.nbxh,"
            + " (select deptname from ptdept where deptid=a.bankid)as deptname, "
            + " (select opername from ptoper where operid=a.custmgr_id) as custmgr_id,"
            + " a.loanid,a.cust_name,a.RT_ORIG_LOAN_AMT,a.CUST_OPEN_DT,a.APLY_DT,a.RT_TERM_INCR"
            + "  from ln_loanapply a  where 1=1 ";

    commSql += " and not exists (select 1 from ln_mortinfo b where b.loanid=a.loanid)  ";
    commSql += " and a.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) ";

    commSql += " and a.operdate=to_char(sysdate,'yyyy-MM-dd') ";

    dbGrid.setfieldSQL(commSql);

    dbGrid.setField("�ڲ����", "text", "10", "nbxh", "false", "-1");
    dbGrid.setField("����", "center", "5", "deptname", "true", "0");
    dbGrid.setField("�ͻ�����", "center", "5", "custmgr_id", "true", "0");
    dbGrid.setField("�����������", "text", "8", "loanid", "true", "0");
    dbGrid.setField("���������", "center", "5", "cust_name", "true", "0");
    dbGrid.setField("������", "money", "6", "RT_ORIG_LOAN_AMT", "true", "0");
    dbGrid.setField("��������", "center", "5", "CUST_OPEN_DT", "true", "0");
    dbGrid.setField("��������", "center", "5", "APLY_DT", "true", "0");
    dbGrid.setField("��������", "center", "5", "RT_TERM_INCR", "true", "0");
    //dbGrid.setField("���ʽ", "text", "20", "PAY_TYPE", "true", "0");

    dbGrid.setWhereStr(" order by a.CUST_OPEN_DT desc,APLY_DT desc,a.cust_py asc ");
    dbGrid.setpagesize(30);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setbuttons("�鿴����=query,��Ӵ���=appendRecod,�༭����=editRecord,ɾ������=deleteRecord,moveFirst,prevPage,nextPage,moveLast");
%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
    <legend> ��ѯ����</legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <!-- �����ֶΣ�ɾ��֮�� -->
            <input type="hidden" id="nbxh" name="nbxh"/>
            <!-- ��ˮ��־ʹ�� -->
            <input type="hidden" id="busiNode" name="busiNode" value=""/>
            <tr height="20">
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> ���������</td>
                <td width="70%" align="right" nowrap="nowrap" class="data_input"><input type="text" id="cust_name"
                                                                                        size="40"
                                                                                        class="ajax-suggestion url-getLoanPull.jsp">
                </td>
                <td align="center" nowrap="nowrap"><input name="cbRetrieve" type="button" class="buttonGrooveDisable"
                                                          id="button" onClick="cbRetrieve_Click(document.queryForm)"
                                                          value="����">
                    <input name="Input" class="buttonGrooveDisable" type="reset" value="����">
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
<script type="text/javascript">
    // Initialize the input highlight script
    //initInputHighlightScript();
</script>
</body>
</html>
