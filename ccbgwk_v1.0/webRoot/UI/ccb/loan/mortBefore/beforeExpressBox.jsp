<!--
  /*********************************************************************
  *    ��������: ��Ѻ��Ϣ�����Ѻ��ϸҳ��;��ݵǼ�/�������
  *
  *    ��    ��:    leonwoo
  *    ��������:  2010/01/16
  *    �� �� ��:
  *    �޸�����:
  *    ��    Ȩ:    ��˾
  ***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK"%>
<%@include file="/global.jsp"%>
<%@ page import="com.ccb.dao.*" %>
<%@ page import="com.ccb.util.*"%>
<%
  // ��������
  String doType = "";
  
  if (request.getParameter("doType") != null)
    doType = request.getParameter("doType");
%>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����Ϣ</title>
<script language="javascript" src="/UI/support/tabpane.js"></script>
<script language="javascript" src="/UI/support/common.js"></script>
<script language="javascript" src="/UI/support/DataWindow.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>
<script language="javascript" src="beforeExpressBox.js"></script>
</head>
<body bgcolor="#ffffff" onLoad="formInit();"  class="Bodydefault" >
<div id="container">
  <form id="editForm" name="editForm" >
    <fieldset >
    <legend>�����Ϣ</legend>
    <table width="100%"  cellspacing="0" border="0">
      <!-- �������� -->
      <input type="hidden" id="doType" value="<%=doType%>">
      <tr>
        <td width="15%"  nowrap="nowrap" class="lbl_right_padding">���</td>
        <td width="35%"  class="data_input" ><input type="text" id="BOXID" name="BOXID"  value=""  style="width:90% " textLength="10" >
        </td>
      </tr>
      <tr>
        <td width="15%" nowrap="nowrap" class="lbl_right_padding">��ݱ�ע</td>
        <td width="35%" colspan="3" class="data_input"><textarea name="EXPRESSNOTE" rows="3"
                                                                   id="EXPRESSNOTE" style="width:96.4%"
                                                                   textLength="500"></textarea></td>
      </tr>
    </table>
    </fieldset>
    <fieldset >
    <legend>����</legend>
    <table width="100%" class="title1"  cellspacing="0">
      <tr >
        <td align="center" ><!--��ѯ-->
          <%if (doType.equals("select")) {      %>
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()" type="button" value="�ر�" onClick="window.close();">
          <%} else if (doType.equals("edit") || doType.equals("add")) {      %>
          <!--���ӣ��޸�-->
          <input id="savebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()" type="button" value="����" onClick="saveClick();">
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()" type="button" value="ȡ��" onClick="window.close();">
          <%} else if (doType.equals("delete")) {      %>
          <!--ɾ��-->
          <input id="deletebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()" type="button" value="ɾ��" onClick="deleteClick();">
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()" type="button" value="ȡ��" onClick="window.close();">
          <%}      %>
        </td>
      </tr>
    </table>
    </fieldset>
  </form>
</div>
</body>
</html>
