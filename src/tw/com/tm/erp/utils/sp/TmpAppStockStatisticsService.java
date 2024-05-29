package tw.com.tm.erp.utils.sp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.dao.BuCountryDAO;
import tw.com.tm.erp.hbm.dao.TmpAppStockStatisticsDAO;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.view.Barcode;
import tw.com.tm.erp.utils.DateUtils;

/**
 * @author T15394
 *
 */
/**
 * @author T15394
 *
 */
public class TmpAppStockStatisticsService {

	private static final Log log = LogFactory.getLog(TmpAppStockStatisticsService.class);

	private TmpAppStockStatisticsDAO tmpAppStockStatisticsDAO;

	/* Spring IoC */
	public void setTmpAppStockStatisticsDAO(TmpAppStockStatisticsDAO tmpAppStockStatisticsDAO) {
		this.tmpAppStockStatisticsDAO = tmpAppStockStatisticsDAO;
	}

	private ImItemService imItemService;

	public void setImItemService(ImItemService imItemService) {
		this.imItemService = imItemService;
	}

	private BuCountryDAO buCountryDAO;

	public void setBuCountryDAO(BuCountryDAO buCountryDAO) {
		this.buCountryDAO = buCountryDAO;
	}


	public List getStockStatistics(HashMap conditionMap) throws Exception {
		String brandCode = null;
		String onHandDate = null;
		String warehouseCode = null;
		try {
			brandCode = (String) conditionMap.get("brandCode");
			warehouseCode = (String) conditionMap.get("warehouseCode");
			onHandDate = (String) conditionMap.get("onHandDate");
			return tmpAppStockStatisticsDAO.getStockStatistics(conditionMap);
		} catch (Exception ex) {
			log.error("查詢品牌代號：" + brandCode + "、庫別：" + warehouseCode + "、日期：" + onHandDate + "的庫存量時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢品牌代號：" + brandCode + "、庫別：" + warehouseCode + "、日期：" + onHandDate + "的庫存量時發生錯誤，原因：" + ex.getMessage());
		}
	}

	public List getStockStatisticsForBarcode(HashMap conditionMap) throws Exception {
		log.info("getStockStatisticsForBarcode");
		String brandCode = null;
		String onHandDate = null;
		String warehouseCode = null;
		List barcodes = new ArrayList(0);
		try {
			brandCode = (String) conditionMap.get("brandCode");
			warehouseCode = (String) conditionMap.get("warehouseCode");
			onHandDate = (String) conditionMap.get("onHandDate");
			barcodes = tmpAppStockStatisticsDAO.getStockStatisticsForBarcode(conditionMap);
			for (Iterator iterator = barcodes.iterator(); iterator.hasNext();) {
				Barcode barcode = (Barcode) iterator.next();
				ImItem item = imItemService.findItem(brandCode, barcode.getItemCode());
				if (null != item) {
					if(null != item.getCategory14()){
						BuCountry buCountry = buCountryDAO.findById(item.getCategory14());
						if(null != buCountry)
							barcode.setCategory14(buCountry.getCountryCName());
					}
					barcode.setCategory08(item.getCategory08());
				}
			}
			return barcodes;
		} catch (Exception ex) {
			log.error("查詢品牌代號：" + brandCode + "、庫別：" + warehouseCode + "、日期：" + onHandDate + "的庫存量時發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢品牌代號：" + brandCode + "、庫別：" + warehouseCode + "、日期：" + onHandDate + "的庫存量時發生錯誤，原因：" + ex.getMessage());
		}
	}
}
