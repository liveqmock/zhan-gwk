/**
 * ��ʼ��form����
 *
 */
function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 150 + "px";
    ActionTable.fdwidth = "1300px";

    initDBGrid("ActionTable");
}

/**
 * ����������Ϣ
 */
function ActionTable_Send_click() {
    var rtnCnt = "";
    var sendCnt = "";
    var updateCnt = "";
    if (confirm("ȷ��Ҫ��������ŷ��Ϳ�������Ϣ��")) {
        show_status_label(window, "���ڷ�������...", true);
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
                    // ��¼��
                    if (attrName == "rtnCnt") {
                        rtnCnt = decode(oneRecord.getAttribute("value"));
                            var arr = rtnCnt.split("_");
                            sendCnt = arr[0];
                            updateCnt = arr[1];
                           if (sendCnt == 0 &&  updateCnt == 0) {
                            alert("û��δ�������ݡ�");
                        } else {
                            alert("�������ݼ�¼����" + sendCnt + "\n���ͳɹ���¼����" + updateCnt);
                        }
                    }
                }
            }
           Table_Refresh("ActionTable");
        } else {
            hide_status_label(window);
        }
    }

}
