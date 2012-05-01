package gateway.sockettest.server.protocol;

import gateway.sockettest.server.domain.RequestData;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 8:09:08
 * To change this template use File | Settings | File Templates.
 */
public interface RequestAction {
    /**
     * 处理请求，返回反应数据
     */
    public void dealReqeust(RequestData requestData,Protocol protocol);

    /**
     * 是否能处理请求
     */
    public boolean canDeal(RequestData requestData);
}   