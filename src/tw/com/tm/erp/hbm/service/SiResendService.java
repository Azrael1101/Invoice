package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.utils.sp.AppExtendItemInfoService;

public class SiResendService {

    private static final Log log = LogFactory.getLog(SiUsersGroupService.class);
    
    private BuShopDAO buShopDAO;
    public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}
    
    private BuCommonPhraseService buCommonPhraseService;
    public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}
    
    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
		this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
	}
    
    private AppExtendItemInfoService appExtendItemInfoService;
    public void setAppExtendItemInfoService(
			AppExtendItemInfoService appExtendItemInfoService) {
		this.appExtendItemInfoService = appExtendItemInfoService;
	}
    
    private SoSalesOrderMainService soSalesOrderMainService;
    public void setSoSalesOrderMainService(
			SoSalesOrderMainService soSalesOrderMainService) {
		this.soSalesOrderMainService = soSalesOrderMainService;
	}
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO){
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
    public void updateSoCmData(SoSalesOrderHead soSalesOrderHead)throws Exception{
		String errorMsg = null;
		log.info("AAAAAAAAAAAAAAAA取報單");
		try {
			HashMap conditionMap = new HashMap();
			conditionMap.put("processObjectName", "soSalesOrderMainService");
			conditionMap.put("searchMethodName", "findSoSalesOrderHeadById");
			conditionMap.put("tableType", "SO_SALES_ORDER");
			conditionMap.put("subEntityBeanName", "soSalesOrderItems");
			conditionMap.put("itemFieldName", "itemCode");
			conditionMap.put("warehouseCodeFieldName", "warehouseCode");
			conditionMap.put("declTypeFieldName", "importDeclType");
			conditionMap.put("declNoFieldName", "importDeclNo");
			conditionMap.put("declSeqFieldName", "importDeclSeq");
			conditionMap.put("declDateFieldName", "importDeclDate");
			conditionMap.put("lotFieldName", "lotNo");
			conditionMap.put("qtyFieldName", "quantity");
				if(!OrderStatus.SIGNING.equals(soSalesOrderHead.getStatus()) 
						&& !OrderStatus.UNCONFIRMED.equals(soSalesOrderHead.getStatus())
						&& !OrderStatus.SAVE.equals(soSalesOrderHead.getStatus())){
					return;
				}

				Long soSalesOrderHeadId = soSalesOrderHead.getHeadId();
				//取店別
				//log.info("====================soSalesOrderHead.headId : " + soSalesOrderHead.getHeadId() + "，shopCode : "+soSalesOrderHead.getShopCode()+"====================");
				BuShop buShop = buShopDAO.findById(soSalesOrderHead.getShopCode());
				if(null == buShop){
					throw new Exception("查無店號: "+soSalesOrderHead.getShopCode()+"之專櫃店號");
				}
				if(null == buShop.getShopSalesType())
				{
					throw new Exception("店號: "+soSalesOrderHead.getShopCode()+"尚未設定是否展報單");
				}
				BuCommonPhraseLine line = buCommonPhraseService.getBuCommonPhraseLine("ShopSalesType", buShop.getShopSalesType());
				if(null != line && "Y".equals(line.getAttribute1()))
				{
					//log.info("===================展報單 soSalesOrderHead.headId : " + soSalesOrderHead.getHeadId() + "===================");
					try{
						conditionMap.put("searchKey", soSalesOrderHeadId);
						appExtendItemInfoService.executeExtendItemProcessor(conditionMap);
						soSalesOrderHead = (SoSalesOrderHead)soSalesOrderHeadDAO.findById("SoSalesOrderHead", soSalesOrderHead.getHeadId());
					}
					catch(Exception ex)
					{
						log.info("核銷報單時發生錯誤");
					}
					
				}
				String identification = MessageStatus.getIdentification(soSalesOrderHead.getBrandCode(), 
				soSalesOrderHead.getOrderTypeCode(), soSalesOrderHead.getOrderNo());
				List assemblyMsg = soSalesOrderMainService.countTotalAmountForT2Pos(null, null, null, identification, "POS", soSalesOrderHead);	
				if(!"99".equals(soSalesOrderHead.getSchedule())){
				    Date date = new Date();
				    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("StraightUploadCustoms", "Switch");
				    if("Y".equals(buCommonPhraseLine.getEnable()) && !(date.before(soSalesOrderHead.getSalesOrderDate()))){
					soSalesOrderHead.setCustomsStatus("A");
					soSalesOrderHead.setTranAllowUpload("I");
				    }
				}
		} catch (Exception ex) {
			errorMsg = "POS銷售資料核銷報單時發生錯誤，原因：" + ex.getMessage();
			throw new Exception(errorMsg);
		}
	}

}
