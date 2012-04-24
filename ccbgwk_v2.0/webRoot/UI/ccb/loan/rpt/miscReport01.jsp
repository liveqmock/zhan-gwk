<!--
/*********************************************************************
* ��������: �ۺϲ�ѯһͳ��һ
* �� ��:
* ��������: 2010/02/03
* �� �� ��:
* �޸�����:
* �� Ȩ:
***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK" %>
<%@page import="com.ccb.util.CcbLoanConst" %>
<%@page import="jxl.Workbook" %>
<%@page import="jxl.WorkbookSettings" %>
<%@page import="jxl.format.Alignment" %>
<%@page import="jxl.format.Border" %>
<%@page import="jxl.format.BorderLineStyle" %>
<%@page import="jxl.format.VerticalAlignment" %>
<%@page import="jxl.write.*" %>
<%@page import="jxl.write.Number" %>
<%@page import="pub.platform.advance.utils.PropertyManager" %>
<%@page import="pub.platform.db.ConnectionManager" %>
<%@page import="pub.platform.db.DatabaseConnection" %>
<%@page import="pub.platform.db.RecordSet" %>
<%@page import="pub.platform.form.config.SystemAttributeNames" %>
<%@page import="pub.platform.security.OperatorManager" %>
<%@page import="java.io.File" %>
<%@page import="java.util.Locale" %>
<%@ page import="java.math.BigDecimal" %>
<%

    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//����do while�ṹ�������������м����������˳�������

    int excelflag = 1;//excelģ���־��Ĭ��Ϊ��miscRptModel01.xls��

    try {
        do {
            // �������
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            // ----------------------------����ģ�崴�������----------------------------------------------------------------
            String rptType = "";
            rptType = request.getParameter("rptType");

            // ��������ǰ
            if (rptType.equalsIgnoreCase("miscRpt01")) {
                excelflag = 1;
                response.addHeader("Content-Disposition", "attachment; filename=" + CcbLoanConst.MISC_RPT_MODEL_01 + ".xls");
            }
            // ��������ǰ
            else {
                excelflag = 2;
                response.addHeader("Content-Disposition", "attachment; filename=" + CcbLoanConst.MISC_RPT_MODEL_02 + ".xls");
            }

            //�õ�����ģ��
            String rptModelPath = PropertyManager.getProperty("REPORT_ROOTPATH");
            String rptName = "";
            if (excelflag == 1) {
                rptName = rptModelPath + CcbLoanConst.MISC_RPT_MODEL_01 + ".xls";
            } else {
                rptName = rptModelPath + CcbLoanConst.MISC_RPT_MODEL_02 + ".xls";
            }
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
            String date = request.getParameter("MORTEXPIREDATE");
            String date2 = request.getParameter("MORTEXPIREDATE2");
            String whereSql = " where 1=1 ";
/*

    if(date!=null&&!date.equals("")){
      whereSql += " and b.MORTEXPIREDATE >='" +date+ "'";
    }
    if(date2!=null&&!date2.equals("")){
      whereSql += " and b.MORTEXPIREDATE <='" +date2+ "'";
    }
    
*/
            //20100403 zhan  �����޸�Ϊ��Ѻ������
            if (date != null && !date.equals("")) {
                whereSql += " and b.MORTDATE >='" + date + "'";
            }
            if (date2 != null && !date2.equals("")) {
                whereSql += " and b.MORTDATE <='" + date2 + "'";
            }

            DatabaseConnection conn = ConnectionManager.getInstance().get();

            String prefixSQL = " "
                    + " select (select deptname from ptdept where deptid=a.bankid)as bankid,"
                    + " c.corpname,c.proj_name,"

                    + " count((case when (b.NOMORTREASONCD='01') then 1 else null end)) as cd01,"
                    + " round(sum((case when (b.NOMORTREASONCD='01') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt01,"

                    + " count((case when (b.NOMORTREASONCD='02') then 1 else null end)) as cd02,"
                    + " round(sum((case when (b.NOMORTREASONCD='02') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt02,"

                    + " count((case when (b.NOMORTREASONCD='03') then 1 else null end)) as cd03,"
                    + " round(sum((case when (b.NOMORTREASONCD='03') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt03,"

                    + " count((case when (b.NOMORTREASONCD='04') then 1 else null end)) as cd04,"
                    + " round(sum((case when (b.NOMORTREASONCD='04') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt04,"

                    + " count((case when (b.NOMORTREASONCD='05') then 1 else null end)) as cd05,"
                    + " round(sum((case when (b.NOMORTREASONCD='05') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt05,"

                    + " count((case when (b.NOMORTREASONCD='06') then 1 else null end)) as cd06,"
                    + " round(sum((case when (b.NOMORTREASONCD='06') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt06,"

                    + " count((case when (b.NOMORTREASONCD='07') then 1 else null end)) as cd07,"
                    + " round(sum((case when (b.NOMORTREASONCD='07') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt07,"

                    + " count((case when (b.NOMORTREASONCD='08') then 1 else null end)) as cd08,"
                    + " round(sum((case when (b.NOMORTREASONCD='08') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt08,"

                    + " count((case when (b.NOMORTREASONCD='09') then 1 else null end)) as cd09,"
                    + " round(sum((case when (b.NOMORTREASONCD='09') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt09,"

                    + " count((case when (b.NOMORTREASONCD='10') then 1 else null end)) as cd10,"
                    + " round(sum((case when (b.NOMORTREASONCD='10') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt10,"

                    + " count((case when (b.NOMORTREASONCD='11') then 1 else null end)) as cd11,"
                    + " round(sum((case when (b.NOMORTREASONCD='11') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt11,"

                    + " count((case when (b.NOMORTREASONCD='12') then 1 else null end)) as cd12,"
                    + " round(sum((case when (b.NOMORTREASONCD='12') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt12,"

                    + " count((case when (b.NOMORTREASONCD='13') then 1 else null end)) as cd13,"
                    + " round(sum((case when (b.NOMORTREASONCD='13') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt13,"

                    + " count((case when (b.NOMORTREASONCD='14') then 1 else null end)) as cd14,"
                    + " round(sum((case when (b.NOMORTREASONCD='14') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt14,"

                    + " count((case when (b.NOMORTREASONCD='15') then 1 else null end)) as cd15,"
                    + " round(sum((case when (b.NOMORTREASONCD='15') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt15,"

                    + " count((case when (b.NOMORTREASONCD='16') then 1 else null end)) as cd16,"
                    + " round(sum((case when (b.NOMORTREASONCD='16') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt16,"

                    + " count((case when (b.NOMORTREASONCD='17') then 1 else null end)) as cd17,"
                    + " round(sum((case when (b.NOMORTREASONCD='17') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt17,"

                    + " count((case when (b.NOMORTREASONCD='99') then 1 else null end)) as cd99,"
                    + " round(sum((case when (b.NOMORTREASONCD='99') then a.RT_ORIG_LOAN_AMT else null end))/10000) as amt99"
//                    + " from ln_loanapply a inner join ln_mortinfo b on a.loanid=b.loanid  "
                    + " from ln_loanapply a inner join (select * from ln_mortinfo  where releasecondcd in ('03','06') and mortregstatus='1') b on a.loanid=b.loanid  "
                    + "  left join ln_coopproj c on a.proj_no= c.proj_no "
                    + whereSql
                    + " and a.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) "
                    + "  ";
//                    + " and  b.mortstatus not in('30','40','41','50') ";

            // ��������ǰ
            if (excelflag == 1) {
                prefixSQL += " group by a.bankid,c.corpname,c.proj_name";
                prefixSQL += " order by a.bankid,c.corpname,c.proj_name";
            }
            // ��������ǰ
            else {
                prefixSQL += " group by c.corpname,c.proj_name,a.bankid";
                prefixSQL += " order by c.corpname,c.proj_name,a.bankid";
            }

            // ģ����ѯƥ�����ǰ��һ��
            RecordSet rs = conn.executeQuery(prefixSQL);
            // �����¼����
            int i = 0;
            // �б����ϼ�
            int totalCnt = 0;
            // �н��ϼ�
            double totalAmt = 0;
            // �ܱ����ϼ�
            int sumCnt = 0;
            // �ܽ��ϼ�
            double sumAmt = 0;
            double totalAmt01 = 0;
            double totalAmt02 = 0;
            double totalAmt03 = 0;
            double totalAmt04 = 0;
            double totalAmt05 = 0;
            double totalAmt06 = 0;
            double totalAmt07 = 0;
            double totalAmt08 = 0;
            double totalAmt09 = 0;
            double totalAmt10 = 0;
            double totalAmt11 = 0;
            double totalAmt12 = 0;
            double totalAmt13 = 0;
            double totalAmt14 = 0;
            double totalAmt15 = 0;
            double totalAmt16 = 0;
            double totalAmt17 = 0;
            double totalAmt99 = 0;
            int totalCnt01 = 0;
            int totalCnt02 = 0;
            int totalCnt03 = 0;
            int totalCnt04 = 0;
            int totalCnt05 = 0;
            int totalCnt06 = 0;
            int totalCnt07 = 0;
            int totalCnt08 = 0;
            int totalCnt09 = 0;
            int totalCnt10 = 0;
            int totalCnt11 = 0;
            int totalCnt12 = 0;
            int totalCnt13 = 0;
            int totalCnt14 = 0;
            int totalCnt15 = 0;
            int totalCnt16 = 0;
            int totalCnt17 = 0;
            int totalCnt99 = 0;

            // ��Ԫ�����ø�ʽ���߿�
            WritableCellFormat wcf = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD, false));
            wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
            // ���ж���
            wcf.setAlignment(Alignment.LEFT);
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
            wcf.setShrinkToFit(true);
            wcf.setWrap(true);

            //�������־��Ҷ���

            NumberFormat nf = new NumberFormat("########");
            WritableCellFormat wcf_num = new WritableCellFormat(nf);
            wcf_num.setFont(new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD, false));
            wcf_num.setBorder(Border.ALL, BorderLineStyle.THIN);
            wcf_num.setAlignment(Alignment.RIGHT);
            wcf_num.setVerticalAlignment(VerticalAlignment.CENTRE);

            //�����Ҷ���
            NumberFormat nf_amt = new NumberFormat("########");
            WritableCellFormat wcf_right = new WritableCellFormat(nf_amt);
            wcf_right.setFont(new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD, false));
            wcf_right.setBorder(Border.ALL, BorderLineStyle.THIN);
            wcf_right.setAlignment(Alignment.RIGHT);
            wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE);


            // ��ʼ��
            int beginRow = 3;
            // ��ʼ��
            int beginCol = 1;
            while (rs.next()) {

                // ��ʼ������
                totalAmt = 0;
                totalCnt = 0;
                // �ӵ�4�п�ʼд����

                if (excelflag == 1) {
                    // ֧��
                    Label lbl = new Label(beginCol + 0, i + beginRow, rs.getString("bankid"), wcf);
                    ws.addCell(lbl);
                    // ¥��
                    lbl = new Label(beginCol + 1, i + beginRow, rs.getString("proj_name"), wcf);
                    ws.addCell(lbl);
                } else {
                    // ¥��
                    Label lbl = new Label(beginCol + 0, i + beginRow, rs.getString("proj_name"), wcf);
                    ws.addCell(lbl);
                    // ֧��
                    lbl = new Label(beginCol + 1, i + beginRow, rs.getString("bankid"), wcf);
                    ws.addCell(lbl);
                }

                // δ����˰
                Number label = new Number(beginCol + 2, i + beginRow, rs.getInt("cd01"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 3, i + beginRow, rs.getDouble("amt01"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd01");
                totalAmt = add(totalAmt, rs.getDouble("amt01"));
                totalCnt01 += rs.getInt("cd01");
                totalAmt01 = add(totalAmt01, rs.getDouble("amt01"));

                // ��Ҫ�������
                label = new Number(beginCol + 4, i + beginRow, rs.getInt("cd02"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 5, i + beginRow, rs.getDouble("amt02"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd02");
                totalAmt = add(totalAmt, rs.getDouble("amt02"));
                totalCnt02 += rs.getInt("cd02");
                totalAmt02 = add(totalAmt02, rs.getDouble("amt02"));

                // ����δ��Ѻ
                label = new Number(beginCol + 6, i + beginRow, rs.getInt("cd03"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 7, i + beginRow, rs.getDouble("amt03"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd03");
                totalAmt = add(totalAmt, rs.getDouble("amt03"));
                totalCnt03 += rs.getInt("cd03");
                totalAmt02 = add(totalAmt02, rs.getDouble("amt03"));

                // ���ظճ�Ѻ
                label = new Number(beginCol + 8, i + beginRow, rs.getInt("cd11"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 9, i + beginRow, rs.getDouble("amt11"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd11");
                totalAmt = add(totalAmt, rs.getDouble("amt11"));
                totalCnt11 += rs.getInt("cd11");
                totalAmt11 = add(totalAmt11, rs.getDouble("amt11"));

                // ���п�����
                label = new Number(beginCol + 10, i + beginRow, rs.getInt("cd04"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 11, i + beginRow, rs.getDouble("amt04"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd04");
                totalAmt = add(totalAmt, rs.getDouble("amt04"));
                totalCnt04 += rs.getInt("cd04");
                totalAmt04 = add(totalAmt04, rs.getDouble("amt04"));

                // �ͻ��Ƴٰ���
                label = new Number(beginCol + 12, i + beginRow, rs.getInt("cd05"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 13, i + beginRow, rs.getDouble("amt05"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd05");
                totalAmt = add(totalAmt, rs.getDouble("amt05"));
                totalCnt05 += rs.getInt("cd05");
                totalAmt05 = add(totalAmt05, rs.getDouble("amt05"));

                // �ͻ�ԤԼ��
                label = new Number(beginCol + 14, i + beginRow, rs.getInt("cd06"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 15, i + beginRow, rs.getDouble("amt06"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd06");
                totalAmt = add(totalAmt, rs.getDouble("amt06"));
                totalCnt06 += rs.getInt("cd06");
                totalAmt06 = add(totalAmt06, rs.getDouble("amt06"));

                // δ�ɵ�Ѻ��
                label = new Number(beginCol + 16, i + beginRow, rs.getInt("cd07"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 17, i + beginRow, rs.getDouble("amt07"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd07");
                totalAmt = add(totalAmt, rs.getDouble("amt07"));
                totalCnt07 += rs.getInt("cd07");
                totalAmt07 = add(totalAmt07, rs.getDouble("amt07"));

                // �����ѽ���
                label = new Number(beginCol + 18, i + beginRow, rs.getInt("cd08"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 19, i + beginRow, rs.getDouble("amt08"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd08");
                totalAmt = add(totalAmt, rs.getDouble("amt08"));
                totalCnt08 += rs.getInt("cd08");
                totalAmt08 = add(totalAmt08, rs.getDouble("amt08"));

                // ׼������
                label = new Number(beginCol + 20, i + beginRow, rs.getInt("cd09"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 21, i + beginRow, rs.getDouble("amt09"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd09");
                totalAmt = add(totalAmt, rs.getDouble("amt09"));
                totalCnt09 += rs.getInt("cd09");
                totalAmt09 = add(totalAmt09, rs.getDouble("amt09"));

                // �����ѱ���
                label = new Number(beginCol + 22, i + beginRow, rs.getInt("cd10"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 23, i + beginRow, rs.getDouble("amt10"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd10");
                totalAmt = add(totalAmt, rs.getDouble("amt10"));
                totalCnt10 += rs.getInt("cd10");
                totalAmt10 = add(totalAmt10, rs.getDouble("amt10"));

                // ��Ԥ��Ǽ�
                label = new Number(beginCol + 24, i + beginRow, rs.getInt("cd12"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 25, i + beginRow, rs.getDouble("amt12"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd12");
                totalAmt = add(totalAmt, rs.getDouble("amt12"));
                totalCnt12 += rs.getInt("cd12");
                totalAmt12 = add(totalAmt12, rs.getDouble("amt12"));

                // ���ڷ�������
                label = new Number(beginCol + 26, i + beginRow, rs.getInt("cd13"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 27, i + beginRow, rs.getDouble("amt13"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd13");
                totalAmt = add(totalAmt, rs.getDouble("amt13"));
                totalCnt13 += rs.getInt("cd13");
                totalAmt13 = add(totalAmt13, rs.getDouble("amt13"));

                // ��Ѻ��
                label = new Number(beginCol + 28, i + beginRow, rs.getInt("cd14"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 29, i + beginRow, rs.getDouble("amt14"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd14");
                totalAmt = add(totalAmt, rs.getDouble("amt14"));
                totalCnt14 += rs.getInt("cd14");
                totalAmt14 = add(totalAmt14, rs.getDouble("amt14"));

                // �ѵ�Ѻ
                label = new Number(beginCol + 30, i + beginRow, rs.getInt("cd15"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 31, i + beginRow, rs.getDouble("amt15"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd15");
                totalAmt = add(totalAmt, rs.getDouble("amt15"));
                totalCnt15 += rs.getInt("cd15");
                totalAmt15 = add(totalAmt15, rs.getDouble("amt15"));

                // δ����
                label = new Number(beginCol + 32, i + beginRow, rs.getInt("cd16"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 33, i + beginRow, rs.getDouble("amt16"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd16");
                totalAmt = add(totalAmt, rs.getDouble("amt16"));
                totalCnt16 += rs.getInt("cd16");
                totalAmt16 = add(totalAmt16, rs.getDouble("amt16"));

                // �ѳ���
                label = new Number(beginCol + 34, i + beginRow, rs.getInt("cd17"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 35, i + beginRow, rs.getDouble("amt17"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd17");
                totalAmt = add(totalAmt, rs.getDouble("amt17"));
                totalCnt17 += rs.getInt("cd17");
                totalAmt17 = add(totalAmt17, rs.getDouble("amt17"));

                // ����
                label = new Number(beginCol + 36, i + beginRow, rs.getInt("cd99"), wcf_num);
                ws.addCell(label);
                label = new Number(beginCol + 37, i + beginRow, rs.getDouble("amt99"), wcf_right);
                ws.addCell(label);
                totalCnt += rs.getInt("cd99");
                totalAmt = add(totalAmt, rs.getDouble("amt99"));
                totalCnt99 += rs.getInt("cd99");
                totalAmt99 = add(totalAmt99, rs.getDouble("amt99"));


                // ���ܱ���
                label = new Number(beginCol + 38, i + beginRow, totalCnt, wcf_num);
                ws.addCell(label);
                // ���ܽ��
                label = new Number(beginCol + 39, i + beginRow, totalAmt, wcf_right);
                ws.addCell(label);
                // �ϼ��ܱ���
                sumCnt += totalCnt;
                // �ϼ��ܽ��
                sumAmt = add(sumAmt, totalAmt);

                // �м�������1
                i++;
            }
            //------------------------- ����ϼ���--------------------------------------------------------------------------------

            wcf = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 11, WritableFont.BOLD, false));
            wcf.setBorder(Border.ALL, BorderLineStyle.THIN);
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
            wcf.setAlignment(Alignment.CENTRE);
            //���Ҷ���
            wcf_num = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 11, WritableFont.BOLD, false));
            wcf_num.setBorder(Border.ALL, BorderLineStyle.THIN);
            wcf_num.setAlignment(Alignment.RIGHT);
            wcf_num.setVerticalAlignment(VerticalAlignment.CENTRE);
            // ¥��
            Label lbl = new Label(beginCol + 0, i + beginRow, "�ϼ�", wcf);
            ws.addCell(lbl);
            // ֧��
            lbl = new Label(beginCol + 1, i + beginRow, "", wcf);
            ws.addCell(lbl);

            // δ����˰
            Number label = new Number(beginCol + 2, i + beginRow, totalCnt01, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 3, i + beginRow, totalAmt01, wcf_num);
            ws.addCell(label);
            // ��Ҫ�������
            label = new Number(beginCol + 4, i + beginRow, totalCnt02, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 5, i + beginRow, totalAmt02, wcf_num);
            ws.addCell(label);
            // ����δ��Ѻ
            label = new Number(beginCol + 6, i + beginRow, totalCnt03, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 7, i + beginRow, totalAmt03, wcf_num);
            ws.addCell(label);
            // ���ظճ�Ѻ
            label = new Number(beginCol + 8, i + beginRow, totalCnt11, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 9, i + beginRow, totalAmt11, wcf_num);
            ws.addCell(label);
            // ���п�����
            label = new Number(beginCol + 10, i + beginRow, totalCnt04, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 11, i + beginRow, totalAmt04, wcf_num);
            ws.addCell(label);
            // �ͻ��Ƴٰ���
            label = new Number(beginCol + 12, i + beginRow, totalCnt05, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 13, i + beginRow, totalAmt05, wcf_num);
            ws.addCell(label);
            // �ͻ�ԤԼ��
            label = new Number(beginCol + 14, i + beginRow, totalCnt06, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 15, i + beginRow, totalAmt06, wcf_num);
            ws.addCell(label);
            // δ�ɵ�Ѻ��
            label = new Number(beginCol + 16, i + beginRow, totalCnt07, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 17, i + beginRow, totalAmt07, wcf_num);
            ws.addCell(label);
            // �����ѽ���
            label = new Number(beginCol + 18, i + beginRow, totalCnt08, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 19, i + beginRow, totalAmt08, wcf_num);
            ws.addCell(label);
            // ׼������
            label = new Number(beginCol + 20, i + beginRow, totalCnt09, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 21, i + beginRow, totalAmt09, wcf_num);
            ws.addCell(label);
            // �����ѱ���
            label = new Number(beginCol + 22, i + beginRow, totalCnt10, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 23, i + beginRow, totalAmt10, wcf_num);
            ws.addCell(label);
            // ��Ԥ��Ǽ�
            label = new Number(beginCol + 24, i + beginRow, totalCnt12, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 25, i + beginRow, totalAmt12, wcf_num);
            ws.addCell(label);
            // ���ڷ�������
            label = new Number(beginCol + 26, i + beginRow, totalCnt13, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 27, i + beginRow, totalAmt13, wcf_num);
            ws.addCell(label);
            // ��Ѻ��
            label = new Number(beginCol + 28, i + beginRow, totalCnt14, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 29, i + beginRow, totalAmt14, wcf_num);
            ws.addCell(label);
            // �ѵ�Ѻ
            label = new Number(beginCol + 30, i + beginRow, totalCnt15, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 31, i + beginRow, totalAmt15, wcf_num);
            ws.addCell(label);
            // δ����
            label = new Number(beginCol + 32, i + beginRow, totalCnt16, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 33, i + beginRow, totalAmt16, wcf_num);
            ws.addCell(label);
            // �ѳ���
            label = new Number(beginCol + 34, i + beginRow, totalCnt17, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 35, i + beginRow, totalAmt17, wcf_num);
            ws.addCell(label);
            // ����
            label = new Number(beginCol + 36, i + beginRow, totalCnt99, wcf_num);
            ws.addCell(label);
            label = new Number(beginCol + 37, i + beginRow, totalAmt99, wcf_num);
            ws.addCell(label);
            // �ϼ����ܱ���
            label = new Number(beginCol + 38, i + beginRow, sumCnt, wcf_num);
            ws.addCell(label);
            // �ϼ����ܽ��
            label = new Number(beginCol + 39, i + beginRow, sumAmt, wcf_num);
            ws.addCell(label);
            // -------------------------------------------�ϼ��������-------------------------------------------------------------
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
<%!
    public double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();

    }
%>
