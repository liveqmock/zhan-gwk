<!--
  /*********************************************************************
  *    ��������: ��Ѻ��Ϣ�����Ѻ��ϸҳ��;��Ѻ��Ϣ�Ǽ�
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
  // �����������
  String loanID = "";
  // ��������
  String doType = "";
  // ��Ѻ���
  String mortID = "";
  // ������Ŀ����
  LNLOANAPPLY loan = null;
  
  if (request.getParameter("loanID") != null)
    loanID = request.getParameter("loanID");

  if (request.getParameter("doType") != null)
    doType = request.getParameter("doType");
  if ("add".equalsIgnoreCase(doType)) {
     // �Զ�ȡ����Ѻ���
     // //mortID = SeqUtil.getSeq(CcbLoanConst.MORTTYPE);
     // ȡ���ſʽ
     loan = LNLOANAPPLY.findFirst(" where loanid='"+loanID+"'");
     
     if( loan == null){
       loan = new LNLOANAPPLY();
     }
  } else {
    if (request.getParameter("mortID") != null)
      mortID = request.getParameter("mortID");
    // ��ʼ��ҳ��
    LNMORTINFO bean = LNMORTINFO.findFirst("where mortid='" + mortID + "'");
    if (bean != null){
      StringUtils.getLoadForm(bean,out);
    }
  }

  OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
 
%>
<html>


<head>

<META http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��Ѻ��Ϣ�Ǽ�</title>

<script language="javascript" src="/UI/support/tabpane.js"></script>
<script language="javascript" src="/UI/support/common.js"></script>
<script language="javascript" src="/UI/support/DataWindow.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>
<script language="javascript" src="mortRegEdit.js"></script>

</head>
<body bgcolor="#ffffff" onload="formInit();"  class="Bodydefault" >
<br>
<div id="container">
<form id="editForm" name="editForm" >
<fieldset > 
  <legend>��Ѻ��Ϣ�Ǽ�</legend>     
  <table width="100%"  cellspacing="0" border="0"> 
  	<!-- �������� -->
   	<input type="hidden" id="doType" value="<%=doType%>">
    <!-- �汾�� -->
    <input type="hidden" id="recVersion" value="">
    <tr> 
      <td width="20%"  nowrap="nowrap" class="lbl_right_padding">�����������</td> 
      <td width="30%"   class="data_input" > <input type="text" id="loanID" name="loanID"  value="<%=loanID%>"  style="width:96% " disabled="disabled" > </td>
<%--
      <td width="20%"  nowrap="nowrap" class="lbl_right_padding"></td>
      <td width="30%"  class="data_input" > </td> 
--%>
    </tr>
    <tr>
      <td width="20%"  nowrap="nowrap" class="lbl_right_padding">��Ѻ��������</td>
      <td width="30%"   class="data_input"><%
        ZtSelect zs = new ZtSelect("MORTECENTERCD", "MORTECENTERCD","01");
        zs.addAttr("style", "width: 40%");
        zs.addAttr("fieldType", "text");
        zs.addAttr("isNull", "false");
        //zs.addOption("", ""); 
        //zs.setDisplayAll(false); 
        out.print(zs);
      %><span class="red_star">*</span></td>
        </tr>
    <tr>
      <td width="20%"  nowrap="nowrap" class="lbl_right_padding">�ſʽ</td>
      <td width="30%"  class="data_input"><%
        if ("add".equalsIgnoreCase(doType)) {
          zs = new ZtSelect("RELEASECONDCD", "RELEASECONDCD", loan.getReleasecondcd());
          //zs.addAttr("disabled","disabled");
        }else{
          zs = new ZtSelect("RELEASECONDCD", "RELEASECONDCD");
          //zs.addAttr("disabled","disabled");
        }
        zs.addAttr("style", "width: 40%");
        zs.addAttr("fieldType", "text"); 
        zs.addAttr("isNull","false"); 
        zs.addOption("","");
        out.print(zs); 
      %><span class="red_star">*</span></td>
        </tr>
      <%--
      <tr>
        <td width="20%"  class="lbl_right_padding">��������</td>
      <td width="30%"  class="data_input"><%
        zs = new ZtSelect("KEEPCONT", "KEEPCONT", "01"); 
        zs.addAttr("style", "width: 40%"); 
        zs.addAttr("fieldType", "text"); 
        //zs.addAttr("isNull","false");
        zs.addOption("","");
        out.print(zs); 
      %></td>
    </tr>--%>

      <tr>
      <td width="20%"  class="lbl_right_padding" >��Ѻ��������      </td>
      <td width="30%"   class="data_input"><input  name="MORTDATE"  type="text" id="MORTDATE"  style="width:40%" onClick="WdatePicker()" fieldType="date" isNull="false" ><span class="red_star">*</span></td>
    </tr>

</table>
    </fieldset>

<br>
<fieldset >
  <legend>������Ϣ</legend>     

      <table width="100%"  cellspacing="0" border="0">

    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ա</td> 
      <td width="30%" class="data_input"><input type="text"  value="<%=omgr.getOperatorName()%>" style="width:90%" disabled="disabled" ></td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ʱ��</td> 
      <td width="30%" class="data_input"><input type="text" value="<%=BusinessDate.getToday() %>" style="width:90%" disabled="disabled" ></td>
    </tr>
  </table> 
  </fieldset>
    <br>
  <fieldset > 
  <legend>����</legend> 
  <table width="100%" class="title1"  cellspacing="0"> 
    <tr > 
      <td align="center" >
        <!--��ѯ--> 
        <%if (doType.equals("select")) {      %> 
        <input id="closebut" class="buttonGrooveDisable" onmouseover="button_onmouseover()" onmouseout="button_onmouseout()" type="button" value="�ر�" onclick="window.close();"> 
        <%} else if (doType.equals("edit") || doType.equals("add")) {      %> 
        <!--���ӣ��޸�--> 
        <input id="savebut" class="buttonGrooveDisable" onmouseover="button_onmouseover()" onmouseout="button_onmouseout()" type="button" value="����" onclick="saveClick();"> 
        <input id="closebut" class="buttonGrooveDisable" onmouseover="button_onmouseover()" onmouseout="button_onmouseout()" type="button" value="ȡ��" onclick="window.close();"> 
        <%} else if (doType.equals("delete")) {      %> 
        <!--ɾ��--> 
        <input id="deletebut" class="buttonGrooveDisable" onmouseover="button_onmouseover()" onmouseout="button_onmouseout()" type="button" value="ɾ��" onclick="deleteClick();"> 
        <input id="closebut" class="buttonGrooveDisable" onmouseover="button_onmouseover()" onmouseout="button_onmouseout()" type="button" value="ȡ��" onclick="window.close();"> 
        <%}      %>         
      </td> 
    </tr> 
  </table> 
  </fieldset> 
 </form> 
 </div>
</body>
</html>
