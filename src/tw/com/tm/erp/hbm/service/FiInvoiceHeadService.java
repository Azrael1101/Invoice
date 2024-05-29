package tw.com.tm.erp.hbm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.BuExchangeRate;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.dao.FiInvoiceHeadDAO;
import tw.com.tm.erp.hbm.dao.ImReceiveInvoiceDAO;

/**
 * Invoice Head Service
 * 
 * @author MyEclipse Persistence Tools
 */
public class FiInvoiceHeadService {
	private static final Log log = LogFactory.getLog(FiInvoiceHeadService.class);
	private FiInvoiceHeadDAO fiInvoiceHeadDAO;
	private BuBasicDataService buBasicDataService;
	private FiInvoiceLineService fiInvoiceLineService;
	private BuOrderTypeService buOrderTypeService;
	private BuCommonPhraseService buCommonPhraseService;
	private PoPurchaseOrderHeadService poPurchaseOrderHeadService;
	private ImReceiveInvoiceDAO imReceiveInvoiceDAO;

	/**
	 * save and update
	 * 
	 * @param modifyObj
	 * @return
	 * @throws Exception
	 */
	public String create(FiInvoiceHead modifyObj) throws Exception {
		if (null != modifyObj) {
			setAllDetailByPurchaseOrderNo(modifyObj);
			if (modifyObj.getHeadId() == null) {
				modifyObj.setOrderTypeCode(FiInvoiceHead.FI_INVOICE_ORDER_TYPE);
				return save(modifyObj);
			} else {
				return update(modifyObj);
			}
		}
		return MessageStatus.ERROR + " Can't find FiInvoiceHead";
	}

	/**
	 * save
	 * 
	 * @param saveObj
	 * @return
	 * @throws Exception
	 */
	public String save(FiInvoiceHead saveObj) throws Exception {
		log.info("FiInvoiceHeadService save start " + saveObj);
		doAllValidate(saveObj);
		//saveObj.setLotNo(buOrderTypeService.getOrderSerialNo(saveObj.getBrandCode(), saveObj.getOrderTypeCode()));
		saveObj.setCreatedBy(saveObj.getLastUpdatedBy());
		saveObj.setLastUpdateDate(new Date());
		saveObj.setCreationDate(new Date());
		fiInvoiceHeadDAO.save(saveObj);
		return MessageStatus.SUCCESS;

	}

	/**
	 * update
	 * 
	 * @param updateObj
	 * @return
	 * @throws Exception
	 */
	public String update(FiInvoiceHead updateObj) throws Exception {
		if (!isUsedInvoice(updateObj.getBrandCode(), updateObj.getInvoiceNo())) {
			doAllValidate(updateObj);
			updateObj.setLastUpdateDate(new Date());
			fiInvoiceHeadDAO.update(updateObj);
		} else {
			throw new FormException("發票已被使用請勿修改");
		}
		return MessageStatus.SUCCESS;

	}

	/**
	 * 利用供應商代號設定 countryCode,currencyCode,exchangeRate
	 * 
	 * @param headObj
	 */
	public void setFormDataBySupplierCode(String organizationCode, FiInvoiceHead headObj) {
		BuSupplierWithAddressView buSWAV = buBasicDataService.findEnableSupplierById(headObj.getBrandCode(), headObj.getSupplierCode());
		if (null != buSWAV) {
			headObj.setCurrencyCode(buSWAV.getCurrencyCode());
			setExchangeRateByCurrencyCode(organizationCode, headObj);
		}
	}

	/**
	 * 利用幣別指定匯率
	 * 
	 * @param headObj
	 */
	public void setExchangeRateByCurrencyCode(String organizationCode, FiInvoiceHead headObj) {
		headObj.setExchangeRate(new Double(1));
		if ((null != organizationCode) && (null != headObj.getCurrencyCode())) {
			BuExchangeRate bxr = buBasicDataService.getLastExchangeRate(organizationCode, headObj.getCurrencyCode(), buCommonPhraseService
					.getBuCommonPhraseLineName("SystemConfig", "LocalCurrency"));
			if (null != bxr) {
				headObj.setExchangeRate(bxr.getExchangeRate());
			}
		}
	}

	/**
	 * 設定Detail的內容
	 * 
	 * @param headObj
	 */
	public void setAllDetail(FiInvoiceHead headObj) {
		log.info("FiInvoiceHeadService.setAllDetail");
		for (FiInvoiceLine detail : headObj.getFiInvoiceLines()) {
			log.info("FiInvoiceLine head Id " + detail.getPoPurchaseOrderHeadId());
			fiInvoiceLineService.setLinePurchaseOrderData(detail);
		}
	}

	/**
	 * 設定Detail的內容
	 * 
	 * @param headObj
	 * @throws FormException
	 */
	public void setAllDetailByPurchaseOrderNo(FiInvoiceHead headObj) throws FormException {
		for (FiInvoiceLine detail : headObj.getFiInvoiceLines()) {
			fiInvoiceLineService.setLinePurchaseOrderDataByPurchaseOrderNo(headObj, detail);
		}
	}

	/**
	 * 移除空白的Detail
	 * 
	 * @param headObj
	 * @return boolean 是否還有 Detail
	 */
	private String checkDetailItemCode(FiInvoiceHead headObj) {
		log.info("FiInvoiceHeadService.removeNullDetail");
		List<FiInvoiceLine> items = headObj.getFiInvoiceLines();
		Iterator<FiInvoiceLine> it = items.iterator();
		while (it.hasNext()) {
			FiInvoiceLine line = it.next();
			if (!StringUtils.hasText(line.getPurchaseOrderNo())) {
				return MessageStatus.ERROR_NO_DETAIL;
			} else if (null == poPurchaseOrderHeadService.findById(line.getPoPurchaseOrderHeadId())) {
				return "請再確認採購單號:" + line.getPurchaseOrderNo();
			}
		}
		return null;
	}

	/**
	 * do master validate
	 * 
	 * @param headObj
	 * @throws FormException,Exception
	 */
	public void doValidate(FiInvoiceHead headObj) throws FormException, Exception {
		log.info("FiInvoiceHeadService doValidate " + headObj);
		// 判斷是否重覆
		if (null == headObj.getHeadId() || headObj.getHeadId() == 0) {
			List heads = fiInvoiceHeadDAO.findByProperty("FiInvoiceHead", "invoiceNo", headObj.getInvoiceNo());
			if ((null != heads) && (heads.size() > 0))
				throw new FormException("Invoice No 重覆");
		}
	}

	/**
	 * 檢核
	 * 
	 * @param headObj
	 * @return
	 * @throws Exception
	 */
	private void doAllValidate(FiInvoiceHead headObj) throws FormException, Exception {
		log.info("FiInvoiceHeadService doAllValidate " + headObj);
		/*
		 * String errorMsg = checkDetailItemCode(headObj); if ( null !=
		 * errorMsg) { throw new FormException(errorMsg); }
		 */

		// 等到確認才開啟
		doValidate(headObj);

		// 明細確認
		List<FiInvoiceLine> items = headObj.getFiInvoiceLines();
		for (FiInvoiceLine item : items) {
			fiInvoiceLineService.doValidate(item);
		}

	}

	/**
	 * 判斷發票是否已經被ImReceive使用掉
	 * 
	 * @param invoiceNo
	 * @return true : 已被使用 , false : 未被使用
	 */
	public boolean isUsedInvoice(String brandCode, String invoiceNo) {
		log.info("FiInvoiceHeadService isUsedInvoice " + invoiceNo);
		return imReceiveInvoiceDAO.isUsedInvoice(brandCode, invoiceNo, null);
	}

	/**
	 * find by pk
	 * 
	 * @param headId
	 * @return
	 */
	public FiInvoiceHead findById(Long headId) {
		return (FiInvoiceHead) fiInvoiceHeadDAO.findByPrimaryKey(FiInvoiceHead.class, headId);
	}

	/**
	 * search
	 * 
	 * @param findObjs
	 * @return
	 */
	public List<FiInvoiceHead> find(HashMap findObjs) {
		return fiInvoiceHeadDAO.find(findObjs);
	}

	public void setFiInvoiceHeadDAO(FiInvoiceHeadDAO fiInvoiceHeadDAO) {
		this.fiInvoiceHeadDAO = fiInvoiceHeadDAO;
	}

	public void setBuBasicDataService(BuBasicDataService buBasicDataService) {
		this.buBasicDataService = buBasicDataService;
	}

	/*
	 * public void setBuSupplierWithAddressViewDAO(BuSupplierWithAddressViewDAO
	 * buSupplierWithAddressViewDAO) { this.buSupplierWithAddressViewDAO =
	 * buSupplierWithAddressViewDAO; }
	 */
	public void setFiInvoiceLineService(FiInvoiceLineService fiInvoiceLineService) {
		this.fiInvoiceLineService = fiInvoiceLineService;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setBuCommonPhraseService(BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setPoPurchaseOrderHeadService(PoPurchaseOrderHeadService poPurchaseOrderHeadService) {
		this.poPurchaseOrderHeadService = poPurchaseOrderHeadService;
	}

	public void setImReceiveInvoiceDAO(ImReceiveInvoiceDAO imReceiveInvoiceDAO) {
		this.imReceiveInvoiceDAO = imReceiveInvoiceDAO;
	}

}