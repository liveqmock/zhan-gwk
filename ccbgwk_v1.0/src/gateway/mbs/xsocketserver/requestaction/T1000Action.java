package gateway.mbs.xsocketserver.requestaction;

import com.ccb.dao.LSPAYBACKINFO;
import gateway.mbs.xsocketserver.RequestAction;
import gateway.mbs.xsocketserver.domain.RequestData;
import gateway.mbs.xsocketserver.domain.ResponseData;
import gov.mof.fasp.service.CommonQueryService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:27:07
 * To change this template use File | Settings | File Templates.
 */
public class T1000Action implements RequestAction {
    private Log logger = LogFactory.getLog(this.getClass());

    /*
    根据协议 每包长度不超过1024字节
    每条还款信息长度为51字节
    故，暂定每包记录数 为10
     */
    private static final int recordNum = 10;

    /*
    当前系统日期
     */
    private String currDate;


    private RequestData requestData;
    private ResponseData responseData = new ResponseData();

    private List<ResponseData> responseDataList = new ArrayList();

    //响应包多包标志
    boolean isMultiResponsePkg = false;

    //响应包头长度
    static final private int headLength = 65;

    //单笔还款记录长度 （ 卡号20 姓名 20 金额 11）
    static final private int oneRecordLength = 51;

    //响应包体中 总计部分长度 ：总笔数 6  总金额13
    static final private int bodyHeadFieldlength = 19;

    //特色平台响应包总笔数
    private int totalCount = 0;
    //特色平台响应包总金额
    private long totalAmt = 0;

    public T1000Action(RequestData requestData) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.currDate = sdf.format(new Date());
        this.requestData = requestData;
    }

    public List<ResponseData> getResponseDataList() {
        return responseDataList;
    }


    //=================

    public void process() {
        //
        String body = requestData.getBodyData();
        logger.info("交易1000 BODY:" + body);

        String year = body.substring(0, 4).trim();
        String voucherid = body.substring(4, 24).trim();

        if (year.equals("")) {
            year = currDate.substring(0, 4);
        }
        List rtnlist = null;
        try {
            rtnlist = queryConsumeInfo(voucherid, year);
        } catch(Exception e) {
            logger.error("交易1000数据请求超时错误:");
            logger.error(e);
            //012 通讯超时
            setErrorResponseData("012");
            responseDataList.add(this.responseData);
        }

        if (rtnlist != null) {
            if (rtnlist.size() > 0) {
                List<LSPAYBACKINFO> paybackinfos = getPaybackInfoList(rtnlist);
                generateResponseData(paybackinfos);
                savePaybackinfoToDB(paybackinfos);
            } else {
                //数据不存在 返回空包: 响应码=100
                logger.error("交易1000返回数据不存在:支付令号 " + voucherid + ",年度 " + year);
                setErrorResponseData("100");
                responseDataList.add(this.responseData);
                
            }
        }
        logger.info(voucherid + " " + year);

    }
    //取得数据
    private List queryConsumeInfo(String voucherid, String year) {
        CommonQueryService service = FaspServiceAdapter.getCommonQueryService();
        Map m = new HashMap();
        m.put("VOUCHERID", voucherid);

        List rtnlist = null;
        try {
            rtnlist = service.getQueryListBySql("BANK.CCB", "queryConsumeInfo", m, year);
        } catch (Exception e) {
            logger.error(e);
            //TODO
            throw new RuntimeException(e);
        }
        return rtnlist;
    }
    /**
     * 自财政局获取还款信息
     * 新加字段(费用年度,网点编号)设定值 2010/07/13 haiyu
     *  setYear
     * @param burlapRtnList
     * @return
     */
    private List<LSPAYBACKINFO> getPaybackInfoList(List burlapRtnList) {
        String branchId = requestData.getBranchId();  //网点编号
        String areaCode = requestData.getAreaCode();  //财政局编号
        List<LSPAYBACKINFO> paybackInfos = new ArrayList();

        for (int i = 0; i < burlapRtnList.size(); i++) {
            Map m = (Map) burlapRtnList.get(i);

            String voucherid = (String) m.get("VOUCHERID");
            String account = (String) m.get("ACCOUNT");
            String cardname = (String) m.get("CARDNAME");
            String amtstr = (String) m.get("Amt");
            String year = (String)m.get("year");
            long amt = (long) Double.parseDouble(amtstr) * 100;

            LSPAYBACKINFO paybackInfo = new LSPAYBACKINFO();
            paybackInfo.setVoucherid(voucherid.trim());
            paybackInfo.setAccount(account.trim());
            paybackInfo.setCardname(cardname.trim());
            paybackInfo.setAmt((double) amt / 100);
            paybackInfo.setQuerydate(currDate);
            //新加字段(财政局编号,费用年度,网点编号)设定值 2010/07/13 haiyu
            paybackInfo.setYear(year);
            paybackInfo.setBranchid(branchId);
            paybackInfo.setAreacode(areaCode);
            paybackInfos.add(paybackInfo);
            totalCount++;
            totalAmt += amt;
        }
        return paybackInfos;
    }


    private void savePaybackinfoToDB(List<LSPAYBACKINFO> paybackInfos) {

        for (LSPAYBACKINFO info : paybackInfos) {
            info.insert();
        }

    }


    /**
     * 生成全部响应包列表
     * 
     * @param paybackInfos 自财政局查询返回的扣款数据列表
     */
    private void generateResponseData(List<LSPAYBACKINFO> paybackInfos) {
        //每个数据包中记录计数             
        int count = 0;
        int listsize = paybackInfos.size();
        if (listsize > this.recordNum) {
            isMultiResponsePkg = true;
        } else {
            isMultiResponsePkg = false;
        }
        String body = null;
        if (isMultiResponsePkg) {
            //第一个包
            body = getResponseDataBodyContent(paybackInfos, 0, this.recordNum);
            responseDataList.add(generateOneReponseDataPkg(recordNum,true, body));

            int loopcount = (listsize - recordNum) / recordNum;

            if ((listsize - recordNum) % recordNum == 0) {
                loopcount--;
            }
            //中间包 循环
            count = recordNum;
            for (int i = 0; i < loopcount; i++) {
                body = getResponseDataBodyContent(paybackInfos, count, count + recordNum);
                responseDataList.add(generateOneReponseDataPkg(recordNum,false, body));
                count += this.recordNum;
            }

            //最后一个包
            int lastct = listsize - count;
            body = getResponseDataBodyContent(paybackInfos, count, listsize);
            ResponseData responseData = generateOneReponseDataPkg(lastct,false, body);
            //设置为无后续包标志 !!
            setMultiPkgFlag(false, responseData);
            responseDataList.add(responseData);

        } else {
            body = getResponseDataBodyContent(paybackInfos, 0, listsize);
            responseDataList.add(generateOneReponseDataPkg(listsize,true, body));
        }
    }

    private void setResponseDataHeader(ResponseData responseData) {
        //包头处理
        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        if (isMultiResponsePkg) {
            responseData.setNextFlag("1"); //有后续包
        } else {
            responseData.setNextFlag("0"); //无后续包
        }
        responseData.setTxnCode("1000");
        responseData.setErrCode("000");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");

        //包长度未设置
    }
    /*
       响应码	描述
       000	交易成功
       010	信息接收失败
       011	信息发送失败
       012	通讯超时
       100	数据不存在
       200	交易正在处理中(交易冲突)
       201	交易处理超时
       210	数据已做还款处理
       301	金额不符
       603	请求包中要求必须输入的信息没有输入
       604	此次交易正在处理，请不要重发
       900	系统错误
       901	数据库错误
    */

    private void setErrorResponseData(String errCode) {
        //包头处理
        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("0"); //无后续包
        responseData.setTxnCode("1000");
        responseData.setErrCode(errCode);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");
        //包长度
        responseData.setLength(headLength);
        String strlength = StringUtils.leftPad(String.valueOf(headLength), 4, ' ');
        responseData.setPkgLength(strlength);
    }

    /**
     * 工具 处理对单个响应包中多包标志
     *
     * @param isExistNextPkgFlag true:有后续包
     * @param responseData
     */

    private void setMultiPkgFlag(boolean isExistNextPkgFlag, ResponseData responseData) {

        if (isExistNextPkgFlag) {
            responseData.setNextFlag("1"); //有后续包
        } else {
            responseData.setNextFlag("0"); //无后续包
        }

    }

    /**
     * 生成单个响应包中的包体内容（不包括总笔数、总金额）
     *
     * @param paybackInfos 单个包体中的数据LIST 一般为10-15条记录
     * @param beginIdx     (from 0)
     * @return
     */
    private String getResponseDataBodyContent(List<LSPAYBACKINFO> paybackInfos, int beginIdx, int endIdx) {
        StringBuffer sb = new StringBuffer();
        for (int i = beginIdx; i < endIdx; i++) {
            LSPAYBACKINFO info = paybackInfos.get(i);
            //循环: 卡号20 姓名 20 金额 11
            String cardNo = StringUtils.leftPad(info.getAccount().trim(), 20, ' ');
            String nametmp = info.getCardname().trim();
            byte[] nameBytes = nametmp.getBytes();
            int nameByteLength = nameBytes.length;
            int nameStrLength = nametmp.length();
            String name = StringUtils.leftPad(nametmp, 20 - (nameByteLength - nameStrLength), ' ');
            String amt = StringUtils.leftPad(String.valueOf(info.getAmt()).trim(), 11, ' ');
            sb.append(cardNo).append(name).append(amt);
        }
        return sb.toString();
    }

    /**
     * 生成单个响应包
     * generateOneReponseDataPkg（）函数参数追加（计算长度）2010/07/13 haiyu
     * @param isFirstPkg 是否为首个响应包
     * @param bodyStr    响应包体中的内容
     * @return
     */
    private ResponseData generateOneReponseDataPkg(int count, boolean isFirstPkg, String bodyStr) {
        ResponseData responseData = new ResponseData();
        int length = 0;
        //包头处理
        setResponseDataHeader(responseData);

        //包体处理

        if (isFirstPkg) {
            //笔数
            String recordCount = StringUtils.leftPad(String.valueOf(this.totalCount), 6, ' ');
            //合计金额
            double amt = this.totalAmt / 100;
            String amtSum = StringUtils.leftPad(String.valueOf(amt), 13, ' ');

            responseData.setBodyData(recordCount + amtSum + bodyStr);
            //包头长度处理
            length = this.headLength + this.bodyHeadFieldlength + this.oneRecordLength *count;
        } else {
            responseData.setBodyData(bodyStr);
            //包头长度处理
            length = this.headLength + this.oneRecordLength * count;
        }

        responseData.setLength(length);
        String strlength = StringUtils.leftPad(String.valueOf(length), 4, ' ');
        responseData.setPkgLength(strlength);

        return responseData;
    }

}
