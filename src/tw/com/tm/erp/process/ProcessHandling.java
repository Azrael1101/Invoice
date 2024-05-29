package tw.com.tm.erp.process;

import java.util.HashMap;
import java.util.Set;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/*import com.tbcn.ceap.workflow.client.activity.ActivityHeader;
import com.tbcn.ceap.workflow.client.assignment.AssignmentCriteria;
import com.tbcn.ceap.workflow.client.assignment.AssignmentDetail;
import com.tbcn.ceap.workflow.client.assignment.AssignmentHeader;
import com.tbcn.ceap.workflow.client.ejb.ServiceLocator;
import com.tbcn.ceap.workflow.client.ejb.WorkflowAdminSB;
import com.tbcn.ceap.workflow.client.ejb.WorkflowClientSB;
import com.tbcn.ceap.workflow.client.process.ProcessDetail;
import com.tbcn.ceap.workflow.data.SourceReference;*/



public class ProcessHandling {

	public static void checkedCompleteAssignment(long long1, Long processId,
			Long assignmentId) {
		// TODO Auto-generated method stub
		
	}/*
	private static final Log log = LogFactory.getLog(ProcessHandling.class);
	private static final String WF_PRC_RUNNING = "WF_PRC_RUNNING";
	private static final String WF_PRC_COMPLETED = "WF_PRC_COMPLETED";
	private static final String WF_PRC_TERMINATED = "WF_PRC_TERMINATED";
	private static final String WF_PRC_ABORTED = "WF_PRC_ABORTED";
	private static final String WF_PRC_NOT_STARTED = "WF_PRC_NOT_STARTED";
	private static final String ACCEPTED = "ACCEPTED";


	*//**
	 * 啟動cEAP中的流程
	 * @param packageId
	 * @param processId : 
	 * @param version : 版本
	 * @param sourceReferenceType :Name of process Property
	 * @param context :傳進process的參數
	 * @return :result[0]流程代號,result[1]活動代號,result[1]活動名稱
	 *//*
	public static Object[] start(String packageId, String processId, 
			String version, String sourceReferenceType, HashMap context)
    {
        try
        {

        	System.out.println("=======Start Process ==============");
        	System.out.println("packageId           :" + packageId);
        	System.out.println("processId           :" + processId);
        	System.out.println("version             :" + version);
        	System.out.println("sourceReferenceType :" + sourceReferenceType);
        	System.out.println("context             :" +context.keySet().toString());
        	System.out.println("===================================");
        	Object[] result = new Object[3];
            WorkflowClientSB clientSB = com.tbcn.ceap.workflow.client.ejb.ServiceLocator.getWorkflowClientSBHome().create();
            String domain = clientSB.getDefaultDomainId();
            SourceReference reference = 
            	new SourceReference((Long)context.get("formId"), sourceReferenceType);
            
            long procId = 0;
            long activityId = 0;
            String activityName ="流程起始";
            if (StringUtils.hasText(version))           
                procId = clientSB.startProcess(packageId, processId, version, context, reference, domain);         
            else           
                procId = clientSB.startProcess(packageId, processId, context, reference, domain);
            
            
            ProcessDetail processDetail = clientSB.getProcessDetail(procId,domain);
            Set<ActivityHeader> activityHeaders  = processDetail.getActivities();
            System.out.println("activityHeaders.size:"+activityHeaders.size());
            
            for(ActivityHeader activityHeader: activityHeaders){
            	System.out.println("getActivityType:"+activityHeader.getActivityType());
            	if("START".equals(activityHeader.getActivityType()) ){
            		activityId = activityHeader.getActivityId();
            		activityName = activityHeader.getName();
            	}
            }
            
            result[0]=procId;
            result[1]=activityId;
            result[2]=activityName;
            return result;
        }
        catch (Exception e)        {
        	log.error("流程啟動時發生錯誤："+e.getMessage());
            throw new EJBException(e);
        }
      
    }
	
	public static Object[] completeAssignment( long assignmentId, HashMap context)
    {
        try{
        	Object[] result = new Object[3];
            WorkflowClientSB clientSB = com.tbcn.ceap.workflow.client.ejb.ServiceLocator.getWorkflowClientSBHome().create();
         
            String domain = clientSB.getDefaultDomainId();    
            if (assignmentId != 0 )
            	clientSB.completeAssignment(assignmentId, context, domain);
            AssignmentDetail  assignmentDetail = clientSB.getAssignmentDetail(assignmentId, domain);
            
            result[0]= assignmentDetail.getProcessId();
            result[1]= assignmentDetail.getActivityHeader().getActivityId();
            result[2]=assignmentDetail.getActivityName();
    	
            return result;
        }catch (Exception e)        {
        	log.error("流程...時發生錯誤："+e.getMessage());
            throw new EJBException(e);
        }
    }
	
	public static Object[] reAssignment( long assignmentId,String loginName, HashMap context)
    {
        String Reassign = "Reassign";
		try{
        	log.info("assignmentId~~~~~"+assignmentId);
        	Object[] result = new Object[3];
            WorkflowClientSB clientSB = com.tbcn.ceap.workflow.client.ejb.ServiceLocator.getWorkflowClientSBHome().create();
            String domain = clientSB.getDefaultDomainId();  
            if (assignmentId != 0 )
            //clientSB.completeAssignment(assignmentId, context, domain);
            clientSB.reassignAssignment(assignmentId, loginName, Reassign, null);
            AssignmentDetail  assignmentDetail = clientSB.getAssignmentDetail(assignmentId, domain);
            result[0]= assignmentDetail.getProcessId();
            result[1]= assignmentDetail.getActivityHeader().getActivityId();
            result[2]=assignmentDetail.getActivityName();
            return result;
        }catch (Exception e)        {
        	log.error("流程...時發生錯誤："+e.getMessage());
            throw new EJBException(e);
        }
    }
	
	
	public static Object[] completeAssignment( HashMap criteria ,HashMap context)
    {
        try
        {
        	//TBCNSecurityManager securtityManager = SecurityManagerFactory.getSecurityManager();
        	//securtityManager.login("jeff", "1");
        	//securtityManager.run(new java.security.PrivilegedAction<T>);
        	Object[] result = new Object[3];
            WorkflowClientSB clientSB = com.tbcn.ceap.workflow.client.ejb.ServiceLocator.getWorkflowClientSBHome().create();
            WorkflowAdminSB adminSB =ServiceLocator.getWorkflowAdminSBHome().create();
            String domain = clientSB.getDefaultDomainId();       
            String status = ACCEPTED;         
            long assignmentId = 0;
            long activityId = 0;
            String activityName = "";
         
            System.out.println("Domain:"+domain);
           // PartyManager partyManager = PartyManagerFactory.createPartyMgr("");
          //  PartyEntry partyEntry = partyManager.getUserByLoginUsername(loginUsername);
            
  
            AssignmentCriteria assignmentCriteria = new AssignmentCriteria();
           
            if(StringUtils.hasText((String )criteria.get("packageId"))){
            	assignmentCriteria.setPackageId((String )criteria.get("packageId"));
            }
            
            if(StringUtils.hasText((String )criteria.get("processDefId"))){
            	assignmentCriteria.setProcessDefId((String )criteria.get("processDefId"));
            }        
          
            if(StringUtils.hasText((String )criteria.get("version"))){
            	assignmentCriteria.setProcessDefVersion((String )criteria.get("version"));
            }   
         
            if(StringUtils.hasText((String )criteria.get("activityDefId"))){
            	assignmentCriteria.setActivityDefId((String )criteria.get("activityDefId"));
            }
           // assignmentCriteria.setParty(partyEntry);   
        
            assignmentCriteria.setStatus(status);
            System.out.println("packageId:"+assignmentCriteria.getPackageId());
            System.out.println("processDefId:"+assignmentCriteria.getProcessDefId());
            System.out.println("version:"+assignmentCriteria.getProcessDefVersion());
            System.out.println("activityDefId:"+assignmentCriteria.getActivityDefId());
            System.out.println("processId:"+(Long)criteria.get("processId"));
            System.out.println("loginUsername:"+(String )criteria.get("loginUsername"));
            AssignmentHeader[] assignmentHeaders =  
            	clientSB.findAssignmentHeaders(assignmentCriteria, domain);
                     
            System.out.println("count of assignmentHeads:"+assignmentHeaders.length);
            for(AssignmentHeader assignmentHeader: assignmentHeaders){

            	if (assignmentHeader.getProcessId() == (Long)criteria.get("processId") &&
            	    assignmentHeader.getAssigneeId().equals((String )criteria.get("loginUsername"))&&
            	    assignmentHeader.getActivityDefId().equals((String )criteria.get("activityDefId")) ){
            		assignmentId = assignmentHeader.getAssignmentId();
            		activityId = assignmentHeader.getManualActivityId();
            		activityName =assignmentHeader.getActivityName();            		
            		break;
            	}
            }            
            System.out.println("processId:"+assignmentId);
            if (assignmentId != 0 ){
            	adminSB.completeAssignment(assignmentId, context, domain);
            }else{
            	
            }
           
           
            result[0]=(Long)criteria.get("processId");           
            result[1]=activityId;            
            result[2]=activityName;           
            return result;
          
           
      
        }
        catch (Exception e)        {
        	log.error("流程...時發生錯誤："+e.getMessage());
            throw new EJBException(e);
        }
      
    }
	
	public static boolean isCurrentRunning(Long processId) throws Exception{
		boolean isRunning = false;
		WorkflowClientSB clientSB = com.tbcn.ceap.workflow.client.ejb.ServiceLocator.getWorkflowClientSBHome().create();
        String domain = clientSB.getDefaultDomainId();
		ProcessDetail processDetail = clientSB.getProcessDetail(processId, domain);
		if(WF_PRC_RUNNING.equals(processDetail.getCurrentStatus()))
			isRunning = true;
		return isRunning;
	}
	
    *//** 確認狀態是否可進行簽核
     * @param Bean,ProcessId,AssignmentId
     * @return
     *//*
	public static void checkedCompleteAssignment(Long beanProcessId, Long processId, Long assignmentId) throws Exception{
		if(processId > 0){
        	//防止重複COMPLETE
        	if(assignmentId == 0)
        		throw new Exception("査無此單據簽核節點資訊");
        	WorkflowClientSB clientSB = com.tbcn.ceap.workflow.client.ejb.ServiceLocator.getWorkflowClientSBHome().create();
            String domain = clientSB.getDefaultDomainId();
            AssignmentDetail assignmentDetail = clientSB.getAssignmentDetail(assignmentId, domain);
            if(!ACCEPTED.equals(assignmentDetail.getStatus()))
            	throw new Exception("此單據流程狀態目前不可簽核送出");
        }else{
        	//防止起流程兩次
        	 if(beanProcessId > 0){
        		 throw new Exception("此單已起過流程，請勿重複送出");
        	 }
        }
	}
	


*/

	public static Object[] completeAssignment(long assignmentId, HashMap context) {
		// TODO Auto-generated method stub
		return null;
	}
	public static Object[] reAssignment(Long assignmentId, String loginName,
			HashMap context) {
		// TODO Auto-generated method stub
		return null;
	}
	public static Object[] start(String packageId, String processId,
			String version, String sourceReferenceType, HashMap context) {
		// TODO Auto-generated method stub
		return null;
	}}
