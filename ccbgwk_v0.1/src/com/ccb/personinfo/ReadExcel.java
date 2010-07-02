package com.ccb.personinfo;


import com.ccb.dao.LSPERSONALINFO;
import com.ccb.util.SeqUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import pub.platform.db.ConnectionManager;
import pub.platform.db.DatabaseConnection;
import pub.platform.db.RecordSet;
import pub.platform.form.control.Action;
import pub.platform.utils.BusinessDate;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-6-23
 * Time: 16:53:25
 * To change this template use File | Settings | File Templates.
 */
public class ReadExcel extends Action {

    public String getData(String path) {
        String perName = "";         //����
        String perID = "";           //���֤
        String deptCD = "";          //Ԥ�㵥λ
        int version = 0;             //�汾��
        int createCD = 0;            //��½��
        String superDeptCD = "";     //һ��Ԥ�㵥λ
        String cfPerIDs = "";   //�ظ����֤ID
        try {
            LSPERSONALINFO perInfo = new LSPERSONALINFO();
            InputStream input = new FileInputStream(path);
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            cfPerIDs = getErrorData(sheet);
            if (!cfPerIDs.equals(""))
                return cfPerIDs;
            else {
                int rowLen = sheet.getLastRowNum();
                HSSFCell cell0 = null;              //����
                HSSFCell cell1 = null;              //���֤ID
                HSSFCell cell2 = null;              //Ԥ�㵥λ
                HSSFCell cell3 = null;              //�汾
                HSSFCell cell4 = null;              //������code
                HSSFCell cell5 = null;              //һ��Ԥ�㵥λ
                //���ݲ���
                for (int i = 1; i <= rowLen; i++) {
                    //����
                    cell0 = sheet.getRow(i).getCell(0);
                    switch (cell0.getCellType()) {
                        case HSSFCell.CELL_TYPE_STRING:
                            perName = cell0.getStringCellValue().trim();
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            perName = Integer.toString((int) (cell0.getNumericCellValue()));
                            break;
                        default:
                            perName = null;
                            break;
                    }
                    //���֤ID
                    cell1 = sheet.getRow(i).getCell(1);
                    switch (cell1.getCellType()) {
                        case HSSFCell.CELL_TYPE_STRING:
                            perID = cell1.getStringCellValue().trim();
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            perID = String.valueOf((int) (cell1.getNumericCellValue()));
                            break;
                        default:
                            perID = "";
                            break;
                    }
                    //Ԥ�㵥λ
                    cell2 = sheet.getRow(i).getCell(2);
                    deptCD = cell2.getStringCellValue().trim();
                    //������CD
                    cell3 = sheet.getRow(i).getCell(3);
                    createCD = (int) (cell3.getNumericCellValue());
                    //һ��Ԥ�㵥λ
                    cell4 = sheet.getRow(i).getCell(4);
                    switch (cell4.getCellType()) {
                        case HSSFCell.CELL_TYPE_STRING:
                            superDeptCD = cell4.getStringCellValue().trim();
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            superDeptCD = String.valueOf((int) (cell4.getNumericCellValue()));
                            break;
                        default:
                            superDeptCD = "";
                            break;
                    }
                    //������������
                    perInfo.setPername(perName);
                    perInfo.setPerid(perID);
                    perInfo.setDeptcode(deptCD);
                    perInfo.setRecversion(version);
                    perInfo.setCreatecode(createCD);
                    perInfo.setSuperdeptcode(superDeptCD);
                    //�ڲ����к�ȡ��
                    String nbxlh = SeqUtil.getNbxh();
                    perInfo.setRecinsequence(nbxlh);
                    //��ǰʱ���趨Ϊ����ʱ��
                    perInfo.setCreatedate(BusinessDate.getTodaytime());
                    if (perInfo.insert() < 0)
                        return "-1";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "-1";
        }
        return "0";
    }

    /*����������֤
    * ��֤1�����֤�ظ�
    * ��֤2�����֤λ��У��??
    * */

    public String getErrorData(HSSFSheet sheet) {
        String perIDLst = "";    //�ظ����֤��
        String perID = "";       //���֤ID
        int rowLen = sheet.getLastRowNum();
        HSSFCell cell = null;
        //��֤�ظ����֤ID���У�ȡ����
        for (int i = 1; i <= rowLen; i++) {
            cell = sheet.getRow(i).getCell(1);
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    perID = cell.getStringCellValue().trim();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    perID = String.valueOf((int) (cell.getNumericCellValue()));
                    break;
                default:
                    perID = "";
                    break;
            }
            DatabaseConnection cn = ConnectionManager.getInstance().get();
            String sqlStr = "select 1 from ls_personalinfo t where t.perid='" + perID + "'";
            RecordSet rec = null;
            rec = cn.executeQuery(sqlStr);
            if (rec.getRecordCount() != 0) {
                if (perIDLst.equals(""))
                    perIDLst = perID;
                else
                    perIDLst = perIDLst + "," + perID;
            }
//            while (rec.next()) {
//                if (perIDLst.equals("")) {
//                    perIDLst = perID;
//                }
//                else
//                    perIDLst = perIDLst + "," + perID;
//            }
            if (rec != null) {
                rec.close();
            }
        }
        return perIDLst;
    }
}
