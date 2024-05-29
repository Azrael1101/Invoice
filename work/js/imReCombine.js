vat.debug.enable();
var afterSavePageProcess = "";
var afterSaveShopPageProcess = "";
var afterSaveCustomerPageProcess = "";
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;
var vnB_Shop_All = 3;
var vnB_Shop_Detail = 4;
var vnB_Customer_All = 5;
var vnB_Customer_Detail = 6;
var vnB_AmountDiv   = 7;
var vnB_ApprovalDiv = 8;

function outlineBlock(){
 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#loginBrandCode"].value != "[binding]"){
	vat.bean().vatBeanOther =
    {
       loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
 	   loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,
 	   orderTypeCode      : document.forms[0]["#orderTypeCode"].value,
       formId             : document.forms[0]["#formId"].value
     };
     vat.bean.init(function(){
	 return "process_object_name=imPromotionReCombineMainAction&process_object_method_name=performInitial"; 
       },{other: true});
  }
  
  //formDataInitial();
  buttonLine();
  headerInitial();
  if (typeof vat.tabm != 'undefined') {
      vat.tabm.createTab(0, "vatTabSpan", "H", "float");      
      vat.tabm.createButton(0, "xTab1","商品資料"    ,"vatItemDiv"              ,"images/tab_item_data_dark.gif"            ,"images/tab_item_data_light.gif", false);
      vat.tabm.createButton(0, "xTab2","參與專櫃"    ,"vatShopDiv"              ,"images/tab_shop_data_dark.gif"            ,"images/tab_shop_data_light.gif", "none");
      vat.tabm.createButton(0, "xTab3","客戶類別"    ,"vatCustomerDiv"          ,"images/tab_customer_type_dark.gif"        ,"images/tab_customer_type_light.gif", "none");
      vat.tabm.createButton(0, "xTab4","金額統計"    ,"vatAmountDiv"            ,"images/tab_total_amount_dark.gif"         ,"images/tab_total_amount_light.gif", "none" , "showTotalCountPage()" );
      vat.tabm.createButton(0, "xTab5","簽核資料"    ,"vatApprovalDiv"          ,"images/tab_approval_data_dark.gif"        ,"images/tab_approval_data_light.gif", false);
  } 

  detailInitial();
  vat.tabm.createDivision("vatShopDiv");
  allShopInitial();
  shopInitial();
  document.write("</div>");
  vat.tabm.createDivision("vatCustomerDiv");
  allCustomerInitial();
  customerInitial();
  document.write("</div>");
  amountInitial();
  kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
	           vat.item.getValueByName("#F.orderTypeCode"), 
	           vat.item.getValueByName("#F.orderNo"),
	           document.forms[0]["#loginEmployeeCode"].value );
}

function formDataInitial(){
    if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#loginBrandCode"].value != "[binding]"){               
        vat.item.SelectBind(vat.bean("allOrderTypes"),{ itemName : "#F.orderTypeCode" });
        vat.item.SelectBind(vat.bean("allPriceTypes"),{ itemName : "#F.priceType" });
        vat.item.SelectBind(vat.bean("allCustomerTypes"),{ itemName : "#F.customerType" });      
        vat.item.SelectBind(vat.bean("allItemCategory"),{ itemName : "#F.itemCategory" });
        vat.item.SelectBind(vat.bean("allPurchaseAssist"),{ itemName : "#F.purchaseAssist" });
        vat.item.SelectBind(vat.bean("allPurchaseMember"),{ itemName : "#F.purchaseMember" });
        vat.item.SelectBind(vat.bean("allPurchaseMaster"),{ itemName : "#F.purchaseMaster" });
        
        vat.item.bindAll();      
	  	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	    
        vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
        vat.block.pageDataLoad(vnB_Shop_Detail, vnCurrentPage = 1);
        vat.block.pageDataLoad(vnB_Customer_Detail, vnCurrentPage = 1);
        refreshWfParameter(vat.item.getValueByName("#F.brandCode"), vat.item.getValueByName("#F.orderTypeCode"),  vat.item.getValueByName("#F.orderNo"));	 
        vat.tabm.displayToggle(0, "xTab5",  vat.item.getValueByName("#F.status") != "SAVE", false, false);
        //vat.tabm.displayToggle(0, "xTab1", true);
	    doFormAccessControl();
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
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_ReCombine:searchMod:20141226.page",
	 									 servicePassData:function(){ return doPassHeadData(); },
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmitHandler()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSaveHandler()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doVoidHandler()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},	 			
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:"openReportWindow()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export"   , type:"IMG" ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'doExport()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import"  , type:"IMG" ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'doImport()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},	 			
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmitBgHandler()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"}, 			
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示"   ,   src:"./images/button_message_prompt.gif"  , eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.importCombine",  type:"PICKER", value:"取組合單資料", src:"./images/button_getCombine.jpg",
	 									  openMode:"open", 
	 									  service:"Im_ReCombine:search:20141226.page", left:0, right:0, width:1024, height:768,	
										  servicePassData:function()
										  	{return "&orderTypeCode="+document.forms[0]["#orderTypeCode"].value+
										  			"&brandCode="    +vat.item.getValueByName("#F.brandCode")+
										  			"&headId="       +vat.item.getValueByName("#F.headId")+
										  			"&loginBrandCode="+document.forms[0]["#loginBrandCode"].value+
										  			"&loginEmployeeCode="+document.forms[0]["#loginEmployeeCode"].value+
										  			"&formName=GetCombine"},
	 									  serviceBeforePick:function(){doPickerWithPromotionCombine()},
	 									  serviceAfterPick:function(){doPickerWithPromotionCombine()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},	 			
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

function headerInitial(){

var allEnable =        [["", true, false], ["啟用", "停用"], ["Y", "N"]];

vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"組合促銷活動維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderType", type:"LABEL" , value:"單別<font color='red'>*</font>"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", back:false, mode:"READONLY"}]},		 
	 {items:[{name:"#L.orderNo"  , type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.orderNo"  , type:"TEXT"  ,  bind:"orderNo", back:false, size:20, mode:"READONLY"},
	 		 {name:"#F.headId"   , type:"TEXT"  ,  bind:"headId", back:false, size:10, mode:"READONLY"}]},
	 {items:[{name:"#L.priceType", type:"LABEL",  value:"價格類型"}]},
	 {items:[{name:"#F.priceType", type:"SELECT",  bind:"priceType", size:1, mode:"READONLY"}]},			 
	 {items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},
	 {items:[{name:"#F.brandCode", type:"TEXT"  ,  bind:"brandCode", size:8, mode:"HIDDEN"},
	         {name:"#F.brandName", type:"TEXT"  ,  bind:"brandName", size:12, mode:"READONLY"}]}, 
	 {items:[{name:"#L.status"   , type:"LABEL" , value:"狀態"}]},	 		 
	 {items:[{name:"#F.status"   , type:"TEXT"  ,  bind:"status", size:12, mode:"HIDDEN"},
	         {name:"#F.statusName"   , type:"TEXT"  ,  bind:"statusName", size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.promotionCode", type:"LABEL", value:"活動代號"}]},
	 {items:[{name:"#F.promotionCode"  , type:"TEXT" , bind:"promotionCode", size:12, maxLen:10}]},
	 {items:[{name:"#L.promotionName", type:"LABEL", value:"活動名稱<font color='red'>*</font>"}]},
	 {items:[{name:"#F.promotionName"  , type:"TEXT" , bind:"promotionName", size:60, maxLen:50, alter:true}], td:" colSpan=3"},
	 {items:[{name:"#L.createdByName", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.createdByName" , type:"TEXT",   bind:"createdByName",  mode:"READONLY", size:12}]},	 
	 {items:[{name:"#L.creationDate" , type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.creationDate" , type:"TEXT",   bind:"creationDate", back:false, mode:"READONLY", size:12}]}]},
 	 {row_style:"", cols:[
	 {items:[{name:"#L.promotionNo", type:"LABEL", value:"分析代號"}]},
	 {items:[{name:"#F.promotionNo"  , type:"TEXT" , bind:"promotionNo", size:20, maxLen:20}]},
	 {items:[{name:"#L.inCharge", type:"LABEL", value:"負責人員"}]},
	 {items:[{name:"#F.inCharge", type:"TEXT" ,  bind:"inCharge", size:10, maxLen:10, onchange:"changeSuperintendent()"},
	         {name:"#F.inChargeName", type:"TEXT"  ,  bind:"inChargeName", size:12, mode:"READONLY"}]},	 
	 {items:[{name:"#L.description", type:"LABEL", value:"說明"}]},
	 {items:[{name:"#F.description" , type:"TEXT",   bind:"description", size:50, maxLen:200}], td:" colSpan=5"}]},
	 
	 {row_style:"", cols:[
	        {items:[{name:"#L.itemCategory",  type:"LABEL",  value:"業種"}]},	 
	 		{items:[{name:"#F.itemCategory",  type:"SELECT", bind:"itemCategory",  size:12, onchange:"changeCategoryType()"}]},
	 		{items:[{name:"#L.purchaseAssist", type:"LABEL",  value:"採購助理"}]},	 
	 		{items:[{name:"#F.purchaseAssist", type:"SELECT", bind:"purchaseAssist", size:12 }]},
	 		{items:[{name:"#L.purchaseMember", type:"LABEL",  value:"採購人員"}]},	 
	 		{items:[{name:"#F.purchaseMember", type:"SELECT", bind:"purchaseMember", size:12, onchange:"changePurchaseMember()" }]},
	 		{items:[{name:"#L.purchaseMaster", type:"LABEL",  value:"採購主管"}]},	 
			{items:[{name:"#F.purchaseMaster", type:"SELECT", bind:"purchaseMaster", size:12 }], td:" colSpan=3"}]},
				 
	 {row_style:"", cols:[
	 {items:[{name:"#L.enable", type:"LABEL", value:"單據狀態"}]},
	 {items:[{name:"#F.enable"  , type:"SELECT" , bind:"enable",init:allEnable, onchange:"changeEnable()"}]},
	 {items:[{name:"#L.beginDate", type:"LABEL", value:"啟/停用日"}]},
	 {items:[{name:"#F.beginDate" , type:"date",   bind:"beginDate"}]}]}],
	 
	 beginService:"",
	 closeService:""			
	});
}

function detailInitial(){
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;	
	
    vat.item.make(vnB_Detail, "indexNo", {type:"IDX", view:"fixed" , desc:"序號"}); 	
	vat.item.make(vnB_Detail, "combineCode", {type:"TEXT", view:"fixed", size:10, maxLen:8, desc:"組合代號", eChange: function(){ upperCase("combineCode"); }}); 
	vat.item.make(vnB_Detail, "combineName", {type:"hidden", view:"",    size:10, desc:"組合名稱"});
	vat.item.make(vnB_Detail, "combineQuantity", {type:"NUMB", view:"",    size:10, desc:"組合數量"});
	vat.item.make(vnB_Detail, "combinePrice", {type:"NUMB", view:"", size:8, maxLen:12, desc:"組合價格"});   
	vat.item.make(vnB_Detail, "itemBrand", {type:"TEXT", view:"", size:12, maxLen:12, desc:"商品品牌", eChange: function(){ changeLineData("ItemBrand"); }});
	vat.item.make(vnB_Detail, "itemBrandName", {type:"TEXT", view:"",desc:"名稱", mode:"READONLY"});	
	vat.item.make(vnB_Detail, "searchItem2"	        , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 		            	service:"Im_Item:searchCategory:20100119.page",
	 									 			            left:0, right:0, width:1024, height:768,	
	 									 						servicePassData:function(){ return doPassData("ItemBrand"); },	
			 									 				serviceAfterPick:function(id){doAfterPickerFunctionProcess("itemBrand",id);} });		 									 				
	vat.item.make(vnB_Detail, "category02", {type:"TEXT", view:"", size: 8, maxLen:40, desc:"商品中類", eChange: function(){ changeLineData("CATEGORY02"); }});
	vat.item.make(vnB_Detail, "category02Name", {type:"TEXT", view:"", desc:"名稱", mode:"READONLY"});	
	vat.item.make(vnB_Detail, "searchItem3"	        , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 		            	service:"Im_Item:searchCategory:20100119.page",
	 									 			            left:0, right:0, width:1024, height:768,	
	 									 						servicePassData:function(){ return doPassData("CATEGORY02"); },	
			 									 				serviceAfterPick:function(id){doAfterPickerFunctionProcess("category02",id);} });
	vat.item.make(vnB_Detail, "foreignCategory", {type:"TEXT", view:"", size: 20, maxLen:20, desc:"國外類別"});		 									 							 									 				
	vat.item.make(vnB_Detail, "unitPrice", {type:"NUMB", view:"", size: 8, maxLen:12, desc:"商品單價"});
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Detail, "reCombineId", {type:"hidden", view:"",desc:"主檔ID", mode:"READONLY"});	
    
	vat.block.pageLayout(vnB_Detail, {
	                            id: "vatItemDiv",	
	                            pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,														
							    beginService: "",
								closeService: "",
							    appendBeforeService : "appendBeforeMethod()",
							    appendAfterService  : "appendAfterMethod()",
								loadBeforeAjxService: "loadBeforeCombineAjxService()",
								loadSuccessAfter    : "pageLoadSuccess()", 
								loadFailureAfter    : "",
								eventService        : "",   
								saveBeforeAjxService: "saveBeforeCombineAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()",
								saveFailureAfter    : ""
														});
}

function allShopInitial(){

    vat.block.create(vnB_Shop_All, {
	id: "vatShopAllDiv", table:"cellspacing='1' class='' border='0' cellpadding='2'",
	title:"", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#F.isAllShop", type:"CHECKBOX" , bind:"isAllShop", eClick:"allShopCheckedMsg()"}]},
	 {items:[{name:"#L.AllShop", type:"LABEL" , value:"所有專櫃&nbsp;"}]}]}], 
	 
	 beginService:"",
	 closeService:""			
	});	
}

function shopInitial(){
  
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;
	// set column
	vat.item.make(vnB_Shop_Detail, "indexNo", {type:"IDX", desc:"序號"});
	vat.item.make(vnB_Shop_Detail, "shopCode", {type:"TEXT" , size:15, maxLen:20, desc:"專櫃代號", mask:"CCCCCCCCCCCC", onchange:"changeShopData()"});
	vat.item.make(vnB_Shop_Detail, "shopName", {type:"TEXT" , size:18, maxLen:20, desc:"專櫃名稱", mode:"READONLY"});	   
	
	vat.item.make(vnB_Shop_Detail, "reserve52", {type:"TEXT", size: 12, maxLen:20, desc:"備註"});	
	vat.item.make(vnB_Shop_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Shop_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Shop_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Shop_Detail, "message", {type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnB_Shop_Detail, {
	                            id:"vatShopDetailDiv",	
	                            pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,														
							    beginService: "",
								closeService: "",
							    appendBeforeService : "appendBeforeMethod()",
							    appendAfterService  : "appendAfterMethod()",
								loadBeforeAjxService: "loadShopBeforeAjxService()",
								loadSuccessAfter    : "pageLoadSuccess()",
								loadFailureAfter    : "",
								eventService        : "",   
								saveBeforeAjxService: "saveShopBeforeAjxService()",
								saveSuccessAfter    : "saveShopSuccessAfter()",
								saveFailureAfter    : ""
														});																							
	//vat.block.pageDataLoad(vnB_Shop_Detail, vnCurrentPage = 1);	
}

function allCustomerInitial(){
   
    vat.block.create(vnB_Customer_All, {
	id: "vatCustomerAllDiv", table:"cellspacing='1' class='' border='0' cellpadding='2'",
	title:"", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#F.isAllCustomer", type:"CHECKBOX" , bind:"isAllCustomer", eClick:"allCustomerCheckedMsg()"}]},
	 {items:[{name:"#L.AllCustomer", type:"LABEL" , value:"所有客戶別(含非會員)&nbsp;&nbsp;"}]},
	 
	 {items:[{name:"#L.customerType", type:"LABEL" , value:"客戶類別"}]},	 
	 {items:[{name:"#F.customerType", type:"SELECT",  bind:"customerType", size:1}]}]}], 
	 
	 beginService:"",
	 closeService:""			
	});	
}

function customerInitial(){
 
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;	
	// set column
	vat.item.make(vnB_Customer_Detail, "enable"                  , {type:"XBOX"});
    vat.item.make(vnB_Customer_Detail, "indexNo"                   , {type:"IDX", desc:"序號"});
	vat.item.make(vnB_Customer_Detail, "vipTypeCode", {type:"TEXT" , size:15, maxLen:20, desc:"客戶類別代號", mode:"READONLY"});
	vat.item.make(vnB_Customer_Detail, "vipTypeName", {type:"TEXT" , size:18, maxLen:20, desc:"客戶類別名稱", mode:"READONLY"});
    vat.item.make(vnB_Customer_Detail, "lineId", {type:"ROWID"});
	vat.block.pageLayout(vnB_Customer_Detail, {
	                            id:"vatCustomerDetailDiv",	
	                            pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,
								loadBeforeAjxService: "loadCustomerBeforeAjxService()",
								loadSuccessAfter    : "pageLoadSuccess()",   
								saveBeforeAjxService: "saveCustomerBeforeAjxService()",
								saveSuccessAfter    : "saveCustomerSuccessAfter()"
														});
}

function amountInitial(){
	vat.block.create(vnB_AmountDiv, {
		id: "vatAmountDiv",generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'",
		rows:[
	 	{row_style:" style='display:none'", cols:[
	 		{items:[{name:"#L.totalCostAmount",     type:"LABEL", value:"成本總金額(成本金額 * 預計銷貨量)"}]},			 
	 		{items:[{name:"#F.totalCostAmount",     type:"TEXT",  bind:"totalCostAmount", back:false, size:100, mode: "READONLY" }]}]},
		{row_style:"", cols:[
	 		{items:[{name:"#L.totalPriceAmount",    type:"LABEL", value:"定價總金額(定價 * 預計銷貨量)"}]},	 
	 		{items:[{name:"#F.totalPriceAmount",    type:"TEXT",  bind:"totalPriceAmount", back:false, size:100, mode: "READONLY" }]}]},
		{row_style:"", cols:[
	 		{items:[{name:"#L.totalDiscountAmount", type:"LABEL", value:"折扣後總金額(折扣金額 * 預計銷貨量)"}]},
	 		{items:[{name:"#F.totalDiscountAmount", type:"TEXT",  bind:"totalDiscountAmount", back:false, size:100, mode: "READONLY" }]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.totalAfterDiscountAmount",  type:"LABEL", value:"折扣總金額(定價總金額 - 折扣後總金額)"}]},
	 		{items:[{name:"#F.totalAfterDiscountAmount",  type:"TEXT",  bind:"totalAfterDiscountAmount", back:false, size:100, mode: "READONLY" }]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.averageDiscountRate",  type:"LABEL", value:"平均折扣率(Discount)(折扣後總金額/定價總金額))"}]},
	 		{items:[{name:"#F.averageDiscountRate",  type:"TEXT",  bind:"averageDiscountRate", back:false, size:100, mode: "READONLY" }]}]},
 	 	],
		beginService:"",
		closeService:function(){formDataInitial();}			
	});
}


function loadBeforeCombineAjxService(){
    
    var processString = "process_object_name=imPromotionReCombineMainService&process_object_method_name=getAJAXPageDataForReCombine" + 
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&formStatus=" + vat.item.getValueByName("#F.status") + 
	                    "&priceType=" + vat.item.getValueByName("#F.priceType"); 
																					
	return processString;											
    
}

function loadShopBeforeAjxService(){
	var processString = "process_object_name=imPromotionMainService&process_object_method_name=getAJAXPageDataForShop" + 
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&formStatus=" + vat.item.getValueByName("#F.status");
																					
	return processString;											
}

function loadCustomerBeforeAjxService(){
	var processString = "process_object_name=imPromotionMainService&process_object_method_name=getAJAXPageDataForCustomer" + 
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&formStatus=" + vat.item.getValueByName("#F.status");
																					
	return processString;											
}


function saveBeforeCombineAjxService() {

	var processString = "process_object_name=imPromotionReCombineMainService&process_object_method_name=updateOrSaveCombinePageLinesData" + 
		"&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status")
		+ "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode"].value;
                                
            return processString;
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {

	var processString = "";
	processString = "process_object_name=imPromotionMainService&process_object_method_name=updateAJAXPageLinesData" + "&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status");
					
	return processString;
}

/*
	SAVE SHOP LINE FUNCTION
*/
function saveShopBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imPromotionMainService&process_object_method_name=updateAJAXShopLinesData" + "&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status");
	return processString;
}

/*
	SAVE CUSTOMER LINE FUNCTION
*/
function saveCustomerBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imPromotionMainService&process_object_method_name=updateAJAXCustomerLinesData" + "&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status");
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
	} else if ("totalCount" == afterSavePageProcess) {
	    getTotalAmt();	
	}
	
	afterSavePageProcess = "";
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveShopSuccessAfter() {
    
    if ("saveHandler" == afterSaveShopPageProcess) {	
	    doCustomerSaveHandler();
	} else if ("submitHandler" == afterSaveShopPageProcess) {
	    doCustomerSubmitHandler();
	} else if ("submitBgHandler" == afterSaveShopPageProcess) {
	    doCustomerSubmitBgHandler();
	} else if ("voidHandler" == afterSaveShopPageProcess) {
		doCustomerVoidHandler();
	} else if ("executeImport" == afterSaveShopPageProcess) {
	    doCustomerImportHandler();	    
	}

	afterSaveShopPageProcess = "";
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveCustomerSuccessAfter() {

    if ("saveHandler" == afterSaveCustomerPageProcess) {	
	    execSubmitAction("SAVE");
	} else if ("submitHandler" == afterSaveCustomerPageProcess) {
	    execSubmitAction("SUBMIT");
	} else if ("submitBgHandler" == afterSaveCustomerPageProcess) {
	    execSubmitAction("SUBMIT_BG");
	} else if ("voidHandler" == afterSaveCustomerPageProcess) {
		execSubmitAction("VOID");
	} else if ("executeImport" == afterSaveCustomerPageProcess) {
	    importFormData();
	}

	afterSaveCustomerPageProcess = "";
}

/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
    if (confirm("是否確認暫存?")) {		
	    //save line
	    afterSavePageProcess = "saveHandler";
		vat.block.pageSearch(vnB_Detail);				
	}
}

/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	if (confirm("是否確認送出?")) {		
		//save line
		afterSavePageProcess = "submitHandler";	
		vat.block.pageSearch(vnB_Detail);
	}
}

/*
	背景送出SUBMIT HEAD && LINE
*/
function doSubmitBgHandler() {
	if (confirm("是否確認背景送出?")) {		
		//save line
		afterSavePageProcess = "submitBgHandler";	
		vat.block.pageSearch(vnB_Detail);
	}
}

/*
	作廢VOID HEAD && LINE
*/
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		afterSavePageProcess = "voidHandler";	
		vat.block.pageSearch(vnB_Detail);			
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
	vat.block.pageSearch(vnB_Shop_Detail);
}

/*
	匯入
*/
function doActualImport() {
	//save line
	afterSaveCustomerPageProcess = "executeImport";
	vat.block.pageSearch(vnB_Customer_Detail);	
}

/*
	暫存 SAVE HEAD && LINE
*/
function doActualSaveHandler() { 
    afterSaveShopPageProcess = "saveHandler";		
    vat.block.pageSearch(vnB_Shop_Detail);
}

/*
	送出SUBMIT HEAD && LINE
*/
function doActualSubmitHandler() {
    afterSaveShopPageProcess = "submitHandler";	
	vat.block.pageSearch(vnB_Shop_Detail);	
}

/*
	背景送出SUBMIT HEAD && LINE
*/
function doActualSubmitBgHandler() {
    afterSaveShopPageProcess = "submitBgHandler";	
	vat.block.pageSearch(vnB_Shop_Detail);	
}

/*
	作廢VOID HEAD && LINE
*/
function doActualVoidHandler() {
    afterSaveShopPageProcess = "voidHandler";
	vat.block.pageSearch(vnB_Shop_Detail);	
}

/*
	暫存 SAVE HEAD && LINE(客戶)
*/
function doCustomerSaveHandler() { 
    afterSaveCustomerPageProcess = "saveHandler";		
    vat.block.pageSearch(vnB_Customer_Detail);
}

/*
	送出SUBMIT HEAD && LINE(客戶)
*/
function doCustomerSubmitHandler() {
    afterSaveCustomerPageProcess = "submitHandler";	
	vat.block.pageSearch(vnB_Customer_Detail);	
}

/*
	背景送出SUBMIT HEAD && LINE(客戶)
*/
function doCustomerSubmitBgHandler() {
    afterSaveCustomerPageProcess = "submitBgHandler";	
	vat.block.pageSearch(vnB_Customer_Detail);	
}

/*
	作廢VOID HEAD && LINE(客戶)
*/
function doCustomerVoidHandler() {
    afterSaveCustomerPageProcess = "voidHandler";
	vat.block.pageSearch(vnB_Customer_Detail);	
}

/*
	匯入LINE(客戶)
*/
function doCustomerImportHandler() {
    afterSaveCustomerPageProcess = "executeImport";
	vat.block.pageSearch(vnB_Customer_Detail);	
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
    vat.item.setValueByName("#F.inCharge", vat.item.getValueByName("#F.inCharge").replace(/^\s+|\s+$/, ''));   
    if(vat.item.getValueByName("#F.inCharge") !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vat.item.getValueByName("#F.inCharge"),
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               vat.item.setValueByName("#F.inCharge", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setValueByName("#F.inChargeName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setValueByName("#F.inChargeName", "");
    }
}

function doPageDataSave(){
    vat.block.pageSearch(vnB_Detail);
    vat.block.pageSearch(vnB_Shop_Detail);
    vat.block.pageSearch(vnB_Customer_Detail);
}

function doPageDataSaveForItem(){
    vat.block.pageRefresh(vnB_Detail);
    vat.block.pageSearch(vnB_Shop_Detail);
    vat.block.pageSearch(vnB_Customer_Detail);
}

function doPageDataSaveForShop(){
    vat.block.pageRefresh(vnB_Shop_Detail);
    vat.block.pageSearch(vnB_Detail);
    vat.block.pageSearch(vnB_Customer_Detail);
}

function doPageDataSaveForCustomer(){
    vat.block.pageRefresh(vnB_Customer_Detail);
    vat.block.pageSearch(vnB_Detail);
    vat.block.pageSearch(vnB_Shop_Detail);
}

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(vnB_Detail);
    vat.block.pageDataSave(vnB_Shop_Detail);
}

function appendBeforeMethod(){
    return true;
}

function appendAfterMethod(){
    // return alert("新增完畢");
}

function pageLoadSuccess(div){
	// alert("載入成功");	
}

function execSubmitAction(actionId){
    var formId = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var assignmentId = document.forms[0]["#assignmentId"].value.replace(/^\s+|\s+$/, '');
    var processId = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var status = vat.item.getValueByName("#F.status");
    var employeeCode = document.forms[0]["#loginEmployeeCode"].value.replace(/^\s+|\s+$/, '');
    var approvalResult = vat.item.getValueByName("#F.approvalResult");
    if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
    var approvalComment = vat.item.getValueByName("#F.approvalComment");
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
      loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
  	  loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,
  	  orderTypeCode      : document.forms[0]["#orderTypeCode"].value,
      beforeChangeStatus : status,
      formStatus         : formStatus,
      assignmentId       : assignmentId,
      processId          : processId,
      approvalResult     : approvalResult,
      approvalComment    : approvalComment
    };
    if("SUBMIT_BG" == actionId){
        vat.block.submit(function(){return "process_object_name=imPromotionReCombineMainAction"+
            "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
    }else{
        vat.block.submit(function(){return "process_object_name=imPromotionReCombineMainAction"+
            "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
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


function exportFormData(){
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IM_PROMOTION_RECOMBINE_MOD" + 
              "&fileType=XLS" + 
              "&processObjectName=imPromotionMainService" + 
              "&processObjectMethodName=findById" + 
              "&gridFieldName=imPromotionReCombines" + 
              "&arguments=" + vat.item.getValueByName("#F.headId") + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '組合單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function importFormData(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=IM_PROMOTION_RECOMBINE_MOD" +
		"&importFileType=XLS" +
        "&processObjectName=imPromotionMainService" + 
        "&processObjectMethodName=executeImportPromotionCombines" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG" +       
        "&eventObjectName=imPromotionReCombineMainService" + 
        "&eventObjectMethodName=saveCombineItemData" +
        "&eventArguments=" + vat.item.getValueByName("#F.headId") +
        "&eventParameterTypes=LONG" +      
        "&blockId=" + vnB_Detail,
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
        "&processObjectName=imPromotionMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imPromotionMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
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

function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
        refreshForm("");
	}
}

function refreshForm(vsHeadId){  
	document.forms[0]["#formId"].value = vsHeadId;
	document.forms[0]["#processId"].value = "";
	document.forms[0]["#assignmentId"].value = ""; 	
	document.forms[0]["#activityId"].value = "";
	document.forms[0]["#activityName"].value = "";
	document.forms[0]["#activityStatus"].value = "";
	document.forms[0]["#canBeClaimed"].value = "false";	
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"].value;	
	vat.block.submit(
		function(){
			return "process_object_name=imPromotionReCombineMainAction&process_object_method_name=performInitial";			         
     	},{other: true, 
     	   funcSuccess:function(){
     	       vat.item.SelectBind(vat.bean("allOrderTypes"),{ itemName : "#F.orderTypeCode" });
               vat.item.SelectBind(vat.bean("allPriceTypes"),{ itemName : "#F.priceType" });
               vat.item.SelectBind(vat.bean("allCustomerTypes"),{ itemName : "#F.customerType" });      
               vat.item.SelectBind(vat.bean("allItemCategory"),{ itemName : "#F.itemCategory" });
               vat.item.SelectBind(vat.bean("allPurchaseAssist"),{ itemName : "#F.purchaseAssist" });
               vat.item.SelectBind(vat.bean("allPurchaseMember"),{ itemName : "#F.purchaseMember" });
               vat.item.SelectBind(vat.bean("allPurchaseMaster"),{ itemName : "#F.purchaseMaster" });
     	       vat.item.bindAll();
     		   vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     		   vat.block.pageDataLoad(vnB_Shop_Detail, vnCurrentPage = 1);
     		   vat.block.pageDataLoad(vnB_Customer_Detail, vnCurrentPage = 1);            
               refreshWfParameter(vat.item.getValueByName("#F.brandCode"), vat.item.getValueByName("#F.orderTypeCode"),  vat.item.getValueByName("#F.orderNo"));	 
               vat.tabm.displayToggle(0, "xTab5",  vat.item.getValueByName("#F.status") != "SAVE", false, false);
               vat.block.pageDataLoad(102, vnCurrentPage = 1);
               //vat.tabm.displayToggle(0, "xTab1", true);
               doFormAccessControl();                    	   		    		 
     	  }}
    );
}

function doFormAccessControl(){
    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
  	var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
  	var vsFormStatus    = vat.item.getValueByName("#F.status");
  	var vsProcessId     = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
  	var vsOrderNoPrefix = vat.item.getValueByName("#F.orderNo").substring(0, 3);
  	var vsActivityStatus= document.forms[0]["#activityStatus"].value.replace(/^\s+|\s+$/, '');
  	var vsCanBeClaimed	= document.forms[0]["#canBeClaimed"].value;
  	var vsEnable = vat.item.getValueByName("#F.enable");
	vat.item.setStyleByName("#B.new"         , "display", "inline");
	vat.item.setStyleByName("#B.search"      , "display", "inline");
	vat.item.setStyleByName("#B.exit"        , "display", "inline");
	vat.item.setStyleByName("#B.submit"      , "display", "inline");
	vat.item.setStyleByName("#B.save"        , "display", "inline");
	vat.item.setStyleByName("#B.void"        , "display", "inline");
	vat.item.setStyleByName("#B.submitBG"    , "display", "inline");
	vat.item.setStyleByName("#B.message"     , "display", "inline");
	vat.item.setStyleByName("#B.export"      , "display", "inline");
	vat.item.setStyleByName("#B.import"      , "display", "inline");
	vat.item.setStyleByName("#B.first"       , "display", "inline");
	vat.item.setStyleByName("#B.forward"     , "display", "inline");
	vat.item.setStyleByName("#B.next"        , "display", "inline");
	vat.item.setStyleByName("#B.last"        , "display", "inline");
	
	if(vsProcessId!="" || vsOrderNoPrefix.indexOf("TMP") == -1){
 		vat.tabm.displayToggle(0, "xTab5", true, false, false); 		
 	}else{
 		vat.tabm.displayToggle(0, "xTab5", false, false, false);
 	}
 	
 	if("TMP" == vsOrderNoPrefix){
 	vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", false);
 	vat.item.setAttributeByName("#F.orderTypeCode"   , "readOnly", true);
 	vat.item.setAttributeByName("#F.orderNo"   , "readOnly", true);
 	vat.item.setAttributeByName("#F.headId"   , "readOnly", true);
 	vat.item.setAttributeByName("#F.priceType"   , "readOnly", true);
 	vat.item.setAttributeByName("#F.brandName"   , "readOnly", true);
 	vat.item.setAttributeByName("#F.statusName"   , "readOnly", true);
 	vat.item.setAttributeByName("#F.createdByName"   , "readOnly", true);
 	vat.item.setAttributeByName("#F.creationDate"   , "readOnly", true);
 	vat.item.setAttributeByName("#F.inChargeName"   , "readOnly", true);
 	
 	}	
	
	if(  vsFormStatus != ""   ){
	
		if(vsProcessId == 0 || vsProcessId == null){//使用Picker或從查詢功能選入  
			if("TMP" == vsOrderNoPrefix){
				
				vat.item.setStyleByName("#B.void"        , "display", "none");
				vat.item.setGridAttributeByName("itemName", "readOnly", true);
				vat.item.setGridAttributeByName("standardPurchaseCost", "readOnly", true);
				vat.item.setGridAttributeByName("originalPrice", "readOnly", true); 
				vat.item.setGridAttributeByName("stockOnHandQty", "readOnly", true);
				vat.item.setGridAttributeByName("totalDiscountAmount", "readOnly", true); 
				vat.item.setGridAttributeByName("totalDiscountPercentage", "readOnly", true);
				vat.item.setGridAttributeByName("margenBeforeDisc", "readOnly", true);
				vat.item.setGridAttributeByName("margenAfterDisc", "readOnly", true);
				vat.item.setGridAttributeByName("shopName", "readOnly", true);				
				vat.item.setGridAttributeByName("vipTypeCode", "readOnly", true);
				vat.item.setGridAttributeByName("vipTypeName", "readOnly", true);
				vat.item.setAttributeByName("vatItemDiv"     , "readOnly", false);				
				if(vsEnable==="Y"){		
				vat.item.setAttributeByName("vatItemDiv"     , "readOnly", false);
				vat.item.setStyleByName("#B.importCombine", 	"display", "none");	// 取原組合單
				vat.item.setStyleByName("#B.import", 	"display", "inline");	// 明細匯入
				}else{		
				vat.item.setAttributeByName("vatItemDiv"     , "readOnly", true);
				vat.item.setStyleByName("#B.importCombine", 	"display", "inline");	// 取原組合單
				vat.item.setStyleByName("#B.import", 	"display", "none");	// 明細匯入
				}				
			}else {
				vat.item.setStyleByName("#B.submit"      , "display", "none");
				vat.item.setStyleByName("#B.save"        , "display", "none");
				vat.item.setStyleByName("#B.submitBG"    , "display", "none");
				vat.item.setStyleByName("#B.import"      , "display", "none");
				vat.item.setStyleByName("#B.void"        , "display", "none");
				vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
			    vat.item.setAttributeByName("vatItemDiv"     , "readOnly", true);
				vat.item.setAttributeByName("vatShopDiv"     , "readOnly", true);
				vat.item.setAttributeByName("vatCustomerDiv" , "readOnly", true);				
			}
		}else{ //從待辦事項進入
		    if("WF_ACT_WAIT" == vsActivityStatus || "" == vsActivityStatus){
		        if( "SAVE" == vsFormStatus || "REJECT" == vsFormStatus){
		            vat.item.setStyleByName("#B.new", "display", "none");
		            vat.item.setGridAttributeByName("itemName", "readOnly", true);
				    vat.item.setGridAttributeByName("standardPurchaseCost", "readOnly", true); 
				    vat.item.setGridAttributeByName("originalPrice", "readOnly", true); 
				    vat.item.setGridAttributeByName("stockOnHandQty", "readOnly", true); 
				    vat.item.setGridAttributeByName("totalDiscountAmount", "readOnly", true); 
				    vat.item.setGridAttributeByName("totalDiscountPercentage", "readOnly", true);
    				vat.item.setGridAttributeByName("margenBeforeDisc", "readOnly", true);
					vat.item.setGridAttributeByName("margenAfterDisc", "readOnly", true);
				    vat.item.setGridAttributeByName("shopName", "readOnly", true);				
				    vat.item.setGridAttributeByName("vipTypeCode", "readOnly", true);
				    vat.item.setGridAttributeByName("vipTypeName", "readOnly", true);
				    if(vsEnable==="N"){
				    	vat.block.canGridModify([vnB_Detail, vnB_Shop_Detail, vnB_Customer_Detail], false, false, false);
				    }		    
		        }else{
		            vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
				    //vat.item.setAttributeByName("vatItemDiv"     , "readOnly", true);
				    vat.item.setAttributeByName("vatShopDiv"     , "readOnly", true);
				    vat.item.setAttributeByName("vatCustomerDiv" , "readOnly", true); 
				    vat.block.canGridModify([vnB_Detail, vnB_Shop_Detail, vnB_Customer_Detail], false, false, false);
				    vat.item.setStyleByName("#B.new"         , "display", "none");	           
	                vat.item.setStyleByName("#B.save"        , "display", "none");
	                vat.item.setStyleByName("#B.void"        , "display", "none");
	                vat.item.setStyleByName("#B.submitBG"    , "display", "none");
	                vat.item.setStyleByName("#B.import"      , "display", "none");
	                if(!"SIGNING" == vsFormStatus || "true" == vsCanBeClaimed){
	                    vat.item.setStyleByName("#B.submit"      , "display", "none");
	                    vat.item.setStyleByName("#B.message"     , "display", "none");
	                }
	                      
			    }
			}else{
			    vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
				//vat.item.setAttributeByName("vatItemDiv"     , "readOnly", true);
				vat.item.setAttributeByName("vatShopDiv"     , "readOnly", true);
				vat.item.setAttributeByName("vatCustomerDiv" , "readOnly", true);
				vat.block.canGridModify([vnB_Detail, vnB_Shop_Detail, vnB_Customer_Detail], false, false, false); 
				vat.item.setStyleByName("#B.new"         , "display", "none");	           
	            vat.item.setStyleByName("#B.save"        , "display", "none");
	            vat.item.setStyleByName("#B.void"        , "display", "none");
	            vat.item.setStyleByName("#B.submitBG"    , "display", "none");
	            vat.item.setStyleByName("#B.import"      , "display", "none");
	            vat.item.setStyleByName("#B.submit"      , "display", "none");
	            vat.item.setStyleByName("#B.message"     , "display", "none");
	            
			}
			vat.item.setStyleByName("#B.search"      , "display", "none");
			vat.item.setStyleByName("#B.first"       , "display", "none");
			vat.item.setStyleByName("#B.forward"     , "display", "none");
			vat.item.setStyleByName("#B.next"        , "display", "none");
			vat.item.setStyleByName("#B.last"        , "display", "none");
		}
	}
	if(vsEnable==="Y"){		
	vat.item.setStyleByName("#B.importCombine", 	"display", "none");	// 取原組合單
	vat.item.setStyleByName("#B.import", 	"display", "inline");	// 明細匯入
	}else{		
	vat.item.setStyleByName("#B.importCombine", 	"display", "inline");	// 取原組合單
	vat.item.setStyleByName("#B.import", 	"display", "none");	// 明細匯入
	}
	
	if("T2" == vsBrandCode){
	    vat.item.setGridStyleByName("standardPurchaseCost", "display", "none");
	}
	if("SAVE" == vsFormStatus ||"REJECT" == vsFormStatus || "VOID" == vsFormStatus){
		vat.item.setAttributeByName("#F.approvalResult","readOnly", true);
	}else{
		vat.item.setStyleByName("#B.importCombine", 	"display", "none");	// 取原組合單
	}
	
	vat.item.setGridAttributeByName("itemBrandName"  , "readOnly", true);
	vat.item.setGridAttributeByName("category02Name"  , "readOnly", true);	
}

function changeCategoryType(){

    var vItemCategory = vat.item.getValueByName("#F.itemCategory");
    
       vat.ajax.XHRequest(
       {
           post:"process_object_name=imPromotionMainService"+
                    "&process_object_method_name=getPurchaseEmployeeForAJAX"+
                    "&itemCategory=" + vItemCategory,
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

function changePurchaseMember(){
	var brandCode = vat.item.getValueByName("#F.brandCode");
	if("T2" == brandCode){	
		vat.item.setValueByName("#F.inCharge", vat.item.getValueByName("#F.purchaseMember"));
		changeSuperintendent();
    }
}

function changeSuperintendent(){

    var vInCharge = vat.item.getValueByName("#F.inCharge").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.inCharge", vInCharge);
    if(vInCharge !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vInCharge,
           find: function changeSuperintendentRequestSuccess(oXHR){              
               //vat.item.setValueByName("#F.inCharge", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setValueByName("#F.inChargeName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
         vat.item.setValueByName("#F.inChargeName", "");
    }
}

function doAfterPickerProcess(){
	//alert("doAfterPickerProcess")
	if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
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

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsHeadId);
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
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsHeadId);
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
	    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function doPassHeadData(){
  
  var suffix = "";
  var brandCode = vat.item.getValueByName("#F.brandCode");
  var employeeCode = document.forms[0]["#loginEmployeeCode"].value;    
  var orderTypeCode  = vat.item.getValueByName("#F.OrderTypeCode").replace(/^\s+|\s+$/, '').toUpperCase();
  suffix += "&loginBrandCode="+escape(brandCode)+
            "&orderTypeCode="+escape(orderTypeCode)+
            "&loginEmployeeCode="+escape(employeeCode);

  return suffix;
}

// 票據列印
function openReportWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=imPromotionMainService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
			                    	//alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);

	if("AFTER_SUBMIT"==type) createRefreshForm();
}

// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "serialPicker":
			suffix += "&formId="+escape(vat.item.getValueByName("#H.itemId"))+"&itemPicker=true"; 
			break;
		case "CATEGORY02":
			suffix += "&categoryType="+escape("CATEGORY02");
			break;
		case "ItemBrand":
			suffix += "&categoryType="+escape("ItemBrand");
			break;
	}
	//alert(suffix);
	return suffix;
}

// do picker 回來的事件
function doAfterPickerFunctionProcess(code,id){
	//do picker back something
	var vLineId	= vat.item.getGridLine(id);
	switch(code){
		case "itemBrand":
		if(typeof vat.bean().vatBeanPicker.result != "undefined"){
			vat.item.setGridValueByName("itemBrand", vLineId, vat.bean().vatBeanPicker.result[0]["id.categoryCode"]);
			changeCategoryCodeName("ItemBrand",id);
		}    	 
		break;
		case "category02":
		if(typeof vat.bean().vatBeanPicker.result != "undefined"){
			vat.item.setGridValueByName("category02", vLineId, vat.bean().vatBeanPicker.result[0]["id.categoryCode"]);
			changeCategoryCodeName("CATEGORY02",id);
		}    	 
		break;
	}	
}

// 動態改變商品類別名稱
function changeCategoryCodeName(code,id){
	var condition = "", name ="";
	var vLineId	= vat.item.getGridLine(id);
	switch(code){
		case "CATEGORY02":
			condition =  "&categoryCode=" + ( "CATEGORY02" === code ? vat.item.getGridValueByName("category02", vLineId) : "" );
			name = "category02Name";
		break;
		case "ItemBrand":
			condition =  "&categoryCode=" + ( "ItemBrand" === code ? vat.item.getGridValueByName("itemBrand", vLineId) : "" );
			name = "itemBrandName";
		break;
	}
	
	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXCategoryName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&categoryType=" + code + condition,
                  
		find: function change(oXHR){ 
         		vat.item.setGridValueByName(name, vLineId, vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		vat.item.setGridValueByName(name, vLineId, "查無此類別");
		}   
	});	
}

// 動態改變一筆商品資料 
function changeLineData(code,pickerLine ){
	
	var brandCode			= vat.item.getValueByName("#F.brandCode");
	
	var vLineId				= typeof pickerLine === "undefined" ? vat.item.getGridLine() : pickerLine ;
	
	var itemBrand =  vat.item.getGridValueByName("itemBrand", vLineId);
	var category02 =  vat.item.getGridValueByName("category02", vLineId);
	
	if(itemBrand != null && itemBrand.length > 0){
		vat.item.setGridValueByName("itemBrand", vLineId, itemBrand.toUpperCase());
	}
	
	if(category02 != null && category02.length > 0){
		vat.item.setGridValueByName("category02", vLineId, category02.toUpperCase());
	}
	
	var condition = "", name ="";
	
	switch(code){
		case "CATEGORY02":
			condition =  "&categoryCode=" + ( "CATEGORY02" === code ? vat.item.getGridValueByName("category02", vLineId) : "" );
			name = "category02Name";
		break;
		case "ItemBrand":
			condition =  "&categoryCode=" + ( "ItemBrand" === code ? vat.item.getGridValueByName("itemBrand", vLineId) : "" );
			name = "itemBrandName";
		break;
	}
	
	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXCategoryName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&categoryType=" + code + condition,
                  
		find: function change(oXHR){ 
         		vat.item.setGridValueByName(name, vLineId, vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		vat.item.setGridValueByName(name, vLineId, "查無此類別");
		}   
	});
}

function changeEnable(){
	var enable = vat.item.getValueByName("#F.enable");
	
	if(enable==="Y"){
		vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
		vat.item.setAttributeByName("vatItemDiv"     , "readOnly", false);
		vat.item.setGridAttributeByName("itemBrandName"  , "readOnly", true);
		vat.item.setGridAttributeByName("category02Name"  , "readOnly", true);
		vat.item.setStyleByName("#B.importCombine", 	"display", "none");	// 取原組合單
		vat.item.setStyleByName("#B.import", 	"display", "inline");	// 明細匯入
	}else{
		vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
		vat.item.setAttributeByName("vatItemDiv"     , "readOnly", true);
		vat.item.setStyleByName("#B.importCombine", 	"display", "inline");	// 取原組合單
		vat.item.setStyleByName("#B.import", 	"display", "none");	// 明細匯入
	}	
}

//轉大寫
function upperCase(id,pickerLine){
	var vLineId				= typeof pickerLine === "undefined" ? vat.item.getGridLine() : pickerLine ;
	var combineCode =  vat.item.getGridValueByName("combineCode", vLineId);
	if(combineCode != null && combineCode.length > 0){
		vat.item.setGridValueByName("combineCode", vLineId, combineCode.toUpperCase());
	}
}

function doPickerWithPromotionCombine(){
	vat.block.pageSearch(vnB_Detail);
}
