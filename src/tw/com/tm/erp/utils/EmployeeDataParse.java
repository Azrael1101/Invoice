package tw.com.tm.erp.utils;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import jxl.*;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.BuCustomerId;
import tw.com.tm.erp.hbm.bean.BuEmployee;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrand;
import tw.com.tm.erp.hbm.bean.BuEmployeeBrandId;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithCustomerView;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.BuShopEmployeeId;
import tw.com.tm.erp.hbm.dao.BuAddressBookDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuCustomerDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeBrandDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithCustomerViewDAO;
import tw.com.tm.erp.hbm.dao.BuShopDAO;
import tw.com.tm.erp.hbm.dao.BuShopEmployeeDAO;

public class EmployeeDataParse {
  private static final Log log = LogFactory.getLog(EmployeeDataParse.class);

  	private BuBrandDAO buBrandDAO;
	private BuEmployeeDAO buEmployeeDAO;
	private BuCustomerDAO buCustomerDAO;
	private BuAddressBookDAO buAddressBookDAO;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private BuEmployeeBrandDAO buEmployeeBrandDAO;
	private BuEmployeeWithCustomerViewDAO buEmployeeWithCustomerViewDAO;
	private BuShopDAO buShopDAO;
	private BuShopEmployeeDAO buShopEmployeeDAO;
	private Map columnName = new HashMap();
	
	InputStream is = null;
	Workbook rwb = null;
	String bean_head = null;
	String curren_value = null;
	String uuid = null;
	String processName = "EMPLOYEE_PARSE";
	Date executeDate = new Date();
	
	public void execute(){
		try{
			uuid = UUID.randomUUID().toString();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "員工轉檔批次匯入程序開始執行...", executeDate, uuid, "SYS");
			columnName.put("身份證明代號(身份証字號、統一編號、護照號嗎)","identityCode");
			columnName.put("組織代號","organizationCode");
			columnName.put("類別：自然人、法人","type");
			columnName.put("名稱","chineseNameBak2");
			columnName.put("英文名稱","englishName");
			columnName.put("簡稱","shortName");
			columnName.put("公司名稱","companyName");
			columnName.put("部門","department");
			columnName.put("國別","countryCode");
			columnName.put("性別 F)女 M)男","gender");
			columnName.put("生日 - 年","birthdayYear");
			columnName.put("生日 - 月","birthdayMonth");
			columnName.put("生日 - 日","birthdayDay");
			columnName.put("電子郵件信箱","EMail");
			columnName.put("城市","city");
			columnName.put("鄉鎮區別","area");
			columnName.put("郵遞區號","zipCode");
			columnName.put("地址","address");
			columnName.put("聯絡人員","contractPerson");
			columnName.put("電話(日)","tel1");
			columnName.put("電話(夜)","tel2");
			columnName.put("傳真一","fax1");
			columnName.put("傳真二","fax2");
			columnName.put("行動電話","mobilePhone");
			columnName.put("員工代碼","employeeCode");
			columnName.put("品牌代號","brandCode");
			columnName.put("職稱","employeePosition");
			columnName.put("到職日","arriveDate");
			columnName.put("離職日","leaveDate");
			columnName.put("所屬部門代號","employeeDepartment");
			columnName.put("成本部門代號","costDepartment");
			columnName.put("地區別代號","employeeZone");
			columnName.put("組別代號","employeeGroup");
			columnName.put("職稱類別代號","employeeType");
			columnName.put("出勤班別代號","workType");
			columnName.put("正式任用日期","appointmentDate");
			columnName.put("調整年資起日","seniorityDate");

			//偉盟EXCEL轉入/轉出檔案路徑設定
			List<BuCommonPhraseLine> EmployeeDataParses = buCommonPhraseLineDAO.findEnableLineById("EmployeeDataParse");
			if(EmployeeDataParses == null || EmployeeDataParses.size() == 0){
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "查無員工轉檔檔案位置", executeDate, uuid, "SYS");
			}
			BuCommonPhraseLine EmployeeDataParse = EmployeeDataParses.get(0);
			log.info("檔案位置=" + EmployeeDataParse.getAttribute1()+EmployeeDataParse.getAttribute3());
			File tasaXls = new File(EmployeeDataParse.getAttribute1()+EmployeeDataParse.getAttribute3());
			File tasaXls_new = new File(EmployeeDataParse.getAttribute2() + DateUtils.format(new Date()) + EmployeeDataParse.getAttribute3());
			if(tasaXls.exists()){
				this.readXLS(tasaXls);
				rwb.close();
				is.close();
				//檔案reName
				if(tasaXls_new.exists())
					tasaXls_new.delete();
				tasaXls.renameTo(tasaXls_new);
			}else{
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "查無EXCELL檔案可供轉入", executeDate, uuid, "SYS");
			}
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "EXCELL檔案轉入結束", executeDate, uuid, "SYS");
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "開始新建員工會員資料", executeDate, uuid, "SYS");
			this.dataBaseMaintain();
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "員工轉檔程式結束", executeDate, uuid, "SYS");
		}catch(Exception e){
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "員工轉檔程式發生終止錯誤，原因："+e.getMessage(), executeDate, uuid, "SYS");
		}
	}
	
	public void dataBaseMaintain(){
		try{
			//到職一個月才開始計算
			Calendar cal_apply = Calendar.getInstance();
			cal_apply.setTime(new Date());

			cal_apply.add(java.util.Calendar.MONTH,-1);
			List <BuBrand> buBrands = buBrandDAO.findByProperty("branchCode","1");
			List <BuBrand> otherBrands = buBrandDAO.findByProperty("branchCode","3");
			List <BuEmployeeWithCustomerView> views = 
				buEmployeeWithCustomerViewDAO.findByProperty("BuEmployeeWithCustomerView" ," and arriveDate <= ? and counts < ? and arriveDate is not null and leaveDate is null" , 
						new Object[]{cal_apply.getTime(),Long.valueOf(buBrands.size()+otherBrands.size()+1)});
			
			for (int i = 0; i < views.size(); i++) {
				BuEmployeeWithCustomerView view = views.get(i);
				
				//branch 1
				for (int j = 0; j < buBrands.size(); j++) {
					//log.info("branch 1");
					BuBrand buBrand = buBrands.get(j);
					BuCustomerId buCustomerId = new BuCustomerId(view.getEmployeeCode(),buBrand.getBrandCode());
					List<BuCustomer> buCustomers = buCustomerDAO.
					findByProperty("BuCustomer", new String[]{"id.customerCode","id.brandCode"}, new Object[]{view.getEmployeeCode(),buBrand.getBrandCode()});
					//需判斷 尚有哪些參數需補齊
					if(buCustomers == null || buCustomers.size() == 0){
						BuCustomer buCustomer = new BuCustomer(buCustomerId,view.getAddressBookId(),"NTD");
						buCustomer.setCustomerTypeCode("1");
						buCustomer.setInvoiceTypeCode("3");
						buCustomer.setInvoiceDeliveryCode("1");
						buCustomer.setPaymentTermCode("Z9");
						buCustomer.setAccountCode("1123999");
						buCustomer.setEnable("Y");
						buCustomer.setTaxType("3");
						buCustomer.setTaxRate(5D);
						buCustomer.setVipTypeCode(buBrand.getBrandCode()+"EMP");
						buCustomer.setCreatedBy("SYS");
						buCustomer.setCreationDate(new Date());
						buCustomer.setLastUpdatedBy("SYS");
						buCustomer.setLastUpdateDate(new Date());
						buCustomer.setIsCurrentUse("Y");					
						buCustomerDAO.save(buCustomer);
					}else{
						BuCustomer buCustomer = buCustomers.get(0);
						if(view.getAddressBookId() != buCustomer.getAddressBookId())
							buCustomer.setAddressBookId(view.getAddressBookId());
						buCustomer.setEnable("Y");
						buCustomer.setLastUpdatedBy("SYS");
						buCustomer.setLastUpdateDate(new Date());
						buCustomer.setIsCurrentUse("Y");
						buCustomerDAO.update(buCustomer);
					}
				}

				//branch 3
				for (int k = 0; k < otherBrands.size(); k++) {
					//log.info("branch 3");
					BuBrand otherBrand = otherBrands.get(k);
					BuCustomerId customerId = new BuCustomerId(view.getEmployeeCode(),otherBrand.getBrandCode());
					List<BuCustomer> buCustomers = buCustomerDAO.
					findByProperty("BuCustomer", new String[]{"id.customerCode","id.brandCode","vipTypeCode"}, new Object[]{view.getEmployeeCode() ,otherBrand.getBrandCode(), otherBrand.getBrandCode()+"EMP"});
					//需判斷 尚有哪些參數需補齊
					if(buCustomers == null || buCustomers.size() == 0){
						BuCustomer customer = new BuCustomer(customerId,view.getAddressBookId(),"NTD");
						customer.setCustomerTypeCode("1");
						customer.setInvoiceTypeCode("3");
						customer.setInvoiceDeliveryCode("1");
						customer.setPaymentTermCode("Z9");
						customer.setAccountCode("C12371287");
						customer.setEnable("Y");
						customer.setTaxType("3");
						customer.setTaxRate(5D);
						customer.setVipTypeCode(otherBrand.getBrandCode()+"EMP");
						customer.setCreatedBy("SYS");
						customer.setCreationDate(new Date());
						customer.setLastUpdatedBy("SYS");
						customer.setLastUpdateDate(new Date());
						customer.setIsCurrentUse("Y");					
						buCustomerDAO.save(customer);
					}else{
						BuCustomer buCustomer = buCustomers.get(0);
						if(view.getAddressBookId() != buCustomer.getAddressBookId())
							buCustomer.setAddressBookId(view.getAddressBookId());
						buCustomer.setEnable("Y");
						buCustomer.setLastUpdatedBy("SYS");
						buCustomer.setLastUpdateDate(new Date());
						buCustomer.setIsCurrentUse("Y");
						buCustomerDAO.update(buCustomer);
					}
				}

				//T2
				//log.info("branch T2");
				BuCustomerId buCustomerId = new BuCustomerId(view.getEmployeeCode(),"T2");
				List<BuCustomer> buCustomers = buCustomerDAO.
				findByProperty("BuCustomer", new String[]{"id.brandCode","id.customerCode"}, new Object[]{"T2",view.getEmployeeCode()});
				if(buCustomers == null || buCustomers.size() == 0){
					BuCustomer buCustomer = new BuCustomer(buCustomerId,view.getAddressBookId(),"NTD");
					buCustomer.setCustomerTypeCode("1");
					buCustomer.setInvoiceTypeCode("3");
					buCustomer.setInvoiceDeliveryCode("1");
					buCustomer.setPaymentTermCode("Z9");
					buCustomer.setAccountCode("C12371287");
					buCustomer.setEnable("Y");
					buCustomer.setTaxType("3");
					buCustomer.setTaxRate(5D);
					buCustomer.setVipTypeCode("E");
					buCustomer.setCreatedBy("SYS");
					buCustomer.setCreationDate(new Date());
					buCustomer.setLastUpdatedBy("SYS");
					buCustomer.setLastUpdateDate(new Date());
					buCustomer.setIsCurrentUse("Y");
					buCustomerDAO.save(buCustomer);
				}else{
					BuCustomer buCustomer = buCustomers.get(0);
					if(view.getAddressBookId() != buCustomer.getAddressBookId())
						buCustomer.setAddressBookId(view.getAddressBookId());
					buCustomer.setEnable("Y");
					buCustomer.setLastUpdatedBy("SYS");
					buCustomer.setLastUpdateDate(new Date());
					buCustomer.setIsCurrentUse("Y");
					buCustomerDAO.update(buCustomer);
				}
			}

			//log.info("作廢");
			views = buEmployeeWithCustomerViewDAO.findByPropertyWithCondition("BuEmployeeWithCustomerView" , 
					new String[]{"leaveDate <= " , "counts > "} , new Object[]{new Date() , 0l});
			for (int i = 0; i < views.size(); i++) {
				BuEmployeeWithCustomerView view = views.get(i);

				//百貨作廢 1
				for (int j = 0; j < buBrands.size(); j++) {
					BuBrand buBrand = buBrands.get(j);
					BuCustomerId buCustomerId = new BuCustomerId(view.getEmployeeCode(),buBrand.getBrandCode());
					BuCustomer buCustomer = (BuCustomer)buCustomerDAO.findById("BuCustomer", buCustomerId);
					//需判斷 尚有哪些參數需補齊
					if(buCustomer != null && !"N".equals(buCustomer.getEnable())){
						buCustomer.setEnable("N");
						buCustomer.setIsCurrentUse("N");
						buCustomer.setLastUpdatedBy("SYS");
						buCustomer.setLastUpdateDate(new Date());
						buCustomerDAO.update(buCustomer);
					}
				}

				//百貨作廢 3
				for (int j = 0; j < otherBrands.size(); j++) {
					BuBrand buBrand = otherBrands.get(j);
					BuCustomerId buCustomerId = new BuCustomerId(view.getEmployeeCode(),buBrand.getBrandCode());
					BuCustomer buCustomer = (BuCustomer)buCustomerDAO.findById("BuCustomer", buCustomerId);
					//需判斷 尚有哪些參數需補齊
					if(buCustomer != null && !"N".equals(buCustomer.getEnable())){
						buCustomer.setEnable("N");
						buCustomer.setIsCurrentUse("N");
						buCustomer.setLastUpdatedBy("SYS");
						buCustomer.setLastUpdateDate(new Date());
						buCustomerDAO.update(buCustomer);
					}
				}

				//T2做廢
				BuCustomerId buCustomerId = new BuCustomerId(view.getEmployeeCode(),"T2");
				BuCustomer buCustomer = (BuCustomer)buCustomerDAO.findById("BuCustomer", buCustomerId);
				//需判斷 尚有哪些參數需補齊
				if(buCustomer != null && !"N".equals(buCustomer.getEnable())){
					buCustomer.setEnable("N");
					buCustomer.setIsCurrentUse("N");
					buCustomer.setLastUpdatedBy("SYS");
					buCustomer.setLastUpdateDate(new Date());
					buCustomerDAO.update(buCustomer);
				}

				List<BuCustomer> buCustomers = buCustomerDAO.findByProperty("BuCustomer", "and id.brandCode = ? and addressBookId = ? and vipTypeCode = ? ", new Object[]{"T2" , view.getAddressBookId() , "E1"});
				if(null != buCustomers && buCustomers.size() > 0){
					buCustomer = buCustomers.get(0);
					buCustomer.setVipTypeCode("99");
					buCustomer.setLastUpdatedBy("SYS");
					buCustomer.setLastUpdateDate(new Date());
					buCustomerDAO.update(buCustomer);
				}
			}
		}catch(Exception e){
			SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "會員轉員工發生錯誤，原因："+e.getMessage(), executeDate, uuid, "SYS");
		}
	}
	
	public void readXLS(File tasaXls) throws Exception{
		BuAddressBook buAddressBook = new BuAddressBook();
		BuEmployee buEmployee = new BuEmployee();
		try{
			is = new FileInputStream(tasaXls);
		    rwb = Workbook.getWorkbook(is);
		    Sheet rs = rwb.getSheet(0);
			List<BuCommonPhraseLine> AddressBookTypes = buCommonPhraseLineDAO.findEnableLineById("AddressBookType");
			Map AddressBookTypemap = new HashMap();
			try{
				for (int i = 0; i < AddressBookTypes.size(); i++) {
					BuCommonPhraseLine AddressBookType = AddressBookTypes.get(i);
					AddressBookTypemap.put(AddressBookType.getName(), AddressBookType.getId().getLineCode());
				}
			}catch(Exception ex){
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "查詢人類別發生錯誤，原因：" + ex.getMessage(), executeDate, uuid, "SYS");
			}
			
			List<BuCommonPhraseLine> EmployeeDepartments = buCommonPhraseLineDAO.findEnableLineById("EmployeeDepartment");
			Map EmployeeDepartmentmap = new HashMap();
			try{
				for (int i = 0; i < EmployeeDepartments.size(); i++) {
					BuCommonPhraseLine EmployeeDepartment = EmployeeDepartments.get(i);
					EmployeeDepartmentmap.put(EmployeeDepartment.getName(), EmployeeDepartment.getAttribute1());
				}
			}catch(Exception ex){
				SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "查詢員工部門發生錯誤，原因：" + ex.getMessage(), executeDate, uuid, "SYS");
			}
			
		    for(int x=1 ; x< rs.getRows() ; x++){
		    	try{
		    		//log.info("start " + x);
			    	if(!StringUtils.hasText(rs.getCell(2,x).getContents())){
			    		break;
			    	}
		    		String organizationCode ="TM";//人事系統變更 2017.06.22 Maco
			    	for(int y=0 ; y<rs.getColumns() ; y++ ){
			    		log.info("YYYYYYYYYY:"+y);
			    		if(y==1){
			    			continue;
			    		}

			    		bean_head = (String)columnName.get(rs.getCell(y, 0).getContents().trim());
			    		log.info("欄位名稱:"+bean_head);
//			    		if(bean_head.equals("organizationCode")){
//			    			curren_value = rs.getCell(y, x).getContents().trim();
//			    			organizationCode = curren_value;//人事系統變更 2017.06.22 Maco
//			    			log.info("組織代碼:"+curren_value);
//			    		}
			    		if(bean_head.equals("chineseNameBak2")){
			    			byte[] binary = rs.getCell(y, x).getContents().trim().getBytes("Big5");
			    			curren_value = new String(binary, "Big5");
			    			log.info(curren_value);			    		}else{
			    			curren_value = rs.getCell(y, x).getContents().trim();
			    		}
			    		if(bean_head.equals("identityCode")&&!StringUtils.hasText(curren_value))
			    			return;
			    		else if(!StringUtils.hasText(bean_head))
			    			continue;
			    		if(bean_head.equals("employeeCode")){
			    			break;
			    		}
			    		Class type = PropertyUtils.getPropertyType(buAddressBook,bean_head);
			    		if(bean_head.equals("identityCode")){
			    			List<BuAddressBook> buAddressBooks = buAddressBookDAO.findByProperty("BuAddressBook", "identityCode", curren_value);
			    			if(buAddressBooks.size()>0&&buAddressBooks!=null){
			    				buAddressBook = buAddressBooks.get(0);
			    				buAddressBook.setOrganizationCode(organizationCode);//人事系統變更 2017.06.22 Maco
			    				buAddressBook.setLastUpdateDate(new Date());
			    				buAddressBook.setLastUpdatedBy("SYS");

			    				
			    			}else{
			    				buAddressBook = new BuAddressBook("0",curren_value);
			    				buAddressBook.setOrganizationCode(organizationCode);//人事系統變更 2017.06.22 Maco
			    				buAddressBook.setCreatedBy("SYS");
			    				buAddressBook.setCreationDate(new Date());
			    				buAddressBook.setLastUpdatedBy("SYS");
			    				buAddressBook.setLastUpdateDate(new Date());

			    			}
			    		}
			    		
			    		if(bean_head.equals("type"))
			    			curren_value = (String)AddressBookTypemap.get(curren_value);
			    		if("ROC".equals(curren_value))
			    			curren_value = "TW";
			    		if(StringUtils.hasText(curren_value)){
				    		if (type == String.class)
				    			PropertyUtils.setNestedProperty(buAddressBook, bean_head, curren_value);
				    		else if(type == Long.class)
				    			PropertyUtils.setNestedProperty(buAddressBook, bean_head, Long.valueOf(curren_value));
				    		else if(type == Date.class)
				    			PropertyUtils.setNestedProperty(buAddressBook, bean_head, DateUtils.parseDate("yyyy/MM/dd", curren_value) );
			    		}
			    	}
			    	//MACO 2016.10.12 ChineseName編碼非UTF-8問題,因此先寫入ChineseNameBak2
			    	
			    	
			    	
			    	
			    	buAddressBook.setShortName(buAddressBook.getChineseNameBak2());
			    	buAddressBook.setChineseName(buAddressBook.getChineseNameBak2());
			    	buAddressBookDAO.saveOrUpdate(buAddressBook);
				 
			    //for BuEmployee
			    	//log.info("start employee " + x);
			    	boolean startEmployee = false;
			    	for(int y=0 ; y<rs.getColumns() ; y++ ){
			    		//如果已經沒有欄位
			    		if(!StringUtils.hasText(rs.getCell(y, 0).getContents())){
			    			break;
			    		}
			    		bean_head = (String)columnName.get(rs.getCell(y, 0).getContents().trim());
			    		curren_value = rs.getCell(y, x).getContents().trim();
			    		//如果欄位未定義即跳過或是該欄位沒值 即跳過
			    		if(!StringUtils.hasText(bean_head) || !StringUtils.hasText(curren_value) || (!bean_head.equals("employeeCode") && !startEmployee)){
			    			//如果離職日是空的，清空
			    			if(null != bean_head && bean_head.equals("leaveDate")){
			    				//log.info("bean_head = " + bean_head);
			    				PropertyUtils.setNestedProperty(buEmployee, bean_head, null);
			    			}
			    			continue;
			    		}
			    		Class type = PropertyUtils.getPropertyType(buEmployee,bean_head);
			    		if(bean_head.equals("employeeCode")){
			    			startEmployee = true;
			    			List<BuEmployee> buEmployees = buEmployeeDAO.findByProperty("BuEmployee", "employeeCode", curren_value);
			    			if(buEmployees.size()>0&&buEmployees!=null){
			    				buEmployee = buEmployees.get(0);
			    				buEmployee.setLastUpdatedBy("SYS");
			    				buEmployee.setLastUpdateDate(new Date());
			    			}else
			    				buEmployee = new BuEmployee(curren_value,buAddressBook.getAddressBookId());
			    				buEmployee.setCreatedBy("SYS");
			    				buEmployee.setCreationDate(new Date());
			    				buEmployee.setLastUpdatedBy("SYS");
			    				buEmployee.setLastUpdateDate(new Date());
			    			}
			    		if(bean_head.equals("brandCode"))
			    			curren_value = (String)EmployeeDepartmentmap.get(curren_value);
			    		if(StringUtils.hasText(curren_value)){
				    		if (type == String.class)
				    			PropertyUtils.setNestedProperty(buEmployee, bean_head, curren_value);
				    		else if(type == Long.class)
				    			PropertyUtils.setNestedProperty(buEmployee, bean_head, Long.valueOf(curren_value));
				    		else if(type == Date.class)
				    			PropertyUtils.setNestedProperty(buEmployee, bean_head, DateUtils.parseDate("yyyy/MM/dd", curren_value) );
			    		}
			    	}
			    	//log.info("start eployeeBrand " + x);
				    //這邊塞buEmployeeBrand
				    BuCommonPhraseLine employeeDepartment = buCommonPhraseLineDAO.findById("EmployeeDepartment", buEmployee.getEmployeeDepartment());
				    if(employeeDepartment != null){
				    	if(employeeDepartment.getAttribute1() != null){
					    	//如果COMMON裡面的ATTRIBUTE1有品牌則只塞該品牌
						    BuEmployeeBrandId buEmployeeBrandId = new BuEmployeeBrandId(buEmployee.getEmployeeCode(),employeeDepartment.getAttribute1());
						    BuEmployeeBrand buEmployeeBrand = buEmployeeBrandDAO.findById(buEmployeeBrandId);
						    if(buEmployeeBrand == null){
						    	buEmployeeBrand = new BuEmployeeBrand(buEmployeeBrandId,buEmployee);
						    	this.setUpdateEmployeeBrand(buEmployeeBrand);
						    }
				    	}else{
				    		//如果沒有 則所有的品牌都塞employeeBrand
				    		BuEmployeeBrandId buEmployeeBrandId = new BuEmployeeBrandId(buEmployee.getEmployeeCode(),"T1BS");
				    		BuEmployeeBrand buEmployeeBrand = new BuEmployeeBrand(buEmployeeBrandId,buEmployee);
				    		this.setUpdateEmployeeBrand(buEmployeeBrand);
				    		buEmployeeBrand.getId().setBrandCode("T1CO");
				    		this.setUpdateEmployeeBrand(buEmployeeBrand);
				    		buEmployeeBrand.getId().setBrandCode("T2");
				    		this.setUpdateEmployeeBrand(buEmployeeBrand);
				    	}
				    }
				    //log.info("start shopEmployee " + x);
				    //這邊塞shopEmployee
				    BuCommonPhraseLine costDepartment = buCommonPhraseLineDAO.findById("CostDepartment", buEmployee.getCostDepartment());
				    if(costDepartment != null && costDepartment.getAttribute1() != null){
				    	//如果不是T2 就塞該LINE裡面定義的SHOPCODE
				    	if(!"T2".equals(costDepartment.getAttribute1())){
					    	BuShopEmployeeId buShopEmployeeId = new BuShopEmployeeId(costDepartment.getAttribute1(),buEmployee.getEmployeeCode());
					    	BuShop buShop = buShopDAO.findById(costDepartment.getAttribute1());
					    	if(buShop != null){
					    		BuShopEmployee buShopEmployee = new BuShopEmployee(buShopEmployeeId,buShop);
					    		this.setUpdateBuShopEmployee(buShopEmployee);
					    	}
					    	//如果是T2 就把SHOP裡面T2都撈出來塞滿滿
				    	}else{
				    		List<BuShop> buShops = buShopDAO.findShopByBrandAndEnable("T2", null);
				    		for (Iterator iterator = buShops.iterator(); iterator.hasNext();) {
								BuShop buShop = (BuShop) iterator.next();
								BuShopEmployeeId buShopEmployeeId = new BuShopEmployeeId(buShop.getShopCode(),buEmployee.getEmployeeCode());
								BuShopEmployee buShopEmployee = new BuShopEmployee(buShopEmployeeId,buShop);
					    		this.setUpdateBuShopEmployee(buShopEmployee);
							}
				    	}
				    
				    	//log.info("start remark " + x);
				    	//地區別判斷專櫃人員 remark設專櫃人員為Y
					    if("T2".equals(costDepartment.getAttribute1())){
					    	buEmployee.setRemark2("Y");
					    }else if("4".equals(buEmployee.getEmployeeZone()) && costDepartment.getAttribute1() != null){
					    	buEmployee.setRemark2("Y");
					    }else if("5".equals(buEmployee.getEmployeeZone()) && costDepartment.getAttribute1() != null){
					    	buEmployee.setRemark2("Y");
					    }else if("6".equals(buEmployee.getEmployeeZone()) && costDepartment.getAttribute1() != null){
					    	buEmployee.setRemark2("Y");
					    }else if("7".equals(buEmployee.getEmployeeZone()) && costDepartment.getAttribute1() != null){
					    	buEmployee.setRemark2("Y");
					    }
				    }
				    
				    //for 部落格 員工密碼取身分證前六碼 Caspar 2012.12.26
				    if(null == buEmployee.getPassword())
				    	buEmployee.setPassword(buAddressBook.getIdentityCode().substring(0, 5));
				    
				    buEmployeeDAO.saveOrUpdate(buEmployee);
				    SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_INFO, "修定員工資料，工號：" + buEmployee.getEmployeeCode() +"完成", executeDate, uuid, "SYS");
			    }catch (Exception e){
			    	SiSystemLogUtils.createSystemLog(processName, MessageStatus.LOG_ERROR, "修定員工資料，身分字號：" + buAddressBook.getIdentityCode()+"之員工發生錯誤，原因"+e.getMessage(), executeDate, uuid, "SYS");
				}
		    }
		}catch (Exception e){
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 更新資訊並且UPDATE
	 * @param buEmployeeBrand
	 */
	public void setUpdateEmployeeBrand(BuEmployeeBrand buEmployeeBrand){
    	buEmployeeBrand.setCreatedBy("SYS");
    	buEmployeeBrand.setCreationDate(new Date());
    	buEmployeeBrand.setLastUpdatedBy("SYS");
    	buEmployeeBrand.setLastUpdateDate(new Date());
    	buEmployeeBrand.setIndexNo(1L);
    	buEmployeeBrandDAO.saveOrUpdate(buEmployeeBrand);
	}
	
	/**
	 * 更新資訊並且UPDATE
	 * @param buEmployeeBrand
	 */
	public void setUpdateBuShopEmployee(BuShopEmployee buShopEmployee){
		buShopEmployee.setCreatedBy("SYS");
		buShopEmployee.setCreationDate(new Date());
		buShopEmployee.setLastUpdatedBy("SYS");
		buShopEmployee.setLastUpdateDate(new Date());
		buShopEmployee.setEnable("Y");
		buShopEmployee.setIndexNo(1L);
		buShopEmployeeDAO.saveOrUpdate(buShopEmployee);
	}
	
	
	public void setBuAddressBookDAO(BuAddressBookDAO buAddressBookDAO) {
		this.buAddressBookDAO = buAddressBookDAO;
	}

	public void setBuEmployeeDAO(BuEmployeeDAO buEmployeeDAO) {
		this.buEmployeeDAO = buEmployeeDAO;
	}

	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	public void setBuEmployeeWithCustomerViewDAO(
			BuEmployeeWithCustomerViewDAO buEmployeeWithCustomerViewDAO) {
		this.buEmployeeWithCustomerViewDAO = buEmployeeWithCustomerViewDAO;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setBuCustomerDAO(BuCustomerDAO buCustomerDAO) {
		this.buCustomerDAO = buCustomerDAO;
	}

	public void setBuEmployeeBrandDAO(BuEmployeeBrandDAO buEmployeeBrandDAO) {
		this.buEmployeeBrandDAO = buEmployeeBrandDAO;
	}

	public void setBuShopDAO(BuShopDAO buShopDAO) {
		this.buShopDAO = buShopDAO;
	}

	public void setBuShopEmployeeDAO(BuShopEmployeeDAO buShopEmployeeDAO) {
		this.buShopEmployeeDAO = buShopEmployeeDAO;
	}
}