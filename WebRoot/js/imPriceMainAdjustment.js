/**
 * 	訂變價 免稅
 */
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;
var vnB_Total = 4;

function kweAdjustmentBlock(){
	formInitial();
	buttonLine();
	headerInitial();
  	
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0,"xTab1","商品資料"   ,"vatItemDiv"                   ,"images/tab_item_data_dark.gif"            ,"images/tab_item_data_light.gif" , false, "doPageRefresh()");
		vat.tabm.createButton(0,"xTab2","簽核資料"   ,"vatApprovalDiv"             ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif");
	}
	detailInitial();
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
	           vat.item.getValueByName("#F.orderTypeCode"), 
	           vat.item.getValueByName("#F.orderNo"),
	           document.forms[0]["#loginEmployeeCode"].value );
	doFormAccessControl();           
}

function formInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	 loginBrandCode	    = document.forms[0]["#loginBrandCode"    ].value;  	
  	 loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" ].value;	  
  	 orderTypeCode      = document.forms[0]["#orderTypeCode"     ].value; 
     processId          = document.forms[0]["#processId"         ].value;       
	 formId             = document.forms[0]["#formId"            ].value;
	 assignmentId       = document.forms[0]["#assignmentId"      ].value;
	 vat.bean().vatBeanOther={
		loginBrandCode:		loginBrandCode,					
	  	processId : 		processId,
	  	loginEmployeeCode : loginEmployeeCode,
	  	orderTypeCode:		orderTypeCode,
	    formId :			formId,
	    assignmentId : 		assignmentId
	 };
     vat.bean.init(function(){
		return "process_object_name=imPriceAdjustmentAction&process_object_method_name=performInitial"; 
     },{other: true});
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
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_ItemPriceAdjustment:search:20090814.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 servicePassData:function(){ return doPassData("buttonLine");},   
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"#B.message"    , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},	 			
	 			{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
	 			{name:"#B.import" 	   ,type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  , 
	 									 openMode:"open", 
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,	
	 									 serviceAfterPick:function(){
	 									 	changeSupplierCode();
	 									 }},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){
	
	var allItemCategory    = vat.bean("allItemCategory");
	var allPurchaseAssist  = vat.bean("allPurchaseAssist");
	var allPurchaseMember  = vat.bean("allPurchaseMember");
	var allPurchaseMaster  = vat.bean("allPurchaseMaster");
	
	var allOrderTypes = vat.bean("allOrderTypes");
	var allPriceTypes = vat.bean("allPriceTypes");
	var allCurrencys = vat.bean("allCurrencys");
	
	var orderTypeCode = vat.bean().vatBeanOther.orderTypeCode;
	
	var titleName = "";

	if("PAP" == orderTypeCode){
		titleName = "新品訂價維護作業";
	}else if("PAJ" == orderTypeCode){
		titleName = "商品變價維護作業";
	}

	vat.block.create(vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:titleName, rows:[  
		 {row_style:"", cols:[
			 {items:[{name:"#L_orderType", type:"LABEL" , value:"單別"}]},	 
			 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode",init:allOrderTypes, mode:"READONLY"}]},		 
			 {items:[{name:"#L_orderNo"  , type:"LABEL" , value:"單號"},
			 		{name:"#formId"   , type:"TEXT"  ,  bind:"formId", back:false, size:10, mode:"HIDDEN" }]},
			 {items:[{name:"#F.orderNo"  , type:"TEXT"  ,  bind:"orderNo", size:20, mode:"READONLY"},
			 		 {name:"#F.headId"   , type:"TEXT"  ,  bind:"headId", back:false, size:10, mode:"READONLY" }]},
			 {items:[{name:"#L_brandCode", type:"LABEL" , value:"品牌"}]},
			 {items:[{name:"#F.brandCode", type:"TEXT"  ,  bind:"brandCode", size:8, mode:"HIDDEN"},
			 		  {name:"#F.brandName", type:"TEXT"  ,  bind:"brandName", size:8, mode:"READONLY"}]},
			 {items:[{name:"#L_status"   , type:"LABEL" , value:"狀態"}]},	 		 
			 {items:[{name:"#F.statusName", type:"TEXT"  ,  bind:"statusName", size:8, mode:"READONLY"},
			 		{name:"#F.status"   , type:"TEXT"  ,  bind:"status", size:12, mode:"HIDDEN"}]}		   
			 ]},
		 {row_style:"", cols:[
		 	{items:[{name:"#L_enableDate", type:"LABEL",  value:"啟用日期<font color='red'>*</font>"}]},
			{items:[{name:"#F.enableDate", type:"DATE",  bind:"enableDate", size:1}]}, 
			{items:[{name:"#L_priceType", type:"LABEL",  value:"價格類型<font color='red'>*</font>"}]},
			{items:[{name:"#F.priceType", type:"SELECT",  bind:"priceType", size:1,init:allPriceTypes}]},
			{items:[{name:"#L_createdBy", type:"LABEL", value:"填單人員"}]},
			{items:[{name:"#F.createdBy" , type:"TEXT",   bind:"createdBy",  mode:"HIDDEN", size:12},
			 		{name:"#F.createdByName" , type:"TEXT",   bind:"createdByName",  mode:"READONLY", size:12}]},	 
			{items:[{name:"#L_creationDate" , type:"LABEL", value:"填單日期"}]},
		 	{items:[{name:"#F.creationDate" , type:"TEXT",   bind:"creationDate", mode:"READONLY", size:12}]}
		 ]},
		{row_style:"", cols:[
			{items:[{name:"#L.itemCategory", type:"LABEL", value:"業種子類<font color='red'>*</font>"}]},
		 	{items:[{name:"#F.itemCategory" , type:"SELECT",   bind:"itemCategory", size:1, init:allItemCategory, onchange:"changeItemCategory()"}]},
	 		{items:[{name:"#L.purchaseAssist", type:"LABEL", value:"採購助理<font color='red'>*</font>"}]},
		 	{items:[{name:"#F.purchaseAssist" , type:"SELECT",   bind:"purchaseAssist", size:1, init:allPurchaseAssist}]},
		 	{items:[{name:"#L.purchaseMember", type:"LABEL", value:"採購人員<font color='red'>*</font>"}]},
		 	{items:[{name:"#F.purchaseMember" , type:"SELECT",   bind:"purchaseMember", size:1, init:allPurchaseMember}]},	
		 	{items:[{name:"#L.purchaseMaster" , type:"LABEL", value:"採購主管<font color='red'>*</font>"}]},
	 	 	{items:[{name:"#F.purchaseMaster" , type:"SELECT",   bind:"purchaseMaster", size:1, init:allPurchaseMaster}]}
	 	]},
		{row_style:"", cols:[
	 	  {items:[{name:"#L_description", type:"LABEL", value:"說明"}]},
			 {items:[{name:"#F.description" , type:"TEXT",   bind:"description", size:50, maxLen:200,desc:"一般說明"}], td:" colSpan=3"},
		  {items:[{name:"#L_supplierCode", type:"LABEL" , value:"廠商代號"}]},	 
			 {items:[{name:"#F.supplierCode", type:"TEXT",  bind:"supplierCode", eChange: function(){ changeSupplierName("supplierCode"); } ,size:20},
			  		 {name:"#B.supplierCode", value:"選取" ,type:"PICKER" ,
			 									 		openMode:"open", src:"./images/start_node_16.gif",
			 									 		service:"Bu_AddressBook:searchSupplier:20091011.page", 
			 									 		left:0, right:0, width:1024, height:768,	
			 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess();} },
				 	{name:"#F.addressBookId", 		type:"TEXT",  	bind:"addressBookId", back:false, mode:"HIDDEN"},								 		
					{name:"#F.supplierName", type:"TEXT", bind:"supplierName", back:false, size:30 , mode:"READONLY"}], td:" colSpan=3"}
	 	]}			
		 
		 
		],	 
		 beginService:"",
		 closeService:""			
	});
}

// 商品定變價
function detailInitial() {
	var allCurrencys = vat.bean("allCurrencys");
	
	var status = vat.item.getValueByName("#F.status");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var userType = document.forms[0]["#userType"].value;
//	var modeType="mode";
	if(""==orderTypeCode){
		orderTypeCode="PAP";
	}
	
	var varCanDataDelete = true;
	var varCanDataAppend = true;
	var varCanDataModify = true;

	if(orderTypeCode=="PAP"){
		vat.item.make(vnB_Detail, "indexNo", {type:"IDX", view:"fixed", desc:"序號"}); 
		vat.item.make(vnB_Detail, "category02", {type:"TEXT", view:"fixed", size:20, maxLen:20, desc:"中類", mode:"READONLY"});
		vat.item.make(vnB_Detail, "itemCode", {type:"TEXT", view:"fixed", size:16, maxLen:20, desc:"品號", onchange:"onChangeItemCode('-1')"});// 
		vat.item.make(vnB_Detail, "searchItem",  {type:"PICKER", view:"", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 			service:"Im_ItemPriceAdjustment:searchPriceView:20091225.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			servicePassData:function(){ return doPassData("PAP"); },
	 									 			serviceAfterPick:function(id){doAfterPickerLineFunctionProcess(id); }}); 
		
		vat.item.make(vnB_Detail, "itemCName", {type:"TEXT", view:"", size:20, maxLen:20, desc:"品名", mode:"READONLY"});
		vat.item.make(vnB_Detail, "currencyCode", {type:"SELECT", view:"", size:1, desc:"幣別", init:allCurrencys, eChange:"calculateLineRate('currencyCode')"});  
		vat.item.make(vnB_Detail, "exchangeRate", {type:"NUMB", view:"", size:8, desc:"匯率", onchange:"calculateLineRate('exchangeRate')" });                                  
		vat.item.make(vnB_Detail, "foreignCost", {type:"NUMB", view:"", size:8, maxLen:20, desc:"原幣成本", mode:"READONLY"}); 
		vat.item.make(vnB_Detail, "localCost", {type:"NUMB", view:"", size:8, maxLen:20, desc:"台幣成本",onchange:"calculateLineRate('localCost')"});   
		vat.item.make(vnB_Detail, "unitPrice", {type:"NUMB", view:"", size:8, maxLen:20, desc:"送簽價格", onchange:"calculateLineRate('unitPrice')"});
		vat.item.make(vnB_Detail, "grossRate", {type:"NUMB", view:"shift", size:8, maxLen:20, desc:"毛利率(%)", mode:"READONLY"});
		vat.item.make(vnB_Detail, "priceId", {type:"NUMB", size:8, maxLen:20, desc:"ID", mode:"HIDDEN"});
		
		vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
		vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});                                         
		vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", view:"fixed", desc:"刪除"});                                                          
		vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});    
	}else if (orderTypeCode=="PAJ"){
		vat.item.make(vnB_Detail, "indexNo", {type:"IDX", view:"fixed", desc:"序號"}); 
		vat.item.make(vnB_Detail, "itemCode", {type:"TEXT", view:"fixed", size:16, maxLen:20, desc:"品號", onchange:"onChangeItemCode('-1')"});    
		vat.item.make(vnB_Detail, "searchItem", {type:"PICKER", view:"", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 			service:"Im_ItemPriceAdjustment:searchPriceView:20091225.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			servicePassData:function(){ return doPassData("PAJ"); },
	 									 			serviceAfterPick:function(id){doAfterPickerLineFunctionProcess(id); } }); 
	 	vat.item.make(vnB_Detail, "category02", {type:"TEXT", view:"", size:10, desc:"中類", mode:"READONLY"});
	 	vat.item.make(vnB_Detail, "category02Name", {type:"TEXT", view:"", size:10, desc:"中類名稱", mode:"READONLY"});
	 	
		vat.item.make(vnB_Detail, "itemCName", {type:"TEXT", view:"", size:20, maxLen:20, desc:"品名", mode:"READONLY"});                                   
		vat.item.make(vnB_Detail, "originalCurrencyCode", {type:"SELECT", view:"", size:1, desc:"原幣幣別(舊)", init:allCurrencys, mode:"READONLY"}); 
		vat.item.make(vnB_Detail, "currencyCode", {type:"SELECT", view:"", size:1, desc:"原幣幣別(新)", init:allCurrencys, onchange:"calculateLineRate('currencyCode')"});
		vat.item.make(vnB_Detail, "exchangeRate", {type:"NUMB", view:"", size:8, desc:"匯率(新)", onchange:"calculateLineRate('exchangeRate')"});      
		   
		vat.item.make(vnB_Detail, "originalForeignCost", {type:"NUMB", view:"", size:8, maxLen:20, desc:"原幣成本(舊)", mode:userType=="ITEM"?"READONLY":"HIDDEN"}); 
		vat.item.make(vnB_Detail, "foreignCost", {type:"NUMB", view:"shift", size:8, maxLen:20, desc:"原幣成本(新)", onchange:"calculateLineRate('foreignCost')", mode:userType=="ITEM"?"":"HIDDEN"});  
		
		vat.item.make(vnB_Detail, "originalPrice", {type:"NUMB", view:"shift", size:8, maxLen:20, desc:"零售價(舊)", mode:"READONLY"});   
		vat.item.make(vnB_Detail, "unitPrice", {type:"NUMB", view:"shift", size:8, maxLen:20, desc:"零售價(新)", onchange:"calculateLineRate('unitPrice')"});
		vat.item.make(vnB_Detail, "originalGrossRate", {type:"NUMB", view:"shift", size:8, maxLen:20, desc:"毛利率%(舊)", mode:userType=="ITEM"?"READONLY":"HIDDEN", back:false});
		vat.item.make(vnB_Detail, "grossRate", {type:"NUMB", view:"shift", size:8, maxLen:20, desc:"毛利率%(新)", mode:userType=="ITEM"?"READONLY":"HIDDEN", back:false});
		vat.item.make(vnB_Detail, "totalStock", {type:"NUMB", view:"shift", size:8, maxLen:20, desc:"總庫存數量", mode:"READONLY", back:false});
		
		vat.item.make(vnB_Detail, "priceId", {type:"NUMB", size:8, maxLen:20, desc:"ID", mode:"HIDDEN"});
		vat.item.make(vnB_Detail, "isTax", {type:"TEXT", size:8, maxLen:20, desc:"isTax", mode:"HIDDEN"});
		vat.item.make(vnB_Detail, "typeCode", {type:"TEXT", size:8, maxLen:20, desc:"typeCode", mode:"HIDDEN"});
		vat.item.make(vnB_Detail, "taxCode", {type:"TEXT", size:8, maxLen:20, desc:"taxCode", mode:"HIDDEN"});
		vat.item.make(vnB_Detail, "originalExchangeRate", {type:"TEXT", size:8, desc:"originalExchangeRate", mode:"HIDDEN"});
		
		vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
		vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});                                         
		vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", view:"fixed", desc:"刪除"});                                                          
		vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});    
	}
	                                                              
	vat.block.pageLayout(vnB_Detail, {	id: "vatItemDiv",
								pageSize: 10,
	                            canGridDelete:varCanDataDelete,
								canGridAppend:varCanDataAppend,
								canGridModify:varCanDataModify,														
							    appendBeforeService : "appendBeforeService()",
							    appendAfterService  : "appendAfterService()",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "loadSuccessAfter()",
								eventService        : "changeRelationData",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

// 單身品號echange or from picker
function onChangeItemCode( priceId , id) {
	var nItemLine = vat.item.getGridLine(id);
	var sItemCode = vat.item.getGridValueByName("itemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
//	var currencyCode = vat.item.getGridValueByName("currencyCode", nItemLine);
	
	if ( priceId != "-1" || sItemCode != "" ) {
//		alert("after\npriceId = " + priceId + "\nsItemCode = " + sItemCode);
		var processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXT2LineData"+ 
							"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
							"&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode") + 
							"&itemCode=" + (priceId ==="-1" ? sItemCode : "") +
//						 	"&currencyCode=" + currencyCode +
						 	"&priceType="+vat.item.getValueByName("#F.priceType") +
						 	"&priceId="+(priceId ==="-1" ? "" : priceId);
		vat.ajax.XHRequest({	
			post:processString,					 	
			find: function change(oXHR){ 
				if("PAP"==orderTypeCode){
					if( priceId != "-1" ){
						vat.item.setGridValueByName("itemCode",nItemLine, vat.ajax.getValue("itemCode",oXHR.responseText));
					}else{ // 表示手打的不存在
						vat.item.setGridValueByName("itemCode",nItemLine, sItemCode.replace(/^\s+|\s+$/, '').toUpperCase() );
					}
					
					vat.item.setGridValueByName("currencyCode",nItemLine, vat.ajax.getValue("currencyCode",oXHR.responseText));
					vat.item.setGridValueByName("exchangeRate",nItemLine, vat.ajax.getValue("exchangeRate",oXHR.responseText));
					vat.item.setGridValueByName("category02",nItemLine, vat.ajax.getValue("category02",oXHR.responseText));
					vat.item.setGridValueByName("unitPrice", nItemLine,  vat.ajax.getValue("unitPrice", oXHR.responseText));
					vat.item.setGridValueByName("foreignCost",nItemLine,  vat.ajax.getValue("foreignCost", oXHR.responseText));
					vat.item.setGridValueByName("localCost",nItemLine,  vat.ajax.getValue("localCost", oXHR.responseText));
					vat.item.setGridValueByName("itemCName",nItemLine,  vat.ajax.getValue("itemCName", oXHR.responseText));
					vat.item.setGridValueByName("grossRate",nItemLine,  vat.ajax.getValue("grossRate", oXHR.responseText));
					vat.item.setGridValueByName("priceId",nItemLine,  vat.ajax.getValue("priceId", oXHR.responseText));
				}else if("PAJ"==orderTypeCode){
					if( priceId != "-1" ){
						vat.item.setGridValueByName("itemCode",nItemLine, vat.ajax.getValue("itemCode",oXHR.responseText));
					}else{ // 表示手打的不存在
						vat.item.setGridValueByName("itemCode",nItemLine, sItemCode.replace(/^\s+|\s+$/, '').toUpperCase() );
					}							
					vat.item.setGridValueByName("category02",nItemLine, vat.ajax.getValue("category02",oXHR.responseText));
					vat.item.setGridValueByName("category02Name",nItemLine, vat.ajax.getValue("category02Name",oXHR.responseText));
					vat.item.setGridValueByName("originalCurrencyCode",nItemLine, vat.ajax.getValue("originalCurrencyCode",oXHR.responseText));	
					vat.item.setGridValueByName("currencyCode",nItemLine, vat.ajax.getValue("currencyCode",oXHR.responseText));
					
					vat.item.setGridValueByName("originalExchangeRate",nItemLine, vat.ajax.getValue("originalExchangeRate",oXHR.responseText));
					vat.item.setGridValueByName("exchangeRate",nItemLine, vat.ajax.getValue("exchangeRate",oXHR.responseText));
					
					vat.item.setGridValueByName("originalForeignCost",nItemLine, vat.ajax.getValue("originalForeignCost",oXHR.responseText));	
					vat.item.setGridValueByName("foreignCost",nItemLine, vat.ajax.getValue("foreignCost",oXHR.responseText));	
					
					vat.item.setGridValueByName("originalPrice",nItemLine, vat.ajax.getValue("orginalPrice",oXHR.responseText));
					vat.item.setGridValueByName("unitPrice", nItemLine,  vat.ajax.getValue("unitPrice", oXHR.responseText));
					
					vat.item.setGridValueByName("originalGrossRate",nItemLine, vat.ajax.getValue("originalGrossRate",oXHR.responseText));
					vat.item.setGridValueByName("grossRate", nItemLine,  vat.ajax.getValue("grossRate", oXHR.responseText));
					
					vat.item.setGridValueByName("totalStock", nItemLine,  vat.ajax.getValue("totalStock", oXHR.responseText));
					
					vat.item.setGridValueByName("itemCName",nItemLine,  vat.ajax.getValue("itemCName", oXHR.responseText));
					vat.item.setGridValueByName("priceId",nItemLine,  vat.ajax.getValue("priceId", oXHR.responseText));
					vat.item.setGridValueByName("isTax",nItemLine,  vat.ajax.getValue("isTax", oXHR.responseText));
					vat.item.setGridValueByName("typeCode",nItemLine,  vat.ajax.getValue("typeCode", oXHR.responseText));
					vat.item.setGridValueByName("taxCode",nItemLine,  vat.ajax.getValue("taxCode", oXHR.responseText));
				}						
			}
		});
	} else {
	}

}

// 載入明細後
function loadSuccessAfter(){
	// alert("載入成功");	
//    vat.block.pageRefresh(vnB_Detail);
}

// 新增空白頁
function appendBeforeService(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}

// 新增空白頁之後
function appendAfterService(){
	// return alert("新增完畢");
}

//	LINE資料 第一次載入或更新後的讀取
function loadBeforeAjxService() {
	
	var processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXT2PageData" +
			"&headId=" + vat.item.getValueByName("#F.headId")+
			"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
			"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") + 
			"&priceType=" + vat.item.getValueByName("#F.priceType");
	return processString;
}

/*
	載入LINE資料 SUCCESS
*/
function loadSuccessAfter() {
}
/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=updateAJAXPageLinesData" + 
						"&headId=" + vat.item.getValueByName("#F.headId")+ 
						"&status=" + vat.item.getValueByName("#F.status") + 
						"&brandCode=" + vat.item.getValueByName("#F.brandCode")  + 
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode;
		
	}
	return processString;
}
/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
//	vat.block.pageRefresh(vnB_Detail);
	
/*	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg === "") {
		if ("saveHandler" == afterSavePageProcess) {	
			executeCommandHandler("main", "saveHandler");
		} else if ("submitHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "submitHandler");
		}else if ("voidHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "voidHandler");
		}else if("executeExport"==afterSavePageProcess){
			exportFormData();
		}else if("executeImport"==afterSavePageProcess){
			importFormData();
		}
	}else{
		alert("錯誤訊息： " + errorMsg);
	}
	afterSavePageProcess = "";*/
}
/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
	//alert("doSaveHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			vat.block.pageDataSave(0);
			afterSavePageProcess = "saveHandler";
		}
	}
}

function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SIGNING" == formAction){
		alertMessage = "是否確定送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
			alertMessage = "是否確定背景送出?";
	}
		
	if(confirm(alertMessage)){
		var formId = vat.item.getValueByName("#formId").replace(/^\s+|\s+$/, '');;
	    var processId = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
	    var status = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');;
	    var employeeCode = vat.bean().vatBeanOther.loginEmployeeCode.replace(/^\s+|\s+$/, '');;
	    var headId = vat.item.getValueByName("#F.headId");
	    var inProcessing   	= !(processId == null || processId == ""  || processId == 0);
	    var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	    var approvalResult 	= getApprovalResult();
	    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	    var assignmentId = vat.item.getValueByName("#assignmentId");
	    
		if((orderNoPrefix == "TMP" &&  status == "SAVE") || status == "UNCONFIRMED" ||
			(inProcessing   && (status == "SAVE"  || status == "SIGNING" || status == "REJECT" ))){
				vat.block.pageDataSave(vnB_Detail,{  
					funcSuccess:function(){
						vat.bean().vatBeanOther.beforeStatus = status;
					  	vat.bean().vatBeanOther.processId = processId;
					  	vat.bean().vatBeanOther.formAction = formAction;
					  	vat.bean().vatBeanOther.loginEmployeeCode = employeeCode;
					  	vat.bean().vatBeanOther.approvalResult = approvalResult;
					  	vat.bean().vatBeanOther.approvalComment = approvalComment;	
					  	vat.bean().vatBeanOther.assignmentId = assignmentId;
						if("SUBMIT_BG" == formAction){
					      	vat.block.submit(function(){return "process_object_name=imPriceAdjustmentAction"+
					                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
					    }else{
							vat.block.submit(function(){return "process_object_name=imPriceAdjustmentAction"+
					                    "&process_object_method_name=performTransaction";},{
					                    bind:true, link:true, other:true,
					                    funcSuccess:function(){
							        		vat.block.pageRefresh(vnB_Detail);
							        	}}
							);
				        } 			                    
		          	}
	          	}
	          );
	    }else{
	    	alert("您的表單已加入待辦事件，請從待辦事件選取後，再次送出!");
	    }
    } 
}

// 取得簽核結果
function getApprovalResult(){
	if(vat.item.getValueByName("#F.status") == "SIGNING"){
		return vat.item.getValueByName("#F.approvalResult").toString();
	}else{
		return "true";
	}
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {

	return true ;
}
function appendBeforeService() {
	
	return true;
}
function appendAfterService() {

}
function doBeforePicker(){
	vat.formD.pageDataSave(vnB_Detail);
}
function doPageDataSave(){
    vat.block.pageDataSave(vnB_Detail);
}

function doPageRefresh(){
    vat.block.pageRefresh(vnB_Detail);
}
/*
	匯出
*/
function doExport() {
	//save line
	afterSavePageProcess = "executeExport";
	vat.block.pageSearch(vnB_Detail);	
}

/*
	匯入
*/
function doImport() {
	//save line
	afterSavePageProcess = "executeImport";
	vat.block.pageSearch(vnB_Detail);	
}

// 明細匯入
function importFormData(){
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var exportBeanName = orderTypeCode == "PAP" ? "IM_PRICE_ADJUSTMENT_T2_PAP" : "IM_PRICE_ADJUSTMENT_T2_PAJ";
	var width = "600";
    var height = "400";
    
	//window.open(
		//"/erp/fileUpload:standard:2.page" +
	var returnData = "&importBeanName=" + exportBeanName +
		"&importFileType=XLS" +
        "&processObjectName=imPriceAdjustmentMainService" + 
        "&processObjectMethodName=executeImportT2PriceLists" +
        "&arguments=" + vat.item.getValueByName("#F.headId") + "{$}" +
        			   vat.item.getValueByName("#F.brandCode") + "{$}" +
        			   vat.item.getValueByName("#F.orderTypeCode") + "{$}" +
        			   vat.item.getValueByName("#F.priceType") +
        "&parameterTypes=LONG{$}STRING{$}STRING{$}STRING" +
        "&blockId="+ vnB_Detail;
        
		///"FileUpload",
		//'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
	return 	returnData;
}

// 匯出
function exportFormData(){
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var exportBeanName = orderTypeCode == "PAP" ? "IM_PRICE_ADJUSTMENT_T2_PAP" : "IM_PRICE_ADJUSTMENT_T2_PAJ";
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=" + exportBeanName +
              "&fileType=XLS" + 
              "&processObjectName=imPriceAdjustmentMainService" + 
              "&processObjectMethodName=findById" + 
              "&gridFieldName=imPriceLists" + 
              "&arguments=" + vat.item.getValueByName("#F.headId") + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '定價變價單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			suffix += "&orderTypeCode="+escape(orderTypeCode); 
			break;
		case "PAP":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			var priceType = vat.item.getValueByName("#F.priceType");
			var headId = vat.item.getValueByName("#F.headId");
			var exchangeRate = vat.item.getValueByName("#F.exchangeRate");
			var ratio = vat.item.getValueByName("#F.ratio");
			var itemCategory = vat.item.getValueByName("#F.itemCategory");
			var supplierCode = vat.item.getValueByName("#F.supplierCode");
			suffix += "&headId="+ headId + "&orderTypeCode=" + escape(orderTypeCode) + "&priceStatus="+escape("N")+ "&priceType="+escape(priceType)+ "&exchangeRate=" + exchangeRate + "&ratio=" + ratio + "&itemCategory=" + itemCategory + "&supplierCode=" + supplierCode;  
			break;	
		case "PAJ":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			var priceType = vat.item.getValueByName("#F.priceType");
			var headId = vat.item.getValueByName("#F.headId");
			var exchangeRate = vat.item.getValueByName("#F.exchangeRate");
			var ratio = vat.item.getValueByName("#F.ratio");
			var itemCategory = vat.item.getValueByName("#F.itemCategory");
			var supplierCode = vat.item.getValueByName("#F.supplierCode");
			suffix += "&headId="+ headId + "&orderTypeCode=" + escape(orderTypeCode) + "&priceStatus="+escape("Y")+ "&priceType="+escape(priceType)+ "&exchangeRate=" + exchangeRate + "&ratio=" + ratio+ "&itemCategory=" + itemCategory + "&supplierCode=" + supplierCode; 
			break;	
	}
	return suffix;
}

// 供應商picker 回來執行
function doAfterPickerFunctionProcess(){
	//do picker back something
	if(typeof vat.bean().vatBeanPicker.result != "undefined"){
    	vat.item.setValueByName("#F.addressBookId", vat.bean().vatBeanPicker.result[0].addressBookId); 
		changeSupplierName("addressBookId");
	}
}

// 定變價 line picker 回來執行
function doAfterPickerLineFunctionProcess(id){
	if( typeof vat.bean().vatBeanPicker.result != "undefined" ){
//		if( vat.bean().vatBeanPicker.result[0].priceId != "" ){
//			alert( "vat.bean().vatBeanPicker.result.length =" + vat.bean().vatBeanPicker.result.length);
//			alert("doAfterPickerLineFunctionProcess before");
			vat.block.pageSearch(vnB_Detail);
//			alert("doAfterPickerLineFunctionProcess after");
//			onChangeItemCode(vat.bean().vatBeanPicker.result[0].priceId, id);
//		}
	}
}

// 動態改變供應商名稱
function changeSupplierName( code ){
//	alert( code + "\n" + vat.item.getValueByName("#F.addressBookId") +"\n" + vat.item.getValueByName("#F.supplierCode") );

	vat.ajax.XHRequest({
		post:"process_object_name=buSupplierWithAddressViewService"+
                  "&process_object_method_name=getAJAXSupplierName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&addressBookId=" + ( "addressBookId" === code ? vat.item.getValueByName("#F.addressBookId") : "" )+
                  "&supplierCode=" + ( "supplierCode" === code ? vat.item.getValueByName("#F.supplierCode") : "" ),
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName("#F.supplierCode", vat.ajax.getValue("supplierCode", oXHR.responseText));
         		vat.item.setValueByName("#F.supplierName", vat.ajax.getValue("supplierName", oXHR.responseText) );
         		vat.item.setValueByName("#F.currencyCode", vat.ajax.getValue("currencyCode", oXHR.responseText) );
//         		changeExchangeRate();
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.supplierName", "查無此供應商");
		}   
	});	
}

// 動態改變採購人
function changeItemCategory(){
    if(vat.item.getValueByName("#F.itemCategory") !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=imPriceAdjustmentMainService"+
                    "&process_object_method_name=getAJAXPurchaseEmployee"+
                    "&categoryType=" + vat.item.getValueByName("#F.itemCategory")+
                    "&version30=Y",
           find: function changeShopCodeRequestSuccess(oXHR){ 
				var allPurchaseAssist = eval(vat.ajax.getValue("purchaseAssist", oXHR.responseText));
				var allPurchaseMember = eval(vat.ajax.getValue("purchaseMember", oXHR.responseText));
				var allPurchaseMaster = eval(vat.ajax.getValue("purchaseMaster", oXHR.responseText));
           		allPurchaseAssist[0][0] = "#F.purchaseAssist";
	       		allPurchaseMember[0][0] = "#F.purchaseMember";
          		allPurchaseMaster[0][0] = "#F.purchaseMaster";
				vat.item.SelectBind(allPurchaseAssist); 
				vat.item.SelectBind(allPurchaseMember);
				vat.item.SelectBind(allPurchaseMaster);
           }   
       });
    }
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(typeof vat.bean().vatBeanPicker.result != "undefined"){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
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

// 送出後更新
function createRefreshForm(){
	refreshForm("");
}

// 建立新資料按鈕
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;
    	refreshForm("","new");
	 }
}

// 刷新頁面
function refreshForm(vsHeadId,key){
	document.forms[0]["#processId"         ].value = "";       
	document.forms[0]["#formId"            ].value = vsHeadId;       	
	document.forms[0]["#assignmentId"      ].value = "";     
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"            ].value;	
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	//alert("currentRecordNumber:"+vat.bean().vatBeanOther.currentRecordNumber);
	vat.block.submit(
		function(){
			return "process_object_name=imPriceAdjustmentAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     		funcSuccess:function(){
 		   		vat.item.bindAll();
 		   		//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
 		             
/* 		        var allPurchaseAssist  = vat.bean("allPurchaseAssist");
				var allPurchaseMember  = vat.bean("allPurchaseMember");
				var allPurchaseMaster  = vat.bean("allPurchaseMaster"); 
				allPurchaseAssist[0][0] = "#F.purchaseAssist";
	       		allPurchaseMember[0][0] = "#F.purchaseMember";
          		allPurchaseMaster[0][0] = "#F.purchaseMaster";
				vat.item.SelectBind(allPurchaseAssist); 
				vat.item.SelectBind(allPurchaseMember);
				vat.item.SelectBind(allPurchaseMaster); */   
 		      	doFormAccessControl();
     	}});
	

}

// 依狀態鎖form
function doFormAccessControl(){
	var status 		= vat.item.getValueByName("#F.status");
	var processId	= vat.bean().vatBeanOther.processId;
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var formId = vat.bean().vatBeanOther.formId;
	var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	var canBeClaimed	= document.forms[0]["#canBeClaimed"].value;
	
	var userType = document.forms[0]["#userType"].value; 
	
	
	if(processId!="" || orderNoPrefix.indexOf("TMP") == -1){
 		vat.tabm.displayToggle(0, "xTab2", true, false, false);
 		refreshWfParameter( vat.item.getValueByName("#F.brandCode"), 
        					vat.item.getValueByName("#F.orderTypeCode"), 
        					vat.item.getValueByName("#F.orderNo"));
 	}else{
 		vat.tabm.displayToggle(0, "xTab2", false, false, false);
 	}     
 		        
	// 初始化
	//======================<header>=============================================
	vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true, true);
	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true);
	//======================<detail>=============================================
	
	vat.block.canGridModify([vnB_Detail], true,true,true);
	//=======================<buttonLine>========================================
//	if( "ITEM" == userType ){
		vat.item.setStyleByName("#B.new", 		"display", "inline");
		vat.item.setStyleByName("#B.search", 	"display", "inline");
		vat.item.setStyleByName("#B.submit", 	"display", "inline");
		vat.item.setStyleByName("#B.save", 		"display", "inline");
		vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
		vat.item.setStyleByName("#B.message", 	"display", "inline");
		vat.item.setStyleByName("#B.import", 	"display", "inline");
/*	}else{
		vat.item.setStyleByName("#B.new", 		"display", "none");
		vat.item.setStyleByName("#B.search", 	"display", "none");
		vat.item.setStyleByName("#B.submit", 	"display", "none");
		vat.item.setStyleByName("#B.save", 		"display", "none");
		vat.item.setStyleByName("#B.submitBG", 	"display", "none");
		vat.item.setStyleByName("#B.message", 	"display", "none");
		vat.item.setStyleByName("#B.import", 	"display", "none");
	}*/
	
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.print", 	"display", "inline");
	//===========================================================================
	
	if(orderNoPrefix == "TMP" ){
		vat.item.setStyleByName("#B.print", 	"display", "none");
	}
	// 流程內
	if( processId != null && processId != 0 ){ //從待辦事項進入
		vat.item.setStyleByName("#B.new", 		"display", "none");
		vat.item.setStyleByName("#B.search", 	"display", "none");
		vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
		vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
		if( status == "SAVE" || status == "REJECT" ){//商品部需求調整(wade)
			vat.item.setStyleByName("#B.void", 		"display", "inline"); 
		}
	}else{
		//
		if(orderNoPrefix == "TMP" ){
		
		}else{
		// 查詢回來
			vat.item.setStyleByName("#B.submit", 	"display", "none"); 
			vat.item.setStyleByName("#B.save", 		"display", "none"); 
			vat.item.setStyleByName("#B.submitBG", 	"display", "none"); 
			vat.item.setStyleByName("#B.message", 	"display", "none"); 
			vat.item.setStyleByName("#B.import", 	"display", "none"); 
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			vat.block.canGridModify([vnB_Detail], false,false,false);
		}	
	}
	
	// 各狀態
	if( status == "SIGNING" || status == "FINISH" || status == "VOID" ){
		//======================<header>=============================================
		vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
		//======================<detail>=============================================
		vat.block.canGridModify([vnB_Detail], false,false,false);
		//=======================<buttonLine>========================================
		if( status == "SIGNING" && ( processId != ""  ) ){ 
			vat.item.setStyleByName("#B.submit", 		"display", "inline");
			vat.item.setAttributeByName("#F.description", "readOnly", false);
		}else{
			vat.item.setStyleByName("#B.submit", 		"display", "none");
		}
		vat.item.setStyleByName("#B.save", 		"display", "none"); 
		vat.item.setStyleByName("#B.submitBG", 	"display", "none"); 
		vat.item.setStyleByName("#B.message", 	"display", "none"); 
		vat.item.setStyleByName("#B.import", 	"display", "none"); 
		
	}

	vat.block.pageRefresh(vnB_Detail);
	//這裡時做onChange
	if(vat.item.getValueByName("#F.itemCategory") !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=imPriceAdjustmentMainService"+
                    "&process_object_method_name=getAJAXPurchaseEmployee"+
                    "&categoryType=" + vat.item.getValueByName("#F.itemCategory")+
                    "&version30=Y",
           find: function changeShopCodeRequestSuccess(oXHR){ 
				var allPurchaseAssist = eval(vat.ajax.getValue("purchaseAssist", oXHR.responseText));
				var allPurchaseMember = eval(vat.ajax.getValue("purchaseMember", oXHR.responseText));
				var allPurchaseMaster = eval(vat.ajax.getValue("purchaseMaster", oXHR.responseText));
           		allPurchaseAssist[0][0] = "#F.purchaseAssist";
	       		allPurchaseMember[0][0] = "#F.purchaseMember";
          		allPurchaseMaster[0][0] = "#F.purchaseMaster";
				vat.item.SelectBind(allPurchaseAssist); 
				vat.item.SelectBind(allPurchaseMember);
				vat.item.SelectBind(allPurchaseMaster);
				vat.item.bindAll();
           }   
       });
    }
    
    //若商控群組裡某人按了鎖定工作則其他人無法送出，按下鎖定工作後canBeClaimed=true，反之都為canBeClaimed=false
    if(canBeClaimed == "true"){
		vat.item.setStyleByName("#B.submit",    "display", "none");
	}	
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_PRICE_ADJUSTMENT" + 
		"&levelType=ERROR" +
        "&processObjectName=imPriceAdjustmentMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imPriceAdjustmentAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
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

// 設定匯率
function changeExchangeRate(){
	var nLine = vat.item.getGridLine();
	var currencyCode = vat.item.getGridValueByName("currencyCode", nLine);
	var processString = "process_object_name=buBasicDataService&process_object_method_name=getAJAXExchangeRateByCurrencyCode&currencyCode=" + currencyCode + "&organizationCode=TM" ;
	vat.ajax.startRequest(processString,  function() { 
  		if (vat.ajax.handleState()){
    		vat.item.setGridValueByName("exchangeRate", nLine,vat.ajax.getValue("exchangeRate", vat.ajax.xmlHttp.responseText));
//    		calculateLineRate();
  		}
	});
}

// 計算 line 毛利率(舊)毛利率(新)
function calculateLineRate(key) {
	var nItemLine = vat.item.getGridLine();                                         
	var processString="";
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var brandCode = vat.item.getValueByName("#F.brandCode"); 
	if(orderTypeCode=="PAJ"){	//變價單
		var condition ="";
		var exchangeRate = "", currencyCode = "", foreignCost = "";
		if( key == "exchangeRate"){	// 表示手打新匯率
			exchangeRate = vat.item.getGridValueByName("exchangeRate",nItemLine);
		}else if( key == "currencyCode" ){	// 表示切換幣別
			currencyCode = vat.item.getGridValueByName("currencyCode",nItemLine);
		}else if( key == "foreignCost" ||  key == "unitPrice"  ){	// 表示手打新原幣成本 或  表示手打送簽價格
			exchangeRate = vat.item.getGridValueByName("exchangeRate",nItemLine);
			foreignCost = vat.item.getGridValueByName("foreignCost",nItemLine);
		}
		condition = "&exchangeRate=" + exchangeRate + "&currencyCode=" + currencyCode + "&foreignCost=" + foreignCost;
		
		var foreignCost = vat.item.getGridValueByName("foreignCost",nItemLine); 
		var unitPrice = vat.item.getGridValueByName("unitPrice",nItemLine); 
		processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXGrossRateNew";
		processString += "&foreignCost="+foreignCost+"&unitPrice="+unitPrice+"&brandCode=" + brandCode + condition;
  
	} else if(orderTypeCode =="PAP"){ //定價單
		var condition="";
		var exchangeRate = "", currencyCode = "", localCost = ""; 
		if( key == "exchangeRate"){	// 表示手打新匯率
			exchangeRate = vat.item.getGridValueByName("exchangeRate",nItemLine);
		}else if( key == "currencyCode" ){	// 表示切換幣別
			currencyCode = vat.item.getGridValueByName("currencyCode",nItemLine);
		}else if( key == "localCost" ||  key == "unitPrice"  ){	// 表示手打新台幣成本 或  表示手打送簽價格
			exchangeRate = vat.item.getGridValueByName("exchangeRate",nItemLine);
			localCost = vat.item.getGridValueByName("localCost",nItemLine);
		}
		
		condition = "&exchangeRate=" + exchangeRate + "&currencyCode=" + currencyCode + "&localCost=" + localCost;
		var foreignCost = vat.item.getGridValueByName("foreignCost",nItemLine);  
		var unitPrice = vat.item.getGridValueByName("unitPrice",nItemLine);
		processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXCalcGrossRate&foreignCost="+foreignCost;
		processString +="&unitPrice="+unitPrice+"&brandCode=" + brandCode + condition;    
	}
	 vat.ajax.startRequest(processString,  function() { 
	    if (vat.ajax.handleState()){
	
	      if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
	      		if(orderTypeCode == "PAP"){
	      			vat.item.setGridValueByName("grossRate",nItemLine,vat.ajax.getValue("grossRate", vat.ajax.xmlHttp.responseText));
	      			vat.item.setGridValueByName("exchangeRate",nItemLine,vat.ajax.getValue("exchangeRate", vat.ajax.xmlHttp.responseText));
	      			vat.item.setGridValueByName("localCost",nItemLine,vat.ajax.getValue("localCost", vat.ajax.xmlHttp.responseText));	
	      		}else if(orderTypeCode == "PAJ"){
	      			vat.item.setGridValueByName("grossRate",nItemLine,vat.ajax.getValue("grossRate", vat.ajax.xmlHttp.responseText));
	      			vat.item.setGridValueByName("exchangeRate",nItemLine,vat.ajax.getValue("exchangeRate", vat.ajax.xmlHttp.responseText))
	      		}
	      }else{
	      		if(orderTypeCode == "PAP"){
	      			vat.item.setGridValueByName("costRate",nItemLine,"0");
	      			vat.item.setGridValueByName("exchangeRate",nItemLine,"1");
	      			vat.item.setGridValueByName("localCost",nItemLine,"0");
	      		}else if(orderTypeCode == "PAJ"){
	      			vat.item.setGridValueByName("grossRate",nItemLine,"0");
	      			vat.item.setGridValueByName("exchangeRate",nItemLine,"1");
	      		}
	      }
	  }
	} );
}

// 票據列印　含成本
function openReportWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=imPriceAdjustmentMainService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
			                    	//alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);	

	if("AFTER_SUBMIT"==type) createRefreshForm();
}

// 抓明細匯入的第一個廠商代號
function changeSupplierCode(){
	var vItemCode = vat.item.getGridValueByName("itemCode", 1);
	var brandCode = document.forms[0]["#loginBrandCode" ].value
	if(vItemCode != "" && brandCode.indexOf("T2") > -1 ){
		vat.ajax.XHRequest({ 
			post:"process_object_name=imPriceAdjustmentMainService&process_object_method_name=getSupplierCode" + 
	                "&brandCode=" + brandCode+
	                "&itemCode=" + vItemCode,
            asyn:false,                      
			find: function change(oXHR){
				if(vat.ajax.getValue("supplierCode", oXHR.responseText) != ""){
					vat.item.setValueByName("#F.supplierCode"	,vat.ajax.getValue("supplierCode", oXHR.responseText));
					changeSupplierName("supplierCode");
				}
           	}
		});
	}
}
