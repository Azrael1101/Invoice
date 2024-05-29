package tw.com.tm.erp.importdb;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.ImportDBService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.DeclarationDataParse;
import tw.com.tm.erp.utils.LCMSDoc;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.T2PosIslandParse;
import tw.com.tm.erp.utils.User;

public class ImportDataParse {
	private static final Log log = LogFactory.getLog(ImportDataParse.class);
	private static ApplicationContext context = SpringUtils.getApplicationContext();

	public String[] getListFilesByPage(String src, String fileName, int pageNum, int pageSize ){
		File folder = new File(src);
		String [] returnList = new String[pageSize+1];
		int x = (pageNum-1)*pageSize;
		int y = 0;
		log.info("src:"+src+" fileName:"+fileName+" pageNum:"+pageNum+" pageSize:"+pageSize);
		if(folder.isDirectory()){
			String[] list = folder.list();
			List<String> fileList = new ArrayList<String>();
			fileName = fileName.replaceAll("/", "");
			String fileNameBlank = fileName.replaceAll(" ", "_");
			for (int i = 0; i < list.length; i++) {
				File listFile = new File(folder+"\\"+list[i]);
				if(listFile.isFile()){
					if(StringUtils.hasText(fileName) && !"ICF.TXT".equals(list[i].toUpperCase()) && !"TRANS.TXT".equals(list[i].toUpperCase()) &&
							!"LOC_ADJ.TXT".equals(list[i].toUpperCase()) && !"LOC_ALLOC.TXT".equals(list[i].toUpperCase()) && 
							(list[i].toUpperCase().indexOf(fileName) > -1 || list[i].toUpperCase().indexOf(fileNameBlank) > -1) )
						fileList.add(list[i]);
					else if(!StringUtils.hasText(fileName))
						fileList.add(list[i]);
				}
			}

			for (int i = x; i < fileList.size(); i++) {
				File listFile = new File(folder+"\\"+fileList.get(i));
				if(listFile.isFile())
					returnList[y++] = fileList.get(i);
				if(y == pageSize)
					break;
			}

			int totalPage = NumberUtils.getInt(fileList.size()) > 0 ? fileList.size()/pageSize + (fileList.size()%pageSize>0?1:0) : 1;
			returnList[y++] = String.valueOf(totalPage);
		}else{
			returnList[0] = "1" ;
		}
		return returnList;
	}

	public String getImportPath(String lineCode) throws Exception{
		BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService)SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine("ImportPath", lineCode);
		if(buCommonPhraseLine != null)
			return buCommonPhraseLine.getAttribute1();
		else
			return "";
	}

	public String getListFiles(String brandCode, String  employeeCode, String path, String methodName, String [] files, String msg) throws Exception{
		try{
			System.out.println("getListFiles");
			SiProgramLogAction siProgramLogAction = (SiProgramLogAction) SpringUtils.getApplicationContext().getBean("siProgramLogAction");
			String returnValue = "N";
			HashMap uiProperties=new HashMap();
			User user = new User();
			user.setBrandCode(brandCode);
			user.setEmployeeCode(employeeCode);
			user.setOrganizationCode("TM");
			uiProperties.put("BRAND_CODE",brandCode);
			uiProperties.put(SystemConfig.USER_SESSION_NAME,user);
			uiProperties.put(ImportDBService.BATCH_NO,employeeCode+DateUtils.getCurrentDateStr("yyyyMMddHHmmss"));
			uiProperties.put(ImportDBService.UPLOAD_DATE,new Date());
			BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService)SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
			BuCommonPhraseLine line = buCommonPhraseService.getBuCommonPhraseLine("ImportPath", methodName);
			if(line == null)
				throw new Exception("查無轉檔路徑資料");
			if(StringUtils.hasText(msg) && "ImInventoryCountsImportDataT2".equals(line.getId().getLineCode())){
				BuCommonPhraseLine countsLine = buCommonPhraseService.getBuCommonPhraseLine("InventoryCountsId", msg);
				if(countsLine == null || null == countsLine.getAttribute1())
					throw new Exception("查無盤點資料");
				else
					uiProperties.put(ImportDBService.COUNTS_ID,countsLine.getAttribute1());
			}
			siProgramLogAction.deleteProgramLog(methodName, null, methodName);
			try{
				if("ImMovementImportDataT2".equals(line.getId().getLineCode()))
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				if("ImMovementImportDataT2Main".equals(line.getId().getLineCode()))
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				if("ImMovementImportDataT2hw".equals(line.getId().getLineCode()))
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				if("ImMovementImportDataT2cw".equals(line.getId().getLineCode())) // 新增調撥單（菸酒）格式 by Weichun 2011.01.25
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				if("ImMovementImportDataT2cwMain".equals(line.getId().getLineCode())) // 新增調撥單（菸酒）格式-新 by Steve 2014.03.16
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				if("ImInventoryCountsImportDataT2".equals(line.getId().getLineCode()))
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				if("DeclarationImportDataT2".equals(line.getId().getLineCode()))
					returnValue = DeclarationImportDataT2(line, path, files);
				if("SoDeliveryInventoryImportDataT2".equals(line.getId().getLineCode()) || "SoDeliveryMoveImportDataT2".equals(line.getId().getLineCode()) || "SoSalesOrderImportDataT2".equals(line.getId().getLineCode()))
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				if("ImStorageImportDataT2".equals(line.getId().getLineCode()))
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				if("ImStorageImportDataT2M".equals(line.getId().getLineCode()))
					returnValue = ImportDataT2(line, path, methodName, files, uiProperties);
				return returnValue;
			}catch(Exception e){
				siProgramLogAction.createProgramLog(methodName, MessageStatus.LOG_ERROR, methodName, e.getMessage(), employeeCode);
				return e.getMessage();
			}
		}catch(Exception e){
			return e.getMessage();
		}
	}

	public String ImportDataT2(BuCommonPhraseLine line, String path, String methodName, String [] files, HashMap uiProperties) throws Exception{
		StringBuffer reMsg = new StringBuffer();
		String returnValue = null;
		User user = (User)uiProperties.get(SystemConfig.USER_SESSION_NAME);
		String employeeCode = user.getEmployeeCode();
		File folder = new File(path);
		File folder_bak = new File(path+"\\"+line.getAttribute2());
		File folder_err = new File(path+"\\"+line.getAttribute3());
		if(!folder.isDirectory())
			folder.mkdir();
		if(!folder_bak.isDirectory())
			folder_bak.mkdir();
		if(!folder_err.isDirectory())
			folder_err.mkdir();
		if("N".equals(line.getEnable())){
			returnValue = "匯入失敗，原因：此張單據目前不允許匯入 ";
			reMsg.append(returnValue + "<br>");
		}else{
			
			for (int i = 0; i < files.length; i++) {
				int fileName = files[i].indexOf(".");
				reMsg.append("檔名：　" +files[i] + "　");
				try{
					ImportDBService serv = (ImportDBService) SpringUtils.getApplicationContext().getBean("importDBService");
					returnValue = serv.importDB(folder + "\\" + files[i], "tw.com.tm.erp.importdb."+methodName, uiProperties);
					File listFile = new File(folder+"\\"+files[i]);
					File listFileBak = new File(folder_bak+"\\"+files[i].substring(0,fileName)+"_"+employeeCode+"_"+System.currentTimeMillis()+files[i].substring(fileName));
					listFile.renameTo(listFileBak);
				}catch(FormException e){
					returnValue = "檔名：　" +files[i] + "　匯入失敗，原因：" + e.getMessage();
					return returnValue;
				}catch(Exception e){
					returnValue = "檔名：　" +files[i] + "　匯入失敗，原因：" + e.getMessage();
					return returnValue;
				}
				reMsg.append(returnValue.split(";")[0] + "<br>");
			}

			//同一個批次
			if("ImMovementImportDataT2".equals(methodName)){
				String originalOrderTypeCode = returnValue.split(";")[1];
				String originalOrderNo = returnValue.split(";")[2];
				if(((null!= originalOrderTypeCode && null != originalOrderNo )|| reMsg.indexOf("覆核") >= 0) && reMsg.indexOf("成功") >= 0)
					reMsg.append(";;confirmImmovement('"+uiProperties.get(ImportDBService.BATCH_NO)+"','"+originalOrderTypeCode+"','"+originalOrderNo+"')");
			}else if("ImMovementImportDataT2Main".equals(methodName)){
				log.info("執行的方法:"+methodName);
				String originalOrderTypeCode = returnValue.split(";")[1];
				String originalOrderNo = returnValue.split(";")[2];
				if(((null!= originalOrderTypeCode && null != originalOrderNo )|| reMsg.indexOf("覆核") >= 0) && reMsg.indexOf("成功") >= 0)
					reMsg.append(";;confirmImmovement('"+uiProperties.get(ImportDBService.BATCH_NO)+"','"+originalOrderTypeCode+"','"+originalOrderNo+"')");
			}else if("ImMovementImportDataT2hw".equals(methodName)){
				String originalOrderTypeCode = returnValue.split(";")[1];
				String originalOrderNo = returnValue.split(";")[2];
				String args[] = originalOrderNo.split("-");
				String[] orderNo = new String[10];

				if(args.length==2){
					if(null==args[1]){
						orderNo[0]="1";
					}else{
						orderNo[0] = args[1];
					}
				}else if(args.length==3){
					if(null==args[2]){
						orderNo[0]="1";
						orderNo[1]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
					}
				}else if(args.length==4){
					if(null==args[3]){
						orderNo[0]="1";
						orderNo[1]="1";
						orderNo[2]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
						orderNo[2] = args[3];
					}
				}else if(args.length==5){
					if(null==args[4]){
						orderNo[0]="1";
						orderNo[1]="1";
						orderNo[2]="1";
						orderNo[3]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
						orderNo[2] = args[3];
						orderNo[3] = args[4];
					}
				}else if(args.length==6){
					if(null==args[5]){
						orderNo[0]="1";
						orderNo[1]="1";
						orderNo[2]="1";
						orderNo[3]="1";
						orderNo[4]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
						orderNo[2] = args[3];
						orderNo[3] = args[4];
						orderNo[4] = args[5];
					}
				}else if(args.length==7){
					if(null==args[6]){
						orderNo[0]="1";
						orderNo[1]="1";
						orderNo[2]="1";
						orderNo[3]="1";
						orderNo[4]="1";
						orderNo[5]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
						orderNo[2] = args[3];
						orderNo[3] = args[4];
						orderNo[4] = args[5];
						orderNo[5] = args[6];
					}
				}else if(args.length==8){
					if(null==args[7]){
						orderNo[0]="1";
						orderNo[1]="1";
						orderNo[2]="1";
						orderNo[3]="1";
						orderNo[4]="1";
						orderNo[5]="1";
						orderNo[6]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
						orderNo[2] = args[3];
						orderNo[3] = args[4];
						orderNo[4] = args[5];
						orderNo[5] = args[6];
						orderNo[6] = args[7];
					}
				}else if(args.length==9){
					if(null==args[8]){
						orderNo[0]="1";
						orderNo[1]="1";
						orderNo[2]="1";
						orderNo[3]="1";
						orderNo[4]="1";
						orderNo[5]="1";
						orderNo[6]="1";
						orderNo[7]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
						orderNo[2] = args[3];
						orderNo[3] = args[4];
						orderNo[4] = args[5];
						orderNo[5] = args[6];
						orderNo[6] = args[7];
						orderNo[7] = args[8];
					}
				}else if(args.length==10){
					if(null==args[9]){
						orderNo[0]="1";
						orderNo[1]="1";
						orderNo[2]="1";
						orderNo[3]="1";
						orderNo[4]="1";
						orderNo[5]="1";
						orderNo[6]="1";
						orderNo[7]="1";
						orderNo[8]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
						orderNo[2] = args[3];
						orderNo[3] = args[4];
						orderNo[4] = args[5];
						orderNo[5] = args[6];
						orderNo[6] = args[7];
						orderNo[7] = args[8];
						orderNo[8] = args[9];
					}
				}else if(args.length==11){
					if(null==args[10]){
						orderNo[0]="1";
						orderNo[1]="1";
						orderNo[2]="1";
						orderNo[3]="1";
						orderNo[4]="1";
						orderNo[5]="1";
						orderNo[6]="1";
						orderNo[7]="1";
						orderNo[8]="1";
						orderNo[9]="1";
					}else{
						orderNo[0] = args[1];
						orderNo[1] = args[2];
						orderNo[2] = args[3];
						orderNo[3] = args[4];
						orderNo[4] = args[5];
						orderNo[5] = args[6];
						orderNo[6] = args[7];
						orderNo[7] = args[8];
						orderNo[8] = args[9];
						orderNo[9] = args[10];
					}
				}

				if(reMsg.indexOf("覆核成功") >= 0 && reMsg.indexOf("點貨覆核") >= 0){
					reMsg.append(";;reconfirmImmovement('test','"+originalOrderTypeCode+"',"+orderNo[0]+","+orderNo[1]+","+orderNo[2]+","+orderNo[3]+","+orderNo[4]+","+orderNo[5]+","+orderNo[6]+","+orderNo[7]+","+orderNo[8]+","+orderNo[9]+")");
				}
			}

			//同一個批次
			if("ImInventoryCountsImportDataT2".equals(methodName) && reMsg.indexOf("成功") >= 0){
				reMsg.append(";;confirmImInventoryCounts('"+uiProperties.get(ImportDBService.BATCH_NO)+"','"+uiProperties.get(ImportDBService.COUNTS_ID)+"')");
			}

		}

		return reMsg.toString();
	}

	public String deleteListFiles(String path, String [] files) throws Exception{
		StringBuffer reMsg = new StringBuffer();
		String returnValue = null;
		File folder = new File(path);
		if(!folder.isDirectory())
			folder.mkdir();
		for (int i = 0; i < files.length; i++) {
			reMsg.append("檔名：　" +files[i] + "　");
			try{
				File listFile = new File(folder+"\\"+files[i]);
				listFile.delete();
				returnValue = "刪除成功";
			}catch(Exception e){
				returnValue = "刪除失敗，原因：" + e.getMessage();
			}
			reMsg.append(returnValue + "<br>");
		}
		return reMsg.toString();
	}

	public String DeclarationImportDataT2(BuCommonPhraseLine line, String path ,String [] files) throws Exception{
		DeclarationDataParse declarationDataParse = (DeclarationDataParse)context.getBean("declarationDataParse");
		System.out.println("aa");
		String returnValue = declarationDataParse.execute(line, path, files);
		return returnValue;
	}
	
	public String T2PosImport(String employeeCode) throws Exception{
		System.out.println("T2PosImport");
		StringBuffer reMsg = new StringBuffer();
		try{
			HashMap map  = new HashMap();
			User userObj = new User();
			userObj.setBrandCode("T2");
			userObj.setEmployeeCode(employeeCode);
			map.put(SystemConfig.USER_SESSION_NAME, userObj);
			map.put("autoJobControl", "N");
			map.put("transferFolder", "HF");
			String base = LCMSDoc.class.getResource("/").getPath();
			LCMSDoc doc = new LCMSDoc(base, "tw.com.tm.erp.importdb.T2PosImportData");
			reMsg.append(doc.folderTransfer(map));
		}catch(Exception ex){
			System.out.println(ex.toString());
			reMsg.append(ex.toString());
		}
		return reMsg.toString();
	}
	
	public String T2PosImportMazu(String employeeCode) throws Exception{
		System.out.println("T2PosImportMazu");
		StringBuffer reMsg = new StringBuffer();
		try{
			T2PosIslandParse t2PosIslandParse = new 
			T2PosIslandParse("T2", employeeCode, "tw.com.tm.erp.importdb.T2PosImportDataMazu", "HF", "N"); 
			t2PosIslandParse.execute("POS");
			t2PosIslandParse.execute("VOID");
			t2PosIslandParse.execute("LAD");
			reMsg.append("馬祖銷售業績上傳完成");
		}catch(Exception ex){
			System.out.println(ex.toString());
			reMsg.append(ex.toString());
		}
		return reMsg.toString();
	}
	
	public String T2PosImportHD(String employeeCode) throws Exception{
		System.out.println("T2PosImportHD");
		StringBuffer reMsg = new StringBuffer();
		try{
			HashMap map  = new HashMap();
			User userObj = new User();
			userObj.setBrandCode("T2");
			userObj.setEmployeeCode(employeeCode);
			map.put(SystemConfig.USER_SESSION_NAME, userObj);
			map.put("autoJobControl", "N");
			map.put("transferFolder", "HD");
			String base = LCMSDoc.class.getResource("/").getPath();
			LCMSDoc doc = new LCMSDoc(base, "tw.com.tm.erp.importdb.T2PosImportDataHD");
			reMsg.append(doc.folderTransfer(map));
		}catch(Exception ex){
			System.out.println(ex.toString());
			reMsg.append(ex.toString());
		}
		return reMsg.toString();
	}
}