/*******************************************************************************
 * 
 * �� �ã� �ͻ������������
 * 
 * �� �ߣ�
 * 
 * ʱ �䣺
 * 
 * �� Ȩ��
 * 
 ******************************************************************************/
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
    // �߶��Զ����ݴ��ڴ�С���е���
    divfd_loanTab.style.height = document.body.offsetHeight - 180;
    loanTab.actionname = "loan01";
    // loanTab.delmethodname = "confirmCLM";
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
 * ����
 * 
 * @param doType:��������
 *            �޸� edit
 * @param nbxh:�ڲ����
 */
function loanTab_confirmCLM_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var tab = document.all["loanTab"];
    var trobj = tab.rows[tab.activeIndex];

    var nbxh = "";
    var recversion = "";
    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        nbxh = tmp[0];
        recversion = tmp[9];
    }

    if (confirm(MSG_CLM_CONFIRM)) {
        // ���浽���ر������ύ��̨
        document.all.nbxh.value = nbxh;
        // �汾��
        document.all.recversion.value = recversion;
        document.getElementById("busiNode").value = BUSINODE_140;
        var retxml = createExecuteform(queryForm, "update", "loan01", "confirmCLM")
        if (analyzeReturnXML(retxml) != "false") {
            document.getElementById("loanTab").RecordCount = "0";
            Table_Refresh("loanTab", false, body_resize);
        }
    }
}

/**
 * �˻�
 * 
 * @param doType:��������
 *            �޸� edit
 * @param nbxh:�ڲ����
 */
function loanTab_cancelCLM_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var tab = document.all["loanTab"];
    var trobj = tab.rows[tab.activeIndex];

    var nbxh = "";
    var recversion = "";
    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        nbxh = tmp[0];
        recversion = tmp[9];
    }

    if (confirm(MSG_CLM_CANCEL)) {
        // ���浽���ر������ύ��̨
        document.all.nbxh.value = nbxh;
        // �汾��
        document.all.recversion.value = recversion;
        document.getElementById("busiNode").value = BUSINODE_140;
        var retxml = createExecuteform(queryForm, "update", "loan01", "cancelCLM")
        if (analyzeReturnXML(retxml) != "false") {
            document.getElementById("loanTab").RecordCount = "0";
            Table_Refresh("loanTab", false, body_resize);
        }
    }
}

/**
 * ��������,��̨��Ӧaction�뷽�����ڸ�ҳ���ʼ����ʱ��ָ��
 * 
 * @return
 */
function loanTab_batchCLM_click() {
    // ��̬�޸ķ�����
    // ѡ�����ݱ�־λ
    var checked = false;
    loanTab.delmethodname = "confirmCLM";
    var tab = document.all.loanTab;
    for ( var i = 0; i < tab.rows.length; i++) {
        if (tab.rows[i].cells[0].children[0] != undefined && tab.rows[i].cells[0].children[0].checked) {
            tab.rows[i].operate = "delete";
            checked = true;
            // ---��Ӱ汾��
            // ����TD��recversion���ݵ���̨�Ա㲢�����ƣ�
            var _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "recversion");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            _cell.setAttribute("oldvalue", tab.rows[i].ValueStr.split(";")[9]);
            tab.rows[i].appendChild(_cell);
            // �ڲ����
            _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "nbxh");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            _cell.setAttribute("oldvalue", tab.rows[i].ValueStr.split(";")[0]);
            tab.rows[i].appendChild(_cell);
            // ��������
            _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "busiNode");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            _cell.setAttribute("oldvalue", BUSINODE_140);
            tab.rows[i].appendChild(_cell);
        } else {
            tab.rows[i].operate = "";
        }
    }
    if (!checked) {
        alert(MSG_NORECORD);
        return;
    }
    // ȷ����Ϣ��
    if (confirm(MSG_BATCH_CLM_CONFIRM)) {
        var retxml = postGridRecord(tab);
        // analyzeReturnXML in dbutil.js pack
        if (retxml + "" == "true") {
            window.returnValue = "1";
        }
    }
}

/**
 * �����˻�,��̨��Ӧaction�뷽�����ڸ�ҳ���ʼ����ʱ��ָ��
 * 
 * @return
 */
function loanTab_batchCancel_click() {
    // ��̬�޸ķ�����
    // ѡ�����ݱ�־λ
    var checked = false;
    loanTab.delmethodname = "cancelCLM";
    var tab = document.all.loanTab;
    for ( var i = 0; i < tab.rows.length; i++) {
        if (tab.rows[i].cells[0].children[0] != undefined && tab.rows[i].cells[0].children[0].checked) {
            tab.rows[i].operate = "delete";
            checked = true;

            // ---��Ӱ汾�š��ڲ����
            // ����TD��recversion���ݵ���̨�Ա㲢�����ƣ�
            var _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "recversion");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            _cell.setAttribute("oldvalue", tab.rows[i].ValueStr.split(";")[9]);
            tab.rows[i].appendChild(_cell);
            // �ڲ����
            _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "nbxh");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            _cell.setAttribute("oldvalue", tab.rows[i].ValueStr.split(";")[0]);
            tab.rows[i].appendChild(_cell);
            // ��������
            _cell = document.createElement("td");
            _cell.setAttribute("fieldname", "busiNode");
            _cell.style.display = "none";
            _cell.setAttribute("fieldtype", "text");
            _cell.setAttribute("oldvalue", BUSINODE_140);
            tab.rows[i].appendChild(_cell);
        } else {
            tab.rows[i].operate = "";
        }
    }
    if (!checked) {
        alert(MSG_NORECORD);
        return;
    }
    // ȷ����Ϣ��
    if (confirm(MSG_BATCH_CLM_CANCEL)) {
        var retxml = postGridRecord(tab);
        // analyzeReturnXML in dbutil.js pack
        if (retxml + "" == "true") {
            window.returnValue = "1";
        }
    }
}