<!--
  /*********************************************************************
  *    ��������: ϵͳ��־
  *    ��          ��: leonwoo
  *    ��������: 2010/01/16
  *    ��   ��  ��:
  *    �޸�����:
  *    ��          Ȩ: ��˾
  ***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK"%>
<%@include file="/global.jsp"%>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<script language="javascript" src="logList.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>
</head>
<%
        OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

        DBGrid dbGrid = new DBGrid();
        dbGrid.setGridID("loanTab");
        dbGrid.setGridType("edit");
        
        //ֻ�ܱ༭��Щδ������Ѻ�Ǽǵ�
        String sql = "select taskid,"
        +" (select deptname from ptdept where deptid=bankid)as bankid,"
        +" (select opername from ptoper t where t.operid=a.operid)as operid,"
        +" loanRecordid,"
        +" (select enuitemlabel from ptenudetail where enutype='BUSINODE' and enuitemvalue =tasktype) as tasktype,"
        +" to_char(tasktime,'YYYY-MM-DD HH24:mi:ss'),loan_id from ln_taskinfo a where 1=1  ";
        dbGrid.setfieldSQL(sql);
        
        dbGrid.setField("�ڲ����", "center", "2", "taskid", "false", "-1");
        dbGrid.setField("����", "center", "12", "bankid", "true", "0");
        dbGrid.setField("�����û�", "center", "10", "operid", "true", "0");
        dbGrid.setField("����", "center", "6", "loanRecordid", "true", "0");
        dbGrid.setField("ҵ������", "text", "12", "tasktype", "true", "0");
        dbGrid.setField("����ʱ��", "center", "10", "tasktime", "true", "0");
        dbGrid.setField("ҵ������", "center", "12", "loan_id", "true", "0");
        
        
        dbGrid.setWhereStr(" and a.bankid in(select deptid from ptdept start with deptid='"+omgr.getOperator().getDeptid()+"' connect by prior deptid=parentdeptid) order by tasktime desc");
        dbGrid.setpagesize(50);
        dbGrid.setdataPilotID("datapilot");
        dbGrid.setbuttons("moveFirst,prevPage,nextPage,moveLast");
  %>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
<legend> ��ѯ���� </legend>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <form id="queryForm" name="queryForm">
    <input id="defaultBankID" type="hidden" value="<%=deptId%>"/>
    <tr height="20">
      <td width="8%" class="lbl_right_padding"> ����ʱ�� </td>
      <td width="16%" class="data_input" ><input type="text" id="TASKTIME" name="TASKTIME" value="" style="width:90%"
                                                      onClick="WdatePicker()" fieldType="date"/>
      </td>
      <td width="1%" class="lbl_right_padding">��</td>
      <td width="16%" class="data_input" ><input type="text" id="TASKTIME2" name="TASKTIME2" value="" style="width:90%"
                                                      onClick="WdatePicker()" fieldType="date"/>
      </td>
      <td width="8%"  class="lbl_right_padding">����</td>
      <td width="18%"  class="data_input"><%
                        ZtSelect zs = new ZtSelect("BANKID", "", omgr.getOperator().getDeptid());
                        zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                                + " start with deptid = '"+omgr.getOperator().getDeptid()+"'"
                                + " connect by prior deptid = parentdeptid");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addAttr("onchange", "reSelect()");
                        out.print(zs);
                    %>
      </td>
      <td width="8%" class="lbl_right_padding">����Ա</td>
      <td width="10%" class="data_input"><%
                        zs = new ZtSelect("operid", "", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
      <td  align="center" nowrap="nowrap"><input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click(document.queryForm)" value="����">
        <input name="Input" class="buttonGrooveDisable" type="reset" value="����" >
      </td>
    </tr>
  </form>
</table>
</fieldset>
<fieldset>
<legend> ������Ϣ </legend>
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
</body>
</html>
