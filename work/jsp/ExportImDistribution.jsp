<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.utils.*"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.utils.*"%>
<%@page import="tw.com.tm.erp.constants.*"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.hssf.util.Region"%>
<%
	String fileName = DateUtils.format(new Date(),DateUtils.C_DATA_PATTON_YYYYMMDD);
	
	response.setHeader("Content-disposition", "attachment; filename=Distribution_" + fileName + ".xls");
	String headId = request.getParameter("headId");
	ImDistributionHeadService imDistributionHeadService = (ImDistributionHeadService) SpringUtils.getApplicationContext().getBean("imDistributionHeadService");
	ImItemCurrentPriceViewDAO imItemCurrentPriceViewDAO = (ImItemCurrentPriceViewDAO) SpringUtils.getApplicationContext().getBean("imItemCurrentPriceViewDAO");
	ImDistributionHead head = imDistributionHeadService.findById(Long.valueOf(headId));

	List<ImDistributionItem> items = head.getImDistributionItems();
	List<ImDistributionShop> shops = head.getImDistributionShops();
	String display[][] = new String[items.size() + 1][shops.size() + 8];
	display[0][0] = "商品品牌";
	display[0][1] = "中類";
	display[0][2] = "系列";
	display[0][3] = "品號";
	display[0][4] = "品名";
	display[0][5] = "廠商代號";
	display[0][6] = "定價";
	display[0][7] = "可配數量";
	
	for (int i =0 ; i< shops.size() ; i++) {
		ImDistributionShop shop = (ImDistributionShop)shops.get(i);
		display[0][8+i] = shop.getShopCode();
	}
	
	for (int i = 1 ; i<= items.size() ; i++) {
		ImDistributionItem item = (ImDistributionItem)items.get(i-1);
		ImItemCurrentPriceView view = imItemCurrentPriceViewDAO.findCurrentPrice(head.getBrandCode(),item.getItemCode(),"1");
		if( null != view ){
			  display[i][0] = view.getItemBrand() ;
			  display[i][1] = view.getCategory02() ;
			  display[i][2] = view.getCategory13() ;
			  display[i][3] = view.getItemCode() ;
			  display[i][4] = view.getItemCName();
			  display[i][5] = view.getSupplierItemCode();
			  display[i][6] = String.valueOf(NumberUtils.getDouble(view.getUnitPrice()));
			  display[i][7] = String.valueOf(NumberUtils.getDouble(item.getQuantity()));
			  String shopQuantitys[] = item.getQuantitys().split(",");
			  for(int ii = 0 ; ii<shopQuantitys.length ; ii++){
				  display[i][8+ii] = shopQuantitys[ii];
			  }
			}
	}

	HSSFWorkbook wb = new HSSFWorkbook();
	ExcelUtils.createSheet(wb, display);
	wb.write(response.getOutputStream());
	response.getOutputStream().flush();
	out.clear();
	out = pageContext.pushBody();
	//response.getOutputStream().close();
%>

