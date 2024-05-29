<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="tw.com.tm.erp.exceptions.ValidationErrorException"%>
<%@page import="tw.com.tm.erp.utils.StandardIEUtils"%>
<%@page import="tw.com.tm.erp.standardie.DataInfo"%>
<%@page import="tw.com.tm.erp.standardie.BarcodeDataInfo"%>
<%@page import="tw.com.tm.erp.utils.sp.TmpAppStockStatisticsService"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%
	System.out.println("StandardExport");
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
		String function = (String)request.getParameter("function");
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
        List detailEntityBeans = (List)session.getAttribute("detailEntityBeans");
        DataInfo dataInfo = (DataInfo)session.getAttribute("dataInfo");
        if("ONHAND_ITEM".equals(exportBeanName) || "ONHAND_ITEM_T1".equals(exportBeanName)){
	    	String brandCode = (String)request.getParameter("brandCode");
	    	String transactionDate = (String)request.getParameter("transactionDate");
	    	// yyyy/MM/dd => yyyyMMdd
	    	//transactionDate = transactionDate.replaceAll("/","");
	    	String wareHouseCode = (String)request.getParameter("wareHouseCode");
	    	HashMap pMap = new HashMap();
	    	SimpleDateFormat sFmt = new SimpleDateFormat("yyyyMMdd");
	    	transactionDate = sFmt.format(new Date(transactionDate));
	    	pMap.put("brandCode",brandCode);
	    	pMap.put("warehouseCode",wareHouseCode);
	    	pMap.put("onHandDate",transactionDate);
	    	pMap.put("overZero","Y");
	    	TmpAppStockStatisticsService appStockServices = (TmpAppStockStatisticsService)SpringUtils.getApplicationContext().getBean("tmpAppStockStatisticsService");
	    	detailEntityBeans=appStockServices.getStockStatisticsForBarcode(pMap);
	    }
	    
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType(contentType);       
	    ServletOutputStream sos = response.getOutputStream();	
	    
	    if("BARCODE_ITEM".equals(exportBeanName) || "BARCODE_ITEM_T1".equals(exportBeanName) ||"IMR_ITEM".equals(exportBeanName) || 
	    	"IMR_ITEM_WATCH".equals(exportBeanName)|| "IMM_ITEM".equals(exportBeanName) || "ONHAND_ITEM".equals(exportBeanName) || "ONHAND_ITEM_T1".equals(exportBeanName)){
	    	dataInfo = new BarcodeDataInfo();
	    	dataInfo.setLineData(detailEntityBeans);
	    }
	    
		StandardIEUtils.executeExport(sos, exportBeanName, fileType, detailEntityBeans, dataInfo,function);
	    
		sos.flush();
		out.clear();
		out = pageContext.pushBody();
    } catch (Exception ex) {
        //errorMsg = ex.getMessage();
        ex.printStackTrace();
        //out.println(errorMsg);
    }
%>

<script language="javascript">
  <%
  if (StringUtils.hasText(errorMsg)){
  %>
      alert("<%=errorMsg%>");
  <%
  }else{
  %>
  	window.close();
  <%
  }
  %>
</script>
