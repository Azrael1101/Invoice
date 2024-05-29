<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="tw.com.tm.erp.hbm.service.BuCommonPhraseService"%>
<%@page import="tw.com.tm.erp.hbm.service.ImInventoryCountsService"%>
<%@page import="tw.com.tm.erp.hbm.bean.BuCommonPhraseLine"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.utils.DES"%>

<%
    String errorMsg = null;
    try {
        String reportUrl = null;
        String reportFileName = null;
        String reportFunctionCode = null;
        String encryText = null;
        StringBuffer url = null;   
        String reportType = request.getParameter("reportType");
        String countsLotNo = request.getParameter("countsLotNo");
        if(!StringUtils.hasText(reportType)){
            throw new Exception("報表類型參數為空值！");
        }  
        String headId = request.getParameter("headId");
        if(!StringUtils.hasText(headId)){
            throw new Exception("主鍵參數為空值！");
        }      
        String brandCode = request.getParameter("brandCode");
        if(!StringUtils.hasText(brandCode)){
            throw new Exception("品牌參數為空值！");
        } 
        String employeeCode = request.getParameter("employeeCode");
        if(!StringUtils.hasText(employeeCode)){
            throw new Exception("使用者參數為空值！");
        }
        String countsId = request.getParameter("countsId");
        if(!StringUtils.hasText(countsId)){
            throw new Exception("盤點代號參數為空值！");
        }
        String warehouseCode = request.getParameter("warehouseCode");
        if(!StringUtils.hasText(warehouseCode)){
            throw new Exception("庫別代號參數為空值！");
        }
        String countsType = request.getParameter("countsType");
        if(!StringUtils.hasText(countsType)){
            throw new Exception("盤點方式參數為空值！");
        }else{
        	if("3".equals(reportType)){
        		if("2".equals(countsType)){
		        	
		        	if(!StringUtils.hasText(countsLotNo)){
		            	throw new Exception("盤點批號參數為空值！");
		        	}
		        }
	        }
        }
               
        BuCommonPhraseService buCommonPhraseService = (BuCommonPhraseService)SpringUtils.getApplicationContext().getBean("buCommonPhraseService");
        BuCommonPhraseLine reportConfig = buCommonPhraseService.getBuCommonPhraseLine("ReportURL", "CrystalReportURL");
        if(reportConfig != null){
            reportUrl = reportConfig.getAttribute1();
        }else{
            throw new Exception("查無報表URL！");
        }     
        
        if("1".equals(reportType)){
            try{
                ImInventoryCountsService service = (ImInventoryCountsService)SpringUtils.getApplicationContext().getBean("imInventoryCountsService");
                service.updateInventoryCountsForPrint(headId);
            }catch (Exception ex) {
                //out.println(errorMsg);
            }
            reportFileName = "im0120.rpt";
            reportFunctionCode = "IM0120";       
        }else if("2".equals(reportType)){
            if("1".equals(countsType)){
                reportFileName = "im0121.rpt";
                reportFunctionCode = "IM0121";              
            }else{
                reportFileName = "im0122.rpt";
                reportFunctionCode = "IM0122";
            }
        }else{
        	if("1".equals(countsType)){
	            reportFileName = "im0123_countsType1.rpt";
	            reportFunctionCode = "IM0123_COUNTSTYPE1";
            }else{
            	reportFileName = "im0123_countsType2.rpt";
	            reportFunctionCode = "IM0123_COUNTSTYPE2";
            }
        }
        StringBuffer permissionInfo = new StringBuffer(brandCode);
        permissionInfo.append("@@");
        permissionInfo.append(employeeCode);
        permissionInfo.append("@@");
        permissionInfo.append(brandCode);
        permissionInfo.append("_");
        permissionInfo.append(reportFunctionCode);
        permissionInfo.append("@@");
        encryText = new DES().encrypt(permissionInfo + String.valueOf(new Date().getTime()));
        url = new StringBuffer(reportUrl);
        url.append(reportFileName);
        url.append("?crypto=");
        url.append(encryText);
        url.append("&prompt0=");
        url.append(brandCode);
        url.append("&prompt1=");
        url.append(countsId);
        if(!"1".equals(reportType)){
            url.append("&prompt2=");
            url.append(warehouseCode);     
        }
        if(StringUtils.hasText(countsLotNo)){
			url.append("&prompt4=");
        	url.append(countsLotNo);
		}

        out.println(url.toString());
        response.sendRedirect(url.toString());   	
    } catch (Exception ex) {
        errorMsg = ex.getMessage();
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
