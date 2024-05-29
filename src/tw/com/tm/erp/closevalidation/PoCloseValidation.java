package tw.com.tm.erp.closevalidation;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.dao.ImReceiveHeadDAO;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

public class PoCloseValidation implements CloseValidationInterface {

	private static final Log log = LogFactory.getLog(PoCloseValidation.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public void executeValidate(String brandCode, Date closeDate, List errorMsgs, String processName, Date executeDate,
			String processLotNo, String opUser) {

		doReceiveValidate(brandCode, closeDate, errorMsgs, processName, executeDate, processLotNo, opUser);
	}

	/**
	 * 進貨單關帳檢核
	 * 
	 * @param brandCode
	 * @param closeDate
	 * @param errorMsgs
	 * @param processName
	 * @param executeDate
	 * @param processLotNo
	 * @param opUser
	 */
	private void doReceiveValidate(String brandCode, Date closeDate, List errorMsgs, String processName, Date executeDate,
			String processLotNo, String opUser) {

		String msg = null;
		try {
			ImReceiveHeadDAO imReceiveHeadDAO = (ImReceiveHeadDAO) context.getBean("imReceiveHeadDAO");
			List<ImReceiveHead> imReceiveHeads = null;
			
			if(!"T2".equals(brandCode)){
				imReceiveHeads = imReceiveHeadDAO.findImReceiveOrderByCriteria(new String[] { brandCode }, closeDate,
					new String[] { OrderStatus.SIGNING });
			}else{
				imReceiveHeads = imReceiveHeadDAO.findByProperty("ImReceiveHead", 
						" and brandCode = ? and (status = ? ) and receiptDate = ?  and warehouseStatus = ? ", 
						new Object[]{brandCode ,  OrderStatus.UNKNOW, closeDate, "FINISH"});
			}
//			imReceiveHeads = imReceiveHeadDAO.findByProperty("ImReceiveHead", 
//					" and brandCode = ? and (status = ? or status = ?) and receiptDate <= ?  and warehouseStatus = ? ", 
//					new Object[]{brandCode , OrderStatus.SIGNING , OrderStatus.REJECT, closeDate, "FINISH"});
			if (imReceiveHeads != null) {
				String identification = null;
				for (ImReceiveHead imReceiveHead : imReceiveHeads) {
					identification = MessageStatus.getIdentificationMsg(imReceiveHead.getBrandCode(), imReceiveHead.getOrderTypeCode(),
							imReceiveHead.getOrderNo());

					msg = identification + "的狀態為" + OrderStatus.getChineseWord(imReceiveHead.getStatus()) + "無法執行關帳程序！";
					errorMsgs.add(msg + "<br>");
					BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg, executeDate, processLotNo, opUser);
				}
			} else {
				throw new ValidationErrorException("查詢進貨單主檔回傳結果為空值！");
			}
		} catch (Exception ex) {
			msg = "檢核進貨單是否能執行關帳程序時發生錯誤，原因：";
			log.error(msg + ex.toString());
			errorMsgs.add(msg + ex.getMessage() + "<br>");
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), executeDate, processLotNo,
					opUser);
		}
	}

}
