<!--
/*********************************************************************
* ��������: ������Ϣ����
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
    <script language="javascript" src="consumeSendMgrList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>

</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("ActionTable");
    dbGrid.setGridType("edit");
/*
    String sql = "select lsh,status,cardname,account,busidate,inac_date,busimoney,limitdate," +
            " tx_cd,ref_number,businame,txlog " +
            " from ls_consumeinfo " +
            " where 1=1  ";
*/
    String sql = " select * from (select (select b.areacode " +
            "          from ls_personalinfo b " +
            "         where trim(b.perid) = " +
            "               (select a.idnumber " +
            "                  from ls_cardbaseinfo a " +
            "                 where a.account = t.account)) as areacode, " +
            " lsh,status,cardname,account,busidate,inac_date,busimoney,limitdate," +
            " tx_cd,ref_number,businame,txlog " +
            " from ls_consumeinfo t) " +
            " where 1=1 ";
    
    dbGrid.setfieldSQL(sql);
    dbGrid.setWhereStr(" and status in ('11','12') order by areacode, lsh ");

    dbGrid.setField("����", "dropdown", "6", "areacode", "true", "AREACODE");
    dbGrid.setField("��ˮ��", "text", "10", "lsh", "true", "0");
    dbGrid.setField("ͨѶ״̬", "dropdown", "8", "status", "true", "CONSUMESTATUS");
    dbGrid.setField("�ֿ���", "text", "8", "cardname", "true", "0");
    dbGrid.setField("���񿨿���", "text", "12", "account", "true", "0");
    dbGrid.setField("��������", "text", "8", "busidate", "true", "0");
    dbGrid.setField("��������", "text", "8", "inac_date", "true", "0");
    dbGrid.setField("���", "money", "8", "busimoney", "true", "0");
    dbGrid.setField("��ٻ�����", "text", "8", "limitdate", "true", "0");
    dbGrid.setField("��������", "text", "8", "tx_cd", "true", "0");
    dbGrid.setField("���ײο����", "text", "15", "ref_number", "true", "0");
    dbGrid.setField("���ѵص�", "text", "15", "businame", "true", "0");
    dbGrid.setField("ͨѶ��־", "text", "20", "txlog", "true", "0");

    dbGrid.setpagesize(40);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setCheck(false);
   
    dbGrid.setbuttons("ȫ������=Send,����Excel=excel,moveFirst,prevPage,nextPage,moveLast");

%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<form id="queryForm" name="queryForm"/>
<fieldset>
    <legend> ���������쳣��Ϣ��ϸ</legend>
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
