package tw.com.tm.erp.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtils {

	public static HSSFCellStyle getSimpleCellStyle(HSSFWorkbook wb) {
		HSSFCellStyle cs = wb.createCellStyle(); // 创造excel的一个样式表
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		return cs;
	}

	public static void createSheet(HSSFWorkbook wb, String data[][]) {
		HSSFSheet sheet = wb.createSheet("new sheet");
		wb.setSheetName(0, "test");
		HSSFCellStyle cs = getSimpleCellStyle(wb);
		for (int x = 0; x < data.length; x++) {
			HSSFRow row = sheet.createRow((short) (x));
			for (int y = 0; y < data[x].length; y++) {
				HSSFCell cell = row.createCell((short) y,HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(cs);
				cell.setCellValue(data[x][y]);
				//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			}
		}
	}

	public static Object getCellData(HSSFCell cell) {
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			return cell.getBooleanCellValue();
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case HSSFCell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue();

		case HSSFCell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();

		case HSSFCell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();

		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();

		}
		return null;
	}
}
