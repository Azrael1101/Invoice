<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String employeeCode = (String) session.getAttribute("user_name");
	employeeCode = employeeCode.substring(0, employeeCode.indexOf("@"));
%>
<html>
	<head>
		<title>KWE</title>
		<script type="text/javascript" src="/erp/dwr/interface/importDataParse.js"></script>
		<script type="text/javascript" src="/erp/dwr/engine.js"></script>
		<script type="text/javascript" src="/erp/dwr/util.js" /></script>
		<script type="text/javascript" src="../js/jquery.js"></script>
		<script type="text/javascript" src="../js/jquery.blockUI.js"></script>
		<script>
			function T2PosImport(){
				if(confirm("是否執行匯入機場(含馬祖完稅)銷售業績?")){
					$.blockUI({ message: '機場(含馬祖完稅)銷售業績上傳中，請等待'});  
					importDataParse.T2PosImport('<%=employeeCode%>', function(callback){
						$.unblockUI();
						alert(callback);
					});
				}
			}

			function T2PosImportMazu(){
				if(confirm("是否匯入馬祖保稅銷售業績?")){
					$.blockUI({ message: '馬祖保稅銷售業績上傳中，請等待'});  
					importDataParse.T2PosImportMazu('<%=employeeCode%>', function(callback){
						$.unblockUI();
						alert(callback);
					});
				}
			}

			function Exit(){
			  if(confirm("是否確認離開?")){
			      window.top.close();
			  }  
			}
		</script>
	</head>
	<body>
		<form name="form0">
			<table>
				<tr>
					<td colspan="2"><img src="../images/button_exit.gif" onClick="Exit()" /></td>
				</tr>
				<tr>
					<td>機場(含馬祖完稅)匯入按此:</td>
					<td><img src="../images/button_import.gif" onClick="T2PosImport()" /></td>
				</tr>
				<tr>
					<td>馬祖保稅匯入按此:</td>
					<td><img src="../images/button_import.gif" onClick="T2PosImportMazu()"></td>
				</tr>
			</table>
		</form>
	</body>
</html>