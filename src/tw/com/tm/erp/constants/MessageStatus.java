package tw.com.tm.erp.constants;

import java.util.List;

import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;

public class MessageStatus {
	public static final String ERROR = "Error";
	public static final String SUCCESS = "Success";
	public static final String ERROR_NO_DETAIL = "請輸入明細";
	public static final String FAIL = "Fail";
	public static final String NONE = "None";
	public static final String DAILY_BALANCE = "日結";
	public static final String DAILY_CLOSE = "日關帳";
	public static final String MONTHLY_BALANCE = "月結";
	public static final String MONTHLY_CLOSE = "月關帳";
	public static final String LOG_INFO = "INFO";
	public static final String LOG_ERROR = "ERROR";
	public static final String DAILY_BALANCE_SUCCESS = "執行日結作業程序成功！";
	public static final String DAILY_BALANCE_PARTIAL_FAIL = "日結作業程序已完成，但部分資料執行結算失敗！";
	public static final String DAILY_BALANCE_FAIL = "執行日結作業程序失敗！";
	public static final String DAILY_BALANCE_NONE = "日結作業程序已完成，但無任何品牌進行結算！";	
	public static final String DAILY_CLOSE_SUCCESS = "執行日關帳作業程序成功！";
	public static final String DAILY_CLOSE_SUCCESS_CM = "執行海關日關帳作業程序成功！";
	public static final String DAILY_CLOSE_FAIL = "執行日關帳作業程序失敗！";
	public static final String DAILY_CLOSE_FAIL_CM = "執行海關日關帳作業程序失敗！";
	public static final String AUDIT_UP_SUCCESS = "執行轉入保稽作業程序成功！";
	public static final String AUDIT_UP_FAIL = "執行轉入保稽作業程序失敗！";
	public static final String MONTHLY_CLOSE_SUCCESS = "執行月關帳作業程序成功！";
	public static final String MONTHLY_CLOSE_FAIL = "執行月關帳作業程序失敗！";
	public static final String MONTHLY_BALANCE_SUCCESS = "執行月結作業程序成功！";
	public static final String MONTHLY_BALANCE_FAIL = "執行月結作業程序失敗！";
	public static final String CUSTOMER_DL_SUCCESS = "執行客戶資料下傳成功！";
	public static final String CUSTOMER_DL_FAIL = "執行客戶資料下傳失敗！";
	public static final String SALES_DL_SUCCESS = "執行專櫃人員資料下傳成功！";
	public static final String SALES_DL_FAIL = "執行專櫃人員資料下傳失敗！";
	public static final String GOODSQTY_DL_SUCCESS = "執行商品庫存資料下傳成功！";
	public static final String GOODSQTY_DL_FAIL = "執行商品庫存資料下傳失敗！";	
	public static final String GOODSLIST_DL_SUCCESS = "執行商品主檔下傳成功！";
	public static final String GOODSLIST_DL_FAIL = "執行商品主檔下傳失敗！";
	public static final String EC_GOODSLIST_DL_SUCCESS = "執行EC商品主檔下傳成功！";
	public static final String EC_GOODSLIST_DL_FAIL = "執行EC商品主檔下傳失敗！";
	public static final String PROMOTION_DL_SUCCESS = "執行促銷資料下傳成功！";
	public static final String PROMOTION_DL_FAIL = "執行促銷資料下傳失敗！";
	public static final String MOVE_DL_SUCCESS = "執行調撥資料下傳成功！";
	public static final String MOVE_DL_FAIL = "執行調撥資料下傳失敗！";
	public static final String EANCODE_DL_SUCCESS = "執行國際碼資料下傳成功！";
	public static final String EANCODE_DL_FAIL = "執行國際碼資料下傳失敗！";	
	public static final String ON_HAND_DL_SUCCESS = "執行離島庫存資料下傳成功！";
	public static final String ON_HAND_DL_FAIL = "執行離島庫存資料下傳失敗！";	
	public static final String ITEM_DISCOUNT_DL_SUCCESS = "執行商品折扣資料下傳成功！";
	public static final String ITEM_DISCOUNT_DL_FAIL = "執行商品折扣資料下傳失敗！";
	public static final String DATA_NOT_FOUND = "查無資料" ;
	public static final String VALIDATION_FAILURE = "未通過檢核，請點選訊息提示按鈕檢視錯誤訊息！";
	public static final String ITEM_NOT_FOUND = "查無此商品" ;
	public static final String COMBINE_DL_SUCCESS = "執行組合單資料下傳成功！";
	public static final String COMBINE_DL_FAIL = "執行組合單資料下傳失敗！";
	public static final String GROUPED_DL_SUCCESS = "執行組合包裝資料下傳成功！";
	public static final String GROUPED_DL_FAIL = "執行組合包裝資料下傳失敗！";
	public static final String FULL_DL_SUCCESS = "執行滿額贈資料下傳成功！";
	public static final String FULL_DL_FAIL = "執行滿額贈資料下傳失敗！";
	

	/**
	 * 用來判斷是否有錯誤
	 * 
	 * @param message
	 * @return
	 */
	public static boolean isSuccess(String message) {
		boolean re = true;
		if ((null != message) && (message.length() >= 5))
			if (MessageStatus.ERROR.equalsIgnoreCase(message.substring(0, 5)))
				re = false;
		return re;
	}

	/**
	 * 顯示常用之錯誤訊息 中文敘述
	 * 
	 * @param status
	 * @return
	 */
	public static String getChineseWord(String status) {
		StringBuffer re = new StringBuffer("錯誤訊息 : ");

		return re.toString();
	}

	/**
	 * JobManagerMsg 品牌-單別-單號(中文名稱)
	 * $orderNo;
	 * 
	 * @param orderName
	 * @param brandCode
	 * @param orderType
	 * @param orderNo
	 * @return
	 */
	public static String getJobManagerMsg(String brandCode, String orderTypeCode, String orderNo) {
		BuOrderTypeService buOrderTypeService = (BuOrderTypeService)SpringUtils.getApplicationContext().getBean("buOrderTypeService");		
		BuOrderTypeId id = new BuOrderTypeId();
		id.setBrandCode(brandCode);
		id.setOrderTypeCode(orderTypeCode);
		BuOrderType buOrderType = buOrderTypeService.findById(id);
		String orderName = buOrderType.getName();		
		StringBuffer sb = new StringBuffer();
		sb.append(brandCode);
		sb.append("-");
		sb.append(orderTypeCode);
		sb.append("-");
		sb.append(orderNo);
		sb.append("(");
		sb.append(orderName);
		sb.append(")");		
		return sb.toString();
	}
	
	/**
	 * IdentificationMsg 品牌-單別-單號
	 * 
	 * @param brandCode
	 * @param orderType
	 * @param orderNo
	 * @return
	 */
	public static String getIdentificationMsg(String brandCode, String orderTypeCode, String orderNo) {		
		StringBuffer sb = new StringBuffer();
		sb.append(brandCode);
		sb.append("-");
		sb.append(orderTypeCode);
		sb.append("-");
		sb.append(orderNo);
		return sb.toString();
	}
	
	/**
	 * Identification 品牌單別單號
	 * 
	 * @param brandCode
	 * @param orderType
	 * @param orderNo
	 * @return
	 */
	public static String getIdentification(String brandCode, String orderTypeCode, String orderNo) {		
		StringBuffer sb = new StringBuffer();
		sb.append(brandCode);
		sb.append(orderTypeCode);
		sb.append(orderNo);
		return sb.toString();
	}
}
