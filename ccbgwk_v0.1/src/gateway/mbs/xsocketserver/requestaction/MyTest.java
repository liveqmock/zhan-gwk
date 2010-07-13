package gateway.mbs.xsocketserver.requestaction;

import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: gwk
 * Date: 2010-7-9
 * Time: 10:22:51
 * To change this template use File | Settings | File Templates.
 */
public class MyTest {
   public static void main(String argv[]){
       String name = "name=–’√˚A";
       System.out.println(name.length());

       byte[] nameBytes = new byte[0];
       try {
           nameBytes = name.getBytes("ISO-8859-1");
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       }
       System.out.println(nameBytes.length);

       int count = 10;
       System.out.println(count%10);
   }
}
