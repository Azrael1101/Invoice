package tw.com.tm.erp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.MailConfig;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.hbm.SpringUtils;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.SiSystemLog;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.service.BuCommonPhraseService;
import tw.com.tm.erp.hbm.service.BuOrderTypeService;
import tw.com.tm.erp.utils.FreeMaker;
import tw.com.tm.erp.utils.JMail;
import freemarker.template.TemplateException; 


/**
 * Send Mail Server
 * 
 * @author shan
 */
public class MailUtils {
    private static final Log log = LogFactory.getLog(MailUtils.class);

    private static ApplicationContext context = SpringUtils.getApplicationContext();

    /**
     * send mail
     * 
     * @param subject
     * @param content
     * @param to_email
     */
    public static void sendMail(String subject, String content, String to_email) {
	log.info("MailService.sendMail 0 ");
	getJMail().doSendMail(subject, content, to_email);
    }

    /**
     * send mail
     * 
     * @param subject
     * @param templateFileName , 放置在 htmlskin 的樣本檔名稱
     * @param propMap
     *            template data
     * @param to_email
     *            send mail to
     * @throws IOException
     * @throws TemplateException
     */
    public static void sendMail(String subject, String templateFileName, Map<String, Object> propMap, String to_email) throws IOException,
    TemplateException {
	log.info("MailService.sendMail 1 ");
	FreeMaker fm = new FreeMaker();
	String content = fm.geneHtmlFile(templateFileName, propMap);
	getJMail().doSendMail(subject, content, to_email);
    }

    /**
    * send mail
    * @param subject
    * @param content
    * @param to_email
    */
    public static void sendMail(String subject, List<String> content, List<String> to_email) {
	log.info("MailService.sendMail 3 ");
	getJMail().doSendMail(subject, content, to_email, null);
    }
    
    /**
     * send mail
     * 
     * @param subject
     * @param templateFileName , 放置在 htmlskin 的樣本檔名稱
     * @param propMap
     * @param to_email
     * @throws IOException
     * @throws TemplateException
     */
    public static void sendMail(String subject, String templateFileName, Map<String, Object> propMap, List<String> to_email) throws IOException,
    TemplateException {
	log.info("MailService.sendMail 2 ");
	FreeMaker fm = new FreeMaker();
	String content = fm.geneHtmlFile(templateFileName, propMap);
	List<String> contents = new ArrayList();
	contents.add(content);
	getJMail().doSendMail(subject, contents, to_email, null);
    }

    /**
     * send mail 額外設定 cidMap
     * 
     * @param subject
     * @param templateFileName , 放置在 htmlskin 的樣本檔名稱
     * @param propMap
     * @param to_email
     * @param cidMap
     * @throws IOException
     * @throws TemplateException
     */
    public static void sendMail(String subject, String templateFileName, Map<String, Object> propMap, List<String> to_email, List<String> attachFiles, Map cidMap) throws IOException,
    TemplateException {
	log.info("MailService.sendMail 5 ");
	FreeMaker fm = new FreeMaker();
	String content = fm.geneHtmlFile(templateFileName, propMap);
	List<String> contents = new ArrayList();
	contents.add(content);
	getJMail().doSendMail(subject, contents, to_email, attachFiles, cidMap); 
    }
    
    /**
     * Java Mail 
     * @return
     */
    private static JMail getJMail(){
	log.info("MailService.getJMail");
	JMail jm = new JMail();
	jm.setStrSMTP_Server(MailConfig.SMTPSERVER);
	jm.setStrLoginName(MailConfig.SMTPLOGINNAME);
	jm.setStrLoginPassword(MailConfig.SMTPLOGINPASSWORD);
	jm.setStrFrom_email(MailConfig.DEFAULTMAILFROM);
	return jm ;
    }

    public static void sendMail(Object entityBean, String templateFileName, String commonPhrase, String utl, boolean isRedirect){

	try{
	    String brandCode = (String)PropertyUtils.getProperty(entityBean, "brandCode");
	    String orderTypeCode = (String)PropertyUtils.getProperty(entityBean, "orderTypeCode");
	    String orderNo = (String)PropertyUtils.getProperty(entityBean, "orderNo");
	    String status = (String)PropertyUtils.getProperty(entityBean, "status");
	    if(StringUtils.hasText(brandCode) && StringUtils.hasText(orderTypeCode) && StringUtils.hasText(orderNo)){
		BuOrderTypeService buOrderTypeService = (BuOrderTypeService) context.getBean("buOrderTypeService");
		BuOrderTypeId orderTypeId = new BuOrderTypeId(brandCode, orderTypeCode);
		BuOrderType orderTypePO = buOrderTypeService.findById(orderTypeId);
		if(orderTypePO != null && StringUtils.hasText(orderTypePO.getMailTo())){
		    BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService) context.getBean("buCommonPhraseService");
		    BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseService.getBuCommonPhraseLine(commonPhrase, brandCode);
		    if(buCommonPhraseLine != null && StringUtils.hasText(buCommonPhraseLine.getAttribute1())){
			String serverPath = buCommonPhraseLine.getAttribute1();
			serverPath = serverPath + utl;
			if(isRedirect){
			    serverPath += "&redirect=Y";
			}
			StringBuffer sbUrl = new StringBuffer();
			sbUrl.append("<a href=");
			sbUrl.append(serverPath);		    
			sbUrl.append(">");
			sbUrl.append(serverPath);
			sbUrl.append("</a>");
			Map root = new HashMap();
			root.put("display", sbUrl.toString());
			String mailAddress = orderTypePO.getMailTo();
			String[] mailAddressArray = StringTools.StringToken(mailAddress, ",");
			List mailTo = new ArrayList();
			for(int i = 0; i < mailAddressArray.length; i++){
			    mailTo.add(mailAddressArray[i]);
			}		            
			StringBuffer sb = new StringBuffer();
			sb.append("<");
			sb.append(OrderStatus.getChineseWord(status));
			sb.append(">");
			sb.append(MessageStatus.getJobManagerMsg(brandCode, orderTypeCode, orderNo));
			sendMail(sb.toString(), "CommonTemplate.ftl", root , mailTo);
		    }
		}		    
	    }		
	}catch(Exception ex){
	    log.error("寄送mail失敗，原因：" + ex.toString());
	    System.out.println("寄送mail失敗，原因：" + ex.toString());
	}
    }
    
    /**
     * 系統出錯寄給維護人員
     * @param key
     * @throws Exception
     */
    public static void systemErrorLogSendMail( String processName, String logLevel, String processLotNo){
	StringBuffer sb = new StringBuffer();
	String now = DateUtils.getCurrentDateStr(DateUtils.C_DATA_PATTON_YYYYMMDD);
	List<String> contents = new ArrayList();
	List mailTo = new ArrayList();
	try {
	    BuCommonPhraseLineDAO buCommonPhraseLineDAO = (BuCommonPhraseLineDAO) context.getBean("buCommonPhraseLineDAO");
	    BuCommonPhraseLine systemError2Mail = (BuCommonPhraseLine)buCommonPhraseLineDAO.findOneBuCommonPhraseLine("SystemError2Mail", processName);
	    if(null == systemError2Mail){
		throw new Exception("查無 SystemError2Mail 的 "+processName+"  配置檔");
	    }

	    String subject = systemError2Mail.getName(); // 主旨
	    String mailAddress = systemError2Mail.getAttribute1(); // 收件人
	    
	    String[] mailAddressArray = mailAddress.split(",");
	    for(int i = 0; i < mailAddressArray.length; i++){
		mailTo.add(mailAddressArray[i]);
	    }
	    
	    List<SiSystemLog> siSystemLogs = SiSystemLogUtils.getSystemLog(processName, logLevel, processLotNo);
	    for (SiSystemLog siSystemLog : siSystemLogs) {
		String creationDate = DateUtils.format(siSystemLog.getCreationDate(), DateUtils.C_TIME_PATTON_SLASH);
		String message = siSystemLog.getMessage();
		sb.append(creationDate).append(" => ").append(message).append("<br>");
	    }
	   
	    contents.add(sb.toString());
	    
	    if(siSystemLogs.size() > 0){
		sendMail(now + subject +"異常", contents, mailTo);
	    }else{
		log.info("無錯誤系統記錄");
	    }
	
	} catch (Exception e) {
	    log.info(e.getMessage());
	}
    }
    
    /**
     * 系統寄給相關人員
     * @param key
     * @throws Exception
     */
    public static void systemSendMail( Map map ){
	StringBuffer sb = new StringBuffer();
	String now = DateUtils.getCurrentDateStr(DateUtils.C_DATA_PATTON_YYYYMMDD);
	try {
	    String headCode = (String)map.get("headCode");
	    String processName = (String)map.get("processName");
	    List<String> contents = (List<String>)map.get("contents");
	    
	    BuCommonPhraseLineDAO buCommonPhraseLineDAO = (BuCommonPhraseLineDAO) context.getBean("buCommonPhraseLineDAO");
	    BuCommonPhraseLine system2Mail = (BuCommonPhraseLine)buCommonPhraseLineDAO.findOneBuCommonPhraseLine(headCode, processName);
	    if(null == system2Mail){
		throw new Exception("查無"+headCode+" 的 "+processName+"  配置檔");
	    }

	    String subject = system2Mail.getName(); // 主旨
	    String mailAddress = system2Mail.getAttribute1(); // 收件人
	    
	    String[] mailAddressArray = mailAddress.split(",");
	    List mailTo = new ArrayList();
	    for(int i = 0; i < mailAddressArray.length; i++){
		mailTo.add(mailAddressArray[i]);
	    }	
	    sb.append(now).append(subject);
	    
	    sendMail(now + subject, contents, mailTo);
	
	} catch (Exception e) {
	    log.info(e.getMessage());
	}
    }
    
}
