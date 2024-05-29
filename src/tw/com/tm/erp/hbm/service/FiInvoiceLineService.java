package tw.com.tm.erp.hbm.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils ;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.bean.FiInvoiceHead;
import tw.com.tm.erp.hbm.bean.FiInvoiceLine;
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderHead;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;


/**
 * Invoice Line Service.
 * 
 * @author MyEclipse Persistence Tools
 */
public class FiInvoiceLineService {
	private static final Log log = LogFactory.getLog(FiInvoiceLineService.class);	
	private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO ;
	
	/**
	 * 依據PoPurchaseOrderHeadId 指定 fi invoice line order type and order no  
	 * @param fiInvoiceLine
	 */
	public void setLinePurchaseOrderData(FiInvoiceLine fiInvoiceLine){
		log.info("FiInvoiceLineService.setLinePurchaseOrderData");
		if( ( null != fiInvoiceLine) && (null != fiInvoiceLine.getPoPurchaseOrderHeadId()) ){
			log.info("PoPurchaseOrderHeadId " + fiInvoiceLine.getPoPurchaseOrderHeadId() );
			PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead)poPurchaseOrderHeadDAO.findByPrimaryKey(PoPurchaseOrderHead.class , fiInvoiceLine.getPoPurchaseOrderHeadId() ) ;
			fiInvoiceLine.setPurchaseOrderType(poPurchaseOrderHead.getOrderTypeCode());
			fiInvoiceLine.setPurchaseOrderNo(poPurchaseOrderHead.getOrderNo());
		}		
	}

	/**
	 * 依據PoPurchaseOrderNo 指定 fi invoice line order type and order head id
	 * @param fiInvoiceLine
	 * @throws FormException 
	 */
	public void setLinePurchaseOrderDataByPurchaseOrderNo(FiInvoiceHead fiInvoiceHead,FiInvoiceLine fiInvoiceLine) throws FormException{
		log.info("FiInvoiceLineService.setLinePurchaseOrderDataByPurchaseOrderNo");
		if( ( null != fiInvoiceLine) && StringUtils.hasText(fiInvoiceLine.getPurchaseOrderNo()) ){			
			//20081121 shan
			HashMap findMap = new HashMap();
			findMap.put("status", OrderStatus.FINISH);
			findMap.put("startOrderNo", fiInvoiceLine.getPurchaseOrderNo());
			findMap.put("endOrderNo", fiInvoiceLine.getPurchaseOrderNo());
			findMap.put("brandCode", fiInvoiceHead.getBrandCode());
			//findMap.put("closeOrder", PoPurchaseOrderHead.CLOSE_ORDER_N);
			findMap.put("orderTypeCode", PoPurchaseOrderHead.PURCHASE_ORDER_FOREIGN );			
			List poPurchaseOrderHeads = poPurchaseOrderHeadDAO.find(findMap);			
			//List poPurchaseOrderHeads = poPurchaseOrderHeadDAO.findByProperty("PoPurchaseOrderHead", "orderNo" , fiInvoiceLine.getPurchaseOrderNo() ) ;
			if(poPurchaseOrderHeads.size()>0){
				PoPurchaseOrderHead poPurchaseOrderHead = (PoPurchaseOrderHead)poPurchaseOrderHeads.get(0);
				fiInvoiceLine.setPoPurchaseOrderHeadId(poPurchaseOrderHead.getHeadId());				
				fiInvoiceLine.setPurchaseOrderType(poPurchaseOrderHead.getOrderTypeCode());
				fiInvoiceLine.setBrandCode(poPurchaseOrderHead.getBrandCode());
			}else{ //如果查無資料把舊資料清掉
				fiInvoiceLine.setPoPurchaseOrderHeadId(new Long(0));
				fiInvoiceLine.setPurchaseOrderNo("");
				fiInvoiceLine.setPurchaseOrderType("");
				fiInvoiceLine.setBrandCode("");				
				throw new FormException("採購單號有問題  品牌:" + fiInvoiceHead.getBrandCode() + " 單號:" + fiInvoiceLine.getPurchaseOrderNo() );
			}
		}		
	}
	
	public void doValidate(FiInvoiceLine item) throws Exception {
		List poPurchaseOrderHeads = poPurchaseOrderHeadDAO.findByProperty(
				"PoPurchaseOrderHead", "orderNo", item.getPurchaseOrderNo());
		if (poPurchaseOrderHeads.size() <= 0) {
			throw new Exception("採購單號有問題 : " + item.getPurchaseOrderNo());
		}
	}

	public void setPoPurchaseOrderHeadDAO(
			PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}


}