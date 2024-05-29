package tw.com.tm.erp.hbm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuCompany;
import tw.com.tm.erp.hbm.bean.MessageBox;
import tw.com.tm.erp.hbm.bean.SiProgramLog;

import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.BeanUtil;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.UserUtils;

import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;

import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.SiProgramLogDAO;


public class CustomsUploadSettingService {
	private static final Log log = LogFactory.getLog(CustomsUploadSettingService.class);
	
	BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	SiProgramLogDAO siProgramLogDAO;
	
	public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}
	
	public void setSiProgramLogDAO(SiProgramLogDAO siProgramLogDAO) {
		this.siProgramLogDAO = siProgramLogDAO;
	}
	
	public Map executeInitial(Map parameterMap) throws Exception {
		Map resultMap = new HashMap(0);
		
		try {
			BuCommonPhraseLine buCommonPhraseLineSchedule = buCommonPhraseLineDAO.findById("Switch", "realTime");
			BuCommonPhraseLine buCommonPhraseLineRealTime = buCommonPhraseLineDAO.findById("StraightUploadCustoms", "Switch");
		
			resultMap.put("scheduleEnable", buCommonPhraseLineSchedule.getEnable());
			resultMap.put("scheduleBeginingTime", buCommonPhraseLineSchedule.getAttribute1());
			resultMap.put("scheduleEndingTime", buCommonPhraseLineSchedule.getAttribute2());
			resultMap.put("scheduleHours", buCommonPhraseLineSchedule.getAttribute3());
			resultMap.put("scheduleHoursAgo", buCommonPhraseLineSchedule.getAttribute4());
			resultMap.put("realTimeEnable", buCommonPhraseLineRealTime.getEnable());
			resultMap.put("realTimeBeginingTime", buCommonPhraseLineRealTime.getAttribute1());
			resultMap.put("realTimeEndingTime", buCommonPhraseLineRealTime.getAttribute2());
			resultMap.put("realTimeHours", buCommonPhraseLineRealTime.getAttribute3());
			
			return resultMap;
		} catch (Exception ex) {
			log.error("海關上傳設定初始化失敗，原因：" + ex.toString());
			throw new Exception("海關上傳設定初始化失敗，原因：" + ex.toString());
		}
	}
	
	public Map updateAJAX(Map parameterMap) throws Exception {
		MessageBox msgBox = new MessageBox();
		Map resultMap = new HashMap(0);
		String resultMsg = null;
		Date date = new Date();
		SiProgramLog siProgramLog = new SiProgramLog();
		BuCommonPhraseLine buCommonPhraseLine = new BuCommonPhraseLine();
		String headCode = "";
		String lineCode = "";
		String uploadWayMsg = "";
		try{
			Object otherBean = parameterMap.get("vatBeanOther");
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			
			String loginEmplyeeCode = (String) PropertyUtils.getProperty(otherBean, "loginEmployeeCode");
			String beginingTime = (String) PropertyUtils.getProperty(formBindBean, "beginingTime");
			String endingTime = (String) PropertyUtils.getProperty(formBindBean, "endingTime");
			String hours = (String) PropertyUtils.getProperty(formBindBean, "hours");
			String enable = (String) PropertyUtils.getProperty(formBindBean, "enable");
			String uploadWay = (String) PropertyUtils.getProperty(formBindBean, "uploadWay");
			String hoursAgo = (String) PropertyUtils.getProperty(formBindBean, "hoursAgo");
			
			if("schedule".equals(uploadWay)){
				headCode = "Switch";
				lineCode = "realTime";
			}else if("realTime".equals(uploadWay)){
				headCode = "StraightUploadCustoms";
				lineCode = "Switch";
			}
			
			buCommonPhraseLine = buCommonPhraseLineDAO.findById(headCode, lineCode);
			
			if(buCommonPhraseLine != null)
			{
				if("schedule".equals(uploadWay)){
					log.info("更新排程");
					buCommonPhraseLine.setEnable(enable);
					buCommonPhraseLine.setAttribute1(beginingTime);
					buCommonPhraseLine.setAttribute2(endingTime);
					buCommonPhraseLine.setAttribute3(hours);
					buCommonPhraseLine.setAttribute4(hoursAgo);
					buCommonPhraseLine.setLastUpdateDate(date);
					buCommonPhraseLine.setLastUpdatedBy(loginEmplyeeCode);
					uploadWayMsg = "異動排程設定";
					resultMsg = "排程設定成功";
				}else{
					log.info("更新即時");
					buCommonPhraseLine.setEnable(enable);
					buCommonPhraseLine.setLastUpdateDate(date);
					buCommonPhraseLine.setLastUpdatedBy(loginEmplyeeCode);
					uploadWayMsg = "異動即時設定";
					resultMsg = "即時設定成功";
				}
				
				
				buCommonPhraseLineDAO.update(buCommonPhraseLine);
				
				siProgramLog.setProgramId("CustomsUploadSetting");
				siProgramLog.setLevelType("EDIT");
				siProgramLog.setMessage(uploadWayMsg+"，啟閉開關更改為:"+enable+" 開啟時間更改為:"+beginingTime+" 結束時間更改為:"+endingTime+" 間隔時間更改為:"+hours+"追朔時間更改為:"+hoursAgo);
				siProgramLog.setCreatedBy(loginEmplyeeCode);
				siProgramLog.setCreationDate(date);
				
				siProgramLogDAO.save(siProgramLog);
				
			}
			
			resultMap.put("resultMsg", resultMsg);
			resultMap.put("vatMessage", msgBox);
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("公司維護單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("公司維護單單存檔失敗，原因：" + ex.toString());
		}
	}

}
