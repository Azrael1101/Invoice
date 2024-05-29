package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBranch;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionCustomer;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionShop;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionItemDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionShopDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.CommonUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.OperationUtils;
import tw.com.tm.erp.utils.StringTools;
import tw.com.tm.erp.utils.UserUtils;

public class ImPromotionService {
	private static final Log log = LogFactory.getLog(ImPromotionService.class);
	public static final String PROGRAM_ID= "IM_PROMOTION";
	private ImPromotionDAO imPromotionDAO;
	private ImPromotionItemDAO imPromotionItemDAO;
	private ImPromotionShopDAO imPromotionShopDAO;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private BuShopDAO buShopDAO;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	private ImPromotionItemService imPromotionItemService;
	private BuBrandService buBrandService;
	private BuOrderTypeService buOrderTypeService;
	private SiProgramLogAction siProgramLogAction;
	
	public static final String[] GRID_FIELD_NAMES = { "indexNo", "itemCode", "itemName", "standardPurchaseCost", "originalPrice", 
		"discountAmount", "discountPercentage", "totalDiscountAmount", "totalDiscountPercentage", "quantity", "reserve5",
		"lineId", "isLockRecord", "isDeleteRecord", "message"};
	    
	public static final int[] GRID_FIELD_TYPES = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	        AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE,
		AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_DOUBLE, AjaxUtils.FIELD_TYPE_STRING,
		AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
	    
	public static final String[] GRID_FIELD_DEFAULT_VALUES = { "0", "", "", "", "", "", "", "", "", "", "", "",
	        AjaxUtils.IS_LOCK_RECORD_FALSE, AjaxUtils.IS_DELETE_RECORD_FALSE, ""};
		
	public static final String[] GRID_FIELD_NAMES_SHOP = { "indexNo", "shopCode", "shopName", "beginDate", "endDate", 
		"reserve5", "lineId", "isLockRecord", "isDeleteRecord", "message"};
	    
	public static final int[] GRID_FIELD_TYPES_SHOP = { AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
	        AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
		AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING};
	    
	public static final String[] GRID_FIELD_DEFAULT_VALUES_SHOP = { "0", "", "", "", "", "", "", AjaxUtils.IS_LOCK_RECORD_FALSE,
	        AjaxUtils.IS_DELETE_RECORD_FALSE, ""};
		
	public void setImPromotionDAO(ImPromotionDAO imPromotionDAO) {
		this.imPromotionDAO = imPromotionDAO;
	}
	
	public void setImPromotionItemDAO(ImPromotionItemDAO imPromotionItemDAO) {
		this.imPromotionItemDAO = imPromotionItemDAO;
	}
	
	public void setImPromotionShopDAO(ImPromotionShopDAO imPromotionShopDAO) {
		this.imPromotionShopDAO = imPromotionShopDAO;
	}
	
	public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}
	
	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}
	
	public void setBuEmployeeWithAddressViewDAO(
                BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}
	
	public void setImPromotionItemService(
		ImPromotionItemService imPromotionItemService) {
		this.imPromotionItemService = imPromotionItemService;
	}
	
	public void setBuBrandService(BuBrandService buBrandService) {
		this.buBrandService = buBrandService;
	}
	
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}
	
	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}
	
	public String save(ImPromotion imPromotion, String formAction, String loginUser) throws Exception {
		//String result = "Error";
		try {
			
			
			/* set enable value */
			if ("Y".equals(imPromotion.getIsAllCustomer()))
				imPromotion.setImPromotionCustomers(new ArrayList(0));
			else 
				imPromotion.setIsAllCustomer("N");
			if ("Y".equals(imPromotion.getIsAllItem()))
				imPromotion.setImPromotionItems(new ArrayList(0));
			else
				imPromotion.setIsAllItem("N");
			
			if ("Y".equals(imPromotion.getIsAllShop())) 
				imPromotion.setImPromotionShops(new ArrayList(0));
			else
				imPromotion.setIsAllShop("N");
			
			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
				System.out.println("1.check promption data");
				this.checkImPromotionData(imPromotion);
			}
			System.out.println("2.save promption data");
			if (imPromotion.getOrderNo() == null
					|| "".equals(imPromotion.getOrderNo())) {
				// 新增
				// 取得最新一筆流水號
				if (!(loginUser.equals(""))) {
					UserUtils.setUserAndDate(imPromotion, loginUser);
					UserUtils.setUserAndDate(imPromotion.getImPromotionItems(), loginUser);
					UserUtils.setUserAndDate(imPromotion.getImPromotionShops(), loginUser);
					UserUtils.setUserAndDate(imPromotion.getImPromotionCustomers(), loginUser);
					UserUtils.setUserAndDate(imPromotion.getImPromotionFiles(), loginUser);
				}
				System.out.println("3.get order No");
				String serialNo = ServiceFactory.getInstance()
				.getBuOrderTypeService().getOrderSerialNo(imPromotion.getBrandCode(),imPromotion.getOrderTypeCode());
				if (!serialNo.equals("unknow")) {
					imPromotion.setOrderNo(serialNo);
					System.out.println("3.reset reserve");
					this.resetLineReserve(imPromotion);
					imPromotionDAO.save(imPromotion);
				} else {
					throw new ObtainSerialNoFailedException("取得"
							+ imPromotion.getOrderTypeCode() + "單號失敗！");
				}
				return imPromotion.getOrderTypeCode() + "-" + serialNo	+ "存檔成功！";
			} else {
				imPromotionDAO.update(imPromotion);				
				return imPromotion.getOrderTypeCode() + "-" + imPromotion.getOrderNo()	+ "存檔成功！";
			}

		} catch (FormException fe) {
			log.error("促銷活動申請單存檔時發生錯誤，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("促銷活動申請單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("促銷活動申請單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	
	}
	
	public void resetLineReserve(ImPromotion resetObj) {
		List<ImPromotionItem>objItems = resetObj.getImPromotionItems();
		for (ImPromotionItem item : objItems) {
			item.setReserve1("Okay ");
			item.setReserve2("");
			item.setReserve3("");
			item.setReserve4("");
			item.setReserve5("");
		}
		List<ImPromotionShop>objShops = resetObj.getImPromotionShops();
		for (ImPromotionShop shop : objShops) {
			shop.setReserve1("Okay ");
			shop.setReserve2("");
			shop.setReserve3("");
			shop.setReserve4("");
			shop.setReserve5("");
		}
	}

	private void checkImPromotionData(ImPromotion imPromotion) throws ValidationErrorException {
		
		if (imPromotion.getIsAllItem().equals("Y")){
			if (imPromotion.getDiscount() == 0L || imPromotion.getDiscount()==null)
				throw new ValidationErrorException("選擇所有商品,但未輸入折扣比率");			
		}else{
			System.out.println("1-1.check item promption data");
			this.checkImPromotionItem(imPromotion);
		}
		
		if (imPromotion.getIsAllShop().equals("Y")){			
			if (imPromotion.getBeginDate() == null)
				throw new ValidationErrorException("選擇所有專櫃,但未輸入起始日期");
			if (imPromotion.getEndDate() == null)
				throw new ValidationErrorException("選擇所有專櫃,但未輸入結束日期");			
		}else{
			System.out.println("1-2.check shop promption data");
			this.checkImPromotionShop(imPromotion);
		}
		
		if (!(imPromotion.getIsAllCustomer().equals("Y"))) {
			System.out.println("1-3.check customer promption data");
			this.checkImPromotionCustomer(imPromotion);
		}	
	}

	
	private void checkImPromotionItem(ImPromotion imPromotion) throws ValidationErrorException {	
		ArrayList  removeArray = new ArrayList(0);
		Integer errSeq = new Integer(0);
		Integer sequence = new Integer(0);
		Integer mapSequence = new Integer(0);
		List<ImPromotionItem> items = imPromotion.getImPromotionItems();
		HashMap itemMap = new HashMap();
		if (items.size() > 0) {
			sequence = 0;
			for (ImPromotionItem item : items) {
				sequence++;
				if(StringUtils.hasText(item.getItemCode())){
					mapSequence = (Integer)itemMap.get(item.getItemCode());
					if (mapSequence == null || mapSequence == 0  ){
						itemMap.put(item.getItemCode(), sequence);					
					}else{
						throw new ValidationErrorException("第"	+ sequence.toString().trim() + 
								"列品號("+item.getItemCode()+")與第"	+ mapSequence.toString().trim() + 
								"列品號相同，請確認");
					}
					if ( imItemCurrentPriceViewDAO.findCurrentPriceByValue
							(imPromotion.getBrandCode(), item.getItemCode(), imPromotion.getPriceType()) != null){
						if ((item.getDiscountAmount() == null || item.getDiscountAmount() == 0D)
								&& (item.getDiscountPercentage() == null || item.getDiscountPercentage() == 0D)) {
							throw new ValidationErrorException("第"	+ sequence.toString().trim() + 
									"列商品代號("+item.getItemCode()+")尚未輸入促銷金額或折扣數");
	
						} else if ((item.getDiscountAmount() != null && item.getDiscountAmount() != 0D)
								&& (item.getDiscountPercentage() != null && item.getDiscountPercentage() != 0D)) {
							throw new ValidationErrorException("第"	+ sequence.toString().trim() + 
									"列商品代號("+item.getItemCode()+")促銷金額及折扣數重覆輸入");
						} else if (item.getDiscountAmount() >= item.getOriginalPrice()) {
							item.setTotalDiscountAmount(0D);
							item.setTotalDiscountPercentage(0D);
							throw new ValidationErrorException("第"	+ sequence.toString().trim() + 
									"列商品代號("+item.getItemCode()+")促銷金額不可大於或等於定價");						
	
						} else if (item.getDiscountPercentage() > 100D) {
							item.setTotalDiscountAmount(0D);
							item.setTotalDiscountPercentage(0D);
							throw new ValidationErrorException("第"	+ sequence.toString().trim() + 
									"列商品代號("+item.getItemCode()+")折扣比率不可大於100%");	
						} else {
							/*
							if (item.getDiscountAmount() != null && item.getDiscountAmount() != 0D) {
								item.setDiscountType("1");
								item.setDiscount(item.getOriginalPrice() - item.getDiscountAmount());
								item.setTotalDiscountAmount(item.getDiscount());
								item.setTotalDiscountPercentage( OperationUtils
										.divison(item.getDiscount(), item.getOriginalPrice(), 4).doubleValue() * 100D);
							} else {
								item.setDiscountType("2");
								item.setDiscount( 100D - item.getDiscountPercentage());
								item.setTotalDiscountAmount(item.getOriginalPrice() * (item.getDiscount() / 100D));
								item.setTotalDiscountPercentage(item.getDiscount());
							}*/
							if (item.getDiscountAmount() != null && item.getDiscountAmount() != 0D) {
								item.setDiscountType("1");
								item.setDiscount(item.getOriginalPrice() - item.getDiscountAmount());
								item.setTotalDiscountAmount(item.getDiscountAmount());
								item.setTotalDiscountPercentage( 
										OperationUtils.divison(item.getDiscountAmount(), item.getOriginalPrice(), 4).doubleValue() * 100D);
							} else {
								item.setDiscountType("2");
								item.setDiscount( 100D - item.getDiscountPercentage());
								item.setTotalDiscountAmount(item.getOriginalPrice() * (item.getDiscountPercentage() / 100D));
								item.setTotalDiscountPercentage(item.getDiscountPercentage());
							}
							item.setReserve1("Okay ");
						}
					}else{
						throw new ValidationErrorException("查無第"	+ sequence.toString().trim() + 
								"列商品代號("+item.getItemCode()+")");
					}
				}else{
					removeArray.add(item);
				}
			
			}
			System.out.println("before remove size:"+items.size());		
			for(int j = removeArray.size() ; 0 < j ; j--){	
				items.remove(removeArray.get(j-1));
			}
			
			System.out.println("after remove size:"+items.size());
		} else {
			errSeq++;
			throw new ValidationErrorException(errSeq.toString().trim() + "未輸入任何商品資料 ");
		}

	}
	
	public void refreshPromotion(ImPromotion imPromotion) throws Exception {
		
            try{
        	Long headId = imPromotion.getHeadId();
	        String brandCode = imPromotion.getBrandCode();
	        String formStatus = imPromotion.getStatus();
		String inCharge = imPromotion.getInCharge();
		String inChargeName = "查無此負責人員";
		//訂單負責人
		if(StringUtils.hasText(inCharge)){
		    BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(brandCode, inCharge);
		    if(employeeWithAddressView != null){
		        inChargeName = employeeWithAddressView.getChineseName();
	            }
		    imPromotion.setInChargeName(inChargeName);
		}
		if(headId != null && (OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus))){
     	            imPromotionItemService.saveItemData(headId);
		}
	    }catch(Exception ex){
		log.error("查詢促銷單相關資訊時發生錯誤，原因：" + ex.toString());
		throw new Exception("查詢促銷單相關資訊時發生錯誤，原因：" + ex.getMessage());
            }
	}
	
	public void refreshPromotionItem( String brandCode, String priceType, String status , ImPromotionItem item) throws ValidationErrorException {	

		
		ImItemCurrentPriceView imItemCurrentPriceView = 
			 imItemCurrentPriceViewDAO.findCurrentPriceByValue(brandCode, item.getItemCode(), priceType);
		if ( imItemCurrentPriceView != null){
			item.setItemName(imItemCurrentPriceView.getItemCName());
			if(OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)){
			   item.setStandardPurchaseCost(imItemCurrentPriceView.getStandardPurchaseCost());
			   item.setOriginalPrice(imItemCurrentPriceView.getUnitPrice());
			   if ((item.getDiscountAmount() == null || item.getDiscountAmount() == 0D)
						&& (item.getDiscountPercentage() == null || item.getDiscountPercentage() == 0D)) {
				   item.setReserve1("Err - 尚未輸入促銷金額或折扣數");

				} else if ((item.getDiscountAmount() != null && item.getDiscountAmount() != 0D)
						&& (item.getDiscountPercentage() != null && item.getDiscountPercentage() != 0D)) {
					item.setReserve1("Err - 促銷金額及折扣數重覆輸入");
				} else if (item.getDiscountAmount() >= item.getOriginalPrice()) {				
					item.setReserve1("Err - 促銷金額不可大於或等於定價");
				} else if (item.getDiscountPercentage() > 100D) {
					item.setReserve1("Err - 折扣比率不可大於100");
				} else {
					if (item.getDiscountAmount() != null && item.getDiscountAmount() != 0D) {
						item.setDiscountType("1");
						item.setDiscount(item.getOriginalPrice() - item.getDiscountAmount());
						item.setTotalDiscountAmount(item.getDiscountAmount());
						item.setTotalDiscountPercentage( 
								OperationUtils.divison(item.getDiscountAmount(), item.getOriginalPrice(), 4).doubleValue() * 100D);
					} else {
						item.setDiscountType("2");
						item.setDiscount( 100D - item.getDiscountPercentage());
						item.setTotalDiscountAmount(item.getOriginalPrice() * (item.getDiscountPercentage() / 100D));
						item.setTotalDiscountPercentage(item.getDiscountPercentage());
					}
					item.setReserve1("Okay ");
				}
			}
		}else{
			item.setItemName("查無此商品資料");
			item.setReserve1("Err - 查無此商品資料");
			
		}
	}
	
	public void refreshPromotionShop( String brandCode, String status , ImPromotionShop shop) throws ValidationErrorException {
		BuShop buShop = buShopDAO.findEnableShopById(brandCode, shop.getShopCode());
		if ( buShop != null){
			shop.setShopName(buShop.getShopCName());
		}else{
			shop.setShopName("查無此專櫃資料");
			shop.setReserve1("Err - 查無此專櫃資料");
			
		}
	}
	private void checkImPromotionShop(ImPromotion imPromotion) throws ValidationErrorException {	
		ArrayList  removeArray = new ArrayList(0);
		Integer errSeq = new Integer(0);
		Integer sequence = new Integer(0);
		Integer mapSequence = new Integer(0);
		List<ImPromotionShop> shops = imPromotion.getImPromotionShops();
		HashMap itemMap = new HashMap();
		if (shops.size() > 0) {
			sequence = 0;
			for (ImPromotionShop shop : shops) {
				sequence++;
				if(StringUtils.hasText(shop.getShopCode())){
					mapSequence = (Integer)itemMap.get(shop.getShopCode());
					if (mapSequence == null || mapSequence == 0 ){
						itemMap.put(shop.getShopCode(), sequence);					
					}else{
						throw new ValidationErrorException("第"	+ sequence.toString().trim() + 
								"列專櫃代號("+shop.getShopCode()+")與第"	+ mapSequence.toString().trim() + 
								"列專櫃代號相同，請確認");
					}
					if ( buShopDAO.findEnableShopById(imPromotion.getBrandCode(), shop.getShopCode())!= null){
						if (shop.getBeginDate() == null	|| shop.getBeginDate().equals("")) {
								throw new ValidationErrorException("查無第"+ sequence.toString().trim() + 
										"列專櫃代號("+ shop.getShopCode() + ")未輸入促銷活動起始日期");
						} else if (shop.getEndDate() == null || shop.getEndDate().equals("")) {
							throw new ValidationErrorException("查無第"+ sequence.toString().trim() + 
									"列專櫃代號("+ shop.getShopCode() + ")未輸入促銷活動結束日期");
						} else if (DateUtils.daysBetween(shop.getBeginDate(), shop.getEndDate()) < 0) {
							throw new ValidationErrorException("查無第"+ sequence.toString().trim() + 
									"列專櫃代號("+ shop.getShopCode() + ")活動結束日期大於起始日期");
						}
					} else {
						throw new ValidationErrorException("查無第"+ sequence.toString().trim() + 
								"列專櫃代號("+ shop.getShopCode() + ")");
					}
				}else{
					removeArray.add(shop);
				}
			}
			System.out.println("before remove size:"+shops.size());		
			for(int j = removeArray.size() ; 0 < j ; j--){	
				shops.remove(removeArray.get(j-1));
			}
			
			System.out.println("after remove size:"+shops.size());
		} else {
			errSeq++;
			throw new ValidationErrorException(errSeq.toString().trim() + "未輸入任何專櫃資料 ");
		}

	}
	
	private void checkImPromotionCustomer(ImPromotion imPromotion) throws ValidationErrorException {
		boolean isPickCustomerType = false;
		List<ImPromotionCustomer> customers = imPromotion.getImPromotionCustomers();
		if (customers.size() > 0) {			
			for (ImPromotionCustomer customer : customers) {				
				if (customer.getEnable() == null )
					customer.setEnable("N");
				else if (customer.getEnable().equals("Y")) 					
					isPickCustomerType = true;
			}
		}
		
		if (!isPickCustomerType)
			throw new ValidationErrorException("未選擇客戶類別 ");
	}
	
	public List<ImPromotion> findPromotionListByValue(String brandCode,
			String orderType, String startOrderNo, String endOrderNo,
			String startDate, String endDate, String status,
			String employeeCode, String categorySearchString) {
		try {

			StringBuffer hql = new StringBuffer("from ImPromotion as model ");
			hql.append("where model.brandCode = '" + brandCode + "' ");
			hql.append("and model.orderTypeCode = '" + orderType + "' ");
			// 判斷針對OrderNo欄位是否要用模糊查詢(LIKE)的進行Query
			// 當startOrderNo或endOrderNo有一參數為空白或Null時，即使用模糊查詢
			if ((startOrderNo == null || "".equals(startOrderNo))
					|| (endOrderNo == null || "".equals(endOrderNo))) {
				hql
						.append((startOrderNo != null
								&& !"".equals(startOrderNo) ? "and model.orderNo LIKE '%"
								+ startOrderNo + "%' "
								: ""));
				hql
						.append((endOrderNo != null && !"".equals(endOrderNo) ? "and model.orderNo LIKE '%"
								+ endOrderNo + "%' "
								: ""));
			} else {
				hql
						.append((startOrderNo != null
								&& !"".equals(startOrderNo) ? "and model.orderNo >= '"
								+ startOrderNo + "' "
								: ""));
				hql
						.append((endOrderNo != null && !"".equals(endOrderNo) ? "and model.orderNo <= '"
								+ endOrderNo + "' "
								: ""));
			}

			// ----更新日期
			hql
					.append((startDate != null && !"".equals(startDate) ? "and model.lastUpdateDate between "
							+ "TO_DATE('"
							+ startDate
							+ "', 'YYYY/MM/DD') and "
							+ "TO_DATE('" + endDate + "', 'YYYY/MM/DD') "
							: ""));
			// 員工代號
			hql
					.append((employeeCode != null && !("".equals(employeeCode)) ? "and model.lastUpdatedBy = '"
							+ employeeCode + "' "
							: ""));
			// 狀態
			hql
					.append((status != null && !"".equals(status) ? "and model.status = '"
							+ status + "' "
							: ""));

			// ----類別組合字串
			hql
					.append((categorySearchString != null
							&& !"".equals(categorySearchString.trim()) ? categorySearchString
							: ""));

			System.out.println(hql.toString());

			return imPromotionDAO.getHibernateTemplate().find(hql.toString());

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ImPromotion findPromotionByOrderTypeNo(String brandCode,
			String orderType, String orderNo) {
		try {

			StringBuffer hql = new StringBuffer("from ImPromotion as model ");
			hql.append("where model.brandCode = '" + brandCode + "' ");
			hql.append("and model.orderTypeCode = '" + orderType + "' ");
			hql.append("and model.orderNo = '" + orderNo + "' ");

			System.out.println(hql.toString());

			List<ImPromotion> lists = imPromotionDAO.getHibernateTemplate()
					.find(hql.toString());
			if (lists.size() > 0) {
				return lists.get(0);
			} else {
				return null;
			}

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ImPromotion findById(Long id){
	    return imPromotionDAO.findById(id);
	}
	/**
	 * 依據品牌代號、活動代號查詢，取得活動資料
	 * 
	 * @param brandCode
	 * @param promotionCode
	 * @return ImPromotion
	 */
	public ImPromotion findByBrandCodeAndPromotionCode(String brandCode, String promotionCode){
	    if(promotionCode != null){
	        promotionCode = promotionCode.trim().toUpperCase();
	    }
	    return imPromotionDAO.findByBrandCodeAndPromotionCode(brandCode, promotionCode);
	}
	
	/**
	 * 是否商品項目低於折扣限制
	 * @param imPromotion 
	 * @param percentage  70% 使用70  50%使用50傳入
	 * @return 低於傳入之percentage，則回傳true
	 */
	public boolean isOverLimitation(ImPromotion imPromotion, Double percentage ) {
		List<ImPromotionItem>objItems = imPromotion.getImPromotionItems();
		boolean result = false;
		for (ImPromotionItem item : objItems) {
			if(percentage >= item.getTotalDiscountPercentage()){
				result=true;
			}
		}
		return result;
	}
	
	
        /**
         * 依據品牌代號、活動代號查詢活動資訊(AJAX)
         * 
         * @param httpRequest
         * @return List<Properties>
         * @throws Exception
         */
        public List<Properties> findByBrandCodeAndPromotionCodeForAJAX(Properties httpRequest) 
	    throws Exception{
	    
	    List<Properties> result = new ArrayList();
	    Properties properties = new Properties();
	    try{
		String brandCode = httpRequest.getProperty("brandCode");
	        String promotionCode = httpRequest.getProperty("promotionCode");
	        properties.setProperty("PromotionCode", promotionCode);
		properties.setProperty("PromotionName", "查無資料");
	        ImPromotion promotion = imPromotionDAO.findByBrandCodeAndPromotionCode(brandCode, promotionCode);
	        if(promotion != null){
	            properties.setProperty("PromotionName", AjaxUtils.getPropertiesValue(promotion.getPromotionName(), ""));
	        }
	        result.add(properties);
		
	        return result;		
	    }catch (Exception ex) {
                log.error("依據品牌代號、活動代號查詢促銷活動資料時發生錯誤，原因：" + ex.toString());
		throw new Exception("查詢促銷活動資料失敗！");
            }
	}
        
        public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
            
            try{
                List<Properties> result = new ArrayList();
     	        List<Properties> gridDatas = new ArrayList();
     	        Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
     	        int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
     	        int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
     	        //======================帶入Head的值=========================
     	        String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
     	        String formStatus = httpRequest.getProperty("formStatus");// 狀態
     	        String priceType = httpRequest.getProperty("priceType");// 價格類別
     	        HashMap map = new HashMap();
     	        map.put("headId", headId);
     	        map.put("startPage", iSPage);
     	        map.put("pageSize", iPSize);
	        map.put("brandCode", brandCode);
	        map.put("formStatus", formStatus);
	        map.put("priceType", priceType);
     	        //======================更新品號的價格資訊=========================
     	        if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)){
     	            imPromotionItemService.savePartialItemData(map);
     	        }
     	        //======================取得頁面所需資料===========================
     	        List<ImPromotionItem> imPromotionItems = imPromotionItemDAO.findPageLine(headId, iSPage, iPSize);
     	        if(imPromotionItems != null && imPromotionItems.size() > 0){
     	            // 取得第一筆的INDEX
    		    Long firstIndex = imPromotionItems.get(0).getIndexNo();
    		    // 取得最後一筆 INDEX
    		    Long maxIndex = imPromotionItemDAO.findPageLineMaxIndex(headId);
    		    refreshItemData(map, imPromotionItems);
    		    result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, imPromotionItems, gridDatas,
			    firstIndex, maxIndex));	
     	        }else{
     	            result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));
     	        }
     	        
     	        return result;
            }catch(Exception ex){
	        log.error("載入頁面顯示的促銷商品明細發生錯誤，原因：" + ex.toString());
	        throw new Exception("載入頁面顯示的促銷商品明細失敗！");
	    }
        }
        
        /**
         * 更新促銷商品明細資料
         * 
         * @param parameterMap
         * @param promotionItems
         */
        private void refreshItemData(HashMap parameterMap, List<ImPromotionItem> promotionItems){
            
            for(ImPromotionItem promotionItem : promotionItems){
        	getPromotionItemRelationData(parameterMap, promotionItem, false);
            }
        }
        
        private void getPromotionItemRelationData(HashMap parameterMap, ImPromotionItem promotionItem, boolean isReplace){
            
            String brandCode = (String)parameterMap.get("brandCode");
            String priceType = (String)parameterMap.get("priceType");
            String itemName = "查無此商品資料";
            String itemCode = promotionItem.getItemCode();
    	    //品名
    	    if(StringUtils.hasText(itemCode)){
                ImItemCurrentPriceView imItemCurrentPriceView = 
		    imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, itemCode, priceType);
		if(imItemCurrentPriceView != null){
		    promotionItem.setItemName(imItemCurrentPriceView.getItemCName());
		    if(isReplace){
		        promotionItem.setStandardPurchaseCost(imItemCurrentPriceView.getStandardPurchaseCost());
		        promotionItem.setOriginalPrice(imItemCurrentPriceView.getUnitPrice());
		    }
                }else{
            	    promotionItem.setItemName(itemName);
                }
    	    }else{
    	        promotionItem.setItemName("");
    	    }
        }
        
        /**
         * 取得暫時單號存檔
         * 
         * @param promotion
         * @throws Exception
         */
        public void saveTmp(ImPromotion promotion) throws Exception{
    	
            try{
    	        String tmpOrderNo = AjaxUtils.getTmpOrderNo();
    	        promotion.setOrderNo(tmpOrderNo);
    	        promotion.setLastUpdateDate(new Date());
    	        promotion.setCreationDate(new Date());
    	        imPromotionDAO.save(promotion);	    
    	    }catch(Exception ex){	   
    	        log.error("取得暫時單號儲存促銷單發生錯誤，原因：" + ex.toString());
    	        throw new Exception("取得暫時單號儲存促銷單發生錯誤，原因：" + ex.getMessage());
    	    }	
        }
        
        /**
         * 處理AJAX參數(促銷單item line變動時計算)
         * 
         * @param httpRequest
         * @return List<Properties>
         * @throws ValidationErrorException
         */
        public List<Properties> getAJAXItemData(Properties httpRequest) throws ValidationErrorException{
            
            List<Properties> result = new ArrayList();
    	    Properties properties = new Properties();
    	    String itemIndexNo = null;
    	    try{
    		itemIndexNo = httpRequest.getProperty("itemIndexNo");
    	        String brandCode = httpRequest.getProperty("brandCode");
    	        String priceType = httpRequest.getProperty("priceType");// 價格類型
    	        String itemCode = httpRequest.getProperty("itemCode");
    	        String discountAmountTemp = httpRequest.getProperty("discountAmount");//
    	        String discountPercentageTemp = httpRequest.getProperty("discountPercentage");
    	        String quantityTemp = httpRequest.getProperty("quantity");
    	        Double discountAmount = null;
    	        Double discountPercentage = null;
    	        Double quantity = null;
    	        if(StringUtils.hasText(itemCode)){
    	            itemCode = itemCode.trim().toUpperCase();
    	        }
    	        if(StringUtils.hasText(discountAmountTemp)){
    	            discountAmount = NumberUtils.getDouble(discountAmountTemp);
    	            discountAmount = CommonUtils.round(discountAmount, 0);
	        }
    	        if(StringUtils.hasText(discountPercentageTemp)){
    	            discountPercentage = NumberUtils.getDouble(discountPercentageTemp);
    	            discountPercentage = CommonUtils.round(discountPercentage, 1);
	        }
    	        if(StringUtils.hasText(quantityTemp)){
    	            quantity = NumberUtils.getDouble(quantityTemp);
	        }  
    	        
    	        HashMap map = new HashMap();
	        map.put("brandCode", brandCode);
	        map.put("priceType", priceType);
	        ImPromotionItem promotionItem = new ImPromotionItem();
	        promotionItem.setItemCode(itemCode);
	        promotionItem.setDiscountAmount(discountAmount);
	        promotionItem.setDiscountPercentage(discountPercentage);
	        promotionItem.setQuantity(quantity);
	        //取得item的相關資料
	        getPromotionItemRelationData(map, promotionItem, true);
	        if(promotionItem.getOriginalPrice() != null && promotionItem.getOriginalPrice() > 0D){
	            if (promotionItem.getDiscountAmount() != null && promotionItem.getDiscountAmount() != 0D) {
	        	promotionItem.setDiscountPercentage(null);
	                promotionItem.setTotalDiscountAmount(promotionItem.getDiscountAmount());
	                promotionItem.setTotalDiscountPercentage(
	                	CommonUtils.round((promotionItem.getDiscountAmount() / promotionItem.getOriginalPrice() * 100D), 1));
		    } else if(promotionItem.getDiscountPercentage() != null){
			promotionItem.setDiscountAmount(null);
			promotionItem.setTotalDiscountAmount(
				CommonUtils.round((promotionItem.getOriginalPrice() * (promotionItem.getDiscountPercentage() / 100D)), 0));
			promotionItem.setTotalDiscountPercentage(promotionItem.getDiscountPercentage());
		    }
	        }else{
	            promotionItem.setDiscountAmount(null);
		    promotionItem.setDiscountPercentage(null);
	        }
	        	        
	        properties.setProperty("ItemCode", AjaxUtils.getPropertiesValue(promotionItem.getItemCode(), ""));
		properties.setProperty("ItemName", AjaxUtils.getPropertiesValue(promotionItem.getItemName(), ""));
		properties.setProperty("StandardPurchaseCost", AjaxUtils.getPropertiesValue(promotionItem.getStandardPurchaseCost(), ""));
		properties.setProperty("OriginalPrice", AjaxUtils.getPropertiesValue(promotionItem.getOriginalPrice(), ""));
		properties.setProperty("DiscountAmount", AjaxUtils.getPropertiesValue(promotionItem.getDiscountAmount(), ""));
		properties.setProperty("DiscountPercentage", AjaxUtils.getPropertiesValue(promotionItem.getDiscountPercentage(), ""));
		properties.setProperty("TotalDiscountAmount", AjaxUtils.getPropertiesValue(promotionItem.getTotalDiscountAmount(), ""));
		properties.setProperty("TotalDiscountPercentage", AjaxUtils.getPropertiesValue(promotionItem.getTotalDiscountPercentage(), ""));
		properties.setProperty("Quantity", AjaxUtils.getPropertiesValue(promotionItem.getQuantity(), ""));
    	        
	        result.add(properties);		    
		return result;
    	    }catch (Exception ex) {
	        log.error("更新商品資料頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
	        throw new ValidationErrorException("更新商品資料頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
	    }
        }
        
        /**
         * 更新PAGE的LINE
         * 
         * @param httpRequest
         * @return List<Properties>
         * @throws Exception
         */
        public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{
    	
            try{
                String status = httpRequest.getProperty("status");
    	        String errorMsg = null;	    
    	        if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) {
    		    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
    		    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
    		    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
    		    Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
    		    if(headId == null){
    		        throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
    		    }
    		    
                    ImPromotion promotion = new ImPromotion();
                    promotion.setHeadId(headId);
    		    // 將STRING資料轉成List Properties record data
    		    List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);		
    		    // Get INDEX NO
    		    int indexNo = imPromotionItemDAO.findPageLineMaxIndex(headId).intValue();
    		    if (upRecords != null) {
    		        for (Properties upRecord : upRecords) {
    		            // 先載入HEAD_ID OR LINE DATA
    			    Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
    			    String itemCode = upRecord.getProperty(GRID_FIELD_NAMES[1]);
    			    if (StringUtils.hasText(itemCode)) {			  
    				ImPromotionItem promotionItemPO = imPromotionItemDAO.findItemByIdentification(headId, lineId);
    			        if(promotionItemPO != null){	
    			            AjaxUtils.setPojoProperties(promotionItemPO, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    			            imPromotionItemDAO.update(promotionItemPO);
    			        }else{
    				    indexNo++;
    				    ImPromotionItem promotionItem = new ImPromotionItem();
    				    AjaxUtils.setPojoProperties(promotionItem, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
    				    promotionItem.setIndexNo(Long.valueOf(indexNo));
    				    promotionItem.setImPromotion(promotion);
    				    imPromotionItemDAO.save(promotionItem);
    			        }			
    			    }
    		        }
    		    }		
    	        }
    	    
    	        return AjaxUtils.getResponseMsg(errorMsg);
            }catch(Exception ex){
                log.error("更新促銷商品明細時發生錯誤，原因：" + ex.toString());
    	        throw new Exception("更新促銷商品明細失敗！"); 
            }	
        }
        
        /**
         * 依據促銷單查詢螢幕的輸入條件進行查詢
         * 
         * @param conditionMap
         * @return List
         * @throws Exception
         */
        public List<ImPromotion> findPromotionList(HashMap conditionMap)
    	    throws Exception {

    	    try {
    	        String startOrderNo = (String) conditionMap.get("startOrderNo");
    	        String endOrderNo = (String) conditionMap.get("endOrderNo");
    	        String employeeCode = (String) conditionMap.get("employeeCode");
    	    
    	        conditionMap.put("startOrderNo", startOrderNo.trim().toUpperCase());
    	        conditionMap.put("endOrderNo", endOrderNo.trim().toUpperCase());
    	        conditionMap.put("employeeCode", employeeCode.trim().toUpperCase());

    	        return imPromotionDAO.findPromotionList(conditionMap);
    	    } catch (Exception ex) {
    	        log.error("查詢促銷單時發生錯誤，原因：" + ex.toString());
    	        throw new Exception("查詢促銷單時發生錯誤，原因：" + ex.getMessage());
    	    }
        }
        
        /**
         * 匯入促銷商品明細
         * 
         * @param headId
         * @param promotionItems
         * @throws Exception
         */
        public void executeImportPromotionItems(Long headId, List promotionItems) throws Exception{
            
            try{
        	ImPromotion promotionPO = findById(headId);
        	if(promotionPO == null){
    	            throw new NoSuchObjectException("查無促銷貨單主鍵：" + headId + "的資料");
    	        }       	
        	if(promotionItems != null && promotionItems.size() > 0){
        	    promotionPO.setImPromotionItems(promotionItems);
        	}else{
        	    promotionPO.setImPromotionItems(new ArrayList(0));
        	}
        	
        	imPromotionDAO.update(promotionPO);      	
            }catch (Exception ex) {
    	        log.error("促銷商品明細匯入時發生錯誤，原因：" + ex.toString());
    	        throw new Exception("促銷商品明細匯入時發生錯誤，原因：" + ex.getMessage());
    	    }        
        }
        
        public Map executeInitial(Map parameterMap) throws Exception{
            
            HashMap resultMap = new HashMap();
            try{
        	Object otherBean = parameterMap.get("vatBeanOther");
        	String formIdString = (String)PropertyUtils.getProperty(otherBean, "formId");
		Long formId =  StringUtils.hasText(formIdString)? Long.valueOf(formIdString):null;
		String isAllShop = "N";
		String beginDate = "";
		String endDate = "";
		if(formId != null){
		    ImPromotion promotion = findById(formId);
		    if(promotion != null){			
		        isAllShop = StringUtils.hasText(promotion.getIsAllShop())?promotion.getIsAllShop():"N";
		        beginDate = promotion.getBeginDate() != null?DateUtils.shortDateConverter(DateUtils.format(promotion.getBeginDate(), "yyyy/MM/dd")):"";	
		        endDate = promotion.getEndDate() != null?DateUtils.shortDateConverter(DateUtils.format(promotion.getEndDate(), "yyyy/MM/dd")):"";		
		    }else{
			throw new NoSuchObjectException("依據促銷單主鍵：" + formId + "查無相關資料，請聯絡系統管理人員處理！");
		    }
		}
		resultMap.put("isAllShop", isAllShop);
		resultMap.put("beginDate", beginDate);
		resultMap.put("endDate", endDate);
		
		return resultMap;       	
            }catch (Exception ex) {
    	        log.error("促銷單初始化失敗，原因：" + ex.toString());
    	        throw new Exception("促銷單初始化失敗，原因：" + ex.toString());
    	    }           
        }
        
        private void getRequiredParameter(Object obj, Map parameterMap) throws ValidationErrorException, Exception{
            
            String brandCode = (String)PropertyUtils.getProperty(obj, "brandCode");
    	    if(!StringUtils.hasText(brandCode)){
    	        throw new ValidationErrorException("傳入的品牌參數為空值！");
    	    }
    	    
    	    BuBranch branchPO = buBrandService.findBranchByBrandCode(brandCode);
    	    if(branchPO == null){
    		throw new ValidationErrorException("查無" + brandCode + "所屬的事業體！");
    	    }
    	    parameterMap.put("branch", branchPO.getBranchCode());
        }
        
        private void getFormBean(Object obj, Map parameterMap)throws ValidationErrorException, Exception{
            
            String formId = (String)PropertyUtils.getProperty(obj, "formId");
            String brandCode = (String)PropertyUtils.getProperty(obj, "brandCode");
            String orderTypeCode = (String)PropertyUtils.getProperty(obj, "orderTypeCode");
            String employeeCode = (String)PropertyUtils.getProperty(obj, "employeeCode");
            if(StringUtils.hasText(formId)){
        	Long headId = NumberUtils.getLong(formId);
        	ImPromotion promotionPO = findById(headId);
        	
            }else{
        	ImPromotion promotion = new ImPromotion();
        	promotion.setBrandCode(brandCode);
        	promotion.setOrderTypeCode(orderTypeCode);
        	promotion.setPriceType("1");
        	promotion.setStatus(OrderStatus.SAVE);
        	promotion.setInCharge(employeeCode);
        	promotion.setIsAllCustomer("N");
        	promotion.setCreatedBy(employeeCode);
        	promotion.setLastUpdatedBy(employeeCode);
            }                    
        }
        
        /**
         * 更新促銷單主檔及明細檔
         * 
         * @param parameterMap
         * @return Map
         * @throws Exception
         */
        public Map updateAJAXPromotion(Map parameterMap) throws FormException, Exception {
            
            HashMap resultMap = new HashMap();
            try{
        	Object formBindBean = parameterMap.get("vatBeanFormBind");
        	Object formLinkBean = parameterMap.get("vatBeanFormLink");
        	Object otherBean = parameterMap.get("vatBeanOther");
        	Long headId = getPromotionHeadId(formLinkBean);      	
        	String formStatus = (String)PropertyUtils.getProperty(otherBean, "formStatus");
        	String beforeChangeStatus = (String)PropertyUtils.getProperty(otherBean, "beforeChangeStatus");
        	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");      	
        	
        	//取得欲更新的bean
        	ImPromotion promotionPO = getActualPromotion(headId);
        	if(OrderStatus.SAVE.equals(formStatus)){
        	    imPromotionItemService.setParameterToBean(otherBean, promotionPO);
            	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);       	    
        	}else if((OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT.equals(beforeChangeStatus)) 
        		&& OrderStatus.SIGNING.equals(formStatus)){
        	    //========================檢核資料======================================   	    
        	    String identification = MessageStatus.getIdentification(promotionPO.getBrandCode(), 
        		    promotionPO.getOrderTypeCode(), promotionPO.getOrderNo());
        	    imPromotionItemService.setParameterToBean(otherBean, promotionPO);
            	    AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);
            	    promotionPO.setLastUpdatedBy(employeeCode);
        	    siProgramLogAction.deleteProgramLog(PROGRAM_ID, null, identification);     	    
        	    imPromotionItemService.checkPromotionData(promotionPO, PROGRAM_ID, identification);
        	    //============remove delete mark record=============
        	    removeDeleteMarkLineForItem(promotionPO);
        	    removeDeleteMarkLineForShop(promotionPO);
        	}
        	promotionPO.setStatus(formStatus);        	
        	String resultMsg = modifyImPromotion(promotionPO, employeeCode);
        	resultMap.put("entityBean", promotionPO);
        	resultMap.put("resultMsg", resultMsg);
        	
        	return resultMap;
        	//=============================================              
            } catch (FormException fe) {
	        log.error("促銷單存檔失敗，原因：" + fe.toString());
	        throw new FormException(fe.getMessage());
	    } catch (Exception ex) {
	        log.error("促銷單存檔時發生錯誤，原因：" + ex.toString());
	        throw new Exception("促銷單存檔時發生錯誤，原因：" + ex.getMessage());
	    }
        }
        
        public ImPromotion getActualPromotion(Long headId) throws FormException, Exception{
            
            ImPromotion promotionPO = findById(headId);
    	    if(promotionPO  == null){
    	        throw new NoSuchObjectException("查無促銷單主鍵：" + headId + "的資料！");
    	    }
            
    	    return promotionPO;
        }
        
        /**
        * 暫存單號取實際單號並更新至Promotion主檔及明細檔
        * 
        * @param promotion
        * @param loginUser
        * @return String
        * @throws ObtainSerialNoFailedException
        * @throws FormException
        * @throws Exception
        */
       private String modifyImPromotion(ImPromotion promotion, String loginUser)
   	    throws ObtainSerialNoFailedException, FormException, Exception {
   	
           if (AjaxUtils.isTmpOrderNo(promotion.getOrderNo())) {
   	       String serialNo = buOrderTypeService.getOrderSerialNo(
   	           promotion.getBrandCode(), promotion.getOrderTypeCode());
   	       if (!serialNo.equals("unknow")) {
   	           promotion.setOrderNo(serialNo);
   	       } else {
   		   throw new ObtainSerialNoFailedException("取得"
   		       + promotion.getOrderTypeCode() + "單號失敗！");
   	       }
           }
           promotion.setLastUpdatedBy(loginUser);
           promotion.setLastUpdateDate(new Date());
           imPromotionDAO.update(promotion);
           return promotion.getOrderTypeCode() + "-" + promotion.getOrderNo() + "存檔成功！";
       }
       
       public List<Properties> getAJAXPageDataForShop(Properties httpRequest) throws Exception{
           
           try{
               List<Properties> result = new ArrayList();
    	       List<Properties> gridDatas = new ArrayList();
    	       Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
    	       int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    	       int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
    	       //======================帶入Head的值=========================
    	       String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
    	       String formStatus = httpRequest.getProperty("formStatus");// 狀態
    	       HashMap map = new HashMap();
	       map.put("brandCode", brandCode);
	       map.put("formStatus", formStatus);
    	       //======================取得頁面所需資料===========================
    	       List<ImPromotionShop> imPromotionShops = imPromotionShopDAO.findPageLine(headId, iSPage, iPSize);
    	       if(imPromotionShops != null && imPromotionShops.size() > 0){
    	           // 取得第一筆的INDEX
   		   Long firstIndex = imPromotionShops.get(0).getIndexNo();
   		   // 取得最後一筆 INDEX
   		   Long maxIndex = imPromotionShopDAO.findPageLineMaxIndex(headId);
   		   refreshShopData(map, imPromotionShops);
   		   result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES_SHOP, GRID_FIELD_DEFAULT_VALUES_SHOP, imPromotionShops, gridDatas,
	               firstIndex, maxIndex));	
    	       }else{
    	           result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES_SHOP, GRID_FIELD_DEFAULT_VALUES_SHOP, gridDatas));
    	       }
    	        
    	       return result;
           }catch(Exception ex){
	       log.error("載入頁面顯示的促銷專櫃明細發生錯誤，原因：" + ex.toString());
	       throw new Exception("載入頁面顯示的促銷專櫃明細失敗！");
	   }
       }
       
       /**
        * 更新促銷專櫃明細資料
        * 
        * @param parameterMap
        * @param promotionItems
        */
       private void refreshShopData(HashMap parameterMap, List<ImPromotionShop> promotionShops){
	   
           for(ImPromotionShop promotionShop : promotionShops){
               getShopRelationData(parameterMap, promotionShop);  
           }
       }
       
       private void getShopRelationData(HashMap parameterMap, ImPromotionShop promotionShop){
	   
	   String brandCode = (String)parameterMap.get("brandCode");
	   String formStatus = (String)parameterMap.get("formStatus");
	   String shopName = "查無此專櫃資料";
           
           if(StringUtils.hasText(promotionShop.getShopCode())){
               BuShop buShop = null;
               if(OrderStatus.SAVE.equals(formStatus) || OrderStatus.REJECT.equals(formStatus)){
        	   buShop = buShopDAO.findEnableShopById(brandCode, promotionShop.getShopCode()); 
               }else{
                   buShop = buShopDAO.findShopByBrandCodeAndShopCode(brandCode, promotionShop.getShopCode());
               }
               if(buShop != null){
                   promotionShop.setShopName(buShop.getShopCName());
               }else{
        	   promotionShop.setShopName(shopName);
               }       	   
           }else{
               promotionShop.setShopName("");
           }	   
       }
       
       /**
        * 更新PAGE的SHOP LINE
        * 
        * @param httpRequest
        * @return List<Properties>
        * @throws Exception
        */
       public List<Properties> updateAJAXShopLinesData(Properties httpRequest) throws Exception{
   	
           try{
               String status = httpRequest.getProperty("status");
   	       String errorMsg = null;	    
   	       if (OrderStatus.SAVE.equals(status) || OrderStatus.REJECT.equals(status)) {
   		   String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
   		   int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
   		   int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
   		   Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));
   		   if(headId == null){
   		       throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
   		   }
   		    
                   ImPromotion promotion = new ImPromotion();
                   promotion.setHeadId(headId);
   		   // 將STRING資料轉成List Properties record data
   		   List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES_SHOP);		
   		   // Get INDEX NO
   		   int indexNo = imPromotionShopDAO.findPageLineMaxIndex(headId).intValue();
   		   if (upRecords != null) {
   		       for (Properties upRecord : upRecords) {
   		           // 先載入HEAD_ID OR LINE DATA
   			   Long lineId = NumberUtils.getLong(upRecord.getProperty("lineId"));
   			   String shopCode = upRecord.getProperty(GRID_FIELD_NAMES_SHOP[1]);
   			   //System.out.println("UShopCode=" + shopCode);
   			   if (StringUtils.hasText(shopCode)) {			  
   			       ImPromotionShop promotionShopPO = imPromotionShopDAO.findShopByIdentification(headId, lineId);
   			       if(promotionShopPO != null){	
   			           AjaxUtils.setPojoProperties(promotionShopPO, upRecord, GRID_FIELD_NAMES_SHOP, GRID_FIELD_TYPES_SHOP);
   			           imPromotionShopDAO.update(promotionShopPO);
   			       }else{
   				   System.out.println("go here");
   				   indexNo++;
   				   ImPromotionShop promotionShop = new ImPromotionShop();
   				   AjaxUtils.setPojoProperties(promotionShop, upRecord, GRID_FIELD_NAMES_SHOP, GRID_FIELD_TYPES_SHOP);
   				   promotionShop.setIndexNo(Long.valueOf(indexNo));
   				   promotionShop.setImPromotion(promotion);
   				   imPromotionShopDAO.save(promotionShop);
   			       }			
   			   }
   		       }
   		   }		
   	       }
   	    
   	       return AjaxUtils.getResponseMsg(errorMsg);
           }catch(Exception ex){
               System.out.println(ex.toString());
               log.error("更新促銷專櫃明細時發生錯誤，原因：" + ex.toString());
   	       throw new Exception("更新促銷專櫃明細失敗！"); 
           }	
       }
       
       /**
        * 處理AJAX參數(促銷單shop line變動時計算)
        * 
        * @param httpRequest
        * @return List<Properties>
        * @throws ValidationErrorException
        */
       public List<Properties> getAJAXShopData(Properties httpRequest) throws ValidationErrorException{
           
           List<Properties> result = new ArrayList();
   	    Properties properties = new Properties();
   	    String itemIndexNo = null;
   	    try{
   		itemIndexNo = httpRequest.getProperty("itemIndexNo");
   	        String brandCode = httpRequest.getProperty("brandCode");
   	        String shopCode = httpRequest.getProperty("shopCode");
   	        String formStatus = httpRequest.getProperty("formStatus");// 狀態
   	        if(StringUtils.hasText(shopCode)){
   	            shopCode = shopCode.trim().toUpperCase();
   	        } 
   	        
   	        HashMap map = new HashMap();
	        map.put("brandCode", brandCode);
	        map.put("formStatus", formStatus);
	        ImPromotionShop promotionShop = new ImPromotionShop();
	        promotionShop.setShopCode(shopCode);
	        //取得shop的相關資料
	        getShopRelationData(map, promotionShop);       	        
	        properties.setProperty("ShopCode", AjaxUtils.getPropertiesValue(promotionShop.getShopCode(), ""));
		properties.setProperty("ShopName", AjaxUtils.getPropertiesValue(promotionShop.getShopName(), ""));
		System.out.println("ShopCode=" + promotionShop.getShopCode());
		System.out.println("ShopName=" + promotionShop.getShopName());
   	        
	        result.add(properties);		    
		return result;
   	    }catch (Exception ex) {
	        log.error("更新參與專櫃頁籤中第 " + itemIndexNo + "項明細的資料發生錯誤，原因：" + ex.getMessage());
	        throw new ValidationErrorException("更新參與專櫃頁籤中第 " + itemIndexNo + "項明細的資料失敗！");
	    }
       }
       
       /**
        * 合計所有Item的成本總金額、定價總金額、折扣後總金額、折扣總金額、平均折扣率
        * 
        * @param httpRequest
        * @return List<Properties>
        * @throws ValidationErrorException
        */
       public List<Properties> performCountTotalAmount(Properties httpRequest) throws ValidationErrorException {

           try{
   	       List<Properties> result = new ArrayList();
   	       Properties properties = new Properties();
   	       //===================取得傳遞的的參數===================
   	       Long headId = NumberUtils.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
   	       ImPromotion promotionPO = findById(headId);    
   	       if(promotionPO == null){
                   throw new ValidationErrorException("查無促銷單主鍵：" + headId + "的資料！");
   	       }
   	       //============計算成本總金額、定價總金額、折扣後總金額、折扣總金額、平均折扣率==========
   	       Double totalCostAmount = 0D;
   	       Double totalPriceAmount = 0D;
   	       Double totalDiscountAmount = 0D;
   	       Double totalAfterDiscountAmount = 0D;
   	       Double averageDiscountRate = 0D;;
   	       List<ImPromotionItem> promotionItems = promotionPO.getImPromotionItems();	    
   	       if(promotionItems != null && promotionItems.size() > 0){
   	           for(ImPromotionItem promotionItem : promotionItems){
   	               if(!"1".equals(promotionItem.getIsDeleteRecord())){
   	                   Double quantity = (promotionItem.getQuantity() == null || promotionItem.getQuantity() == 0D)?1D:promotionItem.getQuantity();  	            
   	                   Double discountAmount = (promotionItem.getTotalDiscountAmount() == null)?0D:promotionItem.getTotalDiscountAmount();
   	                   Double originalPrice = (promotionItem.getOriginalPrice() == null)?0D:promotionItem.getOriginalPrice();
                           Double standardPurchaseCost = (promotionItem.getStandardPurchaseCost() == null)?0D:promotionItem.getStandardPurchaseCost();	            
   	           	 
   	                   totalCostAmount += standardPurchaseCost * quantity;
   	                   totalPriceAmount += originalPrice * quantity;
   	                   totalDiscountAmount += discountAmount * quantity;
   	               }
   		   }
               }
   	       //=============================================================
   	       totalAfterDiscountAmount = totalPriceAmount - totalDiscountAmount;
   	       if(totalPriceAmount != 0D){
   	           averageDiscountRate = OperationUtils.round((totalDiscountAmount / totalPriceAmount) * 100D, 4).doubleValue();
   	       }
   	    	    
   	       properties.setProperty("TotalCostAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalCostAmount, 0), "0.0"));
   	       properties.setProperty("TotalPriceAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalPriceAmount, 0), "0.0"));
   	       properties.setProperty("TotalDiscountAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalDiscountAmount, 0), "0.0"));
   	       properties.setProperty("TotalAfterDiscountAmount", AjaxUtils.getPropertiesValue(NumberUtils.roundToStr(totalAfterDiscountAmount, 0), "0.0"));
   	       properties.setProperty("AverageDiscountRate", AjaxUtils.getPropertiesValue(averageDiscountRate, "0.0"));    
   	       result.add(properties);
   	    
   	       return result;
   	   }catch (Exception ex) {
   	       log.error("促銷單金額統計失敗，原因：" + ex.toString());
   	       throw new ValidationErrorException("促銷單金額統計失敗！");
   	   } 
       }
       
       /**
        * 檢核促銷活動相關資料
        * 
        * @param imPromotion
        * @throws ValidationErrorException
        * @throws NoSuchObjectException
        */
       private void checkPromotionData(ImPromotion imPromotion) throws ValidationErrorException, NoSuchObjectException {
		
	   if(!StringUtils.hasText(imPromotion.getPromotionCode())){
	       throw new ValidationErrorException("請輸入活動代號！");
	   }else if(!StringUtils.hasText(imPromotion.getPromotionName())){
	       throw new ValidationErrorException("請輸入活動名稱！");
	   }
	   
	   if(StringUtils.hasText(imPromotion.getInCharge())){
	       BuEmployeeWithAddressView employeeWithAddressView = 
		   buEmployeeWithAddressViewDAO.findbyBrandCodeAndEmployeeCode(imPromotion.getBrandCode(), imPromotion.getInCharge());
	       if(employeeWithAddressView == null){
	           throw new ValidationErrorException("查無" + imPromotion.getInCharge() + "的員工資料！");
	       }	       
	   }
	   
           if("Y".equals(imPromotion.getIsAllItem())){
	       if(imPromotion.getDiscount() == null || CommonUtils.round(imPromotion.getDiscount(), 2) == 0L)
	           throw new ValidationErrorException("商品資料頁籤中選擇所有商品，但未輸入折扣比率");			
	   }else{
	       checkPromotionItem(imPromotion);
	   }
		
	   if("Y".equals(imPromotion.getIsAllShop())){			
	       if(imPromotion.getBeginDate() == null)
	           throw new ValidationErrorException("參與專櫃頁籤中選擇所有專櫃，但未輸入起始日期");
	       else if(imPromotion.getEndDate() == null)
		   throw new ValidationErrorException("參與專櫃頁籤中選擇所有專櫃，但未輸入結束日期");
	       else if(imPromotion.getBeginDate().after(imPromotion.getEndDate())){
    	           throw new ValidationErrorException("參與專櫃頁籤中選擇所有專櫃，但起始日期不可大於結束日期！");
    	       }
	   }else{
	       checkPromotionShop(imPromotion);
	   }
	   	
	   if(!("Y".equals(imPromotion.getIsAllCustomer()))) {
	       checkPromotionCustomer(imPromotion);
	   }	
       }
       
       /**
        * check items
        * 
        * @param imPromotion
        * @throws ValidationErrorException
        * @throws NoSuchObjectException
        */
       private void checkPromotionItem(ImPromotion imPromotion) throws ValidationErrorException, NoSuchObjectException {
	   
           String tabName = "商品資料頁籤";
	   String brandCode = imPromotion.getBrandCode();
	   String priceType = imPromotion.getPriceType();
	   List promotionItems = imPromotion.getImPromotionItems();
	   if(promotionItems != null && promotionItems.size() > 0){
	       HashMap itemMap = new HashMap();
	       int intactRecordCount = 0;
	       for(int i = 0; i < promotionItems.size(); i++){
		   ImPromotionItem promotionItem = (ImPromotionItem)promotionItems.get(i);
		   if(!"1".equals(promotionItem.getIsDeleteRecord())){
		       String itemCode = promotionItem.getItemCode();
		       if(!StringUtils.hasText(itemCode)){
			   throw new ValidationErrorException("請輸入" + tabName + "中第" + (i + 1) + "項明細的品號！");
		       }
		       itemCode = itemCode.trim().toUpperCase();
		       promotionItem.setItemCode(itemCode);
                       if(imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, itemCode, priceType) != null){
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
    		           
    		           System.out.println("[discountAmount]=" + discountAmount);
    		           System.out.println("[discountPercentage]=" + discountPercentage);
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
			       }else if(discountAmount > originalPrice){
				   throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
			               ")其促銷金額不可大於定價！");
			       }else{
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
			       }else if(discountPercentage > 100D){
				   throw new ValidationErrorException(tabName + "中第" + (i + 1) + "項明細的品號(" + itemCode +
			               ")其折扣數不可大於100%！");
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
	       }
	       if(intactRecordCount == 0){
	           throw new ValidationErrorException(tabName + "中至少需輸入一筆完整的資料！"); 
	       }
	   }else{
	       throw new ValidationErrorException(tabName + "中至少需輸入一筆資料！"); 
	   }         
       }
    
       /**
        * check shops
        * 
        * @param imPromotion
        * @throws ValidationErrorException
        * @throws NoSuchObjectException
        */
       private void checkPromotionShop(ImPromotion imPromotion) throws ValidationErrorException, NoSuchObjectException {
	   
           String tabName = "參與專櫃頁籤";
	   String brandCode = imPromotion.getBrandCode();
	   List promotionShops = imPromotion.getImPromotionShops();
	   if(promotionShops != null && promotionShops.size() > 0){
	       HashMap shopMap = new HashMap();
	       int intactRecordCount = 0;
	       for(int i = 0; i < promotionShops.size(); i++){
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
	       }
	       if(intactRecordCount == 0){
	           throw new ValidationErrorException(tabName + "中至少需輸入一筆完整的資料！"); 
	       }
	   }else{
	       throw new ValidationErrorException(tabName + "中至少需輸入一筆資料！ "); 
	   }	   
       }
       
       /**
        * check customers
        * 
        * @param imPromotion
        * @throws ValidationErrorException
        */
       private void checkPromotionCustomer(ImPromotion imPromotion) throws ValidationErrorException {
		
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
                   throw new ValidationErrorException(tabName + "中至少需勾選一筆客戶類別！");
	       }
	   }
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
       
       public static Object[] startProcess(ImPromotion form) throws ProcessFailedException{       
	        
           try{           
	       String packageId = "Im_Promotion";         
	       String processId = "approval";           
	       String version = "20090624";
	       String sourceReferenceType = "ImPromotion (1)";
	       HashMap context = new HashMap();
	       context.put("brandCode", form.getBrandCode());
	       context.put("formId", form.getHeadId());
	       context.put("orderType", form.getOrderTypeCode());
	       context.put("orderNo", form.getOrderNo());
	       return ProcessHandling.start(packageId, processId, version, sourceReferenceType, context);
	   }catch (Exception ex){
	       log.error("促銷流程啟動失敗，原因：" + ex.toString());
	       throw new ProcessFailedException("促銷流程啟動失敗！");
	   }	      
       }
       
       private void setParameterToBean(Object bean, ImPromotion promotion) throws ValidationErrorException, Exception{
	   
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
   	   System.out.println("vipCheckFalgArray=" + vipCheckFalgArray.length);
   	   System.out.println("vipTypeCodeArray=" + vipTypeCodeArray.length);
   	   List<ImPromotionCustomer> promotionCustomers = promotion.getImPromotionCustomers();
   	   HashMap customerMap = new HashMap();
   	   if(promotionCustomers != null && promotionCustomers.size() > 0){
   	       for(ImPromotionCustomer promotionCustomerPO : promotionCustomers){
   	           Long lineId = promotionCustomerPO.getLineId();
   	           String vipTyepCode = promotionCustomerPO.getVipTypeCode();
   	           if(!StringUtils.hasText(vipTyepCode)){
   	               throw new ValidationErrorException("客戶類別明細主鍵：" + lineId + "的客戶類別代號為空值！");
   	           }else if(customerMap.get(vipTyepCode) != null){
   	               throw new ValidationErrorException("客戶類別明細主鍵：" + lineId + "的客戶類別代號與其他明細重複！");
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
       
       public static Object[] completeAssignment(long assignmentId, boolean approveResult) throws ProcessFailedException{
	   
	   try{           
	       HashMap context = new HashMap();
	       context.put("approveResult", approveResult);
	       
	       return ProcessHandling.completeAssignment(assignmentId, context);
	   }catch (Exception ex){
	       log.error("完成促銷工作任務失敗，原因：" + ex.toString());
	       throw new ProcessFailedException("完成促銷工作任務失敗！");
	   }
       }
       
       /**
        * 更新促銷單主檔的Status
        * 
        * @param headId
        * @param status
        * @throws Exception
        */
       public void updatePromotionStatus(Long headId, String status) throws Exception {
       
           try {
   	       ImPromotion promotion = findById(headId);
   	       if(promotion != null){
   	           promotion.setStatus(status);	
   		   imPromotionDAO.update(promotion);
   	       }else{
   	           throw new NoSuchDataException("促銷單主檔查無主鍵：" + headId
   			+ "的資料！");
   	       }
           } catch (Exception ex) {
   	       log.error("更新促銷單狀態時發生錯誤，原因：" + ex.toString());
   	       throw new Exception("更新促銷單狀態時發生錯誤，原因：" + ex.getMessage());
   	   }
       }
       
       public String getIdentification(Long headId) throws Exception{
	   
	   String id = null;
	   try{
	       ImPromotion promotion = findById(headId);
	       if(promotion != null){
		   id = MessageStatus.getIdentification(promotion.getBrandCode(), 
   	                   promotion.getOrderTypeCode(), promotion.getOrderNo());
   	       }else{
   	           throw new NoSuchDataException("促銷單主檔查無主鍵：" + headId
   			+ "的資料！");
   	       }
	       return id;
	   }catch(Exception ex){
	       log.error("查詢識別碼時發生錯誤，原因：" + ex.toString());
   	       throw new Exception("查詢識別碼時發生錯誤，原因：" + ex.getMessage());	       
	   }	   
       }
       
       /**
        * 取單號後更新促銷單主檔
        * 
        * @param parameterMap
        * @return Map
        * @throws Exception
        */
       public Map updatePromotionWithActualOrderNO(Map parameterMap) throws FormException, Exception {
           
           HashMap resultMap = new HashMap();
           try{
               Object formBindBean = parameterMap.get("vatBeanFormBind");
       	       Object formLinkBean = parameterMap.get("vatBeanFormLink");
       	       Object otherBean = parameterMap.get("vatBeanOther");
       	       Long headId = getPromotionHeadId(formLinkBean); 
       	       String employeeCode = (String)PropertyUtils.getProperty(otherBean, "employeeCode");
       	       //取得欲更新的bean
       	       ImPromotion promotionPO = getActualPromotion(headId);
       	       imPromotionItemService.setParameterToBean(otherBean, promotionPO);
	       AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);
       	       String resultMsg = modifyImPromotion(promotionPO, employeeCode);
       	       resultMap.put("entityBean", promotionPO);
               resultMap.put("resultMsg", resultMsg);
       	
       	       return resultMap;
       	       //=============================================              
           } catch (FormException fe) {
	       log.error("促銷單存檔失敗，原因：" + fe.toString());
	       throw new FormException(fe.getMessage());
	   } catch (Exception ex) {
	       log.error("促銷單存檔時發生錯誤，原因：" + ex.toString());
	       throw new Exception("促銷單存檔時發生錯誤，原因：" + ex.getMessage());
	   }
       }
       
       public Long getPromotionHeadId(Object bean) throws FormException, Exception{
	   
	   Long headId = null;
	   String id = (String)PropertyUtils.getProperty(bean, "headId");
           System.out.println("headId=" + id);
   	   if(StringUtils.hasText(id)){
               headId = NumberUtils.getLong(id);
           }else{
       	       throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
           }
   	   
   	   return headId;
       }
}