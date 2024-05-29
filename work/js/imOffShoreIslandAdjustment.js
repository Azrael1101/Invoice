/*** 
 *	檔案: 只有T2 用的到 imOffShoreIslandAdjustment.js
 *	說明：離島調整單
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;
var vnB_Amount = 4;

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
		vat.tabm.createButton(0 ,"xTab4","金額統計" ,"vatAmountDiv"        		,"images/tab_total_amount_dark.gif"      	,"images/tab_total_amount_light.gif", false, "doPageDataSave("+vnB_Amount+")" );

		vat.tabm.createButton(0 ,"xTab3","簽核資料"   ,"vatApprovalDiv"        ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif",vat.item.getValueByName("#F.status") == "SAVE" || vat.item.getValueByName("#F.status") == "UNCONFIRMED"? "none" : "inline");
		
		 //for 儲位用
 		//enableStorage = "T2" == document.forms[0]["#loginBrandCode"    ].value;
		if(enableStorage){
  			vat.tabm.createButton(0, "xTab6", "儲位資料", "vatStorageDiv", "images/tab_storage_detail_dark.gif", "images/tab_storage_detail_light.gif", "", "reloadStorageDetail()");
  		}
 	}  
 	
	masterInitial();
	detailInitial();
	amountInitial();
	
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
		vat.item.getValueByName("#F.orderTypeCode"), 
		vat.item.getValueByName("#F.orderNo"),
		document.forms[0]["#loginEmployeeCode"].value );
	
	//for 儲位用
	if(enableStorage){
		kweStorageBlock();
	}
	checkCustomsStatus();
	doFormAccessControl();
	checkUploadControl();
	getCustomsDesc();
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
          userType       		: document.forms[0]["#userType"      ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imGeneralAdjustmentAction&process_object_method_name=performOffShoreIsIandInitial"; 
	    	},{								
	    		other: true
    	});
  	}
}

function checkCustomsStatus(){
	//alert(vat.item.getValueByName("#F.customsStatusHidden"));
	var statusHidden = vat.item.getValueByName("#F.customsStatusHidden");
	var cusStatusHidden = statusHidden.substring(0, 1);
	if(cusStatusHidden !== ""){
	 	if(cusStatusHidden == "A"){
	 		vat.item.setValueByName("#F.customsStatus", "待上傳");
	 	}
	 	if(cusStatusHidden == "N"){
	 		vat.item.setValueByName("#F.customsStatus", "已上傳成功");
	 	}
	 	if(cusStatusHidden == "E"){
	 		vat.item.setValueByName("#F.customsStatus", "上傳錯誤");
	 	}
	 }else{
	 	vat.item.setValueByName("#F.customsStatus", "未上傳");
	 }
}

function getCustomsDesc(){
	//alert(vat.item.getValueByName("#F.customsStatusHidden"));
	var customsDesc = "";
	if(vat.item.getValueByName("#F.customsStatusHidden")!==""){
		vat.ajax.XHRequest({
          post:"process_object_name=imMovementMainService"+
                   "&process_object_method_name=getCustomsProcessResponse"+
                   "&customsStatus=" + vat.item.getValueByName("#F.customsStatusHidden"),
          find: function change(oXHR){
          		//alert("success");
          		var response = vat.ajax.getValue("response" , oXHR.responseText);
          		vat.item.setValueByName("#F.customsDesc", response);
          		//alert(response);
          },
          fail: function changeError(){
          		//alert("fail");
          }
		});
	}else{
		//alert("NULL");
	}
}

function checkUploadControl(){
	//alert("checkUploadControl");
	var status = vat.item.getValueByName("#F.status");
	var tranRecordStatus = vat.item.getValueByName("#F.tranRecordStatus");
	var orderTypeCode = vat.item.getValueByName("#F.orderType");
	var customsStatus = vat.item.getValueByName("#F.customsStatusHidden");
	var tranAllowUpload = vat.item.getValueByName("#F.tranAllowUpload");
	var cStatus = customsStatus.substring(0, 1);
	var RecordStatus = tranRecordStatus.substring(0, 5);
	//alert("start check status");
	if((status === "WAIT_IN" || status === "FINISH") && RecordStatus !== "DF15" && (customsStatus === "" || customsStatus === "N18" || cStatus === "E")){
		//alert("1");
		vat.item.setStyleByName("#B.sendCustoms","display","inline");
	}else if(status === "FINISH" && ((cStatus === "N" && tranRecordStatus !== "DF15") || (cStatus === "E" && tranRecordStatus === "DF15")) && (orderTypeCode == "RSF" || orderTypeCode == "RMK")){
		//alert("2");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendBack","display","inline");
	}else if(status === "FINISH" && ((cStatus === "N" && tranAllowUpload === "") || (cStatus === "E" && tranAllowUpload === "E"))&& tranRecordStatus !== "DF10"){
		vat.item.setStyleByName("#B.sendCancel","display","inline");
		vat.item.setStyleByName("#B.sendBack","display","none");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
	}else if(orderTypeCode === "MEF" || orderTypeCode === "MEG" ){
		vat.item.setStyleByName("#B.sendCancel","display","inline");
		vat.item.setStyleByName("#B.sendCustoms","display","inline");
	}else if(orderTypeCode === "MDF" || orderTypeCode === "MDP" ){ //關閉沒用button
		vat.item.setStyleByName("#B.submitBG","display","none");
		vat.item.setStyleByName("#B.barcode","display","none");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendBack","display","none");
		vat.item.setStyleByName("#B.sendCancel","display","none");
	}else{
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendBack","display","none");
		vat.item.setStyleByName("#B.sendCancel","display","none");
		//vat.item.setValueByName("#F.customsStatus", "");
	}
	//alert("end check status");
	//vat.item.setStyleByName("#B.sendCustoms","display","inline");
	//vat.item.setStyleByName("#B.sendCustoms","display","none");
}

function  buttonLine(){
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
	 									 service:"Im_GeneralAdjustment:search:20091026.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 servicePassData:function(){ return doPassData("buttonLine");},   
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"#B.save2"	   , type:"IMG"    ,value:"存檔",   src:"./images/button_save_data.gif", eClick:'doSubmit("ARCHIVE")'},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:"openReportWindow()"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"#B.declaration" , type:"IMG"    ,value:"核銷報單",   src:"./images/button_declaration.gif" , eClick:'execExtendItemInfo()'},
	 			{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
	 			{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
				//for 儲位用
	 			{name:"#B.storageExport", 	type:"IMG"    ,value:"儲位匯出",   src:"./images/button_storage_export.gif" , eClick:'exportStorageFormData()'},
	 			{name:"#B.storageImport",	type:"PICKER" , value:"儲位匯入",  src:"./images/button_storage_import.gif"  , 
						 openMode:"open", 
						 service:"/erp/fileUpload:standard:2.page",
						 servicePassData:function(x){ return importStorageFormData(); },
						 left:0, right:0, width:600, height:400,	
						 serviceAfterPick:function(){}},
	 			{name:"#B.barcode"     , type:"IMG"    ,value:"列印條碼",  src:"./images/button_barcode_print.gif",  eClick:'doBarcode()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendCustoms" 	   , type:"IMG"    ,value:"上傳海關",   src:"./images/send_customs1.jpg", eClick:'updateCustomsStatus("E")'},
	 			{name:"#B.sendCancel" 	   , type:"IMG"    ,value:"註銷上傳",   src:"./images/send_customs3.jpg", eClick:'updateCustomsStatus("cancel")'},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
//	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

// 調整單主檔
function headerInitial(){ 
	var allTaxTypes = vat.bean("allTaxTypes");
	var allOrderTypes = vat.bean("allOrderTypes");
	var allAdjustmentTypes = vat.bean("allAdjustmentTypes");
	var allWarehouseCodes = vat.bean("allWarehouseCodes");
	
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var isMostWrite = "<font color='red'>*</font>";
	if( brandCode.indexOf("T2") <= -1 ){
		isMostWrite = "";
	}
	
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"一般調整功能維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 		type:"LABEL", 	value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 		type:"SELECT", 	bind:"orderTypeCode", init:allOrderTypes, size:15, mode:"READONLY"},
	 		 			{name:"#F.orderType"   , type:"TEXT"  ,  bind:"orderTypeCode", size:6, mode:"HIDDEN"},
						{name:"#F.transport", 				type:"TEXT",  	bind:"transport", mode:"HIDDEN"}]},
				{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"調整單號" }]},
				{items:[{name:"#F.orderNo", 			type:"TEXT", 	bind:"orderNo", size:20, mode:"READONLY"},
						{name:"#F.headId", 				type:"TEXT",	bind:"headId", back:false, mode:"READONLY"}
		 		 		//for 儲位用
						,{name:"#F.storageHeadId",   	type:"TEXT",	bind:"storageHeadId", back:false, mode:"READONLY" }
		 				]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", mode:"HIDDEN"},
	 					{name:"#F.brandName", 			type:"TEXT",  	bind:"brandName", size:8, mode:"READONLY"}]},
				{items:[{name:"#L.status", 				type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"TEXT", 	bind:"status", size:15, mode:"HIDDEN"},
						{name:"#F.statusName", 				type:"TEXT",  bind:"statusName", back:false, mode:"READONLY"},
						{name:"#F.customsDesc"      , type:"TEXT"  , size:35 , mode:"READONLY"},
					    {name:"SPACE"          , type:"LABEL"  ,value:"　"},
	  		 			{name:"#F.tranRecordStatus"  , type:"TEXT"  ,  bind:"tranRecordStatus", mode:"HIDDEN"},
	  		 			{name:"#F.tranAllowUpload"  , type:"TEXT"  ,  bind:"tranAllowUpload", mode:"HIDDEN"},
	  		 			{name:"#F.customsStatus", 				type:"TEXT", mode:"READONLY"},
	  		 			{name:"#F.customsStatusHidden", 				type:"TEXT",  bind:"customsStatus", mode:"HIDDEN"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.taxType", 			type:"LABEL", 	value:"稅別"}]},	 
	 			{items:[{name:"#F.taxType", 			type:"SELECT",  bind:"taxType",  init:allTaxTypes,	size:15, mode:"READONLY" }]}, 
	 			{items:[{name:"#L.adjustmentType",		type:"LABEL", 	value:"調整類別"+isMostWrite }]}, 
				{items:[{name:"#F.adjustmentType",		type:"SELECT", 	bind:"adjustmentType" , init:allAdjustmentTypes , eChange:"changeAdjustmentType()" },
						{name:"#F.unBlockOnHand",		type:"CHECKBOX",bind:"unBlockOnHand", eClick:"checkBox()", mode:"READONLY"},
						{name:"#L.unBlockOnHandLabel",	type:"LABEL", 	value:"自動解鎖報單"}]},  
				{items:[{name:"#L.createBy",			type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",			type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",			type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.creationDate",		type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",		type:"TEXT", 	bind:"creationDate", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.remark1",				type:"LABEL", 	value:"備註" }]},
				{items:[{name:"#F.remark1",				type:"TEXT", 	bind:"remark1", maxLen:40, size:40 }]},
				{items:[{name:"#L.adjustmentDate", 	type:"LABEL", 	value:"調整日期<font color='red'>*</font>" }]},
				{items:[{name:"#F.adjustmentDate", 	type:"Date", 	bind:"adjustmentDate", size:15 }]},
				{items:[{name:"#L.isAdjustCost", 			type:"LABEL", 	value:"影響成本"}]},	 
	 			{items:[{name:"#F.isAdjustCostCheckBox", 			type:"CHECKBOX",  bind:"isAdjustCostCheckBox",  back:false,	size:1, eClick:"changeAmountReadOnly()" },
	 					{name:"#H.isAdjustCost", 			type:"TEXT",  bind:"affectCost", mode:"HIDDEN"  }]}, 
	 			{items:[{name:"#L.defaultWarehouseCode",	type:"LABEL", 	value:"庫別<font color='red'>*</font>" }]}, 
				{items:[{name:"#F.defaultWarehouseCode",	type:"SELECT", 	bind:"defaultWarehouseCode", init:allWarehouseCodes, eChange:"changeWarehouseCode()" }]}
				
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

// 一般調撥單主檔2
function masterInitial(){
	var allDeclarationTypes =  vat.bean("allDeclTypes");;
	var allDistricts = vat.bean("allDistricts");
	var allSourceOrderTypeCodes = vat.bean("allSourceOrderTypeCodes");
	
	var adjustmentType = vat.item.getValueByName("#F.adjustmentType");
	var mustWrite = "";
	if("22" == adjustmentType){
		mustWrite = "<font color='red'>*</font>";
	}
	//MDF,MDP
	if(vat.item.getValueByName("#orderTypeCode") == "MDF" || vat.item.getValueByName("#orderTypeCode") == "MDP"){
	
	vat.block.create( vnB_Master, {
		id: "vatMasterDiv", table:"cellspacing='1' class='default' border='0' cellpadding='3'",
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.district",			type:"LABEL", 	value:"區隔" }]}, 
				{items:[{name:"#F.district",			type:"SELECT", 	bind:"district", init:allDistricts, mode:"READONLY" }]},
				{items:[{name:"#L.boxQty",				type:"LABEL", 	value:"總箱數" }]}, 
				{items:[{name:"#F.boxQty",				type:"NUMB", 	bind:"boxQty", maxLen:8 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.sourceOrderTypeCode",	type:"LABEL", 	value:"來源單別" }]}, 
				{items:[{name:"#F.sourceOrderTypeCode",	type:"SELECT", 	bind:"sourceOrderTypeCode", init:allSourceOrderTypeCodes, mode:"READONLY" }]},
				{items:[{name:"#L.declarationType",		type:"LABEL", 	value:"報關類別" }]}, 
				{items:[{name:"#F.declarationType",		type:"SELECT", 	bind:"declarationType", init:allDeclarationTypes, size:15, mode:"READONLY" }], td:" colSpan=3"}
			]}
		], 	 
		 
		beginService:"",
		closeService:""			
	});
	}else{
	vat.block.create( vnB_Master, {
		id: "vatMasterDiv", table:"cellspacing='1' class='default' border='0' cellpadding='3'",
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.district",			type:"LABEL", 	value:"區隔" }]}, 
				{items:[{name:"#F.district",			type:"SELECT", 	bind:"district", init:allDistricts, mode:"READONLY" }]},
				{items:[{name:"#L.boxQty",				type:"LABEL", 	value:"總箱數" }]}, 
				{items:[{name:"#F.boxQty",				type:"NUMB", 	bind:"boxQty", maxLen:8 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.sourceOrderTypeCode",	type:"LABEL", 	value:"來源單別" }]}, 
				{items:[{name:"#F.sourceOrderTypeCode",	type:"SELECT", 	bind:"sourceOrderTypeCode", init:allSourceOrderTypeCodes, mode:"READONLY" }]},
				{items:[{name:"#L.sourceOrderNo",	type:"LABEL", 	value:"來源單號"+mustWrite }]}, 
				{items:[{name:"#F.sourceOrderNo",	type:"TEXT", 	bind:"sourceOrderNo", eChange:"changeCmDeclarationData()", size:12, maxLen:12 }, // , eChange:"changeLines()"
						{name:"#F.sourceOrderTypeNoMemo",	type:"TEXT", 	bind:"sourceOrderTypeNoMemo", back:false, size:15, mode:"READONLY"  }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.declarationNo",		type:"LABEL", 	value:"報關單號"+mustWrite }]},
				{items:[{name:"#F.declarationNo",		type:"TEXT", 	bind:"declarationNo", eChange:"changeCmDeclarationData()", size:15 }, 
						{name:"#F.declarationNoMemo",	type:"TEXT", 	bind:"declarationNoMemo", back:false, size:15, mode:"READONLY"  }]},
				{items:[{name:"#L.declarationDate",		type:"LABEL", 	value:"報關日期" }]}, 
				{items:[{name:"#F.declarationDate",		type:"Date", 	bind:"declarationDate", size:15, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.declarationType",		type:"LABEL", 	value:"報關類別" }]}, 
				{items:[{name:"#F.declarationType",		type:"SELECT", 	bind:"declarationType", init:allDeclarationTypes, size:15, mode:"READONLY" }], td:" colSpan=3"}
			]}
		], 	 
		 
		beginService:"",
		closeService:""			
	});
	}
}

// 調整單明細
function detailInitial(){
	var status = vat.item.getValueByName("#F.status");
	var taxType 	= vat.item.getValueByName("#F.taxType"); 
		
	var taxTypeMode = "";
	var shiftMode = "";
	if( "P" == taxType){
		taxTypeMode = "HIDDEN";
		shiftMode = "";
	}else if( "F" == taxType ){
		taxTypeMode = "";
		shiftMode = "shift";
	}
	vat.item.make(vnB_Detail, "indexNo"				, {type:"IDX", 	view:"fixed", desc:"序號" });
	vat.item.make(vnB_Detail, "itemCode"			, {type:"TEXT", view:"fixed", size:20, maxLen:23, desc:"品號", eChange:"changeLineData()"          	});
	vat.item.make(vnB_Detail, "searchItem2"	        , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 		            	service:"Im_OnHand:search:20091224.page",
	 									 			            left:0, right:0, width:1024, height:768,	
	 									 						servicePassData:function(x){ return doPassData( "itemCode", x ); },	
	 									 						serviceBeforePick:function(){ return doBeforePick("itemCode"); },	
	 									 		                serviceAfterPick:function(id){doAfterPickerFunctionProcess("itemCode",id); } });
	
	vat.item.make(vnB_Detail, "itemCName"			, {type:"TEXT", view:"" , size:20, desc:"品名", mode:"READONLY"    	});
	vat.item.make(vnB_Detail, "lotNo"				, {type:"TEXT", view:"" , size:12, maxLen:20, desc:"批號"   			});
	
	vat.item.make(vnB_Detail, "localUnitCost"		, {type:"NUMB", view:"" , size:10, desc:"售價", mode:"READONLY"     	});
	vat.item.make(vnB_Detail, "amount"				, {type:"NUMB", view:"" , size:10, desc:"總成本"     	});
	
	vat.item.make(vnB_Detail, "warehouseCode"		, {type:"TEXT", view:"" , desc:"庫別", mode:"READONLY"     	 });
	vat.item.make(vnB_Detail, "difQuantity"			, {type:"NUMB", view:"" , desc:"數量" }); // quantity
	
	vat.item.make(vnB_Detail, "originalDeclarationNo"	, {type:"TEXT", view:shiftMode , desc:"報單單號", size:15, eChange:"changeLineData()" , mode:taxTypeMode });
	vat.item.make(vnB_Detail, "searchItem"			, {type:"PICKER", view:shiftMode , desc:"", openMode:"open", src:"./images/start_node_16.gif", mode:taxTypeMode,  
	 									 			service:"Cm_DeclarationOnHand:search:20091103.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			servicePassData:function(x){ return doPassData("detailLine",x); },
	 									 			serviceBeforePick:function(){ return doBeforePick("originalDeclarationNo"); },
	 									 			serviceAfterPick:function(id){doAfterPickerFunctionProcess("detailLine",id); } }); 
	
	vat.item.make(vnB_Detail, "originalDeclarationSeq"	, {type:"NUMB", view:shiftMode , desc:"報單項次", eChange:"changeLineData()" , mode:taxTypeMode});
	vat.item.make(vnB_Detail, "originalDeclarationDate"	, {type:"DATE", view:shiftMode , desc:"報單日期", mode:taxTypeMode});
	vat.item.make(vnB_Detail, "boxNo"					, {type:"TEXT", view:shiftMode , desc:"箱號", maxLen:10 , mode:taxTypeMode});
	vat.item.make(vnB_Detail, "weight"					, {type:"NUMB", view:shiftMode , desc:"重量" , mode:taxTypeMode});
	
	vat.item.make(vnB_Detail, "customsItemCode"	, {type:"TEXT", view:shiftMode , desc:"海關品號", size:20 ,mode:taxTypeMode }); // , mode:"READONLY"
	
	vat.item.make(vnB_Detail, "specDesc"	        , {type:"TEXT" , view:shiftMode, size:25, desc:"規格" });
	vat.item.make(vnB_Detail, "unit"	        	, {type:"TEXT" , view:shiftMode, size:3, maxLen:3, desc:"單位" });
	vat.item.make(vnB_Detail, "orgMoveWhNo"	        , {type:"TEXT" , view:shiftMode, size:15, maxLen:14, desc:"移倉申請書號碼" });
	vat.item.make(vnB_Detail, "orgMoveWhItemNo"	        , {type:"NUMB" , view:shiftMode, desc:"移倉申請書項次" });
	
	vat.item.make(vnB_Detail, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_Detail, "isDeleteRecord"  , {type:"DEL"  , view:"fixed", desc:"刪除"});
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

// 金額統計
function amountInitial(){
	vat.block.create(vnB_Amount, {
		id: "vatAmountDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[  
			{row_style:"", cols:[
				{items:[{name:"#L.totalQuantity", 		type:"LABEL", 	value:"數量總額"}]},
				{items:[{name:"#F.totalQuantity", 		type:"NUMM", 	bind:"totalQuantity" ,size:15, back:false,mode:"READONLY" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalAmount", 	type:"LABEL", 	value:"總成本總額"}]},
				{items:[{name:"#F.totalAmount", 	type:"NUMM", 	bind:"totalAmount" ,size:15, back:false,mode:"READONLY" }]}
			]}
	 	],
		beginService:"",
		closeService:""			
	});
	
}

// 第一次載入 重新整理
function loadBeforeAjxService(div){
//    alert(div);	
	if( vnB_Detail === div ){
		var processString = "process_object_name=imGeneralAdjustmentService&process_object_method_name=getAJAXPageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
		                    "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") +  
		                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")+
		                    "&taxType=" + vat.item.getValueByName("#F.taxType");
		return processString;	
	}							
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
//    alert("saveBeforeAjxService"); 

	if( vnB_Detail === div ){
		var processString = "process_object_name=imGeneralAdjustmentService&process_object_method_name=updateAJAXPageLinesData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value+
		                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")+
		                    "&adjustmentType=" + vat.item.getValueByName("#F.adjustmentType")+
		                    "&status=" + vat.item.getValueByName("#F.status")+
		                    "&userType=" + vat.bean().vatBeanOther.userType;
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
//		vat.block.pageDataSave(vnB_Detail); //存檔vnB_Detail 
	}else if(vnB_Detail===div){
//		vat.block.pageDataSave(vnB_Master); //存檔vnB_Master
	}else if(vnB_Amount===div){
		vat.block.pageDataSave( vnB_Detail ,{ 
			funcSuccess:function(){
				getTotal();
			}
		});		
	}
}
	
// 金額統計
function getTotal(){
	vat.ajax.XHRequest({
           post:"process_object_name=imGeneralAdjustmentService"+
                    "&process_object_method_name=getAJAXCountTotalAmount"+
                    "&headId=" + vat.item.getValueByName("#F.headId"), 
           find: function change(oXHR){ 
           		vat.item.setValueByName("#F.totalQuantity", vat.ajax.getValue("totalQuantity", oXHR.responseText) );
           		vat.item.setValueByName("#F.totalAmount", vat.ajax.getValue("totalAmount", oXHR.responseText) );
           },
           fail: function changeError(){
          		vat.item.setValueByName("#F.totalQuantity", "0.0" );
           		vat.item.setValueByName("#F.totalAmount", "0.0" );
           }   
    });
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
function doPassData(id, x){
	var suffix = "";
	var taxType = vat.item.getValueByName("#F.taxType");
	var adjustmentType = vat.item.getValueByName("#F.adjustmentType");	
	switch(id){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
//			alert("adjustmentType = " + adjustmentType);
			suffix += "&orderTypeCode="+escape(orderTypeCode)+"&taxType="+escape(taxType)+"&adjustmentType="+escape(adjustmentType); 
			break;
		case "itemCode":
		  var vLineId	      = vat.item.getGridLine(x);
		  var vItemCode       = vat.item.getGridValueByName("itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
		  var vLotNo          = vat.item.getGridValueByName("lotNo"   , vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
		  var vWarehouseCode  = vat.item.getValueByName("#F.defaultWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
//		  var vItemCategory   = vat.item.getValueByName("#F.itemCategory").replace(/^\s+|\s+$/, '').toUpperCase();
		  //alert("LineId:"+vLineId);
		  suffix += "&taxType="+escape(taxType)+
		            "&startItemCode="+escape(vItemCode)+
		            "&endItemCode="+escape(vItemCode)+
		            "&startWarehouseCode="+escape(vWarehouseCode)+
		            "&endWarehouseCode="+escape(vWarehouseCode)+
		            "&startLotNo="+escape(vLotNo)+
		            "&endLotNo="+escape(vLotNo)+
		            "&isReadOnlyControl=Y" + 
		            "&showZero=N";
//		            "&itemCategory="+escape(vItemCategory);
			break;	
		case "detailLine":
			var vLineId	= vat.item.getGridLine(x);
			
			var warehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
			var itemCode = vat.item.getGridValueByName("itemCode", vLineId);
			var originalDeclarationNo = vat.item.getGridValueByName("originalDeclarationNo", vLineId);
			var originalDeclarationSeq = vat.item.getGridValueByName("originalDeclarationSeq", vLineId);
			
			suffix += "&warehouseCode="+escape(warehouseCode)+"&taxType="+escape(taxType)+"&adjustmentType="+escape(adjustmentType)+"&customsItemCode="+escape(itemCode)+"&declarationNo="+escape(originalDeclarationNo)+"&declarationSeqStart="+originalDeclarationSeq+"&declarationSeqEnd="+originalDeclarationSeq;
			break;
	}
//	alert(suffix);
	return suffix;
}

// 選取picker前的click
function doBeforePick(id){
	// 單頭庫別與調整類別
	var adjustmentType = vat.item.getValueByName("#F.adjustmentType");
	var warehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
	var brandCode = vat.item.getValueByName("#F.brandCode");
	switch(id){
		case "itemCode":
		case "originalDeclarationNo":
			if("T2" == brandCode ){
			
				if( adjustmentType == ""   ){
					alert("請選擇調整類別");
					return false;
				}
			}	
			if( warehouseCode == "" ){
				alert("請選擇庫別");
				return false;
			}
			break;
	}
	return true;
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
	
		// 避免暫存後執行流程時不知道跑哪個流程，
		// 與權限控制判斷調整類別被鎖定是互相搭配
		var brandCode = vat.item.getValueByName("#F.brandCode");
		if( brandCode.indexOf("T2") > -1 ){
			var adjustmentType = vat.item.getValueByName("#F.adjustmentType");
			if("" == adjustmentType || null == adjustmentType){
				return alert("請輸入調整類別");
			}
		}
	
		alertMessage = "是否確定暫存?";
	}else if("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}else if("ARCHIVE" == formAction){
		var userType = vat.bean().vatBeanOther.userType;
		if("CREATOR"== userType){
			alertMessage = "是否確定存檔?";
		}else{
			alertMessage = "您無此權限";
		}
	}
	if(confirm(alertMessage)){
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var approvalResult        = getApprovalResult(); 
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
		var orderNoPrefix         = vat.item.getValueByName("#F.orderNo").substring(0,3);
	    
/*	    var formStatus = status;
		if("SAVE" == formAction){
	        formStatus = "SAVE";
	    }else if("SUBMIT" == formAction || "SUBMIT_BG" == formAction ){
	        formStatus = changeFormStatus(formId, processId, status, formAction);
	    }else if("VOID" == formAction){
	        formStatus = "VOID";
	    }*/
//	    alert( "approvalResult = " + approvalResult + "\napprovalComment = " + approvalComment );
	    
		if(( orderNoPrefix == "TMP" &&  status == "SAVE" ) || 
		(inProcessing   && (status == "SAVE" || status == "REJECT" || status == "FINISH" || status == "SIGNING" )) ){
		
			vat.block.pageDataSave( vnB_Detail ,{ 
				funcSuccess:function(){
					vat.bean().vatBeanOther.formAction 		= formAction;
		  			vat.bean().vatBeanOther.beforeStatus	= status;	
		  			vat.bean().vatBeanOther.processId       = processId;
	  				vat.bean().vatBeanOther.approvalResult  = approvalResult;
	  				vat.bean().vatBeanOther.approvalComment =  approvalComment;
					
				    if("SUBMIT_BG" == formAction){
				      	vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentAction"+
				                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
				    }else{
						vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentAction"+
				                    "&process_object_method_name=performTransaction";},{
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
	var taxType				= vat.item.getValueByName("#F.taxType");
	var adjustmentType		= vat.item.getValueByName("#F.adjustmentType");
	
	if(taxType == "F" && (adjustmentType == "21" ) ){ // 出倉
		if(vat.item.getValueByName("#F.status") == "SIGNING"){
			return vat.item.getValueByName("#F.approvalResult").toString();
		}else{
			return "true";
		}
	}else{
		return "true";
	}
}

// 動態改變一筆商品資料 onHand 和 cmOnHand
function changeLineData( pickerLine ){
	
	var brandCode			= vat.item.getValueByName("#F.brandCode");
	var taxType				= vat.item.getValueByName("#F.taxType");
	var adjustmentType		= vat.item.getValueByName("#F.adjustmentType");
	var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
	
	var vLineId				= typeof pickerLine === "undefined" ? vat.item.getGridLine() : pickerLine ;
	var itemCode			= vat.item.getGridValueByName("itemCode", vLineId);
	var originalDeclarationNo	= vat.item.getGridValueByName("originalDeclarationNo", vLineId);
	var originalDeclarationSeq	= vat.item.getGridValueByName("originalDeclarationSeq", vLineId);
	
	if( ("T2"!= brandCode && defaultWarehouseCode == "" ) || ( "T2" == brandCode && (defaultWarehouseCode == "" || adjustmentType == "") ) ){
		vat.item.setGridValueByName("itemCode", vLineId, "" );
		vat.item.setGridValueByName("itemCName", vLineId, "");
		vat.item.setGridValueByName("originalDeclarationNo", vLineId, "" );
		vat.item.setGridValueByName("originalDeclarationSeq", vLineId, "" );
		vat.item.setGridValueByName("warehouseCode", vLineId, "");
		vat.item.setGridValueByName("customsItemCode", vLineId, "");
		vat.item.setGridValueByName("localUnitCost", vLineId, "0.0");
		
		if("T2" == brandCode && adjustmentType == "" ){
			return alert("請選擇調整類別");
		}
		if( defaultWarehouseCode == "" ){
			return alert("請選擇庫別");
		}
	}else{
		if( isRunAjax(taxType,itemCode,originalDeclarationNo,originalDeclarationSeq) ){
			vat.ajax.XHRequest(
		       {
		           post:"process_object_name=imGeneralAdjustmentService"+
		                    "&process_object_method_name=getAJAXImItem"+
		                    "&brandCode=" + brandCode +
		                    "&taxType=" + taxType +
		                    "&adjustmentType=" + adjustmentType +
		                    "&itemCode=" + itemCode +
		                    "&originalDeclarationNo=" + originalDeclarationNo +
		                    "&originalDeclarationSeq=" + originalDeclarationSeq +
		                    "&defaultWarehouseCode=" + defaultWarehouseCode,
//	                    	"&declarationNo=" + declarationNo,
		           find: function change(oXHR){ 
		           		vat.item.setGridValueByName("itemCode", vLineId, vat.ajax.getValue("itemCode", oXHR.responseText));
		          		vat.item.setGridValueByName("itemCName", vLineId, vat.ajax.getValue("itemCName", oXHR.responseText));
		          		vat.item.setGridValueByName("lotNo", vLineId, vat.ajax.getValue("lotNo", oXHR.responseText));
		          		vat.item.setGridValueByName("localUnitCost", vLineId, vat.ajax.getValue("localUnitCost", oXHR.responseText));
//						vat.item.setGridValueByName("amount", vLineId, vat.ajax.getValue("amount", oXHR.responseText));
						vat.item.setGridValueByName("warehouseCode", vLineId, vat.ajax.getValue("warehouseCode", oXHR.responseText));
//						vat.item.setGridValueByName("difQuantity", vLineId, vat.ajax.getValue("difQuantity", oXHR.responseText));
						vat.item.setGridValueByName("originalDeclarationNo", vLineId, vat.ajax.getValue("originalDeclarationNo", oXHR.responseText));
						vat.item.setGridValueByName("originalDeclarationSeq", vLineId, vat.ajax.getValue("originalDeclarationSeq", oXHR.responseText));
//						vat.item.setGridValueByName("boxNo", vLineId, vat.ajax.getValue("boxNo", oXHR.responseText));
//						vat.item.setGridValueByName("weight", vLineId, vat.ajax.getValue("weight", oXHR.responseText));
						vat.item.setGridValueByName("customsItemCode", vLineId, vat.ajax.getValue("customsItemCode", oXHR.responseText));
		           },
		           fail: function changeError(){
		          		vat.item.setGridValueByName("itemCode", vLineId, "");
				     	vat.item.setGridValueByName("itemCName", vLineId, "");
				     	vat.item.setGridValueByName("lotNo", vLineId, "");
				     	vat.item.setGridValueByName("localUnitCost", vLineId, "0.0");
						vat.item.setGridValueByName("amount", vLineId, "0.0");
						vat.item.setGridValueByName("warehouseCode", vLineId, "");
						vat.item.setGridValueByName("difQuantity", vLineId, "0");
						vat.item.setGridValueByName("originalDeclarationNo", vLineId, "");
						vat.item.setGridValueByName("originalDeclarationSeq", vLineId, "");
						vat.item.setGridValueByName("boxNo", vLineId, "");
						vat.item.setGridValueByName("weight", vLineId, "0.0");
						vat.item.setGridValueByName("customsItemCode", vLineId, "");
		           }   
		       });
		  }else{
		  	changeItemMessage(vLineId,taxType,itemCode,defaultWarehouseCode,originalDeclarationNo,originalDeclarationSeq);
		  }
	}  

}

// 依稅別判別是否跑ajax
function isRunAjax(taxType,itemCode,originalDeclarationNo,originalDeclarationSeq ){
	if(taxType == "F"){
		return itemCode != "" || (originalDeclarationNo != "" && originalDeclarationSeq != ""); 
	}else{
		return itemCode != "";
	}
}
// 改變訊息
function changeItemMessage(vLineId,taxType,itemCode,defaultWarehouseCode,originalDeclarationNo,originalDeclarationSeq){
	if(taxType == "F"){
		if( itemCode != "" || originalDeclarationNo != "" || originalDeclarationSeq != "" ){
	  		if( originalDeclarationNo == "" && originalDeclarationSeq != "" ){
		     	vat.item.setGridValueByName("itemCName", vLineId, "請輸入報關單");
	  		}else if( originalDeclarationSeq == "" && originalDeclarationNo != "" ){
		     	vat.item.setGridValueByName("itemCName", vLineId, "請輸入報單項次");
	  		}else{
		     	vat.item.setGridValueByName("itemCName", vLineId, "請輸入報關單");
	  		}
	  	}else{
	     	vat.item.setGridValueByName("itemCName", vLineId, "");
	     	vat.item.setGridValueByName("lotNo", vLineId, "");
	     	vat.item.setGridValueByName("warehouseCode", vLineId, "");
	  	}
	  	vat.item.setGridValueByName("itemCode", vLineId, itemCode);
//  	vat.item.setGridValueByName("localUnitCost", vLineId, "0.0");
//  	vat.item.setGridValueByName("amount", vLineId, "0.0");
//		vat.item.setGridValueByName("warehouseCode", vLineId, defaultWarehouseCode);
//		vat.item.setGridValueByName("difQuantity", vLineId, "0");
		vat.item.setGridValueByName("originalDeclarationNo", vLineId, originalDeclarationNo);
		vat.item.setGridValueByName("originalDeclarationSeq", vLineId, originalDeclarationSeq);
//		vat.item.setGridValueByName("boxNo", vLineId, "");
//		vat.item.setGridValueByName("weight", vLineId, "0.0");
//		vat.item.setGridValueByName("customsItemCode", vLineId, "");
	}else{
		vat.item.setGridValueByName("itemCName", vLineId, "");
		vat.item.setGridValueByName("localUnitCost", vLineId, "0.0");
		vat.item.setGridValueByName("warehouseCode", vLineId, "");
	}
}

// 改變庫別
function changeWarehouseCode(){
	vat.block.pageDataSave( vnB_Detail ,{ 
		funcSuccess:function(){
			vat.ajax.XHRequest({
	        	post:"process_object_name=imGeneralAdjustmentService"+
	                    "&process_object_method_name=updateAJAXWarehouseCode" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode"), 
	       		find: function change(oXHR){ 
	       			changeCmDeclarationData();
					vat.block.pageRefresh(vnB_Detail);
	        	},
	       			fail: function changeError(){
	        	}   
			});
		}
    });
}
// 改變調整類別
function changeAdjustmentType(){
	var adjustmentType		= vat.item.getValueByName("#F.adjustmentType");
	var taxType				= vat.item.getValueByName("#F.taxType");
	var brandCode 			= vat.bean().vatBeanOther.loginBrandCode;
	var orderTypeCode 		= vat.item.getValueByName("#F.orderTypeCode");
	if( adjustmentType == "" ){
		vat.item.setValueByName("#H.isAdjustCost", "N"); 
		vat.item.setValueByName("#F.isAdjustCostCheckBox", "N"); 
		vat.item.setStyleByName("#B.declaration", 	"display", "none");
		
		return;
	}else{
		if(adjustmentType == "21" && taxType == "F" ){
			if(orderTypeCode == "MDF"){
				vat.item.setStyleByName("#B.declaration", 	"display", "inline");	// 核銷報單
				
			}else{
				vat.item.setStyleByName("#B.declaration", 	"display", "inline");	// 核銷報單
				vat.item.setValueByName("#F.unBlockOnHand", "Y"); //D7調整單自動解鎖報單
			}
		}else if(adjustmentType == "22" && taxType == "F" ){
			if(orderTypeCode == "MDF"){
				vat.item.setStyleByName("#B.declaration", 	"display", "none");	// 核銷報單
				
			}else{
				vat.item.setStyleByName("#B.declaration", 	"display", "none");	// 核銷報單
				vat.item.setValueByName("#F.unBlockOnHand", "Y"); //D7調整單自動解鎖報單
	
			}
			vat.item.setStyleByName("#B.declaration", 	"display", "none");	// 核銷報單


		}
	}
		
	changeAmountReadOnly(); // 是否鎖line的總成本
	
	if("T2" == brandCode){
		changeWarehouse();	// 連動庫別
	}
	changeCmDeclarationData();
	
	if("21" == adjustmentType){			// 出倉
		vat.block.canGridModify([vnB_Detail], true,true,true);
		vat.item.setGridAttributeByName("itemCode", "readOnly", false); 
		vat.item.setGridAttributeByName("searchItem2", "readOnly", false);
	}else if ("22" == adjustmentType){	// 入倉
		vat.block.canGridModify([vnB_Detail], true,false,false);
		if(taxType == "F")
			vat.item.setGridAttributeByName("itemCode", "readOnly", false); 
		vat.item.setGridAttributeByName("searchItem2", "readOnly", true); 
	}
}

// 將有關的欄位替換
function changeCmDeclarationData(){
	var adjustmentType	= vat.item.getValueByName("#F.adjustmentType");
	var sourceOrderNo = vat.item.getValueByName("#F.sourceOrderNo").replace(/^\s+|\s+$/, '').toUpperCase();
	var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var declarationNo = vat.item.getValueByName("#F.declarationNo").replace(/^\s+|\s+$/, '').toUpperCase();
	var declarationType = vat.item.getValueByName("#F.declarationType");
	var taxCode = vat.item.getValueByName("#F.taxType");
	var status = vat.item.getValueByName("#F.status");
	var userType = vat.bean().vatBeanOther.userType;
	
	// 缺檢核來源單號,來源單別, 報關單號,報關類別
	if( "F" == taxCode &&  "" != adjustmentType && "" != defaultWarehouseCode && ( "SAVE" == status && "22" == adjustmentType && "" != declarationNo && "" != declarationType )
		|| ( "CREATOR" == userType && ("SIGNING" == status || "SAVE" == status || "REJECT" == status ) &&  "21" == adjustmentType && "" != declarationNo && "" != declarationType) ){
		vat.ajax.XHRequest({
           post:"process_object_name=imGeneralAdjustmentService"+
                    "&process_object_method_name=updateAJAXByD7CmDeclarationData"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&sourceOrderNo=" + sourceOrderNo + 
                    "&sourceOrderTypeCode=" + vat.item.getValueByName("#F.sourceOrderTypeCode") +
                    "&defaultWarehouseCode=" + defaultWarehouseCode + 
                    "&declarationNo=" + declarationNo +
                    "&declType=" + declarationType +
                    "&headId=" + vat.item.getValueByName("#F.headId")+
                    "&adjustmentType=" + adjustmentType, 
           find: function change(oXHR){ 
           		vat.item.setValueByName("#F.declarationNo", vat.ajax.getValue("declarationNo", oXHR.responseText) );
           		vat.item.setValueByName("#F.declarationNoMemo", vat.ajax.getValue("declarationNoMemo", oXHR.responseText) );
           		vat.item.setValueByName("#F.declarationDate", vat.ajax.getValue("declarationDate", oXHR.responseText) );
           		vat.item.setValueByName("#F.adjustmentDate", vat.ajax.getValue("declarationDate", oXHR.responseText) );
           		vat.block.pageRefresh(vnB_Detail);
           },
           fail: function changeError(){
          		vat.item.setValueByName("#F.declarationNo", sourceOrderNo );
           		vat.item.setValueByName("#F.declarationNoMemo", "查無此報關單" );
           		vat.item.setValueByName("#F.declarationDate", "" );
           }   
		});
    }else{
    	if("" == declarationNo){
    		vat.item.setValueByName("#F.declarationNoMemo", "");
    	}
    }  
}

// 撈出多筆單身
function changeLines(){
	var adjustmentType	= vat.item.getValueByName("#F.adjustmentType");
	var sourceOrderNo = vat.item.getValueByName("#F.sourceOrderNo").replace(/^\s+|\s+$/, '').toUpperCase();
	var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var taxCode = vat.item.getValueByName("#F.taxType");
	if("F" == taxCode && "" != adjustmentType && "22" == adjustmentType){
		if( "" != defaultWarehouseCode && "" != sourceOrderNo){
			vat.ajax.XHRequest({
	           post:"process_object_name=imGeneralAdjustmentService"+
	                    "&process_object_method_name=updateAJAXBySourceOrderNo"+
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
	                    "&sourceOrderTypeCode=" + vat.item.getValueByName("#F.sourceOrderTypeCode") +
	                    "&sourceOrderNo=" + sourceOrderNo +
	                    "&warehouseCode=" + defaultWarehouseCode+
	                   	"&headId=" + vat.item.getValueByName("#F.headId"), 
	           find: function change(oXHR){ 
	           		vat.item.setValueByName("#F.sourceOrderTypeNoMemo", vat.ajax.getValue("sourceOrderTypeNoMemo", oXHR.responseText) );
	           		vat.block.pageRefresh(vnB_Detail);
	           		
	           },
	           fail: function changeError(){
	           }   
			});
	    }else{
	    	if("" == adjustmentType){
	    		alert("請先輸入調整類別");
	    	}else if("" == defaultWarehouseCode ){
	    		alert("請先輸入庫別");
	    	}else if("" == sourceOrderNo){
	    		vat.item.setValueByName("#F.sourceOrderTypeNoMemo", "");
	    	}
	    }  
	}
}

// 是否鎖line的總成本
function changeAmountReadOnly(){
	var isAdjustCostCheckBox = vat.item.getValueByName("#F.isAdjustCostCheckBox");
	
//	alert("isAdjustCost = " + isAdjustCost);
	if(isAdjustCostCheckBox == "Y"){
		vat.item.setGridAttributeByName("amount", "readOnly", false); 
		vat.item.setValueByName("#H.isAdjustCost", "Y");
	}else{
		vat.item.setGridAttributeByName("amount", "readOnly", true);
		vat.item.setValueByName("#H.isAdjustCost", "N");
	}
}

// 依單別,調整類型,連動庫別
function changeWarehouse(){
	vat.ajax.XHRequest({
		post:"process_object_name=imGeneralAdjustmentService"+
        		"&process_object_method_name=getAJAXWarehouse"+
          		"&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
              	"&adjustmentType=" + vat.item.getValueByName("#F.adjustmentType") +
              	"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
              	"&taxType=" + vat.item.getValueByName("#F.taxType") +
              	"&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode, 
      	find: function change(oXHR){ 
      		vat.item.setValueByName("#F.sourceOrderTypeCode",  vat.ajax.getValue("sourceOrderType", oXHR.responseText));
      		vat.item.setValueByName("#F.declarationType",  vat.ajax.getValue("declType", oXHR.responseText));
           	var allWarehouseCodes = eval(vat.ajax.getValue("allWarehouseCodes", oXHR.responseText));
           	
          	allWarehouseCodes[0][0] = "#F.defaultWarehouseCode";
			vat.item.SelectBind(allWarehouseCodes);
      	},
     	fail: function changeError(){
//     		alert("error");
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
	vat.item.setValueByName("#F.headId", code);
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = "";    
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=imGeneralAdjustmentAction&process_object_method_name=performOffShoreIsIandInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll()
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
				
				var taxType = vat.item.getValueByName("#F.taxType");
				var adjustmentType = vat.item.getValueByName("#F.adjustmentType");
				if(taxType == "F" && (adjustmentType == "21") ){ // 出倉
	 		      	refreshWfParameter(vat.item.getValueByName("#F.brandCode"), 
	        			vat.item.getValueByName("#F.orderTypeCode"), 
	        			vat.item.getValueByName("#F.orderNo"));
	        		vat.block.pageRefresh(102);		 
	        		vat.tabm.displayToggle(0, "xTab3", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
				}
				checkCustomsStatus();
				doFormAccessControl();
				checkUploadControl();
				getCustomsDesc();
     	}});
 	
    
}

// do picker 回來的事件
function doAfterPickerFunctionProcess(code,id){
	//do picker back something
	var vLineId	= vat.item.getGridLine(id);
	switch(code){
		case "itemCode":
			if(typeof vat.bean().vatBeanPicker.imOnHandResult != 'undefined'){ 
				var adjustmentType		= vat.item.getValueByName("#F.adjustmentType");
				var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
				var brandCode = vat.item.getValueByName("#F.brandCode"); 
				
//				if( defaultWarehouseCode == "" || adjustmentType == "" ){
//					if( adjustmentType == "" ){
//						return alert("請選擇調整類別");
//					}else if( defaultWarehouseCode == "" ){
//						return alert("請選擇庫別");
//					}
//				}else{
					vat.block.pageRefresh(vnB_Detail);
					
					vat.bean().vatBeanOther.lineId = vLineId; 
			  		vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
			  		vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
			  		vat.bean().vatBeanOther.taxType = vat.item.getValueByName("#F.taxType");
					vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentService"+
					            "&process_object_method_name=updatePickerData";}, 
					     {other:true,picker:true, funcSuccess: function() {vat.block.pageRefresh(vnB_Detail);}});
//				}     
			}		
		break;
		case "detailLine":
			if(typeof vat.bean().vatBeanPicker.cmOnHandResult != 'undefined'){ 
		//		alert( "vLineId = " + vLineId);
		//		alert( vat.bean().vatBeanPicker.cmOnHandResult[0]["brandCode"] );
		//		alert( vat.bean().vatBeanPicker.cmOnHandResult[0]["customsWarehouseCode"] );
		//		alert( vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationNo"] );
		//		alert( vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationSeq"] );
		    	vat.item.setGridValueByName("originalDeclarationNo", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationNo"]); 
		    	vat.item.setGridValueByName("originalDeclarationSeq", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationSeq"]);
		    	
		//   	alert( vat.item.getGridValueByName("originalDeclarationNo", vLineId)  );
		//		alert( vat.item.getGridValueByName("originalDeclarationSeq", vLineId)  );
				changeLineData(vLineId);
			}	
		break;
	}	
}

// 依狀態鎖form
function doFormAccessControl(){
	var status 			= vat.item.getValueByName("#F.status");
	var processId		= vat.bean().vatBeanOther.processId;
	var formId 			= vat.bean().vatBeanOther.formId;
	var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	var orderTypeCode 	= vat.item.getValueByName("#F.orderTypeCode");
	var taxType 		= vat.item.getValueByName("#F.taxType"); 
	var adjustmentType 	= vat.item.getValueByName("#F.adjustmentType"); 
	var brandCode 		= vat.item.getValueByName("#F.brandCode"); 
	var isAdjustCost 	= vat.item.getValueByName("#H.isAdjustCost");
	
	
//	if(taxType == "F"){
//		vat.tabm.displayToggle(0, "xTab1", true);
//	}else{
//		vat.tabm.displayToggle(0, "xTab1", false);
//	}

	if("Y" == isAdjustCost){
		vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
	}else{
		vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
	}
	

	// 初始化
	//======================<header>=============================================
	if("T2" == brandCode){
		vat.item.setAttributeByName("#F.adjustmentType", "readOnly", false);
	}else{
		vat.item.setAttributeByName("#F.adjustmentType", "readOnly", true);	
	}
	vat.item.setAttributeByName("#F.remark1", "readOnly", false);
	vat.item.setAttributeByName("#F.isAdjustCostCheckBox", "readOnly", false);
	vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", false); 
	
	vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
	vat.item.setAttributeByName("#F.sourceOrderNo", "readOnly", false);		
	
	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true);
	//======================<master>=============================================
	vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", false); 
	vat.item.setAttributeByName("#F.boxQty", "readOnly", false);
	//======================<detail>=============================================
//	vat.item.setGridAttributeByName("itemCode", "readOnly", false); 
//	vat.item.setGridAttributeByName("lotNo", "readOnly", false); 
//	vat.item.setGridAttributeByName("difQuantity", "readOnly", false);

	vat.item.setAttributeByName("vatDetailDiv", "readOnly", false,true,true);	
	//=======================<buttonLine>========================================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.save2", 	"display", "none");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	vat.item.setStyleByName("#B.import", 	"display", "inline");
	vat.item.setStyleByName("#B.print", 	"display", "inline");
	vat.item.setStyleByName("#B.declaration", 	"display", "inline");	// 核銷報單
	//===========================================================================
	if(adjustmentType == "21" && taxType == "F" ){
		vat.item.setStyleByName("#B.declaration", 	"display", "inline");	// 核銷報單
	}else{
		vat.item.setStyleByName("#B.declaration", 	"display", "none");	// 核銷報單
	}
	
	if(orderNoPrefix == "TMP" ){
		vat.item.setStyleByName("#B.print", 	"display", "none");
	}
	
	//for 儲位用
	if(enableStorage){
		vat.item.setStyleByName("#B.storageExport"   , "display", "inline");
		vat.item.setStyleByName("#B.storageImport"   , "display", "inline");
	}else{
		vat.item.setStyleByName("#B.storageExport"   , "display", "none");
		vat.item.setStyleByName("#B.storageImport"   , "display", "none");
	}
	
	if(formId != ""){
		if( processId != null && processId != 0 ){ //從待辦事項進入
			vat.item.setStyleByName("#B.new", 		"display", "none");
			vat.item.setStyleByName("#B.search", 	"display", "none");
			
			// 避免T2第一次暫存起流程Ａ，駁回後選別的調整類別而錯亂流程，固把它鎖起來
			// 與按暫存紐檢查調整類別為必填是互相搭配
			if( brandCode.indexOf("T2") > -1){ 
				if( adjustmentType != "" ){
					vat.item.setAttributeByName("#F.adjustmentType", "readOnly", true);
				}
			}
			
			if( status == "SAVE" ){
				vat.item.setStyleByName("#B.void", 		"display", "inline"); 
			}else if( status == "SIGNING" ){
				var userType = vat.bean().vatBeanOther.userType;
				if("CREATOR" == userType){ // 若為起單人則可存檔
					vat.item.setStyleByName("#B.save2", 	"display", "inline");
				}
			
				vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
				vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
			}
		}else{
			// 查詢回來
			if( status == "SAVE" || status == "REJECT"){
				vat.item.setStyleByName("#B.submit", 	"display", "none"); 
				vat.item.setStyleByName("#B.save", 		"display", "none"); 
				vat.item.setStyleByName("#B.submitBG", 	"display", "none"); 
				vat.item.setStyleByName("#B.message", 	"display", "none"); 
				vat.item.setStyleByName("#B.import", 	"display", "none"); 
				
				//======================<header>=============================================
				vat.item.setAttributeByName("#F.adjustmentType", "readOnly", true);
				vat.item.setAttributeByName("#F.remark1", "readOnly", true);
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
				vat.item.setAttributeByName("#F.isAdjustCostCheckBox", "readOnly", true);
				vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true); 
				//======================<master>=============================================
				vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", true); 
				vat.item.setAttributeByName("#F.boxQty", "readOnly", true);
				//======================<detail>=============================================
/*				vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
				vat.item.setGridAttributeByName("lotNo", "readOnly", true); 
				vat.item.setGridAttributeByName("amount", "readOnly", true);
				vat.item.setGridAttributeByName("difQuantity", "readOnly", true);
				vat.item.setGridAttributeByName("originalDeclarationNo", "readOnly", true);
				vat.item.setGridAttributeByName("originalDeclarationSeq", "readOnly", true);
				vat.item.setGridAttributeByName("boxNo", "readOnly", true);
				vat.item.setGridAttributeByName("weight", "readOnly", true);
*/
				vat.block.canGridModify([vnB_Detail], false,false,false);	
				vat.item.setAttributeByName("vatDetailDiv", "readOnly", true);	
			}	
		}
	}
	
	if( status == "SIGNING" || status == "FINISH" || status == "CLOSE"  || status == "VOID" ){
		//======================<header>=============================================
		vat.item.setAttributeByName("#F.adjustmentType", "readOnly", true);
		vat.item.setAttributeByName("#F.remark1", "readOnly", true);
		vat.item.setAttributeByName("#F.isAdjustCostCheckBox", "readOnly", true);
		vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true); 
		//======================<master>=============================================
		vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", true); 
		vat.item.setAttributeByName("#F.boxQty", "readOnly", true);
		//======================<detail>=============================================
/*		vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
		vat.item.setGridAttributeByName("lotNo", "readOnly", true); 
		vat.item.setGridAttributeByName("amount", "readOnly", true);
		vat.item.setGridAttributeByName("difQuantity", "readOnly", true);
		vat.item.setGridAttributeByName("originalDeclarationNo", "readOnly", true);
		vat.item.setGridAttributeByName("originalDeclarationSeq", "readOnly", true);
		vat.item.setGridAttributeByName("boxNo", "readOnly", true);
		vat.item.setGridAttributeByName("weight", "readOnly", true);
*/
		vat.block.canGridModify([vnB_Detail], true,false,false);
		vat.item.setAttributeByName("vatDetailDiv", "readOnly", true);
		//=======================<buttonLine>========================================
		if( status == "SIGNING" ){ // || status == "FINISH" 
			vat.item.setStyleByName("#B.submit", 		"display", "inline");
			vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
			vat.item.setAttributeByName("#F.sourceOrderNo", "readOnly", false);
			vat.item.setStyleByName("#B.message", 		"display", "inline");
			vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", false);	
			if( processId != null && processId != 0 && (adjustmentType == "21" || adjustmentType == "22") ){
				if("CREATOR" == userType){
					vat.item.setStyleByName("#B.message", 		"display", "inline");
					vat.item.setAttributeByName("#F.isAdjustCostCheckBox", "readOnly", false);
					
					var isAdjustCostCheckBox = vat.item.getValueByName("#F.isAdjustCostCheckBox");
					if(isAdjustCostCheckBox == "Y"){
						vat.item.setGridAttributeByName("amount", "readOnly", false); // 馬祖出倉之流程中簽核中可以改總成本
					}else{
						vat.item.setGridAttributeByName("amount", "readOnly", true);
					}	
				
					
				}
			}
			
			
		}else{
			vat.item.setStyleByName("#B.submit", 		"display", "none");
			vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
			vat.item.setAttributeByName("#F.sourceOrderNo", "readOnly", true);
			vat.item.setStyleByName("#B.message", 		"display", "none");
		}
		vat.item.setStyleByName("#B.submitBG", 		"display", "none");
		vat.item.setStyleByName("#B.save", 			"display", "none");
		vat.item.setStyleByName("#B.declaration", 	"display", "none");
		vat.item.setStyleByName("#B.import", 		"display", "none");
		//===========================================================================
	}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_GENERAL_ADJUSTMENT" + 
		"&levelType=ERROR" +
        "&processObjectName=imGeneralAdjustmentService" + 
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
    vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

// 展報單
function execExtendItemInfo(){
	vat.block.pageDataSave( vnB_Detail ,{ 
		funcSuccess:function(){
		    vat.bean().vatBeanOther.processObjectName = "imGeneralAdjustmentService";
		    vat.bean().vatBeanOther.searchMethodName = "executeFind"; 
		    vat.bean().vatBeanOther.tableType = "IM_ADJUSTMENT";
		    vat.bean().vatBeanOther.searchKey = vat.item.getValueByName("#F.headId");
		    vat.bean().vatBeanOther.subEntityBeanName = "imAdjustmentLines";
		    vat.bean().vatBeanOther.itemFieldName = "itemCode";
		    vat.bean().vatBeanOther.warehouseCodeFieldName = "warehouseCode";   
		    vat.bean().vatBeanOther.declTypeFieldName = "moreOrLessType";			// 找無
		    vat.bean().vatBeanOther.declNoFieldName = "originalDeclarationNo";	 
		    vat.bean().vatBeanOther.declSeqFieldName = "originalDeclarationSeq";  
		    vat.bean().vatBeanOther.declDateFieldName = "originalDeclarationDate";  
		    vat.bean().vatBeanOther.lotFieldName = "lotNo";
		    vat.bean().vatBeanOther.qtyFieldName = "difQuantity";
		    vat.block.submit(function(){return "process_object_name=appExtendItemInfoService"+
		            "&process_object_method_name=executeExtendItem";}, {other:true, funcSuccess: function() {vat.block.pageRefresh(vnB_Detail);}});
		}
	});            
}

// 票據列印
function openReportWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
			                    	//alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);	
	if("AFTER_SUBMIT"==type) createRefreshForm();
}

// 匯出
function exportFormData(){
	var taxType = vat.item.getValueByName("#F.taxType");
	var importBeanName = "P" == taxType ? "IM_GENERAL_P" : "IM_GENERAL_F_SQL";
    var headId = vat.item.getValueByName("#F.headId");
    var condition = ( "P" == taxType ? "&arguments=" + headId + "&parameterTypes=LONG" : "&headId=" + headId )
    var url = "/erp/jsp/" + ("P" == taxType ? "ExportFormData.jsp" : "ExportFormView.jsp") +
              "?exportBeanName=" + importBeanName +
              "&fileType=XLS" + 
              "&processObjectName=imGeneralAdjustmentService" + 
              "&processObjectMethodName=" +   ("P" == taxType ? "executeFind&gridFieldName=imAdjustmentLines" : "getAJAXFExportData")+
              condition;
    var width = "200";
    var height = "30";
    window.open(url, '調整單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 匯入
function importFormData(){
	var width = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
    var taxType = vat.item.getValueByName("#F.taxType");
    var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
    if( taxType == "F" ){
    	var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=IM_GENERAL_F_SQL" +
			"&importFileType=XLS" +
	        "&processObjectName=imGeneralAdjustmentService" + 
	        "&processObjectMethodName=executeImportLists" +
	        "&arguments=" + headId + "{$}" + 
	        				defaultWarehouseCode + 
	        "&parameterTypes=LONG{$}STRING" +
	        "&blockId=" + vnB_Detail,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
    }else if( taxType == "P" ){
    	var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=IM_GENERAL_P" +
			"&importFileType=XLS" +
	        "&processObjectName=imGeneralAdjustmentService" + 
	        "&processObjectMethodName=executeImportLists" +
	        "&arguments=" + headId + "{$}" + 
	        				defaultWarehouseCode + 
	        "&parameterTypes=LONG{$}STRING" +
	        "&blockId=" + vnB_Detail,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
    }
}

function doBarcode( ){
	var width = "1024";
    var height = "768";
    var url;
	url = "/erp/jsp/ExportFormView.jsp" + 
             "?exportBeanName=IM_ADJUSTMENT_MEG_HEAD,IM_ADJUSTMENT_MEG_LINE" + 
             "&fileType=TXT" + 
             "&processObjectName=imGeneralAdjustmentService,imGeneralAdjustmentService" + 
             "&processObjectMethodName=getAJAXExportDataHeadBySql,getAJAXExportDataLineBySql" + 
             "&headId=" + vat.item.getValueByName("#F.headId");
    	window.open(url, '條碼匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + 
    				 	 ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 上傳海關
function updateCustomsStatus(tranStatus){
	//alert("adv");
		//alert(tranStatus);
		if(confirm("是否要送出?")){
			vat.bean().vatBeanOther =
        	{ 
          		loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          		loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          		formId             	: document.forms[0]["#formId"            ].value,
          		orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,
          		tranStatus         : tranStatus,
          		customsWarehouse	:'KW',
          		headId : vat.item.getValueByName("#F.headId")
        	};
   			vat.bean.init(	
	  			function(){
						return "process_object_name=imGeneralAdjustmentService&process_object_method_name=updateCustomsStatus"; 
	    		},{link:true, other: true,
	    			funcSuccess:function(){
					window.top.close();
					}
    			}
    		);	
		}
		
}
