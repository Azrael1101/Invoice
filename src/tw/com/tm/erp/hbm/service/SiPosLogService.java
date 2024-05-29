package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.SiPosLog;
import tw.com.tm.erp.hbm.bean.SiPosLogId;
import tw.com.tm.erp.hbm.bean.SoPostingTally;
import tw.com.tm.erp.hbm.bean.SoPostingTallyId;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopMachineDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.SiPosLogDAO;
import tw.com.tm.erp.hbm.dao.SoPostingTallyDAO;
import tw.com.tm.erp.utils.DateUtils;

public class SiPosLogService {

	private static final Log log = LogFactory.getLog(SiPosLogService.class);

	public static final String UNPOST = "N";

	public static final String PROCESS_NAME_SOP_UP = "SOP_UP";

	public static final String PROCESS_NAME_MOVE_UP = "MOVE_UP";
	
        public static final String PROCESS_NAME_CUSTOMER_DL = "CUSTOMER_DL";
	
	public static final String PROCESS_NAME_SALES_DL = "SALES_DL";
	
	public static final String PROCESS_NAME_GOODSQTY_DL = "GOODSQTY_DL";
	
	public static final String PROCESS_NAME_GOODSLIST_DL = "GOODSLIST_DL";
	
	public static final String PROCESS_NAME_PROMOTION_DL = "PROMOTION_DL";
	
	public static final String PROCESS_NAME_MOVE_DL = "MOVE_DL";
	
	public static final String PROCESS_NAME_EANCODE_DL = "EANCODE_DL";
	
	public static final String PROCESS_NAME_ITEM_DISCOUNT_DL = "ITEM_DISCOUNT_DL";
	
	public static final String PROCESS_NAME_ITEM_ON_HAND_DL = "ITEM_ON_HAND_DL";
	
	//Steve 下傳幣別
	public static final String PROCESS_NAME_CURRENCY_DL = "CURRENCY_DL";
	
	//Steve 下傳匯率
	public static final String PROCESS_NAME_EXCHANGE_RATE_DL = "EXCHANGE_RATE_DL";
        
	//Caspar 下傳電子標籤
	public static final String PROCESS_NAME_ITEM_TAG_DL = "ITEM_TAG_DL";
	
	//Mark
	public static final String PROCESS_NAME_COMBINE_DL = "COMBINE_DL";
	
	//Yao
	public static final String PROCESS_NAME_GROUPED_DL = "GROUPED_DL";
	
	//Yao
	public static final String PROCESS_NAME_FULL_DL = "FULL_DL";
	
	//MACO EC 20170831
	public static final String PROCESS_NAME_EC_GOODSLIST_DL = "EC_GOODSLIST_DL";

	private SiPosLogDAO siPosLogDAO;

	private SoPostingTallyDAO soPostingTallyDAO;

	private NativeQueryDAO nativeQueryDAO;

	private BuShopDAO buShopDAO;

	private BuBrandDAO buBrandDAO;

	private BuShopMachineDAO buShopMachineDAO;

	/**
	 * 依據日期建立所有 Shop Code 的Pos Log (還不會過濾不同SHOP 不同日期 那些不用建)
	 * 
	 * @param transactionDate
	 * @param brandCode
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void createPosLogByDate(Date transactionDate, String processName, String createdBy) throws IllegalAccessException,
			InvocationTargetException {
		log.info("SiPosLogService.createPosLogByDate");
		// 取得還沒有建LOG的所有日期
		List<Date> notCreateDates = new ArrayList();
		// 取得建立的最後一筆日期
		String queryLastDateSql = "SELECT MAX(TRANSACTION_DATE) FROM SI_POS_LOG";
		List queryLastDateResult = nativeQueryDAO.executeNativeSql(queryLastDateSql);
		Date lastDate = (Date) queryLastDateResult.get(0);
		if (null != lastDate) {
			int dates = DateUtils.daysBetweenWithoutTime(lastDate, transactionDate);
			for (int index = 0; index < dates; index++) {
				log.info("SiPosLogService.createPosLogByDate add date " + DateUtils.addDays(lastDate, index + 1));
				notCreateDates.add(DateUtils.addDays(lastDate, index + 1));
			}
		} else {
			log.info("SiPosLogService.createPosLogByDate add date " + new Date());
			notCreateDates.add(new Date());
		}
		// 呼叫createPosLogByDates
		createPosLogByDates(notCreateDates, processName, createdBy);
	}

	/**
	 * 依據日期建立所有 Shop Code 的Pos Log
	 * 
	 * @param transactionDates
	 * @param brandCode
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void createPosLogByDates(List<Date> transactionDates, String processName, String createdBy) throws IllegalAccessException,
			InvocationTargetException {
		log.info("SiPosLogService.createPosLogByDates");

		List buBrands = buBrandDAO.findByProperty("enable", "Y");
		for (int index = 0; index < buBrands.size(); index++) {
			BuBrand buBrand = (BuBrand) buBrands.get(index);
			String brandCode = buBrand.getBrandCode();
			List<SiPosLog> siPosLogs = new ArrayList();
			for (Date transactionDate : transactionDates) {
				// 取得所有的SHOP
				List<BuShop> buShops = buShopDAO.findShopByBrandAndEnable(brandCode, "Y");
				for (BuShop buShop : buShops) {
					String shopCode = buShop.getShopCode();

					List<BuShopMachine> buShopMachines = buShopMachineDAO.findByShopCode(shopCode);

					for (BuShopMachine buShopMachine : buShopMachines) {
						SiPosLogId siPosLogId = new SiPosLogId();
						siPosLogId.setShopCode(shopCode);
						siPosLogId.setTransactionDate(transactionDate);
						siPosLogId.setProcessName(processName);
						siPosLogId.setPosMachineCode(buShopMachine.getId().getPosMachineCode());

						SiPosLog siPosLog = new SiPosLog();

						siPosLog.setBrandCode(brandCode);
						siPosLog.setCreateDate(new Date());
						siPosLog.setCreatedBy(createdBy);
						// siPosLog.setHeadFileName(headFileName);
						siPosLog.setId(siPosLogId);
						siPosLog.setLastUpdateDate(new Date());
						siPosLog.setLastUpdatedBy(createdBy);
						// siPosLog.setLineFileName(lineFileName);
						siPosLog.setStatus(OrderStatus.SAVE);

						siPosLogs.add(siPosLog);
					}

				}
			}

			// 建立資料
			for (SiPosLog siPosLog : siPosLogs) {
				siPosLogDAO.save(siPosLog);

				SoPostingTallyId soPostingTallyId = new SoPostingTallyId();
				SoPostingTally soPostingTally = new SoPostingTally();

				SiPosLogId siPosLogId = siPosLog.getId();
				soPostingTallyId.setShopCode(siPosLogId.getShopCode());
				soPostingTallyId.setTransactionDate(siPosLogId.getTransactionDate());

				soPostingTally.setBrandCode(brandCode);
				soPostingTally.setCreateDate(siPosLog.getCreateDate());
				soPostingTally.setCreatedBy(createdBy);
				soPostingTally.setIsPosting("N");
				soPostingTally.setLastUpdateDate(siPosLog.getLastUpdateDate());
				soPostingTally.setLastUpdatedBy(siPosLog.getLastUpdatedBy());
				soPostingTally.setId(soPostingTallyId);

				// 呼叫createPoPostingTally
				createPoPostingTally(soPostingTally);
			}

		}

	}


	/**
	 * update or save log
	 * @param siPosLog
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void createPosLog(SiPosLog siPosLog) throws IllegalAccessException, InvocationTargetException {
		if (null != siPosLog && null != siPosLog.getId() ) {
			SiPosLog siPosLogDB = siPosLogDAO.findById(siPosLog.getId());
			if (null != siPosLogDB) {			    	
			    	siPosLogDB.setLastUpdateDate(new Date());
			    	BeanUtils.copyProperties(siPosLogDB, siPosLog);
				siPosLogDAO.update(siPosLogDB);
			}else{
			    siPosLog.setCreateDate(new Date());
			    siPosLog.setLastUpdateDate(new Date());
			    siPosLogDAO.save(siPosLog);
			}
		}else{
		    
		}
	}
	
	/**
	 * update log when write log finish
	 * 
	 * @param shopCode
	 * @param transactionDate
	 * @param lastUpdatedBy
	 * @param headFileName
	 * @param lineFileName
	 */
	public void updatePosLogFinish(String shopCode, Date transactionDate, String processName, String posMachineCode) {
		transactionDate = DateUtils.getDateWithoutTime(transactionDate);

		if (null != transactionDate) {
			SiPosLogId siPosLogId = new SiPosLogId();
			siPosLogId.setShopCode(shopCode);
			siPosLogId.setTransactionDate(transactionDate);
			siPosLogId.setProcessName(processName);
			siPosLogId.setPosMachineCode(posMachineCode);

			SiPosLog siPosLog = siPosLogDAO.findById(siPosLogId);
			if (null != siPosLog) {
				siPosLog.setLastUpdateDate(new Date());
				siPosLog.setStatus(OrderStatus.FINISH);
				siPosLogDAO.update(siPosLog);
			}
		}
	}
	
	/**
	 * update log when write log finish
	 * 
	 * @param shopCode
	 * @param transactionDate
	 * @param lastUpdatedBy
	 * @param headFileName
	 * @param lineFileName
	 */
	public void updatePosLogFinish(String brandCode, String shopCode, Date transactionDate, String processName, String posMachineCode, 
		String headFileName, String lineFileName, String lastUpdatedBy) {
		transactionDate = DateUtils.getDateWithoutTime(transactionDate);

		if (null != transactionDate) {		        
	                SiPosLogId siPosLogId = new SiPosLogId();
			siPosLogId.setShopCode(shopCode);
			siPosLogId.setTransactionDate(transactionDate);
			siPosLogId.setProcessName(processName);
			siPosLogId.setPosMachineCode(posMachineCode);
			SiPosLog siPosLog = siPosLogDAO.findById(siPosLogId);
		       
			if (siPosLog != null) {			   
			        siPosLog.setHeadFileName(headFileName);
			        siPosLog.setLineFileName(lineFileName);
				siPosLog.setLastUpdateDate(new Date());
				siPosLog.setLastUpdatedBy(lastUpdatedBy);
				siPosLog.setStatus(OrderStatus.FINISH);
				siPosLogDAO.update(siPosLog);				
			}
		}
	}

	/**
	 * 
	 * @param shopCode
	 * @param transactionDate
	 * @param processName
	 * @param headFileName
	 * @param lineFileName
	 * @param posMachineCode
	 * @param createdBy
	 * @param brandCode
	 */
	public void createPosLog(String shopCode, Date transactionDate, String processName, String headFileName, String lineFileName,
			String posMachineCode, String createdBy, String brandCode) {
		transactionDate = DateUtils.getDateWithoutTime(transactionDate);

		if (null != transactionDate) {
			SiPosLogId siPosLogId = new SiPosLogId();
			siPosLogId.setShopCode(shopCode);
			siPosLogId.setTransactionDate(transactionDate);
			siPosLogId.setProcessName(processName);
			siPosLogId.setPosMachineCode(posMachineCode);
			SiPosLog siPosLog = siPosLogDAO.findById(siPosLogId);
			if (null != siPosLog) {
				siPosLog.setHeadFileName(headFileName);
				siPosLog.setLineFileName(lineFileName);
				siPosLog.setLastUpdateDate(new Date());
				siPosLog.setLastUpdatedBy(createdBy);
				siPosLog.setStatus(OrderStatus.SAVE);
				siPosLogDAO.update(siPosLog);
			} else {
			    	siPosLog = new SiPosLog(); 
				siPosLog.setBrandCode(brandCode);
				siPosLog.setCreateDate(new Date());
				siPosLog.setCreatedBy(createdBy);
				siPosLog.setHeadFileName(headFileName);
				siPosLog.setId(siPosLogId);
				siPosLog.setLastUpdateDate(new Date());
				siPosLog.setLastUpdatedBy(createdBy);
				siPosLog.setLineFileName(lineFileName);
				siPosLog.setStatus(OrderStatus.SAVE);
				siPosLogDAO.save(siPosLog);
			}
		}
	}

	/**
	 * 
	 * @param shopCode
	 * @param transactionDate
	 * @param headFileName
	 * @param lineFileName
	 */
	public void updatePosLogFile(String shopCode, Date transactionDate, String processName, String headFileName, String lineFileName,
			String posMachineCode) {
		transactionDate = DateUtils.getDateWithoutTime(transactionDate);

		if (null != transactionDate) {
			SiPosLogId siPosLogId = new SiPosLogId();
			siPosLogId.setShopCode(shopCode);
			siPosLogId.setTransactionDate(transactionDate);
			siPosLogId.setProcessName(processName);
			siPosLogId.setPosMachineCode(posMachineCode);

			SiPosLog siPosLog = siPosLogDAO.findById(siPosLogId);
			if (null != siPosLog) {
				siPosLog.setHeadFileName(headFileName);
				siPosLog.setLastUpdateDate(new Date());
				siPosLog.setLineFileName(lineFileName);
				siPosLogDAO.update(siPosLog);
			}
		}
	}

	/**
	 * 依據日期建立所有 Shop Code 的PostingTally
	 * 
	 * @param transactionDates
	 * @param brandCode
	 */
	private void createPoPostingTally(SoPostingTally soPostingTally) {
		if (null != soPostingTally)
			soPostingTallyDAO.save(soPostingTally);
	}

	public void setSiPosLogDAO(SiPosLogDAO siPosLogDAO) {
		this.siPosLogDAO = siPosLogDAO;
	}

	public void setSoPostingTallyDAO(SoPostingTallyDAO soPostingTallyDAO) {
		this.soPostingTallyDAO = soPostingTallyDAO;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}

	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setBuShopMachineDAO(BuShopMachineDAO buShopMachineDAO) {
		this.buShopMachineDAO = buShopMachineDAO;
	}
}
