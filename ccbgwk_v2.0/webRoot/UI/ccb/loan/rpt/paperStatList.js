var dw_form;

/**
 * ��ʼ��
 * 
 */
function formInit() {
  body_init(queryForm, "expExcel");
  // ��ʼ�����ݴ��ڣ�У���ʱ����
  dw_form = new DataWindow(document.getElementById("queryForm"), "form");
}

/**
 * report
 * 
 * @param ��Ѻ������
 * @param doType:select
 *          ��������
 */
function loanTab_expExcel_click() {

  // ����ϵͳ�����
  if (getSysLockStatus() == "1") {
    alert(MSG_SYSLOCK);
    return;  
  }
  if (dw_form.validate() != null)
    return;

  // window.open("miscReport01.jsp?MORTEXPIREDATE="+date+"&MORTEXPIREDATE2="+date2);
  document.getElementById("queryForm").target = "_blank";
  document.getElementById("queryForm").action = "paperStatReport.jsp";
  document.getElementById("queryForm").submit();
}
