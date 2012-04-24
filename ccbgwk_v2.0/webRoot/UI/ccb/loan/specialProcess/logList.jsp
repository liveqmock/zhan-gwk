<!--
  /*********************************************************************
  *    功能描述: 系统日志
  *    作          者: leonwoo
  *    开发日期: 2010/01/16
  *    修   改  人:
  *    修改日期:
  *    版          权: 公司
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
        
        //只能编辑那些未做过抵押登记的
        String sql = "select t.tablename,t.tablepk,t.tasktype,t.tasktime,t.operid from ls_taskinfo t where 1=1  ";
        dbGrid.setfieldSQL(sql);
        
        dbGrid.setField("表名", "center", "9", "tablename", "true", "0");
        dbGrid.setField("表主键值", "center", "12", "tablepk", "true", "0");
        dbGrid.setField("处理类型", "center", "10", "tasktype", "true", "0");
        dbGrid.setField("处理时间", "center", "6", "tasktime", "true", "0");
        dbGrid.setField("操作用户", "text", "12", "operid", "true", "0");
        dbGrid.setWhereStr(" order by t.tablename desc");
        dbGrid.setpagesize(50);
        dbGrid.setdataPilotID("datapilot");
        dbGrid.setbuttons("moveFirst,prevPage,nextPage,moveLast");
  %>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
<legend> 查询条件 </legend>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <form id="queryForm" name="queryForm">
    <tr height="20">
      <td width="8%" class="lbl_right_padding"> 操作时间 </td>
      <td width="16%" class="data_input" ><input type="text" id="TASKTIME" name="TASKTIME" value="" style="width:90%"
                                                      onClick="WdatePicker()" fieldType="date"/>
      </td>
      <td width="1%" class="lbl_right_padding">至</td>
      <td width="16%" class="data_input" ><input type="text" id="TASKTIME2" name="TASKTIME2" value="" style="width:90%"
                                                      onClick="WdatePicker()" fieldType="date"/>
      </td>
      <td width="8%"  class="lbl_right_padding">操作表名</td>
      <td width="18%"  class="data_input">
          <input type="text" id="dbtabName" style="width:91%;" />
      </td>
      <td  align="center" nowrap="nowrap">
          <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click(document.queryForm)" value="检索">
        <input name="Input" class="buttonGrooveDisable" type="reset" value="重填" >
      </td>
    </tr>
  </form>
</table>
</fieldset>
<fieldset>
<legend> 贷款信息 </legend>
<table width="100%">
  <tr>
    <td><%=dbGrid.getDBGrid()%> </td>
  </tr>
</table>
</fieldset>
<FIELDSET>
<LEGEND> 操作 </LEGEND>
<table width="100%" class="title1">
  <tr>
    <td align="right"><%=dbGrid.getDataPilot()%> </td>
  </tr>
</table>
</FIELDSET>
</body>
</html>
