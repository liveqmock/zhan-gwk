/*********************************************************************
 *    ��������: Odsb ���ݶ���
 *    ��    ��: leonwoo
 *    ��������: 2010/01/16
 *    �� �� ��:
 *    �޸�����:
 *    ��    Ȩ: ��˾
 ***********************************************************************/
/**
 * ��ODSB���ݵ���
 */
function readFromODSB() {
  var importCnt = 0;
  if (confirm(MSG_ODSB_READ_CONFIRM)) {
    show_status_label(window, "���ڶ�������...", true);
    // formelement, exctype, actionName, methodname
    rtnXml = createExecuteform(queryForm, "update", "odsb01", "readFromODSB");
    //if (analyzeReturnXML(retxml) + "" == "true") {
    //  hide_status_label(window);
    //}else{
    //  hide_status_label(window);
    //}
   // if (rtnXml != "false") {
    if (analyzeReturnXML(rtnXml) != "false") {
      hide_status_label(window);
      var dom = createDomDocument();
      dom.loadXML(rtnXml);
      var fieldList = dom.getElementsByTagName("record")[0];
      for ( var i = 0; i < fieldList.childNodes.length; i++) {
        if (fieldList.childNodes[i].nodeType == 1) {
          oneRecord = fieldList.childNodes[i];
          attrName = oneRecord.getAttribute("name");
          // ��¼��
          if (attrName == "importCnt") {
            importCnt = decode(oneRecord.getAttribute("value"));
          }
        }
      }
      // ��ʾ��ʾ��Ϣ
      document.getElementById("divResultInfo").style.display = "block";
      document.getElementById("_cell_importCnt").innerHTML = importCnt;
    }else{
      hide_status_label(window);
    }
  }

}
