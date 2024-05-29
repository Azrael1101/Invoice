

var afterSavePageProcess = "";

//頁面控制
//pageControl();
/*
	initial 
*/
function outlineBlock(){
  
  headerInitial();
  kwePoMaster();
  kwePoAmount();
}
function headerInitial(){
//var allOrderTypes = vat.bean("allOrderTypes");
//var allPriceTypes = vat.bean("allPriceTypes");
//var allCurrencys = vat.bean("allCurrencys");
vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: false,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"採購單維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L_orderType", type:"LABEL" , value:"單別<font color='red'>*</font>"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", mode:"READONLY",ceap:"#form.orderTypeCode"}]},		 
	 {items:[{name:"#L_orderNo"  , type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.orderNo"  , type:"TEXT"  ,  bind:"orderNo", size:12, mode:"READONLY",ceap:"#form.orderNo"},
	 		 {name:"#F.headId"   , type:"TEXT"  ,  bind:"headId", back:false, size:10, mode:"READONLY",ceap:"#form.headId" }]},
	 {items:[{name:"#L_purchaseOrderDate", type:"LABEL",  value:"採購日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.purchaseOrderDate", type:"DATE",  bind:"purchaseOrderDate", size:1 ,ceap:"#form.purchaseOrderDate"}]},		 
	 {items:[{name:"#L_brandCode", type:"LABEL" , value:"品牌"}]},
	 {items:[{name:"#F.brandCode", type:"TEXT"  ,  bind:"brandCode", size:8, mode:"READONLY"}]}, 
	 {items:[{name:"#L_status"   , type:"LABEL" , value:"狀態"}]},	 		 
	 {items:[{name:"#F.status"   , type:"TEXT"  ,  bind:"status", size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L_supplierCode", type:"LABEL", value:"廠商代號<font color='red'>*</font>"}]},
	 {items:[{name:"#F.supplierCode" , type:"TEXT",   bind:"supplierCode", size:12 , ceap:"#form.supplierCode"},
	 		 {name:"#F.supplierName"   , type:"TEXT"  ,  bind:"supplierName", size:12, mode:"READONLY",ceap:"#form.supplierName" }]},
	 {items:[{name:"#L_superintendentCode", type:"LABEL", value:"採購負責人<font color='red'>*</font>"}]},
	 {items:[{name:"#F.superintendentCode" , type:"TEXT",   bind:"superintendentCode", size:12,ceap:"#form.superintendentCode"},
	 		 {name:"#F.superintendentName"   , type:"TEXT"  ,  bind:"superintendentName", size:12,ceap:"#form.superintendentName"}]},
	 {items:[{name:"#L_purchaseType", type:"LABEL" , value:"採購類型<font color='red'>*</font>"}]},	 
	 {items:[{name:"#F.purchaseType", type:"SELECT",  bind:"purchaseType",ceap:"#form.purchaseType"}]},	
	 {items:[{name:"#L_inputFormEmployee", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.employeeName" , type:"TEXT",   bind:"employeeName",  mode:"READONLY", size:12}]},	 
	 {items:[{name:"#L_inputFormDate" , type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.inputFormDate" , type:"TEXT",   bind:"inputFormDate", mode:"READONLY", size:12}]}]}],	 
	 beginService:"",
	 closeService:""			
	});
}
function kwePoMaster(){
//var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
//var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
vat.block.create(vnB_master = 1, {
	id: "vatBlock_Master",generate: false, table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
	rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.invoiceTypeCode"    , type:"LABEL" , value:"發票類型"}]},	 
	 {items:[{name:"#F.invoiceTypeCode"    , type:"SELECT",  bind:"invoiceTypeCode", size:12, mode:"READONLY",ceap:"#form.invoiceTypeCode"}]},		 
	 {items:[{name:"#L.taxType"    , type:"LABEL" , value:"稅別"}]},	 
	 {items:[{name:"#F.taxType"    , type:"SELECT",  bind:"taxType", size:12, mode:"READONLY",ceap:"#form.taxType"}]},
	 {items:[{name:"#L.taxRate"    , type:"LABEL" , value:"稅率"}]},	 
	 {items:[{name:"#F.taxRate"    , type:"TEXT",  bind:"taxRate", size:12,ceap:"#form.taxRate"}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.countryCode"    , type:"LABEL" , value:"國別"}]},	 
	 {items:[{name:"#F.countryCode"    , type:"SELECT",  bind:"countryCode", size:12, mode:"READONLY",ceap:"#form.countryCode"}]},		 
	 {items:[{name:"#L.currencyCode"    , type:"LABEL" , value:"幣別"}]},	 
	 {items:[{name:"#F.currencyCode"    , type:"SELECT",  bind:"currencyCode", size:12, mode:"READONLY",ceap:"#form.currencyCode"}]},
	 {items:[{name:"#L.exchangeRate"    , type:"LABEL" , value:"匯率"}]},	 
	 {items:[{name:"#F.exchangeRate"    , type:"TEXT",  bind:"exchangeRate", size:12,ceap:"#form.exchangeRate"},
	 		 {name:"#L.currencyName"    , type:"LABEL" , value:"台幣"}]}]},
	 		 
	 {row_style:"", cols:[
	 {items:[{name:"#L.quotationCode"    , type:"LABEL" , value:"廠商報價單號"}]},	 
	 {items:[{name:"#F.quotationCode"    , type:"TEXT",  bind:"quotationCode", size:12, mode:"READONLY",ceap:"#form.quotationCode"}]},		 
	 {items:[{name:"#L.contactPerson"    , type:"LABEL" , value:"廠商聯絡窗口"}]},	 
	 {items:[{name:"#F.contactPerson"    , type:"TEXT",  bind:"contactPerson", size:12, mode:"READONLY",ceap:"#form.contactPerson"}]},
	 {items:[{name:"#L.paymentTermCode"    , type:"LABEL" , value:"付款條件"}]},	 
	 {items:[{name:"#F.paymentTermCode"    , type:"SELECT",  bind:"paymentTermCode", size:12,ceap:"#form.paymentTermCode"}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.defaultWarehouseCode"    , type:"LABEL" , value:"入庫庫別"}]},	 
	 {items:[{name:"#F.defaultWarehouseCode"    , type:"TEXT",  bind:"defaultWarehouseCode", size:12, mode:"READONLY",ceap:"#form.defaultWarehouseCode"}]},		 
	 {items:[{name:"#L.isPartialShipment"    , type:"LABEL" , value:"到貨方式"}]},	 
	 {items:[{name:"#F.isPartialShipment"    , type:"RADIO",  bind:"isPartialShipment", back:false,ceap:"#form.isPartialShipment"}]},
	 {items:[{name:"#L.paymentTermCode1"    , type:"LABEL" , value:"付款條件二"}]},	 
	 {items:[{name:"#F.paymentTermCode1"    , type:"SELECT",  bind:"paymentTermCode1", size:12,ceap:"#form.paymentTermCode1"}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.budgetYear"    , type:"LABEL" , value:"預算年月"}]},	 
	 {items:[{name:"#F.budgetYear"    , type:"SELECT",  bind:"budgetYear",  size:12, mode:"READONLY",ceap:"#form.budgetYear"},
	 		 {name:"#F.budgetMonth",    type:"TEXT",    bind:"budgetMonth", size:4,  mode:"READONLY",ceap:"#form.budgetMonth"}]},			 
	 {items:[{name:"#L.reserve1"    , type:"LABEL" , value:"國外訂單單號"}]},	 
	 {items:[{name:"#F.reserve1"    , type:"TEXT",  bind:"reserve1",size:12, mode:"READONLY",ceap:"#form.reserve1"}]},
	 {items:[{name:"#L.scheduleReceiptDate"    , type:"LABEL" , value:"預計到貨日"}]},	 
	 {items:[{name:"#F.scheduleReceiptDate"    , type:"DATE",  bind:"scheduleReceiptDate", size:12,ceap:"#form.scheduleReceiptDate"}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.categoryType"    , type:"LABEL" , value:"業種"}]},	 
	 {items:[{name:"#F.categoryType"    , type:"TEXT",  bind:"categoryType", size:12,ceap:"#form.categoryType"}]},
	 {items:[{name:"#L.asignedBudget"    , type:"LABEL" , value:"指定預算"}]},	 
	 {items:[{name:"#F.asignedBudget"    , type:"TEXT",  bind:"asignedBudget", size:12,ceap:"#form.asignedBudget"}], td:" colSpan=3"}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.purchaseAssist", type:"LABEL" , value:"採購助理"}]},	 
	 {items:[{name:"#F.purchaseAssist", type:"SELECT", bind:"purchaseAssist", size:12, ceap:"#form.purchaseAssist"}]},			 
	 {items:[{name:"#L.purchaseMember", type:"LABEL" , value:"採購人員"}]},	 
	 {items:[{name:"#F.purchaseMember", type:"SELECT", bind:"purchaseMember", size:12, ceap:"#form.purchaseMember"}]},
	 {items:[{name:"#L.purchaseMaster", type:"LABEL" , value:"採購主管"}]},	 
	 {items:[{name:"#F.purchaseMaster", type:"SELECT", bind:"purchaseMaster", size:12, ceap:"#form.purchaseMaster"}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.reserve2", type:"LABEL", value:"備註一"}]},
	 {items:[{name:"#F.reserve2", type:"TEXT",   bind:"reserve2", size:70, maxLen:200,ceap:"#form.reserve2"}], td:" colSpan=5"}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.reserve3", type:"LABEL", value:"備註二"}]},
	 {items:[{name:"#F.reserve3", type:"TEXT",   bind:"reserve3", size:70, maxLen:200 , ceap:"#form.reserve3"}], td:" colSpan=5"}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.tradeTermCode", type:"LABEL",  value:"價格條件"}]},
	 {items:[{name:"#F.tradeTermCode", type:"TEXT",   bind:"tradeTermCode", size:70, maxLen:200 , ceap:"#form.tradeTermCode"}], td:" colSpan=5"}]},
	 
	 {row_style:"", cols:[ 
	 {items:[{name:"#L.packaging",     type:"LABEL",  value:"包裝方式"}]},
	 {items:[{name:"#F.packaging",     type:"TEXT",   bind:"packaging",     size:70, maxLen:200 , ceap:"#form.packaging"}], td:" colSpan=5"}]}
 	 	],
		beginService:"",
		closeService:""			
	});
}
function kwePoAmount(){
//var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
//var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
vat.block.create(vnB_master = 3, {
	id: "vatBlock_Amount",generate: false, table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
	rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.totalProductCounts",         type:"LABEL" , value:"總數量"}]},	 		 
	 {items:[{name:"#L.totalForeignPurchaseAmount", type:"LABEL" , value:"採購金額(原幣)"}]},	 
	 {items:[{name:"#L.taxAmount",                  type:"LABEL" , value:"稅額"}]},	 
	 {items:[{name:"#L.totalUnitPriceAmount",       type:"LABEL",  value:"零售價總額"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#F.totalProductCounts",         type:"TEXT",   bind:"totalProductCounts",         size:12, mode:"READONLY",ceap:"#form.totalProductCounts"}]},		 	 
	 {items:[{name:"#L.sign",                       type:"LABEL" , value:"$"},
	 		 {name:"#F.totalForeignPurchaseAmount", type:"TEXT",   bind:"totalForeignPurchaseAmount", size:12, mode:"READONLY",ceap:"#form.totalForeignPurchaseAmount"}]},	 
	 {items:[{name:"#F.taxAmount",                  type:"TEXT",   bind:"taxAmount",                  size:12, mode:"READONLY",ceap:"#form.taxAmount"}]},
	 {items:[{name:"#F.totalUnitPriceAmount",       type:"TEXT",   bind:"totalUnitPriceAmount",       size:12,                 ceap:"#form.totalUnitPriceAmount"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.empty",                      type:"LABEL" , value:""}]},	 		 
	 {items:[{name:"#L.localSalesAmount",           type:"LABEL" , value:"採購金額(NT$)"}]},	 
	 {items:[{name:"#L.empty1",                     type:"LABEL" , value:""}], td:" colSpan=2"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.empt3",                      type:"LABEL" , value:""}]},	 		 
	 {items:[{name:"#F.totalLocalPurchaseAmount",   type:"TEXT" ,  bind:"totalLocalPurchaseAmount", size:12, mode:"READONLY",ceap:"#form.totalLocalPurchaseAmount"}]},	 
	 {items:[{name:"#L.empty4",                     type:"LABEL" , value:""}], td:" colSpan=2"}]},
	 {row_style:"", cols:[	 
	 {items:[{name:"#L.empty5",                     type:"LABEL" , value:"總預算狀態"}], td:" colSpan=4"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.totalBudget",                type:"LABEL" , value:"總預算年度"}]},	 		 
	 {items:[{name:"#F.totalBudget",                type:"TEXT" ,  bind:"totalBudget", size:12, mode:"READONLY",ceap:"#form.totalBudget"}],td:"colspan=3"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.totalAppliedBudget",         type:"LABEL" , value:"已使用預算"}]},	 		 
	 {items:[{name:"#L.totalSigningBudget",         type:"LABEL" , value:"簽核中預算"}]},	 
	 {items:[{name:"#L.totalUsedPriceAmount",       type:"LABEL" , value:"本次使用預算"}]},	 
	 {items:[{name:"#L.totalRemainderBudget",       type:"LABEL",  value:"剩餘可使用預算"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#F.totalAppliedBudget",         type:"TEXT",   bind:"totalAppliedBudget",   size:12, mode:"READONLY",ceap:"#form.totalAppliedBudget"}]},		 	 
	 {items:[{name:"#F.totalSigningBudget",         type:"TEXT",   bind:"totalSigningBudget",   size:12, mode:"READONLY",ceap:"#form.totalSigningBudget"}]},	 
	 {items:[{name:"#F.totalUsedPriceAmount",       type:"TEXT",   bind:"totalUsedPriceAmount", size:12, mode:"READONLY",ceap:"#form.totalUsedPriceAmount"}]},
	 {items:[{name:"#F.totalRemainderBudget",       type:"TEXT",   bind:"totalRemainderBudget", size:12,                 ceap:"#form.totalRemainderBudget"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.budgetYear",                 type:"LABEL" , value:"預算年度"}]},	 
	 {items:[{name:"#F.budgetYear",                 type:"SELECT", bind:"budgetYear", size:12, mode:"READONLY",ceap:"#form.budgetYear"}]},		 
	 {items:[{name:"#L.reserve1",                   type:"LABEL",  value:"國外訂單單號"}]},	 
	 {items:[{name:"#F.reserve1",                   type:"TEXT",   bind:"reserve1",size:12, mode:"READONLY",ceap:"#form.reserve1"}]},
	 {items:[{name:"#L.scheduleReceiptDate",        type:"LABEL",  value:"預計到貨日"}]},	 
	 {items:[{name:"#F.scheduleReceiptDate",        type:"DATE",   bind:"scheduleReceiptDate", size:12,ceap:"#form.scheduleReceiptDate"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.reserve2",                   type:"LABEL",  value:"備註一"}]},
	 {items:[{name:"#F.reserve2",                   type:"TEXT",   bind:"reserve2", size:70, maxLen:200,ceap:"#form.reserve2"}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.reserve3",                   type:"LABEL", value:"備註二"}]},
	 {items:[{name:"#F.reserve3",                   type:"TEXT",   bind:"reserve3", size:70, maxLen:200 , ceap:"#form.reserve3"}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.tradeTermCode",              type:"LABEL", value:"價格條件"}]},
	 {items:[{name:"#F.tradeTermCode",              type:"TEXT",  bind:"tradeTermCode", size:70, maxLen:200 , ceap:"#form.tradeTermCode"}], td:" colSpan=5"}]}
 	 	],
		beginService:"",
		closeService:""			
	});
}
function detailInitial() {
	var statusTmp = document.forms[0]["#form.status"].value;
	var branchCode = document.forms[0]["#branchCode"].value;	
	var varCanDataDelete = false;
	var varCanDataAppend = false;
	var varCanDataModify = false;
	if( statusTmp == "SAVE" || statusTmp == "REJECT" ){
		varCanDataDelete = true;
		varCanDataAppend = true;
		varCanDataModify = true;		
	}
	var vnB_Detail = 2;
	vat.item.make(vnB_Detail, "indexNo",                {type:"IDX",  desc:"序號"});                                                            
	vat.item.make(vnB_Detail, "itemCode",               {type:"TEXT", size:16, maxLen:20, desc:"品號",         onchange:"onChangeItemCode()"});      
	vat.item.make(vnB_Detail, "itemCName",              {type:"TEXT", size:20, maxLen:20, desc:"品名",         mode:"READONLY"});                    
	vat.item.make(vnB_Detail, "onHandQty",              {type:"TEXT", size:8,  maxLen:20, desc:"庫存數量",      mode:"READONLY"});                 
	vat.item.make(vnB_Detail, "unitPrice",              {type:"NUMB", size:8,  maxLen:20, desc:"台幣零售價",    mode:"READONLY"});  
	vat.item.make(vnB_Detail, "lastLocalUnitCost",      {type:"NUMB", size:8,  maxLen:20, desc:"進貨成本(NT)",  mode:"READONLY"});                        
	vat.item.make(vnB_Detail, "quantity",               {type:"NUMB", size:8,  maxLen:20, desc:"數量",         onchange:"calculateLineAmount('1')"});    
	vat.item.make(vnB_Detail, "foreignUnitCost",        {type:"NUMB", size:8,  maxLen:20, desc:"原幣單價",      onchange:"calculateLineAmount('2')"});
	vat.item.make(vnB_Detail, "foreignPurchaseAmount",  {type:"NUMB", size:8,  maxLen:20, desc:"原幣總價",      mode:"READONLY"}); 	                  
	vat.item.make(vnB_Detail, "unitPriceAmount",        {type:"NUMB", size:8,  maxLen:20, desc:"台幣總價",      mode:"READONLY"});                 
	vat.item.make(vnB_Detail, "actualPurchaseQuantity", {type:"NUMB", size:8,  maxLen:20, desc:"最終採購量"});
	if ( branchCode == "2" ){
	vat.item.make(vnB_Detail, "itemBrand",              {type:"TEXT", size:8,  maxLen:20, desc:"商品品牌",       mode:"READONLY"});
	vat.item.make(vnB_Detail, "supplierItemCode",       {type:"TEXT", size:8,  maxLen:20, desc:"廠商貨號",       mode:"READONLY"});
	vat.item.make(vnB_Detail, "margin",                 {type:"NUMB", size:8,  maxLen:20, desc:"毛利率",         mode:"READONLY"});
	vat.item.make(vnB_Detail, "lastForeignUnitCost",    {type:"NUMB", size:8,  maxLen:20, desc:"上次外幣進貨價格", mode:"READONLY"});
	vat.item.make(vnB_Detail, "lastLocalUnitCost",      {type:"NUMB", size:8,  maxLen:20, desc:"上次台幣進貨價格", mode:"READONLY"});
	vat.item.make(vnB_Detail, "nextPriceAdjustDate",    {type:"DATE", size:12,            desc:"下次變價日",      mode:"READONLY"});
	vat.item.make(vnB_Detail, "remark",                 {type:"TEXT", size:100, maxLen:400, desc:"備註"});
	} else {
	vat.item.make(vnB_Detail, "itemBrand",              {type:"TEXT", size:8,  maxLen:20, desc:"商品品牌",       mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "supplierItemCode",       {type:"TEXT", size:8,  maxLen:20, desc:"廠商貨號",       mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "margin",                 {type:"NUMB", size:8,  maxLen:20, desc:"毛利率",         mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "lastForeignUnitCost",    {type:"NUMB", size:8,  maxLen:20, desc:"上次外幣進貨價格", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "lastLocalUnitCost",      {type:"NUMB", size:8,  maxLen:20, desc:"上次台幣進貨價格", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "nextPriceAdjustDate",    {type:"DATE", size:12,            desc:"下次變價日",      mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "remark",                 {type:"TEXT", size:100, maxLen:400, desc:"備註",         mode:"HIDDEN"});
	}
	vat.item.make(vnB_Detail, "minPurchaseQuantity",    {type:"NUMB", size:8,  maxLen:20, desc:"最低採購數量",    mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "minPurchaseQuantity",    {type:"NUMB", size:8,  maxLen:20, desc:"最高採購數量",    mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "lineId",                 {type:"ROWID"});              
	vat.item.make(vnB_Detail, "status",                 {type:"RADIO", desc:"狀態", mode:"HIDDEN"});                    
	vat.item.make(vnB_Detail, "isLockRecord",           {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});                               
	vat.item.make(vnB_Detail, "isDeleteRecord",         {type:"DEL",   desc:"刪除"});                                                          
	vat.item.make(vnB_Detail, "message",                {type:"MSG",   desc:"訊息"});
	vat.block.pageLayout(vnB_Detail, {
														pageSize: 10,
														gridOverflow: "scroll",
								                        canGridDelete : varCanDataDelete,
														canGridAppend : varCanDataAppend,
														canGridModify : varCanDataModify,
														beginService: "",
														closeService: "",
							    						itemMouseinService  : "",
														itemMouseoutService : "",						
														appendBeforeService : "",
														appendAfterService  : "",
														deleteBeforeService : "",
														deleteAfterService  : "",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "",
														loadFailureAfter    : "",						
														eventService        : "",   
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														saveFailureAfter    : ""
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}
/*
	LINE ITEM CODE CHANGE
*/
function onChangeItemCode() {
	var nItemLine = vat.item.getGridLine();
	var sItemCode = vat.item.getGridValueByName("itemCode",nItemLine);
	vat.item.setGridValueByName( "unitPrice",             nItemLine);
	vat.item.setGridValueByName( "foreignUnitCost",       nItemLine);
	vat.item.setGridValueByName( "lastForeignUnitCost",   nItemLine);
	vat.item.setGridValueByName( "itemCName",             nItemLine);
	vat.item.setGridValueByName( "onHandQty",             nItemLine);
	vat.item.setGridValueByName( "quantity",              nItemLine);
	vat.item.setGridValueByName( "foreignPurchaseAmount", nItemLine);
	if (vat.item.getGridValueByName("quantity",nItemLine)=="undefined"){
		vat.item.setGridValueByName("quantity",nItemLine,0);
	}
	if (vat.item.getGridValueByName("foreignPurchaseAmount", nItemLine)=="undefined"){
		vat.item.setGridValueByName("foreignPurchaseAmount", nItemLine,0);
	}
	if (vat.item.getGridValueByName("unitPriceAmount", nItemLine)=="undefined"){
		vat.item.setGridValueByName("unitPriceAmount", nItemLine,0);
	}
	//vat.item.setGridValueByName( "unitPriceAmount", nItemLine);
	//vat.item.setGridValueByName( "actualPurchaseQuantity", nItemLine);
	
	if (sItemCode != "") {
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXLineData" + 
							"&brandCode="     + document.forms[0]["#form.brandCode"].value + 
							"&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value +
							"&itemCode="      + sItemCode +
							"&purchaseOrderDate=" + document.forms[0]["#form.purchaseOrderDate"].value;
		//alert(processString);
		//alert("execute onChangeItemCode " + sItemCode);										   
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					if(vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText) != '查無資料'){
						vat.item.setGridValueByName("unitPrice",           nItemLine, vat.ajax.getValue("UnitPrice",           vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("foreignUnitCost",     nItemLine, vat.ajax.getValue("ForeignUnitCost",     vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("lastForeignUnitCost", nItemLine, vat.ajax.getValue("LastForeignUnitCost", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("itemCName",           nItemLine, vat.ajax.getValue("ItemCName",           vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("onHandQty",           nItemLine, vat.ajax.getValue("OnHandQty",           vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("lastLocalUnitCost",   nItemLine, vat.ajax.getValue("LastLocalUnitCost",   vat.ajax.xmlHttp.responseText));
						
						vat.item.setGridValueByName("itemBrand",           nItemLine, vat.ajax.getValue("ItemBrand",           vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("supplierItemCode",    nItemLine, vat.ajax.getValue("SupplierItemCode",    vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("margin",              nItemLine, vat.ajax.getValue("Margin",              vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("maxPurchaseQty",      nItemLine, vat.ajax.getValue("MaxPurchaseQty",      vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("minPurchaseQty",      nItemLine, vat.ajax.getValue("MinPurchaseQty",      vat.ajax.xmlHttp.responseText));
						//vat.item.setGridValueByName("nextPriceAdjustDate", nItemLine, vat.ajax.getValue("NextPriceAdjustDate", vat.ajax.xmlHttp.responseText));
					}else{
						alert('查無資料，請重新輸入');
						vat.item.setGridValueByName("itemCode", nItemLine, '');
					}					
				}
				//calculateTotalAmount();
				//checkCloseHead();
			}
		});
	} else {
	}
	
	//alert("execute onChangeItemCode e");
}
/*
	計算 單筆 LINE 合計的部份
*/
function calculateLineAmount( chgType ) {
	//alert("execute calculateLineAmount s");
	var nItemLine = vat.item.getGridLine();
	var quantity = vat.item.getGridValueByName("quantity",nItemLine);
	var foreignUnitCost = vat.item.getGridValueByName("foreignUnitCost",nItemLine);
	var unitPrice = vat.item.getGridValueByName("unitPrice",nItemLine);
	var exchangeRate = document.forms[0]["#form.exchangeRate"].value;
	var foreignPurchaseAmount = parseFloat(quantity) * parseFloat(foreignUnitCost);
	var unitPriceAmount = parseFloat(quantity) * parseFloat(unitPrice);
	vat.item.setGridValueByName("foreignPurchaseAmount", nItemLine, foreignPurchaseAmount);
	vat.item.setGridValueByName("unitPriceAmount",       nItemLine, unitPriceAmount);
	//alert("execute calculateLineAmount e");
		
	if (chgType == "1")  {
		//alert("Change Order Quanity !")
		vat.item.setGridValueByName("actualPurchaseQuantity", nItemLine, quantity);
	}
}
/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	//alert( "checkDisableLine" ) ;
	//var headId = document.forms[0]["#form.headId"].value;
	//alert( "head Id=" + headId + " length=" + headId.length ) ;
	//if (null != headId && "0" != headId && headId.length > 0) {
	//	vat.form.item.enable("vatDetailDiv");	//LINE 設定為Enable
	//	return true;
	//} else {
	//	vat.form.item.disable("vatDetailDiv");  //LINE 設定為ReadOnly
	//	return false;
	//}
	return true ;
}
/*
	載入LINE資料
*/
function loadBeforeAjxService() {
	//alert("execute loadBeforeAjxService s");
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXPageData" +
						"&headId=" + document.forms[0]["#form.headId"].value +
						"&status=" + document.forms[0]["#form.status"].value;
	//alert("return " + processString);
	return processString;
}
/*
	載入LINE資料 SUCCESS
*/
function loadSuccessAfter() {
	//alert("execute loadSuccessAfter ");
}
/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
	var processString = "";
	var exchangeRate = 0;
	if (checkEnableLine()) {
		exchangeRate = document.forms[0]["#form.exchangeRate"].value;
		processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=updateAJAXPageLinesData" + 
						"&headId=" + document.forms[0]["#form.headId"].value + 
						"&status=" + document.forms[0]["#form.status"].value + 
						"&brandCode=" + document.forms[0]["#form.brandCode"].value  + 
						"&exchangeRate=" + exchangeRate + 
						"&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value ;
	}
	return processString;
}
/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
	//alert("saveSuccessAfter ->" + afterSavePageProcess );
	//vat.block.pageSearch(1);//vat.formD.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg == "") {
		if ("saveHandler" == afterSavePageProcess) {	
			execSubmitAction("SAVE");//executeCommandHandler("main", "saveHandler");
		} else if ("submitHandler" == afterSavePageProcess) {			 
			execSubmitAction("SUBMIT");//executeCommandHandler("main", "submitHandler");
		} else if ("submitBgHandler" == afterSavePageProcess) {
	    	execSubmitAction("SUBMIT_BG");
		} else if ("voidHandler" == afterSavePageProcess) {
			execSubmitAction("VOID");
		} else if ("totalCount" == afterSavePageProcess) {
			//加上要計算的HEA 欄位
			var taxType              = document.forms[0]["#form.taxType"].value;
			var exchangeRate         = document.forms[0]["#form.exchangeRate"].value;
			var purchaseType         = document.forms[0]["#form.purchaseType"].value;
			var totalUnitPriceAmount = document.forms[0]["#form.totalUnitPriceAmount"].value;

			var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXHeadTotalAmount" + 
								"&headId="       + document.forms[0]["#form.headId"].value + 
								"&taxType="      + taxType +  "&exchangeRate=" + exchangeRate + "&purchaseType=" + purchaseType +
								"&budgetYear="   + document.forms[0]["#form.budgetYear"].value +
								"&budgetMonth="  + document.forms[0]["#form.budgetMonth"].value +
								"&categoryType=" + document.forms[0]["#form.categoryType"].value ;
			vat.ajax.startRequest(processString, function () {
				if (vat.ajax.handleState()) {
					document.forms[0]["#form.totalLocalPurchaseAmount"].value   = vat.ajax.getValue("TotalLocalPurchaseAmount",   vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalForeignPurchaseAmount"].value = vat.ajax.getValue("TotalForeignPurchaseAmount", vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.taxAmount"].value                  = vat.ajax.getValue("TaxAmount",                  vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalProductCounts"].value         = vat.ajax.getValue("TotalProductCounts",         vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalBudget"].value                = vat.ajax.getValue("TotalBudget",                vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalAppliedBudget"].value         = vat.ajax.getValue("TotalAppliedBudget",         vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalRemainderBudget"].value       = vat.ajax.getValue("TotalRemainderBudget",       vat.ajax.xmlHttp.responseText);
					document.forms[0]["#form.totalSigningBudget"].value         = vat.ajax.getValue("TotalSigningBudget",         vat.ajax.xmlHttp.responseText);
					if( totalUnitPriceAmount == null || totalUnitPriceAmount==0){
						document.forms[0]["#form.totalUnitPriceAmount"].value = vat.ajax.getValue("TotalUnitPriceAmount", vat.ajax.xmlHttp.responseText);					
					}
					document.forms[0]["#form.totalUsedPriceAmount"].value = vat.ajax.getValue("TotalUsedPriceAmount", vat.ajax.xmlHttp.responseText);					
					$("div#totalRetailPrice").text(document.forms[0]["#form.totalUnitPriceAmount"].value);
					
					var errorMessage = vat.ajax.getValue("ErrorMessage", vat.ajax.xmlHttp.responseText)
					if( errorMessage != "NONE" ){
						alert(errorMessage);
					}
					
				}
			});
		} else if ("executeExport" == afterSavePageProcess) {
			exportFormData();
			//vat.block.pageSearch(1);
			//executeCommandHandlerNoBlock("main","exportDataHandler");
		}else if ("executeImport" == afterSavePageProcess) {
			importFormData();
		}
	}
	else {
		alert("\u932f\u8aa4\u8a0a\u606f " + errorMsg);
	}
	afterSavePageProcess = "" ;	
}
/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
	//alert("doSaveHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			
			afterSavePageProcess = "saveHandler";
			vat.block.pageSearch(2);
		}
	}
}
/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	//alert("doSubmitHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			afterSavePageProcess = "submitHandler";
			vat.block.pageSearch(2);
		}
	}
}
/*
	背景送出SUBMIT HEAD && LINE
*/
function doSubmitBgHandler() {
	if (confirm("是否確認背景送出?")) {		
		//save line
		afterSavePageProcess = "submitBgHandler";	
		vat.block.pageSearch(2);
	}
}
/*
	作廢VOID HEAD && LINE
*/
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		afterSavePageProcess = "voidHandler";	
		vat.block.pageSearch(2);			
	}
}
function appendBeforeService() {
	//alert("appendBeforeService()");
	return true;
}
function appendAfterService() {
	//alert("appendAfterService()");
	//loadBeforeAjxService();
}

/*
	顯示合計的頁面
*/
function showTotalCountPage() {
	var budgetYear = $('select[@id*=_budgetYear]').find('option:selected').text();
	$("div#budgetYearText").text("總預算 年度:" + budgetYear);
	if (checkEnableLine()) {
		afterSavePageProcess = "totalCount";
		vat.block.pageSearch(2);
	}
}

/*
	匯出
*/
/*
function doExport() {
	//alert("doExport");
	if (checkEnableLine()) {
		//save line
		afterSavePageProcess = "afterExport";
		vat.block.pageSearch(2);
	}
}
*/
/*
	變更匯率
*/
function changeCurrenceCode(){
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXExchangeRateByCurrencyCode" +
						"&currencyCode=" + document.forms[0]["#form.currencyCode"].value + "&organizationCode=TM" ;
	vat.ajax.startRequest(processString,  function() { 
	  if (vat.ajax.handleState()){
	    document.forms[0]["#form.exchangeRate"].value =  vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText);
		setCurrencyCodeName();
		//showTotalCountPage();
	  }
	} );
}


/*
	設定供應商資料
*/
function onChangeSupplierData() {
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplierCode" +
						"&supplierCode=" + document.forms[0]["#form.supplierCode"].value + 
						"&organizationCode=TM&brandCode=" + document.forms[0]["#form.brandCode"].value + 
						"&orderTypeCode=" + document.forms[0]["#form.orderTypeCode"].value;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			document.forms[0]["#form.supplierName"].value = vat.ajax.getValue("SupplierName", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.countryCode"].value = vat.ajax.getValue("CountryCode", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.currencyCode"].value = vat.ajax.getValue("CurrencyCode", vat.ajax.xmlHttp.responseText);
			document.forms[0]["#form.exchangeRate"].value = vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText);
    		//document.forms[0]["#form.tradeTeam"].value =  vat.ajax.getValue("TradeTeam", vat.ajax.xmlHttp.responseText);
    		document.forms[0]["#form.paymentTermCode"].value =  vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText);
			//showTotalCountPage();
		}
	});
}

function setCurrencyCodeName(){
		var currencyCodeNameObj = document.getElementById("currencyCodeName"); 
		//20090122 shan 加上幣別名稱
		var vxObjByName = document.getElementsByName("#form.currencyCode");
		var vsResult = "";
		if (vxObjByName && vxObjByName[0]){
			vxObjSelect = vxObjByName[0];
			if (vat.formD.itemIsSelect(vxObjSelect)){
				for (var i=(vxObjSelect.options.length - 1); i >= 0; i--) {
					if (vxObjSelect.options[i].selected){
						vsResult = vxObjSelect.options[i].value;
						break;
					}
				}
			}else{
				vat.debug("developer", "從 "+vat.callerName(vat.formD.itemSelectGetValue.caller)+"(), 更新的目標:"+psName+", 不是一個<SELECT>元素");
			}
		}else{
			vat.debug("developer", "從 "+vat.callerName(vat.formD.itemSelectGetValue.caller)+"(), 找不到被更新的 HTML 元素名稱"+psName);			
		}
		currencyCodeNameObj.innerHTML  = vsResult ;	
}


/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
	vat.formD.pageDataSave(0);
}

function initPo(){
	if(statusTmp == "FINISH"){
		$('div[@id^=vatCloseBottonDiv]').show();
	}else{
		$('div[@id^=vatCloseBottonDiv]').hide();
	}
	if(statusTmp == "CLOSE"){
		$('div[@id^=vatSubmitBottonDiv]').hide();
	}
	/*
	if(statusTmp == "CLOSE" || statusTmp == "FINISH"){
		$('div[@id^=vatUncomfirmBottonDiv]').show();
	}else{
		$('div[@id^=vatUncomfirmBottonDiv]').hide();
	}
	*/
	//$('div[@id^=vatUncomfirmBottonDiv]').hide();//反確認之功能尚不開放
	var currencyCode = document.forms[0]["#form.currencyCode"];
	if(currencyCode.selectedIndex == 0 ){
		currencyCode.selectedIndex = 5;
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXExchangeRateByCurrencyCode" +
							"&currencyCode=" + document.forms[0]["#form.currencyCode"].value + "&organizationCode=TM" ;
		vat.ajax.startRequest(processString,  function() { 
  		if (vat.ajax.handleState()){
    		document.forms[0]["#form.exchangeRate"].value =  vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText);
    		//AJAX 計算
    		//calculateTotalAmount();
   		setCurrencyCodeName();
  		}
  		} );
	}
	polUseDive();
}


function polUseDive(){
	if(orderTypeCode == 'POL'){
		//$('div[@id*=polUseDive]').hide();
		//$('div[@id*=polReplaceText]').text(" ");
		$('td:contains("稅額")').addClass('tdHidden');
		$('div[@id^=polUseDive]').parent().addClass('tdHidden');
		$('div[@id^=polUseDive2]').parent().parent().addClass('tdHidden');
		$('td:contains("採購金額(NT$)")').parent().addClass('tdHidden');
	}
}
function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == ""){
        formStatus = "SIGNING";
    }else if(processId != null && processId != ""){
        if(status == "SAVE" || status == "REJECT"){
            formStatus = "SIGNING";
        }else if(status == "SIGNING"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }  
    }
    return formStatus;
}
function execSubmitAction(actionId){
    
    var formId          = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var assignmentId    = document.forms[0]["#assignmentId"].value.replace(/^\s+|\s+$/, '');
    var orderTypeCode   = document.forms[0]["#form.orderTypeCode"].value;
    var processId       = document.forms[0]["#processId"].value;
    var status          = document.forms[0]["#form.status"].value.replace(/^\s+|\s+$/, '');
    var employeeCode    = document.forms[0]["#employeeCode"].value.replace(/^\s+|\s+$/, '');
    //alert(employeeCode);
    var invoiceTypeCode = ""; 
    var taxType         = "";
    var taxRate         = "";
    if("POF" != orderTypeCode){//不是國外進貨單時才要抓
    	invoiceTypeCode = document.forms[0]["#form.invoiceTypeCode"].value;
    	taxType         = document.forms[0]["#form.taxType"].value;
    	taxRate         = document.forms[0]["#form.taxRate"].value;	
    }
    var countryCode          = document.forms[0]["#form.countryCode"].value;
    var currencyCode         = document.forms[0]["#form.currencyCode"].value;
    var exchangeRate         = document.forms[0]["#form.exchangeRate"].value;
    var quotationCode        = document.forms[0]["#form.quotationCode"].value;
    var contactPerson        = document.forms[0]["#form.contactPerson"].value;
    var paymentTermCode      = document.forms[0]["#form.paymentTermCode"].value;
    var defaultWarehouseCode = document.forms[0]["#form.defaultWarehouseCode"].value;
    var isPartialShipment    = "Y";
    if(document.forms[0]["#form.isPartialShipment"][0].checked){
    	isPratialShipment = "N";
    }
    var paymentTermCode1    = document.forms[0]["#form.paymentTermCode1"].value;
    var budgetYear          = document.forms[0]["#form.budgetYear"].value;
    var reserve1            = document.forms[0]["#form.reserve1"].value;
    var scheduleReceiptDate = document.forms[0]["#form.scheduleReceiptDate"].value;
    var reserve2            = document.forms[0]["#form.reserve2"].value;
    var reserve3            = document.forms[0]["#form.reserve3"].value;
   	var tradeTermCode       = document.forms[0]["#form.tradeTermCode"].value;
   	var packaging           = document.forms[0]["#form.packaging"].value;
    var approvalResult      = "true";
    
    if(document.forms[0]["#approvalResult.result"][1].checked){
        approvalResult = "false";
    }
    var approvalComment     = document.forms[0]["#approvalResult.approvalComment"].value;
    
	var formStatus = status;
	if("SAVE" == actionId){
		formStatus = "SAVE";
	}else if("SUBMIT" == actionId){
		formStatus = changeFormStatus(formId, processId, status, approvalResult);
	}else if("SUBMIT_BG" == actionId){
		formStatus = "SIGNING";
	}else if("VOID" == actionId){
		formStatus = "VOID";
	}
	//alert(formStatus);
	vat.bean().vatBeanOther={
			beforeChangeStatus: status,
	        formStatus: formStatus,
	        employeeCode: employeeCode,
	        assignmentId: assignmentId,
	        processId: processId,
	        approvalResult: approvalResult,
	        invoiceTypeCode: invoiceTypeCode,
	        taxType: taxType,
	        taxRate: taxRate,
	        countryCode: countryCode,
	        currencyCode: currencyCode,
	        exchangeRate: exchangeRate,
	        quotationCode: quotationCode,
	        contactPerson: contactPerson,
	        paymentTermCode: paymentTermCode,
	        defaultWarehouseCode: defaultWarehouseCode,
	        isPartialShipment: isPartialShipment,
	        paymentTermCode1: paymentTermCode1,
	        budgetYear: budgetYear,
	        reserve1: reserve1,
	        scheduleReceiptDate: scheduleReceiptDate,
	        reserve2: reserve2,
	        reserve3: reserve3,
	        tradeTermCode: tradeTermCode,
	        packaging: packaging,
	        approvalComment: approvalComment
        };
	if ("SUBMIT_BG" == actionId){
		vat.block.submit(function(){return "process_object_name=poPurchaseMainAction"+
                "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
	}else{
		vat.block.submit(function(){return "process_object_name=poPurchaseMainAction"+
            	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
	}
}
/*
	匯出
*/
function doExport() {
	//save line
	afterSavePageProcess = "executeExport";
	vat.block.pageSearch(2);	
}

/*
	匯入
*/
function doImport() {
	//save line
	afterSavePageProcess = "executeImport";
	vat.block.pageSearch(2);	
}

function exportFormData(){
    var headId = document.forms[0]["#form.headId"].value;
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=PO_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=poPurchaseOrderHeadMainService" + 
              "&processObjectMethodName=findById" + 
              "&gridFieldName=poPurchaseOrderLines" + 
              "&arguments=" + headId + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '採購單明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function importFormData(){
	var width = "600";
    var height = "400";
    var headId = document.forms[0]["#form.headId"].value;
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=PO_ITEM" +
		"&importFileType=XLS" +
        "&processObjectName=poPurchaseOrderHeadMainService" + 
        "&processObjectMethodName=executeImportPoLists" +
        "&arguments=" + headId +
        "&parameterTypes=LONG" +
        "&blockId=2",
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
	
	vat.block.pageSearch(2);	// refresh page data after import data
}

function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=PO_POPURCHASE_ORDER_HEAD" +
		"&levelType=ERROR" +
        "&processObjectName=poPurchaseOrderHeadMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + document.forms[0]["#form.headId"].value +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=poPurchaseMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}


function changeCategoryType(){
    document.forms[0]["#form.categoryType"].value = document.forms[0]["#form.categoryType"].value.replace(/^\s+|\s+$/, '');
    if(document.forms[0]["#form.categoryType"].value !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=poPurchaseOrderHeadMainService"+
                    "&process_object_method_name=getPurchaseEmployeeForAJAX"+
                    "&categoryType=" + document.forms[0]["#form.categoryType"].value,                  
           find: function changeShopCodeRequestSuccess(oXHR){ 
               var purchaseAssist = vat.ajax.getValue("purchaseAssist", oXHR.responseText);
               var purchaseMember = vat.ajax.getValue("purchaseMember", oXHR.responseText);
               var purchaseMaster = vat.ajax.getValue("purchaseMaster", oXHR.responseText);
               purchaseAssist = vat.utils.strTwoInputDArray("#form.purchaseAssist", "", 'true', purchaseAssist);  
               purchaseMember = vat.utils.strTwoInputDArray("#form.purchaseMember", "", 'true', purchaseMember); 
               purchaseMaster = vat.utils.strTwoInputDArray("#form.purchaseMaster", "", 'true', purchaseMaster); 
               vat.formD.itemSelectBind(purchaseAssist);
               vat.formD.itemSelectBind(purchaseMember);
               vat.formD.itemSelectBind(purchaseMaster);        
           }   
       });
    }
}
