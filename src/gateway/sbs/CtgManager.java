package gateway.sbs;

import com.ibm.ctg.client.ECIRequest;
import com.ibm.ctg.client.JavaGateway;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-5-10
 * Time: 20:58:47
 * To change this template use File | Settings | File Templates.
 */
public class CtgManager {

    private static Log logger = LogFactory.getLog(CtgManager.class);

    public  int iValidationFailed = 0;

    private JavaGateway javaGatewayObject;

    private  boolean bDataConv = true;
    private  String strDataConv = "ASCII";

    private  String strProgram = "SCLMPC";
    private  String strChosenServer = "haier";

    //SBS测试环境地址 192.168.91.2
    //SBS生产环境地址 192.168.91.5
    private  String strUrl = PropertyManager.getProperty("SBS_HOSTIP");
//    private static String strUrl =  "192.168.91.5";
//    private static int iPort = Integer.getInteger(PropertyManager.getProperty("SBS_HOSTPORT"));
    private  int iPort = 2006;

    private  int iCommareaSize = 32000;


    /*
    简单通讯测试方法，from于涛 200905
     */
    public  void processCtgTest(List list) {

        ECIRequest eciRequestObject = null;
        String buff = "";

        try {
            byte[] abytCommarea = new byte[iCommareaSize];

            javaGatewayObject = new JavaGateway(strUrl, iPort);

//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                System.out.println(e.toString());
//            }

            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);
            int x = 0;
            String zero = "010100100000000005";
            int no = 1001;

            for (int i = 1; i <= 1; i++) {
                if (eciRequestObject.SystemList.isEmpty() == true) {
                    System.out.println("No CICS servers have been defined.");
                    if (javaGatewayObject.isOpen() == true) {
                        i = i;
                        //javaGatewayObject.close();
                    }
                    //System.exit(0);
                }
                //打包
                buff = "TPEIa541  010       XD01XD01"; //包头内容，xxxx交易，010网点，MPC1终端，MPC1柜员，包头定长51个字符
                System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //打包包头

//                 List list = new ArrayList();//包体内容，将包体内容放入list中，有几个输入项，就add几个
                no = no + 1;
                String result = zero.substring(0, 14) + "" + no;

                //打包包体
                setValues(list, abytCommarea);

                System.out.println("发送包内容:\n" + new String(abytCommarea));

                //发送包
                eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                        strChosenServer, //CICS server
                        "1", //CICS userid
                        "1", //CICS password
                        strProgram, //CICS program to be run
                        null, //CICS transid to be run
                        abytCommarea, //Byte array containing the
                        // COMMAREA
                        iCommareaSize, //COMMAREA length
                        ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                        0);                       //ECI LUW token


                //获取返回报文

                String rtnStr = new String(abytCommarea);

                if (flowRequest(eciRequestObject) == true) {
                    //解sof
                    System.out.println("返回值11为\n" + rtnStr);
                }
                System.out.println("返回值22为\n" + rtnStr);

                if (javaGatewayObject.isOpen() == true) {
                    javaGatewayObject.close();
                }


            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /*
     消费信贷扣款成功后入帐处理: a541
     房贷扣款成功后入帐处理: a542
     消费信贷系统放款处理：aa54
      */
    public  byte[] processAccount(List list, String txncode) throws java.io.IOException {

        ECIRequest eciRequestObject = null;
        String buff = "";

        try {
            byte[] abytCommarea = new byte[iCommareaSize];

            javaGatewayObject = new JavaGateway(strUrl, iPort);

            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);
            int no = 1001;

            if (eciRequestObject.SystemList.isEmpty() == true) {
                System.out.println("No CICS servers have been defined.");
                if (javaGatewayObject.isOpen() == true) {
                    javaGatewayObject.close();
                }
            }
            //打包
            buff = "TPEI" + txncode + "  010       XD01XD01"; //包头内容，xxxx交易，010网点，MPC1终端，MPC1柜员，包头定长51个字符
            System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //打包包头

            no = no + 1;

            //打包包体
            setValues(list, abytCommarea);

//            System.out.println("发送包内容:\n" + new String(abytCommarea));
            logger.info("发送包内容:\n" + new String(abytCommarea));

            //发送包
            eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                    strChosenServer, //CICS server
                    "1", //CICS userid
                    "1", //CICS password
                    strProgram, //CICS program to be run
                    null, //CICS transid to be run
                    abytCommarea, //Byte array containing the
                    // COMMAREA
                    iCommareaSize, //COMMAREA length
                    ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                    0);                       //ECI LUW token


            //获取返回报文

            String rtnStr = new String(abytCommarea);

            if (flowRequest(eciRequestObject) == true) {
                //解sof
//                System.out.println("返回值11为\n" + rtnStr);
                logger.info("返回值11为\n" + rtnStr);
            }
//            System.out.println("返回值22为\n" + rtnStr);
            logger.info("返回值22为\n" + rtnStr);

            if (javaGatewayObject.isOpen() == true) {
                javaGatewayObject.close();
            }

            return abytCommarea;

        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new java.io.IOException("SBS系统不能连通或连接超时！");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /*
   批量代扣交易 a050
   批量代扣查询交易 a052
   批量代扣补帐交易 n054-补账
    */

    public  byte[] processBatchRequest(String TxnCode, List list) throws Exception {
        ECIRequest eciRequestObject = null;
        String buff = "";
        try {
            byte[] abytCommarea = new byte[iCommareaSize];

            javaGatewayObject = new JavaGateway(strUrl, iPort);

            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);

            if (eciRequestObject.SystemList.isEmpty() == true) {
                System.out.println("No CICS servers have been defined.");
                if (javaGatewayObject.isOpen() == true) {
                    javaGatewayObject.close();
                }
                throw new Exception("未定义 CICS 服务器，请确认！");
            }
            //打包
            buff = "TPEI" + TxnCode + "  010       XD01XD01"; //包头内容，xxxx交易，010网点，MPC1终端，MPC1柜员，包头定长51个字符
            System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //打包包头

            //打包包体
            setValues(list, abytCommarea);

//            System.out.println("发送包内容:\n" + new String(abytCommarea));
            logger.info("发送包内容:\n" + new String(abytCommarea));

            //发送包
            eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                    strChosenServer, //CICS server
                    "1", //CICS userid
                    "1", //CICS password
                    strProgram, //CICS program to be run
                    null, //CICS transid to be run
                    abytCommarea, //Byte array containing the
                    // COMMAREA
                    iCommareaSize, //COMMAREA length
                    ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                    0);                       //ECI LUW token


            //获取返回报文
            if (flowRequest(eciRequestObject) == true) {
                //解sof
//                System.out.println("CICS 处理正常，返回:" + new String(abytCommarea));
                logger.info("CICS 处理正常，返回:" + new String(abytCommarea));
            }

            String returnbuffer = new String(abytCommarea);
            String formcode = returnbuffer.substring(21, 25);
//            System.out.println("返回FORM CODE:" + formcode);
            logger.info("返回FORM CODE:" + formcode);
            if (javaGatewayObject.isOpen() == true) {
                javaGatewayObject.close();
            }

            return abytCommarea;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /*
    批量扣款查询包结果处理
     */
    public  void getBatchQueryMsg(byte[] buffer, BatchQueryResult result) {
        int k = 0;
        try {
            int pos = 29;
            byte[] bFloflg = new byte[1];
            byte[] bTotcnt = new byte[6];
            byte[] bCurcnt = new byte[6];

            System.arraycopy(buffer, pos, bFloflg, 0, bFloflg.length);
            pos += bFloflg.length;
            System.arraycopy(buffer, pos, bTotcnt, 0, bTotcnt.length);
            pos += bTotcnt.length;
            System.arraycopy(buffer, pos, bCurcnt, 0, bCurcnt.length);
            pos += bCurcnt.length;

            result.setFloflg(new String(bFloflg));
            result.setTotcnt(new String(bCurcnt));
            result.setCurcnt(new String(bCurcnt));

            int curcnt = Integer.parseInt(result.getCurcnt());

            byte[] bCusidt = new byte[18];
            byte[] bApcode = new byte[60];
            byte[] bLasbal = new byte[18];
            byte[] bCurcde = new byte[1];

            for (k = 0; k < curcnt; k++) {
                BalanceRecord record = new BalanceRecord();
                System.arraycopy(buffer, pos, bCusidt, 0, bCusidt.length);
                pos += bCusidt.length;
                System.arraycopy(buffer, pos, bApcode, 0, bApcode.length);
                pos += bApcode.length;
                System.arraycopy(buffer, pos, bLasbal, 0, bLasbal.length);
                pos += bLasbal.length;
                System.arraycopy(buffer, pos, bCurcde, 0, bCurcde.length);
                pos += bCurcde.length;
                record.setCUSIDT(new String(bCusidt));
                record.setAPCODE(new String(bApcode));
                record.setLASBAL(new String(bLasbal));
                record.setCURCDE(new String(bCurcde));
                result.add(record);
            }
//            return result;
        } catch (Exception e) {
            System.out.println("报文解包时出现问题：" + k);
            logger.error("报文解包时出现问题：" + k);
//            Debug.debug(e);
            throw new RuntimeException(e);
        }
    }

    private byte[] getBytes(String source) throws java.io.UnsupportedEncodingException {
        if (bDataConv) {
            return source.getBytes(strDataConv);
        } else {
            return source.getBytes();
        }
    }

    public  void setValues(List list, byte[] bb) {
        int start = 51;
        for (int i = 1; i <= list.size(); i++) {
            String value = list.get(i - 1).toString();

            String size = "";
            try {
                size = new String(value.getBytes("GBK"), "ISO-8859-1");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  System.out.println(i+" "+start+" "+value.length()+" "+value);

//            System.out.println(i + " " + start + " " + size.length() + " " + value);
            logger.info(i + " " + start + " " + size.length() + " " + value);
            setVarData(start, value, bb); //根据list的数量（即输入项的数量），将内容循环加入包体
            start = start + size.length() + 2;
        }
    }

    public  void setVarData(int pos, String data, byte[] aa) {

        //说明，经测试发现，包体内容的内容长度，定长2字符，必须用十六进制形式发送
        //比如，第一个输入项的输入内容为“111111”，长度为6个字符，如果在包体中写入"06111111"，则CTG Server端无法正常读取字符长度，
        //"06"必须用十六进制形式发送，即0x00 0x60
        //如果包体中有中文字符，测了一下不能正确取到中文字符的长度，使用UTF-8字符集可以取到正确的中文字符的长度
        String size = "";
        try {
            size = new String(data.getBytes("GBK"), "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        short len = (short) size.length();

//		short len = (short)data.length();
        byte[] slen = new byte[2];
        slen[0] = (byte) (len >> 8);
        slen[1] = (byte) (len >> 0);
        System.arraycopy(slen, 0, aa, pos, 2);
        System.arraycopy(data.getBytes(), 0, aa, pos + 2, len);
    }


    private  boolean flowRequest(ECIRequest requestObject) throws Exception {
        int iRc = javaGatewayObject.flow(requestObject);
        String msg = null;
        switch (requestObject.getCicsRc()) {
            case ECIRequest.ECI_NO_ERROR:
                if (iRc == 0) {
                    return true;
                } else {
                    if (javaGatewayObject.isOpen() == true) {
                        javaGatewayObject.close();
                    }
                    throw new Exception("SBS Gateway 出现错误("
                            + requestObject.getRcString()
                            + "), 请查明原因，重新发起交易");
                }
            case ECIRequest.ECI_ERR_SECURITY_ERROR:
                msg = "SBS CICS: 用户名或密码错误";
                break;
            case ECIRequest.ECI_ERR_TRANSACTION_ABEND:
                msg = "SBS CICS : 没有权限运行此笔CICS交易";
                break;
            default:
                msg = "SBS CICS : 出现错误，请查找原因。" + requestObject.getCicsRcString();
        }
//        System.out.println("ECI returned: " + requestObject.getCicsRcString());
        logger.info("ECI returned: " + requestObject.getCicsRcString());
//        System.out.println("Abend code was " + requestObject.Abend_Code + " ");
        logger.info("Abend code was " + requestObject.Abend_Code + " ");
        if (javaGatewayObject.isOpen() == true) {
            javaGatewayObject.close();
        }
        throw new Exception(msg);
    }


}
