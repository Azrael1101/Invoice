package tw.com.tm.erp.importdb;

import java.util.HashMap;
import java.util.List;

/**
 * 使用檔案匯入資料要Implement這個Interface 
 * @author T02049
 *
 */

public interface ImportDataAbs {

	/**
	 * initial import data properties 
	 * @param uiProperties 
	 * @return
	 */
	public abstract ImportInfo initial(HashMap uiProperties);	

	/**
	 * write entity bean to database
	 * @param entityBeans
	 * @return result 
	 */
	public abstract String updateDB(List entityBeans,ImportInfo info) throws Exception ;
	
	

}
