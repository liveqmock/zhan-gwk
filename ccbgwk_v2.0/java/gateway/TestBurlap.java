package gateway;

import gateway.service.BankService;
import gateway.service.CommonQueryService;
import gateway.service.ElementService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-1-5
 * Time: ����10:06
 * To change this template use File | Settings | File Templates.
 */
// @Resource(name = "gwk266001elementService")
   // �� @Autowired @Qualifier("gwk266001elementService")
   // private ElementService elementService;
    //
public class TestBurlap {
    public static void main(String[] args) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
        //new BurlapTest().testQueryAllElementCode(beanFactory);
        ElementService service = (ElementService) beanFactory.getBean("elementService");
        List rtnlist = service.queryAllElementCode("����.CCB", "BDGAGENCY", 2010);
        for (Object obj : rtnlist) {
            System.out.println((String) obj);
        }
    }

    public void testQueryAllElementCode(BeanFactory beanFactory) {
        ElementService service = (ElementService) beanFactory.getBean("elementService");
        // 1-��ѯ��������
        List rtnlist = service.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2010);
        System.out.println(" ===============��ѯ��������======================= ");
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
                System.out.println("========" + code + name + guid + levelno + supercode + isleaf);
            }
        }
    }

    public void testQueryElementVersion(BeanFactory beanFactory) {
        ElementService service = (ElementService) beanFactory.getBean("elementService");
        // 2-��ѯ�汾��
        System.out.println("==================��ѯ�汾��===========");
        List elementCodeList = new ArrayList();
        //elementCodeList.add("001");
        //elementCodeList.add("0010010010000000000011111");
        elementCodeList.add("BDGAGENCY");      //????
        Map rtnMap = service.queryElementVersion("BANK.CCB", 2010, elementCodeList);
        System.out.println("BDGAGENCY : " + (String) rtnMap.get("BDGAGENCY"));
    }

    public void testSyncElementCode(BeanFactory beanFactory) {
        // 3-���ػ����������°汾
        ElementService service = (ElementService) beanFactory.getBean("elementService");
        System.out.println("================�������°汾����===============");
        HashMap versionMap = new HashMap();
        // ���Ȳ�ѯ��ǰ�汾��
        int version = 1;
        versionMap.put("BDGAGENCY", version);
        List rtnlist = service.syncElementCode("BANK.CCB", 2010, versionMap);
        Map rtnMap = (Map) rtnlist.get(2);//datas
        System.out.println(" version: " + rtnMap.get("version"));
        List list = (List) rtnMap.get("datas");
        rtnMap = (Map) list.get(2);
        System.out.println((String) rtnMap.get("action") + rtnMap.get("code") + rtnMap.get("name") + rtnMap.get("guid"));


    }

    public void testWriteOfficeCard(BeanFactory beanFactory) {
        // 4-������Ϣ����
        System.out.println("==================������Ϣ����==============");
        List cardList = new ArrayList();
        Map m = new HashMap();
        m.put("ACCOUNT", "9090909090909");
        m.put("CARDNAME", "����");
        m.put("BDGAGENCY", "2000002");
        m.put("GATHERINGBANKACCTNAME", "GATHERINGBANKACCTNAME");
        m.put("GATHERINGBANKNAME", "GATHERINGBANKNAME");
        m.put("GATHERINGBANKACCTCODE", "89897979787");
        m.put("IDNUMBER", "34343");
        m.put("DIGEST", "989");
        m.put("BANK", "8015"); //?9	��������	BANK	NUMBER(16)	�������б���
        m.put("CREATEDATE", "20120101");
        m.put("STARTDATE", "20120102");
        m.put("ENDDATE", "20991231");
        m.put("ACTION", "0");  //13	���ݲ�������	ACTION	VARCHAR2(32)	0-���� 1-�޸� 2-ɾ��
        cardList.add(m);
        BankService bankService = (BankService) beanFactory.getBean("bankService");
        List rtnlist = bankService.writeOfficeCard("BANK.CCB", "8015", "2010", "405", cardList);
        Map rtnMap = (Map) rtnlist.get(0);
        System.out.println((String) rtnMap.get("message") + "--" + rtnMap.get("result"));

    }

    public void testWriteConsumeInfo(BeanFactory beanFactory) {
        // 5-������Ϣ����
        System.out.println("==================������Ϣ����==============");
        List cardList = new ArrayList();
        Map m = new HashMap();
        m.put("ID", "201105270000001");
        m.put("ACCOUNT", "6283660015627785");
        m.put("CARDNAME", "��Ȫ");
        m.put("BUSIDATE", "20110527");
        m.put("BUSIMONEY", "17.00");
        m.put("BUSINAME", "�ൺ���쳬����ɽ�ֵ�");
        m.put("Limitdate", "20110616");
        cardList.add(m);
        BankService bankService = (BankService) beanFactory.getBean("bankService");

        List rtnlist = bankService.writeConsumeInfo("BANK.CCB", "8015", "2010", "405", cardList);
        for (int i = 0; i < rtnlist.size(); i++) {
            Map m1 = (Map) rtnlist.get(i);
            String result = (String) m1.get("result");
            System.out.println(result);
        }
        Map rtnMap = rtnlist.size() > 0 ? (Map) rtnlist.get(0) : null;
        System.out.println((String) rtnMap.get("message") + "--" + rtnMap.get("result"));
    }

    public void testGetOfficeCardStatus(BeanFactory beanFactory) {
        // 6-����״̬����
        BankService bankService = (BankService) beanFactory.getBean("bankService");
        List rtnlist = bankService.getOfficeCardStatus("BANK.CCB", "CCB", "2010", "405", "guid0001111");
        System.out.println("===================����״̬����=======================");
        for (int i = 0; i < rtnlist.size(); i++) {
            Map m1 = (Map) rtnlist.get(i);
            String account = (String) m1.get("ACCOUNT");
            System.out.println("ACCOUNT : " + account);
        }
        Map rtnMap = rtnlist.size() > 0 ? (Map) rtnlist.get(0) : null;
        System.out.println((String) rtnMap.get("ACCOUNT") + "--" + rtnMap.get("CARDNAME") + "--" + rtnMap.get("STATUS"));
    }

    public void testGetQueryListBySql(BeanFactory beanFactory) {
        // 7-������ϸ��ѯ�ӿ�
        List cardList = new ArrayList();
        System.out.println("===================������ϸ��ѯ�ӿ�=======================");
        Map m = new HashMap();
        m.put("VOUCHERID", "10-016001-000001");
        CommonQueryService commonQueryService = (CommonQueryService) beanFactory.getBean("commonQueryService");
        List rtnlist = commonQueryService.getQueryListBySql("BANK.CCB", "queryConsumeInfo", m, "2010");
        Map rtnMap = (Map) rtnlist.get(0);
        System.out.println((String) rtnMap.get("VOUCHERID") + rtnMap.get("ACCOUNT") + rtnMap.get("CARDNAME") + rtnMap.get("Amt"));

    }
}