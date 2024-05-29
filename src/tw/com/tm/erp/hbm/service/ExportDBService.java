package tw.com.tm.erp.hbm.service;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportDBService {
	
	//TODO : 將xls的config 下載
	public void exportXlsConfig(OutputStream output){
		  HSSFWorkbook wb=new HSSFWorkbook();
		  HSSFSheet sheet1=wb.createSheet("sheet1");
		  HSSFRow row=sheet1.createRow((short)0);
		  HSSFCell cell=row.createCell((short)0);
		  cell.setCellValue(1);
		  
		  row.createCell((short)1).setCellValue(2);
		  row.createCell((short)2).setCellValue(3);
		  row.createCell((short)3).setCellValue("中文字符");
		  
		  
		  row=sheet1.createRow((short)1);
		  cell=row.createCell((short)0);
		  cell.setCellValue(1);
		  
		  row.createCell((short)1).setCellValue(2);
		  row.createCell((short)2).setCellValue(3);
		  row.createCell((short)3).setCellValue("中文字符");
		  
		  //FileOutputStream fileout=new FileOutputStream("workbook.xls");
		  
		  try   {
		         output.flush();
		         wb.write(output);
		         output.close();
		 }   catch   (IOException   e)   {
		         e.printStackTrace();
		         System.out.println( "Output   is   closed ");
		 } 
	}

}
