<!--
/*********************************************************************
* ��������: ������Ŀ������ϸҳ��
*
* �� ��:
* ��������: 2010/01/21
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
    String proj_nbxh = "";
    // �����������
//    String proj_no = "";
    // ��������
    String doType = "";

    if (request.getParameter("proj_nbxh") != null) {
        proj_nbxh = request.getParameter("proj_nbxh");
    }
//
//    if (request.getParameter("proj_no") != null)
//        proj_no = request.getParameter("proj_no");

    if (request.getParameter("doType") != null)
        doType = request.getParameter("doType");

    //�쳣����
    LNCOOPPROJ bean = new LNCOOPPROJ();

    // ��ʼ��ҳ��
    if (proj_nbxh != null) {
        bean = LNCOOPPROJ.findFirst("where proj_nbxh='" + proj_nbxh + "'");
        if (bean != null) {
            StringUtils.getLoadForm(bean, out);
        }
    }

    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

%>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>������Ŀ��Ϣ�Ǽ�</title>
    <script language="javascript" src="/UI/support/tabpane.js"></script>
    <script language="javascript" src="/UI/support/common.js"></script>
    <script language="javascript" src="/UI/support/DataWindow.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="coopprojEdit.js"></script>
</head>
<body bgcolor="#ffffff" onLoad="formInit();" class="Bodydefault">
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
        <input type="hidden" id="proj_nbxh" value="<%=proj_nbxh%>"/>
        <!-- ϵͳ��־ʹ�� -->
        <input type="hidden" id="busiNode"/>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ŀ���</td>
            <td width="80%" colspan="3" class="data_input"><input type="text" id="proj_no" name="proj_no"
                                                                  value=""
                                                                  textLength="20" style="width:33.5%"
                                                                  isNull="false">
                <span class="red_star">*</span></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ŀ����</td>
            <td width="30%" colspan="3" class="data_input"><input type="text" id="proj_name" name="proj_name"
                                                                  value=""
                                                                  textLength="200" style="width:96.4% "
                                                                  isNull="false">
                <span class="red_star">*</span></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ŀ���</td>
            <td width="30%" colspan="3" class="data_input"><input type="text" id="proj_name_abbr" name="proj_name_abbr"
                                                                  value=""
                                                                  textLength="50" style="width:33.5% "
                                                                  isNull="false">
                <span class="red_star">*</span></td>
        </tr>
        <%--

                <tr>
                    <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ŀ״̬</td>
                    <td width="80%" colspan="3" class="data_input"><input type="text" id="projstatus" name="projstatus"
                                                                          value=""
                                                                          textLength="100" style="width:20% "
                            ></td>
                </tr>

        --%>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">���������</td>
            <td width="30%" colspan="3" class="data_input"><input type="text" id="corpid" name="corpid"
                                                                  value="" textLength="20" style="width:33.5%"
                                                                  isNull="false">
                <span class="red_star">*</span></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����������</td>
            <td width="30%" colspan="3" class="data_input"><input type="text" id="corpname" name="corpname"
                                                                  value="" textLength="100" style="width:63%"
                                                                  isNull="false">
                <span class="red_star">*</span></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">¥������λ��</td>
            <td width="30%" colspan="3" class="data_input"><input type="text" id="builaddr" name="builaddr"
                                                                  value="" textLength="200" style="width:63%"
                                                                  isNull="false">
                <span class="red_star">*</span></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">������</td>
            <td width="30%" class="data_input"><%
                ZtSelect zs = new ZtSelect("BANKID", "", omgr.getOperator().getDeptid());
                zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                        + " start with deptid = '" + omgr.getOperator().getDeptid() + "'"
                        + " connect by prior deptid = parentdeptid");
                zs.addAttr("style", "width: 33.5%");
                zs.addAttr("fieldType", "text");
                zs.addAttr("isNull", "false");
                zs.addOption("", "");
                out.print(zs);
            %>
                <span class="red_star">*</span></td>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">¼������</td>
            <td width="30%" class="data_input"><input type="text" id="inputdate" name="inputdate" value=""
                                                      style="width:33.5%" onClick="WdatePicker()" fieldType="date"
                                                      isNull="false">
                <span class="red_star">*</span></td>
        </tr>
        </tr>

    </table>
</fieldset>
<br>
<fieldset>
    <legend>������Ϣһ</legend>
    <table width="100%" cellspacing="0" border="0">
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����֤ծȨ��ʼ����</td>
            <td width="30%" class="data_input"><input type="text" id="assustartdate" name="assustartdate" value=""
                                                      style="width:90%" onClick="WdatePicker()" fieldType="date"
                    ></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����֤ծȨ��ֹ����</td>
            <td width="30%" class="data_input"><input type="text" id="assuenddate" name="assuenddate" value=""
                                                      style="width:90%" onClick="WdatePicker()" fieldType="date"
                    ></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">��������(��)</td>
            <td width="30%" class="data_input"><input type="text" id="coopperiod" name="coopperiod"
                                                      value="" textLength="5"
                                                      style="width:90% "></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">�������(��Ԫ)</td>
            <td width="30%" class="data_input"><input type="text" id="cooplimitamt" name="cooplimitamt"
                                                      value="" textLength="10"
                                                      style="width:90% "></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ߴ������(%)</td>
            <td width="30%" class="data_input"><input type="text" id="maxlnpercent" name="maxlnpercent"
                                                      value="" textLength="10"
                                                      style="width:90% " intLength="2" floatLength="3"></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">���������(��)</td>
            <td width="30%" class="data_input"><input type="text" id="maxlnperiod" name="maxlnperiod"
                                                      value="" textLength="5"
                                                      style="width:90% "></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤�ڼ�˵��</td>
            <td width="30%" colspan="3" class="data_input"><textarea name="assuperiod" rows="5" id="assuperiod"
                                                                     value=""
                                                                     style="width:96.4%" textLength="2000"></textarea>
            </td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����Э������</td>
            <td width="30%" class="data_input"><%
                zs = new ZtSelect("AGREEMENTCD", "COOPAGREEMENTCD", "1");
                zs.addAttr("style", "width: 90%");
                zs.addAttr("fieldType", "text");
                zs.addAttr("isNull", "false");
                zs.addOption("", "");
                out.print(zs);
            %>
                <span class="red_star">*</span></td>
            <%--</tr>--%>
            <%----%>
            <%--<tr>--%>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����Э����</td>
            <td width="30%" colspan="3" class="data_input"><input type="text" name="agreementno" id="agreementno"
                                                                  value=""
                                                                  style="width:90%" textLength="100">
            </td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">Լ���ſʽ</td>
            <td width="30%" class="data_input"><%
                zs = new ZtSelect("RELEASECONDCD", "RELEASECONDCD", "01");
                zs.addAttr("style", "width: 90%");
                zs.addAttr("fieldType", "text");
                zs.addAttr("isNull", "false");
                zs.addOption("", "");
                out.print(zs);
            %>
                <span class="red_star">*</span></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ѻ����ʱ��(��)</td>
            <td width="30%" class="data_input"><input type="text" id="followupmortperiod" name="followupmortperiod"
                                                      value="" textLength="10"
                                                      style="width:90%" intLength="10" floatLength="0"
                                                      isNull="false">
                <span class="red_star">*</span></td>
        </tr>
    </table>
</fieldset>
<br>
<fieldset>
    <legend>������Ϣ��</legend>
    <table width="100%" cellspacing="0" border="0">
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ŀ��������</td>
            <td width="30%" class="data_input"><%
                zs = new ZtSelect("BANKFLAG", "BANKFLAG", "1");
                zs.addAttr("style", "width: 90%");
                zs.addAttr("fieldType", "text");
                zs.addAttr("isNull", "false");
                zs.addOption("", "");
                out.print(zs);
            %>
                <span class="red_star">*</span></td>
            <%--</tr>--%>
            <%--<tr>--%>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">�������������</td>
            <td width="30%" colspan="3" class="data_input"><input type="text" id="devlnbankcd" name="devlnbankcd"
                                                                  value="" textLength="50"
                                                                  style="width:90% "></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����������ʼ����</td>
            <td width="30%" class="data_input"><input type="text" id="devlnstartdate" name="devlnstartdate" value=""
                                                      style="width:90%" onClick="WdatePicker()" fieldType="date"
                    ></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">���������ֹ����</td>
            <td width="30%" class="data_input"><input type="text" id="devlnenddate" name="devlnenddate" value=""
                                                      style="width:90%" onClick="WdatePicker()" fieldType="date"
                    ></td>
        </tr>
        <tr>
            <%--
                        <td width="20%" nowrap="nowrap" class="lbl_right_padding">��𻯷���֤�����(%)</td>
                        <td width="30%" class="data_input"><input type="text" id="assuamtpercent" name="assuamtpercent"
                                                                  value="" textLength="10"
                                                                  style="width:90% "></td>
            --%>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤�����(%)</td>
            <td width="30%" class="data_input"><input type="text" id="assuamtpercent" name="assuamtpercent"
                                                      value="" textLength="16"
                                                      style="width:90% " intLength="13" floatLength="2"></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤���˺�</td>
            <td width="30%" class="data_input"><input type="text" id="assuamt" name="assuamt"
                                                      value="" textLength="30"
                                                      style="width:90% "></td>
        </tr>
        <%--
                <tr>
                    <td width="20%" nowrap="nowrap" class="lbl_right_padding">�Ƿ��ṩ��ŵ��</td>
                    <td width="30%" class="data_input"><input type="text" id="commitmentflag"
                                                              value=""  textLength="2"
                                                              style="width:90%" ></td>
                </tr>
        --%>
        <tr>
            <%--

                  <td width="20%" nowrap="nowrap" class="lbl_right_padding">�Ƿ���</td>
                  <td width="30%" class="data_input"><%
                                zs = new ZtSelect("MATURITYFLAG", "COOPMATURITYFLAG", "0");
                                zs.addAttr("style", "width: 90%");
                                zs.addAttr("fieldType", "text");
                                zs.addAttr("isNull", "false");
                                zs.addOption("", "");
                                out.print(zs);
                            %>
                    <span class="red_star">*</span> </td>

            --%>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">Ԥ�����֤����</td>
            <td width="30%"  class="data_input"><input type="text" id="presellid" name="presellid"
                                                                value="" textLength="100"
                                                                style="width:90% "></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding"></td>
            <td width="30%"  class="data_input"></td>

        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����˻��˺�</td>
            <td width="30%" class="data_input"><input type="text" id="adminacct" name="adminacct"
                                                      value="" textLength="50"
                                                      style="width:90% "></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����˻���������</td>
            <td width="30%" class="data_input"><input type="text" id="adminacctbank" name="adminacctbank"
                                                      value="" textLength="50"
                                                      style="width:90% "></td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">���������</td>
            <td width="30%" class="data_input"><%
                zs = new ZtSelect("APPRBILLCD", "COOPAPPRBILLCD", "1");
                zs.addAttr("style", "width: 90%");
                zs.addAttr("fieldType", "text");
//                    zs.addAttr("isNull", "false");
                zs.addOption("", "");
                out.print(zs);
            %>
            </td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">���������</td>
            <td width="30%" class="data_input"><input type="text" id="apprbillno" name="apprbillno"
                                                      value="" textLength="50"
                                                      style="width:90% "></td>
        </tr>
        <%--

                <tr>
                    <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ŀ�����������</td>
                    <td width="30%" colspan="3" class="data_input"><textarea name="projdevlninfo" rows="3" id="projdevlninfo"
                                                                             value=""
                                                                             style="width:96%" textLength="500"></textarea></td>
                </tr>
        --%>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">��������Լ��</td>
            <td width="30%" colspan="3" class="data_input"><textarea name="otherpromise" rows="5" id="otherpromise"
                                                                     value=""
                                                                     style="width:96.4%" textLength="2000"></textarea>
            </td>
        </tr>
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ע</td>
            <td width="30%" colspan="3" class="data_input"><textarea name="remarks" rows="5" id="remarks"
                                                                     value=""
                                                                     style="width:96.4%" textLength="2000"></textarea>
            </td>
        </tr>
    </table>
</fieldset>
<br>
<fieldset>
    <legend>������Ϣ</legend>
    <table width="100%" class="title1" cellspacing="0">
        <tr>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ա</td>
            <td width="30%" class="data_input"><input type="text" value="<%=omgr.getOperatorName()%>"
                                                      style="width:90%" readonly="readonly"></td>
            <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ʱ��</td>
            <td width="30%" class="data_input"><input type="text" value="<%=BusinessDate.getToday() %>"
                                                      style="width:90%" readonly="readonly"></td>
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
