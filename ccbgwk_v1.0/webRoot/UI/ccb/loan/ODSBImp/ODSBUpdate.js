/*********************************************************************
 *    ��������: Odsb ���ݶ���
 *    ��    ��: leonwoo
 *    ��������: 2010/01/16
 *    �� �� ��:
 *    �޸�����:
 *    ��    Ȩ: ��˾
 ***********************************************************************/
/**
 * ���ݼ�� �ٸ���ln_loanapply�С����������ϱ�־��Ϊ��1���ļ�¼�� ��ln_odsb_loanapply����ͬ��������ŵļ�¼�Ƚ�
 * ���������������������� ���в�ͬ������excel��ʾ��<br>
 * �ڲ�ѯln_odsb_loanapply�е�proj_no�Ƿ��ڵ�Ѻϵͳ�д��ڣ�������Ŀ��������������򵯳�excel��
 */
function ODSBCheck() {
    if (confirm(MSG_ODSB_CHECK_CONFIRM)) {
        show_status_label(window, "���ڼ������...", true);
        // ���������Ƚ��
        var checkResult = 0;
        // ���proj_no��������Ž��
        var checkProj = 0;
        // var rtnXml = createselectArr("odsbCheck", "text", "1,", "odsb01",
        // "ODSBCheck");
        var rtnXml = createExecuteform(queryForm, "update", "odsb01", "ODSBCheck")
        // if (rtnXml != "false") {
        if (analyzeReturnXML(rtnXml) != "false") {
            var dom = createDomDocument();
            dom.loadXML(rtnXml);
            var fieldList = dom.getElementsByTagName("record")[0];
            for ( var i = 0; i < fieldList.childNodes.length; i++) {
                if (fieldList.childNodes[i].nodeType == 1) {
                    oneRecord = fieldList.childNodes[i];
                    attrName = oneRecord.getAttribute("name");
                    if (attrName == "checkResult") {
                        checkResult = decode(oneRecord.getAttribute("value"));
                    }
                    if (attrName == "checkProj") {
                        checkProj = decode(oneRecord.getAttribute("value"));
                    }
                }
            }
        }
        if (checkResult > 0 || checkProj > 0) {
            hide_status_label(window);
            // ���������excel
            if (checkResult > 0) {
                document.getElementById("queryForm").target = "_blank";
                document.getElementById("queryForm").action = "ODSBUpdateCheckReport.jsp";
                document.getElementById("queryForm").submit();
            } else if (checkProj > 0) {
                // ODSB�������ڱ�ϵͳ�в�����
                document.getElementById("queryForm").target = "_blank";
                document.getElementById("queryForm").action = "ODSBUpdateCheckProj.jsp";
                document.getElementById("queryForm").submit();
            }
        } else {
            hide_status_label(window);
            alert(MSG_ODSB_CHECK_RESULT);
        }
    }
}
/*******************************************************************************
 * ����ODSB���´�����Ϣ��
 * 
 * @return ���¼�¼��
 * @return ������¼��
 * @return ���µ�Ѻ�����ռ�¼��
 * @return ���´��������ϱ�־��¼��
 */
function ODSBUpdate() {
    var updateCnt = 0;
    var addCnt = 0;
    var updateMortDateCnt = 0;
    var updateNeedCDCnt = 0;
    if (confirm(MSG_ODSB_UPDATE_CONFIRM)) {
        show_status_label(window, "���ڸ�������...", true);
        // ���浽���ر������ύ��̨
        var rtnXml = createExecuteform(queryForm, "update", "odsb01", "ODSBUpdate")
        // if (analyzeReturnXML(retxml) != "false") {
        // hide_status_label(window);
        // alert(MSG_SUCCESS);
        // }
        if (analyzeReturnXML(rtnXml) != "false") {
            hide_status_label(window);
            var dom = createDomDocument();
            dom.loadXML(rtnXml);
            var fieldList = dom.getElementsByTagName("record")[0];
            for ( var i = 0; i < fieldList.childNodes.length; i++) {
                if (fieldList.childNodes[i].nodeType == 1) {
                    oneRecord = fieldList.childNodes[i];
                    attrName = oneRecord.getAttribute("name");
                    // ���¼�¼��
                    if (attrName == "updateCnt") {
                        updateCnt = decode(oneRecord.getAttribute("value"));
                    }
                    // ������¼��
                    if (attrName == "addCnt") {
                        addCnt = decode(oneRecord.getAttribute("value"));
                    }
                    // ���µ�Ѻ�����ռ�¼��
                    if (attrName == "updateMortDateCnt") {
                        updateMortDateCnt = decode(oneRecord.getAttribute("value"));
                    }
                    // ���´��������ϱ�־��¼��
                    if (attrName == "updateNeedCDCnt") {
                        updateNeedCDCnt = decode(oneRecord.getAttribute("value"));
                    }
                }
            }
            // ��ʾ��ʾ��Ϣ
            document.getElementById("divResultInfo").style.display = "block";
            document.getElementById("_cell_updateCnt").innerHTML = updateCnt;
            document.getElementById("_cell_addCnt").innerHTML = addCnt;
            document.getElementById("_cell_updateMortDateCnt").innerHTML = updateMortDateCnt;
            document.getElementById("_cell_updateNeedCDCnt").innerHTML = updateNeedCDCnt;
        } else {
            hide_status_label(window);
        }
    }
}
