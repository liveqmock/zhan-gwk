package com.ccb.cardinfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Thinkpad
 * Date: 13-9-12
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class ProvCardInfoAction extends Action{

    private static final Log logger = LogFactory.getLog(ProvCardInfoAction.class);

    public int queryCardInfoList(){
        DatabaseConnection conn = ConnectionManager.getInstance().get();
        RecordSet rs = null;
        int ret = -1;
        try {
            conn.begin();
            List cardList = new ArrayList();
            String querySql = "select a.account,a.bdgagency,a.cardname from ls_cardbaseinfo a where a.sentflag = '0'";
            if(dc == null){

            }
            rs = conn.executeQuery(querySql);
            Map rowData = new HashMap();
            int columnCount = rs.getColumnCount(); //返回此 ResultSet 对象中的列数
            if (rs != null) {
                while (rs.next()) {
                    rowData = new HashMap(columnCount);
                    for (int i = 0; i < columnCount; i++) {
                        rowData.put(rs.getFieldName(i), rs.getObject(i));
                    }
                    cardList.add(rowData);
                }
            }
            conn.commit();
            if((cardList == null)||(cardList.size()==0)){
                ret = 0;
            }else {
                String strData = this.loadCardData(cardList);
                String exportPath = PropertyManager.getProperty("EXPORT_ROOT_PATH");
                String excelFilename = "qdcard" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt";
                boolean bolResult = false;
                bolResult = this.generateLocalFileData(exportPath,excelFilename,strData);
                if(bolResult) {
                    updateCardSentFlag(conn,"");
                }
            }
        } catch (Exception e) {
            conn.rollback();
            logger.error(e);
            ret = -1;
        } finally {
            if (rs != null) {
                rs.close();
            }
            ConnectionManager.getInstance().release();
        }
        ret = 0;
        return ret;
    }

    //根据查询结果组合内容
    public String loadCardData(List list){
        StringBuffer strBuffer = new StringBuffer();
        for(int i =0; i<list.size();i++){
            Map mapCard = new HashMap();
            mapCard = (Map)list.get(i);
            strBuffer.append((String)mapCard.get("account"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("bdgagency"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("cardname"));
            strBuffer.append("|");
            strBuffer.append("\n");
        }

        return strBuffer.toString();
    }

    //根据所属区域更新为已发送 2013-09-14
    private int updateCardSentFlag(DatabaseConnection conn,String areaCode){
        int rtnCnt = 0;
        String updateSql = "update ls_cardbaseinfo set sentflag = '1' where  sentflag = '0'"+
                " and areaCode='"+areaCode+"'";
        rtnCnt = conn.executeUpdate(updateSql);
        logger.info(updateSql);
        return rtnCnt;
    }

    public static void main(String[] args){
        ProvCardInfoAction action = new ProvCardInfoAction();
//        action.queryCardInfoList();
        action.queryConsumeInfoList();
    }

    public int queryConsumeInfoList(){
        DatabaseConnection conn = ConnectionManager.getInstance().get();
        RecordSet rs = null;
        int ret = -1;
        try {
            conn.begin();
            List consumeList = new ArrayList();
            String querySql = "select a.* from odsb_crd_crt_trad a left outer join ls_consumeinfo b on " +
                    " a.ref_date = b.ref_date and a.ref_batch_id = b.ref_batch_id and a.ref_seq_no = b.ref_seq_no " +
                    " where b.status = '10'";
            rs = conn.executeQuery(querySql);
            Map rowData = new HashMap();
            int columnCount = rs.getColumnCount(); //返回此 ResultSet 对象中的列数
            if (rs != null) {
                while (rs.next()) {
                    rowData = new HashMap(columnCount);
                    for (int i = 0; i < columnCount; i++) {
                        rowData.put(rs.getFieldName(i), rs.getObject(i));
                    }
                    consumeList.add(rowData);
                }
            }
            conn.commit();
            if((consumeList == null)||(consumeList.size()==0)){
                ret = 0;
            }else {
                String strData = this.loadConsumeData(consumeList);
                String exportPath = PropertyManager.getProperty("EXPORT_ROOT_PATH");
                String excelFilename = "qdcost" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt";
                boolean bolResult = false;
                bolResult =  this.generateLocalFileData(exportPath, excelFilename, strData);
                if(bolResult) {
                    updateConsumeStatus(conn,"");
                }
            }
        } catch (Exception e) {
            conn.rollback();
            logger.error(e);
            ret = -1;
        } finally {
            if (rs != null) {
                rs.close();
            }
            ConnectionManager.getInstance().release();
        }
        ret = 0;
        return ret;
    }
    //若有重复发送的流水号或者帐号，则将原状态改为发送成功
    private int updateConsumeStatus(DatabaseConnection conn,String areaCode) {
        String updateSameIdOKSql = "update ls_consumeinfo set status='20' where areacode='"
                + areaCode + "' and status='10'";
        logger.info(updateSameIdOKSql);
        int rtn = conn.executeUpdate(updateSameIdOKSql);
        return rtn;
    }

    //根据查询结果组合内容
    public String loadConsumeData(List list){
        StringBuffer strBuffer = new StringBuffer();
        for(int i =0; i<list.size();i++){
            Map mapCard = new HashMap();
            mapCard = (Map)list.get(i);
            strBuffer.append("371");
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("tx_day"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("actp"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("crd_typ"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("crd_no"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("tx_day"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("tx_cd"));
            strBuffer.append("|");
            //入账金额
            BigDecimal inac_amt = (BigDecimal)mapCard.get("inac_amt");
            strBuffer.append(inac_amt.toString());
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("inac_date"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("src_cd"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("dhi_auth_code"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("ref_date"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("ref_batch_id"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("ref_seq_no"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("merchant_org"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("dhi_merch_acct"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("merchant_knd_no"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("filler_2"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("filler_3"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("tx_dscrp_cntry"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("merch_state"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("dhi_posting_flag"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("ref_nmbr"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("chgbk_flg"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("orig_curr_code"));
            strBuffer.append("|");
            //原交易金额
            BigDecimal orig_curr_amt = (BigDecimal)mapCard.get("orig_curr_amt");
            strBuffer.append(orig_curr_amt.toString());
            strBuffer.append("|");
            BigDecimal orig_curr_decimal = (BigDecimal)mapCard.get("orig_curr_decimal");
            strBuffer.append(orig_curr_decimal.toString());
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("onus_curr_conv"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("dhi_pos_mode"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("mail_order_flg"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("cmbi_merch_no"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("dhi_installment_ind"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("trad_chanel"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("tx_type"));
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("tx_cd_new"));
            strBuffer.append("|");
            strBuffer.append("");
            strBuffer.append("|");
            strBuffer.append((String)mapCard.get("tx_day"));
            strBuffer.append("|");
            strBuffer.append("\n");
        }

        return strBuffer.toString();
    }

    public boolean generateLocalFileData(String localPath, String fileName, String fileData) throws IOException {
        boolean flag = false;
        logger.info(fileData);
        FileOutputStream fos = null;
        try {
            File file = new File(localPath, fileName);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(fileData.getBytes());
            flag = true;
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            fos.flush();
            fos.close();
        }
        return flag;
    }
}
