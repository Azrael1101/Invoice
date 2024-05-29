package tw.com.tm.erp.standardie;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.StandardIEUtils;
import tw.com.tm.erp.hbm.view.*;

public class BarcodeDataInfo extends DataInfo {

	private static final Log log = LogFactory.getLog(BarcodeDataInfo.class);

	@Override
	public List generateOutputData(HashMap essentialInfoMap) throws Exception {
		// TODO Auto-generated method stub

		String beanName = (String) essentialInfoMap.get("beanName");
		String[] fieldArray = (String[]) essentialInfoMap.get("field");
		String[] fieldNotDisplayArray = (String[]) essentialInfoMap.get("fieldNotDisplay");
		String delimiter = (String) essentialInfoMap.get("delimiter");
		String endSymbol = (String) essentialInfoMap.get("endSymbol");
		List entityBeans = (List) getLineData();
//		SortedMap map = new TreeMap();
//		for (int i = 0; i < entityBeans.size(); i++) {
//			Class getClass = entityBeans.get(i).getClass();
//			Object obj = entityBeans.get(i);
//			Method showData = getClass.getMethod("getItemCode", new Class[0]);
//			Object obj2 = showData.invoke(obj, new Object[0]);
//			map.put(obj2.toString(), entityBeans.get(i));
//		}
//		entityBeans = Arrays.asList(map.values().toArray());
		if (fieldArray == null) {
			throw new ValidationErrorException(beanName + "的欄位設定資訊為空值！");
		} else if (!StringUtils.hasText(delimiter)) {
			throw new ValidationErrorException(beanName + "的分隔符號設定資訊為空值！");
		}
		if (fieldNotDisplayArray != null && fieldNotDisplayArray.length > 0) {
			fieldArray = StandardIEUtils.getActualField(fieldNotDisplayArray, fieldArray);
		}

		List assembly = null;
		if (fieldArray != null && fieldArray.length > 0 && entityBeans != null && entityBeans.size() > 0) {
			assembly = new ArrayList(0);
			for (int i = 0; i < entityBeans.size(); i++) {
				Object obj = entityBeans.get(i);
				StringBuffer rowData = new StringBuffer();
				for (int j = 0; j < fieldArray.length; j++) {
					Object vo = PropertyUtils.getProperty(obj, fieldArray[j]);

					StringBuffer actualValue = new StringBuffer();
					if (vo != null) {
						if (vo instanceof Date) {
							actualValue.append(DateUtils.format((Date) vo));
						} else {
							String voStr = vo.toString();

							if (vo instanceof Double) {
								try {
									int intNum = ((Double) vo).intValue();
									DecimalFormat df = new DecimalFormat("###,###,###");
									if (!"quantity".equalsIgnoreCase(fieldArray[j]) && 
										!"deliveryQuantity".equalsIgnoreCase(fieldArray[j])) {
										voStr = df.format(intNum);
									} else {
										voStr = String.valueOf(intNum);
									}
								} catch (Exception e) {
									log.error("executeTxtStandardParsing isNoDot:", e);
								}
							}

							actualValue.append(voStr);
						}
					}
					if (j != (fieldArray.length - 1) || (endSymbol != null && "Y".equalsIgnoreCase(endSymbol))) {
						actualValue.append(delimiter);
					}
					rowData.append(actualValue.toString().trim());
				}

				assembly.add(rowData.toString());
			}
		}
		return assembly;
	}

}
