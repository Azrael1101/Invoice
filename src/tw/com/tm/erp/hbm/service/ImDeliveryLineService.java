package tw.com.tm.erp.hbm.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.ImDeliveryHead;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.dao.ImDeliveryHeadDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryLineDAO;

public class ImDeliveryLineService {

    private static final Log log = LogFactory.getLog(ImDeliveryLineService.class);

    private ImDeliveryHeadDAO imDeliveryHeadDAO;

    private ImDeliveryLineDAO imDeliveryLineDAO;

    /* spring IoC */
    public void setImDeliveryHeadDAO(ImDeliveryHeadDAO imDeliveryHeadDAO) {
	this.imDeliveryHeadDAO = imDeliveryHeadDAO;
    }

    public void setImDeliveryLineDAO(ImDeliveryLineDAO imDeliveryLineDAO) {
	this.imDeliveryLineDAO = imDeliveryLineDAO;
    }
    
    /**
     * 更新銷退單明細
     * 
     * @param deliveryHead
     * @throws FormException
     * @throws Exception
     */
    public void saveLineDateForReturn(ImDeliveryHead deliveryHead) throws FormException, Exception{
	
	try{
	    Long headId = deliveryHead.getHeadId();
            List<ImDeliveryLine> deliveryLines = deliveryHead.getImDeliveryLines();
            if(deliveryLines != null){
	        for(int i = 0; i < deliveryLines.size(); i++){
	            ImDeliveryLine deliveryLine = (ImDeliveryLine)deliveryLines.get(i);
	            Long lineId = deliveryLine.getLineId();
	            if(lineId == null){
	        	throw new ValidationErrorException("第" + (i + 1) + "項明細的主鍵為空值！");
	            }
	            //==========================取得對應的銷退單明細==============================
	            ImDeliveryLine deliveryLinePO = imDeliveryLineDAO.findItemByIdentification(headId, lineId);
	            if(deliveryLinePO == null){
	        	throw new ValidationErrorException("無法取得第" + (i + 1) + "項明細主鍵：" + lineId + "的資料！");
	            }
	            //==========================取得對應的原出貨單明細===============================	            
		    String reserve2 = deliveryLine.getReserve2();
		    if(!StringUtils.hasLength(reserve2)){
			throw new ValidationErrorException("第" + (i + 1) + "項明細所對應的出貨單明細資訊為空值！");
		    }
		    ImDeliveryLine origDeliveryLinePO = (ImDeliveryLine)imDeliveryLineDAO.
		        findByPrimaryKey(ImDeliveryLine.class, Long.valueOf(reserve2));
		    if(origDeliveryLinePO == null){
			throw new ValidationErrorException("查無第" + (i + 1) + "項明細所對應的出貨單主鍵：" + Long.valueOf(reserve2) + "的資訊！");
		    }
		    //更新資訊
		    deliveryLinePO.setReturnQuantity(origDeliveryLinePO.getShipQuantity());
		    deliveryLinePO.setReturnedQuantity(origDeliveryLinePO.getReturnedQuantity());
		    deliveryLine.setReturnQuantity(origDeliveryLinePO.getShipQuantity());
		    deliveryLine.setReturnedQuantity(origDeliveryLinePO.getReturnedQuantity());
		    
		    imDeliveryLineDAO.update(deliveryLinePO);
	        }
            }
	} catch (FormException fe) {
	    log.error("更新銷退單明細失敗，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("更新銷退單明細時發生錯誤，原因：" + ex.toString());
	    throw new Exception("更新銷退單明細時發生錯誤，原因：" + ex.getMessage());
	}
    }
}
