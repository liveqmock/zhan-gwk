<!--
/*********************************************************************
* ��������: ������Ϣ��ѯ
* ����:
* ��������: 2010/06/10
* �޸���:
* �޸�����:
* ��Ȩ:
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%@page import="pub.platform.security.OperatorManager" %>
<%@page import="pub.platform.form.config.SystemAttributeNames" %>
<%@page import="pub.platform.db.DBGrid" %>
<%@page import="pub.platform.html.ZtSelect" %>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>
    <%--<script language="javascript" src="coopprojList.js"></script>--%>
    <script language="javascript" src="paybackInfoList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>

</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("ActionTable");
    dbGrid.setGridType("edit");


    String sql = "select voucherid,account,cardname,amt,querydate,operid,operdate,status,paybackdate,remark from ls_paybackinfo paybackinfo" +
            " where 1=1 ";

    dbGrid.setfieldSQL(sql);
    dbGrid.setWhereStr(" order by voucherid ");

    dbGrid.setField("֧��ƾ֤���", "text", "8", "voucherid", "true", "0");
    dbGrid.setField("���񿨿���", "text", "8", "account", "true", "0");
    dbGrid.setField("�����ƿ���", "text", "8", "cardname", "true", "0");
    dbGrid.setField("������", "text", "6", "amt", "true", "0");
    dbGrid.setField("��ѯ����", "text", "6", "querydate", "true", "0");
    dbGrid.setField("������Ա", "text", "5", "operid", "true", "0");
    dbGrid.setField("����ʱ��", "text", "6", "operdate", "true", "0");
    dbGrid.setField("����״̬", "dropdown", "5", "status", "true", "PAYBACKSTATUS");
    dbGrid.setField("�ɹ���������", "text", "8", "paybackdate", "true", "0");
     dbGrid.setField("��ע", "text", "8", "remark", "true", "0");
    dbGrid.setpagesize(50);
    dbGrid.setdataPilotID("datapilot");

    dbGrid.setbuttons("����Excel=excel,moveFirst,prevPage,nextPage,moveLast");

%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset>
    <legend> ��ѯ����</legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <!-- ���  �������ֶ���Ϊ��ɾ��֮�� -->
            <input type="hidden" id="lsh" value="">
            <!-- ϵͳ��־��ʹ�� -->
            <input type="hidden" id="busiNode"/>
            <tr>
                <td width="15%" class="lbl_right_padding"> ֧��ƾ֤���</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="voucherid" size="30" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> ���񿨿���</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="account" size="60" style="width:91% ">
                </td>
                 <td width="10%" align="right" nowrap="nowrap"><input name="cbRetrieve" type="button"
                                                                     class="buttonGrooveDisable" id="button"
                                                                     onClick="queryClick()"
                                                                     onMouseOver="button_onmouseover()"
                                                                     onMouseOut="button_onmouseout()" value="�� ��">
                </td>
            </tr>
            <tr>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> �����ƿ���</td>
                <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3"><input type="text" id="cardname"
                                                                                        size="60" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> ����״̬</td>
                <td width="30%" class="data_input" colspan="3"><%
                    ZtSelect zs = new ZtSelect("status", "paybackstatus", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>
                </td>

                <td width="10%" align="right" nowrap="nowrap"><input name="Input"
                                                                     class="buttonGrooveDisable"
                                                                     type="reset"
                                                                     value="�� ��"
                                                                     onMouseOver="button_onmouseover()"
                                                                     onMouseOut="button_onmouseout()">
                </td>
            </tr>
        </form>
    </table>

</fieldset>
<fieldset>
    <legend>������Ϣ��ϸ</legend>
    <table width="100%">
        <tr>
            <td><%=dbGrid.getDBGrid()%>
            </td>
        </tr>
    </table>
</fieldset>
<FIELDSET>
    <LEGEND> ����</LEGEND>
    <table width="100%" class="title1">
        <tr>
            <td align="right"><%=dbGrid.getDataPilot()%>
            </td>
        </tr>
    </table>
</FIELDSET>

</body>
</html>
