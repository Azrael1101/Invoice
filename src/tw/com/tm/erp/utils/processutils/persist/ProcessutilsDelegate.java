/*
 * Created by cEAP BPM Business Object Creator Code Generator
 */
package tw.com.tm.erp.utils.processutils.persist;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.type.Type;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

//import com.tbcn.ceap.workflow.db.ClientDBDelegate;

//import tw.com.tm.erp.utils.processutils.value.*;

public class ProcessutilsDelegate// extends ClientDBDelegate
{/*    
    private static final Log logger = LogFactory.getLog(ProcessutilsDelegate.class);
    private static final String APP_PROPERTIES = "processutils.properties";

	public ProcessutilsDelegate(SessionFactory sessionFactory) 
    {
		super(sessionFactory);
	}
    
    private void printQuery(Query query)
    {
        logger.debug("***** SQL String Begin*****\n" + query.getQueryString() + "\n***** SQL String End*****");
    }
    
    
    *//**
     * find assignPerson by primary key 
     *//*
    public tw.com.tm.erp.utils.processutils.value.assignPerson findassignPerson(java.lang.Long pk)
    {
        try {
            return (tw.com.tm.erp.utils.processutils.value.assignPerson) getHibernateSession().get(tw.com.tm.erp.utils.processutils.value.assignPerson.class, pk);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
    
    *//**
     * find assignPerson by primary key 
     *//*
    public tw.com.tm.erp.utils.processutils.value.assignPerson findassignPerson(java.lang.Long pk, Session session )
    {
        try {
            return (tw.com.tm.erp.utils.processutils.value.assignPerson) session.get(tw.com.tm.erp.utils.processutils.value.assignPerson.class, pk);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
    
    *//**
     * find approvalResult by primary key 
     *//*
    public tw.com.tm.erp.utils.processutils.value.approvalResult findapprovalResult(java.lang.Long pk)
    {
        try {
            return (tw.com.tm.erp.utils.processutils.value.approvalResult) getHibernateSession().get(tw.com.tm.erp.utils.processutils.value.approvalResult.class, pk);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
    
    *//**
     * find approvalResult by primary key 
     *//*
    public tw.com.tm.erp.utils.processutils.value.approvalResult findapprovalResult(java.lang.Long pk, Session session )
    {
        try {
            return (tw.com.tm.erp.utils.processutils.value.approvalResult) session.get(tw.com.tm.erp.utils.processutils.value.approvalResult.class, pk);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
    
    *//**
     * findAssignsbyProcessId 
     *//*
    public java.util.List findAssignsbyProcessId(java.lang.Long pid)
    {
        return findAssignsbyProcessId(pid, -1, -1);
    }

    
    *//**
     * findAssignsbyProcessId 
     * Overloading method for paging 
     *//*
    public java.util.List findAssignsbyProcessId(java.lang.Long pid, int first, int max)
    {
        java.util.List result = new java.util.ArrayList();

        try {
            String hsql = "select assignPerson0 from tw.com.tm.erp.utils.processutils.value.assignPerson as assignPerson0";

            boolean whr = true, and = false;
            hsql += (whr? " where ":"");
            hsql += (and? " and ":" ") + "assignPerson0.assignType = 'assign'";
            whr = false;
            and = true;

            if ( pid != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "assignPerson0.processId = :pid";
                whr = false;
                and = true;
            }

	hsql += " order by assignPerson0.id asc";
            Query query = getHibernateSession().createQuery(hsql);

            if ( pid != null ) query.setParameter("pid", pid);

            if ( first > 0 ) query = query.setFirstResult(first);
            if ( max > 0 ) query = query.setMaxResults(max);

            result = query.list();
            printQuery(query);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return result;
    }
    
    *//**
     * findAssignsbyProcessId 
     *//*
    public java.util.List findAssignsbyProcessId(java.lang.Long pid, Session session)
    {
        return findAssignsbyProcessId(pid, -1, -1, session);
    }

    
    *//**
     * findAssignsbyProcessId 
     * Overloading method for paging 
     *//*
    public java.util.List findAssignsbyProcessId(java.lang.Long pid, int first, int max, Session session)
    {
        java.util.List result = new java.util.ArrayList();

        try {
            String hsql = "select assignPerson0 from tw.com.tm.erp.utils.processutils.value.assignPerson as assignPerson0";

            boolean whr = true, and = false;
            hsql += (whr? " where ":"");
            hsql += (and? " and ":" ") + "assignPerson0.assignType = 'assign'";
            whr = false;
            and = true;

            if ( pid != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "assignPerson0.processId = :pid";
                whr = false;
                and = true;
            }

	hsql += " order by assignPerson0.id asc";
            Query query = session.createQuery(hsql);

            if ( pid != null ) query.setParameter("pid", pid);

            if ( first > 0 ) query = query.setFirstResult(first);
            if ( max > 0 ) query = query.setMaxResults(max);

            result = query.list();
            printQuery(query);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return result;
    }
    
    *//**
     * findApprovalsbyCriteria 
     *//*
    public java.util.List findApprovalsbyCriteria(java.lang.Long pid, java.lang.String formType, java.lang.String nodeType, java.lang.Long formId, java.lang.String approver, java.util.Date startDate, java.util.Date endDate)
    {
        return findApprovalsbyCriteria(pid, formType, nodeType, formId, approver, startDate, endDate, -1, -1);
    }

    
    *//**
     * findApprovalsbyCriteria 
     * Overloading method for paging 
     *//*
    public java.util.List findApprovalsbyCriteria(java.lang.Long pid, java.lang.String formType, java.lang.String nodeType, java.lang.Long formId, java.lang.String approver, java.util.Date startDate, java.util.Date endDate, int first, int max)
    {
        java.util.List result = new java.util.ArrayList();

        try {
            String hsql = "select approvalResult0 from tw.com.tm.erp.utils.processutils.value.approvalResult as approvalResult0";

            boolean whr = true, and = false;
            if ( pid != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.processId = :pid";
                whr = false;
                and = true;
            }

            if ( formType != null && !"".equals(formType) ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.formName = :formType";
                whr = false;
                and = true;
            }

            if ( nodeType != null && !"".equals(nodeType) ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.nodeName = :nodeType";
                whr = false;
                and = true;
            }

            if ( formId != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.activityId = :formId";
                whr = false;
                and = true;
            }

            if ( approver != null && !"".equals(approver) ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.approver = :approver";
                whr = false;
                and = true;
            }

            if ( startDate != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.dateTime >= :startDate";
                whr = false;
                and = true;
            }

            if ( endDate != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.dateTime < :endDate";
                whr = false;
                and = true;
            }

	hsql += "";
            Query query = getHibernateSession().createQuery(hsql);

            if ( pid != null ) query.setParameter("pid", pid);
            if ( formType != null && !"".equals(formType) ) query.setParameter("formType", formType);
            if ( nodeType != null && !"".equals(nodeType) ) query.setParameter("nodeType", nodeType);
            if ( formId != null ) query.setParameter("formId", formId);
            if ( approver != null && !"".equals(approver) ) query.setParameter("approver", approver);
            if ( startDate != null ) query.setParameter("startDate", startDate);
            if ( endDate != null ) query.setParameter("endDate", endDate);

            if ( first > 0 ) query = query.setFirstResult(first);
            if ( max > 0 ) query = query.setMaxResults(max);

            result = query.list();
            printQuery(query);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return result;
    }
    
    *//**
     * findApprovalsbyCriteria 
     *//*
    public java.util.List findApprovalsbyCriteria(java.lang.Long pid, java.lang.String formType, java.lang.String nodeType, java.lang.Long formId, java.lang.String approver, java.util.Date startDate, java.util.Date endDate, Session session)
    {
        return findApprovalsbyCriteria(pid, formType, nodeType, formId, approver, startDate, endDate, -1, -1, session);
    }

    
    *//**
     * findApprovalsbyCriteria 
     * Overloading method for paging 
     *//*
    public java.util.List findApprovalsbyCriteria(java.lang.Long pid, java.lang.String formType, java.lang.String nodeType, java.lang.Long formId, java.lang.String approver, java.util.Date startDate, java.util.Date endDate, int first, int max, Session session)
    {
        java.util.List result = new java.util.ArrayList();

        try {
            String hsql = "select approvalResult0 from tw.com.tm.erp.utils.processutils.value.approvalResult as approvalResult0";

            boolean whr = true, and = false;
            if ( pid != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.processId = :pid";
                whr = false;
                and = true;
            }

            if ( formType != null && !"".equals(formType) ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.formName = :formType";
                whr = false;
                and = true;
            }

            if ( nodeType != null && !"".equals(nodeType) ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.nodeName = :nodeType";
                whr = false;
                and = true;
            }

            if ( formId != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.activityId = :formId";
                whr = false;
                and = true;
            }

            if ( approver != null && !"".equals(approver) ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.approver = :approver";
                whr = false;
                and = true;
            }

            if ( startDate != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.dateTime >= :startDate";
                whr = false;
                and = true;
            }

            if ( endDate != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.dateTime < :endDate";
                whr = false;
                and = true;
            }

	hsql += "";
            Query query = session.createQuery(hsql);

            if ( pid != null ) query.setParameter("pid", pid);
            if ( formType != null && !"".equals(formType) ) query.setParameter("formType", formType);
            if ( nodeType != null && !"".equals(nodeType) ) query.setParameter("nodeType", nodeType);
            if ( formId != null ) query.setParameter("formId", formId);
            if ( approver != null && !"".equals(approver) ) query.setParameter("approver", approver);
            if ( startDate != null ) query.setParameter("startDate", startDate);
            if ( endDate != null ) query.setParameter("endDate", endDate);

            if ( first > 0 ) query = query.setFirstResult(first);
            if ( max > 0 ) query = query.setMaxResults(max);

            result = query.list();
            printQuery(query);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return result;
    }
    
    *//**
     * findApprovalsbyProcessId 
     *//*
    public java.util.List findApprovalsbyProcessId(java.lang.Long pid)
    {
        return findApprovalsbyProcessId(pid, -1, -1);
    }

    
    *//**
     * findApprovalsbyProcessId 
     * Overloading method for paging 
     *//*
    public java.util.List findApprovalsbyProcessId(java.lang.Long pid, int first, int max)
    {
        java.util.List result = new java.util.ArrayList();

        try {
            String hsql = "select approvalResult0 from tw.com.tm.erp.utils.processutils.value.approvalResult as approvalResult0";

            boolean whr = true, and = false;
            if ( pid != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.processId = :pid";
                whr = false;
                and = true;
            }

	hsql += "";
            Query query = getHibernateSession().createQuery(hsql);

            if ( pid != null ) query.setParameter("pid", pid);

            if ( first > 0 ) query = query.setFirstResult(first);
            if ( max > 0 ) query = query.setMaxResults(max);

            result = query.list();
            printQuery(query);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return result;
    }
    
    *//**
     * findApprovalsbyProcessId 
     *//*
    public java.util.List findApprovalsbyProcessId(java.lang.Long pid, Session session)
    {
        return findApprovalsbyProcessId(pid, -1, -1, session);
    }

    
    *//**
     * findApprovalsbyProcessId 
     * Overloading method for paging 
     *//*
    public java.util.List findApprovalsbyProcessId(java.lang.Long pid, int first, int max, Session session)
    {
        java.util.List result = new java.util.ArrayList();

        try {
            String hsql = "select approvalResult0 from tw.com.tm.erp.utils.processutils.value.approvalResult as approvalResult0";

            boolean whr = true, and = false;
            if ( pid != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "approvalResult0.processId = :pid";
                whr = false;
                and = true;
            }

	hsql += "";
            Query query = session.createQuery(hsql);

            if ( pid != null ) query.setParameter("pid", pid);

            if ( first > 0 ) query = query.setFirstResult(first);
            if ( max > 0 ) query = query.setMaxResults(max);

            result = query.list();
            printQuery(query);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return result;
    }
    
    *//**
     * findCounterAssignsbyProcessId 
     *//*
    public java.util.List findCounterAssignsbyProcessId(java.lang.Long pid)
    {
        return findCounterAssignsbyProcessId(pid, -1, -1);
    }

    
    *//**
     * findCounterAssignsbyProcessId 
     * Overloading method for paging 
     *//*
    public java.util.List findCounterAssignsbyProcessId(java.lang.Long pid, int first, int max)
    {
        java.util.List result = new java.util.ArrayList();

        try {
            String hsql = "select assignPerson0 from tw.com.tm.erp.utils.processutils.value.assignPerson as assignPerson0";

            boolean whr = true, and = false;
            if ( pid != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "assignPerson0.processId = :pid";
                whr = false;
                and = true;
            }

            hsql += (whr? " where ":"");
            hsql += (and? " and ":" ") + "assignPerson0.assignType = 'cons'";
            whr = false;
            and = true;

	hsql += " order by assignPerson0.id asc";
            Query query = getHibernateSession().createQuery(hsql);

            if ( pid != null ) query.setParameter("pid", pid);

            if ( first > 0 ) query = query.setFirstResult(first);
            if ( max > 0 ) query = query.setMaxResults(max);

            result = query.list();
            printQuery(query);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return result;
    }
    
    *//**
     * findCounterAssignsbyProcessId 
     *//*
    public java.util.List findCounterAssignsbyProcessId(java.lang.Long pid, Session session)
    {
        return findCounterAssignsbyProcessId(pid, -1, -1, session);
    }

    
    *//**
     * findCounterAssignsbyProcessId 
     * Overloading method for paging 
     *//*
    public java.util.List findCounterAssignsbyProcessId(java.lang.Long pid, int first, int max, Session session)
    {
        java.util.List result = new java.util.ArrayList();

        try {
            String hsql = "select assignPerson0 from tw.com.tm.erp.utils.processutils.value.assignPerson as assignPerson0";

            boolean whr = true, and = false;
            if ( pid != null ) {
                hsql += (whr? " where ":"");
                hsql += (and? " and ":" ") + "assignPerson0.processId = :pid";
                whr = false;
                and = true;
            }

            hsql += (whr? " where ":"");
            hsql += (and? " and ":" ") + "assignPerson0.assignType = 'cons'";
            whr = false;
            and = true;

	hsql += " order by assignPerson0.id asc";
            Query query = session.createQuery(hsql);

            if ( pid != null ) query.setParameter("pid", pid);

            if ( first > 0 ) query = query.setFirstResult(first);
            if ( max > 0 ) query = query.setMaxResults(max);

            result = query.list();
            printQuery(query);
        }
        catch (HibernateException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return result;
    }


*/}
