/***
  *	說明：kwePurchaseOrder.js
 *
 *	建立：Mac 
 *	修改：
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 *
 */

/**************************************************************************
	vat.formD.item(<dataGridId>, <itemName>, <option:
		desc: 
		type: TEXT(靠左文字), 
					NUMB(靠右數字),
					NUMM(靠右千分位數字),  
					CHECK, SELECT, RADIO, IMAGE, PICK
					IDX(序號), ROWID(系統行號), DEL(刪除控制), MSG(訊息)
		mask:	{C=任何字母變大寫(Caps), 9=只有數字, X=任何, A=只有大寫字母(alpha)
					}
		size:
		maxLen:			
		init: 
		valid: {} 
		mode: READONLY, HIDDEN 
		io: 00=0 none, 01=1 export, 10=2 import, 11=3 ex/import
				
*/
function kwePurchaseOrderDetail(){
	var vaItemStat = new Array(new Array("Obj name", "1"),				//*** obj name & default value 
														 new Array("一般", "唯讀", "提示"), 	//*** text
														 new Array("1", "2", "3"));					//*** value	
	vat.formD.item(0, "F01", {type:"IDX" , desc:"序"});
	vat.formD.item(0, "F02", {type:"TEXT", io:3, size:  16, maxLen:20, desc:"商品代號", mask:"AACCCCCCCCCCCCCCCCCC", eChange:"kwePoItemOnchange();"});
	vat.formD.item(0, "F03", {type:"TEXT", io:0, size:  20, maxLen:20, desc:"商品名稱", mode:"READONLY"});
	vat.formD.item(0, "F04", {type:"NUMM", io:3, size: 8.2, maxLen:20, desc:"零售單價", mode:"READONLY"});
	vat.formD.item(0, "F05", {type:"NUMM", io:3, size: 8.2, maxLen:20, desc:"進貨成本"});
	vat.formD.item(0, "F06", {type:"NUMM", io:3, size: 8.2, maxLen:20, desc:"進貨數量"});
	vat.formD.item(0, "F07", {type:"NUMM", io:3, size: 8.2, maxLen:20, desc:"原幣單價", init: "1.00"});
	vat.formD.item(0, "F08", {type:"NUMM", io:3, size:12.2, maxLen:20, desc:"原幣總價", eFocus:"kwePoItemOnFocus();"});
	vat.formD.item(0, "F09", {type:"NUMM", io:3, size:12.2, maxLen:20, desc:"台幣總價", eBlur :"kwePoItemOnBlur();"});
	vat.formD.item(0, "F10", {type:"NUMB", io:3, size: 8.2, maxLen:20, desc:"應到數量", mode:"HIDDEN"});
	vat.formD.item(0, "F11", {type:"ROWID"});
	vat.formD.item(0, "F12", {type:"SELECT", desc:"狀態", init:vaItemStat});
	vat.formD.item(0, "F13", {type:"BUTTON", desc:"鎖定", value:"進階輸入", src:"images/button_advance_input.gif", eClick:"kwePoItemOnClick();"});
	vat.formD.item(0, "F14", {type:"DEL"});
	vat.formD.item(0, "F15", {type:"MSG"});	
	vat.formD.pageLayout(0, {	pageSize: 10,
														canDataDelete: true,
														canDataAppend: true,
														canDataModify: true,
														beginService: "",
														closeService: "",
														itemMouseinService  : "",
														itemMouseoutService : "",
														deleteBeforeService : "kwePoLineDeleteBeforeMethod()",
														deleteAfterService  : "kwePoLineDeleteAfterMethod()",
														deleteAllAjxService : "kwePoPageDeleteAllMethod()",
														deleteAllSuccess    : "",
														deleteAllFailure    : "",																												
														appendBeforeService : "kwePoPageAppendBeforeMethod()",
														appendAfterService  : "kwePoPageAppendAfterMethod()",
														pageUpTopService		: "kwePoPageUpTopMethod()",														
														pageUpBeforeService	: "kwePoPageUpBeforeMethod()",
														pageUpAfterService	: "kwePoPageUpAfterMethod()",														
														pageDnBeforeService	: "kwePoPageDnBeforeMethod()",
														pageDnAfterService	: "kwePoPageDnAfterMethod()",
														pageDnBottomService	: "kwePoPageDnBottomMethod()",														
														loadBeforeAjxService: "kwePoPageLoadMethod()",
														loadSuccessAfter    : "kwePoPageLoadSuccess()",
														loadFailureAfter    : "",
														eventService			 	: "alert('event Service test');",
														eventSuccessAfter		: "",
														eventFailureAfter		: "",
														saveBeforeAjxService: "kwePoPageSaveMethod()",
														saveSuccessAfter    : "kwePoPageSaveSuccess()",
														saveFailureAfter    : ""
														});
	vat.formD.pageDataLoad(0, 1);
 	// vat.formD.pageCurrentChange(3);

}

// for tab handler test
function poHandlerToFunctionTest(){
	alert('poHandlerToFunctionTest() test ok');
}

function kwePoItemOnClick(){
	var obj = document.getElementById("vatBeginDiv");
	if (obj){
		obj.filters[0].enabled = true;
		obj.filters[0].opacity = 0.30; 
	}
		
	vat.formD.itemIdDataBind(0, "1", "B100");
	
	window.showModalDialog(
		"http://10.1.99.171:9090/erp/So_Sale,sOrder:advanceInput:1.page?actionType=viewPicker&brandCode=T1CO"+
		"&shopCode=T1CO09&priceType=1&customerType=null&vipType=null&warehouseCode=T1CO00&itemCode=CO0113BNABK"+
		"&promotionCode=null&discountRate=0.0&quantity=5.0&taxType=3&taxRate=5.0&watchSerialNo=&depositCode="+
		"&isUseDeposit=N&isEnableModify=true&promotionPrice=null&originalUnitPrice=15200.0"+
		"&actualUnitPrice=15200.0&onHandQty=0.0&originalSalesAmount=76000.0&actualSalesAmount=76000.0"+
		"&taxAmount=3619.0&warehouseManager=T1CO&warehouseEmployee=T01236&vipPromotionCode=null&deductionAmount=0.0"+
		"&serviceItemPrice=15200.0&isServiceItem=N&pageStyle=popup&pickerId=detailInput"+
		"&pickerHandlerId=pickAdvanceHander&tbcn_form_Id=main&popupFrame=true"+
		"&popupTargetName=So_SalesOrder_create_1_detailInput_80_frame&cdtkey=form.soSalesOrderItems&crowkey=0",
		"",
		"dialogHeight:330px; dialogWidth:750px; dialogTop:350px; dialogLeft:200px; status:off;");
		// edge:sunken/raised;unadorned:yes;
		
	obj.filters[0].enabled = false;			
}


function kwePoItemOnFocus(){
	// alert('test item on focus');
}


function kwePoItemOnBlur(){
	// alert('test item on blur');
}


function kwePoItemOnchange(){
	//---------------
  // 取得目前元素 Name: vat.form.item.list[vat.form.item.current].name
  // 						  ID: vat.form.item.list[vat.form.item.current].id, event.srcElement.id
  //           value: vat.formD.itemCurrentValue()  
	// 					   座標: vat.formD.itemYX()
	//    資料庫索引行號: vat.formD.itemIndexNo()
	//

	
	vat.formD.itemSelectBind(new Array(new Array("#form.purchaseType", "2"), 
														 				 new Array("一般 ", "唯讀", "提示"), new Array("1", "2", "3")));	
	var sItemCode =	vat.formD.itemCurrentValue();
	var nItemLine = vat.formD.itemIndexNo();
	if (sItemCode !== "") {
		vat.ajax.XHRequest(
		{  sId: "kwePoItemOnchange()/"+vat.callerName(kwePoItemOnchange.caller)+"()",
			post: "process_object_name=poPurchaseOrderHeadService"+
						"&process_object_method_name=getAJAXLineData"+
						"&brandCode="+document.forms[0]["#form.brandCode"].value+ 
						"&orderTypeCode="+document.forms[0]["#form.orderTypeCode"].value+ 
						"&itemCode="+sItemCode,
	 		find: function kwePoItemOnchangeAjxRequest(oXHR){ 	
	 														
				vat.formD.itemDataBind(0, nItemLine, "unitPrice"          , vat.ajax.getValue("UnitPrice"      		 , oXHR.responseText));
     		vat.formD.itemDataBind(0, nItemLine, "foreignUnitCost"    , vat.ajax.getValue("ForeignUnitCost"		 , oXHR.responseText));         
     		vat.formD.itemDataBind(0, nItemLine, "lastForeignUnitCost", vat.ajax.getValue("LastForeignUnitCost", oXHR.responseText));
     		vat.formD.itemDataBind(0, nItemLine, "itemCName"          , vat.ajax.getValue("ItemCName"					 , oXHR.responseText));
 			}
		});
	}else{
		vat.formD.itemDataBind(0, nItemLine, "unitPrice"          ,  0);
  	vat.formD.itemDataBind(0, nItemLine, "foreignUnitCost"    ,  0);         
  	vat.formD.itemDataBind(0, nItemLine, "lastForeignUnitCost",  0);
  	vat.formD.itemDataBind(0, nItemLine, "itemCName"          , "");         
  }
}  


function kwePoPageSaveMethod(){
	var processString = "process_object_name=poPurchaseOrderHeadService"+
											"&process_object_method_name=updateAJAXPageLinesData"+
											"&headId=1000136"+
											"&status=SAVE"+
											"&brandCode=T1CO"+
											"&orderTypeCode=POF"+
		  								"&exchangeRate=3";
	return processString;		  								
}	

	  								
function kwePoPageSaveSuccess(){
	// alert("更新成功");
}


function kwePoPageLoadMethod(){	
	var processString = "process_object_name=poPurchaseOrderHeadService"+
											"&process_object_method_name=getAJAXPageData"+
											"&headId=1000136";
	return processString;											
}	


function kwePoPageLoadSuccess(){
	// alert("載入成功");	
}


function kwePoPageDeleteAllMethod(){	
	var processString = "process_object_name=poPurchaseOrderHeadService"+
											"&process_object_method_name=deleteAJAXAllLinesData"+
											"&headId=100131";
	return processString;											
}	


function kwePoPageAppendBeforeMethod(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}


function kwePoPageAppendAfterMethod(){
	// return alert("新增完畢");
}


function kwePoLineDeleteBeforeMethod(){
	// return confirm("你確定要刪除嗎?"); 
	return true;
}


function kwePoLineDeleteAfterMethod(){
	// return alert("刪除完畢");
}


function kwePoPageUpTopMethod(){
	alert("自我設定訊息: 已經是第一頁了");
	return false;
}


function kwePoPageUpBeforeMethod(){
	//alert("自我設定訊息: 往上跳一頁");
	return false;
}


function kwePoPageUpAfterMethod(){
	//alert("自我設定訊息: 往上跳一頁結束");
	return false;
}


function kwePoPageDnBeforeMethod(){
	//alert("自我設定訊息: 往下跳一頁");
	return false;
}


function kwePoPageDnAfterMethod(){
	//alert("自我設定訊息: 往下跳一頁結束");
	return false;
}


function kwePoPageDnBottomMethod(){
	alert("自我設定訊息: 已經是最後一頁了");
	return false;
}
