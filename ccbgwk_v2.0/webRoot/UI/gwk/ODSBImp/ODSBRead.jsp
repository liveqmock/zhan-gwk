<!--
/*********************************************************************
* ��������: Odsb ���ݶ���
* ��������: 2010/06/16
* �� �� ��:
* �޸�����:
* �� Ȩ: ��˾
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
        <legend>ODSB��ȡ</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <form id="queryForm" name="queryForm">
                <!-- ����Ϊƽ̨�������֮�� -->
                <input type="hidden" id="operType" name="operType" value="readFromODSB"/>
                <input type="hidden" id="newLsh" name="newLsh" value=""/>
                <tr height="20">

                    <%--<td width="70%" class="data_input">��ODSB���ȡ����</td>--%>
                    <td align="center" nowrap="nowrap">
                        <input type="button" id="button" onClick="readFromODSB()" value="ODSB�������ݶ�ȡ">
                    </td>
                </tr>
            </form>
        </table>
        <br>
    </fieldset>
</div>
<div id="divResultInfo" class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px;margin-top:0px">

        <legend>ODSB���ݶ�ȡ���</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="5" width="50%">
            <tr>
                <td width="60%" class="lbl_right_padding">���ζ�ȡODSB���ݼ�¼��</td>
                <td width="40%" class="data_input"  id="_cell_importCnt"></td>
            </tr>
            <tr>
                <td width="60%" class="lbl_right_padding">���ն�ȡODSB���ݼ�¼��</td>
                <td width="40%" class="data_input"  id="_cell_importTodayCnt"></td>
            </tr>
        </table>
        <br>
    </fieldset>
</div>
<div id="readResultInfo" class="queryPanalDiv">
    <fieldset style="padding-top:30px;padding-bottom:0px;margin-top:0px">

        <legend>ODSB���ݶ�ȡ�ο���Ϣ</legend>
        <br>
        <table border="0" cellspacing="0" cellpadding="5" width="50%">

            <tr>
                <td width="40%" class="lbl_right_padding">ODSB��ȡ���ݲο���Ϣ</td>
                <td width="60%" class="data_input"  id="_read_odsb_process"></td>
            </tr>
        </table>
        <br>
    </fieldset>
</div>
</body>
</html>
