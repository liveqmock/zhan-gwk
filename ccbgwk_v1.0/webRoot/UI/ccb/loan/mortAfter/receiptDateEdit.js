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
	var arg = window.dialogArguments;

	if (arg) {
		operation = arg.doType;
		if (operation != "add") {
			load_form();
		}

		// �������Ĭ��Ϊϵͳʱ��
		if (document.getElementById("RECEIPTDATE").value == "") {
			var date = new Date();
			document.getElementById("RECEIPTDATE").value = getDateString(date);
			// document.getElementById("RECEIPTDATE").disabled = "true";
		}

		// ֻ������£�ҳ�����пؼ���ֹ�޸�
		if (operation == "select" || operation == "delete") {
			readFun(document.getElementById("editForm"));
		}

	}
	// ��ʼ�����ݴ��ڣ�У���ʱ����
	dw_column = new DataWindow(document.getElementById("editForm"), "form");
	if (operation != "select") {
		document.getElementById("RECEIPTID").focus();
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
 * ��insert ���������ͣ�����Ϊinsert��update��delete֮һ����̨����������ͣ�
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
	if (operation == "add") {
		retxml = createExecuteform(editForm, "insert", "mort01", "add");
	} else if (operation = "edit") {
        // ��ȡ�û�ִ 22
        var MORT_FLOW_RETURNNOTE = "22";
        document.getElementById("MORTSTATUS").value = MORT_FLOW_RETURNNOTE;

		document.getElementById("busiNode").value = BUSINODE_060;
		retxml = createExecuteform(editForm, "update", "mort01", "edit");
	}

	if (analyzeReturnXML(retxml) + "" == "true") {
		window.returnValue = "1";
		window.close();
	}
}
