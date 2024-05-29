/*** 
 *	檔案: cmCustomsDeclarationSearch.js
 *	說明：海關進出倉查詢
 *	修改：Joe
 *  <pre>
 *  	Created by Joe
 *  	All rights reserved.
 *  </pre>
 */
 
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Detail = 1;

function kweImBlock(){
  initialVatBeanOther();
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();
  kweImDetail();

}

function initialVatBeanOther(){
		vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value  
	    };
}

function kweImSearchInitial(){
 
  /*if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	    vat.bean.init(	
	  		function(){
				return "process_object_name=imDistributionHeadService&process_object_method_name=executeSearchInitial"; 
	    	},{								
	    		other: true
    	});
  }*/
  
}

function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:""},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.view"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},	 		
	 			{name:"#F.selectedAll" , type:"CHECKBOX" , value:"N"},
	 			{name:"#L.selectedAll" , type:"LABEL"    , value:"選擇全部"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},	 		
	 			{name:"#F.clearAll" , type:"CHECKBOX"    , value:"N"},
	 			{name:"#L.clearAll" , type:"LABEL"       , value:"清除全部"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function kweImHeader(){ 


	vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"配貨單查詢作業", rows:[  
	 		{row_style:"", cols:[
	 		     {items:[{name:"#L.orderNoS", 	type:"LABEL", value:"配貨單單號"}]},	 
	 			 {items:[{name:"#F.orderNoS", 	type:"TEXT",  bind:"orderNoS",size:15}]},
	 		     {items:[{name:"#L.orderNoE", 	type:"LABEL", value:" 至 "}]},	 
	 			 {items:[{name:"#F.orderNoE", 	type:"TEXT",  bind:"orderNoE",size:15}]}
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function kweImDetail(){
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "checked"          	, {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"           	, {type:"IDX"});
	vat.item.make(vnB_Detail, "orderNo"           	, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"配貨單號"});
	vat.item.make(vnB_Detail, "defaultWarehouseCode", {type:"TEXT" , size:15, maxLen:20, mode:"HIDDEN", desc:"庫別"});
	vat.item.make(vnB_Detail, "lastUpdateDate"      , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"最近修改日期"});
	vat.item.make(vnB_Detail, "headId"             , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId"],
														pickAllService		: "selectAll",
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,		
//														appendBeforeService : "kwePageAppendBeforeMethod()",
//														appendAfterService  : "kwePageAppendAfterMethod()",														
														loadSuccessAfter    : "kwePageLoadSuccess()",						
//														eventService        : "changeRelationData",   
														loadBeforeAjxService: "loadBeforeAjxService()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
}

function kwePageSaveMethod(){}					


function saveSuccessAfter(){}

function kwePageLoadSuccess(){
	 if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

function kwePageAppendBeforeMethod(){
	return true;
}

function kwePageAppendAfterMethod(){
	 return alert("新增完畢");
}


function loadBeforeAjxService(){
	var processString = "process_object_name=imDistributionHeadService&process_object_method_name=getAJAXSearchPageData" + 
	                  "&orderNoS"  + "=" + vat.item.getValueByName("#F.orderNoS"  ) +     
	                  "&orderNoE" + "=" + vat.item.getValueByName("#F.orderNoE" );     
	return processString;											
}



//	判斷是否要關閉LINE

function checkEnableLine() {
	return true;
}


//	取得SAVE要執行的JS FUNCTION

function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=imDistributionHeadService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								


//	顯示合計的頁面

function showTotalCountPage() {
    //save line
    //alert("showTotalCountPage");
    vat.block.pageDataSave(0);
    afterSavePageProcess = "totalCount";	
}

//	匯出
function doExport() {
	//save line
	//alert("doExport");
	vat.block.pageDataSave(0);
	afterSavePageProcess = "executeExport";
}

function doPageDataSave(){
    //alert("doPageDataSave");
    vat.block.pageDataSave(0);
}

function doPageRefresh(){
    //alert("doPageRefresh");
    vat.block.pageRefresh(0);
}

//	PICKER 之前要先RUN LINE SAVE
function doBeforePicker(){
    vat.block.pageDataSave(0);
}


function changeRelationData(){
    afterSavePageProcess = "changeRelationData";
    vat.block.pageDataSave(0);
}

function doSearch(){
    vat.block.submit(function(){
    			return "process_object_name=tmpAjaxSearchDataService"+
                "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, {
                	other: true, 
                	funcSuccess: function() {
                		vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);
                	}
				});
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}

function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
					function(){ return "process_object_name=imDistributionHeadService&process_object_method_name=getSearchSelection";
					}, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

