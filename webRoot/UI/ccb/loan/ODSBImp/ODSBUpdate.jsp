<!--
/*********************************************************************
* ��������: Odsb ����ODSB���´����
* �� ��: leonwoo
* ��������: 2010/01/16
* �� �� ��:
* �޸�����:
* �� Ȩ: ��˾
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@page import="com.ccb.util.CcbLoanConst" %>
<%@include file="/global.jsp" %>
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<script language="javascript" src="ODSBUpdate.js"></script>
<script language="javascript" src="/UI/support/pub.js"></script>
<script language="javascript">     
    var checkSubmitFlg = false;     
    function checkSubmit() {     
      if (checkSubmitFlg == true) {     
         return false;     
      }     
      checkSubmitFlg = true;     
      return true;     
   }     
   document.ondblclick = function docondblclick() {     
    window.event.returnValue = false;     
   }     
   document.onclick = function doconclick() {     
       if (checkSubmitFlg) {     
         window.event.returnValue = false;     
       }     
   }     
</script>
</head>
<body bgcolor="#ffffff" class="Bodydefault">
<div class="queryPanalDiv">
  <fieldset style="padding:30px 10px 10px 10px;margin:0px 20px 0px 20px">
  <legend>ODSB���ݼ�鼰����</legend>
  <br/>
  <table border="0" cellspacing="0" cellpadding="0" width="100%">
    <form id="queryForm" name="queryForm" onsubmit="return checkSubmit();">
      <!-- ����Ϊƽ̨�������֮�� -->
      <input type="hidden" id="operType" name="operType" value="readFromODSB"/>
      <tr>
        <td align="center" nowrap="nowrap"><input type="button" id="odsbCheck" onClick="ODSBCheck()" value="��¼�����ݼ��">
          <input type="button" id="odsbUpdate" onClick="ODSBUpdate()" value="������Ϣ�Զ�����">
        </td>
      </tr>
    </form>
  </table>
  <br/>
  </fieldset>
<br/>
  </div>
<div style="display:none" id="divResultInfo" class="queryPanalDiv">
  <fieldset style="padding:30px 25px 10px 25px;margin:0px 20px 0px 20px">
  <legend>ODSB���ݼ�鼰��鵼����</legend>
      <br>
  <table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
      <td width="50%" class="lbl_right_padding">�������ݼ�¼��</td>
      <td width="50%" class="data_input" id="_cell_updateCnt"></td>
    </tr>
    <tr>
      <td width="50%" class="lbl_right_padding">�������ݼ�¼��</td>
      <td width="50%" class="data_input" id="_cell_addCnt"></td>
    </tr>
    <tr>
      <td width="50%" class="lbl_right_padding">���µ�Ѻ���������ݼ�¼��</td>
      <td width="50%" class="data_input" id="_cell_updateMortDateCnt"></td>
    </tr>
    <tr>
      <td width="50%" class="lbl_right_padding">�������ݴ������־���ݼ�¼��</td>
      <td width="50%" class="data_input" id="_cell_updateNeedCDCnt"></td>
    </tr>
  </table>
      <br>
  </fieldset>
</div>
</body>
</html>
