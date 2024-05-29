package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;

import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionItemDAO;
import tw.com.tm.erp.utils.AjaxUtils;

public class PosDownloadService {
    ImItemEancodeDAO imItemEancodeDAO;
	public void setImItemEancodeDAO(ImItemEancodeDAO imItemEancodeDAO) {
		this.imItemEancodeDAO = imItemEancodeDAO;
	}
	
	private static final String[] GRID_SEARCH_EAN_FIELD_NAMES = {
	    "eanCode","itemCode","lastUpdatedBy","lastUpdateDate",
	    "eanCode"
	};
	
	private static final String[] GRID_SEARCH_EAN_DEFAULT_FIELD_NAMES = {
	    "","","","",""
	};
	
	public List<Properties> getPMOSearch(Properties httpRequest) throws Exception{
	
	  	    List<Properties> result = new ArrayList();
	  	    List<Properties> gridDatas = new ArrayList();
	  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
	  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
	  	  
	  	    //======================帶入Head的值=========================
	  	    String dataType = httpRequest.getProperty("dataType");
	 
	  		String eanCode = httpRequest.getProperty("eanCode");
		  	HashMap map = new HashMap();
		  	HashMap findObjs = new HashMap();
		  	findObjs.put("and model.eanCode like :itemCode", eanCode);
		  	Map itemMap = imItemEancodeDAO.search("ImItemEancode as model", findObjs, " order by lastUpdateDate desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE) ;
		  	
		  	List<ImItemEancode> imItemEanCode = (List<ImItemEancode>) itemMap.get(BaseDAO.TABLE_LIST);
		  	 System.out.println("==EAN=="+imItemEanCode.size());
		 
		  	     System.out.println("==into==");
		  		Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;    	   // 取得第一筆的INDEX
		  		System.out.println("===findex="+firstIndex);
		  	    	Long maxIndex = (Long) imItemEancodeDAO.search("ImItemEancode as model","count(model.eanCode) as rowCount", findObjs, "order by model.lastUpdateDate", iSPage,
					iPSize, BaseDAO.QUERY_RECORD_COUNT).get(BaseDAO.TABLE_RECORD_COUNT);
		  	    	System.out.println("===max=="+maxIndex);
		  
		  	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_EAN_FIELD_NAMES, GRID_SEARCH_EAN_DEFAULT_FIELD_NAMES, imItemEanCode, gridDatas, firstIndex, maxIndex));
		  	
	  
	  	    return result;
	
	}
}
