<%--
  Created by IntelliJ IDEA.
  User: gwk
  Date: 2010-8-4
  Time: 16:58:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp"%>
<%@ page import="pub.platform.db.*"%>
<html>
  <head>
      <title>
          公务卡信息
      </title>
      <script language="javascript" src="cardinfoquery.js"></script>
      <%
           String strSql =  " select cbinfo.account,cbinfo.cardname,bgcy.name as bgcyname," +
                   " cbinfo.gatheringbankacctname,cbinfo.gatheringbankacctcode," +
                   " cbinfo.idnumber,cbinfo.digest,cbinfo.createdate,cbinfo.startdate," +
                   " cbinfo.enddate,cbinfo.sentflag,cbinfo.status" +
                   " from ls_cardbaseinfo cbinfo left join ls_bdgagency bgcy on cbinfo.bdgagency=bgcy.code" +
                   " where 1=1 ";
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("cardInfoTab");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setWhereStr(" order by cbinfo.account");
       dbGrid.setField("卡号","text","12","account","true","0");
       dbGrid.setField("持卡人","text","6","cardname","true","0");
       dbGrid.setField("预算单位","text","14","bgcyname","true","0");
       dbGrid.setField("还款帐户名","text","13","gatheringbankacctname","true","0");
       dbGrid.setField("还帐户号","text","14","gatheringbankacctcode","true","0");
       dbGrid.setField("身份证号","text","12","idnumber","true","0");
       dbGrid.setField("用途","text","10","digest","true","0");
       dbGrid.setField("开卡日期","text","6","createdate","true","0");
       dbGrid.setField("有效起始日期","text","9","startdate","true","0");
       dbGrid.setField("有效终止日期","text","9","enddate","true","0");
       dbGrid.setField("是否发送","dropdown","8","sentflag","true","CARDSENDFLAG");
       dbGrid.setField("卡状态","dropdown","8","status","true","CARDSTATUS");
//       dbGrid.setfieldcn("卡号,持卡人,预算单位,还款帐户名,还帐户号,身份证号,用途,开卡日期,有效起始日期,有效终止日期," +
//               "");
       dbGrid.setpagesize(50);
       dbGrid.setCheck(false);
        //////数据集按钮
       dbGrid.setdataPilotID("datapilot");
       dbGrid.setbuttons("导出Excel=excel,moveFirst,prevPage,nextPage,moveLast");
      %>
  </head>
  <body onLoad="body_resize() " onResize="body_resize()" class="Bodydefault">
  <fieldset>
      <legend>
        查询条件
      </legend>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" >
        <form id="queryForm" name="queryForm">
        <tr height="20">
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
              卡号
            </td>
            <td width="20%" align="right" nowrap="nowrap" class="data_input" >
                <input style="width:90%" type="text" id="cardNo" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
              持卡人姓名
            </td>
            <td width="15%" align="right" nowrap="nowrap" class="data_input" >
                 <input style="width:90%" type="text" id="cust_name" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
              预算单位
            </td>
            <td width="20%" align="right" nowrap="nowrap" class="data_input">
                <input type="text" style="width:90%;" id="departmentName" name="departmentName"
                       value="" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td align="center">
                <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click()" value="检索">
            </td>
        </tr>
        <tr height="20">
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                身份证ID
            </td>
            <td width="20%" align="right" nowrap="nowrap" class="data_input">
               <input type="text" style="width:90%;" id="personalID" name="personalID" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                卡状态
            </td>
            <td width="15%" align="right" nowrap="nowrap" class="data_input">
                <%
                    ZtSelect zs = new ZtSelect("status", "CARDSTATUS", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>
            </td>
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">是否发送</td>
            <td width="20%" align="right" nowrap="nowrap" class="data_input">
                <%
                    ZtSelect zs1 = new ZtSelect("sentflag", "CARDSENDFLAG", "");
                    zs1.addAttr("style", "width: 91%");
                    zs1.addAttr("fieldType", "text");
                    zs1.addOption("", "");
                    out.print(zs1);
                %>
            </td>
            <td  align="center" nowrap="nowrap">
                <input name="Input" class="buttonGrooveDisable" type="reset" value="重填" >
            </td>
        </tr>
       </form>
      </table>
    </fieldset>
    <fieldset>
      <legend>
        个人信息
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

    <div id="search-result-suggestions">
      <div id="search-results">
      </div>
     </div>
  </body>
</html>