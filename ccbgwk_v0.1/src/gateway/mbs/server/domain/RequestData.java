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
     * ��������
     */
    private String cAction;
    /**
     * �����xml����
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
     * ����xml��ʽ����������
     *
     * @return
     */
    public String getRequestXmlString() {
        return requestXmlString;
    }

    /**
     * ����ȥ������xml��ǩ������
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
