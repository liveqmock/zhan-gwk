package gateway.mbs.xsocketserver.requestaction;

import gateway.mbs.xsocketserver.RequestAction;
import gateway.mbs.xsocketserver.domain.RequestData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 10:27:07
 * To change this template use File | Settings | File Templates.
 */
public class T1000Action implements RequestAction {
    private Log log = LogFactory.getLog(this.getClass());

    public void dealReqeust(RequestData requestData) {
        //
        String body = requestData.getBodyData();

        String voucherid = body.substring(0, 20);
        String year = body.substring(20, 24);

        log.info(voucherid + " " + year);

    }

    public void dealResponse(RequestData requestData, RequestData responseData) {

        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("0"); //无后续包
        responseData.setTxnCode("1000");
        responseData.setErrCode("000");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");
        //包体
        //笔数
        String recordCount = StringUtils.leftPad("2", 6, ' ');
        //合计金额
        String totalAmt = StringUtils.leftPad("1123.45", 13, ' ');

        //循环: 卡号20 金额 11
        String cardNo1 = StringUtils.leftPad("A20100701003", 20, ' ');
        String name1 = StringUtils.leftPad("姓名甲", 20, ' ');
        String amt1 = StringUtils.leftPad("123.45", 11, ' ');
        String cardNo2 = StringUtils.leftPad("A20100701004", 20, ' ');
        String name2 = StringUtils.leftPad("姓名乙", 20, ' ');
        String amt2 = StringUtils.leftPad("1000.00", 11, ' ');

        responseData.setBodyData(recordCount + totalAmt + cardNo1 + name1 + amt1 + cardNo2 + name2 + amt2);

        //长度处理
        int length = 65 + 19 + 51 * 2;
        responseData.setLength(length);
        String strlength = StringUtils.leftPad(String.valueOf(length), 4, ' ');
        responseData.setPkgLength(strlength);
    }
    public void dealResponse1(RequestData requestData, RequestData responseData) {

        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("1"); //无后续包
        responseData.setTxnCode("1000");
        responseData.setErrCode("000");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");
        //包体
        //笔数
        String recordCount = StringUtils.leftPad("2", 6, ' ');
        //合计金额
        String totalAmt = StringUtils.leftPad("1123.45", 13, ' ');

        //循环: 卡号20 金额 11
        String cardNo1 = StringUtils.leftPad("AA20100701001", 20, ' ');
        String name1 = StringUtils.leftPad("姓名甲", 20, ' ');
        String amt1 = StringUtils.leftPad("123.45", 11, ' ');
        String cardNo2 = StringUtils.leftPad("AA20100701002", 20, ' ');
        String name2 = StringUtils.leftPad("姓名乙", 20, ' ');
        String amt2 = StringUtils.leftPad("1000.00", 11, ' ');

        responseData.setBodyData(recordCount + totalAmt + cardNo1 + name1 + amt1 + cardNo2 + name2 + amt2);

        //长度处理
        int length = 65 + 19 + 51 * 2;
        responseData.setLength(length);
        String strlength = StringUtils.leftPad(String.valueOf(length), 4, ' ');
        responseData.setPkgLength(strlength);
    }
    public void dealResponse1a(RequestData requestData, RequestData responseData) {

        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("1"); //无后续包
        responseData.setTxnCode("1000");
        responseData.setErrCode("000");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");
        //包体
        //笔数
        String recordCount = StringUtils.leftPad("2", 6, ' ');
        //合计金额
        String totalAmt = StringUtils.leftPad("1123.45", 13, ' ');

        //循环: 卡号20 金额 11
        String cardNo1 = StringUtils.leftPad("AA20100701003", 20, ' ');
        String name1 = StringUtils.leftPad("姓名甲", 20, ' ');
        String amt1 = StringUtils.leftPad("123.45", 11, ' ');
        String cardNo2 = StringUtils.leftPad("AA20100701004", 20, ' ');
        String name2 = StringUtils.leftPad("姓名乙", 20, ' ');
        String amt2 = StringUtils.leftPad("1000.00", 11, ' ');

        responseData.setBodyData(recordCount + totalAmt + cardNo1 + name1 + amt1 + cardNo2 + name2 + amt2);

        //长度处理
        int length = 65 + 19 + 51 * 2;
        responseData.setLength(length);
        String strlength = StringUtils.leftPad(String.valueOf(length), 4, ' ');
        responseData.setPkgLength(strlength);
    }
    public void dealResponse1b(RequestData requestData, RequestData responseData) {

        responseData.setAreaCode(requestData.getAreaCode());
        responseData.setBranchId(requestData.getBranchId());
        responseData.setOperId(requestData.getOperId());
        responseData.setNextFlag("0"); //无后续包
        responseData.setTxnCode("1000");
        responseData.setErrCode("000");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        responseData.setTxnTime(sdf.format(date));
        responseData.setVersion("01"); //TODO ??
        responseData.setMac("_macdatamacdata_");
        //包体
        //笔数
        String recordCount = StringUtils.leftPad("2", 6, ' ');
        //合计金额
        String totalAmt = StringUtils.leftPad("1123.45", 13, ' ');

        //循环: 卡号20 金额 11
        String cardNo1 = StringUtils.leftPad("AA20100701005", 20, ' ');
        String name1 = StringUtils.leftPad("姓名甲", 20, ' ');
        String amt1 = StringUtils.leftPad("123.45", 11, ' ');
        String cardNo2 = StringUtils.leftPad("AA20100701006", 20, ' ');
        String name2 = StringUtils.leftPad("姓名乙", 20, ' ');
        String amt2 = StringUtils.leftPad("1000.00", 11, ' ');

        responseData.setBodyData(recordCount + totalAmt + cardNo1 + name1 + amt1 + cardNo2 + name2 + amt2);

        //长度处理
        int length = 65 + 19 + 51 * 2;
        responseData.setLength(length);
        String strlength = StringUtils.leftPad(String.valueOf(length), 4, ' ');
        responseData.setPkgLength(strlength);
    }
}
