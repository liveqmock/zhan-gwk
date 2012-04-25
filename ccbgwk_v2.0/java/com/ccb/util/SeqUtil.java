package com.ccb.util;

import pub.platform.db.SequenceManager;
import pub.platform.utils.StringUtils;

import java.util.Date;

public class SeqUtil {

    // �����ڲ����
    // 
    public static String getNbxh() {
        String temp = "" + SequenceManager.nextID("NBXH");
        String rtn = StringUtils.toDateFormat(new Date(), "yyyyMMdd") + StringUtils.addPrefix(temp, "0", 7);
        return rtn;
    }

    // ������ˮ���
    public static String getTaskSeq() {
        String temp = "" + SequenceManager.nextID("TASK");
        String rtn = StringUtils.toDateFormat(new Date(), "yyyyMMdd") + StringUtils.addPrefix(temp, "0", 7);
        return rtn;
    }


}
