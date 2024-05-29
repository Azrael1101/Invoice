/*
 * Created by cEAP BPM Business Object Creator Code Generator
 */
package tw.com.tm.erp.utils.processutils.bizOper;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.NamingException;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;

//import com.tbcn.ceap.workflow.db.DBDelegateFactory;


//import tw.com.tm.erp.utils.processutils.value.*;
import tw.com.tm.erp.utils.processutils.persist.*;


public class ProcessutilsBizOper 
{/*
    private static final Log logger = LogFactory.getLog((ProcessutilsBizOper.class));
    
    // start of business objects creation, update, and deletion methods
    
    *//**
     * createAssignPerson business operation description 
     *//*
    public static tw.com.tm.erp.utils.processutils.value.assignPerson createassignPerson(tw.com.tm.erp.utils.processutils.value.assignPerson obj)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();
            delegator.save(obj);
            delegator.flush();
            return obj;
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * updateAssignPerson business operation description 
     *//*
    public static void updateassignPerson(tw.com.tm.erp.utils.processutils.value.assignPerson obj)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();
            delegator.saveOrUpdate(obj);
            delegator.flush();
            
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * deleteAssignPerson business operation description 
     *//*
    public static void deleteassignPerson(tw.com.tm.erp.utils.processutils.value.assignPerson obj)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();
            delegator.remove(obj);
            delegator.flush();
            
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * createApprovalResult business operation description 
     *//*
    public static tw.com.tm.erp.utils.processutils.value.approvalResult createapprovalResult(tw.com.tm.erp.utils.processutils.value.approvalResult obj)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();
            delegator.save(obj);
            delegator.flush();
            return obj;
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * updateApprovalResult business operation description 
     *//*
    public static void updateapprovalResult(tw.com.tm.erp.utils.processutils.value.approvalResult obj)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();
            delegator.saveOrUpdate(obj);
            delegator.flush();
            
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * deleteApprovalResult business operation description 
     *//*
    public static void deleteapprovalResult(tw.com.tm.erp.utils.processutils.value.approvalResult obj)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();
            delegator.remove(obj);
            delegator.flush();
            
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }

    // end of business objects creation, update, and deletion methods
    
    // start of business objects query methods
    
    *//**
     * findAssignPerson business operation description 
     *//*
    public static tw.com.tm.erp.utils.processutils.value.assignPerson findassignPerson(java.lang.Long pk)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findassignPerson(pk);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * findApprovalResult business operation description 
     *//*
    public static tw.com.tm.erp.utils.processutils.value.approvalResult findapprovalResult(java.lang.Long pk)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findapprovalResult(pk);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * findAssignsbyProcessId 
     *//*
    public static java.util.List findAssignsbyProcessId(java.lang.Long pid)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findAssignsbyProcessId(pid);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    
    *//**
     * findAssignsbyProcessId 
     * Overloading method for paging 
     *//*
    public static java.util.List findAssignsbyProcessId(java.lang.Long pid, int first, int max)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findAssignsbyProcessId(pid, first, max);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * findApprovalsbyCriteria 
     *//*
    public static java.util.List findApprovalsbyCriteria(java.lang.Long pid, java.lang.String formType, java.lang.String nodeType, java.lang.Long formId, java.lang.String approver, java.util.Date startDate, java.util.Date endDate)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findApprovalsbyCriteria(pid, formType, nodeType, formId, approver, startDate, endDate);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    
    *//**
     * findApprovalsbyCriteria 
     * Overloading method for paging 
     *//*
    public static java.util.List findApprovalsbyCriteria(java.lang.Long pid, java.lang.String formType, java.lang.String nodeType, java.lang.Long formId, java.lang.String approver, java.util.Date startDate, java.util.Date endDate, int first, int max)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findApprovalsbyCriteria(pid, formType, nodeType, formId, approver, startDate, endDate, first, max);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * findApprovalsbyProcessId 
     *//*
    public static java.util.List findApprovalsbyProcessId(java.lang.Long pid)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findApprovalsbyProcessId(pid);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    
    *//**
     * findApprovalsbyProcessId 
     * Overloading method for paging 
     *//*
    public static java.util.List findApprovalsbyProcessId(java.lang.Long pid, int first, int max)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findApprovalsbyProcessId(pid, first, max);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    *//**
     * findCounterAssignsbyProcessId 
     *//*
    public static java.util.List findCounterAssignsbyProcessId(java.lang.Long pid)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findCounterAssignsbyProcessId(pid);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }
    
    
    *//**
     * findCounterAssignsbyProcessId 
     * Overloading method for paging 
     *//*
    public static java.util.List findCounterAssignsbyProcessId(java.lang.Long pid, int first, int max)
        throws Exception
    {
        ProcessutilsDelegate delegator = null;

        try {
            delegator = (ProcessutilsDelegate) DBDelegateFactory.getDBDelegate(
                ProcessutilsDelegate.class, ProcessutilsMappingFinder.getInstance(), ProcessutilsPropertyFinder.getInstance());

            delegator.connect();

            return delegator.findCounterAssignsbyProcessId(pid, first, max);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new Exception(e);
        }
        finally {
            if (delegator != null) {
                delegator.disconnect();
            }
        }
    }

    // end of business objects query methods

// start of business objects query methods

    // end of business objects query methods

*/}
