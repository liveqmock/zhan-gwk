<!--
/*********************************************************************
* ��������: ��Ѻ��Ϣ�����Ѻ��ϸҳ��
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
    // ��Ѻ����
    LNMORTINFO bean = null;
    // �û�����
    PTOPER oper = null;

    if (request.getParameter("loanID") != null)
        loanID = request.getParameter("loanID");

    if (request.getParameter("doType") != null)
        doType = request.getParameter("doType");
    if ("add".equalsIgnoreCase(doType)) {
        // �Զ�ȡ����Ѻ���
        // //mortID = SeqUtil.getSeq(CcbLoanConst.MORTTYPE);
    } else {
        if (request.getParameter("mortID") != null)
            mortID = request.getParameter("mortID");
        // ��ʼ��ҳ��
        bean = LNMORTINFO.findFirst("where mortid='" + mortID + "'");
        if (bean != null) {
            StringUtils.getLoadForm(bean, out);
        }
    }

    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    //ȡ���û�������
    if (bean != null) {
        oper = PTOPER.findFirst("where operid='" + bean.getOperid() + "'");
    }
    if (oper == null) {
        oper = new PTOPER();
    }
%>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>��Ѻ��Ϣ�Ǽ�</title>
    <script language="javascript" src="/UI/support/tabpane.js"></script>
    <script language="javascript" src="/UI/support/common.js"></script>
    <script language="javascript" src="/UI/support/DataWindow.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="mortgageEdit.js"></script>
</head>
<body bgcolor="#ffffff" onLoad="formInit();" class="Bodydefault">
<br>

<form id="editForm" name="editForm">
<fieldset>
<legend>��Ѻ��Ϣ�Ǽ�</legend>
<table width="100%" cellspacing="0" border="0">
<!-- �������� -->
<input type="hidden" id="doType" name="doType" value="<%=doType%>">
<!-- �汾�� -->
<input type="hidden" id="recVersion" name="recVersion" value="">
<!-- ��ˮ��־ʹ�� -->
<input type="hidden" id="busiNode" name="busiNode" value=""/>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">�����������</td>
    <td width="30%" class="data_input"><input type="text" id="loanID" name="loanID" value="<%=loanID%>"
                                              style="width:90% " disabled="disabled"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ���</td>
    <td width="30%" class="data_input"><input type="text" id="mortID" name="mortID" value="<%=mortID%>"
                                              style="width:90%" disabled="disabled"></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ��������</td>
    <td width="30%" class="data_input"><%
        ZtSelect zs = new ZtSelect("MORTECENTERCD", "MORTECENTERCD", "1");
        zs.addAttr("style", "width: 90%");
        zs.addAttr("fieldType", "text");
        zs.addAttr("isNull", "false");
        //zs.addOption("", "");
        //zs.setDisplayAll(false);
        out.print(zs);
    %>
        <span class="red_star">*</span></td>
    <td width="20%" class="lbl_right_padding">��Ѻ��������</td>
    <td width="30%" class="data_input"><input name="MORTDATE" type="text" id="MORTDATE" style="width:90%"
                                              onClick="WdatePicker()" fieldType="date" isNull="false">
        <span
                class="red_star">*</span></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">�ſʽ</td>
    <td width="30%" class="data_input"><%
        zs = new ZtSelect("RELEASECONDCD", "RELEASECONDCD", "1");
        zs.addAttr("style", "width: 90%");
        zs.addAttr("fieldType", "text");
        //zs.addAttr("isNull","false");
        zs.addOption("", "");
        out.print(zs);
    %></td>
    <td width="20%" class="lbl_right_padding">��������</td>
    <td width="30%" class="data_input"><%
        zs = new ZtSelect("KEEPCONT", "KEEPCONT", "1");
        zs.addAttr("style", "width: 90%");
        zs.addAttr("fieldType", "text");
        //zs.addAttr("isNull","false");
        zs.addOption("", "");
        out.print(zs);
    %></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ҫ�������</td>
    <td width="30%" class="data_input"><input name="documentid" type="text" id="documentid" style="width:90%"
                                              textLength="30"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding"></td>
    <td width="30%" class="data_input"></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ݱ��</td>
    <td width="30%" class="data_input"><input name="EXPRESSNO" type="text" id="EXPRESSNO" style="width:90%"
                                              textLength="30"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ݷ�������</td>
    <td width="30%" class="data_input"><input name="EXPRESSENDSDATE" type="text" id="EXPRESSENDSDATE"
                                              style="width:90%" onClick="WdatePicker()" fieldType="date"></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ݱ�ע</td>
    <td width="30%" colspan="3" class="data_input"><textarea name="EXPRESSNOTE" rows="2" id="EXPRESSNOTE"
                                                             style="width:96.4%" textLength="500"></textarea>
    </td>
</tr>
<%--
<tr>
  <td width="20%" nowrap="nowrap" class="lbl_right_padding">�������</td>
  <td width="30%" class="data_input"><input  name="PAPERRTNDATE"  type="text" id="PAPERRTNDATE"  style="width:90%"  onClick="WdatePicker()" fieldType="date" /></td>
  </td>
</tr>
--%>
<tr>
    </td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ݻ�֤����</td>
    <td width="30%" class="data_input"><input id="EXPRESSRTNDATE" name="EXPRESSRTNDATE" style="width:90%"/></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">δ�����Ѻԭ��</td>
    <td width="30%" class="data_input"><%
        zs = new ZtSelect("NOMORTREASONCD", "NOMORTREASONCD", "1");
        zs.addAttr("style", "width: 90%");
        zs.addAttr("fieldType", "text");
        //zs.addAttr("isNull","false");
        zs.addOption("", "");
        out.print(zs);
    %>
    </td>
</tr>
<tr>
    <td width="20%" class="lbl_right_padding">δ�����Ѻԭ��ע</td>
    <td colspan="3" class="data_input"><textarea name="NOMORTREASON" rows="3" id="NOMORTREASON"
                                                 style="width:96.4%" textLength="500"></textarea></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ִ���</td>
    <td width="30%" class="data_input"><input name="EXPRESSNO" type="text" id="RECEIPTID" style="width:90%"
                                              textLength="30"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">���п��������ɱ�/�ɱ�</td>
    <td width="30%" class="data_input"><%
        zs = new ZtSelect("SENDFLAG", "SENDFLAG", "1");
        zs.addAttr("style", "width: 90%");
        zs.addAttr("fieldType", "text");
        //zs.addAttr("isNull","false");
        zs.addOption("", "");
        out.print(zs);
    %>
    </td>
</tr>

<tr>
    <td width="20%" class="lbl_right_padding">���в��ɱ���Ѻ�Ǽ�����</td>
    <td width="30%" class="data_input"><input name="RPTNOMORTDATE" type="text" id="RPTNOMORTDATE" style="width:90%"
                                              onClick="WdatePicker()" fieldType="date"></td>
    <td width="20%" class="lbl_right_padding">���пɱ���Ѻ�Ǽ�����</td>
    <td width="30%" class="data_input"><input name="RPTMORTDATE" type="text" id="RPTMORTDATE" style="width:90%"
                                              onClick="WdatePicker()" fieldType="date"></td>
    <%--

                  <td width="20%" nowrap="nowrap" class="lbl_right_padding">���ɱ���Ѻ���Ͻ��ӱ�־</td>
                  <td width="30%" class="data_input">
                     <%
                    zs = new ZtSelect("RELAYFLAG", "RELAYFLAG", "1");
                    zs.addAttr("style", "width: 90%");
                    zs.addAttr("fieldType", "text");
                    //zs.addAttr("isNull","false");
                    zs.addOption("","");
                    out.print(zs);
                    %>
                  </td>
            --%>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤��������</td>
    <td width="30%" class="data_input"><input name="CHGPAPERDATE" type="text" id="CHGPAPERDATE"
                                              style="width:90%" onClick="WdatePicker()" fieldType="date"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤ԭ��</td>
    <td width="30%" class="data_input"><%
        zs = new ZtSelect("CHGPAPERREASONCD", "CHGPAPERREASONCD", "1");
        zs.addAttr("style", "width: 90%");
        zs.addAttr("fieldType", "text");
        //zs.addAttr("isNull","false");
        zs.addOption("", "");
        out.print(zs);
    %>
    </td>
</tr>
<tr>
    <td width="20%" class="lbl_right_padding">��֤ԭ��ע</td>
    <td colspan="3" class="data_input"><textarea name="CHGPAPERREASON" rows="3" id="CHGPAPERREASON"
                                                 style="width:96.4%" textLength="300"></textarea></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤�黹����</td>
    <td width="30%" class="data_input"><input name="CHGPAPERRTNDATE" type="text" id="CHGPAPERRTNDATE"
                                              style="width:90%" onClick="WdatePicker()" fieldType="date"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ȡ֤����</td>
    <td width="30%" class="data_input"><input name="CLRPAPERDATE" type="text" id="CLRPAPERDATE"
                                              style="width:90%" onClick="WdatePicker()" fieldType="date"></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ��������</td>
    <td width="30%" class="data_input"><input name="MORTEXPIREDATE" type="text" id="MORTEXPIREDATE"
                                              style="width:90%" onClick="WdatePicker()" fieldType="date"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ����������</td>
    <td width="30%" class="data_input"><input name="MORTOVERRTNDATE" type="text" id="MORTOVERRTNDATE"
                                              style="width:90%" onClick="WdatePicker()" fieldType="date"></td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">ȡ�ػ�ִ����</td>
    <td width="30%" class="data_input"><input name="RECEIPTDATE" type="text" id="RECEIPTDATE" style="width:90%"
                                              onClick="WdatePicker()" fieldType="date"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">�������</td>
    <td width="30%" class="data_input"><input name="PAPERRTNDATE" type="text" id="PAPERRTNDATE"
                                              style="width:90%" onClick="WdatePicker()" fieldType="date"/></td>
    </td>
</tr>
<tr>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ�Ǽ�״̬</td>
    <td width="30%" class="data_input"><%
        zs = new ZtSelect("MORTREGSTATUS", "MORTREGSTATUS", "1");
        zs.addAttr("style", "width: 90%");
        zs.addAttr("fieldType", "text");
        //zs.addAttr("isNull","false");
        zs.addOption("", "");
        out.print(zs);
    %>
    </td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ����״̬</td>
    <td width="30%" class="data_input"><%
        zs = new ZtSelect("MORTSTATUS", "MORTSTATUS", "");
        zs.addAttr("style", "width: 90%");
        zs.addAttr("fieldType", "text");
        //zs.addAttr("isNull","false");
        zs.addOption("", "");
        out.print(zs);
    %>
    </td>
</tr>
<tr>
    <%if (doType.equals("select")) { %>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ա</td>
    <td width="30%" class="data_input"><input type="text" value="<%=oper.getOpername()%>" style="width:90%"
                                              disabled="disabled"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ʱ��</td>
    <td width="30%" class="data_input"><input type="text" id="OPERDATE" value="" style="width:90%"
                                              disabled="disabled"></td>
    <%} else { %>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ա</td>
    <td width="30%" class="data_input"><input type="text" value="<%=omgr.getOperatorName()%>" style="width:90%"
                                              disabled="disabled"></td>
    <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ʱ��</td>
    <td width="30%" class="data_input"><input type="text" value="<%=BusinessDate.getToday() %>"
                                              style="width:90%" disabled="disabled"></td>
    <%} %>
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
</body>
</html>
