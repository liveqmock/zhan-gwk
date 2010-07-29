/**
 * 初始化form函数
 *
 */
function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 150 + "px";
    ActionTable.fdwidth = "1300px";

    initDBGrid("ActionTable");
}

/**
 * 发送消费信息
 */
function ActionTable_Send_click() {
    var rtnCnt = "";
    var sendCnt = "";
    var updateCnt = "";
    if (confirm("确认要向财政部门发送卡消费信息吗？")) {
        show_status_label(window, "正在发送数据...", true);
        rtnXml = createExecuteform(queryForm, "update", "cons01", "writeConsumeExpInfo");
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
                        if (rtnCnt == "-1") {
                            alert("发送超时，请稍后再试。");
                             document.location.href = "consumeSendMgrList.jsp";
                        } else {
                            var arr = rtnCnt.split("_");
                            sendCnt = arr[0];
                            updateCnt = arr[1];
                            if (sendCnt != updateCnt) {
                                alert("发送数据记录数：" + sendCnt + "\n发送成功记录数：" + updateCnt);
                                document.location.href = "consumeSendMgrList.jsp";
                            } else {
                                alert("发送数据记录数：" + sendCnt + "\n发送成功记录数：" + updateCnt);
                                document.location.href = "consumeQryList.jsp";
                            }
                        }
                    }
                }
            }

        } else {
            hide_status_label(window);
        }
    }

}
