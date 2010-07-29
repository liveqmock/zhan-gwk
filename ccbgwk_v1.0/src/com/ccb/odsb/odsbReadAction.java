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
        int count = 0;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df_time = new SimpleDateFormat("HH:mm:ss");

        Date date = new Date();
        String strdate = df.format(date);
        String strtime = df_time.format(date);
        String yymmdd = strdate.substring(0, 4) + strdate.substring(5, 7) + strdate.substring(8, 10);
        String newLsh = "";
        try {
            // ���ODSB״̬
            String checksql = "select TO_CHAR(biz_date, 'yyyymmdd'),Jobflow_status " +
                    "from odssys.f_jci_jobflowinstance@odsb_remote " +
                    "where job_flow_id ='990063719900001' ";
            String jobflow_status = null;
            RecordSet rs = dc.executeQuery(checksql);
            while (rs.next()) {
                jobflow_status = rs.getString("Jobflow_status");
            }
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


            //ȡ�ñ��ر���������������
            String sql = "select max(inac_date) as inac_date from ls_consumeinfo ";
            String last_inac_date = null;
            rs = dc.executeQuery(sql);
            while (rs.next()) {
                last_inac_date = rs.getString("inac_date");
            }
            if (last_inac_date == null) {
                last_inac_date = "1899-01-01";
            }

            //ȡ�ñ��ر��б��մ�����������ˮ��
            sql = "select max(lsh) as lsh from ls_consumeinfo where lsh like '" + yymmdd + "%'";
            String last_today_lsh = null;
            int i_last_today_lsh = 0;
            rs = dc.executeQuery(sql);
            while (rs.next()) {
                last_today_lsh = rs.getString("lsh");
            }
            if (last_today_lsh != null) {
                i_last_today_lsh = Integer.parseInt(last_today_lsh.substring(8, 15));
            }

            // BF_EVT_CRD_CRT_TRAD  ���ǿ�������ϸ
            // BF_AGT_CRD_CRT  ���ǿ�
            // BF_AGT_CRD_CRT_ACCT ���ǿ��˻�
            //TODO ȷ��tx_cd�ĺ��� 40 43
            // ��odsb�Ĵ��ǿ�������ϸ���ݶ���gwk
            sql = " insert into odsb_crd_crt_trad  " +
                    " select a.* from  odsbdata.BF_EVT_CRD_CRT_TRAD@odsb_remote a, ls_cardbaseinfo b " +
                    " where a.crd_no = b.account and a.tx_cd in ('40','43') and a.inac_date > '" + last_inac_date + "' ";


            //rtn = dc.executeUpdate(" truncate table odsb_crd_crt_trad ");
            rtn = dc.executeUpdate(" delete from  odsb_crd_crt_trad ");
            rtn = dc.executeUpdate(sql);
            if (rtn < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("��ȡODSB����������ʧ�ܣ�������������ݿ����ӡ�");
//                this.res.setMessage(PropertyManager.getProperty("300"));
                return -1;
            }

            //������Ϣ��
            //TODO ��BIN�Ĵ���
            sql = " insert into odsb_crd_crt  " +
                    " select * from  odsbdata.BF_AGT_CRD_CRT@odsb_remote  " +
                    " where crd_no like '6283660015%' ";

            int crd_rtn = 0;
            dc.executeUpdate(" truncate table odsb_crd_crt ");
            crd_rtn = dc.executeUpdate(sql);
            if (crd_rtn < 0) {
                this.res.setType(0);
                this.res.setResult(false);
                this.res.setMessage("��ȡODSB����������ʧ�ܣ�������������ݿ����ӡ�");
                return -1;
            }

            //�����ر�
            sql = " select a.*,b.cardname,c.stmt_day from odsb_crd_crt_trad a, ls_cardbaseinfo b, odsb_crd_crt c " +
                    " where a.crd_no=b.account and a.crd_no=c.crd_no ";
            rs = dc.executeQuery(sql);
            LSCONSUMEINFO consume = new LSCONSUMEINFO();

            //�����ն�ν��е�����������
            count += i_last_today_lsh;
            //==============================================
            // �������ݵ���ʼ��ˮ��
            newLsh =  yymmdd + StringUtils.leftPad(String.valueOf(count), 7, '0');
            //===============================================
            while (rs.next()) {
                //��ˮ��
                count++;
                String lsh = String.valueOf(count);
                lsh = yymmdd + StringUtils.leftPad(lsh, 7, '0');
                consume.setLsh(lsh);

                consume.setAccount(rs.getString("crd_no"));
                consume.setBusidate(rs.getString("tx_day"));
                consume.setBusimoney(rs.getDouble("inac_amt"));
                consume.setBusiname(rs.getString("filler_2"));
                consume.setCardname(rs.getString("cardname"));

                //��ٻ����մ���
                String stmt_day = rs.getString("stmt_day");
                String inac_day = rs.getString("inac_date");
                String limitdate = getLimitDate(inac_day, stmt_day);
                consume.setLimitdate(limitdate);

                consume.setRemark("��ODSB��ʼ����");
                consume.setOperid("9999");
                consume.setOperdate(df.format(date));
                consume.setStatus("10");
                consume.setTx_cd(rs.getString("tx_cd"));
                consume.setRef_number(rs.getString("ref_nmbr"));
                consume.setCmbi_merch_no(rs.getString("cmbi_merch_no"));
                consume.setInac_date(inac_day);
                consume.setInac_amt(rs.getDouble("inac_amt"));
                consume.setTxlog("��ODSB��ʼ����");
                consume.setOdsbdate(strdate);
                consume.setOdsbtime(strtime);
                consume.setRecversion(0);

                int insertrtn = consume.insert();
                if (insertrtn < 0 ) {
                    this.res.setType(0);
                    this.res.setResult(false);
                    this.res.setMessage("����ODSB����������ʧ�ܣ���鿴ϵͳ������־��");
                    return -1;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        //���ض����¼��
        String cnt_lsh = String.valueOf(count)+"_"+newLsh;
        this.res.setFieldName("importCnt");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(cnt_lsh);
        this.res.setType(4);
        this.res.setResult(true);

        // �����������ݵ���ʼ��ˮ��
        /*this.res.setFieldName("newLsh");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(newLsh);
        this.res.setType(4);
        this.res.setResult(true);*/
        return 0;
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
}
