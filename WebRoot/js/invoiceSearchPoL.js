vat.debug.disable();
var afterSavePageProcess = "";

function outlineBlock(){
    formDataInitial();
    headerInitial();
    buttonLine();
    detailInitial();
}

function formDataInitial(){ 
	if(typeof document.forms[0]["#brandCode"] != 'undefined'){
		vat.bean().vatBeanOther = {
			brandCode     : document.forms[0]["#brandCode"].value,   	
			employeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
			orderTypeCode : document.forms[0]["#orderTypeCode"].value,
			headId        : document.forms[0]["#headId"].value,
			formName      : document.forms[0]["#formName"].value,	// FiInvoice/ImReceive,
			currencyCode  : document.forms[0]["#currencyCode"].value,
			exchangeRate  : document.forms[0]["#exchangeRate"].value,
			supplierCode  : document.forms[0]["#supplierCode"].value
	    	}; 
		vat.bean.init(function(){
			return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchPoLInitial"; 
		},{other: true});
 	}
}

function headerInitial(){ 
var allOrderTypes = vat.bean("allOrderTypes");
var allCurrencyCode    = vat.bean("allCurrencyCode");
var isHide    = [[true, true], ["隱藏", "不隱藏"], ["Y", "N"]];
var allStatus = [["","","true"],
                 ["暫存","作廢","簽核中","簽核完成","結案"],
                 ["SAVE","VOID","SIGNING","FINISH","CLOSE"]];
var allSort   = [["","","true"],
                 ["品號",     "廠商貨號",      "數量",    "金額"],
                 ["ItemCode","SupplierCode","Quantity","Amount"]];
          
                 
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"Purchase Order 查詢作業", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#L.PurchaseOrderNo.",type:"LABEL",  value:"Purchase Order No."}]},	 
	 	{items:[{name:"#F.startOrderNo",    type:"TEXT",   bind:"startOrderNo", size:20},
				{name:"#L.between",         type:"LABEL",  value:" 至 "},
	 		 	{name:"#F.endOrderNo",      type:"TEXT",   bind:"endOrderNo",   size:20}]},	
	 	{items:[{name:"#L.OrderTypeCode",   type:"LABEL",  value:"Purchase Order Type"}]},	 		 
	 	{items:[{name:"#F.orderTypeCode",   type:"SELECT", bind:"orderTypeCode", init:allOrderTypes}]},
	 	{items:[{name:"#L.CurrencyCode",    type:"LABEL",  value:"幣別"}]},	 		 
	 	{items:[{name:"#F.currencyCode",	type:"SELECT", bind:"currencyCode", mode:"READONLY", init:allCurrencyCode}]}]},

	 {row_style:"", cols:[
	 	{items:[{name:"#L.Purchase Date",  type:"LABEL",  value:"Purchase Date" }]},
	 	{items:[{name:"#F.startDate",      type:"DATE",   bind:"startDate",   size:1 },
	         	{name:"#L.between",        type:"LABEL",  value:" 至 "},
	         	{name:"#F.endDate",        type:"DATE",   bind:"endDate",     size:1 }]},
	    {items:[{name:"#L.sortType",       type:"LABEL",  value:"排序方式"  }]},	 		 
	 	{items:[{name:"#F.sortType",       type:"SELECT", bind:"sortType",    size:12,  init:allSort }]},
	    {items:[{name:"#L.isHideClose",    type:"LABEL",  value:"隱藏已結案採購明細"  }]},	 		 
	 	{items:[{name:"#F.isHideClose",    type:"SELECT", bind:"isHideClose", size:12,  init:isHide }]}]}
	 ], 	 
		beginService:"",
		closeService:""			
	});
}

function buttonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search",      type:"IMG",      value:"查詢", src:"./images/button_find.gif",  eClick:"doSearch()"},
	 			{name:"SPACE",          type:"LABEL",    value:"　"},
	 			{name:"#B.clear",       type:"IMG",      value:"清除", src:"./images/button_reset.gif", eClick:"resetForm()"},
	 			{name:"SPACE",          type:"LABEL",    value:"　"},
	 	 		{name:"#B.exit",        type:"IMG",      value:"離開", src:"./images/button_exit.gif",  eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE",          type:"LABEL",    value:"　"},
	 			{name:"#B.picker",      type:"IMG",      value:"檢視", src:"./images/button_view.gif",  eClick:"doClosePicker()"}]}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
	var allOrderTypes = vat.bean("allOrderTypes");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    
    var vnB_Detail = 2;
    vat.item.make(vnB_Detail, "checkbox",         {type:"XBOX",   view:"fixed"});
    vat.item.make(vnB_Detail, "indexNo",          {type:"IDX",    view:"fixed",                           desc:"序號" });
	vat.item.make(vnB_Detail, "orderNo",          {type:"TEXT",   view:"fixed", size:12, mode:"READONLY", desc:"Order No" });
	vat.item.make(vnB_Detail, "orderTypeCode",    {type:"SELECT", view:"",      size:10, mode:"READONLY", desc:"Order Type", init:allOrderTypes });
	vat.item.make(vnB_Detail, "supplierCode",     {type:"TEXT",   view:"",      size:12, mode:"READONLY", desc:"Supplier No" });
	vat.item.make(vnB_Detail, "supplierName",     {type:"TEXT",   view:"",      size:12, mode:"READONLY", desc:"Supplier Name" });
	vat.item.make(vnB_Detail, "itemCode",         {type:"TEXT",   view:"",      size:16, mode:"READONLY", desc:"Item Code" });
	vat.item.make(vnB_Detail, "supplierItemCode", {type:"TEXT",   view:"",      size:16, mode:"READONLY", desc:"廠商貨號"});
	vat.item.make(vnB_Detail, "itemCname",        {type:"TEXT",   view:"",      size:20, mode:"READONLY", desc:"Item CName" });
	vat.item.make(vnB_Detail, "asignedBudget",    {type:"NUMM",   view:"",      size:8,  mode:"READONLY", desc:"數量" });
	vat.item.make(vnB_Detail, "totalBudget",      {type:"NUMM",   view:"",      size:8,  mode:"READONLY", desc:"金額" });
	vat.item.make(vnB_Detail, "lineId",           {type:"TEXT",   view:"none",  size:20, mode:"HIDDEN",   desc:"PoLineId" });	// view:"shift",
	vat.item.make(vnB_Detail, "status",           {type:"TEXT",   view:"shift", size:8,  mode:"READONLY", desc:"狀態" });		// view:"shift",
	vat.item.make(vnB_Detail, "lastUpdateDate",   {type:"TEXT",   view:"shift", size:20, mode:"READONLY", desc:"更新日期" });		// view:"shift",
	vat.item.make(vnB_Detail, "headId",           {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
									  id                  : "vatDetailDiv",
									  pageSize            : 10,
									  searchKey           : ["headId"],
									  pickAllService	  : function (){return selectAll()},
									  selectionType       : "CHECK",
									  indexType           : "AUTO",
								      canGridDelete       : vbCanGridDelete,
									  canGridAppend       : vbCanGridAppend,
									  canGridModify       : vbCanGridModify,	
									  loadBeforeAjxService: "loadBeforeAjxService()",
									  saveBeforeAjxService: "saveBeforeAjxService()",
									  saveSuccessAfter    : "",
									  blockId             : vnB_Detail
									 });
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}


function loadBeforeAjxService(){
	//alert("loadBeforeAjxService");
	var processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=getAJAXSearchPoLPageData" + 
                        "&brandCode="				+ document.forms[0]["#brandCode"].value +
                        "&currencyCode="			+ document.forms[0]["#currencyCode"].value +
                        "&supplierCode="			+ document.forms[0]["#supplierCode"].value +  
                        "&defaultWarehouseCode="	+ document.forms[0]["#defaultWarehouseCode"].value +    
                        "&sortType="				+ vat.item.getValueByName("#F.sortType") +   
                        "&isHideClose="				+ vat.item.getValueByName("#F.isHideClose") +   
                        "&orderTypeCode="			+ vat.item.getValueByName("#F.orderTypeCode") +    
	                    "&startInvoiceNo="			+ vat.item.getValueByName("#F.startInvoiceNo").replace(/^\s+|\s+$/, '') +     
	                    "&endInvoiceNo="			+ vat.item.getValueByName("#F.endInvoiceNo").replace(/^\s+|\s+$/, '') +
	                    "&status="					+ vat.item.getValueByName("#F.status") +     
	                    "&startDate="				+ vat.item.getValueByName("#F.startDate") +
					    "&endDate="					+ vat.item.getValueByName("#F.endDate");
	return processString;											
}



/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=saveSearchPoLResult";
	return processString;
}								


function selectAll(){
  //alert("selectAll");
  processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=updateAllSearcPoLhData";
  return processString;
}


function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                    funcSuccess: function() {
			                    	vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
			                    	}
			                    });
}


function doClosePicker(){
	vat.bean().vatBeanOther.headId   = document.forms[0]["#headId"].value;
	vat.bean().vatBeanOther.formName = document.forms[0]["#formName"].value;
	vat.bean().vatBeanPicker.exchangeRate  = document.forms[0]["#exchangeRate"].value;
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchPoLSelection";
    		}, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:2} );
	}}); 
}


function doView(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchPoLSelection";
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


function resetForm(){
    vat.item.setValueByName("#F.startOrderNo", "");
    vat.item.setValueByName("#F.endOrderNo", "");
    vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.startDate", "");
    vat.item.setValueByName("#F.endDate", "");
}
