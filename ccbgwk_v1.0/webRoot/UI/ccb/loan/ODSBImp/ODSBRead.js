function body_resize() {
//     divfd_ActionTable.style.height = document.body.clientHeight - 200 + "px";
//    ActionTable.fdwidth = "1200px";
    divfd_ActionTable.style.height="100%";
   divfd_ActionTable.style.width="100%";
     initDBGrid("ActionTable");
}
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
  document.getElementById("_cell_importCnt").innerHTML = "0";
  var importCnt = 0;
  var newLsh = "";
    var whereStr = "";
  if (confirm(MSG_ODSB_READ_CONFIRM)) {
    show_status_label(window, "正在读入数据...", true);
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
          // 记录数
          if (attrName == "importCnt") {

            importCnt = decode(oneRecord.getAttribute("value"));
            var arr = importCnt.split("_");
              importCnt = arr[0];
              newLsh = arr[1];
                if (trimStr(newLsh) != "") {
                whereStr += " where lsh > '" + trimStr(newLsh) + "' ";
                if (whereStr != document.all["ActionTable"].whereStr) {
                document.all["ActionTable"].whereStr = whereStr + " order by lsh ";
                document.all["ActionTable"].RecordCount = "0";
                document.all["ActionTable"].AbsolutePage = "1";
                Table_Refresh("ActionTable");
                }
                }
          }

          }
        }
      }
      // 显示提示信息
      document.getElementById("divResultInfo").style.display = "block";
      if(importCnt >= "0"){
        document.getElementById("_cell_importCnt").innerHTML = importCnt;
      }

      document.getElementById("newLsh").value = newLsh;
    }else{
      hide_status_label(window);
    }
   //Table_Refresh("ActionTable");
}
