/*** 
 *	檔案: imReceiveMoreOrLessAdjustment.js
 *	說明：短溢調整單
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;

//for 儲位用
var vatStorageDetail = 202;
var enableStorage = false;

function outlineBlock(){
 	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float"); 
		vat.tabm.createButton(0 ,"xTab1","調整單主檔" ,"vatMasterDiv"        		,"images/tab_master_data_dark.gif"      	,"images/tab_master_data_light.gif", false, "doPageDataSave("+vnB_Master+")");        
		vat.tabm.createButton(0 ,"xTab2","調整單明細檔" ,"vatDetailDiv"        	,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false, "doPageDataSave("+vnB_Detail+")");                                                                                                                                                                                                                       
 		vat.tabm.createButton(0 ,"xTab3","簽核資料"   ,"vatApprovalDiv"        ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif",vat.item.getValueByName("#F.status") == "SAVE" || vat.item.getValueByName("#F.status") == "UNCONFIRMED"? "none" : "inline");
 		
 		 //for 儲位用
 		enableStorage = "T2" == document.forms[0]["#loginBrandCode"    ].value;
		if(enableStorage){
  			vat.tabm.createButton(0, "xTab6", "儲位資料", "vatStorageDiv", "images/tab_storage_detail_dark.gif", "images/tab_storage_detail_light.gif", "", "reloadStorageDetail()");
  		}
  		
 	}  
  	masterInitial();
	detailInitial();
	
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
             vat.item.getValueByName("#F.orderTypeCode"), 
             vat.item.getValueByName("#F.orderNo"),
             document.forms[0]["#loginEmployeeCode"].value );
             
	//for 儲位用
	if(enableStorage){
		kweStorageBlock();
	}
	
	doFormAccessControl();
}

// 初始化
function formInitial(){  
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,	
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imReceiveAdjustmentAction&process_object_method_name=performInitial"; 
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
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_ReceiveAdjustment:moreOrLessSearch:20091015.page",    // ?orderTypeCode="+vat.bean().vatBeanOther.orderTypeCode
	 									 left:0, right:0, width:1024, height:768,	
	 									 servicePassData:function(){ return doPassData("buttonLine");},
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"saveSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.message"    , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//for 儲位用
	 			{name:"#B.storageExport", 	type:"IMG"    ,value:"儲位匯出",   src:"./images/button_storage_export.gif" , eClick:'exportStorageFormData()'},
	 			{name:"#B.storageImport",	type:"PICKER" , value:"儲位匯入",  src:"./images/button_storage_import.gif"  , 
						 openMode:"open", 
						 service:"/erp/fileUpload:standard:2.page",
						 servicePassData:function(x){ return importStorageFormData(); },
						 left:0, right:0, width:600, height:400,	
						 serviceAfterPick:function(){}},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

// 短溢調整單主檔
function headerInitial(){ 
	var allTaxTypes = vat.bean("allTaxTypes");
	var allOrderTypes = vat.bean("allOrderTypes");
	var allAdjustmentTypes = vat.bean("allAdjustmentTypes");
	
	var allSourceOrderTypeCode = vat.bean("allSourceOrderTypeCode");
	
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"調整進貨短溢到功能維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 		type:"LABEL", 	value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 		type:"SELECT", 	bind:"orderTypeCode", init:allOrderTypes, size:15, mode:"READONLY"},
					{name:"#F.headId", 					type:"TEXT",  	bind:"headId", back:false, mode:"READONLY"}
		 		 		//for 儲位用
						,{name:"#F.storageHeadId",   type:"TEXT",   bind:"storageHeadId",    back:false, mode:"READONLY" }
		 				]},
				{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"調整單號" }]},
				{items:[{name:"#F.orderNo", 			type:"TEXT", 	bind:"orderNo", size:20, mode:"READONLY"}]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", mode:"HIDDEN"},
	 					{name:"#F.brandName", 			type:"TEXT",  	bind:"brandName", size:8, mode:"READONLY"}]},
				{items:[{name:"#L.status", 				type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"TEXT", 	bind:"status", size:15, mode:"HIDDEN"},
					{name:"#F.statusName", 				type:"TEXT",  bind:"statusName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.taxType", 			type:"LABEL", 	value:"稅別"}]},	 
	 			{items:[{name:"#F.taxType", 			type:"SELECT",  bind:"taxType",  init:allTaxTypes,	size:15, mode:"READONLY" }]}, 
	 			{items:[{name:"#L.adjustmentType",		type:"LABEL", 	value:"調整類別" }]}, 
				{items:[{name:"#F.adjustmentType",		type:"SELECT", 	bind:"adjustmentType" , init:allAdjustmentTypes, mode:"READONLY" }]},
				{items:[{name:"#L.createBy",		type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",		type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.creationDate",		type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",		type:"TEXT", 	bind:"creationDate", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.sourceOrderTypeCode",		type:"LABEL", 	value:"來源進貨單別<font color='red'>*</font>" }]},
				{items:[{name:"#F.sourceOrderTypeCode",		type:"SELECT", 	bind:"sourceOrderTypeCode" , init:allSourceOrderTypeCode , eChange:"changeImReceiveData()" , size:20 }]}, 
				{items:[{name:"#L.sourceOrderNo",			type:"LABEL", 	value:"來源進貨單號<font color='red'>*</font>" }]},
				{items:[{name:"#F.sourceOrderNo",			type:"TEXT", 	bind:"sourceOrderNo", eChange:"changeImReceiveData()" , maxLen:20, size:20 },
						{name:"#F.sourceOrderNoMemo",		type:"TEXT", 	bind:"sourceOrderNoMemo", back:false, mode:"READONLY" }]},
				{items:[{name:"#L.isAdjustCost", 			type:"LABEL", 	value:"影響成本"}]},	 
	 			{items:[{name:"#F.isAdjustCost", 			type:"CHECKBOX",  bind:"affectCost",  	size:1 }]}, 
	 			{items:[{name:"#L.defaultWarehouseCode",	type:"LABEL", 	value:"庫別<font color='red'>*</font>" }]}, 
				{items:[{name:"#F.defaultWarehouseCode",	type:"TEXT", 	bind:"defaultWarehouseCode" }]}
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

// 短溢調撥單主檔2
function masterInitial(){

	vat.block.create( vnB_Master, {
		id: "vatMasterDiv", table:"cellspacing='1' class='default' border='0' cellpadding='3'",
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.adjustmentDate", 	type:"LABEL", 	value:"核准日期<font color='red'>*</font>" }]},
				{items:[{name:"#F.adjustmentDate", 	type:"Date", 	bind:"adjustmentDate", size:15 }]},
				{items:[{name:"#L.boxQty",			type:"LABEL", 	value:"總箱數" }]}, 
				{items:[{name:"#F.boxQty",			type:"NUMB", 	bind:"boxQty", maxLen:8 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.declarationDate",		type:"LABEL", 	value:"報關日期" }]}, 
				{items:[{name:"#F.declarationDate",		type:"Date", 	bind:"declarationDate", size:15, mode:"READONLY" }]},
				{items:[{name:"#L.declarationNo",		type:"LABEL", 	value:"報關單號" }]},
				{items:[{name:"#F.declarationNo",		type:"TEXT", 	bind:"declarationNo", size:15, mode:"READONLY"  }]} 
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.declarationType",		type:"LABEL", 	value:"報關類別" }]},
				{items:[{name:"#F.declarationType",		type:"TEXT", 	bind:"declarationType", size:15, mode:"READONLY" }], td:" colSpan=3"}
			]}
		], 	 
		 
		beginService:"",
		closeService:""			
	});
	
}

// 調整單明細
function detailInitial(){
	var status = vat.item.getValueByName("#F.status");
	var allMoreOrLessTypes = vat.bean("allMoreOrLessTypes");
//	alert(status);
	
	var isOpen = true;
	if( status == "SIGNING" || status == "FINISH" || status == "VOID" ){
		isOpen = false;	
	}
	
	var vbCanGridDelete = isOpen;
	var vbCanGridAppend = isOpen;
	var vbCanGridModify = isOpen;
  
	vat.item.make(vnB_Detail, "indexNo"				, {type:"IDX" , view:"fixed", desc:"序號" });
	vat.item.make(vnB_Detail, "moreOrLessType"		, {type:"SELECT" , view:"fixed", size:10, desc:"短溢調整", init:allMoreOrLessTypes , mode:"READONLY" }); 
	vat.item.make(vnB_Detail, "itemCode"			, {type:"TEXT" , view:"fixed", size:20, maxLen:23, desc:"品號", eChange:"changeLineData()"          	});
	
	vat.item.make(vnB_Detail, "itemCName"			, {type:"TEXT" , view:"", size:10, desc:"品名", mode:"READONLY"    	});
	vat.item.make(vnB_Detail, "lotNo"				, {type:"TEXT" , view:"", size:10, desc:"批號"				    	});
	vat.item.make(vnB_Detail, "localUnitCost"		, {type:"NUMB" , view:"", size:10, desc:"售價", mode:"READONLY"     	});
	vat.item.make(vnB_Detail, "amount"				, {type:"NUMB" , view:"", size:10, desc:"總成本"     	});
	
	vat.item.make(vnB_Detail, "warehouseCode"		, {type:"TEXT" , view:"", desc:"庫別", mode:"READONLY" });
	vat.item.make(vnB_Detail, "difQuantity"			, {type:"NUMB" , view:"", desc:"數量" }); // quantity
	vat.item.make(vnB_Detail, "originalDeclarationNo"	, {type:"TEXT" , view:"", desc:"報單單號", mode:"READONLY" });
	vat.item.make(vnB_Detail, "originalDeclarationSeq"	, {type:"NUMB" , view:"", desc:"報單項次" });
	vat.item.make(vnB_Detail, "boxNo"					, {type:"TEXT" , view:"shift",desc:"箱號" });
	vat.item.make(vnB_Detail, "weight"					, {type:"NUMB" , view:"shift",desc:"重量" });
	
	vat.item.make(vnB_Detail, "customsItemCode"	, {type:"TEXT" , desc:"海關品號", mode:"HIDDEN" });
	
	vat.item.make(vnB_Detail, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_Detail, "isDeleteRecord"  , {type:"DEL"  , view:"fixed", desc:"刪除"});
	vat.item.make(vnB_Detail, "message"         , {type:"MSG"  , desc:"訊息"});
	vat.item.make(vnB_Detail, "originalDeclarationDate", {type:"HIDDEN"});
	vat.block.pageLayout(vnB_Detail, {
														id: "vatDetailDiv", 
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
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

// 第一次載入 重新整理
function loadBeforeAjxService(div){
//    alert(div);	
	if( vnB_Detail === div ){
		var processString = "process_object_name=imReceiveAdjustmentService&process_object_method_name=getAJAXMoreOrLessPageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
		                    "&taxType=" + vat.item.getValueByName("#F.taxType");
		return processString;	
	}							
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
//    alert("saveBeforeAjxService"); 

	if( vnB_Detail === div ){
		var processString = "process_object_name=imReceiveAdjustmentService&process_object_method_name=updateAJAXMoreOrLessPageLinesData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value + 
		                    "&status=" + vat.item.getValueByName("#F.status");
		return processString;	
	}		
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){ 
//	vat.block.pageRefresh(div); 
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
//    alert("loadSuccessAfter");	
//	vat.item.setGridAttributeByName("objectCode", "readOnly", true);
} 

// 新增空白頁
function appendBeforeService(){
//    alert("appendBeforeService");	 
	return true;
}    

// 新增空白頁成功後
function appendAfterService(){
//    alert("appendAfterService");	
} 

function eventService(){
//	alert("eventService");
} 
 

// tab切換 存檔
function doPageDataSave(div){

	if(vnB_Master===div){
//		vat.block.pageSearch(vnB_Detail); //存檔vnB_Detail 
	}else if(vnB_Detail===div){
//		vat.block.pageRefresh(div);
	}
}
	
// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
    	refreshForm("");
	 }
}

function createRefreshForm(){
	refreshForm("");
}

// 傳參數
function doPassData(id){
	var suffix="";
	switch(id){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
			var adjustmentType = vat.item.getValueByName("#F.adjustmentType");	 
			suffix += "&orderTypeCode="+escape(orderTypeCode)+"&adjustmentType="+escape(adjustmentType); 
			break;
	}		
	return suffix;
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(typeof vat.bean().vatBeanPicker.result != 'undefined'){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(code);
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
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var orderNoPrefix		  = vat.item.getValueByName("#F.orderNo").substring(0,3);
		var approvalResult        = getApprovalResult();
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
	    
		if((orderNoPrefix == "TMP" &&  status == "SAVE") || status == "UNCONFIRMED" ||
			(inProcessing   && (status == "SAVE"  || status == "SIGNING" || status == "REJECT" ))){
			
			vat.block.pageDataSave( vnB_Detail ,{ 
				funcSuccess:function(){
					vat.bean().vatBeanOther.formAction 		= formAction;
		  			vat.bean().vatBeanOther.processId       = processId;
	  				vat.bean().vatBeanOther.approvalResult  = approvalResult;
	  				vat.bean().vatBeanOther.approvalComment =  approvalComment;
	
				    if("SUBMIT_BG" == formAction){
				      	vat.block.submit(function(){return "process_object_name=imReceiveAdjustmentAction"+
				                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
				    }else{
						vat.block.submit(function(){return "process_object_name=imReceiveAdjustmentAction"+
				                    "&process_object_method_name=performMoreOrLessTransaction";},{
				                    bind:true, link:true, other:true,
				                    funcSuccess:function(){
						        		vat.block.pageRefresh(vnB_Detail);
						        	}}
						);
			        } 
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

// 動態改變一筆商品資料
function changeLineData(){
	var vLineId				= vat.item.getGridLine();
	var itemCode			= vat.item.getGridValueByName("itemCode", vLineId);
	var taxType				= vat.item.getValueByName("#F.taxType");
	var sourceOrderNo 		= vat.item.getValueByName("#F.sourceOrderNo");
	var sourceOrderNoMemo 	= vat.item.getValueByName("#F.sourceOrderNoMemo");  // 為驗證有效來源單號用
	var declarationNo 		= vat.item.getValueByName("#F.declarationNo");  
	
	
	if( sourceOrderNo !== "" && sourceOrderNoMemo ===""  ){
		vat.ajax.XHRequest(
	       {
	           post:"process_object_name=imReceiveAdjustmentService"+
	                    "&process_object_method_name=getAJAXImItem"+
	                    "&itemCode=" + itemCode +
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&taxType=" + vat.item.getValueByName("#F.taxType") +
	                    "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") +
	                    "&sourceOrderNo=" + sourceOrderNo +
	                    "&declarationNo=" + declarationNo,
	           find: function change(oXHR){ 
	           		vat.item.setGridValueByName("moreOrLessType", vLineId, vat.ajax.getValue("moreOrLessType", oXHR.responseText));
	          		vat.item.setGridValueByName("itemCName", vLineId, vat.ajax.getValue("itemCName", oXHR.responseText));
	          		vat.item.setGridValueByName("lotNo", vLineId, vat.ajax.getValue("lotNo", oXHR.responseText));
	          		vat.item.setGridValueByName("localUnitCost", vLineId, vat.ajax.getValue("localUnitCost", oXHR.responseText));
					vat.item.setGridValueByName("amount", vLineId, vat.ajax.getValue("amount", oXHR.responseText));
					vat.item.setGridValueByName("warehouseCode", vLineId, vat.ajax.getValue("warehouseCode", oXHR.responseText));
					vat.item.setGridValueByName("difQuantity", vLineId, vat.ajax.getValue("difQuantity", oXHR.responseText));
					vat.item.setGridValueByName("originalDeclarationNo", vLineId, vat.ajax.getValue("originalDeclarationNo", oXHR.responseText));
					vat.item.setGridValueByName("originalDeclarationSeq", vLineId, vat.ajax.getValue("originalDeclarationSeq", oXHR.responseText));
					vat.item.setGridValueByName("originalDeclarationDate", vLineId, vat.ajax.getValue("originalDeclarationDate", oXHR.responseText));
					vat.item.setGridValueByName("boxNo", vLineId, vat.ajax.getValue("boxNo", oXHR.responseText));
					vat.item.setGridValueByName("weight", vLineId, vat.ajax.getValue("weight", oXHR.responseText));
					vat.item.setGridValueByName("customsItemCode", vLineId, vat.ajax.getValue("customsItemCode", oXHR.responseText));
	           },
	           fail: function changeError(){
	          		vat.item.setGridValueByName("itemCode", vLineId, "");
			    	vat.item.setGridValueByName("moreOrLessType", vLineId, "1");
			     	vat.item.setGridValueByName("itemCName", vLineId, "");
			     	vat.item.setGridValueByName("lotNo", vLineId, "");
			     	vat.item.setGridValueByName("localUnitCost", vLineId, "");
					vat.item.setGridValueByName("amount", vLineId, "");
					vat.item.setGridValueByName("warehouseCode", vLineId, "");
					vat.item.setGridValueByName("difQuantity", vLineId, "");
					vat.item.setGridValueByName("originalDeclarationNo", vLineId, "");
					vat.item.setGridValueByName("originalDeclarationSeq", vLineId, "");
					vat.item.setGridValueByName("originalDeclarationDate", vLineId, "");
					vat.item.setGridValueByName("boxNo", vLineId, "");
					vat.item.setGridValueByName("weight", vLineId, "");
					vat.item.setGridValueByName("customsItemCode", vLineId, "");
	           }   
	       });
    }else if( sourceOrderNo === "" && sourceOrderNoMemo ==="" ){
    	alert("請先輸入來源進貨單號");
    	vat.item.setGridValueByName("itemCode", vLineId, "");
    	vat.item.setGridValueByName("moreOrLessType", vLineId, "1");
     	vat.item.setGridValueByName("itemCName", vLineId, "");
     	vat.item.setGridValueByName("localUnitCost", vLineId, "");
		vat.item.setGridValueByName("amount", vLineId, "");
		vat.item.setGridValueByName("warehouseCode", vLineId, "");
		vat.item.setGridValueByName("difQuantity", vLineId, "");
		vat.item.setGridValueByName("originalDeclarationNo", vLineId, "");
		vat.item.setGridValueByName("originalDeclarationSeq", vLineId, "");
		vat.item.setGridValueByName("originalDeclarationDate", vLineId, "");
		vat.item.setGridValueByName("boxNo", vLineId, "");
		vat.item.setGridValueByName("weight", vLineId, "");
		vat.item.setGridValueByName("customsItemCode", vLineId, "");
    }else if( sourceOrderNo !== "" && sourceOrderNoMemo !=="" ){
    	alert("請先輸入有效的來源進貨單號");
    	vat.item.setGridValueByName("itemCode", vLineId, "");
    	vat.item.setGridValueByName("moreOrLessType", vLineId, "1");
     	vat.item.setGridValueByName("itemCName", vLineId, "");
     	vat.item.setGridValueByName("localUnitCost", vLineId, "");
		vat.item.setGridValueByName("amount", vLineId, "");
		vat.item.setGridValueByName("warehouseCode", vLineId, "");
		vat.item.setGridValueByName("difQuantity", vLineId, "");
		vat.item.setGridValueByName("originalDeclarationNo", vLineId, "");
		vat.item.setGridValueByName("originalDeclarationSeq", vLineId, "");
		vat.item.setGridValueByName("originalDeclarationDate", vLineId, "");
		vat.item.setGridValueByName("boxNo", vLineId, "");
		vat.item.setGridValueByName("weight", vLineId, "");
		vat.item.setGridValueByName("customsItemCode", vLineId, "");
    }
}

// 將有關的欄位和grid都替換
function changeImReceiveData(){
	//alert("changeRelationData");
	var sourceOrderNo = vat.item.getValueByName("#F.sourceOrderNo").replace(/^\s+|\s+$/, '').toUpperCase();
	
	
	vat.ajax.XHRequest(
       {
           post:"process_object_name=imReceiveAdjustmentService"+
                    "&process_object_method_name=updateAJAXByImReceiveData"+
                    "&headId=" + vat.item.getValueByName("#F.headId") +
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&sourceOrderTypeCode=" + vat.item.getValueByName("#F.sourceOrderTypeCode") + 
                    "&taxType=" + vat.item.getValueByName("#F.taxType") + 
                    "&sourceOrderNo=" + sourceOrderNo ,
           find: function change(oXHR){ 
           		vat.item.setValueByName("#F.sourceOrderNo", vat.ajax.getValue("sourceOrderNo", oXHR.responseText) );
           		vat.item.setValueByName("#F.sourceOrderNoMemo", vat.ajax.getValue("sourceOrderNoMemo", oXHR.responseText) );
           		vat.item.setValueByName("#F.declarationDate", vat.ajax.getValue("declarationDate", oXHR.responseText) );
           		vat.item.setValueByName("#F.declarationNo", vat.ajax.getValue("declarationNo", oXHR.responseText) );
           		vat.item.setValueByName("#F.declarationType", vat.ajax.getValue("declarationType", oXHR.responseText) );
//           		vat.item.setValueByName("#F.defaultWarehouseCode", vat.ajax.getValue("defaultWarehouseCode", oXHR.responseText) );
//           		vat.block.pageRefresh(vnB_Master);
           		vat.block.pageRefresh(vnB_Detail);
           },
           fail: function changeError(){
          		vat.item.setValueByName("#F.sourceOrderNo", sourceOrderNo );
           		vat.item.setValueByName("#F.sourceOrderNoMemo", "查無此進貨單" );
           		vat.item.setValueByName("#F.declarationDate", "" );
           		vat.item.setValueByName("#F.declarationNo", "" );
           		vat.item.setValueByName("#F.declarationType", "" );
//           		vat.item.setValueByName("#F.defaultWarehouseCode", "" );
//           		vat.block.pageRefresh(vnB_Master);
           		vat.block.pageRefresh(vnB_Detail);	
           }   
       });
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"            ].value = code; 
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = "";    
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=imReceiveAdjustmentAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
     			
     			vat.tabm.displayToggle(0, "xTab2", true, false, false);
 				refreshWfParameter( vat.item.getValueByName("#F.brandCode"), 
        					vat.item.getValueByName("#F.orderTypeCode"), 
        					vat.item.getValueByName("#F.orderNo"));
     			vat.block.pageRefresh(102);	
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
				vat.tabm.displayToggle(0, "xTab3", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
				doFormAccessControl();
     	}});
 	
    
}

function doAfterPickerFunctionProcess(){
	//do picker back something
}

// 依狀態鎖form
function doFormAccessControl(){
	var status 		= vat.item.getValueByName("#F.status");
	var processId	= vat.bean().vatBeanOther.processId;
	var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	
	// 初始化
	//======================<header>=============================================
	vat.item.setAttributeByName("#F.sourceOrderTypeCode", "readOnly", false);
	vat.item.setAttributeByName("#F.sourceOrderNo", "readOnly", false);
	vat.item.setAttributeByName("#F.isAdjustCost", "readOnly", false);
	vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", false);
	
	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true); 
	//======================<master>=============================================
	vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", false); 
	vat.item.setAttributeByName("#F.boxQty", "readOnly", false);
	//======================<detail>=============================================
	vat.item.setGridAttributeByName("itemCode", "readOnly", false); 
	vat.item.setGridAttributeByName("amount", "readOnly", false);
	vat.item.setGridAttributeByName("difQuantity", "readOnly", false);
	vat.item.setGridAttributeByName("originalDeclarationSeq", "readOnly", false);
	vat.item.setGridAttributeByName("boxNo", "readOnly", false);
	vat.item.setGridAttributeByName("weight", "readOnly", false);
	//=======================<buttonLine>========================================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.print", 	"display", "inline");
	vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	//===========================================================================
	
	//for 儲位用
	if(enableStorage){
		vat.item.setStyleByName("#B.storageExport"   , "display", "inline");
		vat.item.setStyleByName("#B.storageImport"   , "display", "inline");
	}else{
		vat.item.setStyleByName("#B.storageExport"   , "display", "none");
		vat.item.setStyleByName("#B.storageImport"   , "display", "none");
	}
	
	if(orderNoPrefix == "TMP" ){
		vat.item.setStyleByName("#B.print", 	"display", "none");
	}
	
	if( processId != null && processId != 0 ){ //從待辦事項進入
		vat.item.setStyleByName("#B.new", 		"display", "none");
		vat.item.setStyleByName("#B.search", 	"display", "none");
		if( status == "SAVE" || status == "REJECT" ){
			vat.item.setStyleByName("#B.void", 		"display", "inline"); 
		}else if( status == "SIGNING" ){
			vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
			vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
		}
	}else{
		// 查詢回來
		if(orderNoPrefix == "TMP" ){
		
		}else{
			
			//======================<header>=============================================
			vat.item.setAttributeByName("#F.sourceOrderTypeCode", "readOnly", true);
			vat.item.setAttributeByName("#F.sourceOrderNo", "readOnly", true);
			vat.item.setAttributeByName("#F.isAdjustCost", "readOnly", true);
			vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true); 
			//======================<master>=============================================
			vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", true); 
			vat.item.setAttributeByName("#F.boxQty", "readOnly", true);
			//======================<detail>=============================================
			vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
			vat.item.setGridAttributeByName("amount", "readOnly", true);
			vat.item.setGridAttributeByName("difQuantity", "readOnly", true);
			vat.item.setGridAttributeByName("originalDeclarationSeq", "readOnly", true);
			vat.item.setGridAttributeByName("boxNo", "readOnly", true);
			vat.item.setGridAttributeByName("weight", "readOnly", true);
			//=======================<buttonLine>========================================
			vat.item.setStyleByName("#B.submit", 	"display", "none"); 
			vat.item.setStyleByName("#B.save", 		"display", "none"); 
			vat.item.setStyleByName("#B.submitBG", 	"display", "none"); 
			vat.item.setStyleByName("#B.message", 	"display", "none"); 
			//===========================================================================
		}
	}		
	
	if( status == "SIGNING" || status == "FINISH" || status == "VOID" || status == "COLSE" ){
		//======================<header>=============================================
		vat.item.setAttributeByName("#F.sourceOrderTypeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.sourceOrderNo", "readOnly", true);
		vat.item.setAttributeByName("#F.isAdjustCost", "readOnly", true);
		vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true); 
		//======================<master>=============================================
		vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", true); 
		vat.item.setAttributeByName("#F.boxQty", "readOnly", true);
		//======================<detail>=============================================
		vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
		vat.item.setGridAttributeByName("amount", "readOnly", true);
		vat.item.setGridAttributeByName("difQuantity", "readOnly", true);
		vat.item.setGridAttributeByName("originalDeclarationSeq", "readOnly", true);
		vat.item.setGridAttributeByName("boxNo", "readOnly", true);
		vat.item.setGridAttributeByName("weight", "readOnly", true);
		//=======================<buttonLine>========================================
		
		if( status == "SIGNING" ){
			vat.item.setStyleByName("#B.submit", "display", "inline");
		}else{
			vat.item.setStyleByName("#B.submit", "display", "none");
		}
		vat.item.setStyleByName("#B.save", 		"display", "none");
		vat.item.setStyleByName("#B.submitBG", 	"display", "none");
		vat.item.setStyleByName("#B.message", 	"display", "none");
		//===========================================================================
	}

	// 固定勾選『影響成本』的欄位 add by Weichun 2012.03.14
	vat.item.setValueByName("#F.isAdjustCost", "Y");
	vat.item.setAttributeByName("#F.isAdjustCost", "readOnly", true);
}

//	訊息提示
function showMessage(){
	var width = "1500";
    var height = "1000";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_RECEIVE_ADJUSTMENT" + 
		"&levelType=ERROR" +
        "&processObjectName=imReceiveAdjustmentService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// change formStatus 
function changeFormStatus(formId, processId, status, formAction){
    var formStatus = "";
    if(formId === null || formId === "" ){
        formStatus = "SAVE"; 
    }else if(processId !== null && processId !== "" && processId !== 0){
        if(status == "SAVE" ){
            formStatus = "FINISH";
        }
    }
    return formStatus;
}

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imReceiveAdjustmentAction"+
        "&process_object_method_name=performMoreOrLessTransactionForBackGround";}, {bind:true, link:true, other:true});
}

// 票據列印
function openReportWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=imReceiveAdjustmentService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
			                    	//alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);	

	if("AFTER_SUBMIT"==type) createRefreshForm();
}

function openDeclarationLog(){
	
	alertMessage = "是否要繼續報單修改";
	if(confirm(alertMessage)){
		var width = "1500";
    	var height = "1000";
		var returnData = window.open(
		"/erp/Cm_CustomsDeclaration:update:20150415.page?" + 
		"statusType=2"+ 
		"&aifOrderNo="+ vat.item.getValueByName("#F.orderNo"),
		"errorMsg",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
		window.top.close();	
	}else{
		window.top.close();
	}
}