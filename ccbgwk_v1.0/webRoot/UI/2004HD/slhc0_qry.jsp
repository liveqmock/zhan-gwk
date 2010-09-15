<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-8-23
  Time: 13:49:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp"%>
<script language="javascript" src="slhc0_qry.js"></script>
<html>
  <head><title></title>
    <script language="javascript" src="slhc0_qry.js"></script>
    <%
       String strSql = "select bankcode,bankname,bankcode2,branchname from dccq_slhc0 where 1=1 ";

       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("ActionTable");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setField("机构编号","text","6","bankcode","true","0");
       dbGrid.setField("机构名称","text","10","bankname","true","0");
       dbGrid.setField("部门编号","text","8","bankcode2","true","0");
       dbGrid.setField("部门名称","text","12","branchname","true","0");
       dbGrid.setWhereStr(" and 1=2");
       dbGrid.setpagesize(50);
       dbGrid.setCheck(false);
        //////数据集按钮
       dbGrid.setdataPilotID("datapilot");
       dbGrid.setbuttons("导出Excel=excel,moveFirst,prevPage,nextPage,moveLast");
    %>
  </head>
  <body bgcolor="#ffffff" onload="body_resize();" class="Bodydefault">
  <fieldset>
      <legend>
        查询条件
      </legend>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" >
        <form id="queryForm" name="queryForm" method="post" action="slhc0_qry.jsp">
        <tr height="20">
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              机构编号
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" >
            <input style="width:90%" type="text" id="bankcode" name="bankcode" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              机构名称
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3">
              <input type="text" style="width:90%;" id="bankname" name="bankname" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="10%" align="center" nowrap="nowrap">
            <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click()" value="检索">
          </td>
        </tr>
        <tr>
             <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              部门编号
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" >
            <input style="width:90%" type="text" id="bankcode2"  name="bankcode2" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              部门名称
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3">
              <input type="text" style="width:90%;" id="branchname" name="branchname" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="10%" align="center" nowrap="nowrap">
            <input name="Input" class="buttonGrooveDisable" type="reset" value="重填" >
          </td>
        </tr>
       </form>
      </table>
    </fieldset>
  <fieldset>
        <legend>
          查询结果
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
            <td id="cellButtons" align="right">
              <%=dbGrid.getDataPilot()%>
            </td>
          </tr>
        </table>
      </FIELDSET>

  </body>
</html>