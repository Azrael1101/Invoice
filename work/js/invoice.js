var afterSavePageProcess = "";

/**頁面控制
  * pageControl();
  * initial 
*/

function outlineBlock(){
	paramDataInitial();
	headerInitial();
	invoiceMaster();
	//detailInitial()
}


function paramDataInitial(){
    if(document.forms[0]["#formId"].value != "[binding]"){       
        vat.bean().vatBeanOther = {
			brandCode : document.forms[0]["#form.brandCode"].value, 
			formId : document.forms[0]["#formId"].value
        };      
        vat.bean.init(function(){
		return "process_object_name=fiInvoiceMainAction&process_object_method_name=performInitial"; 
        },{other: true});
    }
}


function headerInitial(){
	vat.block.create(vnB_Header = 0, {
		id: "vatBlock_Head", generate: false,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"Invoice 建立作業", rows:[  

	 	{row_style:"", cols:[
	 		{items:[{name:"#L.supplierCode",     type:"LABEL",  value:"Supplier No."}]},
	 		{items:[{name:"#F.supplierCode",     type:"TEXT",   bind:"supplierCode",     size:10,                  ceap:"#form.supplierCode"}, 
	 				{name:"#F.supplierName"   , type:"TEXT"  ,  bind:"supplierName", size:12, mode:"READONLY",ceap:"#form.supplierName" }]},
	 			  //{name:"#F.supplierCodeName", type:"TEXT",   bind:"supplierCodeName", size:10, mode:"READONLY", ceap:"#form.supplierCodeName", back:false }]},
			{items:[{name:"#L.invoiceNo.",       type:"LABEL",  value:"Invoice No.<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.invoiceNo",        type:"TEXT",   bind:"invoiceNo",        size:12,                  ceap:"#form.invoiceNo"},
	 				{name:"#F.headId",           type:"TEXT",   bind:"headId",           size:10, mode:"HIDEEN",   ceap:"#form.headId", back:false }]},
	 		{items:[{name:"#L.invoiceDate",      type:"LABEL",  value:"Invoice Date<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.invoiceDate",      type:"DATE",   bind:"invoiceDate",      size:10,                  ceap:"#form.invoiceDate"}]},
			{items:[{name:"#L.brandCode",        type:"LABEL",  value:"品牌"}]},
	 		{items:[{name:"#F.brandCode",        type:"TEXT",   bind:"brandCode",        size:8,  mode:"HIDDEN",   ceap:"#form.brandCode"},
	 				{name:"#F.brandName",        type:"TEXT",   bind:"brandName",        size:12, mode:"READONLY", back:false}]},
			{items:[{name:"#L.status",           type:"LABEL",  value:"狀態"}]},	
	 		{items:[{name:"#F.status",           type:"TEXT",   bind:"status",           size:12, mode:"READONLY", ceap:"#form.status"}]}]},

	 	{row_style:"", cols:[
	 		{items:[{name:"#L.exchangeRate",       type:"LABEL",  value:"Exchange Rate"}]},
	 		{items:[{name:"#F.exchangeRate",       type:"TEXT",   bind:"exchangeRate",     size:10 ,                 ceap:"#form.exchangeRate"}]},
	 		{items:[{name:"#L.Supplier Order No.", type:"LABEL",  value:"L_Supplier Order No."}]},
			{items:[{name:"#F.supplierOrderNo" ,   type:"TEXT",   bind:"supplierOrderNoe", size:12,                  ceap:"#form.supplierOrderNo"}]},
	 		{items:[{name:"#L.Currency",           type:"LABEL",  value:"Currency>"}]},	 
	 		{items:[{name:"#F.currencyCode",       type:"SELECT", bind:"currencyCode",                               ceap:"#form.currencyCode"}]},	
	 		{items:[{name:"#L.inputFormEmployee",  type:"LABEL",  value:"填單人員"}]},
	 		{items:[{name:"#F.employeeName" ,      type:"TEXT",   bind:"employeeName",     size:12, mode:"READONLY"}]},	 
	 		{items:[{name:"#L.inputFormDate" ,     type:"LABEL",  value:"填單日期"}]},
 	 		{items:[{name:"#F.inputFormDate" ,     type:"DATE",   bind:"inputFormDate",    size:12, mode:"READONLY"}]}]},

 	 	{row_style:"", cols:[
 	 		{items:[{name:"#L.orderTypeCode",    type:"LABEL",  value:"單別<font color='red'>*</font>"}]},
 	 		{items:[{name:"#F.orderTypeCode",    type:"SELECT", bind:"orderTypeCode",    size:10,                   ceap:"#form.orderTypeCode"}]},
 	 		//{items:[{name:"#L.hideClosedPoLine", type:"LABEL",  value:"隱藏已結案採購明細"}]},
 	 		//{items:[{name:"#F.hideClosedPoLine", type:"RADIO",  bind:"hideClosedPoLine", size:10,                   ceap:"#form.hideClosedPoLine"}]},
 	 		{items:[{name:"#L.iremark" ,         type:"TEXT",   value:"<font color='red'>*</font> 為必填欄位，請務必填寫。" }], td:" colSpan=5"}]}],

	 	beginService:"",
	 	closeService:""			
	});
}


function invoiceMaster(){
	vat.block.create(vnB_master = 1, {
		id: "vatBlock_Master",generate: false, table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.Estimated Time of departure", type:"LABEL", value:"Estimated Time of departure"}]},	 
	 		{items:[{name:"#F.estimatedTimeDeparture",      type:"DATE",  bind:"estimatedTimeDeparture",   size:10, ceap:"#form.estimatedTimeDeparture"}]},		 
	 		{items:[{name:"#L.Estimated Time of arrival",   type:"LABEL", value:"Estimated Time of arrival"}]},	 
	 		{items:[{name:"#F.estimatedTimeArrival",        type:"DATE",  bind:"estimatedTimeArrival",     size:10, ceap:"#form.estimatedTimeArrival"}]},
	 		{items:[{name:"#L.Lot No.",                     type:"LABEL", value:"Lot No."}]},	 
			{items:[{name:"#F.lotNo",                       type:"TEXT",  bind:"lotNo",                    size:15, ceap:"#form.lotNo"}]}]},
		{row_style:"", cols:[
	 		{items:[{name:"#L.Foreign Amount",              type:"LABEL", value:"Foreign Amount"}]},	 
	 		{items:[{name:"#F.totalForeignInvoiceAmount",   type:"TEXT", bind:"totalForeignInvoiceAmount", size:10, ceap:"#form.totalForeignInvoiceAmount"}]},
	 		{items:[{name:"#L.Local Amount",                type:"LABEL", value:"Local Amount"}]},	 
	 		{items:[{name:"#F.totalLocalInvoiceAmount",     type:"TEXT",  bind:"totalLocalInvoiceAmount",  size:10, ceap:"#form.totalLocalInvoiceAmount"}]},
	 		{items:[{name:"#L.ReReceive No.",               type:"LABEL", value:"Receive No."}]},	 
	 		{items:[{name:"#F.receiveOrderNo",              type:"TEXT",  bind:"receiveOrderNo",           size:20, ceap:"#form.receiveOrderNo"}]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.Declaration Type",            type:"LABEL", value:"Declaration Type<font color='red'>*</font>"}]},	 
	 		{items:[{name:"#F.customsDeclarationType",      type:"TEXT",  bind:"customsDeclarationType",   size:10, ceap:"#form.customsDeclarationType"}]},
	 		{items:[{name:"#L.Declaration NO",              type:"LABEL", value:"Declaration NO"}]},	 
	 		{items:[{name:"#F.customsDeclarationNo",        type:"TEXT",  bind:"customsDeclarationNo",     size:10, ceap:"#form.customsDeclarationNo"}]},
	 		{items:[{name:"#L.Customer Sequence",           type:"LABEL", value:"Custom Sequence"}]},	 
	 		{items:[{name:"#F.customsSeq",              	type:"TEXT",  bind:"customsSeq",               size:20, ceap:"#form.customsSeq"}]}]}
 	 	],
		beginService:"",
		closeService:""			
	});
}


function detailInitial() {
	var allOrderTypes = vat.bean("allOrderTypes");
	//alert("orderTypeSelect:" + allOrderTypes);
	var statusTmp  = document.forms[0]["#form.status"].value;

	if( statusTmp == "SAVE" || statusTmp == "REJECT" ){
		varCanDataDelete = true;
		varCanDataAppend = true;
		varCanDataModify = true;		
	} else {
		var varCanDataDelete = false;
		var varCanDataAppend = false;
		var varCanDataModify = false;
	}
	
	var branchCode = "1";
if(document.forms[0]["#branchCode"].value != "[binding]"){
	var branchCode = document.forms[0]["#branchCode"].value;

	var vnB_Detail = 2;
	vat.item.make(vnB_Detail, "indexNo",               {type:"IDX",    desc:"序號"});
	if ( branchCode == "2" ){
		vat.item.make(vnB_Detail, "customSeq",   		   {type:"TEXT",   size:10,                desc:"指定順序"});
	}else{
		vat.item.make(vnB_Detail, "customSeq",   		   {type:"TEXT",   size:10, mode:"HIDDEN", desc:"指定順序"});
	}
}
 	vat.item.make(vnB_Detail, "sourceOrderTypeCode",   {type:"SELECT", size:16, maxLen:20, desc:"採購單單別", init:allOrderTypes });
	vat.item.make(vnB_Detail, "poPurchaseOrderHeadId", {type:"TEXT",   size:10,            desc:"採購單PK",  mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "sourceOrderNo",         {type:"TEXT",   size:16,            desc:"採購單單號", onchange:"onChangeData()"});
	vat.item.make(vnB_Detail, "poPurchaseOrderLineId", {type:"TEXT",   size:10,            desc:"採購明細PK", mode:"HIDDEN"});
	if ( branchCode == "2"){
		vat.item.make(vnB_Detail, "itemCode",              {type:"TEXT",   size:16, maxLen:20, desc:"品號",      onchange:"onChangeData()"}); 
		vat.item.make(vnB_Detail, "itemCName",             {type:"TEXT",   size:12, maxLen:20, desc:"品名",      mode:"READONLY"}); 
		vat.item.make(vnB_Detail, "quantity",              {type:"TEXT",   size:12, maxLen:12, desc:"數量"});
	}else{
		vat.item.make(vnB_Detail, "itemCode",              {type:"TEXT",   size:16, maxLen:20, desc:"品號",      mode:"HIDDEN"}); 
		vat.item.make(vnB_Detail, "itemCName",             {type:"TEXT",   size:12, maxLen:20, desc:"品名",      mode:"HIDDEN"}); 
		vat.item.make(vnB_Detail, "quantity",              {type:"TEXT",   size:12, maxLen:12, desc:"數量",      mode:"HIDDEN"});
	}
	vat.item.make(vnB_Detail, "lineId",                {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord",          {type:"CHECK",  desc:"鎖定", mode:"HIDDEN"});                          
	vat.item.make(vnB_Detail, "isDeleteRecord",        {type:"DEL",    desc:"刪除"});                                                          
	vat.item.make(vnB_Detail, "message",               {type:"MSG",    desc:"訊息"});
	vat.block.pageLayout(vnB_Detail, {
														id: "vnB_Detail",
														pageSize: 10,
														gridOverflow: "scroll",
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


/* LINE ITEM CODE CHANGE */
function onChangeData() {
	//alert( "onChangeData" ) ;
	var nItemLine      = vat.item.getGridLine();
	var sItemCode      = vat.item.getGridValueByName("itemCode",nItemLine);
	var nPoHeadId      = vat.item.getGridValueByName("poPurchaseOrderHeadId",nItemLine);
	var nPoLineId      = vat.item.getGridValueByName("poPurchaseOrderLineId",nItemLine);
	var sSourceOrderNo = vat.item.getGridValueByName("sourceOrderNo",nItemLine);
	var sOrderTypeCode = vat.item.getGridValueByName("sourceOrderTypeCode",nItemLine);
	var nQuantity      = vat.item.getGridValueByName("quantity",nItemLine);
	
	vat.item.setGridValueByName( "itemCName", nItemLine);

	var processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=getAJAXLineData" + 
							"&brandCode=" + document.forms[0]["#form.brandCode"].value + 
							"&itemCode="  + sItemCode + 
							"&poHeadId="  + nPoHeadId + "&poLineId="  + nPoLineId +
							"&sourceOrderNo="       + sSourceOrderNo +
							"&sourceOrderTypeCode=" + sOrderTypeCode ;
								   
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
				if( vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText) != '查無資料'){
					vat.item.setGridValueByName("itemCName", nItemLine, vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText));
				//}else{
				//	alert('查無資料，請重新輸入');
				//	vat.item.setGridValueByName("itemCode", nItemLine, '');
				}
				if( vat.ajax.getValue("POHeadId", vat.ajax.xmlHttp.responseText)!='0'){
					vat.item.setGridValueByName("poPurchaseOrderHeadId", nItemLine, vat.ajax.getValue("PoHeadId", vat.ajax.xmlHttp.responseText));
				}
				if( vat.ajax.getValue("PoLineId", vat.ajax.xmlHttp.responseText)!='0'){
					vat.item.setGridValueByName("poPurchaseOrderLineId", nItemLine, vat.ajax.getValue("PoLineId", vat.ajax.xmlHttp.responseText));
				}
				if( vat.ajax.getValue("quantity", vat.ajax.xmlHttp.responseText)!='0' && ( nQuantity=='0' || nQuantity=='')){
					vat.item.setGridValueByName("quantity", nItemLine, vat.ajax.getValue("Quantity", vat.ajax.xmlHttp.responseText));
				}
			}
		}
	});
}


/* 設定供應商名稱資料 */
function onChangeSupplierData() {
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplierCode" +
						"&supplierCode=" + document.forms[0]["#form.supplierCode"].value + 
						"&organizationCode=TM&brandCode=" + document.forms[0]["#form.brandCode"].value + 
						"&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			//alert( vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText) );
			document.forms[0]["#form.supplierName"].value      = vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.exchangeRate"].value      = vat.ajax.getValue("ExchangeRate",    vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.currencyCode"].value      = vat.ajax.getValue("CurrencyCode",    vat.ajax.xmlHttp.responseText);
			//document.forms[0]["#form.countryCode"].value     = vat.ajax.getValue("CountryCode",     vat.ajax.xmlHttp.responseText);
    		//document.forms[0]["#form.paymentTermCode"].value = vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText);
		}
	});
}


/* LINE OrderNo CHANGE */
function onChangeIOrderNo() {
	//alert( "onChangeItemCode" ) ;
}


/* 判斷是否要關閉LINE */
function checkEnableLine() {
	return true ;
}


/* 載入 LINE 資料 */
function loadBeforeAjxService() {
	//alert("execute loadBeforeAjxService");
	var processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=getAJAXPageData" +
						"&headId="    + document.forms[0]["#form.headId"].value +
						"&brandCode=" + document.forms[0]["#form.brandCode"].value;
	return processString;
}


/* 載入LINE資料 SUCCESS */
function loadSuccessAfter() {
	//alert("execute loadSuccessAfter ");
}


/* 取得SAVE要執行的JS FUNCTION */
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
	var processString = "";
	var exchangeRate = 0;
	if (checkEnableLine()) {
		exchangeRate = document.forms[0]["#form.exchangeRate"].value;
		processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=updateAJAXPageLinesData" + 
						"&headId="    + document.forms[0]["#form.headId"].value + 
						"&brandCode=" + document.forms[0]["#form.brandCode"].value + 
						"&status="    + document.forms[0]["#form.status"].value ;
	}
	return processString;
}


/* 取得存檔成功後要執行的JS FUNCTION */
function saveSuccessAfter() {
	//alert("saveSuccessAfter ->" + afterSavePageProcess );
	//vat.block.pageSearch(1);
	//vat.formD.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg == "") {
		if ("saveHandler" == afterSavePageProcess) {	
			execSubmitAction("SAVE");	//executeCommandHandler("main", "saveHandler");
			
		} else if ("submitHandler" == afterSavePageProcess) {			 
			execSubmitAction("SUBMIT");	//executeCommandHandler("main", "submitHandler");

		} else if ("submitBgHandler" == afterSavePageProcess) {
	    	execSubmitAction("SUBMIT_BG");
	    	
		} else if ("voidHandler" == afterSavePageProcess) {
			execSubmitAction("VOID");

		}
	}
	else {
		alert("\u932f\u8aa4\u8a0a\u606f " + errorMsg);
	}
	afterSavePageProcess = "" ;	
}


/* 暫存 SAVE HEAD && LINE */
function doSaveHandler() {
	//alert("doSaveHandler");
	if (confirm("確認是否暫存送出?")) {
		if (checkEnableLine()) {
			//save line
			afterSavePageProcess = "saveHandler";
			vat.block.pageSearch(2);
		}
	}
}


/* 送出SUBMIT HEAD && LINE */
function doSubmitHandler() {
	//alert("doSubmitHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			afterSavePageProcess = "submitHandler";
			vat.block.pageSearch(2);
		}
	}
}


/* 背景送出SUBMIT HEAD && LINE */
function doSubmitBgHandler() {
	if (confirm("是否確認背景送出?")) {		
		afterSavePageProcess = "submitBgHandler";	
		vat.block.pageSearch(2);
	}
}


/* 作廢VOID HEAD && LINE */
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		afterSavePageProcess = "voidHandler";	
		vat.block.pageSearch(2);			
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


/* PICKER 之前要先RUN LINE SAVE */
function doBeforePicker(){
	vat.formD.pageDataSave(0);
}


function execSubmitAction(actionId){
	//alert("execSubmitAction");
    var formId          = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var status          = document.forms[0]["#form.status"].value.replace(/^\s+|\s+$/, '');
    var orderTypeCode   = document.forms[0]["#form.orderTypeCode"].value;
    var employeeCode    = document.forms[0]["#employeeCode"].value.replace(/^\s+|\s+$/, '');
	var assignmentId    = document.forms[0]["#assignmentId"].value.replace(/^\s+|\s+$/, '');
	var processId       = document.forms[0]["#processId"].value;
	var approvalComment = document.forms[0]["#approvalResult.approvalComment"].value;
	var approvalResult  = "true";
	if(document.forms[0]["#approvalResult.result"][1].checked){
        approvalResult = "false";
    }
	
	var formStatus     = status;
	//alert(" formId " + formId );
	
	if("SAVE" == actionId){
		formStatus = "SAVE";
	}else if("SUBMIT" == actionId){
		//formStatus = changeFormStatus(formId, processId, status, approvalResult);
		formStatus = "SIGNING"; 
		}else if("SUBMIT_BG" == actionId){
			formStatus = "SIGNING";
		}else if("VOID" == actionId){
			formStatus = "VOID";
	}

	vat.bean().vatBeanOther={
			beforeChangeStatus: status,
	        formStatus: formStatus,
	        employeeCode: employeeCode,
	        processId: processId,
	        approvalResult: approvalResult,
	        approvalComment: approvalComment,
	        assignmentId: assignmentId
        	};

	if ("SUBMIT_BG" == actionId){
		vat.block.submit(function(){return "process_object_name=fiInvoiceMainAction"+
                "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
	}else{
		vat.block.submit(function(){return "process_object_name=fiInvoiceMainAction"+
            	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
	}
	
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


function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=FI_INVOICE_HEAD" +
		"&levelType=ERROR" +
        "&processObjectName=fiInvoiceHeadMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + document.forms[0]["#form.headId"].value +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


/* 選擇 PO DATA call Picker 前先存 Line Data  */
function doSaveLineHandler() {
	//alert("doSaveLineHandler");
	//afterSavePageProcess = "none";
	vat.formD.pageDataSave(0);
	vat.block.pageSearch(2);			
}


function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=fiInvoiceMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}