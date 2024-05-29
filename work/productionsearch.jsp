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
//parameters init
//String itemCode;
String brandCode="";
String categoryType="";
String loginUser="";
ImItem item = null;
List<ImItemOnHandView> returnOnhandResult = new ArrayList(0);
List<Map> resultLists = null;
List<Map> promoteList = null;
ImItemService imItemService = (ImItemService)SpringUtils.getApplicationContext().getBean("imItemService");
ImMovementMainService imMovementMainService = (ImMovementMainService)SpringUtils.getApplicationContext().getBean("imMovementMainService");
try{
	brandCode = request.getParameter("brandCode");
	categoryType = request.getParameter("categoryType");
	loginUser = request.getParameter("login");
	System.out.println("loginUser:"+loginUser);
}catch(Exception exr){
	exr.printStackTrace();
	%>
	<script language=JavaScript>
      let url = "http://60.250.137.187:58083/erp_dev_20210702/errorpage.jsp";
      window.location.href = url;
    </script>
	<%
}
%>
<script language=JavaScript>
function getRequests() {
    var s1 = location.search.substring(1, location.search.length).split('&'),
        r = {}, s2, i;
    for (i = 0; i < s1.length; i += 1) {
        s2 = s1[i].split('=');
        r[decodeURIComponent(s2[0]).toLowerCase()] = decodeURIComponent(s2[1]);
    }
    return r;
};
var QueryString = getRequests();


function showPrice(id){
	var priceHidden = document.getElementById("price-hidden-"+id);
	var priceShow = document.getElementById("price-show-"+id);
	var it = document.getElementById("itemCode-"+id);
	var login = document.getElementById("userLogin");
	if(priceHidden.style.display != "none" ) {
		priceHidden.style.display = "none";
	}
	if(priceShow.style.display == "none" ) {
		priceShow.style.display = "inline";
	}
	sendLog('click_price',it);
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

window.onbeforeunload = function (){
	var user = document.getElementById("userLogin");
	sendLog('stay_in',user);
	sendLog('leave',user);
	return null;
}
</SCRIPT>
<%
try{
	if( brandCode!=null && categoryType!=null ){
		resultLists = imItemService.getAJAXCategoryTypeInfomation(brandCode, categoryType,loginUser);//itemCode,src,itemName,unitPrice
		if(null!=resultLists){
			System.out.println("resultLists:"+resultLists);
			for(Map itemMap:resultLists){
				 String categoryUrl = "http://60.250.137.187:58083/erp_dev_20210702/productionsearch.jsp?brandCode="+
	                		brandCode+"&categoryType="+itemMap.get("categoryCode")+"&login="+loginUser;
	                	 if(null == promoteList || null== categoryType){
	                		 promoteList = (List)itemMap.get("promoteList");
	                	 }
	                	 if(itemMap.get("categoryCode") == categoryType){
	                		 promoteList = (List)itemMap.get("promoteList");
	                	 }
	                	 System.out.println("promoteList:"+promoteList.size());
			}
		}
		%>
		<script language=JavaScript>
		function sendLog(type,it){
			var httpRequest = new XMLHttpRequest();
			var user = document.getElementById("userLogin");
			var caType = document.getElementById("categoryType");
			var logIt = "";
			console.log(typeof(it));
			if(typeof(it)=="object"){
				logIt = it.value;
			}else if(typeof(it)=="string"){
				logIt = it;
			}
			console.log("saveLog.jsp?login="+user.value+"&functionCode="+type+"&itemCode="+logIt+"&categoryType="+caType.value);
			httpRequest.open("POST", "saveLog.jsp?login="+user.value+"&functionCode="+type+"&itemCode="+logIt+"&categoryType="+caType.value);
		    httpRequest.send();
		}
		function contectNsaveLog(type,it){
			alert("專員將會於24小時內與您聯繫，謝謝！");
			sendLog('contect_customs',it);
		}
	    </script>
		<%
	}
	%>
    <meta name="generator" content="HTML Tidy for HTML5 for Windows version 5.5.24" />
    <title>production_search</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" type="text/css" href="css/product.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
body,h1,h2,h3,h4,h5 {font-family: &quot;Raleway&quot;, sans-serif}
</style>
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
                	 String categoryUrl = "http://60.250.137.187:58083/erp_dev_20210702/productionsearch.jsp?brandCode="+
                		brandCode+"&categoryType="+category.get("categoryCode")+"&login="+loginUser;
                	 if(null == promoteList || null== categoryType){
                		 promoteList = (List)category.get("promoteList");
                	 }
                	 if(category.get("categoryCode") == categoryType){
                		 promoteList = (List)category.get("promoteList");
                	 }
                	 System.out.println("promoteList:"+promoteList.size());
                %>
                <li class="menu-item">
                 		<a class="menu-link" href="<%out.print(categoryUrl);%>" onclick="javascript:sendLog('click_category',this);" ><%out.print(category.get("categoryName")); %></a>
                   <!-- <a class="menu-link" href="#"><%out.print(category.get("categoryName")); %></a> -->
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
        <div class="h4 product_title product-card__title">猜你喜歡</div>
      </nav>
      <%
      if(null != promoteList && promoteList.size()>0){
      %>
      <div class="filters-toolbar__item filters-toolbar__item--count">
      	<input id="userLogin" type="text" class="fadeIn second" name="userLogin" value=<%out.print(loginUser);%> style="display: none;"/>
      	<input id="categoryType" type="text" class="fadeIn second" name="categoryType" value=<%out.print(categoryType);%> style="display: none;"/>
        <span class="filters-toolbar__product-count">
      </div>
	  <% 
		int rowCount=0;
		for(Map itemMap:promoteList){
			if((rowCount%2)==0){
	  %>
	  	 <div class="row">
	        <div class="column">
	          <div class="like-product"><i class="fa fa-heart-o" style="color:red;font-size:24px" id=<%out.println(itemMap.get("itemCode"));%> ></i></div>
	          <img id=<%out.print("ProductCardImage-collection-template-"+rowCount);%>
	          class="grid-view-item__image lazyautosizes ls-is-cached lazyloaded" alt=""
	          data-widths="[180, 360, 540, 720, 900, 1080, 1296, 1512, 1728, 2048]" data-aspectratio="1.7775" data-sizes="auto"
	          data-image="" sizes="286.1775px" src=<%out.print(itemMap.get("src"));%> />
	          <div class="h4 grid-view-item__title product-card__title" aria-hidden="true"><%out.print(itemMap.get("itemName"));%></div>
	          <img id="alarm" class="grid-view-item__image lazyautosizes ls-is-cached lazyloaded" alt="" 
	          style="height: auto;" data-widths="[180, 360, 540, 720, 900, 1080, 1296, 1512, 1728, 2048]" data-aspectratio="1.7775" data-sizes="auto"
	          data-image="" sizes="286.1775px" src="images/wine_alarm.jpeg" />
	          <dl style="text-align: center;">
	            	<span style="text-align: center;" id=<%out.print("price-hidden-"+rowCount);%> class="price-item" onclick="JavaScript:showPrice('<%out.print(rowCount);%>');" 
	            	 onmouseout="JavaScript:hiddenPrice('<%out.print(rowCount); %>');" >點我看優惠</span>
	              	<span id=<%out.print("price-show-"+rowCount);%> class="price-item" style="display: none;" >$<%out.print(itemMap.get("unitPrice")+"起");%></span>
	              	<input id=<%out.print("itemCode-"+rowCount);%> type="text" class="fadeIn second" name="itemCode" value=<%out.print(itemMap.get("itemCode"));%> style="display: none;"/>
	          </dl>
	        </div>
	  <%
			}else{
	  %>
	        <div class="column">
	          <div class="like-product"><i class="fa fa-heart-o" style="color:red;font-size:24px" id=<%out.println(itemMap.get("itemCode"));%>></i></div>
	          <img id=<%out.print("ProductCardImage-collection-template-"+rowCount);%>
	          class="grid-view-item__image lazyautosizes ls-is-cached lazyloaded" alt=""
	          data-widths="[180, 360, 540, 720, 900, 1080, 1296, 1512, 1728, 2048]" data-aspectratio="1.7775" data-sizes="auto"
	          data-image="" sizes="286.1775px" src=<%out.print(itemMap.get("src"));%> />
	          <div class="h4 grid-view-item__title product-card__title" aria-hidden="true"><%out.print(itemMap.get("itemName"));%></div>
	          <img id="alarm" class="grid-view-item__image lazyautosizes ls-is-cached lazyloaded" alt="" data-widths="[180, 360, 540, 720, 900, 1080, 1296, 1512, 1728, 2048]" data-aspectratio="1.7775" data-sizes="auto"
	           style="height: auto;"  data-image="" sizes="286.1775px" src="images/wine_alarm.jpeg" />
	          <dl style="text-align: center;">
	            <span id=<%out.print("price-hidden-"+rowCount);%> class="price-item" onclick="JavaScript:showPrice('<%out.print(rowCount);%>');" 
	            	 onmouseout="JavaScript:hiddenPrice('<%out.print(rowCount);%>');" >點我看優惠</span>
	            <span id=<%out.print("price-show-"+rowCount);%> class="price-item" style="display: none;" >$<%out.print(itemMap.get("unitPrice")+"起");%></span>
	          	<input id=<%out.print("itemCode-"+rowCount);%> type="text" class="fadeIn second" name="itemCode" value=<%out.print(itemMap.get("itemCode"));%> style="display: none;"/>
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
	  <div><img id=<%out.print(loginUser); %> class="grid-view-item__image lazyautosizes ls-is-cached lazyloaded" alt="" data-widths="[180, 360, 540, 720, 900, 1080, 1296, 1512, 1728, 2048]" data-aspectratio="1.7775" data-sizes="auto"
	           style="height: auto;width: 40%;"  data-image="" sizes="286.1775px" src="images/contectMe.png" onclick="contectNsaveLog('contect_customs',this.id);" /></div>
      <!-- Footer -->
      <footer class="w3-container w3-dark-grey w3-padding-32 w3-margin-top">
      <!-- <button class="w3-button w3-black w3-disabled w3-padding-large w3-margin-bottom">Previous</button>  -->
      <!-- <button class="w3-button w3-black w3-padding-large w3-margin-bottom" onclick="javascript:sendLog('next_page',this);">Next »</button> -->
      <p>Powered by 
      <a href="https://dutyfree.tasameng.com.tw/" target="_blank">tasameng</a></p></footer>
    </div>
    <script language=JavaScript>
   		 var user = document.getElementById("userLogin");
    	sendLog("login",user);
    	var ele;
    	var body = document.getElementsByTagName("body")[0];
    	
    	body.addEventListener('click', function(event){
    		let element = event.target;
    		if(element.className.indexOf('fa-heart-o') > 0){
    			element.classList.remove('fa-heart-o');
    			element.classList.add('fa-heart');
    			ele = element;
    			sendLog("favorate",element.id);
    		}else if(element.className.indexOf('fa-heart') > 0){
    			element.classList.remove('fa-heart');
    			element.classList.add('fa-heart-o');
    			ele = element;
    			sendLog("unfavorate",element.id);
    		}
    	});
    </script>
    
  </body>
</html>
<%
}catch(Exception ex){
	%>
	<script language=JavaScript>
      let url = "http://60.250.137.187:58083/erp_dev_20210702/errorpage.jsp";
      window.location.href = url;
    </script>
	<%
}
%>



