/*
	initial 
*/
var afterSavePageProcess = "";

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
    var url = "jsp/DeliveryReport.jsp?reportUrl=" + reportUrl + "&reportFileName=" + reportFileName + "&crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1=" + orderTypeCode + "&prompt2=" + orderNo + "&prompt3=" + orderNo + "&prompt4=" + displayAmt;
    window.open(url,'BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
}

function detailInitial(){
    var statusTmp = document.forms[0]["#form.status"].value;
    var processId = document.forms[0]["#processId"].value;
	var varCanDataDelete = false;
	var varCanDataAppend = false;
	var varCanDataModify = false;
	if( statusTmp == "SAVE" && processId != null && processId != 0){
		varCanDataAppend = true; //暫時設為true
		varCanDataModify = true;		
	}
    vat.formD.item(0, "F01", {type:"IDX" , desc:"序號"});
	vat.formD.item(0, "F02", {type:"TEXT", size:15, maxLen:20, desc:"品號", mode:"READONLY"});
	vat.formD.item(0, "F03", {type:"TEXT", size:18, maxLen:20, desc:"品名", mode:"READONLY"});
	vat.formD.item(0, "F04", {type:"TEXT", size:12, maxLen:12, desc:"庫別", mode:"READONLY"});
	vat.formD.item(0, "F05", {type:"TEXT", size:20, maxLen:20, desc:"庫名", mode:"HIDDEN"});
	vat.formD.item(0, "F06", {type:"NUMB", size: 8, maxLen:12, desc:"單價", mode:"READONLY"});
	vat.formD.item(0, "F07", {type:"NUMB", size: 8, maxLen:12, desc:"折扣後單價", mode:"READONLY"});
	vat.formD.item(0, "F08", {type:"NUMB", size: 8, maxLen: 8, desc:"預計出貨數", mode:"READONLY"});
	vat.formD.item(0, "F09", {type:"NUMB", size: 8, maxLen: 8, desc:"實計出貨數", onchange:"changeItemData()"});
	vat.formD.item(0, "F10", {type:"NUMB", size: 8, maxLen:20, desc:"金額", mode:"READONLY"});
	vat.formD.item(0, "F11", {type:"NUMB", size: 8, maxLen:20, desc:"折扣後金額", mode:"READONLY"});
	vat.formD.item(0, "F12", {type:"ROWID"});
	vat.formD.item(0, "F13", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.formD.item(0, "F14", {type:"MSG", desc:"訊息"});

	vat.formD.pageLayout(0, {	pageSize: 10,
	                            canDataDelete:varCanDataDelete,
								canDataAppend:varCanDataAppend,
								canDataModify:varCanDataModify,														
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
								eventService        : "",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()",
								saveFailureAfter    : ""
														});
	vat.formD.pageDataLoad(0, 1);
	
}

function loadBeforeAjxService(){
	var processString = "process_object_name=imDeliveryService&process_object_method_name=getAJAXPageData" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value + 
	                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                    "&formStatus=" + document.forms[0]["#form.status"].value;
																					
	return processString;											
}

function changeItemData() {
    var nItemLine = vat.formD.itemIndexNo();
    var vOriginalUnitPrice = vat.formD.itemDataGet(nItemLine, "originalUnitPrice").replace(/^\s+|\s+$/, '');
    var vActualUnitPrice = vat.formD.itemDataGet(nItemLine, "actualUnitPrice").replace(/^\s+|\s+$/, '');
	var vShipQuantity = vat.formD.itemDataGet(nItemLine, "shipQuantity").replace(/^\s+|\s+$/, '');
	
    if(isNaN(vShipQuantity)){
        alert("明細資料頁籤中第" + nItemLine + "項明細的實際出貨數量欄位必須為數值！");
    }else{
        vat.ajax.XHRequest(
        {
            post:"process_object_name=imDeliveryService" +
                     "&process_object_method_name=getAJAXItemData" +
                     "&itemIndexNo" + nItemLine +
                     "&originalUnitPrice=" + vOriginalUnitPrice +
                     "&actualUnitPrice=" + vActualUnitPrice +
                     "&shipQuantity=" + vShipQuantity,
                                                  
                   find: function changeItemDataRequestSuccess(oXHR){
                       vat.formD.itemIdDataBind(0,  8, vat.ajax.getValue("ShipQuantity", oXHR.responseText));
                       vat.formD.itemIdDataBind(0,  9, vat.ajax.getValue("OriginalShipAmount", oXHR.responseText)); 
                       vat.formD.itemIdDataBind(0, 10, vat.ajax.getValue("ActualShipAmount", oXHR.responseText)); 
                   }
               });          
            }
       
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imDeliveryService&process_object_method_name=updateAJAXPageLinesData" + "&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value;
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
		    var processString = "process_object_name=imDeliveryService&process_object_method_name=executeCountTotalAmount" + 
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
    if((document.forms[0]["#form.shipDate"].value != document.forms[0]["#form.scheduleShipDate"].value) && document.forms[0]["#form.status"].value == "SAVE"){
        if (confirm("出貨日期不等於預計出貨日，是否確認送出?")){
            //save line
		    vat.formD.pageDataSave(0);
		    afterSavePageProcess = "submitHandler";
        }
    }else if (confirm("是否確認送出?")){		
		//save line
		vat.formD.pageDataSave(0);
		afterSavePageProcess = "submitHandler";	
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

function doPageDataSave(){
    vat.formD.pageDataSave(0);
}

function doPageRefresh(){
    vat.formD.pageRefresh(0);
}

function appendBeforeMethod(){
	//return true;
}

function appendAfterMethod(){
    // return alert("新增完畢");
}

function pageLoadSuccess(){
	// alert("載入成功");	
}