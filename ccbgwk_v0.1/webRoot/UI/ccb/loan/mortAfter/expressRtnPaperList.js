var SQLStr;
var gWhereStr = "";
var dw_form;
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

    loanRegistedTab.actionname = "mort01";
    loanRegistedTab.delmethodname = "batchEdit";
    initDBGrid("loanRegistedTab");
    // ��ʼ��ҳ�潹��
    body_init(queryForm, "cbRetrieve");
    document.getElementById("cust_name").focus();
    document.getElementById("cust_name").select();
    // �������ݴ���У��ʹ��
    dw_form = new DataWindow(document.getElementById("queryForm"), "form");
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
     * whereStr += " order by a.cust_py asc";
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
        mortID = tmp[6];
    }
    var arg = new Object();
    arg.doType = "select";
    dialog("/UI/ccb/loan/mortReg/mortgageEdit.jsp?mortID=" + mortID + "&doType=select", arg, sfeature);
}

/**
 * ˫����񵯳���ϸ��Ϣ�鿴���� ���ò鿴����
 * 
 */
function loanRegistedTab_TRDbclick() {
    loanRegistedTab_query_click();
}

/**
 * �༭��Ѻ���׺� ��ݻ�֤�Ǽ�
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
    var sfeature = "dialogwidth:600px; dialogheight:350px;center:yes;help:no;resizable:yes;scroll:no;status:no";
    var tab = document.all["loanRegistedTab"];
    var trobj = tab.rows[tab.activeIndex];

    var mortID = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        mortID = tmp[6];
    }
    var arg = new Object();
    // �������ͣ�edit
    arg.doType = "edit";
    // ��Ѻ���
    arg.mortID = mortID;
    var ret = dialog("expressRtnPaperEdit.jsp?mortID=" + mortID + "&doType=edit", arg, sfeature);

    if (ret == "1") {
        document.getElementById("loanRegistedTab").RecordCount = "0";
        Table_Refresh("loanRegistedTab", false, body_resize);
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
        nbxh = tmp[0];
    }
    var arg = new Object();
    arg.doType = "select";
    dialog("/UI/ccb/loan/loanMgr/loanEdit.jsp?nbxh=" + nbxh + "&doType=select", arg, sfeature);
}

/**
 * ��������,��̨��Ӧaction�뷽�����ڸ�ҳ���ʼ����ʱ��ָ��
 * 
 * @return
 */
function loanRegistedTab_batchEdit_click() {

    // ѡȡ���ݱ�־
    var checked = false;
    var tab = document.all.loanRegistedTab;
    for ( var i = 0; i < tab.rows.length; i++) {
        if (tab.rows[i].cells[0].children[0] != undefined && tab.rows[i].cells[0].children[0].checked) {
            tab.rows[i].operate = "delete";
            checked = true;
            // ---
            // ����TD��recversion���ݵ���̨�Ա㲢�����ƣ�
            var _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "recversion");
            // _cell.setAttribute("style","display:none");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            _cell.setAttribute("oldvalue", tab.rows[i].ValueStr.split(";")[10]);
            tab.rows[i].appendChild(_cell);
            // ---loanid
            _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "loanid");
            // _cell.setAttribute("style","display:none");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            _cell.setAttribute("oldvalue", tab.rows[i].ValueStr.split(";")[11]);
            tab.rows[i].appendChild(_cell);

            // ��ݻ�֤���ڣ���ǰ����
            _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "EXPRESSRTNDATE");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            var date = new Date();
            _cell.setAttribute("oldvalue", getDateString(date));
            tab.rows[i].appendChild(_cell);
        } else {
            tab.rows[i].operate = "";
        }
    }
    if (!checked) {
        alert(MSG_NORECORD);
        return;
    }
    // ---�򿪹�����봰��---
    // ��Ų�Ϊ�գ��ѹ����ӵ�grid��
    // ���õ�Ѻ����״̬ �ѻ�֤

    if (confirm(MSG_BATCH_EXPRESS)) {
        for ( var i = 0; i < tab.rows.length; i++) {
            if (tab.rows[i].cells[0].children[0] != undefined && tab.rows[i].cells[0].children[0].checked) {
                // ---
                // ����TD��boxid���ݵ���̨�Ա㲢�����ƣ�
                var _cell = document.createElement("td");
                _cell.setAttribute("fieldname", "MORTSTATUS");
                _cell.style.display = "none";
                _cell.setAttribute("fieldtype", "text");
                // �ѿ�ݻ�֤ 21
                var MORT_FLOW_RETURN = "21";
                _cell.setAttribute("oldvalue", MORT_FLOW_RETURN);
                tab.rows[i].appendChild(_cell);
                // ���̽ڵ�
                _cell = document.createElement("td");
                _cell.setAttribute("fieldname", "busiNode");
                _cell.style.display = "none";
                _cell.setAttribute("fieldtype", "text");
                var MORT_FLOW_RETURN = "21";
                _cell.setAttribute("oldvalue", BUSINODE_070);
                tab.rows[i].appendChild(_cell);
            } else {
                tab.rows[i].operate = "";
            }
        }
        // ---
        // �ύ��̨���������޸�
        var retxml = postGridRecord(tab);
        // analyzeReturnXML in dbutil.js pack
        if (retxml + "" == "true") {
            window.returnValue = "1";
        }
    }
}

/**
 * report Ŀǰ�ñ������Ѻ����ǰ��ݱ���һ�£�
 * 
 * @param doType:select
 *            ��������
 */
function loanRegistedTab_expExcel_click() {

    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }
    if (dw_form.validate() != null)
        return;

    document.getElementById("expressType").value = "mortAfter";
    document.getElementById("queryForm").target = "_blank";
    // document.getElementById("queryForm").action =
    // "/UI/ccb/loan/mortBefore/beforeExpressRpt.jsp?expressType=mortAfter";
    document.getElementById("queryForm").action = "/UI/ccb/loan/mortBefore/beforeExpressRpt.jsp";
    document.getElementById("queryForm").submit();
}