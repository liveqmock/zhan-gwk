package com.ccb.personinfo;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 18:45:38
 * To change this template use File | Settings | File Templates.
 */

import com.ccb.dao.LSPERSONALINFO;
import com.ccb.util.SeqUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.BusinessDate;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-6-11
 * Time: 8:23:38
 * To change this template use File | Settings | File Templates.
 */
public class PerInfoEidtAction  extends Action {
    // ��־����
    private static final Log logger = LogFactory.getLog(PerInfoEidtAction.class);
    // ������Ϣ����
    LSPERSONALINFO perInfo = null;
    // ��ˮ��¼��
//    LNTASKINFO task = null;
    /*
    *����¼�¼
    * */
    public int add() {
        perInfo = new LSPERSONALINFO();
        for (int i= 0;i < this.req.getRecorderCount();i++){
            try{
                // ������֤�������ظ�
                RecordSet rec = dc.executeQuery("select 1 from perinfo t where t.perid = '"
                        + req.getFieldValue(i, "perid").trim() + "'");
                while (rec.next()) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("309") + "\r\n���֤���룺"
                            + req.getFieldValue(i, "perid").trim());
                    return -1;
                }
                if (rec != null) {
                    rec.close();
                }
                 // ��ʼ������bean
                perInfo.initAll(i, req);
                //�����ڲ����к�
                String nbxlh = SeqUtil.getNbxh();
                perInfo.setRecinsequence(nbxlh);
                // �汾��
                perInfo.setRecversion(0);
                //������code
//                perInfo.setCreatecode(1);
                //����ʱ��
                perInfo.setCreatedate(BusinessDate.getTodaytime());
                 if (perInfo.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                //��ˮ��־�����
//                if (task.insert() < 0){
//                    d
//                }
            }catch (Exception ex1) {
                logger.error(ex1.getMessage());
                ex1.printStackTrace();
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        }
       this.res.setType(0);
        this.res.setResult(true);
        this.res.setMessage(PropertyManager.getProperty("200"));
        return 0;
    }

    /*
    * �޸ļ�¼
    * */
    public int edit(){
        perInfo = new LSPERSONALINFO();
        for (int i= 0;i < this.req.getRecorderCount();i++){
            try{
                // ������֤�������ظ�
//                RecordSet rec = dc.executeQuery("select 1 from perinfo t where t.perid = '"
//                        + req.getFieldValue(i, "perid").trim() + "'");
//                while (rec.next()) {
//                    this.res.setType(0);
//                    this.res.setResult(false);
//                    this.res.setMessage(PropertyManager.getProperty("309") + "\r\n���֤���룺"
//                            + req.getFieldValue(i, "perid").trim());
//                    return -1;
//                }
//                if (rec != null) {
//                    rec.close();
//                }
                //�޸�
                // ��ʼ������bean
                perInfo.initAll(i, req);
                //�汾��
                int iBeforeVersion = 0;    //����ǰ�İ汾��
                if (req.getFieldValue(i, "recversion") != null && !req.getFieldValue(i, "recversion").equals("")) {
                    iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recversion"));
                }
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from perinfo where recinsequence='"
                        + req.getFieldValue(i, "recinsequence") + "'");
                 while (rs.next()) {
                    iAfterVersion = rs.getInt("recVersion");
                    if (iBeforeVersion != iAfterVersion) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("301"));
                        return -1;
                    } else {
                        // �汾�ż�1
                        iBeforeVersion = iBeforeVersion + 1;
                        perInfo.setRecversion(iBeforeVersion);
                    }
                }
                if (rs != null) {
                    rs.close();
                }
                //createcode
                perInfo.setCreatecode(req.getFieldIntValue(i,"createcode"));
                //createdate
                perInfo.setCreatedate(BusinessDate.getTodaytime());
                if (perInfo.updateByWhere(" where recinsequence='" + req.getFieldValue(i, "recinsequence") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            }catch (Exception ex1) {
                logger.error(ex1.getMessage());
                ex1.printStackTrace();
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        }

        this.res.setType(0);
        this.res.setResult(true);
        this.res.setMessage(PropertyManager.getProperty("200"));
        return 0;
    }

    /*
    * ɾ����¼
    * */
    public int delete(){
        perInfo = new LSPERSONALINFO();
        try{
            for (int i = 0 ; i < req.getRecorderCount() ; i++){
                // ��ʼ������bean    (" where recinsequence='" + req.getFieldValue(i, "recinsequence") + "'") < 0)
                perInfo.initAll(i, req);
                if (perInfo.deleteByWhere(" where recinsequence='" + req.getFieldValue(i,"recinsequence") + "'") < 0){
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                    //��ˮ��־�ĸ��¡�����
                }
            }
        } catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
        }
        this.res.setType(0);
//        this.res.setResult(true);
//        this.res.setMessage(PropertyManager.getProperty("200"));
        return 0;
    }
}
