package gateway.mbs.xsocketserver;

import gateway.mbs.server.domain.RequestData;
import gateway.mbs.server.protocol.Protocol;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 8:09:08
 * To change this template use File | Settings | File Templates.
 */
public interface RequestAction {
    /**
     * �������󣬷��ط�Ӧ����
     */
    public void dealReqeust(RequestData requestData,Protocol protocol);

    /**
     * �Ƿ��ܴ�������
     */
    //public boolean canDeal(RequestData requestData);
}