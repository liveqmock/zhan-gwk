package gateway.mbs.xsocketserver.requestaction;

import com.ccb.dao.LSPAYBACKINFO;
import gateway.mbs.xsocketserver.RequestAction;
import gateway.mbs.xsocketserver.domain.RequestData;
import gateway.mbs.xsocketserver.domain.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.utils.BusinessDate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:27:07
 * To change this template use File | Settings | File Templates.
 */
public class T2000Action implements RequestAction {
    private Log logger = LogFactory.getLog(this.getClass());

    private static final String txnCode = "2000";
    private RequestData requestData;
    private List<RequestData> requestDataList;
    private ResponseData responseData = new ResponseData();

    //特色平台响应包总笔数
    private int totalCount = 0;
    //特色平台响应包总金额
    private double totalAmt = 0.00;
    private String voucherid;
    private String year;
    private String paybackdate; //yyyy-MM-dd

    private List<LSPAYBACKINFO> paybackInfos = new ArrayList();

    public T2000Action() {

    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setRequestDataList(List<RequestData> requestDataList) {
        this.requestDataList = requestDataList;
    }

    /**
     * 先做两个检查,若有问题,返回错误信息包301
     * 1 总分核对
     * 2  数据库明细核对
     * body字符串截取修改（52 -> 51） 2010/07/13 haiyu
     */
    public void process() {
        String bodyAll = "";       //所有包的包体（第一个包的[笔数]，[总金额]除外）组合
        int count = 0;
        for (RequestData requestData : requestDataList) {
            count++;
            String body = requestData.getBodyData();
            if (count == 1) {
                this.requestData = requestData; //2010/07/15 haiyu 
            }
             bodyAll += body;
        }
        logger.info("交易2000 组合:" + bodyAll);
        year = bodyAll.substring(0, 4).trim();
        voucherid = bodyAll.substring(4, 24).trim();
        paybackdate = bodyAll.substring(24, 32).trim();

        totalCount = Integer.parseInt(bodyAll.substring(32, 38).trim());
        totalAmt = Double.parseDouble(bodyAll.substring(38, 51).trim());
        String tempBody = "";
        int temp = 51;
        for (int i = 1;i <= totalCount;i++) {
            tempBody = bodyAll.substring(temp,temp+51);
            getPaybackInfoListFromRequestDataBody(tempBody);  //截取包内容 
            temp = temp + 51;
            logger.info("交易2000 第"+i+"笔:" + tempBody);
        }
        //check
        if (checkTotalRequestData()) {
            if (checkExistRequestData()) {
                saveDataToDB();
                setResponseDataHeader("000");
            } else {
                setResponseDataHeader("100");   //部分数据不存在
            }
        } else {
            setResponseDataHeader("301");   //总分金额错
        }
    }

    /**
     * 多包情况下 向客户端返回回执:
     * 2010/07/15 haiyu
     */

    public void noProcess(){
        RequestData reqData = (RequestData)requestDataList.get(0);
        this.requestData = reqData;
        setResponseDataHeader("000");
    }
    private void getPaybackInfoListFromRequestDataBody(String body) {

        int ptr = 0;
        LSPAYBACKINFO info = new LSPAYBACKINFO();
        info.setAccount(body.substring(ptr, ptr + 20).trim());
        ptr += 20;
        info.setAmt(Double.parseDouble(body.substring(ptr, ptr + 11).trim()));
        ptr += 11;
        String result = body.substring(ptr, ptr + 20).trim();
        ptr += 20;
        if ("000000".equals(result)) { //扣款成功
            info.setStatus("01"); //扣款成功
        }
        paybackInfos.add(info);
    }

    /**
     * 总分(总金额)检查
     * remark:获取包中的每条记录的金额之和 与 包中的表头中的总金额相比 ？
     * @return
     */
    private boolean checkTotalRequestData() {

        int count = 0;
        double amt = 0.00;

        for (LSPAYBACKINFO info : paybackInfos) {
            count++;
            amt += info.getAmt();
        }
        if (amt != totalAmt) {
            //返回301错误 金额不符
            logger.error("交易T2000:总分核对不符.");
            return false;
        } else {
            return true;
        }
    }

    //TODO

    /**
     * 2010/07/13 haiyu
     * 数据存在与否　检查
     * remark：
     *     查询条件：
     *        １，数据支付令号；２，支付年度；３，卡号；４，金额
     *
     */
    private boolean checkExistRequestData() {
        boolean exist = true;
        LSPAYBACKINFO paybackinfo = new LSPAYBACKINFO();
        for (LSPAYBACKINFO info : paybackInfos) {
            String sqlWhere = " where voucherid = '" + voucherid + "' and year = '" + year +
                    "' and account = '" + info.getAccount() + "' and amt=" + info.getAmt();
            logger.info("交易T2000:"+sqlWhere);
            paybackinfo = (LSPAYBACKINFO) paybackinfo.findFirstByWhere(sqlWhere);
            if (paybackinfo == null){
                if (exist){
                    exist = false;
                }
                logger.error("交易T2000－数据检查　数据在数据库中不存在　"+ "支付令号："+voucherid +"；年度："+year+
                        "；卡号："+info.getAccount() +"；金额："+info.getAmt());
            }
        }
        return exist;
    }

    /**
     *  2010/07/13 haiyu
     * 查询条件追加：  １，数据支付令号；２，支付年度；３，卡号；４，金额
     */
    private void saveDataToDB() {
        LSPAYBACKINFO paybackinfo = new LSPAYBACKINFO();
        for (LSPAYBACKINFO info : paybackInfos) {
            String sqlWhere = " where voucherid = '" + voucherid + "' and year = '" + year +
                    "' and account = '" + info.getAccount() + "' and amt=" + info.getAmt();
            paybackinfo = (LSPAYBACKINFO) paybackinfo.findFirstByWhere(sqlWhere);
            if (paybackinfo != null) {
                paybackinfo.setPaybackdate(info.getPaybackdate());
                paybackinfo.setStatus(info.getStatus());
                paybackinfo.setRemark(info.getRemark());
                paybackinfo.setOperdate(BusinessDate.getTodaytime());    //haiyu 2010/07/21
                paybackinfo.setPaybackdate(BusinessDate.getToday());
                paybackinfo.updateByWhere(sqlWhere);
            }else{
                logger.error("交易T2000:还款成功状态更新出现问题,未找到对应明细记录." + sqlWhere);
                //TODO  返回代理平台异常
            }
        }

    }

    private void setResponseDataHeader(String errCode) {
        //包头处理
        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("0"); //无后续包
        responseData.setTxnCode(txnCode);
        responseData.setErrCode(errCode);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");

        //包长度设置
        responseData.setLength(65);
        String strlength = StringUtils.leftPad(String.valueOf(65), 4, ' ');
        responseData.setPkgLength(strlength);

    }


}