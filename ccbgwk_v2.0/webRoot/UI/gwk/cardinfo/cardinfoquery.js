/**
 * ³õÊ¼»¯formº¯Êý
 *
 */
function body_resize() {
    divfd_cardInfoTab.style.height = document.body.clientHeight - 210 + "px";
    cardInfoTab.fdwidth = "1300px";

    initDBGrid("cardInfoTab");
    body_init(queryForm, "cbRetrieve");
}
function cbRetrieve_Click() {
    var whereStr = "";
    if (trimStr(document.getElementById("cardNo").value) != "") {
         whereStr += " and ( cbinfo.account like '" + trimStr(document.getElementById("cardNo").value) + "%')";
    }
    if (trimStr(document.getElementById("cust_name").value) != "") {
         whereStr += " and (cbinfo.cardname like'%" + trimStr(document.getElementById("cust_name").value) + "%')";
    }
    if (trimStr(document.getElementById("departmentName").value) != "") {
        whereStr += " and (bgcy.name like '%" + trimStr(document.getElementById("departmentName").value) + "%')";
    }
    if (trimStr(document.getElementById("personalID").value) != "") {
        whereStr += " and (cbinfo.idnumber like '" + trimStr(document.getElementById("personalID").value) + "%')";
    }
    if (trimStr(document.getElementById("sentflag").value) != "") {
            whereStr += " and cbinfo.sentflag ='" + trimStr(document.getElementById("sentflag").value) + "'";
    }
    if (trimStr(document.getElementById("status").value) != "") {
            whereStr += " and cbinfo.status ='" + trimStr(document.getElementById("status").value) + "'";
    }
//    alert(whereStr);
    document.all["cardInfoTab"].whereStr = whereStr + " order by cbinfo.account ";
    document.all["cardInfoTab"].RecordCount = "0";
    document.all["cardInfoTab"].AbsolutePage = "1";

    Table_Refresh("cardInfoTab");
        
}
