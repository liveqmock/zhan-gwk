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
<%@page import="pub.platform.advance.utils.PropertyManager" %>

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
        OperatorManager om = (OperatorManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if ( om == null ) {
            om = new OperatorManager();
            session.setAttribute(SystemAttributeNames.USER_INFO_NAME,om);
        }
        //获取登录用户所属部门，只有分行的用户有权限查看其它支行的数据 2012-11-26
        String areacode = om.getOperator().getDeptid();
        //获取备注里面的内容，如果是admin，有查看其它支行数据的权限 2012-11-26
        String strRemark = om.getOperator().getFillstr150();
        //获取判断条件 2012-11-26
        String strJudeFlag = PropertyManager.getProperty("pub.plat.admin.jude.flag");

        DBGrid dbGrid = new DBGrid();
        dbGrid.setGridID("DbgagencyTable");
        dbGrid.setGridType("edit");
        String strSql = "select areacode,code,name,guid,levelno,supercode,isleaf,version,remark from ls_bdgagency where (1=1)";
        if(!strJudeFlag.equals(strRemark)){
            strSql = strSql + " and areacode='"+areacode+"'";
        }
        dbGrid.setfieldSQL(strSql);
        dbGrid.setField("地区", "dropdown", "10", "areacode", "true", "AREACODE");
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
                <td width="30%" align="right" nowrap="nowrap" class="data_input" colspan="3"><input type="text"
                                                                                                    id="levelno"
                                                                                                    size="60"
                                                                                                    style="width:91% ">
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

                <%if (strJudeFlag.equals(strRemark)){%>
                    <td width="15%" align="right" nowrap="nowrap" class="lbl_right_padding">所属地区</td>
                    <td width="30%" class="data_input" colspan="3"><%
                        zs = new ZtSelect("areacode", "AREACODE", "");
                        zs.addAttr("style", "width: 91%");
                        zs.addAttr("fieldType", "text");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
                <%}else {%>
                    <td width="15%" align="right" nowrap="nowrap" class="data_input"> </td>
                    <td width="30%" class="data_input" colspan="3">
                    <input type="hidden" style="width:90%;" id="areacode" name="areacode" size="40"
                           value="<%=areacode%>"
                           class="ajax-suggestion url-getLoanPull.jsp">
                    </td>
                <%}%>



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