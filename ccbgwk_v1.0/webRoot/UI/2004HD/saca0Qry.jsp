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
    <script language="javascript" src="saca0Qry.js"></script>
    <%
          String operId = (String)session.getAttribute("operatorId");
       String strSql =  "select  ORIG,CKIND,CARDNO,AMT,STRANDATE,CURRBALA,LASTBL," +
                                "OPERATOR,TSNACC,FLOWNO,TRADECODE,DWFLAG,DELETE1,CARDFLAG  " +
                                "from dccq_saca0  where 1=1";
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("ActionTable");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setField("开户所号","text","8","ORIG","true","0");
       dbGrid.setField("币种","text","6","CKIND","true","0");
       dbGrid.setField("帐号","text","10","CARDNO","true","0");
       dbGrid.setField("发生额","text","6","AMT","true","0");
       dbGrid.setField("交易日期","text","10","STRANDATE","true","0");
       dbGrid.setField("当前余额","text","11","CURRBALA","true","0");
       dbGrid.setField("上次余额","text","10","LASTBL","true","0");
        dbGrid.setField("操作员","text","10","OPERATOR","true","0");
       dbGrid.setField("交易所号","text","8","TSNACC","true","0");
       dbGrid.setField("流水号","text","8","FLOWNO","true","0");
       dbGrid.setField("交易码","text","10","TRADECODE","true","0");
       dbGrid.setField("收付标志","text","8","DWFLAG","true","0");
       dbGrid.setField("更正标志","text","8","DELETE1","true","0");
       dbGrid.setField("卡交易标志","text","10","CARDFLAG","true","0");

       dbGrid.setpagesize(50);
       dbGrid.setWhereStr(" and 1=2 ");
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
        <form id="queryForm" name="queryForm" method="post" action="saca0Qry.jsp">
        <tr height="20">
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              帐号
              <input type="hidden" value="dccq_saca0" id="hidTabName" name="hidTabName"/>
              <input type="hidden" value="2004活期流水查询" id="hidRMK" name="hidRMK" />
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

          <td width="15%" nowrap="nowrap" class="lbl_right_padding">交易日期起</td>
            <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="busidate1" name="busidate1"
                                                                      onClick="WdatePicker()" fieldType="date"
                                                                      style="width:80%"></td>
            <td width="2%" nowrap="nowrap" class="lbl_right_padding">至</td>
            <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="busidate2" name="busidate2"
                                                                      onClick="WdatePicker()" fieldType="date"
                                                                      style="width:80%">
            </td>
            <td colspan="2" width="45%" align="right" nowrap="nowrap">
              &nbsp;
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