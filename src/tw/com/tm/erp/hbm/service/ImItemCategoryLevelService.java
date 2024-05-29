package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.DbcInformationTables;
import tw.com.tm.erp.hbm.bean.EtlEventViewTable;
import tw.com.tm.erp.hbm.bean.ImItemCategoryLevel;
import tw.com.tm.erp.hbm.bean.ImItemCategoryNode;
import tw.com.tm.erp.hbm.bean.OmmChannel;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFHead;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFLine;
import tw.com.tm.erp.hbm.dao.EtlEventViewTableDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryLevelDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryNodeDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class ImItemCategoryLevelService {
	private static final Log log = LogFactory.getLog(ImItemCategoryLevelService.class);
	
	// spring IOC DAO
	ImItemCategoryLevelDAO imItemCategoryLevelDAO;
	ImItemCategoryNodeDAO imItemCategoryNodeDAO;
	EtlEventViewTableDAO etlEventViewTableDAO;
	
	public void setEtlEventViewTableDAO(EtlEventViewTableDAO etlEventViewTableDAO) {
		this.etlEventViewTableDAO = etlEventViewTableDAO;
	}

	public void setImItemCategoryLevelDAO(ImItemCategoryLevelDAO imItemCategoryLevelDAO) {
		this.imItemCategoryLevelDAO = imItemCategoryLevelDAO;
	}
	
	public void setImItemCategoryNodeDAO(ImItemCategoryNodeDAO imItemCategoryNodeDAO) {
		this.imItemCategoryNodeDAO = imItemCategoryNodeDAO;
	}

	public static final String[] GRID_FIELD_NAMES = { 
	        "categoryLevelCode", "categoryNodeName", "categoryNodeCode"};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = {
	        "", "", ""};
	
	public Map executeInitial(Map parameterMap) throws Exception {
		log.info("executeInitial...");
		
		HashMap resultMap = new HashMap();
		HashMap multiList = new HashMap();
		
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(otherBean, "loginBrandCode");
			
//			EtlEventViewTable etlEventViewTable =null;
//			for(Long i = 202101100001L; i <= 202101100100L ; i++) {
//				
//				etlEventViewTable = new EtlEventViewTable();
//				etlEventViewTable.setModule("ImMovement");
//				etlEventViewTable.setCurrentEventCode(1L);
//				etlEventViewTable.setNumberOfExecutions(0);
//				etlEventViewTable.setOrderNo(i+"");
//				etlEventViewTable.setOrderTypeCode("WWP");
//				etlEventViewTable.setStatus('1');
//				etlEventViewTable.setTargetEventCode(4L);
//				etlEventViewTableDAO.save(etlEventViewTable);
//			}
			
			List<ImItemCategoryLevel> allCategoryLevelCodes = this.findAllImItemCategoryLevelByBrandCode(loginBrandCode);
			
			
			
			if(allCategoryLevelCodes!=null) {
				log.info(allCategoryLevelCodes.size());
				multiList.put("allCategoryLevelCodes", AjaxUtils.produceSelectorData(allCategoryLevelCodes, "categoryLevelCode",
						"categoryLevelName", true, true));
				resultMap.put("multiList", multiList);
			}
			resultMap.put("brandCode", loginBrandCode);
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("通道建立作業初始化失敗，原因：" + ex.toString());
			
			throw new Exception("通道建立作業初始化失敗，原因：" + ex.toString());
		}
		return resultMap;
	}
	

	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
		log.info("getAJAXPageData...");
		
		try {
			List<Properties> result = new ArrayList();
		    List<Properties> gridDatas = new ArrayList();
		    ImItemCategoryNode imItemCategoryNode = null;
		    List<ImItemCategoryNode> imItemCategoryNodeList = null;
		    
		    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		    
		    log.info("iSPage = " + iSPage + "; iPSize = " + iPSize);
		    
		    String categoryLevelCode = httpRequest.getProperty("categoryLevelCode");
		    String categoryNodeCode = httpRequest.getProperty("categoryNodeCode");
		    String brandCode = httpRequest.getProperty("brandCode");
		    
		    if(!categoryLevelCode.equals("") && !categoryNodeCode.equals("")) {
			    imItemCategoryNodeList = this.findPageLineByParent(brandCode, categoryLevelCode, categoryNodeCode, iSPage, iPSize);  
		    }else if(!categoryLevelCode.equals("")) {
		    	imItemCategoryNodeList = this.findPageLineByOwn(brandCode, categoryLevelCode, categoryNodeCode, iSPage, iPSize);
		    }
		    
		    if(imItemCategoryNodeList != null && imItemCategoryNodeList.size() != 0) {
		    	// 取得第一筆的INDEX
				Long firstIndex = (long) iSPage * iPSize + 1;
				// 取得最後一筆 INDEX
				Long maxIndex;
				if(!categoryLevelCode.equals("") && !categoryNodeCode.equals("")) {
					maxIndex = this.findPageLineMaxIndexByParent(brandCode, categoryLevelCode, categoryNodeCode);
			    }else{
			    	maxIndex = this.findPageLineMaxIndexByOwn(brandCode, categoryLevelCode, categoryNodeCode);
			    }
				
				
				
				log.info("firstIndex="+firstIndex+", maxIndex="+maxIndex);
		    	
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imItemCategoryNodeList, gridDatas,
						firstIndex, maxIndex));	
		    }else{
		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));   
		    }
		    
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		    log.error("載入頁面顯示的通道明細明細發生錯誤，原因：" + ex.toString());
		    throw new Exception("載入頁面顯示的通道明細失敗！");
		}
	}
	
	public List<ImItemCategoryNode> findPageLine(String categoryLevelCode, int iSPage, int iPSize){
		return imItemCategoryLevelDAO.findPageLine(categoryLevelCode, iSPage, iPSize);
	}
	
	
	/**
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getDetailByCategoryNodeCode(Properties httpRequest) throws Exception {
		log.info("getDetailByCategoryNodeCode...");
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		
		try {
			String categoryNodeCode  = httpRequest.getProperty("categoryNodeCode");
			String categoryLevelCode  = httpRequest.getProperty("categoryLevelCode");
		    String brandCode = httpRequest.getProperty("brandCode");
			ImItemCategoryNode imItemCategoryNode = this.findImItemCategoryNodeByCategoryUnique(brandCode, categoryLevelCode, categoryNodeCode);
			
			
			properties.setProperty("categoryNodeNameDetail", imItemCategoryNode.getCategoryNodeName());
			properties.setProperty("categoryNodeCodeDetail", imItemCategoryNode.getCategoryNodeCode());
			log.info("lazy tset : 取得自己的階層類別開始");
			properties.setProperty("categoryLevelCodeDetail", this.findImItemCategoryLevelByUnique(imItemCategoryNode.getBrandCode(), imItemCategoryNode.getCategoryLevelCode()).getCategoryLevelCode());
			log.info("lazy tset : 取得自己的階層類別結束");
			if( imItemCategoryNode.getpCategoryLevelCode() == null || "".equals(imItemCategoryNode.getpCategoryLevelCode())) {
				properties.setProperty("pCategoryNodeNameDetail", "N/A");
				properties.setProperty("pCategoryNodeCodeDetail", "N/A");
				properties.setProperty("pCategoryLevelNameDetail", "N/A");
			}else {
				ImItemCategoryNode pImItemCategoryNode = imItemCategoryNode.getpImItemCategoryNode();
				properties.setProperty("pCategoryNodeNameDetail", pImItemCategoryNode.getCategoryNodeName());
				properties.setProperty("pCategoryNodeCodeDetail", pImItemCategoryNode.getCategoryNodeCode());
				properties.setProperty("pCategoryLevelNameDetail", "還沒做");
			}
			log.info("lazy tset : 取得孩子的節點類別群開始");
			properties.setProperty("cCategoryNodeNumDetail", imItemCategoryNode.getcImItemCategoryNodeList().size()+"個");
			log.info("lazy tset : 取得孩子的節點類別群結束");
			properties.setProperty("cCategoryLevelNameDetail", "還沒做");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			properties.setProperty("errorMsg", e.toString());
		} finally {
			result.add(properties);
		}
		
		return result;
	}
	
	
	/**
	 * 商品分類查詢結果存檔
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 * @William說明
	 */
	public List<Properties> saveImItemCategorySearchResult(Properties httpRequest) throws Exception{
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	public ImItemCategoryNode findImItemCategoryNodeByCategoryUnique(String brandCode, String categoryLevelCode, String categoryNodeCode) {
		return imItemCategoryNodeDAO.findImItemCategoryNodeByCategoryUnique(brandCode, categoryLevelCode, categoryNodeCode);
	}
	
	public List<ImItemCategoryLevel> findAllImItemCategoryLevelByBrandCode(String brandCode){
		return imItemCategoryLevelDAO.findAllImItemCategoryLevelByBrandCode(brandCode);
	}
	
	public ImItemCategoryLevel findImItemCategoryLevelByUnique(String brandCode, String categoryLevelCode) {
		return imItemCategoryLevelDAO.findImItemCategoryLevelByUnique(brandCode, categoryLevelCode);
	}
	
	public List<ImItemCategoryNode> findAllImItemCategoryNodeByCategoryLevelCode(String brandCode, String categoryLevelCode){
		return imItemCategoryNodeDAO.findAllImItemCategoryNodeByCategoryLevelCode(brandCode, categoryLevelCode);
	}
	
	public List<ImItemCategoryNode> findPageLineByParent(String brandCode, String categoryLevelCode, String categoryNodeCode, int iSPage, int iPSize) {
		return imItemCategoryNodeDAO.findPageLineByParent(brandCode, categoryLevelCode, categoryNodeCode, iSPage, iPSize);
	}
	
	public List<ImItemCategoryNode> findPageLineByOwn(String brandCode, String categoryLevelCode, String categoryNodeCode, int iSPage, int iPSize) {
		return imItemCategoryNodeDAO.findPageLineByOwn(brandCode, categoryLevelCode, categoryNodeCode, iSPage, iPSize);
	}
	
	public Long findPageLineMaxIndexByParent(String brandCode, String categoryLevelCode, String categoryNodeCode) {
		return imItemCategoryNodeDAO.findPageLineMaxIndexByParent(brandCode, categoryLevelCode, categoryNodeCode);
	}
	
	public Long findPageLineMaxIndexByOwn(String brandCode, String categoryLevelCode, String categoryNodeCode) {
		return imItemCategoryNodeDAO.findPageLineMaxIndexByOwn(brandCode, categoryLevelCode, categoryNodeCode);
	}

}
