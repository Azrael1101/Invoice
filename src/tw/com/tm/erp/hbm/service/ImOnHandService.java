package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.ImItemDiscount;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImOnHandId;
import tw.com.tm.erp.hbm.bean.ImWarehouseQuantityView;
import tw.com.tm.erp.hbm.bean.PosCustomer;
import tw.com.tm.erp.hbm.bean.PosImOnHand;
import tw.com.tm.erp.hbm.bean.PosItemDiscount;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseQuantityViewDAO;
import tw.com.tm.erp.hbm.dao.PosExportDAO;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

import org.springframework.util.StringUtils;

public class ImOnHandService {
	private static final Log log = LogFactory.getLog(ImOnHandService.class);
	
	ImOnHandDAO imOnHandDAO;
	ImWarehouseQuantityViewDAO imWarehouseQuantityViewDAO;
	private PosExportDAO posExportDAO;
	
	public void setImOnHandDAO(ImOnHandDAO imOnHandDAO) {
		this.imOnHandDAO = imOnHandDAO;
	}

	public void setImWarehouseQuantityViewDAO(ImWarehouseQuantityViewDAO imWarehouseQuantityViewDAO) {
		this.imWarehouseQuantityViewDAO = imWarehouseQuantityViewDAO;
	}

	public ImOnHand findById(ImOnHandId id) {
		return imOnHandDAO.findById(id);
	}
	
	public void setPosExportDAO(PosExportDAO posExportDAO) {
		this.posExportDAO = posExportDAO;
	}

	/**
	 * 以批號前四碼為條件,取得相同前四碼裡面的最大序號值
	 * 
	 * @param subLotNo
	 *            批號前四碼
	 * @return 有符合條件的則回傳最大批號,反之則回傳null
	 */
	public String findMaxLotNo(final String subLotNo) {
		List<ImOnHand> list = (List<ImOnHand>) imOnHandDAO.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				List<ImOnHand> onHands = session.createCriteria(ImOnHand.class).add(Restrictions.like("id.lotNo", subLotNo + "%"))
						.addOrder(Order.desc("id.lotNo")).list();
				return onHands;
			}
		});
		if (list.size() > 0) {
			return list.get(0).getId().getLotNo();
		} else {
			return null;
		}
	}

	/**
	 * 以 organizationId, locationId, itemId 為條件,查詢庫存產品相關資料
	 * 
	 * @param organizationId
	 *            is Organization ID
	 * @param locationId
	 *            is Location ID
	 * @param itemId
	 *            is Item ID
	 * @return List of ImWarehouseQuantityView Object
	 */
	public List<ImWarehouseQuantityView> findByOrganizationIdAndLocationIdAndItemId(final long organizationId, final long locationId,
			final long itemId) {
		List<ImWarehouseQuantityView> list = (List<ImWarehouseQuantityView>) imWarehouseQuantityViewDAO.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						List<ImWarehouseQuantityView> view = session.createCriteria(ImWarehouseQuantityView.class).add(
								Restrictions.eq("organizationId", organizationId)).add(Restrictions.eq("locationId", locationId)).add(
								Restrictions.eq("itemId", itemId)).addOrder(Order.asc("lotNo")).list();
						return view;
					}
				});
		return list;
	}

	/**
	 * 以 organizationId, locationId, itemId 為條件,查詢庫存產品相關資料,<br>
	 * 並且 stockOnHandQty 需大於 uncommitQty (即有可用庫存)
	 * 
	 * @param organizationCode
	 *            is Organization Code
	 * @param locationId
	 *            is Location ID
	 * @param itemCode
	 *            is Item Code
	 * @return List of ImWarehouseQuantityView Object
	 */
	public List<ImWarehouseQuantityView> findAvailableQuantity(final String organizationCode, final long locationId, final String itemCode) {
		List<ImWarehouseQuantityView> list = (List<ImWarehouseQuantityView>) imWarehouseQuantityViewDAO.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						List<ImWarehouseQuantityView> view = session.createCriteria(ImWarehouseQuantityView.class).add(
								Restrictions.eq("organizationCode", organizationCode)).add(Restrictions.eq("locationId", locationId)).add(
								Restrictions.eq("itemCode", itemCode)).add(Restrictions.gtProperty("stockOnHandQty", "uncommitQty"))
								.addOrder(Order.asc("lotNo")).list();
						return view;
					}
				});
		return list;
	}

	/**
	 * 以 locationId, itemId 為條件,查詢庫存產品相關資料,<br>
	 * 並且 stockOnHandQty 需大於 uncommitQty (即有可用庫存)
	 * 
	 * @param locationId
	 *            is Location ID
	 * @param itemCode
	 *            is Item CODE
	 * @return List of ImWarehouseQuantityView Object
	 */
	public List<ImWarehouseQuantityView> findAvailableQuantity(final long locationId, final String itemCode) {
		List<ImWarehouseQuantityView> list = (List<ImWarehouseQuantityView>) imWarehouseQuantityViewDAO.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						List<ImWarehouseQuantityView> view = session.createCriteria(ImWarehouseQuantityView.class).add(
								Restrictions.eq("locationId", locationId)).add(Restrictions.eq("itemCode", itemCode)).add(
								Restrictions.gtProperty("stockOnHandQty", "uncommitQty")).addOrder(Order.asc("lotNo")).list();
						return view;
					}
				});
		return list;
	}

	/**
	 * 以itemId 為條件,查詢庫存產品相關資料
	 * 
	 * @param itemCode
	 *            is Item CODE
	 * @return List of ImWarehouseQuantityView Object
	 */
	public List<ImWarehouseQuantityView> findAvailableQuantityByItemCode(final String itemCode) {
		List<ImWarehouseQuantityView> list = (List<ImWarehouseQuantityView>) imWarehouseQuantityViewDAO.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) {
						List<ImWarehouseQuantityView> view = session.createCriteria(ImWarehouseQuantityView.class).add(
								Restrictions.eq("itemCode", itemCode)).addOrder(Order.asc("lotNo")).list();
						return view;
					}
				});
		return list;
	}

	/**
	 * 依據organizationCode、itemCode、warehouseCode、lotNo查詢出目前可用庫存
	 * 
	 * @param organizationCode
	 * @param itemCode
	 * @param warehouseCode
	 * @param lotNo
	 * @return Double
	 */
	public Double getCurrentStockOnHandQty(String organizationCode, String itemCode, String warehouseCode, String lotNo) 
	        throws ValidationErrorException{

		itemCode = itemCode.trim().toUpperCase();
		if (warehouseCode != null) {
			warehouseCode.trim().toUpperCase();
			if (lotNo != null) {
				lotNo.trim().toUpperCase();
				return imOnHandDAO.getCurrentStockOnHandQty(organizationCode, itemCode, warehouseCode, lotNo);
			} else {
				return imOnHandDAO.getCurrentStockOnHandQty(organizationCode, itemCode, warehouseCode);
			}
		}

		return imOnHandDAO.getCurrentStockOnHandQty(organizationCode, itemCode);
	}

	/**
	 * 依據organizationCode、warehouseCode 查詢出 warehouse 的商品數量 20081012 shan add
	 * 
	 * @param organizationCode
	 * @param warehouseCode
	 * @return Double
	 */
	public Double getWarehouseCurrentStockOnHandQty(String organizationCode, String warehouseCode) throws ValidationErrorException{
		if (StringUtils.hasText(organizationCode) && StringUtils.hasText(warehouseCode)) {
			return imOnHandDAO.getWarehouseCurrentStockOnHandQty(organizationCode, warehouseCode);
		}
		return 0D;
	}

	/**
	 * 
	 * @param organizationCode
	 * @param warehouseCodes
	 *            可以是多筆的,不過要加上''
	 * @return
	 */
	public List findAllBrandShopOnHand(String organizationCode, String warehouseCodes) {
		log.info("ImOnHandService.findAllBrandShopOnHand");
		if (StringUtils.hasText(organizationCode) && StringUtils.hasText(warehouseCodes)) {
			List re = imOnHandDAO.findAllBrandShopOnHand(organizationCode, warehouseCodes);
			return re;
		}
		return null;
	}
	
    /**
     * 透過傳遞過來的參數來做ImOnHand下傳
     * @param parameterMap
     * @throws Exception
     */
    public Long executePosExport(HashMap parameterMap) throws Exception{
    	log.info("executePosExport");
    	Long responseId = 0L;
    	Long numbers = 0L;
    	
    	//一、解析程式需要排程下傳或是即時下傳
    	Long batchId = (Long)parameterMap.get("BATCH_ID");
    	String uuId = posExportDAO.getDataId();// 產生dataId
		
		
    	//二、下傳程式至POS_CUSTOMER (產生DataId , ResponseId)
		if(null == batchId || batchId <= 0){
			//輸入搜尋條件(排程)
			parameterMap.put("brandCode", parameterMap.get("BRAND_CODE"));
			parameterMap.put("dataDate", DateUtils.format( (Date)parameterMap.get("DATA_DATE_STRAT"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			parameterMap.put("dataDateEnd", DateUtils.format( (Date)parameterMap.get("DATA_DATE_END"), DateUtils.C_DATA_PATTON_YYYYMMDD));
			List<ImOnHand> results = imOnHandDAO.findImOnHandByCondition(parameterMap);
			if(results != null && results.size() >= 0){
		        for (ImOnHand result : results) {
		        	ImOnHand imOnHand = (ImOnHand)result;
			        PosImOnHand posImOnHand = new PosImOnHand();
			        BeanUtils.copyProperties(imOnHand.getId(), posImOnHand);
			        BeanUtils.copyProperties(imOnHand, posImOnHand);
			        posImOnHand.setCurrentOnHandQty(
			        		NumberUtils.getDouble(imOnHand.getStockOnHandQty())	
			        		+ NumberUtils.getDouble(imOnHand.getInUncommitQty())
			        		- NumberUtils.getDouble(imOnHand.getOutUncommitQty())
			        		+ NumberUtils.getDouble(imOnHand.getMoveUncommitQty())
			        		+ NumberUtils.getDouble(imOnHand.getOtherUncommitQty()));
			        posImOnHand.setDataId(uuId);
			        posImOnHand.setAction("I");
			        posExportDAO.save(posImOnHand);
		        }
			}
		}else{
			//非排程則是把DataId找出，再去POS_CUSTOMER依據Data_Id把資訊船進去
			String dataId = (String)parameterMap.get("DATA_ID");
			//尋找PosCustomer中此dataID有哪些需求資料
			List<PosImOnHand> posImOnHands = posExportDAO.findByProperty("PosImOnHand", new String[]{"dataId"}, new Object[]{dataId});
			for (Iterator iterator = posImOnHands.iterator(); iterator.hasNext();) {
			    	PosImOnHand posImOnHand = (PosImOnHand) iterator.next();
				HashMap conditionMap = new HashMap();
				conditionMap.put("brandCode", posImOnHand.getBrandCode());
				conditionMap.put("itemCode", posImOnHand.getItemCode());
				//將每一筆資料進資料庫查，再新建立一筆資料
				List<Object[]> results = imOnHandDAO.findImOnHandByCondition(conditionMap);
				if(results != null && results.size() >= 0){
			        for (Object result : results) {
			        	ImOnHand imOnHand = (ImOnHand)result;
				        PosImOnHand newPosImOnHand = new PosImOnHand();
				        BeanUtils.copyProperties(imOnHand.getId(), newPosImOnHand);
				        BeanUtils.copyProperties(imOnHand, newPosImOnHand);
				        newPosImOnHand.setCurrentOnHandQty(
				        		NumberUtils.getDouble(imOnHand.getStockOnHandQty())	
				        		+ NumberUtils.getDouble(imOnHand.getInUncommitQty())
				        		- NumberUtils.getDouble(imOnHand.getOutUncommitQty())
				        		+ NumberUtils.getDouble(imOnHand.getMoveUncommitQty())
				        		+ NumberUtils.getDouble(imOnHand.getOtherUncommitQty()));
				        newPosImOnHand.setDataId(uuId);
				        newPosImOnHand.setAction("I");
				        posExportDAO.save(newPosImOnHand);
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
}
