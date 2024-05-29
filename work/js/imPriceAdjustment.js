
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;
var vnB_Total = 4;

function kweAdjustmentBlock(){
	formInitial();
	buttonLine();
	headerInitial();
  
	if (typeof vat.tabm != 'undefined') {
		var status = vat.item.getValueByName("#F.status");
		
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0,"xTab1","商品資料"   ,"vatItemDiv"                   ,"images/tab_item_data_dark.gif"            ,"images/tab_item_data_light.gif" , false, "doPageRefresh()");
		vat.tabm.createButton(0,"xTab2","簽核資料"   ,"vatApprovalDiv"             ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif",  status == "SAVE" || status == "UNCONFIRMED" ? "none" : "inline"); // 
	}
	detailInitialPAP();
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
	           vat.item.getValueByName("#F.orderTypeCode"), 
	           vat.item.getValueByName("#F.orderNo"),
	           document.forms[0]["#loginEmployeeCode"].value );
	           
	doFormAccessControl();           

}

function formInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	 loginBrandCode	    = document.forms[0]["#loginBrandCode"    ].value;  	
  	 loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" ].value;	  
  	 orderTypeCode      = document.forms[0]["#orderTypeCode"     ].value; 
     processId          = document.forms[0]["#processId"         ].value;       
	 formId             = document.forms[0]["#formId"            ].value;
	 assignmentId       = document.forms[0]["#assignmentId"      ].value;
	 
	 vat.bean().vatBeanOther={
	 					loginBrandCode:loginBrandCode,					
					  	processId : processId,
					  	loginEmployeeCode : loginEmployeeCode,
					  	orderTypeCode:orderTypeCode,
					    formId :formId,
					    assignmentId : assignmentId,
					    isGoodsController : document.forms[0]["#isGoodsController"      ].value
	 };
     vat.bean.init(function(){
		return "process_object_name=imPriceAdjustmentAction&process_object_method_name=performInitial"; 
     },{other: true});
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
	 									 service:"Im_ItemPriceAdjustment:search:20090814.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 servicePassData:function(){ return doPassData("buttonLine");},   
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},						 
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.message"    , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},	 			
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
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
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){
	
	var allOrderTypes = vat.bean("allOrderTypes");
	var allPriceTypes = vat.bean("allPriceTypes");
	var allCurrencys = vat.bean("allCurrencys");
	
	var orderTypeCode = vat.bean().vatBeanOther.orderTypeCode;
	
	var titleName = "";

	if("PAP" == orderTypeCode){
		titleName = "新品訂價維護作業";
	}else if("PAJ" == orderTypeCode){
		titleName = "商品變價維護作業";
	}

	vat.block.create(vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:titleName, rows:[  
		 {row_style:"", cols:[
			 {items:[{name:"#L_orderType", type:"LABEL" , value:"單別"}]},	 
			 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode",init:allOrderTypes, mode:"READONLY"}]},		 
			 {items:[{name:"#L_orderNo"  , type:"LABEL" , value:"單號"},
			 		{name:"#formId"   , type:"TEXT"  ,  bind:"formId", back:false, size:10, mode:"HIDDEN" }]},
			 {items:[{name:"#F.orderNo"  , type:"TEXT"  ,  bind:"orderNo", size:20, mode:"READONLY"}
			 		,{name:"#F.headId"   , type:"TEXT"  ,  bind:"headId", back:false, size:10, mode:"HIDDEN" }]},
			 {items:[{name:"#L_enableDate", type:"LABEL",  value:"啟用日期<font color='red'>*</font>"}]},
			 {items:[{name:"#F.enableDate", type:"DATE",  bind:"enableDate", size:1}]},
			 {items:[{name:"#L_priceType", type:"LABEL",  value:"價格類型<font color='red'>*</font>"}]},
			 {items:[{name:"#F.priceType", type:"SELECT",  bind:"priceType", size:1,init:allPriceTypes}]},		 
			 {items:[{name:"#L_brandCode", type:"LABEL" , value:"品牌"}]},
			 {items:[{name:"#F.brandCode", type:"TEXT"  ,  bind:"brandCode", size:8, mode:"HIDDEN"},
			 		  {name:"#F.brandName", type:"TEXT"  ,  bind:"brandName", size:8, mode:"READONLY"}]},
			 {items:[{name:"#L_status"   , type:"LABEL" , value:"狀態"}]},	 		 
			 {items:[{name:"#F.statusName", type:"TEXT"  ,  bind:"statusName", size:8, mode:"READONLY"},
			 		{name:"#F.status"   , type:"TEXT"  ,  bind:"status", size:12, mode:"HIDDEN"}]}		   
			 ]},
		 {row_style:"", cols:[
			 {items:[{name:"#L_description", type:"LABEL", value:"說明"}]},
			 {items:[{name:"#F.description" , type:"TEXT",   bind:"description", size:50, maxLen:200,desc:"一般說明"}], td:" colSpan=7"},
			 {items:[{name:"#L_createdBy", type:"LABEL", value:"填單人員"}]},
			 {items:[{name:"#F.createdBy" , type:"TEXT",   bind:"createdBy",  mode:"HIDDEN", size:12},
			 		{name:"#F.createdByName" , type:"TEXT",   bind:"createdByName",  mode:"READONLY", size:12}]},	 
			 {items:[{name:"#L_creationDate" , type:"LABEL", value:"填單日期"}]},
		 	 {items:[{name:"#F.creationDate" , type:"TEXT",   bind:"creationDate", mode:"READONLY", size:12}]}
		 ]}		
		],	 
		 beginService:"",
		 closeService:""			
	});
	
    vat.block.create(vnB_Master, {
		id: "vatBlock_secondHead",generate: true, table:"cellspacing='1' class='' border='0' cellpadding='2'",
		title:"", rows:[
		 {row_style:"", cols:[
			 {items:[{name:"#L_supplierCode", type:"LABEL" , value:"廠商代號"}]},	 
			 {items:[{name:"#F.supplierCode", type:"TEXT",  bind:"supplierCode", eChange: function(){ changeSupplierName("supplierCode"); } ,size:20}]},
			 {items:[{name:"#B.supplierCode",	value:"選取" ,type:"PICKER" ,
			 									 		openMode:"open", src:"./images/start_node_16.gif",
			 									 		service:"Bu_AddressBook:searchSupplier:20091011.page", 
			 									 		left:0, right:0, width:1024, height:768,	
			 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess();} },
				 	{name:"#F.addressBookId", 		type:"TEXT",  	bind:"addressBookId", back:false, mode:"HIDDEN"},								 		
					{name:"#F.supplierName", type:"TEXT",  	bind:"supplierName", back:false ,size:30, mode:"READONLY"}]},
			 {items:[{name:"#L_salesPeriod"  , type:"LABEL" , value:"銷售期間"}]},
			 {items:[{name:"#F.salesPeriod"  , type:"TEXT"  ,  bind:"salesPeriod", size:12}]},			 
			 {items:[{name:"#L_currencyCode", type:"LABEL",  value:"幣別"}]},
			 {items:[{name:"#F.currencyCode", type:"SELECT",  bind:"currencyCode", size:1,init:allCurrencys,onchange:"setExchangeRate()"}]},//
			 {items:[{name:"#L_exchangeRate", type:"LABEL",  value:"匯率"}]},
			 {items:[{name:"#F.exchangeRate", type:"NUMB",  bind:"exchangeRate", size:8}]},
	 
	 		 getColumn(orderTypeCode, {items:[{name:"#L_ratio", type:"LABEL" , value:"比例"}]} ),
	 		 getColumn(orderTypeCode, {items:[{name:"#F.ratio", type:"NUMB"  ,  bind:"ratio", size:8,onchange:"setExchangeRate()"}]} )
	 		 
	 
//			 {items:[{name:"#L_ratio", type:"LABEL" , value:"比例"}]},
//			 {items:[{name:"#F.ratio", type:"NUMB"  ,  bind:"ratio", size:8}]}
	 	 ]}
	 	],
	 
	 beginService:"",
	 closeService:""			
	});
}

// 取得預期要顯示欄位
function getColumn(orderTypeCode, column){
	if("PAP" == orderTypeCode){
		return column;
	}else if("PAJ" == orderTypeCode){
		return {};
	}
}

// 商品訂價
function detailInitialPAP() {
	var statusTmp = vat.item.getValueByName("#F.status");
	var orderType = vat.item.getValueByName("#F.orderTypeCode");
//	var modeType="mode";
	if(""==orderType){
		orderType="PAP";
	}
	
	var varCanDataDelete = true;
	var varCanDataAppend = true;
	var varCanDataModify = true;
//	if( statusTmp == "SAVE" || statusTmp == "REJECT" || statusTmp == "UNCONFIRMED" || statusTmp == "VOID" ){
//		varCanDataDelete = true;
//		varCanDataAppend = true;
//		varCanDataModify = true;		
//	}

	vat.item.make(vnB_Detail, "indexNo", {type:"IDX", desc:"序號"}); 
	if(orderType=="PAP"){
		vat.item.make(vnB_Detail, "category01", {type:"TEXT", size:20, maxLen:20, desc:"系列", mode:"READONLY"});
		vat.item.make(vnB_Detail, "itemCode", {type:"TEXT", size:16, maxLen:20, desc:"品號", onchange:"onChangeItemCode('-1')"});// 
		vat.item.make(vnB_Detail, "searchItem",  {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 			service:"Im_ItemPriceAdjustment:searchPriceView:20091225.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			servicePassData:function(){ return doPassData("PAP"); },
	 									 			serviceAfterPick:function(id){doAfterPickerLineFunctionProcess(id); } }); 
		
		vat.item.make(vnB_Detail, "itemCName", {type:"TEXT", size:20, maxLen:20, desc:"中文名稱", mode:"READONLY"});                                   
		vat.item.make(vnB_Detail, "foreignCost", {type:"NUMB", size:8, maxLen:20, desc:"原幣成本", mode:"READONLY"}); 
		vat.item.make(vnB_Detail, "localCost", {type:"NUMB", size:8, maxLen:20, desc:"台幣成本",onchange:"calculateLineRate()"});//   
		vat.item.make(vnB_Detail, "unitPrice", {type:"NUMB", size:8, maxLen:20, desc:"送簽價格", onchange:"calculateLineRate()"});//
		vat.item.make(vnB_Detail, "costRate", {type:"NUMB", size:8, maxLen:20, desc:"成本率", mode:"READONLY"});
		vat.item.make(vnB_Detail, "priceId", {type:"NUMB", size:8, maxLen:20, desc:"ID", mode:"READONLY"});
	}else if (orderType=="PAJ"){
		vat.item.make(vnB_Detail, "itemCode", {type:"TEXT", size:16, maxLen:20, desc:"品號", onchange:"onChangeItemCode('-1')"});    
		vat.item.make(vnB_Detail, "searchItem", {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 			service:"Im_ItemPriceAdjustment:searchPriceView:20091225.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			servicePassData:function(){ return doPassData("PAJ"); },
	 									 			serviceAfterPick:function(id){doAfterPickerLineFunctionProcess(id); } }); 
		
		vat.item.make(vnB_Detail, "itemCName", {type:"TEXT", size:20, maxLen:20, desc:"中文名稱", mode:"READONLY"});                                   
		vat.item.make(vnB_Detail, "originalQuotationPrice", {type:"NUMB", size:8, maxLen:20, desc:"廠商報價(原)", mode:"READONLY"}); 
		vat.item.make(vnB_Detail, "newQuotationPrice", {type:"NUMB", size:8, maxLen:20, desc:"廠商報價(新)",onchange:"calculateLineRate()"});  
		vat.item.make(vnB_Detail, "originalPrice", {type:"NUMB", size:8, maxLen:20, desc:"原價", mode:"READONLY"});   
		vat.item.make(vnB_Detail, "unitPrice", {type:"NUMB", size:8, maxLen:20, desc:"送簽價格", onchange:"calculateLineRate()"});
		vat.item.make(vnB_Detail, "grossProfitRate", {type:"NUMB", size:8, maxLen:20, desc:"毛利率差異(%)", mode:"READONLY"});
		vat.item.make(vnB_Detail, "priceId", {type:"NUMB", size:8, maxLen:20, desc:"ID", mode:"HIDDEN"});
		vat.item.make(vnB_Detail, "isTax", {type:"TEXT", size:8, maxLen:20, desc:"isTax", mode:"HIDDEN"});
		vat.item.make(vnB_Detail, "typeCode", {type:"TEXT", size:8, maxLen:20, desc:"typeCode", mode:"HIDDEN"});
		vat.item.make(vnB_Detail, "taxCode", {type:"TEXT", size:8, maxLen:20, desc:"taxCode", mode:"HIDDEN"});
	}
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});                                         
	vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});                                                          
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});                                                                  
	vat.block.pageLayout(vnB_Detail, {	id: "vatItemDiv",
								pageSize: 10,
	                            canGridDelete:varCanDataDelete,
								canGridAppend:varCanDataAppend,
								canGridModify:varCanDataModify,														
							    appendBeforeService : "appendBeforeService()",
							    appendAfterService  : "appendAfterService()",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "loadSuccessAfter()",
								eventService        : "changeRelationData",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

// 設定匯率
function setExchangeRate(){
	var headId = vat.item.getValueByName("#F.headId");
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	var currencyCode = vat.item.getValueByName("#F.currencyCode");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var ratio;
	
	if("PAP" == orderTypeCode){
		ratio = vat.item.getValueByName("#F.ratio");
	}else if("PAJ" == orderTypeCode){
		ratio = "1";
	}
	
	var processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=updateAJAXLineExchangeRate" + 
						"&currencyCode=" + currencyCode + "&brandCode="+ brandCode + "&headId="+ headId +
						"&orderTypeCode=" + orderTypeCode + 
						"&ratio=" + ratio;
	
	vat.block.pageDataSave( vnB_Detail ,{ 
		funcSuccess:function(){
			vat.ajax.XHRequest({
	        	post:processString,
	        	find: function change(oXHR){ 
	        		vat.item.setValueByName("#F.exchangeRate" ,vat.ajax.getValue("exchangeRate", oXHR.responseText));
	        		vat.item.setValueByName("#F.ratio" ,vat.ajax.getValue("ratio", oXHR.responseText));
					vat.block.pageRefresh(vnB_Detail);
	        	}
	        });            
		}
    });
}

// 撈 LINE ITEM CODE 
function onChangeItemCode( priceId , id) {
	var nItemLine = vat.item.getGridLine(id);
	var sItemCode = vat.item.getGridValueByName("itemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var sLineId = vat.item.getGridValueByName("lineId",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	if ( priceId != "-1" || sItemCode != "" ) {
//		alert("after\npriceId = " + priceId + "\nsItemCode = " + sItemCode);
		var processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXLineData"+ 
							"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
							"&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode") + 
							"&itemCode=" + (priceId ==="-1" ? sItemCode : "") +
							"&lineId=" + sLineId +
							"&head_id="+vat.item.getValueByName("#F.headId")+
						 	"&exchangeRate="+vat.item.getValueByName("#F.exchangeRate")+
						 	"&ratio="+vat.item.getValueByName("#F.ratio")+
						 	"&priceType="+vat.item.getValueByName("#F.priceType") +
						 	"&priceId="+(priceId ==="-1" ? "" : priceId);
		vat.ajax.XHRequest({	
			post:processString,					 	
			find: function change(oXHR){ 
				if("PAP"==orderTypeCode){
					if( priceId != "-1" ){
						vat.item.setGridValueByName("itemCode",nItemLine, vat.ajax.getValue("itemCode",oXHR.responseText));
					}else{ // 表示手打的不存在
						vat.item.setGridValueByName("itemCode",nItemLine, sItemCode.replace(/^\s+|\s+$/, '').toUpperCase() );
					}
					vat.item.setGridValueByName("category01",nItemLine, vat.ajax.getValue("Category01",oXHR.responseText));
					vat.item.setGridValueByName("unitPrice", nItemLine,  vat.ajax.getValue("UnitPrice", oXHR.responseText));
					vat.item.setGridValueByName("foreignCost",nItemLine,  vat.ajax.getValue("ForeignCost", oXHR.responseText));
					vat.item.setGridValueByName("localCost",nItemLine,  vat.ajax.getValue("LocalCost", oXHR.responseText));
					vat.item.setGridValueByName("itemCName",nItemLine,  vat.ajax.getValue("ItemCName", oXHR.responseText));
					vat.item.setGridValueByName("costRate",nItemLine,  vat.ajax.getValue("CostRate", oXHR.responseText));
					vat.item.setGridValueByName("priceId",nItemLine,  vat.ajax.getValue("priceId", oXHR.responseText));
				}else if("PAJ"==orderTypeCode){
					if( priceId != "-1" ){
						vat.item.setGridValueByName("itemCode",nItemLine, vat.ajax.getValue("itemCode",oXHR.responseText));
					}else{ // 表示手打的不存在
						vat.item.setGridValueByName("itemCode",nItemLine, sItemCode.replace(/^\s+|\s+$/, '').toUpperCase() );
					}
					vat.item.setGridValueByName("originalPrice",nItemLine, vat.ajax.getValue("OrginalPrice",oXHR.responseText));
					vat.item.setGridValueByName("originalQuotationPrice",nItemLine, vat.ajax.getValue("OrginalQuotationPrice",oXHR.responseText));
					vat.item.setGridValueByName("newQuotationPrice",nItemLine, vat.ajax.getValue("newQuotationPrice",oXHR.responseText));	
					vat.item.setGridValueByName("unitPrice", nItemLine,  vat.ajax.getValue("UnitPrice", oXHR.responseText));
					vat.item.setGridValueByName("itemCName",nItemLine,  vat.ajax.getValue("ItemCName", oXHR.responseText));
					vat.item.setGridValueByName("grossProfitRate",nItemLine,  vat.ajax.getValue("GrossProfitRate", oXHR.responseText));
					vat.item.setGridValueByName("priceId",nItemLine,  vat.ajax.getValue("priceId", oXHR.responseText));
					vat.item.setGridValueByName("isTax",nItemLine,  vat.ajax.getValue("isTax", oXHR.responseText));
					vat.item.setGridValueByName("typeCode",nItemLine,  vat.ajax.getValue("typeCode", oXHR.responseText));
					vat.item.setGridValueByName("taxCode",nItemLine,  vat.ajax.getValue("taxCode", oXHR.responseText));
				}						
			}
		});
	} else {
	}

}
// 換算毛利率差異
function calculateLineRate() {
	var nItemLine = vat.item.getGridLine();                                         
	var processString="";
	var orderTypeCodeForm = vat.item.getValueByName("#F.orderTypeCode");
	if(orderTypeCodeForm=="PAJ"){
	  var originalPrice = vat.item.getGridValueByName("originalPrice", nItemLine);//parseInt(document.forms[0]["#form.imPriceLists["+lineId+"].originalPrice"].value);  
		var exchangeRate = vat.item.getValueByName("#F.exchangeRate");
		var originalQuotationPrice = vat.item.getGridValueByName("originalQuotationPrice",nItemLine);
		var unitPrice = vat.item.getGridValueByName("unitPrice",nItemLine); 
		var newQuotationPrice = vat.item.getGridValueByName("newQuotationPrice",nItemLine);
		processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXGrossProfitRate";
		processString += "&originalPrice="+originalPrice+"&originalQuotationPrice="+originalQuotationPrice+"&exchangeRate="+exchangeRate+"&newQuotationPrice="+newQuotationPrice;
		processString += "&unitPrice="+unitPrice;
  
	} else if(orderTypeCodeForm =="PAP"){
	 
	  var localCost = vat.item.getGridValueByName("localCost",nItemLine);  
	  var unitPrice = vat.item.getGridValueByName("unitPrice",nItemLine);
//	  alert(localCost+"--"+unitPrice);   
	     processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXCalcCostRate&localCost="+localCost;
	     processString +="&unitPrice="+unitPrice;    
	}
	 vat.ajax.startRequest(processString,  function() { 
	    if (vat.ajax.handleState()){
	
	      if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
	      		if(orderTypeCodeForm == "PAP"){
	      			vat.item.setGridValueByName("costRate",nItemLine,vat.ajax.getValue("CostRate", vat.ajax.xmlHttp.responseText+"%"));	
	      		}else{
	      			vat.item.setGridValueByName("grossProfitRate",nItemLine,vat.ajax.getValue("GrossProfitRate", vat.ajax.xmlHttp.responseText));
	      		}
	      }else{
	      		if(orderTypeCodeForm == "PAP"){
	      			vat.item.setGridValueByName("costRate",nItemLine,"0%");
	      		}else{
	      			vat.item.setGridValueByName("grossProfitRate",nItemLine,"0");
	      		}
	      }
	  }
	} );
}

// 載入明細後
function loadSuccessAfter(){
	// alert("載入成功");	
//    vat.block.pageRefresh(vnB_Detail);
}

// 新增空白頁
function appendBeforeService(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}

// 新增空白頁之後
function appendAfterService(){
	// return alert("新增完畢");
}

//	LINE資料 第一次載入或更新後的讀取
function loadBeforeAjxService() {
	
	var processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXPageData" +
			"&headId=" + vat.item.getValueByName("#F.headId")+
			"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") + 
			"&priceType=" + vat.item.getValueByName("#F.priceType");
	return processString;
}

/*
	載入LINE資料 SUCCESS
*/
function loadSuccessAfter() {
}
/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=updateAJAXPageLinesData" + 
						"&headId=" + vat.item.getValueByName("#F.headId")+ 
						"&status=" + vat.item.getValueByName("#F.status") + 
						"&brandCode=" + vat.item.getValueByName("#F.brandCode")  + 
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode;
		
	}
	return processString;
}
/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
//	vat.block.pageRefresh(vnB_Detail);
	
/*	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg === "") {
		if ("saveHandler" == afterSavePageProcess) {	
			executeCommandHandler("main", "saveHandler");
		} else if ("submitHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "submitHandler");
		}else if ("voidHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "voidHandler");
		}else if("executeExport"==afterSavePageProcess){
			exportFormData();
		}else if("executeImport"==afterSavePageProcess){
			importFormData();
		}
	}else{
		alert("錯誤訊息： " + errorMsg);
	}
	afterSavePageProcess = "";*/
}
/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
	//alert("doSaveHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			vat.block.pageDataSave(0);
			afterSavePageProcess = "saveHandler";
		}
	}
}

function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SIGNING" == formAction){
		alertMessage = "是否確定送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
			alertMessage = "是否確定背景送出?";
	}
		
	if(confirm(alertMessage)){
		var formId = vat.item.getValueByName("#formId").replace(/^\s+|\s+$/, '');;
	    var processId = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
	    var status = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');;
	    var employeeCode = vat.bean().vatBeanOther.loginEmployeeCode.replace(/^\s+|\s+$/, '');;
	    var headId = vat.item.getValueByName("#F.headId");
	    var inProcessing   	= !(processId == null || processId == ""  || processId == 0);
	    var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	    var approvalResult 	= getApprovalResult();
	    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	    
//	    alert("approvalResult = " + approvalResult + "\napprovalComment = " + approvalComment);
	    
		if((orderNoPrefix == "TMP" &&  status == "SAVE") || status == "UNCONFIRMED" ||
			(inProcessing   && (status == "SAVE"  || status == "SIGNING" || status == "REJECT" ))){
				vat.block.pageDataSave(vnB_Detail,{  
					funcSuccess:function(){
						vat.bean().vatBeanOther.beforeStatus = status;
					  	vat.bean().vatBeanOther.processId = processId;
					  	vat.bean().vatBeanOther.formAction = formAction;
					  	vat.bean().vatBeanOther.loginEmployeeCode = employeeCode;
					  	vat.bean().vatBeanOther.approvalResult = approvalResult;
					  	vat.bean().vatBeanOther.approvalComment = approvalComment;	
					  				
						if("SUBMIT_BG" == formAction){
					      	vat.block.submit(function(){return "process_object_name=imPriceAdjustmentAction"+
					                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
					    }else{
							vat.block.submit(function(){return "process_object_name=imPriceAdjustmentAction"+
					                    "&process_object_method_name=performTransaction";},{
					                    bind:true, link:true, other:true,
					                    funcSuccess:function(){
							        		vat.block.pageRefresh(vnB_Detail);
							        	}}
							);
				        } 			                    
		          	}
	          	}
	          );
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

function checkWareHouseNames(){
	var isOk= true;
	var wareHouseNames = $('div[@id^=wareHouseNames]');
	for(var j = 0; j< wareHouseNames.length; j++){
		if('查無庫號' == wareHouseNames[j].innerText){
			isOk = false;
		}
	}
	if(!isOk){
		alert('庫號輸入有誤，請重新輸入');
	}
	return isOk;
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {

	return true ;
}
function appendBeforeService() {
	
	return true;
}
function appendAfterService() {

}
function doBeforePicker(){
	vat.formD.pageDataSave(vnB_Detail);
}
function doPageDataSave(){
    vat.block.pageDataSave(vnB_Detail);
}

function doPageRefresh(){
    vat.block.pageSearch(vnB_Detail);
}
/*
	匯出
*/
function doExport() {
	//save line
	afterSavePageProcess = "executeExport";
	vat.block.pageSearch(vnB_Detail);	
}

/*
	匯入
*/
function doImport() {
	//save line
	afterSavePageProcess = "executeImport";
	vat.block.pageSearch(vnB_Detail);	
}
function exportFormData(){
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IM_PRICE_ADJUSTMENT" + 
              "&fileType=XLS" + 
              "&processObjectName=imPriceAdjustmentMainService" + 
              "&processObjectMethodName=findById" + 
              "&gridFieldName=imPriceLists" + 
              "&arguments=" + vat.item.getValueByName("#F.headId") + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '定價變價單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			suffix += "&orderTypeCode="+escape(orderTypeCode); 
			break;
		case "PAP":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			var priceType = vat.item.getValueByName("#F.priceType");
			var headId = vat.item.getValueByName("#F.headId");
			var exchangeRate = vat.item.getValueByName("#F.exchangeRate");
			var ratio = vat.item.getValueByName("#F.ratio");
			suffix += "&headId="+ headId + "&orderTypeCode=" + escape(orderTypeCode) + "&priceStatus="+escape("N")+ "&priceType="+escape(priceType)+ "&exchangeRate=" + exchangeRate + "&ratio=" + ratio; 
			break;	
		case "PAJ":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"); 
			var priceType = vat.item.getValueByName("#F.priceType");
			var headId = vat.item.getValueByName("#F.headId");
			var exchangeRate = vat.item.getValueByName("#F.exchangeRate");
			var ratio = vat.item.getValueByName("#F.ratio");
			suffix += "&headId="+ headId + "&orderTypeCode=" + escape(orderTypeCode) + "&priceStatus="+escape("Y")+ "&priceType="+escape(priceType)+ "&exchangeRate=" + exchangeRate + "&ratio=" + ratio; 
			break;	
	}
	return suffix;
}

// 供應商picker 回來執行
function doAfterPickerFunctionProcess(){
	//do picker back something
	if(typeof vat.bean().vatBeanPicker.result != "undefined"){
    	vat.item.setValueByName("#F.addressBookId", vat.bean().vatBeanPicker.result[0].addressBookId); 
		changeSupplierName("addressBookId");
	}
}

// 定變價 line picker 回來執行
function doAfterPickerLineFunctionProcess(id){
	if( typeof vat.bean().vatBeanPicker.result != "undefined" ){
//		if( vat.bean().vatBeanPicker.result[0].priceId != "" ){
//			alert( "vat.bean().vatBeanPicker.result.length =" + vat.bean().vatBeanPicker.result.length);
//			alert("doAfterPickerLineFunctionProcess before");
			vat.block.pageSearch(vnB_Detail);
//			alert("doAfterPickerLineFunctionProcess after");
//			onChangeItemCode(vat.bean().vatBeanPicker.result[0].priceId, id);
//		}
	}
}

// 動態改變供應商名稱
function changeSupplierName( code ){
//	alert( code + "\n" + vat.item.getValueByName("#F.addressBookId") +"\n" + vat.item.getValueByName("#F.supplierCode") );

	vat.ajax.XHRequest({
		post:"process_object_name=buSupplierWithAddressViewService"+
                  "&process_object_method_name=getAJAXSupplierName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&addressBookId=" + ( "addressBookId" === code ? vat.item.getValueByName("#F.addressBookId") : "" )+
                  "&supplierCode=" + ( "supplierCode" === code ? vat.item.getValueByName("#F.supplierCode") : "" ),
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName("#F.supplierCode", vat.ajax.getValue("supplierCode", oXHR.responseText));
         		vat.item.setValueByName("#F.supplierName", vat.ajax.getValue("supplierName", oXHR.responseText) );
         		vat.item.setValueByName("#F.currencyCode", vat.ajax.getValue("currencyCode", oXHR.responseText) );
         		setExchangeRate();
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.supplierName", "查無此供應商");
		}   
	});	
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(typeof vat.bean().vatBeanPicker.result != "undefined"){
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

function importFormData(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=IM_PRICE_ADJUSTMENT" +
		"&importFileType=XLS" +
        "&processObjectName=imPriceAdjustmentMainService" + 
        "&processObjectMethodName=executeImportT1PriceLists" +
        "&arguments=" + vat.item.getValueByName("#F.headId") + "{$}" +
        			   vat.item.getValueByName("#F.brandCode") + "{$}" +
        			   vat.item.getValueByName("#F.orderTypeCode") + "{$}" +
        			   vat.item.getValueByName("#F.priceType") + "{$}" +
        			   vat.item.getValueByName("#F.exchangeRate") +
        "&parameterTypes=LONG{$}STRING{$}STRING{$}STRING{$}STRING" +
        "&blockId="+ vnB_Detail,
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 送出後更新
function createRefreshForm(){
	refreshForm("");
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

// 刷新頁面
function refreshForm(vsHeadId){
	document.forms[0]["#processId"         ].value = "";       
	document.forms[0]["#formId"            ].value = vsHeadId;       	
	document.forms[0]["#assignmentId"      ].value = "";     
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"            ].value;	
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	//alert("currentRecordNumber:"+vat.bean().vatBeanOther.currentRecordNumber);
	vat.block.submit(
		function(){
			return "process_object_name=imPriceAdjustmentAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     		funcSuccess:function(){
    			vat.item.bindAll();
     		             
     			refreshWfParameter(vat.item.getValueByName("#F.brandCode"), 
           			vat.item.getValueByName("#F.orderTypeCode"), 
        			vat.item.getValueByName("#F.orderNo"));
	   			vat.block.pageRefresh(102);		 
	   			vat.block.pageRefresh(vnB_Detail);
				vat.tabm.displayToggle(0, "xTab2", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
     			doFormAccessControl();
     	}});
	

}

// 依狀態鎖form 
function doFormAccessControl(){
	var status 		= vat.item.getValueByName("#F.status");
	var processId	= vat.bean().vatBeanOther.processId;
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	
	var formId = vat.bean().vatBeanOther.formId;
	
	var canBeClaimed	= document.forms[0]["#canBeClaimed"].value;
	// 初始化
	//======================<header>=============================================
	vat.item.setAttributeByName("#F.enableDate", "readOnly", false);
	vat.item.setAttributeByName("#F.priceType", "readOnly", false);
	vat.item.setAttributeByName("#F.description", "readOnly", false);
	vat.item.setAttributeByName("#F.supplierCode", "readOnly", false); 
	vat.item.setAttributeByName("#B.supplierCode", "readOnly", false);
	vat.item.setAttributeByName("#F.salesPeriod", "readOnly", false);
	vat.item.setAttributeByName("#F.currencyCode", "readOnly", false);
	vat.item.setAttributeByName("#F.exchangeRate", "readOnly", false);
	vat.item.setAttributeByName("#F.ratio", "readOnly", false);
	
	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true);
	//======================<detail>=============================================
/*	if(orderTypeCode=="PAP"){
		vat.item.setGridAttributeByName("localCost", "readOnly", false); 
	}else if(orderTypeCode=="PAJ"){
		vat.item.setGridAttributeByName("newQuotationPrice", "readOnly", false);
	}
	vat.item.setGridAttributeByName("itemCode", "readOnly", false); 
	vat.item.setGridAttributeByName("searchItem", "readOnly", false); 
	vat.item.setGridAttributeByName("unitPrice", "readOnly", false);
	vat.item.setGridAttributeByName("isDeleteRecord", "readOnly", false);*/
	
	vat.block.canGridModify([vnB_Detail], true,true,true);
	//=======================<buttonLine>========================================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.import", 	"display", "inline");
	vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	
	//===========================================================================
	if(formId != ""){
		// 流程內
		if( processId != null && processId != 0 ){ //從待辦事項進入
			vat.item.setStyleByName("#B.new", 		"display", "none");
			vat.item.setStyleByName("#B.search", 	"display", "none");
			if( status == "SAVE" || status == "REJECT"){
				vat.item.setStyleByName("#B.void", 		"display", "inline"); 
			}else if( status == "SIGNING" ){
			  	// 是否商控
				if("true" == document.forms[0]["#isGoodsController"      ].value ){
					vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
					vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
				}else{
					vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
					vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
				}
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
				vat.item.setAttributeByName("#F.enableDate", "readOnly", true);
				vat.item.setAttributeByName("#F.priceType", "readOnly", true);
				vat.item.setAttributeByName("#F.description", "readOnly", true);
				vat.item.setAttributeByName("#F.supplierCode", "readOnly", true); 
				vat.item.setAttributeByName("#B.supplierCode", "readOnly", true); 
				vat.item.setAttributeByName("#F.salesPeriod", "readOnly", true);
				vat.item.setAttributeByName("#F.currencyCode", "readOnly", true);
				vat.item.setAttributeByName("#F.exchangeRate", "readOnly", true);
				vat.item.setAttributeByName("#F.ratio", "readOnly", true);
				//======================<detail>=============================================
/*				if(orderTypeCode=="PAP"){
					vat.item.setGridAttributeByName("localCost", "readOnly", true); 
				}else if(orderTypeCode=="PAJ"){
					vat.item.setGridAttributeByName("newQuotationPrice", "readOnly", true);
				}
				vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
				vat.item.setGridAttributeByName("searchItem", "readOnly", true);
				vat.item.setGridAttributeByName("unitPrice", "readOnly", true);
				vat.item.setGridAttributeByName("isDeleteRecord", "readOnly", true);*/
				
				vat.block.canGridModify([vnB_Detail], false,false,false);
			}	
		}
	}
	
	if( status == "SIGNING" || status == "FINISH" || status == "VOID" ){
		
		//======================<header>=============================================
		vat.item.setAttributeByName("#F.enableDate", "readOnly", true);
		vat.item.setAttributeByName("#F.priceType", "readOnly", true);
		vat.item.setAttributeByName("#F.description", "readOnly", true);
		vat.item.setAttributeByName("#F.supplierCode", "readOnly", true); 
		vat.item.setAttributeByName("#B.supplierCode", "readOnly", true); 
		vat.item.setAttributeByName("#F.salesPeriod", "readOnly", true);
		vat.item.setAttributeByName("#F.currencyCode", "readOnly", true);
		vat.item.setAttributeByName("#F.exchangeRate", "readOnly", true);
		vat.item.setAttributeByName("#F.ratio", "readOnly", true);
		//======================<detail>=============================================
/*		if(orderTypeCode=="PAP"){
			vat.item.setGridAttributeByName("localCost", "readOnly", true); 
		}else if(orderTypeCode=="PAJ"){
			vat.item.setGridAttributeByName("newQuotationPrice", "readOnly", true);
		}
		vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
		vat.item.setGridAttributeByName("searchItem", "readOnly", true);
		vat.item.setGridAttributeByName("unitPrice", "readOnly", true);
		vat.item.setGridAttributeByName("isDeleteRecord", "readOnly", true);*/
		
		vat.block.canGridModify([vnB_Detail], false,false,false);
		//=======================<buttonLine>========================================
//		alert( "status = " + status + "\nprocessId=" + processId );
		if( status == "SIGNING" && ( processId != ""  ) ){ 
			if(canBeClaimed == "true"){
				vat.item.setStyleByName("#B.submit",    "display", "none");
			}else{
				vat.item.setStyleByName("#B.submit", 		"display", "inline");
			}
		}else{
			vat.item.setStyleByName("#B.submit", 		"display", "none");
		}
		vat.item.setStyleByName("#B.submitBG", 		"display", "none");
		vat.item.setStyleByName("#B.message", 		"display", "none");
		vat.item.setStyleByName("#B.import", 		"display", "none");
		vat.item.setStyleByName("#B.save", 			"display", "none");
		//===========================================================================
	}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_PRICE_ADJUSTMENT" + 
		"&levelType=ERROR" +
        "&processObjectName=imPriceAdjustmentMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imPriceAdjustmentAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
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

// 票據列印　含成本
function openReportWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=imPriceAdjustmentMainService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
			                    	//alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);	

	if("AFTER_SUBMIT"==type) createRefreshForm();
}