<%
/*
 *---------------------------------------------------------------------------------------
 *
 *	使用方法:
 *		exportBeanName = 匯出欄位格式，設定於standard_ie.properties裡面，若有一個以上需用 "," 做分隔
 *		fileType = 匯出檔案格式 (TXT、XLS)
 *		processObjectName = 匯出執行的java檔案，若有一個以上需用 "," 做分隔
 *		processObjectMethodName = 匯出執行的java功能，若有一個以上需用 "," 做分隔
 *
 *---------------------------------------------------------------------------------------
 * 
 */
%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="tw.com.tm.erp.exceptions.ValidationErrorException"%>
<%@page import="java.lang.reflect.InvocationTargetException"%>
<%@page import="tw.com.tm.erp.utils.StandardIEUtils"%>
<%@page import="tw.com.tm.erp.standardie.SelectDataInfo"%>
<%@page import="tw.com.tm.erp.standardie.DataInfo"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="org.apache.commons.beanutils.MethodUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>

<%
	String errorMsg = null;
	try {

		//standard_ie.properties裡面設定的beanName
		String exportBeanName = request.getParameter("exportBeanName");
		String status = request.getParameter("status");
		//檔案名稱
		String fileName = null;
		if (!StringUtils.hasText(exportBeanName)) {
			throw new ValidationErrorException("無法取得匯出實體名稱參數！");
		} else {
			fileName = exportBeanName + "_" + System.currentTimeMillis();
		}

		//匯出檔案類型，分別為txt與xls
		String fileType = request.getParameter("fileType");
		String function = request.getParameter("function");
		String contentType = null;

		if (!StringUtils.hasText(fileType)) {
			throw new ValidationErrorException("無法取得匯出檔案類型參數！");
		} else if ("XLS".equalsIgnoreCase(fileType)) {
			fileName += ".xls";
			contentType = "application/vnd.ms-excel";
		} else if ("TXT".equalsIgnoreCase(fileType)) {

			fileName += ".txt";
			contentType = "text/html; charset=utf-8";
		} else {
			throw new ValidationErrorException("檔案類型參數設定錯誤！");
		}

		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType(contentType);

		//執行的java檔案名稱
		String processObjectName = request.getParameter("processObjectName");
		//執行的java方法名稱
		String processObjectMethodName = request.getParameter("processObjectMethodName");

		if (!StringUtils.hasText(processObjectName)) {
			throw new ValidationErrorException("無法取得欲執行的元件參數！");
		} else if (!StringUtils.hasText(processObjectMethodName)) {
			throw new ValidationErrorException("無法取得欲執行的元件功能參數！");
		}

		//可以同時執行多次，用","做分隔，前提是三個參數的數量要一致
		String[] exportBeanNames = exportBeanName.split(",");
		String[] processObjectNames = processObjectName.split(",");
		String[] processObjectMethodNames = processObjectMethodName.split(",");

		//判斷三個功能參數要一制
		if(exportBeanNames.length != processObjectNames.length || processObjectNames.length != processObjectMethodNames.length)
			throw new ValidationErrorException("功能參數數量不匹配！");
			
		ApplicationContext context = SpringUtils.getApplicationContext();
		Object[] actualArguments = new Object[] { request };

		//後端回傳的物件，用來組成檔案(TXT、XLS)
		SelectDataInfo entityBean = null;

		//負責輸出的Buffer，會不停的累加		
		ServletOutputStream sos = response.getOutputStream();

		//跑迴圈，有幾次參數就跑幾次，會不停的往下壘加
		for(int i = 0 ; i < processObjectNames.length ; i++){
			Object processObj = context.getBean(processObjectNames[i]);
			try{
				if (request.getParameter("exportType") != null && "barcode".equals(request.getParameter("exportType"))) { //條碼匯出
					MethodUtils.invokeMethod(processObj, processObjectMethodNames[i], actualArguments);
				} else {
					//通常會呼叫javaMethod回傳一個名為 SelectDataInfo 的物件，裡面會記錄著headData(各欄位名稱) 與 lineDate(各欄位值) 的資訊
					entityBean = (SelectDataInfo) MethodUtils.invokeMethod(processObj, processObjectMethodNames[i], actualArguments);
				}
			}catch (InvocationTargetException e){
					throw (Exception)e.getCause();
			}
			if (entityBean != null && !"pTxt".equals(request.getParameter("exportType"))) {
				StandardIEUtils.executeExport(sos, exportBeanNames[i], fileType, null, entityBean,function);
			} else if (request.getParameter("exportType") != null && "barcode".equals(request.getParameter("exportType"))) { //條碼匯出
				List detailEntityBeans = (List) session.getAttribute("detailEntityBeans");
				DataInfo dataInfo = (DataInfo) session.getAttribute("dataInfo");
					StandardIEUtils.executeExport(sos, exportBeanNames[i], fileType, detailEntityBeans, dataInfo,function);

			} else if (request.getParameter("exportType") != null && "pTxt".equals(request.getParameter("exportType"))) { //
				System.out.println("pTxt");
				Long headId = Long.valueOf(request.getParameter("headId"));
				String brandCode = request.getParameter("brandCode");
				StandardIEUtils.executeTxtExport(sos,headId,brandCode,status);
				
			} else {
				throw new ValidationErrorException("查無欲匯出的資料！");
			}
		}
		
		sos.flush();
		out.clear();
		out = pageContext.pushBody();
		
	} catch (Exception ex) {
		ex.printStackTrace();
		errorMsg = ex.getMessage();
		//out.println(errorMsg);
		out.print("<script language='javascript'>alert('" + errorMsg + "');window.close();</script>");
	}
%>
