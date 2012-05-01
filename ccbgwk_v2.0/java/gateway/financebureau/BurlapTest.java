package gateway.financebureau;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 12-5-1
 * Time: ÏÂÎç4:39
 * To change this template use File | Settings | File Templates.
 */
public class BurlapTest {
    public static void main(String... args){
        BurlapTest test = new BurlapTest();
        test.qryElementInfo();
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
}
