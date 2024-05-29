/*
	initial 
*/
vat.debug.enable();
var afterSavePageProcess = "";
var afterSaveShopPageProcess = "";

function detailInitial(){
    var statusTmp = document.forms[0]["#form.status"].value;
    var orderNoTmp = document.forms[0]["#form.orderNo"].value;
    var processId = document.forms[0]["#processId"].value;
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	var varCanGridModify = false;
	if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){
		varCanGridDelete = true;
		varCanGridAppend = true;
		varCanGridModify = true;		
	}
	// set column
	var vnA_Detail = 1;
	vat.item.make(vnA_Detail, "indexNo", {type:"IDX" , desc:"序號"}); 	
	vat.item.make(vnA_Detail, "itemCode", {type:"TEXT", size:15, maxLen:20, desc:"品號", mask:"CCCCCCCCCCCC", onchange:"changeItemData()"}); 
	vat.item.make(vnA_Detail, "itemName", {type:"TEXT", size:18, maxLen:20, desc:"品名", mode:"READONLY"});   
	vat.item.make(vnA_Detail, "standardPurchaseCost", {type:"NUMB", size:12, maxLen:12, desc:"成本", mode:"READONLY"});
	vat.item.make(vnA_Detail, "originalPrice", {type:"NUMB", size: 8, maxLen:12, desc:"定價", mode:"READONLY"});    
	vat.item.make(vnA_Detail, "discountAmount", {type:"NUMB", size: 8, maxLen:12, desc:"促銷價", mask:"CCCCCCCCCCCC", onchange:"changeItemData()"});		
	vat.item.make(vnA_Detail, "discountPercentage", {type:"NUMB", size: 3, maxLen: 4, desc:"Discount", mask:"CCCCCCCCCCCC", onchange:"changeItemData()"});
	vat.item.make(vnA_Detail, "totalDiscountAmount", {type:"NUMB", size: 8, maxLen:12, desc:"折扣後金額", mode:"READONLY"});	
	vat.item.make(vnA_Detail, "totalDiscountPercentage", {type:"NUMB", size: 3, maxLen: 4, desc:"Off", mode:"READONLY"});
	vat.item.make(vnA_Detail, "quantity", {type:"NUMB", size: 8, maxLen: 8, desc:"預計銷量"});
	vat.item.make(vnA_Detail, "reserve5", {type:"TEXT", size: 12, maxLen:20, desc:"備註"});	
	vat.item.make(vnA_Detail, "lineId", {type:"ROWID"});
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
	vat.block.pageDataLoad(vnA_Detail, vnCurrentPage = 1);
}

function allShopInitial(){

    vat.block.create(vnB_Header = 2, {
	id: "vatBlock_Head", table:"cellspacing='1' class='' border='0' cellpadding='2'",
	title:"", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#isAllShop", type:"CHECKBOX" , bind:"isAllShop", back:false, eClick:"allShopCheckedMsg()"}]},
	 {items:[{name:"#L_AllShop", type:"LABEL" , value:"所有專櫃&nbsp;"}]},
	 
	 {items:[{name:"#L_beginDate", type:"LABEL" , value:"起始日期"}]},	 
	 {items:[{name:"#beginDate", type:"DATE",  bind:"beginDate", back:false, size:1}]}, 		 
	 {items:[{name:"#L_endDate", type:"LABEL" , value:"&nbsp;&nbsp; ~ &nbsp;&nbsp;結束日期"}]},	 
	 {items:[{name:"#endDate", type:"DATE",  bind:"endDate", back:false, size:1}]}]}], 
	 
	 beginService:"",
	 closeService:""			
	});	
}

function shopInitial(){

    var statusTmp = document.forms[0]["#form.status"].value;
    var orderNoTmp = document.forms[0]["#form.orderNo"].value;
    var processId = document.forms[0]["#processId"].value;
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	var varCanGridModify = false;	
	if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){
		varCanGridDelete = true;
		varCanGridAppend = true;
		varCanGridModify = true;		
	}
	// set column
	var vnB_Detail = 3;
	vat.item.make(vnB_Detail, "indexNo", {type:"IDX", desc:"序號"});
	vat.item.make(vnB_Detail, "shopCode", {type:"TEXT" , size:15, maxLen:20, desc:"專櫃代號", mask:"CCCCCCCCCCCC", onchange:"changeShopData()"});
	vat.item.make(vnB_Detail, "shopName", {type:"TEXT" , size:18, maxLen:20, desc:"專櫃名稱", mode:"READONLY"});	   
	vat.item.make(vnB_Detail, "beginDate", {type:"DATE", size: 8, maxLen:12, desc:"活動啟始日"});
	vat.item.make(vnB_Detail, "endDate", {type:"DATE", size: 8, maxLen:12, desc:"活動結束日"});
	vat.item.make(vnB_Detail, "reserve5", {type:"TEXT", size: 12, maxLen:20, desc:"備註"});	
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});

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
								loadBeforeAjxService: "loadShopBeforeAjxService()",
								loadSuccessAfter    : "pageLoadSuccess()",
								loadFailureAfter    : "",
								eventService        : "",   
								saveBeforeAjxService: "saveShopBeforeAjxService()",
								saveSuccessAfter    : "saveLastSuccessAfter()",
								saveFailureAfter    : ""
														});																							
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);	
}

function loadBeforeAjxService(){
	var processString = "process_object_name=imPromotionService&process_object_method_name=getAJAXPageData" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value + 
	                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                    "&formStatus=" + document.forms[0]["#form.status"].value + 
	                    "&priceType=" + document.forms[0]["#form.priceType"].value; 
																					
	return processString;											
}

function loadShopBeforeAjxService(){
	var processString = "process_object_name=imPromotionService&process_object_method_name=getAJAXPageDataForShop" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value + 
	                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                    "&formStatus=" + document.forms[0]["#form.status"].value;
																					
	return processString;											
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imPromotionService&process_object_method_name=updateAJAXPageLinesData" + "&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value;
	return processString;
}

/*
	SAVE SHOP LINE FUNCTION
*/
function saveShopBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imPromotionService&process_object_method_name=updateAJAXShopLinesData" + "&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value;
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
	} else if ("executeExport" == afterSavePageProcess) {
	    exportFormData();
	    //executeCommandHandlerNoBlock("main","exportDataHandler");
	} else if ("totalCount" == afterSavePageProcess) {
	    getTotalAmt();	
	}
	
	afterSavePageProcess = "";
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveLastSuccessAfter() {
    
    if ("saveHandler" == afterSaveShopPageProcess) {	
	    execSubmitAction("SAVE");
	} else if ("submitHandler" == afterSaveShopPageProcess) {
	    execSubmitAction("SUBMIT");
	} else if ("submitBgHandler" == afterSaveShopPageProcess) {
	    execSubmitAction("SUBMIT_BG");
	} else if ("voidHandler" == afterSaveShopPageProcess) {
		execSubmitAction("VOID");
	} else if ("executeImport" == afterSaveShopPageProcess) {
	    importFormData();
	}

	afterSaveShopPageProcess = "";
}

/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
    if (confirm("是否確認暫存?")) {		
	    //save line
	    afterSavePageProcess = "saveHandler";
		vat.block.pageSearch(1);				
	}
}

/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	if (confirm("是否確認送出?")) {		
		//save line
		afterSavePageProcess = "submitHandler";	
		vat.block.pageSearch(1);
	}
}

/*
	背景送出SUBMIT HEAD && LINE
*/
function doSubmitBgHandler() {
	if (confirm("是否確認背景送出?")) {		
		//save line
		afterSavePageProcess = "submitBgHandler";	
		vat.block.pageSearch(1);
	}
}

/*
	作廢VOID HEAD && LINE
*/
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		afterSavePageProcess = "voidHandler";	
		vat.block.pageSearch(1);			
	}
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
	afterSaveShopPageProcess = "executeImport";
	vat.block.pageSearch(3);	
}

/*
	暫存 SAVE HEAD && LINE
*/
function doActualSaveHandler() { 
    afterSaveShopPageProcess = "saveHandler";		
    vat.block.pageSearch(3);
}

/*
	送出SUBMIT HEAD && LINE
*/
function doActualSubmitHandler() {
    afterSaveShopPageProcess = "submitHandler";	
	vat.block.pageSearch(3);	
}

/*
	背景送出SUBMIT HEAD && LINE
*/
function doActualSubmitBgHandler() {
    afterSaveShopPageProcess = "submitBgHandler";	
	vat.block.pageSearch(3);	
}

/*
	作廢VOID HEAD && LINE
*/
function doActualVoidHandler() {
    afterSaveShopPageProcess = "voidHandler";
	vat.block.pageSearch(3);	
}

/*
	顯示合計的頁面
*/
function showTotalCountPage() {
    //save line
    afterSavePageProcess = "totalCount";
    doPageDataSave();
}

function changeSuperintendent(){
    document.forms[0]["#form.inCharge"].value = document.forms[0]["#form.inCharge"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#form.inCharge"].value !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value + 
                    "&employeeCode=" + document.forms[0]["#form.inCharge"].value,
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               document.forms[0]["#form.inCharge"].value =  vat.ajax.getValue("EmployeeCode", oXHR.responseText);
               document.forms[0]["#form.inChargeName"].value =  vat.ajax.getValue("EmployeeName", oXHR.responseText);
           }   
       });
    }else{
        document.forms[0]["#form.inChargeName"].value = "";
    }
}

function doPageDataSave(){
    vat.block.pageSearch(1);
    vat.block.pageSearch(3);
}

function doPageRefresh(){
    vat.block.pageRefresh(0);
    vat.block.pageRefresh(1);
}

function doPageDataSaveForItem(){
    vat.block.pageRefresh(1);
    vat.block.pageSearch(3);
}

function doPageDataSaveForShop(){
    vat.block.pageRefresh(3);
    vat.block.pageSearch(1);
}

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(1);
    vat.block.pageDataSave(3);
}

function appendBeforeMethod(){
    return true;
}

function appendAfterMethod(){
    // return alert("新增完畢");
}

function pageLoadSuccess(){
	// alert("載入成功");	
}

function changeItemData() {
    var nItemLine = vat.item.getGridLine();	
	var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vOriginalPriceTmp = vat.item.getGridValueByName("originalPrice", nItemLine);
	var vDiscountAmountTmp = vat.item.getGridValueByName("discountAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vDiscountPercentageTmp = vat.item.getGridValueByName("discountPercentage", nItemLine).replace(/^\s+|\s+$/, '');
	var vQuantity = vat.item.getGridValueByName("quantity", nItemLine).replace(/^\s+|\s+$/, '');
	vat.item.setGridValueByName("itemCode", nItemLine, vItemCode);		
	
	if(vDiscountAmountTmp != null && vDiscountAmountTmp != "" && vDiscountPercentageTmp != null && vDiscountPercentageTmp != ""){
	    alert("促銷價與Discount只能擇一輸入！");
	}else if(vDiscountAmountTmp != null && vDiscountAmountTmp != "" && isNaN(vDiscountAmountTmp)){
	    alert("促銷價必須為數值！");
	}else if(vDiscountAmountTmp != null && vDiscountAmountTmp != "" && parseFloat(vDiscountAmountTmp) <= 0){
	    alert("促銷價不可小於或等於零！");
	}else if(vDiscountPercentageTmp != null && vDiscountPercentageTmp != "" && isNaN(vDiscountPercentageTmp)){
	    alert("Discount必須為數值！");
	}else if(vDiscountPercentageTmp != null && vDiscountPercentageTmp != "" && parseFloat(vDiscountPercentageTmp) > 100){
        alert("Discount不可大於100%");
	}else if(vDiscountPercentageTmp != null && vDiscountPercentageTmp != "" && parseFloat(vDiscountPercentageTmp) <= 0){
	    alert("Discount不可小於或等於0%");
	}else if(vQuantity != null && vQuantity != "" && isNaN(vQuantity)){
	    alert("預計銷量必須為數值！");
	}else{
	    vat.ajax.XHRequest(
        {
            post:"process_object_name=imPromotionService" +
                        "&process_object_method_name=getAJAXItemData" +
                        "&brandCode=" + document.forms[0]["#form.brandCode"].value +
                        "&priceType=" + document.forms[0]["#form.priceType"].value +
                        "&itemIndexNo" + nItemLine +
                        "&itemCode=" + vItemCode +
                        "&discountAmount=" + vDiscountAmountTmp +
                        "&discountPercentage=" + vDiscountPercentageTmp +
                        "&quantity=" + vQuantity,
                                                  
            find: function changeItemDataRequestSuccess(oXHR){     
                vat.item.setGridValueByName("itemCode", nItemLine, vat.ajax.getValue("ItemCode", oXHR.responseText));
                vat.item.setGridValueByName("itemName", nItemLine, vat.ajax.getValue("ItemName", oXHR.responseText));
                vat.item.setGridValueByName("standardPurchaseCost", nItemLine, vat.ajax.getValue("StandardPurchaseCost", oXHR.responseText));
                vat.item.setGridValueByName("originalPrice", nItemLine, vat.ajax.getValue("OriginalPrice", oXHR.responseText));
                vat.item.setGridValueByName("discountAmount", nItemLine, vat.ajax.getValue("DiscountAmount", oXHR.responseText));
                vat.item.setGridValueByName("discountPercentage", nItemLine, vat.ajax.getValue("DiscountPercentage", oXHR.responseText));
                vat.item.setGridValueByName("totalDiscountAmount", nItemLine, vat.ajax.getValue("TotalDiscountAmount", oXHR.responseText));
                vat.item.setGridValueByName("totalDiscountPercentage", nItemLine, vat.ajax.getValue("TotalDiscountPercentage", oXHR.responseText));
                vat.item.setGridValueByName("quantity", nItemLine, vat.ajax.getValue("Quantity", oXHR.responseText));
            }
        });
    }
}

function changeShopData() {
    var nItemLine = vat.item.getGridLine();
	var vshopCode = vat.item.getGridValueByName("shopCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setGridValueByName("shopCode", nItemLine, vshopCode);		
	vat.ajax.XHRequest(
    {
        post:"process_object_name=imPromotionService" +
                    "&process_object_method_name=getAJAXShopData" +
                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
                    "&itemIndexNo" + nItemLine +
                    "&shopCode=" + vshopCode +
                    "&formStatus=" + document.forms[0]["#form.status"].value,
                                                                       
        find: function changeItemDataRequestSuccess(oXHR){
            vat.item.setGridValueByName("shopCode", nItemLine, vat.ajax.getValue("ShopCode", oXHR.responseText));
            vat.item.setGridValueByName("shopName", nItemLine, vat.ajax.getValue("ShopName", oXHR.responseText));
        }
    });
}

function outlineBlock(){
  formDataInitial();
  headerInitial();
}

function headerInitial(){
vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: false,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"促銷活動維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L_orderType", type:"LABEL" , value:"單別"}]},	 
	 {items:[{name:"#orderTypeCode", type:"SELECT",  bind:"orderTypeCode", back:false, mode:"READONLY", ceap:"#form.orderTypeCode"}]},		 
	 {items:[{name:"#L_orderNo"  , type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#orderNo"  , type:"TEXT"  ,  bind:"orderNo", back:false, size:12, mode:"READONLY", ceap:"#form.orderNo"},
	 		 {name:"#headId"   , type:"TEXT"  ,  bind:"headId", back:false, size:10, mode:"READONLY", ceap:"#form.headId"}]},
	 {items:[{name:"#L_priceType", type:"LABEL",  value:"價格類型"}]},
	 {items:[{name:"#priceType", type:"SELECT",  bind:"priceType", size:1, ceap:"#form.priceType"}]},			 
	 {items:[{name:"#L_brandCode", type:"LABEL" , value:"品牌"}]},
	 {items:[{name:"#brandCode", type:"TEXT"  ,  bind:"brandCode", size:8, mode:"READONLY", ceap:"#form.brandCode"},
	         {name:"#brandName", type:"TEXT"  ,  bind:"brandName", size:12, mode:"READONLY"}]}, 
	 {items:[{name:"#L_status"   , type:"LABEL" , value:"狀態"}]},	 		 
	 {items:[{name:"#status"   , type:"TEXT"  ,  bind:"status", size:12, mode:"READONLY"},
	         {name:"#statusName"   , type:"TEXT"  ,  bind:"status", size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L_promotionCode", type:"LABEL", value:"活動代號"}]},
	 {items:[{name:"#promotionCode"  , type:"TEXT" , bind:"promotionCode", size:12, ceap:"#form.promotionCode"}]},
	 {items:[{name:"#L_promotionName", type:"LABEL", value:"活動名稱"}]},
	 {items:[{name:"#promotionName"  , type:"TEXT" , bind:"promotionName", size:20, ceap:"#form.promotionName"}], td:" colSpan=3"},
	 {items:[{name:"#L_inputFormEmployee", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#inputFormEmployee" , type:"TEXT",   bind:"inputFormEmployee",  mode:"READONLY", size:12}]},	 
	 {items:[{name:"#L_inputFormDate" , type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#inputFormDate" , type:"TEXT",   bind:"inputFormDate", mode:"READONLY", size:12}]}]},
 	 {row_style:"", cols:[
	 {items:[{name:"#L_promotionNo", type:"LABEL", value:"分析代號"}]},
	 {items:[{name:"#promotionNo"  , type:"TEXT" , bind:"promotionNo", size:12, ceap:"#form.promotionNo"}]},
	 {items:[{name:"#L_inCharge", type:"LABEL", value:"負責人員"}]},
	 {items:[{name:"#inCharge", type:"TEXT"  ,  bind:"inCharge", size:8, mode:"READONLY", ceap:"#form.inCharge"},
	         {name:"#inChargeName", type:"TEXT"  ,  bind:"inChargeName", size:12, mode:"READONLY"}]},	 
	 {items:[{name:"#L_description", type:"LABEL", value:"說明"}]},
	 {items:[{name:"#description" , type:"TEXT",   bind:"description",  mode:"READONLY", size:25, ceap:"#form.description"}], td:" colSpan=5"}]}],	 
	 
	 beginService:"",
	 closeService:""			
	});
}

function execSubmitAction(actionId){
    var formId = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var assignmentId = document.forms[0]["#assignmentId"].value.replace(/^\s+|\s+$/, '');
    var processId = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var status = document.forms[0]["#form.status"].value.replace(/^\s+|\s+$/, '');
    var employeeCode = document.forms[0]["#employeeCode"].value.replace(/^\s+|\s+$/, '');
    var approvalResult = "true";
    if(document.forms[0]["#approvalResult.result"][1].checked){
        approvalResult = "false";
    }
    var approvalComment = document.forms[0]["#approvalResult.approvalComment"].value.replace(/^\s+|\s+$/, '');
    var isAllShop = "N";
    if(document.forms[0]["#isAllShop"].checked){
        isAllShop = "Y";
    }
    var beginDate = document.forms[0]["#beginDate"].value.replace(/^\s+|\s+$/, '');
    var endDate = document.forms[0]["#endDate"].value.replace(/^\s+|\s+$/, '');
    var isAllCustomer = "N";
    if(document.forms[0]["#form.isAllCustomer"][0].checked){
        isAllCustomer = "Y";
    }
    var customerType = document.forms[0]["#form.customerType"].value.replace(/^\s+|\s+$/, '');
    //alert("formId=" + formId);
    //alert("assignmentId=" + assignmentId);
    //alert("processId=" + processId);
    //alert("status=" + status);
    //alert("approvalResult=" + approvalResult);
    
    var vipTypeSize = document.forms[0]["#vipTypeSize"].value.replace(/^\s+|\s+$/, '');
    if(!isNaN(vipTypeSize)){
        var vipCheckFalg = "";
        var vipTypeCode = "";
        for(var i = 0; i < vipTypeSize; i++){
            if(document.forms[0]["#form.imPromotionCustomers[" + i + "].enable"][0].checked){
                vipCheckFalg += "Y";
            }else{
                vipCheckFalg += "N"
            }            
            vipTypeCode += document.forms[0]["#form.imPromotionCustomers[" + i + "].vipTypeCode"].value;
            if(i < (vipTypeSize - 1)){
                vipCheckFalg += "#";
                vipTypeCode += "#";
            }
        }
        //alert("vipTypeSize=" + vipCheckFalg);
        //alert("vipTypeCode=" + vipTypeCode);
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
          isAllShop : isAllShop,
          beginDate : beginDate,
          endDate : endDate,
          isAllCustomer : isAllCustomer,
          customerType : customerType,
          approvalComment : approvalComment,
          vipCheckFalg : vipCheckFalg,
          vipTypeCode : vipTypeCode
        };
        if("SUBMIT_BG" == actionId){
            vat.block.submit(function(){return "process_object_name=imPromotionAction"+
                "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
        }else{
            vat.block.submit(function(){return "process_object_name=imPromotionAction"+
                "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
        }  
    }else{
        alert("無法執行存檔，原因：取得客戶類別的數量失敗，請聯絡系統管理人員處理！");
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

function allShopCheckedMsg(){
    if(document.forms[0]["#isAllShop"].checked){
        alert("點選所有專櫃後，此促銷方案將適用所有專櫃，下方之專櫃設定將不適用!!");
    }
}

function formDataInitial(){
    if(document.forms[0]["#formId"].value != "[binding]"){
        vat.bean().vatBeanOther =
        {
          formId : document.forms[0]["#formId"].value
        };
        vat.bean.init(function(){
		return "process_object_name=imPromotionAction&process_object_method_name=performInitial"; 
        },{other: true});
    }
}

function getTotalAmt(){
    var processString = "process_object_name=imPromotionService&process_object_method_name=performCountTotalAmount" + 
	                "&headId=" + document.forms[0]["#form.headId"].value;
    vat.ajax.startRequest(processString, function () {
	    if (vat.ajax.handleState()) {
		    document.forms[0]["#form.totalCostAmount"].value = vat.ajax.getValue("TotalCostAmount", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.totalPriceAmount"].value = vat.ajax.getValue("TotalPriceAmount", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.totalDiscountAmount"].value = vat.ajax.getValue("TotalDiscountAmount", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.totalAfterDiscountAmount"].value = vat.ajax.getValue("TotalAfterDiscountAmount", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.averageDiscountRate"].value = vat.ajax.getValue("AverageDiscountRate", vat.ajax.xmlHttp.responseText);			
		}		
	});
}

function exportFormData(){
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IM_PROMOTION_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=imPromotionService" + 
              "&processObjectMethodName=findById" + 
              "&gridFieldName=imPromotionItems" + 
              "&arguments=" + document.forms[0]["#form.headId"].value + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '促銷單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function importFormData(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=IM_PROMOTION_ITEM" +
		"&importFileType=XLS" +
        "&processObjectName=imPromotionService" + 
        "&processObjectMethodName=executeImportPromotionItems" +
        "&arguments=" + document.forms[0]["#form.headId"].value +
        "&parameterTypes=LONG" +       
        "&eventObjectName=imPromotionItemService" + 
        "&eventObjectMethodName=saveItemData" +
        "&eventArguments=" + document.forms[0]["#form.headId"].value +
        "&eventParameterTypes=LONG" +      
        "&blockId=1",
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_PROMOTION" +
		"&levelType=ERROR" +
        "&processObjectName=imPromotionService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + document.forms[0]["#form.headId"].value +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imPromotionAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}