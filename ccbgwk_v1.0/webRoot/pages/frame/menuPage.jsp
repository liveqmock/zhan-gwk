<%@ page contentType="text/html; charset=GBK" %>
<%@ include file="/pages/security/online.jsp" %>
<%@ page import="pub.platform.security.OperatorManager" %>
<%@ page import="pub.platform.form.config.SystemAttributeNames" %>
<%@ page import="pub.platform.security.OperatorManager" %>
<%@ page import="pub.platform.utils.Basic" %>
<%
    response.setContentType("text/html; charset=GBK");
%>
<html xmlns:hGui>
<head>
    <link href="/css/ccb.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="/js/xmlHttp.js"></script>
    <script language="javascript" src="/js/basic.js"></script>
    <script language="javascript" src="/js/menuPage.js"></script>
    <script language="javascript" src="/js/tree.js"></script>
    <script language="javascript" src="/js/dbutil.js"></script>

    <LINK href="/dhtmlx/codebase/dhtmlxlayout.css" type="text/css" rel="stylesheet">
    <LINK href="/dhtmlx/codebase/skins/dhtmlxlayout_dhx_skyblue.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="/dhtmlx/codebase/dhtmlxcommon.js"></script>
    <script language="javascript" src="/dhtmlx/codebase/dhtmlxlayout.js"></script>

    <LINK href="/dhtmlx/codebase/skins/dhtmlxaccordion_dhx_skyblue.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="/dhtmlx/codebase/dhtmlxaccordion.js"></script>
    <script language="javascript" src="/dhtmlx/codebase/dhtmlxcontainer.js"></script>

    <LINK href="/dhtmlx/codebase/dhtmlxtree.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="/dhtmlx/codebase/dhtmlxtree.js"></script>
    <script language="javascript" src="/dhtmlx/codebase/ext/dhtmlxtree_json.js"></script>

    <script language="javascript">
        <%
             String jsonDefaultMenu = null;
             String jsonSystemMenu = null;
             OperatorManager om = (OperatorManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
             try {
//                 xmlString =om.getXmlString();
                 jsonDefaultMenu = om.getJsonString("default");
                 jsonSystemMenu = om.getJsonString("system");
                 System.out.println( om.getOperator().getPtDeptBean().getSuperdqdm());
                 System.out.println( om.getOperator().getPtDeptBean().getSuperdqmc());
              } catch(Exception e) {
                 System.out.println("jsp" +e +"\n");
              }

//              xmlString = "{id:0,item:[{'id':'X1',text:'aa',userdata:[{name:'href',content:'http://www.baidu.com'}]}]}";
//              xmlString = "{id:0,item:[{'id':'X1',text:'aa',item:[{id:'a1',text:'asas',item:[]}]},{'id':'X1',text:'bb'}]}";
              //xmlString = "'{id:0,item:[{id:\"X1\",text:\"bb\"}]}'";
         %>
        var defaultMenuStr = '<%=jsonDefaultMenu%>';
        var systemMenuStr = '<%=jsonSystemMenu%>';

        var dhxAccord;
        function doOnLoad() {
            document.all("accordObj").style.height = document.body.clientHeight - 2;
            dhxAccord = new dhtmlXAccordion("accordObj");
            dhxAccord.setSkin("dhx_skyblue");
            dhxAccord.setIconsPath("/dhtmlx/codebase/icons/");
            dhxAccord.addItem("a1", "业务处理");
            dhxAccord.addItem("a2", "系统管理");
            dhxAccord.addItem("a3", "用户信息");
            dhxAccord.addItem("a4", "帮助信息");
            dhxAccord.openItem("a1");
            dhxAccord._enableOpenEffect = true;

            dhxAccord.cells("a1").setIcon("accord_biz.png");
            dhxAccord.cells("a2").setIcon("accord_manage.png");
            dhxAccord.cells("a3").setIcon("editor.gif");
            dhxAccord.cells("a4").setIcon("accord_support.png");
            var biztree = dhxAccord.cells("a1").attachTree();
            var managetree = dhxAccord.cells("a2").attachTree();

            var treeDefaultJson = eval('(' + defaultMenuStr + ')');

            biztree.setSkin('dhx_skyblue');
            biztree.setImagePath("/dhtmlx/codebase/imgs/csh_books/");
            biztree.loadJSONObject(treeDefaultJson);
            biztree.attachEvent("onClick", function(id) {
                var action = (biztree.getUserData(id, "url"));
                if (action == "#") {
                    biztree.openItem(id);
                } else {
                    parent.window.workFrame.location.replace(action);
                }
                return true;
            });

            var treeSystemJson = eval('(' + systemMenuStr + ')');

            managetree.setSkin('dhx_skyblue');
            managetree.setImagePath("/dhtmlx/codebase/imgs/csh_books/");
            managetree.loadJSONObject(treeSystemJson);
            managetree.attachEvent("onClick", function(id) {
                var action = (managetree.getUserData(id, "url"));
                if (action == "#") {
                    managetree.openItem(id);
                } else {
                    parent.window.workFrame.location.replace(action);
                }
                return true;
            });


        }

        function doOnResize() {
            var parentObj = document.getElementById("accordObj");
            parentObj.style.height = document.body.clientHeight - 2;
            dhxAccord.setSizes();
        }
    </script>

</head>


<body onload="doOnLoad();" onResize="doOnResize();">

<div id="accordObj" style="width: 194px; height: 500px; margin-left:6px;"></div>

</body>
</html>
