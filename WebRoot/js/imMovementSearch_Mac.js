/*** 
 *	檔案: imMovementSearch.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";

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
	 			{name:"#B.update"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doView()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},	 		
	 			{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doView()"},
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
var allOrderTypes=vat.bean("allOrderTypes");
var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
var allStatus = [["","","true"],
                 ["暫存中","簽核中","待轉出","待轉入","簽核完成","結案","待確認"],
                 ["SAVE","SIGNING","WAIT_OUT","WAIT_IN","FINISH","CLOSE","UNCONFIRMED"]];
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"調撥單查詢作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode"            , type:"LABEL"  , value:"單別"}]},	 
	 {items:[{name:"#F.orderTypeCode"            , type:"SELECT" ,  bind:"orderTypeCode", size:1, init:allOrderTypes}]},		 
	 {items:[{name:"#L.orderNo"                  , type:"LABEL"  , value:"單號"}]},
	 {items:[{name:"#F.startOrderNo"             , type:"TEXT"   ,  bind:"startOrderNo", size:20},
			 {name:"#L.between"                  		 , type:"LABEL"  , value:"至"},
	 		 {name:"#F.endOrderNo"               		 , type:"TEXT"   ,  bind:"endOrderNo"  , size:20}]},
	 {items:[{name:"#L.status"                   , type:"LABEL"  , value:"狀態"}]},	 		 
	 {items:[{name:"#F.status"                   , type:"SELECT" ,  bind:"status", size:12, init:allStatus}]},
	 {items:[{name:"#L.brandCode"                , type:"LABEL"  , value:"品牌"}]},	 
	 {items:[{name:"#F.brandCode"                , type:"TEXT"   ,  bind:"brandCode", size:6, mode:"HIDDEN"},
	 		 {name:"#F.brandName"                		 , type:"TEXT"   ,  bind:"brandName", back:false, size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryWarehouseCode"    , type:"LABEL" , value:"轉出倉庫"}]},	 
	 {items:[{name:"#F.deliveryWarehouseCode"    , type:"SELECT",  bind:"deliveryWarehouseCode", size:20, init:allDeliveryWarehouses}]},		 
	 {items:[{name:"#L.deliveryDate"             , type:"LABEL" , value:"轉出日期"}]},
	 {items:[{name:"#F.startDeliveryDate"        , type:"DATE"  ,  bind:"startDeliveryDate", size:12},
	         {name:"#L.between"                  , type:"LABEL" , value:"至"},
	         {name:"#F.endDeliveryDate"          , type:"DATE"  ,  bind:"endDeliveryDate", size:12}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.arrivalWarehouseCode"     , type:"LABEL" , value:"轉入倉庫"}]},	 
	 {items:[{name:"#F.arrivalWarehouseCode"     , type:"SELECT",  bind:"arrivalWarehouseCode", size:20, init:allArrivalWarehouses}]},		 
	 {items:[{name:"#L.arrivalDate"              , type:"LABEL" , value:"轉入日期"}]},
	 {items:[{name:"#F.startArrivalDate"         , type:"DATE"  ,  bind:"startArrivalDate", size:12},
	         {name:"#L.between"                  , type:"LABEL" , value:"至"},
	         {name:"#F.endArrivalDate"           , type:"DATE"  ,  bind:"endArrivalDate", size:12}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.originalOrderTypeCode"    , type:"LABEL" , value:"來源單別"}]},	 
	 {items:[{name:"#F.originalOrderTypeCode"    , type:"TEXT"  ,  bind:"originalOrderTypeCode", size:20}]},		 
	 {items:[{name:"#L.originalOrderNo"          , type:"LABEL" , value:"來源單號"}]},
	 {items:[{name:"#F.startOriginalOrderNo"     , type:"TEXT"  ,  bind:"startOriginalOrderNo", size:20},
			 {name:"#L.between"                  		 , type:"LABEL" , value:"至"},
	 		 {name:"#F.endOriginalOrderNo"       		 , type:"TEXT"   ,  bind:"endOriginalOrderNo"  , size:20}]},
	 {items:[{name:"#L.itemCode"                 , type:"LABEL" , value:"品號"}]},	 
	 {items:[{name:"#F.itemCode"                 , type:"TEXT"  ,  bind:"itemCode", size:20}], td:" colSpan=5"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});

}



function kweImDetail(){
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

  var vnB_Detail = 2;
	vat.item.make(vnB_Detail, "checked"                 	, {type:"RBOX"});
	vat.item.make(vnB_Detail, "indexNo"                 	, {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "orderTypeCode"             , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"單別"      });
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "deliveryDate"              , {type:"DATE" , size: 8, maxLen:20, mode:"READONLY", desc:"轉出日期"   });
	vat.item.make(vnB_Detail, "deliveryWarehouseCode"     , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"轉出庫別"   });
	vat.item.make(vnB_Detail, "arrivalDate"               , {type:"DATE" , size:12, maxLen:12, mode:"READONLY", desc:"轉入日期"   }); 
	vat.item.make(vnB_Detail, "arrivalWarehouseCode"      , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"轉人庫別"   });
	vat.item.make(vnB_Detail, "originalOrderTypeCode"     , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"來源單別"   });
	vat.item.make(vnB_Detail, "orignialOrderNo"           , {type:"TEXT" , size: 8, maxLen:20, mode:"READONLY", desc:"來源單號"   });
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "headId"                    , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														pickAllService			: "process_test=mac",
														searchKey           : ["headId"],
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
	
/*																												

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
   // alert("loadBeforeAjxService");	
    //vat.item.setValueByName("#F.orderTypeCode","IMV");
	//vat.item.setValueByName("#F.deliveryWarehouseCode","T1BS99");
	//vat.item.setValueByName("#F.arrivalWarehouseCode" ,"T1BS08");
	//vat.item.setValueByName("#F.status"               ,"CLOSE");
	var processString = "process_object_name=imMovementService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&orderTypeCode"          + "=" + vat.item.getValueByName("#F.orderTypeCode"        ) +     
	                  "&startOrderNo"           + "=" + vat.item.getValueByName("#F.startOrderNo"         ) +     
	                  "&endOrderNo"             + "=" + vat.item.getValueByName("#F.endOrderNo"           ) +     
	                  "&deliveryWarehouseCode"  + "=" + vat.item.getValueByName("#F.deliveryWarehouseCode") +     
	                  "&startDeliveryDate"      + "=" + vat.item.getValueByName("#F.startDeliveryDate"    ) +     
					  "&endDeliveryDate"        + "=" + vat.item.getValueByName("#F.endDeliveryDate"      ) +     
	                  "&arrivalWarehouseCode"   + "=" + vat.item.getValueByName("#F.arrivalWarehouseCode" ) +     
                      "&startArrivalDate"       + "=" + vat.item.getValueByName("#F.startArrivalDate"     ) +     
                      "&endArrivalDate"         + "=" + vat.item.getValueByName("#F.endArrivalDate"       ) +     
                      "&status"                 + "=" + vat.item.getValueByName("#F.status"               ) +     
                      "&originalOrderTypeCode"  + "=" + vat.item.getValueByName("#F.originalOrderTypeCode") +     
                      "&originalStartOrderNo"   + "=" + vat.item.getValueByName("#F.originalStartOrderNo" ) +     
                      "&originalEndOrderNo"     + "=" + vat.item.getValueByName("#F.originalEndOrderNo"   ) +     
                      "&itemCode"               + "=" + vat.item.getValueByName("#F.itemCode"             );      
                                                                            
	//alert(	processString);
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
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=imMovementService&process_object_method_name=saveSearchResult";
		//alert(processString);
	}
	
	return processString;
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
    //alert("showTotalCountPage");
    vat.block.pageDataSave(0);
    afterSavePageProcess = "totalCount";	
}

/*
	匯出
*/
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




/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
}


function changeRelationData(){
    afterSavePageProcess = "changeRelationData";
    vat.block.pageDataSave(0);
}

function kweImSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	  
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     ].value
	    };
     vat.bean.init(function(){
		return "process_object_name=imMovementService&process_object_method_name=executeSearchInitial"; 
   		},{other: true});
     
   
   
  }
 
}

function doSearch(){
    //alert("searchService");	
   // vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
   // alert("timeScope:"+vat.bean().timeScope);
   
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);}
			                    });
			                   
	//vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1)
}

function doView(){
    //alert("doView");
	//vat.bean().vatBeanPicker.xxx = 1;
	
	 vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imMovementService&process_object_method_name=getSearchSelection";
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