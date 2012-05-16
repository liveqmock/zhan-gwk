package com.ccb.bdgagency;

import gateway.financebureau.ElementService;
import gateway.financebureau.GwkBurlapServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.form.control.Action;

import java.util.List;
import java.util.Map;

public class bdgAgencyAction extends Action {
    private static final Log logger = LogFactory.getLog(bdgAgencyAction.class);

    public int initData() {
        try {
            initAllElementInfo();
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }
        this.res.setType(0);
        this.res.setResult(true);
        String message = PropertyManager.getProperty("200");
        this.res.setMessage(message);
//        this.res.setMessage("操作成功。");
        return 0;
    }

    private void initAllElementInfo() {
        //所属区域代码
        String areacode = "";
        ElementService service = null;
        DatabaseConnection conn = ConnectionManager.getInstance().get();
        try {
            //获取所属区域代码 2012-05-13 linyong
            for (int i = 0 ; i < req.getRecorderCount() ; i++){
                areacode=req.getFieldValue(i,"areacode");
                System.out.println(areacode);
            }
            //根据不同的代码，获取相应的接口 2012-05-13 linyong
            service = GwkBurlapServiceFactory.getInstance().getElementService(areacode);
            conn.begin();
            conn.executeUpdate(" delete from  ls_bdgagency where areacode = '" + areacode + "'");
            List rtnlist = service.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2012);
            for (int i = 0; i < rtnlist.size(); i++) {
                Map m1 = (Map) rtnlist.get(i);
                if (i == rtnlist.size() - 1) {
                    String version = String.valueOf( m1.get("version"));
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
                            " '" + areacode + "', " +
                            " '" + code + "', " +
                            " '" + name + "', " +
                            " '" + guid + "', " +
                            " '" + levelno + "', " +
                            " '" + supercode + "', " +
                            " '" + isleaf + "', " +
                            " 0, " +
                            " '初始化', " +
                            " 'auto', " +
                            " sysdate " +
                            "              )";
                    conn.executeUpdate(sql);
                }
            }
            conn.commit();
            //int i = 0;
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException("数据库处理错误.", e);
        } finally {
            ConnectionManager.getInstance().release();
        }
    }
}
