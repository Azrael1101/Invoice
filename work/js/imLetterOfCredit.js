/*** 
 *	檔案: imLetterOfCredit.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by david
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_ALT_Detail = 2;
var vnB_Detail = 3;
var vnB_Amount = 4;

var activeTab = 2;

function kweImBlock(){
  	kweImInitial();
	kweButtonLine();
  	kweImHeader();

	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");         
		vat.tabm.createButton(0 ,"xTab1","修狀明細檔" ,"vatAltDetailDiv"        		,"images/tab_master_data_dark.gif"      	,"images/tab_master_data_light.gif", false, "doPageDataSave("+vnB_ALT_Detail+")");                                                                                                                                                                                                                       
		vat.tabm.createButton(0 ,"xTab2","信用狀明細檔" ,"vatDetailDiv"        		,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false, "doPageDataSave("+vnB_Detail+")" );                                                                                                    
		vat.tabm.createButton(0 ,"xTab3","金額統計" ,"vatAmountDiv"        		,"images/tab_total_amount_dark.gif"      	,"images/tab_total_amount_light.gif", false, "doPageDataSave("+vnB_Amount+")" ); 
  	}  
	kweImAltDetail();
	kweImDetail();
	kweImAmount();
	
	doFormAccessControl();
	
//	var lcDate = document.getElementById("#F.lcDate");
//	lcDate += "\nlcDate.datatype =" + lcDate.datatype +"\n";
//	alert( "最後變成" + lcDate ); 
}

function kweImInitial(){ 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,	
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          currentRecordNumber : 0,
	      lastRecordNumber    : 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imLetterOfCreditAction&process_object_method_name=performInitial"; 
	    	},{
	    		other: true
    	});
  	}
 
}

function kweButtonLine(){
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
	 									 service:"Im_LetterOfCredit:search:20090924.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.close"       , type:"IMG"    ,value:"結案",   src:"./images/button_close.gif", eClick:'doSubmit("CLOSE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.message"    , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
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

// 主檔
function kweImHeader(){ 
	var allCurrencyCodes = vat.bean("allCurrencyCodes");
	vat.block.create( 0, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"信用狀功能維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.lcNo", 		type:"LABEL", 	value:"L/CNO<font color='red'>*</font>"}]},
				{items:[{name:"#F.lcNo", 		type:"TEXT", 	bind:"lcNo", back:false, size:15},
					{name:"#H.headId"  , type:"TEXT"  ,  bind:"headId", back:false, mode:"HIDDEN"}]},  
				{items:[{name:"#L.lcDate", 		type:"LABEL", 	value:"開狀日<font color='red'>*</font>" }]},
				{items:[{name:"#F.lcDate", 		type:"DATE", 	bind:"lcDate", size:1 }]},
				{items:[{name:"#L.creditNoted",		type:"LABEL", 	value:"Credit noted" }]},
				{items:[{name:"#F.creditNoted",		type:"TEXT", 	bind:"creditNoted" ,mode:"READONLY", size:8 }]}, 
				{items:[{name:"#L.brandCode", 	type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 	type:"TEXT",  	bind:"brandCode", size:6, mode:"HIDDEN"},
	 					{name:"#F.brandName", 	type:"TEXT",  	bind:"brandName", size:8, mode:"READONLY"}]},
	 		 	{items:[{name:"#L.status", type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", type:"TEXT", 	bind:"status", size:15, mode:"HIDDEN"},
					{name:"#F.statusName"  , type:"TEXT"  ,  bind:"statusName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.supplierCode",	type:"LABEL", 	value:"供應商<font color='red'>*</font>" }]},
				{items:[{name:"#F.supplierCode",	type:"TEXT", 	bind:"supplierCode", eChange: function(){ changeSupplierName("supplierCode"); }, size:15 },
					{name:"#B.supplierCode",	value:"選取" ,type:"PICKER" ,
	 									 		openMode:"open", src:"./images/start_node_16.gif",
	 									 		service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 		left:0, right:0, width:1024, height:768,	
	 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("supplierCode");}},
	 				{name:"#F.addressBookId", 		type:"TEXT",  	bind:"addressBookId", back:false, mode:"HIDDEN"},	
	 				{name:"#L.supplierName", 		type:"LABEL", 	value:"<br>" },				 		
					{name:"#F.supplierName", 		type:"TEXT",  	bind:"supplierName", back:false, mode:"READONLY"}]},	 									 	
				{items:[{name:"#L.currencyCode",	type:"LABEL", 	value:"幣別<font color='red'>*</font>" }]},
				{items:[{name:"#F.currencyCode",	type:"SELECT", 	bind:"currencyCode", init:allCurrencyCodes }]},	
				{items:[{name:"#L.openingBankCode", type:"LABEL", 	value:"銀行<font color='red'>*</font>" }]},
				{items:[{name:"#F.openingBankCode", type:"TEXT",	bind:"openingBankCode", eChange: function(){ changeBankSupplierName("openingBankCode"); }, size:15 },
				 		{name:"#B.bankSupplierCode",	value:"選取" ,type:"PICKER" ,
	 									 		openMode:"open", src:"./images/start_node_16.gif",
	 									 		service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 		left:0, right:0, width:1024, height:768,	
	 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("openingBankCode");}},
	 					{name:"#F.bankAddressBookId",	type:"TEXT",  	bind:"bankAddressBookId", back:false, mode:"HIDDEN"},
	 					{name:"#L.openingBank", type:"LABEL", 	value:"<br>" },
						{name:"#F.openingBank", type:"TEXT", 	bind:"openingBank", size:15, mode:"READONLY" }]},	
	 			{items:[{name:"#L.createBy",	type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",	type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",	type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.creationDate",	type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",	type:"TEXT", 	bind:"creationDate", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.latestShipmentDate",	type:"LABEL", 	value:"最後裝運日" }]},
				{items:[{name:"#F.latestShipmentDate",	type:"DATE", 	bind:"latestShipmentDate", size:1}]},
				{items:[{name:"#L.expiryDate",			type:"LABEL", 	value:"有效日期" }]},
				{items:[{name:"#F.expiryDate",			type:"DATE", 	bind:"expiryDate"}]},
				{items:[{name:"#L.openingAmount",		type:"LABEL", 	value:"開狀金額" }]},
				{items:[{name:"#F.openingAmount",		type:"NUMM", 	bind:"openingAmount", maxLen:14, dec:4, eChange:"changeCountAllAmount()"}]},
				{items:[{name:"#L.openingFees",			type:"LABEL", 	value:"開狀手續費" }]},
				{items:[{name:"#F.openingFees",			type:"NUMM", 	bind:"openingFees", maxLen:12, dec:4}], td:" colSpan=3"} 
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.poNo",	type:"LABEL", 	value:"PO單號" }]},
				{items:[{name:"#F.poNo",	type:"TEXT", 	bind:"poNo", size:150, maxLen:200 }], td:" colSpan=9"}
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});

}

// 修狀明細檔
function kweImAltDetail(){

	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
  
	vat.item.make(vnB_ALT_Detail, "indexNo"			, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_ALT_Detail, "alterLcDate"		, {type:"DATE" , size:10, maxLen:10, desc:"修改日期"          	});
	vat.item.make(vnB_ALT_Detail, "alterAmount"		, {type:"NUMM" , size:12, maxLen:12, dec:4, desc:"修改金額"    	}); // , eChange:"changeO()"
	vat.item.make(vnB_ALT_Detail, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_ALT_Detail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_ALT_Detail, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_ALT_Detail, "message"         , {type:"MSG"  , desc:"訊息"}); 
	vat.block.pageLayout(vnB_ALT_Detail, {
														id: "vatAltDetailDiv",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_ALT_Detail+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_ALT_Detail+")",
														saveSuccessAfter    : "saveSuccessAfter()" 
														});
	vat.block.pageDataLoad(vnB_ALT_Detail, vnCurrentPage = 1);
}

// 信用狀明細檔
function kweImDetail(){
	var status = vat.item.getValueByName("#F.status");
//	var isOpen = false;
//	if( status == "FINISH" ){
//		isOpen = true;
//	}
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
	 
	vat.item.make(vnB_Detail, "indexNo"			, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_Detail, "receiveNo"		, {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"進貨單號"          });
	vat.item.make(vnB_Detail, "receiveAmount"	, {type:"NUMM" , size:8, maxLen:12, dec:4, mode:"READONLY", desc:"進貨金額" });
	vat.item.make(vnB_Detail, "creditAmount"	, {type:"NUMM" , size:8, maxLen:16, dec:4, desc:"折讓金額(Credit noted)", eChange:"changeReceiveAmount()" });
	vat.item.make(vnB_Detail, "arriveDate"		, {type:"DATE" , size:8, maxLen:8, desc:"到單日"          	});
	vat.item.make(vnB_Detail, "dueDate"			, {type:"DATE" , size:8, maxLen:8, desc:"due date"          });
	vat.item.make(vnB_Detail, "returnAmount"	, {type:"NUMM" , size:8, maxLen:12, dec:4, desc:"還款金額"         	});
	vat.item.make(vnB_Detail, "returnFees"		, {type:"NUMM" , size:8, maxLen:12, dec:4, desc:"還款手續費"          });
	vat.item.make(vnB_Detail, "remark1"			, {type:"TEXT" , size:12, maxLen:100, desc:"備註"          	});
	vat.item.make(vnB_Detail, "lineId"          , {type:"HIDDEN"});
	
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
														saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

// 金額統計
function kweImAmount(){
	vat.block.create(vnB_Amount, {
		id: "vatAmountDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[  
			{row_style:"", cols:[
				{items:[{name:"#L.totalLcAmount", 		type:"LABEL", 	value:"信用狀總額"}]},
				{items:[{name:"#F.totalLcAmount", 		type:"NUMM", 	bind:"totalLcAmount" ,size:15, back:false,mode:"READONLY" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalReceiveAmount", 	type:"LABEL", 	value:"進貨總額"}]},
				{items:[{name:"#F.totalReceiveAmount", 	type:"NUMM", 	bind:"totalReceiveAmount" ,size:15, back:false,mode:"READONLY" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalCreditNoted", 	type:"LABEL", 	value:"Credit noted"}]},
				{items:[{name:"#F.totalCreditNoted", 	type:"NUMM", 	bind:"creditNoted" ,size:15, back:false,mode:"READONLY" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.restAmount", 			type:"LABEL", 	value:"信用狀剩餘金額"}]},
				{items:[{name:"#F.restAmount", 			type:"NUMM", 	bind:"restAmount" ,size:15, back:false,mode:"READONLY" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalReturnAmount", 	type:"LABEL", 	value:"還款金額合計"}]},
				{items:[{name:"#F.totalReturnAmount", 	type:"NUMM", 	bind:"totalReturnAmount" ,size:15, back:false,mode:"READONLY" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalReturnFees", 	type:"LABEL", 	value:"還款手續費合計"}]},
				{items:[{name:"#F.totalReturnFees", 	type:"NUMM", 	bind:"totalReturnFees" ,size:15, back:false,mode:"READONLY" }]}
			]}
	 	],
		beginService:"",
		closeService:""			
	});
	
}

// 第一次載入 重新整理
function loadBeforeAjxService(div){
//    alert("loadBeforeAjxService");	
	var processString = "";
	if( vnB_ALT_Detail === div){
		processString = "process_object_name=imLetterOfCreditService&process_object_method_name=getAJAXAltPageData" + 
		                    "&headId=" + vat.item.getValueByName("#H.headId");
		return processString;				
	}else if( vnB_Detail === div ){
		processString = "process_object_name=imLetterOfCreditService&process_object_method_name=getAJAXPageData" + 
		                    "&headId=" + vat.item.getValueByName("#H.headId");
		return processString;	
	}							
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
//    alert("saveBeforeAjxService"); 
	var processString = "" ;
	if( vnB_ALT_Detail === div){
		processString = "process_object_name=imLetterOfCreditService&process_object_method_name=updateOrSaveAJAXAltPageLinesData" + 
			"&headId=" + vat.item.getValueByName("#H.headId") + 
			"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value  +
			"&status=" + vat.item.getValueByName("#F.status");
	}else if( vnB_Detail === div ){
		processString = "process_object_name=imLetterOfCreditService&process_object_method_name=updateAJAXPageLinesData" + 
			"&headId=" + vat.item.getValueByName("#H.headId") +
			"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value  +
			"&status=" + vat.item.getValueByName("#F.status");
	}		
	return processString;
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(){ 
//	alert("saveSuccessAfter");
	vat.ajax.XHRequest({
           post:"process_object_name=imLetterOfCreditService"+
                    "&process_object_method_name=countAllTotalAmount"+
                    "&headId=" + vat.item.getValueByName("#H.headId") + 
                    "&openingAmount=" + vat.item.getValueByName("#F.openingAmount"),
           find: function change(oXHR){ 
           		vat.item.setValueByName("#F.totalLcAmount", vat.ajax.getValue("totalLcAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.totalReceiveAmount", vat.ajax.getValue("totalReceiveAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.totalCreditNoted", vat.ajax.getValue("totalCreditAmount", oXHR.responseText));
           		// head
           		vat.item.setValueByName("#F.creditNoted", vat.ajax.getValue("totalCreditAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.restAmount", vat.ajax.getValue("restAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.totalReturnAmount", vat.ajax.getValue("totalReturnAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.totalReturnFees", vat.ajax.getValue("totalReturnFees", oXHR.responseText));
           		
           } 
		});	 
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
} 


// tab切換 存檔
function doPageDataSave(div){
//	alert("doPageDataSave");

	var status = vat.item.getValueByName("#F.status");
	if(status == "SAVE" || status == "FINISH"  ){ 
		if(vnB_ALT_Detail===div){
			activeTab = vnB_ALT_Detail;
//			alert("vnB_ALT_Detail");
			vat.block.pageSearch(vnB_Detail); //存檔vnB_Detail
			vat.block.pageRefresh(div);
		}else if(vnB_Detail===div){
//			alert("vnB_Detail");
			activeTab = vnB_Detail;
			vat.block.pageSearch(vnB_ALT_Detail); //存檔vnB_ALT_Detail
			vat.block.pageRefresh(div);
		}else if(vnB_Amount===div){
//			alert("vnB_Amount");
			vat.block.pageSearch(vnB_ALT_Detail); //存檔vnB_ALT_Detail
			vat.block.pageSearch(vnB_Detail); //存檔vnB_Detail
			vat.block.pageRefresh(div);
		}
	}
}
	
// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	
    	vat.bean().vatBeanPicker.result = null;  
     	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
    	refreshForm("");
/*
		vat.bean().vatBeanOther.formId       = "";
      	vat.bean().vatBeanOther.currentRecordNumber = 0;
     	vat.bean().vatBeanOther.lastRecordNumber = 0;
    	vat.bean.init( 
    		function(){
				return "process_object_name=imLetterOfCreditService&process_object_method_name=executeInitial"; 
    		},{
    			other: true 
    		}
    	);
    	vat.item.bindAll();  
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");*/
	 }
}

function createRefreshForm(){
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
		  
		  var vsheadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(vsheadId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}

// 供應商picker 回來執行
function doAfterPickerFunctionProcess( code ){
	//do picker back something
	if(vat.bean().vatBeanPicker.result !== null){
	
		if("openingBankCode" == code ){
			vat.item.setValueByName("#F.bankAddressBookId", vat.bean().vatBeanPicker.result[0].addressBookId); 
			changeBankSupplierName("bankAddressBookId");
		}else if( "supplierCode" == code ){
			vat.item.setValueByName("#F.addressBookId", vat.bean().vatBeanPicker.result[0].addressBookId); 
			changeSupplierName("addressBookId");
		}
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
	}else if("CLOSE" == formAction){
		alertMessage = "是否確定結案?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}
	
	if(confirm(alertMessage)){
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var approvalResult        = "true"; // vat.item.getValueByName("#F.approvalResult"); 
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
	
//		var formStatus = status;
//		if("SAVE" == formAction){
//	        formStatus = "SAVE";
//	    }else if("SUBMIT" == formAction || "SUBMIT_BG" == formAction ){
//	        formStatus = changeFormStatus(formId, processId, status, formAction);
//	    }else if("VOID" == formAction){
//	        formStatus = "VOID";
//	    }
	
		vat.bean().vatBeanOther.formAction = formAction;
		vat.bean().vatBeanOther.status = status;
		
		if(( status == "SAVE" || status == "FINISH" ) ||
		   			(inProcessing   && (status == "SAVE") )){
		
			vat.block.pageDataSave( activeTab ,{ 
				funcSuccess:function(){
					vat.bean().vatBeanOther.formAction 		= formAction;
					vat.bean().vatBeanOther.beforeStatus 	= status; // 前 一個狀態
		  			vat.bean().vatBeanOther.processId       = processId;
	  				vat.bean().vatBeanOther.approvalResult  = approvalResult;
	  				vat.bean().vatBeanOther.approvalComment =  approvalComment;
	  				
					if("SUBMIT_BG" == formAction){
				      	vat.block.submit(function(){return "process_object_name=imLetterOfCreditAction"+
				                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
				    }else{
						vat.block.submit(function(){return "process_object_name=imLetterOfCreditAction"+
				                    "&process_object_method_name=performTransaction";},{
				                    bind:true, link:true, other:true,
				                    funcSuccess:function(){
						        		vat.block.pageRefresh(activeTab);
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

function changeO(){
	var vLineId                   = vat.item.getGridLine();	 
	
	alert( "alterLcDate("+vLineId+") = " + vat.item.getGridValueByName("alterLcDate", vLineId) ); 
	alert( "isDeleteRecord("+vLineId+") = " + vat.item.getGridValueByName("isDeleteRecord", vLineId) );       
}

// 動態改變銀行供應商名稱
function changeBankSupplierName( code ){
//	alert( code + "\n" + vat.item.getValueByName("#F.bankAddressBookId") +"\n" + vat.item.getValueByName("#F.openingBankCode") );

	vat.ajax.XHRequest({
		post:"process_object_name=buSupplierWithAddressViewService"+
                  "&process_object_method_name=getAJAXSupplierName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&addressBookId=" + ( "bankAddressBookId" === code ? vat.item.getValueByName("#F.bankAddressBookId") : "" )+
                  "&supplierCode=" + ( "openingBankCode" === code ? vat.item.getValueByName("#F.openingBankCode") : "" ),
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName("#F.openingBankCode", vat.ajax.getValue("supplierCode", oXHR.responseText));
         		vat.item.setValueByName("#F.openingBank", vat.ajax.getValue("supplierName", oXHR.responseText) );
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.openingBank", "查無此銀行供應商");
		}   
	});	
}

// 動態改變廠商供應商名稱
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
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.supplierName", "查無此廠商供應商");
		}   
	});	
}

// 動態改變幣別名稱
function changeCurrencyName(){
	vat.ajax.XHRequest(
       {
           post:"process_object_name=imLetterOfCreditService"+
                    "&process_object_method_name=getAJAXCurrencyName"+
                    "&currencyCode=" + vat.item.getValueByName("#F.currencyCode"),
           find: function change(oXHR){ 
           		vat.item.setValueByName("#F.currencyCode", vat.ajax.getValue("currencyCode", oXHR.responseText));
           		vat.item.setValueByName("#F.currencyName", vat.ajax.getValue("currencyName", oXHR.responseText));
           },
           fail: function changeError(){
           		vat.item.setValueByName("#F.currencyName", "查無此幣別名稱");
           }   
       });	
}
// 動態改變進貨金額
function changeReceiveAmount(){
	var vLineId                   = vat.item.getGridLine();	
	vat.ajax.XHRequest({
          post:"process_object_name=imLetterOfCreditService"+
                   "&process_object_method_name=getAJAXReceiveAmount"+
                   "&receiveAmount=" + vat.item.getGridValueByName("receiveAmount", vLineId) +
                   "&creditAmount=" + vat.item.getGridValueByName("creditAmount", vLineId),
                   
          find: function change(oXHR){ 
          	vat.item.setGridValueByName("returnAmount", vLineId, vat.ajax.getValue("returnAmount", oXHR.responseText));
          },
          fail: function changeError(){
          	vat.item.setGridValueByName("returnAmount", vLineId, "0.0");
          }   
    });	
}

// 刷新金額統計
function changeCountAllAmount(){
	vat.ajax.XHRequest({
           post:"process_object_name=imLetterOfCreditService"+
                    "&process_object_method_name=countAllTotalAmount"+
                    "&headId=" + vat.item.getValueByName("#H.headId") + 
                    "&openingAmount=" + vat.item.getValueByName("#F.openingAmount"),
           find: function change(oXHR){ 
           		vat.item.setValueByName("#F.totalLcAmount", vat.ajax.getValue("totalLcAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.totalReceiveAmount", vat.ajax.getValue("totalReceiveAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.totalCreditNoted", vat.ajax.getValue("totalCreditAmount", oXHR.responseText));
           		// head
           		vat.item.setValueByName("#F.creditNoted", vat.ajax.getValue("totalCreditAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.restAmount", vat.ajax.getValue("restAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.totalReturnAmount", vat.ajax.getValue("totalReturnAmount", oXHR.responseText));
           		vat.item.setValueByName("#F.totalReturnFees", vat.ajax.getValue("totalReturnFees", oXHR.responseText));
           		vat.block.pageRefresh(vnB_Amount);
           } 
		});	 
	
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var vsheadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsheadId);
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
	  	var vsheadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsheadId);
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
	  	var vsheadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsheadId);
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
	    var vsheadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsheadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(vsheadId){
	document.forms[0]["#formId"            ].value = vsheadId;
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = "";   
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=imLetterOfCreditAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
 //    	   	alert("I use bindAll");
     		 vat.item.bindAll();
			 vat.block.pageDataLoad(vnB_ALT_Detail, vnCurrentPage = 1);
			 vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1); 
			 
//			vat.block.pageRefresh(vnB_ALT_Detail, vnCurrentPage = 1);
//			vat.block.pageRefresh(vnB_Detail, vnCurrentPage = 1);
//			vat.block.pageRefresh(vnB_Amount, vnCurrentPage = 1);
			doFormAccessControl();
     	}});
}

// 依狀態鎖form
function doFormAccessControl(){
	var status = vat.item.getValueByName("#F.status");
	var processId = null;
	if( vat.bean().vatBeanOther.processId != null ){
		processId	= vat.bean().vatBeanOther.processId; 
	}
	var formId = vat.bean().vatBeanOther.formId;
	// 初始化
	//============================head====================================
//	vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", false);
//	vat.item.setAttributeByName("vatAltDetailDiv", "readOnly", false);
//	vat.item.setAttributeByName("vatDetailDiv"   , "readOnly", true);

//	var lcDate = document.getElementById("#F.lcDate");
//	lcDate += "\nlcDate.datatype =" + lcDate.datatype +"\n";
//	alert( "change前" + lcDate );
	
	vat.item.setAttributeByName("#F.lcNo", 					"readOnly", false);
	vat.item.setAttributeByName("#F.lcDate", 				"readOnly", false);
	
//	var lcDate2 = document.getElementById("#F.lcDate");
//	lcDate2 += "\nlcDate2.datatype =" + lcDate2.datatype +"\n";
//	alert( "change後" + lcDate2 );
	vat.item.setAttributeByName("#F.openingBankCode", 		"readOnly", false);
	
	vat.item.setAttributeByName("#F.supplierCode", 			"readOnly", false);
	vat.item.setAttributeByName("#B.supplierCode", 			"readOnly", false);
	vat.item.setAttributeByName("#B.bankSupplierCode", 		"readOnly", false);
	vat.item.setAttributeByName("#F.currencyCode",			"readOnly", false);
	vat.item.setAttributeByName("#F.latestShipmentDate", 	"readOnly", false);
	vat.item.setAttributeByName("#F.expiryDate", 			"readOnly", false);
	vat.item.setAttributeByName("#F.openingAmount", 		"readOnly", false);
	vat.item.setAttributeByName("#F.openingFees", 			"readOnly", false);
	vat.item.setAttributeByName("#F.poNo", 					"readOnly", false);
	//==============================修狀明細============================================
	vat.item.setGridAttributeByName("alterLcDate", 			"readOnly", false);
	vat.item.setGridAttributeByName("alterAmount", 			"readOnly", false);
	vat.block.canGridDelete(vnB_ALT_Detail, true, true);
	vat.block.canGridAppend(vnB_ALT_Detail, true, true);
//	vat.block.canGridModify([vnB_ALT_Detail], true,true,true);
//	vat.item.setAttributeByName("vatAltDetailDiv", "readOnly", false, true, true);
	//=============================信用狀明細=============================================
	vat.item.setGridAttributeByName("receiveNo", 			"readOnly", true); 
	vat.item.setGridAttributeByName("receiveAmount", 		"readOnly", true);
	vat.item.setGridAttributeByName("creditAmount", 		"readOnly", true);
	vat.item.setGridAttributeByName("arriveDate", 			"readOnly", true);
	vat.item.setGridAttributeByName("dueDate", 				"readOnly", true);
	vat.item.setGridAttributeByName("returnAmount", 		"readOnly", true);
	vat.item.setGridAttributeByName("returnFees", 			"readOnly", true);
	vat.item.setGridAttributeByName("remark1", 				"readOnly", true); 
	//==============================buttonLine============================================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.close", 	"display", "none"); 
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	//================================================================
	
	if(formId != ""){
		// 流程內
		if( processId != null && processId != 0 ){ //從待辦事項進入
			vat.item.setAttributeByName("#F.lcNo", 					"readOnly", true);
			if( status == "SAVE" || status == "FINISH" ){
				if(status == "SAVE"){
					vat.item.setStyleByName("#B.void" ,	"display", "inline"); 
				}
				vat.item.setStyleByName("#B.new", 		"display", "none");
				vat.item.setStyleByName("#B.search", 	"display", "none");
				
			}
		}else{
			// 查詢回來
			if( status == "SAVE" ){
				vat.item.setStyleByName("#B.submit", 	"display", "none"); 
				vat.item.setStyleByName("#B.save", 		"display", "none"); 
				vat.item.setStyleByName("#B.submitBG", 	"display", "none"); 
				vat.item.setStyleByName("#B.message", 	"display", "none"); 
				
				vat.item.setAttributeByName("#F.lcNo", 					"readOnly", true);
				vat.item.setAttributeByName("#F.lcDate", 				"readOnly", true);
				vat.item.setAttributeByName("#F.openingBankCode", 		"readOnly", true);
				vat.item.setAttributeByName("#F.supplierCode", 			"readOnly", true);
				vat.item.setAttributeByName("#B.supplierCode", 			"readOnly", true);
				vat.item.setAttributeByName("#B.bankSupplierCode", 		"readOnly", true);
				vat.item.setAttributeByName("#F.currencyCode",			"readOnly", true);
				vat.item.setAttributeByName("#F.latestShipmentDate", 	"readOnly", true);
				vat.item.setAttributeByName("#F.expiryDate", 			"readOnly", true);
				vat.item.setAttributeByName("#F.openingAmount", 		"readOnly", true);
				vat.item.setAttributeByName("#F.openingFees", 			"readOnly", true);
				vat.item.setAttributeByName("#F.poNo", 					"readOnly", true);
				
				vat.item.setGridAttributeByName("alterLcDate", 			"readOnly", true);
				vat.item.setGridAttributeByName("alterAmount", 			"readOnly", true);
				vat.block.canGridDelete(vnB_ALT_Detail, false, false);
				vat.block.canGridAppend(vnB_ALT_Detail, false, false);
//				vat.block.canGridModify([vnB_ALT_Detail], false,false,false);
//				vat.item.setAttributeByName("vatAltDetailDiv", "readOnly", true, true, true);
			}
		}
	}
	
	if(status == "FINISH" || status == "CLOSE" || status == "VOID" ){ 
//		vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
		vat.item.setAttributeByName("#F.lcNo", 					"readOnly", true);
		
		
		vat.item.setStyleByName("#B.save", "display", "none");
		
		if(status == "FINISH"){
//			vat.item.setAttributeByName("vatDetailDiv"   , "readOnly", false);
			vat.item.setAttributeByName("#F.lcDate", 				"readOnly", false);
		
			vat.item.setAttributeByName("#F.openingBankCode", 		"readOnly", false);
			vat.item.setAttributeByName("#F.supplierCode", 			"readOnly", false);
			vat.item.setAttributeByName("#B.supplierCode", 			"readOnly", false);
			vat.item.setAttributeByName("#B.bankSupplierCode", 		"readOnly", false);
			vat.item.setAttributeByName("#F.currencyCode",			"readOnly", false);
			vat.item.setAttributeByName("#F.latestShipmentDate", 	"readOnly", false);
			vat.item.setAttributeByName("#F.expiryDate", 			"readOnly", false);
			vat.item.setAttributeByName("#F.openingAmount", 		"readOnly", false);
			vat.item.setAttributeByName("#F.openingFees", 			"readOnly", false);
			vat.item.setAttributeByName("#F.poNo", 					"readOnly", false);
		
			vat.item.setStyleByName("#B.close" , "display", "inline"); 
			vat.item.setStyleByName("#B.submit" , "display", "inline");
			
			vat.item.setGridAttributeByName("creditAmount", "readOnly", false);
			vat.item.setGridAttributeByName("arriveDate", "readOnly", false);
			vat.item.setGridAttributeByName("dueDate", "readOnly", false);
			vat.item.setGridAttributeByName("returnAmount", "readOnly", false);
			vat.item.setGridAttributeByName("returnFees", "readOnly", false);
			vat.item.setGridAttributeByName("remark1", "readOnly", false);
		}
		
		if(status == "CLOSE" || status == "VOID"  ){
//			vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
//			vat.item.setAttributeByName("vatAltDetailDiv", "readOnly", true);
			vat.item.setAttributeByName("#F.lcDate", 				"readOnly", true);
		
			vat.item.setAttributeByName("#F.openingBankCode", 		"readOnly", true);
			vat.item.setAttributeByName("#F.supplierCode", 			"readOnly", true);
			vat.item.setAttributeByName("#B.supplierCode", 			"readOnly", true);
			vat.item.setAttributeByName("#B.bankSupplierCode", 		"readOnly", true);
			vat.item.setAttributeByName("#F.currencyCode",			"readOnly", true);
			vat.item.setAttributeByName("#F.latestShipmentDate", 	"readOnly", true);
			vat.item.setAttributeByName("#F.expiryDate", 			"readOnly", true);
			vat.item.setAttributeByName("#F.openingAmount", 		"readOnly", true);
			vat.item.setAttributeByName("#F.openingFees", 			"readOnly", true);
			vat.item.setAttributeByName("#F.poNo", 					"readOnly", true);
		
		
			vat.item.setStyleByName("#B.submit" , "display", "none");
			vat.item.setStyleByName("#B.submitBG" , "display", "none");
			vat.item.setStyleByName("#B.message" , "display", "none");
			
			vat.item.setGridAttributeByName("alterLcDate", "readOnly", true);
			vat.item.setGridAttributeByName("alterAmount", "readOnly", true);
			vat.block.canGridDelete(vnB_ALT_Detail, false, false);
			vat.block.canGridAppend(vnB_ALT_Detail, false, false);
//			vat.block.canGridModify([vnB_ALT_Detail], false,false,false);
//			vat.item.setAttributeByName("vatAltDetailDiv", "readOnly", true, true, true);
		}
		
	}
	
	
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_LETTEOFCREDIT" +
		"&levelType=ERROR" +
        "&processObjectName=imLetterOfCreditService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#H.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imLetterOfCreditAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}