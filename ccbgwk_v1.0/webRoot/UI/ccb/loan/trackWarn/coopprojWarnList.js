var SQLStr;
var gWhereStr = "";

/**
 * ��ʼ��form����,
 * <p>
 * ����ʼ�����㶨λ�ڲ�ѯ������һ���ؼ���;
 * <p>
 * ��ÿ�β�ѯ��Ϻ󽹵��Զ���λ����һ���ؼ�����ȫѡ��
 * 
 * @return
 */
function body_resize() {
    // var wheight = document.body.clientHeight - 300;
    divfd_coopprojTable.style.height = document.body.clientHeight - 200 + "px";
    coopprojTable.fdwidth = "1640px";
    initDBGrid("coopprojTable");
    // ��ʼ��ҳ�潹��
    // body_init(queryForm, "cbRetrieve");
    body_init(queryForm, "queryClick");
//    document.getElementById("proj_no").focus();

}

// //////��ѯ����
function queryClick() {
    var whereStr = "";

/*
    if (trimStr(document.all["proj_no"].value) != "")
        whereStr += " and ( proj_no like '%" + document.all.proj_no.value + "%')";
*/

    if (trimStr(document.all["bankid"].value) != "")
        whereStr += " and ( bankid like '%" + document.all.bankid.value + "%')";

    if (trimStr(document.all["releasecondcd"].value) != "")
        whereStr += " and ( releasecondcd like '%" + document.all.releasecondcd.value + "%')";

/*
    if (trimStr(document.all["corpname"].value) != "")
        whereStr += " and ( corpname like '%" + document.all.corpname.value + "%')";
*/

    /*
     * if (trimStr(document.all["corpid"].value) != "") whereStr += " and (
     * corpid like '%"+document.all.corpid.value+"%')";
     */
/*

    var maturityflag = trimStr(document.all["maturityflag"].value);
    if (maturityflag != "") {
        var date = new Date();
        if (maturityflag == "1") { // �ѵ���
            whereStr += " and ( assuenddate >= '" + getDateString(date) + "') ";
        } else {
            whereStr += " and ( assuenddate < '" + getDateString(date) + "') ";
        }
    }
*/

    if (trimStr(document.all["bankflag"].value) != "")
        whereStr += " and ( bankflag = '" + document.all.bankflag.value + "')";
/*

    if (trimStr(document.all["proj_name"].value) != "")
        whereStr += " and ( proj_name like '%" + document.all.proj_name.value + "%')";

*/
    var assuamt = trimStr(document.all["booltype"].value);
    if (assuamt != "") {
        if (assuamt == "1") { // Ϊ��
            whereStr += " and ( assuamtpercent = 0  or assuamtpercent is null ) ";
        } else {
            whereStr += " and ( assuamtpercent != 0 ) ";
        }
    }

    /*
     * if (trimStr(document.all["inputdate1"].value) != "") whereStr += " and (
     * inputdate1 >= '"+document.all.inputdate1.value+"')";
     * 
     * if (trimStr(document.all["inputdate2"].value) != "") whereStr += " and (
     * inputdate2 <= '"+document.all.inputdate2.value+"')";
     */

    if (whereStr != document.all["coopprojTable"].whereStr) {
        document.all["coopprojTable"].whereStr = whereStr + " order by proj_no ";
        document.all["coopprojTable"].RecordCount = "0";
        document.all["coopprojTable"].AbsolutePage = "1";

        // alert(whereStr);
        Table_Refresh("coopprojTable");
    }

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
 * @return
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
    whereStr += " order by a.cust_py asc";

    document.all["coopprojTable"].whereStr = whereStr;
    document.all["coopprojTable"].RecordCount = "0";
    document.all["coopprojTable"].AbsolutePage = "1";
    // Table_Refresh in dbgrid.js pack
    Table_Refresh("coopprojTable", false, body_resize);
}

function coopprojTable_query_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:620px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["coopprojTable"];
    var trobj = tab.rows[tab.activeIndex];
    var proj_nbxh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        proj_nbxh = tmp[0];
        // alert(proj_no);

    }
    var arg = new Object();
    arg.doType = "select";
    // dialog("/UI/ccb/loan/mortReg/mortgageEdit.jsp?mortID=" + mortID +
    // "&doType=select", arg, sfeature);
    dialog("/UI/ccb/loan/coopprojMgr/coopprojEdit.jsp?proj_nbxh=" + proj_nbxh + "&doType=select", arg, sfeature);
    // window.open("/UI/ccb/loan/coopprojMgr/coopprojEdit.jsp?proj_no=" +
    // proj_no + "&doType=select");
}

/**
 * ˫����񵯳���ϸ��Ϣ�鿴���� ���ò鿴����
 * 
 * @return
 */
function coopprojTable_TRDbclick() {
    coopprojTable_query_click();
}

/**
 * �����Ϣ
 * 
 * @param doType:��������
 *            ���ӣ�add��
 * @param loanid:�����������
 * @return
 */
function coopprojTable_appendRecod_click() {

    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    // var sfeature = "dialogwidth:800px;
    // dialogheight:460px;center:yes;help:no;resizable:yes;scroll:no;status:no";
    var sfeature = "dialogwidth:800px; dialogheight:620px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["coopprojTable"];
    var trobj = tab.rows[tab.activeIndex];

    var proj_nbxh = "";

    // if (tab.rows.length <= 0) {
    // alert(MSG_NORECORD);
    // return;
    // }

    var arg = new Object();
    // �������ͣ�add
    arg.doType = "add";
    arg.proj_nbxh = "";
    var ret = dialog("/UI/ccb/loan/coopprojMgr/coopprojEdit.jsp?proj_nbxh=" + proj_nbxh + "&doType=add", arg, sfeature);

    if (ret == "1") {
        document.getElementById("coopprojTable").RecordCount = "0";
        Table_Refresh("coopprojTable", false, body_resize);
    }
}

/**
 * �༭��Ϣ
 * 
 * @param doType:��������
 *            �޸� edit
 * @param 
 * @return
 */
function coopprojTable_editRecord_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }
    // var sfeature = "dialogwidth:800px;
    // dialogheight:460px;center:yes;help:no;resizable:yes;scroll:no;status:no";
    var sfeature = "dialogwidth:800px; dialogheight:620px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["coopprojTable"];
    var trobj = tab.rows[tab.activeIndex];

    var proj_nbxh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        proj_nbxh = tmp[0];
    }
    var arg = new Object();
    // �������ͣ�edit
    arg.doType = "edit";
    // ��Ѻ���
    arg.proj_no = proj_nbxh;
    var ret = dialog("/UI/ccb/loan/coopprojMgr/coopprojEdit.jsp?proj_nbxh=" + proj_nbxh + "&doType=edit", arg, sfeature);
    // var ret =
    // window.open("/UI/ccb/loan/coopprojMgr/coopprojEdit.jsp?proj_no=" +
    // proj_no + "&doType=edit");

    if (ret == "1") {
        document.getElementById("coopprojTable").RecordCount = "0";
        Table_Refresh("coopprojTable", false, body_resize);
    }
}

/**
 * <p>
 * ɾ������
 */
function coopprojTable_deleteRecord_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var tab = document.all["coopprojTable"];
    var trobj = tab.rows[tab.activeIndex];

    var proj_nbxh = "";
    var proj_name = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        proj_nbxh = tmp[0];
        proj_name = tmp[6];
    }

    if (confirm("ȷ��ɾ��������Ϣ��\r\n��Ŀ���ƣ�" + proj_name)) {
        document.getElementById("busiNode").value = BUSINODE_160;
        document.all.proj_nbxh.value = proj_nbxh;
        // alert(document.all.proj_nbxh.value);
        var retxml = createExecuteform(queryForm, "delete", "cc0101", "delete")
        if (analyzeReturnXML(retxml) != "false") {
            alert(MSG_DEL_SUCCESS);
            document.getElementById("coopprojTable").RecordCount = "0";
            Table_Refresh("coopprojTable", false, body_resize);
        }
    }
}

/*
 * �鿴����Ŀ�µ���ϸ��Ѻ��Ϣ
 */
function coopprojTable_queryMort_click() {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:900px; dialogheight:700px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["coopprojTable"];
    var trobj = tab.rows[tab.activeIndex];
    var proj_nbxh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        proj_nbxh = tmp[0];
        // alert(proj_no);

    }
    var arg = new Object();
    arg.doType = "select";

    dialog("/UI/ccb/loan/coopprojMgr/loanQuery.jsp?proj_nbxh=" + proj_nbxh, arg, sfeature);

    // dialog("/UI/ccb/loan/mortReg/mortgageEdit.jsp?mortID=" + mortID +
    // "&doType=select", arg, sfeature);
    // window.open("/UI/ccb/loan/coopprojMgr/coopprojEdit.jsp?proj_no=" +
    // proj_no + "&doType=select");
}
