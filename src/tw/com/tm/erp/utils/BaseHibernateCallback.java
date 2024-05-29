package tw.com.tm.erp.utils;

import java.sql.SQLException;
import java.util.HashMap;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class BaseHibernateCallback implements HibernateCallback{
	
	private String tableName;
	private String columns;
	private HashMap find;
	private String order;
	private int startPage; 
	private int pageSize;
	private int searchType;
	 
	public BaseHibernateCallback(String tableName, String columns, HashMap find, String order, int startPage, int pageSize, int searchType ) {
		this.tableName = tableName;
		this.columns = columns;
		this.find = find;
		this.order = order;
		this.startPage = startPage;
		this.pageSize = pageSize;
		this.searchType = searchType;
		
	}
	
	public Object doInHibernate(Session arg0) throws HibernateException,
			SQLException {
			
		return null;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public HashMap getFind() {
		return find;
	}

	public void setFind(HashMap find) {
		this.find = find;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

}
