function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 235 + "px";
    ActionTable.fdwidth = "1300px";

}

function cbRetrieve_Click() {
 var whereStr = "";
    var acctno = "";
    var bankNo = "";
    acctno =  trimStr(document.getElementById("acctno").value) ;
    bankNo = trimStr(document.getElementById("bankNo").value);

    if(acctno == "" && bankNo == "" ){
            alert("�������ѯ����!");
            return;
    }

    if (acctno!= "") {
        whereStr += " and  SACCNO1 ='" + acctno+ "'";
    }
    if (bankNo != "") {
         whereStr += " and ORIGBRANCH ='" + bankNo + "'";
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