package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.SoShopDailyCompetitor;
import tw.com.tm.erp.hbm.bean.SoShopDailyCompetitorId;
import tw.com.tm.erp.hbm.dao.SoShopDailyCompetitorDAO;
import tw.com.tm.erp.utils.DateUtils;

public class SoShopDailyCompetitorService {

    private static final Log log = LogFactory
	    .getLog(SoShopDailyCompetitorService.class);

    private SoShopDailyCompetitorDAO soShopDailyCompetitorDAO;

    public void setSoShopDailyCompetitorDAO(SoShopDailyCompetitorDAO soShopDailyCompetitorDAO) {
	this.soShopDailyCompetitorDAO = soShopDailyCompetitorDAO;
    }

    /**
     * 依據primary key為查詢條件，取得專櫃每日資料檔
     * 
     * @param id
     * @return SoShopDailyHead
     * @throws Exception
     */
    public SoShopDailyCompetitor findSoShopDailyCompetitorById(SoShopDailyCompetitorId id)
	    throws Exception {

	try {
	    SoShopDailyCompetitor shopDailyCompetitor = (SoShopDailyCompetitor) soShopDailyCompetitorDAO
		    .findByPrimaryKey(SoShopDailyCompetitor.class, id);
	    return shopDailyCompetitor;
	} catch (Exception ex) {
	    log.error("依據櫃號：" + id.getShopCode() + "、銷售日期："
		    + DateUtils.format(id.getSalesDate()) + "、鄰櫃名稱：" + id.getCompetitorName()
		    + "查詢鄰櫃每日資料檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據櫃號：" + id.getShopCode() + "、銷售日期："
		    + DateUtils.format(id.getSalesDate()) + "、鄰櫃名稱：" + id.getCompetitorName()
		    + "查詢鄰櫃每日資料檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 依據鄰櫃每日資料查詢螢幕的輸入條件進行查詢
     * 
     * @param conditionMap
     * @return List
     */
    public List<SoShopDailyCompetitor> findShopDailyCompetitorList(HashMap conditionMap) throws Exception {
	try{
	    return soShopDailyCompetitorDAO.findShopDailyCompetitorList(conditionMap);
	}catch (Exception ex) {
	    log.error("依據查詢螢幕查詢鄰櫃每日資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("依據查詢螢幕查詢鄰櫃每日資料時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
   
    /**
     * 將鄰櫃每日資料新增或更新至鄰櫃每日資料檔
     * 
     * @param ShopDailyCompetitors
     * @param actualSaveShopCode
     * @param actualSaveSalesDate
     * @param loginUser
     * @return String
     * @throws FormException
     * @throws Exception
     */
    public String saveOrUpdateSoShopDailyCompetitor(List<SoShopDailyCompetitor> ShopDailyCompetitors, String actualSaveShopCode, Date actualSaveSalesDate,
	    String loginUser) throws FormException, Exception {

	try {
	    checkSoShopDailyCompetitor(ShopDailyCompetitors, actualSaveSalesDate, loginUser);
	    List<SoShopDailyCompetitor> actualSaveCompetitors = produceNewShopDailyCompetitors(ShopDailyCompetitors, actualSaveSalesDate, loginUser);
	    
	    HashMap conditionMap = new HashMap();
	    conditionMap.put("shopCode", actualSaveShopCode);
	    conditionMap.put("salesDate", actualSaveSalesDate); 
	    List<SoShopDailyCompetitor> currentCompetitors = soShopDailyCompetitorDAO.findShopDailyCompetitorList(conditionMap);	    
	    if(currentCompetitors != null && currentCompetitors.size() > 0){
	        for(SoShopDailyCompetitor currentCompetitor : currentCompetitors){
		    soShopDailyCompetitorDAO.delete(currentCompetitor);
	        }
	    }
	    for(SoShopDailyCompetitor actualSaveCompetitor : actualSaveCompetitors){
		soShopDailyCompetitorDAO.save(actualSaveCompetitor);
	    }
	    
	    return "櫃號："+ actualSaveShopCode + "、銷售日期：" + DateUtils.format(actualSaveSalesDate) + "其鄰櫃資料存檔成功！";
	} catch (FormException fe) {
	    log.error("鄰櫃每日資料存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("鄰櫃每日資料存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("鄰櫃每日資料存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
   
    /**
     * 檢核鄰櫃每日資料
     * 
     * @param ShopDailyCompetitors
     * @param actualSaveSalesDate
     * @param loginUser
     * @throws ValidationErrorException
     */
    private void checkSoShopDailyCompetitor(List<SoShopDailyCompetitor> shopDailyCompetitors, Date actualSaveSalesDate, String loginUser)
	    throws ValidationErrorException {
	
	if(actualSaveSalesDate == null){
	    throw new ValidationErrorException("請輸入欲存檔的銷售日期！");
	}else if(shopDailyCompetitors == null || shopDailyCompetitors.size() == 0){
	    throw new ValidationErrorException("請至少輸入一筆鄰櫃資料！");
	}else{
	    HashMap CompetitorNameMap = new HashMap();
	    for(int i = 0; i < shopDailyCompetitors.size(); i++){
	        SoShopDailyCompetitor shopDailyCompetitor = (SoShopDailyCompetitor)shopDailyCompetitors.get(i);
	        SoShopDailyCompetitorId shopDailyCompetitorId = shopDailyCompetitor.getId();
	        if(!StringUtils.hasText(shopDailyCompetitorId.getCompetitorName())){
	            throw new ValidationErrorException("請選擇第" + (i + 1) + "項鄰櫃資料的鄰櫃名稱！");
	        }else{
	            shopDailyCompetitorId.setCompetitorName(shopDailyCompetitorId.getCompetitorName().trim().toUpperCase());
	            if(CompetitorNameMap.get(shopDailyCompetitorId.getCompetitorName()) != null){
		        throw new ValidationErrorException("第" + (i + 1) + "項鄰櫃資料的鄰櫃名稱重複！");
		    }else{
		        CompetitorNameMap.put(shopDailyCompetitorId.getCompetitorName(), shopDailyCompetitorId.getCompetitorName());
		    }
	        }
	             
	        if(shopDailyCompetitor.getCompetitorPerformance() == null){
	            throw new ValidationErrorException("請輸入第" + (i + 1) + "項鄰櫃資料的鄰櫃營收彙總金額！");
	        }
	    }
	}
    }
    
   
    /**
     * 產生實際存檔的鄰櫃資料
     * 
     * @param shopDailyCompetitors
     * @param actualSaveSalesDate
     * @param loginUser
     * @return
     */
    private List<SoShopDailyCompetitor> produceNewShopDailyCompetitors(List<SoShopDailyCompetitor> shopDailyCompetitors, Date actualSaveSalesDate, String loginUser){
	
	List<SoShopDailyCompetitor> actualSaveCompetitors = new ArrayList(0);
	for(SoShopDailyCompetitor shopDailyCompetitor : shopDailyCompetitors){
	    Date origSalesDate = shopDailyCompetitor.getId().getSalesDate();
	    if(origSalesDate == null){
	        shopDailyCompetitor.getId().setSalesDate(actualSaveSalesDate);
	        shopDailyCompetitor.setCreatedBy(loginUser);
	        shopDailyCompetitor.setCreationDate(new Date());
	        shopDailyCompetitor.setLastUpdatedBy(loginUser);
	        shopDailyCompetitor.setLastUpdateDate(new Date());
	    
	        actualSaveCompetitors.add(shopDailyCompetitor);
	    }else{
	        SoShopDailyCompetitor actualSaveCompetitor = new SoShopDailyCompetitor();
	        BeanUtils.copyProperties(shopDailyCompetitor, actualSaveCompetitor);	    
	        actualSaveCompetitor.getId().setSalesDate(actualSaveSalesDate); 
	        if(DateUtils.daysBetween(origSalesDate, actualSaveSalesDate) != 0){
		    actualSaveCompetitor.setCreatedBy(loginUser);
		    actualSaveCompetitor.setCreationDate(new Date());
	        }	    
	        actualSaveCompetitor.setLastUpdatedBy(loginUser);
	        actualSaveCompetitor.setLastUpdateDate(new Date());
	    
	        actualSaveCompetitors.add(actualSaveCompetitor);
	    }
	}
	
	return actualSaveCompetitors;
    }
}
