/*
	initial 
*/
var afterSavePageProcess = "";
var vnB_Header = 0;
var vnB_Master = 1; 
var vnB_Detail = 2;

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
    var url = "jsp/SoReturnReport.jsp?reportUrl=" + reportUrl + "&reportFileName=" + reportFileName + "&crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1=" + orderTypeCode + "&prompt2=" + orderNo + "&prompt3=" + displayAmt;
    window.open(url,'BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
}

function detailInitial(){
    var statusTmp = document.forms[0]["#form.status"].value;
    var orderTypeCodeTmp = document.forms[0]["#form.orderTypeCode"].value;
    var orderNoTmp = document.forms[0]["#form.orderNo"].value;
    var processId = document.forms[0]["#processId"].value;
    document.forms[0]["#form.origDeliveryOrderNo"].value = document.forms[0]["#form.origDeliveryOrderNo"].value.replace(/^\s+|\s+$/, '');
    var origDeliveryOrderNoTmp = document.forms[0]["#form.origDeliveryOrderNo"].value;    
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	var varCanGridModify = false;	
	if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){
		varCanGridDelete = true;
		varCanGridAppend = true;
		varCanGridModify = true;		
	}
	// set column
	vat.item.make(vnB_Detail, "indexNo", {type:"IDX" , desc:"序號"});	
	vat.item.make(vnB_Detail, "itemCode", {type:"TEXT", size:15, maxLen:20, desc:"品號", mask:"CCCCCCCCCCCC", onchange:"changeItemData(1)"}); 
	vat.item.make(vnB_Detail, "itemCName", {type:"TEXT", size:18, maxLen:20, desc:"品名", mode:"READONLY"});   
	vat.item.make(vnB_Detail, "warehouseCode", {type:"TEXT", size:12, maxLen:12, desc:"庫別", mode:"READONLY"});
	vat.item.make(vnB_Detail, "warehouseName", {type:"TEXT", size:20, maxLen:20, desc:"庫名", mode:"HIDDEN"});    
	vat.item.make(vnB_Detail, "lotNo", {type:"TEXT", size:20, maxLen:20, desc:"批號", mask:"CCCCCCCCCCCC"});	
	vat.item.make(vnB_Detail, "originalUnitPrice", {type:"NUMB", size: 8, maxLen:12, desc:"原單價", mask:"CCCCCCCCCCCC", onchange:"changeItemData(2)"});
	vat.item.make(vnB_Detail, "actualUnitPrice", {type:"NUMB", size: 8, maxLen:12, desc:"實際單價", mask:"CCCCCCCCCCCC", onchange:"changeItemData(2)"});	
	vat.item.make(vnB_Detail, "returnableQuantity", {type:"NUMB", size: 8, maxLen: 8, desc:"可退數量", mode:"READONLY"});
	vat.item.make(vnB_Detail, "shipQuantity", {type:"NUMB", size: 8, maxLen: 8, desc:"退貨數量", onchange:"changeItemData(3)"});
	vat.item.make(vnB_Detail, "originalShipAmount", {type:"NUMB", size: 8, maxLen:20, desc:"原退貨金額", mode:"READONLY"});
	vat.item.make(vnB_Detail, "actualShipAmount", {type:"NUMB", size: 8, maxLen:20, desc:"實際退貨金額", mode:"READONLY"});
	vat.item.make(vnB_Detail, "taxType", {type:"TEXT", size: 1, maxLen: 1, desc:"稅別", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "taxRate", {type:"NUMB", size: 1, maxLen: 8, desc:"稅率", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "shipTaxAmount", {type:"NUMB", size: 1, maxLen: 1, desc:"稅額", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "isTax", {type:"TEXT", size: 1, maxLen: 1, desc:"是否含稅", mode: "HIDDEN"});	
	vat.item.make(vnB_Detail, "watchSerialNo", {type:"TEXT", size: 1, maxLen: 20, desc:"手錶序號", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "reserve2", {type:"TEXT", size: 1, maxLen: 20, desc:"原出貨單識別碼", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});
	vat.item.make(vnB_Detail, "F22", {type:"BUTTON", desc:"進階輸入", value:"進階輸入", src:"images/button_advance_input.gif", eClick:"advanceInput()"});

	vat.block.pageLayout(vnB_Detail, {	pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,													
							    beginService: "",
								closeService: "",
							    itemMouseinService  : "",
								itemMouseoutService : "",
							    appendBeforeService : "appendBeforeMethod()",
							    appendAfterService  : "appendAfterMethod()",
								deleteBeforeService : "",
								deleteAfterService  : "",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "pageLoadSuccess()",
								loadFailureAfter    : "",
								eventService        : "changeRelationData()",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()",
								saveFailureAfter    : ""
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function loadBeforeAjxService(){
	var processString = "process_object_name=imDeliveryService&process_object_method_name=getAJAXPageDataForReturn" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value + 
	                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                    "&formStatus=" + document.forms[0]["#form.status"].value +
	                    "&defaultWarehouseCode=" + document.forms[0]["#form.defaultWarehouseCode"].value +
	                    "&taxType=" + document.forms[0]["#form.taxType"].value + 
	                    "&taxRate=" + document.forms[0]["#form.taxRate"].value; 
																					
	return processString;											
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imDeliveryService&process_object_method_name=updateAJAXPageLinesDataForReturn" + "&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value;
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
    if ("saveHandler" == afterSavePageProcess) {
	    execSubmitAction("SAVE");
	} else if ("submitHandler" == afterSavePageProcess) {
		execSubmitAction("SUBMIT");
	} else if ("submitBgHandler" == afterSavePageProcess) {
	    execSubmitAction("SUBMIT_BG");
	} else if ("voidHandler" == afterSavePageProcess) {
		execSubmitAction("VOID");
	} else if ("executeExport" == afterSavePageProcess) {
		exportFormData();
	} else if ("executeImport" == afterSavePageProcess) {
	    importFormData();
	} else if ("totalCount" == afterSavePageProcess) {
		var processString = "process_object_name=imDeliveryService&process_object_method_name=performCountTotalAmountForReturn" + 
	                "&headId=" + document.forms[0]["#form.headId"].value +
	                "&formStatus=" + document.forms[0]["#form.status"].value;
		vat.ajax.startRequest(processString, function () {
		    if (vat.ajax.handleState()) {
				document.forms[0]["#form.totalOriginalShipAmount"].value = vat.ajax.getValue("TotalOriginalShipAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalDeductionAmount"].value = vat.ajax.getValue("TotalDeductionAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.shipTaxAmount"].value = vat.ajax.getValue("ShipTaxAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalOtherExpense"].value = vat.ajax.getValue("TotalOtherExpense", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalActualShipAmount"].value = vat.ajax.getValue("TotalActualShipAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalNoneTaxShipAmount"].value = vat.ajax.getValue("TotalNoneTaxShipAmount", vat.ajax.xmlHttp.responseText);
				document.forms[0]["#form.totalItemQuantity"].value = vat.ajax.getValue("TotalItemQuantity", vat.ajax.xmlHttp.responseText);				
		    }
		});	
	}else if ("changeRelationData" == afterSavePageProcess) {
	    var processString = "process_object_name=imDeliveryService&process_object_method_name=updateReturnRelationData" + 
	                "&headId=" + document.forms[0]["#form.headId"].value + 
	                "&brandCode=" + document.forms[0]["#form.brandCode"].value + 
	                "&defaultWarehouseCode=" + document.forms[0]["#form.defaultWarehouseCode"].value + 
	                "&taxType=" + document.forms[0]["#form.taxType"].value +
	                "&taxRate=" + document.forms[0]["#form.taxRate"].value +
	                "&returnDate=" + document.forms[0]["#form.shipDate"].value +
	                "&origDeliveryOrderNo=" + document.forms[0]["#form.origDeliveryOrderNo"].value +
	                "&formStatus=" + document.forms[0]["#form.status"].value;
	    vat.ajax.startRequest(processString, function () {
		    if (vat.ajax.handleState()) {
			    vat.block.pageRefresh(vnB_Detail);
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
		doPageDataSave();		
	}
}

/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	if (confirm("是否確認送出?")) {		
		//save line
		afterSavePageProcess = "submitHandler";
		doPageDataSave();	
	}
}

/*
	背景送出SUBMIT HEAD && LINE
*/
function doSubmitBgHandler() {
	if (confirm("是否確認背景送出?")) {		
		//save line
		afterSavePageProcess = "submitBgHandler";	
		doPageDataSave();
	}
}

/*
	作廢VOID HEAD && LINE
*/
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		afterSavePageProcess = "voidHandler";
		doPageDataSave();			
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
	afterSavePageProcess = "executeImport";
	doPageDataSave();
}

function changeCustomerCode(){
    //vat.formD.pageDataSave(0);
    document.forms[0]["#form.customerCode"].value = document.forms[0]["#form.customerCode"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#form.customerCode"].value !== ""){
        vat.ajax.XHRequest(
       {
           post:"process_object_name=buCustomerWithAddressViewService"+
                    "&process_object_method_name=findCustomerByTypeForAJAX"+
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value + 
                    "&customerCode=" + document.forms[0]["#form.customerCode"].value +
                    "&searchCustomerType=customerCode" +
                    "&isEnable=",
           find: function changeCustomerCodeRequestSuccess(oXHR){
               document.forms[0]["#form.customerCode"].value = vat.ajax.getValue("CustomerCode_var", oXHR.responseText);
               document.forms[0]["#form.customerName"].value = vat.ajax.getValue("CustomerName", oXHR.responseText);
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
           }   
       });  
    }else{
        document.forms[0]["#form.customerCode"].value = "";
        document.forms[0]["#form.customerName"].value = "";
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

function changePromotionCode(){
    document.forms[0]["#form.promotionCode"].value = document.forms[0]["#form.promotionCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();
    if(document.forms[0]["#form.promotionCode"].value !== ""){
        vat.ajax.XHRequest(
        {
           post:"process_object_name=imPromotionService"+
                    "&process_object_method_name=findByBrandCodeAndPromotionCodeForAJAX"+
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
                    "&promotionCode=" + document.forms[0]["#form.promotionCode"].value,                      
           find: function changePromotionCodeRequestSuccess(oXHR){ 
               document.forms[0]["#form.promotionCode"].value =  vat.ajax.getValue("PromotionCode", oXHR.responseText);
               document.forms[0]["#form.promotionName"].value =  vat.ajax.getValue("PromotionName", oXHR.responseText);
           }
        });
    }else{
        document.forms[0]["#form.promotionName"].value = "";
    }
}

function getOriginalDelivery(){
    document.forms[0]["#form.origDeliveryOrderNo"].value = document.forms[0]["#form.origDeliveryOrderNo"].value.replace(/^\s+|\s+$/, ''); 
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=imDeliveryService"+
                    "&process_object_method_name=executeCopyOrigDelivery"+
                    "&headId=" + document.forms[0]["#form.headId"].value + 
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value + 
                    "&employeeCode=" + document.forms[0]["#employeeCode"].value +
                    "&origDeliveryOrderTypeCode=" + document.forms[0]["#form.origDeliveryOrderTypeCode"].value +
                    "&origDeliveryOrderNo=" + document.forms[0]["#form.origDeliveryOrderNo"].value +
                    "&returnDate=" + document.forms[0]["#form.shipDate"].value,
                    
        find: function getOriginalDeliverySuccess(oXHR){
            //document.forms[0]["#form.origDeliveryOrderTypeCode"].value = vat.ajax.getValue("OrigDeliveryOrderTypeCode", oXHR.responseText);
            document.forms[0]["#form.origDeliveryOrderNo"].value = vat.ajax.getValue("OrigDeliveryOrderNo", oXHR.responseText);
            document.forms[0]["#form.returnDate"].value = vat.ajax.getValue("OrigShipDate", oXHR.responseText);
            document.forms[0]["#form.shipDate"].value = vat.ajax.getValue("ReturnDate", oXHR.responseText);      
            document.forms[0]["#form.customerCode"].value = vat.ajax.getValue("CustomerCode", oXHR.responseText);
            document.forms[0]["#form.customerName"].value = vat.ajax.getValue("CustomerName", oXHR.responseText);
            document.forms[0]["#form.customerPoNo"].value = vat.ajax.getValue("CustomerPoNo", oXHR.responseText);
            document.forms[0]["#form.quotationCode"].value = vat.ajax.getValue("QuotationCode", oXHR.responseText);
            document.forms[0]["#form.paymentTermCode"].value = vat.ajax.getValue("PaymentTermCode", oXHR.responseText);
            document.forms[0]["#form.countryCode"].value = vat.ajax.getValue("CountryCode", oXHR.responseText);
            document.forms[0]["#form.currencyCode"].value = vat.ajax.getValue("CurrencyCode", oXHR.responseText);
            document.forms[0]["#form.contactPerson"].value = vat.ajax.getValue("ContactPerson", oXHR.responseText);
            document.forms[0]["#form.contactTel"].value = vat.ajax.getValue("ContactTel", oXHR.responseText);      
            document.forms[0]["#form.receiver"].value = vat.ajax.getValue("Receiver", oXHR.responseText);
            document.forms[0]["#form.superintendentCode"].value = vat.ajax.getValue("SuperintendentCode", oXHR.responseText);
            document.forms[0]["#form.superintendentName"].value = vat.ajax.getValue("SuperintendentName", oXHR.responseText);
            document.forms[0]["#form.invoiceTypeCode"].value = vat.ajax.getValue("InvoiceTypeCode", oXHR.responseText);
            document.forms[0]["#form.guiCode"].value = vat.ajax.getValue("GuiCode", oXHR.responseText);            
            document.forms[0]["#form.taxType"].value = vat.ajax.getValue("TaxType", oXHR.responseText);
            document.forms[0]["#form.taxRate"].value = vat.ajax.getValue("TaxRate", oXHR.responseText);
            document.forms[0]["#form.scheduleCollectionDate"].value = vat.ajax.getValue("ScheduleCollectionDate", oXHR.responseText);
            document.forms[0]["#form.invoiceCity"].value = vat.ajax.getValue("InvoiceCity", oXHR.responseText);     
            document.forms[0]["#form.invoiceArea"].value = vat.ajax.getValue("InvoiceArea", oXHR.responseText);
            document.forms[0]["#form.invoiceZipCode"].value = vat.ajax.getValue("InvoiceZipCode", oXHR.responseText);
            document.forms[0]["#form.invoiceAddress"].value = vat.ajax.getValue("InvoiceAddress", oXHR.responseText);
            document.forms[0]["#form.shipCity"].value = vat.ajax.getValue("ShipCity", oXHR.responseText);
            document.forms[0]["#form.shipArea"].value = vat.ajax.getValue("ShipArea", oXHR.responseText);
            document.forms[0]["#form.shipZipCode"].value = vat.ajax.getValue("ShipZipCode", oXHR.responseText);
            document.forms[0]["#form.shipAddress"].value = vat.ajax.getValue("ShipAddress", oXHR.responseText);                    
            document.forms[0]["#form.promotionCode"].value = vat.ajax.getValue("PromotionCode", oXHR.responseText);
            document.forms[0]["#form.promotionName"].value = vat.ajax.getValue("PromotionName", oXHR.responseText);
            document.forms[0]["#form.discountRate"].value = vat.ajax.getValue("DiscountRate", oXHR.responseText);
            document.forms[0]["#form.sufficientQuantityDelivery"].value = vat.ajax.getValue("SufficientQuantityDelivery", oXHR.responseText);
            //document.forms[0]["#form.invoiceNo"].value = vat.ajax.getValue("InvoiceNo", oXHR.responseText);
            document.forms[0]["#form.remark1"].value = vat.ajax.getValue("Remark1", oXHR.responseText);          
            document.forms[0]["#form.remark2"].value = vat.ajax.getValue("Remark2", oXHR.responseText);
            document.forms[0]["#form.homeDelivery"].value = vat.ajax.getValue("HomeDelivery", oXHR.responseText);
            document.forms[0]["#form.paymentCategory"].value = vat.ajax.getValue("PaymentCategory", oXHR.responseText);
            document.forms[0]["#form.attachedInvoice"].value = vat.ajax.getValue("AttachedInvoice", oXHR.responseText);
            var shopCodeArray = vat.ajax.getValue("ShopCodeArray", oXHR.responseText);
            shopCodeArray = vat.utils.strTwoInputDArray("#form.shopCode", "", 'true', shopCodeArray);  
            vat.formD.itemSelectBind(shopCodeArray);
            document.forms[0]["#form.shopCode"].value = vat.ajax.getValue("ShopCode", oXHR.responseText);          
            var defaultWarehouseCodeArray = vat.ajax.getValue("DefaultWarehouseCodeArray", oXHR.responseText);
            defaultWarehouseCodeArray = vat.utils.strTwoInputDArray("#form.defaultWarehouseCode", "", 'true', defaultWarehouseCodeArray);  
            vat.formD.itemSelectBind(defaultWarehouseCodeArray);
            document.forms[0]["#form.defaultWarehouseCode"].value = vat.ajax.getValue("DefaultWarehouseCode", oXHR.responseText);
            doPageRefresh();
            var findOrigDeliveryResult = vat.ajax.getValue("FindOrigDeliveryResult", oXHR.responseText);
            if("N" == findOrigDeliveryResult){
                alert("查無" + document.forms[0]["#form.origDeliveryOrderTypeCode"].value + "-" + document.forms[0]["#form.origDeliveryOrderNo"].value + "的出貨單資料！");
            }else if(findOrigDeliveryResult != null && findOrigDeliveryResult != ""){
                alert(document.forms[0]["#form.origDeliveryOrderTypeCode"].value + "-" + document.forms[0]["#form.origDeliveryOrderNo"].value + "的出貨單狀態為" + findOrigDeliveryResult + "無法執行退貨！");
            }
            
            if(document.forms[0]["#form.origDeliveryOrderNo"].value != null && document.forms[0]["#form.origDeliveryOrderNo"].value != ""){
                vat.form.item.readonly("vatHeadDiv");	//單頭 設定為ReadOnly
	            vat.form.item.readonly("vatMasterDiv");	//單身 設定為ReadOnly
                vat.form.item.none("vatImportBottonDiv");	 //Hidden 明細匯入按鍵
                vat.item.setGridAttributeByName("itemCode", "readOnly", true);
	            vat.item.setGridAttributeByName("lotNo", "readOnly", true);
	            vat.item.setGridAttributeByName("originalUnitPrice", "readOnly", true);
	            vat.item.setGridAttributeByName("actualUnitPrice", "readOnly", true); 
	            //vat.item.setGridStyleByName("isDeleteRecord", "visibility", "hidden");             
                if(document.forms[0]["#form.status"].value == "SAVE" || document.forms[0]["#form.status"].value == "REJECT"){	
                    vat.form.item.enable("vatReturnDateDiv");
                    vat.form.item.enable("vatDeliveryOrderDiv");
                    vat.form.item.enable("vatDeliveryNoDiv");
                    vat.form.item.enable("vatReturnReasonDiv");                
	            }
            }else{
                vat.form.item.enable("vatHeadDiv");
                vat.form.item.enable("vatMasterDiv");
                vat.form.item.inline("vatImportBottonDiv");
                vat.form.item.readonly("vatOrderTypeDiv");
                vat.form.item.readonly("vatOrderNoDiv");
                vat.form.item.readonly("vatHeadIdDiv");
                vat.form.item.readonly("vatBrandCodeDiv");
                vat.form.item.readonly("vatShipDateDiv");
                vat.form.item.readonly("vatInvoiceNoDiv");
                vat.form.item.readonly("vatCustomerNameDiv");
                vat.form.item.readonly("vatSuperintendentNameDiv");
                vat.form.item.readonly("vatPromotionNameDiv");             
                vat.item.setGridAttributeByName("itemCode", "readOnly", false);
	            vat.item.setGridAttributeByName("lotNo", "readOnly", false);
	            vat.item.setGridAttributeByName("originalUnitPrice", "readOnly", false);
	            vat.item.setGridAttributeByName("actualUnitPrice", "readOnly", false);
            }              
       }   
   }); 
}














function doPageDataSave(){
    vat.block.pageSearch(vnB_Detail);
}

function doPageRefresh(){
    vat.block.pageRefresh(vnB_Detail);
}

function appendBeforeMethod(){
    var origDeliveryOrderNoTmp = document.forms[0]["#form.origDeliveryOrderNo"].value.replace(/^\s+|\s+$/, '');
    if(origDeliveryOrderNoTmp == null || origDeliveryOrderNoTmp == ""){
	    return true;
	}
}

function appendAfterMethod(){
    // return alert("新增完畢");
}

function pageLoadSuccess(){
	// alert("載入成功");	
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

function changeItemData(actionId) {
    var nItemLine = vat.item.getGridLine();   
    var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();  
	var vWarehouseCode = vat.item.getGridValueByName("warehouseCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vOriginalUnitPrice = vat.item.getGridValueByName("originalUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualUnitPrice = vat.item.getGridValueByName("actualUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vShipQuantity = vat.item.getGridValueByName("shipQuantity", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxType = vat.item.getGridValueByName("taxType", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxRate = vat.item.getGridValueByName("taxRate", nItemLine).replace(/^\s+|\s+$/, '');
	var vWatchSerialNo = vat.item.getGridValueByName("watchSerialNo", nItemLine).replace(/^\s+|\s+$/, '');
	
	vat.item.setGridValueByName("itemCode", nItemLine, vItemCode);
    vat.item.setGridValueByName("warehouseCode", nItemLine, vWarehouseCode);	
	vat.ajax.XHRequest(
    {
        post:"process_object_name=imDeliveryService" +
                    "&process_object_method_name=getAJAXItemDataForReturn" +
                     "&brandCode=" + document.forms[0]["#form.brandCode"].value +
                     "&priceType=" + document.forms[0]["#priceType"].value +
                     "&itemIndexNo" + nItemLine +
                     "&itemCode=" + vItemCode +
                     "&warehouseCode=" + vWarehouseCode +
                     "&originalUnitPrice=" + vOriginalUnitPrice +
                     "&actualUnitPrice=" + vActualUnitPrice +
                     "&returnQuantity=" + vShipQuantity +
                     "&returnDate=" + document.forms[0]["#form.shipDate"].value +
                     "&taxType=" + vTaxType +
                     "&taxRate=" + vTaxRate +
                     "&watchSerialNo=" + vWatchSerialNo +
                     "&actionId=" + actionId,
                                                  
        find: function changeItemDataRequestSuccess(oXHR){                    
            vat.item.setGridValueByName("itemCode", nItemLine, vat.ajax.getValue("ItemCode", oXHR.responseText));	
		    vat.item.setGridValueByName("itemCName", nItemLine, vat.ajax.getValue("ItemCName", oXHR.responseText)); 
            vat.item.setGridValueByName("warehouseCode", nItemLine, vat.ajax.getValue("WarehouseCode", oXHR.responseText)); 
            vat.item.setGridValueByName("warehouseName", nItemLine, vat.ajax.getValue("WarehouseName", oXHR.responseText)); 	    
		    vat.item.setGridValueByName("originalUnitPrice", nItemLine, vat.ajax.getValue("OriginalUnitPrice", oXHR.responseText));
		    vat.item.setGridValueByName("actualUnitPrice", nItemLine, vat.ajax.getValue("ActualUnitPrice", oXHR.responseText));		    
		    vat.item.setGridValueByName("shipQuantity", nItemLine, vat.ajax.getValue("ShipQuantity", oXHR.responseText));
		    vat.item.setGridValueByName("originalShipAmount", nItemLine, vat.ajax.getValue("OriginalShipAmount", oXHR.responseText));
		    vat.item.setGridValueByName("actualShipAmount", nItemLine, vat.ajax.getValue("ActualShipAmount", oXHR.responseText));
		    vat.item.setGridValueByName("shipTaxAmount", nItemLine, vat.ajax.getValue("ShipTaxAmount", oXHR.responseText));
		    vat.item.setGridValueByName("watchSerialNo", nItemLine, vat.ajax.getValue("WatchSerialNo", oXHR.responseText));            
        }
    });  
}

function advanceInput(){
    var nItemLine = vat.item.getGridLine();   
    var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vWarehouseCode = vat.item.getGridValueByName("warehouseCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vLotNo = vat.item.getGridValueByName("lotNo", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();			
	var vOriginalUnitPrice = vat.item.getGridValueByName("originalUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualUnitPrice = vat.item.getGridValueByName("actualUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');	
	var vReturnableQuantity = vat.item.getGridValueByName("returnableQuantity",nItemLine).replace(/^\s+|\s+$/, '');
	var vShipQuantity = vat.item.getGridValueByName("shipQuantity", nItemLine).replace(/^\s+|\s+$/, '');
	var vOriginalShipAmount = vat.item.getGridValueByName("originalShipAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualShipAmount = vat.item.getGridValueByName("actualShipAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxType = vat.item.getGridValueByName("taxType", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxRate = vat.item.getGridValueByName("taxRate", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxAmount = vat.item.getGridValueByName("shipTaxAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vWatchSerialNo = vat.item.getGridValueByName("watchSerialNo", nItemLine).replace(/^\s+|\s+$/, '');		
	var vLineId = vat.item.getGridValueByName("lineId", nItemLine).replace(/^\s+|\s+$/, '');
	var vOrigDeliveryOrderNo = document.forms[0]["#form.origDeliveryOrderNo"].value.replace(/^\s+|\s+$/, '');	
	
	var obj = document.getElementById("vatBeginDiv");
	if (obj){
		obj.filters[0].enabled = true;
		obj.filters[0].opacity = 0.60; 
	}
	
	var returnData = window.showModalDialog(
		"Im_Delivery:returnAdvanceInput:1.page"+
		"?headId=" + document.forms[0]["#form.headId"].value +
		"&brandCode=" + document.forms[0]["#form.brandCode"].value +
        "&priceType=" + document.forms[0]["#priceType"].value +
        "&returnDate=" + document.forms[0]["#form.shipDate"].value +
        "&status=" + document.forms[0]["#form.status"].value +
        "&itemIndexNo=" + nItemLine +                        
        "&itemCode=" + vItemCode +
        "&warehouseCode=" + vWarehouseCode +
        "&lotNo=" + vLotNo +
        "&originalUnitPrice=" + vOriginalUnitPrice +
        "&actualUnitPrice=" + vActualUnitPrice +
        "&returnableQuantity=" + vReturnableQuantity +
        "&shipQuantity=" + vShipQuantity +
        "&originalShipAmount=" + vOriginalShipAmount +
        "&actualShipAmount=" + vActualShipAmount +
        "&taxType=" + vTaxType +
        "&taxRate=" + vTaxRate +
        "&taxAmount=" + vTaxAmount +
        "&watchSerialNo=" + vWatchSerialNo +
        "&origDeliveryOrderNo=" + vOrigDeliveryOrderNo +
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
		    vat.item.setGridValueByName("lotNo", nItemLine, returnDataArray[4]);
		    vat.item.setGridValueByName("originalUnitPrice", nItemLine, returnDataArray[5]);
		    vat.item.setGridValueByName("actualUnitPrice", nItemLine, returnDataArray[6]);		    
		    vat.item.setGridValueByName("shipQuantity", nItemLine, returnDataArray[7]);
		    vat.item.setGridValueByName("originalShipAmount", nItemLine, returnDataArray[8]);
		    vat.item.setGridValueByName("actualShipAmount", nItemLine, returnDataArray[9]);
		    vat.item.setGridValueByName("shipTaxAmount", nItemLine, returnDataArray[10]);
		    vat.item.setGridValueByName("watchSerialNo", nItemLine, returnDataArray[11]);         
		}
		
	obj.filters[0].enabled = false;
}

function changeAdvanceData(actionId){
    var vItemIndexNo = document.forms[0]["#itemIndexNo"].value;
    var vBrandCode = document.forms[0]["#brandCode"].value;
    var vPriceType = document.forms[0]["#priceType"].value;
    var vItemCode = document.forms[0]["#itemCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();
    var vWarehouseCode = document.forms[0]["#warehouseCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();
    var vReturnDate= document.forms[0]["#returnDate"].value;
    var vTaxType= document.forms[0]["#taxType"].value;
    var vTaxRate= document.forms[0]["#taxRate"].value.replace(/^\s+|\s+$/, '');  
    if(vTaxType == "1" || vTaxType == "2"){
        vTaxRate = "0.0";
    }
    var vOriginalUnitPrice= document.forms[0]["#originalUnitPrice"].value;
    var vActualUnitPrice= document.forms[0]["#actualUnitPrice"].value;   
    var vShipQuantity = document.forms[0]["#shipQuantity"].value.replace(/^\s+|\s+$/, '');
    var vWatchSerialNo = document.forms[0]["#watchSerialNo"].value.replace(/^\s+|\s+$/, '');
    
    if(isNaN(vOriginalUnitPrice)){
        alert("原單價必須為數值！");
    }else if(isNaN(vActualUnitPrice)){
        alert("實際單價必須為數值！");
    }else if(isNaN(vShipQuantity)){
        alert("退貨數量必須為數值！");
    }else{   
        vat.ajax.XHRequest(
        {
            post:"process_object_name=imDeliveryService" +
            "&process_object_method_name=getAJAXItemDataForReturn" +
            "&itemIndexNo" + vItemIndexNo +
            "&brandCode=" + vBrandCode +
            "&priceType=" + vPriceType + 
            "&itemCode=" + vItemCode +
            "&warehouseCode=" + vWarehouseCode +
            "&returnDate=" + vReturnDate +
            "&taxType=" + vTaxType +
            "&taxRate=" + vTaxRate +
            "&originalUnitPrice=" + vOriginalUnitPrice +
            "&actualUnitPrice=" + vActualUnitPrice +
            "&returnQuantity=" + vShipQuantity +
            "&watchSerialNo=" + vWatchSerialNo +        
            "&actionId=" + actionId,                                                 
            find: function changeAdvanceItemDataRequestSuccess(oXHR){
                document.forms[0]["#itemCode"].value = vat.ajax.getValue("ItemCode", oXHR.responseText);
                document.forms[0]["#itemName"].value = vat.ajax.getValue("ItemCName", oXHR.responseText);
                document.forms[0]["#warehouseCode"].value = vat.ajax.getValue("WarehouseCode", oXHR.responseText);
                document.forms[0]["#warehouseName"].value = vat.ajax.getValue("WarehouseName", oXHR.responseText);
                document.forms[0]["#originalUnitPrice"].value = vat.ajax.getValue("OriginalUnitPrice", oXHR.responseText);              
                document.forms[0]["#actualUnitPrice"].value = vat.ajax.getValue("ActualUnitPrice", oXHR.responseText);
                document.forms[0]["#shipQuantity"].value = vat.ajax.getValue("ShipQuantity", oXHR.responseText);
                document.forms[0]["#originalShipAmount"].value = vat.ajax.getValue("OriginalShipAmount", oXHR.responseText);
                document.forms[0]["#actualShipAmount"].value = vat.ajax.getValue("ActualShipAmount", oXHR.responseText);
                document.forms[0]["#taxAmount"].value = vat.ajax.getValue("ShipTaxAmount", oXHR.responseText);
                document.forms[0]["#watchSerialNo"].value = vat.ajax.getValue("WatchSerialNo", oXHR.responseText);
            }
        });
    }
}

function advanceDataSave(){
    //var opener = window.dialogArguments;
    var vItemCode = document.forms[0]["#itemCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();;
    var vItemName = document.forms[0]["#itemName"].value.replace(/^\s+|\s+$/, '');
    var vWarehouseCode = document.forms[0]["#warehouseCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();;
    var vWarehouseName = document.forms[0]["#warehouseName"].value.replace(/^\s+|\s+$/, '');  
    var vLotNo = document.forms[0]["#lotNo"].value.replace(/^\s+|\s+$/, '');
    var vOriginalUnitPrice = document.forms[0]["#originalUnitPrice"].value; 
    var vActualUnitPrice = document.forms[0]["#actualUnitPrice"].value.replace(/^\s+|\s+$/, '');    
    var vShipQuantity = document.forms[0]["#shipQuantity"].value;   
    var vOriginalShipAmount = document.forms[0]["#originalShipAmount"].value;   
    var vActualShipAmount = document.forms[0]["#actualShipAmount"].value.replace(/^\s+|\s+$/, ''); 
    var vTaxAmount= document.forms[0]["#taxAmount"].value;
    var vWatchSerialNo= document.forms[0]["#watchSerialNo"].value;
    
    if(isNaN(vOriginalUnitPrice)){
        alert("原單價必須為數值！");
    }else if(isNaN(vActualUnitPrice)){
        alert("實際單價必須為數值！");
    }else if(isNaN(vShipQuantity)){
        alert("退貨數量必須為數值！");
    }else{       
        window.returnValue = vItemCode + "{#}" + vItemName + "{#}" + vWarehouseCode + "{#}" + vWarehouseName + "{#}" + vLotNo + "{#}" + vOriginalUnitPrice + "{#}" + 
                             vActualUnitPrice + "{#}" + vShipQuantity + "{#}" + vOriginalShipAmount + "{#}" + vActualShipAmount + "{#}" + vTaxAmount + "{#}" + 
                             vWatchSerialNo;
        //alert("returnValue:" + window.returnValue);
        window.close();  
    }
}

function changeRelationData(){
    var statusTmp = document.forms[0]["#form.status"].value;
    var orderNoTmp = document.forms[0]["#form.orderNo"].value;
    var processId = document.forms[0]["#processId"].value;
    //var origDeliveryOrderNoTmp = document.forms[0]["#form.origDeliveryOrderNo"].value.replace(/^\s+|\s+$/, '');
    if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){      
        afterSavePageProcess = "changeRelationData";
		doPageDataSave();	
	}  
}

function outlineBlock(){
  headerInitial();
  masterInitial();
}

function headerInitial(){
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: false,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"銷退單維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderType", type:"LABEL", value:"單別"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode", back:false, mode:"READONLY", ceap:"#form.orderTypeCode"}]},		 
	 {items:[{name:"#L.orderNo", type:"LABEL", value:"單號"}]},
	 {items:[{name:"#F.orderNo", type:"TEXT", bind:"orderNo", back:false, size:20, mode:"READONLY", ceap:"#form.orderNo"},
	 		 {name:"#F.headId", type:"TEXT", bind:"headId", back:false, size:10, mode:"READONLY", ceap:"#form.headId"}]},
	 {items:[{name:"#L.shipDate", type:"LABEL", value:"退貨日期"}]},
	 {items:[{name:"#F.shipDate", type:"DATE", bind:"shipDate", size:1, ceap:"#form.shipDate"}]},
	 {items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},
	 {items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", back:false, size:8, mode:"READONLY", ceap:"#form.brandCode"},
	         {name:"#F.brandName", type:"TEXT", bind:"brandName", size:12, mode:"READONLY"}]}, 
	 {items:[{name:"#L.status", type:"LABEL", value:"狀態"}]},
	 {items:[{name:"#F.status", type:"TEXT", bind:"status", size:12, mode:"READONLY"},
	         {name:"#F.statusName", type:"TEXT", bind:"statusName", size:12, mode:"READONLY"}]}]},	 			 	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.origDeliveryOrderTypeCode", type:"LABEL", value:"出貨單別"}]},
	 {items:[{name:"#F.origDeliveryOrderTypeCode", type:"SELECT", bind:"origDeliveryOrderTypeCode", ceap:"#form.origDeliveryOrderTypeCode"}]},	 
	 {items:[{name:"#L.origDeliveryOrderNo", type:"LABEL", value:"出貨單號"}]},
	 {items:[{name:"#F.origDeliveryOrderNo", type:"TEXT", bind:"origDeliveryOrderNo", size:12, maxLen:12, ceap:"#form.origDeliveryOrderNo"}]},	 
	 {items:[{name:"#L.returnDate", type:"LABEL", value:"出貨日期"}]},
	 {items:[{name:"#F.returnDate", type:"DATE", bind:"returnDate", size:1, ceap:"#form.returnDate"}]},
	 {items:[{name:"#L.inputFormEmployee", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.inputFormEmployee", type:"TEXT", bind:"inputFormEmployee", mode:"READONLY", size:12}], td:" colSpan=3"}]}, 
	 {row_style:"", cols:[
	 {items:[{name:"#L.customerCode", type:"LABEL", value:"客戶代號"}]},
	 {items:[{name:"#F.customerCode", type:"TEXT", bind:"customerCode", size:15, maxLen:15, ceap:"#form.customerCode"},
	         {name:"#F.customerName", type:"TEXT", bind:"customerName", size:12, mode:"READONLY"}], td:" colSpan=2"}, 
	 {items:[{name:"#L.guiCode", type:"LABEL", value:"統一編號"}]},	 
	 {items:[{name:"#F.guiCode", type:"TEXT", bind:"guiCode", size:8, maxLen:8, ceap:"#form.guiCode"}]}, 	 
	 {items:[{name:"#L.inputFormDate", type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.inputFormDate", type:"TEXT", bind:"inputFormDate", mode:"READONLY", size:12}], td:" colSpan=3"}]},	 	 
 	 {row_style:"", cols:[
	 {items:[{name:"#L.returnReason", type:"LABEL", value:"退貨原因"}]},
	 {items:[{name:"#F.returnReason", type:"TEXT", bind:"reserve1", size:15, maxLen:15, ceap:"#form.reserve1"}], td:" colSpan=7"}]}], 
	 
	 beginService:"",
	 closeService:""			
	});
}

function masterInitial(){
vat.block.create(vnB_Master, {
	id: "vatBlock_Master", generate: false,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.shopCode", type:"LABEL", value:"專櫃代號"}]},
	 {items:[{name:"#F.shopCode", type:"SELECT", bind:"shopCode", mode:"READONLY", ceap:"#form.shopCode"}]},	 
	 {items:[{name:"#L.sufficientQuantityDelivery", type:"LABEL", value:"足量出貨"}]},
	 {items:[{name:"#F.sufficientQuantityDelivery", type:"SELECT", bind:"sufficientQuantityDelivery", ceap:"#form.sufficientQuantityDelivery"}]},
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
	 {items:[{name:"#L.countryCode", type:"LABEL", value:"國別"}]},
	 {items:[{name:"#F.countryCode", type:"SELECT", bind:"countryCode", ceap:"#form.countryCode"}]}]},	 	 	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.paymentTermCode", type:"LABEL", value:"付款條件"}]},
	 {items:[{name:"#F.paymentTermCode", type:"SELECT", bind:"paymentTermCode", ceap:"#form.paymentTermCode"}]},	
	 {items:[{name:"#L.scheduleCollectionDate", type:"LABEL", value:"付款日"}]},
	 {items:[{name:"#F.scheduleCollectionDate", type:"DATE", bind:"scheduleCollectionDate", ceap:"#form.scheduleCollectionDate"}]},	 
	 {items:[{name:"#L.currencyCode", type:"LABEL", value:"幣別"}]},
	 {items:[{name:"#F.currencyCode", type:"SELECT", bind:"currencyCode", ceap:"#form.currencyCode"}]}]},	 	 
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
	 {items:[{name:"#L.attachedInvoice", type:"LABEL", value:"隨附發票"}]},
	 {items:[{name:"#F.attachedInvoice", type:"SELECT" , bind:"attachedInvoice", ceap:"#form.attachedInvoice"}]}, 	 
	 {items:[{name:"#L.homeDelivery", type:"LABEL", value:"運送方式"}]},
	 {items:[{name:"#F.homeDelivery", type:"SELECT" , bind:"homeDelivery", ceap:"#form.homeDelivery"}]}]},	
     {row_style:"", cols:[	 		 
	 {items:[{name:"#L.promotionCode", type:"LABEL", value:"活動代號"}]},
	 {items:[{name:"#F.promotionCode", type:"TEXT" , bind:"promotionCode", size:20, maxLen:20, ceap:"#form.promotionCode"}]}, 
	 {items:[{name:"#L.discountRate", type:"LABEL", value:"折扣比率"}]},
	 {items:[{name:"#F.discountRate", type:"TEXT" , bind:"discountRate", size:8, maxLen:4, ceap:"#form.discountRate"}]},
	 {items:[{name:"#L.paymentCategory", type:"LABEL", value:"付款方式"}]},
	 {items:[{name:"#F.paymentCategory", type:"SELECT" , bind:"paymentCategory", ceap:"#form.paymentCategory"}]}]},	 	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark1", type:"LABEL", value:"備註一"}]},
	 {items:[{name:"#F.remark1", type:"TEXT", bind:"remark1", size:100, maxLen:100, ceap:"#form.remark1"}], td:" colSpan=3"}]}, 
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark2", type:"LABEL", value:"備註二"}]},
	 {items:[{name:"#F.remark2", type:"TEXT", bind:"remark2", size:100, maxLen:100, ceap:"#form.remark2"}], td:" colSpan=5"}]}],
	  
	 beginService:"",
	 closeService:""			
	});
}

function exportFormData(){
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IR_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=imDeliveryService" + 
              "&processObjectMethodName=findImDeliveryHeadById" + 
              "&gridFieldName=imDeliveryLines" + 
              "&arguments=" + document.forms[0]["#form.headId"].value + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '銷退單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function importFormData(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=IR_ITEM" +
		"&importFileType=XLS" +
        "&processObjectName=imDeliveryService" + 
        "&processObjectMethodName=executeImportItems" +
        "&arguments=" + document.forms[0]["#form.headId"].value + "{$}"
                      + document.forms[0]["#form.shipDate"].value + "{$}" 
                      + document.forms[0]["#priceType"].value + "{$}"
                      + document.forms[0]["#form.taxType"].value + "{$}" 
                      + document.forms[0]["#form.taxRate"].value + "{$}"
                      + document.forms[0]["#form.discountRate"].value +  
        "&parameterTypes=LONG{$}DATE{$}STRING{$}STRING{$}DOUBLE{$}DOUBLE" +
        "&blockId=" + vnB_Detail,
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
    var organizationCode = document.forms[0]["#organizationCode"].value.replace(/^\s+|\s+$/, '');   
    var isCommitOnHand = document.forms[0]["#isCommitOnHand"].value.replace(/^\s+|\s+$/, '');   
        
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
      organizationCode : organizationCode,
      isCommitOnHand : isCommitOnHand
    };
    if("SUBMIT_BG" == actionId){
        vat.block.submit(function(){return "process_object_name=soSalesOrderReturnAction"+
            "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
    }else{
        vat.block.submit(function(){return "process_object_name=soSalesOrderReturnAction"+
            "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
    }
}

function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == ""){
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
		"?programId=SO_RETURN" +
		"&levelType=ERROR" +
        "&processObjectName=imDeliveryService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + document.forms[0]["#form.headId"].value +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=soSalesOrderReturnAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    doPageDataSave();
}