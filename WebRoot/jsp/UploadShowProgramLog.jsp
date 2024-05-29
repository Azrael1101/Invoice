<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="tw.com.tm.erp.exceptions.ValidationErrorException"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="tw.com.tm.erp.utils.StringTools"%>
<%@page import="tw.com.tm.erp.utils.StandardIEUtils"%>
<%@page import="org.apache.commons.beanutils.MethodUtils"%>
<%@page import="tw.com.tm.erp.hbm.service.SiProgramLogService"%>
<%@page import="tw.com.tm.erp.hbm.bean.SiProgramLog"%>

<%
    String errorMsg = null;
    String SYMBOL = "{$}";
    Double now = 0.0;
	Double all = 0.0;
    try {
        ApplicationContext context = SpringUtils.getApplicationContext();       
		String programId = request.getParameter("programId");
		String levelType = request.getParameter("levelType");
		String identification = request.getParameter("identification");	
		HashMap parameterMap = new HashMap();
		parameterMap.put("programId", programId);
		parameterMap.put("levelType", levelType);
		parameterMap.put("identification", identification);
		SiProgramLogService programLogService = (SiProgramLogService)context.getBean("siProgramLogService");
		List programLogs = programLogService.findByIdentification(parameterMap);
		
		parameterMap.put("programId", "Upload");
		parameterMap.put("levelType", "COUNT");
		parameterMap.put("identification", "IslandOrInternationUpload");
		List<SiProgramLog> countLogs = programLogService.findByIdentification(parameterMap);
		if(countLogs != null && countLogs.size() > 0){
			StringBuffer tmpMsg = new StringBuffer();
			for(int i = 0; i < countLogs.size(); i++){
		        tmpMsg.delete(0, tmpMsg.length());
		        SiProgramLog programLog = (SiProgramLog)countLogs.get(i);
		        tmpMsg.append(programLog.getMessage());
		        tmpMsg.append("<BR>");
		        out.println(tmpMsg);
		    }
		}else{
			%><script>location.reload();</script><%
		}
		String mesg = countLogs.get(0).getMessage();
		now = Double.valueOf(mesg.substring(0,mesg.indexOf("/")));
		all = Double.valueOf(mesg.substring(mesg.indexOf("/")+1));
		Double percent = (now/all)*100;
		System.out.println("getMessage:"+mesg);
		System.out.println("now:"+now);
		System.out.println("all:"+all);
		System.out.println("percent:"+percent);
		if(percent>=0.0 && percent<10.0){
			%><img src="../images/0.gif"></img><BR><%
		}else if(percent>=10.0 && percent<20.0){
			%><img src="../images/10.gif"></img><BR><%
		}else if(percent>=20.0 && percent<30.0){
			%><img src="../images/20.gif"></img><BR><%
		}else if(percent>=30.0 && percent<40.0){
			%><img src="../images/30.gif"></img><BR><%
		}else if(percent>=40.0 && percent<50.0){
			%><img src="../images/40.gif"></img><BR><%
		}else if(percent>=50.0 && percent<60.0){
			%><img src="../images/50.gif"></img><BR><%
		}else if(percent>=60.0 && percent<70.0){
			%><img src="../images/60.gif"></img><BR><%
		}else if(percent>=70.0 && percent<80.0){
			%><img src="../images/70.gif"></img><BR><%
		}else if(percent>=80.0 && percent<90.0){
			%><img src="../images/80.gif"></img><BR><%
		}else if(percent>=90.0 && percent<100.0){
			%><img src="../images/90.gif"></img><BR><%
		}else{
			%><img src="../images/100.gif"></img><BR><%
			%><script>for(int i = 0;i<time;i++){clearTimeout(i);}</script><%
			%><script>window.close();</script><%
		}
		
		%>請勿關閉視窗，避免檢核異常！<%
			if(programLogs != null && programLogs.size() > 0){
		    StringBuffer tmpMsg = new StringBuffer();
		    for(int i = 0; i < programLogs.size(); i++){
		        tmpMsg.delete(0, tmpMsg.length());
		        SiProgramLog programLog = (SiProgramLog)programLogs.get(i);
		        tmpMsg.append("(" + (i + 1) + ")");
		        tmpMsg.append(programLog.getMessage());
		        tmpMsg.append("<BR>");
		        out.println(tmpMsg);
		    }
		}else{
		    throw new Exception("查無錯誤訊息！");
		}
	    
    } catch (Exception ex) {
        errorMsg = ex.getMessage();
        //out.println(errorMsg);
    }
%>
<script language="javascript">
	var time = setTimeout("location.reload();",1000*3);
	
</script>
