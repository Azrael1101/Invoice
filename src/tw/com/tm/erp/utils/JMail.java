package tw.com.tm.erp.utils;

import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * <p>
 * Title : Java Mail 工具
 * </p>
 * 
 * @author shan
 * 
 */

public class JMail {

	public static int ONETHREADSENDMAILS = 10; // 目前提供一個thread送10個mail

	private String strLoginName;

	private String strLoginPassword;

	private String strSMTP_Server;

	private String strContentType;

	private String strFrom_email;

	private String strSubjectEncode;

	private String strEncode;

	private Properties headerPros;

	private Map cid;

	
	
	/**
	 * send mail
	 * @param subject
	 * @param content
	 * @param to_email
	 */
	public void doSendMail(String subject, String content, String to_email) {
		doSendMail(subject, content, to_email, null, false);
	}
	
	/**
	 * send mail
	 * @param subject 
	 * @param content
	 * @param to_email
	 * @param attchefiles
	 *            String File Path or File
	 */
	public void doSendMail(String subject, String content, String to_email,
			List<String> attchefiles) {
		doSendMail(subject, content, to_email, attchefiles, false);
	}

	/**
	 * send mail 
	 * @param subject
	 * @param content
	 * @param to_email
	 * @param attchefiles
	 * @param doLog
	 */
	@SuppressWarnings("unchecked")
	public void doSendMail(String subject, String content, String to_email,
			List<String> attchefiles, boolean doLog) {
		Vector<String> con = new Vector();
		con.add(content);
		Vector<String> tomail = new Vector();
		tomail.add(to_email);
		doSendMail(subject, con, tomail, attchefiles, null , null , doLog);
	}

	/**
	 * send mail
	 * @param subject
	 * @param contents
	 * @param to_email
	 * @param AttachFiles
	 */
	public void doSendMail(String subject, List<String> contents,
			List<String> to_email, List<String> AttachFiles) {
		doSendMail(subject, contents, to_email, AttachFiles , null , null , false);
	}
	
	/**
	 *  send mail 額外設定 headerPros
	 * @param subject
	 * @param contents
	 * @param to_email
	 * @param AttachFiles
	 * @param headerPros
	 */
	public void doSendMail(String subject, List<String> contents,
			List<String> to_email, List<String> AttachFiles, Map cidMap) {
	    	this.setCid(cidMap);
		doSendMail(subject, contents, to_email, AttachFiles , null , null , false);
	}
	
	/**
	 * send mail
	 * @param subject
	 * @param contents
	 * @param to_email
	 * @param AttachFiles
	 * @param ccMails
	 */
	public void doSendMail(String subject, List<String> contents,
			List<String> to_email, List<String> AttachFiles , String ccMails ) {
		doSendMail(subject, contents, to_email, AttachFiles , null , null , false);
	}	

	/**
	 * send mail
	 * @param subject
	 * @param contents
	 * @param to_email
	 * @param AttachFiles
	 * @param ccMails
	 * @param bccMail
	 */
	public void doSendMail(String subject, List<String> contents,
			List<String> to_email, List<String> AttachFiles , String ccMails , String bccMail ) {
		doSendMail(subject, contents, to_email, AttachFiles , ccMails , bccMail , false);
	}
	
	/**
	 * send mail
	 * @param subject
	 * @param contents
	 * @param to_email
	 * @param AttachFiles
	 * @param ccMails
	 * @param bccMails
	 * @param doLog
	 */
	@SuppressWarnings("unchecked")
	public void doSendMail(String subject, List<String> contents,
			List<String> to_email, List<String> AttachFiles, String ccMails , String bccMails , boolean doLog) {
		if ((contents != null) && (to_email != null) && (to_email.size() > 0)
				&& (contents.size() > 0)) {

			Vector sms = new Vector();
			SendMail sm = null;
			for (int i = 0; i < to_email.size(); i++) {
				if ((i % ONETHREADSENDMAILS) == 0) {
					if (sm != null)
						sms.add(sm);
					sm = new SendMail();
					sm.strLogin_name = strLoginName;
					sm.strLogin_password = strLoginPassword;
					sm.strSMTP_server = strSMTP_Server;
					if (strSubjectEncode != null)
						sm.strSubjectEncode = strSubjectEncode;
					if (strEncode != null)
						sm.strEncode = strEncode;
					if (strContentType != null)
						sm.strContentType = strContentType;
					sm.strSubject = subject;
					sm.strFrom_email = strFrom_email;
					// 20051017 shan NEW 一份新的 夾檔給 THREAD 去寄送
					if (AttachFiles != null)
						sm.ltAttachFiles = new ArrayList(AttachFiles);
					if (ccMails != null)
						sm.ccToMail = ccMails;
					if (bccMails != null)
						sm.bccToMail = bccMails;
					if (headerPros != null)
						sm.headerPros = headerPros;
					
					if(cid != null)
					    	sm.cid = cid;
					
					sm.initial();
				}
				if (contents.size() == 1) {
					sm.add(to_email.get(i), contents.get(0));
				} else {
					sm.add(to_email.get(i), contents.get(i));
				}
			}
			sms.add(sm);

			for (int i = 0; i < sms.size(); i++) {
				SendMail sendsm = (SendMail) sms.get(i);
				sendsm.run();
			}
		}
		// 20051017 shan 清除夾檔的資料
		AttachFiles = null;
	}

	public void setStrSMTP_Server(String strSMTP_Server) {
		this.strSMTP_Server = strSMTP_Server;
	}

	public void setStrLoginPassword(String strLoginPassword) {
		this.strLoginPassword = strLoginPassword;
	}

	public void setStrLoginName(String strLoginName) {
		this.strLoginName = strLoginName;
	}

	public void setStrFrom_email(String strFrom_email) {
		this.strFrom_email = strFrom_email;
	}

	public void setStrEncode(String strEncode) {
		this.strEncode = strEncode;
	}

	public void setStrContentType(String strContentType) {
		this.strContentType = strContentType;
	}

	public void setHeaderPros(Properties headerPros) {
		this.headerPros = headerPros;
	}

	public String getStrContentType() {
		return strContentType;
	}

	public String getStrEncode() {
		return strEncode;
	}

	public String getStrFrom_email() {
		return strFrom_email;
	}

	public String getStrLoginName() {
		return strLoginName;
	}

	public String getStrLoginPassword() {
		return strLoginPassword;
	}

	public String getStrSMTP_Server() {
		return strSMTP_Server;
	}

	public Properties getHeaderPros() {
		return headerPros;
	}

	public String getStrSubjectEncode() {
		return strSubjectEncode;
	}

	public void setStrSubjectEncode(String strSubjectEncode) {
		this.strSubjectEncode = strSubjectEncode;
	}

	public Map getCid() {
	    return cid;
	}

	public void setCid(Map cid) {
	    this.cid = cid;
	}
}

/**
 * 
 * @author shan one content to multi mail : set one ltContents and set multi
 *         ltTo_emails
 */
class SendMail extends Thread {
	String strLogin_name;

	String strLogin_password;

	String strSMTP_server;

	String strSubject;

	String strContentType = "multi"; // multipart or text

	String strFrom_email;

	String strEncode = "big5";

	String strSubjectEncode = "big5";

	List<String> ltAttachFiles; // MAIL attach file

	List<String> ltContents; // MAIL content

	List<String> ltTo_emails; // mail to

	String ccToMail; // cc to Mail split by ,

	String bccToMail; // bcc to mail split by ,

	InternetAddress iaFrom;

	Session sessin;

	Properties headerPros; // header Properties

	Map cid;	// for 附件內容夾圖檔
	
	/**
	 * 分批加入 mail to and content
	 * 
	 * @param to_mail
	 * @param content
	 */
	@SuppressWarnings("unchecked")
	public void add(String mail_to, String content) {
		if (ltContents == null) {
			ltContents = new ArrayList();
			ltTo_emails = new ArrayList();
		}
		ltContents.add(content);
		ltTo_emails.add(mail_to);
	}

	/**
	 * 指定夾檔 , 每次寄送都只有一份夾檔 , 夾檔可以是多個檔案
	 * 
	 * @param attachfiles
	 */
	public void addAttachFiles(List<String> attachfiles) {
		ltAttachFiles = attachfiles;
	}

	/**
	 * 將設定值初始化
	 */
	public void initial() {
		Properties props = new Properties();
		props.put("mail.smtp.host", strSMTP_server); // default server is
														// localhost
		PopupAuthenticator auth = null;
		if ((strLogin_name != null) && (strLogin_password != null)
				&& (strLogin_name.length() > 0)
				&& (strLogin_password.length() > 0)) {
			auth = new PopupAuthenticator();
			auth.password = strLogin_password;
			auth.username = strLogin_name;
			props.put("mail.smtp.auth", "true");
			sessin = Session.getDefaultInstance(props, auth);
		} else {
			Session.getDefaultInstance(props);
		}
		try {
			iaFrom = new InternetAddress(strFrom_email);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 寄送Mail
	 */
	@SuppressWarnings("unchecked")
	public void run() {
		String content = null;
		for (int i = 0; i < ltTo_emails.size(); i++) {
			if (ltContents.size() > i)
				content = ltContents.get(i);
			String to_email = ltTo_emails.get(i);
			if ((content != null && to_email != null)
					&& (content.length() > 0 && to_email.length() > 0)) {
				try {

					// check mail address is ok ?
					(new javax.mail.internet.InternetAddress(to_email))
							.validate();

					MimeMessage message = new MimeMessage(sessin);
					message.setFrom(iaFrom);
					InternetAddress to = new InternetAddress(to_email);
					message.addRecipient(Message.RecipientType.TO, to);

					// 20060729 shan
					if ((ccToMail != null) && (ccToMail.length() > 0)) {
						InternetAddress[] cc = InternetAddress.parse(ccToMail,
								false);
						message.setRecipients(Message.RecipientType.CC, cc);
					}

					if ((bccToMail != null) && (bccToMail.length() > 0)) {
						InternetAddress[] bcc = InternetAddress.parse(
								bccToMail, false);
						message.setRecipients(Message.RecipientType.BCC, bcc);
					}

					// 20061003 shan
					if ((headerPros != null) && (!headerPros.isEmpty())) {
						Enumeration keys = headerPros.keys();
						while (keys.hasMoreElements()) {
							String proName = (String) keys.nextElement();
							String proValue = (String) headerPros
									.getProperty(proName);
							message.setHeader(proName, proValue);
						}
					}

					message.setSubject(strSubject, strSubjectEncode);
					// message.setSubject(new
					// String(strSubject.getBytes(strEncode)));
					if (strContentType.equals("multi")) { // 使用multi part
						MimeMultipart mmp = new MimeMultipart();
						MimeBodyPart mbp = new MimeBodyPart();
						mbp.setContent(new String(content.getBytes(strEncode)),
								"text/html;charset=" + strEncode);
						mmp.addBodyPart(mbp);
						// 20051017 shan 指定夾檔
						if (ltAttachFiles != null) {
							for (int j = 0; j < ltAttachFiles.size(); j++) {
								// String AttachFile = (String)
								// ltAttachFiles.get(j);
								mbp = new MimeBodyPart();
								// 得到數據源
								FileDataSource fds = new FileDataSource(
										ltAttachFiles.get(j));
								// 得到附件本身並至入BodyPart
								mbp.setDataHandler(new DataHandler(fds));
								// 得到文件名同樣至入BodyPart
								mbp.setFileName(fds.getName());
								
								// 表示附加的檔案為信件內容的夾檔
								if(cid.containsKey(ltAttachFiles.get(j))){
								    mbp.setHeader("Content-ID", (String)cid.get(ltAttachFiles.get(j)));
								}
								
								
								mmp.addBodyPart(mbp);
							}
						} else {
							mbp.setContent(new String(content
									.getBytes(strEncode)), "text/html;charset="
									+ strEncode); // \u7ED9BodyPart\u5BF9象\u8BBE置\u5185容和格式/\u7F16\u7801方式
							mmp.addBodyPart(mbp);
						}
						message.setContent(mmp);
					} else {
						// 使用new String(content.getBytes("big5"))是為了防止中文變成亂碼。
						message
								.setText(new String(content.getBytes(strEncode)));
					}
					Transport.send(message);
				} catch (Exception msexc) {
					msexc.printStackTrace();
					// LogTools.writeExcept(this,LogTools.WARN) ;
				}
			}
			/*
			 * }catch(MessagingException msexc){ //if mail create bug dont send
			 * mail LogTools.writeExcept(this,msexc,LogTools.DONTSENDMAIL);
			 * }catch(Exception e){ LogTools.writeExcept(this,e,LogTools.ERROR); }
			 */

		}
	}

	public void Destory() { // 20051017 shan 希望可以DESTORY DATA
		ltAttachFiles = null; // MAIL 夾檔
		ltContents = null; // MAIL 內容
		ltTo_emails = null; // 寄送的對象
	}

	@SuppressWarnings("unchecked")
	public void setLtContents(List<String> ltContents) {
		this.ltContents = ltContents;
	}

	@SuppressWarnings("unchecked")
	public void setLtTo_emails(List ltTo_emails) {
		this.ltTo_emails = ltTo_emails;
	}
}

class PopupAuthenticator extends Authenticator {
	String username;

	String password;

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}

/*
 * 使用方式 Mail sendmail=newMail("zhang@263.net",
 * "chtwoy@21cn.com","smtp.21cn.com","test"); sendmail.attachfile("table.pdf");
 * sendmail.startSend(); }catch(Exception e){ e.printStackTrace(); }
 */
/*
 * class Mail { //定義發件人、收件人、主題等 String stTo = ""; String stFrom = ""; String
 * stHost = ""; String stFilename = ""; String stSubject = ""; //用於保存發送附件的文件名的集合
 * Vector file = new Vector(); //做一個可以傳發件人等參數的構造 public Mail(String to, String
 * from, String smtpServer, String subject) { //初始化發件人、收件人、主題等 stTo = to; stFrom =
 * from; stHost = smtpServer; stSubject = subject; }
 * 
 * //該方法用於收集附件名 public void attachfile(String fname) { file.addElement(fname); }
 * 
 * //開始發送信件的方法 public boolean startSend() { //創建Properties對象 Properties props =
 * System.getProperties(); //創建信件伺服器 props.put("mail.smtp.host", stHost);
 * //得到默認的對話對象 Session session = Session.getDefaultInstance(props, null); try {
 * //創建一個消息，並初始化該消息的各項元素 MimeMessage msg = new MimeMessage(session);
 * msg.setFrom(new InternetAddress(stFrom)); InternetAddress[] address = { new
 * InternetAddress(stTo)}; msg.setRecipients(Message.RecipientType.TO, address);
 * msg.setSubject(stSubject); //後面的BodyPart將加入到此處創建的Multipart中 Multipart mp =
 * new MimeMultipart(); //利用枚舉器方便的遍歷集合 Enumeration efile = file.elements();
 * //檢查序列中是否還有更多的對象 while (efile.hasMoreElements()) { MimeBodyPart mbp = new
 * MimeBodyPart(); //選擇出每一個附件名 stFilename = efile.nextElement().toString();
 * //得到數據源 FileDataSource fds = new FileDataSource(stFilename);
 * //得到附件本身並至入BodyPart mbp.setDataHandler(new DataHandler(fds));
 * //得到文件名同樣至入BodyPart mbp.setFileName(fds.getName()); mp.addBodyPart(mbp); }
 * //移走集合中的所有元素 file.removeAllElements(); //Multipart加入到信件 msg.setContent(mp);
 * //設置信件頭的發送日期 msg.setSentDate(new Date()); //發送信件 Transport.send(msg); } catch
 * (MessagingException mex) { mex.printStackTrace(); Exception ex = null; if (
 * (ex = mex.getNextException()) != null) { ex.printStackTrace(); } return
 * false; } return true; } }
 */
