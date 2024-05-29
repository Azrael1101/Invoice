/***
 *	檔案: buGoal.js
 *	說明：目標設定/人員設定
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_PriceDetailDiv = 2;				// 定變價
var vnB_ReceiveDetailDiv = 3; 			// 進貨
var vnB_MovementDetailDiv = 4; 			// 調撥單
var vnB_ItemDetailDiv = 5; 				// 商品主檔(暫存)
var vnB_Receive02DetailDiv = 6; 		// 進貨外標
var vnB_Movement03DetailDiv = 7; 		// 調撥食品菸
var vnB_Movement06DetailDiv = 8; 		// 調撥化妝品.精品
var vnB_Price2DetailDiv = 9; 			// 定變價(含報單日期)
var vnB_Item2DetailDiv = 10; 			// 商品主檔(建檔)
var vnB_Movement07DetailDiv = 11; 		// 調撥單轉補條碼
var vnB_AdjustReceiveDetailDiv = 12; 	// 進貨調整單(T2A)
var vnB_AdjustReceive02DetailDiv = 13; 	// 進貨調整單外標(T2A)
var vnB_AdjustReceive03DetailDiv = 14; 	// 進貨調整單效期外標(T2A) by Weichun 2011.01.10
var vnB_Receive03DetailDiv = 15; 		// 進貨外標明細檔(Excel匯入) by Weichun 2011.03.02

var activeTab = 1;
var isSearch = false; // 判斷是否可查詢參數

function outlineBlock(){
 
 	formInitial();
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","變價明細檔" ,"vatPriceDetailDiv"        ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none", "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab2","進貨明細檔" ,"vatReceiveDetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab3","調撥明細檔" ,"vatMovementDetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab4","主檔明細檔" ,"vatItemDetailDiv"      	,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab5","進貨外標明細檔" ,"vatReceive02DetailDiv"	 ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab6","調撥食品菸明細檔" ,"vatMovement03DetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab7","調撥化妝品精品明細檔" ,"vatMovement06DetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab8","變價明細檔(含報單日期)" ,"vatPrice2DetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab9","主檔明細檔(建檔)" ,"vatItem2DetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab10","調撥單轉補條碼" ,"vatMovement07DetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab11","進貨調整單(T2A)" ,"vatAdjustReceiveDetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab12","進貨調整單外標(T2A)" ,"vatAdjustReceive02DetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()");
		vat.tabm.createButton(0 ,"xTab13","進貨調整單效期外標(T2A)" ,"vatAdjustReceive03DetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()"); // 新增外標格式 by Weichun 2011.01.10
		vat.tabm.createButton(0 ,"xTab14","進貨外標明細檔(Excel匯入)" ,"vatReceive03DetailDiv"	 ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none",  "doPageDataSave()"); // 進貨外標明細檔(Excel匯入) by Weichun 2011.03.02
	}

	priceDetailInitial(); // 變價明細檔
	receiveDetailInitial(); // 進貨明細檔
	movementDetailInitial(); // 調撥明細檔
	itemDetailInitial(); // 主檔明細檔(暫存)
	receive02DetailInitial(); // 進貨外標明細檔
	movement03DetailInitial(); // 調撥食品菸明細檔
	movement06DetailInitial(); // 調撥化妝品精品明細檔
	price2DetailInitial(); // 變價明細檔(含報單日期)
	item2DetailInitial(); // 主檔明細檔(建檔)
	movement07DetailInitial(); // 調撥單轉補條碼
	adjustReceiveDetailInitial(); // 進貨調整單(T2A)
	adjustReceive02DetailInitial(); // 進貨調整單-外標(T2A)
	adjustReceive03DetailInitial(); // 進貨調整單-效期外標(T2A)
	receive03DetailInitial(); // 進貨外標(Excel匯入)

	doFormAccessControl();

}

// 初始化
function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        {
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(
	  		function(){
				return "process_object_name=gerenateBarCodeAction&process_object_method_name=performInitial";
	    	},{
	    		other: true
    	});
  	}
}

function buttonLine(){
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      		, type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          		, type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       		, type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          		, type:"LABEL"    , value:"　"},
	 			{name:"#B.exit"        		, type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          		, type:"LABEL"  ,value:"　"},
	 			{name:"#B.match"       		, type:"IMG"    ,value:"凍結",   src:"./images/button_lock_data.gif", eClick:'doMatchLine()'},
	 			{name:"matchSpace"	   		, type:"LABEL"  ,value:"　"},
	 			{name:"#B.unMatch"       	, type:"IMG"    ,value:"取消凍結",   src:"./images/button_unlock_data.gif", eClick:'doFormAccessControl("unMatch")'},
	 			{name:"matchSpace"	   		, type:"LABEL"  ,value:"　"},
	 			{name:"#B.export"			, type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"matchSpace"	   		, type:"LABEL"  ,value:"　"},
	 			{name:"#B.import" 	   		, type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'doImport()'},
	 			{name:"matchSpace"	   		, type:"LABEL"  ,value:"　"},
	 			{name:"#B.importExcel" 		, type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'doImportExcel()'},
	 			{name:"matchSpace"	   		, type:"LABEL"  ,value:"　"},
	 			{name:"#B.barCodeExport"	, type:"IMG"    ,value:"條碼匯出",   src:"./images/button_barcode_print.gif", eClick:'doBarcodeExport()'},
	 			{name:"matchSpace"	   		, type:"LABEL"  ,value:"　"},
	 			{name:"#B.print"       		, type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:"openReportWindow()"}

	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:"",
		closeService:""
	});
}

// 目標主檔
function headerInitial(){
	var allBarCodeTypes =  vat.bean("allBarCodeTypes");
//				[["","","true"],
//                 ["進貨單"				,"進貨單-皮件.酒外標"	,"調撥單-轉出貨裝箱單麥頭"	,"調撥單-食品、菸帶效期商品外標"	,"調撥單-化妝品.精品轉出口麥頭"	,"定價單"					,"變價單" ],
//                 ["01_ImReceiveHead"	,"02_ImReceiveHead"	,"05_ImMovementHead"	,"03_ImMovementHead"		,"06_ImMovementHead"		,"02_ImPriceAdjustment"	,"01_ImPriceAdjustment"]];

	var allCustomsWarehouseCode = vat.bean("allCustomsWarehouseCode");
	var allWarehouseCode = vat.bean("allWarehouseCode");
    var allCategory01 = vat.bean("allCategory01");
    var allOrderBys = [
        ["", "", "true"],
        ["品號小至大排序","品號大至小排序","項次小至大排序","項次大至小排序"],
        ["L.ITEM_CODE ASC","L.ITEM_CODE DESC","L.INDEX_NO ASC","L.INDEX_NO DESC"]
    ];
    
    var all = [
        ["", "", "true"],
        ["有", "沒有"],
        ["Y", "N"]
    ];
    var stockSelect = [
        ["", "N", true],
        ["庫存不為零", "庫存可為零"],
        ["N", "Y"]
    ];
    var samePriceSelect = [
        ["", "N", true],
        ["原價與變價可一樣", "原價與變價不一樣"],
        ["Y", "N"]
    ];
    var resultSelect = [
        ["", "F", true],
        ["F", "P"],
        ["F", "P"]
    ];
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"條碼產生作業",
		rows:[

			{row_style:"", cols:[
				{items:[{name:"#L.barCodeType", 	type:"LABEL", 	value:"類型<font color='red'>*</font>"}]},
				{items:[{name:"#F.barCodeType", 	type:"SELECT", 	bind:"barCodeType", eChange:"changeOrderTypeCode()", init:allBarCodeTypes, size:1, back:false}]},

				{items:[{name:"#L.orderTypeCode", 	type:"LABEL", 	value:"單別<font color='red'>*</font>"}]},
				{items:[{name:"#F.orderTypeCode", 	type:"SELECT", 	bind:"orderTypeCode", size:1, back:false}]},

				{items:[{name:"#L.orderNo", 		type:"LABEL", 	value:"單號<font color='red'>*</font>"}]},
				{items:[{name:"#F.orderNo", 		type:"TEXT", 	bind:"orderNo" , size:20, back:false},
				 		{name:"#L.orderNoEnd", 		type:"LABEL", 	value:" "},
						{name:"#F.orderNoEnd", 		type:"TEXT", 	bind:"orderNoEnd" , size:20, back:false}]},
				{items:[{name:"#L.brandName", 	type:"LABEL", 	value:"品牌"}]},
				{items:[{name:"#F.brandName", 	type:"TEXT", 	bind:"brandName", mode:"READONLY", back:false},
						{name:"#F.brandCode", 	type:"TEXT", 	bind:"brandCode", mode:"HIDDEN", back:false},
						{name:"#F.headId", 		type:"TEXT", 	bind:"headId", mode:"HIDDEN", back:false},
						{name:"#F.headIds", 	type:"TEXT", 	bind:"headIds", mode:"HIDDEN", back:false},
						{name:"#F.papers", 		type:"TEXT", 	bind:"papers", mode:"HIDDEN", back:false}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.category01", 	type:"LABEL", 	value:"大類"}]},
				{items:[{name:"#F.category01", 	type:"SELECT", 	bind:"category01", init:allCategory01, mode:"READONLY" , size:10, back:false}]},
				{items:[{name:"#L.category", 	type:"LABEL", 	value:"中類<font color='red'>*</font>"}]},
				{items:[{name:"#F.category", 	type:"SELECT", 	bind:"category", init:all, mode:"READONLY" , size:10, back:false} ,
						{name:"#F.category02",	type:"TEXT", 	bind:"category02", eChange: function(){ changeCategoryCodeName("CATEGORY02"); }, back:false},
						{name:"#B.category02",	value:"選取" ,type:"PICKER" ,
			 									openMode:"open", src:"./images/start_node_16.gif",
			 									service:"Im_Item:searchCategory:20100119.page",
			 									left:0, right:0, width:1024, height:768,
			 									servicePassData:function(){ return doPassData("CATEGORY02"); },
			 									serviceAfterPick:function(){ doAfterPickerFunctionProcess("CATEGORY02");} },
			 			{name:"#F.category02Name",	type:"TEXT", 	bind:"category02Name", back:false, mode:"READONLY"}]},
	 			{items:[{name:"#L.price", 		type:"LABEL", 	value:"價格<font color='red'>*</font>"}]},
				{items:[{name:"#F.price", 		type:"SELECT", 	bind:"price", init:all, mode:"READONLY" , size:10, back:false},
						{name:"#F.samePrice", 	type:"SELECT", 	bind:"samePrice", init:samePriceSelect, back:false }],td:" colSpan=3"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseCode", 	type:"LABEL", 	value:"庫別"}]},
				{items:[//{name:"#F.warehouseCode", 	type:"TEXT", 	bind:"warehouseCode", eChange:"changeLineWarehouseCode('-1')", size:10, back:false},
						/*
						{name:"#B.warehouseCode",	value:"選取" ,type:"PICKER" , size:15 , src:"./images/start_node_16.gif",
	 									 		openMode:"open",
	 									 		service:"Im_Warehouse:search:20091112.page",
	 									 		left:0, right:0, width:1024, height:768,
	 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("#F.warehouseCode");}},
	 					*/
	 					//{name:"#F.warehouseName", 		type:"TEXT",  	bind:"warehouseName", back:false, mode:"READONLY"},
	 					//{name:"#L.showZeroN", 	type:"LABEL"   , value:"<br>"},
	 					{name:"#F.customsWarehouseCode"     , type:"SELECT", init:allCustomsWarehouseCode, eChange:"changeStorage()"},
	 					{name:"#F.warehouseCode"    , type:"SELECT" /*, init:allWarehouseCode */ },
	 					{name:"#F.showZero", 	type:"SELECT" , init:stockSelect}]},
				{items:[{name:"#L.taxType", 	type:"LABEL", 	value:"稅別<font color='red'>*</font>"}]},
				{items:[{name:"#F.taxType", 	type:"RADIO", 	bind:"taxType",init:resultSelect, mode:"READONLY" , size:10, back:false}]},
				{items:[{name:"#L.orderBy", 	type:"LABEL", 	value:"排序<font color='red'>*</font>"}]},
				{items:[{name:"#F.orderBy", 	type:"SELECT", 	bind:"orderBy", init:allOrderBys, mode:"READONLY" , size:10, back:false}],td:" colSpan=3"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.description",	type:"LABEL", 	value:"說明<font color='red'>*</font>"}]},
				{items:[{name:"#F.description", type:"SELECT", 	bind:"description", init:all, mode:"READONLY" , size:10, back:false}]},
				{items:[{name:"#L.startDate", 	type:"LABEL", 	value:"日期"}]},
				{items:[{name:"#F.startDate", 	type:"DATE", 	bind:"startDate", back:false},
						{name:"#L.betweenDate", type:"LABEL", 	value:"至"},
						{name:"#F.endDate", 	type:"DATE", 	bind:"endDate", back:false}]},
				{items:[{name:"#L.supplierCode", 	type:"LABEL", 	value:"廠商代號"}]},
				{items:[{name:"#F.supplierCode", 	type:"TEXT", 	bind:"supplierCode", size:20, back:false}],td:" colSpan=3"}
			]}//,
			//{row_style:"", cols:[
			//	{items:[{name:"#L.priceAdjustCode",	type:"LABEL", 	value:"變價單<font color='red'>*</font>"}]},
			//	{items:[{name:"#F.priceAdjustCode", type:"SELECT", 	bind:"priceAdjustCode", init:all, mode:"READONLY" , size:10, back:false}]},
			//	{items:[{name:"#L.startDate", 	type:"LABEL", 	value:"變價單有效日期"},
			//			{name:"#F.startDate", 	type:"DATE", 	bind:"startDate", back:false},
			//			{name:"#L.betweenDate", type:"LABEL", 	value:"至"},
			//			{name:"#F.endDate", 	type:"DATE", 	bind:"endDate", back:false}],td:" colSpan=3"}
			//]}
		],

		beginService:"",
		closeService:""
	});
}

// 變價明細檔
function priceDetailInitial(){
	vat.item.make(vnB_PriceDetailDiv, "indexNo", {type:"IDX", view:"fixed", desc:"序號"});
	vat.item.make(vnB_PriceDetailDiv, "orderNo", {type:"TEXT", view:"fixed", size:16, desc:"單號"}); // 單別單號
	vat.item.make(vnB_PriceDetailDiv, "enableDate", {type:"TEXT", view:"", size:12, desc:"生效日期"}); // 生效日期
	vat.item.make(vnB_PriceDetailDiv, "warehouseCode", {type:"TEXT", view:"", size:8, desc:"庫別 "});  // 庫別
	vat.item.make(vnB_PriceDetailDiv, "itemCode", {type:"TEXT", view:"", size:16, desc:"品號"});
	vat.item.make(vnB_PriceDetailDiv, "category02", {type:"TEXT", view:"", size:8, desc:"中類"});
	vat.item.make(vnB_PriceDetailDiv, "category02Name", {type:"TEXT", view:"", size:8, desc:"中類名稱"});
	vat.item.make(vnB_PriceDetailDiv, "itemBrand", {type:"TEXT", view:"", size:8, desc:"品牌"});
	vat.item.make(vnB_PriceDetailDiv, "category13", {type:"TEXT", view:"", size:16, desc:"系列"});
	vat.item.make(vnB_PriceDetailDiv, "originalPrice", {type:"NUMB", view:"", size:10, desc:"原價"});// 原價
	vat.item.make(vnB_PriceDetailDiv, "PAunitPrice", {type:"NUMB", view:"", size:10, desc:"售價"});
	vat.item.make(vnB_PriceDetailDiv, "currentOnHandQty", {type:"NUMB", view:"shift", size:6, desc:"庫存數量"});
	vat.item.make(vnB_PriceDetailDiv, "PAitemCName", {type:"TEXT", view:"shift", size:20,alter:true, desc:"品名"});
	vat.item.make(vnB_PriceDetailDiv, "status", {type:"TEXT", view:"shift", size:8, desc:"狀態"}); // 狀態
	vat.item.make(vnB_PriceDetailDiv, "description2", {type:"TEXT", view:"shift", size:16, desc:"說明"});

	vat.block.pageLayout(vnB_PriceDetailDiv, {	id: "vatPriceDetailDiv",
								pageSize: 10,
								selectionType : "NONE",
								indexType	: "AUTO",
	                            canGridDelete:false,
								canGridAppend:false,
								canGridModify:false,
								appendBeforeService : "appendBeforeService()",
								appendAfterService  : "appendAfterService()",
								loadBeforeAjxService: "loadBeforeAjxService("+vnB_PriceDetailDiv+")",
								loadSuccessAfter    : "loadSuccessAfter("+vnB_PriceDetailDiv+")",
								eventService        : "eventService()",
								saveBeforeAjxService: "saveBeforeAjxService("+vnB_PriceDetailDiv+")",
								saveSuccessAfter    : "saveSuccessAfter("+vnB_PriceDetailDiv+")"
														});
}

// 進貨單明細
function receiveDetailInitial(){
	vat.item.make(vnB_ReceiveDetailDiv, "indexNo",  {type:"IDX", desc: "序號"});
	vat.item.make(vnB_ReceiveDetailDiv, "itemCode", {type:"TEXT", size: 16, desc:"品號"});
	vat.item.make(vnB_ReceiveDetailDiv, "category02", {type:"TEXT", size: 10, desc:"中類"});
	vat.item.make(vnB_ReceiveDetailDiv, "itemBrand", {type:"TEXT", size: 10, desc:"商品品牌"});
	vat.item.make(vnB_ReceiveDetailDiv, "category13", {type:"TEXT", size: 10, desc:"商品系列"});
	vat.item.make(vnB_ReceiveDetailDiv, "unitPrice", {type:"NUMB", size: 16, desc:"售價"});
	vat.item.make(vnB_ReceiveDetailDiv, "quantity", {type:"NUMB", size: 16, desc:"數量"});
	vat.item.make(vnB_ReceiveDetailDiv, "itemCName", {type:"TEXT", size: 16,alter:true, desc:"品名"});
	vat.item.make(vnB_ReceiveDetailDiv, "declartionDate", {type:"TEXT", size: 16, desc:"進貨日期"});
	vat.item.make(vnB_ReceiveDetailDiv, "description", {type:"TEXT", size: 16, desc:"說明"});
	vat.block.pageLayout(vnB_ReceiveDetailDiv, {
														id: "vatReceiveDetailDiv",
														pageSize: 10,
														selectionType : "NONE",
														indexType	: "AUTO",
								            			canGridDelete : false,
														canGridAppend : false,
														canGridModify : false,
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_ReceiveDetailDiv+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_ReceiveDetailDiv+")",
														eventService        : "eventService()",
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_ReceiveDetailDiv+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_ReceiveDetailDiv+")"
														});
}

// 進貨外標明細
function receive02DetailInitial(){
	vat.item.make(vnB_Receive02DetailDiv, "indexNo",  {type:"IDX", desc: "序號"});
	vat.item.make(vnB_Receive02DetailDiv, "itemCode", {type:"TEXT", size: 16, desc:"品號"});
	vat.item.make(vnB_Receive02DetailDiv, "itemCName", {type:"TEXT", size: 10,alter:true, desc:"品名"});
	vat.item.make(vnB_Receive02DetailDiv, "category02", {type:"TEXT", size: 10, desc:"中類"});
	vat.item.make(vnB_Receive02DetailDiv, "category02Name", {type:"TEXT", size: 10, desc:"中類名稱"});
	vat.item.make(vnB_Receive02DetailDiv, "boxCapacity", {type:"NUMB", size: 16, desc:"每箱數量"});
	vat.item.make(vnB_Receive02DetailDiv, "quantity", {type:"NUMB", size: 16, desc:"數量"});
	vat.item.make(vnB_Receive02DetailDiv, "supplierItemCode", {type:"TEXT", size: 16, desc:"廠商貨號"});
	vat.item.make(vnB_Receive02DetailDiv, "declartionDate", {type:"TEXT", size: 16, desc:"進貨日期"});
	vat.item.make(vnB_Receive02DetailDiv, "description", {type:"TEXT", size: 16, desc:"說明"});
	vat.block.pageLayout(vnB_Receive02DetailDiv, {
														id: "vatReceive02DetailDiv",
														pageSize: 10,
														selectionType : "NONE",
														indexType	: "AUTO",
								            			canGridDelete : false,
														canGridAppend : false,
														canGridModify : false,
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Receive02DetailDiv+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_Receive02DetailDiv+")",
														eventService        : "eventService()",
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Receive02DetailDiv+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_Receive02DetailDiv+")"
														});
}

// 調撥明細
function movementDetailInitial(){
	vat.item.make(vnB_MovementDetailDiv, "indexNo"                   , {type:"IDX",        		desc:"序號"		});
	vat.item.make(vnB_MovementDetailDiv, "arrivalWarehouseCode"      , {type:"TEXT", size: 5,	desc:"轉入庫"		});
	vat.item.make(vnB_MovementDetailDiv, "deliveryWarehouseCode"     , {type:"TEXT", size: 5,	desc:"轉出庫"		});
	vat.item.make(vnB_MovementDetailDiv, "originalOrderNo"     		 , {type:"TEXT", size: 20,	desc:"單號"	});
	vat.item.make(vnB_MovementDetailDiv, "boxNo"     		 		 , {type:"TEXT", size: 3,	desc:"箱號"		});
	vat.item.make(vnB_MovementDetailDiv, "taxType"     		 		 , {type:"TEXT", size: 3,	desc:"稅別"		});
	vat.item.make(vnB_MovementDetailDiv, "deliveryQuantity"     	 , {type:"TEXT", size: 5,	desc:"轉出量"		});
	vat.item.make(vnB_MovementDetailDiv, "boxCount"     		 	 , {type:"NUMB", size: 5,	desc:"總箱數"		});
	vat.item.make(vnB_MovementDetailDiv, "deliveryDate"     		 , {type:"TEXT", size: 10,	desc:"出貨日期"	});

	vat.block.pageLayout(vnB_MovementDetailDiv, {
														id: "vatMovementDetailDiv",
														pageSize: 10,
														selectionType : "NONE",
														indexType	: "AUTO",
								                        canGridDelete : false,
														canGridAppend : false,
														canGridModify : false,
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_MovementDetailDiv+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_MovementDetailDiv+")",
														eventService        : "eventService()",
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_MovementDetailDiv+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_MovementDetailDiv+")"
	});
}

// 調撥食品菸明細
function movement03DetailInitial(){
	vat.item.make(vnB_Movement03DetailDiv, "indexNo"                 , {type:"IDX",        		desc:"序號"		});
	vat.item.make(vnB_Movement03DetailDiv, "itemCode"      			 , {type:"TEXT", size: 20,	desc:"品號"		});
	vat.item.make(vnB_Movement03DetailDiv, "itemCName"     			 , {type:"TEXT", size: 20,alter:true,	desc:"品名"		});
	vat.item.make(vnB_Movement03DetailDiv, "boxCapacity"     		 , {type:"NUMB", size: 5,	desc:"裝箱量"	});
	vat.item.make(vnB_Movement03DetailDiv, "indexNo"     		 	 , {type:"NUMB", size: 3,	desc:"項次"		});
	vat.item.make(vnB_Movement03DetailDiv, "deliveryDate"     		 , {type:"TEXT", size: 10,	desc:"進貨日期"		});
	vat.item.make(vnB_Movement03DetailDiv, "lotNo"     	 			 , {type:"TEXT", size: 15,	desc:"有效期限"		});
	vat.item.make(vnB_Movement03DetailDiv, "description"     		 , {type:"TEXT", size: 16,	desc:"說明"	});

	vat.block.pageLayout(vnB_Movement03DetailDiv, {
														id: "vatMovement03DetailDiv",
														pageSize: 10,
														selectionType : "NONE",
														indexType	: "AUTO",
								                        canGridDelete : false,
														canGridAppend : false,
														canGridModify : false,
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Movement03DetailDiv+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_Movement03DetailDiv+")",
														eventService        : "eventService()",
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Movement03DetailDiv+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_Movement03DetailDiv+")"
	});
}

// 調撥化妝品精品明細
function movement06DetailInitial(){
	vat.item.make(vnB_Movement06DetailDiv, "indexNo"                , {type:"IDX",        		desc:"序號"		});
	vat.item.make(vnB_Movement06DetailDiv, "originalOrderNo"      	, {type:"TEXT", size: 20,	desc:"調撥單號"		});
	vat.item.make(vnB_Movement06DetailDiv, "indexNo"     			, {type:"NUMB", size: 10,	desc:"項次"		});
	vat.item.make(vnB_Movement06DetailDiv, "weight"     		 	, {type:"NUMB", size: 10,	desc:"重量"	});
	vat.item.make(vnB_Movement06DetailDiv, "deliveryQuantity"     	, {type:"NUMB", size: 3,	desc:"數量"		});
	vat.item.make(vnB_Movement06DetailDiv, "boxNo"     		 		, {type:"NUMB", size: 10,	desc:"箱號"		});
	vat.item.make(vnB_Movement06DetailDiv, "boxCount"     	 		, {type:"NUMB", size: 10,	desc:"總箱數"		});
	vat.item.make(vnB_Movement06DetailDiv, "description"     		, {type:"TEXT", size: 16,	desc:"說明"	});
	

	vat.block.pageLayout(vnB_Movement06DetailDiv, {
														id: "vatMovement06DetailDiv",
														pageSize: 10,
														selectionType : "NONE",
														indexType	: "AUTO",
								                        canGridDelete : false,
														canGridAppend : false,
														canGridModify : false,
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Movement06DetailDiv+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_Movement06DetailDiv+")",
														eventService        : "eventService()",
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Movement06DetailDiv+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_Movement06DetailDiv+")"
	});
}


// 商品明細(暫存)
function itemDetailInitial(){
 	vat.item.make(vnB_ItemDetailDiv, "checkbox" , {type:"XBOX"});
	vat.item.make(vnB_ItemDetailDiv, "imIndexNo"                 , {type:"TEXT", 						desc:"序號", mode:"READONLY"		});
	vat.item.make(vnB_ItemDetailDiv, "imItemCode"      			 , {type:"TEXT", size: 13, maxLen:13,	desc:"品號", eChange:"changeItemCode()"		});
	vat.item.make(vnB_ItemDetailDiv, "imUnitPrice"     			 , {type:"TEXT",						desc:"售價", mode:"READONLY"		});
	vat.item.make(vnB_ItemDetailDiv, "paper"     		 		 , {type:"NUMB", size: 13,				desc:"張數"		});
	vat.item.make(vnB_ItemDetailDiv, "imItemCName"     		 	 , {type:"TEXT", alter:true,			desc:"品名", mode:"READONLY"		});
	vat.item.make(vnB_ItemDetailDiv, "beginDate"     		 	 , {type:"TEXT", size: 7,				desc:"進貨日期"});
	vat.item.make(vnB_ItemDetailDiv, "imItemDesc"     		 	 , {type:"TEXT", 						desc:"說明", mode:"READONLY"		});

	vat.block.pageLayout(vnB_ItemDetailDiv, {
		id: "vatItemDetailDiv",
		pageSize: 10,
		selectionType 		: "CHECK",
		//indexType			: "AUTO",
		searchKey			: ["imIndexNo", "imItemCode", "imUnitPrice", "paper", "imItemCName", "beginDate", "imItemDesc"],
		canGridDelete 		: true,
		canGridAppend 		: true,
		canGridModify 		: true,
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_ItemDetailDiv+")",
		loadSuccessAfter    : "loadSuccessAfter("+vnB_ItemDetailDiv+")",
		eventService        : "eventService()",
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_ItemDetailDiv+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_ItemDetailDiv+")"
	});
}

// 變價明細檔(含報單日期)
function price2DetailInitial(){
	vat.item.make(vnB_Price2DetailDiv, "indexNo", {type:"IDX", view:"fixed", desc:"序號"});
	vat.item.make(vnB_Price2DetailDiv, "orderNoDecl", {type:"TEXT", view:"fixed", size:16, desc:"單號"}); // 單別單號
	vat.item.make(vnB_Price2DetailDiv, "enableDateDecl", {type:"TEXT", view:"", size:16, desc:"生效日期"}); // 生效日期
	vat.item.make(vnB_Price2DetailDiv, "customerWarehouseCode", {type:"TEXT", view:"", size:16, desc:"關別 "});  // 關別
	vat.item.make(vnB_Price2DetailDiv, "warehouseCodeDecl", {type:"TEXT", view:"", size:16, desc:"庫別 "});  // 庫別
	vat.item.make(vnB_Price2DetailDiv, "itemCodeDecl", {type:"TEXT", view:"", size:16, desc:"品號"});
	vat.item.make(vnB_Price2DetailDiv, "category02Decl", {type:"TEXT", view:"", size:16, desc:"中類"});
	vat.item.make(vnB_Price2DetailDiv, "itemBrandDecl", {type:"TEXT", view:"", size:16, desc:"商品品牌"});
	vat.item.make(vnB_Price2DetailDiv, "category13Decl", {type:"TEXT", view:"shift", size:16, desc:"系列"});
	vat.item.make(vnB_Price2DetailDiv, "originalPriceDecl", {type:"NUMB", view:"shift", size:16, desc:"原價"});// 原價
	vat.item.make(vnB_Price2DetailDiv, "PAunitPriceDecl", {type:"NUMB", view:"shift", size:16, desc:"售價"});
	vat.item.make(vnB_Price2DetailDiv, "imCurrentOnHandQtyDecl", {type:"NUMB", view:"shift", size:16, desc:"實體庫存數量"});
	vat.item.make(vnB_Price2DetailDiv, "cmCurrentOnHandQtyDecl", {type:"NUMB", view:"shift", size:16, desc:"報單庫存數量"});
	vat.item.make(vnB_Price2DetailDiv, "PAitemCNameDecl", {type:"TEXT", view:"shift", size:16,alter:true, desc:"品名"});
	vat.item.make(vnB_Price2DetailDiv, "declarationNo", {type:"TEXT", view:"shift", size:16, desc:"報單號碼"});
	vat.item.make(vnB_Price2DetailDiv, "declarationSeq", {type:"TEXT", view:"shift", size:16, desc:"報單項次"});
	vat.item.make(vnB_Price2DetailDiv, "declDate", {type:"TEXT", view:"shift", size:16, desc:"報單日期"});
	vat.item.make(vnB_Price2DetailDiv, "statusDecl", {type:"TEXT", view:"shift", size:16, desc:"狀態"}); // 狀態
	vat.item.make(vnB_Price2DetailDiv, "itemNo", {type:"TEXT", view:"shift", size:16, desc:"項次"});
	vat.item.make(vnB_Price2DetailDiv, "description1", {type:"TEXT", view:"shift", size:16, desc:"說明"});
	
	vat.block.pageLayout(vnB_Price2DetailDiv, {	id: "vatPrice2DetailDiv",
								pageSize: 10,
								selectionType : "NONE",
								indexType	: "AUTO",
	                            canGridDelete:false,
								canGridAppend:false,
								canGridModify:false,
								appendBeforeService : "appendBeforeService()",
								appendAfterService  : "appendAfterService()",
								loadBeforeAjxService: "loadBeforeAjxService("+vnB_Price2DetailDiv+")",
								loadSuccessAfter    : "loadSuccessAfter("+vnB_Price2DetailDiv+")",
								eventService        : "eventService()",
								saveBeforeAjxService: "saveBeforeAjxService("+vnB_Price2DetailDiv+")",
								saveSuccessAfter    : "saveSuccessAfter("+vnB_Price2DetailDiv+")"
														});
}

// 商品明細(建檔)
function item2DetailInitial(){
	vat.item.make(vnB_Item2DetailDiv, "indexNo"                 , {type:"IDX",        							desc:"序號"		});
	vat.item.make(vnB_Item2DetailDiv, "itemCodeBuilder"      	, {type:"TEXT", size: 13, maxLen:13,			desc:"品號"		});
	vat.item.make(vnB_Item2DetailDiv, "unitPriceBuilder"     	, {type:"TEXT",	mode:"READONLY", 				desc:"售價"		});
	vat.item.make(vnB_Item2DetailDiv, "paperBuilder"     		, {type:"NUMB", size: 13,						desc:"張數"		});
	vat.item.make(vnB_Item2DetailDiv, "itemCNameBuilder"     	, {type:"TEXT", alter:true, mode:"READONLY",	desc:"品名"		});
	vat.item.make(vnB_Item2DetailDiv, "description"     		, {type:"TEXT", size: 16,						desc:"說明"		});

	vat.block.pageLayout(vnB_Item2DetailDiv, {
		id: "vatItem2DetailDiv",
		pageSize: 10,
		selectionType 		: "NONE",
		indexType			: "AUTO",
		canGridDelete 		: false,
		canGridAppend 		: false,
		canGridModify 		: false,
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_Item2DetailDiv+")",
		loadSuccessAfter    : "loadSuccessAfter("+vnB_Item2DetailDiv+")",
		eventService        : "eventService()",
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_Item2DetailDiv+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_Item2DetailDiv+")"
	});
}

// 調撥單轉補條碼
function movement07DetailInitial(){
	vat.item.make(vnB_Movement07DetailDiv, "indexNo"                , {type:"IDX",        					desc:"序號"		});
	vat.item.make(vnB_Movement07DetailDiv, "itemCodeMovement"      	, {type:"TEXT", size: 13, maxLen:13,	desc:"品號"		});
	vat.item.make(vnB_Movement07DetailDiv, "unitPriceMovement"     	, {type:"TEXT",							desc:"售價", mode:"READONLY"		});
	vat.item.make(vnB_Movement07DetailDiv, "paperMovement"     		, {type:"NUMB", size: 13,				desc:"張數"		});
	vat.item.make(vnB_Movement07DetailDiv, "itemCNameMovement"     	, {type:"TEXT", alter:true,				desc:"品名", mode:"READONLY"		});
	vat.item.make(vnB_Movement07DetailDiv, "description"     		, {type:"TEXT", size: 16,				desc:"說明"		});
	
	vat.block.pageLayout(vnB_Movement07DetailDiv, {
		id: "vatMovement07DetailDiv",
		pageSize: 10,
		selectionType 		: "NONE",
		indexType			: "AUTO",
		canGridDelete 		: false,
		canGridAppend 		: false,
		canGridModify 		: false,
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_Movement07DetailDiv+")",
		loadSuccessAfter    : "loadSuccessAfter("+vnB_Movement07DetailDiv+")",
		eventService        : "eventService()",
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_Movement07DetailDiv+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_Movement07DetailDiv+")"
	});
}

// 進貨調整單(T2A)
function adjustReceiveDetailInitial(){
	vat.item.make(vnB_AdjustReceiveDetailDiv, "indexNo"                 , {type:"IDX",	desc:"序號" });
	vat.item.make(vnB_AdjustReceiveDetailDiv, "itemCode"      			, {type:"TEXT",	desc:"品號", mode:"READONLY", size: 13 });
	vat.item.make(vnB_AdjustReceiveDetailDiv, "category02"     			, {type:"TEXT",	desc:"類別", mode:"READONLY"	 });
	vat.item.make(vnB_AdjustReceiveDetailDiv, "itemBrand"     			, {type:"TEXT", desc:"品牌", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceiveDetailDiv, "category13"     			, {type:"TEXT", desc:"系列", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceiveDetailDiv, "unitPrice"     			, {type:"TEXT", desc:"價錢", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceiveDetailDiv, "quantity"     			, {type:"TEXT", desc:"數量", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceiveDetailDiv, "itemCName"     			, {type:"TEXT", desc:"品名", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceiveDetailDiv, "creationDate"     		, {type:"TEXT", desc:"批號", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceiveDetailDiv, "description"     		, {type:"TEXT", desc:"說明", mode:"READONLY"	});
	vat.block.pageLayout(vnB_AdjustReceiveDetailDiv, {
		id: "vatAdjustReceiveDetailDiv",
		pageSize: 10,
		selectionType 		: "NONE",
		indexType			: "AUTO",
		canGridDelete 		: false,
		canGridAppend 		: false,
		canGridModify 		: false,
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_AdjustReceiveDetailDiv+")",
		loadSuccessAfter    : "loadSuccessAfter("+vnB_AdjustReceiveDetailDiv+")",
		eventService        : "eventService()",
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_AdjustReceiveDetailDiv+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_AdjustReceiveDetailDiv+")"
	});
}

// 進貨調整單-外標(T2A)
function adjustReceive02DetailInitial(){
	vat.item.make(vnB_AdjustReceive02DetailDiv, "indexNo"               , {type:"IDX",	desc:"序號" });
	vat.item.make(vnB_AdjustReceive02DetailDiv, "itemCode"      		, {type:"TEXT",	desc:"品號", mode:"READONLY", size: 13 });
	vat.item.make(vnB_AdjustReceive02DetailDiv, "itemCName"     		, {type:"TEXT",	desc:"品名", mode:"READONLY"	 });
	vat.item.make(vnB_AdjustReceive02DetailDiv, "adjustDate"     		, {type:"TEXT", desc:"進貨日期", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceive02DetailDiv, "seq"     				, {type:"TEXT", desc:"序號", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceive02DetailDiv, "boxCapacity"     		, {type:"TEXT", desc:"裝箱量", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceive02DetailDiv, "declNo"     			, {type:"TEXT", desc:"序號", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceive02DetailDiv, "quantity"     			, {type:"TEXT", desc:"數量", mode:"READONLY"	});
	vat.item.make(vnB_AdjustReceive02DetailDiv, "description"  			, {type:"TEXT", desc:"說明", mode:"READONLY"	});
	
	vat.block.pageLayout(vnB_AdjustReceive02DetailDiv, {
		id: "vatAdjustReceive02DetailDiv",
		pageSize: 10,
		selectionType 		: "NONE",
		indexType			: "AUTO",
		canGridDelete 		: false,
		canGridAppend 		: false,
		canGridModify 		: false,
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_AdjustReceive02DetailDiv+")",
		loadSuccessAfter    : "loadSuccessAfter("+vnB_AdjustReceive02DetailDiv+")",
		eventService        : "eventService()",
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_AdjustReceive02DetailDiv+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_AdjustReceive02DetailDiv+")"
	});
}

// 進貨調整單-效期外標(T2A) by Weichun 2011.01.10
function adjustReceive03DetailInitial(){
	vat.item.make(vnB_AdjustReceive03DetailDiv, "indexNo"               , {type:"IDX",	desc:"序號", size: 1});
	vat.item.make(vnB_AdjustReceive03DetailDiv, "supplierItemCode"     	, {type:"TEXT",	desc:"原廠貨號", mode:"READONLY", size: 16 });
	vat.item.make(vnB_AdjustReceive03DetailDiv, "itemCName"     		, {type:"TEXT",	desc:"品名", mode:"READONLY"	, size: 28 });
	vat.item.make(vnB_AdjustReceive03DetailDiv, "expireDate"     		, {type:"TEXT", desc:"效期", mode:"READONLY", size: 6	});
	vat.item.make(vnB_AdjustReceive03DetailDiv, "itemCode"     			, {type:"TEXT", desc:"品號", mode:"READONLY", size: 16	});
	vat.item.make(vnB_AdjustReceive03DetailDiv, "boxCapacity"     		, {type:"NUMB", desc:"裝箱量", mode:"READONLY", size: 3	});
	vat.item.make(vnB_AdjustReceive03DetailDiv, "indexNo"     			, {type:"TEXT", desc:"項次", mode:"READONLY"	, size: 1});
	vat.item.make(vnB_AdjustReceive03DetailDiv, "quantity"     			, {type:"NUMB", desc:"張數", mode:"READONLY"	, size: 3});
	vat.item.make(vnB_AdjustReceive03DetailDiv, "difQuantity"     		, {type:"NUMB", desc:"進貨量", mode:"READONLY", size: 3});
	vat.item.make(vnB_AdjustReceive03DetailDiv, "adjustDate"     		, {type:"TEXT", desc:"進貨日期", mode:"READONLY"	, size: 6});
	vat.item.make(vnB_AdjustReceive03DetailDiv, "description"     		, {type:"TEXT", desc:"說明", mode:"READONLY"	, size: 16});
	
	vat.block.pageLayout(vnB_AdjustReceive03DetailDiv, {
		id: "vatAdjustReceive03DetailDiv",
		pageSize: 10,
		selectionType 		: "NONE",
		indexType			: "AUTO",
		canGridDelete 		: false,
		canGridAppend 		: false,
		canGridModify 		: false,
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_AdjustReceive03DetailDiv+")",
		loadSuccessAfter    : "loadSuccessAfter("+vnB_AdjustReceive03DetailDiv+")",
		eventService        : "eventService()",
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_AdjustReceive03DetailDiv+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_AdjustReceive03DetailDiv+")"
	});
}

// 進貨外標(Excel匯入) by Weichun 2011.03.02
function receive03DetailInitial(){
	vat.item.make(vnB_Receive03DetailDiv, "indexNo",  {type:"IDX", desc: "序號"});
	vat.item.make(vnB_Receive03DetailDiv, "itemCode", {type:"TEXT", size: 16, desc:"品號"});
	vat.item.make(vnB_Receive03DetailDiv, "itemCName", {type:"TEXT", size: 20,alter:true, desc:"品名"});
	vat.item.make(vnB_Receive03DetailDiv, "category02", {type:"TEXT", size: 10, desc:"中類"});
	vat.item.make(vnB_Receive03DetailDiv, "category02Name", {type:"TEXT", size: 20, desc:"中類名稱"});
	vat.item.make(vnB_Receive03DetailDiv, "boxCapacity", {type:"NUMB", size: 8, desc:"每箱數量"});
	vat.item.make(vnB_Receive03DetailDiv, "quantity", {type:"NUMB", size: 8, desc:"數量"});
	vat.item.make(vnB_Receive03DetailDiv, "supplierItemCode", {type:"TEXT", size: 16, desc:"廠商貨號"});
	vat.item.make(vnB_Receive03DetailDiv, "declartionDate", {type:"TEXT", size: 10, desc:"進貨日期"});
	vat.block.pageLayout(vnB_Receive03DetailDiv, {
														id: "vatReceive03DetailDiv",
														pageSize: 10,
														selectionType : "NONE",
														indexType	: "AUTO",
														searchKey	: ["imItemCode", "imUnitPrice", "paper", "imItemCName", "itemId"],
								            			canGridDelete : false,
														canGridAppend : false,
														canGridModify : false,
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Receive03DetailDiv+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_Receive03DetailDiv+")",
														eventService        : "eventService()",
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Receive03DetailDiv+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_Receive03DetailDiv+")"
														});
}


// 第一次載入 重新整理
function loadBeforeAjxService(div){
	var processString = "process_object_name=generateBarCodeService&process_object_method_name=getAJAXPageData" +
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")+
						"&orderNo=" + vat.item.getValueByName("#F.orderNo")+
						"&orderNoEnd=" + vat.item.getValueByName("#F.orderNoEnd")+
						"&div=" + activeTab +
						"&barCodeType=" + vat.item.getValueByName("#F.barCodeType") +
						"&orderBy=" + vat.item.getValueByName("#F.orderBy") +
						"&customsWarehouseCode=" + vat.item.getValueByName("#F.customsWarehouseCode") +
						"&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode") +
						"&showZero=" + vat.item.getValueByName("#F.showZero") +
						"&brandCode=" + vat.item.getValueByName("#F.brandCode") +
						"&taxType=" + vat.item.getValueByName("#F.taxType") +
						"&category02=" + vat.item.getValueByName("#F.category02")+
						"&category01=" + vat.item.getValueByName("#F.category01")+
						"&samePrice=" + vat.item.getValueByName("#F.samePrice")+
						"&startDate=" + vat.item.getValueByName("#F.startDate")+
						"&endDate=" + vat.item.getValueByName("#F.endDate")+
						"&supplierCode=" + vat.item.getValueByName("#F.supplierCode")+
						"&description=" + vat.item.getValueByName("#F.description")+
						"&timeScope=" + vat.block.$box[vnB_ItemDetailDiv].timeScope;

	return processString;
}

// 第一頁 翻到前或後頁 最後一頁
function saveBeforeAjxService(div){
	var processString = "";
	if(div == vnB_ItemDetailDiv){
		//	取得SAVE要執行的將搜尋結果存到暫存檔
		processString = "process_object_name=generateBarCodeService&process_object_method_name=saveSearchResult&startPage="+vat.block.getGridObject(div).firstIndex+
		"&pageSize="+ vat.block.getGridObject(div).pageSize;
		return processString;
	}else{
		processString = "process_object_name=generateBarCodeService&process_object_method_name=updateAJAXPageLinesData";
		return processString;
	}
}

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){
	vat.block.pageRefresh(div);
}

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(div){
	if( vnB_ItemDetailDiv != div && isSearch && vat.block.getGridObject(div).dataCount == vat.block.getGridObject(div).pageSize &&
	    vat.block.getGridObject(div).lastIndex == 1){
		alert("您輸入條件查無資料");
	}
}

// 新增空白頁
function appendBeforeService(){
	return true;
}

// 新增空白頁成功後
function appendAfterService(){
}

function eventService(){
}


// 動態撈出品號
function changeItemCode(){
	var nItemLine = vat.item.getGridLine();
	var sItemCode = vat.item.getGridValueByName("imItemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var processString = "process_object_name=imItemService&process_object_method_name=getAJAXItemCode"+
						"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
						"&itemCode=" + sItemCode +
						"&category02=" + vat.item.getValueByName("#F.category02")+
						"&category01=" + vat.item.getValueByName("#F.category01")+
						"&priceType=1";
	vat.ajax.XHRequest({
		post:processString,
		find: function change(oXHR){
			vat.item.setGridValueByName("checkbox", nItemLine, "Y");
			vat.item.setGridValueByName("imItemCode", nItemLine, vat.ajax.getValue("itemCode",oXHR.responseText));
			vat.item.setGridValueByName("imUnitPrice", nItemLine, vat.ajax.getValue("unitPrice",oXHR.responseText));
			vat.item.setGridValueByName("imItemCName", nItemLine, vat.ajax.getValue("itemCName",oXHR.responseText));
			vat.item.setGridValueByName("paper", nItemLine, vat.ajax.getValue("paper",oXHR.responseText));
			//vat.item.setGridValueByName("beginDate", ""));
			if("" != vat.ajax.getValue("description",oXHR.responseText))
				vat.item.setGridValueByName("imItemDesc", nItemLine, vat.ajax.getValue("description",oXHR.responseText));
			else
				vat.item.setGridValueByName("imItemDesc", nItemLine, " ");

		}
	});

}

// 動態新增欄位
function changeColumn(){
	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	if(barCodeType.indexOf("ImReceiveHead") > -1){
		vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", false);
		vat.item.setAttributeByName("#F.orderNo", "readOnly", false);
		vat.item.setAttributeByName("#F.orderBy", "readOnly", false);

		if(barCodeType.indexOf("01") > -1 ){  // 一般
			vat.item.setAttributeByName("#F.price", "readOnly", false);
			vat.item.setValueByName("#F.price", "Y");
		}else if(barCodeType.indexOf("02") > -1){ // 外標
			vat.item.setAttributeByName("#F.category01", "readOnly", false);
			vat.item.setValueByName("#F.category01", "");
		}
		
		vat.item.setAttributeByName("#F.category", "readOnly", false);
		vat.item.setAttributeByName("#F.category02", "readOnly", false);
		vat.item.setAttributeByName("#F.startDate", "readOnly", false);
		vat.item.setAttributeByName("#F.endDate", "readOnly", false);

		vat.item.setValueByName("#F.orderBy", "L.INDEX_NO");
		vat.item.setValueByName("#F.category", "Y");

		if(barCodeType.indexOf("03") > -1){ // excel匯入  by Weichun
			vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", true);
			vat.item.setAttributeByName("#F.orderNo", "readOnly", true);
			vat.item.setAttributeByName("#F.orderBy", "readOnly", true);
			vat.item.setAttributeByName("#F.category", "readOnly", true);
			vat.item.setAttributeByName("#F.category02", "readOnly", true);
		}
		
	}else if(barCodeType.indexOf("ImMovementHead") > -1){
		vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", false);
		vat.item.setAttributeByName("#F.orderNo", "readOnly", false);
		vat.item.setAttributeByName("#F.orderNoEnd", "readOnly", false);

	}else if(barCodeType.indexOf("ImPriceAdjustment") > -1){
		vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", false);
		vat.item.setAttributeByName("#F.orderNo", "readOnly", false);
		vat.item.setAttributeByName("#F.orderBy", "readOnly", false);
		vat.item.setAttributeByName("#F.price", "readOnly", false);
		vat.item.setAttributeByName("#F.category01", "readOnly", false);
		vat.item.setAttributeByName("#F.category", "readOnly", false);
		vat.item.setAttributeByName("#F.category02", "readOnly", false);
		vat.item.setAttributeByName("#F.warehouseCode", "readOnly", false);
		vat.item.setAttributeByName("#B.warehouseCode", "readOnly", false);
		vat.item.setAttributeByName("#F.customsWarehouseCode", "readOnly", false);
		vat.item.setAttributeByName("#F.showZero", "readOnly", false);
		vat.item.setAttributeByName("#F.samePrice", "readOnly", false);
		vat.item.setAttributeByName("#F.taxType", "readOnly", false);
		vat.item.setAttributeByName("#F.startDate", "readOnly", false);
		vat.item.setAttributeByName("#F.endDate", "readOnly", false);
		vat.item.setAttributeByName("#F.supplierCode", "readOnly", false);

		vat.item.setValueByName("#F.orderBy", "L.INDEX_NO");
		vat.item.setValueByName("#F.price", "Y");

		vat.item.setStyleByName("#B.print", 			"display", "inline");
		if(barCodeType.indexOf("01") > -1 ){  // 變價
			vat.item.setValueByName("#F.samePrice", "N");
		}else if(barCodeType.indexOf("02") > -1){ // 定價
			vat.item.setValueByName("#F.samePrice", "Y");
		}

		vat.item.setValueByName("#F.category", "Y");

	}else if(barCodeType.indexOf("ImItem") > -1){
		if(barCodeType.indexOf("01") > -1){ // 暫存
			vat.item.setStyleByName("#B.import", 			"display", "inline");
		}else if(barCodeType.indexOf("02") > -1){ // 建檔
			vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", false);
			vat.item.setAttributeByName("#F.orderNo", "readOnly", false);
			vat.item.setAttributeByName("#F.orderNoEnd", "readOnly", false);
			vat.item.setAttributeByName("#F.startDate", "readOnly", false);
			vat.item.setAttributeByName("#F.endDate", "readOnly", false);
		}

		vat.item.setAttributeByName("#F.category01", "readOnly", false);
		vat.item.setAttributeByName("#F.category02", "readOnly", false);
	}else if(barCodeType.indexOf("ImAdjustmentHead") > -1){
		vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", false);
		vat.item.setAttributeByName("#F.orderNo", "readOnly", false);
		if(barCodeType.indexOf("01") > -1){
			vat.item.setAttributeByName("#F.orderBy", "readOnly", false);
			vat.item.SelectBind([["#F.orderBy","",true],["項次","品號"],["L.INDEX_NO","L.ITEM_CODE"]]);
		}else if(barCodeType.indexOf("03") > -1){
			vat.item.setAttributeByName("#F.orderBy", "readOnly", false);
			vat.item.SelectBind([["#F.orderBy","",true],["項次","品號","原廠貨號","效期"],["L.INDEX_NO","L.ITEM_CODE","I.SUPPLIER_ITEM_CODE", "EXPIRE_DATE"]]);
		}
	}
	vat.item.setAttributeByName("#F.barCodeType", "readOnly", true);
	vat.item.setAttributeByName("#F.description", "readOnly", false);
	
}

// 動態改變單別下拉
function changeOrderTypeCode(){
	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	if(barCodeType.indexOf("ImReceiveHead") > -1){
		// 撈單別 type_code = IMR
		changeSelect("IMR", "");
	}else if(barCodeType.indexOf("ImMovementHead") > -1){
		// 撈單別 type_code = IM
		changeSelect("IM", "");
	}else if(barCodeType.indexOf("ImPriceAdjustment") > -1 ){
		// 撈單別 TYPE_CODE = PA, AND ORDER_TYPE_CODE = PAP,
		if(barCodeType.indexOf("01") > -1){
			changeSelect("PA", "PAJ");
		}else if(barCodeType.indexOf("02") > -1){
			changeSelect("PA", "PAP");
		}
	}else if(barCodeType.indexOf("02_ImItem") > -1 ){
		changeSelect("IT", "ITM");
	}else if(barCodeType.indexOf("ImAdjustmentHead") > -1 ){
		changeSelect("IAJ", "");
	}else{
		vat.item.SelectBind([["#F.orderTypeCode","",true],[],[]]);
	}
}

// 改變下拉
function changeSelect(typeCode,orderTypeCode){
	 vat.ajax.XHRequest(
       {
           post:"process_object_name=buOrderTypeService"+
                    "&process_object_method_name=getAJAXOrderTypeCode"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
                    "&typeCode=" + typeCode +
                    "&orderTypeCode="+ orderTypeCode ,
           find: function change(oXHR){
				var allOrderType = eval(vat.ajax.getValue("allOrderType", oXHR.responseText));
           		allOrderType[0][0] = "#F.orderTypeCode";
				vat.item.SelectBind(allOrderType);
           }
       });
}

// tab切換 存檔
function doPageDataSave(div){
	vat.block.pageRefresh(activeTab);
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
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

// picker 回來
function doAfterPickerFunctionProcess(id){

	if("#F.warehouseCode" == id){
		if( typeof vat.bean().vatBeanPicker.imWarehouseResult != 'undefined'){
	    	vat.item.setValueByName("#F.warehouseCode", vat.bean().vatBeanPicker.imWarehouseResult[0]["warehouseCode"]);
			changeLineWarehouseCode(id);
		}
	}else if("CATEGORY02" == id){
		if( typeof vat.bean().vatBeanPicker.categoryResult != 'undefined'){
	    	vat.item.setValueByName("#F.category02", vat.bean().vatBeanPicker.categoryResult[0]["id.categoryCode"]);
			changeCategoryCodeName(id);
		}
	}
}

// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "CATEGORY02":
			suffix += "&categoryType="+escape(id);
			break;
	}
	return suffix;
}

// 動態改變商品類別名稱
function changeCategoryCodeName(code){
	var condition = "", name ="";

	switch(code){
		case "CATEGORY02":
			condition =  "&categoryCode=" + ( "CATEGORY02" == code ? vat.item.getValueByName("#F.category02") : "" );
			name = "#F.category02Name";
		break;
	}

	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXImItemCategoryName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
                  "&categoryType=" + code + condition,

		find: function change(oXHR){
         		vat.item.setValueByName(name, vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		vat.item.setValueByName(name, "查無此類別");
		}
	});
}

// 改變庫別
function changeLineWarehouseCode(id){
	var warehouseCode	= vat.item.getValueByName("#F.warehouseCode");
	var brandCode 		= vat.bean().vatBeanOther.loginBrandCode;
	vat.ajax.XHRequest({
	           post:"process_object_name=imWarehouseService"+
	                    "&process_object_method_name=getAJAXWarehouseCode"+
	                    "&warehouseCode=" + warehouseCode +
	                    "&brandCode=" + brandCode,
	           find: function change(oXHR){
	           		vat.item.setValueByName("#F.warehouseCode", vat.ajax.getValue("warehouseCode", oXHR.responseText));
	           		vat.item.setValueByName("#F.warehouseName", vat.ajax.getValue("warehouseName", oXHR.responseText));
	           		vat.item.setValueByName("#F.customsWarehouseCode", vat.ajax.getValue("customsWarehouseCode", oXHR.responseText));

	           },
	           fail: function changeError(){
	          		vat.item.setGridValueByName("#F.warehouseCode", "");
	          		vat.item.setGridValueByName("#F.warehouseName", "-1");
	           }
	});
}

// 關別連動庫別
function changeStorage(){
	var brandCode 		= vat.bean().vatBeanOther.loginBrandCode;
	var customsWarehouseCode = vat.item.getValueByName("#F.customsWarehouseCode");
	vat.ajax.XHRequest({
	           post:"process_object_name=imWarehouseService"+
	                    "&process_object_method_name=getAJAXEditWarehouseCode"+
	                    "&brandCode=" + brandCode +
	                    "&customsWarehouseCode=" + customsWarehouseCode,
	           find: function changeRequestSuccess(oXHR){
	           var wc = eval( vat.ajax.getValue("warehouseCode", oXHR.responseText));
	           //alert("wc = " + wc);
	           wc[0][0] = "#F.warehouseCode";
	           vat.item.SelectBind(wc); 
           }
	});
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

// 查詢主檔headId
function doSearch(){
	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	var orderNo = vat.item.getValueByName("#F.orderNo");
	var orderNoEnd = vat.item.getValueByName("#F.orderNoEnd");
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var price = vat.item.getValueByName("#F.price");
	var category = vat.item.getValueByName("#F.category");
	var orderBy = vat.item.getValueByName("#F.orderBy");
	var taxType = vat.item.getValueByName("#F.taxType");
	var warehouseCode = vat.item.getValueByName("#F.warehouseCode");
	var showZero = vat.item.getValueByName("#F.showZero");

	var startDate = vat.item.getValueByName("#F.startDate");
	var endDate = vat.item.getValueByName("#F.endDate");
	var supplierCode = vat.item.getValueByName("#F.supplierCode");
	var customsWarehouseCode = vat.item.getValueByName("#F.customsWarehouseCode");//
	if(orderTypeCode == "" && barCodeType != "01_ImItem"  && barCodeType != ("03_ImReceiveHead")){
		alert('單別不可為空');
	}else if(orderNo == "" && (barCodeType != "01_ImItem" && barCodeType != ("03_ImReceiveHead") && !(barCodeType.indexOf("ImPriceAdjustment") > -1 || barCodeType.indexOf("02_ImItem") > -1  ) ) ){
		alert('單號不可為空');
	}else{
/*		vat.ajax.XHRequest({
				post:"process_object_name=generateBarCodeService"+
	            		"&process_object_method_name=getAJAXHeadId"+
	            		"&barCodeType="+ barCodeType +
	                	"&orderNo=" + orderNo +
	                	"&orderNoEnd=" + orderNoEnd +
	                	"&brandCode=" + brandCode +
	                	"&orderTypeCode="+ orderTypeCode +
	                	"&taxType=" + taxType +
	                	"&warehouseCode=" + warehouseCode +
	                	"&showZero=" + showZero +
	                	"&startDate=" + startDate +
	                	"&endDate=" + endDate +
	                	"&supplierCode=" +supplierCode,

				find: function changeRequestSuccess(oXHR){
					isSearch = true;
					if( "" !== vat.ajax.getValue("errorMsg", oXHR.responseText)   ){
						alert(vat.ajax.getValue("errorMsg", oXHR.responseText));

					}else{*/
//						vat.item.setValueByName("#F.headId", vat.ajax.getValue("headId", oXHR.responseText));
//						vat.item.setValueByName("#F.headIds", vat.ajax.getValue("headIds", oXHR.responseText));
						vat.block.pageRefresh(activeTab, vnCurrentPage = 1);
//					}
//				}
//		});
	}
}

// 匯出條碼
function doBarcodeExport(){
	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var orderNo = vat.item.getValueByName("#F.orderNo");
	var orderNoEnd = vat.item.getValueByName("#F.orderNoEnd");
	var orderBy = vat.item.getValueByName("#F.orderBy");
	var price = vat.item.getValueByName("#F.price");
	var category = vat.item.getValueByName("#F.category");
	var category01 = vat.item.getValueByName("#F.category01");
	var category02 = vat.item.getValueByName("#F.category02");
	var warehouseCode = vat.item.getValueByName("#F.warehouseCode");
	var customsWarehouseCode = vat.item.getValueByName("#F.customsWarehouseCode");
	var showZero = vat.item.getValueByName("#F.showZero");
	var taxType = vat.item.getValueByName("#F.taxType");
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var startDate = vat.item.getValueByName("#F.startDate");
	var beginDate = vat.item.getValueByName("#F.beginDate");
	var endDate = vat.item.getValueByName("#F.endDate");
	var supplierCode = vat.item.getValueByName("#F.supplierCode");
	var description = vat.item.getValueByName("#F.description");
	var samePrice = vat.item.getValueByName("#F.samePrice");

	var exportBeanName = barCodeType.substring(3,barCodeType.length);
	if(barCodeType == ""){
		alert('類型不可為空');
	}else if( barCodeType == "02_ImReceiveHead" && orderBy =="" ){
		if( orderBy =="" ){
			alert('請選擇排序方式');
		}
	}else if( barCodeType == "01_ImReceiveHead" && (price == "" || category == "" ) ){
		if( price == ""){
			alert('請選擇是否包含價格');
		}else if( category == "" ){
			alert('請選擇是否包含類別');
		}
	}else if( (barCodeType == "01_ImPriceAdjustment" || barCodeType == "02_ImPriceAdjustment") && ( taxType == "") ){ // warehouseCode == "" ||
		if(taxType == ""){
			alert('請選擇稅別');
		}
	}else{

		var url = "/erp/jsp/ExportBarCode.jsp" +
	              "?fileType=TXT" +
	              "&processObjectName=generateBarCodeService" +
	              "&processObjectMethodName=executeMatch" +
	              "&brandCode=" + brandCode +
	              "&barCodeType=" + barCodeType +
	              "&orderTypeCode=" + orderTypeCode +
	              "&orderNo=" + orderNo +
	              "&orderNoEnd=" + orderNoEnd +
	              "&orderBy=" + orderBy +
	              "&price=" + price +
	              "&category=" + category +
	              "&category01=" + category01 +
	              "&category02=" + category02 +
	              "&warehouseCode=" + warehouseCode +
	              "&customsWarehouseCode=" + customsWarehouseCode +
	              "&showZero=" + showZero +
	              "&samePrice=" + samePrice +
	              "&taxType=" + taxType +
	              "&startDate=" + startDate +
	              "&beginDate=" + beginDate +
	              "&endDate=" + endDate +
	              "&supplierCode=" + supplierCode +
	              "&description=" + description +
	              "&exportBeanName=" + exportBeanName +
	              "&timeScope=" + vat.block.$box[vnB_ItemDetailDiv].timeScope+
	              "&div=" + activeTab;
	    var width = "200";
	    var height = "30";

		if(activeTab == vnB_ItemDetailDiv ){
			vat.block.pageDataSave( vnB_ItemDetailDiv ,{
				asyn: false,
				funcSuccess:function(){
//					window.open(url, '條碼子系統', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
				}
			});
		}
		window.open(url, '條碼子系統', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

	}
}

// 依品牌,條碼類型決定哪種明細
function doMatchLine(){
	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	if(barCodeType.indexOf("ImPriceAdjustment") > -1 ){
		// 變價明細
		if(barCodeType.indexOf("Decl") > -1){
			activeTab = vnB_Price2DetailDiv;

			vat.tabm.displayToggle(0, "xTab8", true);
			vat.block.pageDataLoad(vnB_Price2DetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab1", false);
		}else{
			activeTab = vnB_PriceDetailDiv;

			vat.tabm.displayToggle(0, "xTab1", true);
			vat.block.pageDataLoad(vnB_PriceDetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab8", false);
		}

		// 關閉其他
		vat.tabm.displayToggle(0, "xTab2", false);
		vat.tabm.displayToggle(0, "xTab3", false);
		vat.tabm.displayToggle(0, "xTab4", false);
		vat.tabm.displayToggle(0, "xTab5", false);
		vat.tabm.displayToggle(0, "xTab6", false);
		vat.tabm.displayToggle(0, "xTab7", false);
		vat.tabm.displayToggle(0, "xTab10", false);
		vat.tabm.displayToggle(0, "xTab11", false);
		vat.tabm.displayToggle(0, "xTab12", false);
		vat.tabm.displayToggle(0, "xTab13", false);
		vat.tabm.displayToggle(0, "xTab14", false);
		doFormAccessControl("match");
	}else if(barCodeType.indexOf("01_ImReceiveHead") > -1){
		// 進貨明細
		activeTab = vnB_ReceiveDetailDiv;
		vat.tabm.displayToggle(0, "xTab2", true);
		vat.block.pageDataLoad(vnB_ReceiveDetailDiv, vnCurrentPage = 1);


		// 關閉其他
		vat.tabm.displayToggle(0, "xTab1", false);
		vat.tabm.displayToggle(0, "xTab3", false);
		vat.tabm.displayToggle(0, "xTab4", false);
		vat.tabm.displayToggle(0, "xTab5", false);
		vat.tabm.displayToggle(0, "xTab6", false);
		vat.tabm.displayToggle(0, "xTab7", false);
		vat.tabm.displayToggle(0, "xTab8", false);
		vat.tabm.displayToggle(0, "xTab10", false);
		vat.tabm.displayToggle(0, "xTab11", false);
		vat.tabm.displayToggle(0, "xTab12", false);
		vat.tabm.displayToggle(0, "xTab13", false);
		vat.tabm.displayToggle(0, "xTab14", false);
		doFormAccessControl("match");
	}else if(barCodeType.indexOf("02_ImReceiveHead") > -1){
		// 進貨外標明細
		activeTab = vnB_Receive02DetailDiv;
		vat.tabm.displayToggle(0, "xTab5", true);
		vat.block.pageDataLoad(vnB_Receive02DetailDiv, vnCurrentPage = 1);

		// 關閉其他
		vat.tabm.displayToggle(0, "xTab1", false);
		vat.tabm.displayToggle(0, "xTab2", false);
		vat.tabm.displayToggle(0, "xTab3", false);
		vat.tabm.displayToggle(0, "xTab4", false);
		vat.tabm.displayToggle(0, "xTab6", false);
		vat.tabm.displayToggle(0, "xTab7", false);
		vat.tabm.displayToggle(0, "xTab8", false);
		vat.tabm.displayToggle(0, "xTab10", false);
		vat.tabm.displayToggle(0, "xTab11", false);
		vat.tabm.displayToggle(0, "xTab12", false);
		vat.tabm.displayToggle(0, "xTab13", false);
		vat.tabm.displayToggle(0, "xTab14", false);
		doFormAccessControl("match");
	}else if(barCodeType.indexOf("03_ImReceiveHead") > -1) { // 進貨外標明細(Excel匯入)
		activeTab = vnB_Receive03DetailDiv;
		vat.tabm.displayToggle(0, "xTab14", true);
		vat.block.pageDataLoad(vnB_Receive03DetailDiv, vnCurrentPage = 1);

		// 關閉其他
		vat.tabm.displayToggle(0, "xTab1", false);
		vat.tabm.displayToggle(0, "xTab2", false);
		vat.tabm.displayToggle(0, "xTab3", false);
		vat.tabm.displayToggle(0, "xTab4", false);
		vat.tabm.displayToggle(0, "xTab5", false);
		vat.tabm.displayToggle(0, "xTab6", false);
		vat.tabm.displayToggle(0, "xTab7", false);
		vat.tabm.displayToggle(0, "xTab8", false);
		vat.tabm.displayToggle(0, "xTab10", false);
		vat.tabm.displayToggle(0, "xTab11", false);
		vat.tabm.displayToggle(0, "xTab12", false);
		vat.tabm.displayToggle(0, "xTab13", false);
		doFormAccessControl("match");
		vat.item.setStyleByName("#B.search", "display", "none");
		vat.item.setStyleByName("#B.importExcel", "display", "inline");
	}else if(barCodeType.indexOf("ImMovementHead") > -1 ){

		if(barCodeType.indexOf("05") > -1){
			// 調撥明細-裝箱單麥頭
			activeTab = vnB_MovementDetailDiv;
			vat.tabm.displayToggle(0, "xTab3", true);
			vat.block.pageDataLoad(vnB_MovementDetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab6", false);
			vat.tabm.displayToggle(0, "xTab7", false);
			vat.tabm.displayToggle(0, "xTab10", false);
			vat.tabm.displayToggle(0, "xTab11", false);
		}else if(barCodeType.indexOf("03") > -1){
			// 調撥食品菸明細
			activeTab = vnB_Movement03DetailDiv;
			vat.tabm.displayToggle(0, "xTab6", true);
			vat.block.pageDataLoad(vnB_Movement03DetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab3", false);
			vat.tabm.displayToggle(0, "xTab7", false);
			vat.tabm.displayToggle(0, "xTab10", false);
		}else if(barCodeType.indexOf("06") > -1){
			// 調撥化妝品精品明細
			activeTab = vnB_Movement06DetailDiv;
			vat.tabm.displayToggle(0, "xTab7", true);
			vat.block.pageDataLoad(vnB_Movement06DetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab3", false);
			vat.tabm.displayToggle(0, "xTab6", false);
			vat.tabm.displayToggle(0, "xTab10", false);
		}else if(barCodeType.indexOf("07") > -1){
			// 調撥轉補條碼
			activeTab = vnB_Movement07DetailDiv;
			vat.tabm.displayToggle(0, "xTab10", true);
			vat.block.pageDataLoad(vnB_Movement06DetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab3", false);
			vat.tabm.displayToggle(0, "xTab6", false);
			vat.tabm.displayToggle(0, "xTab7", false);
		}

		// 關閉其他
		vat.tabm.displayToggle(0, "xTab1", false);
		vat.tabm.displayToggle(0, "xTab2", false);
		vat.tabm.displayToggle(0, "xTab4", false);
		vat.tabm.displayToggle(0, "xTab5", false);
		vat.tabm.displayToggle(0, "xTab8", false);
		vat.tabm.displayToggle(0, "xTab11", false);
		vat.tabm.displayToggle(0, "xTab12", false);
		vat.tabm.displayToggle(0, "xTab13", false);
		vat.tabm.displayToggle(0, "xTab14", false);
		doFormAccessControl("match");

	}else if(barCodeType.indexOf("ImItem") > -1 ){
		// 商品主檔明細
		if(barCodeType.indexOf("01") > -1){ // 暫存
			activeTab = vnB_ItemDetailDiv;
			vat.tabm.displayToggle(0, "xTab4", true);
			vat.block.pageDataLoad(vnB_ItemDetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab9", false);
		}else if(barCodeType.indexOf("02") > -1){ // 建檔
			activeTab = vnB_Item2DetailDiv;
			vat.tabm.displayToggle(0, "xTab9", true);
			vat.block.pageDataLoad(vnB_Item2DetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab4", false);
		}

		// 關閉其他
		vat.tabm.displayToggle(0, "xTab1", false);
		vat.tabm.displayToggle(0, "xTab2", false);
		vat.tabm.displayToggle(0, "xTab3", false);
		vat.tabm.displayToggle(0, "xTab5", false);
		vat.tabm.displayToggle(0, "xTab6", false);
		vat.tabm.displayToggle(0, "xTab7", false);
		vat.tabm.displayToggle(0, "xTab8", false);
		vat.tabm.displayToggle(0, "xTab10", false);
		vat.tabm.displayToggle(0, "xTab11", false);
		vat.tabm.displayToggle(0, "xTab12", false);
		vat.tabm.displayToggle(0, "xTab13", false);
		vat.tabm.displayToggle(0, "xTab14", false);
		doFormAccessControl("match");
	}else if(barCodeType.indexOf("ImAdjustmentHead") > -1 ){
		//調整進貨單
		if(barCodeType.indexOf("01") > -1){ // 調整進貨單
			activeTab = vnB_AdjustReceiveDetailDiv;
			vat.tabm.displayToggle(0, "xTab11", true);
			vat.block.pageDataLoad(vnB_AdjustReceiveDetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab12", false);
			vat.tabm.displayToggle(0, "xTab13", false);
		}else if(barCodeType.indexOf("02") > -1){ // 調整進貨單-外標
			activeTab = vnB_AdjustReceive02DetailDiv;
			vat.tabm.displayToggle(0, "xTab12", true);
			vat.block.pageDataLoad(vnB_AdjustReceive02DetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab11", false);
			vat.tabm.displayToggle(0, "xTab13", false);
		}else if(barCodeType.indexOf("03") > -1){ // 調整進貨單-效期外標 by Weichun 2011.01.11\
			activeTab = vnB_AdjustReceive03DetailDiv;
			vat.tabm.displayToggle(0, "xTab13", true);
			vat.block.pageDataLoad(vnB_AdjustReceive03DetailDiv, vnCurrentPage = 1);
			vat.tabm.displayToggle(0, "xTab11", false);
			vat.tabm.displayToggle(0, "xTab12", false);
		}

		// 關閉其他
		vat.tabm.displayToggle(0, "xTab1", false);
		vat.tabm.displayToggle(0, "xTab2", false);
		vat.tabm.displayToggle(0, "xTab3", false);
		vat.tabm.displayToggle(0, "xTab4", false);
		vat.tabm.displayToggle(0, "xTab5", false);
		vat.tabm.displayToggle(0, "xTab6", false);
		vat.tabm.displayToggle(0, "xTab7", false);
		vat.tabm.displayToggle(0, "xTab8", false);
		vat.tabm.displayToggle(0, "xTab9", false);
		vat.tabm.displayToggle(0, "xTab10", false);
		vat.tabm.displayToggle(0, "xTab14", false);
		doFormAccessControl("match");
	}else{
		alert("請先選擇類型");
	}

}

// 初始化 form
function initialFormAccessControl(){

	vat.item.setStyleByName("#B.search", 			"display", "none");
	vat.item.setStyleByName("#B.export", 			"display", "none");
	vat.item.setStyleByName("#B.import", 			"display", "none");
	vat.item.setStyleByName("#B.importExcel",		"display", "none");
	vat.item.setStyleByName("#B.barCodeExport", 	"display", "none");
	vat.item.setStyleByName("#B.match", 			"display", "inline");
	vat.item.setStyleByName("#B.unMatch", 			"display", "none");
	vat.item.setStyleByName("#B.print", 			"display", "none");

	vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", true);
	vat.item.setAttributeByName("#F.orderNo", "readOnly", true);
	vat.item.setAttributeByName("#F.orderNoEnd", "readOnly", true);
	vat.item.setAttributeByName("#F.orderBy", "readOnly", true);
	vat.item.setAttributeByName("#F.price", "readOnly", true);
	vat.item.setAttributeByName("#F.category", "readOnly", true);
	vat.item.setAttributeByName("#F.category02", "readOnly", true);
	vat.item.setAttributeByName("#F.description", "readOnly", true);
	vat.item.setAttributeByName("#F.warehouseCode", "readOnly", true);
//	vat.item.setAttributeByName("#B.warehouseCode", "readOnly", true);
	vat.item.setAttributeByName("#F.showZero", "readOnly", true);
	vat.item.setAttributeByName("#F.customsWarehouseCode", "readOnly", true);
	vat.item.setAttributeByName("#F.samePrice", "readOnly", true);
	vat.item.setAttributeByName("#F.taxType", "readOnly", true);
	vat.item.setAttributeByName("#F.startDate", "readOnly", true);
	vat.item.setAttributeByName("#F.beginDate", "readOnly", true);
	vat.item.setAttributeByName("#F.endDate", "readOnly", true);
	vat.item.setAttributeByName("#F.supplierCode", "readOnly", true);

	vat.item.setValueByName("#F.orderNoEnd", "");
	vat.item.setValueByName("#F.orderBy", "");
	vat.item.setValueByName("#F.price", "");
	vat.item.setValueByName("#F.samePrice", "N");
	vat.item.setValueByName("#F.category", "");
	vat.item.setValueByName("#F.warehouseCode", "");
	vat.item.setValueByName("#F.category", "");
	vat.item.setValueByName("#F.startDate", "");
	vat.item.setValueByName("#F.beginDate", "");
	vat.item.setValueByName("#F.endDate", "");
	vat.item.setValueByName("#F.supplierCode", "");

}

// 依狀態鎖form
function doFormAccessControl( match ){

	initialFormAccessControl();
	if( match =="match" ){
		var barCodeType = vat.item.getValueByName("#F.barCodeType");

		vat.item.setStyleByName("#B.search", 			"display", "inline");
		vat.item.setStyleByName("#B.clear", 			"display", "inline");
		vat.item.setStyleByName("#B.export", 			"display", "inline");
		vat.item.setStyleByName("#B.barCodeExport", 	"display", "inline");
		vat.item.setStyleByName("#B.match", 			"display", "none");
		vat.item.setStyleByName("#B.unMatch", 			"display", "inline");

		changeColumn();
	}else if( match =="unMatch"){
		vat.item.setStyleByName("#B.search", 			"display", "none");
		vat.item.setStyleByName("#B.barCodeExport", 	"display", "none");
		vat.item.setStyleByName("#B.match", 			"display", "inline");
		vat.item.setStyleByName("#B.unMatch", 			"display", "none");
		vat.item.setStyleByName("#B.import", 			"display", "none");

		activeTab = 4;
		isSearch = false;

		vat.item.setAttributeByName("#F.barCodeType", "readOnly", false);

		vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.orderNo", "readOnly", true);
		vat.item.setAttributeByName("#F.orderNoEnd", "readOnly", true);
		vat.item.setAttributeByName("#F.orderBy", "readOnly", true);
		vat.item.setAttributeByName("#F.price", "readOnly", true);
		vat.item.setAttributeByName("#F.category", "readOnly", true);
		vat.item.setAttributeByName("#F.category01", "readOnly", true);
		vat.item.setAttributeByName("#F.category02", "readOnly", true);
		vat.item.setAttributeByName("#F.description", "readOnly", true);
		vat.item.setAttributeByName("#F.warehouseCode", "readOnly", true);
		vat.item.setAttributeByName("#F.taxType", "readOnly", true);
		vat.item.setAttributeByName("#F.startDate", "readOnly", true)
		vat.item.setAttributeByName("#F.beginDate", "readOnly", true);
		vat.item.setAttributeByName("#F.endDate", "readOnly", true);
		vat.item.setAttributeByName("#F.supplierCode", "readOnly", true);

		vat.item.setValueByName("#F.orderTypeCode", "");
		vat.item.setValueByName("#F.orderNo", "");
		vat.item.setValueByName("#F.orderNoEnd", "");
		vat.item.setValueByName("#F.orderBy", "");
		vat.item.setValueByName("#F.price", "");
		vat.item.setValueByName("#F.samePrice", "N");
		vat.item.setValueByName("#F.category", "");
		vat.item.setValueByName("#F.category01", "");
		vat.item.setValueByName("#F.category02", "");
		vat.item.setValueByName("#F.description", "");
		vat.item.setValueByName("#F.category02Name", "");
		vat.item.setValueByName("#F.warehouseCode", "");
		vat.item.setValueByName("#F.warehouseName", "");
		vat.item.setValueByName("#F.showZero", "N");
		vat.item.setValueByName("#F.customsWarehouseCode", "");
		vat.item.setValueByName("#F.category", "");
		vat.item.setValueByName("#F.headId", "");
		vat.item.setValueByName("#F.headIds", "");
		vat.item.setValueByName("#F.startDate", "");
		vat.item.setValueByName("#F.beginDate", "");
		vat.item.setValueByName("#F.endDate", "");
		vat.item.setValueByName("#F.supplierCode", "");

		vat.tabm.displayToggle(0, "xTab1", false);
		vat.tabm.displayToggle(0, "xTab2", false);
		vat.tabm.displayToggle(0, "xTab3", false);
		vat.tabm.displayToggle(0, "xTab4", false);
		vat.tabm.displayToggle(0, "xTab5", false);
		vat.tabm.displayToggle(0, "xTab6", false);
		vat.tabm.displayToggle(0, "xTab7", false);
		vat.tabm.displayToggle(0, "xTab8", false);
		vat.tabm.displayToggle(0, "xTab9", false);
		vat.tabm.displayToggle(0, "xTab10", false);
		vat.tabm.displayToggle(0, "xTab11", false);
		vat.tabm.displayToggle(0, "xTab12", false);
		vat.tabm.displayToggle(0, "xTab13", false);
		vat.tabm.displayToggle(0, "xTab14", false);
		vat.block.pageRefresh(vnB_MovementDetailDiv);
	}
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.orderTypeCode", "");
	vat.item.setValueByName("#F.orderNo", "");
	vat.item.setValueByName("#F.orderNoEnd", "");
	vat.item.setValueByName("#F.orderBy", "");
	vat.item.setValueByName("#F.price", "");
	vat.item.setValueByName("#F.category", "");
	vat.item.setValueByName("#F.category01", "");
	vat.item.setValueByName("#F.category02", "");
	vat.item.setValueByName("#F.description", "");
	vat.item.setValueByName("#F.category02Name", "");
	vat.item.setValueByName("#F.warehouseCode", "");
	vat.item.setValueByName("#F.warehouseName", "");
	vat.item.setValueByName("#F.showZero", "N");
	vat.item.setValueByName("#F.customsWarehouseCode", "");
	vat.item.setValueByName("#F.samePrice", "N");
	vat.item.setValueByName("#F.category", "");
	vat.item.setValueByName("#F.headId", "");
	vat.item.setValueByName("#F.headIds", "");
	vat.item.setValueByName("#F.startDate", "");
	vat.item.setValueByName("#F.beginDate", "");
	vat.item.setValueByName("#F.endDate", "");
	vat.item.setValueByName("#F.supplierCode", "");

	// 將暫存的清除
	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	if(barCodeType.indexOf("01_ImItem") > -1){
		vat.bean().vatBeanOther.timeScope = vat.block.$box[vnB_ItemDetailDiv].timeScope
	 	vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService&process_object_method_name=deleteByOtherBeanTimeScope"; }, // &timeScope="+ vat.bean().vatBeanOther.timeScope
			{other: true,
				funcSuccess: function() {vat.block.pageDataLoad(vnB_ItemDetailDiv , vnCurrentPage = 1);}
			});
	}
}

//sql明細匯出excel
function doExport(){
	var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var beanName = ""; // standard_ie.properties檔中對應的key

	if(vnB_PriceDetailDiv == activeTab){ // 定變價
		beanName = "BARCODE_PRICE";
	}else if(vnB_ReceiveDetailDiv == activeTab){ // 進貨
		beanName = "BARCODE_RECEIVE";
	}else if(vnB_MovementDetailDiv == activeTab){ // 調撥
		beanName = "BARCODE_MOVEMENT";
	}else if(vnB_ItemDetailDiv == activeTab){ // 商品主檔(暫存)
		beanName = "BARCODE_TMP_ITEM";
		vat.block.pageDataSave( vnB_ItemDetailDiv ); // 先存檔以利明細匯出
	}else if(vnB_Receive02DetailDiv == activeTab){ // 進貨外標
		beanName = "BARCODE_RECEIVE_02";
	}else if(vnB_Movement03DetailDiv == activeTab){ // 調撥食品菸
		beanName = "BARCODE_MOVEMENT_03";
	}else if(vnB_Movement06DetailDiv == activeTab){ // 調撥化妝品.精品
		beanName = "BARCODE_MOVEMENT_06";
	}else if(vnB_Price2DetailDiv == activeTab){ // 定變價(含報單日期)
		beanName = "BARCODE_PRICE_DECLARATION";
	}else if(vnB_Item2DetailDiv == activeTab){ // 商品主檔(建檔)
		beanName = "BARCODE_BUILDER_ITEM";
	}else if(vnB_Movement07DetailDiv == activeTab){ // 調撥轉補條碼
		beanName = "BARCODE_MOVEMENT_07";
	}else if(vnB_AdjustReceiveDetailDiv == activeTab){ // 進貨調整單(T2A)
		beanName = "BARCODE_ADJUST_RECEIVE_01";
	}else if(vnB_AdjustReceive02DetailDiv == activeTab){ // 進貨調整單外標(T2A)
		beanName = "BARCODE_ADJUST_RECEIVE_02";
	}else if(vnB_AdjustReceive03DetailDiv == activeTab){ // 進貨調整單效期外標(T2A) by Weichun 2011.01.12
		beanName = "BARCODE_ADJUST_RECEIVE_03";
	}else if(vnB_Receive03DetailDiv == activeTab){ // 進貨外標(Excel匯入) by Weichun 2011.03.02
		beanName = "BARCODE_RECEIVE_02";
	}


	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var orderNo = vat.item.getValueByName("#F.orderNo");
	var orderNoEnd = vat.item.getValueByName("#F.orderNoEnd");
	var orderBy = vat.item.getValueByName("#F.orderBy");
	var price = vat.item.getValueByName("#F.price");
	var category = vat.item.getValueByName("#F.category");
	var category01 = vat.item.getValueByName("#F.category01");
	var category02 = vat.item.getValueByName("#F.category02");
	var warehouseCode = vat.item.getValueByName("#F.warehouseCode");
	var customsWarehouseCode = vat.item.getValueByName("#F.customsWarehouseCode");
	var showZero = vat.item.getValueByName("#F.showZero");
	var taxType = vat.item.getValueByName("#F.taxType");
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var startDate = vat.item.getValueByName("#F.startDate");
	var beginDate = vat.item.getValueByName("#F.beginDate");
	var endDate = vat.item.getValueByName("#F.endDate");
	var supplierCode = vat.item.getValueByName("#F.supplierCode");
	var samePrice = vat.item.getValueByName("#F.samePrice");

    var url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&processObjectName=generateBarCodeService" +
              "&processObjectMethodName=getAJAXExportData" +
              "&brandCode=" + brandCode +
	          "&barCodeType=" + barCodeType +
	          "&orderTypeCode=" + orderTypeCode +
	          "&orderNo=" + orderNo +
	          "&orderNoEnd=" + orderNoEnd +
	          "&orderBy=" + orderBy +
	          "&price=" + price +
	          "&category=" + category +
	          "&category01=" + category01 +
	          "&category02=" + category02 +
	          "&warehouseCode=" + warehouseCode +
	          "&customsWarehouseCode=" + customsWarehouseCode +
	          "&showZero=" + showZero +
	          "&samePrice=" + samePrice +
	          "&taxType=" + taxType +
	          "&startDate=" + startDate +
	          "&beginDate=" + beginDate +
	          "&endDate=" + endDate +
	          "&supplierCode=" + supplierCode +
	          "&timeScope=" + vat.block.$box[vnB_ItemDetailDiv].timeScope +
	          "&div=" + activeTab;

    var width = "200";
    var height = "30";


	if(activeTab == vnB_ItemDetailDiv ){
		vat.block.pageDataSave( vnB_ItemDetailDiv ,{
			asyn: false,
			funcSuccess:function(){
			}
		});
	}
	window.open(url, '匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 補條碼(暫存)匯入
function doImport(){
	var width = "600";
    var height = "400";
    var timeScope= vat.block.$box[vnB_ItemDetailDiv].timeScope;
    var brandCode = vat.item.getValueByName("#F.brandCode");
    var beginDate = vat.item.getValueByName("#F.beginDate");
    var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=BARCODE_TMP_ITEM_IMPORT" +
			"&importFileType=XLS" +
	        "&processObjectName=generateBarCodeService" +
	        "&processObjectMethodName=executeImportLists" +
	        "&arguments=" + timeScope + "{$}" + brandCode + 
	        "&parameterTypes=String{$}String" +
	        "&blockId=" + vnB_ItemDetailDiv,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 進貨外標(Excel匯入)
function doImportExcel(){
	var width = "600";
    var height = "400";
    var timeScope= vat.block.$box[vnB_ItemDetailDiv].timeScope;
    var brandCode = vat.item.getValueByName("#F.brandCode");
    var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=BARCODE_RECEIVE_TMP_IMPORT" +
			"&importFileType=XLS" +
	        "&processObjectName=generateBarCodeService" +
	        "&processObjectMethodName=executeImportExcel" +
	        "&arguments=" + timeScope + "{$}" + brandCode +
	        "&parameterTypes=String{$}String" +
	        "&blockId=" + vnB_Receive03DetailDiv,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 票據列印 變價
function openReportWindow(type){

	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");

    vat.bean().vatBeanOther.category01 = vat.item.getValueByName("#F.category01");
    vat.bean().vatBeanOther.category02 = vat.item.getValueByName("#F.category02");
    vat.bean().vatBeanOther.warehouseCode = vat.item.getValueByName("#F.warehouseCode");
	vat.bean().vatBeanOther.customsWarehouseCode = vat.item.getValueByName("#F.customsWarehouseCode");
	vat.bean().vatBeanOther.showZero = vat.item.getValueByName("#F.showZero");
	vat.bean().vatBeanOther.taxType = vat.item.getValueByName("#F.taxType");
	vat.bean().vatBeanOther.startDate = vat.item.getValueByName("#F.startDate");
	vat.bean().vatBeanOther.endDate = vat.item.getValueByName("#F.endDate");
	vat.bean().vatBeanOther.supplierCode = vat.item.getValueByName("#F.supplierCode");
	vat.bean().vatBeanOther.samePrice = vat.item.getValueByName("#F.samePrice");

    var orderBy = vat.item.getValueByName("#F.orderBy");

    if("L.INDEX_NO" == orderBy){
    	vat.bean().vatBeanOther.reportFileName = "SO0756.rpt";  // 依順序排序變價單的報表
    }else{ // L.ITEM_CODE
    	vat.bean().vatBeanOther.reportFileName = "SO0757.rpt";	// 依品號排序變價單的報表
    }

	if("01_Decl_ImPriceAdjustment" == barCodeType){
		if("L.INDEX_NO" == orderBy){
			vat.bean().vatBeanOther.reportFileName = "SO0756_1.rpt";  // 依順序排序變價單(含報表)的報表
		}else{
			vat.bean().vatBeanOther.reportFileName = "SO0757_1.rpt";  // 依品號排序變價單(含報表)的報表
		}
	}

    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=generateBarCodeService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
			                    	//alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);

	if("AFTER_SUBMIT"==type) createRefreshForm();
}