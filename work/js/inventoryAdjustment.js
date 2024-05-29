var afterSavePageProcess = "";

//頁面控制
//pageControl();
/*
	initial 
*/
function outlineBlock(){
 headerInitial();

 formDataInitial(); 
  
  
  
  //kwePoMaster();
  //kwePoAmount();
}
function headerInitial(){
//var allOrderTypes = vat.bean("allOrderTypes");
//var allPriceTypes = vat.bean("allPriceTypes");
//var allCurrencys = vat.bean("allCurrencys");
vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: false,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"調整單維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L_orderType", type:"LABEL" , value:"單別<font color='red'>*</font>"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", mode:"READONLY",ceap:"#form.orderTypeCode"}]},		 
	 {items:[{name:"#L_orderNo"  , type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.orderNo"  , type:"TEXT"  ,  bind:"orderNo", size:12, mode:"READONLY",ceap:"#form.orderNo"},
	 		 {name:"#F.headId"   , type:"TEXT"  ,  bind:"headId", back:false, size:10, mode:"READONLY",ceap:"#form.headId" }]},
	 {items:[{name:"#L_adjustmentDate", type:"LABEL",  value:"調整日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.adjustmentDate", type:"DATE",  bind:"adjustmentDate", size:1 ,ceap:"#form.adjustmentDate"}]},		 
	 {items:[{name:"#L_brandCode", type:"LABEL" , value:"品牌"}]},
	 {items:[{name:"#F.brandCode", type:"TEXT"  , size:8, mode:"READONLY"}]}, 
	 {items:[{name:"#L_status"   , type:"LABEL" , value:"狀態"}]},	 		 
	 {items:[{name:"#F.status"   , type:"TEXT"  , size:12, mode:"READONLY"}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L_reason", type:"LABEL", value:"原因"}]},
	 {items:[{name:"#F.reason" , type:"TEXT", size:12 ,td:" colSpan=5"}]},	
	 {items:[{name:"#L_inputFormEmployee", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.employeeName" , type:"TEXT",  mode:"READONLY", size:12}]},	 
	 {items:[{name:"#L_inputFormDate" , type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.inputFormDate" , type:"TEXT", mode:"READONLY", size:12}]},
 	 {row_style:"", cols:[
	 {items:[{name:"#L_sourceOrderTypeCode", type:"LABEL" , value:"[拆/併]貨單別"}]},	 
	 {items:[{name:"#F.sourceOrderTypeCode", type:"SELECT", mode:"READONLY"}]},		 
	 {items:[{name:"#L_desc"  , type:"LABEL" , value:"備註:"}]},
	 {items:[{name:"#L_desc1", type:"LABEL",  
	 		value:"1. 拆貨 :請將要 <font color='red'>拆貨商品</font> 的品號填寫在<font color='red'>拆貨後新商品</font>的<font color='blue'>關聯品號</font>欄位上<br>"+
				  "2. 併貨 :請將<font color='red'>併貨後新商品</font>的品號填寫在<font color='red'>併貨商品</font>的<font color='blue'>關聯品號</font>欄位上",
		td:"colSpan=7"}]}]}
 	 ]}],	 
	 beginService:"",
	 closeService:""			
	});
}

function detailInitial() {
	var wareHouseCode = vat.bean("allArrivalWarehouses");
	
	//var statusTmp = document.forms[0]["#form.status"].value;
	var varCanDataDelete = true;
	var varCanDataAppend = true;
	var varCanDataModify = true;
	/*
	if( statusTmp == "SAVE" || statusTmp == "REJECT" ){
		varCanDataDelete = true;
		varCanDataAppend = true;
		varCanDataModify = true;		
	}*/
	var vnB_Detail = 1;
	vat.item.make(vnB_Detail, "indexNo", {type:"IDX", desc:"序號"});                                                            
	vat.item.make(vnB_Detail, "itemCode", {type:"TEXT", size:16, maxLen:20, desc:"品號", onchange:"onChangeItemCode()"});      
	vat.item.make(vnB_Detail, "warehouseCode", {type:"SELECT", desc:"庫別", init:wareHouseCode});                    
	vat.item.make(vnB_Detail, "lotNo", {type:"TEXT", size:12, maxLen:20, desc:"批號"});                 
	vat.item.make(vnB_Detail, "difQuantity", {type:"NUMB", size:12, maxLen:20, desc:"差異數量"});  
	vat.item.make(vnB_Detail, "localUnitCost", {type:"NUMB", size:8, maxLen:20, desc:"單項金額"});                        
	vat.item.make(vnB_Detail, "amount", {type:"NUMB", size:8, maxLen:20, desc:"合計金額", mode:"READONLY", onchange:"calculateLineAmount()"});    
	vat.item.make(vnB_Detail, "sourceItemCode", {type:"TEXT", size:12, maxLen:20, desc:"關聯品號"}); 	                  
	vat.item.make(vnB_Detail, "accountCode", {type:"TEXT", size:12, maxLen:20, desc:"帳務代號"});                 
	vat.item.make(vnB_Detail, "reason", {type:"TEXT", size:16, maxLen:40, desc:"原因"});           
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});                                                                     
	vat.item.make(vnB_Detail, "status", {type:"RADIO", desc:"狀態", mode:"HIDDEN"});                                         
	vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});                                         
	vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});                                                          
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});                                                          
	vat.block.pageLayout(vnB_Detail, {
														pageSize: 10,											
								                        canGridDelete : varCanDataDelete,
														canGridAppend : varCanDataAppend,
														canGridModify : varCanDataModify,
														beginService: "",
														closeService: "",
							    						itemMouseinService  : "",
														itemMouseoutService : "",						
														appendBeforeService : "",
														appendAfterService  : "",
														deleteBeforeService : "",
														deleteAfterService  : "",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "",
														loadFailureAfter    : "",						
														eventService        : "",   
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														saveFailureAfter    : ""
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}
function formDataInitial(){
	 if(document.forms[0]["#formId"].value != "[binding]"){
		vat.bean().vatBeanOther={
			loginBrandCode : document.forms[0]["#loginBrandCode"    ].value,
			formId : document.forms[0]["#formId"].value
		};
		vat.bean.init(function(){
			return "process_object_name=imAdjustmentHeadAction&process_object_method_name=performInitial"; 
	        },{other: true});
	 }
}
/*
	LINE ITEM CODE CHANGE
*/
function onChangeItemCode() {
	//alert("execute onChangeItemCode s");
	var nItemLine = vat.item.getGridLine();
	//alert( "test ->" + nItemLine ) ;
	//alert("be nItemLine=" + nItemLine );
	var sItemCode = vat.item.getGridValueByName("itemCode",nItemLine);
	//alert("af nItemLine=" + nItemLine+ ",sItemCode=" + sItemCode);
	//alert("testPO.item:"+ sItemCode + "/current line:"+nItemLine+"/current:"+vat.form.item.current);
	vat.item.setGridValueByName( "unitPrice",nItemLine);
	vat.item.setGridValueByName( "foreignUnitCost", nItemLine);
	vat.item.setGridValueByName( "lastForeignUnitCost", nItemLine);
	vat.item.setGridValueByName( "itemCName", nItemLine);
	vat.item.setGridValueByName( "onHandQty", nItemLine);
	vat.item.setGridValueByName( "quantity", nItemLine);
	vat.item.setGridValueByName( "foreignPurchaseAmount", nItemLine);
	if(vat.item.getGridValueByName("quantity",nItemLine)=="undefined"){
		vat.item.setGridValueByName("quantity",nItemLine,0);
	}
	if(vat.item.getGridValueByName("foreignPurchaseAmount", nItemLine)=="undefined"){
		vat.item.setGridValueByName("foreignPurchaseAmount", nItemLine,0);
	}
	if(vat.item.getGridValueByName("unitPriceAmount", nItemLine)=="undefined"){
		vat.item.setGridValueByName("unitPriceAmount", nItemLine,0);
	}
	//vat.item.setGridValueByName( "unitPriceAmount", nItemLine);
	//vat.item.setGridValueByName( "actualPurchaseQuantity", nItemLine);
	
	if (sItemCode != "") {
		var processString = "process_object_name=imAdjustmentHeadService&process_object_method_name=getAJAXLineData&brandCode=" + document.forms[0]["#form.brandCode"].value + "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value + "&itemCode=" + sItemCode;
		//alert(processString);
		//alert("execute onChangeItemCode " + sItemCode);										   
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
						vat.item.setGridValueByName("amount", nItemLine, vat.ajax.getValue("Amount", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("localUnitCost", nItemLine, vat.ajax.getValue("LocalUnitCost", vat.ajax.xmlHttp.responseText));
						//alert(vat.ajax.getValue("LocalUnitCost", vat.ajax.xmlHttp.responseText));			
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
	判斷是否要關閉LINE
*/
function checkEnableLine() {

	return true ;
}
/*
	載入LINE資料
*/
function loadBeforeAjxService() {
	//alert("execute loadBeforeAjxService s");
	var processString = "process_object_name=imAdjustmentHeadService&process_object_method_name=getAJAXPageData&headId=" + document.forms[0]["#form.headId"].value;
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
		//exchangeRate = document.forms[0]["#form.exchangeRate"].value;
		processString = "process_object_name=imAdjustmentHeadService&process_object_method_name=updateAJAXPageLinesData" + "&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value + "&brandCode=" + document.forms[0]["#form.brandCode"].value  + "&exchangeRate=" + exchangeRate + "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value ;
	}
	return processString;
}
/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
	//alert("saveSuccessAfter ->" + afterSavePageProcess );
	//vat.block.pageSearch(1);//vat.formD.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg == "") {
		if ("saveHandler" == afterSavePageProcess) {	
			execSubmitAction("SAVE");//executeCommandHandler("main", "saveHandler");
		} else if ("submitHandler" == afterSavePageProcess) {			 
			execSubmitAction("SUBMIT");//executeCommandHandler("main", "submitHandler");
		} else if ("submitBgHandler" == afterSavePageProcess) {
	    	execSubmitAction("SUBMIT_BG");
		}else if ("voidHandler" == afterSavePageProcess) {
			execSubmitAction("VOID");
		} else if ("executeExport" == afterSavePageProcess) {
			exportFormData();
			//vat.block.pageSearch(1);
			//executeCommandHandlerNoBlock("main","exportDataHandler");
		}else if ("executeImport" == afterSavePageProcess) {
			importFormData();
		}
	}
	else {
		alert("\u932f\u8aa4\u8a0a\u606f " + errorMsg);
	}
	afterSavePageProcess = "" ;	
}
/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
	//alert("doSaveHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			
			afterSavePageProcess = "saveHandler";
			vat.block.pageSearch(1);
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
			afterSavePageProcess = "submitHandler";
			vat.block.pageSearch(1);
		}
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
	var budgetYear = $('select[@id*=_budgetYear]').find('option:selected').text();
	$("div#budgetYearText").text("總預算 年度:" + budgetYear);
	if (checkEnableLine()) {
		afterSavePageProcess = "totalCount";
		vat.block.pageSearch(2);
	}
}

/*
	匯出
*/
/*
function doExport() {
	//alert("doExport");
	if (checkEnableLine()) {
		//save line
		afterSavePageProcess = "afterExport";
		vat.block.pageSearch(2);
	}
}
*/
/*
	變更匯率
*/
function changeCurrenceCode(){
	var processString = "process_object_name=poPurchaseOrderHeadService&process_object_method_name=getAJAXExchangeRateByCurrencyCode&currencyCode=" + document.forms[0]["#form.currencyCode"].value + "&organizationCode=TM" ;
	vat.ajax.startRequest(processString,  function() { 
	  if (vat.ajax.handleState()){
	    document.forms[0]["#form.exchangeRate"].value =  vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText);
		setCurrencyCodeName();
		showTotalCountPage();
	  }
	} );
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
			document.forms[0]["#form.exchangeRate"].value = vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText);
    		//document.forms[0]["#form.tradeTeam"].value =  vat.ajax.getValue("TradeTeam", vat.ajax.xmlHttp.responseText);
    		document.forms[0]["#form.paymentTermCode"].value =  vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText);
			showTotalCountPage();
		}
	});
}

function setCurrencyCodeName(){
		var currencyCodeNameObj = document.getElementById("currencyCodeName"); 
		//20090122 shan 加上幣別名稱
		var vxObjByName = document.getElementsByName("#form.currencyCode");
		var vsResult = "";
		if (vxObjByName && vxObjByName[0]){
			vxObjSelect = vxObjByName[0];
			if (vat.formD.itemIsSelect(vxObjSelect)){
				for (var i=(vxObjSelect.options.length - 1); i >= 0; i--) {
					if (vxObjSelect.options[i].selected){
						vsResult = vxObjSelect.options[i].value;
						break;
					}
				}
			}else{
				vat.debug("developer", "從 "+vat.callerName(vat.formD.itemSelectGetValue.caller)+"(), 更新的目標:"+psName+", 不是一個<SELECT>元素");
			}
		}else{
			vat.debug("developer", "從 "+vat.callerName(vat.formD.itemSelectGetValue.caller)+"(), 找不到被更新的 HTML 元素名稱"+psName);			
		}
		currencyCodeNameObj.innerHTML  = vsResult ;	
}


/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
	vat.formD.pageDataSave(0);
}

function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == ""){
        formStatus = "SIGNING";
    }else if(processId != null && processId != ""){
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
function execSubmitAction(actionId){
    
    var formId = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var assignmentId = document.forms[0]["#assignmentId"].value.replace(/^\s+|\s+$/, '');
    var orderTypeCode = document.forms[0]["#form.orderTypeCode"].value;
    var processId = document.forms[0]["#processId"].value;
    var status = document.forms[0]["#form.status"].value.replace(/^\s+|\s+$/, '');
	var employeeCode = document.forms[0]['#user.employeeCode'].value;
	//alert(employeeCode);
    var invoiceTypeCode = ""; 
    var taxType = "";
    var taxRate = "";
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
        //alert(formStatus);
     vat.bean().vatBeanOther={
     	beforeChangeStatus : status,
		employeeCode : employeeCode,
		formStatus : formStatus ,
		assignmentId : assignmentId,
        processId : processId,
        approvalResult : approvalResult,
        approvalComment : approvalComment
	};
	if("SUBMIT_BG" == actionId){
            vat.block.submit(function(){return "process_object_name=imAdjustmentHeadAction"+
                "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
     }else{
        vat.block.submit(function(){return "process_object_name=imAdjustmentHeadAction"+
            "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});  
  	}
}
/*
	匯出
*/
function doExport() {
	//save line
	afterSavePageProcess = "executeExport";
	vat.block.pageSearch(1);	
}

/*
	匯入
*/
function doImport() {
	//save line
	afterSavePageProcess = "executeImport";
	vat.block.pageSearch(1);	
}
function exportFormData(){
    var headId = document.forms[0]["#form.headId"].value;
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IM_ADJUSTMENT_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=imAdjustmentHeadService" + 
              "&processObjectMethodName=findById" + 
              "&gridFieldName=imAdjustmentLines" + 
              "&arguments=" + headId + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '商品調整單明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function importFormData(){
	var width = "600";
    var height = "400";
    var headId = document.forms[0]["#form.headId"].value;
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=IM_ADJUSTMENT_ITEM" +
		"&importFileType=XLS" +
        "&processObjectName=imAdjustmentHeadService" + 
        "&processObjectMethodName=executeImportImAdjustmentLists" +
        "&arguments=" + headId +
        "&parameterTypes=LONG" +
        "&blockId=1",
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_ADJUSTMENT_HEAD" +
		"&levelType=ERROR" +
        "&processObjectName=imAdjustmentHeadService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + document.forms[0]["#form.headId"].value +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imAdjustmentHeadAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}