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
import tw.com.tm.erp.hbm.bean.ImPriceAdjustment;
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.bean.ImAdjustmentLine;
import tw.com.tm.erp.hbm.dao.BuOrderTypeDAO;
import tw.com.tm.erp.hbm.dao.ImItemCurrentPriceViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentLineDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;

/**
 * 商品調整明細 SERVICE
 * 
 * @author T61682
 * 
 */
public class ImAdjustmentLineService {
	private static final Log log = LogFactory.getLog(ImAdjustmentLineService.class);
	private ImAdjustmentLineDAO imAdjustmentLineDAO;
	private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
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
	public String create(ImAdjustmentLine modifyObj) {
		log.info("ImAdjustmentLineService.doValidate");
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
	public String save(ImAdjustmentLine saveObj) {
		log.info("ImAdjustmentLineService.save");
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imAdjustmentLineDAO.save(saveObj);
		return "success";
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 */
	public String update(ImAdjustmentLine updateObj) {
		log.info("ImAdjustmentLineService.update");
		updateObj.setLastUpdateDate(new Date());
		imAdjustmentLineDAO.update(updateObj);
		return "success";
	}

	/**
	 * find by id
	 * 
	 * @param headId
	 * @return
	 */
	public ImAdjustmentLine findById(Long headId) {
		log.info("ImAdjustmentLineService.ImAdjustmentLine");
		ImAdjustmentLine re = (ImAdjustmentLine) imAdjustmentLineDAO.findByPrimaryKey(ImAdjustmentLine.class, headId);
		if (null == re)
			re = new ImAdjustmentLine();
		return re;
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
		log.info("ImAdjustmentLineService.getOriginalUnitPriceDouble BrandCode=" + brandCode + ",OrderTypeCode=" + orderTypeCode);
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
		log.info("ImAdjustmentLineService.getOriginalUnitPrice BrandCode=" + brandCode + ",OrderTypeCode=" + orderTypeCode);
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
		log.info("ImAdjustmentLineService.getAverageUnitCost itemCode=" + itemCode);
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
		log.info("ImAdjustmentLineService.setLastUnitCost itemCode=" + itemCode);
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
	public void doValidate(ImAdjustmentLine item) throws FormException {
		log.info("ImAdjustmentLineService.doValidate");
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
	public List<ImAdjustmentLine> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("ImAdjustmentLineService.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		return imAdjustmentLineDAO.findPageLine(headId, startPage, pageSize);
	}

	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public ImAdjustmentLine findLine(String headId, String lineId) {
		log.info("ImAdjustmentLineService.setLastUnitCost headId=" + headId + ",lineId=" + lineId);
		if (StringUtils.hasText(headId) && StringUtils.hasText(lineId)) {
			Long hId = 0L;
			Long lId = 0L;
			try {
				hId = Long.valueOf(headId);
				lId = Long.valueOf(lineId);
			} catch (Exception ex) {

			}
			return imAdjustmentLineDAO.findLine(hId, lId);
		}
		return null;
	}
	public void deleteLineLists(Long headId) throws FormException, Exception{
		
		try{
    	    ImAdjustmentHead poPurchaseOrderHead = (ImAdjustmentHead)imAdjustmentHeadDAO.findByPrimaryKey(ImAdjustmentHead.class, headId);
    	    if( poPurchaseOrderHead == null){
    	        throw new NoSuchObjectException("查無商品調整單主鍵：" + headId + "的資料");
    	    }
    	    poPurchaseOrderHead.setImAdjustmentLines(new ArrayList(0));
    	    imAdjustmentHeadDAO.update(poPurchaseOrderHead);
		} catch (FormException fe) {
		    log.error("刪除商品調整明細失敗，原因：" + fe.toString());
		    throw new FormException(fe.getMessage());
		} catch (Exception ex) {
		    log.error("刪除商品調整明細時發生錯誤，原因：" + ex.toString());
		    throw new Exception("刪除商品調整明細時發生錯誤，原因：" + ex.getMessage());
		} 	
	 }
	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public ImAdjustmentLine findLine(Long headId, Long lineId) {
		log.info("ImAdjustmentLineService.findLine headId=" + headId + ",lineId=" + lineId);
		return imAdjustmentLineDAO.findLine(headId, lineId);
	}

	/**
	 * 取得GRID最後一筆
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("ImAdjustmentHeadDAO.findPageLineMaxIndex");
		return imAdjustmentLineDAO.findPageLineMaxIndex(headId);
	}

	public void setImAdjustmentLineDAO(ImAdjustmentLineDAO ImAdjustmentLineDAO) {
		this.imAdjustmentLineDAO = ImAdjustmentLineDAO;
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

	public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
		this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
	}
	
}
