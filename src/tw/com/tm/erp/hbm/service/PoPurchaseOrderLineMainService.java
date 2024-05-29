package tw.com.tm.erp.hbm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderLineDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;

/**
 * 採購單明細 SERVICE
 * 
 * @author T02049
 * 
 */
public class PoPurchaseOrderLineMainService {
	private static final Log log = LogFactory.getLog(PoPurchaseOrderLineMainService.class);
	private PoPurchaseOrderLineDAO poPurchaseOrderLineDAO;
	private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO;
	private BuOrderTypeDAO buOrderTypeDAO;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private ImReceiveHeadDAO imReceiveHeadDAO;
	private ImItemDAO imItemDAO;
	private NativeQueryDAO nativeQueryDAO;
	private BuOrderTypeService buOrderTypeService;

	/**
	 * save or update
	 * 
	 * @param modifyObj
	 * @return
	 */
	public String create(PoPurchaseOrderLine modifyObj) {
		log.info("PoPurchaseOrderLineMainService.doValidate");
		if (null != modifyObj) {
			if (modifyObj.getLineId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		}
		return "ImWarehouse is null ..";
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 */
	public String save(PoPurchaseOrderLine saveObj) {
		log.info("PoPurchaseOrderLineMainService.save");
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		poPurchaseOrderLineDAO.save(saveObj);
		return "success";
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 */
	public String update(PoPurchaseOrderLine updateObj) {
		log.info("PoPurchaseOrderLineMainService.update");
		updateObj.setLastUpdateDate(new Date());
		poPurchaseOrderLineDAO.update(updateObj);
		return "success";
	}

	/**
	 * find by id
	 * 
	 * @param headId
	 * @return
	 */
	public PoPurchaseOrderLine findById(Long headId) {
		log.info("PoPurchaseOrderLineMainService.PoPurchaseOrderLine");
		PoPurchaseOrderLine re = (PoPurchaseOrderLine) poPurchaseOrderLineDAO.findByPrimaryKey(PoPurchaseOrderLine.class, headId);
		if (null == re)
			re = new PoPurchaseOrderLine();
		return re;
	}

	/**
	 * 指定上次進貨價格 先抓入庫單的STATUS是FINISH,抓日期最近的 DECLARATION_ITEM_CODE ->
	 * LOCAL_UNIT_PRICE, 如果沒有 才抓產品ITEM
	 * 
	 * @param itemCode
	 *            品號
	 * @return
	 */
	public void setLastUnitCost(PoPurchaseOrderLine lineObj) {
		if (null != lineObj) {
			log.info("PoPurchaseOrderLineMainService.setLastUnitCost lineObj.getLastForeignUnitCost()=" + lineObj.getLastForeignUnitCost());
			if (lineObj.getLastForeignUnitCost().equals(0D)) { // 已經帶過的資料不會再重覆帶入
				Double foreignUnitPrice = new Double(0);
				if (null != lineObj && StringUtils.hasText(lineObj.getItemCode())) {
					foreignUnitPrice = imReceiveHeadDAO.getLastForeignUnitPrice(lineObj.getItemCode());
					if (null == foreignUnitPrice || foreignUnitPrice.equals(0D)) {
						ImItem imItem = imItemDAO.findById(lineObj.getItemCode());
						if (null != imItem) {
							foreignUnitPrice = imItem.getSupplierQuotationPrice();
						}
					}
				}

				lineObj.setLastForeignUnitCost(foreignUnitPrice);

				// 指定單價
				if (lineObj.getForeignUnitCost().equals(0D))
					lineObj.setForeignUnitCost(foreignUnitPrice);
			}
		}
	}

	/**
	 * 指定商品原本售價 由Order_Type.Price_Type = IM ITEM CURRENT PRICE VIEW.Type_Code
	 * 拿最後一筆 MAX , ITEM CODE and BRAND CODE
	 * 
	 * @param lineObj
	 */
	public void setOriginalUnitPrice(PoPurchaseOrderHead headObj, PoPurchaseOrderLine lineObj) {
		log.info("PoPurchaseOrderLineMainService.setOriginalUnitPrice");
		if ((null != headObj) && (null != lineObj)) {
			if (lineObj.getUnitPrice().equals(0D)) { // 已經帶過的資料不會再重覆帶入
				BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
				buOrderTypeId.setBrandCode(headObj.getBrandCode());
				buOrderTypeId.setOrderTypeCode(headObj.getOrderTypeCode());
				BuOrderType buOrderType = buOrderTypeDAO.findById(buOrderTypeId);
				if (null != buOrderType) {
					String priceType = buOrderType.getPriceType();
					if (StringUtils.hasText(lineObj.getItemCode()) && StringUtils.hasText(headObj.getBrandCode())
							&& StringUtils.hasText(priceType)) {
						ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findCurrentPriceByValue(headObj
								.getBrandCode(), lineObj.getItemCode(), priceType);
						if (null != imItemCurrentPriceView) {
							lineObj.setUnitPrice(new Double(imItemCurrentPriceView.getUnitPrice()));
						}
					}
				}
			}
		}
	}

	/**
	 * 取得商品的原始售價
	 * 
	 * @param brandCode
	 * @param orderTypeCode
	 * @param itemCode
	 * @return
	 * @throws FormException 
	 */
	public Double getOriginalUnitPriceDouble(String brandCode, String orderTypeCode, String itemCode) throws FormException {
		log.info("PoPurchaseOrderLineMainService.getOriginalUnitPriceDouble BrandCode=" + brandCode + ",OrderTypeCode=" + orderTypeCode);
		Double re = 0D ;
		BuOrderTypeId buOrderTypeId = new BuOrderTypeId();
		buOrderTypeId.setBrandCode(brandCode);
		buOrderTypeId.setOrderTypeCode(orderTypeCode);
		BuOrderType buOrderType = buOrderTypeDAO.findById(buOrderTypeId);
		if (null != buOrderType && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(itemCode)) {
			String priceType = buOrderType.getPriceType();
			log.info("priceType=" + buOrderType.getPriceType());
			if (null != priceType) {
				log.info("BrandCode=" + brandCode + ",ItemCode=" + itemCode + ",priceType=" + priceType);
				ImItemCurrentPriceView imItemCurrentPriceView = imItemCurrentPriceViewDAO.findCurrentPrice(brandCode, itemCode, priceType);
				if (null != imItemCurrentPriceView) {
					re = imItemCurrentPriceView.getUnitPrice().doubleValue() ;
				}
			}
		}else{
			throw new FormException("取得商品的原始售價資料有誤請確認  品牌 :" + brandCode + ",單別 :" + orderTypeCode + ",品號:" + itemCode ); 
		}
		return re;		
	}
	
	public Double getOriginalUnitPrice(String brandCode, String orderTypeCode, String itemCode) throws FormException {
		log.info("PoPurchaseOrderLineMainService.getOriginalUnitPrice BrandCode=" + brandCode + ",OrderTypeCode=" + orderTypeCode);
		return getOriginalUnitPriceDouble(brandCode,orderTypeCode,itemCode);
	}

	/**
	 * 取得最後平均成本
	 * 
	 * @param itemCode
	 *            品號
	 * @return
	 */
	public Double getAverageUnitCost(String itemCode, String brandCode) {
		log.info("PoPurchaseOrderLineMainService.getAverageUnitCost itemCode=" + itemCode);
		BigDecimal averageUnitCost = new BigDecimal(0);

		try {
			if (StringUtils.hasText(itemCode)) {
				String nativeSql = "SELECT H.ITEM_CODE, H.YEAR, H.MONTH, H.AVERAGE_UNIT_COST FROM IM_MONTHLY_BALANCE_HEAD H , (SELECT BRAND_CODE, ITEM_CODE, MAX (YEAR||MONTH) YM FROM IM_MONTHLY_BALANCE_HEAD WHERE BRAND_CODE ='"
						+ brandCode
						+ "' AND ITEM_CODE='"
						+ itemCode
						+ "' GROUP BY BRAND_CODE, ITEM_CODE) MAX_DAY WHERE 1=1 AND H.BRAND_CODE = MAX_DAY.BRAND_CODE AND H.ITEM_CODE=MAX_DAY.ITEM_CODE AND (H.YEAR|| H.MONTH) = YM";
				List reList = nativeQueryDAO.executeNativeSql(nativeSql);
				if (null != reList && reList.size() > 0) {
					Object[] result = (Object[]) reList.get(0);
					if (result.length >= 4)
						averageUnitCost = (BigDecimal) result[3];
				}

				// 多加一個0D的判斷
				if (null == averageUnitCost || averageUnitCost.doubleValue() == 0) {
					ImItem imItem = imItemDAO.findById(itemCode);
					if (null != imItem && null != imItem.getStandardPurchaseCost())
						averageUnitCost = new BigDecimal(imItem.getStandardPurchaseCost());
					else{
						averageUnitCost = new BigDecimal(0);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return NumberUtils.getDouble(averageUnitCost.doubleValue());
	}

	/**
	 * 取得最後進貨單價
	 * 
	 * @param itemCode
	 *            品號
	 * @return
	 */
	public Double getLastUnitCost(String itemCode) {
		log.info("PoPurchaseOrderLineMainService.setLastUnitCost itemCode=" + itemCode);
		Double foreignUnitPrice = new Double(0);
		if (StringUtils.hasText(itemCode)) {
			foreignUnitPrice = imReceiveHeadDAO.getLastForeignUnitPrice(itemCode);
			if (null == foreignUnitPrice || foreignUnitPrice.equals(0D)) {
				ImItem imItem = imItemDAO.findById(itemCode);
				if (null != imItem && null != imItem.getSupplierQuotationPrice()) {
					foreignUnitPrice = imItem.getSupplierQuotationPrice();
				}
			}
		}
		return foreignUnitPrice.doubleValue();
	}

	/**
	 * do validate
	 * 
	 * @param item
	 * @throws FormException
	 */
	public void doValidate(PoPurchaseOrderLine item) throws FormException {
		log.info("PoPurchaseOrderLineMainService.doValidate");
		// item.getItemCode() 產品編號檢核
		List imItems = imItemDAO.findByProperty("orderNo", item.getItemCode());
		if (imItems.size() <= 0) {
			throw new FormException("品號有問題 : " + item.getItemCode());
		}
	}

	/**
	 * LINE 頁面顯示
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public List<PoPurchaseOrderLine> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("PoPurchaseOrderLineMainService.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		return poPurchaseOrderLineDAO.findPageLine(headId, startPage, pageSize);
	}

	/**
	 * find line
	 * 
	 * @param headId
	 * @param lineId
	 * @return
	 */
	public PoPurchaseOrderLine findLine(String headId, String lineId) {
		log.info("PoPurchaseOrderLineMainService.setLastUnitCost headId=" + headId + ",lineId=" + lineId);
		if (StringUtils.hasText(headId) && StringUtils.hasText(lineId)) {
			Long hId = 0L;
			Long lId = 0L;
			try {
				hId = Long.valueOf(headId);
				lId = Long.valueOf(lineId);
			} catch (Exception ex) {

			}
			return poPurchaseOrderLineDAO.findLine(hId, lId);
		}
		return null;
	}
	
	/**
	 * find line
	 * 
	 * @param headId
	 * @param lineId
	 * @return
	 */
	public List <PoPurchaseOrderLine> findByItemCode(Long headId, String itemCode) {
		log.info("PoPurchaseOrderLineMainService.setLastUnitCost headId=" + headId + ",itemCode=" + itemCode);
		return poPurchaseOrderLineDAO.findByItemCode( headId, itemCode);
	}
	
	/**
	 * find line
	 * 
	 * @param headId
	 * @param lineId
	 * @return
	 */
	public List <PoPurchaseOrderLine> findByItemCode(Long headId, String itemCode , String lineId) {
		log.info("PoPurchaseOrderLineMainService.setLastUnitCost headId=" + headId + ",itemCode=" + itemCode + ",lineId=" + lineId);
		return poPurchaseOrderLineDAO.findByItemCode( headId, itemCode, lineId);//採購欄位回寫增poLineId-Jerome
	}
	
	
	public void deleteLineLists(Long headId) throws FormException, Exception{
		
		try{
    	    PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead)poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class, headId);
    	    if( poPurchaseOrderHead == null){
    	        throw new NoSuchObjectException("查無採購單主鍵：" + headId + "的資料");
    	    }
    	    poPurchaseOrderHead.setPoPurchaseOrderLines(new ArrayList(0));
    	    poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);
		} catch (FormException fe) {
		    log.error("刪除採購單明細失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("刪除採購單明細時發生錯誤，原因：" + ex.toString());
		    throw new Exception("刪除採購單明細時發生錯誤，原因：" + ex.getMessage());
		} 	
	 }
	    /**
	     * 暫存單號取實際單號並更新至PoPurchase主檔
	     * 
	     * @param headId
	     * @param loginUser
	     * @return PoPurchaseOrderHead
	     * @throws ObtainSerialNoFailedException
	     * @throws FormException
	     * @throws Exception
	     */
	    public PoPurchaseOrderHead saveActualOrderNo(Long headId, String loginUser)
		    throws ObtainSerialNoFailedException, FormException, Exception {
		
		PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead) poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class,headId);
		if(poPurchaseOrderHead == null){
		    throw new NoSuchObjectException("查無採購單主鍵：" + headId + "的資料！");
		}else if (AjaxUtils.isTmpOrderNo(poPurchaseOrderHead.getOrderNo())) {
		    String serialNo = buOrderTypeService.getOrderSerialNo(
			    poPurchaseOrderHead.getBrandCode(), poPurchaseOrderHead.getOrderTypeCode());
		    if (!serialNo.equals("unknow")) {
			poPurchaseOrderHead.setOrderNo(serialNo);
		    } else {
			throw new ObtainSerialNoFailedException("取得"
			    + poPurchaseOrderHead.getOrderTypeCode() + "單號失敗！");
		    }
	        }
		poPurchaseOrderHead.setLastUpdatedBy(loginUser);
		poPurchaseOrderHead.setLastUpdateDate(new Date());
	        poPurchaseOrderHeadDAO.update(poPurchaseOrderHead);

	        return poPurchaseOrderHead;
	    }
	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public PoPurchaseOrderLine findLine(Long headId, Long lineId) {
		log.info("PoPurchaseOrderLineMainService.findLine headId=" + headId + ",lineId=" + lineId);
		return poPurchaseOrderLineDAO.findLine(headId, lineId);
	}

	/**
	 * 取得GRID最後一筆
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("PoPurchaseOrderHeadDAO.findPageLineMaxIndex");
		return poPurchaseOrderLineDAO.findPageLineMaxIndex(headId);
	}

	public void setPoPurchaseOrderLineDAO(PoPurchaseOrderLineDAO poPurchaseOrderLineDAO) {
		this.poPurchaseOrderLineDAO = poPurchaseOrderLineDAO;
	}

	public void setImItemCurrentPriceViewDAO(ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO) {
		this.imItemCurrentPriceViewDAO = imItemCurrentPriceViewDAO;
	}

	public void setImReceiveHeadDAO(ImReceiveHeadDAO imReceiveHeadDAO) {
		this.imReceiveHeadDAO = imReceiveHeadDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setBuOrderTypeDAO(BuOrderTypeDAO buOrderTypeDAO) {
		this.buOrderTypeDAO = buOrderTypeDAO;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}

	public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
	    this.buOrderTypeService = buOrderTypeService;
	}
	
}
