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
public class ResponseData extends TxnDataPkg {
}