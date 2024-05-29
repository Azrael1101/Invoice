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
  	 vat.bean().vatBeanOther = 
  	    {brandCode     : document.forms[0]["#brandCode"].value,   	
  	     employeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode : document.forms[0]["#orderTypeCode"].value,
  	     vatPickerId   : document.forms[0]["#vatPickerId"       ].value  
	    }; 

     vat.bean.init(function(){
		return "process_object_name=poPurchaseMainAction&process_object_method_name=performSearchInitial"; 
     },{other: true});

  }
}

function headerInitial(){ 
var allOrderTypes   = vat.bean("allOrderTypes");
var allItemCategory = vat.bean("allItemCategory");
var allStatus = [["","","true"],
                 ["暫存","作廢","簽核中","簽核完成","結案"],
                 ["SAVE","VOID","SIGNING","FINISH","CLOSE"]];
                 
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"採購單查詢作業", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#L.orderTypeCode",      type:"LABEL",  value:"單別"}]},	 
	 	{items:[{name:"#F.orderTypeCode",      type:"SELECT", bind:"orderTypeCode",  size:1, mode:"READONLY", init:allOrderTypes}]},		 
	 	{items:[{name:"#L.superintendentCode", type:"LABEL",  value:"採購單負責人"}]},
	 	{items:[{name:"#F.superintendentCode", type:"TEXT",   bind:"superintendentCode", size:12, mask:"CCCCCC", onchange:"onChangeSuperintendent()" },
	 			{name:"#B.superintendentCode", type:"PICKER", value:"查詢",  src:"./images/start_node_16.gif",
	 									 			openMode:"open", 
	 									 			service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			serviceAfterPick:function(){doAfterPickerEmp()}},
	 		 	{name:"#F.superintendentName", type:"TEXT",   bind:"superintendentName", size:12, mode:"READONLY" }]},
		{items:[{name:"#L.Date",               type:"LABEL",  value:"採購日期"}]},
	 	{items:[{name:"#F.startDate",          type:"DATE",   bind:"startDate",    size:1},
	         	{name:"#L.between",            type:"LABEL",  value:" 至 "},
	         	{name:"#F.endDate",            type:"DATE",   bind:"endDate",      size:1}]},
	    {items:[{name:"#L.status",             type:"LABEL",  value:"狀態"}]},	 		 
	 	{items:[{name:"#F.status",             type:"SELECT", bind:"status",    size:12, init:allStatus}]}]},

	 {row_style:"", cols:[
	 	{items:[{name:"#L.categoryType",  type:"LABEL",  value:"業種"}]},	 
	 	{items:[{name:"#F.categoryType",  type:"SELECT", bind:"categoryType", size:12, init:allItemCategory }]},
	 	{items:[{name:"#L.supplierCode",  type:"LABEL",  value:"廠商代號"}]},
	 	{items:[{name:"#F.supplierCode",  type:"TEXT",   bind:"supplierCode", size:16, mask:"CCCCCCCCCCCCCCCC", onchange:"onChangeSupplierCode()" },
				{name:"#B.supplierCode",  type:"PICKER", value:"PICKER", src:"./images/start_node_16.gif",
	 									  openMode:"open", 
	 									  service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									  left:0, right:0, width:1024, height:768,	
	 									  serviceAfterPick:function(){doAfterPickerSupplier();}},
	 		 	{name:"#F.supplierName",  type:"TEXT",   bind:"supplierName",       size:20, mode:"READONLY"}]},
	 	{items:[{name:"#L.orderNo",       type:"LABEL", value:"採購單號"}]},
	 	{items:[{name:"#F.startOrderNo",  type:"TEXT",  bind:"startOrderNo",    size:20},
				{name:"#L.between",       type:"LABEL", value:" 至 "},
	 		 	{name:"#F.endOrderNo",    type:"TEXT",  bind:"endOrderNo",      size:20}]},
	 	{items:[{name:"#L.startSourceOrderNo",  type:"LABEL", value:"來源單號"}]},
	 	{items:[{name:"#F.startSourceOrderNo",  type:"TEXT",  bind:"startSourceOrderNo", size:20},
				{name:"#L.between",       		type:"LABEL", value:" 至 "},
	 		 	{name:"#F.endSourceOrderNo",    type:"TEXT",  bind:"endSourceOrderNo", size:20}]}]},
     {row_style:"", cols:[
        {items:[{name:"#L.orderNo",       type:"LABEL", value:"品號"}]},
        {items:[{name:"#F.startItemCode",  type:"TEXT",  bind:"startItemCode",    size:20},
                {name:"#L.between",       type:"LABEL", value:" 至 "},
                {name:"#F.endItemCode",    type:"TEXT",  bind:"endItemCode",      size:20}]},     
        {items:[{name:"#L.orderNo",       type:"LABEL", value:"廠商報價單號"}]},
        {items:[{name:"#F.startQuotationCode",  type:"TEXT",  bind:"startQuotationCode",    size:20},
                {name:"#L.between",       type:"LABEL", value:" 至 "},
                {name:"#F.endQuotationCode",    type:"TEXT",  bind:"endQuotationCode",      size:20}]},
        {items:[{name:"#L.startSourceOrderNo",  type:"LABEL", value:"國外採購單號"}]},
        {items:[{name:"#F.startPoOrderNo",  type:"TEXT",  bind:"startPoOrderNo", size:20},
                {name:"#L.between",             type:"LABEL", value:" 至 "},
                {name:"#F.endPoOrderNo",    type:"TEXT",  bind:"endPoOrderNo", size:20}]}]}	 		 	
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
	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
	var vbSelectionType = "CHECK";    
	var vnB_Detail = 2;
    if(vatPickerId != null && vatPickerId != ""){
 		vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
		vbSelectionType = "CHECK";    
    }else{
		vat.item.setStyleByName("#B.view" , "display", "none");
		vbSelectionType = "NONE";
    }
    
    vat.item.make(vnB_Detail, "indexNo",            {type:"IDX",                                      desc:"序號" });
    vat.item.make(vnB_Detail, "orderNo",            {type:"TEXT",   size:20, mode:"READONLY", desc:"採購單號" });
    vat.item.make(vnB_Detail, "quotationCode",      {type:"TEXT",   size:20, mode:"READONLY", desc:"廠商報價單號" });
    vat.item.make(vnB_Detail, "poOrderNo",          {type:"TEXT",   size:20, mode:"READONLY", desc:"國外採購單號" });        	
	vat.item.make(vnB_Detail, "superintendentCode", {type:"TEXT",   size:20, mode:"READONLY", desc:"負責人員" });
	vat.item.make(vnB_Detail, "Status",             {type:"TEXT",   size:12, mode:"READONLY", desc:"狀態" });
	vat.item.make(vnB_Detail, "sourceOrderNo",  	{type:"TEXT" ,  size:20, maxLen:20, mode:"READONLY", desc:"來源單號" });
	vat.item.make(vnB_Detail, "lastUpdateDate",     {type:"TEXT",   size:12, mode:"READONLY", desc:"更新日期" });	
	vat.item.make(vnB_Detail, "headId",             {type:"ROWID"});
	vat.item.make(vnB_Detail, "aclose",     {type:"BUTTON",   size:12, value:"結案" ,desc:"結案" ,eClick: 'finishtoclose()'});
	vat.block.pageLayout(vnB_Detail, {
									id                  : "vatDetailDiv",
									pageSize            : 10,
									searchKey           : ["headId"],
									selectionType       : vbSelectionType,
									indexType           : "AUTO",
								  	canGridDelete       : vbCanGridDelete,
									canGridAppend       : vbCanGridAppend,
									canGridModify       : vbCanGridModify,	
									loadBeforeAjxService: "loadBeforeAjxService()",
									saveBeforeAjxService: "saveBeforeAjxService()",
									saveSuccessAfter    : "",
									blockId             : vnB_Detail,
									indicate            : 
										function(){
											if(vatPickerId != null && vatPickerId != ""){
												return false}else{ openModifyPage()}
										}
									});
}

function loadBeforeAjxService(){
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXSearchPageData" + 
                        "&brandCode="          	+ document.forms[0]["#brandCode"].value +     
	                    "&orderTypeCode="      	+ vat.item.getValueByName("#F.orderTypeCode") +     
	                    "&startOrderNo="       	+ vat.item.getValueByName("#F.startOrderNo") +     
	                    "&endOrderNo="         	+ vat.item.getValueByName("#F.endOrderNo") +                       
	                    "&status="             	+ vat.item.getValueByName("#F.status") +     
	                    "&superintendentCode=" 	+ vat.item.getValueByName("#F.superintendentCode") +
	                    "&supplierCode="       	+ vat.item.getValueByName("#F.supplierCode") +
	                    "&categoryType="       	+ vat.item.getValueByName("#F.categoryType") +
					    "&startDate="          	+ vat.item.getValueByName("#F.startDate") +
					    "&endDate="            	+ vat.item.getValueByName("#F.endDate") + 
					    "&startSourceOrderNo="  + vat.item.getValueByName("#F.startSourceOrderNo").replace(/^\s+|\s+$/, '') +     
	                    "&endSourceOrderNo="    + vat.item.getValueByName("#F.endSourceOrderNo").replace(/^\s+|\s+$/, '');
					  
	return processString;											
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=saveSearchResult";
	return processString;
}								


function selectAll(){
  //vatIsAllClick
  alert("selectAll");
  processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=updateAllSearchData";
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
			function(){ return "process_object_name=poPurchaseMainAction&process_object_method_name=performSearchSelection";
    		}, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
	}}); 
}


function doView(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=poPurchaseMainAction&process_object_method_name=performSearchSelection";
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
    vat.item.setValueByName("#F.superintendentCode", ""); 
    vat.item.setValueByName("#F.startDate", "");
    vat.item.setValueByName("#F.endDate", "");
}


/* 設定供應商資料 */
function onChangeSupplierCode() {
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplierCode" +
						"&supplierCode="  + vat.item.getValueByName("#F.supplierCode") + 
						"&organizationCode=TM"+
						"&brandCode="     + document.forms[0]["#brandCode"].value  + 
						"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value ;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
		}
	});
}


/* */
function onChangeSuperintendent() {
	var superintendentCode = vat.item.getValueByName("#F.superintendentCode").toUpperCase();
	vat.item.setValueByName("#F.superintendentCode", superintendentCode) ;
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySuperintendent" +
						"&superintendentCode="  + superintendentCode;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("superintendentName", vat.ajax.xmlHttp.responseText))
		}
	});
}


/* 供應商picker 回來執行 */
function doAfterPickerSupplier(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXFormDataBySupplier"+
							"&addressBookId=" + vat.bean().vatBeanPicker.result[0].addressBookId +
							"&organizationCode=TM"+
							"&brandCode="     + document.forms[0]["#brandCode"].value  + 
							"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.supplierCode",    vat.ajax.getValue("SupplierCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}


/* 採購負責人picker 回來執行 */
function doAfterPickerEmp(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataByEmp"+
							"&employeeCode="  + vat.bean().vatBeanPicker.result[0].employeeCode ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.superintendentCode", vat.bean().vatBeanPicker.result[0].employeeCode );
				vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("employeeName", vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}

function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(!(vFormId == "" || vFormId == "0")){
	    var url = "/erp/Po_PurchaseOrder:create:20091226.page?formId=" + vFormId+"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value; 
	     sc=window.open(url, '調撥單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	     sc.resizeTo((screen.availWidth),(screen.availHeight));
	     sc.moveTo(0,0);
	}
	
}
function finishtoclose(){
	//var processString = "";
	//var status = vat.item.getValueByName("dddd");
	var nItemLine 			 = vat.item.getGridLine();
	var status 				 = vat.item.getGridValueByName("Status"	, nItemLine);
	var formStatus 			 = status;
	var orderNO    			 = vat.item.getGridValueByName("orderNo"	, nItemLine);
	var assignmentId         = vat.item.getValueByName("#assignmentId");
	var loginEmployeeCode	 = document.forms[0]["#loginEmployeeCode"].value;

    
	if (status==="簽核完成") {
		if(confirm("確定要結案?")){
			status = "FINISH";
			formStatus = "CLOSE";
			vat.bean().vatBeanOther={
				beforeChangeStatus: status,
	        	formStatus	  : formStatus,
	        	brandCode     : document.forms[0]["#brandCode"].value,   	  
  	     		orderTypeCode : document.forms[0]["#orderTypeCode"].value,
  	     		orderNo		  : orderNO,
  	     		loginEmployeeCode  : loginEmployeeCode,
  	     		assignmentId  : assignmentId
  	     		
        	};
				vat.block.submit(function(){return "process_object_name=poPurchaseMainAction"+
            	"&process_object_method_name=performTransactionClose";}, {bind:true, link:true, other:true 
		});
		
		vat.item.setGridValueByName("Status",nItemLine,"結案");
	}

	
	}

}
