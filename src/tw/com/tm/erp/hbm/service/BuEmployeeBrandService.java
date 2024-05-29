package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.ImLetterOfCreditHead;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiFunction;
import tw.com.tm.erp.hbm.bean.SiGroup;
import tw.com.tm.erp.hbm.bean.SiGroupId;
import tw.com.tm.erp.hbm.bean.SiGroupMenu;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.SiGroupDAO;
import tw.com.tm.erp.hbm.dao.SiUsersGroupDAO;
import tw.com.tm.erp.hbm.bean.SiUsersGroup;
import tw.com.tm.erp.hbm.bean.SiUsersGroupId;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class BuEmployeeBrandService {

    private static final Log log = LogFactory.getLog(BuEmployeeBrandService.class);
    private BuEmployeeBrandDAO buEmployeeBrandDAO;
    private BuBrandDAO buBrandDAO;
    private BuEmployeeDAO buEmployeeDAO;
    
  	public static final String[] GRID_FIELD_NAMES = { 
  		"indexNo" , "isSelected","brandCode","brandName",
  		"reserve1", "reserve2", "reserve3", "reserve4", "reserve5",
  		"createdBy", "creationDate", "lastUpdatedBy", "lastUpdateDate"};
  	
    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
    	"", "", "", "", 
			"", "", "", "", "",  
			"", "", "", ""};
    
    public static final int[] GRID_FIELD_TYPES = { 
    	AjaxUtils.FIELD_TYPE_LONG,   AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,	AjaxUtils.FIELD_TYPE_STRING,
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,  
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, 	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, };
  	
	/**
	 * AJAX Load Page Data
	 * 
	 * @param headObj
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<Properties> getAJAXPageData(Properties httpRequest) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException ,Exception{

		String employeeCode = httpRequest.getProperty("employeeCode");
		List<Properties> re = new ArrayList();
		List<Properties> gridDatas = new ArrayList();
		int iSPage = AjaxUtils.getStartPage(httpRequest);
		int iPSize = AjaxUtils.getPageSize(httpRequest);

		log.info("BuEmployeeBrandService.getAJAXPageData employeeCode=" + employeeCode + ",iSPage=" + iSPage + ",iPSize=" + iPSize);

		List<BuBrand> buBrands =  buBrandDAO.findPageLineAll(iSPage,iPSize);

		if (null != buBrands && buBrands.size() > 0) {
			log.info("BuEmployeeBrandService.getAJAXPageData buBrands.size() = " + buBrands.size());
			try {
				BuEmployeeBrand buEmployeeBrand;
				BuEmployeeBrandId buEmployeeBrandId;
      	for (int i=0 ; i<buBrands.size() ;i++) {
  				BuBrand buBrand = buBrands.get(i);
  				log.info("buBrand =  " +buBrand.getBrandCode());
  				buEmployeeBrandId = new BuEmployeeBrandId(employeeCode,buBrand.getBrandCode());
  				buEmployeeBrand = buEmployeeBrandDAO.findById(buEmployeeBrandId);
  				if(buEmployeeBrand != null){
  					buBrand.setIsSelected("Y");
  				}else{
  					buBrand.setIsSelected("N");
  				}
  			}
			// 取得第一筆的INDEX
			Long firstIndex = (buBrands.get(0)).getIndexNo();
			log.info("firstIndex = " + firstIndex);
			// 取得最後一筆 INDEX
			Long maxIndex = buBrandDAO.findPageLineMaxIndex();
			log.info("maxIndex = "+maxIndex);
				re.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, buBrands, gridDatas,
						firstIndex, maxIndex));
				
			} catch (Exception e) {
				e.printStackTrace();
			}				
		}else{
				log.info("SiUsersGroupService.getAJAXPageData AjaxUtils.siGroupMenuLists=nothing");
				re.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
		}
		log.info("------return is Here");
		return re;
	}

  
	/**
	 * 更新PAGE的LINE
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{

		try{
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			String employeeCode = httpRequest.getProperty("employeeCode");
			String loginEmployeeCode = httpRequest.getProperty("loginEmployeeCode");
			String errorMsg = null;

			log.info("updateAJAXPageLinesData.gridData:"+gridData);
			log.info("updateAJAXPageLinesData.gridLineFirstIndex:"+gridLineFirstIndex);
			log.info("updateAJAXPageLinesData.gridRowCount:"+gridRowCount);
			log.info("updateAJAXPageLinesData.brandCode:"+employeeCode);
			// 將STRING資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);

			BuEmployee buEmployee = buEmployeeDAO.findById(employeeCode);
			BuEmployeeBrand buEmployeeBrand;
			BuEmployeeBrandId buEmployeeBrandId;

			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					buEmployeeBrandId = new BuEmployeeBrandId(employeeCode,upRecord.getProperty("brandCode"));
					buEmployeeBrand = buEmployeeBrandDAO.findById(buEmployeeBrandId);
					if("N".equals(upRecord.getProperty("isSelected")) && buEmployeeBrand != null){
						log.info("buEmployeeBrandDAO.delete " + buEmployeeBrand.getId().getBrandCode() );
						log.info("buEmployeeBrandDAO.delete " + buEmployeeBrand.getId().getEmployeeCode() );
						log.info("buEmployeeBrandDAO.delete " + buEmployeeBrand.getIndexNo() );
						buEmployeeBrandDAO.delete(buEmployeeBrand);
						log.info("buEmployeeBrandDAO.delete " + buEmployeeBrand.getIndexNo() );
					}else if("Y".equals(upRecord.getProperty("isSelected")) && buEmployeeBrand == null){
						buEmployeeBrand = new BuEmployeeBrand(buEmployeeBrandId, buEmployee);
						buEmployeeBrand.setCreatedBy(loginEmployeeCode);
						buEmployeeBrand.setCreationDate(new Date());
						buEmployeeBrand.setLastUpdatedBy(loginEmployeeCode);
						buEmployeeBrand.setLastUpdateDate(new Date());
						buEmployeeBrand.setIndexNo(1l);
						log.info("buEmployeeBrandDAO.save " + upRecord.getProperty("brandCode") );
						buEmployeeBrandDAO.save(buEmployeeBrand);
					}
				}
			}
			return AjaxUtils.getResponseMsg(errorMsg);
		}catch(Exception ex){
			log.info("更新使用者群組發生錯誤，原因：" + ex.toString());
			log.error("更新使用者群組發生錯誤，原因：" + ex.toString());
			throw new Exception("更新使用者群組失敗！"); 
		}	
	}

	public List<Properties> getEmployeeBrandInfo(Properties httpRequest) throws Exception{
		List<Properties> result = new ArrayList();
		Properties properties   = new Properties();
		String employeeCode = httpRequest.getProperty("employeeCode");
		try{
			List<BuEmployeeBrand> buEmployeeBrands = buEmployeeBrandDAO.findByProperty("id.employeeCode", employeeCode);
			log.info("buEmployeeBrands.size = " + buEmployeeBrands.size());
			properties.setProperty("buEmployeeBrands", AjaxUtils.parseSelectorData(AjaxUtils.produceSelectorData( buEmployeeBrands, "brandCode", "brandCode", false, false)));
			result.add(properties);
			return result;
		}catch (Exception ex) {
			log.error("查詢 LC 資料時發生錯誤，原因：" + ex.getMessage());
			throw new Exception("查詢 LC 資料時發生錯誤失敗！");
		}
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setBuEmployeeBrandDAO(BuEmployeeBrandDAO buEmployeeBrandDAO) {
		this.buEmployeeBrandDAO = buEmployeeBrandDAO;
	}

	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}
}
