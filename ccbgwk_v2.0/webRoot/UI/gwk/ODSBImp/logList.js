/*******************************************************************************
 * ��������: ϵͳ��־ <br>
 * �� ��: leonwoo <br>
 * ��������: 2010/01/16<br>
 * �� �� ��:<br>
 * �޸�����: <br>
 * �� Ȩ: ��˾
 ******************************************************************************/

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
    divfd_gwkTab.style.height = document.body.clientHeight - 180 + "px";
    gwkTab.fdwidth = "1000px";
    initDBGrid("gwkTab");
    // ��ʼ��ҳ�潹��
    body_init(queryForm, "cbRetrieve_Click");
}

/**
 * ���ݾ���������������Ŀ:����Ա
 * 
 * @return
 */
//function reSelect() {
//    refresh_select("operid", "select OPERID as value ,OPERNAME as text  from ptoper where" + " deptid='"
//            + document.getElementById("BANKID").value + "'");
//    var _cell = document.getElementById("operid");
//    // ����Ĭ�Ͽ�ֵ
//    var _option = document.createElement("option");
//    _option.value = "";
//    _option.text = "";
//    _cell.appendChild(_option);
//    _cell.value = "";
//}

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
 */

function cbRetrieve_Click(formname) {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    if (checkForm(queryForm) == "false")
        return;
    var whereStr = "";
    if (trimStr(document.getElementById("TASKTIME").value) != "") {
        whereStr += " and TASKTIME >=to_date('" + trimStr(document.getElementById("TASKTIME").value) + "','YYYY-MM-DD HH24:mi:ss')";
    }
    if (trimStr(document.getElementById("TASKTIME2").value) != "") {
        whereStr += " and TASKTIME <=to_date('" + trimStr(document.getElementById("TASKTIME2").value) + "','YYYY-MM-DD HH24:mi:ss')";
    }
    if (trimStr(document.getElementById("dbtabName").value) != "") {
        whereStr += " and tablename like '%" + trimStr(document.getElementById("dbtabName").value) + "%'";
    }
    whereStr += " order by t.tablename desc";
    
    document.all["gwkTab"].whereStr = whereStr;
    document.all["gwkTab"].RecordCount = "0";
    document.all["gwkTab"].AbsolutePage = "1";
    // Table_Refresh in dbgrid.js pack
    Table_Refresh("gwkTab");
}
