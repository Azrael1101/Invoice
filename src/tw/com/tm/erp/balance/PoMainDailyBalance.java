package tw.com.tm.erp.balance;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.ImReceiveHead;
import tw.com.tm.erp.hbm.service.BuBrandService;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.hbm.service.ImReceiveHeadMainService;
import tw.com.tm.erp.hbm.service.PoPurchaseOrderHeadService;
import tw.com.tm.erp.utils.BalanceAndCloseUtils;

/**
 * 採購單,進貨單 日結
 * 
 * @author T02049
 * 
 */

public class PoMainDailyBalance implements DailyBlanceInterface {

	private static final Log log = LogFactory.getLog(PoMainDailyBalance.class);

	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public String performBalance(String[] brandCodeArray, Date balanceDate, List errorMsgs, String processName, String processLotNo,
			String opUser, Date startDate, Date endDate) {
		log.info("PoDailyBalance.performBalance");
		String returnMessage = MessageStatus.SUCCESS;
		String msg = null;
		String identification = null;
		String[] orderType = {"IMR","RR"};
		ImReceiveHeadMainService imReceiveHeadMainService = (ImReceiveHeadMainService) context.getBean("imReceiveHeadMainService");
		BuBrandService buBrandService = (BuBrandService) context.getBean("buBrandService");
		BuOrderTypeService buOrderTypeService = (BuOrderTypeService) context.getBean("buOrderTypeService");
		
		try {
			for (int i = 0; i < brandCodeArray.length; i++) {
				// 出貨單結算
				BuBrand buBrand = buBrandService.findById(brandCodeArray[i]);
				if(buBrand != null){
					for(int k=0; k< orderType.length; k++){
						List<BuOrderType> orderTypes = buOrderTypeService.findOrderbyType(brandCodeArray[i], orderType[k]);
						for(BuOrderType orderTypeCode: orderTypes){
							
							List<ImReceiveHead> ImReceiveHeads = imReceiveHeadMainService.findReceiveByProperty(
									brandCodeArray[i], OrderStatus.FINISH, orderType[k], orderTypeCode.getId().getOrderTypeCode() ,startDate, endDate);
							if (ImReceiveHeads != null && ImReceiveHeads.size() > 0) { 
								for (int j = 0; j < ImReceiveHeads.size(); j++) {
									ImReceiveHead imReceiveHead = null;
									try {
										imReceiveHead = (ImReceiveHead) ImReceiveHeads.get(j);
										identification = MessageStatus.getIdentificationMsg(imReceiveHead.getBrandCode(), imReceiveHead
												.getOrderTypeCode(), imReceiveHead.getOrderNo());
										System.out.println(identification+"("+(j+1)+"/"+ImReceiveHeads.size()+")");
										imReceiveHeadMainService.executeDailyBalance(imReceiveHead, opUser);
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
}
