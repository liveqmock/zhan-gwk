<!--
  /*********************************************************************
  *    ��������:    1.����
  *
  *    ��    ��:    leonwoo
  *    ��������:    2005/01/28
  *    �� �� ��:
  *    �޸�����:
  *    ��    Ȩ:    leonwoo
  ***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK"%>
<!-- ƽ̨���õ�CSS���࣬Javascript-->
<%@include file="/global.jsp"%>
<%@page import="pub.platform.security.OperatorManager"%>
<%@page import="pub.platform.form.config.SystemAttributeNames"%>
<%@page import="pub.platform.utils.*"%>
<%@ page import="com.ccb.dao.*" %>
<%@ page import="com.ccb.util.*"%>


<%
  String fileId = "";
  String doType = "";


  if (request.getParameter("fileId") != null)
    fileId = request.getParameter("fileId");
      
  if (request.getParameter("doType") != null)
    doType = request.getParameter("doType");
 
    
  OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
  PTDEMO bean=PTDEMO.findFirst("where file_id='"+fileId+"'");		
   if(bean!=null){
   	StringUtils.getLoadForm(bean,out);
   }

%>
<html>
<style type="text/css">
  <!--
    .style1 {color: #FF0000}
  -->
</style>
<head>

<META http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ļ�����</title>

<script language="javascript" src="/UI/support/tabpane.js"></script>
<script language="javascript" src="/UI/support/common.js"></script>
<script language="javascript" src="/UI/support/DataWindow.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>
<script language="javascript" src="js/fileEdit.js"></script>
<script language="javascript" src="/UI/support/suggest/js/ajaxSuggestions.js"></script>
<style type="text/css" media="screen">
    @import url("/UI/support/suggest/css/ajax-suggestions.css");
  </style>
 

  
</head>
<body bgcolor="#ffffff" onload="formInit();"  class="Bodydefault"> 
<div id="container">
<form id="editForm" name="editForm" >
<fieldset > 
  <legend>�ļ���Ϣ</legend>     
  <table width="100%"  cellspacing="0" border="0"> 
  	
   	<input type="hidden" id="doType" value="<%=doType%>">
  	<input type="hidden" id="file_id" value="<%=fileId%>"> 
    <tr> 
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding">�ļ�����</td> 
      <td colspan="3" align="left" class="data_input" > <input type="text" id="file_name" name="file_name"  textLength="100" style="width:100% " isNull="false"> </td> 
    </tr> 
  
    <tr>
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding">�ſ���ʽ(��ѡ)</td>
      <td width="30%" align="left" class="data_input"><% 
        ZtSelect zs = new ZtSelect("file_type", "impawnLoanType", "1"); 
    	//zs.setSqlString("select ENUITEMVALUE as value ,ENUITEMLABEL as text  from PTENUDETAIL where enutype='QYLB'" 
		//+" and enuitemvalue not in('4','5')"); 
        zs.addAttr("style", "width: 200px"); 
 
        zs.addAttr("fieldType", "text"); 
        zs.addAttr("isNull","false"); 
	    //zs.addOption("", ""); 
        //zs.setDisplayAll(false); 
        out.print(zs); 
      %></td>
      <td width="20%" align="right" class="lbl_right_padding" >δ�����Ѻԭ��(����)
      </td>
      <td width="30%" class="data_input">
      <% 
        zs = new ZtSelect("impawnNoReasonSelect", "impawnNoReason", "1"); 
        zs.addAttr("style", "top:47px;POSITION: absolute;width:168px;CLIP: rect(0px auto auto 150px); "); 
        zs.addAttr("fieldType", "text"); 
        //zs.addAttr("isNull","false"); 
        zs.addAttr("onChange","onListChanged('impawnNoReasonSelect','impawnNoReason')");
        out.print(zs); 
      %>
      <input type="text" isNull="false" id="impawnNoReason" style="top:47px;POSITION: absolute; width:150;border-right:0" value="">
      </td>
    </tr>
    <tr>
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding">�ļ�С��</td>
      <td width="30%" align="left" class="data_input"><input type="text" name="file_namelength" id="file_namelength" size="20" intlength="2" isnull="false" value="10"></td>
      <td width="20%" align="left" class="lbl_right_padding">SuggestPullDown</td>
      <td width="30%" class="data_input">
        
      </td>
    </tr> 
    
    <tr> 
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding">�ļ�������ʾ����</td> 
      <td width="30%" align="left" valign="top" class="data_input"><input name="file_namelength" type="text" id="file_namelength" size="20px" intLength="2" isNull="false" value="10"></td> 
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding">�ļ���Ч����      </td> 
      <td width="30%" align="left" class="data_input"><input  name="file_lastdate"  type="text" id="file_lastdate"  size="20px"  onClick="WdatePicker()" fieldType="date" ></td>
    </tr> 
    <tr> 
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding">�Ƿ���ʾ���ļ�</td> 
      <td width="30%" align="left" valign="top" class="data_input">
	  <%
        zs = new ZtSelect("isvisibled", "BOOLTYPE", "1");
    	//zs.setSqlString("select ENUITEMVALUE as value ,ENUITEMLABEL as text  from PTENUDETAIL where enutype='QYLB'"
		//+" and enuitemvalue not in('4','5')");
        zs.addAttr("style", "width: 60px");
        zs.addAttr("fieldType", "text");
        zs.addAttr("isNull","false");
	    //zs.addOption("", "");
        //zs.setDisplayAll(false);
        out.print(zs);
      %></td> 
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding">����</td> 
      <td width="30%" align="left" valign="top" class="data_input"><input type="text" name="file_namelength" id="file_namelength" size="20" intlength="2" isnull="false" value="10"></td>
    </tr>
    <tr>
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding">�ļ�����</td>
      <td colspan="3" align="left" valign="top" class="data_input"><textarea name="file_info" rows="23" id="file_info" style="width:100%" textLength="3000"></textarea></td>
    </tr>
  </table> 
  </fieldset> 
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
 <div id="search-result-suggestions">
     <div id="search-results">
     </div>      
  </div>
 
</body>
</html>
