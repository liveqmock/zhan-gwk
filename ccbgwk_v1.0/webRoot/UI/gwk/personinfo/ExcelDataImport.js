var idTmr = "";
         /*
         * 关闭Excel进程 垃圾回收
         * */
          function Cleanup() {
            window.clearInterval(idTmr);
            CollectGarbage();
          }
        function isIdCardNo(num)
        {
            var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);
            var error;
            var varArray = new Array();
            var intValue;
            var lngProduct = 0;
            var intCheckDigit;
            var intStrLen = num.length;
            var idNumber = num;
            // initialize
            if (num != ""){
                if ((intStrLen != 15) && (intStrLen != 18)) {
//                alert("length error");
                return false;
                }
                // check and set value
                for(i=0;i<intStrLen;i++) {
                    varArray = idNumber.charAt(i);
                    if ((varArray < '0' || varArray > '9') && (i != 17)) {
//                        alert("char error!");
                        return false;
                    }
                }
                if (intStrLen == 18) {
                    //check date
                    var date8 = idNumber.substring(6,14);
                    date8 = date8.substring(0,4) + "-" + date8.substring(4,6) + "-" + date8.substring(6,8);
                    if (checkDate(date8) == false) {
//                        alert("date error!");
                        return false;
                    }
                    // calculate the sum of the products
                    for(i=0;i<17;i++) {
                        lngProduct = lngProduct + (idNumber.charAt(i))*factorArr[i];
                    }

                    // calculate the check digit
                    intCheckDigit = 12 - lngProduct % 11;
                    switch (intCheckDigit) {

                        case 10:
                            intCheckDigit = 'X';
                            break;
                        case 11:
                            intCheckDigit = 0;
                            break;
                        case 12:
                            intCheckDigit = 1;
                            break;
                    }
                    // check last digit

                    if (idNumber.charAt(17).toUpperCase() != intCheckDigit) {
//                        alert("last position error!");
                        return false;
                    }
                }
                else{        //length is 15
                    //check date
                    var date6 = idNumber.substring(6,12);
                    date6 = "19" + date6.substring(0,2) + "-" + date6.substring(2,4) + "-" + date6.substring(4,6);
                    if (checkDate(date6) == false) {
//                        alert("dade error --15type!");
                        return false;
                    }

                }
            }
            return true;

        }
        //Excel格式及数据正确性前台验证
        function ExcelFomatCheck(){

            var oXL = new ActiveXObject("Excel.Application");
            oXL.Visible=false;
            var oWB;
            var oSheet;
            var excelfile = document.getElementById("himport").value;
            var isRight = true;
            var errPeridLst = "";      //错误身份证号码list
            var FormatTitle = "文件表头不正确，请检查文件是否正确。";
//            alert(isRight);
            try{
                oWB = oXL.Workbooks.Open(excelfile,0,true);
                oSheet = oWB.Sheets(1);
//                alert(oSheet.cells(1,1).value);
                if (oSheet.cells(1,1).value != "姓名"){
                    alert(FormatTitle);
                    return false;
                }else if (oSheet.cells(1,2).value != "身份证号码"){
                    alert(FormatTitle);
                    return false;
                }else if(oSheet.cells(1,3).value != "预算单位编号"){
                    alert(FormatTitle);
                    return false;
                }else if (oSheet.cells(1,4).value != "创建者编号"){
                    alert(FormatTitle);
                    return false;
                }else if (oSheet.cells(1,5).value != "一级预算单位"){
                    alert(FormatTitle);
                    return false;
                }
                //身份证号码正确性验证
                var ExcelCount=oSheet.UsedRange.Rows.Count; //行数
                if (ExcelCount==1){
                    alert("Excel没有数据。");
                    return false;
                }
                for (var i = 2; i < ExcelCount; i++){
                    if (oSheet.cells(i,1).value.length > 6){
                        alert("姓名列字数不能超过六个，请重新检查数据！")
                        return false;
                    }else if (isNaN(oSheet.cells(i,4).value)){
                        alert("创建者编号必须为数字，请重新检查数据！");
                        return false;
                    }else if (!isIdCardNo(oSheet.cells(i,2).value)){
                        if (errPeridLst == "")
                            errPeridLst = oSheet.cells(i,2).value;
                        else
                            errPeridLst = errPeridLst + "," + oSheet.cells(i,2).value;
                    }
                }
                if (errPeridLst != ""){
                    document.getElementById("tabUpload").style.display = "none";
                    document.getElementById("divErrPerIDLst").style.display = "inline";
                    document.getElementById("pErrorPerIDLst").innerText = errPeridLst;
                    return false;

                }
                document.forms[0].submit();
//                var sfeature = "dialogwidth:800px; dialogheight:520px;center:yes;help:no;resizable:yes;scroll:yes;status:yes";
//                var arg1 = new Object();
//                alert(233);
//                var ret = dialog("ExcelDataImport.jsp",arg1 , sfeature);
//                alert(11111);
//                if (ret == "1") {
//                    document.getElementById("PerInfoTab").RecordCount = "0";
//                    Table_Refresh("PerInfoTab", false, body_resize);
//                }

            }catch(ex){
                alert(ex);
            }
            finally{
               //关闭Excel进程
               oXL.Quit();
               oXL = null;
               idTmr = window.setInterval("Cleanup();",1);
            }

        }
        //返回按钮单击事件
        function reback(){
            /* 个人信息查询同一个页面处理时用
            document.getElementById("tabUpload").style.display = "inline";
            document.getElementById("divErrPerIDLst").style.display = "none";
            document.getElementById("himport").outerHTML =  document.getElementById("himport").outerHTML;
            */
            document.location.href = "ExcelDataImport.html";    //单独页面时用
        }