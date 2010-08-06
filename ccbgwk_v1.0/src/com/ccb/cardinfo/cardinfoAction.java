package com.ccb.cardinfo;

import com.ccb.consume.RtnTagKey;
import gov.mof.fasp.service.BankService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gwk
 * Date: 2010-8-5
 * Time: 15:53:23
 * To change this template use File | Settings | File Templates.
 */
public class cardinfoAction extends Action {

     // 记录每次发往财政局的数据记录数
    private int sendInfoCount = 0;
    // 超时标志
//    private boolean isTimeOut = false;
    //log
    private static final Log logger = LogFactory.getLog(cardinfoAction.class);
     /*   卡信息
    <list>
    <map>
    <!—卡号-->
    <string> ACCOUNT </string><string>value</string>
    <!—持卡人 -->
    <string> CARDNAME </string><string>value</string>
    <!—预算单位-->
    <string> BDGAGENCY </string><string>value</string>
    <!—还款帐户名-->
    <string> GATHERINGBANKACCTNAME </string>value</double>
    <!—还款账户开户行-->
    <string> GATHERINGBANKNAME </string><string>value</string>
    <!—还账户号-->
    <string> GATHERINGBANKACCTCODE </string><string>value</string>
    <!—身份证号-->
    <string> IDNUMBER </string><string>value</string>
    <!—用途-->
    <string> DIGEST </string><string>value</string>
    <!—开户银行（财政银行编码)-->
    <string> BANK </string><string>value</string>
    <!—开卡日期-->
    <string> CREATEDATE </string><string>value</string>
    <!—有效起始日期-->
    <string> STARTDATE </string><string>value</string>
    <!—有效终止日期-->
    <string> ENDDATE </string><string>value</string>
    <!—数据操作类型-->
    <string> ACTION </string><string>value</string>
    
    </list>

     */
    public int writeCardInfo() {
        //发送flag=0（未发送）的数据
       List cardList = queryInfoBySentFlag();
        int updateInfoCount = sendCardInfoByStatus(cardList);
        //发送成功记录数
        if (updateInfoCount != -1) {
            String send_update_count =  String.valueOf(updateInfoCount);
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue(send_update_count);
            this.res.setType(4);
            this.res.setResult(true);
        }
        return updateInfoCount;
    }
    //查询 未发送并且卡状态=1的数据
    public List queryInfoBySentFlag() {
         this.sendInfoCount = 0;

        String selectsql = "select account, cardname, bdgagency, gatheringbankacctname, gatheringbankname," +
                " gatheringbankacctcode, idnumber, digest, bank, createdate, startdate, enddate, action " +
                " from ls_cardbaseinfo where sentflag = '0' and status = '1' order by account";
        RecordSet rs = null;
        List cardList = new ArrayList();
        try {
            rs = dc.executeQuery(selectsql);
            while (rs.next()) {
                this.sendInfoCount++;
                Map m = new HashMap();
                String account = rs.getString("account").trim();
                String cardname = rs.getString("cardname").trim();
                String bgcy = rs.getString("cardname");
                String gatherbankacctname = rs.getString("gatheringbankacctname");
                String gatherbankname = rs.getString("gatheringbankname");
                String gatherbankacctcode = rs.getString("gatheringbankacctcode");
                String idnumber = rs.getString("idnumber");
                String digest = rs.getString("digest");
                String bank = rs.getString("bank");
                String createdate = rs.getString("createdate");
                String startdate = rs.getString("startdate");
                String enddate = rs.getString("enddate");
                String action = rs.getString("action");

                m.put("ACCOUNT", account);
                m.put("CARDNAME", cardname);
                m.put("BDGAGENCY", bgcy);
                m.put("GATHERINGBANKACCTNAME", gatherbankacctname);
                m.put("GATHERINGBANKNAME", gatherbankname);
                m.put("GATHERINGBANKACCTCODE", gatherbankacctcode);
                m.put("IDNUMBER", idnumber);
                m.put("DIGEST", digest);
                m.put("BANK", bank);
                m.put("CREATEDATE", createdate);
                m.put("STARTDATE", startdate);
                m.put("ENDDATE", enddate);
                m.put("ACTION", action);
                cardList.add(m);
            }

        } catch (Exception e) {
            logger.error("读取卡信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("读取卡信息出现错误，请查看系统日志。");
            return null;
        }
        finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        }
        return cardList;
    }
    //发送卡信息（状态＝未注销）
    public int sendCardInfoByStatus(List cardList) {
        int updateInfoCount = 0;
        try {
            if (cardList.size() > 0) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = df.format(new Date());
                String year = strDate.substring(0, 4);
//                BankService service = FaspServiceAdapter.getBankService();
                List rtnlist = null;
//                rtnlist = service.writeOfficeCard("BANK.CCB", "8015", year, "405", cardList);
                String te = "hh";
                rtnlist = new ArrayList();
                rtnlist.add(te);
                // 60秒时间内如果返回结果为空，则更新消费信息为发送超时
                if ( rtnlist == null || rtnlist.size() <= 0) {
//                    updateAllStatusArrToStatus(statusArr, RtnTagKey.SEND_FAIL);
//                    return -1;
                } else {
                    // 处理返回信息
                    for (int i = 0; i < rtnlist.size(); i++) {
//                        Map m1 = (Map) rtnlist.get(i);
//                        String result = (String) m1.get(RtnTagVal.RESULT);
                        String result = "success";
                        // 通过判断result的值判断是否发送成功，若全部成功则返回一个result==success的值
                        if (RtnTagVal.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            updateInfoCount += updateAllsentFlagArr( RtnTagVal.SEND_SUCCESS);
                        } else {
                            // 判断是否有重复流水号和帐号，若有，则更改记录状态为发送成功
//                            String sameid = (String) m1.get(RtnTagVal.SAMEID);
//                            String sameaccount = (String) m1.get(RtnTagVal.SAMEACCOUNT);
                             String sameid = "1";
                            String sameaccount = "2";
                            if ((sameid != null && !"".equals(sameid.trim()))
                                    && (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                updateInfoCount += updateSameidRecordFlag(sameid, sameaccount);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("发送消费信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送消费信息出现错误，请查看系统日志。");
            return -1;
        }
        return updateInfoCount;
    }
    //更新状态为成功状态
    public int updateAllsentFlagArr(String status) {

        String updateStatusOKSql = null;
        updateStatusOKSql = "update ls_cardbaseinfo set sentflag='1',txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 已发送' " +
                " where sentflag='0' and status='"+ status+"'";
        int rtn = dc.executeUpdate(updateStatusOKSql);
        dc.commit();
        if (rtn < 0) {
            logger.error("发送卡信息后变更本地记录状态出现错误，请查看系统日志。");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送卡信息后变更本地记录状态出现错误，请查看系统日志。");
            return -1;
        }
        return rtn;
    }
    //更新重复发送数据
    public int updateSameidRecordFlag(String sameid,String sameacct) {
         String updateStatusOKSql = null;
        updateStatusOKSql = "update ls_cardbaseinfo set sentflag='1',txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 已发送' " +
                " where account='" + sameacct +"' and idnumber='"+ sameid +"'";
        int rtn = dc.executeUpdate(updateStatusOKSql);
        dc.commit();
        if (rtn < 0) {
            logger.error("发送卡信息后变更本地记录状态出现错误，请查看系统日志。");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送卡信息后变更本地记录状态出现错误，请查看系统日志。");
            return -1;
        }
        return rtn;
    }
}
