package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.CmDeclarationHead;

/**
 * CmDeclarationHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CmDeclarationLogDAO extends BaseDAO {

	private static final Log log = LogFactory.getLog(CmDeclarationLogDAO.class);

}