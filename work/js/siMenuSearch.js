/*** 
 *	檔案: siMenuSearch.js
 *	說明：選單查詢作業
 */
 
vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){
  	formInitial();
  	headerInitial();
  	buttonLine();
  	detailInitial();
	//doFormAccessControl();
}

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = {
			brandCode     : document.forms[0]["#loginBrandCode"].value,   	
			employeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
			orderTypeCode : document.forms[0]["#orderTypeCode"].value,
			vatPickerId   : document.forms[0]["#vatPickerId"].value 
		};
	}
/*
     vat.bean.init(function(){
		return "process_object_name=poPurchaseMainAction&process_object_method_name=performSearchInitial"; 
     },{other: true});	
	
*/
}

function headerInitial(){
	vat.block.create(vnB_Header, {
		id: "vnB_Header", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"選單查詢作業", rows:[  
		 	{row_style:"", cols:[
			 		{items:[{name:"#L.functionCode",       type:"LABEL",  value:"選單代號"}]},
					{items:[{name:"#F.functionCode",       type:"TEXT",   bind:"functionCode", size:30}]},
			 		{items:[{name:"#L.name",          	   type:"LABEL",  value:"選單名稱"}]},	 
			 		{items:[{name:"#F.name",      		   type:"TEXT",   bind:"name", size:30}]}
		  		]
		  	}
	 	],
	 beginService:"",
	 closeService:""			
	});
}

function buttonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search",      type:"IMG",      value:"查詢", src:"./images/button_find.gif",  eClick:"doSearch()"},
	 			{name:"SPACE",          type:"LABEL",    value:"　"},
	 			{name:"#B.clear",       type:"IMG",      value:"清除", src:"./images/button_reset.gif", eClick:"resetForm()"},
	 			{name:"SPACE",          type:"LABEL",    value:"　"},
	 	 		{name:"#B.exit",        type:"IMG",      value:"離開", src:"./images/button_exit.gif",  eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE",          type:"LABEL",    value:"　"},
	 			{name:"#B.picker",      type:"IMG",      value:"檢視", src:"./images/button_view.gif",  eClick:"doClosePicker()"}]}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
	var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
	var vatPickerId = vat.bean().vatBeanOther.vatPickerId;   
	var vbSelectionType = "CHECK";    
	var vnB_Detail = 2;
	
    if(vatPickerId != null && vatPickerId != ""){
 		vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
		vbSelectionType = "CHECK";    
    }
    else{
		vat.item.setStyleByName("#B.view" , "display", "none");
		vbSelectionType = "NONE";
    }
    
    vat.item.make(vnB_Detail, "indexNo",            {type:"IDX",                              desc:"序號" });
    vat.item.make(vnB_Detail, "functionCode",          {type:"TEXT",   size:20, mode:"READONLY", desc:"選單代號" });
    vat.item.make(vnB_Detail, "name",          {type:"TEXT",   size:20, mode:"READONLY", desc:"選單名稱" });        	
	vat.item.make(vnB_Detail, "url", 			{type:"TEXT",   size:30, mode:"READONLY", desc:"URL" });
	vat.item.make(vnB_Detail, "parentMenuId", 			{type:"TEXT",   size:10, mode:"READONLY", desc:"上層選單" });
	vat.item.make(vnB_Detail, "menuId",             {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail, {
									id                  : "vatDetailDiv",
									pageSize            : 10,
									searchKey           : ["menuId"],
									selectionType       : vbSelectionType,
									indexType           : "AUTO",
								  	canGridDelete       : vbCanGridDelete,
									canGridAppend       : vbCanGridAppend,
									canGridModify       : vbCanGridModify,	
									loadBeforeAjxService: "loadBeforeAjxService()",
									saveBeforeAjxService: "saveBeforeAjxService()",
									saveSuccessAfter    : "",
									blockId             : vnB_Detail,
									indicate            : 
										function(){
											if(vatPickerId != null && vatPickerId != ""){
												return false}else{ openModifyPage()}
										}
									});
									
}

function loadBeforeAjxService(){
	var processString = "process_object_name=siMenuService&process_object_method_name=getAJAXSearchPageData" +
	                    "&functionCode=" + vat.item.getValueByName("#F.functionCode") +
	                    "&name=" + vat.item.getValueByName("#F.name");
	return processString;	
}

function saveBeforeAjxService(){
var processString = "";
	processString = "process_object_name=siMenuService&process_object_method_name=saveSearchResult";
	return processString;
}

function doSearch(){
	vat.block.submit(function(){return "process_object_name=siMenuService"+
			       		"&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			            {other: true, 
			            	funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			            });
}

function doClosePicker(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=siMenuAction&process_object_method_name=performSearchSelection";
    		}, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
	}}); 
}

function resetForm(){
    vat.item.setValueByName("#F.functionCode", "");
    vat.item.setValueByName("#F.name", "");
}

function closeWindows(closeType){
	var isExit = true;
	if("CONFIRM" == closeType) isExit = confirm("是否確認離開?");
	if(isExit) window.top.close();
}
