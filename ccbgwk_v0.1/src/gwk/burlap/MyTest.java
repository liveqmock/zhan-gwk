package gwk.burlap;

import com.caucho.burlap.client.BurlapProxyFactory;
import com.caucho.hessian.client.HessianProxyFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;


/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-5-5
 * Time: 22:24:11
 * To change this template use File | Settings | File Templates.
 */
public class MyTest {

    public  static void main(String argv[]){

//        String url = "http://www.caucho.com/hessian/test/basic";

//   HessianProxyFactory factory = new HessianProxyFactory();
//   Basic basic = (Basic) factory.create(Basic.class, url);
//
//   System.out.println("hello(): " + basic.hello());


        String url = "http://localhost:7001/burlap";
//        String url = "http://48.135.201.1:7001/burlap";
//        String url = "http://48.135.204.1:7004/exserver/remoting/elementservice";
//        String url = "http://48.135.204.1:7004/exserver/remoting/service/bankservice";

        BurlapProxyFactory factory = new BurlapProxyFactory();
        IElement b = null;                                              
        try {                                                
            b = (IElement) factory.create(IElement.class, url);
        } catch (MalformedURLException e) {
            System.out.println("occur exception: " + e);                 
        }
        try {
            System.out.println(b.queryAllElementCode("BANK.CCB","000-000-000-000",2010));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        System.out.println(b.queryAllElementCode("BANK.CCB","001-001-001-001",2010));
//        System.out.println(h.getOfficeCardStatus("BANK.CCB","CCB","2010","guid0001111"));


    }
}
