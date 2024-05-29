package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.BuCountry;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.PosControl;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.PosControlDAO;
import tw.com.tm.erp.utils.AjaxUtils;

    public class PosControlService {
    
    private static final Log log = LogFactory.getLog(PosControlService.class);
    private PosControlDAO posControlDAO;

    public void setPosControlDAO(PosControlDAO posControlDAO) {
	    this.posControlDAO = posControlDAO;
	}
    
	public static final String[] GRID_SEARCH_POSCONTROL_FIELD_NAMES = { 
		 "brandCode", "dataType", "executeType",
		 "frequence", "indexNo", "machineCode",
		 "timeout","transfer","remark",
		 "brandCode"
	};
	
	public static final String[] GRID_SEARCH_POSCONTROL_FIELD_DEFAULT_VALUES = { 
		"","","",
		"","","",
		"","","",
		""
	};
	
    public Map updatePosCtrl(Map parameterMap) {
                Map resultMap = new HashMap(0);
                MessageBox msgBox = new MessageBox();
                String resultMsg = null;
	try{
               Object otherBean = parameterMap.get("vatBeanOther");
               PosControl posCtrl = new PosControl();
               setPosControl(parameterMap,posCtrl);
//               String isOppositePicker = (String) PropertyUtils.getProperty(otherBean, "isOppositePicker");
//               String fieldName[] ={"brandCode","dataType","machineCode"};
//               String fieldValue[] = {posCtrl.getBrandCode(),posCtrl.getDataType(),posCtrl.getMachineCode()};
//               List result = posControlDAO.findPosControl(posCtrl.getBrandCode(),posCtrl.getDataType(),posCtrl.getMachineCode());
//               System.out.println("====="+isOppositePicker);
               boolean check = posControlDAO.checkInsertOrUpdate(posCtrl.getBrandCode(),posCtrl.getDataType(),posCtrl.getMachineCode());
          
//               if(result.size()>0){
               	if(check){
               	    posControlDAO.update(posCtrl);     	
               	    resultMsg = "修改成功";
               }else{
        	   posControlDAO.save(posCtrl);
        	   resultMsg = "新增成功!";
               }
           	
        	resultMap.put("resultMsg", resultMsg);
        	resultMap.put("vatMessage", msgBox);
        	return resultMap;
	}catch(Exception ex){
	    	System.out.println("==eee=="+ex.toString());
	    	resultMsg = "交易失敗! 原因 : "+ex.toString();
	    	resultMap.put("resultMsg", resultMsg);
    		resultMap.put("vatMessage", msgBox);
    		return resultMap;
    		}
    }

    private void setPosControl(Map parameterMap, PosControl posCtrl) throws Exception{
	Object formBindBean = parameterMap.get("vatBeanFormBind");
	String brandCode = (String) PropertyUtils.getProperty(formBindBean, "brandCode");
	String downloadFunction = (String) PropertyUtils.getProperty(formBindBean, "downloadFunction");
        String machineCode = (String) PropertyUtils.getProperty(formBindBean, "posMachineCode");
        String executeType = (String) PropertyUtils.getProperty(formBindBean, "executeType");
        String frequency = (String) PropertyUtils.getProperty(formBindBean, "frequency");
        String remark = (String) PropertyUtils.getProperty(formBindBean, "remark");
        String timeout = (String) PropertyUtils.getProperty(formBindBean, "timeout");
	String transfer = (String) PropertyUtils.getProperty(formBindBean, "transfer");
	String indexNo = (String) PropertyUtils.getProperty(formBindBean, "indexNo");
	String description = "";
	posCtrl.setBrandCode(brandCode);
	posCtrl.setDataType(downloadFunction);
	posCtrl.setMachineCode(machineCode);
	posCtrl.setExecuteType(executeType);
	posCtrl.setRemark(remark);
	posCtrl.setTransfer(transfer);
	if(!"".equals(frequency)&&frequency != null){
	    posCtrl.setFrequence(Long.valueOf(frequency));
	}
	if(!"".equals(timeout)&&timeout != null){
	    posCtrl.setTimeout(Long.valueOf(timeout));
	}
	if(!"".equals(indexNo)&&indexNo != null){
	    posCtrl.setIndexNo(Long.valueOf(indexNo));
	}
	
	if("EAN".equals(downloadFunction)){
	    description = "POS_ITEM_EANCODE";
	}else if("ITEM".equals(downloadFunction)){
	    description = "POS_ITEM";
	}else if("DSC".equals(downloadFunction)){
	    description = "POS_ITEM_DISCOUNT";
	}else if("EMP".equals(downloadFunction)){
	    description = "POS_EMPLOYEE";
	}else if("PMOS".equals(downloadFunction)){
	    description = "POS_EXCESSIVE_PROMOTION";
	}else if("RATE".equals(downloadFunction)){
	    description = "POS_EXCESSIVE_PROMOTION";
	}else if("VIP".equals(downloadFunction)){
	    description = "POS_CUSTOMER";
	}
	posCtrl.setDescription(description);
    }

    public List<Properties> getSearchData(Properties httpRequest) {
	try{
	    List<Properties> result = new ArrayList();
  	    List<Properties> gridDatas = new ArrayList();
  	    int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
  	    int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
  	    String brandCode = httpRequest.getProperty("brandCode");
  	    String downloadFunction = httpRequest.getProperty("downloadFunction");
  	    String machineCode = httpRequest.getProperty("machineCode");
  	    HashMap map = new HashMap();
  	    HashMap findObjs = new HashMap();
  	    findObjs.put(" and model.brandCode = :brandCode",brandCode);
  	    findObjs.put(" and model.dataType = :downloadFunction",downloadFunction);
  	    if(!"".equals(machineCode)&&machineCode != null){
  		findObjs.put(" and model.machineCode like :machineCode",machineCode);
  	    }
  	    Map posControlMap = posControlDAO.search("PosControl as model", findObjs, "order by machineCode desc", iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE );
  	    List<PosControl> posControls = (List<PosControl>) posControlMap.get(BaseDAO.TABLE_LIST);
	    Long firstIndex = Long.valueOf(iSPage * iPSize)+ 1;   
	    Long maxIndex = (Long)posControlDAO.search("PosControl as model ", "count(*) as rowCount" ,findObjs, "order by machineCode desc", iSPage, iPSize, BaseDAO.QUERY_RECORD_COUNT ).get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
  	    if(posControls != null && posControls.size()>0){
        	    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_POSCONTROL_FIELD_NAMES, GRID_SEARCH_POSCONTROL_FIELD_DEFAULT_VALUES,posControls, gridDatas, firstIndex, maxIndex));
  	    }else{
  		    result.add(AjaxUtils.getAJAXPageData(httpRequest,GRID_SEARCH_POSCONTROL_FIELD_NAMES, GRID_SEARCH_POSCONTROL_FIELD_DEFAULT_VALUES,posControls, gridDatas, firstIndex, maxIndex));
  	    }
	    return result;
	}catch(Exception ex){
	    System.out.println("==ex=="+ex.toString()); return null;
	}
    }
    
    public List<Properties> getClickData(Properties httpRequest) {
	    System.out.println("GOOOOOOOOOO");
	    String brandCode = httpRequest.getProperty("brandCode");
  	    String dataType = httpRequest.getProperty("dataType");
  	    String posMachineCode = httpRequest.getProperty("posMachineCode");
  	  System.out.println("GO");
  	    posControlDAO.findPosControl(brandCode,dataType,posMachineCode);
  	  System.out.println("GOO");
	return null;
    }
}
    
