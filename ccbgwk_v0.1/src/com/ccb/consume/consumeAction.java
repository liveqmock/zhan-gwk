package com.ccb.consume;

/**
 * <p>Title: ��̨ҵ�����</p>
 *
 * <p>Description: ��̨ҵ�����</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * @author
 * @version 1.0
 */

import com.ccb.dao.LNCOOPPROJ;
import com.ccb.dao.LNTASKINFO;
import com.ccb.mortgage.MortUtil;
import com.ccb.util.CcbLoanConst;
import com.ccb.util.SeqUtil;
import gov.mof.fasp.service.BankService;
import gov.mof.fasp.service.adapter.client.FaspServiceAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.BusinessDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class consumeAction extends Action {
    // ��Ѻ��Ϣ����
    LNCOOPPROJ coopproj = null;
    // ϵͳ��־��
    LNTASKINFO task = null;

    private static final Log logger = LogFactory.getLog(consumeAction.class);


    //4.2.4.2	������Ϣ����
    /*
    <list>
    <map>
    <!��������ˮ��-->
    <string> ID </string><string>value</string>
    <!������-->
    <string> ACCOUNT </string><string>value</string>
    <!���ֿ��� -->
    <string> CARDNAME </string><string>2008</string>
    <!����������-->
    <string> BUSIDATE </string><string>yymmdd</string>
    <!�����ѽ��-->
    <string> BUSIMONEY </string><double>0.0</double>
    <!�����ѵص�-->
    <string> BUSINAME </string><string>value</string>
    <!����ٻ�����-->
    <string> Limitdate </string><string>value</string>
    </list>

     */

    public int writeConsumeInfo() {

        //DatabaseConnection conn = ConnectionManager.getInstance().get();

//        String inac_date = "2010-06-08";
//        String sql = "select id,account,cardname,busidate,busimoney,businame,limitdate,tx_cd " +
//                " from ls_consumeinfo where status='10' and inac_date='" + inac_date + "' " +
//                " order by id ";
        String wheresql = " where status='10' order by id ";
        String selectsql = "select id,account,cardname,busidate,busimoney,businame,limitdate,tx_cd from ls_consumeinfo " + wheresql;
        String updatesql = "update ls_consumeinfo set status='20' " + wheresql;

        RecordSet rs = null;
        List cardList = new ArrayList();
        try {
            rs = dc.executeQuery(selectsql);
            while (rs.next()) {
                Map m = new HashMap();
                String id = rs.getString("id");
                String account = rs.getString("account").trim();
                String cardname = rs.getString("cardname").trim();
                String busidate = rs.getString("busidate");
                busidate = busidate.substring(0,4) + busidate.substring(5,7) + busidate.substring(8,10);
                Double busimoney = rs.getDouble("busimoney");
                String businame = rs.getString("businame").trim();
                String limitdate = rs.getString("limitdate");
                limitdate = limitdate.substring(0,4) + limitdate.substring(5,7) + limitdate.substring(8,10);
                String tx_cd = rs.getString("tx_cd");
                if (busimoney <= 0) {
                     limitdate="";
                }
                if ("43".equals(tx_cd)) {
                    busimoney = -busimoney;
                }
                m.put("ID", id);
                m.put("ACCOUNT", account);
                m.put("CARDNAME", cardname);
                m.put("BUSIDATE", busidate);
                m.put("BUSIMONEY", busimoney);
                m.put("BUSINAME", businame);
                m.put("Limitdate", limitdate);
                cardList.add(m);
            }

        } catch (Exception e) {
            logger.error("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��",e);
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage("��ȡ������Ϣ���ִ�����鿴ϵͳ��־��");
            return -1;
        }

        try{
            BankService service = FaspServiceAdapter.getBankService();
            
            if (cardList.size() > 0) {
                List rtnlist = service.writeConsumeInfo("BANK.CCB", "8015", "2010", "405", cardList);
                for (int i = 0; i < rtnlist.size(); i++) {
                    Map m1 = (Map) rtnlist.get(i);
                    String result = (String) m1.get("result");
                    if ("SUCCESS".equalsIgnoreCase(result)) {
                        System.out.println(result);

                        int rtn = dc.executeUpdate(updatesql);
                        if (rtn < 0) {

                        }

                    } else {
                        
                    }

                }
            }
            int i = 0;

//            service.createElementCode("AAA", "FUNC", 2008, ElementCodeList);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (rs != null) {
                rs.close();
            }
//            ConnectionManager.getInstance().release();
        }
        return 0;
    }




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