<!--
/*********************************************************************
* ��������: �򵥹���ͳ����Ŀ��11
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
<%@page import="jxl.Cell" %>
<%@page import="jxl.format.Alignment" %>
<%@page import="jxl.format.Border" %>
<%@page import="jxl.format.VerticalAlignment" %>
<%@page import="jxl.format.BorderLineStyle" %>
<%@page import="pub.platform.db.DBUtil" %>
<%@page import="jxl.write.NumberFormat" %>
<%

    OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
    final String RPTLABEL01 = "����ס������(��������ס����������ٽ���ס������)";
    final String RPTLABEL02 = "������ҵ����";
    final String RPTLABEL03 = "����������������������÷���������߶�������Ѷ�ȡ�����������������Ѻ����ȣ�";

//����do while�ṹ�������������м����������˳�������
    try {
        do {
            // �������
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment; filename=" + CcbLoanConst.RPT_PAY_BILL_11 + ".xls");
            // ----------------------------����ģ�崴�������----------------------------------------------------------------
            //�õ�����ģ��
            String rptModelPath = PropertyManager.getProperty("REPORT_ROOTPATH");
            String rptName = rptModelPath + CcbLoanConst.RPT_PAY_BILL_11 + ".xls";
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
            String whereStr = " where 1=1  and t.LOANSTATE is not null and t.LOANSTATE <> '0' ";
            if (!request.getParameter("CUST_OPEN_DT").trim().equals("")) {
                whereStr += " and t.CUST_OPEN_DT >='" + request.getParameter("CUST_OPEN_DT").trim() + "'";
            }
            if (!request.getParameter("CUST_OPEN_DT2").trim().equals("")) {
                whereStr += " and t.CUST_OPEN_DT <='" + request.getParameter("CUST_OPEN_DT2").trim() + "'";
            }

            String loantype1 = "t.ln_typ  in ('011','013')";
            String loantype2 = "t.ln_typ  in ('033','433')";
            String loantype3 = "t.ln_typ  in ('012','014','015','016','020','022','026','028','029','030','031','113','114','115','122')";

//      String loantype3="t.ln_typ not in ('013','011','033','117')";

            DatabaseConnection conn = ConnectionManager.getInstance().get();
            // ͳ�Ʋ�ѯ���
            RecordSet rs = conn.executeQuery(""

                    + " select t.bankid,t.custmgr_id,(select opername from ptoper where operid=t.custmgr_id) as custmgr_name,"
                    + " (select deptname from ptdept where deptid=t.bankid)as bankname,"

                    + " sum(case"
                    + "       when (" + loantype1 + " and t.RATECALEVALUE <= " + PropertyManager.getProperty("RATE_CODE_75") + ") then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt11,"

                    + " sum(case"
                    + "       when (" + loantype1 + " and t.RATECALEVALUE >" + PropertyManager.getProperty("RATE_CODE_75") + " and "
                    + " t.RATECALEVALUE<" + PropertyManager.getProperty("RATE_CODE_80") + ") then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt12,"

                    + " sum(case"
                    + "       when (" + loantype1 + " and t.RATECALEVALUE >=" + PropertyManager.getProperty("RATE_CODE_80") + " and "
                    + " t.RATECALEVALUE<" + PropertyManager.getProperty("RATE_CODE_85") + ") then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt13,"
                    + " sum(case"
                    + "       when (" + loantype1 + " and t.RATECALEVALUE >=" + PropertyManager.getProperty("RATE_CODE_85") + ") then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt14,"

                    + " sum(case"
                    + "       when (" + loantype2 + " and t.RATECALEVALUE <" + PropertyManager.getProperty("RATE_CODE_12") + ") then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt21,"

                    + " sum(case"
                    + "       when (" + loantype2 + " and t.RATECALEVALUE >=" + PropertyManager.getProperty("RATE_CODE_12") + ") then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt22,"

                    + " sum(case"
                    + "       when (" + loantype3 + " and t.RATECALEVALUE <" + PropertyManager.getProperty("RATE_CODE_10") + ") then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt31,"

                    + " sum(case"
                    + "       when (" + loantype3 + " and t.RATECALEVALUE <" + PropertyManager.getProperty("RATE_CODE_11") + " and "
                    + " t.RATECALEVALUE>=" + PropertyManager.getProperty("RATE_CODE_10") + " ) then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt32,"

                    + " sum(case"
                    + "       when (" + loantype3 + " and t.RATECALEVALUE <" + PropertyManager.getProperty("RATE_CODE_12") + " and "
                    + " t.RATECALEVALUE>=" + PropertyManager.getProperty("RATE_CODE_11") + " ) then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt33,"

                    + " sum(case"
                    + "       when (" + loantype3 + " and t.RATECALEVALUE >=" + PropertyManager.getProperty("RATE_CODE_12") + ") then"
                    + "        rt_orig_loan_amt"
                    + "       else"
                    + "        null"
                    + "     end) as amt34 "

                    + " from ln_loanapply t"
                    //+" where t.custmgr_id ='xujian.qd'"
                    + whereStr
                    + " and t.bankid in(select deptid from ptdept start with deptid='" + omgr.getOperator().getDeptid() + "' connect by prior deptid=parentdeptid) "
                    + " group by bankid,custmgr_id"
                    + " order by bankid,custmgr_id"
                    + "  ");

            // --------------������ʽ--------------------
            // ---������ʽ ���ж���----
            WritableFont NormalFont = new WritableFont(WritableFont.COURIER, 11);
            // ��������
            WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
            //  ����
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
            //  ��ֱ����
            wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
            //ˮƽ���ж���
            wcf_center.setAlignment(Alignment.CENTRE);
            wcf_center.setWrap(true);  //  �Ƿ���

            // ----��ֵ��ʽ ���Ҷ���---
            NumberFormat nf = new NumberFormat("#,###,###,##0.00");
            WritableCellFormat wcf_right = new WritableCellFormat(nf);
            wcf_right.setFont(new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD, false));
            //WritableCellFormat wcf_right = new WritableCellFormat(new WritableFont(WritableFont.COURIER,11,WritableFont.NO_BOLD,false));
            // �߿���ʽ
            wcf_right.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            // ����
            wcf_right.setWrap(false);
            // ��ֱ����
            wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE);
            // ˮƽ����
            wcf_right.setAlignment(Alignment.RIGHT);
            // �ܽ��
            double totalAmt = 0;
            // �и�
            int rowHeight = 500;
            //��ʼ��
            int beginRow = 4;
            //��ʼ��
            int beginCol = 1;
            // �����кϲ� ������
            int mergeBank = 0;
            // �м�����
            int i = 0;
            // ������ÿ����¼���9�У�
            int step = 10;
            int cnt = i * step;

            String bankName = "";
            String custName = "";

            //��ͷ
            String startdate = request.getParameter("CUST_OPEN_DT").trim();
            String enddate = request.getParameter("CUST_OPEN_DT2").trim();
            ws.setRowView(2, 600, false);

            jxl.format.CellFormat fmtPt = ws.getCell(1, 2).getCellFormat();

            startdate = startdate.substring(0, 4) + "��" + Integer.parseInt(startdate.substring(5, 7)) + "��" + Integer.parseInt(startdate.substring(8, 10)) + "��";
            enddate = enddate.substring(0, 4) + "��" + Integer.parseInt(enddate.substring(5, 7)) + "��" + Integer.parseInt(enddate.substring(8, 10)) + "��";
            Label lbl_t = new Label(1, 2, startdate + "-" + enddate, fmtPt);
            ws.addCell(lbl_t);

            while (rs.next()) {
                cnt = i * step;
                if (i > 0) {
                    cnt = cnt - i;
                }
                // �ͻ�����
                custName = rs.getString("custmgr_name");
                //һ�������ڵ�����ƫ��ֵ
                int rowoffset = 0;


                // �ӵ�4�п�ʼд����
                //--------------------------��1��--------------------------------
                ws.setRowView(i + beginRow + cnt + rowoffset, rowHeight, false);
                // ��������
                Label lbl = new Label(beginCol, i + beginRow + cnt + rowoffset, rs.getString("bankname"), wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow + cnt + rowoffset, custName, wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow + cnt + rowoffset, RPTLABEL01, wcf_center);
                ws.addCell(lbl);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow + cnt + rowoffset, "X<=" + PropertyManager.getProperty("RATE_CODE_75"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                Number nLbl = new Number(beginCol + 4, i + beginRow + cnt + rowoffset, rs.getDouble("amt11"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt11");

                rowoffset++;
                //--------------------------��2��--------------------------------
                ws.setRowView(i + beginRow + cnt + rowoffset, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow + cnt + rowoffset, rs.getString("bankname"), wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow + cnt + rowoffset, custName, wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow + cnt + rowoffset, RPTLABEL01, wcf_center);
                ws.addCell(lbl);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow + cnt + rowoffset, PropertyManager.getProperty("RATE_CODE_75")+"<X<"+PropertyManager.getProperty("RATE_CODE_80"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow + cnt + rowoffset, rs.getDouble("amt12"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt12");

                rowoffset++;
                //---------------------------��3��-------------------------------
                ws.setRowView(i + beginRow  + rowoffset + cnt, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow  + rowoffset + cnt, RPTLABEL01, wcf_center);
                ws.addCell(lbl);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow  + rowoffset + cnt, PropertyManager.getProperty("RATE_CODE_80") + "<=X<" + PropertyManager.getProperty("RATE_CODE_85"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow  + rowoffset + cnt, rs.getDouble("amt13"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt13");

                rowoffset++;
                //---------------------------��4��-------------------------------
                ws.setRowView(i + beginRow  + rowoffset + cnt, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow + rowoffset + cnt, RPTLABEL01, wcf_center);
                ws.addCell(lbl);
                // ��������ϲ�
                ws.mergeCells(beginCol + 2, i + beginRow + cnt + rowoffset -3 , beginCol + 2, i + beginRow  + rowoffset + cnt);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow  + rowoffset + cnt, "X>=" + PropertyManager.getProperty("RATE_CODE_85"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow  + rowoffset + cnt, rs.getDouble("amt14"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt14");

                rowoffset++;
                //---------------------------��5��-------------------------------
                ws.setRowView(i + beginRow  + rowoffset + cnt, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                //ws.mergeCells(beginCol,i+beginRow+cnt,beginCol,i+beginRow+3+cnt);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow  + rowoffset + cnt, RPTLABEL02, wcf_center);
                ws.addCell(lbl);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow  + rowoffset + cnt, "X<" + PropertyManager.getProperty("RATE_CODE_12"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow  + rowoffset + cnt, rs.getDouble("amt21"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt21");

                rowoffset++;
                //---------------------------��6��-------------------------------
                ws.setRowView(i + beginRow  + rowoffset + cnt, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow  + rowoffset+ cnt, RPTLABEL02, wcf_center);
                ws.addCell(lbl);
                ws.mergeCells(beginCol + 2, i + beginRow  + rowoffset - 1 + cnt, beginCol + 2, i + beginRow  + rowoffset + cnt);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow  + rowoffset + cnt, "X>=" + PropertyManager.getProperty("RATE_CODE_12"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow  + rowoffset + cnt, rs.getDouble("amt22"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt22");

                rowoffset++;
                //---------------------------��7��-------------------------------
                ws.setRowView(i + beginRow  + rowoffset + cnt, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow  + rowoffset + cnt, RPTLABEL03, wcf_center);
                ws.addCell(lbl);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow  + rowoffset + cnt, "X<" + PropertyManager.getProperty("RATE_CODE_10"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow  + rowoffset + cnt, rs.getDouble("amt31"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt31");

                rowoffset++;
                //---------------------------��8��-------------------------------
                ws.setRowView(i + beginRow  + rowoffset + cnt, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow  + rowoffset + cnt, PropertyManager.getProperty("RATE_CODE_10") + "<=X<" + PropertyManager.getProperty("RATE_CODE_11"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow  + rowoffset + cnt, rs.getDouble("amt32"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt32");


                rowoffset++;
                //---------------------------��9��-------------------------------
                ws.setRowView(i + beginRow  + rowoffset + cnt, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow  + rowoffset + cnt, PropertyManager.getProperty("RATE_CODE_11") + "<=X<" + PropertyManager.getProperty("RATE_CODE_12"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow  + rowoffset + cnt, rs.getDouble("amt33"), wcf_right);
                ws.addCell(nLbl);
                // �����ܶ�
                totalAmt += rs.getDouble("amt33");

                rowoffset++;
                //---------------------------��10��-------------------------------
                ws.setRowView(i + beginRow  + rowoffset + cnt, rowHeight, false);
                // ��������
                lbl = new Label(beginCol, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // �ͻ�����
                lbl = new Label(beginCol + 1, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // �ͻ�����ϲ�
                ws.mergeCells(beginCol + 1, i + beginRow + cnt , beginCol + 1, i + beginRow  + rowoffset + cnt);
                // ��������
                lbl = new Label(beginCol + 2, i + beginRow  + rowoffset + cnt, "", wcf_center);
                ws.addCell(lbl);
                // ��������ϲ�
                ws.mergeCells(beginCol + 2, i + beginRow  + rowoffset -3 + cnt, beginCol + 2, i + beginRow  + rowoffset + cnt);
                // ����ִ��ˮƽ
                lbl = new Label(beginCol + 3, i + beginRow + rowoffset + cnt, "X>=" + PropertyManager.getProperty("RATE_CODE_12"), wcf_center);
                ws.addCell(lbl);
                // ����Ŷ�
                nLbl = new Number(beginCol + 4, i + beginRow + rowoffset + cnt, rs.getDouble("amt34"), wcf_right);
                ws.addCell(nLbl);
                // ����������
                if (!bankName.equals(rs.getString("bankname"))) {
                    ws.mergeCells(beginCol, i + beginRow + cnt, beginCol, i + beginRow  + rowoffset + cnt);
                } else {
                    ws.mergeCells(beginCol, i + beginRow + cnt, beginCol, i + beginRow  + rowoffset + cnt);
                    //�Ѻϲ���cell�ٺϲ�,excel�ϲ���ĵ�Ԫ���������ݶ��ڣ�ֻ����ʾ���Ͻǵ�һ�����ݶ���
                }
                bankName = rs.getString("bankName");
                // �����ܶ�
                totalAmt += rs.getDouble("amt34");

                // �м�������1
                i++;
            }

            // �ͷ����ݿ�����
            //ConnectionManager.getInstance().release();
            //-----------------------------------����ܶ�-------------------------------------------------------------------------
            // ----������ʾ--
            // ����������
            WritableCellFormat wcf_bold_center = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 11, WritableFont.BOLD, false));
            wcf_bold_center.setBorder(Border.ALL, BorderLineStyle.THIN);
            wcf_bold_center.setAlignment(Alignment.CENTRE);
            wcf_bold_center.setVerticalAlignment(VerticalAlignment.CENTRE);
            // ----������ʾ--
            //WritableCellFormat wcf_bold_right = new WritableCellFormat(new WritableFont(WritableFont.COURIER,11,WritableFont.BOLD,false));
            WritableCellFormat wcf_bold_right = new WritableCellFormat(nf);
            wcf_bold_right.setFont(new WritableFont(WritableFont.COURIER, 11, WritableFont.NO_BOLD, false));
            wcf_bold_right.setBorder(Border.ALL, BorderLineStyle.THIN);
            wcf_bold_right.setAlignment(Alignment.RIGHT);
            wcf_bold_right.setVerticalAlignment(VerticalAlignment.CENTRE);
            // �����и�
            ws.setRowView(i + beginRow + 9 + cnt, rowHeight, false);
            Label lbl = new Label(beginCol, i + beginRow + 9 + cnt, "��  ��", wcf_bold_center);
            ws.addCell(lbl);
            lbl = new Label(beginCol + 1, i + beginRow + 9 + cnt, "", wcf_bold_center);
            ws.addCell(lbl);
            lbl = new Label(beginCol + 2, i + beginRow + 9 + cnt, "", wcf_bold_center);
            ws.addCell(lbl);
            lbl = new Label(beginCol + 3, i + beginRow + 9 + cnt, "", wcf_bold_center);
            ws.addCell(lbl);
            Number nLbl = new Number(beginCol + 4, i + beginRow + 9 + cnt, totalAmt, wcf_bold_right);
            ws.addCell(nLbl);
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
