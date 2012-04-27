<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-6-8
  Time: 17:10:08
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%@ page import="com.ccb.dao.*" %>
<%@ page import="com.ccb.util.*" %>
<%@ page import="com.ccb.dao.LSPERSONALINFO" %>
<%@ page import="com.ccb.dao.PTOPER" %>
<html>
<head><title>个人信息录入</title>
    <LINK href="/css/ccb.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="PerInfoEdit.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/DataWindow.js"></script>
    <script language="javascript" src="/js/function.js"></script>
    <script language="javascript" src="/js/identityValidate.js"></script>
    <%
        String doType = "";      //类型
        String recSequence = "";       //内部序列号
        String deptCD = "";      //预算单位编号
        String createName = "";  //操作人员名
        int recVer = 0;      //版本号
        OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (request.getParameter("doType") != null)
            doType = request.getParameter("doType");
        if (!doType.equals("add")) {
            recSequence = request.getParameter("recSequ");
        }

        // 初始化页面
        LSPERSONALINFO bean = LSPERSONALINFO.findFirst(" where recinsequence='" + recSequence + "'");

        if (bean != null) {
            //取得预算单位code
            deptCD = bean.getDeptcode();
            recVer = bean.getRecversion();
            StringUtils.getLoadForm(bean, out);
        }
        if (doType.equals("select")) {
            if (bean.getCreatecode() != 0) {
                PTOPER ptrbean = PTOPER.findFirst(" where operid ='" + bean.getCreatecode() + "'");
                createName = ptrbean.getOpername();
            } else {
                createName = "";
            }

        } else {
            createName = omgr.getOperatorName();
        }
    %>
</head>
<body onload="formInit()">
<form id="editForm" name="editForm">
    <input type="hidden" id="hhidDeptCode" value="<%=deptCD%>">
    <input type="hidden" id="createcode" value="">
    <input type="hidden" id="recinsequence" value="<%=recSequence%>">
    <input type="hidden" id="recversion" value="<%=recVer%>">
    <fieldset>
        <legend>个人信息</legend>
        <table width="100%" cellspacing="0" border="0">
            <!-- 系统日志使用 -->
            <input type="hidden" id="busiNode"/>
            <tr>
                <td width="15%" nowrap="nowrap" class="lbl_right_padding">姓名</td>
                <td width="35%" class="data_input"><input type="text" id="pername" name="pername" value=""
                                                          textLength="24"
                                                          style="width:90% " isNull="false">
                    <span class="red_star">*</span></td>
                <td width="15%" class="lbl_right_padding" nowrap="nowrap">
                    身份证号
                </td>
                <td width="35%" class="data_input">
                    <input type="text" id="perid" name="perid" value="" style="width:90%" isNull="false">
                    <span class="red_star">*</span>
                </td>
            </tr>
            <tr>
                <td width="15%" nowrap="nowrap" class="lbl_right_padding">一级预算单位</td>
                <td width="35%" class="data_input">
                    <%
                        ZtSelect zs = new ZtSelect("superdeptcode", "", "");
                        zs.setSqlString("select code , name from ls_bdgagency t where levelno=1 ");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addAttr("onchange", "reSelect()");
                        zs.addOption("", "");
                        zs.addAttr("isNull", "false");
                        out.print(zs);
                    %>
                    <span class="red_star">*</span></td>
                <td width="15%" nowrap="nowrap" class="lbl_right_padding">预算单位</td>
                <td width="35%" class="data_input">
                    <%
                        zs = new ZtSelect("deptcode", "", "");
                        //zs.setSqlString("select OPERID as value ,OPERNAME as text  from ptoper");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addAttr("isNull", "false");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
                    <span class="red_star">*</span></td>
            </tr>
            <tr>
                <td width="15%" nowrap="nowrap" class="lbl_right_padding">所属地区</td>
                <td width="35%" class="data_input">
                    <%
                        zs = new ZtSelect("areacode", "AREACODE", "");
                        //zs.setSqlString("select OPERID as value ,OPERNAME as text  from ptoper");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addAttr("isNull", "false");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
                    <span class="red_star">*</span></td>
            </tr>
        </table>
    </fieldset>
    <br>
    <fieldset>
        <legend>操作信息</legend>
        <table width="100%" class="title1" cellspacing="0">
            <tr>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">操作人员</td>
                <td width="30%" class="data_input"><input type="text" value="<%=createName%>"
                                                          style="width:90%" disabled="disabled">
                </td>
                <td width="20%" nowrap="nowrap" class="lbl_right_padding">操作时间</td>
                <td width="30%" class="data_input"><input type="text" value="<%=BusinessDate.getToday() %>"
                                                          style="width:90%" disabled="disabled">
                </td>
            </tr>
        </table>
    </fieldset>
    <br>
    <fieldset>
        <legend>操作</legend>
        <table width="100%" class="title1" cellspacing="0">
            <tr>
                <td align="center"><!--查询-->
                    <% if (doType.equals("select")) { %>
                    <input id="closebut" class="buttonGrooveDisable" onmouseover="button_onmouseover()"
                           onmouseout="button_onmouseout()" type="button" value="关闭" onclick="window.close();">
                    <% } else {
                    %>
                    <input id="savebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                           onmouseout="button_onmouseout()" type="button" value="保存" onClick="saveClick();">
                    <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                           onmouseout="button_onmouseout()" type="button" value="取消" onClick="window.close();">
                    <% } %>
                </td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>