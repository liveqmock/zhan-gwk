/**
 * 从ODSB数据导入
 */
function readFromODSB() {
    var newCnt = 0;
    var nullCnt = 0;
    var updateCnt = 0;
    var read_odsb = "";
    if (confirm(MSG_ODSB_READ_CONFIRM)) {
        show_status_label(window, "正在读入数据...", true);
        // formelement, exctype, actionName, methodname
        rtnXml = createExecuteform(queryForm, "update", "crd001", "readCrdCrts");
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
                    if (attrName == "importData") {

                        read_odsb = decode(oneRecord.getAttribute("value"));
                        var arr = read_odsb.split("_");
                        newCnt = arr[0];
                        nullCnt = arr[1];
                        updateCnt = arr[2];
                    }
                }
            }
        }
        // 显示提示信息
        document.getElementById("divResultInfo").style.display = "block";
        document.getElementById("_cell_importNewCnt").innerHTML = newCnt;
        document.getElementById("_cell_importNullCnt").innerHTML = nullCnt;
        document.getElementById("_cell_importUpdateCnt").innerHTML = updateCnt;
    } else {
        hide_status_label(window);
    }
    //Table_Refresh("ActionTable");
}
