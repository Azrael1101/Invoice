<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="java.util.*,java.io.*,java.rmi.RemoteException"%>
<%@page import="javax.swing.JTree"%>
<%@page import="org.springframework.util.StringUtils"%>

<%@page import="tw.com.tm.erp.utils.*"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.bean.BuCommonPhraseLine"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	
	BuEmployeeDAO buEmployeeDAO = (BuEmployeeDAO)SpringUtils.getApplicationContext().getBean("buEmployeeDAO");
	BuEmployee buEmployee = buEmployeeDAO.findById("T96085");
	
	tw.com.tm.erp.utils.User userObj = null;
	
	if("NEW".equals(buEmployee.getSwitchTo())){
		userObj = ObtainUserInfomationNew.getUserPermission("T2", "T96085", "林暐哲");
	}else{
		userObj = ObtainUserInfomation.getUserPermission("T2", "T96085", "林暐哲");
	}
	
	session.setAttribute("userObj", userObj);
	List<JTree> jTrees = userObj.getUserProgramRight().getMenuTreeXml();
	List<String> menuNames = userObj.getUserProgramRight().getMenuName();
	List<String> menuUrls = userObj.getUserProgramRight().getMenuUrl();
	List<String> imgNameAs = userObj.getUserProgramRight().getImgNameA();
	List<String> imgNameBs = userObj.getUserProgramRight().getImgNameB();
	List<String> menuStrings = new ArrayList();

	for (Iterator iterator = jTrees.iterator(); iterator.hasNext();) {
		JTree jTree = (JTree) iterator.next();
		if("NEW".equals(buEmployee.getSwitchTo())){
			String menuString = ObtainUserInfomationNew.getUserMenuString(jTree);
			//System.out.println("menuString = " + menuString);
			menuStrings.add(menuString);
		}else{
			String menuString = ObtainUserInfomation.getUserMenuString(jTree);
			//System.out.println("menuString = " + menuString);
			menuStrings.add(menuString);	
		}
	}

	try {
		File imageFile = new File("D:/Java/Tomcat_9.0/tomcat.ico");
		File srcFile = new File("D:/Java/Tomcat_9.0/tomcat.ico");
		boolean createImage = false;
		if (!imageFile.exists()) {
			createImage = true;
		} else {
			if ((imageFile.lastModified() < srcFile.lastModified()) || (imageFile.length() - srcFile.length() != 0L)) {
				createImage = true;
			}
		}
		if (createImage) {
			File imagePath = new File("D:/Java/Tomcat_9.0");
			if (!imagePath.exists()) {
				imagePath.mkdirs();
			}
			FileInputStream inputStream = new FileInputStream(srcFile);
			FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
			byte[] buf = new byte[1];
			int len = 0;
			while ((len = inputStream.read(buf)) != -1) {
				fileOutputStream.write(buf, 0, len);
			}
			inputStream.close();
			fileOutputStream.close();
		}
	} catch (Exception e) {
		//e.printStackTrace();
		for (int i = 0; i < e.getStackTrace().length; i++) {
			System.out.println(e.getStackTrace()[i].toString());
		}
	}
	//跑馬燈顯示資訊
	//BuCommonPhraseLineDAO buCommonPhraseLineDAO = (BuCommonPhraseLineDAO)SpringUtils.getApplicationContext().getBean("buCommonPhraseLineDAO");
	//BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("Marquee", "content");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>KWE</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script type="text/javascript" src="js/jquery-1.2.2.pack.js"></script>
<script type="text/javascript" src="js/jquery.dimensions.min.js"></script>
<script type="text/javascript" src="js/jquery.menu.js"></script>
<script type="text/javascript" src="js/shCore.js"></script>
<script type="text/javascript" src="js/shBrushXml.js"></script>
<script type="text/javascript" src="js/shBrushJScript.js"></script>

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
</style>
<style type="text/css">

div#nifty{ margin: 0 30px;background: #eeeeee;}
b.rtop, b.rbottom{display:block;background: #FFF}
b.rtop b, b.rbottom b{display:block;height: 1px;overflow: hidden; background: #eeeeee}
b.r1{margin: 0 5px}
b.r2{margin: 0 3px}
b.r3{margin: 0 2px}
b.rtop b.r4, b.rbottom b.r4{margin: 0 1px;height: 2px}
</style>
<link href="table.css" rel="stylesheet" type="text/css" />
<script src="js/AC_RunActiveContent.js" type="text/javascript"></script>

<link href="css/css.css" rel="stylesheet" type="text/css" />

<style type="text/css">
#xsnazzy {background: transparent; margin:1px;}
.xtop, .xbottom {display:block; background:transparent; font-size:1px;}
.xb1, .xb2, .xb3, .xb4 {display:block; overflow:hidden;}
.xb1, .xb2, .xb3 {height:1px;}
.xb2, .xb3, .xb4 {background:#E7E7E7; border-left:1px solid #08c; border-right:1px solid #08c;}
.xb1 {margin:0 5px; background:#08c;}
.xb2 {margin:0 3px; border-width:0 2px;}
.xb3 {margin:0 2px;}
.xb4 {height:2px; margin:0 1px;}
.xboxcontent {display:block; background:#eeeeee; border:0 solid #08c; border-width:0 1px;}
.xb9 {width:80%;}
.style5 {color: #CC0000}
</style>
<script type="text/javascript">
<!--

function MM_preloadImages() { //v3.0
    var d = document;
    if (d.images) {
        if (!d.MM_p) d.MM_p = new Array();
        var i, j = d.MM_p.length,
            a = MM_preloadImages.arguments;
        for (i = 0; i < a.length; i++)
        if (a[i].indexOf("#") != 0) {
            d.MM_p[j] = new Image;
            d.MM_p[j++].src = a[i];
        }
    }
}

function MM_swapImgRestore() { //v3.0
    var i, x, a = document.MM_sr;
    for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++) x.src = x.oSrc;
}

function MM_findObj(n, d) { //v4.01
    var p, i, x;
    if (!d) d = document;
    if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
        d = parent.frames[n.substring(p + 1)].document;
        n = n.substring(0, p);
    }
    if (!(x = d[n]) && d.all) x = d.all[n];
    for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][n];
    for (i = 0; !x && d.layers && i < d.layers.length; i++) x = MM_findObj(n, d.layers[i].document);
    if (!x && d.getElementById) x = d.getElementById(n);
    return x;
}

function MM_swapImage() { //v3.0
    var i, j = 0,
        x, a = MM_swapImage.arguments;
    document.MM_sr = new Array;
    for (i = 0; i < (a.length - 2); i += 3)
    if ((x = MM_findObj(a[i])) != null) {
        document.MM_sr[j++] = x;
        if (!x.oSrc) x.oSrc = x.src;
        x.src = a[i + 2];
    }
}
//-->
var speed = 100
var text = "采盟股份有限公司"

function Blink1() {
    window.status = text
    setTimeout("Blink2()", speed)
}

function Blink2() {
    window.status = "采盟股份有限公司"
    setTimeout("Blink1()", speed)
}
Blink1()

//-->
</script>


<script language="JavaScript">
<!--
if (navigator.appName.indexOf("Internet Explorer") != -1)
document.onmousedown = noSourceExplorer;
function noSourceExplorer(){if (event.button == 2 | event.button == 3)
{
	alert("禁止使用滑鼠右鍵!");
return false;
}
return true;
}

// -->
</script>

</head>

<body>
<script language="JavaScript" type="text/javascript">
var vn = "Microsoft Internet Explorer";
var some;
if (navigator.appName != vn)
	some = 1900;
else
	some = 0;

function montharr(m0, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11) {
    this[0] = m0;
    this[1] = m1;
    this[2] = m2;
    this[3] = m3;
    this[4] = m4;
    this[5] = m5;
    this[6] = m6;
    this[7] = m7;
    this[8] = m8;
    this[9] = m9;
    this[10] = m10;
    this[11] = m11;
}

function calendar() {
    var monthNames = "JanFebMarAprMayJunJulAugSepOctNovDec";
    var today = new Date();
    var thisDay;
    var monthDays = new montharr(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    year = today.getYear();
    thisDay = today.getDate();
    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
    	monthDays[1] = 29;
    nDays = monthDays[today.getMonth()];
    firstDay = today;
    firstDay.setDate(1); // works fine for most systems
    testMe = firstDay.getDate();
    if (testMe == 2) firstDay.setDate(0);
    startDay = firstDay.getDay();
    document.write('<table width="100%" border="0" cellspacing="1" cellpadding="1" align="CENTER" bgcolor="#FFFFFF"><TR><TD><table width="100%" border="0" cellspacing="1" cellpadding="1" bgcolor="Silver">');
    document.write('<TR><th colspan="7" bgcolor="#C8E3FF">');
    var dayNames = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    var monthNames = new Array("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月");
    var now = new Date();
    document.write("<font style=font-size:10pt;Color:#330099;font-family:新細明體>", "西元", " ", now.getYear() + some, "年", " ", monthNames[now.getMonth()], " ", now.getDate(), "日", " ", dayNames[now.getDay()], "</FONT>");
    document.writeln('</TH></TR><TR><TH BGCOLOR="#0080FF"><font style="font-size:10pt;Color:White">日</FONT></TH>');
    document.writeln('<th bgcolor="#0080FF"><font style="font-size:10pt;Color:White;font-family:新細明體">一</FONT></TH>');
    document.writeln('<TH BGCOLOR="#0080FF"><font style="font-size:10pt;Color:White;font-family:新細明體">二</FONT></TH>');
    document.writeln('<TH BGCOLOR="#0080FF"><font style="font-size:10pt;Color:White;font-family:新細明體">三</FONT></TH>');
    document.writeln('<TH BGCOLOR="#0080FF"><font style="font-size:10pt;Color:White;font-family:新細明體">四</FONT></TH>');
    document.writeln('<TH BGCOLOR="#0080FF"><font style="font-size:10pt;Color:White;font-family:新細明體">五</FONT></TH>');
    document.writeln('<TH BGCOLOR="#0080FF"><font style="font-size:10pt;Color:White;font-family:新細明體">六</FONT></TH>');
    document.writeln("</TR><TR>");
    column = 0;
    for (i = 0; i < startDay; i++) {
        document.writeln("\n<TD><FONT style=font-size:10pt> </FONT></TD>");
        column++;
    }
    for (i = 1; i <= nDays; i++) {
        if (i == thisDay) {
            document.writeln('</TD><td align="CENTER" bgcolor="#FF8040" ><FONT style=font-size:10pt;Color:#ffffff><B>')
        }
        else {
            document.writeln('</TD><TD bgcolor="#CCCCCC" ALIGN="CENTER"><FONT style=font-size:10pt;font-family:Arial;font-weight:bold;Color:#330066>');
        }
        document.writeln(i);
        if (i == thisDay)
        	document.writeln("</FONT></TD>");
        	column++;
        if (column == 7) {
            document.writeln("<TR>");
            column = 0;
        }
    }
    document.writeln('</TABLE></TD></TR></TABLE>');
}

var timerID = null;
var timerRunning = false;
var winArr = new Array(); // 子視窗

function openwindow(url) {
    closeAll();
    var newwin = window.open(url, '_blank', 'height=768, width=1024,titlebar=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes');
    newwin.focus();
    winArr[winArr.length] = newwin; // 紀錄所有子視窗
    return false;
}

// 關閉所有子視窗（包括iframe開啟的視窗）
function closeChildWindow() {
	if(!confirm("登出時，子視窗將會被關閉，您是否確定登出？"))
		return;
    var iframeObj = document.getElementById('workList');
    //alert(iframeObj.contentWindow.getWinObj());
    if(iframeObj.contentWindow.getWinObj() != null){ // iframe內開啟的子視窗
    	var iframeWinArray = new Array();
    	iframeWinArray = iframeObj.contentWindow.getWinObj();
    	for (var i = 0; i < iframeWinArray.length; i++) {
        	iframeWinArray[i].close();
    	}
    }
    for (var i = 0; i < winArr.length; i++) { // 本頁開啟的子視窗
        winArr[i].close();
    }
    window.location.href="TBCNLogout?nextUrl=/logout.jsp";
}
</script>
<script type="text/JavaScript">
function showSearch() {
    alert("showSearch");
    alert(document.getElementById('popup_search').style.display);
    alert(document.getElementById('hideDiv').style.display);
    document.getElementById("hideDiv").style.display = "inline";
    document.getElementById("popup_search").style.display = "none";
    alert(document.getElementById('popup_search').style.display);
    alert(document.getElementById('hideDiv').style.display);
}

function closeSearch() {
    alert("closeSearch");
    document.getElementById("hideDiv").style.display = "none";
    document.getElementById("popup_search").style.display = "inline";
}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th align="center" valign="middle" scope="col"><table width="951" border="0" cellpadding="0" cellspacing="0" bgcolor="#000000">
        <tr>
          <th scope="col">
          	<script type="text/javascript">
			  AC_FL_RunContent( 'codebase','cab/swflash.cab#version=9,0,28,0','width','950','height','62','src','images/top','quality','high','pluginspage','http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash','movie','images/top' ); //end AC code
		    </script>
            <noscript>
            <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="cab/swflash.cab#version=9,0,28,0" width="950" height="62">
              <param name="movie" value="images/top.swf" />
              <param name="quality" value="high" />
              <embed src="images/top.swf" quality="high" pluginspage="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" width="950" height="62"></embed>
            </object>
            </noscript></th>
        </tr>
        <tr>
          <td width="100%" height="33" align="center" valign="top" background="images/top_02.jpg"><table width="100%" border="0" align="center" cellpadding=" 0" cellspacing="0" >
              <tr>
                <!-- <td>&nbsp;</td> -->
                <%for (int i = 0 ; i < menuStrings.size() ; i++) {
                	String menuName = menuNames.get(i);
                	String menuUrl = menuUrls.get(i);
                	String imgNameA = imgNameAs.get(i);
                	String imgNameB = imgNameBs.get(i);
                	String menuString = menuStrings.get(i);
                %>
                	<td width="87" align="left"><div id="userMenu<%=i%>"><a onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image00<%=i%>','','<%=imgNameB%>',1)" <%=null != menuUrl ? "href='"+menuUrl+"' target='_blank' ": ""%>><img src="<%=imgNameA%>" name="Image00<%=i%>" width="87" height="33" border="0" id="Image00<%=i%>" align="top" title="<%=menuName%>" /></a><%=menuString%></div></td>
                <%}%>
                <td width="100%">&nbsp;</td>
                <!-- <td width="87" align="right"><a href="./redirectBI.jsp" onClick="return openwindow(this.href);" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image26','','images/but03_b.gif',1)"><img src="images/but03_a.gif" name="Image26" width="108" height="33" border="0" id="Image26"  /></a></td>
                <td width="87" align="right"><a onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image27','','images/but00.gif',1)"><img src="images/but00.gif" name="Image27" width="87" height="33" border="0" id="Image27"  /></a></td>
                <td width="87" align="right"><a onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image28','','images/but00.gif',1)"><img src="images/but00.gif" name="Image28" width="87" height="33" border="0" id="Image28"  /></a></td> -->
              </tr>
            </table></td>
        </tr>
        <!--<tr>
        <td height="10">&nbsp;</td>
      </tr>-->
        <tr>
          <td><div align="center">
              <table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                <tr>
                  <td width="680" align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding=" 0">
                      <tr>
                        <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding=" 0">
                            <tr>
                              <td>
							  <%if(true){%>
                              	<br/>
                              	
                              	<br/>
                              <%}%>	
                              	<img src="images/but_01.gif" width="162" height="24" />
                              </td>
                            </tr>
                            <tr>
                              <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                  <tr>
                                    <td width="6" height="6" align="right" valign="top"><img src="images/cons_01.jpg" width="6" height="6" /></td>
                                    <td background="images/cons_02.jpg"><img src="images/cons_02.jpg" width="6" height="6" /></td>
                                    <td width="6" align="left" valign="top"><img src="images/cons_04.jpg" width="6" height="6" /></td>
                                  </tr>
                                  <tr>
                                    <td align="right" background="images/cons_08.jpg"><img src="images/cons_08.jpg" width="6" height="6" /></td>
                                    <td bgcolor="#C8C8C8" height="500px" width="660px"><iframe id='workList' align="center" MARGINHEIGHT="0" MARGINWIDTH="0" SCOLLING="no" FRAMEBORDER="0" style="width:100%;height:100%;background-color:#C8C8C8;" src="/wf_worklist/worklist:worklist:portal.page"> </iframe></td>
                                    <td width="6" align="left" valign="top" background="images/cons_06.jpg"><img src="images/cons_06.jpg" width="6" height="6" /></td>
                                  </tr>
                                  <tr>
                                    <td width="6" height="6" align="right" valign="top"><img src="images/cons_10.jpg" width="6" height="6" /></td>
                                    <td background="images/cons_11.jpg"><img src="images/cons_11.jpg" width="6" height="6" border="0" /></td>
                                    <td width="6" height="6" align="left" valign="top"><img src="images/cons_12.jpg" width="6" height="6" /></td>
                                  </tr>
                                </table></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table></td>
                  <td width="10" align="right" valign="top">&nbsp;</td>
                  <td width="10" align="right" valign="top">&nbsp;</td>
                  <td width="260" align="right" valign="top"><table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                      <tr>
                        <td><div align="center">
                            <table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                              <tr>
                                <td height="1"><img src="images/right_but_04.jpg" width="259" height="1" /></td>
                              </tr>
                              <tr>
                                <td valign="top" background="images/right_but_07.jpg"><table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                                    <tr>
                                      <td width="24">&nbsp;</td>
                                      <td colspan="2" valign="top"><table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                                          <tr>
                                            <td width="16" height="17" align="left"><img src="images/icon_09.gif" width="12" height="12" /></td>
                                            <td class="orage2"><div align="left" class="cu1">登入資訊</div></td>
                                          </tr>
                                        </table></td>
                                    </tr>
                                    <tr>
                                      <td>&nbsp;</td>
                                      <td width="65" valign="top"><table width="65" border=" 0" align="center" cellpadding=" 0" cellspacing="0">
                                          <tr>
                                            <td height="10" class="orage2"><div align="left"></div></td>
                                          </tr>
                                          <tr>
                                            <td class="orage2"><div align="left"><img src="images/boy.gif" width="65" height="75" /></div></td>
                                          </tr>
                                          <tr>
                                            <td class="coffee">&nbsp;</td>
                                          </tr>
                                        </table></td>
                                      <td valign="top"><table width="120" border=" 0" align="center" cellpadding=" 0" cellspacing="0">
                                          <tr>
                                            <td height="10" class="orage2"><div align="left"></div></td>
                                          </tr>
                                          <tr>
                                            <td class="coffee"><div align="left">姓名:<%=userObj.getChineseName()%></div></td>
                                          </tr>
                                          <tr>
                                            <td class="coffee"><div align="left">單位:<%=""+"/"+userObj.getDepartment()+"<br>品牌: "+userObj.getBrandName()%></div></td>
                                          </tr>
                                          <tr>
                                            <td class="coffee"><div align="left"><img src="images/logout01.gif" name="Image27" width="37" height="22" border="0" id="Image27" onclick="closeChildWindow();" onmouseout="MM_swapImgRestore()" onmouseover="this.style.cursor='pointer';MM_swapImage('Image27','','images/logout02.gif',1)"/><a href="/wf_worklist/worklist:user_preference:001.page" onClick="return openwindow(this.href);" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image28','','images/revise02.gif',1)"><img src="images/revise01.gif" name="Image28" width="37" height="22" border="0" id="Image28" /></a><a href="/wf_worklist/worklist:delegation:001.page" onClick="return openwindow(this.href);" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image29','','images/agent02.gif',1)"><img src="images/agent01.gif" name="Image29" width="46" height="22" border="0" id="Image29" /></a></div></td>
                                          </tr>
                                          <tr>
                                            <td class="coffee"><div align="left"></div></td>
                                          </tr>
                                        </table></td>
                                    </tr>
                                  </table></td>
                              </tr>
                              <tr>
                                <td height="1"><img src="images/right_but_12.jpg" width="259" height="1" /></td>
                              </tr>
                            </table>
                          </div></td>
                      </tr>
                      <tr>
                        <td><div align="center">
                            <div align="center">
                              <table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                                <tr>
                                  <td height="1"><img src="images/right_but_04.jpg" width="259" height="1" /></td>
                                </tr>
                                <tr>
                                  <td valign="top" background="images/right_but_07.jpg"><table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                                <!--<tr>
                                  <td width="24">&nbsp;</td>
                                  <td colspan="2" valign="top"><table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                                      <tr>
                                        <td width="16" height="17" align="left"><img src="images/icon_09.gif" width="12" height="12" /></td>
                                        <td class="orage2"><div align="left" class="cu1">日曆</div></td>
                                      </tr>
                                  </table></td>
                                </tr>-->
                                      <tr>
                                        <td>&nbsp;</td>
                                        <td width="80%" valign="top" bgcolor="#FFFFFF"><script language="JavaScript" type="text/javascript">calendar();</script>
                                        </td>
                                        <td valign="top">&nbsp;</td>
                                      </tr>
                                      <!--<tr>
                                  <td>&nbsp;</td>
                                  <td valign="top">&nbsp;</td>
                                  <td valign="top">&nbsp;</td>
                                </tr>-->
                                    </table></td>
                                </tr>
                                <tr>
                                  <td height="1"><img src="images/right_but_12.jpg" width="259" height="1" /></td>
                                </tr>
                              </table>
                            </div>
                          </div></td>
                      </tr>
                      <tr>
                        <td><div align="center">
                            <div align="center">
                              <table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                                <tr>
                                  <td height="1"><img src="images/right_but_04.jpg" width="259" height="1" /></td>
                                </tr>
                                <tr>
                                  <td background="images/right_but_07.jpg"><table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                                      <tr>
                                        <td width="24">&nbsp;</td>
                                        <td width="65"><table width="120" border=" 0" align="left" cellpadding=" 0" cellspacing="0">
                                            <tr>
                                              <td valign="top"><table width="100%" border=" 0" cellspacing="0" cellpadding=" 0">
                                                  <tr>
                                                    <td width="16" height="17" align="left"><img src="images/icon_09.gif" width="12" height="12" /></td>
                                                    <td class="orage2"><div align="left" class="cu1">我的最愛</div></td>
                                                  </tr>
                                                  <tr>
                                                    <td width="16" height="17" align="left" valign="middle"><!--<img src="images/icon_09.gif" width="12" height="12" />--></td>
                                                    <td height="32" class="orage2"><div align="left" class="cu1">
                                                        <div align="right"><a href="http://www.tasameng.com.tw"  onClick="return openwindow(this.href);" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image38','','images/link_tsa_02.gif',1)"><img src="images/link_tsa_01.gif" name="Image38" width="100" height="30" border="0" id="Image38" /></a></div>
                                                      </div></td>
                                                  </tr>
                                                  <tr>
                                                    <td height="17" align="left" valign="middle"><!--<img src="images/icon_09.gif" width="12" height="12" />--></td>
                                                    <td height="32" class="orage2"><div align="right"><a href="http://dutyfree.tasameng.com.tw" onClick="return openwindow(this.href);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image39','','images/link_tsb_02.gif',1)"><img src="images/link_tsb_01.gif" name="Image39" width="100" height="30" border="0" id="Image39" /></a></div></td>
                                                  </tr>
                                                  <tr>
                                                    <td height="17" align="left" valign="middle"><!--<img src="images/icon_09.gif" width="12" height="12" />--></td>
                                                    <td height="32" class="orage2"><div align="right"><a href="http://www.tasameng.com.tw/tasameng/tm/our_brands/borsalini.html" onClick="return openwindow(this.href);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image40','','images/link_borsa_02.gif',1)"><img src="images/link_borsa_01.gif" name="Image40" width="100" height="30" border="0" id="Image40" /></a></div></td>
                                                  </tr>
                                                  <!--<tr>-->
                                                  <!--<td height="17" align="left" valign="middle"><!--<img src="images/icon_09.gif" width="12" height="12" /></td>-->
                                                  <!--<td height="32" class="orage2"><div align="right"><a href="http://www.fruits-passion.com.tw/fruits-passion/index.asp"  onClick="return openwindow(this.href);" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image41','','images/link_fruits_02.gif',1)"><img src="images/link_fruits_01.gif" name="Image41" width="100" height="30" border="0" id="Image41" /></a></div></td>-->
                                                  <!--</tr>-->
                                                  <tr>
                                                    <td height="17" align="left" valign="middle"><!--<img src="images/icon_09.gif" width="12" height="12" />--></td>
                                                    <td height="32" class="orage2"><div align="right"><a href="http://www.testoni.com/"  onClick="return openwindow(this.href);" onmouseout="MM_swapImgRestore()"  onClick="return openwindow(this.href);" onmouseover="MM_swapImage('Image42','','images/link_atestoni_02.gif',1)"><img src="images/link_atestoni_01.gif" name="Image42" width="100" height="30" border="0" id="Image42" /></a></div></td>
                                                  </tr>
                                                  <tr>
                                                    <td height="17" align="left" valign="middle"><!--<img src="images/icon_09.gif" width="12" height="12" />--></td>
                                                    <td height="32" class="orage2"><div align="right"><a href="http://www.coach.com"  onClick="return openwindow(this.href);" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image43','','images/link_coach_02.gif',1)"><img src="images/link_coach_01.gif" name="Image43" width="100" height="30" border="0" id="Image43" /></a></div></td>
                                                  </tr>
                                                  <tr>
                                                    <td height="17" align="left" valign="middle"><!--<img src="images/icon_09.gif" width="12" height="12" />--></td>
                                                    <td height="32" class="orage2"><div align="right"><a href=""  onClick="return openwindow(this.href);" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image45','','images/link_grosse_01.jpg',1)"><img src="images/link_grosse_01.jpg" name="Image45" width="100" height="30" border="0" id="Image45" /></a></div></td>
                                                  </tr>
                                                  <tr>
                                                  
                                                  <tr>
                                                    <td height="70"/>
                                                  </tr>
                                                </table></td>
                                            </tr>
                                          </table></td>
                                        <td>&nbsp;</td>
                                      </tr>
                                    </table></td>
                                </tr>
                                <tr>
                                  <td height="1"><img src="images/right_but_12.jpg" width="259" height="1" /></td>
                                </tr>
                              </table>
                            </div>
                          </div></td>
                      </tr>
                      <tr>
                        <td><div align="center"></div></td>
                      </tr>
                    </table></td>
                </tr>
              </table>
            </div></td>
        </tr>
        <!--<tr>
        <td>&nbsp;</td>
      </tr>-->
      </table></th>
  </tr>
</table>

<%
    String redirectUrl = (String)session.getAttribute("redirectUrl");
    if(StringUtils.hasText(redirectUrl)){
        session.removeAttribute("redirectUrl");
%>
        <script type="text/javascript">
            function setCookie(c_name,value,expiredays){
		        var exdate=new Date();
		        exdate.setDate(exdate.getDate()+expiredays);
		        document.cookie=c_name+ "=" +escape(value)+((expiredays==null) ? "" : "; expires="+exdate.toGMTString());
	        }
	        setCookie('requestUrl', "", 1);
            var urlPath = '<%=redirectUrl%>';
            var newWin = window.open(urlPath,'RedirectPage','height=768, width=1024,titlebar=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes');
            winArr[winArr.length] = newWin;
            newWin.resizeTo((screen.availWidth),(screen.availHeight));
		    newWin.moveTo(0,0);
            newWin.focus();
        </script>

<%
    }
%>

<script type="text/javascript">
<!--
var options = {
    minWidth: 78,
    arrowSrc: 'images/arrow_right.gif',
    copyClassAttr: true,
    onClick: function (e, menuItem) {}
};
var options = {
    minWidth: 78,
    arrowSrc: 'images/arrow_right.gif'
};

<%for (int i = 0 ; i < menuStrings.size() ; i++) { %>
	$('#userMenu<%=i%>').menu(options);
<%}%>

function closeAll() {
    $.Menu.closeAll();
}

function pagerefresh() {
    window.location.reload();
}
setInterval('pagerefresh()', 1000 * 60 * 10); // 指定10分鐘刷新一次

// 更改保持session的方式
//var time_id = setInterval("keepSession()", 1000 * 60 * 10); // 每十分鐘回 call一次tm.jsp
function keepSession() {
    $.ajax({
        url: "tm.jsp",
        dataType: "html",
        type: "POST",
        aynsc: false,
        success: function (data) {
            //alert(time_id);
        },
        error: function (data, status, e) {
            //alert("Error:" + data);
        }
    });
}

function checkTime() {
    RightNow = new Date();
    //alert(RightNow.getHours() + ":" + (RightNow.getMinutes() < 10 ? "0" + RightNow.getMinutes() : RightNow.getMinutes()) + ":" + (RightNow.getSeconds() < 10 ? "0" + RightNow.getSeconds() : RightNow.getSeconds()));
    var time = RightNow.getHours() + "" + (RightNow.getMinutes() < 10 ? "0" + RightNow.getMinutes() : RightNow.getMinutes()) + "" + (RightNow.getSeconds() < 10 ? "0" + RightNow.getSeconds() : RightNow.getSeconds())
    if (parseInt(time, 10) > 210000) // 超過晚上九點，則不在保持連線
    	clearInterval(time_id);

    setTimeout('checkTime()', 1000 * 15);
}
-->
</script>
</body >
</html>
