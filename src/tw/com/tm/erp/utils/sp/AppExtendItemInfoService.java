package tw.com.tm.erp.utils.sp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.TmpExtendItemInfo;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.TmpExtendItemInfoDAO;
import tw.com.tm.erp.hbm.service.SiSystemLogService;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.XFireUtils;
import tw.com.tm.erp.hbm.dao.ImSysLogDAO;
import tw.com.tm.erp.hbm.bean.ImSysLog;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
/**
 * @author T15394
 *
 */
public class AppExtendItemInfoService {
	
	private SiProgramLogAction siProgramLogAction;
	
	private static ImMovementHeadDAO imMovementHeadDAO;

    private static final Log log = LogFactory.getLog(AppExtendItemInfoService.class);

    private String[] extendItemFields = new String[]{"lotNo", "declarationNo", "declarationSeq", "declarationDate", "quantity", "declarationType", "perUnitAmount"};

    private String[] customizekeys = new String[]{"lotFieldName", "declNoFieldName", "declSeqFieldName", "declDateFieldName", "qtyFieldName", "declTypeFieldName", "perUnitAmountFieldName"};
    
    
    //Steve
	private ImSysLogDAO imSysLogDAO;
	
	private ImItemDAO imItemDAO;
	
    public void setImSysLogDAO(ImSysLogDAO imSysLogDAO) {
		this.imSysLogDAO = imSysLogDAO;
	}
    
    public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
		this.imMovementHeadDAO = imMovementHeadDAO;
	}
    
    public void setImItemDAO(ImItemDAO imItemDAO) {
    	this.imItemDAO = imItemDAO;
    }
    
    //Steve
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
    	this.siProgramLogAction = siProgramLogAction;
    }

	public List<Properties> executeExtendItem(Map parameterMap) throws Exception {
		synchronized (this) {
			Map returnMap = new HashMap(0);
			MessageBox msgBox = new MessageBox();
			try {
				HashMap conditionMap = getConditionData(parameterMap);
				String processObjectName = (String) conditionMap.get("processObjectName");
				String searchMethodName = (String) conditionMap.get("searchMethodName");
				Long searchKey = (Long) conditionMap.get("searchKey");
				String subEntityBeanName = (String) conditionMap.get("subEntityBeanName");
				Object processObj = SpringUtils.getApplicationContext().getBean(processObjectName);
				Object entityBean = MethodUtils.invokeMethod(processObj, searchMethodName, searchKey);
				if (entityBean == null) {
					throw new NoSuchObjectException("查無單據(" + searchKey + ")的資料！");
				}

				List subEntityBeans = (List) PropertyUtils.getProperty(entityBean, subEntityBeanName);
				log.info("======= 展完報單前的資料筆數： " + subEntityBeans.size() + "=======");
				if (subEntityBeans != null && subEntityBeans.size() > 0) {
					List tmpExtendItemInfoBeans = getExtendItemInfo(conditionMap);
					if (tmpExtendItemInfoBeans != null && tmpExtendItemInfoBeans.size() > 0) {
						// get bean class type
						Object subEntityBean = subEntityBeans.get(0);
						Class clsTask = subEntityBean.getClass();
						HashMap originalEntityBeansMap = produceOriginalEntityBeansMap(subEntityBeans);
						List extendEntityBeans = getAfterExtendEntityBeans(conditionMap, originalEntityBeansMap,
								tmpExtendItemInfoBeans, clsTask,searchKey);
						log.info("======= 展完報單後的資料筆數： " + extendEntityBeans.size() + "=======");
						PropertyUtils.setProperty(entityBean, subEntityBeanName, extendEntityBeans);
						BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
						MethodUtils.invokeMethod(baseDAO, "update", entityBean);
					}
				}
				log.info("=====extend item process finished=====");

			} catch (Exception ex) {
				log.info("取得商品報單資訊時發生錯誤，原因：" + ex.toString());
				log.error("取得商品報單資訊失敗，原因：" + ex.toString());
				msgBox.setMessage(ex.getMessage());
				returnMap.put("vatMessage", msgBox);
			}

			return AjaxUtils.parseReturnDataToJSON(returnMap);
		}
	}

	/**
	 * 展報單（扣除鎖住的報單庫存） by Weichun 2011.09.22
	 *
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public List<Properties> executeExtendItemWithBlock(Map parameterMap) throws Exception {
		synchronized (this) {
			Map returnMap = new HashMap(0);
			MessageBox msgBox = new MessageBox();
			ImMovementHead imh = null;
			try {
				
				HashMap conditionMap = getConditionData(parameterMap);
				String processObjectName = (String) conditionMap.get("processObjectName");
				String searchMethodName = (String) conditionMap.get("searchMethodName");
				Long searchKey = (Long) conditionMap.get("searchKey");
				imh = imMovementHeadDAO.findById(searchKey);
				String subEntityBeanName = (String) conditionMap.get("subEntityBeanName");
				Object processObj = SpringUtils.getApplicationContext().getBean(processObjectName);
				Object entityBean = MethodUtils.invokeMethod(processObj, searchMethodName, searchKey);
				if (entityBean == null) {
					throw new NoSuchObjectException("查無單據(" + searchKey + ")的資料！");
				}

				List subEntityBeans = (List) PropertyUtils.getProperty(entityBean, subEntityBeanName);
                System.out.println("======= 展完報單前的資料筆數： " + subEntityBeans.size() + "=======");
				if (subEntityBeans != null && subEntityBeans.size() > 0) {
					List tmpExtendItemInfoBeans = getExtendItemInfoWithBlock(conditionMap);
					if (tmpExtendItemInfoBeans != null && tmpExtendItemInfoBeans.size() > 0) {
						// get bean class type
						Object subEntityBean = subEntityBeans.get(0);
						Class clsTask = subEntityBean.getClass();
						HashMap originalEntityBeansMap = produceOriginalEntityBeansMap(subEntityBeans);
						List extendEntityBeans = getAfterExtendEntityBeans(conditionMap, originalEntityBeansMap,
								tmpExtendItemInfoBeans, clsTask,searchKey);
						System.out.println("======== 展完報單後的資料筆數： " + extendEntityBeans.size() + "=======");
						PropertyUtils.setProperty(entityBean, subEntityBeanName, extendEntityBeans);
						BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
						MethodUtils.invokeMethod(baseDAO, "update", entityBean);
						
						
					}
				}
				log.info("=====extend item process finished=====");

			} catch (Exception ex) {
				log.info("取得商品報單資訊時發生錯誤，原因：" + ex.toString());
				log.error("取得商品報單資訊失敗，原因：" + ex.toString());
				msgBox.setMessage(ex.getMessage());
				returnMap.put("vatMessage", msgBox);
			}

			return AjaxUtils.parseReturnDataToJSON(returnMap);
		}
	}

	/**
	 * 取得展完報單資訊後的bean
	 *
	 * @param conditionMap
	 * @param originalEntityBeansMap
	 * @param tmpExtendItemInfoBeans
	 * @param cls
	 * @return List
	 * @throws Exception
	 */
	public List getAfterExtendEntityBeans(HashMap conditionMap, HashMap originalEntityBeansMap, List tmpExtendItemInfoBeans,
			Class cls,Long searchKey) throws Exception {

		List extendEntityBeans = new ArrayList(0);
		String customizeItemValue = (String) conditionMap.get("itemFieldName");
		String customizeWarehouseValue = (String) conditionMap.get("warehouseCodeFieldName");
		String itemField = (StringUtils.hasText(customizeItemValue)) ? customizeItemValue : "itemCode";
		String warehouseField = (StringUtils.hasText(customizeWarehouseValue)) ? customizeWarehouseValue : "warehouseCode";
		ImMovementHead imh = null;
		
		System.out.println("tmpExtendItemInfoBeans.size() ::: " + tmpExtendItemInfoBeans.size());
		for (int i = 0; i < tmpExtendItemInfoBeans.size(); i++) {
			TmpExtendItemInfo extendItemInfo = (TmpExtendItemInfo) tmpExtendItemInfoBeans.get(i);
			Long originalLineId = extendItemInfo.getOriginalLineId();
			Object subEntityBean = originalEntityBeansMap.get(originalLineId);
			Object customizeObj = cls.newInstance();
			// originalLineId所對應的bean非null時執行copy
			if (subEntityBean != null) {
				BeanUtils.copyProperties(subEntityBean, customizeObj);
			} else {
				PropertyUtils.setProperty(customizeObj, itemField, PropertyUtils.getProperty(extendItemInfo, "itemCode"));
				PropertyUtils.setProperty(customizeObj, warehouseField, PropertyUtils.getProperty(extendItemInfo,
						"warehouseCode"));
			}
			// 將TmpExtendItemInfo的資訊copy至新的bean
			for (int j = 0; j < customizekeys.length; j++) {				
				String customizeValue = (String) conditionMap.get(customizekeys[j]);
				if (StringUtils.hasText(customizeValue)) {
					PropertyUtils.setProperty(customizeObj, customizeValue,
							PropertyUtils.getProperty(extendItemInfo, extendItemFields[j]));
				}
			}
						
//			System.out.println(i + ".originalQuantity ::: " + PropertyUtils.getProperty(extendItemInfo, "originalQuantity"));
//			System.out.println(i + ".quantity ::: " + PropertyUtils.getProperty(extendItemInfo, "quantity"));
//			
//			System.out.println(i + ".originalDeclarationNo ::: " + PropertyUtils.getProperty(extendItemInfo, "originalDeclarationNo"));
//			System.out.println(i + ".originalDeclarationSeq ::: " + PropertyUtils.getProperty(extendItemInfo, "originalDeclarationSeq"));
//			System.out.println(i + ".warehouseCode ::: " + PropertyUtils.getProperty(extendItemInfo, "warehouseCode"));
//			System.out.println(i + ".declarationNo ::: " + PropertyUtils.getProperty(extendItemInfo, "declarationNo"));
//			System.out.println(i + ".declarationSeq ::: " + PropertyUtils.getProperty(extendItemInfo, "declarationSeq"));
//			System.out.println(i + ".perUnitAmount ::: " + PropertyUtils.getProperty(extendItemInfo, "perUnitAmount"));
			
			Double originalQuantity = Double.parseDouble(String.valueOf(PropertyUtils.getProperty(extendItemInfo, "originalQuantity")));
			Double quantity = Double.parseDouble(String.valueOf(PropertyUtils.getProperty(extendItemInfo, "quantity")));
			//System.out.println("class name =====> " + cls.getName());
			if (cls.getName().equals("tw.com.tm.erp.hbm.bean.ImAdjustmentLine")) {
				tw.com.tm.erp.hbm.bean.ImAdjustmentLine imAdjustmentLine = (tw.com.tm.erp.hbm.bean.ImAdjustmentLine) customizeObj;
				//System.out.println("amount ::: " + imAdjustmentLine.getAmount());
				// 調整單點選核銷報單時，成本按報單數量比例分配 add by Weichun 2012.03.06
				imAdjustmentLine.setAmount(imAdjustmentLine.getAmount() * quantity / originalQuantity);
				customizeObj = imAdjustmentLine;
			}
			PropertyUtils.setProperty(customizeObj, "lineId", null);
			PropertyUtils.setProperty(customizeObj, "indexNo", null);
			extendEntityBeans.add(customizeObj);
			//Steve 展報單 LOG Start

			if (cls.getName().equals("tw.com.tm.erp.hbm.bean.ImMovementHead")) {
				imh = imMovementHeadDAO.findById(searchKey);
				String itemId = extendItemInfo.getItemCode();
    			System.out.println("Babu1:"+itemId);
    			ImItem imItem = imItemDAO.findById(itemId);
    			System.out.println("Babu2:"+imItem.getItemCName());
    			ImSysLog ims = new ImSysLog();
    			ims.setHeadId(searchKey);
    			ims.setAction("展報單");
    			ims.setCreate_date(new Date());
    			ims.setCreated_by(imh.getCreatedBy());
    			ims.setIdentification(imh.getBrandCode()+imh.getOrderTypeCode()+imh.getOrderNo());
    			ims.setMessage("品名:"+imItem.getItemCName());//imItem.getItemCName()
    			ims.setModule_name("Im_Movement");
    			imSysLogDAO.save(ims);
			}
            //Steve 展報單 LOG End
		}
		return extendEntityBeans;
	}


	/**
	 * 產生原實體資料的對應
	 *
	 * @param subEntityBeans
	 * @return
	 * @throws Exception
	 */
	private HashMap produceOriginalEntityBeansMap(List subEntityBeans) throws Exception {

		HashMap map = new HashMap();
		ImItem imItem = null;
		MessageBox msgBox = new MessageBox();
		StringBuffer errorString = new StringBuffer("");
		String className = "";
		System.out.println("==== 展報單前原實體資料的對應筆數 ::: " + subEntityBeans.size() + " ====");
		for (int i = 0; i < subEntityBeans.size(); i++) {
			//Object subEntityBean = subEntityBeans.get(i);
			//Object lineId = PropertyUtils.getProperty(subEntityBean, "lineId");
			//map.put(lineId, subEntityBean);
			Object subEntityBean = subEntityBeans.get(i);
			String itemCode = PropertyUtils.getProperty(subEntityBean, "itemCode").toString();
			String indexNo = PropertyUtils.getProperty(subEntityBean, "indexNo").toString();
			imItem = imItemDAO.findById(itemCode);
			className = subEntityBean.getClass().getName();
			
			Object lineId = PropertyUtils.getProperty(subEntityBean, "lineId");
			map.put(lineId, subEntityBean);
			//if("tw.com.tm.erp.hbm.bean.ImMovementItem".equals(className)){//限定為調撥單
				if(imItem==null){
					errorString.append("查無序號"+ indexNo +" 品號"+ itemCode +",無法展報單!!\n");
				}
			//}
		}
		System.out.println("==== 展報單後原實體資料的對應筆數 ::: " + subEntityBeans.size() + " ====");
		if(errorString.length()!=0){
			//msgBox.setMessage(errorString.toString());
			throw new NoSuchObjectException(errorString.toString());
		}
		map.put("vatMessage", msgBox);
		return map;
	}

    /**
     * 取得條件參數
     *
     * @param parameterMap
     * @return HashMap
     * @throws FormException
     * @throws Exception
     */
    private HashMap getConditionData(Map parameterMap) throws FormException, Exception{

        Object otherBean = parameterMap.get("vatBeanOther");
	HashMap conditionMap = new HashMap();
	
	
	//取出參數
	String processObjectName = (String)PropertyUtils.getProperty(otherBean, "processObjectName");
	String searchMethodName = (String)PropertyUtils.getProperty(otherBean, "searchMethodName");
 	String tableType = (String)PropertyUtils.getProperty(otherBean, "tableType");
 	String searchKey = (String)PropertyUtils.getProperty(otherBean, "searchKey");
 	String subEntityBeanName = (String)PropertyUtils.getProperty(otherBean, "subEntityBeanName");

 	String itemFieldName = (String)PropertyUtils.getProperty(otherBean, "itemFieldName");
 	String warehouseCodeFieldName = (String)PropertyUtils.getProperty(otherBean, "warehouseCodeFieldName");
 	String declTypeFieldName = (String)PropertyUtils.getProperty(otherBean, "declTypeFieldName");
 	String declNoFieldName = (String)PropertyUtils.getProperty(otherBean, "declNoFieldName");
 	String declSeqFieldName = (String)PropertyUtils.getProperty(otherBean, "declSeqFieldName");
 	String declDateFieldName = (String)PropertyUtils.getProperty(otherBean, "declDateFieldName");
 	String lotFieldName = (String)PropertyUtils.getProperty(otherBean, "lotFieldName");
 	String qtyFieldName = (String)PropertyUtils.getProperty(otherBean, "qtyFieldName");
 	String perUnitAmountFieldName = "";
 	if(tableType.equals("SO_SALES_ORDER")){
 		perUnitAmountFieldName = (String)PropertyUtils.getProperty(otherBean, "perUnitAmountFieldName");
 	}else{
 		perUnitAmountFieldName = "";
 	}
 	
 	
 	
System.out.println("Babu_searchKey:"+searchKey);
System.out.println("Babu_itemFieldName:"+itemFieldName);
System.out.println("Babu_warehouseCodeFieldName:"+warehouseCodeFieldName);
System.out.println("Babu_declTypeFieldName:"+declTypeFieldName);
System.out.println("Babu_searchMethodName:"+searchMethodName);
System.out.println("Babu_tableType:"+tableType);


 	if(!StringUtils.hasText(processObjectName)){
	    throw new ValidationErrorException("processObjectName參數為空值，無法執行程序！");
	}else if(!StringUtils.hasText(searchMethodName)){
	    throw new ValidationErrorException("searchMethodName參數為空值，無法執行程序！");
	}else if(!StringUtils.hasText(tableType)){
	    throw new ValidationErrorException("tableType參數為空值，無法執行程序！");
	}else if(!StringUtils.hasText(searchKey)){
	    throw new ValidationErrorException("searchKey參數為空值，無法執行程序！");
	}else if(!StringUtils.hasText(subEntityBeanName)){
	    throw new ValidationErrorException("subEntityBeanName參數為空值，無法執行程序！");
	}

 	conditionMap.put("processObjectName", processObjectName);
 	conditionMap.put("searchMethodName", searchMethodName);
 	conditionMap.put("tableType", tableType);
 	conditionMap.put("searchKey", new Long(searchKey));
 	conditionMap.put("subEntityBeanName", subEntityBeanName);
 	conditionMap.put("itemFieldName", itemFieldName);
 	conditionMap.put("warehouseCodeFieldName", warehouseCodeFieldName);
 	conditionMap.put("declTypeFieldName", declTypeFieldName);
 	conditionMap.put("declNoFieldName", declNoFieldName);
 	conditionMap.put("declSeqFieldName", declSeqFieldName);
 	conditionMap.put("declDateFieldName", declDateFieldName);
 	conditionMap.put("lotFieldName", lotFieldName);
 	conditionMap.put("qtyFieldName", qtyFieldName);
 	conditionMap.put("perUnitAmountFieldName", perUnitAmountFieldName);

 	/*log.info("[processObjectName]=" + processObjectName);
 	log.info("[searchMethodName]=" + searchMethodName);
 	log.info("[tableType]=" + tableType);
 	log.info("[searchKey]=" + searchKey);
 	log.info("[subEntityBeanName]=" + subEntityBeanName);
 	log.info("[itemFieldName]=" + itemFieldName);
 	log.info("[warehouseCodeFieldName]=" + warehouseCodeFieldName);
 	log.info("[declTypeFieldName]=" + declTypeFieldName);
 	log.info("[declNoFieldName]=" + declNoFieldName);
 	log.info("[declSeqFieldName]=" + declSeqFieldName);
 	log.info("[declDateFieldName]=" + declDateFieldName);
 	log.info("[lotFieldName]=" + lotFieldName);
 	log.info("[qtyFieldName]=" + qtyFieldName);*/

 	return conditionMap;
    }

	/**
	 * call sp
	 *
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List getExtendItemInfo(HashMap conditionMap) throws Exception {

		TmpExtendItemInfoDAO extendItemInfoDAO = (TmpExtendItemInfoDAO) SpringUtils.getApplicationContext().getBean(
				"tmpExtendItemInfoDAO");
		return extendItemInfoDAO.getExtendItemInfo(conditionMap);
	}

	/**
	 * call sp（鎖住報單庫存） by Weichun 2011.09.22
	 *
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List getExtendItemInfoWithBlock(HashMap conditionMap) throws Exception {

		TmpExtendItemInfoDAO extendItemInfoDAO = (TmpExtendItemInfoDAO) SpringUtils.getApplicationContext().getBean(
				"tmpExtendItemInfoDAO");
		return extendItemInfoDAO.getExtendItemInfoWithBlock(conditionMap);
	}

	public void executeExtendItemProcessor(HashMap parameterMap) throws Exception {
		log.info("=====enter appExtendItemInfoService.executeExtendItemProcessor==取KWE報單=");
		String errorMsg = null;
		try {
			String processObjectName = (String) parameterMap.get("processObjectName");
			String searchMethodName = (String) parameterMap.get("searchMethodName");
			Long searchKey = (Long) parameterMap.get("searchKey");
			String subEntityBeanName = (String) parameterMap.get("subEntityBeanName");
			Object processObj = SpringUtils.getApplicationContext().getBean(processObjectName);
			Object entityBean = MethodUtils.invokeMethod(processObj, searchMethodName, searchKey);
			if (entityBean == null) {
				throw new NoSuchObjectException("查無單據(" + searchKey + ")的資料！");
			}

			List subEntityBeans = (List) PropertyUtils.getProperty(entityBean, subEntityBeanName);
			if (subEntityBeans != null && subEntityBeans.size() > 0) {
				List tmpExtendItemInfoBeans = getExtendItemInfo(parameterMap);
				if (tmpExtendItemInfoBeans != null && tmpExtendItemInfoBeans.size() > 0) {
					// get bean class type
					Object subEntityBean = subEntityBeans.get(0);
					Class clsTask = subEntityBean.getClass();
					HashMap originalEntityBeansMap = produceOriginalEntityBeansMap(subEntityBeans);
					List extendEntityBeans = getAfterExtendEntityBeans(parameterMap, originalEntityBeansMap,
							tmpExtendItemInfoBeans, clsTask,searchKey);
					PropertyUtils.setProperty(entityBean, subEntityBeanName, extendEntityBeans);
					BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
					MethodUtils.invokeMethod(baseDAO, "update", entityBean);
				}
			}
			log.info("=====extend item process finished=====");

		} catch (Exception ex) {
			errorMsg = "擷取商品報單資訊時發生錯誤，原因：";
			log.info(errorMsg + ex.toString());
			log.error(errorMsg + ex.toString());
			throw new Exception(errorMsg + ex.getMessage());
		}

		log.info("=====LEAVE appExtendItemInfoService.executeExtendItemProcessor==取KWE報單=");
	}

    public void executeSoItemInfoFromXFire(HashMap parameterMap) throws Exception{

        String errorMsg = null;
	try{
	    String processObjectName = (String)parameterMap.get("processObjectName");
	    String searchMethodName = (String)parameterMap.get("searchMethodName");
	    Long searchKey = (Long)parameterMap.get("searchKey");
	    Object processObj = SpringUtils.getApplicationContext().getBean(processObjectName);
	    SoSalesOrderHead salesOrderHeadPO = (SoSalesOrderHead)MethodUtils.invokeMethod(processObj, searchMethodName, searchKey);
	    if(salesOrderHeadPO == null){
		throw new NoSuchObjectException("查無主鍵(" + searchKey + ")的銷貨單資料！");
	    }

	    List<SoSalesOrderItem> soSalesOrderItems = salesOrderHeadPO.getSoSalesOrderItems();
	    if(soSalesOrderItems != null && soSalesOrderItems.size() > 0){
		CmDeclarationHeadDAO cmDeclarationHeadDAO = (CmDeclarationHeadDAO)SpringUtils.getApplicationContext().getBean("cmDeclarationHeadDAO");
		Map originalEntityBeansMap = new HashMap();
	        for (int i = 0; i < soSalesOrderItems.size(); i++) {
		    SoSalesOrderItem soSalesOrderItem = (SoSalesOrderItem)soSalesOrderItems.get(i);
		    originalEntityBeansMap.put(soSalesOrderItem.getItemCode() + "{#}" + soSalesOrderItem.getPosSeq(), soSalesOrderItem);
		}
	        String patTrdno = salesOrderHeadPO.getTransactionSeqNo();
	        //String patTrdno = "TD" + salesOrderHeadPO.getPosMachineCode() + salesOrderHeadPO.getTransactionSeqNo();
		Date patHdate =  salesOrderHeadPO.getSalesOrderDate();

		// ===============記錄連到小花系統的啟始時間==================
		SiSystemLogService siSystemLogService = (SiSystemLogService) SpringUtils
			.getApplicationContext().getBean("siSystemLogService");
		Date executeDate = new Date();
		String uuid = UUID.randomUUID().toString();
		siSystemLogService.createSystemLog("T2_SOP_UP",
			MessageStatus.LOG_INFO, "連到小花系統的啟始時間...", executeDate,
			uuid, "SYSTEM");
		List<SoSalesOrderItem> tdfsItems = XFireUtils.getPosData(patTrdno, patHdate);
		// ===============記錄連到連到小花系統的終止時間==================
		siSystemLogService.createSystemLog("T2_SOP_UP",
			MessageStatus.LOG_INFO, "連到小花系統的終止時間...", executeDate,
			uuid, "SYSTEM");

		if(tdfsItems == null || tdfsItems.size() == 0){
		    throw new Exception("查無舊系統單號(" + patTrdno + ")、日期(" + DateUtils.format(patHdate, DateUtils.C_DATA_PATTON_YYYYMMDD) + ")的明細資料！");
		}
		boolean isObtainOldSystemData = false;
                for (int i = 0; i < tdfsItems.size(); i++) {
		    SoSalesOrderItem tdfsItem = (SoSalesOrderItem)tdfsItems.get(i);
		    Double quantity = tdfsItem.getQuantity();
		    Double actualSalesAmount = tdfsItem.getActualSalesAmount();
		    String importDeclNo = tdfsItem.getImportDeclNo();
		    Long importDeclSeq = tdfsItem.getImportDeclSeq();
		    String status = tdfsItem.getStatus();
		    if(quantity == null){
			throw new Exception("品號(" + tdfsItem.getItemCode() + ")、序號(" +  (i + 1) + ")的銷貨數量為空值！");
		    }else if(actualSalesAmount == null){
			throw new Exception("品號(" + tdfsItem.getItemCode() + ")、序號(" +  (i + 1) + ")的銷售金額小計為空值！");
		    }
		    if(i == 0 && StringUtils.hasText(status) && "CL".equals(status)){
			isObtainOldSystemData = true;
		    }
		    if(isObtainOldSystemData){
			SoSalesOrderItem originalEntityBean = (SoSalesOrderItem)originalEntityBeansMap.get(tdfsItem.getItemCode() + "{#}" + tdfsItem.getPosSeq());
			if(originalEntityBean == null){
			    throw new Exception("依據品號(" + tdfsItem.getItemCode() + ")、原POS序號(" +  tdfsItem.getPosSeq() + ")查無相對應的銷貨資料！");
			}
	                BeanUtils.copyProperties(originalEntityBean, tdfsItem);
	                tdfsItem.setQuantity(quantity);
	                tdfsItem.setActualUnitPrice(getActualUnitPrice(actualSalesAmount, quantity));
	                tdfsItem.setActualSalesAmount(actualSalesAmount);
	                tdfsItem.setImportDeclNo(importDeclNo);
	                tdfsItem.setImportDeclSeq(importDeclSeq);
	                tdfsItem.setLotNo("000000000000"); //預設暫時12個零
	                tdfsItem.setOriginalSalesAmount(CommonUtils.round(tdfsItem.getOriginalUnitPrice() * quantity, 0));
	                tdfsItem.setStatus(null);
	                tdfsItem.setIndexNo(null);
	                if((i + 1) != tdfsItem.getPosSeq().intValue()){
                            tdfsItem.setLineId(null);
			}
	                if(StringUtils.hasText(importDeclNo)){
			    CmDeclarationHead declarationHead = cmDeclarationHeadDAO.findOneCmDeclaration(importDeclNo);
			    if(declarationHead != null){
				tdfsItem.setImportDeclDate(declarationHead.getDeclDate());
				tdfsItem.setImportDeclType(declarationHead.getDeclType());
			    }
			}
		    }else{
			throw new Exception("舊系統單號(" + patTrdno + ")、日期(" + DateUtils.format(patHdate, DateUtils.C_DATA_PATTON_YYYYMMDD) + ")" +
					"的銷貨單尚未結案，無法取得報單資料！");
		    }
		}
                salesOrderHeadPO.setSoSalesOrderItems(tdfsItems);
                BaseDAO baseDAO = (BaseDAO)SpringUtils.getApplicationContext().getBean("baseDAO");
	        MethodUtils.invokeMethod(baseDAO, "update", salesOrderHeadPO);
	    }else{
		throw new Exception("主鍵(" + searchKey + ")的銷貨單無任何一筆明細資料！");
	    }
        }catch(Exception ex){
	    errorMsg = "擷取舊系統資訊失敗，原因：" + ex.getMessage();
	    throw new Exception(errorMsg);
	}
    }

    private Double getActualUnitPrice(Double actualSalesAmt, Double quantity){

	if(quantity == 0D){
	    return actualSalesAmt;
	}else if(actualSalesAmt != null && actualSalesAmt != 0D && quantity != null && quantity != 0D){
	    return CommonUtils.round(actualSalesAmt / quantity, 1);
	}else{
	    return 0D;
	}
    }
}
