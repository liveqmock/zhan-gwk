<!--
/*********************************************************************
* ��������: ������Ϣ������ϸҳ��
* �� ��:
* ��������: 2010/06/01
* �� �� ��:
* �޸�����:
* �� Ȩ:
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%@page import="pub.platform.security.OperatorManager" %>
<%@page import="pub.platform.form.config.SystemAttributeNames" %>
<%@page import="pub.platform.utils.*" %>
<%@page import="com.ccb.dao.*" %>
<%@page import="com.ccb.util.*" %>
<%
    // �ڲ����
    String lsh = "";
    // �����������
//    String proj_no = "";
    // ��������
    String doType = "";
    // �û�����
    PTOPER oper = null;

    if (request.getParameter("lsh") != null) {
        lsh = request.getParameter("lsh");
    }

    if (request.getParameter("doType") != null)
        doType = request.getParameter("doType");

    //�쳣����
    LSCONSUMEINFO bean = new LSCONSUMEINFO();

    
    // ��ʼ��ҳ��
    if (lsh != null && !"".equals(lsh)) {
        bean = LSCONSUMEINFO.findFirst("where lsh='" + lsh + "'");
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
    <title>����������Ϣ</title>
    <script language="javascript" src="/UI/support/tabpane.js"></script>
    <script language="javascript" src="/UI/support/common.js"></script>
    <script language="javascript" src="/UI/support/DataWindow.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="consumeEdit.js"></script>
</head>
<body bgcolor="#ffffff" onLoad="formInit();" class="Bodydefault">
<%--<body bgcolor="#ffffff" class="Bodydefault">--%>
<form id="editForm" name="editForm">
    <!-- ������Ŀ״̬ -->
    <input type="hidden" id="MYPROJSTATUS">
    <br>
    <fieldset>
        <legend>������Ϣ</legend>
        <table width="100%" cellspacing="0" border="0">
            <!-- �������� -->
            <input type="hidden" id="doType" value="<%=doType%>">
            <!-- �汾�� -->
            <input type="hidden" id="recVersion" value="">
            <%--<input type="hidden" id="proj_nbxh" value="<%=proj_nbxh%>"/>--%>
            <!-- ϵͳ��־ʹ�� -->
            <input type="hidden" id="busiNode"/>
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ˮ��</td>
                <td width="30%" class="data_input"><input type="text" id="lsh" name="lsh"
                                                          value="" textLength="30" style="width:90%"
                                                          isNull="false"></td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">ͨѶ״̬</td>
                <td width="30%" class="data_input"><input type="text" id="status" name="status"
                                                          value="" textLength="20" style="width:90% "
                                                          isNull="false"></td>
            </tr>
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">����</td>
                <td width="30%" class="data_input"><input type="text" id="account" name="account"
                                                          value="" textLength="50" style="width:90% "
                                                          isNull="false"></td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">�ֿ���</td>
                <td width="30%" class="data_input"><input type="text" id="cardname" name="cardname"
                                                          value="" textLength="20" style="width:90%"
                                                          isNull="false"></td>
            </tr>
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">��������</td>
                <td width="30%" class="data_input"><input type="text" id="busidate" name="busidate" value=""
                                                          style="width:90%" onClick="WdatePicker()" fieldType="date">
                </td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding"></td>
                <td width="30%" class="data_input"></td>
            </tr>
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">���ѽ��</td>
                <td width="30%" class="data_input"><input type="text" id="busimoney" name="busimoney"
                                                          value="" textLength="20" style="width:90% " intLength="13"
                                                          floatLength="2"></td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ٻ�����</td>
                <td width="30%" class="data_input"><input type="text" id="limitdate" name="limitdate" value=""
                                                          style="width:90%" onClick="WdatePicker()" fieldType="date">
                </td>
            </tr>
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">���ѵص�</td>
                <td width="30%" colspan="3" class="data_input"><input type="text" id="businame" name="businame"
                                                                      value="" textLength="200" style="width:96.4%">
                </td>
            </tr>


        </table>
    </fieldset>
    <br>
    <fieldset>
        <legend>������Ϣ</legend>
        <table width="100%" cellspacing="0" border="0">
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">��������</td>
                <td width="30%" class="data_input"><input type="text" id="inac_date" name="inac_date" value=""
                                                          style="width:90%" onClick="WdatePicker()" fieldType="date">
                </td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">���˽��</td>
                <td width="30%" class="data_input"><input type="text" id="inac_amt" name="inac_amt"
                                                          value="" textLength="20" style="width:90% " intLength="13"
                                                          floatLength="2"></td>
            </tr>

            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">�����̻���</td>
                <td width="30%" class="data_input"><input type="text" id="cmbi_merch_no" name="cmbi_merch_no"
                                                          value="" textLength="15"
                                                          style="width:90% "></td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">�ο����</td>
                <td width="30%" class="data_input"><input type="text" id="ref_number" name="ref_number"
                                                          value="" textLength="23"
                                                          style="width:90% "></td>
            </tr>
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">������</td>
                <td width="30%" class="data_input"><input type="text" id="tx_cd" name="tx_cd"
                                                          value="" textLength="5" style="width:90% "></td>

                <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ע</td>
                <td width="30%" colspan="3" class="data_input"><input type="text" id="remark" name="remark"
                                                                      value="" textLength="200" style="width:90% "></td>
            </tr>
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">ODSBȡ������</td>
                <td width="30%" class="data_input"><input type="text" id="odsbdate" name="odsbdate"
                                                          value="" textLength="10" style="width:90% "></td>

                <td width="20%" nowrap="nowrap" class="lbl_right_padding">ODSBȡ��ʱ��</td>
                <td width="30%" colspan="3" class="data_input"><input type="text" id="odsbtime" name="odsbtime"
                                                                      value="" textLength="8" style="width:90% "></td>
            </tr>
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">ͨѶ��־</td>
                <td width="30%" colspan="3" class="data_input"><textarea name="txlog" rows="6" id="txlog"
                                                                         value="" style="width:96.4%"
                                                                         textLength="1000"></textarea>
                </td>
            </tr>


        </table>
    </fieldset>
    <br>
    <fieldset>
        <legend>������Ϣ</legend>
        <table width="100%" class="title1" cellspacing="0">
            <tr>
                <%if (doType.equals("select")) { %>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ա</td>
                <td width="30%" class="data_input"><input type="text" value="<%=oper.getOpername()%>" style="width:90%"
                                                          disabled="disabled"></td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ʱ��</td>
                <td width="30%" class="data_input"><input type="text" id="OPERDATE" value="" style="width:90%"
                                                          disabled="disabled">
                </td>
                <%} else { %>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ա</td>
                <td width="30%" class="data_input"><input type="text" value="<%=omgr.getOperatorName()%>"
                                                          style="width:90%"
                                                          disabled="disabled"></td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ʱ��</td>
                <td width="30%" class="data_input"><input type="text" value="<%=BusinessDate.getToday() %>"
                                                          style="width:90%"
                                                          disabled="disabled"></td>
                <%} %>
            </tr>
        </table>
    </fieldset>
    <br>
    <fieldset>
        <legend>����</legend>
        <table width="100%" class="title1" cellspacing="0">
            <tr>
                <td align="center"><!--��ѯ-->
                    <%if (doType.equals("select")) { %>
                    <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                           onmouseout="button_onmouseout()" type="button" value="&nbsp;&nbsp;&nbsp;�ر�&nbsp;&nbsp;&nbsp;"
                           onclick="window.close();">
                    <%} else if (doType.equals("edit") || doType.equals("add")) { %>
                    <!--���ӣ��޸�-->
                    <input id="savebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                           onmouseout="button_onmouseout()" type="button" value="&nbsp;&nbsp;&nbsp;����&nbsp;&nbsp;&nbsp;"
                           onclick="saveClick();">
                    <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                           onmouseout="button_onmouseout()" type="button" value="&nbsp;&nbsp;&nbsp;ȡ��&nbsp;&nbsp;&nbsp;"
                           onclick="window.close();">
                    <%} else if (doType.equals("delete")) { %>
                    <!--ɾ��-->
                    <input id="deletebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                           onmouseout="button_onmouseout()" type="button" value="&nbsp;&nbsp;&nbsp;ɾ��&nbsp;&nbsp;&nbsp;"
                           onclick="deleteClick();">
                    <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                           onmouseout="button_onmouseout()" type="button" value="&nbsp;&nbsp;&nbsp;ȡ��&nbsp;&nbsp;&nbsp;"
                           onclick="window.close();">
                    <%} %>
                </td>
            </tr>
        </table>
    </fieldset>
    <br>
</form>
</body>
</html>
