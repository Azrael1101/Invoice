 /*** 
 *	檔案: buCustomer.js
 *	說明: 類別代號,抽成率維護
 */
 

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;


function outlineBlock(){ 
 	formInitial();
 	buttonLine();
  	headerInitial();
  	detailInitial();
	//doFormAccessControl();
}
			
//初始化
function formInitial(){
 	
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther ={
			loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
			loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
			orderTypeCode      : vat.item.getValueByName("#F.orderTypeCode") == "" ? document.forms[0]["#orderTypeCode"].value:vat.item.getValueByName("#F.orderTypeCode"),
			processId          : document.forms[0]["#processId"         ].value,
			formId             : document.forms[0]["#formId"            ].value,
			assignmentId       : document.forms[0]["#assignmentId"      ].value,
			activityStatus     : document.forms[0]["#activityStatus"    ].value,
			departmentLogin    : document.forms[0]["#departmentLogin"   ].value,
			updateForm         : document.forms[0]["#formId"].value==""?"N":"Y",
			itemCategory       : vat.item.getValueByName("#F.itemCategory"),
			currentRecordNumber: 0,
			lastRecordNumber   : 0
		};
	}
	vat.bean.init(function(){
		return "process_object_name=&process_object_method_name="; 
	},{
		other: true
	});
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
		 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
						{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  ,
		 									 openMode:"open",
		 									 service:"Im_Movement:search:20090720.page",
		 									 servicePassData:function(x){ return doPassHeadData(x); },
		 									 left:0, right:0, width:1024, height:768,
		 									 serviceAfterPick:function(){doAfterPickerProcess()}},
			 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
			 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
			 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
			 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
						{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},	 
			 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
			 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
			 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
			 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
			 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 2, mode:"READONLY"},
			 			{name:"SPACE"           , type:"LABEL" ,value:"/"},
			 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 2, mode:"READONLY" },
			 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
			 			{name:"#F.reportList"  , type:"SELECT"},
			 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
			 			{name:"#B.importMovePrint"      , type:"IMG"    ,value:"送出",   src:"./images/button_form_print.gif", eClick:"importMovePrint()"}
		 			],
					td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
			]}
		],
		beginService:function(){return ""}	,
		closeService:function(){return ""}
	});

}

function headerInitial(){ 
	var vsRowStyle= vat.bean("loginBrandCode").indexOf("T2") > -1 ? "" : " style= 'display:none;'";

	vat.block.create(vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"調撥單維護作業", rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", type:"LABEL" , value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", size:1, mode:"READONLY", eChange:'kweImInitial()'}]},
				{items:[{name:"#L.orderNo"     , type:"LABEL" , value:"單號"}]},
				{items:[{name:"#F.orderNo"     , type:"TEXT"  ,  bind:"orderNo",back:false,size:20, mode:"READONLY"},
						{name:"#F.headId"      , type:"TEXT"  ,  bind:"headId", back:false, mode:"READONLY"},
	 		 		//for 儲位用
						{name:"#F.storageHeadId",   type:"TEXT",   bind:"storageHeadId",    back:false, mode:"READONLY" }
	 			]},
				{items:[{name:"#L.brandCode"   , type:"LABEL" , value:"品牌"}]},
				{items:[{name:"#F.brandCode"   , type:"TEXT"  ,  bind:"brandCode", size:6, mode:"HIDDEN"},
						{name:"#F.brandName"   , type:"TEXT"  ,  bind:"brandName", back:false, size:12, mode:"READONLY"},
						{name:"#F.status"      , type:"TEXT"  ,  bind:"status", size:12, mode:"hidden"},
						{name:"#F.statusName"  , type:"TEXT"  ,  bind:"statusName", back:false, mode:"hidden"},
						{name:"#F.customsDesc"      , type:"TEXT"  , size:35 , mode:"READONLY"}
	  		 	]}
			]},
	 		{row_style:"", cols:[
				{items:[{name:"#L.itemNo"      , type:"LABEL", value:"總件數"}]},
				{items:[{name:"#F.itemCount"   , type:"NUMB",  size:6, bind:"itemCount",  mode:"READONLY"},
						{name:"#L./"           , type:"LABEL", value:"／", size:1},
						{name:"#F.itemCount1"  , type:"NUMB",  size:6, mode:"READONLY"},
						{name:"#B.itemCount"   , type:"IMG" ,value:"件數計算",   src:"./images/arrowdown.png" ,eClick:'showTotalCountPage()'}]},
				{items:[{name:"#L.createdBy"    , type:"LABEL", value:"填單人員"},
						{name:"#F.allFixSuppliers"    	, type:"SELECT", bind:"rpSupplier",mode:"hidden", eChange:'getFixAddr()'}]},
				{items:[{name:"#F.createdBy"    , type:"TEXT",   bind:"createdBy",  mode:"HIDDEN", size:12},
						{name:"#F.createdByName", type:"TEXT",   bind:"createdByName",  mode:"READONLY", size:12}]},
				{items:[{name:"#L.lastDt"      , type:"LABEL", value:"填單日期"}]},
				{items:[{name:"#F.lastDt"      , type:"TEXT",   bind:"lastUpdateDate", mode:"READONLY", size:12}]}]},
			{row_style:"", cols:[
				{items:[{name:"#L.deliveryWarehouseCode"    , type:"LABEL" , value:"轉出倉庫<font color='red'>*</font>"}]},
				{items:[{name:"#F.deliveryWarehouseCode"    , type:"SELECT",  bind:"deliveryWarehouseCode", size:12,  eChange:'changeWarehouseCode("delivery")'},
						{name:"#F.customsDeliveryWarehouseCode" , type:"TEXT",bind:"customsDeliveryWarehouseCode", size:1, mode:"READONLY"},
						{name:"#F.allowMinusStock"          , type:"TEXT",    bind:"allowMinusStock", size:1, mode:"HIDDEN"}]},
				{items:[{name:"#L.deliveryDate"             , type:"LABEL" , value:"轉出日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.deliveryDate"             , type:"DATE"  ,  bind:"deliveryDate", size:12,  eChange:'changeDeliveryDate()'}]},
				{items:[{name:"#L.deliveryContactPerson"    , type:"LABEL" , value:"倉管人員"}]},
				{items:[{name:"#F.deliveryContactPerson"    , type:"TEXT"  ,  bind:"deliveryContactPerson"    , size:12, mode:"READONLY"},
						{name:"#F.deliveryContactPersonName", type:"TEXT"  ,  bind:"deliveryContactPersonName", back:false, size:12, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.arrivalWarehouseCode"    , type:"LABEL" , value:"轉入倉庫<font color='red'>*</font>"}]},
				{items:[{name:"#F.arrivalWarehouseCode"    , type:"SELECT",  bind:"arrivalWarehouseCode", size:12, eChange:'changeWarehouseCode("arrival")'},
						{name:"#F.customsArrivalWarehouseCode" , type:"TEXT",bind:"customsArrivalWarehouseCode", size:1, mode:"READONLY"}]},
				{items:[{name:"#L.arrivalDate"             , type:"LABEL" , value:"轉入日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.arrivalDate"             , type:"DATE"  , bind:"arrivalDate", size:12}]},
				{items:[{name:"#L.arrivalContactPerson"    , type:"LABEL" , value:"倉管人員"}]},
				{items:[{name:"#F.arrivalContactPerson"    , type:"TEXT"  , bind:"arrivalContactPerson", size:12, mode:"READONLY"},
						{name:"#F.arrivalContactPersonName", type:"TEXT"  , bind:"arrivalContactPersonName", size:12, mode:"READONLY"}]}
			]}
		],
		beginService:"",
		closeService:"doCheckStore"
	});
}

function detailInitial(){
		vbCanGridDelete = true;
		vbCanGridAppend = true;
		vbCanGridModify = true;

	vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"   ,                     desc:"序號"          });
    vat.item.make(vnB_Detail, "boxNo"                     , {type:"NUMM"  , size:3, maxLen:10,  desc:"箱號"         , mode:"hidden"});
	vat.item.make(vnB_Detail, "itemCode"                  , {type:"TEXT"  , size:18, maxLen:20, desc:"品號"         , mask:"CCCCCCCCCCCC", eChange:"changeItemData()"});
    vat.item.make(vnB_Detail, "searchItem"	              , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif",
	 									 		            	service:"Im_OnHand:search:20091224.page",
	 									 			            left:0, right:0, width:1024, height:768,
	 									 						servicePassData:function(x){ return doPassLineData(x); },
	 									 		                serviceAfterPick:function(id){doLineAfterPickerProcess(id); },mode:"hidden" });
	vat.item.make(vnB_Detail, "itemName"                  , {type:"TEXT" , size:12, maxLen:20, desc:"品名"	, mode:"READONLY"	, alter:true });
	vat.item.make(vnB_Detail, "deliveryWarehouseCode"     , {type:"TEXT" , size: 5, maxLen:20, desc:"轉出庫"	, mode:"hidden"});
	vat.item.make(vnB_Detail, "lotNo"                     , {type:"TEXT" , size:12, maxLen:15, desc:"批號"	, mask:"CCCCCCCCCCCC", mode:"hidden",eChange:"changeItemData()"});
	vat.item.make(vnB_Detail, "stockOnHandQuantity"       , {type:"NUMM" , size: 4, maxLen:12, desc:"可用量"	, dec:0	, mode:"READONLY"});
	vat.item.make(vnB_Detail, "originalDeliveryQuantity"  , {type:"NUMM" , size: 4, maxLen:12, desc:"預出量"	, dec:0 ,  eChange:"checkQuantity()"});
	vat.item.make(vnB_Detail, "deliveryQuantity"          , {type:"NUMM" , size: 4, maxLen:12, desc:"實出量"	, dec:0	,  eChange:"checkQuantity()"});
	vat.item.make(vnB_Detail, "whComfirmedQuantity"          , {type:"NUMM" , size: 4, maxLen:12, desc:"覆核量"	, dec:0, mode:"hidden"	,  eChange:""});
	vat.item.make(vnB_Detail, "arrivalWarehouseCode"      , {type:"TEXT" , size: 4, maxLen:20, desc:"轉入庫"	, mode:"hidden"});
	vat.item.make(vnB_Detail, "arrivalQuantity"           , {type:"NUMM" , size: 4, maxLen:12, desc:"轉入量"	, dec:0});
	vat.item.make(vnB_Detail, "originalDeclarationNo"     , {type:"TEXT" , size:14, maxLen:14, desc:"報單單號"  });
	vat.item.make(vnB_Detail, "searchDeclaration"		  , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", //mode:"HIDDEN",
	 									 			service:"Cm_DeclarationOnHand:search:20091103.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			servicePassData:function(x){ return doPassDeclData(x); },
	 									 			serviceAfterPick:function(id){doDeclAfterPickerProcess(id); } });
	vat.item.make(vnB_Detail, "originalDeclarationSeq"    , {type:"NUMB" , size: 3, maxLen:4,  desc:"項次"    });
	vat.item.make(vnB_Detail, "originalDeclarationDate"   , {type:"DATE" , size: 5,  desc:"報單日期"     , mode:"READONLY"});

	vat.item.make(vnB_Detail, "F11"                       , {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord"              , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
    vat.item.make(vnB_Detail, "isDeleteRecord"            , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_Detail, "message"                   , {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vnB_Detail, {
														id: "vatDetailDiv",
														pageSize: 5,
								                        canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,
														beginService: "",
								                        //closeService: function(){kweImInitial();},
														appendBeforeService : "kwePageAppendBeforeMethod()",
														appendAfterService  : "kwePageAppendAfterMethod()",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "kwePageLoadSuccess()",
														eventService        : "assignOriginalQtyToArrival()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
}


// 送出的返回
function createRefreshForm(){
	//window.close();
	var customerResult = vat.item.getValueByName("#F.customerCode");
	window.returnValue = customerResult;
	window.close();
}

// 離開按鈕按下
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.close();
   }
}

// 送出,暫存按鈕
function doSubmit(formAction){
vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
//alert(vat.item.getValueByName("#F.headId"));

	var alertMessage ="送出後將會新增會員並關閉視窗，是否繼續?";
	if(confirm(alertMessage)){		
		vat.block.submit
		(function(){
			return "process_object_name=buCustomerModAction" + "&process_object_method_name=performPosTransaction";
		},
		{bind:true, link:true, other:true});
		//window.close();
	}
}



