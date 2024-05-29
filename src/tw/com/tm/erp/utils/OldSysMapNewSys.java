package tw.com.tm.erp.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;

/**
 * 新舊系統對照
 * 
 * @author T02049
 * 
 */

public class OldSysMapNewSys {
	private static final Log log = LogFactory.getLog(OldSysMapNewSys.class);

	/**
	 * 新 SHOP CODE 對照
	 * 
	 * @param oldShopCode
	 * @return
	 */
	public static BuShop getNewShop(String oldShopCode) {
		log.info("OldSysMapNewSys.getNewShop oldShopCode=" + oldShopCode);
		BuShop buShop = null;
		if (StringUtils.hasText(oldShopCode)) {
			oldShopCode = oldShopCode.trim();
			BuShopDAO buShopDAO = (BuShopDAO) SpringUtils.getApplicationContext().getBean("buShopDAO");
			log.info("OldSysMapNewSys.setData oldShopCode=" + oldShopCode);
			if ("RH81_1".equalsIgnoreCase(oldShopCode))
				oldShopCode = "RH81";
			List buShops = buShopDAO.findByProperty("BuShop", "reserve1", oldShopCode);
			log.info("OldSysMapNewSys.setData buShops.size()=" + buShops.size());
			for (int index = 0; index < buShops.size(); index++) {
				buShop = (BuShop) buShops.get(index);
			}
		}
		return buShop;
	}

	/**
	 * 舊 SHOP CODE 對照
	 * 
	 * @param oldShopCode
	 * @return
	 */
	public static String getOldShopCode(String newShopCode) {
		log.info("OldSysMapNewSys.getNewShop newShopCode=" + newShopCode);
		BuShopDAO buShopDAO = (BuShopDAO) SpringUtils.getApplicationContext().getBean("buShopDAO");
		String buShopCode = null;
		if (StringUtils.hasText(newShopCode)) {
			newShopCode = newShopCode.trim();
			BuShop buShop = null;
			List buShops = buShopDAO.findByProperty("BuShop", "shopCode", newShopCode);
			log.info("OldSysMapNewSys.getNewShop buShops.size()=" + buShops.size());
			for (int index = 0; index < buShops.size(); index++) {
				buShop = (BuShop) buShops.get(index);
			}
			if (null != buShop) {
				buShopCode = buShop.getReserve1();
				if (null == buShopCode) {
					buShopCode = buShop.getShopCode();
				}
			}
		}
		return buShopCode;
	}

	/**
	 * 新 ITEM CODE 對照
	 * 
	 * @param brandCode
	 * @param itemCode
	 * @return
	 */
	public static ImItem getNewItemCode(String brandCode, String oldItemCode) {
		log.info("OldSysMapNewSys.getNewItemCode oldItemCode=" + oldItemCode);
		ImItem newItem = null;
		ImItemDAO imItemDAO = (ImItemDAO) SpringUtils.getApplicationContext().getBean("imItemDAO");
		if (StringUtils.hasText(brandCode) && StringUtils.hasText(oldItemCode)) {
			List<ImItem> newItems = imItemDAO.findOldItemList(brandCode.trim(), oldItemCode.trim());
			if (null != newItems && newItems.size() > 0) {
				newItem = newItems.get(0);
			}
		}
		return newItem;
	}

	/**
	 * 舊 ITEM CODE 對照
	 * 
	 * @param brandCode
	 * @param newItemCode
	 * @return
	 */
	public static ImItem getOldItemCode(String brandCode, String newItemCode) {
		log.info("OldSysMapNewSys.getOldItemCode newItemCode=" + newItemCode);
		ImItem newItem = new ImItem();
		ImItemDAO imItemDAO = (ImItemDAO) SpringUtils.getApplicationContext().getBean("imItemDAO");
		if (StringUtils.hasText(brandCode) && StringUtils.hasText(newItemCode)) {
			brandCode = brandCode.trim();
			newItemCode = newItemCode.trim();
			List<ImItem> newItems = imItemDAO.findOldItemList(brandCode, newItemCode);
			if (null != newItems && newItems.size() > 0) {
			    ImItem itemPO  = newItems.get(0);
			    if(itemPO != null){
				if(StringUtils.hasText(itemPO.getReserve5())){
				    newItem.setItemCode(itemPO.getReserve5());
				}else{
				    newItem.setItemCode(itemPO.getItemCode());
				}
			    }
			}else{
			    newItem.setItemCode("");
			}
		}
		return newItem;
	}

	/**
	 * 取得NEW WAREHOUSE CODE
	 * 
	 * @param oldWarehouseCode
	 * @return
	 */
	public static String getNewWarehouse(String oldWarehouseCode) {
		log.info("OldSysMapNewSys.getNewWarehouse oldItemCode=" + oldWarehouseCode);
		String newWarehouseCode = null;
		if (StringUtils.hasText(oldWarehouseCode)) {
			oldWarehouseCode = oldWarehouseCode.trim();
			String nativeSql = "select * from erp.SIM_WAREHOUSE where OLD_WAREHOUSE_CODE='" + oldWarehouseCode + "'";
			NativeQueryDAO nativeQueryDAO = (NativeQueryDAO) SpringUtils.getApplicationContext().getBean("nativeQueryDAO");
			List results = nativeQueryDAO.executeNativeSql(nativeSql);
			if (null != results && results.size() > 0) {
				Object[] result = (Object[]) results.get(0);
				newWarehouseCode = (String) result[0];
			}
		}
		return newWarehouseCode;
	}

	/**
	 * 取得OLD WAREHOUSE CODE
	 * 
	 * @param oldWarehouseCode
	 * @return
	 */
	public static String getOldWarehouse(String newWarehouseCode) {
		log.info("OldSysMapNewSys.getOldWarehouse newWarehouseCode=" + newWarehouseCode);
		String oldWarehouseCode = null;
		if (StringUtils.hasText(newWarehouseCode)) {
			newWarehouseCode = newWarehouseCode.trim();
			String nativeSql = "select * from erp.SIM_WAREHOUSE where WAREHOUSE_CODE='" + newWarehouseCode + "'";
			NativeQueryDAO nativeQueryDAO = (NativeQueryDAO) SpringUtils.getApplicationContext().getBean("nativeQueryDAO");
			List results = nativeQueryDAO.executeNativeSql(nativeSql);
			if (null != results && results.size() > 0) {
				Object[] result = (Object[]) results.get(0);
				oldWarehouseCode = (String) result[1];
			}
		}
		return oldWarehouseCode;
	}

}
