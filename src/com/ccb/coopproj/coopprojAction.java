package com.ccb.coopproj;

/**
 * <p>Title: ��̨ҵ�����</p>
 *
 * <p>Description: ��̨ҵ�����</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: ��˾</p>
 *
 * @author
 * @version 1.0
 */

import com.ccb.dao.LNCOOPPROJ;
import com.ccb.dao.LNTASKINFO;
import com.ccb.mortgage.MortUtil;
import com.ccb.util.CcbLoanConst;
import com.ccb.util.SeqUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.BusinessDate;

public class coopprojAction extends Action {
    // ��Ѻ��Ϣ����
    LNCOOPPROJ coopproj = null;
    // ϵͳ��־��
    LNTASKINFO task = null;

    private static final Log logger = LogFactory.getLog(coopprojAction.class);

    /**
     * <p/>
     * ��Ѻ��Ϣ���ӽӿ�
     * <p/>
     * �ɹ���ʧ�ܾ�������Ϣ
     * <p/>
     * ����id���û�id������ʱ����ں�̨��ֵ
     *
     * @return
     */

    public int add() {
        coopproj = new LNCOOPPROJ();
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                // ��ʼ������bean
                coopproj.initAll(i, req);

                LNCOOPPROJ coopprojTmp = coopproj.findFirst(" where  proj_no = '" + coopproj.getProj_no() + "'");
                if (coopprojTmp != null) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage("���Ӻ�����Ŀ���ִ���, ����Ŀ�Ѵ��ڡ�");
                    return -1;
                }

                // �ڲ����
                String coopSeq = SeqUtil.getCoop();
                coopproj.setProj_nbxh(coopSeq);
                // ����id
                coopproj.setDeptid(this.getDept().getDeptid());
                // ����ʱ��
                coopproj.setOperdate(BusinessDate.getToday());
                // �û�id
                coopproj.setOperid(this.getOperator().getOperid());
                // �汾��
                coopproj.setRecversion(0);
                if (coopproj.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // ��ˮ��־��
                task = MortUtil.getTaskObj(coopSeq, req.getFieldValue(i, "busiNode"), CcbLoanConst.OPER_ADD);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }

            } catch (Exception ex1) {
                ex1.printStackTrace();
                logger.error("���Ӻ�����Ŀ���ִ���");
                logger.error(ex1.getMessage());
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

    /**
     * <p/>
     * ��Ѻ��Ϣ�༭�ӿ�
     * <p/>
     * ���˸���ҳ���ϵ�ֵ֮�⣬�û�id������ʱ��Ҳһ����£�
     * <p/>
     * ����ǰ���а汾�ż�飬���Ʋ�������
     *
     * @return
     */
    public int edit() {

        coopproj = new LNCOOPPROJ();
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                // ��ʼ������bean
                coopproj.initAll(i, req);

                LNCOOPPROJ coopprojTmp = coopproj.findFirst(" where  proj_no = '" + coopproj.getProj_no() + "'");
                if (coopprojTmp != null) {
                    if (!coopprojTmp.getProj_nbxh().equals(coopproj.getProj_nbxh())) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage("�޸ĺ�����Ŀ���ִ���, ����Ŀ�Ѵ��ڡ�");
                        return -1;
                    }
                }

                // ����ʱ��
                coopproj.setOperdate(BusinessDate.getToday());
                // �û�id
                coopproj.setOperid(this.getOperator().getOperid());
                // ����ǰ�汾��
                int iBeforeVersion = 0;
                if (req.getFieldValue(i, "recVersion") != null && !req.getFieldValue(i, "recVersion").equals("")) {
                    iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                }
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from ln_coopproj where proj_nbxh='"
                        + req.getFieldValue("proj_nbxh") + "'");
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
                        coopproj.setRecversion(iBeforeVersion);
                    }
                }

                if (coopproj.updateByWhere(" where proj_nbxh='" + req.getFieldValue(i, "proj_nbxh") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // ��ˮ��־��
                task = MortUtil.getTaskObj(req.getFieldValue(i, "proj_nbxh"), req.getFieldValue(i, "busiNode"),
                        CcbLoanConst.OPER_EDIT);
                task.setOperid(this.getOperator().getOperid());
                task.setBankid(this.getOperator().getDeptid());
                if (task.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }

            } catch (Exception ex1) {
                ex1.printStackTrace();
                logger.error("�༭������Ŀ���ִ���");
                logger.error(ex1.getMessage());
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

    /**
     * <p/>
     * ɾ���ӿ�
     * <p/>
     */

    public int delete() {

        try {
            // �ڲ����
            String proj_nbxh = req.getFieldValue("proj_nbxh");

            if (proj_nbxh == null || proj_nbxh.equalsIgnoreCase("null")) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }

            dc.executeUpdate("delete from ln_coopproj where proj_nbxh='" + proj_nbxh + "'");
            // ��ˮ��־��
            task = MortUtil.getTaskObj(proj_nbxh, req.getFieldValue("busiNode"), CcbLoanConst.OPER_DEL);
            task.setOperid(this.getOperator().getOperid());
            task.setBankid(this.getOperator().getDeptid());
            if (task.insert() < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ɾ��������Ŀ���ִ���");
            logger.error(e.getMessage());
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        this.res.setType(0);
        return 0;
    }

}