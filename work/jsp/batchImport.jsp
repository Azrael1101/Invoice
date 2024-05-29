<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO"%>
<%@page import="tw.com.tm.erp.hbm.bean.BuCommonPhraseLine"%>
<%@page import="java.util.*"%>
<%
	//String brandCode = (String)session.getAttribute("brandCode");
	//String employeeCode = (String)session.getAttribute("employeeCode");
	String brandCode = (String)session.getAttribute("user_brand");
	String employeeCode = (String)session.getAttribute("user_name");
	if(null == brandCode || null == employeeCode){
		response.sendRedirect("index.jsp");
	}

	String importPath = request.getParameter("importPath");
	String msg = request.getParameter("msg");
	if(null == msg)
		msg = "";
	employeeCode = employeeCode.substring(0,employeeCode.indexOf("@"));
	BuCommonPhraseLineDAO buCommonPhraseLineDAO = (BuCommonPhraseLineDAO)SpringUtils.getApplicationContext().getBean("buCommonPhraseLineDAO");
	List<BuCommonPhraseLine> buCommonPhraseLines =  new ArrayList(0);
	if(null != importPath){
		BuCommonPhraseLine buCommonPhraseLine = buCommonPhraseLineDAO.findById("ImportPath",importPath);
		if(null == buCommonPhraseLine)
			response.sendRedirect("../index.jsp");
		else
			buCommonPhraseLines.add(buCommonPhraseLine);
	}else{
		buCommonPhraseLines = buCommonPhraseLineDAO.findEnableLineById("ImportPath");
	}

	String desc = request.getParameter("desc");
	if(null == desc)
		desc = "";
	else
		desc = new String(desc.getBytes("ISO-8859-1"), "UTF-8");

%>
<html><head>
<title>KWE</title>
<script type="text/javascript" src="/erp/dwr/interface/importDataParse.js"></script>
<script type="text/javascript" src="/erp/dwr/engine.js"></script>
<script type="text/javascript" src="/erp/dwr/util.js"/></script>
<script>
var iPage = 1;
var iSize = 10;
var timeStamp = "";
var orderTypeCode = "";
var orderNo = "";
var countsId = "";

	function changePath(){
		for(i = 0 ; i < document.form0.selectImportMethod.length ; i++){
			var selectList = document.form0.selectImportMethod.options[i].value;
			var selectedList = document.form0.selectImportMethod.value;

			if(selectList == selectedList){
				if(null != document.getElementById(selectList))
					document.getElementById(selectList).style.display = "inline";
			}else{
				if(null != document.getElementById(selectList))
					document.getElementById(selectList).style.display = "none";
			}
		}

		importDataParse.getImportPath(document.form0.selectImportMethod.value,function(callback){
			if("DeclarationImportDataT2" != document.form0.selectImportMethod.value)
				callback = "\\\\<%=request.getRemoteAddr()%>\\"+callback;
			document.form0.selectedFileImportPath.value = callback;
		});
	}

	//查詢
	function findImportFiles(clean){
		var filePath = document.form0.selectedFileImportPath.value;
		var fileName = document.form0.selectedFileName.value.toUpperCase()
		if("Y" == clean){
			var rTable = document.getElementById("rTable");
			while(rTable.rows.length > 0){
				rTable.deleteRow(0);
			}
		}
		document.form0.selectedFileName.value = fileName;
			iPage = document.form0.iPage.value;
			importDataParse.getListFilesByPage(filePath, fileName, iPage, iSize, function(callback){
			var listFiles = eval(callback);
			var iTable = document.getElementById("iTable");
			while(iTable.rows.length > 2){
				iTable.deleteRow(2);
			}
			for(i=0 ; i<11 ; i++){
				if(listFiles[i+1] != null){
					var x=iTable.insertRow(i+2);
					var y=x.insertCell(0);
					var z=x.insertCell(1);
					y.innerHTML="<input type=checkbox name=\"listFiles\" value=\""+listFiles[i]+"\" >";
					z.innerHTML=listFiles[i];
				}else{
					document.form0.iTotalPage.value = listFiles[i];
					break;
				}
			}
		});
	}

	function getImportFiles(){
		document.getElementById("buttonImport").style.display = "none";
		var filePath = document.form0.selectedFileImportPath.value;
		var methodName = document.form0.selectImportMethod.value;
		var msg = document.form0.msg.value;
		if(methodName == ""){
			alert('請選擇匯入種類名稱');
		}else{
			var listFiles = document.getElementsByName("listFiles");
			var importFiles = [];
			for(i=0 ; i<listFiles.length ; i++){
				if(listFiles[i].checked){
					importFiles.push(listFiles[i].value);
				}
			}

			importDataParse.getListFiles('<%=brandCode%>', '<%=employeeCode%>', filePath, methodName, importFiles, msg, function(callback){
				var rTable = document.getElementById("rTable");
				while(rTable.rows.length > 0){
					rTable.deleteRow(0);
				}
				var x=rTable.insertRow(0);
				var y=x.insertCell(0);
				var returnValues = callback.split(";;");
				y.innerHTML=returnValues[0];
				if(returnValues.length > 1){
					var url = eval(returnValues[1]);
					//y.innerHTML = y.innerHTML + "<br>" + url;
				}
				findImportFiles('N');
				document.form0.listFile.checked = false;
				document.getElementById("buttonImport").style.display = "inline";
			});
		}
	}

	function deleteImportFiles(){
		document.getElementById("buttonDelete").style.display = "none";
		var filePath = document.form0.selectedFileImportPath.value;
		var listFiles = document.getElementsByName("listFiles");
		var importFiles = [];
		for(i=0 ; i<listFiles.length ; i++){
			if(listFiles[i].checked){
				importFiles.push(listFiles[i].value);
			}
		}
		importDataParse.deleteListFiles(filePath, importFiles, function(callback){
			var rTable = document.getElementById("rTable");
			while(rTable.rows.length > 0){
				rTable.deleteRow(0);
			}
			var x=rTable.insertRow(0);
			var y=x.insertCell(0);
			var returnValues = callback.split(";;");
			y.innerHTML=returnValues[0];
			findImportFiles('N');
			document.form0.listFile.checked = false;
			document.getElementById("buttonDelete").style.display = "inline";
		});
	}

	function showMessage(){
		var width = "600";
	    var height = "400";
	    var methodName = document.form0.selectImportMethod.value;
		var returnData = window.open(
			"/erp/jsp/ShowProgramLog.jsp" +
			"?programId=" + methodName +
			"&levelType=ERROR" +
	        "&identification=" + methodName,
			"Message",
			'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
	}

	function selectAll(){
		var listFiles = document.getElementsByName("listFiles");
		for(i=0 ; i<listFiles.length ; i++){
			listFiles[i].checked = document.form0.listFile.checked;
		}
	}

	function changePage(sta){
		curPage = parseInt(document.form0.iPage.value);
		maxPage = parseInt(document.form0.iTotalPage.value);
		//F為首頁
		if(sta == "F"){
			document.form0.iPage.value = 1;
		//B為上一頁
		}else if(sta == "B"){
			if(curPage == 1){
				document.form0.iPage.value = 1;
			}else{
				document.form0.iPage.value = curPage - 1;
			}
		//A為下一頁
		}else if(sta == "A"){
			if(curPage == maxPage){
				document.form0.iPage.value = maxPage;
			}else{
				document.form0.iPage.value = curPage + 1;
			}
		//L為最後一頁
		}else if(sta == "L"){
			document.form0.iPage.value = maxPage;
		}else{
			if(isNaN(curPage) || curPage > maxPage){
				alert('請輸入正確的數字');
				document.form0.iPage.value = 1;
			}
		}
		findImportFiles('Y');
	}

	function confirmImmovement(batchNo,originalOrderTypeCode,originalOrderNo){
		var url = "";
		if(typeof batchNo != 'undefined' && batchNo != 'undefined' && batchNo != '')
			timeStamp = batchNo;
		if(typeof originalOrderTypeCode != 'undefined' && originalOrderTypeCode != 'undefined' && originalOrderTypeCode != '')
			orderTypeCode = originalOrderTypeCode;
		if(typeof originalOrderNo != 'undefined' && originalOrderNo != 'undefined' && originalOrderNo != '')
			orderNo = originalOrderNo;
		if(timeStamp == ""){
			alert('尚未匯入清單，無法開啟報表');
		}else if(orderTypeCode != '' && orderTypeCode != 'null' && orderNo != '' && orderNo != 'null' && (orderTypeCode == 'EIF' || orderTypeCode == 'EIP')){
			if(confirm('是否要列印進貨差異表(預出量)')){
					//http://10.1.99.161:8080/crystal/t2/IM0601_T2.rpt?user=supervisor&prompt0=&prompt1=&prompt2=&prompt3=
					url = "http://10.1.94.161:8080/crystal/t2/IM0601_T2_batch.rpt?prompt0="+orderTypeCode+"&prompt1="+orderNo;
		    		window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
			}
		}else{
			if(confirm('是否要列印調撥匯入複核差異表')){
				//alert('<%=brandCode%>');
				//http://10.1.99.161:8080/crystal/t2/IM0163.rpt?user=supervisor&prompt0=WRP&prompt1= 201005030003&prompt2= 201005030004&prompt3=2010050301
				if('T2A'=='<%=brandCode%>')
					url = "http://10.1.94.161:8080/crystal/t2/IM0163_T2A.rpt?prompt0=&prompt1=&prompt2=&prompt3=" + timeStamp;
	    		else
					url = "http://10.1.94.161:8080/crystal/t2/IM0163.rpt?prompt0=&prompt1=&prompt2=&prompt3=" + timeStamp;
				window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
			}
		}
		return url;
	}

	function confirmImInventoryCounts(batchNo, countId){
		var url = "";
		if(typeof batchNo != 'undefined' && batchNo != 'undefined' && batchNo != '')
			timeStamp = batchNo;
		if(typeof countId != 'undefined' && countId != 'undefined' && countId != '')
			countsId = countId;
		if(timeStamp == "" || countsId == ""){
			alert('尚未匯入清單，無法開啟報表');
		}else{
			if(confirm('是否要列印盤點表')){
				url = "http://10.1.94.161:8080/crystal/t2/IM0123.rpt?prompt0=<%=brandCode%>&prompt1="+countsId+"&prompt2=&prompt3=&prompt4=&prompt5="+timeStamp+"&promopt6=";
	    		window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
	    	}
	    }
		return url;
	}

	function confirmImInventoryCountsList(batchNo){
		var url = "";
		if(timeStamp == "" || countsId == ""){
			alert('尚未匯入清單，無法開啟報表');
		}else{
			if(confirm('是否要列印盤點清單')){
				url = "http://10.1.94.161:8080/crystal/t2/IM0120.rpt?prompt0=<%=brandCode%>&prompt1="+countsId+"&prompt2=" + timeStamp +"&prompt3=&prompt4=&prompt5=";
    			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
			}
		}
		return url;
	}
	
	function reconfirmImmovement(batchNo,originalOrderTypeCode,orderNo1,orderNo2,orderNo3,orderNo4,orderNo5,orderNo6,orderNo7,orderNo8,orderNo9,orderNo10){
		if(confirm('是否要列印內倉覆核差異表')){	
			url = "http://10.1.94.161:8080/crystal/t2/IM0614.rpt?prompt0="+originalOrderTypeCode+"&prompt1="+orderNo1+"&prompt2="+orderNo2+"&prompt3="+orderNo3+"&prompt4="+orderNo4+"&prompt5="+orderNo5+"&prompt6="+orderNo6+"&prompt7="+orderNo7+"&prompt8="+orderNo8+"&prompt9="+orderNo9;+"&prompt10="+orderNo10;
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}

</script>
</head>
<body onload="changePath()">
<form name="form0">
<input type="HIDDEN" name="msg" value="<%=msg%>">
	<table border="0">
		<tr><td style="font-size:36px;font-weight:bold"><%=desc%></td></tr>
		<tr>
			<td>
				<select name="selectImportMethod" onchange="changePath()">
					<!-- <option value="">請選擇匯入種類名稱</option> -->
					<%for(int i = 0; i < buCommonPhraseLines.size(); i++){
						BuCommonPhraseLine line = buCommonPhraseLines.get(i);
					%>
					<option value="<%=line.getId().getLineCode()%>"><%=line.getName()%></option>
					<%} %>
				</select>
				&nbsp;
				<img src="../images/button_find.gif" onClick = "findImportFiles('Y')">
				&nbsp;
				<img id="buttonImport" src="../images/button_import.gif" onClick="getImportFiles()">
				&nbsp;
				<img src="../images/button_message_prompt.gif" onClick="showMessage()">
				&nbsp;
				<img id="buttonDelete" src="../images/button_delete.gif" onClick="deleteImportFiles()">
				&nbsp;
				<div id = "ImMovementImportDataT2" style="display:none">
					<img src="../images/button_receive_difference_reprot.gif" onClick="confirmImmovement()">
				</div>
				<div id = "ImMovementImportDataT2cw" style="display:none">
					<img src="../images/button_receive_difference_reprot.gif" onClick="confirmImmovement()">
				</div>
				<div id = "ImInventoryCountsImportDataT2" style="display:none">
					<img src="../images/button_inventory.gif" onClick="confirmImInventoryCounts()">
					&nbsp;
					<img src="../images/button_inventory_list.gif" onClick="confirmImInventoryCountsList()">
				</div>
			</td>
		</tr>
		<tr>
			<td>
				檔案名稱篩選
				<input type="text" name="selectedFileName" size="40" value=".TXT">
			</td>
		</tr>
		<tr>
			<td>
				路徑
				<input type="text" name="selectedFileImportPath" size="50">
			</td>
		</tr>
	</table>
	<table border="1" id ="iTable">
		<tr>
			<td colspan="2">
				<img src="../images/icon_10.gif" border="0" onClick="changePage('F')">&nbsp;
				<img src="../images/icon_09.gif" border="0" onClick="changePage('B')">&nbsp;
				<input type="text" name="iPage" value="1" size="1" onChange="changePage()">
				&nbsp;/&nbsp;
				<input type="text" name="iTotalPage" value="1" size="1" readOnly>
				<img src="../images/icon_08.gif" border="0" onClick="changePage('A')">&nbsp;
				<img src="../images/icon_11.gif" border="0" onClick="changePage('L')">&nbsp;
			</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="listFile" onClick="selectAll()">全選</td>
			<td>檔案名稱</td>
	</table>
	<br>
	<table id ="rTable">
		<tr>
			<td>&nbsp;</td>
		</tr>
	</table>
</form>
</body>
</html>
