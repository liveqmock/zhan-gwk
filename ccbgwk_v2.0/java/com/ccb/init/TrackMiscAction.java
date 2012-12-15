package com.ccb.init;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

public class TrackMiscAction extends Action {
    private static final Log logger = LogFactory.getLog(TrackMiscAction.class);

    private String areacode = "";
    private String strRemark = "";
    private String strJudeFlag = "";

    /**
     * <p/>
     * 初始化查询
     * 1、自本地消费信息表中查询初始状态的数据记录数
     * 2、查询异常（发送超时和发现失败）的数据记录数
     * 3、查询还款信息表中的尚未还款记录数
     *
     * @return
     */
    public int initQuery() {
        //获取登录用户所属部门，只有分行的用户有权限查看其它支行的数据 2012-11-26
        areacode = this.getOperator().getDeptid();
        //获取备注里面的内容，如果是admin，有查看其它支行数据的权限 2012-11-26
        strRemark = this.getOperator().getFillstr150();
        //获取判断条件 2012-11-26
        strJudeFlag = PropertyManager.getProperty("pub.plat.admin.jude.flag");

        int count = 0;
        StringBuffer show_content = new StringBuffer();
        try {
            // 查询初始状态消费信息记录数
            count = queryStatusCnt("10");
            show_content.append(String.valueOf(count));
            // 查询发送失败和发送超时的消费信息记录数
            count = queryStatusArrCnt(new String[]{"11", "12"});
            show_content.append("_").append(String.valueOf(count));
            // 查询还款信息表中的尚未还款记录数
            count = queryNotPaybackCnt();
            show_content.append("_").append(String.valueOf(count));
            // 查询未发送卡基本信息记录数
            count = queryCardBaseInfos();
            show_content.append("_").append(String.valueOf(count));
            // 查询注销卡基本信息记录数
            count = queryCardBaseNullInfos();
            show_content.append("_").append(String.valueOf(count));

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        //返回初始状态的数据记录数_异常（发送超时和发现失败）的数据记录数_尚未还款记录数
        this.res.setFieldName("importData");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(new String(show_content));
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }

    // 查询公务卡未发送记录
    private int queryCardBaseInfos(){
        int count = 0;
        String selectsql = "select count(*) as sendCnt from ls_cardbaseinfo where status = '1' and sentflag = '0'";
        //当不具有管理员权限时，只能查看本支行的数据 2012-11-26
        if(!strJudeFlag.equals(strRemark)){
            selectsql = selectsql+" and areacode='"+areacode+"'";
        }
        RecordSet rs = null;
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            count = rs.getInt("sendCnt");
        }
        return count;
    }
     // 查询公务卡未发送记录
    private int queryCardBaseNullInfos(){
        int count = 0;
        String selectsql = "select count(*) as sendCnt from ls_cardbaseinfo where status = '128' and sentflag = '0'";
        //当不具有管理员权限时，只能查看本支行的数据 2012-11-26
        if(!strJudeFlag.equals(strRemark)){
            selectsql = selectsql+" and areacode='"+areacode+"'";
        }
        RecordSet rs = null;
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            count = rs.getInt("sendCnt");
        }
        return count;
    }
    // 查询所有某一状态的消费数据数

    private int queryStatusCnt(String status) {
        return queryStatusArrCnt(new String[]{status});
    }

    // 查询所有某几状态的消费数据数

    private int queryStatusArrCnt(String[] status) {

        int count = 0;
        StringBuffer wheresqlbfr = new StringBuffer(" where status in ('");
        int statusCount = status.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(status[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("')");
        //当不具有管理员权限时，只能查看本支行的数据
        if(!strJudeFlag.equals(strRemark)){
            wheresqlbfr.append(" and areacode='"+areacode+"'");
        }
        String wheresql = new String(wheresqlbfr);
        String selectsql = "select count(*) as statusCount from ls_consumeinfo " + wheresql;
        RecordSet rs = null;
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            count = rs.getInt("statusCount");
        }
        return count;
    }
    // 查询还款信息表中的尚未还款记录数

    private int queryNotPaybackCnt() {

        int count = 0;
        String selectsql = "select count(*) as notCount from ls_paybackinfo where status = '00'";
        //当不具有管理员权限时，只能查看本支行的数据 2012-11-26
        if(!strJudeFlag.equals(strRemark)){
            selectsql = selectsql+" and areacode='"+areacode+"'";
        }
        RecordSet rs = null;
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            count = rs.getInt("notCount");
        }
        return count;
    }
}
