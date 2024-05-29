package tw.com.tm.erp.utils;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImOnHand;
import tw.com.tm.erp.hbm.bean.ImOnHandId;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.service.SoSalesOrderMainService;
import tw.com.tm.erp.utils.NumberUtils;
//for 儲位用
import tw.com.tm.erp.hbm.bean.ImStorageHead;
import tw.com.tm.erp.action.ImStorageAction;



public class updateOnhand {
         
	  
	    private static final Log log = LogFactory.getLog(updateOnhand.class);
	    public static final String IN = "IN";
		public static final String MOVE = "MOVE";
		public static final String OUT = "OUT";
		public static final String ADJ = "ADJ";
		private static final String POSTYPECODE = "SOP";
		public static final String PROGRAM_ID = "SO_SALES_ORDER";
		
		List errorMsgs = new ArrayList(0);
		private SiProgramLogAction siProgramLogAction;
		public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
			this.siProgramLogAction = siProgramLogAction;
		}
		
	    public  void updateOnhandData(Map imMap , Map cmMap , String transactionType , String brandCode , boolean disallowMinusStock , boolean allowCmStockMinus, String organizationCode, boolean isClose
		, boolean isRevert,String employeeCode, String identification,
		List errorMsgs, Map isServiceItemMap, Map allowWholeSale, SoSalesOrderHead soSalesOrderHead,Set[] aggregateResult)throws Exception{
			  log.info("新版調撥扣庫存Method");
			  String wareHouseCode = null;
			  
			  ImOnHandDAO imOnHandDAO = (ImOnHandDAO) SpringUtils.getApplicationContext().getBean("imOnHandDAO");
			  CmDeclarationOnHandDAO cmDeclarationOnHandDAO =  (CmDeclarationOnHandDAO) SpringUtils.getApplicationContext().getBean("cmDeclarationOnHandDAO");
			  //String identification = MessageStatus.getIdentification(checkObj.getBrandCode(),
						//checkObj.getOrderTypeCode(), checkObj.getOrderNo());
			  
			  log.info("新版調撥扣庫存Method1");
			 if("MOVE".equals(transactionType)){
				  
				    log.info("updateCmOnHandByMap....");
					String lineId = new String("");
					String itemCode = new String("");
					String cmWarehouseCode = new String("");
					String declarationNo = new String("");
					Long declarationSeq = new Long(0);
					Double quantity = new Double(0);
				    
				     try{
						   //  log.info("進入調撥扣庫存:"+imMap.size());
						   Iterator it = imMap.values().iterator();
						   Iterator cm = cmMap.values().iterator();
						   while (it.hasNext()) {
								 log.info("進入調撥扣庫存:"+imMap.size());
								 Map modifyObj = (Map) it.next();
								 if (null != modifyObj) {
									
								          log.info("進入調撥imKey.length:");
								          String imItemCode = null;
										  String warehouseCode = null;
										  String lotNo = null;
								  	   
								           warehouseCode = (String)modifyObj.get("warehouseCode");
								           log.info("進入調撥實體庫存:"+warehouseCode);
								           imItemCode = (String) modifyObj.get("itemCode");
								           log.info("進入調撥imkeyString:"+imItemCode);
								            lotNo = (String) modifyObj.get("lotNo");
								              
								        
								             Double imQuantity = (Double) modifyObj.get("quantity");
								              
								               if(isRevert==true)
						    			        	 imQuantity = imQuantity * -1;
						    			       if(isClose==false){
						    			        	 
						    			    	    int tryTimes = 3; // 嘗試連接次數
						   						    int interval = 200; // 等待時間
						   						    for (int i = 0; i < tryTimes; i++) { // 如果更新失敗，經過5秒後重試
						   						    	try {
						    								 if(imQuantity!=0){ 	
						    							        if(disallowMinusStock){
								    			            	      log.info("NotallowImStockMinus:"+warehouseCode+","+imItemCode+","+lotNo+","+imQuantity+","+organizationCode+","+employeeCode+","+brandCode);
								    			                      imOnHandDAO.updateMoveUncommitQuantity(organizationCode, imItemCode, warehouseCode, lotNo, imQuantity, employeeCode, brandCode); 
								    			                 }else if(!disallowMinusStock){
								    			                	   log.info("allowImStockMinus:"+warehouseCode+","+imItemCode+","+lotNo+","+imQuantity);
								    			                        imOnHandDAO.updateMoveUncommitQuantityAllowMinus (organizationCode, imItemCode, warehouseCode, lotNo, imQuantity, employeeCode, brandCode);
								    			                 }
							    			                  }
						    								 break;
						   						    	} catch (Exception e) {
						   									try {
						   										Thread.sleep(interval);
						   									} catch (InterruptedException ie) {
						   										ie.printStackTrace();
						   									}
						   									if (i == tryTimes - 1) {
						   										//messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單庫存時(updateImOnHandByMap."
						   												//+ "" + ")發生錯誤，原因:" + e.getMessage(), employeeCode, errorMsgs);
						   										
						   										throw e;
						   									}
						   								}	 
						   						    }
						    			             
								                }else if(isClose==true){
								                	    int tryTimes = 3; // 嘗試連接次數
							   						    int interval = 5000; // 等待時間
							   						    for (int i = 0; i < tryTimes; i++) { // 如果更新失敗，經過5秒後重試
							   						      try {	 
							   						    	imOnHandDAO.updateStockOnHand (organizationCode, imItemCode, warehouseCode, lotNo, employeeCode, imQuantity, brandCode);
							   						      }catch (Exception e) {
							   									try {
							   										Thread.sleep(interval);
							   									} catch (InterruptedException ie) {
							   										ie.printStackTrace();
							   									}
							   									if (i == tryTimes - 1) {
							   										//messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單庫存時(updateImOnHandByMap."
							   												//+ "" + ")發生錯誤，原因:" + e.getMessage(), employeeCode, errorMsgs);
							   										
							   										throw e;
							   									}
							   								}
							   						    }
							   				    }
								    			              
								    			  
										     }
										  }
										  
										  
										  
									while (cm.hasNext()) {
										log.info("進入調撥報單庫存~");
										Map modifyObj = (Map) cm.next();
										if (null != modifyObj) {
											quantity = (Double) modifyObj.get("quantity");
											if (quantity != 0) {
												lineId = (String) modifyObj.get("lineId");
												itemCode = (String) modifyObj.get("itemCode");
												cmWarehouseCode = (String) modifyObj.get("cmWarehouseCode");
												declarationNo = (String) modifyObj.get("declarationNo");
												declarationSeq = (Long) modifyObj.get("declarationSeq");
												log.info("itemCode=" + itemCode + "/" + "cmWarehouseCode=" + cmWarehouseCode + "/declarationNo="
														+ declarationNo + "/declarationSeq=" + declarationSeq + "/quantity=" + quantity);
												cmDeclarationOnHandDAO.updateMoveUncommitQuantity(declarationNo, declarationSeq, itemCode,
														cmWarehouseCode, brandCode, quantity, employeeCode);
											}
										}
									}
			    }catch(Exception ex){
			    	log.error("調撥發生錯誤");
			  		ex.printStackTrace();  
			    	throw ex;
			    	
			    	
			    }
			  }else if("OUT".equals(transactionType)){
				  log.info("已進入------------------");				  
				  String errorMsg = null ;
				  Iterator it = aggregateResult[0].iterator(); // ImOnHand扣庫存用
				  Iterator cm = aggregateResult[1].iterator(); // CmOnHand扣庫存用
				  
				// ======================================預扣報單庫存量=======================================================                                                                                                                                                                             
					while (cm.hasNext()) {
					    try {
					    log.info("已進入1111111111");
						//Map.Entry cmEntry = (Map.Entry) cm.next();                                                                                                                                                                                                d                                                          
						Double outUnCommitQty = (Double)  0.0D;
						log.info("outUnCommitQty----"+outUnCommitQty);
						String[] cmkeyArray = StringUtils.delimitedListToStringArray(
							String.valueOf(0.0D) , "{$}");
						
						log.info("品牌(" + brandCode + ")、報關單號("
							    + cmkeyArray[0] + ")、報關項次(" + cmkeyArray[1]
							             							    + ")、海關料號(" + cmkeyArray[2] + ")、關別("
							             							    + cmkeyArray[3] + ") 其中有一項並未填寫");
						
						if (!StringUtils.hasText(cmkeyArray[0])
							|| NumberUtils.getLong((cmkeyArray[1])) == 0
							|| !StringUtils.hasText(cmkeyArray[2])
							|| !StringUtils.hasText(cmkeyArray[3]))
						    throw new Exception("品牌(" + brandCode + ")、報關單號("
							    + cmkeyArray[0] + ")、報關項次(" + cmkeyArray[1]
							    + ")、海關料號(" + cmkeyArray[2] + ")、關別("
							    + cmkeyArray[3] + ") 其中有一項並未填寫");
						if (!"Y".equals((String) isServiceItemMap.get(cmkeyArray[2]))) {
						    cmDeclarationOnHandDAO.updateOutUncommitQty(cmkeyArray[0],
							    NumberUtils.getLong(cmkeyArray[1]), cmkeyArray[2],
							    cmkeyArray[3], brandCode, outUnCommitQty,
							    employeeCode, (String) allowWholeSale
								    .get(cmkeyArray[2]));
						}
					    } catch (Exception ex) {
						errorMsg = "預扣 " + identification + " 的報單庫存量時發生錯誤，原因： ";
						log.error(errorMsg + ex.toString());
						siProgramLogAction.createProgramLog(PROGRAM_ID,
							MessageStatus.LOG_ERROR, identification, errorMsg
								+ ex.getMessage(), employeeCode);
						errorMsgs.add(errorMsg);
					    }
					}
					
					// ======================================預扣實體庫別庫存量=======================================================
					while (it.hasNext()) {
					    try {
					    log.info("已進入22222222");	
						//Ma ntry = (Map.Entry) it.next();
						// Double outUnCommitQty = (Double) entry.getValue();
						log.info("已進入33333333");
						String[] keyArray = StringUtils.delimitedListToStringArray(
								String.valueOf(0.0D), "{$}");
						if (!"Y".equals((String) isServiceItemMap.get(keyArray[0]))) {
							log.info("已進入4444444");
							List<ImOnHand> lockedOnHands = null;
						    try {
							lockedOnHands = imOnHandDAO.getLockedOnHand(
								organizationCode, keyArray[0], keyArray[1],
								keyArray[2], brandCode);
						    } catch (CannotAcquireLockException cale) {
							throw new FormException("品牌(" + brandCode + ")、品號("
								+ keyArray[0] + ")、庫別(" + keyArray[1] + ")、批號("
								+ keyArray[2] + ")已鎖定，請稍後再試！");
						    }
						    if (lockedOnHands != null && lockedOnHands.size() > 0) {
							ImOnHand onHandPO = (ImOnHand) lockedOnHands.get(0);
							Double availableQuantity = onHandPO.getStockOnHandQty()
								- onHandPO.getOutUncommitQty()
								+ onHandPO.getInUncommitQty()
								+ onHandPO.getMoveUncommitQty()
								+ onHandPO.getOtherUncommitQty();
							// 如果數量超過庫存 且不是POS以及允許全賣出
							if ((Double) 0.0 > availableQuantity
								&& (!(POSTYPECODE.equals(soSalesOrderHead
									.getOrderTypeCode())))) {
							    throw new ValidationErrorException("品牌("
								    + brandCode + ")、品號(" + keyArray[0]
								    + ")、庫別(" + keyArray[1] + ")、批號("
								    + keyArray[2] + ")可用庫存量不足！");
							} else {
							    if (!POSTYPECODE.equals(soSalesOrderHead
								    .getOrderTypeCode())) {
								imOnHandDAO.bookAvailableQuantity(
									lockedOnHands, (Double) 0.0
										, "FIFO", employeeCode);
							    } else {
								imOnHandDAO.bookQuantity(lockedOnHands,
									(Double) 0.0, "FIFO",
									employeeCode);
							    }
							}
						    } else {
							// 如果是T2POS 且 不允許全賣出
							if (!(POSTYPECODE.equals(soSalesOrderHead
								.getOrderTypeCode()))) {
							    throw new ValidationErrorException("查無品牌("
								    + brandCode + ")、品號(" + keyArray[0]
								    + ")、庫別(" + keyArray[1] + ")、批號("
								    + keyArray[2] + ")的庫存資料！");
							} else {
							    // ==========================SOP單查無onHand時新增一筆====================================
							    ImOnHandId id = new ImOnHandId();
							    ImOnHand newOnHand = new ImOnHand();
							    id.setOrganizationCode(organizationCode);
							    id.setItemCode(keyArray[0]);
							    id.setWarehouseCode(keyArray[1]);
							    id.setLotNo(keyArray[2]);
							    newOnHand.setId(id);
							    newOnHand.setBrandCode(brandCode);
							    newOnHand.setStockOnHandQty(0D);
							    newOnHand.setOutUncommitQty((Double) 0.0);
							    newOnHand.setInUncommitQty(0D);
							    newOnHand.setMoveUncommitQty(0D);
							    newOnHand.setOtherUncommitQty(0D);
							    newOnHand.setCreatedBy(employeeCode);
							    newOnHand.setCreationDate(new Date());
							    newOnHand.setLastUpdatedBy(employeeCode);
							    newOnHand.setLastUpdateDate(new Date());
							    imOnHandDAO.save(newOnHand);
							}
						    }
						}
					    } catch (Exception ex) {
						errorMsg = "預扣 " + identification + " 的庫存量時發生錯誤，原因： ";
						log.error(errorMsg + ex.toString());
						siProgramLogAction.createProgramLog(PROGRAM_ID,
							MessageStatus.LOG_ERROR, identification, errorMsg
								+ ex.getMessage(), employeeCode);
						errorMsgs.add(errorMsg);
					    }
					}		
					
			  }

			  
			  
		}
	    
	    
	    //Luke add 多型for區別是否為排程重跑已鎖定資料
	    public void updateOnhandData2(Map imMap , Map cmMap , String transactionType ,String brandCode, boolean allowImStockMinus , boolean allowCmStockMinus, String organizationCode, boolean isClose
	    		, boolean isRevert,String employeeCode, String identification,List errorMsgs, ImMovementHead imMovementHead, Map isServiceItemMap, Map allowWholeSale,
	    		SoSalesOrderHead soSalesOrderHead,Set[] aggregateResult)throws Exception{
	    	log.info("非排程扣庫存");
	    	try {
				this.updateOnhandData2(imMap,cmMap,transactionType,brandCode,allowImStockMinus,allowCmStockMinus,organizationCode,isClose
						,isRevert,employeeCode,identification,errorMsgs, imMovementHead, isServiceItemMap,allowWholeSale,
						soSalesOrderHead,aggregateResult,false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("資料鎖定");	
			}
	    }
	    
	    //103-06-23 Luke add
	    public  void updateOnhandData2(Map imMap , Map cmMap , String transactionType ,String brandCode, boolean allowImStockMinus , boolean allowCmStockMinus, String organizationCode, boolean isClose
	    		, boolean isRevert,String employeeCode, String identification,List errorMsgs, ImMovementHead imMovementHead, Map isServiceItemMap, Map allowWholeSale,
	    		SoSalesOrderHead soSalesOrderHead,Set[] aggregateResult,boolean isResend)throws Exception{
	    			  log.info("調撥扣庫存Method2");
	    			  String wareHouseCode = null;
	    			  ImOnHandDAO imOnHandDAO = (ImOnHandDAO) SpringUtils.getApplicationContext().getBean("imOnHandDAO");
	    			  CmDeclarationOnHandDAO cmDeclarationOnHandDAO =  (CmDeclarationOnHandDAO) SpringUtils.getApplicationContext().getBean("cmDeclarationOnHandDAO");
	    			  
	    			  Map imlockedlist = new HashMap();
    				  Map cmlockedlist = new HashMap();
    				  
	    			 if("MOVE".equals(transactionType)){
	    				  log.info("updateCmOnHandByMap....");
	    				  String lineId = new String("");
	    				  String itemCode = new String("");
	    				  String cmWarehouseCode = new String("");
	    				  String declarationNo = new String("");
	    				  Long declarationSeq = new Long(0);
	    				  Double quantity = new Double(0);
	    				  
	    				  try{
	    					  Iterator it = imMap.values().iterator();
	    					  Iterator cm = cmMap.values().iterator();
	    					  while (it.hasNext()) {
	    						  log.info("進入調撥扣庫存im:"+imMap.size());
	    						  Map modifyObj = (Map) it.next();
	    						  if (null != modifyObj) {
	    							  String imItemCode = null;
	    							  String warehouseCode = null;
	    							  String lotNo = null;
	    							  
	    							  warehouseCode = (String)modifyObj.get("warehouseCode");
	    							  log.info("進入調撥實體庫存:"+warehouseCode);
	    							  imItemCode = (String) modifyObj.get("itemCode");
	    							  log.info("進入調撥imkeyString:"+imItemCode);
	    							  lotNo = (String) modifyObj.get("lotNo");
	    							  Double imQuantity = (Double) modifyObj.get("quantity");
	    							  String key = imItemCode.trim()+warehouseCode+lotNo; 
	    							  if(isRevert==true)
	    								  imQuantity = imQuantity * -1;
	    							  if(isClose==false){
	    								  try {
	    									  if(imQuantity!=0){
	    										  if(!allowImStockMinus){
	    											  log.info("NotallowImStockMinus:"+warehouseCode+","+imItemCode+","+lotNo+","+imQuantity+","+organizationCode+","+employeeCode+","+brandCode);
	    											  imOnHandDAO.updateMoveUncommitQuantity(organizationCode, imItemCode, warehouseCode, lotNo, imQuantity, employeeCode, brandCode); 
	    										  }else if(allowImStockMinus){
	    											  log.info("allowImStockMinus:"+warehouseCode+","+imItemCode+","+lotNo+","+imQuantity);
	    											  imOnHandDAO.updateMoveUncommitQuantityAllowMinus (organizationCode, imItemCode, warehouseCode, lotNo, imQuantity, employeeCode, brandCode);
	    										  }
	    									  }
	    								  } catch (Exception e) {
	    									  //紀錄
	    									  log.info("IM-LOCKED!!"+key);
	    									  imlockedlist.put(key, modifyObj);
	    								  }
	    							  }else if(isClose==true){
	    								  try {
	    									  imOnHandDAO.updateStockOnHand (organizationCode, imItemCode, warehouseCode, lotNo, employeeCode, imQuantity, brandCode);
	    								  }catch (Exception e) {
	    									  //紀錄
	    									  log.info("IM-LOCKED!!"+key);
	    									  imlockedlist.put(key, modifyObj);
	    								  }
	    							  } 
	    						  }
	    					  }
	    					  
	    					  log.info("im end,check LockedAndRetry,clear lockedlist");
	    					  this.imLockedRetry(imlockedlist,imOnHandDAO,isRevert,isClose,allowImStockMinus,employeeCode
	    					    		,identification,organizationCode,brandCode);
	    					  
	    					  while (cm.hasNext()) {
	    						  log.info("進入cm調撥報單庫存~");
	    						  String key = "";
	    						  Map modifyObj = (Map) cm.next();
	    						  if (null != modifyObj) {
	    							  quantity = (Double) modifyObj.get("quantity");
	    							  try{
	    								  if (quantity != 0) {
		    								  lineId = (String) modifyObj.get("lineId");
		    								  itemCode = (String) modifyObj.get("itemCode");
		    								  cmWarehouseCode = (String) modifyObj.get("cmWarehouseCode");
		    								  declarationNo = (String) modifyObj.get("declarationNo");
		    								  declarationSeq = (Long) modifyObj.get("declarationSeq");
		    								  key =itemCode.trim()+cmWarehouseCode+declarationNo+declarationSeq; 
		    								  log.info("itemCode=" + itemCode + "/" + "cmWarehouseCode=" + cmWarehouseCode + "/declarationNo="
		    										  + declarationNo + "/declarationSeq=" + declarationSeq + "/quantity=" + quantity);
		    								  cmDeclarationOnHandDAO.updateMoveUncommitQuantity(declarationNo, declarationSeq, itemCode,
		    										  cmWarehouseCode, brandCode, quantity, employeeCode);
		    							  }
	    							  }catch(Exception e){
	    								  //紀錄
	    								  log.info("CM-LOCKED!!"+key);
    									  cmlockedlist.put(key, modifyObj);
	    							  }
	    						  }
	    					  }
	    					  
	    					  log.info("cm end,check LockedAndRetry,clear lockedlist");
	    					  this.cmLockedRetry(cmlockedlist,cmDeclarationOnHandDAO,declarationNo,declarationSeq
	    					    		,itemCode,lineId,cmWarehouseCode,brandCode,quantity,employeeCode);
	    					  
	    					  if(imlockedlist.size()==0 && cmlockedlist.size()==0 ){
	    						  SiResendStatus(imMovementHead.getHeadId(), imMovementHead.getOrderNo(), imMovementHead.getOrderTypeCode());
	    					  }else{
	    						  log.info("lock資料存在");
	    					  }
	    				  }catch(Exception ex){
	    					  log.error("調撥發生錯誤");
	    					  if(imlockedlist.size()!=0 || cmlockedlist.size()!=0 ){
								  log.info("將鎖定資料回寫SI_RESEND");
								  WriteToSiResend(imMovementHead.getHeadId(), imMovementHead.getOrderNo(), imMovementHead.getOrderTypeCode());
							  }
	    					  ex.printStackTrace();  
	    					  throw new Exception("資料遭鎖定  請稍後再試~");	    					  
	    				  }
	    			  }else if("OUT".equals(transactionType)){
	    				  log.info("進入更新OUT量");				  
	    				  String errorMsg = null ;
	    				  
//	    				  Map cMap = new HashMap();
	    				  
	    				  try{

		    				  Iterator it = aggregateResult[0].iterator(); // ImOnHand扣庫存用
		    				  Iterator cm = aggregateResult[1].iterator(); // CmOnHand扣庫存用
		    				  
		    				// ======================================預扣報單庫存量=======================================================
		    					while (cm.hasNext()) {
		    						//Map.Entry cmEntry = (Map.Entry) cm.next();
		    						
//		    						cMap.put("value", cmEntry.getValue());
//		    						cMap.put("key", cmEntry.getKey());
		    						
		    					    try {
		    					    log.info("已進入預扣報單庫存量");			
		    						Double outUnCommitQty = (Double) 0.0;
		    						log.info("outUnCommitQty----"+outUnCommitQty);
		    						String[] cmkeyArray = StringUtils.delimitedListToStringArray(
		    							String.valueOf(0.0), "{$}");
		    						
		    						log.info("品牌(" + brandCode + ")、報關單號("
		    							    + cmkeyArray[0] + ")、報關項次(" + cmkeyArray[1]
		    							             							    + ")、海關料號(" + cmkeyArray[2] + ")、關別("
		    							             							    + cmkeyArray[3] + ") 其中有一項並未填寫");
		    						
		    						if (!StringUtils.hasText(cmkeyArray[0])
		    							|| NumberUtils.getLong((cmkeyArray[1])) == 0
		    							|| !StringUtils.hasText(cmkeyArray[2])
		    							|| !StringUtils.hasText(cmkeyArray[3]))
		    						    throw new Exception("品牌(" + brandCode + ")、報關單號("
		    							    + cmkeyArray[0] + ")、報關項次(" + cmkeyArray[1]
		    							    + ")、海關料號(" + cmkeyArray[2] + ")、關別("
		    							    + cmkeyArray[3] + ") 其中有一項並未填寫");
		    						if (!"Y".equals((String) isServiceItemMap.get(cmkeyArray[2]))) {
		    						    cmDeclarationOnHandDAO.updateOutUncommitQty(cmkeyArray[0],
		    							    NumberUtils.getLong(cmkeyArray[1]), cmkeyArray[2],
		    							    cmkeyArray[3], brandCode, outUnCommitQty,
		    							    employeeCode, (String) allowWholeSale
		    								    .get(cmkeyArray[2]));
		    						}
		    					    } catch (Exception ex) {
		    					    	ex.printStackTrace();
		    						errorMsg = "預扣 " + identification + " 的報單庫存量時發生錯誤，原因： ";
		    						log.error(errorMsg + ex.toString());
		    						siProgramLogAction.createProgramLog(PROGRAM_ID,
		    							MessageStatus.LOG_ERROR, identification, errorMsg
		    								+ ex.getMessage(), employeeCode);
//									cmlockedlist.put(cmEntry.getKey(),cMap);
		    						errorMsgs.add(errorMsg);
		    					    }
		    					}
		    					
//		    					this.soCmLockRetry(cmlockedlist, brandCode, isServiceItemMap, cmDeclarationOnHandDAO, employeeCode, 
//	    								allowWholeSale);
		    					
		    					// ======================================預扣實體庫別庫存量=======================================================
		    					while (it.hasNext()) {
		    						//Map.Entry entry = (Map.Entry) it.next();
		    					    try {
		    					    log.info("已進入預扣實體庫別庫存量");	
		    						// Double outUnCommitQty = (Double) entry.getValue();
		    						//log.info("已進入33333333");
		    						String[] keyArray = StringUtils.delimitedListToStringArray(
		    							String.valueOf(0.0), "{$}");
		    						if (!"Y".equals((String) isServiceItemMap.get(keyArray[0]))) {
		    							log.info("已進入4444444");
		    							List<ImOnHand> lockedOnHands = null;
		    						    try {
		    							lockedOnHands = imOnHandDAO.getLockedOnHand(
		    								organizationCode, keyArray[0], keyArray[1],
		    								keyArray[2], brandCode);
		    						    } catch (CannotAcquireLockException cale) {
		    							throw new FormException("品牌(" + brandCode + ")、品號("
		    								+ keyArray[0] + ")、庫別(" + keyArray[1] + ")、批號("
		    								+ keyArray[2] + ")已鎖定，請稍後再試！");
		    						    }
		    						    if (lockedOnHands != null && lockedOnHands.size() > 0) {
		    							ImOnHand onHandPO = (ImOnHand) lockedOnHands.get(0);
		    							Double availableQuantity = onHandPO.getStockOnHandQty()
		    								- onHandPO.getOutUncommitQty()
		    								+ onHandPO.getInUncommitQty()
		    								+ onHandPO.getMoveUncommitQty()
		    								+ onHandPO.getOtherUncommitQty();
		    							log.info("可用實體庫存量availableQuantity:"+availableQuantity);
		    							// 如果數量超過庫存 且不是POS以及允許全賣出
		    							if ((Double) 0.0 > availableQuantity
		    								&& (!(POSTYPECODE.equals(soSalesOrderHead
		    									.getOrderTypeCode())))) {
		    							    throw new ValidationErrorException("品牌("
		    								    + brandCode + ")、品號(" + keyArray[0]
		    								    + ")、庫別(" + keyArray[1] + ")、批號("
		    								    + keyArray[2] + ")可用庫存量不足！");
		    							} else {
		    							    if (!POSTYPECODE.equals(soSalesOrderHead
		    								    .getOrderTypeCode())) {
		    								imOnHandDAO.bookAvailableQuantity(
		    									lockedOnHands, (Double) 0.0D, "FIFO", employeeCode);
		    							    } else {
		    								imOnHandDAO.bookQuantity(lockedOnHands,
		    									(Double) 0.0, "FIFO",
		    									employeeCode);
		    							    }
		    							}
		    							log.info("更新實體庫存資料完成");
		    						    } else {
		    							// 如果是T2POS 且 不允許全賣出
		    							if (!(POSTYPECODE.equals(soSalesOrderHead
		    								.getOrderTypeCode()))) {
		    							    throw new ValidationErrorException("查無品牌("
		    								    + brandCode + ")、品號(" + keyArray[0]
		    								    + ")、庫別(" + keyArray[1] + ")、批號("
		    								    + keyArray[2] + ")的庫存資料！");
		    							} else {
		    							    // ==========================SOP單查無onHand時新增一筆====================================
		    							    ImOnHandId id = new ImOnHandId();
		    							    ImOnHand newOnHand = new ImOnHand();
		    							    id.setOrganizationCode(organizationCode);
		    							    id.setItemCode(keyArray[0]);
		    							    id.setWarehouseCode(keyArray[1]);
		    							    id.setLotNo(keyArray[2]);
		    							    newOnHand.setId(id);
		    							    newOnHand.setBrandCode(brandCode);
		    							    newOnHand.setStockOnHandQty(0D);
		    							    newOnHand.setOutUncommitQty((Double)0.0);
		    							    newOnHand.setInUncommitQty(0D);
		    							    newOnHand.setMoveUncommitQty(0D);
		    							    newOnHand.setOtherUncommitQty(0D);
		    							    newOnHand.setCreatedBy(employeeCode);
		    							    newOnHand.setCreationDate(new Date());
		    							    newOnHand.setLastUpdatedBy(employeeCode);
		    							    newOnHand.setLastUpdateDate(new Date());
		    							    imOnHandDAO.save(newOnHand);
		    							    log.info("新增實體庫存資料完成");
		    							}
		    						    }
		    						}
		    					    } catch (Exception ex) {
		    					    	ex.printStackTrace();
		    						errorMsg = "預扣 " + identification + " 的庫存量時發生錯誤，原因： ";
		    						log.error(errorMsg + ex.toString());
		    						siProgramLogAction.createProgramLog(PROGRAM_ID,
		    							MessageStatus.LOG_ERROR, identification, errorMsg
		    								+ ex.getMessage(), employeeCode);
//		    						imlockedlist.put(entry.getKey(),entry);
		    						errorMsgs.add(errorMsg);
		    					    }
		    					}
		    					
//		    					this.soImLockRetry(imlockedlist, isServiceItemMap, imOnHandDAO, organizationCode, brandCode,
//		    							soSalesOrderHead, employeeCode);
		    					
//		    					if(imlockedlist.size()==0 && cmlockedlist.size()==0 ){
		    						  SiResendStatus(soSalesOrderHead.getHeadId(), soSalesOrderHead.getOrderNo(), soSalesOrderHead.getOrderTypeCode());
//		    					  }else{
//		    						  log.info("lock資料存在");
//		    					  }
		    					
	    				  }catch(Exception e){
	    					  log.error("銷售發生錯誤");
	    					  log.info("imlockedlist size:"+imlockedlist.size());
	    					  log.info("cmlockedlist size:"+cmlockedlist.size());
	    					  //if(imlockedlist.size()!=0 || cmlockedlist.size()!=0 ){
								  log.info("將鎖定資料回寫SI_RESEND");
								  WriteToSiResend(soSalesOrderHead.getHeadId(), soSalesOrderHead.getOrderNo(), soSalesOrderHead.getOrderTypeCode());
							  //}
	    					  e.printStackTrace();
	    					  throw new Exception(errorMsg);
	    				  }		
	    					
	    			  }
	    			 
	    		}
	    
	    private void SiResendStatus(Long headId, String orderNO, String orderTypeCode) throws Exception{
	    
		    log.info("WriteToSiResend....");
	    	Connection conn = null;
	    	Statement stmt = null;
	    	ResultSet rs = null;
	    	DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");
	    	try{
	    		conn = dataSource.getConnection();
				log.info("conn success!!");
				StringBuffer sqlsel = new StringBuffer();
				StringBuffer sql = new StringBuffer();
				stmt = conn.createStatement();
				//查詢SiResend
				sqlsel.append("select * from ERP.SI_RESEND where ORDER_NO='").append(orderNO).append("'")
				.append(" and ORDER_TYPE_CODE='").append(orderTypeCode).append("'");
				rs = stmt.executeQuery(sqlsel.toString());
				log.info(sqlsel.toString());
				if(rs.next()){
					//update
					//sql.append("update ERP.SI_RESEND set STATUS='OK' where HEAD_ID ='").append(headId).append("'");
					log.info(sql.toString());
					stmt = conn.createStatement();
					stmt.executeUpdate(sql.toString());
					log.info("update success!!");
				}
	    	}catch(Exception e){
	    		log.error(e);
	    		throw e;
	    	}finally{
	    		if (stmt != null) {
	    			try {
	    				stmt.close();
	    			} catch (SQLException e) {
	    				e.printStackTrace();
	    				throw new Exception(e.getMessage());
	    			}
	    		}
	    		if (conn != null) {
	    			try {
	    				conn.close();
	    			} catch (SQLException e) {
	    				e.printStackTrace();
	    				throw new Exception(e.getMessage());
	    			}
	    		}
	    	}
    	}
	    
	    // 103-06-23 Luke add cmLockRetry
	private void cmLockedRetry(Map lockedlist,CmDeclarationOnHandDAO cmDeclarationOnHandDAO,String declarationNo,
			Long declarationSeq, String itemCode,String lineId, String cmWarehouseCode, String brandCode,
			Double quantity, String employeeCode) throws Exception {
		Iterator lc = lockedlist.values().iterator();
		while (lc.hasNext()) {
			log.info("進入cm調撥報單庫存LockRetry~");
			Map modifyObj = (Map) lc.next();
			if (null != modifyObj) {
				quantity = (Double) modifyObj.get("quantity");
				int tryTimes = 3; // 嘗試連接次數
				int interval = 2000; // 等待時間
				for (int i = 0; i < tryTimes; i++) { // 如果更新失敗，經過2秒後重試
					try {
						if (quantity != 0) {
							lineId = (String) modifyObj.get("lineId");
							itemCode = (String) modifyObj.get("itemCode");
							cmWarehouseCode = (String) modifyObj
									.get("cmWarehouseCode");
							declarationNo = (String) modifyObj
									.get("declarationNo");
							declarationSeq = (Long) modifyObj
									.get("declarationSeq");
							log.info("itemCode=" + itemCode + "/" + "cmWarehouseCode=" + cmWarehouseCode
									+ "/declarationNo=" + declarationNo	+ "/declarationSeq=" + declarationSeq
									+ "/quantity=" + quantity);
							cmDeclarationOnHandDAO.updateMoveUncommitQuantity(declarationNo, declarationSeq, itemCode,
									cmWarehouseCode, brandCode, quantity,employeeCode);
						}
					} catch (Exception e) {
						try {
							Thread.sleep(interval);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						if (i == tryTimes - 1) {
							// messageHandle(MessageStatus.LOG_ERROR,
							// identification, "更新調撥單庫存時(updateImOnHandByMap."
							// + "" + ")發生錯誤，原因:" + e.getMessage(),
							// employeeCode, errorMsgs);
							throw e;
						}
					}
				}
			}
		}
	}
	//103-06-24 Luke add 鎖定資料回寫多型for IS_LOCK
	/**
	 * @param imMovementHead
	 * @param soSalesOrderHead
	 * @throws Exception
	 */
	public void WriteToSiResend(Long headId, String orderNO, String orderTypeCode) throws Exception{
		try{
			log.info("資料遭鎖定");
			WriteToSiResend(headId, orderNO, orderTypeCode, "Y");
		}catch(Exception e){
			log.error(e);
    		throw e;
		}
	}
	
	//103-06-24 Luke add 將執行例外之資料回寫SiResend
	/**
	 * @param imMovementHead
	 * @param soSalesOrderHead
	 * @param IS_LOCK             是否為鎖定資料
	 * @param errorMsg            錯誤訊息
	 * @throws Exception
	 */
	public void WriteToSiResend(Long headId, String orderNO, String orderTypeCode, String IS_LOCK) throws Exception{
    	log.info("WriteToSiResend....");
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	DataSource dataSource = (DataSource) SpringUtils.getApplicationContext().getBean("dataSource");
    	try{
    		conn = dataSource.getConnection();
			log.info("conn success!!");
			StringBuffer sqlsel = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			stmt = conn.createStatement();
			//查詢SiResend
			sqlsel.append("select * from ERP.SI_RESEND where ORDER_NO='").append(orderNO).append("'")
			.append(" and ORDER_TYPE_CODE='").append(orderTypeCode).append("'");
			rs = stmt.executeQuery(sqlsel.toString());
			log.info(sqlsel.toString());
			if(!rs.next()){
				//insert
				sql.append("insert into ERP.SI_RESEND(HEAD_ID,IS_LOCK,ORDER_NO,ORDER_TYPE_CODE,STATUS) values(").append(headId)
				.append(",'").append(IS_LOCK).append("','").append(orderNO).append("','")
				.append(orderTypeCode).append("','NOTOK')");
				log.info(sql.toString());
				stmt.executeUpdate(sql.toString());
				log.info("insert success!!");
			}
    	}catch(Exception e){
    		log.error(e);
    		throw e;
    	}finally{
    		if (stmt != null) {
    			try {
    				stmt.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    				throw new Exception(e.getMessage());
    			}
    		}
    		if (conn != null) {
    			try {
    				conn.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    				throw new Exception(e.getMessage());
    			}
    		}
    	}
    }
	
	    // 103-06-23 Luke add imLockRetry
	private void imLockedRetry(Map lockedlist, ImOnHandDAO imOnHandDAO,
			boolean isRevert, boolean isClose, boolean allowImStockMinus,
			String employeeCode, String identification,
			String organizationCode, String brandCode) throws Exception {
		Iterator lc = lockedlist.values().iterator();
		while (lc.hasNext()) {
			log.info("進入調撥扣庫存lc:" + lockedlist.size());
			Map modifyObj = (Map) lc.next();
			if (null != modifyObj) {
				log.info("進入調撥LC.length:");
				String imItemCode = null;
				String warehouseCode = null;
				String lotNo = null;

				warehouseCode = (String) modifyObj.get("warehouseCode");
				log.info("進入調撥實體庫存:" + warehouseCode);
				imItemCode = (String) modifyObj.get("itemCode");
				log.info("進入調撥imkeyString:" + imItemCode);
				lotNo = (String) modifyObj.get("lotNo");
				Double imQuantity = (Double) modifyObj.get("quantity");

				if (isRevert == true)
					imQuantity = imQuantity * -1;
				if (isClose == false) {

					int tryTimes = 3; // 嘗試連接次數
					int interval = 2000; // 等待時間
					for (int i = 0; i < tryTimes; i++) { // 如果更新失敗，經過2秒後重試
						try {
							if (imQuantity != 0) {
								if (!allowImStockMinus) {
									log.info("NotallowImStockMinus:"
											+ warehouseCode + "," + imItemCode
											+ "," + lotNo + "," + imQuantity
											+ "," + organizationCode + ","
											+ employeeCode + "," + brandCode);
									imOnHandDAO.updateMoveUncommitQuantity(
											organizationCode, imItemCode,
											warehouseCode, lotNo, imQuantity,
											employeeCode, brandCode);
								} else if (allowImStockMinus) {
									log.info("allowImStockMinus:"
											+ warehouseCode + "," + imItemCode
											+ "," + lotNo + "," + imQuantity);
									imOnHandDAO
											.updateMoveUncommitQuantityAllowMinus(
													organizationCode,
													imItemCode, warehouseCode,
													lotNo, imQuantity,
													employeeCode, brandCode);
								}
							}
							break;
						} catch (Exception e) {
							try {
								Thread.sleep(interval);
							} catch (InterruptedException ie) {
								ie.printStackTrace();
							}
							if (i == tryTimes - 1) {
								// messageHandle(MessageStatus.LOG_ERROR,
								// identification,
								// "更新調撥單庫存時(updateImOnHandByMap."
								// + "" + ")發生錯誤，原因:" + e.getMessage(),
								// employeeCode, errorMsgs);
								throw e;
							}
						}
					}
				} else if (isClose == true) {
					int tryTimes = 3; // 嘗試連接次數
					int interval = 5000; // 等待時間
					for (int i = 0; i < tryTimes; i++) { // 如果更新失敗，經過5秒後重試
						try {
							imOnHandDAO.updateStockOnHand(organizationCode,
									imItemCode, warehouseCode, lotNo,
									employeeCode, imQuantity, brandCode);
						} catch (Exception e) {
							try {
								Thread.sleep(interval);
							} catch (InterruptedException ie) {
								ie.printStackTrace();
							}
							if (i == tryTimes - 1) {
								// messageHandle(MessageStatus.LOG_ERROR,
								// identification,
								// "更新調撥單庫存時(updateImOnHandByMap."
								// + "" + ")發生錯誤，原因:" + e.getMessage(),
								// employeeCode, errorMsgs);

								throw e;
							}
						}
					}
				}

			}
		}
	}
	    
	    public void messageHandle(String messageStatus, String identification, String message, String user, List errorMsgs)throws Exception {
            
	    	log.info("進入messageHandle");
			siProgramLogAction.createProgramLog("IM_MOVEMENT", MessageStatus.LOG_ERROR, identification, message, user);
			errorMsgs.add(message);
			log.error("ERROR:" + message);
			
		}
	    
	  /*
		private void updateImOnHandByMap(String organizationCode, String brandCode, Map collectMaps, boolean disallowMinusStock,
				String employeeCode, String identification, List errorMsgs) throws Exception {
			log.info("updateImOnHandByMap....");
			Iterator it = collectMaps.values().iterator();
			String lineId = new String("");
			String itemCode = new String("");
			String warehouseCode = new String("");
			String lotNo = new String("");
			Double quantity = new Double(0);
			try {
				while (it.hasNext()) {
					Map modifyObj = (Map) it.next();
					if (null != modifyObj) {
						quantity = (Double) modifyObj.get("quantity");
						if (quantity != 0) {
							lineId = (String) modifyObj.get("lineId");
							itemCode = (String) modifyObj.get("itemCode");
							warehouseCode = (String) modifyObj.get("warehouseCode");
							lotNo = (String) modifyObj.get("lotNo");
							log.info("updateImOnHandByMap.itemCode=" + itemCode + "/" + "warehouseCode=" + warehouseCode
									+ "/lotNo=" + lotNo + "/quantity=" + quantity);
							int tryTimes = 3; // 嘗試連接次數
							int interval = 5000; // 等待時間
							for (int i = 0; i < tryTimes; i++) { // 如果更新失敗，經過5秒後重試
								try {
									if (disallowMinusStock) { // 不允許負庫存
										log.info("disallowMinusStock... itemCode = " + itemCode + " and warehouseCode = " + warehouseCode + " and quantity = " + quantity);
										imOnHandDAO.updateMoveUncommitQuantity(organizationCode, itemCode, warehouseCode, lotNo,
												quantity, employeeCode, brandCode);
									} else { // 允許負庫存
										log.info("allowMinusStock... itemCode = " + itemCode + " and warehouseCode = " + warehouseCode + " and quantity = " + quantity);
										imOnHandDAO.updateMoveUncommitQuantityAllowMinus(organizationCode, itemCode,
												warehouseCode, lotNo, quantity, employeeCode, brandCode);

									}
									break;
								} catch (Exception e) {
									try {
										Thread.sleep(interval);
									} catch (InterruptedException ie) {
										ie.printStackTrace();
									}
									if (i == tryTimes - 1) {
										messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單庫存時(updateImOnHandByMap."
												+ disallowMinusStock + ")發生錯誤，原因:" + e.getMessage(), employeeCode, errorMsgs);
										//throw e;
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				messageHandle(MessageStatus.LOG_ERROR, identification, "更新調撥單庫存時(updateImOnHandByMap." + disallowMinusStock
						+ ")發生錯誤，原因:" + ex.getMessage(), employeeCode, errorMsgs);
				// throw new NoSuchObjectException(ex.getMessage()+"");
				//throw ex;
			}
		}
		*/
	    
	    // 103-06-23 Mark add soCmLockRetry
		private void soCmLockRetry(Map cmLockedList, String brandCode, Map isServiceItemMap, CmDeclarationOnHandDAO cmDeclarationOnHandDAO, String employeeCode, 
				Map allowWholeSale) throws Exception {
			Iterator lcm = cmLockedList.values().iterator();
			// ======================================預扣報單庫存量=======================================================
			
			while (lcm.hasNext()) {
			    
				int tryTimes = 3; // 嘗試連接次數
				int interval = 2000; // 等待時間
				for (int i = 0; i < tryTimes; i++) {
					try {
					    log.info("跑跑跑-------"+i);
						
					    Map.Entry cmEntry = (Map.Entry) lcm.next();						
						Double outUnCommitQty = (Double) cmEntry.getValue();
						
						String[] cmkeyArray = StringUtils.delimitedListToStringArray(
							(String) cmEntry.getKey(), "{$}");
						
						log.info("品牌(" + brandCode + ")、報關單號("
							    + cmkeyArray[0] + ")、報關項次(" + cmkeyArray[1]
							             							    + ")、海關料號(" + cmkeyArray[2] + ")、關別("
							             							    + cmkeyArray[3] + ") 其中有一項並未填寫");
						
						if (!StringUtils.hasText(cmkeyArray[0])
							|| NumberUtils.getLong((cmkeyArray[1])) == 0
							|| !StringUtils.hasText(cmkeyArray[2])
							|| !StringUtils.hasText(cmkeyArray[3]))
						    throw new Exception("品牌(" + brandCode + ")、報關單號("
							    + cmkeyArray[0] + ")、報關項次(" + cmkeyArray[1]
							    + ")、海關料號(" + cmkeyArray[2] + ")、關別("
							    + cmkeyArray[3] + ") 其中有一項並未填寫");
						if (!"Y".equals((String) isServiceItemMap.get(cmkeyArray[2]))) {
						    cmDeclarationOnHandDAO.updateOutUncommitQty(cmkeyArray[0],
							    NumberUtils.getLong(cmkeyArray[1]), cmkeyArray[2],
							    cmkeyArray[3], brandCode, outUnCommitQty,
							    employeeCode, (String) allowWholeSale
								    .get(cmkeyArray[2]));
						}
				    } catch (Exception e) {
						try {
							Thread.sleep(interval);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						if (i == tryTimes - 1) {
							
							throw e;
						}
					}
				}
			}			
		}
		
	    // 103-06-25 Mark add soImLockRetry
		private void soImLockRetry(Map imlockedlist, Map isServiceItemMap, ImOnHandDAO imOnHandDAO, String organizationCode, String brandCode,
				SoSalesOrderHead soSalesOrderHead, String employeeCode) throws Exception {
			
			Iterator lim = imlockedlist.values().iterator();
			
			// ======================================預扣實體庫別庫存量=======================================================
			while (lim.hasNext()) {
				
				int tryTimes = 3; // 嘗試連接次數
				int interval = 2000; // 等待時間
				for (int i = 0; i < tryTimes; i++) {
					Map.Entry entry = (Map.Entry) lim.next();
				    try {			    
					String[] keyArray = StringUtils.delimitedListToStringArray(
						(String) entry.getKey(), "{$}");
					if (!"Y".equals((String) isServiceItemMap.get(keyArray[0]))) {
						log.info("已進入4444444");
						List<ImOnHand> lockedOnHands = null;
					    try {
						lockedOnHands = imOnHandDAO.getLockedOnHand(
							organizationCode, keyArray[0], keyArray[1],
							keyArray[2], brandCode);
					    } catch (CannotAcquireLockException cale) {
						throw new FormException("品牌(" + brandCode + ")、品號("
							+ keyArray[0] + ")、庫別(" + keyArray[1] + ")、批號("
							+ keyArray[2] + ")已鎖定，請稍後再試！");
					    }
					    if (lockedOnHands != null && lockedOnHands.size() > 0) {
						ImOnHand onHandPO = (ImOnHand) lockedOnHands.get(0);
						Double availableQuantity = onHandPO.getStockOnHandQty()
							- onHandPO.getOutUncommitQty()
							+ onHandPO.getInUncommitQty()
							+ onHandPO.getMoveUncommitQty()
							+ onHandPO.getOtherUncommitQty();
						// 如果數量超過庫存 且不是POS以及允許全賣出
						if ((Double) entry.getValue() > availableQuantity
							&& (!(POSTYPECODE.equals(soSalesOrderHead
								.getOrderTypeCode())))) {
						    throw new ValidationErrorException("品牌("
							    + brandCode + ")、品號(" + keyArray[0]
							    + ")、庫別(" + keyArray[1] + ")、批號("
							    + keyArray[2] + ")可用庫存量不足！");
						} else {
						    if (!POSTYPECODE.equals(soSalesOrderHead
							    .getOrderTypeCode())) {
							imOnHandDAO.bookAvailableQuantity(
								lockedOnHands, (Double) entry
									.getValue(), "FIFO", employeeCode);
						    } else {
							imOnHandDAO.bookQuantity(lockedOnHands,
								(Double) entry.getValue(), "FIFO",
								employeeCode);
						    }
						}
					    } else {
						// 如果是T2POS 且 不允許全賣出
						if (!(POSTYPECODE.equals(soSalesOrderHead
							.getOrderTypeCode()))) {
						    throw new ValidationErrorException("查無品牌("
							    + brandCode + ")、品號(" + keyArray[0]
							    + ")、庫別(" + keyArray[1] + ")、批號("
							    + keyArray[2] + ")的庫存資料！");
						} else {
						    // ==========================SOP單查無onHand時新增一筆====================================
						    ImOnHandId id = new ImOnHandId();
						    ImOnHand newOnHand = new ImOnHand();
						    id.setOrganizationCode(organizationCode);
						    id.setItemCode(keyArray[0]);
						    id.setWarehouseCode(keyArray[1]);
						    id.setLotNo(keyArray[2]);
						    newOnHand.setId(id);
						    newOnHand.setBrandCode(brandCode);
						    newOnHand.setStockOnHandQty(0D);
						    newOnHand.setOutUncommitQty((Double) entry
							    .getValue());
						    newOnHand.setInUncommitQty(0D);
						    newOnHand.setMoveUncommitQty(0D);
						    newOnHand.setOtherUncommitQty(0D);
						    newOnHand.setCreatedBy(employeeCode);
						    newOnHand.setCreationDate(new Date());
						    newOnHand.setLastUpdatedBy(employeeCode);
						    newOnHand.setLastUpdateDate(new Date());
						    imOnHandDAO.save(newOnHand);
						}
					    }
					}
				    } catch (Exception e) {
						try {
							Thread.sleep(interval);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						if (i == tryTimes - 1) {
							// messageHandle(MessageStatus.LOG_ERROR,
							// identification,
							// "更新調撥單庫存時(updateImOnHandByMap."
							// + "" + ")發生錯誤，原因:" + e.getMessage(),
							// employeeCode, errorMsgs);
							throw e;
						}
					}
				}
			}			
		}
	
}
