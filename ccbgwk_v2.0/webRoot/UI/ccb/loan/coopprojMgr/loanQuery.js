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
    divfd_loanRegistedTab.style.height = document.body.clientHeight - 200;
    loanRegistedTab.fdwidth = "1400px";
    initDBGrid("loanRegistedTab");
    // ��ʼ��ҳ�潹��
    body_init(queryForm, "cbRetrieve");
    //    document.getElementById("cust_name").focus();
    //    document.getElementById("cust_name").select();
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
 * @param ��form���ֻ���ID
 */

function cbRetrieve_Click(formname) {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    //    if (checkForm(queryForm) == "false")
    //        return;
    var whereStr = "";

/*
    if (trimStr(document.getElementById("cust_name").value) != "") {
        whereStr += " and ((a.cust_name like'" + trimStr(document.getElementById("cust_name").value) + "%')";
        whereStr += " or (a.cust_py  like'" + trimStr(document.getElementById("cust_name").value) + "%'))";
    }

    if (trimStr(document.getElementById("mortecentercd").value) != "") {
        whereStr += " and b.mortecentercd ='" + trimStr(document.getElementById("mortecentercd").value) + "' ";
    }
*/
    if (trimStr(document.getElementById("bankid").value) != "") {
        whereStr += " and a.bankid in(select deptid from ptdept start with deptid='"+document.getElementById("bankid").value+"' connect by prior deptid=parentdeptid)";
    }

    if (trimStr(document.all["releasecondcd"].value) != "")
        whereStr += " and ( b.releasecondcd like '%" + document.getElementById("releasecondcd").value + "%')";

    if (trimStr(document.all["ln_typ"].value) != "")
        whereStr += " and ( a.ln_typ like '%" + document.getElementById("ln_typ").value + "%')";

    var assuamt = trimStr(document.all["booltype"].value);
    if (assuamt != "") {
        if (assuamt == "1") { // Ϊ��
            whereStr += " and ( c.assuamtpercent = 0  or c.assuamtpercent is null ) ";
        } else {
            whereStr += " and ( c.assuamtpercent != 0 ) ";
        }
    }

    whereStr += " order by a.bankid, c.proj_no, a.loanid  ";
    document.all["loanRegistedTab"].whereStr = whereStr;
    /*

     whereStr += " order by b.mortdate desc,a.cust_py asc";

     document.all["loanRegistedTab"].whereStr = whereStr;
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
 *          ��������
 */
function loanRegistedTab_query_click() {
    //����ϵͳ�����
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
        mortID = tmp[10];

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
 * �쿴������ϸ����
 *
 * @param loanID�������������
 * @param doType:select
 *          ��������
 */
function loanRegistedTab_loanQuery_click() {

    //����ϵͳ�����
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
        nbxh = tmp[12];
        if (nbxh == "") {
            alert(MSG_LOANNULL);
            return;
        }
    }
    var arg = new Object();
    arg.doType = "select";
    dialog("/UI/ccb/loan/loanMgr/loanEdit.jsp?nbxh=" + nbxh + "&doType=select", arg, sfeature);
}


/*
�鿴������Ŀ
 */
function loanRegistedTab_projQuery_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:620px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["loanRegistedTab"];
    var trobj = tab.rows[tab.activeIndex];
    var proj_nbxh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        proj_nbxh = tmp[13];
        // alert(proj_no);

    }
    var arg = new Object();
    arg.doType = "select";
    dialog("/UI/ccb/loan/coopprojMgr/coopprojEdit.jsp?proj_nbxh=" + proj_nbxh + "&doType=select", arg, sfeature);
}
