<!--
/*********************************************************************
* ��������: Ȩ֤���ͳ��
* �� ��: leonwoo
* ��������: 2010/01/16
* �� �� ��:
* �޸�����:
* �� Ȩ: ��˾
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
%>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>
    <script language="javascript" src="paperStatList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/common.js"></script>
    <script language="javascript" src="/UI/support/DataWindow.js"></script>
</head>
<body onload="formInit()" bgcolor="#ffffff" class="Bodydefault">

<fieldset style="padding:40px 25px 0px 25px;margin:0px 20px 0px 20px">
    <%--<div class="title">����������ڡ��������ȡ֤���ڡ���֤�������ڡ���֤�黹���ڼ���Ȩ֤�����</div>--%>
    <legend>��ѯ����</legend>
        <br>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <!-- ��ϲ�ѯͳ������һ -->
            <input type="hidden" value="miscRpt03" id="rptType" name="rptType"/>
            <tr>
                <td width="30%" nowrap="nowrap" class="lbl_right_padding">��ֹ����</td>
                <td width="70%" nowrap="nowrap" class="data_input"><input type="text" id="MORTEXPIREDATE"
                                                                          name="MORTEXPIREDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:35%"
                                                                          isNull="false"><span class="red_star">*</span>
                </td>

            </tr>
            <tr>
                <td width="30%" nowrap="nowrap" class="lbl_right_padding">����</td>
                <td width="70%" nowrap="nowrap" class="data_input">
                    <%
                        ZtSelect zs = new ZtSelect("bankid", "", omgr.getOperator().getDeptid());
                        zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                                + " start with deptid = '" + omgr.getOperator().getDeptid() + "'"
                                + " connect by prior deptid = parentdeptid");
                        zs.addAttr("style", "width: 35%");
                        zs.addAttr("fieldType", "text");
                        out.print(zs);
                    %>
                </td>
            </tr>
            <tr>
                <td colspan="2" nowrap="nowrap" align="center" style="padding:20px">
                    <input name="expExcel" class="buttonGrooveDisableExcel" type="button"
                           onClick="loanTab_expExcel_click()" value="����excel">
                    <input type="reset" value="����" class="buttonGrooveDisable">
                </td>
            </tr>
        </form>
    </table>
</fieldset>
</body>
</html>
