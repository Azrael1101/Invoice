var afterSavePageProcess = "" ;

/*
	initial 
*/
function detailInitial() {

	var statusTmp = document.forms[0]["#form.status"].value;
	var orderTypeCodeTmp = document.forms[0]["#form.orderTypeCode"].value;
	
	var varCanDataDelete = false;
	var varCanDataAppend = false;
	var varCanDataModify = false;
	if( statusTmp == "SAVE" || statusTmp == "REJECT" ){
		varCanDataDelete = true;
		varCanDataAppend = true;
		varCanDataModify = true;		
	}
	
	vat.formD.item(0, "F01", {type: "IDX", desc: "NO"});
	vat.formD.item(0, "F02", {type: "TEXT", size: 16, maxlength: 20,  desc: "品號", onchange:"onChangeItemCode()"});
	vat.formD.item(0, "F03", {type: "TEXT", size: 20, maxlength: 20,  desc: "品名", mode: "READONLY"});
	vat.formD.item(0, "F04", {type: "NUMB", size: 8, maxlength: 20,  desc: "數量", onchange:"calculateLineAmount()"});
	vat.formD.item(0, "F05", {type: "NUMB", size: 8, maxlength: 20,  desc: "單價(原幣)", onchange:"calculateLineAmount()"});
	vat.formD.item(0, "F06", {type: "NUMB", size: 8, maxlength: 20,  desc: "單價(台幣)", mode: "READONLY"});	
	vat.formD.item(0, "F07", {type: "NUMB", size: 8, maxlength: 20,  desc: "原幣合計", mode: "READONLY"});
	vat.formD.item(0, "F08", {type: "NUMB", size: 8, maxlength: 20,  desc: "台幣合計", mode: "READONLY"});
	vat.formD.item(0, "F09", {type: "NUMB", size: 8, maxlength: 20,  desc: "分攤費用", mode: "READONLY"});
	
	if( orderTypeCodeTmp == "IRF" ){
		vat.formD.item(0, "F10", {type: "TEXT", size: 8, maxlength: 20,  desc: "Invoice No."});
		vat.formD.item(0, "F11", {type: "TEXT", size: 8, maxlength: 20,  desc: "採購單單號", mode:"HIDDEN"});
	}
	else{
		vat.formD.item(0, "F10", {type: "TEXT", size: 8, maxlength: 20,  desc: "Invoice No.", mode:"HIDDEN"});
		vat.formD.item(0, "F11", {type: "TEXT", size: 8, maxlength: 20,  desc: "採購單單號"});
	}
	
	vat.formD.item(0, "F12", {type:"ROWID"});
	vat.formD.item(0, "F13", {type:"RADIO", desc:"狀態", mode:"HIDDEN"});
	vat.formD.item(0, "F14", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.formD.item(0, "F15", {type:"DEL", desc:"刪除"});
	vat.formD.item(0, "F16", {type:"MSG", desc:"訊息"});
	
	vat.formD.pageLayout(0, {pageSize:10, canDataDelete:varCanDataDelete, canDataAppend:varCanDataAppend, canDataModify:varCanDataModify, beginService:"", closeService:"", itemMouseinService:"", itemMouseoutService:"", appendBeforeService:"appendBeforeService()", appendAfterService:"appendAfterService()", deleteBeforeService:"", deleteAfterService:"",pageUpTopService:"", pageUpBeforeService:"", pageUpAfterService:"", pageDnBeforeService:"", pageDnAfterService:"", pageDnBottomService:"", loadBeforeAjxService:"loadBeforeAjxService()", loadSuccessAfter:"loadSuccessAfter()", loadFailureAfter:"", saveBeforeAjxService:"saveBeforeAjxService()", saveSuccessAfter:"saveSuccessAfter()", saveFailureAfter:""});
	vat.formD.pageDataLoad(0, 1);
}

function onChangeItemCode() {
	var nItemLine = vat.formD.itemIndexNo();
	var sItemCode = vat.formD.itemDataGet(nItemLine, "itemCode");
	vat.formD.itemDataBind(0,nItemLine, "itemCName", "");
	vat.formD.itemDataBind(0,nItemLine, "quantity", 0);
	vat.formD.itemDataBind(0,nItemLine, "foreignUnitPrice", 0);	
	vat.formD.itemDataBind(0,nItemLine, "localUnitPrice", 0);
	vat.formD.itemDataBind(0,nItemLine, "foreignAmount", 0);
	vat.formD.itemDataBind(0,nItemLine, "localAmount", 0);
	vat.formD.itemDataBind(0,nItemLine, "expenseApportionmentAmount", 0);	
	if (sItemCode != "") {
		var processString = "process_object_name=imReceiveHeadService&process_object_method_name=getAJAXLineData&brandCode=" + document.forms[0]["#form.brandCode"].value + "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value + "&itemCode=" + sItemCode;
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					vat.formD.itemDataBind(0,nItemLine, "itemCName", vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText));
				}
			}
		});
	} else {
	}
}

//計算LINE合計的部份
function calculateLineAmount() {	
	var nItemLine = vat.formD.itemIndexNo();
	//alert( "receive.calculateLineAmount nItemLine=" + nItemLine) ;	
	var quantity = vat.formD.itemDataGet(nItemLine,"quantity");
	var foreignUnitPrice = vat.formD.itemDataGet(nItemLine,"foreignUnitPrice");	
	var exchangeRate = document.forms[0]["#form.exchangeRate"].value ;
	//alert(exchangeRate);
	//alert("exchangeRate -> " + exchangeRate) ;
	var localUnitPrice = parseFloat(foreignUnitPrice) * parseFloat(exchangeRate) ;
	var foreignAmount = parseFloat(quantity) * parseFloat(foreignUnitPrice) ;
	var localAmount = foreignAmount * parseFloat(exchangeRate) ;
	 
	vat.formD.itemDataBind(0,nItemLine, "localUnitPrice", localUnitPrice );
	vat.formD.itemDataBind(0,nItemLine, "foreignAmount", foreignAmount );
	vat.formD.itemDataBind(0,nItemLine, "localAmount", localAmount );
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	//alert( "checkDisableLine" ) ;
	//var headId = document.forms[0]["#form.headId"].value;
	//alert( "head Id=" + headId + " length=" + headId.length ) ;
	//if (null != headId && "0" != headId && headId.length > 0) {
	//	vat.form.item.enable("vatDetailDiv");    //LINE 設定為Enable
	//	return true;
	//} else {
	//	vat.form.item.disable("vatDetailDiv");    //LINE 設定為ReadOnly
	//	return false;
	//}
	return true ;
}

/*
	載入LINE資料
*/
function loadBeforeAjxService() {
	var processString = "process_object_name=imReceiveHeadService&process_object_method_name=getAJAXPageData" + "&headId=" + document.forms[0]["#form.headId"].value;
	return processString;
}

/*
	載入LINE資料 SUCCESS
*/
function loadSuccessAfter() {
	setTimeout ( "setDefaultPO()" , 300 );
}

/*
	SAVE BEFORE AJAX
*/
function saveBeforeAjxService() {
	var processString = "" ;
	var exchangeRate = 0 ; 
	if (checkEnableLine()) {
		exchangeRate = document.forms[0]["#form.exchangeRate"].value;
		processString = "process_object_name=imReceiveHeadService&process_object_method_name=updateAJAXPageLinesData" + "&headId=" + document.forms[0]["#form.headId"].value + "&exchangeRate=" + exchangeRate ;
	}
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter(){
	//alert("saveSuccessAfter ->" + afterSavePageProcess );
	vat.formD.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg == "") {
		if( "saveHandler" == afterSavePageProcess ) {	
			//save head		
			executeCommandHandler("main", "saveHandler");
		}else if( "submitHandler" == afterSavePageProcess ){
			//save head	
			executeCommandHandler("main", "submitHandler");
		}else if("totalCount" == afterSavePageProcess){
			//加上要計算的HEA 欄位
			var headId = document.forms[0]["#form.headId"].value ;
			var taxType = document.forms[0]["#form.taxType"].value ;			
			var taxRate = document.forms[0]["#form.taxRate"].value ;			
			var exchangeRate = document.forms[0]["#form.exchangeRate"].value ;
			//計算所有費用合計
			var expenseLocalAmount = document.forms[0]["#form.expenseLocalAmount"].value ;
			var expenseForeignAmount = document.forms[0]["#form.expenseForeignAmount"].value ;
			
			var processString = "process_object_name=imReceiveHeadService&process_object_method_name=updateAJAXHeadTotalAmount&headId=" + headId + "&taxType=" + taxType + "&exchangeRate=" + exchangeRate + "&expenseForeignAmount=" + expenseForeignAmount + "&expenseLocalAmount=" + expenseLocalAmount + "&taxRate=" + taxRate ;
			vat.ajax.startRequest(processString, function () {
				if (vat.ajax.handleState()) {
					if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
						document.forms[0]["#form.totalLocalPurchaseAmount"].value = vat.ajax.getValue("TotalLocalPurchaseAmount", vat.ajax.xmlHttp.responseText) ;
						document.forms[0]["#form.totalForeignPurchaseAmount"].value = vat.ajax.getValue("TotalForeignPurchaseAmount", vat.ajax.xmlHttp.responseText) ;
						document.forms[0]["#form.expenseLocalAmount"].value = vat.ajax.getValue("ExpenseLocalAmount", vat.ajax.xmlHttp.responseText) ;
						document.forms[0]["#form.expenseForeignAmount"].value = vat.ajax.getValue("ExpenseForeignAmount", vat.ajax.xmlHttp.responseText) ;
						document.forms[0]["#form.taxAmount"].value = vat.ajax.getValue("TaxAmount", vat.ajax.xmlHttp.responseText) ;						
					}
				}
			});						
			vat.formD.pageRefresh(0);
		}else if ("afterExport" == afterSavePageProcess) {
			executeCommandHandlerNoBlock("main","exportDataHandler");
		}
	}else {
		alert("\u932f\u8aa4\u8a0a\u606f " + errorMsg);
	}
	afterSavePageProcess = "" ;	
}

/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
	//alert( "doSaveHandler" );
	if (confirm("確認是否送出?")) {
		if(checkEnableLine()) {
			//save line
			vat.formD.pageDataSave();
			afterSavePageProcess = "saveHandler" ;
		}
	}
}
/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	if (confirm("確認是否送出?")) {
		if(checkEnableLine()) {
			//save line
			vat.formD.pageDataSave();
			afterSavePageProcess = "submitHandler" ;
		}
	}
}

function appendBeforeService() {
	//alert("appendBeforeService()");
	return true;
}
function appendAfterService() {
	//alert("appendAfterService()");
	//loadBeforeAjxService();
}

/*
	顯示合計的頁面
*/
function showTotalCountPage(){
	if(checkEnableLine()) {
		afterSavePageProcess = "totalCount" ;
		countExpenseAmount();
		vat.formD.pageDataSave(0);		
	}	
}

/*
	變更匯率
*/
function changeCurrenceCode(){
	var processString = "process_object_name=imReceiveHeadService&process_object_method_name=getAJAXExchangeRateByCurrencyCode&currencyCode=" + document.forms[0]["#form.currencyCode"].value + "&organizationCode=TM&orderDate=" + document.forms[0]["#form.orderDate"].value ;
	vat.ajax.startRequest(processString,  function() { 
  		if (vat.ajax.handleState()){
    		document.forms[0]["#form.exchangeRate"].value =  vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText);
    		showTotalCountPage();
  		}
	} );
}

/*
	變更供應商
*/
function changeSupplierCode(){
	var processString = "process_object_name=imReceiveHeadService&process_object_method_name=getAJAXFormDataBySupplierCode&supplierCode=" + document.forms[0]["#form.supplierCode"].value + "&organizationCode=TM&brandCode=" + document.forms[0]["#form.brandCode"].value + "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value + "&orderDate=" + document.forms[0]["#form.orderDate"].value ;
	vat.ajax.startRequest(processString,  function() { 
		if (vat.ajax.handleState()){
    		document.forms[0]["#form.supplierName"].value =  vat.ajax.getValue("SupplierName", vat.ajax.xmlHttp.responseText);
    		document.forms[0]["#form.countryCode"].value =  vat.ajax.getValue("CountryCode", vat.ajax.xmlHttp.responseText);
    		document.forms[0]["#form.currencyCode"].value =  vat.ajax.getValue("CurrencyCode", vat.ajax.xmlHttp.responseText);
    		document.forms[0]["#form.exchangeRate"].value =  vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText);
    		document.forms[0]["#form.tradeTeam"].value =  vat.ajax.getValue("TradeTeam", vat.ajax.xmlHttp.responseText);
    		document.forms[0]["#form.paymentTermCode"].value =  vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText);
    		showTotalCountPage();
  		}
	} );
}

/*
	匯出
*/
function doExport() {
	//alert("doExport");
	if (checkEnableLine()) {
		//save line
		afterSavePageProcess = "afterExport";
		vat.formD.pageDataSave(0);
	}
}

function countExpenseAmount(){
	var foreignAmountTotal = 0 ;
	var localAmountTotal = 0 ;
	//金額合計
	for(index = 0 ; index < 1000 ; index++){
		if( null != document.forms[0]["#form.imReceiveExpenses["+index+"].localAmount"] ){ 
			localAmountTotal = localAmountTotal + parseFloat(document.forms[0]["#form.imReceiveExpenses["+index+"].localAmount"].value) ;
			foreignAmountTotal = foreignAmountTotal + parseFloat(document.forms[0]["#form.imReceiveExpenses["+index+"].foreignAmount"].value) ;
		}else{
			document.forms[0]["#form.expenseLocalAmount"].value = localAmountTotal  ;
			document.forms[0]["#form.expenseForeignAmount"].value = foreignAmountTotal ;
			break ;
		}
	}
}

function setDefaultPO(){
	for(index = vat.formD.page[0].firstIndex ; index < vat.formD.page[0].firstIndex + 10 ; index++){		
		if( "" == vat.formD.itemDataGet(index,"poOrderNo") ){
			vat.formD.itemDataBind(0,index,"poOrderNo", document.forms[0]["#form.defPoOrderNo"].value ) ; 
		}		
	}
}

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
	vat.formD.pageDataSave(0);
}