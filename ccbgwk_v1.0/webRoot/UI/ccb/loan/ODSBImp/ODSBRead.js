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
 * ��ODSB���ݵ���
 */
function readFromODSB() {
    var importCnt = 0;
    var todayCnt = 0;
    var read_odsb = "";
    if (confirm(MSG_ODSB_READ_CONFIRM)) {
        show_status_label(window, "���ڶ�������...", true);
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
                    // ��¼��
                    if (attrName == "importData") {

                        read_odsb = decode(oneRecord.getAttribute("value"));
                        var arr = read_odsb.split("_");
                        var show_read_odsb = "ODSB״̬:" + arr[0] + " <br>���ر�������������:" + arr[1] + " <br>��ȡODSB���������ݼ�¼��:" + arr[2]
                                + " <br>��ȡODSB���������ݼ�¼��:" + arr[3];
                        importCnt = arr[4];
                        todayCnt = arr[5];
                        if (arr[2] > "0") {
                            show_read_odsb += " <br>��ʼ������ˮ�ߺ�:" + arr[6] + " <br>��ֹ������ˮ�ߺ�:" + arr[7];
                        }
                        //odsb״̬_���ر�������������_��ȡODSB���������ݼ�¼��_��ȡODSB���������ݼ�¼��_���ζ����¼��_���ն�ȡ��������_��ʼ������ˮ�ߺ�_��ֹ������ˮ�ߺ�
                    }
                }
            }
        }
        // ��ʾ��ʾ��Ϣ
        document.getElementById("divResultInfo").style.display = "block";
        document.getElementById("_cell_importTodayCnt").innerHTML = todayCnt;
        document.getElementById("_cell_importCnt").innerHTML = importCnt;
        document.getElementById("_read_odsb_process").innerHTML = show_read_odsb;
    } else {
        hide_status_label(window);
    }
    //Table_Refresh("ActionTable");
}
