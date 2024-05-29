package tw.com.tm.erp.hbm.dao;

import java.io.Serializable;
import java.util.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;

public class BaseDAO<T> extends HibernateDaoSupport {
	
	private static final Log log = LogFactory.getLog(BaseDAO.class);
	public static final String TABLE_LIST   = "tableList";
	public static final String TABLE_RECORD_COUNT   = "recordCount";
	public static final int QUERY_SELECT_ALL   		= 1;
	public static final int QUERY_SELECT_RANGE   	= 2;
	public static final int QUERY_RECORD_COUNT   	= 3;
	
	/**
	 * save
	 * 
	 * @param saveObj
	 */
	public void save(Object saveObj) {
		// getHibernateTemplate().persist(saveObj);
		getHibernateTemplate().save(saveObj);
	}

	/**
	 * update
	 * 
	 * @param updateObj
	 */
	public void update(Object updateObj) {
		// getHibernateTemplate().merge(updateObj);
		getHibernateTemplate().update(updateObj);
	}
	
	/**
	 * update by hql
	 * 
	 * @param Obj[] of replace value
	 */
	public int update(String hql, Object[] obj){
		return getHibernateTemplate().bulkUpdate(hql, obj);
	}
	
	/**
	 * merge
	 * 
	 * @param updateObj
	 */
	public void merge(Object updateObj) {
		// getHibernateTemplate().merge(updateObj);
		getHibernateTemplate().merge(updateObj);
	}	

	/**
	 * delete
	 * 
	 * @param deleteObj
	 */
	public void delete(Object deleteObj) {
		getHibernateTemplate().delete(deleteObj);
	}

	
	public List find(String queryString) {
		return getHibernateTemplate().find(queryString);
	}
	/**
	 * find by search obj but not include key
	 * 
	 * @param findObj
	 * @return
	 */
	@SuppressWarnings(value = "unchecked")
	public List<Object> findByExample(Object findObj) {
		return getHibernateTemplate().findByExample(findObj);
	}

	
	public Object findById(String beanName , Serializable id) {
		try {
			Object instance = getHibernateTemplate().get("tw.com.tm.erp.hbm.bean."+beanName, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public boolean saveOrUpdate(Object transientInstance) {
		boolean result = false;
		try {
			getHibernateTemplate().saveOrUpdate(transientInstance);
			result = true;
		} catch (RuntimeException re) {
			result = false;
			re.printStackTrace();
			throw re;
		} 
		return result;
	}
	/**
	 * find by primary key
	 * 
	 * @param findClass
	 *            return class
	 * @param pk
	 *            primary key object
	 * @return
	 */
	@SuppressWarnings(value = "unchecked")
	public Object findByPrimaryKey(Class findClass, Serializable pk) {
		return getHibernateTemplate().get(findClass, pk, null);
	}

	/**
	 * MAIN SELECT
	 * @param columns
	 * @param entityBean
	 * @param fieldNames
	 * @param fieldValue
	 * @param orderFieldName
	 * @return
	 */
	public List findByProperty(String entityBean, String columns, String fieldNames, Object fieldValue[], String orderFieldName) {
		StringBuffer queryString = new StringBuffer();
		if( StringUtils.hasText( columns ) ){
			queryString.append( "select " + columns + " ");
		}
		queryString.append( "from ");
		queryString.append(entityBean);
		
		queryString.append(" where 1=1 ");
		queryString.append(fieldNames);
		
		if( StringUtils.hasText( orderFieldName ) ){
			queryString.append(orderFieldName);
		}
		
		return fieldValue.length > 0 ? getHibernateTemplate().find(queryString.toString(), fieldValue) : getHibernateTemplate().find(queryString.toString());
	}
	
	public List findByProperty( String entityBean, String columns, String fieldNames, Object fieldValue[]) {
		return findByProperty( entityBean, columns, fieldNames, fieldValue, "" );
	}
	
	public List findByProperty( String entityBean, String fieldNames, Object fieldValue[]) {
		return findByProperty( entityBean, "", fieldNames, fieldValue, "" );
	}
	
	/**
	 * find by property
	 * 
	 * @param entityBean
	 *            entity bean name
	 * @param fieldName
	 *            search field name
	 * @param fieldValue
	 *            search field value
	 * @return
	 */
	public List findByProperty(String entityBean, String fieldName, Object fieldValue) {
		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(entityBean);
		queryString.append(" as model where model.");
		queryString.append(fieldName);
		queryString.append("= ?");
		return getHibernateTemplate().find(queryString.toString(), fieldValue);
	}
	
	public List findByProperty(String entityBean, String fieldName, Object fieldValue,String orderFieldName) {
		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(entityBean)
		           .append(" as model where model.")
		           .append(fieldName)
		           .append("= ?")
		           .append(" order by ").append(orderFieldName);
		return getHibernateTemplate().find(queryString.toString(), fieldValue);
	}

	/**
	 * find by properties
	 * 
	 * @param entityBean
	 *            entity bean name
	 * @param fieldName
	 *            search field name
	 * @param fieldValue
	 *            search field value
	 * @return
	 */
	public List findByProperty(String entityBean, String fieldNames[], Object fieldValue[]) {
		boolean second = false ;
		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(entityBean);
		queryString.append(" as model where ");
		for( String fieldName : fieldNames){
			if(second){
				queryString.append(" and ");
			}
			queryString.append(" model.");
			queryString.append(fieldName);
			queryString.append("= ? ");
			second = true ;
		}
		return getHibernateTemplate().find(queryString.toString(), fieldValue);
	}

	public List findByProperty(String entityBean, String fieldNames[], Object fieldValue[],String orderFieldName) {
		boolean second = false ;
		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(entityBean);
		queryString.append(" as model where ");
		for( String fieldName : fieldNames){
			if(second){
				queryString.append(" and ");
			}
			queryString.append(" model.");
			queryString.append(fieldName);
			queryString.append("= ? ");
			second = true ;
		}
		queryString.append(" order by ").append(orderFieldName);
		return getHibernateTemplate().find(queryString.toString(), fieldValue);
	}
	
	/**
	 * find by properties with condition
	 * 
	 * @param entityBean
	 *            entity bean name
	 * @param fieldName widh Condition
	 *            search field name
	 * @param fieldValue
	 *            search field value
	 * @return
	 */
	public List findByPropertyWithCondition(String entityBean, String fieldNames[], Object fieldValue[]) {
		boolean second = false ;
		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(entityBean);
		queryString.append(" as model where ");
		for( String fieldName : fieldNames){
			if(second){
				queryString.append(" and ");
			}
			queryString.append(" model.");
			queryString.append(fieldName);
			queryString.append(" ? ");
			second = true ;
		}
		return getHibernateTemplate().find(queryString.toString(), fieldValue);
	}
	
	public List findByPropertyWithCondition(String entityBean, String fieldNames[], Object fieldValue[],String orderFieldName) {
		boolean second = false ;
		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(entityBean);
		queryString.append(" as model where ");
		for( String fieldName : fieldNames){
			if(second){
				queryString.append(" and ");
			}
			queryString.append(" model.");
			queryString.append(fieldName);
			queryString.append(" ? ");
			second = true ;
		}
		queryString.append(" order by ").append(orderFieldName);
		return getHibernateTemplate().find(queryString.toString(), fieldValue);
	}
	
	public T findFirstByProperty(String entityBean, String columns, String fieldNames, Object[] fieldValue, String orderFieldName){
		List<T> lists = this.findByProperty(entityBean, columns, fieldNames, fieldValue, orderFieldName);
		return this.findOne(lists, 0);
	}
	public T findFirstByProperty(String entityBean, String fieldNames, Object[] fieldValue){
		return findFirstByProperty(entityBean, "", fieldNames, fieldValue, "");
	}
	public T findLastByProperty(String entityBean, String columns, String fieldNames, Object[] fieldValue, String orderFieldName){
		List<T> lists = this.findByProperty(entityBean, columns, fieldNames, fieldValue, orderFieldName);
		return this.findOne(lists, (lists.size()-1) );
	}
	
	/**
	 * find only one in i
	 * @param records
	 * @param i
	 * @return
	 */
	private T findOne(List<T> records, int i ){
		
		if(records != null && records.size() > 0 ){
			return records.get(i);
		}
		return null;
	}
	
	/**
	 * delete record will field value is null
	 * 
	 * @param tableName
	 * @param nullFieldName
	 * @return
	 */
	public int deleteNullField(String tableName, String nullFieldName) {
		final String tn = tableName;
		final String fn = nullFieldName;
		int t = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("delete " + tn + " where " + fn + " is null");
				Query query = session.createSQLQuery(hql.toString());
				return query.executeUpdate();
			}
		});
		return t;
	}
	
	/**
	 * main search for 分頁
	 * @param tableName
	 * @param columns
	 * @param map
	 * @param order
	 * @param startPage
	 * @param pageSize
	 * @param searchType
	 * @return
	 */
	public Map search(final String tableName, final String columns, final Map map, final String order, final int startPage, final int pageSize, final int searchType ){
		log.info( "================<search>=================");
		log.info( "searchType = " + searchType );
		log.info( "startPage = " + startPage );
		log.info( "pageSize = " + pageSize );
		List result = getHibernateTemplate().executeFind(new HibernateCallback( ) {
			public Object doInHibernate(Session session)throws HibernateException, SQLException { 
				StringBuffer hql = new StringBuffer(); 
				//這裡判斷選擇的方法
				switch ( searchType ) {  
  				case QUERY_SELECT_ALL:
  				case QUERY_RECORD_COUNT:
  					if( StringUtils.hasText( columns) ){
  						hql.append( "select " );
  						hql.append( columns );
  						hql.append( " " );
  					}else{
  						//hql.append( "select " );
  						//throw new SQLException(" columns 必須有此搜尋條件 ");
  					}
  					break;
  				case QUERY_SELECT_RANGE:
  					if( StringUtils.hasText( columns) ){
  						hql.append( "select " );
  						hql.append( columns );
  						hql.append( " " );
  					}
  					break;
  				default:
  					throw new SQLException(" searchType 無此搜尋條件 ");
				}
				
				hql.append( "from " );
				hql.append( tableName );
				hql.append( " where 1 = 1 " );
				
				Iterator it = map.keySet().iterator();
				while (it.hasNext()) {
					// objectKey = SQL敘述
					Object objectKey = (Object) it.next();
					String strKey = (String)objectKey;
					//log.info( "objectKey = " + objectKey );
					if( map.containsKey(objectKey) ){
						Object objectValue = map.get( objectKey );
						//log.info( "objectValue = " + objectValue );
						if(objectValue != null){
							if(objectValue instanceof Date){
  								hql.append( objectKey );
	  						}else if( objectValue instanceof String ){ 
	  							String strValue = (String) objectValue;
	  							if( strKey.indexOf("like :") > -1 ){	// 代表使用like
	  								if ( !"%%".equals(strValue) ){
	  									log.info( strKey + "使用 like 判定" + (strKey.indexOf(" like ") > -1)  ); 
	  									hql.append( objectKey );
	  								}
	  							}else if(StringUtils.hasText( strValue ) ){
	  								hql.append( objectKey );
	  							}
	  						}else if( objectValue instanceof Long ){
	  							hql.append( objectKey );
	  						}else if( objectValue instanceof Double ){
	  						    hql.append( objectKey );
	  						}else if( objectValue instanceof String[] ){
	  							if( strKey.indexOf("in (:") > -1 ){	// 代表使用in
	  								log.info( strKey + "使用 in 判定" + (strKey.indexOf(" in ") > -1)  ); 
	  								hql.append( objectKey );
	  							}
	  						}
						}
					}
				}
				
				hql.append( " "  + order );
				log.info( "HQL = " + hql.toString() );
				
				Query query = session.createQuery(hql.toString());
				
				if( QUERY_SELECT_RANGE == searchType ){
	  				query.setFirstResult( pageSize * startPage );
	  				query.setMaxResults( pageSize );
				}
				
				Iterator setIt = map.keySet().iterator();
				while (setIt.hasNext()) {
					// strKey = SQL敘述
					String strKey = (String) setIt.next();
					// columnKey = :後的所有字元 (如果為in則要去掉最後一個尾碼)
					String columnKey 	 = strKey.substring( strKey.indexOf(":") + 1, strKey.indexOf("in (:") > 0 ? strKey.indexOf(")") : strKey.length() );
					//log.info( "strKey = " + strKey );
					//log.info( "columnKey = " + columnKey );
					if( map.containsKey(strKey) ){
						Object objectValue = map.get( strKey );
						//log.info( "objectValue = " + objectValue );
						if(objectValue != null){
	  						if( objectValue instanceof Date ){
	  								query.setDate( columnKey, (Date)objectValue );
	  						}else if( objectValue instanceof String ){ 
	  							String strValue = (String) objectValue;
	  							if( strKey.indexOf("like :") > -1 ){	// 代表使用like
	  								if ( !"%%".equals(strValue) ){
	  									query.setString( columnKey, strValue );
	  								}
	  							}else if( StringUtils.hasText(strValue) ){
	  								query.setString( columnKey, strValue );
	  							}
	  						}else if( objectValue instanceof Long ){
	  							query.setLong( columnKey, (Long)objectValue );
	  						}else if( objectValue instanceof Double ){
	  						    query.setDouble( columnKey, (Double)objectValue );
	  						}else if( objectValue instanceof String[] ){
	  							if( strKey.indexOf("in (:") > -1 ){	// 代表使用in
	  								query.setParameterList( columnKey, (String[])objectValue);
	  							}
	  						}
						}
					}
				}
				List list = query.list();
				log.info( " 搜尋結果 size = " + list.size() );
				return list;
			}
		});
		
		Map returnResult = new HashMap();		
		returnResult.put( TABLE_LIST, QUERY_SELECT_ALL == searchType || QUERY_SELECT_RANGE == searchType? result: null );			
		if(result.size() == 0){
			returnResult.put( TABLE_RECORD_COUNT, 0L);
		}else{
			log.info( tableName + ".size = "+result.size() );
			returnResult.put( TABLE_RECORD_COUNT, QUERY_SELECT_ALL == searchType || QUERY_SELECT_RANGE == searchType ? result.size() : Long.valueOf(result.get(0).toString()) );
		}
		log.info( "================</search>=================");
		return returnResult;
	}
	public Map search(String tableName, Map findObjs, int startPage, int pageSize, int searchType ){
		return search( tableName, "", findObjs, "", startPage, pageSize, searchType);
	}
	public Map search(String tableName, Map findObjs, String order,int startPage, int pageSize, int searchType ){
		return search( tableName, "", findObjs, order, startPage, pageSize, searchType);
	}
	public Map search(String tableName, String columns,Map findObjs, int startPage, int pageSize, int searchType ){
		return search( tableName, columns, findObjs, "", startPage, pageSize, searchType);
	}
	public Map search(String tableName, String columns,Map findObjs, int searchType ){
		return search( tableName, columns, findObjs, "", 0, 0, searchType);
	}
	public Map search(String tableName, String columns,Map findObjs, String order, int searchType ){
		return search( tableName, columns, findObjs, order, 0, 0, searchType);
	}
	
    public List findPageLine(String model, String head, String identifyN ,Long identify, int startPage, int pageSize) {
    	final int startRecordIndexStar = startPage * pageSize;
    	final int pSize = pageSize;
    	final String identifyName = identifyN;
    	final Long identifyId = identify;
    	final String modelName = model;
    	final String headName = head;
    	List result = getHibernateTemplate().executeFind(
    		new HibernateCallback() {
    		    public Object doInHibernate(Session session)
    			    throws HibernateException, SQLException {
    			StringBuffer hql = new StringBuffer("from "+modelName+" as model where 1=1 ");
    			if (identifyId != null)
    			    hql.append(" and model."+headName+identifyName+" = :"+identifyName+" order by indexNo");
    			Query query = session.createQuery(hql.toString());
    			query.setFirstResult(startRecordIndexStar);
    			query.setMaxResults(pSize);
    			if (identifyId != null)
    			    query.setLong(identifyName, identifyId);
    			return query.list();
    		    }
    		});
    	return result;
        }
    
    public List findPageLine(String model, String head, Long identify, int startPage, int pageSize) {
    	return findPageLine(model, head, "headId" ,identify, startPage, pageSize);
    }
    
    public Object findPageLineMaxIndex(String model, String head, String identifyN ,Long identify ,String order) {
    	Object objects = null;
    	final String identifyName = identifyN;
    	final Long identifyId = identify;
    	final String modelName = model;
    	final String headName = head;
    	final String orderBy = order;
    	List result = getHibernateTemplate().executeFind(
    		new HibernateCallback() {
    		    public Object doInHibernate(Session session)
    			    throws HibernateException, SQLException {
    			StringBuffer hql = new StringBuffer("from "+modelName+" as model where 1=1 ");
    			if (identifyId != null)
    			    hql.append(" and model."+headName+identifyName+" = :"+identifyName);
    			if(StringUtils.hasText(orderBy))
    				hql.append(" order by " + orderBy);
    			Query query = session.createQuery(hql.toString());
    			if (identifyId != null)
    			    query.setLong(identifyName, identifyId);
    			return query.list();
    		    }
    		});
    	if (result != null && result.size() > 0) {
    	    objects = result.get(0);		    
    	}
    	return objects;
        }
    
    public Object findPageLineMaxIndex(String model, String head ,Long identify) {
    	return findPageLineMaxIndex(model, head, "headId" ,identify ,"indexNo desc");
    }
    
    public Object find(String hql, Object[] obj){
		return getHibernateTemplate().find(hql, obj);
	}
}
