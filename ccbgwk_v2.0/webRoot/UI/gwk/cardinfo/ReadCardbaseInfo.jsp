<!--
/*********************************************************************
* 功能描述: Odsb 数据读入
* 开发日期: 2010/06/16
* 修 改 人:
* 修改日期:
* 版 权: 公司
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@page import="com.ccb.util.CcbLoanConst" %>
<%@include file="/global.jsp" %>

<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>

    <script language="javascript" src="ReadCardbaseInfo.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
</head>
<!-- onLoad="body_resize()" onResize="body_resize();"-->
<body bgcolor="#ffffff" class="Bodydefault">
<div class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px">
        <%--<fieldset>--%>
        <legend>ODSB读取</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <form id="queryForm" name="queryForm">
                <!-- 仅作为平台打包数据之用 -->
                <input type="hidden" id="operType" name="operType" value="readFromODSB"/>
                <tr height="20">

                    <td align="center" nowrap="nowrap">
                        <input type="button" id="button" onClick="readFromODSB()" value="ODSB卡信息数据读取">
                    </td>
                </tr>
            </form>
        </table>
        <br>
    </fieldset>
</div>
<div id="divResultInfo" class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px;margin-top:0px">

        <legend>ODSB数据读取结果</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="6" width="50%">
            <tr>
                <td width="60%" class="lbl_right_padding">本次新增卡信息记录数</td>
                <td width="40%" class="data_input"  id="_cell_importNewCnt"></td>
            </tr>
            <tr>
                <td width="60%" class="lbl_right_padding">本次注销卡信息记录数</td>
                <td width="40%" class="data_input"  id="_cell_importNullCnt"></td>
            </tr>
            <tr>
                <td width="60%" class="lbl_right_padding">本次更新卡信息记录数</td>
                <td width="40%" class="data_input"  id="_cell_importUpdateCnt"></td>
            </tr>
        </table>
        <br>
    </fieldset>
</div>
</body>
</html>
