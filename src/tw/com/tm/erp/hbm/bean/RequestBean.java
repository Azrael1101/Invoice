package tw.com.tm.erp.hbm.bean;

import java.util.HashMap;
import java.util.Map;

//T (type) 表示具體的一個java型別
public class RequestBean<T> {
	
	//執行動作
	private String action;
	//裝前端pos的一些狀態參數
	private Map<String ,String> posTransaction;
	//裝銷售交易資料
	private Transaction transaction;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, String> getPosTransaction() {
		return posTransaction;
	}

	public void setPosTransaction(Map<String, String> posTransaction) {
		this.posTransaction = posTransaction;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
}
