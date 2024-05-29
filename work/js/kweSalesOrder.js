/***
  *	說明：kweSalesOrder.js
 *
 *	建立：Mac 
 *	修改：
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 *
 */
 
function kweSalesOrderDetail(){
	vat.formD.item(0, "F01", {type:"TEXT", size: 1, maxLen:20, desc:"NO" , mode: "READONLY"});
	vat.formD.item(0, "F02", {type:"TEXT", size:16, maxLen:20, desc:"品號", onchange: "kweSoItemOnchange();"});
	vat.formD.item(0, "F03", {type:"TEXT", size:20, maxLen:20, desc:"品名", mode: "READONLY"});
	vat.formD.item(0, "F04", {type:"TEXT", size: 8, maxLen:20, init: "vat.formD.r('0.00')", desc:"台幣零售價"});
	vat.formD.item(0, "F05", {type:"TEXT", size: 8, maxLen:20, init: "vat.formD.r(   '0')", desc:"數量"});
	vat.formD.item(0, "F06", {type:"TEXT", size: 8, maxLen:20, init: "vat.formD.r('0.00')", desc:"原幣單價"});
	vat.formD.item(0, "F07", {type:"TEXT", size: 8, maxLen:20, init: "vat.formD.r('0.00')", desc:"原幣總價"});
	vat.formD.item(0, "F08", {type:"TEXT", size: 8, maxLen:20, init: "vat.formD.r('0.00')", desc:"台幣零售價"});	
	vat.formD.item(0, "F09", {type:"TEXT", size: 8, maxLen:20, init: "vat.formD.r(   '0')", desc:"核銷數量", mode: "HIDDEN"});
	vat.formD.item(0, "F10", {type:"IDX" ,   desc:"行號"});
	vat.formD.item(0, "F11", {type:"RADIO" , desc:"狀態"});
	vat.formD.item(0, "F12", {type:"BUTTON", desc:"鎖定"});
	vat.formD.item(0, "F13", {type:"DEL",    desc:"刪除"});
	vat.formD.item(0, "F14", {type:"MSG",    desc:"訊息"});	
	vat.formD.pageLayout(0, {	pageSize: 10,
														canDataDelete: false,
														canDataAppend: true,
														canDataModify: true,
														beginService: "",
														closeService: "",
														itemMouseinService  : "",
														itemMouseoutService : "",
														appendBeforeService : "kweSoPageAppendBeforeMethod()",
														appendAfterService  : "kweSoPageAppendAfterMethod()",
														deleteBeforeService : "",
														deleteAfterService  : "",
														loadBeforeAjxService: "kweSoPageLoadMethod()",
														loadSuccessAfter    : "kweSoPageLoadSuccess()",
														loadFailureAfter    : "",
														saveBeforeAjxService: "kweSoPageSaveMethod()",
														saveSuccessAfter    : "kweSoPageSaveSuccess()",
														saveFailureAfter    : ""
														});
	vat.formD.pageDataLoad(0, 1);
	
}



function kweSoItemOnchange(){
	//---------------
  // 取得目前元素 Name: vat.form.item.list[vat.form.item.current].name
  // 						 ID: vat.form.item.list[vat.form.item.current].id, event.srcElement.id
  //          value: vat.formD.itemCurrentValue()  
	// 						座標: vat.formD.itemYX()
	//    資料庫索引行號: vat.formD.itemIndexNo()
	//
	vat.formD.itemSelectBind(new Array(new Array("#searchCustomerType", "x", true), 
														 				 new Array("一般 ", "唯讀", "提示"), new Array("1", "2", "3")));
	vat.formD.itemSelectBind(new Array(new Array("#form.countryCode", "2"), 
														 				 new Array("一般 ", "唯讀", "提示"), new Array("1", "2", "3")));
	vat.formD.itemSelectBind(new Array(new Array("#form.paymentTermCode", "3", true), 
														 				 new Array(), new Array()));														 				 
	var sItemCode =	vat.formD.itemCurrentValue();
	var nItemLine = vat.formD.itemIndexNo();
	if (sItemCode != "") {
		var processString = "process_object_name=poPurchaseOrderHeadService"+
												"&process_object_method_name=getAJAXLineData"+
												"&brandCode="+document.forms[0]["#form.brandCode"].value+ 
												"&orderTypeCode="+document.forms[0]["#form.orderTypeCode"].value+ 
												"&itemCode="+sItemCode ;
		vat.ajax.startRequest(processString, function(){ 
  		if (vat.ajax.handleState()){
				if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
					vat.formD.itemDataBind(0, nItemLine, "unitPrice"          , vat.ajax.getValue("UnitPrice"      		 , vat.ajax.xmlHttp.responseText));
     			vat.formD.itemDataBind(0, nItemLine, "foreignUnitCost"    , vat.ajax.getValue("ForeignUnitCost"		 , vat.ajax.xmlHttp.responseText));         
     			vat.formD.itemDataBind(0, nItemLine, "lastForeignUnitCost", vat.ajax.getValue("LastForeignUnitCost", vat.ajax.xmlHttp.responseText));
     			vat.formD.itemDataBind(0, nItemLine, "itemCName"          , vat.ajax.getValue("ItemCName"					 , vat.ajax.xmlHttp.responseText));         
   			}
				// calculateTotalAmount();
				// checkCloseHead();
 			}
		});
	}else{
		vat.formD.itemDataBind(0, nItemLine, "unitPrice"          ,  0);
  	vat.formD.itemDataBind(0, nItemLine, "foreignUnitCost"    ,  0);         
  	vat.formD.itemDataBind(0, nItemLine, "lastForeignUnitCost",  0);
  	vat.formD.itemDataBind(0, nItemLine, "itemCName"          , "");         
		//culateTotalAmount();
		//checkCloseHead();
  }
}  



function kweSoPageSaveMethod(){
	var processString = "process_object_name=poPurchaseOrderHeadService"+
											"&process_object_method_name=updateAJAXPageLinesData"+
											"&headId=1000136"+
		  								"&exchangeRate=3";
		  								/*											
											"&gridData="+sReturnGridData+
		  								"&gridLineFirstIndex="+nGridLineFirstIndex+
		  								"&gridRowCount="+nPageSize+
											*/
	return processString;		  								
}		  								



function kweSoPageSaveSuccess(){
	// alert("更新成功");
}


	
function kweSoPageLoadMethod(){	
	var processString = "process_object_name=poPurchaseOrderHeadService"+
											"&process_object_method_name=getAJAXPageData"+
											"&headId=1000136"
											/*
											"&startPage="+pnThisPageNo+
											"&pageSize="+pageSize;
											*/
	return processString;											
}		  								



function kweSoPageLoadSuccess(){
	// alert("載入成功");	
}



function kweSoPageAppendBeforeMethod(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}



function kweSoPageAppendAfterMethod(){
	// return alert("新增完畢");
}



