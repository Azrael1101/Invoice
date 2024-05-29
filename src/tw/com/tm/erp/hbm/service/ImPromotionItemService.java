package tw.com.tm.erp.hbm.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImItemPrice;
import tw.com.tm.erp.hbm.bean.ImItemPriceView;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionCustomer;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionShop;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceDAO;
import tw.com.tm.erp.hbm.dao.ImItemPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionItemDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.StringTools;

public class ImPromotionItemService {
    
    private static final Log log = LogFactory.getLog(ImPromotionItemService.class);
    
    private ImPromotionDAO imPromotionDAO;
    
    private ImPromotionItemDAO imPromotionItemDAO;
    
    private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
    
    private BuOrderTypeService buOrderTypeService;
    
    private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
    
    private SiProgramLogAction siProgramLogAction;
    
    private BuShopDAO buShopDAO;
    
    private ImItemPriceDAO imItemPriceDAO;

    private ImItemPriceViewDAO imItemPriceViewDAO;
    
    public void setImPromotionDAO(ImPromotionDAO imPromotionDAO) {
        this.imPromotionDAO = imPromotionDAO;
    }
    
    public void setImPromotionItemDAO(ImPromotionItemDAO imPromotionItemDAO) {
        this.imPromotionItemDAO = imPromotionItemDAO;
    }
    
    public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
	this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
    }
   
    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
	this.buOrderTypeService = buOrderTypeService;
    }
    
    public void setBuEmployeeWithAddressViewDAO(BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
	this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
    }
    
    public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
	this.siProgramLogAction = siProgramLogAction;
    }
    
    public void setBuShopDAO(BuShopDAO buShopDAO) {
        this.buShopDAO = buShopDAO;
    }
    
    public void setImItemPriceDAO(ImItemPriceDAO imItemPriceDAO) {
    	this.imItemPriceDAO = imItemPriceDAO;
    }
    public void setImItemPriceViewDAO(ImItemPriceViewDAO imItemPriceViewDAO) {
    	this.imItemPriceViewDAO = imItemPriceViewDAO;
    }
   
    /**
     * 更新促銷商品明細
     * 
     * @param headId
     * @throws FormException
     * @throws Exception
     */
    public void saveItemData(Long headId) throws FormException, Exception{
	
	try{
	    ImPromotion promotionPo = imPromotionDAO.findById(headId);
	    if(promotionPo == null){
		throw new NoSuchObjectException("查無促銷單主鍵(" + headId + ")的資料！");
	    }else{
		String brandCode = promotionPo.getBrandCode();
		String priceType = promotionPo.getPriceType();
		
		HashMap conditionMap = new HashMap();
		conditionMap.put("brandCode", brandCode);
		conditionMap.put("priceType", priceType);
		List promotionItems = promotionPo.getImPromotionItems();
		if(promotionItems != null && promotionItems.size() > 0){
		    for(int i = 0; i < promotionItems.size(); i++){
			ImPromotionItem promotionItem = (ImPromotionItem)promotionItems.get(i);
			refreshPromotionItems(conditionMap, promotionItem);
		    }
		    imPromotionDAO.update(promotionPo);
		}		
	    }	    
	}catch (FormException fe) {
	    log.error("更新促銷商品明細失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("更新促銷商品明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新促銷商品明細時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    public void deletePromotionItems(Long headId) throws FormException, Exception{
	
	try{
    	    ImPromotion promotion = imPromotionDAO.findById(headId);
    	    if(promotion == null){
    	        throw new NoSuchObjectException("查無促銷單主鍵(" + headId + ")的資料");
    	    }
    	    List promotionItems = promotion.getImPromotionItems();
    	    if(promotionItems != null && promotionItems.size() > 0){
    		for(int i = 0; i < promotionItems.size(); i++){
    		    ImPromotionItem promotionItem = (ImPromotionItem)promotionItems.get(i);
    		    imPromotionItemDAO.delete(promotionItem);
    		} 		
    	    }
        } catch (FormException fe) {
	    log.error("刪除促銷商品明細失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("刪除促銷商品明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("刪除促銷商品明細時發生錯誤，原因：" + ex.getMessage());
	} 	
    }
    
    private void revertToInitial(ImPromotionItem promotionItem, boolean isAllInit){
	
	if(isAllInit){
	    promotionItem.setStandardPurchaseCost(null);
	    promotionItem.setOriginalPrice(null);
	}
	promotionItem.setDiscountType(null);
	promotionItem.setDiscount(null);
	promotionItem.setDiscountAmount(null);
	promotionItem.setDiscountPercentage(null);
	promotionItem.setTotalDiscountAmount(null);
	promotionItem.setTotalDiscountPercentage(null);
    }
    
    private void refreshPromotionItems(Map conditionMap, ImPromotionItem promotionItem){
	
        String brandCode = (String)conditionMap.get("brandCode");
	String priceType = (String)conditionMap.get("priceType");	
//	String beginDate = (String)conditionMap.get("beginDate");	//--03/10
//	String endDate = (String)conditionMap.get("endDate");
	ImItemCurrentPriceView imItemCurrentPriceView = 
	        imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, promotionItem.getItemCode(), priceType);
	//----------------03/10----------------
	//ImItemPriceView imItemPriceView = imItemPriceViewDAO.findAdPrice(brandCode, promotionItem.getItemCode(), priceType ,beginDate,endDate);
	//update item data
	if(imItemCurrentPriceView != null){
            promotionItem.setStandardPurchaseCost(imItemCurrentPriceView.getStandardPurchaseCost());
	    promotionItem.setOriginalPrice(imItemCurrentPriceView.getUnitPrice());//------03/10修改
	    if(promotionItem.getOriginalPrice() != null && promotionItem.getOriginalPrice() > 0D){
	        if (promotionItem.getDiscountAmount() != null && CommonUtils.round(promotionItem.getDiscountAmount(), 0) != 0D) {
		    promotionItem.setDiscountAmount(CommonUtils.round(promotionItem.getDiscountAmount(), 0));
	            promotionItem.setDiscountType("1");
	            promotionItem.setDiscountPercentage(null);
	            promotionItem.setDiscount(promotionItem.getOriginalPrice() - promotionItem.getDiscountAmount());
	            promotionItem.setTotalDiscountAmount(promotionItem.getDiscountAmount());
	            promotionItem.setTotalDiscountPercentage(
	            CommonUtils.round((promotionItem.getDiscountAmount() / promotionItem.getOriginalPrice() * 100D), 1));
		} else if(promotionItem.getDiscountPercentage() != null){
		    promotionItem.setDiscountPercentage(CommonUtils.round(promotionItem.getDiscountPercentage(), 1));
	            promotionItem.setDiscountType("2");
		    promotionItem.setDiscountAmount(null);
	            promotionItem.setDiscount(100D - promotionItem.getDiscountPercentage());
		    promotionItem.setTotalDiscountAmount(CommonUtils.round((promotionItem.getOriginalPrice() * (promotionItem.getDiscountPercentage() / 100D)), 0));
	            promotionItem.setTotalDiscountPercentage(promotionItem.getDiscountPercentage());
		} else{
		    revertToInitial(promotionItem, false);
		}   
	    }else{
	        revertToInitial(promotionItem, false);
	    }
	}else{
	    revertToInitial(promotionItem, true);			    
	}
    }
    
    /**
     * 更新促銷商品明細(換頁籤)
     * 
     * @param headId
     * @throws FormException
     * @throws Exception
     */
    public void savePartialItemData(Map conditionMap) 
            throws FormException, Exception{
	
	try{
	    Long headId = (Long)conditionMap.get("headId");
	    Integer startPage = (Integer)conditionMap.get("startPage");
	    Integer pageSize = (Integer)conditionMap.get("pageSize");

	    List<ImPromotionItem> promotionItems = imPromotionItemDAO.findPageLine(headId, startPage, pageSize);
	    if(promotionItems != null && promotionItems.size() > 0){
	        for(int i = 0; i < promotionItems.size(); i++){
	            ImPromotionItem promotionItem = (ImPromotionItem)promotionItems.get(i);
	            refreshPromotionItems(conditionMap, promotionItem);
	            imPromotionItemDAO.update(promotionItem);
		}
	    }
	}catch (Exception ex) {
	    log.error("更新促銷商品明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新促銷商品明細時發生錯誤，原因：" + ex.getMessage());
	}
    }
    
    /**
     * 暫存單號取實際單號並更新至Promotion主檔
     * 
     * @param headId
     * @param loginUser
     * @return ImPromotion
     * @throws ObtainSerialNoFailedException
     * @throws FormException
     * @throws Exception
     */
    public ImPromotion saveActualOrderNo(Long headId, String loginUser)
	    throws ObtainSerialNoFailedException, FormException, Exception {
	
	ImPromotion promotionPO = imPromotionDAO.findById(headId);
	if(promotionPO == null){
	    throw new NoSuchObjectException("查無促銷單主鍵(" + headId + ")的資料！");
	}else if (AjaxUtils.isTmpOrderNo(promotionPO.getOrderNo())) {
	    String serialNo = buOrderTypeService.getOrderSerialNo(
	        promotionPO.getBrandCode(), promotionPO.getOrderTypeCode());
	    if (!serialNo.equals("unknow")) {
		promotionPO.setOrderNo(serialNo);
	    } else {
		throw new ObtainSerialNoFailedException("取得"
		    + promotionPO.getOrderTypeCode() + "單號失敗！");
	    }
        }
	promotionPO.setLastUpdatedBy(loginUser);
	promotionPO.setLastUpdateDate(new Date());
        imPromotionDAO.update(promotionPO);

        return promotionPO;
    }
    
    /**
     * 檢核促銷活動相關資料
     * 
     * @param imPromotion
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    public void checkPromotionData(ImPromotion imPromotion, String programId, String identification) 
        throws ValidationErrorException {
        
	String message = null;
	List errorMsgs = new ArrayList(0);
	try{
	    checkPromotionHead(imPromotion, programId, identification, errorMsgs);
	    checkPromotionItems(imPromotion, programId, identification, errorMsgs);
	    checkPromotionShops(imPromotion, programId, identification, errorMsgs);
	    if(!("Y".equals(imPromotion.getIsAllCustomer()))) {
	        checkPromotionCustomer(imPromotion, programId, identification, errorMsgs);
	    }
	}catch(Exception ex){
	    message = "檢核促銷單時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}
	
	if(errorMsgs.size() > 0){
	    throw new ValidationErrorException(MessageStatus.VALIDATION_FAILURE);
	}
    }
    
    /**
     * check promotion head
     * 
     * @param imPromotion
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkPromotionHead(ImPromotion imPromotion, String programId, String identification, List errorMsgs){
	
	String message = null;
        try{
            String brandCode = imPromotion.getBrandCode();
            
	    if(!StringUtils.hasText(imPromotion.getPromotionCode()) && !"T2".equalsIgnoreCase(brandCode) ){
		message = "請輸入活動代號！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	    
	    if(!StringUtils.hasText(imPromotion.getPromotionName())){
		message = "請輸入活動名稱！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
	   
	    if(StringUtils.hasText(imPromotion.getInCharge())){
	        BuEmployeeWithAddressView employeeWithAddressView = 
		    buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(imPromotion.getBrandCode(), imPromotion.getInCharge());
	        if(employeeWithAddressView == null){
	            message = "查無" + imPromotion.getInCharge() + "的員工資料！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
	        }	       
	    }
	    
	    if("T2".equals(brandCode)){
	        if(!StringUtils.hasText(imPromotion.getItemCategory())){
		    message = "請選擇業種！";
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);		
	        }else{
		    if(!StringUtils.hasText(imPromotion.getPurchaseAssist())){
		        message = "請選擇採購助理！";
	                siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		        errorMsgs.add(message);
		        log.error(message);
		    }
		
		    if(!StringUtils.hasText(imPromotion.getPurchaseMember())){
		        message = "請選擇採購人員！";
	                siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		        errorMsgs.add(message);
		        log.error(message);
		    }
		
		    if(!StringUtils.hasText(imPromotion.getPurchaseMaster())){
		        message = "請選擇採購主管！";
	                siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		        errorMsgs.add(message);
		        log.error(message);
		    }
	        }
	    }
	}catch(Exception ex){
	    message = "檢核促銷單主檔時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}	
    }
    
    /**
     * check promotion items
     * 
     * @param imPromotion
     * @param programId
     * @param identification
     * @param errorMsgs
     * @param  
     */
  
    private void checkPromotionItems(ImPromotion imPromotion, String programId, String identification, List errorMsgs){
    	
	String message = null;
        try{
        	if("Y".equals(imPromotion.getIsAllItem())){
        		if(imPromotion.getDiscount() == null || CommonUtils.round(imPromotion.getDiscount(), 2) == 0L){
        			message = "商品資料頁籤中選擇所有商品，但未輸入折扣比率";
        			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
        			errorMsgs.add(message);	           
        		}
        	}else{
        		checkItems(imPromotion, programId, identification, errorMsgs);
        	}	    
	}catch(Exception ex){
	    message = "檢核商品資料頁籤時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}	
    }
    
    /**
     * check items
     * 
     * @param imPromotion
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkItems(ImPromotion imPromotion, String programId, String identification, List errorMsgs) 
            throws ValidationErrorException, NoSuchObjectException {
	
    	String message = null;
    	String tabName = "商品資料頁籤";
    	String brandCode = imPromotion.getBrandCode();
    	String priceType = imPromotion.getPriceType();
    	String itemCategory = imPromotion.getItemCategory(); //業種子類
    	List promotionItems = imPromotion.getImPromotionItems();
    	
    	if(promotionItems != null && promotionItems.size() > 0){
    		HashMap itemMap = new HashMap();
    		int intactRecordCount = 0;
    		for(int i = 0; i < promotionItems.size(); i++){
    			try{
    				ImPromotionItem promotionItem = (ImPromotionItem)promotionItems.get(i);
    				if(!"1".equals(promotionItem.getIsDeleteRecord())){
    					String itemCode = promotionItem.getItemCode();
    					if(!StringUtils.hasText(itemCode)){
    						throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的品號！");
    					}
    					itemCode = itemCode.trim().toUpperCase();
    					promotionItem.setItemCode(itemCode);
    					//=============2014/2/26檢核促銷檔期內是否有變價==============
    					if("T2".equals(brandCode)){
    					List <ImItemPrice> imItemPrices = imItemPriceDAO.findByItemCode(itemCode);
    					for (int j = 0; j < imItemPrices.size(); j++) {
    						ImItemPrice imItemPrice = (ImItemPrice) imItemPrices.get(j);
    						Date PrbeginDate = (imItemPrice).getBeginDate(); 
    						log.info("PrbeginDate=="+ PrbeginDate );
    					if(imPromotion.getBeginDate().before(PrbeginDate) && imPromotion.getEndDate().after(PrbeginDate)){
    						throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
									")在促銷期間" + DateUtils.format(PrbeginDate, DateUtils.C_DATA_PATTON_YYYYMMDD) +"變價啟用，故無法做促銷活動" );
    					}
    				}
    			}
    					ImItemCurrentPriceView currentPriceView = imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, itemCode, priceType);
    					if(currentPriceView != null){
    						//==================檢核商品業種子類是否與主檔業種相同===================
    						if("T2".equals(brandCode) && StringUtils.hasText(itemCategory)){
    							if(!itemCategory.equals(currentPriceView.getItemCategory())){
    								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    										")的業種(" + currentPriceView.getItemCategory() + ")與主檔業種(" + itemCategory + ")不相同！");
    							}                       	
    						}                           
    						//==================檢核商品是否重複======================
    						if(itemMap.get(itemCode) == null){
    							itemMap.put(itemCode, new Integer(i + 1));
    						}else{
    							throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    									")與第" + itemMap.get(itemCode) + "項明細的品號相同，請確認！");
    						}
    						//==================檢核促銷金額、折扣數===============
    						Double discountAmount = promotionItem.getDiscountAmount(); //促銷價
    						Double discountPercentage = promotionItem.getDiscountPercentage(); //discount
    						Double originalPrice = promotionItem.getOriginalPrice();
    						if(discountAmount != null){
    							discountAmount = CommonUtils.round(discountAmount, 0);
    							promotionItem.setDiscountAmount(discountAmount);
    						}
    						if(discountPercentage != null){
    							discountPercentage = CommonUtils.round(discountPercentage, 1);
    							promotionItem.setDiscountPercentage(discountPercentage);
    						}
    						//System.out.println("[discountAmount]=" + discountAmount);
    						//System.out.println("[discountPercentage]=" + discountPercentage);
    						if((discountAmount == null || discountAmount == 0D) && 
    								(discountPercentage == null || discountPercentage == 0D)) {
    							throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    									")尚未輸入促銷金額或折扣數！");
    						}else if((discountAmount != null && discountAmount != 0D) &&
    								(discountPercentage != null && discountPercentage != 0D)) {
    							throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    									")其促銷金額及折扣數重覆輸入！");
    						}else if(originalPrice == null) {
    							throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    									")其定價為空值！");
    						}else if(originalPrice == 0D) {
    							throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    									")其定價為零無法執行促銷！");
    						}else if(discountAmount != null && discountAmount != 0D) {
    							if(discountAmount <= 0D){
    								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    										")其促銷金額不可小於或等於零！");
    							}/*2014/2/26關閉
    							else if(discountAmount >= originalPrice){
    								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    										")其促銷金額不可大於或等於定價！");
    							}*/else{
    								promotionItem.setDiscountType("1");
    								promotionItem.setDiscountPercentage(null);
    								promotionItem.setDiscount(originalPrice - discountAmount);
    								promotionItem.setTotalDiscountAmount(discountAmount);
    								promotionItem.setTotalDiscountPercentage(CommonUtils.round((discountAmount / originalPrice * 100D), 1));
    							}
    						}else if(discountPercentage != null && discountPercentage != 0D){
    							if(discountPercentage < 0D){
    								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    										")其折扣數不可小於0%！");
    							}else if(discountPercentage >= 100D){
    								throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
    										")其折扣數不可大於或等於100%！");
    							}else{
    								promotionItem.setDiscountType("2");
    								promotionItem.setDiscountAmount(null);
    								promotionItem.setDiscount(100D - discountPercentage);
    								promotionItem.setTotalDiscountAmount(CommonUtils.round((originalPrice * (discountPercentage / 100D)), 0));
    								promotionItem.setTotalDiscountPercentage(discountPercentage);
    							}			       
    						}
    						intactRecordCount++;
    					}else{
    						throw new NoSuchObjectException("查無" + tabName + "中第" + (i + 1) + "項明細的品號！");
    					}
    				}
    			}catch(Exception ex){
    				message = ex.getMessage();
    				siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    				errorMsgs.add(message);
    				log.error(message);	            
    			}
    		}
    		if(intactRecordCount == 0){
    			message = tabName + "中至少需輸入一筆完整的資料！";
    			siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    			errorMsgs.add(message);
    			log.error(message);
    		}
    	}else{
    		message = tabName + "中至少需輸入一筆資料！";
    		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    		errorMsgs.add(message);
    		log.error(message);
    	}         
    }
    
    /**
     * check promotion shops
     * 
     * @param imPromotion
     * @param programId
     * @param identification
     * @param errorMsgs
     */
    private void checkPromotionShops(ImPromotion imPromotion, String programId, String identification, List errorMsgs){
	
        String message = null;
        try{
            if("Y".equals(imPromotion.getIsAllShop())){			
 	        if(imPromotion.getBeginDate() == null){
 	            message = "參與專櫃頁籤中選擇所有專櫃，但未輸入起始日期！";
 	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
 	            errorMsgs.add(message);
 		    log.error(message);
 	        }
 	       	       
 	        if(imPromotion.getEndDate() == null){
 	            message = "參與專櫃頁籤中選擇所有專櫃，但未輸入結束日期！";
 	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
	            errorMsgs.add(message);
		    log.error(message);		   
 	        }
 	        
 	        if(imPromotion.getBeginDate() != null && imPromotion.getEndDate() != null && imPromotion.getBeginDate().after(imPromotion.getEndDate())){
 	            message = "參與專櫃頁籤中選擇所有專櫃，但起始日期不可大於結束日期！";
 	            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
	            errorMsgs.add(message);
		    log.error(message);
  	        }
 	    }else{
 		checkShops(imPromotion, programId, identification, errorMsgs);
 	    } 
	}catch(Exception ex){
	    message = "檢核參與專櫃頁籤時發生錯誤，原因：" + ex.toString();
	    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}	
    }
    
   
    /**
     * check shops
     * 
     * @param imPromotion
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     * @throws NoSuchObjectException
     */
    private void checkShops(ImPromotion imPromotion, String programId, String identification, List errorMsgs)
            throws ValidationErrorException, NoSuchObjectException {
	   
        String message = null;
        String tabName = "參與專櫃頁籤";
	String brandCode = imPromotion.getBrandCode();
        List promotionShops = imPromotion.getImPromotionShops();
	if(promotionShops != null && promotionShops.size() > 0){
	    HashMap shopMap = new HashMap();
	    int intactRecordCount = 0;
	    for(int i = 0; i < promotionShops.size(); i++){
	        try{
		    ImPromotionShop promotionShop = (ImPromotionShop)promotionShops.get(i);
		    if(!"1".equals(promotionShop.getIsDeleteRecord())){
		        String shopCode = promotionShop.getShopCode();
		        if(!StringUtils.hasText(shopCode)){
			    throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的專櫃代號！");
		        }
		        shopCode = shopCode.trim().toUpperCase();
		        promotionShop.setShopCode(shopCode);
		        if(buShopDAO.findEnableShopById(brandCode, shopCode) != null){
		            //==================檢核專櫃是否重複======================
             	            if(shopMap.get(shopCode) == null){
             	                shopMap.put(shopCode, new Integer(i + 1));
             	            }else{
             	                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的專櫃代號(" + shopCode +
			            ")與第" + shopMap.get(shopCode) + "項明細的專櫃代號相同，請確認！");
             	            }
             	            //===============檢核促銷活動啟始日期和結束日期===============
             	            Date beginDate = promotionShop.getBeginDate();
             	            Date endDate = promotionShop.getEndDate();
             	            if(beginDate == null){
             	                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的專櫃代號(" + shopCode +
			            ")未輸入促銷活動起始日期！");
             	            }else if(endDate == null){
             	                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的專櫃代號(" + shopCode +
			            ")未輸入促銷活動結束日期！");
             	            }else if(beginDate.after(endDate)){
             	                throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的專櫃代號(" + shopCode +
			            ")其促銷活動起始日期不可大於結束日期！");
             	            }
             	            intactRecordCount++;
		        }else{
			    throw new NoSuchObjectException("查無" + tabName + "中第" + (i + 1) + "項明細的專櫃代號！"); 
		        }
		    }
	        }catch(Exception ex){
	            message = ex.getMessage();
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);	            
	        }
	    }
	    if(intactRecordCount == 0){
	        message = tabName + "中至少需輸入一筆完整的資料！";
		siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
		errorMsgs.add(message);
		log.error(message);
	    }
        }else{
	    message = tabName + "中至少需輸入一筆資料！";
            siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
	    errorMsgs.add(message);
	    log.error(message);
	}	   
    }
    
   
    /**
     * check customers
     * 
     * @param imPromotion
     * @param programId
     * @param identification
     * @param errorMsgs
     * @throws ValidationErrorException
     */
    private void checkPromotionCustomer(ImPromotion imPromotion, String programId, String identification, List errorMsgs) 
            throws ValidationErrorException {
		
        String message = null;
	String tabName = "客戶類別頁籤";
	List promotionCustomers = imPromotion.getImPromotionCustomers();
	if(promotionCustomers != null && promotionCustomers.size() > 0){
	    boolean isHaveCustomer = false;
	    for(int i = 0; i < promotionCustomers.size(); i++){
                ImPromotionCustomer promotionCustomer = (ImPromotionCustomer)promotionCustomers.get(i);
		if("Y".equals(promotionCustomer.getEnable())){
		    isHaveCustomer = true;
		    break;
		}
	    }
	    if(!isHaveCustomer){
                message = tabName + "中至少需勾選一筆客戶類別！";
                siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, imPromotion.getLastUpdatedBy());
    	        errorMsgs.add(message);
    	        log.error(message);
	    }
	}
    }
    
    
    
    /**
     * 儲存檢核完畢的促銷活動相關資料
     * 
     * @param headId
     * @param parameterMap
     * @param programId
     * @param employeeCode
     * @return ImPromotion
     * @throws Exception
     */
    public ImPromotion saveCheckedPromotionData(Long headId, Map parameterMap, String programId, String employeeCode) 
        throws Exception {
        
	String message = null;
	List errorMsgs = new ArrayList(0);
	//Object formBindBean = parameterMap.get("vatBeanFormBind");
	//Object otherBean = parameterMap.get("vatBeanOther");
	try{
	    ImPromotion promotionPO = imPromotionDAO.findById(headId);
	    if(promotionPO == null){
	        throw new NoSuchObjectException("查無促銷單主鍵(" + headId + ")的資料！");
	    }else{
		String identification = MessageStatus.getIdentification(promotionPO.getBrandCode(), 
    		        promotionPO.getOrderTypeCode(), promotionPO.getOrderNo());
		try{		    
		    //setParameterToBean(otherBean, promotionPO);
        	    //AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);
        	    siProgramLogAction.deleteProgramLog(programId, null, identification);
	            checkPromotionHead(promotionPO, programId, identification, errorMsgs);
	            checkPromotionItems(promotionPO, programId, identification, errorMsgs);
	            checkPromotionShops(promotionPO, programId, identification, errorMsgs);
	            if(!("Y".equals(promotionPO.getIsAllCustomer()))) {
	                checkPromotionCustomer(promotionPO, programId, identification, errorMsgs);
	            }
		}catch(Exception ex){
		    message = "檢核促銷單時發生錯誤，原因：" + ex.toString();
		    siProgramLogAction.createProgramLog(programId, MessageStatus.LOG_ERROR, identification, message, promotionPO.getLastUpdatedBy());
		    errorMsgs.add(message);
		    log.error(message);
		}
	        
	        if(errorMsgs.size() == 0){
	        //============remove delete mark record=============
        	    removeDeleteMarkLineForItem(promotionPO);
        	    removeDeleteMarkLineForShop(promotionPO);
        	    promotionPO.setStatus(OrderStatus.SIGNING);
		}	        
	        //promotionPO.setLastUpdatedBy(employeeCode);
	        promotionPO.setLastUpdateDate(new Date());	        
	        imPromotionDAO.update(promotionPO);
	        
	        return promotionPO;
	    }
	}catch(Exception ex){
	    log.error("促銷單檢核後存檔失敗，原因：" + ex.toString());
	    throw new Exception("促銷單檢核後存檔失敗，原因：" + ex.getMessage());
	}
    }
    
    public void setParameterToBean(Object bean, ImPromotion promotion) throws ValidationErrorException, Exception{
	   
        String delim = "#";
	String isAllShop = (String)PropertyUtils.getProperty(bean, "isAllShop");
	String beginDate = (String)PropertyUtils.getProperty(bean, "beginDate");
	String endDate = (String)PropertyUtils.getProperty(bean, "endDate");
	String isAllCustomer = (String)PropertyUtils.getProperty(bean, "isAllCustomer");
	String customerType = (String)PropertyUtils.getProperty(bean, "customerType");       	
	String vipCheckFalg = (String)PropertyUtils.getProperty(bean, "vipCheckFalg");
	String vipTypeCode = (String)PropertyUtils.getProperty(bean, "vipTypeCode");
	//customer
	promotion.setCustomerType(customerType);
	if("Y".equals(isAllCustomer)){
	    promotion.setIsAllCustomer("Y");
	}else{
	    promotion.setIsAllCustomer("N");
	}
	if(!StringUtils.hasText(vipCheckFalg)){
	    throw new ValidationErrorException("客戶類別選取參數為空值，無法執行存檔！");
	}else if(!StringUtils.hasText(vipTypeCode)){
	    throw new ValidationErrorException("客戶類別參數為空值，無法執行存檔！");
	}
	   
	String[] vipCheckFalgArray = StringTools.StringToken(vipCheckFalg, delim);
	String[] vipTypeCodeArray = StringTools.StringToken(vipTypeCode, delim);
	//System.out.println("vipCheckFalgArray=" + vipCheckFalgArray.length);
	//System.out.println("vipTypeCodeArray=" + vipTypeCodeArray.length);
	List<ImPromotionCustomer> promotionCustomers = promotion.getImPromotionCustomers();
	HashMap customerMap = new HashMap();
	if(promotionCustomers != null && promotionCustomers.size() > 0){
	    for(ImPromotionCustomer promotionCustomerPO : promotionCustomers){
	        Long lineId = promotionCustomerPO.getLineId();
	        String vipTyepCode = promotionCustomerPO.getVipTypeCode();
	        if(!StringUtils.hasText(vipTyepCode)){
	            throw new ValidationErrorException("客戶類別明細主鍵(" + lineId + ")的客戶類別代號為空值！");
	        }else if(customerMap.get(vipTyepCode) != null){
	            throw new ValidationErrorException("客戶類別明細主鍵(" + lineId + ")的客戶類別代號與其他明細重複！");
	        }else{
	            customerMap.put(vipTyepCode, promotionCustomerPO);
	        }
	    }
	}
	//將前端傳入的客戶類別轉成customer bean	       
	for(int i = 0; i < vipTypeCodeArray.length; i++){
	    ImPromotionCustomer customer = (ImPromotionCustomer)customerMap.get(vipTypeCodeArray[i]);
	    if(customer != null){
                if("Y".equals(isAllCustomer)){
		    customer.setEnable("N");
		}else{
	            customer.setEnable(vipCheckFalgArray[i]);
		}
	    }else{
	        ImPromotionCustomer newCustomer = new ImPromotionCustomer();
	        newCustomer.setVipTypeCode(vipTypeCodeArray[i]);
		if("Y".equals(isAllCustomer)){
		    newCustomer.setEnable("N");
		}else{  	             	               
	            newCustomer.setEnable(vipCheckFalgArray[i]);
		}
		promotionCustomers.add(newCustomer);
	    }
	}
	//shop
	if("Y".equals(isAllShop)){
	    promotion.setIsAllShop("Y");
	    if(StringUtils.hasText(beginDate)){
	        promotion.setBeginDate(DateUtils.parseDate("yyyy/MM/dd", beginDate));
	    }
	    if(StringUtils.hasText(endDate)){
	        promotion.setEndDate(DateUtils.parseDate("yyyy/MM/dd", endDate));
	    }
	    promotion.setImPromotionShops(new ArrayList(0));
        }else{
	    promotion.setIsAllShop("N");
	    promotion.setBeginDate(null);
	    promotion.setEndDate(null);
	}
	//item
	promotion.setIsAllItem("N"); 
    }
    
    /**
     * remove delete mark record(item)
     * 
     * @param promotion
     */
    private void removeDeleteMarkLineForItem(ImPromotion promotion){
	       
        List promotionItems = promotion.getImPromotionItems();
	if(promotionItems != null && promotionItems.size() > 0){
	    for(int i = promotionItems.size() - 1; i >= 0; i--){
	        ImPromotionItem promotionItem = (ImPromotionItem)promotionItems.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(promotionItem.getIsDeleteRecord())){
		    promotionItems.remove(promotionItem);
	        }
	    }
	}
    }
    
    /**
     * remove delete mark record(shop)
     * 
     * @param promotion
     */
    private void removeDeleteMarkLineForShop(ImPromotion promotion){
	       
        List promotionShops = promotion.getImPromotionShops();
	if(promotionShops != null && promotionShops.size() > 0){
	    for(int i = promotionShops.size() - 1; i >= 0; i--){
                ImPromotionShop promotionShop = (ImPromotionShop)promotionShops.get(i);
		if(AjaxUtils.IS_DELETE_RECORD_TRUE.equals(promotionShop.getIsDeleteRecord())){
		   promotionShops.remove(promotionShop);
	        }
	    }
	}
    }
    
    /**
     * 重整促銷專櫃、促銷客戶的資料
     * 
     * @param promotion
     */
    public void refreshFieldData(ImPromotion promotion){
	
	//=============isAllShop為N時===============
	if(!"Y".equals(promotion.getIsAllShop())){
	    promotion.setBeginDate(null);
	    promotion.setEndDate(null);
	}else{
	    promotion.setImPromotionShops(new ArrayList(0));
	}
	//=============isAllCustomer為Y時===============
	if("Y".equals(promotion.getIsAllCustomer())){
	    List promotionCustomers = promotion.getImPromotionCustomers();
	    if(promotionCustomers != null){
		for(int i = 0; i < promotionCustomers.size(); i++){
		    ImPromotionCustomer promotionCustomer = (ImPromotionCustomer)promotionCustomers.get(i);
		    promotionCustomer.setEnable("N");
		}
	    }
	}
	//isAllItem預設皆為N
	promotion.setIsAllItem("N");	
    }
}