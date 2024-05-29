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
			headId        : document.forms[0]["#headId"].value
	    	}; 

		vat.bean.init(function(){
			return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchPoHInitial"; 
		},{other: true});
 	}
}

function headerInitial(){ 
var allOrderTypes = vat.bean("allOrderTypes");
var isHide    = [[true, true], ["隱藏", "不隱藏"], ["Y", "N"]];
var allStatus = [["","","true"],
                 ["暫存","作廢","簽核中","簽核完成","結案"],
                 ["SAVE","VOID","SIGNING","FINISH","CLOSE"]];
          
                 
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"Purchase Order 查詢作業", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#L.PurchaseOrderNo.", type:"LABEL", value:"Purchase Order No."}]},	 
	 	{items:[{name:"#F.startOrderNo",     type:"TEXT",  bind:"startOrderNo", size:20},
				{name:"#L.between",          type:"LABEL", value:" 至 "},
	 		 	{name:"#F.endOrderNo",       type:"TEXT",  bind:"endOrderNo",   size:20}]},	
	 	{items:[{name:"#L.OrderTypeCode",    type:"LABEL", value:"Purchase Order Type"}]},	 		 
	 	{items:[{name:"#F.orderTypeCode",    type:"SELECT", bind:"orderTypeCode", size:12, init:allOrderTypes}]}]},

	 {row_style:"", cols:[
	 	{items:[{name:"#L.Purchase Date",  type:"LABEL",  value:"Purchase Date" }]},
	 	{items:[{name:"#F.startDate",      type:"DATE",   bind:"startDate", size:1 },
	         	{name:"#L.between",        type:"LABEL",  value:" 至 "},
	         	{name:"#F.endDate",        type:"DATE",   bind:"endDate",   size:1 }]},
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
    vat.item.make(vnB_Detail, "checkbox",       {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo",        {type:"IDX"  ,                                        desc:"序號" });
	vat.item.make(vnB_Detail, "orderTypeCode",  {type:"TEXT" ,   size:20, maxLen:20, mode:"READONLY", desc:"Order Type", init:allOrderTypes });
	vat.item.make(vnB_Detail, "orderNo",        {type:"TEXT" ,   size:20, maxLen:20, mode:"READONLY", desc:"Purchase Order No" });
	vat.item.make(vnB_Detail, "supplierCode",   {type:"TEXT" ,   size:20, maxLen:20, mode:"READONLY", desc:"Supplier No" });
	vat.item.make(vnB_Detail, "supplierName",   {type:"TEXT" ,   size:20, maxLen:20, mode:"READONLY", desc:"Supplier Name" });
	vat.item.make(vnB_Detail, "status",         {type:"TEXT" ,   size:12, maxLen:12, mode:"READONLY", desc:"狀態" });
	vat.item.make(vnB_Detail, "lastUpdateDate", {type:"TEXT" ,   size:12, maxLen:12, mode:"READONLY", desc:"更新日期" });
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
	var processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=getAJAXSearchPoHPageData" + 
                        "&brandCode="      + document.forms[0]["#brandCode"].value +     
                        "&supplierCode="   + document.forms[0]["#supplierCode"].value +    
	                    "&orderTypeCode="  + vat.item.getValueByName("#F.orderTypeCode") +  
 	                    "&startInvoiceNo=" + vat.item.getValueByName("#F.startInvoiceNo") +     
	                    "&endInvoiceNo="   + vat.item.getValueByName("#F.endInvoiceNo") +                       
	                  	"&isHideClose="    + vat.item.getValueByName("#F.isHideClose") +  
	                    "&startDate="      + vat.item.getValueByName("#F.startDate") +
					    "&endDate="        + vat.item.getValueByName("#F.endDate");
	return processString;											
}



/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=saveSearchPoHResult";
	return processString;
}								


function selectAll(){
  //vatIsAllClick
  //alert("selectAll");
  processString = "process_object_name=fiInvoiceHeadMainService&process_object_method_name=updateAllSearchPoHData";
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
	vat.bean().vatBeanOther.headId = document.forms[0]["#headId"].value;
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchPoHSelection";
    		}, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:2} );
	}}); 
}


function doView(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=fiInvoiceMainAction&process_object_method_name=performSearchPoHSelection";
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
