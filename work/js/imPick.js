vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

var afterSavePageProcess = "";

function outlineBlock(){
	formInitial(); 
	formButton(); 
	formHeader();
	vat.tabm.createTab(0, "vatTabSpan", "L", "float");                                                                                                                                   
	vat.tabm.createButton(0, "xTab1", "明細資料", "vnB_Detail",   "images/tab_detail_data_dark.gif",   "images/tab_detail_data_light.gif"); 
	formDetail();
	doFormAccessControl();
}

// 初始化
function formInitial(){
	
	vat.bean().vatBeanOther = {
		loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
		loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,
		orderTypeCode      : document.forms[0]["#orderTypeCode"].value,	  
		formId             : document.forms[0]["#formId"].value,
		currentRecordNumber: 0,
		lastRecordNumber   : 0
	};  
	    
	vat.bean.init(	
		function(){
			return "process_object_name=imPickAction&process_object_method_name=performInitial"; 
 		},{ other: true
	});
}

/* function button */
function formButton(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;

    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new",			type:"IMG",     value:"新增",  src:"./images/button_create.gif",         eClick:"createNewForm()"},
	 			{name:"#B.search" ,		type:"PICKER",  value:"查詢",  src:"./images/button_find.gif", 
	 									  openMode:"open", 
										  servicePassData:function()
										  	{return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode")},
	 									  service:"Im_Pick:search:1.page",
	 									  left:0, right:0, width:1024, height:768,	
	 									  serviceAfterPick:function(){doAfterPickerProcess()}},
	 	 		{name:"#B.exit",		type:"IMG",		value:"離開",    src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.submit",		type:"IMG",		value:"送出",    src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.save",		type:"IMG",		value:"暫存",    src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"#B.void",		type:"IMG",		value:"作廢",    src:"./images/button_void.gif", eClick:'doSubmit("VOID")'},
	 			{name:"#B.message",		type:"IMG",		value:"訊息提示", src:"./images/button_message_prompt.gif",  eClick:'showMessage()'},
	 			{name:"#B.extend",		type:"IMG",		value:"展儲位",  src:"./images/button_storage_extend.gif", eClick:'doSubmit("EXTEND")'},
	 			{name:"#B.export",		type:"IMG",		value:"明細匯出",  src:"./images/button_detail_export.gif", eClick:'doSubmit("EXPORT")'},
	 			{name:"#B.import",		type:"PICKER" ,	value:"明細匯入",  src:"./images/button_detail_import.gif"  , 
										openMode:"open", 
	 									service:"/erp/fileUpload:standard:2.page",
										servicePassData:function(x){ return importFormData(); },
										left:0, right:0, width:600, height:400,	
										serviceAfterPick:function(){afterImportSuccess()}},
				{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif", eClick:'openReportWindow()'},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"     , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
		vat.bean().vatBeanPicker.imPickResult = null;
    	refreshForm("");
	 }
}

function refreshForm(vsHeadId){   
	document.forms[0]["#formId"].value        = vsHeadId;	
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;
	/*
	vat.bean().vatBeanOther = {
		loginBrandCode    	: document.forms[0]["#loginBrandCode"].value,   	
		loginEmployeeCode 	: document.forms[0]["#loginEmployeeCode"].value,
		orderTypeCode     	: document.forms[0]["#orderTypeCode"].value,	  
		formId            	: vsHeadId
	};*/
	vat.block.submit(
		function(){
			return "process_object_name=imPickAction&process_object_method_name=performInitial";  
     	},{other: true, 
     	   funcSuccess:function(){
				vat.item.bindAll();
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
				doFormAccessControl();
     	  }}
    );
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
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("EXPORT" == formAction){
	 	alertMessage = "是否確定匯出作業?";
	}else if ("EXTEND" == formAction){
	 	alertMessage = "是否確定展儲位?";
	}
		
	if(confirm(alertMessage)){
	    if("SAVE" == formAction){
	    	afterSavePageProcess = "SAVE";
        }else if("SUBMIT" == formAction){
        	afterSavePageProcess = "SUBMIT";
        }else if("VOID" == formAction){
        	afterSavePageProcess = "VOID";
        }else if("EXPORT" == formAction){
        	afterSavePageProcess = "EXPORT";
        }else if("EXTEND" == formAction){
        	afterSavePageProcess = "EXTEND";
        }
        vat.block.pageSearch(vnB_Detail);
	}
}

/*訊息提示*/
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=" +  vat.item.getValueByName("#F.programId")+
		"&levelType=ERROR" +
        "&identification=" +  vat.item.getValueByName("#F.identification"), 
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

//展儲位
function extendFormData(isClean){
	var headId = vat.item.getValueByName("#F.headId");
	vat.ajax.XHRequest({
		post:"process_object_name=appExtendItemStorageInfoService&process_object_method_name=executeExtendItemStorage"+
			"&tableType=IM_PICK" +
			"&processObjectName=imPickService" + 
			"&searchMethodName=findById" + 
			"&searchKey=" + headId +
			"&isClean=" + isClean +
			"&subEntityBeanName=imPickItems",
		find: function onExtendFormDataSuccess(oXHR){
				vat.ajax.XHRequest({
					post:"process_object_name=imPickService&process_object_method_name=updateResetItems"+
						"&headId=" + headId,
					find: function onExtendFormDataSuccess(oXHR){
						vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);
        			}
				});
        }
	});
}

function eventStorageService(){
	vat.block.pageDataSave(vnB_Detail,{  
		funcSuccess:function(){
			extendFormData('Y')
		}});
}


/*匯出*/
function exportFormData(){
	var headId =  + vat.item.getValueByName("#F.headId");
	var url = "/erp/jsp/ExportFormView.jsp" +
	     "?exportBeanName=IM_PICK_SQL" +
	     "&fileType=XLS" + 
	     "&processObjectName=imPickService" + 
	     "&processObjectMethodName=getAJAXExportDataBySql" +
	     "&headId=" + headId;
    var width = "1024";
    var height = "768";
    window.open(url, '挑貨單明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + 
    				 ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

/*匯入*/
function importFormData(){
	var width = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
	var suffix = "&importBeanName=IM_PICK" +
		"&importFileType=XLS" +
       	"&processObjectName=imPickService" + 
       	"&processObjectMethodName=executeImportLists" +
       	"&arguments=" + headId +
       	"&parameterTypes=LONG" +
        "&blockId=" + vnB_Detail;
    return suffix;
}

function afterImportSuccess(){
	onChangeWarehouseCode();
}

// 主檔
function formHeader(){
	var allOrderTypeCodes = vat.bean("allOrderTypeCodes");
	var allWarehouses = vat.bean("allWarehouses");
	vat.block.create( vnB_Header, { 
		id: "vnB_Header", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"挑貨單", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 	type:"LABEL", 	value:"單別<font color='red'>*</font>"}]},
				{items:[{name:"#F.orderTypeCode", 	type:"SELECT",	size:12, 	bind:"orderTypeCode", init:allOrderTypeCodes}]},
				{items:[{name:"#L.orderNo", 		type:"LABEL", 	value:"單號"}]},
				{items:[{name:"#F.orderNo", 		type:"TEXT",	size:20, 	bind:"orderNo", mode:"READONLY"},
						{name:"#F.headId", 			type:"TEXT",	size:8, 	bind:"headId", mode:"READONLY", back:false},
						{name:"#F.identification", 	type:"TEXT",	size:8, 	bind:"identification", mode:"HIDDEN"},
						{name:"#F.programId", 		type:"TEXT",	size:8, 	bind:"programId", mode:"HIDDEN"}]},
				{items:[{name:"#L.status", 			type:"LABEL", 	value:"狀態"}]},
				{items:[{name:"#F.status", 			type:"TEXT",	size:8, 	bind:"status", mode:"READONLY"},
						{name:"#F.statusName",		type:"TEXT",	size:8, 	bind:"statusName", mode:"READONLY"}]},
				{items:[{name:"#L.brandCode", 		type:"LABEL", 	value:"品牌"}]},
				{items:[{name:"#F.brandCode", 		type:"TEXT",	size:8, 	bind:"brandCode", mode:"READONLY"},
						{name:"#F.brandName",		type:"TEXT",	size:8, 	bind:"brandName", mode:"READONLY"}]},
				{items:[{name:"#L.createdBy", 		type:"LABEL", 	value:"填單人員"}]},
				{items:[{name:"#F.createdBy", 		type:"TEXT",	size:8, 	bind:"createdBy", mode:"READONLY"},
						{name:"#F.createdByName", 	type:"TEXT",	size:8, 	bind:"createdByName", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseCode", 	type:"LABEL", 	value:"出貨庫<font color='red'>*</font>"}]},
				{items:[{name:"#F.warehouseCode", 	type:"SELECT",	size:12, 	bind:"warehouseCode", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.pickDate", 		type:"LABEL", 	value:"挑貨日<font color='red'>*</font>"}]},
				{items:[{name:"#F.pickDate", 		type:"DATE",	size:12, 	bind:"pickDate"}]},
				{items:[{name:"#L.description", 	type:"LABEL", 	value:"敘述"}]},
				{items:[{name:"#F.description", 	type:"TEXT",	size:70, 	bind:"description"}], td:" colSpan=3"},
				{items:[{name:"#L.creationDate",		type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",		type:"TEXT", 	bind:"creationDate", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.arrivalWarehouseCode1", 	type:"LABEL", 	value:"收貨庫1"}]},
				{items:[{name:"#F.arrivalWarehouseCode1", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode1", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.arrivalWarehouseCode2", 	type:"LABEL", 	value:"收貨庫2"}]},
				{items:[{name:"#F.arrivalWarehouseCode2", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode2", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.arrivalWarehouseCode3", 	type:"LABEL", 	value:"收貨庫3"}]},
				{items:[{name:"#F.arrivalWarehouseCode3", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode3", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.arrivalWarehouseCode4", 	type:"LABEL", 	value:"收貨庫4"}]},
				{items:[{name:"#F.arrivalWarehouseCode4", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode4", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.arrivalWarehouseCode5", 	type:"LABEL", 	value:"收貨庫5"}]},
				{items:[{name:"#F.arrivalWarehouseCode5", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode5", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.arrivalWarehouseCode6", 	type:"LABEL", 	value:"收貨庫6"}]},
				{items:[{name:"#F.arrivalWarehouseCode6", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode6", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.arrivalWarehouseCode7", 	type:"LABEL", 	value:"收貨庫7"}]},
				{items:[{name:"#F.arrivalWarehouseCode7", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode7", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.arrivalWarehouseCode8", 	type:"LABEL", 	value:"收貨庫8"}]},
				{items:[{name:"#F.arrivalWarehouseCode8", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode8", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.arrivalWarehouseCode9", 	type:"LABEL", 	value:"收貨庫9"}]},
				{items:[{name:"#F.arrivalWarehouseCode9", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode9", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]},
				{items:[{name:"#L.arrivalWarehouseCode10", 	type:"LABEL", 	value:"收貨庫10"}]},
				{items:[{name:"#F.arrivalWarehouseCode10", 	type:"SELECT",	size:20, 	bind:"arrivalWarehouseCode10", init:allWarehouses, onchange:"onChangeWarehouseCode()"}]}
			]}
		], 	
		beginService:"",
		closeService:""			
	});
}

function formDetail() {
	vat.item.make(vnB_Detail, "indexNo"				, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_Detail, "itemCode"			, {type:"TEXT", size:16, desc:"品號", onchange:"onChangeItemCode()"});
	vat.item.make(vnB_Detail, "itemCName"			, {type:"TEXT", size:20, desc:"品名", mode:"READONLY" });
	vat.item.make(vnB_Detail, "blockQuantity"		, {type:"NUMM" , size:3, desc:"預扣量", mode:"READONLY" });
	vat.item.make(vnB_Detail, "commitQuantity"		, {type:"NUMM" , size:3, desc:"已出量", mode:"READONLY" });
	vat.item.make(vnB_Detail, "storageInNo"			, {type:"NUMB" , size:8, desc:"進貨日" });
	vat.item.make(vnB_Detail, "storageLotNo"		, {type:"NUMB" , size:8, desc:"效期" });
	vat.item.make(vnB_Detail, "warehouseCode"		, {type:"TEXT" , size:4, desc:"出庫", mode:"READONLY" });
	vat.item.make(vnB_Detail, "storageCode"			, {type:"NUMB" , size:4, desc:"出儲位" });
	vat.item.make(vnB_Detail, "searchStroage"	    , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif",
	 									 		    	service:"Im_Storage:onHand:20111011.page",
	 									 			    left:0, right:0, width:1024, height:768,
	 									 				servicePassData:function(x){ return doPassLineData(x); },
	 									 		        serviceAfterPick:function(xx){doLineAfterPickerProcess(xx); } });
	vat.item.make(vnB_Detail, "arrivalWarehouse1", {type:"NUMB" , size:3, desc:"收庫1", onchange:"onSumQuantity()"});
	vat.item.make(vnB_Detail, "arrivalWarehouse2", {type:"NUMB" , size:3, desc:"收庫2", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "arrivalWarehouse3", {type:"NUMB" , size:3, desc:"收庫3", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "arrivalWarehouse4", {type:"NUMB" , size:3, desc:"收庫4", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "arrivalWarehouse5", {type:"NUMB" , size:3, desc:"收庫5", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "arrivalWarehouse6", {type:"NUMB" , size:3, desc:"收庫6", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "arrivalWarehouse7", {type:"NUMB" , size:3, desc:"收庫7", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "arrivalWarehouse8", {type:"NUMB" , size:3, desc:"收庫8", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "arrivalWarehouse9", {type:"NUMB" , size:3, desc:"收庫9", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "arrivalWarehouse10",{type:"NUMB" , size:3, desc:"收庫10", onchange:"onSumQuantity()" });
	vat.item.make(vnB_Detail, "lineId"          	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "isLockRecord"    	, {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_Detail, "isDeleteRecord"  	, {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_Detail, "message"         	, {type:"MSG"  , desc:"訊息"});

	vat.block.pageLayout( vnB_Detail, {
		id: "vnB_Detail",
		pageSize: 10,											
        canGridDelete : true,
		canGridAppend : true,
		canGridModify : true,						
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService()",
		loadSuccessAfter    : "loadSuccessAfter()",						
		eventService        : "eventService()",   
		saveBeforeAjxService: "saveBeforeAjxService()",
		saveSuccessAfter    : "saveSuccessAfter()"
	});
	//vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);	
}

// 新增空白頁
function appendBeforeService(){
	return true;
}    

// 新增空白頁成功後
function appendAfterService(){

}

// 第一次載入 重新整理
function loadBeforeAjxService(){
	var processString = "process_object_name=imPickService&process_object_method_name=getAJAXPageLineData" +
						"&headId=" + vat.item.getValueByName("#F.headId") +
						"&brandCode=" + vat.item.getValueByName("#F.brandCode") +  
						"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode"].value;
	return processString;	
}

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){

} 

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(){
	var processString = "process_object_name=imPickService&process_object_method_name=updateAJAXPageLineData" +
						"&headId=" + vat.item.getValueByName("#F.headId") + 
						"&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode") + 
						"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode"].value;
	return processString;	
}

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(){
		if( "SAVE" == afterSavePageProcess ) {
			execSubmit("SAVE");
		}else if( "SUBMIT" == afterSavePageProcess ){
			execSubmit("SUBMIT");
		}else if( "VOID" == afterSavePageProcess ){
			execSubmit("VOID");
		}else if ("EXPORT" == afterSavePageProcess) {
			exportFormData();
		}else if ("EXTEND" == afterSavePageProcess) {
			extendFormData('N');
		}
	afterSavePageProcess = "" ;	
}

// 自定事件
function eventService(){

}

/* 明細都存檔完成後, 實際執行主檔存檔作業 */
function execSubmit(actionId){
	var beforeChangeStatus = vat.item.getValueByName("#F.status");
	var formStatus = beforeChangeStatus;
	if("SAVE" == actionId){
		formStatus = "SAVE";
	}else if("SUBMIT" == actionId){
		if("SAVE" == beforeChangeStatus)
			formStatus = "SIGNING";
		else if("SIGNING" == beforeChangeStatus)
			formStatus = "FINISH";
	}else if("VOID" == actionId){
		formStatus = "VOID";
	}
	
	var approvalResult  = "true";
	
	vat.bean().vatBeanOther = {
		loginBrandCode    	: document.forms[0]["#loginBrandCode"].value,   	
		loginEmployeeCode 	: document.forms[0]["#loginEmployeeCode"].value,
		orderTypeCode     	: document.forms[0]["#orderTypeCode"].value,	  
		formId            	: vat.item.getValueByName("#F.headId"),
		beforeChangeStatus	: beforeChangeStatus,
		formStatus 			: formStatus,
		identification		: vat.item.getValueByName("#F.identification"),
		processId			: document.forms[0]["#processId"].value,
		assignmentId		: document.forms[0]["#assignmentId"].value,
		approvalResult		: approvalResult
	};
	
	vat.block.submit(function(){return "process_object_name=imPickAction"+
           	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
}

function onChangeWarehouseCode(){
	var headId = vat.item.getValueByName("#F.headId");
	var warehouseCode = vat.item.getValueByName("#F.warehouseCode");
	var arrivalWarehouseCode1 = vat.item.getValueByName("#F.arrivalWarehouseCode1");
	var arrivalWarehouseCode2 = vat.item.getValueByName("#F.arrivalWarehouseCode2");
	var arrivalWarehouseCode3 = vat.item.getValueByName("#F.arrivalWarehouseCode3");
	var arrivalWarehouseCode4 = vat.item.getValueByName("#F.arrivalWarehouseCode4");
	var arrivalWarehouseCode5 = vat.item.getValueByName("#F.arrivalWarehouseCode5");
	var arrivalWarehouseCode6 = vat.item.getValueByName("#F.arrivalWarehouseCode6");
	var arrivalWarehouseCode7 = vat.item.getValueByName("#F.arrivalWarehouseCode7");
	var arrivalWarehouseCode8 = vat.item.getValueByName("#F.arrivalWarehouseCode8");
	var arrivalWarehouseCode9 = vat.item.getValueByName("#F.arrivalWarehouseCode9");
	var arrivalWarehouseCode10 = vat.item.getValueByName("#F.arrivalWarehouseCode10");
	
	vat.block.pageDataSave(vnB_Detail, {  
 		funcSuccess:function(){
			vat.ajax.XHRequest({
				post:"process_object_name=imPickService&process_object_method_name=updateChangeWarehouseCode"+
						"&headId=" + headId + 
						"&warehouseCode=" + warehouseCode + 
						"&arrivalWarehouseCode1=" + arrivalWarehouseCode1 + 
						"&arrivalWarehouseCode2=" + arrivalWarehouseCode2 + 
						"&arrivalWarehouseCode3=" + arrivalWarehouseCode3 + 
						"&arrivalWarehouseCode4=" + arrivalWarehouseCode4 + 
						"&arrivalWarehouseCode5=" + arrivalWarehouseCode5 + 
						"&arrivalWarehouseCode6=" + arrivalWarehouseCode6 + 
						"&arrivalWarehouseCode7=" + arrivalWarehouseCode7 + 
						"&arrivalWarehouseCode8=" + arrivalWarehouseCode8 + 
						"&arrivalWarehouseCode9=" + arrivalWarehouseCode9 + 
						"&arrivalWarehouseCode10=" + arrivalWarehouseCode10,
				find: function onChangeItemCodeSuccess(oXHR){ 
					vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);
		        }   
			});
		}
	});
}

function onChangeItemCode() {
	var nItemLine = vat.item.getGridLine();
	var sItemCode = vat.item.getGridValueByName("itemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setGridValueByName("itemCode", nItemLine, sItemCode);
	//vat.item.setGridValueByName("storageInNo", nItemLine, "00000000");
	//vat.item.setGridValueByName("storageLotNo", nItemLine, "00000000");
	//vat.item.setGridValueByName("storageCode", nItemLine, "00000000");
	var vBrandCode = document.forms[0]["#loginBrandCode"].value;
	if (sItemCode != "") {
		vat.ajax.XHRequest({
			post:"process_object_name=imItemService&process_object_method_name=getAJAXItemName"+
					"&brandCode=" + vBrandCode + 
					"&itemCode=" + sItemCode,
			find: function onChangeItemCodeSuccess(oXHR){ 
				if("" != vat.ajax.getValue("itemName", oXHR.responseText))
					vat.item.setGridValueByName("itemCName", nItemLine, vat.ajax.getValue("itemName", oXHR.responseText));
				else
					vat.item.setGridValueByName("itemCName", nItemLine, "查無此商品");
			}   
		});
	}
}


function onSumQuantity(){
	var nItemLine = vat.item.getGridLine();
	var arrivalWarehouse1 = Number(vat.item.getGridValueByName("arrivalWarehouse1",nItemLine));
	var arrivalWarehouse2 = Number(vat.item.getGridValueByName("arrivalWarehouse2",nItemLine));
	var arrivalWarehouse3 = Number(vat.item.getGridValueByName("arrivalWarehouse3",nItemLine));
	var arrivalWarehouse4 = Number(vat.item.getGridValueByName("arrivalWarehouse4",nItemLine));
	var arrivalWarehouse5 = Number(vat.item.getGridValueByName("arrivalWarehouse5",nItemLine));
	var arrivalWarehouse6 = Number(vat.item.getGridValueByName("arrivalWarehouse6",nItemLine));
	var arrivalWarehouse7 = Number(vat.item.getGridValueByName("arrivalWarehouse7",nItemLine));
	var arrivalWarehouse8 = Number(vat.item.getGridValueByName("arrivalWarehouse8",nItemLine));
	var arrivalWarehouse9 = Number(vat.item.getGridValueByName("arrivalWarehouse9",nItemLine));
	var arrivalWarehouse10= Number(vat.item.getGridValueByName("arrivalWarehouse10",nItemLine));
	
	var blockQuantity = arrivalWarehouse1 + arrivalWarehouse2 + arrivalWarehouse3 + arrivalWarehouse4 + 
						arrivalWarehouse5 + arrivalWarehouse6 + arrivalWarehouse7 +
						arrivalWarehouse8 + arrivalWarehouse9 + arrivalWarehouse10;
	vat.item.setGridValueByName("blockQuantity",nItemLine,blockQuantity);
	vat.item.setGridValueByName("commitQuantity",nItemLine,"0");

}

//按下明細Picker
function doPassLineData(x){
	vat.block.pageDataSave( vnB_Detail, {refresh:false});

	var suffix = "";
	var vLineId			= vat.item.getGridLine(x);
	var vItemCode       = vat.item.getGridValueByName("itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
	//var vWarehouseCode    = vat.item.getGridValueByName("warehouseCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
	var vWarehouseCode	= vat.item.getValueByName("#F.warehouseCode").toUpperCase();

	//alert("LineId:"+vLineId);
	suffix += "&itemCode="+escape(vItemCode)+
				"&warehouseCode="+escape(vWarehouseCode);
	//alert("suffix = " + suffix);
	return suffix;
}


//明細Picker回傳
function doLineAfterPickerProcess(xx){
	//alert('doLineAfterPickerProcess');
	var vLineId	      = vat.item.getGridLine(xx);
	//alert('vLineId = ' + vLineId);
	
	if(vat.bean().vatBeanPicker.imOnHandResult != null){
		vat.item.setGridValueByName("itemCode", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].id_itemCode);
		vat.item.setGridValueByName("storageLotNo", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].id_storageLotNo);
		vat.item.setGridValueByName("storageInNo", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].id_storageInNo);
		vat.item.setGridValueByName("storageCode", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].id_storageCode);
		vat.item.setGridValueByName("itemCName", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].itemCName);
	}
}

function doFormAccessControl(){
	var status 		= vat.item.getValueByName("#F.status");
	var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	var processId = document.forms[0]["#processId"].value;

	// 初始化
	//====================================================================
	vat.item.setAttributeByName("vnB_Header", "readOnly", false, true, true);
	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true); 
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true); 
	//===========================line========================================

	vat.block.canGridModify([vnB_Detail], true,true,true);
	//===========================buttonLine==============================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	vat.item.setStyleByName("#B.import", 	"display", "inline");
	vat.item.setStyleByName("#B.export", 	"display", "inline");
	vat.item.setStyleByName("#B.extend", 	"display", "inline");

	if(null != vat.bean().vatBeanPicker.imPickResult || "SAVE" != status){
		vat.item.setAttributeByName("vnB_Header", "readOnly", true, true, true);
		vat.block.canGridModify([vnB_Detail], false,false,false);
		vat.item.setStyleByName("#B.submit", "display", "none");
		vat.item.setStyleByName("#B.save", 	"display", "none");
		vat.item.setStyleByName("#B.import", "display", "none");
		vat.item.setStyleByName("#B.extend", 	"display", "none");
	}
	
	if("" != processId){
		
		if("SAVE" == status){
			vat.item.setStyleByName("#B.save",	"display", "inline");
			vat.item.setStyleByName("#B.void",	"display", "inline");
		}
		
		if("SIGNING" == status){
			vat.item.setStyleByName("#B.submit", 	"display", "inline");
			vat.item.setStyleByName("#B.import", 	"display", "none");
			vat.item.setStyleByName("#B.extend", 	"display", "none");
		}
		
		vat.item.setStyleByName("#B.new", 		"display", "none");
		vat.item.setStyleByName("#B.search", 	"display", "none");
		
	}
	vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);	
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.imPickResult !== null){
	    var vsMaxSize = vat.bean().vatBeanPicker.imPickResult.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var headId = vat.bean().vatBeanPicker.imPickResult[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(headId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    vsfunctionCode = vat.bean().vatBeanPicker.imPickResult[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsfunctionCode);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 上一筆
function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	vsfunctionCode = vat.bean().vatBeanPicker.imPickResult[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsfunctionCode);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 下一筆
function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	vsfunctionCode = vat.bean().vatBeanPicker.imPickResult[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsfunctionCode);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 最後一筆
function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    vsfunctionCode = vat.bean().vatBeanPicker.imPickResult[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsfunctionCode);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function openReportWindow(type){ 
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(
		function(){return "process_object_name=imPickService"+
						"&process_object_method_name=getReportConfig";},{other:true,
						funcSuccess:function(){
						eval(vat.bean().vatBeanOther.reportUrl);
					}}
		);   
}