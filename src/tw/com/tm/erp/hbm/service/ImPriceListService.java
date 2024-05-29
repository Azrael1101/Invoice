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
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemCurrentPriceView;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.ImPriceList;
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImPriceAdjustmentDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.ImPriceListDAO;

/**
 * 商品訂價變價單明細 SERVICE
 * 
 * @author T61682
 * 
 */
public class ImPriceListService {
	private static final Log log = LogFactory.getLog(ImPriceListService.class);
	private ImPriceListDAO imPriceListDAO;
	private ImPriceAdjustmentDAO imPriceAdjustmentDAO;
	private BuOrderTypeDAO buOrderTypeDAO;
	private ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO;
	private ImReceiveHeadDAO imReceiveHeadDAO;
	private ImItemDAO imItemDAO;
	private NativeQueryDAO nativeQueryDAO;

	/**
	 * save or update
	 * 
	 * @param modifyObj
	 * @return
	 */
	public String create(ImPriceList modifyObj) {
		log.info("ImPriceListService.doValidate");
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
	public String save(ImPriceList saveObj) {
		log.info("ImPriceListService.save");
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imPriceListDAO.save(saveObj);
		return "success";
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 */
	public String update(ImPriceList updateObj) {
		log.info("ImPriceListService.update");
		updateObj.setLastUpdateDate(new Date());
		imPriceListDAO.update(updateObj);
		return "success";
	}

	/**
	 * find by id
	 * 
	 * @param headId
	 * @return
	 */
	public ImPriceList findById(Long headId) {
		log.info("ImPriceListService.ImPriceList");
		ImPriceList re = (ImPriceList) imPriceListDAO.findById( headId);
		if (null == re)
			re = new ImPriceList();
		return re;
	}



	/**
	 * 指定商品原本售價 由Order_Type.Price_Type = IM ITEM CURRENT PRICE VIEW.Type_Code
	 * 拿最後一筆 MAX , ITEM CODE and BRAND CODE
	 * 
	 * @param lineObj
	 */
	public void setOriginalUnitPrice(PoPurchaseOrderHead headObj, ImPriceList lineObj) {
		log.info("ImPriceListService.setOriginalUnitPrice");
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
		log.info("ImPriceListService.getOriginalUnitPriceDouble BrandCode=" + brandCode + ",OrderTypeCode=" + orderTypeCode);
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
	
	public String getOriginalUnitPrice(String brandCode, String orderTypeCode, String itemCode) throws FormException {
		log.info("ImPriceListService.getOriginalUnitPrice BrandCode=" + brandCode + ",OrderTypeCode=" + orderTypeCode);
		return new Integer(getOriginalUnitPriceDouble(brandCode,orderTypeCode,itemCode).intValue()).toString();
	}

	/**
	 * 取得最後單價(進貨成本)
	 * 
	 * @param itemCode
	 *            品號
	 * @return
	 */
	public String getAverageUnitCost(String itemCode, String brandCode) {
		log.info("ImPriceListService.getAverageUnitCost itemCode=" + itemCode);
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
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return String.valueOf(averageUnitCost);
	}

	/**
	 * 取得最後單價
	 * 
	 * @param itemCode
	 *            品號
	 * @return
	 */
	public String getLastUnitCost(String itemCode) {
		log.info("ImPriceListService.setLastUnitCost itemCode=" + itemCode);
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
		return String.valueOf(foreignUnitPrice.doubleValue());
	}

	/**
	 * do validate
	 * 
	 * @param item
	 * @throws FormException
	 */
	public void doValidate(ImPriceList item) throws FormException {
		log.info("ImPriceListService.doValidate");
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
	public List<ImPriceList> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("ImPriceListService.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		return imPriceListDAO.findPageLine(headId, startPage, pageSize);
	}

	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public ImPriceList findLine(String headId, String lineId) {
		log.info("ImPriceListService.setLastUnitCost headId=" + headId + ",lineId=" + lineId);
		if (StringUtils.hasText(headId) && StringUtils.hasText(lineId)) {
			Long hId = 0L;
			Long lId = 0L;
			try {
				hId = Long.valueOf(headId);
				lId = Long.valueOf(lineId);
			} catch (Exception ex) {

			}
			return imPriceListDAO.findLine(hId, lId);
		}
		return null;
	}

	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public ImPriceList findLine(Long headId, Long lineId) {
		log.info("ImPriceListService.findLine headId=" + headId + ",lineId=" + lineId);
		return imPriceListDAO.findLine(headId, lineId);
	}
	
	
	
	public void deletePriceLists(Long headId) throws FormException, Exception{
		
		try{
	    	    ImPriceAdjustment imPriceAdjustment = imPriceAdjustmentDAO.findById(headId);
	    	    if(imPriceAdjustment == null){
	    	        throw new NoSuchObjectException("查無定價變價單主鍵：" + headId + "的資料");
	    	    }
	    	    imPriceAdjustment.setImPriceLists(new ArrayList(0));
	    	    imPriceAdjustmentDAO.update(imPriceAdjustment);	
	        } catch (FormException fe) {
		    log.error("刪除商品定價變價明細失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("刪除商品定價變價明細時發生錯誤，原因：" + ex.toString());
		    throw new Exception("刪除商品定價變價明細時發生錯誤，原因：" + ex.getMessage());
		} 	
	    }
	/**
	 * 取得GRID最後一筆
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("ImPriceAdjustmentDAO.findPageLineMaxIndex");
		return imPriceListDAO.findPageLineMaxIndex(headId);
	}

	public void setImPriceListDAO(ImPriceListDAO imPirceListDAO) {
		this.imPriceListDAO = imPirceListDAO;
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

	public void setImPriceAdjustmentDAO(ImPriceAdjustmentDAO imPriceAdjustmentDAO) {
		this.imPriceAdjustmentDAO = imPriceAdjustmentDAO;
	}

}
