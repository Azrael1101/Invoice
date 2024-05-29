<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.lang.*"%>
<%@page import="javax.servlet.ServletOutputStream"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.utils.*"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.constants.*"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.hssf.util.Region"%>
<%@page import="org.apache.commons.httpclient.*"%>
<%@page import="org.apache.commons.httpclient.methods.*"%>
<%@page import="org.apache.commons.httpclient.params.HttpMethodParams"%>
<%@page import="org.apache.commons.beanutils.MethodUtils"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="net.sf.json.JSONObject"%> 
<%
	//Log log = LogFactory.getLog(ImReceiveHeadService.class);
	StringBuffer returnData = new StringBuffer();
	String processSqlCode = request.getParameter("process_sql_code");
	String processObjectName = request.getParameter("process_object_name");
	String processObjectMethodName = request.getParameter("process_object_method_name");
	String processTest = request.getParameter("process_test");
	
	HttpSession userSession = request.getSession();
	try{
	
	String processSqlCodeFields[] = null;
	if (null != processSqlCode) {
		System.out.println("AjaxQuery.jsp processSqlCode=" + processSqlCode);
		//取得PROPERTIES 組出SQL
		String QueryNativeSqlProFile = "/query_native_sql.properties";
		String message ="";
		StringBuffer returnDataSting = new StringBuffer();
		Properties queryNativeSqlPro = new Properties();
		queryNativeSqlPro.load(NativeQueryDAO.class.getResourceAsStream(QueryNativeSqlProFile));
		String processSql = queryNativeSqlPro.getProperty(processSqlCode);
		String processSqlField = queryNativeSqlPro.getProperty(processSqlCode + "Fields");
		//System.out.println("processSqlField =" + processSqlField);
		processSqlCodeFields = StringTools.split(processSqlField, ",");
		//System.out.println("processSqlField len =" + processSqlCodeFields.length);
		Enumeration paramNames = request.getParameterNames();
		StringBuffer replaceParam = null;
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String values[] = request.getParameterValues(paramName);
			if (values.length > 0) {
				System.out.println(paramName + "=" + values[0]);
				replaceParam = new StringBuffer();
				replaceParam.append("{#");
				replaceParam.append(paramName);
				replaceParam.append("}");
				String value = "'" + values[0] + "'";
				processSql = StringUtils.replace(processSql, replaceParam.toString(), value);
			}
		}

		//執行SQL
		NativeQueryDAO nativeQueryDAO = (NativeQueryDAO) SpringUtils.getApplicationContext().getBean("nativeQueryDAO");
		List results = nativeQueryDAO.executeNativeSql(processSql);
		if (null != results && results.size() > 0) {
			for (int index = 0; index < results.size(); index++) {
				//建立回傳資料
				Object record = results.get(index);
				Object records[] = null;
				try {
					records = (Object[]) results.get(index);
				} catch (Exception ex) {
				}
				//log.error("processSqlCodeFields.length =" + processSqlCodeFields.length );
				
				if(null == records || records.length <= 0){					
					message =MessageStatus.FAIL;
				}else{
	
					if (null != records && records.length > 0 && processSqlCodeFields.length > 1) {
						for (int recordIndex = 0; recordIndex < records.length; recordIndex++) {
							returnDataSting.append(AjaxUtils.RESPONSE_TAG_START);
							returnDataSting.append(processSqlCodeFields[recordIndex].trim());
							returnDataSting.append("=");
							returnDataSting.append(records[recordIndex]);
							returnDataSting.append(AjaxUtils.RESPONSE_TAG_END);
						}
					} else if (null != record && records.length > 0) {
						returnDataSting.append(AjaxUtils.RESPONSE_TAG_START);
						returnDataSting.append(processSqlCodeFields[0].trim());
						returnDataSting.append("=");
						returnDataSting.append(record);
						returnDataSting.append(AjaxUtils.RESPONSE_TAG_END);
					} 
					message =MessageStatus.SUCCESS;
					
				}
				
			}
		} else {
			message =MessageStatus.FAIL;
		}
		returnData.append(AjaxUtils.RESPONSE_TAG_START);
		returnData.append(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[0]);
		returnData.append("=");
		returnData.append(message);					
		returnData.append(AjaxUtils.RESPONSE_TAG_END);	
		returnData.append(returnDataSting.toString());
		returnData.append(AjaxUtils.RESPONSE_TAG_START);
		returnData.append(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[1]);
		returnData.append("=");
		returnData.append(MessageStatus.SUCCESS.equals(message) ? "存檔成功":"查無資料..");
		returnData.append(AjaxUtils.RESPONSE_TAG_END);

	} else if (null != processObjectName) {
		System.out.println("AjaxQuery.jsp request -> processObjectName=" + processObjectName + " ,processObjectMethodName="
				+ processObjectMethodName);
		//RUN 物件
		Properties httpRequestPro = new Properties();
		Object processObj = SpringUtils.getApplicationContext().getBean(processObjectName);
		Enumeration names = request.getParameterNames();
		String beanName ="";
		Map pojoBeanMap = new HashMap();
		Object fromClientObj = null;
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] values = request.getParameterValues(name);
			if ((null != values) && (values.length > 0)) {
				String value = values[0];
				if("vatBean".equals(name)){
					JSONObject jsonO = JSONObject.fromObject(value);
					for(Iterator iter = jsonO.keys(); iter.hasNext();){
						String keyName = (String)iter.next();
						JSONObject nextO = (JSONObject)jsonO.get(keyName);
						fromClientObj = JSONObject.toBean(jsonO.getJSONObject(keyName));
						pojoBeanMap.put(keyName,fromClientObj);
					}
					
				}else{
					httpRequestPro.setProperty(name, value);
				}
				System.out.println("request ajax name=" + name + ",value=" + value);
			}
		}
 	
		System.out.println("AjaxQuery.jsp before invokeMethod -> processObjectName=" + processObjectName + " ,processObjectMethodName="
				+ processObjectMethodName);
				
		
		//回傳回來一定是List<Properties>
		
		
		List<Properties> processObjReturn = new ArrayList() ;
		String errorMsg = null ;
		Object[] objA = new Object[1];	
		objA[0]=httpRequestPro;	
		try{
			if(!pojoBeanMap.isEmpty()){//若是沒有JSON的資訊就代表舊版，要對應到舊的service method
				processObjReturn = (List<Properties>) MethodUtils.invokeMethod(processObj, processObjectMethodName,pojoBeanMap);
				
			}else{
				processObjReturn = (List<Properties>) MethodUtils.invokeMethod(processObj, processObjectMethodName,httpRequestPro);
			}
					
			
		}catch(Exception ex){
			errorMsg = ex.getMessage();
		}
		
		// 訊息處理 START 目前只抓只抓第一個LIST 
		Properties reMessageProperties = new Properties() ;
		if(  null != processObjReturn && processObjReturn.size() > 0 && null != processObjReturn.get(0) ){
			//System.out.println("AjaxQuery.processObjReturn before addMessage log ->" + processObjReturn );
			reMessageProperties = processObjReturn.get(0) ;
		}
		
		if (null != errorMsg) {
			// 回傳成功的訊息 
			reMessageProperties.setProperty(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[0], MessageStatus.ERROR);
			reMessageProperties.setProperty(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[1], errorMsg);
		} else {
			// 回傳成功的訊息
			reMessageProperties.setProperty(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[0], MessageStatus.SUCCESS);
			reMessageProperties.setProperty(AjaxUtils.RETURN_STATUS_MESSAGE_FIELD_NAMES[1], "存檔成功");
		}
		if( null != processObjReturn && processObjReturn.size() > 0 ){
			processObjReturn.set(0,reMessageProperties);
		}else{
			processObjReturn.add(reMessageProperties);
		}
	
		//System.out.println("AjaxQuery.processObjReturn after addMessage log ->" + processObjReturn );	
		// 訊息處理 END		
				
		if (null != processObjReturn) {
			List<Properties> processObjReturnPros = processObjReturn;
			for (Properties processObjReturnPro : processObjReturnPros) {
				Enumeration keys = processObjReturnPro.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					String value = processObjReturnPro.getProperty(key);
					returnData.append(AjaxUtils.RESPONSE_TAG_START);
					returnData.append(key);
					returnData.append("=");
					returnData.append(value);
					returnData.append(AjaxUtils.RESPONSE_TAG_END);
				}
			}
		}
	} else if (null != processTest) {
		if("init".equalsIgnoreCase(processTest)){
			System.out.println("AjaxQuery.jsp - mac.json.test, "+processTest);
			returnData.append("<vatBean={");
			returnData.append("'title':'調撥單維護作業',");
			returnData.append("'getLastUpdateDate':function(){return '2009/6/23';},");
			returnData.append("'test1':'test variable',");
			returnData.append("'arrivalDate':'2009/06/30',");
			returnData.append("'deliveryDate':'2009/01/01',");						
			returnData.append("'items':{'name':'@h0.header', 'type':'LABEL', 'desc':'單別'}");
			returnData.append("}><status=Success>");
		}else{
			System.out.println("AjaxQuery.jsp - mac.json.init, "+processTest);
			returnData.append("<vatBean={");
			returnData.append("'getLastUpdateDate':function(){return '2009/6/23';}");
			returnData.append("}><status=Success>");
		}	
	} else {
		returnData.append("請確認 process_sql_code / process_object_name參數是否存在..");
	}
	System.out.println("AjaxQuery.processObjReturn after addMessage log ->" + returnData.toString() );
	out.println("returnData:"+returnData.toString());
	
	}catch(Exception ex){
		ex.printStackTrace();
	}
%>