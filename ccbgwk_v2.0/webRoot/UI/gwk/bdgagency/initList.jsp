<!--
/*********************************************************************
* ��������: Ԥ�㵥λ������Ϣ��ʼ��
* ��������: 2012/4/26
* �� �� ��: zhanrui
* �޸�����:
* �� Ȩ: ��˾
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%@page import="pub.platform.advance.utils.PropertyManager" %>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>

    <script language="javascript" src="initList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <%
        OperatorManager om = (OperatorManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if ( om == null ) {
        om = new OperatorManager();
        session.setAttribute(SystemAttributeNames.USER_INFO_NAME,om);
        }
        //��ȡ��¼�û��������ţ�ֻ�з��е��û���Ȩ�޲鿴����֧�е����� 2012-11-26
        String areacode = om.getOperator().getDeptid();
        //��ȡ��ע��������ݣ������admin���в鿴����֧�����ݵ�Ȩ�� 2012-11-26
        String strRemark = om.getOperator().getFillstr150();
        //��ȡ�ж����� 2012-11-26
        String strJudeFlag = PropertyManager.getProperty("pub.plat.admin.jude.flag");
    %>
</head>
<body bgcolor="#ffffff" class="Bodydefault">
<div class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px">
        <legend style="margin-left:20px">Ԥ�㵥λ������Ϣ��ʼ��</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="0" width="100%" style="padding-top:20px;padding-bottom:10px">
            <form id="queryForm" name="queryForm">
                <input type="hidden" id="operType" name="operType" value="initData"/>
                <input type="hidden" id="newLsh" name="newLsh" value=""/>
                <tr align="center">
                    <td align="center" width="35%">
                        <table>
                            <%if (strJudeFlag.equals(strRemark)){%>
                                <tr height="20">
                                    <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">��������</td>
                                    <td width="30%" class="data_input" colspan="3"><%
                                    ZtSelect zs = new ZtSelect("areacode", "AREACODE", "");
                                    zs.addAttr("style", "width: 91%");
                                    zs.addAttr("fieldType", "text");
                                    zs.addOption("", "");
                                    out.print(zs);
                                %>
                                </tr>
                            <%}else {%>
                                <input type="hidden" style="width:90%;" id="areacode" name="areacode" size="40"
                                   value="<%=areacode%>"
                                   class="ajax-suggestion url-getLoanPull.jsp">
                            <%}%>
                        </table>
                    </td>
                </tr>
                <tr height="20">
                    <td align="center" nowrap="nowrap">
                        <input type="button" id="button" onClick="initData()" value="   ͬ  ��   ">
                    </td>
                </tr>
            </form>
        </table>
    </fieldset>
</div>
</body>
</html>
