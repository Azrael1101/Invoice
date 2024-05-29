/*** 
 *	檔案:imStorage.js
 *	說明：儲位單
 *  <pre>
 *  	Created by david
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var vnB_Button = 0;
var vatStorageHeader = 201;
var vatStorageDetail = 202;

function outlineBlock(){
 	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                            
		vat.tabm.createTab(0, "vatTabSpan", "H", "float"); 
		vat.tabm.createButton(0 ,"xTab1","儲位單明細檔" ,"vatStorageDetailDiv" ,"images/tab_storage_detail_dark.gif" ,"images/tab_storage_detail_light.gif", false);
  	}  
	kweStorageBlock(vatStorageDetail);
	doFormAccessControl();
}

// 初始化
function formInitial(){  
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
			loginBrandCode		: document.forms[0]["#loginBrandCode"].value,
			loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode"].value,
			formId             	: document.forms[0]["#formId"].value,
			sourceOrderTypeCode : document.forms[0]["#sourceOrderTypeCode"].value,
            sourceOrderNo       : document.forms[0]["#sourceOrderNo"].value,
			orderTypeCode       : document.forms[0]["#orderTypeCode"].value,	
			processId          	: document.forms[0]["#processId"].value, 
			assignmentId       	: document.forms[0]["#assignmentId"].value,
			currentRecordNumber : 0,
			lastRecordNumber    : 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imStorageAction&process_object_method_name=performInitial"; 
	    	},{
	    		other: true
    	});
  	}
}

function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_Storage:search:20110331.page",    // ?orderTypeCode="+vat.bean().vatBeanOther.orderTypeCode
	 									 left:0, right:0, width:1024, height:768,
	 									 servicePassData:function(){ return doPassData("buttonLine");},	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"#B.void"		   , type:"IMG"	   ,value:"作廢",    src:"./images/button_void.gif", eClick:'doSubmit("VOID")'},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"#B.storageExport",		type:"IMG"    ,value:"明細匯出",   src:"./images/button_storage_export.gif" , eClick:'exportStorageFormData()'},
	 			{name:"#B.storageImport",		type:"PICKER" , value:"明細匯入",  src:"./images/button_storage_import.gif"  , 
						 openMode:"open", 
						 service:"/erp/fileUpload:standard:2.page",
						 servicePassData:function(x){ return importStorageFormData(); },
						 left:0, right:0, width:600, height:400,	
						 serviceAfterPick:function(){afterImportStorageSuccess();}},
				{name:"#B.extend",		type:"IMG",		value:"展儲位",  src:"./images/button_storage_extend.gif", eClick:"eventStorageService('N')"},
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

// 儲位單主檔
function headerInitial(){ 
	var allOrderTypeCodes = vat.bean("allOrderTypeCodes");
	var allWarehouses = vat.bean("allWarehouses");

	vat.block.create( vatStorageHeader, {
		id: "vatStorageHeader", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"儲位單功能維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 		type:"LABEL", 	value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 		type:"SELECT", 	bind:"orderTypeCode", init:allOrderTypeCodes, size:15, mode:"READONLY"}]},
				{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"儲位單號" }]},
				{items:[{name:"#F.orderNo", 			type:"TEXT", 	bind:"orderNo", size:20, mode:"READONLY"},
						{name:"#F.storageHeadId", 		type:"TEXT",  	bind:"storageHeadId", back:false, mode:"READONLY", size:10}]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", size:6, mode:"READONLY"},
	 					{name:"#F.brandName", 			type:"TEXT"  ,  bind:"brandName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.status", 				type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"TEXT", 	bind:"status", size:15, mode:"HIDDEN"},
					{name:"#F.statusName", 				type:"TEXT",  bind:"statusName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.storageTransactionType", 	type:"LABEL", 	value:"異動類別"}]},	 
	 			{items:[{name:"#F.storageTransactionType", 	type:"TEXT",  	bind:"storageTransactionType",	size:5, mode:"READONLY" }]},
	 			{items:[{name:"#L.storageTransactionDate", 	type:"LABEL", 	value:"異動日期<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.storageTransactionDate", 	type:"DATE",  	bind:"storageTransactionDate",	size:15 }]},
	 			{items:[{name:"#L.sourceOrderTypeCode",		type:"LABEL", 	value:"來源單別" }]}, 
				{items:[{name:"#F.sourceOrderTypeCode",		type:"TEXT", 	bind:"sourceOrderTypeCode", size:20}]},
				{items:[{name:"#L.sourceOrderNo",			type:"LABEL", 	value:"來源單號" }]},
				{items:[{name:"#F.sourceOrderNo",			type:"TEXT", 	bind:"sourceOrderNo", size:20}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.deliveryWarehouseCode", 	type:"LABEL" , value:"轉出倉庫<font color='red'>*</font>"}]},
	 			{items:[{name:"#F.deliveryWarehouseCode", 	type:"SELECT",  bind:"deliveryWarehouseCode", init:allWarehouses,  eChange:'onChangeStorageDeliveryWarehouseCode()'}]},
	 			{items:[{name:"#L.arrivalWarehouseCode", 	type:"LABEL" , value:"轉入倉庫<font color='red'>*</font>"}]},
	 			{items:[{name:"#F.arrivalWarehouseCode", 	type:"SELECT",  bind:"arrivalWarehouseCode", init:allWarehouses, eChange:'onChangeStorageArrivalWarehouseCode()'}]},
				{items:[{name:"#L.createBy",				type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",				type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
						{name:"#F.createByName",			type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.creationDate",			type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",			type:"TEXT", 	bind:"creationDate", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
			    {items:[{name:"#L.storageCode",			type:"LABEL", 	value:"儲位" }]},
				{items:[{name:"#F.storageCode",			type:"TEXT", 	bind:"storageCode", back:false}]},
	 			{items:[{name:"#L.description",			type:"LABEL", 	value:"敘述" }]}, 
				{items:[{name:"#F.description",			type:"TEXT", 	bind:"description", size:100 },
						{name:"#F.identification", 		type:"TEXT",  	bind:"identification", back:false, mode:"HIDDEN", size:1},
						{name:"#F.programId", 			type:"TEXT",  	bind:"programId", back:false, mode:"HIDDEN", size:1}],td:" colSpan=7"}
			]}
		],
		beginService:"",
		closeService:""			
	});
}

// 切換轉出庫別
function onChangeStorageDeliveryWarehouseCode() {
		if("MOVE" == vat.item.getValueByName("#F.storageTransactionType")){
			vat.item.setValueByName("#F.arrivalWarehouseCode", vat.item.getValueByName("#F.deliveryWarehouseCode"));
		}
		onChangeStorageWarehouseCode();
}

// 切換轉入庫別
function onChangeStorageArrivalWarehouseCode() {
		if("MOVE" == vat.item.getValueByName("#F.storageTransactionType")){
			vat.item.setValueByName("#F.deliveryWarehouseCode", vat.item.getValueByName("#F.arrivalWarehouseCode"));
		}
		onChangeStorageWarehouseCode();
}

// 切換庫別
function onChangeStorageWarehouseCode() {
	
	vat.block.pageDataSave( vatStorageDetail ,{
		saveSuccessAfter:function(){
			vat.ajax.XHRequest({
		           post:"process_object_name=imStorageService"+
		                    "&process_object_method_name=updateChangeWarehouse"+
		                    "&headId=" + vat.item.getValueByName("#F.storageHeadId") +
		                    "&deliveryWarehouseCode=" + vat.item.getValueByName("#F.deliveryWarehouseCode") +
		                    "&arrivalWarehouseCode=" + vat.item.getValueByName("#F.arrivalWarehouseCode"),
		           find: function change(oXHR){ 
		           		vat.block.pageDataLoad(vatStorageDetail, vnCurrentPage = 1);
		           }
			});
		}
	});
}

// tab切換 存檔
function doPageDataSave(div){
	var status = vat.item.getValueByName("#F.status");
	if(status == "SAVE" ){ 
		if(vatStorageDetail===div){
			vat.block.pageRefresh(div);
		}
	}
}
	
// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
		vat.bean().vatBeanOther.firstRecordNumber = 0;
      	vat.bean().vatBeanPicker.imStorageResult = null;  
    	refreshForm("");
	 }
}

function createRefreshForm(){
	refreshForm("");
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.imStorageResult !== null){
	    var vsMaxSize = vat.bean().vatBeanPicker.imStorageResult.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var headId = vat.bean().vatBeanPicker.imStorageResult[vat.bean().vatBeanOther.currentRecordNumber -1 ].storageHeadId;
		  refreshForm(headId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}

// 離開按鈕按下
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}

// 送出,暫存按鈕
function doSubmit(formAction){
	
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){
		alertMessage = "是否確定暫存?";
	}else if("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}
	
	if(confirm(alertMessage)){
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var orderNoPrefix		  = vat.item.getValueByName("#F.orderNo").substring(0,3);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var approvalResult        = getApprovalResult(); 
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
	    
	    var formStatus = status;
		if("SAVE" == formAction){
	        formStatus = "SAVE";
	    }else if("SUBMIT" == formAction || "SUBMIT_BG" == formAction ){
	        formStatus = changeFormStatus(formId, processId, status, formAction);
	    }else if("VOID" == formAction){
	        formStatus = "VOID";
	    }
	    
		if(status == "SAVE"){
			vat.block.pageDataSave( vatStorageDetail ,{ 
				funcSuccess:function(){
				
					vat.bean().vatBeanOther.formAction 			= formAction;
					vat.bean().vatBeanOther.formStatus 			= formStatus;
					vat.bean().vatBeanOther.beforeChangeStatus 	= status;
		  			vat.bean().vatBeanOther.processId       	= processId;
	  				vat.bean().vatBeanOther.approvalResult  	= approvalResult;
	  				vat.bean().vatBeanOther.approvalComment 	= approvalComment;
				    
					vat.block.submit(function(){return "process_object_name=imStorageAction"+
			                    "&process_object_method_name=performTransaction";},{
			                    bind:true, link:true, other:true,
			                    funcSuccess:function(){
					        		vat.block.pageRefresh(vatStorageDetail);
					        	}}
					);
		      	}
	      	});
		}else{
	    	alert("您的表單已加入待辦事件，請從待辦事件選取後，再次送出!");
	    }
	}
}

// 取得簽核結果
function getApprovalResult(){
	if(vat.item.getValueByName("#F.status") == "SIGNING"){
		return vat.item.getValueByName("#F.approvalResult").toString();
	}else{
		return "true";
	}
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var headId = vat.bean().vatBeanPicker.imStorageResult[vat.bean().vatBeanOther.currentRecordNumber -1].storageHeadId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(headId);
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
	  	var headId = vat.bean().vatBeanPicker.imStorageResult[vat.bean().vatBeanOther.currentRecordNumber -1].storageHeadId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(headId);
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
	  	var headId = vat.bean().vatBeanPicker.imStorageResult[vat.bean().vatBeanOther.currentRecordNumber -1].storageHeadId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(headId);
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
	    var headId = vat.bean().vatBeanPicker.imStorageResult[vat.bean().vatBeanOther.currentRecordNumber -1].storageHeadId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(headId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(vsfunctionCode){
	document.forms[0]["#formId"            ].value = vsfunctionCode; 
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = "";    
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=imStorageAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				vat.block.pageDataLoad(vatStorageDetail, vnCurrentPage = 1);
	        	vat.tabm.displayToggle(0, "xTab2", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
				doFormAccessControl();
     	}});
}

// 傳參數
function doPassData(div,x){
	var suffix = "";
	switch(div){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			suffix += "&orderTypeCode="+escape(orderTypeCode); 
			break;
	}
	return suffix;
}

// 依狀態鎖form
function doFormAccessControl(){
	var status 		= vat.item.getValueByName("#F.status");
	var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	var sourceOrderNo = vat.item.getValueByName("#F.sourceOrderNo");
	var storageTransactionType = vat.item.getValueByName("#F.storageTransactionType");
	var processId = null;
	if( vat.bean().vatBeanOther.processId != null ){
		processId	= vat.bean().vatBeanOther.processId; 
	}
	
	// 初始化
	//====================================================================
	vat.item.setAttributeByName("vatStorageHeader", "readOnly", false, true, true);
	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true); 
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true); 
	//===========================line========================================
	
	vat.block.canGridModify([vatStorageDetail], true,true,true);
	//===========================buttonLine==============================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.void", 		"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	vat.item.setStyleByName("#B.storageImport", 	"display", "inline");
	vat.item.setStyleByName("#B.storageExport", 	"display", "inline");
	
	if("MOVE" == storageTransactionType){
		//nothing
		vat.item.setAttributeByName("#F.storageCode", "readOnly", true);
	}else if("OUT" == storageTransactionType){
		vat.item.setAttributeByName("#F.arrivalWarehouseCode", "readOnly", true);
		vat.item.setAttributeByName("#F.storageCode", "readOnly", true);
	}else{
		vat.item.setAttributeByName("#F.deliveryWarehouseCode", "readOnly", true);
	}
	
	if(processId==""){
		vat.item.setStyleByName("#B.void", 	"display", "none");
		
		if(null != vat.bean().vatBeanPicker.imStorageResult || "SAVE" != status){
			vat.item.setAttributeByName("vatStorageHeader", "readOnly", true, true, true);
			vat.block.canGridModify([vatStorageDetail], false,false,false);
			vat.item.setStyleByName("#B.submit", "display", "none");
			vat.item.setStyleByName("#B.save", 	"display", "none");
			vat.item.setStyleByName("#B.storageImport", 	"display", "none");
			vat.item.setStyleByName("#B.extend", 	"display", "none");
		}
	
	}else{
		vat.item.setStyleByName("#B.search", "display", "none");
		vat.item.setStyleByName("#B.new", "display", "none");
	}
	
	vat.block.pageRefresh(vatStorageDetail);
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=" + vat.item.getValueByName("#F.programId")+
		"&levelType=ERROR" +
        "&identification=" + vat.item.getValueByName("#F.identification"), 
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// change formStatus 
function changeFormStatus(formId, processId, status, formAction){
	var sourceOrderTypeCode = vat.item.getValueByName("#F.sourceOrderTypeCode");
	var sourceOrderNo = vat.item.getValueByName("#F.sourceOrderNo");
    //if(sourceOrderTypeCode == "" || sourceOrderNo == ""){
        return "FINISH"; 
    //}else {
        //return "SAVE";
    //}
}

// 後端背景送出執行
/*
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imStorageServiceIoc"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}*/


// 票據列印
function openReportWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=imStorageServiceIoc"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
			                    	//alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);	

	if("AFTER_SUBMIT"==type) createRefreshForm();
}

function storageClean(){
	
}