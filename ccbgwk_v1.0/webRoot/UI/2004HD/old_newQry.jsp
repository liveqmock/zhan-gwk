<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-8-23
  Time: 13:49:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp"%>
<script language="javascript" src="old_newQry.js"></script>
<html>
  <head><title></title>
    <script language="javascript" src="old_newQry.js"></script>
    <%
       String strSql =  " select old_18,new_19,new_28,name,cardno from dccq_old_new where 1=1 ";
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("ActionTable");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setField("��18λ�ʺ�","text","8","old_18","true","0");
       dbGrid.setField("��19λ�ʺ�","text","8","new_19","true","0");
       dbGrid.setField("��28λ�ʺ�","text","14","new_28","true","0");
       dbGrid.setField("����","text","13","name","true","0");
       dbGrid.setField("����","text","8","cardno","true","0");
       dbGrid.setWhereStr(" and 1=2");
       dbGrid.setpagesize(50);
       dbGrid.setCheck(false);
        //////���ݼ���ť
       dbGrid.setdataPilotID("datapilot");
       dbGrid.setbuttons("����Excel=excel,moveFirst,prevPage,nextPage,moveLast");
    %>
  </head>
  <body bgcolor="#ffffff" onload="body_resize();" class="Bodydefault">
  <fieldset>
      <legend>
        ��ѯ����
      </legend>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" >
        <form id="queryForm" name="queryForm" method="post" action="old_newQry.jsp">
        <tr height="20">
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              ��18λ�ʺ�
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" >
            <input style="width:90%" type="text" id="old_18" name="old_18" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              ��19λ�ʺ�
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3">
              <input type="text" style="width:90%;" id="new_19" name="new_19" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="10%" align="center" nowrap="nowrap">
            <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click()" value="����">
          </td>
        </tr>
        <tr>
             <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              ����
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" >
            <input style="width:90%" type="text" id="cardNo"  name="cardNo" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              ����
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3">
              <input type="text" style="width:90%;" id="name" name="name" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="10%" align="center" nowrap="nowrap">
            <input name="Input" class="buttonGrooveDisable" type="reset" value="����" >
          </td>
        </tr>
       </form>
      </table>
    </fieldset>
  <fieldset>
        <legend>
         ��ѯ���
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
          ����
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