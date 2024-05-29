/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA :
 * PG : Weichun.Liao
 * Filename : ImInventoryCountService.java
 * Function :
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	Jun 9, 2010		Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
package tw.com.tm.erp.utils.sp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.service.ImWarehouseService;

public class ImInventoryCountService {

	private static final Log log = LogFactory.getLog(ImInventoryCountService.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Call store procedure
	 *
	 * @param conditionMap
	 * @throws ValidationErrorException
	 */
	public void getImInventoryCountItemInfo(HttpServletRequest request) throws ValidationErrorException {

		Connection conn = null;
		CallableStatement calStmt = null;
		try {

			String date = (String) request.getParameter("date");
			String[] warehouseCode = request.getParameterValues("warehouseCode") == null ? new String[0] : request
					.getParameterValues("warehouseCode"); // 庫別
			String inventoryCountsId = (String) request.getParameter("inventoryCountsId");
			String brandCode = (String) request.getParameter("brandCode");

			String _warehouseCode = "";
			StringBuilder sb = new StringBuilder();
			if (warehouseCode.length == 1 && "".equals(warehouseCode[0])) { // 選擇全部
				ImWarehouseService imWarehouseService = (ImWarehouseService) SpringUtils.getApplicationContext().getBean(
						"imWarehouseService");
				List<ImWarehouse> imWarehouses = imWarehouseService.findByBrandCode(brandCode);
				for (int j = 0; j < imWarehouses.size(); j++) {
					ImWarehouse imWarehouse = imWarehouses.get(j);
					if (j == 0)
						sb.append(imWarehouse.getWarehouseCode());
					else
						sb.append(",").append(imWarehouse.getWarehouseCode());
				}

			}

			for (int i = 0; i < warehouseCode.length; i++) {
				if (sb.length() == 0)
					sb.append(warehouseCode[i]);
				else
					sb.append(",").append(warehouseCode[i]);
				_warehouseCode = sb.toString();
			}

			/*
			String[] category01 = request.getParameterValues("category01"); // 大類
			String _category01 = "";
			sb = new StringBuilder();
			for (int i = 0; i < category01.length; i++) {
				if (sb.length() == 0)
					sb.append(category01[i]);
				else
					sb.append(",").append(category01[i]);
				_category01 = sb.toString();
			}
			String[] category02 = request.getParameterValues("category02");// 中類
			String _category02 = "";
			sb = new StringBuilder();
			for (int i = 0; i < category02.length; i++) {
				if (sb.length() == 0)
					sb.append(category02[i]);
				else
					sb.append(",").append(category02[i]);
				_category02 = sb.toString();
			}
			String[] itemBrand = request.getParameterValues("itemBrand");// 小類
			String _itemBrand = "";
			sb = new StringBuilder();
			for (int i = 0; i < itemBrand.length; i++) {
				if (sb.length() == 0)
					sb.append(itemBrand[i]);
				else
					sb.append(",").append(itemBrand[i]);
				_itemBrand = sb.toString();
			}
			*/
			System.out.println("日期 ::: " + date);
			System.out.println("庫別 ::: " + _warehouseCode);
			//System.out.println("大類 ::: " + _category01);
			//System.out.println("小類 ::: " + _category02);
			//System.out.println("品牌 ::: " + _itemBrand);

			conn = dataSource.getConnection();
			calStmt = conn.prepareCall("{call ERP.APP_STOCK_CAL_PACKAGE_T2.CALCULATE_ITEM_ON_HAND(?,?,?,?)}"); // 呼叫store procedure
			//calStmt.registerOutParameter(1, OracleTypes.CURSOR);
			calStmt.setString(1, brandCode);
			calStmt.setString(2, date);
			calStmt.setString(3, _warehouseCode);
			calStmt.setString(4, inventoryCountsId);
			// calStmt.setString(5, category02);
			System.out.println("==== call ERP.APP_STOCK_CAL_PACKAGE_T2.CALCULATE_ITEM_ON_HAND(" + brandCode + "," + date
					+ "," + _warehouseCode + "," + inventoryCountsId + ") ====");
			calStmt.execute();
			System.out.println("==== 呼叫資料庫procedure成功！ ====");
		} catch (Exception ex) {
			log.error("呼叫資料庫proceduce發生錯誤，原因：" + ex.toString());
			throw new ValidationErrorException(ex.toString());
		} finally {
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("關閉Connection時發生錯誤！");
				}
			}
		}
	}
}
