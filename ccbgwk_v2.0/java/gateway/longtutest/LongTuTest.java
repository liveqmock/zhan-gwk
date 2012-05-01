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

        <!������-->
        <string> ACCOUNT </string><string>billcode</string>
        <!���ֿ��� -->
        <string> CARDNAME </string><string>2008</string>
        <!��Ԥ�㵥λ-->
        <string> Bdgagency</string><string>value</string>
        <!�������˻���-->
        <string> GATHERINGBANKACCTNAME </string><string>3</string>
        <!�������˻�������-->
        <string> GATHERINGBANKNAME </string><double>0.0</double>
        <!�����˻���-->
        <string> GATHERINGBANKACCTCODE </string><string>value</string>
        <!�����֤��-->
        <string> IDNUMBER </string><string>value</string>
        <!����;-->
        <string> DIGEST </string><string>value</string>
        <!����������-->
        <string> BANK</string><string>value</string>
        <!����������-->
        <string> CREATEDATE </string><string>value</string>
        <!����Ч��ʼ����-->
        <string> Startdate</string><string>value</string>
        <!����Ч��ֹ����-->
        <string>enddate</string><string>value</string>
        <!�����ݲ�������	-->
        <string> action</string><string>value</string>
     */

    /*
    ����odsb���ص��¿���������Ϣ ������ַ������ݡ�
    �����죺
       1�������ÿ��һ���� �����쳣����
       2�����ͳɹ��� �����״̬��־

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
            m.put("BANK", "8015"); //?9	��������	BANK	NUMBER(16)	�������б���
            m.put("CREATEDATE", rs.getString("CREATEDATE"));
            m.put("STARTDATE", rs.getString("STARTDATE"));
            m.put("ENDDATE", rs.getString("ENDDATE"));
            m.put("ACTION", rs.getString("ACTION"));  //13	���ݲ�������	ACTION	VARCHAR2(32)	0-���� 1-�޸� 2-ɾ��
            cardList.add(m);
        }


        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

            //405 �̶�ֵ
            List rtnlist = service.writeOfficeCard("BANK.CCB", "8015", "2010", "405", cardList);
            for (int i = 0; i < rtnlist.size(); i++) {
                rtnlist.get(2);

            }
            int i = 0;

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //todo: �޸��ѷ���״̬
    }

    //4.2.4.2	������Ϣ����
    /*
    <list>
    <map>
    <!��������ˮ��-->
    <string> ID </string><string>value</string>
    <!������-->
    <string> ACCOUNT </string><string>value</string>
    <!���ֿ��� -->
    <string> CARDNAME </string><string>2008</string>
    <!����������-->
    <string> BUSIDATE </string><string>yymmdd</string>
    <!�����ѽ��-->
    <string> BUSIMONEY </string><double>0.0</double>
    <!�����ѵص�-->
    <string> BUSINAME </string><string>value</string>
    <!����ٻ�����-->
    <string> Limitdate </string><string>value</string>
    </list>

     */

    public void writeConsumeInfo() {
        BankService service = FaspServiceAdapter.getBankService();
        //ѭ��
/*
        List cardList = new ArrayList();
        Map m = new HashMap();
        m.put("ID", "201005270000001");
        m.put("ACCOUNT", "6283660015627785");
        m.put("CARDNAME", "��Ȫ");
        m.put("BUSIDATE", "20100527");
        m.put("BUSIMONEY", "17.00");
        m.put("BUSINAME", "�ൺ���쳬����ɽ�ֵ�");
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
    �Բ����ֻ�ȡ������Ϣ
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
