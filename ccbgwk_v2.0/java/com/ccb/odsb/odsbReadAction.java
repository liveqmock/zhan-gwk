package com.ccb.odsb;

/**
 * <p>Title: 后台业务组件</p>
 *
 * <p>Description: 后台业务组件</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 */

import com.ccb.dao.LSCONSUMEINFO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class odsbReadAction extends Action {
    private static final Log logger = LogFactory.getLog(odsbReadAction.class);

    /**
     * <p/>
     * 从ODSB读入数据
     * 1、自本地表中检索已从ODSB中取得的消费数据最后日期
     * 2、自ODSB表中获取本地最后消费之后的消费数据  (同时更新公务卡基本信息，包括卡状态处理)
     * 3、生成流水号，插入到本地消费表中
     *
     * @return
     */
    public int readFromODSB() {
        int rtn = 0;
        int lshCount = 0;
        StringBuffer show_content = new StringBuffer();
        try {
            // 检查ODSB状态
             String jobflow_status = checkODSBStatus();
            if (jobflow_status == null) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("检查ODSB系统接口状态失败，请检查网络或数据库连接。");
                return -1;
            }
            //0：初始状态， 1：运行状态， 2：完成状态，3：未初始化，4:时间驱动，待初始化
            if (!"2".equals(jobflow_status) && !"3".equals(jobflow_status)) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("ODSB系统尚未就绪，请稍候。");
                return -1;
            }
            show_content.append("可读").append("_");
            //取得本地表中最后的入帐日期
             String last_inac_date = queryLastInacDate();
             show_content.append(last_inac_date).append("_");
            // BF_EVT_CRD_CRT_TRAD  贷记卡交易明细
            // BF_AGT_CRD_CRT  贷记卡
            // BF_AGT_CRD_CRT_ACCT 贷记卡账户
            //TODO 确认tx_cd的含义 40 43
            // 将最后入账日期之后的odsb的贷记卡交易明细数据读入gwk
            rtn = insertIntoGwk(last_inac_date);
            if (rtn < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("读取ODSB卡消费数据失败，请检查网络或数据库连接。");
                return -1;
            }
            show_content.append(String.valueOf(rtn)).append("_");
             //处理卡信息表
            int crd_rtn = insertCrdCrt();
            if (crd_rtn < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("读取ODSB卡基本数据失败，请检查网络或数据库连接。");
                return -1;
            }
            show_content.append(String.valueOf(rtn));
             //取得本地表中本日处理过的最大流水号
            String yymmdd = DateUtil.getDateStr();
            int i_last_today_lsh = getLastLsh(yymmdd);
            // 获取需要的时间格式
            Date date = new Date();
            String strtime = pub.platform.utils.StringUtils.toDateFormat(date,"HH:mm:ss");
            String dateTime = DateUtil.getCurrentDateTime();
            String currentDate = DateUtil.getCurrentDate();
            //处理本地表
            /*
              String sql = " select a.*,b.cardname,c.stmt_day " +
                    " from odsb_crd_crt_trad a, " +
                    " ls_cardbaseinfo b, " +
                    " odsb_crd_crt c " +
                    " where a.crd_no=b.account and a.crd_no=c.crd_no ";
            */
            // 添加字段areacode 2012-12-14
            String sql = "select a.*, b.cardname,b.areacode, c.stmt_day " +
                    "  from (select * " +
                    "  from odsb_crd_crt_trad t1 " +
                    "  where not exists " +
                    "  (select 1 from ls_consumeinfo t2 " +
                    "  where nvl(t1.ref_date, ' ') = nvl(t2.ref_date, ' ') " +
                    "  and nvl(t1.ref_batch_id, ' ') = nvl(t2.ref_batch_id, ' ') " +
                    "  and nvl(t1.ref_seq_no, ' ') = nvl(t2.ref_seq_no, ' '))) a, " +
                    "  ls_cardbaseinfo b, " +
                    "  odsb_crd_crt c " +
                    "  where a.crd_no = b.account " +
                    "  and a.crd_no = c.crd_no";
            rs = dc.executeQuery(sql);
            LSCONSUMEINFO consume = new LSCONSUMEINFO();

            //处理本日多次进行导入操作的情况
            lshCount += i_last_today_lsh;

            int count = 0;
            while (rs.next()) {
                //流水号
                count++;
                lshCount++;
                String lsh = String.valueOf(lshCount);
                lsh = yymmdd + StringUtils.leftPad(lsh, 7, '0');
                consume.setLsh(lsh);
                consume.setAccount(rs.getString("crd_no"));
                consume.setBusidate(rs.getString("tx_day"));
                consume.setBusimoney(rs.getDouble("inac_amt"));
                String businame = rs.getString("filler_2");
                if (businame == null) {
                    businame = "无";
                }
                consume.setBusiname(businame);
                consume.setCardname(rs.getString("cardname"));

                //最迟还款日处理
                String stmt_day = rs.getString("stmt_day");
                String inac_day = rs.getString("inac_date");
                String limitdate = getLimitDate(inac_day, stmt_day);
                consume.setLimitdate(limitdate);

                consume.setRemark("自ODSB初始读入");
                consume.setOperid("9999");
                consume.setOperdate(dateTime);
                consume.setStatus("10");
                consume.setTx_cd(rs.getString("tx_cd"));
                consume.setRef_number(rs.getString("ref_nmbr"));
                consume.setCmbi_merch_no(rs.getString("cmbi_merch_no"));
                consume.setInac_date(inac_day);
                consume.setInac_amt(rs.getDouble("inac_amt"));
                consume.setTxlog("自ODSB初始读入");
                consume.setOdsbdate(currentDate);
                consume.setOdsbtime(strtime);
                consume.setRecversion(0);

                //20110210 zhanrui  新增三个参考字段 用于唯一标识一条记录 （未添加索引）
                consume.setRef_date(rs.getString("ref_date"));
                consume.setRef_batch_id(rs.getString("ref_batch_id"));
                consume.setRef_seq_no(rs.getString("ref_seq_no"));
                // 添加字段areacode2012-12-14 linyong
                consume.setAreacode(rs.getString("areacode"));

                int insertrtn = consume.insert();
                if (insertrtn < 0 ) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage("导入ODSB卡消费数据失败，请查看系统错误日志。");
                    return -1;
                }
            }
             // 本次读取的数据记录数
            show_content.append("_").append(String.valueOf(count));
            // 本日读取的数据总数
            show_content.append("_").append(String.valueOf(lshCount));

            // 若有读入数据，则添加本次读取的数据的起始和终止流水号
            if(count > 0){
            show_content.append("_").append(yymmdd + StringUtils.leftPad(String.valueOf(i_last_today_lsh+1), 7, '0')).append("_");
            show_content.append(yymmdd + StringUtils.leftPad(String.valueOf(lshCount), 7, '0'));
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            //e.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        //返回odsb状态_本地表中最后的入帐日_读取ODSB卡消费数据记录数_读取ODSB卡基本数据记录数_本次读入记录数_本日读取数据总数_起始数据流水线号_终止数据流水线号
        this.res.setFieldName("importData");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(new String(show_content));
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }
    // 检查ODSB状态
    private String checkODSBStatus(){
         String checksql = "select TO_CHAR(biz_date, 'yyyymmdd'),Jobflow_status " +
                    "from odssys.f_jci_jobflowinstance@odsb_remote " +
                    "where job_flow_id ='990063719900001' ";
            String jobflow_status = null;
            RecordSet rs = dc.executeQuery(checksql);
            while (rs.next()) {
                jobflow_status = rs.getString("Jobflow_status");
            }
        return jobflow_status;
    }
    //取得本地表中最后的入帐日期
     private String queryLastInacDate(){
           String sql = "select max(inac_date) as inac_date from ls_consumeinfo ";
            String last_inac_date = null;
            rs = dc.executeQuery(sql);
            while (rs.next()) {
                last_inac_date = rs.getString("inac_date");
            }
            if (last_inac_date == null) {
                last_inac_date = "1899-01-01";
            }
         return last_inac_date;
     }
    //取得本地表中某日(yymmdd)处理过的最大流水号
    private int getLastLsh(String yymmdd){
           String sql = "select max(lsh) as lsh from ls_consumeinfo where lsh like '" + yymmdd + "%'";
            String last_today_lsh = null;
            int i_last_today_lsh = 0;
            rs = dc.executeQuery(sql);
            while (rs.next()) {
                last_today_lsh = rs.getString("lsh");
            }
            if (last_today_lsh != null) {
                i_last_today_lsh = Integer.parseInt(last_today_lsh.substring(8, 15));
            }
        return i_last_today_lsh;
    }
    // 将大于入账日（inac_date）的odsb的贷记卡交易明细数据读入gwk
    private int insertIntoGwk(String inac_date) {
        int exeCount = 0;
        /*
        2011/2/10 zhanrui
        修改获取ODSB数据规则：由根据入帐日期获取修改为根据 三个参考类字段 获取。
        获取方法：由ODSB读取卡对应的消费信息 ,因效率问题 改进成 inac_date >= 已取回数据的最后入帐日期之前7天

        String sql = " insert into odsb_crd_crt_trad  " +
                    " select a.* from  odsbdata.BF_EVT_CRD_CRT_TRAD@odsb_remote a, ls_cardbaseinfo b " +
                    " where a.crd_no = b.account and a.tx_cd in ('40','43') and a.inac_date > '" + inac_date + "' ";
        */
        String sql = " insert into odsb_crd_crt_trad  " +
                    " select a.* from  odsbdata.BF_EVT_CRD_CRT_TRAD@odsb_remote a, ls_cardbaseinfo b " +
                    " where a.crd_no = b.account and a.tx_cd in ('40','43') and a.inac_date > '" + getReadOdsbDate(inac_date) + "' ";

        // 因效率问题 改进成 inac_date >= 已取回数据的最后入帐日期之前7天
/*
        String sql = "insert into odsb_crd_crt_trad  " +
                " select t1.* from " +
                "     (select a.* from  odsbdata.BF_EVT_CRD_CRT_TRAD@odsb_remote a, ls_cardbaseinfo b " +
                "        where a.crd_no = b.account and a.tx_cd in ('40','43')) t1 and a.inac_date >= '" +  getReadOdsbDate(inac_date) + "' " +
                "   where not exists " +
                "      (select 1 from ls_consumeinfo t2 " +
                "         where nvl(t1.ref_date,' ') = nvl(t2.ref_date,' ')  " +
                "           and nvl(t1.ref_batch_id,' ') = nvl(t2.ref_batch_id,' ')" +
                "           and nvl(t1.ref_seq_no,' ') = nvl(t2.ref_seq_no,' ') ) ";
*/

        //exeCount = dc.executeUpdate(" truncate table odsb_crd_crt_trad ");
        //TODO 判断返回值 须确保删除无误
        exeCount = dc.executeUpdate(" delete from odsb_crd_crt_trad ");
        exeCount = dc.executeUpdate(sql);
        return exeCount;
    }
     //处理卡信息表
     private int insertCrdCrt() {
           String sql = " insert into odsb_crd_crt  " +
                    " select * from  odsbdata.BF_AGT_CRD_CRT@odsb_remote  " +
                    " where crd_no like '62836600%' ";
            int crd_rtn = 0;
            dc.executeUpdate(" truncate table odsb_crd_crt ");
            crd_rtn = dc.executeUpdate(sql);
            return crd_rtn;
     }

    // inac_date 入账日    长度10
    // stmt_day 账单日    长度3
    private String getLimitDate(String inac_date, String stmt_day) {
        if (inac_date == null || stmt_day == null) {
            throw new RuntimeException("参数错误");
        }
        int i_inac_date_day = Integer.parseInt(inac_date.substring(8, 10));
        int i_stmt_day = Integer.parseInt(stmt_day);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String rtndate = null;
        Calendar c = new GregorianCalendar();
        if (i_stmt_day <= i_inac_date_day) {
            //入帐日在还款日相同或之前，最迟还款日期为还款日后20个自然日
            c.set(Calendar.DATE, i_stmt_day);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, 20);
        } else {
            //入帐日在还款日之后，最迟还款日期为下个月的帐单日加20天
            c.set(Calendar.DATE, i_stmt_day);
            c.add(Calendar.DATE, 20);
        }
        rtndate = df.format(c.getTime());
        return rtndate;
    }

    /**
     * 本地表最后入帐日期前7天的日期
     * @param inac_date
     * @return
     * @throws ParseException
     */
    private String getReadOdsbDate(String inac_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(sdf.parse(inac_date));
        } catch (ParseException e) {
            //TODO
            logger.error("日期转换错误！");
            cal.setTime(new Date(inac_date));
        }
        cal.add(Calendar.DATE, -7);
        return  sdf.format(cal.getTime());
    }
}
