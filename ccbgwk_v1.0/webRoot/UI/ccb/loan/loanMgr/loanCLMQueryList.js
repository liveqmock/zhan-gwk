/*******************************************************************************
 * 
 * �� �ã� �ͻ�������������ѯ
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
    loanTab.fdwidth = "100%";
    initDBGrid("loanTab");
    reSelect();
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
    // ����
    if (trimStr(document.getElementById("BANKID").value) != "") {
        whereStr += " and a.BANKID ='" + trimStr(document.getElementById("BANKID").value) + "'";
    }
    // �ͻ�����
    if (trimStr(document.getElementById("CUSTMGR_ID").value) != "") {
        whereStr += " and a.CUSTMGR_ID ='" + trimStr(document.getElementById("CUSTMGR_ID").value) + "'";
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
 * ���ݾ���������������Ŀ:�ͻ�����ID
 * 
 * @return
 */
function reSelect() {
    refresh_select("CUSTMGR_ID", "select OPERID as value ,OPERNAME as text  from ptoper where" + " deptid='"
                    + document.getElementById("BANKID").value + "'");
    var _cell = document.getElementById("CUSTMGR_ID");
    // ����Ĭ�Ͽ�ֵ
    var _option = document.createElement("option");
    _option.value = "";
    _option.text = "";
    _cell.appendChild(_option);
    _cell.value = "";

}
