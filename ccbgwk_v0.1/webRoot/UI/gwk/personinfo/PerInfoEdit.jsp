<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-6-8
  Time: 17:10:08
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=GBK"%>
<%@include file="/global.jsp"%>
<%@ page import="com.ccb.dao.*" %>
<%@ page import="com.ccb.util.*" %>
<%@ page import="gwk.dao.LSPERSONALINFO" %>
<html>
  <head><title>������Ϣ¼��</title>
    <LINK href="/css/ccb.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="PerInfoEdit.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/DataWindow.js"></script>
    <script language="javascript" src="/js/function.js"></script>
    <script language="javascript" src="identityValidate.js"></script>
    <%
        String doType = "";      //����
        String recSequence = "";       //�ڲ����к�
        String deptCD = "";      //Ԥ�㵥λ���
        int recVer = 0;      //�汾��
        if (request.getParameter("doType") != null)
            doType = request.getParameter("doType");
        if (!doType.equals("add"))
            recSequence = request.getParameter("recSequ");
        // ��ʼ��ҳ��
        LSPERSONALINFO bean = LSPERSONALINFO.findFirst("where recinsequence='" + recSequence + "'");
        if (bean != null){
            //ȡ��Ԥ�㵥λcode
            deptCD = bean.getDeptcode();
            recVer = bean.getRecversion();
            StringUtils.getLoadForm(bean,out);
        }
        OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    %>
  </head>
  <body onload="formInit()">
      <form id="editForm" name="editForm">
          <input type="hidden" id="hhidDeptCode" value="<%=deptCD%>">
          <input type="hidden" id="createcode" value="">
          <input type="hidden" id="recinsequence" value="<%=recSequence%>">
          <input type="hidden" id="recversion" value="<%=recVer%>">
          <fieldset>
              <legend>������Ϣ</legend>
              <table width="100%" cellspacing="0" border="0">
                <!-- ϵͳ��־ʹ�� -->
                <input type="hidden" id="busiNode"/>
                <tr>
                    <td width="15%" nowrap="nowrap" class="lbl_right_padding">����</td>
                    <td width="35%" class="data_input"><input type="text" id="pername" name="pername" value=""
                                                                          textLength="24"
                                                                          style="width:90% " isNull="false">
                        <span class="red_star">*</span></td>
                    <td width="15%" class="lbl_right_padding" nowrap="nowrap" >
                        ���֤��
                    </td>
                    <td width="35%" class="data_input">
                        <input type="text" id="perid" name="perid" onblur="isIdCardNo(this)" value="" style="width:90%" isNull="false">
                        <span class="red_star">*</span>
                    </td>
                </tr>
                <tr>
                    <td width="15%" nowrap="nowrap" class="lbl_right_padding">һ��Ԥ�㵥λ</td>
                    <td width="35%" class="data_input">
                        <%
                            ZtSelect zs = new ZtSelect("superdeptcode", "", "");
//                          ZtSelect zs = new ZtSelect("BANKID", "", omgr.getOperator().getDeptid());
                            //zs.setSqlString("select deptid as value ,deptname as text  from ptdept");
                            zs.setSqlString("select code , name from ls_bdgagency t where lengthb(code)=3 ");
                            zs.addAttr("style", "width: 90%");
                            zs.addAttr("fieldType", "text");
                            zs.addAttr("onchange", "reSelect()");
                            //zs.setDefValue("371980000");
                            zs.addOption("", "");
                            zs.addAttr("isNull", "false");
                            out.print(zs);
                        %>
                        <span class="red_star">*</span></td>
                    <td width="15%" nowrap="nowrap" class="lbl_right_padding">Ԥ�㵥λ</td>
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
              </table>
          </fieldset>
          <fieldset>
          <p style="height:100px;"></p>    
          <legend>����</legend>
            <table width="100%" class="title1" cellspacing="0">
                <tr>
                    <td align="center"><!--��ѯ-->
                        <% if (doType.equals("select")){ %>
                              <input id="closebut" class="buttonGrooveDisable" onmouseover="button_onmouseover()"
                       onmouseout="button_onmouseout()" type="button" value="�ر�" onclick="window.close();">
                        <% }
                            else{
                        %>
                        <input id="savebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="����" onClick="saveClick();">
                        <input id="closebut" class="buttonGrooveDisable" onMouseOver="button_onmouseover()"
                               onmouseout="button_onmouseout()" type="button" value="ȡ��" onClick="window.close();">
                        <% } %>
                    </td>
                </tr>
            </table>
          </fieldset>
       </form>
  </body>
</html>