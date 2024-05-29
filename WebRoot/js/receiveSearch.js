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
  	    {brandCode     	: document.forms[0]["#brandCode"].value,   	
  	     employeeCode  	: document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode 	: document.forms[0]["#orderTypeCode"].value,
  	     userType 		: document.forms[0]["#userType"].value,
  	     vatPickerId    : document.forms[0]["#vatPickerId"       ].value
	    }; 

     vat.bean.init(function(){
		return "process_object_name=imReceiveMainAction&process_object_method_name=performSearchInitial"; 
     },{other: true});

  }
}

function headerInitial(){ 
	var allOrderTypes = vat.bean("allOrderTypes");
	var allItemCategory = vat.bean("allItemCategory");
	var allStatus = [["","","true"],
                 ["暫存","作廢","簽核中","簽核完成","結案"],
                 ["SAVE","VOID","SIGNING","FINISH","CLOSE"]];
	var allOtherStatus = [["","","true"],
		["暫存","簽核中","簽核完成"],
		["SAVE","SIGNING","FINISH"]];
  	var typeCode        = vat.bean("typeCode");
	var titleString = "進貨單查詢作業";
	if(typeCode=="RR"){			// 進貨退回
		titleString = "進貨退出單查詢作業";
	}                 
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:titleString, rows:[  
	{row_style:"", cols:[
	 	{items:[{name:"#L.orderTypeCode", type:"LABEL",  value:"單別"}]},	 
	 	{items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode",  size:1, mode:"READONLY", init:allOrderTypes}]},		 
	 	{items:[{name:"#L.Date",          type:"LABEL", value:"日期"}]},
	 	{items:[{name:"#F.startDate",     type:"DATE",  bind:"startDate", size:1},
	         	{name:"#L.between",       type:"LABEL", value:" 至 "},
	         	{name:"#F.endDate",       type:"DATE",  bind:"endDate",   size:1}]},
	    {items:[{name:"#L.status",        type:"LABEL",  value:"狀態"}]},	 		 
	 	{items:[{name:"#F.status",        type:"SELECT", bind:"status", init:allStatus}]}
	 	]},
	{row_style:"", cols:[
	 	{items:[{name:"#L.orderNo",       type:"LABEL", value:"單號"}]},
	 	{items:[{name:"#F.startOrderNo",  type:"TEXT",  bind:"startOrderNo",    size:20},
				{name:"#L.between",       type:"LABEL", value:" 至 "},
	 		 	{name:"#F.endOrderNo",    type:"TEXT",  bind:"endOrderNo",      size:20}]},
	    {items:[{name:"#L.startSourceOrderNo",  type:"LABEL", value:"來源單號"}]},
	 	{items:[{name:"#F.startSourceOrderNo",  type:"TEXT",  bind:"startSourceOrderNo", size:20},
				{name:"#L.between",       		type:"LABEL", value:" 至 "},
	 		 	{name:"#F.endSourceOrderNo",    type:"TEXT",  bind:"endSourceOrderNo", size:20}]},
	 	{items:[{name:"#L.DeclarationNo", type:"LABEL",  value:"報單單號"}]},
	 	{items:[{name:"#F.declarationNo", type:"TEXT",   bind:"declarationNo",  size:20}]}]},
	{row_style:"", cols:[
	 	{items:[{name:"#L.itemCategory",	type:"LABEL", value:"業種"}]},
	 	{items:[{name:"#F.itemCategory",	type:"SELECT", bind:"itemCategory", init:allItemCategory}]},
	 	{items:[{name:"#L.warehouseStatus", type:"LABEL",  value:"驗收狀態"}]},	 		 
	 	{items:[{name:"#F.warehouseStatus", type:"SELECT", bind:"warehouseStatus", init:allOtherStatus}]},
	 	{items:[{name:"#L.expenseStatus",   type:"LABEL",  value:"費用狀態"}]},	 		 
	 	{items:[{name:"#F.expenseStatus",   type:"SELECT", bind:"expenseStatus", init:allOtherStatus}]}
	 	]},
	{row_style:"", cols:[
	 	{items:[{name:"#L.supplierCode",	type:"LABEL",  value:"廠商代號<font color='red'>*</font>"}]},
		{items:[{name:"#F.supplierCode",	type:"TEXT",   bind:"supplierCode",     size:12, mask:"C", eChange:"onChangeSupplierCode('HEAD')" },
				{name:"#B.supplierCode",	type:"PICKER", value:"PICKER", src:"./images/start_node_16.gif",
	 									 	openMode:"open", 
	 									 	service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 	left:0, right:0, width:1024, height:768,	
	 									 	serviceAfterPick:function(X){doAfterPickerSupplier();}},
	 			{name:"#L.supplierName",	type:"LABEL",  value:""},
	 		 	{name:"#F.supplierName",	type:"TEXT",   bind:"supplierName",     size:30, mode:"READONLY" }]},
	 	{items:[{name:"#L.startDateDiff",	type:"LABEL", value:"驗收日-進倉日 天數"}]},	 
	 	{items:[{name:"#F.startDateDiff",	type:"NUM",   bind:"startDateDiff", size:10},
	 			{name:"#L.endDateDiff",		type:"LABEL", value:" 至 "},
	 			{name:"#F.endDateDiff",		type:"NUM",   bind:"endDateDiff", size:10}]},
	 	{items:[{name:"#L.itemCode",  type:"LABEL",  value:"品號"}]},	 
	 	{items:[{name:"#F.itemCode",  type:"TEXT", bind:"itemCode", size:20}]}
	 	]},
	{row_style:"", cols:[
	 	{items:[{name:"#L.IDate",		type:"LABEL", value:"進倉日期"}]},
	 	{items:[{name:"#F.startIDate",	type:"DATE",  bind:"startIDate", size:1},
	         	{name:"#L.betweenIDate",type:"LABEL", value:" 至 "},
	         	{name:"#F.endIDate",	type:"DATE",  bind:"endIDate",   size:1}]},
		{items:[{name:"#L.RDate",		type:"LABEL", value:"驗收日期"}]},
	 	{items:[{name:"#F.startRDate",	type:"DATE",  bind:"startRDate", size:1},
	         	{name:"#L.betweenRDate",type:"LABEL", value:" 至 "},
	         	{name:"#F.endRDate",	type:"DATE",  bind:"endRDate",   size:1}]}
	 	]}
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
	 	 		{name:"#B.export"	   , type:"IMG"      ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"matchExport"	   , type:"LABEL"    ,value:"　"},
	 			{name:"#B.picker",      type:"IMG",      value:"檢視", src:"./images/button_view.gif",  eClick:"doClosePicker()"}]}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
	var allOrderTypes = vat.bean("allOrderTypes");
	var allItemCategory = vat.bean("allItemCategory");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    
    var vnB_Detail = 2;
    var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
	var vbSelectionType = "CHECK";    
    if(vatPickerId != null && vatPickerId != ""){
		vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
		vbSelectionType = "CHECK";    
    }else{
		vat.item.setStyleByName("#B.view" , "display", "none");
		vbSelectionType = "NONE";
    }
    vat.item.make(vnB_Detail, "indexNo",        {type:"IDX"  ,                                        desc:"序號" });
	vat.item.make(vnB_Detail, "orderTypeCode",  {type:"SELECT", view:"fixed", size:20, maxLen:20, mode:"READONLY", desc:"單別", init:allOrderTypes });
	vat.item.make(vnB_Detail, "orderNo",        {type:"TEXT"  , view:"fixed", size:15, maxLen:20, mode:"READONLY", desc:"單號" });
	vat.item.make(vnB_Detail, "itemCategory",   {type:"SELECT", view:"", 	  size:20, maxLen:20, mode:"READONLY", desc:"業種", init:allItemCategory });
	vat.item.make(vnB_Detail, "supplierCode",   {type:"TEXT"  , view:"", 	  size:10, maxLen:20, mode:"READONLY", desc:"供應商代碼" });
	vat.item.make(vnB_Detail, "supplierName",   {type:"TEXT"  , view:"", 	  size:20, maxLen:30, mode:"READONLY", desc:"供應商名稱" });
	vat.item.make(vnB_Detail, "declarationNo",  {type:"TEXT"  , view:"", 	  size:15, maxLen:20, mode:"READONLY", desc:"報單單號" });
	vat.item.make(vnB_Detail, "warehouseInDate",{type:"DATE"  , view:"", 	  size:12, maxLen:12, mode:"READONLY", desc:"進倉日" });
	vat.item.make(vnB_Detail, "receiptDate",	{type:"DATE"  , view:"", 	  size:12, maxLen:12, mode:"READONLY", desc:"驗收日" });
	vat.item.make(vnB_Detail, "status",         {type:"TEXT"  , view:"", 	  size:6, maxLen:12, mode:"READONLY", desc:"狀態" });
	vat.item.make(vnB_Detail, "warehouseStatus",{type:"TEXT"  , view:"shift", size:6, maxLen:12, mode:"READONLY", desc:"驗收狀態" });
	vat.item.make(vnB_Detail, "expenseStatus",  {type:"TEXT"  , view:"shift", size:6, maxLen:12, mode:"READONLY", desc:"費用狀態" });
	vat.item.make(vnB_Detail, "sourceOrderNo",  {type:"TEXT"  , view:"shift", size:15, maxLen:20, mode:"READONLY", desc:"來源單號" });
	vat.item.make(vnB_Detail, "lastUpdateDate", {type:"TEXT"  , view:"shift", size:6, maxLen:12, mode:"READONLY", desc:"更新日期" });
	vat.item.make(vnB_Detail, "headId",         {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
									  id                  : "vatDetailDiv",
									  pageSize            : 10,
									  searchKey           : ["headId","orderNo"],
									  //pickAllService	  : "selectAll",
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
	var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXSearchPageData" + 
                        "&brandCode="     		+ document.forms[0]["#brandCode"].value +     
	                    "&orderTypeCode=" 		+ vat.item.getValueByName("#F.orderTypeCode") +
	                    "&startDate="     		+ vat.item.getValueByName("#F.startDate") +
					    "&endDate="       		+ vat.item.getValueByName("#F.endDate") +
					    "&status="        		+ vat.item.getValueByName("#F.status") + 
	                    "&startOrderNo="  		+ vat.item.getValueByName("#F.startOrderNo").replace(/^\s+|\s+$/, '') +
	                    "&endOrderNo="    		+ vat.item.getValueByName("#F.endOrderNo").replace(/^\s+|\s+$/, '') +
						"&startSourceOrderNo="  + vat.item.getValueByName("#F.startSourceOrderNo").replace(/^\s+|\s+$/, '') +
	                    "&endSourceOrderNo="    + vat.item.getValueByName("#F.endSourceOrderNo").replace(/^\s+|\s+$/, '') +
	                    "&declarationNo=" 		+ vat.item.getValueByName("#F.declarationNo").replace(/^\s+|\s+$/, '').toUpperCase() +
					    "&itemCategory="    	+ vat.item.getValueByName("#F.itemCategory").replace(/^\s+|\s+$/, '') +
					    "&warehouseStatus="    	+ vat.item.getValueByName("#F.warehouseStatus").replace(/^\s+|\s+$/, '') +
					    "&expenseStatus="    	+ vat.item.getValueByName("#F.expenseStatus").replace(/^\s+|\s+$/, '') +
					    "&supplierCode="    	+ vat.item.getValueByName("#F.supplierCode").replace(/^\s+|\s+$/, '') +
					    "&startDateDiff="       + vat.item.getValueByName("#F.startDateDiff") +
					    "&endDateDiff="       	+ vat.item.getValueByName("#F.endDateDiff") +
					    "&itemCode="    		+ vat.item.getValueByName("#F.itemCode").replace(/^\s+|\s+$/, '') +
					    "&startIDate=" 			+ vat.item.getValueByName("#F.startIDate") +
						"&endIDate=" 			+ vat.item.getValueByName("#F.endIDate")  +
			            "&startRDate=" 			+ vat.item.getValueByName("#F.startRDate") +
						"&endRDate=" 			+ vat.item.getValueByName("#F.endRDate");
	return processString;											
}



/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=saveSearchResult";
	return processString;
}								


function doAfterPickerSupplier(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXFormDataBySupplier"+
							"&addressBookId="  + vat.bean().vatBeanPicker.result[0].addressBookId +
							"&organizationCode=TM"+
							"&brandCode="      + document.forms[0]["#brandCode"].value + 
							"&orderDate="      + vat.item.getValueByName("#F.orderDate") ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
					vat.item.setValueByName("#F.supplierCode",    vat.ajax.getValue("SupplierCode",    vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}

/* 變更供應商設定相關資料 */
function onChangeSupplierCode(){
		var supplierCode = vat.item.getValueByName("#F.supplierCode").toUpperCase();
		vat.item.setValueByName("#F.supplierCode", supplierCode);
		var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXFormDataBySupplier"+
						"&supplierCode="  + supplierCode +
						"&organizationCode=TM"+
						"&brandCode="     + document.forms[0]["#brandCode"].value + 
						"&orderDate="     + vat.item.getValueByName("#F.orderDate") ;
	vat.ajax.startRequest(processString,  function() { 
						if (vat.ajax.handleState())
							vat.item.setValueByName("#F.supplierName", vat.ajax.getValue("SupplierName", vat.ajax.xmlHttp.responseText));
  					});
}

function selectAll(){
  //vatIsAllClick
  alert("selectAll");
  processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=updateAllSearchData";
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
			function(){ return "process_object_name=imReceiveMainAction&process_object_method_name=performSearchSelection";
    		}, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
	}}); 
}


function doView(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=imReceiveMainAction&process_object_method_name=performSearchSelection";
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
    vat.item.setValueByName("#F.startDate", "");
    vat.item.setValueByName("#F.endDate", "");
    vat.item.setValueByName("#F.startOrderNo", "");
    vat.item.setValueByName("#F.endOrderNo", "");
    vat.item.setValueByName("#F.startSourceOrderNo", "");
    vat.item.setValueByName("#F.endSourceOrderNo", "");
    vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.declarationNo", "");
    vat.item.setValueByName("#F.itemCategory", "");
    vat.item.setValueByName("#F.warehouseStatus", ""); 
    vat.item.setValueByName("#F.expenseStatus", "");
    vat.item.setValueByName("#F.supplierCode", "");
    vat.item.setValueByName("#F.startDateDiff", "");
    vat.item.setValueByName("#F.endDateDiff", "");
    vat.item.setValueByName("#F.itemCode", "");
    vat.item.setValueByName("#F.startIDate", "");
    vat.item.setValueByName("#F.endIDate", "");
    vat.item.setValueByName("#F.startRDate", "");
    vat.item.setValueByName("#F.endRDate", "");
}


/* 報單單號 picker 回來執行 */
function doAfterPickerDeclarationNo(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getAJAXFormDataByDeclarationHeadId"+
							"&headId="  + vat.bean().vatBeanPicker.result[0].headId ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.declarationNo", vat.ajax.getValue("DeclarationNo", vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}

function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(!(vFormId == "" || vFormId == "0")){
	    var url = "/erp/Im_Receive:create:20091209.page?formId=" + vFormId
	    		+"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value
	    		+"&userType=" + document.forms[0]["#userType"].value; 
	     sc=window.open(url, '進貨單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	     sc.resizeTo((screen.availWidth),(screen.availHeight));
	     sc.moveTo(0,0);
	}
}

//sql明細匯出excel
function doExport(){

    var url = "/erp/jsp/ExportFormView.jsp" + 
              "?exportBeanName=IM_RECEIVE_SEARCH_SQL" + 
              "&fileType=XLS" + 
              "&processObjectName=imReceiveHeadMainService" + 
              "&processObjectMethodName=getAJAXExportDataBySql" +
              "&type=search" + 
              "&brandCode" 				+ "=" + document.forms[0]["#brandCode"].value +     
	          "&orderTypeCode"     		+ "=" + vat.item.getValueByName("#F.orderTypeCode") +
              "&startDate"   			+ "=" + vat.item.getValueByName("#F.startDate") +
              "&endDate"   				+ "=" + vat.item.getValueByName("#F.endDate") +
              "&status"   				+ "=" + vat.item.getValueByName("#F.status") +	               
	          "&startOrderNo"   		+ "=" + vat.item.getValueByName("#F.startOrderNo") +
	          "&endOrderNo"   			+ "=" + vat.item.getValueByName("#F.endOrderNo") +
			  "&startSourceOrderNo"   	+ "=" + vat.item.getValueByName("#F.startSourceOrderNo") +
	          "&endSourceOrderNo"   	+ "=" + vat.item.getValueByName("#F.endSourceOrderNo") +  
              "&declarationNo"    		+ "=" + vat.item.getValueByName("#F.declarationNo") +     
			  "&itemCategory"        	+ "=" + vat.item.getValueByName("#F.itemCategory") +    
			  "&warehouseStatus"        + "=" + vat.item.getValueByName("#F.warehouseStatus") +  
              "&expenseStatus"   		+ "=" + vat.item.getValueByName("#F.expenseStatus") +
              "&supplierCode"   		+ "=" + vat.item.getValueByName("#F.supplierCode") +
              "&startDateDiff"   		+ "=" + vat.item.getValueByName("#F.startDateDiff") +
              "&endDateDiff"   			+ "=" + vat.item.getValueByName("#F.endDateDiff") +
              "&itemCode"   			+ "=" + vat.item.getValueByName("#F.itemCode") +
              "&startIDate"   			+ "=" + vat.item.getValueByName("#F.startIDate") +
              "&endIDate"   			+ "=" + vat.item.getValueByName("#F.endIDate")  +
              "&startRDate"   			+ "=" + vat.item.getValueByName("#F.startRDate") +
              "&endRDate"   			+ "=" + vat.item.getValueByName("#F.endRDate") ;
    var width = "200";
    var height = "30";  
    window.open(url, '儲位匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

}