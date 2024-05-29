package tw.com.tm.erp.utils.sp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.utils.NumberUtils;

/**
 * @author T15394
 *
 */
public class AppExtendItemStorageInfoService {

    private static final Log log = LogFactory.getLog(AppExtendItemStorageInfoService.class);

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public String [] pickName = {
			"lineId", "itemCode", "warehouseCode", "storageCode",
			"storageInNo", "storageLotNo", "blockQuantity", "message"
	};
	
	public String [] storageDeliveryName = {
			"storageLineId", "itemCode", "deliveryWarehouseCode", "deliveryStorageCode",
			"storageInNo", "storageLotNo", "storageQuantity", "message"
	};
	
	public String [] storageArrivalName = {
			"storageLineId", "itemCode", "arrivalWarehouseCode", "arrivalStorageCode",
			"storageInNo", "storageLotNo", "storageQuantity", "message"
	};
	
	/**
	 * 展儲位
	 */
	public List<Properties> executeExtendItemStorage(Properties httpRequest) throws Exception{
		log.info("executeExtendItemStorage");
		List<Properties> result = new ArrayList();
		String processObjectName = httpRequest.getProperty("processObjectName");
		String searchMethodName = httpRequest.getProperty("searchMethodName");
		Long searchKey = NumberUtils.getLong(httpRequest.getProperty("searchKey"));
		String subEntityBeanName = httpRequest.getProperty("subEntityBeanName");
		String tableType = httpRequest.getProperty("tableType");
		String isClean = httpRequest.getProperty("isClean");
		
		HashMap parameterMap = new HashMap();
		parameterMap.put("processObjectName", processObjectName);
		parameterMap.put("searchMethodName", searchMethodName);
		parameterMap.put("searchKey", searchKey);
		parameterMap.put("subEntityBeanName", subEntityBeanName);
		parameterMap.put("tableType", tableType);
		parameterMap.put("isClean", isClean);
		
		result = executeExtendItemStorage(parameterMap);
		
		return result;
	}
	
	/**
	 * 展儲位
	 */
	public List<Properties> executeExtendItemStorage(HashMap parameterMap) throws Exception{
		log.info("executeExtendItemStorage");
		List<Properties> result = new ArrayList();
		Properties properties   = new Properties();
		String processObjectName = (String)parameterMap.get("processObjectName");
		String searchMethodName = (String)parameterMap.get("searchMethodName");
		Long searchKey = (Long)parameterMap.get("searchKey");
		String subEntityBeanName = (String)parameterMap.get("subEntityBeanName");
		String tableType = (String)parameterMap.get("tableType");
		String isClean = (String)parameterMap.get("isClean");
		
		try{
			Object processObj = SpringUtils.getApplicationContext().getBean(processObjectName);
			Object entityBean = MethodUtils.invokeMethod(processObj, searchMethodName, searchKey);
			if (entityBean == null) {
				throw new NoSuchObjectException("查無單據(" + searchKey + ")的資料！");
			}
			
			List subEntityBeans = (List) PropertyUtils.getProperty(entityBean, subEntityBeanName);
			
			log.info("======= 展完報單前的資料筆數： " + subEntityBeans.size() + "=======");
			
			if (subEntityBeans != null && subEntityBeans.size() > 0) {
				Object subEntityBean = subEntityBeans.get(0);
				Class cls = subEntityBean.getClass();
				log.info("cls = " + cls);
				
				HashMap originalEntityBeansMap = produceOriginalEntityBeansMap(subEntityBeans, cls);
				
				//展儲位的資訊
				List storageInfos = getExtendItemStorageInfo(tableType, searchKey, isClean);
				
				//展完儲位
				List extendEntityBeans = getAfterExtendEntityBeans(originalEntityBeansMap, storageInfos, cls);
				
				log.info("======= 展完儲位後的資料筆數： " + extendEntityBeans.size() + "=======");

				PropertyUtils.setProperty(entityBean, subEntityBeanName, extendEntityBeans);
				BaseDAO baseDAO = (BaseDAO) SpringUtils.getApplicationContext().getBean("baseDAO");
				MethodUtils.invokeMethod(baseDAO, "update", entityBean);
			}
			result.add(properties);
			return result;
		}catch (Exception ex) {
			log.error("展儲位時發生錯誤，原因：" + ex.getMessage());
			throw new Exception(ex.getMessage());
		}
	}
	
	public List getExtendItemStorageInfo(String tableType, Long searchKey, String isClean) throws Exception {
		log.info("getExtendItemStorageInfo");
		log.info("tableType = " + tableType);
		log.info("searchKey = " + searchKey);
		Connection conn = null;
		CallableStatement calStmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List storageInfos = new ArrayList(0);
		try {
			conn = dataSource.getConnection();
			calStmt = conn.prepareCall("{call ERP.APP_EXTEND_ITEM_STORAGE_INFO.extend_item_storage_info(?,?,?)}");
			calStmt.setString(1, tableType);
			calStmt.setLong(2, searchKey);
			calStmt.setString(3, isClean);
			calStmt.executeQuery();
			stmt = conn.prepareStatement("SELECT * FROM ERP.TMP_EXTEND_ITEM_STORAGE_INFO ORDER BY ROWID");
			rs = stmt.executeQuery();
			if(null != rs){
				while (rs.next()) {
					Object[] storageInfo = new Object[8];
					//log.info(rs.getLong("LINE_ID"));
					storageInfo[0] = rs.getLong("LINE_ID");
					//log.info(rs.getString("ITEM_CODE"));
					storageInfo[1] = rs.getString("ITEM_CODE");
					//log.info(rs.getString("WAREHOUSE_CODE"));
					storageInfo[2] = rs.getString("WAREHOUSE_CODE");
					//log.info(rs.getString("STORAGE_CODE"));
					storageInfo[3] = rs.getString("STORAGE_CODE");
					//log.info(rs.getString("STORAGE_IN_NO"));
					storageInfo[4] = rs.getString("STORAGE_IN_NO");
					//log.info(rs.getString("STORAGE_LOT_NO"));
					storageInfo[5] = rs.getString("STORAGE_LOT_NO");
					//log.info(rs.getDouble("QUANTITY"));
					storageInfo[6] = rs.getDouble("QUANTITY");
					//log.info(rs.getString("MESSAGE"));
					storageInfo[7] = rs.getString("MESSAGE");
					storageInfos.add(storageInfo);
				}
			}
			return storageInfos;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("關閉ResultSet時發生錯誤！");
				}
			}
			if (calStmt != null) {
				try {
					calStmt.close();
				} catch (SQLException e) {
					log.error("關閉CallableStatement時發生錯誤！");
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("關閉PreparedStatement時發生錯誤！");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("關閉Connection時發生錯誤！");
				}
			}
		}
	}
	
	/**
	 * 產生原實體資料的對應
	 *
	 * @param subEntityBeans
	 * @return
	 * @throws Exception
	 */
	private HashMap produceOriginalEntityBeansMap(List subEntityBeans, Class cls) throws Exception {
		log.info("produceOriginalEntityBeansMap");
		HashMap map = new HashMap();
		log.info("==== 原實體資料的對應筆數 ::: " + subEntityBeans.size() + " ====");
		for (int i = 0; i < subEntityBeans.size(); i++) {
			Object subEntityBean = subEntityBeans.get(i);
			Object lineId = 0L;
			if("tw.com.tm.erp.hbm.bean.ImPickItem".equals(cls.getName())){
				lineId = PropertyUtils.getProperty(subEntityBean, "lineId");
				
			}else if("tw.com.tm.erp.hbm.bean.ImStorageItem".equals(cls.getName())){
				lineId = PropertyUtils.getProperty(subEntityBean, "storageLineId");
			}
			map.put(lineId, subEntityBean);
		}
		return map;
	}

	/**
	 * 取得展完報單資訊後的bean
	 *
	 * @param conditionMap
	 * @param originalEntityBeansMap
	 * @param tmpExtendItemInfoBeans
	 * @param cls
	 * @return List
	 * @throws Exception
	 */
	public List getAfterExtendEntityBeans(HashMap originalEntityBeansMap, List storageInfos, Class cls) throws Exception {
	    log.info("getAfterExtendEntityBeans");
	    List newObjs = new ArrayList(0);
	    for (Iterator iterator = storageInfos.iterator(); iterator.hasNext();) {
		Object[] storageInfo = (Object[]) iterator.next();
		Long originalLineId = (Long)storageInfo[0];
		Object originalsubEntityBean = originalEntityBeansMap.get(originalLineId);
		Object newObj = cls.newInstance();

		// originalLineId所對應的bean非null時執行copy
		if (originalsubEntityBean != null) {
		    BeanUtils.copyProperties(originalsubEntityBean, newObj);
		} else {
		    throw new Exception ("查無明細Id" + originalLineId + "對應的bean");
		}

		// 將TmpExtendItemInfo的資訊copy至新的bean
		if("tw.com.tm.erp.hbm.bean.ImPickItem".equals(cls.getName())){
		    for (int j = 1; j < storageInfo.length; j++) {
			PropertyUtils.setProperty(newObj, pickName[j], storageInfo[j]);
		    }
		    PropertyUtils.setProperty(newObj, "lineId", null);
		    PropertyUtils.setProperty(newObj, "indexNo", null);
		    
		    //如果不夠配message會自動空
		    if(!StringUtils.hasText((String)PropertyUtils.getProperty(newObj, "message")))
			newObjs.add(newObj);
		    
		}else if("tw.com.tm.erp.hbm.bean.ImStorageItem".equals(cls.getName())){ 
		    Double storageQuantity = (Double)storageInfo[6];
		    if(storageQuantity > 0){
			for (int j = 1; j < storageInfo.length; j++) {
			    PropertyUtils.setProperty(newObj, storageDeliveryName[j], storageInfo[j]);
			}
		    }else{
			for (int j = 1; j < storageInfo.length; j++) {
			    PropertyUtils.setProperty(newObj, storageArrivalName[j], storageInfo[j]);
			}
		    }
		    PropertyUtils.setProperty(newObj, "storageLineId", null);
		    PropertyUtils.setProperty(newObj, "indexNo", null);
		    newObjs.add(newObj);
		    
		}
		
	    }
	    log.info("newObjs.size = " + newObjs.size());
	    return newObjs;
	}
}