function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 235 + "px";
    ActionTable.fdwidth = "1300px";

}

function cbRetrieve_Click() {
 var whereStr = "";
    var acctno = "";
    var bankNo = "";
    var opacctDate = "";
    var perID = "";
    var custName = "";
    acctno =  trimStr(document.getElementById("acctno").value) ;
    bankNo = trimStr(document.getElementById("bankNo").value);
    opacctDate = trimStr(document.getElementById("date").value);
    perID =  trimStr(document.getElementById("custNo").value) ;
    custName = trimStr(document.getElementById("custName").value);
    if(acctno == "" && bankNo == "" && opacctDate == "" && perID == "" && custName == ""){
            alert("�������ѯ����!");
            return;
    }
    if (acctno!= "") {
        whereStr += " and t.saccno = '" + acctno+ "'";
    }
    if (bankNo != "") {
         whereStr += " and t.origbranch ='" + bankNo + "'";
    }
    if (opacctDate!= "") {
         whereStr += " and t.sopaccdate= '" + dateChange(opacctDate)+ "'";
    }
    if (perID != "") {
        whereStr += " and t.spersonid= '" + perID + "'";
    }
    if (custName != "") {
        whereStr += " and t.sname like '%" + custName + "%'";
    }
     if (whereStr != document.all["ActionTable"].whereStr) {
        document.all["ActionTable"].whereStr = whereStr + " order by  t.saccno ";
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