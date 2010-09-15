function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 235 + "px";
    ActionTable.fdwidth = "1300px";

}

function cbRetrieve_Click() {
 var whereStr = "";
    var acctno = "";
    var bankNo = "";
    //交易日期
    var dt1 = "";
    var dt2 = "";
    acctno =  trimStr(document.getElementById("acctno").value) ;
    bankNo = trimStr(document.getElementById("bankNo").value);
    dt1 = trimStr(document.getElementById("busidate1").value);
    dt2 = trimStr(document.getElementById("busidate2").value);
    if(acctno == "" && bankNo == "" && dt1 == "" && dt2==""){
            alert("请输入查询条件!");
            return;
    }

    if (dt1 != "") {
        whereStr += " and (STRANDATE >= '" + dateChange(dt1) + "')";
    }
    if (dt2 != "") {
        whereStr += " and (STRANDATE <= '" + dateChange(dt2) + "')";
    }

    if (acctno!= "") {
        whereStr += " and  CARDNO ='" + acctno+ "'";
    }
    if (bankNo != "") {
         whereStr += " and ORIG ='" + bankNo + "'";
    }
     if (whereStr != document.all["ActionTable"].whereStr) {
        document.all["ActionTable"].whereStr = whereStr;
        document.all["ActionTable"].RecordCount = "0";
        document.all["ActionTable"].AbsolutePage = "1";
         //添加日志记录 taskAction
        retxml = createExecuteform(queryForm, "insert", "hsqr01", "taskinfoInsert");
        Table_Refresh("ActionTable");
    }

}
//日期字符串格式转换 yyyy-mm-dd -> yyyymmdd
function dateChange(dt) {
    var dateStr = dt.substring(0,4) + dt.substring(5,7) + dt.substring(8,10);
    return dateStr;
}