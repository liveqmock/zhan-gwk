function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 235 + "px";
    ActionTable.fdwidth = "1300px";
}

function cbRetrieve_Click() {
    var whereStr = "";
    var old_18 = "";
    var new_19 = "";
    var cardNo = "";
    var name = "";
    old_18 =  trimStr(document.getElementById("old_18").value) ;
    cardNo = trimStr(document.getElementById("cardNo").value);
    new_19 = trimStr(document.getElementById("new_19").value);
    name =  trimStr(document.getElementById("name").value) ;
    if(old_18 == "" && new_19 == "" && cardNo == "" && name == ""){
            alert("请输入查询条件!");
            return;
    }
    if (old_18!= "") {
        whereStr += " and  old_18 ='" + old_18+ "'";
    }
    if (cardNo != "") {
         whereStr += " and cardno ='" + cardNo + "'";
    }
    if (new_19!= "") {
         whereStr += " and new_19 = '" + new_19+ "'";
    }
    if (name != "") {
        whereStr += " and name like '%" + name + "%'";
    }
     if (whereStr != document.all["ActionTable"].whereStr) {
        document.all["ActionTable"].whereStr = whereStr;
        document.all["ActionTable"].RecordCount = "0";
        document.all["ActionTable"].AbsolutePage = "1";

        Table_Refresh("ActionTable");
    }
}