<!--
/*********************************************************************
* ��������: ������ѯͳ��
* �� ��: leonwoo
* ��������: 2010/01/16
* �� �� ��:
* �޸�����:
* �� Ȩ: ��˾
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<script language="javascript" src="basicQueryList.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>
</head>
<%
    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

    DBGrid dbGrid = new DBGrid();
    dbGrid.setGridID("loanTab");
    dbGrid.setGridType("edit");

    // ֻͳ���Ѿ�������Ѻ�Ǽǵļ�¼
    String sql = ""
            // ������Ϣ
            + "select "
            //+ "(select deptname from ptdept where deptid=a.bankid) as bankname,"
            + " d.deptname as bankname,"
            + "a.cust_name,"
            + "(select code_desc from ln_odsb_code_desc where code_type_id='053' and code_id=a.LN_TYP) as ln_typ_name,"
            + "a.RT_ORIG_LOAN_AMT,a.RT_TERM_INCR,a.RATECALEVALUE,"
            + "(select enuitemlabel from ptenudetail where enutype='RELEASECONDCD' and enuitemvalue=b.RELEASECONDCD)as RELEASECONDCD,"
            + " a.CUST_OPEN_DT,a.EXPIRING_DT,"
            + "(select code_desc from ln_odsb_code_desc where code_type_id='807' and code_id = a.GUARANTY_TYPE)as GUARANTY_TYPE,"
            //+ "a.RT_TERM_INCR,"
            + "a.PROJ_NO,"
            + "c.proj_name,"
            + "c.PROJ_NAME_ABBR,"
            + "c.CORPNAME,"
            + "(select opername from ptoper where operid=a.custmgr_id) as custmgr_name, "
            //��Ѻ��Ϣ
            
            +"b.MORTDATE,b.MORTID,"
            +"(select enuitemlabel from ptenudetail where enutype='MORTECENTERCD' and enuitemvalue=b.MORTECENTERCD)as MORTECENTERCD,"
            +"(select enuitemlabel from ptenudetail where enutype='MORTREGSTATUS' and enuitemvalue=b.MORTREGSTATUS) as MORTREGSTATUS,"
            +"(select enuitemlabel from ptenudetail where enutype='NOMORTREASONCD' and enuitemvalue=b.NOMORTREASONCD) as NOMORTREASONCD,"            
            +"(select enuitemlabel from ptenudetail where enutype='KEEPCONT' and enuitemvalue=b.KEEPCONT)as keepcont,"
            +"b.EXPRESSNO,b.EXPRESSENDSDATE,b.EXPRESSRTNDATE,"
            +" b.EXPRESSNOTE,"
//            +"(select enuitemlabel from ptenudetail where enutype='RELAYFLAG' and enuitemvalue=b.RELAYFLAG) as RELAYFLAG,"
            +"(select enuitemlabel from ptenudetail where enutype='SENDFLAG' and enuitemvalue=b.SENDFLAG) as SENDFLAG,"
            +"b.MORTEXPIREDATE,b.MORTOVERRTNDATE,"            
            +"b.PAPERRTNDATE,b.CHGPAPERDATE,b.CHGPAPERRTNDATE,b.CLRPAPERDATE,"
            +"(select enuitemlabel from ptenudetail where enutype='CHGPAPERREASONCD' and enuitemvalue=b.CHGPAPERREASONCD) as CHGPAPERREASONCD "
            + " from ln_loanapply a inner join ln_mortinfo b on a.loanid=b.loanid   "
            + " left join ln_coopproj c on a.PROJ_NO=c.PROJ_NO "
            + " left join ptdept d on a.bankid=d.deptid "
            + " where 1=1  ";
    
    dbGrid.setfieldSQL(sql);
    // ������Ϣ
    dbGrid.setField("�������", "text", "8", "bankname", "true", "0");
    dbGrid.setField("���������", "text", "8", "cust_name", "true", "0");
    dbGrid.setField("��������", "text", "8", "ln_typ_name", "true", "0");
    dbGrid.setField("������", "money", "8", "RT_ORIG_LOAN_AMT", "true", "0");
    dbGrid.setField("��������", "text", "8", "RT_TERM_INCR", "true", "0");
    dbGrid.setField("���ʸ�������", "text", "8", "RATECALEVALUE", "true", "0");
    dbGrid.setField("�ſʽ", "text", "8", "RELEASECONDCD", "true", "0");
    dbGrid.setField("��������", "text", "8", "CUST_OPEN_DT", "true", "0");
    dbGrid.setField("��������", "text", "8", "EXPIRING_DT", "true", "0");
    dbGrid.setField("������ʽ", "text", "8", "GUARANTY_TYPE", "true", "0");
    //dbGrid.setField("�������ֵ", "text", "8", "RT_TERM_INCR", "true", "0");
    dbGrid.setField("������Ŀ���", "text", "10", "PROJ_NO", "true", "0");
    dbGrid.setField("������Ŀ����", "text", "12", "proj_name", "true", "0");
    dbGrid.setField("������Ŀ���", "text", "8", "PROJ_NAME_ABBR", "true", "0");
    dbGrid.setField("����������", "text", "12", "CORPNAME", "true", "0");
    dbGrid.setField("�ͻ�����", "text", "6", "custmgr_name", "true", "0");
    //��Ѻ��Ϣ
    
    dbGrid.setField("��Ѻ��������", "text", "8", "MORTDATE", "true", "0");
    dbGrid.setField("��Ѻ���", "text", "8", "MORTID", "true", "0");
    dbGrid.setField("��Ѻ��������", "text", "8", "MORTECENTERCD", "true", "0");
    dbGrid.setField("��Ѻ�Ǽ�״̬", "text", "8", "MORTREGSTATUS", "true", "0");
    dbGrid.setField("δ�����Ѻԭ�� ", "text", "8", "NOMORTREASONCD", "true", "0");    
    dbGrid.setField("��������", "text", "8", "KEEPCONT", "true", "0");
    dbGrid.setField("��ݱ��", "text", "8", "EXPRESSNO", "true", "0");    
    dbGrid.setField("��ݷ�������", "text", "8", "EXPRESSENDSDATE", "true", "0");
    dbGrid.setField("��ݻ�֤����", "text", "8", "EXPRESSRTNDATE", "true", "0");    
    dbGrid.setField("��ݱ�ע", "text", "8", "EXPRESSNOTE", "true", "0");    
//    dbGrid.setField("���ɱ���Ѻ��־", "text", "8", "RELAYFLAG", "true", "0");
    dbGrid.setField("�ɱ�/���ɱ�", "text", "8", "SENDFLAG", "true", "0");
    dbGrid.setField("��Ѻ������", "text", "8", "MORTEXPIREDATE", "true", "0");
    dbGrid.setField("��Ѻ����������", "text", "8", "MORTOVERRTNDATE", "true", "0");    
    dbGrid.setField("�������", "text", "8", "PAPERRTNDATE", "true", "0");
    dbGrid.setField("��֤��������", "text", "8", "CHGPAPERDATE", "true", "0");
    dbGrid.setField("��֤�黹����", "text", "8", "CHGPAPERRTNDATE", "true", "0");
    dbGrid.setField("����ȡ֤����", "text", "8", "CLRPAPERDATE", "true", "0");
    dbGrid.setField("��֤ԭ��", "text", "8", "CHGPAPERREASONCD", "true", "0");
    
    dbGrid.setWhereStr(" and a.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) order by b.mortecentercd,a.bankid,a.cust_name  ");
    dbGrid.setpagesize(10);
    dbGrid.setdataPilotID("datapilot");
    dbGrid.setbuttons("����excel=excel,moveFirst,prevPage,nextPage,moveLast");

%>
<body bgcolor="#ffffff" onLoad="body_resize()" onResize="body_resize();" class="Bodydefault">
<fieldset style="padding-top:-5px;padding-bottom:0px;margin-top:0px">
<legend>��ѯ����</legend>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <form id="queryForm" name="queryForm">
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ����������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="MORTDATE" name="MORTDATE"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="MORTDATE2" name="MORTDATE2"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:90%"></td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">�ſʽ</td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3"><%
                        ZtSelect zs = new ZtSelect("RELEASECONDCD", "RELEASECONDCD", "");
                        zs.addAttr("style", "width: 95%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
    </tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ�Ǽ�״̬</td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3"><%
                        zs = new ZtSelect("MORTREGSTATUS", "MORTREGSTATUS", "");
                        zs.addAttr("style", "width: 95.4%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">�Ƿ��ѵǼ�δ�����Ѻԭ��</td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3"><%
                        zs = new ZtSelect("NOMORTREASONCD", "BOOLTYPE", "");
                        zs.addAttr("style", "width: 95%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
    </tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤ԭ��</td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3"><%
                        zs = new ZtSelect("CHGPAPERREASONCD", "CHGPAPERREASONCD", "");
                        zs.addAttr("style", "width: 95.4%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">����������</td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3">
      	<%
                            zs = new ZtSelect("bankid", "", omgr.getOperator().getDeptid() );
                            zs.setSqlString("select deptid, LPad('&nbsp;', (level - 1) * 36, '&nbsp;') || deptname  from ptdept"
                                    + " start with deptid = '" + omgr.getOperator().getDeptid() + "'"
                                    + " connect by prior deptid = parentdeptid");
                            zs.addAttr("style", "width: 95.4%");
                            zs.addAttr("fieldType", "text");
                            out.print(zs);
                        %>
      </td>
    </tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">������Ŀ���</td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3"><input type="text" id="PROJ_NAME_ABBR" name="PROJ_NAME_ABBR"  fieldType="text" style="width:95%">
      </td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">����������</td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3"><input type="text" id="CORPNAME" name="CORPNAME"  fieldType="text" style="width:95%">
      </td>
    </tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">ȡ�û�ִ������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="RECEIPTDATE"
                                                                          name="RECEIPTDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="RECEIPTDATE2"
                                                                          name="RECEIPTDATE2" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��Ѻ������������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="MORTOVERRTNDATE"
                                                                          name="MORTOVERRTNDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="MORTOVERRTNDATE2"
                                                                          name="MORTOVERRTNDATE2"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:90%"></td>
    </tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">���п������Ƿ�ɱ���Ѻ</td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3"><%
                        zs = new ZtSelect("SENDFLAG", "SENDFLAG", "");
                        zs.addAttr("style", "width: 95%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
      </td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding"></td>
      <td width="30%" nowrap="nowrap" class="data_input" colspan="3"></td>
    </tr>
    <tr style="height:10px"><td></td></tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">���������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="PAPERRTNDATE"
                                                                          name="PAPERRTNDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="PAPERRTNDATE2"
                                                                          name="PAPERRTNDATE2" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">����ȡ֤������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="CLRPAPERDATE"
                                                                          name="CLRPAPERDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="CLRPAPERDATE2"
                                                                          name="CLRPAPERDATE2" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
    </tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤����������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="CHGPAPERDATE"
                                                                          name="CHGPAPERDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="CHGPAPERDATE2"
                                                                          name="CHGPAPERDATE2" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��֤�黹������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="CHGPAPERRTNDATE"
                                                                          name="CHGPAPERRTNDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="CHGPAPERRTNDATE2"
                                                                          name="CHGPAPERRTNDATE2" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
    </tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ݷ���������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="EXPRESSENDSDATE"
                                                                          name="EXPRESSENDSDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="EXPRESSENDSDATE2"
                                                                          name="EXPRESSENDSDATE2"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:90%"></td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">��ݻ�֤������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="EXPRESSRTNDATE"
                                                                          name="EXPRESSRTNDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="EXPRESSRTNDATE2"
                                                                          name="EXPRESSRTNDATE2"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:90%"></td>
    </tr>
    <tr>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">���п��������ɱ���Ѻ������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="RPTNOMORTDATE"
                                                                          name="RPTNOMORTDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="RPTNOMORTDATE2"
                                                                          name="RPTNOMORTDATE2"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:90%"></td>
      <td width="20%" nowrap="nowrap" class="lbl_right_padding">���п������ɱ���Ѻ������</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="RPTMORTDATE"
                                                                          name="RPTMORTDATE" onClick="WdatePicker()"
                                                                          fieldType="date" style="width:90%"></td>
      <td width="2%" nowrap="nowrap" class="lbl_right_padding">��</td>
      <td width="14%" nowrap="nowrap" class="data_input"><input type="text" id="RPTMORTDATE2"
                                                                          name="RPTMORTDATE2"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          style="width:90%"></td>
    </tr>
    <tr>
      <td colspan="8" nowrap="nowrap" align="right"><input class="buttonGrooveDisable" name="cbRetrieve" type="button" id="button"
                           onClick="cbRetrieve_Click(document.queryForm)" onMouseOver="button_onmouseover()"
                           onMouseOut="button_onmouseout()" value="����">
        <input class="buttonGrooveDisable" name="Input" type="reset" value="����"
                           onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()">
      </td>
    </tr>
  </form>
</table>
</fieldset>
<fieldset>
<legend> ��ϸ��Ϣ </legend>
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
</body>
</html>
