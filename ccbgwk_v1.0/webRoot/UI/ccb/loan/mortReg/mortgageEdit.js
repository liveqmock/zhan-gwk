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
var operation;
/**
 * ��ʼ��form�����ݣ���������ȡ����ϸ��Ϣ��������ѯ��ɾ�����޸�
 */
function formInit() {
  // ��ʼ���¼�
  var arg = window.dialogArguments;

  if (arg) {
    operation = arg.doType;
    if (operation != "add") {
      load_form();
    }else{
      // ��Ѻ�����Զ�����
      var date = new Date();
      document.getElementById("MORTDATE").value = getDateString(date);
      document.getElementById("MORTDATE").readOnly = "readOnly";
    }

    // ֻ������£�ҳ�����пռ��ֹ�޸�
    if (operation == "select" || operation == "delete") {
      readFun(document.getElementById("editForm"));
    }

  }
  // ��ʼ�����ݴ��ڣ�У���ʱ����
  dw_column = new DataWindow(document.getElementById("editForm"), "form");
  // ����Ĭ�Ͻ���
  if (operation != "select") {
    document.getElementById("MORTECENTERCD").focus();
  }
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
  // ��ˮ��־��־
  document.getElementById("busiNode").value = BUSINODE_120;
  if (operation == "add") {
    // ��Ѻ����״̬ 02
    document.getElementById("MORTSTATUS").value = "02";
    retxml = createExecuteform(editForm, "insert", "mort01", "add");
  } else if (operation = "edit") {
    
    retxml = createExecuteform(editForm, "update", "mort01", "edit");
  }

  if (analyzeReturnXML(retxml) + "" == "true") {
    window.returnValue = "1";
    window.close();
  }
}
