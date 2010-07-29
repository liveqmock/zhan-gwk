<!--
  /*********************************************************************
  *    ��������: Ȩ֤���ͳ��
  *    ��    ��:    
  *    ��������:  2010/02/03
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
<%@page import="jxl.format.Border"%>
<%@page import="jxl.format.BorderLineStyle"%>
<%@page import="jxl.format.Alignment"%>
<%@page import="jxl.format.VerticalAlignment"%>
<%

OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
//����do while�ṹ�������������м����������˳�������
try{
  do{
    // �������
    response.reset();
    response.setContentType("application/vnd.ms-excel");
    response.addHeader("Content-Disposition", "attachment; filename=" + CcbLoanConst.MISC_RPT_PAPER_STAT+".xls");
    // ----------------------------����ģ�崴�������----------------------------------------------------------------
    //�õ�����ģ��
    String rptModelPath = PropertyManager.getProperty("REPORT_ROOTPATH");
    String rptName = rptModelPath + CcbLoanConst.MISC_RPT_PAPER_STAT+".xls";   
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
    WritableWorkbook wwb=Workbook.createWorkbook(response.getOutputStream(),rw,setting);
    // �õ���һ��������
    WritableSheet ws = wwb.getSheet(0);
    
    // ----------------�����ݿ��ȡ����д��excel��-------------------------------------------------------------------------------- 
    // ��ѯ�ַ���
    String date = request.getParameter("MORTEXPIREDATE");
    String bankid = "";
    if(request.getParameter("bankid") != null){
        bankid = request.getParameter("bankid");
    }
    DatabaseConnection conn = ConnectionManager.getInstance().get();  
    
    String prefixSQL = " "
      +" select (select deptname from ptdept where deptid=t.deptid)as deptname,t.deptid,"
      +" sum((CASE"
      +"      WHEN t.PAPERRTNDATE <= '"+date+"' AND trim(t.PAPERRTNDATE) IS NOT NULL THEN"
      +"       1"
      +"      ELSE"
      +"       0"
      +"    END)) as paperRtn,"
      +" sum((CASE"
      +"      WHEN t.CLRPAPERDATE <= '"+date+"' AND trim(t.CLRPAPERDATE) IS NOT NULL THEN"
      +"       1"
      +"      ELSE"
      +"       0"
      +"    END)) as clsPaper,"
      +" sum((CASE"
      +"      WHEN t.CHGPAPERDATE <= '"+date+"' AND trim(t.CHGPAPERDATE) IS NOT NULL THEN"
      +"       1"
      +"      ELSE"
      +"       0"
      +"    END)) as chgPaper,"
      +" sum((CASE"
      +"      WHEN t.CHGPAPERRTNDATE <= '"+date+"' AND trim(t.CHGPAPERRTNDATE) IS NOT NULL THEN"
      +"       1"
      +"      ELSE"
      +"       0"
      +"    END)) as chgPaperRtn"
      +" FROM ln_mortinfo t"
      +" where t.deptid in(select deptid from ptdept start with deptid='"+bankid+"' connect by prior deptid=parentdeptid) "
      +" group by t.deptid ";
    
    
    // ģ����ѯƥ�����ǰ��һ��
    RecordSet rs=conn.executeQuery(prefixSQL);
    // �����¼����
    int i = 0;
    // �и� 
    int rowHeight = 500;
    // �б����ϼ�(���)
    int lagerCnt = 0;
    // �б����ϼ�
    int totalCnt01 = 0;
    int totalCnt02 = 0;
    int totalCnt03 = 0;
    int totalCnt04 = 0;
    int totalLager = 0;
    
    // ��Ԫ�����ø�ʽ���߿�
    WritableCellFormat wcf_center = new WritableCellFormat(new WritableFont(WritableFont.COURIER,11,WritableFont.NO_BOLD,false));   
    wcf_center.setBorder(Border.ALL,BorderLineStyle.THIN);
    // ���ж���
    wcf_center.setAlignment(Alignment.CENTRE);
    wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
    
    // ���Ҷ���
    WritableCellFormat wcf_right = new WritableCellFormat(new WritableFont(WritableFont.COURIER,11,WritableFont.NO_BOLD,false));   
    wcf_right.setBorder(Border.ALL,BorderLineStyle.THIN);
    // ���ж���
    wcf_right.setAlignment(Alignment.RIGHT);
    wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE);
    
    // ��ʼ��
    int beginRow = 2;
    // ��ʼ��
    int beginCol = 1;
    while(rs.next()){
      // �кϼ������ʼ��
      lagerCnt = 0;
      // �ӵ�3�п�ʼд����
      // �����и�
      ws.setRowView(i+beginRow,rowHeight,false);
      // ����
      Label lbl  = new Label(beginCol+0,i+beginRow,rs.getString("deptname"),wcf_center);
      ws.addCell(lbl);
      // ����
      lbl = new Label(beginCol+1,i+beginRow,date,wcf_center);
      ws.addCell(lbl);      
      // ��֤
      Number label  = new Number(beginCol+2,i+beginRow,rs.getInt("paperRtn"),wcf_right);
      ws.addCell(label);
      lagerCnt += rs.getInt("paperRtn");
      totalCnt01 += rs.getInt("paperRtn");
      // ����ȡ֤
      label  = new Number(beginCol+3,i+beginRow,rs.getInt("clsPaper"),wcf_right);
      ws.addCell(label);      
      lagerCnt -= rs.getInt("clsPaper");
      totalCnt02 += rs.getInt("clsPaper");
      // ��֤����
      label  = new Number(beginCol+4,i+beginRow,rs.getInt("chgPaper"),wcf_right);
      ws.addCell(label);
      lagerCnt -= rs.getInt("chgPaper");
      totalCnt03 += rs.getInt("chgPaper");
      // ��֤�黹
      label  = new Number(beginCol+5,i+beginRow,rs.getInt("chgPaperRtn"),wcf_right);
      ws.addCell(label);
      lagerCnt += rs.getInt("chgPaperRtn");
      totalCnt04 += rs.getInt("chgPaperRtn");
      
      label  = new Number(beginCol+6,i+beginRow,lagerCnt,wcf_right);
      ws.addCell(label);
      totalLager += lagerCnt;
      
      // �м�������1
      i++;
    }  
    //------------------------- ����ϼ���--------------------------------------------------------------------------------
    // ���ж���
    wcf_center = new WritableCellFormat(new WritableFont(WritableFont.COURIER,11,WritableFont.BOLD,false));
    wcf_center.setBorder(Border.ALL,BorderLineStyle.THIN);
    wcf_center.setAlignment(Alignment.CENTRE);
    wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
    // ���Ҷ���
    wcf_right = new WritableCellFormat(new WritableFont(WritableFont.COURIER,11,WritableFont.BOLD,false));   
    wcf_right.setBorder(Border.ALL,BorderLineStyle.THIN);
    // ���ж���
    wcf_right.setAlignment(Alignment.RIGHT);
    wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE);
    // �����и�
    ws.setRowView(i+beginRow,rowHeight,false);
    // ����
    Label lbl = new Label(beginCol+0,i+beginRow,"�ϼ�",wcf_center);
    ws.addCell(lbl);
    // �ո�
    lbl = new Label(beginCol+1,i+beginRow,"",wcf_center);
    ws.addCell(lbl);
    // ��֤
    Number label  = new Number(beginCol+2,i+beginRow,totalCnt01,wcf_right);
    ws.addCell(label);
    // ����ȡ֤
    label  = new Number(beginCol+3,i+beginRow,totalCnt02,wcf_right);
    ws.addCell(label);      
    // ��֤����
    label  = new Number(beginCol+4,i+beginRow,totalCnt03,wcf_right);
    ws.addCell(label);
    // ��֤�黹
    label  = new Number(beginCol+5,i+beginRow,totalCnt04,wcf_right);
    ws.addCell(label);
    // ���
    label  = new Number(beginCol+6,i+beginRow,totalLager,wcf_right);
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
  }while(false);
}catch(Exception e){
  e.printStackTrace();
} finally {
    // �ͷ����ݿ�����
    ConnectionManager.getInstance().release();
}
%>
