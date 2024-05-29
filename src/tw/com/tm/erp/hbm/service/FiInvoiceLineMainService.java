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
import tw.com.tm.erp.hbm.bean.PoPurchaseOrderLine;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.PoPurchaseOrderLineDAO;
import tw.com.tm.erp.hbm.dao.FiInvoiceLineDAO;



/**
 * Invoice Line Service.
 * 
 * @author MyEclipse Persistence Tools
 */
public class FiInvoiceLineMainService {
	private static final Log log = LogFactory.getLog(FiInvoiceLineMainService.class);	
	private PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO ;
	private FiInvoiceLineDAO       fiInvoiceLineDAO;
	
	/**
	 * 依據PoPurchaseOrderHeadId 指定 fi invoice line order type and order no  
	 * @param fiInvoiceLine
	 */
	public void setLinePurchaseOrderData(FiInvoiceLine fiInvoiceLine){
		log.info("setLinePurchaseOrderData");
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
		log.info("setLinePurchaseOrderDataByPurchaseOrderNo");
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
		List poPurchaseOrderHeads = 
		    	poPurchaseOrderHeadDAO.findByProperty( "PoPurchaseOrderHead", "orderNo", item.getPurchaseOrderNo());
		if (poPurchaseOrderHeads.size() <= 0) {
			throw new Exception("採購單號有問題 : " + item.getPurchaseOrderNo());
		}
	}

	public void setPoPurchaseOrderHeadDAO(
			PoPurchaseOrderHeadDAO poPurchaseOrderHeadDAO) {
		this.poPurchaseOrderHeadDAO = poPurchaseOrderHeadDAO;
	}
	
	public void setFiInvoiceLineDAO(FiInvoiceLineDAO fiInvoiceLineDAO) {
	this.fiInvoiceLineDAO = fiInvoiceLineDAO;
}

    /**LINE 頁面顯示
     * @param headId
     * @param startPage
     * @param pageSize
     * @return
    */
    public List<FiInvoiceLine> findPageLine(Long headId, int startPage, int pageSize) {
	log.info("FiInvoiceLineMainService.findPageLine headId=" + headId + "startPage=" + startPage + "pageSize" + pageSize);
	return fiInvoiceLineDAO.findPageLine(headId, startPage, pageSize);
    }

    
    /**取得GRID最後一筆 indexNo
     * @param headId
     * @return
     */
    public Long findPageLineMaxIndex(Long headId) {
	log.info("fiInvoiceLineDAO.findPageLineMaxIndex");
	return fiInvoiceLineDAO.findPageLineMaxIndex(headId);
    }
    
    /**取得GRID最後一筆 customSeq
     * @param headId
     * @return
     */
    public Long findPageLineMaxCustomSeq(Long headId) {
	log.info("fiInvoiceLineDAO.findPageLineMaxCustomSeq");
	return fiInvoiceLineDAO.findPageLineMaxCustomSeq(headId);
    }
    
    
    /**find line
     * @param headId
     * @param LineId
     * @param FiInvoiceLine
     * @return
     */
    public FiInvoiceLine findLine(Long headId, Long lineId) {
	log.info("FiInvoiceLineMainService.findLine headId=" + headId + ",lineId=" + lineId);
	return fiInvoiceLineDAO.findLine(headId, lineId);
    }
	
    
    /**find line by object
     * @param headId
     * @param LineId
     * @param FiInvoiceLine
     * @return
     */
    public List<FiInvoiceLine> find( HashMap findObj ) {
	log.info("find " + findObj );
	return fiInvoiceLineDAO.find(findObj);
    }
	
    
    /**LINE 依照 declarationNo 找出 fiInvoiceLine Data
     * @param headId
     * @param startPage
     * @param pageSize
     * @return
    */
    //public List<FiInvoiceLine> findBydDclarationNo( String declarationNo ) {
    public List findBydDclarationNo( String declarationNo ) {
	log.info("FiInvoiceLineMainService.findBydDclarationNo declarationNo=" + declarationNo );
	return fiInvoiceLineDAO.findBydDclarationNo( declarationNo );
    }
    

}