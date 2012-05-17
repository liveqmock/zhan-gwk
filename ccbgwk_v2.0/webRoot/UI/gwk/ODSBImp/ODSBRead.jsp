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
<%@include file="/global.jsp" %>

<html>
<head>
    <META http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>

    <script language="javascript" src="ODSBRead.js"></script>
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
                <input type="hidden" id="newLsh" name="newLsh" value=""/>
                <tr height="20">

                    <%--<td width="70%" class="data_input">从ODSB库读取数据</td>--%>
                    <td align="center" nowrap="nowrap">
                        <input type="button" id="button" onClick="readFromODSB()" value="ODSB消费数据读取">
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
        <table border="0" cellspacing="0" cellpadding="5" width="50%">
            <tr>
                <td width="60%" class="lbl_right_padding">本次读取ODSB数据记录数</td>
                <td width="40%" class="data_input"  id="_cell_importCnt"></td>
            </tr>
            <tr>
                <td width="60%" class="lbl_right_padding">本日读取ODSB数据记录数</td>
                <td width="40%" class="data_input"  id="_cell_importTodayCnt"></td>
            </tr>
        </table>
        <br>
    </fieldset>
</div>
<div id="readResultInfo" class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px;margin-top:0px">

        <legend>ODSB数据读取参考信息</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="5" width="50%">

            <tr>
                <td width="40%" class="lbl_right_padding">ODSB读取数据参考信息</td>
                <td width="60%" class="data_input"  id="_read_odsb_process"></td>
            </tr>
        </table>
        <br>
    </fieldset>
</div>
</body>
</html>
