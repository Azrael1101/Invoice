package tw.com.tm.erp.hbm.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.bean.BuBranch;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuOrganization;

public class BuBrandService {
    private static final Log log = LogFactory.getLog(BuBrandService.class);

    private BuBrandDAO buBrandDAO;

    /* Spring IoC */
    public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
	this.buBrandDAO = buBrandDAO;
    }

    public BuBrand findById(String brandCode) throws Exception {

	try {
	    BuBrand brand = buBrandDAO.findById(brandCode);
	    if (brand == null)
		throw new NoSuchDataException("品牌檔查無" + brandCode + "相關資料！");
	    else
		return brand;
	} catch (Exception ex) {
	    log.error("查詢品牌檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢品牌檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public String findBrandNameById(String brandCode) {
	if (brandCode == null) {
	    return null;
	} else {
	    BuBrand buBrand = (BuBrand) buBrandDAO.findByPrimaryKey(
		    BuBrand.class, brandCode);
	    if (buBrand == null) {
		return null;
	    } else {
		return buBrand.getBrandName();
	    }

	}
    }

    public BuBranch findBranchByBrandCode(String brandCode) throws Exception {
	if (brandCode == null) {
	    return null;
	} else {
	    BuBrand buBrand = this.findById(brandCode);
	    if (buBrand == null) {
		return null;
	    } else {
		return buBrand.getBuBranch();
	    }

	}
    }

    public String findBranchNameById(String brandCode) throws Exception {
	if (brandCode == null) {
	    return "unknow";
	} else {
	    BuBranch buBranch = findBranchByBrandCode(brandCode);
	    if (buBranch == null) {
		return "unknow";
	    } else {
		return buBranch.getBranchName();
	    }

	}
    }

    public BuOrganization findOrganizationByBrandCode(String brandCode)
	    throws Exception {
	if (brandCode == null) {
	    return null;
	} else {
	    BuBranch buBranch = findBranchByBrandCode(brandCode);
	    if (buBranch == null) {
		return null;
	    } else {
		return buBranch.getBuOrganization();
	    }

	}
    }

    public String findOrganizationCNameById(String brandCode) throws Exception {
	if (brandCode == null) {
	    return "unknow";
	} else {
	    BuOrganization buOrganization = findOrganizationByBrandCode(brandCode);
	    if (buOrganization == null) {
		return "unknow";
	    } else {
		return buOrganization.getOrganizationCName();
	    }

	}
    }
    
    public List<BuBrand> findAllBuBrand() throws Exception{
        try{
	    return buBrandDAO.findAll();
	}catch (Exception ex) {
	    log.error("查詢品牌檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢品牌檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    public void updateBuBrand(Object updateObj){
	buBrandDAO.update(updateObj);
    }
    
    public List findByBranchCodes(String[] branchCodes, String organizationCode) throws Exception{
	
	try{
	    return buBrandDAO.findByBranchCodes(branchCodes, organizationCode);
	}catch (Exception ex) {
	    log.error("依據事業體代碼查詢所屬品牌時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據事業體代碼查詢所屬品牌失敗！");
	}	
    }
}
