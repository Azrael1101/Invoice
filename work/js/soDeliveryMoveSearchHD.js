/*** 
 *	檔案: soDeliverySearch.js
 *	說明：入提單查詢
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";

function kweBlock(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	 var orderTypeCode = "DZM";
     vat.bean().vatBeanOther = 
  	    {loginBrandCode       : document.forms[0]["#loginBrandCode"       ].value,   	
  	     loginEmployeeCode    : document.forms[0]["#loginEmployeeCode"    ].value,
  	     vatPickerId          : document.forms[0]["#vatPickerId"          ].value,
  	     orderTypeCode        :orderTypeCode
	    };
  }
  kweHeader();
  kweButtonLine();
  kweDetail();
  kweSearchInitial();

}


function kweSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean.init(function(){
		return "process_object_name=soDeliveryMoveService&process_object_method_name=executeSearchInitial"; 
   		},{other: true});
   		
     vat.item.SelectBind(vat.bean("allDeliveryStoreAreas")   ,{ itemName : "#F.deliveryStoreArea" });
     vat.item.SelectBind(vat.bean("allArrivalStoreAreas")    ,{ itemName : "#F.arrivalStoreArea" });
     
     vat.item.SelectBind(vat.bean("allReportList")         ,{ itemName : "#F.reportList" });
     vat.item.setValueByName("#F.brandCode",vat.bean().vatBeanOther.loginBrandCode);
     vat.item.bindAll();
  }
}
function kweButtonLine(){
	vsViewFunction = vat.bean().vatBeanOther.vatPickerId==""?"doView()":"doClosePicker()";

    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"    , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"    , value:"清除",  src:"./images/button_reset.gif", eClick:"doClear()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.view"        , type:"IMG"    , value:"檢視",   src:"./images/button_view.gif", eClick:vsViewFunction}]
	 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"},
	    {items:[{name:"#F.reportList"  , type:"SELECT" },
	 			{name:"#B.print"       , type:"BUTTON"    ,value:"列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'}]
	 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
	 			]},
	
	  ], 	 
		beginService:"",
		closeService:""			
	});
	if(null== vat.bean().vatBeanOther.vatPickerId || "" == vat.bean().vatBeanOther.vatPickerId) 
		vat.item.setStyleByName("#B.view" , "display", "none");
}

function kweHeader(){ 
var allOrderTypes=vat.bean("allOrderTypes");
var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
var allStatus = [["","",true],
                 ["暫存","結案","作廢"],
                 ["SAVE","CLOSE","VOID"]];
var allSortSeq = [["","",false],
                 ["由大到小","由小到大"],
                 ["desc","asc"]];
                
var allSortKey = [["","",false],
                 ["最後更新日","單號","申請日期","領貨日","存放區域","回程區域","回程班機","袋數","狀態"],
                 ["lastUpdateDate","orderNo","orderDate","deliveryDate","storeArea","flightArea","flightNo","totalBagCounts","status"]];

vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"入提庫存移轉單查詢作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderNo"                  , type:"LABEL"  , value:"單號"}]},
	 {items:[{name:"#F.startOrderNo"             , type:"TEXT"   ,  bind:"startOrderNo", size:14},
			 {name:"#L.between"                  , type:"LABEL"  , value:"至"},
	 		 {name:"#F.endOrderNo"               , type:"TEXT"   ,  bind:"endOrderNo"  , size:14}]},
	 {items:[{name:"#L.deliveryOrderNo"          , type:"LABEL" , value:"入提單號"}]},
	 {items:[{name:"#F.deliveryOrderNo"          , type:"TEXT"  ,  bind:"deliveryOrderNo",size:14}]},
	 {items:[{name:"#L.status"                   , type:"LABEL"  , value:"狀態"}]},	 		 
	 {items:[{name:"#F.status"                   , type:"SELECT" ,  bind:"status", size:12, init:allStatus}]},
	 {items:[{name:"#L.brandCode"                , type:"LABEL"  , value:"品牌"}]},	 
	 {items:[{name:"#F.brandCode"                , type:"TEXT"   ,  bind:"brandCode", size:6, mode:"HIDDEN"},
	 		 {name:"#F.brandName"                , type:"TEXT"   ,  bind:"brandName", back:false, size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.OrderDate"                , type:"LABEL"  , value:"申請日期"}]},
	 {items:[{name:"#F.startOrderDate"           , type:"DATE"  ,  bind:"startOrderDate", size:12},
	         {name:"#L.between"                  , type:"LABEL" , value:"至"},
	         {name:"#F.endOrderDate"             , type:"DATE"  ,  bind:"endOrderDate", size:12}]},
	 {items:[{name:"#L.deliveryStoreArea"        , type:"LABEL" , value:"轉出庫別"}]},	 
	 {items:[{name:"#F.deliveryStoreArea"        , type:"SELECT",  bind:"deliveryStoreArea", size:20}]},
	 {items:[{name:"#L.arrivalStoreArea"         , type:"LABEL" , value:"轉入庫別"}]},
	 {items:[{name:"#F.arrivalStoreArea"         , type:"SELECT", bind:"arrivalStoreArea"}]},
	 {items:[{name:"#L.moveEmployee"             , type:"LABEL" , value:"轉貨人員"}]},
	 {items:[{name:"#F.moveEmployee"             , type:"TEXT"  ,  bind:"moveEmployee", size:12, eChange:"getEmployeeInfo()"},
	         {name:"#F.moveEmployeeName"         , type:"TEXT"  ,  bind:"moveEmployeeName", size:12, mode:"READONLY"}]}]},
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
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"       });
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" , size:14, maxLen:20, mode:"READONLY", desc:"單號"   });
	vat.item.make(vnB_Detail, "orderDate"                 , {type:"DATE" , size: 5, maxLen:12, mode:"READONLY", desc:"日期"});
	vat.item.make(vnB_Detail, "deliveryStoreArea"		  , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"轉出庫別"}); 
	vat.item.make(vnB_Detail, "arrivalStoreArea"          , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"轉入庫別"});
	vat.item.make(vnB_Detail, "moveEmployeeName"          , {type:"TEXT" , size:13, maxLen:20, mode:"READONLY", desc:"轉貨人員"});
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"   });
	vat.item.make(vnB_Detail, "headId"                    , {type:"ROWID"});	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId"],														
														pickAllService		: function (){return selectAll();},
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "pageLoadSuccess()",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "2",
													    indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});


}


function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
    vat.item.setValueByName("#F.deliveryOrderNo",vat.item.getValueByName("#F.deliveryOrderNo").toUpperCase());
	var processString = "process_object_name=soDeliveryMoveService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&orderTypeCode"          + "=" +"DHM"+     
	                  "&startOrderNo"           + "=" + vat.item.getValueByName("#F.startOrderNo") +    
	                  "&endOrderNo"             + "=" + vat.item.getValueByName("#F.endOrderNo") +      
	                  "&startOrderDate"         + "=" + vat.item.getValueByName("#F.startOrderDate") +     
	                  "&endOrderDate"           + "=" + vat.item.getValueByName("#F.endOrderDate"  ) +     
	                  "&moveEmployee"           + "=" + vat.item.getValueByName("#F.moveEmployee") +     
                      "&deliveryStoreArea"      + "=" + vat.item.getValueByName("#F.deliveryStoreArea") + 
                      "&arrivalStroeArea"       + "=" + vat.item.getValueByName("#F.arrivalStroeArea") +    
                      "&deliveryOrderNo"        + "=" + vat.item.getValueByName("#F.deliveryOrderNo" ) +     
                      "&status"                 + "=" + vat.item.getValueByName("#F.status") ;       
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
		processString = "process_object_name=soDeliveryMoveService&process_object_method_name=saveSearchResult";
		//alert(processString);
	}
	
	return processString;
}								



/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
}

function saveSuccessAfter() {
}



function doSearch(){
   //alert("searchService");	
   // vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
   // alert("timeScope:"+vat.bean().timeScope);
   
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess:
			                        function() {
			                                    vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
			                                   }
			                    });

}

function pageLoadSuccess(){
	//alert("pageLoadSuccess");
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


function doClosePicker(){
   // alert("doClosePicker");
	//vat.bean().vatBeanPicker.xxx = 1;

	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=soDeliveryMoveService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

function doView(){
	alert("doView");
	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=soDeliveryMoveService&process_object_method_name=getSearchSelection";
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

function selectAll(){
  var processString = "";
  var isProcessing = false;
  //alert(vat.bean().vatBeanOther.isAllClick);
  if("N" == vat.bean().vatBeanOther.isAllClick ||
     typeof vat.bean().vatBeanOther.isAllClick == "undefined"){
  	if(confirm("本功能僅協助勾選前100筆之查詢結果，是否執行?"))
  		isProcessing = true;
  }else{
  	isProcessing = true;
  }
  if(isProcessing)
  	processString = "process_object_name=soDeliveryMoveService&process_object_method_name=updateAllSearchData";  
  return processString;
 
}

function doClear(){
	vat.item.setValueByName("#F.startOrderNo" ,"");
	vat.item.setValueByName("#F.endOrderNo" ,"");
	vat.item.setValueByName("#F.startOrderDate" ,"");
	vat.item.setValueByName("#F.endOrderDate","");
	vat.item.setValueByName("#F.moveEmployee" ,"");
	vat.item.setValueByName("#F.deliveryOrderNo" ,"");
	vat.item.setValueByName("#F.deliveryStoreArea" ,"");
	vat.item.setValueByName("#F.arrivalStoreArea","");
	vat.item.setValueByName("#F.status" ,"");
}



function getEmployeeInfo() {
    
    if ("" !=vat.item.getValueByName("#F.moveEmployee")) {
        vat.item.setValueByName("#F.moveEmployee",vat.item.getValueByName("#F.moveEmployee").toUpperCase());

        vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.moveEmployee");
        vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=getEmployeeInfo";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("" != vat.bean().vatBeanOther.employeeName ){
                    vat.item.setValueByName("#F.moveEmployeeName", vat.bean().vatBeanOther.employeeName);
                    vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.moveEmployee");
                    vat.bean().vatBeanOther.executeEmployeeName=vat.item.getValueByName("#F.moveEmployeeName");
			     }else{
			        vat.item.setValueByName("#F.moveEmployeeName", "");
			     	alert("員工代號錯誤，請重新輸入！");
	 				vat.form.item.setFocus( "#F.moveEmployee" );			     	
			     }
		     }
		});
    }else{
    	vat.item.setValueByName("#F.moveEmployeeName","");
    }
}


function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(null != vFormId && "" != vFormId && 0 != vFormId){
    	var url = "/erp/So_Delivery:move:20150605.page?formId=" + vFormId;	
		sc=window.open(url, '入提庫存移轉維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.moveTo(0,0);
		sc.resizeTo(screen.availWidth,screen.availHeight);
	}
}



//列印
function openReportWindow() {
	var vbAllow = true;

    if(vbAllow){
	    vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.loginBrandCode");
	    vat.bean().vatBeanOther.executeEmployeeCode = vat.bean().vatBeanOther.loginEmployeeCode;
	    vat.bean().vatBeanOther.reportFunctionCode = vat.item.getValueByName("#F.reportList");
	    vat.bean().vatBeanOther.startOrderNo = vat.item.getValueByName("#F.startOrderNo");
	    vat.bean().vatBeanOther.endOrderNo = vat.item.getValueByName("#F.endOrderNo");
	    vat.bean().vatBeanOther.startOrderDate = vat.item.getValueByName("#F.startOrderDate");
	    vat.bean().vatBeanOther.endOrderDate = vat.item.getValueByName("#F.endOrderDate");
	    vat.bean().vatBeanOther.moveEmployee = vat.item.getValueByName("#F.moveEmployee");
	    vat.bean().vatBeanOther.deliveryStoreArea = vat.item.getValueByName("#F.deliveryStoreArea");
	    vat.bean().vatBeanOther.arrivalStoreArea = vat.item.getValueByName("#F.arrivalStoreArea");
	    vat.bean().vatBeanOther.deliveryOrderNo = vat.item.getValueByName("#F.deliveryOrderNo");
	    vat.bean().vatBeanOther.status = vat.item.getValueByName("#F.status");
	    vat.block.submit(function () {
	        return "process_object_name=soDeliveryMoveService" + "&process_object_method_name=getReportConfig";
	    }, {
	        other: true,
	        funcSuccess: function () {
	            //vat.item.setValueByName("#F.remark2", vat.bean().vatBeanOther.reportUrl);
	            eval(vat.bean().vatBeanOther.reportUrl);
	
	           
	        }
	    });
	}
   
}