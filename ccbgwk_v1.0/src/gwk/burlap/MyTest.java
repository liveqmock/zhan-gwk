package gwk.burlap;

import com.caucho.burlap.client.BurlapProxyFactory;
import gov.mof.fasp.service.BankService;
import gov.mof.fasp.service.CommonQueryService;
import gov.mof.fasp.service.ElementService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.db.RecordSet;

import java.io.*;
import java.net.MalformedURLException;
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
public class MyTest {

    private static final Log logger = LogFactory.getLog(MyTest.class);

    public static void main(String argv[]) {

        MyTest test = new MyTest();
        //test.testBurlap();

//         test.testQuery();

//         test.getAllElementInfo();
//       test.queryElementVersion();
        //test.syncElementCode();
//        test.writeOfficeCardInfo();
//        test.writeConsumeInfo();
//        test.testAdapter_queryservice();
//        test.testHttp();
    }

    public void testBurlap() {

//        String url = "http://www.caucho.com/hessian/test/basic";

//   HessianProxyFactory factory = new HessianProxyFactory();
//   Basic basic = (Basic) factory.create(Basic.class, url);
//
//   System.out.println("hello(): " + basic.hello());


//        String url = "http://localhost:7001/burlap";
        String url = "http://48.0.204.250:7002/exserver/remoting/elementservice";
//        String url = "http://48.135.204.1:7004/exserver/remoting/service/bankservice";

        BurlapProxyFactory factory = new BurlapProxyFactory();
        IElement element = null;
        try {
            element = (IElement) factory.create(IElement.class, url);
        } catch (MalformedURLException e) {
            System.out.println("occur exception: " + e);
        }
        try {
            List rtnlist = element.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2010);
            System.out.println(rtnlist);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        System.out.println(b.queryAllElementCode("BANK.CCB","001-001-001-001",2010));
//        System.out.println(h.getOfficeCardStatus("BANK.CCB","CCB","2010","guid0001111"));


    }

    public void getAllElementInfo() {
        ElementService service = FaspServiceAdapter.getElementService();
        List ElementCodeList = new ArrayList();
        Map m = new HashMap();
        m.put("CODE", "20101");
        m.put("NAME", "XXXXX");
        ElementCodeList.add(m);

//        ConnectionManager cm = ConnectionManager.getInstance();
        DatabaseConnection conn = ConnectionManager.getInstance().get();

        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

//            DatabaseConnection dc = cm.getConnection();
            conn.begin();
            int rtn = conn.executeUpdate(" delete from  ls_bdgagency ");
            //TODO rtn
            List rtnlist = service.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2010);
            for (int i = 0; i < rtnlist.size(); i++) {
                Map m1 = (Map) rtnlist.get(i);
                if (i == rtnlist.size() - 1) {
                    String version = (String) m1.get("version");
                    System.out.println(" ======================= ");
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

/*

    public void testHttp() {

        String url = "http://48.135.204.1:7004/exserver/remoting/elementservice";
        String host = "48.135.204.1";
//    String https = xxx;

        try {
            ContentProducer cp = new ContentProducer() {
                public void writeTo(OutputStream outstream) throws IOException {
                    Writer writer = new OutputStreamWriter(outstream, "GBK");
                    writer.write("<?xml version=\"1.0\" encoding=\"GBK\">");
                    writer.write("<burlap:call>");
                    writer.write("<method>queryAllElementCode</method>");
                    writer.write("<string>BANK.CCB</string>");
                    writer.write("<string>BDGAGENCY</string>");
                    writer.write("<int>2010</int>");
                    writer.write("</burlap:call>");
                    writer.flush();
                }
            };
            HttpEntity reqentity = new EntityTemplate(cp);
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(reqentity);


            HttpClient httpclient = new DefaultHttpClient();
//        HttpGet httpget = new HttpGet("http://localhost/");
            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();

            System.out.println("=======================================");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }

            // Consume response content
            if (entity != null) {
                entity.consumeContent();
            }

            System.out.println("=======================================");

            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();

*/
/*

            if (r_entity != null) {
                System.out.println("tools=" +EntityUtils.toString(r_entity));
                InputStream instream = r_entity.getContent();
                int l;
                byte[] tmp = new byte[2048];
                while ((l = instream.read(tmp)) != -1) {

                }
                String r = new String(tmp);
                System.out.println("response=" + r);
            }
*//*

        } catch (Exception e) {

        }
    }

*/


    public void testQuery() {
        ElementService service = FaspServiceAdapter.getElementService();
        List ElementCodeList = new ArrayList();

        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

//            DatabaseConnection dc = cm.getConnection();
            //TODO rtn
            List rtnlist = service.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2010);
            for (int i = 0; i < rtnlist.size(); i++) {
                Map m1 = (Map) rtnlist.get(i);
                if (i == rtnlist.size() - 1) {
                    String version = (String) m1.get("version");
                    System.out.println(" ======================= ");
                    System.out.println(" version=" + version);
                } else {
                    String code = (String) m1.get("code");
                    String name = (String) m1.get("name");
                    String guid = (String) m1.get("guid");
                    String levelno = (String) m1.get("levelno");
                    String supercode = (String) m1.get("supercode");
                    String isleaf = (String) m1.get("isleaf");
                    System.out.println("code=" + code + name);

                }
            }

            int i = 0;

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            //ConnectionManager.getInstance().release();
        }
    }

}
