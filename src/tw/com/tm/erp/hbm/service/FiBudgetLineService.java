package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;

import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.FiBudgetHead;
import tw.com.tm.erp.hbm.bean.FiBudgetLine;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;

import tw.com.tm.erp.hbm.dao.FiBudgetHeadDAO;
import tw.com.tm.erp.hbm.dao.FiBudgetLineDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;

/**
 * 採購預算 Line Service
 * @author Mac8
 *
 */
public class FiBudgetLineService {
    private static final Log log = LogFactory.getLog(FiBudgetLineService.class);
    private FiBudgetHeadDAO fiBudgetHeadDAO;
    private FiBudgetLineDAO fiBudgetLineDAO;
    
    private FiBudgetHeadService   fiBudgetHeadService;
    private BuBrandService        buBrandService;
    private ImItemService         imItemService;
    private ImItemCategoryService imItemCategoryService;
    private FiInvoiceHeadDAO fiInvoiceHeadDAO;
    private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO;
    
    /** 依類別更新預算檔 fiBudgetLine
     * budgetType: RECEIVE->進貨金額, POACTUAL->採購核准金額, SIGNING->PO簽核中金額, ADJUST->調整金額
     * @param brandCode
     * @param budgetYear
     * @param budgetMonth
     * @param itemCode
     * @param budgetType
     * @param budgetAmount
     * @throws Exception
     */
    public void updateFiBudgetLineByItemCode( String brandCode, String budgetYear, String budgetMonth, 
	    	String itemCode, String budgetType, Double budgetAmount, String loginUser, boolean isReject ) throws Exception {
	log.info("updateFiBudgetLineByItemCode"+budgetAmount);
	
	BuBrand buBrand = buBrandService.findById( brandCode );
	String errorMsg;
	if ("M".equals(buBrand.getBudgetCheckType()) && StringUtils.hasText(budgetMonth) ){	// 預算扣除類別 Y:year/M:month
	    errorMsg = "無此預算年月 " + budgetYear +"/" + budgetMonth + "，請檢查!";
	}else{
	    errorMsg = "無此預算年度 " + budgetYear + "，請檢查!";
	}
	
	try{
		
		
	    ImItem imItem = imItemService.findItem(brandCode, itemCode);
	    if(null==imItem){
    		throw new FormException( "查無此貨號："+ brandCode +"-"+ itemCode );
    	    }
	    String categoryType = null;
	    List<ImItemCategory> allItemCategory = imItemCategoryService.findByCategoryType(brandCode, "ITEM_CATEGORY");
	    for(ImItemCategory imItemCategory : allItemCategory ){
			if( imItem.getItemCategory().equals( imItemCategory.getId().getCategoryCode() ) ){
			    //categoryType = imItemCategory.getParentCategoryCode();
				categoryType = imItemCategory.getId().getCategoryCode();
			    break;
			}
	    }
	    HashMap findObjs = new HashMap();
	    findObjs.put("orderTypeCode", FiBudgetHead.BUDGET_ORDER_TYPE_CODE_PO);
	    findObjs.put("brandCode",     brandCode );
	    findObjs.put("categoryType",  categoryType );
	    findObjs.put("budgetYear",    budgetYear );
	    if ("M".equals(buBrand.getBudgetCheckType()) && StringUtils.hasText(budgetMonth) ){	// 預算扣除類別 Y:year/M:month
	    	findObjs.put("budgetMonth",  budgetMonth );
	    }
	    
	    FiBudgetHead fiBudgetHead;
	    FiBudgetLine fiBudgetLine = null;
	    List fiBudgetHeads = fiBudgetHeadService.find(findObjs);	// 取 Budget Head Data
	    if (null != fiBudgetHeads && fiBudgetHeads.size() > 0) {
	    	fiBudgetHead = (FiBudgetHead) fiBudgetHeads.get(0);
	    }else{
	    	throw new FormException(errorMsg);
	    }
	    
	    List<FiBudgetLine> fiBudgetLines;
	    if ("M".equals(buBrand.getBudgetCheckType()) && StringUtils.hasText(budgetMonth) ){
		fiBudgetLines = fiBudgetLineDAO.find( fiBudgetHead.getHeadId(), imItem.getBudgetType(),
				imItem.getItemBrand(), imItem.getCategory01(), imItem.getCategory02());
	    }else{				// 其他僅有一筆 fiBudgetLine
	    	if(brandCode.equals("T2")){
				fiBudgetLines = fiBudgetHead.getFiBudgetLines();
				for (FiBudgetLine fiBudgetLine2 : fiBudgetLines) {
					log.info("T2其他-- fiBudgetLine");
					if(imItem.getItemBrand().equalsIgnoreCase(fiBudgetLine2.getItemBrandCode()) && imItem.getCategory01().equalsIgnoreCase(fiBudgetLine2.getCategoryTypeCode1())){
						log.info("T2其他--1");
					fiBudgetLine = fiBudgetLineDAO.findByRRItemFi(fiBudgetLine2.getFiBudgetHead().getHeadId(), imItem.getItemBrand(),imItem.getCategory01());
					log.info("品牌+大類::"+fiBudgetLine.getItemBrandCode()+fiBudgetLine.getCategoryTypeCode1());
					}else if(imItem.getItemBrand().equalsIgnoreCase(fiBudgetLine2.getItemBrandCode())){
						log.info("T2其他--2");
						fiBudgetLine = fiBudgetLineDAO.findByPoItem(fiBudgetLine2.getFiBudgetHead().getHeadId(), imItem.getItemBrand());
						log.info("品牌::"+fiBudgetLine.getItemBrandCode());						
					}	
					if(null == fiBudgetLine){
						log.info("T2其他--3");
						if(imItem.getCategory01().equalsIgnoreCase(fiBudgetLine2.getCategoryTypeCode1())){
						fiBudgetLine = fiBudgetLineDAO.findByRRItem(fiBudgetLine2.getFiBudgetHead().getHeadId(),imItem.getCategory01());
						log.info("大類::"+fiBudgetLine.getCategoryTypeCode1());
						}
					}else{
						log.info("T2其他--4");
						fiBudgetLine = fiBudgetLineDAO.findByPoItem(fiBudgetLine2.getFiBudgetHead().getHeadId(),"OTHER");
						log.info("OTHER類::"+fiBudgetLine.getItemBrandCode());
					}
				}
			}else{
	    	fiBudgetLines = fiBudgetLineDAO.find( fiBudgetHead.getHeadId(), "A", "ALL", "ALL", "ALL" );
			}
	    }
	    if (null != fiBudgetLines && fiBudgetLines.size() > 0) {
	    	if(brandCode.equals("T2")){
				//fiBudgetLine = fiBudgetLineDAO.findByPoItem(fiBudgetHead.getHeadId(),imItem.getItemBrand());
				//fiBudgetLine = fiBudgetLineDAO.findByLine(itemBrandCode);
				//log.info("updateBudgetline"+fiBudgetLine.getFiBudgetHead().getHeadId());
				//log.info("updateBudgetline"+fiBudgetLine.getLineId());
				}else{
	    	fiBudgetLine = (FiBudgetLine) fiBudgetLines.get(0);
				}
	    }else{
	    	throw new FormException(errorMsg);
	    }
	    //log.info("XXXXXXXXXX2:"+fiBudgetHead.getHeadId()+"-"+fiBudgetLine.getLineId());
	    Double receiveAmount  = (fiBudgetLine.getReceiveActualAmount() == null)? 0D: fiBudgetLine.getReceiveActualAmount();
	    Double poActualAmount = (fiBudgetLine.getPoActualAmount()      == null)? 0D: fiBudgetLine.getPoActualAmount();
	    Double signingAmount  = (fiBudgetLine.getSigningAmount()       == null)? 0D: fiBudgetLine.getSigningAmount();
	    Double adjustAmount   = (fiBudgetLine.getAdjustActualAmount()  == null)? 0D: fiBudgetLine.getAdjustActualAmount();
	    if(isReject)
	    	//budgetAmount = budgetAmount*(-1);
	    log.info("budgetAmount++"+budgetAmount);
	    	//adjustAmount = adjustAmount* (-1);
	    if("RECEIVE".equals(budgetType)){
	    	receiveAmount  += budgetAmount;
	    }else if("POACTUAL".equals(budgetType)){
	    	poActualAmount += budgetAmount;
	    }else if("SIGNING".equals(budgetType)){
	    	signingAmount  += budgetAmount;
	    }else if("ADJUST".equals(budgetType)){
	    	adjustAmount   +=  budgetAmount;
	    }
	    fiBudgetLine.setReceiveActualAmount(receiveAmount);
	    fiBudgetLine.setPoActualAmount(     poActualAmount);
	    fiBudgetLine.setSigningAmount(      signingAmount);
	    fiBudgetLine.setAdjustActualAmount( adjustAmount);
	    modifyFiBudgetLine(fiBudgetLine, loginUser);
	    
	}catch (Exception ex) {
	    log.error("異動預算失敗，原因：" + ex.toString());
	    throw new Exception("異動預算失敗，原因：" + ex.getMessage());
	}
    }
    
    
    /**修改預算資料
     * @param FiBudgetLine fiBudgetLine
     */
    public void modifyFiBudgetLine( FiBudgetLine fiBudgetLine, String loginUser ) {
	log.info("FiBudgetLineService.modifyFiBudgetLine");
	fiBudgetLine.setLastUpdatedBy(loginUser);
	fiBudgetLine.setLastUpdateDate(new Date());
	fiBudgetLineDAO.update(fiBudgetLine);
	}


    public FiBudgetHeadService getFiBudgetHeadService() {
        return fiBudgetHeadService;
    }

    public void setFiBudgetHeadService(FiBudgetHeadService fiBudgetHeadService) {
        this.fiBudgetHeadService = fiBudgetHeadService;
    }
    
    public FiBudgetHeadDAO getFiBudgetHeadDAO() {
        return fiBudgetHeadDAO;
    }

    public void setFiBudgetHeadDAO(FiBudgetHeadDAO fiBudgetHeadDAO) {
        this.fiBudgetHeadDAO = fiBudgetHeadDAO;
    }

    public FiBudgetLineDAO getFiBudgetLineDAO() {
        return fiBudgetLineDAO;
    }

    public void setFiBudgetLineDAO(FiBudgetLineDAO fiBudgetLineDAO) {
        this.fiBudgetLineDAO = fiBudgetLineDAO;
    }
    
    public BuBrandService getBuBrandService() {
        return buBrandService;
    }

    public void setBuBrandService(BuBrandService buBrandService) {
        this.buBrandService = buBrandService;
    }

    public ImItemService getImItemService() {
        return imItemService;
    }

    public void setImItemService(ImItemService imItemService) {
        this.imItemService = imItemService;
    }
    
    public void setImItemCategoryService(ImItemCategoryService imItemCategoryService) {
	this.imItemCategoryService = imItemCategoryService;
}
    public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
    	this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
    }
    public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
    	this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
    }
    
}
