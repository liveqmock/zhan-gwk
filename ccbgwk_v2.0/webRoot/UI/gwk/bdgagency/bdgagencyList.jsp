<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-6-8
  Time: 13:37:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%@ page import="pub.platform.db.*" %>
<html>
<head><title>Ԥ�㵥λ��Ϣ��ѯ</title>
    <LINK href="/css/ccb.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/js/basic.js"></script>
    <script language="javascript" src="/js/xmlHttp.js"></script>
    <script language="javascript" src="/js/dbgrid.js"></script>
    <script language="javascript" src="/js/dropdownData.js"></script>
    <script language="javascript" src="/js/dbutil.js"></script>
    <script language="javascript" src="bdgagencyList.js"></script>
    <%
        DBGrid dbGrid = new DBGrid();
        dbGrid.setGridID("DbgagencyTable");
        dbGrid.setGridType("edit");
        dbGrid.setfieldSQL("select areacode,code,name,guid,levelno,supercode,isleaf,version,remark from ls_bdgagency where (1=1)");
        dbGrid.setField("����", "dropdown", "10", "areacode", "true", "AREACODE");
        dbGrid.setField("���", "text", "20", "code", "true", "0");
        dbGrid.setField("����", "text", "30", "name", "true", "0");
        dbGrid.setField("GUID", "text", "40", "guid", "true", "0");
        dbGrid.setField("����", "text", "10", "levelno", "true", "0");
        dbGrid.setField("�ϼ�����", "text", "10", "supercode", "true", "0");
        dbGrid.setField("�Ƿ�ĩ��", "dropdown", "10", "isleaf", "true", "BDGAGENCYISLEAF");
        dbGrid.setField("��ǰ�汾", "text", "10", "version", "true", "0");
        dbGrid.setField("��ע", "text", "20", "remark", "true", "0");


        dbGrid.setpagesize(50);
        dbGrid.setWhereStr(" order by code");
        dbGrid.setdataPilotID("datapilot");
        dbGrid.setbuttons("����Excel=excel,�鿴��ϸ=query,moveFirst,prevPage,nextPage,moveLast");
    %>
</head>
<body onload="body_load()" onresize="body_load()" class="Bodydefault">
<fieldset>
    <legend> ��ѯ����</legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <tr>
                <td width="15%" class="lbl_right_padding"> ���</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="code" size="30" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> ����</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="name" size="60" style="width:91% ">
                </td>
            </tr>
            <tr>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> ����</td>
                <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3"><input type="text"
                                                                                                    id="levelno"
                                                                                                    size="60"
                                                                                                    style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> �ϼ�����</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="supercode" size="60" style="width:91% ">
                </td>
                <td width="10%" align="right" nowrap="nowrap"><input name="cbRetrieve" type="button"
                                                                     class="buttonGrooveDisable" id="button"
                                                                     onClick="cbRetrieve_Click()"
                                                                     onMouseOver="button_onmouseover()"
                                                                     onMouseOut="button_onmouseout()" value="�� ��">
                </td>
            </tr>
            <tr>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> �Ƿ�ĩ��</td>
                <td width="30%" class="data_input" colspan="3"><%
                    ZtSelect zs = new ZtSelect("isleaf", "bdgagencyisleaf", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">��������</td>
                <td width="30%" class="data_input" colspan="3"><%
                    zs = new ZtSelect("areacode", "AREACODE", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>


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
    <legend>
        Ԥ�㵥λ��Ϣ�б�
    </legend>
    <table width="100%">
        <tr>
            <td>
                <%=dbGrid.getDBGrid()%>
            </td>
        </tr>
    </table>
</fieldset>
<fieldset>
    <legend>
        ����
    </legend>
    <table width="100%" rules="border" class="title1">
        <tr>
            <td>
                <span id="title"></span>
            </td>
            <td align="right">
                <%=dbGrid.getDataPilot()%>
            </td>
        </tr>
    </table>
</fieldset>
</body>
</html>