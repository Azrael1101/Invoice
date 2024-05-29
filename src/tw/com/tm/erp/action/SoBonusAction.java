package tw.com.tm.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.service.SoBonusService;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;

public class SoBonusAction {
	private static final Log log = LogFactory.getLog(SoBonusAction.class);
	
	private SoBonusService soBonusService;
	
	public void setSoBonusService(SoBonusService soBonusService) {
		this.soBonusService = soBonusService;
	}

	public List<Properties> performSoBonusTransaction(Map parameterMap) throws Exception {
	    	Map returnMap = new HashMap(0);
		MessageBox msgBox = new MessageBox();
		long logId = 0;
		try{ 
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			String startDate = (String) PropertyUtils.getProperty(formBindBean, "startDate");
			String endDate = (String) PropertyUtils.getProperty(formBindBean, "endDate");
			
			//驗證輸入參數...
			if(startDate == null || startDate.trim().length() == 0){
			    msgBox.setMessage("請輸入開始日期!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
			else if(endDate == null || endDate.trim().length() == 0){
			    msgBox.setMessage("請輸入結束日期!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
			else if(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate).after(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate))){
			    msgBox.setMessage("開始日期不可大於結束日期!");
			    returnMap.put("vatMessage" ,msgBox);
			    return AjaxUtils.parseReturnDataToJSON(returnMap);
			}
			
			//計算獎金...
			String year = soBonusService.getYear(startDate), month = soBonusService.getMonth(startDate);
			String yearMonth = year + month;
			boolean isBusying = soBonusService.isBusying(yearMonth);
			if(!isBusying){
				//檢查業績目標設定...
				if(soBonusService.getBuGoalEmployeeCount(year, month) == 0){
					msgBox.setMessage("業績目標主檔或員工檔未設定,請檢查報表T2獎金報表(HR)-員工業績目標");
				    returnMap.put("vatMessage" ,msgBox);
				    return AjaxUtils.parseReturnDataToJSON(returnMap);
				}
				else if(soBonusService.getBuGoalTargetCount(year, month) == 0){
					msgBox.setMessage("業績目標主檔或銷售檔未設定,請檢查報表T2獎金報表(HR)-達成率業績有效大類");
				    returnMap.put("vatMessage" ,msgBox);
				    return AjaxUtils.parseReturnDataToJSON(returnMap); 
				}
				else if(soBonusService.getBuGoalWorkCount(year, month) == 0){
					msgBox.setMessage("業績目標主檔或班別檔未設定,請檢查報表T2獎金報表(HR)-班別目標");
				    returnMap.put("vatMessage" ,msgBox);
				    return AjaxUtils.parseReturnDataToJSON(returnMap); 
				}
				
				StringBuilder msg = new StringBuilder();
				if(soBonusService.getEmployeeCountWithoutGoal(startDate, endDate, year, month) > 0){
					msg.append("部分有業績的人員未設定業績目標,請檢查報表T2獎金未設定目標人員\\t");
				}
				
			    //1.開始計算寫log
			    logId = soBonusService.saveSoBonusLog(yearMonth, "SYS");
			    
			    //2.利用SO_BONUS_VIEW, BU_GOAL_COMMISSION先算出營業員的銷售明細(包含金額,折數,實際業績)
			    soBonusService.saveTmpSoBonus(startDate, endDate);
			    
			    //3.依據BU_GOAL_COMMISSION_EXCEPTION更新TMP_SO_BONUS的抽成率...
			    soBonusService.updateTmpSoBonusDiscountRate(year, month);
			    
			    //4.重新計算分項抽成獎金
			    soBonusService.calculateTmpSoBonusCommission(year, month);
			    
			    //5.計算加總抽成獎金
			    soBonusService.saveSoBonus(startDate);
			    
			    //6.計算降抽(達成率)
			    soBonusService.calculateBonusPercentage(startDate, endDate);
			    
			    //7.計算群達成資料
			    //final String YEAR = year, MONTH = month, DAY = soBonusService.getDay(endDate) + "";
			    //new Thread(
			    	//new Runnable(){
					//public void run(){
						//try{
			    				//soBonusService.calculateGroupAchevement(YEAR, MONTH, DAY);
							//}
							//catch(Exception e){
							//}
						//}
					//}	
				//);
			    
			    //7.計算完畢寫log
			    soBonusService.updateSoBonusLog(logId, "FINISH");
			    msg.append("獎金計算完成!");
			    msgBox.setMessage(msg.toString());
			}
			else
			    msgBox.setMessage("獎金計算中!");
		}
		catch (Exception ex){
		    soBonusService.updateSoBonusLog(logId, "ERROR");
		    msgBox.setMessage("執行獎金計算作業失敗，原因：" + ex.getMessage());
		}
		returnMap.put("vatMessage" ,msgBox);
		return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
	public List<Properties> performGroupAchevement(Map parameterMap) throws Exception {
    		Map returnMap = new HashMap(0);
    		MessageBox msgBox = new MessageBox();
        	try{ 
            		Object formBindBean = parameterMap.get("vatBeanFormBind");
            		String startDate = (String) PropertyUtils.getProperty(formBindBean, "startDate");
            		String endDate = (String) PropertyUtils.getProperty(formBindBean, "endDate");
            		//驗證輸入參數...
            		if(startDate == null || startDate.trim().length() == 0){
            		    msgBox.setMessage("請輸入開始日期!");
            		    returnMap.put("vatMessage" ,msgBox);
            		    return AjaxUtils.parseReturnDataToJSON(returnMap);
            		}
            		else if(endDate == null || endDate.trim().length() == 0){
            		    msgBox.setMessage("請輸入結束日期!");
            		    returnMap.put("vatMessage" ,msgBox);
            		    return AjaxUtils.parseReturnDataToJSON(returnMap);
            		}
            		else if(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, startDate).after(DateUtils.parseDate(DateUtils.C_DATE_PATTON_SLASH, endDate))){
            		    msgBox.setMessage("開始日期不可大於結束日期!");
            		    returnMap.put("vatMessage" ,msgBox);
            		    return AjaxUtils.parseReturnDataToJSON(returnMap);
            		}
            		//檢查業績目標設定...
            		String year = soBonusService.getYear(startDate), month = soBonusService.getMonth(startDate);
            		if(soBonusService.getBuGoalEmployeeCount(year, month) == 0){
    					msgBox.setMessage("業績目標主檔或員工檔未設定,請檢查報表T2獎金報表(HR)-員工業績目標");
    				    returnMap.put("vatMessage" ,msgBox);
    				    return AjaxUtils.parseReturnDataToJSON(returnMap);
    				}
    				else if(soBonusService.getBuGoalTargetCount(year, month) == 0){
    					msgBox.setMessage("業績目標主檔或銷售檔未設定,請檢查報表T2獎金報表(HR)-達成率業績有效大類");
    				    returnMap.put("vatMessage" ,msgBox);
    				    return AjaxUtils.parseReturnDataToJSON(returnMap); 
    				}
    				else if(soBonusService.getBuGoalWorkCount(year, month) == 0){
    					msgBox.setMessage("業績目標主檔或班別檔未設定,請檢查報表T2獎金報表(HR)-班別目標");
    				    returnMap.put("vatMessage" ,msgBox);
    				    return AjaxUtils.parseReturnDataToJSON(returnMap); 
    				}
            		//計算群達成資料...
            		soBonusService.calculateGroupAchevement(year, month, soBonusService.getDay(endDate) + "");
            		msgBox.setMessage("群達成資料計算完畢!");
        	}
        	catch (Exception ex){
        	    	msgBox.setMessage("執行群達成資料失敗，原因：" + ex.getMessage());
        	}
        	returnMap.put("vatMessage" ,msgBox);
        	return AjaxUtils.parseReturnDataToJSON(returnMap);
	}
	
}
