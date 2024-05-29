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
			orderTypeCode : document.forms[0]["#orderTypeCode"].value
	    	}; 
		vat.bean.init(function(){
			return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchInitial"; 
		},{other: true});
 	}
}

function headerInitial(){ 
var allOrderTypes = vat.bean("allOrderTypes");
var allStatus = [["","","true"],
                 ["暫存","作廢","簽核中","簽核完成","結案"],
                 ["SAVE","VOID","SIGNING","FINISH","CLOSE"]];
                 
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"Invoice 查詢作業", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#L.Invoice No.",    type:"LABEL",  value:"Invoice No."}]},	 
	 	{items:[{name:"#F.startInvoiceNo", type:"TEXT",   bind:"startInvoiceNo", size:20},
				{name:"#L.between",        type:"LABEL",  value:" 至 "},
	 		 	{name:"#F.endInvoiceNo",   type:"TEXT",   bind:"endInvoiceNo",   size:20}]},	
	 	{items:[{name:"#L.supplierCode",   type:"LABEL",  value:"Supplier No."}]},
	    {items:[{name:"#F.supplierCode",   type:"TEXT",   bind:"supplierCode",   size:12, onchange:"onChangeSupplierCode()", mask:"CCCCCCCCCCCCCCCCCCCC" },
	 			{name:"#B.supplierCode",   type:"PICKER", value:"PICKER", src:"./images/start_node_16.gif",
	 									   openMode:"open", 
	 									   service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									   left:0, right:0, width:1024, height:768,	
	 									   serviceAfterPick:function(){doAfterPickerSupplier();} },
	 			{name:"#F.supplierName",   type:"TEXT",   bind:"supplierName",   size:20, mode:"READONLY", back:false }], td:" colSpan=3"}]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.Invoice Date",   type:"LABEL",  value:"Invoice Date"}]},
	 	{items:[{name:"#F.startDate",      type:"DATE",   bind:"startDate",      size:1},
	         	{name:"#L.between",        type:"LABEL",  value:" 至 "},
	         	{name:"#F.endDate",        type:"DATE",   bind:"endDate",        size:1}]},

	 	{items:[{name:"#L.status",         type:"LABEL",  value:"狀態"}]},	 		 
	 	{items:[{name:"#F.status",         type:"SELECT", bind:"status",         size:12, init:allStatus}]},
	 	{items:[{name:"#L.orderTypeCode",  type:"LABEL",  value:"單別"}]},	 		 
	 	{items:[{name:"#F.orderTypeCode",  type:"SELECT", bind:"orderTypeCode",  size:20, init:allOrderTypes}]}]}
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
	 			//{name:"#B.update",      type:"PICKER", value:"檢視", src:"./images/button_view.gif",  eClick:"doView()"},
	 			//{name:"SPACE",          type:"LABEL",  value:"　"},	 		
	 			{name:"#B.picker",      type:"IMG",      value:"檢視", src:"./images/button_view.gif",  eClick:"doClosePicker()"}]}]}
	 			/*
	 			{name:"SPACE",          type:"LABEL",    value:"　"},	 		
	 			{name:"#F.selectedAll", type:"CHECKBOX", value:"N"},
	 			{name:"#L.selectedAll", type:"LABEL",    value:"選擇全部"},
	 			{name:"SPACE",          type:"LABEL",    value:"　"},	 		
	 			{name:"#F.clearAll",    type:"CHECKBOX", value:"N"},
	 			{name:"#L.clearAll",    type:"LABEL",    value:"清除全部"}]}]}
	 			*/
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
    vat.item.make(vnB_Detail, "checkbox",       {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo",        {type:"IDX",                                         desc:"序號" });
    vat.item.make(vnB_Detail, "orderTypeCode",  {type:"SELECT", size:20, maxLen:20, mode:"READONLY", desc:"單別", init:allOrderTypes});
	vat.item.make(vnB_Detail, "invoiceNo",      {type:"TEXT",   size:20, maxLen:20, mode:"READONLY", desc:"Invoice No"});
	vat.item.make(vnB_Detail, "invoiceDate",    {type:"TEXT",   size:20, maxLen:20, mode:"READONLY", desc:"Invoice Date" });
	vat.item.make(vnB_Detail, "supplierCode",   {type:"TEXT",   size:20, maxLen:20, mode:"READONLY", desc:"Supplier No" });
	vat.item.make(vnB_Detail, "supplierName",   {type:"TEXT",   size:20, maxLen:20, mode:"READONLY", desc:"Supplier Name" });
	vat.item.make(vnB_Detail, "status",         {type:"TEXT",   size:12, maxLen:12, mode:"READONLY", desc:"狀態" });
	vat.item.make(vnB_Detail, "lastUpdateDate", {type:"TEXT",   size:12, maxLen:12, mode:"READONLY", desc:"更新日期" });
	//vat.item.make(vnB_Detail, "headId",       {type:"TEXT", size:12, maxLen:12, mode:"READONLY",   desc:"流水號(PK)" });
	vat.item.make(vnB_Detail, "headId",         {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
									  id                  : "vatDetailDiv",
									  pageSize            : 10,
									  searchKey           : ["headId"],
									  pickAllService	  : "selectAll",
									  selectionType       : "CHECK",
									  indexType           : "AUTO",
								      canGridDelete       : vbCanGridDelete,
									  canGridAppend       : vbCanGridAppend,
									  canGridModify       : vbCanGridModify,	
									  loadBeforeAjxService: "loadBeforeAjxService()",
									  saveBeforeAjxService: "saveBeforeAjxService()",
									  saveSuccessAfter    : ""
									 });
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}


function loadBeforeAjxService(){
	var processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=getAJAXSearchPageData" + 
                        "&brandCode="      + document.forms[0]["#brandCode"].value +     
	                    "&orderTypeCode="  + vat.item.getValueByName("#F.orderTypeCode") +     
	                    "&startInvoiceNo=" + vat.item.getValueByName("#F.startInvoiceNo").replace(/^\s+|\s+$/, '') +     
	                    "&endInvoiceNo="   + vat.item.getValueByName("#F.endInvoiceNo").replace(/^\s+|\s+$/, '') +
	                    "&supplierCode="   + vat.item.getValueByName("#F.supplierCode") +                   
	                    "&status="         + vat.item.getValueByName("#F.status") +     
	                    "&startDate="      + vat.item.getValueByName("#F.startDate") +
					    "&endDate="        + vat.item.getValueByName("#F.endDate");
	return processString;											
}



/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=saveSearchResult";
	return processString;
}								


function selectAll(){
  //vatIsAllClick
  alert("selectAll");
  processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=updateAllSearchData";
  return processString;
  
}


function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);}
			                    });
}


function doClosePicker(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchSelection";
    		}, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
	}}); 
}


function doView(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchSelection";
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
    vat.item.setValueByName("#F.startInvoiceNo", "");
    vat.item.setValueByName("#F.endInvoiceNo", "");
    vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.startDate", "");
    vat.item.setValueByName("#F.endDate", "");
}

/* 供應商picker 回來執行 */
function doAfterPickerSupplier(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXFormDataBySupplier"+
							"&addressBookId=" + vat.bean().vatBeanPicker.result[0].addressBookId +
							"&organizationCode=TM"+
							"&brandCode=" + document.forms[0]["#brandCode"].value;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.supplierCode", vat.ajax.getValue("SupplierCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.supplierName", vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}

/* 設定供應商名稱資料 */
function onChangeSupplierCode() {
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplierCode" +
						"&supplierCode="  + vat.item.getValueByName("#F.supplierCode") + 
						"&organizationCode=TM"+
						"&brandCode="     + document.forms[0]["#brandCode"].value  + 
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") ;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
		}
	});
}