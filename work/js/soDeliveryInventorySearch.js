/***
 *	檔案: soDeliveryInventorySearch.js
 *	說明：表單查詢
 *	修改：Lara
 *  <pre>
 *  	Created by Lara
 *  	All rights reserved.
 *  </pre>
 */

vat.debug.disable();
var vnB_Header = 0;
var vnB_Button = 1;
var vnB_Detail = 2;

function kweBlock(){

  if(typeof document.forms[0]["#brandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {loginBrandCode       : document.forms[0]["#brandCode"       ].value,   	
  	     loginEmployeeCode    : document.forms[0]["#loginEmployeeCode"    ].value,
  	     vatPickerId          : document.forms[0]["#vatPickerId"          ].value,
  	     orderTypeCode        :"DZC"
	    };

	 vat.bean.init(function(){
	 return "process_object_name=soDeliveryInventoryService&process_object_method_name=executeSearchInitial";
     },{other: true});
  }


  kweHeader();
  kweButtonLine();
  kweDetail();	
}

function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    
    vsViewFunction = vat.bean().vatBeanOther.vatPickerId==""?"doView()":"doClosePicker()";
    
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"doClear()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.update"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:vsViewFunction}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function kweHeader(){

var allOrderTypes = vat.bean("allOrderTypes");
var allStatus = [["","",true],
                 ["暫存","作廢","盤點中","盤點完成"],
                 ["SAVE","VOID","COUNTING","COUNT_FINISH"]];

vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"盤點單維護作業", rows:[  
	{row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", size:1, mode:"READONLY",  init:allOrderTypes}]},		 
	 {items:[{name:"#L.orderNo", type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.startorderNo", type:"TEXT"  ,  bind:"startorderNo", size:20},
	 		 {name:"#L.between"                  , type:"LABEL" , value:"至"},
	 		 {name:"#F.endorderNo", type:"TEXT"  ,  bind:"endorderNo", size:20}]},	 		 		 	 		 	 		 
	 {items:[{name:"#L.status"      , type:"LABEL" , value:"狀態"}]},	 		 
     {items:[{name:"#F.status"      , type:"SELECT" ,  bind:"status", size:12, init:allStatus}]}]},	 	  		 
	 {row_style:"", cols:[	
	 {items:[{name:"#L.countsId", type:"LABEL", value:"盤點代號"}]},
	 {items:[{name:"#F.countsId", type:"TEXT"  ,  bind:"countsId", size:20}]}, 	 	    	
	 {items:[{name:"#L.countsDate", type:"LABEL", value:"盤點日期"}]},
 	 {items:[{name:"#F.startcountsDate", type:"DATE",   bind:"startcountsDate", size:12},
 	 		 {name:"#L.between"                  , type:"LABEL" , value:"至"},
 	 		 {name:"#F.endcountsDate", type:"DATE",   bind:"endcountsDate", size:12}], td:" colSpan=3"}]}	 
	], 	 
		beginService:"",
		closeService:""			
	});
}

function kweDetail(){
	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = true;
	
	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
	var vbSelectionType = "CHECK";    
    var vnB_Detail = 2;
    if(vatPickerId != null && vatPickerId != ""){
    	vat.item.make(vnB_Detail, "checkbox"                  , {type:"XBOX"});
    	vbSelectionType = "CHECK";    
    }else{
    	vbSelectionType = "NONE";
    }	
	// set column
    vat.item.make(vnB_Detail, "indexNo", {type:"IDX" , desc:"序號"});
	vat.item.make(vnB_Detail, "orderTypeCode", {type:"TEXT", size:10, maxLen:20, desc:"單別", mode:"READONLY"});
	vat.item.make(vnB_Detail, "orderNo", {type:"TEXT", size:20, maxLen:20, desc:"單號", mode:"READONLY"});
	vat.item.make(vnB_Detail, "countsDate", {type:"TEXT", size:15, maxLen:20, desc:"盤點日期", mode:"READONLY"});
	vat.item.make(vnB_Detail, "countsId", {type:"TEXT", size:20, maxLen:20, desc:"盤點代號", mode:"READONLY"});
	vat.item.make(vnB_Detail, "status", {type:"TEXT", size:10, maxLen:20, desc:"狀態", mode:"READONLY"});	
	vat.item.make(vnB_Detail, "headId", {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
									id                  : "vatDetailDiv",
									pageSize            : 10,
									searchKey           : ["headId"],
									indexType           : "AUTO",
									selectionType       : vbSelectionType,
			                        canGridDelete       : vbCanGridDelete,
									canGridAppend       : vbCanGridAppend,
									canGridModify       : vbCanGridModify,	
									loadBeforeAjxService: "loadBeforeAjxService()",
									loadSuccessAfter    : "pageLoadSuccess()",	
									saveBeforeAjxService: "saveBeforeAjxService()",
									saveSuccessAfter    : "saveSuccessAfter()",
								    blockId             : "2",
								    indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()}}
									});
}

function loadBeforeAjxService(){
	var processString = "process_object_name=soDeliveryInventoryService&process_object_method_name=getAJAXSearchPageData" +
						"&loginBrandCode=" + vat.item.getValueByName("#F.loginBrandCode") + 
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") + 
	                    "&startorderNo=" + vat.item.getValueByName("#F.startorderNo") +
	                    "&endorderNo=" + vat.item.getValueByName("#F.endorderNo") + 
	                    "&status=" + vat.item.getValueByName("#F.status") + 
	                    "&countsId=" + vat.item.getValueByName("#F.countsId") + 
	                    "&startcountsDate=" + vat.item.getValueByName("#F.startcountsDate") + 
	                    "&endcountsDate=" + vat.item.getValueByName("#F.endcountsDate");
	//alert("After loadBeforeAjxService");
	return processString;
}

function pageLoadSuccess(){}

function saveBeforeAjxService() {

	var processString = "";
	//alert("saveBeforeAjxService");
	processString = "process_object_name=soDeliveryInventoryService&process_object_method_name=saveSearchResult";
	
	return processString;
}

function saveSuccessAfter(){}

function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess:
			                        function() {
			                                    vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
			                                   }
			                    });
}

function doClosePicker(){
	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=soDeliveryInventoryService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

function doView(){
	alert("doView");
	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=soDeliveryInventoryService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
}

function doClear(){
	vat.item.setValueByName("#F.startorderNo" ,"");
	vat.item.setValueByName("#F.endorderNo" ,"");
	vat.item.setValueByName("#F.status" ,"");
	vat.item.setValueByName("#F.countsId" ,"");
	vat.item.setValueByName("#F.startcountsDate" ,"");
	vat.item.setValueByName("#F.endcountsDate" ,"");
}

function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(null != vFormId && "" != vFormId && 0 != vFormId){
    	var url = "/erp/So_Delivery:inventoryCounts:20120209.page?formId=" + vFormId;	
		sc=window.open(url, '盤點單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.moveTo(0,0);
		sc.resizeTo(screen.availWidth,screen.availHeight);
	}
}