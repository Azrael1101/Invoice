<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="tw.com.tm.erp.exceptions.ValidationErrorException"%>
<%@page import="tw.com.tm.erp.utils.StandardIEUtils"%>
<%@page import="tw.com.tm.erp.standardie.DataInfo"%>
<%@page import="tw.com.tm.erp.utils.StringTools"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="org.apache.commons.beanutils.MethodUtils"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>

<%
    String errorMsg = null;
    try {
		String exportBeanName = request.getParameter("exportBeanName");
		String fileName = null;
		if (!StringUtils.hasText(exportBeanName)) {
		    throw new ValidationErrorException("無法取得匯出實體名稱參數！");
		}else{
		    fileName = exportBeanName + "_" + System.currentTimeMillis();
		}
		
		String fileType = request.getParameter("fileType");
		String contentType = null;
		if (!StringUtils.hasText(fileType)) {
		    throw new ValidationErrorException("無法取得匯出檔案類型參數！");
		}else if("XLS".equalsIgnoreCase(fileType)){
		    fileName += ".xls";
		    contentType = "application/vnd.ms-excel";
		}else if("TXT".equalsIgnoreCase(fileType)){
		    fileName += ".txt";
		    contentType = "application/plain";
		}else{
		    throw new ValidationErrorException("檔案類型參數設定錯誤！");
		}
        
        String processObjectName = request.getParameter("processObjectName");
	    String processObjectMethodName = request.getParameter("processObjectMethodName");
	    String gridFieldName = request.getParameter("gridFieldName");
	    String arguments = request.getParameter("arguments");
	    String parameterTypes = request.getParameter("parameterTypes");
	    String execDataInfo = request.getParameter("execDataInfo");
	    String function = request.getParameter("onePage");
	    DataInfo dataInfo = null;
	    
	    if (!StringUtils.hasText(processObjectName)) {
		    throw new ValidationErrorException("無法取得欲執行的元件參數！");
		}else if (!StringUtils.hasText(processObjectMethodName)) {
		    throw new ValidationErrorException("無法取得欲執行的元件功能參數！");
		}else if (!StringUtils.hasText(gridFieldName)) {
		    throw new ValidationErrorException("無法取得明細欄位參數！");
		}else if (!StringUtils.hasText(arguments)) {
		    throw new ValidationErrorException("無法取得查詢條件參數！");
		}else if (!StringUtils.hasText(parameterTypes)) {
		    throw new ValidationErrorException("無法取得查詢條件型別參數！");
		}
		
		ApplicationContext context = SpringUtils.getApplicationContext();
        String[] argumentsArray = StringTools.StringToken(arguments, "{$}");
	    String[] parameterTypesArray = StringTools.StringToken(parameterTypes, "{$}");
	    Object[] actualArguments = StandardIEUtils.parameterTypeConvert(argumentsArray, parameterTypesArray);
        
        Object processObj = context.getBean(processObjectName);
	    Object entityBean = MethodUtils.invokeMethod(processObj, processObjectMethodName, actualArguments);
	    
	    if(entityBean != null){
	        List gridBeans = (List)PropertyUtils.getProperty(entityBean, gridFieldName);
	        if (StringUtils.hasText(execDataInfo) && "Y".equals(execDataInfo)) {
		        String[] dataInfoArray = StandardIEUtils.getDataInfo(exportBeanName, fileType);
		        if(dataInfoArray.length > 0){
		            Class clsTask = Class.forName(dataInfoArray[0]);	
                    dataInfo = (DataInfo)clsTask.newInstance();
                    PropertyUtils.setProperty(dataInfo, "headData", entityBean);
                    PropertyUtils.setProperty(dataInfo, "lineData", gridBeans);		        
		        }
		    }
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		    response.setContentType(contentType);       
	        ServletOutputStream sos = response.getOutputStream();	
		    StandardIEUtils.executeExport(sos, exportBeanName, fileType, gridBeans, dataInfo,function);
	    
		    sos.flush();
		    out.clear();
		    out = pageContext.pushBody();
		}else{
	        throw new ValidationErrorException("查無欲匯出的資料！");
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
