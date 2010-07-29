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
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.BusinessDate;

import com.ccb.dao.LNLOANAPPLY;
import com.ccb.dao.LNMORTINFO;
import com.ccb.dao.LNTASKINFO;
import com.ccb.dao.SYSSEQDISCARD;
import com.ccb.util.CcbLoanConst;
import com.ccb.util.SeqUtil;

public class mortgageAction extends Action {
    // 日志对象
    private static final Log logger = LogFactory.getLog(mortgageAction.class);
    // 抵押信息对象
    LNMORTINFO mortInfo = null;
    // 废号管理对象
    SYSSEQDISCARD seqInfoDiscard = null;
    // 贷款信息对象
    LNLOANAPPLY loan = null;
    // 流水记录表
    LNTASKINFO task = null;

    /**
     * <p/>
     * 抵押信息增加接口
     * <p/>
     * 成功或失败均返回消息
     * <p/>
     * 部门id、用户id、操作时间均在后台赋值
     *
     * @return
     */
    public int add() {
        mortInfo = new LNMORTINFO();
        // 取出抵押编号
        String mortID = "";
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {

                // 初始化数据bean
                mortInfo.initAll(i, req);
                // 部门id
                mortInfo.setDeptid(this.getDept().getDeptid());
                // 操作时间
                mortInfo.setOperdate(BusinessDate.getToday());
                // 用户id
                mortInfo.setOperid(this.getOperator().getOperid());
                // 登记状态
                // mortInfo.setMortstatus(CcbLoanConst.NODE_DOING);
                mortInfo.setMortstatus(CcbLoanConst.MORT_FLOW_REGISTED);
                // 未办妥抵押原因 默认值：其他
                mortInfo.setNomortreasoncd("99");
                // 抵押登记状态 默认为：未登记：1
                mortInfo.setMortregstatus("1");
                // 版本号
                mortInfo.setRecversion(0);

                // 抵押到日期
                // String mortExpireDate = getMortExpireDate(req);
                String releasecondcd = req.getFieldValue(i, "RELEASECONDCD");
                String mortExpireDate = MortUtil.getMortExpireDate(releasecondcd, req
                        .getFieldValue(i, "MORTDATE"), dc, req.getFieldValue(i, "loanID"), req.getFieldValue(i,
                        "MORTECENTERCD"));

                // TODO:抵押到期日取不出情况下，保存空值
                // 抵押到期日生成失败
                // if ("".equals(mortExpireDate)) {
                // this.res.setType(0);
                // this.res.setResult(false);
                // this.res.setMessage(PropertyManager.getProperty("305"));
                // return -1;
                // }

                mortInfo.setMortexpiredate(mortExpireDate);

                //20100403 zhan
                //如果为签约类 同时更新抵押超批复日期
                if (releasecondcd.equals("03") || releasecondcd.equals("06")) {
                    mortInfo.setMortoverrtndate(mortExpireDate);
                }

                // 是否从废号表取值
                boolean discardFlg = false;

                /*
                20100423 zhan  取消废号管理

                RecordSet rs = dc.executeQuery("select discardno from sys_seq_discard where bhlx='"
                        + CcbLoanConst.MORTTYPE + "' and useflg='0' order by discardno asc ");
                if (rs.next()) {
                    discardFlg = true;
                    mortID = rs.getString("discardno");
                } else {
                    mortID = SeqUtil.getMortID();
                }
                if (rs != null) {
                    rs.close();
                }
                */
                //20100423 zhan 取消废号管理 直接取号
                mortID = SeqUtil.getMortID();

                mortInfo.setMortid(mortID);

                if (mortInfo.insert() < 0) {
                    // ------已生成的抵押编号进废号管理表--------
                    seqInfoDiscard = new SYSSEQDISCARD();
                    // 使用标志：未使用
                    seqInfoDiscard.setUseflg(0);
                    // 贷款申请序号
                    seqInfoDiscard.setNseqno(req.getFieldValue(i, "loanid"));
                    // 进表时间
                    seqInfoDiscard.setIndate(BusinessDate.getNowDay());
                    // 进表操作人员
                    seqInfoDiscard.setInoperid(this.getOperator().getOperid());
                    // 抵押编号
                    seqInfoDiscard.setDiscardno(mortID);
                    // 废号码类型
                    seqInfoDiscard.setBhlx(CcbLoanConst.MORTTYPE);
                    // 进表类型
                    seqInfoDiscard.setDotype("add");

                    if (seqInfoDiscard.insert() < 0) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("300"));
                        return -1;
                    }

                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }

                // 判断该序号是否从废弃表取得，如果是则更新废号管理表
                if (discardFlg) {
                    seqInfoDiscard = new SYSSEQDISCARD();
                    // 使用标志：已使用
                    seqInfoDiscard.setUseflg(1);
                    seqInfoDiscard.setNseqno(req.getFieldValue(i, "loanid"));
                    seqInfoDiscard.setUsedate(BusinessDate.getNowDay());
                    seqInfoDiscard.setUseoperid(this.getOperator().getOperid());
                    // 该处更新条件可以更唯一
                    if (seqInfoDiscard.updateByWhere(" where  bhlx='" + CcbLoanConst.MORTTYPE + "' and discardno='"
                            + mortID + "'") < 0) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("300"));
                        return -1;
                    }
                }
                // 更新贷款信息表
                loan = new LNLOANAPPLY();
                loan.init(i, req);
                if (loan.updateByWhere(" where loanid='" + req.getFieldValue(i, "loanid") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // 流水日志表
                task = MortUtil.getTaskObj(req.getFieldValue(i, "loanid"), CcbLoanConst.BUSINODE_010,
                        CcbLoanConst.OPER_ADD);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }

            } catch (Exception ex1) {
                ex1.printStackTrace();
                logger.error(ex1.getMessage());
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        }
        // this.res.setType(0);
        // this.res.setResult(true);
        // this.res.setMessage(PropertyManager.getProperty("200"));
        // return 0;

        this.res.setFieldName("mortID");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(mortID);
        this.res.setType(4);
        this.res.setResult(true);
        return 0;

    }

    /**
     * <p/>
     * 抵押信息编辑接口
     * <p/>
     * 除了更新页面上的值之外，用户id、操作时间也一起更新；
     * <p/>
     * 更新前进行版本号检查，控制并发问题
     *
     * @return
     */
    public int edit() {

        mortInfo = new LNMORTINFO();
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                // 初始化数据bean
                mortInfo.initAll(i, req);
                /*
                 * // ---根据未办理抵押原因进行状态修改--- // 贷款已结清、已撤卷 抵押登记状态改为【贷款结清取证登记】已结清取证
                 * if (req.getFieldValue(i, "NOMORTREASONCD") != null) { if
                 * (req.getFieldValue(i, "NOMORTREASONCD").equals("08") ||
                 * req.getFieldValue(i, "NOMORTREASONCD").equals("17")) { //
                 * 已结清取证 mortInfo.setMortstatus("50"); } }
                 */
                // ---根据未办理抵押原因进行状态修改---
                // 贷款已结清、已撤卷 抵押登记状态改为【贷款结清取证登记】已结清取证
                if (req.getFieldValue(i, "NOMORTREASONCD") != null) {
                    if (req.getFieldValue(i, "NOMORTREASONCD").equals("08")) {
                        // 未抵押贷款已结清
                        mortInfo.setMortstatus(CcbLoanConst.MORT_FLOW_NOMORT_CLEARED);
                    } else if (req.getFieldValue(i, "NOMORTREASONCD").equals("17")) {
                        // 已撤卷
                        mortInfo.setMortstatus(CcbLoanConst.MORT_FLOW_NOMORT_GETBOOK);
                    }
                }

                //20100403  zhan   田琨
                //在处理他行开发贷不可报抵押时 同时将“未办理抵押原因” 修改为 “他行开发贷”:04
                String sendflag = req.getFieldValue(i, "SENDFLAG");
                if (sendflag != null) {
                    //可能为空字符串，或为“0”，或为“1”
                    if (sendflag.equals("0")) {
                        mortInfo.setNomortreasoncd("04");
                    } else if (sendflag.equals("1")) {
                        mortInfo.setNomortreasoncd("");
                    }
                }

                // 操作时间
                mortInfo.setOperdate(BusinessDate.getToday());
                // 用户id
                mortInfo.setOperid(this.getOperator().getOperid());
                // 更新前版本号
                int iBeforeVersion = 0;
                if (req.getFieldValue(i, "recVersion") != null && !req.getFieldValue(i, "recVersion").equals("")) {
                    iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                }
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from ln_mortinfo where mortid='"
                        + req.getFieldValue(i, "mortid") + "'");
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
                        mortInfo.setRecversion(iBeforeVersion);
                    }
                }
                if (rs != null) {
                    rs.close();
                }

                if (mortInfo.updateByWhere(" where mortid='" + req.getFieldValue(i, "mortid") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // 流水日志表
                task = MortUtil.getTaskObj(req.getFieldValue(i, "loanid"), req.getFieldValue(i, "busiNode"),
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
                ex1.printStackTrace();
                logger.error(ex1.getMessage());
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
     * <p/>
     * 删除接口
     * <p/>
     * 删除一条记录后，抵押编号要保存进废弃号码管理表，以备下次继续使用
     */
    public int delete() {
        seqInfoDiscard = new SYSSEQDISCARD();

        try {
            // 抵押编号
            String mortID = req.getFieldValue("mortID");
            // 抵押编号得不到的情况下提示操作失败
            if (mortID == null || mortID.equalsIgnoreCase("null")) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }

            /** 抵押编号保存进废弃号码管理表 */
            // 编号类型
            seqInfoDiscard.setBhlx(CcbLoanConst.MORTTYPE);
            // 抵押编号
            seqInfoDiscard.setDiscardno(mortID);
            // 使用标志: 未使用状态；
            seqInfoDiscard.setUseflg(0);
            // 原关系业务序号:贷款申请序号
            seqInfoDiscard.setOseqno(req.getFieldValue("loanID"));
            // 进表日期
            seqInfoDiscard.setIndate(BusinessDate.getNowDay());
            // 进表人员
            seqInfoDiscard.setInoperid(this.getOperator().getOperid());
            // 进表类型
            seqInfoDiscard.setDotype("delete");

            if (seqInfoDiscard.insert() < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }

            // 删除抵押信息
            dc.executeUpdate("delete from ln_mortinfo where mortid='" + mortID + "'");
            // 流水日志表
            task = MortUtil.getTaskObj(req.getFieldValue("loanid"), req.getFieldValue("busiNode"),
                    CcbLoanConst.OPER_DEL);
            task.setOperid(this.getOperator().getOperid());
            task.setBankid(this.getOperator().getDeptid());
            if (task.insert() < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        this.res.setType(0);
        return 0;
    }

    /**
     * <p/>
     * 如果用户直接退出抵押管理页面，则把已生成的抵押编号放进废弃表中以备下次使用
     * <p/>
     */
    public int saveMortID_bak() {
        seqInfoDiscard = new SYSSEQDISCARD();

        try {
            // 抵押编号
            String mortID = req.getFieldValue("mortID");
            /** 抵押编号保存进废弃号码管理表 */
            // 业务类型
            seqInfoDiscard.setBhlx(CcbLoanConst.MORTTYPE);
            // 编号
            seqInfoDiscard.setDiscardno(mortID);
            // 使用标志: 未使用状态；
            seqInfoDiscard.setUseflg(0);
            // 原关系业务序号:贷款申请序号
            seqInfoDiscard.setOseqno(req.getFieldValue("loanID"));

            /** 先判断废号管理表中有无该没有使用的序号，如果没有则增加 */
            RecordSet rs = dc.executeQuery("select 1 from sys_seq_discard where bhlx='" + CcbLoanConst.MORTTYPE
                    + "' and discardno='" + mortID + "' and useflg='0'");
            if (!rs.next()) {
                if (seqInfoDiscard.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            } else {
                // do nothing
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        this.res.setType(0);
        return 0;
    }

    /**
     * <p/>
     * 批量快递
     * <p/>
     * 除了更新页面上的值之外，用户id、操作时间也一起更新；
     * <p/>
     * 更新前进行版本号检查，控制并发问题
     *
     * @return
     */
    public int batchEdit() {

        mortInfo = new LNMORTINFO();
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                // 初始化数据bean
                mortInfo.initAll(i, req);
                // 操作时间
                mortInfo.setOperdate(BusinessDate.getToday());
                // 用户id
                mortInfo.setOperid(this.getOperator().getOperid());
                // 柜号
                if (req.getFieldValue(i, "boxid") != null) {
                    mortInfo.setBoxid(req.getFieldValue(i, "boxid"));
                }
                // 更新前版本号
                int iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from ln_mortinfo where mortid='"
                        + req.getFieldValue(i, "mortid") + "'");
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
                        mortInfo.setRecversion(iBeforeVersion);
                    }
                }
                if (rs != null) {
                    rs.close();
                }

                if (mortInfo.updateByWhere(" where mortid='" + req.getFieldValue(i, "mortid") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // 流水日志表
                task = MortUtil.getTaskObj(req.getFieldValue(i, "loanid"), req.getFieldValue(i, "busiNode"),
                        CcbLoanConst.OPER_BATCHEDIT);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }

            } catch (Exception ex1) {
                ex1.printStackTrace();
                logger.error(ex1.getMessage());
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
      * <p/>
      * 修改抵押信息中的贷款申请号
      * <p/>
      * 更新前进行版本号检查，控制并发问题
      *
      * @return
      */
     public int editLoanId() {

         mortInfo = new LNMORTINFO();
         for (int i = 0; i < this.req.getRecorderCount(); i++) {
             try {
                 // 初始化数据bean
                 mortInfo.initAll(i, req);

                 LNMORTINFO mortTmp = mortInfo.findFirst(" where  loanid = '" + mortInfo.getLoanid() + "'");
                 if (mortTmp != null) {
                         this.res.setType(0);
                         this.res.setResult(false);
                         this.res.setMessage("修改贷款申请号出现错误, 此申请号已关联到:"+mortTmp.getMortid()+ "号抵押。");
                         return -1;
                 }

                 LNLOANAPPLY  loanTmp = new LNLOANAPPLY();
                 loanTmp = loanTmp.findFirst(" where  loanid = '" + mortInfo.getLoanid() + "'");
                 if (loanTmp == null) {
                         this.res.setType(0);
                         this.res.setResult(false);
                         this.res.setMessage("修改贷款申请号出现错误, 此申请号不存在。");
                         return -1;
                 }


                 // 操作时间
                 mortInfo.setOperdate(BusinessDate.getToday());
                 // 用户id
                 mortInfo.setOperid(this.getOperator().getOperid());
                 // 更新前版本号
                 int iBeforeVersion = 0;
                 if (req.getFieldValue(i, "recVersion") != null && !req.getFieldValue(i, "recVersion").equals("")) {
                     iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                 }
                 int iAfterVersion = 0;
                 RecordSet rs = dc.executeQuery("select recversion from ln_mortinfo where mortid='"
                         + req.getFieldValue(i, "mortid") + "'");
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
                         mortInfo.setRecversion(iBeforeVersion);
                     }
                 }
                 if (rs != null) {
                     rs.close();
                 }

                 if (mortInfo.updateByWhere(" where mortid='" + req.getFieldValue(i, "mortid") + "'") < 0) {
                     this.res.setType(0);
                     this.res.setResult(false);
                     this.res.setMessage(PropertyManager.getProperty("300"));
                     return -1;
                 }
                 // 流水日志表
                 task = MortUtil.getTaskObj(req.getFieldValue(i, "loanid"), req.getFieldValue(i, "busiNode"),
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
                 ex1.printStackTrace();
                 logger.error(ex1.getMessage());
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



}
