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
    <script language="javascript" src="dqzdQry.js"></script>
    <%
          String operId = (String)session.getAttribute("operatorId");
       String strSql =  " select tt.opnbankno,tt.accno,tt.name,tt.idno,tt.opndate,tt.enddate,tt.opnteller" +
               ",tt.endteller,tt.dcapital,tt.period,tt.ckind,tt.newaccno from " +
               " ((select t1.opnbankno,t1.accno,t1.name,t1.idno,t1.opndate,t1.enddate,t1.opnteller" +
               ",t1.endteller,t1.dcapital,t1.period,t1.ckind,t1.newaccno from dccq_dma00 t1) " +
               " union all (select t2.opnbankno,t2.accno,t2.name,t2.idno,t2.opndate,t2.enddate," +
               " t2.opntelerno as opnteller,t2.endtelerno as endteller,t2.dcapital,t2.period,t2.ckind,t2.newaccno" +
               " from dccq_dma10 t2) union all (select t3.opnbankno,t3.accno,t3.name,t3.idno,t3.opndate," +
               "t3.enddate,t3.opnteller,t3.endteller,null as dcapital ,' ' as period ,' ' as ckind," +
               " ' ' as newaccno from dccq_dma20 t3)) tt where 1=1 ";
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("ActionTable");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setField("开户所号","text","6","opnbankno","true","0");
       dbGrid.setField("帐号","text","6","accno","true","0");
       dbGrid.setField("姓名","text","8","name","true","0");
       dbGrid.setField("身份证","text","10","idno","true","0");
       dbGrid.setField("开户日期","text","10","opndate","true","0");
       dbGrid.setField("销户日期","text","11","enddate","true","0");
       dbGrid.setField("开户操作员号","text","10","opnteller","true","0");
       dbGrid.setField("销户操作员号","text","8","endteller","true","0");
        dbGrid.setField("余额","text","8","dcapital","true","0");
        dbGrid.setField("存期","text","8","period","true","0");
        dbGrid.setField("币种","text","8","ckind","true","0");
        dbGrid.setField("转存新帐号","text","8","newaccno","true","0");
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
        <form id="queryForm" name="queryForm" method="post" action="dargpQry.jsp">
        <tr height="20">
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              帐号
               <input type="hidden" value="dccq_dma00(10&20)" id="hidTabName" name="hidTabName"/>
              <input type="hidden" value="2004定期主档查询" id="hidRMK" name="hidRMK" />
              <input type="hidden" value="<%=operId%>" id="hidOpId" name="hidOpId"/>
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input">
            <input style="width:90%" type="text" id="acctno" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              开户所号
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input">
              <input type="text" style="width:90%;" id="bankNo" name="bankNo" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="10%" align="center" nowrap="nowrap">
            &nbsp;
          </td>
        </tr>
        <tr>
           <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              身份证号
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input">
              <input type="text" style="width:90%;" id="custNo" name="custNo" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" nowrap="nowrap" class="lbl_right_padding">开户日期</td>
            <td width="30%" nowrap="nowrap" class="data_input"><input type="text" id="date" name="date"
                                                                      onClick="WdatePicker()" fieldType="date"
                                                                      style="width:90%"></td>
          <td width="10%" align="center" nowrap="nowrap">
            <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click()" value="检索">
          </td>
        </tr>
        <tr>
            <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
                姓名
            </td>
            <td width="30%" align="right" nowrap="nowrap" class="data_input">
              <input type="text" style="width:90%;" id="custName" name="custName" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td colspan="2" width="45%">&nbsp;</td>
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