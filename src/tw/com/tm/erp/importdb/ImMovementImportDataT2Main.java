package tw.com.tm.erp.importdb;
  
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuLocation;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImItemEanPriceView;
import tw.com.tm.erp.hbm.bean.ImItemEancode;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuLocationDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImItemEancodeDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImWarehouseDAO;
import tw.com.tm.erp.hbm.service.ImItemEanPriceViewService;
import tw.com.tm.erp.hbm.service.ImItemService;
import tw.com.tm.erp.hbm.service.ImMovementService;
import tw.com.tm.erp.hbm.service.ImMovementMainService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.User;
//Steve
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.service.BuShopService;
import tw.com.tm.erp.hbm.dao.BuShopDAO;;
//Steve
/**
 * 調撥單匯入
 *
 * fix 20081210 line id 不匯入
 *
 * @author T02049
 *
 */

public class ImMovementImportDataT2Main implements ImportDataAbs {
	private static final Log log = LogFactory.getLog(ImMovementImportDataT2Main.class);
	public static final String split[] = { "{#}" };

	// private static final String ORGANIZATION_CODE = "TM";

	private static ApplicationContext context = SpringUtils.getApplicationContext();
  
	
	public ImportInfo initial(HashMap uiProperties) {
		log.info("ImMovementImportDataT2Main initial");
		ImportInfo imInfo = new ImportInfo();

		// set entity class name
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImMovementHead.class.getName());
		imInfo.setSplit(split);

		// set key info
		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("H");
		imInfo.setSaveKeyField(true);

		// set field name
		imInfo.addFieldName("h");
		imInfo.addFieldName("orderTypeCode");
		imInfo.addFieldName("orderNo");
		imInfo.addFieldName("deliveryDate");
		imInfo.addFieldName("packedBy");
		imInfo.addFieldName("deliveryWarehouseCode");
		imInfo.addFieldName("arrivalWarehouseCode");
		imInfo.addFieldName("originalOrderTypeCode");
		imInfo.addFieldName("originalOrderNo");
		imInfo.addFieldName("a");
		imInfo.addFieldName("b");
		imInfo.addFieldName("arrivalStoreCode");

		// set field type
		imInfo.setFieldType("h", "java.lang.String");
		imInfo.setFieldType("orderTypeCode", "java.lang.String");
		imInfo.setFieldType("orderNo", "java.lang.String");
		imInfo.setFieldType("deliveryDate", "java.util.Date");
		imInfo.setFieldType("packedBy", "java.lang.String");
		imInfo.setFieldType("deliveryWarehouseCode", "java.lang.String");
		imInfo.setFieldType("arrivalWarehouseCode", "java.lang.String");
		imInfo.setFieldType("originalOrderTypeCode", "java.lang.String");
		imInfo.setFieldType("originalOrderNo", "java.lang.String");
		imInfo.setFieldType("a", "java.lang.String");
		imInfo.setFieldType("b", "java.lang.String");
		imInfo.setFieldType("arrivalStoreCode", "java.lang.String");

		// set date format
		imInfo.setFieldTypeFormat("java.util.Date", "yyyyMMdd");

		// set default value
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		HashMap defaultValue = new HashMap();
		defaultValue.put("brandCode", user.getBrandCode());
		defaultValue.put("lastUpdateDate", new Date());
		defaultValue.put("lastUpdatedBy", user.getEmployeeCode());
		defaultValue.put("reserve2", uiProperties.get(ImportDBService.BATCH_NO));
		defaultValue.put("reserve3", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
		defaultValue.put("fileName", uiProperties.get(ImportDBService.UPLOAD_FILE_NAME));
		imInfo.setDefaultValue(defaultValue);

		// add detail
		imInfo.addDetailImportInfos(getImMovementItems());
		return imInfo;
	}

	/**
	 * im item price detail config
	 *
	 * @return
	 */
	private ImportInfo getImMovementItems() {
		log.info("getImMovementItems");
		ImportInfo imInfo = new ImportInfo();

		imInfo.setKeyIndex(0);
		imInfo.setKeyValue("D");
		imInfo.setSaveKeyField(true);
		imInfo.setEntityBeanClassName(tw.com.tm.erp.hbm.bean.ImMovementItem.class.getName());

		imInfo.addFieldName("d");
		imInfo.addFieldName("itemCode");
		imInfo.addFieldName("deliveryQuantity");
		imInfo.addFieldName("boxNo");
		imInfo.addFieldName("lotNo");

		imInfo.setFieldType("d", "java.lang.String");
		imInfo.setFieldType("itemCode", "java.lang.String");
		imInfo.setFieldType("deliveryQuantity", "java.lang.Double");
		imInfo.setFieldType("boxNo", "java.lang.String");
		imInfo.setFieldType("lotNo", "java.lang.String");

		return imInfo;
	}

	public String updateDB(List entityBeans, ImportInfo info) throws Exception {
		StringBuffer reMsg = new StringBuffer();
		//20160426 by jason 建單工號
		HashMap uiProperties = info.getUiProperties();
		User user = (User) uiProperties.get(SystemConfig.USER_SESSION_NAME);
		String employeeCode = user.getEmployeeCode();
		//
		log.info("ImMovementImportDataT2Main Main版本");
		try {
			//調撥單原進貨單單號
			String originalOrderTypeCode = null;
			String originalOrderNo = null;
          
			/*if(true){
				throw new Exception("錯誤啦!!");
			}*/
			//品項與單據對應陣列  by jason
			String[] wtf = {"J01","J02","J03","JT1","JT2","JT3","JE2","JE3","JS2","JS3","JS4","JS5","JS9",
								"JC1","JC2","JC3","JF1","JF2","JF3","JF4","JF5","JF9","JM1","JM2","JM3","JM4","JM5","JM9"
								,"JB2","JB3","JB4","JB5","JB9","JK2","JK3","JK4","JK5","JK9"};//wtf中不能出現
			String[] wtp = {"J01","J02","J03","JT1","JT2","JT3","JE2","JE3","JS2","JS3","JC1",
					"JC2","JC3","JF1","JF2","JF3","JM1","JM2","JM3","JB2","JB3",
					"JD2","JD3","JK2","JK3"};//wtp中不能出現
			String[] wff = {"J01","JT1","JC1"};//wff只能出現
			String[] wgf = {"J02","J03","JT2","JT3","JE2","JE3","JS2","JS3","JC2","JC3","JE2","JE3",
					"JM2","JM3","JB2","JB3","JD2","JD3","JK2","JK3"};//wgf中只能出現
			//------
		    List<ImMovementHead> imMovementHeads = new ArrayList(0);
		    ImMovementMainService imMovementMainService = (ImMovementMainService) context.getBean("imMovementMainService");
		    ImItemService imItemService = (ImItemService) context.getBean("imItemService");
		    ImItemEancodeDAO imItemEancodeDAO = (ImItemEancodeDAO) context.getBean("imItemEancodeDAO");
		    BuShopDAO buShopDAO = (BuShopDAO) context.getBean("buShopDAO");
		    BuEmployeeDAO buEmployeeDAO = (BuEmployeeDAO) context.getBean("buEmployeeDAO");
		    ImWarehouseDAO imWarehouseDAO = (ImWarehouseDAO)context.getBean("imWarehouseDAO");//by jason
		    ImItemDAO imItemDAO = (ImItemDAO)context.getBean("imItemDAO");//by jason
		    ImOnHandDAO imOnHandDAO = (ImOnHandDAO)context.getBean("imOnHandDAO");//by jason
		    for (int index = 0; index < entityBeans.size(); index++) {
		    	Boolean isSpec = false;
                ImMovementHead entityBean = (ImMovementHead) entityBeans.get(index);
                System.out.println("輸入店號 :"+entityBean.getArrivalStoreCode());
                String ordertype = entityBean.getOrderTypeCode();
                System.out.println("單別 :"+ordertype);
                //---庫別檢核
                String brandCode 		 = entityBean.getBrandCode();
                String deliveryWarehouse = entityBean.getDeliveryWarehouseCode();
                String arrivalWarehouse  = entityBean.getArrivalWarehouseCode();         
                if(imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode,deliveryWarehouse,"Y")== null||
                		imWarehouseDAO.findByBrandCodeAndWarehouseCode(brandCode,arrivalWarehouse,"Y")== null){
                	 log.info("錯誤的庫別或庫別未啟用");
                	throw new Exception("錯誤的庫別或庫別未啟用");
                }
               if(deliveryWarehouse.equals(arrivalWarehouse)){
            	   log.info("轉出與轉入庫別相同");
               	throw new Exception("轉出與轉入庫別相同");
               }
            	//---
	            if(ordertype.equals("WFF")){
	            	isSpec = true;
	            	
	            }else if(ordertype.equals("WFP")){
	            	isSpec = true;
	            	
	            }else if(ordertype.equals("WGF")){
	            	isSpec = true;
	            	
	            }else if(ordertype.equals("WGP")){
	            	isSpec = true;
	            	
	            }else if(ordertype.equals("WHF")){
	            	isSpec = true;
	            	
	            }else if(ordertype.equals("WHP")){
	            	isSpec = true;
	            }
	            
                 if(isSpec == true){
                	 
                   if(null != entityBean.getArrivalStoreCode()){
	                	
	                	BuShop buShop = buShopDAO.findById(entityBean.getArrivalStoreCode());
	                	if(null == buShop){
	                		throw new Exception("第  " + index+1 + "筆單據，查無輸入店號: " + entityBean.getArrivalStoreCode() + " 對應的店別");
	                	}
	                }else if(null == entityBean.getArrivalStoreCode()){
                	    throw new Exception("第  " + index+1 + "筆單據，輸入店別不得為空白");
                    }
                   
                   if(null != entityBean.getComfirmedBy()){
	                	
	                   String ComfirmedBy = entityBean.getComfirmedBy();
                       BuEmployee conFirmBy = buEmployeeDAO.findById(ComfirmedBy);
	                	if(null == conFirmBy){
	                		throw new Exception("複合人員工號有誤!!");
	                	}else if(ordertype.equals("WCF")||ordertype.equals("WCP")||ordertype.equals("WMF")||ordertype.equals("WMP")){
	                		throw new Exception("此單別不需填入複合人員!!");
	                	}
                   }
                   
                   if(null != entityBean.getReceiptedBy()){
	                	
	                  
                	   String ReceiptedBy= entityBean.getReceiptedBy();
                       BuEmployee receiptedBy= buEmployeeDAO.findById(ReceiptedBy);
	                	if(null == receiptedBy){
	                		throw new Exception("收貨人員工號有誤!!");
	                	}else if(ordertype.equals("WTF")||ordertype.equals("WTP")||ordertype.equals("WFP")||ordertype.equals("WFF")||
	                			ordertype.equals("WGP")||ordertype.equals("WGF")||ordertype.equals("WHP")||ordertype.equals("WHF")||
	                			ordertype.equals("WTF")||ordertype.equals("WTP")||ordertype.equals("MRF")||ordertype.equals("MRP")){
	                		throw new Exception("此單別不需填入複合人員!!");
	                	}
                    }

                   
                 }
                
                 
                 if(null != entityBean.getDeliveryDate()){
                	       
	                	   System.out.println("轉出庫日期:"+entityBean.getDeliveryDate());
                           DateFormat df=new java.text.SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		               	  
	                	   Calendar cd1_ori = Calendar.getInstance();
	                	   cd1_ori.setTime(entityBean.getDeliveryDate());//單據轉出日期
	                	    
	                	   String formatDate = cd1_ori.get(Calendar.DATE)+"/"+(cd1_ori.get(Calendar.MONTH)+1)+"/"+cd1_ori.get(Calendar.YEAR);
	                	    
	                	   String time1=df.format(new Date());  
		               	   Calendar cd1_mod = Calendar.getInstance();
		               	   Calendar cd1_mod_m = Calendar.getInstance();
		               	   cd1_mod.setTime(df.parse(time1));
		               	   cd1_mod_m.setTime(df.parse(time1));
		               	   cd1_mod.add(Calendar.DATE, 60);
		               	cd1_mod_m.add(Calendar.DATE, -60);
		               	   System.out.println("xxxx:"+ cd1_mod.get(Calendar.YEAR)+"/"+(cd1_mod.get(Calendar.MONTH)+1)+"/"+cd1_mod.get(Calendar.DATE));
		               	   if(cd1_mod.compareTo(cd1_ori)<0){
		               			   throw new Exception("轉出入日期超出範圍!!");
		               	   }else if(cd1_mod_m.compareTo(cd1_ori)>0){
		               		       throw new Exception("轉出入日期超出範圍!!");
		               	   }else{
		               	   
		               		System.out.println("formatDate:"+ cd1_mod.get(Calendar.DATE)+"/"+(cd1_mod.get(Calendar.MONTH)+1)+"/"+cd1_mod.get(Calendar.YEAR));
		               	   }
		               	   
                	 
		           }else{
		               	throw new Exception("未輸入日期!!");
                 }
                 
                 if(null != entityBean.getPackedBy()){
	                	
	                	String PackedBy= entityBean.getPackedBy();
                    BuEmployee packedBy = buEmployeeDAO.findById(PackedBy);
	                	if(null == packedBy){
	                		throw new Exception("撿貨人員工號有誤!!");
	                	}
                }else{
             	   throw new Exception("請輸入揀貨人員!!");
                }
                 
		    	originalOrderTypeCode = entityBean.getOriginalOrderTypeCode();
		    	originalOrderNo = entityBean.getOriginalOrderNo();
		    	List<ImMovementItem> imMovementItems = entityBean.getImMovementItems();
		    	//把ITEM的WAREHOUSECODE設定 LOTNO空值則設定預設值
		    	for (Iterator iterator = imMovementItems.iterator(); iterator.hasNext();) {
					ImMovementItem imMovementItem = (ImMovementItem) iterator.next();
					imMovementItem.setOriginalDeliveryQuantity(0D);
					imMovementItem.setArrivalWarehouseCode(entityBean.getArrivalWarehouseCode());
					imMovementItem.setDeliveryWarehouseCode(entityBean.getDeliveryWarehouseCode());
					imMovementItem.setCreatedBy(employeeCode);//20160426 by jason 建單工號
					imMovementItem.setLastUpdatedBy(employeeCode);//20160426 by jason 建單工號
				    imMovementItem.setCreationDate(new Date());
				    imMovementItem.setLastUpdateDate(new Date());
				    imMovementItem.setScanCode(imMovementItem.getItemCode());
					if(null == imMovementItem.getLotNo())
						imMovementItem.setLotNo(SystemConfig.LOT_NO);
					String itemcode = imItemEancodeDAO.getOneItemCodeByProperty(entityBean.getBrandCode(), imMovementItem.getItemCode());
					// 如果是國際碼，把商品品號匯入
					if(StringUtils.hasText(itemcode)){
						imMovementItem.setItemCode(itemcode);
					}
					//---by jason
					//Double Qty = imOnHandDAO.getCurrentStockOnHandQty1("TM",brandCode,imMovementItem.getItemCode(),deliveryWarehouse);	
					
					ImItem imItem = imItemDAO.findImItem(brandCode,imMovementItem.getItemCode(),"Y");
					if(imItem == null)
						throw new Exception("品號: "+imMovementItem.getItemCode()+"不存在或未啟用");
					/*
					if(imItem.getAllowMinusStock().equals("N")){
					if(Qty == null || Qty <= 0.0D )
						throw new Exception("品號: "+imMovementItem.getItemCode()+"庫存為0");
					}
					*/
					//---by jason 檢核商品稅別
					if(ordertype.substring(ordertype.length()-1).equals("F")){
						if(imItemDAO.findOneImItem(brandCode,imMovementItem.getItemCode(),"F") == null){
							throw new Exception("品號: "+imMovementItem.getItemCode()+"  單別:"+ordertype+"與品項稅別不符");	
						}
					}
					if(ordertype.substring(ordertype.length()-1).equals("P")){
						if(imItemDAO.findOneImItem(brandCode,imMovementItem.getItemCode(),"P") == null){
							throw new Exception("品號: "+imMovementItem.getItemCode()+"  單別:"+ordertype+"與品項稅別不符");
						}
					}
					//--by Jason 檢核單別與品項類別是否正確
			
					if(ordertype.equals("WTF")){
							int i = 0;
							boolean check = false;
							for(i = 0;i<wtf.length;i++){
								if(imItemDAO.findOneImItem2(brandCode, imMovementItem.getItemCode(), wtf[i])!=null){
									check = true;
									break;
								}
							}
							if(check)throw new Exception("品號: "+imMovementItem.getItemCode()+"  單別:"+ordertype+" 與品項類別不符");						
					}
					if(ordertype.equals("WTP")){
						int i = 0;
						boolean check = false;
						for(i = 0;i<wtp.length;i++){
							if(imItemDAO.findOneImItem2(brandCode, imMovementItem.getItemCode(), wtp[i])!=null){
								check = true;
								break;
							}
						}
						if(check)throw new Exception("品號: "+imMovementItem.getItemCode()+"  單別:"+ordertype+" 與品項類別不符");						
					}
					if(ordertype.equals("WFF")||ordertype.equals("WFP")){
						int i = 0;
						boolean check = false;
						for(i = 0;i<wff.length;i++){

							if(imItemDAO.findOneImItem2(brandCode, imMovementItem.getItemCode(), wff[i])!=null){
								check = true;
							}
						}
						if(!check)throw new Exception("品號: "+imMovementItem.getItemCode()+"  單別:"+ordertype+" 與品項類別不符");						
					}
					if(ordertype.equals("WGF")||ordertype.equals("WGP")){
						int i = 0;
						boolean check = false;
						for(i = 0;i<wgf.length;i++){
							if(imItemDAO.findOneImItem2(brandCode, imMovementItem.getItemCode(), wgf[i])!=null){
								check = true;
							}
						}
						if(!check)throw new Exception("品號: "+imMovementItem.getItemCode()+"  單別:"+ordertype+" 與品項類別不符");						
					}
					//-----
					 
					 
				}
		    	
	    	//第一筆資料業種
		    	if(null != imMovementItems && imMovementItems.size() > 1){
		    		ImMovementItem imMovementItem = imMovementItems.get(0);
		    		ImItem item = imItemService.findItem("T2", imMovementItem.getItemCode());
		    		if(null != item)
		    			entityBean.setItemCategory(item.getItemCategory());
		    	}
		    	
		    	imMovementHeads.add(entityBean);
		    }

		    List<ImMovementHead> newMovementHeads = imMovementMainService.executeBatchImportT2(imMovementHeads);

		    //執行Process
		    for (Iterator iterator = newMovementHeads.iterator(); iterator.hasNext();) {
				ImMovementHead imMovementHead = (ImMovementHead) iterator.next();
				log.info("ImMovementImportDataT2Main Main版本起流程");
    	    	ImMovementMainService.startProcess(imMovementHead);
			}

			if(imMovementHeads.size() != newMovementHeads.size()){
				reMsg.append("調撥單覆核成功");
			}else{
				reMsg.append("調撥單匯入成功");
			}
			reMsg.append(";").append(originalOrderTypeCode).append(";").append(originalOrderNo);
		} catch (Exception ex) {
			log.info(ex.getMessage());
			throw ex;
		}
		log.info("reMsg.toString() = " + reMsg.toString());
		return reMsg.toString();
	}
}
