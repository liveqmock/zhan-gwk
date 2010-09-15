<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-8-26
  Time: 14:55:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp"%>
<html>
  <head><title></title>
       <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/js/basic.js"></script>
    <script language="javascript" src="/js/xmlHttp.js"></script>
    <script language="javascript" src="/js/dbgrid.js"></script>
    <script language="javascript" src="/js/dropdownData.js"></script>
    <script language="javascript" src="/js/dbutil.js"></script>
    <script language="javascript" src="dargsQry.js"></script>
    <%
          String operId = (String)session.getAttribute("operatorId");
       String strSql =  "select OpnBankNo,CKind,AccNo,Name,RegDate,IDName,IDNo,TellerNo1,AgentNo,NewAccNo " +
                                "from dccq_dargs where 1=1";
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("ActionTable");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setField("开户所号","text","8","OpnBankNo","true","0");
       dbGrid.setField("币种","text","6","CKind","true","0");
       dbGrid.setField("帐号","text","10","AccNo","true","0");
       dbGrid.setField("姓名","text","6","Name","true","0");
       dbGrid.setField("止付日期","text","10","RegDate","true","0");
       dbGrid.setField("证件类型","text","11","IDName","true","0");
       dbGrid.setField("证件号码","text","10","IDNo","true","0");
       dbGrid.setField("止付操作员","text","10","TellerNo1","true","0");
       dbGrid.setField("发生所号","text","8","AgentNo","true","0");
       dbGrid.setField("新帐号","text","8","NewAccNo","true","0");
       dbGrid.setpagesize(50);
       dbGrid.setWhereStr(" and 1=2 ");
       dbGrid.setCheck(false);
        //////数据集按钮
       dbGrid.setdataPilotID("datapilot");
       dbGrid.setbuttons("moveFirst,prevPage,nextPage,moveLast");
    %>
  </head>
  <body bgcolor="#ffffff" onload="body_resize();" class="Bodydefault">
  
  <fieldset>
      <legend>
        查询条件
      </legend>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" >
        <form id="queryForm" name="queryForm" method="post" action="dargsQry.jsp">
        <tr height="20">
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              帐号
               <input type="hidden" value="dccq_dargs" id="hidTabName" name="hidTabName"/>
              <input type="hidden" value="2004止付登记" id="hidRMK" name="hidRMK" />
              <input type="hidden" value="<%=operId%>" id="hidOpId" name="hidOpId"/>
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3">
            <input style="width:90%" type="text" id="acctno" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              开户所号
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input">
              <input type="text" style="width:90%;" id="bankNo" name="bankNo" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="10%" align="center" nowrap="nowrap">
            <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click()" value="检索">
          </td>
        </tr>
        <tr>
           <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              姓名
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3">
              <input type="text" style="width:90%;" id="custName" name="custName" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" nowrap="nowrap" class="lbl_right_padding">止付日期</td>
            <td width="30%" nowrap="nowrap" class="data_input"><input type="text" id="date" name="date"
                                                                      onClick="WdatePicker()" fieldType="date"
                                                                      style="width:90%"></td>
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