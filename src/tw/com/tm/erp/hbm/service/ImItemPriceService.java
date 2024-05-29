package tw.com.tm.erp.hbm.service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceViewDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class ImItemPriceService {
	private ImItemPriceViewDAO imItemPriceViewDAO;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private ImItemPriceDAO imItemPriceDAO ;
    private NativeQueryDAO nativeQueryDAO;
    
	/* spring IoC */

	public void setImItemPriceViewDAO(ImItemPriceViewDAO imItemPriceViewDAO) {
		this.imItemPriceViewDAO = imItemPriceViewDAO;
	}

	public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}
	
	public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
		this.imItemPriceDAO = imItemPriceDAO;
	}
	
	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
	
    
	public List<ImItemCurrentPriceView> findCurrentPriceByValue(String brandCode, String startItemCode, 
			String endItemCode, String priceType, String categorySearchString) {
		try {

			return imItemCurrentPriceViewDAO.findCurrentPriceByValue(brandCode, startItemCode, endItemCode, priceType, categorySearchString);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<ImItemPriceView> findPriceViewByValue(String brandCode,
			String startItemCode, String endItemCode, String startDate,
			String endDate, String priceType, String priceEnable,
			String employeeCode, String categorySearchString) {
		try {
			return imItemPriceViewDAO.findPriceViewByValue(brandCode, startItemCode, endItemCode, 
					startDate, endDate, priceType,priceEnable, employeeCode, categorySearchString);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ImItemCurrentPriceView findCurrentPriceByValue(String brandCode, String itemCode, String priceType) {
		try {
			return imItemCurrentPriceViewDAO.findCurrentPriceByValue(brandCode,
					itemCode, priceType);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ImItemPrice findByItemTypeEnableDate(String brandCode, String itemCode,String typeCode) {
		return imItemPriceDAO.findByItemTypeEnableDate(brandCode, itemCode, typeCode);
	}
	
	public Double getUnitPriceByDate(String brandCode, String itemCode, Date priceDate) throws Exception{
		Double unitPrice = 0D;
		try{
			String priceDateStr = DateUtils.format(priceDate,DateUtils.C_DATA_PATTON_YYYYMMDD);
			String sql = " select ERP.GET_UNIT_PRICE_BY_DATE ('"+brandCode+"', '"+itemCode+"'," +
					" to_date('"+priceDateStr+"','yyyyMMdd') ) from dual " ;
			List unitPriceList = nativeQueryDAO.executeNativeSql(sql);
			if(null != unitPriceList && unitPriceList.size() > 0){
				Object obj = unitPriceList.get(0);
				unitPrice = NumberUtils.round(((BigDecimal)obj).doubleValue(),2);
			}
			return unitPrice;
		}catch(Exception ex){
			throw(ex);
		}
	}
	
}
