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
         whereStr += " and ( cdinfo.account like '" + trimStr(document.getElementById("cardNo").value) + "%')";
    }
    if (trimStr(document.getElementById("cust_name").value) != "") {
         whereStr += " and (cdinfo.cardname like'%" + trimStr(document.getElementById("cust_name").value) + "%')";
    }
    if (trimStr(document.getElementById("deptcode").value) != "") {
        whereStr += " and (pinfo.deptcode like '%" + trimStr(document.getElementById("deptcode").value) + "%')";
    }
    if (trimStr(document.getElementById("personalID").value) != "") {
        whereStr += " and (cdinfo.idnumber like '" + trimStr(document.getElementById("personalID").value) + "%')";
    }
    if (trimStr(document.getElementById("sentflag").value) != "") {
            whereStr += " and cdinfo.sentflag ='" + trimStr(document.getElementById("sentflag").value) + "'";
    }
    if (trimStr(document.getElementById("status").value) != "") {
            whereStr += " and cdinfo.status ='" + trimStr(document.getElementById("status").value) + "'";
    }
    if (trimStr(document.getElementById("areacode").value) != "") {
            whereStr += " and areacode ='" + trimStr(document.getElementById("areacode").value) + "'";
    }
//    alert(whereStr);
    document.all["cardInfoTab"].whereStr = whereStr + " order by areacode, bgcyname, cdinfo.account ";
    document.all["cardInfoTab"].RecordCount = "0";
    document.all["cardInfoTab"].AbsolutePage = "1";

    Table_Refresh("cardInfoTab");
        
}
