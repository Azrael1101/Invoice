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
    try {
        ApplicationContext context = SpringUtils.getApplicationContext();       
		String programId = request.getParameter("programId");
		String levelType = request.getParameter("levelType");
		String identification = request.getParameter("identification");
		String processObjectName = request.getParameter("processObjectName");
		String processObjectMethodName = request.getParameter("processObjectMethodName");		
		String arguments = request.getParameter("arguments");
		String parameterTypes = request.getParameter("parameterTypes");
			
		if (!StringUtils.hasText(programId)) {
		    throw new ValidationErrorException("程式代號參數為空值！");
		}else if (!StringUtils.hasText(identification)) {
		    if(!StringUtils.hasText(processObjectName)){
                throw new ValidationErrorException("元件參數為空值！");
            }else if(!StringUtils.hasText(processObjectMethodName)){
                throw new ValidationErrorException("元件功能參數為空值！");
            }else if(!StringUtils.hasText(arguments)){
                throw new ValidationErrorException("查詢條件參數為空值！");
            }else if(!StringUtils.hasText(parameterTypes)){
                throw new ValidationErrorException("查詢條件型別參數為空值！");
            }else{
               String[] argumentsArray = StringTools.StringToken(arguments, SYMBOL);
	           String[] parameterTypesArray = StringTools.StringToken(parameterTypes, SYMBOL);
               Object[] actualArguments = StandardIEUtils.parameterTypeConvert(argumentsArray, parameterTypesArray);
               Object eventObj = context.getBean(processObjectName);
               identification = (String)MethodUtils.invokeMethod(eventObj, processObjectMethodName, actualArguments);
            }
		}
		HashMap parameterMap = new HashMap();
		parameterMap.put("programId", programId);
		parameterMap.put("levelType", levelType);
		parameterMap.put("identification", identification);
		SiProgramLogService programLogService = (SiProgramLogService)context.getBean("siProgramLogService");
		List programLogs = programLogService.findByIdentification(parameterMap);
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
  <%
  if (StringUtils.hasText(errorMsg)){
  %>
      alert("<%=errorMsg%>");
      window.close();
  <%
  }
  %>
</script>
