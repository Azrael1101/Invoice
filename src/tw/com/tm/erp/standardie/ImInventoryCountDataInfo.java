package tw.com.tm.erp.standardie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsHead;
import tw.com.tm.erp.hbm.bean.ImInventoryCountsLine;

public class ImInventoryCountDataInfo extends HDDataInfo {

    /**
     * 
     */
    private static final long serialVersionUID = 1223464826135673843L;

    public ImInventoryCountDataInfo(Object headData, Object lineData) {
	setHeadData(headData);
	setLineData(lineData);
    }

    public List generateOutputData(HashMap essentialInfoMap) throws Exception {
	
	String beanName = (String)essentialInfoMap.get("beanName");
	String delimiter = (String)essentialInfoMap.get("delimiter");	
	if(!StringUtils.hasText(delimiter)){
	    throw new ValidationErrorException(beanName + "的分隔符號設定資訊為空值！");
	}
	
	List assembly = null;
	Object headData = getHeadData();
	Object lineData = getLineData();
	StringBuffer rowData = null;	
	if(headData != null){
	    assembly = new ArrayList(0);
	    rowData = new StringBuffer();
	    //head
	    ImInventoryCountsHead  inventoryCountsHead= (ImInventoryCountsHead)headData;
	    String brandCode = inventoryCountsHead.getBrandCode();
	    String orderTypeCode = inventoryCountsHead.getOrderTypeCode();
	    String orderNo = inventoryCountsHead.getOrderNo();
	    rowData.append(ImInventoryCountDataInfo.MASTERSYMBOL);
	    rowData.append(delimiter);
	    rowData.append(brandCode);
	    rowData.append(delimiter);
	    rowData.append(orderTypeCode);
	    rowData.append(delimiter);
	    rowData.append(orderNo);
	    assembly.add(rowData.toString());
	    
	    //line	    
	    if(lineData != null){
		List inventoryCountsLines = (List)lineData;
		for(int i = 0; i < inventoryCountsLines.size(); i++){
		    rowData = new StringBuffer();
		    ImInventoryCountsLine inventoryCountsLine = (ImInventoryCountsLine)inventoryCountsLines.get(i);
		    String itemCode = inventoryCountsLine.getItemCode();
		    Double onHandQty = inventoryCountsLine.getOnHandQty();
		    
		    rowData.append(ImInventoryCountDataInfo.DETAILSYMBOL);
		    rowData.append(delimiter);
		    rowData.append(itemCode);
		    rowData.append(delimiter);
		    rowData.append(onHandQty);
		    rowData.append(delimiter);
		    rowData.append(0D);
		    assembly.add(rowData.toString());
		}
	    }
	}

	return assembly;
    }
}
