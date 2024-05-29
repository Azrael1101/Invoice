/*
	initial 
*/
var afterSavePageProcess = "";

function detailInitial(){
    var statusTmp = document.forms[0]["#form.status"].value;
    var orderTypeCodeTmp = document.forms[0]["#form.orderTypeCode"].value;
    var orderNoTmp = document.forms[0]["#form.orderNo"].value;
    var processId = document.forms[0]["#processId"].value;
	var varCanDataDelete = false;
	var varCanDataAppend = false;
	var varCanDataModify = false;
	if(orderTypeCodeTmp == "SOP"){
	    if(statusTmp == "SAVE" || statusTmp == "UNCONFIRMED"){
		    varCanDataDelete = true;
		    varCanDataAppend = true;
		    varCanDataModify = true;		
	    }
	}else{
	    if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){
		    varCanDataDelete = true;
		    varCanDataAppend = true;
		    varCanDataModify = true;		
	    }	
	}
    vat.formD.item(0, "F01", {type:"IDX" , desc:"序號"});
	vat.formD.item(0, "F02", {type:"TEXT", size:15, maxLen:20, desc:"品號", mask:"CCCCCCCCCCCC", onchange:"changeItemData(1)"});
	vat.formD.item(0, "F03", {type:"TEXT", size:18, maxLen:20, desc:"品名", mode:"READONLY"});
	vat.formD.item(0, "F04", {type:"TEXT", size:12, maxLen:12, desc:"庫別", mask:"CCCCCCCCCCCC", onchange:"changeItemData(2)"});
	vat.formD.item(0, "F05", {type:"TEXT", size:20, maxLen:20, desc:"庫名", mode:"HIDDEN"});
	vat.formD.item(0, "F06", {type:"NUMB", size: 8, maxLen:12, desc:"單價", mode:"READONLY"});
	vat.formD.item(0, "F07", {type:"NUMB", size: 8, maxLen:12, desc:"折扣後單價", mode:"READONLY"});
	vat.formD.item(0, "F08", {type:"NUMB", size: 8, maxLen:12, desc:"庫存量", mode:"READONLY"});
	vat.formD.item(0, "F09", {type:"NUMB", size: 8, maxLen: 8, desc:"數量", onchange:"changeItemData(2)"});
	vat.formD.item(0, "F10", {type:"NUMB", size: 8, maxLen:20, desc:"金額", mode:"READONLY"});
	vat.formD.item(0, "F11", {type:"NUMB", size: 8, maxLen:20, desc:"折扣後金額", mode:"READONLY"});
	vat.formD.item(0, "F12", {type:"NUMB", size: 1, maxLen: 8, desc:"折讓金額", mode: "HIDDEN"});
	vat.formD.item(0, "F13", {type:"NUMB", size: 1, maxLen: 8, desc:"折扣率", mode: "HIDDEN"});
	vat.formD.item(0, "F14", {type:"TEXT", size: 1, maxLen: 1, desc:"稅別", mode: "HIDDEN"});
	vat.formD.item(0, "F15", {type:"NUMB", size: 1, maxLen: 8, desc:"稅率", mode: "HIDDEN"});
	vat.formD.item(0, "F16", {type:"TEXT", size: 1, maxLen: 1, desc:"是否含稅", mode: "HIDDEN"});
	vat.formD.item(0, "F17", {type:"TEXT", size: 1, maxLen: 1, desc:"活動代號", mode: "HIDDEN"});
	vat.formD.item(0, "F18", {type:"TEXT", size: 1, maxLen: 1, desc:"活動折扣類型", mode: "HIDDEN"});
	vat.formD.item(0, "F19", {type:"NUMB", size: 1, maxLen: 1, desc:"活動折扣", mode: "HIDDEN"});
	vat.formD.item(0, "F20", {type:"TEXT", size: 1, maxLen: 20, desc:"VIP類別代號", mode: "HIDDEN"});
	vat.formD.item(0, "F21", {type:"TEXT", size: 1, maxLen: 1, desc:"VIP折扣類型", mode: "HIDDEN"});
	vat.formD.item(0, "F22", {type:"NUMB", size: 1, maxLen: 1, desc:"VIP折扣", mode: "HIDDEN"});
	vat.formD.item(0, "F23", {type:"TEXT", size: 1, maxLen: 20, desc:"手錶序號", mode: "HIDDEN"});
	vat.formD.item(0, "F24", {type:"TEXT", size: 1, maxLen: 8, desc:"訂金單代號", mode: "HIDDEN"});
	vat.formD.item(0, "F25", {type:"TEXT", size: 1, maxLen: 1, desc:"訂金支付", mode: "HIDDEN"});
	vat.formD.item(0, "F26", {type:"TEXT", size: 1, maxLen: 1, desc:"服務性商品", mode: "HIDDEN"});
	vat.formD.item(0, "F27", {type:"NUMB", size: 1, maxLen: 1, desc:"稅額", mode: "HIDDEN"});
	vat.formD.item(0, "F28", {type:"ROWID"});
	vat.formD.item(0, "F29", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.formD.item(0, "F30", {type:"DEL", desc:"刪除"});
	vat.formD.item(0, "F31", {type:"MSG", desc:"訊息"});
	vat.formD.item(0, "F32", {type:"BUTTON", desc:"進階輸入", value:"進階輸入", src:"images/button_advance_input.gif", eClick:"advanceInput()"});

	vat.formD.pageLayout(0, {	pageSize: 10,
	                            canDataDelete:varCanDataDelete,
								canDataAppend:varCanDataAppend,
								canDataModify:varCanDataModify,														
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
	vat.formD.pageDataLoad(0, 1);
	
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
    vat.formD.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg == "") {
		if ("saveHandler" == afterSavePageProcess) {	
			executeCommandHandler("main", "saveHandler");
		} else if ("submitHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "submitHandler");
		} else if ("voidHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "voidHandler");
		} else if ("copyHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "copyHandler");
		} else if ("executeExport" == afterSavePageProcess) {
			//vat.formD.pageRefresh(0);
			executeCommandHandlerNoBlock("main","exportDataHandler");
		} else if ("pageRefresh" == afterSavePageProcess) {
			//vat.formD.pageRefresh(0);
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
			        vat.formD.pageRefresh(0);
				}
			});	
		}
	} else {
		alert("錯誤訊息： " + errorMsg);
	}
	afterSavePageProcess = "";
}

/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
    if (confirm("是否確認暫存?")) {		
	    //save line
		vat.formD.pageDataSave(0);
		afterSavePageProcess = "saveHandler";		
	}
}

/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	if (confirm("是否確認送出?")) {		
		//save line
		vat.formD.pageDataSave(0);
		afterSavePageProcess = "submitHandler";	
	}
}

/*
	作廢VOID HEAD && LINE
*/
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		vat.formD.pageDataSave(0);
		afterSavePageProcess = "voidHandler";		
	}
}

/*
	COPY HEAD && LINE
*/
function doCopyHandler() {
	if (confirm("是否進行複製?")) {		
		//save line
		vat.formD.pageDataSave(0);
		afterSavePageProcess = "copyHandler";		
	}
}

/*
	顯示合計的頁面
*/
function showTotalCountPage() {
    //save line
    vat.formD.pageDataSave(0);
    afterSavePageProcess = "totalCount";	
}

/*
	匯出
*/
function doExport() {
	//save line
	vat.formD.pageDataSave(0);
	afterSavePageProcess = "executeExport";
}

function doPageDataSave(){
    vat.formD.pageDataSave(0);
}

function doPageRefresh(){
    vat.formD.pageRefresh(0);
}

function advanceInput(){
    
    var nItemLine = vat.formD.itemIndexNo();
	var vItemCode = vat.formD.itemDataGet(nItemLine, "itemCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var vWarehouseCode = vat.formD.itemDataGet(nItemLine, "warehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var vOriginalUnitPrice = vat.formD.itemDataGet(nItemLine, "originalUnitPrice").replace(/^\s+|\s+$/, '');
	var vActualUnitPrice = vat.formD.itemDataGet(nItemLine, "actualUnitPrice").replace(/^\s+|\s+$/, '');
	var vCurrentOnHandQty = vat.formD.itemDataGet(nItemLine, "currentOnHandQty").replace(/^\s+|\s+$/, '');
	var vQuantity = vat.formD.itemDataGet(nItemLine, "quantity").replace(/^\s+|\s+$/, '');
	var vOriginalSalesAmount = vat.formD.itemDataGet(nItemLine, "originalSalesAmount").replace(/^\s+|\s+$/, '');
	var vActualSalesAmount = vat.formD.itemDataGet(nItemLine, "actualSalesAmount").replace(/^\s+|\s+$/, '');	
	var vTaxType = vat.formD.itemDataGet(nItemLine, "taxType").replace(/^\s+|\s+$/, '');
	var vTaxRate = vat.formD.itemDataGet(nItemLine, "taxRate").replace(/^\s+|\s+$/, '');
	var vDeductionAmount = vat.formD.itemDataGet(nItemLine, "deductionAmount").replace(/^\s+|\s+$/, '');
	var vDiscountRate = vat.formD.itemDataGet(nItemLine, "discountRate").replace(/^\s+|\s+$/, '');
	var vPromotionCode = vat.formD.itemDataGet(nItemLine, "promotionCode").replace(/^\s+|\s+$/, '');
	var vDiscountType = vat.formD.itemDataGet(nItemLine, "discountType").replace(/^\s+|\s+$/, '');
	var vDiscount = vat.formD.itemDataGet(nItemLine, "discount").replace(/^\s+|\s+$/, '');
	var vVipPromotionCode = vat.formD.itemDataGet(nItemLine, "vipPromotionCode").replace(/^\s+|\s+$/, '');
	var vVipDiscountType = vat.formD.itemDataGet(nItemLine, "vipDiscountType").replace(/^\s+|\s+$/, '');
	var vVipDiscount = vat.formD.itemDataGet(nItemLine, "vipDiscount").replace(/^\s+|\s+$/, '');	
	var vWatchSerialNo = vat.formD.itemDataGet(nItemLine, "watchSerialNo").replace(/^\s+|\s+$/, '');
	var vDepositCode = vat.formD.itemDataGet(nItemLine, "depositCode").replace(/^\s+|\s+$/, '');
	var vIsUseDeposit = vat.formD.itemDataGet(nItemLine, "isUseDeposit").replace(/^\s+|\s+$/, '');
	var vIsServiceItem = vat.formD.itemDataGet(nItemLine, "isServiceItem").replace(/^\s+|\s+$/, '');
	var vTaxAmount = vat.formD.itemDataGet(nItemLine, "taxAmount").replace(/^\s+|\s+$/, '');	
	var vLineId = vat.formD.itemDataGet(nItemLine, "lineId").replace(/^\s+|\s+$/, '');	
	
	var obj = document.getElementById("vatBeginDiv");
	if (obj){
		obj.filters[0].enabled = true;
		obj.filters[0].opacity = 0.60; 
	}
	
	var returnData = window.showModalDialog(
		"So_SalesOrder:advanceInput:20090219.page"+
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
		    vat.formD.itemIdDataBind(0, 1, returnDataArray[0]);	    
		    vat.formD.itemIdDataBind(0, 2, returnDataArray[1]); 
            vat.formD.itemIdDataBind(0, 3, returnDataArray[2]); 
            vat.formD.itemIdDataBind(0, 4, returnDataArray[3]); 
            vat.formD.itemIdDataBind(0, 5, returnDataArray[4]); 
            vat.formD.itemIdDataBind(0, 6, returnDataArray[5]); 
            vat.formD.itemIdDataBind(0, 7, returnDataArray[6]); 
            vat.formD.itemIdDataBind(0, 8, returnDataArray[7]); 
            vat.formD.itemIdDataBind(0, 9, returnDataArray[8]); 
            vat.formD.itemIdDataBind(0, 10, returnDataArray[9]);  
            vat.formD.itemIdDataBind(0, 11, returnDataArray[10]);
            vat.formD.itemIdDataBind(0, 12, returnDataArray[11]);
            vat.formD.itemIdDataBind(0, 13, returnDataArray[12]);
            vat.formD.itemIdDataBind(0, 14, returnDataArray[13]);    
            vat.formD.itemIdDataBind(0, 16, returnDataArray[14]);
            vat.formD.itemIdDataBind(0, 17, returnDataArray[15]);
            vat.formD.itemIdDataBind(0, 18, returnDataArray[16]);
            vat.formD.itemIdDataBind(0, 19, returnDataArray[17]);
            vat.formD.itemIdDataBind(0, 20, returnDataArray[18]);
            vat.formD.itemIdDataBind(0, 21, returnDataArray[19]);
            vat.formD.itemIdDataBind(0, 22, returnDataArray[20]);
            vat.formD.itemIdDataBind(0, 23, returnDataArray[21]);
            vat.formD.itemIdDataBind(0, 24, returnDataArray[22]);          
            vat.formD.itemIdDataBind(0, 25, returnDataArray[23]);
            vat.formD.itemIdDataBind(0, 26, returnDataArray[24]);
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
    vat.formD.pageDataSave(0);
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
    var nItemLine = vat.formD.itemIndexNo();	
	var vItemCode = vat.formD.itemDataGet(nItemLine, "itemCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var vWarehouseCode = vat.formD.itemDataGet(nItemLine, "warehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var vQuantity = vat.formD.itemDataGet(nItemLine, "quantity").replace(/^\s+|\s+$/, '');
	var vDeductionAmount = vat.formD.itemDataGet(nItemLine, "deductionAmount").replace(/^\s+|\s+$/, '');
	var vDiscountRate = vat.formD.itemDataGet(nItemLine, "discountRate").replace(/^\s+|\s+$/, '');
	var vPromotionCode = vat.formD.itemDataGet(nItemLine, "promotionCode").replace(/^\s+|\s+$/, '');
	var vVipPromotionCode = vat.formD.itemDataGet(nItemLine, "vipPromotionCode").replace(/^\s+|\s+$/, '');
	var vOriginalUnitPrice = vat.formD.itemDataGet(nItemLine, "originalUnitPrice").replace(/^\s+|\s+$/, '');
	var vTaxType = vat.formD.itemDataGet(nItemLine, "taxType").replace(/^\s+|\s+$/, '');
	var vTaxRate = vat.formD.itemDataGet(nItemLine, "taxRate").replace(/^\s+|\s+$/, '');
	
	vat.formD.itemIdDataBind(0, 1, vItemCode);
	vat.formD.itemIdDataBind(0, 3, vWarehouseCode);	
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
                       vat.formD.itemIdDataBind(0,  1, vat.ajax.getValue("ItemCode", oXHR.responseText));
                       vat.formD.itemIdDataBind(0,  2, vat.ajax.getValue("ItemCName", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0,  3, vat.ajax.getValue("WarehouseCode", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0,  4, vat.ajax.getValue("WarehouseName", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0,  5, vat.ajax.getValue("OriginalUnitPrice", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0,  6, vat.ajax.getValue("ActualUnitPrice", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0,  7, vat.ajax.getValue("CurrentOnHandQty", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0,  8, vat.ajax.getValue("Quantity", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0,  9, vat.ajax.getValue("OriginalSalesAmount", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0, 10, vat.ajax.getValue("ActualSalesAmount", oXHR.responseText));  
                       vat.formD.itemIdDataBind(0, 11, vat.ajax.getValue("DeductionAmount", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 12, vat.ajax.getValue("DiscountRate", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 13, vat.ajax.getValue("TaxType", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 14, vat.ajax.getValue("TaxRate", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 15, vat.ajax.getValue("IsTax", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 16, vat.ajax.getValue("PromotionCode", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 17, vat.ajax.getValue("DiscountType", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 18, vat.ajax.getValue("Disct", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 19, vat.ajax.getValue("VipPromotionCode", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 20, vat.ajax.getValue("VipDiscountType", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 21, vat.ajax.getValue("VipDisct", oXHR.responseText));     
                       vat.formD.itemIdDataBind(0, 25, vat.ajax.getValue("IsServiceItem", oXHR.responseText));
                       vat.formD.itemIdDataBind(0, 26, vat.ajax.getValue("TaxAmount", oXHR.responseText));
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
    vat.formD.pageDataSave(0);
}

function doAfterCustomerPicker(){
    vat.formD.pageRefresh(0);
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
		    vat.formD.pageDataSave(0);		
	    }
	}else{
	    if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){
		    afterSavePageProcess = "changeRelationData";
		    vat.formD.pageDataSave(0);		
	    }	
	}
}