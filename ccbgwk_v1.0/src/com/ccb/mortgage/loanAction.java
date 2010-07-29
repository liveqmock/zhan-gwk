package com.ccb.mortgage;

/**
 * <p>Title: 后台业务组件</p>
 *
 * <p>Description: 后台业务组件</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: 公司</p>
 *
 * @author leonwoo
 * @version 1.0
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.DBUtil;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.BusinessDate;

import com.ccb.dao.LNLOANAPPLY;
import com.ccb.dao.LNTASKINFO;
import com.ccb.util.CcbLoanConst;
import com.ccb.util.SeqUtil;

public class loanAction extends Action {
    // 日志对象
    private static final Log logger = LogFactory.getLog(loanAction.class);
    // 贷款信息对象
    LNLOANAPPLY              loan   = null;
    // 流水日志表
    LNTASKINFO               task   = null;

    /**
     * <p>
     * 贷款信息增加接口
     * <p>
     * 成功或失败均返回消息
     * <p>
     * 部门id、用户id、操作时间均在后台赋值
     * 
     * @return
     */
    public int add() {
        loan = new LNLOANAPPLY();
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                // 检测贷款序号有无重复
                RecordSet rec = dc.executeQuery("select 1 from ln_loanapply where loanid='"
                        + req.getFieldValue(i, "loanid").trim() + "'");
                while (rec.next()) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("303") + "\r\n贷款申请序号："
                            + req.getFieldValue(i, "loanid").trim());
                    return -1;
                }
                if (rec != null) {
                    rec.close();
                }

                // 项目编号需要检查，是否在合作项目表中存在；
                if (!req.getFieldValue(i, "PROJ_NO").trim().equals("")) {
                    rec = dc.executeQuery(" select count(1) as count from LN_COOPPROJ where PROJ_NO='"
                            + req.getFieldValue(i, "PROJ_NO").trim() + "'");
                    while (rec.next()) {
                        if (rec.getInt(0) == 0) {
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage(PropertyManager.getProperty("306") + "\r\n项目编号："
                                    + req.getFieldValue(i, "PROJ_NO").trim());
                            return -1;
                        }
                    }
                    if (rec != null) {
                        rec.close();
                    }
                }
                // 初始化数据bean
                loan.initAll(i, req);
                // 部门id
                loan.setDeptid(this.getDept().getDeptid());
                // 操作时间
                loan.setOperdate(BusinessDate.getToday());
                // 用户id
                loan.setOperid2(this.getOperator().getOperid());
                // 版本号
                loan.setRecversion(0);
                // 主键内部序号
                String nbxh = SeqUtil.getNbxh();
                loan.setNbxh(nbxh);
                // 数据是否待补充,1:新增不完整
                loan.setNeedaddcd("1");
                // 汉字对应拼音
                loan.setCust_py(DBUtil.getSpell(this.dc, req.getFieldValue(i, "cust_name")));
                if (loan.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // 流水日志表
                task = MortUtil.getTaskObj(nbxh, req.getFieldValue(i, "busiNode"), CcbLoanConst.OPER_ADD);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            } catch (Exception ex1) {
                logger.error(ex1.getMessage());
                ex1.printStackTrace();
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        }
        this.res.setType(0);
        this.res.setResult(true);
        this.res.setMessage(PropertyManager.getProperty("200"));
        return 0;
    }

    /**
     * <p>
     * 编辑接口
     * <p>
     * 除了更新页面上的值之外，用户id、操作时间也一起更新；
     * <p>
     * 更新前进行版本号检查，控制并发问题
     * 
     * @return
     */
    public int edit() {

        loan = new LNLOANAPPLY();
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {

                // 项目编号需要检查，是否在合作项目表中存在；
                if (!req.getFieldValue(i, "PROJ_NO").trim().equals("")) {
                    RecordSet rec = dc.executeQuery(" select count(1) as count from LN_COOPPROJ where PROJ_NO='"
                            + req.getFieldValue(i, "PROJ_NO").trim() + "'");
                    while (rec.next()) {
                        if (rec.getInt(0) == 0) {
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage(PropertyManager.getProperty("306") + "\r\n项目编号："
                                    + req.getFieldValue(i, "PROJ_NO").trim());
                            return -1;
                        }
                    }
                    if (rec != null) {
                        rec.close();
                    }
                }
                // 初始化数据bean
                loan.initAll(i, req);
                // 操作时间
                loan.setOperdate(BusinessDate.getToday());
                // 部门id
                loan.setDeptid(this.getDept().getDeptid());
                // 用户id
                loan.setOperid2(this.getOperator().getOperid());
                // 汉字对应拼音
                loan.setCust_py(DBUtil.getSpell(this.dc, req.getFieldValue(i, "cust_name")));
                // 更新前版本号
                int iBeforeVersion = 0;
                if (req.getFieldValue(i, "recVersion") != null && !req.getFieldValue(i, "recVersion").equals("")) {
                    iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                }
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from ln_loanapply where nbxh='"
                        + req.getFieldValue(i, "nbxh") + "'");
                while (rs.next()) {
                    iAfterVersion = rs.getInt("recVersion");
                    if (iBeforeVersion != iAfterVersion) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("301"));
                        return -1;
                    } else {
                        // 版本号加1
                        iBeforeVersion = iBeforeVersion + 1;
                        loan.setRecversion(iBeforeVersion);
                    }
                }
                if (rs != null) {
                    rs.close();
                }

                if (loan.updateByWhere(" where nbxh='" + req.getFieldValue(i, "nbxh") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // 流水日志表
                task = MortUtil.getTaskObj(req.getFieldValue(i, "nbxh"), req.getFieldValue(i, "busiNode"),
                        CcbLoanConst.OPER_EDIT);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            } catch (Exception ex1) {
                logger.error(ex1.getMessage());
                ex1.printStackTrace();
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        }
        this.res.setType(0);
        this.res.setResult(true);
        this.res.setMessage(PropertyManager.getProperty("200"));
        return 0;
    }

    /**
     * <p>
     * 删除接口
     * <p>
     * 删除一条记录
     */
    public int delete() {
        loan = new LNLOANAPPLY();
        try {
            for (int i = 0; i < this.req.getRecorderCount(); i++) {
                loan.initAll(i, req);
                // 删除信息
                if (loan.deleteByWhere(" where nbxh='" + req.getFieldValue(i, "nbxh") + "' ") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // 流水日志表
                task = MortUtil.getTaskObj(req.getFieldValue(i, "nbxh"), req.getFieldValue(i, "busiNode"),
                        CcbLoanConst.OPER_DEL);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        this.res.setType(0);
        return 0;
    }

    /**
     * 认领
     */
    public int confirmCLM() {
        loan = new LNLOANAPPLY();
        try {
            for (int i = 0; i < this.req.getRecorderCount(); i++) {
                // loan.initAll(i, req);
                loan.setCustmgr_id(this.getOperator().getOperid());
                // ---版本号检测--
                int iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from ln_loanapply where nbxh='"
                        + req.getFieldValue(i, "nbxh") + "'");
                while (rs.next()) {
                    iAfterVersion = rs.getInt("recVersion");
                    if (iBeforeVersion != iAfterVersion) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("301"));
                        return -1;
                    } else {
                        // 版本号加1
                        iBeforeVersion = iBeforeVersion + 1;
                        loan.setRecversion(iBeforeVersion);
                    }
                }
                if (rs != null) {
                    rs.close();
                }
                // ---版本号检测--
                // 认领信息
                if (loan.updateByWhere(" where nbxh='" + req.getFieldValue(i, "nbxh") + "' ") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // 流水日志表
                task = MortUtil.getTaskObj(req.getFieldValue(i, "nbxh"), req.getFieldValue(i, "busiNode"),
                        CcbLoanConst.OPER_CLM);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        this.res.setType(0);
        this.res.setResult(true);
        this.res.setMessage(PropertyManager.getProperty("200"));
        return 0;
    }

    /**
     * 退回
     */
    public int cancelCLM() {
        loan = new LNLOANAPPLY();
        try {
            for (int i = 0; i < this.req.getRecorderCount(); i++) {
                // loan.initAll(i, req);
                loan.setCustmgr_id("");
                // ---版本号检测--
                int iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from ln_loanapply where nbxh='"
                        + req.getFieldValue(i, "nbxh") + "'");
                while (rs.next()) {
                    iAfterVersion = rs.getInt("recVersion");
                    if (iBeforeVersion != iAfterVersion) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("301"));
                        return -1;
                    } else {
                        // 版本号加1
                        iBeforeVersion = iBeforeVersion + 1;
                        loan.setRecversion(iBeforeVersion);
                    }
                }
                if (rs != null) {
                    rs.close();
                }
                // ---版本号检测--
                // 认领信息
                if (loan.updateByWhere(" where nbxh='" + req.getFieldValue(i, "nbxh") + "' ") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // 流水日志表
                task = MortUtil.getTaskObj(req.getFieldValue(i, "nbxh"), req.getFieldValue(i, "busiNode"),
                        CcbLoanConst.OPER_BACK);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        this.res.setType(0);
        this.res.setResult(true);
        this.res.setMessage(PropertyManager.getProperty("200"));
        return 0;
    }
}
