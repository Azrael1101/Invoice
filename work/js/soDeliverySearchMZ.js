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
  	 var orderTypeCode = "DZN";
  	 var cwCode = "VD";
     vat.bean().vatBeanOther = 
  	    {loginBrandCode       : document.forms[0]["#loginBrandCode"       ].value,   	
  	     loginEmployeeCode    : document.forms[0]["#loginEmployeeCode"    ].value,
  	     accessType           : document.forms[0]["#accessType"           ].value,
  	     vatPickerId          : document.forms[0]["#vatPickerId"          ].value,
  	     cwCode          : cwCode,
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
		return "process_object_name=soDeliveryService&process_object_method_name=executeSearchInitial"; 
   		},{other: true});
     //vat.item.SelectBind(vat.bean("allOrderTypes")         ,{ itemName : "#F.orderTypeCode" });
     vat.item.SelectBind(vat.bean("allSalesFaultReasons")  ,{ itemName : "#F.SFaultReason" });
     vat.item.SelectBind(vat.bean("allDeliverFaultReasons"),{ itemName : "#F.DFaultReason" });
     vat.item.SelectBind(vat.bean("allShops"),{ itemName : "#F.shopCode" });
     //vat.item.SelectBind(vat.bean("allItemCategories")     ,{ itemName : "#F.itemCategory" });
     //vat.item.SelectBind(vat.bean("allFlightAreas")        ,{ itemName : "#F.flightArea" });
     vat.item.SelectBind(vat.bean("allStoreAreas")         ,{ itemName : "#F.storeArea" });
     vat.item.SelectBind(vat.bean("allReportListHD")         ,{ itemName : "#F.reportList" });
     vat.item.bindAll();
     vat.form.item.setFocus( "#F.orderNo" );
  }
 
}
function kweButtonLine(){
    var vsMode =vat.bean().vatBeanOther.accessType == "SUPERVISOR"?"":"HIDDEN";
	var vsViewFunction = vat.bean().vatBeanOther.vatPickerId==""?"doView()":"doClosePicker()";
	var allProcess = [["","",true],
                      ["待收貨","待建檔","待提領","結案","作廢","取消","退貨","待銷退","已退款"],
                      ["W_PICK","W_CREATE","W_DELIVERY","CLOSE","VOID","CANCEL","RETURN","W_RETURN","REFUND"]];
    if(vat.bean().vatBeanOther.accessType == "SUPERVISOR"){
	    vat.block.create(vnB_Button = 0, {
		id: "vatBlock_Button", generate: true,
		table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
		title:"", rows:[  
		 {row_style:"", cols:[
		 	{items:[{name:"#B.search"      , type:"IMG"    , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
		 			{name:"#B.clear"       , type:"IMG"    , value:"清除",  src:"./images/button_reset.gif", eClick:"doClear()"},
		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
		 	 		{name:"#B.exit"        , type:"IMG"    , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
		 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData('S')"},
		 	 		//{name:"SPACE"          , type:"LABEL"  , value:"　"},
		 			{name:"#B.view"        , type:"IMG"    , value:"檢視",   src:"./images/button_view.gif", eClick:vsViewFunction}]
		 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"},
		 	{items:[{name:"#F.reportList"  , type:"SELECT" },
		 			{name:"#B.print"       , type:"BUTTON"    ,value:"列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'}]
		 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"},
		 	{items:[{name:"#L.processItem", type:"LABEL", value:"處理事項:"},
		 			{name:"#F.processItem", type:"SELECT" , size:12, init:allProcess}]
		 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"},
		 	{items:[{name:"#L.employeeCode", type:"LABEL", value:"工號:"},
		 			{name:"#F.employeeCode", type:"TEXT" , bind:"employeeCode", size:4, eChange:'getEmployeeInfo()'},
		 			{name:"#F.employeeName", type:"TEXT" , bind:"employeeName", size:4,mode:"READONLY"}]
		 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
		 			]},
		
		  ], 	 
			beginService:"",
			closeService:""			
		});
//	}else if(vat.bean().vatBeanOther.accessType == "SIMPLE"){
//  		vat.block.create(vnB_Button = 0, {
//		id: "vatBlock_Button", generate: true,
//		table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
//		title:"", rows:[  
//		 {row_style:"", cols:[
//		 	{items:[{name:"#B.search"      , type:"IMG"    , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
//		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
//		 			{name:"#B.clear"       , type:"IMG"    , value:"清除",  src:"./images/button_reset.gif", eClick:"doClear()"},
//		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
//		 	 		{name:"#B.exit"        , type:"IMG"    , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
//		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
//		 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},
//		 	 		//{name:"SPACE"          , type:"LABEL"  , value:"　"},
//		 			{name:"#B.view"        , type:"IMG"    , value:"檢視",   src:"./images/button_view.gif", eClick:vsViewFunction}]
//		 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
//		 			]},
//		
//		  ], 	 
//			beginService:"",
//			closeService:""			
//		});
	}else{
		vat.block.create(vnB_Button = 0, {
		id: "vatBlock_Button", generate: true,
		table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
		title:"", rows:[  
		 {row_style:"", cols:[
		 	{items:[{name:"#B.search"      , type:"IMG"    , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
		 			{name:"#B.clear"       , type:"IMG"    , value:"清除",  src:"./images/button_reset.gif", eClick:"doClear()"},
		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
		 	 		{name:"#B.exit"        , type:"IMG"    , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
		 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},
		 	 		//{name:"SPACE"          , type:"LABEL"  , value:"　"},
		 			{name:"#B.view"        , type:"IMG"    , value:"檢視",   src:"./images/button_view.gif", eClick:vsViewFunction}]  
		 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"},
		 	{items:[{name:"#F.reportList"  , type:"SELECT" },
		 			{name:"#B.print"       , type:"BUTTON"    ,value:"列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'}]
		 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"},
		 	{items:[{name:"#L.employeeCode", type:"LABEL", value:"工號:"},
		 			{name:"#F.employeeCode", type:"TEXT" , bind:"employeeCode", size:4, eChange:'getEmployeeInfo()'},
		 			{name:"#F.employeeName", type:"TEXT" , bind:"employeeName", size:4,mode:"READONLY"}]
		 			,td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
		 			]},
		
		  ], 	 
			beginService:"",
			closeService:""			
		});
	}
	if(null== vat.bean().vatBeanOther.vatPickerId || "" == vat.bean().vatBeanOther.vatPickerId) 
		vat.item.setStyleByName("#B.view" , "display", "none");
	
}

function kweHeader(){ 
var allOrderTypes=vat.bean("allOrderTypes");
var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
var allShops  = vat.bean("allShops");
var allStatus = [["","",true],
                 ["待收貨","待建檔","待提領","結案","作廢","取消","退貨","待銷退","已退款"],
                 ["W_PICK","W_CREATE","W_DELIVERY","CLOSE","VOID","CANCEL","RETURN","W_RETURN","REFUND"]];
var allTerminals = [["","",true],
                 ["第一航廈","第二航廈"],
                 ["1","2"]];
var allItemType = [["","",true],
                 ["是","否"],
                 ["Y","N"]];
                 
var allAffidavits=[["","",true],
                 ["是","否"],
                 ["Y","N"]];
                 
var allLockFlag = [["","",true],
                 ["是","否"],
                 ["LOCK","UNLOCK"]];
                 
var allSortSeq = [["","",false],
                 ["由大到小","由小到大"],
                 ["desc","asc"]];
                
var allSortKey = [["","",false],
                 ["單號","回程日期","回程班機","實際提領日","申請日期"],
                 ["orderNo","flightDate","flightNo","deliveryDate","orderDate"]];
if(vat.bean().vatBeanOther.accessType == "SIMPLE"){
	vat.block.create(vnB_Header=1, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='5' style='border-right-width: 0px; border-bottom-width: 5px; border-left-width: 0px; border-color: #ffffff;'",
		title:"", 
		rows:[
		 {row_style:"background-color: #990000", cols:[		
		 {items:[{name:"#L.orderNo"     , type:"LABEL", value:"&nbsp&nbsp&nbsp;<FONT SIZE=4><BOLD>單號</BOLD></FONT>&nbsp&nbsp&nbsp;"}
		         ],  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
		 {items:[ {name:"#F.memo"        , type:"TEXT", mode:"READONLY", size:4,  style:"font-size: 1pt; width:1px;height:1px;"}
		         ,{name:"#F.orderNo"     , type:"TEXT" ,mask:"CCCCCCCCCCCCC", bind:"orderNo", eChange:"doSearch()", size:13, maxLen:13,  style:"font-size: 20pt; height:40px"}], 
		          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"}]} 		  
		  ],
			beginService:"",
			closeService:""
		});
}else{
	vat.block.create(vnB_Header = 1, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"入提單查詢作業", rows:[  
		 {row_style:"", cols:[
		 {items:[{name:"#L.orderNo"                  , type:"LABEL"  , value:"入提單號"}]},
		 {items:[{name:"#F.orderNo"                  , type:"TEXT"   ,maxLen:13 ,mask:"CCCCCCCCCCCCC",  bind:"OrderNo", size:14}]},
		 {items:[{name:"#L.customerPoNo"             , type:"LABEL" , value:"銷售單號"}]},
		 {items:[{name:"#F.customerPoNo"             , type:"TEXT"   ,maxLen:10 ,mask:"CCCCCCCCCC",  bind:"customerPoNo",size:12}]},
		 {items:[{name:"#L.OrderDate"                , type:"LABEL"  , value:"申請日期"}]},
		 {items:[{name:"#F.startOrderDate"           , type:"DATE"  ,  bind:"startOrderDate", size:12},
		         {name:"#L.between"                  , type:"LABEL" , value:"至"},
		         {name:"#F.endOrderDate"             , type:"DATE"  ,  bind:"endOrderDate", size:12}]},
		 {items:[{name:"#L.status"                   , type:"LABEL"  , value:"狀態"}]},	 		 
		 {items:[{name:"#F.status"                   , type:"SELECT" ,  bind:"status", size:12, init:allStatus}]}]},
		 
		 {row_style:"", cols:[
		 {items:[{name:"#L.flightNo"                 , type:"LABEL" , value:"回程班機"}]},	 
		 {items:[{name:"#F.flightNo"                 , type:"TEXT"  ,mask:"CCCCCC",  bind:"flightNo", size:10}]},
		 {items:[{name:"#L.shopCode"                 , type:"LABEL" , value:"店 別"}]},
		 {items:[{name:"#F.shopCode"                 , type:"SELECT",  bind:"shopCode", size:10}]},
		 {items:[{name:"#L.flightDate"               , type:"LABEL" , value:"回程日期"}]},
		 {items:[{name:"#F.startFlightDate"          , type:"DATE"  ,  bind:"startFlightDate", size:12},
		         {name:"#L.between"                  , type:"LABEL" , value:"至"},
		         {name:"#F.endFlightDate"            , type:"DATE"  ,  bind:"endFlightDate", size:12}]},
		 {items:[{name:"#L.breakable"                , type:"LABEL" , value:"易碎品"}]},
		 {items:[{name:"#F.breakable"                , type:"SELECT"  ,  bind:"breakable", size:20, init:allItemType}]}]},
		 
		 {row_style:"", cols:[
		 {items:[{name:"#L.customerName"             , type:"LABEL" , value:"客戶姓名"}]},	 
		 {items:[{name:"#F.customerName"             , type:"TEXT"  ,mask:"CCCCCCCCCCCCCCCCCCC",  bind:"customerName" , size:10}]},	
		 {items:[{name:"#L.passportNo"               , type:"LABEL" , value:"護照號碼"}]},
		 {items:[{name:"#F.passportNo"               , type:"TEXT"  ,mask:"CCCCCCCCCCCCCCCCCCC",  bind:"passportNo", size:20}]},	
		 {items:[{name:"#L.deliveryDate"              , type:"LABEL" , value:"實際提領日"}]},
		 {items:[{name:"#F.startDeliveryDate"          , type:"DATE"  ,  bind:"startDeliveryDate", size:12},
		         {name:"#L.between"                  , type:"LABEL" , value:"至"},
		         {name:"#F.endDeliveryDate"            , type:"DATE"  ,  bind:"endDeliveryDate", size:12}]},
		 {items:[{name:"#L.valuable"                 , type:"LABEL" , value:"貴重品"}]},
		 {items:[{name:"#F.valuable"                 , type:"SELECT"  ,  bind:"valuable", size:20, init:allItemType}]}]},
		// {items:[{name:"#L.lockFlag"                 , type:"LABEL" , value:"是否鎖定"}]},
		// {items:[{name:"#F.lockFlag"                 , type:"SELECT"  ,  bind:"lockFlag", size:20, init:allLockFlag}]}]},
		
		 {row_style:"", cols:[
		 {items:[{name:"#L.storeArea"                , type:"LABEL" , value:"存放地點"}]},	 
		 {items:[{name:"#F.storeArea"                , type:"SELECT",  bind:"storeArea", size:20}]},
		 {items:[{name:"#L.contactInfo"               , type:"LABEL" , value:"連絡電話"}]},
		 {items:[{name:"#F.contactInfo"               , type:"TEXT"  ,  bind:"contactInfo", size:20}]},
		 {items:[{name:"#L.scheduleDeliveryDate"     , type:"LABEL" , value:"預計提領日"}]},
		 {items:[{name:"#F.startScheduleDeliveryDate", type:"DATE"  ,  bind:"startScheduleDeliveryDate", size:12},
		         {name:"#L.between"                  , type:"LABEL" , value:"至"},
		         {name:"#F.endScheduleDeliveryDate"  , type:"DATE"  ,  bind:"endScheduleDeliveryDate", size:12}]},
		 {items:[{name:"#L.terminal"                 , type:"LABEL" , value:"航廈"}]},	 
		 {items:[{name:"#F.terminal"                 , type:"SELECT"  ,  bind:"terminal", size:20, init:allTerminals}]}]},
		 {row_style:"", cols:[
		 {items:[{name:"#L.SFaultReason" , type:"LABEL" , value:"營業異常"}]},
		 {items:[{name:"#F.SFaultReason" , type:"SELECT",  bind:"SFaultReason"}]},
	     {items:[{name:"#L.DFaultReason" , type:"LABEL" , value:"提貨異常"}]},
		 {items:[{name:"#F.DFaultReason" , type:"SELECT",  bind:"DFaultReason"}]},
		 {items:[{name:"#L.expiryDate"   , type:"LABEL" , value:"商品效期"}]},
	     {items:[{name:"#F.startExpiryDate", type:"DATE",  bind:"startExpiryDate", size:12},
		         {name:"#L.between"      , type:"LABEL" , value:"至"},
		         {name:"#F.endExpiryDate", type:"DATE"  ,  bind:"endExpiryDate", size:12}]},
		 {items:[{name:"#L.affidavit" , type:"LABEL" , value:"提領證明"}]},
		 {items:[{name:"#F.affidavit" , type:"SELECT", bind:"affidavit", init:allAffidavits}]}]}, 
		 {row_style:"", cols:[
		 {items:[{name:"#L.expiryReturnNo" , type:"LABEL" , value:"逾期銷退單號"}]},
		 {items:[{name:"#F.expiryReturnNo" , type:"TEXT", maxLen:9 ,bind:"expiryReturnNo"}]},
		 {items:[{name:"#L.storageCode"    , type:"LABEL" , value:"儲位號碼"}]},
		 {items:[{name:"#F.storageCode"    , type:"TEXT"  ,  bind:"storageCode", size:12, max:6},
		         {name:"#L.storageCode"    , type:"LABEL" , value:"至"},
		         {name:"#F.storageCode1"    , type:"TEXT"  ,  bind:"storageCode1", size:12, max:6}]},
		 {items:[{name:"#L.sortKey" , type:"LABEL" , value:"排序欄位"}]},
		 {items:[{name:"#F.sortKey" , type:"SELECT", bind:"sortKey", init:allSortKey}]},
		 {items:[{name:"#L.sortSeq" , type:"LABEL" , value:"排序方向"}]},
		 {items:[{name:"#F.sortSeq" , type:"SELECT", bind:"sortKey", init:allSortSeq}]}]},
		 {row_style:"", cols:[
		 {items:[{name:"#L.soDelUpdateHeadCode" , type:"LABEL" , value:"更正單號"}]},
		 {items:[{name:"#F.soDelUpdateHeadCode" , type:"TEXT", maxLen:9 ,bind:"soDelUpdateHeadCode"}]}
		 ]},
		  ], 	 
			beginService:"",
			closeService:""			
		});
	}
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
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,view:"fixed",                     desc:"序號"       });
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" ,view:"fixed", size:14, mode:"READONLY", desc:"單號"   });
	vat.item.make(vnB_Detail, "flightDate"                , {type:"TEXT" ,view:"fixed", size: 8, mode:"READONLY", desc:"回程日"});
	vat.item.make(vnB_Detail, "flightNo"				  , {type:"TEXT" ,view:"fixed", size: 4, mode:"READONLY", desc:"班機"}); 
	vat.item.make(vnB_Detail, "deliveryDate"			  , {type:"TEXT" ,view:"fixed", size: 8, mode:"READONLY", desc:"實提日"}); 
	vat.item.make(vnB_Detail, "customerName"			  , {type:"TEXT" ,view:"", size: 8,  mode:"READONLY", desc:"客戶名"});
	vat.item.make(vnB_Detail, "passportNo"				  , {type:"TEXT" ,view:"", size:13,  mode:"READONLY", desc:"護照號碼"});
	vat.item.make(vnB_Detail, "orderDate"                 , {type:"TEXT" ,view:"", size: 8,  mode:"READONLY", desc:"申請日"});
	vat.item.make(vnB_Detail, "storeArea"                 , {type:"TEXT" ,view:"", size: 4, mode:"READONLY", desc:"存放"});
	vat.item.make(vnB_Detail, "storageCode"               , {type:"TEXT" ,view:"", size: 4, mode:"READONLY", desc:"儲位"});
	vat.item.make(vnB_Detail, "totalBagCounts"            , {type:"NUMM" ,view:"", size: 4,  mode:"READONLY", dec:0, desc:"件數", bind:"itemCount"});
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" ,view:"", size: 4,  mode:"READONLY", desc:"狀態"   });
	vat.item.make(vnB_Detail, "customerPoNoString"        , {type:"TEXT" ,view:"", size: 9,  mode:"READONLY", desc:"銷貨單號" , alter:true  });
	vat.item.make(vnB_Detail, "sFaultReason"              , {type:"TEXT" ,view:"shift", size: 16, mode:"READONLY", desc:"營業異常"   });
	vat.item.make(vnB_Detail, "dFaultReason"              , {type:"TEXT" ,view:"shift", size: 16, mode:"READONLY", desc:"提貨異常"   });
	vat.item.make(vnB_Detail, "headId"                    , {type:"ROWID"  });
	if(vat.bean().vatBeanOther.accessType == "SUPERVISOR")
		vat.item.make(vnB_Detail, "process", {type:"BUTTON", view: "fixed", desc:"", value:"處理",  eClick:"doProcess()"});	
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

   if("" ==  vat.bean().vatBeanOther.status){
		 vat.item.setGridStyleByName("modifyDeliveryDate"  , "display", "inline");
   }else{
	 	 vat.item.setGridStyleByName("modifyDeliveryDate"  , "display", "none");
   }
}


function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
    var vsOrderNo="";
    var vsCustomerPoNo="";
    vat.item.setValueByName("#F.orderNo",vat.item.getValueByName("#F.orderNo").toUpperCase());
    vat.item.setValueByName("#F.customerPoNo",vat.item.getValueByName("#F.customerPoNo").toUpperCase());
    if(vat.bean().vatBeanOther.accessType == "SIMPLE"){
    	//alert(vat.item.getValueByName("#F.orderNo").substring(0, 3));
    	if(vat.item.getValueByName("#F.orderNo").substring(0, 3)=="DMP"){
    		vsOrderNo=vat.item.getValueByName("#F.orderNo");
    	}else{
    		vat.item.setValueByName("#F.orderNo",vat.item.getValueByName("#F.orderNo").substring(0,10));
    		vsCustomerPoNo=vat.item.getValueByName("#F.orderNo");
    	}
    }else{
    	vsOrderNo=vat.item.getValueByName("#F.orderNo");
    	vsCustomerPoNo=vat.item.getValueByName("#F.customerPoNo");
    }
	var processString = "process_object_name=soDeliveryService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&orderTypeCode"          + "=" +"DZN"+   
	                  "&cwCode"                 + "=" +"VD"+    
	                  "&orderNo"                + "=" + vsOrderNo +     
	                  "&startOrderDate"         + "=" + vat.item.getValueByName("#F.startOrderDate") +     
	                  "&endOrderDate"           + "=" + vat.item.getValueByName("#F.endOrderDate"  ) +     
	                  "&customerName"           + "=" + vat.item.getValueByName("#F.customerName") +     
                      "&contactInfo"            + "=" + vat.item.getValueByName("#F.contactInfo") + 
                      "&passportNo"             + "=" + vat.item.getValueByName("#F.passportNo") +    
                      "&flightNo"               + "=" + vat.item.getValueByName("#F.flightNo" ) +             
                      "&status"                 + "=" + vat.item.getValueByName("#F.status") +     
	                  "&startFlightDate"        + "=" + vat.item.getValueByName("#F.startFlightDate") +     
					  "&endFlightDate"          + "=" + vat.item.getValueByName("#F.endFlightDate") +      
                      "&flightNo"               + "=" + vat.item.getValueByName("#F.flightNo" ) +     
                      "&storeArea"              + "=" + vat.item.getValueByName("#F.storeArea") +
                      "&shopCode"               + "=" + vat.item.getValueByName("#F.shopCode" ) +     
                      "&startScheduleDeliveryDate"+ "=" + vat.item.getValueByName("#F.startScheduleDeliveryDate"   ) +     
                      "&endScheduleDeliveryDate"+ "=" + vat.item.getValueByName("#F.endScheduleDeliveryDate" ) + 
 					  "&startExpiryDate"        + "=" + vat.item.getValueByName("#F.startExpiryDate"   ) +     
                      "&endExpiryDate"          + "=" + vat.item.getValueByName("#F.endExpiryDate" ) + 
                      "&lockFlag"               + "=" + vat.item.getValueByName("#F.lockFlag" ) + 
                      "&valuable"               + "=" + vat.item.getValueByName("#F.valuable" ) + 
                      "&breakable"              + "=" + vat.item.getValueByName("#F.breakable" ) + 
                      "&customerPoNo"           + "=" + vsCustomerPoNo +
                      "&SFaultReason"           + "=" + vat.item.getValueByName("#F.SFaultReason" ) + 
                      "&DFaultReason"           + "=" + vat.item.getValueByName("#F.DFaultReason" ) +
                      "&terminal"               + "=" + vat.item.getValueByName("#F.terminal" ) +
                      "&affidavit"              + "=" + vat.item.getValueByName("#F.affidavit" ) +
                      "&sortKey"                + "=" + vat.item.getValueByName("#F.sortKey" ) +
                      "&sortSeq"                + "=" + vat.item.getValueByName("#F.sortSeq" )+
                      "&soDelUpdateHeadCode"                + "=" + vat.item.getValueByName("#F.soDelUpdateHeadCode" )+
                      "&expiryReturnNo"         + "=" + vat.item.getValueByName("#F.expiryReturnNo" )+
                      "&storageCode"            + "=" + vat.item.getValueByName("#F.storageCode" )+
                      "&storageCode1"            + "=" + vat.item.getValueByName("#F.storageCode1" );             	
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
		processString = "process_object_name=soDeliveryService&process_object_method_name=saveSearchResult";
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
			                                    if(vat.bean().vatBeanOther.accessType == "SIMPLE"){
													vat.form.item.setFocus( "#F.orderNo" );
												}
			                                   }
			                    });

}

function pageLoadSuccess(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
	    if(null== vat.bean().vatBeanOther.vatPickerId || "" == vat.bean().vatBeanOther.vatPickerId) 
			vat.item.setStyleByName("#B.view" , "display", "none");
		else
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
    		                     function(){ return "process_object_name=soDeliveryService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

function doView(){
	
	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=soDeliveryService&process_object_method_name=getSearchSelection";
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
  	processString = "process_object_name=soDeliveryService&process_object_method_name=updateAllSearchData";  
  return processString;
 
}

function doClear(){
	vat.item.setValueByName("#F.orderNo" ,"");
	vat.item.setValueByName("#F.startOrderDate" ,"");
	vat.item.setValueByName("#F.endOrderDate","");
	vat.item.setValueByName("#F.startDeliveryDate" ,"");
	vat.item.setValueByName("#F.endDeliveryDate" ,"");
	vat.item.setValueByName("#F.startScheduleDeliveryDate" ,"");
	vat.item.setValueByName("#F.endScheduleDeliveryDate" ,"");
	vat.item.setValueByName("#F.startExpiryDate" ,"");
	vat.item.setValueByName("#F.endExpiryDate" ,"");
	vat.item.setValueByName("#F.startFlightDate" ,"");
	vat.item.setValueByName("#F.endFlightDate","");
	vat.item.setValueByName("#F.customerPoNo" ,"");
	vat.item.setValueByName("#F.status" ,"");
	vat.item.setValueByName("#F.updateFlag" ,"");
	vat.item.setValueByName("#F.customerName" ,"");
	vat.item.setValueByName("#F.contactInfo" ,"");
	vat.item.setValueByName("#F.passportNo" ,"");	
	vat.item.setValueByName("#F.shopCode" ,"");
	vat.item.setValueByName("#F.storeArea" ,"");
	vat.item.setValueByName("#F.affidavit" ,"");
	//vat.item.setValueByName("#F.flightArea" ,"");
	vat.item.setValueByName("#F.flightNo" ,"");	
	vat.item.setValueByName("#F.valuable" ,"");
	vat.item.setValueByName("#F.breakable" ,"");
	vat.item.setValueByName("#F.terminal" ,"");
	vat.item.setValueByName("#F.SFaultReason" ,"");
	vat.item.setValueByName("#F.DFaultReason" ,"");
	vat.item.setValueByName("#F.DFaultReason" ,"");
	vat.item.setValueByName("#F.soDelUpdateHeadCode" ,"");
	vat.item.setValueByName("#F.StorageCode" ,"");
	vat.item.setValueByName("#F.StorageCode1" ,"");
}



function getEmployeeInfo() {
    
    if ("" !=vat.item.getValueByName("#F.employeeCode")) {
        vat.item.setValueByName("#F.employeeCode",vat.item.getValueByName("#F.employeeCode").toUpperCase());

        vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
        vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=getEmployeeInfo";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("" != vat.bean().vatBeanOther.employeeName ){
                    vat.item.setValueByName("#F.employeeName", vat.bean().vatBeanOther.employeeName);
                    vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
                    vat.bean().vatBeanOther.executeEmployeeName=vat.item.getValueByName("#F.employeeName");
			     }else{
			        vat.item.setValueByName("#F.employeeName", "");
			     	alert("員工代號錯誤，請重新輸入！");
	 				vat.form.item.setFocus( "#F.employeeCode" );			     	
			     }
		     }
		});
    }else{
    	vat.item.setValueByName("#F.employeeName","");
    }
}


function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(null != vFormId && "" != vFormId && 0 != vFormId){
    	var url = "/erp/So_Delivery:edit:20101014.page?formId=" + vFormId + "&cusWhCode=MD";	
		sc=window.open(url, '入提單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.moveTo(0,0);
		sc.resizeTo(screen.availWidth,screen.availHeight);
	}
}

function doProcess(){
	// alert("doProcess 1");
	var vsProcess = vat.item.getValueByName("#F.processItem");
	var vsEmployeeCode = vat.item.getValueByName("#F.employeeCode");
	var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    var vOrderNo = vat.item.getGridValueByName("orderNo", nItemLine);
    var vDeliveryDate = vat.item.getValueByName("#F.startDeliveryDate");
    var vAllow=true;
    if("" ==  vsEmployeeCode ){
    	vAllow = false;
		alert("請輸入執行人員工號");
		vat.form.item.setFocus( "#F.employeeCode" );	
	}	
	if("" ==  vsProcess ){
	    vAllow = false;
		alert("未選擇要處理的項目，請重新選擇");
		vat.form.item.setFocus( "#F.processItem" );	
	}		
		
	if( vFormId == "" || vFormId == "0"){
		 vAllow = false;
		alert("查無此張單號，請重新點選查詢按鈕");
	}
	if( vsProcess == "CLOSE" && vDeliveryDate == ""){
		vAllow = false;
		alert("請輸入提領日期");		
		vat.form.item.setFocus( "#F.startDeliveryDate" );	
	}
	if(vAllow){
		/* 某些狀態下會把提領日期清空
			["待收貨","待建檔","待提領","結案","作廢","取消","退貨","待銷退","已退款"],
			["W_PICK","W_CREATE","W_DELIVERY","CLOSE","VOID","CANCEL","RETURN","W_RETURN","REFUND"]];
	    */
		if( vsProcess == "W_PICK" || vsProcess == "W_CREATE" || vsProcess == "W_DELIVERY" ){
			vDeliveryDate = "";
		}	
		vat.bean().vatBeanOther.executeEmployee= vsEmployeeCode;
		vat.bean().vatBeanOther.status= vsProcess;
		vat.bean().vatBeanOther.formId= vFormId;
		vat.bean().vatBeanOther.deliveryNo= vOrderNo;
		vat.bean().vatBeanOther.pDeliveryDate = vDeliveryDate;
		vat.bean().vatBeanOther.mode=vat.bean().vatBeanOther.accessType;

	    vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		                    "&process_object_method_name=updateStatusByHeadId" }, 
		                    {other: true, 
		                     funcSuccess: function() {
		                     	if( "undefined" != typeof vat.bean().vatBeanOther.statusName)
	    							vat.item.setGridValueByName("status", nItemLine, vat.bean().vatBeanOther.statusName);
	    					 	if( "undefined" != typeof vat.bean().vatBeanOther.pDeliveryDate )
	    							vat.item.setGridValueByName("deliveryDate", nItemLine, vat.bean().vatBeanOther.pDeliveryDate);
	    					
		                    }
		 });
	}
}

//列印
function openReportWindow() {
	var vbAllow = true;
	if("" == vat.item.getValueByName("#F.employeeCode")){
		vbAllow = false;
   		alert("請輸入員工代號");
   	}
    if(vbAllow){
	    vat.bean().vatBeanOther.executeEmployeeCode= vat.item.getValueByName("#F.employeeCode");
	    vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.loginBrandCode");
	    vat.bean().vatBeanOther.reportFunctionCode = vat.item.getValueByName("#F.reportList");
	    vat.bean().vatBeanOther.orderNo = vat.item.getValueByName("#F.orderNo");
	    vat.bean().vatBeanOther.startOrderDate = vat.item.getValueByName("#F.startOrderDate");
	    vat.bean().vatBeanOther.endOrderDate = vat.item.getValueByName("#F.endOrderDate");
	    vat.bean().vatBeanOther.customerName = vat.item.getValueByName("#F.customerName");
	    vat.bean().vatBeanOther.contactInfo = vat.item.getValueByName("#F.contactInfo");
	    vat.bean().vatBeanOther.passportNo = vat.item.getValueByName("#F.passportNo");
	    vat.bean().vatBeanOther.startDeliveryDate = vat.item.getValueByName("#F.startDeliveryDate");
	    vat.bean().vatBeanOther.endDeliveryDate = vat.item.getValueByName("#F.endDeliveryDate");
	    vat.bean().vatBeanOther.flightNo = vat.item.getValueByName("#F.flightNo");
	    //vat.bean().vatBeanOther.flightArea = vat.item.getValueByName("#F.flightArea");
	    vat.bean().vatBeanOther.startFlightDate = vat.item.getValueByName("#F.startFlightDate");
	    vat.bean().vatBeanOther.endFlightDate = vat.item.getValueByName("#F.endFlightDate");
	    vat.bean().vatBeanOther.storeArea = vat.item.getValueByName("#F.storeArea");
	    vat.bean().vatBeanOther.shopCode = vat.item.getValueByName("#F.shopCode");
	    vat.bean().vatBeanOther.status = vat.item.getValueByName("#F.status");
	    vat.bean().vatBeanOther.lockFlag = vat.item.getValueByName("#F.lockFlag");
	    vat.bean().vatBeanOther.valuable = vat.item.getValueByName("#F.valuable");
	    vat.bean().vatBeanOther.breakable = vat.item.getValueByName("#F.breakable");
	    vat.bean().vatBeanOther.startScheduleDeliveryDate = vat.item.getValueByName("#F.startScheduleDeliveryDate");
	    vat.bean().vatBeanOther.endScheduleDeliveryDate = vat.item.getValueByName("#F.endScheduleDeliveryDate");
	    vat.bean().vatBeanOther.customerPoNo = vat.item.getValueByName("#F.customerPoNo");
	    vat.bean().vatBeanOther.terminal = vat.item.getValueByName("#F.terminal");
	    vat.bean().vatBeanOther.sortKey = vat.item.getValueByName("#F.sortKey");
	    vat.bean().vatBeanOther.sortSeq = vat.item.getValueByName("#F.sortSeq");
	    vat.bean().vatBeanOther.cwCode = "VD"
	    vat.block.submit(function () {
	        return "process_object_name=soDeliveryService" + "&process_object_method_name=getReportConfig";
	    }, {
	        other: true,
	        funcSuccess: function () {
	            //vat.item.setValueByName("#F.remark2", vat.bean().vatBeanOther.reportUrl);
	            eval(vat.bean().vatBeanOther.reportUrl);
	
	           
	        }
	    });
	}
   
}


//調撥單匯出明細

String.prototype.RTrim = function() 
{ 
return this.replace(/(\s*$)/g, ""); 
}

function RTrim(str){
var i;
for(i=str.length-1;i>=0;i--){
if(str.charAt(i)!=" "&&str.charAt(i)!=" ") break;
}
str = str.substring(0,i+1);
return str;
}

function exportFormData(arg){
    // alert("export to xml file...")
	var beanName = "S"==arg?"SO_DELIVERY":"SO_DELIVERYS";
	
	//alert(beanName);
	// var customer = vat.utils.escape(RTrim(vat.item.getValueByName("#F.customerName")));
	
	var customer = (RTrim(vat.item.getValueByName("#F.customerName")));
	var url;
    	url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&processObjectName=soDeliveryService" +
              "&processObjectMethodName=findViewByMap" +
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&orderTypeCode"          + "=" +"DZN"+  
	                  "&ifHd"                   + "=" + "Y" +    
	                  "&orderNo"                + "=" + vat.item.getValueByName("#F.orderNo") +     
	                  "&startOrderDate"         + "=" + vat.item.getValueByName("#F.startOrderDate") +     
	                  "&endOrderDate"           + "=" + vat.item.getValueByName("#F.endOrderDate"  ) +     
	                  "&customerName"           + "=" + customer +     
                      "&contactInfo"            + "=" + vat.item.getValueByName("#F.contactInfo") + 
                      "&passportNo"             + "=" + vat.item.getValueByName("#F.passportNo") +    
                      "&flightNo"               + "=" + vat.item.getValueByName("#F.flightNo" ) +            
                      "&status"                 + "=" + vat.item.getValueByName("#F.status") +     
	                  "&startFlightDate"        + "=" + vat.item.getValueByName("#F.startFlightDate") +     
					  "&endFlightDate"          + "=" + vat.item.getValueByName("#F.endFlightDate") +      
                      "&flightNo"               + "=" + vat.item.getValueByName("#F.flightNo" ) +     
                      "&storeArea"              + "=" + vat.item.getValueByName("#F.storeArea") +
                      "&shopCode"               + "=" + vat.item.getValueByName("#F.shopCode" ) +
                      "&startDeliveryDate"      + "=" + vat.item.getValueByName("#F.startDeliveryDate" ) +     
                      "&endDeliveryDate"        + "=" + vat.item.getValueByName("#F.endDeliveryDate" ) + 
                   "&startScheduleDeliveryDate" + "=" + vat.item.getValueByName("#F.startScheduleDeliveryDate" ) +     
                     "&endScheduleDeliveryDate" + "=" + vat.item.getValueByName("#F.endScheduleDeliveryDate" ) + 
                      "&lockFlag"               + "=" + vat.item.getValueByName("#F.lockFlag" ) + 
                      "&valuable"               + "=" + vat.item.getValueByName("#F.valuable" ) + 
                      "&breakable"              + "=" + vat.item.getValueByName("#F.breakable" ) + 
                      "&customerPoNo"           + "=" + vat.item.getValueByName("#F.customerPoNo") +
                      "&SFaultReason"           + "=" + vat.item.getValueByName("#F.SFaultReason" ) + 
                      "&DFaultReason"           + "=" + vat.item.getValueByName("#F.DFaultReason" ) +
                      "&terminal"               + "=" + vat.item.getValueByName("#F.terminal" )+    
                      "&startExpiryDate"        + "=" + vat.item.getValueByName("#F.startExpiryDate"   ) +     
                      "&endExpiryDate"          + "=" + vat.item.getValueByName("#F.endExpiryDate" ) + 
                      "&affidavit"              + "=" + vat.item.getValueByName("#F.affidavit" ) +
                      "&expiryReturnNo"         + "=" + vat.item.getValueByName("#F.expiryReturnNo" )+ 
                      "&storageCode"            + "=" + vat.item.getValueByName("#F.storageCode" )+ 
                      "&storageCode1"            + "=" + vat.item.getValueByName("#F.storageCode1" )+ 
                      "&soDelUpdateHeadCode"    + "=" + vat.item.getValueByName("#F.soDelUpdateHeadCode" )+
                      "&oriDate"    + "=" + vat.item.getValueByName("#F.oriDate" )+
                      "&oriFlight"    + "=" + vat.item.getValueByName("#F.oriFlight" )+
                      "&contactInfo_cs"    + "=" + vat.item.getValueByName("#F.contactInfo_cs" )+
                      "&updateType"    + "=" + vat.item.getValueByName("#F.updateType" )+
                      "&updateContent"    + "=" + vat.item.getValueByName("#F.updateContent" )+
              	      "&QueryType=ExcelExport" 
    var width = "200";
    var height = "30";
    url = encodeURI(encodeURI(url));
    // alert(url + "/customer:" + customer + "/");
    vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : function(){
    			// alert('success for excel export');
    			window.open(url, '入提單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});

}