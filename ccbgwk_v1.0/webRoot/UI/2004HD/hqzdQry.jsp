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
    <script language="javascript" src="hqzdQry.js"></script>
    <%
          String operId = (String)session.getAttribute("operatorId");
       String strSql =  " select t.saccno ,t.origbranch,t.spersonid,t.sname  ,t.sopaccdate,t.sendaccdat," +
               " t.currbala,t.sopacctell,t.sendacctel from dccq_smac0 t where 1=1 " ;
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("ActionTable");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setField("�ʺ�","text","8","saccno","true","0");
       dbGrid.setField("��������","text","6","origbranch","true","0");
       dbGrid.setField("���֤��","text","10","spersonid","true","0");
       dbGrid.setField("����","text","6","sname","true","0");
       dbGrid.setField("��������","text","10","sopaccdate","true","0");
       dbGrid.setField("��������","text","11","sendaccdat","true","0");
       dbGrid.setField("��ǰ���","text","10","currbala","true","0");
       dbGrid.setField("��������Ա��","text","10","sopacctell","true","0");
       dbGrid.setField("��������Ա��","text","8","sendacctel","true","0");
       dbGrid.setpagesize(50);
       dbGrid.setWhereStr(" and 1=2 ");
       dbGrid.setCheck(false);
        //////���ݼ���ť
       dbGrid.setdataPilotID("datapilot");
       dbGrid.setbuttons("moveFirst,prevPage,nextPage,moveLast");
    %>
  </head>
  <body bgcolor="#ffffff" onload="body_resize();" class="Bodydefault">

  <fieldset>
      <legend>
        ��ѯ����
      </legend>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" >
        <form id="queryForm" name="queryForm" method="post" action="dargpQry.jsp">
        <tr height="20">
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              �ʺ�
              <input type="hidden" value="dccq_smac0" id="hidTabName" name="hidTabName"/>
              <input type="hidden" value="2004����������ѯ" id="hidRMK" name="hidRMK" />
              <input type="hidden" value="<%=operId%>" id="hidOpId" name="hidOpId"/>
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input">
            <input style="width:90%" type="text" id="acctno" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              ��������
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
              ���֤��
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input">
              <input type="text" style="width:90%;" id="custNo" name="custNo" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" nowrap="nowrap" class="lbl_right_padding">��������</td>
            <td width="30%" nowrap="nowrap" class="data_input"><input type="text" id="date" name="date"
                                                                      onClick="WdatePicker()" fieldType="date"
                                                                      style="width:90%"></td>
          <td width="10%" align="center" nowrap="nowrap">
            <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click()" value="����">
          </td>
        </tr>
        <tr>
            <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
                ����
            </td>
            <td width="30%" align="right" nowrap="nowrap" class="data_input">
              <input type="text" style="width:90%;" id="custName" name="custName" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td colspan="2" width="45%">&nbsp;</td>
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