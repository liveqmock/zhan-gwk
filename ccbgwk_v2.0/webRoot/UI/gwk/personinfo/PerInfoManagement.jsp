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
<head><title>持卡人信息</title>
    <LINK href="/css/ccb.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/js/basic.js"></script>
    <script language="javascript" src="/js/xmlHttp.js"></script>
    <script language="javascript" src="/js/dbgrid.js"></script>
    <script language="javascript" src="/js/dropdownData.js"></script>
    <script language="javascript" src="/js/dbutil.js"></script>
    <script language="javascript" src="PerInfoManagement.js"></script>
    <%
      String deptCode = request.getParameter("deptCode");
      String levelNo = request.getParameter("levelNo");
      String strSql =  "select recinsequence, p.areacode, pername , perid ,lg.name as deptName ,lg1.name superDeptName" +
               ",p.createdate  from ls_personalinfo p" +
               " left join  ls_bdgagency lg on p.deptcode = lg.code and p.areacode=lg.areacode" +
               " left join ls_bdgagency lg1 on p.superdeptcode = lg1.code and p.areacode=lg1.areacode where 1=1 ";
       System.out.println(strSql);
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("PerInfoTab");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setfieldcn("内部序号,地区,姓名,身份证号码,预算单位,一级预算单位,操作日期");
       dbGrid.setenumType("-1,0,0,0,0,0");
       dbGrid.setvisible("false,true,true,true,true,true,true");
       dbGrid.setfieldName("recinsequence,areacode,pername,perid,deptName,superDeptName,createdate");
       dbGrid.setfieldWidth("0,10,10,15,20,20,15");
       dbGrid.setfieldType("text,dropdown,text,text,text,text,text");
       dbGrid.setenumType("0,AREACODE,0,0,0,0,0");
       dbGrid.setfieldCheck(";textLength=20;textLength=40; textLength=20;textLength=18;textLength=18;textLength=20");
       dbGrid.setpagesize(50);
       dbGrid.setCheck(false);
       dbGrid.setdataPilotID("datapilot");
       if (deptCode == null && levelNo == null){
           dbGrid.setbuttons("导出Excel=excel,添加个人信息=appendRecod,编辑=editRecord,删除=deleteRecord,moveFirst,prevPage,nextPage,moveLast");
       } else{
           dbGrid.setbuttons("导出Excel=excel,moveFirst,prevPage,nextPage,moveLast");

       }
    %>
</head>
<body bgcolor="#ffffff" onload="body_load();body_resize() " class="Bodydefault">
<fieldset>
    <legend>
        查询条件
    </legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <!-- 隐藏字段，删除之用 -->
            <input type="hidden" id="recinsequence"/>
            <!-- 系统日志表使用 -->
            <input type="hidden" id="busiNode"/>
            <tr height="20">
                <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">所属地区</td>
                <td width="30%" class="data_input"><%
                    ZtSelect zs = new ZtSelect("areacode", "AREACODE", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>
                <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                    预算单位
                </td>
                <td width="30%" align="right" nowrap="nowrap" class="data_input">
                    <input type="text" style="width:90%;" id="departmentName" name="departmentName"
                           value="" size="40" class="ajax-suggestion url-getLoanPull.jsp">
                    <input type="hidden" value="<%=deptCode%>" id="departmentID" name="departmentID"/>
                    <input type="hidden" value="<%=levelNo%>" id="levelNo" name="levelNo"/>
                </td>
                <td align="center" nowrap="nowrap">
                    <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button"
                           onClick="cbRetrieve_Click()" value="检索">
                </td>
            </tr>
            <tr height="20">
                <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                    姓名
                </td>
                <td width="30%" align="right" nowrap="nowrap" class="data_input">
                    <input style="width:90%" type="text" id="cust_name" size="40" >
                </td>
                <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                    身份证ID
                </td>
                <td width="30%" align="right" nowrap="nowrap" class="data_input">
                    <input type="text" style="width:90%;" id="personalID" name="personalID" size="40" >
                </td>
                <td align="center" nowrap="nowrap">
                    <input name="Input" class="buttonGrooveDisable" type="reset" value="重填">
                </td>
            </tr>
        </form>
    </table>
</fieldset>
<fieldset>
    <legend>
        个人信息
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
        操作
    </LEGEND>
    <table width="100%" class="title1">
        <tr>
            <td id="cellButtons" align="right">
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