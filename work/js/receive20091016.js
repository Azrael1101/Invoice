//將共用JS的DEBUG關閉
vat.debug.disable();
//設afterSavePageProcess = 空值
var afterSavePageProcess = "";
//
var afterSaveExpensePageProcess = "";

//定義區段 全域變數
var vnB_Button     = 0; //此區段vnB_Button 值為0
var vatBlock_Head  = 1; //此區段vatBlock_Head 值為1
var vatMasterDiv   = 2;	//此區段vatMasterDiv 值為2
var vatDetailDiv   = 3; 
var vatExpenseDiv  = 4;
var vatAmountDiv   = 5;
var vatApprovalDiv = 6;

//for 儲位用
var vatStorageDetail = 202;
var enableStorage = false;

/* call from cEAP */
//
function outlineBlock(){//重CEAP VatBlockDiv-HTML進來 主要函數
	//初始化
	formDataInitial();
	//畫出按鈕列	
	buttonLine();
	//畫出單頭 
 	headerInitial();
 	var userType = vat.bean("userType"); //定義CEAP傳入的userType 
 	var typeCode = vat.bean("typeCode"); //定義CEAP傳入的typeCode 
 	if(typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
	    vat.tabm.createTab(0, "vatTabSpan", "L", "float");                                                                                                                                   
		vat.tabm.createButton(0, "xTab1", "主檔資料", "vatMasterDiv",   "images/tab_master_data_dark.gif",   "images/tab_master_data_light.gif"); 
		vat.tabm.createButton(0, "xTab2", "明細資料", "vatDetailDiv",   "images/tab_detail_data_dark.gif",   "images/tab_detail_data_light.gif");
		vat.tabm.createButton(0, "xTab3", "其他費用", "vatExpenseDiv",  "images/tab_other_expense_dark.gif", "images/tab_other_expense_light.gif", ((userType=="WAREHOUSE" || userType=="WAREHOUSE2" || typeCode=="RR") ?  "none" : "inline" ));
 		vat.tabm.createButton(0, "xTab4", "金額統計", "vatAmountDiv",   "images/tab_total_amount_dark.gif",  "images/tab_total_amount_light.gif",  "inline", "showTotalCountPage()");	//(userType!="WAREHOUSE" ? "inline" : "none" )
 		vat.tabm.createButton(0, "xTab5", "簽核資料", "vatApprovalDiv", "images/tab_approval_data_dark.gif", "images/tab_approval_data_light.gif", (document.forms[0]["#processId"].value!="" ? "inline" : "none" ));
		
		//for 儲位用
 		enableStorage = "T2" == document.forms[0]["#loginBrandCode"    ].value;
		if(enableStorage){
  			vat.tabm.createButton(0, "xTab6", "儲位資料", "vatStorageDiv", "images/tab_storage_detail_dark.gif", "images/tab_storage_detail_light.gif", "", "reloadStorageDetail()");
  		}
  	}
	
    masterInitial();
    detailInitial();
  	expenseInitial();
 	amountInitial();
 	//簽核資料帶入品牌 單別 單號 登入人員 
	kweWfBlock( vat.item.getValueByName("#F.brandCode"), 
            	vat.item.getValueByName("#F.orderTypeCode"), 
             	vat.item.getValueByName("#F.orderNo"),
             	document.forms[0]["#loginEmployeeCode"].value );

	//for 儲位用
	if(enableStorage){
		kweStorageBlock();
	}

	
    doFormAccessControl();
}


/* initial */
function formDataInitial(){ // document.forms[0]對應頁面上的form標籤 
	//typeof 判斷此變數loginBrandCode 是否有塞入值  若取不到值則undefined
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#formId"].value != '[binding]'){
        //定義otherbean為以下頁面帶回的值
        vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"].value,
  	     userType           : document.forms[0]["#userType"].value, 
	     formId             : document.forms[0]["#formId"].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	    };
	    //定義init(function)為process_object_name=imReceiveMainAction&process_object_method_name=performInitial
        vat.bean.init(function(){
		return "process_object_name=imReceiveMainAction&process_object_method_name=performInitial"; 
        },{other: true});
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

/* initial after Query */
function refreshForm(vsHeadId){   
	document.forms[0]["#formId"].value        = vsHeadId;	
	vat.bean().vatBeanOther.loginBrandCode    = document.forms[0]["#loginBrandCode"].value;	
  	vat.bean().vatBeanOther.loginEmployeeCode = document.forms[0]["#loginEmployeeCode"].value;	  
  	vat.bean().vatBeanOther.orderTypeCode     = document.forms[0]["#orderTypeCode"].value;  
  	vat.bean().vatBeanOther.userType          = document.forms[0]["#userType"].value;   
	vat.bean().vatBeanOther.formId            = document.forms[0]["#formId"].value;
	orderTypeCode = document.forms[0]["#orderTypeCode"].value; 
		

	
	
	vat.block.submit(
		function(){
			return "process_object_name=imReceiveMainAction&process_object_method_name=performInitial";  
     	},{other: true, 
     	   funcSuccess:function(){
     	   		if(orderTypeCode=="IRF" || orderTypeCode=="EIF"){
					var allLCList    = vat.bean("allLCList");
					var allLCList1   = vat.bean("allLCList");
					allLCList[0][0]  = "#F.lcNo";
					allLCList1[0][0] = "#F.lcNo1";
					vat.item.SelectBind(allLCList); 
					vat.item.SelectBind(allLCList1);
				}
				vat.item.bindAll();
				doFormAccessControl();
     	  }}
    );
}

/* function button */
function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
	    
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new",			type:"IMG",     value:"新增",  src:"./images/button_create.gif",         eClick:"createNewForm()"},
	 			//{name:"SPACE",            type:"LABEL",   value:"　"},
	 			{name:"#B.search" ,		type:"PICKER",  value:"查詢",  src:"./images/button_find.gif", 
	 									  openMode:"open", 
										  servicePassData:function()
										  	{return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode")},
	 									  service:"Im_Receive:search:20091102.page",
	 									  left:0, right:0, width:1024, height:768,	
	 									  serviceAfterPick:function(){doAfterPickerProcess()}},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 	 		{name:"#B.exit",		type:"IMG",   value:"離開",    src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
	 	 		//{name:"SPACE",            type:"LABEL", value:"　"},
	 			
	 			{name:"#B.save",		type:"IMG",   value:"暫存",    src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.void",		type:"IMG",   value:"作廢",    src:"./images/button_void.gif", eClick:'doSubmit("VOID")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"}, 	
	 			{name:"#B.submitBg",	type:"IMG",   value:"背景送出", src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.saveDate",	type:"IMG",   value:"日期存檔", src:"./images/button_save_date.gif",  eClick:'updateAjaxDate()'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.message",		type:"IMG",   value:"訊息提示", src:"./images/button_message_prompt.gif",  eClick:'showMessage()'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.expense",		type:"BUTTON",   value:"分攤費用", src:"./images/button_form_print.gif", eClick:'doSubmit("EXPENSE")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.produceIC",	type:"IMG",   value:"單據列印",  src:"./images/button_form_print.gif", eClick:'openReportWindow()'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.listReport1",	type:"BUTTON",   value:"費用單列印", src:"./images/button_form_print.gif", eClick:'listReport("1")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.listReport2",	type:"BUTTON",   value:"驗收單列印", src:"./images/button_form_print.gif", eClick:'listReport("2")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.listReport3",	type:"BUTTON",   value:"出貨差異列印", src:"./images/button_receive_difference_reprot.gif", eClick:'listReport("3")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.listReport4",	type:"IMG",   value:"進貨差異列印", src:"./images/button_receive_difference_reprot.gif", eClick:'listReport("4")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.listReport5",	type:"BUTTON",   value:"效期驗收表列印", src:"./images/button_receive_difference_reprot.gif", eClick:'listReport("5")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.export",		type:"IMG",   value:"明細匯出",  src:"./images/button_detail_export.gif", eClick:'doSubmit("EXPORT")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.import",		type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  , 
	 									 openMode:"open", 
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,	
	 									 serviceAfterPick:function(){afterImportSuccess()}},
				{name:"SPACE",            type:"LABEL", value:"　"},	 									 
	 			{name:"#B.sendCustoms" 	   , type:"IMG"    ,value:"上傳海關",   src:"./images/send_customs1.jpg", eClick:'updateCustomsStatus("")'},
	 			{name:"#B.sendCancel" 	   , type:"IMG"    ,value:"註銷上傳",   src:"./images/send_customs3.jpg", eClick:'updateCustomsStatus("cancel")'},
	 			{name:"SPACE",            type:"LABEL", value:"　"},
	 			//百貨舊條碼
	 			{name:"#B.barcode1",	type:"IMG",   value:"列印條碼",  src:"./images/button_barcode_print.gif",  eClick:'doBarcode(1)'},
	 			{name:"SPACE",            type:"LABEL", value:"　"},
	 			
	 			//手錶用條碼
	 			{name:"#B.barcode2",	type:"IMG",   value:"列印條碼",  src:"./images/button_barcode_print.gif",  eClick:'doBarcode(2)'},
	 			{name:"SPACE",            type:"LABEL", value:"　"},
	 			
	 			//百貨新條碼
	 			{name:"#B.barcode3",	type:"IMG",   value:"列印條碼",  src:"./images/button_barcode_print.gif",  eClick:'doBarcode(3)'},
	 			{name:"SPACE",			type:"LABEL", value:"　"},

	 			//免稅自營珠寶條碼
	 			{name:"#B.barcode4",	type:"IMG",   value:"列印條碼",  src:"./images/button_barcode_print.gif",  eClick:'doBarcode(4)'},
	 			{name:"SPACE",			type:"LABEL", value:"　"},
	 			{name:"SPACE",			type:"LABEL", value:"　"},
	 			{name:"SPACE",			type:"LABEL", value:"　"},
	 			{name:"SPACE",			type:"LABEL", value:"　"},
	 			{name:"SPACE",			type:"LABEL", value:"　"},
	 			{name:"SPACE",			type:"LABEL", value:"　"},
	 			//for 儲位用
	 			{name:"#B.storageExport", 	type:"IMG"    ,value:"儲位匯出",   src:"./images/button_storage_export.gif" , eClick:'exportStorageFormData()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.storageImport",	type:"PICKER" , value:"儲位匯入",  src:"./images/button_storage_import.gif"  , 
						 openMode:"open", 
						 service:"/erp/fileUpload:standard:2.page",
						 servicePassData:function(x){ return importStorageFormData(); },
						 left:0, right:0, width:600, height:400,	
						 serviceAfterPick:function(){}},
				{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.submit",		type:"IMG",   value:"送出",    src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			//{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.importDeclaration", type:"BUTTON", value:"匯入報單明細", eClick:'importData()'},
	 			{name:"#B.importPoLine",type:"PICKER", value:"取採購明細資料", src:"./images/button_obtain_po.gif",
	 									      	openMode:"open", 
	 									      	service:"Fi_Invoice:search:20091210_PoL.page", left:0, right:0, width:1024, height:768,	
										      	servicePassData:function()
										  	  		{return "&orderTypeCode="+document.forms[0]["#orderTypeCode"].value+
										      				"&defaultWarehouseCode=" +vat.item.getValueByName("#F.defaultWarehouseCode")+
										  					"&supplierCode=" +vat.item.getValueByName("#F.supplierCode")+
										  		   	 		"&brandCode="    +vat.item.getValueByName("#F.brandCode")+
										  		    		"&headId="       +vat.item.getValueByName("#F.headId")+
										  		    		"&currencyCode=" +vat.item.getValueByName("#F.currencyCode")+
										  		    		"&exchangeRate=" +vat.item.getValueByName("#F.exchangeRate")+
										  		    		"&formName=ImReceive"},
										  		serviceBeforePick:function(){},
	 									  		serviceAfterPick:function(){doPickerWithPoPurchase()}},
	 			{name:"#B.extendItem",	type:"IMG",   value:"取報單明細",  src:"./images/button_declaration.gif", eClick:'doSubmit("EXTEND")'},
	 			//{name:"#B.deleteImStorageItems", 	type:"IMG"    ,value:"儲位匯出",   src:"./images/del_detail.png" , eClick:'deleteImStorageItems()'},
	 			{name:"SPACE",			type:"LABEL", value:"　"},
	 			{name:"#B.first",		type:"IMG",   value:"第一筆",   src:"./images/play-first.png",   eClick:"gotoFirst()"},
	 			{name:"#B.forward",		type:"IMG",   value:"上一筆",   src:"./images/play-back.png",    eClick:"gotoForward()"},
	 			{name:"#B.next",		type:"IMG",   value:"下一筆",   src:"./images/play.png",         eClick:"gotoNext()"},
	 			{name:"#B.last",		type:"IMG",   value:"最後一筆",  src:"./images/play-forward.png", eClick:"gotoLast()"},
	 			{name:"#L.currentRecord",type:"NUMM",  bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE",			type:"LABEL", value:" / "},
	 			{name:"#L.maxRecord",	type:"NUMM",  bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}


/* form head data */
function headerInitial(){
	var allOrderType    = vat.bean("allOrderType"); //對照出始化 "類別"下拉值
	var allPoOrderType  = vat.bean("allPoOrderType");
  	var typeCode        = vat.bean("typeCode");
	var orderTypeCode   = document.forms[0]["#orderTypeCode"].value;
	var labelOrderDate = "日期<font color='red'>*</font>";
	var lableDefaultNo = "預設採購單號";
	var labelNo        = "預設採購單別";
	var labelInDate    = "進倉日期";
	var displayF       = "HIDDEN";
	var displayP       = "READONLY";
	if (orderTypeCode=="IRF" || orderTypeCode=="EIF" || orderTypeCode=="EOF"){
		labelOrderDate = "日期<font color='red'>*</font>";
		lableDefaultNo = "預設 Invoice No.";
		labelNo        = "報單單號";
		displayF       = "";
		displayP       = "HIDDEN";
		if(typeCode=="RR")			// 進貨退回
			labelOrderDate = "日期<font color='red'>*</font>";
	}
	
	if(orderTypeCode=="IRF" || orderTypeCode=="IRL"){
		labelOrderDate = "歸帳日期<font color='red'>*</font>";
	}
	
	var titleString = "進貨作業";
	if(typeCode=="RR"){			// 進貨退回
		titleString = "進貨退出作業";
		labelInDate = "出倉日期";
	}

	vat.block.create(vatBlock_Head, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:titleString, rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.OrderTypeCode",    type:"LABEL",  value:"類別<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.orderTypeCode",    type:"SELECT", bind:"orderTypeCode",    size:10, mode:"READONLY", init:allOrderType }]},
			{items:[{name:"#L.OrderNo",          type:"LABEL",  value:"單號<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.orderNo",          type:"TEXT",   bind:"orderNo",          size:20,    back:false, mode:"READONLY"},
	 				{name:"#F.headId",           type:"TEXT",   bind:"headId",           back:false, mode:"READONLY" }
					//for 儲位用
					,{name:"#F.storageHeadId",   type:"TEXT",   bind:"storageHeadId",    back:false, mode:"READONLY" }
	 				]},
	 		{items:[{name:"#L.OrderDate",        type:"LABEL",  value:labelOrderDate}]},
	 		{items:[{name:"#F.orderDate",        type:"DATE",   bind:"orderDate",        size:10,    eChange:"onChangeSetExchangeRate()" }]},
			{items:[{name:"#L.BrandCode",        type:"LABEL",  value:"品牌"}]},
	 		{items:[{name:"#F.brandCode",        type:"TEXT",   bind:"brandCode",        size:8,     mode:"HIDDEN"},
	 				{name:"#F.brandName",        type:"TEXT",   bind:"brandName",        back:false, mode:"READONLY"}]},
			{items:[{name:"#L.Status",           type:"LABEL",  value:"狀態"}]},	
			{items:[{name:"#F.status",           type:"TEXT",   bind:"status",           size:12,    mode:"HIDDEN"},
			        {name:"#F.warehouseStatus",	 type:"TEXT",   bind:"warehouseStatus",  size:12,    mode:"HIDDEN"},
	  				{name:"#F.statusName",       type:"TEXT",   bind:"statusName",       back:false, mode:"READONLY"},
		         	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
		         	{name:"#F.customsDesc"      , type:"TEXT"  , size:20 , mode:"READONLY"},
		 			{name:"#F.tranRecordStatus"  , type:"TEXT"  ,  bind:"tranRecordStatus", mode:"HIDDEN"},
		 			{name:"#F.tranAllowUpload"  , type:"TEXT"  ,  bind:"tranAllowUpload", mode:"HIDDEN"},
		 			{name:"#F.customsStatus", 				size:20,type:"TEXT", mode:"READONLY"},
		 			{name:"#F.customsStatusHidden", 				type:"TEXT",  bind:"customsStatus", mode:"HIDDEN"}
	  				
	  				]}]},	
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.SupplierCode",     type:"LABEL",  value:"廠商代號<font color='red'>*</font>"}]},
			{items:[{name:"#F.supplierCode",     type:"TEXT",   bind:"supplierCode",     size:12, mask:"CCCCCCCCCCCCCCCCCCCC", eChange:"onChangeSupplierCode('HEAD')" },
					{name:"#B.supplierCode",	 type:"PICKER", value:"PICKER", src:"./images/start_node_16.gif",
	 									 		 openMode:"open", 
	 									 		 service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 		 left:0, right:0, width:1024, height:768,	
	 									 		 serviceAfterPick:function(X){doAfterPickerSupplier(X,'HEAD');}},
	 				{name:"#L.supplierName",     type:"LABEL",  value:"<br>"},
	 		 		{name:"#F.supplierName",     type:"TEXT",   bind:"supplierName",     size:20, mode:"READONLY" }]},
	 		{items:[{name:"#L.ActualPayDate",    type:"LABEL",  value:"實際付款日"}]},
	 		{items:[{name:"#F.actualPayDate",    type:"DATE",   bind:"actualPayDate",    size:10 }]},
	 		{items:[{name:"#L.DefaultNo",        type:"LABEL",  value:lableDefaultNo}]},	 
	 		{items:[{name:"#F.defPoOrderNo",     type:"TEXT",   bind:"defPoOrderNo",     size:12, eChange:"onChangeDefPoOrderNo()"}]},
	 		{items:[{name:"#L.CreatedBy",        type:"LABEL",  value:"填單人員"}]},
	 		{items:[{name:"#F.createdBy",        type:"TEXT",   bind:"createdBy",        size:12, mode:"HIDDEN"},
	         		{name:"#F.createdByName",    type:"TEXT",   bind:"createdByName",    size:12, mode:"READONLY" }]},	
	 		{items:[{name:"#L.CreationDate",     type:"LABEL",  value:"填單日期"}]},
	 		{items:[{name:"#F.creationDate",     type:"TEXT",   bind:"creationDate",     size:12, mode:"READONLY" }]}]},
		{row_style:"", cols:[ 
			{items:[{name:"#L.DeclarationNo",    type:"LABEL",  value:labelNo}]},
			{items:[{name:"#F.defPoOrderType",   type:"SELECT", bind:"defPoOrderType",   size:12, mode:displayP, init:allPoOrderType},	// 
	 				{name:"#F.declarationNo",    type:"TEXT",   bind:"declarationNo",    size:20, mode:displayF, eChange:"onChangeDeclarationNo()"}]},
	 			/*	{name:"#B.declarationNo",    type:"PICKER", value:"PICKER",  src:"./images/start_node_16.gif",
	 									         openMode:"open", 
	 									         service:"Cm_CustomsDeclaration:search:20091109Receive.page", 
	 									         left:0, right:0, width:1024, height:768,	
	 									         serviceAfterPick:function(){doAfterPickerDeclarationNo();}}]},*/
			
	 		{items:[{name:"#L.WarehouseInDate",  type:"LABEL",  value:labelInDate}]},
	 		{items:[{name:"#F.warehouseInDate",  type:"DATE",   bind:"warehouseInDate",  size:18, mode:"READONLY", eChange:"changeWarehouseInDate()"}]},
	 		{items:[{name:"#L.ReceiptDate",    type:"LABEL",  value:"驗收日期"}]},
	 		{items:[{name:"#F.receiptDate",    type:"DATE",   bind:"receiptDate",   size:8 }]},
			{items:[{name:"#L.ReceiptedBy",      type:"LABEL",  value:"驗收人員"}]},
	 		{items:[{name:"#F.receiptedBy",      type:"TEXT",   bind:"receiptedBy",    size:8 }]},
	 		{items:[{name:"#L.LotNo",            type:"LABEL",  value:"批號"}]},	
	 		{items:[{name:"#F.lotNo",            type:"TEXT",   bind:"lotNo",        size:12, mode:"READONLY" }]}]}
		],	 				
	 	beginService:"",
	 	closeService:""			
	});
}


function masterInitial(){
	var allItemCategory    = vat.bean("allItemCategory");
	var allCountryCode     = vat.bean("allCountryCode");
	var allCurrencyCode    = vat.bean("allCurrencyCode");
	var allTradeTeam       = vat.bean("allTradeTeam");
	var allPaymentTerm     = vat.bean("allPaymentTerm");
	var allWarehouse       = vat.bean("allWarehouse");
	var allTaxType         = vat.bean("allTaxType");
	var allDeclarationType = vat.bean("allDeclarationType");
	var allBudgetYearList  = vat.bean("allBudgetYearList");
	var allBudgetMonthList = vat.bean("allBudgetMonthList");
	var allLCList          = vat.bean("allLCList");
	var allLCList1         = vat.bean("allLCList");
	var isConfirm = [[true, true], ["已確認", "未確認"], ["Y", "N"]];
	
  	var typeCode        = vat.bean("typeCode");
  	var orderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");
  	var budgetType      = vat.bean("budgetType");		// 預算扣除方式 P:UnitPrice/C:Cost/T:整筆扣除
	var budgetCheckType = vat.bean("budgetCheckType");	// 預算扣除類別 Y:year/M:month
  	
	var styleF = " style='display:none'";
	var styleP = "";
	var styleRR  = " style='display:none'";
	if ( orderTypeCode=="EIF" || orderTypeCode=="IRF" || orderTypeCode=="EOF"){
		styleF = ""
		styleP = " style='display:none'";
	}
	if(typeCode=="RR"){
		var styleRR  = "";
	}
	
	var isMonthBudget = "READONLY";
	if(budgetCheckType=="M"){
		isMonthBudget = "";
	}

	vat.block.create(vatMasterDiv, {
		id: "vatMasterDiv",generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[  
	 	{row_style:styleF , cols:[
	 		{items:[{name:"#L.ImportDate",       type:"LABEL", value:"進口日期"}]},	 
	 		{items:[{name:"#F.importDate",       type:"DATE",  bind:"importDate",      size:10, mode:"READONLY"}]},		 
	 		{items:[{name:"#L.BondNo",           type:"LABEL", value:"海關監管編號"}]},	 
	 		{items:[{name:"#F.bondNo",           type:"TEXT",  bind:"bondNo",          size:12,  mode:"READONLY"}]},
	 		{items:[{name:"#L.declarationDate",  type:"LABEL", value:"報關日期"}]},	 
	 		{items:[{name:"#F.declarationDate",  type:"DATE",  bind:"declarationDate",  size:10,  mode:"READONLY"}]},
			{items:[{name:"#L.DeclarationType",  type:"LABEL",  value:"報單類別"}]},
			{items:[{name:"#F.declarationType",  type:"SELECT", bind:"declarationType",  size:8, init:allDeclarationType,  mode:"READONLY" }]}]},
		{row_style:styleF , cols:[
	 		{items:[{name:"#L.ReleasePackage",   type:"LABEL", value:"放行件數"}]},	 
	 		{items:[{name:"#F.releasePackage",   type:"TEXT",  bind:"releasePackage",   size:8, mode:"READONLY"}]},
	 		{items:[{name:"#L.ReleaseCondition", type:"LABEL", value:"放行附帶條件"}]},	 
			{items:[{name:"#F.releaseCondition", type:"TEXT",  bind:"releaseCondition", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.StoreagePlace",    type:"LABEL", value:"存放處所"}]},	 
	 		{items:[{name:"#F.storeagePlace",    type:"TEXT",  bind:"storeagePlace",    size:8, mode:"READONLY"}]},		 
	 		{items:[{name:"#L.PackageUnit",      type:"LABEL", value:"件數單位"}]},	 
	 		{items:[{name:"#F.packageUnit",      type:"TEXT",  bind:"packageUnit",      size:10, mode:"READONLY"}]}]},
		{row_style:styleF , cols:[
	 		{items:[{name:"#L.Weight",           type:"LABEL", value:"總重量"}]},	 
			{items:[{name:"#F.weight",           type:"TEXT",  bind:"weight",           size:10, mode:"READONLY"}]},
	 		{items:[{name:"#L.VesselSign",       type:"LABEL", value:"船舶呼號"}]},	 
	 		{items:[{name:"#F.vesselSign",       type:"TEXT",  bind:"vesselSign",       size:10, mode:"READONLY"}]},		 
	 		{items:[{name:"#L.VoyageNo",         type:"LABEL", value:"航次"}]},	  
	 		{items:[{name:"#F.voyageNo",         type:"TEXT",  bind:"voyageNo",         size:12, mode:"READONLY"}]},
	 		{items:[{name:"#L.shipCode",         type:"LABEL", value:"船公司代碼"}]},	 
			{items:[{name:"#F.shipCode",         type:"TEXT",  bind:"shipCode",         size:10, mode:"READONLY"}]}]},
		{row_style:styleF , cols:[
	 		{items:[{name:"#L.Exporter",         type:"LABEL", value:"貨物輸出人"}]},	 
	 		{items:[{name:"#F.exporter",         type:"TEXT",  bind:"exporter",         size:12, maxLen:70, mode:"READONLY"}]},		 
	 		{items:[{name:"#L.ClearanceType",    type:"LABEL", value:"通關方式"}]},	 
	 		{items:[{name:"#F.clearanceType",    type:"TEXT",  bind:"clearanceType",    size:10, mode:"READONLY"}]},
	 		{items:[{name:"#L.ContactPerson",    type:"LABEL",  value:"廠商聯絡窗口"}]},	 
	 		{items:[{name:"#F.contactPerson",    type:"TEXT",   bind:"contactPerson",   size:12}]},
	 		{items:[{name:"#L.declarationBoxNo", type:"LABEL", value:"報關行箱號"}]},	 
			{items:[{name:"#F.declarationBoxNo", type:"TEXT",  bind:"declarationBoxNo", size:4, mode:"READONLY"}]}]},		
		{row_style:styleF , cols:[
	 		{items:[{name:"#L.InbondNo",         type:"LABEL", value:"進倉保稅業者代碼"}]},	 
	 		{items:[{name:"#F.inbondNo",         type:"TEXT", bind:"inbondNo",          size:12, maxLen:70, mode:"READONLY"}]},
	 		{items:[{name:"#L.OutbondNo",        type:"LABEL", value:"出倉保稅業者代碼"}]},	 
	 		{items:[{name:"#F.outbondNo",        type:"TEXT",  bind:"outbondNo",        size:12, maxLen:70, mode:"READONLY"}]},
	 		{items:[{name:"#L.ReferenceBillNo",  type:"LABEL", value:"參考單號"}]},	 
			{items:[{name:"#F.referenceBillNo",  type:"TEXT",  bind:"referenceBillNo", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.ReleaseTime",      type:"LABEL", value:"放行時間"}]},	 
	 		{items:[{name:"#F.releaseTime",      type:"DATE",  bind:"releaseTime",      size:10, mode:"READONLY"}]}]},
		// 以上是 readonly for taxType == "F"	

		{row_style:"" , cols:[
			{items:[{name:"#L.ItemCategory",     type:"LABEL",  value:"業種"}]},
			{items:[{name:"#F.itemCategory",     type:"SELECT", bind:"itemCategory",   size:5, init:allItemCategory }]},
			{items:[{name:"#L.CountryCode",      type:"LABEL",  value:"國別"}]},
			{items:[{name:"#F.countryCode",      type:"SELECT", bind:"countryCode",  size:8, init:allCountryCode }]},
	 		{items:[{name:"#L.CurrencyCode",     type:"LABEL",  value:"幣別"}]},	 
			{items:[{name:"#F.currencyCode",     type:"SELECT", bind:"currencyCode", size:12, init:allCurrencyCode, eChange:"onChangeSetExchangeRate()"}]},	// 
	 		{items:[{name:"#L.ExchangeRate",     type:"LABEL",  value:"匯率"}]},	 
	 		{items:[{name:"#F.exchangeRate",     type:"NUMM",   bind:"exchangeRate", size:6,  dec:4  },
	 				{name:"#L.currencyText",     type:"LABEL",  value:"台幣"}]}]},
		{row_style:"" , cols:[
			{items:[{name:"#L.TaxType",              type:"LABEL",  value:"稅別"}]},	 
	 		{items:[{name:"#F.taxType",              type:"SELECT", bind:"taxType",        size:5, init:allTaxType, eChange:"onChangeTaxType()" }]}, 
	 		{items:[{name:"#L.TaxRate",              type:"LABEL",  value:"稅率"}]},	  
	 		{items:[{name:"#F.taxRate",              type:"NUMM",   bind:"taxRate",        size:8, mode:"READONLY"},
	 				{name:"#L.taxRate1",             type:"LABEL",  value:"%"}]},
	 		{items:[{name:"#L.DefaultWarehouseCode", type:"LABEL",  value:"倉庫<font color='red'>*</font>"}]},	 
			{items:[{name:"#F.defaultWarehouseCode", type:"SELECT", bind:"defaultWarehouseCode", size:5, 
					mode:orderTypeCode=="RRL"||orderTypeCode=="RRF"?"":"", init:allWarehouse }], td:" colSpan=3"}]},
		{row_style:"", cols:[
			{items:[{name:"#L.PaymentTermCode",      type:"LABEL",  value:"付款條件"}]},	 
	 		{items:[{name:"#F.paymentTermCode",      type:"SELECT", bind:"paymentTermCode", size:5, init:allPaymentTerm }]},
	 		{items:[{name:"#L.TradeTeam",            type:"LABEL",  value:"價格條件"}]},	 
	 		{items:[{name:"#F.tradeTeam",            type:"SELECT", bind:"tradeTeam",       size:5, init:allTradeTeam }], td:" colSpan=5"}]},
		{row_style:"", cols:[
	 		{items:[{name:"#L.budgetYear",           type:"LABEL",  value:"預算年度"}]},	 
	 		{items:[{name:"#F.budgetYear",           type:"SELECT",  bind:"budgetYear",    size:8,  init:allBudgetYearList}]},
	 		{items:[{name:"#L.budgetMonth",          type:"LABEL",  value:"預算月份"}]},	 
	 		{items:[{name:"#F.budgetMonth",          type:"SELECT",  bind:"budgetMonth",   size:8,  init:allBudgetMonthList, mode:isMonthBudget}]},
	 		{items:[{name:"#L.guiNo",                type:"LABEL",  value:"國內發票"}]},		// mode:function(){}()
	 		{items:[{name:"#F.guiNo",                type:"TEXT",   bind:"guiNo",          size:8}]}, 
	 		{items:[{name:"#L.financeConfirm",       type:"LABEL",  value:"財務確認"}]},	 
	 	    {items:[{name:"#F.financeConfirm",       type:"SELECT", bind:"financeConfirm", size:1,  init:isConfirm }]}]},
		{row_style:styleF , cols:[
	 		{items:[{name:"#L.LcNo",           type:"LABEL",  value:"LC NO."}]},	 
	 		{items:[{name:"#F.lcNo",           type:"SELECT", bind:"lcNo",  init:allLCList }]},		 
	 		{items:[{name:"#L.lcUseAmount",    type:"LABEL",  value:"LC 使用金額"}]},	 
	 		{items:[{name:"#F.lcUseAmount",    type:"NUMM",   bind:"lcUseAmount",   size:10, dec:2 }]},
	 		{items:[{name:"#L.LcNo1",          type:"LABEL",  value:"LC NO."}]},	 
	 		{items:[{name:"#F.lcNo1",          type:"SELECT", bind:"lcNo1",  init:allLCList1}]},		 
	 		{items:[{name:"#L.lcUseAmount1",   type:"LABEL",  value:"LC 使用金額"}]},	 
	 		{items:[{name:"#F.lcUseAmount1",   type:"NUMM",   bind:"lcUseAmount1",   size:10, dec:2}]}]},
	 	{row_style:styleRR, cols:[
	 		{items:[{ name:"#L.latestExportDeclNo",   type:"LABEL",  value:"退關後報關單號" }]},
			{items:[{ name:"#F.latestExportDeclNo",   type:"TEXT",   bind:"latestExportDeclNo",  size:20 }]},
			{items:[{ name:"#L.latestExportDeclDate", type:"LABEL",  value:"退關後報關日期" }]},
			{items:[{ name:"#F.latestExportDeclDate", type:"DATE",   bind:"latestExportDeclDate", size:10 }]},
			{items:[{ name:"#L.latestExportDeclType", type:"LABEL",  value:"退關後報關類別" }]},
			{items:[{ name:"#F.latestExportDeclType", type:"SELECT", bind:"latestExportDeclType", size:10, init:allDeclarationType }],td:"colSpan=3"}]},
	 	{row_style:styleRR, cols:[
	 		{items:[{name:"#L.exportCommissionRate", type:"LABEL", value:"手續費率"}]},	 
			{items:[{name:"#F.exportCommissionRate", type:"NUMM",  bind:"exportCommissionRate", size:12, dec:2},
					{name:"#L.taxRate1",             type:"LABEL",  value:"%"}]},
			{items:[{name:"#L.exportExpense",        type:"LABEL", value:"其他費用"}]},	 
			{items:[{name:"#F.exportExpense",        type:"NUMM",  bind:"exportExpense",        size:12, dec:4}],td:"colSpan=5"}]},
		{row_style:"" , cols:[
	 		{items:[{name:"#L.remark1",        type:"LABEL",  value:"備註一"}]},	 
	 		{items:[{name:"#F.remark1",        type:"TEXT",   bind:"remark1",        size:120, maxLen:120}], td:" colSpan=7"}]},
	 	{row_style:"" , cols:[
	 		{items:[{name:"#L.remark2",        type:"LABEL",  value:"備註二"}]},	 
	 		{items:[{name:"#F.remark2",        type:"TEXT",   bind:"remark2",        size:120, maxLen:120}], td:" colSpan=7"}]},
 	 	{row_style:" style='display:none'" , cols:[
	 		{items:[{name:"#L.sourceOrderNo",	type:"LABEL",  value:"來源單號"}]},	 
	 		{items:[{name:"#F.sourceOrderNo",	type:"TEXT",   bind:"sourceOrderNo"}]}]}
 	 	],
		beginService:"",
		closeService:""			
	});
}


function amountInitial(){
	var typeCode      = vat.bean("typeCode");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var styleIRF  = "";
	var styleIRL  = " style='display:none'";
	if ( orderTypeCode=="EIF" || orderTypeCode=="IRF" ){
		styleIRF  = " style='display:none'";
		styleIRL  = "";
	}
	var labelPurAmount = "進貨金額";
	var labelPayable   = "應付金額";
	if(typeCode=="RR"){
		var styleRR    = "";
		labelPurAmount = "退貨金額";
		labelPayable   = "應收金額";
	}
	
	var userType       = vat.bean("userType");	// SHIPPING, WAREHOUSE, SHIPPING2, ACCOUNT, FINANCEMGR, FINANCE
	var styleWarehouse = " style='display:none'";
	var styleOther     = "";
	if ( userType == "WAREHOUSE" || userType == "WAREHOUSE2" ){
		styleWarehouse = "";
		styleOther     = " style='display:none'";
	}

	vat.block.create(vnB_Amount = 5, {
		id: "vatAmountDiv",generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'",
		rows:[  
		{row_style:styleOther, cols:[
	 		{items:[{name:"#L.Amount1",   type:"LABEL", value:"　" }]},
	 		{items:[{name:"#L.Amount1",   type:"LABEL", value:"外幣" }]},
	 		{items:[{name:"#L.Amount1",   type:"LABEL", value:"台幣" }]}]},
	 	{row_style:styleOther, cols:[
	 		{items:[{name:"#L.TotalLocalPurchaseAmount",   type:"LABEL", value:labelPurAmount}]},
	 		{items:[{name:"#F.totalForeignPurchaseAmount", type:"NUMM",  bind:"totalForeignPurchaseAmount", size:30, dec:2, mode: "READONLY" }]},
	 		{items:[{name:"#F.totalLocalPurchaseAmount",   type:"NUMM",  bind:"totalLocalPurchaseAmount",   size:30, dec:2, mode: "READONLY" }]}]},
		{row_style:styleOther, cols:[
	 		{items:[{name:"#L.exportCommissionLocalAmount",       type:"LABEL", value:"手續費率"}]},
	 		{items:[{name:"#F.exportCommissionForeignAmount",     type:"NUMM",  bind:"exportCommissionForeignAmount", dec:2, size:30, mode: "READONLY" }]},
	 		{items:[{name:"#F.exportCommissionLocalAmount",       type:"NUMM",  bind:"exportCommissionLocalAmount",   dec:2, size:30, mode: "READONLY" }]}]},
	 	{row_style:styleOther, cols:[
	 		{items:[{name:"#L.ExpenseAmount",        type:"LABEL", value:"其他費用"}]},
	 		{items:[{name:"#F.expenseForeignAmount", type:"NUMM",  bind:"expenseForeignAmount", size:30, dec:2, mode: "READONLY" }]},	 
	 		{items:[{name:"#F.expenseLocalAmount",   type:"NUMM",  bind:"expenseLocalAmount",   size:30, dec:2, mode: "READONLY" }]}]},
		{row_style:styleOther, cols:[
	 		{items:[{name:"#L.taxAmount",            type:"LABEL", value:"稅金合計"}]},
	 		{items:[{name:"#F.taxForeignAmount",     type:"NUMM",  bind:"taxForeignAmount",	size:30, dec:2, mode: "READONLY" }]},
	 		{items:[{name:"#F.taxAmount",            type:"NUMM",  bind:"taxAmount",		size:30, dec:2, mode: "READONLY" }]}]},
	 	{row_style:styleOther, cols:[
	 		{items:[{name:"#L.totalAccountsPayable",        type:"LABEL", value:labelPayable}]},
	 		{items:[{name:"#F.totalForeignAccountsPayable", type:"NUMM",  bind:"totalForeignAccountsPayable", size:30, dec:2, mode: "READONLY" }]},
	 		{items:[{name:"#F.totalLocalAccountsPayable",   type:"NUMM",  bind:"totalLocalAccountsPayable",   size:30, dec:2, mode: "READONLY" }]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.ReceiveQuantity", type:"LABEL", value:"進貨數量"}]},	
	 		{items:[{name:"#F.ReceiveQuantity", type:"NUMM",  bind:"ReceiveQuantity", size:30, dec:0, mode: "READONLY" }]}]},
	 	{row_style:styleWarehouse, cols:[
	 		{items:[{name:"#L.ReceiptQuantity", type:"LABEL", value:"驗收數量"}]},	
	 		{items:[{name:"#F.ReceiptQuantity", type:"NUMM",  bind:"ReceiptQuantity", size:30, dec:0, mode: "READONLY" }]}]},
	 	{row_style:styleWarehouse, cols:[
	 		{items:[{name:"#L.AcceptQuantity",  type:"LABEL", value:"良品數量"}]},	
	 		{items:[{name:"#F.AcceptQuantity",  type:"NUMM",  bind:"AcceptQuantity",  size:30, dec:0, mode: "READONLY" }]}]},
	 	{row_style:styleWarehouse, cols:[
	 		{items:[{name:"#L.DefectQuantity",  type:"LABEL", value:"不良品數量"}]},	
	 		{items:[{name:"#F.DefectQuantity",  type:"NUMM",  bind:"DefectQuantity",  size:30, dec:0, mode: "READONLY" }]}]},
	 	{row_style:styleWarehouse, cols:[
	 		{items:[{name:"#L.SampleQuantity",  type:"LABEL", value:"抽樣數量"}]},	
	 		{items:[{name:"#F.SampleQuantity",  type:"NUMM",  bind:"SampleQuantity",  size:30, dec:0, mode: "READONLY" }]}]},
	 	{row_style:styleWarehouse, cols:[
	 		{items:[{name:"#L.ShortQuantity",   type:"LABEL", value:"短溢到數量"}]},	
	 		{items:[{name:"#F.ShortQuantity",   type:"NUMM",  bind:"ShortQuantity",   size:30, dec:0, mode: "READONLY" }]}]}
 	 	],
		beginService:"",
		closeService:""			
	});
}

function detailInitial() {
	var statusTmp     = vat.item.getValueByName("#F.status");
	var branchCode    = vat.bean("branchCode");
	var userType      = vat.bean("userType");	// SHIPPING, WAREHOUSE, SHIPPING2, ACCOUNT, FINANCEMGR, FINANCE
	var typeCode      = vat.bean("typeCode");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var CanGridDelete = true;
	var CanGridAppend = true;
	var CanGridModify = true;
	
	if (userType!="SHIPPING" || orderTypeCode=="EIF" ){
		// SHIPPING or (EIF && 進貨) 禁止新增 imReceiveItem
		var CanGridAppend = false;
	}
	if (userType=="WAREHOUSE" && statusTmp == "SIGNING" || userType=="WAREHOUSE2" && statusTmp == "SIGNING" ){
		var CanGridModify = true;
	}
	
	vat.item.make(vatDetailDiv, "indexNo",  {type:"IDX",  view:"fixed", desc: "NO"});
	if( userType=="SHIPPING" || userType=="SHIPPING2" ){
		vat.item.make(vatDetailDiv, "itemCode", {type:"TEXT", view:"fixed", size: 16, desc:"品號", eChange:"onChangeItemCode()"});
	}else{
		vat.item.make(vatDetailDiv, "itemCode", {type:"TEXT", view:"fixed", size: 16, desc:"品號", mode:"READONLY"});
	}
	if ( orderTypeCode=="EIF"){
		vat.item.make(vatDetailDiv, "declarationItemCode", {type:"TEXT", view:"", size:12, desc:"報單品號", mode:"READONLY"});
	}else{
		vat.item.make(vatDetailDiv, "declarationItemCode", {type:"TEXT", view:"", size:12, desc:"報單品號", mode:"HIDDEN"});
	}
	vat.item.make(vatDetailDiv, "itemCName", {type:"TEXT", view:"", size:20, desc:"品名",     mode:"READONLY"});
	if( orderTypeCode=="EIF" || userType=="WAREHOUSE" || userType=="WAREHOUSE2" ){
		vat.item.make(vatDetailDiv, "quantity",  {type:"NUMM", view:"", size: 6, desc:"數量", dec:0, mode:"READONLY"});
	}else{
		vat.item.make(vatDetailDiv, "quantity",  {type:"NUMM", view:"", size: 6, desc:"數量", dec:0, eChange:"calculateLineAmount('1')"});
	}
	
	if(typeCode=="RR" && userType!="WAREHOUSE" || typeCode=="RR" && userType!="WAREHOUSE2" ){
		vat.item.make(vatDetailDiv, "isConsignSale", {type:"TEXT", view:"", size:2, desc:"寄賣", mode:"READONLY" });
		vat.item.make(vatDetailDiv, "lastForeignUnitCost", {type:"NUMM", view:"", size:12, desc:"最近進貨成本", dec:4, mode:"READONLY" });
		vat.item.make(vatDetailDiv, "standardPurchaseCost", {type:"NUMM", view:"", size:12, desc:"期初平均成本", dec:4, mode:"READONLY" });
		vat.item.make(vatDetailDiv, "foreignUnitPriceOri", {type:"NUMM", view:"", size:12, desc:"原幣成本",    dec:6, mode:"READONLY" });
	}else{
		vat.item.make(vatDetailDiv, "isConsignSale", {type:"TEXT", view:"", size:2, desc:"寄賣", mode:"HIDDEN" });
		vat.item.make(vatDetailDiv, "lastForeignUnitCost", {type:"NUMM", view:"", size:12, desc:"最近進貨成本", dec:4, mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "standardPurchaseCost", {type:"NUMM", view:"", size:12, desc:"期初平均成本", dec:4, mode:"HIDDEN" });
		vat.item.make(vatDetailDiv, "foreignUnitPriceOri", {type:"NUMM", view:"", size:12, desc:"原幣成本",    dec:6, mode:"HIDDEN"});
	}
	
	if( userType=="WAREHOUSE" || userType=="WAREHOUSE2" ){
		vat.item.make(vatDetailDiv, "foreignUnitPrice", {type:"NUMM", view:"", size:8,  desc: "原幣單價", dec:6, mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "localUnitPrice",   {type:"NUMM", view:"", size:8,  desc: "台幣單價", dec:6, mode:"HIDDEN"});	
		vat.item.make(vatDetailDiv, "foreignAmount",    {type:"NUMM", view:"shift", size:12, desc: "原幣合計", dec:2, mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "localAmount",      {type:"NUMM", view:"shift", size:12, desc: "台幣合計", dec:2, mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "unitPrice",        {type:"NUMM", view:"shift", size:12, desc: "零售價", dec:2, mode:"HIDDEN"});//零售價(wade)
	}else{
		if(typeCode=="RR"){
			vat.item.make(vatDetailDiv, "foreignUnitPrice", {type:"NUMM", view:"", size:8,  desc: "退貨單價", dec:6, eChange:"calculateLineAmount('2')"});
		}else{
			vat.item.make(vatDetailDiv, "foreignUnitPrice", {type:"NUMM", view:"", size:8,  desc: "進貨單價", dec:6, eChange:"calculateLineAmount('2')"});
		}
		vat.item.make(vatDetailDiv, "localUnitPrice",   {type:"NUMM", view:"", size:8,  desc: "台幣單價", dec:6});	
		vat.item.make(vatDetailDiv, "foreignAmount",    {type:"NUMM", view:"", size:12, desc: "原幣合計", dec:2, mode:"READONLY"});
		vat.item.make(vatDetailDiv, "localAmount",      {type:"NUMM", view:"", size:12, desc: "台幣合計", dec:2, mode:"READONLY"});
		vat.item.make(vatDetailDiv, "unitPrice",        {type:"NUMM", view:"", size:12, desc: "零售價", dec:2, mode:"READONLY"});//零售價(wade)
	}
	
	if(typeCode=="RR" || userType=="WAREHOUSE" || userType=="WAREHOUSE2" ){
		vat.item.make(vatDetailDiv, "expenseApportionmentAmount", {type:"NUMM", view:"shift",   size: 8, desc:"分攤費用",     mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "invoiceNo",                  {type:"TEXT", view:"none", 	size:16, desc:"Invoice No.", mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "poOrderNo",                  {type:"TEXT", view:"none", 	size:16, desc:"採購單單號",    mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "poNo",                  	  {type:"TEXT", view:"none", 	size:16, desc:"P/O No.", mode:"HIDDEN"});
	}else if( orderTypeCode=="EIF" || orderTypeCode=="IRF" ){
		vat.item.make(vatDetailDiv, "expenseApportionmentAmount", {type:"NUMM", view:"shift",   size: 8, desc:"分攤費用",      mode:"READONLY"});
		vat.item.make(vatDetailDiv, "invoiceNo",                  {type:"TEXT", view:"shift",	size:16, desc:"Invoice No." });
		vat.item.make(vatDetailDiv, "poOrderNo",                  {type:"TEXT", view:"none", 	size:16, desc:"採購單單號",     mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "poNo",                  	  {type:"TEXT", view:"shift", 	size:16, desc:"P/O No."});
	}else{
		vat.item.make(vatDetailDiv, "expenseApportionmentAmount", {type:"NUMM", view:"shift",	size: 8, desc:"分攤費用",       mode:"READONLY"});
		vat.item.make(vatDetailDiv, "invoiceNo",                  {type:"TEXT", view:"none", 	size:16, desc:"Invoice No.",  mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "poOrderNo",                  {type:"TEXT", view:"shift",   size:16, desc:"採購單單號"});
		vat.item.make(vatDetailDiv, "poNo",                  	  {type:"TEXT", view:"shift", 	size:16, desc:"P/O No."});
	}
	
	if(userType=="WAREHOUSE" && typeCode=="IMR" ){	// 倉管 && 進貨時才出現
		vat.item.make(vatDetailDiv, "receiptQuantity", {type:"NUMM", view:"", size:8, desc:"驗收數量", dec:0,   mode:"READONLY"});
		vat.item.make(vatDetailDiv, "acceptQuantity",  {type:"NUMM", view:"", size:8, desc:"良品數量", dec:0,   mode:branchCode==2?"READONLY":"HIDDEN"});
		vat.item.make(vatDetailDiv, "defectQuantity",  {type:"NUMM", view:"", size:8, desc:"不良品數量", dec:0,   mode:branchCode==2?"":"HIDDEN" , eChange:"countReceiptQuantity()"});
		vat.item.make(vatDetailDiv, "sampleQuantity",  {type:"NUMM", view:"", size:8, desc:"抽樣數量", dec:0,   mode:branchCode==2?"":"HIDDEN" ,   eChange:"countReceiptQuantity()"});
		vat.item.make(vatDetailDiv, "shortQuantity",   {type:"NUMM", view:"", size:8, desc:"短到數量", dec:0,   mode:branchCode==2?"":"HIDDEN", eChange:"countReceiptQuantity()"});
		vat.item.make(vatDetailDiv, "diffQty",         {type:"NUMM", view:"shift", size:8, desc:"調整數量", dec:0,   mode:branchCode==2?"READONLY":"HIDDEN"});
		vat.item.make(vatDetailDiv, "barcodeCount",    {type:"NUMM", view:"", size:8, desc:"條碼數量", dec:0});
	}else if(userType=="WAREHOUSE2" && typeCode=="IMR"){	//倉管主管
		vat.item.make(vatDetailDiv, "receiptQuantity", {type:"NUMM", view:"", size:8, desc:"驗收數量", dec:0,   mode:"READONLY"});
		vat.item.make(vatDetailDiv, "acceptQuantity",  {type:"NUMM", view:"", size:8, desc:"良品數量", dec:0,   mode:branchCode==2?"READONLY":"HIDDEN"});
		vat.item.make(vatDetailDiv, "defectQuantity",  {type:"NUMM", view:"", size:8, desc:"不良品數量", dec:0,   mode:branchCode==2?"READONLY":"HIDDEN" });
		vat.item.make(vatDetailDiv, "sampleQuantity",  {type:"NUMM", view:"", size:8, desc:"抽樣數量", dec:0,   mode:branchCode==2?"READONLY":"HIDDEN" });
		vat.item.make(vatDetailDiv, "shortQuantity",   {type:"NUMM", view:"", size:8, desc:"短到數量", dec:0,   mode:branchCode==2?"READONLY":"HIDDEN" });
		vat.item.make(vatDetailDiv, "diffQty",         {type:"NUMM", view:"shift", size:8, desc:"調整數量", dec:0,   mode:branchCode==2?"READONLY":"HIDDEN"});
		vat.item.make(vatDetailDiv, "barcodeCount",    {type:"NUMM", view:"", size:8, desc:"條碼數量", dec:0, mode:"READONLY"});
	}else{
		vat.item.make(vatDetailDiv, "receiptQuantity", {type:"NUMM", view:"none", size:8, desc:"驗收數量", dec:0,   mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "acceptQuantity",  {type:"NUMM", view:"none", size:8, desc:"良品數量", dec:0,   mode:"READONLY", eChange:"countReceiptQuantity()"});
		vat.item.make(vatDetailDiv, "defectQuantity",  {type:"NUMM", view:"none", size:8, desc:"不良品數量", dec:0, mode:"HIDDEN", eChange:"countReceiptQuantity()"});
		vat.item.make(vatDetailDiv, "sampleQuantity",  {type:"NUMM", view:"none", size:8, desc:"抽樣數量", dec:0,   mode:"HIDDEN", eChange:"countReceiptQuantity()"});
		vat.item.make(vatDetailDiv, "shortQuantity",   {type:"NUMM", view:"none", size:8, desc:"短溢到數量", dec:0, mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "diffQty",         {type:"NUMM", view:"none", size:8, desc:"調整數量", dec:0,   mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "barcodeCount",    {type:"NUMM", view:"none", size:8, desc:"條碼數量", dec:0,   mode:"HIDDEN"});
	}
	if ( typeCode=="RR" && (orderTypeCode=="EOF" || orderTypeCode=="RRF")){  // 進貨退回 && (EOF || RRF)
		vat.item.make(vatDetailDiv, "shippingMark",  {type:"TEXT", view:"shift", size: 12, desc:"裝箱單號"});
		vat.item.make(vatDetailDiv, "weight",        {type:"NUMM", view:"shift", size: 8, desc:"重量(KGS)"});
	}else{
		vat.item.make(vatDetailDiv, "shippingMark",  {type:"TEXT", view:"none", size: 12, desc:"裝箱單號",  mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "weight",        {type:"NUMM", view:"none", size:  8, desc:"重量(KGS)", mode:"HIDDEN"});
	}
	
	if(typeCode=="RR"){
		if( orderTypeCode=="EOF" || orderTypeCode=="RRF" ){
			vat.item.make(vatDetailDiv, "originalDeclarationDate", {type:"DATE", view:"shift", size:10, desc:"原進口日期", mode:""});
			vat.item.make(vatDetailDiv, "originalDeclarationNo",   {type:"TEXT", view:"shift", size:15, desc:"原報單號",   mode:""});
			vat.item.make(vatDetailDiv, "originalDeclarationSeq",  {type:"NUMM", view:"shift", size:5, desc:"原報單項次", mode:""});
		}else{
			vat.item.make(vatDetailDiv, "originalDeclarationDate", {type:"DATE", view:"shift", size:10, desc:"原進口日期", mode:"HIDDEN"});
			vat.item.make(vatDetailDiv, "originalDeclarationNo",   {type:"TEXT", view:"shift", size:15, desc:"進貨單號",   mode:""});
			vat.item.make(vatDetailDiv, "originalDeclarationSeq",  {type:"NUMM", view:"shift", size:5, desc:"原報單項次", mode:"HIDDEN"});
		}
		vat.item.make(vatDetailDiv, "lotNo",                   {type:"TEXT", view:"shift", size:10, desc:"原批號",    mode:""});
 
	}else if( orderTypeCode=="EIF" && userType!="WAREHOUSE" || orderTypeCode=="EIF" && userType!="WAREHOUSE2" ){
		vat.item.make(vatDetailDiv, "originalDeclarationDate", {type:"DATE", view:"shift", size:10, desc:"原進口日期"});
		vat.item.make(vatDetailDiv, "originalDeclarationNo",   {type:"TEXT", view:"shift", size:15, desc:"原D8報單"});
		vat.item.make(vatDetailDiv, "originalDeclarationSeq",  {type:"NUMM", view:"shift", size: 5, desc:"原報單項次"});
		vat.item.make(vatDetailDiv, "lotNo",                   {type:"TEXT", view:"none",  size:10, desc:"原批號",    mode:"HIDDEN"});

	}else{
		vat.item.make(vatDetailDiv, "originalDeclarationDate", {type:"DATE", view:"none",  size:10, desc:"原進口日期", mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "originalDeclarationNo",   {type:"TEXT", view:"none",  size:15, desc:"原D8報單",  mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "originalDeclarationSeq",  {type:"NUMM", view:"none",  size: 5, desc:"原報單項次", mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "lotNo",                   {type:"TEXT", view:"none",  size:10, desc:"原批號",    mode:"HIDDEN"});
	}
	
	vat.item.make(vatDetailDiv, "lineId",         {type:"ROWID", view:"none"});              
	vat.item.make(vatDetailDiv, "status",         {type:"RADIO", view:"none",  desc:"狀態", mode:"HIDDEN"});
	vat.item.make(vatDetailDiv, "isLockRecord",   {type:"CHECK", view:"none",  desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vatDetailDiv, "isDeleteRecord", {type:"DEL",   view:"fixed", desc:"刪除", mode:orderTypeCode=="EIF"?"HIDDEN":""});
	vat.item.make(vatDetailDiv, "message",        {type:"MSG",   desc:"訊息"});

	vat.block.pageLayout( vatDetailDiv, {
			id: "vatDetailDiv",
			pageSize:10, 
			OptselectAll:true,
		  //gridOverflow: "scroll",         //** 設定 Data Grid 有 scroll bar
			canGridDelete:CanGridDelete,
			canGridAppend:CanGridAppend,
			canGridModify:CanGridModify,
			loadBeforeAjxService:"loadBeforeAjxService()", 
			saveBeforeAjxService:"saveBeforeAjxService()", 
			saveSuccessAfter    :"saveSuccessAfter()"});
	vat.block.pageDataLoad( vatDetailDiv, vnCurrentPage = 1);
}


function expenseInitial() {
	var allExpenseCode = vat.bean("allExpenseCode");
	var userType       = vat.bean("userType");	// SHIPPING, WAREHOUSE, SHIPPING2, ACCOUNT, FINANCEMGR, FINANCE
	var orderTypeCode  = vat.item.getValueByName("#F.orderTypeCode");
	var statusTmp      = vat.item.getValueByName("#F.status");


	// 船務在 簽核完成與結案前都可以輸入費用
		var CanGridDelete = true;
		var CanGridAppend = true;
		var CanGridModify = true;	
	
	vat.item.make(vatExpenseDiv, "indexNo",        {type:"IDX",    desc: "NO"});
	vat.item.make(vatExpenseDiv, "supplierCode",   {type:"TEXT", view:"fixed", size: 15, maxlength: 15, desc: "廠商代號",  eChange:"onChangeSupplierCode('EXPENSE')"});
	vat.item.make(vatExpenseDiv, "picker",		   {type:"PICKER", view:"fixed", value:"PICKER", src:"./images/start_node_16.gif",
	 									 			openMode:"open", 
	 									 		 	service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 		 	left:0, right:0, width:1024, height:768,	
	 									 		 	serviceAfterPick:function(X){doAfterPickerSupplier(X,'EXPENSE');}}),	 		 	
	vat.item.make(vatExpenseDiv, "supplierName",   {type:"TEXT", view:"",   size: 20, maxlength: 20, desc: "廠商品稱",  mode: "READONLY"});
	vat.item.make(vatExpenseDiv, "expenseCode",    {type:"SELECT", view:"", size:  8, maxlength: 20, desc: "費用類型",  init:allExpenseCode}); 
	vat.item.make(vatExpenseDiv, "foreignAmount",  {type:"NUMM", view:"",   size:  8, maxlength: 20, desc: "原幣金額",  eChange:"countAmount('F')"});
	vat.item.make(vatExpenseDiv, "localAmount",    {type:"NUMM", view:"",   size:  8, maxlength: 20, desc: "台幣金額",  eChange:"countAmount('L')"});
	vat.item.make(vatExpenseDiv, "taxAmount",      {type:"NUMM", view:"",   size: 12, maxlength: 20, desc: "營業稅"});
	vat.item.make(vatExpenseDiv, "billDate",       {type:"DATE", view:"",   size:  8, maxlength: 20, desc: "帳款日期"});
	vat.item.make(vatExpenseDiv, "reserve1",       {type:"TEXT", view:"shift",   size: 40, maxlength: 50, desc: "備註"});
	vat.item.make(vatExpenseDiv, "lineId",         {type:"ROWID"});              
	vat.item.make(vatExpenseDiv, "status",         {type:"RADIO", desc:"狀態", mode:"HIDDEN"});                    
	vat.item.make(vatExpenseDiv, "isLockRecord",   {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});                               
	vat.item.make(vatExpenseDiv, "isDeleteRecord", {type:"DEL",   desc:"刪除"});                                                          
	vat.item.make(vatExpenseDiv, "message",        {type:"MSG",   desc:"訊息"});

	vat.block.pageLayout( vatExpenseDiv, {
			id: "vatExpenseDiv",
			pageSize:10, 
			OptselectAll:true,
		  //gridOverflow: "scroll",         //** 設定 Data Grid 有 scroll bar
			canGridDelete:CanGridDelete,
			canGridAppend:CanGridAppend,
			canGridModify:CanGridModify,
			beginService        :"", 
			closeService        :"", 
			appendBeforeService :"appendBeforeService()", 
			appendAfterService  :"", 
			loadBeforeAjxService:"loadBeforeAjxExpenseService()", 
			loadSuccessAfter    :"", 
			saveBeforeAjxService:"saveBeforeAjxExpenseService()", 
			saveSuccessAfter    :"saveSuccessExpenseAfter()"});
			
	vat.block.pageDataLoad( vatExpenseDiv, vnCurrentPage = 1);
}


function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
		vat.bean().vatBeanOther.firstRecordNumber = 0;
		vat.bean().vatBeanPicker.result = null;
    	refreshForm("");
	 }
}

/* 計算LINE合計的部份 */
function calculateLineAmount(type) {
	var exchangeRate     = vat.item.getValueByName("#F.exchangeRate");
	var nItemLine        = vat.item.getGridLine();
	var quantity         = vat.item.getGridValueByName("quantity",nItemLine);
	var foreignUnitPrice = vat.item.getGridValueByName("foreignUnitPrice",nItemLine);

	var localUnitPrice   = parseFloat(foreignUnitPrice) * parseFloat(exchangeRate) ;
	var foreignAmount    = parseFloat(foreignUnitPrice) * parseFloat(quantity)   ;
	var localAmount      = foreignAmount                * parseFloat(exchangeRate) ;
	vat.item.setGridValueByName("localUnitPrice", nItemLine, localUnitPrice );
	vat.item.setGridValueByName("foreignAmount",  nItemLine, foreignAmount  );
	vat.item.setGridValueByName("localAmount",    nItemLine, localAmount    );
	if(type == "1"){
		vat.item.setGridValueByName("receiptQuantity", nItemLine, quantity );
		vat.item.setGridValueByName("acceptQuantity", nItemLine, quantity );
		vat.item.setGridValueByName("barcodeCount", nItemLine, quantity );
		vat.item.setGridValueByName("declarationQty", nItemLine, quantity );
	}
}


/* 計算 item 驗收數量 */
function countReceiptQuantity() {
	var brandCode   	= vat.item.getValueByName("#F.brandCode");
	var orderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");
	var nItemLine       = vat.item.getGridLine();
	var quantity        = vat.item.getGridValueByName("quantity",        nItemLine);
	var receiptQuantity = vat.item.getGridValueByName("receiptQuantity", nItemLine);
	var acceptQuantity  = vat.item.getGridValueByName("acceptQuantity",  nItemLine);
	var defectQuantity  = vat.item.getGridValueByName("defectQuantity",  nItemLine);
	var sampleQuantity  = vat.item.getGridValueByName("sampleQuantity",  nItemLine);
	var shortQuantity   = vat.item.getGridValueByName("shortQuantity",   nItemLine);
	//驗收數量每次重新還原計算
	receiptQuantity = quantity;
	if(brandCode=="T2"){
		if(shortQuantity > 0 ){ //若短到，則短溢到欄位填正數，良品數量為：驗收數量－不良品－抽樣－短到
			if(orderTypeCode == "EIF"){
				acceptQuantity = parseFloat(receiptQuantity) - parseFloat(defectQuantity) - parseFloat(sampleQuantity) - parseFloat(shortQuantity);
				//若為完稅進貨 短到的話 驗收數量 會變少
			}else if(orderTypeCode == "EIP"){
				acceptQuantity = parseFloat(receiptQuantity) - parseFloat(defectQuantity) - parseFloat(sampleQuantity) - parseFloat(shortQuantity);
				receiptQuantity = parseFloat(receiptQuantity) - parseFloat(shortQuantity);
			}
		}else{					//若溢到，則短溢到欄位填負數，良品數量為：驗收數量－不良品－抽樣
			acceptQuantity = parseFloat(receiptQuantity) - parseFloat(defectQuantity) - parseFloat(sampleQuantity);	
		}
	}else{
		var receiptQuantity = parseFloat(acceptQuantity) + parseFloat(defectQuantity) + parseFloat(sampleQuantity);	// 驗收 = 良品 + 不良品 + 樣品
		var shortQuantity   = parseFloat(receiptQuantity) - parseFloat(quantity) ;	// 短溢 = 驗收 - 應到 
	}
	vat.item.setGridValueByName("receiptQuantity", nItemLine, receiptQuantity );
	vat.item.setGridValueByName("acceptQuantity",  nItemLine, acceptQuantity  );
	vat.item.setGridValueByName("shortQuantity",   nItemLine, shortQuantity  );
}


/* 換算 expense 外幣與本幣 */
function countAmount( type ) {
	/*
	var nItemLine     = vat.item.getGridLine();
	var foreignAmount = vat.item.getGridValueByName("foreignAmount", nItemLine);
	var localAmount   = vat.item.getGridValueByName("localAmount",   nItemLine);
	var exchangeRate  = vat.item.getValueByName("#F.exchangeRate");
	alert(type+"-"+foreignAmount+"-"+localAmount+"-"+exchangeRate);
	
	if( type=="F" &&  foreignAmount!=0) 
		vat.item.setGridValueByName("localAmount", nItemLine, parseFloat(foreignAmount) * parseFloat(exchangeRate));
		
	if( type=="L" &&  localAmount!=0) 
		vat.item.setGridValueByName("foreignAmount", nItemLine, parseFloat(localAmount) / parseFloat(exchangeRate));
	*/
}


/* 判斷是否要關閉LINE */
function checkEnableLine() {
	return true ;
}


/* 載入 ImReceiveItem LINE資料 */
function loadBeforeAjxService() {
	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXPageData" + 
						"&brandCode=" + vat.item.getValueByName("#F.brandCode")+  
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")+
						"&headId="    + vat.item.getValueByName("#F.headId");
	return processString;
}


/* 載入 ImReceiveExpense LINE資料 */
function loadBeforeAjxExpenseService() {
	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXExpensePageData" + 
						"&headId=" + vat.item.getValueByName("#F.headId");
	return processString;
}


/* SAVE ImReceiveItem BEFORE AJAX */
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
	var processString = "" ;
	var exchangeRate = 0 ; 
	if (checkEnableLine()) {
		processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=updateAJAXPageLinesData" + 
						"&headId="       + vat.item.getValueByName("#F.headId") + 
						"&exchangeRate=" + vat.item.getValueByName("#F.exchangeRate") ;
	}
	return processString;
}


/* SAVE ImReceiveExpense BEFORE AJAX */
function saveBeforeAjxExpenseService() {
	//alert("saveBeforeAjxExpenseService");
	var processString = "" ;
	var exchangeRate = 0 ; 
	if (checkEnableLine()) {
		//exchangeRate  = vat.item.getValueByName("#F.exchangeRate");
		processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=updateAJAXExpensePageLinesData" + 
						"&headId="       + vat.item.getValueByName("#F.headId") + 
						"&exchangeRate=" + vat.item.getValueByName("#F.exchangeRate") ;
	}
	return processString;
}


/* Button Hit Function */
function doSubmit(formAction){
	
	var status = vat.item.getValueByName("#F.status");
	var warehouseStatus = vat.bean("warehouseStatus");
	var warehouseInDate = new Date(vat.item.getValueByName("#F.warehouseInDate"));
	var receiptDate = new Date(vat.item.getValueByName("#F.receiptDate"));
	var diff = warehouseInDate.DateDiff('d', receiptDate);
	
	vat.block.pageSearch(vatExpenseDiv);
	var alertMessage ="是否確定送出?";
	if("SIGNING" == status && "SAVE" == warehouseStatus && diff > 2){
		alertMessage = "進貨到驗收日期大於二，是否確定送出?";
	}else if("SUBMIT" == formAction){
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
	}else if ("EXPENSE" == formAction){
	 	alertMessage = "是否確定重新分攤費用?";
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
        }else if ("EXPENSE" == formAction){
        	afterSavePageProcess = "EXPENSE"; 
        }
        vat.block.pageSearch(vatDetailDiv);	// save & refresh ImReceiveItem Page / after save success call saveSuccessAfter()
	}
}


/*  ImReceiveItem 存檔成功後要執行的JS FUNCTION */
function saveSuccessAfter(){
		if( "SAVE" == afterSavePageProcess ) {
			doActualSubmit("SAVE");
		}else if( "SUBMIT" == afterSavePageProcess ){
			doActualSubmit("SUBMIT");
		}else if( "SUBMIT_BG" == afterSavePageProcess ){
			doActualSubmit("SUBMIT_BG");
		}else if( "VOID" == afterSavePageProcess ){
			doActualSubmit("VOID");
		} else if ("EXPORT" == afterSavePageProcess) {
			exportFormData();
		}else if ("IMPORT" == afterSavePageProcess) {
			importFormData();
		}else if ("EXTEND" == afterSavePageProcess) {
			execExtendItemInfo();
		}else if ("EXPENSE" == afterSavePageProcess) {
			updateExpense();
		}else if("totalCount" == afterSavePageProcess){
			var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=updateAJAXHeadTotalAmount" +
								"&headId="       + vat.item.getValueByName("#F.headId") + 
								"&taxType="      + vat.item.getValueByName("#F.taxType") + 
								"&exchangeRate=" + vat.item.getValueByName("#F.exchangeRate") + 
								"&taxRate="      + vat.item.getValueByName("#F.taxRate") +
								"&exportCommissionRate=" + vat.item.getValueByName("#F.exportCommissionRate") +
								"&exportExpense="        + vat.item.getValueByName("#F.exportExpense") ;
			vat.ajax.startRequest(processString, function () {
				if (vat.ajax.handleState()) {
					if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
						vat.item.setValueByName("#F.totalLocalPurchaseAmount",    	vat.ajax.getValue("TotalLocalPurchaseAmount", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.totalForeignPurchaseAmount",  	vat.ajax.getValue("TotalForeignPurchaseAmount", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.expenseLocalAmount",          	vat.ajax.getValue("ExpenseLocalAmount", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.expenseForeignAmount",        	vat.ajax.getValue("ExpenseForeignAmount", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.taxAmount",                   	vat.ajax.getValue("TaxAmount", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.taxForeignAmount",            	vat.ajax.getValue("TaxForeignAmount", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.exportCommissionLocalAmount",	vat.ajax.getValue("ExportCommissionLocalAmount", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.exportCommissionForeignAmount",	vat.ajax.getValue("ExportCommissionForeignAmount", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.totalLocalAccountsPayable",   	vat.ajax.getValue("TotalLocalAccountsPayable", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.totalForeignAccountsPayable", 	vat.ajax.getValue("TotalForeignAccountsPayable", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.ReceiveQuantity",  vat.ajax.getValue("ReceiptQuantity", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.ReceiptQuantity",  vat.ajax.getValue("ReceiptQuantity", vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.AcceptQuantity",   vat.ajax.getValue("AcceptQuantity",  vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.DefectQuantity",   vat.ajax.getValue("DefectQuantity",  vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.SampleQuantity",   vat.ajax.getValue("SampleQuantity",  vat.ajax.xmlHttp.responseText)) ;
						vat.item.setValueByName("#F.ShortQuantity",    vat.ajax.getValue("ShortQuantity",   vat.ajax.xmlHttp.responseText)) ;
					}
				}
			});
		}
	afterSavePageProcess = "" ;	
}


/* Button Hit Function */
function doActualSubmit(formAction){
	//alert("doActualSubmit"+formAction);
	if ("SAVE" == afterSavePageProcess) {	
	    afterSaveExpensePageProcess = "SAVE";
	} else if ("SUBMIT" == afterSavePageProcess) {
	    afterSaveExpensePageProcess = "SUBMIT";
	} else if ("SUBMIT_BG" == afterSavePageProcess) {
	    afterSaveExpensePageProcess = "SUBMIT_BG";
	} else if ("VOID" == afterSavePageProcess) {
		afterSaveExpensePageProcess = "VOID";
	} else if ("copyHandler" == afterSavePageProcess) {
		executeCommandHandler("main", "copyHandler");
	} else if ("executeImport" == afterSavePageProcess) {
	    importFormData();
	}
	vat.block.pageSearch(4);	// save & refresh ImReceiveExpense Page / after save success call saveSuccessExpenseAfter()
}


/* ImReceiveExpense 存檔成功後要執行的JS FUNCTION */
function saveSuccessExpenseAfter(){
		if( "SAVE" == afterSaveExpensePageProcess ) {	
			execSubmitAction("SAVE");
		}else if( "SUBMIT" == afterSaveExpensePageProcess ){
			execSubmitAction("SUBMIT");
		}else if( "SUBMIT_BG" == afterSaveExpensePageProcess ){
			execSubmitAction("SUBMIT_BG");
		}else if( "VOID" == afterSaveExpensePageProcess ){
			execSubmitAction("VOID");
		}
	afterSaveExpensePageProcess = "";	
}


/* 明細都存檔完成後, 實際執行主檔存檔作業 */
function execSubmitAction(actionId){
	var formId    = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var processId = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var loginBrandCode   = document.forms[0]["#loginBrandCode"].value;	
    var loginEmployeeCode= document.forms[0]["#loginEmployeeCode"].value;	
	var userType		 = vat.bean("userType");
	var branchCode       = vat.bean("branchCode");
	var employeeCode     = vat.bean("employeeCode");
	var typeCode         = vat.bean("typeCode");
	var warehouseStatus  = vat.bean("warehouseStatus");
	var expenseStatus    = vat.bean("expenseStatus");
    var status           = vat.item.getValueByName("#F.status");
    var orderTypeCode    = vat.item.getValueByName("#F.orderTypeCode");
   	var assignmentId     = vat.item.getValueByName("#assignmentId");
    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	var approvalResult  = vat.item.getValueByName("#F.approvalResult");
	
	var storageHeadId = vat.bean().vatBeanOther.storageHeadId;
	var beanHead = vat.bean().vatBeanOther.beanHead;
	var beanItem = vat.bean().vatBeanOther.beanItem;
	var quantity = vat.bean().vatBeanOther.quantity;
	var storageTransactionType = vat.bean().vatBeanOther.storageTransactionType;
	var storageStatus = vat.bean().vatBeanOther.storageStatus;
	var arrivalWarehouse = vat.bean().vatBeanOther.arrivalWarehouse;
	
    if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
	
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
	/*
	vat.bean().vatBeanOther.warehouseStatus = warehouseStatus;
	vat.bean().vatBeanOther.expenseStatus = expenseStatus;
	vat.bean().vatBeanOther.typeCode = typeCode;
	vat.bean().vatBeanOther.beforeChangeStatus = status;
	vat.bean().vatBeanOther.formStatus = formStatus;
	vat.bean().vatBeanOther.orderTypeCode = orderTypeCode;
	vat.bean().vatBeanOther.userType = userType;
	vat.bean().vatBeanOther.branchCode = branchCode;
	vat.bean().vatBeanOther.employeeCode = employeeCode;
	vat.bean().vatBeanOther.processId = processId;
	vat.bean().vatBeanOther.approvalResult = approvalResult;
	vat.bean().vatBeanOther.approvalComment = approvalComment;
	vat.bean().vatBeanOther.assignmentId = assignmentId;
	*/

	vat.bean().vatBeanOther={
			warehouseStatus: warehouseStatus,
			expenseStatus: expenseStatus,
			typeCode: typeCode,
			beforeChangeStatus: status,
	        formStatus: formStatus,
	        orderTypeCode: orderTypeCode,
	        userType: userType,
	        loginBrandCode: loginBrandCode,
	        loginEmployeeCode: loginEmployeeCode,
	        branchCode: branchCode,
	        employeeCode: employeeCode,
	        processId: processId,
	        approvalResult: approvalResult,
	        approvalComment: approvalComment,
	        assignmentId: assignmentId,
	        storageHeadId: storageHeadId,
	        beanHead: beanHead,
	        beanItem: beanItem,
	        quantity: quantity,
	        storageTransactionType: storageTransactionType,
	        storageStatus: storageStatus,
	        arrivalWarehouse: arrivalWarehouse
        	};

	if ("SUBMIT_BG" == actionId){
		vat.block.submit(function(){return "process_object_name=imReceiveMainAction"+
                "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
	}else{
		vat.block.submit(function(){return "process_object_name=imReceiveMainAction"+
            	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
	}
}


function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imReceiveMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}


/* 顯示合計的頁面 不要寫存的動作 因為 totalCount 會做 */
function showTotalCountPage(){
	afterSavePageProcess = "";
	vat.block.pageSearch(vatDetailDiv);
	afterSavePageProcess = "totalCount";
	vat.block.pageSearch(4);
}


/*匯入 */
function importFormData(){
	var width = "600";
    var height = "400";
    var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode")
    var headId        = vat.item.getValueByName("#F.headId");
    var userType      = vat.bean("userType");
	    if( orderTypeCode=="EIF" || orderTypeCode=="IRF" || orderTypeCode=="EOF" || orderTypeCode=="RRF"){
			var suffix = 
				"&importBeanName=IM_RECEIVE_FOREIGN_ITEM" +
				"&importFileType=XLS" +
	        	"&processObjectName=imReceiveHeadMainService" + 
	        	"&processObjectMethodName=executeImportImLists" +
	        	"&arguments=" + headId +
	        	"&parameterTypes=LONG" +
		        "&blockId=3";
			return suffix;
		}else{
			var suffix = 
				"&importBeanName=IM_RECEIVE_LOCAL_ITEM" +
				"&importFileType=XLS" +
	        	"&processObjectName=imReceiveHeadMainService" + 
	        	"&processObjectMethodName=executeImportImLists" +
	        	"&arguments=" + headId +
	        	"&parameterTypes=LONG" +
		        "&blockId=3";
		    return suffix;
		}
}

function afterImportSuccess(){
	//alert("FindTheFirstItemCategory");
	refreshHeadData();
}

function refreshHeadData(){	
	changeItemCategory();
}

/* 匯出 */
function exportFormData(){
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode")
    var headId        = vat.item.getValueByName("#F.headId");
    var userType      = vat.bean("userType");
    if( userType != "WAREHOUSE" && userType != "WAREHOUSE2" ){
	    if( orderTypeCode=="EIF" || orderTypeCode=="IRF" || orderTypeCode=="EOF" || orderTypeCode=="RRF" ){
	        var url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName=IM_RECEIVE_FOREIGN_ITEM_SQL" +
              "&fileType=XLS" + 
              "&processObjectName=imReceiveHeadMainService" + 
              "&processObjectMethodName=getAJAXExportDataBySql" +
              "&headId=" + headId + "&type=F";
	    }else if(orderTypeCode=="EIP" || orderTypeCode=="IRL"|| orderTypeCode=="EOP"|| orderTypeCode=="RRL"){
	    	var url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName=IM_RECEIVE_LOCAL_ITEM_SQL" +
              "&fileType=XLS" + 
              "&processObjectName=imReceiveHeadMainService" + 
              "&processObjectMethodName=getAJAXExportDataBySql" +
              "&headId=" + headId + "&type=P";
	    }
    }else{
   		var url = "/erp/jsp/ExportFormData.jsp" + 
              		"?exportBeanName=IM_RECEIVE_WAREHOUSE_ITEM" + 
             	 	"&fileType=XLS" + 
              		"&processObjectName=imReceiveHeadMainService" + 
              		"&processObjectMethodName=findByIdForWarehouseExport" + 
              		"&gridFieldName=imReceiveItems" + 
             	 	"&arguments=" + headId + 
              		"&parameterTypes=LONG";
    }
    
    var width = "1024";
    var height = "768";
    window.open(url, '進貨單明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + 
    				 ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


/* PICKER 之前要先RUN LINE SAVE */
function doBeforePicker(){
	vat.block.pageSearch(vatDetailDiv);	// save & refresh imReceiveItem
	vat.block.pageSearch(4);	// save & refresh imReceiveExpense
}


function onChangeItemCode() {
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var invoiceNo     = vat.item.getValueByName("#F.defPoOrderNo");
	var nItemLine = vat.item.getGridLine();
	var sItemCode = vat.item.getGridValueByName("itemCode",nItemLine).toUpperCase();
	vat.item.setGridValueByName( "itemCode",		nItemLine, sItemCode);
	vat.item.setGridValueByName( "quantity",		nItemLine, 0);
	vat.item.setGridValueByName( "foreignUnitPrice",nItemLine, 0);	
	vat.item.setGridValueByName( "localUnitPrice",  nItemLine, 0);
	vat.item.setGridValueByName( "foreignAmount",   nItemLine, 0);
	vat.item.setGridValueByName( "localAmount",     nItemLine, 0);
	vat.item.setGridValueByName( "expenseApportionmentAmount", nItemLine, 0);	
	
	if (sItemCode != "") {
		//自動帶出預設INVOICE或是預設採購單號
		if(null!=invoiceNo && "IRF"==orderTypeCode){
			vat.item.setGridValueByName("invoiceNo", nItemLine, invoiceNo );
		}else if(null!=invoiceNo && "IRL"==orderTypeCode){
			vat.item.setGridValueByName("poOrderNo", nItemLine, invoiceNo );
		}
		var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXLineData" +
								"&brandCode="       + vat.item.getValueByName("#F.brandCode")+  
								"&orderTypeCode="   + vat.item.getValueByName("#F.orderTypeCode") + 
								"&itemCode="        + sItemCode +
								"&branchCode="      + vat.bean("branchCode") +
								"&typeCode="        + vat.bean("typeCode") ;
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					vat.item.setGridValueByName("originalDeclarationNo", nItemLine, vat.ajax.getValue("OrderNo",             vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("declarationItemCode",   nItemLine, vat.ajax.getValue("DeclarationItemCode", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("foreignUnitPriceOri",   nItemLine, vat.ajax.getValue("ForeignUnitPriceOri", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("foreignUnitPrice",      nItemLine, vat.ajax.getValue("ForeignUnitPrice",    vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("localUnitPrice",        nItemLine, vat.ajax.getValue("LocalUnitPrice",      vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("itemCName",             nItemLine, vat.ajax.getValue("ItemCName",           vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("isConsignSale",         nItemLine, vat.ajax.getValue("IsConsignSale",       vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("lotNo",                 nItemLine, vat.ajax.getValue("LotNo",               vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("lastForeignUnitCost",   nItemLine, vat.ajax.getValue("LastForeignUnitCost", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("standardPurchaseCost",  nItemLine, vat.ajax.getValue("StandardPurchaseCost",vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("unitPrice",  			 nItemLine, vat.ajax.getValue("unitPrice",			 vat.ajax.xmlHttp.responseText));//零售價(wade)
					if(nItemLine == "1")
						changeItemCategory();
				}
			}
		});
	} 
}


/* 變更供應商設定相關資料 */
function onChangeSupplierCode( opType ){
	if ( opType=="HEAD" || opType=="DECL" ){
		var supplierCode = vat.item.getValueByName("#F.supplierCode").toUpperCase();
	} else if (opType=="EXPENSE") {
		var nItemLine    = vat.item.getGridLine();
		var supplierCode = vat.item.getGridValueByName("supplierCode",nItemLine).toUpperCase();
		vat.item.setGridValueByName("supplierCode", nItemLine, supplierCode);
	}

	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXFormDataBySupplier"+
						"&supplierCode="  + supplierCode +
						"&organizationCode=TM"+
						"&brandCode="     + vat.item.getValueByName("#F.brandCode") + 
						"&orderDate="     + vat.item.getValueByName("#F.orderDate") ;
	vat.ajax.startRequest(processString,  function() { 
		if (vat.ajax.handleState()){
			if (opType=="HEAD") {
				vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.countryCode",     vat.ajax.getValue("CountryCode",     vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.tradeTeam",       vat.ajax.getValue("PriceTermCode",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.contactPerson",   vat.ajax.getValue("ContactPerson",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.taxType",         vat.ajax.getValue("TaxType",         vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.taxRate",         vat.ajax.getValue("TaxRate",         vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.exchangeRate",    vat.ajax.getValue("ExchangeRate",    vat.ajax.xmlHttp.responseText));
			}else if (opType=="EXPENSE") {
				vat.item.setGridValueByName("supplierName", nItemLine, vat.ajax.getValue("SupplierName", vat.ajax.xmlHttp.responseText));
			}else if (opType=="DECL") {
				vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.tradeTeam",       vat.ajax.getValue("PriceTermCode",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.contactPerson",   vat.ajax.getValue("ContactPerson",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.taxType",         vat.ajax.getValue("TaxType",         vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.taxRate",         vat.ajax.getValue("TaxRate",         vat.ajax.xmlHttp.responseText));
			}
  		}
	} );
	
	if (opType=="HEAD") {
       	vat.ajax.XHRequest({
           post:"process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXLcDataBySupplier"+
                    "&supplierCode="  + supplierCode +
                    "&status="        + vat.item.getValueByName("#F.status")+
					"&brandCode="     + vat.item.getValueByName("#F.brandCode"),
           find: function changeShopCodeRequestSuccess(oXHR){ 
				var allLCList  = eval(vat.ajax.getValue("allLCList", oXHR.responseText));
				var allLCList1 = eval(vat.ajax.getValue("allLCList", oXHR.responseText));
				allLCList[0][0]  = "#F.lcNo";
	       		allLCList1[0][0] = "#F.lcNo1";
				vat.item.SelectBind(allLCList); 
				vat.item.SelectBind(allLCList1); 
           }   
       });	
	}
}

function onChangeDeclarationNo(){
	vat.item.setValueByName("#F.declarationNo", vat.item.getValueByName("#F.declarationNo").replace(/^\s+|\s+$/, ''))
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=cmDeclarationHeadService"+
                    "&process_object_method_name=executeFindCM"+
                    "&declarationNo=" + vat.item.getValueByName("#F.declarationNo"),
        find: function getOriginalDeliverySuccess(oXHR){
        vat.item.setValueByName("#F.importDate" ,vat.ajax.getValue("ImportDate", oXHR.responseText));
        vat.item.setValueByName("#F.declarationDate" ,vat.ajax.getValue("DeclDate", oXHR.responseText));
        vat.item.setValueByName("#F.declarationType" ,vat.ajax.getValue("DeclType", oXHR.responseText));
        vat.item.setValueByName("#F.exchangeRate" ,vat.ajax.getValue("ExchangeRate", oXHR.responseText));
       }   
   });
}


/* 變更歸帳日期/幣別時須異動 匯率時 */
function onChangeSetExchangeRate() {
	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXExchangeRateByCurrencyCode"+
							"&currencyCode=" + vat.item.getValueByName("#F.currencyCode") + 
							"&organizationCode=TM"+
							"&orderDate="    + vat.item.getValueByName("#F.orderDate") ;
	vat.ajax.startRequest(processString,  function() { 
		if (vat.ajax.handleState()){
			vat.item.setValueByName("#F.exchangeRate", vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText));
		}
	});
}


/* 變更稅別 */
function onChangeTaxType() {	
	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXTaxRate"+
						"&taxType=" + vat.item.getValueByName("#F.taxType");
	vat.ajax.startRequest(processString,  function() { 
  		if (vat.ajax.handleState()){
    		vat.item.setValueByName("#F.taxRate", vat.ajax.getValue("TaxRate", vat.ajax.xmlHttp.responseText));
  		}
  	});
}

function changeItemCategory(){
	var vItemCode = vat.item.getGridValueByName("itemCode", 1);
	var brandCode = document.forms[0]["#loginBrandCode" ].value;
	if(brandCode == "T2"){
		vat.ajax.XHRequest({ 
			post:"process_object_name=imReceiveHeadMainService&process_object_method_name=getItemCategory" + 
	                "&headId=" + vat.item.getValueByName("#F.headId")+
	                "&brandCode=" + brandCode+
	                "&itemCode=" + vItemCode,
            asyn:false,                      
			find: function change(oXHR){
				if(vat.ajax.getValue("ItemCategory", oXHR.responseText) != ""){
					vat.item.setValueByName("#F.itemCategory"			,vat.ajax.getValue("ItemCategory", oXHR.responseText));
					vat.item.setValueByName("#F.remark1"			,vat.ajax.getValue("Remark1", oXHR.responseText));
					vat.item.setValueByName("#F.remark2"			,vat.ajax.getValue("Remark2", oXHR.responseText));
				}
           	}
		});
	}
}


function changeWarehouseInDate(){
	if(enableStorage){
		//eventStorageService();
	}
}


function updateAjaxDate(){
	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=updateAjaxDate"+
						"&headId=" + vat.item.getValueByName("#F.headId") + 
						"&warehouseInDate=" + vat.item.getValueByName("#F.warehouseInDate");
	vat.ajax.startRequest(processString,  function() { 
  		if (vat.ajax.handleState()){
  			alert('日期儲存完成');
  		}
  	});
}

function updateExpense(){
	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=updateExpense"+
						"&headId=" + vat.item.getValueByName("#F.headId") + 
						"&orderDate=" + vat.item.getValueByName("#F.orderDate");
	vat.ajax.startRequest(processString,  function() { 
  		if (vat.ajax.handleState()){
  			alert('費用分攤完成');
  			vat.block.pageDataLoad( vatDetailDiv, vnCurrentPage = 1);
  		}
  	});
}

/* 匯入報單/採購單明細  from cmDeclaration & fiInvoice / purchaseOrder */
function importData() {
	var orderTypeCode    = vat.item.getValueByName("#F.orderTypeCode");
	var brandCode        = vat.item.getValueByName("#F.brandCode");
	var branchCode       = vat.bean("branchCode");
	
	var confirmString = "是否確定匯入報單明細?(原有資料將會清除)";
	if (orderTypeCode=="IRL" || orderTypeCode=="EIP"){ 	//國內採購 or 完稅採購
		confirmString = "是否確定匯入採購單明細?(原有資料將會清除)";
	}

	if(confirm(confirmString)){
		var defPoOrderNo  = vat.item.getValueByName("#F.defPoOrderNo");
		var declarationNo = vat.item.getValueByName("#F.declarationNo");
		
		if( (null==declarationNo || ""==declarationNo ) && orderTypeCode=="EIF" ) {	// T2->EIF
			return alert("請輸入報單單號, 再執行匯入報單明細");
			
		} else if( (null==defPoOrderNo || ""==defPoOrderNo || null==declarationNo || ""==declarationNo) 
					&& orderTypeCode=="IRF" ) {										// T1->IRF
			return alert("請輸入報單單號與 Invoice No, 再執行匯入報單明細");
			
		} else if( (null==defPoOrderNo || ""==defPoOrderNo) && 
				   (orderTypeCode=="IRL" || orderTypeCode=="EOP" )){				// IRL, EOP
			return alert("請輸入預設採購單號, 再執行匯入採購單明細");
		}	
	
		vat.block.submit(function()
				{return "process_object_name=imReceiveHeadMainService"+
            		"&process_object_method_name=updateAJAXImportItem";}, 
            	{bind:true, link:true, other:true, 
            		funcSuccess:function(){
						vat.item.bindAll();
						vat.block.pageDataLoad( vatDetailDiv, vnCurrentPage = 1);
						var errorMessage = vat.bean("rtnString");
        				if( errorMessage != "NONE" ){
        					alert(errorMessage+", 敬請檢查！");
        				}else{
        					alert("明細資料匯入成功, 請檢查明細資料頁面！");
        					vat.item.setAttributeByName("#F.importDate",	"readOnly", true);
        					vat.item.setAttributeByName("#F.releaseTime",	"readOnly", true);
        					vat.item.setAttributeByName("#F.declarationDate","readOnly", true);
        					changeItemCategory();
        					onChangeSupplierCode('DECL');
        				}
     		   		}
            	});
	}
}


function onChangeDefPoOrderNo(){
	
	//如果是T2的進貨
	if("T2" == vat.item.getValueByName("#F.brandCode") && vat.bean("typeCode") =="IMR"){
	var defPoOrderTypeCode = "";
	if("EIF" == vat.item.getValueByName("#F.orderTypeCode"))
		defPoOrderTypeCode = "EPF";
	else
		defPoOrderTypeCode = "EPP";
		
		var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXDefPoOrderNo"+
							"&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
							"&defPoOrderNo=" + vat.item.getValueByName("#F.defPoOrderNo")+ 
							"&defPoOrderTypeCode=" + defPoOrderTypeCode;
		vat.ajax.startRequest(processString,  function() { 
  			if (vat.ajax.handleState()){
  				if("OK" == vat.ajax.getValue("ReturnMessage", vat.ajax.xmlHttp.responseText)){
  					vat.item.setValueByName("#F.defaultWarehouseCode", vat.ajax.getValue("DefaultWarehouseCode", vat.ajax.xmlHttp.responseText))
  					vat.item.setValueByName("#F.supplierCode", vat.ajax.getValue("SupplierCode", vat.ajax.xmlHttp.responseText))
  					onChangeSupplierCode('HEAD');
  				}
  			}
		});
	}
	
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
function doAfterPickerSupplier( X, opType ){
	if(vat.bean().vatBeanPicker.result !== null){
		for(i=0 ; i<vat.bean().vatBeanPicker.result.length ; i++ ){
			vat.ajax.XHRequest({ 
				post:"process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXFormDataBySupplier"+
								"&addressBookId="  + vat.bean().vatBeanPicker.result[i].addressBookId +
								"&organizationCode=TM"+
								"&brandCode="      + vat.item.getValueByName("#F.brandCode") + 
								"&orderDate="      + vat.item.getValueByName("#F.orderDate") ,
			          asyn:false,                      
				find: function doAfterPickerSupplierReturn(oXHR){
						if (opType=="HEAD") {			// imReceiveHead
							vat.item.setValueByName("#F.supplierCode",    vat.ajax.getValue("SupplierCode",    oXHR.responseText));
							vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    oXHR.responseText));
							vat.item.setValueByName("#F.countryCode",     vat.ajax.getValue("CountryCode",     oXHR.responseText));
							vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    oXHR.responseText));
							vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("PaymentTermCode", oXHR.responseText));
							vat.item.setValueByName("#F.tradeTeam",       vat.ajax.getValue("PriceTermCode",   oXHR.responseText));
							vat.item.setValueByName("#F.contactPerson",   vat.ajax.getValue("ContactPerson",   oXHR.responseText));
							vat.item.setValueByName("#F.taxType",         vat.ajax.getValue("TaxType",         oXHR.responseText));
							vat.item.setValueByName("#F.taxRate",         vat.ajax.getValue("TaxRate",         oXHR.responseText));
							vat.item.setValueByName("#F.exchangeRate",    vat.ajax.getValue("ExchangeRate",    oXHR.responseText));
						}else if(opType=="EXPENSE"){	// imReceiveExpense
							nItemLine = vat.item.getGridLine(X)+i;
							vat.item.setGridValueByName( "supplierCode", nItemLine, vat.ajax.getValue("SupplierCode",oXHR.responseText));
							vat.item.setGridValueByName( "supplierName", nItemLine, vat.ajax.getValue("SupplierName",oXHR.responseText));
						}
					}
			});
		}
	}
}


/* 報單單號 picker 回來執行 */
function doAfterPickerDeclarationNo(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXFormDataByDeclarationHeadId"+
							"&headId="  + vat.bean().vatBeanPicker.result[0].headId ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.declarationNo", vat.ajax.getValue("DeclarationNo", vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}


function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_RECEIVE_HEAD" +
		"&levelType=ERROR" +
        "&processObjectName=imReceiveHeadMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function listReport(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
	vat.bean().vatBeanOther.warehouseInDate  = vat.item.getValueByName("#F.warehouseInDate");
	vat.bean().vatBeanOther.itemCategory  = vat.item.getValueByName("#F.itemCategory");
	
	//費用單
	if(type == "1"){
		if("T2" != vat.item.getValueByName("#F.brandCode") && "RR" == vat.bean("typeCode"))
			vat.bean().vatBeanOther.reportFileName  = "po0206.rpt";
		else
			vat.bean().vatBeanOther.reportFileName  = "po0632.rpt";
			vat.block.submit(
						function(){return "process_object_name=imReceiveHeadMainService"+
									"&process_object_method_name=getReportConfig";},{other:true,
	                    			funcSuccess:function(){
									eval(vat.bean().vatBeanOther.reportUrl);
						}}
			);
	//驗收單
	}else if(type == "2"){
		if("T2" != vat.item.getValueByName("#F.brandCode"))
			vat.bean().vatBeanOther.reportFileName  = "po0108.rpt";
		else
			vat.bean().vatBeanOther.reportFileName  = "po0633.rpt";
			vat.block.submit(
						function(){return "process_object_name=imReceiveHeadMainService"+
									"&process_object_method_name=getReportConfig";},{other:true,
	                    			funcSuccess:function(){
									eval(vat.bean().vatBeanOther.reportUrl);
						}}
			);
	//出貨差異表
	}else if(type == "3"){
			vat.bean().vatBeanOther.reportFileName  = "IM0601_T2.rpt";
			vat.block.submit(
						function(){return "process_object_name=imReceiveHeadMainService"+
									"&process_object_method_name=getReportConfig";},{other:true,
	                    			funcSuccess:function(){
									eval(vat.bean().vatBeanOther.reportUrl);
						}}
			);
	//進貨差異表
	}else if(type == "4"){
			vat.bean().vatBeanOther.reportFileName  = "po0634.rpt";
			vat.block.submit(
						function(){return "process_object_name=imReceiveHeadMainService"+
									"&process_object_method_name=getReportConfig";},{other:true,
	                    			funcSuccess:function(){
									eval(vat.bean().vatBeanOther.reportUrl);
						}}
			);
	//效期驗收表
	}else if(type == "5"){
			vat.bean().vatBeanOther.reportFileName  = "po0644.rpt";
			vat.block.submit(
						function(){return "process_object_name=imReceiveHeadMainService"+
									"&process_object_method_name=getReportConfig";},{other:true,
	                    			funcSuccess:function(){
									eval(vat.bean().vatBeanOther.reportUrl);
						}}
			);
	}
	
	/*
	var processString;
		processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getReportURL" + 
							"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
							"&brandCode="     + vat.item.getValueByName("#F.brandCode") +
							"&employeeCode="  + vat.bean("employeeCode") +
							"&orderNo="       + vat.item.getValueByName("#F.orderNo") +
							"&reportName=po0206.rpt";
	vat.ajax.startRequest(processString,  function() {
		if (vat.ajax.handleState()){
			var returnURL = vat.ajax.getValue("returnURL", vat.ajax.xmlHttp.responseText);
			returnURL = returnURL.substring(0,returnURL.indexOf("','"));
			window.open(returnURL);
  			}
	 } );
	 */
}


function openReportWindow(){ 
  	var typeCode = vat.bean("typeCode");
  	var brandCode = vat.item.getValueByName("#F.brandCode");
  	//alert('typeCode = ' + typeCode);
    vat.bean().vatBeanOther.brandCode  = brandCode;
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
	vat.bean().vatBeanOther.warehouseInDate  = vat.item.getValueByName("#F.warehouseInDate");
	vat.bean().vatBeanOther.itemCategory  = vat.item.getValueByName("#F.itemCategory");
	  //
	if("T2" == brandCode){
		if("IMR" == typeCode)
			vat.bean().vatBeanOther.reportFileName = "po0631_1.rpt";
			//vat.bean().vatBeanOther.reportFileName = "po0631.rpt";
		else if("RR" == typeCode)
			vat.bean().vatBeanOther.reportFileName = "po0636.rpt";
	}else
		if("IMR" == typeCode)
			vat.bean().vatBeanOther.reportFileName = "po0206.rpt";
		else if("RR" == typeCode)
			vat.bean().vatBeanOther.reportFileName = "po0105_"+brandCode+".rpt";
		vat.block.submit(
				function(){return "process_object_name=imReceiveHeadMainService"+
							"&process_object_method_name=getReportConfig";},{other:true,
                   			funcSuccess:function(){
							eval(vat.bean().vatBeanOther.reportUrl);
				}}
	);
}


function doBarcode( barcodeType ){
	var width = "1024";
    var height = "768";
    var url;
		if( barcodeType == 1 ){
			url = "/erp/jsp/ExportFormView.jsp" + 
	              "?exportBeanName=IMR_ITEM" + 
	              "&fileType=TXT" + 
	              "&processObjectName=imReceiveHeadMainService" + 
	              "&processObjectMethodName=getAJAXExportDataBySql" + 
	              "&headId=" + vat.item.getValueByName("#F.headId")+
	              "&type=receiveBarcodeName";
		}else if ( barcodeType == 2 ){
			url = "/erp/jsp/ExportFormView.jsp" + 
	              "?exportBeanName=IMR_ITEM_WATCH" + 
	              "&fileType=TXT" + 
	              "&processObjectName=imReceiveHeadMainService" + 
	              "&processObjectMethodName=getAJAXExportDataBySql" + 
	              "&headId=" + vat.item.getValueByName("#F.headId")+
	              "&type=receiveBarcode";
		}else if ( barcodeType == 3 ){
			url = "/erp/jsp/ExportFormView.jsp" + 
	              "?exportBeanName=IMR_ITEM_T1" + 
	              "&fileType=TXT" + 
	              "&processObjectName=imReceiveHeadMainService" + 
	              "&processObjectMethodName=getAJAXExportDataBySql" + 
	              "&headId=" + vat.item.getValueByName("#F.headId")+
	              "&type=receiveBarcodeNew";
		}else if ( barcodeType == 4 ){
			url = "/erp/jsp/ExportFormView.jsp" + 
	              "?exportBeanName=IMR_ITEM_T2_JEWERLY" + 
	              "&fileType=TXT" + 
	              "&processObjectName=imReceiveHeadMainService" + 
	              "&processObjectMethodName=getAJAXExportDataBySql" + 
	              "&headId=" + vat.item.getValueByName("#F.headId")+
	              "&type=receiveBarcodeT2Jewerly";
		}
    	window.open(url, '進貨單條碼明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + 
    				 	 ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function changeFormStatus(formId, processId, status, approvalResult){
	//alert("formId:"+formId+":processId:"+processId+":status:"+status+":approvalResult:"+approvalResult);
    var formStatus = "";
    if(formId == null || formId == "" || status == "UNCONFIRMED"){
        formStatus = "SIGNING";
    }else if(processId != null && processId != "" && processId != 0){
        if(status == "SAVE" || status == "REJECT"){
            formStatus = "SIGNING";
        }else if (status == "SIGNING"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }else{
        	formStatus = status;
        }
    }else if(status == "FINISH"){
    	formStatus = "FINISH";
    }else if(status == "CLOSE"){
    	formStatus = "CLOSE";
    }else{
    	formStatus = "SIGNING";
    }
    return formStatus;
}


function doPickerWithPoPurchase(){
	//alert("Before and After Picker PO");
	vat.block.pageSearch(vatDetailDiv);
	//vat.block.pageSearch(4);
	changeItemCategory();
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


function execExtendItemInfo(){
	vat.block.submit(function()
		{return "process_object_name=imReceiveHeadMainService&process_object_method_name=updateAJAXImportItem";}, 
 			{bind:true, link:true, other:true, 
				funcSuccess:function(){
				
					vat.bean().vatBeanOther.processObjectName = "imReceiveHeadMainService";
    				vat.bean().vatBeanOther.searchMethodName  = "findById";
    				vat.bean().vatBeanOther.tableType         = "IM_RECEIVE_RETURN";
    				vat.bean().vatBeanOther.searchKey         = vat.item.getValueByName("#F.headId");
    				vat.bean().vatBeanOther.subEntityBeanName = "imReceiveItems";
    				vat.bean().vatBeanOther.itemFieldName     = "itemCode";
    				vat.bean().vatBeanOther.warehouseCodeFieldName = "";
   					vat.bean().vatBeanOther.declTypeFieldName = "";
    				vat.bean().vatBeanOther.declNoFieldName   = "originalDeclarationNo";
    				vat.bean().vatBeanOther.declSeqFieldName  = "originalDeclarationSeq";
    				vat.bean().vatBeanOther.declDateFieldName = "originalDeclarationDate";
    				vat.bean().vatBeanOther.qtyFieldName      = "quantity";
   					vat.bean().vatBeanOther.lotFieldName      = "lotNo";
   					vat.block.submit(function(){return  "process_object_name=appExtendItemInfoService"+
            											"&process_object_method_name=executeExtendItem";}, 
            											{other:true, funcSuccess: function() {vat.block.pageRefresh(vatDetailDiv);}});
					vat.item.bindAll();
					vat.block.pageDataLoad( vatDetailDiv, vnCurrentPage = 1);
					changeItemCategory();
   				}
			});
}


function doFormAccessControl(){
	var processId     	= document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
	var orderTypeCode 	= vat.item.getValueByName("#F.orderTypeCode");
	var orderNo       	= vat.item.getValueByName("#F.orderNo");
    var formStatus    	= vat.item.getValueByName("#F.status");
    var branchCode      = vat.bean("branchCode");
    var brandCode       = vat.item.getValueByName("#F.brandCode");
    var userType        = vat.bean("userType");
    var warehouseStatus = vat.bean("warehouseStatus");
	var expenseStatus   = vat.bean("expenseStatus");
	var typeCode        = vat.bean("typeCode");
	var canBeClaimed	= document.forms[0]["#canBeClaimed"].value;
	var allowApproval 	= document.forms[0]["#allowApproval"].value;
	var canBeMod		= document.forms[0]["#canBeMod"].value;
	var monthlyCloseMonth = vat.bean("monthlyCloseMonth");
	var closeYear = monthlyCloseMonth.substring(0,4);
	var closeMonth = monthlyCloseMonth.substring(4,6);
	var receiptDate = vat.item.getValueByName("#F.receiptDate");
	var receiptYear = receiptDate.substring(0,4);
	var receiptMonth = receiptDate.substring(5,7);
	var canModClose = closeYear<receiptYear||((closeYear==receiptYear)&&(closeMonth<receiptMonth));
	getCustomsDesc();
	checkCustomsStatus();  	
    vat.item.setStyleByName("#B.new",    	"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.void",      "display", "none");  
    vat.item.setStyleByName("#B.import",    "display", "inline");
	vat.item.setStyleByName("#B.save",      "display", "inline");
	vat.item.setStyleByName("#B.submitBG",  "display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.message",   "display", "inline");
	vat.item.setStyleByName("#B.import",    "display", "inline");
	vat.item.setStyleByName("#B.expense",	"display", "inline");
	vat.item.setStyleByName("#B.export",    "display", "inline");
	vat.item.setStyleByName("#B.importPoLine",      "display", "none");
    vat.item.setStyleByName("#B.importDeclaration", "display", "none");
	vat.item.setStyleByName("#B.extendItem",        "display", "none");
	vat.item.setStyleByName("#B.listReport1",		"display", "inline");
	vat.item.setStyleByName("#B.listReport2",		"display", "none");

	if("2" != branchCode){
		vat.item.setStyleByName("#B.listReport3",		"display", "none");
		vat.item.setStyleByName("#B.listReport4",		"display", "none");
	}
	
	vat.item.setStyleByName("#B.barcode1",	"display", "none");
	vat.item.setStyleByName("#B.barcode2",	"display", "none");
	vat.item.setStyleByName("#B.barcode3",	"display", "none");
	vat.item.setStyleByName("#B.barcode4",	"display", "none");
	vat.item.setStyleByName("#B.saveDate",	"display", "none");
	
	//for 儲位用
	if(enableStorage){
		vat.item.setStyleByName("#B.storageExport"   , "display", "inline");
		vat.item.setStyleByName("#B.storageImport"   , "display", "inline");
	}else{
		vat.item.setStyleByName("#B.storageExport"   , "display", "none");
		vat.item.setStyleByName("#B.storageImport"   , "display", "none");
	}

	if(canBeClaimed == "true")
		vat.item.setStyleByName("#B.submit",    "display", "none");
	
    if(allowApproval == "N" || formStatus=="SAVE" || formStatus=="REJECT" || (userType=="WAREHOUSE" && warehouseStatus=="FINISH" || userType=="WAREHOUSE2" && warehouseStatus=="FINISH"))
    	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);//by wade(倉管主管不能駁回)
    	
	if(processId==""){
		if(orderNo.indexOf("TMP") == -1){
			vat.item.setStyleByName("#B.import",    "display", "none");
			
			//for 儲位用
			if(enableStorage){
				vat.item.setStyleByName("#B.storageImport"   , "display", "none");
			}
			
			vat.item.setStyleByName("#B.save",      "display", "none");
			vat.item.setStyleByName("#B.submitBG",  "display", "none");
			vat.item.setStyleByName("#B.submit", 	"display", "none");
			vat.item.setStyleByName("#B.message",   "display", "none");
			vat.item.setAttributeByName("vatBlock_Head",         "readOnly", true, true, true);
			vat.item.setAttributeByName("vatMasterDiv",          "readOnly", true, true, true);
			vat.block.canGridModify( [vatDetailDiv], false, false, false );
			vat.block.canGridModify( [vatExpenseDiv], false, false, false );
		}else{
			if( formStatus=="SAVE" || formStatus=="REJECT" ){
				if(userType=="SHIPPING" && (orderTypeCode=="IRF" || orderTypeCode=="EIF"))	// IRF, EIF
		    		vat.item.setStyleByName("#B.importDeclaration", "display", "inline");
		
		   		if(userType=="SHIPPING" && (orderTypeCode=="IRL" || orderTypeCode=="EIP"))	// IRL, EIP
		    		vat.item.setStyleByName("#B.importPoLine",   "display", "inline");
			
				if(userType=="SHIPPING" && orderTypeCode=="EOF")
					vat.item.setStyleByName("#B.extendItem", "display", "inline");
			}
			vat.item.setAttributeByName("vatBlock_Head",         "readOnly", false, true, true);
			vat.item.setAttributeByName("vatMasterDiv",          "readOnly", false, true, true);
			vat.block.canGridModify( [vatDetailDiv], true, true, true );
			vat.block.canGridModify( [vatExpenseDiv], true, true, true );
		}
		
		//特殊修改開啟
		if(canBeMod == "Y" && formStatus=="FINISH"){
			vat.item.setStyleByName("#B.submit", "display", "inline");
			vat.item.setAttributeByName("#F.currencyCode", "readOnly", false);
			vat.item.setAttributeByName("#F.exchangeRate", "readOnly", false);
			vat.block.canGridModify( [vatDetailDiv], true, false, false );
			vat.block.canGridModify( [vatExpenseDiv], true, true, true );
			vat.item.setGridAttributeByName("itemCode", "readOnly", true);
			vat.item.setGridAttributeByName("localUnitPrice", "readOnly", true);
			vat.item.setGridAttributeByName("invoiceNo",   "readOnly", true);
			vat.item.setGridAttributeByName("originalDeclarationDate",   "readOnly", true);
			vat.item.setGridAttributeByName("originalDeclarationNo",   "readOnly", true);
			vat.item.setGridAttributeByName("originalDeclarationSeq",   "readOnly", true);
		}
	}else{
		vat.item.setStyleByName("#B.new",  "display", "none");
		vat.item.setStyleByName("#B.search", "display", "none");
		if(formStatus=="SAVE" || formStatus=="FORM_SAVE" || formStatus=="REJECT"){
			if(userType=="SHIPPING"){
				vat.item.setStyleByName("#B.void", "display", "inline");
				if( formStatus=="SAVE" || formStatus=="REJECT" ){
					if((orderTypeCode=="IRF" || orderTypeCode=="EIF"))	// IRF, EIF
			    		vat.item.setStyleByName("#B.importDeclaration", "display", "inline");
			    		
			    	if("2" != branchCode && orderTypeCode=="IRF")
		    			vat.item.setAttributeByName("vatMasterDiv", "readOnly", false, true, true);
			
			   		if((orderTypeCode=="IRL" || orderTypeCode=="EIP"))	// IRL, EIP
			    		vat.item.setStyleByName("#B.importPoLine", "display", "inline");
				
					if(orderTypeCode=="EOF")
						vat.item.setStyleByName("#B.extendItem", "display", "inline");
				}
			}
		}else{
			vat.item.setStyleByName("#B.message",	"display", "none");
			vat.item.setStyleByName("#B.save",  	"display", "none");
			vat.item.setStyleByName("#B.submitBG",  "display", "none");
			vat.item.setStyleByName("#B.import",    "display", "none");
			
			//for 儲位用
			if(enableStorage){
				vat.item.setStyleByName("#B.storageImport"   , "display", "none");
			}
				
			vat.item.setAttributeByName("vatBlock_Head",         "readOnly", true, true, true);
			vat.item.setAttributeByName("vatMasterDiv",          "readOnly", true, true, true);

			//如果是倉管進貨的話 還可以調整數量
			if( userType=="WAREHOUSE" && warehouseStatus=="SAVE" || userType=="WAREHOUSE2" && warehouseStatus=="SAVE" ){
				vat.item.setAttributeByName("#F.warehouseInDate", "readOnly", false);
				vat.item.setAttributeByName("#F.receiptDate", "readOnly", false);
				vat.block.canGridModify( [vatDetailDiv], true, false, false );
				
				//for 儲位用
				if(enableStorage){
					vat.item.setStyleByName("#B.storageImport"   , "display", "inline");
				}
				
			}else{
				vat.block.canGridModify( [vatDetailDiv], false, false, false );
			}
		}
		
		if(warehouseStatus=="FINISH"){
			vat.item.setStyleByName("#B.save",      "display", "none");
			vat.item.setGridAttributeByName("itemCode",         "readOnly", true);
			vat.item.setGridAttributeByName("quantity",         "readOnly", true);
			vat.item.setGridAttributeByName("foreignUnitPrice", "readOnly", true);
			vat.item.setGridAttributeByName("localUnitPrice",   "readOnly", true);
			vat.item.setGridAttributeByName("invoiceNo",        "readOnly", true);
			vat.item.setGridAttributeByName("poOrderNo",        "readOnly", true);
			vat.item.setAttributeByName("#F.itemCategory", "readOnly", true);
			vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true);
			vat.item.setAttributeByName("vatBlock_Head",         "readOnly", true, true, true);
			vat.block.canGridModify( [vatDetailDiv], true, false, false);
		}
		
		if(expenseStatus != "FINISH" && userType != "WAREHOUSE" || expenseStatus != "FINISH" && userType != "WAREHOUSE2"){
			vat.item.setAttributeByName("#F.orderDate", "readOnly", false);
			vat.item.setAttributeByName("#F.warehouseInDate", "readOnly", false);
			vat.item.setAttributeByName("#F.receiptDate", "readOnly", false);
			vat.item.setAttributeByName("#F.lcNo", "readOnly", false);
			vat.item.setAttributeByName("#F.lcUseAmount", "readOnly", false);
			vat.item.setAttributeByName("#F.lcNo1", "readOnly", false);
			vat.item.setAttributeByName("#F.lcUseAmount1", "readOnly", false);
			vat.item.setAttributeByName("vatMasterDiv", "readOnly", false, true, true);
			vat.block.canGridModify( [vatExpenseDiv], true, true, true );
		}else{
			vat.block.canGridModify( [vatExpenseDiv], false, false, false );
			vat.item.setStyleByName("#B.expense",    "display", "none");
		}
	}
	
	if(processId!="" || orderNo.indexOf("TMP") == -1){
		vat.tabm.displayToggle(0, "xTab5", true, false, false);
     	refreshWfParameter( vat.item.getValueByName("#F.brandCode"), 
     		   				vat.item.getValueByName("#F.orderTypeCode"),
     		   				vat.item.getValueByName("#F.orderNo" ) );
		vat.block.pageDataLoad(102, vnCurrentPage = 1); 
	}else{
		vat.tabm.displayToggle(0, "xTab5", false, false, false);
	}

	if(userType=="WAREHOUSE" || userType=="WAREHOUSE2"){	
		vat.item.setStyleByName("#B.produceIC",     "display", "none");
		vat.item.setStyleByName("#B.listReport1",	"display", "none");
		vat.item.setStyleByName("#B.saveDate",      "display", "inline");
		vat.item.setStyleByName("#B.import",        "display", "none");
		vat.item.setStyleByName("#B.expense",		"display", "none");
		vat.item.setStyleByName("#B.listReport2",	"display", "inline");
		vat.item.setStyleByName("#B.listReport3",	"display", "inline");
		vat.item.setStyleByName("#B.barcode1", 		"display", "inline");
		vat.item.setStyleByName("#B.barcode2", 		"display", "inline");
		vat.item.setStyleByName("#B.barcode3", 		"display", "inline");
		vat.item.setStyleByName("#B.barcode4",		"display", "inline");
	}

	if(userType=="SHIPPING" || userType=="SHIPPING2"){
		vat.item.setGridAttributeByName("acceptQuantity", "readOnly", true);
		vat.item.setGridAttributeByName("defectQuantity", "readOnly", true);
		vat.item.setGridAttributeByName("sampleQuantity", "readOnly", true);
	}
	
	// 船務輸入費用時
	if(userType=="SHIPPING2" && expenseStatus!="FINISH"){
		vat.item.setGridAttributeByName("foreignUnitPrice", "readOnly", false);
		vat.item.setAttributeByName("#F.orderDate",    "readOnly", false);	//歸帳日期
		vat.item.setAttributeByName("#F.currencyCode", "readOnly", false);  //幣別
		vat.item.setAttributeByName("#F.exchangeRate", "readOnly", false);	//匯率
	}
	
	// 船務輸入費用時
	if(userType=="SHIPPING2" && orderTypeCode=="EOF"){
		vat.item.setAttributeByName("#F.declarationNo",    "readOnly", false);	//報單號碼
	}
	
	//歷史歷程開啟
	if(userType=="SHIPPING" && vat.item.getValueByName("#F.sourceOrderNo") != "" && formStatus=="SAVE"){
		vat.item.setStyleByName("#B.submit", "display", "inline");
		vat.item.setStyleByName("#B.message", "display", "inline");
	}

	if("2" == branchCode){
		vat.item.setStyleByName("#B.barcode1", 		"display", "none");
		vat.item.setStyleByName("#B.barcode2", 		"display", "none");
		vat.item.setStyleByName("#B.barcode3", 		"display", "none");
	}else{
		vat.item.setStyleByName("#B.barcode4",		"display", "none");
	}
	
	vat.block.pageDataLoad( vatDetailDiv, vnCurrentPage = 1);
	vat.block.pageDataLoad( vatExpenseDiv, vnCurrentPage = 1);
	
}

// 新增空白頁
function appendBeforeService(){
	return true;
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
						return "process_object_name=imReceiveHeadMainService&process_object_method_name=updateCustomsStatus"; 
	    		},{link:true, other: true,
	    			funcSuccess:function(){
					window.top.close();
					}
    			}
    		);	
		}
		
}
function getCustomsDesc(){
	//alert(vat.item.getValueByName("#F.customsStatusHidden"));
	var customsDesc = "";
	if(vat.item.getValueByName("#F.customsStatusHidden")!==""){
		vat.ajax.XHRequest({
          post:"process_object_name=imReceiveHeadMainService"+
                   "&process_object_method_name=getCustomsProcessResponse"+
                   "&customsStatus=" + vat.item.getValueByName("#F.customsStatusHidden"),
          find: function change(oXHR){
          		//alert("success");
          		var response = vat.ajax.getValue("response" , oXHR.responseText);
          		vat.item.setValueByName("#F.customsDesc", response);
          		//alert(response);
          },
          fail: function changeError(){
          		//alert("fail");
          }
		});
	}else{
		//alert("NULL");
	}
}
