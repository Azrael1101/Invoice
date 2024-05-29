package tw.com.tm.erp.balance;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.service.ImReceiveHeadService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

/**
 * 採購單,進貨單 日結
 * 
 * @author T02049
 * 
 */

public class PoDailyBalance implements DailyBlanceInterface {

	private static final Log log = LogFactory.getLog(PoDailyBalance.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public String performBalance(String[] brandCodeArray, Date balanceDate, List errorMsgs,
			String processName, String processLotNo, String opUser, Date startDate, Date endDate) {
		log.info("PoDailyBalance.performBalance");
		String returnMessage = MessageStatus.SUCCESS;
		String msg = null;
		String identification = null;
		ImReceiveHeadService imReceiveHeadService = (ImReceiveHeadService) context.getBean("imReceiveHeadService");
		try {
			for (int i = 0; i < brandCodeArray.length; i++) {
				// 出貨單結算
				List<ImReceiveHead> ImReceiveHeads = imReceiveHeadService.findReceiveByProperty(brandCodeArray[i], OrderStatus.FINISH);
				if (ImReceiveHeads != null && ImReceiveHeads.size() > 0) {
					for (int j = 0; j < ImReceiveHeads.size(); j++) {
						ImReceiveHead imReceiveHead = null;
						try {
							imReceiveHead = (ImReceiveHead) ImReceiveHeads.get(j);
							identification = MessageStatus.getIdentificationMsg(imReceiveHead.getBrandCode(), imReceiveHead
									.getOrderTypeCode(), imReceiveHead.getOrderNo());
							imReceiveHeadService.executeDailyBalance(imReceiveHead, opUser);
						} catch (Exception ex) {
							msg = identification + "執行結算時發生錯誤，原因：";
							log.error(msg + ex.toString());
							errorMsgs.add(msg + ex.getMessage() + "<br>");
							BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate,
									processLotNo, opUser);
							returnMessage = MessageStatus.ERROR;
						}
					}
				}

			}
			System.out.println("PoDailyBalance finish...");
			return returnMessage;
		} catch (Exception ex) {
			msg = "執行進貨單結算時發生錯誤，原因：";
			log.error(msg + ex.toString());
			errorMsgs.add(msg + ex.getMessage() + "<br>");
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate, processLotNo,
					opUser);
			returnMessage = MessageStatus.ERROR;
			return returnMessage;
		} finally {
			BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "調整單模組結算作業完成！", balanceDate, processLotNo, opUser);
		}
	}
	/*
	 * public String performBalance(String[] brandCodeArray, Date balanceDate,
	 * List errorMsgs, String processName, String processLotNo, String opUser) {
	 * String returnMessage = MessageStatus.SUCCESS; return returnMessage; }
	 */

	/*
	 * String msg = null; try{ ImReceiveHeadService imReceiveHeadService =
	 * (ImReceiveHeadService) context.getBean("imReceiveHeadService");
	 * 
	 * String identification = null; for(int i = 0; i < brandCodeArray.length;
	 * i++){ //出貨單結算 List imDeliveryHeads =
	 * imDeliveryHeadDAO.findDeliveryByProperty(brandCodeArray[i],
	 * OrderStatus.FINISH, ""); if(imDeliveryHeads != null &&
	 * imDeliveryHeads.size() > 0){ for(int j = 0; j < imDeliveryHeads.size();
	 * j++){ ImDeliveryHead deliveryHead = null; try{ deliveryHead =
	 * (ImDeliveryHead)imDeliveryHeads.get(j); identification =
	 * MessageStatus.getIdentificationMsg(deliveryHead.getBrandCode(),
	 * deliveryHead.getOrderTypeCode(), deliveryHead.getOrderNo());
	 * imDeliveryService.executeDailyBalance(deliveryHead, opUser);
	 * }catch(Exception ex){ msg = identification + "執行結算時發生錯誤，原因：";
	 * log.error(msg + ex.toString()); errorMsgs.add(msg + ex.getMessage() + "<br>");
	 * BalanceAndCloseUtils.createSystemLog(processName,
	 * MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate,
	 * processLotNo, opUser); returnMessage = MessageStatus.ERROR; } } } //退貨單結算
	 * imDeliveryHeads =
	 * imDeliveryHeadDAO.findDeliveryByProperty(brandCodeArray[i],
	 * OrderStatus.FINISH, "IR"); if(imDeliveryHeads != null &&
	 * imDeliveryHeads.size() > 0){ for(int k = 0; k < imDeliveryHeads.size();
	 * k++){ ImDeliveryHead deliveryHead = null; try{ deliveryHead =
	 * (ImDeliveryHead)imDeliveryHeads.get(k); identification =
	 * MessageStatus.getIdentificationMsg(deliveryHead.getBrandCode(),
	 * deliveryHead.getOrderTypeCode(), deliveryHead.getOrderNo());
	 * imDeliveryService.executeDailyBalanceForReturn(deliveryHead, opUser);
	 * }catch(Exception ex){ msg = identification + "執行結算時發生錯誤，原因：";
	 * log.error(msg + ex.toString()); errorMsgs.add(msg + ex.getMessage() + "<br>");
	 * BalanceAndCloseUtils.createSystemLog(processName,
	 * MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate,
	 * processLotNo, opUser); returnMessage = MessageStatus.ERROR; } } } }
	 * return returnMessage; }catch(Exception ex){ msg = "執行銷售模組結算時發生錯誤，原因：";
	 * log.error(msg + ex.toString()); errorMsgs.add(msg + ex.getMessage() + "<br>");
	 * BalanceAndCloseUtils.createSystemLog(processName,
	 * MessageStatus.LOG_ERROR, msg + ex.getMessage(), balanceDate,
	 * processLotNo, opUser); returnMessage = MessageStatus.ERROR; return
	 * returnMessage; }finally{
	 * BalanceAndCloseUtils.createSystemLog(processName, MessageStatus.LOG_INFO,
	 * "銷貨模組結算作業完成！", balanceDate, processLotNo, opUser); } }
	 */
}
