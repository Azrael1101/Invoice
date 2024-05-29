/*** 
 *	檔案: imLocaton.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by david
 *  	All rights reserved.
 *  </pre>
 */

vat.debug.disable();
var afterSavePageProcess = "";

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;
var vnB_Total = 4;

function kweImBlock(){
  kweInitial();
  kweButtonLine();
  kweHeader();
    if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");                                                                                                                                                                                                                                          
		vat.tabm.createButton(0 ,"xTab1","主檔資料" ,"vatMasterDiv","images/tab_master_data_dark.gif","images/tab_master_data_light.gif", false , "");
		vat.tabm.createButton(0 ,"xTab2","明細資料" ,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false , ""); 
		vat.tabm.createButton(0 ,"xTab3","金額資料" ,"vatTotalDiv","images/tab_total_amount_dark.gif","images/tab_total_amount_light.gif", false , "showTotalCountPage()");
      	vat.tabm.createButton(0 ,"xTab5","簽核資料" ,"vatApprovalDiv","images/tab_approval_data_dark.gif","images/tab_approval_data_light.gif",document.forms[0]["#processId"].value==""?"none":"inline");
  	}
  kweMaster();
  kweDetail();
  kweTotal();
  kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
             vat.item.getValueByName("#F.orderTypeCode"), 
             vat.item.getValueByName("#F.orderNo"),
             document.forms[0]["#loginEmployeeCode"].value );
	//簽核意見寫死為Y             
	vat.item.setAttributeByName("#F.approvalResult","readOnly",true);           
 
  	doFormAccessControl();
}

function kweInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#brandCode" ].value,
          //organizationCode  	: document.forms[0]["#organizationCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          loginUser				: document.forms[0]["#loginEmployeeCode" ].value,
          employeeCode			: document.forms[0]["#loginEmployeeCode" ].value,
          loginEmployeeCode		: document.forms[0]["#loginEmployeeCode" ].value,
          currentRecordNumber : 0,
		  lastRecordNumber    : 0	
        };
   	vat.bean.init(	
  		function(){
				return "process_object_name=imDeliveryMainService&process_object_method_name=executeInitial"; 
    	},{
    		other: true
    	}
    );
  }
}


function kweHeader(){ 
	var orderTypes = vat.bean("orderTypes");
	var branchMode = vat.bean("branchCode");
	//如果不是T2
	if(branchMode == "2"){
		branchMode = "";
	}else{
		branchMode = " style='display:none'";
	}
	
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"出貨單維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{ name:"#L.orderTypeCode", type:"LABEL", value:"單別" }]},
				{items:[{ name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode", init:orderTypes, size:20, mode:"READONLY" }]},
				{items:[{ name:"#L.orderNo", type:"LABEL", value:"單號", size:10 }]},
				{items:[{ name:"#F.orderNo", type:"text", bind:"orderNo", back:false, size:20, mode:"READONLY" },
						{ name:"#F.headId",  type:"text", bind:"headId", back:false, mode:"HIDDEN" }]},
				{items:[{ name:"#L.shipDate", type:"LABEL", value:"出貨日期" }]},
				{items:[{ name:"#F.shipDate", type:"DATE", bind:"shipDate", size:8, mode:"READONLY" }]},
				{items:[{ name:"#L.brandCode", type:"LABEL", value:"品牌" }]},
				{items:[{ name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:10, mode:"HIDDEN" },
						{ name:"#F.brandName", type:"TEXT", bind:"brandName", size:10, mode:"READONLY"}]},
				{items:[{ name:"#L.status", type:"LABEL", value:"狀態" }]},
				{items:[{ name:"#F.status", type:"TEXT", bind:"status", mode:"HIDDEN"},
						{ name:"#F.statusName", type:"TEXT", bind:"statusName", size:10, back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
			 	{items:[{ name:"#L.customerCode", type:"LABEL" , value:"客戶代號" }]},
			 	{items:[{ name:"#F.customerCode", type:"TEXT", bind:"customerCode", mode:"READONLY", size:10 },
			 			{ name:"#F.customerName", type:"TEXT", bind:"customerName", mode:"READONLY", size:6 }]},
			 	{items:[{ name:"#L.scheduleShipDate", type:"LABEL" , value:"預計出貨日" }]},
			 	{items:[{ name:"#F.scheduleShipDate", type:"TEXT", bind:"scheduleShipDate", mode:"READONLY", size:10 }]},
			 	{items:[{ name:"#L.salesOrderType", type:"LABEL" , value:"銷貨單別" }]},
			 	{items:[{ name:"#F.salesOrderType", type:"TEXT", bind:"salesOrderType", mode:"READONLY", size:20 }]},
			 	{items:[{ name:"#L.salesOrderNo", type:"LABEL" , value:"銷貨單號" }]},
			 	{items:[{ name:"#F.salesOrderNo", type:"TEXT", bind:"salesOrderNo", mode:"READONLY", size:20 }],td:"colSpan=3"}
			]},
			{row_style:"", cols:[
			 	{items:[{ name:"#L.shipAddress", type:"LABEL" , value:"送貨地址" }]},
			 	{items:[{ name:"#F.shipAddress", type:"TEXT", bind:"shipAddresss", mode:"READONLY", size:50 }],td:"colSpan=5"},
			 	{items:[{ name:"#L.createdBy", type:"LABEL" , value:"填單人員" }]},
		 		{items:[{name:"#F.createdBy"  	, type:"TEXT", bind:"createdBy", mode:"HIDDEN"},
		 				{name:"#F.createdByName", type:"TEXT",   bind:"createdByName", size:20, mode:"READONLY"}]},
		 		{items:[{ name:"#L.creationDate", type:"LABEL" , value:"填單日期" }]},
			 	{items:[{ name:"#F.creationDate", type:"TEXT", bind:"creationDate", mode:"READONLY", size:10 }]}
			 	]},
			{row_style:branchMode, cols:[
				{items:[{ name:"#L.exportDeclNo", type:"LABEL" , value:"報關單號" }]},
			 	{items:[{ name:"#F.exportDeclNo", type:"TEXT", bind:"exportDeclNo", mode:"READONLY", size:20, eChange:"getCMHead()" }],td:"colSpan=5"},
			 	{items:[{ name:"#L.exportDeclDate", type:"LABEL" , value:"報關日期" }]},
			 	{items:[{ name:"#F.exportDeclDate", type:"DATE", bind:"exportDeclDate", mode:"READONLY", size:10 }]},
			 	{items:[{ name:"#L.exportDeclType", type:"LABEL" , value:"報關類別" }]},
			 	{items:[{ name:"#F.exportDeclType", type:"TEXT", bind:"exportDeclType", mode:"READONLY", size:10 }]}
			]},
			{row_style:branchMode, cols:[
				{items:[{ name:"#L.latestExportDeclNo", type:"LABEL" , value:"退關後報關單號" }]},
			 	{items:[{ name:"#F.latestExportDeclNo", type:"TEXT", bind:"latestExportDeclNo", mode:"READONLY", size:20 }],td:"colSpan=5"},
			 	{items:[{ name:"#L.latestExportDeclDate", type:"LABEL" , value:"退關後報關日期" }]},
			 	{items:[{ name:"#F.latestExportDeclDate", type:"DATE", bind:"latestExportDeclDate", mode:"READONLY", size:10 }]},
			 	{items:[{ name:"#L.latestExportDeclType", type:"LABEL" , value:"退關後報關類別" }]},
			 	{items:[{ name:"#F.latestExportDeclType", type:"TEXT", bind:"latestExportDeclType", mode:"READONLY", size:10 }]}
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

function kweMaster(){
var sufficientQuantityDeliverys = [["","",false],["是","否"],["Y","N"]];
var invoiceTypeCodes = vat.bean("invoiceTypeCodes");
var taxTypes = vat.bean("taxTypes");
var homeDeliverys = vat.bean("homeDeliverys");
var paymentCategorys = vat.bean("paymentCategorys");
var allCurrency = vat.bean("allCurrency");


vat.block.create(vnB_Master, {
	id: "vatMasterDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[
		{row_style:"", cols:[
			{items:[{name:"#L.shopCode"             , type:"LABEL" , value:"專櫃代號"}]},
			{items:[{name:"#F.shopCode"             , type:"TEXT"  ,  bind:"shopCode", size:12, mode:"HIDDEN"},
					{name:"#F.shopName"             , type:"TEXT"  ,  bind:"shopName", size:20, mode:"READONLY"}]},
			{items:[{name:"#L.sufficientQuantityDelivery"    , type:"LABEL" , value:"足量出貨"}]},	 
			{items:[{name:"#F.sufficientQuantityDelivery"    , type:"SELECT"  ,  bind:"sufficientQuantityDelivery", mode:"READONLY", init:sufficientQuantityDeliverys }]},
			{items:[{name:"#L.superintendentCode"    , type:"LABEL" , value:"訂單負責人"}]},	 
			{items:[{name:"#F.superintendentCode"    , type:"TEXT"  ,  bind:"superintendentCode", size:12, mode:"READONLY"},
					{name:"#F.superintendentName"    , type:"TEXT"  ,  bind:"superintendentName", size:12, mode:"READONLY"}]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.contactPerson"             , type:"LABEL" , value:"客戶聯絡窗口"}]},
			{items:[{name:"#F.contactPerson"             , type:"TEXT"  ,  bind:"contactPerson", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.contactTel"    , type:"LABEL" , value:"客戶聯絡電話"}]},	 
			{items:[{name:"#F.contactTel"    , type:"TEXT"  ,  bind:"contactTel", mode:"READONLY"}]},
			{items:[{name:"#L.receiver"    , type:"LABEL" , value:"收貨人"}]},	 
			{items:[{name:"#F.receiver"    , type:"TEXT"  ,  bind:"receiver", mode:"READONLY"}]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.customerPoNo"     , type:"LABEL" , value:"客戶訂單編號"}]},
			{items:[{name:"#F.customerPoNo"     , type:"TEXT"  ,  bind:"customerPoNo", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.quotationCode"    , type:"LABEL" , value:"報價單編號"}]},	 
			{items:[{name:"#F.quotationCode"    , type:"TEXT"  ,  bind:"quotationCode", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.countryCode"    , type:"LABEL" , value:"國別"}]},	 
			{items:[{name:"#F.countryCode"    , type:"TEXT"  ,  bind:"countryCode", mode:"READONLY"},
					{name:"#F.countryName"    , type:"TEXT"  ,  bind:"countryName", mode:"READONLY"}]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.paymentTermCode"     , type:"LABEL" , value:"付款條件"}]},
			{items:[{name:"#F.paymentTermCode"     , type:"TEXT"  ,  bind:"paymentTermCode", size:12, mode:"READONLY"},
					{name:"#F.paymentTermName"     , type:"TEXT"  ,  bind:"paymentTermName", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.scheduleCollectionDate"    , type:"LABEL" , value:"付款日"}]},	 
			{items:[{name:"#F.scheduleCollectionDate"    , type:"TEXT"  ,  bind:"scheduleCollectionDate", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.currencyCode"    , type:"LABEL" , value:"幣別"}]},	 
			{items:[{name:"#F.currencyCode"    , type:"SELECT",  bind:"currencyCode", init:allCurrency, mode:"READONLY"},
					{name:"#L.exportExchangeRate", type:"LABEL", value:" 兌換匯率 "},
					{name:"#F.exportExchangeRate", type:"TEXT", bind:"exportExchangeRate", size:4, mode:"readOnly"}
			]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.invoiceAddress"     , type:"LABEL" , value:"發票地址"}]},
			{items:[{name:"#F.invoiceAddress"     , type:"TEXT"  ,  bind:"invoiceAddresss", size:12, mode:"READONLY"}],td:"colSpan=3"},
			{items:[{name:"#L.guiCode"    , type:"LABEL" , value:"統一編號"}]},	 
			{items:[{name:"#F.guiCode"    , type:"TEXT"  ,  bind:"guiCode", mode:"READONLY"}]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.invoiceTypeCode"     , type:"LABEL" , value:"發票類型"}]},
			{items:[{name:"#F.invoiceTypeCode"     , type:"SELECT"  ,  bind:"invoiceTypeCode", mode:"READONLY", init:invoiceTypeCodes}]},
			{items:[{name:"#L.taxType"    , type:"LABEL" , value:"稅別"}]},	 
			{items:[{name:"#F.taxType"    , type:"SELECT"  ,  bind:"taxType", size:12, mode:"READONLY", init:taxTypes}]},
			{items:[{name:"#L.taxRate"    , type:"LABEL" , value:"稅率"}]},	 
			{items:[{name:"#F.taxRate"    , type:"TEXT"  ,  bind:"taxRate", mode:"READONLY"}]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.defaultWarehouseCode"     , type:"LABEL" , value:"庫別"}]},
			{items:[{name:"#F.defaultWarehouseCode"     , type:"TEXT"  ,  bind:"defaultWarehouseCode", size:12, mode:"READONLY"},
					{name:"#F.defaultWarehouseName"     , type:"TEXT"  ,  bind:"defaultWarehouseName", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.invoiceNo"    , type:"LABEL" , value:"發票號碼"}]},	 
			{items:[{name:"#F.invoiceNo"    , type:"TEXT"  ,  bind:"invoiceNo", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.homeDelivery"    , type:"LABEL" , value:"運送方式"}]},	 
			{items:[{name:"#F.homeDelivery"    , type:"SELECT"  ,  bind:"homeDelivery", mode:"READONLY", init:homeDeliverys}]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.promotionCode"     , type:"LABEL" , value:"活動代號"}]},
			{items:[{name:"#F.promotionCode"     , type:"TEXT"  ,  bind:"promotionCode", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.exportCommissionRate"    , type:"LABEL" , value:"手續費率"}]},	 
			{items:[{name:"#F.exportCommissionRate"    , type:"TEXT"  ,  bind:"exportCommissionRate", size:12, mode:"READONLY"}]},
			{items:[{name:"#L.discountRate"    , type:"LABEL" , value:"折扣比率"}]},	 
			{items:[{name:"#F.discountRate"    , type:"TEXT"  ,  bind:"discountRate", mode:"READONLY"}]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.remark1"     , type:"LABEL" , value:"備註一"}]},
			{items:[{name:"#F.remark1"     , type:"TEXT"  ,  bind:"remark1", size:80, mode:"READONLY"}],td:"colSpan=3"},
			{items:[{name:"#L.paymentCategory"    , type:"LABEL" , value:"付款方式"}]},	 
			{items:[{name:"#F.paymentCategory"    , type:"SELECT"  ,  bind:"paymentCategory", mode:"READONLY", init:paymentCategorys}]}
	 	]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.remark2"     , type:"LABEL" , value:"備註二"}]},
			{items:[{name:"#F.remark2"     , type:"TEXT"  ,  bind:"remark2", size:80, mode:"READONLY"}],td:"colSpan=3"},
			{items:[{name:"#L.attachedInvoice"    , type:"LABEL" , value:"隨附發票"}]},	 
			{items:[{name:"#F.attachedInvoice"    , type:"TEXT"  , bind:"attachedInvoice", mode:"HIDDEN"},
					{name:"#F.attachedInvoiceExpress"    , type:"TEXT"  , bind:"attachedInvoiceExpress", mode:"READONLY"}]}
	 	]}
 	 ],
		beginService:"",
		closeService:""			
	});
	
}

function kweDetail(){

	var branchMode = vat.bean("branchCode");
	//如果不是T2
	if(branchMode == "2"){
		branchMode = "READONLY";
	}else{
		branchMode = "HIDDEN";
	}
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	var varCanGridModify = false;
	//如果不適是足量出貨 且單子暫存中	
 	if("SAVE" == vat.item.getValueByName("#F.status") && "N" == vat.item.getValueByName("#F.sufficientQuantityDelivery")){
  		varCanGridModify = true;
	}
	
    vat.item.make(vnB_Detail, "indexNo", 					{type:"IDX" , view:"fixed", desc:"序號"});
	vat.item.make(vnB_Detail, "itemCode", 					{type:"TEXT", view:"", size:15, maxLen:20, desc:"品號", mode:"READONLY"});
	vat.item.make(vnB_Detail, "itemCName", 					{type:"TEXT", view:"", size:18, maxLen:20, desc:"品名", mode:"READONLY"});
	vat.item.make(vnB_Detail, "warehouseCode", 				{type:"TEXT", view:"", size:12, maxLen:12, desc:"庫別", mode:"READONLY"});
	vat.item.make(vnB_Detail, "warehouseName", 				{type:"TEXT", view:"", size:20, maxLen:20, desc:"庫名", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "originalForeignUnitPrice", 	{type:"NUMB", view:"", size: 8, maxLen:12, desc:"原幣單價", mode:branchMode});
	vat.item.make(vnB_Detail, "originalUnitPrice", 			{type:"NUMB", view:"", size: 8, maxLen:12, desc:"本幣單價", mode:"READONLY"});
	vat.item.make(vnB_Detail, "actualForeignUnitPrice", 	{type:"NUMB", view:"", size: 8, maxLen:12, desc:"原幣折扣後單價", mode:branchMode});
	vat.item.make(vnB_Detail, "actualUnitPrice", 			{type:"NUMB", view:"", size: 8, maxLen:12, desc:"本幣折扣後單價", mode:"READONLY"});
	vat.item.make(vnB_Detail, "salesQuantity", 				{type:"NUMB", view:"shift", size: 8, maxLen: 8, desc:"預計出貨數", mode:"READONLY"});
	vat.item.make(vnB_Detail, "shipQuantity", 				{type:"NUMB", view:"shift", size: 8, maxLen: 8, desc:"實計出貨數" , mask:"cccc" ,onchange:"changeItemData(2)"});
	vat.item.make(vnB_Detail, "originalForeignShipAmt", 	{type:"NUMB", view:"shift", size: 8, maxLen:20, desc:"原幣金額", mode:branchMode});
	vat.item.make(vnB_Detail, "originalShipAmount", 		{type:"NUMB", view:"shift", size: 8, maxLen:20, desc:"本幣金額", mode:"READONLY"});
	vat.item.make(vnB_Detail, "actualForeignShipAmt", 		{type:"NUMB", view:"shift", size: 8, maxLen:20, desc:"原幣折扣後金額", mode:branchMode});
	vat.item.make(vnB_Detail, "actualShipAmount", 			{type:"NUMB", view:"shift", size: 8, maxLen:20, desc:"本幣折扣後金額", mode:"READONLY"});
	vat.item.make(vnB_Detail, "shipTaxAmount", 				{type:"NUMB", view:"shift", size: 8, maxLen:20, desc:"完稅金額", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "importDeclNo", 				{type:"TEXT", view:"shift", size: 16, maxLen:14, desc:"報單號碼", mode:branchMode});
	vat.item.make(vnB_Detail, "importDeclDate", 			{type:"TEXT", view:"shift", size: 8, maxLen:20, desc:"報單日期", mode:branchMode});
	vat.item.make(vnB_Detail, "importDeclType", 			{type:"TEXT", view:"shift", size: 4, maxLen:20, desc:"報單類別", mode:branchMode});
	vat.item.make(vnB_Detail, "importDeclSeq", 				{type:"NUMB", view:"shift", size: 4, maxLen:20, desc:"報單項次", mode:branchMode});
	vat.item.make(vnB_Detail, "lotNo", 						{type:"TEXT", view:"shift", size: 12, maxLen:20, desc:"批號", mode:branchMode});
	vat.item.make(vnB_Detail, "combineCode", 				{type:"TEXT", view:"shift", size: 8, maxLen:8, desc:"組合代號", mode:branchMode});
	vat.item.make(vnB_Detail, "lineId", 					{type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", 				{type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "message", 					{type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnB_Detail, {
								id: "vatDetailDiv",	
								pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,									
							    beginService: "",
								closeService: "",
							    itemMouseinService  : "",
								itemMouseoutService : "",
							    appendBeforeService : "kweSoPageAppendBeforeMethod()",
							    appendAfterService  : "kweSoPageAppendAfterMethod()",								deleteBeforeService : "",
								deleteAfterService  : "",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "",
								loadFailureAfter    : "",
								eventService        : "",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()",
								saveFailureAfter    : ""});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}


function kweTotal(){
	var branchMode = vat.bean("branchCode");
	if(branchMode == "2"){
		vat.block.create(vnB_Total, {
		id: "vatTotalDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
			rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.originalTotalFrnShipAmt"    , type:"LABEL" , value:"原幣出貨總金額"}]},
				{items:[{name:"#F.originalTotalFrnShipAmt"    , type:"TEXT"  , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalOriginalShipAmount"    , type:"LABEL" , value:"出貨總金額"}]},	 
				{items:[{name:"#F.totalOriginalShipAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalDeductionFrnAmount"    , type:"LABEL" , value:"原幣出貨折讓"}]},
				{items:[{name:"#F.totalDeductionFrnAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalDeductionAmount"    , type:"LABEL" , value:"出貨折讓"}]},	 
				{items:[{name:"#F.totalDeductionAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.shipTaxFrnAmount"    , type:"LABEL" , value:"原幣營業稅"}]},
				{items:[{name:"#F.shipTaxFrnAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.shipTaxAmount"    , type:"LABEL" , value:"營業稅"}]},	 
				{items:[{name:"#F.shipTaxAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalOtherFrnExpense"	, type:"LABEL" , value:"原幣其他費用"}]},
				{items:[{name:"#F.totalOtherFrnExpense"	, type:"TEXT"  , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalOtherExpense"    , type:"LABEL" , value:"其他費用"}]},	 
				{items:[{name:"#F.totalOtherExpense"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.expenseForeignAmount"    , type:"LABEL" , value:"原幣手續費"}]},
				{items:[{name:"#F.expenseForeignAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.expenseLocalAmount"    , type:"LABEL" , value:"手續費"}]},	 
				{items:[{name:"#F.expenseLocalAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.actualTotalFrnShipAmt"    , type:"LABEL" , value:"原幣實際出貨金額"}]},
				{items:[{name:"#F.actualTotalFrnShipAmt"    , type:"TEXT"  , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalActualShipAmount"    , type:"LABEL" , value:"實際出貨金額"}]},	 
				{items:[{name:"#F.totalActualShipAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalNoneTaxFrnShipAmount"    , type:"LABEL" , value:"原幣未稅金額"}]},
				{items:[{name:"#F.totalNoneTaxFrnShipAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.totalNoneTaxShipAmount"    , type:"LABEL" , value:"未稅金額"}]},	 
				{items:[{name:"#F.totalNoneTaxShipAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalItemQuantity"    , type:"LABEL" , value:"商品數量"}]},
				{items:[{name:"#F.totalItemQuantity"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]}
	 	 ],
			beginService:"",
			closeService:""			
		});
	}else{
		vat.block.create(vnB_Total, {
		id: "vatTotalDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
			rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.totalOriginalShipAmount"    , type:"LABEL" , value:"出貨總金額"}]},	 
				{items:[{name:"#F.totalOriginalShipAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalDeductionAmount"    , type:"LABEL" , value:"出貨折讓"}]},	 
				{items:[{name:"#F.totalDeductionAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.shipTaxAmount"    , type:"LABEL" , value:"營業稅"}]},	 
				{items:[{name:"#F.shipTaxAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalOtherExpense"    , type:"LABEL" , value:"其他費用"}]},	 
				{items:[{name:"#F.totalOtherExpense"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.expenseLocalAmount"    , type:"LABEL" , value:"手續費"}]},	 
				{items:[{name:"#F.expenseLocalAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalActualShipAmount"    , type:"LABEL" , value:"實際出貨金額"}]},	 
				{items:[{name:"#F.totalActualShipAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
		 	{row_style:"", cols:[
				{items:[{name:"#L.totalNoneTaxShipAmount"    , type:"LABEL" , value:"未稅金額"}]},	 
				{items:[{name:"#F.totalNoneTaxShipAmount"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalItemQuantity"    , type:"LABEL" , value:"商品數量"}]},
				{items:[{name:"#F.totalItemQuantity"    , type:"TEXT"  , size:12, mode:"READONLY"}]}
		 	]}
	 	 ],
			beginService:"",
			closeService:""			
		});
	}
}


function kweSoPageAppendBeforeMethod(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}

function kweSoPageAppendAfterMethod(){
	// return alert("新增完畢");
}


/*
	顯示出貨單Line
*/
function loadBeforeAjxService(){
	var processString = "process_object_name=imDeliveryMainService&process_object_method_name=getAJAXPageData" + 
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&formStatus=" + vat.item.getValueByName("#F.status");
																					
	return processString;											
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "process_object_name=imDeliveryMainService&process_object_method_name=updateAJAXPageLinesData" + 
						"&headId=" + vat.item.getValueByName("#F.headId") + 
						"&status=" + vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
}

/*
	計算實際出貨金額
*/
function changeItemData(actionId) {
    var nItemLine = vat.item.getGridLine();
	var vOriginalUnitPrice = vat.item.getGridValueByName("originalUnitPrice", nItemLine);
	var vOriginalForeignUnitPrice = vat.item.getGridValueByName("originalForeignUnitPrice", nItemLine);
	var vActualUnitPrice = vat.item.getGridValueByName("actualUnitPrice", nItemLine);
	var vActualForeignUnitPrice = vat.item.getGridValueByName("actualForeignUnitPrice", nItemLine);
	var vSalesQuantity = vat.item.getGridValueByName("salesQuantity", nItemLine).replace(/^\s+|\s+$/, '');
	var vShipQuantity = vat.item.getGridValueByName("shipQuantity", nItemLine).replace(/^\s+|\s+$/, '');
	
	vat.item.setGridValueByName("itemCode", nItemLine, vItemCode);
	vat.item.setGridValueByName("warehouseCode", nItemLine, vWarehouseCode);	
            if(isNaN(vQuantity) || vShipQuantity > vSalesQuantity){
               alert("明細資料頁籤中第" + nItemLine + "項明細的數量欄位必須為正確數值！");
            }else{
               vat.ajax.XHRequest(
               {
                   post:"process_object_name=imDeliveryMainService" +
                            "&process_object_method_name=getAJAXItemData" +
                            "&itemIndexNo=" + nItemLine +
                            "&shipQuantity=" + vShipQuantity +
                            "&originalUnitPrice=" + vOriginalUnitPrice +
                            "&originalForeignUnitPrice=" + vOriginalForeignUnitPrice +
                            "&actualUnitPrice=" + vActualUnitPrice +
                            "&actualForeignUnitPrice=" + vActualForeignUnitPrice +
                            "&taxType=" + vat.item.getValueByName("#F.taxType") +
                            "&taxRate=" + vat.item.getValueByName("#F.taxRate"),
                   find: function changeItemDataRequestSuccess(oXHR){
                       vat.item.setGridValueByName("originalShipAmount", nItemLine, vat.ajax.getValue("OriginalShipAmount", oXHR.responseText));
                       vat.item.setGridValueByName("originalForeignShipAmt", nItemLine, vat.ajax.getValue("OriginalForeignShipAmt", oXHR.responseText));
                       vat.item.setGridValueByName("actualShipAmount", nItemLine, vat.ajax.getValue("ActualShipAmount", oXHR.responseText));
                       vat.item.setGridValueByName("actualForeignShipAmt", nItemLine, vat.ajax.getValue("ActualForeignShipAmt", oXHR.responseText));
                       vat.item.setGridValueByName("shipTaxAmount", nItemLine, vat.ajax.getValue("ShipTaxAmount", oXHR.responseText));
                   }
               });          
            }
}


/*
	顯示合計的頁面
*/
function showTotalCountPage() {
	var branchMode = vat.bean("branchCode");
    var processString = "process_object_name=imDeliveryMainService&process_object_method_name=executeCountTotalAmount" + 
	                    "&headId=" + document.forms[0]["#formId"].value +
	                    "&formStatus=" + vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
	                    
		    vat.ajax.startRequest(processString, function () {
				if (vat.ajax.handleState()) {
					if(branchMode == "2"){
						vat.item.setValueByName("#F.totalOriginalShipAmount",vat.ajax.getValue("TotalOriginalShipAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalActualShipAmount",vat.ajax.getValue("TotalActualShipAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.shipTaxAmount",vat.ajax.getValue("ShipTaxAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.shipTaxFrnAmount",vat.ajax.getValue("ShipTaxFrnAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalNoneTaxShipAmount",vat.ajax.getValue("TotalNoneTaxShipAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalNoneTaxFrnShipAmount",vat.ajax.getValue("TotalNoneTaxFrnShipAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalItemQuantity",vat.ajax.getValue("TotalItemQuantity", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.originalTotalFrnShipAmt",vat.ajax.getValue("OriginalTotalFrnShipAmt", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.actualTotalFrnShipAmt",vat.ajax.getValue("ActualTotalFrnShipAmt", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalDeductionAmount",vat.ajax.getValue("TotalDeductionAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalDeductionFrnAmount",vat.ajax.getValue("TotalDeductionFrnAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.expenseLocalAmount",vat.ajax.getValue("ExpenseLocalAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.expenseForeignAmount",vat.ajax.getValue("ExpenseForeignAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalOtherFrnExpense",vat.ajax.getValue("TotalOtherFrnExpense", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalOtherExpense",vat.ajax.getValue("TotalOtherExpense", vat.ajax.xmlHttp.responseText));
					}else{
						vat.item.setValueByName("#F.totalOriginalShipAmount",vat.ajax.getValue("TotalOriginalShipAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalActualShipAmount",vat.ajax.getValue("TotalActualShipAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.shipTaxAmount",vat.ajax.getValue("ShipTaxAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalNoneTaxShipAmount",vat.ajax.getValue("TotalNoneTaxShipAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalItemQuantity",vat.ajax.getValue("TotalItemQuantity", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalDeductionAmount",vat.ajax.getValue("TotalDeductionAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.expenseLocalAmount",vat.ajax.getValue("ExpenseLocalAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.totalOtherExpense",vat.ajax.getValue("TotalOtherExpense", vat.ajax.xmlHttp.responseText));
					}
				}
			});	
}

function kweButtonLine(){
	var showTotal = [["","",false],["是","否"],["Y","N"]];
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[/*{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"/erp/Im_Delivery:search:20091009.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			*/
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SIGNING")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_delivery_report.gif", eClick:'openReportWindow()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　顯示金額"},
	 			{name:"#F.total"       , type:"SELECT" ,init:showTotal},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.address"     , type:"IMG"    ,value:"地址列印",   src:"", eClick:'openAddressWindow()'}
	 			/*,
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }
	 			*/],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}


function doSubmit(formAction){
	var alertMessage ="是否確定?";
		if("SIGNING" == formAction && vat.item.getValueByName("#F.shipDate") != vat.item.getValueByName("#F.scheduleShipDate")){
			alertMessage = "出貨日期不等於預計出貨日，是否確認送出?";
		}else if("SIGNING" == formAction){
			alertMessage = "是否確定送出?";
		}else if ("SAVE" == formAction){
		 	alertMessage = "是否確定暫存?";
		}else if("SUBMIT_BG" == formAction){
			alertMessage = "是否確定背景送出?";
		}
		
		if(confirm(alertMessage)){
		    var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');
		    var processId             = document.forms[0]["#processId"      ].value;
		    var beforeChangeStatus	  = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		    var employeeCode          = document.forms[0]["#loginEmployeeCode" ].value;
		    var shipDate 			  = vat.item.getValueByName("#F.shipDate");
		    var assignmentId          = document.forms[0]["#assignmentId"      ].value;
		    var approvalResult        = true; //vat.item.getValueByName("#F.approvalResult");
		    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
			var organizationCode 	  = "TM";
		    var userType			  = document.forms[0]["#userType"      ].value;
		    
			      vat.block.pageDataSave(vnB_Detail, 
				    {  funcSuccess:function(){
				    	  vat.bean().vatBeanOther.formId          	= formId;
						  vat.bean().vatBeanOther.beforeChangeStatus= beforeChangeStatus;
						  vat.bean().vatBeanOther.assignmentId      = assignmentId;
						  vat.bean().vatBeanOther.processId       	= processId;
						  vat.bean().vatBeanOther.formAction      	= formAction;
						  vat.bean().vatBeanOther.approvalResult  	= approvalResult;
						  vat.bean().vatBeanOther.approvalComment 	= approvalComment;
						  vat.bean().vatBeanOther.shipDate 		  	= shipDate;
						  vat.bean().vatBeanOther.loginEmployeeCode	= employeeCode;
						  vat.bean().vatBeanOther.employeeCode		= employeeCode;
						  vat.bean().vatBeanOther.organizationCode	= organizationCode;
						  vat.bean().vatBeanOther.userType			= userType;
						  
						  if("SUBMIT_BG" == formAction){
				      		vat.block.submit(function(){return "process_object_name=imDeliveryAction"+
				                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
				    	  }else{
						  	vat.block.submit(function(){return "process_object_name=imDeliveryAction"+
				                    "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
				          }
			          	}
		          	}
		          );
		}

}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_DELIVERY" +
		"&levelType=ERROR" +
        "&processObjectName=imDeliveryMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imDeliveryAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

/*
function doPageDataSave(){
    vat.block.pageDataSave(vnB_Detail);
}
*/

function getCMHead(){
	vat.item.setValueByName("#F.exportDeclNo", vat.item.getValueByName("#F.exportDeclNo").replace(/^\s+|\s+$/, ''))
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=imDeliveryMainService"+
                    "&process_object_method_name=executeFindCM"+
                    "&headId=" + vat.item.getValueByName("#F.headId") + 
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&exportDeclNo=" + vat.item.getValueByName("#F.exportDeclNo"),
        find: function getOriginalDeliverySuccess(oXHR){
        vat.item.setValueByName("#F.exportDeclDate" ,vat.ajax.getValue("ExportDeclDate", oXHR.responseText));
        vat.item.setValueByName("#F.exportDeclType" ,vat.ajax.getValue("ExportDeclType", oXHR.responseText));
        vat.item.setValueByName("#F.exportExchangeRate", vat.ajax.getValue("ExportExchangeRate", oXHR.responseText));
        if(vat.item.getValueByName("#F.exportDeclNo") != null && vat.item.getValueByName("#F.exportDeclNo") !=""){
            if(vat.ajax.getValue("ExportDeclType", oXHR.responseText) == ""){
        		alert("查無報關單-" + vat.item.getValueByName("#F.exportDeclNo") + "的資料！");
        	}
        }
       }   
   });
}
/*******20171026-yao**************
function changeExchangeRate(){
    vat.ajax.XHRequest(
       {
           post:"process_object_name=buBasicDataService"+
                    "&process_object_method_name=getLastExchangeRateAJAX"+
                    "&currencyCode=" + vat.item.getValueByName("#F.currencyCode")+
                    "&currencyDate=" + vat.item.getValueByName("#F.shipDate"),
           find: function changeExchangeRateRequestSuccess(oXHR){
				vat.item.setValueByName("#F.exportExchangeRate", vat.ajax.getValue("ExportExchangeRate", oXHR.responseText));
           }   
       });
}**/

function doFormAccessControl(){
	var VprocessId = document.forms[0]["#processId"].value;    
	var Vstatus = vat.item.getValueByName("#F.status");
	var VbrandCode = vat.item.getValueByName("#F.brandCode");
	var canBeClaimed	= document.forms[0]["#canBeClaimed"].value;
	var allowApproval 	= document.forms[0]["#allowApproval"].value;
			
    if("SAVE" == Vstatus && VprocessId != ""){
  		vat.item.setAttributeByName("#F.shipDate","readOnly",false);
  		vat.item.setAttributeByName("#F.scheduleShipDate","readOnly",false);
  	}

    if("SIGNING" == Vstatus){
  		vat.item.setStyleByName("#B.save"		, "display", "none");
  		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
  		vat.item.setStyleByName("#B.message"	, "display", "none");
  	}
  	
  	if(canBeClaimed == "true"){
		vat.item.setStyleByName("#B.submit",    "display", "none");
		vat.item.setStyleByName("#B.submitBG",    "display", "none");
	}
	
	if(allowApproval == "N")
    	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
    	
  	VorderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
  	
  	//如果是查詢單子狀態
  	if(VprocessId == ""){
  		vat.item.setAttributeByName("#F.shipDate","readOnly",true);
  		vat.item.setStyleByName("#B.save"		, "display", "none");	
  		vat.item.setStyleByName("#B.submit"		, "display", "none");
  		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
  		vat.item.setStyleByName("#B.message"	, "display", "none");
  	}
  	
	if(VorderTypeCode == "ION" || VorderTypeCode == "IOW" || VorderTypeCode == "IOS"){
		vat.item.setAttributeByName("#F.total","readOnly",false);
	}else{
		vat.item.setAttributeByName("#F.total","readOnly",true);
	}
	
	if(VorderTypeCode != "ION"){
		vat.item.setStyleByName("#B.address", "display", "none");	 //Hidden 地址條列印按鍵
	}
	
  	if(VprocessId == "" && "SAVE" != status){
  		vat.tabm.displayToggle(0, "xTab5", true, false, false);
  	}
  	var userType			  = document.forms[0]["#userType"      ].value;
  	if(VprocessId != "" && userType =="SHIPPING"){
  		vat.item.setStyleByName("#B.save"		, "display", "none");
  		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
  		vat.item.setAttributeByName("#F.exportDeclNo","readOnly",false);
  		vat.item.setAttributeByName("#F.currencyCode","readOnly",false);
  		vat.item.setAttributeByName("#F.exportExchangeRate","readOnly",false);
  		vat.item.setAttributeByName("#F.latestExportDeclNo","readOnly",false);
  		vat.item.setAttributeByName("#F.latestExportDeclDate","readOnly",false);
  		vat.item.setAttributeByName("#F.latestExportDeclType","readOnly",false);
  	}

	if(VorderTypeCode == "ESF" && Vstatus =="FINISH" && VprocessId == ""){
		vat.item.setStyleByName("#B.submit"		, "display", "inline");
		vat.item.setAttributeByName("#F.latestExportDeclNo","readOnly",false);
  		vat.item.setAttributeByName("#F.latestExportDeclDate","readOnly",false);
  		vat.item.setAttributeByName("#F.latestExportDeclType","readOnly",false);
	}
}

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    vsHeadId = si_Menu_List[vat.bean().vatBeanOther.currentRecordNumber -1].menuId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	vsHeadId = si_Menu_List[vat.bean().vatBeanOther.currentRecordNumber -1].menuId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	vsHeadId = si_Menu_List[vat.bean().vatBeanOther.currentRecordNumber -1].menuId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    vsHeadId = si_Menu_List[vat.bean().vatBeanOther.currentRecordNumber -1].menuId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}
function doAfterPickerFunctionProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
		vat.item.setValueByName("#F.functionCode", vat.bean().vatBeanPicker.result[0].functionCode);
		changeFunctionName();
	}
}

function doAfterPickerParentMenuIDProcess(){
	if(vat.bean().vatBeanPicker.si_Menu_List !== null){
		vat.item.setValueByName("#F.parentMenuId", vat.bean().vatBeanPicker.si_Menu_List[0].menuId);
	}
}

function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.si_Menu_List !== null){
			si_Menu_List = vat.bean().vatBeanPicker.si_Menu_List;
	    var vsMaxSize = si_Menu_List.length;
	   
	    if( vsMaxSize === 0){
	  	vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		  
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  vsHeadId = si_Menu_List[vat.bean().vatBeanOther.currentRecordNumber -1 ].menuId;
		  refreshForm(vsHeadId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}

function refreshForm(vsHeadId){
	document.forms[0]["#formId"].value = vsHeadId;
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.block.submit(
		function(){
			return "process_object_name=siMenuService&process_object_method_name=executeInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
			vat.item.bindAll();
     	}});
}


function openReportWindow(type){ 
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    vat.bean().vatBeanOther.displayAmt = vat.item.getValueByName("#F.total");
	if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
		vat.block.submit(
					function(){return "process_object_name=imDeliveryMainService"+
								"&process_object_method_name=getReportConfig";},{other:true,
                    			funcSuccess:function(){
                    			//alert(vat.bean().vatBeanOther.reportUrl);
								eval(vat.bean().vatBeanOther.reportUrl);
					}}
		);   
     if("AFTER_SUBMIT"==type) createRefreshForm();//因為調撥單在送出後要直接列印報表，所以要有這行
}


/*
function openReportWindow(){
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=imDeliveryMainService"+
                    "&process_object_method_name=findEncryTextPrint"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode"),
        find: function getOriginalDeliverySuccess(oXHR){
        	var encryText = vat.ajax.getValue("encryText", oXHR.responseText);
            var reportUrl = vat.ajax.getValue("reportUrl", oXHR.responseText);
		    var reportFileName = vat.ajax.getValue("reportFileName", oXHR.responseText);  
		    var brandCode = vat.item.getValueByName("#F.brandCode");
		    var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
		    var orderNo = vat.item.getValueByName("#F.orderNo");
		    var displayAmt = vat.item.getValueByName("#F.total");
		    //var url = "jsp/DeliveryReport.jsp?reportUrl=" + reportUrl + "&reportFileName=" + reportFileName + "&crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1=" + orderTypeCode + "&prompt2=" + orderNo + "&prompt3=" + orderNo + "&prompt4=" + displayAmt;
		    //window.open(url,'BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		    //alert(reportUrl+"im0201.rpt?crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1=" + orderTypeCode + "&prompt2=" + orderNo + "&prompt3=" + orderNo + "&prompt4=" + displayAmt);
		    window.open(reportUrl+"im0201.rpt?crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1=" + orderTypeCode + "&prompt2=" + orderNo + "&prompt3=" + orderNo + "&prompt4=" + displayAmt,'BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
       }   
   });
}
*/

function openAddressWindow(){
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=imDeliveryMainService"+
                    "&process_object_method_name=findEncryTextAddress"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode"),
        find: function getOriginalDeliverySuccess(oXHR){
        	var encryText = vat.ajax.getValue("encryText", oXHR.responseText);
            var reportUrl = vat.ajax.getValue("reportUrl", oXHR.responseText);
		    var brandCode = vat.item.getValueByName("#F.brandCode");
		    var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
		    var orderNo = vat.item.getValueByName("#F.orderNo");
		    window.open(reportUrl + "im0208.rpt?crypto=" + encryText + "&prompt0=" + brandCode + "&prompt1=" + orderTypeCode + "&prompt2=" + orderNo,'BI', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
       }   
   });
}