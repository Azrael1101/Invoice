package tw.com.tm.erp.constants;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public final class OrderStatus {
    public final static String SAVE = "SAVE"; // 暫存
    public final static String PLAN = "PLAN"; //計畫 
    public final static String WAITING = "WAITING"; //
    public final static String SIGNING = "SIGNING"; // 簽核
    public final static String APPROVE = "APPROVE"; //
    public final static String PARTIAL = "PARTIAL"; //
    public final static String SHIPPING = "SHIPPING"; //
    public final static String VOID = "VOID"; // 作廢
    public final static String CANCEL = "CANCEL"; // 取消
    public final static String REJECT = "REJECT"; //
    public final static String BACK_ORDER = "BACK_ORDER"; //
    public final static String CLOSE = "CLOSE"; //
    public final static String FINISH = "FINISH"; // 簽核完成
    public final static String UNDELIVERY = "UNDELIVERY"; // 待出貨
    public final static String WAIT_OUT = "WAIT_OUT"; //
    public final static String WAIT_IN = "WAIT_IN"; // 簽核
    public final static String W_PICK ="W_PICK";
    public final static String W_CREATE ="W_CREATE";
    public final static String W_DELIVERY ="W_DELIVERY";
    public final static String W_RETURN ="W_RETURN";  //待退貨
    public final static String RETURN ="RETURN";  //退貨
    public final static String REFUND ="REFUND";  //退款
    public final static String LOCK ="LOCK";
    public final static String UNLOCK ="UNLOCK";
    public final static String COUNTING = "COUNTING"; //盤點中
    public final static String COUNT_FINISH = "COUNT_FINISH"; //盤點完成
    public final static String UNCONFIRMED = "UNCONFIRMED"; //未確認
    public final static String UNKNOW = "UNKNOW"; //

    public final static String FORM_SUBMIT = "SUBMIT"; // FORM 的動作
    public final static String SUBMIT_BG = "SUBMIT_BG"; // FORM 背景動作
    
    public final static String FORM_SAVE = "SAVE"; //
    public final static String FORM_VOID = "VOID"; //
    
    public final static String F_EDIT = "FORM_EDIT" ; // 編輯
    public final static String F_SAVE = "FORM_SAVE" ; // 儲存
    
    public final static String PLANING ="PLANING";//計畫
    public final static String GOLIVE ="GOLIVE";//上線
    public final static String PERFORM = "PERFORM";//進行
    public final static String TEST = "TEST";//測試
    public final static String SUSPEND = "SUSPEND";//暫停
    public final static String EVALUATE = "EVALUATE";//評估
    public final static String FINISHED = "FINISHED";//完成
    public final static String ASSIGN = "ASSIGN";//指派
    public final static String RESTORE = "RESTORE"; // 恢復
    public final static String REPLAN = "REPLAN"; // 轉派
    public final static String ORDER = "ORDER"; // 下單
    public final static String PURCHASE = "PURCHASE"; // 採購
    //public final static String TEMP = "TEMP" ; //暫存

    public static String getChineseWord(String status) {
	if (SAVE.equals(status)) {
	    return "暫存";
	} else if (WAITING.equals(status)) {
	    return "待簽核";
	} else if (PLAN.equals(status)) {
	    return "計畫";
	} else if (SIGNING.equals(status)) {
	    return "簽核中";
	} else if (APPROVE.equals(status)) {
	    return "簽核完成";
	} else if (UNDELIVERY.equals(status)) {
	    return "待出貨";
	} else if (WAIT_OUT.equals(status)) {
	    return "待轉出";
	} else if (WAIT_IN.equals(status)) {
	    return "待轉入";
	} else if (W_CREATE.equals(status)) {
	    return "待建檔";
	} else if (W_PICK.equals(status)) {
	    return "待收貨";
	} else if (W_DELIVERY.equals(status)) {
	    return "待提領";
	} else if (W_RETURN.equals(status)) {
	    return "待銷退";
	} else if (RETURN.equals(status)) {
	    return "退貨";			
	} else if (REFUND.equals(status)) {
	    return "已退款";
	} else if (LOCK.equals(status)) {
	    return "鎖定";
	} else if (UNLOCK.equals(status)) {
	    return "解鎖";
	} else if (PARTIAL.equals(status)) {
	    return "分批出貨";
	} else if (SHIPPING.equals(status)) {
	    return "運送中";
	} else if (VOID.equals(status)) {
	    return "作廢";
	} else if (CANCEL.equals(status)) {
	    return "取消";
	} else if (REJECT.equals(status)) {
	    return "駁回";
	} else if (BACK_ORDER.equals(status)) {
	    return "退單";
	} else if (CLOSE.equals(status)) {
	    return "結案";
	} else if (FINISH.equals(status)) {
	    return "簽核完成";
	} else if (COUNTING.equals(status)) {
	    return "盤點中";
	} else if (COUNT_FINISH.equals(status)) {
	    return "盤點完成";
	} else if (UNCONFIRMED.equals(status)) {
	    return "未確認";
	} else if (F_EDIT.equals(status)) {
	    return "編輯";
	} else if (F_SAVE.equals(status)) {
	    return "儲存";				
	} else if(PLANING.equals(status)){
	    return "計畫";
	}else if(GOLIVE.equals(status)){
	    return "待上線";
	}else if(SUSPEND.equals(status)){
	    return "暫停";
	}else if(TEST.equals(status)){
	    return "測試";
	}else if(PERFORM.equals(status)){
	    return "執行";
	} else if(EVALUATE.equals(status)){
	    return "評估";
	}else if(ASSIGN.equals(status)){
	    return "分配";
	}else if(FINISHED.equals(status)){
	    return "完成";
	}else if(PLAN.equals(status)){
	    return "執行中";
	}else if(ORDER.equals(status)){
	    return "下單";
	}else if(PURCHASE.equals(status)){
	    return "採購";
	}else if(RESTORE.equals(status)){
	    return "恢復";
	}else if(REPLAN.equals(status)){
	    return "轉派";
	}else {
	    return "未知";
	}
    }

    /**
     * 檢驗目前的 HEAD/LINE 狀態是否可以做 Sales Order 轉成 Delivery
     * 
     * @param status
     *                HEAD/LINE Status
     * @return boolean
     */
    public static boolean isCanSO2Delivery(String status) {
	if (status.equals(VOID) || status.equals(CLOSE)
		|| status.equals(SHIPPING)) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * 檢驗目前的 HEAD/LINE 狀態是否可以做 Back Order
     * 
     * @param status
     *                HEAD/LINE Status
     * @return boolean
     */
    public static boolean isCanBackOrder(String status) {
	if (status.equals(VOID) || status.equals(CLOSE)
		|| status.equals(SHIPPING)) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * 改變單據狀態欄位
     * 
     * @param obj
     *                is your bean
     * @param status
     *                is new status value
     */
    public static void changeStatus(Object obj, String status) {
	try {
	    Map<String, Object> map = BeanUtils.describe(obj);
	    if (map.containsKey("status")) {
		BeanUtils.setProperty(obj, "status", status);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * 在暫存之前,取得FORM暫存的狀態
     * 
     * @param formId
     * @param processId
     * @param formStatus
     * @param approvalResult
     * @return
     */
    public static String getStatus(String formActoin, Long formId,
	    Long processId, String formStatus, String approvalResult) {
	String status = OrderStatus.UNKNOW;

	if (FORM_SUBMIT.equalsIgnoreCase(formActoin)) {
	    if (formId == null) {
		status = OrderStatus.SIGNING;
	    } else if (processId != null) {
		if (formStatus.equals(OrderStatus.SAVE)) {
		    status = OrderStatus.SIGNING;
		} else if (formStatus.equals(OrderStatus.REJECT)) {
		    status = OrderStatus.SIGNING;
		} else if (formStatus.equals(OrderStatus.SIGNING)) {
		    if (approvalResult.equals("true")) {
			status = OrderStatus.SIGNING;
		    } else {
			status = OrderStatus.REJECT;
		    }
		}
	    }
	} else if (FORM_SAVE.equalsIgnoreCase(formActoin)) {
	    status = OrderStatus.SAVE;
	} else if (FORM_VOID.equalsIgnoreCase(formActoin)) {
	    status = OrderStatus.VOID;
	}

	return status;
    }
}
