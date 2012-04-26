
function initData() {
    var importCnt = 0;
    var todayCnt = 0;
    var read_odsb = "";
    if (confirm("确认要进行数据初始化吗？")) {
        show_status_label(window, "正在初始化数据...", true);
        //rtn = createExecuteform(queryForm, "update", "bdgagency", "initData");
        rtn = createExecuteform(queryForm, "update", "bdg01", "initData");
        hide_status_label(window);
        if (analyzeReturnXML(rtn) != "false") {
        }
    }
}
