package gateway.mbs.server.domain;

import gateway.mbs.util.XmlUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:03:22
 * To change this template use File | Settings | File Templates.
 */
public class RequestData {   
    /**
     * 请求类型
     */
    private String cAction;
    /**
     * 请求的xml数据
     */
    private String requestXmlString;

    public RequestData(String cAction, String requestXmlString) {
        this.cAction = cAction;
        this.requestXmlString = requestXmlString;
    }

    public String getCAction() {
        return cAction;
    }

    /**
     * 返回xml型式的请求数据
     *
     * @return
     */
    public String getRequestXmlString() {
        return requestXmlString;
    }

    /**
     * 返回去除请求xml标签的数据
     */
    public String getRequestString() {
        return this.requestXmlString.replaceAll(XmlUtil.getStartTag(cAction) + "|" + XmlUtil.getEndTag(cAction), "");
    }

    public String getStartTag() {
        return XmlUtil.getStartTag(cAction);
    }

    public String getEndTag() {
        return XmlUtil.getEndTag(cAction);
    }
}
