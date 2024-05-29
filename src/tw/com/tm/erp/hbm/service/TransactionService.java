package tw.com.tm.erp.hbm.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.SoDepartmentOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.bean.Transaction;
import tw.com.tm.erp.hbm.controller.TransactionController;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.SoDepartmentOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.TransactionDAO;
import tw.com.tm.erp.regulation.TransactionInterface;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class TransactionService implements TransactionInterface{
	private static final Log log = LogFactory.getLog(TransactionService.class);
	
	private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
    	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }
    private ImItemDAO imItemDAO;
    public void setImItemDAO(ImItemDAO imItemDAO) {
    	this.imItemDAO = imItemDAO;
    }
    private TransactionDAO transactionDAO;
    public void setTransactionDAO(TransactionDAO transactionDAO) {
    	this.transactionDAO = transactionDAO;
    }
    
    @Override
	//初始化
	public Transaction executeInitTransaction(Map<String ,String> posTransaction) {
    	Transaction transaction = null;
    	
		try {
    		//主檔初始化
			String headIdStr = posTransaction.get("headId");
			String orderTypeCode = posTransaction.get("orderTypeCode");
			String brandCode = posTransaction.get("brandCode");
			String superintendentCode = posTransaction.get("superintendentCode");
			String localCurrencyCode = "NTD";
			String verificationStatus = "N";
			
			log.info("headIdStr="+headIdStr);
			log.info("orderTypeCode="+orderTypeCode);
			log.info("brandCode="+brandCode);
			log.info("superintendentCode="+superintendentCode);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate = DateUtils.parseDate(sdf.format(new Date()));
			
			
			Long headId = StringUtils.hasText(headIdStr) ? Long.valueOf(headIdStr) : null;
			if(headId!=null) {
				log.info("查詢舊的單據");
				//有單就去查詢回來
				transaction = this.findTransactionByHeadId(SoSalesOrderHead.class, headId);
				//log最後異動的紀錄
				
			}else {
				log.info("新的單據");
				//沒有單據起一張新的
				transaction = new Transaction();
				transaction.setOrderNo(AjaxUtils.getTmpOrderNo());
				transaction.setStatus(OrderStatus.SAVE);
				transaction.setOrderTypeCode(orderTypeCode);
				transaction.setBrandCode(brandCode);
				log.info("日期="+currentDate);
				transaction.setSalesOrderDate(currentDate);
				transaction.setCurrencyCode(localCurrencyCode);
				transaction.setSuperintendentCode(superintendentCode);
				transaction.setScheduleShipDate(currentDate);
				transaction.setExportCommissionRate(0D);
				transaction.setExportExchangeRate(1.0D);
				transaction.setVerificationStatus(verificationStatus);
				transaction.setCreatedBy(superintendentCode);
				transaction.setCreationDate(currentDate);
				transaction.setLastUpdatedBy(superintendentCode);
				transaction.setLastUpdateDate(currentDate);
				if(!"SOP".equals(orderTypeCode)||!("T2".equals(brandCode))){
					transaction.setSchedule("99");
	    		}
				if("SOF".equals(orderTypeCode)) {
					transaction.setDiscountRate(0D);
	    		}else if("T2".equals(brandCode) && "SOE".equals(orderTypeCode)){
	    			transaction.setDiscountRate(70D);
	    			transaction.setInvoiceTypeCode("2");
	    		}else{
	    			transaction.setDiscountRate(100D);
	    		}
				transactionDAO.saveTransaction(SoSalesOrderHead.class, transaction);
			}
    		
    	} catch (Exception ex) {
    		log.error("POS初始化失敗，原因：" + ex.toString());
    		ex.printStackTrace();
    	}
		return transaction;
	}
    
	@Override
	public Transaction executeDiscount(Map<String, String> posTransaction) {
		
		Double totalAmount = 0D;
		Long headId = Long.valueOf(posTransaction.get("headId"));
		Long ptr = Long.valueOf(posTransaction.get("ptr")); //ptr=0:全部
		Long vAmount = Long.valueOf(posTransaction.get("vAmount"));
		String executeWay = posTransaction.get("executeWay");
		String category = posTransaction.get("category");//1:折扣 2:折讓
		
		
		System.out.println("headId="+headId);
		
		try {
			
			/**主檔查詢**/
			SoSalesOrderHead head = (SoSalesOrderHead) soSalesOrderHeadDAO.findByPrimaryKey(SoSalesOrderHead.class, headId);
			if(null != head)
			{
				List<SoSalesOrderItem> soSalesOrderItems = head.getSoSalesOrderItems();
				log.info("soSalesOrderItems.size():"+soSalesOrderItems.size());
				if(soSalesOrderItems.size()>0)
				{
					for(SoSalesOrderItem soSalesOrderItem:soSalesOrderItems){
						if(ptr == 0 || soSalesOrderItem.getIndexNo().equals(ptr)){
							if(validateDiscountItem(soSalesOrderItem, executeWay)){//檢核商品
								totalAmount = totalAmount + soSalesOrderItem.getActualSalesAmount();//統計銷售總額供計算全部折讓使用
							}
						}
					}
					log.info("總計:"+totalAmount);
					Long remainderAmount = vAmount;
					for(SoSalesOrderItem soSalesOrderItem:soSalesOrderItems){
						log.info("ptr:"+ptr+"/ INDEX:"+soSalesOrderItem.getIndexNo());
						if(ptr == 0 || soSalesOrderItem.getIndexNo().equals(ptr)){
							if(validateDiscountItem(soSalesOrderItem, executeWay)){//檢核商品
								boolean isLast=soSalesOrderItem.getIndexNo()==soSalesOrderItems.size();
								remainderAmount = calculateDiscountAmount(soSalesOrderItem,vAmount,totalAmount,remainderAmount,category,isLast);//逐筆計算折抵金額並回寫折扣率
							}
						}
					}
					head.setSoSalesOrderItems(soSalesOrderItems);//.setSoDepartmentOrderItem(soSalesOrderItems);//回存單身
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		/**回傳資料**/
//		properties.setProperty("totaleActualSalesAmount", AjaxUtils.getPropertiesValue(totaleActualSalesAmount, "0"));
		return null;
	}
	
	/**檢核商品是否為可折扣商品**/
    private boolean validateDiscountItem(SoSalesOrderItem soSalesOrderItem, String executeWay) throws Exception{
    	boolean isAllDiscountItem =true;
    	boolean isLimited = false;
		ImItem imItem = imItemDAO.findById(soSalesOrderItem.getItemCode());
		if("AUTO".equals(executeWay)){
			isLimited = true;
		}else if("MANUAL".equals(executeWay)){
			isLimited = false;
		}
		log.info("soSalesOrderItem.getItemCode():"+soSalesOrderItem.getItemCode());
		if(null != imItem){
			if("Y".equals(imItem.getIsComposeItem())){
				isAllDiscountItem = false;
			}
			if("Y".equals(imItem.getIsServiceItem())){
				isAllDiscountItem = false;
			}
			if(soSalesOrderItem.getActualSalesAmount() <= 0 ){
				isAllDiscountItem = false;
			}
			if(soSalesOrderItem.getOriginalUnitPrice() <= 0){
				isAllDiscountItem = false;
			}
			if(soSalesOrderItem.getDiscountRate() < 80 && isLimited){
				isAllDiscountItem = false;
			}
		}
		else{
			throw new Exception("商品"+imItem+"品號輸入錯誤");
		}
    	return isAllDiscountItem;
    }
    
    /**計算折扣(讓)金額並回寫折扣率**/
    private Long calculateDiscountAmount(SoSalesOrderItem soSalesOrderItem,Long discountAmount,Double totalAmount,Long remainderAmount,String discountType,boolean isLast) throws Exception{
    	//紀錄原售價
    	Double beforeDiscountAmount = soSalesOrderItem.getActualSalesAmount();
    	int newDiscountRate = 100;
    	if("1".equals(discountType)){//折扣
    		if(discountAmount<100 || discountAmount>0){
    			//計算新售價
		    	Double actualSalesAmount = new Double((int)((soSalesOrderItem.getActualSalesAmount()*discountAmount/100)+0.5)).doubleValue();
		    	soSalesOrderItem.setActualSalesAmount(actualSalesAmount);
    		}
    	}
    	else if("2".equals(discountType)){//折讓
    		
    		if(discountAmount <= totalAmount){
		    	//計算新售價 = 單一商品售價*(1-(折讓金額/目前所有商品售價加總)) 
	    		//Double actualSalesAmount =(soDepartmentOrderItem.getActualSalesAmount() * (1-1/totalAmount*discountAmount));
    			
	    		int actualSalesAmount ;
	    		if(isLast){
	    			log.info("計算新售價 = 單一商品售價-剩餘折讓金額");
	    			log.info("原售價:"+soSalesOrderItem.getActualSalesAmount()+"  剩餘折讓金額:"+remainderAmount);
	    			actualSalesAmount = (int)(soSalesOrderItem.getActualSalesAmount()-remainderAmount); 
	    		}else{
	    			log.info("計算新售價 = 單一商品售價*(1-(折讓金額/目前所有商品售價加總))");
	    			log.info("原售價:"+soSalesOrderItem.getActualSalesAmount()+"  折讓占商品總額比"+(1-(discountAmount/totalAmount)));
	    			actualSalesAmount = (int)(soSalesOrderItem.getActualSalesAmount() * (1-1/totalAmount*discountAmount));
	    		}
	    		soSalesOrderItem.setActualSalesAmount(new Double(actualSalesAmount).doubleValue());
	//	    	Double actualSalesAmount = new Double((int)((soDepartmentOrderItem.getActualSalesAmount()*discountAmount/100)+0.5)).doubleValue();
    		}else{
    			throw new Exception("折讓金額錯誤，折讓金額不可超過商品售價");
    		}
    	}else{
    		throw new Exception("折扣類型輸入錯誤");
    	}
    	//回寫折扣率
    	double newActureUnitSalesAmount = soSalesOrderItem.getActualSalesAmount()/soSalesOrderItem.getQuantity();//單一售價
		newDiscountRate = (int)(newActureUnitSalesAmount/soSalesOrderItem.getOriginalUnitPrice()*100+0.5);
		soSalesOrderItem.setDiscountRate(new Double(newDiscountRate).doubleValue());
		log.info("   折扣前金額:" + beforeDiscountAmount + " /   折扣後金額:" + soSalesOrderItem.getActualSalesAmount() + " =  折扣率:" + soSalesOrderItem.getDiscountRate());
		double discountNum = sub( beforeDiscountAmount,soSalesOrderItem.getActualSalesAmount() );
		soSalesOrderItem.setDiscountType(discountType);
		soSalesOrderItem.setDiscount(discountNum);
		log.info("  折扣/讓 金額："+discountNum +",type:"+discountType);
		remainderAmount = remainderAmount - (int)(beforeDiscountAmount-soSalesOrderItem.getActualSalesAmount());
		log.info("剩餘折讓金額:"+remainderAmount);
    	return remainderAmount;
    }
    
    public static double sub(double v1,double v2){ 
    	BigDecimal b1 = new BigDecimal(Double.toString(v1)); 
    	BigDecimal b2 = new BigDecimal(Double.toString(v2)); 
    	return b1.subtract(b2).doubleValue(); 
    }
    
    public Transaction findTransactionByHeadId(Class findClass, Long headId) throws Exception {
    	Transaction transaction = null;
    	try {
    		transaction = (Transaction) transactionDAO.findTransactionByHeadId(findClass, headId);
    	    return transaction;
    	} catch (Exception ex) {
    	    log.error("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因：" + ex.toString());
    	    throw new Exception("依據主鍵：" + headId + "查詢銷售單主檔時發生錯誤，原因："+ ex.getMessage());
    	}
	}

}
