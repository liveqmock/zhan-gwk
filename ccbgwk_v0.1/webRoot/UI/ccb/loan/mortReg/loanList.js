var SQLStr;
var gWhereStr = "";

/**
 * ��ʼ��form����,
 * <p>
 * ����ʼ�����㶨λ�ڲ�ѯ������һ���ؼ���;
 * <p>
 * ��ÿ�β�ѯ��Ϻ󽹵��Զ���λ����һ���ؼ�����ȫѡ��
 * 
 * @return
 */
function body_resize() {
  divfd_loanTab.style.height = document.body.clientHeight-180;
  loanTab.fdwidth="100%";
  initDBGrid("loanTab");
  // ��ʼ��ҳ�潹��
  body_init(queryForm, "cbRetrieve");
  document.getElementById("cust_name").focus();
  document.getElementById("cust_name").select();
}

/**
 * <p>
 * ������������������ɺ󽹵��Զ���λ�����������һ�������У� ����ȫѡ��;
 * <p>
 * ��ϵͳ��������������ĺ��ֻ���ƴ����ѯ ������ƴ����ѯ�ǡ�or���Ĺ�ϵ;
 * <p>
 * ��������ƴ����֧��ǰ��һ�¡����ģ����ѯ��
 * <p>
 * �����»س����Զ�֧�ֲ�ѯ��
 * </p>
 * 
 * @param ��form���ֻ���ID
 * @return
 */

function cbRetrieve_Click(formname) {
  // ����ϵͳ�����
  if(getSysLockStatus() == "1"){
    alert(MSG_SYSLOCK);
    return;
  }

  if (checkForm(queryForm) == "false")
    return;
  var whereStr = "";
  if (trimStr(document.getElementById("cust_name").value) != "") {
    whereStr += " and ((a.cust_name like'" + trimStr(document.getElementById("cust_name").value) + "%')";
    whereStr += " or (a.cust_py  like'" + trimStr(document.getElementById("cust_name").value) + "%'))";
  }
    if (trimStr(document.getElementById("bankid").value) != "") {
        // whereStr += " and a.bankid ='" +
        // trimStr(document.getElementById("bankid").value) + "' ";
        whereStr += " and a.bankid in(select deptid from ptdept start with deptid='"+document.getElementById("bankid").value+"' connect by prior deptid=parentdeptid)";
    }

    
  whereStr += " order by a.CUST_OPEN_DT desc,APLY_DT desc,a.cust_py asc ";

  document.all["loanTab"].whereStr = whereStr;
  document.all["loanTab"].RecordCount = "0";
  document.all["loanTab"].AbsolutePage = "1";
  // Table_Refresh in dbgrid.js pack
  Table_Refresh("loanTab", false, body_resize);
  
  // �ж����޴����¼���أ����û������ʾ�Ƿ����Ӵ�����Ϣ
  // �����ӳ�ʵ�ֲſ�ʵ��
  window.clearTimeout();
  window.setTimeout("addMortWin()",500);
  window.clearTimeout();
}

/**
 * ��������
 * 
 * @param �������
 */
function addMortWin(){
  var tab = document.all["loanTab"];
  if (tab.rows.length <= 0) {
    if(confirm(MSG_ADDMORTINFO)){
      var sfeature = "dialogwidth:800px; dialogheight:520px;center:yes;help:no;resizable:yes;scroll:no;status:no";
      var arg = new Object();
      // �������ͣ�add
      arg.doType = "add";
      var ret = dialog("/UI/ccb/loan/loanMgr/loanEdit.jsp?doType=add", arg, sfeature);
      if (ret == "1") {
        // document.getElementById("loanTab").RecordCount = "0";
        Table_Refresh("loanTab", false, body_resize);
      }
    }
  }
}

/**
 * ��ӵ�Ѻ��Ϣ
 * 
 * @param doType:��������
 * @param loanid:�����������
 * @return
 */
function loanTab_appendRecod_click() {
// ����ϵͳ�����
  if(getSysLockStatus() == "1"){
    alert(MSG_SYSLOCK);
    return;
  }
  
  var sfeature = "dialogwidth:600px; dialogheight:360px;center:yes;help:no;resizable:yes;scroll:no;status:no";
  var tab = document.all["loanTab"];
  var trobj = tab.rows[tab.activeIndex];

  var loanID = "";

  if (tab.rows.length <= 0) {
    alert(MSG_NORECORD);
    return;
  }

  if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
    var tmp = trobj.ValueStr.split(";");
    loanID = tmp[1];
  }
  var arg = new Object();
  // �������ͣ�add
  arg.doType = "add";
  // �����������
  arg.loanID = loanID;
  var ret = dialog("mortRegEdit.jsp?loanID=" + loanID + "&doType=add", arg, sfeature);

  if (ret == "1") {
    document.getElementById("loanTab").RecordCount = "0";
    Table_Refresh("loanTab", false, body_resize);
  }
  
}

/**
 * �쿴������ϸ����
 * 
 * @param loanID�������������
 * @param doType:select
 *            ��������
 * @return
 */
function loanTab_query_click() {
  
// ����ϵͳ�����
  if(getSysLockStatus() == "1"){
    alert(MSG_SYSLOCK);
    return;
  }
  
  var sfeature = "dialogwidth:800px; dialogheight:520px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
  var tab = document.all["loanTab"];
  var trobj = tab.rows[tab.activeIndex];
  var loanID = "";
  var nbxh = "";

  if (tab.rows.length <= 0) {
    alert(MSG_NORECORD);
    return;
  }

  if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
    var tmp = trobj.ValueStr.split(";");
    nbxh = tmp[0];
    loanID = tmp[1];

  }
  var arg = new Object();
  arg.doType = "select";
  dialog("/UI/ccb/loan/loanMgr/loanEdit.jsp?nbxh="+nbxh+"&loanID=" + loanID + "&doType=select", arg, sfeature);
}

/**
 * ˫����񵯳���Ѻ�Ǽ���Ϣ
 * 
 * @return
 */
function loanTab_TRDbclick() {
  // loanTab_query_click();
  loanTab_appendRecod_click();
}
