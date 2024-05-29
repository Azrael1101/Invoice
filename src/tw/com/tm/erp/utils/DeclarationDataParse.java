package tw.com.tm.erp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;
import tw.com.tm.erp.hbm.bean.CmDeclarationItem;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.CmDeclarationHeadDAO;
import tw.com.tm.erp.hbm.service.CmDeclarationHeadService;

public class DeclarationDataParse {
	
	private static final Log log = LogFactory.getLog(DeclarationDataParse.class);
	
	private CmDeclarationHeadDAO  cmDeclarationHeadDAO;
	
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	
	static Map decl5105A1 = new LinkedHashMap();
	static Map decl5105A4 = new LinkedHashMap();
	
	static Map decl5203A1 = new LinkedHashMap();
	static Map decl5203A4 = new LinkedHashMap();
	
	//static Map decl5105S1 = new HashMap();
	//static Map decl5105S4 = new HashMap();
	
	static Map releaseDateTime = new LinkedHashMap();
	
	String processName = "DECLARATION_PARSE";
	Date executeDate = new Date();
	String uuid = null;
	
	public void execute(){
		set5105A();
		//set5105S();
		set5203A();
		setReleaseDateTime();
		
		uuid = UUID.randomUUID().toString();
		try{
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "報關轉檔批次匯入程序開始執行...", executeDate, uuid, "SYS");
			List<BuCommonPhraseLine> DeclarationDataParses = buCommonPhraseLineDAO.findEnableLineById("DeclarationDataParse");
			if(DeclarationDataParses == null || DeclarationDataParses.size() == 0){
				throw new Exception("查無報關轉檔路徑資料");
			}
			BuCommonPhraseLine DeclarationDataParse = DeclarationDataParses.get(0);
			String path =  DeclarationDataParse.getAttribute1();
			String path_bak =  DeclarationDataParse.getAttribute1()+"_"+DeclarationDataParse.getAttribute2();
			String path_err =  DeclarationDataParse.getAttribute1()+"_"+DeclarationDataParse.getAttribute3();
			File folder = new File(path);
			File folder_bak = new File(path_bak);
			File folder_err = new File(path_err);
			if(!folder_bak.isDirectory())
				folder_bak.mkdir();
			if(!folder_err.isDirectory())
				folder_err.mkdir();
			System.out.println("path:"+path);
			System.out.println("folder.isDirectory():"+folder.isDirectory());
			if(folder.isDirectory()){
				String[] list = folder.list();
				// 轉報關單 5105,5203
				for (int i = 0; i < list.length; i++) {
					BufferedReader in = new BufferedReader(new FileReader(folder+"\\"+list[i]));
					if(list[i].indexOf("5105") > -1 || list[i].indexOf("5203") > -1){
						System.out.println("5105,5203");
						try{
							//進出倉別進口設一，出口設二
							if(list[i].indexOf("5105") > -1){
								System.out.println("5105");
								executeRead(in,decl5105A1,decl5105A4,false,false);
							}else if(list[i].indexOf("5203") > -1){
								System.out.println("5203");
								executeRead(in,decl5203A1,decl5203A4,false,false);
							}
							in.close();
							File declarationFile = new File(path+"\\"+list[i]);
							File declarationFileBak = new File(path_bak+"\\"+list[i]);
							if(declarationFileBak.exists())
								declarationFileBak.delete();
							declarationFile.renameTo(declarationFileBak);
						}catch (Exception e){
							SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "報關轉檔批次匯入發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
							log.error(e.getMessage());
							in.close();
							File declarationFile = new File(path+"\\"+list[i]);
							File declarationFileErr = new File(path_err+"\\"+list[i]);
							if(declarationFileErr.exists())
								declarationFileErr.delete();
							declarationFile.renameTo(declarationFileErr);
							throw e;
						}
					}else{
						in.close();
					}
				}
			}
			
			File folder2 = new File(path);
			File folder_bak2 = new File(path_bak);
			File folder_err2 = new File(path_err);
			if(!folder_bak2.isDirectory())
				folder_bak2.mkdir();
			if(!folder_err2.isDirectory())
				folder_err2.mkdir();
			if(folder2.isDirectory()){
				String[] list2 = folder2.list();
				// 轉放行日期 5116,5204
				for (int i = 0; i < list2.length; i++) {
					BufferedReader in = new BufferedReader(new FileReader(folder+"\\"+list2[i]));
					if(list2[i].indexOf("5116") > -1 || list2[i].indexOf("5204") > -1){
						System.out.println("5116,5204");
						try{
							executeRead(in,releaseDateTime,null,false,true);
							in.close();
							File declarationFile = new File(path+"\\"+list2[i]);
							File declarationFileBak = new File(path_bak+"\\"+list2[i]);
							if(declarationFileBak.exists())
								declarationFileBak.delete();
							declarationFile.renameTo(declarationFileBak);
						}catch (Exception e){
							SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "報關轉檔批次匯入發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
							log.error(e.getMessage());
							in.close();
							File declarationFile = new File(path+"\\"+list2[i]);
							File declarationFileErr = new File(path_bak+"\\"+list2[i]);
							if(declarationFileErr.exists())
								declarationFileErr.delete();
							declarationFile.renameTo(declarationFileErr);
						}
					}else{
						in.close();
					}
				}
			}
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "報關轉檔批次匯入程序執行完成...", executeDate, uuid, "SYS");
		}catch(Exception e){
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "報關轉檔批次匯入發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
			log.error(e.getMessage());
		}
	}
	
	public String execute(BuCommonPhraseLine DeclarationDataParse, String path, String[] files) throws Exception{
		String returnValue = "匯入成功";
		SiProgramLogAction siProgramLogAction = (SiProgramLogAction) SpringUtils.getApplicationContext().getBean("siProgramLogAction");
		set5105A();
		//set5105S();
		set5203A();
		setReleaseDateTime();
		
		uuid = UUID.randomUUID().toString();
		File folder = new File(path);
		if(folder.isDirectory()){
			// 轉報關單 5105,5203
			for (int i = 0; i < files.length; i++) {
				BufferedReader in = new BufferedReader(new FileReader(folder+"\\"+files[i]));
				if(files[i].indexOf("5105") > -1 || files[i].indexOf("5203") > -1){
					try{
						//進出倉別進口設一，出口設二
						if(files[i].indexOf("5105") > -1){
							executeRead(in,decl5105A1,decl5105A4,true,false);
						}else if(files[i].indexOf("5203") > -1){
							executeRead(in,decl5203A1,decl5203A4,true,false);
						}
						in.close();
					}catch (Exception e){
						siProgramLogAction.createProgramLog("DeclarationImportDataT2", MessageStatus.LOG_ERROR, "DeclarationImportDataT2", e.getMessage(), "SYS");
						log.error(e.getMessage());
						in.close();
						returnValue = "匯入未完成，原因：" + e.getMessage();
						return returnValue;
					}
				}else{
					in.close();
				}
			}
			
			// 轉放行日期 5116,5204
			for (int i = 0; i < files.length; i++) {
				BufferedReader in = new BufferedReader(new FileReader(folder+"\\"+files[i]));
				try{
					if(files[i].indexOf("5116") > -1 || files[i].indexOf("5204") > -1){
						executeRead(in,releaseDateTime,null,true,true);
					}
					in.close();
				}catch (Exception e){
					siProgramLogAction.createProgramLog("DeclarationImportDataT2", MessageStatus.LOG_ERROR, "DeclarationImportDataT2", e.getMessage(), "SYS");
				    log.error(e.getMessage());
					in.close();
					returnValue = "匯入未完成，原因：" + e.getMessage();
					return returnValue;
				}
			}
		}
		return returnValue;
	}
	
    /**
     * 將報關文字檔做解析
     *
     * @param in 報關文字檔
     * @param keyMap 文字檔對應參數
     * @param forceImport 是否強制起一個流程
     * @param releaseDateTime 是否為追加放行時間
     * @throws Exception
     */
	public void executeRead(BufferedReader in, Map keyMap, Map lineMap, boolean forceImport, boolean releaseDateTime) throws Exception{
		System.out.println("executeRead");
		String declNo = "";
		try{
//MACO 2016.01.30 superviseCode
			String superviseCodeT2=buCommonPhraseLineDAO.findById("SuperviseCode","T2Warehouse").getName();
			Iterator it;
	        String line;
	        StringBuffer sb = new StringBuffer();
			CmDeclarationHead head = new CmDeclarationHead();
			List<CmDeclarationItem> items = new ArrayList<CmDeclarationItem>();
			Long indexNo = 1L;
	        while((line = in.readLine())!=null){
	        	System.out.println("BufferedReader not null");
	        	byte[] a = line.getBytes();
	        	sb.append(line);
	        	switch (sb.charAt(1)) {
				case '1':
					System.out.println("sb.charAt(1) = 1");
					it = keyMap.keySet().iterator();
					while (it.hasNext()) {
						Object object = (Object)it.next();
						Integer[] i = (Integer[])keyMap.get(object);
						byte[] b = new byte[i[1]-i[0]];
			        	System.arraycopy(a, i[0], b, 0, i[1]-i[0]);
			        	String s = new String(b).trim();
			        	if("declNo".equals(object)){
			        		System.out.println("declNo = "+ s);
			        		declNo = s;
			        		List<CmDeclarationHead> cmDeclarationHeads = cmDeclarationHeadDAO.findByProperty("CmDeclarationHead", "declNo", declNo);
	        				if(!releaseDateTime){
				        		if(cmDeclarationHeads != null && cmDeclarationHeads.size() > 0){
				        			head = cmDeclarationHeads.get(0);
			        					if(OrderStatus.SAVE.equals(head.getStatus())){
			        						//如果不是追加放行日期的話，代表報關單重新匯入，將會清除明細
			        						List<CmDeclarationItem> cmDeclarationItems = head.getCmDeclarationItems();
			        						for (Iterator iterator = cmDeclarationItems.iterator(); iterator.hasNext();) {
			        							CmDeclarationItem cmDeclarationItem = (CmDeclarationItem) iterator.next();
			        							cmDeclarationHeadDAO.delete(cmDeclarationItem);
			        						}
			        					}else{
			        						throw new Exception("報單號碼"+declNo+"已存在，且狀態不為" + OrderStatus.SAVE + "無法異動");
			        					}
				        		}
	        				}else{
	        					if(cmDeclarationHeads != null && cmDeclarationHeads.size() > 0)
				        			head = cmDeclarationHeads.get(0);
	        					else
	        						throw new Exception("查無報單號碼"+declNo+"於系統中無法更新放行日期");
	        				}
			        	}
			        	
			        	Class type = PropertyUtils.getPropertyType(head,(String)object);
			        	if(StringUtils.hasText(s)){
				    		if (type == String.class)
				    			PropertyUtils.setNestedProperty(head, (String)object, s);
				    		else if(type == Long.class)
				    			PropertyUtils.setNestedProperty(head, (String)object, NumberUtils.getLong(s));
				    		else if(type == Double.class)
				    			PropertyUtils.setNestedProperty(head, (String)object, NumberUtils.getDouble(s));
				    		else if(type == Date.class){
				    			Date d = null;
				    			if(s.trim().length() == 12 )
				    				d = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,s.substring(0,8));
				    			else if(s.trim().length() == 8 )
				    				d = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,s);
				    			else
				    				d = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYMMDD,s);
				    			PropertyUtils.setNestedProperty(head, (String)object, d);
				    		}
			    		}
					}
					break;
				case '4':
					System.out.println("sb.charAt(1) = 4");
					CmDeclarationItem item = new CmDeclarationItem();
					it = lineMap.keySet().iterator();
					while (it.hasNext()) {
						Object object = (Object) it.next();
						Integer[] i = (Integer[])lineMap.get(object);
						byte[] b = new byte[i[1]-i[0]];
			        	System.arraycopy(a, i[0], b, 0, i[1]-i[0]);
			        	String s = new String(b).trim();
			        	Class type = PropertyUtils.getPropertyType(item,(String)object);
			        	if(StringUtils.hasText(new String(b))){
				    		if (type == String.class)
				    			PropertyUtils.setNestedProperty(item, (String)object, s);
				    		else if(type == Long.class)
				    			PropertyUtils.setNestedProperty(item, (String)object, NumberUtils.getLong(s));
				    		else if(type == Double.class)
				    			PropertyUtils.setNestedProperty(item, (String)object, NumberUtils.getDouble(s));
				    		else if(type == Date.class){
				    			Date d = DateUtils.parseDate(DateUtils.C_DATA_PATTON_YYYYMMDD,s);
			    				PropertyUtils.setNestedProperty(item, (String)object, d);
				    		}
			    		}
					}
					item.setCmDeclarationHead(head);
					item.setDeclNo(declNo);
					//如果是D1或是D8報單，自動往下帶
					if("D1".equals(head.getDeclType()) || "D8".equals(head.getDeclType()))
						item.setODeclDate(head.getDeclDate());
					if(StringUtils.hasText(head.getCurrencyCode()) && "TWD".equals(head.getCurrencyCode()))
						head.setCurrencyCode("NTD");
					item.setLastUpdatedBy("SYS");
					item.setLastUpdateDate(new Date());
					item.setCreatedBy("SYS");
					item.setCreationDate(new Date());
					item.setIndexNo(indexNo++);
					items.add(item);
					break;
				default:
					break;
				}
	        	sb.delete(0,sb.length());
	        }
	        
	        log.info("此筆 declNo = " + head.getDeclNo());
	        
	        //如果是D1報單，轉入時進口日期
			if("D1".equals(head.getDeclType())||"D5".equals(head.getDeclType())||"B2".equals(head.getDeclType()))
				head.setImportDate(head.getDeclDate());
			
			//如果是賣方，進出倉別就設2
			if(superviseCodeT2.equals(head.getOutbondNo()) || superviseCodeT2.equals(head.getExWhNo())){
				head.setStrType("2");
				head.setBondNo(superviseCodeT2);
			//如果是買方進出倉別就設1
			}else if(superviseCodeT2.equals(head.getInbondNo()) || superviseCodeT2.equals(head.getImWhNo())){
					head.setStrType("1");
					head.setBondNo(superviseCodeT2);
			}/*else if(true){
				log.info("head.getOutbondNo():"+head.getOutbondNo());
				log.info("head.getImWhNo():"+head.getImWhNo());
				log.info("head.getExWhNo():"+head.getExWhNo());
			}*/
			
			head.setLastUpdatedBy("SYS");
			head.setLastUpdateDate(new Date());
			head.setCreatedBy("SYS");
			head.setCreationDate(new Date());
			if(!releaseDateTime)
				head.setCmDeclarationItems(items);
			
			if(OrderStatus.SAVE.equals(head.getStatus()) || releaseDateTime){
			    cmDeclarationHeadDAO.update(head);
			}else if(forceImport && !releaseDateTime){
				head.setStatus(OrderStatus.SAVE);
			    cmDeclarationHeadDAO.save(head);
			    CmDeclarationHeadService.startProcess(head);
			}else if(StringUtils.hasText(head.getBondNo())){
			    head.setStatus(OrderStatus.SAVE);
			    cmDeclarationHeadDAO.save(head);
			    CmDeclarationHeadService.startProcess(head); 
			}else if(head.getDeclType().equals("G1")||head.getDeclType().equals("G3")){
				head.setStatus(OrderStatus.SAVE);
			    cmDeclarationHeadDAO.save(head);
			    CmDeclarationHeadService.startProcess(head);
			}
		}catch(Exception e){
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "報關轉檔批次匯入單號"+declNo+"發生錯誤，原因 ："+e.getMessage(), executeDate, uuid, "SYS");
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception("匯入報關單號"+declNo+"發生錯誤，原因 ："+e.getMessage());
		}
	}
	
	public static void set5105A(){
		
		decl5105A1.put("declNo",new Integer[]{2,16});
		decl5105A1.put("msgFun",new Integer[]{16,19});
		decl5105A1.put("declType",new Integer[]{19,21});
		decl5105A1.put("stgPlace",new Integer[]{22,30});
		decl5105A1.put("shipPort",new Integer[]{30,35});
		decl5105A1.put("countryCode",new Integer[]{35,37});
		decl5105A1.put("exportDate",new Integer[]{53,61});
		decl5105A1.put("importDate",new Integer[]{61,69});
		decl5105A1.put("declDate",new Integer[]{69,77});
		decl5105A1.put("GWgt",new Integer[]{119,131});
		decl5105A1.put("mawbNo",new Integer[]{131,147});
		decl5105A1.put("hawbNo",new Integer[]{147,163});
		decl5105A1.put("originalDeclNo",new Integer[]{175,189});
		decl5105A1.put("rlsPkg",new Integer[]{331,339});
		decl5105A1.put("pkgUnit",new Integer[]{339,342});
		decl5105A1.put("voyageNo",new Integer[]{1042,1054});
		decl5105A1.put("vesselSign",new Integer[]{1056,1063});
		decl5105A1.put("boxNo",new Integer[]{1063,1067});
		decl5105A1.put("buyPayName",new Integer[]{1089,1159});
		decl5105A1.put("inbondNo",new Integer[]{1264,1269});
		decl5105A1.put("salePayName",new Integer[]{1303,1373});
		decl5105A1.put("outbondNo",new Integer[]{1478,1483});
		decl5105A1.put("relBfNo",new Integer[]{1497,1502});
		decl5105A1.put("originalFobValue",new Integer[]{1518,1533});
		decl5105A1.put("freightFee",new Integer[]{1536,1549});
		decl5105A1.put("insuranceFee",new Integer[]{1549,1562});
		decl5105A1.put("additionFee",new Integer[]{1562,1575});
		decl5105A1.put("deductionFee",new Integer[]{1575,1588});
		decl5105A1.put("originalCifValue",new Integer[]{1588,1603});
		decl5105A1.put("cifValue",new Integer[]{1603,1615});
		decl5105A1.put("currencyCode",new Integer[]{1615,1618});
		decl5105A1.put("exchangeRate",new Integer[]{1618,1627});
		decl5105A1.put("NWgt",new Integer[]{1627,1638});
		decl5105A1.put("moaType1",new Integer[]{1638,1641});
		decl5105A1.put("dutyAmt1",new Integer[]{1641,1651});
		decl5105A1.put("moaType2",new Integer[]{1651,1654});
		decl5105A1.put("dutyAmt2",new Integer[]{1654,1664});
		decl5105A1.put("moaType3",new Integer[]{1664,1667});
		decl5105A1.put("dutyAmt3",new Integer[]{1667,1677});
		decl5105A1.put("moaType4",new Integer[]{1677,1680});
		decl5105A1.put("dutyAmt4",new Integer[]{1680,1690});
		decl5105A1.put("moaType5",new Integer[]{1690,1693});
		decl5105A1.put("dutyAmt5",new Integer[]{1693,1703});
		decl5105A1.put("moaType6",new Integer[]{1703,1706});
		decl5105A1.put("dutyAmt6",new Integer[]{1706,1716});
		decl5105A1.put("moaType7",new Integer[]{1716,1719});
		decl5105A1.put("dutyAmt7",new Integer[]{1719,1729});
		decl5105A1.put("dutyAmt",new Integer[]{1755,1765});
		decl5105A1.put("totalDuty",new Integer[]{1765,1775});
		decl5105A1.put("dutyDase",new Integer[]{1775,1787});
		decl5105A1.put("sendDate",new Integer[]{1801,1807});
		decl5105A1.put("sendTime",new Integer[]{1807,1811});
		decl5105A1.put("exWhNo",new Integer[]{1917,1925});
		decl5105A1.put("imWhNo",new Integer[]{1925,1933});
		
		decl5105A4.put("itemNo",new Integer[]{2,6});
		decl5105A4.put("code",new Integer[]{6,17});
		decl5105A4.put("descrip",new Integer[]{21,411});
		decl5105A4.put("brand",new Integer[]{411,431});
		decl5105A4.put("model",new Integer[]{431,461});
		decl5105A4.put("spec",new Integer[]{461,611});
		decl5105A4.put("produceCountry",new Integer[]{651,653});
		decl5105A4.put("NWght",new Integer[]{653,664});
		decl5105A4.put("qty",new Integer[]{664,678});
		decl5105A4.put("unit",new Integer[]{678,681});
		decl5105A4.put("custValueAmt",new Integer[]{732,744});
		decl5105A4.put("unitPrice",new Integer[]{744,763});
		decl5105A4.put("currencyCode",new Integer[]{763,766});
		decl5105A4.put("unitCuritem",new Integer[]{766,769});
		decl5105A4.put("buyCommNo",new Integer[]{769,784});
		decl5105A4.put("saleCommNo",new Integer[]{784,799});
		decl5105A4.put("ODeclNo",new Integer[]{799,813});
		decl5105A4.put("OItemNo",new Integer[]{813,817});
		decl5105A4.put("permitNo",new Integer[]{817,831});
		decl5105A4.put("permitItem",new Integer[]{831,834});
		decl5105A4.put("pricduty",new Integer[]{913,923});
		decl5105A4.put("duty1",new Integer[]{943,946});
		decl5105A4.put("dutyRate1",new Integer[]{946,956});
		decl5105A4.put("duty2",new Integer[]{962,965});
		decl5105A4.put("dutyRate2",new Integer[]{965,975});
		decl5105A4.put("duty3",new Integer[]{981,984});
		decl5105A4.put("dutyRate3",new Integer[]{984,994});
		decl5105A4.put("duty4",new Integer[]{1000,1003});
		decl5105A4.put("dutyRate4",new Integer[]{1003,1013});
		decl5105A4.put("assGoodNo",new Integer[]{1114,1264});
	}

	public static void set5203A(){

		decl5203A1.put("declNo",new Integer[]{2,16});
		decl5203A1.put("msgFun",new Integer[]{16,19});
		decl5203A1.put("declType",new Integer[]{19,21});
		decl5203A1.put("stgPlace",new Integer[]{22,30});
		decl5203A1.put("declDate",new Integer[]{58,66});
		decl5203A1.put("GWgt",new Integer[]{106,118});
		decl5203A1.put("rlsPkg",new Integer[]{352,360});
		decl5203A1.put("pkgUnit",new Integer[]{360,363});
		decl5203A1.put("voyageNo",new Integer[]{923,935});
		decl5203A1.put("transType",new Integer[]{935,937});
		decl5203A1.put("vesselSign",new Integer[]{937,944});
		decl5203A1.put("boxNo",new Integer[]{944,948});
		decl5203A1.put("exporterId",new Integer[]{953,967});
		decl5203A1.put("buyPayName",new Integer[]{970,1040});
		decl5203A1.put("outbondNo",new Integer[]{1145,1150});
		decl5203A1.put("buyBan",new Integer[]{1167,1181});
		decl5203A1.put("salePayName",new Integer[]{1184,1219});
		decl5203A1.put("inbondNo",new Integer[]{1359,1364});
		decl5203A1.put("exWhBan",new Integer[]{1377,1385});
		decl5203A1.put("imWhBan",new Integer[]{1385,1393});
		decl5203A1.put("fobValue",new Integer[]{1393,1405});
		decl5203A1.put("originalFobValue",new Integer[]{1405,1420});
		decl5203A1.put("currencyCode",new Integer[]{1420,1423});
		decl5203A1.put("freightFee",new Integer[]{1423,1436});
		decl5203A1.put("insuranceFee",new Integer[]{1436,1449});
		decl5203A1.put("additionFee",new Integer[]{1449,1462});
		decl5203A1.put("deductionFee",new Integer[]{1462,1475});
		decl5203A1.put("exchangeRate",new Integer[]{1478,1487});
		decl5203A1.put("NWgt",new Integer[]{1487,1498});
		decl5203A1.put("moaType1",new Integer[]{1498,1501});
		decl5203A1.put("dutyAmt1",new Integer[]{1501,1511});
		decl5203A1.put("moaType2",new Integer[]{1511,1514});
		decl5203A1.put("dutyAmt2",new Integer[]{1514,1524});
		decl5203A1.put("moaType4",new Integer[]{1537,1540});
		decl5203A1.put("dutyAmt4",new Integer[]{1540,1550});
		decl5203A1.put("moaType5",new Integer[]{1550,1553});
		decl5203A1.put("dutyAmt5",new Integer[]{1553,1563});
		decl5203A1.put("moaType6",new Integer[]{1563,1566});
		decl5203A1.put("dutyAmt6",new Integer[]{1566,1576});
		decl5203A1.put("moaType7",new Integer[]{1576,1579});
		decl5203A1.put("dutyAmt7",new Integer[]{1579,1589});
		decl5203A1.put("totalDuty",new Integer[]{1615,1625});
		decl5203A1.put("sendDate",new Integer[]{1639,1645});
		decl5203A1.put("sendTime",new Integer[]{1645,1649});
		decl5203A1.put("exWhNo",new Integer[]{1755,1763});
		decl5203A1.put("imWhNo",new Integer[]{1763,1771});
		
		decl5203A4.put("itemNo",new Integer[]{2,6});
		decl5203A4.put("code",new Integer[]{6,17});
		decl5203A4.put("descrip",new Integer[]{19,409});
		decl5203A4.put("brand",new Integer[]{409,429});
		decl5203A4.put("model",new Integer[]{429,449});
		decl5203A4.put("spec",new Integer[]{449,599});
		decl5203A4.put("pacs",new Integer[]{599,634});
		decl5203A4.put("NWght",new Integer[]{634,645});
		decl5203A4.put("qty",new Integer[]{645,659});
		decl5203A4.put("unit",new Integer[]{659,662});
		decl5203A4.put("stuAnt",new Integer[]{679,693});
		decl5203A4.put("stuUnit",new Integer[]{693,696});
		decl5203A4.put("fobValue",new Integer[]{780,792});
		decl5203A4.put("buyCommNo",new Integer[]{792,807});
		decl5203A4.put("saleCommNo",new Integer[]{807,822});
		decl5203A4.put("ODeclNo",new Integer[]{822,836});
		decl5203A4.put("OItemNo",new Integer[]{836,840});
		decl5203A4.put("permitNo",new Integer[]{840,854});
		decl5203A4.put("permitItem",new Integer[]{854,857});
		decl5203A4.put("statMode",new Integer[]{925,927});
		decl5203A4.put("duty1",new Integer[]{927,930});
		decl5203A4.put("dutyRate1",new Integer[]{930,936});
		decl5203A4.put("duty2",new Integer[]{936,939});
		decl5203A4.put("dutyRate2",new Integer[]{939,945});
		decl5203A4.put("duty3",new Integer[]{945,948});
		decl5203A4.put("dutyRate3",new Integer[]{948,954});
		decl5203A4.put("duty4",new Integer[]{954,957});
		decl5203A4.put("dutyRate4",new Integer[]{957,963});
		decl5203A4.put("assGoodNo",new Integer[]{1008,1158});
	}
	
	public static void setReleaseDateTime(){
		releaseDateTime.put("declNo", new Integer[]{2,16});
		releaseDateTime.put("rlsTime", new Integer[]{137,149});
	}
	
	public void setCmDeclarationHeadDAO(CmDeclarationHeadDAO cmDeclarationHeadDAO) {
		this.cmDeclarationHeadDAO = cmDeclarationHeadDAO;
	}
	
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

}
