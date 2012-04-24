/**
 * 初始化form函数
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

    document.all["cardInfoTab"].whereStr = whereStr + " order by cbinfo.account ";
    document.all["cardInfoTab"].RecordCount = "0";
    document.all["cardInfoTab"].AbsolutePage = "1";

    Table_Refresh("cardInfoTab");

}


/**
 * 发送消费信息
 */
function cardInfoTab_Send_click() {
    var rtnCnt = 0;
    var sendCnt = 0;
    var updateCnt = 0;
    if (confirm("确认要向财政部门发送卡基本信息吗？")) {
        show_status_label(window, "正在发送数据...", true);
        rtnXml = createExecuteform(queryForm, "update", "crd002", "sendCrdbaseInfos");
        if (analyzeReturnXML(rtnXml) != "false") {
            hide_status_label(window);
            var dom = createDomDocument();
            dom.loadXML(rtnXml);
            var fieldList = dom.getElementsByTagName("record")[0];
            for (var i = 0; i < fieldList.childNodes.length; i++) {
                if (fieldList.childNodes[i].nodeType == 1) {
                    oneRecord = fieldList.childNodes[i];
                    attrName = oneRecord.getAttribute("name");
                    // 记录数
                    if (attrName == "rtnCnt") {
                        rtnCnt = decode(oneRecord.getAttribute("value"));
                       if(rtnCnt != 0){
                        var arr = rtnCnt.split("_");
                        sendCnt = arr[0];
                        updateCnt = arr[1];
                        if (sendCnt == 0 &&  updateCnt == 0) {
                            alert("没有未发送数据。");
                        } else {
                            alert("发送数据记录数：" + sendCnt + "\n发送成功记录数：" + updateCnt);
                        }
                        }
                    }
                }
            }
        }
           Table_Refresh("cardInfoTab");

        } else {
            hide_status_label(window);
        }
}
