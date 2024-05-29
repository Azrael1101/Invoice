package tw.com.tm.erp.hbm.bean;

public class ResponseBean<T> {
	private Transaction posTransaction;

	public Transaction getPosTransaction() {
		return posTransaction;
	}

	public void setPosTransaction(Transaction posTransaction) {
		this.posTransaction = posTransaction;
	}
	
}
