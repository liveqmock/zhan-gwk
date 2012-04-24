package com.ccb.init;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;

public class TrackMiscAction extends Action {
    private static final Log logger = LogFactory.getLog(TrackMiscAction.class);

    /**
     * <p/>
     * ��ʼ����ѯ
     * 1���Ա���������Ϣ���в�ѯ��ʼ״̬�����ݼ�¼��
     * 2����ѯ�쳣�����ͳ�ʱ�ͷ���ʧ�ܣ������ݼ�¼��
     * 3����ѯ������Ϣ���е���δ�����¼��
     *
     * @return
     */
    public int initQuery() {
        int count = 0;
        StringBuffer show_content = new StringBuffer();
        try {
            // ��ѯ��ʼ״̬������Ϣ��¼��
            count = queryStatusCnt("10");
            show_content.append(String.valueOf(count));
            // ��ѯ����ʧ�ܺͷ��ͳ�ʱ��������Ϣ��¼��
            count = queryStatusArrCnt(new String[]{"11", "12"});
            show_content.append("_").append(String.valueOf(count));
            // ��ѯ������Ϣ���е���δ�����¼��
            count = queryNotPaybackCnt();
            show_content.append("_").append(String.valueOf(count));
            // ��ѯδ���Ϳ�������Ϣ��¼��
            count = queryCardBaseInfos();
            show_content.append("_").append(String.valueOf(count));
            // ��ѯע����������Ϣ��¼��
            count = queryCardBaseNullInfos();
            show_content.append("_").append(String.valueOf(count));

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            this.res.setType(0);
            this.res.setResult(false);
            this.res.setMessage(PropertyManager.getProperty("300"));
            return -1;
        }

        //���س�ʼ״̬�����ݼ�¼��_�쳣�����ͳ�ʱ�ͷ���ʧ�ܣ������ݼ�¼��_��δ�����¼��
        this.res.setFieldName("importData");
        this.res.setFieldType("text");
        this.res.setEnumType("0");
        this.res.setFieldValue(new String(show_content));
        this.res.setType(4);
        this.res.setResult(true);
        return 0;
    }

    // ��ѯ����δ���ͼ�¼
    private int queryCardBaseInfos(){
        int count = 0;
        String selectsql = "select count(*) as sendCnt from ls_cardbaseinfo where status = '1' and sentflag = '0'";
        RecordSet rs = null;
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            count = rs.getInt("sendCnt");
        }
        return count;
    }
     // ��ѯ����δ���ͼ�¼
    private int queryCardBaseNullInfos(){
        int count = 0;
        String selectsql = "select count(*) as sendCnt from ls_cardbaseinfo where status = '128' and sentflag = '0'";
        RecordSet rs = null;
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            count = rs.getInt("sendCnt");
        }
        return count;
    }
    // ��ѯ����ĳһ״̬������������

    private int queryStatusCnt(String status) {
        return queryStatusArrCnt(new String[]{status});
    }

    // ��ѯ����ĳ��״̬������������

    private int queryStatusArrCnt(String[] status) {

        int count = 0;
        StringBuffer wheresqlbfr = new StringBuffer(" where status in ('");
        int statusCount = status.length;
        for (int i = 0; i < statusCount; i++) {
            wheresqlbfr.append(status[i]);
            if (i + 1 < statusCount) {
                wheresqlbfr.append("','");
            }
        }
        wheresqlbfr.append("')");
        String wheresql = new String(wheresqlbfr);
        String selectsql = "select count(*) as statusCount from ls_consumeinfo " + wheresql;
        RecordSet rs = null;
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            count = rs.getInt("statusCount");
        }
        return count;
    }
    // ��ѯ������Ϣ���е���δ�����¼��

    private int queryNotPaybackCnt() {

        int count = 0;
        String selectsql = "select count(*) as notCount from ls_paybackinfo where status = '00'";
        RecordSet rs = null;
        rs = dc.executeQuery(selectsql);
        while (rs.next()) {
            count = rs.getInt("notCount");
        }
        return count;
    }
}
