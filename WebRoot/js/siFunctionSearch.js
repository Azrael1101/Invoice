

/*** 
 *	檔案: siFunctionSearch.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
 
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Detail = 1;

function kweImBlock(){
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();
  kweImDetail();

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
	 			{name:"#B.update"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"},
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
	title:"系統功能查詢作業", rows:[  
	 		{row_style:"", cols:[
			 {items:[{name:"#L.functionCode"             , type:"LABEL"  , value:"功能代號"}]},
			 {items:[{name:"#F.functionCode"             , type:"TEXT"   , size:20}]},
			 {items:[{name:"#L.functionName"             , type:"LABEL"  , value:"功能名稱"}]},
			 {items:[{name:"#F.functionName"             , type:"TEXT"   , size:20}]}
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

    vat.item.make(vnB_Detail, "checked"                   , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "functionCode"             	, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"功能代號"      });
	vat.item.make(vnB_Detail, "functionName"               , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"功能名稱"      });
	vat.item.make(vnB_Detail, "functionCode"             , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["functionCode"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,		
														appendBeforeService : "kwePageAppendBeforeMethod()",
														appendAfterService  : "kwePageAppendAfterMethod()",														
														loadSuccessAfter    : "kwePageLoadSuccess()",						
														eventService        : "changeRelationData",   
														loadBeforeAjxService: "loadBeforeAjxService()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
	

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
   // alert("loadBeforeAjxService");	
    //vat.item.setValueByName("#F.orderTypeCode","IMV");
	//vat.item.setValueByName("#F.deliveryWarehouseCode","T1BS99");
	//vat.item.setValueByName("#F.arrivalWarehouseCode" ,"T1BS08");
	//vat.item.setValueByName("#F.status"               ,"CLOSE");
	var processString = "process_object_name=siFunctionService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&functionCode"          + "=" + vat.item.getValueByName("#F.functionCode"        ) +     
	                  "&functionName"           + "=" + vat.item.getValueByName("#F.functionName"         );     
                                                                            
	//alert(	processString);
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
		processString = "process_object_name=siFunctionService&process_object_method_name=saveSearchResult";
		//alert(processString); 
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

function doClosePicker(){
	//alert('送出');
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=siFunctionService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

function changeRelationData(){
    afterSavePageProcess = "changeRelationData";
    vat.block.pageDataSave(0);
}

function kweImSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value	  
	    };
  }
}

function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
	//vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1)
}


function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}
