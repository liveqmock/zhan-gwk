<!--
/*********************************************************************
* 功能描述: 抵押信息管理抵押详细页面;抵押回证登记 (抵押入库登记)
*
* 作 者: leonwoo
* 开发日期: 2010/01/16
* 修 改 人:
* 修改日期:
* 版 权: 公司
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>
<%@ page import="com.ccb.dao.*" %>
<%@ page import="com.ccb.util.*" %>
<%
    // 贷款申请序号
    String loanID = "";
    // 操作类型
    String doType = "";
    // 抵押编号
    String mortID = "";

    if (request.getParameter("loanID") != null)
        loanID = request.getParameter("loanID");

    if (request.getParameter("doType") != null)
        doType = request.getParameter("doType");
    if ("add".equalsIgnoreCase(doType)) {
        // 自动取出抵押编号
        //mortID = SeqUtil.getSeq(CcbLoanConst.MORTTYPE);
    } else {
        if (request.getParameter("mortID") != null)
            mortID = request.getParameter("mortID");
        // 初始化页面
        LNMORTINFO bean = LNMORTINFO.findFirst("where mortid='" + mortID + "'");
        if (bean != null) {
            StringUtils.getLoadForm(bean, out);
        }
    }

    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

%>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<title>抵押回证登记</title>
<script language="javascript" src="/UI/support/tabpane.js"></script>
<script language="javascript" src="/UI/support/common.js"></script>
<script language="javascript" src="/UI/support/DataWindow.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>
<script language="javascript" src="mortRtnPaperEdit.js"></script>
</head>
<body bgcolor="#ffffff" onLoad="formInit();" class="Bodydefault">
<div id="container">
  <form id="editForm" name="editForm">
    <br>
    <fieldset>
    <legend>抵押入库登记</legend>
    <table width="100%" cellspacing="0" border="0">
      <!-- 操作类型 -->
      <input type="hidden" id="doType" value="<%=doType%>">
      <!-- 版本号 -->
      <input type="hidden" id="recVersion" value="">
      <!-- 抵押登记状态 -->
      <%--<input type="hidden" id="MORTSTATUS" name="MORTSTATUS" value="<%=CcbLoanConst.NODE_RTN_PAPER%>">--%>
      <input type="hidden" id="MORTSTATUS" value=""/>
      <!-- 业务节点 -->
      <input type="hidden" id="busiNode" value="">
      <tr>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">贷款申请序号</td>
        <td width="30%" class="data_input"><input type="text" id="loanID" name="loanID" value="<%=loanID%>"
                                                              style="width:90% " disabled="disabled"></td>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">抵押编号</td>
        <td width="30%" class="data_input"><input type="text" id="mortID" name="mortID" value="<%=mortID%>"
                                                              style="width:90%" disabled="disabled"></td>
      </tr>
      <tr>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">入库日期</td>
        <td width="30%" colspan="3" class="data_input"><input name="PAPERRTNDATE" type="text"
                                                                          id="PAPERRTNDATE" style="width:33.4%"
                                                                          onClick="WdatePicker()" fieldType="date"
                                                                          isNull="false">
          <span class="red_star">*</span></td>
      </tr>
      <tr>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">抵押登记状态</td>
        <td width="30%" colspan="3" class="data_input"><%
                            ZtSelect zs = new ZtSelect("MORTREGSTATUS", "MORTREGSTATUS", "3");
                            zs.addAttr("style", "width: 33.4%");
                            zs.addAttr("fieldType", "text");
                            zs.addAttr("isNull", "false");
                            //zs.addOption("", "");
                            //zs.setDisplayAll(false);
                            out.print(zs);
                        %>
          <span class="red_star">*</span> </td>
      </tr>
      <tr>
        <td width="20%" class="lbl_right_padding">保管内容</td>
        <td width="30%"  class="data_input"><%
                        zs = new ZtSelect("KEEPCONT", "KEEPCONT", "10");
                        zs.addAttr("style", "width: 90%");
                        zs.addAttr("fieldType", "text");
                        zs.addAttr("isNull","false");
                        zs.addOption("", "");
                        out.print(zs);
                    %>
          <span class="red_star">*</span></td>

          <td width="20%" nowrap="nowrap" class="lbl_right_padding">重要档案编号</td>
          <td width="30%" class="data_input"><input type="text" id="documentid" name="documentid" style="width:90%"
                                                    textLength="30" isNull="false"><span class="red_star">*</span>
          </td>

      </tr>
      <tr>
        <%if (doType.equals("select")) { %>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">操作人员</td>
        <td width="30%" class="data_input"><input type="text" value="" style="width:90%"
                                                              disabled="disabled"></td>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">操作时间</td>
        <td width="30%" class="data_input"><input type="text" id="OPERDATE" value="" style="width:90%"
                                                              disabled="disabled"></td>
        <%} else { %>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">操作人员</td>
        <td width="30%" class="data_input"><input type="text" value="<%=omgr.getOperatorName()%>"
                                                              style="width:90%" disabled="disabled"></td>
        <td width="20%" nowrap="nowrap" class="lbl_right_padding">操作时间</td>
        <td width="30%" class="data_input"><input type="text" value="<%=BusinessDate.getToday() %>"
                                                              style="width:90%" disabled="disabled"></td>
        <%} %>
      </tr>
    </table>
    </fieldset>
    <fieldset>
    <legend>操作</legend>
    <table width="100%" class="title1" cellspacing="0">
      <tr>
        <td align="center"><!--查询-->
          <%if (doType.equals("select")) { %>
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="关闭" onClick="window.close();">
          <%} else if (doType.equals("edit") || doType.equals("add")) { %>
          <!--增加，修改-->
          <input id="savebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="保存" onClick="saveClick();">
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="取消" onClick="window.close();">
          <%} else if (doType.equals("delete")) { %>
          <!--删除-->
          <input id="deletebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="删除" onClick="deleteClick();">
          <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="取消" onClick="window.close();">
          <%} %>
        </td>
      </tr>
    </table>
    </fieldset>
  </form>
</div>
<div id="search-result-suggestions">
  <div id="search-results"> </div>
</div>
</body>
</html>
