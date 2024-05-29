/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA : 
 * PG : Weichun.Liao
 * Filename : ImItemOnHandViewDataInfo.java
 * Function : Excel匯出，提供使用Native SQL或是View取回資料，
 * 			     在沒有bean的狀況，直接將物件陣列按standard_ie.properties定義的欄位順序，產生出匯出檔案的資料。
 * 
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	Jun 11, 2010	Weichun.Liao	Create
 *--------------------------------------------------------------------------------------- 
 */
package tw.com.tm.erp.standardie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class SelectDataInfo extends HDDataInfo {

	private static final long serialVersionUID = 3094571049834759982L;

	public SelectDataInfo(Object headData, Object lineData) {
		setHeadData(headData);
		setLineData(lineData);
	}

	/**
	 * 組成Excel的Data
	 * 
	 * @param HashMap
	 * @return List
	 * @throws Exception
	 */
	public List generateOutputData(HashMap essentialInfoMap) throws Exception {

		List assembly = null;
		Object lineData = getLineData();
		String fileType = (String) essentialInfoMap.get("fileType");
		if (lineData != null) {
			String[] fieldArray = (String[]) essentialInfoMap.get("field");
			List<Object[]> selectDataList = (List<Object[]>) lineData;
			if (selectDataList.size() > 0 && fieldArray.length != ((Object[]) selectDataList.get(0)).length)
				throw new ValidationErrorException("檔案定義的欄位數量與資料庫回傳欄位數量不符，請檢查！");
			if("XLS".equals(fileType)){
				if (fieldArray != null && fieldArray.length > 0 && selectDataList != null && selectDataList.size() > 0) {
					assembly = new ArrayList(0);
					for (int i = 0; i < selectDataList.size(); i++) {
						Object[] obj = (Object[]) selectDataList.get(i);
						List rowData = new ArrayList(0);
						for (int j = 0; j < fieldArray.length; j++) {
							String actualValue = null;
							if (obj[j] != null) {
								actualValue = obj[j].toString();
							}
							rowData.add(actualValue);
						}
						assembly.add(rowData);
					}
				}
			}else if("TXT".equals(fileType)){
				//分隔符號，需設定於standard_ie.properties
				String delimiter = (String)essentialInfoMap.get("delimiter");
				//空白補左補右，需設定於standard_ie.properties
				String[] align = (String[])essentialInfoMap.get("align");
				//欄位長度，需設定於standard_ie.properties
				String[] fieldLength = (String[])essentialInfoMap.get("fieldLength");
				
				if (StringUtils.hasText(delimiter)) {
					if (fieldArray != null && fieldArray.length > 0 && selectDataList != null && selectDataList.size() > 0) {
						if (fieldArray != null && fieldArray.length > 0 && selectDataList != null && selectDataList.size() > 0) {
							assembly = new ArrayList(0);
							for (int i = 0; i < selectDataList.size(); i++) {
								Object[] obj = (Object[]) selectDataList.get(i);
								StringBuffer rowData = new StringBuffer();
								for (int j = 0; j < fieldArray.length; j++) {
									StringBuffer actualValue = new StringBuffer();
									if (obj[j] != null) 
										actualValue.append(obj[j]);
									actualValue.append(delimiter);
									rowData.append(actualValue.toString().trim());
								}
								assembly.add(rowData.toString());
							}
						}
					}
				}else if(null !=  align && null !=  fieldLength ){
					if (fieldArray != null && fieldArray.length > 0 && selectDataList != null && selectDataList.size() > 0) {
						if (fieldArray != null && fieldArray.length > 0 && selectDataList != null && selectDataList.size() > 0) {
							assembly = new ArrayList(0);
							for (int i = 0; i < selectDataList.size(); i++) {
								Object[] obj = (Object[]) selectDataList.get(i);
								StringBuffer rowData = new StringBuffer();
								for (int j = 0; j < fieldArray.length; j++) {
									int fieldLengthInt = NumberUtils.getInt(fieldLength[j]);
									StringBuffer actualValue = new StringBuffer();
									if (obj[j] != null) 
										actualValue.append(obj[j]);
									rowData.append(CommonUtils.insertCharacterWithLimitedLength(actualValue.toString(), fieldLengthInt, fieldLengthInt, CommonUtils.SPACE, align[j]));
								}
								assembly.add(rowData.toString());
							}
						}
					}
				}else{
					throw new ValidationErrorException("查無匯出設定資訊！(需要分隔符號 or 欄位長度、靠左靠右、填補符號資訊)");
				}
			}
		}
		return assembly;
	}
}
