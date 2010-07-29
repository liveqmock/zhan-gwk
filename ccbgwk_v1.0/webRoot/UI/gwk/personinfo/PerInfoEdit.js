var dbOperateType = "";
var perID="";

var dw_column;
/**
 * 初始化form表单内容，根据主键取出详细信息，包括查询、删除、修改
 */
function formInit() {
    var arg = window.dialogArguments;
    if (arg) {
        operation = arg.doType;
        if (operation != "add") {
            load_form();
            // 客户经理初始化
            operReSelectCustMgr();
            document.getElementById("deptcode").value = document.getElementById("hhidDeptCode").value;
        }
        // 只读情况下，页面所有空间禁止修改
        if (operation == "select" || operation == "delete") {
            readFun(document.getElementById("editForm"));
        }
    }
    // 初始化数据窗口，校验的时候用
    dw_column = new DataWindow(document.getElementById("editForm"), "form");
    //初始化时，把创建者更新成现在登陆者
    document.getElementById("createcode").value = arg.operid;
}


/**
 * 根据经办行联动
  */
function reSelect() {
    operReSelectCustMgr();
}
/**
 * 根据经办行联动下拉项目:客户经理ID
 */
function operReSelectCustMgr() {
    var strSql = "select code as value , name as text from ls_bdgagency t  where supercode="+ document.getElementById("superdeptcode").value;
    strSql = strSql + " and lengthb(t.code) <> 3 ";
    refresh_select("deptcode", strSql );
}
/*
* 保存按钮
* */
function saveClick()
{
    //姓名不能为空
    if (document.getElementById("perName").value == ""){
        alert("姓名不能为空！");
        document.getElementById("perName").focus();
        return;
    }
    //身份证ID不能为空
    if (document.getElementById("perId").value == ""){
        alert("身份证ID不能为空！");
        document.getElementById("perId").focus();
        return;
    }
    //预算单位不能为空
    if (document.getElementById("superdeptcode").value == "" || document.getElementById("deptcode").value == ""){
        alert("预算单位不能为空！");
        //document.getElementById("perId").focus();
        return;
    }
    //保存
    var retxml = "";
    if (operation == "add") {
        retxml = createExecuteform(editForm, "insert", "pe001", "add");
    } else if (operation == "edit") {
        retxml = createExecuteform(editForm, "update", "pe001", "edit");
    }
    if (analyzeReturnXML(retxml) + "" == "true") {
        window.returnValue = "1";
        window.close();
    }
}