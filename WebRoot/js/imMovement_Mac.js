/*** 
 *	檔案: imMovement.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.enable();
var afterSavePageProcess = "";

function kweImBlock(){
	if(typeof vat.bean !== 'undefined'){	
		vat.bean.init("process_test=mac");
	}
  kweButtonLine();	
  kweImHeader();
  if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");                                                                                                                                                                                                                                          
		vat.tabm.createButton(0 ,"xTab1","主檔資料"   ,"vatBlock_Master"  			,"images/tab_master_data_dark.gif"    		,"images/tab_master_data_light.gif", false, "doPageDataSave()");                                                                                                                                                
		vat.tabm.createButton(0 ,"xTab2","明細檔資料" ,"vatBlock_Detail"     		,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false, "doPageRefresh()");                                                                                                                                              
		vat.tabm.createButton(0 ,"xTab3","其他附件"   ,"vatBlock_Test"  				,"images/tab_other_attachment_dark.gif"   ,"images/tab_other_attachment_light.gif");                                                                                                                                 
		vat.tabm.createButton(0 ,"xTab5","簽核資料"   ,"vatApprovalDiv"        ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif");
//	vat.tabm.createButton(0 ,"xTab6","動態加會簽" ,"vatDynamicApprovalDiv"	,"images/tab_dynamic_approval_dark.gif"   ,"images/tab_dynamic_approval_light.gif", "none");
  }  
  kweImMaster();
	kweImDetail();
  kweImTest(); 	
// sample:
	var w = vat.bean("getLastUpdateDate");
	var x = {'x1':'123', 'x2':vat.bean().getLastUpdateDate};
	vat.bean.sets(x);
	
	vat.bean().getLastUpdateDate = "2010/01/01";
	vat.bean().test = {'aa': {'bb':{'cc':'c2222', 'dd':'d2222'}, 'dd':{'ee':'e1111', 'ff':'f1111'}}, 'bb':'b1111', 'cc':'c1111'};
	// alert(vat.bean('dd')); result {object}
	// alert(vat.bean('cc')); result c1111
	var z = vat.item.getGridValueByName("F02", 2);
	var z1 = vat.item.getValueByName("#F.orderType");	// 2
	vat.item.setValueById("#F.remark1", "12345");			// type is text
	vat.item.setValueByName("#F.orderType", "2");			// type is select
	vat.item.setGridValueByName("F02", 2, "111002");	// type is grid text
	var z2 = vat.item.getValueByName("#F.isCurrectWarehouseCode");	//
	// alert("typeof isCurrentWarehouseCode: "+(typeof z2)+" : "+z2);	
	// alert(z); 	
	// vat.$("vatF#B0A#Y1#X14").value = y;
	// vat.item();
	// alert("this.caller:"+this.caller);

}

function test1(){
	return;
	// vat.item.setGridAttributeByName("F02", "disabled", false);
	vat.item.setGridAttributeByName("F02", "readOnly", false);
	vat.item.setGridAttributeByName("F04", "readOnly", false);
	vat.item.setAttributeByName("#F.orderType", "readOnly", false);
	vat.item.setAttributeByName("#F.brandName", "readOnly", true);
	vat.item.setGridStyleByName("F03", "display", "none");
	vat.item.setGridStyleByName("F06", "backgroundColor", "red");
	vat.item.setStyleByName("#F.brandCode", "visibility", "visible");
}

function test3(){
	vat.item.moveGridByName("F07", "F04");
}

function test4(){
	vat.item.moveGridByName("F04", "F07");
}
function picker3(){
//var vaOpt1 = [["#form.orderType[3]", "x", true], ["一般111", "唯讀111", "提示111"], ["1", "2", "3"]];
alert('alert3 readonly');
//vat.item.SelectBind(vaOpt1);
vat.item.setAttributeByName("#F.orderType", "readOnly", true);
}

function picker4(){
alert('alert3-1 r/w');
vat.item.setAttributeByName("#F.orderType", "readOnly", false);
}

function test2(){
	return;
	// vat.item.setGridAttributeByName("F02", "disabled", true);
	vat.item.setGridAttributeByName("F02", "readOnly", true);
	vat.item.setGridAttributeByName("F04", "readOnly", true);
	vat.item.setAttributeByName("#F.orderType", "readOnly", true);
	vat.item.setGridStyleByName("F03", "display", "inline");
	vat.item.setGridStyleByName("F06", "backgroundColor", "green");
	vat.item.setStyleByName("#F.brandCode", "visibility", "hidden");
}

function kweImHeader(){ 
var vaOpt1 = [["#searchCustomerType", "x", true], ["一般 ", "唯讀", "提示"], ["1", "2", "3"]];

vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"調撥單維護作業", 
	rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderType", type:"LABEL" , value:"單別"}]},	 
	 {items:[{name:"#F.orderType", type:"RADIO",  bind:"orderTypeCode", size:1, init:vaOpt1, eChange:"test1()"}]},		 
	 {items:[{name:"#L.orderNo"  , type:"LABEL" , value:"單號 "}]},
	 {items:[{name:"#F.orderNo"  , type:"TEXT"  ,  bind:"test1", size:12},
	 				 {name:"#F.headId"   , type:"TEXT"  ,  bind:"headId", mode:"READONLY"}]},
	 {items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},	 
	 {items:[{name:"#F.brandCode", type:"TEXT"  ,  bind:"brandCode", size:12, mode:"READONLY", init:"123456"},
	 				 {name:"#F.brandName", type:"TEXT"  ,  bind:"BrandName", size:12}]},
	 {items:[{name:"#L.status"   , type:"LABEL" , value:"狀態"}]},
	 {items:[{name:"#F.status"   , type:"PICKER", 	src:"images/button_pick_item_data.gif",
	 																					 openMode:"open", 
	 																						service:"Im_Movement:search:20090720.page",
	 																	 serviceAfterPick:function(){alert('Success !!, timeScope = '+ vat.bean().vatBeanPicker.timeScope)},
	 																	 servicePassData :function(){return "&test1=123&test2=abc" },																						
	 																					 		 left:100,
	 																					 		right:100,
	 																					 		width:1024,
	 																					 	 height:500,
	 																					 beanName:"imMovement"},
	 				{name:"#F.status0"  , type:"PICKER", 	src:"images/button_pick_item_data.gif",
	 																					 openMode:"open", 
	 																						service:"Im_Movement:search:20090720.page",
	 																	 serviceAfterPick:function(){alert('Success !!, timeScope = '+ vat.bean().vatBeanPicker.timeScope)},
	 																	 servicePassData: function(){return "&test1=123&test2=abc" },																						
	 																					 		 left:100,
	 																					 		right:100,
	 																					 		width:1024,
	 																					 	 height:500,
	 																					 beanName:"imMovement0"},	 																					 
					{name:"#F.status1" , type:"OPEN",      src:"images/button_advance_search_shrink.gif",
	 																					 openMode:"showModalDialog",
	 																					 openMode:"open", 
	 																						service:"Im_Movement:search:20090720.page",
	 																					 	   left:100,
	 																					 	  right:100,
	 																					 	  width:1024,
	 																					   height:500,	
	 																 	serviceBeforeOpen:function(){alert('Success !!, timeScope = '+ vat.bean().vatBeanPicker.timeScope)},
	 																					 beanName:"imMovement"
	 																						  },
	 				 {name:"#F.status2"  , type:"IMG",  src:"images/button_advance_search_shrink.gif", value:"測試按鈕1", size:12, eClick:"picker3()"},	 																									
	 				 {name:"#F.status3"  , type:"IMG",  src:"images/button_advance_search_extend.gif", value:"測試按鈕2", size:12, eClick:"picker4()"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark1", type:"LABEL", value:"備註一"}]},
	 {items:[{name:"#F.remark1", type:"TEXTAREA",   bind:"remark1", row:3, col:60, desc:"放一般備註內容", ceap:"#form.remark1", eMouseIn:"test2()", eMouseOut:"test1()"}], td:" colSpan=3"},
	 {items:[{name:"#L.LastBy" , type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.LastBy" , type:"TEXT",   bind:"getLastUpdatedBy",  mode:"READONLY", size:12}]},	 
	 {items:[{name:"#L.LastDt" , type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.LastDt" , type:"DATE",   bind:"getLastUpdateDate", mode:"READONLY", size:12, mode:"READONLY"}]}]},
	function xxxx(){
		if(false){
			return {
	 		row_style:"", 
	 		cols:[
	 		{items:[{name:"#L.remark1", type:"LABEL", value:"備註一"}]},
			{items:[{name:"#F.remark1", type:"TEXTAREA",   bind:"remark1", row:3, col:60, desc:"放一般備註內容", ceap:"#form.remark1", eMouseIn:"test2()", eMouseOut:"test1()"}], td:" colSpan=3"},
	 		{items:[{name:"#L.LastBy" , type:"LABEL", value:"填單人員"}]},
	 		{items:[{name:"#F.LastBy" , type:"TEXT",   bind:"getLastUpdatedBy",  mode:"READONLY", size:12}]},	 
	 		{items:[{name:"#L.LastDt" , type:"LABEL", value:"填單日期"}]},
 	 		{items:[{name:"#F.LastDt" , type:"DATE",   bind:"getLastUpdateDate", mode:"READONLY", size:12, mode:"READONLY"}]}]
 	 		}
 	 	}else{
 	 		return {};
 	 	}
 	 }()
 	 ],
		beginService:"",
		closeService:""			
	});
}

function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-bottom-color: #cccccc; border-top-color: #eeeeee; border-right-color: #cccccc; border-left-color: #eeeeee;'",	
	title:"", rows:[  
	 {row_style:" style='background-color:#eeeeee;' ", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"kweImInitial()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"IMG"    , value:"查詢",  src:"./images/button_find.gif"  , eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG" ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG" ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doMask("./images/loading.gif")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print"       , type:"IMG" ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:"openReportWindow()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export"      , type:"IMG" ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import"      , type:"IMG" ,value:"明細匯入", 	src:"./images/button_detail_import.gif"}],td:"style='border-color:#eeeeee; border-width: 5px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function doMask(psBtnId, psSrc){
}

function kweImMaster(){
var vaOpt1 = [["#searchCustomerType", "x", true], ["一般 ", "唯讀", "提示"], ["1", "2", "3"]];
var vaOpt2 = [["", "x", true], ["一般 ", "唯讀", "提示"], ["1", "2", "3"]];
var vaOpt3 = [["", true, true], ["核准 ", "駁回"], [true, false]];
var now = new Date();
vat.block.create(vnB_master = 1, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3' style='background-color:#CCCCCC; width:850';",	
	rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryWarehouseCode"    , type:"LABEL" , value:"轉出倉庫<font color='red'>*</font>"}]},	 
	 {items:[{name:"#F.deliveryWarehouseCode"    , type:"SELECT",  bind:"deliveryWarehouseCode", size:12, init:vaOpt1},
					 {name:"#F.isCurrectWarehouseCode"   , type:"RADIO",   bind:"isCurrectDeliveryWarehouseCode", size:12, init:vaOpt3},
					 {name:"#L.deliveryWarehouseCode"    , type:"LABEL",  value:"--"},
					 {name:"#F.deliveryWarehouseCode"    , type:"TEXT",    bind:"deliveryWarehouseCode"},
	 				 {name:"#F.deliveryWarehouseName"    , type:"TEXT",    bind:"deliveryWarehouseName", size:12, mode:"READONLY"}]},		 
	 {items:[{name:"#L.deliveryDate"             , type:"LABEL",  value:"轉出日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.deliveryDate"             , type:"LABEL",  value: formatDate(now, "yyyy/MM/dd") , size:12}]},
	 {items:[{name:"#L.deliveryContactPerson"    , type:"LABEL" , value:"倉管人員"}]},	 
	 {items:[{name:"#F.deliveryContactPerson"    , type:"CHECKBOX",bind:"deliveryContactPerson", size:12, mode:"READONLY", init:"Y", eClick:"alert('hello !!')"},
	 				 {name:"#F.deliveryContactPersonName", type:"TEXT"  ,  bind:"deliveryContactPersonName", back:false, size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryAddress", type:"LABEL", value:"出貨地址"}]},
	 {items:[{name:"#L.deliveryCity"   , type:"LABEL", value:"城市:&nbsp&nbsp;"},
	 				 {name:"#F.deliveryCity"   , type:"TEXT" ,  bind:"deliveryCity", size:10},
					 {name:"#L.deliveryArea"   , type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
	 				 {name:"#F.deliveryArea"   , type:"TEXT",   bind:"deliveryArea", size:10},
	 				 {name:"#L.deliveryZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
	 				 {name:"#F.deliveryZipCode", type:"TEXT",   bind:"deliveryZipCode", size:5},
 					 {name:"#F.deliveryAddress", type:"TEXT",   bind:"deliveryAddress", size:70, maxLen:200}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.arrivalWarehouseCode"    , type:"LABEL" , value:"轉入倉庫<font color='red'>*</font>"}]},	 
	 {items:[{name:"#F.arrivalWarehouseCode"    , type:"SELECT",  bind:"arrivalWarehouseCode", size:12, init:vaOpt2, eChange:""},
					 {name:"#F.isCurrectWarehouseCode1"  , type:"TEXT",   bind:"isCurrectarrivalWarehouseCode", size:12, mode:"READONLY"},
					 {name:"#F.arrivalWarehouseCode"    , type:"TEXT",    bind:"arrivalWarehouseCode", size:12, mode:"READONLY"},
	 				 {name:"#F.arrivalWarehouseName"    , type:"TEXT",    bind:"arrivalWarehouseName", size:12, mode:"READONLY"}]},		 
	 {items:[{name:"#L.arrivalDate"             , type:"LABEL" , value:"轉入日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.arrivalDate"             , type:"DATE"  ,  bind:"arrivalDate", size:12}]},
	 {items:[{name:"#L.arrivalContactPerson"    , type:"LABEL" , value:"倉管人員"}]},	 
	 {items:[{name:"#F.arrivalContactPerson"    , type:"TEXT"  ,  bind:"arrivalContactPerson", size:12, mode:"READONLY"},
	 				 {name:"#F.arrivalContactPersonName", type:"TEXT"  ,  bind:"arrivalContactPersonName", size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[ 					 
	 {items:[{name:"#L.arrivalAddress", type:"LABEL", value:"到貨地址"}]},
	 {items:[{name:"#L.arrivalCity"   , type:"LABEL", value:"城市:&nbsp&nbsp;"},
	 				 {name:"#F.arrivalCity"   , type:"TEXT" ,  bind:"arrivalCity", size:10},
	 				 {name:"#L.arrivalArea"   , type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
	 				 {name:"#F.arrivalArea"   , type:"TEXT",   bind:"arrivalArea", size:10},
	 				 {name:"#L.arrivalZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
	 				 {name:"#F.arrivalZipCode", type:"TEXT",   bind:"arrivalZipCode", size:5},
 					 {name:"#F.arrivalAddress", type:"TEXT",   bind:"arrivalAddress", size:70, maxLen:200}], td:" colSpan=5"}]},
	 {row_style:" style='background-color:#CCFF66;' ", cols:[
	 {items:[{name:"#L.originalOrderTypeCode", type:"LABEL", value:"來源單別"}]},
	 {items:[{name:"#F.originalOrderTypeCode", type:"TEXT",   bind:"originalOrderTypeCode", size:12}]},
	 {items:[{name:"#L.originalOrderNo"      , type:"LABEL", value:"來源單號"}]},
 	 {items:[{name:"#F.originalOrderNo"      , type:"TEXT",   bind:"originalOrderNo"      , size:12}], td:" colSpan=3 style='background-color:#99FFCC;'"}]}, 					 
	 {row_style:"", cols:[
	 {items:[{name:"#L.transport"     , type:"LABEL", value:"運輸方式"}]},
	 {items:[{name:"#F.transport"     , type:"TEXT",   bind:"transport", size:100, maxLen:200, desc:""}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark2"       , type:"LABEL", value:"備註二"}]},
	 {items:[{name:"#F.remark2"       , type:"TEXT",   bind:"remark2", size:100, maxLen:200, desc:""}], td:" colSpan=5"}]}
 	 	],
		beginService:"",
		closeService:""			
	});
}


function kweImTest(){
var vaData = [["", "x", true], ["一般 ", "唯讀", "提示"], ["1", "2", "3"]];
var vaOpt2 = [["", "x", true], ["一般 ", "唯讀", "提示"], ["1", "2", "3"]];

var voData = {"brandCode" : ["1", "1", "3", "1", "2"], "brandName" : ["AA", "BB", "CC", "DD", "EE"]};
//			[{brandCode: "1", brandName:"AA", status:"xx"}, {}, {}]
vat.bean.sets(voData);

vat.block.create(vnB_Test = 0, {
	id:"vatBlock_Test", layout:"grid:bean", extend:5, 
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"測試作業", 
		rows:[
			{cols:[
				{items:[{name:"#L.status"   , type:"LABEL" ,value:"狀態"}]},			
				{items:[{name:"#L.brandCode", type:"LABEL" ,value:"品牌代號"}]},
	 			{items:[{name:"#L.brandName", type:"LABEL" ,value:"品牌名稱"}]}
	 		]},
			{cols:[
				{items:[{name:"#F.status[0]"   , type:"CHECKBOX"  , bind:"status",        size:3, mode:"READONLY"}]},			
				{items:[{name:"#F.brandCode[0]", type:"SELECT", bind:"brandCode",     size:16, mode:"READONLY", init: vaOpt2}]},
	 			{items:[{name:"#F.brandName[0]", type:"TEXT"  , bind:"brandName",     size:60, mode:"READONLY"}]}
	 		]}
		],
		beginService:"",
		closeService:""			
	});
}

function kweImDetail(){
  var vsFormStatus = vat.$("#F.status").value;
	var	vbCanGridDelete = true;
	var	vbCanGridAppend = true;
	var vbCanGridModify = true;
  var vnB_Detail = 2;
  vat.item.make(vnB_Detail, "IDX", {type:"IDX"  , view: "fixed",                     desc:"序號", style:"backgroundColor=green"});
	vat.item.make(vnB_Detail, "F02", {type:"ALT" , view: "", size:15, maxLen:20, desc:"品號", style:"backgroundColor=green",	 mask:"CCCCCCCCCCCC", onchange:"changeItemData()", suggest:"process_test=suggest"});	
	vat.item.make(vnB_Detail, "F03", {type:"TEXT" , view: "", size:18, maxLen:20, desc:"品名", style:"backgroundColor=green", mode:"READONLY"});
	vat.item.make(vnB_Detail, "F04", {type:"DATE" , view: "", size: 8, maxLen:20, desc:"轉出庫別"});
	vat.item.make(vnB_Detail, "F05", {type:"TEXT" , view: "", size:12, maxLen:12, desc:"批號",					mask:"CCCCCCCCCCCC", onchange:function(){changeItemData();}             });
	vat.item.make(vnB_Detail, "F06", {type:"NUMB" , view: "shift", size:12, maxLen:12, desc:"目前庫存數量",		mode:"READONLY"}); 
	vat.item.make(vnB_Detail, "F07", {type:"NUMB" , view: "shift", size: 8, maxLen:12, desc:"預計轉出數量"});
	vat.item.make(vnB_Detail, "F08", {type:"NUMB" , view: "shift", size: 8, maxLen:12, desc:"轉出數量"});
	vat.item.make(vnB_Detail, "F09", {type:"TEXT" , view: "shift", size: 8, maxLen:20, desc:"轉入庫別",			mode:"READONLY"});
	vat.item.make(vnB_Detail, "F10", {type:"NUMB" , view: "shift", size: 8, maxLen:12, desc:"轉入數量",			mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "F11", {type:"ROWID"});
	vat.item.make(vnB_Detail, "F12", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "DEL", {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_Detail, "MSG", {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vnB_Detail, {
												id : "vatBlock_Detail",
												pageSize  : 10,
										/*	gridOverflow  : "scroll",	*/
												canGridDelete : vbCanGridDelete,
												canGridAppend : vbCanGridAppend,
												canGridModify : vbCanGridModify,
												searchKey	: ["head_id", "orderTypeNo"]
											});
	//vat.item.setGridStyleByName("IDX", "backgroundColor", "green");
	//vat.item.setGridStyleByName("F02", "backgroundColor", "green");
	//vat.item.setGridStyleByName("F05", "backgroundColor", "green");
	vat.item.setGridStyleByName("DEL", "backgroundColor", "red");
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
/*																												
							    					appendBeforeService : "kwePageAppendBeforeMethod()",
							    					appendAfterService  : "kwePageAppendAfterMethod()",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "kwePageLoadSuccess()",
														eventService        : "changeRelationData",   
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
*/	
}

function kwePageSaveMethod(){}					


function kwePageSaveSuccess(){
	// alert("更新成功");
}

function kwePageLoadSuccess(){
	// alert("載入成功");	
}

function kwePageAppendBeforeMethod(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}

function kwePageAppendAfterMethod(){
	// return alert("新增完畢");
}


function loadBeforeAjxService(){
	var processString = "process_object_name=imMovementService&process_object_method_name=getAJAXPageData" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value + 
	                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                    "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value +
	                    "&deliveryWarehouseCode=" + document.forms[0]["#form.deliveryWarehouseCode"].value + 
	                    "&arrivalWarehouseCode=" + document.forms[0]["#form.arrivalWarehouseCode"].value + 
	                    "&deliveryContactPerson=" + document.forms[0]["#form.deliveryContactPerson"].value + 
						"&arrivalContactPerson=" + document.forms[0]["#form.arrivalContactPerson"].value + 
	                    "&formStatus=" + document.forms[0]["#form.status"].value;
	return processString;											
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	return true;
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=imMovementService&process_object_method_name=updateAJAXPageLinesData" + 
		"&headId=" + document.forms[0]["#form.headId"].value + "&status=" + document.forms[0]["#form.status"].value;
	}
	return processString;
}								

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
    vat.block.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg === "") {
		if ("saveHandler" == afterSavePageProcess) {	
			executeCommandHandler("main", "saveHandler");
		} else if ("submitHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "submitHandler");
		} else if ("voidHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "voidHandler");
		} else if ("copyHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "copyHandler");
		} else if ("executeExport" == afterSavePageProcess) {
			//vat.block.pageRefresh(0);
			executeCommandHandlerNoBlock("main","exportDataHandler");
		} else if ("pageRefresh" == afterSavePageProcess) {
			//vat.block.pageRefresh(0);
		} else if ("totalCount" == afterSavePageProcess) {
		  
		}else if ("changeRelationData" == afterSavePageProcess) {
		    var processString = "process_object_name=imMovementService&process_object_method_name=updateItemRelationData" + 
	                    "&headId=" + document.forms[0]["#form.headId"].value + 
	                    "&brandCode=" + document.forms[0]["#form.brandCode"].value +
	                    "&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value +
	                    "&deliveryWarehouseCode=" + document.forms[0]["#form.deliveryWarehouseCode"].value + 
	                    "&arrivalWarehouseCode=" + document.forms[0]["#form.arrivalWarehouseCode"].value + 
	                    "&deliveryContactPerson=" + document.forms[0]["#form.deliveryContactPerson"].value + 
						"&arrivalContactPerson=" + document.forms[0]["#form.arrivalContactPerson"].value + 
	                    "&formStatus=" + document.forms[0]["#form.status"].value;
	        vat.ajax.startRequest(processString, function () {
				if (vat.ajax.handleState()) {
			  	vat.block.pageRefresh(0);
				}
			});	
		}
	}else{
		alert("錯誤訊息： " + errorMsg);
	}
	afterSavePageProcess = "";
}

/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
    if (confirm("是否確認暫存?")) {		
	    //save line
		vat.block.pageDataSave(0);
		afterSavePageProcess = "saveHandler";		
	}
}
function kweFocus(){
}
/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	if (confirm("是否確認送出?")) {		
		//save line
		vat.block.pageDataSave(0);
		afterSavePageProcess = "submitHandler";	
	}
}

/*
	作廢VOID HEAD && LINE
*/
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		vat.block.pageDataSave(0);
		afterSavePageProcess = "voidHandler";		
	}
}

/*
	COPY HEAD && LINE
*/
function doCopyHandler() {
	if (confirm("是否進行複製?")) {		
		//save line
		vat.block.pageDataSave(0);
		afterSavePageProcess = "copyHandler";		
	}
}

/*
	顯示合計的頁面
*/
function showTotalCountPage() {
    //save line
    vat.block.pageDataSave(0);
    afterSavePageProcess = "totalCount";	
}

/*
	匯出
*/
function doExport() {
	//save line
	vat.block.pageDataSave(0);
	afterSavePageProcess = "executeExport";
}

function doPageDataSave(){
    vat.block.pageDataSave(0);
}

function doPageRefresh(){
    vat.block.pageRefresh(0);
}



function changeItemData() {  
  var vLineId                 = vat.item.getGridLine();	
  var vItemCode               = vat.item.getGridValueByName("itemCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vLotNo                  = vat.item.getGridValueByName("lotNo").replace(/^\s+|\s+$/, '').toUpperCase();
  var vDeliveryWarehouseCode  = vat.item.getGridValueByName("deliveryWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vProcessSqlName         = vLotNo === "" ? "FindItemInformationWithoutLotNo" : "FindItemInformationWithLotNo";  
  var vDeliveryQuantity = 0;
  // alert('test item event');
  /*   
	vat.item.IdDataBind(0, 1, vItemCode);
	vat.item.IdDataBind(0, 3, vDeliveryWarehouseCode);
	var vProcessString =	"process_sql_code="+vProcessSqlName +"&"+                                                                                                                        
   										 	"brandCode=" + document.forms[0]["#brandCode"].value+ "&"+                                                                                                      
   											"itemCode="+vItemCode+"&"+                                                                          
   											"warehouseCode="+ vDeliveryWarehouseCode +"&"+                                                       
   											(vLotNo==""?"":"lotNo="+vLotNo+"&")+                                                                                                                              
   											"warehouseManager="+ document.forms[0]["#form.deliveryContactPerson"].value+"&"+                                                                                
   											"employeeCode="+  document.forms[0]["#employeeCode"].value; 

	vat.ajax.startRequest(vProcessString,  function() {                                                                                                                 
     if (vat.ajax.handleState()){                                                                                                                                     
         if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){     
	         	vat.item.IdDataBind(0, 1, vItemCode);   
  	       	vat.item.IdDataBind(0, 2, vat.ajax.getValue("ITEM_C_NAME", vat.ajax.xmlHttp.responseText));    
    	     	vat.item.IdDataBind(0, 3, vDeliveryWarehouseCode);  
            vat.item.IdDataBind(0, 4, vat.ajax.getValue("LOT_NO", vat.ajax.xmlHttp.responseText));                                                                                     
            vat.item.IdDataBind(0, 5, vat.ajax.getValue("CURRENT_ON_HAND_QTY", vat.ajax.xmlHttp.responseText));       
         }else{ 
            vat.item.IdDataBind(0, 2, "查無資料"); 
            vat.item.IdDataBind(0, 5, 0.0);                                                                
         }                                                                                                                                                            
     }                                                                                                                                                                
  } )
  */
}                                        



/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
}




function openReportWindow(encryText){
    var reportUrl = document.forms[0]["#reportUrl"].value;
    var reportFileName = document.forms[0]["#reportFileName"].value;  
    var brandCode = document.forms[0]["#form.brandCode"].value;
    var orderTypeCode = document.forms[0]["#form.orderTypeCode"].value;
    var orderNo = document.forms[0]["#form.orderNo"].value;
    var displayAmt = document.forms[0]["#displayAmt"].value;
    var url = "jsp/SalesOrderReport.jsp?reportUrl=" + reportUrl + "&reportFileName=" + reportFileName + "&crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1=" + orderTypeCode + "&prompt2=" + orderNo + "&prompt3=" + displayAmt;
    window.open(url,'BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
}

function changeRelationData(){
    afterSavePageProcess = "changeRelationData";
    vat.block.pageDataSave(0);
}
