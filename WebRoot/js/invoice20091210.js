vat.debug.disable();
var afterSavePageProcess = "";

var vnB_Button     = 0;
var vatBlock_Head  = 1;
var vatMasterDiv   = 2; 
var vatDetailDiv   = 3;

function outlineBlock(){
	formDataInitial();
	buttonLine(); 
	headerInitial();
	var status   = vat.bean("status");
 	if(typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
	    vat.tabm.createTab(0, "vatTabSpan", "L", "float");                                                                                                                                   
		vat.tabm.createButton(0, "xTab1", "主檔資料", "vatMasterDiv",   "images/tab_master_data_dark.gif",   "images/tab_master_data_light.gif", "", "showTotalCountPage()"); 
		vat.tabm.createButton(0, "xTab2", "採購資料", "vatDetailDiv",   "images/tab_po_detail_dark.gif",     "images/tab_po_detail_light.gif");
		vat.tabm.createButton(0, "xTab3", "簽核資料", "vatApprovalDiv", "images/tab_approval_data_dark.gif", "images/tab_approval_data_light.gif", (document.forms[0]["#processId"].value!="" ? "inline" : "none" ));
	}
	masterInitial();
	detailInitial();
	kweWfBlock( vat.item.getValueByName("#F.brandCode"), 
            	vat.item.getValueByName("#F.orderTypeCode"), 
             	vat.item.getValueByName("#F.invoiceNo"),
             	document.forms[0]["#loginEmployeeCode"].value );
    doFormAccessControl();
}


/* initial */
function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#formId"].value != '[binding]'){
        vat.bean().vatBeanOther = 
  	    {brandCode     : document.forms[0]["#loginBrandCode"].value,   	
  	     employeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode : document.forms[0]["#orderTypeCode"].value,
	     formId        : document.forms[0]["#formId"].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	    };     
        vat.bean.init(function(){
		return "process_object_name=fiInvoiceMainAction&process_object_method_name=performInitial"; 
        },{other: true});
    }
}


/* initial after Query */
function refreshForm(vsHeadId){
	document.forms[0]["#formId"].value    = vsHeadId;	
	vat.bean().vatBeanOther.brandCode     = document.forms[0]["#loginBrandCode"].value;	
  	vat.bean().vatBeanOther.employeeCode  = document.forms[0]["#loginEmployeeCode"].value;	  
  	vat.bean().vatBeanOther.orderTypeCode = document.forms[0]["#orderTypeCode"].value;    
	vat.bean().vatBeanOther.formId        = document.forms[0]["#formId"].value;
	
	vat.block.submit(
		function(){
			return "process_object_name=fiInvoiceMainAction&process_object_method_name=performInitial";  
     	},{other: true, 
     	   funcSuccess:function(){
     	       vat.item.bindAll();
     	       doFormAccessControl();
     		   
     		   if(vat.bean().vatBeanPicker.result == null  && vat.item.getValueByName("#F.status")=="SAVE" ){
     		   		vat.tabm.displayToggle(0, "xTab3", false, false, false);
     		   }else{
					vat.tabm.displayToggle(0, "xTab3", true, false, false);
     		   		refreshWfParameter( vat.item.getValueByName("#F.brandCode"), 
     		   							vat.item.getValueByName("#F.orderTypeCode"),
     		   							vat.item.getValueByName("#F.invoiceNo" ) );
					vat.block.pageDataLoad(102, vnCurrentPage = 1); 
     		  }
     	  }}
    );
}


/* function button */
function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    //alert(document.forms[0]["#orderTypeCode"].value);
	    
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new",           type:"IMG",     value:"新增",  src:"./images/button_create.gif",         eClick:"createNewForm()"},
	 			//{name:"SPACE",          type:"LABEL",   value:"　"},
	 			{name:"#B.search" ,       type:"PICKER",  value:"查詢",  src:"./images/button_find.gif", 
	 									  openMode:"open", 
	 									  service:"Fi_Invoice:search:20091210.page", left:0, right:0, width:1024, height:768,	
										  servicePassData:function()
										  	{return "&orderTypeCode="+document.forms[0]["#orderTypeCode"].value+"&brandCode="+vat.item.getValueByName("#F.brandCode")},
	 									  serviceAfterPick:function(){doAfterPickerProcess()}},
	 			//{name:"SPACE",          type:"LABEL", value:"　"},
	 	 		{name:"#B.exit",          type:"IMG",   value:"離開",    src:"./images/button_exit.gif",           eClick:'closeWindows("CONFIRM")'},
	 	 		//{name:"SPACE",          type:"LABEL", value:"　"},
	 			{name:"#B.submit",        type:"IMG",   value:"送出",    src:"./images/button_submit.gif",         eClick:'doSubmit("SUBMIT")'},
	 			//{name:"SPACE",          type:"LABEL", value:"　"},
	 			{name:"#B.save",          type:"IMG",   value:"暫存",    src:"./images/button_save.gif",           eClick:'doSubmit("SAVE")'},
	 			//{name:"SPACE",          type:"LABEL", value:"　"},
	 			{name:"#B.void",          type:"IMG",   value:"作廢",    src:"./images/button_void.gif",           eClick:'doSubmit("VOID")'},
	 			//{name:"SPACE",          type:"LABEL", value:"　"}, 	
	 			{name:"#B.submitBg",      type:"IMG",   value:"背景送出", src:"./images/button_submit_background.gif",  eClick:'doSubmit("SUBMIT_BG")'},
	 			//{name:"SPACE",          type:"LABEL", value:"　"},		
	 			{name:"#B.message",       type:"IMG",   value:"訊息提示", src:"./images/button_message_prompt.gif",  eClick:'showMessage()'},
	 			//{name:"SPACE",          type:"LABEL", value:"　"},
	 			{name:"#B.export",        type:"IMG",   value:"明細匯出",  src:"./images/button_detail_export.gif",  eClick:'doSubmit("EXPORT")'},
	 		  	//{name:"SPACE",          type:"LABEL", value:"　"},
	 			//{name:"#B.import",        type:"IMG",   value:"明細匯入",  src:"./images/button_detail_import.gif", eClick:'doSubmit("IMPORT")'},
	 			{name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  , 
	 									 openMode:"open", 
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,	
	 									 serviceAfterPick:function(){afterImportSuccess()}},
	 			{name:"#B.importPoLine",  type:"PICKER", value:"取採購明細資料", src:"./images/button_obtain_po.gif",
	 									  openMode:"open", 
	 									  service:"Fi_Invoice:search:20091210_PoL.page", left:0, right:0, width:1024, height:768,	
										  servicePassData:function()
										  	{return "&orderTypeCode="+document.forms[0]["#orderTypeCode"].value+
										  			"&supplierCode=" +vat.item.getValueByName("#F.supplierCode")+
										  		    "&brandCode="    +vat.item.getValueByName("#F.brandCode")+
										  		    "&headId="       +vat.item.getValueByName("#F.headId")+
										  		    "&currencyCode=" +vat.item.getValueByName("#F.currencyCode")+
										  		    "&exchangeRate=" +vat.item.getValueByName("#F.exchangeRate")+
										  			"&formName=FiInvoice"},
										  serviceBeforePick:function(){doPickerWithPoPurchase()},
	 									  serviceAfterPick:function(){doPickerWithPoPurchase()}},
	 			{name:"#B.importPoHead",  type:"PICKER", value:"取採購單資料", src:"./images/button_obtain_po.gif",
	 									  openMode:"open", 
	 									  service:"Fi_Invoice:search:20091210_PoH.page", left:0, right:0, width:1024, height:768,	
										  servicePassData:function()
										  	{return "&orderTypeCode="+document.forms[0]["#orderTypeCode"].value+
										  			"&supplierCode=" +vat.item.getValueByName("#F.supplierCode")+
										  			"&brandCode="    +vat.item.getValueByName("#F.brandCode")+
										  			"&headId="       +vat.item.getValueByName("#F.headId")+
										  			"&formName=FiInvoice"},
	 									  serviceBeforePick:function(){doPickerWithPoPurchase()},
	 									  serviceAfterPick:function(){doPickerWithPoPurchase()}},
	 			{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.first",         type:"IMG",   value:"第一筆",   src:"./images/play-first.png",   eClick:"gotoFirst()"},
	 			{name:"#B.forward",       type:"IMG",   value:"上一筆",   src:"./images/play-back.png",    eClick:"gotoForward()"},
	 			{name:"#B.next",          type:"IMG",   value:"下一筆",   src:"./images/play.png",         eClick:"gotoNext()"},
	 			{name:"#B.last",          type:"IMG",   value:"最後一筆",  src:"./images/play-forward.png", eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB",  bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE",            type:"LABEL", value:" / "},
	 			{name:"#L.maxRecord",     type:"NUMB",  bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}


function headerInitial(){
	var allOrderTypes   = vat.bean("allOrderTypes");
	var allCurrencyCode = vat.bean("allCurrencyCode");
	var isConfirm       = [[true, true], ["已確認", "未確認"], ["Y", "N"]];
	
	var orderTypeCode   = document.forms[0]["#orderTypeCode"].value;
	var modeConfirm = "";
	if(orderTypeCode=="FII" || orderTypeCode=="IPP"){	// || orderTypeCode=="IPF" 
		modeConfirm = "HIDDEN";
	}

	vat.block.create(vatBlock_Head, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"Invoice 建立作業", rows:[  

	 	{row_style:"", cols:[
	 		{items:[{name:"#L.supplierCode",     type:"LABEL",  value:"Supplier No."}]},
	 		{items:[{name:"#F.supplierCode",     type:"TEXT",   bind:"supplierCode", size:12, onchange:"onChangeSupplierCode()", mask:"CCCCCCCCCCCCCCCCCCCC" },
	 				{name:"#B.supplierCode",	 type:"PICKER", value:"PICKER", src:"./images/start_node_16.gif",
	 									 		 openMode:"open", 
	 									 		 service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 		 left:0, right:0, width:1024, height:768,	
	 									 		 serviceAfterPick:function(){doAfterPickerSupplier();}},
	 				{name:"#L.supplierName",     type:"LABEL",  value:"<br>"},
	 				{name:"#F.supplierName",     type:"TEXT",   bind:"supplierName",    size:30, mode:"READONLY", back:false }]},
			{items:[{name:"#L.invoiceNo.",       type:"LABEL",  value:"Invoice No.<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.invoiceNo",        type:"TEXT",   bind:"invoiceNo",        size:20, back:false },
	 				{name:"#F.headId",           type:"TEXT",   bind:"headId",           size:10, mode:"READONLY",   back:false }]},
	 		{items:[{name:"#L.invoiceDate",      type:"LABEL",  value:"Invoice Date<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.invoiceDate",      type:"DATE",   bind:"invoiceDate",      size:10 }]},
			{items:[{name:"#L.brandCode",        type:"LABEL",  value:"品牌"}]},
	 		{items:[{name:"#F.brandCode",        type:"TEXT",   bind:"brandCode",        size:8,  mode:"HIDDEN" },
	 				{name:"#F.brandName",        type:"TEXT",   bind:"brandName",        size:12, mode:"READONLY", back:false}]},
			{items:[{name:"#L.status",           type:"LABEL",  value:"狀態"}]},	
	 		{items:[{name:"#F.status",           type:"TEXT",   bind:"status",           size:12, mode:"HIDDEN" },
	 				{name:"#F.statusName",       type:"TEXT",   bind:"statusName",       size:12, mode:"READONLY", back:false}]}]},

	 	{row_style:"", cols:[
	 		{items:[{name:"#L.Supplier Order No.", type:"LABEL",  value:"Supplier Order No."}]},
			{items:[{name:"#F.supplierOrderNo" ,   type:"TEXT",   bind:"supplierOrderNoe",  size:30 }]},
	 		{items:[{name:"#L.Currency",           type:"LABEL",  value:"Currency"}]},	 
	 		{items:[{name:"#F.currencyCode",       type:"SELECT", bind:"currencyCode",  init:allCurrencyCode, onchange:"onChangeSetExchangeRate()" }]},
	 		{items:[{name:"#L.exchangeRate",       type:"LABEL",  value:"Exchange Rate"}]},
	 		{items:[{name:"#F.exchangeRate",       type:"TEXT",   bind:"exchangeRate",      size:10 }]},
	 		{items:[{name:"#L.CreatedBy",          type:"LABEL",  value:"填單人員"}]},
	 		{items:[{name:"#F.createdBy",          type:"TEXT",   bind:"createdBy",         size:12, mode:"HIDDEN"},
	         		{name:"#F.createdByName",      type:"TEXT",   bind:"createdByName",     size:12, mode:"READONLY" }]},	
	 		{items:[{name:"#L.CreationDate",       type:"LABEL",  value:"填單日期"}]},
	 		{items:[{name:"#F.creationDate",       type:"TEXT",   bind:"creationDate",      size:12, mode:"READONLY" }]}]},

 	 	{row_style:"", cols:[
 	 		{items:[{name:"#L.orderTypeCode",  type:"LABEL",  value:"單別<font color='red'>*</font>"}]},
 	 		{items:[{name:"#F.orderTypeCode",  type:"SELECT", bind:"orderTypeCode",         size:10, mode:"READONLY",  init:allOrderTypes}]},
 	 		{items:[{name:"#L.financeConfirm", type:"LABEL",  value:"財務確認" }]},
 	 		{items:[{name:"#F.financeConfirm", type:"SELECT", bind:"financeConfirm",        size:1,  mode:modeConfirm, init:isConfirm },
 	 				{name:"#F.blank2",         type:"LABEL",  value:"　",                   size:1,  mode:"HIDDEN" }]},
 	 		{items:[{name:"#L.iremark",        type:"LABEL",  value:"<font color='red'>*</font> 為必填欄位，請務必填寫。" }], td:" colSpan=6"}]}],

	 	beginService:"",
	 	closeService:""			
	});
}


function masterInitial(){
	var allDeclarationType = vat.bean("allDeclarationType");
	
	var branchCode    = vat.bean("branchCode");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var styleT2   = " style='display:none'";
	var styleDecl = " style='display:none'";
	if( branchCode=="2" && (orderTypeCode=="IPF" || orderTypeCode=="IPP")){
		styleT2 = "";
	}else if(orderTypeCode!="IPF" && orderTypeCode!="IPP" && orderTypeCode!="FII"){
		styleDecl ="";
	}
	var labelLocalAmount = "Local Amount";
	var modeT2 = "";
	if( orderTypeCode=="IPF" ){
		labelLocalAmount = "Item Count";
		modeT2 = "READONLY";
	}

	vat.block.create(vatMasterDiv, {
		id: "vatMasterDiv",generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'",		
		rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.Estimated Time of departure", type:"LABEL",  value:"Estimated Time of departure"}]},	 
	 		{items:[{name:"#F.estimatedTimeDeparture",      type:"DATE",   bind:"estimatedTimeDeparture",    size:10 }]},		 
	 		{items:[{name:"#L.Estimated Time of arrival",   type:"LABEL",  value:"Estimated Time of arrival"}]},	 
	 		{items:[{name:"#F.estimatedTimeArrival",        type:"DATE",   bind:"estimatedTimeArrival",      size:10 }]},
	 		{items:[{name:"#L.Lot No.",                     type:"LABEL",  value:"Lot No."}]},	 
			{items:[{name:"#F.lotNo",                       type:"TEXT",   bind:"lotNo",                     size:15 }]}]},
		{row_style:"", cols:[
	 		{items:[{name:"#L.Foreign Amount",              type:"LABEL",  value:"Foreign Amount"}]},	 
	 		{items:[{name:"#F.totalForeignInvoiceAmount",   type:"NUMM",   bind:"totalForeignInvoiceAmount", size:16, mode:modeT2, dec:4 }]},
	 		{items:[{name:"#L.Local Amount",                type:"LABEL",  value:labelLocalAmount}]},	 
	 		{items:[{name:"#F.totalLocalInvoiceAmount",     type:"NUMM",   bind:"totalLocalInvoiceAmount",   size:16, mode:modeT2 }]},
	 		{items:[{name:"#L.Receive No.",                 type:"LABEL",  value:"Receive No."}]},	 
	 		{items:[{name:"#F.receiveOrderNo",              type:"TEXT",   bind:"receiveOrderNo",            size:20 }]}]},
	 	{row_style:styleT2, cols:[
	 		{items:[{name:"#L.Declaration Type",            type:"LABEL",  value:"Declaration Type<font color='red'>*</font>"}]},	 
	 		{items:[{name:"#F.customsDeclarationType",      type:"SELECT", bind:"customsDeclarationType",    size:10, init:allDeclarationType }]},
	 		{items:[{name:"#L.Declaration NO",              type:"LABEL",  value:"Declaration NO<font color='red'>*</font>"}]},	 
	 		{items:[{name:"#F.customsDeclarationNo",        type:"TEXT",   bind:"customsDeclarationNo",      size:16 }]},
	 		{items:[{name:"#L.Customer Sequence",           type:"LABEL",  value:"Custom Sequence"}]},	 
	 		{items:[{name:"#F.customsSeq",              	type:"NUMM",   bind:"customsSeq",                size:20 }]}]},
	 	{row_style:styleDecl, cols:[
	 		{items:[{name:"#L.remark1", type:"LABEL", value:"Remark1"}]},	 
	 		{items:[{name:"#F.remark1", type:"TEXT",  bind:"remark1",   size:160 }], td:" colSpan=5"}]},
	 	{row_style:styleDecl, cols:[
	 		{items:[{name:"#L.remark2", type:"LABEL", value:"Remark2"}]},	 
	 		{items:[{name:"#F.remark2", type:"TEXT",  bind:"remark2",   size:160 }], td:" colSpan=5"}]},
	 	{row_style:styleDecl, cols:[
	 		{items:[{name:"#L.remark3", type:"LABEL", value:"Remark3"}]},	 
	 		{items:[{name:"#F.remark3", type:"TEXT",  bind:"remark3",   size:160 }], td:" colSpan=5"}]},
	 	{row_style:styleDecl, cols:[
	 		{items:[{name:"#L.remark4", type:"LABEL", value:"Remark4"}]},	 
	 		{items:[{name:"#F.remark4", type:"TEXT",  bind:"remark4",   size:160 }], td:" colSpan=5"}]},
	 	{row_style:styleDecl, cols:[
	 		{items:[{name:"#L.remark5", type:"LABEL", value:"Remark5"}]},	 
	 		{items:[{name:"#F.remark5", type:"TEXT",  bind:"remark5",   size:160 }], td:" colSpan=5"}]},
	 	{row_style:styleDecl, cols:[
	 		{items:[{name:"#L.remark6", type:"LABEL", value:"Remark6"}]},	 
	 		{items:[{name:"#F.remark6", type:"TEXT",  bind:"remark6",   size:160 }], td:" colSpan=5"}]}
 	 	],
		beginService:"",
		closeService:""			
	});
}


function detailInitial() {
	var allPoOrderTypes = vat.bean("allPoOrderTypes");
	var branchCode      = vat.bean("branchCode");
	var statusTmp       = vat.item.getValueByName("#F.status");
	var orderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");

	//if( statusTmp == "SAVE" || statusTmp == "REJECT" ){
		varCanDataDelete = true;
		varCanDataAppend = true;
		varCanDataModify = true;		
	//} else {
		//var varCanDataDelete = false;
		//var varCanDataAppend = false;
		//var varCanDataModify = false;
	//}
	if(orderTypeCode!="IPF" && orderTypeCode!="IPP" && orderTypeCode!="FII")
		var varCanDataAppend = false;
	
	vat.item.make(vatDetailDiv, "indexNo",   {type:"IDX", view:"fixed", size:4, desc:"序號"});
	/*
	vat.item.make(vatDetailDiv, "customSeq", {type:"NUMB", view:"fixed", size:10, desc:"指定順序", 
												//mode:(function(){return(orderTypeCode=="IPF"?"":"HIDDEEN");}())
												mode:function(){
											     	var a = "";
											     	if(orderTypeCode!="IPF"){a = "HIDDEEN";}
											     	return a;
												}() 
											 });
	*/
	if ( branchCode == "2" && orderTypeCode=="IPF" ){
		vat.item.make(vatDetailDiv, "customSeq", {type:"NUMB", view:"fixed", size:3, desc:"指定順序"});
	}else{
		vat.item.make(vatDetailDiv, "customSeq", {type:"NUMB", view:"fixed", size:3, desc:"指定順序", mode:"HIDDEN"});
	}
	if(orderTypeCode=="IPF" || orderTypeCode=="IPP" || orderTypeCode=="FII"){
 		vat.item.make(vatDetailDiv, "sourceOrderTypeCode",    {type:"SELECT", view:"fixed", size:10, desc:"採購單單別",    init:allPoOrderTypes });
		vat.item.make(vatDetailDiv, "poPurchaseOrderHeadId",  {type:"TEXT",   view:"",      size:10, desc:"採購單PK",      mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "sourceOrderNo",          {type:"TEXT",   view:"",      size:12, desc:"採購單單號",     onchange:"onChangeData()"});
		vat.item.make(vatDetailDiv, "poPurchaseOrderLineId",  {type:"TEXT",   view:"",      size:10, desc:"採購明細PK",     mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "shippingMark",           {type:"TEXT",   view:"",      size:20, desc:"Shipping-Mark", mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "originalDeclarationNo",  {type:"TEXT",   view:"",      size:16, desc:"原進口報單號碼",   mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "originalDeclarationSeq", {type:"NUMM",   view:"",      size:2,  desc:"項次",           mode:"HIDDEN"});
	}else{
		vat.item.make(vatDetailDiv, "sourceOrderTypeCode",    {type:"SELECT", view:"",      size:10, desc:"採購單單別", mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "poPurchaseOrderHeadId",  {type:"TEXT",   view:"",      size:10, desc:"採購單PK",  mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "sourceOrderNo",          {type:"TEXT",   view:"",      size:12, desc:"採購單單號", mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "poPurchaseOrderLineId",  {type:"TEXT",   view:"",      size:10, desc:"採購明細PK", mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "shippingMark",           {type:"TEXT",   view:"",      size:20, desc:"Shipping-Mark" });
		vat.item.make(vatDetailDiv, "originalDeclarationNo",  {type:"TEXT",   view:"",      size:16, desc:"原進口報單號碼", mode:"READONLY"});
		vat.item.make(vatDetailDiv, "originalDeclarationSeq", {type:"NUMM",   view:"",      size:2,  desc:"項次", mode:"READONLY"});
	}
	
	if (orderTypeCode=="IPF"){
		vat.item.make(vatDetailDiv, "itemCode",          {type:"TEXT", view:"",      size:14, maxLen:20, desc:"品號",        onchange:"onChangeData()"}); 
		vat.item.make(vatDetailDiv, "supplierItemCode",  {type:"TEXT", view:"",      size:14, maxLen:20, desc:"廠商貨號",     mode:"READONLY"});
		vat.item.make(vatDetailDiv, "itemCName",         {type:"TEXT", view:"",      size:30, maxLen:20, desc:"品名",        mode:"READONLY"}); 
		vat.item.make(vatDetailDiv, "purchaseUnit",      {type:"TEXT", view:"",      size:5,  maxLen:20, desc:"單位",        mode:"READONLY"});
		vat.item.make(vatDetailDiv, "category15",            {type:"TEXT", view:"", size:10, maxLen:40, desc:"功能",mode:"READONLY"});
		vat.item.make(vatDetailDiv, "specWeight",            {type:"TEXT", view:"", size:10, maxLen:40, desc:"重量",mode:"READONLY"}); 
		vat.item.make(vatDetailDiv, "quantity",          {type:"NUMM", view:"",      size:8,  maxLen:12, desc:"數量",        onchange:"calculateLineAmount()" });
		vat.item.make(vatDetailDiv, "foreignUnitPrice",  {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"外幣單價",	dec:6, onchange:"calculateLineAmount()" });
		vat.item.make(vatDetailDiv, "foreignAmount",     {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"外幣小計",     mode:"READONLY", dec:2 });
		vat.item.make(vatDetailDiv, "localUnitPrice",    {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"本幣單價",     mode:"HIDDEN",   dec:2 });
		vat.item.make(vatDetailDiv, "localAmount",       {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"Amount",      mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "weight",            {type:"NUMM", view:"shift", size:10, maxLen:12, desc:"GrossWeight", mode:"HIDDEN"});	
		vat.item.make(vatDetailDiv, "remark",            {type:"TEXT", view:"shift", size:30, maxLen:30, desc:"備註" });			
		
	}else if (orderTypeCode=="IPP" || orderTypeCode=="FII"){
		vat.item.make(vatDetailDiv, "itemCode",          {type:"TEXT", view:"",      size:14, maxLen:20, desc:"品號",         mode:"HIDDEN"}); 
		vat.item.make(vatDetailDiv, "supplierItemCode",  {type:"TEXT", view:"",      size:14, maxLen:20, desc:"廠商貨號",      mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "itemCName",         {type:"TEXT", view:"",      size:30, maxLen:20, desc:"品名",         mode:"HIDDEN"}); 
		vat.item.make(vatDetailDiv, "purchaseUnit",      {type:"TEXT", view:"",      size:5,  maxLen:20, desc:"單位",         mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "category15",            {type:"TEXT", view:"", size:10, maxLen:40, desc:"功能",mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "specWeight",            {type:"TEXT", view:"", size:10, maxLen:40, desc:"重量",mode:"HIDDEN"}); 
		vat.item.make(vatDetailDiv, "quantity",          {type:"NUMM", view:"",      size:8,  maxLen:12, desc:"數量",         mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "foreignUnitPrice",  {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"外幣單價",      mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "foreignAmount",     {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"外幣小計",      mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "localUnitPrice",    {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"本幣單價",      mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "localAmount",       {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"Amount",      mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "weight",            {type:"NUMM", view:"shift", size:10, maxLen:12, desc:"GrossWeight", mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "remark",            {type:"TEXT", view:"shift", size:12, maxLen:12, desc:"備註",         mode:"HIDDEN"});			
		
	}else{
		vat.item.make(vatDetailDiv, "itemCode",          {type:"TEXT", view:"",      size:14, maxLen:20, desc:"品號",         mode:"READONLY", onchange:"onChangeData()"}); 
		vat.item.make(vatDetailDiv, "supplierItemCode",  {type:"TEXT", view:"",      size:14, maxLen:20, desc:"廠商貨號",      mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "itemCName",         {type:"TEXT", view:"",      size:30, maxLen:20, desc:"品名",         mode:"READONLY"}); 
		vat.item.make(vatDetailDiv, "purchaseUnit",      {type:"TEXT", view:"",      size:5,  maxLen:20, desc:"單位",         mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "category15",            {type:"TEXT", view:"", size:10, maxLen:40, desc:"功能",mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "specWeight",            {type:"TEXT", view:"", size:10, maxLen:40, desc:"重量",mode:"HIDDEN"}); 
		vat.item.make(vatDetailDiv, "quantity",          {type:"NUMM", view:"",      size:8,  maxLen:12, desc:"數量",         mode:"READONLY"});
		vat.item.make(vatDetailDiv, "foreignUnitPrice",  {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"外幣單價",      mode:"", dec:6, onchange:"calculateLineAmount()"});
		vat.item.make(vatDetailDiv, "foreignAmount",     {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"外幣小計",      mode:"READONLY", dec:2});
		vat.item.make(vatDetailDiv, "localUnitPrice",    {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"本幣單價",      mode:"", dec:4, onchange:"calculateLineAmount()"});
		vat.item.make(vatDetailDiv, "localAmount",       {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"本幣小計",      mode:"READONLY", dec:2});
		vat.item.make(vatDetailDiv, "weight",            {type:"NUMM", view:"shift",      size:10, maxLen:12, desc:"GrossWeight", mode:"READONLY", dec:2});
		vat.item.make(vatDetailDiv, "remark",            {type:"TEXT", view:"shift", size:12, maxLen:12, desc:"備註",         mode:"HIDDEN"});			
	}
	vat.item.make(vatDetailDiv, "lineId",                {type:"ROWID"});
	vat.item.make(vatDetailDiv, "isLockRecord",          {type:"CHECK", view:"fixed", desc:"鎖定", mode:"HIDDEN"});                          
	vat.item.make(vatDetailDiv, "isDeleteRecord",        {type:"DEL",   desc:"刪除"});                                                          
	vat.item.make(vatDetailDiv, "message",               {type:"MSG",   desc:"訊息"});
	vat.block.pageLayout(vatDetailDiv, {
		id: "vatDetailDiv",
		pageSize: 10,
		canGridDelete : varCanDataDelete,
		canGridAppend : varCanDataAppend,
		canGridModify : varCanDataModify,
		appendBeforeService : "appendBeforeService()",
		loadBeforeAjxService: "loadBeforeAjxService()",
		saveBeforeAjxService: "saveBeforeAjxService()",
		saveSuccessAfter    : "saveSuccessAfter()"
		});
	vat.block.pageDataLoad(vatDetailDiv, vnCurrentPage = 1);
}


function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.bean().vatBeanPicker.result = null;
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
		vat.bean().vatBeanOther.firstRecordNumber = 0;
    	refreshForm("");
	 }
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
							"&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
							"&itemCode="  + sItemCode + 
							"&poHeadId="  + nPoHeadId + "&poLineId="  + nPoLineId +
							"&sourceOrderNo="       + sSourceOrderNo +
							"&sourceOrderTypeCode=" + sOrderTypeCode ;
								   
	vat.ajax.startRequest(processString, function () {
		//alert(vat.ajax.getValue("category15", vat.ajax.xmlHttp.responseText));
		if (vat.ajax.handleState()) {
			if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
				if( vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText) != '查無資料'){
					vat.item.setGridValueByName("itemCName", nItemLine, vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText));
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
				vat.item.setGridValueByName("supplierItemCode", nItemLine, vat.ajax.getValue("SupplierItemCode", vat.ajax.xmlHttp.responseText));
				vat.item.setGridValueByName("purchaseUnit",     nItemLine, vat.ajax.getValue("PurchaseUnit",     vat.ajax.xmlHttp.responseText));
				vat.item.setGridValueByName("foreignUnitPrice", nItemLine, vat.ajax.getValue("PurchaseAmount",   vat.ajax.xmlHttp.responseText));
				vat.item.setGridValueByName("category15", nItemLine, vat.ajax.getValue("category15", vat.ajax.xmlHttp.responseText));
				vat.item.setGridValueByName("specWeight", nItemLine, vat.ajax.getValue("specWeight", vat.ajax.xmlHttp.responseText));
			}
		}
	});
}



/* 計算 單筆 LINE 合計的部份 */
function calculateLineAmount() {
	//alert("execute calculateLineAmount s");
	var nItemLine        = vat.item.getGridLine();
	var quantity         = vat.item.getGridValueByName("quantity",         nItemLine);
	var foreignUnitPrice = vat.item.getGridValueByName("foreignUnitPrice", nItemLine);
	var foreignAmount    = parseFloat(quantity) * parseFloat(foreignUnitPrice);
	vat.item.setGridValueByName("foreignAmount", nItemLine, foreignAmount);
	var localUnitPrice = vat.item.getGridValueByName("localUnitPrice", nItemLine);
	var localAmount    = parseFloat(quantity) * parseFloat(localUnitPrice);
	vat.item.setGridValueByName("localAmount", nItemLine, localAmount);
}



/* 設定供應商名稱資料 */
function onChangeSupplierCode() {
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplier" +
						"&supplierCode="  + vat.item.getValueByName("#F.supplierCode") + 
						"&organizationCode=TM"+
						"&brandCode="     + vat.item.getValueByName("#F.brandCode")  + 
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") ;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.exchangeRate",    vat.ajax.getValue("ExchangeRate",    vat.ajax.xmlHttp.responseText));
		}
	});
}


/* 變更幣別時須異動 匯率時 */
function onChangeSetExchangeRate() {
	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXExchangeRateByCurrencyCode"+
							"&currencyCode=" + vat.item.getValueByName("#F.currencyCode") + 
							"&organizationCode=TM"+
							"&orderDate="    + vat.item.getValueByName("#F.invoiceDate") ;
	vat.ajax.startRequest(processString,  function() { 
		if (vat.ajax.handleState()){
			//alert(vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.exchangeRate", vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText));
		} });
}


/* 判斷是否要關閉LINE */
function checkEnableLine() {
	return true ;
}


/* 載入 LINE 資料 */
function loadBeforeAjxService() {
	//alert("loadBeforeAjxService");
	var processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=getAJAXPageData" +
						"&headId="    + vat.item.getValueByName("#F.headId") +
						"&brandCode=" + vat.item.getValueByName("#F.brandCode") +
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") ;
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
		exchangeRate = vat.item.getValueByName("#F.exchangeRate");
		processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=updateAJAXPageLinesData" + 
						"&headId="    + vat.item.getValueByName("#F.headId") + 
						"&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
						"&status="    + vat.item.getValueByName("#F.status") ;
	}
	return processString;
}


/* 取得存檔成功後要執行的JS FUNCTION */
function saveSuccessAfter() {
	//alert("saveSuccessAfter ->" + afterSavePageProcess );
	//if (errorMsg == "") {
		if ("SAVE" == afterSavePageProcess) {	
			execSubmitAction("SAVE");
			
		} else if ("SUBMIT" == afterSavePageProcess) {			 
			execSubmitAction("SUBMIT");

		} else if ("SUBMIT_BG" == afterSavePageProcess) {
	    	execSubmitAction("SUBMIT_BG");
	    	
		} else if ("VOID" == afterSavePageProcess) {
			execSubmitAction("VOID");

		} else if ("EXPORT" == afterSavePageProcess) {
			exportFormData();

		}else if ("IMPORT" == afterSavePageProcess) {
			importFormData();
			
		} else if ("totalCount" == afterSavePageProcess){
			var processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=getAJAXHeadTotalAmount" + 
								"&headId=" + vat.item.getValueByName("#F.headId") +
								"&exchangeRate=" + vat.item.getValueByName("#F.exchangeRate");
			vat.ajax.startRequest(processString, function (){
				if (vat.ajax.handleState()) {
					vat.item.setValueByName("#F.totalForeignInvoiceAmount", vat.ajax.getValue("totalForeignInvoiceAmount", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalLocalInvoiceAmount",   vat.ajax.getValue("totalLocalInvoiceAmount",   vat.ajax.xmlHttp.responseText));
				}
			});
		}
	afterSavePageProcess = "" ;	
}


/* Button Hit Function */
function doSubmit(formAction){
	//alert("doSubmit " + formAction);
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("SUBMIT_BG" == formAction){
	 	alertMessage = "是否確定背景送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("EXPORT" == formAction){
	 	alertMessage = "是否確定匯出作業?";
	}else if ("IMPORT" == formAction){
	 	alertMessage = "是否確定匯入作業?";
	}else if ("EXTEND" == formAction){
	 	alertMessage = "是否執行取報單程序?";
	}
		
	if(confirm(alertMessage)){
	    if("SAVE" == formAction){
	    	afterSavePageProcess = "SAVE";
            
        }else if("SUBMIT" == formAction){
        	afterSavePageProcess = "SUBMIT";
            
        }else if("SUBMIT_BG" == formAction){
        	afterSavePageProcess = "SUBMIT_BG";
            
        }else if("VOID" == formAction){
        	afterSavePageProcess = "VOID";
        	
        }else if("EXPORT" == formAction){
        	afterSavePageProcess = "EXPORT";
        	
        }else if("IMPORT" == formAction){
        	afterSavePageProcess = "IMPORT";
        	
        }else if ("EXTEND" == formAction){
        	afterSavePageProcess = "EXTEND"; 
        
        }
        vat.block.pageSearch(3);	// save & refresh FiInvoiceLine Page / after save success call saveSuccessAfter()
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
	var formId    = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var processId = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
	var employeeCode    = vat.bean("employeeCode").replace(/^\s+|\s+$/, '');
    var status          = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
    var orderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");
    var assignmentId    = vat.item.getValueByName("#assignmentId");
 	var approvalComment = vat.item.getValueByName("#F.approvalComment");
	var approvalResult  = vat.item.getValueByName("#F.approvalResult");
    if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
	
	var formStatus     = status;
	if("SAVE" == actionId){
		formStatus = "SAVE";
	}else if("SUBMIT" == actionId){
		formStatus = changeFormStatus(formId, processId, status, approvalResult);
		//formStatus = "SIGNING"; 
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
    }else if(status = "FINISH"){
    	formStatus = "FINISH";
    }else{
    	formStatus = "SIGNING";
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
        "&arguments=" + vat.item.getValueByName("#F.headId")+
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


/* 選擇 PO DATA call Picker 前先存 Line Data  */
function doSaveLineHandler() {
	//alert("doSaveLineHandler");
	vat.formD.pageDataSave(0);
	vat.block.pageSearch(3);			
}


function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=fiInvoiceMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}


function doAfterPickerProcess(){
    if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    //alert(vat.bean().vatBeanPicker.result.length);
	    if( vsMaxSize == 0){
	  	    vat.bean().vatBeanOther.firstRecordNumber   = 0;
		    vat.bean().vatBeanOther.lastRecordNumber    = 0;
		    vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		    vat.bean().vatBeanOther.firstRecordNumber   = 1;
		    vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		 
		    refreshForm(vsHeadId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}


/* 供應商picker 回來執行 */
function doAfterPickerSupplier(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplier"+
							"&addressBookId="  + vat.bean().vatBeanPicker.result[0].addressBookId +
							"&organizationCode=TM"+
							"&brandCode="      + vat.item.getValueByName("#F.brandCode") + 
							"&orderDate="      + vat.item.getValueByName("#F.invoiceDate") ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.supplierCode",    vat.ajax.getValue("SupplierCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.exchangeRate",    vat.ajax.getValue("ExchangeRate",    vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}


function doPickerWithPoPurchase(){
	//alert("Before and After Picker PO");
	vat.block.pageSearch(3);
}


function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
      window.top.close();
  }  	
}

function exportFormData(){
    var headId = vat.item.getValueByName("#F.headId");
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=INVOICE_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=fiInvoiceHeadMainService" + 
              "&processObjectMethodName=findByIdForExport" + 
              "&gridFieldName=fiInvoiceLines" + 
              "&arguments=" + headId + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, 'Invoice明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function importFormData(){
	/*var width  = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=INVOICE_ITEM" +
		"&importFileType=XLS" +
        "&processObjectName=fiInvoiceHeadMainService" + 
        "&processObjectMethodName=updateImportLines" +
        "&arguments=" + headId +
        "&parameterTypes=LONG" +
        "&blockId=3",
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + 
		',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
	vat.block.pageRefresh(3);	// refresh page data after import data
	*/
	var headId = vat.item.getValueByName("#F.headId");
		var suffix = 
		"&importBeanName=INVOICE_ITEM" +
		"&importFileType=XLS" +
       	"&processObjectName=fiInvoiceHeadMainService" + 
       	"&processObjectMethodName=updateImportLines" +
       	"&arguments=" + headId +
       	"&parameterTypes=LONG" +
        "&blockId=3";
	    return suffix;
}

function afterImportSuccess(){
	//alert("FindTheFirstItemCategory");
	totalCount();
}

function totalCount(){
	var processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=getAJAXHeadTotalAmount" + 
						"&headId=" + vat.item.getValueByName("#F.headId") +
						"&exchangeRate=" + vat.item.getValueByName("#F.exchangeRate");
	vat.ajax.startRequest(processString, function (){
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.totalForeignInvoiceAmount", vat.ajax.getValue("totalForeignInvoiceAmount", vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.totalLocalInvoiceAmount",   vat.ajax.getValue("totalLocalInvoiceAmount",   vat.ajax.xmlHttp.responseText));
		}
	});
}


/* 顯示合計DATA */
function showTotalCountPage() {
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var branchCode    = vat.bean("branchCode");
	if( orderTypeCode=="IPF" ){
		afterSavePageProcess = "totalCount";
		vat.block.pageSearch(3);
	}
}


function doFormAccessControl(){
	var processId      	= document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
	var orderTypeCode  	= vat.item.getValueByName("#F.orderTypeCode");
    var formStatus     	= vat.item.getValueByName("#F.status");
    var financeConfirm	= vat.item.getValueByName("#F.financeConfirm");
    var branchCode     	= vat.bean("branchCode");
	var typeCode      	= vat.bean("typeCode");
	var canBeMod		= document.forms[0]["#canBeMod"].value;
	vat.item.setStyleByName("#B.new",    "display", "inline");
	vat.item.setStyleByName("#B.search", "display", "inline");
	vat.item.setStyleByName("#B.save", "display", "inline");
	vat.item.setStyleByName("#B.submit", "display", "inline");
	vat.item.setStyleByName("#B.submitBG", "display", "inline");
	vat.item.setStyleByName("#B.import", "display", "inline");
	vat.item.setStyleByName("#B.void",   "display", "none");
	vat.item.setStyleByName("#B.importPoHead", "display", "none");
	vat.item.setStyleByName("#B.importPoLine", "display", "none");
	vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true , true);
	vat.item.setAttributeByName("vatMasterDiv", "readOnly", false, true , true);
	if(processId==""){
		if(formStatus=="SAVE" && vat.bean().vatBeanPicker.result == null){
			if( branchCode=="2" && orderTypeCode=="IPF" )
				vat.item.setStyleByName("#B.importPoLine", "display", "inline");
			if( orderTypeCode=="IPP" || orderTypeCode=="FII" )
				vat.item.setStyleByName("#B.importPoHead", "display", "inline");
			vat.block.canGridModify( [3], true, true , true );
		}else if(canBeMod == "Y" && (formStatus=="FINISH" || formStatus=="SAVE")){
			if( branchCode=="2" && orderTypeCode=="IPF" )
				vat.item.setStyleByName("#B.importPoLine", "display", "inline");
			if( orderTypeCode=="IPP" || orderTypeCode=="FII" )
				vat.item.setStyleByName("#B.importPoHead", "display", "inline");
			vat.block.canGridModify( [3], true, true , true );
			vat.item.setStyleByName("#B.save", "display", "none");
			vat.item.setStyleByName("#B.submitBG", "display", "none");
		}else{
			vat.item.setStyleByName("#B.save", "display", "none");
			vat.item.setStyleByName("#B.submit", "display", "none");
			vat.item.setStyleByName("#B.submitBG", "display", "none");
			vat.item.setStyleByName("#B.import", "display", "none");
			vat.item.setAttributeByName("vatBlock_Head","readOnly", true, true , true);
			vat.item.setAttributeByName("vatMasterDiv",	"readOnly", true, true , true);
			vat.block.canGridModify( [3], false, false , false );
		}
	}else{
		if(formStatus=="SAVE" || formStatus=="REJECT"){
			if( branchCode=="2" && orderTypeCode=="IPF" )
				vat.item.setStyleByName("#B.importPoLine", "display", "inline");
			if( orderTypeCode=="IPP" || orderTypeCode=="FII" )
				vat.item.setStyleByName("#B.importPoHead", "display", "inline");
			vat.item.setStyleByName("#B.new",    "display", "none");
			vat.item.setStyleByName("#B.search", "display", "none");
			vat.item.setStyleByName("#B.void",   "display", "inline");
			vat.block.canGridModify( [3], true, true , true );
    		vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
		}else{
			vat.item.setStyleByName("#B.new", 			"display", "none");
			vat.item.setStyleByName("#B.search",		"display", "none");
			vat.item.setStyleByName("#B.import",		"display", "none");
			vat.item.setStyleByName("#B.save",			"display", "none");
			vat.item.setStyleByName("#B.submitBG",		"display", "none");
			vat.item.setStyleByName("#B.void",			"display", "none");  
			vat.item.setStyleByName("#B.importPoLine",	"display", "none");
			vat.item.setStyleByName("#B.importPoHead", 	"display", "none");
			vat.item.setAttributeByName("vatBlock_Head","readOnly", true, true , true);
			vat.item.setAttributeByName("vatMasterDiv",	"readOnly", true, true , true);
			vat.item.setAttributeByName("#F.financeConfirm", "readOnly", false);
			vat.block.canGridModify( [3], false, false , false );
		}
	}
	totalCount();
	vat.block.pageDataLoad( vatDetailDiv,  vnCurrentPage = 1);
}

// 新增空白頁
function appendBeforeService(){
	return true;
}  
