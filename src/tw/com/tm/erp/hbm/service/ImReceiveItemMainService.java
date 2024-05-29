package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.bean.ImReceiveItem;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveInvoiceDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveItemDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;

/**
 * 進貨單 ITEM Service
 * 
 * @author MyEclipse Persistence Tools
 */
public class ImReceiveItemMainService {
	private static final Log log = LogFactory.getLog(ImReceiveItemMainService.class);
	private ImReceiveItemDAO imReceiveItemDAO;
	private ImItemDAO imItemDAO;
	private FiInvoiceHeadDAO fiInvoiceHeadDAO;
	private ImReceiveInvoiceDAO imReceiveInvoiceDAO;
	private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO;

	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 */
	public String create(ImReceiveItem modifyObj) {
		if (null != modifyObj) {
			if (modifyObj.getLineId() == null) {
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		}
		return "";
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 */
	public String save(ImReceiveItem saveObj) {
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		imReceiveItemDAO.save(saveObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 */
	public String update(ImReceiveItem updateObj) {
		updateObj.setLastUpdateDate(new Date());
		imReceiveItemDAO.update(updateObj);
		return MessageStatus.SUCCESS;
	}

	/**
	 * delete
	 * 
	 * @param updateObj
	 * @return
	 */
	public String delete(ImReceiveItem updateObj) {
		//updateObj.setLastUpdateDate(new Date());
		imReceiveItemDAO.delete(updateObj);
		return MessageStatus.SUCCESS;
	}
	
	
	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public ImReceiveItem findById(Long headId) {
		ImReceiveItem re = (ImReceiveItem) imReceiveItemDAO.findByPrimaryKey(ImReceiveItem.class, headId);
		if (null == re)
			re = new ImReceiveItem();
		return re;
	}

	/**
	 * do validate
	 * 
	 * @param item
	 * @throws FormException
	 * @throws Exception
	 */
	public void doValidate(ImReceiveHead head, ImReceiveItem item) throws FormException {
		log.info("doValidate");

		if (ImReceiveHead.IM_RECEIVE_LOCAL.equalsIgnoreCase(head.getOrderTypeCode())) {
		} else {

			if (OrderStatus.FORM_SAVE.equalsIgnoreCase(head.getWarehouseStatus())) {
				// item.getInvoiceNo() 發票編號資料為空檢核
				if (!StringUtils.hasText(item.getInvoiceNo())) {
					log.info("throw FormException 發票編號資料為空 ");
					throw new FormException("發票編號資料為空");
				}

				// item.getInvoiceNo() 發票編號檢核
				List fiInvoiceLines = fiInvoiceHeadDAO.findByProperty(FiInvoiceHead.class.getName(), "invoiceNo", item.getInvoiceNo());
				if (fiInvoiceLines.size() <= 0) {
					log.info("throw FormException 發票編號有問題 " + item.getInvoiceNo());
					throw new FormException("發票編號有問題 : " + item.getIndexNo());
				}

				// item.getInvoiceNo() 發票資料已被使用
				if (imReceiveInvoiceDAO.isUsedInvoice(head.getBrandCode(), item.getInvoiceNo(), head.getHeadId())) {
					log.info("throw FormException 發票資料已被使用 " + item.getInvoiceNo());
					throw new FormException("發票資料已被使用 : " + item.getInvoiceNo());
				}

				// 20080826 item.getInvoiceNo() 檢查 相對應的 FI -> PO 是否包含這個商品
				HashMap findPOObjs = new HashMap();
				findPOObjs.put("invoiceNo", item.getInvoiceNo());
				findPOObjs.put("itemCode", item.getItemCode());
				findPOObjs.put("brandCode", head.getBrandCode());
				List<PoPurchaseOrderHead> poPurchaseOrderHeads = poPurchaseOrderHeadDAO.getInvoiceItemCode(findPOObjs);
				if ((null == poPurchaseOrderHeads) || (poPurchaseOrderHeads.size() <= 0)) {
					log.info("throw FormException 發票編號查無對應的商品名細 " + item.getInvoiceNo());
					throw new FormException("發票編號查無對應的商品名細 : 發票" + item.getInvoiceNo() + ",商品" + item.getItemCode());
				} else {
					// 20081120 shan 判斷ITEM中是否有符合需求
					boolean checkOneItemReady = false;
					for (PoPurchaseOrderHead poPurchaseOrderHead : poPurchaseOrderHeads) {
						List<PoPurchaseOrderLine> poPurchaseOrderLines = poPurchaseOrderHead.getPoPurchaseOrderLines();
						for (PoPurchaseOrderLine poPurchaseOrderLine : poPurchaseOrderLines) {
							if (item.getItemCode().equalsIgnoreCase(poPurchaseOrderLine.getItemCode())) {
								if (poPurchaseOrderLine.getQuantity() - poPurchaseOrderLine.getReceiptedQuantity() > 0) {
									checkOneItemReady = true;
									break;
								}
							}
						}
					}
					if (!checkOneItemReady) {
						log.info("發票對應的所有商品已無核銷數量 : 發票" + item.getInvoiceNo() + ",商品" + item.getItemCode());
						throw new FormException("發票對應的所有商品已無核銷數量 : 發票" + item.getInvoiceNo() + ",商品" + item.getItemCode());
					}
				}
			}
		}

		// item.getItemCode() 產品編號檢核
		List imItems = imItemDAO.findByProperty("itemCode", item.getItemCode());
		if (imItems.size() <= 0) {
			log.info("throw FormException 品號有問題 " + item.getItemCode());
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
	public List<ImReceiveItem> findPageLine(Long headId, int startPage, int pageSize) {
		log.info("findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
		return imReceiveItemDAO.findPageLine(headId, startPage, pageSize);
	}

	/**
	 * find line
	 * 
	 * @param headId
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	public ImReceiveItem findLine(String headId, String lineId) {
		log.info("findLine headId=" + headId + ",lineId=" + lineId);
		if (StringUtils.hasText(headId) && StringUtils.hasText(lineId)) {
			Long hId = 0L;
			Long lId = 0L;
			try {
				hId = Long.valueOf(headId);
				lId = Long.valueOf(lineId);
			} catch (Exception ex) {

			}
			return imReceiveItemDAO.findLine(hId, lId);
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
	public ImReceiveItem findLine(Long headId, Long lineId) {
		log.info("findLine headId=" + headId + ",lineId=" + lineId);
		return imReceiveItemDAO.findLine(headId, lineId);
	}

	/**
	 * 取得GRID最後一筆
	 * 
	 * @param headId
	 * @return
	 */
	public Long findPageLineMaxIndex(Long headId) {
		log.info("findPageLineMaxIndex");
		return imReceiveItemDAO.findPageLineMaxIndex(headId);
	}

	public void setImReceiveItemDAO(ImReceiveItemDAO imReceiveItemDAO) {
		this.imReceiveItemDAO = imReceiveItemDAO;
	}

	public void setImItemDAO(ImItemDAO imItemDAO) {
		this.imItemDAO = imItemDAO;
	}

	public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
		this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
	}

	public void setImReceiveInvoiceDAO(ImReceiveInvoiceDAO imReceiveInvoiceDAO) {
		this.imReceiveInvoiceDAO = imReceiveInvoiceDAO;
	}

	public void setPoPurchaseOrderHeadDAO(PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}

}