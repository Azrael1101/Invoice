package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;

import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.MachineSale;
import tw.com.tm.erp.hbm.bean.PosMachineSale;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.MachineSaleDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class MachineSaleService {
    private ImItemCategoryDAO imItemCategoryDAO;
    private ImItemDAO imItemDAO;
    private MachineSaleDAO machineSaleDAO;
    private PosExportDAO posExportDAO;
    private NativeQueryDAO nativeQueryDAO;
    
    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
        this.nativeQueryDAO = nativeQueryDAO;
    }

    public void setPosExportDAO(PosExportDAO posExportDAO) {
        this.posExportDAO = posExportDAO;
    }

    public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
	    this.imItemCategoryDAO = imItemCategoryDAO;
	}
    
    public void setImItemDAO(ImItemDAO imItemDAO) {
	    this.imItemDAO = imItemDAO;
	}
    
    public void setMachineSaleDAO(MachineSaleDAO machineSaleDAO){
	this.machineSaleDAO = machineSaleDAO;
    	}
    
    public static final String[] GRID_SEARCH_MACHINE_SALE_FIELD_NAMES = { 
		"category01", "classId", "itemBrand",
		"itemCode", "machineCode", "lastUpdateBy",
		"lastUpdateDate"
	};
	
	public static final String[] GRID_SEARCH_MACHINE_SALE_FIELD_DEFAULT_VALUES = { 
		"","","",
		"","","",
		""
	};
	
	public static final String[] GRID_SEARCH_UNLOCKED_ITEM_FIELD_NAMES = { 
		"category01", "category02", "itemBrand",
		"itemCode", "lastUpdatedBy",
		"lastUpdateDate"
	};
	
	public static final String[] GRID_SEARCH_UNLOCKED_ITEM_FIELD_DEFAULT_VALUES = { 
		"","","",
		"","",""
	};
	
	public static final String[] GRID_SEARCH_CATEGORY02_FIELD_NAMES = { 
		"parentCategoryCode", "categoryName","lastUpdatedBy","lastUpdateDate","id.categoryCode"
	};
	
	public static final String[] GRID_SEARCH_CATEGORY02_FIELD_DEFAULT_VALUES = { 
		"","","","",""
	};
	
	public static final String[] GRID_SEARCH_CATEGORY_FIELD_NAMES = { 
		"category01", "category02", "itemBrand"
	};
	
	public static final String[] GRID_SEARCH_CATEGORY_FIELD_DEFAULT_VALUES = { 
		"","",""
	
	};
	public static final String[] GRID_SEARCH_ITEM_BRAND_FIELD_NAMES = { 
		"category01", "category02", "itemBrand",
		"itemCode", "lastUpdatedBy",
		"lastUpdateDate","itemCode"
	};
	
	public static final String[] GRID_SEARCH_ITEM_BRAND_FIELD_DEFAULT_VALUES = { 
		"","","",
		"","","",""
	};
    public Map executeInitial(Map parameterMap) throws Exception{
        HashMap resultMap    = new HashMap();
        Object otherBean     = parameterMap.get("vatBeanOther");
        String brandCode     = (String)PropertyUtils.getProperty(otherBean, "loginBrandCode");
	List<ImItemCategory> result = imItemCategoryDAO.findByCategoryType(brandCode, "CATEGORY01");
	resultMap.put("category01", AjaxUtils.produceSelectorData(result, "categoryCode", "categoryName", true, true ));
	return resultMap;
    }

    public List<Properties> getAJAXItemBrand(Properties httpRequest) throws Exception{
	Properties properties = new Properties();
	List returnList = new ArrayList();
	String category = httpRequest.getProperty("category");
//	 imItemDAO.findByCategory02(category);
	 List itemBrandList = new ArrayList();
	 List defaultList = new ArrayList();
	 defaultList.add("");
	 defaultList.add(" ");
	 defaultList.add(true);
	 itemBrandList.add(defaultList);
	 List selectList = imItemDAO.findItemBrandListByCategory02(category);
	 itemBrandList.add(selectList);
	 itemBrandList.add(selectList);
//	itemBrandList = AjaxUtils.produceSelectorData(itemBrandList, "itemBrand", "itemBrand", false, true);
	properties.setProperty("itemBrandList", AjaxUtils.parseSelectorData(itemBrandList));
	returnList.add(properties);
	return returnList;
    }
    
    public List<Properties> getLockedSearch(Properties httpRequest) {
	try{
	    List<Properties> result = new ArrayList();
  	    List<Properties> gridDatas = new ArrayList();
  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
  	    String category01 = httpRequest.getProperty("category01");
  	    String category02 = httpRequest.getProperty("category02");
  	    String itemBrand = httpRequest.getProperty("itemBrand");
  	    String ItemCode = httpRequest.getProperty("itemCode");
  	    String machineCode = httpRequest.getProperty("posMachineCode");
  	    HashMap map = new HashMap();
  	    HashMap findObjs = new HashMap();
          	  if(!"".equals(category01)&&category01 != null){
          	    findObjs.put(" and model.category01 = :category01",category01);
          	  }
          	  if(!"".equals(category02)&&category02 != null){
          	    findObjs.put(" and model.classId = :category02",category02);
          	  }
          	  if(!"".equals(itemBrand)&&itemBrand != null){
          	    findObjs.put(" and model.itemBrand = :itemBrand",itemBrand);
          	  }
          	  if(!"".equals(ItemCode)&&ItemCode != null){
          	    findObjs.put(" and model.itemCode = :ItemCode",ItemCode);
          	  }
          	  System.out.println("=======ItemCode========:"+ItemCode);
          	String orderBy = "";
          	  if(!"".equals(machineCode)&&machineCode != null){
          	    findObjs.put(" and model.posMachineCode = :posMachineCode",machineCode);
          	    orderBy = "order by machineCode desc";
          	  }
          	  findObjs.put(" and model.status = :status", "A");
          	  
  	    Map machineSale = machineSaleDAO.search("MachineSale as model", findObjs, orderBy, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
  	    System.out.println("===machinesale==="+machineSale);
  	    List<MachineSale> machineSales = (List<MachineSale>) machineSale.get(BaseDAO.TABLE_LIST);
	    Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;   
	    Long maxIndex = (Long)machineSaleDAO.search("MachineSale as model ", "count(*) as rowCount" ,findObjs, orderBy, iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
	    System.out.println("====size====="+machineSales);
  	    if(machineSales != null && machineSales.size()>0){
        	    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_MACHINE_SALE_FIELD_NAMES, GRID_SEARCH_MACHINE_SALE_FIELD_DEFAULT_VALUES,machineSales, gridDatas, firstIndex, maxIndex));
  	    }else{
  		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_MACHINE_SALE_FIELD_NAMES, GRID_SEARCH_MACHINE_SALE_FIELD_DEFAULT_VALUES, map,gridDatas));
  	    }
	    return result;
	}catch(Exception ex){
	    System.out.println("==ex=="+ex.toString()); return null;
	}
    }
    
    public List<Properties> getUnlockedSearch(Properties httpRequest) {
	try{
	    List<Properties> result = new ArrayList();
  	    List<Properties> gridDatas = new ArrayList();
  	    HashMap findObjs = new HashMap();
  	    HashMap map = new HashMap();
  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
  	    Map resultMap = new HashMap();
  	    Long firstIndex = 0L; 
  	    Long maxIndex = 0L;
  	    String category01 = httpRequest.getProperty("category01");
  	    String category02 = httpRequest.getProperty("category02");
  	    String itemBrand = httpRequest.getProperty("itemBrand");
  	    String itemCode = httpRequest.getProperty("itemCode");
  	    String brandCode = httpRequest.getProperty("brandCode");
  	    StringBuffer sql = new StringBuffer();
  	    //找該大類之下的中類
  	    if(!"".equals(category01)&&"".equals(category02)&&"".equals(itemBrand)&&"".equals(itemCode)){
  		findObjs.put(" and parentCategoryCode = :parentCategoryCode",category01);
  		findObjs.put(" and id.brandCode = :brandCode", brandCode);
  		resultMap = imItemCategoryDAO.search("ImItemCategory as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
  		List<ImItemCategory> imItems = (List<ImItemCategory>) resultMap.get(BaseDAO.TABLE_LIST);
  		firstIndex = Long.valueOf(iSPage * iPSize)+ 1;   
  		maxIndex = (Long)imItemCategoryDAO.search("ImItemCategory as model ", "count(*) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
  		if(imItems.size()>0&&imItems != null){
  		    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_CATEGORY02_FIELD_NAMES, GRID_SEARCH_CATEGORY02_FIELD_DEFAULT_VALUES,imItems, gridDatas, firstIndex, maxIndex));
  		}else{
  		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_CATEGORY02_FIELD_NAMES, GRID_SEARCH_CATEGORY02_FIELD_DEFAULT_VALUES, map,gridDatas));
  		}
  	    }else  if(!"".equals(category01)&&!"".equals(category02)&&"".equals(itemBrand)&&"".equals(itemCode)){
  		sql.append("SELECT CATEGORY01,CATEGORY02,ITEM_BRAND FROM ERP.IM_ITEM WHERE ITEM_BRAND IN (");
  		sql.append("SELECT CATEGORY_CODE FROM ERP.IM_ITEM_CATEGORY WHERE BRAND_CODE = 'T2' AND CATEGORY_TYPE = 'ItemBrand')");
  		sql.append(" AND CATEGORY01 = '"+category01+"'");
  		sql.append(" AND CATEGORY02 = '"+category02+"'");
  		sql.append(" GROUP BY CATEGORY01,CATEGORY02,ITEM_BRAND");
  		sql.append(" ORDER BY CATEGORY01,CATEGORY02,ITEM_BRAND");
  		List results = nativeQueryDAO.executeNativeSql(sql.toString(), iSPage, iPSize);
  		StringBuffer sqlMax = new StringBuffer(" select count(rownum) AS rowCount FROM (");
  		sqlMax.append(sql+" )");
  		
  		    if (null != results && results.size() > 0) {
  			
  			List maxResult = nativeQueryDAO.executeNativeSql(sqlMax.toString());
  		    	firstIndex = Long.valueOf(iSPage * iPSize) + 1; // NumberUtils.getLong(((Object[])results.get(0))[0].toString());
  		    	maxIndex = NumberUtils.getLong( ((Object) maxResult.get(maxResult.size()-1)).toString() );
  		    	result.add(AjaxUtils.getAJAXJoinTablePageData(httpRequest, GRID_SEARCH_CATEGORY_FIELD_NAMES, GRID_SEARCH_CATEGORY_FIELD_DEFAULT_VALUES, results, gridDatas,
  		    			firstIndex, maxIndex,null));

  		    } else {
  		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_CATEGORY_FIELD_NAMES, GRID_SEARCH_CATEGORY_FIELD_DEFAULT_VALUES, gridDatas));
  		    }

  	    }else if(!"".equals(category01)&&!"".equals(category02)&&!"".equals(itemBrand)&&"".equals(itemCode)){
  		findObjs.put(" and category01 = :category01",category01);
  		findObjs.put(" and category02 = :category02",category02);
  		findObjs.put(" and id.brandCode = :brandCode", brandCode);
  		findObjs.put(" and itemBrand = :itemBrand", itemBrand);
  		resultMap = imItemDAO.search("ImItem as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
  		List<ImItem> imItems = (List<ImItem>) resultMap.get(BaseDAO.TABLE_LIST);
  		firstIndex = Long.valueOf(iSPage * iPSize)+ 1;   
  		maxIndex = (Long)imItemCategoryDAO.search("ImItem as model ", "count(*) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
  		if(imItems.size()>0&&imItems != null){
  		    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_ITEM_BRAND_FIELD_NAMES, GRID_SEARCH_ITEM_BRAND_FIELD_DEFAULT_VALUES,imItems, gridDatas, firstIndex, maxIndex));
  		}else{
  		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_ITEM_BRAND_FIELD_NAMES, GRID_SEARCH_ITEM_BRAND_FIELD_DEFAULT_VALUES, map,gridDatas));
  		}
  	    }else if(!"".equals(category01)&&!"".equals(category02)&&!"".equals(itemBrand)&&!"".equals(itemCode)){
  		findObjs.put(" and category01 = :category01",category01);
  		findObjs.put(" and category02 = :category02",category02);
  		findObjs.put(" and id.brandCode = :brandCode", brandCode);
  		findObjs.put(" and itemBrand = :itemBrand", itemBrand);
  		findObjs.put(" and itemCode = :itemCode", itemCode);
  		resultMap = imItemDAO.search("ImItem as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
  		List<ImItem> imItems = (List<ImItem>) resultMap.get(BaseDAO.TABLE_LIST);
  		firstIndex = Long.valueOf(iSPage * iPSize)+ 1;   
  		maxIndex = (Long)imItemCategoryDAO.search("ImItem as model ", "count(*) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
  		if(imItems.size()>0&&imItems != null){
  		    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_ITEM_BRAND_FIELD_NAMES, GRID_SEARCH_ITEM_BRAND_FIELD_DEFAULT_VALUES,imItems, gridDatas, firstIndex, maxIndex));
  		}else{
  		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_ITEM_BRAND_FIELD_NAMES, GRID_SEARCH_ITEM_BRAND_FIELD_DEFAULT_VALUES, map,gridDatas));
  		}
  	    }else if("".equals(category01)&&"".equals(category02)&&"".equals(itemBrand)&&!"".equals(itemCode)){
  	    findObjs.put(" and id.brandCode = :brandCode", brandCode);
  	    findObjs.put(" and itemCode = :itemCode", itemCode);
  		resultMap = imItemDAO.search("ImItem as model", findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
  		List<ImItem> imItems = (List<ImItem>) resultMap.get(BaseDAO.TABLE_LIST);
  		firstIndex = Long.valueOf(iSPage * iPSize)+ 1;   
  		maxIndex = (Long)imItemCategoryDAO.search("ImItem as model ", "count(*) as rowCount" ,findObjs, "order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
  		System.out.println("vvvv!!!!="+imItems.size());
  		if(imItems.size()>0&&imItems != null){
  			System.out.println("aa");
  		    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_ITEM_BRAND_FIELD_NAMES, GRID_SEARCH_ITEM_BRAND_FIELD_DEFAULT_VALUES,imItems, gridDatas, firstIndex, maxIndex));
  		}else{
  			System.out.println("bb");
  		    result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_SEARCH_ITEM_BRAND_FIELD_NAMES, GRID_SEARCH_ITEM_BRAND_FIELD_DEFAULT_VALUES, map,gridDatas));
  		} 	    
  	    }
	    return result;
	}catch(Exception ex){
	    System.out.println("==ex=="+ex.toString()+"==="+ex.getStackTrace()); return null;
	}
    }
    
    public List<Properties> saveCategory02Result(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_CATEGORY02_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    public List<Properties> saveCategoryResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_CATEGORY_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    public List<Properties> saveItemBrandResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_ITEM_BRAND_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    
    public List<Properties> saveItemCodeResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_ITEM_BRAND_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }

    public List<Properties> saveLockedItemResult(Properties httpRequest) throws Exception{
        String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_MACHINE_SALE_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
    public void saveLockItem(HashMap parameterMap) {
	try{
	    Object formBindBean = parameterMap.get("formBindBean");
	    String lockType = (String)PropertyUtils.getProperty(formBindBean, "lockType");
	    String machineCode = (String)PropertyUtils.getProperty(formBindBean, "posMachineCode");
	    if("".equals(machineCode)){
		machineCode = "all";
	    }
	    Object otherBean     = parameterMap.get("vatBeanOther");
	    String employeeCode  = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
	    Date date = new Date();
	    //鎖定商品
	    if("A".equals(lockType)){
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
		    List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
		    
		    System.out.println("==AAAA====results==AAAA="+results);
		    	if(results != null && results.size()>0){
		    	    
		    		for(int i=0;i<results.size();i++){
		    		   MachineSale ms = new MachineSale();
		    		   List chk = new ArrayList();
		    		   String category01 = results.get(i).getProperty("parentCategoryCode");
		    		   String classId = results.get(i).getProperty("id.categoryCode");
		    		   if("".equals(category01) || category01 == null){
		    		       category01 =  results.get(i).getProperty("category01");
        		    	   }
		    		   if("".equals(classId) || classId == null){    
		    		       classId = results.get(i).getProperty("category02");
		    		   }
		    		   String itCode = results.get(i).getProperty("itemCode") == null ? "":results.get(i).getProperty("itemCode");
		    		   String itBrand = results.get(i).getProperty("itemBrand") == null ? "":results.get(i).getProperty("itemBrand");
		    		   ms.setCategory01(category01);
		    		   ms.setClassId(classId);
		    		   ms.setItemBrand(itBrand);
		    		   ms.setItemCode(itCode);
		    		   ms.setMachineCode(machineCode);
		    		   ms.setLastUpdateBy(employeeCode);
		    		   ms.setLastUpdateDate(date);
		    		   ms.setStatus("A");
//		    		   machineSaleDAO.save(ms);
		    		   chk = machineSaleDAO.checkExist(ms);
		    		   System.out.println("====chk==="+chk);
		    		   if(!(chk != null &&chk.size()>0)){
		    		       machineSaleDAO.saveLockItem(ms);
		    		   }
		    		}
		    	}

	    }else if("D".equals(lockType)){
		//解鎖商品
		System.out.println("===unlock===");
		Object pickerBean = parameterMap.get("vatBeanPicker");
		String timeScope = (String) PropertyUtils.getProperty(pickerBean, AjaxUtils.TIME_SCOPE);
		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(pickerBean, AjaxUtils.SEARCH_KEY);
		List<Properties> results = AjaxUtils.getSelectedResults(timeScope, searchKeys);
		System.out.println("==results="+results);
		if(results != null && results.size()>0){
	    		for(int i=0;i<results.size();i++){
	    		MachineSale ms = new MachineSale();
	    		String category01 = results.get(i).getProperty("category01");
	    		String classId = results.get(i).getProperty("classId");
	    		String itCode = results.get(i).getProperty("itemCode") == null ? "":results.get(i).getProperty("itemCode");
	    		String itBrand = results.get(i).getProperty("itemBrand") == null ? "":results.get(i).getProperty("itemBrand");
	    		String machineC = results.get(i).getProperty("machineCode") == null ? "":results.get(i).getProperty("machineCode");
	    		ms.setCategory01(category01);
	    		ms.setClassId(classId);
	    		ms.setItemBrand(itBrand);
	    		ms.setItemCode(itCode);
	    		ms.setMachineCode(machineC);
	    		ms.setLastUpdateBy(employeeCode);
	    		ms.setLastUpdateDate(date);
	    		ms.setStatus("A");
	    		machineSaleDAO.delete(ms);
	    		ms.setStatus("D");
	    		machineSaleDAO.save(ms);
	    		}
	    	}
	    }
	}catch(Exception ex){
	    System.out.println("===="+ex.toString());
	}
    }

    public Long executeLockItemExport(HashMap parameterMap) throws Exception{
	
	Long responseId = 0L;
	Long numbers = 0L;

	// 一、解析程式需要排程下傳或是即時下傳
	Long batchId = (Long) parameterMap.get("BATCH_ID");
	String uuId = posExportDAO.getDataId();// 產生dataId

	// 二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
	if (null == batchId || batchId <= 0) {

	    String lockDate = DateUtils.format((Date) parameterMap .get("DATA_LOCK_DATE"), DateUtils.C_DATA_PATTON_YYYYMMDD);
	    String machineCode = parameterMap .get("MACHINE_CODE") == null ? "":String.valueOf(parameterMap .get("MACHINE_CODE"));
	    List<Object[]> results = machineSaleDAO.findByCondition(lockDate,machineCode);
	    if (results != null && results.size() > 0) {
		for (Object[] result : results) {
		    
		    PosMachineSale posMachineSale = new PosMachineSale();
		    posMachineSale.setClassId(result[0] == null ? "":String.valueOf(result[0]));
		    posMachineSale.setItemBrand(result[1] == null ? "":String.valueOf(result[1]));
		    posMachineSale.setItemCode(result[2] == null ? "":String.valueOf(result[2]));
		    posMachineSale.setMachineCode(result[3] == null ? "":String.valueOf(result[3]));
		    posMachineSale.setStatus(result[4] == null ? "":String.valueOf(result[4]));
		    posMachineSale.setDataId(uuId);
		    posMachineSale.setAction("U");
		    System.out.println("========="+posMachineSale.getClassId());
		    System.out.println("========="+posMachineSale.getItemBrand());
		    System.out.println("========="+posMachineSale.getItemCode());
		    System.out.println("========="+posMachineSale.getMachineCode());
		    System.out.println("========="+posMachineSale.getStatus());
		    posExportDAO.save(posMachineSale);
		}
	    }
	} 

	// 更新新的DATA_ID做回傳
	parameterMap.put("DATA_ID", uuId);
	parameterMap.put("NUMBERS", numbers);
	responseId = posExportDAO.executeCommand(parameterMap);
	return responseId;
    }
}
