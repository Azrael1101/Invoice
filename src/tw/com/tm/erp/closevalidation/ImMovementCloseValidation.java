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
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

public class ImMovementCloseValidation implements CloseValidationInterface {

	private static final Log log = LogFactory.getLog(ImMovementCloseValidation.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public void executeValidate(String brandCode, Date closeDate, List errorMsgs, String processName, Date executeDate,
			String processLotNo, String opUser) {

		doMovementValidate(brandCode, closeDate, errorMsgs, processName, executeDate, processLotNo, opUser);
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
	private void doMovementValidate(String brandCode, Date closeDate, List errorMsgs, String processName, Date executeDate,
			String processLotNo, String opUser) {

		String msg = null;
		try {
			 System.out.println("aa");
			ImMovementHeadDAO imMovementHeadDAO = (ImMovementHeadDAO) context.getBean("imMovementHeadDAO");
			List<ImMovementHead> imMovementHeads = imMovementHeadDAO.findImMovementByCriteria(new String[] { brandCode }, closeDate,
					new String[] { OrderStatus.WAIT_IN, OrderStatus.WAIT_OUT });
            System.out.println("bb");
			if (imMovementHeads != null) {
				String identification = null;
				for (ImMovementHead imMovementHead : imMovementHeads) {
					identification = MessageStatus.getIdentificationMsg(imMovementHead.getBrandCode(), imMovementHead.getOrderTypeCode(),
							imMovementHead.getOrderNo());

					//msg = identification + "的狀態為" + OrderStatus.getChineseWord(imMovementHead.getStatus()) + 
					//		"，如於月結前未轉入，此商品於月結時，將暫時轉入在途倉！";
					//errorMsgs.add(msg + "<br>");
					//BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, msg, executeDate, processLotNo, opUser);
				}
			} else {
				throw new ValidationErrorException("查詢調撥單主檔回傳結果為空值！");
			}
		} catch (Exception ex) {
			msg = "檢核調撥單是否能執行關帳程序時發生錯誤，原因：";
			log.error(msg + ex.toString());
			errorMsgs.add(msg + ex.getMessage() + "<br>");
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), executeDate, processLotNo,
					opUser);
		}
	}

}
