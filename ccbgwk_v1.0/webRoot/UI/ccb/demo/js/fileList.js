
//����DataWindow����
var lcdw;
var SQLStr;
var gWhereStr="";

/**
 * ��ʼ��form����
 *
 */
function body_resize()
{

	//itemTab.fdwidth="100%";
	//divfd_itemTab.style.height=document.body.offsetHeight-200;
	divfd_itemTab.style.height="100%";
	initDBGrid("itemTab" );
	body_init(queryForm,"cbRetrieve");
}

/*
*/

function cbRetrieve_Click( formname )
{
	 if(checkForm(queryForm)=="false")
        return ;
     //var sql="select  file_id,file_name,file_date,file_lastdate,isvisibled,file_type from aic_file_info where 1=1 ";
     var  whereStr ="";    
     if(trimStr(document.all.file_name.value)!="")
		whereStr+=" and (file_name like'%"+trimStr(document.all.file_name.value)+"%')";
	 if(trimStr(document.all.file_type.value)!="")
		whereStr+=" and (file_type ='"+trimStr(document.all.file_type.value)+"')";
			

     whereStr += " order by file_date desc ";
     
     //document.all["itemTab"].SQLStr=sql;
     document.all["itemTab"].whereStr = whereStr;
     document.all["itemTab"].RecordCount = "0";
     document.all["itemTab"].AbsolutePage = "1";
    //Table_Refresh in dbgrid.js pack
     Table_Refresh("itemTab",false,body_resize);
}

/*
* operate ����������add
*/
function itemTab_appendRecod_click()
{
	
	var sfeature = "dialogwidth:800px; dialogheight:600px;center:yes;help:no;resizable:yes;scroll:no;status:no";		 
	var arg=new Object();	
       
	arg.doType="add";
	var ret = dialog("fileEdit.jsp?doType=add",arg,sfeature);
	
    if(ret=="1"){ 
       document.getElementById("itemTab").RecordCount="0";
	   Table_Refresh("itemTab",false,body_resize);	   
    }
    
    //alert(ret);
    
    //document.getElementById("itemTab").RecordCount="0";
	//Table_Refresh("itemTab",false,body_resize);	  	
}

/*�༭����
* ���ݲ�����
* nbxh :�ڲ����
* qymc ����ҵ����
* read : ���ڸ���tabҳ���ѯ�����ݣ��Ƿ���Ը��ģ�ֻ��read=yes �༭��read=no��
* operate : edit
*/
function itemTab_editRecord_click(){


     var sfeature = "dialogwidth:800px; dialogheight:600px;center:yes;help:no;resizable:yes;scroll:no;status:no";		 
     var tab = document.all["itemTab"];
     var trobj = tab.rows[tab.activeIndex];

     var fileId="";

 	
	 if(tab.rows.length<=0){
		 alert("û�п��Բ����ļ�¼��");
		 return;
	 }

     if ((trobj.ValueStr !=undefined)&&(trobj.ValueStr!="")){
          var tmp = trobj.ValueStr.split(";");
          fileId=tmp[0];
             
     }

     if ((trobj.whStr !=undefined)&&(trobj.whStr!=""))
     {
          var tmp = trobj.whStr.split("&");
          var arg = new Object();
          arg.key = tmp[2];
          
         
           var arg=new Object();
	       arg.doType="edit";
	       var rtn=dialog("fileEdit.jsp?fileId="+fileId+"&doType=edit",arg,sfeature);
	       if(rtn=="1"){ 
		       document.getElementById("itemTab").RecordCount="0";
			   Table_Refresh("itemTab",false,body_resize);	   
		    }
     }
     
    // document.getElementById("itemTab").RecordCount="0";
    // Table_Refresh("itemTab",false,body_resize);
}


/*�쿴����
* ���ݲ�����
* nbxh : �ڲ����
* qymc ����ҵ����
* read : ���ڸ���tabҳ���ѯ�����ݣ��Ƿ���Ը��ģ�ֻ��read=yes �༭��read=no��
*/
function itemTab_query_click(){

	
     var sfeature = "dialogwidth:800px; dialogheight:600px;center:yes;help:no;resizable:yes;scroll:no;status:no";		 
     var tab = document.all["itemTab"];
     var trobj = tab.rows[tab.activeIndex];
     var fileId="";

   	 if(tab.rows.length<=0){
		 alert("û�п��Բ����ļ�¼��");
		 return;
	 }

     if ((trobj.ValueStr !=undefined)&&(trobj.ValueStr!="")){
          var tmp = trobj.ValueStr.split(";");
          fileId=tmp[0];
  

     }       
     var arg=new Object();
     arg.doType="select";
     dialog("fileEdit.jsp?fileId="+fileId+"&readOnly=yes&doType=select",arg,sfeature);

}

function itemTab_TRDbclick(){
	itemTab_query_click();
}


/*ɾ������
* ���ݲ�����
* nbxh :�ڲ����
* qymc ����ҵ����
* read : ���ڸ���tabҳ���ѯ�����ݣ��Ƿ���Ը��ģ�ֻ��read=yes �༭��read=no��
* operate : 
*/
function itemTab_deleteRecord_click(){
     var tab = document.all["itemTab"];
     var trobj = tab.rows[tab.activeIndex];

     var fileId="";
     var fileName="";

	 if(tab.rows.length<=0){
		 alert("û�п��Բ����ļ�¼��");
		 return;
	 }
		 

     if ((trobj.ValueStr !=undefined)&&(trobj.ValueStr!="")){
          var tmp = trobj.ValueStr.split(";");
          fileId=tmp[0];
          fileName=tmp[1];

     }    
         
	if(confirm("ȷ��ɾ�����ļ���\r\n�ļ����ƣ�"+fileName)){
       document.all.fileId.value=fileId;
	   var retxml=createExecuteform(queryForm,"update","pub007","delete")
	   if (analyzeReturnXML(retxml)!="false"){	
			alert("ɾ����¼�ɹ���");
			document.getElementById("itemTab").RecordCount="0";
			//document.getElementById("itemTab").AbsolutePage="1";
			Table_Refresh("itemTab",false,body_resize);
	   }			  
	}
     
}
