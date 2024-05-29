<!-- DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" import="tw.com.tm.erp.hbm.bean.BuCommonPhraseHead,tw.com.tm.erp.hbm.dao.BuCommonPhraseHeadDAO,tw.com.tm.erp.utils.DES,java.net.InetAddress,java.util.Date,tw.com.tm.erp.utils.User,org.springframework.util.StringUtils,tw.com.tm.erp.hbm.dao.SiMenuLogDAO,tw.com.tm.erp.hbm.bean.SiMenuLog"  import="tw.com.tm.erp.hbm.SpringUtils" import="org.springframework.context.ApplicationContext" import="java.util.Enumeration" pageEncoding="utf-8"%>
<html><head> 
<title>MenuLog</title> 
<style>
		*{
			margin: 0 ;
			padding: 0 ;
		}
		
		body{
			background: #f0f0f0;
		  color: #555555;
		  font-family: Georgia,Arial,sans-serif;
		  font-size: 15px;
		
		}	
		
		a {
		  color: #646D8B;
			text-decoration: none;	
			outline:none;
			blr: expression(this.onFocus=this.blur());	
		}
		
		a:hover {
			text-decoration: none;
			outline:none;
			blr: expression(this.onFocus=this.blur());	
		}
		
		ol{
			list-style: none outside none;
		}
		
		ul ,li{
		  margin: 0;  
		  list-style: none;
		}
		
		img {
			border: 0 none;
		}
		
		/** wrapper **/
		#wrapper{	
			display: block;
			margin: 10px auto;
		}
		
		#item{
			width: 50%;
			margin: 0 auto;
		}
		
		#item:after{
			content: "";
			clear: both;
			display: block;
		}
		
		
		#employee-wrapper{
			margin: auto 10px;	
		 	margin-bottom: 15px;
		}
		
		#employee-wrapper h3{
			background: rgb(239,197,202); /* Old browsers */
			background: -moz-linear-gradient(top,  rgba(239,197,202,1) 0%, rgba(186,39,55,1) 81%, rgba(186,39,55,1) 81%); /* FF3.6+ */
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(239,197,202,1)), color-stop(81%,rgba(186,39,55,1)), color-stop(81%,rgba(186,39,55,1))); /* Chrome,Safari4+ */
			background: -webkit-linear-gradient(top,  rgba(239,197,202,1) 0%,rgba(186,39,55,1) 81%,rgba(186,39,55,1) 81%); /* Chrome10+,Safari5.1+ */
			background: -o-linear-gradient(top,  rgba(239,197,202,1) 0%,rgba(186,39,55,1) 81%,rgba(186,39,55,1) 81%); /* Opera 11.10+ */
			background: -ms-linear-gradient(top,  rgba(239,197,202,1) 0%,rgba(186,39,55,1) 81%,rgba(186,39,55,1) 81%); /* IE10+ */
			background: linear-gradient(to bottom,  rgba(239,197,202,1) 0%,rgba(186,39,55,1) 81%,rgba(186,39,55,1) 81%); /* W3C */
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#efc5ca', endColorstr='#ba2737',GradientType=0 ); /* IE6-9 */
			
			text-align: center;
			margin: auto 10px;
			color: #f0f0f0;
			-moz-box-shadow: 0px 1px 10px #c1bfea; 
			-webkit-box-shadow: 0px 1px 10px #c1bfea; 
			box-shadow: 0px 1px 10px #c1bfea;
			border-radius: 1em;  		
		}	

		#form-wrapper {
			background: rgb(214,219,191); /* Old browsers */
			background: -moz-linear-gradient(top,  rgba(214,219,191,1) 0%, rgba(254,255,232,1) 100%); /* FF3.6+ */
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(214,219,191,1)), color-stop(100%,rgba(254,255,232,1))); /* Chrome,Safari4+ */
			background: -webkit-linear-gradient(top,  rgba(214,219,191,1) 0%,rgba(254,255,232,1) 100%); /* Chrome10+,Safari5.1+ */
			background: -o-linear-gradient(top,  rgba(214,219,191,1) 0%,rgba(254,255,232,1) 100%); /* Opera 11.10+ */
			background: -ms-linear-gradient(top,  rgba(214,219,191,1) 0%,rgba(254,255,232,1) 100%); /* IE10+ */
			background: linear-gradient(to bottom,  rgba(214,219,191,1) 0%,rgba(254,255,232,1) 100%); /* W3C */
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#d6dbbf', endColorstr='#feffe8',GradientType=0 ); /* IE6-9 */
			font-family: Arial, Helvetica, sans-serif;
						
			margin: auto 20px;	
			letter-spacing: 2.5pt;
			line-height: 1.8em;
			
			-moz-box-shadow: 0px 1px 10px #b3bead; 
			-webkit-box-shadow: 0px 1px 10px #b3bead; 
			box-shadow: 0px 1px 10px #b3bead; 	
		}	

		#form-wrapper ul{
			margin: 0 auto;
			margin-top: 10px;
			width: 250px;	 	
		}
		#num,#psd{
			width: 150px;	
			background:transparent;			
		}

		#send{
			width: 50px;
			background:transparent;	
			float: right;
			margin-right: 40px;	
			margin-bottom: 10px;
			outline: none;
			behavior:expression(this.onFocus=this.blur()); 
		}
								
		/** footer **/
		#colophon{
		 	border-top: 5px solid #28343b;	 
		}
		
		#footer-bottom{
			background: rgb(69,72,77); /* Old browsers */
			background: -moz-linear-gradient(top,  rgba(69,72,77,1) 0%, rgba(0,0,0,1) 100%); /* FF3.6+ */
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(69,72,77,1)), color-stop(100%,rgba(0,0,0,1))); /* Chrome,Safari4+ */
			background: -webkit-linear-gradient(top,  rgba(69,72,77,1) 0%,rgba(0,0,0,1) 100%); /* Chrome10+,Safari5.1+ */
			background: -o-linear-gradient(top,  rgba(69,72,77,1) 0%,rgba(0,0,0,1) 100%); /* Opera 11.10+ */
			background: -ms-linear-gradient(top,  rgba(69,72,77,1) 0%,rgba(0,0,0,1) 100%); /* IE10+ */
			background: linear-gradient(to bottom,  rgba(69,72,77,1) 0%,rgba(0,0,0,1) 100%); /* W3C */
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#45484d', endColorstr='#000000',GradientType=0 ); /* IE6-9 */
		}
		
		.writer{
			width: 1000px;
		  margin: 0 auto;
			line-height: 30px;
			height: 35px;
			color: #f0f0f0;
			text-align: center;
		}
					
	</style>
</head> 

<%! 
//Steve Menu_Log Start 13/2/27
private ApplicationContext context = SpringUtils.getApplicationContext(); 
//Steve Menu_Log End
private String userName = null;
private String pswd = null;
%>

<%
if(session.getAttribute("userObj")!=null){
User userObj=(User)session.getAttribute("userObj");
String url = request.getParameter("url");

StringBuffer otherParas = new StringBuffer();
Enumeration enums = request.getParameterNames();
while(enums.hasMoreElements()){
	String para = enums.nextElement().toString();
	String value = request.getParameter(para) == null ? "" : new String(request.getParameter(para).getBytes("ISO-8859-1"),"UTF8");
	
	if(!para.equals("url") && !para.equals("reportName") && value.length() > 0){
		if(url.indexOf("?") > 0 || otherParas.toString().indexOf("?") > 0)
			otherParas.append("&").append(para).append("=").append(value);
		else
			otherParas.append("?").append(para).append("=").append(value);
	}
}

//Steve Menu_Log Start 13/2/27
String menuId = request.getParameter("menuId");
//String serverIP = request.getRemoteAddr();
InetAddress inetAddress = InetAddress.getLocalHost();
String serverIP = inetAddress.getHostAddress();
String userName = userObj.getChineseName();
String employeeCode = userObj.getEmployeeCode();
BuCommonPhraseHeadDAO buCommonPhraseHeadDao = (BuCommonPhraseHeadDAO) context.getBean("buCommonPhraseHeadDAO");
BuCommonPhraseHead bph = buCommonPhraseHeadDao.findById("MenuLogSwitch");
%>

<%
if("Y".equals(bph.getEnable())){

if(null!=menuId){
SiMenuLogDAO simenulogDAO = (SiMenuLogDAO) context.getBean("siMenuLogDAO");
SiMenuLog simenulog = new  SiMenuLog();

simenulog.setEmployeeCode(employeeCode);
simenulog.setActionMode("Click");
simenulog.setMenuId(menuId);
simenulog.setOpenDate(new Date());
simenulog.setApServerIp(serverIP);
simenulogDAO.save(simenulog); 
}
}
//Steve Menu_Log End

%>
<script language="JavaScript">
  
   var userName;
   var pswd;
   function  checkIden(){
    alert('dede');
      userName = document.getElementById('j_username').value;
      pswd = document.getElementById('j_password').value; 
      
   }
</script>
<body>
<div id="wrapper">
		<div id="item">
			<div id="employee-wrapper">
				<h3> 請輸入帳號密碼 </h3>
					
				<form id="form-wrapper" method="post"  action="#">
					<ul>
						<li> 帳號：<input type="text" id="num"></input> </li>
						<li> 密碼：<input type="password" id="psd"></input> </li>
						<li> <input type="submit" id="send" value="送出">	</li>	
					</ul>	
				</form>					
								
			</div>	
		</div>	
	</div>
	<footer id="colophon">

		<div id="footer-bottom"  class="clear-both">
			<address class="writer">
				Copyright © 2014 by TasaMengCoporation 資訊部
			</address>
		</div>
				
	</footer>	
<script language="JavaScript">
<%
if(userName!=""&&pswd!=""){
response.sendRedirect(url + (otherParas.toString()));
}
%>
</script>
<%
}else{
%>
<script language="JavaScript">
<!--
opener.document.location.reload();
self.close(); 
// -->
</script>
<%
}
%>
</body>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
</html>



