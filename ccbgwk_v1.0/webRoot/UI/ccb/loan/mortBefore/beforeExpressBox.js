/*******************************************************************************
 * 
 * �ļ����� ��Ѻ��ϸ����
 * 
 * �� �ã�
 * 
 * �� �ߣ� leonwoo
 * 
 * ʱ �䣺 2010-01-16
 * 
 * �� Ȩ�� leonwoo
 * 
 ******************************************************************************/
var dw_column;
/**
 * ��ʼ��form�����ݣ���������ȡ����ϸ��Ϣ��������ѯ��ɾ�����޸�
 */
function formInit() {

  // ��ʼ�����ݴ��ڣ�У���ʱ����
  dw_column = new DataWindow(document.getElementById("editForm"), "form");
  // ����Ĭ�Ͻ��㣻���
  document.getElementById("BOXID").focus();
}

/**
 * <p>
 * ���溯�����������ӡ��޸Ķ����øú���
 * <p>
 * createExecuteform �����ֱ�Ϊ
 * <p>
 * ��editForm :�ύ��form����
 * <p>
 * ��insert ���������ͣ�����Ϊinsert��update��delete֮һ��
 * <p>
 * ��mort01 ���Ựid����̨ҵ���߼����
 * <p>
 * ��add: : ��̨ҵ�����ʵ�ʶ�Ӧ��������
 * 
 * @param doType����������
 * 
 */
function saveClick() {

  var doType = document.all.doType.value;
  if (dw_column.validate() != null)
    return;
  var retxml = "";
  var arg = new Object();
  if (operation = "edit") {
    arg.boxid = document.getElementById("BOXID").value;
    arg.expressNote = document.getElementById("EXPRESSNOTE").value;
    window.returnValue = arg;
    window.close();
  }
}
