package tw.com.tm.erp.utils;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jxl.Cell;
import jxl.CellView;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;

public class ExcelBeanUtil {

	private static final Log log = LogFactory.getLog(ExcelBeanUtil.class);

	private static final String FIELD = "_FIELD";

	private static final String COMMENT = "_COMMENT";

	private static final int TITLE_ROWS = 1;

	private static final String ORIG_ENCODING = "ISO-8859-1";

	private static final String DESTN_ENCODING = "UTF-8";

	public static void exportExcel(OutputStream os, HashMap essentialInfoMap) throws Exception {
	    System.out.println("exportExcel");
	    WritableWorkbook wbook = null;
	    try {
		String[] commentArray = (String[]) essentialInfoMap.get("comment");
		String sheetName = (String) essentialInfoMap.get("sheetName");
		String function = "";
		List assembly = (List) essentialInfoMap.get("assembly");
		String[] pattermArray = null;
		if (essentialInfoMap.get("patterm") != null)
		    pattermArray = (String[]) essentialInfoMap.get("patterm");
		if(essentialInfoMap.get("function")!= null)
			 function = (String)essentialInfoMap.get("function");
		WritableCellFormat titleFormat;
		WritableCellFormat contentFormat;
		if(function.equals("onePage"))
		{
			log.info("字體大小設定12");
			titleFormat= getTitleFormat(12);
			contentFormat= getContentFormat(12);
		}
		else
		{
			log.info("預設字體大小設定");
			titleFormat= getTitleFormat();
			contentFormat= getContentFormat();
		}
		CellView cellView = new CellView();
		// 建立文件
		wbook = Workbook.createWorkbook(os);
		WritableSheet wsheet = wbook.createSheet(new String(sheetName.getBytes(ORIG_ENCODING), DESTN_ENCODING), 0);
		wsheet.getSettings().setVerticalFreeze(1);
		
		//判斷是否為不產生表頭
		//是的話按照Standard產生
		//不是的話按照RESLT的第一排產生
		if(null != assembly && null != essentialInfoMap.get("noComment") && "Y".equals(essentialInfoMap.get("noComment"))){
		    System.out.println("noComment");
		    List rowList = (List) assembly.get(0);
		    for (int j = 0; j < rowList.size(); j++) {
			Object vo = rowList.get(j);
			if(null != vo){
			    wsheet.addCell(new Label(j, 0, vo.toString(), titleFormat));
			    wsheet.setColumnView(j, 20);
			    cellView.setAutosize(true); // 自動調整欄寬
				wsheet.setColumnView(j, cellView);
			}
		    }
		}else{
		    System.out.println("Comment");
		    for (int j = 0; j < commentArray.length; j++) {
			wsheet.addCell(new Label(j, 0, new String(commentArray[j].getBytes(ORIG_ENCODING), DESTN_ENCODING), titleFormat));
			wsheet.setColumnView(j, 20);
			cellView.setAutosize(true); // 自動調整欄寬
			wsheet.setColumnView(j, cellView);
		    }    
		}

		int rowIndex = 0;

		if (null != assembly) {
		    // 設定寫入數字的型式，取到小數點以下X位，若小於1要顯示0，預設為整數
		    NumberFormat nf = new NumberFormat("#,###,###,##0");
		    WritableCellFormat wcfN = null;

		    for (int k = 0; k < assembly.size(); k++) {

			//如果是自定義 因為第一排已經使用 這邊跳過
			if(k == 0 && null != essentialInfoMap.get("noComment") && "Y".equals(essentialInfoMap.get("noComment")))
			    continue;

			rowIndex++;
			List rowList = (List) assembly.get(k);

			for (int m = 0; m < rowList.size(); m++) {
			    Object vo = rowList.get(m);
			    String actualExportValue = null;
			    if (vo != null) {
				if (pattermArray != null) {
				    if (pattermArray[m].toString().indexOf("Number") > -1) {
					if (pattermArray[m].toString().length() > 6) { // Number後面有數字，代表小數點幾位
					    int decimal_place = Integer.parseInt(pattermArray[m].toString().substring(6)); 
					    StringBuilder sb = new StringBuilder("#,###,###,##0.");
					    for (int i = 0; i < decimal_place; i++) {
						sb.append("0");
					    }
					    nf = new NumberFormat(sb.toString());
					}else{
					    nf = new NumberFormat("#,###,###,##0");
					}
					wcfN = new WritableCellFormat(nf);
					double d = Double.parseDouble(vo.toString());
					jxl.write.Number labelNF = new jxl.write.Number(m, rowIndex, d, wcfN);
					wsheet.addCell(labelNF);
				    } else if (pattermArray[m].toString().indexOf("Date") > -1) {
					Date voDate = DateUtils.parseDate(DateUtils.C_DATE_PATTON_DEFAULT, vo.toString());
					actualExportValue = DateUtils.format(voDate, DateUtils.C_DATA_PATTON_YYYYMMDD);
					wsheet.addCell(new Label(m, rowIndex, actualExportValue, contentFormat));
				    } else if (pattermArray[m].toString().indexOf("Status") > -1) {
					actualExportValue = OrderStatus.getChineseWord(vo.toString());
					wsheet.addCell(new Label(m, rowIndex, actualExportValue, contentFormat));
				    }else {
					actualExportValue = vo.toString();
					wsheet.addCell(new Label(m, rowIndex, actualExportValue, contentFormat));
				    }
				} else {
				    actualExportValue = vo.toString();
				    wsheet.addCell(new Label(m, rowIndex, actualExportValue, contentFormat));
				}
			    }
			}
		    }
		    
		}

		if(function.equals("onePage"))
		{


			log.info("設定整頁模式");
			wsheet.getSettings().setFitToPages(true);
			wsheet.getSettings().setFitWidth(1);
			
			
		}
		wbook.write();
	    } catch (Exception ex) {
		log.error("匯出XLS檔失敗！原因：" + ex.toString());
		throw ex;
	    } finally {
		if (wbook != null)
		    wbook.close();
	    }
	}

	public static List importExcel(HashMap essentialInfoMap, byte[] buf) throws Exception {
	    System.out.println("importExcel");
	    try {
		String[] classNameArray = (String[]) essentialInfoMap.get("className");
		String[] fieldArray = (String[]) essentialInfoMap.get("field");
		String className = (String) classNameArray[0];
		String[] extraInfoArray = (String[]) essentialInfoMap.get("extraInfoArray");

		ByteArrayInputStream inputStream = new ByteArrayInputStream(buf);
		Workbook book = Workbook.getWorkbook(inputStream);
		Sheet sheet = book.getSheet(0);
		int rows = sheet.getRows();
		List entityBeanList = new ArrayList(0);
		if (extraInfoArray[0] != null && extraInfoArray[1] != null) {
		    ApplicationContext context = SpringUtils.getApplicationContext();
		    Object processObj = context.getBean(extraInfoArray[0]);
		    entityBeanList = (List) MethodUtils.invokeMethod(processObj, extraInfoArray[1], buf);
		} else {
		    for (int i = TITLE_ROWS; i < rows; i++) {
			Class clsTask = Class.forName(className);
			Object entityBean = clsTask.newInstance();
			int spaceFields = 0;
			//迴圈不超過欄位寬度
			for (int j = 0; j < sheet.getColumns(); j++) {
			    Cell cell = sheet.getCell(j, i);
			    Object contentValue = cell.getContents();
			    if (contentValue != null && StringUtils.hasText(contentValue.toString())) {
				try {
				    Field field = clsTask.getDeclaredField(fieldArray[j]);
				    Class fieldTypeClass = field.getType();
				    if (fieldTypeClass == Date.class) {
					contentValue = DateUtils.parseDate(contentValue.toString());
				    } else if (fieldTypeClass == Double.class) {
					NumberCell nc = (NumberCell) cell;
					contentValue = nc.getValue();
				    } else {
					contentValue = contentValue.toString().trim();
				    }
				} catch (Exception e) {

				}
				BeanUtils.setProperty(entityBean, fieldArray[j], contentValue);
			    } else {
				spaceFields++;
			    }
			}
			if (spaceFields < sheet.getColumns()) {
			    entityBeanList.add(entityBean);
			}
		    }
		}
		return entityBeanList;
	    } catch (Exception ex) {
		log.error("匯入XLS檔失敗！原因：" + ex.toString());
		throw ex;
	    }
	}

	private static WritableCellFormat getTitleFormat() throws Exception {

		WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

		WritableCellFormat format = new WritableCellFormat(titleFont);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.BOTTOM);
		return format;
	}
	private static WritableCellFormat getTitleFormat(int size) throws Exception {

		WritableFont titleFont = new WritableFont(WritableFont.createFont("微軟正黑體"), size, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

		WritableCellFormat format = new WritableCellFormat(titleFont);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.BOTTOM);
		return format;
	}
	private static WritableCellFormat getCommentFormat() throws Exception {

		WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.RED);

		WritableCellFormat format = new WritableCellFormat(titleFont);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.BOTTOM);
		return format;
	}
	private static WritableCellFormat getContentFormat(int size) throws Exception {

		WritableFont contentFont = new WritableFont(WritableFont.createFont("微軟正黑體"), size, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		WritableCellFormat format = new WritableCellFormat(contentFont);
		// format.setWrap(true);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.BOTTOM);
		return format;
	}
	private static WritableCellFormat getContentFormat() throws Exception {

		WritableFont contentFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		WritableCellFormat format = new WritableCellFormat(contentFont);
		// format.setWrap(true);
		format.setAlignment(Alignment.CENTRE);
		format.setVerticalAlignment(VerticalAlignment.BOTTOM);
		return format;
	}
	
}