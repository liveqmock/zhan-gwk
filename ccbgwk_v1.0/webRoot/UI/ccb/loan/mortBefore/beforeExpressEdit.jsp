<!--
/*********************************************************************
* ��������: ��Ѻ��Ϣ������Ѻ��ϸҳ��;��ݵǼ�
*
* �� ��: leonwoo
* ��������: 2010/01/16
* �� �� ��:
* �޸�����:
* �� Ȩ: ��˾
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%@ page import="com.ccb.dao.*" %>
<%@ page import="com.ccb.util.*" %>
<%
    // �����������
    String loanID = "";
    // ��������
    String doType = "";
    // ��Ѻ���
    String mortID = "";

    if (request.getParameter("loanID") != null)
        loanID = request.getParameter("loanID");

    if (request.getParameter("doType") != null)
        doType = request.getParameter("doType");
    if ("add".equalsIgnoreCase(doType)) {
        // �Զ�ȡ����Ѻ���
        //mortID = SeqUtil.getSeq(CcbLoanConst.MORTTYPE);
    } else {
        if (request.getParameter("mortID") != null)
            mortID = request.getParameter("mortID");
        // ��ʼ��ҳ��
        LNMORTINFO bean = LNMORTINFO.findFirst("where mortid='" + mortID + "'");
        if (bean != null) {
            StringUtils.getLoadForm(bean, out);
        }
    }

    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

%>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����Ϣ�Ǽ�</title>
<script language="javascript" src="/UI/support/tabpane.js"></script>
<script language="javascript" src="/UI/support/common.js"></script>
<script language="javascript" src="/UI/support/DataWindow.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>
<script language="javascript" src="beforeExpressEdit.js"></script>
</head>
<body bgcolor="#ffffff" onLoad="formInit();" class="Bodydefault">
<div id="container">
  <form id="editForm" name="editForm">
    <fieldset>
    <legend>�����Ϣ�Ǽ�</legend>
    <table width="100%" cellspacing="0" border="0">
      <!-- �������� -->
      <input type="hidden" id="doType" value="<%=doType%>">
      <!-- �汾�� -->
      <input type="hidden" id="recVersion" value="">
      <!-- ҵ��ڵ� -->
      <input type="hidden" id="busiNode" value="">
      <!-- ��Ѻ�Ǽ�״̬ -->
      <input type="hidden" id="MORTSTATUS" name="MORTSTATUS" value="">
      <tr>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">�����������</td>
        <td width="30%" class="data_input"><input type="text" id="loanID" name="loanID" value="<%=loanID%>"
                                                              style="width:90% " disabled="disabled"></td>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ���</td>
        <td width="30%" class="data_input"><input type="text" id="mortID" name="mortID" value="<%=mortID%>"
                                                              style="width:90%" disabled="disabled"></td>
      </tr>
      <tr>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">���</td>
        <td width="30%" class="data_input"><input name="BOXID" type="text" id="BOXID" style="width:90%"
                                                              textLength="10"></td>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ݷ�������</td>
        <td width="30%" class="data_input"><input name="EXPRESSENDSDATE" type="text" id="EXPRESSENDSDATE"
                                                              style="width:90%" onClick="WdatePicker()" fieldType="date"
                                                              isNull="false">
          <span class="red_star">*</span></span></td>
      </tr>
      <tr>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ݱ�ע</td>
        <td width="30%" colspan="3" class="data_input"><textarea name="EXPRESSNOTE" rows="3"
                                                                             id="EXPRESSNOTE" style="width:96%"
                                                                             textLength="500"></textarea></td>
      </tr>
      <tr>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ա</td>
        <td width="30%" class="data_input"><input type="text" value="<%=omgr.getOperatorName()%>"
                                                              style="width:90%" disabled="disabled"></td>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ʱ��</td>
        <td width="30%" class="data_input"><input type="text" value="<%=BusinessDate.getToday() %>"
                                                              style="width:90%" disabled="disabled"></td>
      </tr>
    </table>
    </fieldset>
    <fieldset>
    <legend>����</legend>
    <table width="100%" class="title1" cellspacing="0">
      <tr>
        <td align="center"><!--��ѯ-->
          <%if (doType.equals("select")) { %>
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="�ر�" onClick="window.close();">
          <%} else if (doType.equals("edit") || doType.equals("add")) { %>
          <!--���ӣ��޸�-->
          <input id="savebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="����" onClick="saveClick();">
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="ȡ��" onClick="window.close();">
          <%} else if (doType.equals("delete")) { %>
          <!--ɾ��-->
          <input id="deletebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="ɾ��" onClick="deleteClick();">
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="ȡ��" onClick="window.close();">
          <%} %>
        </td>
      </tr>
    </table>
    </fieldset>
  </form>
</div>
</body>
</html>