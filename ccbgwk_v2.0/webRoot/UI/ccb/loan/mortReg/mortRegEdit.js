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
        } else {
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
    if (operation == "edit") {
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
    if (operation == "add") {
        var mortID = "";
        retxml = createExecuteform(editForm, "insert", "mort01", "add");
        //alert(analyzeReturnXML(retxml));
        if (analyzeReturnXML(retxml) != "false") {
            var dom = createDomDocument();
            dom.loadXML(retxml);
            var fieldList = dom.getElementsByTagName("record")[0];
            for (var i = 0; i < fieldList.childNodes.length; i++) {
                if (fieldList.childNodes[i].nodeType == 1) {
                    oneRecord = fieldList.childNodes[i];
                    attrName = oneRecord.getAttribute("name");
                    if (attrName == "mortID") {
                        mortID = oneRecord.getAttribute("value");
                        window.returnValue = "1";
                        alert(MSG_SUCCESS + "\r\n��Ѻ��ţ�" + mortID + "�����¼��");
                        window.close();
                    }
                }
            }
        }
    } else if (operation = "edit") {
        retxml = createExecuteform(editForm, "update", "mort01", "edit");
        if (analyzeReturnXML(retxml) + "" == "true") {
            window.returnValue = "1";
            window.close();
        }
    }
}
