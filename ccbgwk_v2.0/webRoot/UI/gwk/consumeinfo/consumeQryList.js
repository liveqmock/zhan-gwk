
/**
 * 初始化form函数
 *
 */
function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 235 + "px";
    ActionTable.fdwidth = "1300px";

    initDBGrid("ActionTable");
    body_init(queryForm, "queryClick");
}

function datatable_refresh() {
}

/**
 * 查询按钮对应方法
 */
function queryClick() {
    var whereStr = "";

    if (trimStr(document.all["account"].value) != "")
        whereStr += " and ( account like '" + document.all.account.value + "%')";

    if (trimStr(document.all["cardname"].value) != "") {
        whereStr += " and ( cardname like '" + document.all.cardname.value + "%')";
    }
    if (trimStr(document.all["tx_cd"].value) != "")
        whereStr += " and ( tx_cd ='" + document.all.tx_cd.value + "')";

    if (trimStr(document.all["status"].value) != "")
        whereStr += " and ( status ='" + document.all.status.value + "')";

    if (trimStr(document.getElementById("busidate1").value) != "") {
        whereStr += " and  busidate >='" + trimStr(document.getElementById("busidate1").value) + "'";
    }
    if (trimStr(document.getElementById("busidate2").value) != "") {
        whereStr += " and busidate <='" + trimStr(document.getElementById("busidate2").value) + "' ";
    }
    if (trimStr(document.getElementById("inac_date1").value) != "") {
        whereStr += " and inac_date >='" + trimStr(document.getElementById("inac_date1").value) + "'";
    }
    if (trimStr(document.getElementById("inac_date2").value) != "") {
        whereStr += " and inac_date <='" + trimStr(document.getElementById("inac_date2").value) + "' ";
    }

    if (whereStr != document.all["ActionTable"].whereStr) {
        document.all["ActionTable"].whereStr = whereStr + " order by lsh ";
        document.all["ActionTable"].RecordCount = "0";
        document.all["ActionTable"].AbsolutePage = "1";

        Table_Refresh("ActionTable");
    }

}


/**
 * 察看详细函数
 */
function ActionTable_query_click() {

    //增加系统锁检查
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:550px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["ActionTable"];
    var trobj = tab.rows[tab.activeIndex];
    var lsh = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        lsh = tmp[0];

    }
    var arg = new Object();
    arg.doType = "select";
    dialog("consumeEdit.jsp?lsh=" + lsh + "&doType=select", arg, sfeature);
}

/**
 * 双击表格弹出详细信息查看画面 调用查看函数
 */
function ActionTable_TRDbclick() {
    ActionTable_query_click();
}

