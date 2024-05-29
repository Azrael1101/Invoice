/*** 
 *	檔案: imApartAdjustment.js
 *	說明： 拆/併貨調整單
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){

 	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float"); 
		vat.tabm.createButton(0 ,"xTab2","調整單明細檔" ,"vatDetailDiv"        	,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false, "doPageDataSave("+vnB_Detail+")");  
		
 	}  
	detailInitial();
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
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imGeneralAdjustmentAction&process_object_method_name=performApartInitial"; 
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
	 //vat.item.setValueByName("#F.processCustCd", "FW");
  	 //vat.item.setValueByName("#F.resStoreCode", "CD198");
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
	var cStatus = customsStatus.substring(0, 1);
	var RecordStatus = tranRecordStatus.substring(0, 5);
	//alert("start check status");
	if((status === "FINISH") && customsStatus === "" ){
		//alert("1");
		vat.item.setStyleByName("#B.sendCustoms","display","inline");
		//vat.item.setStyleByName("#B.sendCancel","display","none");
	}
	else{
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendBack","display","none");
		vat.item.setStyleByName("#B.sendCancel","display","none");
		//vat.item.setValueByName("#F.customsStatus", "");
	}
	//alert("end check status");
	//vat.item.setStyleByName("#B.sendCustoms","display","inline");
	//vat.item.setStyleByName("#B.sendCustoms","display","none");
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
	 									 service:"Im_GeneralAdjustment:apartSearch:20091111.page", 
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
	 			{name:"#B.close"      , type:"IMG"    ,value:"結案",   src:"./images/button_close.gif", eClick:'doSubmit("CLOSE")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.declaration" , type:"IMG"    ,value:"核銷報單",   src:"./images/button_declaration.gif" , eClick:'execExtendItemInfo()'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.copyDetail" 	   , type:"IMG"    ,value:"明細COPY",   src:"./images/detail_copy.jpg", eClick:'copyDetail()'},
	 			{name:"#B.sendCustoms" 	   , type:"IMG"    ,value:"上傳海關",   src:"./images/send_customs1.jpg", eClick:'updateCustomsStatus("")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},	 			
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

// 調整單主檔
function headerInitial(){ 
	var allOrderTypes = vat.bean("allOrderTypes");
	var allSourceOrderTypeCode = [["", "", "請選擇"], ["1-整理","2-分割 "], ["1", "2"]];
	var allProcessCustCd = [["", "", false], ["FW","KW"], ["FW", "KW"]];
	var allResStoreCode = [["", "", false], ["CD198","AD910"], ["CD198", "AD910"]];
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"重整功能維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 		type:"LABEL", 	value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 		type:"SELECT", 	bind:"orderTypeCode", init:allOrderTypes, size:15, mode:"READONLY"},
	 		 			{name:"#F.orderType"   , type:"TEXT"  ,  bind:"orderTypeCode", size:6, mode:"HIDDEN"},
						{name:"#F.headId", 					type:"TEXT",  	bind:"headId", back:false, mode:"HIDDEN"}]},
				{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"重整單號" }]},
				{items:[{name:"#F.orderNo", 			type:"TEXT", 	bind:"orderNo", size:20, mode:"READONLY"}]},
				{items:[{name:"#L.adjustmentDate", 		type:"LABEL", 	value:"重整日期<font color='red'>*</font>" }]},
				{items:[{name:"#F.adjustmentDate", 		type:"Date", 	bind:"adjustmentDate", size:15 }]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", mode:"HIDDEN"},
	 					{name:"#F.brandName", 			type:"TEXT",  	bind:"brandName", size:8, mode:"READONLY"}]},
				{items:[{name:"#L.status", 				type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"TEXT", 	bind:"status", size:15, mode:"HIDDEN"},
					    {name:"#F.statusName", 				type:"TEXT",  bind:"statusName", back:false, mode:"READONLY"},
					    {name:"#F.customsDesc"      , type:"TEXT"  , size:35 , mode:"READONLY"},
					    {name:"SPACE"          , type:"LABEL"  ,value:"　"},
	  		 			{name:"#F.tranRecordStatus"  , type:"TEXT"  ,  bind:"tranRecordStatus", mode:"HIDDEN"},
	  		 			{name:"#F.customsStatus", 				type:"TEXT", mode:"READONLY"},
	  		 			{name:"#F.customsStatusHidden", 				type:"TEXT",  bind:"customsStatus", mode:"HIDDEN"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.resNo", 				type:"LABEL", 	value:"重整申請書號碼"}]},	 
	 			{items:[{name:"#F.resNo", 				type:"TEXT",  	bind:"resNo",	size:15, maxLen:14, mode:"READONLY" }]}, 
				{items:[{name:"#L.processCustCd",		type:"LABEL", 	value:"受理關別" }]},
				{items:[{name:"#F.processCustCd",		type:"SELECT", 	bind:"processCustCd", maxLen:2,init:allProcessCustCd, eChange:"changeCustomsWarehouse()"}]},
				{items:[{name:"#L.resStoreCode",		type:"LABEL", 	value:"重整處所" }]},
				{items:[{name:"#F.resStoreCode",		type:"SELECT", 	bind:"resStoreCode", maxLen:5, mode:"READONLY", init:allResStoreCode}]},
				{items:[{name:"#L.befTotalItems",		type:"LABEL", 	value:"整前總項次"}]},
				{items:[{name:"#F.befTotalItems",		type:"NUMB", 	bind:"befTotalItems", maxLen:3}]},
				{items:[{name:"#L.aftTotalItems",		type:"LABEL", 	value:"整後總項次"}]},
				{items:[{name:"#F.aftTotalItems",		type:"NUMB", 	bind:"aftTotalItems", maxLen:3}]}
			]},	
			{row_style:"", cols:[
				{items:[{name:"#L.reason", 				type:"LABEL", 	value:"原因"}]},	 
	 			{items:[{name:"#F.reason", 				type:"TEXT",  	bind:"reason",	size:180, maxLen:180 }], td:" colSpan=5"}, 
				{items:[{name:"#L.createBy",		type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",		type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.creationDate",		type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",		type:"TEXT", 	bind:"creationDate", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.sourceOrderTypeCode",		type:"LABEL", 	value:"重整類型<font color='red'>*</font>" }]},
				{items:[{name:"#F.sourceOrderTypeCode",		type:"SELECT", 	bind:"sourceOrderTypeCode",init:allSourceOrderTypeCode, size:1 }]},
	 			{items:[{name:"#L.remark1",	type:"LABEL", 	value:"備註:" }]}, 
				//{items:[{name:"#L.remark2",	type:"LABEL", 	value:"1. 拆貨 :請將要<font color='red'> 拆貨商品 </font>的品號填寫在<font color='red'>拆貨後新商品</font>的<font color='blue'>關聯品號</font>欄位上<br>2. 併貨 :請將<font color='red'>併貨後新商品</font>的品號填寫在<font color='red'>併貨商品</font>的<font color='blue'>關聯品號</font>欄位上" }], td:" colSpan=7"}
				{items:[{name:"#F.remark1",	type:"TEXT" , 	bind:"remark1",	size:100}], td:" colSpan=7"}

			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

// 調整單明細
function detailInitial(){
	var allItemType = [["","true",""],["請選擇","O-非菸酒類","W-酒","C-雪茄","T-捲菸","P-菸絲","X-其他菸品","M-藥妝"],["NULL","O","W","C","T","P","X","M"]];
	var allResetType = [["", "", "請選擇"],["1-重整前","2-重整後"],["1","2"]];
	var status = vat.item.getValueByName("#F.status");
//	alert(status);
	
	var isOpen = true;
	if( status == "SIGNING" || status == "FINISH" || status == "VOID" || status == "CLOSE" ){
		isOpen = false;	
	}
	
	var vbCanGridDelete = isOpen;
	var vbCanGridAppend = isOpen;
	var vbCanGridModify = isOpen;
  
	vat.item.make(vnB_Detail, "indexNo"				, {type:"IDX", 	view:"fixed", desc:"序號" });
	vat.item.make(vnB_Detail, "beAft"				, {type:"SELECT", view:"", size:1, init:allResetType, desc:"重整前/後"});
	vat.item.make(vnB_Detail, "itemCode"			, {type:"TEXT", view:"fixed", size:20, maxLen:20, desc:"品號", eChange:"changeLineItemCode()"          	});
	
	vat.item.make(vnB_Detail, "searchItem"	        , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 		     	service:"Im_OnHand:search:20091224.page",
	 									 				left:0, right:0, width:1024, height:768,	
	 									 				servicePassData:function(x){ return doPassData( "itemCode", x ); },	
	 									 		 	 	serviceAfterPick:function(id){doAfterPickerFunctionProcess("itemCode",id); } });
	//vat.item.make(vnB_Detail, "itemCName"			, {type:"TEXT", view:"", size:20, desc:"品名", mode:"READONLY"          	}); 									 		                
	vat.item.make(vnB_Detail, "itemCName"			, {type:"TEXT", view:"", size:20, desc:"品名", mode:"READONLY"          	});
	vat.item.make(vnB_Detail, "warehouseCode"		, {type:"TEXT", view:"", size:10, desc:"庫別", eChange:"changeLineWarehouseCode()"   	});
	vat.item.make(vnB_Detail, "warehouseCodeSearch"	, {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 			service:"Im_Warehouse:search:20091112.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			servicePassData:function(x){ return doPassData( "warehouseCode", x ); },		
	 									 			serviceAfterPick:function(id){doAfterPickerFunctionProcess("warehouseCode",id); } });  
	vat.item.make(vnB_Detail, "warehouseName"		, {type:"TEXT", view:"", size:10, desc:"庫名", mode:"READONLY"    	});
	vat.item.make(vnB_Detail, "lotNo"				, {type:"TEXT", view:"", size:10, maxLen:12 , desc:"批號", mode:""});
	vat.item.make(vnB_Detail, "difQuantity"			, {type:"NUMB", view:"", size:8, maxLen:8, desc:"數量", eChange:"changeLineAmount()" }); 

	vat.item.make(vnB_Detail, "localUnitCost"		, {type:"NUMB", view:"", size:8, maxLen:8, desc:"單項金額", mode:"HIDDEN", eChange:"changeLineAmount()" });
	//vat.item.make(vnB_Detail, "amount"				, {type:"NUMB", view:"", desc:"合計金額", mode:"HIDDEN" });
	vat.item.make(vnB_Detail, "amount"				, {type:"NUMB", view:"", desc:"總成本" });
	vat.item.make(vnB_Detail, "sourceItemCode"		, {type:"TEXT", view:"", size:20, maxLen:20, desc:"關聯品號", mode:"HIDDEN" });
	//vat.item.make(vnB_Detail, "accountCode"			, {type:"TEXT", view:"shift", maxLen:20, desc:"帳務代號", mode:"" });
	//vat.item.make(vnB_Detail, "reason"				, {type:"TEXT", view:"shift", maxLen:100, desc:"原因", mode:"" });
	vat.item.make(vnB_Detail, "originalDeclarationNo"	        , {type:"TEXT" , view:"", size:15, desc:"報單號碼" });
	vat.item.make(vnB_Detail, "searchDeclaration"		  , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", //mode:"HIDDEN",
	 									 			service:"Cm_DeclarationOnHand:search:20091103.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			servicePassData:function(x){ return doPassDeclData(x); },
	 									 			serviceAfterPick:function(id){doDeclAfterPickerProcess(id); } });
	vat.item.make(vnB_Detail, "originalDeclarationSeq"	        	, {type:"NUMB" , view:"", desc:"報單項次",eChange:"changeDeclarationSeq()"  });
	vat.item.make(vnB_Detail, "itemNo"	        	, {type:"NUMB" , view:"shift", desc:"重整前/後<br>序號" });
	vat.item.make(vnB_Detail, "unit"	        	, {type:"TEXT" , view:"shift", size:10, maxLen:3, desc:"重整前/後<br>單位" });
	vat.item.make(vnB_Detail, "cigarWineMark"		, {type:"SELECT" , view:"shift", size:1, desc:"重整前/後<br>菸酒註記",init:allItemType});

	//vat.item.make(vnB_Detail, "declarationDate"	, {type:"DATE",  view:"" , desc:"報單起始日"});
	//vat.item.make(vnB_Detail, "itemNo"	        	, {type:"NUMB" , view:"shift", desc:"重整前/後<br>序號" });
	/*
	vat.item.make(vnB_Detail, "specDesc"	        , {type:"TEXT" , view:"shift", size:10, desc:"重整前/後<br>規格" });
	vat.item.make(vnB_Detail, "unit"	        	, {type:"TEXT" , view:"shift", size:10, maxLen:3, desc:"重整前/後<br>單位" });
	vat.item.make(vnB_Detail, "importTax"			, {type:"NUMB" , view:"shift", size:10, desc:"重整前/後<br>進口稅"});
	vat.item.make(vnB_Detail, "goodsTax"			, {type:"NUMB" , view:"shift", size:15, desc:"重整前/後<br>貨物稅"});
	vat.item.make(vnB_Detail, "cigarWineTax"		, {type:"NUMB" , view:"shift", size:15, desc:"重整前/後<br>菸酒稅"});
	vat.item.make(vnB_Detail, "health"   			, {type:"NUMB" , view:"shift", size:15, desc:"重整前/後<br>健康福利捐"});
	vat.item.make(vnB_Detail, "advDutyRate"			, {type:"NUMB" , view:"shift", size:15, desc:"重整前/後<br>進口稅率"});
	vat.item.make(vnB_Detail, "cigarWineTaxRate"	, {type:"NUMB" , view:"shift", size:15, desc:"重整前/後<br>菸酒稅稅率"});
	vat.item.make(vnB_Detail, "goodsTaxRate"		, {type:"NUMB" , view:"shift", size:15, desc:"重整前/後<br>貨物稅率"});
	vat.item.make(vnB_Detail, "cigarWineMark"		, {type:"TEXT" , view:"shift", size:15, desc:"重整前/後<br>菸酒註記"});
	*/
	vat.item.make(vnB_Detail, "lineId"          	, {mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isLockRecord"    	, {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_Detail, "isDeleteRecord"  	, {type:"DEL", view:"fixed", desc:"刪除"});
	vat.item.make(vnB_Detail, "message"         	, {type:"MSG"  , desc:"訊息"});
	vat.item.make(vnB_Detail, "specDesc"	        , {type:"TEXT" , view:"shift", size:10, desc:"重整前/後<br>說明"});
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
   //alert("LOAD");	
	if( vnB_Detail === div ){
		var processString = "process_object_name=imGeneralAdjustmentService&process_object_method_name=getAJAXApartPageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") ;
		return processString;	
	}							
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
//    alert("saveBeforeAjxService"); 

	if( vnB_Detail === div ){
	    //alert("eeee");
		var processString = "process_object_name=imGeneralAdjustmentService&process_object_method_name=updateAJAXApartPageLinesData" + 
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

	if(vnB_Detail===div){
		vat.block.pageRefresh(div); //存檔vnB_Detail 
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
	vat.item.setValueByName("#L.currentRecord", "0");
	vat.item.setValueByName("#L.maxRecord", "0");
    vat.bean().vatBeanPicker.result = null; 
	refreshForm("");
}



// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
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
	}else if("CLOSE" == formAction){
		alertMessage = "是否確定結案?";
	}
	if(confirm(alertMessage)){
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var approvalResult        = "true";  
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
		var orderNoPrefix         = vat.item.getValueByName("#F.orderNo").substring(0,3);
		
	    if(vat.item.getValueByName("#F.adjustmentDate")==="" ||vat.item.getValueByName("#F.sourceOrderTypeCode")==="" )
	    {
				alert("重整日期/重整類型未填入，請填入後送出");
		}
		else{
			if(( orderNoPrefix == "TMP" &&  status == "SAVE" ) ||(inProcessing   && (status == "SAVE" )) ){
			$.blockUI({
		       						 message: '<font size=4 color="#000000"><b>執行中請稍後...</b></font>',
		       						 overlayCSS: { // 遮罩的css設定
		            				backgroundColor: '#eee'
		       			 },
		       			 css: { // 遮罩訊息的css設定
		            				border: '3px solid #aaa',
		            				width: '30%',
		            				left: '35%',
		            				backgroundColor: 'white',
		            				opacity: '0.9' //透明度，值在0~1之間
		        		}
	    				});		
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
					                    "&process_object_method_name=performTransactionApart";},{
					                    bind:true, link:true, other:true,
					                    funcSuccess:function(){
							        		//vat.block.pageRefresh(vnB_Detail);
							        		$.unblockUI();
							        	}}
							);
				        } 
			      	}
		      	});
			}
		}
	}
}
// 改變合計金額
function changeLineAmount(){
	var vLineId 		= vat.item.getGridLine();
	var difQuantity		= vat.item.getGridValueByName("difQuantity", vLineId);
	var localUnitCost	= vat.item.getGridValueByName("localUnitCost", vLineId);
	
	vat.ajax.XHRequest({
	           post:"process_object_name=imGeneralAdjustmentService"+
	                    "&process_object_method_name=getAJAXAmount"+
	                    "&difQuantity=" + difQuantity +
	                    "&localUnitCost=" + localUnitCost,
	           find: function change(oXHR){ 
	           		vat.item.setGridValueByName("amount", vLineId, vat.ajax.getValue("amount", oXHR.responseText));
	           		vat.item.setGridValueByName("localUnitCost", vLineId, vat.ajax.getValue("localUnitCost", oXHR.responseText));
	           },
	           fail: function changeError(){
	          		vat.item.setGridValueByName("amount", vLineId, "");
	          		vat.item.setGridValueByName("localUnitCost", vLineId, "");
	           }   
	});
}

// 改變品號
function changeLineItemCode(){
	var vLineId 		= vat.item.getGridLine();
	var itemCode		= vat.item.getGridValueByName("itemCode", vLineId);
	var brandCode 		= vat.bean().vatBeanOther.loginBrandCode;
	var lotNo			= vat.item.getGridValueByName("lotNo", vLineId);
	var difQuantity		= vat.item.getGridValueByName("difQuantity", vLineId);
	var localUnitCost	= vat.item.getGridValueByName("localUnitCost", vLineId);
	
	vat.ajax.XHRequest({
	           post:"process_object_name=imGeneralAdjustmentService"+
	                    "&process_object_method_name=getAJAXItemCode"+
	                    "&itemCode=" + itemCode +
	                    "&lotNo=" + lotNo +
	                    "&localUnitCost=" + localUnitCost +
	                    "&difQuantity=" + difQuantity +
	                    "&brandCode=" + brandCode,
	           find: function change(oXHR){ 
	           		vat.item.setGridValueByName("itemCode", vLineId, vat.ajax.getValue("itemCode", oXHR.responseText));
	           		vat.item.setGridValueByName("itemCName", vLineId, vat.ajax.getValue("itemCName", oXHR.responseText));
	           		vat.item.setGridValueByName("specDesc", vLineId, vat.ajax.getValue("specDesc", oXHR.responseText));
	           		vat.item.setGridValueByName("lotNo", vLineId, vat.ajax.getValue("lotNo", oXHR.responseText));
	           		vat.item.setGridValueByName("localUnitCost", vLineId, vat.ajax.getValue("localUnitCost", oXHR.responseText));
	           		vat.item.setGridValueByName("beAft", vLineId, "1");
	           },
	           fail: function changeError(){
	          		vat.item.setGridValueByName("itemCode", vLineId, "");
	          		vat.item.setGridValueByName("itemCName", vLineId, "");
	          		vat.item.setGridValueByName("lotNo", vLineId, "");
	          		vat.item.setGridValueByName("localUnitCost", vLineId, "-1");
	          		vat.item.setGridValueByName("beAft", vLineId, "");
	          		vat.item.setGridValueByName("specDesc", vLineId, "");
	           }   
	});
}
// 改變庫別
function changeLineWarehouseCode(LineId){

	var vLineId 		= typeof(LineId) === "undefined" ? vat.item.getGridLine() : LineId;
	var warehouseCode	= vat.item.getGridValueByName("warehouseCode", vLineId);
	var brandCode 		= vat.bean().vatBeanOther.loginBrandCode;
	vat.ajax.XHRequest({
	           post:"process_object_name=imWarehouseService"+
	                    "&process_object_method_name=getAJAXWarehouseCode"+
	                    "&warehouseCode=" + warehouseCode +
	                    "&brandCode=" + brandCode,
	           find: function change(oXHR){ 
	           		vat.item.setGridValueByName("warehouseCode", vLineId, vat.ajax.getValue("warehouseCode", oXHR.responseText));
	           		vat.item.setGridValueByName("warehouseName", vLineId, vat.ajax.getValue("warehouseName", oXHR.responseText));
	           },
	           fail: function changeError(){
	          		vat.item.setGridValueByName("warehouseCode", vLineId, "");
	          		vat.item.setGridValueByName("warehouseName", vLineId, "-1");
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
			return "process_object_name=imGeneralAdjustmentAction&process_object_method_name=performApartInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);

				checkCustomsStatus();
				doFormAccessControl();
				vat.block.pageRefresh(vnB_Detail);
				checkUploadControl();
				getCustomsDesc();
     	}});
 	
    
}


// 依狀態鎖form
function doFormAccessControl(){



/*	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"#B.declaration" , type:"IMG"    ,value:"核銷報單",   src:"./images/button_declaration.gif" , eClick:'execExtendItemInfo()'},
	 			{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
	 			{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
	 			{name:"#B.copyDetail"  , type:"IMG"    ,value:"明細COPY",   src:"./images/detail_copy.gif", eClick:'copyDetail()'},
	 			{name:"#B.sendCustoms" , type:"IMG"    ,value:"上傳海關",   src:"./images/send_customs1.jpg", eClick:'updateCustomsStatus("")'},
*/

	var status 		= vat.item.getValueByName("#F.status");
	var processId	= vat.bean().vatBeanOther.processId;
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
//	alert(processId);
	// 初始化
	//======================<header>=============================================
	vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", false);
	vat.item.setAttributeByName("#F.reason", "readOnly", false);
	vat.item.setAttributeByName("#F.sourceOrderTypeCode", "readOnly", false);
	//======================<detail>=============================================
/*	vat.item.setGridAttributeByName("itemCode", "readOnly", false); 
	vat.item.setGridAttributeByName("warehouseCode", "readOnly", false);
	vat.item.setGridAttributeByName("lotNo", "readOnly", false);
	vat.item.setGridAttributeByName("difQuantity", "readOnly", false);
	vat.item.setGridAttributeByName("localUnitCost", "readOnly", false);
	vat.item.setGridAttributeByName("sourceItemCode", "readOnly", false);
	vat.item.setGridAttributeByName("accountCode", "readOnly", false);
	vat.item.setGridAttributeByName("reason", "readOnly", false); */
	vat.block.canGridModify([vnB_Detail], true,true,true);
	//=======================<buttonLine>========================================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	vat.item.setStyleByName("#B.declaration", 	"display", "inline");
	vat.item.setStyleByName("#B.import", 	"display", "inline");
	vat.item.setStyleByName("#B.export", 	"display", "inline");
	vat.item.setStyleByName("#B.sendCustoms", 	"display", "none");
	vat.item.setStyleByName("#B.copyDetail", 	"display", "inline");
	//===========================================================================
	if( processId != null && processId != 0 ){ //從待辦事項進入
		vat.item.setStyleByName("#B.new", 		"display", "none");
		vat.item.setStyleByName("#B.search", 	"display", "none");
		if( status == "SAVE" ){
			vat.item.setStyleByName("#B.void", 		"display", "inline"); 
		}
	}else{
		//
		if(orderNoPrefix == "TMP" ){
		
		}else{
			// 查詢回來
			vat.item.setStyleByName("#B.submit", 	"display", "none");
			vat.item.setStyleByName("#B.save", 		"display", "none"); 
			vat.item.setStyleByName("#B.message", 	"display", "none"); 
			vat.item.setStyleByName("#B.import", 	"display", "none"); 
			vat.item.setStyleByName("#B.declaration", 	"display", "none");
			vat.item.setStyleByName("#B.copyDetail", 	"display", "none");
			//======================<header>======	 =======================================
			vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", true);
			vat.item.setAttributeByName("#F.reason", "readOnly", true);
			vat.item.setAttributeByName("#F.sourceOrderTypeCode", "readOnly", true);
			vat.item.setAttributeByName("#F.remark1", "readOnly", true);
			vat.item.setAttributeByName("#F.befTotalItems", "readOnly", true);
			vat.item.setAttributeByName("#F.aftTotalItems", "readOnly", true);
			//======================<detail>=============================================
/*			vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
			vat.item.setGridAttributeByName("warehouseCode", "readOnly", true);
			vat.item.setGridAttributeByName("lotNo", "readOnly", true);
			vat.item.setGridAttributeByName("difQuantity", "readOnly", true);
			vat.item.setGridAttributeByName("localUnitCost", "readOnly", true);
			vat.item.setGridAttributeByName("sourceItemCode", "readOnly", true);
			vat.item.setGridAttributeByName("accountCode", "readOnly", true);
			vat.item.setGridAttributeByName("reason", "readOnly", true);	*/
			vat.block.canGridModify([vnB_Detail], false,false,false);
		}
	}	
	
	if( status == "FINISH" || status == "VOID" || status == "CLOSE" ){

		//======================<header>=============================================
		vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", true);
		vat.item.setAttributeByName("#F.reason", "readOnly", true);
		vat.item.setAttributeByName("#F.sourceOrderTypeCode", "readOnly", true);
		//======================<detail>=============================================
/*		vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
		vat.item.setGridAttributeByName("warehouseCode", "readOnly", true);
		vat.item.setGridAttributeByName("lotNo", "readOnly", true);
		vat.item.setGridAttributeByName("difQuantity", "readOnly", true);
		vat.item.setGridAttributeByName("localUnitCost", "readOnly", true);
		vat.item.setGridAttributeByName("sourceItemCode", "readOnly", true);
		vat.item.setGridAttributeByName("accountCode", "readOnly", true);
		vat.item.setGridAttributeByName("reason", "readOnly", true); */
		
		vat.block.canGridModify([vnB_Detail], false,false,false);
		//=======================<buttonLine>========================================
		vat.item.setStyleByName("#B.submit", 		"display", "none");
		vat.item.setStyleByName("#B.save", 			"display", "none");
		vat.item.setStyleByName("#B.message", 		"display", "none");
		vat.item.setStyleByName("#B.import", 		"display", "none");
		vat.item.setStyleByName("#B.declaration", 	"display", "none");
		vat.item.setStyleByName("#B.copyDetail", 	"display", "none");
		if(status == "FINISH" || status == "CLOSE")
		{
			vat.item.setStyleByName("#B.sendCustoms", 	"display", "inline");
		}
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

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentAction"+
        "&process_object_method_name=performTransactionForBackGroundApart";}, {bind:true, link:true, other:true});
}

// 展報單
function execExtendItemInfo(){
			$.blockUI({
	        message: '<font size=4 color="#000000"><b>報單核銷中，請稍後...</b></font>',
	        overlayCSS: { // 遮罩的css設定
	            backgroundColor: '#eee'
	        },
	        css: { // 遮罩訊息的css設定
	            border: '3px solid #aaa',
	            width: '30%',
	            left: '35%',
	            backgroundColor: 'white',
	            opacity: '0.9' //透明度，值在0~1之間
	        }
    	});		

	vat.block.pageDataSave( vnB_Detail ,{
		funcSuccess:function(){
		    vat.bean().vatBeanOther.processObjectName = "imGeneralAdjustmentService";
		    vat.bean().vatBeanOther.searchMethodName = "executeFind";
		    vat.bean().vatBeanOther.tableType = "IM_ADJUSTMENT";
		    vat.bean().vatBeanOther.searchKey = vat.item.getValueByName("#F.headId");
		    vat.bean().vatBeanOther.subEntityBeanName = "imAdjustmentLines";
		    vat.bean().vatBeanOther.itemFieldName = "itemCode";
		    vat.bean().vatBeanOther.warehouseCodeFieldName = "warehouseCode";
		    vat.bean().vatBeanOther.declTypeFieldName = "";			// 找無
		    vat.bean().vatBeanOther.declNoFieldName = "originalDeclarationNo";
		    vat.bean().vatBeanOther.declSeqFieldName = "originalDeclarationSeq";
		    vat.bean().vatBeanOther.declDateFieldName = "originalDeclarationDate";
		    vat.bean().vatBeanOther.lotFieldName = "lotNo";
		    vat.bean().vatBeanOther.qtyFieldName = "difQuantity";
		    vat.block.submit(function(){return "process_object_name=appExtendItemInfoService"+
		            "&process_object_method_name=executeExtendItem";}, {other:true, funcSuccess: function() {vat.block.pageRefresh(vnB_Detail);$.unblockUI();}});
		}
	});
}

// 明細匯入
function importFormData(){
	var width = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=IM_APART_ADJUSTMENT" +
		"&importFileType=XLS" +
        "&processObjectName=imGeneralAdjustmentService" + 
        "&processObjectMethodName=executeImportApartLists" +
        "&arguments=" + headId +
        "&parameterTypes=LONG" +
        "&blockId=" + vnB_Detail,
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 明細匯出
function exportFormData(){
//	vat.block.pageDataSave( vnB_Detail ,{ 
//		funcSuccess:function(){
			var width = "200";
		    var height = "30";
		    var headId = vat.item.getValueByName("#F.headId");
		    var url = "/erp/jsp/ExportFormData.jsp" + 
		              "?exportBeanName=IM_APART_ADJUSTMENT_EXPORT" + 
		              "&fileType=XLS" + 
		              "&processObjectName=imGeneralAdjustmentService" + 
		              "&processObjectMethodName=executeFind" + 
		              "&gridFieldName=imAdjustmentLines" + 
		              "&arguments=" + headId + 
		              "&parameterTypes=LONG";
		    
		    window.open(url, '拆併貨調整單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
//		}
//	});
}

// 上傳海關
function updateCustomsStatus(tranStatus){
	//alert("adv");
	if(confirm("此單據無註銷功能，是否要送出?")){
		vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,
          tranStatus         : tranStatus,
          customsWarehouse       : vat.item.getValueByName("#F.processCustCd"),	
          headId : vat.item.getValueByName("#F.headId")
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imGeneralAdjustmentService&process_object_method_name=updateCustomsStatus"; 
	    	},{link:true, other: true,
	    		funcSuccess:function(){
				window.top.close();
			}
    	});	
	}
   	 	 
}
// 傳參數
function doPassData(id, x){
	var suffix = "";
	switch(id){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			
//			alert("adjustmentType = " + adjustmentType);
			suffix += "&orderTypeCode="+escape(orderTypeCode); 
			break;
		case "itemCode":
			var vLineId	      = vat.item.getGridLine(x);
		  	var vItemCode       = vat.item.getGridValueByName("itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
		  	var vLotNo          = vat.item.getGridValueByName("lotNo"   , vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
		  	var vWarehouseCode  = vat.item.getGridValueByName("warehouseCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase(); 
		  	
			suffix += "&taxType=P"+
					"&storage="+escape(vStorage)+
		            "&startItemCode="+escape(vItemCode)+
		            "&endItemCode="+escape(vItemCode)+
		            "&startWarehouseCode="+escape(vWarehouseCode)+
		            "&endWarehouseCode="+escape(vWarehouseCode)+
		            "&startLotNo="+escape(vLotNo)+
		            "&endLotNo="+escape(vLotNo)+
		            "&showZero=N";
			break;	
		case "warehouseCode":

		  	var vStorage 		  = vat.item.getValueByName("#F.processCustCd").replace(/^\s+|\s+$/, '').toUpperCase();
			suffix += "&customsWarehouseCode="+escape(vStorage);
			break;	
	}
//	alert(suffix);
	return suffix;
}
function doPassDeclData(x){
//  vat.block.pageDataSave( vnB_Detail);
  var suffix = "";
  var vLineId	      = vat.item.getGridLine(x);
  var vItemCode       = vat.item.getGridValueByName("itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  var vWarehouseCode  = vat.item.getValueByName("#F.deliveryWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vCustomsWarehouseCode  = vat.item.getValueByName("#F.processCustCd").replace(/^\s+|\s+$/, '').toUpperCase();
  var vDeclarationNo  = vat.item.getGridValueByName("#F.originalDeclarationNo", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  var vDeclarationSeq = vat.item.getGridValueByName("#F.originalDeclarationSeq", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();

  //alert("LineId:"+vLineId);
  suffix += 

            "&customsItemCode="+escape(vItemCode)+
            "&warehouseCode="+escape(vWarehouseCode)+
            "&declarationNo="+escape(vDeclarationNo)+
            "&declarationSeqStart="+escape(vDeclarationSeq)+
            "&customsWarehouseCode="+escape(vCustomsWarehouseCode)+
            "&declarationSeqEnd="+escape(vDeclarationSeq);
            //"&adjustmentType=61";
 // alert(suffix);
  return suffix;
}

// do picker 回來的事件
function doAfterPickerFunctionProcess(id,x){
	//do picker back something

	var vLineId	= vat.item.getGridLine(x);
	
	switch(id){
		case "warehouseCode":
			if( typeof vat.bean().vatBeanPicker.result != 'undefined'){ 

		    	vat.item.setGridValueByName("warehouseCode", vLineId, vat.bean().vatBeanPicker.result[0]["warehouseCode"]); 
				changeLineWarehouseCode(vLineId);
			}
			break;
		case "itemCode":
			if(typeof vat.bean().vatBeanPicker.imOnHandResult != 'undefined'){ 
				vat.block.pageRefresh(vnB_Detail);
				vat.bean().vatBeanOther.lineId = vLineId; 
		  		vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
				vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentService"+
				            "&process_object_method_name=updateApartPickerData";}, 
				     {other:true,picker:true, funcSuccess: function() {vat.block.pageRefresh(vnB_Detail);}});
			}		
			break;	
	}
}
function doDeclAfterPickerProcess(id){
	//do picker back something
	if(vat.bean().vatBeanPicker.cmOnHandResult != null ){
		var vLineId	= vat.item.getGridLine(id);
		//alert("vLineId:"+vLineId);
    	vat.item.setGridValueByName("originalDeclarationNo", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationNo"]);
    	vat.item.setGridValueByName("originalDeclarationSeq", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationSeq"]);

    	//vat.item.setGridValueByName("unit", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["unit"]);
    	//vat.item.setGridValueByName("originalDeclarationDate", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["importDate"]);
	var vDecSeq = vat.item.getGridValueByName("originalDeclarationSeq"	, vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
	var vDecNo = vat.item.getGridValueByName("originalDeclarationNo"	, vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
    	//alert("declarationNo:"+vDecNo);
    	//alert("declarationSeq:"+vDecSeq);
    	vat.ajax.XHRequest(
       	{
           post:"process_object_name=cmDeclarationHeadService"+
                    "&process_object_method_name=findbyDecSeqForAJAX"+
                    "&originalDeclarationSeq=" + vDecSeq +
                    "&originalDeclarationNo=" + vDecNo,
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               vat.item.setGridValueByName("unit", vLineId, vat.ajax.getValue("unit", oXHR.responseText));
        }  
       });
	}
}
function changeDeclarationSeq()
{
    var same = 0;//0:不同　1：相同
    
	var nItemLine = vat.item.getGridLine();
	var vDecSeq = vat.item.getGridValueByName("originalDeclarationSeq"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vDecNo = vat.item.getGridValueByName("originalDeclarationNo"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();	

	if(same == 0){
	
		vat.item.setGridValueByName("originalDeclarationSeq", nItemLine, vDecSeq);
		vat.item.setGridValueByName("originalDeclarationNo", nItemLine, vDecNo);
		
		vat.ajax.XHRequest(
       	{
           post:"process_object_name=cmDeclarationHeadService"+
                    "&process_object_method_name=findbyDecSeqForAJAX"+
                    "&originalDeclarationSeq=" + vDecSeq +
                    "&originalDeclarationNo=" + vDecNo,
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               vat.item.setGridValueByName("unit", nItemLine, vat.ajax.getValue("unit", oXHR.responseText));
        }   
       });
      
     }

}
function changeCustomsWarehouse(){
	if(vat.item.getValueByName("#F.processCustCd")==='KW'){
			vat.item.setValueByName("#F.resStoreCode", "AD910");
	}else{
		vat.item.setValueByName("#F.resStoreCode", "F9900");
	}


}
function copyDetail(){

	var headId =  vat.item.getValueByName("#F.headId");
	var loginEmployeeCode =  document.forms[0]["#loginEmployeeCode" ].value;
vat.block.pageDataSave( vnB_Detail );
	   	var alertMessage ="是否確定送出?";

		if(confirm(alertMessage))
		{
		
			//alert("明細複製");
		   vat.ajax.XHRequest({
		          asyn:false,
		          post:"process_object_name=imGeneralAdjustmentService"+
		                   "&process_object_method_name=updateCopyDetail"
							+"&headId="+headId
							+"&loginEmployee="+loginEmployeeCode,
		          find: function change(oXHR){ 
						alert("明細複製完畢");
		          		vat.item.setValueByName("#F.befTotalItems", vat.ajax.getValue("befTotalItems", oXHR.responseText));
		          		vat.item.setValueByName("#F.aftTotalItems", vat.ajax.getValue("aftTotalItems", oXHR.responseText));
		          		vat.block.pageRefresh(vnB_Detail);
		          		
		          } 
			});	
		}
}

