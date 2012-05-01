package gateway.longtutest;

import gov.mof.fasp.service.BankService;
import gov.mof.fasp.service.CommonQueryService;
import gov.mof.fasp.service.ElementService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.db.RecordSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-5-5
 * Time: 22:24:11
 * To change this template use File | Settings | File Templates.
 */
public class LongTuTest {

    private static final Log logger = LogFactory.getLog(LongTuTest.class);

    public static void main(String argv[]) {

        LongTuTest test = new LongTuTest();


         test.getAllElementInfo();
//       test.queryElementVersion();
        //test.syncElementCode();
//        test.writeOfficeCardInfo();
//        test.writeConsumeInfo();
//        test.testAdapter_queryservice();
//        test.testHttp();
    }


    public void getAllElementInfo() {
        ElementService service = FaspServiceAdapter.getElementService();
        List ElementCodeList = new ArrayList();
        Map m = new HashMap();
        m.put("CODE", "20101");
        m.put("NAME", "XXXXX");
        ElementCodeList.add(m);

        DatabaseConnection conn = ConnectionManager.getInstance().get();
        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);
            conn.begin();
            int rtn = conn.executeUpdate(" delete from  ls_bdgagency where areacode = '266100'");
            //TODO rtn
            List rtnlist = service.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2010);
            for (int i = 0; i < rtnlist.size(); i++) {
                Map m1 = (Map) rtnlist.get(i);
                if (i == rtnlist.size() - 1) {
                    String version = (String) m1.get("version");
                    System.out.println(" version=" + version);
                } else {
                    String code = (String) m1.get("code");
                    String name = (String) m1.get("name");
                    String guid = (String) m1.get("guid");
                    String levelno = (String) m1.get("levelno");
                    String supercode = (String) m1.get("supercode");
                    String isleaf = (String) m1.get("isleaf");
                    System.out.println("code=" + code + name);
                    String sql = "insert into ls_bdgagency t" +
                            "              (t.areacode," +
                            "               t.code," +
                            "               t.name," +
                            "               t.guid," +
                            "               t.levelno," +
                            "               t.supercode," +
                            "               t.isleaf," +
                            "               t.version," +
                            "               t.remark," +
                            "               t.operid," +
                            "               t.operdate) values (" +
                            " '266100', " +
                            " '" + code + "', " +
                            " '" + name + "', " +
                            " '" + guid + "', " +
                            " '" + levelno + "', " +
                            " '" + supercode + "', " +
                            " '" + isleaf + "', " +
                            " 0, " +
                            " 'remark', " +
                            " 'auto', " +
                            " sysdate " +
                            "              )";
                    rtn = conn.executeUpdate(sql);
                }
            }
            conn.commit();
            int i = 0;
        } catch (Exception e) {
            conn.rollback();
            logger.error(e);
        } finally {
            ConnectionManager.getInstance().release();
        }
    }

    public void queryElementVersion() {
        ElementService service = FaspServiceAdapter.getElementService();
        List elementCodeList = new ArrayList();
//        Map m = new HashMap();
//        m.put("CODE", "20101");
//        m.put("NAME", "XXXXX");
        elementCodeList.add("001");
        elementCodeList.add("0010010010000000000011111");
        elementCodeList.add("aabbcc");      //????

        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

            Map rtnMap = service.queryElementVersion("BANK.CCB", 2010, elementCodeList);
            rtnMap.clear();
            int i = 0;

//            service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void syncElementCode() {
        ElementService service = FaspServiceAdapter.getElementService();
        List elementCodeList = new ArrayList();
        Map m = new HashMap();
        m.put("key", "001");
        m.put("value", "0");
//        elementCodeList.add("001");
//        elementCodeList.add("0010010010000000000011111");
//        elementCodeList.add("aabbcc");      //????

        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

            List rtnlist = service.syncElementCode("BANK.CCB", 2010, m);
            rtnlist.clear();
            int i = 0;

//            service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /*

        <!—卡号-->
        <string> ACCOUNT </string><string>billcode</string>
        <!—持卡人 -->
        <string> CARDNAME </string><string>2008</string>
        <!—预算单位-->
        <string> Bdgagency</string><string>value</string>
        <!—还款账户名-->
        <string> GATHERINGBANKACCTNAME </string><string>3</string>
        <!—还款账户开户行-->
        <string> GATHERINGBANKNAME </string><double>0.0</double>
        <!—还账户号-->
        <string> GATHERINGBANKACCTCODE </string><string>value</string>
        <!—身份证号-->
        <string> IDNUMBER </string><string>value</string>
        <!—用途-->
        <string> DIGEST </string><string>value</string>
        <!—开户银行-->
        <string> BANK</string><string>value</string>
        <!—开卡日期-->
        <string> CREATEDATE </string><string>value</string>
        <!—有效起始日期-->
        <string> Startdate</string><string>value</string>
        <!—有效终止日期-->
        <string>enddate</string><string>value</string>
        <!—数据操作类型	-->
        <string> action</string><string>value</string>
     */

    /*
    根据odsb返回的新开卡基本信息 向财政局发送数据。
    待改造：
       1、改造成每笔一发送 便于异常处理
       2、发送成功后 须更新状态标志

     */

    public void writeOfficeCardInfo() {
        BankService service = FaspServiceAdapter.getBankService();
        List cardList = new ArrayList();

        DatabaseConnection conn = ConnectionManager.getInstance().get();

        RecordSet rs = conn.executeQuery("select * from ls_cardbaseinfo where sentflag='0' and bdgagency='016003' ");

        while (rs.next()) {
            Map m = new HashMap();
            m.put("ACCOUNT", rs.getString("account"));
            m.put("CARDNAME", rs.getString("cardname"));
            m.put("BDGAGENCY", rs.getString("BDGAGENCY"));
            m.put("GATHERINGBANKACCTNAME", rs.getString("GATHERINGBANKACCTNAME"));
            m.put("GATHERINGBANKNAME", rs.getString("GATHERINGBANKNAME"));
            m.put("GATHERINGBANKACCTCODE", rs.getString("GATHERINGBANKACCTCODE"));
            m.put("IDNUMBER", rs.getString("IDNUMBER"));
            m.put("DIGEST", rs.getString("DIGEST"));
            m.put("BANK", "8015"); //?9	开户银行	BANK	NUMBER(16)	财政银行编码
            m.put("CREATEDATE", rs.getString("CREATEDATE"));
            m.put("STARTDATE", rs.getString("STARTDATE"));
            m.put("ENDDATE", rs.getString("ENDDATE"));
            m.put("ACTION", rs.getString("ACTION"));  //13	数据操作类型	ACTION	VARCHAR2(32)	0-新增 1-修改 2-删除
            cardList.add(m);
        }


        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

            //405 固定值
            List rtnlist = service.writeOfficeCard("BANK.CCB", "8015", "2010", "405", cardList);
            for (int i = 0; i < rtnlist.size(); i++) {
                rtnlist.get(2);

            }
            int i = 0;

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //todo: 修改已发送状态
    }

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

    public void writeConsumeInfo() {
        BankService service = FaspServiceAdapter.getBankService();
        //循环
/*
        List cardList = new ArrayList();
        Map m = new HashMap();
        m.put("ID", "201005270000001");
        m.put("ACCOUNT", "6283660015627785");
        m.put("CARDNAME", "沈函泉");
        m.put("BUSIDATE", "20100527");
        m.put("BUSIMONEY", "17.00");
        m.put("BUSINAME", "青岛乐天超市崂山分店");
        m.put("Limitdate", "20100616");
        cardList.add(m);
*/

        DatabaseConnection conn = ConnectionManager.getInstance().get();

//        String inac_date = "2010-05-29";
//        String inac_date = "2010-06-02";
//        String inac_date = "2010-06-03";
//        String inac_date = "2010-05-30";
//        String inac_date = "2010-06-04";
        String inac_date = "2010-06-08";
        String sql = "select id,account,cardname,busidate,busimoney,businame,limitdate " +
                " from ls_consumeinfo where status='10' and inac_date='" + inac_date + "' " +
                " order by id ";

        RecordSet rs = null;
        try {

            rs = conn.executeQuery(sql);
            List cardList = new ArrayList();
            while (rs.next()) {
                Map m = new HashMap();
                String id = rs.getString("id");
                String account = rs.getString("account");
                String cardname = rs.getString("cardname");
                String busidate = rs.getString("busidate");
                Double busimoney = rs.getDouble("busimoney");
                String businame = rs.getString("businame");
                String limitdate = rs.getString("limitdate");
                if (busimoney <= 0) {
                     limitdate="";
                }
                m.put("ID", id);
                m.put("ACCOUNT", account);
                m.put("CARDNAME", cardname);
                m.put("BUSIDATE", busidate);
                m.put("BUSIMONEY", busimoney);
                m.put("BUSINAME", businame);
                m.put("Limitdate", limitdate);
                cardList.add(m);

            }
            if (cardList.size() > 0) {

                List rtnlist = service.writeConsumeInfo("BANK.CCB", "8015", "2010", "405", cardList);
//                List rtnlist = null;
                for (int i = 0; i < rtnlist.size(); i++) {
                    Map m1 = (Map) rtnlist.get(i);
                    String result = (String) m1.get("result");
                    if ("SUCCESS".equalsIgnoreCase(result)) {
                        System.out.println(result);
                    }

                }
            }
            int i = 0;

//            service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (rs != null) {
                rs.close();
            }
            ConnectionManager.getInstance().release();
        }

    }

    /*
    自财政局获取还款信息
     */

    public void testAdapter_queryservice() {
        CommonQueryService service = FaspServiceAdapter.getCommonQueryService();
        List cardList = new ArrayList();
        Map m = new HashMap();
//        m.put("bankcode", "8015");
        m.put("VOUCHERID", "10-016001-000001");


        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

            List rtnlist = service.getQueryListBySql("BANK.CCB", "queryConsumeInfo", m, "2010");
            for (int i = 0; i < rtnlist.size(); i++) {
                rtnlist.get(1);

            }
            int i = 0;

//            service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
