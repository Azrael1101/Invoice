package tw.com.tm.erp.hbm.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.SoShopDailyHead;
import tw.com.tm.erp.hbm.bean.SoShopDailyHeadId;
import tw.com.tm.erp.hbm.dao.SoShopDailyHeadDAO;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.UserUtils;

public class SoShopDailyHeadService {

    private static final Log log = LogFactory
	    .getLog(SoShopDailyHeadService.class);

    private SoShopDailyHeadDAO soShopDailyHeadDAO;

    public void setSoShopDailyHeadDAO(SoShopDailyHeadDAO soShopDailyHeadDAO) {
	this.soShopDailyHeadDAO = soShopDailyHeadDAO;
    }

    /**
     * 依據primary key為查詢條件，取得專櫃每日資料檔
     * 
     * @param id
     * @return SoShopDailyHead
     * @throws Exception
     */
    public SoShopDailyHead findSoShopDailyHeadById(SoShopDailyHeadId id)
	    throws Exception {

	try {
	    SoShopDailyHead shopDailyHead = (SoShopDailyHead) soShopDailyHeadDAO
		    .findByPrimaryKey(SoShopDailyHead.class, id);
	    return shopDailyHead;
	} catch (Exception ex) {
	    log.error("依據櫃號：" + id.getShopCode() + "、銷售日期："
		    + DateUtils.format(id.getSalesDate())
		    + "查詢專櫃每日資料檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據櫃號：" + id.getShopCode() + "、銷售日期："
		    + DateUtils.format(id.getSalesDate())
		    + "查詢專櫃每日資料檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 判斷是否為新資料，並將專櫃每日資料新增或更新至專櫃每日資料檔
     * 
     * @param shopDailyHead
     * @param isNew
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String saveOrUpdateSoShopDailyHead(SoShopDailyHead shopDailyHead, String loginUser,
	    boolean isNew) throws FormException, Exception {
	try {

	    checkSoShopDailyHead(shopDailyHead, loginUser);	    
	    SoShopDailyHead shopDailyHeadPO = findSoShopDailyHeadById(shopDailyHead.getId());	    
	    if (shopDailyHeadPO == null) {
		insertSoShopDailyHead(shopDailyHead);
	    }else if (isNew) {
		throw new ValidationErrorException("櫃號："
			+ shopDailyHead.getId().getShopCode()
			+ "、銷售日期："
			+ DateUtils
				.format(shopDailyHead.getId().getSalesDate())
			+ "已經存在，請勿重覆建立！");
	    } else {
		BeanUtils.copyProperties(shopDailyHead, shopDailyHeadPO);
		modifySoShopDailyHead(shopDailyHeadPO);
	    }
	    
	    return "櫃號："+ shopDailyHead.getId().getShopCode() + "、銷售日期：" + DateUtils.format(shopDailyHead.getId().getSalesDate()) + "存檔成功！";
	} catch (FormException fe) {
	    log.error("專櫃每日資料存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("專櫃每日資料存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("專櫃每日資料存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 新增至專櫃每日資料檔
     * 
     * @param saveObj
     */
    private void insertSoShopDailyHead(Object saveObj) {
	soShopDailyHeadDAO.save(saveObj);
    }

    /**
     * 更新至專櫃每日資料檔
     * 
     * @param updateObj
     */
    private void modifySoShopDailyHead(Object updateObj) {
	soShopDailyHeadDAO.update(updateObj);
    }

    /**
     * 檢核專櫃每日資料
     * 
     * @param shopDailyHead
     * @param loginUser
     * @throws ValidationErrorException
     */
    private void checkSoShopDailyHead(SoShopDailyHead shopDailyHead, String loginUser)
	    throws ValidationErrorException {

	SoShopDailyHeadId shopDailyHeadId = shopDailyHead.getId();
	if (!StringUtils.hasText(shopDailyHeadId.getShopCode())) {
	    throw new ValidationErrorException("請選擇專櫃代號！");
	} else if (shopDailyHeadId.getSalesDate() == null) {
	    throw new ValidationErrorException("請輸入銷售日期！");
	} else if (shopDailyHead.getVisitorCount() == null) {
	    throw new ValidationErrorException("請輸入來客數！");
	} else if (shopDailyHead.getTotalActualSalesAmount() == null) {
	    throw new ValidationErrorException("請輸入營收彙總金額！");
	}
	
	UserUtils.setOpUserAndDate(shopDailyHead, loginUser);
    }

    /**
     * 依據專櫃每日資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List<SoShopDailyHead> findShopDailyHeadList(HashMap conditionMap) throws Exception {
	try{
	    return soShopDailyHeadDAO.findShopDailyHeadList(conditionMap);
	}catch (Exception ex) {
	    log.error("依據查詢螢幕查詢專櫃每日資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據查詢螢幕查詢專櫃每日資料時發生錯誤，原因：" + ex.getMessage());
	}
    }
}
