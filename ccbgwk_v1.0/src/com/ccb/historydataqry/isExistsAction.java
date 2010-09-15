package com.ccb.historydataqry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.DBGrid;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-9-2
 * Time: 10:50:53
 * To change this template use File | Settings | File Templates.
 */
public class isExistsAction extends Action {
     private static final Log logger = LogFactory.getLog(taskAction.class);
    public void getGridContents() {
         String acctNo = req.getFieldValue("acctno");        //账号
        String cardNo = req.getFieldValue("cardNo");         //卡号
        String acctnumber = null;
        String titleName = null;
        if (acctNo.equals("")) {
            acctnumber = cardNo;
            titleName = "卡号";
//            String strAcctNo = "select gwk.fnc_get_contrast(t.sa_acct_no) as acctno  from dcc_saacnacn t where t.sa_card_no='" + cardNo + "'";
//            RecordSet recAcctNo = null;
//            recAcctNo = dc.executeQuery(strAcctNo);
//            while (recAcctNo.next()) {
//                acctnumber = recAcctNo.getString("acctno");
//            }
        } else {
            acctnumber = acctNo;
             titleName = "帐号";
        }


        String strSql =  " select '"+acctnumber+"' as acctno,satn.sa_ddp_acct_no_det_n,satn.sa_curr_cod," +
               " satn.sa_opr_no,(select ct1.cm_opun_inil_1_chn from dcc_cmbctbct ct1 where " +
               " ct1.cm_opun_cod=substr(satn.sa_opr_no,0,9) ) as opac_inil_1_chn" +
                " ,satn.sa_cr_amt,satn.sa_ddp_acct_bal ,satn.sa_dr_amt,satn.sa_tx_crd_no,satn.sa_tx_log_no" +
               " ,satn.sa_rmrk,satn.sa_tx_dt,satn.sa_op_cust_name" +
                " from saacntxn satn  left join dcc_saacnacn acn on satn.sa_acct_no=acn.sa_acct_no" +
               " where satn.sa_tx_dt<=(select t.tx_dt from dcc_historydatadate t where t.tx_id=1) ";
        // 超级用户的判断   隐藏控件获取值
        String isSuper = req.getFieldValue("hidIsSuper");    //超级权限区分
        String operId = req.getFieldValue("hidOpId");       //登陆用户id
       
        if(!"1".equalsIgnoreCase(isSuper)){
            strSql += "  and acn.sa_opac_instn_no ='"+operId+"'";
        }
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("ActionTable");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setField(titleName,"text","18","acctno","true","0");
       dbGrid.setField("明细笔数","text","9","sa_ddp_acct_no_det_n","true","0");
       dbGrid.setField("币别","text","6","sa_curr_cod","true","0");
       dbGrid.setField("操作员号","text","14","sa_opr_no","true","0");
       dbGrid.setField("交易机构名","text","16","opac_inil_1_chn","true","0");
       dbGrid.setField("贷方发生额","text","11","sa_cr_amt","true","0");
       dbGrid.setField("活存帐户余额","text","12","sa_ddp_acct_bal","true","0");
       dbGrid.setField("借方发生额","text","11","sa_dr_amt","true","0");
       dbGrid.setField("交易卡号","text","18","sa_tx_crd_no","true","0");
       dbGrid.setField("交易流水号","text","18","sa_tx_log_no","true","0");
       dbGrid.setField("备注","text","20","sa_rmrk","true","0");
       dbGrid.setField("交易日期","text","9","sa_tx_dt","true","0");
       dbGrid.setField("对方户名","text","10","sa_op_cust_name","true","0");
       dbGrid.setpagesize(50);
       dbGrid.setWhereStr(" and 1=2 ");
       dbGrid.setCheck(false);
        //////数据集按钮
       dbGrid.setdataPilotID("datapilot");
       dbGrid.setbuttons("导出Excel=excel,moveFirst,prevPage,nextPage,moveLast");
        String strAll = dbGrid.getDBGrid()+ "#@$%" + dbGrid.getDataPilot();
        this.res.setFieldName("dbContentsStr");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(strAll);
        this.res.setType(4);
        this.res.setResult(true);
        
    }

    /*
     数据存在与否判断
     return: 0 存在数据但非本权限所有;1 数据不存在
    */
    //对私活期
    public int isExits_dshq() {
//       String isSuper = req.getFieldValue("hidIsSuper");    //超级权限区分
        String operId = req.getFieldValue("hidOpId");       //登陆用户id
        String acctNo = req.getFieldValue("acctno");        //账号
        String cardNo = req.getFieldValue("cardNo");         //卡号
        String sqlStr = " select t.sa_opac_instn_no from dcc_saacnacn t where 1=1 ";
        if (!acctNo.equals("")) {
            sqlStr += " and t.sa_acct_no=fnc_get_grnbzh('" + acctNo + "')";
        }
        if (!cardNo.equals("")) {
            sqlStr += " and t.sa_card_no='" + cardNo + "'";
        }
         RecordSet rec = null;
          RecordSet rec1 = null;
        try {
            rec = dc.executeQuery(sqlStr);
            if (rec.getRecordCount() == 0) {
                this.res.setFieldName("isExitsStr");
                this.res.setFieldType("text");
                this.res.setEnumType("0");
                this.res.setFieldValue(PropertyManager.getProperty("-105"));
                this.res.setType(4);
                this.res.setResult(true);
                return 0;
            } else if (rec.getRecordCount() > 0) {
                String opnInstnNo = "";
                while(rec.next()) {
                    opnInstnNo = rec.getString("sa_opac_instn_no");
                }
                rec1 = dc.executeQuery(sqlStr + " and t.sa_opac_instn_no='"+operId+"'");
                if (rec1.getRecordCount() == 0) {
                    this.res.setFieldName("isExitsStr");
                    this.res.setFieldType("text");
                    this.res.setEnumType("0");
                    this.res.setFieldValue("该账户的开户行为：" + opnInstnNo + ",请到开户行查询该数据。");
                    //PropertyManager.getProperty("309")
                    this.res.setType(4);
                    this.res.setResult(true);
                    return 0;
                }
            }

        } catch (Exception ex) {
           ex.printStackTrace();
        } finally {
            if (rec != null) {
                rec.close();
            }
            if (rec1 != null) {
                rec1.close();
            }
        }
        this.res.setFieldName("isExitsStr");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue("true");
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }
    //对公活期
    public void isExits_dghq() {
         String operId = req.getFieldValue("hidOpId");       //登陆用户id
        String acctNo = req.getFieldValue("acctno");        //账号
        String sqlStr = " select t.sa_opac_instn_no from dcc_saecnecn t where 1=1 ";
        if (!acctNo.equals("")) {
            sqlStr += " and t.sa_acct_no=fnc_get_dgnbzh('" + acctNo + "')";
        }
       
         RecordSet rec = null;
          RecordSet rec1 = null;
        try {
            rec = dc.executeQuery(sqlStr);
            if (rec.getRecordCount() == 0) {
                this.res.setFieldName("isExitsStr");
                this.res.setFieldType("text");
                this.res.setEnumType("0");
                this.res.setFieldValue(PropertyManager.getProperty("-105"));
                this.res.setType(4);
                this.res.setResult(true);
            } else if (rec.getRecordCount() > 0) {
                String opnInstnNo = "";
                while(rec.next()) {
                    opnInstnNo = rec.getString("sa_opac_instn_no");
                }
                rec1 = dc.executeQuery(sqlStr + " and t.sa_opac_instn_no='"+operId+"'");
                if (rec1.getRecordCount() == 0) {
                    this.res.setFieldName("isExitsStr");
                    this.res.setFieldType("text");
                    this.res.setEnumType("0");
                    this.res.setFieldValue("该账户的开户行为：" + opnInstnNo + ",请到开户行查询该数据。");
                    //PropertyManager.getProperty("309")
                    this.res.setType(4);
                    this.res.setResult(true);
                }
            }

        } catch (Exception ex) {
           ex.printStackTrace();
        } finally {
            if (rec != null) {
                rec.close();
            }
            if (rec1 != null) {
                rec1.close();
            }
        }
        this.res.setFieldName("isExitsStr");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue("true");
        this.res.setType(4);
        this.res.setResult(true);
    }
    //对公定期
    public void isExits_dgdq() {
         String operId = req.getFieldValue("hidOpId");       //登陆用户id
        String acctNo = req.getFieldValue("acctno");        //账号
        String sqlStr = " select t.td_opac_instn_no from dcc_tdacnacn t where 1=1 ";
        if (!acctNo.equals("")) {
            sqlStr += " and t.td_td_acct_no=fnc_get_dgnbzh('" + acctNo + "')";
        }

         RecordSet rec = null;
          RecordSet rec1 = null;
        try {
            rec = dc.executeQuery(sqlStr);
            if (rec.getRecordCount() == 0) {
                this.res.setFieldName("isExitsStr");
                this.res.setFieldType("text");
                this.res.setEnumType("0");
                this.res.setFieldValue(PropertyManager.getProperty("-105"));
                this.res.setType(4);
                this.res.setResult(true);
            } else if (rec.getRecordCount() > 0) {
                String opnInstnNo = "";
                while(rec.next()) {
                    opnInstnNo = rec.getString("td_opac_instn_no");
                }
                rec1 = dc.executeQuery(sqlStr + " and t.td_opac_instn_no='"+operId+"'");
                if (rec1.getRecordCount() == 0) {
                    this.res.setFieldName("isExitsStr");
                    this.res.setFieldType("text");
                    this.res.setEnumType("0");
                    this.res.setFieldValue("该账户的开户行为：" + opnInstnNo + ",请到开户行查询该数据。");
                    //PropertyManager.getProperty("309")
                    this.res.setType(4);
                    this.res.setResult(true);
                }
            }

        } catch (Exception ex) {
           ex.printStackTrace();
        } finally {
            if (rec != null) {
                rec.close();
            }
            if (rec1 != null) {
                rec1.close();
            }
        }
        this.res.setFieldName("isExitsStr");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue("true");
        this.res.setType(4);
        this.res.setResult(true);
    }
    //对私定期
    public void isExits_dsdq() {
         String operId = req.getFieldValue("hidOpId");       //登陆用户id
        String acctNo = req.getFieldValue("acctno");        //账号
        String sqlStr = " select t.td_opac_instn_no from dcc_tdacnacn t where 1=1 ";
        if (!acctNo.equals("")) {
            sqlStr += " and t.td_td_acct_no=fnc_get_grnbzh('" + acctNo + "')";
        }

         RecordSet rec = null;
          RecordSet rec1 = null;
        try {
            rec = dc.executeQuery(sqlStr);
            if (rec.getRecordCount() == 0) {
                this.res.setFieldName("isExitsStr");
                this.res.setFieldType("text");
                this.res.setEnumType("0");
                this.res.setFieldValue(PropertyManager.getProperty("-105"));
                this.res.setType(4);
                this.res.setResult(true);
            } else if (rec.getRecordCount() > 0) {
                String opnInstnNo = "";
                while(rec.next()) {
                    opnInstnNo = rec.getString("td_opac_instn_no");
                }
                rec1 = dc.executeQuery(sqlStr + " and t.td_opac_instn_no='"+operId+"'");
                if (rec1.getRecordCount() == 0) {
                    this.res.setFieldName("isExitsStr");
                    this.res.setFieldType("text");
                    this.res.setEnumType("0");
                    this.res.setFieldValue("该账户的开户行为：" + opnInstnNo + ",请到开户行查询该数据。");
                    //PropertyManager.getProperty("309")
                    this.res.setType(4);
                    this.res.setResult(true);
                }
            }

        } catch (Exception ex) {
           ex.printStackTrace();
        } finally {
            if (rec != null) {
                rec.close();
            }
            if (rec1 != null) {
                rec1.close();
            }
        }
        this.res.setFieldName("isExitsStr");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue("true");
        this.res.setType(4);
        this.res.setResult(true);
    }
}

