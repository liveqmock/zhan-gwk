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
    divfd_loanTab.style.height = document.body.offsetHeight - 130;
    // divfd_loanTab.style.height = "350px";
    loanTab.fdwidth = "1700px";
    initDBGrid("loanTab");
    // ��ʼ��ҳ�潹��
    //body_init(queryForm, "cbRetrieve");
    // document.getElementById("MORTDATE").focus();
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
 */

function cbRetrieve_Click(formname) {
    // ����ϵͳ�����
    if (getSysLockStatus() == "1") {
        alert(MSG_SYSLOCK);
        return;
    }

    var comSql = ""
            // ��֤�ſ�δ����
            + " select b.MORTDATE,b.MORTID,"
            + " mortexpireDate,RELEASECONDCD,"
            + "(select enuitemlabel from ptenudetail where enutype='MORTECENTERCD' and enuitemvalue=b.MORTECENTERCD)as MORTECENTERCD,"
            + "(select enuitemlabel from ptenudetail where enutype='SENDFLAG' and enuitemvalue=b.SENDFLAG) as SENDFLAG,"
            + " (select deptname from ptdept where deptid=b.deptid) as deptname ";
    var comWhere = "" + " from ln_mortinfo b  where b.deptid in(select deptid from ptdept start with deptid='"
            + document.getElementById("deptid").value + "' connect by prior deptid=parentdeptid) ";

    var cautionType = document.getElementById("CAUTIONTYPE").value;
    var tempWhere2 = "";
    var tempWhere3 = "";
    var tempWhere4 = "";
    var sql2 = "";
    var sql3 = "";
    var sql4 = "";
    var totalSql = "";

    tempWhere2 = document.getElementById("tempWhere2").value + "and RELEASECONDCD in ('01')";
    tempWhere3 = document.getElementById("tempWhere3").value + "and RELEASECONDCD in ('02')";
    tempWhere4 = document.getElementById("tempWhere4").value + "and RELEASECONDCD in ('02')";
    sql2 = comSql + ",'��֤�ſ�δ����' as cautionType " + comWhere + tempWhere2;
    sql3 = comSql + ",'����ִ�ſ�δ��ִ' as cautionType " + comWhere + tempWhere3;
    sql4 = comSql + ",'����ִ�ſ�δ��֤' as cautionType " + comWhere + tempWhere4;

    if (cautionType == "") {
        totalSql = "select * from ((" + sql2 + ") union (" + sql3 + ") union (" + sql4
                + ")) order by cautionType,MORTEXPIREDATE,MORTDATE,MORTID ";
    }
    // ��֤�ſ�δ����
    else if (cautionType == "3") {
        totalSql = sql2 + " order by cautionType,MORTEXPIREDATE,MORTDATE,MORTID  ";
    }
    // ����ִ�ſ�δ��ִ
    else if (cautionType == "1") {
        totalSql = sql3 + " order by cautionType,MORTEXPIREDATE,MORTDATE,MORTID  ";
    }
    //����ִ�ſ�δ��֤
    else if (cautionType == "2") {
        totalSql = sql4 + " order by cautionType,MORTEXPIREDATE,MORTDATE,MORTID  ";
    }
    // whereStr += " order by mortid asc ";
    // document.all["loanTab"].whereStr = whereStr;
    document.all["loanTab"].SQLStr = totalSql;
    document.all["loanTab"].RecordCount = "0";
    document.all["loanTab"].AbsolutePage = "1";
    // Table_Refresh in dbgrid.js pack
    Table_Refresh("loanTab", false, body_resize);
}

/**
 * 
 * @return
 */
function loanTab_TRDbclick() {

}
