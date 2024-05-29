/**
 * Copyright © 2008 Tasameng Corperation. All rights reserved.
 * -----------------------------------------------------------
 * Create Date Apr 15, 2008
 */
package tw.com.tm.erp.utils;

import java.util.List;

import tw.com.tm.erp.hbm.bean.ImWarehouseQuantityView;

/**
 * @author Dumars.Tsai
 *
 */
public class QuantityUtils {
	
	/**
	 * 檢查目前可用庫存是否大於出貨數量
	 * @param list is ImWarehouseQuantityView 的 List 集合
	 * @param shipQuantity is 出貨數量
	 * @return boolean<br>true表示目前庫存大於出貨數量,可出貨;<br>false則反之
	 */
	public static boolean isAvailableGTShipQuantity(
			List<ImWarehouseQuantityView> list, float shipQuantity) {
		float availableQty = 0;
		for(ImWarehouseQuantityView view : list) {
			// 可用數量累加
			availableQty = OperationUtils.add(availableQty,
					OperationUtils.subtraction(
							view.getStockOnHandQty(), view.getUncommitQty())).floatValue();
		}
		if(availableQty >= shipQuantity) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
