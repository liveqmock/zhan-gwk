package gateway.mbs.xsocketserver.domain;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-6-30
 * Time: 12:51:44
 * To change this template use File | Settings | File Templates.
 */


/*
�ֶ�	����	����
������	4	�������ĳ���(�������ֶα���)
�����ֱ��	3	001Ϊ��ɽ
������	10	����Ӫҵ������
����Ա���	8	���в���Ա���
��������־	1	1���к�����  0����
���״���	4	��ʶ��������
�������	3	000���ɹ�  ����������
����ʱ��	14	24ʱ�ƣ�YYYYMMDDHHMISS
�汾��	2	�ý��ױ��İ汾��
MACУ��	16	��֤���װ��ĺϷ���

 */
public class TxnPkgDataBean {
    String pkgLength;
    String areaCode; //�����ֱ��
    String branchId; //������
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