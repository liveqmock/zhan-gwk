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
       dbGrid.setField("��������","text","8","ORIG","true","0");
       dbGrid.setField("����","text","6","CKIND","true","0");
       dbGrid.setField("�ʺ�","text","10","CARDNO","true","0");
       dbGrid.setField("������","text","6","AMT","true","0");
       dbGrid.setField("��������","text","10","STRANDATE","true","0");
       dbGrid.setField("��ǰ���","text","11","CURRBALA","true","0");
       dbGrid.setField("�ϴ����","text","10","LASTBL","true","0");
        dbGrid.setField("����Ա","text","10","OPERATOR","true","0");
       dbGrid.setField("��������","text","8","TSNACC","true","0");
       dbGrid.setField("��ˮ��","text","8","FLOWNO","true","0");
       dbGrid.setField("������","text","10","TRADECODE","true","0");
       dbGrid.setField("�ո���־","text","8","DWFLAG","true","0");
       dbGrid.setField("������־","text","8","DELETE1","true","0");
       dbGrid.setField("�����ױ�־","text","10","CARDFLAG","true","0");

       dbGrid.setpagesize(50);
       dbGrid.setWhereStr(" and 1=2 ");
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
        <form id="queryForm" name="queryForm" method="post" action="saca0Qry.jsp">
        <tr height="20">
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              �ʺ�
              <input type="hidden" value="dccq_saca0" id="hidTabName" name="hidTabName"/>
              <input type="hidden" value="2004������ˮ��ѯ" id="hidRMK" name="hidRMK" />
              <input type="hidden" value="<%=operId%>" id="hidOpId" name="hidOpId"/>
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3">
            <input style="width:90%" type="text" id="acctno" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
              ��������
          </td>
          <td width="30%" align="right" nowrap="nowrap" class="data_input">
              <input type="text" style="width:90%;" id="bankNo" name="bankNo" size="40" class="ajax-suggestion url-getLoanPull.jsp">
          </td>
          <td width="10%" align="center" nowrap="nowrap">
            <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click()" value="����">
          </td>
        </tr>
        <tr>

          <td width="15%" nowrap="nowrap" class="lbl_right_padding">����������</td>
            <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="busidate1" name="busidate1"
                                                                      onClick="WdatePicker()" fieldType="date"
                                                                      style="width:80%"></td>
            <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
            <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="busidate2" name="busidate2"
                                                                      onClick="WdatePicker()" fieldType="date"
                                                                      style="width:80%">
            </td>
            <td colspan="2" width="45%" align="right" nowrap="nowrap">
              &nbsp;
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