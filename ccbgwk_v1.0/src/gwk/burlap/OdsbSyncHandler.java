package gwk.burlap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-6-10
 * Time: 20:07:12
 * To change this template use File | Settings | File Templates.
 */
public class OdsbSyncHandler {
    private static final Log logger = LogFactory.getLog(OdsbSyncHandler.class);

    public void getAllConsumeInfoFromOdsb(){
        DatabaseConnection dc = ConnectionManager.getInstance().get();

        try {
            dc.begin();
            String sql = " truncate table odsb_crd_crt_trad ";
            int rtn = dc.executeUpdate(sql);
            sql = "insert into odsb_crd_crt_trad " +
                    "select a.* " +
                    "  from odsbdata.bf_evt_crd_crt_trad@odsb_remote a, odsb_crd_crt b " +
                    " where a.crd_no = b.crd_no ";
             rtn = dc.executeUpdate(sql);
            if (rtn < 0) {
                logger.error("....");
                throw new RuntimeException("...");
            }

        } catch (Exception e) {
            dc.rollback();
            logger.error(e);
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.getInstance().release();
        }
    }
    public void syncConsumeInfo(){
        DatabaseConnection dc = ConnectionManager.getInstance().get();

        try {
            dc.begin();
            String sql = " truncate table odsb_crd_crt_trad ";
            int rtn = dc.executeUpdate(sql);
            sql = "insert into odsb_crd_crt_trad " +
                    "select a.* " +
                    "  from odsbdata.bf_evt_crd_crt_trad@odsb_remote a, odsb_crd_crt b " +
                    " where a.crd_no = b.crd_no ";
             rtn = dc.executeUpdate(sql);
            if (rtn < 0) {
                logger.error("....");
                throw new RuntimeException("...");
            }

        } catch (Exception e) {
            dc.rollback();
            logger.error(e);
            throw new RuntimeException(e);
        } finally {
            
            ConnectionManager.getInstance().release();
        }
    }
}
