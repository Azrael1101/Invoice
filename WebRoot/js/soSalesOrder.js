/*** 
 *	檔案：
 *	說明：
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;
var vnB_Payment = 4;
var vnB_POS = 5;
var vnB_Total = 6;
var vEnable = "Y";

//for 儲位用
var vatStorageDetail = 202;
var enableStorage = false;

function outlineBlock(){
	initialVatBeanOther();
	formInitial();
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0, "xTab1", "銷貨單主檔"	,"vatMasterDiv","images/tab_master_data_dark.gif","images/tab_master_data_light.gif", false, "" );
		vat.tabm.createButton(0, "xTab2", "銷貨單明細檔"	,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false, "" );
		vat.tabm.createButton(0, "xTab3", "付款資料"   	,"vatPaymentDiv","images/tab_payment_data_dark.gif","images/tab_payment_data_light.gif", false, "" );
		vat.tabm.createButton(0, "xTab4", "金額統計"		,"vatTotalDiv","images/tab_total_amount_dark.gif","images/tab_total_amount_light.gif", false , "showTotalCountPage()" );
      	vat.tabm.createButton(0, "xTab5", "POS資料"		,"vatPosDiv","images/tab_pos_data_dark.gif","images/tab_pos_data_light.gif",document.forms[0]["#orderTypeCode"].value=="SOP"?"inline":"none", "doPagaDataSave()" );
		vat.tabm.createButton(0 ,"xTab7", "簽核資料" 		,"vatApprovalDiv","images/tab_approval_data_dark.gif","images/tab_approval_data_light.gif","none");
		
		//for 儲位用
 		enableStorage = "T2" == document.forms[0]["#loginBrandCode"    ].value;
		if(enableStorage){
  			vat.tabm.createButton(0, "xTab6", "儲位資料", "vatStorageDiv", "images/tab_storage_detail_dark.gif", "images/tab_storage_detail_light.gif", "", "reloadStorageDetail()");
  		}
	}
	
	masterInitial();
	detailInitial();
	paymentInitial();
	posInitial();
	totalInitial();
	
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
		document.forms[0]["#orderTypeCode"].value, 
    	vat.item.getValueByName("#F.orderNo"),
    	document.forms[0]["#loginEmployeeCode"].value );
    	
    //for 儲位用
	if(enableStorage){
		kweStorageBlock();
	}
	checkCustomsStatus();  	
	//doFormAccessControl();
}

function initialVatBeanOther(formAction){

		var priceType			= vat.item.getValueByName("#F.priceType");
      	var warehouseEmployee	= vat.item.getValueByName("#F.createdBy");
      	var warehouseManager	= vat.item.getValueByName("#F.warehouseManager");
	    var beforeChangeStatus	= vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
	    //var assignmentId        = document.forms[0]["#assignmentId"      ].value;
	    var approvalResult      = vat.item.getValueByName("#F.approvalResult");
		//var processId           = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');//for ceap防重送單單
		//var inProcessing        = !(processId === null || processId === ""  || processId === 0);//for ceap防重送單單
	    
	    if(approvalResult == true){
	    	approvalResult = "true";
	    }else{
	    	approvalResult = "false";
	    	formAction = "REJECT";
	    }
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");

		if(null != vat.bean().vatBeanOther){
			var storageHeadId = vat.bean().vatBeanOther.storageHeadId;
			var beanHead = vat.bean().vatBeanOther.beanHead;
			var beanItem = vat.bean().vatBeanOther.beanItem;
			var quantity = vat.bean().vatBeanOther.quantity;
			var storageTransactionType = vat.bean().vatBeanOther.storageTransactionType;
			var storageStatus = vat.bean().vatBeanOther.storageStatus;
			var arrivalWarehouse = vat.bean().vatBeanOther.arrivalWarehouse;
		}
		
		vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,	
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          firstRecordNumber		: 0,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0,
	      beforeChangeStatus	: "",
	      formAction			: "",
	      approvalResult		: "",
	      approvalComment		: "",
	      priceType				: "",
	      warehouseEmployee		: "",
	      warehouseManager		: "",
	      organizationCode		: "TM"
        };
        
			vat.bean().vatBeanOther.beforeChangeStatus	= beforeChangeStatus;
			vat.bean().vatBeanOther.formAction      	= formAction;
			vat.bean().vatBeanOther.approvalResult  	= approvalResult;
			vat.bean().vatBeanOther.approvalComment 	= approvalComment;
			vat.bean().vatBeanOther.priceType			= priceType;
			vat.bean().vatBeanOther.warehouseEmployee	= warehouseEmployee;
			vat.bean().vatBeanOther.warehouseManager	= warehouseManager;
					
			vat.bean().vatBeanOther.storageHeadId       = storageHeadId;
			vat.bean().vatBeanOther.beanHead       		= beanHead;
			vat.bean().vatBeanOther.beanItem       		= beanItem;
			vat.bean().vatBeanOther.quantity       		= quantity;
			vat.bean().vatBeanOther.storageTransactionType  = storageTransactionType;
			vat.bean().vatBeanOther.storageStatus       = storageStatus;
			vat.bean().vatBeanOther.arrivalWarehouse    = arrivalWarehouse;
						
}

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   		vat.bean.init(	
	  		function(){
				return "process_object_name=soSalesOrderMainAction&process_object_method_name=performInitial"; 
	    	},{								
	    		other: true
    	});
  	}
}

function checkCustomsStatus(){
	//alert(vat.item.getValueByName("#F.customsStatusHidden"));
	var statusHidden = vat.item.getValueByName("#F.customsStatusHidden");
	var cusStatusHidden = statusHidden.substring(0, 1);
	if(cusStatusHidden !== ""){
	 	if(cusStatusHidden == "A"){
	 		vat.item.setValueByName("#F.customsStatus", "待上傳");
	 	}
	 	if(cusStatusHidden == "N"){
	 		vat.item.setValueByName("#F.customsStatus", "已上傳成功");
	 	}
	 	if(cusStatusHidden == "E"){
	 		vat.item.setValueByName("#F.customsStatus", "上傳錯誤");
	 	}
	 }else{
	 	vat.item.setValueByName("#F.customsStatus", "未上傳");
	 }
}

function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
	 	
	 	        {name:"#B.new"         	, type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          	, type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      	, type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 	openMode:"open", 
	 									 	service:"So_SalesOrder:search:20100101.page",
											left:0, right:0, width:1024, height:768,	
											servicePassData:function(){ return doPassData("", "orderTypeCode");},   
	 									 	serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.copy"     	, type:"IMG"    ,value:"複製"	,   src:"./images/button_copy.gif", eClick:''},
	 	 		{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 	 		{name:"#B.exit"     	, type:"IMG"    ,value:"離開"	,   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.submit"   	, type:"IMG"    ,value:"送出"	,	src:"./images/button_submit.gif", eClick:'doSubmit("SIGNING")'},
	 			{name:"SPACE"			, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.save"			, type:"IMG"    ,value:"暫存"	,   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"      		, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.void"     	, type:"IMG"    ,value:"作廢"   	,   src:"./images/button_void.gif", eClick:'doSubmit("VOID")'},
	 			{name:"SPACE"      		, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.print"    	, type:"IMG"    ,value:"銷貨單列印",  src:"./images/button_sales_order_report.gif", eClick:'openReportWindow()'},
	 			{name:"SPACE"      		, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.uncomfirm"    , type:"IMG"    ,value:"反確認"  ,   src:"./images/button_uncomfirm.gif", eClick:'doSubmit("UNCONFIRMED")'},
	 			{name:"SPACE"      		, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.submitBG" 	, type:"IMG"    ,value:"背景送出"	,   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
				{name:"#B.message"  	, type:"IMG"    ,value:"訊息提示"	,   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.export"  		, type:"IMG"    ,value:"明細匯出"	,   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif", 
	 									 openMode:"open", 
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return doImport(); },
	 									 left:0, right:0, width:600, height:400,	
	 									 serviceAfterPick:function(){afterImportSuccess();}},
	 			//{name:"#B.import"  		, type:"IMG"    ,value:"明細匯入"	,   src:"./images/button_detail_import.gif", eClick:'doImport()'},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			//for 儲位用
	 			{name:"#B.storageExport", 	type:"IMG"    ,value:"儲位匯出",   src:"./images/button_storage_export.gif" , eClick:'exportStorageFormData()'},
	 			{name:"#B.storageImport",	type:"PICKER" , value:"儲位匯入",  src:"./images/button_storage_import.gif"  , 
						 openMode:"open", 
						 service:"/erp/fileUpload:standard:2.page",
						 servicePassData:function(x){ return importStorageFormData(); },
						 left:0, right:0, width:600, height:400,	
						 serviceAfterPick:function(){}},
				{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.extendItemInfo", type:"IMG"   ,value:"核銷報單"	,   src:"./images/button_declaration.gif", eClick:'doExtendItemInfo()'},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			
	 			{name:"#B.sendCustoms" 	   , type:"IMG"    ,value:"上傳海關",   src:"./images/send_customs1.jpg", eClick:'updateCustomsStatus("")'},
	 			{name:"#B.sendCancel" 	   , type:"IMG"    ,value:"註銷上傳",   src:"./images/send_customs3.jpg", eClick:'updateCustomsStatus("cancel")'},
	 			
	 			{name:"#B.first"    	, type:"IMG"    ,value:"第一筆"  	,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.forward"  	, type:"IMG"    ,value:"上一筆"	,   src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.next"        	, type:"IMG"    ,value:"下一筆"	,   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.last"        	, type:"IMG"    ,value:"最後一筆"	,   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

function headerInitial(){
vat.block.create(vnB_Header , {
	id: "vatHeadDiv", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"銷貨單維護作業", rows:[  
	 {row_style:"", cols:[
		{items:[{name:"#L.orderType", type:"LABEL", value:"單別"}]},	 
		{items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode", back:false, mode:"READONLY"},
				{name:"#F.orderCondition", type:"TEXT", bind:"orderCondition", size:2, mode:"HIDDEN"}]},		 
		{items:[{name:"#L.orderNo", type:"LABEL", value:"單號"}]},
		{items:[{name:"#F.orderNo", type:"TEXT", bind:"orderNo", back:false, size:20, mode:"READONLY"},
		 		{name:"#F.headId", type:"TEXT", bind:"headId", back:false, size:10, mode:"READONLY"}
				//for 儲位用
				,{name:"#F.storageHeadId",   type:"TEXT",   bind:"storageHeadId",    back:false, mode:"READONLY" }
	 			]},
		{items:[{name:"#L.salesOrderDate", type:"LABEL", value:"銷貨日期"}]},
		{items:[{name:"#F.salesOrderDate", type:"DATE", bind:"salesOrderDate", size:1}]},
		{items:[{name:"#L.schedule", type:"LABEL", value:"銷貨班次"}]},
		{items:[{name:"#F.schedule", type:"TEXT", bind:"schedule"}]},			 
		{items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},
		{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", back:false, size:2, mode:"HIDDEN"},
	         	{name:"#F.brandName", type:"TEXT", bind:"brandName", size:8, mode:"READONLY"}]}, 
	 	{items:[{name:"#L.status", type:"LABEL", value:"狀態"}]},	 		 
	 	{items:[{name:"#F.status", type:"TEXT", bind:"status", size:2, mode:"HIDDEN"},
	         	{name:"#F.statusName", type:"TEXT", bind:"statusName", size:8, mode:"READONLY"},
	         	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#F.tranRecordStatus"  , type:"TEXT"  ,  bind:"tranRecordStatus", mode:"HIDDEN"},
	 			{name:"#F.tranAllowUpload"  , type:"TEXT"  ,  bind:"tranAllowUpload", mode:"HIDDEN"},
	 			{name:"#F.customsStatus", 				type:"TEXT", mode:"READONLY"},
	 			{name:"#F.customsStatusHidden", 				type:"TEXT",  bind:"customsStatus", mode:"HIDDEN"}
	         	]}]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.customerType", type:"LABEL", value:"客戶代號"}]},
	 	{items:[{name:"#F.searchCustomerType", type:"SELECT", bind:"searchCustomerType", back:false},
		        {name:"#F.customerCode_var", type:"TEXT", bind:"customerCode_var", back:false, size:12, maxLen:12, eChange:"changeCustomerCode()"},
		        {name:"#F.customerType", type:"TEXT", bind:"customerType", size:8, mode:"HIDDEN"},
		        {name:"#F.customerCode", type:"TEXT", bind:"customerCode", size:8, mode:"HIDDEN"},
				{name:"#B.customerCode", value:"選取", type:"PICKER" ,
				 									openMode:"open", src:"./images/start_node_16.gif",
				 									service:"Bu_AddressBook:searchCustomer:20100101.page", 
				 									left:0, right:0, width:1024, height:768,	
				 									serviceAfterPick:function(){doAfterPickerCustomer();} },
		 		{name:"#F.customerName", type:"TEXT", bind:"customerName", back:false, size:12, mode:"READONLY"}], td:" colSpan=1"},
		{items:[{name:"#L.customsNo", type:"LABEL", value:" 上傳單號 "}]},
		{items:[{name:"#F.customsNo", type:"TEXT", bind:"customsNo", size:20, maxLen:14}]}, 		
		{items:[{name:"#L.verificationStatus", type:"LABEL", value:" 財務確認 ", size:1}]},
		{items:[{name:"#F.verificationStatus", type:"CHECKBOX", bind:"verificationStatus", size:1}]},
		
	 	{items:[{name:"#L.guiCode", type:"LABEL", value:"統一編號"}]},
	 	{items:[{name:"#F.guiCode", type:"TEXT", bind:"guiCode", size:8, maxLen:8},
	 		 	{name:"#F.priceType", type:"TEXT", bind:"priceType", back:false, size:1, mode:"HIDDEN"},
	 		 	{name:"#F.vipTypeCode", type:"TEXT", bind:"vipTypeCode", size:1, mode:"HIDDEN"}]},	 
		{items:[{name:"#L.createdBy", type:"LABEL", value:"填單人員"}]},
		{items:[{name:"#F.createdBy", type:"TEXT", bind:"createdBy", mode:"HIDDEN", size:6},
				{name:"#F.createdByName", type:"TEXT", bind:"createdByName", mode:"READONLY", size:6}]},	 
		{items:[{name:"#L.creationDate", type:"LABEL", value:"填單日期"}]},
		{items:[{name:"#F.creationDate", type:"TEXT", bind:"creationDate", mode:"READONLY", size:8}]}]}
 	 ],	 
	 beginService:"",
	 closeService:function(){closeHeader();}			
	});
}

function closeHeader(){
	vat.item.SelectBind(vat.bean("orderTypes"), { itemName : "#F.orderTypeCode" });
	vat.item.SelectBind([["","",false],["客戶代號","身分ID"],["customerCode","identify"]], { itemName : "#F.searchCustomerType" });
}

function masterInitial(){
	var branchCode = vat.bean("branchCode");
	
vat.block.create(vnB_Master, {
	id: "vatMasterDiv", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	rows:[
		{row_style:branchCode=="2"?"":" style='display:none'", cols:[
		{items:[{name:"#L.exportDeclNo", type:"LABEL" , value:"報關單號" }]},
	 	{items:[{name:"#F.exportDeclNo", type:"TEXT", bind:"exportDeclNo", mode:"READONLY", size:20, eChange:"getCMHead()" }]},
	 	{items:[{name:"#L.exportDeclDate", type:"LABEL" , value:"報關日期" }]},
	 	{items:[{name:"#F.exportDeclDate", type:"DATE", bind:"exportDeclDate", mode:"READONLY", size:8 }]},
	 	{items:[{name:"#L.exportDeclType", type:"LABEL" , value:"報關類別" }]},
	 	{items:[{name:"#F.exportDeclType", type:"TEXT", bind:"exportDeclType", mode:"READONLY", size:6 }],td:"colSpan=3"}]},
	{row_style:branchCode=="2"?"":" style='display:none'", cols:[
		{items:[{name:"#L.latestExportDeclNo", type:"LABEL" , value:"退關後報關單號" }]},
	 	{items:[{name:"#F.latestExportDeclNo", type:"TEXT", bind:"latestExportDeclNo", mode:"READONLY", size:20 }]},
	 	{items:[{name:"#L.latestExportDeclDate", type:"LABEL" , value:"退關後報關日期" }]},
	 	{items:[{name:"#F.latestExportDeclDate", type:"DATE", bind:"latestExportDeclDate", mode:"READONLY", size:8 }]},
	 	{items:[{name:"#L.latestExportDeclType", type:"LABEL" , value:"退關後報關類別" }]},
	 	{items:[{name:"#F.latestExportDeclType", type:"TEXT", bind:"latestExportDeclType", mode:"READONLY", size:6 }],td:"colSpan=3"}]},  
	{row_style:"", cols:[
		 {items:[{name:"#L.shopCode", type:"LABEL", value:"專櫃代號"}]},
		 {items:[{name:"#F.shopCode", type:"SELECT", bind:"shopCode", eChange:"changeShopCode()"}]},	 
		 {items:[{name:"#L.sufficientQuantityDelivery", type:"LABEL", value:"足量出貨"}]},
		 {items:[{name:"#F.sufficientQuantityDelivery", type:"SELECT", bind:"sufficientQuantityDelivery"}]},
		 {items:[{name:"#L.superintendentCode", type:"LABEL", value:"訂單負責人"}]},
		 {items:[{name:"#F.superintendentCode", type:"TEXT", bind:"superintendentCode", size:10, maxLen:15,eChange:"changeSuperintendent()"},
		 		 {name:"#B.superintendentCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
		 		 {name:"#F.superintendentName", type:"TEXT", bind:"superintendentName", size:10, mode:"readOnly"}]}]},	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.contactPerson", type:"LABEL", value:"客戶聯絡窗口"}]},
		 {items:[{name:"#F.contactPerson", type:"TEXT", bind:"contactPerson", size:20, maxLen:20}]},	
		 {items:[{name:"#L.contactTel", type:"LABEL", value:"客戶聯絡電話"}]},
		 {items:[{name:"#F.contactTel", type:"TEXT", bind:"contactTel", size:20, maxLen:20}]},	 
		 {items:[{name:"#L.receiver", type:"LABEL", value:"收貨人"}]},
		 {items:[{name:"#F.receiver"  , type:"TEXT" , bind:"receiver", size:20, maxLen:20}]}]},	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.customerPoNo", type:"LABEL", value:"原訂單編號"}]},
		 {items:[{name:"#F.customerPoNo", type:"TEXT", bind:"customerPoNo", size:20, maxLen:20}]},	
		 {items:[{name:"#L.quotationCode", type:"LABEL", value:"報價單編號"}]},
		 {items:[{name:"#F.quotationCode", type:"TEXT", bind:"quotationCode", size:20, maxLen:20}]},	 
		 {items:[{name:"#L.scheduleShipDate", type:"LABEL", value:"預計出貨日"}]},
		 {items:[{name:"#F.scheduleShipDate", type:"DATE", bind:"scheduleShipDate"}]}]},	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.countryCode", type:"LABEL", value:"國別"}]},
		 {items:[{name:"#F.countryCode", type:"SELECT", bind:"countryCode"}]},	
		 {items:[{name:"#L.currencyCode", type:"LABEL", value:"幣別"}]},
		 {items:[{name:"#F.currencyCode", type:"SELECT", bind:"currencyCode" ,eChange:"changeExchangeRate()"},
		 		 {name:"#L.exportExchangeRate", type:"LABEL", value:" 兌換匯率 "},
		 		 {name:"#F.exportExchangeRate", type:"TEXT", bind:"exportExchangeRate", size:4}]},	 
		 {items:[{name:"#L.homeDelivery", type:"LABEL", value:"運送方式"}]},
		 {items:[{name:"#F.homeDelivery", type:"SELECT", bind:"homeDelivery"}]}]},	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.paymentTermCode", type:"LABEL", value:"付款條件"}]},
		 {items:[{name:"#F.paymentTermCode", type:"SELECT", bind:"paymentTermCode"}]},	
		 {items:[{name:"#L.scheduleCollectionDate", type:"LABEL", value:"付款日"}]},
		 {items:[{name:"#F.scheduleCollectionDate", type:"DATE", bind:"scheduleCollectionDate"}]},	 
		 {items:[{name:"#L.paymentCategory", type:"LABEL", value:"付款方式"}]},
		 {items:[{name:"#F.paymentCategory", type:"SELECT" , bind:"paymentCategory"}]}]},	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.invoiceAddressTitle", type:"LABEL", value:"發票地址"}]},
		 {items:[{name:"#L.invoiceCity", type:"LABEL", value:"城市:&nbsp&nbsp;"},
		 		 {name:"#F.invoiceCity", type:"TEXT" ,  bind:"invoiceCity", size:10, maxLen:10},
				 {name:"#L.invoiceArea", type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
		 		 {name:"#F.invoiceArea", type:"TEXT",   bind:"invoiceArea", size:10, maxLen:10},
		 		 {name:"#L.invoiceZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
		 		 {name:"#F.invoiceZipCode", type:"TEXT", bind:"invoiceZipCode", size:5, maxLen:5},	 		 
		 		 {name:"#L.invoiceAddress", type:"LABEL", value:"&nbsp&nbsp&nbsp;地址:&nbsp&nbsp;"},
	 			 {name:"#F.invoiceAddress", type:"TEXT", bind:"invoiceAddress", size:70, maxLen:200}], td:" colSpan=5"}]},	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.shipAddressTitle", type:"LABEL", value:"送貨地址"}]},
		 {items:[{name:"#L.shipCity", type:"LABEL", value:"城市:&nbsp&nbsp;"},
		 		 {name:"#F.shipCity", type:"TEXT" ,  bind:"shipCity", size:10, maxLen:10},
				 {name:"#L.shipArea", type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
		 		 {name:"#F.shipArea", type:"TEXT",   bind:"shipArea", size:10, maxLen:10},
		 		 {name:"#L.shipZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
		 		 {name:"#F.shipZipCode", type:"TEXT", bind:"shipZipCode", size:5, maxLen:5},	 		 
		 		 {name:"#L.shipAddress", type:"LABEL", value:"&nbsp&nbsp&nbsp;地址:&nbsp&nbsp;"},
	 			 {name:"#F.shipAddress", type:"TEXT", bind:"shipAddress", size:70, maxLen:100}], td:" colSpan=5"}]},			 
 	 {row_style:"", cols:[
		 {items:[{name:"#L.invoiceTypeCode", type:"LABEL", value:"發票類型"}]},
		 {items:[{name:"#F.invoiceTypeCode", type:"SELECT", bind:"invoiceTypeCode"}]},	
		 {items:[{name:"#L.taxType", type:"LABEL", value:"稅別"}]},
		 {items:[{name:"#F.taxType", type:"SELECT", bind:"taxType", eChange:"changeTaxRate()"}]},	 
		 {items:[{name:"#L.taxRate", type:"LABEL", value:"稅率"}]},
		 {items:[{name:"#F.taxRate", type:"TEXT" , bind:"taxRate", size:2, maxLen:4, mode:"readOnly"},
		 		 {name:"#L.%", type:"LABEL", value:"%"}]}]},	 
     {row_style:"", cols:[
		 {items:[{name:"#L.defaultWarehouseCode", type:"LABEL", value:"庫別"}]},
		 {items:[{name:"#F.defaultWarehouseCode", type:"SELECT", bind:"defaultWarehouseCode", eChange:"changeDefaultWarehouseCode()"},
		 		 {name:"#F.warehouseManager", type:"TEXT", bind:"warehouseManager", mode:"hidden"}]},		 
		 {items:[{name:"#L.promotionCode", type:"LABEL", value:"活動代號"}]},
		 {items:[{name:"#F.promotionCode", type:"TEXT" , bind:"promotionCode", size:20, maxLen:20,eChange:"changePromotionCode()"},
		 		 {name:"#F.promotionName", type:"TEXT" , size:20, maxLen:20, mode:"readOnly"}]},
		 {items:[{name:"#L.discountRate", type:"LABEL", value:"折扣比率"}]},
		 {items:[{name:"#F.discountRate", type:"TEXT" , bind:"discountRate", size:2, maxLen:4, eChange:"changeDiscountRate()"},
		 		 {name:"#L.%", type:"LABEL", value:"%"}]}]},
	 {row_style:branchCode=="2"?"":" style='display:none'", cols:[
		 {items:[{name:"#L.itemCategory", type:"LABEL", value:"業種子類"}]},
		 {items:[{name:"#F.itemCategory", type:"SELECT", bind:"itemCategory",mode:branchCode=="2"?"":"readOnly"}]},
		 {items:[{name:"#L.orderDiscountType", type:"LABEL", value:"商品折扣類型"}]},
		 {items:[{name:"#F.orderDiscountType", type:"SELECT", bind:"orderDiscountType"}]},		 
		 {items:[{name:"#L.exportCommissionRate", type:"LABEL", value:"手續費率"}]},
		 {items:[{name:"#F.exportCommissionRate", type:"TEXT" , bind:"exportCommissionRate", size:2, maxLen:4, eChange:"changeCommissionRate()"},
		 		 {name:"#L.%", type:"LABEL", value:"%"}]}]},	 	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.remark1", type:"LABEL", value:"備註一"}]},
		 {items:[{name:"#F.remark1", type:"TEXT", bind:"remark1", size:100, maxLen:100}]},
		 {items:[{name:"#L.appCustomerCode", type:"LABEL", value:"APP代號"}]},
		 {items:[{name:"#F.appCustomerCode", type:"TEXT", bind:"appCustomerCode", size:20, maxLen:20, mode:"READONLY"}]},
		 {items:[{name:"#L.exportExpense", type:"LABEL", value:"其他費用"}]},
		 {items:[{name:"#F.exportExpense", type:"NUMM" , bind:"exportExpense"}]}]},	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.remark2", type:"LABEL", value:"備註二"}]},
		 {items:[{name:"#F.remark2", type:"TEXT", bind:"remark2", size:100, maxLen:100}], td:" colSpan=3"},
		 {items:[{name:"#L.attachedInvoice", type:"LABEL", value:"隨附發票"}]},
		 {items:[{name:"#F.attachedInvoice", type:"SELECT" , bind:"attachedInvoice"}]}]}],
	 beginService:"",
	 closeService:function(){closeMaster();}			
	});
}

function closeMaster(){
	var brandCode = vat.bean("brandCode");
	if("T2" == brandCode){
		vat.item.setAttributeByName("#F.sufficientQuantityDelivery", "readOnly", true);
	}
}

function detailInitial(){
	var brandCode = vat.bean("brandCode");
    var status = vat.item.getValueByName("#F.status");
    var orderTypeCode = document.forms[0]["#orderTypeCode"].value;
    var orderCondition = vat.item.getValueByName("#F.orderCondition");
    var orderNoTmp = vat.item.getValueByName("#F.orderNo");
    var processId = document.forms[0]["#processId"].value;
    var canBeMod = document.forms[0]["#canBeMod"].value;
    var hideExport = true;
    if("F" == orderCondition){
		hideExport = false;
	}
    var POS = false;
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;
	vEnable = "N";
	if(orderTypeCode == "SOP"){
	    if(status == "SAVE" || status == "UNCONFIRMED"){
		    vEnable = "Y";	
	    }
	}else{
	    if( (status == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((status == "SAVE" || status == "REJECT") && processId != "" && processId != 0) ){
			vEnable = "Y";		
	    }	
	}
	
	if(brandCode == "T2" && orderTypeCode == "SOP"){
		POS = true;
	}
	
	
	// set column
    vat.item.make(vnB_Detail, "indexNo", 					{type:"IDX" , view: "fixed", desc:"序號"});
	vat.item.make(vnB_Detail, "itemCode", 					{type:"TEXT", size:15, view: "fixed", maxLen:20, desc:"品號", eChange:"changeItemData(1)"});
	vat.item.make(vnB_Detail, "itemCName", 					{type:"TEXT", size:18, view: "fixed", maxLen:20, desc:"品名", mode:"READONLY"});
	vat.item.make(vnB_Detail, "warehouseCode", 				{type:"TEXT", size: 4, view: "", maxLen:12, desc:"庫別", mode:"READONLY"});
	vat.item.make(vnB_Detail, "warehouseName", 				{type:"TEXT", size:10, view: "", maxLen:20, desc:"庫名", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "originalForeignUnitPrice", 	{type:"NUMM", size: 6, view: "", maxLen:12, desc:"原幣<br>單價", dec:6, mode:hideExport?"HIDDEN":"READONLY"});
	vat.item.make(vnB_Detail, "originalUnitPrice", 			{type:"NUMM", size: 6, view: "", maxLen:12, desc:"單價", dec:6, mode:"READONLY"});
	vat.item.make(vnB_Detail, "bonusPointAmount", 			{type:"NUMM", size: 6, view: "", maxLen:12, desc:"可轉換點數金額", dec:6, mode:"READONLY"});
	vat.item.make(vnB_Detail, "discountRate", 				{type:"NUMM", size: 2, view: "", maxLen: 6, desc:"折扣率", dec:2, eChange:"changeItemData(2)"});
	vat.item.make(vnB_Detail, "actualForeignUnitPrice", 	{type:"NUMM", size: 6, view: "", maxLen:12, desc:"原幣<br>折扣後單價", dec:6, mode:hideExport?"HIDDEN":"", eChange:"changeItemData(3)"});
	vat.item.make(vnB_Detail, "actualUnitPrice", 			{type:"NUMM", size: 6, view: "", maxLen:12, desc:"折扣後單價", dec:6, mode:"READONLY"});
	vat.item.make(vnB_Detail, "currentOnHandQty", 			{type:"NUMM", size: 4, view: "", maxLen:12, desc:"庫存量", dec:2, mode:"READONLY"});
	vat.item.make(vnB_Detail, "quantity", 					{type:"NUMM", size: 4, view: "", maxLen: 8, desc:"數量", dec:2, onchange:"changeItemData(3)"});
	vat.item.make(vnB_Detail, "originalForeignSalesAmt", 	{type:"NUMM", size: 6, view: "shift", maxLen:20, desc:"原幣<br>金額", dec:6, mode:hideExport?"HIDDEN":"READONLY"});
	vat.item.make(vnB_Detail, "originalSalesAmount", 		{type:"NUMM", size: 6, view: "shift", maxLen:20, desc:"金額", dec:6, mode:"READONLY"});
	vat.item.make(vnB_Detail, "actualForeignSalesAmt", 		{type:"NUMM", size: 6, view: "shift", maxLen:20, desc:"原幣<br>折扣後金額", dec:6, mode:hideExport?"HIDDEN":"READONLY"});
	vat.item.make(vnB_Detail, "actualSalesAmount", 			{type:"NUMM", size: 6, view: "shift", maxLen:20, desc:"折扣後金額", dec:6, mode:hideExport?"READONLY":""});
	vat.item.make(vnB_Detail, "deductionForeignAmount", 	{type:"NUMM", size: 1, view: "shift", maxLen: 8, desc:"原幣<br>折讓金額", dec:6, mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "deductionAmount", 			{type:"NUMM", size: 1, view: "shift", maxLen: 8, desc:"折讓金額", dec:6, mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "taxType", 					{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"稅別", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "taxRate", 					{type:"NUMM", size: 1, view: "shift", maxLen: 8, desc:"稅率", dec:2, mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "isTax", 						{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"是否含稅", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "promotionCode", 				{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"活動代號", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "discountType", 				{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"活動折扣類型", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "discount", 					{type:"NUMM", size: 1, view: "shift", maxLen: 1, desc:"活動折扣", dec:2, mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "vipPromotionCode", 			{type:"TEXT", size: 1, view: "shift", maxLen: 20, desc:"VIP類別代號", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "vipDiscountType", 			{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"VIP折扣類型", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "vipDiscount", 				{type:"NUMM", size: 1, view: "shift", maxLen: 1, desc:"VIP折扣", dec:2, mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "watchSerialNo", 				{type:"TEXT", size: 20, view: "shift", maxLen: 20, desc:"手錶序號", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "watchSerialNoPicker",		{type:"PICKER", view:"shift", value:"PICKER", src:"./images/start_node_16.gif", mode:"HIDDEN",
			 									 			openMode:"open", 
			 									 		 	service:"Im_Item:searchSerialLine:20100404.page", 
			 									 		 	left:0, right:0, width:1024, height:768,
			 									 		 	servicePassData:function(X){ return doPassData(X, "watchSerialNo");},
			 									 			serviceAfterPick:function(X){doAfterPickerWatchSerialNo(X);}});
	vat.item.make(vnB_Detail, "depositCode", 				{type:"TEXT", size: 1, view: "shift", maxLen: 8, desc:"訂金單代號", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "isUseDeposit", 				{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"訂金支付", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "isServiceItem", 				{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"服務性商品", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "taxAmount", 					{type:"NUMM", size: 1, view: "shift", maxLen: 1, desc:"稅額", dec:2, mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "importCost", 				{type:"TEXT", size: 1, view: "shift", maxLen: 20, desc:"進貨成本", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "importCurrencyCode", 		{type:"TEXT", size: 1, view: "shift", maxLen: 20, desc:"進貨幣別", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "importDeclNo", 				{type:"TEXT", size:16, view: "shift", maxLen: 20, desc:"原進口<br>報單單號", mode:hideExport?"HIDDEN":"", eChange:"changeDeclNo()"});
	vat.item.make(vnB_Detail, "searchImportDeclNo",         {type:"PICKER", view:"shift" , desc:"", openMode:"open", src:"./images/start_node_16.gif", mode:hideExport?"HIDDEN":"",   // 
			 									 			service:"Cm_DeclarationOnHand:search:20091103.page",
			 									 			left:0, right:0, width:1024, height:768,	
			 									 			servicePassData:function(X){ return doPassData(X, "detailLine"); },
			 									 			serviceAfterPick:function(X){doAfterPickerImportDeclNo(X); } }); 
	vat.item.make(vnB_Detail, "importDeclType", 			{type:"TEXT", size: 4, view: "shift", maxLen: 20, desc:"原進口<br>報單類別", mode:hideExport?"HIDDEN":"readOnly"});
	vat.item.make(vnB_Detail, "importDeclDate", 			{type:"DATE", size: 4, view: "shift", maxLen: 20, desc:"進口日", mode:hideExport?"HIDDEN":"readOnly"});
	vat.item.make(vnB_Detail, "importDeclSeq", 				{type:"NUMM", size: 1, view: "shift", maxLen: 20, desc:"原進口<br>報單項次", mode:hideExport?"HIDDEN":""});
	vat.item.make(vnB_Detail, "perUnitAmount", 				{type:"NUMM", size: 16, view: "shift", maxLen: 20, desc:"原進口<br>報單進貨單價", mode:hideExport?"HIDDEN":""});
	vat.item.make(vnB_Detail, "lotNo", 						{type:"TEXT", size: 1, view: "shift", maxLen: 20, desc:"批號", mode:hideExport?"HIDDEN":""});
	vat.item.make(vnB_Detail, "usedIdentification", 		{type:"TEXT", size:10, view: "shift", maxLen: 20, desc:"使用身份", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "usedCardId", 				{type:"TEXT", size: 5, view: "shift", maxLen: 20, desc:"使用卡號", mode:POS?"":"HIDDEN", eChange:"changeCardId()"});
	vat.item.make(vnB_Detail, "usedCardType", 				{type:"TEXT", size: 5, view: "shift", maxLen: 20, desc:"使用卡別", mode:POS?"READONLY":"HIDDEN"});
	vat.item.make(vnB_Detail, "usedDiscountRate", 			{type:"NUMM", size: 5, view: "shift", maxLen: 20, desc:"使用折扣率", dec:2, mode:POS?"READONLY":"HIDDEN"});
	vat.item.make(vnB_Detail, "itemDiscountType", 			{type:"TEXT", size: 5, view: "shift", maxLen: 20, desc:"商品折扣類型", mode:POS?"READONLY":"HIDDEN"});
	vat.item.make(vnB_Detail, "combineCode", 			    {type:"TEXT", size: 8, view: "shift", maxLen: 8, desc:"組合代號", mode:"READONLY"});	
	vat.item.make(vnB_Detail, "allowMinusStock", 			{type:"TEXT", size: 5, view: "shift", maxLen: 20, desc:"是否允許負庫存", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "allowWholeSale", 			{type:"CHECKBOX", size: 5, view: "shift", maxLen: 20, desc:"強制售出", mode:"Y"==canBeMod?"":"HIDDEN"});
	vat.item.make(vnB_Detail, "lineId", 					{type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", 				{type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", 			{type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Detail, "message", 					{type:"MSG", desc:"訊息"});
	vat.item.make(vnB_Detail, "advanceInput", 				{type:"BUTTON", view: "fixed", desc:"進階輸入", value:"進階輸入", src:"images/button_advance_input.gif", eClick:"advanceInput()"});

	vat.block.pageLayout(vnB_Detail, {
								id: "vatDetailDiv", 
								pageSize: 10,			
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,
							    appendBeforeService : "appendBeforeService()",
							    appendAfterService  : "",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "loadSuccessAfter()",
								eventService        : "changeRelationData()",     
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()"
								});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function closeDetail(){
}

function paymentInitial(){
    var status = vat.item.getValueByName("#F.status");
    var processId = document.forms[0]["#processId"].value;
    var orderTypeCode = document.forms[0]["#orderTypeCode"].value;
    var orderNo = vat.item.getValueByName("#F.orderNo");
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;
	// set column
    vat.item.make(vnB_Payment, "indexNo1", {type:"IDX" , desc:"序號"});
	vat.item.make(vnB_Payment, "posPaymentType", {type:"SELECT", size:1, desc:"付款類型" ,eChange:"changePaymentExchangeRate()"});
	vat.item.make(vnB_Payment, "foreignCurrencyCode", {type:"TEXT", size:1, desc:"幣別", mode:"readOnly" });
	vat.item.make(vnB_Payment, "foreignAmount", {type:"NUMB", size:8, maxLen:8, desc:"金額", mask:"CCCCCCCCCCCC" ,eChange:"changePayment()"});
	vat.item.make(vnB_Payment, "exchangeRate", {type:"NUMB", size:8, maxLen:8, desc:"匯率", mask:"CCCCCCCCCCCC" ,eChange:"changePayment()"});
	vat.item.make(vnB_Payment, "localCurrencyCode", {type:"TEXT", size:1, desc:"本幣幣別", mode:"HIDDEN"});
	vat.item.make(vnB_Payment, "localAmount", {type:"NUMB", size:8, maxLen:8, desc:"本幣金額", mode:"READONLY"});
	vat.item.make(vnB_Payment, "discountRatePay", {type:"NUMB", size:8, maxLen:4, desc:"折扣率", mask:"CCCCCCCCCCCC"});
	vat.item.make(vnB_Payment, "payNo", {type:"TEXT", size:8, maxLen:4, desc:"付款登記"});
	vat.item.make(vnB_Payment, "remark1", {type:"TEXT", size:40, maxLen:100, desc:"備註"});
	vat.item.make(vnB_Payment, "posPaymentId", {type:"ROWID"});
	vat.item.make(vnB_Payment, "isLockRecord1", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Payment, "isDeleteRecord1", {type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Payment, "message1", {type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnB_Payment, {
								id: "vatPaymentDiv", 
								pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,														
							    appendBeforeService : appendBeforeService(),
								loadBeforeAjxService: "loadPaymentBeforeAjxService()",
								loadSuccessAfter    : "",
								saveBeforeAjxService: "savePaymentBeforeAjxService()",
								saveSuccessAfter    : ""});
	vat.block.pageDataLoad(vnB_Payment, vnCurrentPage = 1);
}

function closePayment(){
}

function posInitial(){
vat.block.create(vnB_POS, {
	id: "vatPosDiv", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	rows:[  
	 {row_style:"", cols:[
		 {items:[{name:"#L.posMachineCode", type:"LABEL", value:"POS機號"}]},	 
		 {items:[{name:"#F.posMachineCode", type:"SELECT", bind:"posMachineCode"}]},		 
		 {items:[{name:"#L.casherCode", type:"LABEL", value:"收銀員代號"}]},
		 {items:[{name:"#F.casherCode", type:"TEXT", bind:"casherCode", size:15, maxLen:15, eChange:"changeCasherCode()"},
		 		 {name:"#B.casherCode",	value:"選取" ,type:"PICKER" ,
	 									 	openMode:"open", src:"./images/start_node_16.gif",
	 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 	left:0, right:0, width:1024, height:768,	
	 									 	serviceAfterPick:function(){doAfterPickerCasher();}},
		 		 {name:"#F.casherName", type:"TEXT", bind:"casherName", size:15, mode:"READONLY"}]},
		 {items:[{name:"#L.departureDate", type:"LABEL", value:"出境日期"}]},
		 {items:[{name:"#F.departureDate", type:"DATE", bind:"departureDate", size:1}]}]},	
	 {row_style:"", cols:[
		 {items:[{name:"#L.flightNo", type:"LABEL", value:"班機代碼"}]},
		 {items:[{name:"#F.flightNo", type:"TEXT", bind:"flightNo", size:10, maxLen:10}]},	 
		 {items:[{name:"#L.passportNo", type:"LABEL", value:"護照號碼"}]},
		 {items:[{name:"#F.passportNo", type:"TEXT", bind:"passportNo", size:20, maxLen:20}]},	 	 
		 {items:[{name:"#L.ladingNo", type:"LABEL", value:"提貨單號"}]},
		 {items:[{name:"#F.ladingNo", type:"TEXT", bind:"ladingNo", size:20, maxLen:20}]}]},	 
	 {row_style:"", cols:[
		 {items:[{name:"#L.transactionSeqNo", type:"LABEL", value:"交易序號"}]},
		 {items:[{name:"#F.transactionSeqNo", type:"TEXT", bind:"transactionSeqNo", size:20, maxLen:20}]},	 
		 {items:[{name:"#L.salesInvoicePage", type:"LABEL", value:"Sales Invoice page"}]},
		 {items:[{name:"#F.salesInvoicePage", type:"TEXT", bind:"salesInvoicePage", size:20, maxLen:20}]},
		 {items:[{name:"#L.transactionTime", type:"LABEL", value:"交易區間"}]},
		 {items:[{name:"#F.transactionTime", type:"TEXT", bind:"transactionTime", size:3, maxLen:3},		 		 		 		 
		 		 {name:"#F.transactionMinute", type:"TEXT", bind:"transactionMinute", size:3, maxLen:3},
		 		 {name:"#F.transactionSecond", type:"TEXT", bind:"transactionSecond", size:3, maxLen:3}			 		 
		 ]}]
		 
	 }],	 	 
		 	 
	 beginService:"",
	 closeService:function(){closePos();}	
	});
}

function closePos(){
}

function totalInitial(){
    var branchCode = vat.bean("branchCode");
    var brandCode = vat.bean("brandCode");
    var orderTypeCode = vat.bean("orderTypeCode");
    if(branchCode == "2"){
		vat.block.create(vnB_Total, {
		id: "vatTotalDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
			rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.originalTotalFrnSalesAmt"    , type:"LABEL" , value:"原銷售金額"}]},
				{items:[{name:"#F.originalTotalFrnSalesAmt"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalOriginalSalesAmount"    , type:"LABEL" , value:"原銷售金額"}]},	 
				{items:[{name:"#F.totalOriginalSalesAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.actualTotalFrnSalesAmt"	, type:"LABEL" , value:"實際銷售金額"}]},
				{items:[{name:"#F.actualTotalFrnSalesAmt"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalActualSalesAmount"	, type:"LABEL" , value:"實際銷售金額"}]},	 
				{items:[{name:"#F.totalActualSalesAmount"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]}/*,
			{row_style:"", cols:[
				{items:[{name:"#L.totalDeductionFrnAmount" , type:"LABEL" , value:"銷貨折讓"}]},
				{items:[{name:"#F.totalDeductionFrnAmount" , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalDeductionAmount"    , type:"LABEL" , value:"銷貨折讓"}]},	 
				{items:[{name:"#F.totalDeductionAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]}*/,
			{row_style:"", cols:[
				{items:[{name:"#L.totalOtherFrnExpense"	, type:"LABEL" , value:"其他費用"}]},
				{items:[{name:"#F.totalOtherFrnExpense"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalOtherExpense"	, type:"LABEL" , value:"其他費用"}]},	 
				{items:[{name:"#F.totalOtherExpense"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.expenseForeignAmount"	, type:"LABEL" , value:"手續費"}]},
				{items:[{name:"#F.expenseForeignAmount"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.expenseLocalAmount"	, type:"LABEL" , value:"手續費"}]},	 
				{items:[{name:"#F.expenseLocalAmount"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.taxFrnAmount" 	, type:"LABEL" , value:"營業稅"}]},
				{items:[{name:"#F.taxFrnAmount"		, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.taxAmount"    	, type:"LABEL" , value:"營業稅"}]},	 
				{items:[{name:"#F.taxAmount"    	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalForeignAmount" 	, type:"LABEL" , value:"總金額"}]},
				{items:[{name:"#F.totalForeignAmount"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalAmount"    	, type:"LABEL" , value:"總金額"}]},	 
				{items:[{name:"#F.totalAmount"    	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalNoneTaxFrnSalesAmount"	, type:"LABEL" , value:"未稅金額"}]},	 
				{items:[{name:"#F.totalNoneTaxFrnSalesAmount"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalNoneTaxSalesAmount"		, type:"LABEL" , value:"未稅金額"}]},	 
				{items:[{name:"#F.totalNoneTaxSalesAmount"		, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalItemQuantity"	, type:"LABEL" , value:"商品數量"}]},
				{items:[{name:"#F.totalItemQuantity"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalBonusPointAmount"	, type:"LABEL" , value:"可轉換點數總金額"}]},	 
				{items:[{name:"#F.totalBonusPointAmount"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 	]}
	 	 ],
			beginService:"",
			closeService:function(){closeTotal();}			
		});
	}else{
		if(brandCode == "T3CO" && orderTypeCode == "SOW"){
			vat.block.create(vnB_Total, {
			id: "vatTotalDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
				rows:[
				{row_style:"", cols:[
					{items:[{name:"#L.totalOriginalSalesAmount"    , type:"LABEL" , value:"原銷售金額"}]},	 
					{items:[{name:"#F.totalOriginalSalesAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalDeductionAmount"    , type:"LABEL" , value:"折讓"}]},	 
					{items:[{name:"#F.totalDeductionAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalOtherExpense"    , type:"LABEL" , value:"其他費用"}]},	 
					{items:[{name:"#F.totalOtherExpense"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.taxAmount"    , type:"LABEL" , value:"營業稅"}]},	 
					{items:[{name:"#F.taxAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalActualSalesAmount"    , type:"LABEL" , value:"實際銷售金額"}]},	 
					{items:[{name:"#F.totalActualSalesAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalNoneTaxSalesAmount"    , type:"LABEL" , value:"未稅金額"}]},	 
					{items:[{name:"#F.totalNoneTaxSalesAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalItemQuantity"    , type:"LABEL" , value:"商品數量"}]},
					{items:[{name:"#F.totalItemQuantity"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.unSettledCredit"    , type:"LABEL" , value:"未結額度"}]},
					{items:[{name:"#F.unSettledCredit"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.settledCredit"    , type:"LABEL" , value:"已結額度"}]},
					{items:[{name:"#F.settledCredit"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]}
		 	 ],
				beginService:"",
				closeService:function(){closeTotal();}			
			});
		}else{
			vat.block.create(vnB_Total, {
			id: "vatTotalDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
				rows:[
				{row_style:"", cols:[
					{items:[{name:"#L.totalOriginalSalesAmount"    , type:"LABEL" , value:"原銷售金額"}]},	 
					{items:[{name:"#F.totalOriginalSalesAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalDeductionAmount"    , type:"LABEL" , value:"折讓"}]},	 
					{items:[{name:"#F.totalDeductionAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalOtherExpense"    , type:"LABEL" , value:"其他費用"}]},	 
					{items:[{name:"#F.totalOtherExpense"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.taxAmount"    , type:"LABEL" , value:"營業稅"}]},	 
					{items:[{name:"#F.taxAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalActualSalesAmount"    , type:"LABEL" , value:"實際銷售金額"}]},	 
					{items:[{name:"#F.totalActualSalesAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalNoneTaxSalesAmount"    , type:"LABEL" , value:"未稅金額"}]},	 
					{items:[{name:"#F.totalNoneTaxSalesAmount"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
				{row_style:"", cols:[
					{items:[{name:"#L.totalItemQuantity"    , type:"LABEL" , value:"商品數量"}]},
					{items:[{name:"#F.totalItemQuantity"    , type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
			 	]},
			 	{row_style:"", cols:[
					{items:[{name:"#L.totalBonusPointAmount"	, type:"LABEL" , value:"可轉換點數金額"}]},	 
					{items:[{name:"#F.totalBonusPointAmount"	, type:"NUMM"  , dec:4, size:12, mode:"READONLY"}]}
		 		]}
		 	 ],
				beginService:"",
				closeService:function(){closeTotal();}			
			});
		}		
	}
}

function closeTotal(){
	vat.item.SelectBind([["","",true],["是","否"],["Y","N"]], { itemName : "#F.attachedInvoice" });
	vat.item.SelectBind([["","",false],["是","否"],["Y","N"]], { itemName : "#F.sufficientQuantityDelivery" });
	vat.ajax.XHRequest({ 
	post:"process_object_name=soSalesOrderMainService"+
          		"&process_object_method_name=findInitialCommon"+
          		"&headId=" + vat.item.getValueByName("#F.headId"),
          asyn:false,                      
	find: function change(oXHR){
		vat.item.setValueByName("#F.warehouseManager", vat.ajax.getValue("WarehouseManager", oXHR.responseText));
		vat.item.SelectBind(eval(vat.ajax.getValue("shopForEmployee", oXHR.responseText)),{ itemName : "#F.shopCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allAvailableWarehouse", oXHR.responseText)),{ itemName : "#F.defaultWarehouseCode"});
		vat.item.SelectBind(eval(vat.ajax.getValue("allshopMachine", oXHR.responseText)),{ itemName : "#F.posMachineCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allCountry", oXHR.responseText)),{ itemName : "#F.countryCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allCurrency", oXHR.responseText)),{ itemName : "#F.currencyCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allDeliveryType", oXHR.responseText)),{ itemName : "#F.homeDelivery" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentCategory", oXHR.responseText)),{ itemName : "#F.paymentCategory" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentTerm", oXHR.responseText)),{ itemName : "#F.paymentTermCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allInvoiceType", oXHR.responseText)),{ itemName : "#F.invoiceTypeCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allTaxType", oXHR.responseText)),{ itemName : "#F.taxType" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allDiscountType", oXHR.responseText)),{ itemName : "#F.orderDiscountType" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allItemCategory", oXHR.responseText)),{ itemName : "#F.itemCategory" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[1]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[2]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[3]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[4]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[5]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[6]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[7]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[8]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[9]" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentType", oXHR.responseText)),{ itemName : "#form.posPaymentType[10]" });
		vat.item.bindAll();
		doFormAccessControl();
		}
	});
}


function advanceInput(){
	var nItemLine = vat.item.getGridLine();
	
	vat.block.pageSearch(vnB_Detail, {  
		funcSuccessAfter:function(){
		    //var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
			var vDiscountRate = vat.item.getValueByName("#F.discountRate").replace(/^\s+|\s+$/, '');
			var vLineId = vat.item.getGridValueByName("lineId", nItemLine).replace(/^\s+|\s+$/, '');
			var returnData = 
				window.showModalDialog(
					"So_SalesOrder:advanceInput:20100201.page"+
					"?headId=" + vat.item.getValueByName("#F.headId") +
					"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
			        "&priceType=" + vat.item.getValueByName("#F.priceType") + 
			        "&shopCode=" + vat.item.getValueByName("#F.shopCode") +
			        "&customerType=" + vat.item.getValueByName("#F.customerType") +
			        "&vipType=" + vat.item.getValueByName("#F.vipTypeCode") +
			        "&warehouseEmployee=" + vat.item.getValueByName("#F.createdBy") +
				    "&warehouseManager=" + vat.item.getValueByName("#F.warehouseManager") +
			        "&salesDate=" + vat.item.getValueByName("#F.salesOrderDate") +
			        "&warehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") +
			        "&taxType=" + vat.item.getValueByName("#F.taxType") +
			        "&taxRate=" + vat.item.getValueByName("#F.taxRate") +
			        "&discountRate=" + vDiscountRate +
					"&exportExchangeRate=" + vat.item.getValueByName("#F.exportExchangeRate") +
					"&enable=" + vEnable +
			        "&lineId=" + vLineId,"",
					"dialogHeight:600px;dialogWidth:1060px;dialogTop:100px;dialogLeft:100px;status:no;");
				
			vat.block.pageDataFlow(vnB_Detail, "load:stop");
			vat.block.pageRefresh(vnB_Detail,{  
				funcSuccessAfter:function(){changeItemCategory();}});
		}
	});
}

function appendBeforeService(){
	return true;
}

function loadBeforeAjxService(){
	var processString = "process_object_name=soSalesOrderMainService&process_object_method_name=getAJAXPageData" + 
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
	                    "&shopCode=" + vat.item.getValueByName("#F.shopCode") +  
	                    "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") + 
	                    "&taxType=" + vat.item.getValueByName("#F.taxType") +
	                    "&taxRate=" + vat.item.getValueByName("#F.taxRate") +
	                    "&bonusPointAmount=" + vat.item.getValueByName("#F.bonusPointAmount") +
	                    "&discountRate=" + vat.item.getValueByName("#F.discountRate") +
	                    "&vipPromotionCode=" + vat.item.getValueByName("#F.vipPromotionCode") +
	                    "&promotionCode=" + vat.item.getValueByName("#F.promotionCode") +                   
	                    "&warehouseEmployee=" + vat.item.getValueByName("#F.createdBy") +
	                    "&warehouseManager=" + vat.item.getValueByName("#F.warehouseManager") +
	                    "&customerType=" + vat.item.getValueByName("#F.customerType") +
	                    "&vipType=" + vat.item.getValueByName("#F.vipTypeCode") +
	                    "&priceType=" + vat.item.getValueByName("#F.priceType") +
	                    "&salesOrderDate=" + vat.item.getValueByName("#F.salesOrderDate") + 
	                    "&status=" + vat.item.getValueByName("#F.status") + 
	                    "&exportExchangeRate=" + vat.item.getValueByName("#F.exportExchangeRate");
	return processString;
}

function loadSuccessAfter(){

}

function loadPaymentBeforeAjxService(){
	var processString = "process_object_name=soSalesOrderMainService&process_object_method_name=getAJAXPageDataForPayment" + 
	                    "&headId=" +  vat.item.getValueByName("#F.headId") +
	                    "&status=" +  vat.item.getValueByName("#F.headId");
	return processString;											
}

function saveBeforeAjxService() {
	processString = "process_object_name=soSalesOrderMainService"+
					"&process_object_method_name=updateAJAXPageLinesData" + 
					"&headId=" + vat.item.getValueByName("#F.headId") + 
					"&status=" + vat.item.getValueByName("#F.status");
	return processString;
}

function savePaymentBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=soSalesOrderMainService&process_object_method_name=updateAJAXPaymentPageLinesData" + 
					"&headId=" + vat.item.getValueByName("#F.headId") + 
					"&status=" + vat.item.getValueByName("#F.status");
	return processString;
}

function changeRelationData(){
    var statusTmp =  vat.item.getValueByName("#F.status");
    var orderTypeCodeTmp =  vat.item.getValueByName("#F.orderTypeCode");
    var orderNoTmp = vat.item.getValueByName("#F.orderNo");
    var processId = document.forms[0]["#processId"].value;
    if(orderTypeCodeTmp == "SOP"){
	    if(statusTmp == "SAVE" || statusTmp == "UNCONFIRMED"){
	        afterSavePageProcess = "changeRelationData";
		    vat.block.pageDataSave(vnB_Detail);		
	    }
	}else{
	    if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != "" && processId != 0)){
		    afterSavePageProcess = "changeRelationData";
		    vat.block.pageDataSave(vnB_Detail);		
	    }	
	}
}

function saveSuccessAfter() {
    if ("saveHandler" == afterSavePageProcess) {	
        //doActualSaveHandler();
    } else if ("submitHandler" == afterSavePageProcess) {
		//doActualSubmitHandler();
	} else if ("submitBgHandler" == afterSavePageProcess) {
	    //doActualSubmitBgHandler();
    } else if ("voidHandler" == afterSavePageProcess) {
		//doActualVoidHandler();
	} else if ("copyHandler" == afterSavePageProcess) {
		//doActualCopyHandler();
	} else if ("executeExport" == afterSavePageProcess) {
		//exportFormData();
	} else if("executeExtendItem" == afterSavePageProcess){
		execExtendItemInfo();

		//changeItemData(2);
	} else if ("totalCount" == afterSavePageProcess) {
		countTotal();
    }else if ("changeRelationData" == afterSavePageProcess) {
        var processString = "process_object_name=soSalesOrderMainService&process_object_method_name=updateItemRelationData" +
        			"&status=" + vat.item.getValueByName("#F.status")+ 
	                "&headId=" + vat.item.getValueByName("#F.headId") +
	                "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
	                "&shopCode=" + vat.item.getValueByName("#F.shopCode")+  
	                "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") + 
	                "&taxType=" + vat.item.getValueByName("#F.taxType") +
	                "&taxRate=" + vat.item.getValueByName("#F.taxRate") +
	                "&discountRate=" + vat.item.getValueByName("#F.discountRate") +
	                "&vipPromotionCode=" + vat.item.getValueByName("#F.vipPromotionCode") +
	                "&promotionCode=" + vat.item.getValueByName("#F.promotionCode") +
	                "&warehouseEmployee=" + vat.item.getValueByName("#F.createdBy") +
	                "&warehouseManager=" + vat.item.getValueByName("#F.warehouseManager") +
	                "&customerType=" + vat.item.getValueByName("#F.customerType") +
	                "&vipType=" + vat.item.getValueByName("#F.vipTypeCode") +
	                "&priceType=" + vat.item.getValueByName("#F.priceType") +
	                "&salesDate=" + vat.item.getValueByName("#F.salesOrderDate") +
	                "&exportExchangeRate=" + vat.item.getValueByName("#F.exportExchangeRate");
	    vat.ajax.startRequest(processString, function () {
		    if (vat.ajax.handleState()) {
			    vat.block.pageRefresh(vnB_Detail);
		     }
		});	
    }
	afterSavePageProcess = "";
}

function doExtendItemInfo() {
    if (confirm("是否執行取報單程序?")) {      
        afterSavePageProcess = "executeExtendItem"; 
        vat.block.pageSave(vnB_Detail);



    }
}


function checkAmount() {
//撈出所有該銷售單的明細
//抓商品數量
//變更銷售金額
//重整頁面
		//alert("1");
		vat.ajax.XHRequest({
			post:"process_object_name=soSalesOrderMainService&process_object_method_name=executeCheckAmountAfterExtend" + 
	                "&headId=" + vat.item.getValueByName("#F.headId") + 
	                "&status=" + vat.item.getValueByName("#F.status") +
	                "&brandCode=" + vat.item.getValueByName("#F.brandCode"),
            asyn:false,                      
			find: function change(oXHR){

           	}
		});
}

function execExtendItemInfo(){
    vat.bean().vatBeanOther.processObjectName = "soSalesOrderMainService";
    vat.bean().vatBeanOther.searchMethodName = "findSoSalesOrderHeadById";
    vat.bean().vatBeanOther.tableType = "SO_SALES_ORDER";
    vat.bean().vatBeanOther.searchKey = vat.item.getValueByName("#F.headId");
    vat.bean().vatBeanOther.subEntityBeanName = "soSalesOrderItems";
    vat.bean().vatBeanOther.itemFieldName = "itemCode";
    vat.bean().vatBeanOther.warehouseCodeFieldName = "warehouseCode";   
    vat.bean().vatBeanOther.declTypeFieldName = "importDeclType";
    vat.bean().vatBeanOther.declNoFieldName = "importDeclNo";
    vat.bean().vatBeanOther.declSeqFieldName = "importDeclSeq";
    vat.bean().vatBeanOther.declDateFieldName = "importDeclDate";
    vat.bean().vatBeanOther.lotFieldName = "lotNo";
    vat.bean().vatBeanOther.qtyFieldName = "quantity";
    vat.bean().vatBeanOther.perUnitAmountFieldName = "perUnitAmount";
    
    vat.block.submit(function(){return "process_object_name=appExtendItemInfoService"+
            "&process_object_method_name=executeExtendItem";}, {other:true, funcSuccess: function() {
            		checkAmount();
            		vat.block.pageRefresh(vnB_Detail);}});
}

/*
	顯示合計的頁面
*/
function showTotalCountPage() {
	afterSavePageProcess = "totalCount";
	doPagaDataSave();
}

function countTotal(){
		vat.ajax.XHRequest({
			post:"process_object_name=soSalesOrderMainService&process_object_method_name=executeCountTotalAmount" + 
	                "&headId=" + vat.item.getValueByName("#F.headId") + 
	                "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
	                "&shopCode=" + vat.item.getValueByName("#F.shopCode") +  
	                "&priceType=" + vat.item.getValueByName("#F.priceType") +     
	                "&customerCode=" + vat.item.getValueByName("#F.customerCode_var") +
	                "&searchCustomerType=" + vat.item.getValueByName("#F.searchCustomerType") +        
	                "&warehouseEmployee=" + vat.item.getValueByName("#F.createdBy") +
	                "&warehouseManager=" + vat.item.getValueByName("#F.warehouseManager") +        
	                "&salesDate=" + vat.item.getValueByName("#F.salesOrderDate") + 
	                "&status=" + vat.item.getValueByName("#F.status") +
	                "&taxType=" + vat.item.getValueByName("#F.taxType") +
	                "&taxRate=" + vat.item.getValueByName("#F.taxRate") +
	                "&exportCommissionRate=" + vat.item.getValueByName("#F.exportCommissionRate") +
	                "&exportExchangeRate=" + vat.item.getValueByName("#F.exportExchangeRate")+
	                "&exportExpense=" + vat.item.getValueByName("#F.exportExpense")+
	                "&totalBonusPointAmount=" + vat.item.getValueByName("#F.totalBonusPointAmount"),
            asyn:false,                      
			find: function change(oXHR){
				var branchCode = vat.bean("branchCode");
				vat.item.setValueByName("#F.totalOriginalSalesAmount"	,vat.ajax.getValue("TotalOriginalSalesAmount", oXHR.responseText));
				vat.item.setValueByName("#F.totalDeductionAmount"		,vat.ajax.getValue("TotalDeductionAmount", oXHR.responseText));
				vat.item.setValueByName("#F.taxAmount"					,vat.ajax.getValue("TaxAmount", oXHR.responseText));
				vat.item.setValueByName("#F.totalOtherExpense"			,vat.ajax.getValue("TotalOtherExpense", oXHR.responseText));
				vat.item.setValueByName("#F.expenseLocalAmount"			,vat.ajax.getValue("ExpenseLocalAmount", oXHR.responseText));
				vat.item.setValueByName("#F.totalActualSalesAmount"		,vat.ajax.getValue("TotalActualSalesAmount", oXHR.responseText));
				vat.item.setValueByName("#F.totalNoneTaxSalesAmount"	,vat.ajax.getValue("TotalNoneTaxSalesAmount", oXHR.responseText));
				vat.item.setValueByName("#F.totalAmount"				,vat.ajax.getValue("TotalAmount", oXHR.responseText));
				if(branchCode == "2"){
					vat.item.setValueByName("#F.originalTotalFrnSalesAmt"	,vat.ajax.getValue("OriginalTotalFrnSalesAmt", oXHR.responseText));
					//vat.item.setValueByName("#F.totalDeductionFrnAmount"	,vat.ajax.getValue("TotalDeductionFrnAmount", oXHR.responseText));
					vat.item.setValueByName("#F.taxFrnAmount"				,vat.ajax.getValue("TaxFrnAmount", oXHR.responseText));
					vat.item.setValueByName("#F.totalOtherFrnExpense"		,vat.ajax.getValue("TotalOtherFrnExpense", oXHR.responseText));
					vat.item.setValueByName("#F.expenseForeignAmount"		,vat.ajax.getValue("ExpenseForeignAmount", oXHR.responseText));
					vat.item.setValueByName("#F.actualTotalFrnSalesAmt"		,vat.ajax.getValue("ActualTotalFrnSalesAmt", oXHR.responseText));
					vat.item.setValueByName("#F.totalNoneTaxFrnSalesAmount"	,vat.ajax.getValue("TotalNoneTaxFrnSalesAmount", oXHR.responseText));
					vat.item.setValueByName("#F.totalForeignAmount"				,vat.ajax.getValue("TotalForeignAmount", oXHR.responseText));
				}
				vat.item.setValueByName("#F.totalItemQuantity"			,vat.ajax.getValue("TotalItemQuantity", oXHR.responseText));
		        vat.item.setValueByName("#F.totalBonusPointAmount"		,vat.ajax.getValue("totalBonusPointAmount", oXHR.responseText));
           	}
		});
}

function doPagaDataSave(){
   		vat.block.pageSearch(vnB_Detail);
   		vat.block.pageSearch(vnB_Payment);
}


// 供應商picker 回來執行
function doAfterPickerCustomer(){
	if(typeof vat.bean().vatBeanPicker.customerResult != "undefined"){
		vat.item.setValueByName("#F.searchCustomerType", "customerCode");
    	vat.item.setValueByName("#F.customerCode_var", vat.bean().vatBeanPicker.customerResult[0].customerCode); 
		changeCustomerCode();
	}
}

function doAfterPickerEmployee(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.superintendentCode", vat.bean().vatBeanPicker.result[0].employeeCode);
    	vat.item.setValueByName("#F.superintendentName", vat.bean().vatBeanPicker.result[0].chineseName);
	}
}

function doAfterPickerCasher(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.casherCode", vat.bean().vatBeanPicker.result[0].employeeCode);
    	vat.item.setValueByName("#F.casherName", vat.bean().vatBeanPicker.result[0].chineseName);
	}
}

function changeCustomerCode(){
	var brandCode = vat.item.getValueByName("#F.brandCode");
    var customerCode_var = vat.item.getValueByName("#F.customerCode_var").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.customerCode_var", customerCode_var);
    if(customerCode_var != ""){
        vat.ajax.XHRequest({
           post:"process_object_name=buCustomerWithAddressViewService"+
                    "&process_object_method_name=findCustomerByTypeForAJAX"+
                    "&brandCode=" + brandCode + 
                    "&customerCode=" + customerCode_var +
                    "&searchCustomerType=" + vat.item.getValueByName("#F.searchCustomerType") +
                    "&isEnable=",
           find: function changeCustomerCodeRequestSuccess(oXHR){
				vat.item.setValueByName("#F.customerCode", vat.ajax.getValue("CustomerCode", oXHR.responseText));
           		vat.item.setValueByName("#F.contactPerson", vat.ajax.getValue("ContactPerson", oXHR.responseText));
           		vat.item.setValueByName("#F.contactTel", vat.ajax.getValue("ContactTel", oXHR.responseText));
           		vat.item.setValueByName("#F.receiver", vat.ajax.getValue("Receiver", oXHR.responseText));
           		vat.item.setValueByName("#F.countryCode", vat.ajax.getValue("CountryCode", oXHR.responseText));
           		vat.item.setValueByName("#F.currencyCode", vat.ajax.getValue("CurrencyCode", oXHR.responseText));
           		changeExchangeRate();
           		vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("PaymentTermCode", oXHR.responseText));
           		vat.item.setValueByName("#F.invoiceCity", vat.ajax.getValue("InvoiceCity", oXHR.responseText));
           		vat.item.setValueByName("#F.invoiceArea", vat.ajax.getValue("InvoiceArea", oXHR.responseText));
           		vat.item.setValueByName("#F.invoiceZipCode", vat.ajax.getValue("InvoiceZipCode", oXHR.responseText));
           		vat.item.setValueByName("#F.invoiceAddress", vat.ajax.getValue("InvoiceAddress", oXHR.responseText));
           		vat.item.setValueByName("#F.shipCity", vat.ajax.getValue("ShipCity", oXHR.responseText));
           		vat.item.setValueByName("#F.shipArea", vat.ajax.getValue("ShipArea", oXHR.responseText));
           		vat.item.setValueByName("#F.shipZipCode", vat.ajax.getValue("ShipZipCode", oXHR.responseText));
           		vat.item.setValueByName("#F.shipAddress", vat.ajax.getValue("ShipAddress", oXHR.responseText));
           		vat.item.setValueByName("#F.invoiceTypeCode", vat.ajax.getValue("InvoiceTypeCode", oXHR.responseText));
           		vat.item.setValueByName("#F.guiCode", vat.ajax.getValue("GuiCode", oXHR.responseText));
           		vat.item.setValueByName("#F.customerType", vat.ajax.getValue("CustomerType", oXHR.responseText));
           		vat.item.setValueByName("#F.customerName", vat.ajax.getValue("CustomerName", oXHR.responseText));
           		vat.item.setValueByName("#F.vipTypeCode", vat.ajax.getValue("VipType", oXHR.responseText));
           		vat.item.setValueByName("#F.taxType", vat.ajax.getValue("TaxType", oXHR.responseText));
           		vat.item.setValueByName("#F.taxRate", vat.ajax.getValue("TaxRate", oXHR.responseText));
           		
			if(vat.item.getValueByName("#F.orderTypeCode") != "SOP"){
               	vat.item.setValueByName("#F.vipPromotionCode", vat.ajax.getValue("VipPromotionCode", oXHR.responseText));
            }else{
            	vat.item.setValueByName("#F.vipPromotionCode", "");
            }           
				if(vat.item.getValueByName("#F.orderTypeCode") == "SOE"){
					vat.item.setValueByName("#F.invoiceTypeCode", "2");
				}
               changePromotionCode(); 
           }});  
    }else{
        vat.item.setValueByName("#F.customerCode", "");
  		vat.item.setValueByName("#F.contactPerson", "");
  		vat.item.setValueByName("#F.contactTel", "");
  		vat.item.setValueByName("#F.receiver", "");
  		vat.item.setValueByName("#F.countryCode", "");
  		vat.item.setValueByName("#F.currencyCode", "NTD");
  		vat.item.setValueByName("#F.paymentTermCode", "");
  		vat.item.setValueByName("#F.invoiceCity", "");
  		vat.item.setValueByName("#F.invoiceArea", "");
  		vat.item.setValueByName("#F.invoiceZipCode", "");
  		vat.item.setValueByName("#F.invoiceAddress", "");
  		vat.item.setValueByName("#F.shipCity", "");
  		vat.item.setValueByName("#F.shipArea", "");
  		vat.item.setValueByName("#F.shipZipCode", "");
  		vat.item.setValueByName("#F.shipAddress", "");
  		vat.item.setValueByName("#F.invoiceTypeCode", "");
  		vat.item.setValueByName("#F.guiCode", "");
  		vat.item.setValueByName("#F.customerType", "");
  		vat.item.setValueByName("#F.customerName", "");
  		vat.item.setValueByName("#F.vipTypeCode", "");
  		if("T2" == brandCode){
  			vat.item.setValueByName("#F.taxType", "1");
  			vat.item.setValueByName("#F.taxRate", "0");
  		}else{
  			vat.item.setValueByName("#F.taxType", "3");
  			vat.item.setValueByName("#F.taxRate", "5");
  		}
        changePromotionCode();     
    }
}

function changePromotionCode(){
	var promotionCode = vat.item.getValueByName("#F.promotionCode").replace(/^\s+|\s+$/, '');
    vat.item.setValueByName("#F.promotionCode", promotionCode);
    if(promotionCode != ""){
        vat.ajax.XHRequest(
        {
           post:"process_object_name=imPromotionViewService"+
                    "&process_object_method_name=findPromotionCodeByPropertyForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
                    "&promotionCode=" + promotionCode +
                    "&priceType=" + vat.item.getValueByName("#F.priceType") + 
                    "&shopCode=" + vat.item.getValueByName("#F.shopCode") +
                    "&customerType=" + vat.item.getValueByName("#F.customerType") +
                    "&vipType=" + vat.item.getValueByName("#F.vipTypeCode") +
                    "&salesDate=" + vat.item.getValueByName("#F.salesOrderDate"),                      
           find: function changePromotionCodeRequestSuccess(oXHR){
 				vat.item.setValueByName("#F.promotionCode", vat.ajax.getValue("PromotionCode", oXHR.responseText));
 				vat.item.setValueByName("#F.promotionName", vat.ajax.getValue("PromotionName", oXHR.responseText));          		 
           }
        });
    }else{
         vat.item.setValueByName("#F.promotionName", "");
    }
}

function changeShopCode(){
	var shopCode = vat.item.getValueByName("#F.shopCode").replace(/^\s+|\s+$/, '');
    vat.item.setValueByName("#F.shopCode", shopCode);
    vat.item.setValueByName("#F.defaultWarehouseCode", shopCode);
    var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
       vat.ajax.XHRequest(
       {
           post:"process_object_name=soSalesOrderMainService"+
                    "&process_object_method_name=getShopMachineForAJAX"+
                    "&shopCode=" + shopCode+
                    "&defaultWarehouseCode=" + defaultWarehouseCode,
           find: function changeShopCodeRequestSuccess(oXHR){ 
               	vat.item.setValueByName("#F.defaultWarehouseCode", vat.ajax.getValue("DefaultWarehouseCode", oXHR.responseText));
               	vat.item.setValueByName("#F.warehouseManager", vat.ajax.getValue("WarehouseManager", oXHR.responseText));
               	vat.item.SelectBind(eval(vat.ajax.getValue("allshopMachine", oXHR.responseText)),{ itemName : "#F.posMachineCode" });
           }   
       });
}

function changeTaxRate(){
	var taxType = vat.item.getValueByName("#F.taxType");
    if(taxType == "1" || taxType == "2"){
    	vat.item.setValueByName("#F.taxRate", "0");
    }else if(taxType == "3"){
        vat.item.setValueByName("#F.taxRate", "5");
    }
}

function changeDefaultWarehouseCode(){
	var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode").replace(/^\s+|\s+$/, '');
    vat.item.setValueByName("#F.defaultWarehouseCode", defaultWarehouseCode);
    if(defaultWarehouseCode !== ""){
        vat.ajax.XHRequest(
        {
           post:"process_object_name=imWarehouseService"+
                    "&process_object_method_name=findByIdForAJAX"+
                    "&warehouseCode=" + defaultWarehouseCode,
           find: function changeDefaultWarehouseCodeRequestSuccess(oXHR){ 
               vat.item.setValueByName("#F.warehouseManager", vat.ajax.getValue("WarehouseManager", oXHR.responseText));
               if(vat.item.getValueByName("#F.warehouseManager") == ""){
                   alert("庫別：" + defaultWarehouseCode + "的倉管人員為空值！");
               }
           }
        });
    }else{
        vat.item.setValueByName("#F.warehouseManager", "");
    }
}

function changeSuperintendent(){
    vat.item.setValueByName("#F.superintendentCode", vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, ''));
    if(vat.item.getValueByName("#F.superintendentCode") !== ""){
		vat.ajax.XHRequest(
       	{
			post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vat.item.getValueByName("#F.superintendentCode"),
			find: function changeSuperintendentRequestSuccess(oXHR){
           		vat.item.setValueByName("#F.superintendentCode", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
           		vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("EmployeeName", oXHR.responseText)); 
			}   
		});
	}else{
        vat.item.setValueByName("#F.superintendentName", "");
	}
}

function changeExchangeRate(){
    vat.ajax.XHRequest(
       {
           post:"process_object_name=soSalesOrderMainService"+
                    "&process_object_method_name=getExchangeRate"+
                    "&currencyCode=" + vat.item.getValueByName("#F.currencyCode")+
                    "&currencyDate=" + vat.item.getValueByName("#F.salesOrderDate"),
           find: function changeExchangeRateRequestSuccess(oXHR){
				vat.item.setValueByName("#F.exportExchangeRate", vat.ajax.getValue("ExportExchangeRate", oXHR.responseText));
           }   
       });
}

function changeCommissionRate(){
    var commissionRate = vat.item.getValueByName("#F.exportCommissionRate").replace(/^\s+|\s+$/, '');
    if(isNaN(commissionRate) || parseInt(commissionRate > 100) < 0){
    	vat.item.setValueByName("#F.exportCommissionRate", 0.0);
    }else if(commissionRate == ""){
        vat.item.setValueByName("#F.exportCommissionRate", 0.0);
    }
}

function changeDiscountRate(){
    var discountRate = vat.item.getValueByName("#F.discountRate").replace(/^\s+|\s+$/, '');
    if(isNaN(discountRate)){
        vat.item.setValueByName("#F.discountRate", 100.0);
    }else if(discountRate == "" || parseInt(discountRate > 100)){
        vat.item.setValueByName("#F.discountRate", 100.0);
    }
}

function changeCasherCode(){
	vat.item.setValueByName("#F.casherCode", vat.item.getValueByName("#F.casherCode").replace(/^\s+|\s+$/, ''));
    if(vat.item.getValueByName("#F.casherCode") !== ""){
		vat.ajax.XHRequest(
		{
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vat.item.getValueByName("#F.casherCode"),
           find: function changeCasherCodeRequestSuccess(oXHR){
           		vat.item.setValueByName("#F.casherCode", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
           		vat.item.setValueByName("#F.casherName", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
           }   
		});
    }else{
		vat.item.setValueByName("#F.casherName", "");
    }
}

function changeItemData(actionId) {
	
	var vOriginalUnitPrice = parseFloat(vat.item.getGridValueByName("originalUnitPrice", nItemLine));
	if(isNaN(vOriginalUnitPrice))
		vOriginalUnitPrice = 0;
	var vExportExchangeRate = vat.item.getValueByName("#F.exportExchangeRate");
	var vActualForeignUnitPrice = parseFloat(vat.item.getGridValueByName("actualForeignUnitPrice", nItemLine));
	if(isNaN(vActualForeignUnitPrice))
		vActualForeignUnitPrice = 0;
	var vActualUnitPrice = vActualForeignUnitPrice * vExportExchangeRate;
	if(isNaN(vActualUnitPrice))
		vActualUnitPrice = 0;
	var vDeductionAmount = parseFloat(vat.item.getGridValueByName("deductionAmount", nItemLine));
	if(isNaN(vDeductionAmount))
		vDeductionAmount = 0;
	//折扣率帶預設的，如果不是第一次就抓LINE的
	var vDiscountRate = vat.item.getValueByName("#F.discountRate");
	if(actionId == "2"){
		vDiscountRate = vat.item.getGridValueByName("discountRate", nItemLine);
		if(isNaN(vDiscountRate) || vDiscountRate > 100 || vDiscountRate < 0)
			vDiscountRate = 100;
	}else if(actionId == "3"){
		vDiscountRate = (vActualUnitPrice+vDeductionAmount)*100/vOriginalUnitPrice;
		if(isNaN(vDiscountRate) || vDiscountRate > 100 || vDiscountRate < 0)
			vDiscountRate = 100;
		vDiscountRate = Math.round(vDiscountRate*100)/100;
	}
	vat.item.setGridValueByName("discountRate", nItemLine, vDiscountRate);
	var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
	var warehouseManager = vat.item.getValueByName("#F.warehouseManager");
    var nItemLine = vat.item.getGridLine();
    var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vQuantity = vat.item.getGridValueByName("quantity", nItemLine).replace(/^\s+|\s+$/, '');
	var vPromotionCode = vat.item.getGridValueByName("promotionCode", nItemLine).replace(/^\s+|\s+$/, '');
	var vVipPromotionCode = vat.item.getGridValueByName("vipPromotionCode", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxType = vat.item.getValueByName("#F.taxType").replace(/^\s+|\s+$/, '');
	var vTaxRate = vat.item.getValueByName("#F.taxRate").replace(/^\s+|\s+$/, '');
	vat.item.setGridValueByName("itemCode", nItemLine, vItemCode);
	vat.item.setGridValueByName("warehouseCode", nItemLine, defaultWarehouseCode);
    if(defaultWarehouseCode != ""){
        if(warehouseManager == ""){
            alert("主檔頁籤庫別：" + defaultWarehouseCode + "的倉管人員為空值！");
        }else{
            if(isNaN(vQuantity)){
               alert("明細資料頁籤中第" + nItemLine + "項明細的數量欄位必須為數值！");
            }else{
               vat.ajax.XHRequest(
               {
                   post:"process_object_name=soSalesOrderMainService" +
                        "&process_object_method_name=getAJAXItemData" +
                        "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
                        "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
                        "&priceType=" + vat.item.getValueByName("#F.priceType") + 
                        "&shopCode=" + vat.item.getValueByName("#F.shopCode") +
                        "&customerType=" + vat.item.getValueByName("#F.customerType") +
                        "&vipType=" + vat.item.getValueByName("#F.vipTypeCode") +
                        "&itemIndexNo=" + nItemLine +
                        "&itemCode=" + vItemCode +
                        "&warehouseCode=" + defaultWarehouseCode +
                        "&quantity=" + vQuantity +
                        "&deductionAmount=" + vDeductionAmount +
                        "&discountRate=" + vDiscountRate +
                        "&promotionCode=" + vPromotionCode +
                        "&vipPromotionCode=" + vVipPromotionCode +                         
                        "&warehouseManager=" + warehouseManager +
                        "&warehouseEmployee=" + vat.item.getValueByName("#F.createdBy") +
                        "&originalUnitPrice=" + vOriginalUnitPrice +
                        "&salesDate=" + vat.item.getValueByName("#F.salesOrderDate") +
                        "&taxType=" + vTaxType +
                        "&taxRate=" + vTaxRate +
                        "&exportExchangeRate=" + vExportExchangeRate +
                        "&actualForeignUnitPrice=" + vActualForeignUnitPrice +
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
						vat.item.setGridValueByName("discount", nItemLine, vat.ajax.getValue("Discount", oXHR.responseText));
						vat.item.setGridValueByName("vipPromotionCode", nItemLine, vat.ajax.getValue("VipPromotionCode", oXHR.responseText));
						vat.item.setGridValueByName("vipDiscountType", nItemLine, vat.ajax.getValue("VipDiscountType", oXHR.responseText));
						vat.item.setGridValueByName("vipDiscount", nItemLine, vat.ajax.getValue("VipDiscount", oXHR.responseText));     
						vat.item.setGridValueByName("isServiceItem", nItemLine, vat.ajax.getValue("IsServiceItem", oXHR.responseText));
						vat.item.setGridValueByName("taxAmount", nItemLine, vat.ajax.getValue("TaxAmount", oXHR.responseText));
						vat.item.setGridValueByName("originalForeignUnitPrice", nItemLine, vat.ajax.getValue("OriginalForeignUnitPrice", oXHR.responseText));
						vat.item.setGridValueByName("actualForeignUnitPrice", nItemLine, vat.ajax.getValue("ActualForeignUnitPrice", oXHR.responseText));
						vat.item.setGridValueByName("originalForeignSalesAmt", nItemLine, vat.ajax.getValue("OriginalForeignSalesAmt", oXHR.responseText));
						vat.item.setGridValueByName("actualForeignSalesAmt", nItemLine, vat.ajax.getValue("ActualForeignSalesAmt", oXHR.responseText));
						vat.item.setGridValueByName("deductionForeignAmount", nItemLine, vat.ajax.getValue("DeductionForeignAmount", oXHR.responseText));
						vat.item.setGridValueByName("importCost", nItemLine, vat.ajax.getValue("ImportCost", oXHR.responseText));
						vat.item.setGridValueByName("importCurrencyCode", nItemLine, vat.ajax.getValue("ImportCurrencyCode", oXHR.responseText));
						vat.item.setGridValueByName("itemDiscountType", nItemLine, vat.ajax.getValue("ItemDiscountType", oXHR.responseText));
						vat.item.setGridValueByName("allowMinusStock", nItemLine, vat.ajax.getValue("AllowMinusStock", oXHR.responseText));
						if(actionId == "1")
							vat.form.item.setFocus(vat.item.nameMake("actualForeignUnitPrice", nItemLine));
						if(nItemLine == "1")
							changeItemCategory();
							
                   }
               });          
            }
        }
    }else{
	    alert("請先選擇主檔頁籤的庫別！");
	}
}

function changeDeclNo(pickerLine){
	//var nItemLine = vat.item.getGridLine();
	var nItemLine = typeof pickerLine === "undefined" ? vat.item.getGridLine() : pickerLine ;
	
    var vDeclNo = vat.item.getGridValueByName("importDeclNo", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.getGridValueByName("importDeclNo", nItemLine, vDeclNo);
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=soSalesOrderMainService"+
                    "&process_object_method_name=executeFindCM"+
                    "&exportDeclNo=" + vDeclNo,
        find: function getOriginalDeliverySuccess(oXHR){
        	vat.item.setGridValueByName("importDeclDate" ,nItemLine ,vat.ajax.getValue("ExportDeclDate", oXHR.responseText));
        	vat.item.setGridValueByName("importDeclType" ,nItemLine ,vat.ajax.getValue("ExportDeclType", oXHR.responseText));
        	vat.item.setGridValueByName("lotNo", nItemLine, "000000000000");
       }   
   });
}




function changePaymentExchangeRate() {
    var nItemLine = vat.item.getGridLine();
	var vPosPaymentType = vat.item.getGridValueByName("posPaymentType", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vForeignCurrencyCode = vat.item.getGridValueByName("foreignCurrencyCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vForeignAmount = vat.item.getGridValueByName("foreignAmount", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	if(!vForeignAmount >0){
		vForeignAmount = 0;
	}
    vat.ajax.XHRequest({
		post:"process_object_name=soSalesOrderMainService"+
                 "&process_object_method_name=getPostPayment"+
                 "&posPaymentType=" + vPosPaymentType,
        find: function changeExchangeRateRequestSuccess(oXHR){ 
        		vat.item.setGridValueByName("foreignCurrencyCode", nItemLine, vat.ajax.getValue("PaymentType", oXHR.responseText));
       		    vat.ajax.XHRequest({
				post:"process_object_name=soSalesOrderMainService"+
                	"&process_object_method_name=getExchangeRate"+
                	"&currencyCode=" + vat.ajax.getValue("PaymentType", oXHR.responseText),
       			find: function changeExchangeRateRequestSuccess(oXHR){
       				vat.item.setGridValueByName("exchangeRate", nItemLine, vat.ajax.getValue("ExportExchangeRate", oXHR.responseText));
       				vat.item.setGridValueByName("localAmount", nItemLine,  vat.ajax.getValue("ExportExchangeRate", oXHR.responseText) * vForeignAmount);
       			}});
        }});
}

function changePayment(){
	var nItemLine = vat.item.getGridLine();
	var vForeignAmount = parseInt(vat.item.getGridValueByName("foreignAmount", nItemLine),10);
	var vExchangeRate = vat.item.getGridValueByName("exchangeRate", nItemLine);
	if(!vForeignAmount > 0){
		vForeignAmount = 0;
	}
	vat.item.setGridValueByName("localAmount", nItemLine,  parseInt(vForeignAmount*vExchangeRate));
}

function changeItemCategory(){
	var vItemCode = vat.item.getGridValueByName("itemCode", 1);
	var brandCode = document.forms[0]["#loginBrandCode" ].value
		if(brandCode == "T2"){
			vat.ajax.XHRequest({ 
				post:"process_object_name=soSalesOrderMainService&process_object_method_name=getItemCategory" + 
		                "&headId=" + vat.item.getValueByName("#F.headId")+
		                "&brandCode=" + brandCode+
		                "&itemCode=" + vItemCode,
	            asyn:false,                      
				find: function change(oXHR){
				//alert("ItemCategory = " + vat.ajax.getValue("ItemCategory", oXHR.responseText));
				if(vat.ajax.getValue("ItemCategory", oXHR.responseText) != "")
					vat.item.setValueByName("#F.itemCategory"			,vat.ajax.getValue("ItemCategory", oXHR.responseText));
	           	}
			});
		}
}

function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=SO_SALES_ORDER" +
		"&levelType=ERROR" +
        "&processObjectName=soSalesOrderMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function doSubmit(formAction){
	var alertMessage ="是否確定?";
	if("SIGNING" == formAction){
		alertMessage = "是否確定送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("UNCONFIRMED" == formAction){
	 	alertMessage = "是否確定反確認?";
	}
	
	if(confirm(alertMessage)){
		vat.block.pageDataSave(vnB_Payment, {  
 			funcSuccess:function(){
				vat.block.pageDataSave(vnB_Detail, {  
					funcSuccess:function(){
					
	       			initialVatBeanOther(formAction);
					
					if("SUBMIT_BG" == formAction){
						vat.block.submit(function(){return "process_object_name=soSalesOrderMainAction"+
						"&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
					}else{
						vat.block.submit(function(){return "process_object_name=soSalesOrderMainAction"+
					    "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
					}
	      			}   
				});
			}   
		});
	}
}

function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=soSalesOrderMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

function doExport(){
	var type = "SO_ITEM";
	var brandCode = vat.item.getValueByName("#F.brandCode");
	if("T2" == brandCode || "T6CO" == brandCode)
		type = "SO_ITEM2"
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=" + type +  
              "&fileType=XLS" + 
              "&processObjectName=soSalesOrderMainService" + 
              "&processObjectMethodName=findSoSalesOrderHeadById" + 
              "&gridFieldName=soSalesOrderItems" + 
              "&arguments=" + vat.item.getValueByName("#F.headId") + 
              "&parameterTypes=LONG";
    var width = "200";
    var height = "30";
    vat.block.pageDataSave(vnB_Detail, {  
 			funcSuccess:function(){
    		window.open(url, '銷貨單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
    		}
    });
}

function doImport(){
	var type = "SO_ITEM";
	var brandCode = vat.item.getValueByName("#F.brandCode");
	if("T2" == brandCode || "T6CO" == brandCode)
		type = "SO_ITEM2"
	var suffix = 
		"&importBeanName=" + type + 
		"&importFileType=XLS" +
        "&processObjectName=soSalesOrderMainService" + 
        "&processObjectMethodName=executeImportItems" +
        "&arguments=" + vat.item.getValueByName("#F.headId") + "{$}" + vat.item.getValueByName("#F.vipPromotionCode") + "{$}" + 
                      vat.item.getValueByName("#F.taxType") + "{$}" + vat.item.getValueByName("#F.taxRate") + "{$}" + 
                      vat.item.getValueByName("#F.exportExchangeRate") + "{$}" + vat.item.getValueByName("#F.defaultWarehouseCode") + 
        "&parameterTypes=LONG{$}STRING{$}STRING{$}DOUBLE{$}DOUBLE{$}STRING" +
        "&blockId=3"
	return suffix;
}

function afterImportSuccess(){
	//alert("afterImportSuccess");
	refreshHeadData();
}

function refreshHeadData(){	
	changeItemCategory();
}	

// 傳參數
function doPassData(X, id){
	var suffix = "";
	switch(id){
		case "orderTypeCode":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
			var incharge = document.forms[0]["#incharge"].value;    
			suffix += "&orderTypeCode="+escape(orderTypeCode)+
			"&incharge="+escape(incharge); 
			break;
		case "watchSerialNo":
			var nItemLine = vat.item.getGridLine(X);
			var itemCode = vat.item.getGridValueByName( "itemCode", nItemLine);
			suffix += "&itemCode="+escape(itemCode); 
			break;
		case "detailLine":
			var nItemLine = vat.item.getGridLine(X);
			var itemCode = vat.item.getGridValueByName( "itemCode", nItemLine);
			var warehouseCode = vat.item.getGridValueByName( "warehouseCode", nItemLine);
			suffix += "&customsItemCode="+escape(itemCode)+
					"&warehouseCode="+escape(warehouseCode) +
		            "&isAutoLoad=Y";
			break;
	}
	return suffix;
}

function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
		initialVatBeanOther();
	    if( vsMaxSize == 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
		//doFormAccessControl();
	}
}

function doAfterPickerWatchSerialNo(X){
	if(vat.bean().vatBeanPicker.serialResult !== null){
		var nItemLine = vat.item.getGridLine(X);
		vat.item.setGridValueByName( "watchSerialNo", nItemLine, vat.bean().vatBeanPicker.serialResult[0].serial);
	}
}

//由picker取到報單資料後，再取得報單類型及報單日期
function doAfterPickerImportDeclNo(X){
	if(vat.bean().vatBeanPicker.cmOnHandResult !== null){
		var nItemLine = vat.item.getGridLine(X);
		vat.item.setGridValueByName("importDeclNo", nItemLine, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationNo"]); 
    	vat.item.setGridValueByName("importDeclSeq", nItemLine, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationSeq"]);

		changeDeclNo(nItemLine);
	}	
}

function openReportWindow(){ 

	refreshForm(vat.item.getValueByName("#F.headId"));
	
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    vat.bean().vatBeanOther.orderNo = vat.item.getValueByName("#F.orderNo");
    vat.bean().vatBeanOther.displayAmt = vat.item.getValueByName("#F.total");
    vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId")
	vat.block.submit(
		function(){return "process_object_name=imDeliveryMainService"+
					"&process_object_method_name=getReportConfig";},{other:true,
                 			funcSuccess:function(){
					eval(vat.bean().vatBeanOther.reportUrl);
		}}
	); 
}

//新增資料
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
    	refreshForm("");
	 }
}

// 離開按鈕按下
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 上一筆
function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 下一筆
function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 最後一筆
function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"].value = code;
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = ""; 
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=soSalesOrderMainAction&process_object_method_name=performInitial";			
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll();
     			closeTotal();
     	}});
    
}

function doFormAccessControl(){
	var Vstatus = vat.item.getValueByName("#F.status");
	var VprocessId = document.forms[0]["#processId"].value;
	var VorderTypeCode = document.forms[0]["#orderTypeCode"     ].value;
	var VorderNo = vat.item.getValueByName("#F.orderNo");
	var brandCode = vat.item.getValueByName("#F.brandCode");
	
	vat.item.setStyleByName("#B.new"		, "display", "inline");
	vat.item.setStyleByName("#B.search"		, "display", "inline");
	vat.item.setStyleByName("#B.copy"		, "display", "none");
	vat.item.setStyleByName("#B.submit"		, "display", "inline");
	vat.item.setStyleByName("#B.save"		, "display", "inline");
	vat.item.setStyleByName("#B.void"		, "display", "none");
	vat.item.setStyleByName("#B.print"		, "display", "inline");
	vat.item.setStyleByName("#B.uncomfirm"	, "display", "none");
	vat.item.setStyleByName("#B.submitBG"	, "display", "inline");
	vat.item.setStyleByName("#B.message"	, "display", "inline");
	vat.item.setStyleByName("#B.export"		, "display", "inline");
	vat.item.setStyleByName("#B.import"		, "display", "inline");
	
	//for 儲位用
	if(enableStorage){
		vat.item.setStyleByName("#B.storageExport"   , "display", "inline");
		vat.item.setStyleByName("#B.storageImport"   , "display", "inline");
	}else{
		vat.item.setStyleByName("#B.storageExport"   , "display", "none");
		vat.item.setStyleByName("#B.storageImport"   , "display", "none");
	}
	
	vat.item.setAttributeByName("vatHeadDiv","readOnly",false,true,true);
	vat.item.setAttributeByName("vatMasterDiv","readOnly",false,true,true);
	vat.item.setAttributeByName("vatPosDiv","readOnly",false,true,true);
	vat.block.canGridModify( [vnB_Detail], true, true, true );
	vat.block.canGridModify( [vnB_Payment], true, true, true );
	
	if("T2" != brandCode | ("T2" == brandCode & VorderTypeCode == "SOE")){
		vat.item.setStyleByName("#B.extendItemInfo"	, "display", "none");
		vat.item.setAttributeByName("#F.verificationStatus","readOnly",true);
		if ("T2" != brandCode)
			vat.item.setStyleByName("#B.print"		, "display", "none");
	}
	
	if(VorderNo.indexOf("TMP") == -1){
		vat.tabm.displayToggle(0, "xTab7", true, false, false);
		refreshWfParameter( vat.item.getValueByName("#F.brandCode"), 
     		   				vat.item.getValueByName("#F.orderTypeCode"),
     		   				vat.item.getValueByName("#F.orderNo" ) );
		vat.block.pageDataLoad(102, vnCurrentPage = 1); 
	}else{
		vat.tabm.displayToggle(0, "xTab7", false, false, false);
	}
	if(enableStorage){
		vat.item.setAttributeByName("#F.customsNo","readOnly",false);
	}else{
		vat.item.setAttributeByName("#F.customsNo","readOnly",true);
	}
	if(VprocessId == ""){
		if(VorderTypeCode == "SOP" && Vstatus == "SIGNING"){
			vat.item.setAttributeByName("vatHeadDiv","readOnly",true,true,true);
			vat.item.setAttributeByName("vatMasterDiv","readOnly",true,true,true);
			vat.item.setAttributeByName("vatPosDiv","readOnly",true,true,true);
			vat.item.setStyleByName("#B.save"			, "display", "none");
			vat.item.setStyleByName("#B.submit"			, "display", "none");
			vat.item.setStyleByName("#B.submitBG"		, "display", "none");
			vat.item.setStyleByName("#B.message"		, "display", "none");
			vat.item.setStyleByName("#B.import"			, "display", "none");
			//for 儲位用
			if(enableStorage){
				vat.item.setStyleByName("#B.storageImport"   , "display", "none");
			}
			vat.item.setStyleByName("#B.extendItemInfo"	, "display", "none");
			vat.item.setStyleByName("#B.uncomfirm"	, "display", "inline");
			vat.block.canGridModify( [vnB_Detail], false, false, false );
			vat.block.canGridModify( [vnB_Payment], false, false, false );
		}else if(VorderTypeCode == "SOP" && (Vstatus == "UNCONFIRMED" || Vstatus == "SAVE")){
			if(VorderNo.indexOf("TMP") == -1){
				vat.item.setStyleByName("#B.void"		, "display", "inline");
			}
			vat.block.canGridModify( [vnB_Detail], true, true, true );
			vat.block.canGridModify( [vnB_Payment], true, true, true );
		}else if (brandCode == "T2" && VorderTypeCode == "SOE" && Vstatus == "SAVE"){
			if(VorderNo.indexOf("TMP") == -1){
				vat.item.setStyleByName("#B.void"		, "display", "inline");
			}
		}else if(VorderNo.indexOf("TMP") == -1){
			vat.item.setAttributeByName("vatHeadDiv","readOnly",true,true,true);
			vat.item.setAttributeByName("vatMasterDiv","readOnly",true,true,true);
			vat.item.setAttributeByName("vatPosDiv","readOnly",true,true,true);
			vat.block.canGridModify( [vnB_Detail], false, false, false );
			vat.block.canGridModify( [vnB_Payment], false, false, false );
			if(VorderTypeCode == "SOP" && Vstatus == "VOID"){
				vat.item.setStyleByName("#B.save"			, "display", "inline");
				vat.item.setStyleByName("#B.extendItemInfo"	, "display", "inline");
			}else{
				vat.item.setStyleByName("#B.save"			, "display", "none");
				vat.item.setStyleByName("#B.extendItemInfo"	, "display", "none");
			}			
			vat.item.setStyleByName("#B.submit"			, "display", "none");
			vat.item.setStyleByName("#B.submitBG"		, "display", "none");
			vat.item.setStyleByName("#B.message"		, "display", "none");
			vat.item.setStyleByName("#B.import"			, "display", "none");
			//for 儲位用
			if(enableStorage){
				vat.item.setStyleByName("#B.storageImport"   , "display", "none");
			}			
		}
		else{
			vat.item.setAttributeByName("vatHeadDiv","readOnly",false,true,true);
			vat.item.setAttributeByName("vatMasterDiv","readOnly",false,true,true);
			vat.item.setAttributeByName("vatPosDiv","readOnly",false,true,true);
			vat.block.canGridModify( [vnB_Detail], true, true, true );
			vat.block.canGridModify( [vnB_Payment], true, true, true );
		}
	}else{
		vat.item.setStyleByName("#B.new"			, "display", "none");
		vat.item.setStyleByName("#B.search"			, "display", "none");
		if(Vstatus == "SAVE" || Vstatus == "REJECT"){
		
			vat.item.setStyleByName("#B.void"		, "display", "inline");
		}else if(Vstatus == "SIGNING" || Vstatus == "FINISH"){
			vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
			vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
			
			vat.item.setAttributeByName("vatHeadDiv","readOnly",true,true,true);
			vat.item.setAttributeByName("vatMasterDiv","readOnly",true,true,true);
			vat.item.setAttributeByName("vatPosDiv","readOnly",true,true,true);
			vat.item.setStyleByName("#B.save"			, "display", "none");
			vat.item.setStyleByName("#B.submitBG"		, "display", "none");
			vat.item.setStyleByName("#B.message"		, "display", "none");
			vat.item.setStyleByName("#B.import"			, "display", "none");
			//for 儲位用
			if(enableStorage){
				vat.item.setStyleByName("#B.storageImport"   , "display", "none");
			}
			vat.item.setStyleByName("#B.extendItemInfo"	, "display", "none");
			if(document.forms[0]["#loginEmployeeCode"].value == vat.item.getValueByName("#F.createdBy")){
				vat.item.setAttributeByName("#F.verificationStatus", "readOnly", false);
			}
			if(document.forms[0]["#allowApproval"].value == "N"){
				vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
			}
			vat.block.canGridModify( [vnB_Detail], false, false, false );
			vat.block.canGridModify( [vnB_Payment], false, false, false );
		}
	}
	
	checkCustomsStatus();
	vat.block.pageRefresh(vnB_Detail);
	vat.block.pageRefresh(vnB_Payment);
}


// 上傳海關
function updateCustomsStatus(tranStatus){
	//alert("adv");
		//alert(tranStatus);
		if(confirm("是否要送出?")){
			vat.bean().vatBeanOther =
        	{ 
          		loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          		loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          		formId             	: document.forms[0]["#formId"            ].value,
          		orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,
          		tranStatus         : tranStatus,
          		headId : vat.item.getValueByName("#F.headId"),
          		orderNo: vat.item.getValueByName("#F.orderNo")
        	};
   			vat.bean.init(	
	  			function(){
						return "process_object_name=soSalesOrderMainService&process_object_method_name=updateCustomsStatus"; 
	    		},{link:true, other: true,
	    			funcSuccess:function(){
					window.top.close();
					}
    			}
    		);	
		}
		
}