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

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.OmmChannel;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFHead;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.OmmChannelDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class OmmChannelService {
	private static final Log log = LogFactory.getLog(OmmChannelService.class);
	
	private static HashMap<String, String> categoryTypeMap = new HashMap();
	
	static {
		categoryTypeMap.put("T", "巧克力菸酒");
		categoryTypeMap.put("E", "精品");
		categoryTypeMap.put("C", "化妝品");
		categoryTypeMap.put("F", "台產品");
		categoryTypeMap.put("D", "3C影音圖書");
	}
	
	private OmmChannelDAO ommChannelDAO;
	
	public void setOmmChannelDAO(OmmChannelDAO ommChannelDAO) {
		this.ommChannelDAO = ommChannelDAO;
	}
	
	public static final String[] GRID_SEARCH_FIELD_NAMES = {
			"channelType", "categoryTypeName", "categoryType", "enable"
	};
	
	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = {
			"", "", "", ""
	};

	/**
	 * 
	 * 透過sysSno(PK)查出OmmChannel(通道主檔)
	 * 
	 * @param formBindBean
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public OmmChannel getActualOmmChannel(Object formBindBean) throws FormException, Exception {

		OmmChannel ommChannel = null;
		String sysSnoString = (String) PropertyUtils.getProperty(formBindBean, "sysSno");

		if (StringUtils.hasText(sysSnoString)) {
			Long sysSno = NumberUtils.getLong(sysSnoString);
			ommChannel = this.getActualOmmChannel(sysSno);
		} else {
			throw new ValidationErrorException("傳入的通道主檔主鍵為空值！");
		}
		return ommChannel;

	}
	
	/**
	 * 
	 * @param sysSno
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public OmmChannel getActualOmmChannel(Long sysSno) throws FormException, Exception {
		OmmChannel ommChannel = null;
		ommChannel = (OmmChannel) ommChannelDAO.findByPrimaryKey(OmmChannel.class, sysSno);
		if (ommChannel == null) {
			throw new NoSuchObjectException("查無通道主檔主鍵：" + sysSno + "的資料！");
		}
		return ommChannel;
	}
	
	/**
	 * 透過唯一值取得一筆OmmChannel,可以由isVerification決定是否檢核空值
	 * @param categoryType
	 * @param channelType
	 * @param isVerification
	 * @return
	 * @throws NoSuchObjectException 
	 */
	public OmmChannel getActualOmmChannel(String categoryType, String channelType, boolean isVerification) throws NoSuchObjectException {
		OmmChannel ommChannel = null;
		ommChannel = (OmmChannel) ommChannelDAO.findByIdentification(channelType, categoryType);
		if(isVerification && ommChannel == null) {
			throw new NoSuchObjectException("查無通道主檔：" + channelType + "," + categoryType + "的資料！");
		}
		return ommChannel;
	}
	
	/**
	 * 透過唯一值取得一筆OmmChannel,預設檢核空值
	 * @param categoryType
	 * @param channelType
	 * @return
	 * @throws NoSuchObjectException 
	 */
	public OmmChannel getActualOmmChannel(String categoryType, String channelType) throws NoSuchObjectException {
		return this.getActualOmmChannel(categoryType, channelType, true);
	}
	
	public Map executeSearchInitial(Map parameterMap) throws Exception {

		HashMap resultMap = new HashMap();
		Map multiList = new HashMap();

		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			// 待改
			List channelTypeList = new ArrayList();
			List channelTypeDefaultList = new ArrayList();
			List channelTypeNameList = new ArrayList();
			List channelTypeValueList = new ArrayList();
			channelTypeDefaultList.add("");
			channelTypeDefaultList.add(" ");
			channelTypeDefaultList.add(true);
			channelTypeNameList.add("ERP");
			channelTypeNameList.add("POS");
			channelTypeNameList.add("EC");
			channelTypeNameList.add("APP");
			channelTypeValueList.add("ERP");
			channelTypeValueList.add("POS");
			channelTypeValueList.add("EC");
			channelTypeValueList.add("APP");
			channelTypeList.add(channelTypeDefaultList);
			channelTypeList.add(channelTypeNameList);
			channelTypeList.add(channelTypeValueList);

			List categoryTypeList = new ArrayList();
			List categoryTypeDefaultList = new ArrayList();
			List categoryTypeNameList = new ArrayList();
			List categoryTypeValueList = new ArrayList();
			categoryTypeDefaultList.add("");
			categoryTypeDefaultList.add(" ");
			categoryTypeDefaultList.add(true);
			categoryTypeNameList.add("菸酒巧克力-T");
			categoryTypeNameList.add("精品-E");
			categoryTypeNameList.add("化妝品-C");
			categoryTypeNameList.add("台產品-F");
			categoryTypeNameList.add("3C影音圖書-D");
			categoryTypeValueList.add("T");
			categoryTypeValueList.add("E");
			categoryTypeValueList.add("C");
			categoryTypeValueList.add("F");
			categoryTypeValueList.add("D");
			categoryTypeList.add(categoryTypeDefaultList);
			categoryTypeList.add(categoryTypeNameList);
			categoryTypeList.add(categoryTypeValueList);

			// 下拉選單
			multiList.put("channelType", channelTypeList);
			multiList.put("categoryType", categoryTypeList);
			resultMap.put("multiList", multiList);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("通道查詢作業初始化失敗，原因：" + e.toString());

			throw new Exception("通道查詢作業初始化失敗，原因：" + e.toString());
		}
		return resultMap;

	}
	
	/**
	 * ajax 取得通道主檔查詢分頁
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest) throws Exception {
		log.info("getAJAXSearchPageData...");
		
		List<Properties> result = new ArrayList();
		List<Properties> gridDatas = new ArrayList();
		
		try {
			
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			
			// 查詢條件
			String channelType = httpRequest.getProperty("channelType");
			String categoryType = httpRequest.getProperty("categoryType");
			String enable = httpRequest.getProperty("enable");
			
			log.info("channelType = " + channelType);
			log.info("categoryType = " + categoryType);
			log.info("enable = " + enable);
			
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			StringBuffer findHql = new StringBuffer();
			
			findObjs.put(" and model.channelType = :channelType", channelType);
			findObjs.put(" and model.categoryType = :categoryType", categoryType);
			findObjs.put(" and model.enable = :enable", enable);
			
			Map ommChannelMap = ommChannelDAO.search("OmmChannel as model", findObjs,
					"order by channelType, categoryType", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			
			List<OmmChannel> ommChannelList = (List<OmmChannel>) ommChannelMap.get(BaseDAO.TABLE_LIST);
			
			if(ommChannelList != null && ommChannelList.size() > 0) {
				
				for(OmmChannel ommChannel:ommChannelList) {
					if(categoryTypeMap.containsKey(ommChannel.getCategoryType())) {
						ommChannel.setCategoryTypeName(categoryTypeMap.get(ommChannel.getCategoryType()));
					}else {
						ommChannel.setCategoryTypeName("");
					}
				}
				
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1 ;
				Long maxIndex = (Long) ommChannelDAO.search("OmmChannel as model", 
						"count(model.sysSno) as rowCount", findObjs, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES,
						ommChannelList, gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_SEARCH_FIELD_NAMES, GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error("載入頁面顯示的通道主檔查詢發生錯誤，原因：" + e.toString());
			throw new Exception("載入頁面顯示的通道主檔查詢失敗！");
		}
		
		return result;
	}

}
