package com.ccb.mortgage;

/**
 * <p>Title: ��̨ҵ�����</p>
 *
 * <p>Description: ��̨ҵ�����</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: ��˾</p>
 *
 * @author leonwoo
 * @version 1.0
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.BusinessDate;

import com.ccb.dao.LNLOANAPPLY;
import com.ccb.dao.LNMORTINFO;
import com.ccb.dao.LNTASKINFO;
import com.ccb.dao.SYSSEQDISCARD;
import com.ccb.util.CcbLoanConst;
import com.ccb.util.SeqUtil;

public class mortgageAction extends Action {
    // ��־����
    private static final Log logger = LogFactory.getLog(mortgageAction.class);
    // ��Ѻ��Ϣ����
    LNMORTINFO mortInfo = null;
    // �ϺŹ������
    SYSSEQDISCARD seqInfoDiscard = null;
    // ������Ϣ����
    LNLOANAPPLY loan = null;
    // ��ˮ��¼��
    LNTASKINFO task = null;

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
        mortInfo = new LNMORTINFO();
        // ȡ����Ѻ���
        String mortID = "";
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {

                // ��ʼ������bean
                mortInfo.initAll(i, req);
                // ����id
                mortInfo.setDeptid(this.getDept().getDeptid());
                // ����ʱ��
                mortInfo.setOperdate(BusinessDate.getToday());
                // �û�id
                mortInfo.setOperid(this.getOperator().getOperid());
                // �Ǽ�״̬
                // mortInfo.setMortstatus(CcbLoanConst.NODE_DOING);
                mortInfo.setMortstatus(CcbLoanConst.MORT_FLOW_REGISTED);
                // δ���׵�Ѻԭ�� Ĭ��ֵ������
                mortInfo.setNomortreasoncd("99");
                // ��Ѻ�Ǽ�״̬ Ĭ��Ϊ��δ�Ǽǣ�1
                mortInfo.setMortregstatus("1");
                // �汾��
                mortInfo.setRecversion(0);

                // ��Ѻ������
                // String mortExpireDate = getMortExpireDate(req);
                String releasecondcd = req.getFieldValue(i, "RELEASECONDCD");
                String mortExpireDate = MortUtil.getMortExpireDate(releasecondcd, req
                        .getFieldValue(i, "MORTDATE"), dc, req.getFieldValue(i, "loanID"), req.getFieldValue(i,
                        "MORTECENTERCD"));

                // TODO:��Ѻ������ȡ��������£������ֵ
                // ��Ѻ����������ʧ��
                // if ("".equals(mortExpireDate)) {
                // this.res.setType(0);
                // this.res.setResult(false);
                // this.res.setMessage(PropertyManager.getProperty("305"));
                // return -1;
                // }

                mortInfo.setMortexpiredate(mortExpireDate);

                //20100403 zhan
                //���ΪǩԼ�� ͬʱ���µ�Ѻ����������
                if (releasecondcd.equals("03") || releasecondcd.equals("06")) {
                    mortInfo.setMortoverrtndate(mortExpireDate);
                }

                // �Ƿ�ӷϺű�ȡֵ
                boolean discardFlg = false;

                /*
                20100423 zhan  ȡ���ϺŹ���

                RecordSet rs = dc.executeQuery("select discardno from sys_seq_discard where bhlx='"
                        + CcbLoanConst.MORTTYPE + "' and useflg='0' order by discardno asc ");
                if (rs.next()) {
                    discardFlg = true;
                    mortID = rs.getString("discardno");
                } else {
                    mortID = SeqUtil.getMortID();
                }
                if (rs != null) {
                    rs.close();
                }
                */
                //20100423 zhan ȡ���ϺŹ��� ֱ��ȡ��
                mortID = SeqUtil.getMortID();

                mortInfo.setMortid(mortID);

                if (mortInfo.insert() < 0) {
                    // ------�����ɵĵ�Ѻ��Ž��ϺŹ����--------
                    seqInfoDiscard = new SYSSEQDISCARD();
                    // ʹ�ñ�־��δʹ��
                    seqInfoDiscard.setUseflg(0);
                    // �����������
                    seqInfoDiscard.setNseqno(req.getFieldValue(i, "loanid"));
                    // ����ʱ��
                    seqInfoDiscard.setIndate(BusinessDate.getNowDay());
                    // ���������Ա
                    seqInfoDiscard.setInoperid(this.getOperator().getOperid());
                    // ��Ѻ���
                    seqInfoDiscard.setDiscardno(mortID);
                    // �Ϻ�������
                    seqInfoDiscard.setBhlx(CcbLoanConst.MORTTYPE);
                    // ��������
                    seqInfoDiscard.setDotype("add");

                    if (seqInfoDiscard.insert() < 0) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("300"));
                        return -1;
                    }

                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }

                // �жϸ�����Ƿ�ӷ�����ȡ�ã����������·ϺŹ����
                if (discardFlg) {
                    seqInfoDiscard = new SYSSEQDISCARD();
                    // ʹ�ñ�־����ʹ��
                    seqInfoDiscard.setUseflg(1);
                    seqInfoDiscard.setNseqno(req.getFieldValue(i, "loanid"));
                    seqInfoDiscard.setUsedate(BusinessDate.getNowDay());
                    seqInfoDiscard.setUseoperid(this.getOperator().getOperid());
                    // �ô������������Ը�Ψһ
                    if (seqInfoDiscard.updateByWhere(" where  bhlx='" + CcbLoanConst.MORTTYPE + "' and discardno='"
                            + mortID + "'") < 0) {
                        this.res.setType(0);
                        this.res.setResult(false);
                        this.res.setMessage(PropertyManager.getProperty("300"));
                        return -1;
                    }
                }
                // ���´�����Ϣ��
                loan = new LNLOANAPPLY();
                loan.init(i, req);
                if (loan.updateByWhere(" where loanid='" + req.getFieldValue(i, "loanid") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // ��ˮ��־��
                task = MortUtil.getTaskObj(req.getFieldValue(i, "loanid"), CcbLoanConst.BUSINODE_010,
                        CcbLoanConst.OPER_ADD);
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
                logger.error(ex1.getMessage());
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }
        }
        // this.res.setType(0);
        // this.res.setResult(true);
        // this.res.setMessage(PropertyManager.getProperty("200"));
        // return 0;

        this.res.setFieldName("mortID");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(mortID);
        this.res.setType(4);
        this.res.setResult(true);
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

        mortInfo = new LNMORTINFO();
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                // ��ʼ������bean
                mortInfo.initAll(i, req);
                /*
                 * // ---����δ�����Ѻԭ�����״̬�޸�--- // �����ѽ��塢�ѳ��� ��Ѻ�Ǽ�״̬��Ϊ���������ȡ֤�Ǽǡ��ѽ���ȡ֤
                 * if (req.getFieldValue(i, "NOMORTREASONCD") != null) { if
                 * (req.getFieldValue(i, "NOMORTREASONCD").equals("08") ||
                 * req.getFieldValue(i, "NOMORTREASONCD").equals("17")) { //
                 * �ѽ���ȡ֤ mortInfo.setMortstatus("50"); } }
                 */
                // ---����δ�����Ѻԭ�����״̬�޸�---
                // �����ѽ��塢�ѳ��� ��Ѻ�Ǽ�״̬��Ϊ���������ȡ֤�Ǽǡ��ѽ���ȡ֤
                if (req.getFieldValue(i, "NOMORTREASONCD") != null) {
                    if (req.getFieldValue(i, "NOMORTREASONCD").equals("08")) {
                        // δ��Ѻ�����ѽ���
                        mortInfo.setMortstatus(CcbLoanConst.MORT_FLOW_NOMORT_CLEARED);
                    } else if (req.getFieldValue(i, "NOMORTREASONCD").equals("17")) {
                        // �ѳ���
                        mortInfo.setMortstatus(CcbLoanConst.MORT_FLOW_NOMORT_GETBOOK);
                    }
                }

                //20100403  zhan   ����
                //�ڴ������п��������ɱ���Ѻʱ ͬʱ����δ�����Ѻԭ�� �޸�Ϊ �����п�������:04
                String sendflag = req.getFieldValue(i, "SENDFLAG");
                if (sendflag != null) {
                    //����Ϊ���ַ�������Ϊ��0������Ϊ��1��
                    if (sendflag.equals("0")) {
                        mortInfo.setNomortreasoncd("04");
                    } else if (sendflag.equals("1")) {
                        mortInfo.setNomortreasoncd("");
                    }
                }

                // ����ʱ��
                mortInfo.setOperdate(BusinessDate.getToday());
                // �û�id
                mortInfo.setOperid(this.getOperator().getOperid());
                // ����ǰ�汾��
                int iBeforeVersion = 0;
                if (req.getFieldValue(i, "recVersion") != null && !req.getFieldValue(i, "recVersion").equals("")) {
                    iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                }
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from ln_mortinfo where mortid='"
                        + req.getFieldValue(i, "mortid") + "'");
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
                        mortInfo.setRecversion(iBeforeVersion);
                    }
                }
                if (rs != null) {
                    rs.close();
                }

                if (mortInfo.updateByWhere(" where mortid='" + req.getFieldValue(i, "mortid") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // ��ˮ��־��
                task = MortUtil.getTaskObj(req.getFieldValue(i, "loanid"), req.getFieldValue(i, "busiNode"),
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
     * ɾ��һ����¼�󣬵�Ѻ���Ҫ������������������Ա��´μ���ʹ��
     */
    public int delete() {
        seqInfoDiscard = new SYSSEQDISCARD();

        try {
            // ��Ѻ���
            String mortID = req.getFieldValue("mortID");
            // ��Ѻ��ŵò������������ʾ����ʧ��
            if (mortID == null || mortID.equalsIgnoreCase("null")) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }

            /** ��Ѻ��ű���������������� */
            // �������
            seqInfoDiscard.setBhlx(CcbLoanConst.MORTTYPE);
            // ��Ѻ���
            seqInfoDiscard.setDiscardno(mortID);
            // ʹ�ñ�־: δʹ��״̬��
            seqInfoDiscard.setUseflg(0);
            // ԭ��ϵҵ�����:�����������
            seqInfoDiscard.setOseqno(req.getFieldValue("loanID"));
            // ��������
            seqInfoDiscard.setIndate(BusinessDate.getNowDay());
            // ������Ա
            seqInfoDiscard.setInoperid(this.getOperator().getOperid());
            // ��������
            seqInfoDiscard.setDotype("delete");

            if (seqInfoDiscard.insert() < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }

            // ɾ����Ѻ��Ϣ
            dc.executeUpdate("delete from ln_mortinfo where mortid='" + mortID + "'");
            // ��ˮ��־��
            task = MortUtil.getTaskObj(req.getFieldValue("loanid"), req.getFieldValue("busiNode"),
                    CcbLoanConst.OPER_DEL);
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
            logger.error(e.getMessage());
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        this.res.setType(0);
        return 0;
    }

    /**
     * <p/>
     * ����û�ֱ���˳���Ѻ����ҳ�棬��������ɵĵ�Ѻ��ŷŽ����������Ա��´�ʹ��
     * <p/>
     */
    public int saveMortID_bak() {
        seqInfoDiscard = new SYSSEQDISCARD();

        try {
            // ��Ѻ���
            String mortID = req.getFieldValue("mortID");
            /** ��Ѻ��ű���������������� */
            // ҵ������
            seqInfoDiscard.setBhlx(CcbLoanConst.MORTTYPE);
            // ���
            seqInfoDiscard.setDiscardno(mortID);
            // ʹ�ñ�־: δʹ��״̬��
            seqInfoDiscard.setUseflg(0);
            // ԭ��ϵҵ�����:�����������
            seqInfoDiscard.setOseqno(req.getFieldValue("loanID"));

            /** ���жϷϺŹ���������޸�û��ʹ�õ���ţ����û�������� */
            RecordSet rs = dc.executeQuery("select 1 from sys_seq_discard where bhlx='" + CcbLoanConst.MORTTYPE
                    + "' and discardno='" + mortID + "' and useflg='0'");
            if (!rs.next()) {
                if (seqInfoDiscard.insert() < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
            } else {
                // do nothing
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        this.res.setType(0);
        return 0;
    }

    /**
     * <p/>
     * �������
     * <p/>
     * ���˸���ҳ���ϵ�ֵ֮�⣬�û�id������ʱ��Ҳһ����£�
     * <p/>
     * ����ǰ���а汾�ż�飬���Ʋ�������
     *
     * @return
     */
    public int batchEdit() {

        mortInfo = new LNMORTINFO();
        for (int i = 0; i < this.req.getRecorderCount(); i++) {
            try {
                // ��ʼ������bean
                mortInfo.initAll(i, req);
                // ����ʱ��
                mortInfo.setOperdate(BusinessDate.getToday());
                // �û�id
                mortInfo.setOperid(this.getOperator().getOperid());
                // ���
                if (req.getFieldValue(i, "boxid") != null) {
                    mortInfo.setBoxid(req.getFieldValue(i, "boxid"));
                }
                // ����ǰ�汾��
                int iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                int iAfterVersion = 0;
                RecordSet rs = dc.executeQuery("select recversion from ln_mortinfo where mortid='"
                        + req.getFieldValue(i, "mortid") + "'");
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
                        mortInfo.setRecversion(iBeforeVersion);
                    }
                }
                if (rs != null) {
                    rs.close();
                }

                if (mortInfo.updateByWhere(" where mortid='" + req.getFieldValue(i, "mortid") + "'") < 0) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage(PropertyManager.getProperty("300"));
                    return -1;
                }
                // ��ˮ��־��
                task = MortUtil.getTaskObj(req.getFieldValue(i, "loanid"), req.getFieldValue(i, "busiNode"),
                        CcbLoanConst.OPER_BATCHEDIT);
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
      * �޸ĵ�Ѻ��Ϣ�еĴ��������
      * <p/>
      * ����ǰ���а汾�ż�飬���Ʋ�������
      *
      * @return
      */
     public int editLoanId() {

         mortInfo = new LNMORTINFO();
         for (int i = 0; i < this.req.getRecorderCount(); i++) {
             try {
                 // ��ʼ������bean
                 mortInfo.initAll(i, req);

                 LNMORTINFO mortTmp = mortInfo.findFirst(" where  loanid = '" + mortInfo.getLoanid() + "'");
                 if (mortTmp != null) {
                         this.res.setType(0);
                         this.res.setResult(false);
                         this.res.setMessage("�޸Ĵ�������ų��ִ���, ��������ѹ�����:"+mortTmp.getMortid()+ "�ŵ�Ѻ��");
                         return -1;
                 }

                 LNLOANAPPLY  loanTmp = new LNLOANAPPLY();
                 loanTmp = loanTmp.findFirst(" where  loanid = '" + mortInfo.getLoanid() + "'");
                 if (loanTmp == null) {
                         this.res.setType(0);
                         this.res.setResult(false);
                         this.res.setMessage("�޸Ĵ�������ų��ִ���, ������Ų����ڡ�");
                         return -1;
                 }


                 // ����ʱ��
                 mortInfo.setOperdate(BusinessDate.getToday());
                 // �û�id
                 mortInfo.setOperid(this.getOperator().getOperid());
                 // ����ǰ�汾��
                 int iBeforeVersion = 0;
                 if (req.getFieldValue(i, "recVersion") != null && !req.getFieldValue(i, "recVersion").equals("")) {
                     iBeforeVersion = Integer.parseInt(req.getFieldValue(i, "recVersion"));
                 }
                 int iAfterVersion = 0;
                 RecordSet rs = dc.executeQuery("select recversion from ln_mortinfo where mortid='"
                         + req.getFieldValue(i, "mortid") + "'");
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
                         mortInfo.setRecversion(iBeforeVersion);
                     }
                 }
                 if (rs != null) {
                     rs.close();
                 }

                 if (mortInfo.updateByWhere(" where mortid='" + req.getFieldValue(i, "mortid") + "'") < 0) {
                     this.res.setType(0);
                     this.res.setResult(false);
                     this.res.setMessage(PropertyManager.getProperty("300"));
                     return -1;
                 }
                 // ��ˮ��־��
                 task = MortUtil.getTaskObj(req.getFieldValue(i, "loanid"), req.getFieldValue(i, "busiNode"),
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



}
