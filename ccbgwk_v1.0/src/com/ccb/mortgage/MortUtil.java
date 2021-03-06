package com.ccb.mortgage;

import com.ccb.dao.LNTASKINFO;
import com.ccb.util.SeqUtil;
import pub.platform.db.DatabaseConnection;
import pub.platform.db.RecordSet;
import pub.platform.utils.BusinessDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MortUtil {

    /**
     * 计算抵押到期日
     *
     * @param p_releaseConCd 放款方式
     * @param p_mortDate     抵押日期
     * @param dc             数据库连接对象
     * @param p_loanid       抵押ID
     * @param p_center       抵押交易中心
     * @return
     */
    public static String getMortExpireDate(String p_releaseConCd, String p_mortDate, DatabaseConnection dc,
                                           String p_loanid, String p_center) {
        // 抵押到日期
        String mortExpireDate = "";
        // 放款方式
        String releaseConCD = p_releaseConCd;
        // 抵押接收日期
        String mortDate = p_mortDate;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = null;

        // 见回执放款
        // 组合回执放款
        // 抵押接收日期+2天=抵押到期日
        // if (releaseConCD.equals("02") || releaseConCD.equals("05")) {
        // calendar = new GregorianCalendar();
        // int year = Integer.parseInt(mortDate.substring(0, 4));
        // int month = Integer.parseInt(mortDate.substring(5, 7));
        // int day = Integer.parseInt(mortDate.substring(8, 10));
        // calendar.set(Calendar.YEAR, year);
        // calendar.set(Calendar.MONTH, month - 1);
        // calendar.set(Calendar.DAY_OF_MONTH, day);
        // calendar.add(Calendar.DATE, 2);
        // mortExpireDate = df.format(calendar.getTime());
        // }
        // 签约放款
        // 组合签约放款
        // 信用放款
        // 开户日期（贷款表的开户日期）+根据合作项目名称丛楼盘管理一览表中取得的办理时限(合作表：后续抵押办理时限（天）)=抵押到期日
        if (releaseConCD.equals("03") || releaseConCD.equals("06")) {
            // 取贷款表的开户日期
            String openDate = "";
            // 项目编号
            String projNo = "";
            // 后续抵押办理时限
            String mortPeriod = "";
            calendar = new GregorianCalendar();
            RecordSet rs = dc.executeQuery(" select CUST_OPEN_DT,PROJ_NO from LN_LOANAPPLY where loanid='" + p_loanid
                    + "'");
            if (rs.next()) {
                openDate = rs.getString("CUST_OPEN_DT");
                projNo = rs.getString("PROJ_NO");
                // 项目编号
                if (projNo == null || projNo.equals("")) {
                    return "";
                }
            }
            rs = dc.executeQuery("select FOLLOWUPMORTPERIOD from LN_COOPPROJ where proj_no='" + projNo + "'");
            // 后续办理时限
            if (rs.next()) {
                mortPeriod = rs.getString("FOLLOWUPMORTPERIOD");
                if (mortPeriod == null || mortPeriod.equals("")) {
                    return "";
                }
            }
            // rs关闭
            if (rs != null) {
                rs.close();
            }
            // 开户日期
            if (openDate == null || openDate.equals("")) {
                return "";
            } else {
                // 开户日期
                if (openDate.length() == 10) {
                    int year = Integer.parseInt(openDate.substring(0, 4));
                    int month = Integer.parseInt(openDate.substring(5, 7));
                    int day = Integer.parseInt(openDate.substring(8, 10));
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    if (!"".equals(mortPeriod)) {
                        calendar.add(Calendar.DATE, Integer.parseInt(mortPeriod));
                        mortExpireDate = df.format(calendar.getTime());
                    }
                }
            }
        }
        // 见回执
        // 组合回执
        // 见证放款的贷款
        // 组合见证放款
        // 用抵押接收日期+1+办理天数即为抵押到期日
        else if (releaseConCD.equals("01") || releaseConCD.equals("04") || releaseConCD.equals("02")
                || releaseConCD.equals("05")) {
            // 交易中心
            String mortCenterCD = p_center;

/*

            // 贷款种类
            String loanType = "";
            RecordSet rs = dc.executeQuery("select LN_TYP from LN_LOANAPPLY where loanid='" + p_loanid + "'");
            if (rs.next()) {
                loanType = rs.getString("LN_TYP");
            }
            // 办理天数
            String limitDate = "";
            // 办理天数贷款类型
            String limit_ln_typ = "";

            // 2010-3-10
            if (loanType != null) {
                if (loanType.equals("011") || loanType.equals("013")) { // 011个人住房贷款
                    // 013个人再交易住房贷款
                    // 个人住房贷款
                    limit_ln_typ = "001";
                } else {
                    // 非个人住房贷款
                    limit_ln_typ = "002";
                }
            } else {
                // 默认为个人住房贷款
                limit_ln_typ = "001";
            }

*/
/*
   20100403 zhan
   limit_ln_typ = "001" 的情况暂时不处理
*/
            // 办理天数
            String limitDate = "";

            // 办理天数贷款类型
            String limit_ln_typ = "002";
            RecordSet rs = dc.executeQuery("select LIMITDATE from LN_MORTLIMIT where LN_TYP='" + limit_ln_typ
                    + "' and MORTECENTERCD='" + mortCenterCD + "'");
            if (rs.next()) {
                limitDate = rs.getString("LIMITDATE");
            }
            // rs关闭
            if (rs != null) {
                rs.close();
            }
            // 办理时限
            if (limitDate == null || limitDate.equals("")) {
                return "";
            } else {
                // 抵押接收日期
                calendar = new GregorianCalendar();
                int year = Integer.parseInt(mortDate.substring(0, 4));
                int month = Integer.parseInt(mortDate.substring(5, 7));
                int day = Integer.parseInt(mortDate.substring(8, 10));
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.add(Calendar.DATE, 1);
                calendar.add(Calendar.DATE, Integer.parseInt(limitDate));
                mortExpireDate = df.format(calendar.getTime());
            }
        } else {
            // do nothing
        }
        return mortExpireDate;
    }

    /**
     * 返回日志流水对象
     *
     * @param busiKey  业务主键
     * @param busiNode 菜单ID
     * @param operType 操作类型(add、edit、delete)
     */
    public static LNTASKINFO getTaskObj(String busiKey, String busiNode, String operType) {
        // 写流水记录表
        LNTASKINFO task = new LNTASKINFO();
        try {
            // 流水ID
            task.setTaskid(SeqUtil.getTaskSeq());
            // 业务主键
            task.setLoan_id(busiKey);
            // 抵押信息登记、业务流程节点
            task.setTasktype(busiNode);
            // 抵押登记时间
            task.setTasktime(BusinessDate.getTodaytime());
            task.setRemarks(operType);
            task.setLoanrecordid(operType);
        } catch (Exception e) {
            // logger.error(e.getMessage());
            e.printStackTrace();
        }
        return task;
    }
}
