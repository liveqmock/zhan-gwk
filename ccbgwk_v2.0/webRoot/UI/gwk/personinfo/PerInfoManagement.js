//////初始化信息
function body_load() {
	var tab = document.all("PerInfoTab");

	PerInfoTab.actionname = "sm0021";
	divfd_PerInfoTab.style.height = document.body.clientHeight - 180 + "px";
    PerInfoTab.fdwidth="100%";
	PerInfoTab.editmethodname = "editenum";
	PerInfoTab.delmethodname = "delenum";

    var whereStr = "";
    var deptCD = document.getElementById("departmentID").value;
    var levelNo = document.getElementById("levelNo").value;
    if (deptCD != null && levelNo != null){
        if(levelNo == 1){
           whereStr += " and lg.supercode = " + deptCD; 
        } else if (levelNo == 2){
            whereStr += " and lg.code = " + deptCD;
        }
        whereStr += " order by areacode, deptcode, pername ";
        document.all["PerInfoTab"].whereStr = whereStr;
        document.all["PerInfoTab"].RecordCount = "0";
        document.all["PerInfoTab"].AbsolutePage = "1";
        //alert(document.all["PerInfoTab"].whereStr);
        Table_Refresh("PerInfoTab", false , body_resize);
    }
}

function body_resize() {
    divfd_PerInfoTab.style.height = document.body.clientHeight - 180 + "px";
    PerInfoTab.fdwidth = "100%";
    initDBGrid("PerInfoTab");
    // 初始化页面焦点
    body_init(queryForm, "cbRetrieve");
    document.getElementById("cust_name").focus();
    document.getElementById("cust_name").select();
}

/*
* 检索按钮单击事件
* */
function cbRetrieve_Click() {
    // 增加系统锁检查
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    if (checkForm(queryForm) == "false")
        return;
    var whereStr = "";
    if (trimStr(document.getElementById("areacode").value) != "") {
        whereStr += " and (p.areacode =" + trimStr(document.getElementById("areacode").value) + ")";
    }
    if (trimStr(document.getElementById("cust_name").value) != "") {
        whereStr += " and (p.pername like'%" + trimStr(document.getElementById("cust_name").value) + "%')";
    }
    if (trimStr(document.getElementById("personalID").value) != ""){
        whereStr += "and p.perid like '" + trimStr(document.getElementById("personalID").value) + "%'";
    }
    if (trimStr(document.getElementById("departmentName").value) != ""){
        whereStr += " and lg.name like '%" + trimStr(document.getElementById("departmentName").value) + "%'";
    }
    whereStr += " order by areacode, deptcode, pername ";
    document.all["PerInfoTab"].whereStr = whereStr;
    document.all["PerInfoTab"].RecordCount = "0";
    document.all["PerInfoTab"].AbsolutePage = "1";
    // Table_Refresh in dbgrid.js pack
    Table_Refresh("PerInfoTab", false , body_resize);
//    initDBGrid("PerInfoTab");
}

/////主数据表格TD的单击事件
function MenuTable_TDclick(el) {
	el = event.srcElement;
	var trobj = getOwnerTR(el);
	if (trobj.edit == "true") {
		///根据数据类型生成添加对象
		if (el.fieldtype == "text") {
			CreateText(el);
		}
		if (el.fieldtype == "number") {
			CreateNumberText(el);
		}
		if (el.fieldtype == "dropdown") {
			switch (el.fieldname) {
			  case "targetMachine":
				el.enumType = "matchinetype";
				el.fieldTitle = "\u7f16\u7801,\u540d\u79f0";
				break;
			  case "OpenWindow":
				el.enumType = "openwindow";
				el.fieldTitle = "\u7f16\u7801,\u540d\u79f0";
				break;
			}
			dropdown(el);
		}
	}
}

////////////枚举添加一条纪录
function MenuTable_updateRecord_click(tab) {
	updateRecord(tab, "ParentMenuID&text&" + parent.window.paramValue.value);
}

/**
 * 添加信息
 *
 * @param doType:操作类型
 * @param loanid:贷款申请序号
 */
function PerInfoTab_appendRecod_click() {
    // 增加系统锁检查
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:320px;center:yes;help:no;resizable:yes;scroll:no;status:yes";
    var tab = document.all["PerInfoTab"];
    var trobj = tab.rows[tab.activeIndex];

    var arg = new Object();
    // 操作类型：add
    arg.doType = "add";
    arg.operid =  parent.parent.document.frames("roofFrame").document.getElementById("hhidOperatorID").value;
//    alert(operid);
    var ret = dialog("PerInfoEdit.jsp?doType=add", arg, sfeature);

    if (ret == "1") {
        document.getElementById("PerInfoTab").RecordCount = "0";
        Table_Refresh("PerInfoTab", false, body_resize);
    }
}
       
/**
 * 察看详细函数
 *
 * @param loanID：贷款申请序号
 * @param doType:select
 *            操作类型 
 */
function PerInfoTab_query_click() {

    // 增加系统锁检查
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }
    var sfeature = "dialogwidth:800px; dialogheight:320px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["PerInfoTab"];
    var trobj = tab.rows[tab.activeIndex];
    var recSequence = "";
    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }
   
    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        recSequence = tmp[0];

    }
    var arg = new Object();
    // 操作类型：select
    arg.doType = "select";
//    arg.perID = perID;
    arg.operid =  parent.parent.document.frames("roofFrame").document.getElementById("hhidOperatorID").value;
    dialog("PerInfoEdit.jsp?doType=select&recSequ="+recSequence, arg, sfeature);
}

/**
 * 双击表格弹出详细信息查看画面 调用查看函数
 *
 */
function PerInfoTab_TRDbclick() {
    PerInfoTab_query_click();
}

/**
 * 编辑信息
 *
 * @param doType:操作类型
 *            修改 edit
 * @param nbxh:内部序号
 */
function PerInfoTab_editRecord_click() {
    // 增加系统锁检查
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }
    var sfeature = "dialogwidth:800px; dialogheight:320px;center:yes;help:no;resizable:yes;scroll:no;status:no";
    var tab = document.all["PerInfoTab"];
    var trobj = tab.rows[tab.activeIndex];
    var recSequence = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        recSequence = tmp[0];
    }
    var arg = new Object();
    // 操作类型：edit
    arg.doType = "edit";
    arg.operid =  parent.parent.document.frames("roofFrame").document.getElementById("hhidOperatorID").value;
    var ret = dialog("PerInfoEdit.jsp?doType=edit&recSequ="+recSequence, arg, sfeature);

    if (ret == "1") {
        document.getElementById("PerInfoTab").RecordCount = "0";
        Table_Refresh("PerInfoTab", false, body_resize);
    }
}

/**
 * <p>
 * 删除函数
 *
 * @param mortID:抵押编号
 * @param keepCont:保管内容
 * @param loanID：贷款申请序号
 */
function PerInfoTab_deleteRecord_click() {
    // 增加系统锁检查
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var tab = document.all["PerInfoTab"];
    var trobj = tab.rows[tab.activeIndex];

    var recSequence = "";

    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        recSequence = tmp[0];
    }
    if (trobj.cells[0] != undefined){
        if (confirm(MSG_DELETE_CONFRIM)) {
            // 保存到隐藏变量中提交后台
            document.all.recinsequence.value = recSequence;
            document.getElementById("busiNode").value = BUSINODE_150;    //系统日志表是用
            var retxml = createExecuteform(queryForm, "delete", "pe001", "delete")
            if (analyzeReturnXML(retxml) != "false") {
                alert(MSG_DEL_SUCCESS);
                document.getElementById("PerInfoTab").RecordCount = "0";
                Table_Refresh("PerInfoTab", false, body_resize);
            }
        }
    }
}
