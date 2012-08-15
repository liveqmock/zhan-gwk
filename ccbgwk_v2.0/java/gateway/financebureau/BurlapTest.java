package gateway.financebureau;

import gov.mof.fasp.service.*;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.db.RecordSet;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 12-5-1
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
public class BurlapTest {
    public static void main(String... args){
        BurlapTest test = new BurlapTest();
//        test.qryElementInfo();
        String status="10";
        String[] strArr = new String[]{status};
        test.testMap(strArr);

    }
    private void qryElementInfo(){
        ElementService service = null;
        try {
            service = GwkBurlapServiceFactory.getInstance().getElementService("002");
            List rtnlist = service.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2012);
            System.out.println(rtnlist.size());
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    /*
    自财政局获取还款信息
     */

    public void testAdapter_queryservice() {
        BankService service = null;
        List cardList = new ArrayList();
        Map m = new HashMap();
//        m.put("bankcode", "8015");
        m.put("VOUCHERID", "10-016001-000001");
        try {
            //service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);
            service = GwkBurlapServiceFactory.getInstance().getBankService("002");
//            List rtnlist = service.queryConsumeInfo("BANK.CCB","300001","2012","","10030001-0002");
//            for (int i = 0; i < rtnlist.size(); i++) {
//                rtnlist.get(1);
//
//            }
            int i = 0;
//            service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public void testMap(String[] status){
//        this.sendInfoCount = 0;
        DatabaseConnection dc = ConnectionManager.getInstance().get();
        HashMap mapConsume = new HashMap();
        String strCode = PropertyManager.getProperty("finance.codeset");
        String[] strArr = strCode.split(",");
        String areaCode="";
        String areaName="";
        for (int j=0;j<strArr.length;j++){
            StringBuffer wheresqlbfr = new StringBuffer(" where csi.status in ('");
            int statusCount = status.length;
            for (int i = 0; i < statusCount; i++) {
                wheresqlbfr.append(status[i]);
                if (i + 1 < statusCount) {
                    wheresqlbfr.append("','");
                }
            }
            wheresqlbfr.append("')");

            areaCode=strArr[j];
            areaName=PropertyManager.getProperty("finance.name."+areaCode);
            wheresqlbfr.append(" and cbi.gatheringbankacctname='"+areaName+"' ");
            wheresqlbfr.append(" and cbi.status = '1' order by lsh ");
            String wheresql = new String(wheresqlbfr);
            String selectsql = "select lsh,csi.account as account,csi.cardname as cardname,busidate,busimoney,businame,limitdate,tx_cd from ls_consumeinfo csi "
                    + " join ls_cardbaseinfo cbi on csi.account = cbi.account "
                    + wheresql;

            System.out.println(selectsql);

            RecordSet rs = null;
            List cardList = new ArrayList();
            rs = dc.executeQuery(selectsql);
            while (rs.next()) {
//                this.sendInfoCount++;
                Map m = new HashMap();
                String lsh = rs.getString("lsh");
                String account = rs.getString("account").trim();
                String cardname = rs.getString("cardname").trim();
                String busidate = rs.getString("busidate");
                busidate = busidate.substring(0, 4) + busidate.substring(5, 7) + busidate.substring(8, 10);
                Double busimoney = rs.getDouble("busimoney");
                String businame = rs.getString("businame").trim();
                String limitdate = rs.getString("limitdate");
                limitdate = limitdate.substring(0, 4) + limitdate.substring(5, 7) + limitdate.substring(8, 10);
                String tx_cd = rs.getString("tx_cd");
                if (busimoney <= 0) {
                    limitdate = "";
                }
                if ("43".equals(tx_cd)) {
                    busimoney = -busimoney;
                }
                m.put("ID", lsh);
                m.put("ACCOUNT", account);
                m.put("CARDNAME", cardname);
                m.put("BUSIDATE", busidate);
                m.put("BUSIMONEY", busimoney);
                m.put("BUSINAME", businame);
                m.put("Limitdate", limitdate);
                cardList.add(m);
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if(cardList.size()>0){
                mapConsume.put(areaCode,cardList);
            }
            List listConsume = (List)mapConsume.get("002");
            if (listConsume!=null){
                System.out.println(String.valueOf(listConsume.size()));
            }

        }
    }
}
