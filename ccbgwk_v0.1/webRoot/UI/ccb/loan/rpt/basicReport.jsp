<!--
/*********************************************************************
* ��������: ������ѯͳ��
* �� ��:
* ��������: 2010/02/12
* �� �� ��:
* �޸�����:
* �� Ȩ:
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@page import="pub.platform.db.ConnectionManager" %>
<%@page import="pub.platform.db.DatabaseConnection" %>
<%@page import="pub.platform.db.RecordSet" %>
<%@page import="pub.platform.utils.Basic" %>
<%@page import="com.ccb.util.*" %>
<%@page import="java.io.File" %>
<%@page import="pub.platform.advance.utils.PropertyManager" %>
<%@page import="java.io.InputStream" %>
<%@page import="java.io.FileInputStream" %>
<%@page import="jxl.Workbook" %>
<%@page import="jxl.write.WritableWorkbook" %>
<%@page import="java.io.OutputStream" %>
<%@page import="jxl.write.Label" %>
<%@page import="jxl.write.WritableSheet" %>
<%@page import="jxl.write.Number" %>
<%@page import="jxl.write.WritableCellFormat" %>
<%@page import="jxl.write.WritableFont" %>
<%@page import="jxl.WorkbookSettings" %>
<%@page import="java.util.Locale" %>
<%@page import="pub.platform.security.OperatorManager" %>
<%@page import="pub.platform.form.config.SystemAttributeNames" %>
<%@page import="jxl.format.Alignment" %>
<%@page import="jxl.format.VerticalAlignment"%>
<%

    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//����do while�ṹ�������������м����������˳�������
    try {
        do {
            // �������
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment; filename=" + CcbLoanConst.BASIC_RPT_MODEL + ".xls");
            // ----------------------------����ģ��----------------------------------------------------------------
            //�õ�����ģ��
            String rptModelPath = PropertyManager.getProperty("REPORT_ROOTPATH");
            String rptName = rptModelPath + CcbLoanConst.BASIC_RPT_MODEL + ".xls";
            File file = new File(rptName);
            // �ж�ģ���Ƿ����,���������˳�
            if (!file.exists()) {
                out.println(rptName + PropertyManager.getProperty("304"));
                break;
            }
            WorkbookSettings setting = new WorkbookSettings();
            Locale locale = new Locale("zh", "CN");
            setting.setLocale(locale);
            setting.setEncoding("ISO-8859-1");
            // �õ�excel��sheet
            File fileInput = new File(rptName);
            Workbook rw = Workbook.getWorkbook(fileInput, setting);
            WritableWorkbook wwb = Workbook.createWorkbook(response.getOutputStream(), rw, setting);
            // �õ���һ��������
            WritableSheet ws = wwb.getSheet(0);

            // ----------------�����ݿ��ȡ����д��excel��--------------------------------------------------------------------------------
            // ��ѯ�ַ���
            String whereStr = " where 1=1 ";
            if (!request.getParameter("MORTDATE").equals("")) {
                whereStr += " and b.MORTDATE >='" + request.getParameter("MORTDATE") + "'";
            }
            if (!request.getParameter("MORTDATE2").equals("")) {
                whereStr += " and b.MORTDATE <='" + request.getParameter("MORTDATE2") + "'";
            }
            if (!request.getParameter("EXPRESSENDSDATE").equals("")) {
                whereStr += " and b.EXPRESSENDSDATE >='" + request.getParameter("EXPRESSENDSDATE") + "'";
            }
            if (!request.getParameter("EXPRESSENDSDATE2").equals("")) {
                whereStr += " and b.EXPRESSENDSDATE <='" + request.getParameter("EXPRESSENDSDATE2") + "'";
            }
            if (!request.getParameter("PAPERRTNDATE").equals("")) {
                whereStr += " and b.PAPERRTNDATE >='" + request.getParameter("PAPERRTNDATE") + "'";
            }
            if (!request.getParameter("PAPERRTNDATE2").equals("")) {
                whereStr += " and b.PAPERRTNDATE <='" + request.getParameter("PAPERRTNDATE2") + "'";
            }
            // ȡ�û�ִ����
            if (!request.getParameter("RECEIPTDATE").equals("")) {
                whereStr += " and b.RECEIPTDATE >='" + request.getParameter("RECEIPTDATE") + "'";
            }
            if (!request.getParameter("RECEIPTDATE2").equals("")) {
                whereStr += " and b.RECEIPTDATE <='" + request.getParameter("RECEIPTDATE2") + "'";
            }

            if (!request.getParameter("CLRPAPERDATE").equals("")) {
                whereStr += " and b.CLRPAPERDATE >='" + request.getParameter("CLRPAPERDATE") + "'";
            }
            if (!request.getParameter("CLRPAPERDATE2").equals("")) {
                whereStr += " and b.CLRPAPERDATE <='" + request.getParameter("CLRPAPERDATE2") + "'";
            }
            if (!request.getParameter("RELEASECONDCD").equals("")) {
                whereStr += " and b.RELEASECONDCD ='" + request.getParameter("RELEASECONDCD") + "'";
            }
            if (!request.getParameter("MORTOVERRTNDATE").equals("")) {
                whereStr += " and b.MORTOVERRTNDATE >='" + request.getParameter("MORTOVERRTNDATE") + "'";
            }
            if (!request.getParameter("MORTOVERRTNDATE2").equals("")) {
                whereStr += " and b.MORTOVERRTNDATE <='" + request.getParameter("MORTOVERRTNDATE2") + "'";
            }
            if (request.getParameter("NOMORTREASONCD").equals("1")) {
                whereStr += " and trim(b.NOMORTREASONCD) is not null ";
            }else if(request.getParameter("NOMORTREASONCD").equals("0")){
                whereStr += " and trim(b.NOMORTREASONCD) is  null ";
            }
            if (!request.getParameter("MORTREGSTATUS").equals("")) {
                whereStr += " and b.MORTREGSTATUS ='" + request.getParameter("MORTREGSTATUS") + "'";
            }
            
            // ��ݻ�֤������ʼ
            if (!request.getParameter("EXPRESSRTNDATE").equals("")) {
                whereStr += " and b.EXPRESSRTNDATE >='" + request.getParameter("EXPRESSRTNDATE") + "'";
            }
            if (!request.getParameter("EXPRESSRTNDATE2").equals("")) {
                whereStr += " and b.EXPRESSRTNDATE <='" +request.getParameter("EXPRESSRTNDATE2") + "'";
            }
            // ��֤����
            if (!request.getParameter("CHGPAPERDATE").equals("")) {
                whereStr += " and b.CHGPAPERDATE >='" + request.getParameter("CHGPAPERDATE")+ "'";
            }
            if (!request.getParameter("CHGPAPERDATE2").equals("")) {
                whereStr += " and b.CHGPAPERDATE <='" + request.getParameter("CHGPAPERDATE2") + "'";
            }
            // ��֤�黹
            if (!request.getParameter("CHGPAPERRTNDATE").equals("")) {
                whereStr += " and b.CHGPAPERRTNDATE >='" + request.getParameter("CHGPAPERRTNDATE") + "'";
            }
            if (!request.getParameter("CHGPAPERRTNDATE2").equals("")) {
                whereStr += " and b.CHGPAPERRTNDATE <='" + request.getParameter("CHGPAPERRTNDATE2") + "'";
            }
            
            DatabaseConnection conn = ConnectionManager.getInstance().get();
            // ģ����ѯƥ�����ǰ��һ��
            RecordSet rs = conn.executeQuery(""
                    // ������Ϣ
                    + "select a.nbxh,"
//        +"a.bankid,"
                    + "(select deptname from ptdept where deptid=a.bankid) as bankname,"
                    + "a.cust_name,"
//        +"a.ln_typ,"
                    + "(select code_desc from ln_odsb_code_desc where code_type_id='053' and code_id=a.LN_TYP) as ln_typ_name,"
                    + "a.RT_ORIG_LOAN_AMT,a.RT_TERM_INCR,a.RATECALEVALUE,a.CUST_OPEN_DT,a.EXPIRING_DT,"
                    + "(select code_desc from ln_odsb_code_desc where code_type_id='807' and code_id = a.GUARANTY_TYPE)as GUARANTY_TYPE,"
                    + "a.PROJ_NO,"
                    + "(select proj_name from ln_coopproj where proj_no=a.proj_no)as proj_name,"
                    + "(select CORPNAME from ln_coopproj where proj_no=a.proj_no)as CORPNAME,"
                    + "(select opername from ptoper where operid=a.operid)as opername,"
                    //��Ѻ��Ϣ
                    + "b.MORTDATE,b.MORTID,"
                    + "(select enuitemlabel from ptenudetail where enutype='MORTECENTERCD' and enuitemvalue=b.MORTECENTERCD)as MORTECENTERCD,"
                    + "(select enuitemlabel from ptenudetail where enutype='KEEPCONT' and enuitemvalue=b.KEEPCONT)as keepcont,"
                    + "b.EXPRESSNO,b.EXPRESSENDSDATE,b.EXPRESSNOTE,"
                    + "(select enuitemlabel from ptenudetail where enutype='RELAYFLAG' and enuitemvalue=b.RELAYFLAG) as RELAYFLAG,"
                    + "(select enuitemlabel from ptenudetail where enutype='SENDFLAG' and enuitemvalue=b.SENDFLAG) as SENDFLAG,"
                    + "(select enuitemlabel from ptenudetail where enutype='NOMORTREASONCD' and enuitemvalue=b.NOMORTREASONCD) as NOMORTREASONCD,"
                    + "(select enuitemlabel from ptenudetail where enutype='MORTREGSTATUS' and enuitemvalue=b.MORTREGSTATUS) as MORTREGSTATUS,"
                    + "b.MORTEXPIREDATE,b.MORTOVERRTNDATE,b.PAPERRTNDATE,b.CHGPAPERDATE,b.CHGPAPERRTNDATE,b.CLRPAPERDATE,expressrtndate,"
                    + "(select enuitemlabel from ptenudetail where enutype='CHGPAPERREASONCD' and enuitemvalue=b.CHGPAPERREASONCD) as CHGPAPERREASONCD "
                    //+"from ln_loanapply a left join ln_mortinfo b on a.loanid=b.loanid   "
                    //+"where exists (select 1 from ln_mortinfo  where loanid=a.loanid)  " ;
//        +" (select deptname from ptdept where deptid=a.deptid) as deptname "
                    + " from ln_loanapply a inner join ln_mortinfo b on a.loanid=b.loanid   "
                    + whereStr
                    + " and a.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) "
                    + " order by a.bankid,a.cust_name ");
            int i = 0;
            // ��Ԫ����ʽ
            WritableCellFormat wcf = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD, false));
            wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            wcf.setAlignment(Alignment.LEFT);
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
            wcf.setWrap(false);

            //���Ҷ���
            WritableCellFormat wcf_right = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD, false));
            wcf_right.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            wcf_right.setAlignment(Alignment.RIGHT);
            wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE);
            wcf_right.setWrap(false);
            //��ʼ��
            int beginRow = 2;
            //��ʼ��
            int beginCol = 1;
            int k = 0;
            while (rs.next()) {
                // �ӵ�2�п�ʼд����
/*
                // �����д���
                Label label = new Label(beginCol+k, i + beginRow, rs.getString("bankid"), wcf);
                ws.addCell(label);
                k++;
*/

                // ����������
               Label label = new Label(beginCol+k, i + beginRow, rs.getString("bankname"), wcf);
                ws.addCell(label);
                k++;
                // ���������
                label = new Label(beginCol+k, i + beginRow, rs.getString("cust_name"), wcf);
                ws.addCell(label);
                k++;
/*
                // �����������
                label = new Label(beginCol+k, i + beginRow, rs.getString("ln_typ"), wcf);
                ws.addCell(label);
                k++;
*/
                // ��������
                label = new Label(beginCol+k, i + beginRow, rs.getString("ln_typ_name"), wcf);
                ws.addCell(label);
                k++;
                // ������
                label = new Label(beginCol+k, i + beginRow, rs.getString("RT_ORIG_LOAN_AMT"), wcf_right);
                ws.addCell(label);
                k++;
                // ��������
                label = new Label(beginCol+k, i + beginRow, rs.getString("RT_TERM_INCR"), wcf_right);
                ws.addCell(label);
                k++;
                // ���ʸ�������
                label = new Label(beginCol+k, i + beginRow, rs.getString("RATECALEVALUE"), wcf_right);
                ws.addCell(label);
                k++;
                // ��������
                label = new Label(beginCol+k, i + beginRow, rs.getString("CUST_OPEN_DT"), wcf);
                ws.addCell(label);
                k++;
                // ��������
                label = new Label(beginCol+k, i + beginRow, rs.getString("EXPIRING_DT"), wcf);
                ws.addCell(label);
                k++;
                // ������ʽ
                label = new Label(beginCol+k, i + beginRow, rs.getString("GUARANTY_TYPE"), wcf);
                ws.addCell(label);
                k++;
                // �������ֵ
                label = new Label(beginCol+k, i + beginRow, rs.getString("RT_TERM_INCR"), wcf_right);
                ws.addCell(label);
                k++;
                // ������Ŀ���
                label = new Label(beginCol+k, i + beginRow, rs.getString("PROJ_NO"), wcf);
                ws.addCell(label);
                k++;
                // ������Ŀ����
                label = new Label(beginCol+k, i + beginRow, rs.getString("proj_name"), wcf);
                ws.addCell(label);
                k++;
                // ����������
                label = new Label(beginCol+k, i + beginRow, rs.getString("CORPNAME"), wcf);
                ws.addCell(label);
                k++;
                // �������
                label = new Label(beginCol+k, i + beginRow, rs.getString("opername"), wcf);
                ws.addCell(label);
                k++;
                // ��Ѻ��������
                label = new Label(beginCol+k, i + beginRow, rs.getString("MORTDATE"), wcf);
                ws.addCell(label);
                k++;
                // ��Ѻ���
                label = new Label(beginCol+k, i + beginRow, rs.getString("MORTID"), wcf);
                ws.addCell(label);
                k++;
                // ��Ѻ��������
                label = new Label(beginCol+k, i + beginRow, rs.getString("MORTECENTERCD"), wcf);
                ws.addCell(label);
                k++;
                // ��������
                label = new Label(beginCol+k, i + beginRow, rs.getString("KEEPCONT"), wcf);
                ws.addCell(label);
                k++;
                // ��ݱ��
                label = new Label(beginCol+k, i + beginRow, rs.getString("EXPRESSNO"), wcf);
                ws.addCell(label);
                k++;
                // ��ݷ�������
                label = new Label(beginCol+k, i + beginRow, rs.getString("EXPRESSENDSDATE"), wcf);
                ws.addCell(label);
                k++;
                // ��ݱ�ע
                label = new Label(beginCol+k, i + beginRow, rs.getString("EXPRESSNOTE"), wcf);
                ws.addCell(label);
                k++;
                // ���ɱ���Ѻ���Ͻ��ӱ�־
                label = new Label(beginCol+k, i + beginRow, rs.getString("RELAYFLAG"), wcf);
                ws.addCell(label);
                k++;
                // �ɱ���Ѻ��־
                label = new Label(beginCol+k, i + beginRow, rs.getString("SENDFLAG"), wcf);
                ws.addCell(label);
                k++;
                // δ�����Ѻԭ��
                label = new Label(beginCol+k, i + beginRow, rs.getString("NOMORTREASONCD"), wcf);
                ws.addCell(label);
                k++;
                // ��Ѻ�Ǽ�״̬
                label = new Label(beginCol+k, i + beginRow, rs.getString("MORTREGSTATUS"), wcf);
                ws.addCell(label);
                k++;
                // ��Ѻ������
                label = new Label(beginCol+k, i + beginRow, rs.getString("MORTEXPIREDATE"), wcf);
                ws.addCell(label);
                k++;
                // ��Ѻ����������
                label = new Label(beginCol+k, i + beginRow, rs.getString("MORTOVERRTNDATE"), wcf);
                ws.addCell(label);
                k++;
                // �������
                label = new Label(beginCol+k, i + beginRow, rs.getString("PAPERRTNDATE"), wcf);
                ws.addCell(label);
                k++;
                // ��֤��������
                label = new Label(beginCol+k, i + beginRow, rs.getString("CHGPAPERDATE"), wcf);
                ws.addCell(label);
                k++;
                // ��֤�黹����
                label = new Label(beginCol+k, i + beginRow, rs.getString("CHGPAPERRTNDATE"), wcf);
                ws.addCell(label);
                k++;
                // ����ȡ֤����
                label = new Label(beginCol+k, i + beginRow, rs.getString("CLRPAPERDATE"), wcf);
                ws.addCell(label);
                k++;
                // ��ݻ�֤����
                label = new Label(beginCol+k, i + beginRow, rs.getString("expressrtndate"), wcf);
                ws.addCell(label);
                k++;
                // ��֤ԭ��
                label = new Label(beginCol+k, i + beginRow, rs.getString("CHGPAPERREASONCD"), wcf);
                ws.addCell(label);
                k++;
/*

                // ����
                label = new Label(beginCol+k, i + beginRow, rs.getString("deptname"), wcf);
                ws.addCell(label);
*/

                k=0;
                // �м�������1
                i++;
            }
            //--------------------------------�ر�excel����------------------------------------------------------------------------
            // �ر�excel
            wwb.write();
            wwb.close();
            rw.close();

            //--------------------------------�������-----------------------------------------------------------------------------
            // �������
            out.flush();
            out.close();
        } while (false);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // �ͷ����ݿ�����
        ConnectionManager.getInstance().release();
    }
%>
