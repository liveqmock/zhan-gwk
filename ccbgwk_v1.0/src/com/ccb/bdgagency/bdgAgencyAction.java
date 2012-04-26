package com.ccb.bdgagency;

import gov.mof.fasp.service.ElementService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.form.control.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class bdgAgencyAction extends Action {
    private static final Log logger = LogFactory.getLog(bdgAgencyAction.class);

    public int initData() {
        try {
            getAllElementInfo();
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }
        this.res.setType(0);
        this.res.setResult(true);
        this.res.setMessage(PropertyManager.getProperty("200"));
        return 0;
    }
    private void getAllElementInfo() {
        ElementService service = FaspServiceAdapter.getElementService();
        List ElementCodeList = new ArrayList();
        Map m = new HashMap();
        m.put("CODE", "20101");
        m.put("NAME", "XXXXX");
        ElementCodeList.add(m);

        DatabaseConnection conn = ConnectionManager.getInstance().get();
        try {
            conn.begin();
            conn.executeUpdate(" delete from  ls_bdgagency where areacode = '370213'");
            List rtnlist = service.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2010);
            for (int i = 0; i < rtnlist.size(); i++) {
                Map m1 = (Map) rtnlist.get(i);
                if (i == rtnlist.size() - 1) {
                    String version = (String) m1.get("version");
                    System.out.println(" version=" + version);
                } else {
                    String code = (String) m1.get("code");
                    String name = (String) m1.get("name");
                    String guid = (String) m1.get("guid");
                    String levelno = (String) m1.get("levelno");
                    String supercode = (String) m1.get("supercode");
                    String isleaf = (String) m1.get("isleaf");
                    System.out.println("code=" + code + name);
                    String sql = "insert into ls_bdgagency t" +
                            "              (t.areacode," +
                            "               t.code," +
                            "               t.name," +
                            "               t.guid," +
                            "               t.levelno," +
                            "               t.supercode," +
                            "               t.isleaf," +
                            "               t.version," +
                            "               t.remark," +
                            "               t.operid," +
                            "               t.operdate) values (" +
                            " '370213', " +
                            " '" + code + "', " +
                            " '" + name + "', " +
                            " '" + guid + "', " +
                            " '" + levelno + "', " +
                            " '" + supercode + "', " +
                            " '" + isleaf + "', " +
                            " 0, " +
                            " 'remark', " +
                            " 'auto', " +
                            " sysdate " +
                            "              )";
                    conn.executeUpdate(sql);
                }
            }
            conn.commit();
            int i = 0;
        } catch (Exception e) {
            conn.rollback();
            logger.error(e);
            throw new RuntimeException("数据库处理错误.", e);
        } finally {
            ConnectionManager.getInstance().release();
        }
    }

}
