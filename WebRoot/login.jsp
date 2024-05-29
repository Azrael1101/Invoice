<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%
java.util.Locale locale = request.getLocale();
java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ApplicationResources", locale);
boolean multiple = false;
//com.tbcn.ceap.party.ejb.PartySB partyAdmin = com.tbcn.ceap.party.ejb.ServiceLocator.getPartySBHome().create();
//com.tbcn.ceap.workflow.client.ejb.WorkflowClientSB clientSB = com.tbcn.ceap.workflow.client.ejb.ServiceLocator.getWorkflowClientSBHome().create();
//com.tbcn.ceap.party.HRDomain [] domains = partyAdmin.findAllHRDomains();

//if(!clientSB.isUniqueUsernameEnabled() && domains.length != 1)
//{
//     multiple = true;
//}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>KWE</title>
<style type="text/css">
	<!--
	@import url("table.css");
	body {
		background-color: #000000;
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}	
	-->
	div#nifty{ margin: 0 30px;background: #9BD1FA;}
	b.rtop, b.rbottom{display:block;background: #FFF}
	b.rtop b, b.rbottom b{display:block;height: 1px;overflow: hidden; background: #9BD1FA}
	b.r1{margin: 0 5px}
	b.r2{margin: 0 3px}
	b.r3{margin: 0 2px}
	b.rtop b.r4, b.rbottom b.r4{margin: 0 1px;height: 2px}
</style>
<link href="table.css" rel="stylesheet" type="text/css" />
<link href="css/css.css" rel="stylesheet" type="text/css" />

<script src="js/AC_RunActiveContent.js" type="text/javascript"></script>
<script src="js/vat.js" type="text/javascript"></script>
<script src="js/vat-block.js" type="text/javascript"></script>

<script language=JavaScript>
    var currentUrl = top.window.location.href;
	function login_onload(){
	    var requestUrl=getCookie('requestUrl');
	    if(requestUrl == null || requestUrl == "" || currentUrl.indexOf("&redirect=Y")!=-1){
	        setCookie('requestUrl', currentUrl, 1);
	    }
		var username=getCookie('username');
		if (username!=null && username!=""){
			 document.login_form.username.value=username;
		}
		makeRequest('brandServlet', vfunc = function(){
			var userbrnad=getCookie('userbrand');
			if (userbrnad!=null && userbrnad!=""){
				vat.item.setValue(document.brand_form.user_brand, userbrnad);
  		};
		});	 
	}
	document.onkeydown = keyDown;
	function keyDown(e){ 
		if(event.keyCode === 13){ 
			login();
			event.returnValue = false;        
			event.cancel = true;      
		}
	}
	document.onkeypress = function(e){
		if (event.keyCode >= 97 && event.keyCode <= 122){
			event.keyCode -= 32;	// 'a'~'z'
		}
	};
	function MM_swapImgRestore(){ //v3.0
  	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
	}
	function MM_preloadImages(){ //v3.0
		var d=document; 
		if(d.images){
			if(!d.MM_p) d.MM_p=new Array();
			var i,j=d.MM_p.length,a=MM_preloadImages.arguments; 
			for(i=0; i<a.length; i++)
				if (a[i].indexOf("#")!=0){
					d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];
				}
		}
	}
	function MM_findObj(n, d){ //v4.01
  	var p,i,x;  
  	if(!d) d=document; 
  	if((p=n.indexOf("?"))>0&&parent.frames.length){
    	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);
    }
  	if(!(x=d[n])&&d.all) x=d.all[n];
  	for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  	for (i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  	if (!x && d.getElementById) x=d.getElementById(n); 
  	return x;
	}
	function MM_swapImage(){ //v3.0
		var i,j=0,x,a=MM_swapImage.arguments; 
		document.MM_sr=new Array; 
		for(i=0;i<(a.length-2);i+=3)
			if ((x=MM_findObj(a[i]))!=null){
				document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];
			}
	}
	
  function makeRequest(url, pfunc){
		var size=document.brand_form.user_brand.length;
		for(var i=0;i<size;i++){
			document.brand_form.user_brand.options.remove(0);		 
		}
		var http_request = false;
		if(window.XMLHttpRequest){ // Mozilla, Safari,...
			http_request = new XMLHttpRequest();
		}else if (window.ActiveXObject){ // IE
			try{
				http_request = new ActiveXObject("Msxml2.XMLHTTP");
			}catch(e){
				try {
					http_request = new ActiveXObject("Microsoft.XMLHTTP");
				}catch(e){}
			}
		}
		if(!http_request){
			alert('Giving up :( Cannot create an XMLHTTP instance');
			return false;
		}
    // 定義事件處理函數為 alterContents()
		http_request.onreadystatechange = function(){
			if (http_request.readyState == 4){
					if (http_request.status == 200){
					var vnP1 = http_request.responseText.indexOf("<|") + 2;
					var vnP2 = http_request.responseText.indexOf("|>");
					var vsHrDomain = http_request.responseText.substring(vnP1, vnP2);
					if (vsHrDomain !== "null"){
						document.login_form.hrDomain.value = "@"+vsHrDomain;
					}	
					var mesg = http_request.responseText.substr(vnP2 + 2);
					var mesgAry = mesg.split("@@");
					for(var i=0;i<mesgAry.length;i++){
						document.brand_form.user_brand.options.add(new Option(mesgAry[i], mesgAry[i]));
					}
					var size=document.brand_form.user_brand.length;
					document.brand_form.user_brand.options.remove(size-1);
					vat.$jsr(pfunc);
				}else{
					// alert('There was a problem with the request.');
				}
			}
		};
    // IE 6.x 和 Firefox 1.5.x 皆要 encodeURI()
		document.login_form.username.value=document.login_form.username.value.toUpperCase() ;
		url = url + "?name=" + encodeURIComponent(document.login_form.username.value);
		http_request.open('GET', url, true);
		http_request.send(null);
	}

	function alertContents(http_request) {
	}
	
	function makeSession(url){
		var http_request = false;
		if(window.XMLHttpRequest){ // Mozilla, Safari,...
			http_request = new XMLHttpRequest();
		}else if(window.ActiveXObject){ // IE
			try{
				http_request = new ActiveXObject("Msxml2.XMLHTTP");
			}catch (e){
				try{
					http_request = new ActiveXObject("Microsoft.XMLHTTP");
				}catch(e){}
			}
		}
		if (!http_request){
			alert('Giving up :( Cannot create an XMLHTTP instance');
			return false;
		}
		// http_request.onreadystatechange = function() { alertMsgs(http_request); };
		// IE 6.x 和 Firefox 1.5.x 皆要 encodeURI()
		url = url + "?user_name=" + encodeURIComponent(document.brand_form.user_name.value) + "&user_brand=" + encodeURIComponent(document.brand_form.user_brand.value) + "&user_domain=" + encodeURIComponent(document.brand_form.user_domain.value);
		http_request.open('GET', url, true);
		http_request.send(null);
	}
  
	function login(){
		if ( document.login_form.username.value !== ""){
			setCookie('username',  document.login_form.username.value, 365);
    }
    var vsUserBrand = vat.item.getValue(document.brand_form.user_brand);
    if (typeof vsUserBrand === "string" && vsUserBrand !== ""  && vsUserBrand !== null){
    	setCookie('userbrand', vsUserBrand, 365);
    }
<%	if(multiple){ %>
			document.login_form.j_username.value = document.login_form.username.value + login_form.hrDomain.value;
			if(login_form.hrDomain.value==""){
				document.brand_form.user_domain.value ="@default";
			}else{
				document.brand_form.user_domain.value =login_form.hrDomain.value;
			}
<%	}else{	%>
			document.login_form.j_username.value = document.login_form.username.value;
			document.brand_form.user_domain.value ="default";
<%	}	%>	
		document.brand_form.user_name.value = document.login_form.username.value;
		makeSession("temp.jsp");
		document.login_form.action="j_security_check";	
		document.login_form.submit();
	}
	  
	function getCookie(c_name){
		if (document.cookie.length>0){
  		c_start=document.cookie.indexOf(c_name + "=");
  		if (c_start!=-1){ 
    		c_start=c_start + c_name.length+1 ;
    		c_end=document.cookie.indexOf(";",c_start);
    		if (c_end==-1) c_end=document.cookie.length
    		return unescape(document.cookie.substring(c_start,c_end));
    	} 
  	}
		return ""
	}

	function setCookie(c_name,value,expiredays){
		var exdate=new Date();
		exdate.setDate(exdate.getDate()+expiredays);
		document.cookie=c_name+ "=" +escape(value)+((expiredays==null) ? "" : "; expires="+exdate.toGMTString());
	}
</script> 
</head>

<body onload="login_onload()">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td align="center" valign="middle" scope="col">

		<table width="493" border="0" cellspacing="0" cellpadding="0">
			<tr>
			<td width="493" height="360" align="center" background="images/index_03.jpg">
			<br/>
			<img width="400" src="images/index.jpg"/>
			</td>
			</tr>
			<tr>
			<td width="493" height="229" align="center" background="images/index_05.jpg">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td width="10%" height="120" align="right" valign="bottom" scope="col">&nbsp;</td>
				<td width="80%" height="120" align="right" valign="bottom" scope="col">
					<form name="login_form" action="" method="post">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="30%" height="30" class="buttons" align="right"><div align="right" class="style1">帳號</div></td>
						<td width="10%" height="20" class="buttons" align="right">&nbsp;</td>
						<td width="60%" height="20" class="buttons" align="left">
							<input name="username" value="" type="text" id="textfield" size="20" onchange="makeRequest('brandServlet')"/>
							<input type="hidden" name="j_username" value = "" />
						</td>
					</tr>
					<tr>
						<td width="30%" height="30" class="buttons" align="right"><div align="right" class="style1">密碼</div></td>
						<td width="10%" height="20" class="buttons" align="right">&nbsp;</td>
						<td width="60%" height="20" class="buttons" align="left">
							<input name="j_password" type="password" id="textfield2" size="20" value=""/>
						</td>
					</tr>
					<tr>
						<td width="30%" height="30" class="buttons" align="right"><div align="right" class="style1"><!-- Domain --></div></td>
						<td width="10%" height="20" class="buttons" align="right">&nbsp;</td>
						<td width="60%" height="20" class="buttons" align="right">
							<input name="hrDomain" type="text" size="10" value="" readonly="readonly" style="display:none"/>
							
						</td>
					</tr>
					</table>
					</form>

					<form name="brand_form" action="" method="post">					
						<table width="100%" border="0" cellspacing="0" cellpadding="0">					
						<tr>
							<td width="30%" height="30" align="right" class="buttons"><div align="right" class="style1">品牌</div></td>
							<td width="10%" height="20" align="right" class="buttons">&nbsp;</td>
							<td width="60%" height="20" align="left"  class="buttons">
								<select name="user_brand"></select>
								<input type="hidden" name="user_name" 	value = "" />
								<input type="hidden" name="user_domain" value = "" />
							</td>
						</tr>
						</table>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">					
						<tr>
							<td width="60%" height="30" class="buttons">&nbsp;</td>
							<td width="30%"  height="20" align="right" valign="middle" class="buttons">
								<a onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','images/enter02.jpg',1)">
								<img src="images/enter01.jpg" name="Image1" width="62" height="23" border="0" id="Image1" onclick="javascript:login();"/>
								</a>
							</td>
							<td width="10%" height="20" align="right" class="buttons">&nbsp;</td>							
						</tr>
						</table>
					</form>					
				</td>
				<td width="10%" height="120" align="right" valign="bottom" scope="col">&nbsp;</td>
				</tr>
				</table>
			</td>
      </tr>

      <tr>
        <td height="20" align="center"><span class="gray style2">采盟股份有限公司 版權所有<br />
          c 2008 Tasa Meng Corperation. All Rights Reserved</span></td>
			</tr>
		</table>
	
	</td>
	</tr>
	</table>
	
	<script language=JavaScript>
	// makeRequest('brandServlet');	//** modify by Mac for auto-login
	</script>
</body>
</html>
