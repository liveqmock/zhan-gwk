var SQLStr;
var gWhereStr = "";

/**
 * ��ʼ��form����,
 * <p>
 * ����ʼ�����㶨λ�ڲ�ѯ������һ���ؼ���;
 * <p>
 * ��ÿ�β�ѯ��Ϻ󽹵��Զ���λ����һ���ؼ�����ȫѡ��
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
 * �쿴������ϸ����
 *
 * @param loanID�������������
 * @param doType:select
 *          ��������
 */
function ActionTable_query_click() {

//����ϵͳ�����
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
 * ˫����񵯳���ϸ��Ϣ�鿴���� ���ò鿴����
 *
 */
function ActionTable_TRDbclick() {
  ActionTable_query_click();
}

