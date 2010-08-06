/*function body_resize() {
 divfd_ActionTable.style.height = document.body.clientHeight - 200 + "px";
 ActionTable.fdwidth = "1200px";
 divfd_ActionTable.style.height="100%";
 divfd_ActionTable.style.width="100%";
 initDBGrid("ActionTable");
 }*/
/*function body_load(){
 var cnt = document.getElementById("newLsh").value;
 alert("cnt:"+cnt);
 if(cnt == null || cnt == "" || cnt == "0"){

 }
 }*/

/**
 * 从ODSB数据导入
 */
function readFromODSB() {
    var importCnt = 0;
    var todayCnt = 0;
    var read_odsb = "";
    if (confirm(MSG_ODSB_READ_CONFIRM)) {
        show_status_label(window, "正在读入数据...", true);
        // formelement, exctype, actionName, methodname
        rtnXml = createExecuteform(queryForm, "update", "odsb01", "readFromODSB");
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
                        var show_read_odsb = "ODSB状态:" + arr[0] + " <br>本地表中最后的入帐日:" + arr[1] + " <br>读取ODSB卡消费数据记录数:" + arr[2]
                                + " <br>读取ODSB卡基本数据记录数:" + arr[3];
                        importCnt = arr[4];
                        todayCnt = arr[5];
                        if (arr[2] > "0") {
                            show_read_odsb += " <br>起始数据流水线号:" + arr[6] + " <br>终止数据流水线号:" + arr[7];
                        }
                        //odsb状态_本地表中最后的入帐日_读取ODSB卡消费数据记录数_读取ODSB卡基本数据记录数_本次读入记录数_本日读取数据总数_起始数据流水线号_终止数据流水线号
                    }
                }
            }
        }
        // 显示提示信息
        document.getElementById("divResultInfo").style.display = "block";
        document.getElementById("_cell_importTodayCnt").innerHTML = todayCnt;
        document.getElementById("_cell_importCnt").innerHTML = importCnt;
        document.getElementById("_read_odsb_process").innerHTML = show_read_odsb;
    } else {
        hide_status_label(window);
    }
    //Table_Refresh("ActionTable");
}
