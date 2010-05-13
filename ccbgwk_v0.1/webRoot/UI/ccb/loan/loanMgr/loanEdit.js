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

// tab enter键 zhan
document.onkeydown = function(evt) {
    var isie = (document.all) ? true : false;
    var key;
    var srcobj;
    if (isie) {
        key = event.keyCode;
        srcobj = event.srcElement;
    } else {
        key = evt.which;
        srcobj = evt.target;
    }
    if (key == 13 && srcobj.type != 'button' && srcobj.type != 'submit' && srcobj.type != 'reset'
            && srcobj.type != 'textarea' && srcobj.type != '') {
        if (isie) {
            event.keyCode = 9;
        } else {
            var el = getNextElement(evt.target);
            if (el.type != 'hidden')
                el.focus();
            else
                while (el.type == 'hidden')
                    el = getNextElement(el);
            el.focus();
            return false;
        }
    }
}
function getNextElement(field) {
    var form = field.form;
    for ( var e = 0; e < form.elements.length; e++) {
        if (field == form.elements[e])
            break;
    }
    return form.elements[++e % form.elements.length];
}

/**
 * 初始化form表单内容，根据主键取出详细信息，包括查询、删除、修改
 */
function formInit() {
    var arg = window.dialogArguments;

    if (arg) {
        operation = arg.doType;
        if (operation != "add") {
            load_form();
            operReSelectCustMgr();
            // 客户经理初始化
            document.getElementById("CUSTMGR_ID").value = document.getElementById("custMgrID").value;
        }

        // 只读情况下，页面所有空间禁止修改
        if (operation == "select" || operation == "delete") {
            readFun(document.getElementById("editForm"));
        }
    }
    // 初始化数据窗口，校验的时候用
    dw_column = new DataWindow(document.getElementById("editForm"), "form");
    // fm_ini();

}

// window.onload=fn_ini;
function fm_ini() {
    var fm, i, j;
    for (i = 0; i < document.forms.length; i++) {
        fm = document.forms[i]
        for (j = 0; j < fm.length; j++) {
            // 这段是input框加入把回车替换成Tab的函数
            if (fm[j].tagName == "INPUT")
                if (fm[j].getAttribute("type").toLowerCase() != "button")
                    addKeyDownEvent(fm[j]);
        }

    }
}
/**
 * 把输入框的onkedown调用函数队列里加入回车键等于Tab键
 */
function addKeyDownEvent(iupt) {
    var oldpress = iupt.onkeydown;
    if (typeof iupt.onkeydown != "function") {
        iupt.onkeydown = jumpNext;
    } else {
        iupt.onkeydown = function() {
            oldpress();
            jumpNext();
        };
    }
}
function jumpNext() {
    if (event.keyCode == 13) {
        event.keyCode = 9;
    }

}

/**
 * 根据经办行联动
 */
function reSelect() {
    operReSelectCustMgr();
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

    var retxml = "";
    // 贷款管理
    document.getElementById("busiNode").value = BUSINODE_130;
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
