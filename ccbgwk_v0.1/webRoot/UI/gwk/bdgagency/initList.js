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
    divfd_ActionTable.style.height = document.body.clientHeight - 225 +"px";
    ActionTable.fdwidth="100%";

    initDBGrid("ActionTable");
    // ��ʼ��ҳ�潹��
    //    body_init(queryForm, "cbRetrieve");
    body_init(queryForm, "queryClick");
//    document.getElementById("cusidt").focus();
//    document.getElementById("cusidt").select();
}

function datatable_refresh() {
}


////////��ѯ����
function queryClick() {

    var whereStr = "";

    var retxml = createExecuteform(queryForm, "insert", "qcorp1", "querySBS");

    if (analyzeReturnXML(retxml) != "false") {
//        alert("��ȡSBS��¼�ɹ�...");
    }else{
        alert("��ȡSBS��¼ʧ��...");
        return;
    }
    document.all["ActionTable"].RecordCount = "0";
    document.all["ActionTable"].AbsolutePage = "1";


    if (whereStr !=document.all["ActionTable"].whereStr){
        document.all["ActionTable"].whereStr=whereStr +" order by id asc ";
        document.all["ActionTable"].RecordCount="0";
        document.all["ActionTable"].AbsolutePage="1";
    }

    Table_Refresh("ActionTable", false, datatable_refresh);
//    		Table_Refresh("ActionTable");
}

