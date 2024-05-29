package tw.com.tm.erp.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Template Tools
 * @author shan 
 *  
 */
public class FreeMaker {
	
	private String templateFolder ; //template root
	private String templateCode = "utf-8" ;	//template file encoding
	private File templateFileFolder ;
	private static Configuration freemarkerCfg = null;
	private static final Log log = LogFactory.getLog(FreeMaker.class);	
	private static final String SKIN_FOLDER = "htmlskin" ;
	
    public FreeMaker() throws IOException
    {
    	log.info("FreeMaker init ");
    	URL classPath = FreeMaker.class.getResource("");    	
    	log.info("FreeMaker init " + classPath.getPath() + "-" + SKIN_FOLDER);    	
    	templateFileFolder = new File(classPath.getPath() + SKIN_FOLDER);
		freemarkerCfg = new Configuration();
		if (null != templateFileFolder)
			freemarkerCfg.setDirectoryForTemplateLoading(templateFileFolder);
		else
			freemarkerCfg.setClassForTemplateLoading(this.getClass(),
					templateFolder);
		if (null != templateCode)
			freemarkerCfg.setDefaultEncoding(templateCode);
    } 
    
    /**
	 * 產生套板之後的字串
	 * 
	 * @param templateFileName
	 *            樣板名稱
	 * @param propMap
	 *            樣板中使用到的物件
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
    public String geneHtmlFile(String templateFileName, Map<String,Object> propMap) throws IOException, TemplateException {
    	log.info("FreeMaker geneHtmlFile ");
		Template temp = freemarkerCfg.getTemplate(templateFileName);
		StringWriter sw = new StringWriter();
		temp.process(propMap, sw);
		sw.flush();
		return sw.toString();
	}

    /**
     * 指定套板的Folder
     * @param templateFolder
     */
	public void setTemplateFolder(String templateFolder) {
		this.templateFolder = templateFolder;
	}

	/**
	 * 指定套板的編碼方式
	 * @param templateCode
	 */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

    /**
     * 指定套板的Folder
     * @param templateFolder
     */	
	public void setTemplateFileFolder(File templateFileFolder) {
		this.templateFileFolder = templateFileFolder;
	}   
    
     

}
