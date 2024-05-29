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


function kweSearchSoBlock(){
   if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
	     formId             : document.forms[0]["#formId"            ].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	 };
  }

  kweSoDetail();
 
}

function kweSoDetail(){
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = false;
    vat.block.create(vnB_So_Head, {
	id: "vatBlock_So_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='9' style=' border-bottom-width: 10px; border-left-width: 0px; border-color: #ffffff;'",
	title:"", 
	rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#F.salesOrderId"  , type:"TEXT", size:6, mode:"HIDDEN"},
	         {name:"#F.brandCode"     , type:"TEXT", size:4, mode:"READONLY"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
     {items:[{name:"#L.salesOrderDate", type:"LABEL", value:"交易日期"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#F.salesOrderDate", type:"DATE" , bind:"salesOrderDate", mode:"READONLY"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
     {items:[{name:"#L.posMachineCode", type:"LABEL", value:"機檯代號"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#F.posMachineCode", type:"TEXT" , bind:"posMachineCode", mode:"READONLY"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 {items:[{name:"#L.customerPoNo" , type:"LABEL", value:"單號"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
 	 {items:[{name:"#F.customerPoNo" , type:"TEXT" , bind:"searchCustomerPoNo", size:20 , mode:"READONLY"}], td:"style='background-color:#FFFF33; border-color:#FFFF33;'"}]}
	  ],
		beginService:"",
		closeService:""
	});	

    
    var vnB_So_Detail = 5;
    vat.item.make(vnB_So_Detail, "indexNo"   , {type:"IDX"  , desc:"序號"       });
	vat.item.make(vnB_So_Detail, "itemCode"  , {type:"TEXT" , size:20, desc:"品號"   });
	vat.item.make(vnB_So_Detail, "itemName"  , {type:"TEXT" , size:50,desc:"品名"   });
	vat.item.make(vnB_So_Detail, "quantity"  , {type:"NUMB" , size:10,desc:"數量"});
	vat.item.make(vnB_So_Detail, "actualSalesAmount"  , {type:"NUMM" , desc:"金額", size:12}); 
	vat.item.make(vnB_So_Detail, "lineId"    , {type:"ROWID"});
	vat.block.pageLayout(vnB_So_Detail, {
														id                  : "vatSoDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxSoService()",
														loadSuccessAfter    : "pageLoadSuccess()",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "5"
														});
  	if(typeof document.forms[0]["#salesOrderId"] != 'undefined'){														
	    vat.item.setValueByName("#F.salesOrderId",document.forms[0]["#salesOrderId" ].value);
	    vat.item.setValueByName("#F.brandCode",document.forms[0]["#brandCode" ].value);
	    vat.item.setValueByName("#F.salesOrderDate",document.forms[0]["#salesOrderDate" ].value);
	    vat.item.setValueByName("#F.posMachineCode",document.forms[0]["#posMachineCode" ].value);
	    vat.item.setValueByName("#F.customerPoNo",document.forms[0]["#customerPoNo" ].value);
	    vat.block.pageDataLoad(vnB_So_Detail, vnCurrentPage = 1);
	}

}


function loadBeforeAjxSoService(){
	//alert("After loadBeforeAjxSoService");
	var processString = "process_object_name=soDeliveryService&process_object_method_name=getAJAXPageSoData" +
	                    "&salesOrderId=" + vat.item.getValueByName("#F.salesOrderId") +
	                    "&brandCode=" +  vat.item.getValueByName("#F.brandCode")+
	                    "&customerPoNo=" +  vat.item.getValueByName("#F.customerPoNo");
	                   
	
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

