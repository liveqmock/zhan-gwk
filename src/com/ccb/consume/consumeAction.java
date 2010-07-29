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

import java.text.SimpleDateFormat;
import java.util.*;

public class consumeAction extends Action {
    // 系统日志表
    LNTASKINFO task = null;
    // 记录每次发往财政局的数据记录数
    private int sendInfoCount = 0;
    // 超时标志
    private boolean isTimeOut = false;

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
    //  查询所有初始状态的未发送消费数据均发往财政局
    public int writeConsumeInfo() {
        // 查询所有初始状态的未发送消费数据
        List cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        int updateInfoCount = sendConsumeInfoByStatus(cardList, RtnTagKey.SEND_INIT);
        //返回发送总数和发送成功记录数
        if (updateInfoCount != -1) {
            String send_update_count = String.valueOf(this.getSendInfoCount()) + "_" + String.valueOf(updateInfoCount);
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue(send_update_count);
            this.res.setType(4);
            this.res.setResult(true);
        } else {
            logger.error("发送超时，请进行消费信息的发送异常管理。");
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue("-1");
            this.res.setType(4);
            this.res.setResult(true);
            /*this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送超时，请进行消费信息的发送异常管理。");*/
            return -1;
        }
        return 0;
    }

    //  发送所有异常状态的未发送成功消费数据到财政局
    public int writeConsumeExpInfo() {
        String[] status = new String[]{RtnTagKey.SEND_FAIL, RtnTagKey.SEND_TIME_OUT};
        List cardList = queryInfoByStatusArr(status);
        int updateInfoCount = sendConsumeInfoByStatusArr(cardList, status);
        if (updateInfoCount != -1) {
            String send_update_count = String.valueOf(this.getSendInfoCount()) + "_" + String.valueOf(updateInfoCount);
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue(send_update_count);
            this.res.setType(4);
            this.res.setResult(true);
        } else {
            logger.error("发送超时，请进行消费信息的发送异常管理。");
            /*this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送超时，请进行消费信息的发送异常管理。");*/
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue("-1");
            this.res.setType(4);
            this.res.setResult(true);
            return -1;
        }
        return 0;
    }

    public int sendConsumeInfoByStatus(List cardList, String status) {
        return sendConsumeInfoByStatusArr(cardList, new String[]{status});
    }

    // 发送某几状态的消费数据到财政局
    public int sendConsumeInfoByStatusArr(List cardList, String[] statusArr) {
        int updateInfoCount = 0;
        try {
            if (cardList.size() > 0) {
                // TODO 年度为当前年度?
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = df.format(new Date());
                String year = strDate.substring(0, 4);
                //===========================================================================
                //---------------------------------------------------------------------------
                BankService service = FaspServiceAdapter.getBankService();
                List rtnlist = null;
                // 限定时间：60秒
                 int delayMilliseconds = 60 * 1000;
                // 测试时间:5秒
                //int delayMilliseconds = 5 * 1000;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        isTimeOut = true;
                    }
                }, delayMilliseconds);
                // 循环发送，直至成功 若超过60秒，则视为超时，跳出循环
                while (!isTimeOut) {
                    rtnlist = service.writeConsumeInfo("BANK.CCB", "8015", year, "405", cardList);
                    //rtnlist = SendConsumeInfoTest.sendConsumeInfoRtn();
                    if (rtnlist != null && rtnlist.size() > 0) {
                        break;
                    }
                }
                // 60秒时间内如果返回结果为空，则更新消费信息为发送超时
                if (rtnlist == null || rtnlist.size() <= 0) {
                    updateAllStatusArrToStatus(statusArr, RtnTagKey.SEND_TIME_OUT);
                    return -1;
                } else {
                    //---------------------------------------------------------------------------
                    //===========================================================================
                    // 处理返回信息
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        // 通过判断result的值判断是否发送成功，若全部成功则返回一个result==success的值
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            updateInfoCount += updateAllStatusArrToStatus(statusArr, RtnTagKey.SEND_SUCCESS);
                        } else {
                            // TODO 先判断是否发送失败！！！！(暂无result标记)
                            // 判断是否有重复流水号和帐号，若有，则更改记录状态为发送成功
                            String sameid = (String) m1.get(RtnTagKey.SAMEID);
                            String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                            if ((sameid != null && !"".equals(sameid.trim()))
                                    || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
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

    // 查询所有某一状态的消费数据
    public List queryInfoByStatus(String status) {
        return queryInfoByStatusArr(new String[]{status});
    }

    // 查询所有某几状态的消费数据
    public List queryInfoByStatusArr(String[] status) {

        this.sendInfoCount = 0;
        StringBuffer wheresqlbfr = new StringBuffer(" where status in ('");
        int statusCount = status.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(status[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("') order by lsh ");
        String wheresql = new String(wheresqlbfr);
        String selectsql = "select lsh,account,cardname,busidate,busimoney,businame,limitdate,tx_cd from ls_consumeinfo " + wheresql;
        RecordSet rs = null;
        List cardList = new ArrayList();
        try {
            rs = dc.executeQuery(selectsql);
            while (rs.next()) {
                this.sendInfoCount++;
                Map m = new HashMap();
                String lsh = rs.getString("lsh");
                String account = rs.getString("account").trim();
                String cardname = rs.getString("cardname").trim();
                String busidate = rs.getString("busidate");
                busidate = busidate.substring(0, 4) + busidate.substring(5, 7) + busidate.substring(8, 10);
                Double busimoney = rs.getDouble("busimoney");
                String businame = rs.getString("businame").trim();
                String limitdate = rs.getString("limitdate");
                limitdate = limitdate.substring(0, 4) + limitdate.substring(5, 7) + limitdate.substring(8, 10);
                String tx_cd = rs.getString("tx_cd");
                if (busimoney <= 0) {
                    limitdate = "";
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
            logger.error("读取消费信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("读取消费信息出现错误，请查看系统日志。");
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

    // 更新某一旧状态为新状态(10-初始状态,11-发送失败,12-发送超时,20-发送成功)
    public int updateAllStatusToStatus(String oldStatus, String newStatus) {
        //String updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' where status='" + oldStatus + "' ";
        return updateAllStatusArrToStatus(new String[]{oldStatus}, newStatus);
    }


    // 更新某几旧状态为新状态(10-初始状态,11-发送失败,12-发送超时,20-发送成功)
    public int updateAllStatusArrToStatus(String[] oldStatus, String newStatus) {
        StringBuffer wheresqlbfr = new StringBuffer(" where status in ('");
        int statusCount = oldStatus.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(oldStatus[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("')");
        String wheresql = new String(wheresqlbfr);
        String updateStatusOKSql = null;
        if(RtnTagKey.SEND_TIME_OUT.equals(newStatus.trim())){
           updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送超时' "+ wheresql;
        }else if(RtnTagKey.SEND_FAIL.equals(newStatus.trim())){
           updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送失败' "+ wheresql;
        }else{
           updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送成功' "+ wheresql;
        }
        int rtn = dc.executeUpdate(updateStatusOKSql);
        dc.commit();
        if (rtn < 0) {
            logger.error("发送消费信息后变更本地记录状态出现错误，请查看系统日志。");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送消费信息后变更本地记录状态出现错误，请查看系统日志。");
            return -1;
        }
        return rtn;
    }

    //若有重复发送的流水号或者帐号，则将原状态改为发送成功
    public int updateSameIdRecordStatus(String sameid, String sameaccount) {
        String updateSameIdOKSql = "update ls_consumeinfo set status='20' where lsh='"
                + sameid + "' and account='" + sameaccount + "'";
        logger.info(updateSameIdOKSql);
        int rtn = dc.executeUpdate(updateSameIdOKSql);
        if (rtn < 0) {
            logger.error("发送消费信息后变更重复发送记录状态出现错误，请查看系统日志。");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送消费信息后变更重复发送记录状态出现错误，请查看系统日志。");
            return -1;
        }
        return rtn;
    }

    // 返回发往财政局的记录数
    public int getSendInfoCount() {
        return this.sendInfoCount;
    }
}