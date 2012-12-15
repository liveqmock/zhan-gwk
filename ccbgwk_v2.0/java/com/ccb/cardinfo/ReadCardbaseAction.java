package com.ccb.cardinfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

/**
 * <p>Title: 后台业务组件</p>
 * <p/>
 * <p>Description: 后台业务组件</p>
 * <p/>
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * @author
 * @version 1.0
 */


public class ReadCardbaseAction extends Action {

    // 记录从odsb读取到的卡信息记录数
    private int odsbReadCrdTotalCnt = 0;

    // 记录从odsb读取到的更新数据记录数
    private int odsbReadUpdateCrdCnt = 0;

    // 记录从odsb读取到的新增数据记录数
    private int odsbReadNewCrdCnt = 0;

    // 记录从odsb读取到的新增数据记录数
    private int odsbReadNullCrdCnt = 0;

    // 记录每次发往财政局的数据记录数
    private int sendInfoCount = 0;

    private static final Log logger = LogFactory.getLog(ReadCardbaseAction.class);


    //4.2.4.1	公务卡信息接入
    /*
   	<list>
    <map>
    <!—卡号-->
    <string> ACCOUNT </string><string>billcode</string>
    <!—持卡人 -->
    <string> CARDNAME </string><string>2008</string>
    <!—预算单位-->
    <string> Bdgagency</string><string>value</string>
    <!—还款账户名-->
    <string> GATHERINGBANKACCTNAME </string><string>3</string>
    <!—还款账户开户行-->
    <string> GATHERINGBANKNAME </string><double>0.0</double>
	<!—还账户号-->
	<string> GATHERINGBANKACCTCODE </string><string>value</string>
	<!—身份证号-->
	<string> IDNUMBER </string><string>value</string>
	<!—用途-->
	<string> DIGEST </string><string>value</string>
	<!—开户银行-->
	<string> BANK</string><string>value</string>
	<!—开卡日期-->
	<string> CREATEDATE </string><string>value</string>
	<!—有效起始日期-->
	<string> Startdate</string><string>value</string>
	<!—有效终止日期-->
	<string>enddate</string><string>value</string>
	<!—数据操作类型	-->
	<string> action</string><string>value</string>
</map>
<!-- … -->
</list>
    */
    // 1、从odsb读取公务卡表(BF_AGT_CRD_CRT)读取数据保存到gwk.odsb_crd_crt表中
    // 2、对比更新gwk中ls_cardbaseinfo表中数据有无变化 若有则更改相应字段，并将action设置为修改('1')，发送状态(SENTFLAG)设置为未发送('0')
    // 3、若有新增字段，则写入ls_cardbaseinfo表，action为新增('0'),未发送状态
    // 4、若卡已注销，则将action设置为删除('2')，状态为未发送

    public int readCrdCrts() {
        try {
            // 从odsb读取公务卡表(BF_AGT_CRD_CRT)读取数据保存到gwk.odsb_crd_crt表中
            this.odsbReadCrdTotalCnt = insertAllCrdCrt();
        } catch (Exception e) {
            logger.error("读取ODSB公务卡信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("读取ODSB公务卡信息出现错误，请查看系统日志。");
            return -1;
        }
        try {
            // 获取新增公务卡信息
            this.odsbReadNewCrdCnt = checkNewCrdCrt();
        } catch (Exception e) {
            logger.error("处理ODSB新增公务卡信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("处理ODSB新增公务卡信息出现错误，请查看系统日志。");
            return -1;
        }
        try {
            // 处理注销公务卡信息
            this.odsbReadNullCrdCnt = checkNullCrdCrt();
        } catch (Exception e) {
            logger.error("处理ODSB已注销公务卡信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("处理ODSB已注销公务卡信息出现错误，请查看系统日志。");
            return -1;
        }
        try {
            this.odsbReadUpdateCrdCnt = checkUpdateCrdCrt();
        } catch (Exception e) {
            logger.error("更新公务卡信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("更新公务卡信息出现错误，请查看系统日志。");
            return -1;
        }
        StringBuffer show_content = new StringBuffer();
        show_content.append(this.odsbReadNewCrdCnt).append("_");
        show_content.append(this.odsbReadNullCrdCnt).append("_");
        show_content.append(this.odsbReadUpdateCrdCnt).append("_");
        this.res.setFieldName("importData");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(new String(show_content));
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }
    //从odsb读取公务卡表(BF_AGT_CRD_CRT)读取数据保存到gwk.odsb_crd_crt表中

    private int insertAllCrdCrt() throws Exception {
        String sql = " insert into odsb_crd_crt  " +
                " select * from  odsbdata.BF_AGT_CRD_CRT@odsb_remote  " +
                " where crd_no like '62836600%' ";
        int crd_rtn = 0;
        //dc.executeUpdate(" truncate table odsb_crd_crt ");
        crd_rtn = dc.executeUpdate(sql);
        return crd_rtn;
    }

    // 查看有无新增的用户，若有更新ls_cardbaseinfo的action字段，并发往财政局

    private int checkNewCrdCrt() throws Exception {
        int newCnt = 0;
        // 查找odsb读取的数据中的新增账户号
        String allSql = "select distinct occ.crd_no as account from odsb_crd_crt occ join ls_personalinfo gwy" +
                " on rtrim(occ.embosser_name3) = rtrim(gwy.perid) where occ.crd_no not in (select account from ls_cardbaseinfo)";
//        +                        " and occ.crd_sts in ('801','802')";     
        RecordSet rs = dc.executeQuery(allSql);
        // 处理新增数据的账户号
        if (rs != null) {
            StringBuffer accountBuffer = new StringBuffer();
            while (rs.next()) {
                accountBuffer.append("'");
                String newAccount = rs.getString("account");
                accountBuffer.append(newAccount);
                accountBuffer.append("'");
                if (!rs.isLast()) {
                    accountBuffer.append(",");
                }
            }
            String accounts = new String(accountBuffer).trim();
            if (!"".equals(accounts)) {
                // 将新增账户保存到ls_cardbaseinfo,字段gatheringbankacctname存放所属区域代码
                // 添加areacode字段 2012-12-14
                String insertCrdBsSql = "insert into ls_cardbaseinfo (select o.crd_no,p.pername,p.deptcode,p.areacode," +
                        "'建设银行','37101986827059123456',p.perid,'公务卡开卡',8015,o.open_card_dt,o.open_card_dt," +
                        "substr(o.open_card_dt,0,4)+3||substr(o.open_card_dt,5,6) as enddate ,'0',null,'AUTO',sysdate,'0'" +
                        ",null,to_char(sysdate,'yyyy-mm-dd'),to_char(sysdate,'HH24:MI:SS'),0,'1',p.areacode from ls_personalinfo p" +
                        " left join odsb_crd_crt o on p.perid = o.embosser_name3 where o.crd_no in (" + accounts + "))";
                newCnt = dc.executeUpdate(insertCrdBsSql);
                //插入记录成功以后，根据所属区域代码更新gatheringbankacctname和bank两个字段 2012-05-13 linyong
                //查询gatheringbankacctname是数字的记录
                // 添加areacode字段 2012-12-14
                String strSql = "select t.gatheringbankacctname,t.account,t.idnumber,t.areacode From ls_cardbaseinfo t "+
                        "where trim(translate(nvl(t.gatheringbankacctname,'X'),'0123456789',' '))is null ";
                rs = dc.executeQuery(strSql);
                //获取数据集以后进行遍历，更新
                if (rs!=null){
                    while(rs.next()){
                        strSql="update ls_cardbaseinfo set gatheringbankacctname='"+
                                PropertyManager.getProperty("finance.name." + rs.getString("areacode"))+
                                "',bank="+PropertyManager.getProperty("ccb.code."+rs.getString("areacode"))+
                                " where account='"+rs.getString("account")+"' and idnumber='"+rs.getString("idnumber")+"' ";
                        int rtn = dc.executeUpdate(strSql);
                    }
                }
            }
        }
        return newCnt;
    }

    // 查看有无注销的用户，若有更新ls_cardbaseinfo的action字段，并发往财政局

    private int checkNullCrdCrt() throws Exception {
        int nullCnt = 0;
        // 查找odsb读取的数据中的废弃账户号
        String sql = "select distinct occ.crd_no as account from odsb_crd_crt occ right join ls_personalinfo gwy" +
                " on occ.embosser_name3 = gwy.perid where" +
                " occ.crd_sts in ('809','810')";
        RecordSet rs = dc.executeQuery(sql);
        // 处理废弃账户号
        if (rs != null) {
            StringBuffer accountBuffer = new StringBuffer();
            while (rs.next()) {
                accountBuffer.append("'");
                String newAccount = rs.getString("account");
                accountBuffer.append(newAccount);
                accountBuffer.append("'");
                if (!rs.isLast()) {
                    accountBuffer.append(",");
                }
            }
            String accounts = new String(accountBuffer).trim();
            if (!"".equals(accounts)) {
                // 将注销账户保存到ls_cardbaseinfo
                String updateCrdBsSql = "update ls_cardbaseinfo l set l.action='2',l.status='128',l.sentflag='0'" +
                        " where l.account in (" + accounts + "))";
                nullCnt = dc.executeUpdate(updateCrdBsSql);
            }
        }
        return nullCnt;
    }

    // 选择崂山财政局的公务卡持卡人的卡信息，对比数据有无变化，修改ls_cardbaseinfo
    // TODO 卡基本信息对比

    private int checkUpdateCrdCrt() throws Exception {
        int updateCnt = 0;
        return updateCnt;
    }
}