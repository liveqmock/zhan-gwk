<!--
/*********************************************************************
* ��������: ������Ŀ�����б�
* ����:
* ��������: 2010/01/24
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
<script language="javascript" src="coopprojList.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>

<script type="text/javascript">
        // ��pulldownֵ���Ƶ�input��
        function setPullToInput(elm) {
            document.getElementById("cust_name").value = elm.innerText;
            document.getElementById("cust_name").focus();
        }
    </script>
</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    //test
    ConnectionManager cm = ConnectionManager.getInstance();
    DatabaseConnection dc = cm.get();

    StringBuilder deptAll = new StringBuilder(" ");
    try {
        RecordSet chrs = dc.executeQuery("select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid");
        while (chrs != null && chrs.next()) {
            deptAll.append("'" + chrs.getString("deptid") + "',");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
//         if ( isAutoRelease )
        cm.release();
    }

    deptAll = deptAll.deleteCharAt(deptAll.length() - 1);


    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("coopprojTable");
    dbGrid.setGridType("edit");

    /*
       ?	����Э������
       ?	����Э����
       (?	�������  ?)
       ?	��������
       ?	������Ŀ����
       ?	������Ŀ���
       ?	Լ���ſʽ
       ?	��ߴ������
       ?	�������ޣ��������ڣ�
       ?	�������
       ?	������Ŀ���
       ?	������

        */

    String sql = "select proj_nbxh," +
//            "rownum," +
            "(select enuitemlabel from ptenudetail where enutype='COOPAGREEMENTCD' and enuitemvalue=a.AGREEMENTCD)||AGREEMENTNO as AGREEMENTCD," +
//            "AGREEMENTNO," +
            "otherpromise," +
            "corpname," +
            "proj_name," +
            "proj_name_abbr," +
            "(select enuitemlabel from ptenudetail where enutype='RELEASECONDCD' and enuitemvalue=a.RELEASECONDCD)as RELEASECONDCD," +
            "MAXLNPERCENT," +
            "coopperiod, " +
            "COOPLIMITAMT," +
            "proj_no,  " +
            "(select deptname from ptdept where deptid=a.bankid)as bankid " +
            "from ln_coopproj a where 1=1  " +
            " ";
//            " a.bankid in(select deptid from ptdept start with deptid='"+omgr.getOperator().getDeptid()+"' connect by prior deptid=parentdeptid) ";

//    sql = "select proj_nbxh,rownum,AGREEMENTCD,AGREEMENTNO,corpname,proj_name,proj_name_abbr, RELEASECONDCD,MAXLNPERCENT,coopperiod,COOPLIMITAMT,proj_no, bankid from (" + sql+" order by proj_no asc )";
    dbGrid.setfieldSQL(sql);

    dbGrid.setField("�ڲ����", "center", "10", "proj_nbxh", "false", "-1");
//    dbGrid.setField("���", "center", "5", "rownum", "true", "0");
    dbGrid.setField("����Э��", "text", "24", "agreementcd", "true", "0");
//    dbGrid.setField("����Э����", "text", "10", "agreementno", "true", "0");
    dbGrid.setField("��������Լ��", "text", "10", "otherpromise", "true", "0");
    dbGrid.setField("������������", "text", "14", "corpname", "true", "0");
    dbGrid.setField("������Ŀ����", "text", "14", "proj_name", "true", "0");
    dbGrid.setField("������Ŀ���", "text", "8", "proj_name_abbr", "true", "0");
    dbGrid.setField("Լ���ſʽ", "center", "8", "releasecondcd", "true", "0");
    dbGrid.setField("��ߴ������", "number", "8", "maxlnpercent", "true", "0");
    dbGrid.setField("��������(��)", "number", "8", "coopperiod", "true", "0");
    dbGrid.setField("�������", "number", "8", "cooplimitamt", "true", "0");
    dbGrid.setField("������Ŀ���", "center", "14", "proj_no", "true", "0");
    dbGrid.setField("������", "center", "10", "bankid", "true", "0");
    dbGrid.setWhereStr(" and a.bankid in(" + deptAll + ") order by proj_no asc ");
    dbGrid.setpagesize(50);
    dbGrid.setdataPilotID("datapilot");

    String menutype = request.getParameter("menutype");
//    if (menutype == null) {
//        dbGrid.setbuttons("�鿴��Ŀ=query,������Ŀ=appendRecod,�޸���Ŀ=editRecord,ɾ����Ŀ=deleteRecord,moveFirst,prevPage,nextPage,moveLast");
//    } else {
        if ("query".equals(menutype)) {
            dbGrid.setbuttons("����Excel=excel,�鿴��Ѻ=queryMort,�鿴��Ŀ=query,moveFirst,prevPage,nextPage,moveLast");
        } else {
            dbGrid.setbuttons("����Excel=excel,�鿴��Ѻ=queryMort,�鿴��Ŀ=query,������Ŀ=appendRecod,�޸���Ŀ=editRecord,ɾ����Ŀ=deleteRecord,moveFirst,prevPage,nextPage,moveLast");
        }
//    }

%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<%--<div id="aa">--%>
<%--<br>--%>
<fieldset>
<legend> ��ѯ���� </legend>
<%--<div class="queryPanalDiv">--%>
<%--<div class="queryDiv">--%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <form id="queryForm" name="queryForm">
    <!-- ���  �������ֶ���Ϊ��ɾ��֮�� -->
    <input type="hidden" id="proj_nbxh" value="">
    <!-- ϵͳ��־��ʹ�� -->
    <input type="hidden" id="busiNode" />
    <tr>
      <td width="20%" class="lbl_right_padding"> ������Ŀ��� </td>
      <td width="30%" align="right" nowrap="nowrap" class="data_input"><%--<input type="text" id="proj_name" size="40" class="ajax-suggestion url-getPull.jsp">--%>
        <input type="text" id="proj_no" size="30" style="width:90% ">
      </td>
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding"> ������Ŀ���� </td>
      <td width="30%" align="right" nowrap="nowrap" class="data_input"><%--<input type="text" id="proj_name" size="40" class="ajax-suggestion url-getPull.jsp">--%>
        <input type="text" id="proj_name" size="60" style="width:90% ">
      </td>
    </tr>
    <tr>
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding"> ������������ </td>
      <td width="30%" align="right" nowrap="nowrap" class="data_input"><input type="text" id="corpname" size="60" style="width:90% ">
      </td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">�Ƿ���</td>
      <td width="30%" class="data_input"><%
                        ZtSelect zs = new ZtSelect("maturityflag", "coopmaturityflag", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
      <td width="10%" align="right" nowrap="nowrap"><input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button"
                           onClick="queryClick()" onMouseOver="button_onmouseover()"
                           onMouseOut="button_onmouseout()" value="�� ��">
      </td>
    </tr>
    <tr>
      <td width="20%" align="right" nowrap="nowrap" class="lbl_right_padding"> ������ </td>
      <td width="30%" class="data_input"><%
//                        zs = new ZtSelect("bankid", "", omgr.getOperator().getDeptid());
                        zs = new ZtSelect("bankid", "", "");
                        zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                                + " start with deptid = '" + omgr.getOperator().getDeptid() + "'"
                                + " connect by prior deptid = parentdeptid");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        out.print(zs);
                    %>
      </td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">Լ���ſʽ</td>
      <td width="30%" class="data_input"><%
                        zs = new ZtSelect("releasecondcd", "releasecondcd", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        //zs.addAttr("isNull","false");
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
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤������Ƿ�Ϊ��</td>
      <td width="30%" class="data_input"><%
                        zs = new ZtSelect("booltype", "booltype", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        //zs.addAttr("isNull","false");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ŀ��������</td>
      <td width="30%" class="data_input"><%
                        zs = new ZtSelect("bankflag", "bankflag", "");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
//                        zs.addAttr("isNull", "false");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
    </tr>
    <%--

            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ʼ¼������</td>
                <td width="30%" class="data_input"><input type="text" id="inputdate1" name="inputdate1" value=""
                                                          style="width:90%" onClick="WdatePicker()" fieldType="date"
                        ></td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">����¼������</td>
                <td width="30%" class="data_input"><input type="text" id="inputdate2" name="inputdate2" value=""
                                                          style="width:90%" onClick="WdatePicker()" fieldType="date"
                        ></td>
            </tr>

--%>
  </form>
</table>
<%--</div>--%>
<%--</div>--%>
<%--

                <div class="queryButtonDiv">
                    <table>
                        <tr>
                            <td width="10%" align="Right" nowrap="nowrap">
                                <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button"
                                       onClick="queryClick()" onMouseOver="button_onmouseover()"
                                       onMouseOut="button_onmouseout()" value="&nbsp;&nbsp;&nbsp;�� ��&nbsp;&nbsp;&nbsp;">
                                <input name="Input" class="buttonGrooveDisable" type="reset"
                                       value="&nbsp;&nbsp;&nbsp;�� ��&nbsp;&nbsp;&nbsp;"
                                       onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()">
                            </td>
                        </tr>
                    </table>
                </div>

    --%>
</fieldset>
<%--<div class="listPanalDiv">--%>
<fieldset>
<legend> ������Ŀ��Ϣ </legend>
<table width="100%">
  <tr>
    <td><%=dbGrid.getDBGrid()%> </td>
  </tr>
</table>
</fieldset>
<FIELDSET>
<LEGEND> ���� </LEGEND>
<table width="100%" class="title1">
  <tr>
    <td align="right"><%=dbGrid.getDataPilot()%> </td>
  </tr>
</table>
</FIELDSET>
<%--</div>--%>
<%--
    <div id="search-result-suggestions">
        <div id="search-results">
        </div>
    </div>
--%>
<%--</div>--%>
</body>
</html>
