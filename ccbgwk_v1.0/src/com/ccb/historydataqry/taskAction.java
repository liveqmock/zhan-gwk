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
     //��������־����
    public String taskinfoInsert() {
        OperatorManager om = (OperatorManager)sc.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        String addrIp = om.getRemoteAddr();
        // System.out.println("==============client ip: "+addrIp+"======================");
        try {
            task = new DCCTASKINFO();
            String tableName = req.getFieldValue("hidTabName"); //��������
            String acctNo = req.getFieldValue("acctno");  //�˺�
            String cardNo = req.getFieldValue("cardNo");  //����
            String custNo = req.getFieldValue("custNo");  //���֤��
            String custName = req.getFieldValue("custName"); //����
            String startDt = req.getFieldValue("busidate1"); //������ʼ����
            String endDt = req.getFieldValue("busidate2");   //���׽�ֹ����
            String bankNo = req.getFieldValue(("bankNo"));   //��������
            String dt = req.getFieldValue(("date"));         //����
            String rmk = req.getFieldValue("hidRMK");               //��ע
            String OccBankNo = req.getFieldValue(("OccBankNo"));  //��������
            task.setTaskid(SeqUtil.getTaskSeq());   //��ȡ��־���к�
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
                logger.error("������־ʧ�ܣ�");
            }
        }  catch (Exception ex1) {
            logger.error(ex1.getMessage());
            ex1.printStackTrace();
        }
        return "1";
    }
}
