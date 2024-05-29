/*** 
 *	檔案: inventoryCountsBatchCreate.js
 *	說明：盤點單批次建立
 *	修改：Jeremy
 *  <pre>
 *  	Created by jeremy
 *  	All rights reserved.
 *  </pre>
 */

vat.debug.disable();
var afterSavePageProcess = "";

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function kweImBlock(){
	formInitial();
	buttonLine();
	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0, "xTab1", "明細資料"	,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false, "" );
	}
	detailInitial();
	doFormAccessControl();
}

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther =
		{ 
          loginBrandCode  	 	: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          orderTypeCode      	: document.forms[0]["#orderTypeCode"].value,
          countsId      		: document.forms[0]["#countsId"].value,
          customsWarehouseCode  : document.forms[0]["#customsWarehouseCode"].value
		};
   		vat.bean.init(	
  			function(){
				return "process_object_name=imInventoryCountsAction&process_object_method_name=performListInitial"; 
    		},{
    			other: true
    		}
    	);    
  }
}

function buttonLine(){
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[//{name:"#B.clear"       , type:"IMG"    , value:"清除",  src:"./images/button_reset.gif"	, eClick:"resetForm()"},
	 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    , value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    , value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.message"  	, type:"IMG"    ,value:"訊息提示"	,   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"}	
	 			],td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}	

function headerInitial(){
	var allOrderTypes = vat.bean("allOrderTypes");
	var allCountsId = vat.bean("allCountsId");
	var allCustomsWarehouseCode = vat.bean("allCustomsWarehouseCode");
	var allWarehouseCode = vat.bean("allWarehouseCode");
	
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"盤點單清單建立",
		rows:[
			{row_style:"", cols:[		
	 			{items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別"}]},
	 			{items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", init:allOrderTypes, mode:"READONLY"}]},
	 			{items:[{name:"#L.taxName", type:"LABEL" , value:"稅別"}]},	 
				{items:[{name:"#F.taxTypeCode", type:"TEXT",  bind:"taxTypeCode", size:6, mode:"HIDDEN"},
						{name:"#F.taxName", type:"TEXT",  bind:"taxName", size:6, mode:"READONLY" }]},	 
				{items:[{name:"#L.brandName", type:"LABEL", value:"品牌" }] },
				{items:[{name:"#F.brandCode"   , type:"TEXT"  ,  bind:"brandCode", size:8, mode:"HIDDEN"},
						{name:"#F.brandName", type:"TEXT", bind:"brandName", size:20, mode:"READONLY"}]},
				{items:[{name:"#L.createdBy", type:"LABEL", value:"建立者"}]},
				{items:[{name:"#F.createdBy", type:"TEXT", bind:"createdBy", size:15, maxLen:15, mode:"READONLY"},
	         			{name:"#F.createdByName", type:"TEXT", bind:"createdByName", size:12, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[		
				{items:[{name:"#L.countsId", type:"LABEL" , value:"盤點代號"}]},
	 			{items:[{name:"#F.countsId", type:"SELECT", bind:"countsId", size:15, maxLen:15, init:allCountsId, eChange:'changeCountsId()'}]},
	 			{items:[{name:"#L.customsWarehouseCode", type:"LABEL" , value:"關別"}]},
	 			{items:[{name:"#F.customsWarehouseCode", type:"SELECT", bind:"customsWarehouseCode", size:15, maxLen:15, init:allCustomsWarehouseCode, eChange:'changeCountsId()'}]},
	         	{items:[{name:"#L.warehouseCode", type:"LABEL" , value:"庫別"}]},
	 			{items:[{name:"#F.warehouseCode", type:"SELECT", bind:"warehouseCode", size:15, maxLen:15, init:allWarehouseCode, eChange:'changeCountsId()'}], td:" colSpan=3"}	 			
			]}
		],
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
	// set column
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;
	vat.item.make(vnB_Detail, "indexNo",				{type:"IDX" , view: "fixed", desc:"項次"});
	vat.item.make(vnB_Detail, "warehouseCode",			{type:"TEXT", size:5, view: "fixed", maxLen:20, desc:"庫號", eChange:"onChangeWarehouseCode()"});
	vat.item.make(vnB_Detail, "warehouseName",			{type:"TEXT", size:10, view: "fixed", maxLen:20, desc:"庫名", mode:"readOnly"});
	vat.item.make(vnB_Detail, "customsWarehouseCode",	{type:"TEXT", size:5, view: "fixed", maxLen:20, desc:"關別", mode:"hidden"});
	vat.item.make(vnB_Detail, "categoryCode",			{type:"TEXT", size:5, view: "fixed", maxLen:20, desc:"儲區", mode:"hidden"});
	vat.item.make(vnB_Detail, "superintendentCode",		{type:"TEXT", size:10, view: "fixed", maxLen:20, desc:"盤點人工號", eChange:"onChangeSuperintendentCode()"});
	vat.item.make(vnB_Detail, "superintendentName",		{type:"TEXT", size:10, view: "fixed", maxLen:20, desc:"盤點人姓名", mode:"readOnly"});
	vat.item.make(vnB_Detail, "countsDate",				{type:"DATE", size:10, view: "fixed", maxLen:20, desc:"盤點日"});
	vat.item.make(vnB_Detail, "startNo",				{type:"NUMB", size:10, view: "fixed", maxLen:20, desc:"起始號碼"});
	vat.item.make(vnB_Detail, "endNo",					{type:"NUMB", size:10, view: "fixed", maxLen:20, desc:"結束號碼"});
	vat.item.make(vnB_Detail, "isLockRecord",			{type:"CHECKBOX", size:1, view: "fixed", maxLen:20, desc:"重置盤點單"});
	vat.item.make(vnB_Detail, "headId", 				{type:"ROWID"});
	vat.item.make(vnB_Detail, "isDeleteRecord", 		{type:"DEL", view: "fixed", desc:"刪除"});
	vat.item.make(vnB_Detail, "message", 				{type:"MSG"  , desc:"訊息"});
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
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()"
								});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function appendBeforeService(){
	return true;
}

function loadBeforeAjxService(){
	var processString = "process_object_name=imInventoryCountsService&process_object_method_name=getAJAXPageDataForList" + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&countsId=" + vat.item.getValueByName("#F.countsId") +
	                    "&customsWarehouseCode=" + vat.item.getValueByName("#F.customsWarehouseCode") +
	                    "&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode") +
	                    "&taxTypeCode=" + vat.item.getValueByName("#F.taxTypeCode");
	return processString;
}

function loadSuccessAfter(){

}

function saveBeforeAjxService() {
	processString = "process_object_name=imInventoryCountsService"+
					"&process_object_method_name=updateAJAXPageDataForList" + 
					"&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                "&countsId=" + vat.item.getValueByName("#F.countsId") +
	                "&taxTypeCode=" + vat.item.getValueByName("#F.taxTypeCode") +
	                "&customsWarehouseCode=" + vat.item.getValueByName("#F.customsWarehouseCode") +
	                "&employeeCode=" + document.forms[0]["#loginEmployeeCode" ].value;
	return processString;
}

function saveSuccessAfter(){
	if("SUBMIT" == afterSavePageProcess){
		vat.block.submit(
			function(){return "process_object_name=imInventoryCountsAction&process_object_method_name=performListTransaction";}, 
					{bind:true, link:true, other:true});
	}
	afterSavePageProcess = "";
}

function changeCountsId(){
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function onChangeWarehouseCode() {
	var nItemLine = vat.item.getGridLine();
	var warehouseCode = vat.item.getGridValueByName("warehouseCode",nItemLine).toUpperCase();
	vat.item.setGridValueByName("warehouseCode", nItemLine, warehouseCode);
	var customsWarehouseCode = vat.item.getValueByName("#F.customsWarehouseCode")
	if (warehouseCode != "") {
		var processString = "process_object_name=imInventoryCountsService&process_object_method_name=getWarehouseInfo" +
								"&brandCode="       + vat.item.getValueByName("#F.brandCode") +
								"&warehouseCode="	+ warehouseCode;
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					vat.item.setGridValueByName("warehouseName", nItemLine, vat.ajax.getValue("WarehouseName", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("customsWarehouseCode", nItemLine, vat.ajax.getValue("CustomsWarehouseCode", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("categoryCode", nItemLine, vat.ajax.getValue("CategoryCode", vat.ajax.xmlHttp.responseText));
					//if(customsWarehouseCode != vat.ajax.getValue("CustomsWarehouseCode", vat.ajax.xmlHttp.responseText)){
						//vat.item.setGridValueByName("message", nItemLine, "單頭關別與明細關別不相符");
					//}
				}
			}
		});
	} 
}


function onChangeSuperintendentCode(){

	var nItemLine = vat.item.getGridLine();
	var superintendent = vat.item.getGridValueByName("superintendentCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();    
    vat.item.setValueByName("superintendentCode", nItemLine, superintendent);   
    if(superintendent !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + superintendent,
           find: function changeSuperintendentRequestSuccess(oXHR){
               vat.item.setGridValueByName("superintendentCode", nItemLine, vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setGridValueByName("superintendentName", nItemLine, vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setGridValueByName("superintendentName", nItemLine, "");
    }
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

function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}
	if(confirm(alertMessage)){
		afterSavePageProcess = formAction;
		vat.block.pageSearch(vnB_Detail);
	}
}

function showMessage(){
	var width = "600";
    var height = "400";  
	window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_INVENTORY_LIST" +
		"&levelType=ERROR" +
        "&processObjectName=imInventoryCountsService" + 
        "&processObjectMethodName=getIdentificationList" +
        "&arguments=" + vat.item.getValueByName("#F.brandCode") +
		"{$}" + vat.item.getValueByName("#F.countsId") +
	    "{$}" + vat.item.getValueByName("#F.taxTypeCode") + 
        "&parameterTypes=STRING{$}STRING{$}STRING",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function resetForm(){   
	//vat.item.setValueByName("#F.countsDate", "");
	//vat.item.setValueByName("#F.superintendentCode", "");
	//vat.item.setValueByName("#F.actualSuperintendentCode", "");
	//vat.item.setValueByName("#F.superintendentName", "");
	//vat.item.setValueByName("#F.actualSuperintendentName", "");
	//vat.item.setValueByName("#F.description", "");
}

function doFormAccessControl(){
		var countsId = document.forms[0]["#countsId"].value;
		var customsWarehouseCode = document.forms[0]["#customsWarehouseCode"].value;
		if(countsId != ""){
          	vat.item.setAttributeByName("#F.countsId", "readOnly", true);
          	//vat.block.canGridModify( [vnB_Detail], true, true, false );
          	//vat.item.setGridAttributeByName("warehouseCode", "display", "none"); 
        }
		if(customsWarehouseCode != ""){
          	vat.item.setAttributeByName("#F.customsWarehouseCode", "readOnly", true);
          	//vat.block.canGridModify( [vnB_Detail], true, true, false );
          	//vat.item.setGridAttributeByName("warehouseCode", "display", "none");
        }
        vat.block.pageRefresh(vnB_Detail);
}