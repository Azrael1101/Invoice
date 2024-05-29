package tw.com.tm.erp.hbm.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.ImWarehouseEmployee;
import tw.com.tm.erp.hbm.dao.ImWarehouseEmployeeDAO;

public class ImWarehouseEmployeeService {

    private static final Log log = LogFactory
	    .getLog(ImWarehouseEmployeeService.class);

    private ImWarehouseEmployeeDAO imWarehouseEmployeeDAO;

    public void setImWarehouseEmployeeDAO(
	    ImWarehouseEmployeeDAO imWarehouseEmployeeDAO) {
	this.imWarehouseEmployeeDAO = imWarehouseEmployeeDAO;
    }

    public List<ImWarehouse> findByEmployeeCode(String employeeCode) {
	return imWarehouseEmployeeDAO.findByEmployeeCode(employeeCode);

    }

    public List findWarehouseCodeByEmployeeCode(String warehouseCode,
	    String employeeCode) {
	return imWarehouseEmployeeDAO.findWarehouseCodeByEmployeeCode(
		warehouseCode, employeeCode);

    }

    /**
     * 依據倉儲代號查詢倉庫人員明細檔
     * 
     * @param warehouseCode
     * @return List
     * @throws Exception
     */
    public List<ImWarehouseEmployee> findByWarehouseCode(String warehouseCode)
	    throws Exception {	
	try {
	    return imWarehouseEmployeeDAO.findByWarehouseCode(warehouseCode);
	} catch (Exception ex) {
	    log.error("1依據倉儲代號：" + warehouseCode + "查詢倉庫人員明細檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據倉儲代號：" + warehouseCode + "查詢倉庫人員明細檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
}
