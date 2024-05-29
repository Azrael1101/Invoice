package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.DbcInformationColumns;
import tw.com.tm.erp.hbm.bean.DbcInformationTables;
import tw.com.tm.erp.hbm.bean.OmmChannel;
import tw.com.tm.erp.hbm.bean.OmmChannelConfig;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFHead;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFLine;
import tw.com.tm.erp.hbm.dao.OmmChannelConfigDAO;
import tw.com.tm.erp.hbm.dao.OmmChannelDAO;
import tw.com.tm.erp.hbm.dao.OmmChannelTXFHeadDAO;
import tw.com.tm.erp.hbm.dao.OmmChannelTXFLineDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class OmmChannelTXFService {
	private static final Log log = LogFactory.getLog(OmmChannelTXFService.class);
	
	
	// spring IOC DAO
	private OmmChannelTXFHeadDAO ommChannelTXFHeadDAO;
	private OmmChannelTXFLineDAO ommChannelTXFLineDAO;
	private OmmChannelDAO ommChannelDAO;
	private OmmChannelConfigDAO ommChannelConfigDAO;

	// spring IOC Service
	private BuOrderTypeService buOrderTypeService;
	private DbcInformationTablesService dbcInformationTablesService;
	private OmmChannelService ommChannelService;
	
	
	
	public void setOmmChannelDAO(OmmChannelDAO ommChannelDAO) {
		this.ommChannelDAO = ommChannelDAO;
	}

	public void setOmmChannelConfigDAO(OmmChannelConfigDAO ommChannelConfigDAO) {
		this.ommChannelConfigDAO = ommChannelConfigDAO;
	}

	public void setOmmChannelTXFLineDAO(OmmChannelTXFLineDAO ommChannelTXFLineDAO) {
		this.ommChannelTXFLineDAO = ommChannelTXFLineDAO;
	}

	public void setOmmChannelService(OmmChannelService ommChannelService) {
		this.ommChannelService = ommChannelService;
	}

	public void setDbcInformationTablesService(DbcInformationTablesService dbcInformationTablesService) {
		this.dbcInformationTablesService = dbcInformationTablesService;
	}
	
	public void setOmmChannelTXFHeadDAO(OmmChannelTXFHeadDAO ommChannelTXFHeadDAO) {
		this.ommChannelTXFHeadDAO = ommChannelTXFHeadDAO;
	}
	
	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public static final String[] GRID_FIELD_NAMES = { 
        "columnIndex", "columnCode", "columnName", "enable"};

	public static final String[] GRID_FIELD_DEFAULT_VALUES = {
        "", "", "", ""};
	
	public static final int[] GRID_FIELD_TYPES = {
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING
	};

	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{
		log.info("getAJAXPageData...");
		try{
		    List<Properties> result = new ArrayList();
		    List<Properties> gridDatas = new ArrayList();
		    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
		    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
		    
		    String sysSnoString = httpRequest.getProperty("sysSno");
		    Long sysSno = NumberUtils.getLong(sysSnoString);
		    //==============================================================
		    List<OmmChannelTXFLine> ommChannelTXFLineList = this.findPageLine(sysSno, iSPage, iPSize);
		    
		    if(ommChannelTXFLineList != null && ommChannelTXFLineList.size() != 0) {
		    	// 取得第一筆的INDEX
				Long firstIndex = ommChannelTXFLineList.get(0).getColumnIndex();
				// 取得最後一筆 INDEX
				Long maxIndex = this.findPageLineMaxIndex(sysSno);
				
				log.info("firstIndex="+firstIndex+", maxIndex="+maxIndex);
		    	
				result.add(AjaxUtils.getAJAXPageData(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, ommChannelTXFLineList, gridDatas,
						firstIndex, maxIndex));	
		    }else{
		    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest, GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, gridDatas));   
		    }
		    
		    return result;
		}catch(Exception ex){
			ex.printStackTrace();
		    log.error("載入頁面顯示的通道明細明細發生錯誤，原因：" + ex.toString());
		    throw new Exception("載入頁面顯示的通道明細失敗！");
		}	
	}
	
	public Map executeInitial(Map parameterMap) throws Exception {
		log.info("executeInitial...");
		
		HashMap resultMap = new HashMap();
		HashMap multiList = new HashMap();
		OmmChannelTXFHead ommChannelTXFHead = null;
		try {
			
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String sysSnoString = (String) PropertyUtils.getProperty(otherBean, "sysSno");
			String channelType = (String) PropertyUtils.getProperty(otherBean, "channelType");
			String categoryType = (String) PropertyUtils.getProperty(otherBean, "categoryType");
			
			if(StringUtils.hasText(sysSnoString)) {
				Long sysSno = NumberUtils.getLong(sysSnoString);
				ommChannelTXFHead = this.getActualOmmChannelTXFHead(sysSno);
			}else {
				ommChannelTXFHead = createNewOmmChannelTXFHead(parameterMap);
				if( !"".equals(channelType) && !"".equals(categoryType)) {
					ommChannelTXFHead.setSysSign("");
					ommChannelTXFHead.setOmmChannelTXFLineList(new ArrayList<OmmChannelTXFLine>());
					DbcInformationTables dbcInformationTables = dbcInformationTablesService.findDbcInformationTablesByIdentification("IM_ITEM");
					OmmChannel ommChannel = ommChannelService.getActualOmmChannel(categoryType, channelType , true);
					this.copyDbcInformationTablesToOmmChannelTXFHead(dbcInformationTables, ommChannelTXFHead);
					this.copyOmmChannelConfigListToOmmChannelTXFLineList(ommChannel, ommChannelTXFHead);
				}
				ommChannelTXFHeadDAO.save(ommChannelTXFHead);
			}
			resultMap.put("form", ommChannelTXFHead);
			resultMap.put("statusName", OrderStatus.getChineseWord(ommChannelTXFHead.getStatus()));
			resultMap.put("sysModifierAmailName", UserUtils.getUsernameByEmployeeCode(loginEmployeeCode));
			
			ommChannelTXFHeadDAO.update(ommChannelTXFHead);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("通道建立作業初始化失敗，原因：" + ex.toString());
			
			throw new Exception("通道建立作業初始化失敗，原因：" + ex.toString());
		}
		return resultMap;
	}
	
	
	
	public OmmChannelTXFHead createNewOmmChannelTXFHead(Map parameterMap) throws Exception {
		
		OmmChannelTXFHead ommChannelTXFHead = new OmmChannelTXFHead();
		
		Object otherBean = parameterMap.get("vatBeanOther");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
		String channelType = (String) PropertyUtils.getProperty(otherBean, "channelType");
		String categoryType = (String) PropertyUtils.getProperty(otherBean, "categoryType");
		
		ommChannelTXFHead.setOrderNo(AjaxUtils.getTmpOrderNo());
		ommChannelTXFHead.setEnable("N");
		ommChannelTXFHead.setSysLastUpdateTime(new Date());
		ommChannelTXFHead.setSysModifierAmail(loginEmployeeCode);
		ommChannelTXFHead.setOrderTypeCode("CCT");
		ommChannelTXFHead.setStatus(OrderStatus.SAVE);
		ommChannelTXFHead.setCategoryType(categoryType);
		ommChannelTXFHead.setChannelType(channelType);
		ommChannelTXFHead.setSysSign("N");
		ommChannelTXFHead.setOmmChannelTXFLineList(new ArrayList<OmmChannelTXFLine>());
		
		return ommChannelTXFHead;
	}
	
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest) throws Exception{
		log.info("updateAJAXPageLinesData...test");
		
		try {
			String sysSnoStr = httpRequest.getProperty("sysSno");
			Long sysSno = NumberUtils.getLong(sysSnoStr);
			
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
			
			if(sysSno == null) {
				throw new ValidationErrorException("傳入的通道異動單主鍵為空值");
			}
			
			OmmChannelTXFHead ommChannelTXFHead = this.getActualOmmChannelTXFHead(sysSno);
			List<OmmChannelTXFLine> ommChannelTXFLineList = ommChannelTXFHead.getOmmChannelTXFLineList();
			OmmChannelTXFLine ommChannelTXFLine = null;
			
			// 將String資料轉成List Properties record data
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount,
					GRID_FIELD_NAMES);
			
			if(upRecords != null) {
				
				int columnIndex;
				
				log.info("upRecords.size()="+upRecords.size());
				
				for(Properties upRecord:upRecords) {
					log.info("updateAJAXPageLinesData"
							+ ": columnIndex = " + upRecord.getProperty("columnIndex")
							+ ", columnCode = " + upRecord.getProperty("columnCode") 
							+ ", columnName = " + upRecord.getProperty("columnName")
							+ ", enable = " + upRecord.getProperty("enable"));
					
					
					if (!"".equals(upRecord.getProperty("columnName"))) {
						columnIndex = NumberUtils.getInt(upRecord.getProperty("columnIndex"));
						
						ommChannelTXFLine = ommChannelTXFLineList.get(columnIndex-1);
						
						log.info(ommChannelTXFLine.getColumnCode()+ ", " + ommChannelTXFLine.getColumnName());
						if(upRecord.getProperty("columnCode").equals(ommChannelTXFLine.getColumnCode()) 
								&& upRecord.getProperty("columnName").equals(ommChannelTXFLine.getColumnName())) {
							log.info("相同");
							AjaxUtils.setPojoProperties(ommChannelTXFLine,upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
							ommChannelTXFLineDAO.update(ommChannelTXFLine);
						}else {
							log.info("不相同");
						}
					}
					
				}
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		String errorMsg = null;
		
		
		
		return AjaxUtils.getResponseMsg(errorMsg);
	}
	
	
	/**
	 * 
	 * 將前端資料封裝於parameterMap的entityBean中
	 * 
	 * @param parameterMap
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public HashMap updateOmmChannelTXFHeadBean(Map parameterMap) throws FormException, Exception {
		HashMap resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			// 取得欲更新的bean
			OmmChannelTXFHead ommChannelTXFHead = this.getActualOmmChannelTXFHead(formBindBean);
			AjaxUtils.copyJSONBeantoPojoBean(formBindBean, ommChannelTXFHead);
			resultMap.put("entityBean", ommChannelTXFHead);
			return resultMap;
		} catch (FormException fe) {
			log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			throw new Exception("通道異動單資料塞入bean發生錯誤，原因：" + ex.getMessage());
		}
    }
	/**
	 * 
	 * 透過sysSno(PK)查出OmmChannelTXFHead(異動單頭)
	 * 
	 * @param formBindBean
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public OmmChannelTXFHead getActualOmmChannelTXFHead(Object formBindBean) throws FormException, Exception {

		OmmChannelTXFHead ommChannelTXFHead = null;
		String sysSnoString = (String) PropertyUtils.getProperty(formBindBean, "sysSno");

		if (StringUtils.hasText(sysSnoString)) {
			Long sysSno = NumberUtils.getLong(sysSnoString);
			ommChannelTXFHead = this.getActualOmmChannelTXFHead(sysSno);
		} else {
			throw new ValidationErrorException("傳入的通道異動單主鍵為空值！");
		}
		return ommChannelTXFHead;

	}
	
	
	
	/**
	 * 透過PK取得一筆OmmChannelTXFHead
	 * @param sysSno
	 * @return
	 * @throws FormException
	 * @throws Exception
	 */
	public OmmChannelTXFHead getActualOmmChannelTXFHead(Long sysSno) throws FormException, Exception {

		OmmChannelTXFHead ommChannelTXFHead = null;
		ommChannelTXFHead = (OmmChannelTXFHead) ommChannelTXFHeadDAO.findByPrimaryKey(OmmChannelTXFHead.class, sysSno);
		if (ommChannelTXFHead == null) {
			throw new NoSuchObjectException("查無通道異動單主鍵：" + sysSno + "的資料！");
		}
		
		return ommChannelTXFHead;

	}
	
	/**
	 * 
	 * 執行異動存檔,變更單號、狀態、最後異動時間
	 * 
	 * @param parameterMap
	 */
	public void executeTransaction(Map parameterMap) throws FormException, Exception {
		HashMap resultMap = null;
		OmmChannelTXFHead ommChannelTXFHead = null;
		OmmChannel ommChannel = null;
		
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginEmployeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");

			resultMap = (HashMap) parameterMap.get("resultMap");
			String formAfterStatus = (String) PropertyUtils.getProperty(resultMap,"formAfterStatus");
			
			ommChannelTXFHead = (OmmChannelTXFHead) resultMap.get("entityBean");
			
			
			ommChannelTXFHead.setStatus(formAfterStatus);
			ommChannelTXFHead.setSysModifierAmail(loginEmployeeCode);
			ommChannelTXFHead.setSysLastUpdateTime(new Date());
			this.setOrderNo(ommChannelTXFHead);
			
			ommChannelTXFHeadDAO.update(ommChannelTXFHead);
			
			if(formAfterStatus.equals(OrderStatus.FINISH)) {
				ommChannel = ommChannelService.getActualOmmChannel(ommChannelTXFHead.getCategoryType(), ommChannelTXFHead.getChannelType(), false);
				if(ommChannel == null) {
					log.info("是null");
					ommChannel = new OmmChannel();
					ommChannel.setOmmChannelConfigList(new ArrayList<OmmChannelConfig>());
				}
				this.copyOmmChannelTXFHeadToOmmChannel(ommChannelTXFHead, ommChannel);
				this.copyOmmChannelTXFLineToOmmChannelConfig(ommChannelTXFHead, ommChannel);
				
				ommChannelDAO.save(ommChannel);
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("執行異動存檔失敗，原因：" + ex.toString());
			throw new Exception("執行異動存檔失敗，原因：" + ex.toString());
		}	
	}
	
	/**
	 * 將ommChannelTXFHead屬性複製進ommChannel
	 * @param ommChannelTXFHead
	 * @param ommChannel
	 */
	public void copyOmmChannelTXFHeadToOmmChannel(OmmChannelTXFHead ommChannelTXFHead, OmmChannel ommChannel) {
		log.info("copyOmmChannelTXFHeadToOmmChannel...");
		ommChannel.setCategoryType(ommChannelTXFHead.getCategoryType());
		ommChannel.setChannelType(ommChannelTXFHead.getChannelType());
		ommChannel.setEnable(ommChannelTXFHead.getEnable());
	}
	
	/**
	 * 將OmmChannelTXFLine屬性複製進OmmChannelConfig
	 * @param ommChannelTXFHead
	 * @param ommChannel
	 */
	public void copyOmmChannelTXFLineToOmmChannelConfig(OmmChannelTXFHead ommChannelTXFHead, OmmChannel ommChannel) {
		log.info("copyOmmChannelTXFLineToOmmChannelConfig...");
		List<OmmChannelTXFLine> ommChannelTXFLineList = ommChannelTXFHead.getOmmChannelTXFLineList();
		List<OmmChannelConfig> ommChannelConfigList = ommChannel.getOmmChannelConfigList();
		
		log.info("ommChannelTXFLineList.size()" + ommChannelTXFLineList.size());
		log.info("ommChannelConfigList.size()" + ommChannelConfigList.size());
		
		OmmChannelTXFLine ommChannelTXFLine = null;
		OmmChannelConfig ommChannelConfig = null;
		
		
		int mainCount = 0; //主檔計數
		int txfCount = 0; //異動檔計數
		int maxMainIndex = ommChannelConfigList.size() - 1; //主檔最大index
		
		for( txfCount = 0 ; txfCount < ommChannelTXFLineList.size(); ) {
			log.info("mainCount = " + mainCount + ", txfCount = " +txfCount + ", maxMainIndex = " + maxMainIndex);
			
			ommChannelTXFLine = ommChannelTXFLineList.get(txfCount);
			if( mainCount > maxMainIndex || maxMainIndex == 0 ) {
				ommChannelConfig = new OmmChannelConfig();
			}else {
				ommChannelConfig = ommChannelConfigList.get(mainCount);
			}
			
			log.info(ommChannelTXFLine.getColumnCode() + " " +ommChannelConfig.getColumnCode() + " " + ommChannelTXFLine.getColumnName() + " " + ommChannelConfig.getColumnName());
			
			if( ommChannelTXFLine.getColumnCode().equals(ommChannelConfig.getColumnCode()) && ommChannelTXFLine.getColumnName().equals(ommChannelConfig.getColumnName())) {
				log.info("update");
				ommChannelConfig.setOmmChannel(ommChannel);
				ommChannelConfig.setColumnCode(ommChannelTXFLine.getColumnCode());
				ommChannelConfig.setColumnName(ommChannelTXFLine.getColumnName());
				ommChannelConfig.setColumnIndex((long) (mainCount+1));
				ommChannelConfig.setEnable(ommChannelTXFLine.getEnable());
				ommChannelConfigList.add(ommChannelConfig);
				ommChannelConfigDAO.update(ommChannelConfig);
				
				mainCount++;
				txfCount++;
				
			}else {
				log.info("insert");
				ommChannelConfig.setOmmChannel(ommChannel);
				ommChannelConfig.setColumnCode(ommChannelTXFLine.getColumnCode());
				ommChannelConfig.setColumnName(ommChannelTXFLine.getColumnName());
				ommChannelConfig.setColumnIndex((long) (mainCount+1));
				ommChannelConfig.setEnable(ommChannelTXFLine.getEnable());
				ommChannelConfigList.add(ommChannelConfig);
				ommChannelConfigDAO.save(ommChannelConfig);
				
				mainCount++;
				txfCount++;
			}
			
			
			
			
		}
	}
	
	/**
	 * 若是暫存單號,則取得新單號
	 *
	 * @param head
	 */
	private void setOrderNo(OmmChannelTXFHead head) throws ObtainSerialNoFailedException {
		String orderNo = head.getOrderNo();
		log.info("setOrderNo...original_order=" + orderNo);
		if (AjaxUtils.isTmpOrderNo(orderNo)) {
			try {
				String serialNo = buOrderTypeService.getOrderSerialNo("T2", head.getOrderTypeCode());
				if ("unknow".equals(serialNo))
					throw new ObtainSerialNoFailedException("取得T2-" + head.getOrderTypeCode()
							+ "單號失敗！");
				else {
					head.setOrderNo(serialNo);
					log.info("the order no. is " + serialNo);
				}
			} catch (Exception ex) {
				throw new ObtainSerialNoFailedException("取得" + head.getOrderTypeCode() + "單號失敗！");
			}
		}
	}
	
	public List<Properties> updateDetailByCondition(Properties httpRequest) {
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		
		try {
			
			String sysSnoString  = httpRequest.getProperty("sysSno");
			String channelType   = httpRequest.getProperty("channelType");
			String categoryType  = httpRequest.getProperty("categoryType");
			Long sysSno = NumberUtils.getLong(sysSnoString);
			
			//查詢通道主檔
			OmmChannel ommChannel = ommChannelService.getActualOmmChannel(categoryType, channelType, false);
			if(ommChannel != null) {
				properties.setProperty("errorMsg", "通道主檔已存在,無法重複建立,若欲修改內容請透過查詢頁面進行");	
			}
			
			//查詢欄位資訊主檔
			DbcInformationTables dbcInformationTables = dbcInformationTablesService.findDbcInformationTablesByIdentification("IM_ITEM");
			
			//查詢通道異動主檔
			OmmChannelTXFHead ommChannelTXFHead = this.getActualOmmChannelTXFHead(sysSno);
			properties.setProperty("channelTypeRes", ommChannelTXFHead.getChannelType()==null?"":ommChannelTXFHead.getChannelType());
			properties.setProperty("categoryTypeRes", ommChannelTXFHead.getCategoryType()==null?"":ommChannelTXFHead.getCategoryType());
			
			//刪除通道異動明細的舊資料
			log.info("1-1="+ommChannelTXFHead.getOmmChannelTXFLineList().size());
			List<OmmChannelTXFLine> oldOmmChannelTXFLineList = ommChannelTXFHead.getOmmChannelTXFLineList();
			for(int i = oldOmmChannelTXFLineList.size() - 1; i >= 0; i--){
				OmmChannelTXFLine ommChannelTXFLine = oldOmmChannelTXFLineList.get(i);
				ommChannelTXFLine.setOmmChannelTXFHead(null);
				oldOmmChannelTXFLineList.remove(ommChannelTXFLine);
				ommChannelTXFLineDAO.delete(ommChannelTXFLine);
			}
			log.info("1-2="+ommChannelTXFHead.getOmmChannelTXFLineList().size());
			
			//將主檔明細存入異動檔明細
			if( !"".equals(categoryType) && !"".equals(channelType) ) {
				this.copyDbcInformationTablesToOmmChannelTXFHead(dbcInformationTables, ommChannelTXFHead);
				ommChannelTXFHead.setCategoryType(categoryType);
				ommChannelTXFHead.setChannelType(channelType);			
			}
			
			ommChannelTXFHeadDAO.update(ommChannelTXFHead);
		} catch (Exception e) {
			e.printStackTrace();
			properties.setProperty("errorMsg", e.toString());
		} finally {
			result.add(properties);
		}
		
		return result;
	}
	
	/**
	 * 
	 * 將父主檔明細(啟用欄位)存入異動檔明細
	 * 
	 * @param ommChannel
	 * @param ommChannelTXFHead
	 */
	public void copyDbcInformationTablesToOmmChannelTXFHead(DbcInformationTables dbcInformationTables, OmmChannelTXFHead ommChannelTXFHead) throws Exception{
		List<DbcInformationColumns> dbcInformationColumnsList = dbcInformationTables.getDbcInformationColumnsList();
		List<OmmChannelTXFLine> ommChannelTXFLineList = ommChannelTXFHead.getOmmChannelTXFLineList();
		OmmChannelTXFLine ommChannelTXFLine = null;
		long indexCount = 0;
		for(DbcInformationColumns dbcInformationColumns:dbcInformationColumnsList) {
			// 僅把父主檔啟用的資料存入
			if( "Y".equals(dbcInformationColumns.getEnable())) {
				ommChannelTXFLine = new OmmChannelTXFLine();
				ommChannelTXFLine.setOmmChannelTXFHead(ommChannelTXFHead);
				ommChannelTXFLine.setColumnCode(dbcInformationColumns.getColumnName());
				ommChannelTXFLine.setColumnName(dbcInformationColumns.getColumnComments());
				ommChannelTXFLine.setColumnIndex(++indexCount);
				ommChannelTXFLine.setEnable(dbcInformationColumns.getEnable());
				ommChannelTXFLineList.add(ommChannelTXFLine);
				ommChannelTXFLineDAO.save(ommChannelTXFLine);
			}		
		}
	}

	public List<OmmChannelTXFLine> findPageLine(Long pSysSno, int iSPage, int iPSize){
		return ommChannelTXFLineDAO.findPageLine(pSysSno, iSPage, iPSize);
	}
	
	public Long findPageLineMaxIndex(Long sysSno) {
		return ommChannelTXFLineDAO.findPageLineMaxIndex(sysSno);
	}
	
	/**
	 * 
	 * 更新單頭資料
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> refreshTXFHeadData(Properties httpRequest) {
		log.info("refreshTXFHeadData...");
		OmmChannelTXFHead ommChannelTXFHead = null;
		List<Properties> result = new ArrayList();
		Properties properties = new Properties();
		try {
			String sysSnoString  = httpRequest.getProperty("sysSno");
			Long sysSno = NumberUtils.getLong(sysSnoString);
			ommChannelTXFHead = this.getActualOmmChannelTXFHead(sysSno);
			
			properties.setProperty("formOrderNo", ommChannelTXFHead.getOrderNo());
			properties.setProperty("formStatus", ommChannelTXFHead.getStatus());
			properties.setProperty("formStatusName", OrderStatus.getChineseWord(ommChannelTXFHead.getStatus()));
	    	result.add(properties);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	/**
	 * 把主檔身不啟用的欄位回寫到異動單身
	 * @param ommChannel
	 * @param ommChannelTXFHead
	 */
	public void copyOmmChannelConfigListToOmmChannelTXFLineList(OmmChannel ommChannel, OmmChannelTXFHead ommChannelTXFHead) {
		log.info("copyOmmChannelConfigListToOmmChannelTXFLineList...");
		List<OmmChannelConfig> ommChannelConfigList = ommChannel.getOmmChannelConfigList();
		List<OmmChannelTXFLine> ommChannelTXFLineList = ommChannelTXFHead.getOmmChannelTXFLineList();
		
		int mainCount = 0;
		int txfCount = 0;
		int maxMainCount = ommChannelConfigList.size() - 1;
		
		OmmChannelConfig ommChannelConfig = null;
		OmmChannelTXFLine ommChannelTXFLine = null;
		
		for( txfCount = 0 ;  txfCount < ommChannelTXFLineList.size() ; ) {
			
			ommChannelTXFLine = ommChannelTXFLineList.get(txfCount);
			ommChannelConfig = ommChannelConfigList.get(mainCount);
			
			if( ommChannelTXFLine.getColumnCode().equals(ommChannelConfig.getColumnCode()) 
					&& ommChannelTXFLine.getColumnName().equals(ommChannelConfig.getColumnName()) ) {
				log.info("回寫啟用狀態");
				ommChannelTXFLine.setEnable(ommChannelConfig.getEnable());
				mainCount++;
				txfCount++;
			}else {
				txfCount++;
			}
			
		}
		
	}
	

}
