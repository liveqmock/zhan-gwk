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

import gateway.financebureau.BankService;
import gateway.financebureau.GwkBurlapServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class consumeAction extends Action {

    // 记录每次发往财政局的数据记录数
    private int sendInfoCount = 0;
    private static final Log logger = LogFactory.getLog(consumeAction.class);
    private String pubAreaCode = "";
    private String strRemark = "";
    private String strJudeFlag = "";


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
        //获取登录用户所属部门，只有分行的用户有权限查看其它支行的数据 2012-11-26
        pubAreaCode = this.getOperator().getDeptid();
        //获取备注里面的内容，如果是admin，有查看其它支行数据的权限 2012-11-26
        strRemark = this.getOperator().getFillstr150();
        //获取判断条件 2012-11-26
        strJudeFlag = PropertyManager.getProperty("pub.plat.admin.jude.flag");
        // 查询所有初始状态的未发送消费数据
        List cardList = null;
        HashMap mapConsume=null;
        int updateInfoCount = 0;
        try {
            //将所有财政局的消费记录获取过来 2012-05-13 linyong
            mapConsume=queryInfoByStatusMap(RtnTagKey.SEND_INIT);
//            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("读取消费信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("读取消费信息出现错误，请查看系统日志。");
            return -1;
        }
        String areaCode="";
        this.sendInfoCount = 0;
        //当不具有管理员权限时，只能查看本支行的数据
        if(!strJudeFlag.equals(strRemark)){
            areaCode=pubAreaCode;
            //根据所属区域代码，获取将要发送的消费列表 2012-05-13 linyong
            cardList = (List)mapConsume.get(areaCode);
            if (cardList != null && cardList.size() > 0) {
                this.sendInfoCount = cardList.size();
                List rtnlist = null;
                try {
                    rtnlist = sendConsumeInfoByStatus(areaCode,cardList, RtnTagKey.SEND_INIT);
                } catch (Exception e) {
                    logger.error("发送消费信息出现错误，请查看系统日志。", e);
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage("发送消费信息出现错误，请查看系统日志。");
                    return -1;
                }
                // 判断是否有返回数据，若无，则更新数据状态为发送失败
                if (rtnlist == null || rtnlist.size() <= 0) {
                    try {
                        // 条件中加入所属区域编码 2012-12-14 linyong
                        updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_FAIL,areaCode);
                    } catch (Exception e) {
                        logger.error("发送数据后更新本地数据为失败状态失败，请查看系统日志。");
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("发送数据后更新本地数据状态失败，请查看系统日志。");
                        return -1;
                    }

                } else {
                    // 处理返回信息
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        if (result == null){
                            result = (String) m1.get("RESULT");
                        }
                        // 通过判断result的值判断是否发送成功，若全部成功则返回一个result==success的值
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // 条件中加入所属区域编码 2012-12-14 linyong
                                updateInfoCount += updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_SUCCESS,areaCode);
                            } catch (Exception e) {
                                logger.error("发送数据后更新本地数据为失败状态出现异常，请查看系统日志。");
                                this.res.setType(0);
                                this.res.setResult(false);
                                this.res.setMessage("发送数据后更新本地数据状态出现异常，请查看系统日志。");
                                return -1;
                            }
                        } else {
                            // 判断是否有重复流水号和帐号，若有，则更改记录状态为发送成功
                            String sameid = (String) m1.get(RtnTagKey.SAMEID);
                            String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                            if ((sameid != null && !"".equals(sameid.trim()))
                                    || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                try {
                                    updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
                                } catch (Exception e) {
                                    logger.error("发送数据后更新重复发送的数据时出现异常，请查看系统日志。");
                                    this.res.setType(0);
                                    this.res.setResult(false);
                                    this.res.setMessage("发送数据后更新本地数据时出现异常，请查看系统日志。");
                                    return -1;
                                }
                            } else {
                                logger.error("Server response message:" + (String) m1.get("message"));
                            }
                        }
                    }
                }
            }
        }else{
            String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
            for (int j =0;j<strArr.length;j++){
                areaCode=strArr[j];
                //根据所属区域代码，获取将要发送的消费列表 2012-05-13 linyong
                cardList = (List)mapConsume.get(areaCode);
                if (cardList != null && cardList.size() > 0) {
                    this.sendInfoCount =this.sendInfoCount + cardList.size();
                    List rtnlist = null;
                    try {
                        rtnlist = sendConsumeInfoByStatus(areaCode,cardList, RtnTagKey.SEND_INIT);
                    } catch (Exception e) {
                        logger.error("发送消费信息出现错误，请查看系统日志。", e);
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("发送消费信息出现错误，请查看系统日志。");
                        return -1;
                    }
                    // 判断是否有返回数据，若无，则更新数据状态为发送失败
                    if (rtnlist == null || rtnlist.size() <= 0) {
                        try {
                            // 条件中加入所属区域编码 2012-12-14 linyong
                            updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_FAIL,areaCode);
                        } catch (Exception e) {
                            logger.error("发送数据后更新本地数据为失败状态失败，请查看系统日志。");
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage("发送数据后更新本地数据状态失败，请查看系统日志。");
                            return -1;
                        }

                    } else {
                        // 处理返回信息
                        for (int i = 0; i < rtnlist.size(); i++) {
                            Map m1 = (Map) rtnlist.get(i);
                            String result = (String) m1.get(RtnTagKey.RESULT);
                            if (result == null){
                                result = (String) m1.get("RESULT");
                            }
                            // 通过判断result的值判断是否发送成功，若全部成功则返回一个result==success的值
                            if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                                try {
                                    // 条件中加入所属区域编码 2012-12-14 linyong
                                    updateInfoCount += updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_SUCCESS,areaCode);
                                } catch (Exception e) {
                                    logger.error("发送数据后更新本地数据为失败状态出现异常，请查看系统日志。");
                                    this.res.setType(0);
                                    this.res.setResult(false);
                                    this.res.setMessage("发送数据后更新本地数据状态出现异常，请查看系统日志。");
                                    return -1;
                                }
                            } else {
                                // 判断是否有重复流水号和帐号，若有，则更改记录状态为发送成功
                                String sameid = (String) m1.get(RtnTagKey.SAMEID);
                                String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                                if ((sameid != null && !"".equals(sameid.trim()))
                                        || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                    try {
                                        updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
                                    } catch (Exception e) {
                                        logger.error("发送数据后更新重复发送的数据时出现异常，请查看系统日志。");
                                        this.res.setType(0);
                                        this.res.setResult(false);
                                        this.res.setMessage("发送数据后更新本地数据时出现异常，请查看系统日志。");
                                        return -1;
                                    }
                                } else {
                                    logger.error("Server response message:" + (String) m1.get("message"));
                                }
                            }
                        }
                    }
                }
            }
        }

        //返回发送总数和发送成功记录数
        if (updateInfoCount != -1) {
            String send_update_count = String.valueOf(this.sendInfoCount) + "_" + String.valueOf(updateInfoCount);
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue(send_update_count);
            this.res.setType(4);
            this.res.setResult(true);
        } else {
            logger.error("发送数据后更新本地数据状态失败，请查看系统日志。");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送数据后更新本地数据状态失败，请查看系统日志。");
            return -1;
        }

        return 0;
    }

    private void writeObject(List tempList,String areaCode) {
        try {

            FileOutputStream outStream = new FileOutputStream("D:/CardResponse"+areaCode+".txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(tempList);
            for(int i=0;i<tempList.size();i++){
                Map m = new HashMap();
                m = (Map)tempList.get(i);
                objectOutputStream.writeObject(m);
            }
            outStream.close();
            System.out.println("successful");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  发送所有异常状态的未发送成功消费数据到财政局
    public int writeConsumeExpInfo() {
        //获取登录用户所属部门，只有分行的用户有权限查看其它支行的数据 2012-11-26
        pubAreaCode = this.getOperator().getDeptid();
        //获取备注里面的内容，如果是admin，有查看其它支行数据的权限 2012-11-26
        strRemark = this.getOperator().getFillstr150();
        //获取判断条件 2012-11-26
        strJudeFlag = PropertyManager.getProperty("pub.plat.admin.jude.flag");
        // 查询所有初始状态的未发送消费数据
        List cardList = null;
        HashMap mapConsume=null;
        int updateInfoCount = 0;
        String[] status = new String[]{RtnTagKey.SEND_FAIL, RtnTagKey.SEND_TIME_OUT};
        try {
            //将所有财政局的消费记录获取过来 2012-05-13 linyong
            mapConsume=queryInfoByStatusMap(status);
//            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("读取消费信息出现错误，请查看系统日志。", e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("读取消费信息出现错误，请查看系统日志。");
            return -1;
        }
        String areaCode="";
        this.sendInfoCount = 0;
        //当不具有管理员权限时，只能查看本支行的数据
        if(!strJudeFlag.equals(strRemark)){
            areaCode=pubAreaCode;
            //根据所属区域代码，获取将要发送的消费列表 2012-05-13 linyong
            cardList = (List)mapConsume.get(areaCode);
            if (cardList != null && cardList.size() > 0) {
                this.sendInfoCount = cardList.size();
                List rtnlist = null;
                try {
                    rtnlist = sendConsumeInfoByStatusArr(areaCode,cardList, status);
                } catch (Exception e) {
                    logger.error("发送消费信息出现错误，请查看系统日志。", e);
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage("发送消费信息出现错误，请查看系统日志。");
                    return -1;
                }
                // 判断是否有返回数据，若无，则更新数据状态为发送失败
                if (rtnlist == null || rtnlist.size() <= 0) {
                    try {
                        // 条件中加入所属区域编码 2012-12-14 linyong
                        updateAllStatusArrToStatus(status, RtnTagKey.SEND_FAIL,areaCode);
                    } catch (Exception e) {
                        logger.error("发送数据后更新本地数据为失败状态失败，请查看系统日志。");
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("发送数据后更新本地数据状态失败，请查看系统日志。");
                        return -1;
                    }
                } else {
                    // 处理返回信息
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        if (result == null){
                            result = (String) m1.get("RESULT");
                        }
                        // 通过判断result的值判断是否发送成功，若全部成功则返回一个result==success的值
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // 条件中加入所属区域编码 2012-12-14 linyong
                                updateInfoCount += updateAllStatusArrToStatus(status, RtnTagKey.SEND_SUCCESS,areaCode);
                            } catch (Exception e) {
                                logger.error("发送数据后更新本地数据为失败状态出现异常，请查看系统日志。");
                                this.res.setType(0);
                                this.res.setResult(false);
                                this.res.setMessage("发送数据后更新本地数据状态出现异常，请查看系统日志。");
                                return -1;
                            }
                        } else {
                            // 判断是否有重复流水号和帐号，若有，则更改记录状态为发送成功
                            String sameid = (String) m1.get(RtnTagKey.SAMEID);
                            String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                            if ((sameid != null && !"".equals(sameid.trim()))
                                    || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                try {
                                    updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
                                } catch (Exception e) {
                                    logger.error("发送数据后更新重复发送的数据时出现异常，请查看系统日志。");
                                    this.res.setType(0);
                                    this.res.setResult(false);
                                    this.res.setMessage("发送数据后更新本地数据时出现异常，请查看系统日志。");
                                    return -1;
                                }
                            } else {
                                logger.error("Server response message:" + (String) m1.get("message"));
                            }
                        }
                    }
                }
            }
        }else{
            String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
            for (int j =0;j<strArr.length;j++){
                areaCode=strArr[j];
                //根据所属区域代码，获取将要发送的消费列表 2012-05-13 linyong
                cardList = (List)mapConsume.get(areaCode);
                if (cardList != null && cardList.size() > 0) {
                    this.sendInfoCount =this.sendInfoCount + cardList.size();
                    List rtnlist = null;
                    try {
                        rtnlist = sendConsumeInfoByStatusArr(areaCode,cardList, status);
                    } catch (Exception e) {
                        logger.error("发送消费信息出现错误，请查看系统日志。", e);
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("发送消费信息出现错误，请查看系统日志。");
                        return -1;
                    }
                    // 判断是否有返回数据，若无，则更新数据状态为发送失败
                    if (rtnlist == null || rtnlist.size() <= 0) {
                        try {
                            // 条件中加入所属区域编码 2012-12-14 linyong
                            updateAllStatusArrToStatus(status, RtnTagKey.SEND_FAIL,areaCode);
                        } catch (Exception e) {
                            logger.error("发送数据后更新本地数据为失败状态失败，请查看系统日志。");
                            this.res.setType(0);
                            this.res.setResult(false);
                            this.res.setMessage("发送数据后更新本地数据状态失败，请查看系统日志。");
                            return -1;
                        }

                    } else {
                        // 处理返回信息
                        for (int i = 0; i < rtnlist.size(); i++) {
                            Map m1 = (Map) rtnlist.get(i);
                            String result = (String) m1.get(RtnTagKey.RESULT);
                            if (result == null){
                                result = (String) m1.get("RESULT");
                            }
                            // 通过判断result的值判断是否发送成功，若全部成功则返回一个result==success的值
                            if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                                try {
                                    // 条件中加入所属区域编码 2012-12-14 linyong
                                    updateInfoCount += updateAllStatusArrToStatus(status, RtnTagKey.SEND_SUCCESS,areaCode);
                                } catch (Exception e) {
                                    logger.error("发送数据后更新本地数据为失败状态出现异常，请查看系统日志。");
                                    this.res.setType(0);
                                    this.res.setResult(false);
                                    this.res.setMessage("发送数据后更新本地数据状态出现异常，请查看系统日志。");
                                    return -1;
                                }
                            } else {
                                // 判断是否有重复流水号和帐号，若有，则更改记录状态为发送成功
                                String sameid = (String) m1.get(RtnTagKey.SAMEID);
                                String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                                if ((sameid != null && !"".equals(sameid.trim()))
                                        || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                    try {
                                        updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
                                    } catch (Exception e) {
                                        logger.error("发送数据后更新重复发送的数据时出现异常，请查看系统日志。");
                                        this.res.setType(0);
                                        this.res.setResult(false);
                                        this.res.setMessage("发送数据后更新本地数据时出现异常，请查看系统日志。");
                                        return -1;
                                    }
                                } else {
                                    logger.error("Server response message:" + (String) m1.get("message"));
                                }
                            }
                        }
                    }
                }
            }
        }

        //返回发送总数和发送成功记录数
        if (updateInfoCount != -1) {
            String send_update_count = String.valueOf(this.sendInfoCount) + "_" + String.valueOf(updateInfoCount);
            this.res.setFieldName("rtnCnt");
            this.res.setFieldType("text");
            this.res.setEnumType("0");
            this.res.setFieldValue(send_update_count);
            this.res.setType(4);
            this.res.setResult(true);
        } else {
            logger.error("发送数据后更新本地数据状态失败，请查看系统日志。");
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("发送数据后更新本地数据状态失败，请查看系统日志。");
            return -1;
        }
        return 0;
    }

    private List sendConsumeInfoByStatus(String areaCode,List cardList, String status) throws Exception {
        return sendConsumeInfoByStatusArr(areaCode,cardList, new String[]{status});
    }

    // 发送某几状态的消费数据到财政局
    private List sendConsumeInfoByStatusArr(String areaCode,List cardList, String[] statusArr) throws Exception {
        List rtnlist = null;
        BankService service=null;
        //银行代码
        String strBank = "";
        //龙图接口版本号 2012-10-29
        String longtuVer = "";
        //业务系统标示 2012-10-29
        String applicationid = "";
        //行政区划编码 2012-10-29
        String admdivCode="";
        //财政编码 2012-10-29
        String finOrgCode="";
        //根据所属区域代码获取建设银行的编码
        strBank = PropertyManager.getProperty("ccb.code."+areaCode);
        longtuVer = PropertyManager.getProperty("longtu.version."+areaCode);
        admdivCode = PropertyManager.getProperty("admdiv.code."+areaCode);
        finOrgCode = PropertyManager.getProperty("fin.org.code."+areaCode);
        //根据不同的代码获取相应的业务系统标识 2012-10-29
        applicationid = PropertyManager.getProperty("application.id."+areaCode);
        if ("".equals(applicationid)){
            applicationid="BANK.CCB";
        }
        if (cardList.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = df.format(new Date());
            String year = strDate.substring(0, 4);
//            BankService service = FaspServiceAdapter.getBankService();
            //根据所属区域代码连接相应财政局的接口
            service = GwkBurlapServiceFactory.getInstance().getBankService(areaCode);
            try{
                if("v1".equals(longtuVer)){
                    rtnlist = service.writeConsumeInfo(applicationid, strBank, year, finOrgCode, cardList);
                }else if("v2".equals(longtuVer)){
                    rtnlist = service.writeConsumeInfo(applicationid, strBank, year,admdivCode,finOrgCode, cardList);
                }
                logger.info(strDate+" 向编码为:"+areaCode+"的财政局发送消费信息成功！");
            } catch (Exception ex){
                logger.error(strDate+" 向编码为:"+areaCode+"的财政局发送消费信息失败！"+ex.getMessage());
            }
//            rtnlist = service.writeConsumeInfo("BANK.CCB", strBank, year, "405", cardList);
        }

        return rtnlist;
    }

    // 查询所有某一状态的消费数据
    private List queryInfoByStatus(String status) {
        return queryInfoByStatusArr(new String[]{status});
    }

    // 查询所有某几状态的消费数据
    // 卡未注销
    private List queryInfoByStatusArr(String[] status) {

        this.sendInfoCount = 0;
        StringBuffer wheresqlbfr = new StringBuffer(" where csi.status in ('");
        int statusCount = status.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(status[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("') and cbi.status = '1' order by lsh ");
        String wheresql = new String(wheresqlbfr);
        String selectsql = "select lsh,csi.account as account,csi.cardname as cardname,busidate,busimoney,businame,limitdate,tx_cd from ls_consumeinfo csi "
                + " join ls_cardbaseinfo cbi on csi.account = cbi.account "
                + wheresql;

        System.out.println(selectsql);

        RecordSet rs = null;
        List cardList = new ArrayList();
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
            m.put("id", lsh);
            m.put("account", account);
            m.put("cardname", cardname);
            m.put("busidate", busidate);
            m.put("busimoney", busimoney);
            m.put("businame", businame);
            m.put("limitdate", limitdate);
            cardList.add(m);
        }
        if (rs != null) {
            rs.close();
            rs = null;
        }
        return cardList;
    }

    // 查询所有某一状态的消费数据
    private HashMap queryInfoByStatusMap(String status) {
        return queryInfoByStatusMap(new String[]{status});
    }

    // 查询所有某几状态的消费数据
    // 卡未注销
    // 将所有财政局的消费记录获取过来
    private HashMap queryInfoByStatusMap(String[] status) {
        HashMap mapConsume = new HashMap();
        String strCode = PropertyManager.getProperty("finance.codeset");
        String[] strArr = strCode.split(",");
        String areaCode="";
        String areaName="";
        for (int j=0;j<strArr.length;j++){
            StringBuffer wheresqlbfr = new StringBuffer(" where csi.status in ('");
            int statusCount = status.length;
            for (int i = 0; i < statusCount; i++) {
                wheresqlbfr.append(status[i]);
                if (i + 1 < statusCount) {
                    wheresqlbfr.append("','");
                }
            }
            wheresqlbfr.append("')");

            areaCode=strArr[j];
//            areaName=PropertyManager.getProperty("finance.name."+areaCode);
//            wheresqlbfr.append(" and cbi.gatheringbankacctname='"+areaName+"' ");
            // 使用areacode 2012-12-14
            wheresqlbfr.append(" and csi.areacode='"+areaCode+"' ");
            wheresqlbfr.append(" and cbi.status = '1'  ");
            wheresqlbfr.append(" order by lsh");
            String wheresql = new String(wheresqlbfr);
            String selectsql = "select lsh,csi.account as account,csi.cardname as cardname,busidate,busimoney,businame,limitdate,tx_cd from ls_consumeinfo csi "
                    + " join ls_cardbaseinfo cbi on csi.account = cbi.account "
                    + wheresql;

            System.out.println(selectsql);

            RecordSet rs = null;
            List cardList = new ArrayList();
            rs = dc.executeQuery(selectsql);
            while (rs.next()) {

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
                m.put("id", lsh);
                m.put("account", account);
                m.put("cardname", cardname);
                m.put("busidate", busidate);
                m.put("busimoney", busimoney);
                m.put("businame", businame);
                m.put("limitdate", limitdate);
                cardList.add(m);
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if(cardList.size()>0){
                mapConsume.put(areaCode,cardList);
            }
        }
        return mapConsume;
    }

    // 更新某一旧状态为新状态(10-初始状态,11-发送失败,12-发送超时,20-发送成功)
    private int updateAllStatusToStatus(String oldStatus, String newStatus) throws Exception {
        //String updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' where status='" + oldStatus + "' ";
        return updateAllStatusArrToStatus(new String[]{oldStatus}, newStatus);
    }

    // 更新某一旧状态为新状态(10-初始状态,11-发送失败,12-发送超时,20-发送成功)
    // 条件中加入所属区域 2012-12-14 linyong
    private int updateAllStatusToStatus(String oldStatus, String newStatus,String areaCode) throws Exception {
        //String updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' where status='" + oldStatus + "' ";
        return updateAllStatusArrToStatus(new String[]{oldStatus}, newStatus,areaCode);
    }


    // 更新某几旧状态为新状态(10-初始状态,11-发送失败,12-发送超时,20-发送成功)
    private int updateAllStatusArrToStatus(String[] oldStatus, String newStatus) throws Exception {
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
        if (RtnTagKey.SEND_TIME_OUT.equals(newStatus.trim())) {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送超时' " + wheresql;
        } else if (RtnTagKey.SEND_FAIL.equals(newStatus.trim())) {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送失败' " + wheresql;
        } else {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送成功' " + wheresql;
        }
        int rtn = dc.executeUpdate(updateStatusOKSql);
        dc.commit();
        return rtn;
    }

    // 更新某几旧状态为新状态(10-初始状态,11-发送失败,12-发送超时,20-发送成功)
    // 根据所属区域更新为已发送 2012-12-14
    private int updateAllStatusArrToStatus(String[] oldStatus, String newStatus,String areaCode) throws Exception {
        StringBuffer wheresqlbfr = new StringBuffer(" where status in ('");
        int statusCount = oldStatus.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(oldStatus[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("')");
        wheresqlbfr.append(" and areacode = '"+areaCode+"'");
        String wheresql = new String(wheresqlbfr);
        String updateStatusOKSql = null;
        if (RtnTagKey.SEND_TIME_OUT.equals(newStatus.trim())) {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送超时' " + wheresql;
        } else if (RtnTagKey.SEND_FAIL.equals(newStatus.trim())) {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送失败' " + wheresql;
        } else {
            updateStatusOKSql = "update ls_consumeinfo set status='" + newStatus + "' ,txlog=to_char(sysdate,'yyyy-mm-dd HH:mm:ss')||' 发送成功' " + wheresql;
        }
        int rtn = dc.executeUpdate(updateStatusOKSql);
        dc.commit();
        return rtn;
    }

    //若有重复发送的流水号或者帐号，则将原状态改为发送成功
    private int updateSameIdRecordStatus(String sameid, String sameaccount) {
        String updateSameIdOKSql = "update ls_consumeinfo set status='20' where lsh='"
                + sameid + "' and account='" + sameaccount + "'";
        logger.info(updateSameIdOKSql);
        int rtn = dc.executeUpdate(updateSameIdOKSql);
        return rtn;
    }

    // 返回发往财政局的记录数
    public int getSendInfoCount() {
        return this.sendInfoCount;
    }

    public int sendAllConsumeInfo() {
        // 查询所有初始状态的未发送消费数据
        List cardList = null;
        HashMap mapConsume=null;
        int updateInfoCount = 0;
        try {
            //将所有财政局的消费记录获取过来 2012-05-13 linyong
            mapConsume=queryInfoByStatusMap(RtnTagKey.SEND_INIT);
//            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("读取消费信息出现错误，请查看系统日志。", e);
            return -1;
        }
        String areaCode="";
        String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
        for (int j =0;j<strArr.length;j++){
            areaCode=strArr[j];
            //根据所属区域代码，获取将要发送的消费列表 2012-05-13 linyong
            cardList = (List)mapConsume.get(areaCode);
            if (cardList != null && cardList.size() > 0) {
                List rtnlist = null;
                try {
                    rtnlist = sendConsumeInfoByStatus(areaCode,cardList, RtnTagKey.SEND_INIT);
                } catch (Exception e) {
                    logger.error("发送消费信息出现错误，请查看系统日志。", e);
                    return -1;
                }
                // 判断是否有返回数据，若无，则更新数据状态为发送失败
                if (rtnlist == null || rtnlist.size() <= 0) {
                    try {
                        // 条件中加入所属区域编码 2012-12-14 linyong
                        updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_FAIL,areaCode);
                    } catch (Exception e) {
                        logger.error("发送数据后更新本地数据为失败状态失败，请查看系统日志。");
                    }
                    return -1;
                } else {
                    // 处理返回信息
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        if (result == null){
                            result = (String) m1.get("RESULT");
                        }
                        // 通过判断result的值判断是否发送成功，若全部成功则返回一个result==success的值
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // 条件中加入所属区域编码 2012-12-14 linyong
                                updateInfoCount += updateAllStatusToStatus(RtnTagKey.SEND_INIT, RtnTagKey.SEND_SUCCESS,areaCode);
                            } catch (Exception e) {
                                logger.error("发送数据后更新本地数据为失败状态出现异常，请查看系统日志。");
                                return -1;
                            }
                        } else {
                            // 判断是否有重复流水号和帐号，若有，则更改记录状态为发送成功
                            String sameid = (String) m1.get(RtnTagKey.SAMEID);
                            String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                            if ((sameid != null && !"".equals(sameid.trim()))
                                    || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                try {
                                    updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
                                } catch (Exception e) {
                                    logger.error("发送数据后更新重复发送的数据时出现异常，请查看系统日志。");
                                    return -1;
                                }
                            } else {
                                logger.error("Server response message:" + (String) m1.get("message"));
                            }
                        }
                    }
                }
            }
        }

        //返回发送总数和发送成功记录数
        if (updateInfoCount != -1) {
        } else {
            logger.error("发送数据后更新本地数据状态失败，请查看系统日志。");
            return -1;
        }
        return 0;
    }

    public int sendAllConsumeExpInfo() {
        // 查询所有初始状态的未发送消费数据
        List cardList = null;
        HashMap mapConsume=null;
        int updateInfoCount = 0;
        String[] status = new String[]{RtnTagKey.SEND_FAIL, RtnTagKey.SEND_TIME_OUT};
        try {
            //将所有财政局的消费记录获取过来 2012-05-13 linyong
            mapConsume=queryInfoByStatusMap(status);
//            cardList = queryInfoByStatus(RtnTagKey.SEND_INIT);
        } catch (Exception e) {
            logger.error("读取消费信息出现错误，请查看系统日志。", e);
            return -1;
        }
        String areaCode="";
        String[] strArr = PropertyManager.getProperty("finance.codeset").split(",");
        for (int j =0;j<strArr.length;j++){
            areaCode=strArr[j];
            //根据所属区域代码，获取将要发送的消费列表 2012-05-13 linyong
            cardList = (List)mapConsume.get(areaCode);
            if (cardList != null && cardList.size() > 0) {
                List rtnlist = null;
                try {
                    rtnlist = sendConsumeInfoByStatusArr(areaCode,cardList,status);
                } catch (Exception e) {
                    logger.error("发送消费信息出现错误，请查看系统日志。", e);
                    return -1;
                }
                // 判断是否有返回数据，若无，则更新数据状态为发送失败
                if (rtnlist == null || rtnlist.size() <= 0) {
                    try {
                        // 条件中加入所属区域编码 2012-12-14 linyong
                        updateAllStatusArrToStatus(status, RtnTagKey.SEND_FAIL,areaCode);
                    } catch (Exception e) {
                        logger.error("发送数据后更新本地数据为失败状态失败，请查看系统日志。");
                    }
                    return -1;
                } else {
                    // 处理返回信息
                    for (int i = 0; i < rtnlist.size(); i++) {
                        Map m1 = (Map) rtnlist.get(i);
                        String result = (String) m1.get(RtnTagKey.RESULT);
                        if (result == null){
                            result = (String) m1.get("RESULT");
                        }
                        // 通过判断result的值判断是否发送成功，若全部成功则返回一个result==success的值
                        if (RtnTagKey.RESULT_SUCCESS.equalsIgnoreCase(result)) {
                            try {
                                // 条件中加入所属区域编码 2012-12-14 linyong
                                updateInfoCount += updateAllStatusArrToStatus(status, RtnTagKey.SEND_SUCCESS,areaCode);
                            } catch (Exception e) {
                                logger.error("发送数据后更新本地数据为失败状态出现异常，请查看系统日志。");
                                return -1;
                            }
                        } else {
                            // 判断是否有重复流水号和帐号，若有，则更改记录状态为发送成功
                            String sameid = (String) m1.get(RtnTagKey.SAMEID);
                            String sameaccount = (String) m1.get(RtnTagKey.SAMEACCOUNT);
                            if ((sameid != null && !"".equals(sameid.trim()))
                                    || (sameaccount != null && !"".equals(sameaccount.trim()))) {
                                try {
                                    updateInfoCount += updateSameIdRecordStatus(sameid, sameaccount);
                                } catch (Exception e) {
                                    logger.error("发送数据后更新重复发送的数据时出现异常，请查看系统日志。");
                                    return -1;
                                }
                            } else {
                                logger.error("Server response message:" + (String) m1.get("message"));
                            }
                        }
                    }
                }
            }
        }

        //返回发送总数和发送成功记录数
        if (updateInfoCount != -1) {
        } else {
            logger.error("发送数据后更新本地数据状态失败，请查看系统日志。");
            return -1;
        }
        return 0;
    }
}