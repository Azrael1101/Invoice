/*** 
 *	檔案: singlePostingTally.js
 *	說明：POS單筆過帳作業
 *	修改：joey
 *  <pre>
 *  	Created by Joey
 *  	All rights reserved.
 *  </pre>
 */



vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;
var vnB_Detail2 = 3;
var vnB_Detail3 = 4;
var vnB_Detail4 = 5;
var vnB_Detail5 = 6;
var vnB_Detail6 = 7;
var vnB_Detail7 = 8;
function outlineBlock(){
  	formDataInitial();
	kweButtonLine();
  	headerInitial();
  	    if (typeof vat.tabm != 'undefined') {
        vat.tabm.createTab(0, "vatTabSpan", "H", "float");
        vat.tabm.createButton(0, "xTab1", "促銷查詢", "vatDetailDiv", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", "none", "");
        vat.tabm.createButton(0, "xTab2", "商品品號查詢", "vatDetailDiv2", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", "none", "");
		vat.tabm.createButton(0, "xTab3", "國際碼查詢", "vatDetailDiv3", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", "none", "");        
		vat.tabm.createButton(0, "xTab4", "VIP卡號查詢", "vatDetailDiv4", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", "none", "");
		vat.tabm.createButton(0, "xTab5", "員工工號查詢", "vatDetailDiv5", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", "none", "");
		vat.tabm.createButton(0, "xTab6", "商品折扣查詢", "vatDetailDiv6", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", "inline", "");
		vat.tabm.createButton(0, "xTab7", "匯率查詢", "vatDetailDiv7", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", "none", "");
    }
  	detailInitial();
	detailInitial2();
  	detailInitial3();
  	detailInitial4();
  	detailInitial5();
  	detailInitial6();
  	detailInitial7();
  	vat.tabm.displayToggle(0, "xTab1", false, false, false);
    vat.tabm.displayToggle(0, "xTab2", false, false, false);
    vat.tabm.displayToggle(0, "xTab3", false, false, false);
    vat.tabm.displayToggle(0, "xTab4", false, false, false);
    vat.tabm.displayToggle(0, "xTab5", false, false, false);
    vat.tabm.displayToggle(0, "xTab6", true, false, false);
    vat.tabm.displayToggle(0, "xTab7", false, false, false);
}

function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = 
		{
			loginBrandCode		:	document.forms[0]["#loginBrandCode" ].value,
			loginEmployeeCode	:	document.forms[0]["#loginEmployeeCode" ].value
    	}; 
	    
		vat.bean.init(function(){
			return "process_object_name=posDUAction&process_object_method_name=performInitial"; 
		},{other: true});
		vat.item.bindAll();
	}
}

function kweButtonLine(){
	vat.block.create(vnB_Button, {id: "vatBlock_Button", generate: true,	
	title:"", rows:[	 
	{row_style:"", cols:[
	 	{items:[	 	        
	 			{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
                //{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		//{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit()'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.e xit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.selectSubmit"      , type:"IMG"    ,value:"明細送出",   src:"./images/button_detail_submit.gif", eClick:'doSelectSubmit()'},
                {name:"SPACE"          , type:"LABEL"  ,value:"　"},
                {name:"#L.downloadCount", type:"LABEL", value:"下傳筆數:"},
                {name:"#F.downloadCount", type:"TEXT" , size:5, bind:"downloadCount" , eChange:'numcheck()'}]
                ,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
                
	 	 		//{name:"#B.selectSubmit"      , type:"BUTTON"    ,value:"明細送出", eClick:'doSelectSubmit()'}],
	 			//	td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
	 			]}
				//{name:"#B.message"  	, type:"IMG"    ,value:"訊息提示"	,   src:"./images/button_message_prompt.gif", eClick:'showMessage()'}
	 	],
		beginService:"",
		closeService:""			
	});
}

function headerInitial(){ 	
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"POS資料下傳作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.downloadFunction"		, type:"LABEL"	, value:"下傳類別"}]},
	 			{items:[{name:"#F.downloadFunction"		, type:"SELECT"	, bind:"downloadFunction", eChange:"lockCol()"}]},
	 			{items:[{name:"#L.brandCode"			, type:"LABEL"	, value:"品牌"}]},	
				{items:[{name:"#F.brandCode"			, type:"TEXT"	, bind:"brandCode", mode:"HIDDEN" },
			            {name:"#F.brandName"			, type:"TEXT"	, bind:"brandName", mode:"READONLY"}]},
			    {items:[{name:"#L.createdBy"			, type:"LABEL", value:"下傳人員"}]},
				{items:[{name:"#F.createdBy"			, type:"TEXT", bind:"createdBy", mode:"HIDDEN", size:6},
						{name:"#F.createdByName"		, type:"TEXT", bind:"createdByName", mode:"READONLY", size:6}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.shopCode"				, type:"LABEL"	, value:"店別代號"}]},
	 			{items:[{name:"#F.shopCode"				, type:"SELECT"	, bind:"shopCode", eChange:"changeShopCode()"}]},  
				{items:[{name:"#L.posMachineCode"		, type:"LABEL"	, value:"機台號碼"}]},
				{items:[{name:"#F.posMachineCode"		, type:"SELECT"	, bind:"posMachineCode"}]},
				{items:[{name:"#L.priceDate"			, type:"LABEL"	, value:"價錢日期"}]},
				{items:[{name:"#F.priceDate"			, type:"DATE"	, bind:"priceDate", mode:"READONLY"}]}
				
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.dateStart"			, type:"LABEL"	, value:"起始日期"}]},
	 			{items:[{name:"#F.dateStart"			, type:"DATE"	, bind:"dateStart"}]},  
				{items:[{name:"#L.dateEnd"				, type:"LABEL"	, value:"結束日期"}]},
				{items:[{name:"#F.dateEnd"				, type:"DATE"	, bind:"dateEnd"}]},
				{items:[{name:"#L.ItemCode"		, type:"LABEL"	, value:"商品品號"}]},
				{items:[{name:"#F.ItemCode"		, type:"TEXT"	, bind:"ItemCode", mode:"READONLY"}]}

			]},
				{row_style:"", cols:[
				{items:[{name:"#L.promotionCode"				, type:"LABEL"	, value:"促銷單號"}]},
	 			{items:[{name:"#F.promotionCode"				, type:"TEXT"	, bind:"promotionCode", mode:"READONLY"}]},  
				{items:[{name:"#L.promotionItemCode"		, type:"LABEL"	, value:"促銷品號"}]},
				{items:[{name:"#F.promotionItemCode"		, type:"TEXT"	, bind:"promotionItemCode", mode:"READONLY"}]},
				{items:[{name:"#L.empNo"		, type:"LABEL"	, value:"員工工號"}]},
				{items:[{name:"#F.empNo"		, type:"TEXT"	,bind:"empNo", mode:"READONLY"}]}
			]},
				{row_style:"", cols:[
				{items:[{name:"#L.eanCode"				, type:"LABEL"	, value:"國際碼"}]},
	 			{items:[{name:"#F.eanCode"				, type:"TEXT"	, bind:"eanCode", mode:"READONLY"}]},  
				{items:[{name:"#L.vipCode"		, type:"LABEL"	, value:"VIP卡號"}]},
				{items:[{name:"#F.vipCode"		, type:"TEXT"	,bind:"vipCode", mode:"READONLY"}]},
				{items:[{name:"#L.LockDate"				, type:"LABEL"	, value:"商品鎖定/解鎖日期"}]},
				{items:[{name:"#F.LockDate"			, type:"DATE"	, bind:"LockDate", mode:"READONLY"}]}
				
			]}
		], 	
		beginService:"",
		closeService:function(){closeHeader();}	
	});	  
}

function closeHeader(){
vat.ajax.XHRequest({ 
	post:"process_object_name=posDUService"+
          		"&process_object_method_name=findDownloadCommon"+
          		"&brandCode=" + document.forms[0]["#loginBrandCode" ].value,
          asyn:false,                      
	find: function change(oXHR){
		vat.item.SelectBind(eval(vat.ajax.getValue("allDownloadFunction", oXHR.responseText)),{ itemName : "#F.downloadFunction" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allShop"			, oXHR.responseText)),{ itemName : "#F.shopCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allBuShopMachine"	, oXHR.responseText)),{ itemName : "#F.posMachineCode" });
		vat.item.bindAll();
		}
	});
}

function detailInitial(){
  	
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    //var vbSelectionType = "CHECK";
	//if(vatPickerId != null && vatPickerId != ""){
    //     vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX" ,                     desc:""});
    //     vbSelectionType = "NONE";
    //}else{
    //     vbSelectionType = "NONE";
    //}
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "checked"         , {type:"XBOX",                     desc:""});
    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "orderNo"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"促銷單號"      });
	vat.item.make(vnB_Detail, "itemCodePmo"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"促銷品號"      });
	vat.item.make(vnB_Detail, "lastUpdatedBy", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail, "beginDate"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"啟用日期"   });
	vat.item.make(vnB_Detail, "lineId"      	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "orderNo2"      	, {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["lineId"],
														//pickAllService      : function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}

function detailInitial2(){
	//商品  	
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    //var vbSelectionType = "CHECK";
	//if(vatPickerId != null && vatPickerId != ""){
    //     vat.item.make(vnB_Detail2, "checkbox" , {type:"XBOX",                     desc:"全選"});
    //     vbSelectionType = "CHECK";
    //}else{
    //     vbSelectionType = "NONE";
    //}
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail2, "checked2"         , {type:"XBOX",                     desc:""});
    vat.item.make(vnB_Detail2, "indexNo2"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail2, "itemCode"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"商品品號"      });
	vat.item.make(vnB_Detail2, "itemCName"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"商品名稱"      });
	vat.item.make(vnB_Detail2, "unitPrice"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"售價"      });	
	vat.item.make(vnB_Detail2, "lastUpdatedBy2", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail2, "lastUpdateDate2"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail2, "itemCode1"      	, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail2, {
														id                  : "vatDetailDiv2",
														pageSize            : 10,
														searchKey           : ["itemCode"],
														//pickAllService      : function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}

function detailInitial3(){
  	//國際碼
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    //var vbSelectionType = "CHECK";
	//if(vatPickerId != null && vatPickerId != ""){
    //     vat.item.make(vnB_Detail3, "checkbox" , {type:"XBOX",                     desc:""});
    //     vbSelectionType = "CHECK";
    //}else{
    //     vbSelectionType = "NONE";
    //}
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail3, "checked3"         , {type:"XBOX",                     desc:""});
    vat.item.make(vnB_Detail3, "indexNo3"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail3, "eanCode"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"國際碼"      });
		vat.item.make(vnB_Detail3, "itemCode3"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"商品品號"      });
	vat.item.make(vnB_Detail3, "lastUpdatedBy3", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail3, "lastUpdateDate3"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail3, "eanCode"      	, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail3, {
														id                  : "vatDetailDiv3",
														pageSize            : 10,
														searchKey           : ["eanCode"],
														//pickAllService      : function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}

function detailInitial4(){
  	//會員資料
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    //var vbSelectionType = "CHECK";
	//if(vatPickerId != null && vatPickerId != ""){
    //     vat.item.make(vnB_Detail4, "checkbox" , {type:"XBOX",                     desc:"全選"});
    //     vbSelectionType = "CHECK";
    //}else{
    //     vbSelectionType = "NONE";
    //}
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail4, "checked4"         , {type:"XBOX",                     desc:""});
    vat.item.make(vnB_Detail4, "indexNo4"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail4, "cardNo"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"會員卡號"      });
	vat.item.make(vnB_Detail4, "vipName"		, {type:"TEXT" , size:18, maxLen:60, mode:"READONLY", desc:"客戶名稱"      });
	vat.item.make(vnB_Detail4, "lastUpdatedBy4", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail4, "lastUpdateDate4"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail4, "vipCode"      	, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail4, {
														id                  : "vatDetailDiv4",
														pageSize            : 10,
														searchKey           : ["cardNo"],
														//pickAllService      : function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}

function detailInitial5(){
  	
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    //var vbSelectionType = "CHECK";
	//if(vatPickerId != null && vatPickerId != ""){
    //     vat.item.make(vnB_Detail5, "checkbox" , {type:"XBOX",                     desc:"全選"});
    //     vbSelectionType = "CHECK";
    //}else{
    //     vbSelectionType = "NONE";
    //}
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail5, "checked5"         , {type:"XBOX",                     desc:""});
    vat.item.make(vnB_Detail5, "indexNo5"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail5, "employeeCode"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"員工工號"      });
	vat.item.make(vnB_Detail5, "employeeName"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"員工姓名"      });
	vat.item.make(vnB_Detail5, "lastUpdatedBy5", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail5, "lastUpdateDate5"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail5, "employeeCode"      	, {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail5, {
														id                  : "vatDetailDiv5",
														pageSize            : 10,
														searchKey           : ["employeeCode"],
														//pickAllService      : function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}

function detailInitial6(){

  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
  
  vat.item.make(vnB_Detail6, "checked6"         , {type:"XBOX",                     desc:""});
  vat.item.make(vnB_Detail6, "indexNo6"         , {type:"IDX"  ,                     desc:"序號"      });
  vat.item.make(vnB_Detail6, "vipTypeCode"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"卡別代碼"      });
  vat.item.make(vnB_Detail6, "reserve1"		, {type:"TEXT" , size:20, maxLen:20, mode:"READONLY", desc:"卡別名稱"      });
  vat.item.make(vnB_Detail6, "vipTypeCode"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"商品折扣代碼"      });
  vat.item.make(vnB_Detail6, "reserve1"		, {type:"TEXT" , size:20, maxLen:20, mode:"READONLY", desc:"商品折扣名稱"      });
  vat.item.make(vnB_Detail6, "beginDate"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"起始日期"      });
  vat.item.make(vnB_Detail6, "endDate"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"結束日期"      });
  vat.item.make(vnB_Detail6, "endDate"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"折扣比率"      });
  vat.item.make(vnB_Detail6, "lastUpdatedBy6", {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"更新人員"      });
  vat.item.make(vnB_Detail6, "lastUpdateDate6"  , {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"更新日期"   });
  vat.block.pageLayout(vnB_Detail6, {
														id                  : "vatDetailDiv6",
														pageSize            : 10,
														searchKey           : ["brandCode","vipTypeCode","itemDiscountType"],
														//pickAllService      : function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});
}

function detailInitial7(){

  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
  
  vat.item.make(vnB_Detail7, "checked7"         , {type:"XBOX",                     desc:""});
  vat.item.make(vnB_Detail7, "indexNo7"         , {type:"IDX"  ,                     desc:"序號"      });
  vat.item.make(vnB_Detail7, "sourceCurrency"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"幣別"      });
  vat.item.make(vnB_Detail7, "currencyName"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"幣別名稱"      });
  vat.item.make(vnB_Detail7, "exchangeRate"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"匯率"      });
  vat.item.make(vnB_Detail7, "beginDate"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"啟用日期"      });
  vat.item.make(vnB_Detail7, "lastUpdatedBy"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"更新人員"      });
  vat.item.make(vnB_Detail7, "lastUpdateDate"		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"更新日期"      });
  vat.item.make(vnB_Detail7, "currencyEName"		, {type:"TEXT" , size:10, maxLen:20, mode:"HIDDEN", desc:"幣別英文名"      });
  vat.item.make(vnB_Detail7, "organizationCode"		, {type:"TEXT" , size:10, maxLen:20, mode:"HIDDEN", desc:"組織"      });
  vat.item.make(vnB_Detail7, "againstCurrency"		, {type:"TEXT" , size:10, maxLen:20, mode:"HIDDEN", desc:"目的幣別"      });
  vat.block.pageLayout(vnB_Detail7, {
														id                  : "vatDetailDiv7",
														pageSize            : 10,
														searchKey           : ["id.organizationCode","id.sourceCurrency","id.againstCurrency","id.beginDate"],
														//pickAllService      : function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});
}



function changeShopCode(){
	var shopCode = vat.item.getValueByName("#F.shopCode").replace(/^\s+|\s+$/, '');
    vat.item.setValueByName("#F.shopCode", shopCode);
       vat.ajax.XHRequest(
       {
           post:"process_object_name=posDUService"+
                    "&process_object_method_name=getShopMachineForAJAX"+
                    "&shopCode=" + shopCode +
                    "&brandCode=" + document.forms[0]["#loginBrandCode" ].value,
           find: function changeShopCodeRequestSuccess(oXHR){ 
               	vat.item.SelectBind(eval(vat.ajax.getValue("allBuShopMachine", oXHR.responseText)),{ itemName : "#F.posMachineCode" });
           }   
       });
}

	// 送出,暫存按鈕
function doSubmit(){
	var alertMessage ="是否確定送出?";
		
	if(confirm(alertMessage)){
	    vat.block.submit(function(){return "process_object_name=posDUAction"+
			"&process_object_method_name=performDownloadTransaction";}, {bind:true, link:true, other:true
			, funcSuccess:function(){
			}});
	}	
}

function lockCol(){
	var vType = vat.item.getValueByName("#F.downloadFunction");
	if (vType == 'RATE') {
	    vat.tabm.displayToggle(0, "xTab1", false, false, false);
		vat.tabm.displayToggle(0, "xTab2", false, false, false);
		vat.tabm.displayToggle(0, "xTab3", false, false, false);
		vat.tabm.displayToggle(0, "xTab4", false, false, false);
		vat.tabm.displayToggle(0, "xTab5", false, false, false);
		vat.tabm.displayToggle(0, "xTab6", false, false, false);
		vat.tabm.displayToggle(0, "xTab7", true, false, false);
		vat.item.setAttributeByName("#F.dateStart", "readOnly", false);
		vat.item.setAttributeByName("#F.dateEnd", "readOnly", false);
		vat.item.setAttributeByName("#F.priceDate", "readOnly", false);
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.ItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.eanCode", "readOnly", true);
		vat.item.setAttributeByName("#F.vipCode", "readOnly", true);
		vat.item.setAttributeByName("#F.empNo", "readOnly", true);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
		vat.item.setAttributeByName("#F.LockDate", "readOnly", true);
	}else if(vType =="PMOS"){
		vat.tabm.displayToggle(0, "xTab1", true, false, false);
		vat.tabm.displayToggle(0, "xTab2", false, false, false);
		vat.tabm.displayToggle(0, "xTab3", false, false, false);
		vat.tabm.displayToggle(0, "xTab4", false, false, false);
		vat.tabm.displayToggle(0, "xTab5", false, false, false);
		vat.tabm.displayToggle(0, "xTab6", false, false, false);
		vat.tabm.displayToggle(0, "xTab7", false, false, false);
		vat.item.setAttributeByName("#F.dateStart", "readOnly", false);
		vat.item.setAttributeByName("#F.dateEnd", "readOnly", false);
		vat.item.setAttributeByName("#F.priceDate", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", false);
		vat.item.setAttributeByName("#F.promotionItemCode", "readOnly", false);
		vat.item.setAttributeByName("#F.ItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.eanCode", "readOnly", true);
		vat.item.setAttributeByName("#F.vipCode", "readOnly", true);
		vat.item.setAttributeByName("#F.empNo", "readOnly", true);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
		vat.item.setAttributeByName("#F.LockDate", "readOnly", true);
	}else if(vType =="ITEM"){
		vat.tabm.displayToggle(0, "xTab2", true, false, false);
		vat.tabm.displayToggle(0, "xTab1", false, false, false);
		vat.tabm.displayToggle(0, "xTab3", false, false, false);
		vat.tabm.displayToggle(0, "xTab4", false, false, false);
		vat.tabm.displayToggle(0, "xTab5", false, false, false);
		vat.tabm.displayToggle(0, "xTab6", false, false, false);
		vat.tabm.displayToggle(0, "xTab7", false, false, false);
		//vat.item.setStyleByName("#F.priceDate", "display", "none");
		vat.item.setAttributeByName("#F.dateStart", "readOnly", false);
		vat.item.setAttributeByName("#F.dateEnd", "readOnly", false);
		vat.item.setAttributeByName("#F.priceDate", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.ItemCode", "readOnly", false);
		vat.item.setAttributeByName("#F.eanCode", "readOnly", true);
		vat.item.setAttributeByName("#F.vipCode", "readOnly", true);
		vat.item.setAttributeByName("#F.empNo", "readOnly", true);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
		vat.item.setAttributeByName("#F.LockDate", "readOnly", true);
	}else if(vType =="DSC"){
		vat.tabm.displayToggle(0, "xTab6", true, false, false);
	  	vat.tabm.displayToggle(0, "xTab1", false, false, false);
	    vat.tabm.displayToggle(0, "xTab2", false, false, false);
	    vat.tabm.displayToggle(0, "xTab3", false, false, false);
	    vat.tabm.displayToggle(0, "xTab4", false, false, false);
	    vat.tabm.displayToggle(0, "xTab5", false, false, false); 
	    vat.tabm.displayToggle(0, "xTab7", false, false, false);
		vat.item.setAttributeByName("#F.dateStart", "readOnly", false);
		vat.item.setAttributeByName("#F.dateEnd", "readOnly", false);
		vat.item.setAttributeByName("#F.priceDate", "readOnly", true);
		//vat.item.setStyleByName("#F.priceDate", "display", "none");
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.ItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.eanCode", "readOnly", true);
		vat.item.setAttributeByName("#F.vipCode", "readOnly", true);
		vat.item.setAttributeByName("#F.empNo", "readOnly", true);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
		vat.item.setAttributeByName("#F.LockDate", "readOnly", true);
	}else if(vType =="EAN"){
		vat.tabm.displayToggle(0, "xTab3", true, false, false);
		vat.tabm.displayToggle(0, "xTab1", false, false, false);
		vat.tabm.displayToggle(0, "xTab2", false, false, false);
		vat.tabm.displayToggle(0, "xTab4", false, false, false);
		vat.tabm.displayToggle(0, "xTab5", false, false, false);
		vat.tabm.displayToggle(0, "xTab6", false, false, false);
		vat.tabm.displayToggle(0, "xTab7", false, false, false);
		vat.item.setAttributeByName("#F.dateStart", "readOnly", false);
		vat.item.setAttributeByName("#F.dateEnd", "readOnly", false);
		vat.item.setAttributeByName("#F.priceDate", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.ItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.eanCode", "readOnly", false);
		vat.item.setAttributeByName("#F.vipCode", "readOnly", true);
		vat.item.setAttributeByName("#F.empNo", "readOnly", true);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
		vat.item.setAttributeByName("#F.LockDate", "readOnly", true);
	}else if(vType =="VIP"){
		vat.tabm.displayToggle(0, "xTab4", true, false, false);
		vat.tabm.displayToggle(0, "xTab1", false, false, false);
		vat.tabm.displayToggle(0, "xTab2", false, false, false);
		vat.tabm.displayToggle(0, "xTab3", false, false, false);
		vat.tabm.displayToggle(0, "xTab5", false, false, false);
		vat.tabm.displayToggle(0, "xTab6", false, false, false);
		vat.tabm.displayToggle(0, "xTab7", false, false, false);
		vat.item.setAttributeByName("#F.dateStart", "readOnly", false);
		vat.item.setAttributeByName("#F.dateEnd", "readOnly", false);
		vat.item.setAttributeByName("#F.priceDate", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.ItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.eanCode", "readOnly", true);
		vat.item.setAttributeByName("#F.vipCode", "readOnly", false);
		vat.item.setAttributeByName("#F.empNo", "readOnly", true);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
		vat.item.setAttributeByName("#F.LockDate", "readOnly", true);
	}else if(vType =="EMP"){
		vat.tabm.displayToggle(0, "xTab5", true, false, false);
		vat.tabm.displayToggle(0, "xTab1", false, false, false);
		vat.tabm.displayToggle(0, "xTab2", false, false, false);
		vat.tabm.displayToggle(0, "xTab3", false, false, false);
		vat.tabm.displayToggle(0, "xTab4", false, false, false);
		vat.tabm.displayToggle(0, "xTab6", false, false, false);
		vat.tabm.displayToggle(0, "xTab7", false, false, false);
		vat.item.setAttributeByName("#F.dateStart", "readOnly", false);
		vat.item.setAttributeByName("#F.dateEnd", "readOnly", false);
		vat.item.setAttributeByName("#F.priceDate", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.ItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.eanCode", "readOnly", true);
		vat.item.setAttributeByName("#F.vipCode", "readOnly", true);
		vat.item.setAttributeByName("#F.empNo", "readOnly", false);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
		vat.item.setAttributeByName("#F.LockDate", "readOnly", true);
	}else if(vType =="LOCK"){
	  	vat.tabm.displayToggle(0, "xTab1", false, false, false);
	    vat.tabm.displayToggle(0, "xTab2", false, false, false);
	    vat.tabm.displayToggle(0, "xTab3", false, false, false);
	    vat.tabm.displayToggle(0, "xTab4", false, false, false);
	    vat.tabm.displayToggle(0, "xTab5", false, false, false); 
	    vat.tabm.displayToggle(0, "xTab6", false, false, false);
	    vat.tabm.displayToggle(0, "xTab7", false, false, false);
		vat.item.setAttributeByName("#F.dateStart", "readOnly", true);
		vat.item.setAttributeByName("#F.dateEnd", "readOnly", true);
		vat.item.setAttributeByName("#F.priceDate", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.ItemCode", "readOnly", true);
		vat.item.setAttributeByName("#F.eanCode", "readOnly", true);
		vat.item.setAttributeByName("#F.vipCode", "readOnly", true);
		vat.item.setAttributeByName("#F.empNo", "readOnly", true);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
		vat.item.setAttributeByName("#F.LockDate", "readOnly", false);
	}
	
	resetForm();
}

function loadBeforeAjxService(){
   // alert("loadBeforeAjxService");	
   
	var processString = "process_object_name=posDUService"+
			                    "&process_object_method_name=getPOSDownSearch" + 
	                  "&promotionItemCode"       	+ "=" + vat.item.getValueByName("#F.promotionItemCode"   ) +
	                  "&promotionCode"    	+ "=" + vat.item.getValueByName("#F.promotionCode"  )+
	                  "&dataType"    	+ "=" + vat.item.getValueByName("#F.downloadFunction"  )+
	                  "&ItemCode"    	+ "=" + vat.item.getValueByName("#F.ItemCode"  )+
	                  "&eanCode"    	+ "=" + vat.item.getValueByName("#F.eanCode"  )+
	                  "&vipCode"    	+ "=" + vat.item.getValueByName("#F.vipCode"  )+
	                  "&empNo"    	+ "=" + vat.item.getValueByName("#F.empNo"  )+
	                  "&dateStart"     + "=" + vat.item.getValueByName("#F.dateStart"  )+
	                  "&dateEnd"     + "=" + vat.item.getValueByName("#F.dateEnd"  )+
	                  "&priceDate"     + "=" + vat.item.getValueByName("#F.priceDate"  )+
	                  "&selectall=N";    
	                                                                                              
	return processString;											
}

function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {
			                     if("PMOS" == vat.item.getValueByName("#F.downloadFunction")){
			                     		vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);
			                     }else if("ITEM" == vat.item.getValueByName("#F.downloadFunction")){
			                     		vat.block.pageDataLoad(vnB_Detail2 , vnCurrentPage = 1);
			                     }else if("EAN" == vat.item.getValueByName("#F.downloadFunction")){
			                     		vat.block.pageDataLoad(vnB_Detail3 , vnCurrentPage = 1);
			                     }else if("VIP" == vat.item.getValueByName("#F.downloadFunction")){
			                     		vat.block.pageDataLoad(vnB_Detail4 , vnCurrentPage = 1);
			                     }else if("EMP" == vat.item.getValueByName("#F.downloadFunction")){
			                     		vat.block.pageDataLoad(vnB_Detail5 , vnCurrentPage = 1);
			                     }else if("DSC" == vat.item.getValueByName("#F.downloadFunction")){
			                     		vat.block.pageDataLoad(vnB_Detail6 , vnCurrentPage = 1);
			                     }else if("RATE" == vat.item.getValueByName("#F.downloadFunction")){
			                     		vat.block.pageDataLoad(vnB_Detail7 , vnCurrentPage = 1);
			                     }
			                     }
			                    });
			                    	                    
}

function doSelectSubmit(){
    if (""!= vat.item.getValueByName("#F.downloadCount"))
    {
		var vType = vat.item.getValueByName("#F.downloadFunction");
		if(vType == "PMOS"){
	        vat.block.pageSearch(vnB_Detail, {
	    		funcSuccess : 
	    		    function(){
	    		      vat.block.submit(
	    		                     function(){ return "process_object_name=posDUAction&process_object_method_name=performSelectDownload";
	    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
	    			}}); 
	
	    }else if(vType == "ITEM"){
	        vat.block.pageSearch(vnB_Detail2, {
	    		funcSuccess : 
	    		    function(){
	    		      vat.block.submit(
	    		                     function(){ return "process_object_name=posDUAction&process_object_method_name=performSelectDownload";
	    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail2} );
	    			}}); 
	
	    }else if(vType == "EAN"){
	        vat.block.pageSearch(vnB_Detail3, {
	    		funcSuccess : 
	    		    function(){
	    		      vat.block.submit(
	    		                     function(){ return "process_object_name=posDUAction&process_object_method_name=performSelectDownload";
	    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail3} );
	    			}}); 
	    }else if(vType == "VIP"){
	        vat.block.pageSearch(vnB_Detail4, {
	    		funcSuccess : 
	    		    function(){
	    		      vat.block.submit(
	    		                     function(){ return "process_object_name=posDUAction&process_object_method_name=performSelectDownload";
	    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail4} );
	    			}}); 
	
	    }else if(vType == "EMP"){
	        vat.block.pageSearch(vnB_Detail5, {
	    		funcSuccess : 
	    		    function(){
	    		      vat.block.submit(
	    		                     function(){ return "process_object_name=posDUAction&process_object_method_name=performSelectDownload";
	    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail5} );
	    			}}); 
	
	    }else if(vType == "DSC"){
	        vat.block.pageSearch(vnB_Detail6, {
	    		funcSuccess : 
	    		    function(){
	    		      vat.block.submit(
	    		                     function(){ return "process_object_name=posDUAction&process_object_method_name=performSelectDownload";
	    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail6} );
	    			}}); 
  		}else if(vType == "RATE"){
	        vat.block.pageSearch(vnB_Detail7, {
	    		funcSuccess : 
	    		    function(){
	    		      vat.block.submit(
	    		                     function(){ return "process_object_name=posDUAction&process_object_method_name=performSelectDownload";
	    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail7} );
	    			}}); 
  		}
    }
    else
    {
        alert("請輸入下傳筆數！");
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

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	var processString = "";
	var vType = vat.item.getValueByName("#F.downloadFunction");
	if (checkEnableLine()) {
		if(vType == "PMOS"){
	 		 processString = "process_object_name=posDUService&process_object_method_name=savePMOSSearchResult";
	    }else if(vType == "ITEM"){
			 processString = "process_object_name=posDUService&process_object_method_name=saveItemSearchResult";
	    }else if(vType == "EAN"){
			processString = "process_object_name=posDUService&process_object_method_name=saveEANSearchResult";
	    }else if(vType == "VIP"){
	   		processString = "process_object_name=posDUService&process_object_method_name=saveVIPSearchResult";
	    }else if(vType == "EMP"){
	    	processString = "process_object_name=posDUService&process_object_method_name=saveEMPSearchResult";
	    }else if(vType == "DSC"){
	    	processString = "process_object_name=posDUService&process_object_method_name=saveDSCSearchResult";
	    }else if(vType == "RATE"){
	    	processString = "process_object_name=posDUService&process_object_method_name=saveRATESearchResult";
	    }
	}
	return processString;
}

function loadSuccessAfter(){
	var vType = vat.item.getValueByName("#F.downloadFunction");
	var tab = 2;
		if(vType == "PMOS"){
	 		tab = 2;
	    }else if(vType == "ITEM"){
			tab = 3;
	    }else if(vType == "EAN"){
			tab = 4;
	    }else if(vType == "VIP"){
	   		tab = 5;
	    }else if(vType == "EMP"){
	    	tab = 6;
	    }else if(vType == "DSC"){
	    	tab = 7;
	    }else if(vType == "RATE"){
	    	tab = 8;
	    }
	if( vat.block.getGridObject(tab).dataCount == vat.block.getGridObject(tab).pageSize &&
	    vat.block.getGridObject(tab).lastIndex == 1){
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

// 清除
function resetForm(){
        vat.item.setValueByName("#F.promotionCode", "");
        vat.item.setValueByName("#F.promotionItemCode", "");
        vat.item.setValueByName("#F.ItemCode", "");
        vat.item.setValueByName("#F.eanCode", "");
        vat.item.setValueByName("#F.vipCode","");
        vat.item.setValueByName("#F.dateStart", "");
        vat.item.setValueByName("#F.dateEnd", "");
        vat.item.setValueByName("#F.priceDate", "");
        vat.item.setValueByName("#F.shopCode","");
        vat.item.setValueByName("#F.posMachineCode","");
        vat.item.setValueByName("#F.empNo", "");
        vat.item.setValueByName("#F.LockDate", "");
        vat.item.setValueByName("#F.downloadCount", "");
}

function numcheck(){

    if ("" !=vat.item.getValueByName("#F.downloadCount")){
        var re = /^[0-9]+$/;
        var keyValue = vat.item.getValueByName("#F.downloadCount");
   
        if (!re.test(keyValue)){
           vat.item.setValueByName("#F.downloadCount","");
           alert("下傳筆數只能輸入數字！"); 
        } 
    }
 } 
