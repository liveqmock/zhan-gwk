<!--
  /*********************************************************************
  *    ��������:    1.
  *
  *    ��    ��:    leonwoo
  *    ��������:    2005/01/28
  *    �� �� ��:
  *    �޸�����:
  *    ��    Ȩ:    leonwoo
  ***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK"%>
<%@include file="/global.jsp"%>
<%@page import="pub.platform.security.OperatorManager"%>
<%@page import="pub.platform.form.config.SystemAttributeNames"%>
<%@page import="pub.platform.db.DBGrid"%>
<%@page import="pub.platform.html.ZtSelect"%>
<html>
  <head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>
 
    <script language="javascript" src="js/fileList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/suggest/js/ajaxSuggestions.js"></script>
    <style type="text/css" media="screen">
        @import url("/UI/support/suggest/css/ajax-suggestions.css");
    </style>
     <script type="text/javascript">
     // ��pulldownֵ���Ƶ�input��
      function setPullToInput(elm){
        document.getElementById("file_name").value=elm.innerText;
        document.getElementById("file_name").focus();
       }
    </script> 
  </head>
  <%
        OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        String deptId = omgr.getOperator().getPtDeptBean().getDeptid();

        DBGrid dbGrid = new DBGrid();
        dbGrid.setGridID("itemTab");
        dbGrid.setGridType("edit");

        String sql = "select  file_id,file_name,impawnNoReason,file_date,file_lastdate,isvisibled,file_type from ptdemo where 1=1 ";
        dbGrid.setfieldSQL(sql);

        dbGrid.setField("Ψһ���", "text", "5", "file_id", "false", "-1");
        dbGrid.setField("�ļ�����", "text", "10", "file_name", "true", "0");
        dbGrid.setField("δ�����Ѻԭ��", "text", "10", "impawnNoReason", "true", "0");
        dbGrid.setField("�ļ�����ʱ��", "text", "8", "file_date", "true", "0");
        dbGrid.setField("�ļ���Ч����", "text", "8", "file_lastdate", "true", "0");

        dbGrid.setField("�ļ��Ƿ���ʾ", "dropdown", "8", "isvisibled", "true", "BOOLTYPE");
        dbGrid.setField("�ſ���ʽ", "dropdown", "8", "file_type", "true", "impawnLoanType");

        dbGrid.setWhereStr(" order by file_date desc ");
        dbGrid.setpagesize(17);
        dbGrid.setdataPilotID("datapilot");
        dbGrid.setbuttons("�鿴=query,���=appendRecod,�༭=editRecord,ɾ��=deleteRecord,moveFirst,prevPage,nextPage,moveLast");
  %>
  <body bgcolor="#ffffff" onload="body_resize()" onresize="body_resize();" class="Bodydefault">
    <fieldset>
      <legend>
        ��ѯ����
      </legend>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" >
        <form id="queryForm" name="queryForm">
          <input type="hidden" id="fileId" value="">
        <tr height="20">
          <td width="14%" align="right" nowrap="nowrap" class="lbl_right_padding">
            �ļ�����
          </td>
          <td align="right" nowrap="nowrap" class="data_input">
            
            <input type="text" id="file_name" class="ajax-suggestion url-getPull.jsp">
          </td>
          <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">
          �ſ���ʽ
          </td>
          <td width="20%" class="data_input">
            <%
                  ZtSelect zs = new ZtSelect("file_type", "impawnLoanType", "");
                  //ZtSelect zs = new ZtSelect("file_type", "");
                  //zs
                  //    .setSqlString("select a.loanType as value ,(select enuitemlabel from ptenudetail where enutype='loanType' and enuitemvalue=a.file_id) as text  from aic_file_userpower a where a.deptid='"
                  //        + deptId + "' order by file_id");
                  //zs.setSqlString("select ENUITEMVALUE as value ,ENUITEMLABEL as text  from PTENUDETAIL where enutype='QYLB'"
                  //+" and enuitemvalue not in('4','5')");
                  zs.addAttr("style", "width: 100%");

                  zs.addAttr("fieldtype", "text");
                  //zs.addAttr("isNull","false");
                  //zs.addAttr("onchange","fulx()");
                  //zs.addAttr("alertTitle","��ҵ����");
                  zs.addOption("", "");
                  zs.setDisplayAll(false);
                  out.print(zs);
            %>
          </td>
          <td width="14%" align="center" nowrap="nowrap">
            <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click(document.queryForm)" onMouseOver="button_onmouseover()"
              onMouseOut="button_onmouseout()" value="����">
            <input name="Input" class="buttonGrooveDisable" type="reset" value="����" onMouseOver="button_onmouseover()" onMouseOut="button_onmouseout()">
          </td>
        </tr>
        </form>
      </table>
    </fieldset>

    <fieldset>
      <legend>
        �ļ��б�
      </legend>
      <table width="100%">
        <tr>
          <td>
            <%=dbGrid.getDBGrid()%>
          </td>
        </tr>
      </table>
    </fieldset>
    <FIELDSET>
      <LEGEND>
        ����
      </LEGEND>
      <table width="100%" class="title1">
        <tr>
          <td align="right">
            <%=dbGrid.getDataPilot()%>
          </td>
        </tr>
      </table>
    </FIELDSET>
    
    <div id="search-result-suggestions">
      <div id="search-results">
      </div>      
     </div>
    
  </body>
</html>
