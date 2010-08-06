package com.ccb.consume;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gwk
 * Date: 2010-8-4
 * Time: 13:49:47
 * To change this template use File | Settings | File Templates.
 */
public class SendConsumeInfoTest {
    public static List sendConsumeInfoRtn(){
        List list = new ArrayList();
        try {
            Thread.currentThread().sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        list.add("hello");
        System.out.println("===================æ≠π˝¡À6√Î=================================");
        return list;
    }
}
