/*
	initial 
*/
var afterSavePageProcess = "";
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2; 
var vnB_Detail = 3;
var vnB_Total  = 4;
var vnB_POS = 5;

//for 儲位用
var vatStorageDetail = 202;
var enableStorage = false;

function kweImBlock(){
	kweInitial();
  	kweButtonLine();
  	kweHeader();

    if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");                                                                                                                                                                                                                                          
		vat.tabm.createButton(0 ,"xTab1","主檔資料" ,"vatMasterDiv","images/tab_master_data_dark.gif","images/tab_master_data_light.gif", false , "");
		vat.tabm.createButton(0 ,"xTab2","明細資料" ,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false , ""); 
		vat.tabm.createButton(0 ,"xTab3","金額資料" ,"vatTotalDiv","images/tab_total_amount_dark.gif","images/tab_total_amount_light.gif", false , "showTotalCountPage()");
      	vat.tabm.createButton(0, "xTab4","POS資料" ,"vatPosDiv"  ,"images/tab_pos_data_dark.gif"    ,"images/tab_pos_data_light.gif",(document.forms[0]["#orderTypeCode" ].value == "IRP" ? "inline" : "inline"), "");
      	vat.tabm.createButton(0 ,"xTab5","簽核資料" ,"vatApprovalDiv","images/tab_approval_data_dark.gif","images/tab_approval_data_light.gif",document.forms[0]["#processId"].value==""?"none":"inline");

      	//for 儲位用
 		enableStorage = "T2" == document.forms[0]["#loginBrandCode"    ].value;
		if(enableStorage){
  			vat.tabm.createButton(0, "xTab6", "儲位資料", "vatStorageDiv", "images/tab_storage_detail_dark.gif", "images/tab_storage_detail_light.gif", "", "reloadStorageDetail()");
  		}
  	}

	kweMaster();
	kweDetail();
	kweTotal();
	posDataInitial();
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
             vat.item.getValueByName("#F.orderTypeCode"), 
             vat.item.getValueByName("#F.orderNo"),
             document.forms[0]["#loginEmployeeCode"].value );

    //for 儲位用
	if(enableStorage){
		kweStorageBlock();
	}

	//簽核意見寫死為Y             
  	doFormAccessControl();

}

function kweInitial(){
	var organizationCode = "TM";
    var assignmentId = document.forms[0]["#assignmentId"].value.replace(/^\s+|\s+$/, '');
    var processId = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var priceType = "1";
    var isCommitOnHand = document.forms[0]["#isCommitOnHand"].value.replace(/^\s+|\s+$/, '');
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther =
		{
		loginBrandCode  	: document.forms[0]["#loginBrandCode" ].value,
		orderTypeCode		: document.forms[0]["#orderTypeCode" ].value,
		organizationCode 	: organizationCode,
		formId             	: document.forms[0]["#formId"            ].value,
		loginUser			: document.forms[0]["#loginEmployeeCode" ].value,
		employeeCode		: document.forms[0]["#loginEmployeeCode" ].value,
		loginEmployeeCode	: document.forms[0]["#loginEmployeeCode" ].value,
		currentRecordNumber : 0,
		lastRecordNumber    : 0,
		beforeChangeStatus 	: "",
		formStatus 			: "",
		assignmentId 		: assignmentId,
		processId 			: processId,
		approvalResult 		: "",
		approvalComment 	: "",
		priceType 			: priceType,
		isCommitOnHand 		: isCommitOnHand
    	};
   	 	vat.bean.init(	
  		function(){
				return "process_object_name=imDeliveryMainService&process_object_method_name=executeReturnInitial"; 
    	},{
    		other: true
    	}
    );
  }
}
function checkCustomsStatus(){
	//alert(vat.item.getValueByName("#F.customsStatusHidden"));
	var statusHidden = vat.item.getValueByName("#F.customsStatusHidden");
	var cusStatusHidden = statusHidden.substring(0, 1);
	//alert("cusStatusHidden:"+cusStatusHidden);
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

function kweButtonLine(){
	var showTotal = [["","",false],["是","否"],["Y","N"]];
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[/*{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"/erp/Im_Delivery:search:20091009.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			*/
	 			{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmitHandler()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSaveHandler()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢",   src:"./images/button_void.gif", eClick:'doVoidHandler()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmitBgHandler()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendCustoms" 	   , type:"IMG"    ,value:"上傳海關",   src:"./images/send_customs1.jpg", eClick:'updateCustomsStatus("")'},
	 			//{name:"#B.uncomfirm"   , type:"IMG"    ,value:"反確認"  ,   src:"./images/button_uncomfirm.gif"  , eClick:'doUnConfirmedHandler()'},
	 			//{name:"SPACE"      	, type:"LABEL"  ,value:"　"}		,
	 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import"      , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif", eClick:'doImport()'},
				{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				//for 儲位用
	 			{name:"#B.storageExport", 	type:"IMG"    ,value:"儲位匯出",   src:"./images/button_storage_export.gif" , eClick:'exportStorageFormData()'},
	 			{name:"#B.storageImport",	type:"PICKER" , value:"儲位匯入",  src:"./images/button_storage_import.gif"  , 
						 openMode:"open", 
						 service:"/erp/fileUpload:standard:2.page",
						 servicePassData:function(x){ return importStorageFormData(); },
						 left:0, right:0, width:600, height:400,	
						 serviceAfterPick:function(){}},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif", eClick:'openReportWindow()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　顯示金額"},
	 			{name:"#F.total"       , type:"SELECT" ,init:showTotal}
	 			/*,
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }
	 			*/],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

function kweHeader(){
var allReturnOrderType = vat.bean("allReturnOrderType");
var allDeliveryOrderType = vat.bean("allDeliveryOrderType");
var allItemCategory = vat.bean("allItemCategory");

vat.block.create(vnB_Header, {
	id: "vatHeaderDiv", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"銷退單維護作業", rows:[  
	{row_style:"", cols:[
		{items:[{name:"#L.orderType", type:"LABEL", value:"單別"}]},	 
		{items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode", init:allReturnOrderType, mode:"READONLY"},
				{name:"#F.orderCondition", type:"TEXT", bind:"orderCondition", size:2, mode:"HIDDEN"}]},		 
		{items:[{name:"#L.orderNo", type:"LABEL", value:"單號"}]},
		{items:[{name:"#F.orderNo", type:"TEXT", bind:"orderNo", back:false, size:20, mode:"READONLY"},
				 {name:"#F.headId", type:"TEXT", bind:"headId", back:false, size:8, mode:"READONLY"}
				//for 儲位用
				,{name:"#F.storageHeadId",   type:"TEXT",   bind:"storageHeadId",    back:false, mode:"READONLY" }
	 			]},
		{items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},
		{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:8, mode:"READONLY"},
		        {name:"#F.brandName", type:"TEXT", bind:"brandName", size:12, mode:"READONLY"}]}, 
		{items:[{name:"#L.status", type:"LABEL", value:"狀態"}]},
		{items:[{name:"#F.status", type:"TEXT", bind:"status", size:8, mode:"HIDDEN"},
	        	{name:"#F.statusName", type:"TEXT", bind:"statusName", size:8, mode:"READONLY"},
		 		{name:"#F.tranRecordStatus"  , type:"TEXT"  ,  bind:"tranRecordStatus", mode:"HIDDEN"},
	  		 	{name:"#F.tranAllowUpload"  , type:"TEXT"  ,  bind:"tranAllowUpload", mode:"HIDDEN"},
	  		 	{name:"#F.customsStatus", 				type:"TEXT",size:8, mode:"READONLY"},
	  		 	{name:"#F.customsStatusHidden", 				type:"TEXT",  bind:"customsStatus", mode:"HIDDEN"}
	  		 ]}]},
	    	 			 	 
	{row_style:"", cols:[
		{items:[{name:"#L.origDeliveryOrderTypeCode", type:"LABEL", value:"出貨單別"}]},
		{items:[{name:"#F.origDeliveryOrderTypeCode", type:"SELECT", bind:"origDeliveryOrderTypeCode", init:allDeliveryOrderType}]},	 
		{items:[{name:"#L.origDeliveryOrderNo", type:"LABEL", value:"出貨單號"}]},
		{items:[{name:"#F.origDeliveryOrderNo", type:"TEXT", bind:"origDeliveryOrderNo", size:13, maxLen:13, eChange:"getOriginalDelivery()"}]},
		{items:[{name:"#L.shipDate", type:"LABEL", value:"退貨日期"}]},
		{items:[{name:"#F.shipDate", type:"DATE", bind:"shipDate", size:1}]},	 
		{items:[{name:"#L.returnDate", type:"LABEL", value:"出貨日期"}]},
		{items:[{name:"#F.returnDate", type:"DATE", bind:"returnDate", size:1, mode:"readOnly"}], td:" colSpan=2"}]}, 
	{row_style:"", cols:[
		{items:[{name:"#L.customerCode", type:"LABEL", value:"客戶代號"}]},
		{items:[{name:"#F.customerCode", type:"TEXT", bind:"customerCode", size:12,eChange:"changeCustomerCode()"},
				{name:"#B.customerCode", value:"選取" ,type:"PICKER" ,
				 									 openMode:"open", src:"./images/start_node_16.gif",
				 									 service:"Bu_AddressBook:searchCustomer:20100101.page", 
				 									 left:0, right:0, width:1024, height:768,	
				 									 serviceAfterPick:function(){doAfterPickerCustomer();} },
	        	{name:"#F.customerName", type:"TEXT", bind:"customerName", size:6, mode:"READONLY"},
	        	{name:"#F.priceType", type:"TEXT", bind:"priceType", mode:"HIDDEN"}]},
		{items:[{name:"#L.guiCode", type:"LABEL", value:"統一編號"}]},	 
		{items:[{name:"#F.guiCode", type:"TEXT", bind:"guiCode", size:8, maxLen:8}]}, 	 
		{items:[{name:"#L.creationDate", type:"LABEL", value:"填單日期"}]},
		{items:[{name:"#F.creationDate", type:"TEXT", bind:"creationDate", mode:"READONLY", size:12}]},
		{items:[{name:"#L.createdBy", type:"LABEL", value:"填單人員"}]},
		{items:[{name:"#F.createdByName", type:"TEXT",   bind:"createdByName", size:10, mode:"READONLY"},
				{name:"#F.createdBy", type:"TEXT", bind:"createdBy", mode:"HIDDEN", size:8}], td:" colSpan=2"}
		]},
	{row_style:"", cols:[
		{items:[{name:"#L.itemCategory", type:"LABEL", value:"業種"}]},
		{items:[{name:"#F.itemCategory", type:"SELECT" , bind:"itemCategory", init:allItemCategory}]},
		{items:[{name:"#L.receiptDate", type:"LABEL", value:"入庫日期"}]},
		{items:[{name:"#F.receiptDate", type:"DATE", bind:"receiptDate", mode:"READONLY", size:12}]},
		{items:[{name:"#L.reserve1", type:"LABEL", value:"退貨原因"}]},
		{items:[{name:"#F.reserve1", type:"TEXT", bind:"reserve1", size:60, maxLen:100}]},
	 	{items:[{name:"#L.customsNo", type:"LABEL", value:"上傳單號"}]},	 
	 	{items:[{name:"#F.customsNo", type:"TEXT", bind:"customsNo", size:14}]}
		]}],
	 beginService:"",
	 closeService:""			
	});
}

function kweMaster(){

	var sufficientQuantityDeliverys = [["","",false],["是","否"],["Y","N"]];
	var attachedInvoices = [["","",false],["是","否"],["Y","N"]];
	var allInvoiceType = vat.bean("allInvoiceType");
	var allTaxType = vat.bean("allTaxType");
	var allDeliveryType = vat.bean("allDeliveryType");
	var allPaymentCategory = vat.bean("allPaymentCategory");
	var allCountry = vat.bean("allCountry");
	var allCurrency = vat.bean("allCurrency");
	var allPaymentTerm = vat.bean("allPaymentTerm");
	var allShop = vat.bean("allShop"); 
	var allAvailableWarehouse = vat.bean("allAvailableWarehouse");
	var branchMode = vat.bean("branchCode");
	//如果是保稅銷退單
	if("ERF" == document.forms[0]["#orderTypeCode" ].value){
		branchMode = "";
	}else{
		branchMode = " style='display:none'";
	}

	vat.block.create(vnB_Master, {
		id: "vatMasterDiv", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		rows:[
		{row_style:branchMode, cols:[
			{items:[{name:"#L.exportDeclNo", type:"LABEL" , value:"報關單號" }]},
		 	{items:[{name:"#F.exportDeclNo", type:"TEXT", bind:"exportDeclNo", size:16 ,eChange:"getCMHead()", size:20 }]},
		 	{items:[{name:"#L.exportDeclDate", type:"LABEL" , value:"報關單日期" }]},
		 	{items:[{name:"#F.exportDeclDate", type:"DATE", bind:"exportDeclDate",size:10,mode:"readOnly", size:20 }]},
		 	{items:[{name:"#L.exportDeclType", type:"LABEL" , value:"報關單類別" }]},
		 	{items:[{name:"#F.exportDeclType", type:"TEXT", bind:"exportDeclType",size:10,maxLen:2,mode:"readOnly", size:20 }]}
		]},
	 	{row_style:"", cols:[
			 {items:[{name:"#L.shopCode", type:"LABEL", value:"專櫃代號"}]},
			 {items:[{name:"#F.shopCode", type:"SELECT", bind:"shopCode", init:allShop, eChange:"changeShopCode()"}]},	 
			 {items:[{name:"#L.sufficientQuantityDelivery", type:"LABEL", value:"足量出貨"}]},
			 {items:[{name:"#F.sufficientQuantityDelivery", type:"SELECT", bind:"sufficientQuantityDelivery", init:sufficientQuantityDeliverys}]},
			 {items:[{name:"#L.superintendentCode", type:"LABEL", value:"訂單負責人"}]},
			 {items:[{name:"#F.superintendentCode", type:"TEXT", bind:"superintendentCode", size:10, maxLen:6, eChange:"changeSuperintendent()"},
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
			{items:[{name:"#L.countryCode", type:"LABEL", value:"國別"}]},
			{items:[{name:"#F.countryCode", type:"SELECT", bind:"countryCode", init:allCountry}]}
			]},	 	 	 
		{row_style:"", cols:[
			{items:[{name:"#L.paymentTermCode", type:"LABEL", value:"付款條件"}]},
			{items:[{name:"#F.paymentTermCode", type:"SELECT", bind:"paymentTermCode", init:allPaymentTerm}]},	
			{items:[{name:"#L.scheduleCollectionDate", type:"LABEL", value:"付款日"}]},
			{items:[{name:"#F.scheduleCollectionDate", type:"DATE", bind:"scheduleCollectionDate"}]},	 
			{items:[{name:"#L.currencyCode", type:"LABEL", value:"幣別"}]},
			{items:[{name:"#F.currencyCode", type:"SELECT", bind:"currencyCode", init:allCurrency, eChange:"changeExchangeRate()"},
			 		{name:"#L.exportExchangeRate", type:"LABEL", value:" 匯率 "},
			 		{name:"#F.exportExchangeRate", type:"text", bind:"exportExchangeRate", mode:"readOnly"}]}
			]},	 	 
		{row_style:"", cols:[
			{items:[{name:"#L.invoiceAddressTitle", type:"LABEL", value:"發票地址"}]},
			{items:[{name:"#L.invoiceCity", type:"LABEL", value:"城市:&nbsp&nbsp;"},
					{name:"#F.invoiceCity", type:"TEXT" ,  bind:"invoiceCity", size:10, maxLen:10},
					{name:"#L.invoiceArea", type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
			 		{name:"#F.invoiceArea", type:"TEXT",   bind:"invoiceArea", size:10, maxLen:10},
			 		{name:"#L.invoiceZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
			 		{name:"#F.invoiceZipCode", type:"TEXT", bind:"invoiceZipCode", size:5, maxLen:5},	 		 
			 		{name:"#L.invoiceAddress", type:"LABEL", value:"&nbsp&nbsp&nbsp;地址:&nbsp&nbsp;"},
		 			{name:"#F.invoiceAddress", type:"TEXT", bind:"invoiceAddress", size:70, maxLen:200}], td:" colSpan=5"}
		 	]},	 
		{row_style:"", cols:[
			{items:[{name:"#L.shipAddressTitle", type:"LABEL", value:"送貨地址"}]},
			{items:[{name:"#L.shipCity", type:"LABEL", value:"城市:&nbsp&nbsp;"},
					{name:"#F.shipCity", type:"TEXT" ,  bind:"shipCity", size:10, maxLen:10},
					{name:"#L.shipArea", type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
			 		{name:"#F.shipArea", type:"TEXT",   bind:"shipArea", size:10, maxLen:10},
			 		{name:"#L.shipZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
			 		{name:"#F.shipZipCode", type:"TEXT", bind:"shipZipCode", size:5, maxLen:5},	 		 
			 		{name:"#L.shipAddress", type:"LABEL", value:"&nbsp&nbsp&nbsp;地址:&nbsp&nbsp;"},
		 			{name:"#F.shipAddress", type:"TEXT", bind:"shipAddress", size:70, maxLen:100}], td:" colSpan=5"}
		 	]},	 
		{row_style:"", cols:[
			{items:[{name:"#L.invoiceTypeCode", type:"LABEL", value:"發票類型"}]},
			{items:[{name:"#F.invoiceTypeCode", type:"SELECT", bind:"invoiceTypeCode", init:allInvoiceType}]},	
			{items:[{name:"#L.taxType", type:"LABEL", value:"稅別"}]},
			{items:[{name:"#F.taxType", type:"SELECT", bind:"taxType", init:allTaxType, eChange:"changeTaxRate()"}]},	 
			{items:[{name:"#L.taxRate", type:"LABEL", value:"稅率"}]},
			{items:[{name:"#F.taxRate", type:"TEXT" , bind:"taxRate", size:8, maxLen:4, mode:"readOnly"}]}
			]}, 	 
		{row_style:"", cols:[
			{items:[{name:"#L.defaultWarehouseCode", type:"LABEL", value:"庫別"}]},
			{items:[{name:"#F.defaultWarehouseCode", type:"SELECT", bind:"defaultWarehouseCode", init:allAvailableWarehouse, eChange:"doPageRefresh()"}]},
			{items:[{name:"#L.homeDelivery", type:"LABEL", value:"運送方式"}]},
			{items:[{name:"#F.homeDelivery", type:"SELECT", bind:"homeDelivery", init:allDeliveryType}]},
			{items:[{name:"#L.exportCommissionRate", type:"LABEL", value:"手續費率"}]},
			{items:[{name:"#F.exportCommissionRate", type:"TEXT" , bind:"exportCommissionRate", size:2, maxLen:4, eChange:"changeCommissionRate()"},
					{name:"#L.%", type:"LABEL", value:"%"}]}
			 ]},	
	    {row_style:"", cols:[	 		 
			{items:[{name:"#L.promotionCode", type:"LABEL", value:"活動代號"}]},
			{items:[{name:"#F.promotionCode", type:"TEXT", bind:"promotionCode", size:20, maxLen:20, eChange:"changePromotionCode()"}]},
			{items:[{name:"#L.paymentCategory", type:"LABEL", value:"付款方式"}]},
			{items:[{name:"#F.paymentCategory", type:"SELECT", bind:"paymentCategory", init:allPaymentCategory}]},
			{items:[{name:"#L.discountRate", type:"LABEL", value:"折扣比率"}]},
			{items:[{name:"#F.discountRate", type:"TEXT" , bind:"discountRate", size:2, maxLen:4, eChange:"changeDiscountRate()"},
					{name:"#L.%", type:"LABEL", value:"%"}]}
			]},
		{row_style:"", cols:[
			{items:[{name:"#L.remark1", type:"LABEL", value:"備註一"}]},
			{items:[{name:"#F.remark1", type:"TEXT", bind:"remark1", size:100, maxLen:100}], td:" colSpan=3"},
			{items:[{name:"#L.exportExpense", type:"LABEL", value:"其他費用"}]},
			{items:[{name:"#F.exportExpense", type:"NUMM" , bind:"exportExpense"}]}
			]},	  
		{row_style:"", cols:[
			{items:[{name:"#L.remark2", type:"LABEL", value:"備註二"}]},
			{items:[{name:"#F.remark2", type:"TEXT", bind:"remark2", size:100, maxLen:100}], td:" colSpan=3"},
			{items:[{name:"#L.attachedInvoice", type:"LABEL", value:"隨附發票"}]},
			{items:[{name:"#F.attachedInvoice", type:"SELECT", bind:"attachedInvoice", init:attachedInvoices}]}
			]}
		 ],
		 beginService:"",
		 closeService:""			
	});
}

function kweDetail(){
    vat.item.setValueByName("#F.origDeliveryOrderNo" , vat.item.getValueByName("#F.origDeliveryOrderNo").replace(/^\s+|\s+$/, ''));
    var brandCode = vat.bean("brandCode");
    var orderCondition = vat.item.getValueByName("#F.orderCondition");
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;	
	var nameType = "";
	//如果不是T2
	if(orderCondition == "F"){
		nameType = "原幣"
	}else{
	}
	
	var hideExport = true;
    if("F" == orderCondition){
		hideExport = false;
	}
	
	// set column
	vat.item.make(vnB_Detail, "indexNo", 				{type:"IDX" , desc:"序號", view: "fixed"});	
	vat.item.make(vnB_Detail, "itemCode", 				{type:"TEXT", size:15, view: "fixed", maxLen:20, desc:"品號", mask:"CCCCCCCCCCCC", onchange:"changeItemData(1)"}); 
	vat.item.make(vnB_Detail, "itemCName", 				{type:"TEXT", size:18, view: "fixed", maxLen:20, desc:"品名", mode:"READONLY"});   
	vat.item.make(vnB_Detail, "warehouseCode", 			{type:"TEXT", size:6, view: "", maxLen:12, desc:"庫別", mode:"READONLY"});
	vat.item.make(vnB_Detail, "warehouseName", 			{type:"TEXT", size:10, view: "", maxLen:20, desc:"庫名", mode:"HIDDEN"});    
	vat.item.make(vnB_Detail, "lotNo", 					{type:"TEXT", size:12, view: "", maxLen:20, desc:"批號", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "originalForeignUnitPrice",{type:"NUMB", size: 8, view: "", maxLen:12, desc:nameType+"單價", mask:"CCCCCCCCCCCC", onchange:"changeItemData(2)", mode:hideExport?"HIDDEN":""});	
	vat.item.make(vnB_Detail, "originalUnitPrice", 		{type:"NUMB", size: 8, view: "", maxLen:12, desc:"本幣單價", mode:hideExport?"HIDDEN":"READONLY"});
	vat.item.make(vnB_Detail, "actualForeignUnitPrice", {type:"NUMB", size: 8, view: "", maxLen:12, desc:nameType+"實際單價", mask:"CCCCCCCCCCCC", onchange:"changeItemData(2)"});
	vat.item.make(vnB_Detail, "actualUnitPrice", 		{type:"NUMB", size: 8, view: "", maxLen:12, desc:"本幣實際單價", mode:hideExport?"HIDDEN":"READONLY"});	
	vat.item.make(vnB_Detail, "returnableQuantity", 	{type:"NUMB", size: 8, view: "shift", maxLen: 8, desc:"可退數量", mode:"READONLY"});
	vat.item.make(vnB_Detail, "shipQuantity", 			{type:"NUMB", size: 8, view: "shift", maxLen: 8, desc:"退貨數量", onchange:"changeItemData(3)"});
	vat.item.make(vnB_Detail, "originalForeignShipAmt", {type:"NUMB", size: 8, view: "shift", maxLen:20, desc:nameType+"退貨金額", mode:"READONLY"});
	vat.item.make(vnB_Detail, "originalShipAmount", 	{type:"NUMB", size: 8, view: "shift", maxLen:20, desc:"本幣退貨金額", mode:hideExport?"HIDDEN":"READONLY"});
	vat.item.make(vnB_Detail, "actualForeignShipAmt", 	{type:"NUMB", size: 8, view: "shift", maxLen:20, desc:nameType+"實際退貨金額", mode:"READONLY"});
	vat.item.make(vnB_Detail, "actualShipAmount", 		{type:"NUMB", size: 8, view: "shift", maxLen:20, desc:"本幣實際退貨金額", mode:hideExport?"HIDDEN":"READONLY"});
	vat.item.make(vnB_Detail, "taxType", 				{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"稅別", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "taxRate", 				{type:"NUMB", size: 1, view: "shift", maxLen: 8, desc:"稅率", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "shipTaxAmount", 			{type:"NUMB", size: 1, view: "shift", maxLen: 1, desc:"稅額", mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "isTax", 					{type:"TEXT", size: 1, view: "shift", maxLen: 1, desc:"是否含稅", mode: "HIDDEN"});	
	vat.item.make(vnB_Detail, "watchSerialNo", 			{type:"TEXT", size:20, view: "shift", maxLen:20, desc:"手錶序號", mode: "T3CU"==brandCode?"":"HIDDEN"});
	vat.item.make(vnB_Detail, "watchSerialNoPicker",	{type:"PICKER", view:"shift", value:"PICKER", src:"./images/start_node_16.gif", mode: "T3CU"==brandCode?"":"HIDDEN",
			 									 			openMode:"open", 
			 									 		 	service:"Im_Item:searchSerialLine:20100404.page", 
			 									 		 	left:0, right:0, width:1024, height:768,
			 									 		 	servicePassData:function(X){ return doPassData(X, "watchSerialNo");},
			 									 			serviceAfterPick:function(X){doAfterPickerWatchSerialNo(X);}});
	vat.item.make(vnB_Detail, "reserve2", 				{type:"TEXT", size: 1, view: "shift", maxLen: 20, desc:"原出貨單識別碼",mode: "HIDDEN"});
	vat.item.make(vnB_Detail, "importDeclNo", 			{type:"TEXT", size: 14, view: "shift",maxLen:16, desc:"報單號碼", mode:hideExport?"READONLY":""});
	vat.item.make(vnB_Detail, "importDeclDate", 		{type:"DATE", view: "shift", desc:"報單日期", mode:hideExport?"READONLY":""});
	vat.item.make(vnB_Detail, "importDeclType", 		{type:"TEXT", size: 2, view: "shift", maxLen:2, desc:"報單類別", mode:hideExport?"READONLY":""});
	vat.item.make(vnB_Detail, "importDeclSeq", 			{type:"NUMB", size: 2, view: "shift", maxLen:3, desc:"報單項次", mode:hideExport?"READONLY":""});
	vat.item.make(vnB_Detail, "lineId", 				{type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", 			{type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", 		{type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Detail, "message", 				{type:"MSG", desc:"訊息"});
	//vat.item.make(vnB_Detail, "adv", 					{type:"BUTTON", desc:"進階輸入", value:"進階輸入", src:"images/button_advance_input.gif", eClick:"advanceInput()"});
	vat.block.pageLayout(vnB_Detail, {	
								id: "vatDetailDiv",
								pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,													
							    appendBeforeService : "appendBeforeMethod()",
							    appendAfterService  : "appendAfterMethod()",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "pageLoadSuccess()",
								eventService        : "changeRelationData()",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}


function kweTotal(){
    var branchMode = vat.bean("branchCode");
    if(branchMode == "2"){
		vat.block.create(vnB_Total, {
		id: "vatTotalDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
			rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.originalTotalFrnShipAmt"    , type:"LABEL" , value:"原幣退貨總金額"}]},
				{items:[{name:"#F.originalTotalFrnShipAmt"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalOriginalShipAmount"    , type:"LABEL" , value:"退貨總金額"}]},	 
				{items:[{name:"#F.totalOriginalShipAmount"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]}/*,
			{row_style:"", cols:[
				{items:[{name:"#L.totalDeductionFrnAmount" , type:"LABEL" , value:"退貨折讓"}]},
				{items:[{name:"#F.totalDeductionFrnAmount" , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalDeductionAmount"    , type:"LABEL" , value:"退貨折讓"}]},	 
				{items:[{name:"#F.totalDeductionAmount"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]}*/,
			{row_style:"", cols:[
				{items:[{name:"#L.actualTotalFrnShipAmt", type:"LABEL" , value:"原幣實際退貨金額"}]},
				{items:[{name:"#F.actualTotalFrnShipAmt", type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalActualShipAmount", type:"LABEL" , value:"實際退貨金額"}]},	 
				{items:[{name:"#F.totalActualShipAmount", type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalOtherFrnExpense" , type:"LABEL" , value:"原幣其他費用"}]},
				{items:[{name:"#F.totalOtherFrnExpense" , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalOtherExpense"    , type:"LABEL" , value:"其他費用"}]},	 
				{items:[{name:"#F.totalOtherExpense"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.expenseForeignAmount"	, type:"LABEL" , value:"原幣手續費"}]},
				{items:[{name:"#F.expenseForeignAmount"	, type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.expenseLocalAmount"   , type:"LABEL" , value:"手續費"}]},	 
				{items:[{name:"#F.expenseLocalAmount"   , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.shipTaxFrnAmount" , type:"LABEL" , value:"原幣營業稅"}]},
				{items:[{name:"#F.shipTaxFrnAmount" , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.shipTaxAmount"    , type:"LABEL" , value:"營業稅"}]},	 
				{items:[{name:"#F.shipTaxAmount"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalNoneTaxFrnShipAmount", type:"LABEL" , value:"原幣未稅金額"}]},	 
				{items:[{name:"#F.totalNoneTaxFrnShipAmount", type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalNoneTaxShipAmount"   , type:"LABEL" , value:"未稅金額"}]},	 
				{items:[{name:"#F.totalNoneTaxShipAmount"   , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalItemQuantity"	, type:"LABEL" , value:"商品數量"}]},
				{items:[{name:"#F.totalItemQuantity"	, type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]}
	 	 ],
			beginService:"",
			closeService:""			
		});
	}else{
		vat.block.create(vnB_Total, {
		id: "vatTotalDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
			rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.totalOriginalShipAmount"    , type:"LABEL" , value:"退貨總金額"}]},	 
				{items:[{name:"#F.totalOriginalShipAmount"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]}/*,
			{row_style:"", cols:[
				{items:[{name:"#L.totalDeductionAmount"    , type:"LABEL" , value:"退貨折讓"}]},	 
				{items:[{name:"#F.totalDeductionAmount"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]}*/,
			{row_style:"", cols:[
				{items:[{name:"#L.totalActualShipAmount", type:"LABEL" , value:"實際退貨金額"}]},	 
				{items:[{name:"#F.totalActualShipAmount", type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalOtherExpense"    , type:"LABEL" , value:"其他費用"}]},	 
				{items:[{name:"#F.totalOtherExpense"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.expenseLocalAmount"   , type:"LABEL" , value:"手續費"}]},	 
				{items:[{name:"#F.expenseLocalAmount"   , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.shipTaxAmount"    , type:"LABEL" , value:"營業稅"}]},	 
				{items:[{name:"#F.shipTaxAmount"    , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalNoneTaxShipAmount"   , type:"LABEL" , value:"未稅金額"}]},	 
				{items:[{name:"#F.totalNoneTaxShipAmount"   , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalItemQuantity"	, type:"LABEL" , value:"商品數量"}]},
				{items:[{name:"#F.totalItemQuantity"	, type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]}
	 	 ],
			beginService:"",
			closeService:""			
		});
	}
}

function posDataInitial(){
var allshopMachine = vat.bean("allshopMachine");
	vat.block.create(vnB_POS, {
		id: "vatPosDiv", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		rows:[  
		 {row_style:"", cols:[
		 {items:[{name:"#L.posMachineCode", type:"LABEL", value:"POS機號"}]},	 
		 {items:[{name:"#F.posMachineCode", type:"SELECT", bind:"posMachineCode", init:allshopMachine}]},		 
		 {items:[{name:"#L.casherCode", type:"LABEL", value:"收銀員代號"}]},
		 {items:[{name:"#F.casherCode", type:"TEXT", bind:"casherCode", size:15, maxLen:15},
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
		 {items:[{name:"#F.transactionTime", type:"TEXT", bind:"transactionTime", size:20, maxLen:20}]}]
		 }],	 	 
			 	 
		 beginService:"",
		 closeService:""			
	});
}

function loadBeforeAjxService(){
	var processString = "process_object_name=imDeliveryMainService&process_object_method_name=getAJAXPageDataForReturn" + 
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&formStatus=" + vat.item.getValueByName("#F.status") +
	                    "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") +
	                    "&taxType=" + vat.item.getValueByName("#F.taxType") + 
	                    "&taxRate=" + vat.item.getValueByName("#F.taxRate"); 
	return processString;											
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imDeliveryMainService&process_object_method_name=updateAJAXPageLinesDataForReturn" + 
					"&headId=" + vat.item.getValueByName("#F.headId") + 
					"&status=" + vat.item.getValueByName("#F.status");
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
    if ("saveHandler" == afterSavePageProcess) {
	    execSubmitAction("SAVE");
	}if ("unConfirmedHandler" == afterSavePageProcess) {
	    execSubmitAction("UNCONFIRMED");
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
		var processString = "process_object_name=imDeliveryMainService&process_object_method_name=updateTotalAmountForReturn" + 
	                "&headId=" + vat.item.getValueByName("#F.headId") +
	                "&taxType=" + vat.item.getValueByName("#F.taxType")+
	                "&taxRate=" + vat.item.getValueByName("#F.taxRate")+
	                "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                "&exportCommissionRate=" + vat.item.getValueByName("#F.exportCommissionRate")+
	                "&exportExchangeRate=" + vat.item.getValueByName("#F.exportExchangeRate")+
	                "&exportExpense=" + vat.item.getValueByName("#F.exportExpense");
		vat.ajax.startRequest(processString, function () {
		    if (vat.ajax.handleState()) {
				var branchMode = vat.bean("branchCode");
				vat.item.setValueByName("#F.totalOriginalShipAmount"	,vat.ajax.getValue("TotalOriginalShipAmount", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.totalDeductionAmount"		,vat.ajax.getValue("TotalDeductionAmount", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.shipTaxAmount"				,vat.ajax.getValue("ShipTaxAmount", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.totalOtherExpense"			,vat.ajax.getValue("TotalOtherExpense", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.expenseLocalAmount"			,vat.ajax.getValue("ExpenseLocalAmount", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.totalActualShipAmount"		,vat.ajax.getValue("TotalActualShipAmount", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.totalNoneTaxShipAmount"		,vat.ajax.getValue("TotalNoneTaxShipAmount", vat.ajax.xmlHttp.responseText));
				if(branchMode == "2"){
					vat.item.setValueByName("#F.originalTotalFrnShipAmt"	,vat.ajax.getValue("OriginalTotalFrnShipAmt", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalDeductionFrnAmount"	,vat.ajax.getValue("TotalDeductionFrnAmount", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.shipTaxFrnAmount"			,vat.ajax.getValue("ShipTaxFrnAmount", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalOtherFrnExpense"		,vat.ajax.getValue("TotalOtherFrnExpense", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.expenseForeignAmount"		,vat.ajax.getValue("ExpenseForeignAmount", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.actualTotalFrnShipAmt"		,vat.ajax.getValue("ActualTotalFrnShipAmt", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalNoneTaxFrnShipAmount"	,vat.ajax.getValue("TotalNoneTaxFrnShipAmount", vat.ajax.xmlHttp.responseText));
				}
				vat.item.setValueByName("#F.totalItemQuantity"			,vat.ajax.getValue("TotalItemQuantity", vat.ajax.xmlHttp.responseText));
			}
		});	
	}else if ("changeRelationData" == afterSavePageProcess) {
	    var processString = "process_object_name=imDeliveryMainService&process_object_method_name=updateReturnRelationData" + 
	                "&headId=" + vat.item.getValueByName("#F.headId") + 
	                "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
	                "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") + 
	                "&taxType=" + vat.item.getValueByName("#F.taxType") +
	                "&taxRate=" + vat.item.getValueByName("#F.taxRate") +
	                "&priceType=" + vat.item.getValueByName("#F.priceType") +
	                "&returnDate=" + vat.item.getValueByName("#F.shipDate") +
	                "&origDeliveryOrderNo=" + vat.item.getValueByName("#F.origDeliveryOrderNo") +
	                "&exportExchangeRate=" + vat.item.getValueByName("#F.exportExchangeRate") +
	                "&formStatus=" + vat.item.getValueByName("#F.status");
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
	送出反確認
*/
function doUnConfirmedHandler() {
	if (confirm("是否反確認送出?")) {		
		//save line
		afterSavePageProcess = "unConfirmedHandler";
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


function openReportWindow(type){ 
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    vat.bean().vatBeanOther.displayAmt = vat.item.getValueByName("#F.total");
	if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
		vat.block.submit(
					function(){return "process_object_name=imDeliveryMainService"+
								"&process_object_method_name=getReportConfig";},{other:true,
                    			funcSuccess:function(){
								eval(vat.bean().vatBeanOther.reportUrl);
					}}
		);   
     if("AFTER_SUBMIT"==type) createRefreshForm();//因為調撥單在送出後要直接列印報表，所以要有這行
}

function changeCustomerCode(){
    vat.item.setValueByName("#F.customerCode", vat.item.getValueByName("#F.customerCode").replace(/^\s+|\s+$/, ''));
    if(vat.item.getValueByName("#F.customerCode") != ""){
        vat.ajax.XHRequest(
       {
           post:"process_object_name=buCustomerWithAddressViewService"+
                    "&process_object_method_name=findCustomerByTypeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&customerCode=" + vat.item.getValueByName("#F.customerCode") +
                    "&searchCustomerType=customerCode" +
                    "&isEnable=",
           find: function changeCustomerCodeRequestSuccess(oXHR){
               vat.item.setValueByName("#F.customerCode", vat.ajax.getValue("CustomerCode_var", oXHR.responseText));
               vat.item.setValueByName("#F.customerName", vat.ajax.getValue("CustomerName", oXHR.responseText));
               vat.item.setValueByName("#F.contactPerson", vat.ajax.getValue("ContactPerson", oXHR.responseText));        
               vat.item.setValueByName("#F.contactTel", vat.ajax.getValue("ContactTel", oXHR.responseText));      
               vat.item.setValueByName("#F.receiver", vat.ajax.getValue("Receiver", oXHR.responseText));
               vat.item.setValueByName("#F.countryCode", vat.ajax.getValue("CountryCode", oXHR.responseText));
               vat.item.setValueByName("#F.currencyCode", vat.ajax.getValue("CurrencyCode", oXHR.responseText));
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
           }   
       });  
    }else{
               vat.item.setValueByName("#F.customerCode", "");
               vat.item.setValueByName("#F.customerName", "");
               vat.item.setValueByName("#F.contactPerson", "");        
               vat.item.setValueByName("#F.contactTel", "");      
               vat.item.setValueByName("#F.receiver", "");
               vat.item.setValueByName("#F.countryCode", "");
               vat.item.setValueByName("#F.currencyCode", "");
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
    }
}

function changeSuperintendent(){
	vat.item.setValueByName("#F.superintendentCode", vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, ''))
    if(vat.item.getValueByName("#F.superintendentCode") != ""){
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
        vat.item.setValueByName("#F.superintendentName", "")
    }
}

function changePromotionCode(){
	vat.item.setValueByName("#F.promotionCode" , vat.item.getValueByName("#F.promotionCode").replace(/^\s+|\s+$/, '').toUpperCase());
    if(vat.item.getValueByName("#F.promotionCode") != ""){
        vat.ajax.XHRequest(
        {
           post:"process_object_name=imPromotionService"+
                    "&process_object_method_name=findByBrandCodeAndPromotionCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
                    "&promotionCode=" + vat.item.getValueByName("#F.promotionCode"),                      
           find: function changePromotionCodeRequestSuccess(oXHR){ 
               vat.item.setValueByName("#F.promotionCode", vat.ajax.getValue("PromotionCode", oXHR.responseText));
               vat.item.setValueByName("#F.promotionName", vat.ajax.getValue("PromotionName", oXHR.responseText));
           }
        });
    }else{
        vat.item.setValueByName("#F.promotionName", "")
    }
}

function changeShopCode(posMachineCode){
        vat.ajax.XHRequest(
        {
			post:"process_object_name=imDeliveryMainService"+
            		"&process_object_method_name=findShopMachineByShopCodeForAJAX"+
                	"&shopCode=" + vat.item.getValueByName("#F.shopCode"),                      
			find: function changeShopCodeRequestSuccess(oXHR){
				//var posMachineCode = vat.ajax.getValue("allshopMachine", oXHR.responseText);
           		var allshopMachine = eval(vat.ajax.getValue("allshopMachine", oXHR.responseText));
           		allshopMachine[0][0] = "#F.posMachineCode";
           		allshopMachine[0][1] = posMachineCode;
				vat.item.SelectBind(allshopMachine); 
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

function getOriginalDelivery(){
	vat.item.setValueByName("#F.origDeliveryOrderNo", vat.item.getValueByName("#F.origDeliveryOrderNo").replace(/^\s+|\s+$/, ''))
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=imDeliveryMainService"+
                    "&process_object_method_name=executeCopyOrigDelivery"+
                    "&headId=" + vat.item.getValueByName("#F.headId") + 
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
                    "&origDeliveryOrderTypeCode=" + vat.item.getValueByName("#F.origDeliveryOrderTypeCode") +
                    "&origDeliveryOrderNo=" + vat.item.getValueByName("#F.origDeliveryOrderNo") +
                    "&returnDate=" + vat.item.getValueByName("#F.shipDate"),
                    
        find: function getOriginalDeliverySuccess(oXHR){
        	vat.item.setValueByName("#F.origDeliveryOrderNo",vat.ajax.getValue("OrigDeliveryOrderNo", oXHR.responseText));
            vat.item.setValueByName("#F.returnDate" ,vat.ajax.getValue("OrigShipDate", oXHR.responseText));
            vat.item.setValueByName("#F.shipDate" ,vat.ajax.getValue("ReturnDate", oXHR.responseText));      
            vat.item.setValueByName("#F.customerCode" ,vat.ajax.getValue("CustomerCode", oXHR.responseText));
            vat.item.setValueByName("#F.customerName" ,vat.ajax.getValue("CustomerName", oXHR.responseText));
            vat.item.setValueByName("#F.customerPoNo" ,vat.ajax.getValue("CustomerPoNo", oXHR.responseText));
            vat.item.setValueByName("#F.customsNo" ,vat.ajax.getValue("CustomsNo", oXHR.responseText));
            vat.item.setValueByName("#F.quotationCode" ,vat.ajax.getValue("QuotationCode", oXHR.responseText));
            vat.item.setValueByName("#F.paymentTermCode" ,vat.ajax.getValue("PaymentTermCode", oXHR.responseText));
            vat.item.setValueByName("#F.countryCode" ,vat.ajax.getValue("CountryCode", oXHR.responseText));
            vat.item.setValueByName("#F.currencyCode" ,vat.ajax.getValue("CurrencyCode", oXHR.responseText));
            vat.item.setValueByName("#F.contactPerson" ,vat.ajax.getValue("ContactPerson", oXHR.responseText));
            vat.item.setValueByName("#F.contactTel" ,vat.ajax.getValue("ContactTel", oXHR.responseText));      
            vat.item.setValueByName("#F.receiver" ,vat.ajax.getValue("Receiver", oXHR.responseText));
            vat.item.setValueByName("#F.superintendentCode" ,vat.ajax.getValue("SuperintendentCode", oXHR.responseText));
            vat.item.setValueByName("#F.superintendentName" ,vat.ajax.getValue("SuperintendentName", oXHR.responseText));
            vat.item.setValueByName("#F.invoiceTypeCode" ,vat.ajax.getValue("InvoiceTypeCode", oXHR.responseText));
            vat.item.setValueByName("#F.guiCode" ,vat.ajax.getValue("GuiCode", oXHR.responseText));            
            vat.item.setValueByName("#F.taxType" ,vat.ajax.getValue("TaxType", oXHR.responseText));
            vat.item.setValueByName("#F.taxRate" ,vat.ajax.getValue("TaxRate", oXHR.responseText));
            vat.item.setValueByName("#F.scheduleCollectionDate" ,vat.ajax.getValue("ScheduleCollectionDate", oXHR.responseText));
            vat.item.setValueByName("#F.invoiceCity" ,vat.ajax.getValue("InvoiceCity", oXHR.responseText));
            vat.item.setValueByName("#F.invoiceArea" ,vat.ajax.getValue("InvoiceArea", oXHR.responseText));
            vat.item.setValueByName("#F.invoiceZipCode" ,vat.ajax.getValue("InvoiceZipCode", oXHR.responseText));
            vat.item.setValueByName("#F.invoiceAddress" ,vat.ajax.getValue("InvoiceAddress", oXHR.responseText));
            vat.item.setValueByName("#F.shipCity" ,vat.ajax.getValue("ShipCity", oXHR.responseText));
            vat.item.setValueByName("#F.shipArea" ,vat.ajax.getValue("ShipArea", oXHR.responseText));
            vat.item.setValueByName("#F.shipZipCode" ,vat.ajax.getValue("ShipZipCode", oXHR.responseText));
            vat.item.setValueByName("#F.shipAddress" ,vat.ajax.getValue("ShipAddress", oXHR.responseText));                    
            vat.item.setValueByName("#F.promotionCode" ,vat.ajax.getValue("PromotionCode", oXHR.responseText));
            vat.item.setValueByName("#F.promotionName" ,vat.ajax.getValue("PromotionName", oXHR.responseText));
            vat.item.setValueByName("#F.discountRate" ,vat.ajax.getValue("DiscountRate", oXHR.responseText));
            vat.item.setValueByName("#F.sufficientQuantityDelivery" ,vat.ajax.getValue("SufficientQuantityDelivery", oXHR.responseText));
            vat.item.setValueByName("#F.remark1" ,vat.ajax.getValue("Remark1", oXHR.responseText));          
            vat.item.setValueByName("#F.remark2" ,vat.ajax.getValue("Remark2", oXHR.responseText));
            vat.item.setValueByName("#F.homeDelivery" ,vat.ajax.getValue("HomeDelivery", oXHR.responseText));
            vat.item.setValueByName("#F.paymentCategory" ,vat.ajax.getValue("PaymentCategory", oXHR.responseText));
            vat.item.setValueByName("#F.attachedInvoice" ,vat.ajax.getValue("AttachedInvoice", oXHR.responseText));
            vat.item.setValueByName("#F.exportExchangeRate" ,vat.ajax.getValue("ExportExchangeRate", oXHR.responseText));
            vat.item.setValueByName("#F.exportCommissionRate" ,vat.ajax.getValue("ExportCommissionRate", oXHR.responseText));
            vat.item.setValueByName("#F.casherCode" ,vat.ajax.getValue("CasherCode", oXHR.responseText));
            vat.item.setValueByName("#F.casherName" ,vat.ajax.getValue("CasherName", oXHR.responseText));
            vat.item.setValueByName("#F.departureDate" ,vat.ajax.getValue("DepartureDate", oXHR.responseText));
            vat.item.setValueByName("#F.flightNo" ,vat.ajax.getValue("FlightNo", oXHR.responseText));
            vat.item.setValueByName("#F.passportNo" ,vat.ajax.getValue("PassportNo", oXHR.responseText));
            vat.item.setValueByName("#F.ladingNo" ,vat.ajax.getValue("LadingNo", oXHR.responseText));
            vat.item.setValueByName("#F.transactionSeqNo" ,vat.ajax.getValue("TransactionSeqNo", oXHR.responseText));
            vat.item.setValueByName("#F.salesInvoicePage" ,vat.ajax.getValue("SalesInvoicePage", oXHR.responseText));
            vat.item.setValueByName("#F.transactionTime" ,vat.ajax.getValue("TransactionTime", oXHR.responseText));
            
            vat.item.setValueByName("#F.shopCode" ,vat.ajax.getValue("ShopCode", oXHR.responseText));
            vat.item.setValueByName("#F.defaultWarehouseCode" ,vat.ajax.getValue("DefaultWarehouseCode", oXHR.responseText));
            
            //var shopCodeArray = vat.ajax.getValue("ShopCodeArray", oXHR.responseText);
            //var shopCodes = eval(shopCodeArray);
            //shopCodes[0][0] = "#F.shopCode";
            //shopCodes[0][1] = vat.ajax.getValue("ShopCode", oXHR.responseText);
            //vat.item.SelectBind(shopCodes);
            changeShopCode(vat.ajax.getValue("PosMachineCode", oXHR.responseText));
            //var defaultWarehouseCodeArray = vat.ajax.getValue("DefaultWarehouseCodeArray", oXHR.responseText);
            //var warehouseCodes = eval(defaultWarehouseCodeArray);
            //warehouseCodes[0][0] = "#F.defaultWarehouseCode";
            //warehouseCodes[0][1] = vat.ajax.getValue("DefaultWarehouseCodeArray", oXHR.responseText);
            //vat.item.SelectBind(warehouseCodes);
            
            var findOrigDeliveryResult = vat.ajax.getValue("FindOrigDeliveryResult", oXHR.responseText);
            if("N" == findOrigDeliveryResult){
                alert("查無" + vat.item.getValueByName("#F.origDeliveryOrderTypeCode") + "-" + vat.item.getValueByName("#F.origDeliveryOrderNo") + "的出貨單資料！");
            }else if(findOrigDeliveryResult != null && findOrigDeliveryResult != ""){
                alert(vat.item.getValueByName("#F.origDeliveryOrderTypeCode") + "-" + vat.item.getValueByName("#F.origDeliveryOrderNo") + "的出貨單狀態為" + findOrigDeliveryResult + "無法執行退貨！");
            }
            
			if(vat.item.getValueByName("#F.origDeliveryOrderNo") != null && vat.item.getValueByName("#F.origDeliveryOrderNo") != ""){
  				vat.item.setAttributeByName("vatHeaderDiv","readOnly",true,true,true);
                vat.item.setStyleByName("#B.import"		, "display", "inline");
                vat.item.setGridAttributeByName("itemCode", "readOnly", true);
	            vat.item.setGridAttributeByName("lotNo", "readOnly", true);
	            vat.item.setGridAttributeByName("originalForeignUnitPrice", "readOnly", true);
	            vat.item.setGridAttributeByName("actualForeignUnitPrice", "readOnly", true);
	            vat.item.setGridAttributeByName("watchSerialNo", "readOnly", true);
	            vat.item.setGridAttributeByName("watchSerialNoPicker", "readOnly", true);
	            vat.item.setAttributeByName("#F.shipDate","readOnly",false);
	            vat.item.setAttributeByName("#F.origDeliveryOrderNo","readOnly",false);
	            vat.item.setAttributeByName("#F.reserve1","readOnly",false);
	            vat.block.canGridModify( [vnB_Detail], true, false, false );
            }else{
                vat.item.setAttributeByName("vatHeaderDiv","readOnly",false,true,true);
                vat.item.setStyleByName("#B.import"		, "display", "none");
                vat.item.setGridAttributeByName("itemCode", "readOnly", false);
	            vat.item.setGridAttributeByName("lotNo", "readOnly", false);
	            vat.item.setGridAttributeByName("originalForeignUnitPrice", "readOnly", false);
	            vat.item.setGridAttributeByName("actualForeignUnitPrice", "readOnly", false);
	            vat.item.setGridAttributeByName("watchSerialNo", "readOnly", false);
	            vat.item.setGridAttributeByName("watchSerialNoPicker", "readOnly", false);
	            vat.block.canGridModify( [vnB_Detail], true, true, true );
            }
            doPageRefresh();
       }   
   }); 
}

function getCMHead(){
	vat.item.setValueByName("#F.exportDeclNo", vat.item.getValueByName("#F.exportDeclNo").replace(/^\s+|\s+$/, ''))
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=imDeliveryMainService"+
                    "&process_object_method_name=executeFindCMHead"+
                    "&headId=" + vat.item.getValueByName("#F.headId") + 
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") +
                    "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
                    "&exportDeclNo=" + vat.item.getValueByName("#F.exportDeclNo"),
        find: function getCMHeadSuccess(oXHR){
        vat.item.setValueByName("#F.exportDeclDate" ,vat.ajax.getValue("ExportDeclDate", oXHR.responseText));
        vat.item.setValueByName("#F.exportDeclType" ,vat.ajax.getValue("ExportDeclType", oXHR.responseText));
        doPageRefresh();
        
        if(vat.item.getValueByName("#F.exportDeclNo") != null && vat.item.getValueByName("#F.exportDeclNo") !=""){
            if(vat.ajax.getValue("ExportDeclType", oXHR.responseText) == ""){
        		alert("查無報關單-" + vat.item.getValueByName("#F.exportDeclNo") + "的資料！");
        	}
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

// 供應商picker 回來執行
function doAfterPickerCustomer(){
	if(typeof vat.bean().vatBeanPicker.customerResult != "undefined"){
		//vat.item.setValueByName("#F.searchCustomerType", "customerCode");
    	vat.item.setValueByName("#F.customerCode", vat.bean().vatBeanPicker.customerResult[0].customerCode); 
		changeCustomerCode();
	}
}

function appendBeforeMethod(){
    var origDeliveryOrderNoTmp = vat.item.getValueByName("#F.origDeliveryOrderNo").replace(/^\s+|\s+$/, '');
    if(origDeliveryOrderNoTmp == null || origDeliveryOrderNoTmp == ""){
	    return true;
	}
}


// 傳參數
function doPassData(X, id){
	var suffix = "";
	switch(id){
		case "orderTypeCode":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			suffix += "&orderTypeCode="+escape(orderTypeCode); 
			break;
		case "watchSerialNo":
			var nItemLine = vat.item.getGridLine(X);
			var itemCode = vat.item.getGridValueByName( "itemCode", nItemLine);
			suffix += "&itemCode="+escape(itemCode); 
			break;
	}
	return suffix;
}

function doAfterPickerWatchSerialNo(X){
	if(vat.bean().vatBeanPicker.serialResult !== null){
		var nItemLine = vat.item.getGridLine(X);
		vat.item.setGridValueByName( "watchSerialNo", nItemLine, vat.bean().vatBeanPicker.serialResult[0].serial);
	}
}

function appendAfterMethod(){
    // return alert("新增完畢");
}

function pageLoadSuccess(){
	// alert("載入成功");	
}

function changeTaxRate(){
    if(vat.item.getValueByName("#F.taxType") == "1" || vat.item.getValueByName("#F.taxType") == "2"){
    	vat.item.setValueByName("#F.taxRate", "0.0");
    }else if(vat.item.getValueByName("#F.taxType") == "3"){
	    vat.item.setValueByName("#F.taxRate", "5.0");
    }else{
        vat.item.setValueByName("#F.taxRate", "0.0");
    }
}
function changeItemData(actionId) {
    var nItemLine = vat.item.getGridLine();   
    var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();  
	var vWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
	var vOriginalUnitPrice = vat.item.getGridValueByName("originalUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vOriginalForeignUnitPrice = vat.item.getGridValueByName("originalForeignUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualUnitPrice = vat.item.getGridValueByName("actualUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualForeignUnitPrice = vat.item.getGridValueByName("actualForeignUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vShipQuantity = vat.item.getGridValueByName("shipQuantity", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxType = vat.item.getValueByName("#F.taxType");
	var vTaxRate = vat.item.getValueByName("#F.taxRate");
	var vWatchSerialNo = vat.item.getGridValueByName("watchSerialNo", nItemLine).replace(/^\s+|\s+$/, '');
	
	vat.item.setGridValueByName("itemCode", nItemLine, vItemCode);
    vat.item.setGridValueByName("warehouseCode", nItemLine, vWarehouseCode);	
	vat.ajax.XHRequest(
    {
        post:"process_object_name=imDeliveryMainService" +
                    "&process_object_method_name=getAJAXItemDataForReturn" +
                     "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
                     "&priceType=" + vat.item.getValueByName("#F.priceType") +
                     "&itemIndexNo=" + nItemLine +
                     "&itemCode=" + vItemCode +
                     "&warehouseCode=" + vWarehouseCode +
                     "&originalUnitPrice=" + vOriginalUnitPrice +
                     "&originalForeignUnitPrice=" + vOriginalForeignUnitPrice +
                     "&actualUnitPrice=" + vActualUnitPrice +
                     "&actualForeignUnitPrice=" + vActualForeignUnitPrice +
                     "&returnQuantity=" + vShipQuantity +
                     "&returnDate=" + vat.item.getValueByName("#F.shipDate") +
                     "&taxType=" + vTaxType +
                     "&taxRate=" + vTaxRate +
                     "&watchSerialNo=" + vWatchSerialNo +
                     "&exportExchangeRate=" + vat.item.getValueByName("#F.exportExchangeRate")+
                     "&headId=" + vat.item.getValueByName("#F.headId")+
                     "&actionId=" + actionId,
                                                  
        find: function changeItemDataRequestSuccess(oXHR){                    
            vat.item.setGridValueByName("itemCode", nItemLine, vat.ajax.getValue("ItemCode", oXHR.responseText));	
		    vat.item.setGridValueByName("itemCName", nItemLine, vat.ajax.getValue("ItemCName", oXHR.responseText));
		    vat.item.setGridValueByName("isTax", nItemLine, vat.ajax.getValue("IsTax", oXHR.responseText)); 
            vat.item.setGridValueByName("warehouseCode", nItemLine, vat.ajax.getValue("WarehouseCode", oXHR.responseText)); 
            vat.item.setGridValueByName("warehouseName", nItemLine, vat.ajax.getValue("WarehouseName", oXHR.responseText)); 	    
		    vat.item.setGridValueByName("originalUnitPrice", nItemLine, vat.ajax.getValue("OriginalUnitPrice", oXHR.responseText));
		    vat.item.setGridValueByName("originalForeignUnitPrice", nItemLine, vat.ajax.getValue("OriginalForeignUnitPrice", oXHR.responseText));
		    vat.item.setGridValueByName("actualUnitPrice", nItemLine, vat.ajax.getValue("ActualUnitPrice", oXHR.responseText));		    
		    vat.item.setGridValueByName("actualForeignUnitPrice", nItemLine, vat.ajax.getValue("ActualForeignUnitPrice", oXHR.responseText));
		    vat.item.setGridValueByName("shipQuantity", nItemLine, vat.ajax.getValue("ShipQuantity", oXHR.responseText));
		    vat.item.setGridValueByName("originalShipAmount", nItemLine, vat.ajax.getValue("OriginalShipAmount", oXHR.responseText));
		    vat.item.setGridValueByName("originalForeignShipAmt", nItemLine, vat.ajax.getValue("OriginalForeignShipAmt", oXHR.responseText));
		    vat.item.setGridValueByName("actualShipAmount", nItemLine, vat.ajax.getValue("ActualShipAmount", oXHR.responseText));
		    vat.item.setGridValueByName("actualForeignShipAmt", nItemLine, vat.ajax.getValue("ActualForeignShipAmt", oXHR.responseText));
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
	var vOriginalForeignUnitPrice = vat.item.getGridValueByName("originalForeignUnitPrice", nItemLine).replace(/^\s+|\s+$/, ''); 
	var vActualUnitPrice = vat.item.getGridValueByName("actualUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');	
	var vActualForeignUnitPrice = vat.item.getGridValueByName("actualForeignUnitPrice", nItemLine).replace(/^\s+|\s+$/, '');
	var vReturnableQuantity = vat.item.getGridValueByName("returnableQuantity",nItemLine).replace(/^\s+|\s+$/, '');
	var vShipQuantity = vat.item.getGridValueByName("shipQuantity", nItemLine).replace(/^\s+|\s+$/, '');
	var vOriginalShipAmount = vat.item.getGridValueByName("originalShipAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vOriginalForeignShipAmt = vat.item.getGridValueByName("originalForeignShipAmt", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualShipAmount = vat.item.getGridValueByName("actualShipAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vActualForeignShipAmt = vat.item.getGridValueByName("actualForeignShipAmt", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxType = vat.item.getGridValueByName("taxType", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxRate = vat.item.getGridValueByName("taxRate", nItemLine).replace(/^\s+|\s+$/, '');
	var vTaxAmount = vat.item.getGridValueByName("shipTaxAmount", nItemLine).replace(/^\s+|\s+$/, '');
	var vWatchSerialNo = vat.item.getGridValueByName("watchSerialNo", nItemLine).replace(/^\s+|\s+$/, '');
	var vImportDeclNo = vat.item.getGridValueByName("importDeclNo", nItemLine).replace(/^\s+|\s+$/, '');
	var vImportDeclDate = vat.item.getGridValueByName("importDeclDate", nItemLine).replace(/^\s+|\s+$/, '');
	var vImportDeclType = vat.item.getGridValueByName("importDeclType", nItemLine).replace(/^\s+|\s+$/, '');
	var vImportDeclSeq = vat.item.getGridValueByName("importDeclSeq", nItemLine).replace(/^\s+|\s+$/, '');
	var vLineId = vat.item.getGridValueByName("lineId", nItemLine).replace(/^\s+|\s+$/, '');
	var vOrigDeliveryOrderNo = vat.item.getValueByName("#F.origDeliveryOrderNo").replace(/^\s+|\s+$/, '');
	var returnData = window.showModalDialog(
		"Im_Delivery:returnAdvanceInput:20091019.page"+
		"?headId=" + vat.item.getValueByName("#F.headId") +
		"&brandCode=" + vat.item.getValueByName("#F.brandCode") +
        "&priceType=" + vat.item.getValueByName("#F.brandCode") +
        "&returnDate=" + vat.item.getValueByName("#F.shipDate") +
        "&status=" + vat.item.getValueByName("#F.status") +
        "&itemIndexNo=" + nItemLine +                        
        "&itemCode=" + vItemCode +
        "&warehouseCode=" + vWarehouseCode +
        "&lotNo=" + vLotNo +
        "&originalUnitPrice=" + vOriginalUnitPrice +
        "&originalForeignUnitPrice=" + vOriginalForeignUnitPrice +
        "&actualUnitPrice=" + vActualUnitPrice +
        "&actualForeignUnitPrice=" + vActualForeignUnitPrice +
        "&returnableQuantity=" + vReturnableQuantity +
        "&shipQuantity=" + vShipQuantity +
        "&originalShipAmount=" + vOriginalShipAmount +
        "&originalForeignShipAmt=" + vOriginalForeignShipAmt +
        "&actualShipAmount=" + vActualShipAmount +
        "&actualForeignShipAmt=" + vActualForeignShipAmt +
        "&taxType=" + vTaxType +
        "&taxRate=" + vTaxRate +
        "&taxAmount=" + vTaxAmount +
        "&watchSerialNo=" + vWatchSerialNo +
        "&origDeliveryOrderNo=" + vOrigDeliveryOrderNo +
        "&importDeclNo=" + vImportDeclNo +
        "&importDeclDate=" + vImportDeclDate +
        "&importDeclType=" + vImportDeclType +
        "&importDeclSeq=" + vImportDeclSeq +
        "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
        "&orderNo=" + vat.item.getValueByName("#F.orderNo") +
        "&processId=" + document.forms[0]["#processId"].value +
        "&exportExchangeRate=" + vat.item.getValueByName("#F.exportExchangeRate") +
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
		    vat.item.setGridValueByName("originalForeignUnitPrice", nItemLine, returnDataArray[5]);
		    vat.item.setGridValueByName("originalUnitPrice", nItemLine, returnDataArray[6]);
		    vat.item.setGridValueByName("actualForeignUnitPrice", nItemLine, returnDataArray[7]);
		    vat.item.setGridValueByName("actualUnitPrice", nItemLine, returnDataArray[8]);		    
		    vat.item.setGridValueByName("shipQuantity", nItemLine, returnDataArray[9]);
		    vat.item.setGridValueByName("originalForeignShipAmt", nItemLine, returnDataArray[10]);
		    vat.item.setGridValueByName("originalShipAmount", nItemLine, returnDataArray[11]);
		    vat.item.setGridValueByName("actualForeignShipAmt", nItemLine, returnDataArray[12]);
		    vat.item.setGridValueByName("actualShipAmount", nItemLine, returnDataArray[13]);
		    vat.item.setGridValueByName("shipTaxAmount", nItemLine, returnDataArray[14]);
		    vat.item.setGridValueByName("watchSerialNo", nItemLine, returnDataArray[15]);
		    vat.item.setGridValueByName("importDeclNo", nItemLine, returnDataArray[16]);
		    vat.item.setGridValueByName("importDeclDate", nItemLine, returnDataArray[17]);
		    vat.item.setGridValueByName("importDeclType", nItemLine, returnDataArray[18]);
		    vat.item.setGridValueByName("importDeclSeq", nItemLine, returnDataArray[19]);
		}
	//changeItemData(3);
}

function changeAdvanceData(actionId){
	var vItemIndexNo = document.forms[0]["#itemIndexNo"].value;
	var vItemCode = document.forms[0]["#itemCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();
    var vBrandCode = document.forms[0]["#brandCode"].value;
    var vPriceType = document.forms[0]["#priceType"].value;
    var vWarehouseCode = document.forms[0]["#warehouseCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();
    
    var vTaxType= document.forms[0]["#taxType"].value;
    var vTaxRate= document.forms[0]["#taxRate"].value.replace(/^\s+|\s+$/, '');  
    if(vTaxType == "1" || vTaxType == "2"){
        vTaxRate = "0.0";
    }
    var vOriginalUnitPrice= document.forms[0]["#originalUnitPrice"].value;
    var vOriginalForeignUnitPrice= document.forms[0]["#originalForeignUnitPrice"].value;
    var vActualUnitPrice= document.forms[0]["#actualUnitPrice"].value;
    var vActualForeignUnitPrice= document.forms[0]["#actualForeignUnitPrice"].value;   
    var vShipQuantity = document.forms[0]["#shipQuantity"].value.replace(/^\s+|\s+$/, '');
    var vWatchSerialNo = document.forms[0]["#watchSerialNo"].value.replace(/^\s+|\s+$/, '');
    var vReturnDate= document.forms[0]["#returnDate"].value;
    var vExportExchangeRate= document.forms[0]["#exportExchangeRate"].value;
    
    if(isNaN(vOriginalForeignUnitPrice)){
        alert("原單價必須為數值！");
    }else if(isNaN(vActualForeignUnitPrice)){
        alert("實際單價必須為數值！");
    }else if(isNaN(vShipQuantity)){
        alert("退貨數量必須為數值！");
    }else{   
        vat.ajax.XHRequest(
        {
            post:"process_object_name=imDeliveryMainService" +
                    "&process_object_method_name=getAJAXItemDataForReturn" +
                     "&brandCode=" + vBrandCode +
                     "&priceType=" + vPriceType +
                     "&itemIndexNo" + vItemIndexNo +
                     "&itemCode=" + vItemCode +
                     "&warehouseCode=" + vWarehouseCode +
                     "&originalUnitPrice=" + vOriginalUnitPrice +
                     "&originalForeignUnitPrice=" + vOriginalForeignUnitPrice +
                     "&actualUnitPrice=" + vActualUnitPrice +
                     "&actualForeignUnitPrice=" + vActualForeignUnitPrice +
                     "&returnQuantity=" + vShipQuantity +
                     "&returnDate=" + vReturnDate +
                     "&taxType=" + vTaxType +
                     "&taxRate=" + vTaxRate +
                     "&watchSerialNo=" + vWatchSerialNo +
                     "&exportExchangeRate=" + vExportExchangeRate+
                     "&headId=" + document.forms[0]["#headId"].value+
                     "&actionId=" + actionId,                                                 
            find: function changeAdvanceItemDataRequestSuccess(oXHR){
            	document.forms[0]["#itemCode"].value = vat.ajax.getValue("ItemCode", oXHR.responseText);
            	document.forms[0]["#itemName"].value = vat.ajax.getValue("ItemCName", oXHR.responseText);
            	document.forms[0]["#warehouseCode"].value = vat.ajax.getValue("WarehouseCode", oXHR.responseText);
            	document.forms[0]["#warehouseName"].value = vat.ajax.getValue("WarehouseName", oXHR.responseText);
            	document.forms[0]["#originalUnitPrice"].value = vat.ajax.getValue("OriginalUnitPrice", oXHR.responseText);
            	document.forms[0]["#originalForeignUnitPrice"].value = vat.ajax.getValue("OriginalForeignUnitPrice", oXHR.responseText);
            	document.forms[0]["#actualUnitPrice"].value = vat.ajax.getValue("ActualUnitPrice", oXHR.responseText);
            	document.forms[0]["#actualForeignUnitPrice"].value = vat.ajax.getValue("ActualForeignUnitPrice", oXHR.responseText);
            	document.forms[0]["#shipQuantity"].value = vat.ajax.getValue("ShipQuantity", oXHR.responseText);
            	document.forms[0]["#originalShipAmount"].value = vat.ajax.getValue("OriginalShipAmount", oXHR.responseText);
            	document.forms[0]["#originalForeignShipAmt"].value = vat.ajax.getValue("OriginalForeignShipAmt", oXHR.responseText);
            	document.forms[0]["#actualShipAmount"].value = vat.ajax.getValue("ActualShipAmount", oXHR.responseText);
            	document.forms[0]["#actualForeignShipAmt"].value = vat.ajax.getValue("ActualForeignShipAmt", oXHR.responseText);
            	document.forms[0]["#taxAmount"].value = vat.ajax.getValue("TaxAmount", oXHR.responseText);
            	document.forms[0]["#watchSerialNo"].value = vat.ajax.getValue("WatchSerialNo", oXHR.responseText);
            }
        });
    }
}

function advanceDataSave(){
    var vItemCode = document.forms[0]["#itemCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();;
    var vItemName = document.forms[0]["#itemName"].value.replace(/^\s+|\s+$/, '');
    var vWarehouseCode = document.forms[0]["#warehouseCode"].value.replace(/^\s+|\s+$/, '').toUpperCase();;
    var vWarehouseName = document.forms[0]["#warehouseName"].value.replace(/^\s+|\s+$/, '');  
    var vLotNo = document.forms[0]["#lotNo"].value.replace(/^\s+|\s+$/, '');
    var vOriginalForeignUnitPrice = document.forms[0]["#originalForeignUnitPrice"].value;
    var vOriginalUnitPrice = document.forms[0]["#originalUnitPrice"].value;
    var vActualForeignUnitPrice = document.forms[0]["#actualForeignUnitPrice"].value.replace(/^\s+|\s+$/, ''); 
    var vActualUnitPrice = document.forms[0]["#actualUnitPrice"].value.replace(/^\s+|\s+$/, '');
    var vShipQuantity = document.forms[0]["#shipQuantity"].value;   
    var vOriginalForeignShipAmt = document.forms[0]["#originalForeignShipAmt"].value;
	var vOriginalShipAmount = document.forms[0]["#originalShipAmount"].value;
    var vActualForeignShipAmt = document.forms[0]["#actualForeignShipAmt"].value.replace(/^\s+|\s+$/, '');
    var vActualShipAmount = document.forms[0]["#actualShipAmount"].value.replace(/^\s+|\s+$/, ''); 
    var vTaxAmount= document.forms[0]["#taxAmount"].value;
    var vWatchSerialNo= document.forms[0]["#watchSerialNo"].value;
    var vImportDeclNo= document.forms[0]["#importDeclNo"].value;
    var vImportDeclDate= document.forms[0]["#importDeclDate"].value;
    var vImportDeclType= document.forms[0]["#importDeclType"].value;
    var vImportDeclSeq= document.forms[0]["#importDeclSeq"].value;
    
    if(isNaN(vOriginalForeignUnitPrice)){
        alert("原單價必須為數值！");
    }else if(isNaN(vActualForeignUnitPrice)){
        alert("實際單價必須為數值！");
    }else if(isNaN(vShipQuantity)){
        alert("退貨數量必須為數值！");
    }else{       
    window.returnValue =vItemCode + "{#}" + vItemName + "{#}" + vWarehouseCode + "{#}" + vWarehouseName + "{#}" +
         				vLotNo + "{#}" + vOriginalForeignUnitPrice + "{#}" + vOriginalUnitPrice + "{#}" + 
         				vActualForeignUnitPrice + "{#}" + vActualUnitPrice + "{#}" + vShipQuantity + "{#}" + 
         				vOriginalForeignShipAmt + "{#}" + vOriginalShipAmount + "{#}" + vActualForeignShipAmt + "{#}" + 
         				vActualShipAmount + "{#}" + vTaxAmount + "{#}" + vWatchSerialNo + "{#}" + vImportDeclNo + "{#}" +
         				vImportDeclDate + "{#}" + vImportDeclType + "{#}" + vImportDeclSeq;
        window.close();  
    }
}

function changeRelationData(){
    var statusTmp = vat.item.getValueByName("#F.status");
    var orderNoTmp = vat.item.getValueByName("#F.orderNo");
    var processId = document.forms[0]["#processId"].value;
    if((statusTmp == "SAVE" && orderNoTmp.indexOf("TMP") != -1) || ((statusTmp == "SAVE" || statusTmp == "REJECT") && processId != null && processId != 0)){      
        afterSavePageProcess = "changeRelationData";
		doPageDataSave();	
	}  
}

function changeExchangeRate(){
    vat.ajax.XHRequest(
       {
           post:"process_object_name=soSalesOrderMainService"+
                    "&process_object_method_name=getExchangeRate"+
                    "&currencyCode=" + vat.item.getValueByName("#F.currencyCode"),
           find: function changeExchangeRateRequestSuccess(oXHR){ 
               vat.item.setValueByName("#F.exportExchangeRate" ,vat.ajax.getValue("ExportExchangeRate", oXHR.responseText));
           }   
       });
}

function exportFormData(){
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IR_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=imDeliveryMainService" + 
              "&processObjectMethodName=findImDeliveryHeadById" + 
              "&gridFieldName=imDeliveryLines" + 
              "&arguments=" + vat.item.getValueByName("#F.headId") + 
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
        "&processObjectName=imDeliveryMainService" + 
        "&processObjectMethodName=executeImportItems" +
        "&arguments=" + vat.item.getValueByName("#F.headId") + "{$}"
                      + vat.item.getValueByName("#F.shipDate") + "{$}" 
                      + vat.item.getValueByName("#F.priceType") + "{$}"
                      + vat.item.getValueByName("#F.taxType") + "{$}" 
                      + vat.item.getValueByName("#F.taxRate") + "{$}"
                      + vat.item.getValueByName("#F.discountRate") +"{$}"
                      + vat.item.getValueByName("#F.exportExchangeRate") +   
        "&parameterTypes=LONG{$}DATE{$}STRING{$}STRING{$}DOUBLE{$}DOUBLE{$}DOUBLE" +
        "&blockId=" + vnB_Detail,
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function execSubmitAction(actionId){
    var formId = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var processId = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var status = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
    var isCommitOnHand = document.forms[0]["#isCommitOnHand"].value.replace(/^\s+|\s+$/, '');
    var approvalResult        = vat.item.getValueByName("#F.approvalResult");
    if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
    
    var formStatus = status;
    
    if("SAVE" == actionId){
        formStatus = "SAVE";
    }if("UNCONFIRMED" == actionId){
        formStatus = "UNCONFIRMED";
    }else if("SUBMIT" == actionId){
        formStatus = changeFormStatus(formId, processId, status, approvalResult);
    }else if("SUBMIT_BG" == actionId){
        formStatus = "SIGNING";
    }else if("VOID" == actionId){
        formStatus = "VOID";
    }
	vat.bean().vatBeanOther.approvalResult = approvalResult;
	vat.bean().vatBeanOther.approvalComment = approvalComment;
	vat.bean().vatBeanOther.beforeChangeStatus = status;
	vat.bean().vatBeanOther.formStatus = formStatus;
	vat.bean().vatBeanOther.employeeCode = document.forms[0]["#loginEmployeeCode"].value; 
	if(isCommitOnHand == "Y" && vat.item.getValueByName("#F.receiptDate") == "" && approvalResult == "true"){
    	alert('請輸入入庫日期');
    }else{
	    if("SUBMIT_BG" == actionId){
	        vat.block.submit(function(){return "process_object_name=soSalesOrderReturnMainAction"+
	            "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
	    }else{
	        vat.block.submit(function(){return "process_object_name=soSalesOrderReturnMainAction"+
	            "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
	    }
    }
}

function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == ""){
        formStatus = "SIGNING";
    }else if(processId != null && processId != "" && processId != 0){
        if(status == "SAVE" || status == "REJECT" || status == "UNCONFIRMED"){
            formStatus = "SIGNING";
        }else if(status == "SIGNING"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }  
    }else{
    	formStatus = "FINISH";
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
        "&processObjectName=imDeliveryMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=soSalesOrderReturnMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    doPageDataSave();
}

/*
	refreshForm
*/
function refresh(){
	refreshForm('')
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

function refreshForm(vsHeadId){
	document.forms[0]["#formId"].value = vsHeadId;
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.block.submit(
		function(){
			return "process_object_name=imDeliveryMainService&process_object_method_name=executeReturnInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     		             vat.item.bindAll();
     		             vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     	}});
}

function doFormAccessControl(){
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var status = vat.item.getValueByName("#F.status");
	var processId = document.forms[0]["#processId"].value;
	var orderNo = vat.item.getValueByName("#F.orderNo");
	var allowApproval 	= document.forms[0]["#allowApproval"].value;
		
	vat.item.setStyleByName("#B.new"		, "display", "inline");
	vat.item.setStyleByName("#B.submit"		, "display", "inline");
	vat.item.setStyleByName("#B.submitBG"	, "display", "inline");
	vat.item.setStyleByName("#B.save"		, "display", "inline");
	vat.item.setStyleByName("#B.message"	, "display", "inline");
	vat.item.setStyleByName("#B.export"		, "display", "inline");
	vat.item.setStyleByName("#B.import"		, "display", "inline");
	vat.item.setStyleByName("#B.print"		, "display", "inline");
	
	//for 儲位用
	if(enableStorage){
		vat.item.setStyleByName("#B.storageExport"   , "display", "inline");
		vat.item.setStyleByName("#B.storageImport"   , "display", "inline");
	}else{
		vat.item.setStyleByName("#B.storageExport"   , "display", "none");
		vat.item.setStyleByName("#B.storageImport"   , "display", "none");
	}
	
	vat.item.setAttributeByName("vatHeaderDiv","readOnly",false,true,true);
	vat.item.setAttributeByName("vatMasterDiv","readOnly",false,true,true);
	vat.item.setAttributeByName("vatPosDiv","readOnly",false,true,true);
	vat.block.canGridModify( [vnB_Detail], true, true, true );
	
	vat.item.setStyleByName("#B.void"		, "display", "none");
	//vat.item.setStyleByName("#B.uncomfirm"	, "display", "none");
	
  	if("ERF" == orderTypeCode){
  		vat.item.setAttributeByName("#F.origDeliveryOrderNo","readOnly",true);
  		vat.item.setAttributeByName("#F.origDeliveryOrderTypeCode","readOnly",true);
		vat.item.setGridAttributeByName("shipQuantity", "readOnly", true);
  	}
  	
  	if(processId != ""){
  		vat.tabm.displayToggle(0, "xTab5", true, false, false);
  		if("SIGNING" == status || "VOID" == status || "CLOSE" == status || "FINISH" == status || "CLOSE" == status){
	  		vat.item.setStyleByName("#B.new"		, "display", "none");
	  		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
	  		vat.item.setStyleByName("#B.save"		, "display", "none");
	  		vat.item.setStyleByName("#B.message"	, "display", "none");
	  		vat.item.setStyleByName("#B.import"		, "display", "none");
	  		//for 儲位用
			if(enableStorage){
				vat.item.setStyleByName("#B.storageImport"   , "display", "none");
			}
	  		vat.item.setAttributeByName("vatHeaderDiv","readOnly",true,true,true);
	  		vat.item.setAttributeByName("vatMasterDiv","readOnly",true,true,true);
	  		vat.item.setAttributeByName("vatPosDiv","readOnly",true,true,true);
	  		vat.block.canGridModify( [vnB_Detail], false, false, false );
  		}else if("SAVE" == status || "REJECT" == status || "UNCONFIRMED" == status){
  			vat.item.setStyleByName("#B.new"		, "display", "none");
  			vat.item.setStyleByName("#B.void"		, "display", "inline");
  			if(vat.item.getValueByName("#F.origDeliveryOrderNo") != null && vat.item.getValueByName("#F.origDeliveryOrderNo") != ""){
				vat.item.setAttributeByName("vatHeaderDiv","readOnly",true,true,true);
		        vat.item.setStyleByName("#B.import", "display", "none");
		        //for 儲位用
				if(enableStorage){
					vat.item.setStyleByName("#B.storageImport"   , "display", "none");
				}
		        vat.item.setGridAttributeByName("itemCode", "readOnly", true);
		        vat.item.setGridAttributeByName("lotNo", "readOnly", true);
		        vat.item.setGridAttributeByName("originalForeignUnitPrice", "readOnly", true);
		        vat.item.setGridAttributeByName("actualForeignUnitPrice", "readOnly", true);
		        vat.item.setGridAttributeByName("watchSerialNo", "readOnly", true);
		        vat.item.setGridAttributeByName("watchSerialNoPicker", "readOnly", true);
		        vat.item.setAttributeByName("#F.shipDate","readOnly",false);
		        vat.item.setAttributeByName("#F.origDeliveryOrderNo","readOnly",false);
		        vat.item.setAttributeByName("#F.reserve1","readOnly",false);
		        vat.block.canGridModify( [vnB_Detail], true, false, false );
			}
  		}
  	}else{
  		if(orderNo.indexOf("TMP") != -1){
  			
  		}else{
  		  	var statusHidden = vat.item.getValueByName("#F.customsStatusHidden");
			var cusStatusHidden = statusHidden.substring(0, 1);
  			if((orderTypeCode == "IRP"||orderTypeCode == "IBT") &&( status == "FINISH" || status == "CLOSE")&&(cusStatusHidden !== "N")){

						vat.item.setStyleByName("#B.sendCustoms"		, "display", "inline");
			}
			else{
				vat.item.setStyleByName("#B.sendCustoms"		, "display", "none");
			}
	  		vat.item.setStyleByName("#B.new"		, "display", "none");
	  		vat.item.setStyleByName("#B.submit"		, "display", "none");
	  		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
	  		vat.item.setStyleByName("#B.save"		, "display", "none");
	  		vat.item.setStyleByName("#B.message"	, "display", "none");
	  		vat.item.setStyleByName("#B.import"		, "display", "none");
	  		//for 儲位用
			if(enableStorage){
				vat.item.setStyleByName("#B.storageImport"   , "display", "none");
			}
	  		vat.item.setAttributeByName("vatHeaderDiv","readOnly",true,true,true);
	  		vat.item.setAttributeByName("vatMasterDiv","readOnly",true,true,true);
	  		vat.item.setAttributeByName("vatPosDiv","readOnly",true,true,true);
	  		vat.block.canGridModify( [vnB_Detail], false, false, false );
	  		vat.tabm.displayToggle(0, "xTab5", true, false, false);
  		}
  	}
  	
  	if(processId == "" && "ERF" == orderTypeCode && "FINISH" == status){
		vat.item.setStyleByName("#B.submit"		, "display", "inline");
		vat.item.setAttributeByName("#F.latestExportDeclNo","readOnly",false);
  		vat.item.setAttributeByName("#F.latestExportDeclDate","readOnly",false);
  		vat.item.setAttributeByName("#F.latestExportDeclType","readOnly",false);
	}
	
	    
	if(document.forms[0]["#isCommitOnHand"].value == "Y"){
		vat.item.setAttributeByName("#F.receiptDate","readOnly",false);
	}
	
	//vat.item.setStyleByName("#B.sendCustoms"		, "display", "none");
	
	if(allowApproval == "N" || processId == "" || "SAVE" == status || "REJECT" == status)
    	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
    checkCustomsStatus();
	vat.block.pageRefresh(vnB_Detail);
}

function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
        vat.item.setAttributeByName("vatHeaderDiv","readOnly",false,true,true);
        vat.item.setAttributeByName("vatHeaderDiv","readOnly",false,true,true);
        vat.item.setStyleByName("#B.import"		, "display", "none");
        //for 儲位用
		if(enableStorage){
			vat.item.setStyleByName("#B.storageImport"   , "display", "none");
		}
        vat.item.setGridAttributeByName("itemCode", "readOnly", false);
        vat.item.setGridAttributeByName("lotNo", "readOnly", false);
        vat.item.setGridAttributeByName("originalForeignUnitPrice", "readOnly", false);
        vat.item.setGridAttributeByName("actualForeignUnitPrice", "readOnly", false);
        vat.item.setGridStyleByName("isDeleteRecord", "visibility", "visible");
       	refreshForm('');
	 }
}

// 上傳海關
function updateCustomsStatus(tranStatus){
	var statusHidden = vat.item.getValueByName("#F.customsStatusHidden");
	var cusStatusHidden = statusHidden.substring(0, 1);

		if(confirm("是否要送出?")){
			if(cusStatusHidden !== "N"){
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
							return "process_object_name=imDeliveryMainService&process_object_method_name=updateCustomsStatus"; 
		    		},{link:true, other: true,
		    			funcSuccess:function(){
						window.top.close();
						}
	    			}
	    		);	
	    	}else{
	    		alert("資料已上傳海關，請勿重複上傳");
	    	}
		}
		
}