<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Properties"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="tw.com.tm.erp.exceptions.ValidationErrorException"%>
<%@page import="tw.com.tm.erp.utils.StandardIEUtils"%>
<%@page import="tw.com.tm.erp.standardie.DataInfo"%>
<%@page import="tw.com.tm.erp.utils.StringTools"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.utils.TxtUtil"%>
<%@page import="tw.com.tm.erp.utils.AjaxUtils"%>
<%@page import="org.apache.commons.beanutils.MethodUtils"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>



<%
    String errorMsg = null;
    try {
		String processObjectName = request.getParameter("processObjectName");
		String processObjectMethodName = request.getParameter("processObjectMethodName");

		String exportBeanName = request.getParameter("exportBeanName");
		String fileName = null;
		if (!StringUtils.hasText(exportBeanName)) {
		    throw new ValidationErrorException("無法取得匯出實體名稱參數！");
		} else {
		    fileName = exportBeanName + "_"
			    + System.currentTimeMillis();
		}

		String fileType = request.getParameter("fileType");
		String contentType = null;
		if (!StringUtils.hasText(fileType)) {
		    throw new ValidationErrorException("無法取得匯出檔案類型參數！");
		} else if ("XLS".equalsIgnoreCase(fileType)) {
		    fileName += ".xls";
		    contentType = "application/vnd.ms-excel";
		} else if ("TXT".equalsIgnoreCase(fileType)) {
		    fileName += ".txt";
		    contentType = "application/plain";
		} else {
		    throw new ValidationErrorException("檔案類型參數設定錯誤！");
		}
		
		System.out.println("0 = 0");
		Properties properties = new Properties();
		
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String name = (String) paramNames.nextElement();
			String[] values = request.getParameterValues(name);
			if ((null != values) && (values.length > 0)) {
				String value = values[0];
				properties.setProperty(name, value);
			}
		}
		
		String brandCode = request.getParameter("brandCode");
//		String barCodeType = request.getParameter("barCodeType");
//		String orderTypeCode = request.getParameter("orderTypeCode");
//		String orderNo = request.getParameter("orderNo");
//		String orderNoEnd = request.getParameter("orderNoEnd");
//		String orderBy = request.getParameter("orderBy");
//		String price = request.getParameter("price");
//		String category = request.getParameter("category");
//		String category01 = request.getParameter("category01");
//		String category02 = request.getParameter("category02");
//		String warehouseCode = request.getParameter("warehouseCode");
//		String showZero = request.getParameter("showZero");
//		String taxType = request.getParameter("taxType");
//		String startDate = request.getParameter("startDate");
//		String endDate = request.getParameter("endDate");
//		String supplierCode = request.getParameter("supplierCode");
//		String timeScope = request.getParameter("timeScope");

		if (!StringUtils.hasText(brandCode)) {
		    throw new ValidationErrorException("無法取得品牌！");
		}
		//		if (!StringUtils.hasText(barCodeType)) {
		//		    throw new ValidationErrorException("無法取得欲執行的元件參數！");
		//		}else if (!StringUtils.hasText(processObjectMethodName)) {
		//		    throw new ValidationErrorException("無法取得欲執行的元件功能參數！");
		//		}else if (!StringUtils.hasText(gridFieldName)) {
		//		    throw new ValidationErrorException("無法取得明細欄位參數！");
		//		}else if (!StringUtils.hasText(arguments)) {
		//		    throw new ValidationErrorException("無法取得查詢條件參數！");
		//		}else if (!StringUtils.hasText(parameterTypes)) {
		//		    throw new ValidationErrorException("無法取得查詢條件型別參數！");
		//		}

		System.out.println("brandCode = "+ AjaxUtils.getPropertiesValue(brandCode, ""));
//		System.out.println("barCodeType = " + AjaxUtils.getPropertiesValue(barCodeType, ""));
//		System.out.println("orderTypeCode = " + AjaxUtils.getPropertiesValue(orderTypeCode, ""));
//		System.out.println("orderNo = " + AjaxUtils.getPropertiesValue(orderNo, ""));
//		System.out.println("orderNoEnd = " + AjaxUtils.getPropertiesValue(orderNoEnd, ""));
//		System.out.println("orderBy = " + AjaxUtils.getPropertiesValue(orderBy, ""));
//		System.out.println("price = " + AjaxUtils.getPropertiesValue(price, ""));
//		System.out.println("category = " + AjaxUtils.getPropertiesValue(category, ""));
//		System.out.println("category01 = " + AjaxUtils.getPropertiesValue(category01, ""));
//		System.out.println("category02 = " + AjaxUtils.getPropertiesValue(category02, ""));
//		System.out.println("warehouseCode = " + AjaxUtils.getPropertiesValue(warehouseCode, ""));
//		System.out.println("showZero = " + AjaxUtils.getPropertiesValue(showZero	, ""));
//		System.out.println("taxType = " + AjaxUtils.getPropertiesValue(taxType, ""));
//		System.out.println("startDate = " + AjaxUtils.getPropertiesValue(startDate, ""));
//		System.out.println("endDate = " + AjaxUtils.getPropertiesValue(endDate, ""));
//		System.out.println("supplierCode = " + AjaxUtils.getPropertiesValue(supplierCode, ""));
//		System.out.println("timeScope = " + AjaxUtils.getPropertiesValue(timeScope, ""));

		ApplicationContext context = SpringUtils.getApplicationContext();
//		System.out.println("1 = 1");
//		properties.setProperty("brandCode", brandCode);
//		System.out.println("2 = 2");
//		properties.setProperty("barCodeType", barCodeType);
//		System.out.println("3 = 3");
//		properties.setProperty("orderTypeCode", orderTypeCode);
//		System.out.println("3.1 = 3.1");
//		properties.setProperty("orderNo", orderNo);
//		System.out.println("4 = 4");
//		properties.setProperty("orderNoEnd", orderNoEnd);
//		System.out.println("5 = 5");
//		properties.setProperty("orderBy", orderBy);
//		System.out.println("6 = 6");
//		properties.setProperty("price", price);
//		System.out.println("7 = 7");
//		properties.setProperty("category", category);
//		System.out.println("8 = 8");
//		properties.setProperty("category01", category01);
//		System.out.println("8.2 = 8.2");
//		properties.setProperty("category02", category02);
//		System.out.println("8.3 = 8.3");
//		properties.setProperty("warehouseCode", warehouseCode);
//		System.out.println("8.4 = 8.4");
//		properties.setProperty("showZero", showZero	);
//		System.out.println("9 = 9");
//		properties.setProperty("taxType", taxType);
//		properties.setProperty("startDate", startDate);
//		properties.setProperty("endDate", endDate);
//		properties.setProperty("supplierCode", supplierCode);
//		properties.setProperty("timeScope", timeScope);

		System.out.println("a = a");
		Object processObj = context.getBean(processObjectName);
		System.out.println("b = b");
		List entityBean = (List) MethodUtils.invokeMethod(processObj,
			processObjectMethodName, properties);
		System.out.println("c = c");
		if (entityBean != null && entityBean.size() > 0) {
		    System.out.println("d = d");
		    response.setHeader("Content-disposition",
			    "attachment; filename=" + fileName);
		    response.setContentType(contentType);
		    ServletOutputStream sos = response.getOutputStream();
		    HashMap map = new HashMap();
		    map.put("assembly", entityBean);
		    System.out.println("e = e");
		    TxtUtil.exportTxt(sos, map);
		    System.out.println("f = f");
		    sos.flush();
		    out.clear();
		    out = pageContext.pushBody();
		} else {
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
