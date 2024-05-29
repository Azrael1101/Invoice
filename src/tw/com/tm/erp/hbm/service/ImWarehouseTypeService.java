package tw.com.tm.erp.hbm.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.ImWarehouseType;
import tw.com.tm.erp.hbm.dao.ImWarehouseTypeDAO;

public class ImWarehouseTypeService {
    
    private static final Log log = LogFactory.getLog(ImWarehouseTypeService.class);
    
    private ImWarehouseTypeDAO imWarehouseTypeDAO;

    public void setImWarehouseTypeDAO(ImWarehouseTypeDAO imWarehouseTypeDAO) {
	this.imWarehouseTypeDAO = imWarehouseTypeDAO;
    }

    /**
     * 查詢出全部的倉庫類別
     * 
     * @return List
     * @throws Exception
     */
    public List<ImWarehouseType> findAll() throws Exception {
	
	try {
	    return imWarehouseTypeDAO.findAll();
	} catch (Exception ex) {
	    log.error("查詢倉庫類別時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢倉庫類別時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public String findWarehouseTypeNameById(Long warehouseTypeId) {
	ImWarehouseType imWarehouseType = (ImWarehouseType) imWarehouseTypeDAO
		.findByPrimaryKey(ImWarehouseType.class, warehouseTypeId);
	if (imWarehouseType != null)
	    return imWarehouseType.getName();
	else
	    return "unknow";
    }
}
