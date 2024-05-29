/*** 
 *	檔案: cmCustomsDeclarationSearch.js
 *	說明：海關進出倉查詢
 *	修改：Joe
 *  <pre>
 *  	Created by Joe
 *  	All rights reserved.
 *  </pre>
 */
 
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Detail = 1;

function kweImBlock(){
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();
  kweImDetail();
}

function kweImSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     orderTypeCode     	: document.forms[0]["#orderTypeCode"    ].value,	
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value,
  	     incharge           : document.forms[0]["#incharge"       ].value  
	    };
	    vat.bean.init(	
	  		function(){
				return "process_object_name=soSalesOrderMainService&process_object_method_name=executeSearchInitial"; 
	    	},{								
	    		other: true
    	});
  }
  
}

function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      	, type:"IMG"      , value:"查詢",  src:"./images/button_find.gif"	, eClick:"doSearch()"},
	 			{name:"SPACE"          	, type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       	, type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"	, eClick:"resetForm()"},
	 			{name:"SPACE"          	, type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        	, type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"	, eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"			, type:"LABEL"    , value:"　"},
	 			{name:"#B.view"      	, type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"	, eClick:"doClosePicker()"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function kweImHeader(){ 
	//var allStatus = vat.bean("allStatus");
	var allStatus = [["","",true],["暫存","簽核中","簽核完成","駁回","作廢","結案","未確認"],["SAVE","SIGNING","FINISH","REJECT","VOID","CLOSE","UNCONFIRMED"]];
// 排序依據  Maco 2015.08.11
	var allOrderType= [["","",true],["交易序號","依銷貨單號","依銷貨日期","依更新日期","依狀態"],["transactionSeqNo","orderNo","salesOrderDate","lastUpdateDate","status"]];
// 遞增or遞減排序 Maco 2015.08.11
	var allOrderSeq= [["","",true],["從大到小排列","從小到大排列"],["desc","adc"]];
// 歸帳地區  Maco 2017.07.13
	var allIncharge = [[true, true, true], [ "桃園","馬祖南竿","高雄","馬祖福澳"],  [ "T2F03","T2F04","T2F05","T2F08"]];
	var allAvailableWarehouse = vat.bean("allAvailableWarehouse");
	var allShopMachine = vat.bean("allShopMachine");
	
	
	vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"銷貨單查詢作業", rows:[  
			{row_style:"", cols:[
				{items:[{name:"#L.startOrderNo",	type:"LABEL", value:"銷貨單號"}]},	 
	 			{items:[{name:"#F.startOrderNo",	type:"TEXT"	, size:16},
	 			 		{name:"#L.endOrderNo",		type:"LABEL", value:" 至 "},
	 			 		{name:"#F.endOrderNo",		type:"TEXT"	, size:16}],td:" colSpan=1"},
	 			{items:[{name:"#L.customsNo", 		type:"LABEL", value:"上傳單號"}]},	 
	 			{items:[{name:"#F.customsNo", 		type:"TEXT", size:14},
	 					{name:"#L.endCustomsNo",		type:"LABEL", value:" 至 "},
	 			 		{name:"#F.customsNo1",		type:"TEXT"	, size:16}],td:" colSpan=3"}, 		
	 		    {items:[{name:"#L.status", 		type:"LABEL", value:"狀態"}]},	 
	 			{items:[{name:"#F.status", 		type:"SELECT",init:allStatus,	size:10}],td:" colSpan=1"},
	 		    {items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},
	 			{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", back:false, size:2, mode:"HIDDEN"},
	         			{name:"#F.brandName", type:"TEXT", bind:"brandName", size:8, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
	 		    {items:[{name:"#L.customerCode",		type:"LABEL", value:"客戶代號"}]},	 
	 			{items:[{name:"#F.customerCode",		type:"TEXT"	, size:10, onchange:"onChangeCustomerCode()"},
	 					{name:"#B.customerCode", value:"選取", type:"PICKER" ,
				 									openMode:"open", src:"./images/start_node_16.gif",
				 									service:"Bu_AddressBook:searchCustomer:20100101.page", 
				 									left:0, right:0, width:1024, height:768,	
				 									serviceAfterPick:function(){doAfterPickerCustomer();} },
		 				{name:"#F.customerName", type:"TEXT", bind:"customerName", back:false, size:12, mode:"READONLY"}]},
	 		    {items:[{name:"#L.salesOrderStartDate",	type:"LABEL", value:"銷貨日期"}]},	 
	 			{items:[{name:"#F.salesOrderStartDate",	type:"DATE"	, size:10},
	 			 		{name:"#L.salesOrderEndDate",	type:"LABEL", value:" 至 "},
	 			 		{name:"#F.salesOrderEndDate",	type:"DATE"	, size:10}],td:" colSpan=3"},
				{items:[{name:"#L.scheduleShipDate",	type:"LABEL", value:"預計出貨日期"}]},	 
				{items:[{name:"#F.scheduleShipDate",	type:"DATE"	, size:10}]},
				{items:[{name:"#L.schedule",	type:"LABEL", value:"班次"}]},	 
				{items:[{name:"#F.schedule",	type:"TEXT"	, size:10}]}
			]},
			{row_style:"", cols:[
	 		    {items:[{name:"#L.superintendentCode",			type:"LABEL" , value:"負責人員"}]},	 
	 			{items:[{name:"#F.superintendentCode",			type:"TEXT"	 , size:10, onchange:"onChangeSuperintendent()"},
	 					{name:"#B.superintendentCode", type:"PICKER", value:"查詢",  src:"./images/start_node_16.gif",
	 									 				openMode:"open", 
	 									 				service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 				left:0, right:0, width:1024, height:768,	
	 									 				serviceAfterPick:function(){doAfterPickerEmp();}},
						{name:"#F.superintendentName", type:"TEXT",   bind:"superintendentName", size:12, mode:"READONLY" }]},
	 		    {items:[{name:"#L.scheduleCollectionStartDate",	type:"LABEL" , value:"付款日期"}]},	 
	 			{items:[{name:"#F.scheduleCollectionStartDate",	type:"DATE"	 , size:10},
	 			 		{name:"#L.scheduleCollectionEndDate",	type:"LABEL" , value:" 至 "},
	 			 		{name:"#F.scheduleCollectionEndDate",	type:"DATE"	 , size:10}],td:" colSpan=3"},
				{items:[{name:"#L.defaultWarehouseCode",		type:"LABEL" , value:"庫別"}]},	 
				{items:[{name:"#F.incharge",					type:"SELECT", init:allIncharge},
						{name:"#F.defaultWarehouseCode",		type:"SELECT", init:allAvailableWarehouse}]},
				{items:[{name:"#L.posMachineCode",				type:"LABEL" , value:"機台編號"}]},	 
				{items:[{name:"#F.posMachineCode",				type:"SELECT", init:allShopMachine}]}
			]},
			{row_style:"", cols:[
	 		    {items:[{name:"#L.transactionSeqNo",			type:"LABEL" , value:"交易序號"}]},	 
	 			{items:[{name:"#F.transactionSeqNo",			type:"TEXT"	 , size:15},
	 			//{name:"#L.transactionSeqNo1",			type:"LABEL" , value:"至"},
	 			{name:"#F.transactionSeqNo1",			type:"TEXT"	 , size:15 , mode:"HIDDEN"}]},
				{items:[{name:"#L.customerPoNo",				type:"LABEL" , value:"原(客戶)訂單編號"}]},	 
				{items:[{name:"#F.customerPoNo",				type:"TEXT", size:16},
						{name:"#L.endCustomerPoNo1",					type:"LABEL" , value:" 至 "},
						{name:"#F.customerPoNo1",						type:"TEXT", size:16}],td:" colSpan=3"},
//排序 Maco 2015.08.11
				{items:[{name:"#L.eventCode",					type:"LABEL" , value:"活動代號"}]},	 
				{items:[{name:"#F.eventCode", 					type:"TEXT", bind:"eventCode",init:allOrderSeq,	size:10}], td:"colSpan=3"}
			]},
			{row_style:"", cols:[
//排序 Maco 2015.08.11
				{items:[{name:"#L.orderBySeq",					type:"LABEL" , value:"排序順序"}]},	 
				{items:[{name:"#F.orderBySeq", 					type:"SELECT", bind:"orderBySeq",init:allOrderSeq,	size:10}],td:" colSpan=5"},
				{items:[{name:"#L.orderByType",					type:"LABEL" , value:"排序條件"}]},	 
				{items:[{name:"#F.orderByType", 					type:"SELECT", bind:"orderByType",init:allOrderType,	size:10}],td:" colSpan=3"}
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function kweImDetail(){
  
	var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
	var vbSelectionType = "CHECK";    
    if(vatPickerId != null && vatPickerId != ""){
		vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
		vbSelectionType = "CHECK";    
    }else{
		vat.item.setStyleByName("#B.view" , "display", "none");
		vbSelectionType = "NONE";
    }

    vat.item.make(vnB_Detail, "indexNo"         		, {type:"IDX" });
	vat.item.make(vnB_Detail, "orderTypeCode"   		, {type:"TEXT" , size:2 , view:"fixed" , mode:"READONLY", desc:"單別"      });
	vat.item.make(vnB_Detail, "orderNo"     			, {type:"TEXT" , size:14 , view:"fixed" , mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "salesOrderDate"  		, {type:"TEXT" , size:10 , view:"fixed" , mode:"READONLY", desc:" 銷貨日期"      });
	vat.item.make(vnB_Detail, "schedule"  				, {type:"TEXT" , size:2 , view:"fixed" , mode:(document.forms[0]["#loginBrandCode"].value==='T2')?"READONLY":"HIDDEN", desc:" 班次"      });
	vat.item.make(vnB_Detail, "customerPoNo"  			, {type:"TEXT" , size:15 , view:"fixed" , mode:"READONLY", desc:"原訂單編號"      });//ADD BY JOEYWU 20100709
	vat.item.make(vnB_Detail, "transactionSeqNo"  		, {type:"TEXT" , size:20 , view:"fixed" , mode:(document.forms[0]["#loginBrandCode"].value==='T2')?"READONLY":"HIDDEN", desc:"交易序號"      });
	vat.item.make(vnB_Detail, "customsNo"  				, {type:"TEXT" , size:20 , view:"fixed" , mode:(document.forms[0]["#loginBrandCode"].value==='T2')?"READONLY":"HIDDEN", desc:"海關上傳單號"      });
	vat.item.make(vnB_Detail, "superintendentName"  	, {type:"TEXT" , size:2 , view:"" , mode:"READONLY", desc:"負責人員"      });
	vat.item.make(vnB_Detail, "defaultWarehouseCode"	, {type:"TEXT" , size:6 , view:"" , mode:"READONLY", desc:"庫別"      });
	vat.item.make(vnB_Detail, "totalActualSalesAmount"  , {type:"NUMM" , size:17 , view:"" , mode:"READONLY", desc:"實際銷售金額"      });
	vat.item.make(vnB_Detail, "posMachineCode"     		, {type:"TEXT" , size:2 , view:"" , mode:(document.forms[0]["#loginBrandCode"].value==='T2')?"READONLY":"HIDDEN", desc:"機台號碼"      });
	vat.item.make(vnB_Detail, "statusName"     			, {type:"TEXT" , size:2 , view:"" , mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "customerName"     		, {type:"TEXT" , size:2 , view:"shift" , mode:"READONLY", desc:"客戶"      });
	vat.item.make(vnB_Detail, "eventCode"     			, {type:"TEXT" , size:20 , view:"shift" , mode:"READONLY", desc:"活動代號"      });
	vat.item.make(vnB_Detail, "lastUpdatedBy"     		, {type:"TEXT" , size:2 , view:"shift" , mode:"READONLY", desc:"最後更新人員"      });
	vat.item.make(vnB_Detail, "lastUpdateDate"     		, {type:"TEXT" , size:4 , view:"shift" , mode:"READONLY", desc:"更新日期"      });
	vat.item.make(vnB_Detail, "headId"          		, {type:"ROWID", view:"shift"});
	vat.block.pageLayout(vnB_Detail, {
									id                  : "vatDetailDiv",
									pageSize            : 10,
									searchKey           : ["headId"],
									selectionType       : vbSelectionType,
									indexType           : "AUTO",
			                        canGridDelete       : vbCanGridDelete,
									canGridAppend       : vbCanGridAppend,
									canGridModify       : vbCanGridModify,		
									loadSuccessAfter    : "kwePageLoadSuccess()",						
									loadBeforeAjxService: "loadBeforeAjxService()",
									saveBeforeAjxService: "saveBeforeAjxService()",
									saveSuccessAfter    : "saveSuccessAfter()",
                                 	blockId             : vnB_Detail,
									indicate            : 
										function(){
											if(vatPickerId != null && vatPickerId != ""){
												return false}else{ openModifyPage()}
										}
                        });

}

function kwePageSaveMethod(){}					


function saveSuccessAfter(){
	 //alert("更新成功");
}

function kwePageLoadSuccess(){
	 if( vat.block.getGridObject(vnB_Detail).dataCount == vat.block.getGridObject(vnB_Detail).pageSize &&
	    vat.block.getGridObject(vnB_Detail).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

function kwePageAppendBeforeMethod(){
    //alert("kwePageAppendBeforeMethod");
	// return confirm("你確定要新增嗎?"); 
	return true;
}

function kwePageAppendAfterMethod(){
	 return alert("新增完畢");
}


function loadBeforeAjxService(){
	var processString = "process_object_name=soSalesOrderMainService&process_object_method_name=getAJAXSearchPageData" + 
                     	"&orderTypeCode=" 		+ document.forms[0]["#orderTypeCode"].value +
                     	"&incharge=" 			+ vat.item.getValueByName("#F.incharge") +
                     	"&startOrderNo=" 		+ vat.item.getValueByName("#F.startOrderNo") +
                     	"&endOrderNo=" 			+ vat.item.getValueByName("#F.endOrderNo") +
                     	"&customsNo=" 			+ vat.item.getValueByName("#F.customsNo") +
                     	"&customsNo1=" 			+ vat.item.getValueByName("#F.customsNo1") +
                     	"&status=" 				+ vat.item.getValueByName("#F.status") +
                     	"&customerCode=" 		+ vat.item.getValueByName("#F.customerCode") +
                     	"&salesOrderStartDate=" + vat.item.getValueByName("#F.salesOrderStartDate") +
                     	"&salesOrderEndDate=" 	+ vat.item.getValueByName("#F.salesOrderEndDate") +
                     	"&scheduleShipDate=" 	+ vat.item.getValueByName("#F.scheduleShipDate") +
                     	"&superintendentCode=" 	+ vat.item.getValueByName("#F.superintendentCode") +
                     	"&defaultWarehouseCode="+ vat.item.getValueByName("#F.defaultWarehouseCode") +
                     	"&scheduleCollectionStartDate=" + vat.item.getValueByName("#F.scheduleCollectionStartDate") +
                     	"&scheduleCollectionEndDate=" 	+ vat.item.getValueByName("#F.scheduleCollectionEndDate") +
                     	"&transactionSeqNo=" 	+ vat.item.getValueByName("#F.transactionSeqNo") +
                     	"&transactionSeqNo1=" 	+ vat.item.getValueByName("#F.transactionSeqNo1") +
                     	"&customerPoNo=" 		+ vat.item.getValueByName("#F.customerPoNo") +
                     	"&customerPoNo1=" 		+ vat.item.getValueByName("#F.customerPoNo1") +
                     	"&posMachineCode=" 		+ vat.item.getValueByName("#F.posMachineCode") +
                     	"&eventCode=" 		+ vat.item.getValueByName("#F.eventCode") +
                      	"&brandCode=" + vat.item.getValueByName("#F.brandCode") +
                      	"&schedule=" + vat.item.getValueByName("#F.schedule") +
                      	"&orderByType=" +  vat.item.getValueByName("#F.orderByType") +
                      	"&orderBySeq=" +  vat.item.getValueByName("#F.orderBySeq") ;
	return processString;											
}


function onChangeCustomerCode(){
    var customerCode = vat.item.getValueByName("#F.customerCode").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.customerCode", customerCode);
    if(customerCode != ""){
        vat.ajax.XHRequest({
           post:"process_object_name=buCustomerWithAddressViewService"+
                    "&process_object_method_name=findCustomerByTypeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&customerCode=" + customerCode +
                    "&searchCustomerType=customerCode",
           find: function changeCustomerCodeRequestSuccess(oXHR){
           		vat.item.setValueByName("#F.customerName", vat.ajax.getValue("CustomerName", oXHR.responseText));
           }});
	}else{
		vat.item.setValueByName("#F.customerName","");
	} 
}

// 客戶picker 回來執行
function doAfterPickerCustomer(){
	if(typeof vat.bean().vatBeanPicker.customerResult != "undefined"){
    	vat.item.setValueByName("#F.customerCode", vat.bean().vatBeanPicker.customerResult[0].customerCode); 
		onChangeCustomerCode();
	}
}


/* */
function onChangeSuperintendent() {
	var superintendentCode = vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setValueByName("#F.superintendentCode", superintendentCode) ;
	if(superintendentCode != ""){
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySuperintendent" +
							"&superintendentCode="  + superintendentCode;
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("superintendentName", vat.ajax.xmlHttp.responseText))
			}
		});
	}else{
		vat.item.setValueByName("#F.superintendentName","");
	} 
}


/* 採購負責人picker 回來執行 */
function doAfterPickerEmp(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataByEmp"+
							"&employeeCode="  + vat.bean().vatBeanPicker.result[0].employeeCode ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.superintendentCode", vat.bean().vatBeanPicker.result[0].employeeCode );
				vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("employeeName", vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}


//	判斷是否要關閉LINE

function checkEnableLine() {
	return true;
}


//	取得SAVE要執行的JS FUNCTION

function saveBeforeAjxService() {
	var processString = "process_object_name=soSalesOrderMainService&process_object_method_name=saveSearchResult";
	return processString;
}								



function doPageDataSave(){
    //alert("doPageDataSave");
    vat.block.pageDataSave(0);
}

function doPageRefresh(){
    //alert("doPageRefresh");
    vat.block.pageRefresh(0);
}


//	PICKER 之前要先RUN LINE SAVE
function doBeforePicker(){
    vat.block.pageDataSave(0);
}


function changeRelationData(){
    //alert("kwePageAppendAfterMethod");
    afterSavePageProcess = "changeRelationData";
    vat.block.pageDataSave(0);
}

//查詢
function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope
			                     }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
}


//清除
function resetForm(){
	vat.item.setValueByName("#F.startOrderNo", "");
    vat.item.setValueByName("#F.endOrderNo", "");
    vat.item.setValueByName("#F.customsNo", "");
    vat.item.setValueByName("#F.customsNo1", "");
    vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.customerCode", "");
    vat.item.setValueByName("#F.customerName", "");
    vat.item.setValueByName("#F.salesOrderStartDate", "");
    vat.item.setValueByName("#F.salesOrderEndDate", "");
    vat.item.setValueByName("#F.scheduleShipDate", "");
    vat.item.setValueByName("#F.superintendentCode", "");
    vat.item.setValueByName("#F.superintendentName", "");
    vat.item.setValueByName("#F.scheduleCollectionStartDate", "");
    vat.item.setValueByName("#F.scheduleCollectionEndDate", "");
    vat.item.setValueByName("#F.defaultWarehouseCode", "");
}

//離開
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}

//檢視
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		      	function(){ return "process_object_name=soSalesOrderMainService&process_object_method_name=getSearchSelection";
    		        }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(!(vFormId == "" || vFormId == "0")){
	    var url = "/erp/So_SalesOrder:create:20100101.page?formId=" + vFormId+"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value+"&incharge="+ document.forms[0]["#incharge"].value; 
	     sc=window.open(url, '銷貨單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	     sc.resizeTo((screen.availWidth),(screen.availHeight));
	     sc.moveTo(0,0);
	}
}
