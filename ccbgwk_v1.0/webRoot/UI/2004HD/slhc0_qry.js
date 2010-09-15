function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 235 + "px";
   ActionTable.fdwidth = "700px";
   // divfd_ActionTable.style.height = "100%";
   // ActionTable.fdwidth = "100%";
}

function cbRetrieve_Click() {
    var whereStr = "";
    var bankcode = "";
    var bankname = "";
    var bankcode2 = "";
    var branchname = "";
    bankcode =  trimStr(document.getElementById("bankcode").value) ;
    bankname = trimStr(document.getElementById("bankname").value);
    bankcode2 = trimStr(document.getElementById("bankcode2").value);
    branchname =  trimStr(document.getElementById("branchname").value) ;
    if(bankcode == "" && bankname == "" && bankcode2 == "" && branchname == ""){
            alert("请输入查询条件!");
            return;
    }
    if (bankcode!= "") {
        whereStr += " and  bankcode like '%" + bankcode+ "%'";
    }
    if (bankname != "") {
         whereStr += " and bankname  like '%" + bankname + "%'";
    }
    if (bankcode2!= "") {
         whereStr += " and bankcode2 like '%" + bankcode2+ "%'";
    }
    if (branchname != "") {
        whereStr += " and branchname like '%" + branchname + "%'";
    }
     if (whereStr != document.all["ActionTable"].whereStr) {
        document.all["ActionTable"].whereStr = whereStr;
        document.all["ActionTable"].RecordCount = "0";
        document.all["ActionTable"].AbsolutePage = "1";

        Table_Refresh("ActionTable");
    }
}