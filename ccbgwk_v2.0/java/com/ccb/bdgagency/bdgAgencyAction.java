package com.ccb.bdgagency;

import gateway.financebureau.ElementService;
import gateway.financebureau.GwkBurlapServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.form.control.Action;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        String areaCode = "";
        ElementService service = null;
        //业务系统标识
        String applicationid="";
        String strDate = "";
        String year = "";

        DatabaseConnection conn = ConnectionManager.getInstance().get();
        try {
            //获取所属区域代码 2012-05-13 linyong
            for (int i = 0 ; i < req.getRecorderCount() ; i++){
                areaCode=req.getFieldValue(i,"areacode");
                System.out.println(areaCode);
            }
            //根据不同的代码获取相应的业务系统标识 2012-10-29
            applicationid = PropertyManager.getProperty("application.id."+areaCode);
            if ("".equals(applicationid)){
                applicationid="BANK.CCB";
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            strDate = df.format(new Date());
            year = strDate.substring(0, 4);
            //根据不同的代码，获取相应的接口 2012-05-13 linyong
            service = GwkBurlapServiceFactory.getInstance().getElementService(areaCode);
            conn.begin();
            conn.executeUpdate(" delete from  ls_bdgagency where areacode = '" + areaCode + "'");

            List rtnlist = service.queryAllElementCode(applicationid, "BDGAGENCY", Integer.parseInt(year));
//            List rtnlist = service.queryAllElementCode("BANK.CCB", "BDGAGENCY", 2012);

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
                    if(supercode==null){
                        supercode="";
                    }
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
                            " '" + areaCode + "', " +
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
