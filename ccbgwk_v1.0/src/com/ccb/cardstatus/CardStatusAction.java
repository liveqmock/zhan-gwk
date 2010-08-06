package com.ccb.cardstatus;

import com.ccb.dao.LSCARDSTATUS;
import gov.mof.fasp.service.BankService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 2010-8-6
 * Time: 11:42:00
 * To change this template use File | Settings | File Templates.
 */
public class CardStatusAction extends Action{

    private static final Log logger = LogFactory.getLog(CardStatusAction.class);
    // 处理卡状态信息
    public int handleCardStatus(){
        dc = ConnectionManager.getInstance().getConnection();
      // 查询公务卡凭证中最大的GUID（顺序码）
        String maxguid = null;
        maxguid = queryMaxGuid();
        if(maxguid != null){
           // 读取公务卡状态
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = df.format(new Date());
            String year = strDate.substring(0, 4);
            BankService service = FaspServiceAdapter.getBankService();
            List rtnList = service.getOfficeCardStatus("BANK.CCB", "8015", year, "405", maxguid);
//            List rtnList = SendConsumeInfoTest.sendConsumeInfoRtn();
            if(rtnList != null){
                // TODO 确认卡状态更改接口返回的数据，是否仅发送已停用卡信息
               // dc.executeUpdate("truncate table ls_cardstatus");
                LSCARDSTATUS cardstatus = new LSCARDSTATUS();
                for(Object o : rtnList){
                   if(o instanceof Map){
                       Map m = (Map)o;
                       String account = (String)m.get("account");
                       String cardname = (String)m.get("cardname");
                        String status = (String)m.get("status");
                       String bdgagency = (String)m.get("bdgagency");
                       String guid = (String)m.get("guid");
                       cardstatus.setAccount(account);
                       cardstatus.setCardname(cardname);
                       cardstatus.setStatus(status);
                       cardstatus.setBdgagency(bdgagency);
                       cardstatus.setGuid(guid);
                       cardstatus.insert();
                       if(status != null && "128".equalsIgnoreCase(status.trim())){
                           updateCardbaseStatus(account);
                       }
                   }
                }
            }
        }
       return 0;
    }
    // 查询最大顺序号
    private String queryMaxGuid(){
       String guidsql = "select max(guid) as maxguid from LS_BDGAGENCY";
       RecordSet rs = dc.executeQuery(guidsql);
       String maxguid = null;
       while(rs.next()){
          maxguid = rs.getString("maxguid");
       }
        return maxguid;
    }

    // 更新卡基础信息表
    private int updateCardbaseStatus(String account){
       int rtnCnt = 0;
       String updateSql = "update ls_cardbaseinfo set status = '128',action = '2',sentflag='0' where account = '"+account+"' and status='1'";
       rtnCnt = dc.executeUpdate(updateSql);
       return rtnCnt;
    }
   /* public static void main(String[] args){
        CardStatusAction csa = new CardStatusAction();

        csa.dc = ConnectionManager.getInstance().getConnection();
        DatabaseConnection d = csa.dc;
        if(d != null){
         String maxguid = csa.queryMaxGuid();
        System.out.println(maxguid);
        }else {
           System.out.println("==null===");
        }
        // csa.handleCardStatus();
    }*/
}
