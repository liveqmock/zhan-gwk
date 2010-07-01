package gateway.mbs.protocol.requestaction;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 8:07:20
 * To change this template use File | Settings | File Templates.
 */
public class ActionRegister {
    private static List actions = new LinkedList();
    static {
//        actions.add(new SendAllUserListAction());
    }   

    public static List getActions() {
        return actions;
    }
}