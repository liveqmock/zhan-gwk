package com.ccb.odsb;

/**
 * <p>Title: ��̨ҵ�����</p>
 *
 * <p>Description: ��̨ҵ�����</p>
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
     * ��ODSB��������
     * 1���Ա��ر��м����Ѵ�ODSB��ȡ�õ����������������
     * 2����ODSB���л�ȡ�����������֮�����������  (ͬʱ���¹��񿨻�����Ϣ��������״̬����)
     * 3��������ˮ�ţ����뵽�������ѱ���
     *
     * @return
     */
    public int readFromODSB() {
        int rtn = 0;
        int lshCount = 0;
        StringBuffer show_content = new StringBuffer();
        try {
            // ���ODSB״̬
             String jobflow_status = checkODSBStatus();
            if (jobflow_status == null) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("���ODSBϵͳ�ӿ�״̬ʧ�ܣ�������������ݿ����ӡ�");
                return -1;
            }
            //0����ʼ״̬�� 1������״̬�� 2�����״̬��3��δ��ʼ����4:ʱ������������ʼ��
            if (!"2".equals(jobflow_status) && !"3".equals(jobflow_status)) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("ODSBϵͳ��δ���������Ժ�");
                return -1;
            }
            show_content.append("�ɶ�").append("_");
            //ȡ�ñ��ر���������������
             String last_inac_date = queryLastInacDate();
             show_content.append(last_inac_date).append("_");
            // BF_EVT_CRD_CRT_TRAD  ���ǿ�������ϸ
            // BF_AGT_CRD_CRT  ���ǿ�
            // BF_AGT_CRD_CRT_ACCT ���ǿ��˻�
            //TODO ȷ��tx_cd�ĺ��� 40 43
            // �������������֮���odsb�Ĵ��ǿ�������ϸ���ݶ���gwk
            rtn = insertIntoGwk(last_inac_date);
            if (rtn < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("��ȡODSB����������ʧ�ܣ�������������ݿ����ӡ�");
                return -1;
            }
            show_content.append(String.valueOf(rtn)).append("_");
             //������Ϣ��
            int crd_rtn = insertCrdCrt();
            if (crd_rtn < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("��ȡODSB����������ʧ�ܣ�������������ݿ����ӡ�");
                return -1;
            }
            show_content.append(String.valueOf(rtn));
             //ȡ�ñ��ر��б��մ�����������ˮ��
            String yymmdd = DateUtil.getDateStr();
            int i_last_today_lsh = getLastLsh(yymmdd);
            // ��ȡ��Ҫ��ʱ���ʽ
            Date date = new Date();
            String strtime = pub.platform.utils.StringUtils.toDateFormat(date,"HH:mm:ss");
            String dateTime = DateUtil.getCurrentDateTime();
            String currentDate = DateUtil.getCurrentDate();
            //�����ر�
            /*
              String sql = " select a.*,b.cardname,c.stmt_day " +
                    " from odsb_crd_crt_trad a, " +
                    " ls_cardbaseinfo b, " +
                    " odsb_crd_crt c " +
                    " where a.crd_no=b.account and a.crd_no=c.crd_no ";
            */
            // ����ֶ�areacode 2012-12-14
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

            //�����ն�ν��е�����������
            lshCount += i_last_today_lsh;

            int count = 0;
            while (rs.next()) {
                //��ˮ��
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
                    businame = "��";
                }
                consume.setBusiname(businame);
                consume.setCardname(rs.getString("cardname"));

                //��ٻ����մ���
                String stmt_day = rs.getString("stmt_day");
                String inac_day = rs.getString("inac_date");
                String limitdate = getLimitDate(inac_day, stmt_day);
                consume.setLimitdate(limitdate);

                consume.setRemark("��ODSB��ʼ����");
                consume.setOperid("9999");
                consume.setOperdate(dateTime);
                consume.setStatus("10");
                consume.setTx_cd(rs.getString("tx_cd"));
                consume.setRef_number(rs.getString("ref_nmbr"));
                consume.setCmbi_merch_no(rs.getString("cmbi_merch_no"));
                consume.setInac_date(inac_day);
                consume.setInac_amt(rs.getDouble("inac_amt"));
                consume.setTxlog("��ODSB��ʼ����");
                consume.setOdsbdate(currentDate);
                consume.setOdsbtime(strtime);
                consume.setRecversion(0);

                //20110210 zhanrui  ���������ο��ֶ� ����Ψһ��ʶһ����¼ ��δ���������
                consume.setRef_date(rs.getString("ref_date"));
                consume.setRef_batch_id(rs.getString("ref_batch_id"));
                consume.setRef_seq_no(rs.getString("ref_seq_no"));
                // ����ֶ�areacode2012-12-14 linyong
                consume.setAreacode(rs.getString("areacode"));

                int insertrtn = consume.insert();
                if (insertrtn < 0 ) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage("����ODSB����������ʧ�ܣ���鿴ϵͳ������־��");
                    return -1;
                }
            }
             // ���ζ�ȡ�����ݼ�¼��
            show_content.append("_").append(String.valueOf(count));
            // ���ն�ȡ����������
            show_content.append("_").append(String.valueOf(lshCount));

            // ���ж������ݣ�����ӱ��ζ�ȡ�����ݵ���ʼ����ֹ��ˮ��
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

        //����odsb״̬_���ر�������������_��ȡODSB���������ݼ�¼��_��ȡODSB���������ݼ�¼��_���ζ����¼��_���ն�ȡ��������_��ʼ������ˮ�ߺ�_��ֹ������ˮ�ߺ�
        this.res.setFieldName("importData");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(new String(show_content));
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }
    // ���ODSB״̬
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
    //ȡ�ñ��ر���������������
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
    //ȡ�ñ��ر���ĳ��(yymmdd)������������ˮ��
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
    // �����������գ�inac_date����odsb�Ĵ��ǿ�������ϸ���ݶ���gwk
    private int insertIntoGwk(String inac_date) {
        int exeCount = 0;
        /*
        2011/2/10 zhanrui
        �޸Ļ�ȡODSB���ݹ����ɸ����������ڻ�ȡ�޸�Ϊ���� �����ο����ֶ� ��ȡ��
        ��ȡ��������ODSB��ȡ����Ӧ��������Ϣ ,��Ч������ �Ľ��� inac_date >= ��ȡ�����ݵ������������֮ǰ7��

        String sql = " insert into odsb_crd_crt_trad  " +
                    " select a.* from  odsbdata.BF_EVT_CRD_CRT_TRAD@odsb_remote a, ls_cardbaseinfo b " +
                    " where a.crd_no = b.account and a.tx_cd in ('40','43') and a.inac_date > '" + inac_date + "' ";
        */
        String sql = " insert into odsb_crd_crt_trad  " +
                    " select a.* from  odsbdata.BF_EVT_CRD_CRT_TRAD@odsb_remote a, ls_cardbaseinfo b " +
                    " where a.crd_no = b.account and a.tx_cd in ('40','43') and a.inac_date > '" + getReadOdsbDate(inac_date) + "' ";

        // ��Ч������ �Ľ��� inac_date >= ��ȡ�����ݵ������������֮ǰ7��
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
        //TODO �жϷ���ֵ ��ȷ��ɾ������
        exeCount = dc.executeUpdate(" delete from odsb_crd_crt_trad ");
        exeCount = dc.executeUpdate(sql);
        return exeCount;
    }
     //������Ϣ��
     private int insertCrdCrt() {
           String sql = " insert into odsb_crd_crt  " +
                    " select * from  odsbdata.BF_AGT_CRD_CRT@odsb_remote  " +
                    " where crd_no like '62836600%' ";
            int crd_rtn = 0;
            dc.executeUpdate(" truncate table odsb_crd_crt ");
            crd_rtn = dc.executeUpdate(sql);
            return crd_rtn;
     }

    // inac_date ������    ����10
    // stmt_day �˵���    ����3
    private String getLimitDate(String inac_date, String stmt_day) {
        if (inac_date == null || stmt_day == null) {
            throw new RuntimeException("��������");
        }
        int i_inac_date_day = Integer.parseInt(inac_date.substring(8, 10));
        int i_stmt_day = Integer.parseInt(stmt_day);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String rtndate = null;
        Calendar c = new GregorianCalendar();
        if (i_stmt_day <= i_inac_date_day) {
            //�������ڻ�������ͬ��֮ǰ����ٻ�������Ϊ�����պ�20����Ȼ��
            c.set(Calendar.DATE, i_stmt_day);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, 20);
        } else {
            //�������ڻ�����֮����ٻ�������Ϊ�¸��µ��ʵ��ռ�20��
            c.set(Calendar.DATE, i_stmt_day);
            c.add(Calendar.DATE, 20);
        }
        rtndate = df.format(c.getTime());
        return rtndate;
    }

    /**
     * ���ر������������ǰ7�������
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
            logger.error("����ת������");
            cal.setTime(new Date(inac_date));
        }
        cal.add(Calendar.DATE, -7);
        return  sdf.format(cal.getTime());
    }
}
