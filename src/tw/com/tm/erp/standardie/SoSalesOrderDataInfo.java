package tw.com.tm.erp.standardie;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.utils.DateUtils;

public class SoSalesOrderDataInfo extends DataInfo {

    public SoSalesOrderDataInfo(Object lineData) {
	setLineData(lineData);
    }

    public List generateOutputData(HashMap essentialInfoMap) throws Exception {

	List assembly = null;
	Object lineData = getLineData();
	
	if (lineData != null) {
	    String[] fieldArray = (String[]) essentialInfoMap.get("field");
	    List<SoSalesOrderItem> soSalesOrderItems = (List<SoSalesOrderItem>) lineData;
	    if (fieldArray != null && fieldArray.length > 0
		    && soSalesOrderItems != null
		    && soSalesOrderItems.size() > 0) {
		assembly = new ArrayList(0);
		for (int i = 0; i < soSalesOrderItems.size(); i++) {
		    Object obj = soSalesOrderItems.get(i);
		    List rowData = new ArrayList(0);
		    for (int j = 0; j < fieldArray.length; j++) {
			Object vo = PropertyUtils.getProperty(obj,
				fieldArray[j]);
			String actualValue = null;
			if (vo != null) {
			    if (vo instanceof Date) {
				actualValue = DateUtils.format((Date) vo);
			    } else {
				actualValue = vo.toString();
			    }
			}
			rowData.add(actualValue);
		    }
		    assembly.add(rowData);
		}
	    }
	}

	return assembly;
    }
}
