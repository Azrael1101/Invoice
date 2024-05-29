package tw.com.tm.erp.exportdb;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.NativeQueryDAO;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.MailUtils;
import tw.com.tm.erp.utils.NumberUtils;
import tw.com.tm.erp.utils.SiSystemLogUtils;
import tw.com.tm.erp.utils.StringTools;

public class CheckExportData {
    private static final Log log = LogFactory.getLog(CheckExportData.class);
    public static String PROCESS_NAME = "CHEAK_DATA";
    
    public BuOrderTypeService buOrderTypeService;
    
    private NativeQueryDAO nativeQueryDAO;
    private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
    
    public void setBuCommonPhraseLineDAO(BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
        this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
    }

    public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
        this.buOrderTypeService = buOrderTypeService;
    }

    public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
        this.nativeQueryDAO = nativeQueryDAO;
    }

    public void execute(){
	String uuid = UUID.randomUUID().toString();
	Date date = new Date();
	StringBuffer pa = new StringBuffer();
	StringBuffer po = new StringBuffer();
	StringBuffer sbUrl = new StringBuffer();
	String opUser = "SYS";
	String config = "CheckData";
	Map group = new HashMap();
	try {
	    
	    List<BuCommonPhraseLine> buCommonPhraseLines = buCommonPhraseLineDAO.findByProperty("BuCommonPhraseLine", "and id.buCommonPhraseHead.headCode = ? ", new Object[]{ config});
	    if(null == buCommonPhraseLines){
		throw new Exception("查無常用字彙無配置"+config);
	    }
	    for (BuCommonPhraseLine buCommonPhraseLine2 : buCommonPhraseLines) {
		String brandCode = buCommonPhraseLine2.getAttribute4();
		String orderTypeCode = buCommonPhraseLine2.getAttribute5();
		String serverPath = buCommonPhraseLine2.getAttribute1();
		String mailto = buCommonPhraseLine2.getAttribute2();
		String pageTemplete = buCommonPhraseLine2.getAttribute3();
		String bufferDate = buCommonPhraseLine2.getParameter1();
		
		String[] mailAddressArray = StringTools.StringToken(mailto.toString(), ",");
		
		pa.delete(0,pa.length());
		// 檢查 訂變價 
		pa.append("SELECT H.HEAD_ID, H.BRAND_CODE, H.ORDER_NO, H.ORDER_TYPE_CODE, H.STATUS, H.ENABLE_DATE, H.CREATED_BY ")
		.append("FROM IM_PRICE_ADJUSTMENT H ")
		.append("WHERE SYSDATE - ").append(bufferDate).append(" >= ENABLE_DATE ")
		.append("AND H.STATUS = 'SIGNING' ")
		.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
		.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
		.append("ORDER BY H.BRAND_CODE, H.ORDER_NO, H.ORDER_TYPE_CODE ");
		
		List palist = nativeQueryDAO.executeNativeSql(pa.toString());
		for (int i = 0; i < palist.size(); i++) {
		    Object obj = palist.get(i);
		    try {
			Long headId = NumberUtils.getLong(String.valueOf(((Object[])obj)[0]));
			String orderNo = String.valueOf(((Object[])obj)[2]);

			String priceServerPath = serverPath + pageTemplete + "orderTypeCode="+orderTypeCode+"&formId="+headId;
			priceServerPath += "&redirect=Y";

			sbUrl.delete(0, sbUrl.length());
			sbUrl.append("<a href=");
			sbUrl.append(priceServerPath);		    
			sbUrl.append(">");
			sbUrl.append(MessageStatus.getJobManagerMsg(brandCode, orderTypeCode, orderNo)).append(" 快到期或已到期");
			sbUrl.append("</a><br>");
			
			// 將相同人的要寄的資料 SUM起來
			for (String mailAddress : mailAddressArray) {
			    if(!group.containsKey(mailAddress)){
				group.put(mailAddress, sbUrl.toString());
			    }else{
				group.put(mailAddress, group.get(mailAddress)+sbUrl.toString());
			    }
			}
			
		    } catch (Exception e) {
			log.error(e.getMessage());
			SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR,"檢核訂變價資料發生錯誤，原因 ：" + e.getMessage(), date, uuid, opUser);
		    }
		}
		
		po.delete(0,po.length());
		po.append("SELECT H.HEAD_ID, H.BRAND_CODE, H.ORDER_NO, H.ORDER_TYPE_CODE, H.STATUS, H.BEGIN_DATE, H.CREATED_BY ")
		.append("FROM IM_PROMOTION H ")
		.append("WHERE SYSDATE - ").append(bufferDate).append(" >= BEGIN_DATE ")
		.append("AND H.STATUS = 'SIGNING' ")
		.append("AND H.BRAND_CODE = '").append(brandCode).append("' ")
		.append("AND H.ORDER_TYPE_CODE = '").append(orderTypeCode).append("' ")
		.append("ORDER BY H.BRAND_CODE, H.ORDER_NO, H.ORDER_TYPE_CODE ");
		// 檢查 促銷價
		List polist = nativeQueryDAO.executeNativeSql(po.toString());
		for (int i = 0; i < polist.size(); i++) {
		    Object obj = polist.get(i);
		    try {
			Long headId = NumberUtils.getLong(String.valueOf(((Object[])obj)[0]));
			String orderNo = String.valueOf(((Object[])obj)[2]);

			String promotionServerPath = serverPath + pageTemplete +"formId="+headId;
			promotionServerPath += "&redirect=Y";

			sbUrl.delete(0, sbUrl.length());
			sbUrl.append("<a href=");
			sbUrl.append(promotionServerPath);		    
			sbUrl.append(">");
			sbUrl.append(MessageStatus.getJobManagerMsg(brandCode, orderTypeCode, orderNo)).append(" 快到期或已到期");
			sbUrl.append("</a><br>");

			// 將相同人的要寄的資料 SUM起來
			for (String mailAddress : mailAddressArray) {
			    if(!group.containsKey(mailAddress)){
				group.put(mailAddress, sbUrl.toString());
			    }else{
				group.put(mailAddress, group.get(mailAddress)+sbUrl.toString());
			    }
			}
			
		    } catch (Exception e) {
			log.error(e.getMessage());
			SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR,"檢核促銷價資料發生錯誤，原因 ：" + e.getMessage(), date, uuid, opUser);
		    }
		}
	    }
	    
            // 寄給依EMAIL為KEY SUM每個不同品牌的有資料問題為value
            Iterator it = group.keySet().iterator();
            while (it.hasNext()) {
		String mailAddress = (String) it.next();
		String body = (String)group.get(mailAddress);
		Map root = new HashMap();
		root.put("display", body);
		
		String[] bodys = body.split("<br>");
		log.info("mailAddress = " + mailAddress);
		log.info("有問題資料筆數 = " + bodys.length);
		// 主題
		StringBuffer subject = new StringBuffer();
		subject.append(DateUtils.getCurrentDateStr(DateUtils.C_DATA_PATTON_YYYYMMDD)).append(" 檢核有問題的資料共").append(bodys.length).append("筆");
		
		MailUtils.sendMail(subject.toString(), "CommonTemplate.ftl", root , mailAddress);
	    }
            
	    
	} catch (Exception e) {
	    log.error(e.getMessage());
	    SiSystemLogUtils.createSystemLog(PROCESS_NAME,MessageStatus.LOG_ERROR,"檢核資料發生錯誤，原因 ：" + e.getMessage(), date, uuid, opUser);
	}
    }
}
