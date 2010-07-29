<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-6-8
  Time: 13:37:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp"%>
<%@ page import="pub.platform.db.*"%>
<html>
  <head><title>预算单位信息查询</title>
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
        dbGrid.setfieldSQL("select code,name,guid,levelno,supercode,isleaf,version,remark from ls_bdgagency where (1=1)");
     // dbGrid.setfieldSQL("select code as keyCode,code,name,guid,levelno,supercode,isleaf,version,remark from ls_bdgagency where (1=1)");

      /*dbGrid.setfieldcn("主键,编号,名称,GUID,级次,上级编码,是否末级,当前版本,备注");
        dbGrid.setenumType("-1,0,0,0,0,0,BDGAGENCYISLEAF,0,0");
        dbGrid.setvisible("false,true,true,true,true,true,true,true,true");
        dbGrid.setfieldName("keyCode,code,name,guid,levelno,supercode,isleaf,version,remark");
        dbGrid.setfieldWidth("10,0,20,30,40,10,10,10,10,20");
        //dbGrid.setfieldType("text,text,text,text,text,text,text,text,text");
        //dbGrid.setfieldType("text,text,text,text,text,text,text,text,text");        
        */
         dbGrid.setField("编号", "text", "20", "code", "true", "0");
       dbGrid.setField("名称", "text", "30", "name", "true", "0");
       dbGrid.setField("GUID", "text", "40", "guid", "true", "0");
       dbGrid.setField("级次", "text", "10", "levelno", "true", "0");
       dbGrid.setField("上级编码", "text", "10", "supercode", "true", "0");
       dbGrid.setField("是否末级", "dropdown", "10", "isleaf", "true", "BDGAGENCYISLEAF");
       dbGrid.setField("当前版本", "text", "10", "version", "true", "0");
       dbGrid.setField("备注", "text", "20", "remark", "true", "0");


        dbGrid.setpagesize(50);
        dbGrid.setWhereStr(" order by code");
        dbGrid.setdataPilotID("datapilot");
        dbGrid.setbuttons("导出Excel=excel,查看详细=query,moveFirst,prevPage,nextPage,moveLast");
  %>
  </head>
   <body onload="body_load()" onresize="body_load()" class="Bodydefault">
    <fieldset>
    <legend> 查询条件</legend>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <tr>
                <td width="15%" class="lbl_right_padding"> 编号</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="code" size="30" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 名称</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="name" size="60" style="width:91% ">
                </td>
            </tr>
            <tr>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 级次</td>
                <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3"><input type="text" id="levelno"
                                                                                        size="60" style="width:91% ">
                </td>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 上级编码</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="supercode" size="60" style="width:91% ">
                </td>
                <td width="10%" align="right" nowrap="nowrap"><input name="cbRetrieve" type="button"
                                                                     class="buttonGrooveDisable" id="button"
                                                                     onClick="cbRetrieve_Click()"
                                                                     onMouseOver="button_onmouseover()"
                                                                     onMouseOut="button_onmouseout()" value="检 索">
                </td>
            </tr>
            <tr>

                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 是否末级</td>
                <td width="30%" class="data_input" colspan="3"><%
                    ZtSelect zs = new ZtSelect("isleaf", "bdgagencyisleaf", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>
                <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding"> 版本</td>
                <td width="30%" align="right" nowrap="nowrap"
                    class="data_input" colspan="3">
                    <input type="text" id="version" size="60" style="width:91% ">
                </td>


                <td width="10%" align="right" nowrap="nowrap"><input name="Input"
                                                                     class="buttonGrooveDisable"
                                                                     type="reset"
                                                                     value="重 填"
                                                                     onMouseOver="button_onmouseover()"
                                                                     onMouseOut="button_onmouseout()">
                </td>
            </tr>

        </form>
    </table>
    </fieldset>
    <fieldset>
      <legend>
        预算单位信息列表
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
        操作
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