/**
 * Copyright © 2008 Tasameng Corperation. All rights reserved.
 * -----------------------------------------------------------
 * Create Date Apr 1, 2008
 */
package tw.com.tm.erp.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.SoSalesOrderLine;

/**
 * @author Dumars.Tsai
 */
public class CollectionUtils {
	
	public static HashMap getSortedMap(HashMap hmap) {
		HashMap map = new LinkedHashMap();
		List mapKeys = new ArrayList(hmap.keySet());
		List mapValues = new ArrayList(hmap.values());
		hmap.clear();
		TreeSet sortedSet = new TreeSet(mapValues);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;
		for (int i = 0; i < size; i++) {
			map.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), sortedArray[i]);
		}
		return map;
	}
	
	/**
	 * Sales Order Line 排序規則
	 * @return CompositeComparator
	 */
	public static CompositeComparator salesOrderLineComparators() {
		CompositeComparator comparator = new CompositeComparator();
		comparator.addComparators(new Comparator[] {
		new Comparator() {
			public int compare(Object a, Object b) {
				SoSalesOrderLine aLine, bLine;
				aLine = (SoSalesOrderLine) a;
				bLine = (SoSalesOrderLine) b;
				Long aValue = aLine.getLineNo();
				Long bValue = bLine.getLineNo();
				return aValue.compareTo(bValue);
			}
		}, new Comparator() {
			public int compare(Object a, Object b) {
				SoSalesOrderLine aLine, bLine;
				aLine = (SoSalesOrderLine) a;
				bLine = (SoSalesOrderLine) b;
				Long aValue = aLine.getShipmentNo();
				Long bValue = bLine.getShipmentNo();
				return aValue.compareTo(bValue);
			}
		}});
		return comparator;
	}
	
	/**
	 * On Hand 的批號排序規則
	 * @return CompositeComparator
	 */
	public static CompositeComparator onHandLotNoComparators() {
		CompositeComparator comparator = new CompositeComparator();
		comparator.addComparators(new Comparator[] {
		new Comparator() {
			public int compare(Object a, Object b) {
				ImOnHand aLine, bLine;
				aLine = (ImOnHand) a;
				bLine = (ImOnHand) b;
				String aValue = aLine.getId().getLotNo();
				String bValue = bLine.getId().getLotNo();
				return newLotNoString(aValue).compareTo(newLotNoString(bValue));
			}
		}});
		return comparator;
	}
	
	/**
	 * 重組批號字串才能正確比較
	 * @param lotNo 原批號
	 * @return 用來比較的新批號字串
	 */
	private static String newLotNoString(String lotNo) {
		StringBuffer newLotNo = new StringBuffer();
		newLotNo.append(lotNo.substring(3, 4));
		newLotNo.append(lotNo.substring(2, 3));
		newLotNo.append(lotNo.substring(0, 2));
		newLotNo.append(lotNo.substring(4, 6));
		return newLotNo.toString();
	}
	
	/*public static void main(String[] args) {
		System.out.println(newLotNoString("20BA01"));
	}*/
}
