<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.ImItemOnHandView" %>
<%@page import="tw.com.tm.erp.hbm.bean.ImItem" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
String itemCode;
String brandCode = request.getParameter("brandCode");
ImItem item = null;
List<ImItemOnHandView> returnOnhandResult = new ArrayList(0);
Map returnMap = new HashMap();

ImItemService imItemService = (ImItemService)SpringUtils.getApplicationContext().getBean("imItemService");
ImMovementMainService imMovementMainService = (ImMovementMainService)SpringUtils.getApplicationContext().getBean("imMovementMainService");

 
if(request.getParameter("itemCode")!=null){
	itemCode=request.getParameter("itemCode");
	returnMap = imItemService.getAJAXItemInfomation(brandCode,itemCode);
}
%>
<HTML>d
<HEAD>
<% 

if(request.getParameter("itemCode")!=null){
	item =(ImItem) returnMap.get("Item");
	
	itemCode=item.getItemCode();
	returnOnhandResult = imMovementMainService.executeOnHandInitial(brandCode,itemCode);
}
  %>
<META content=no-cache http-equiv=Pragma></META>
<META content=no-cache http-equiv=Cache-Control></META>
<SCRIPT language=JavaScript type=text/javascript src="js/calendar.js"></SCRIPT>
<SCRIPT language=JavaScript type=text/javascript src="js/calendar-zh_TW.js"></SCRIPT>
<SCRIPT language=JavaScript type=text/javascript src="js/calendar-setup.js"></SCRIPT>
<SCRIPT language=JavaScript type=text/javascript src="js/selectbox.js"></SCRIPT>
<link rel="stylesheet" type="text/css" href="css/goods.css">

</HEAD>
<header >商品查詢</header>
<BODY><!-- vat JS files -->
	<section id="rauchbier" class="tab-panel container" >
		<div class="w3-card w3-round w3-white div2-center">
			<div id="c_card" class="card pi-draggable " draggable="true">
				<div class="card-header"> 
					<form accept-charset='UTF-8' id='main' method='POST' action="http://10.99.50.147:9090/erp/showFileOnhandImage.jsp" enctype='application/x-www-form-urlencoded' onSubmit=''>
						<p><TD><SPAN style="TEXT-ALIGN: left" id=itemCode class=defaultField name="itemCode" init template mode datatype="LABEL" view>請輸入品號&ensp;</SAPN></SPAN></TD>
						<TD><INPUT style="TEXT-ALIGN: left" id=itemCode class=defaultField alt="" maxLength=20 size=10 name=itemCode init template mode dat
						view bind="itemCode" default_src focusSaved="null" blurSaved="null" itemIndex="1" saveBgColor></TD>
						<TD><INPUT style="TEXT-ALIGN: left" id=brandCode value="T2" class=defaultField alt="" maxLength=20 size=10 name=brandCode init template mode type=hidden 
						view bind="brandCode" default_src focusSaved="null" itemIndex="1" saveBgColor></TD>
						<input type="submit" name="button" value="查詢"  class="button" />
						</p>
					</form>
					<div class="div3"></div>
				</div>
				<div class="frame">
					<img id="c_img" class="img-fluid d-block pi-draggable" src=<%out.println(returnMap.containsKey("img")?returnMap.get("img"):"images/notImg_.jpg");%> 
                          draggable="true">
				</div>
				<div class="div1">
					<p>
						<TD><SPAN style="TEXT-ALIGN: left" id=#L.selectItemBrand class=defaultField name="#L.selectItemBrand" init template mode datatype="LABEL" view>商品品牌&ensp;</SAPN></SPAN></TD>
						<TD><INPUT style="BORDER-BOTTOM: 0px; TEXT-ALIGN: left; BORDER-LEFT: 0px; BORDER-TOP: 0px; BORDER-RIGHT: 0px" id=#F.selectItemBrand class=defaultField 
						value=<%out.println((request.getParameter("itemCode")==null)?"(empty)":item.getItemBrand());%>  
						alt="" maxLength=20 size=12 name=#F.selectItemBrand init template mode datatype="TEXT" view bind="selectItemBrand" default_src focusSaved="null" blurSaved="null"
						itemIndex="5" saveBgColor></TD>
					</p>
					<p>
						<TD><SPAN style="TEXT-ALIGN: left" id=#L.itemCName class=defaultField name="#L.itemCName" init template mode datatype="LABEL" view>品名&ensp;</SAPN></SPAN></TD>
						<TD><INPUT  style="BORDER-BOTTOM: 0px; TEXT-ALIGN: left; BORDER-LEFT: 0px; BORDER-TOP: 0px; BORDER-RIGHT: 0px" id=#F.itemCName class=defaultField 
						value=<%out.println((request.getParameter("itemCode")==null)?"(empty)":item.getItemCName()); %>
						readOnly alt="" maxLength=20 size=12 name=#F.itemCName init template mode="READONLY" datatype="TEXT" view bind="itemName" default_src mode_readOnly="true"
						focusSaved="null" blurSaved="null" itemIndex="6"></TD>
					</p>
					<p>
						<TD><SPAN style="TEXT-ALIGN: left" id=#L.categoryType name="#L.categoryType" init template mode datatype="LABEL" view>業種&ensp;</SAPN></SPAN></TD>
						<TD><INPUT style="BORDER-BOTTOM: 0px; TEXT-ALIGN: left; BORDER-LEFT: 0px; BORDER-TOP: 0px; BORDER-RIGHT: 0px" id=#F.categoryType 
						value=<%out.println((request.getParameter("itemCode")==null) ?"(empty)":item.getCategoryType()); %>  readOnly alt="" 
						maxLength=20 size=12 name=#F.categoryType init template mode="READONLY" datatype="TEXT" view bind="categoryType" default_src mode_readOnly="true"
						 focusSaved="null" blurSaved="null" itemIndex="8"></TD>
					</p>
					<p>
              			<TD><SPAN style="TEXT-ALIGN: left" id=#L.category01 class=defaultField name="#L.category01" init template mode datatype="LABEL" view>大類&ensp;</SAPN></SPAN></TD>
						<TD><INPUT style="BORDER-BOTTOM: 0px; TEXT-ALIGN: left; BORDER-LEFT: 0px; BORDER-TOP: 0px; BORDER-RIGHT: 0px" id=#F.category01 
						value=<%out.println((request.getParameter("itemCode")==null)?"(empty)":item.getCategory01()); %> readOnly alt="" 
						maxLength=20 size=12 name=#F.category01 init template mode="READONLY" datatype="TEXT" view bind="category01" default_src mode_readOnly="true" focusSaved="null" 
						blurSaved="null" itemIndex="10"></TD>
              		</p>
              		<p>
              			<TD><SPAN style="TEXT-ALIGN: left" id=#L.category02 class=defaultField name="#L.category02" init template mode datatype="LABEL" view>中類&ensp;</SAPN></SPAN></TD>
						<TD><INPUT style="BORDER-BOTTOM: 0px; TEXT-ALIGN: left; BORDER-LEFT: 0px; BORDER-TOP: 0px; BORDER-RIGHT: 0px"  id=#F.category02 
						value=<%out.println((request.getParameter("itemCode")==null)?"(empty)":item.getCategory02()); %> readOnly alt="" 
						maxLength=20 size=12 name=#F.category02 init template mode="READONLY" datatype="TEXT" view bind="category02" default_src focusSaved="null" blurSaved="null" 
						itemIndex="11"></TD>
              		</p>
              		<p>
              			<TD><SPAN style="TEXT-ALIGN: left" id=#L.maxPurchaseAmount class=defaultField name="#L.maxPurchaseAmount" init template mode datatype="LABEL" view>價格&ensp;</SAPN></SPAN></TD>
						<TD><INPUT style="BORDER-BOTTOM: 0px; TEXT-ALIGN: left; BORDER-LEFT: 0px; BORDER-TOP: 0px; BORDER-RIGHT: 0px" id=#F.maxPurchaseAmount class=defaultField 
						value=<%out.println((request.getParameter("itemCode")==null)?"(empty)":item.getUnitPrice()); %> 
						alt="" maxLength=20 size=12 name=#F.maxPurchaseAmount init template mode datatype="LABEL" mode="READONLY" view bind="maxPurchaseAmount" default_src focusSaved="null" 
						blurSaved="null" itemIndex="12"></TD>
              		</p>
                </div>
				<div class="div2"></div>
				<div>
					<table id="c_table" class="table pi-draggable" draggable="true">
                      <thead>
                        <tr>
                          <th>#</th>
                          <th>庫存名稱</th>
                          <th>剩餘庫存量</th>
                        </tr>
                      </thead>
                      <tbody id="onhandsQtyBlock" name = "onhandsQtyBlock">
						<% int onhandCount = 0; %>
							<% for(ImItemOnHandView itemOnHand:returnOnhandResult){%>
								<tr>
									<td><%out.println(onhandCount); %></td>
									<td><%out.println(itemOnHand.getWarehouseName()); %></td>
									<td><%out.println(itemOnHand.getCurrentOnHandQty()); %></td>
								</tr>
								<%  onhandCount++; %>
						<%  } %>
                        <!--  <tr>
                          <td>1</td>
                          <td>中山特賣</td>
                          <td>10</td>
                        </tr>
                        <tr>
                          <td>2</td>
                          <td>南闊特賣</td>
                          <td>5</td>
                        </tr>
                        <tr>
                          <td>3</td>
                          <td>總公司</td>
                          <td>4</td>  -->
                      </tbody>
                    </table>
                </div>
			</div>
		</div>	
	</section>
</BODY>
</HTML>