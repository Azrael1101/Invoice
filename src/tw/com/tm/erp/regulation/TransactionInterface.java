package tw.com.tm.erp.regulation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import tw.com.tm.erp.hbm.bean.Transaction;
 
public interface TransactionInterface {
	
	public Transaction executeInitTransaction(Map<String, String> posTransaction);
	public Transaction executeDiscount(Map<String, String> posTransaction);
	
}
