/*** 
 *	檔案: imItemBarCode.js 補條碼
 *	說明：表單明細
 *	修改：david
 *  <pre>
 *  	Created by david
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

var activeTab = 2;

function outlineBlock(){

  	formInitial();
	buttonLine();
 	headerInitial();

	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0,"xTab1","明細資料"   ,"vatDetailDiv"                   ,"images/tab_item_data_dark.gif"            ,"images/tab_item_data_light.gif" , false, "doPageRefresh()");
	}
  	
  	detailInitial();  
	doFormAccessControl();
}

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	 	vat.bean().vatBeanOther = { 
        	loginBrandCode  	: document.forms[0]["#loginBrandCode" ].value,
        	loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
        	formId             	: document.forms[0]["#formId"            ].value,	
        	orderTypeCode       : document.forms[0]["#orderTypeCode"     ].value,
        	processId          	: document.forms[0]["#processId"         ].value, 
          	assignmentId       	: document.forms[0]["#assignmentId"      ].value,	
        	currentRecordNumber : 0,
	   		lastRecordNumber    : 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imItemBarcodeHeadAction&process_object_method_name=performInitial"; 
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
	 									 service:"Im_ItemBarcode:search:20100611.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 servicePassData:function(){ return doPassData("buttonLine");},
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 	 		{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 	 		{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 	 		{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
	 	 		{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
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
}

// 主檔
function headerInitial(){ 
	var allOrderTypeCodes = vat.bean("allOrderTypeCodes");
	
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"補條碼維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 	type:"LABEL", 	value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 	type:"SELECT",	size:16, 	bind:"orderTypeCode", init: allOrderTypeCodes, mode:"READONLY"}]},
				{items:[{name:"#L.orderNo", 		type:"LABEL", 	value:"單號"}]},
				{items:[{name:"#F.orderNo", 		type:"TEXT",	size:16, 	bind:"orderNo", mode:"READONLY"},
						{name:"#F.headId", 			type:"TEXT"  ,  bind:"headId", back:false, mode:"HIDDEN"}]},	
			    {items:[{name:"#L.brandCode", 		type:"LABEL", 	value:"品牌"}]},	
				{items:[{name:"#F.brandCode", 		type:"TEXT", 	bind:"brandCode", mode:"HIDDEN" },
			            {name:"#F.brandName", 		type:"TEXT",	bind:"brandName", back:false, mode:"READONLY"}]},
			    {items:[{name:"#L.status", 			type:"LABEL", 	value:"狀態"}]},       
			    {items:[{name:"#F.status", 			type:"TEXT", 	bind:"status", 	mode:"HIDDEN" },
			            {name:"#F.statusName", 		type:"TEXT",	bind:"statusName", back:false, mode:"READONLY"}]} 		 	
			]},
			{row_style:"", cols:[
			    {items:[{name:"#L.dueDate", 		type:"LABEL", 	value:"到期日<font color='red'>*</font>" }]},
				{items:[{name:"#F.dueDate", 		type:"DATE", 	bind:"dueDate",size:1}],td:" colSpan=3"},        
			    {items:[{name:"#L.createBy",		type:"LABEL", 	value:"建檔人員" }]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",		type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
			    {items:[{name:"#L.creationDate",	type:"LABEL", 	value:"建檔日期" }]},
				{items:[{name:"#F.creationDate",	type:"TEXT", 	bind:"creationDate", mode:"READONLY"}]} 			
			]}	
		], 	
		beginService:"",
		closeService:""			
	});
}

// 明細
function detailInitial(){
    
    vat.item.make(vnB_Detail, "indexNo"  		, {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_Detail, "itemCode"		, {type:"TEXT" , size:13, maxLen:13, desc:"品號", eChange:"changeItemCode()" });
	vat.item.make(vnB_Detail, "itemCName"	    , {type:"TEXT" , bind:"itemCName",size:10, desc:"品名",mode:"READONLY", alter:true});
	vat.item.make(vnB_Detail, "unitPrice"	    , {type:"NUMB" , size:5, desc:"售價", mode:"READONLY" });
	vat.item.make(vnB_Detail, "paper"			, {type:"NUMB" , bind:"paper",size:5, desc:"張數"});
	
	vat.item.make(vnB_Detail, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_Detail, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_Detail, "message"         , {type:"MSG"  , desc:"訊息"});
	
	vat.block.pageLayout(vnB_Detail, {
		id: "vatDetailDiv",
		pageSize: 10,											
		canGridDelete : true,
		canGridAppend : true,
		canGridModify : true,						
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail+")",
		loadSuccessAfter    : "loadSuccessAfter()",						
		eventService        : "eventService()",   
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_Detail+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_Detail+")"
	});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

	
// 建立新資料按鈕	
function createNewForm(){

    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.bean().vatBeanPicker.resultBarcode = null;  
     	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
    	refreshForm("");
	 }
}

// 刷新頁面
function refreshForm(vsHeadId){   
	document.forms[0]["#formId"            ].value = vsHeadId;
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = "";    
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
				return "process_object_name=imItemBarcodeHeadAction&process_object_method_name=performInitial"; 
	   	},{ other: true, 
     		funcSuccess:function(){
     	   	vat.item.bindAll();
     	    vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     	    
     	    doFormAccessControl();
     	  }}
    );
}

function resetForm(){
    refreshForm("");
}	

// picker 檢視回來作用的事件
function doAfterPickerProcess(){
//    alert("after picker process");
	if(typeof vat.bean().vatBeanPicker.resultBarcode != "undefined" ){
		result = vat.bean().vatBeanPicker.resultBarcode;
	    var vsMaxSize = result.length;
	    if( vsMaxSize === 0){
	  	vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		  
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  vsHeadId = result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(vsHeadId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
		
	}
}	
	
// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			suffix += "&orderTypeCode="+escape(orderTypeCode); 
			break;
	}
	return suffix;
}				
	
function doPageDataSave(div){
	if(div == vnB_Detail){
		activeTab = vnB_Detail;
	}
}
	
// 第一次載入 重新整理
function loadBeforeAjxService(div){
	var	processString = "process_object_name=imItemBarcodeHeadService&process_object_method_name=getAJAXPageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId")+
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode");
		return processString;				
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
	var processString = "";
	
	processString = "process_object_name=imItemBarcodeHeadService&process_object_method_name=updateAJAXPageLinesData" + 
			"&headId=" + vat.item.getValueByName("#F.headId") + 
			"&status=" + vat.item.getValueByName("#F.status") + 
			"&brandCode=" +  vat.bean().vatBeanOther.loginBrandCode + 
			"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value;
			
		return processString;
}   

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){ 
//	     vat.block.pageRefresh(vnB_Detail);
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
    vat.block.pageRefresh(0);
} 

// 新增空白頁
function appendBeforeService(){
	return true;
}    

// 新增空白頁成功後
function appendAfterService(){
} 

// 自定事件
function eventService(){
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
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var orderNoPrefix		  = vat.item.getValueByName("#F.orderNo").substring(0,3);
		var approvalResult 		  = "true";
		
		 if((orderNoPrefix == "TMP" &&  status == "SAVE") ||
			(inProcessing   && (status == "SAVE" ))){
			vat.block.pageDataSave(activeTab, {  funcSuccess:function(){
					vat.bean().vatBeanOther.processId = processId;
					vat.bean().vatBeanOther.formAction = formAction;
					vat.bean().vatBeanOther.approvalResult = approvalResult;
					
					if("SUBMIT_BG" == formAction){
				      	vat.block.submit(function(){
				      		return "process_object_name=imItemBarcodeHeadAction"+
				                    "&process_object_method_name=getOrderNo";
				        	}, {bind:true, link:true, other:true}
				        );
				    }else{
						vat.block.submit(function(){
								return "process_object_name=imItemBarcodeHeadAction"+
				                    "&process_object_method_name=performTransaction";
				            },{
				            	bind:true, link:true, other:true,
				            	funcSuccess:function(){
						        	vat.block.pageRefresh(activeTab);
						    	}
						    }
						);
			        } 
					
			}});	      
	  	}else{
	    	alert("您的表單已加入待辦事件，請從待辦事件選取後，再次送出!");
	    }      
	}
}

function gotoFirst(){
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	        vsHeadId = vat.bean().vatBeanPicker.resultBarcode[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
    if(vat.bean().vatBeanOther.firstRecordNumber > 0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	    vsHeadId = vat.bean().vatBeanPicker.resultBarcode[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
	  	    vsHeadId = vat.bean().vatBeanPicker.resultBarcode[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
	        vsHeadId = vat.bean().vatBeanPicker.resultBarcode[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm(vsHeadId);
	    }else{
	  	    alert("目前已在最後一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
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

// 匯入
function importFormData(){
	var width = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
    
    	var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=IM_ITEM_BARCODE" +
			"&importFileType=XLS" +
	        "&processObjectName=imItemBarcodeHeadService" + 
	        "&processObjectMethodName=executeImportLists" +
	        "&arguments=" + headId +
	        "&parameterTypes=LONG" +
	        "&blockId=" + vnB_Detail,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 匯出
function exportFormData(){
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IM_ITEM_BARCODE" +
              "&fileType=XLS" + 
              "&processObjectName=imItemBarcodeHeadService" + 
              "&processObjectMethodName=executeFind" + 
              "&gridFieldName=imItemBarcodeLines" + 
              "&arguments=" + vat.item.getValueByName("#F.headId") + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '補條碼匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 欄位鎖定
function doFormAccessControl(){
	var status 		= vat.item.getValueByName("#F.status");
	var formId = vat.bean().vatBeanOther.formId;
	var orderNoPrefix         = vat.item.getValueByName("#F.orderNo").substring(0,3);
	var processId = null;
	if( vat.bean().vatBeanOther.processId != null ){
		processId	= vat.bean().vatBeanOther.processId; 
	}
	
  	// 初始化
  	vat.item.setStyleByName("#B.void", 		"display", "none"); 
 	vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true, true);
 	vat.block.canGridModify([vnB_Detail], true,true,true);
 	
 	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	vat.item.setStyleByName("#B.import", 	"display", "inline");
 	
 	// 流程內
	if( processId != null && processId != 0 ){ //從待辦事項進入
		vat.item.setStyleByName("#B.new", 		"display", "none");
		vat.item.setStyleByName("#B.search", 	"display", "none");
		if( status == "SAVE" ){
			vat.item.setStyleByName("#B.void", 		"display", "inline"); 
		}
 	}else{ // 查詢回來
		if(orderNoPrefix == "TMP" ){
		}else{
			vat.item.setStyleByName("#B.submit", 	"display", "none"); 
			vat.item.setStyleByName("#B.save", 		"display", "none"); 
			vat.item.setStyleByName("#B.submitBG", 	"display", "none"); 
			vat.item.setStyleByName("#B.message", 	"display", "none"); 
			vat.item.setStyleByName("#B.import", 	"display", "none"); 
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			vat.block.canGridModify([vnB_Detail], false,false,false);
		}
	}
	
	
	if( status == "FINISH" || status == "VOID" ){
 		vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
 		vat.block.canGridModify([vnB_Detail], false,false,false);
 		
 		vat.item.setStyleByName("#B.save", 		"display", "none"); 
		vat.item.setStyleByName("#B.submitBG", 	"display", "none"); 
		vat.item.setStyleByName("#B.message", 	"display", "none"); 
		vat.item.setStyleByName("#B.import", 	"display", "none"); 
 	}
	
}

// 後端背景送出再執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imItemBarcodeHeadAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

// 動態撈出品號
function changeItemCode(){
	var nItemLine = vat.item.getGridLine();
	var sItemCode = vat.item.getGridValueByName("itemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var processString = "process_object_name=imItemService&process_object_method_name=getAJAXItemCode"+ 
						"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
						"&itemCode=" + sItemCode + 
						"&priceType=1";
	vat.ajax.XHRequest({	
		post:processString,					 	
		find: function change(oXHR){ 
			vat.item.setGridValueByName("itemCode",nItemLine, vat.ajax.getValue("itemCode",oXHR.responseText));
			vat.item.setGridValueByName("unitPrice",nItemLine, vat.ajax.getValue("unitPrice",oXHR.responseText));
			vat.item.setGridValueByName("itemCName",nItemLine, vat.ajax.getValue("itemCName",oXHR.responseText));
			vat.item.setGridValueByName("paper",nItemLine, vat.ajax.getValue("paper",oXHR.responseText));
		}
	});						

}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_ITEM_BARCODE" + 
		"&levelType=ERROR" +
        "&processObjectName=imItemBarcodeHeadService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 送出後反回更新前端
function createRefreshForm(){
	refreshForm("");
}
