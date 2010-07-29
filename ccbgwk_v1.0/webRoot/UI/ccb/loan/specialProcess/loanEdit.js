/*******************************************************************************
 * 
 * 文件名： 贷款详细管理
 * 
 * 作 用：
 * 
 * 作 者： leonwoo
 * 
 * 时 间： 2010-01-16
 * 
 * 版 权： leonwoo
 * 
 ******************************************************************************/
var dw_column;
/**
 * 初始化form表单内容，根据主键取出详细信息，包括查询、删除、修改
 */
function formInit() {
    var arg = window.dialogArguments;
    operReSelectCustMgr();
    if (arg) {
        operation = arg.doType;
        if (operation != "add") {
            load_form();
            // 客户经理初始化
            operReSelectCustMgr();
            document.getElementById("CUSTMGR_ID").value = document.getElementById("custMgrID").value;
        }

        // 只读情况下，页面所有空间禁止修改
        if (operation == "select" || operation == "delete") {
            readFun(document.getElementById("editForm"));
        }
    }
    // 初始化数据窗口，校验的时候用
    dw_column = new DataWindow(document.getElementById("editForm"), "form");

}

/**
 * 根据经办行联动
 */
function reSelect() {
    operReSelectCustMgr();
}

/**
 * 根据经办行联动下拉项目:经办人
 * 
 * @return
 */
function operReSelect() {
    refresh_select("OPERID", "select OPERID as value ,OPERNAME as text  from ptoper where" + " deptid='"
            + document.getElementById("BANKID").value + "'");
}

/**
 * 根据经办行联动下拉项目:客户经理ID
 * 
 * @return
 */
function operReSelectCustMgr() {
    refresh_select("CUSTMGR_ID", "select OPERID as value ,OPERNAME as text  from ptoper where" + " deptid='"
            + document.getElementById("BANKID").value + "'");
}

/**
 * <p>
 * 保存函数，包括增加、修改都调用该函数
 * <p>
 * createExecuteform 参数分别为
 * <p>
 * ■editForm :提交的form名称
 * <p>
 * ■insert ：操作类型，必须为insert、update、delete之一；
 * <p>
 * ■mort01 ：会话id，后台业务逻辑组件
 * <p>
 * ■add: : 后台业务组件实际对应方法名称
 * 
 * @param doType：操作类型
 * 
 */
function saveClick() {

    var doType = document.all.doType.value;
    if (document.getElementById("RELEASECONDCD").value == "03") {
        var waringMsgDate = "放款方式为签约放款，则【开户日期】不能为空！";
        var waringMsgProj = "放款方式为签约放款，则【项目编号】不能为空！";
        if (document.getElementById("CUST_OPEN_DT").value == "") {
            alert(waringMsgDate);
            document.getElementById("CUST_OPEN_DT").focus();
            return;
        }
        if (document.getElementById("PROJ_NO").value == "") {
            alert(waringMsgProj);
            document.getElementById("PROJ_NO").focus();
            return;
        }
    }

    if (dw_column.validate() != null)
        return;

    document.getElementById("busiNode").value = BUSINODE_150;
    var retxml = "";
    if (operation == "add") {
        retxml = createExecuteform(editForm, "insert", "loan01", "add");
    } else if (operation = "edit") {
        retxml = createExecuteform(editForm, "update", "loan01", "edit");
    }

    if (analyzeReturnXML(retxml) + "" == "true") {
        window.returnValue = "1";
        window.close();
    }
}
