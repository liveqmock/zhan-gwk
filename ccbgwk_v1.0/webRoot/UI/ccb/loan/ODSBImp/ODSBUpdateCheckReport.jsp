<!--
  /*********************************************************************
  *    ��������: ODSB ��ȡ����ǰ�����
  *    ��    ��:    
  *    ��������:  2010/02/12
  *    �� �� ��:
  *    �޸�����:
  *    ��    Ȩ:    
  ***********************************************************************/
-->
<%@page contentType="text/html; charset=GBK"%>
<%@page import="pub.platform.db.ConnectionManager"%>
<%@page import="pub.platform.db.DatabaseConnection"%>
<%@page import="pub.platform.db.RecordSet"%>
<%@page import="pub.platform.utils.Basic"%>
<%@page import="com.ccb.util.*"%>
<%@page import="java.io.File"%>
<%@page import="pub.platform.advance.utils.PropertyManager"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="jxl.Workbook"%>
<%@page import="jxl.write.WritableWorkbook"%>
<%@page import="java.io.OutputStream"%>
<%@page import="jxl.write.Label"%>
<%@page import="jxl.write.WritableSheet"%>
<%@page import="jxl.write.Number"%>
<%@page import="jxl.write.WritableCellFormat"%>
<%@page import="jxl.write.WritableFont"%>
<%@page import="jxl.WorkbookSettings"%>
<%@page import="java.util.Locale"%>
<%@page import="pub.platform.security.OperatorManager"%>
<%@page import="pub.platform.form.config.SystemAttributeNames"%>
<%@page import="jxl.Cell"%>
<%@page import="jxl.format.Alignment"%>
<%@page import="jxl.format.Border"%>
<%@page import="jxl.format.VerticalAlignment"%>
<%@page import="jxl.format.BorderLineStyle"%>
<%@page import="pub.platform.db.DBUtil"%>

<%

OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

//����do while�ṹ�������������м����������˳�������
try{
  do{
    // �������
    response.reset();
    response.setContentType("application/vnd.ms-excel");
    response.addHeader("Content-Disposition", "attachment; filename=" + CcbLoanConst.RPT_ODSB_CHECK+".xls");
    // ----------------------------����ģ�崴����workbook�õ��ɲ�����writableworkbookд���������-----------------------------------
    //�õ�����ģ��
    String rptModelPath = PropertyManager.getProperty("REPORT_ROOTPATH");
    String rptName = rptModelPath + CcbLoanConst.RPT_ODSB_CHECK+".xls";   
    File file = new File(rptName);
    // �ж�ģ���Ƿ����,���������˳�
    if(!file.exists()){
      out.println(rptName+PropertyManager.getProperty("304"));
      break;
    } 
    WorkbookSettings setting = new WorkbookSettings();
    Locale locale = new Locale("zh","CN");
    setting.setLocale(locale);
    setting.setEncoding("ISO-8859-1");
    // �õ�excel��sheet
    File fileInput = new File(rptName);
    Workbook rw=Workbook.getWorkbook(fileInput,setting);
    //String targetFileName = CcbLoanConst.RPT_PAY_BILL_10+System.currentTimeMillis()+".xls";
    //String targetFile = PropertyManager.getProperty("cims")+"temp\\" + targetFileName;
    //File newFile = new File(targetFile);
    // д��jsp�е��������
    WritableWorkbook wwb=Workbook.createWorkbook(response.getOutputStream(),rw,setting);
    // �õ���һ��������
    WritableSheet ws = wwb.getSheet(0);
    
    // ----------------�����ݿ��ȡ����д��excel��-------------------------------------------------------------------------------- 
    
    DatabaseConnection conn = ConnectionManager.getInstance().get();  
    // ͳ�Ʋ�ѯ���
    // �Ƚϻ��������ͻ�����
    RecordSet rs=conn.executeQuery(""
       +" select"
       +" a.loanid,"
       +" a.CUST_NAME,"
       +" a.RT_ORIG_LOAN_AMT,"
       +" (select  deptname from ptdept where deptid= a.bankid) bankid,"
       +" b.CUST_NAME odsb_cust_name,"
       +" b.RT_ORIG_LOAN_AMT odsb_amt,"
       +" (select deptname from ptdept where deptid= b.bankid) odsb_bankid "
       +" from LN_LOANAPPLY a"
       +" inner join ln_odsb_loanapply b on a.loanid = b.loanid"
       +" where a.NEEDADDCD='1' and (trim(a.cust_name) <> trim(b.cust_name)"
       +" or a.rt_orig_loan_amt <> b.rt_orig_loan_amt"
       +" or trim(a.bankid) <> trim(b.bankid))"
       +"  " );
    // �м�����
    int i = 0;
    // ������ÿ����¼���1�У�
    int step = 1;
    int cnt = i * step;
    // --------------������ʽ--------------------
    // ---������ʽ ���ж���----    
    WritableFont NormalFont  =   new  WritableFont(WritableFont.COURIER,  11 ); 
    // ��������
    WritableCellFormat wcf_center  =   new  WritableCellFormat(NormalFont);   
	//  ����    
    wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);  
	//  ��ֱ����    
    wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);  
  	//ˮƽ���ж���
    wcf_center.setAlignment(Alignment.CENTRE);   
    wcf_center.setWrap( true );  //  �Ƿ���   
    
    // ----��ֵ��ʽ ���Ҷ���---///ͳһ��Ϊ���ж���
    WritableCellFormat wcf_right = new WritableCellFormat(new WritableFont(WritableFont.COURIER,11,WritableFont.NO_BOLD,false));  
    // �߿���ʽ
    wcf_right.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
    // ����
    wcf_right.setWrap(false);
    // ��ֱ����
    wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE);
    // ˮƽ����
    wcf_right.setAlignment(Alignment.RIGHT);
    // �и�
    int rowHeight = 500;
    // ��ʼ��
    int beginRow = 4;
    // ��ʼ��
    int beginCol = 1;
    while(rs.next()){
      cnt = i * step;
      if(i > 0){
        cnt = cnt -i;
      }
      
      String bankName = rs.getString("bankname");
   	  // �ӵ�5�п�ʼд����
   	  //--------------------------��һ��--------------------------------
   	  ws.setRowView(i+beginRow+cnt,rowHeight,false);
   	  // ��Ѻ��� 	  
   	  Label lbl = new Label(beginCol,i+beginRow+cnt,rs.getString("loanid"),wcf_center);
      ws.addCell(lbl);    
      //--- ������Ѻ ---
      // ������
      Number nLbl = new Number(beginCol+1,i+beginRow+cnt,rs.getDouble("RT_ORIG_LOAN_AMT"),wcf_right);
      ws.addCell(nLbl);
      // �ͻ���
      lbl = new Label(beginCol+2,i+beginRow+cnt,rs.getString("cust_name"),wcf_center);
      ws.addCell(lbl);
      // ����
      lbl = new Label(beginCol+3,i+beginRow+cnt,rs.getString("bankid"),wcf_center);
      ws.addCell(lbl);
      
      //---ODSB---
      // ������
      nLbl = new Number(beginCol+4,i+beginRow+cnt,rs.getDouble("odsb_amt"),wcf_right);
      ws.addCell(nLbl);
      // �ͻ���
      lbl = new Label(beginCol+5,i+beginRow+cnt,rs.getString("odsb_cust_name"),wcf_center);
      ws.addCell(lbl);
      // ����
      lbl = new Label(beginCol+6,i+beginRow+cnt,rs.getString("odsb_bankid"),wcf_center);
      ws.addCell(lbl);
      
      i++;
    }  
    
    // �ͷ����ݿ�����
    ConnectionManager.getInstance().release();
    
    //--------------------------------�ر�excel����------------------------------------------------------------------------
    // �ر�excel
    wwb.write();
    wwb.close(); 
    rw.close();
    
    //--------------------------------�������-----------------------------------------------------------------------------
    out.flush();
    out.close();
  }while(false);
}catch(Exception e){
  e.printStackTrace();
}
%>
