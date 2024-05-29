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
import tw.com.tm.erp.hbm.bean.ImAdjustmentHead;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

public class ImAdjustmentCloseValidation implements CloseValidationInterface {

	private static final Log log = LogFactory.getLog(ImAdjustmentCloseValidation.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public void executeValidate(String brandCode, Date closeDate, List errorMsgs, String processName, Date executeDate,
			String processLotNo, String opUser) {

		doAdjustmentValidate(brandCode, closeDate, errorMsgs, processName, executeDate, processLotNo, opUser);
	}

	/**
	 * 調整單關帳檢核
	 * 
	 * @param brandCode
	 * @param closeDate
	 * @param errorMsgs
	 * @param processName
	 * @param executeDate
	 * @param processLotNo
	 * @param opUser
	 */
	private void doAdjustmentValidate(String brandCode, Date closeDate, List errorMsgs, String processName, Date executeDate,
			String processLotNo, String opUser) {

		String msg = null;
		try {
			ImAdjustmentHeadDAO imAdjustmentHeadDAO = (ImAdjustmentHeadDAO) context.getBean("imAdjustmentHeadDAO");
			List<ImAdjustmentHead> imAdjustmentHeads = imAdjustmentHeadDAO.findImAdjustmentByCriteria(new String[] { brandCode }, closeDate,
					new String[] { OrderStatus.UNKNOW });

			if (imAdjustmentHeads != null) {
				String identification = null;
				for (ImAdjustmentHead imAdjustmentHead : imAdjustmentHeads) {
					identification = MessageStatus.getIdentificationMsg(imAdjustmentHead.getBrandCode(), imAdjustmentHead.getOrderTypeCode(),
							imAdjustmentHead.getOrderNo());

					msg = identification + "的狀態為" + OrderStatus.getChineseWord(imAdjustmentHead.getStatus()) + "無法執行關帳程序！";
					errorMsgs.add(msg + "<br>");
					BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg, executeDate, processLotNo, opUser);
				}
			} else {
				throw new ValidationErrorException("查詢調整單主檔回傳結果為空值！");
			}
		} catch (Exception ex) {
			msg = "檢核調整單是否能執行關帳程序時發生錯誤，原因：";
			log.error(msg + ex.toString());
			errorMsgs.add(msg + ex.getMessage() + "<br>");
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.toString(), executeDate, processLotNo,
					opUser);
		}
	}

}
