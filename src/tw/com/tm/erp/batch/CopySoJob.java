package tw.com.tm.erp.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.ImDeliveryLine;
import tw.com.tm.erp.hbm.bean.ImItem;
import tw.com.tm.erp.hbm.bean.ImWarehouse;
import tw.com.tm.erp.hbm.bean.SiResend;
import tw.com.tm.erp.hbm.bean.SiResendId;
import tw.com.tm.erp.hbm.bean.SoSalesOrderHead;
import tw.com.tm.erp.hbm.bean.SoSalesOrderItem;
import tw.com.tm.erp.hbm.bean.SoSalesOrderPayment;
import tw.com.tm.erp.hbm.dao.CmDeclarationOnHandDAO;
import tw.com.tm.erp.hbm.dao.ImDeliveryLineDAO;
import tw.com.tm.erp.hbm.dao.ImItemDAO;
import tw.com.tm.erp.hbm.dao.ImOnHandDAO;
import tw.com.tm.erp.hbm.dao.SiResendDAO;
import tw.com.tm.erp.hbm.dao.SoSalesOrderHeadDAO;
import tw.com.tm.erp.hbm.dao.UploadControlDAO;
import tw.com.tm.erp.hbm.service.ImWarehouseService;


public class CopySoJob {
	
	private static ApplicationContext context = SpringUtils.getApplicationContext();
	
	
	
	OutputStream output = null;
	InputStream is = null;
	Properties config = new Properties();
	final String CONFIG_FILE = "D:/Java/jboss-4.0.5.GA/server/ceap/deploy/erp.ear/erp.war/WEB-INF/classes/controller.properties";
	
	
	public void loadConfig() throws Exception {
		try {
			File f = new File(CONFIG_FILE);
	        is = new FileInputStream( f );
	        config.load( is );
	        is.close();
			System.out.println("讀取 "+CONFIG_FILE+" 設定檔完成！");
		} catch (IOException ex) {
			throw new Exception("讀取 "+CONFIG_FILE+" 設定檔失敗！");
		}
	}
	
	public void saveConfig() throws Exception {
		try {
			File f = new File(CONFIG_FILE);
			FileOutputStream out = new FileOutputStream( f );
	        config.store(out, null);
	        out.close();
			System.out.println("儲存 "+CONFIG_FILE+" 設定檔完成！");
		} catch (IOException ex) {
			throw new Exception("儲存 "+CONFIG_FILE+" 設定檔失敗！");
		}
	}
	
	public void saveCopySO()throws Exception{
		UploadControlDAO uploadControlDAO = (UploadControlDAO) context.getBean("uploadControlDAO");
		SiResendDAO siResendDAO = (SiResendDAO) context.getBean("siResendDAO");
		SoSalesOrderHeadDAO soSalesOrderHeadDAO = (SoSalesOrderHeadDAO) context.getBean("soSalesOrderHeadDAO");
		//get controller
		loadConfig();
		String control = config.getProperty("controller");
		//boolean flag = false;
		
		if(control.equals("0")){
			//flag = 1
			config.setProperty("controller", "1");
			saveConfig();
			/*
			//取 98.101 SO 資料
			int maxResults = 500;
			List<SoSalesOrderHead> salesHeads = uploadControlDAO.getSoSalesOrderHeadsSetMax(maxResults);
			System.out.println("正式機銷售.size():"+salesHeads.size());
			
			for(SoSalesOrderHead so:salesHeads){
				try{
					String organizationCode = "TM";
			    	System.out.println("=========SO_SALE_DATE=="+so.getSalesOrderDate()+"=======SO_DATAID=="+so.getReserve4()+"=======SO_TransactionSeqNo=="+so.getTransactionSeqNo());
			    	
			    	HashMap parameterMap = new HashMap();
					parameterMap.put("BRAND_CODE", so.getBrandCode());
					parameterMap.put("salesOrderDate", so.getSalesOrderDate());
					parameterMap.put("MACHINE_CODE", so.getPosMachineCode());
					parameterMap.put("transactionSeqNo", so.getTransactionSeqNo());
					
					List<SoSalesOrderHead> old_SoHeads = soSalesOrderHeadDAO.findOnlineSoHead(parameterMap);  //判斷95.101銷售單是否存在
					try{
						if (old_SoHeads.size()!= 0) {
							System.out.println("銷售資料存在,transactionSEQno:"+old_SoHeads.get(0).getTransactionSeqNo()+",判斷狀態");
							if(so.getStatus().equals(OrderStatus.VOID)){
								for(SoSalesOrderHead so_old:old_SoHeads){
									if(so_old.getStatus().equals(OrderStatus.SIGNING)){
										saveToOriginallyAvailableQuantity(so_old,organizationCode, "POS");
										so_old.setStatus(OrderStatus.VOID);
										soSalesOrderHeadDAO.update(so_old);
									}
								}
							}
						}else{
							//flag = true;
							System.out.println("測試環境 此筆正式機銷售資料不存在:");
							
							SiResendId id = new SiResendId();
							id.setOrderTypeCode(so.getOrderTypeCode());
							id.setOrderNo(so.getOrderNo());
							SiResend siResend = siResendDAO.findById(id);
							
							if(so.getStatus().equals(OrderStatus.VOID)){*/
								/*if(siResend==null){
									SoSalesOrderHead so_new = so;
									so_new.setStatus(OrderStatus.UNCONFIRMED);
									soSalesOrderHeadDAO.save(so_new);
									for(SoSalesOrderItem soLine:so.getSoSalesOrderItems()){
										soLine.setSoSalesOrderHead(so_new);
										soSalesOrderHeadDAO.save(soLine);
									}
									for(SoSalesOrderPayment soPay:so.getSoSalesOrderPayments()){
										soPay.setSoSalesOrderHead(so_new);
										soSalesOrderHeadDAO.save(soPay);
									}
								}else{*/
									//saveToOriginallyAvailableQuantity(so,organizationCode, "POS");
			/*
									SoSalesOrderHead so_new = so;
									so_new.setStatus(OrderStatus.VOID);
									soSalesOrderHeadDAO.save(so_new);
									for(SoSalesOrderItem soLine:so.getSoSalesOrderItems()){
										soLine.setSoSalesOrderHead(so_new);
										soSalesOrderHeadDAO.save(soLine);
									}
									for(SoSalesOrderPayment soPay:so.getSoSalesOrderPayments()){
										soPay.setSoSalesOrderHead(so_new);
										soSalesOrderHeadDAO.save(soPay);
									}
								//}
							}else{
								so.setStatus(OrderStatus.UNCONFIRMED);
								//存 95.101 SO 資料
								if(siResend!=null){
									siResend.setStatus("NOTOK");
									siResendDAO.update(siResend);
								}else{
									System.out.println("+++++++SAVE++++++++++++::"+so.getHeadId());
									SoSalesOrderHead so_new = so;
									so_new.setStatus(OrderStatus.UNCONFIRMED);
									soSalesOrderHeadDAO.save(so_new);
									for(SoSalesOrderItem soLine:so.getSoSalesOrderItems()){
										soLine.setSoSalesOrderHead(so_new);
										soSalesOrderHeadDAO.save(soLine);
									}
									for(SoSalesOrderPayment soPay:so.getSoSalesOrderPayments()){
										soPay.setSoSalesOrderHead(so_new);
										soSalesOrderHeadDAO.save(soPay);
									}
									System.out.println("==============SAVE_SO====================headId:"+so.getHeadId());
									
									SiResend siResend_new = new SiResend();
									SiResendId id_new = new SiResendId();						
									siResend_new.setHeadId(so.getHeadId());
									id_new.setOrderNo(so.getOrderNo());
									id_new.setOrderTypeCode(so.getOrderTypeCode());
									siResend_new.setId(id_new);
									siResend_new.setIsLock("Y");
									siResend_new.setStatus("NOTOK");
									System.out.println("==============siResend====================siResend:"+siResend_new.getId());
									siResendDAO.save(siResend_new);
									System.out.println("==============SAVE_siResend====================siResend:"+siResend_new.getId());
								}
							}
						}
						
						//回存正式機 reserve2 = ""*/
						/*so.setReserve2("");
						uploadControlDAO.updateData(so);*/
						//if(flag){
					/*		Connection conn = null;
							try{
								Class.forName("oracle.jdbc.driver.OracleDriver");
								conn = DriverManager.getConnection("jdbc:oracle:thin:@10.0.98.64:1521:KWEDB1","KWE_ERP","1QAZ8IK,!@#$");
								Statement stat = conn.createStatement();
								stat.executeUpdate(" update ERP.SO_SALES_ORDER_HEAD H set RESERVE2 = '' where H.POS_MACHINE_CODE = '"+so.getPosMachineCode()+"' and H.BRAND_CODE='T2' and H.TRANSACTION_SEQ_NO='"+so.getTransactionSeqNo()+"'");
							}catch(Exception e){
								e.printStackTrace();
							}finally{
								if(conn!= null){
									conn.close();
								}
							}
						//}
						
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("銷售資料單據狀態為"+so.getStatus()+":儲存單據 "+so.getOrderTypeCode()+so.getOrderNo()+" 時發生異常");
						throw e;
					}
				}catch(Exception e){
					System.out.println("copy 【正式機】銷售單據 "+so.getOrderTypeCode()+so.getOrderNo()+" 至【測試機】時發生異常：");
					e.printStackTrace();
				}
			}
			
			//flag = 0
			config.put("controller", "0");
			saveConfig();
		}else{
			System.out.println("使用中");*/
		}
		
				
		//新增SiResend		
	}	
	
	/**
     * 回復至原始可用庫存，並將出貨單明細刪除
     * 
     * @param salesOrderHead
     * @param organizationCode
     * @param loginUser
     * @throws FormException
     * @throws NoSuchDataException
     */
    public void saveToOriginallyAvailableQuantity(
	    SoSalesOrderHead salesOrderHead, String organizationCode,
	    String loginUser) throws FormException, Exception {
    	ImDeliveryLineDAO imDeliveryLineDAO = (ImDeliveryLineDAO) context.getBean("imDeliveryLineDAO");
    	ImItemDAO imItemDAO = (ImItemDAO) context.getBean("imItemDAO");
    	ImWarehouseService imWarehouseService = (ImWarehouseService) context.getBean("imWarehouseService");
    	CmDeclarationOnHandDAO cmDeclarationOnHandDAO = (CmDeclarationOnHandDAO) context.getBean("cmDeclarationOnHandDAO");
    	ImOnHandDAO imOnHandDAO = (ImOnHandDAO) context.getBean("imOnHandDAO");
	String brandCode = salesOrderHead.getBrandCode();
	Long SalesOrderHeadId = salesOrderHead.getHeadId();
	List imDeliveryLines = imDeliveryLineDAO.findByProperty(
		"ImDeliveryLine", "salesOrderId", SalesOrderHeadId);
	if (imDeliveryLines != null && imDeliveryLines.size() > 0) {
	    for (int i = 0; i < imDeliveryLines.size(); i++) {
		ImDeliveryLine deliveryLine = (ImDeliveryLine) imDeliveryLines
			.get(i);
		// 非服務性商品補回庫存
		if (!"Y".equals(deliveryLine.getIsServiceItem())) {
		    String itemTaxCode = deliveryLine.getIsTax();
		    String itemCode = deliveryLine.getItemCode();
		    String warehouseCode = deliveryLine.getWarehouseCode();
		    String customsWarehouseCode = null;
		    Double salesQuantity = deliveryLine.getSalesQuantity();
		    if (!StringUtils.hasText(itemTaxCode)) {
			ImItem itemPO = imItemDAO.findItem(brandCode, itemCode);
			if (itemPO == null) {
			    throw new NoSuchObjectException("依據品牌(" + brandCode
				    + ")、品號(" + itemCode + ")查無商品相關資料！");
			} else {
			    itemTaxCode = itemPO.getIsTax();
			    if (!StringUtils.hasText(itemTaxCode))
				throw new ValidationErrorException("品牌("
					+ brandCode + ")、品號(" + itemCode
					+ ")的稅別未設定！");
			}
		    }
		    System.out.println("這邊開始退貨 itemTaxCode=" + itemTaxCode);
		    if ("F".equals(itemTaxCode)) {
			ImWarehouse warehousePO = imWarehouseService
				.findByBrandCodeAndWarehouseCode(brandCode,
					warehouseCode, null);
			if (warehousePO == null) {
			    throw new NoSuchObjectException("依據品牌(" + brandCode
				    + ")、庫別(" + warehouseCode + ")查無庫別相關資料！");
			} else {
			    customsWarehouseCode = warehousePO
				    .getCustomsWarehouseCode();
			    if (!StringUtils.hasText(customsWarehouseCode))
				throw new ValidationErrorException("庫別("
					+ warehouseCode + ")的海關關別未設定！");
			}
			cmDeclarationOnHandDAO.updateOutUncommitQuantity(
				deliveryLine.getImportDeclNo(), deliveryLine
					.getImportDeclSeq(), itemCode,
				customsWarehouseCode, brandCode, salesQuantity,
				loginUser);
		    }
		    imOnHandDAO.updateOutUncommitQuantity(organizationCode,
			    itemCode, warehouseCode, deliveryLine.getLotNo(),
			    salesQuantity, loginUser, brandCode);
		}
		imDeliveryLineDAO.delete(deliveryLine);
	    }
	} else {
	    throw new NoSuchObjectException("依據銷貨單主檔主鍵：" + SalesOrderHeadId
		    + "查無相關出貨單明細檔資料！");
	}
    }
	
}
