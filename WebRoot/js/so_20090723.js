/*
	initial 
*/
var afterSavePageProcess = "";
var afterSavePaymentPageProcess = "";

function detailInitial(){
    var statusTmp = document.forms[0]["#form.status"].value;
    var orderTypeCodeTmp = document.forms[0]["#form.orderTypeCode"].value;
    var orderNoTmp = document.forms[0]["#form.orderNo"].value;
    var processId = document.forms[0]["#processId"].value;
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	var varCanGridModify = false;
	if(orderTypeCodeTmp == "SOP"){
	    if(statusTmp == "SAVE" || statusTmp == "UNCONFIRMED"){
		    varCanGridDelete = true;
		    varCanGridAppend = true;
		    varCanGridModify = true;		
	    }
	}else{
	    if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){
		    varCanGridDelete = true;
		    varCanGridAppend = true;
		    varCanGridModify = true;		
	    }	
	}
	// set column
	var vnA_Detail = 2;
    vat.item.make(vnA_Detail, "indexNo", {type:"IDX" , desc:"序號"});
	vat.item.make(vnA_Detail, "itemCode", {type:"TEXT", size:15, maxLen:20, desc:"品號", mask:"CCCCCCCCCCCC", onchange:"changeItemData(1)"});
	vat.item.make(vnA_Detail, "itemCName", {type:"TEXT", size:18, maxLen:20, desc:"品名", mode:"READONLY"});
	vat.item.make(vnA_Detail, "warehouseCode", {type:"TEXT", size:12, maxLen:12, desc:"庫別", mode:"READONLY"});
	vat.item.make(vnA_Detail, "warehouseName", {type:"TEXT", size:20, maxLen:20, desc:"庫名", mode:"HIDDEN"});
	vat.item.make(vnA_Detail, "originalUnitPrice", {type:"NUMB", size: 8, maxLen:12, desc:"單價", mode:"READONLY"});
	vat.item.make(vnA_Detail, "actualUnitPrice", {type:"NUMB", size: 8, maxLen:12, desc:"折扣後單價", mode:"READONLY"});
	vat.item.make(vnA_Detail, "currentOnHandQty", {type:"NUMB", size: 8, maxLen:12, desc:"庫存量", mode:"READONLY"});
	vat.item.make(vnA_Detail, "quantity", {type:"NUMB", size: 8, maxLen: 8, desc:"數量", onchange:"changeItemData(2)"});
	vat.item.make(vnA_Detail, "originalSalesAmount", {type:"NUMB", size: 8, maxLen:20, desc:"金額", mode:"READONLY"});
	vat.item.make(vnA_Detail, "actualSalesAmount", {type:"NUMB", size: 8, maxLen:20, desc:"折扣後金額", mode:"READONLY"});
	vat.item.make(vnA_Detail, "deductionAmount", {type:"NUMB", size: 1, maxLen: 8, desc:"折讓金額", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "discountRate", {type:"NUMB", size: 1, maxLen: 8, desc:"折扣率", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "taxType", {type:"TEXT", size: 1, maxLen: 1, desc:"稅別", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "taxRate", {type:"NUMB", size: 1, maxLen: 8, desc:"稅率", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "isTax", {type:"TEXT", size: 1, maxLen: 1, desc:"是否含稅", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "promotionCode", {type:"TEXT", size: 1, maxLen: 1, desc:"活動代號", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "discountType", {type:"TEXT", size: 1, maxLen: 1, desc:"活動折扣類型", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "discount", {type:"NUMB", size: 1, maxLen: 1, desc:"活動折扣", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "vipPromotionCode", {type:"TEXT", size: 1, maxLen: 20, desc:"VIP類別代號", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "vipDiscountType", {type:"TEXT", size: 1, maxLen: 1, desc:"VIP折扣類型", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "vipDiscount", {type:"NUMB", size: 1, maxLen: 1, desc:"VIP折扣", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "watchSerialNo", {type:"TEXT", size: 1, maxLen: 20, desc:"手錶序號", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "depositCode", {type:"TEXT", size: 1, maxLen: 8, desc:"訂金單代號", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "isUseDeposit", {type:"TEXT", size: 1, maxLen: 1, desc:"訂金支付", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "isServiceItem", {type:"TEXT", size: 1, maxLen: 1, desc:"服務性商品", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "taxAmount", {type:"NUMB", size: 1, maxLen: 1, desc:"稅額", mode: "HIDDEN"});
	vat.item.make(vnA_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnA_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnA_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});
	vat.item.make(vnA_Detail, "message", {type:"MSG", desc:"訊息"});
	vat.item.make(vnA_Detail, "advanceInput", {type:"BUTTON", desc:"進階輸入", value:"進階輸入", src:"images/button_advance_input.gif", eClick:"advanceInput()"});

	vat.block.pageLayout(vnA_Detail, {	pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,														
							    beginService: "",
								closeService: "",
							    itemMouseinService  : "",
								itemMouseoutService : "",
							    appendBeforeService : "kweSoPageAppendBeforeMethod()",
							    appendAfterService  : "kweSoPageAppendAfterMethod()",
								deleteBeforeService : "",
								deleteAfterService  : "",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "kweSoPageLoadSuccess()",
								loadFailureAfter    : "",
								eventService        : "changeRelationData()",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()",
								saveFailureAfter    : ""
														});
	vat.block.pageDataLoad(vnA_Detail, vnCurrentPage = 1);	
}

function kweSoPageSaveMethod(){
						
}		  								

function kweSoPageSaveSuccess(){
	// alert("更新成功");
}

function loadBeforeAjxService(){
	var processString = "process_object_name=soSalesOrderService&process_object_method_name=getAJAXPageData" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value + 
	                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                    "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value +
	                    "&shopCode=" + document.forms[0]["#form.shopCode"].value +  
	                    "&defaultWarehouseCode=" + document.forms[0]["#form.defaultWarehouseCode"].value + 
	                    "&taxType=" + document.forms[0]["#form.taxType"].value +
	                    "&taxRate=" + document.forms[0]["#form.taxRate"].value +
	                    "&discountRate=" + document.forms[0]["#form.discountRate"].value +
	                    "&vipPromotionCode=" + document.forms[0]["#form.vipPromotionCode"].value +
	                    "&promotionCode=" + document.forms[0]["#form.promotionCode"].value +                   
	                    "&warehouseEmployee=" + document.forms[0]["#warehouseEmployee"].value +
	                    "&warehouseManager=" + document.forms[0]["#warehouseManager"].value +
	                    "&customerType=" + document.forms[0]["#form.customerType"].value +
	                    "&vipType=" + document.forms[0]["#form.vipTypeCode"].value +
	                    "&priceType=" + document.forms[0]["#priceType"].value +
	                    "&salesDate=" + document.forms[0]["#form.salesOrderDate"].value + 
	                    "&formStatus=" + document.forms[0]["#form.status"].value;
																					
	return processString;											
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	/*var formStatus = document.forms[0]["#form.status"].value;
	if (formStatus !== null && (formStatus == 'SAVE' || formStatus == 'REJECT' || formStatus == 'UNCONFIRMED')) {
		vat.form.item.enable("vatDetailDiv");	//LINE 設定為Enable
		return true;
	} else {
		vat.form.item.disable("vatDetailDiv");  //LINE 設定為ReadOnly
		return false;
	}*/
	return true;
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=soSalesOrderService&process_object_method_name=updateAJAXPageLinesData" + "&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value;
	}
	return processString;
}		  								

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
   
    if ("saveHandler" == afterSavePageProcess) {	
        doActualSaveHandler();
    } else if ("submitHandler" == afterSavePageProcess) {
		doActualSubmitHandler();
	} else if ("submitBgHandler" == afterSavePageProcess) {
	    doActualSubmitBgHandler();
    } else if ("voidHandler" == afterSavePageProcess) {
		doActualVoidHandler();
	} else if ("copyHandler" == afterSavePageProcess) {
		doActualCopyHandler();
	} else if ("executeExport" == afterSavePageProcess) {
		exportFormData();
	} else if ("totalCount" == afterSavePageProcess) {
		var processString = "process_object_name=soSalesOrderService&process_object_method_name=executeCountTotalAmount" + 
	                "&headId=" + document.forms[0]["#form.headId"].value + 
	                "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value +
	                "&shopCode=" + document.forms[0]["#form.shopCode"].value +  
	                "&priceType=" + document.forms[0]["#priceType"].value +     
	                "&customerCode=" + document.forms[0]["#customerCode_var"].value +
	                "&searchCustomerType=" + document.forms[0]["#searchCustomerType"].value +        
	                "&warehouseEmployee=" + document.forms[0]["#warehouseEmployee"].value +
	                "&warehouseManager=" + document.forms[0]["#warehouseManager"].value +        
	                "&salesDate=" + document.forms[0]["#form.salesOrderDate"].value + 
	                "&formStatus=" + document.forms[0]["#form.status"].value;
		vat.ajax.startRequest(processString, function () {
		    if (vat.ajax.handleState()) {
			    document.forms[0]["#form.totalOriginalSalesAmount"].value = vat.ajax.getValue("TotalOriginalSalesAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalDeductionAmount"].value = vat.ajax.getValue("TotalDeductionAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.taxAmount"].value = vat.ajax.getValue("TaxAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalOtherExpense"].value = vat.ajax.getValue("TotalOtherExpense", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalActualSalesAmount"].value = vat.ajax.getValue("TotalActualSalesAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalNoneTaxSalesAmount"].value = vat.ajax.getValue("TotalNoneTaxSalesAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalItemQuantity"].value = vat.ajax.getValue("TotalItemQuantity", vat.ajax.xmlHttp.responseText);				
			}
		});	
    }else if ("changeRelationData" == afterSavePageProcess) {
        var processString = "process_object_name=soSalesOrderService&process_object_method_name=updateItemRelationData" + 
	                "&headId=" + document.forms[0]["#form.headId"].value + 
	                "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value +
	                "&shopCode=" + document.forms[0]["#form.shopCode"].value +  
	                "&defaultWarehouseCode=" + document.forms[0]["#form.defaultWarehouseCode"].value + 
	                "&taxType=" + document.forms[0]["#form.taxType"].value +
	                "&taxRate=" + document.forms[0]["#form.taxRate"].value +
	                "&discountRate=" + document.forms[0]["#form.discountRate"].value +
	                "&vipPromotionCode=" + document.forms[0]["#form.vipPromotionCode"].value +
	                "&promotionCode=" + document.forms[0]["#form.promotionCode"].value +                   
	                "&warehouseEmployee=" + document.forms[0]["#warehouseEmployee"].value +
	                "&warehouseManager=" + document.forms[0]["#warehouseManager"].value +
	                "&customerType=" + document.forms[0]["#form.customerType"].value +
	                "&vipType=" + document.forms[0]["#form.vipTypeCode"].value +
	                "&priceType=" + document.forms[0]["#priceType"].value +
	                "&salesDate=" + document.forms[0]["#form.salesOrderDate"].value + 
	                "&formStatus=" + document.forms[0]["#form.status"].value;
	    vat.ajax.startRequest(processString, function () {
		    if (vat.ajax.handleState()) {
			    vat.block.pageRefresh(2);
		     }
		});	
    }
	
	afterSavePageProcess = "";
}

/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
    if (confirm("是否確認暫存?")) {		
	    //save line
	    afterSavePageProcess = "saveHandler";
		vat.block.pageSearch(2);				
	}
}

/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	if (confirm("是否確認送出?")) {		
		//save line
		afterSavePageProcess = "submitHandler";	
		vat.block.pageSearch(2);
	}
}

/*
	背景送出SUBMIT HEAD && LINE
*/
function doSubmitBgHandler() {
	if (confirm("是否確認背景送出?")) {		
		//save line
		afterSavePageProcess = "submitBgHandler";	
		vat.block.pageSearch(2);
	}
}

/*
	作廢VOID HEAD && LINE
*/
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		afterSavePageProcess = "voidHandler";	
		vat.block.pageSearch(2);			
	}
}

/*
	COPY HEAD && LINE
*/
function doCopyHandler() {
	if (confirm("是否進行複製?")) {		
		//save line
		afterSavePageProcess = "copyHandler";
		vat.block.pageSearch(2);		
	}
}

/*
	AntiConfirm
*/
function doAntiConfirmHandler() {
	if (confirm("是否執行反確認?")) {		
        execSubmitAction("UNCONFIRMED");		
	}
}

/*
	顯示合計的頁面
*/
function showTotalCountPage() {
    //save line
    afterSavePageProcess = "totalCount";
    doPageDataSave();	
}

/*
	匯出
*/
function doExport() {
	//save line
	afterSavePageProcess = "executeExport";
	doPageDataSave();	
}

/*
	匯入
*/
function doImport() {
	//save line
	afterSavePaymentPageProcess = "executeImport";
	vat.block.pageSearch(4);	
}

/*
	暫存 SAVE HEAD && LINE
*/
function doActualSaveHandler() { 
    afterSavePaymentPageProcess = "saveHandler";		
    vat.block.pageSearch(4);
}

/*
	送出SUBMIT HEAD && LINE
*/
function doActualSubmitHandler() {
    afterSavePaymentPageProcess = "submitHandler";	
	vat.block.pageSearch(4);	
}

/*
	背景送出SUBMIT HEAD && LINE
*/
function doActualSubmitBgHandler() {
    afterSavePaymentPageProcess = "submitBgHandler";	
	vat.block.pageSearch(4);	
}

/*
	作廢VOID HEAD && LINE
*/
function doActualVoidHandler() {
    afterSavePaymentPageProcess = "voidHandler";
	vat.block.pageSearch(4);	
}

/*
	COPY HEAD && LINE
*/
function doActualCopyHandler() {
    afterSavePaymentPageProcess = "copyHandler";
	vat.block.pageSearch(4);	
}

function doPageDataSave(){
    vat.block.pageSearch(2);
    vat.block.pageSearch(4);
}

function doPageRefresh(){
    vat.block.pageRefresh(2);
    vat.block.pageRefresh(4);
}

function doPageDataSaveForItem(){
    vat.block.pageRefresh(2);
    vat.block.pageSearch(4);
}

function doPageDataSaveForPayment(){
    vat.block.pageRefresh(4);
    vat.block.pageSearch(2);
}

function advanceInput(){
    
    var nItemLine = vat.item.getGridLine();
    var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
    var vWarehouseCode = vat.item.getGridValueByName("warehouseCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();	
	var vOriginalUnitPrice = vat.item.getGridValueByName("originalUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualUnitPrice = vat.item.getGridValueByName("actualUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vCurrentOnHandQty = vat.item.getGridValueByName("currentOnHandQty", nItemLine).replace(/^\s+|\s+$/, '');
	var vQuantity = vat.item.getGridValueByName("quantity", nItemLine).replace(/^\s+|\s+$/, '');
	var vOriginalSalesAmount = vat.item.getGridValueByName("originalSalesAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualSalesAmount = vat.item.getGridValueByName("actualSalesAmount", nItemLine).replace(/^\s+|\s+$/, '');	
	var vTaxType = vat.item.getGridValueByName("taxType", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxRate = vat.item.getGridValueByName("taxRate", nItemLine).replace(/^\s+|\s+$/, '');
	var vDeductionAmount = vat.item.getGridValueByName("deductionAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vDiscountRate = vat.item.getGridValueByName("discountRate", nItemLine).replace(/^\s+|\s+$/, '');
	var vPromotionCode = vat.item.getGridValueByName("promotionCode", nItemLine).replace(/^\s+|\s+$/, '');
	var vDiscountType = vat.item.getGridValueByName("discountType", nItemLine).replace(/^\s+|\s+$/, '');
	var vDiscount = vat.item.getGridValueByName("discount", nItemLine).replace(/^\s+|\s+$/, '');
	var vVipPromotionCode = vat.item.getGridValueByName("vipPromotionCode", nItemLine).replace(/^\s+|\s+$/, '');
	var vVipDiscountType = vat.item.getGridValueByName("vipDiscountType", nItemLine).replace(/^\s+|\s+$/, '');
	var vVipDiscount = vat.item.getGridValueByName("vipDiscount", nItemLine).replace(/^\s+|\s+$/, '');	
	var vWatchSerialNo = vat.item.getGridValueByName("watchSerialNo", nItemLine).replace(/^\s+|\s+$/, '');
	var vDepositCode = vat.item.getGridValueByName("depositCode", nItemLine).replace(/^\s+|\s+$/, '');
	var vIsUseDeposit = vat.item.getGridValueByName("isUseDeposit", nItemLine).replace(/^\s+|\s+$/, '');
	var vIsServiceItem = vat.item.getGridValueByName("isServiceItem", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxAmount = vat.item.getGridValueByName("taxAmount", nItemLine).replace(/^\s+|\s+$/, '');	
	var vLineId = vat.item.getGridValueByName("lineId", nItemLine).replace(/^\s+|\s+$/, '');	
	
	var obj = document.getElementById("vatBeginDiv");
	if (obj){
		obj.filters[0].enabled = true;
		obj.filters[0].opacity = 0.60; 
	}
	
	var returnData = window.showModalDialog(
		"So_SalesOrder:advanceInput:20090723.page"+
		"?headId=" + document.forms[0]["#form.headId"].value +
		"&brandCode=" + document.forms[0]["#form.brandCode"].value +
        "&priceType=" + document.forms[0]["#priceType"].value + 
        "&shopCode=" + document.forms[0]["#form.shopCode"].value +
        "&customerType=" + document.forms[0]["#form.customerType"].value +
        "&vipType=" + document.forms[0]["#form.vipTypeCode"].value +
        "&warehouseManager=" + document.forms[0]["#warehouseManager"].value +
        "&warehouseEmployee=" + document.forms[0]["#warehouseEmployee"].value +
        "&salesDate=" + document.forms[0]["#form.salesOrderDate"].value +
        "&status=" + document.forms[0]["#form.status"].value +
        "&itemIndexNo=" + nItemLine +                        
        "&itemCode=" + vItemCode +
        "&warehouseCode=" + vWarehouseCode +
        "&originalUnitPrice=" + vOriginalUnitPrice +
        "&actualUnitPrice=" + vActualUnitPrice +
        "&currentOnHandQty=" + vCurrentOnHandQty +
        "&quantity=" + vQuantity +
        "&originalSalesAmount=" + vOriginalSalesAmount +
        "&actualSalesAmount=" + vActualSalesAmount +
        "&taxType=" + vTaxType +
        "&taxRate=" + vTaxRate +
        "&deductionAmount=" + vDeductionAmount +
        "&discountRate=" + vDiscountRate +
        "&promotionCode=" + vPromotionCode +
        "&discountType=" + vDiscountType +
        "&discount=" + vDiscount +
        "&vipPromotionCode=" + vVipPromotionCode +
        "&vipDiscountType=" + vVipDiscountType +
        "&vipDiscount=" + vVipDiscount +
        "&watchSerialNo=" + vWatchSerialNo +  
        "&depositCode=" + vDepositCode +  
        "&isUseDeposit=" + vIsUseDeposit +  
        "&isServiceItem=" + vIsServiceItem +
        "&taxAmount=" + vTaxAmount +
        "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value +
        "&orderNo=" + document.forms[0]["#form.orderNo"].value +
        "&processId=" + document.forms[0]["#processId"].value +
        "&lineId=" + vLineId,
		"",
		"dialogHeight:400px;dialogWidth:1060px;dialogTop:250px;dialogLeft:100px;status:no;");
		
		if(typeof returnData !== "undefined" && returnData !== null && returnData !== ""){
		    var returnDataArray = returnData.split("{#}");
		    vat.item.setGridValueByName("itemCode", nItemLine, returnDataArray[0]);	    
		    vat.item.setGridValueByName("itemCName", nItemLine, returnDataArray[1]); 
            vat.item.setGridValueByName("warehouseCode", nItemLine, returnDataArray[2]); 
            vat.item.setGridValueByName("warehouseName", nItemLine, returnDataArray[3]); 
            vat.item.setGridValueByName("originalUnitPrice", nItemLine, returnDataArray[4]); 
            vat.item.setGridValueByName("actualUnitPrice", nItemLine, returnDataArray[5]); 
            vat.item.setGridValueByName("currentOnHandQty", nItemLine, returnDataArray[6]); 
            vat.item.setGridValueByName("quantity", nItemLine, returnDataArray[7]); 
            vat.item.setGridValueByName("originalSalesAmount", nItemLine, returnDataArray[8]); 
            vat.item.setGridValueByName("actualSalesAmount", nItemLine, returnDataArray[9]);  
            vat.item.setGridValueByName("deductionAmount", nItemLine, returnDataArray[10]);
            vat.item.setGridValueByName("discountRate", nItemLine, returnDataArray[11]);
            vat.item.setGridValueByName("taxType", nItemLine, returnDataArray[12]);
            vat.item.setGridValueByName("taxRate", nItemLine, returnDataArray[13]);    
            vat.item.setGridValueByName("promotionCode", nItemLine, returnDataArray[14]);
            vat.item.setGridValueByName("discountType", nItemLine, returnDataArray[15]);
            vat.item.setGridValueByName("discount", nItemLine, returnDataArray[16]);
            vat.item.setGridValueByName("vipPromotionCode", nItemLine, returnDataArray[17]);
            vat.item.setGridValueByName("vipDiscountType", nItemLine, returnDataArray[18]);
            vat.item.setGridValueByName("vipDiscount", nItemLine, returnDataArray[19]);
            vat.item.setGridValueByName("watchSerialNo", nItemLine, returnDataArray[20]);           
            vat.item.setGridValueByName("depositCode", nItemLine, returnDataArray[21]);
            vat.item.setGridValueByName("isUseDeposit", nItemLine, returnDataArray[22]);          
            vat.item.setGridValueByName("isServiceItem", nItemLine, returnDataArray[23]);
            vat.item.setGridValueByName("taxAmount", nItemLine, returnDataArray[24]);
		}
		
	obj.filters[0].enabled = false;
}

function kweSoPageLoadSuccess(){
	// alert("載入成功");	
}

function kweSoPageAppendBeforeMethod(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}



function kweSoPageAppendAfterMethod(){
	// return alert("新增完畢");
}

function changeTaxRate(){
    if(document.forms[0]["#form.taxType"].value == "1" || document.forms[0]["#form.taxType"].value == "2"){
        document.forms[0]["#form.taxRate"].value = "0.0";
    }else if(document.forms[0]["#form.taxType"].value == "3"){
        document.forms[0]["#form.taxRate"].value = "5.0";
    }else{
        document.forms[0]["#form.taxRate"].value = "";
    }
}

function changeSuperintendent(){
    document.forms[0]["#form.superintendentCode"].value = document.forms[0]["#form.superintendentCode"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#form.superintendentCode"].value !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value + 
                    "&employeeCode=" + document.forms[0]["#form.superintendentCode"].value,
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               document.forms[0]["#form.superintendentCode"].value =  vat.ajax.getValue("EmployeeCode", oXHR.responseText);
               document.forms[0]["#form.superintendentName"].value =  vat.ajax.getValue("EmployeeName", oXHR.responseText);
           }   
       });
    }else{
        document.forms[0]["#form.superintendentName"].value = "";
    }
}

function changeCasherCode(){
    document.forms[0]["#form.casherCode"].value = document.forms[0]["#form.casherCode"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#form.casherCode"].value !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value + 
                    "&employeeCode=" + document.forms[0]["#form.casherCode"].value,
           find: function changeCasherCodeRequestSuccess(oXHR){ 
               document.forms[0]["#form.casherCode"].value =  vat.ajax.getValue("EmployeeCode", oXHR.responseText);
               document.forms[0]["#form.casherName"].value =  vat.ajax.getValue("EmployeeName", oXHR.responseText);
           }   
       });
    }else{
        document.forms[0]["#form.casherName"].value = "";
    }
}

function changeCustomerCode(){
    afterSavePageProcess = "totalCount";
    vat.block.pageSearch(2);
    document.forms[0]["#customerCode_var"].value = document.forms[0]["#customerCode_var"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#customerCode_var"].value !== ""){
        vat.ajax.XHRequest(
       {
           post:"process_object_name=buCustomerWithAddressViewService"+
                    "&process_object_method_name=findCustomerByTypeForAJAX"+
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value + 
                    "&customerCode=" + document.forms[0]["#customerCode_var"].value +
                    "&searchCustomerType=" + document.forms[0]["#searchCustomerType"].value +
                    "&isEnable=",
           find: function changeCustomerCodeRequestSuccess(oXHR){ 
               document.forms[0]["#customerCode_var"].value = vat.ajax.getValue("CustomerCode_var", oXHR.responseText);
               document.forms[0]["#form.customerCode"].value = vat.ajax.getValue("CustomerCode", oXHR.responseText);
               document.forms[0]["#form.contactPerson"].value = vat.ajax.getValue("ContactPerson", oXHR.responseText);        
               document.forms[0]["#form.contactTel"].value = vat.ajax.getValue("ContactTel", oXHR.responseText);      
               document.forms[0]["#form.receiver"].value = vat.ajax.getValue("Receiver", oXHR.responseText);
               document.forms[0]["#form.countryCode"].value = vat.ajax.getValue("CountryCode", oXHR.responseText);
               document.forms[0]["#form.currencyCode"].value = vat.ajax.getValue("CurrencyCode", oXHR.responseText);
               document.forms[0]["#form.paymentTermCode"].value = vat.ajax.getValue("PaymentTermCode", oXHR.responseText);
               document.forms[0]["#form.invoiceCity"].value = vat.ajax.getValue("InvoiceCity", oXHR.responseText);     
               document.forms[0]["#form.invoiceArea"].value = vat.ajax.getValue("InvoiceArea", oXHR.responseText);
               document.forms[0]["#form.invoiceZipCode"].value = vat.ajax.getValue("InvoiceZipCode", oXHR.responseText);
               document.forms[0]["#form.invoiceAddress"].value = vat.ajax.getValue("InvoiceAddress", oXHR.responseText);
               document.forms[0]["#form.shipCity"].value = vat.ajax.getValue("ShipCity", oXHR.responseText);
               document.forms[0]["#form.shipArea"].value = vat.ajax.getValue("ShipArea", oXHR.responseText);
               document.forms[0]["#form.shipZipCode"].value = vat.ajax.getValue("ShipZipCode", oXHR.responseText);
               document.forms[0]["#form.shipAddress"].value = vat.ajax.getValue("ShipAddress", oXHR.responseText);
               document.forms[0]["#form.invoiceTypeCode"].value = vat.ajax.getValue("InvoiceTypeCode", oXHR.responseText);
               document.forms[0]["#form.taxType"].value = vat.ajax.getValue("TaxType", oXHR.responseText);
               document.forms[0]["#form.taxRate"].value = vat.ajax.getValue("TaxRate", oXHR.responseText);
               document.forms[0]["#form.guiCode"].value = vat.ajax.getValue("GuiCode", oXHR.responseText);
               document.forms[0]["#form.customerType"].value = vat.ajax.getValue("CustomerType", oXHR.responseText);
               document.forms[0]["#form.customerName"].value = vat.ajax.getValue("CustomerName", oXHR.responseText);
               document.forms[0]["#form.vipTypeCode"].value = vat.ajax.getValue("VipType", oXHR.responseText);
               if(document.forms[0]["#form.orderTypeCode"].value != "SOP"){
                   document.forms[0]["#form.vipPromotionCode"].value = vat.ajax.getValue("VipPromotionCode", oXHR.responseText);
               }else{
                   document.forms[0]["#form.vipPromotionCode"].value = "";
               }              
               changePromotionCode(); 
           }   
       });  
    }else{
        document.forms[0]["#form.customerCode"].value = "";
        document.forms[0]["#form.contactPerson"].value = "";        
        document.forms[0]["#form.contactTel"].value = "";       
        document.forms[0]["#form.receiver"].value = "";
        document.forms[0]["#form.countryCode"].value = "";
        document.forms[0]["#form.currencyCode"].value = "NTD";
        document.forms[0]["#form.paymentTermCode"].value = "";
        document.forms[0]["#form.invoiceCity"].value = "";     
        document.forms[0]["#form.invoiceArea"].value = "";
        document.forms[0]["#form.invoiceZipCode"].value = "";
        document.forms[0]["#form.invoiceAddress"].value = "";
        document.forms[0]["#form.shipCity"].value = "";
        document.forms[0]["#form.shipArea"].value = "";
        document.forms[0]["#form.shipZipCode"].value = "";
        document.forms[0]["#form.shipAddress"].value = "";
        document.forms[0]["#form.invoiceTypeCode"].value = "";
        document.forms[0]["#form.taxType"].value = "3";
        document.forms[0]["#form.taxRate"].value = "5.0";
        document.forms[0]["#form.guiCode"].value = "";
        document.forms[0]["#form.customerType"].value = "";
        document.forms[0]["#form.customerName"].value = "";   
        document.forms[0]["#form.vipTypeCode"].value = "";
        document.forms[0]["#form.vipPromotionCode"].value = "";
        changePromotionCode();     
    }
}

function changeShopCode(){
    document.forms[0]["#form.shopCode"].value = document.forms[0]["#form.shopCode"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#form.shopCode"].value !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=soSalesOrderService"+
                    "&process_object_method_name=getShopMachineForAJAX"+
                    "&shopCode=" + document.forms[0]["#form.shopCode"].value,                  
           find: function changeShopCodeRequestSuccess(oXHR){ 
               var tempShopMachine = vat.ajax.getValue("ShopMachine", oXHR.responseText);
               tempShopMachine = vat.utils.strTwoInputDArray("#form.posMachineCode", "", 'true', tempShopMachine);  
               vat.formD.itemSelectBind(tempShopMachine);
               var tempWarehouseCode = vat.ajax.getValue("DefaultWarehouseCode", oXHR.responseText).replace(/^\s+|\s+$/, '');
               var tempWarehouseManager = vat.ajax.getValue("WarehouseManager", oXHR.responseText).replace(/^\s+|\s+$/, '');
               if(tempWarehouseCode !== ""){
                   for (var i = 0; i < document.forms[0]["#form.defaultWarehouseCode"].length; i++) {
                       if(document.forms[0]["#form.defaultWarehouseCode"][i].value == tempWarehouseCode){
                           document.forms[0]["#form.defaultWarehouseCode"].value = tempWarehouseCode;
                           document.forms[0]["#warehouseManager"].value = tempWarehouseManager;
                           if(document.forms[0]["#warehouseManager"].value == ""){
                               alert("庫別：" + document.forms[0]["#form.defaultWarehouseCode"].value + "的倉管人員為空值！");
                           }
                           break;
                       }
                   }
               }
           }   
       });
    }
}

function changeDefaultWarehouseCode(){
    document.forms[0]["#form.defaultWarehouseCode"].value = document.forms[0]["#form.defaultWarehouseCode"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#form.defaultWarehouseCode"].value !== ""){
        vat.ajax.XHRequest(
        {
           post:"process_object_name=imWarehouseService"+
                    "&process_object_method_name=findByIdForAJAX"+
                    "&warehouseCode=" + document.forms[0]["#form.defaultWarehouseCode"].value,
           find: function changeDefaultWarehouseCodeRequestSuccess(oXHR){ 
               document.forms[0]["#warehouseManager"].value = vat.ajax.getValue("WarehouseManager", oXHR.responseText);
               document.forms[0]["#warehouseManager"].value = document.forms[0]["#warehouseManager"].value.replace(/^\s+|\s+$/, '');
               if(document.forms[0]["#warehouseManager"].value == ""){
                   alert("庫別：" + document.forms[0]["#form.defaultWarehouseCode"].value + "的倉管人員為空值！");
               }
           }
        });
    }else{
        document.forms[0]["#warehouseManager"].value = "";
    }
}

function changePromotionCode(){
    document.forms[0]["#form.promotionCode"].value = document.forms[0]["#form.promotionCode"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#form.promotionCode"].value !== ""){
        vat.ajax.XHRequest(
        {
           post:"process_object_name=imPromotionViewService"+
                    "&process_object_method_name=findPromotionCodeByPropertyForAJAX"+
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
                    "&promotionCode=" + document.forms[0]["#form.promotionCode"].value +
                    "&priceType=" + document.forms[0]["#priceType"].value + 
                    "&shopCode=" + document.forms[0]["#form.shopCode"].value +
                    "&customerType=" + document.forms[0]["#form.customerType"].value +
                    "&vipType=" + document.forms[0]["#form.vipTypeCode"].value +
                    "&salesDate=" + document.forms[0]["#form.salesOrderDate"].value,                      
           find: function changePromotionCodeRequestSuccess(oXHR){ 
               document.forms[0]["#form.promotionCode"].value =  vat.ajax.getValue("PromotionCode", oXHR.responseText);
               document.forms[0]["#form.promotionName"].value =  vat.ajax.getValue("PromotionName", oXHR.responseText);
           }
        });
    }else{
        document.forms[0]["#form.promotionName"].value = "";
    }
}

function changeItemData(actionId) {
    var nItemLine = vat.item.getGridLine();
    var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vWarehouseCode = vat.item.getGridValueByName("warehouseCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vQuantity = vat.item.getGridValueByName("quantity", nItemLine).replace(/^\s+|\s+$/, '');
	var vDeductionAmount = vat.item.getGridValueByName("deductionAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vDiscountRate = vat.item.getGridValueByName("discountRate", nItemLine).replace(/^\s+|\s+$/, '');
	var vPromotionCode = vat.item.getGridValueByName("promotionCode", nItemLine).replace(/^\s+|\s+$/, '');
	var vVipPromotionCode = vat.item.getGridValueByName("vipPromotionCode", nItemLine).replace(/^\s+|\s+$/, '');
	var vOriginalUnitPrice = vat.item.getGridValueByName("originalUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');	
	var vTaxType = vat.item.getGridValueByName("taxType", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxRate = vat.item.getGridValueByName("taxRate", nItemLine).replace(/^\s+|\s+$/, '');
	
	vat.item.setGridValueByName("itemCode", nItemLine, vItemCode);
	vat.item.setGridValueByName("warehouseCode", nItemLine, vWarehouseCode);	
    if(document.forms[0]["#form.defaultWarehouseCode"].value !== ""){
        if(document.forms[0]["#warehouseManager"].value == ""){
            alert("主檔頁籤庫別：" + document.forms[0]["#form.defaultWarehouseCode"].value + "的倉管人員為空值！");
        }else{
            if(isNaN(vQuantity)){
               alert("明細資料頁籤中第" + nItemLine + "項明細的數量欄位必須為數值！");
            }else{
               vat.ajax.XHRequest(
               {
                   post:"process_object_name=soSalesOrderService" +
                            "&process_object_method_name=getAJAXItemData" +
                            "&brandCode=" + document.forms[0]["#form.brandCode"].value +
                            "&priceType=" + document.forms[0]["#priceType"].value + 
                            "&shopCode=" + document.forms[0]["#form.shopCode"].value +
                            "&customerType=" + document.forms[0]["#form.customerType"].value +
                            "&vipType=" + document.forms[0]["#form.vipTypeCode"].value +
                            "&itemIndexNo" + nItemLine +
                            "&itemCode=" + vItemCode +
                            "&warehouseCode=" + vWarehouseCode +
                            "&quantity=" + vQuantity +
                            "&deductionAmount=" + vDeductionAmount +
                            "&discountRate=" + vDiscountRate +
                            "&promotionCode=" + vPromotionCode +
                            "&vipPromotionCode=" + vVipPromotionCode +                         
                            "&warehouseManager=" + document.forms[0]["#warehouseManager"].value +
                            "&warehouseEmployee=" + document.forms[0]["#warehouseEmployee"].value +
                            "&originalUnitPrice=" + vOriginalUnitPrice +
                            "&salesDate=" + document.forms[0]["#form.salesOrderDate"].value +
                            "&taxType=" + vTaxType +
                            "&taxRate=" + vTaxRate +
                            "&actionId=" + actionId,
                                                  
                   find: function changeItemDataRequestSuccess(oXHR){
                       vat.item.setGridValueByName("itemCode", nItemLine, vat.ajax.getValue("ItemCode", oXHR.responseText));
                       vat.item.setGridValueByName("itemCName", nItemLine, vat.ajax.getValue("ItemCName", oXHR.responseText)); 
                       vat.item.setGridValueByName("warehouseCode", nItemLine, vat.ajax.getValue("WarehouseCode", oXHR.responseText)); 
                       vat.item.setGridValueByName("warehouseName", nItemLine, vat.ajax.getValue("WarehouseName", oXHR.responseText)); 
                       vat.item.setGridValueByName("originalUnitPrice", nItemLine, vat.ajax.getValue("OriginalUnitPrice", oXHR.responseText)); 
                       vat.item.setGridValueByName("actualUnitPrice", nItemLine, vat.ajax.getValue("ActualUnitPrice", oXHR.responseText)); 
                       vat.item.setGridValueByName("currentOnHandQty", nItemLine, vat.ajax.getValue("CurrentOnHandQty", oXHR.responseText)); 
                       vat.item.setGridValueByName("quantity", nItemLine, vat.ajax.getValue("Quantity", oXHR.responseText)); 
                       vat.item.setGridValueByName("originalSalesAmount", nItemLine, vat.ajax.getValue("OriginalSalesAmount", oXHR.responseText)); 
                       vat.item.setGridValueByName("actualSalesAmount", nItemLine, vat.ajax.getValue("ActualSalesAmount", oXHR.responseText));  
                       vat.item.setGridValueByName("deductionAmount", nItemLine, vat.ajax.getValue("DeductionAmount", oXHR.responseText));
                       vat.item.setGridValueByName("discountRate", nItemLine, vat.ajax.getValue("DiscountRate", oXHR.responseText));
                       vat.item.setGridValueByName("taxType", nItemLine, vat.ajax.getValue("TaxType", oXHR.responseText));
                       vat.item.setGridValueByName("taxRate", nItemLine, vat.ajax.getValue("TaxRate", oXHR.responseText));
                       vat.item.setGridValueByName("isTax", nItemLine, vat.ajax.getValue("IsTax", oXHR.responseText));
                       vat.item.setGridValueByName("promotionCode", nItemLine, vat.ajax.getValue("PromotionCode", oXHR.responseText));
                       vat.item.setGridValueByName("discountType", nItemLine, vat.ajax.getValue("DiscountType", oXHR.responseText));
                       vat.item.setGridValueByName("discount", nItemLine, vat.ajax.getValue("Disct", oXHR.responseText));
                       vat.item.setGridValueByName("vipPromotionCode", nItemLine, vat.ajax.getValue("VipPromotionCode", oXHR.responseText));
                       vat.item.setGridValueByName("vipDiscountType", nItemLine, vat.ajax.getValue("VipDiscountType", oXHR.responseText));
                       vat.item.setGridValueByName("vipDiscount", nItemLine, vat.ajax.getValue("VipDisct", oXHR.responseText));     
                       vat.item.setGridValueByName("isServiceItem", nItemLine, vat.ajax.getValue("IsServiceItem", oXHR.responseText));
                       vat.item.setGridValueByName("taxAmount", nItemLine, vat.ajax.getValue("TaxAmount", oXHR.responseText));
                   }
               });          
            }
        }
    }else{
	    alert("請先選擇主檔頁籤的庫別！");
	}
}

function changeAdvanceData(actionId){
    var vItemIndexNo = document.forms[0]["#itemIndexNo"].value;
    var vBrandCode = document.forms[0]["#brandCode"].value;
    var vPriceType = document.forms[0]["#priceType"].value;
    var vShopCode = document.forms[0]["#shopCode"].value;
    var vCustomerType = document.forms[0]["#customerType"].value;
    var vVipType = document.forms[0]["#vipType"].value;
    var vItemCode = document.forms[0]["#itemCode"].value.replace(/^\s+|\s+$/, '');
    var vWarehouseCode = document.forms[0]["#warehouseCode"].value.replace(/^\s+|\s+$/, '');
    var vQuantity = document.forms[0]["#quantity"].value.replace(/^\s+|\s+$/, '');
    var vDeductionAmount = document.forms[0]["#deductionAmount"].value.replace(/^\s+|\s+$/, '');
    var vDiscountRate = document.forms[0]["#discountRate"].value.replace(/^\s+|\s+$/, '');
    var vPromotionCode= document.forms[0]["#promotionCode"].value.replace(/^\s+|\s+$/, '');
	var vVipPromotionCode= document.forms[0]["#vipPromotionCode"].value;
	var vWarehouseManager= document.forms[0]["#warehouseManager"].value;
	var vWarehouseEmployee= document.forms[0]["#warehouseEmployee"].value;
    //var vOriginalUnitPrice= document.forms[0]["#originalUnitPrice"].value;
    var vSalesDate= document.forms[0]["#salesDate"].value;
    var vTaxType= document.forms[0]["#taxType"].value;
    var vTaxRate= document.forms[0]["#taxRate"].value.replace(/^\s+|\s+$/, ''); 
    var vServiceItemPrice = document.forms[0]["#serviceItemPrice"].value.replace(/^\s+|\s+$/, '');   
    if(vTaxType == "1" || vTaxType == "2"){
        vTaxRate = "0.0";
    }
    
    if(isNaN(vServiceItemPrice)){
        alert("服務性商品單價必須為數值！");
    }else if(isNaN(vDeductionAmount)){
        alert("折讓金額必須為數值！");
    }else if(isNaN(vDiscountRate)){
        alert("折扣率必須為數值！");
    }else if(isNaN(vQuantity)){
        alert("數量欄位必須為數值！");
    }else if(isNaN(vTaxRate)){
        alert("稅率欄位必須為數值！");
    }else{   
        vat.ajax.XHRequest(
        {
            post:"process_object_name=soSalesOrderService" +
            "&process_object_method_name=getAJAXItemData" +
            "&brandCode=" + vBrandCode +
            "&priceType=" + vPriceType + 
            "&shopCode=" + vShopCode +
            "&customerType=" + vCustomerType +
            "&vipType=" + vVipType +
            "&itemIndexNo" + vItemIndexNo +
            "&itemCode=" + vItemCode +
            "&warehouseCode=" + vWarehouseCode +
            "&quantity=" + vQuantity +
            "&deductionAmount=" + vDeductionAmount +
            "&discountRate=" + vDiscountRate +
            "&promotionCode=" + vPromotionCode +
            "&vipPromotionCode=" + vVipPromotionCode +                         
            "&warehouseManager=" + vWarehouseManager +
            "&warehouseEmployee=" + vWarehouseEmployee +
            "&originalUnitPrice=" + vServiceItemPrice +
            "&salesDate=" + vSalesDate +
            "&taxType=" + vTaxType +
            "&taxRate=" + vTaxRate +
            "&actionId=" + actionId,                                                 
            find: function changeAdvanceItemDataRequestSuccess(oXHR){
                document.forms[0]["#itemCode"].value = vat.ajax.getValue("ItemCode", oXHR.responseText);
                document.forms[0]["#itemName"].value = vat.ajax.getValue("ItemCName", oXHR.responseText);
                document.forms[0]["#warehouseCode"].value = vat.ajax.getValue("WarehouseCode", oXHR.responseText);
                document.forms[0]["#warehouseName"].value = vat.ajax.getValue("WarehouseName", oXHR.responseText);
                document.forms[0]["#originalUnitPrice"].value = vat.ajax.getValue("OriginalUnitPrice", oXHR.responseText);              
                document.forms[0]["#actualUnitPrice"].value = vat.ajax.getValue("ActualUnitPrice", oXHR.responseText);
                document.forms[0]["#currentOnHandQty"].value = vat.ajax.getValue("CurrentOnHandQty", oXHR.responseText);
                document.forms[0]["#quantity"].value = vat.ajax.getValue("Quantity", oXHR.responseText);
                document.forms[0]["#originalSalesAmount"].value = vat.ajax.getValue("OriginalSalesAmount", oXHR.responseText);
                document.forms[0]["#actualSalesAmount"].value = vat.ajax.getValue("ActualSalesAmount", oXHR.responseText);
                document.forms[0]["#deductionAmount"].value = vat.ajax.getValue("DeductionAmount", oXHR.responseText);
                document.forms[0]["#discountRate"].value = vat.ajax.getValue("DiscountRate", oXHR.responseText);     
                document.forms[0]["#promotionCode"].value = vat.ajax.getValue("PromotionCode", oXHR.responseText);
                document.forms[0]["#promotionName"].value = vat.ajax.getValue("PromotionName", oXHR.responseText);
                document.forms[0]["#discount"].value = vat.ajax.getValue("Disct", oXHR.responseText);
                document.forms[0]["#discountType"].value = vat.ajax.getValue("DiscountType", oXHR.responseText);       
                document.forms[0]["#vipPromotionCode"].value = vat.ajax.getValue("VipPromotionCode", oXHR.responseText);
                document.forms[0]["#vipPromotionName"].value = vat.ajax.getValue("VipPromotionName", oXHR.responseText);
                document.forms[0]["#vipDiscount"].value = vat.ajax.getValue("VipDisct", oXHR.responseText);
                document.forms[0]["#vipDiscountType"].value = vat.ajax.getValue("VipDiscountType", oXHR.responseText);             
                document.forms[0]["#taxType"].value = vat.ajax.getValue("TaxType", oXHR.responseText);
                document.forms[0]["#taxRate"].value = vat.ajax.getValue("TaxRate", oXHR.responseText);
                document.forms[0]["#isServiceItem"].value = vat.ajax.getValue("IsServiceItem", oXHR.responseText);
                document.forms[0]["#taxAmount"].value = vat.ajax.getValue("TaxAmount", oXHR.responseText);
                
                if(document.forms[0]["#isServiceItem"].value !== "Y"){
                    document.forms[0]["#serviceItemPrice"].value = "";
                }else{
                    //document.forms[0]["#discountRate"].value = "";
                    document.forms[0]["#promotionCode"].value = "";
                    document.forms[0]["#promotionName"].value = "";
                    document.forms[0]["#discount"].value = "";
                    document.forms[0]["#discountType"].value = "";
                }         
            }
        });
    }
}

function advanceDataSave(){
    //var opener = window.dialogArguments;
    var vItemCode = document.forms[0]["#itemCode"].value.replace(/^\s+|\s+$/, '');
    var vItemName = document.forms[0]["#itemName"].value.replace(/^\s+|\s+$/, '');
    var vWarehouseCode = document.forms[0]["#warehouseCode"].value.replace(/^\s+|\s+$/, '');
    var vWarehouseName = document.forms[0]["#warehouseName"].value.replace(/^\s+|\s+$/, '');  
    var vCurrentOnHandQty = document.forms[0]["#currentOnHandQty"].value;
    var vOriginalUnitPrice = document.forms[0]["#originalUnitPrice"].value;  
    var vOriginalSalesAmount = document.forms[0]["#originalSalesAmount"].value;
    var vActualUnitPrice = document.forms[0]["#actualUnitPrice"].value.replace(/^\s+|\s+$/, '');
    var vActualSalesAmount = document.forms[0]["#actualSalesAmount"].value.replace(/^\s+|\s+$/, '');   
    var vDiscountRate = document.forms[0]["#discountRate"].value.replace(/^\s+|\s+$/, ''); 
    var vDeductionAmount = document.forms[0]["#deductionAmount"].value.replace(/^\s+|\s+$/, '');
    var vPromotionCode= document.forms[0]["#promotionCode"].value.replace(/^\s+|\s+$/, '');
    var vDiscountType= document.forms[0]["#discountType"].value.replace(/^\s+|\s+$/, '');
    var vDiscount= document.forms[0]["#discount"].value.replace(/^\s+|\s+$/, '');
    var vVipPromotionCode= document.forms[0]["#vipPromotionCode"].value;
    var vVipDiscountType= document.forms[0]["#vipDiscountType"].value.replace(/^\s+|\s+$/, '');
    var vVipDiscount= document.forms[0]["#vipDiscount"].value.replace(/^\s+|\s+$/, '');   
    var vQuantity = document.forms[0]["#quantity"].value.replace(/^\s+|\s+$/, '');
    var vTaxType= document.forms[0]["#taxType"].value;
    var vTaxRate= document.forms[0]["#taxRate"].value.replace(/^\s+|\s+$/, '');
    var vTaxAmount= document.forms[0]["#taxAmount"].value;
    var vWatchSerialNo= document.forms[0]["#watchSerialNo"].value.replace(/^\s+|\s+$/, '');
    var vDepositCode= document.forms[0]["#depositCode"].value.replace(/^\s+|\s+$/, '');
    var vIsUseDeposit= document.forms[0]["#isUseDeposit"].value.replace(/^\s+|\s+$/, '');
    var vIsServiceItem= document.forms[0]["#isServiceItem"].value;
    var vServiceItemPrice = document.forms[0]["#serviceItemPrice"].value.replace(/^\s+|\s+$/, ''); 
    //var vLineId= document.forms[0]["#lineId"].value;
    //var vHeadId= document.forms[0]["#headId"].value;
    if(vTaxType == "1" || vTaxType == "2"){
        vTaxRate = "0.0";
    }
    
    if(isNaN(vServiceItemPrice)){
        alert("服務性商品單價必須為數值！");
    }else if(isNaN(vDeductionAmount)){
        alert("折讓金額必須為數值！");
    }else if(isNaN(vDiscountRate)){
        alert("折扣率必須為數值！");
    }else if(isNaN(vQuantity)){
        alert("數量欄位必須為數值！");
    }else if(isNaN(vTaxRate)){
        alert("稅率欄位必須為數值！");
    }else{       
        window.returnValue = vItemCode + "{#}" + vItemName + "{#}" + vWarehouseCode + "{#}" + vWarehouseName + "{#}" + vOriginalUnitPrice + "{#}" + 
                             vActualUnitPrice + "{#}" + vCurrentOnHandQty + "{#}" + vQuantity + "{#}" + vOriginalSalesAmount + "{#}" +
                             vActualSalesAmount + "{#}" + vDeductionAmount + "{#}" + vDiscountRate + "{#}" + vTaxType + "{#}" +
                             vTaxRate + "{#}" + vPromotionCode + "{#}" + vDiscountType + "{#}" + vDiscount + "{#}" + vVipPromotionCode + "{#}" +
                             vVipDiscountType + "{#}" + vVipDiscount + "{#}" + vWatchSerialNo + "{#}" + vDepositCode + "{#}" + vIsUseDeposit + "{#}" +
                             vIsServiceItem + "{#}" + vTaxAmount;
        //alert("returnValue:" + window.returnValue);
        window.close();  
    }
}

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    doPageDataSave();
}

function doAfterCustomerPicker(){
    var processString = "process_object_name=soSalesOrderService&process_object_method_name=executeCountTotalAmount" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value + 
	                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                    "&shopCode=" + document.forms[0]["#form.shopCode"].value +  
	                    "&priceType=" + document.forms[0]["#priceType"].value +     
	                    "&customerType=" + document.forms[0]["#form.customerType"].value +
	                    "&vipType=" + document.forms[0]["#form.vipTypeCode"].value +        
	                    "&warehouseEmployee=" + document.forms[0]["#warehouseEmployee"].value +
	                    "&warehouseManager=" + document.forms[0]["#warehouseManager"].value +        
	                    "&salesDate=" + document.forms[0]["#form.salesOrderDate"].value + 
	                    "&formStatus=" + document.forms[0]["#form.status"].value;
    vat.ajax.startRequest(processString, function () {
	if (vat.ajax.handleState()) {
	    vat.block.pageRefresh(2);
	    document.forms[0]["#form.totalOriginalSalesAmount"].value = vat.ajax.getValue("TotalOriginalSalesAmount", vat.ajax.xmlHttp.responseText);
	    document.forms[0]["#form.totalDeductionAmount"].value = vat.ajax.getValue("TotalDeductionAmount", vat.ajax.xmlHttp.responseText);
		document.forms[0]["#form.taxAmount"].value = vat.ajax.getValue("TaxAmount", vat.ajax.xmlHttp.responseText);
		document.forms[0]["#form.totalOtherExpense"].value = vat.ajax.getValue("TotalOtherExpense", vat.ajax.xmlHttp.responseText);
		document.forms[0]["#form.totalActualSalesAmount"].value = vat.ajax.getValue("TotalActualSalesAmount", vat.ajax.xmlHttp.responseText);
		document.forms[0]["#form.totalNoneTaxSalesAmount"].value = vat.ajax.getValue("TotalNoneTaxSalesAmount", vat.ajax.xmlHttp.responseText);
		document.forms[0]["#form.totalItemQuantity"].value = vat.ajax.getValue("TotalItemQuantity", vat.ajax.xmlHttp.responseText);				
	}
	});
}

function changeDiscountRate(){
    var vDiscountRate = document.forms[0]["#form.discountRate"].value.replace(/^\s+|\s+$/, '');
    if(isNaN(vDiscountRate)){
        document.forms[0]["#form.discountRate"].value = 100.0;
    }else if(vDiscountRate == ""){
        document.forms[0]["#form.discountRate"].value = 100.0;
    }
}

function changeDisplayAmt(){
   if(document.forms[0]["showAmt"].checked == true){       
       document.forms[0]["#displayAmt"].value = "N";
   }else{     
       document.forms[0]["#displayAmt"].value = "Y";
   }
}

function openReportWindow(encryText){
    var reportUrl = document.forms[0]["#reportUrl"].value;
    var reportFileName = document.forms[0]["#reportFileName"].value;  
    var brandCode = document.forms[0]["#form.brandCode"].value;
    var orderTypeCode = document.forms[0]["#form.orderTypeCode"].value;
    var orderNo = document.forms[0]["#form.orderNo"].value;
    var displayAmt = document.forms[0]["#displayAmt"].value;
    var url = "jsp/SalesOrderReport.jsp?reportUrl=" + reportUrl + "&reportFileName=" + reportFileName + "&crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1=" + orderTypeCode + "&prompt2=" + orderNo + "&prompt3=" + displayAmt;
    window.open(url,'BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
}

function changeRelationData(){
    var statusTmp = document.forms[0]["#form.status"].value;
    var orderTypeCodeTmp = document.forms[0]["#form.orderTypeCode"].value;
    var orderNoTmp = document.forms[0]["#form.orderNo"].value;
    var processId = document.forms[0]["#processId"].value;
    if(orderTypeCodeTmp == "SOP"){
	    if(statusTmp == "SAVE" || statusTmp == "UNCONFIRMED"){
	        afterSavePageProcess = "changeRelationData";
		    vat.block.pageDataSave(2);		
	    }
	}else{
	    if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){
		    afterSavePageProcess = "changeRelationData";
		    vat.block.pageDataSave(2);		
	    }	
	}
}

function outlineBlock(){
  formDataInitial();
  headerInitial();
  masterInitial();
  posDataInitial();
}

function headerInitial(){
vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: false,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"銷貨單維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderType", type:"LABEL", value:"單別"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode", back:false, mode:"READONLY", ceap:"#form.orderTypeCode"}]},		 
	 {items:[{name:"#L.orderNo", type:"LABEL", value:"單號"}]},
	 {items:[{name:"#F.orderNo", type:"TEXT", bind:"orderNo", back:false, size:20, mode:"READONLY", ceap:"#form.orderNo"},
	 		 {name:"#F.headId", type:"TEXT", bind:"headId", back:false, size:10, mode:"READONLY", ceap:"#form.headId"}]},
	 {items:[{name:"#L.salesOrderDate", type:"LABEL", value:"銷貨日期"}]},
	 {items:[{name:"#F.salesOrderDate", type:"DATE", bind:"salesOrderDate", size:1, ceap:"#form.salesOrderDate"}]},			 
	 {items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},
	 {items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", back:false, size:8, mode:"READONLY", ceap:"#form.brandCode"},
	         {name:"#F.brandName", type:"TEXT", bind:"brandName", size:12, mode:"READONLY"}]}, 
	 {items:[{name:"#L.status", type:"LABEL", value:"狀態"}]},	 		 
	 {items:[{name:"#F.status", type:"TEXT", bind:"status", size:12, mode:"READONLY"},
	         {name:"#F.statusName", type:"TEXT", bind:"statusName", size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.customerType", type:"LABEL", value:"客戶代號"}]},
	 {items:[{name:"#F.searchCustomerType", type:"SELECT", bind:"searchCustomerType", back:false, ceap:"#searchCustomerType"},
	         {name:"#F.customerCode_var", type:"TEXT", bind:"customerCode_var", back:false, size:15, maxLen:15, ceap:"#customerCode_var"},
	         {name:"#F.customerName", type:"TEXT", bind:"customerName", size:12, mode:"READONLY", ceap:"#form.customerName"}], td:" colSpan=3"},	 
	 {items:[{name:"#L.guiCode", type:"LABEL", value:"統一編號"}]},
	 {items:[{name:"#F.guiCode", type:"TEXT", bind:"guiCode", size:8, maxLen:8, ceap:"#form.guiCode"}]},	 
	 {items:[{name:"#L.inputFormEmployee", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.inputFormEmployee", type:"TEXT", bind:"inputFormEmployee", mode:"READONLY", size:12}]},	 
	 {items:[{name:"#L.inputFormDate", type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.inputFormDate", type:"TEXT", bind:"inputFormDate", mode:"READONLY", size:12}]}]}],	 
	 
	 beginService:"",
	 closeService:""			
	});
}

function masterInitial(){
vat.block.create(vnB_Master = 1, {
	id: "vatBlock_Master", generate: false,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.shopCode", type:"LABEL", value:"專櫃代號"}]},
	 {items:[{name:"#F.shopCode", type:"SELECT", bind:"shopCode", mode:"READONLY", ceap:"#form.shopCode"}]},	 
	 {items:[{name:"#L.sufficientQuantityDelivery", type:"LABEL", value:"足量出貨"}]},
	 {items:[{name:"#F.sufficientQuantityDelivery", type:"RADIO", bind:"sufficientQuantityDelivery", ceap:"#form.sufficientQuantityDelivery"}]},
	 {items:[{name:"#L.superintendentCode", type:"LABEL", value:"訂單負責人"}]},
	 {items:[{name:"#F.superintendentCode", type:"TEXT", bind:"superintendentCode", size:10, maxLen:15, ceap:"#form.superintendentCode"}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.contactPerson", type:"LABEL", value:"客戶聯絡窗口"}]},
	 {items:[{name:"#F.contactPerson", type:"TEXT", bind:"contactPerson", size:20, maxLen:20, ceap:"#form.contactPerson"}]},	
	 {items:[{name:"#L.contactTel", type:"LABEL", value:"客戶聯絡電話"}]},
	 {items:[{name:"#F.contactTel", type:"TEXT", bind:"contactTel", size:20, maxLen:20, ceap:"#form.contactTel"}]},	 
	 {items:[{name:"#L.receiver", type:"LABEL", value:"收貨人"}]},
	 {items:[{name:"#F.receiver"  , type:"TEXT" , bind:"receiver", size:20, maxLen:20, ceap:"#form.receiver"}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.customerPoNo", type:"LABEL", value:"原訂單編號"}]},
	 {items:[{name:"#F.customerPoNo", type:"TEXT", bind:"customerPoNo", size:20, maxLen:20, ceap:"#form.customerPoNo"}]},	
	 {items:[{name:"#L.quotationCode", type:"LABEL", value:"報價單編號"}]},
	 {items:[{name:"#F.quotationCode", type:"TEXT", bind:"quotationCode", size:20, maxLen:20, ceap:"#form.quotationCode"}]},	 
	 {items:[{name:"#L.scheduleShipDate", type:"LABEL", value:"預計出貨日"}]},
	 {items:[{name:"#F.scheduleShipDate", type:"DATE", bind:"scheduleShipDate", ceap:"#form.scheduleShipDate"}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.countryCode", type:"LABEL", value:"國別"}]},
	 {items:[{name:"#F.countryCode", type:"SELECT", bind:"countryCode", ceap:"#form.countryCode"}]},	
	 {items:[{name:"#L.currencyCode", type:"LABEL", value:"幣別"}]},
	 {items:[{name:"#F.currencyCode", type:"SELECT", bind:"currencyCode", ceap:"#form.currencyCode"}]},	 
	 {items:[{name:"#L.homeDelivery", type:"LABEL", value:"運送方式"}]},
	 {items:[{name:"#F.homeDelivery", type:"SELECT", bind:"homeDelivery", ceap:"#form.homeDelivery"}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.paymentTermCode", type:"LABEL", value:"付款條件"}]},
	 {items:[{name:"#F.paymentTermCode", type:"SELECT", bind:"paymentTermCode", ceap:"#form.paymentTermCode"}]},	
	 {items:[{name:"#L.scheduleCollectionDate", type:"LABEL", value:"付款日"}]},
	 {items:[{name:"#F.scheduleCollectionDate", type:"DATE", bind:"scheduleCollectionDate", ceap:"#form.scheduleCollectionDate"}]},	 
	 {items:[{name:"#L.paymentCategory", type:"LABEL", value:"付款方式"}]},
	 {items:[{name:"#F.paymentCategory", type:"SELECT" , bind:"paymentCategory", ceap:"#form.paymentCategory"}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.invoiceAddressTitle", type:"LABEL", value:"發票地址"}]},
	 {items:[{name:"#L.invoiceCity", type:"LABEL", value:"城市:&nbsp&nbsp;"},
	 		 {name:"#F.invoiceCity", type:"TEXT" ,  bind:"invoiceCity", size:10, maxLen:10, ceap:"#form.invoiceCity"},
			 {name:"#L.invoiceArea", type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
	 		 {name:"#F.invoiceArea", type:"TEXT",   bind:"invoiceArea", size:10, maxLen:10, ceap:"#form.invoiceArea"},
	 		 {name:"#L.invoiceZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
	 		 {name:"#F.invoiceZipCode", type:"TEXT", bind:"invoiceZipCode", size:5, maxLen:5, ceap:"#form.invoiceZipCode"},	 		 
	 		 {name:"#L.invoiceAddress", type:"LABEL", value:"&nbsp&nbsp&nbsp;地址:&nbsp&nbsp;"},
 			 {name:"#F.invoiceAddress", type:"TEXT", bind:"invoiceAddress", size:70, maxLen:200, ceap:"#form.invoiceAddress"}], td:" colSpan=5"}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.shipAddressTitle", type:"LABEL", value:"送貨地址"}]},
	 {items:[{name:"#L.shipCity", type:"LABEL", value:"城市:&nbsp&nbsp;"},
	 		 {name:"#F.shipCity", type:"TEXT" ,  bind:"shipCity", size:10, maxLen:10, ceap:"#form.shipCity"},
			 {name:"#L.shipArea", type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
	 		 {name:"#F.shipArea", type:"TEXT",   bind:"shipArea", size:10, maxLen:10, ceap:"#form.shipArea"},
	 		 {name:"#L.shipZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
	 		 {name:"#F.shipZipCode", type:"TEXT", bind:"shipZipCode", size:5, maxLen:5, ceap:"#form.shipZipCode"},	 		 
	 		 {name:"#L.shipAddress", type:"LABEL", value:"&nbsp&nbsp&nbsp;地址:&nbsp&nbsp;"},
 			 {name:"#F.shipAddress", type:"TEXT", bind:"shipAddress", size:70, maxLen:100, ceap:"#form.shipAddress"}], td:" colSpan=5"}]},			 
 	 {row_style:"", cols:[
	 {items:[{name:"#L.invoiceTypeCode", type:"LABEL", value:"發票類型"}]},
	 {items:[{name:"#F.invoiceTypeCode", type:"SELECT", bind:"invoiceTypeCode", ceap:"#form.invoiceTypeCode"}]},	
	 {items:[{name:"#L.taxType", type:"LABEL", value:"稅別"}]},
	 {items:[{name:"#F.taxType", type:"SELECT", bind:"taxType", ceap:"#form.taxType"}]},	 
	 {items:[{name:"#L.taxRate", type:"LABEL", value:"稅率"}]},
	 {items:[{name:"#F.taxRate", type:"TEXT" , bind:"taxRate", size:8, maxLen:4, ceap:"#form.taxRate"}]}]},	 
     {row_style:"", cols:[
	 {items:[{name:"#L.defaultWarehouseCode", type:"LABEL", value:"庫別"}]},
	 {items:[{name:"#F.defaultWarehouseCode", type:"SELECT", bind:"defaultWarehouseCode", ceap:"#form.defaultWarehouseCode"}]},		 
	 {items:[{name:"#L.promotionCode", type:"LABEL", value:"活動代號"}]},
	 {items:[{name:"#F.promotionCode", type:"TEXT" , bind:"promotionCode", size:20, maxLen:20, ceap:"#form.promotionCode"}]}, 
	 {items:[{name:"#L.discountRate", type:"LABEL", value:"折扣比率"}]},
	 {items:[{name:"#F.discountRate", type:"TEXT" , bind:"discountRate", size:8, maxLen:4, ceap:"#form.discountRate"}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark1", type:"LABEL", value:"備註一"}]},
	 {items:[{name:"#F.remark1", type:"TEXT", bind:"remark1", size:100, maxLen:100, ceap:"#form.remark1"}], td:" colSpan=3"},
	 {items:[{name:"#L.attachedInvoice", type:"LABEL", value:"隨附發票"}]},
	 {items:[{name:"#F.attachedInvoice", type:"SELECT" , bind:"attachedInvoice", ceap:"#form.attachedInvoice"}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark2", type:"LABEL", value:"備註二"}]},
	 {items:[{name:"#F.remark2", type:"TEXT", bind:"remark2", size:100, maxLen:100, ceap:"#form.remark2"}], td:" colSpan=5"}]}],
	  
	 beginService:"",
	 closeService:""			
	});
}

function posDataInitial(){
vat.block.create(vnB_POS = 3, {
	id: "vatBlock_POS", generate: false,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.posMachineCode", type:"LABEL", value:"POS機號"}]},	 
	 {items:[{name:"#F.posMachineCode", type:"SELECT", bind:"posMachineCode", ceap:"#form.posMachineCode"}]},		 
	 {items:[{name:"#L.casherCode", type:"LABEL", value:"收銀員代號"}]},
	 {items:[{name:"#F.casherCode", type:"TEXT", bind:"casherCode", size:15, maxLen:15, ceap:"#form.casherCode"},
	 		 {name:"#F.casherName", type:"TEXT", bind:"casherName", size:15, mode:"READONLY", ceap:"#form.casherName"}]},
	 {items:[{name:"#L.departureDate", type:"LABEL", value:"出境日期"}]},
	 {items:[{name:"#F.departureDate", type:"DATE", bind:"departureDate", size:1, ceap:"#form.departureDate"}]}]},	
	 {row_style:"", cols:[
	 {items:[{name:"#L.flightNo", type:"LABEL", value:"班機代碼"}]},
	 {items:[{name:"#F.flightNo", type:"TEXT", bind:"flightNo", size:10, maxLen:10, ceap:"#form.flightNo"}]},	 
	 {items:[{name:"#L.passportNo", type:"LABEL", value:"護照號碼"}]},
	 {items:[{name:"#F.passportNo", type:"TEXT", bind:"passportNo", size:20, maxLen:20, ceap:"#form.passportNo"}]},	 	 
	 {items:[{name:"#L.ladingNo", type:"LABEL", value:"提貨單號"}]},
	 {items:[{name:"#F.ladingNo", type:"TEXT", bind:"ladingNo", size:20, maxLen:20, ceap:"#form.ladingNo"}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.transactionSeqNo", type:"LABEL", value:"交易序號"}]},
	 {items:[{name:"#F.transactionSeqNo", type:"TEXT", bind:"transactionSeqNo", size:20, maxLen:20, ceap:"#form.transactionSeqNo"}]},	 
	 {items:[{name:"#L.salesInvoicePage", type:"LABEL", value:"Sales Invoice page"}]},
	 {items:[{name:"#F.salesInvoicePage", type:"TEXT", bind:"salesInvoicePage", size:20, maxLen:20, ceap:"#form.salesInvoicePage"}], td:" colSpan=3"}]}],	 	 
		 	 
	 beginService:"",
	 closeService:""			
	});
}

function paymentDataInitial(){
    var allPaymentType = vat.bean("allPaymentType");
    var statusTmp = document.forms[0]["#form.status"].value;
    var orderTypeCodeTmp = document.forms[0]["#form.orderTypeCode"].value;
    var orderNoTmp = document.forms[0]["#form.orderNo"].value;
    var processId = document.forms[0]["#processId"].value;
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	var varCanGridModify = false;
	if(orderTypeCodeTmp == "SOP"){
	    if(statusTmp == "SAVE" || statusTmp == "UNCONFIRMED"){
		    varCanGridDelete = true;
		    varCanGridAppend = true;
		    varCanGridModify = true;		
	    }
	}else{
	    if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){
		    varCanGridDelete = true;
		    varCanGridAppend = true;
		    varCanGridModify = true;		
	    }	
	}
	// set column
	var vnA_Detail = 4;
    vat.item.make(vnA_Detail, "indexNo", {type:"IDX" , desc:"序號"});
	vat.item.make(vnA_Detail, "posPaymentType", {type:"SELECT", size:1, desc:"付款類型", init:allPaymentType});
	vat.item.make(vnA_Detail, "localCurrencyCode", {type:"TEXT", size:1, desc:"原幣幣別", mode:"HIDDEN"});
	vat.item.make(vnA_Detail, "localAmount", {type:"NUMB", size:8, maxLen:8, desc:"金額", mask:"CCCCCCCCCCCC"});
	vat.item.make(vnA_Detail, "discountRate", {type:"NUMB", size:8, maxLen:4, desc:"折扣率", mask:"CCCCCCCCCCCC"});
	vat.item.make(vnA_Detail, "remark1", {type:"TEXT", size:80, maxLen:100, desc:"備註"});
	vat.item.make(vnA_Detail, "posPaymentId", {type:"ROWID"});
	vat.item.make(vnA_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnA_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});
	vat.item.make(vnA_Detail, "message", {type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnA_Detail, {	pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,														
							    beginService: "",
								closeService: "",
							    itemMouseinService  : "",
								itemMouseoutService : "",
							    appendBeforeService : "kweSoPageAppendBeforeMethod()",
							    appendAfterService  : "kweSoPageAppendAfterMethod()",
								deleteBeforeService : "",
								deleteAfterService  : "",
								loadBeforeAjxService: "loadPaymentBeforeAjxService()",
								loadSuccessAfter    : "kweSoPageLoadSuccess()",
								loadFailureAfter    : "",
								eventService        : "",   
								saveBeforeAjxService: "savePaymentBeforeAjxService()",
								saveSuccessAfter    : "savePaymentSuccessAfter()",
								saveFailureAfter    : ""
														});
	vat.block.pageDataLoad(vnA_Detail, vnCurrentPage = 1);	
}

function loadPaymentBeforeAjxService(){
	var processString = "process_object_name=soSalesOrderService&process_object_method_name=getAJAXPageDataForPayment" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value +
	                    "&formStatus=" + document.forms[0]["#form.status"].value;
																					
	return processString;											
}

/*
	SAVE PAYMENT LINE FUNCTION
*/
function savePaymentBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=soSalesOrderService&process_object_method_name=updateAJAXPaymentPageLinesData" + "&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value;
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function savePaymentSuccessAfter() {
    
    if ("saveHandler" == afterSavePaymentPageProcess) {	
	    execSubmitAction("SAVE");
	} else if ("submitHandler" == afterSavePaymentPageProcess) {
	    execSubmitAction("SUBMIT");
	} else if ("submitBgHandler" == afterSavePaymentPageProcess) {
	    execSubmitAction("SUBMIT_BG");
	} else if ("voidHandler" == afterSavePaymentPageProcess) {
		execSubmitAction("VOID");
	} else if ("copyHandler" == afterSavePaymentPageProcess) {
		executeCommandHandler("main", "copyHandler");
	} else if ("executeImport" == afterSavePaymentPageProcess) {
	    importFormData();
	}

	afterSavePaymentPageProcess = "";
}

function formDataInitial(){
    if(document.forms[0]["#formId"].value != "[binding]"){       
        vat.bean().vatBeanOther =
        {
          formId : document.forms[0]["#formId"].value
        };      
        vat.bean.init(function(){
		return "process_object_name=soSalesOrderAction&process_object_method_name=performInitial"; 
        },{other: true});
    }
}

function exportFormData(){
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=SO_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=soSalesOrderService" + 
              "&processObjectMethodName=findSoSalesOrderHeadById" + 
              "&gridFieldName=soSalesOrderItems" + 
              "&arguments=" + document.forms[0]["#form.headId"].value + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '銷貨單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function importFormData(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=SO_ITEM" +
		"&importFileType=XLS" +
        "&processObjectName=soSalesOrderService" + 
        "&processObjectMethodName=executeImportItems" +
        "&arguments=" + document.forms[0]["#form.headId"].value + "{$}" + document.forms[0]["#form.vipPromotionCode"].value + "{$}" + 
                      document.forms[0]["#form.taxType"].value + "{$}" + document.forms[0]["#form.taxRate"].value +
        "&parameterTypes=LONG{$}STRING{$}STRING{$}DOUBLE" +
        "&blockId=2",
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function execSubmitAction(actionId){
    var formId = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var assignmentId = document.forms[0]["#assignmentId"].value.replace(/^\s+|\s+$/, '');
    var processId = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var status = document.forms[0]["#form.status"].value.replace(/^\s+|\s+$/, '');
    var employeeCode = document.forms[0]["#employeeCode"].value.replace(/^\s+|\s+$/, '');
    var priceType = document.forms[0]["#priceType"].value.replace(/^\s+|\s+$/, '');   
    var warehouseEmployee = document.forms[0]["#warehouseEmployee"].value.replace(/^\s+|\s+$/, '');
    var warehouseManager = document.forms[0]["#warehouseManager"].value.replace(/^\s+|\s+$/, '');
    var organizationCode = document.forms[0]["#organizationCode"].value.replace(/^\s+|\s+$/, '');    
    var approvalResult = "true";
    if(document.forms[0]["#approvalResult.result"][1].checked){
        approvalResult = "false";
    }
    var approvalComment = document.forms[0]["#approvalResult.approvalComment"].value.replace(/^\s+|\s+$/, '');
    //alert("formId=" + formId);
    //alert("assignmentId=" + assignmentId);
    //alert("processId=" + processId);
    //alert("status=" + status);
    //alert("approvalResult=" + approvalResult);
    var formStatus = status;
    if("SAVE" == actionId){
        formStatus = "SAVE";
    }else if("SUBMIT" == actionId){
        formStatus = changeFormStatus(formId, processId, status, approvalResult);
    }else if("SUBMIT_BG" == actionId){
        formStatus = "SIGNING";
    }else if("VOID" == actionId){
        formStatus = "VOID";
    }else if("UNCONFIRMED" == actionId){
        formStatus = "UNCONFIRMED";
    }
    vat.bean().vatBeanOther =
    {
      beforeChangeStatus : status,
      formStatus : formStatus,
      employeeCode : employeeCode,
      assignmentId : assignmentId,
      processId : processId,
      approvalResult : approvalResult,
      approvalComment : approvalComment,
      priceType : priceType,
      warehouseEmployee : warehouseEmployee,
      warehouseManager : warehouseManager,
      organizationCode : organizationCode
    };
    if("SUBMIT_BG" == actionId){
        vat.block.submit(function(){return "process_object_name=soSalesOrderAction"+
            "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
    }else{    
        vat.block.submit(function(){return "process_object_name=soSalesOrderAction"+
            "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
    }
}

function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == "" || status == "UNCONFIRMED"){
        formStatus = "SIGNING";
    }else if(processId != null && processId != "" && processId != 0){
        if(status == "SAVE" || status == "REJECT"){
            formStatus = "SIGNING";
        }else if(status == "SIGNING"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }  
    }
    return formStatus;
}

function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=SO_SALES_ORDER" +
		"&levelType=ERROR" +
        "&processObjectName=soSalesOrderService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + document.forms[0]["#form.headId"].value +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=soSalesOrderAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}