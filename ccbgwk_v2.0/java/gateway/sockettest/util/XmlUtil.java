package gateway.sockettest.util;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:07:23
 * To change this template use File | Settings | File Templates.
 */
public class XmlUtil {
    public static String XML_HEADER="<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->";
    public static String getStartTag(String tagName) {
        return "<" + tagName.toString() + ">";
    }

    public static String getEndTag(String tagName) {
        return "<!--" + tagName.toString() + "-->";
    }

    public static String buildXmlByStr(String tagName,String str){
        return getStartTag(tagName)+str+getEndTag(tagName);
    }

    public static String getRedColor(String str) {
        return "<span style=\"color:#FF0000\">" + str + "</span>";   
    }
}