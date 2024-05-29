<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.ImItemOnHandView" %>
<%@page import="tw.com.tm.erp.hbm.bean.ImItem" %>
<!DOCTYPE html>
<html>
  <head>
<%
//String itemCode;
String brandCode="";
String categoryType="";
String loginUser="";
ImItem item = null;
List<ImItemOnHandView> returnOnhandResult = new ArrayList(0);
List<Map> resultLists = null;
List<Map> promoteList = null;
try{
	ImItemService imItemService = (ImItemService)SpringUtils.getApplicationContext().getBean("imItemService");
	ImMovementMainService imMovementMainService = (ImMovementMainService)SpringUtils.getApplicationContext().getBean("imMovementMainService");
	brandCode = request.getParameter("brandCode");
	categoryType = request.getParameter("categoryType");
	loginUser = request.getParameter("login");
	System.out.println("loginUser:"+loginUser);
	if( brandCode!=null && categoryType!=null ){
		resultLists = imItemService.getAJAXCategoryTypeInfomation(brandCode, categoryType,loginUser);//itemCode,src,itemName,unitPrice
		if(null!=resultLists){
			System.out.println("resultLists:"+resultLists);
			for(Map itemMap:resultLists){
				System.out.println(itemMap.get("promoteList"));
				/*System.out.println(itemMap.get("itemCode"));
				System.out.println(itemMap.get("itemName"));
				System.out.println(itemMap.get("src"));
				System.out.println(itemMap.get("unitPrice"));*/
			}
		}
	}
}catch(Exception ex){
	%>
	<script language=JavaScript>
      let url = "http://60.250.137.187:58083/erp_dev_20210702/errorpage.jsp";
      window.location.href = url;
    </script>
	<%
}
%>
    <meta name="generator" content="HTML Tidy for HTML5 for Windows version 5.5.24" />
    <title>production_search</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" type="text/css" href="css/product.css" />
<style>
body,h1,h2,h3,h4,h5 {font-family: &quot;Raleway&quot;, sans-serif}
</style>
<script language=JavaScript>
function showPrice(id){
	var priceHidden = document.getElementById("price-hidden-"+id);
	var priceShow = document.getElementById("price-show-"+id);
	var it = document.getElementById("itemCode-"+id);
	var login = document.getElementById("userLogin");
	if(priceHidden.style.display == "inline" ) {
		priceHidden.style.display = "none";
	}
	if(priceShow.style.display == "none" ) {
		priceShow.style.display = "inline";
	}
	var httpRequest = new XMLHttpRequest();
	httpRequest.open('POST', 'saveLog.jsp?login='+login.value+"&functionCode=click&itemCode="+it.value);
    httpRequest.send();
    var millisecondsToWait = 1000;
    setTimeout(function() {
    	hiddenPrice(id);
    }, millisecondsToWait);
    
}
function hiddenPrice(id){
	var priceHidden = document.getElementById("price-hidden-"+id);
	var priceShow = document.getElementById("price-show-"+id);
	priceHidden.style.display = "inline";
	priceShow.style.display = "none";
	
}
</SCRIPT>
  </head>
  <body class="w3-light-grey">
    <div class="w3-content" style="max-width:1400px">
      <!-- Header -->
      <header class="w3-container w3-padding-32">
        <div class="navbar" style="color:#D68B00;border:2px #ccc solid;padding:10px;">
          <div class="mydiv">
            <input id="nav-toggle" type="checkbox" />
            <label for="nav-toggle" class="ham">
	            <div class="ham-origin">
	              <div class="ham-bar ham-bar--top"></div>
	              <div class="ham-bar ham-bar--middle"></div>
	              <div class="ham-bar ham-bar--bottom"></div>
	            </div>
            </label>
            <label for="nav-toggle" class="ham-bg"></label>
            <nav class="menu">
              <ul class="menu-list">
                <%  
                for(Map category:resultLists){
                	 String categoryUrl = "http://localhost:8080/erp_dev_20210702/productionsearch.jsp?brandCode="+
                		brandCode+"&categoryType"+category.get("categoryCode")+"&login="+loginUser;
                	 if(null == promoteList || null== categoryType){
                		 promoteList = (List)category.get("promoteList");
                	 }
                	 if(category.get("categoryCode") == categoryType){
                		 promoteList = (List)category.get("promoteList");
                	 }
                	 System.out.println("promoteList:"+promoteList.size());
                %>
                <li class="menu-item">
                  <a class="menu-link" href="<%out.print(categoryUrl);%>"><%out.print(category.get("categoryName")); %></a>
                </li>
                <%} %>
              </ul>
            </nav>
          </div>
          <div class="mydiv">
            <h1>
              <b>MY Product List</b>
            </h1>
          </div>
        </div>
      </header>
      <nav class="breadcrumb">
        <ul>
          <li>
          <a href="http://60.250.137.187:58083/erp_dev_20210702/errorpage.jsp" title="首頁">首頁</a> 
          <svg class="svg-icon" width="6" height="9.33333336" viewbox="0 0 9 14" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M1.5 1l6 6-6 6" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round">
            </path>
          </svg>
		  </li>
          <li>威士忌系列</li>
        </ul>
      </nav>
      <%
      if(null != promoteList && promoteList.size()>0){
      %>
      <div class="filters-toolbar__item filters-toolbar__item--count">
      	<input id="userLogin" type="text" class="fadeIn second" name="userLogin" value=<%out.print(loginUser);%> style="display: none;"/>
        <span class="filters-toolbar__product-count">
        <span class="filters-toolbar__product-count-num"><%out.print(promoteList.size()+" 項產品"); %></span></span>
      </div>
	  <% 
		int rowCount=0;
		for(Map itemMap:promoteList){
			if((rowCount%2)==0){
	  %>
	  	 <div class="row">
	        <div class="column">
	          <img id=<%out.print("ProductCardImage-collection-template-"+rowCount);%>
	          class="grid-view-item__image lazyautosizes ls-is-cached lazyloaded" alt=""
	          data-widths="[180, 360, 540, 720, 900, 1080, 1296, 1512, 1728, 2048]" data-aspectratio="1.7775" data-sizes="auto"
	          data-image="" sizes="286.1775px" src=<%out.print(itemMap.get("src"));%> />
	          <div class="h4 grid-view-item__title product-card__title" aria-hidden="true"><%out.print(itemMap.get("itemName"));%></div>
	          <dl>
	            <dd>
	            	<p><span id=<%out.print("price-hidden-"+rowCount);%> class="price-item" onclick="JavaScript:showPrice('<%out.print(rowCount);%>');" 
	            	 onmouseout="JavaScript:hiddenPrice('<%out.print(rowCount); %>');" >點我看優惠</span></p>
	              	<span id=<%out.print("price-show-"+rowCount);%> class="price-item" style="display: none;" >$<%out.print(itemMap.get("unitPrice")+"起");%></span>
	              	<input id=<%out.print("itemCode-"+rowCount);%> type="text" class="fadeIn second" name="itemCode" value=<%out.print(itemMap.get("itemCode"));%> style="display: none;"/>
	            </dd>
	          </dl>
	        </div>
	  <%
			}else{
	  %>
	        <div class="column">
	          <img id=<%out.print("ProductCardImage-collection-template-"+rowCount);%>
	          class="grid-view-item__image lazyautosizes ls-is-cached lazyloaded" alt=""
	          data-widths="[180, 360, 540, 720, 900, 1080, 1296, 1512, 1728, 2048]" data-aspectratio="1.7775" data-sizes="auto"
	          data-image="" sizes="286.1775px" src=<%out.print(itemMap.get("src"));%> />
	          <div class="h4 grid-view-item__title product-card__title" aria-hidden="true"><%out.print(itemMap.get("itemName"));%></div>
	          <dl>
	            <dd>
	            	<p><span id=<%out.print("price-hidden-"+rowCount);%> class="price-item" onclick="JavaScript:showPrice('<%out.print(rowCount);%>');" 
	            	 onmouseout="JavaScript:hiddenPrice('<%out.print(rowCount);%>');" >點我看優惠</span></p>
	              	<span id=<%out.print("price-show-"+rowCount);%> class="price-item" style="display: none;" >$<%out.print(itemMap.get("unitPrice")+"起");%></span>
	              	<input id=<%out.print("itemCode-"+rowCount);%> type="text" class="fadeIn second" name="itemCode" value=<%out.print(itemMap.get("itemCode"));%> style="display: none;"/>
	            </dd>
	          </dl>
	        </div>
		</div>
	  <%
			}
			rowCount++;
		}
      }else{
	  %>
	  <div class="filters-toolbar__item filters-toolbar__item--count">
        <span class="filters-toolbar__product-count">
        <span class="filters-toolbar__product-count-num"> 暫無相關產品</span></span>
      </div>
	  <%} %>
      <!-- Footer -->
      <footer class="w3-container w3-dark-grey w3-padding-32 w3-margin-top">
      <button class="w3-button w3-black w3-disabled w3-padding-large w3-margin-bottom">Previous</button> 
      <button class="w3-button w3-black w3-padding-large w3-margin-bottom">Next »</button>
      <p>Powered by 
      <a href="https://dutyfree.tasameng.com.tw/" target="_blank">tasameng</a></p></footer>
    </div>
  </body>
</html>



