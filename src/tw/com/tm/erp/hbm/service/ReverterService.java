package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.*;
import tw.com.tm.erp.hbm.dao.*;
import tw.com.tm.erp.utils.*;


/**
 * 報單單頭 Service 
 * 
 * @author MyEclipse Persistence Tools
 */
public class ReverterService {

	private static final Log log = LogFactory.getLog(ReverterService.class);
	private static ApplicationContext context = SpringUtils.getApplicationContext();
	private BuCommonPhraseService buCommonPhraseService;
	private BuOrderTypeService buOrderTypeService;
	private BuBrandService buBrandService;
	private NativeQueryDAO nativeQueryDAO;
	private ImTransationDAO imTransationDAO;
	
	
	/**
	 * 執行初始化
     * @param parameterMap
     * @return Map
     * @throws Exception
     */
    public Map executeInitial(Map parameterMap) throws Exception{
	log.info("executeInitial");
        HashMap resultMap	= new HashMap();
        Object otherBean	= parameterMap.get("vatBeanOther");
        String brandCode	= (String)PropertyUtils.getProperty(otherBean, "brandCode");
        String category		= (String)PropertyUtils.getProperty(otherBean, "category");
        String typeCode		= (String)PropertyUtils.getProperty(otherBean, "typeCode");
        Map multiList = new HashMap(0);
        try{
        	BuBrand buBrand = buBrandService.findById( brandCode );
        	List<BuCommonPhraseLine> allOrderType;
        	try{
	        	if("user".equals(category))
	        	{
	        		log.info("商控反確初始化");
	        		allOrderType = buCommonPhraseService.getBuCommonPhraseLines("ReverterOrderTypeOpen");
	        	}
	        	else
	        	{
	        		log.info("資訊反確初始化");
	        		allOrderType = buCommonPhraseService.getBuCommonPhraseLines("ReverterOrderType");
	        	}
        	}
        	catch(Exception e)
        	{
        		allOrderType = buCommonPhraseService.getBuCommonPhraseLines("ReverterOrderType");
        	}
        	if(null == allOrderType || allOrderType.size() == 0)
        		throw new Exception("查無反轉類別設定");
        	if(!StringUtils.hasText(typeCode))
        		typeCode = allOrderType.get(0).getId().getLineCode();
        	List<BuOrderType>        allOrderTypes  = buOrderTypeService.findOrderbyType(brandCode, typeCode);
        	log.info(allOrderTypes.size());
        	multiList.put("allOrderType" ,     AjaxUtils.produceSelectorData( allOrderType, "lineCode", "name", false, false, typeCode));
        	multiList.put("allOrderTypes" ,     AjaxUtils.produceSelectorData( allOrderTypes, "orderTypeCode", "name", true, false));
        	resultMap.put("multiList",multiList);
        	resultMap.put("branchCode",        buBrand.getBranchCode());
            resultMap.put("brandName",         buBrand.getBrandName());
	    	return resultMap;       	
        }catch (Exception ex) {
        	log.error("反轉單初始化失敗，原因：" + ex.getMessage());
	    	throw new Exception("反轉單初始化別失敗，原因：" + ex.getMessage());
        }           
    }


    /**
	 * 依據brandCode,typeCode取得單別下拉清單
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> changeTypeCode(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			String typeCode = httpRequest.getProperty("typeCode");
			List<BuOrderType> allOrderTypes = buOrderTypeService.findOrderbyType(brandCode, typeCode);
			allOrderTypes = AjaxUtils.produceSelectorData(allOrderTypes  ,"orderTypeCode" ,"name",  true,  false);
			properties.setProperty("allOrderTypes", AjaxUtils.parseSelectorData(allOrderTypes));
			result.add(properties);
			return result;
		}catch(Exception ex){
			log.error("取得單別下拉清單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得單別下拉清單發生錯誤，原因：" + ex.getMessage());
		}
	}
	
	/**
	 * 依據brandCode,typeCode,orderTypeCode,orderNo取得單聚資訊
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> changeOrderNo(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try{
			String brandCode = httpRequest.getProperty("brandCode");
			String typeCode = httpRequest.getProperty("typeCode");
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			String orderNo = httpRequest.getProperty("orderNo");
			
			BuCommonPhraseLine orderType 	= buCommonPhraseService.getBuCommonPhraseLine("ReverterOrderType", typeCode);
			if(null == orderType || !StringUtils.hasText(orderType.getAttribute1()))
				throw new Exception("查無類別所對應之資料表名稱");
			
			String tableName = orderType.getAttribute1();
			String number = orderType.getAttribute3(); // 單號
			
			String headId = "HEAD_ID";
			if("IM_STORAGE_HEAD".equals(tableName))
				headId = "STORAGE_HEAD_ID";
					
			StringBuffer tmpSql = new StringBuffer();
			tmpSql.append("select "+headId+", CREATED_BY, STATUS from ").append(tableName).append(" ")
			.append("where BRAND_CODE = '").append(brandCode).append("' ");
			if(StringUtils.hasText(orderTypeCode)){
			    tmpSql.append("and ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ");
			}
			if(StringUtils.hasText(number)){
			    tmpSql.append("and ").append(number).append(" = '").append(orderNo).append("' ");
			}else{
			    tmpSql.append("and ORDER_NO = '").append(orderNo).append("' ");    
			}
			
			List headList = nativeQueryDAO.executeNativeSql(tmpSql.toString());
			if(null == headList || headList.size() == 0)
				throw new Exception("查無此張單據");
			Object[] head = (Object[])headList.get(0);
			properties.setProperty("headId", ((BigDecimal)head[0]).toString());
			properties.setProperty("lastUpdatedBy", (String)head[1]);
			properties.setProperty("lastUpdatedByName", UserUtils.getUsernameByEmployeeCode((String)head[1]));
			properties.setProperty("formStatus", (String)head[2]);
			properties.setProperty("formStatusName", OrderStatus.getChineseWord((String)head[2]));
		}catch(Exception ex){
			log.error("查詢單號發生錯誤，原因：" + ex.getMessage());
			properties.setProperty("errorMsg", ex.getMessage());
		}
		
		result.add(properties);
		return result;
	}
	
	/**
	 * 執行反轉
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Object executeReverter(Map parameterMap) throws FormException, Exception {
		log.info("executeReverter");
		Object bean = null;
		try{
			Object formBindBean 	= parameterMap.get("vatBeanFormBind");
			String brandCode		= (String)PropertyUtils.getProperty(formBindBean, "brandCode");
			String typeCode			= (String)PropertyUtils.getProperty(formBindBean, "typeCode");
			String orderTypeCode	= (String)PropertyUtils.getProperty(formBindBean, "orderTypeCode");
			String orderNo			= (String)PropertyUtils.getProperty(formBindBean, "orderNo");
			Long headId				= NumberUtils.getLong((String)PropertyUtils.getProperty(formBindBean, "headId"));
			String lastUpdatedBy	= (String)PropertyUtils.getProperty(formBindBean, "lastUpdatedBy");
			
			BuCommonPhraseLine orderType 	= buCommonPhraseService.getBuCommonPhraseLine("ReverterOrderType", typeCode);
			if(null == orderType || !StringUtils.hasText(orderType.getAttribute2()))
				throw new Exception("查無類別所對應之執行函式名稱");
			
			Object objectService = context.getBean(orderType.getAttribute2());
			log.info("orderType.getAttribute2() = " + orderType.getAttribute2());
			try{
				if(typeCode.equals("CM")){
					bean = MethodUtils.invokeMethod(objectService, "executeReverter", new Object[]{headId, lastUpdatedBy, parameterMap});
				}else{
					bean = MethodUtils.invokeMethod(objectService, "executeReverter", new Object[]{headId, lastUpdatedBy});
				}
				if(null == bean)
					throw new Exception("查無主鍵:" + headId + " 之對應單據，請洽資訊室");
			}catch (InvocationTargetException e){
				throw (Exception)e.getCause();
			}
			return bean;
		} catch (Exception ex) {
			log.error("執行反確認時發生錯誤，原因： " + ex.toString());
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * 執行反確認流程
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public void executeProcess(Map parameterMap, Object bean) throws FormException, Exception {
		log.info("executeProcess");
		try{
			Object formBindBean 	= parameterMap.get("vatBeanFormBind");
			String typeCode			= (String)PropertyUtils.getProperty(formBindBean, "typeCode");
			BuCommonPhraseLine orderType 	= buCommonPhraseService.getBuCommonPhraseLine("ReverterOrderType", typeCode);
			if(null == orderType || !StringUtils.hasText(orderType.getAttribute2()))
				throw new Exception("查無類別所對應之執行函式名稱");
			Object objectService = context.getBean(orderType.getAttribute2());
			log.info("orderType.getAttribute2() = " + orderType.getAttribute2());
			try{
				MethodUtils.invokeMethod(objectService, "executeReverterProcess", new Object[]{bean});
			}catch (InvocationTargetException e){
				throw (Exception)e.getCause();
			}
		} catch (Exception ex) {
			log.error("執行反確認時發生錯誤，原因： " + ex.toString());
			throw new Exception(ex.getMessage());
		}
	}
	
	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}

	public void setImTransationDAO(ImTransationDAO imTransationDAO) {
		this.imTransationDAO = imTransationDAO;
	}
	
}