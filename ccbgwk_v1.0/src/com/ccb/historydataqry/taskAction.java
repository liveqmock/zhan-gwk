package com.ccb.historydataqry;

import com.ccb.dao.DCCTASKINFO;
import com.ccb.util.SeqUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.form.config.SystemAttributeNames;
import pub.platform.form.control.Action;
import pub.platform.security.OperatorManager;
import pub.platform.utils.BusinessDate;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-8-31
 * Time: 10:05:08
 * To change this template use File | Settings | File Templates.
 */
public class taskAction extends Action {
     private static final Log logger = LogFactory.getLog(taskAction.class);
    DCCTASKINFO task = null;
     //插叙那日志插入
    public String taskinfoInsert() {
        OperatorManager om = (OperatorManager)sc.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        String addrIp = om.getRemoteAddr();
        // System.out.println("==============client ip: "+addrIp+"======================");
        try {
            task = new DCCTASKINFO();
            String tableName = req.getFieldValue("hidTabName"); //操作表名
            String acctNo = req.getFieldValue("acctno");  //账号
            String cardNo = req.getFieldValue("cardNo");  //卡号
            String custNo = req.getFieldValue("custNo");  //身份证号
            String custName = req.getFieldValue("custName"); //姓名
            String startDt = req.getFieldValue("busidate1"); //交易起始日期
            String endDt = req.getFieldValue("busidate2");   //交易截止日期
            String bankNo = req.getFieldValue(("bankNo"));   //开户所号
            String dt = req.getFieldValue(("date"));         //日期
            String rmk = req.getFieldValue("hidRMK");               //备注
            String OccBankNo = req.getFieldValue(("OccBankNo"));  //发生所号
            task.setTaskid(SeqUtil.getTaskSeq());   //获取日志序列号
            task.setTablename(tableName);
            task.setAcctno(acctNo);
            task.setCardno(cardNo);
            task.setCustno(custNo);
            task.setCustname(custName);
            task.setTxdtstart(startDt);
            task.setTxdtend(endDt);
            task.setTasktype("query");
            task.setTasktime(BusinessDate.getTodaytime());
            task.setOperid(this.getOperator().getOperid());
            task.setRemarks(rmk);
            task.setOrigbranch(bankNo);
            task.setOpenaccdate(dt);
            task.setFsorigbranch(OccBankNo);
            task.setClientaddr(addrIp);
            if (task.insert() < 0) {
                logger.error("插入日志失败！");
            }
        }  catch (Exception ex1) {
            logger.error(ex1.getMessage());
            ex1.printStackTrace();
        }
        return "1";
    }
}
