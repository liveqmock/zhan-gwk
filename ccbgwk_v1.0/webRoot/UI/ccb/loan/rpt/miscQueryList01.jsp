<!--
/*********************************************************************
* ��������: ��ϲ�ѯͳ��һ ǩԼ�ſ�δ��֤ԭ�򱨱�һ �������С���������������Ŀ���ơ�δ���Ѻԭ��ͳ��
* �� ��: leonwoo
* ��������: 2010/01/16
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
    <script language="javascript" src="miscQueryList01.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/pub.js"></script>
    <script language="javascript" src="/UI/support/common.js"></script>
    <script language="javascript" src="/UI/support/DataWindow.js"></script>

    <LINK href="/css/newccb.css" type="text/css" rel="stylesheet">

</head>
<body onload="formInit()" bgcolor="#ffffff" class="Bodydefault">
<%--     <div class="title">
       <br>
       �������С���������������Ŀ���ơ�δ���Ѻԭ��ͳ��:<br>
       </div>--%>
<fieldset style="padding:40px 25px 0px 25px;margin:0px 20px 0px 20px">
    <legend>��ѯ����</legend>
    <br>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <form id="queryForm" name="queryForm">
            <!-- ��ϲ�ѯͳ������һ -->
            <input type="hidden" value="miscRpt01" id="rptType" name="rptType"/>
            <tr>
                <td width="25%" nowrap="nowrap" class="lbl_right_padding">��Ѻ��������</td>
                <td width="20%" nowrap="nowrap" class="data_input"><input type="text" id="MORTEXPIREDATE"
                                                                          name="MORTEXPIREDATE" onClick="WdatePicker()"
                                                                          fieldType="date" size="20"></td>
                <td width="5%" nowrap="nowrap" class="lbl_right_padding">��</td>
                <td width="50%" nowrap="nowrap" class="data_input"><input type="text" id="MORTEXPIREDATE2"
                                                                          name="MORTEXPIREDATE2" onClick="WdatePicker()"
                                                                          fieldType="date" size="20"></td>
            </tr>
            <tr>
                <td colspan="4" nowrap="nowrap" align="center" style="padding:20px">
                    <input name="expExcel" class="buttonGrooveDisableExcel" type="button"
                           onClick="loanTab_expExcel_click()" value="����excel">
                    <input type="reset" value="����" class="buttonGrooveDisable">
                </td>
            </tr>
        </form>
    </table>
</fieldset>

<br/>
<br/>
<br/>

<div class="help-window">
    <DIV class=formSeparator>
        <H2>����˵��</H2>
    </DIV>
    <div class="help-info">
        <ul>
            <li>��ʵ�ְ������С�������Ŀ��δ���׵�Ѻԭ��ͳ��ĳһʱ�����ǩԼ�ſ��ѷſ�δ��֤���嵥.</li>
            <li>ĳһʱ�����ָ�����Ѻ���̵�ʱ��Σ�����PMISϵͳ�еĿ��������ڼ�.</li>
            <li>ͳ��������ǩԼ�ſ�����ǩԼ�ſ�����.</li>
            <li>�������Ѻ������ʼ���ڣ�ϵͳĬ��Ϊͳ�Ƶ�ǰ���ݿ���ȫ��������������Ϣ����.</li>
            <li>ͳ�Ʊ��Ծ�������Ļ����������.</li>
        </ul>
    </div>
</div>

</body>
</html>
