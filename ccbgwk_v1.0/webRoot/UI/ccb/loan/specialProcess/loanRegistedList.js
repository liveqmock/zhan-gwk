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
    divfd_loanRegistedTab.style.height = document.body.clientHeight - 180;
    loanRegistedTab.fdwidth = "100%";
    initDBGrid("loanRegistedTab");
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
 * ��ϵͳ��Ϊ1��ʱ��ϵͳ������0��ʱ��δ����
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

    if (trimStr(document.getElementById("mortecentercd").value) != "") {
        whereStr += " and b.mortecentercd ='" + trimStr(document.getElementById("mortecentercd").value) + "' ";
    }
    if (trimStr(document.getElementById("bankid").value) != "") {
        whereStr += " and a.bankid in(select deptid from ptdept start with deptid='"+document.getElementById("bankid").value+"' connect by prior deptid=parentdeptid)";
    }

    whereStr += " order by b.mortecentercd, a.bankid, b.mortid ";
    document.all["loanRegistedTab"].whereStr = whereStr;
    /*
     * 
     * whereStr += " order by b.mortdate desc,a.cust_py asc";
     * 
     * document.all["loanRegistedTab"].whereStr = whereStr;
     */
    document.all["loanRegistedTab"].RecordCount = "0";
    document.all["loanRegistedTab"].AbsolutePage = "1";
    // Table_Refresh in dbgrid.js pack
    Table_Refresh("loanRegistedTab", false, body_resize);
}

/**
 * �쿴��Ѻ��ϸ����
 * 
 * @param mortid����Ѻ���
 * @param doType:select
 *            ��������
 */
function loanRegistedTab_query_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:600px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["loanRegistedTab"];
    var trobj = tab.rows[tab.activeIndex];
    // ��Ѻ���
    var mortID = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        mortID = tmp[5];

    }
    var arg = new Object();
    arg.doType = "select";
    dialog("/UI/ccb/loan/mortReg/mortgageEdit.jsp?mortID=" + mortID + "&doType=select", arg, sfeature);
}

/**
 * ˫�����񵯳���ϸ��Ϣ�鿴���� ���ò鿴����
 * 
 */
function loanRegistedTab_TRDbclick() {
    loanRegistedTab_query_click();
}

/**
 * ���ӵ�Ѻ��Ϣ
 * 
 * @param doType:��������
 *            ���ӣ�add��
 * @param loanid:�����������
 */
function loanRegistedTab_appendRecod_click() {

    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:600px; dialogheight:260px;center:yes;help:no;resizable:yes;scroll:no;status:no";
    var tab = document.all["loanRegistedTab"];
    var trobj = tab.rows[tab.activeIndex];

    var loanID = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        loanID = tmp[2];
    }
    var arg = new Object();
    // �������ͣ�add
    arg.doType = "add";
    // �����������

    arg.loanID = loanID;
    var ret = dialog("/UI/ccb/loan/mortReg/mortRegEdit.jsp?loanID=" + loanID + "&doType=add", arg, sfeature);

    if (ret == "1") {
        document.getElementById("loanRegistedTab").RecordCount = "0";
        Table_Refresh("loanRegistedTab", false, body_resize);
    }
}

/**
 * �༭��Ѻ��Ϣ
 * 
 * @param doType:��������
 *            �޸� edit
 * @param mortid:��Ѻ���
 */
function loanRegistedTab_editRecord_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }
    var sfeature = "dialogwidth:800px; dialogheight:600px;center:yes;help:no;resizable:yes;scroll:no;status:no";
    var tab = document.all["loanRegistedTab"];
    var trobj = tab.rows[tab.activeIndex];

    var mortID = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        mortID = tmp[5];
    }
    var arg = new Object();
    // �������ͣ�edit
    arg.doType = "edit";
    // ��Ѻ���
    arg.mortID = mortID;
    var ret = dialog("/UI/ccb/loan/mortReg/mortgageEdit.jsp?mortID=" + mortID + "&doType=edit", arg, sfeature);

    if (ret == "1") {
        document.getElementById("loanRegistedTab").RecordCount = "0";
        Table_Refresh("loanRegistedTab", false, body_resize);
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
function loanRegistedTab_deleteRecord_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var tab = document.all["loanRegistedTab"];
    var trobj = tab.rows[tab.activeIndex];

    var mortID = "";
    var loanID = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        loanID = tmp[2];
        mortID = tmp[5];
    }

    // if (confirm("ȷ��ɾ������������Ϣ��\r\n�������ݣ�" + keepCont)) {
    if (confirm(MSG_DELETE_CONFRIM)) {
        // ���浽���ر������ύ��̨
        document.all.mortID.value = mortID;
        document.all.loanID.value = loanID;
        // ��Ѻ����ͨ��
        document.getElementById("busiNode").value = BUSINODE_120;
        var retxml = createExecuteform(queryForm, "delete", "mort01", "delete")
        if (analyzeReturnXML(retxml) != "false") {
            alert(MSG_DEL_SUCCESS);
            document.getElementById("loanRegistedTab").RecordCount = "0";
            Table_Refresh("loanRegistedTab", false, body_resize);
        }
    }
}

/**
 * �쿴������ϸ����
 * 
 * @param loanID�������������
 * @param doType:select
 *            ��������
 */
function loanRegistedTab_loanQuery_click() {

    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:520px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["loanRegistedTab"];
    var trobj = tab.rows[tab.activeIndex];
    var nbxh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        nbxh = tmp[9];
        if (nbxh == "") {
            alert(MSG_LOANNULL);
            return;
        }
    }
    var arg = new Object();
    arg.doType = "select";
    dialog("/UI/ccb/loan/loanMgr/loanEdit.jsp?nbxh=" + nbxh + "&doType=select", arg, sfeature);
}