
function initData() {
    var importCnt = 0;
    var todayCnt = 0;
    var read_odsb = "";
    if (confirm("ȷ��Ҫ�������ݳ�ʼ����")) {
        show_status_label(window, "���ڳ�ʼ������...", true);
        rtn = createExecuteform(queryForm, "update", "bdgagency", "initData");
        hide_status_label(window);
        if (analyzeReturnXML(rtn) != "false") {
        }
    }
}
