package tw.com.tm.erp.hbm.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.constants.MailConfig;
import tw.com.tm.erp.utils.FreeMaker;
import tw.com.tm.erp.utils.JMail;
import freemarker.template.TemplateException;

/**
 * Send Mail Server
 * 
 * @author shan
 */
public class MailService {
	private static final Log log = LogFactory.getLog(MailService.class);
	
	/**
	 * send mail
	 * 
	 * @param subject
	 * @param content
	 * @param to_email
	 */
	public void sendMail(String subject, String content, String to_email) {
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
	public void sendMail(String subject, String templateFileName, Map<String, Object> propMap, String to_email) throws IOException,
			TemplateException {
		log.info("MailService.sendMail 1 ");
		FreeMaker fm = new FreeMaker();
		String content = fm.geneHtmlFile(templateFileName, propMap);
		getJMail().doSendMail(subject, content, to_email);
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
	@SuppressWarnings("unchecked")
	public void sendMail(String subject, String templateFileName, Map<String, Object> propMap, List<String> to_email) throws IOException,
			TemplateException {
		log.info("MailService.sendMail 2 ");
		FreeMaker fm = new FreeMaker();
		String content = fm.geneHtmlFile(templateFileName, propMap);
		List<String> contents = new ArrayList();
		contents.add(content);
		getJMail().doSendMail(subject, contents, to_email, null);
	}
	
	/**
	 * Java Mail 
	 * @return
	 */
	private JMail getJMail(){
		log.info("MailService.getJMail");
		JMail jm = new JMail();
		jm.setStrSMTP_Server(MailConfig.SMTPSERVER);
		jm.setStrLoginName(MailConfig.SMTPLOGINNAME);
		jm.setStrLoginPassword(MailConfig.SMTPLOGINPASSWORD);
		jm.setStrFrom_email(MailConfig.DEFAULTMAILFROM);
		return jm ;
	}

}
