package tw.com.tm.erp.hbm.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.springframework.util.StringUtils;

import bsh.StringUtil;

import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.GnFile;

/**
 * A data access object (DAO) providing persistence and search support for
 * GnFile entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.dao.GnFile
 * @author MyEclipse Persistence Tools
 */

public class GnFileDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(GnFileDAO.class);
    
    public String findByPropertyToHtmlString(String headCode, String defaultValue, String typeValue){
	List<BuCommonPhraseLine> types = findByProperty("BuCommonPhraseLine",
		new String[] { "id.buCommonPhraseHead.headCode" },
		new Object[] { "GN_TYPE" }, "indexNo");
	StringBuffer sb = new StringBuffer();
	
	for (BuCommonPhraseLine buCommonPhraseLine : types) {
	    String lineCode = buCommonPhraseLine.getId().getLineCode();
	    sb.append("<option value=").append(lineCode);
	    
	    if(StringUtils.hasText(typeValue)){
		sb.append(" selected='selected' ");
	    }else{
		if(defaultValue.equalsIgnoreCase(lineCode)){
		    sb.append(" selected='selected '");
		}
	    }
	    sb.append(">").append(buCommonPhraseLine.getName()).append("</option>");
	}
	
	return sb.toString();
    }
    
}