/*******************************************************************************
 * 
 * �ļ����� ������ϸ����
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
    var arg = window.dialogArguments;
    operReSelectCustMgr();
    if (arg) {
        operation = arg.doType;
        if (operation != "add") {
            load_form();
            // �ͻ������ʼ��
            operReSelectCustMgr();
            document.getElementById("CUSTMGR_ID").value = document.getElementById("custMgrID").value;
        }

        // ֻ������£�ҳ�����пռ��ֹ�޸�
        if (operation == "select" || operation == "delete") {
            readFun(document.getElementById("editForm"));
        }
    }
    // ��ʼ�����ݴ��ڣ�У���ʱ����
    dw_column = new DataWindow(document.getElementById("editForm"), "form");

}

/**
 * ���ݾ���������
 */
function reSelect() {
    operReSelectCustMgr();
}

/**
 * ���ݾ���������������Ŀ:������
 * 
 * @return
 */
function operReSelect() {
    refresh_select("OPERID", "select OPERID as value ,OPERNAME as text  from ptoper where" + " deptid='"
            + document.getElementById("BANKID").value + "'");
}

/**
 * ���ݾ���������������Ŀ:�ͻ�����ID
 * 
 * @return
 */
function operReSelectCustMgr() {
    refresh_select("CUSTMGR_ID", "select OPERID as value ,OPERNAME as text  from ptoper where" + " deptid='"
            + document.getElementById("BANKID").value + "'");
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
    if (document.getElementById("RELEASECONDCD").value == "03") {
        var waringMsgDate = "�ſʽΪǩԼ�ſ�򡾿������ڡ�����Ϊ�գ�";
        var waringMsgProj = "�ſʽΪǩԼ�ſ����Ŀ��š�����Ϊ�գ�";
        if (document.getElementById("CUST_OPEN_DT").value == "") {
            alert(waringMsgDate);
            document.getElementById("CUST_OPEN_DT").focus();
            return;
        }
        if (document.getElementById("PROJ_NO").value == "") {
            alert(waringMsgProj);
            document.getElementById("PROJ_NO").focus();
            return;
        }
    }

    if (dw_column.validate() != null)
        return;

    document.getElementById("busiNode").value = BUSINODE_150;
    var retxml = "";
    if (operation == "add") {
        retxml = createExecuteform(editForm, "insert", "loan01", "add");
    } else if (operation = "edit") {
        retxml = createExecuteform(editForm, "update", "loan01", "edit");
    }

    if (analyzeReturnXML(retxml) + "" == "true") {
        window.returnValue = "1";
        window.close();
    }
}
