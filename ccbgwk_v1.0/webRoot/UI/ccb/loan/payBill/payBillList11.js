var dw_form;

/**
 * ��ʼ��
 * 
 * @return
 */
function payBillInit() {
  body_init(queryForm, "expExcel");
  // ��ʼ�����ݴ��ڣ�У���ʱ����
  dw_form = new DataWindow(document.getElementById("queryForm"), "form");
}
/**
 * report
 * 
 * @param
 * @param doType:select
 *          ��������
 * @return ��
 */
function loanTab_expExcel_click() {

  // ����ϵͳ�����
  if (getSysLockStatus() == "1") {
    alert(MSG_SYSLOCK);
    return;
  }
  if (dw_form.validate() != null)
    return;

  document.getElementById("queryForm").target = "_blank";
  document.getElementById("queryForm").action = "payBillReport11.jsp";
  document.getElementById("queryForm").submit();
}