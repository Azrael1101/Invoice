/*** 
 *	檔案:
 *	說明：配貨單
 */
vat.debug.disable();
var saveMode = "beforeLock";
var readAfter = false;
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_ItemDetail = 2;
var vnB_ShopDetail = 3;
var vnB_ViewDetail = 4;

function outlineBlock(){
	initialVatBeanOther();
	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","商品明細資料" ,"vatItemDiv"	,"images/tab_po_detail_dark.gif" ,"images/tab_po_detail_light.gif", false, "doPageDataSave('')");
		vat.tabm.createButton(0 ,"xTab2","店名明細資料" ,"vatShopDiv"	,"images/tab_po_detail_dark.gif" ,"images/tab_po_detail_light.gif", false, "doPageDataSave('')");
		vat.tabm.createButton(0 ,"xTab3","總明細資料" ,"vatViewDiv"	,"images/tab_po_detail_dark.gif" ,"images/tab_po_detail_light.gif", false, "doPageDataSave('')");
		vat.tabm.createButton(0 ,"xTab5","簽核資料" ,"vatApprovalDiv","images/tab_approval_data_dark.gif","images/tab_approval_data_light.gif",document.forms[0]["#processId"].value==""?"none":"inline");
	}
	
	itemInitial();
	shopInitial();
	viewInitial();
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
             vat.item.getValueByName("#F.orderTypeCode"), 
             vat.item.getValueByName("#F.orderNo"),
             document.forms[0]["#loginEmployeeCode"].value );
	doFormAccessControl();
}

// 初始化
function formInitial(){  
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imDistributionAction&process_object_method_name=performInitial"; 
	    	},{								
	    		other: true
    	});
  	}
}

function initialVatBeanOther(){
	vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,	
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          firstRecordNumber		: 0,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0,
	      formStatus			:"",
	      beforeChangeStatus	:"",
	      approvalResult		:"",
	      approvalComment		:"",
	      organizationCode		:"TM"
        };
}

function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
	 	
	 	        {name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_Distribution:search:20091213.page",    // ?orderTypeCode="+vat.bean().vatBeanOther.orderTypeCode
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doPageDataSave("SIGNING")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doPageDataSave("SAVE")'},
	 			{name:"saveSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doPageDataSave("VOID")'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doPageDataSave("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.lock"    	   , type:"IMG"    ,value:"查詢",   src:"./images/button_lock_data.gif", eClick:'doPageDataSave("loadDistribution")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.unlock"      , type:"IMG"    ,value:"解除查詢",   src:"./images/button_unlock_data.gif", eClick:'disableDistribution()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export"      , type:"IMG"    ,value:"匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import"      , type:"IMG"    ,value:"匯入",   src:"./images/button_detail_import.gif", eClick:'doImport()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆",   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
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
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

// 配貨單維護主檔
function headerInitial(){
var allOrderTypes = vat.bean("allOrderTypes");
var allWarehouses = vat.bean("allWarehouses");
var allReceiveOrderTypeCodes = vat.bean("allReceiveOrderTypeCodes");
var allItemCategorys = vat.bean("allItemCategorys");
var orderTypeCode = document.forms[0]["#orderTypeCode"     ].value;
var ido = "";
var idr = "";

if(orderTypeCode == "IDO"){
	idr = "readOnly";
}else if(orderTypeCode == "IDR"){
	ido="readOnly";
}
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"配貨單維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.defaultWarehouseCode",type:"LABEL", 	value:"配貨庫別" }]},
				{items:[{name:"#F.defaultWarehouseCode",type:"SELECT", 	bind:"defaultWarehouseCode", size:15, init:allWarehouses, eChange:"changeWarehouse()", mode:ido}]},
				{items:[{name:"#L.actualWarehouseCode",type:"LABEL", 	value:"實際出貨庫別" }]},
				{items:[{name:"#F.actualWarehouseCode",type:"SELECT", 	bind:"actualWarehouseCode", size:15, init:allWarehouses, mode:ido}]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", size:4, mode:"HIDDEN"},
	 					{name:"#F.brandName", 			type:"TEXT",  	bind:"brandName", size:10, mode:"READONLY"},
	 			   		{name:"#F.headId", 				type:"TEXT",  	bind:"headId", back:false, size:3, mode:"READONLY"}]},
	 			{items:[{name:"#L.status", 				type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"TEXT", 	bind:"status", size:5, mode:"HIDDEN"},
						{name:"#F.statusName", 			type:"TEXT",  bind:"statusName", back:false, mode:"READONLY"}]} 
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 		type:"LABEL", 	value:"單別" }]},
				{items:[{name:"#F.orderTypeCode", 		type:"SELECT", 	bind:"orderTypeCode", size:15, mode:"READONLY", init:allOrderTypes}]},			
				{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"單號" }]},
				{items:[{name:"#F.orderNo", 			type:"TEXT", 	bind:"orderNo", size:20, mode:"READONLY"}]},									
	 			{items:[{name:"#L.lastUpdateDate", 		type:"LABEL", 	value:"最近修改日期"}]},	 
	 			{items:[{name:"#F.lastUpdateDate", 		type:"TEXT",  	bind:"lastUpdateDate", size:10, mode:"READONLY"}]},
	 			{items:[{name:"#L.lastUpdatedByName", 	type:"LABEL", 	value:"最近修改人員"}]},	 
	 			{items:[{name:"#F.lastUpdatedByName", 	type:"TEXT",  	bind:"lastUpdatedByName", size:6, mode:"READONLY"},
	 					{name:"#F.lastUpdatedBy", 		type:"TEXT",	bind:"lastUpdatedBy", size:5, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.receiveOrderTypeCode", 		type:"LABEL", 	value:"進貨單別" }]},
				{items:[{name:"#F.receiveOrderTypeCode", 		type:"SELECT", 	bind:"receiveOrderTypeCode", size:15, init:allReceiveOrderTypeCodes, mode:idr}]},			
				{items:[{name:"#L.receiveOrderNo", 			type:"LABEL", 	value:"進貨單號" }]},
				{items:[{name:"#F.receiveOrderNo", 			type:"TEXT", 	bind:"receiveOrderNo", size:20, mode:idr, eChange:"changeReceive()"},
						{name:"#B.receiveOrderNo",	value:"選取" ,type:"PICKER" ,
			 									 		openMode:"open", src:"./images/start_node_16.gif",
										  				servicePassData:function()
										  				{return "&orderTypeCode="+vat.item.getValueByName("#F.receiveOrderTypeCode")},
	 									  				service:"Im_Receive:search:20091102.page",
			 									 		left:0, right:0, width:1024, height:768,	
			 									 		serviceAfterPick:function(){doAfterPickerReceiveOrderNoProcess();}}]},
				{items:[{name:"#L.itemCategory", 			type:"LABEL", 	value:"業種" }]},
				{items:[{name:"#F.itemCategory", 			type:"SELECT", 	bind:"itemCategory", size:20, mode:ido, init:allItemCategorys}]},
				{items:[{name:"#L.reserve1", 		type:"LABEL", 	value:"備註"}]},	 
	 			{items:[{name:"#F.reserve1", 		type:"TEXT",  	bind:"reserve1", size:20}]}
			]}
		], 	
		beginService:"",
		closeService:""			
	});
}


function itemInitial(){
	var Vstatus = vat.item.getValueByName("#F.status");
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
	
	vat.item.make(vnB_ItemDetail, "indexNo"			, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_ItemDetail, "itemCode"		, {type:"TEXT" , size:15, desc:"商品號碼", mode:"readOnly"});
	vat.item.make(vnB_ItemDetail, "itemName"		, {type:"TEXT" , size:20, maxLen:20, desc:"商品名稱",mode:"readOnly"});
	vat.item.make(vnB_ItemDetail, "lotNo"			, {type:"TEXT" , size:15, desc:"批號",maxLen:20, mode:"hidden"});
	vat.item.make(vnB_ItemDetail, "quantity"		, {type:"NUMB" , size:4, desc:"可配貨數量", mode:"readOnly"});
	vat.item.make(vnB_ItemDetail, "lineId"			, {type:"ROWID"});
	vat.item.make(vnB_ItemDetail, "isLockRecord"	, {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_ItemDetail, "isDeleteRecord"	, {type:"DEL", desc:"刪除"});
	vat.item.make(vnB_ItemDetail, "message"			, {type:"MSG", desc:"訊息"});
	vat.block.pageLayout(vnB_ItemDetail, {
										id: "vatItemDiv", 
										pageSize: 10,											
				            			canGridDelete : vbCanGridDelete,
										canGridAppend : vbCanGridAppend,
										canGridModify : vbCanGridModify,						
										loadBeforeAjxService: "loadBeforeAjxService('"+vnB_ItemDetail+"')",
										loadSuccessAfter    : "loadSuccessAfter('"+vnB_ItemDetail+"')",						
										eventService        : "eventService()",   
										saveBeforeAjxService: "saveBeforeAjxService('"+vnB_ItemDetail+"')",
										saveSuccessAfter    : "saveSuccessAfter('"+vnB_ItemDetail+"')"
										});
	vat.block.pageDataLoad(vnB_ItemDetail, vnCurrentPage = 1);
}

function shopInitial(){
	var Vstatus = vat.item.getValueByName("#F.status");
	var vbCanGridDelete = true; 
	var vbCanGridAppend = true; 
	var vbCanGridModify = true;

	vat.item.make(vnB_ShopDetail, "indexNo1"			, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_ShopDetail, "shopCode1"			, {type:"TEXT" , size:15, desc:"店別代號" ,eChange:"changeShopCode()"});
	vat.item.make(vnB_ShopDetail, "shopName1"			, {type:"TEXT" , size:10, maxLen:20, desc:"店名",mode:"readOnly"});
	vat.item.make(vnB_ShopDetail, "warehouseCode1"		, {type:"TEXT" , size:4, desc:"倉庫代號", mode:"readOnly"});
	vat.item.make(vnB_ShopDetail, "lineId1"				, {type:"ROWID"});
	vat.item.make(vnB_ShopDetail, "isLockRecord1"		, {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_ShopDetail, "isDeleteRecord1"		, {type:"DEL", desc:"刪除"});
	vat.item.make(vnB_ShopDetail, "message1"			, {type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnB_ShopDetail, {
										id: "vatShopDiv", 
										pageSize: 10,											
				            			canGridDelete : vbCanGridDelete,
										canGridAppend : vbCanGridAppend,
										canGridModify : vbCanGridModify,						
										loadBeforeAjxService: "loadBeforeAjxService('"+vnB_ShopDetail+"')",
										loadSuccessAfter    : "loadSuccessAfter('"+vnB_ShopDetail+"')",						
										eventService        : "eventService()",   
										saveBeforeAjxService: "saveBeforeAjxService('"+vnB_ShopDetail+"')",
										saveSuccessAfter    : "saveSuccessAfter('"+vnB_ShopDetail+"')"
										});
	vat.block.pageDataLoad(vnB_ShopDetail, vnCurrentPage = 1);
}

function viewInitial(){
	var Vstatus = vat.item.getValueByName("#F.status");
	var VorderTypeCode = document.forms[0]["#orderTypeCode"].value;
	var allShops = vat.bean("allShops");
	var allShopNames = allShops[1];
	var allShopCodes = allShops[2];
	var vbCanGridDelete = true; 
	var vbCanGridAppend = true; 
	var vbCanGridModify = true;
	
	vat.item.make(vnB_ViewDetail, "indexNo2"		, {type:"IDX"  , view: "fixed", desc:"序號" });
	vat.item.make(vnB_ViewDetail, "itemBrand2"		, {type:"TEXT" , view: "fixed", size:5, desc:"商品品牌", mode:"readOnly"});
	vat.item.make(vnB_ViewDetail, "category022"		, {type:"TEXT" , view: "fixed", size:5, desc:"商品類別", mode:"readOnly"});
	vat.item.make(vnB_ViewDetail, "category132"		, {type:"TEXT" , view: "fixed", size:5, desc:"商品系列", mode:"readOnly"});
	vat.item.make(vnB_ViewDetail, "itemCode2"		, {type:"TEXT" , view: "fixed", size:15, desc:"商品號碼", mode:"readOnly"});
	vat.item.make(vnB_ViewDetail, "itemName2"		, {type:"TEXT" , view: "fixed", size:25, desc:"商品名稱",mode:"readOnly"});
	vat.item.make(vnB_ViewDetail, "supplierItemCode", {type:"TEXT" , view: "", size:15, desc:"廠商貨號", mode:"readOnly"});
	vat.item.make(vnB_ViewDetail, "unitPrice"		, {type:"NUMM" , view: "", size:10, desc:"售價", mode:"readOnly"});
	vat.item.make(vnB_ViewDetail, "lotNo2"			, {type:"TEXT" , view: "", size:15, desc:"批號",maxLen:20, mode:"hidden"});
	vat.item.make(vnB_ViewDetail, "quantity2"		, {type:"NUMB" , view: "", size:4, desc:"配貨庫數量", mode:"readOnly"});
	vat.item.make(vnB_ViewDetail, "shops"			, {type:"TEXT" , view: "", size:4, desc:"店櫃", mode:"hidden"});
	vat.item.make(vnB_ViewDetail, "shopQuantitys"	, {type:"TEXT" , view: "", size:4, desc:"現有", mode:"hidden"});
	vat.item.make(vnB_ViewDetail, "quantitys"		, {type:"TEXT" , view: "", size:4, desc:"分配", mode:"hidden"});
	vat.item.make(vnB_ViewDetail, "lineId2"			, {type:"ROWID"});
	for(var i=0 ; i<allShopNames.length ; i++){
		vat.item.make(vnB_ViewDetail, allShopCodes[i]+"0", {type:"NUMB", size:5, view: "none", desc:allShopCodes[i]+"現", mode:"readOnly"});
		vat.item.make(vnB_ViewDetail, allShopCodes[i]+"1", {type:"NUMB", size:5, view: "none", desc:allShopCodes[i]+"配", mode:"", eChange:"changeQuantity()"});
	}
	vat.item.make(vnB_ViewDetail, "total"	, {type:"TEXT", size:5, desc:"總分配數", mode:"readOnly"});	
	vat.block.pageLayout(vnB_ViewDetail, {
										id: "vatViewDiv", 
										pageSize: 10,											
				            			canGridDelete : vbCanGridDelete,
										canGridAppend : vbCanGridAppend,
										canGridModify : vbCanGridModify,						
										loadBeforeAjxService: "loadBeforeAjxService('"+vnB_ViewDetail+"')",
										loadSuccessAfter    : "loadSuccessAfter('"+vnB_ViewDetail+"')",						
										eventService        : "eventService()",   
										saveBeforeAjxService: "saveBeforeAjxService('"+vnB_ViewDetail+"')",
										saveSuccessAfter    : "saveSuccessAfter('"+vnB_ViewDetail+"')"
										});
}

function changeShopCode() {  
  var vLineId                 = vat.item.getGridLine();
  var vShopCode               = vat.item.getGridValueByName("shopCode1", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  vat.item.setGridValueByName("shopCode1",vLineId, vShopCode);
	vat.ajax.XHRequest(
	    {
	        post:"process_object_name=imDistributionHeadService"+
	                    "&process_object_method_name=executeFindShopName"+
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&shopCode=" + vShopCode,
	        find: function changeShopCodeSuccess(oXHR){
	        	vat.item.setGridValueByName("shopName1" ,vLineId, vat.ajax.getValue("ShopName", oXHR.responseText));
	        	vat.item.setGridValueByName("warehouseCode1" ,vLineId, vat.ajax.getValue("WarehouseCode", oXHR.responseText));
	       }   
	   });
}

// 第一次載入 重新整理
function loadBeforeAjxService(div){
		var processString = "";
			processString = "process_object_name=imDistributionHeadService&process_object_method_name=getAJAXLinePageData" + 
		    "&headId=" + vat.item.getValueByName("#F.headId")+
		    "&div=" + div ;
		return processString;	
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
			var processString = "process_object_name=imDistributionHeadService&process_object_method_name=updateOrSaveAJAXPageLinesData" + 
				"&headId=" + vat.item.getValueByName("#F.headId")+
				"&saveMode=" + saveMode+
		    	"&div=" + div ;
		return processString;
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){

} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(div){
	if(div =="4" && saveMode == "afterLock"){
		var shopCodes = vat.item.getGridValueByName("shops", 1).split(",");
		if(typeof shopCodes != "undefined" && shopCodes != ""){
			for(o=0 ; o<shopCodes.length ; o++){
				vat.block.columnViewChange ( vnB_ViewDetail, shopCodes[i]+"0", "shift" );
				vat.block.columnViewChange ( vnB_ViewDetail, shopCodes[i]+"1", "shift" );
				//vat.item.setGridStyleByName(shopCodes[o]+"0", "display", "inline");
				//vat.item.setGridStyleByName(shopCodes[o]+"1", "display", "inline");
			}
			for(ii = 1 ; ii<=10 ; ii++){
				var total = 0;
				var shopQuans = vat.item.getGridValueByName("shopQuantitys", ii).split(",");
				var quans = vat.item.getGridValueByName("quantitys", ii).split(",");
				if(typeof shopQuans != "undefined" && shopQuans != ""){
					for(i = 0 ; i<shopQuans.length ; i++){
						vat.item.setGridValueByName(shopCodes[i]+"0" , ii , shopQuans[i]);
						vat.item.setGridValueByName(shopCodes[i]+"1" , ii , quans[i]);
						total += parseInt(quans[i]);
					}
					vat.item.setGridValueByName("total" , ii , total);
				}
			}
		}
	}
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
function doPageDataSave(processName){
	var alertMessage = "";
	if("SIGNING" == processName){
		alertMessage = "是否確定送出?";
	}else if ("SAVE" == processName){
	 	alertMessage = "是否確定暫存?";
	}else if("SUBMIT_BG" == processName){
		alertMessage = "是否確定背景送出?";
	}
	
	if(processName != ""){
		if(confirm(alertMessage)){
			doSaveMode(processName)
		}
	}else{
		doSaveMode(processName)
	}
}

function doSaveMode(processName){
	if(saveMode == "beforeLock"){
		vat.block.pageDataSave(vnB_ItemDetail, {  
			funcSuccess:function(){
				vat.block.pageDataSave(vnB_ShopDetail,{  
					funcSuccess:function(){
						afterPageDataSave(processName);
					}
				});
			}
		});
	}else if(saveMode == "afterLock"){
		vat.block.pageDataSave(vnB_ViewDetail, {  
			funcSuccess:function(){
				afterPageDataSave(processName);
			}
		});
	}
}
	
	

function afterPageDataSave(processName){
	if(processName == "loadDistribution")
		loadDistribution();
	else if(processName == "SAVE" )
		doSubmit("SAVE");
	else if(processName == "SIGNING" )
		doSubmit("SIGNING");
	else if(processName == "SUBMIT_BG" )
		doSubmit("SUBMIT_BG");
	else if(processName == "VOID" )
		doSubmit("VOID");
}

function changeWarehouse(){
	var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode")
	vat.ajax.XHRequest(
		    {
		        asyn:false,
		        post:"process_object_name=imDistributionHeadService"+
	                    "&process_object_method_name=updateChangeWarehouseCode"+
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
	                    "&defaultWarehouseCode=" + defaultWarehouseCode,
		        find: function changeWarehouseSuccess(oXHR){
		        vat.block.pageRefresh(vnB_ItemDetail);
		       }   
		   })
}

function changeReceive(){
	var receiveOrderNo = vat.item.getValueByName("#F.receiveOrderNo")
	var receiveOrderTypeCode = vat.item.getValueByName("#F.receiveOrderTypeCode")
	vat.ajax.XHRequest(
		    {
		        asyn:false,
		        post:"process_object_name=imDistributionHeadService"+
	                    "&process_object_method_name=updateChangeReceive"+
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
	                    "&receiveOrderNo=" + receiveOrderNo+
	                    "&receiveOrderTypeCode=" + receiveOrderTypeCode,
		        find: function changeWarehouseSuccess(oXHR){
		        var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
		        if(errorMsg != "" && receiveOrderNo !=""){
		        	vat.item.setAttributeByName("#F.receiveOrderTypeCode", "readOnly", false);
		        	alert(errorMsg);
		        }else{
		        	vat.item.setValueByName("#F.itemCategory", vat.ajax.getValue("itemCategory", oXHR.responseText));
		        	if(receiveOrderNo != "")
						vat.item.setAttributeByName("#F.receiveOrderTypeCode", "readOnly", true);
					else
						vat.item.setAttributeByName("#F.receiveOrderTypeCode", "readOnly", false);
		        }
		        vat.block.pageRefresh(vnB_ItemDetail);
		       }   
		   })
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

function changeQuantity(){
	var vLineId   = vat.item.getGridLine();
	var shopCodes = vat.item.getGridValueByName("shops", vLineId).split(",");
	var shopQuans = vat.item.getGridValueByName("shops", vLineId).split(",");
	if(typeof shopQuans != "undefined" && shopQuans != ""){
		var sb = "";
		var total = 0;
		for(i = 0 ; i<shopQuans.length ; i++){
			sb += vat.item.getGridValueByName(shopCodes[i]+"1" , vLineId);
			total += parseInt(vat.item.getGridValueByName(shopCodes[i]+"1" , vLineId));
			if(i+1 != shopQuans.length)
				sb +=",";
		}
		vat.item.setGridValueByName("quantitys" , vLineId , sb);
		vat.item.setGridValueByName("total" , vLineId , total);
	}
}

function doSubmit(formAction){
	var alertMessage ="是否確定?";
		/*if("SIGNING" == formAction){
			alertMessage = "是否確定送出?";
		}else if ("SAVE" == formAction){
		 	alertMessage = "是否確定暫存?";
		}else if("SUBMIT_BG" == formAction){
			alertMessage = "是否確定背景送出?";
		}*/
		//if(confirm(alertMessage)){
		    var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');
		    var beforeChangeStatus	  = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		    var approvalResult        = vat.item.getValueByName("#F.approvalResult");
		    if(approvalResult == true){
		    	approvalResult = "true"
		    }else{
		    	approvalResult = "false"
		    }
		    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
		    initialVatBeanOther();
			vat.bean().vatBeanOther.beforeChangeStatus	= beforeChangeStatus;
			vat.bean().vatBeanOther.formStatus      	= formAction;
			vat.bean().vatBeanOther.approvalResult  	= approvalResult;
			vat.bean().vatBeanOther.approvalComment 	= approvalComment;
			vat.bean().vatBeanOther.formId 				= formId;
			if("SUBMIT_BG" == formAction){
				vat.block.submit(function(){return "process_object_name=imDistributionAction"+
				"&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
			}else{
				vat.block.submit(function(){return "process_object_name=imDistributionAction"+
			    "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
			}   
		//}
}

function doAfterPickerReceiveOrderNoProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.receiveOrderNo", vat.bean().vatBeanPicker.result[0].orderNo);
    	changeReceive();
	}
	
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_DISTRIBUTION" +
		"&levelType=ERROR" +
        "&processObjectName=imDistributionHeadService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
	vat.bean().vatBeanOther.formStatus = "SIGNING";
    vat.block.submit(function(){return "process_object_name=imDistributionAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

function createRefreshForm(){
	refreshForm("");
}

function loadDistribution(){
	var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
	var actualWarehouseCode = vat.item.getValueByName("#F.actualWarehouseCode");
	var receiveOrderNo = vat.item.getValueByName("#F.receiveOrderNo")
	var VorderTypeCode = document.forms[0]["#orderTypeCode"].value;
	var itemCategory = vat.item.getValueByName("#F.itemCategory")
	
	if(defaultWarehouseCode == "" && VorderTypeCode == "IDO"){
		alert('請選擇配貨庫別');
	}else if(actualWarehouseCode == "" && VorderTypeCode == "IDO"){
		alert('請選擇實際出貨庫別');
	}else if(itemCategory == "" && VorderTypeCode == "IDO"){
		alert('請選擇業種');
	}else if(receiveOrderNo == "" && VorderTypeCode == "IDR"){
		alert('請輸入進貨單號');
	}else{
		vat.ajax.XHRequest(
		    {
		        asyn:false,
		        post:"process_object_name=imDistributionHeadService"+
		                    "&process_object_method_name=updateComposeLine"+
		                    "&actualWarehouseCode="+ actualWarehouseCode +
		                    "&itemCategory="+ itemCategory +
		                    "&headId=" + vat.item.getValueByName("#F.headId"),
		        find: function loadDistributionSuccess(oXHR){
			        var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
			        var shopCodes = vat.ajax.getValue("shopCodes", oXHR.responseText).split(",");
			        if(errorMsg != ""){
			        	alert(errorMsg);
			        	showMessage();
			        }else{
			        	vat.item.setStyleByName("#B.submitBG", "display", "inline");
	  					vat.item.setStyleByName("#B.submit"	 , "display", "inline");
			        	vat.item.setStyleByName("#B.unlock"	 , "display", "inline");
	  					vat.item.setStyleByName("#B.lock"	 , "display", "none");
	  					vat.item.setGridAttributeByName("shopCode1", "readOnly", true);
	  					vat.item.setGridStyleByName("isDeleteRecord", "display", "none");
	  					vat.item.setGridStyleByName("isDeleteRecord1", "display", "none");
	  					if(VorderTypeCode == "IDO"){
							vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true);
							vat.item.setAttributeByName("#F.actualWarehouseCode", "readOnly", true);
						}else{
							vat.item.setAttributeByName("#F.receiveOrderNo", "readOnly", true);
						}
						
						vat.item.setAttributeByName("#F.reserve1", "readOnly", true);
						vat.item.setStyleByName("#B.export"		, "display", "inline");
						vat.item.setStyleByName("#B.import"		, "display", "inline");
	  					var Vstatus = vat.item.getValueByName("#F.status");
	  					for(i = 0 ; i<shopCodes.length ; i++){
							if(Vstatus == "SAVE")
								vat.item.setGridAttributeByName(shopCodes[i]+"1", "readOnly", false);
							else
								vat.item.setGridAttributeByName(shopCodes[i]+"1", "readOnly", true);
						}
	  					saveMode = "afterLock";
	  					vat.block.pageDataLoad(vnB_ViewDetail, vnCurrentPage = 1);
						vat.tabm.displayToggle(0, "xTab3", true);
			        }
		       	}
		   })
	}
}

function disableDistribution(){
	var VorderTypeCode = document.forms[0]["#orderTypeCode"].value;
	vat.ajax.XHRequest(
	    {
	        asyn:false,
	        post:"process_object_name=imDistributionHeadService"+
	                    "&process_object_method_name=updateDisableLine"+
	                    "&headId=" + vat.item.getValueByName("#F.headId"),
	        find: function disableDistributionSuccess(oXHR){
      				vat.item.setStyleByName("#B.submitBG", "display", "none");
					vat.item.setStyleByName("#B.submit"	 , "display", "none");
        			vat.item.setStyleByName("#B.unlock"	 , "display", "none");
					vat.item.setStyleByName("#B.lock"	 , "display", "inline");
					vat.item.setGridAttributeByName("shopCode1", "readOnly", false);
					vat.item.setGridStyleByName("isDeleteRecord", "display", "inline");	
					vat.item.setGridStyleByName("isDeleteRecord1", "display", "inline");
					if(VorderTypeCode == "IDO"){
						vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", false);
						vat.item.setAttributeByName("#F.actualWarehouseCode", "readOnly", false);
					}else{
						vat.item.setAttributeByName("#F.receiveOrderNo", "readOnly", false);
					}
					vat.item.setAttributeByName("#F.reserve1", "readOnly", false);
					vat.item.setStyleByName("#B.export"		, "display", "none");
					vat.item.setStyleByName("#B.import"		, "display", "none");
					saveMode = "beforeLock";
					vat.tabm.displayToggle(0, "xTab3", false);
	       }   
	   });
}

function doPageRefresh(){
    vat.block.pageRefresh(vnB_ItemDetail);
    vat.block.pageRefresh(vnB_ShopDetail);
    vat.block.pageRefresh(vnB_ViewDetail);
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


// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"].value = code;
	vat.bean().vatBeanOther.formId = code;
	vat.block.submit(
		function(){
			return "process_object_name=imDistributionAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll();
     			doFormAccessControl();
     			doPageRefresh();
     	}});
}

function doFormAccessControl(){
	var Vstatus = vat.item.getValueByName("#F.status");
	var VprocessId = document.forms[0]["#processId"].value;
	var VorderTypeCode = document.forms[0]["#orderTypeCode"     ].value;
	var VreceiveOrderNo = document.forms[0]["#F.receiveOrderNo"     ].value;
	var allShops = vat.bean("allShops");
	var allShopCodes = allShops[2];
	for(var i=0 ; i<allShopCodes.length ; i++){
		vat.block.columnViewChange ( vnB_ViewDetail, allShopCodes[i]+"0", "shift" );
		vat.block.columnViewChange ( vnB_ViewDetail, allShopCodes[i]+"1", "shift" );
		//vat.item.setGridStyleByName(allShopCodes[i]+"0", "display", "none");
		//vat.item.setGridStyleByName(allShopCodes[i]+"1", "display", "none");
		//vat.item.setGridAttributeByName(allShopCodes[i]+"1", "readOnly", true);
	}
	
		saveMode = "beforeLock";
		vat.item.setStyleByName("#B.void"		, "display", "none");
		vat.item.setStyleByName("#B.unlock"		, "display", "none");
		vat.item.setStyleByName("#B.submit"		, "display", "none");
		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
		vat.item.setStyleByName("#B.save"		, "display", "inline");
		vat.item.setStyleByName("#B.export"		, "display", "none");
		vat.item.setStyleByName("#B.import"		, "display", "none");
		if(VorderTypeCode == "IDO"){
			vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", false);
			vat.item.setAttributeByName("#F.actualWarehouseCode", "readOnly", false);
		}else{
			vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true);
			vat.item.setAttributeByName("#F.actualWarehouseCode", "readOnly", true);
			if(VreceiveOrderNo == "")
				vat.item.setAttributeByName("#F.receiveOrderTypeCode", "readOnly", false);
			else
				vat.item.setAttributeByName("#F.receiveOrderTypeCode", "readOnly", true);
		}
		
		vat.item.setAttributeByName("#F.reserve1", "readOnly", false);
		//vat.tabm.displayToggle(0, "xTab3", false);
		vat.item.setGridAttributeByName("shopCode1", "readOnly", false);
  		vat.item.setGridStyleByName("isDeleteRecord", "display", "inline");
  		vat.item.setGridStyleByName("isDeleteRecord1", "display", "inline");
  		vat.item.setStyleByName("#B.lock", "display", "inline");
	if(VprocessId == ""){
		if(vat.bean().vatBeanPicker.result != null){
			saveMode = "afterLock";
			vat.block.pageDataLoad(vnB_ViewDetail, vnCurrentPage = 1);
			vat.item.setStyleByName("#B.unlock"		, "display", "none");
			vat.item.setStyleByName("#B.lock"		, "display", "none");
			vat.item.setStyleByName("#B.void"		, "display", "none");
			vat.item.setStyleByName("#B.submit"	, "display", "none");
			vat.item.setStyleByName("#B.submitBG"	, "display", "none");
			vat.item.setStyleByName("#B.message"	, "display", "none");
			vat.item.setStyleByName("#B.save"		, "display", "none");
			vat.item.setStyleByName("#B.import"		, "display", "none");
			vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true);
			vat.item.setAttributeByName("#F.reserve1", "readOnly", true);
			vat.tabm.displayToggle(0, "xTab3", true);
			vat.item.setGridAttributeByName("shopCode1", "readOnly", true);
	  		vat.item.setGridStyleByName("isDeleteRecord", "display", "none");
	  		vat.item.setGridStyleByName("isDeleteRecord1", "display", "none");
	  		if(Vstatus == "SIGNING"){
				vat.item.setStyleByName("#B.export"		, "display", "inline");
	  		}
		}
	}else{
		vat.item.setStyleByName("#B.new"	, "display", "none");
		vat.item.setStyleByName("#B.search"	, "display", "none");
		if(Vstatus == "SAVE"){
			saveMode = "beforeLock";
			vat.item.setStyleByName("#B.unlock"		, "display", "none");
			vat.item.setStyleByName("#B.submit"		, "display", "none");
			vat.item.setStyleByName("#B.submitBG"	, "display", "none");
		}else if(Vstatus == "SIGNING"){
			saveMode = "afterLock";
			vat.block.pageDataLoad(vnB_ViewDetail, vnCurrentPage = 1);
			vat.item.setStyleByName("#B.unlock"		, "display", "none");
			vat.item.setStyleByName("#B.lock"		, "display", "none");
			vat.item.setStyleByName("#B.void"		, "display", "none");
			vat.item.setStyleByName("#B.submitBG"	, "display", "none");
			vat.item.setStyleByName("#B.message"	, "display", "none");
			vat.item.setStyleByName("#B.save"		, "display", "none");
			vat.item.setStyleByName("#B.import"		, "display", "none");
			vat.item.setStyleByName("#B.export"		, "display", "inline");
			
			vat.form.item.readonly("vatBlock_Head");
			vat.item.setGridAttributeByName("shopCode1", "readOnly", true);
	  		vat.item.setGridStyleByName("isDeleteRecord", "display", "none");
	  		vat.item.setGridStyleByName("isDeleteRecord1", "display", "none");
			vat.tabm.displayToggle(0, "xTab3", true);
		}
	}
}

function doExport(){
		return window.open('/erp/jsp/ExportImDistribution.jsp?headId=' + vat.item.getValueByName("#F.headId"),'視窗名稱','參數設定','記錄設定');
}


function doImport(){
	var width = "600";
    var height = "400";
    var beanName = "IM_DISTRIBUTION_ITEM";
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName="+ beanName +
		"&importFileType=XLS" +
        "&processObjectName=imDistributionHeadService" + 
        "&processObjectMethodName=executeImport" +
        "&arguments=" +vat.item.getValueByName("#F.headId")  +
        "&parameterTypes=LONG" +
        "&blockId=" + vnB_ViewDetail,
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    initialVatBeanOther();
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var headId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(headId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
	  	vat.item.setStyleByName("#B.save"		, "display", "none");
  		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
  		vat.item.setStyleByName("#B.submit"		, "display", "none");
  		vat.item.setStyleByName("#B.import"		, "display", "none");
  		vat.item.setStyleByName("#B.message"	, "display", "none");
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