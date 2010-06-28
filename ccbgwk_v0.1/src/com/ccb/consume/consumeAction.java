package com.ccb.consume;

/**
 * <p>Title: 后台业务组件</p>
 *
 * <p>Description: 后台业务组件</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * @author
 * @version 1.0
 */

import com.ccb.dao.LNTASKINFO;
import gov.mof.fasp.service.BankService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class consumeAction extends Action {
    // 系统日志表
    LNTASKINFO task = null;

    private static final Log logger = LogFactory.getLog(consumeAction.class);


    //4.2.4.2	消费信息接入
    /*
    <list>
    <map>
    <!—消费流水号-->
    <string> ID </string><string>value</string>
    <!—卡号-->
    <string> ACCOUNT </string><string>value</string>
    <!—持卡人 -->
    <string> CARDNAME </string><string>2008</string>
    <!—消费日期-->
    <string> BUSIDATE </string><string>yymmdd</string>
    <!—消费金额-->
    <string> BUSIMONEY </string><double>0.0</double>
    <!—消费地点-->
    <string> BUSINAME </string><string>value</string>
    <!—最迟还款日-->
    <string> Limitdate </string><string>value</string>
    </list>

     */

    public int writeConsumeInfo() {

        //DatabaseConnection conn = ConnectionManager.getInstance().get();

//        String inac_date = "2010-06-08";
//        String sql = "select id,account,cardname,busidate,busimoney,businame,limitdate,tx_cd " +
//                " from ls_consumeinfo where status='10' and inac_date='" + inac_date + "' " +
//                " order by id ";
        String wheresql = " where status='10' order by lsh ";
        String selectsql = "select lsh,account,cardname,busidate,busimoney,businame,limitdate,tx_cd from ls_consumeinfo " + wheresql;
        String updateStatusOKSql = "update ls_consumeinfo set status='20' where status='10' ";

        RecordSet rs = null;
        List cardList = new ArrayList();
        try {
            rs = dc.executeQuery(selectsql);
            while (rs.next()) {
                Map m = new HashMap();
                String lsh = rs.getString("lsh");
                String account = rs.getString("account").trim();
                String cardname = rs.getString("cardname").trim();
                String busidate = rs.getString("busidate");
                busidate = busidate.substring(0,4) + busidate.substring(5,7) + busidate.substring(8,10);
                Double busimoney = rs.getDouble("busimoney");
                String businame = rs.getString("businame").trim();
                String limitdate = rs.getString("limitdate");
                limitdate = limitdate.substring(0,4) + limitdate.substring(5,7) + limitdate.substring(8,10);
                String tx_cd = rs.getString("tx_cd");
                if (busimoney <= 0) {
                     limitdate="";
                }
                if ("43".equals(tx_cd)) {
                    busimoney = -busimoney;
                }
                m.put("ID", lsh);
                m.put("ACCOUNT", account);
                m.put("CARDNAME", cardname);
                m.put("BUSIDATE", busidate);
                m.put("BUSIMONEY", busimoney);
                m.put("BUSINAME", businame);
                m.put("Limitdate", limitdate);
                cardList.add(m);
            }

        } catch (Exception e) {
            logger.error("读取消费信息出现错误，请查看系统日志。",e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("读取消费信息出现错误，请查看系统日志。");
            return -1;
        }

        try{
            BankService service = FaspServiceAdapter.getBankService();
            
            if (cardList.size() > 0) {
                //TODO  年度"2010" 参数！！！
//                List rtnlist = null;
                List rtnlist = service.writeConsumeInfo("BANK.CCB", "8015", "2010", "405", cardList);
                for (int i = 0; i < rtnlist.size(); i++) {
                    Map m1 = (Map) rtnlist.get(i);
                    String result = (String) m1.get("result");
                    if ("SUCCESS".equalsIgnoreCase(result)) {
                        System.out.println(result);
                        int rtn = dc.executeUpdate(updateStatusOKSql);
                        if (rtn < 0) {
                            logger.error("发送消费信息后变更本地记录状态出现错误，请查看系统日志。");
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage("发送消费信息后变更本地记录状态出现错误，请查看系统日志。");
                            return -1;
                        }
                    } else {
                        //TODO    !!
                        logger.error("发送消费信息后变更本地记录状态出现错误，请查看系统日志。");
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("发送消费信息后变更本地记录状态出现错误，请查看系统日志。");
                        return -1;
                  }

                }
            }
        } catch (Exception e) {
            logger.error("发送消费信息出现错误，请查看系统日志。",e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送消费信息出现错误，请查看系统日志。");
            return -1;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return 0;
    }

}