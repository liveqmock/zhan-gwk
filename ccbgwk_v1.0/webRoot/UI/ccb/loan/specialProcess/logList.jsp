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
//        OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//        String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

        DBGrid dbGrid = new DBGrid();
        dbGrid.setGridID("gwkTab");
        dbGrid.setGridType("edit");
        
        //ֻ�ܱ༭��Щδ������Ѻ�Ǽǵ�
        String sql = "select t.tablename,t.tablepk,t.tasktype,t.tasktime,t.operid from ls_taskinfo t where 1=1  ";
        dbGrid.setfieldSQL(sql);
        
        dbGrid.setField("����", "center", "9", "tablename", "true", "0");
        dbGrid.setField("������ֵ", "center", "12", "tablepk", "true", "0");
        dbGrid.setField("��������", "center", "10", "tasktype", "true", "0");
        dbGrid.setField("����ʱ��", "center", "6", "tasktime", "true", "0");
        dbGrid.setField("�����û�", "text", "12", "operid", "true", "0");
        dbGrid.setWhereStr(" order by t.tablename desc");
        dbGrid.setpagesize(50);
        dbGrid.setdataPilotID("datapilot");
        dbGrid.setbuttons("moveFirst,prevPage,nextPage,moveLast");
  %>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
<legend> ��ѯ���� </legend>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <form id="queryForm" name="queryForm">
    <tr height="20">
      <td width="8%" class="lbl_right_padding"> ����ʱ�� </td>
      <td width="16%" class="data_input" ><input type="text" id="TASKTIME" name="TASKTIME" value="" style="width:90%"
                                                      onClick="WdatePicker()" fieldType="date"/>
      </td>
      <td width="1%" class="lbl_right_padding">��</td>
      <td width="16%" class="data_input" ><input type="text" id="TASKTIME2" name="TASKTIME2" value="" style="width:90%"
                                                      onClick="WdatePicker()" fieldType="date"/>
      </td>
      <td width="8%"  class="lbl_right_padding">��������</td>
      <td width="18%"  class="data_input">
          <input type="text" id="dbtabName" style="width:91%;" />
      </td>
      <td  align="center" nowrap="nowrap">
          <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click(document.queryForm)" value="����">
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
