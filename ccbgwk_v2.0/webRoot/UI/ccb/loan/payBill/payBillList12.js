var dw_form;

/**
 * ��ʼ��
 */
function payBillInit() {
  body_init(queryForm, "expExcel");
  // ��ʼ�����ݴ��ڣ�У���ʱ����
  dw_form = new DataWindow(document.getElementById("queryForm"), "form");
}


function loanTab_expExcel_click() {

  // ����ϵͳ�����
  if (getSysLockStatus() == "1") {
    alert(MSG_SYSLOCK);
    return;
  }
  if (dw_form.validate() != null)
    return;

  document.getElementById("queryForm").target = "_blank";
  document.getElementById("queryForm").action = "payBillReport12.jsp";
  document.getElementById("queryForm").submit();
}