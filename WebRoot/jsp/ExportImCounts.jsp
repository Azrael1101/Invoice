<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="tw.com.tm.erp.exceptions.ValidationErrorException"%>
<%@page import="tw.com.tm.erp.utils.StandardIEUtils"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.service.ImInventoryCountsService"%>
<%@page import="tw.com.tm.erp.hbm.bean.ImInventoryCountsHead"%>
<%@page import="tw.com.tm.erp.utils.DateUtils"%>
<%@page import="tw.com.tm.erp.standardie.ImInventoryCountDataInfo"%>

<%
    String errorMsg = null;
    try {
	    String headId = request.getParameter("headId");
	    if (!StringUtils.hasText(headId)) {
		    throw new ValidationErrorException("無法取得盤點單主鍵參數！");
		}
		String exportBeanName = request.getParameter("exportBeanName");
		if (!StringUtils.hasText(exportBeanName)) {
		    throw new ValidationErrorException("無法取得匯出實體名稱參數！");
		}
		
        ImInventoryCountsService inventoryCountsService = (ImInventoryCountsService)SpringUtils.getApplicationContext().getBean("imInventoryCountsService");
        ImInventoryCountsHead inventoryCountsHead = inventoryCountsService.findImInventoryCountsHeadById(new Long(headId));      
        if(inventoryCountsHead != null){
            String fileType = "TXT";
            String contentType = "application/plain";
            String warehouseCode = inventoryCountsHead.getWarehouseCode();
            String fileName = warehouseCode + "_" + DateUtils.format(new Date(), DateUtils.C_DATA_PATTON_YYYYMMDD) + ".txt";
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		    response.setContentType(contentType);       
	        ServletOutputStream sos = response.getOutputStream();		
		    StandardIEUtils.executeExport(sos, exportBeanName, fileType, null, new ImInventoryCountDataInfo(inventoryCountsHead, inventoryCountsHead.getImInventoryCountsLines()),"");
            
            sos.flush();
		    out.clear();
		    out = pageContext.pushBody();      
	    }else{
	        throw new ValidationErrorException("查無盤點單主鍵：" + headId +"的資料！");
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