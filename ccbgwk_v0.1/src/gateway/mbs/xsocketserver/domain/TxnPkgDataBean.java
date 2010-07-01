package gateway.mbs.xsocketserver.domain;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-6-30
 * Time: 12:51:44
 * To change this template use File | Settings | File Templates.
 */


/*
字段	长度	描述
包长度	4	整个包的长度(包含此字段本身)
财政局编号	3	001为崂山
网点编号	10	银行营业网点编号
操作员编号	8	银行操作员编号
后续包标志	1	1＝有后续包  0＝无
交易代码	4	标识交易类型
错误代码	3	000＝成功  其它＝错误
交易时间	14	24时制：YYYYMMDDHHMISS
版本号	2	该交易报文版本号
MAC校验	16	验证交易包的合法性

 */
public class TxnPkgDataBean {
    String pkgLength;
    String areaCode; //财政局编号
    String branchId; //网点编号
    String operId;
    String nextFlag;
    String txnCode;
    String errCode;
    String txnTime;
    String version;
    String mac;
    int[] fieldLength = {4,3,10,8,1,4,3,14,2,16};

    public String getPkgLength() {
        return pkgLength;
    }

    public void setPkgLength(String pkgLength) {
        this.pkgLength = pkgLength;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int[] getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(int[] fieldLength) {
        this.fieldLength = fieldLength;
    }
}