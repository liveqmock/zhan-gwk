var dbOperateType = "";
var perID="";

var dw_column;
/**
 * ��ʼ��form�����ݣ���������ȡ����ϸ��Ϣ��������ѯ��ɾ�����޸�
 */
function formInit() {
    var arg = window.dialogArguments;
    if (arg) {
        operation = arg.doType;
        if (operation != "add") {
            load_form();
            // �ͻ������ʼ��
            operReSelectCustMgr();
            document.getElementById("deptcode").value = document.getElementById("hhidDeptCode").value;
        }
        // ֻ������£�ҳ�����пռ��ֹ�޸�
        if (operation == "select" || operation == "delete") {
            readFun(document.getElementById("editForm"));
        }
    }
    // ��ʼ�����ݴ��ڣ�У���ʱ����
    dw_column = new DataWindow(document.getElementById("editForm"), "form");
    //��ʼ��ʱ���Ѵ����߸��³����ڵ�½��
    document.getElementById("createcode").value = arg.operid;
}
/**
 * ��������������ȡһ��Ԥ�㵥λ
 */
function getSuperDeptCode(){
    if (document.getElementById("areacode").value == ""){
        alert("������������Ϊ�գ�");
        return;
    }
    var strSql = "select code as value , name as text from ls_bdgagency t where levelno=1 and areacode="+ document.getElementById("areacode").value;
    refresh_select("superdeptcode", strSql );

}

/**
 * ���ݾ���������
  */
function reSelect() {
    if (document.getElementById("superdeptcode").value == ""){
        alert("Ԥ�㵥λ����Ϊ�գ�");
        return;
    }
    operReSelectCustMgr();
}
/**
 * ���ݾ���������������Ŀ:�ͻ�����ID
 */
function operReSelectCustMgr() {
    var strSql = "select code as value , name as text from ls_bdgagency t  where supercode="+ document.getElementById("superdeptcode").value;
    strSql = strSql + " and lengthb(t.code) <> 3 and areacode="+document.getElementById("areacode").value;
    refresh_select("deptcode", strSql );
}
/*
* ���水ť
* */
function saveClick()
{
    //��������Ϊ��
    if (document.getElementById("perName").value == ""){
        alert("��������Ϊ�գ�");
        document.getElementById("perName").focus();
        return;
    }
    //���֤ID����Ϊ��
    if (document.getElementById("perId").value == ""){
        alert("���֤ID����Ϊ�գ�");
        document.getElementById("perId").focus();
        return;
    }
    //Ԥ�㵥λ����Ϊ��
    if (document.getElementById("superdeptcode").value == "" || document.getElementById("deptcode").value == ""){
        alert("Ԥ�㵥λ����Ϊ�գ�");
        //document.getElementById("perId").focus();
        return;
    }
    //������������Ϊ��
    if (document.getElementById("areacode").value == ""){
        alert("������������Ϊ�գ�");
        //document.getElementById("perId").focus();
        return;
    }
    //����
    var retxml = "";
    if (operation == "add") {
        retxml = createExecuteform(editForm, "insert", "pe001", "add");
    } else if (operation == "edit") {
        retxml = createExecuteform(editForm, "update", "pe001", "edit");
    }
    if (analyzeReturnXML(retxml) + "" == "true") {
        window.returnValue = "1";
        window.close();
    }
}
function checkAreaCode(){
    //������������Ϊ��
    if (document.getElementById("areacode").value == ""){
        alert("����ѡ������������");
        document.getElementById("areacode").focus();
        return;
    }
}