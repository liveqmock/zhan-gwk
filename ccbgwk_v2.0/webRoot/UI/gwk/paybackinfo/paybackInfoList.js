
/**
 * 初始化form函数
 *
 */
function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 200 + "px";
    ActionTable.fdwidth = "1200px";
    //divfd_ActionTable.style.height="100%";
    //divfd_ActionTable.style.width="100%";

    initDBGrid("ActionTable");
    //body_init(queryForm, "queryClick");
}

function datatable_refresh() {
}

/**
 * 查询按钮对应方法
 */
function queryClick() {
    var whereStr = "";

    if (trimStr(document.all["account"].value) != "")
        whereStr += " and ( account like '%" + document.all.account.value + "%')";

    if (trimStr(document.all["cardname"].value) != "") {
        whereStr += " and ( cardname like '%" + document.all.cardname.value + "%')";
    }
    if (trimStr(document.all["voucherid"].value) != "")
        whereStr += " and ( voucherid like '%" + document.all.voucherid.value + "%')";

    if (trimStr(document.all["areacode"].value) != "")
        whereStr += " and ( areacode ='" + document.getElementById("areacode").value + "')";

    if (trimStr(document.all["status"].value) != "")
        whereStr += " and ( status ='" + document.all.status.value + "')";

    if (whereStr != document.all["ActionTable"].whereStr) {
        document.all["ActionTable"].whereStr = whereStr + " order by voucherid ";
        document.all["ActionTable"].RecordCount = "0";
        document.all["ActionTable"].AbsolutePage = "1";
        
        Table_Refresh("ActionTable");
    }

}
