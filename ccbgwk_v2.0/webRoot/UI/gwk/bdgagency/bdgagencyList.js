//////初始化信息
var menu;

function body_load()
{

	//divfd_DbgagencyTable.style.height="100%";
    divfd_DbgagencyTable.style.height = document.body.clientHeight - 235 + "px";
    //divfd_DbgagencyTable.style.width="100%";
    DbgagencyTable.fdwidth = "1200px";
     body_init(queryForm, "cbRetrieve");
    initDBGrid("DbgagencyTable");

}


/////主数据表格TD的单击事件
function DeveloperTable_TDclick(el){


	el = event.srcElement;

	var trobj =getOwnerTR(el);

	if (trobj.edit=="true")
	{
		///根据数据类型生成添加对象
		if (el.fieldtype=="text")
		{

               CreateText(el);
		}
		if (el.fieldType=="number")
		{
			CreateNumberText(el);
		}
	}


}


////////查询函数
function cbRetrieve_Click(){

	var  whereStr ="";

	if (trimStr(document.all["code"].value) != "")
		whereStr += " and ( code like '"+document.all.code.value+"%')";

	if (trimStr(document.all["name"].value) != "")
		whereStr += " and ( name like '%"+document.all.name.value+"%')";

	if (trimStr(document.all["levelno"].value) != "")
		whereStr += " and ( levelno = '"+document.all.levelno.value+"')";

     if (trimStr(document.all["supercode"].value) != "")
		whereStr += " and ( supercode like '"+document.all.supercode.value+"%')";

    if (trimStr(document.all["isleaf"].value) != "")
		whereStr += " and ( isleaf = '"+document.all.isleaf.value+"')";
    
    if (trimStr(document.all["areacode"].value) != "")
		whereStr += " and ( areacode = '"+document.all.areacode.value+"' )";
    
	if (whereStr !=document.all["DbgagencyTable"].whereStr){
		document.all["DbgagencyTable"].whereStr=whereStr +" order by code ";
		document.all["DbgagencyTable"].RecordCount="0";
		document.all["DbgagencyTable"].AbsolutePage="1";


		Table_Refresh("DbgagencyTable");
    }

}
/**
 * 察看详细函数
 */
function DbgagencyTable_query_click() {

    //增加系统锁检查
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var sfeature = "dialogwidth:800px; dialogheight:550px;center:yes;help:no;resizable:yes;scroll:yes;status:no";
    var tab = document.all["DbgagencyTable"];
    var trobj = tab.rows[tab.activeIndex];
    var levelNo = "";
    var deptCode = "";


    if (tab.rows.length <= 0) {
        alert(MSG_NORECORD);
        return;
    }

    if ((trobj.ValueStr != undefined) && (trobj.ValueStr != "")) {
        var tmp = trobj.ValueStr.split(";");
        levelNo = tmp[3];
        /*for(var i=0;i<tmp.length;i++){
          alert("tmp["+i+"]="+tmp[i]);
        }*/
        deptCode = tmp[0];

    }
    var arg = new Object();
    arg.doType = "select";
    document.location.href="../personinfo/PerInfoManagement.jsp?deptCode="+deptCode+"&levelNo="+levelNo;
    //打开的页面
    //dialog("PerInfoMana.jsp?doType=select&code="+deptCode+"&levelno="+levelNo, arg, sfeature);
}
/**
 * 双击表格弹出详细信息查看画面 调用查看函数
 */
function DbgagencyTable_TRDbclick() {
    DbgagencyTable_query_click();
}
