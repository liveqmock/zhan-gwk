var SQLStr;
var gWhereStr = "";

/**
 * ��ʼ��form����,
 * <p>
 * ����ʼ�����㶨λ�ڲ�ѯ������һ���ؼ���;
 * <p>
 * ��ÿ�β�ѯ��Ϻ󽹵��Զ���λ����һ���ؼ�����ȫѡ��
 * 
 */
function body_resize() {
    divfd_loanTab.style.height = document.body.clientHeight - 180;
    loanTab.fdwidth = "100%";
    initDBGrid("loanTab");
    // ��ʼ��ҳ�潹��
    body_init(queryForm, "cbRetrieve");
    document.getElementById("cust_name").focus();
    document.getElementById("cust_name").select();
}

/**
 * <p>
 * ������������������ɺ󽹵��Զ���λ�����������һ�������У� ����ȫѡ��;
 * <p>
 * ��ϵͳ��������������ĺ��ֻ���ƴ����ѯ ������ƴ����ѯ�ǡ�or���Ĺ�ϵ;
 * <p>
 * ��������ƴ����֧��ǰ��һ�¡����ģ����ѯ��
 * <p>
 * �����»س����Զ�֧�ֲ�ѯ��
 * </p>
 * 
 * @param ��form���ֻ���ID
 */

function cbRetrieve_Click(formname) {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    if (checkForm(queryForm) == "false")
        return;
    var whereStr = "";
    if (trimStr(document.getElementById("cust_name").value) != "") {
        whereStr += " and ((a.cust_name like'" + trimStr(document.getElementById("cust_name").value) + "%')";
        whereStr += " or (a.cust_py  like'" + trimStr(document.getElementById("cust_name").value) + "%'))";
    }
    whereStr += " order by a.CUST_OPEN_DT desc,APLY_DT desc,a.cust_py asc ";

    document.all["loanTab"].whereStr = whereStr;
    document.all["loanTab"].RecordCount = "0";
    document.all["loanTab"].AbsolutePage = "1";
    // Table_Refresh in dbgrid.js pack
    Table_Refresh("loanTab", false, body_resize);
}

/**
 * ��Ӵ�����Ϣ
 * 
 * @param doType:��������
 * @param loanid:�����������
 */
function loanTab_appendRecod_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:520px;center:yes;help:no;resizable:yes;scroll:no;status:no";
    var tab = document.all["loanTab"];
    var trobj = tab.rows[tab.activeIndex];

    var arg = new Object();
    // �������ͣ�add
    arg.doType = "add";

    var ret = dialog("loanEdit.jsp?doType=add", arg, sfeature);

    if (ret == "1") {
        document.getElementById("loanTab").RecordCount = "0";
        Table_Refresh("loanTab", false, body_resize);
    }
}

/**
 * �쿴������ϸ����
 * 
 * @param loanID�������������
 * @param doType:select
 *            ��������
 */
function loanTab_query_click() {

    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:520px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["loanTab"];
    var trobj = tab.rows[tab.activeIndex];
    var nbxh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        nbxh = tmp[0];

    }
    var arg = new Object();
    arg.doType = "select";
    dialog("loanEdit.jsp?nbxh=" + nbxh + "&doType=select", arg, sfeature);
}

/**
 * ˫����񵯳���ϸ��Ϣ�鿴���� ���ò鿴����
 * 
 */
function loanTab_TRDbclick() {
    loanTab_query_click();
}

/**
 * �༭������Ϣ
 * 
 * @param doType:��������
 *            �޸� edit
 * @param nbxh:�ڲ����
 */
function loanTab_editRecord_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }
    var sfeature = "dialogwidth:800px; dialogheight:520px;center:yes;help:no;resizable:yes;scroll:no;status:no";
    var tab = document.all["loanTab"];
    var trobj = tab.rows[tab.activeIndex];

    var nbxh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        nbxh = tmp[0];
    }
    var arg = new Object();
    // �������ͣ�edit
    arg.doType = "edit";
    // �ڲ����
    arg.nbxh = nbxh;
    var ret = dialog("loanEdit.jsp?nbxh=" + nbxh + "&doType=edit", arg, sfeature);

    if (ret == "1") {
        document.getElementById("loanTab").RecordCount = "0";
        Table_Refresh("loanTab", false, body_resize);
    }
}

/**
 * <p>
 * ɾ������
 * 
 * @param mortID:��Ѻ���
 * @param keepCont:��������
 * @param loanID�������������
 */
function loanTab_deleteRecord_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var tab = document.all["loanTab"];
    var trobj = tab.rows[tab.activeIndex];

    var nbxh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        nbxh = tmp[0];
    }

    if (confirm(MSG_DELETE_CONFRIM)) {
        // ���浽���ر������ύ��̨
        document.all.nbxh.value = nbxh;
        document.getElementById("busiNode").value = BUSINODE_150;
        var retxml = createExecuteform(queryForm, "delete", "loan01", "delete")
        if (analyzeReturnXML(retxml) != "false") {
            alert(MSG_DEL_SUCCESS);
            document.getElementById("loanTab").RecordCount = "0";
            Table_Refresh("loanTab", false, body_resize);
        }
    }
}
