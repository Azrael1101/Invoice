
var afterSavePageProcess = "";

//頁面控制
//pageControl();
/*
	initial 
*/
function detailInitial() {
	var statusTmp = document.forms[0]["#form.status"].value;
	var varCanDataDelete = false;
	var varCanDataAppend = false;
	var varCanDataModify = false;
	if( statusTmp == "SAVE" || statusTmp == "REJECT" ){
		varCanDataDelete = true;
		varCanDataAppend = true;
		varCanDataModify = true;		
	}
	vat.formD.item(0, "F01", {type:"IDX", desc:"序"});
	vat.formD.item(0, "F02", {type:"TEXT", size:16, maxLen:20,  desc:"品號", onchange:"onChangeItemCode()"});
	vat.formD.item(0, "F03", {type:"TEXT", size:20, maxLen:20,  desc:"品名", mode:"READONLY"});
	vat.formD.item(0, "F04", {type:"TEXT", size:8, maxLen:20, desc:"庫存數量", mode:"READONLY"});
	vat.formD.item(0, "F05", {type:"TEXT", size:8, maxLen:20,  desc:"台幣零售價", mode:"READONLY"});
	vat.formD.item(0, "F06", {type:"TEXT", size:8, maxLen:20,  desc:"進貨成本(NT)", onchange:"calculateLineAmount()"});
	vat.formD.item(0, "F07", {type:"NUMB", size:8, maxLen:20,  desc:"數量", onchange:"calculateLineAmount()"});
	vat.formD.item(0, "F08", {type:"NUMB", size:8, maxLen:20,  desc:"原幣單價", mode:"HIDDEN"});
	vat.formD.item(0, "F09", {type:"NUMB", size:8, maxLen:20,  desc:"原幣總價", mode:"HIDDEN"});
	vat.formD.item(0, "F10", {type:"NUMB", size:8, maxLen:20,  desc:"台幣總價", mode:"READONLY"});
	vat.formD.item(0, "F11", {type:"NUMB", size:8, maxLen:20,  desc:"實際應到貨數量", mode:"HIDDEN"});
	vat.formD.item(0, "F12", {type:"ROWID"});
	vat.formD.item(0, "F13", {type:"RADIO", desc:"狀態", mode:"HIDDEN"});
	vat.formD.item(0, "F14", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.formD.item(0, "F15", {type:"DEL", desc:"刪除"});
	vat.formD.item(0, "F16", {type:"MSG", desc:"訊息"});
	vat.formD.pageLayout(0, {pageSize:10, canDataDelete:varCanDataDelete, canDataAppend:varCanDataAppend, canDataModify:varCanDataModify, beginService:"", closeService:"", itemMouseinService:"", itemMouseoutService:"", appendBeforeService:"appendBeforeService()", appendAfterService:"appendAfterService()", deleteBeforeService:"", deleteAfterService:"", pageUpTopService:"", pageUpBeforeService:"", pageUpAfterService:"", pageDnBeforeService:"", pageDnAfterService:"", pageDnBottomService:"", loadBeforeAjxService:"loadBeforeAjxService()", loadSuccessAfter:"loadSuccessAfter()", loadFailureAfter:"", saveBeforeAjxService:"saveBeforeAjxService()", saveSuccessAfter:"saveSuccessAfter()", saveFailureAfter:""});
	vat.formD.pageDataLoad(0, 1);
}
/*
	LINE ITEM CODE CHANGE
*/
function onChangeItemCode() {
	//alert("execute onChangeItemCode s");
	var nItemLine = vat.formD.itemIndexNo();
	//alert( "test ->" + nItemLine ) ;
	//alert("be nItemLine=" + nItemLine );
	var sItemCode = vat.formD.itemDataGet(nItemLine, "itemCode");
	//alert("af nItemLine=" + nItemLine+ ",sItemCode=" + sItemCode);
	//alert("testPO.item:"+ sItemCode + "/current line:"+nItemLine+"/current:"+vat.form.item.current);
	vat.formD.itemDataBind(0, nItemLine, "unitPrice", 0);
	vat.formD.itemDataBind(0, nItemLine, "foreignUnitCost", 0);
	vat.formD.itemDataBind(0, nItemLine, "lastForeignUnitCost", 0);
	vat.formD.itemDataBind(0, nItemLine, "itemCName", "");
	vat.formD.itemDataBind(0, nItemLine, "onHandQty", 0);
	vat.formD.itemDataBind(0, nItemLine, "quantity", 0);
	vat.formD.itemDataBind(0, nItemLine, "foreignPurchaseAmount", 0);
	vat.formD.itemDataBind(0, nItemLine, "unitPriceAmount", 0);
	vat.formD.itemDataBind(0, nItemLine, "actualPurchaseQuantity", 0);
	if (sItemCode != "") {
		var processString = "process_object_name=poPurchaseOrderHeadService&process_object_method_name=getAJAXLineData&brandCode=" + document.forms[0]["#form.brandCode"].value + "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value + "&itemCode=" + sItemCode;
		//alert(processString);
		//alert("execute onChangeItemCode " + sItemCode);										   
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					vat.formD.itemDataBind(0, nItemLine, "unitPrice", vat.ajax.getValue("UnitPrice", vat.ajax.xmlHttp.responseText));
					vat.formD.itemDataBind(0, nItemLine, "foreignUnitCost", vat.ajax.getValue("ForeignUnitCost", vat.ajax.xmlHttp.responseText));
					vat.formD.itemDataBind(0, nItemLine, "lastForeignUnitCost", vat.ajax.getValue("LastForeignUnitCost", vat.ajax.xmlHttp.responseText));
					vat.formD.itemDataBind(0, nItemLine, "itemCName", vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText));
					vat.formD.itemDataBind(0, nItemLine, "onHandQty", vat.ajax.getValue("onHandQty", vat.ajax.xmlHttp.responseText));
					vat.formD.itemDataBind(0, nItemLine, "lastLocalUnitCost", vat.ajax.getValue("LastLocalUnitCost", vat.ajax.xmlHttp.responseText));
				}
				//calculateTotalAmount();
				//checkCloseHead();
			}
		});
	} else {
	}
	
	//alert("execute onChangeItemCode e");
}
/*
	計算 單筆 LINE 合計的部份
*/
function calculateLineAmount() {
	//alert("execute calculateLineAmount s");
	var nItemLine = vat.formD.itemIndexNo();
	var quantity = vat.formD.itemDataGet(nItemLine, "quantity");
	var foreignUnitCost = vat.formD.itemDataGet(nItemLine, "foreignUnitCost");
	var unitPrice = vat.formD.itemDataGet(nItemLine, "unitPrice");
	var exchangeRate = document.forms[0]["#form.exchangeRate"].value;
	var foreignPurchaseAmount = parseFloat(quantity) * parseFloat(foreignUnitCost);
	var unitPriceAmount = parseFloat(quantity) * parseFloat(unitPrice);
	vat.formD.itemDataBind(0, nItemLine, "foreignPurchaseAmount", foreignPurchaseAmount);
	vat.formD.itemDataBind(0, nItemLine, "unitPriceAmount", unitPriceAmount);
	//alert("execute calculateLineAmount e");
}
/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	//alert( "checkDisableLine" ) ;
	//var headId = document.forms[0]["#form.headId"].value;
	//alert( "head Id=" + headId + " length=" + headId.length ) ;
	//if (null != headId && "0" != headId && headId.length > 0) {
	//	vat.form.item.enable("vatDetailDiv");	//LINE 設定為Enable
	//	return true;
	//} else {
	//	vat.form.item.disable("vatDetailDiv");  //LINE 設定為ReadOnly
	//	return false;
	//}
	return true ;
}
/*
	載入LINE資料
*/
function loadBeforeAjxService() {
	//alert("execute loadBeforeAjxService s");
	var processString = "process_object_name=poPurchaseOrderHeadService&process_object_method_name=getAJAXPageData&headId=" + document.forms[0]["#form.headId"].value;
	//alert("return " + processString);
	return processString;
}
/*
	載入LINE資料 SUCCESS
*/
function loadSuccessAfter() {
	//alert("execute loadSuccessAfter ");
}
/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
	var processString = "";
	var exchangeRate = 0;
	if (checkEnableLine()) {
		exchangeRate = document.forms[0]["#form.exchangeRate"].value;
		processString = "process_object_name=poPurchaseOrderHeadService&process_object_method_name=updateAJAXPageLinesData" + "&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value + "&brandCode=" + document.forms[0]["#form.brandCode"].value  + "&exchangeRate=" + exchangeRate + "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value ;
	}
	return processString;
}
/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
	vat.formD.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg == "") {
		if ("saveHandler" == afterSavePageProcess) {	
			executeCommandHandler("main", "saveHandler");
		} else if ("submitHandler" == afterSavePageProcess) {			 
			executeCommandHandler("main", "submitHandler");
		} else if ("totalCount" == afterSavePageProcess) {
			//加上要計算的HEA 欄位
			var taxType = document.forms[0]["#form.taxType"].value;
			var exchangeRate = document.forms[0]["#form.exchangeRate"].value;
			var purchaseType = document.forms[0]["#form.purchaseType"].value;
			var processString = "process_object_name=poPurchaseOrderHeadService&process_object_method_name=getAJAXHeadTotalAmount&headId=" + document.forms[0]["#form.headId"].value + "&taxType=" + taxType + "&exchangeRate=" + exchangeRate + "&purchaseType=" + purchaseType ;
			vat.ajax.startRequest(processString, function () {
				if (vat.ajax.handleState()) {
					document.forms[0]["#form.totalLocalPurchaseAmount"].value = vat.ajax.getValue("TotalLocalPurchaseAmount", vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalForeignPurchaseAmount"].value = vat.ajax.getValue("TotalForeignPurchaseAmount", vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.taxAmount"].value = vat.ajax.getValue("TaxAmount", vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalProductCounts"].value = vat.ajax.getValue("TotalProductCounts", vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalBudget"].value = vat.ajax.getValue("TotalBudget", vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalAppliedBudget"].value = vat.ajax.getValue("TotalAppliedBudget", vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalRemainderBudget"].value = vat.ajax.getValue("TotalRemainderBudget", vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalUnitPriceAmount"].value = vat.ajax.getValue("TotalUnitPriceAmount", vat.ajax.xmlHttp.responseText);
				}
			});
		} else if ("afterExport" == afterSavePageProcess) {
			vat.formD.pageRefresh(0);
			executeCommandHandlerNoBlock("main","exportDataHandler");
		}				
	} else {
		alert("\u932f\u8aa4\u8a0a\u606f " + errorMsg);
	}
}
/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
	//alert("doSaveHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			vat.formD.pageDataSave();
			afterSavePageProcess = "saveHandler";
		}
	}
}
/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	//alert("doSubmitHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			vat.formD.pageDataSave();
			afterSavePageProcess = "submitHandler";
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
function showTotalCountPage() {
	if (checkEnableLine()) {
		afterSavePageProcess = "totalCount";
		vat.formD.pageDataSave(0);
	}
}

/*
設定供應商資料
*/
function onChangeSupplierData() {
	var processString = "process_object_name=poPurchaseOrderHeadService&process_object_method_name=getAJAXFormDataBySupplierCode&supplierCode=" + document.forms[0]["#form.supplierCode"].value + "&organizationCode=TM&brandCode=" + document.forms[0]["#form.brandCode"].value + "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			document.forms[0]["#form.supplierName"].value = vat.ajax.getValue("SupplierName", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.countryCode"].value = vat.ajax.getValue("CountryCode", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.currencyCode"].value = vat.ajax.getValue("CurrencyCode", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.taxType"].value = vat.ajax.getValue("TaxType", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.taxRate"].value = vat.ajax.getValue("TaxRate", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.exchangeRate"].value = vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText);
    		//document.forms[0]["#form.tradeTeam"].value =  vat.ajax.getValue("TradeTeam", vat.ajax.xmlHttp.responseText);
    		document.forms[0]["#form.paymentTermCode"].value =  vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText);
			showTotalCountPage();
		}
	});
}

/*
function pageControl(){
	//alert("test");
	var orderNoTmp = document.forms[0]["#form.orderNo"].value ;
	//alert("test1");
	var statusTmp = document.forms[0]["#form.status"].value ;
	var processIdTmp = document.forms[0]["#processId"].value ;
	var user_session_employeeCodeTmp = document.forms[0]["#user.employeeCode"].value  ;
	var user_employeeCodeTmp = document.forms[0]["#form.createdBy"].value ;

           if(statusTmp == "SAVE" || statusTmp == "REJECT"  || statusTmp == "[binding]" ){
			if( statusTmp == "SAVE" ){	
				if (orderNoTmp.length > 0 && orderNoTmp.indexOf("TMP") < 0){ 
					//不從待辦事項進入  
					if(!(processIdTmp != 0 && processIdTmp != null )){ 
						vat.form.item.none("vatSubmitBottonDiv");    //Hidden 送出按鍵
						vat.form.item.none("vatSaveBottonDiv");       //Hidden 暫存按鍵
						vat.form.item.none("vatVoidBottonDiv");        //Hidden 作廢按鍵
						 //alert("如果您是要修改此單據請由工作項目中進入,感謝您的合作");
						vat.form.item.none("vatCalculateBottonDiv");  //Hidden 計算

                    				vat.form.item.readonly("vatMasterTopDiv");    //單頭 設定為ReadOnly          
                     				vat.form.item.readonly("vatMasterDiv");    //單頭 設定為ReadOnly          
					}
				}else{
					vat.form.item.none("vatVoidBottonDiv");        //Hidden 作廢按鍵
				}
			}
           }else{ //非暫存及駁回
                     vat.form.item.readonly("vatMasterTopDiv");    //單頭 設定為ReadOnly          
                     vat.form.item.readonly("vatMasterDiv");    //單頭 設定為ReadOnly          
                     vat.form.item.readonly("vatItemDiv");       //單身 設定為ReadOnly
                     vat.form.item.none("vatSaveBottonDiv");  //Hidden 暫存按鍵
                     vat.form.item.none("vatVoidBottonDiv");   //Hidden 作廢按鍵
		     vat.form.item.none("vatCopyBottonDiv");   //Hidden COPY按鍵
                     if(processIdTmp != 0 &&
                        processIdTmp != null ){ //從待辦事項進入     
                                vat.form.item.none("vatNewBottonDiv");   //Hidden 新增按鍵
                                vat.form.item.none("vatSearchBottonDiv");         //Hidden 查詢按鍵
                     }else{ //使用Picker或從查詢功能選入
                                if(statusTmp == "FINISH" ||
                                   statusTmp == "SIGNING" ||
                                   statusTmp == "VOID"){
                                           vat.form.item.none("vatSubmitBottonDiv");         //Hidden 送出按鍵

					   vat.form.item.none("vatCalculateBottonDiv");  //Hidden 計算
                                }
                     }
           }

	if( ! orderNoTmp.length > 0 ){
		vat.form.item.none("vatReportBottonDiv");	
	}

	//checkEnableLine();
}
*/

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
	vat.formD.pageDataSave(0);
}