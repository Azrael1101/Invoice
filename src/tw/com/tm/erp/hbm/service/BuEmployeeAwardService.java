package tw.com.tm.erp.hbm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeAward;
import tw.com.tm.erp.hbm.bean.BuEmployeeAwardCategory;
import tw.com.tm.erp.hbm.bean.BuEmployeeAwardRank;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.PosEmployee;
import tw.com.tm.erp.hbm.bean.PosEmployeeAward;
import tw.com.tm.erp.hbm.bean.PosEmployeeAwardCategory;
import tw.com.tm.erp.hbm.bean.PosEmployeeAwardRank;
import tw.com.tm.erp.hbm.bean.PosItemDiscount;
import tw.com.tm.erp.hbm.dao.BuEmployeeAwardDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class BuEmployeeAwardService {
	private static final Log log = LogFactory.getLog(BuEmployeeAwardService.class);

	private BuEmployeeAwardDAO buEmployeeAwardDAO;

	public void setBuEmployeeAwardDAO(BuEmployeeAwardDAO buEmployeeAwardDAO) {
		this.buEmployeeAwardDAO = buEmployeeAwardDAO;
	}

	private PosExportDAO posExportDAO;

	public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}

	/**
	 * 更新
	 * @param bean
	 * @throws Exception
	 */
	public void updateIndexNo(String importEmployeeCode) throws Exception{
		List<BuEmployeeAward> selectBuEmployeeAwards = (List<BuEmployeeAward> )buEmployeeAwardDAO.findFirstByProperty("BuEmployeeAward", "and employeeCode = ?", new Object[]{importEmployeeCode});
		Long indexNo = 1L;
		for (BuEmployeeAward buEmployeeAward : selectBuEmployeeAwards) {
			buEmployeeAward.setIndexNo(indexNo++);
			buEmployeeAwardDAO.update(buEmployeeAward);
		}
	}

	/**
	 * 更新
	 * @param bean
	 * @throws Exception
	 */
	public void update(BuEmployeeAward bean) throws Exception{
		try {
			buEmployeeAwardDAO.update(bean);
		} catch (Exception e) {
			throw new Exception("更新學習護照發生問題" + e.getMessage());
		}

	}

	/**
	 * 新增
	 * @param bean
	 * @throws Exception
	 */
	public void save(BuEmployeeAward bean) throws Exception{
		try {
			buEmployeeAwardDAO.save(bean);
		} catch (Exception e) {
			throw new Exception("新增學習護照發生問題" + e.getMessage());
		}

	}

	/**
	 * 新增
	 * @param bean
	 * @throws Exception
	 */
	public void delete(BuEmployeeAward bean) throws Exception{
		try {
			buEmployeeAwardDAO.delete(bean);
		} catch (Exception e) {
			throw new Exception("新增學習護照發生問題" + e.getMessage());
		}

	}

	/**
	 * 透過傳遞過來的參數來做EmployeeAward
	 * @param parameterMap
	 * @throws Exception
	 */
	public Long executePosEMPAExport(HashMap parameterMap) throws Exception{
		log.info("executePosEMPAExport");
		Long responseId = -1L;
		Long numbers = 0L;

		//一、解析程式需要排程下傳或是即時下傳
		Long batchId = (Long)parameterMap.get("BATCH_ID");
		String uuId = posExportDAO.getDataId();// 產生dataId

		//二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
		if(null == batchId || batchId <= 0){
			//輸入搜尋條件(排程)
			parameterMap.put("dataDate", DateUtils.format( (Date)parameterMap.get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			parameterMap.put("dataDateEnd", DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			parameterMap.put("enable", "Y");
			List results = buEmployeeAwardDAO.findEMPAbyCondition(parameterMap);
			if(results != null && results.size() >= 0){
		        for (Object result : results) {
		        	BuEmployeeAward buEmployeeAward = (BuEmployeeAward)result;
			        PosEmployeeAward posEmployeeAward = new PosEmployeeAward();
			        BeanUtils.copyProperties(buEmployeeAward, posEmployeeAward);
				    posEmployeeAward.setDataId(uuId);
				    posEmployeeAward.setAction("U");
			        posExportDAO.save(posEmployeeAward);
		        }
			}
		}else{
			//非排程則是把DataId找出，再去POS_CUSTOMER依據Data_Id把資訊船進去
			String dataId = (String)parameterMap.get("DATA_ID");
			//尋找PosCustomer中此dataID有哪些需求資料
			List<PosEmployeeAward> posEmployeeAwards = posExportDAO.findByProperty("PosEmployeeAward", new String[]{"dataId"}, new Object[]{dataId});
			for (Iterator iterator = posEmployeeAwards.iterator(); iterator.hasNext();) {
				PosEmployeeAward posEmployeeAward = (PosEmployeeAward) iterator.next();
				HashMap conditionMap = new HashMap();
				conditionMap.put("employeeCode", posEmployeeAward.getEmployeeCode());
				conditionMap.put("enable", "Y");
				//將每一筆資料進資料庫查，再補全資料
				List results = buEmployeeAwardDAO.findEMPAbyCondition(conditionMap);
				long indexNo = 1L;
				if(results != null && results.size() >= 0){
			        for (Object result : results) {
			        	BuEmployeeAward buEmployeeAward = (BuEmployeeAward)result;
				        PosEmployeeAward newPosEmployeeAward = new PosEmployeeAward();
				        BeanUtils.copyProperties(buEmployeeAward, newPosEmployeeAward);
				        newPosEmployeeAward.setDataId(uuId);
				        newPosEmployeeAward.setAction("U");
				        newPosEmployeeAward.setIndexNo(indexNo++);
				        posExportDAO.save(newPosEmployeeAward);
			        }
				}
			}
		}

		//更新新的DATA_ID做回傳
		parameterMap.put("DATA_ID", uuId);
		parameterMap.put("NUMBERS", numbers);
		responseId = posExportDAO.executeCommand(parameterMap);
		return responseId;
	}
	
	/**
	 * 透過傳遞過來的參數來做EmployeeAwardCategory
	 * @param parameterMap
	 * @throws Exception
	 */
	public Long executePosEMPACExport(HashMap parameterMap) throws Exception{
		log.info("executePosEMPACExport");
		Long responseId = -1L;
		Long numbers = 0L;

		//一、解析程式需要排程下傳或是即時下傳
		Long batchId = (Long)parameterMap.get("BATCH_ID");
		String uuId = posExportDAO.getDataId();// 產生dataId

		//二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
		if(null == batchId || batchId <= 0){
			//輸入搜尋條件(排程)
			parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
			List results = buEmployeeAwardDAO.findEMPACbyCondition(parameterMap);
			if(results != null && results.size() >= 0){
		        for (Object result : results) {
		        	BuEmployeeAwardCategory buEmployeeAwardCategory = (BuEmployeeAwardCategory)result;
			        PosEmployeeAwardCategory posEmployeeAwardCategory = new PosEmployeeAwardCategory();
			        BeanUtils.copyProperties(buEmployeeAwardCategory, posEmployeeAwardCategory);
			        posEmployeeAwardCategory.setDataId(uuId);
			        posEmployeeAwardCategory.setAction("U");
			        posExportDAO.save(posEmployeeAwardCategory);
		        }
			}
		}else{
			//非排程則是把DataId找出，再去POS_CUSTOMER依據Data_Id把資訊船進去
			String dataId = (String)parameterMap.get("DATA_ID");
			//尋找PosCustomer中此dataID有哪些需求資料
			List<PosEmployeeAwardCategory> posEmployeeAwardCategorys = posExportDAO.findByProperty("PosEmployeeAwardCategory", new String[]{"dataId"}, new Object[]{dataId});
			for (Iterator iterator = posEmployeeAwardCategorys.iterator(); iterator.hasNext();) {
				PosEmployeeAwardCategory posEmployeeAwardCategory = (PosEmployeeAwardCategory) iterator.next();
				HashMap conditionMap = new HashMap();
				conditionMap.put("brandCode", posEmployeeAwardCategory.getBrandCode());
				//將每一筆資料進資料庫查，再補全資料
				List results = buEmployeeAwardDAO.findEMPACbyCondition(conditionMap);
				if(results != null && results.size() >= 0){
			        for (Object result : results) {
			        	BuEmployeeAwardCategory buEmployeeAwardCategory = (BuEmployeeAwardCategory)result;
				        PosEmployeeAwardCategory newPosEmployeeAwardCategory = new PosEmployeeAwardCategory();
				        BeanUtils.copyProperties(buEmployeeAwardCategory, newPosEmployeeAwardCategory);
				        newPosEmployeeAwardCategory.setDataId(uuId);
				        newPosEmployeeAwardCategory.setAction("U");
				        posExportDAO.save(newPosEmployeeAwardCategory);
			        }
				}
			}
		}

		//更新新的DATA_ID做回傳
		parameterMap.put("DATA_ID", uuId);
		parameterMap.put("NUMBERS", numbers);
		responseId = posExportDAO.executeCommand(parameterMap);
		return responseId;
	}
	
	/**
	 * 透過傳遞過來的參數來做EmployeeAwardRank
	 * @param parameterMap
	 * @throws Exception
	 */
	public Long executePosEMPARExport(HashMap parameterMap) throws Exception{
		log.info("executePosEMPARExport");
		Long responseId = -1L;
		Long numbers = 0L;

		//一、解析程式需要排程下傳或是即時下傳
		Long batchId = (Long)parameterMap.get("BATCH_ID");
		String uuId = posExportDAO.getDataId();// 產生dataId

		//二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
		if(null == batchId || batchId <= 0){
			
		}else{
			//非排程則是把DataId找出，再去POS_CUSTOMER依據Data_Id把資訊船進去
			String dataId = (String)parameterMap.get("DATA_ID");
			//尋找PosCustomer中此dataID有哪些需求資料
			List<PosEmployeeAwardRank> posEmployeeAwardRanks = posExportDAO.findByProperty("PosEmployeeAwardRank", new String[]{"dataId"}, new Object[]{dataId});
			for (Iterator iterator = posEmployeeAwardRanks.iterator(); iterator.hasNext();) {
				PosEmployeeAwardRank posEmployeeAwardRank = (PosEmployeeAwardRank) iterator.next();
				String employeeDepartment = posEmployeeAwardRank.getEmployeeDepartment();
				HashMap conditionMap = new HashMap();
				conditionMap.put("employeeDepartment", employeeDepartment);
				//將每一筆資料進資料庫查，再補全資料
				List results = buEmployeeAwardDAO.findEMPARbyCondition(conditionMap);
				for (Iterator iterator2 = results.iterator(); iterator2.hasNext();) {
					Object[] obj = (Object[])iterator2.next();
					PosEmployeeAwardRank newPosEmployeeAwardRank = new PosEmployeeAwardRank();
					newPosEmployeeAwardRank.setEmployeeDepartment(employeeDepartment);
					newPosEmployeeAwardRank.setEmployeeCode((String)obj[1]);
					newPosEmployeeAwardRank.setEmployeeName((String)obj[2]);
					newPosEmployeeAwardRank.setEmployeePoint(NumberUtils.round(((BigDecimal)obj[3]).doubleValue(),2));
					newPosEmployeeAwardRank.setEmployeeRank((String)obj[4]);
					newPosEmployeeAwardRank.setDataId(uuId);
					newPosEmployeeAwardRank.setAction("U");
					posExportDAO.save(newPosEmployeeAwardRank);
				}
			}
		}

		//更新新的DATA_ID做回傳
		parameterMap.put("DATA_ID", uuId);
		parameterMap.put("NUMBERS", numbers);
		responseId = posExportDAO.executeCommand(parameterMap);
		return responseId;
	}
}
