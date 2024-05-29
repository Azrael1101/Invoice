/***
 *	檔案: soDelivery.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Search = 5;
var vnB_Header = 1;
var vnB_master = 2;
var vnB_Detail = 3;
var vnB_Log    = 4;
var vnB_So_Head =1;


function kweBlock(){
   if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
	     passportNo         : document.forms[0]["#passportNo"        ].value,
	     customerName       : document.forms[0]["#customerName"      ].value,
	     reason1            : document.forms[0]["#reason1"           ].value,
  	     vatPickerId          : document.forms[0]["#vatPickerId"          ].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	 };
  }

  kweDetail();
 
}

function kweDetail(){
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = false;
    var vbSelectionType = "CHECK";    
    var vnB_Detail = 5;
    var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
    if(vatPickerId != null && vatPickerId != ""){
    	vat.item.make(vnB_Detail, "checkbox"                  , {type:"XBOX"});
    	vbSelectionType = "CHECK";    
    }else{
    	vbSelectionType = "NONE";
    }
    vat.block.create(vnB_So_Head, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='9' style=' border-bottom-width: 10px; border-left-width: 0px; border-color: #ffffff;'",
	title:"", 
	rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#F.salesOrderId"  , type:"TEXT", size:6, mode:"HIDDEN"},
	         {name:"#F.brandCode"     , type:"TEXT", size:4, mode:"READONLY"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
     {items:[{name:"#L.passportNo"    , type:"LABEL", value:"護照號碼"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#F.passportNo"    , type:"TEXT" , bind:"passportNo", size:20}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
     {items:[{name:"#L.tel"           , type:"LABEL", value:"電話"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#F.tel"           , type:"TEXT" , bind:"tel", size:20}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
     {items:[{name:"#L.customerName"  , type:"LABEL", value:"客戶姓名"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#F.customerName"  , type:"TEXT" , bind:"customerName", size:20}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#L.reason1"       , type:"LABEL", value:"原因1"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
 	 {items:[{name:"#F.reason1"       , type:"TEXT" , bind:"reason1", size:20 }], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"}]}
	  ],
		beginService:"",
		closeService:""
	});	

    kweButtonLine();
    var vnB_Detail = 5;
    vat.item.make(vnB_Detail, "indexNo"       , {type:"IDX"  , desc:"序號"       });
	vat.item.make(vnB_Detail, "passportNo"    , {type:"TEXT" , size:20, desc:"護照號碼"   });
	vat.item.make(vnB_Detail, "customerName"  , {type:"TEXT" , size:20, desc:"客戶姓名"   });
	vat.item.make(vnB_Detail, "tel"           , {type:"TEXT" , size:20, desc:"電話"   });
	vat.item.make(vnB_Detail, "reason1"       , {type:"TEXT" , size:20, desc:"原因1"});
	vat.item.make(vnB_Detail, "reason2"       , {type:"TEXT" , size:20, desc:"原因2"}); 
	vat.item.make(vnB_Detail, "remark1"       , {type:"TEXT" , size:20, desc:"備註1"}); 
	vat.item.make(vnB_Detail, "remark2"       , {type:"TEXT" , size:20, desc:"備註2"}); 
	vat.item.make(vnB_Detail, "headId"    , {type:"ROWID"});
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
													    blockId             : "5",
													    indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});
  	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){														
	    vat.item.setValueByName("#F.brandCode",vat.bean().vatBeanOther.loginBrandCode);
	    vat.item.setValueByName("#F.passportNo",vat.bean().vatBeanOther.passportNo);
	    vat.item.setValueByName("#F.customerName",vat.bean().vatBeanOther.customerName);
	    vat.item.setValueByName("#F.reason1",vat.bean().vatBeanOther.reason1);
	    vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
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

function loadBeforeAjxService(){
	//alert("After loadBeforeAjxSoService");
	var processString = "process_object_name=soDeliveryBlackListService&process_object_method_name=getAJAXSearchPageData" +
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&passportNo=" + vat.item.getValueByName("#F.passportNo") +
	                    "&tel=" + vat.item.getValueByName("#F.tel") +
	                    "&reason1=" + vat.item.getValueByName("#F.reason1") +
	                    "&customerName=" +  vat.item.getValueByName("#F.customerName");
	                   
	
	return processString;
}

function pageLoadSuccess(){
	//alert("kwePageLoadSuccess");
	//countTotalQuantity();
	//getTheFirstItemCategory();

}

function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	processString = "process_object_name=soDeliveryService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status");


	return processString;
}

function saveSuccessAfter() {

}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" +
		"?programId=IM_MOVEMENT" +
		"&levelType=ERROR" +
        "&processObjectName=imMovementService" +
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
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
			                                    vat.block.pageDataLoad(vnB_Detail = 5, vnCurrentPage = 1);
			                                   }
			                    });

}
function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(null != vFormId && "" != vFormId && 0 != vFormId){
    	var url = "/erp/So_Delivery:blackList:20101201.page?formId=" + vFormId;	
		sc=window.open(url, '入提常客名單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.moveTo(0,0);
		sc.resizeTo(700,800);
	}
}

