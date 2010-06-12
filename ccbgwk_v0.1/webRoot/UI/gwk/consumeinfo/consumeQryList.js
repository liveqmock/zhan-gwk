var SQLStr;
var gWhereStr = "";

/**
 * 初始化form函数,
 * <p>
 * ■初始化焦点定位在查询条件第一个控件上;
 * <p>
 * ■每次查询完毕后焦点自动定位到第一个控件，且全选；
 *
 */
function body_resize() {
    divfd_ActionTable.style.height = document.body.clientHeight - 235 +"px";
    ActionTable.fdwidth="1300px";

    initDBGrid("ActionTable");
    body_init(queryForm, "queryClick");
}

function datatable_refresh() {
}

/**
 * 察看贷款详细函数
 *
 * @param loanID：贷款申请序号
 * @param doType:select
 *          操作类型
 */
function ActionTable_query_click() {

//增加系统锁检查
  if(getSysLockStatus() == "1"){
    alert(MSG_SYSLOCK);
    return;
  }

  var sfeature = "dialogwidth:800px; dialogheight:550px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
  var tab = document.all["ActionTable"];
  var trobj = tab.rows[tab.activeIndex];
  var lsh = "";

  if (tab.rows.length <= 0) {
    alert(MSG_NORECORD);
    return;
  }

  if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
    var tmp = trobj.ValueStr.split(";");
    lsh = tmp[0];

  }
  var arg = new Object();
  arg.doType = "select";
  dialog("consumeEdit.jsp?lsh=" + lsh + "&doType=select", arg, sfeature);
}

/**
 * 双击表格弹出详细信息查看画面 调用查看函数
 *
 */
function ActionTable_TRDbclick() {
  ActionTable_query_click();
}

