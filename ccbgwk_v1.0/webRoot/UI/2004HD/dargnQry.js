function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 235 + "px";
    ActionTable.fdwidth = "1300px";

}

function cbRetrieve_Click() {
 var whereStr = "";
    var acctno = "";
    var bankNo = "";
    var enddate = "";
    var name = "";
    acctno =  trimStr(document.getElementById("acctno").value) ;
    bankNo = trimStr(document.getElementById("bankNo").value);
    enddate = trimStr(document.getElementById("date").value);
    name =  trimStr(document.getElementById("custName").value) ;
    if(acctno == "" && bankNo == "" && enddate == "" && name == ""){
            alert("�������ѯ����!");
            return;
    }
    if (acctno!= "") {
        whereStr += " and  AccNo ='" + acctno+ "'";
    }
    if (bankNo != "") {
         whereStr += " and OpnBankNo ='" + bankNo + "'";
    }
    if (enddate!= "") {
         whereStr += " and EndDate = '" + dateChange(enddate)+ "'";
    }
    if (name != "") {
        whereStr += " and Name like '%" + name + "%'";
    }
     if (whereStr != document.all["ActionTable"].whereStr) {
        document.all["ActionTable"].whereStr = whereStr;
        document.all["ActionTable"].RecordCount = "0";
        document.all["ActionTable"].AbsolutePage = "1";
        //�����־��¼ taskAction
        retxml = createExecuteform(queryForm, "insert", "hsqr01", "taskinfoInsert");
        Table_Refresh("ActionTable");
    }

}
//�����ַ�����ʽת�� yyyy-mm-dd -> yyyymmdd
function dateChange(dt) {
    var dateStr = dt.substring(0,4) + dt.substring(5,7) + dt.substring(8,10);
    return dateStr;
}