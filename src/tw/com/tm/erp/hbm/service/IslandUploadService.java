package tw.com.tm.erp.hbm.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import loadCustomsConfig.InitialCustomsConfig;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import tw.com.tm.erp.hbm.bean.*;
import tw.com.tm.erp.hbm.dao.CmMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ErpDAO;
import tw.com.tm.erp.hbm.dao.ImAdjustmentHeadDAO;
import tw.com.tm.erp.hbm.dao.ImMovementHeadDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;

public class IslandUploadService {
	
    private ImWarehouseDAO imWarehouseDAO;
    private ImAdjustmentHeadDAO imAdjustmentHeadDAO;
    private CmMovementHeadDAO cmMovementHeadDAO;
    private ImMovementHeadDAO imMovementHeadDAO;
    private SoSalesOrderHeadDAO soSalesOrderHeadDAO;
    private ErpDAO erpDAO;
    private ImItemDAO imItemDAO;
    
    public void setImItemDAO(ImItemDAO imItemDAO) {
    	this.imItemDAO = imItemDAO;
    }
    
    public void setErpDAO(ErpDAO erpDAO) {
    	this.erpDAO = erpDAO;
    }
    
    public void setSoSalesOrderHeadDAO(SoSalesOrderHeadDAO soSalesOrderHeadDAO) {
    	this.soSalesOrderHeadDAO = soSalesOrderHeadDAO;
    }
    
    public void setImMovementHeadDAO(ImMovementHeadDAO imMovementHeadDAO) {
    	this.imMovementHeadDAO = imMovementHeadDAO;
    }
    public void setCmMovementHeadDAO(CmMovementHeadDAO cmMovementHeadDAO) {
    	this.cmMovementHeadDAO = cmMovementHeadDAO;
    }
    
    public void setImAdjustmentHeadDAO(ImAdjustmentHeadDAO imAdjustmentHeadDAO) {
    	this.imAdjustmentHeadDAO = imAdjustmentHeadDAO;
    }
    public void setImWarehouseDAO(ImWarehouseDAO imWarehouseDAO) {
    	this.imWarehouseDAO = imWarehouseDAO;
    }
    
	
	
	public List<CustomsConfiguration> loadConfig(String DF,String orderTypeCode) throws Exception{
		try{
			List<CustomsConfiguration> configs = InitialCustomsConfig.getConfigs(DF,orderTypeCode);
			System.out.println("Load "+DF+"config success!");
			return configs;
		}catch(Exception e){
			throw new Exception("error:IslandUpload.class&loadConfig(),msg: Load "+DF+"config fault!");
		}
	}
	/****
	 * 
	 * 
	 * @param DFconfig  DF??'s Config
	 * @param outputType SQL/XML
	 * @param dataList SourceDataList
	 * @return
	 * @throws Exception 
	 */
	public Map dataBind(List<CustomsConfiguration> DFconfigs,String outputType,List dataList) throws Exception{
		Map returnMap = new HashMap();
		String destinationFunction = DFconfigs.get(0).getDestinationFunction();
		
			if(outputType.equals("XML")){
				//每張單據<datalist>
				for(Object obj:dataList){
					//XML's root
					Document doc = DocumentHelper.createDocument();
					Element request = null;
					request = doc.addElement(destinationFunction.toLowerCase()+":Set"+destinationFunction.toUpperCase()+"Request");
		        	request.addNamespace(destinationFunction.toLowerCase(), "urn:CUSTOMS:"+destinationFunction.toUpperCase()+":1.0");
		        	Map beanMap = bindBean(DFconfigs); //config的table
		        	Map beanClassMap = bindClassBean(destinationFunction); //程式設定檔的table map
		        	int level = 1;
		        	Class nowTbc = obj.getClass();
		        	Object nowBean = nowTbc.newInstance();
		        	nowBean = obj;
		        	Element nowE = null;
		        	nowE = request;
		        	Element newE = null;
		        	String nowLevel = DFconfigs.get(0).getReserve3();
		        	Integer totals = 0;
		        	for(CustomsConfiguration config:DFconfigs){
		        		String ds = config.getDestinationFunction();
		        		String targetField = config.getTargetField();
		        		String sourceField = config.getSourceField();
		        		String sourceTable = config.getSourceTable();
		        		String targetLevel = config.getReserve3();
		        		String mO = config.getReserve4();
		        		
		        		
		        		if(null != sourceTable && sourceTable.toUpperCase().indexOf(beanMap.get(level).toString())>-1){
		        			if((null != ds && ds.indexOf("09")==-1 )
		        					|| (null != ds && ds.indexOf("09")>-1 && !targetLevel.equals("T2"))){
			        			if(!targetField.toUpperCase().equals("SENDSTORECODE")){	
				        			Class tb = nowTbc;  //單頭
				        			String s = getDataBySourceField(config,sourceField,obj,tb); //取單頭第一階層的值
				        			if(null!= mO && mO.toUpperCase().equals("M") && (null == s || s.equals("") || s.equals("null") )){
				        				throw new Exception("必填欄位:"+sourceField+" 為空值!");
				        			}
				        			if(null != sourceField && sourceField.toUpperCase().indexOf("TOTALS")>-1){
				        				totals = Integer.parseInt(s);
				        			}
				        			if(s != null && !s.equals("null")){
				        				if(nowLevel.equals(targetLevel)){
				        					newE = setDataElement(s,nowE,config);
				        				}else{
				        					nowE = newE;
				        					newE = setDataElement(s,nowE,config);
				        					nowLevel = targetLevel;
				        				}
				        			}	
			        			}
		        			}
		        		}else if(null != sourceTable & sourceTable.toUpperCase().equals("SO_DELIVERY_LINE")
		        				&&null != targetLevel && targetLevel.equals("T2")
		        				&& null != ds && ds.indexOf("08")>-1){
		        			int level1 = 2;
		        			List linesDataList = null;
		        			Method[] ms = nowBean.getClass().getMethods();
		        			String getLineMethodName = "GET"+beanMap.get(level1).toString().toUpperCase().replace("_","");
		        			
		        			for(Method m:ms){
				        		if(m.getName().toUpperCase().indexOf(getLineMethodName)>-1){
				        			linesDataList = (List) m.invoke(nowBean);
				        		}
				        	}
				        	if(null != linesDataList && linesDataList.size()>=0){
				        		 Class nowLineClass =  linesDataList.get(0).getClass();
				        		 if(sourceField.toUpperCase().equals("CUSTOMER_PO_NO")){
					        		 String s = getDataBySourceField(config,sourceField,linesDataList.get(0),nowLineClass);
					        		 if(nowLevel.equals(targetLevel)){
				        					newE = setDataElement(s,nowE,config);
				        				}else{
				        					nowE = newE;
				        					newE = setDataElement(s,nowE,config);
				        					nowLevel = targetLevel;
				        				}
				        		 }
				        	}
		        		}
		        		
		        	}
		        	
		        	//getLinedata
		        	level = 2;
		        	List linesDataList = null;
		        	String LineId = "";
		        	String orderTypeCode = "";
		        	String orderNo = "";
		        	Method[] ms = nowBean.getClass().getMethods();
		        	if(null!=beanClassMap.get(level)){
		        	String getLineMethodName = "GET"+beanClassMap.get(level).toString().substring(beanClassMap.get(level).toString().indexOf("bean.")+5).toUpperCase().replace("_","");
		        	for(Method m:ms){
		        		if(m.getName().toUpperCase().indexOf(getLineMethodName)>-1){
		        			linesDataList = (List) m.invoke(nowBean);
		        		}
		        	}
		        	Integer linesTotals = 0;
		        	if(null != linesDataList && linesDataList.size()>=0){
		        		String nowLineClass =  linesDataList.get(0).getClass().toString();
			        	ImMovementHead mH = new ImMovementHead();
			        	Integer linesNo = 0;
			        	for(Object objLine:linesDataList){
			        		level = 2;
			        		linesNo ++;
			        		Element newE2 = null,newE2_ = null ;
			        		//須轉CLASS(DF10)
				        	String nowLineClassName = nowLineClass.substring(nowLineClass.indexOf("bean.")+5);
				        	if(!beanMap.get(level).toString().toUpperCase().replace("_","").equals(nowLineClassName.toUpperCase())){//||linesDataList.size()>=1
				        		nowTbc = objLine.getClass();
					        	nowBean = nowTbc.newInstance();
					        	nowBean = objLine;
					        	ms = nowBean.getClass().getMethods();
					        	for(Method m:ms){
					        		if(m.getName().toUpperCase().indexOf("GET")>-1 && m.getName().toUpperCase().indexOf("LINEID")>-1){
					        			LineId = String.valueOf(m.invoke(nowBean)) ;
					        		}
					        		if(m.getName().toUpperCase().indexOf("GET")>-1 && m.getName().toUpperCase().indexOf("ORDERTYPECODE")>-1){
					        			orderTypeCode = String.valueOf(m.invoke(nowBean)) ;
					        		}
					        		if(m.getName().toUpperCase().indexOf("GET")>-1 && m.getName().toUpperCase().indexOf("ORDERNO")>-1){
					        			orderNo = String.valueOf(m.invoke(nowBean)) ;
					        		}
					        	}
					        	mH = erpDAO.findByTbNOrderNo(beanMap.get(level).toString(),orderTypeCode,orderNo).get(0); 
					        	objLine = mH;
				        	}
				        	String LineLevel = DFconfigs.get(0).getReserve3();
				        	for(CustomsConfiguration config:DFconfigs){
				        		String ds = config.getDestinationFunction();
				        		String targetField = config.getTargetField();
				        		String sourceField = config.getSourceField();
				        		String sourceTable = config.getSourceTable();
				        		String targetLevel = config.getReserve3();
				        		String mO = config.getReserve4();
				        		String s = null;
				        		
				        		//第二筆明細之前,加入明細ROOT標籤				        		
				        		if(!linesNo.equals(1)){
				        			if(nowLevel.equals(targetLevel) & (sourceField == null || sourceField.equals("")) & ds.indexOf("12")==-1){
				        				Class tb = (Class) beanClassMap.get(level);
						        		s = getDataBySourceField(config,sourceField,objLine,tb);
						        		newE = setDataElement(s,nowE,config);
				        			}
				        		}
				        		if("null"!=beanMap.get(level)&&null != sourceTable && sourceTable.toUpperCase().indexOf(beanMap.get(level).toString())>-1){
				        			Class tb = (Class) beanClassMap.get(level);
				        			if(null != sourceField && sourceField.equals("CUSTOMER_PO_NO") && null != ds && ds.indexOf("08")>-1){
				        				continue;
				        			}else{
				        				s = getDataBySourceField(config,sourceField,objLine,tb);
				        				if(null!= mO && mO.toUpperCase().equals("M") ){//&& (null == s || s.equals(""))
				        					//System.out.println("s:"+s);
				        					if(null == s || s.equals("") || s.equals("null")){
				        						//System.out.println("s:null or empty");
				        						throw new Exception("必填欄位:"+sourceField+" 為空值!");
				        					}
					        				//
					        			}
				        				if(null != sourceField && sourceField.toUpperCase().indexOf("TOTALS")>-1){
				        					linesTotals = Integer.parseInt(s);
					        			}
				        			}
				        			if(s != null && !s.equals("null")){
				        				if(ds.indexOf("12")>-1 ){
				        					ImAdjustmentLine line = (ImAdjustmentLine) objLine;
				        					if(line.getBeAft().equals("2") && targetLevel.equals("T3")){//aft
				        						newE2 = setDataElement(s,newE,config);
						        				if(!LineLevel.equals(targetLevel)){
						        					LineLevel = targetLevel;
						        				}
				        					}else if(line.getBeAft().equals("1") && targetLevel.equals("T4") 
				        							& (sourceField == null || sourceField.equals(""))){//bef root
				        						newE2_ = setDataElement(s,nowE,config);
				        					}else if(line.getBeAft().equals("1") && targetLevel.equals("T4")){//bef
				        						newE2 = setDataElement(s,newE2_,config);
						        				if(!LineLevel.equals(targetLevel)){
						        					LineLevel = targetLevel;
						        				}
				        					}
				        				}else{
				        					newE2 = setDataElement(s,newE,config);
					        				if(!LineLevel.equals(targetLevel)){
					        					LineLevel = targetLevel;
					        				}
				        				}
				        			}
				        		}else{
				        			if(targetLevel.equals("T2") && ds.indexOf("09")>-1){
				        				int lineToheadLevel = level;
					        			Class tb = (Class) beanClassMap.get(lineToheadLevel--);
					        			s = getDataBySourceField(config,sourceField,obj,tb);
					        			if(null!= mO && mO.toUpperCase().equals("M") && (null == s || s.equals("") || s.equals("null") )){
					        				throw new Exception("必填欄位:"+sourceField+" 為空值!");
					        			}
					        			if(s != null && !s.equals("null")){
					        				newE2 = setDataElement(s,newE,config);
					        				if(!LineLevel.equals(targetLevel)){
					        					LineLevel = targetLevel;
					        				}
					        			}
				        			}else if(targetField.toUpperCase().equals("SENDSTORECODE")){
				        				int level1 = level--;
				        				Class tb2 = (Class) beanClassMap.get(level1);
					        			s = getDataBySourceField(config,sourceField,obj,tb2);
					        			if(s != null && !s.equals("null")){
					        				newE2 = setDataElement(s,newE,config);
					        				if(!LineLevel.equals(targetLevel)){
					        					LineLevel = targetLevel;
					        				}
					        			}
				        			}
				        		}
				        	}
				        	
				        	//LinesLine 
				        	level = 3;
				        	List linesLineDataList = null;
				        	nowTbc = objLine.getClass();
				        	nowBean = nowTbc.newInstance();
				        	nowLineClassName = nowLineClass.substring(nowLineClass.indexOf("bean.")+5);
				        	if(null!=beanMap.get(level) && (!beanMap.get(level).equals(nowLineClassName))) nowBean = mH;
				        	ms = objLine.getClass().getMethods();
				        	for(Method m:ms){
				        		if(m.getName().toUpperCase().indexOf("GET")>-1 && 
				        				(m.getName().toUpperCase().indexOf("LINES")>-1 || m.getName().toUpperCase().indexOf("ITEMS")>-1)){
				        			linesLineDataList = (List) m.invoke(nowBean);
				        		}
				        	}
				        	
				        	if(null != linesLineDataList && linesLineDataList.size()>=0){
				        		nowLineClass =  linesLineDataList.get(0).getClass().toString();
				        		Integer lineslineNo = 0;
					        	for(Object linsLineObj:linesLineDataList){
					        		lineslineNo ++;
					        		for(CustomsConfiguration config:DFconfigs){
						        		String targetField = config.getTargetField();
						        		String sourceField = config.getSourceField();
						        		String sourceTable = config.getSourceTable();
						        		String targetLevel = config.getReserve3();
						        		String mO = config.getReserve4();
						        		String s = null;
						        		String plug = null;
						        		
						        		//第二筆明細之前,加入明細ROOT標籤				        		
						        		if(!lineslineNo.equals(1)){
						        			if(LineLevel.equals(targetLevel) & (sourceField == null || sourceField.equals(""))){
						        				Class tb = (Class) beanClassMap.get(level);
						        				s = getDataBySourceField(config,sourceField,objLine,tb);
						        				newE2 = setDataElement(s,newE,config);
						        			}
						        		}
						        		
						        		if(null != sourceTable && sourceTable.toUpperCase().indexOf(beanMap.get(level).toString())>-1){
						        			//String className = beanClassMap.get(level);
						        			Class tb = (Class) beanClassMap.get(level);
						        			if(sourceTable.equals("IM_MOVEMENT_ITEM")&sourceField.equals("ORIGINAL_DECLARATION_DATE")){
						        				s = getDataBySourceField(config,sourceField,linsLineObj,tb);
						        				plug = "Y";
						        			}else{
						        				s = getDataBySourceField(config,sourceField,linsLineObj,tb);
						        				plug = "N";
						        			}
						        			if(null!= mO && mO.toUpperCase().equals("M") && (null == s || s.equals("") || s.equals("null") )){
						        				throw new Exception("必填欄位:"+sourceField+" 為空值!");
						        			}
						        			if(s != null && !s.equals("null")){
						        					setDataElement(s,newE2,config);
						        			}
						        		}	        		
						        	}
					        	}
					        	
					        	if(!lineslineNo.equals(linesTotals)){
					        		System.out.println("lineslineNo:"+lineslineNo);
					        		System.out.println("linesTotals:"+linesTotals);
					        		throw new Exception("統計筆數與明細筆數不相同");
					        	}
				        	}else{
				        		//System.out.println("取無明細檔的明細資料方法或無明細內容!");
				        	}
				        }
			        	if(!linesNo.equals(totals) && !destinationFunction.equals("NF09") && !destinationFunction.equals("NF12") && !destinationFunction.equals("NF14")
			        			&& !destinationFunction.equals("NF15")){
			        		System.out.println("linesNo:"+linesNo);
				        	System.out.println("totals:"+totals);
				        	throw new Exception("統計筆數與明細筆數不相同");
			        	}
		        	  }	
		        	}else{
		        		//System.out.println("取無明細資料方法或無明細內容!");
		        	}
					String s = doc.asXML();
					System.out.println("stringXml:"+s);
					returnMap.put("XML",s);
				}
			}else{
				throw new Exception("SORRY!!只提供SQL/XML的output");
			}
		
		
		return returnMap;
	}
	
	private Element setDataElement(String s,Element forwardElement,CustomsConfiguration config) throws Exception{
		Element e = null;

		if(s.indexOf("attr")>-1){
			forwardElement.addAttribute(config.getTargetField(), s.substring(s.indexOf(".")+1));
		}else{
			e = forwardElement.addElement(config.getDestinationFunction().toLowerCase()+":"+config.getTargetField());
			e.addText(s);
		}
		
		return e;
	}
	
	/****
	 * 資料來源分類
	 * @param DFconfig
	 * @param sourceOracleField
	 * @param dataList
	 * @return
	 * @throws Exception 
	 */
	private String getDataBySourceField(CustomsConfiguration DFconfig, String sourceOracleField,Object dataList,Class tb) throws Exception{
		if(null != sourceOracleField){
			if(sourceOracleField.indexOf("PROPERTIES")>-1){
				Properties config = new Properties();
				String CONFIG_FILE = "/customs_upload_tsmg.properties";
				config.load(IslandUploadService.class.getResourceAsStream(CONFIG_FILE));
				return config.getProperty(sourceOracleField.substring(sourceOracleField.indexOf(".")+1));
			}else if(sourceOracleField.indexOf("TOTALS")>-1){
				if(sourceOracleField.equals("TOTALS.SO_SALES_ORDER_HEAD")){
					SoSalesOrderHead soh = (SoSalesOrderHead) soSalesOrderHeadDAO.findById("SoSalesOrderHead",Long.valueOf(getDataByMethods(DFconfig,"HEAD_ID",dataList,tb)));
					Integer returnSize = soh.getSoSalesOrderItems().size();
					String retSize = returnSize.toString();
					return retSize;
				}else if(sourceOracleField.equals("TOTALS.CM_MOVEMENT_HEAD")){
					String retSize = "1";
					return retSize;							
				}else if(sourceOracleField.equals("TOTALS.CM_MOVEMENT_LINE")){
					//findbyHeadId
					CmMovementHead cmH = (CmMovementHead) cmMovementHeadDAO.findById("CmMovementHead",Long.valueOf(getDataByMethods(DFconfig,"HEAD_ID",dataList,tb)));
					Integer returnSize = cmH.getCmMovementLines().size();
					String retSize = returnSize.toString();
					return retSize;							
				}else if(sourceOracleField.equals("TOTALS.IM_MOVEMENT_ITEM")){
					//findbyHeadId
					ImMovementHead cmH = imMovementHeadDAO.findById(Long.valueOf(getDataByMethods(DFconfig,"HEAD_ID",dataList,tb)));
					Integer returnSize = cmH.getImMovementItems().size();
					String retSize = returnSize.toString();
					return retSize;							
				}else{
					String totalsTb = sourceOracleField.substring(sourceOracleField.indexOf(".")+1);
					String hL = "";
					if(totalsTb.toUpperCase().indexOf("HEAD")>-1){
						hL = "HEAD";
					}else if(totalsTb.toUpperCase().indexOf("LINE")>-1){
						hL = "LINE";
					}else if(totalsTb.toUpperCase().indexOf("ITEM")>-1){
						hL = "ITEM";
					}
					totalsTb = totalsTb.toLowerCase().replace("_", "");
					String middle = totalsTb.substring(3,totalsTb.indexOf(hL.toLowerCase()));
					if(middle.toLowerCase().indexOf("order")>-1) middle =middle.substring(0,middle.indexOf("order"))+ middle.substring(middle.toLowerCase().indexOf("order"),middle.toLowerCase().indexOf("order")+1).toUpperCase() + middle.substring(middle.toLowerCase().indexOf("order")+1);
					totalsTb = totalsTb.substring(0,1).toUpperCase() + totalsTb.substring(1,2) + totalsTb.substring(2,3).toUpperCase() + middle + hL.substring(0,1).toUpperCase() + hL.substring(1).toLowerCase();
					System.out.println("getDataByMethods(DFconfig,'HEAD_ID',dataList,tb):"+getDataByMethods(DFconfig,"HEAD_ID",dataList,tb));
					ImAdjustmentHead adj = imAdjustmentHeadDAO.findById(Long.valueOf(getDataByMethods(DFconfig,"HEAD_ID",dataList,tb)));// .findByBeanNId(totalsTb,Long.valueOf(getDataByMethods(DFconfig,"HEAD_ID",dataList,tb))); 
					List<ImAdjustmentLine> list = adj.getImAdjustmentLines();
					return String.valueOf(list.size());			
				}
			}else if(sourceOracleField.indexOf("STRING")>-1){
				String rt = sourceOracleField.substring(sourceOracleField.indexOf("'")+1,sourceOracleField.length()-1);
				if((DFconfig.getDestinationFunction().indexOf("08")>-1 || DFconfig.getDestinationFunction().indexOf("09")>-1) && !DFconfig.getReserve3().equals("T3")){
					String store = "";
					if(DFconfig.getDestinationFunction().indexOf("08")>-1){
						SoSalesOrderHead so = (SoSalesOrderHead) dataList;
						store = so.getShopCode();
					}else{
						SoDeliveryHead so = (SoDeliveryHead) dataList;
						store = so.getShopCode();
					}						
					ImWarehouse imWarehouse = imWarehouseDAO.findById(store);	
					if(imWarehouse.getCustomsWarehouseCode().equals("HD")){
						if(DFconfig.getTargetField().equals("PlaceCode")){
							rt = "H01";
						}else if(DFconfig.getTargetField().equals("StoreCode")){
							rt = "BDF06";
						}else if(DFconfig.getTargetField().equals("GetStoreCode")){
							rt = "PK20";
						}
					}
				}else if(DFconfig.getDestinationFunction().indexOf("08")>-1 & DFconfig.getReserve3().equals("T3")){
					
					if(DFconfig.getDestinationFunction().indexOf("08")>-1){
						SoSalesOrderItem si = (SoSalesOrderItem) dataList;
						
						
							ImItem item  = imItemDAO.findItem("T2", si.getItemCode());
							if(DFconfig.getTargetField().equals("GoodDesc")){
							  if(null!=item.getItemCName()){
								  rt = item.getItemCName();  
							  }else{
								  rt = item.getItemEName();
							  }
							}else if(DFconfig.getTargetField().equals("SaleUnit")){
							  if(null!=item.getPurchaseUnit()){
								  rt = item.getPurchaseUnit();  
							  }else{
								  rt = item.getPurchaseUnit();
							  }	
							}
						
					}
				}
				return rt;
			}else if(sourceOracleField.indexOf("SD")>-1){
				if(DFconfig.getTargetField().toUpperCase().equals("IOTYPE")){
					String arg = getDataByMethods(DFconfig,sourceOracleField.substring(sourceOracleField.indexOf(".")+1,sourceOracleField.length()),dataList,tb);
					if(arg.equals("null")||arg==""){
						return "E";
					}else{
						return "I";
					}
				}else if(DFconfig.getTargetField().toUpperCase().equals("DIRECTDEPARTURE")){
					String arg = getDataByMethods(DFconfig,sourceOracleField.substring(sourceOracleField.indexOf(".")+1,sourceOracleField.length()),dataList,tb);
					if(arg.equals("null")||arg==""){
						return "Y";
					}else{
						return null;
					}
				}
			}else {
				if(DFconfig.getTargetField().toUpperCase().equals("TRANSTYPE")){
					String attribute ="attr."+ getDataByMethods(DFconfig,sourceOracleField,dataList,tb);
					return attribute;
				}else{
					if(sourceOracleField.equals("QUANTITY")||sourceOracleField.equals("DELIVERY_QUANTITY")||sourceOracleField.equals("RETURN_QUANTITY")){
						String qty = getDataByMethods(DFconfig,sourceOracleField,dataList,tb);
						String[] qty_mod = qty.split("\\.");
						return qty_mod[0];
					}else{
						return getDataByMethods(DFconfig,sourceOracleField,dataList,tb);
					}
							
					
					
				}
			}
		}else{
			return "";
		}
		return "";
	}
	
	/***
	 * 取資料轉型
	 * @param DFconfig
	 * @param fieldName
	 * @param dataList
	 * @return
	 * @throws Exception 
	 */
	private String getDataByMethods(CustomsConfiguration DFconfig, String fieldName,Object dataList,Class c) throws Exception{
		
		String returnString = null;
		fieldName = fieldName.replace("_","");
		//get getter methodName
		fieldName = "get"+fieldName;
		//get Bean Class & Methods 
		Object o = c.newInstance();
		if(c.toString().substring(c.toString().indexOf("bean.")+5).equals("ImMovementHead")){
			ImMovementHead ImHead = new ImMovementHead();
			ImHead = (ImMovementHead) dataList;
			returnString = invoke(DFconfig, fieldName, ImHead, c);
		}else{
			o =  dataList;
			returnString = invoke(DFconfig, fieldName, dataList, c);
		}
		return returnString;
	}
	
	private String invoke(CustomsConfiguration DFconfig, String fieldName,Object dataList,Class c) throws Exception{
		String returnString = null;
		Object returnObj = null;
		Method[] ms = dataList.getClass().getMethods();
		for(Method m:ms){
			if(m.getName().toUpperCase().equals(fieldName.toUpperCase())){
				returnObj = m.invoke(dataList);
				//轉儲存字串
				String dtType = DFconfig.getCustomsType();
				String dtSize = DFconfig.getTargetSize();
				
				if(null != dtType && dtType.equals("DATE")){
					try{
						String format = DFconfig.getReserve1();
						SimpleDateFormat sdf = new SimpleDateFormat(format);
						Date d = (Date) returnObj;
	    				returnString = sdf.format(d);
					}catch(java.lang.IllegalArgumentException e){
						throw new Exception("設定檔日期格式錯誤");
					}
				}
				if(null!= dtSize && !dtSize.equals("")){
					returnString = (String) returnObj;
    				int length = Math.abs(Integer.parseInt(dtSize) - returnString.length());
    				for(int i=1;i<=length;i++){
    					if((Integer.parseInt(dtSize) - returnString.length())>0){
    						returnString = "0"+returnString;
    					}else if((Integer.parseInt(dtSize) - returnString.length())<0){
    						returnString = returnString.substring(0,returnString.length()-1);
    					}
    				}
    			}
				if(null == returnString){
    				try{
    					returnString = String.valueOf(returnObj) ;
    				}catch(Exception e){
    					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date d = (Date) returnObj;
	    				returnString = sdf.format(d);
    				}
    				
    			}
				
			}
		}
		return returnString;
	}
	
	
	private Map bindClassBean(String DF) throws Exception{
		Map returnMap = new HashMap();
		if(DF.substring(2).equals("08")){
			returnMap.put(1,SoSalesOrderHead.class);
			returnMap.put(2,SoSalesOrderItem.class);
		}else if(DF.substring(2).equals("09")){
			returnMap.put(1,SoDeliveryHead.class);
			returnMap.put(2,SoDeliveryLine.class);
		}else if(DF.substring(2).equals("10")){
			returnMap.put(1,CmMovementHead.class);
			returnMap.put(2,CmMovementLine.class);
			returnMap.put(3,ImMovementItem.class);
			return returnMap;
		}else if(DF.substring(2).equals("11")){
			returnMap.put(1,ImAdjustmentHead.class);
			returnMap.put(2,ImAdjustmentLine.class);
			return returnMap;
		}else if(DF.substring(2).equals("12")){
			returnMap.put(1,ImAdjustmentHead.class);
			returnMap.put(2,ImAdjustmentLine.class);
			return returnMap;
		}else if(DF.substring(2).equals("13")){
			returnMap.put(1,ImMovementHead.class);
			returnMap.put(2,ImMovementItem.class);
			return returnMap;
		}else if(DF.equals("DF15")){
			returnMap.put(1,SoDeliveryHead.class);
			returnMap.put(2,SoDeliveryLine.class);
		}else if(DF.equals("DF16")){
			returnMap.put(1,SoSalesOrderHead.class);
			returnMap.put(2,SoSalesOrderItem.class);
			return returnMap;
		}else if(DF.equals("NF14")){
			returnMap.put(1,ImMovementHead.class);
		}else if(DF.equals("NF15")){
			returnMap.put(1,ImDeliveryHead.class);
			returnMap.put(2,ImDeliveryLine.class);
			return returnMap;
		}
		return returnMap;
	}
	
	private Map bindBean(List<CustomsConfiguration> DFconfigs) throws Exception{
		Map returnMap = new HashMap();
		String nowTable = "";int i = 1;
		for(CustomsConfiguration config:DFconfigs){
			if(null != config.getSourceTable() && !nowTable.equals(config.getSourceTable())){
				boolean sourceTB = true;
				for(int j=1;j<returnMap.size();j++){
					if(returnMap.get(j).equals(config.getSourceTable())){
						sourceTB = false;
					}
				}
				if(sourceTB){
					returnMap.put(i,config.getSourceTable());
					nowTable = config.getSourceTable();
					i++;
				}
			}
		}
		return returnMap;
	}
	
}
