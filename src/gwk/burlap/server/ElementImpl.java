package gwk.burlap.server;

import com.caucho.burlap.server.BurlapServlet;
import gwk.burlap.IElement;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-5-6
 * Time: 16:00:19
 * To change this template use File | Settings | File Templates.
 */
public class ElementImpl extends BurlapServlet implements IElement {
    public List queryAllElementCode(String applicationid, String ElementCode, int year) throws UnsupportedEncodingException {
        List elements = new ArrayList();
        elements.add("中文");
        elements.add("һ��");
        elements.add("ʩ��");   
        elements.add("elementsһ��");      
        try {
            String gbk = new String("中文".getBytes("ISO-8859-1"),"GBK");
            elements.add(gbk);   
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
//        if (1==1) {
//            throw new BurlapRuntimeException();
//        }
        return  elements;
    }

    public Map queryElementVersion(String applicationid, int year, List elements) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List syncElementCode(String applicationid, int year, Map element) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
