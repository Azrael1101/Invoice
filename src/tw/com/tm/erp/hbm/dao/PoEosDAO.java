package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.EosHead;


/**
 * A data access object (DAO) providing persistence and search support for
 * BuShop entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see tw.com.tm.erp.hbm.bean.BuShop
 * @author MyEclipse Persistence Tools
 */

public class PoEosDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(PoEosDAO.class);
    // property constants
    


    public EosHead findById(Long headId)
    {
    	return (EosHead)findByPrimaryKey(EosHead.class, headId);
    }


}