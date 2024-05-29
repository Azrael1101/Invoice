package tw.com.tm.erp.hbm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseHead;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLineId;
import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.BuCurrency;
import tw.com.tm.erp.hbm.bean.BuShop;
import tw.com.tm.erp.hbm.bean.BuShopEmployee;
import tw.com.tm.erp.hbm.bean.BuShopMachine;
import tw.com.tm.erp.hbm.bean.BuShopMachineId;
import tw.com.tm.erp.hbm.bean.BuSupplier;
import tw.com.tm.erp.hbm.bean.BuSupplierId;
import tw.com.tm.erp.hbm.bean.BuSupplierWithAddressView;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.bean.ImMovementItem;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

public class BuCommonPhraseService {

    private static final Log log = LogFactory
	    .getLog(BuCommonPhraseService.class);
    BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    BuCommonPhraseHeadDAO buCommonPhraseHeadDAO;

  	public static final String[] GRID_FIELD_NAMES = { 
  		"indexNo","lineCode", "name", "description", "enable",
  		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5", 
  		"parameter1", "parameter2", "parameter3", "parameter4","parameter5",
  		"reserve1", "reserve2", "reserve3", "reserve4", "reserve5",
  		"createdBy", "creationDate", "lastUpdatedBy", "lastUpdateDate"};
  	
    public static final String[] GRID_FIELD_DEFAULT_VALUES = { 
    	"", "", "", "", "",
			"", "", "", "", "",  
			"", "", "", "", "", 
			"", "", "", "", "", 
			"", "", "", ""};
    
    public static final int[] GRID_FIELD_TYPES = { 
    	AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,  
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,  
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING, 
    	AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_DATE, };
    
  	public static final String[] GRID_SEARCH_FIELD_NAMES = { 
  		"headCode", "name", "description", "enable",
  		"createdBy", "creationDate", "lastUpdatedBy", "creationDate"};
  	
    public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { 
    	"", "", "", "", "",
			"", "", "", ""};

    private static Hashtable<String, List<BuCommonPhraseLine>> buCommonPhraseData = new Hashtable();

    // cache all common phrase data

    public void setBuCommonPhraseLineDAO(
	    BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
	this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }

    public void setBuCommonPhraseHeadDAO(
	    BuCommonPhraseHeadDAO buCommonPhraseHeadDAO) {
	this.buCommonPhraseHeadDAO = buCommonPhraseHeadDAO;
    }

    public void save(BuCommonPhraseHead saveObj) {
    	buCommonPhraseHeadDAO.save(saveObj);
    }

    public void update(BuCommonPhraseHead updateObj) {
    	buCommonPhraseHeadDAO.update(updateObj);
    }
    
    public void saveupdate(BuCommonPhraseHead Obj){
    	if(buCommonPhraseHeadDAO.findById(Obj.getHeadCode())== null){
    		log.info("save this obj!!!");
    		buCommonPhraseHeadDAO.save(Obj);
    	}else{
    		buCommonPhraseHeadDAO.update(Obj);
    		log.info("update this obj!!!");
    	}
    }
    
    public String getLineName(String headCode, String lineCode) {
    	String lineName = "";
    	try {
    	    BuCommonPhraseLineId id = new BuCommonPhraseLineId(
    		    new BuCommonPhraseHead(headCode), lineCode);
    	    lineName = buCommonPhraseLineDAO.findById(id).getName();
    	} catch (Exception e) {
    	    if (e.getCause() == null) {
    		lineName = "unknow";
    	    }
  	}
	// System.out.println(lineName);
	return lineName;

    }

    
    /**
     * 依據常用字彙查詢螢幕的輸入條件進行查詢
     * 
     * @param headCode
     * @param name
     * @param description
     * @return List
     * @throws Exception
     */
    public List<BuCommonPhraseHead> findBySearchValue(String headCode,
	    String name, String description) throws Exception{
	
	try {
	    headCode = headCode.trim();
	    name = name.trim();
	    description = description.trim();
	    return buCommonPhraseHeadDAO.findBySearchValue(headCode, name,
		    description);
	} catch (Exception ex) {
	    log.error("查詢常用字彙資料時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢常用字彙資料時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public List<BuCommonPhraseLine> findEnableLineById(String headCode) {
	try {
	    return buCommonPhraseLineDAO.findEnableLineById(headCode);
	} catch (RuntimeException re) {
	    throw re;
	}
    }

    public void getAllBuCommonPhaseLines() {
	String hql = "from BuCommonPhraseHead where headCode = 'SystemConfig' ";
	buCommonPhraseLineDAO.getHibernateTemplate().find(hql,
		BuCommonPhraseHead.class);

    }
    
    public List getBuPosUiConfigByHead() {
    	//String hql = "from BuCommonPhraseHead where headCode = 'SystemConfig' ";
    	return (List) buCommonPhraseLineDAO.findBuPosUiConfigByHead();
    }
    
    public List getPosConnectionSatausByPos(String pos) {
    	return buCommonPhraseLineDAO.findEnableLineById(pos);
    }

    /**
     * 
     * @return
     */
    public java.util.Hashtable<String, List<BuCommonPhraseLine>> getBuCommonPhraseData() {
	if (buCommonPhraseData.size() == 0)
	    setBuCommonPhraseData();
	return buCommonPhraseData;
    }

    /**
     * 
     * @param headCode
     * @return
     */
    public List<BuCommonPhraseLine> getBuCommonPhraseLines(String headCode) {
	if (buCommonPhraseData.size() == 0)
	    setBuCommonPhraseData();
	return buCommonPhraseData.get(headCode);
    }

    /**
     * 
     * @param headCode
     * @param lineCode
     * @return
     */
    public BuCommonPhraseLine getBuCommonPhraseLine(String headCode,
	    String lineCode) {
	if (buCommonPhraseData.size() == 0)
	    setBuCommonPhraseData();
	List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseData
		.get(headCode);
	for (BuCommonPhraseLine buCommonPhraseLine : buCommonPhraseLines)
	    if (buCommonPhraseLine.getId().getLineCode().equalsIgnoreCase(
		    lineCode))
		return buCommonPhraseLine;
	return null;
    }

    /**
     * 
     * @param headCode
     * @param lineCode
     * @return
     */
    public String getBuCommonPhraseLineName(String headCode, String lineCode) {
	if (buCommonPhraseData.size() == 0)
	    setBuCommonPhraseData();
	List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseData
		.get(headCode);
	for (BuCommonPhraseLine buCommonPhraseLine : buCommonPhraseLines)
	    if (buCommonPhraseLine.getId().getLineCode().equalsIgnoreCase(
		    lineCode))
		return buCommonPhraseLine.getName();
	return null;
    }

    /**
     * load common phrase data
     */
    public void setBuCommonPhraseData() {
	List heads = buCommonPhraseHeadDAO.findAll();
	for (int index = 0; index < heads.size(); index++) {
	    BuCommonPhraseHead head = (BuCommonPhraseHead) heads.get(index);
	    List lines = findEnableLineById(head.getHeadCode());
	    if ((null != lines) && (lines.size() > 0)) {
		buCommonPhraseData.put(head.getHeadCode(), lines);
	    }
	}
    }

   
    /**
     * @param headCode
     * @return BuCommonPhraseHead
     * @throws Exception
     */
    public BuCommonPhraseHead findById(String headCode) throws Exception{
	log.info("test");
	try{
	    headCode = headCode.trim();
	    return buCommonPhraseHeadDAO.findById(headCode);
	}catch (Exception ex) {
	    log.error("依據常用字彙代碼：" + headCode + "查詢常用字彙資料時發生錯誤，原因："
		    + ex.toString());
	    throw new Exception("依據常用字彙代碼：" + headCode + "查詢常用字彙資料時發生錯誤，原因："
		    + ex.getMessage());
	}
    }

    /**
     * get Common Phrase Line By Id
     * 
     * @param headCode
     * @return HashMap
     * @throws Exception
     */
    public HashMap findCommonPhraseLinesById(String headCode) throws Exception {
	HashMap commonPhraseMap = new HashMap();
	try {
	    BuCommonPhraseHead head = buCommonPhraseHeadDAO.findById(headCode);
	    if (head != null) {
		List<BuCommonPhraseLine> lines = head.getBuCommonPhraseLines();
		if (lines != null && lines.size() > 0) {
		    for (BuCommonPhraseLine line : lines) {
			commonPhraseMap.put(line.getId().getLineCode(), line
				.getName());
		    }
		    return commonPhraseMap;
		} else {
		    throw new NoSuchDataException("常用字彙明細檔查無" + headCode
			    + "相關資料！");
		}
	    } else {
		throw new NoSuchDataException("常用字彙主檔查無" + headCode + "相關資料！");
	    }
	} catch (Exception ex) {
	    log.error("查詢常用字彙檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢常用字彙檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * get Common Phrase Line By Id
     * 
     * @param headCode
     * @param isAllowedNull
     * @return List
     * @throws Exception
     */
    public List<BuCommonPhraseLine> getCommonPhraseLinesById(String headCode,
	    boolean isAllowedNull) throws Exception {
	try {
	    BuCommonPhraseHead head = buCommonPhraseHeadDAO.findById(headCode);
	    if (head != null) {
		List<BuCommonPhraseLine> lines = head.getBuCommonPhraseLines();
		if (isAllowedNull || (lines != null && lines.size() > 0)) {
		    return lines;
		} else {
		    throw new NoSuchDataException("常用字彙明細檔查無" + headCode
			    + "相關資料！");
		}

	    } else {
		throw new NoSuchDataException("常用字彙主檔查無" + headCode + "相關資料！");
	    }
	} catch (Exception ex) {
	    log.error("查詢常用字彙檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢常用字彙檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * clear BuCommonPhraseData cache
     */
    public void clearBuCommonPhraseData() {
	buCommonPhraseData = new Hashtable();
    }

    public List findCommonPhraseLineByProperty(String propertyName, Object value)
	    throws Exception {
	try {
	    return buCommonPhraseLineDAO.findByProperty(propertyName, value);
	} catch (Exception ex) {
	    log.error("查詢常用字彙檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("查詢常用字彙檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    public String saveOrUpdateBuCommonPhrase(
	    BuCommonPhraseHead commonPhraseHead, String loginUser, boolean isNew)
	    throws FormException, Exception {
	try {
	    doBuCommonPhraseHeadValidate(commonPhraseHead);
	    doBuCommonPhraseLineValidate(commonPhraseHead
		    .getBuCommonPhraseLines());

	    commonPhraseHead.setEnable(("N".equals(commonPhraseHead.getEnable()) ? "N" : "Y"));	    
	    if (isNew) {
		BuCommonPhraseHead commonPhraseHeadPO = findById(commonPhraseHead.getHeadCode());
		if (commonPhraseHeadPO == null) {
		    insertBuCommonPhraseHead(commonPhraseHead, loginUser);
		} else {
		    throw new ValidationErrorException("常用字彙類別代碼："
			    + commonPhraseHead.getHeadCode() + "已經存在，請勿重複建立！");
		}
	    } else {
		modifyBuCommonPhraseHead(commonPhraseHead, loginUser);
	    }

	    return "常用字彙代碼：" + commonPhraseHead.getHeadCode() + "存檔成功！";
	} catch (FormException fe) {
	    log.error("常用字彙資料存檔時發生錯誤，原因：" + fe.toString());
	    throw new FormException(fe.getMessage());
	} catch (Exception ex) {
	    log.error("常用字彙資料存檔時發生錯誤，原因：" + ex.toString());
	    throw new Exception("常用字彙資料存檔時發生錯誤，原因：" + ex.getMessage());
	}
    }

    /**
     * 新增至BuCommonPhrase
     * 
     * @param saveObj
     * @param loginUser
     */
    private void insertBuCommonPhraseHead(BuCommonPhraseHead saveObj,
	    String loginUser) {

	saveObj.setCreatedBy(loginUser);
	saveObj.setCreationDate(new Date());
	saveObj.setLastUpdatedBy(loginUser);
	saveObj.setLastUpdateDate(new Date());
	List<BuCommonPhraseLine> commonPhraseLines = saveObj.getBuCommonPhraseLines();
	for(BuCommonPhraseLine commonPhraseLine : commonPhraseLines){
	    commonPhraseLine.getId().setBuCommonPhraseHead(saveObj);
	    commonPhraseLine.setCreatedBy(loginUser);
	    commonPhraseLine.setCreationDate(new Date());
	    commonPhraseLine.setLastUpdatedBy(loginUser);
	    commonPhraseLine.setLastUpdateDate(new Date());	    
	}
	buCommonPhraseHeadDAO.save(saveObj);
    }

    /**
     * 修改BuCommonPhrase
     * 
     * @param updateObj
     * @param loginUser
     */
    private void modifyBuCommonPhraseHead(BuCommonPhraseHead updateObj,
	    String loginUser) {

	updateObj.setLastUpdatedBy(loginUser);
	updateObj.setLastUpdateDate(new Date());	
	List<BuCommonPhraseLine> commonPhraseLines = updateObj.getBuCommonPhraseLines();
	for(BuCommonPhraseLine commonPhraseLine : commonPhraseLines){
	    BuCommonPhraseHead commonPhraseHead = commonPhraseLine.getId().getBuCommonPhraseHead();
	    if(commonPhraseHead != null){
		commonPhraseLine.setLastUpdatedBy(loginUser);
		commonPhraseLine.setLastUpdateDate(new Date());
	    }else{
		commonPhraseLine.getId().setBuCommonPhraseHead(updateObj);
	        commonPhraseLine.setCreatedBy(loginUser);
		commonPhraseLine.setCreationDate(new Date());
		commonPhraseLine.setLastUpdatedBy(loginUser);
		commonPhraseLine.setLastUpdateDate(new Date());
	    }	   	    
	}
	buCommonPhraseHeadDAO.update(updateObj);
    }

    /**
     * 檢核常用字彙資料
     * 
     * @param commonPhraseHead
     * @throws ValidationErrorException
     */
    private void doBuCommonPhraseHeadValidate(
	    BuCommonPhraseHead commonPhraseHead)
	    throws ValidationErrorException {

	if (!StringUtils.hasText(commonPhraseHead.getHeadCode())) {
	    throw new ValidationErrorException("請輸入常用字彙類別代碼！");
	} else {
	    commonPhraseHead.setHeadCode(commonPhraseHead.getHeadCode().trim());
	}

	if (!StringUtils.hasText(commonPhraseHead.getName())) {
	    throw new ValidationErrorException("請輸入常用字彙類別名稱！");
	} else {
	    commonPhraseHead.setName(commonPhraseHead.getName().trim());
	}
	
	if (commonPhraseHead.getDescription() != null) {
	    commonPhraseHead.setDescription(commonPhraseHead.getDescription().trim());
	}
    }

    /**
     * 檢核常用字彙明細資料
     * 
     * @param buCommonPhraseLines
     * @param loginUser
     * @throws ValidationErrorException
     */
    private void doBuCommonPhraseLineValidate(
	    List<BuCommonPhraseLine> buCommonPhraseLines)
	    throws ValidationErrorException {

	String tabName = "常用字彙明細";
	HashMap map = new HashMap();
	for (int i = 0; i < buCommonPhraseLines.size(); i++) {
	    BuCommonPhraseLine commonPhraseLine = (BuCommonPhraseLine) buCommonPhraseLines
		    .get(i);
	    BuCommonPhraseLineId id = commonPhraseLine.getId();
	    if (!StringUtils.hasText(id.getLineCode())) {
		throw new ValidationErrorException("請輸入" + tabName + "中第"
			+ (i + 1) + "項明細的代號！");
	    } else {
		id.setLineCode(id.getLineCode().trim());
	    }
	    
	    if (!StringUtils.hasText(commonPhraseLine.getName())) {
		throw new ValidationErrorException("請輸入" + tabName + "中第"
			+ (i + 1) + "項明細的名稱！");
	    } else {
		commonPhraseLine.setName(commonPhraseLine.getName().trim());
	    }

	    if (map.get(id.getLineCode()) != null) {
		throw new ValidationErrorException(tabName + "中第" + (i + 1)
			+ "項明細的代號重複！");
	    } else {
		map.put(id.getLineCode(), id.getLineCode());
	    }

	    commonPhraseLine.setEnable(("N"
		    .equals(commonPhraseLine.getEnable()) ? "N" : "Y"));
	}
    }
    
    /**
     * 產生實際存檔的常用字彙資料
     * 
     * @param commonPhraseHead
     * @param loginUser
     * @return BuCommonPhraseHead
     */
    private BuCommonPhraseHead produceNewBuCommonPhrase(BuCommonPhraseHead commonPhraseHead, String loginUser) {

	BuCommonPhraseHead actualSaveCommonPhrase = new BuCommonPhraseHead();
	actualSaveCommonPhrase.setHeadCode(commonPhraseHead.getHeadCode());
	actualSaveCommonPhrase.setName(commonPhraseHead.getName());
	actualSaveCommonPhrase.setDescription(commonPhraseHead.getDescription());
	actualSaveCommonPhrase.setType(commonPhraseHead.getType());
	actualSaveCommonPhrase.setEnable(commonPhraseHead.getEnable());
	
	
	
	BeanUtils.copyProperties(commonPhraseHead, actualSaveCommonPhrase);
	List<BuCommonPhraseLine> origCommonPhraseLines = commonPhraseHead.getBuCommonPhraseLines();
	List<BuCommonPhraseLine> actualSaveCommonPhraseLines = new ArrayList(0);
	for (int i = 0; i < origCommonPhraseLines.size(); i++) {
	    BuCommonPhraseLine origcommonPhraseLine = (BuCommonPhraseLine) origCommonPhraseLines.get(i);
	    BuCommonPhraseHead origCommonPhraseHead = origcommonPhraseLine.getId().getBuCommonPhraseHead();
		    
	    if (origCommonPhraseHead == null) {
		origcommonPhraseLine.getId().setBuCommonPhraseHead(actualSaveCommonPhrase);
		origcommonPhraseLine.setCreatedBy(loginUser);
		origcommonPhraseLine.setCreationDate(new Date());
		origcommonPhraseLine.setLastUpdatedBy(loginUser);
		origcommonPhraseLine.setLastUpdateDate(new Date());

		actualSaveCommonPhraseLines.add(origcommonPhraseLine);
	    } else {
		BuCommonPhraseLine actualSaveCommonPhraseLine = new BuCommonPhraseLine();
		BeanUtils.copyProperties(origcommonPhraseLine, actualSaveCommonPhraseLine);
		actualSaveCommonPhraseLine.getId().setBuCommonPhraseHead(actualSaveCommonPhrase);
		actualSaveCommonPhraseLine.setLastUpdatedBy(loginUser);
		actualSaveCommonPhraseLine.setLastUpdateDate(new Date());

		actualSaveCommonPhraseLines.add(actualSaveCommonPhraseLine);
	    }
	}

	actualSaveCommonPhrase.setBuCommonPhraseLines(actualSaveCommonPhraseLines);
	return actualSaveCommonPhrase;
    }
    
	public List<BuCommonPhraseLine> findCommonPhraseLineByAttribute(
			String headCode, String searchString) {
		return buCommonPhraseLineDAO.findCommonPhraseLineByAttribute(headCode, searchString);
		
	}
	
    public HashMap updateAJAXMovement(Map parameterMap) throws FormException, Exception {
      
      System.out.println("go here prepare to insert");
  
    MessageBox msgBox = new MessageBox();
    HashMap resultMap = new HashMap(0);
      try{
    	Object formBindBean = parameterMap.get("vatBeanFormBind");
    	Object formLinkBean = parameterMap.get("vatBeanFormLink");
    	Object otherBean = parameterMap.get("vatBeanOther");
  
    	String employeeCode = (String)PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
    	String formId = (String)PropertyUtils.getProperty(otherBean, "formId");
    	System.out.println("employeeCode=" + employeeCode);
    	//取得欲更新的bean
    	System.out.println("======準備Get Hi=========");
  
    	BuCommonPhraseHead PO = getActualMovement(formLinkBean,formId);
    	System.out.println("======Start copyJSONBeantoPojoBean=========");
    	AjaxUtils.copyJSONBeantoPojoBean(formBindBean, PO);
    	System.out.println("======Finish copyJSONBeantoPojoBean=========");
  
    	PO.setEnable("Y");
    	String resultMsg = this.saveAjaxData(PO, formId);
      	resultMap.put("resultMsg", resultMsg);
      	resultMap.put("entityBean", PO);
          
      }catch (FormException fe) {
         log.error("調撥單存檔失敗，原因：" + fe.toString());
         throw new FormException("調撥單存檔失敗，原因：" + fe.toString());
         
    }catch (Exception ex) {		  
        log.error("調撥單存檔時發生錯誤，原因：" + ex.toString());
        throw new Exception("調撥單存檔失敗，原因：" + ex.toString());
    }
   
    resultMap.put("vatMessage" ,msgBox);
  	
    return resultMap;
  }
  
  private BuCommonPhraseHead getActualMovement(Object bean , String formId) throws FormException, Exception{
  	String id = (String)PropertyUtils.getProperty(bean, "headCode");
  	System.out.println("Head Code = "+id);
  	BuCommonPhraseHead BuCommonPhraseHead  = null;
  	if("".equals(formId)){
  		BuCommonPhraseHead = new BuCommonPhraseHead();
  		BuCommonPhraseHead.setHeadCode(id);
  	}else{
  		BuCommonPhraseHead = findById(id);
  	}
    return BuCommonPhraseHead;
  }
  
  
          
  
  public String saveAjaxData(BuCommonPhraseHead modifyObj,String formId) throws Exception {
  try{
  	/*
      if ("".equals(formId)) {
      	save(modifyObj);	
      	System.out.println("新增成功");
      	return "新增成功";
      } else {
      	update(modifyObj);
      	System.out.println("修改成功");
      	return "修改成功";
      }
      */
  		saveupdate(modifyObj);
  		return "成功";
  } catch (Exception ex) {
  		log.error("調撥單存檔時發生錯誤，原因：" + ex.toString());
  		throw new Exception("調撥單存檔時發生錯誤，原因：" + ex.getMessage());
  }

}
  
	public Map executeInitial(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		try{
			Map multiList = new HashMap(0);
			Object otherBean = parameterMap.get("vatBeanOther");
			
			BuCommonPhraseHead buCommonPhraseHead = this.executeFindActualCommonPhraseHead(parameterMap);
			log.info(buCommonPhraseHead.getEnable());
			parameterMap.put("entityBean", buCommonPhraseHead);
			resultMap.put("multiList", multiList);
			resultMap.put("form",buCommonPhraseHead);
		}catch(Exception ex){
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type"   , "ALERT");
			messageMap.put("message", "表單初始化失敗，原因："+ex.toString());
			messageMap.put("event1" , null);
			messageMap.put("event2" , null);
			resultMap.put("vatMessage",messageMap);
		}
		return resultMap;
	}
	public void updateBean(Map parameterMap) throws FormException,
	Exception {
// TODO Auto-generated method stub

BuCommonPhraseHead buCommonPhraseHead = null;
try {
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	// Object formLinkBean = parameterMap.get("vatBeanFormLink");
	// Object otherBean = parameterMap.get("vatBeanOther");

	buCommonPhraseHead = (BuCommonPhraseHead) parameterMap.get("entityBean");
	
	AjaxUtils.copyJSONBeantoPojoBean(formBindBean, buCommonPhraseHead);

	parameterMap.put("entityBean", buCommonPhraseHead);
} /*
	 * catch (FormException fe) { log.error("前端資料塞入bean失敗，原因：" +
	 * fe.toString()); throw new FormException(fe.getMessage()); }
	 */catch (Exception ex) {
	log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
	throw new Exception("資料塞入bean發生錯誤，原因：" + ex.getMessage());
}

}
	public Map updateAJAXMaster(Map parameterMap) throws Exception {
		System.out.println("======異動單頭資料=========");
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0);
		String resultMsg = null;
		try {
			Object otherBean = parameterMap.get("vatBeanOther");

			String loginEmployeeCode = (String) PropertyUtils.getProperty(
					otherBean, "loginEmployeeCode");

			BuCommonPhraseHead buCommonPhraseHead = (BuCommonPhraseHead) parameterMap.get("entityBean");
			// String shopCode = (String) PropertyUtils.getProperty(otherBean,
			// "shopCode");
			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			String formAction = (String) PropertyUtils.getProperty(otherBean,"formAction");

			if (OrderStatus.FORM_SUBMIT.equals(formAction)) {
				buCommonPhraseHead.setCreatedBy(loginEmployeeCode);
				buCommonPhraseHead.setCreationDate(new Date());
				buCommonPhraseHead.setLastUpdatedBy(loginEmployeeCode);
				buCommonPhraseHead.setLastUpdateDate(new Date());

				System.out.println("======準備異動單頭資料=========:");
				if (!StringUtils.hasText(formIdString)) {
					buCommonPhraseHead.setLastUpdatedBy(loginEmployeeCode);
					buCommonPhraseHead.setLastUpdateDate(new Date());
					buCommonPhraseHeadDAO.save(buCommonPhraseHead);
				} else {
					buCommonPhraseHeadDAO.update(buCommonPhraseHead);
				}

			}

			resultMsg = "常用字彙 ：" + buCommonPhraseHead.getHeadCode() + "存檔成功！ 是否繼續新增？";

			resultMap.put("resultMsg", resultMsg);
			resultMap.put("entityBean", buCommonPhraseHead);
			resultMap.put("vatMessage", msgBox);

			return resultMap;

		} catch (Exception ex) {
			log.error("主檔維護作業存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception(ex.getMessage());
		}
	}
	public BuCommonPhraseHead executeFindActualCommonPhraseHead(Map parameterMap) throws FormException,Exception {

		// Object formBindBean = parameterMap.get("vatBeanFormBind");
		// Object formLinkBean = parameterMap.get("vatBeanFormLink");
		Object otherBean = parameterMap.get("vatBeanOther");

		BuCommonPhraseHead buCommonPhraseHead = null;
		try {

			String formIdString = (String) PropertyUtils.getProperty(otherBean,"formId");
			buCommonPhraseHead = !StringUtils.hasText(formIdString) ? this.executeNewCommonPhraseHead() : this.findById(formIdString);
			parameterMap.put("entityBean", buCommonPhraseHead);
			return buCommonPhraseHead;
		} catch (Exception e) {
			log.error("取得庫別主檔失敗,原因:" + e.toString());
			throw new Exception("取得庫別主檔失敗,原因:" + e.toString());
		}
	}
	public BuCommonPhraseHead executeNewCommonPhraseHead() throws Exception {
		System.out.println("NewCommonPhraseHead().........");
		BuCommonPhraseHead form = new BuCommonPhraseHead();
		form.setEnable("Y");
		return form;
	}
	public Map executeInitialForMarquee(Map parameterMap) throws Exception{
		Map resultMap = new HashMap(0);
		try{
            BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findOneBuCommonPhraseLineForMarquee("Marquee","content");  
            resultMap.put("enable",     buCommonPhraseLine.getEnable());
            resultMap.put("attribute1",  buCommonPhraseLine.getAttribute1());
		}catch(Exception ex){
			log.error("表單初始化失敗，原因：" + ex.toString());
			Map messageMap = new HashMap();
			messageMap.put("type"   , "ALERT");
			messageMap.put("message", "表單初始化失敗，原因："+ex.toString());
			messageMap.put("event1" , null);
			messageMap.put("event2" , null);
			resultMap.put("vatMessage",messageMap);
		}
		return resultMap;
	}	
	
	public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception
	{
		try
		{
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			HashMap map = new HashMap();
			HashMap findObjs = new HashMap();
			// ======================帶入Head的值=============================

			String headCode = httpRequest.getProperty("headCode");


			log.info("shopCode"+headCode);

			if(!(""==headCode||null==headCode||headCode.equals(null)))
			{
				log.info("headCode="+headCode);

					//List<BuShopMachine> buShopMachines = buShopMachineDAO.findByProperty("BuShopMachine", fieldNames, fieldValue);
				List<BuCommonPhraseLine> buCommonPhraseLine = buCommonPhraseLineDAO.findCommonPhraseEnablePageLine(headCode, iSPage, iPSize);

					// ==============================================================
					log.info("buCommonPhraseLine.size:" + buCommonPhraseLine.size());

					if (buCommonPhraseLine.size() > 0)
					{

						 
						for (BuCommonPhraseLine line : buCommonPhraseLine)
						{
							line.setLineCode(line.getId().getLineCode());
						}
						Long firstIndex =iSPage * iPSize + 1L; // 取得第一筆的INDEX
						Long maxIndex = buCommonPhraseLineDAO.findCommonPhraseEnablePageLineMaxIndex(headCode);
						result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES,buCommonPhraseLine, gridDatas, firstIndex, maxIndex));
					} 
					else 
					{

						result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES,GRID_FIELD_DEFAULT_VALUES, gridDatas));

					}


			}
			else
			{
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES,GRID_FIELD_DEFAULT_VALUES,map, gridDatas));
				//throw new Exception("尚未填入字彙代碼");

			}
			return result;
		} 
		catch (Exception ex) 
		{
			log.error("載入頁面顯示的倉庫查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的倉庫查詢失敗！");
		}
	}
/*  public List<Properties> getAJAXPageData(Properties httpRequest) throws Exception{

      	try{
      	    List<Properties> result = new ArrayList();
      	    List<Properties> gridDatas = new ArrayList();
      	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
      	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
      	    
      	    //======================帶入Head的值=========================
      	    
      	    String headCode = httpRequest.getProperty("headCode");// 品牌代號
      	   
      	    HashMap map = new HashMap();
      	    map.put("headCode", headCode);
      	    //==============================================================	    
      	    
      	    log.info("buCommonPhrase.headCode = "+headCode);
      	    List<BuCommonPhraseLine> buCommonPhraseLine = buCommonPhraseLineDAO.findCommonPhraseEnablePageLine(headCode, iSPage, iPSize);

      	    if (buCommonPhraseLine != null && buCommonPhraseLine.size() > 0) {
      	    	// 取得第一筆的INDEX
      	    	Long firstIndex = (iSPage-1L)*iPSize;
      	    	log.info("firstIndex = " + firstIndex);
      	    	// 取得最後一筆 INDEX
      	    	Long maxIndex = buCommonPhraseLineDAO.findCommonPhraseEnablePageLineMaxIndex(headCode);
      	    	log.info("maxIndex = " + maxIndex);
      	    	//assignItemDefaultValue( map, imMovementItems,  true);
      	    	//refreshImMovement(map, imMovementItems);
      	    	log.info("BuCommonPhrase.AjaxUtils.getAJAXPageData ");
      	    	result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, buCommonPhraseLine, gridDatas, firstIndex, maxIndex));
      	    } else {
      	    	log.info("BuCommonPhrase.AjaxUtils.getAJAXPageDataDefault ");
      	    	result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,GRID_FIELD_NAMES, GRID_FIELD_DEFAULT_VALUES, map,gridDatas));
      	    }
      	
      	    return result;
      	}catch(Exception ex){
      	    log.error("載入頁面顯示的調撥明細發生錯誤，原因：" + ex.toString());
      	    throw new Exception("載入頁面顯示的調撥明細失敗！");
      	}	
      }*/
      
	public List<Properties> updateAJAXPageLinesData(Properties httpRequest)throws Exception {

		try {
			Date date = new Date();
    	    String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
    	    int gridLineFirstIndex = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
    	    int gridRowCount = NumberUtils.getInt(httpRequest.getProperty(AjaxUtils.GRID_ROW_COUNT));
    	    String headCode = httpRequest.getProperty("headCode");
    	    String errorMsg = null;

			if (!(headCode.equals(null)||headCode == "")) 
			{
				log.info("headCode="+headCode);
				BuCommonPhraseHead buCommonPhraseHead = new BuCommonPhraseHead(headCode);
				// 將STRING資料轉成List Properties record data
				List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData, gridLineFirstIndex, gridRowCount, GRID_FIELD_NAMES);
				int indexNo = buCommonPhraseLineDAO.findCommonPhraseEnablePageLineMaxIndex(headCode).intValue();
				if (upRecords.size()>0) 
				{
					for (Properties upRecord : upRecords)
					{

						String lineCode = upRecord.getProperty("lineCode");
						if(null==lineCode||lineCode.equals(""))
						{
							log.info("無資料");
						}
						else
						{

							if(StringUtils.hasText(lineCode))
							{	
								BuCommonPhraseLineId buCommonPhraseLineId = new BuCommonPhraseLineId(buCommonPhraseHead, lineCode);
								BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById(buCommonPhraseLineId);



								if (buCommonPhraseLine != null)
								{
									buCommonPhraseLine.setAttribute1(upRecord.getProperty("attribute1"));
									buCommonPhraseLine.setAttribute2(upRecord.getProperty("attribute2"));
									buCommonPhraseLine.setAttribute3(upRecord.getProperty("attribute3"));
									buCommonPhraseLine.setAttribute4(upRecord.getProperty("attribute4"));
									buCommonPhraseLine.setAttribute5(upRecord.getProperty("attribute5"));
									//buCommonPhraseLine.setCreatedBy(upRecord.getProperty("lineCode"));
									//buCommonPhraseLine.setCreationDate(upRecord.getProperty("lineCode"));
									buCommonPhraseLine.setDescription(upRecord.getProperty("description"));
									buCommonPhraseLine.setEnable(upRecord.getProperty("enable"));
									//buCommonPhraseLine.setIndexNo(upRecord.getProperty("lineCode"));
									//buCommonPhraseLine.setLastUpdateDate(upRecord.getProperty("lineCode"));
									//buCommonPhraseLine.setLastUpdatedBy(upRecord.getProperty("lineCode"));
									buCommonPhraseLine.setName(upRecord.getProperty("name"));
									buCommonPhraseLine.setParameter1(upRecord.getProperty("parameter1"));
									buCommonPhraseLine.setParameter2(upRecord.getProperty("parameter2"));
									buCommonPhraseLine.setParameter3(upRecord.getProperty("parameter3"));
									buCommonPhraseLine.setParameter4(upRecord.getProperty("parameter4"));
									buCommonPhraseLine.setParameter5(upRecord.getProperty("parameter5"));
									buCommonPhraseLine.setReserve1(upRecord.getProperty("reserve1"));
									buCommonPhraseLine.setReserve2(upRecord.getProperty("reserve2"));
									buCommonPhraseLine.setReserve3(upRecord.getProperty("reserve3"));
									buCommonPhraseLine.setReserve4(upRecord.getProperty("reserve4"));
									buCommonPhraseLine.setReserve5(upRecord.getProperty("reserve5"));
									

	    		    				buCommonPhraseLineDAO.update(buCommonPhraseLine);
									//}
								} else {

									indexNo++;
		    	  	    			log.info("updateAJAXPageLinesData.save() , lineCode = " + lineCode);
		    	  	    				BuCommonPhraseLine newLine = new BuCommonPhraseLine(buCommonPhraseLineId);
		    		    				//AjaxUtils.setPojoProperties(buCommonPhraseLine, upRecord, GRID_FIELD_NAMES, GRID_FIELD_TYPES);
		    	  	    				newLine.setAttribute1(upRecord.getProperty("attribute1"));
		    	  	    				newLine.setAttribute2(upRecord.getProperty("attribute2"));
		    	  	    				newLine.setAttribute3(upRecord.getProperty("attribute3"));
		    	  	    				newLine.setAttribute3(upRecord.getProperty("attribute4"));
		    	  	    				newLine.setAttribute5(upRecord.getProperty("attribute5"));
										//buCommonPhraseLine.setCreatedBy(upRecord.getProperty("lineCode"));
										//buCommonPhraseLine.setCreationDate(upRecord.getProperty("lineCode"));
		    	  	    				newLine.setDescription(upRecord.getProperty("description"));
										//buCommonPhraseLine.setEnable(upRecord.getProperty("enable"));
										//buCommonPhraseLine.setIndexNo(upRecord.getProperty("lineCode"));
										//buCommonPhraseLine.setLastUpdateDate(upRecord.getProperty("lineCode"));
										//buCommonPhraseLine.setLastUpdatedBy(upRecord.getProperty("lineCode"));
		    	  	    				newLine.setName(upRecord.getProperty("name"));
		    	  	    				newLine.setParameter1(upRecord.getProperty("parameter1"));
		    	  	    				newLine.setParameter2(upRecord.getProperty("parameter2"));
		    	  	    				newLine.setParameter3(upRecord.getProperty("parameter3"));
		    	  	    				newLine.setParameter4(upRecord.getProperty("parameter4"));
		    	  	    				newLine.setParameter5(upRecord.getProperty("parameter5"));
		    	  	    				newLine.setReserve1(upRecord.getProperty("reserve1"));
		    	  	    				newLine.setReserve2(upRecord.getProperty("reserve2"));
										newLine.setReserve3(upRecord.getProperty("reserve3"));
										newLine.setReserve4(upRecord.getProperty("reserve4"));
										newLine.setReserve5(upRecord.getProperty("reserve5"));
										newLine.setEnable("Y");
										newLine.setIndexNo(Long.valueOf(indexNo));
		    		    				buCommonPhraseLineDAO.save(newLine);
								}
							}
						}		    		
					}	
				}
			}
			else
			{
				//errorMsg = "NO ShopCode";
			}

			return AjaxUtils.getResponseMsg(errorMsg);

			//if (warehouseCode == null || warehouseCode=="") {
			//	System.out.println("我進來了~~~");
			//throw new ValidationErrorException("傳入的倉庫維護明細資料的主鍵為空值！");
			//}
		} catch (Exception ex) {
			log.error("更新倉庫維護檔明細時發生錯誤，原因：" + ex.toString());
			ex.printStackTrace();
			throw new Exception("更新倉庫維護檔明細失敗！" + ex.getMessage());
		}

		//return new ArrayList();
	}
    
    public Map updateMarquee(Map parameterMap) throws Exception {

		 MessageBox msgBox = new MessageBox();
		 Map resultMap = new HashMap(0);
		 String resultMsg = null;
		 
		 try {
			 Object formBindBean = parameterMap.get("vatBeanFormBind");

			 BuCommonPhraseHead buCommonPhraseHead = (BuCommonPhraseHead)parameterMap.get("entityBean");
			 
			 String enable = (String) PropertyUtils.getProperty(formBindBean, "enable");
			 String attribute1 = (String) PropertyUtils.getProperty(formBindBean, "attribute1");
			 
			 
			 List buCommonPhraseLines = buCommonPhraseHead.getBuCommonPhraseLines();
			 BuCommonPhraseLine buCommonPhraseLine = (BuCommonPhraseLine)buCommonPhraseLines.get(0);
 			     buCommonPhraseLine.setEnable(enable);
			     buCommonPhraseLine.setAttribute1(attribute1);
			     
			     buCommonPhraseHead.setBuCommonPhraseLines(buCommonPhraseLines);
				 // 若國別不存在,則SAVE 反之UPDATE
				 
				 log.info("update");
				 buCommonPhraseHeadDAO.merge(buCommonPhraseHead);
				
			
			 resultMsg = "跑馬燈存檔成功！";
             resultMap.put("resultMsg", resultMsg);
			 resultMap.put("entityBean", buCommonPhraseLine);
			 resultMap.put("vatMessage", msgBox);
			 return resultMap;

		 } catch (Exception ex) {
			 log.error("跑馬燈維護存檔時發生錯誤，原因：" + ex.toString());
			 throw new Exception("跑馬燈維護存檔失敗，原因：" + ex.toString());
		 }
	 }
    
    public void updateMarqueeBean(Map parameterMap)throws FormException, Exception {
		 try{
			 Object formBindBean = parameterMap.get("vatBeanFormBind");
//			 Object formLinkBean = parameterMap.get("vatBeanFormLink");
//			 Object otherBean = parameterMap.get("vatBeanOther");
			 BuCommonPhraseHead buCommonPhraseHead  = null;
			 buCommonPhraseHead = findById("Marquee");
			 parameterMap.put("entityBean", buCommonPhraseHead);
			 
		 } catch (FormException fe) {
			 log.error("前端資料塞入bean失敗，原因：" + fe.toString());
			 throw new FormException(fe.getMessage());
		 } catch (Exception ex) {
			 log.error("前端資料塞入bean發生錯誤，原因：" + ex.toString());
			 throw new Exception("跑馬資料塞入bean發生錯誤，原因：" + ex.getMessage());
		 }
	 }
    
    public List<Properties> getAJAXFormDataByHeadCode(Properties httpRequest)throws Exception {
		//log.info("getFormDataBySupplier");
		Properties pro = new Properties();
		List re = new ArrayList();

		String headCode = httpRequest.getProperty("headCode");
		log.info("headCode:"+headCode);

		BuCommonPhraseHead buCommonPhraseHead = buCommonPhraseHeadDAO.findById(headCode);

		if (null != buCommonPhraseHead ) {
			pro.setProperty("enable",    AjaxUtils.getPropertiesValue(buCommonPhraseHead.getEnable(),  ""));
			pro.setProperty("name",    AjaxUtils.getPropertiesValue(buCommonPhraseHead.getName(),  ""));
			pro.setProperty("description",    AjaxUtils.getPropertiesValue(buCommonPhraseHead.getDescription(),  ""));
			pro.setProperty("reserve1",    AjaxUtils.getPropertiesValue(buCommonPhraseHead.getReserve1(),  ""));
			pro.setProperty("reserve2",    AjaxUtils.getPropertiesValue(buCommonPhraseHead.getReserve2(),  ""));
			pro.setProperty("reserve3",    AjaxUtils.getPropertiesValue(buCommonPhraseHead.getReserve3(),  ""));
			pro.setProperty("reserve4",    AjaxUtils.getPropertiesValue(buCommonPhraseHead.getReserve4(),  ""));
			pro.setProperty("reserve5",    AjaxUtils.getPropertiesValue(buCommonPhraseHead.getReserve5(),  ""));


			
		} else {
			log.info("新的設定檔");
			
		}
		re.add(pro);
		return re;
	    }
    public List<Properties> getAJAXSearchPageData(Properties httpRequest)
    throws Exception {
    	try {
    		List<Properties> result = new ArrayList();
    		List<Properties> gridDatas = new ArrayList();
    		int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
    		int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小

    		// ======================帶入Head的值=========================

    		String headCode = httpRequest.getProperty("headCode");
    		String name = httpRequest.getProperty("name");
    		String enable = httpRequest.getProperty("enable");


    		HashMap map = new HashMap();
    		HashMap findObjs = new HashMap();


    		findObjs.put(" and model.headCode like :headCode", "%" + headCode + "%");
    		findObjs.put(" and model.name like :name", "%" + name + "%");
    		findObjs.put(" and model.enable = :enable", enable);

    		// ==============================================================

    		Map buCommonPhraseHeadMap = buCommonPhraseHeadDAO.search("BuCommonPhraseHead as model", findObjs,"order by headCode asc", iSPage, iPSize,BaseDAO.QUERY_SELECT_RANGE);

    		List<BuCommonPhraseHead> buCommonPhraseHeads = (List<BuCommonPhraseHead>) buCommonPhraseHeadMap.get(BaseDAO.TABLE_LIST);

    		if (buCommonPhraseHeads != null && buCommonPhraseHeads.size() > 0) {

    			Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
    			Long maxIndex = (Long) buCommonPhraseHeadDAO.search("BuCommonPhraseHead as model",
    					"count(model.headCode) as rowCount", findObjs,
    					"order by headCode desc", BaseDAO.QUERY_RECORD_COUNT)
    					.get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX

    			result.add(AjaxUtils.getAJAXPageData(httpRequest,
    					GRID_SEARCH_FIELD_NAMES,
    					GRID_SEARCH_FIELD_DEFAULT_VALUES, buCommonPhraseHeads, gridDatas,
    					firstIndex, maxIndex));
    		} else {
    			result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
    					GRID_SEARCH_FIELD_NAMES,
    					GRID_SEARCH_FIELD_DEFAULT_VALUES, map, gridDatas));
    		}

    		return result;
    	} catch (Exception ex) {
    		log.error("載入頁面顯示的店別查詢發生錯誤，原因：" + ex.toString());
    		throw new Exception("載入頁面顯示的倉庫查詢失敗！");
    	}
    }
    public List<Properties> getSearchSelection(Map parameterMap)
    throws FormException, Exception {
    	Map resultMap = new HashMap(0);
    	Map pickerResult = new HashMap(0);
    	try {
    		Object pickerBean = parameterMap.get("vatBeanPicker");
    		String timeScope = (String) PropertyUtils.getProperty(pickerBean,
    				AjaxUtils.TIME_SCOPE);
    		ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
    				pickerBean, AjaxUtils.SEARCH_KEY);

    		List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
    				searchKeys);
    		System.out.println("size:::" + result.size());
    		if (result.size() > 0) {
    			pickerResult.put("result", result);
    		}
    		resultMap.put("vatBeanPicker", pickerResult);
    		resultMap.put("topLevel", new String[] { "vatBeanPicker" });
    	} catch (Exception ex) {
    		log.error("檢視失敗，原因：" + ex.toString());
    		Map messageMap = new HashMap();
    		messageMap.put("type", "ALERT");
    		messageMap.put("message", "檢視失敗，原因：" + ex.toString());
    		messageMap.put("event1", null);
    		messageMap.put("event2", null);
    		resultMap.put("vatMessage", messageMap);

    	}
    	return AjaxUtils.parseReturnDataToJSON(resultMap);
    }
    public List<Properties> saveSearchResult(Properties httpRequest)
    throws Exception {
    	String errorMsg = null;
    	AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
    	return AjaxUtils.getResponseMsg(errorMsg);
    }
}
