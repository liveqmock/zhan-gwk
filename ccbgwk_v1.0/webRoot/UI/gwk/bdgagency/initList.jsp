<!--
/*********************************************************************
* 功能描述: 预算单位基本信息初始化
* 开发日期: 2012/4/26
* 修 改 人: zhanrui
* 修改日期:
* 版 权: 公司
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp" %>

<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>

    <script language="javascript" src="initList.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
</head>
<body bgcolor="#ffffff" class="Bodydefault">
<div class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px">
        <legend style="margin-left:20px">预算单位基本信息初始化</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="0" width="100%" style="padding-top:20px;padding-bottom:10px">
            <form id="queryForm" name="queryForm">
                <input type="hidden" id="operType" name="operType" value="initData"/>
                <input type="hidden" id="newLsh" name="newLsh" value=""/>
                <tr height="20">
                    <td align="center" nowrap="nowrap">
                        <input type="button" id="button" onClick="initData()" value="   确  认   ">
                    </td>
                </tr>
            </form>
        </table>
    </fieldset>
</div>
</body>
</html>
