/*** 
 *	檔案: imItem.js
 *	說明：表單明細
 *	修改：david
 *  <pre>
 *  	Created by Joe
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_vatT3 = 3;  // 標準售價
var vnB_vatT4 = 4;  // 商品圖片 
var vnB_vatT5 = 5;	// 組合商品
var vnB_vatT6 = 6;	// 庫存資料
var vnB_vatT7 = 7;	// 成本資料
var vnB_vatT8 = 8;  // T2 其他欄位
var vnB_vatT9 = 9;	// 國際碼
var vnB_vatT10 = 10; // 進貨紀錄
var vnB_vatT11 = 11; // 洗標
var activeTab = 3;

function kweImBlock(){

  	kweImInitial();
	kweButtonLine();
  	kweImHeader();

	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");         
		vat.tabm.createButton(0 ,"xTab1","規格及類別" ,"vatMasterDiv" ,"images/tab_specification_dark.gif" ,"images/tab_specification_light.gif", false, "doPageDataSave(1)");
		
		if(isT2()){
			vat.tabm.createButton(0 ,"xTab8","規格及類別" ,"vatMaster2Div" ,"images/tab_specification_dark.gif" ,"images/tab_specification_light.gif", false, "doPageDataSave(8)");
		}
				      
		vat.tabm.createButton(0 ,"xTab2","標準售價" ,"vatT3Div" ,"images/tab_standard_price_dark.gif" ,"images/tab_standard_price_light.gif", "none", "doPageDataSave(3)");                                                                                                                                                                                                                       
		vat.tabm.createButton(0 ,"xTab3","商品圖片" ,"vatT4Div" ,"images/tab_item_image_dark.gif" ,"images/tab_item_image_light.gif", "none", "doPageDataSave(4)" );                                                                                                    
		vat.tabm.createButton(0 ,"xTab4","組合商品" ,"vatT5Div" ,"images/tab_compose_item_dark.gif" ,"images/tab_compose_item_light.gif", "none", "doPageDataSave(5)" );
		vat.tabm.createButton(0 ,"xTab5","庫存資料" ,"vatT6Div","images/tab_on_hand_data_dark.gif","images/tab_on_hand_data_light.gif", "none" , "doPageDataSave(6)" );
		vat.tabm.createButton(0 ,"xTab6","成本資料" ,"vatT7Div","images/tab_cost_data_dark.gif","images/tab_cost_data_light.gif", "none" , "doPageDataSave(7)" );
		if(isT2()){
			vat.tabm.createButton(0 ,"xTab9","國際碼" ,"vatT9Div","images/tab_ean_dark.gif","images/tab_ean_light.gif", "none" , "doPageDataSave("+vnB_vatT9+")" );
		}
		vat.tabm.createButton(0 ,"xTab10","進貨紀錄" ,"vatT10Div","images/tab_receive_dark.gif","images/tab_receive_light.gif", "none" , "doPageDataSave("+vnB_vatT10+")" );
		if(!isT2()){
			vat.tabm.createButton(0 ,"xTab11","洗標" ,"vatT11Div","images/tab_washmark_dark.gif","images/tab_washmark_light.gif", false , "doPageDataSave("+vnB_vatT11+")" );
		}
		
  	}
  	
  	kweMaster();  	// 規格及類別
	kweImVatT3();	// 標準售價
	kweImVatT5();	// 組合商品
	kweImVatT6(); 	// 庫存資料
	kweImVatT7();	// 成本資料
	
	if(isT2()){
		kweImVatT8();	// T2 其他欄位
		kweImVatT9();	// 國際碼
	}
	kweImVatT10();	// 進貨紀錄資料
	if(!isT2()){
		kweImVatT11();  // 洗標
	}
	
	doFormAccessControl();
	var isComposeItem = vat.item.getValueByName("#F.isComposeItem");
   	if(isComposeItem=="Y"){
  	 	// 如果是組合商品，提供明細匯入的按鈕
  	 	vat.item.setStyleByName("#B.import", "display", "inline");
  	}else{
   		vat.item.setStyleByName("#B.import", "display", "none");
  	}
   
}

// 是否為T2
function isT2(){
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	if( brandCode.indexOf("T2") > -1 ){
		return true;
	}else{
		return false;
	} 
}


function getColumn(brandCode, col){
	if( brandCode.indexOf("T2") > -1){
		return {};
	}else{
		return col;
	} 
}

function kweImInitial(){
	
	 	initialVaTBeanCode();
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imItemAction&process_object_method_name=performInitial"; 
	    	},{
	    		other: true
    	});
  	
}


function initialVaTBeanCode(){
	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: "T2",
          loginEmployeeCode  	: "T96085",
          formId             	: "1981247",	
          processId          	: "", 
          assignmentId       	: "",
          isOppositePicker 		: "",
          userType 				: "",     
          currentRecordNumber : 0,
	      lastRecordNumber    : 0
        };
}

function initialVaTBeanOther(){
	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: "T2",
          loginEmployeeCode  	: "T49674",
          formId             	: "1981247",	
          processId          	: "", 
          assignmentId       	: "",
          isOppositePicker 		: "",
          userType 				: "",     	
          firstRecordNumber		: vat.bean().vatBeanOther.firstRecordNumber,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
}

function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_Item:search:20091106.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  ,
					 openMode:"open",
					 service:"/erp/fileUpload:standard:2.page",
					 servicePassData:function(x){ return importFormData(); },
					 left:0, right:0, width:600, height:400,
					 serviceAfterPick:function(){ /*afterImportSuccess()*/}},
				{name:"SPACE"          , type:"LABEL"  ,value:"　"},
			    {name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("FINISH")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

// 主檔
function kweImHeader(){ 

    var allItemUnit = vat.bean("allItemUnit");
    var allCategoryType = vat.bean("allCategoryType");
 	var allCurrencyCodes = vat.bean("allCurrencyCodes");
    
    var allIsComposeItem = [["", true, false], ["否", "是"], ["N", "Y"]];
    var allEnable =        [["", true, false], ["啟用", "停用"], ["Y", "N"]];
	
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	var itemCNameValue = "中文名稱";
	var itemCNameMaxLen = "50";
	if(brandCode.indexOf("T2") > -1 ){
		itemCNameValue = "品名";
		itemCNameMaxLen = "24";
	} 
	
	vat.block.create( 0, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"商品主檔維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.itemCode", 		type:"LABEL", 	value:"商品品號<font color='red'>*</font>"}]},
				{items:[{name:"#F.itemCode", 		type:"TEXT",	size:16, 	bind:"itemCode" , eChange:"changeItemCode()"},
					{name:"#H.itemId"  , 			type:"TEXT"  ,  bind:"itemId", back:false, mode:"HIDDEN"},
					{name:"#F.isLockPurchaseAmount",	type:"TEXT", 	bind:"isLockPurchaseAmount", back:false, mode:"HIDDEN"},
				    {name:"#L.isComposeItem", 		type:"LABEL", 	value:"組合商品?"},
				    {name:"#F.isComposeItem", 		type:"SELECT", 	bind:"isComposeItem",init:allIsComposeItem,onchange:"showComposeItem()"}]},
				    /*
				    {name:"#F.isComposeItem", 		type:"SELECT", 	bind:"isComposeItem",init:allIsComposeItem,onchange:"showComposeItem()"},

				    {name:"#F.space",	type:"TEXT", back:false,size:1, mode:"READONLY"},
				    {name:"#L.composeLevel", 		type:"LABEL", 	value:"階層"},
				    {name:"#F.composeLevel", 		type:"TEXT", 	bind:"composeLevel", back:false, mode:"READONLY"}]},
				    */
				{items:[{name:"#L.status",			type:"LABEL", 	value:"狀態" }]},
				{items:[//{name:"#F.brandCode", 		type:"hidden", 	bind:"brandCode"},
				        //{name:"#F.statusName", 	type:"TEXT", 	bind:"statusName",mode:"READONLY"},
				        //{name:"#L.isEnable", 		type:"LABEL", 	value:"停用?"}, 
				        {name:"#F.enable", 			type:"SELECT", 	bind:"enable",init:allEnable },
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }
				     ]},
			    {items:[{name:"#L.brandCode", 		type:"LABEL", 	value:"品牌"}]},	
				{items:[{name:"#F.brandCode", 		type:"TEXT", 	bind:"brandCode", mode:"HIDDEN" },
			            {name:"#F.brandName", 		type:"TEXT",	bind:"brandName", back:false, mode:"READONLY"}]},
			    {items:[{name:"#L.lastUpdateBy",	type:"LABEL", 	value:"修改人員" }]},
				{items:[{name:"#F.lastUpdateBy",	type:"TEXT", 	bind:"lastUpdatedBy", mode:"HIDDEN"},
						{name:"#F.department",		type:"TEXT", 	bind:"department", mode:"HIDDEN"},
					{name:"#F.lastUpdatedByName",	type:"TEXT", 	bind:"lastUpdatedByName", back:false, mode:"READONLY"}]},
			    {items:[{name:"#L.lastUpdateDate",	type:"LABEL", 	value:"修改日期" }]},
				{items:[{name:"#F.lastUpdateDate",	type:"TEXT", 	bind:"lastUpdateDate", mode:"READONLY"}]} 			 		 	
			]},
			{row_style:"", cols:[
			    {items:[{name:"#L.itemCName", 		type:"LABEL", 	value:itemCNameValue }]},
				{items:[{name:"#F.itemCName", 		type:"TEXT", 	bind:"itemCName",size:61 ,maxLen:itemCNameMaxLen}],td:" colSpan=3"},
//				{items:[{name:"#L.categoryType", 	type:"LABEL", 	value:"業種" }]},
//				{items:[{name:"#F.categoryType", 	type:"SELECT", 	init:allCategoryType, 	bind:"categoryType"}]},
				{items:[{name:"#L.description",		type:"LABEL", 	value:"說明" }],td:" rowSpan=2"},
				{items:[{name:"#F.description",		type:"TEXTAREA",  bind:"description" , maxLen:100, row:2, col: 40	}],td:" rowSpan=2 colSpan=5"}
				
			]},
			{row_style:"", cols:[
			    {items:[{name:"#L.salesRatio", 		type:"LABEL", 	value:"庫存單位" }]},
				{items:[{name:"#F.salesRatio", 		type:"NUMB", 	bind:"salesRatio"},
	                    {name:"#L.star", 			type:"LABEL", 	value:"　＊　" },
	                    {name:"#F.salesUnit", 		type:"SELECT", 	bind:"salesUnit",init:allItemUnit },
	                    {name:"#F.equal", 			type:"LABEL", 	value:"　＝　 " }]},
				{items:[{name:"#L.purchaseUnit",	type:"LABEL", 	value:"進貨單位" }]},
				{items:[{name:"#F.purchaseUnit",	type:"SELECT", 	bind:"purchaseUnit" ,init:allItemUnit}]}
			]}		
		], 	
		beginService:"",
		closeService:""			
	});
	  	//vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
	}

function kweMaster(){
	var category01CName = vat.bean("category01CName");
	var category02CName = vat.bean("category02CName");
	
    var allCategory01 = vat.bean("allCategory01");
    var allCategory02 = vat.bean("allCategory02");
    var allCategory03 = vat.bean("allCategory03");
    var allCategory05 = vat.bean("allCategory05");
    var allCategory06 = vat.bean("allCategory06");
    var allCategory07 = vat.bean("allCategory07");
    var allCategory12 = vat.bean("allCategory12");
    var allCategory13 = vat.bean("allCategory13");
    var allCategory20 = vat.bean("allCategory20");
 
 	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
 	var itemENameValue = "英文名稱";
 	var category17CName = "製造商";
 	var isHidden = "";
 	var mustWrite = "";
 	var specWeight = "* 深：";
 	var category13Type = "SELECT";
 	var category13MaxLen = 40;
 	var category13PickerType = "HIDDEN";
 	var standardPurchaseCostIsReadOnly = "";
 	var release = "releaseDate"; // 上市日期
 	var releaseType = "DATE";
 	var expiry = "expiryDate"; // 下市日期
 	var expiryType = "DATE";
 	var itemENameMaxLen = "200";
 	var isTaxReadOnly = "ReadOnly";
 	
 	var costReadOnly = "";
 	var userType = vat.bean().vatBeanOther.userType;
 	if(brandCode.indexOf("T2") > -1 ){
 		itemENameValue = "其他品名";
 		category17CName += "/供應商<font color='red'>*</font>";
// 		isHidden = "HIDDEN";
 		mustWrite = "<font color='red'>*</font>";
 		category01CName += "<font color='red'>*</font>";
 		category02CName += "<font color='red'>*</font>";
 		specWeight = "* 重量/容量："; // 
 		category13Type = "TEXT";
 		allCategory13 = "";
 		category13PickerType = "PICKER";
 		standardPurchaseCostIsReadOnly = "ITEM" != userType ? "HIDDEN" : "ReadOnly";
 		if("ITEMLOCK" == userType){
 		standardPurchaseCostIsReadOnly = "ITEMLOCK" != userType ? "HIDDEN" : "ReadOnly";
 		}else if("MOD" == userType){
 		standardPurchaseCostIsReadOnly = "MOD" != userType ? "HIDDEN" : "ReadOnly";
 		}else if("WITHOUTCOST" == userType){
 		standardPurchaseCostIsReadOnly = "WITHOUTCOST" != userType ? "HIDDEN" : "ReadOnly";
 		}
 		
 		release = "releaseString";
 		releaseType = "TEXT";
 		expiry = "expiryString";
 		expiryType = "TEXT";
 		itemENameMaxLen = "200";
 		isTaxReadOnly = "";
 	}else{
 		standardPurchaseCostIsReadOnly = "ITEM" != userType ? "HIDDEN" : "";
 		costReadOnly = "ITEM" != userType ? "HIDDEN" : "";
 		
 	}
 
    var selectYorN = [["", true, false], ["否", "是"], ["N", "Y"]];
    
    var allLotControls = vat.bean("allLotControls");
    
    var allTaxRadio = brandCode.indexOf("T2") <= -1 ? [["", true, false], ["含稅", "未稅"], ["P", "F"]] : [["", true, false], ["完稅", "保稅"], ["P", "F"]];
    
    var vsRowStyle= brandCode.indexOf("T2") > -1 ? " style= 'display:none;'":"";
	var vsColStyle= brandCode.indexOf("T2") <= -1 ? " style= 'display:none;'":"";

vat.block.create(vnB_Master, {

	id: "vatMasterDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[
				{row_style:"", cols:[
			    {items:[{name:"#L.supplierItemCode",type:"LABEL", 	value:"原廠貨號" }]},
				{items:[{name:"#F.supplierItemCode",type:"TEXT", 	bind:"supplierItemCode",size:30, maxLen:40}]}, 
	 			{items:[{name:"#L.itemLevel",		type:"LABEL", 	value:"商品等級" }]},
				{items:[{name:"#F.itemLevel",		type:"TEXT", 	bind:"itemLevel"}]},
				{items:[{name:"#L."+release,		type:"LABEL", 	value:"上市日期" }]},
				{items:[{name:"#F."+release,		type:releaseType, 	bind:release }]},
				{items:[{name:"#L."+expiry,			type:"LABEL", 	value:"下市日期" }]},
				{items:[{name:"#F."+expiry,			type:expiryType, 	bind:expiry}]}
		    ]},
		{row_style:"", cols:[
				{items:[{name:"#L.itemEName",	type:"LABEL", 	value:itemENameValue }]},
				{items:[{name:"#F.itemEName",	type:"TEXT", 	bind:"itemEName", maxLen:itemENameMaxLen, size:48}]},
/*				{items:[{name:"#L.serial",		type:"LABEL", 	value:"序號" }]},
				{items:[
//						{name:"#F.serial",		type:"TEXT", 	bind:"serial", eChange: function(){ changeSerial("serial"); }, maxLen:8, size:8},
						
						{name:"#B.serial",		type:"PICKER" , value:"選取" ,
			 									 		openMode:"open", src:"./images/start_node_16.gif",
			 									 		service:"Im_Item:createSerial:20100404.page",  // Im_Item:searchSerial:20100404.page
			 									 		left:0, right:0, width:1024, height:768,
			 									 		servicePassData:function(){ return doPassData("serialPicker"); },	
			 									 		serviceBeforePick:function(){ return doBeforePick("serialPicker"); },	
			 									 		serviceAfterPick:function(){  } }]},	*/// doAfterPickerFunctionProcess("serialPicker");
			 									 		
//			 			{name:"#F.lineId", 		type:"TEXT",  	bind:"lineId", back:false, mode:"HIDDEN"}, 	 			 		 		
//			 			{name:"#F.serialMemo", 	type:"TEXT",  	bind:"serialMemo", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.spec",		type:"LABEL", 	value:"規格" }]},
				{items:[{name:"#L.specLength",	type:"LABEL",   value:"長："},
				        {name:"#F.specLength",	type:"TEXT", 	bind:"specLength", maxLen:10},
				        {name:"#L.specWidth",	type:"LABEL",   value:"* 寬："},
				        {name:"#F.specWidth",	type:"TEXT", 	bind:"specWidth" , maxLen:10},
				        {name:"#L.specHeight",	type:"LABEL",   value:"* 高："},
				        {name:"#F.specHeight",	type:"TEXT", 	bind:"specHeight" , maxLen:10},
				        {name:"#L.specWeight",	type:"LABEL",   value:specWeight},
				        {name:"#F.specWeight",	type:"TEXT", 	bind:"specWeight"} 
				  ],td:"colSpan=3"},
			    {items:[{name:"#L.eStore",		type:"LABEL", 	value:"網購" }]},
				{items:[{name:"#F.eStoreReserve1",	type:"TEXT", 	bind:"EStoreReserve1"},
						{name:"#F.eStoreReserve2",	type:"TEXT", 	bind:"EStoreReserve2",mode:"HIDDEN"},
						{name:"#F.eStoreReserve3",	type:"TEXT", 	bind:"EStoreReserve3",mode:"HIDDEN"},
						{name:"#F.eStore",		type:"XBOX", 	bind:"EStore"}]}
			]},
	    {row_style:"", cols:[
			    {items:[{name:"#L.category01",	type:"LABEL", 	value:category01CName }]},	
				{items:[{name:"#F.category01",	type:"SELECT", 	bind:"category01" ,init:allCategory01, eChange:function(){changeCategory("01","02");}}], size:1}, //	大類
	 			{items:[{name:"#L.category02",	type:"LABEL", 	value:category02CName }]},
				{items:[{name:"#F.category02",	type:"SELECT", 	bind:"category02",init:allCategory02, eChange:function(){changeCategory("02","03");} }]}, // 中類
				{items:[{name:"#L.category03",	type:"LABEL", 	bind:"category03CName" }]},
				{items:[{name:"#F.category03",	type:"SELECT", 	bind:"category03",init:allCategory03}]},	// 小類 
				{items:[{name:"#L.category04",	type:"LABEL", 	bind:"category04CName" }]},
				{items:[{name:"#F.category04",	type:"TEXT", 	bind:"category04", maxLen:20}]}  			// 尺寸
		    ]},
         {row_style:"", cols:[
			    {items:[{name:"#L.category05",	type:"LABEL", 	bind:"category05CName" }]},
				{items:[{name:"#F.category05",	type:"SELECT", 	bind:"category05",init:allCategory05}], size:1}, //  年份
	 			{items:[{name:"#L.category06",	type:"LABEL", 	bind:"category06CName" }]},
				{items:[{name:"#F.category06",	type:"SELECT", 	bind:"category06",init:allCategory06}]}, //  季別
				{items:[{name:"#L.category07",	type:"LABEL", 	bind:"category07CName" }]},
				{items:[{name:"#F.category07",	type:"SELECT", 	bind:"category07",init:allCategory07}]}, //  性別
				{items:[{name:"#L.category08",	type:"LABEL", 	bind:"category08CName" }]},
				{items:[{name:"#F.category08",	type:"TEXT", 	bind:"category08", maxLen:20}]}  		// 材質
		    ]},
          {row_style:"", cols:[
			    {items:[{name:"#L.category09",	type:"LABEL", 	bind:"category09CName" }]},
				{items:[{name:"#F.category09",	type:"TEXT", 	bind:"category09", maxLen:20}], size:1}, 	// 年份
	 			{items:[{name:"#L.category10",	type:"LABEL", 	bind:"category10CName" }]},
				{items:[{name:"#F.category10",	type:"TEXT", 	bind:"category10", maxLen:40}]}, 	// 顏色
				{items:[{name:"#L.category11",	type:"LABEL", 	bind:"category11CName" }]},
				{items:[{name:"#F.category11",	type:"TEXT", 	bind:"category11", maxLen:20}]},	// 款式編號
				{items:[{name:"#L.category12",	type:"LABEL", 	bind:"category12CName" }]},
				{items:[{name:"#F.category12",	type:"SELECT", 	bind:"category12",init:allCategory12}]}  // 屬性
		    ]},
          {row_style:"", cols:[
			    {items:[{name:"#L.category13",	type:"LABEL", 	bind:"category13CName" }]},
				{items:[{name:"#F.category13",	type:category13Type, 	bind:"category13",init:allCategory13, maxLen:category13MaxLen }], size:1}, // , eChange: function(){ brandCode.indexOf("T2") > -1 ? changeCategoryCodeName("CATEGORY13") : ""; } 系列
//						{name:"#B.category13",	value:"選取" ,type:category13PickerType ,
//			 									 		openMode:"open", src:"./images/start_node_16.gif",
//			 									 		service:"Im_Item:searchCategory:20100119.page", 
//			 									 		left:0, right:0, width:1024, height:768,
//			 									 		servicePassData:function(){ return doPassData("CATEGORY13"); },	
//			 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("category13");} },
//			 			{name:"#F.category13Name",	type:"TEXT", 	bind:"category13Name", back:false, mode:"READONLY"}]}, // 系別
	 			{items:[{name:"#L.category14",	type:"LABEL", 	bind:"category14CName" }]},
				{items:[{name:"#F.category14",	type:"TEXT", 	bind:"category14", maxLen:20}]}, 	// 產地
				{items:[{name:"#L.category15",	type:"LABEL", 	bind:"category15CName" }]},
				{items:[{name:"#F.category15",	type:"TEXT", 	bind:"category15", maxLen:20}]},	// 功能
				{items:[{name:"#L.category16",	type:"LABEL", 	bind:"category16CName" }]},
				{items:[{name:"#F.category16",	type:"TEXT", 	bind:"category16", maxLen:20}]}  	// 樣本編號
		    ]},
          {row_style:"", cols:[
			    {items:[{name:"#L.category17",	type:"LABEL", 	value:category17CName }]},
				{items:[{name:"#F.category17",	type:"TEXT", 	bind:"category17", eChange: function(){ changeSupplierName("supplierCode"); }}, // 製造商/供應商
						{name:"#B.category17",	value:"選取" ,type:"PICKER" ,
			 									 		openMode:"open", src:"./images/start_node_16.gif",
			 									 		service:"Bu_AddressBook:searchSupplier:20091011.page", 
			 									 		left:0, right:0, width:1024, height:768,	
			 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("category17");} },
			 			{name:"#F.addressBookId", 		type:"TEXT",  	bind:"addressBookId", back:false, mode:"HIDDEN"},						 		
			 			{name:"#F.supplierName",	type:"TEXT", bind:"supplierName", back:false, mode:"READONLY",size:10 }]}, 
	 			{items:[{name:"#L.category18",	type:"LABEL", 	bind:"category18CName" }]},
				{items:[{name:"#F.category18",	type:"TEXT", 	bind:"category18", maxLen:20}]}, 	// 其他1 
				{items:[{name:"#L.category19",	type:"LABEL", 	bind:"category19CName" }]},
				{items:[{name:"#F.category19",	type:"TEXT", 	bind:"category19", maxLen:20}]},	// 其他2
				{items:[{name:"#L.category20",	type:"LABEL", 	bind:"category20CName" }]},
				{items:[{name:"#F.category20",	type:"SELECT", 	bind:"category20",init:allCategory20}]}  // 帳務類別
		    ]},
		{row_style:"", cols:[
			    {items:[{name:"#L.purchaseRatio",	type:"LABEL", 	value:"預計採購比" }]},
				{items:[{name:"#F.purchaseRatio",	type:"NUMB", 	bind:"purchaseRatio"},
				        {name:"#L.percent",	type:"LABEL", 	value:"%"}]}, 
	 			{items:[{name:"#L.boxCapacity",	type:"LABEL", 	value:"每箱數量" }]},
				{items:[{name:"#F.boxCapacity",	type:"NUMB", 	bind:"boxCapacity"}]},
				{items:[{name:"#L.isServiceItem",	type:"LABEL", 	value:"服務性商品?" },
				        {name:"#F.isServiceItem",	type:"SELECT", bind:"isServiceItem",init:selectYorN },
				        {name:"#L.lotControl",	type:"LABEL", 	value:"本商品執行批號管理?" },
				        {name:"#F.lotControl",	type:"SELECT", bind:"lotControl",init:allLotControls}],td:"colSpan=4"}
		    ]},
		{row_style:"", cols:[
			    {items:[{name:"#L.foreignListPrice",	type:"LABEL", 	value:"原幣零售價" }]},
				{items:[{name:"#F.foreignListPrice",	type:"NUMB", 	bind:"foreignListPrice", mode:costReadOnly}]}, 
	 			{items:[{name:"#L.supplierQuotationPrice",	type:"LABEL", 	value:"原廠報價" }]},
				{items:[{name:"#F.supplierQuotationPrice",	type:"NUMB", 	bind:"supplierQuotationPrice", mode:costReadOnly}]},
				{items:[{name:"#L.standardPurchaseCost",	type:"LABEL", 	value:"計價後成本", mode:costReadOnly }]},
				{items:[{name:"#F.standardPurchaseCost",	type:"NUMB", 	bind:"standardPurchaseCost", mode:standardPurchaseCostIsReadOnly }]},
				{items:[{name:"#L.isTax",			type:"LABEL", 	value:"稅別"+mustWrite}]},
				{items:[{name:"#F.isTax",			type:"SELECT", 	bind:"isTax", size:12, maxLen:12 , init:allTaxRadio, mode:isTaxReadOnly}]}
		    ]},
		{row_style:vsColStyle, cols:[
			    {items:[{name:"#L.colorCode",	type:"LABEL", 	value:"<font color='blue'>色碼</font>" }]},
				{items:[{name:"#F.colorCode",	type:"TEXT", size:20, maxLen:20, 	bind:"colorCode"}]}, 
	 			{items:[{name:"#L.material",	type:"LABEL", 	value:"<font color='blue'>材質說明</font>" }]},
				{items:[{name:"#F.material",	type:"TEXT", size:30, maxLen:30, 	bind:"material"}]},
				{items:[{name:"#L.validityDay",	type:"LABEL", 	value:"<font color='blue'>效期天數</font>" }]},
				{items:[{name:"#F.validityDay",	type:"TEXT", size:4, maxLen:4, 	bind:"validityDay"}]},
				{items:[{name:"#L.foreignCategory",	type:"LABEL", 	value:"<font color='blue'>國外類別</font>" }]},
				{items:[{name:"#F.foreignCategory",	type:"TEXT", size:20, maxLen:20, 	bind:"foreignCategory"}]}
		    ]},
		{row_style:vsRowStyle, cols:[
			    {items:[{name:"#L.category22",	type:"LABEL", 	value:"材質1" }]},
				{items:[{name:"#F.category22",	type:"TEXT", size:20, maxLen:80, 	bind:"category22"}]},
				{items:[{name:"#L.category21",	type:"LABEL", 	value:"材質2" }]},
				{items:[{name:"#F.category21",	type:"TEXT", size:20, maxLen:80, 	bind:"category21"}],td:"colSpan=5"}
		    ]},
		    
		 {row_style:vsRowStyle, cols:[
			    {items:[{name:"#L.desc",	type:"LABEL", 	value:"<Strong>計價後成本(台幣)：<br/></Strong>"+  
                            "1.為採購單所使用，於採購單輸入時，系統將依據品號代出上期之月結後成本，如為新品並無上月份之月結成本，系統即自動以此欄位作為採購單上之計價後成本。<br/>"+
                            "2.月結計算成本時，如上月以無庫存，但本月有退貨情況發生時，系統將依此金額做為退貨之庫存成本。<br/>"+
                            "<Strong>原廠報價(原幣)：</Strong><br/>1.訂價單使用，訂價單將依據原廠報價*匯率*固定比例計算出其成本金額。"}],td:"colSpan=8"}
		    ]}   
	 ],
		beginService:"",
		closeService:""			
	});
	
}
	
	// 標準售價
function kweImVatT3(){
    var allPriceType = vat.bean("allPriceType");
    
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
  
  	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
  	var unitPriceValue = "單價";
  	if(brandCode.indexOf("T2") > -1){
  		unitPriceValue = "售價";
  	}
  
    vat.item.make(vnB_vatT3, "indexNo"  		, {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_vatT3, "typeCode"			, {type:"SELECT",init:allPriceType , size:12, maxLen:12, desc:"價格類別" });
	vat.item.make(vnB_vatT3, "unitPrice"	    , {type:"NUMB" , size:12, maxLen:12, desc:unitPriceValue, eChange:"changeUnitPriceReadOnly()" });
	vat.item.make(vnB_vatT3, "beginDate"	    , {type:"TEXT" , bind:"beginDate",mode:"READONLY",size:10, desc:"起用日期"});
	vat.item.make(vnB_vatT3, "createByName"	, {type:"TEXT" , bind:"createByName",mode:"READONLY",size:10, desc:"填單人員"});
	
	vat.item.make(vnB_vatT3, "enable"		    , {type:"TEXT" , size:12, maxLen:12,bind:"enableName",mode:"READONLY", desc:"狀態"});
	vat.item.make(vnB_vatT3, "priceId"          ,  {type:"HIDDEN"});
	
	vat.block.pageLayout(vnB_vatT3, {
														id: "vatT3Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT3+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT3+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_vatT3+")"
														});
	vat.block.pageDataLoad(vnB_vatT3, vnCurrentPage = 1);
}

	// 商品圖片
function kweImVatT4(){

	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = false;
    vat.item.make(vnB_vatT4, "indexNo"  		, {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_vatT4, "itemNo"			, {type:"TEXT" , size:12, maxLen:12, desc:"報單項次" });
	vat.item.make(vnB_vatT4, "vehicleNo"	    , {type:"TEXT" , size:12, maxLen:12, desc:"車身號碼" });
	vat.item.make(vnB_vatT4, "itemId"          ,  {type:"HIDDEN"});
	vat.block.pageLayout(vnB_vatT4, {
														id: "vatT4Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT4+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT4+")",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_vatT4, vnCurrentPage = 1);
}
	
	// 組合商品
function kweImVatT5(){
    
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
	
    var brandCode = vat.bean().vatBeanOther.loginBrandCode;
  
    vat.item.make(vnB_vatT5, "indexNo"  			, {type:"IDX"  , desc:"序號" });
    vat.item.make(vnB_vatT5, "composeItemCode"		, {type:"TEXT" , size:14, maxLen:14, desc:"品號", eChange:"showComposeItemData()" });
	vat.item.make(vnB_vatT5, "composeItemName"		, {type:"TEXT" , size:12, maxLen:12,mode:"READONLY", desc:"中文名稱"});
	//vat.item.make(vnB_vatT5, "composeItemUnit"  , {type:"TEXT" , size:12, maxLen:12,mode:"READONLY", desc:"庫存單位"});
	vat.item.make(vnB_vatT5, "quantity"	        	, {type:"NUMB" , size:12, maxLen:6, desc:"數量" ,eChange:"checkComposeQuantity()"});
	vat.item.make(vnB_vatT5, "purchaseCurrencyCode"	, {type:"TEXT" , size:12, maxLen:6, desc:"幣別", mode:"READONLY" });
	vat.item.make(vnB_vatT5, "itemPrice"			, {type:"NUMB" , size:12, maxLen:6, desc:"定價", mode:"READONLY" });
	vat.item.make(vnB_vatT5, "itemCost"				, {type:"NUMB" , size:12, maxLen:6, desc:"成本", mode:"READONLY" });
	//vat.item.make(vnB_vatT5, "composeLevel"			, {type:"NUMB" , size:12, maxLen:6, desc:"階層", mode:"READONLY" });
	vat.item.make(vnB_vatT5, "remark"	    		, {type:"TEXT" , size:36, maxLen:12, desc:"備註"});
	vat.item.make(vnB_vatT5, "reserve1"	    		, {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"提示訊息"});
	
	vat.item.make(vnB_vatT5, "composeId"          	,  {type:"HIDDEN"});
	//vat.item.make(vnB_vatT5, "itemId"          	,  {type:"HIDDEN"});
	//vat.item.make(vnB_vatT5, "itemCode"          	,  {type:"HIDDEN"});
	vat.block.pageLayout(vnB_vatT5, {
														id: "vatT5Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT5+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT5+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_vatT5+")"
														});
	//vat.block.pageDataLoad(vnB_vatT5, vnCurrentPage = 1);
}	

	// 庫存資料
function kweImVatT6(){

	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = false;
  
    vat.item.make(vnB_vatT6, "indexNo"  		        , {type:"IDX"  , desc:"序號" });
    vat.item.make(vnB_vatT6, "warehouseCode"	        , {type:"TEXT" , size:12, maxLen:12, desc:"庫別"});
	vat.item.make(vnB_vatT6, "warehouseName"	        , {type:"TEXT" , size:12, maxLen:12, desc:"名稱"});
	//vat.item.make(vnB_vatT6, "lotNo"	                , {type:"TEXT" , size:12, maxLen:12, desc:"批號"});
	vat.item.make(vnB_vatT6, "stockOnHandQty"	        , {type:"TEXT" , size:12, maxLen:12, desc:"目前庫存量"});
	vat.item.make(vnB_vatT6, "outUncommitQty"	        , {type:"TEXT" , size:12, maxLen:12, desc:"銷貨未結庫存"});
	vat.item.make(vnB_vatT6, "inUncommitQty"	        , {type:"TEXT" , size:12, maxLen:12, desc:"採購未結庫存"});
	vat.item.make(vnB_vatT6, "moveUncommitQty"	        , {type:"TEXT" , size:12, maxLen:12, desc:"調撥未結庫存"});
	vat.item.make(vnB_vatT6, "otherUncommitQty"	        , {type:"TEXT" , size:12, maxLen:12, desc:"其他未結庫存"});
	vat.item.make(vnB_vatT6, "currentOnHandQty"	        , {type:"TEXT" , size:12, maxLen:12, desc:"可用庫存量"});
	
	//vat.item.make(vnB_vatT6, "itemCode"          ,  {type:"HIDDEN"});
	vat.block.pageLayout(vnB_vatT6, {
														id: "vatT6Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT6+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT6+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_vatT6+")"
														});
	//vat.block.pageDataLoad(vnB_vatT6, vnCurrentPage = 1);
}	
	
// 成本資料
function kweImVatT7(){

	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = false;
  
    vat.item.make(vnB_vatT7, "indexNo"  		        , {type:"IDX"  , desc:"序號" });
    vat.item.make(vnB_vatT7, "lineYear"	        , {type:"TEXT" , size:12, maxLen:12, desc:"年"});
	vat.item.make(vnB_vatT7, "lineMonth"	        , {type:"TEXT" , size:12, maxLen:12, desc:"月"});
	vat.item.make(vnB_vatT7, "periodSalesQuantity"	                , {type:"TEXT" , size:12, maxLen:12, desc:"銷售量"});
	vat.item.make(vnB_vatT7, "periodPurchaseQuantity"	        , {type:"TEXT" , size:12, maxLen:12, desc:"採購量"});
	vat.item.make(vnB_vatT7, "periodMovementQuantity"	        , {type:"TEXT" , size:12, maxLen:12, desc:"調撥量"});
	vat.item.make(vnB_vatT7, "adjustQuantity"	        , {type:"TEXT" , size:12, maxLen:12, desc:"調整量"});
	vat.item.make(vnB_vatT7, "periodOtherQuantity"	        , {type:"TEXT" , size:12, maxLen:12, desc:"其他異動量"});
	vat.item.make(vnB_vatT7, "endingOnHandQuantity"	        , {type:"TEXT" , size:12, maxLen:12, desc:"庫存量"});
	vat.item.make(vnB_vatT7, "averageUnitCost"	        , {type:"TEXT" , size:12, maxLen:12, desc:"單位成本"});
	vat.item.make(vnB_vatT7, "endingOnHandAmount"	        , {type:"TEXT" , size:12, maxLen:12, desc:"庫存成本金額"});
	
	//vat.item.make(vnB_vatT7, "itemCode"          ,  {type:"HIDDEN"});
	vat.block.pageLayout(vnB_vatT7, {
														id: "vatT7Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT7+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT7+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_vatT7+")"
														});
	//vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1);
}		

// 第二個頁籤　for t2規格及類別	
function kweImVatT8(){
	var allCategoryTypes = vat.bean("allCategoryTypes"); 
	var standardPurchaseCostIsReadOnly = "";
	var allCurrencyCodes = vat.bean("allCurrencyCodes");
	var selectYorN = [["", true, false], ["否", "是"], ["N", "Y"]];
	standardPurchaseCostIsReadOnly = "ITEM" != userType ? "HIDDEN" : "";
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	var mustWrite = "";
	if(brandCode.indexOf("T2") > -1){
		mustWrite = "<font color='red'>*</font>";
	}
	
	if( brandCode.indexOf("T2") <= -1){
		vat.block.create(vnB_vatT8, {
			id: "vatMaster2Div", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
				rows:[
					{row_style:"", cols:[
						{items:[{name:"#L.categoryType",	type:"LABEL", 	value:"業種" }]},
						{items:[{name:"#F.categoryType",	type:"TEXT", 	bind:"categoryType", mode:"READONLY" }]}, 
					    {items:[{name:"#L.itemCategory",	type:"LABEL", 	value:"業種子類" }]},
						{items:[{name:"#F.itemCategory",	type:"TEXT", 	bind:"itemCategory", mode:"READONLY"}]}, 
						{items:[{name:"#L.budgetType",		type:"LABEL", 	value:"預算類別" }]},
						{items:[{name:"#F.budgetType",		type:"TEXT", 	bind:"budgetType", mode:"READONLY"}]},
						{items:[{name:"#L.purchaseCurrencyCode",	type:"LABEL", 	value:"採購幣別" }]},
						{items:[{name:"#F.purchaseCurrencyCode",	type:"SELECT", 	bind:"purchaseCurrencyCode", mode:"READONLY", init:allCurrencyCodes},
								{name:"#F.isLockPurchaseAmount",	type:"TEXT", 	bind:"isLockPurchaseAmount", back:false, mode:"HIDDEN"}]}
				    ]},
				    {row_style:"", cols:[
						{items:[{name:"#L.lastCurrencyCode",	type:"LABEL", 	value:"最近進貨幣別" }]},
						{items:[{name:"#F.lastCurrencyCode",	type:"TEXT", 	bind:"lastCurrencyCode", mode:"READONLY"}]},
						{items:[{name:"#L.lastPurForeignAmount", type:"LABEL", 	value:"最近進貨原幣金額" }]},
						{items:[{name:"#F.lastPurForeignAmount", type:"NUMB", 	bind:"lastPurForeignAmount", mode:"READONLY"}]},
						{items:[{name:"#L.firstPurchaseDate",	type:"LABEL", 	value:"首次進貨日" }]},
						{items:[{name:"#F.firstPurchaseDate",	type:"DATE", 	bind:"firstPurchaseDate", mode:"READONLY"}]}, 
			 			{items:[{name:"#L.lastPurchaseDate",	type:"LABEL", 	value:"最近進貨日" }]},
						{items:[{name:"#F.lastPurchaseDate",	type:"DATE", 	bind:"lastPurchaseDate", mode:"READONLY"}]}
				    ]},
				    {row_style:"", cols:[
						{items:[{name:"#L.maxPurchaseAmount",	type:"LABEL", 	value:"最高進貨金額" }]},
						{items:[{name:"#F.maxPurchaseAmount",	type:"NUMB", 	bind:"maxPurchaseAmount", mode:"READONLY"}]},
						{items:[{name:"#L.minPurchaseAmount",	type:"LABEL", 	value:"最低進貨金額" }]},
						{items:[{name:"#F.minPurchaseAmount",	type:"NUMB", 	bind:"minPurchaseAmount", mode:"READONLY"}]}, 
			 			{items:[{name:"#L.maxPurchaseQuantity",	type:"LABEL", 	value:"最高採購量" }]},
						{items:[{name:"#F.maxPurchaseQuantity",	type:"NUMB", 	bind:"maxPurchaseQuantity"}]},
						{items:[{name:"#L.minPurchaseQuantity",		type:"LABEL", 	value:"最低採購量" }]},
						{items:[{name:"#F.minPurchaseQuantity",		type:"NUMB", 	bind:"minPurchaseQuantity"}]}
				    ]},
				    {row_style:"", cols:[
						{items:[{name:"#L.lastUnitCost",	type:"LABEL", 	value:"最近單位成本" }]},
						{items:[{name:"#F.lastUnitCost",	type:"TEXT", 	bind:"lastUnitCost",mode:standardPurchaseCostIsReadOnly}]},
						{items:[{name:"#L.purchaseAmount",	type:"LABEL", 	value:"原幣成本" }]},
						{items:[{name:"#F.purchaseAmount",	type:"NUMB", 	bind:"purchaseAmount", mode:"READONLY"}]},
						{items:[{name:"#L.accountCode",		type:"LABEL", 	value:"會計科目代號" }]},
						{items:[{name:"#F.accountCode",		type:"TEXT", 	bind:"accountCode"}]},
						{items:[{name:"#L.isConsignSale",	type:"LABEL", 	value:"寄賣品" }]},
						{items:[{name:"#F.isConsignSale",	type:"SELECT", 	bind:"isConsignSale", init:selectYorN }]}
						
						
				    ]},
				    {row_style:"", cols:[
						{items:[{name:"#L.margen",	type:"LABEL", 	value:"毛利率" }]},
						{items:[{name:"#F.margen",	type:"NUMB", 	bind:"margen"}], td:" colSpan=7"}
				    ]}
					
			 	],
				beginService:"",
				closeService:""			
		});
	}else{	
		var allVipDiscounts = vat.bean("allVipDiscounts");
		var allItemTypes = vat.bean("allItemTypes");
		var allBonusTypes = vat.bean("allBonusTypes");
		var userType = vat.bean().vatBeanOther.userType;
        var vsCostStyle= "ITEM" != userType ? " style= 'display:none;'":""; // 因權限問題
        var allPayOnlines = [["", "", true], ["不可折抵、累計", "可折抵、累計"], ["Y", "N"]];
		vat.block.create(vnB_vatT8, {
			id: "vatMaster2Div", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
				rows:[
					{row_style:"", cols:[
					    {items:[{name:"#L.categoryType",	type:"LABEL", 	value:"業種"+mustWrite }]},
						{items:[{name:"#F.categoryType",	type:"SELECT", 	bind:"categoryType", init:allCategoryTypes, eChange:"changeItemSubcategory()" }]},
					    {items:[{name:"#L.itemCategory",	type:"LABEL", 	value:"業種子類"+mustWrite }]},
						{items:[{name:"#F.itemCategory",	type:"SELECT", 	bind:"itemCategory"},
								{name:"#F.itemCategoryBack",	type:"TEXT", 	bind:"itemCategoryBack", back:false, mode:"HIDDEN" }]},
						{items:[{name:"#L.itemBrand",	type:"LABEL", 	value:"商品品牌"+mustWrite }]},
						{items:[{name:"#F.itemBrand",	type:"TEXT", 	bind:"itemBrand", eChange: function(){ changeCategoryCodeName("ItemBrand"); }},
								{name:"#B.itemBrand",	value:"選取" ,type:"PICKER" ,
			 									 		openMode:"open", src:"./images/start_node_16.gif",
			 									 		service:"Im_Item:searchCategory:20100119.page", 
			 									 		left:0, right:0, width:1024, height:768,
			 									 		servicePassData:function(){ return doPassData("ItemBrand"); },	
			 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("itemBrand");} },
			 					{name:"#F.itemBrandName",	type:"TEXT", 	bind:"itemBrandName", back:false, mode:"READONLY"}]},
						{items:[{name:"#L.purchaseAmount",	type:"LABEL", 	value:"標準進價(原幣)"+mustWrite }]},
						{items:[{name:"#F.purchaseAmount",	type:"NUMB", 	bind:"purchaseAmount", mode:"ITEM" != userType ? "HIDDEN" : ""}]}
						
				    ]},
				    {row_style:"", cols:[
				    	{items:[{name:"#L.purchaseCurrencyCode",	type:"LABEL", 	value:"採購幣別<font color='red'>*</font>" }]},
						{items:[{name:"#F.purchaseCurrencyCode",	type:"SELECT", 	bind:"purchaseCurrencyCode", init:allCurrencyCodes}]},
						{items:[{name:"#L.vipDiscount",		type:"LABEL", 	value:"VIP折扣<font color='red'>*</font>" }]},
						{items:[{name:"#F.vipDiscount",		type:"SELECT", 	bind:"vipDiscount", init:allVipDiscounts}],td:"colSpan=5"}
				    ]},
				    {row_style:"", cols:[
				    	
						{items:[{name:"#L.declRatio",		type:"LABEL", 	value:"銷售單位/報關單號換算" }]}, // <font color='red'>*</font>
						{items:[{name:"#F.declRatio",		type:"NUMB", 	bind:"declRatio"}]},
						{items:[{name:"#L.minRatio",		type:"LABEL", 	value:"最小單位/銷貨單位的比例換算" }]}, // <font color='red'>*</font>
						{items:[{name:"#F.minRatio",		type:"NUMB", 	bind:"minRatio"}]},
						{items:[{name:"#L.itemType",			type:"LABEL", 	value:"商品類別" }]},
						{items:[{name:"#F.itemType",			type:"SELECT", 	bind:"itemType", init:allItemTypes}]},
						{items:[{name:"#L.bonusType",		type:"LABEL", 	value:"獎金類別" }]},
						{items:[{name:"#F.bonusType",		type:"SELECT", 	bind:"bonusType", init:allBonusTypes}]}
				    ]},
					{row_style:"", cols:[
						{items:[{name:"#L.budgetType",	type:"LABEL", 	value:"預算類別" }]},
						{items:[{name:"#F.budgetType",	type:"TEXT", 	bind:"budgetType"}]},
						{items:[{name:"#L.isConsignSale",	type:"LABEL", 	value:"寄賣品" }]},
						{items:[{name:"#F.isConsignSale",	type:"SELECT", 	bind:"isConsignSale", init:selectYorN }]},
						{items:[{name:"#L.replenishCoefficient",	type:"LABEL", 	value:"補貨係數" }]},
						{items:[{name:"#F.replenishCoefficient",	type:"NUMM", size:12, dec:4, 	bind:"replenishCoefficient"}]},
						{items:[{name:"#L.allowMinusStock",			type:"LABEL", 	value:"負庫存" }]}, 
						{items:[{name:"#F.allowMinusStock",			type:"SELECT", 	bind:"allowMinusStock", init:selectYorN}]} // ,td:"colSpan=7"
				    ]},
				    {row_style:vsCostStyle, cols:[
					    {items:[{name:"#L.maxPurchaseQuantity",	type:"LABEL", 	value:"最高採購量" }]},
						{items:[{name:"#F.maxPurchaseQuantity",	type:"NUMB", 	bind:"maxPurchaseQuantity"}]},
						{items:[{name:"#L.minPurchaseQuantity",		type:"LABEL", 	value:"最低採購量" }]},
						{items:[{name:"#F.minPurchaseQuantity",		type:"NUMB", 	bind:"minPurchaseQuantity"}]},
						{items:[{name:"#L.lastCurrencyCode",	type:"LABEL", 	value:"最近進貨幣別" }]},
						{items:[{name:"#F.lastCurrencyCode",	type:"TEXT", 	bind:"lastCurrencyCode", mode:"READONLY"}]},
						{items:[{name:"#L.lastPurForeignAmount", type:"LABEL", 	value:"最近進貨原幣金額" }]},
						{items:[{name:"#F.lastPurForeignAmount", type:"NUMB", 	bind:"lastPurForeignAmount", mode:"READONLY"}]}
				    ]},
				    {row_style:vsCostStyle, cols:[
						{items:[{name:"#L.firstPurchaseDate",	type:"LABEL", 	value:"首次進貨日" }]},
						{items:[{name:"#F.firstPurchaseDate",	type:"DATE", 	bind:"firstPurchaseDate", mode:"READONLY"}]}, 
			 			{items:[{name:"#L.lastPurchaseDate",	type:"LABEL", 	value:"最近進貨日" }]},
						{items:[{name:"#F.lastPurchaseDate",	type:"DATE", 	bind:"lastPurchaseDate", mode:"READONLY"}]},
						{items:[{name:"#L.maxPurchaseAmount",	type:"LABEL", 	value:"最高進貨金額" }]},
						{items:[{name:"#F.maxPurchaseAmount",	type:"NUMB", 	bind:"maxPurchaseAmount", mode:"READONLY"}]},
						{items:[{name:"#L.minPurchaseAmount",	type:"LABEL", 	value:"最低進貨金額" }]},
						{items:[{name:"#F.minPurchaseAmount",	type:"NUMB", 	bind:"minPurchaseAmount", mode:"READONLY"}]}
				    ]},
				    {row_style:"", cols:[
						{items:[{name:"#L.lastUnitCost",	type:"LABEL", 	value:"最近單位成本"  }]},
						{items:[{name:"#F.lastUnitCost",	type:"NUMB", 	bind:"lastUnitCost",mode:standardPurchaseCostIsReadOnly}]},
						{items:[{name:"#L.margen",	type:"LABEL", 	value:"毛利率" }]},
						{items:[{name:"#F.margen",	type:"NUMB", 	bind:"margen"}]},
						{items:[{name:"#L.taxRelativeItemCode ",	type:"LABEL", 	value:"對應稅別品號" }]},
						{items:[{name:"#F.taxRelativeItemCode",	type:"TEXT", 	bind:"taxRelativeItemCode"}]},
						{items:[{name:"#L.accountCode",		type:"LABEL", 	value:"會計科目代號" }]},
						{items:[{name:"#F.accountCode",		type:"TEXT", 	bind:"accountCode"}]}
				    ]},
				    {row_style:"", cols:[
						{items:[{name:"#L.customsItemCode",		type:"LABEL", 	value:"海關品號" }]},
						{items:[{name:"#F.customsItemCode",		type:"TEXT", 	size:16, bind:"customsItemCode", mode:"READONLY"}]},
						{items:[{name:"#L.reserve1",	type:"LABEL", 	value:"備註1" }]},
						{items:[{name:"#F.reserve1",	type:"TEXT", 	bind:"reserve1"}]},
						{items:[{name:"#L.reserve2",	type:"LABEL", 	value:"備註2" }]},
						{items:[{name:"#F.reserve2",	type:"TEXT", 	bind:"reserve2"}]},
						{items:[{name:"#L.reserve3",	type:"LABEL", 	value:"備註3" }]},
						{items:[{name:"#F.reserve3",	type:"TEXT", 	bind:"reserve3"}]}
					]},
				    {row_style:"", cols:[
						{items:[{name:"#L.reserve4",	type:"LABEL", 	value:"備註4" }]},
						{items:[{name:"#F.reserve4",	type:"TEXT", 	bind:"reserve4"},

						//{items:[{name:"#L.reserve5",	type:"LABEL", 	value:"備註5" }]},
						//{items:[{name:"#F.reserve5",	type:"TEXT", 	bind:"reserve5", mode:"HIDDEN"}]},
						{name:"#F.reserve5",	type:"TEXT", 	bind:"reserve5", mode:"HIDDEN"}] },
						{items:[{name:"#L.importLotNo",		type:"LABEL", 	value:"匯入批號" }]},
						{items:[{name:"#F.importLotNo",		type:"TEXT", 	size:16, bind:"importLotNo", mode:"READONLY"}]},
						{items:[{name:"#L.alcolhoPercent",	type:"LABEL", 	value:"酒精濃度" }]},
						{items:[{name:"#F.alcolhoPercent",	type:"NUMM", size:4, dec:2,	bind:"alcolhoPercent"},
								{name:"#L.percent",	type:"LABEL", 	value:"%"}]},
								
						{items:[{name:"#L.payOline",		type:"LABEL", 	value:"電子支付"+mustWrite}]},
						{items:[{name:"#F.payOline",		type:"SELECT", bind:"payOline", init:allPayOnlines}, 
								{name:"#L.payOline2",			type:"LABEL", 	value:"<font color='red'>Y為不可折抵；N為可折抵</font>"	}]}
					]}
					
			 	],
				beginService:"",
				closeService:""			
		});
				 
	}			
}	

// 國際碼	
function kweImVatT9(){
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
  
    vat.item.make(vnB_vatT9, "indexNo"  		, {type:"IDX"  , desc:"序號" });
    vat.item.make(vnB_vatT9, "eanCode"			, {type:"TEXT" , size:23, maxLen:20, desc:"國際碼", eChange:"checkEanCode()"});
	vat.item.make(vnB_vatT9, "enable"			, {type:"CHECKBOX" , size:12, maxLen:12, desc:"啟用"});
	
	vat.item.make(vnB_vatT9, "lineId"          , {type:"HIDDEN"});
//	vat.item.make(vnB_vatT9, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
//  vat.item.make(vnB_vatT9, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_vatT9, "message"         , {type:"MSG"  , desc:"訊息"});
	
	vat.block.pageLayout(vnB_vatT9, {
														id: "vatT9Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT9+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT9+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_vatT9+")"
														});
}	
	
// 進貨明細
function kweImVatT10(){
	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = false;
	var userType = vat.bean().vatBeanOther.userType;
	var hasCost = false;
	var foreignUnitPriceMode;
	 if(userType=="WITHOUTCOST")
	 {
	 	foreignUnitPriceMode = "HIDDEN";
	 }
	 else
	 {
	 	foreignUnitPriceMode = "READONLY";
	 }
	//alert(foreignUnitPriceMode);
	var isHidden = "READONLY";
  
    vat.item.make(vnB_vatT10, "lineIndexNo"  		, {type:"IDX"  , desc:"序號" });
    vat.item.make(vnB_vatT10, "orderTypeCode"	, {type:"TEXT" , size:12, maxLen:12, desc:"單別"});
    vat.item.make(vnB_vatT10, "orderNo"			, {type:"TEXT" , size:12, maxLen:12, desc:"單號"});
	vat.item.make(vnB_vatT10, "receiptDate"		, {type:"TEXT" , size:23, maxLen:20, desc:"驗收日期"});
	vat.item.make(vnB_vatT10, "receiptQuantity"	, {type:"TEXT" , size:23, maxLen:20, desc:"進貨數量"});
	vat.item.make(vnB_vatT10, "foreignUnitPrice", {type:"TEXT" , size:23, maxLen:20, desc:"進貨成本",mode:foreignUnitPriceMode});
//	vat.item.make(vnB_vatT10, "foreignUnitPrice", {type:"TEXT" , size:23, maxLen:20, desc:"進貨成本", mode:function(){ if(isHidden){return "READONLY";}else{return "HIDDEN";}}});
//	vat.item.make(vnB_vatT10, "quantity"		, {type:"TEXT" , size:23, maxLen:20, desc:"退貨數量"});
	
	vat.block.pageLayout(vnB_vatT10, {
														id: "vatT10Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT10+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT10+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_vatT10+")"
														});
}

// 洗標 FOR T1
function kweImVatT11(){
	
	vat.block.create(vnB_vatT11, {

		id: "vatT11Div", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[
			{row_style:"", cols:[
			    {items:[{name:"#L.show01",	type:"LABEL", 	value:"表布1:" },
			    		{name:"#V.show01",	type:"TEXT", 	bind:"show01", mode:"READONLY" }]},
				{items:[{name:"#F.show01Percent",	type:"NUMB", 	bind:"show01Percent", mode:"READONLY" }]}, 
				{items:[{name:"#L.lininging01",	type:"LABEL", 	value:"裡布1:" },
			    		{name:"#V.lininging01",	type:"TEXT", 	bind:"lininging01", mode:"READONLY" }]},
				{items:[{name:"#F.lininging01Percent",	type:"NUMB", 	bind:"lininging01Percent", mode:"READONLY" }]}
		    ]},
		    {row_style:"", cols:[
		    	{items:[{name:"#L.show02",	type:"LABEL", 	value:"表布2:" },
			    		{name:"#V.show02",	type:"TEXT", 	bind:"show02", mode:"READONLY" }]},
				{items:[{name:"#F.show02Percent",	type:"NUMB", 	bind:"show02Percent", mode:"READONLY" }]},
				{items:[{name:"#L.lininging02",	type:"LABEL", 	value:"裡布2:" },
			    		{name:"#V.lininging02",	type:"TEXT", 	bind:"lininging02", mode:"READONLY" }]},
				{items:[{name:"#F.lininging02Percent",	type:"NUMB", 	bind:"lininging02Percent", mode:"READONLY" }]} 
		    ]},
		    {row_style:"", cols:[
		    	{items:[{name:"#L.show03",	type:"LABEL", 	value:"表布3:" },
			    		{name:"#V.show03",	type:"TEXT", 	bind:"show03", mode:"READONLY" }]},
				{items:[{name:"#F.show03Percent",	type:"NUMB", 	bind:"show03Percent", mode:"READONLY" }]},
		    	{items:[{name:"#L.lininging03",	type:"LABEL", 	value:"裡布3:" },
			    		{name:"#V.lininging03",	type:"TEXT", 	bind:"lininging03", mode:"READONLY" }]},
				{items:[{name:"#F.lininging03Percent",	type:"NUMB", 	bind:"lininging03Percent", mode:"READONLY" }]}
		    ]},
		    
		    {row_style:"", cols:[
			    {items:[{name:"#L.washIconPath01",	type:"LABEL", 	value:"圖片路徑1" }]},
				{items:[{name:"#F.washIconPath01",	type:"TEXT", 	bind:"washIconPath01", mode:"READONLY",alter:true, size:40 }]}, 
				{items:[{name:"#L.washIconPath02",	type:"LABEL", 	value:"圖片路徑2" }]},
				{items:[{name:"#F.washIconPath02",	type:"TEXT", 	bind:"washIconPath02", mode:"READONLY",alter:true, size:40 }]}
		    ]},
		    {row_style:"", cols:[
		    	{items:[{name:"#L.washIconPath03",	type:"LABEL", 	value:"圖片路徑3" }]},
				{items:[{name:"#F.washIconPath03",	type:"TEXT", 	bind:"washIconPath03", mode:"READONLY",alter:true, size:40 }]},
				{items:[{name:"#L.washIconPath04",	type:"LABEL", 	value:"圖片路徑4" }]},
				{items:[{name:"#F.washIconPath04",	type:"TEXT", 	bind:"washIconPath04", mode:"READONLY",alter:true, size:40 }]} 
		    ]},
		    {row_style:"", cols:[
		    	{items:[{name:"#L.washIconPath05",	type:"LABEL", 	value:"圖片路徑5" }]},
				{items:[{name:"#F.washIconPath05",	type:"TEXT", 	bind:"washIconPath05", mode:"READONLY",alter:true, size:40 }]},
		    	{items:[{name:"#L.washIconPath06",	type:"LABEL", 	value:"圖片路徑5" }]},
				{items:[{name:"#F.washIconPath06",	type:"TEXT", 	bind:"washIconPath06", mode:"READONLY",alter:true, size:40 }]}
		    ]}      
	 ],
		beginService:"",
		closeService:""			
	});
	
}

	
// 建立新資料按鈕	
function createNewForm(){

    //kweImBlock();
    
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.bean().vatBeanPicker.result = null;  
     	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
    	refreshForm("");
	 }
}

// 刷新頁面
function refreshForm(vsItemId){   
    //alert("resfrshForm:"+vsItemId);/////////////////
    var userType = vat.bean().vatBeanOther.userType;
	document.forms[0]["#formId"            ].value = vsItemId;
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = ""; 
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	initialVaTBeanOther();
	vat.block.submit(
		function(){
				return "process_object_name=imItemAction&process_object_method_name=performInitial"; 
	   	},{ other: true, 
     		funcSuccess:function(){
     	   	vat.item.bindAll();
     	    if(vsItemId!=""){
     	    	vat.block.pageDataLoad(vnB_vatT3, vnCurrentPage = 1);
     	       	vat.block.pageDataLoad(vnB_vatT5, vnCurrentPage = 1);
     	       	vat.block.pageDataLoad(vnB_vatT6, vnCurrentPage = 1);
     	       	
     	       	if("ITEM" == userType){
     	       	  vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1);  // 因權限問題
     	       	  vat.block.pageDataLoad(vnB_vatT10, vnCurrentPage = 1);
     	       	}else if ("ITEMLOCK" == userType){
     	       	  vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1);  // 因權限問題
     	       	  vat.block.pageDataLoad(vnB_vatT10, vnCurrentPage = 1);
     	       	}else if ("MOD" == userType){
     	       	  vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1);  // 因權限問題
     	       	  vat.block.pageDataLoad(vnB_vatT10, vnCurrentPage = 1);
     	       	}
     	       	else if ("WITHOUTCOST" == userType){
     	       	  vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1);  // 因權限問題
     	       	  vat.block.pageDataLoad(vnB_vatT10, vnCurrentPage = 1);
     	       	}
/*     	    	vat.block.pageRefresh(vnB_vatT3);
     	       	vat.block.pageRefresh(vnB_vatT5);
     	       	vat.block.pageRefresh(vnB_vatT6);
     	       	vat.block.pageRefresh(vnB_vatT7);*/
     	    	
     	       	if(isT2()){
     	       		vat.block.pageDataLoad(vnB_vatT9, vnCurrentPage = 1);
//     	       		vat.block.pageRefresh(vnB_vatT9);
     	       	}
     	    }else{
//     	    	vat.tabm.displayToggle(0, "xTab1", false, false, false);	
     	    	vat.tabm.displayToggle(0, "xTab2", false, false, false);	// 標準售價
//     	        vat.tabm.displayToggle(0, "xTab3", false, false, false);
     	        vat.tabm.displayToggle(0, "xTab4", false, false, false);	// 組合商品
     	        vat.tabm.displayToggle(0, "xTab5", false, false, false);	// 庫存資料
     	        vat.tabm.displayToggle(0, "xTab6", false, false, false);	// 月結成本資料
     	        vat.tabm.displayToggle(0, "xTab10", false, false, false);	// 進貨明細紀錄 
//     	        vat.tabm.displayToggle(0, "xTab7", false, false, false);
/*    	    	vat.block.pageDataLoad(vnB_vatT3, vnCurrentPage = 1);
     	       	vat.block.pageDataLoad(vnB_vatT5, vnCurrentPage = 1);
     	       	vat.block.pageDataLoad(vnB_vatT6, vnCurrentPage = 1);
     	       	vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1);*/
     	       	vat.block.pageRefresh(vatMasterDiv);
				vat.block.pageRefresh(vnB_vatT3);
     	       	vat.block.pageRefresh(vnB_vatT5);
     	       	vat.block.pageRefresh(vnB_vatT6);
     	       	vat.block.pageRefresh(vnB_vatT7);  //因權限問題
     	       	vat.block.pageRefresh(vnB_vatT10); //進貨明細
     	        if(isT2()){ 
     	        	vat.tabm.displayToggle(0, "xTab9", false, false, false);	// 國際碼
//     	       		vat.block.pageDataLoad(vnB_vatT9, vnCurrentPage = 1);
					vat.block.pageRefresh(vnB_vatT9);
     	        }
     	        vat.item.setValueByName("#F.lotControl", "N");
     	    }
     	    
     	    doFormAccessControl();
     	  }}
    );
}

function resetForm(){
    refreshForm("");
}	

// picker 檢視回來作用的事件
function doAfterPickerProcess(){
//    alert("after picker process");
	if(vat.bean().vatBeanPicker.result !== null){
		result = vat.bean().vatBeanPicker.result;
	    var vsMaxSize = result.length;
	    if( vsMaxSize === 0){
	  	vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		  
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  vsItemId = result[vat.bean().vatBeanOther.currentRecordNumber -1 ].itemId;
		  refreshForm(vsItemId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
		
	}
}	
	
function doPageDataSave(div){
//	var status = vat.item.getValueByName("#F.status");
//	if(status == "SAVE"){
	var itemId = vat.item.getValueByName("#H.itemId");
	if(itemId != "" ){
		if(div == vnB_vatT3){
			activeTab = vnB_vatT3;
		}else if(div == vnB_vatT5){
			activeTab = vnB_vatT5;
		}else if(div == vnB_vatT9){
			activeTab = vnB_vatT9;
		}
		vat.block.pageSearch(3);
		vat.block.pageSearch(5);
//		vat.block.pageSearch(vnB_vatT9);
	}	
//	}
}
	
	// 第一次載入 重新整理
function loadBeforeAjxService(div){
    //alert("loadBeforeAjxService:"+div);
    //alert(vat.item.getValueByName("#H.itemId"));	
	var processString = "";
		processString = "process_object_name=imItemService&process_object_method_name=getAJAXPageData" + 
		                    "&itemId=" + vat.item.getValueByName("#H.itemId")+
		                    "&tab="+div;
		return processString;				
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
   // alert("saveBeforeAjxService"); 
	var processString = "";
	
	processString = "process_object_name=imItemService&process_object_method_name=updateOrSaveAJAXPageLinesData" + 
			"&itemId=" + vat.item.getValueByName("#H.itemId") + 
			"&itemCode=" + vat.item.getValueByName("#F.itemCode") + 
			"&brandCode=" +  vat.bean().vatBeanOther.loginBrandCode + 
			"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value+
			"&tab="+div;
			
		return processString;
}   

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){ 
	     //alert("saveSuccessAfter");
//	     vat.block.pageRefresh(vnB_Detail);
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
    //alert("loadSuccessAfter");
    vat.block.pageRefresh(0);
} 

// 新增空白頁
function appendBeforeService(){
//    alert("appendBeforeService");	 
	return true;
}    

// 新增空白頁成功後
function appendAfterService(){
//    alert("appendAfterService");	
} 

function eventService(){
} 
	
	
	// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("FINISH" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){
		alertMessage = "是否確定暫存?";
	}else if("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}
	if(confirm(alertMessage)){
	    var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
	    	      
		initialVaTBeanOther();
		vat.bean().vatBeanOther.formAction = formAction;
		vat.bean().vatBeanOther.category06 = vat.item.getValueByName("#F.category06");	   
		vat.block.submit(
			function(){
				return "process_object_name=imItemAction"+
				"&process_object_method_name=performTransaction";
			},{bind:true, link:true, other:true,
				funcSuccess:function(){
					vat.block.pageRefresh(0);
				}
			}
		);
	}
}

function gotoFirst(){
//	alert("vat.bean().vatBeanOther.currentRecordNumber = " + vat.bean().vatBeanOther.currentRecordNumber + "\nvat.bean().vatBeanOther.firstRecordNumber = " + vat.bean().vatBeanOther.firstRecordNumber);
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	        vsItemId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].itemId;
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm(vsItemId);
	    }else{
	  	    alert("目前已在第一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoForward(){
    if(vat.bean().vatBeanOther.firstRecordNumber > 0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	    vsItemId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].itemId;
	  	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	    refreshForm(vsItemId);
	    }else{
	  	    alert("目前已在第一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoNext(){	
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	    vsItemId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].itemId;
	  	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	    refreshForm(vsItemId);
	    }else{
	  	   alert("目前已在最後一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoLast(){
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	        vsItemId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].itemId;
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm(vsItemId);
	    }else{
	  	    alert("目前已在最後一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
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

function showMessage(){
	var width = "600";
    var height = "400";  
	window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=CM_DECLARATION" +
		"&levelType=ERROR" +
        "&processObjectName=cmDeclarationHeadService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#H.itemId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}	

function showComposeItemData(){
	var vLineId 		= vat.item.getGridLine();
	//alert(vLineId);/////////////////////
	var composeItemCode		= vat.item.getGridValueByName("composeItemCode", vLineId);
	//alert("composeItemCode:"+composeItemCode);//////////////////
	var brandCode 		= vat.bean().vatBeanOther.loginBrandCode;
	
	//var itemCode		= vat.item.getGridValueByName("itemCode", vLineId);
	//var lotNo			= vat.item.getGridValueByName("lotNo", vLineId);
	//var difQuantity		= vat.item.getGridValueByName("difQuantity", vLineId);
	//var localUnitCost	= vat.item.getGridValueByName("localUnitCost", vLineId);
	
	vat.ajax.XHRequest({
	           post:"process_object_name=imItemService"+
	                    "&process_object_method_name=getAJAXComposeItemData"+
	                    "&composeItemCode=" + composeItemCode +
	                    "&brandCode=" + brandCode,
	           find: function change(oXHR){ 
	           		//vat.item.setGridValueByName("itemCode", vLineId, vat.ajax.getValue("itemCode", oXHR.responseText));
	           		vat.item.setGridValueByName("composeItemName", vLineId, vat.ajax.getValue("composeItemName", oXHR.responseText));
	           		//vat.item.setGridValueByName("composeItemUnit", vLineId, vat.ajax.getValue("composeItemUnit", oXHR.responseText));
	           		//vat.item.setGridValueByName("quantity", vLineId, vat.ajax.getValue("quantity", oXHR.responseText));
	           		//vat.item.setGridValueByName("remark", vLineId, vat.ajax.getValue("remark", oXHR.responseText));
	           		//vat.item.setGridValueByName("reserve1", vLineId, vat.ajax.getValue("reserve1", oXHR.responseText));
	           		//vat.item.setGridValueByName("localUnitCost", vLineId, vat.ajax.getValue("localUnitCost", oXHR.responseText));
	           		vat.item.setGridValueByName("purchaseCurrencyCode", vLineId, vat.ajax.getValue("purchaseCurrencyCode", oXHR.responseText));
	           		vat.item.setGridValueByName("itemPrice", vLineId, vat.ajax.getValue("itemPrice", oXHR.responseText));
	           		vat.item.setGridValueByName("itemCost", vLineId, vat.ajax.getValue("itemCost", oXHR.responseText));
	           		
	           		checkComposeQuantity(vLineId);
	           },
	           fail: function changeError(){
	          		vat.item.setGridValueByName("composeItemCode", vLineId, "");
	          		vat.item.setGridValueByName("composeItemName", vLineId, "");
	           		//vat.item.setGridValueByName("composeItemUnit", vLineId, "");
	           		vat.item.setGridValueByName("quantity", vLineId, "");
	           		vat.item.setGridValueByName("remark", vLineId, "");
	           		vat.item.setGridValueByName("reserve1", vLineId, "");
	          		//vat.item.setGridValueByName("itemCName", vLineId, "");
	          		//vat.item.setGridValueByName("lotNo", vLineId, "");
	          		//vat.item.setGridValueByName("localUnitCost", vLineId, "-1");
	           }   
	});
}


function checkComposeQuantity(lindId){
	var vLineId 		= lindId == null ? vat.item.getGridLine() : lindId;
	var quantity		= vat.item.getGridValueByName("quantity", vLineId);
	var composeItemName	= vat.item.getGridValueByName("composeItemName", vLineId);
	var patrn=/^[0-9]*[1-9][0-9]*$/;
	
    if (patrn.exec(quantity)){
        if(composeItemName!=""){
          vat.item.setGridValueByName("reserve1", vLineId, "Okay");
        }else{
          vat.item.setGridValueByName("reserve1", vLineId, "請輸入品號");
        }
	}else{
	      vat.item.setGridValueByName("reserve1", vLineId, "請輸入數量");
	}
}

// 欄位鎖定
function doFormAccessControl(){

	changeItemSubcategory("refreshForm");

	var userType = vat.bean().vatBeanOther.userType;
	var formId = document.forms[0]["#formId"].value;
	
	var branch =  vat.bean("branch");
  	// 初始化
 	vat.item.setAttributeByName("#F.itemCode", "readOnly", false);
	vat.item.setAttributeByName("#F.salesRatio", "readOnly", false);
	vat.item.setAttributeByName("#F.salesUnit", "readOnly", false);
	vat.item.setAttributeByName("#F.purchaseUnit", "readOnly", false);
		
	vat.item.setGridAttributeByName("unitPrice", "readOnly", false);
	
	// 若訂過價鎖欄位
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	var amount = "#F.purchaseAmount";
	if(brandCode.indexOf("T2") <= -1){
		amount = "#F.standardPurchaseCost";
	}
	var isLockPurchaseAmount = vat.item.getValueByName("#F.isLockPurchaseAmount");
	
	if("true" == isLockPurchaseAmount){
		vat.item.setAttributeByName(amount, "readOnly", true);
	}else{
		vat.item.setAttributeByName(amount, "readOnly", false);
	}
	
//	vat.block.canGridModify([vnB_vatT6], false,false,false);
	
  if(formId==""){
       //alert("access control");
       //vat.item.setAttributeByName("vatT3Div"  , "readOnly", true);
       //vat.item.setAttributeByName("vatT5Div"  , "readOnly", true);
   }else if("ITEMLOCK" == userType){
   		//商管因控管修改主檔權限需求
		
		vat.tabm.displayToggle(0, "xTab2", true, false, false); // 標準售價
     	//vat.tabm.displayToggle(0, "xTab3", true);
     	vat.tabm.displayToggle(0, "xTab5", true, false, false);	// 庫存資料
     	vat.tabm.displayToggle(0, "xTab6", "ITEMLOCK" != userType ? false : true, false, false); // 月結成本資料 因權限問題
      	vat.tabm.displayToggle(0, "xTab9", true, false, false); // 國際碼
      	vat.tabm.displayToggle(0, "xTab10", "ITEMLOCK" != userType ? false : true, false, false); // 月結成本資料 因權限問題
      	
      	vat.item.setAttributeByName("vatMasterDiv"  , "readOnly", true);
      	vat.item.setAttributeByName("vatMaster2Div"  , "readOnly", true);
      	
      	vat.item.setAttributeByName("#F.itemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.salesRatio", "readOnly", true);
		vat.item.setAttributeByName("#F.salesUnit", "readOnly", true);
		vat.item.setAttributeByName("#F.purchaseUnit", "readOnly", true);
		vat.item.setAttributeByName("#F.itemCName", "readOnly", true);
		vat.item.setAttributeByName("#F.isComposeItem", "readOnly", true);
		vat.item.setAttributeByName("#F.enable", "readOnly", true);
		
		vat.item.setGridAttributeByName("unitPrice", "readOnly", true);
		
      	//vat.tabm.displayToggle(0, "xTab7", true);
       	//vat.item.setAttributeByName("vatT3Div"  , "readOnly", false);
       	//vat.item.setAttributeByName("typeCode", "readOnly", false);
       	//vat.item.setAttributeByName("unitPrice", "readOnly", false);
       	//vat.item.setAttributeByName("isTax", "readOnly", false);
       
       	var isComposeItem = vat.item.getValueByName("#F.isComposeItem");
       	if(isComposeItem=="Y"){
      	 	vat.tabm.displayToggle(0, "xTab4", true, false, false);	// 組合商品
      	 	// 如果是組合商品，提供明細匯入的按鈕
      	 	vat.item.setStyleByName("#B.import", "display", "inline");
         	//vat.item.setAttributeByName("vatT5Div"  , "readOnly", false);
         	//vat.item.setAttributeByName("composeItemCode", "readOnly", false);
         	//vat.item.setAttributeByName("quantity", "readOnly", false);
         	//vat.item.setAttributeByName("remark", "readOnly", false);
      	}else{
       		vat.tabm.displayToggle(0, "xTab4", false, false, false); // 組合商品
       		vat.item.setStyleByName("#B.import", "display", "none");
        	// vat.item.setAttributeByName("vatT5Div"  , "readOnly", true);
      	}
       
       // 鎖標準售價單價
       
       	if( "false" == vat.item.getValueByName("#F.enablePrice")){
       		vat.item.setGridAttributeByName("unitPrice", "readOnly", false);
       	} else{
			vat.item.setGridAttributeByName("unitPrice", "readOnly", true);    		
       	}
        
        //======================<header>=============================================
		vat.item.setAttributeByName("#F.itemCode", "readOnly", true);
		//vat.item.setAttributeByName("#F.salesRatio", "readOnly", true);
		//alert("userType"+userType);
		// 反查
	var isOppositePicker = vat.bean().vatBeanOther.isOppositePicker;
   	if("true" == isOppositePicker){
		vat.block.pageDataLoad(vnB_vatT3, vnCurrentPage = 1);
   		vat.block.pageDataLoad(vnB_vatT5, vnCurrentPage = 1);
     	vat.block.pageDataLoad(vnB_vatT6, vnCurrentPage = 1);
     	vat.item.setAttributeByName("vatMasterDiv"  , "readOnly", true);
      	vat.item.setAttributeByName("vatMaster2Div"  , "readOnly", true);
     	if("ITEMLOCK" == userType){
     		vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1); // 因權限問題
     		vat.block.pageDataLoad(vnB_vatT10, vnCurrentPage = 1); // 進貨明細
     	}
	 	if(isT2()){
     	  	vat.block.pageDataLoad(vnB_vatT9, vnCurrentPage = 1);
     	}
   	}
   }else{// USERTYPE=MOD
   //alert("ITEM"+userType)
   		vat.tabm.displayToggle(0, "xTab2", true, false, false); // 標準售價
     	//vat.tabm.displayToggle(0, "xTab3", true);
     	vat.tabm.displayToggle(0, "xTab5", true, false, false);	// 庫存資料
     	vat.tabm.displayToggle(0, "xTab6", "MOD" != userType ? false : true, false, false); // 月結成本資料 因權限問題
      	vat.tabm.displayToggle(0, "xTab9", true, false, false); // 國際碼
      	vat.tabm.displayToggle(0, "xTab10", "MOD" != userType ? false : true, false, false); // 月結成本資料 因權限問題
      	if("ITEM"==userType){
      	vat.tabm.displayToggle(0, "xTab6", "ITEM" != userType ? false : true, false, false); // 月結成本資料 因權限問題
      	vat.tabm.displayToggle(0, "xTab10", "ITEM" != userType ? false : true, false, false); // 月結成本資料 因權限問題
      	}
      	if("WITHOUTCOST"==userType){
      	vat.tabm.displayToggle(0, "xTab10", "WITHOUTCOST" != userType ? false : true, false, false); // 月結成本資料 因權限問題
      	vat.item.setStyleByName("#F.lastUnitCost", "display", "none");
      	}
      	
      	vat.item.setAttributeByName("#F.itemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.salesRatio", "readOnly", false);
		vat.item.setAttributeByName("#F.salesUnit", "readOnly", false);
		vat.item.setAttributeByName("#F.purchaseUnit", "readOnly", false);
		
		if(branch == "1")
			vat.item.setAttributeByName("#F.supplierQuotationPrice", "readOnly", true);
		
		vat.item.setGridAttributeByName("unitPrice", "readOnly", false);
		
      	//vat.tabm.displayToggle(0, "xTab7", true);
       	//vat.item.setAttributeByName("vatT3Div"  , "readOnly", false);
       	//vat.item.setAttributeByName("typeCode", "readOnly", false);
       	//vat.item.setAttributeByName("unitPrice", "readOnly", false);
       	//vat.item.setAttributeByName("isTax", "readOnly", false);
       
       	var isComposeItem = vat.item.getValueByName("#F.isComposeItem"); 
       	if(isComposeItem=="Y"){
      	 	vat.tabm.displayToggle(0, "xTab4", true, false, false);	// 組合商品
      	 	// 如果是組合商品，提供明細匯入的按鈕
      	 	vat.item.setStyleByName("#B.import", "display", "inline");
         	//vat.item.setAttributeByName("vatT5Div"  , "readOnly", false);
         	//vat.item.setAttributeByName("composeItemCode", "readOnly", false);
         	//vat.item.setAttributeByName("quantity", "readOnly", false);
         	//vat.item.setAttributeByName("remark", "readOnly", false);
      	}else{
       		vat.tabm.displayToggle(0, "xTab4", false, false, false); // 組合商品
       		vat.item.setStyleByName("#B.import", "display", "none");
        	// vat.item.setAttributeByName("vatT5Div"  , "readOnly", true);
      	}
       
       // 鎖標準售價單價
       
       	if( "false" == vat.item.getValueByName("#F.enablePrice")){
       		vat.item.setGridAttributeByName("unitPrice", "readOnly", false);
       	} else{
			vat.item.setGridAttributeByName("unitPrice", "readOnly", true);    		
       	}
        
        //======================<header>=============================================
		vat.item.setAttributeByName("#F.itemCode", "readOnly", true);
		//vat.item.setAttributeByName("#F.salesRatio", "readOnly", true);
		
	// 反查
	var isOppositePicker = vat.bean().vatBeanOther.isOppositePicker;
   	if("true" == isOppositePicker){
   //	alert("isOppositePicker")
		vat.block.pageDataLoad(vnB_vatT3, vnCurrentPage = 1);
   		vat.block.pageDataLoad(vnB_vatT5, vnCurrentPage = 1);
     	vat.block.pageDataLoad(vnB_vatT6, vnCurrentPage = 1);
     	if("ITEM" == userType){
     	//alert("userType2"+userType)
     		vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1); // 因權限問題
     		vat.block.pageDataLoad(vnB_vatT10, vnCurrentPage = 1); // 進貨明細
     	}
     	if("WITHOUTCOST" == userType){
     	//alert("userType2"+userType)
     		vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1); // 因權限問題
     		vat.block.pageDataLoad(vnB_vatT10, vnCurrentPage = 1); // 進貨明細
     	}
	 	if(isT2()){
     	  	vat.block.pageDataLoad(vnB_vatT9, vnCurrentPage = 1);
     	}
   	}
   	}
   	if(vat.item.getValueByName("#F.department")  ==='150'){
		vat.item.setStyleByName("#B.submit","display", "none");
	}
}


// 送出返回呼叫的
function showLine(itemId){
               //refreshForm(itemId);
               var userType = vat.bean().vatBeanOther.userType;
               document.forms[0]["#formId"            ].value = itemId;
     	       vat.bean().vatBeanOther.formId = itemId;
     	       vat.item.setValueByName("#H.itemId",itemId);
     	       
     	       vat.tabm.displayToggle(0, "xTab2", true, false, false); // 標準售價
                var isComposeItem = vat.item.getValueByName("#F.isComposeItem");
                if(isComposeItem=="Y"){
                    vat.tabm.displayToggle(0, "xTab4", true, false, false);	// 組合商品
                }else{
                    vat.tabm.displayToggle(0, "xTab4", false, false, false); 	
                }
               
     	       //vat.tabm.displayToggle(0, "xTab3", true);
     	      
     	       vat.tabm.displayToggle(0, "xTab5", true, false, false);	// 庫存資料
     	       vat.tabm.displayToggle(0, "xTab6",  "ITEM" != userType ? false : true, false, false);	// 月結成本資料 因權限問題
     	       vat.block.pageDataLoad(vnB_vatT6, vnCurrentPage = 1); 	// 庫存資料
     	       if( "ITEM" == userType ){
     	      
     	         vat.block.pageDataLoad(vnB_vatT7, vnCurrentPage = 1); 	// 月結成本資料 因權限問題
     	         
     	         vat.tabm.displayToggle(0, "xTab10", true, false, false);
     	         vat.block.pageDataLoad(vnB_vatT10, vnCurrentPage = 1); 	// 進貨本資料 因權限問題
     	       }
     	       
			    if(isT2()){
					vat.tabm.displayToggle(0, "xTab9", true, false, false);	// 國際碼
					vat.block.pageDataLoad(vnB_vatT9, vnCurrentPage = 1); 
				}
				
     	       ////vat.tabm.displayToggle(0, "xTab7", true);
     	       //vat.block.pageDataLoad(vnB_vatT3, vnCurrentPage = 1); 
     	       //vat.block.pageDataLoad(vnB_vatT5, vnCurrentPage = 1); 
     	       //vat.block.pageDataLoad(vnB_vatT6, vnCurrentPage = 1); 
}

function showComposeItem(){
       doFormAccessControl();
}

// 若輸入完一筆,鎖單價欄位
function changeUnitPriceReadOnly(){
	var vLineId		= vat.item.getGridLine();
	var unitPrice 	= vat.item.getGridValueByName("unitPrice", vLineId);
	
	// 檢查價錢不得有小數點
	if(unitPrice != ""){
		var position = unitPrice.indexOf(".") + 1;
		var dot = parseFloat("0."+unitPrice.substring(position));
		if(unitPrice.indexOf(".") > -1 && dot > 0 ){
			alert("售價:"+unitPrice+" 不得含有小數點");
			vat.item.setGridValueByName("unitPrice", vLineId,"");
		}else{
			vat.item.setGridAttributeByName("unitPrice", "readOnly", true);
		}
	}
}


function checkEanCode(){
	var vLineId		= vat.item.getGridLine();
	var eanCode 	= vat.item.getGridValueByName("eanCode", vLineId);
	
	alert('eanCode =' + eanCode);
	
	if(eanCode.length < 13){
		alert('國際碼' + eanCode + '不可以小於13位數');
	}
}

// 商品品號
function changeItemCode(){
	var ItemCode = vat.item.getValueByName("#F.itemCode").replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setValueByName("#F.itemCode" , ItemCode);
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	if( brandCode.indexOf("T2") > -1 ){
		vat.item.setValueByName("#F.customsItemCode" , ItemCode);
	}
	var pattern = new RegExp("[^a-zA-Z\\d\\s]+"); 
//	alert(ItemCode+"\n"+!pattern.test(ItemCode));
	 
	if( pattern.test(ItemCode) == true ){
    	alert("品號不可有特殊符號，請重新輸入!");
   	}else if(ItemCode.indexOf(" ") > -1 ){
   		alert("品號不可含有空白，請重新輸入!");
   	}else if(ItemCode.length > 13 ){
   		alert("品號不可大於13碼");
   	}else{
		var processString = "process_sql_code=FindItemByItemCode&"+
	   						"itemCode=" + ItemCode + "&"+
	   						"brandCode=" + brandCode;
		vat.ajax.startRequest(processString,  function() { 
		  if (vat.ajax.handleState()){
		    if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
		    	alert("品號已存在，請重新輸入!");
		    }
		  }
		} );
	}

}

// 動態改變業種子類
function changeItemSubcategory(key){ 
	var categoryType = vat.item.getValueByName("#F.categoryType");
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	
	vat.ajax.XHRequest({ 
			post:"process_object_name=imItemCategoryService"+
            		"&process_object_method_name=getAJAXCategory"+
            		"&category="+ categoryType +   // 
                	"&categoryType=ITEM_CATEGORY" +
                	"&brandCode=" + brandCode,                      
			find: function changeRequestSuccess(oXHR){
				// 重新設定 哪一列的下拉
				var allCategory = eval( vat.ajax.getValue("allCategory", oXHR.responseText));
//				alert("allCategory = " + allCategory);
           		allCategory[0][0] = "#F.itemCategory";
           		
           		if(key =="refreshForm"){
 //         			alert("\n" + vat.item.getValueByName("#F.itemCategoryBack"));
					allCategory[0][1] =  vat.item.getValueByName("#F.itemCategoryBack");
				}
 //          		alert( "vsName =" + vsName + "\nallItemSubcategory = " + allItemSubcategory);
				vat.item.SelectBind(allCategory); 
           }
        });
}

// 取得指定連動的類別下拉
function changeCategory(parentCategoryType, toggleCategoryType){
	var parentCategory = vat.item.getValueByName("#F.category"+parentCategoryType);
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
 
    vat.ajax.XHRequest(
    {
        post:"process_object_name=imItemCategoryService"+
                 "&process_object_method_name=getAJAXCategory"+
                 "&category=" + parentCategory +
                 "&categoryType=CATEGORY" + toggleCategoryType +
                 "&brandCode=" + brandCode,
        find: function changeRequestSuccess(oXHR){ 
	var allCategory = eval(vat.ajax.getValue("allCategory", oXHR.responseText));
        		allCategory[0][0] = "#F.category"+toggleCategoryType;
	vat.item.SelectBind(allCategory); 
        }   
    });
}

// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "serialPicker":
			suffix += "&formId="+escape(vat.item.getValueByName("#H.itemId"))+"&itemPicker=true"; 
			break;
		case "CATEGORY13":
			suffix += "&categoryType="+escape("CATEGORY13");
			break;
		case "ItemBrand":
			suffix += "&categoryType="+escape("ItemBrand");
			break;
	}
//	alert(suffix);
	return suffix;
}

// 選取picker前的click
function doBeforePick(id){
	switch(id){
		case "serialPicker":
			var itemId = vat.item.getValueByName("#H.itemId");
			if(itemId == "" || itemId == "0"){
				return false;
			}
			break;
	}
	return true;
}

// picker 回來執行
function doAfterPickerFunctionProcess(key){
	//do picker back something
	
	switch(key){
		case "serialPicker":
			if(typeof vat.bean().vatBeanPicker.serialResult != "undefined"){
		    	vat.item.setValueByName("#F.lineId", vat.bean().vatBeanPicker.serialResult[0].lineId); 
				changeSerial("lineId");
			}
		break;
		case "category17":
			if(typeof vat.bean().vatBeanPicker.result != "undefined"){
		    	vat.item.setValueByName("#F.addressBookId", vat.bean().vatBeanPicker.result[0].addressBookId); 
				changeSupplierName("addressBookId");
			}
		break;
		case "category13":
			if(typeof vat.bean().vatBeanPicker.categoryResult != "undefined"){
		    	vat.item.setValueByName("#F.category13", vat.bean().vatBeanPicker.categoryResult[0]["id.categoryCode"]); 
				changeCategoryCodeName("CATEGORY13");
			}
		break;
		case "itemBrand":
			if(typeof vat.bean().vatBeanPicker.categoryResult != "undefined"){
		    	vat.item.setValueByName("#F.itemBrand", vat.bean().vatBeanPicker.categoryResult[0]["id.categoryCode"]); 
				changeCategoryCodeName("ItemBrand");

			}
		break;
		
	}
	
}

// 動態改變序號
function changeSerial(code){
	vat.ajax.XHRequest({
		post:"process_object_name=imItemService"+
                  "&process_object_method_name=getAJAXSerial"+
                  "&itemId=" + vat.item.getValueByName("#F.itemId") + 
                  "&lineId=" + ( "lineId" === code ? vat.item.getValueByName("#F.lineId") : "" ) +
                  "&serial=" + ( "serial" === code ? vat.item.getValueByName("#F.serial") : "" ),
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName("#F.serial", vat.ajax.getValue("serial", oXHR.responseText));
         		vat.item.setValueByName("#F.serialMemo", vat.ajax.getValue("serialMemo", oXHR.responseText) );
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.serialMemo", "查無此序號");
		}   
	});	
}

// 動態改變供應商名稱
function changeSupplierName( code ){
//	alert( code + "\n" + vat.item.getValueByName("#F.addressBookId") +"\n" + vat.item.getValueByName("#F.supplierCode") );

	vat.ajax.XHRequest({
		post:"process_object_name=buSupplierWithAddressViewService"+
                  "&process_object_method_name=getAJAXSupplierName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&addressBookId=" + ( "addressBookId" === code ? vat.item.getValueByName("#F.addressBookId") : "" )+
                  "&supplierCode=" + ( "supplierCode" === code ? vat.item.getValueByName("#F.category17") : "" ),
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName("#F.category17", vat.ajax.getValue("supplierCode", oXHR.responseText));
         		vat.item.setValueByName("#F.supplierName", vat.ajax.getValue("supplierName", oXHR.responseText) );
         		vat.item.setValueByName("#F.purchaseCurrencyCode", vat.ajax.getValue("currencyCode", oXHR.responseText) );
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.supplierName", "查無此供應商");
		}   
	});	
}

// 動態改變商品類別名稱
function changeCategoryCodeName(code){
	var condition = "", name ="";
	
	switch(code){
		case "CATEGORY13":
			condition =  "&categoryCode=" + ( "CATEGORY13" === code ? vat.item.getValueByName("#F.category13") : "" );
			name = "#F.category13Name";
		break;
		case "ItemBrand":
			condition =  "&categoryCode=" + ( "ItemBrand" === code ? vat.item.getValueByName("#F.itemBrand") : "" );
			name = "#F.itemBrandName";
		break;
	}
	
	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXCategoryName"+
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

//組合商品明細匯入
function importFormData(){
	
    var beanName = "IMPORT_COMPOSE_ITEMS";
    var suffix ="";
	suffix =
		"&importBeanName="+ beanName +
		"&importFileType=XLS" +
        "&processObjectName=imItemService" +
        "&processObjectMethodName=executeImportComposeItems" +
        "&arguments=" + vat.item.getValueByName("#H.itemId")  +
        "&parameterTypes=LONG" +
        "&blockId=" + vnB_vatT5;
	return suffix;
	

}