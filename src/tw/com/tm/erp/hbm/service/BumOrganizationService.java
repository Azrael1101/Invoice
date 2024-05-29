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

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BumOrganizationTree;
import tw.com.tm.erp.hbm.bean.DbcInformationTables;
import tw.com.tm.erp.hbm.bean.ImItemCategoryLevel;
import tw.com.tm.erp.hbm.bean.ImItemCategoryNode;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BumOrganizationDAO;
import tw.com.tm.erp.hbm.dao.BumOrganizationTreeDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class BumOrganizationService {
	private static final Log log = LogFactory.getLog(BumOrganizationService.class);
	/**
	 * 查詢明細欄位 kweImDetail
	 */
	public static final String[] GRID_FIELD_NAMES_SEARCH = 
		{"sysSno", "businessUnitName"};

	public static final String[] GRID_FIELD_DEFAULT_VALUES_SEARCH = 
		{"", ""};

	public static final int[] GRID_FIELD_TYPES_SEARCH = 
		{AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
	/**
	 * 查詢明細欄位 kweImDetail2
	 */
	public static final String[] GRID_FIELD_NAMES_SEARCH2 = 
		{"ownNode", "businessUnitType", "businessUnitName"};

	public static final String[] GRID_FIELD_DEFAULT_VALUES_SEARCH2 = 
		{"", "", ""};

	public static final int[] GRID_FIELD_TYPES_SEARCH2 = 
		{AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
	
	private BumOrganizationTreeDAO bumOrganizationTreeDAO;

	public void setBumOrganizationTreeDAO(BumOrganizationTreeDAO bumOrganizationTreeDAO) {
		this.bumOrganizationTreeDAO = bumOrganizationTreeDAO;
	}


	/**
	 * ajax 查詢明細資料檔載入時,取得明細分頁資料
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest)throws Exception {
		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			List<BumOrganizationTree> bumOrganizationTreeList = null;
			
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			
			/**帶入查詢的值*/
			String pSysSnoStr = httpRequest.getProperty("pSysSno");
//			String organizationType = httpRequest.getProperty("organizationType");// 組織類別
//			String organizationName = httpRequest.getProperty("organizationName");// 組織名稱
//			String ownNode = httpRequest.getProperty("ownNode");// 自己的節點
//			log.info("獲得ownNode = " + ownNode);
			
			/**執行查詢*/
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			if(StringUtils.hasText(pSysSnoStr)) {
				Long pSysSno = NumberUtils.getLong(pSysSnoStr);
				findObjs.put(" and model.parentNode.sysSno = :pSysSno", pSysSno);
				bumOrganizationTreeList = (List<BumOrganizationTree>) bumOrganizationTreeDAO.search(
						" BumOrganizationTree as model", findObjs, " order by indexNo asc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE).get(BaseDAO.TABLE_LIST);
				System.out.println("bumOrganizationTreeList.Size=["+ bumOrganizationTreeList.size() + "]");
			}
//			if(StringUtils.hasText(organizationType)|| StringUtils.hasText(organizationName)) {
//				findObjs.put(" and model.organizationType = :organizationType", organizationType);
//				findObjs.put(" and model.organizationName = :organizationName", organizationName);
//			}
//			if(StringUtils.hasText(ownNode)) {
//				findObjs.put(" and model.parentNode = :parentNode", ownNode);
//			}
			
//			List<BumOrganizationTree> bumOrganizationTreeList = (List<BumOrganizationTree>) bumOrganizationTreeDAO.search(
//					" BumOrganizationTree as model", findObjs, " order by indexNo asc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE).get(BaseDAO.TABLE_LIST);
//			System.out.println("bumOrganizationTreeList.Size=["+ bumOrganizationTreeList.size() + "]");
			 
			if (bumOrganizationTreeList != null && bumOrganizationTreeList.size() > 0) {
				// 取得第一筆的 INDEX
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; 
				// 取得最後一筆 INDEX
				Long maxIndex = (Long) bumOrganizationTreeDAO.search("BumOrganizationTree as model",
						"count(model.sysSno) as rowCount", findObjs,"order by indexNo asc", BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
				log.info("maxIndex: "+maxIndex);
				
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES_SEARCH, GRID_FIELD_DEFAULT_VALUES_SEARCH, bumOrganizationTreeList, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES_SEARCH, GRID_FIELD_DEFAULT_VALUES_SEARCH, map, gridDatas));
			}
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的查詢發生錯誤，原因：" + ex.toString());
			ex.printStackTrace();
			throw new Exception("載入頁面顯示的查詢失敗！");
		}
	}
	
	/**
	 * 執行查詢初始化
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public Map executeInitial(Map parameterMap) throws Exception {
		log.info("executeInitial...");
		
		HashMap resultMap = new HashMap();
		HashMap multiList = new HashMap();
		
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			
			List<BumOrganizationTree> allBumOrganizationTree = this.findAllBumOrganizationTree();
			
			if(allBumOrganizationTree != null) {
				multiList.put("allBumOrganizationTree", AjaxUtils.produceSelectorData(allBumOrganizationTree, "sysSno",
						"businessUnitName", true, true));
				resultMap.put("multiList", multiList);
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("組織結構查詢作業初始化失敗，原因：" + ex.toString());
			
			throw new Exception("組織結構查詢作業初始化失敗，原因：" + ex.toString());
		}
		return resultMap;
	}
	
	public List<Properties> getDetailBySysSno(Properties httpRequest) throws Exception {
		log.info("getDetailByCategoryNodeCode...");
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		BumOrganizationTree bumOrganizationTree = null;
		
		try {
			String sysSnoStr  = httpRequest.getProperty("sysSno");
			if(StringUtils.hasText(sysSnoStr)) {
				Long sysSno = NumberUtils.getLong(sysSnoStr);
				bumOrganizationTree = (BumOrganizationTree) bumOrganizationTreeDAO.findByPrimaryKey(BumOrganizationTree.class, sysSno);
				
				if(bumOrganizationTree != null) {
					properties.setProperty("businessUnitNameDetail", bumOrganizationTree.getBusinessUnitName());
				}else {
					log.info("sysSno = " + sysSno);
					properties.setProperty("errorMsg", "查無此組織代號");
				}
				
				
			}else {
				properties.setProperty("errorMsg", "查無此明細");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			properties.setProperty("errorMsg", e.toString());
		} finally {
			result.add(properties);
		}
		
		return result;
	}
	
	public List<BumOrganizationTree> findAllBumOrganizationTree(){
		return bumOrganizationTreeDAO.findAllBumOrganizationTree();
	}

}
