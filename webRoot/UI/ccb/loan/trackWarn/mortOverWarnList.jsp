<!--
/*********************************************************************
* ��������: ��Ѻ������ʱ������
* �� ��: leonwoo
* ��������: 2010/01/16
* �� �� ��:
* �޸�����:
* �� Ȩ: ��˾
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@include file="/global.jsp" %>
<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>
    <script language="javascript" src="mortOverWarnList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("loanTab");
    dbGrid.setGridType("read");
    // ��Ѻ����������-5��=��ǰ���� �����е�Ѻ������ʱ������
    String comSql = ""
            // ǩԼ�ſ�
            + " select * from ( "
            + " select b.MORTDATE,"
            + " to_date(c.cust_open_dt, 'yyyy-mm-dd') + decode(trim(x.FOLLOWUPMORTPERIOD), null, 0, x.FOLLOWUPMORTPERIOD)  as overdate,"
            + " (select enuitemlabel from ptenudetail where enutype='MORTECENTERCD' and enuitemvalue=b.MORTECENTERCD)as MORTECENTERCD,"
            + " b.MORTID,"
            + " (select deptname from ptdept where deptid=c.bankid) as deptname, "
            + " c.cust_name,"
            + " (select code_desc from ln_odsb_code_desc where code_type_id='053' and code_id=c.ln_typ) as ln_typ, "
            + " c.RT_ORIG_LOAN_AMT,c.RT_TERM_INCR,b.RELEASECONDCD,x.PROJ_NAME,x.CORPNAME,b.NOMORTREASONCD,"
            + " (select opername from ptoper where operid=c.custmgr_id) as custmgr_name,"
            + " c.loanid,b.mortstatus,c.bankid,c.LOANSTATE,  "
            + " (select code_desc  from ln_odsb_code_desc where code_type_id='149' and code_id='1' ) as LOANSTATNAME "
            + " from ln_mortinfo b left join ln_loanapply c on b.loanid=c.loanid "
            + " left join ln_coopproj x on c.proj_no=x.proj_no "
            + " ) ";
    String comWhere = ""
            + " where bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) "
            + " and MORTSTATUS  in('10','20') and LOANSTATE ='1' and RELEASECONDCD in ('03','06') ";
    //----------------------------------------------- ��Ѻ������ʱ������---------------------------------------------
    // ��Ѻ����������-5������� = ��ǰ���ڡ����ѹ���
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = new GregorianCalendar();
    String currentDate = BusinessDate.getToday();
    int year = Integer.parseInt(currentDate.substring(0, 4));
    int month = Integer.parseInt(currentDate.substring(5, 7));
    int day = Integer.parseInt(currentDate.substring(8, 10));
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.add(Calendar.DATE, 5);
    String tempDate1 = df.format(calendar.getTime());
    String tempWhere1 = " and to_char(overdate,'yyyy-mm-dd') <= '" + tempDate1 + "' and to_char(overdate,'yyyy-mm-dd')>'" + currentDate + "'";
    String sql1 = comSql + comWhere + tempWhere1;


    String totalSql = sql1 + " order by mortid asc ";

    dbGrid.setfieldSQL(totalSql);
    dbGrid.setField("��Ѻ��������", "center", "10", "MORTDATE", "true", "0");
    dbGrid.setField("����������", "center", "10", "MORTEXPIREDATE", "true", "0");
    dbGrid.setField("��������", "center", "10", "MORTECENTERCD", "true", "0");
    dbGrid.setField("��Ѻ���", "text", "10", "MORTID", "true", "0");
    dbGrid.setField("������", "center", "10", "deptname", "true", "0");
    dbGrid.setField("���������", "text", "10", "cust_name", "true", "0");
    dbGrid.setField("��������", "text", "16", "ln_typ", "true", "0");
    dbGrid.setField("������", "money", "10", "RT_ORIG_LOAN_AMT", "true", "0");
    dbGrid.setField("��������", "text", "10", "RT_TERM_INCR", "true", "0");
    dbGrid.setField("�ſʽ", "dropdown", "10", "RELEASECONDCD", "true", "RELEASECONDCD");
    dbGrid.setField("������Ŀ����", "text", "40", "PROJ_NAME", "true", "0");
    dbGrid.setField("����������", "text", "30", "CORPNAME", "true", "0");
    dbGrid.setField("δ�����Ѻԭ��", "dropdown", "16", "NOMORTREASONCD", "true", "NOMORTREASONCD");
    dbGrid.setField("�ͻ�����", "text", "10", "custmgr_name", "true", "0");
    dbGrid.setField("�����������", "text", "18", "loanid", "true", "0");
    dbGrid.setField("��Ѻ����״̬", "dropdown", "8", "mortstatus", "false", "MORTSTATUS");
    dbGrid.setField("������", "text", "8", "deptid", "false", "0");
    dbGrid.setField("����״̬��", "text", "8", "LOANSTATE", "false", "0");
    dbGrid.setField("����״̬", "text", "8", "LOANSTATENAME", "false", "0");
    dbGrid.setpagesize(30);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setbuttons("����Excel=excel,moveFirst,prevPage,nextPage,moveLast");

%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
    <form id="queryForm" name="queryForm">
        <!-- ���������ֶΣ�Ϊ�˲�ѯʹ�� -->
        <input type="hidden" id="deptid" value="<%=omgr.getOperator().getDeptid()%>">
        <input type="hidden" id="tempWhere1" value="<%=tempWhere1 %>"/>
    </form>
</table>
<fieldset>
    <legend> ��Ѻ������ʱ������</legend>
    <table width="100%">
        <tr>
            <td><%=dbGrid.getDBGrid()%>
            </td>
        </tr>
    </table>
</fieldset>
<fieldset>
    <legend> ����</legend>
    <table width="100%">
        <tr>
            <td align="right"><%=dbGrid.getDataPilot()%>
            </td>
        </tr>
    </table>
</fieldset>
</body>
</html>
