/*** 
 *	檔案: imReturnSearch.js
 *	說明：退貨單查詢
 *	修改：Caspar
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
				return "process_object_name=imDeliveryMainService&process_object_method_name=executeSearchInitialReturn"; 
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
	var allStatus = [["","",true],["暫存","簽核中","簽核完成","駁回","作廢","結案"],["SAVE","SIGNING","FINISH","REJECT","VOID","CLOSE"]];
	vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"銷退單查詢作業", rows:[  
			{row_style:"", cols:[
				{items:[{name:"#L.startOrderNo",	type:"LABEL", value:"銷退單號"}]},	 
	 			{items:[{name:"#F.startOrderNo",	type:"TEXT"	, size:20},
	 			 		{name:"#L.endOrderNo",		type:"LABEL", value:" 至 "},
	 			 		{name:"#F.endOrderNo",		type:"TEXT"	, size:20}]},
	 		    {items:[{name:"#L.status", 		type:"LABEL", value:"狀態"}]},	 
	 			{items:[{name:"#F.status", 		type:"SELECT",init:allStatus,	size:10}]}
			]},
			{row_style:"", cols:[
	 		    {items:[{name:"#L.startShipDate",	type:"LABEL", value:"退貨日期"}]},	 
	 			{items:[{name:"#F.startShipDate",	type:"DATE"	, size:10},
	 			 		{name:"#L.endShipDate",	type:"LABEL", value:" 至 "},
	 			 		{name:"#F.endShipDate",	type:"DATE"	, size:10}]},
	 		    {items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},
	 			{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", back:false, size:2, mode:"HIDDEN"},
	         			{name:"#F.brandName", type:"TEXT", bind:"brandName", size:8, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
	 		    {items:[{name:"#L.transactionSeqNo",			type:"LABEL" , value:"交易序號"}]},	 
	 			{items:[{name:"#F.transactionSeqNo",			type:"TEXT"	 , size:20}]},
				{items:[{name:"#L.customerPoNo",				type:"LABEL" , value:"原(客戶)訂單編號"}]},	 
				{items:[{name:"#F.customerPoNo",				type:"TEXT", size:20}]}
			]},
			{row_style:"", cols:[
	 		    {items:[{name:"#L.customerCode",		type:"LABEL", value:"客戶代號"}]},	 
	 			{items:[{name:"#F.customerCode",		type:"TEXT"	, size:20, onchange:"onChangeCustomerCode()"},
	 					{name:"#B.customerCode", value:"選取", type:"PICKER" ,
				 									openMode:"open", src:"./images/start_node_16.gif",
				 									service:"Bu_AddressBook:searchCustomer:20100101.page", 
				 									left:0, right:0, width:1024, height:768,	
				 									serviceAfterPick:function(){doAfterPickerCustomer();} },
		 				{name:"#F.customerName", type:"TEXT", bind:"customerName", back:false, size:12, mode:"READONLY"}]},
				{items:[{name:"#L.customsNo", type:"LABEL" , value:"上傳單號"}]},	 
				{items:[{name:"#F.customsNo", type:"TEXT", size:14}]}
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

    vat.item.make(vnB_Detail, "indexNo"         			, {type:"IDX"});
	vat.item.make(vnB_Detail, "orderTypeCode"   			, {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"退貨單別"});
	vat.item.make(vnB_Detail, "orderNo"     				, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"退貨單號"});
	vat.item.make(vnB_Detail, "ShipDate"  					, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:" 退貨日期"});
	vat.item.make(vnB_Detail, "customerName"     			, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:"客戶" });
	vat.item.make(vnB_Detail, "origDeliveryOrderTypeCode"	, {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"出貨單別"});
	vat.item.make(vnB_Detail, "origDeliveryOrderNo"			, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"出貨單號"});
	vat.item.make(vnB_Detail, "statusName"     				, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:"狀態"});
	vat.item.make(vnB_Detail, "lastUpdateDate"     			, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:"更新日期"});
	vat.item.make(vnB_Detail, "customsNo"     				, {type:"TEXT" , size:14, maxLen:20, mode:"READONLY", desc:"上傳單號"});
	vat.item.make(vnB_Detail, "headId"          			, {type:"ROWID"});
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
	var processString = "process_object_name=imDeliveryMainService&process_object_method_name=getAJAXSearchPageDataReturn" + 
                     	"&orderTypeCode=" 		+ document.forms[0]["#orderTypeCode"].value +
                     	"&incharge=" 			+ document.forms[0]["#incharge"].value +
                     	"&startOrderNo=" 		+ vat.item.getValueByName("#F.startOrderNo") +
                     	"&endOrderNo=" 			+ vat.item.getValueByName("#F.endOrderNo") +
                     	"&status=" 				+ vat.item.getValueByName("#F.status") +
                     	"&startShipDate=" 		+ vat.item.getValueByName("#F.startShipDate") +
                     	"&endShipDate=" 		+ vat.item.getValueByName("#F.endShipDate") +
                     	"&transactionSeqNo=" 	+ vat.item.getValueByName("#F.transactionSeqNo") +
                     	"&customerPoNo=" 		+ vat.item.getValueByName("#F.customerPoNo") +
                     	"&customsNo=" 			+ vat.item.getValueByName("#F.customsNo") +
                     	"&customerCode=" 		+ vat.item.getValueByName("#F.customerCode") +
                      	"&brandCode=" 			+ vat.item.getValueByName("#F.brandCode") ;
	return processString;											
}


//	判斷是否要關閉LINE

function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的JS FUNCTION

function saveBeforeAjxService() {
	var processString = "process_object_name=imDeliveryMainService&process_object_method_name=saveSearchResultReturn";
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
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
}


//清除
function resetForm(){
	vat.item.setValueByName("#F.startOrderNo", "");
    vat.item.setValueByName("#F.endOrderNo", "");
    vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.startShipDate", "");
    vat.item.setValueByName("#F.endShipDate", "");
    vat.item.setValueByName("#F.transactionSeqNo", "");
    vat.item.setValueByName("#F.customerPoNo", "");
    vat.item.setValueByName("#F.customsNo", "");
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
    		      	function(){ return "process_object_name=imDeliveryMainService&process_object_method_name=getSearchSelection";
    		        }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(!(vFormId == "" || vFormId == "0")){
	    var url = "/erp/Im_Delivery:return:20091019.page?formId=" + vFormId+"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value; 
	     sc=window.open(url, '銷貨退回單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	     sc.resizeTo((screen.availWidth),(screen.availHeight));
	     sc.moveTo(0,0);
	}
}

// 客戶picker 回來執行
function doAfterPickerCustomer(){
	if(typeof vat.bean().vatBeanPicker.customerResult != "undefined"){
    	vat.item.setValueByName("#F.customerCode", vat.bean().vatBeanPicker.customerResult[0].customerCode); 
		onChangeCustomerCode();
	}
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
                    "&searchCustomerType=customerCode" +
                    "&isEnable=",
           find: function changeCustomerCodeRequestSuccess(oXHR){
           		vat.item.setValueByName("#F.customerName", vat.ajax.getValue("CustomerName", oXHR.responseText));
           }});
	}else{
		vat.item.setValueByName("#F.customerName","");
	} 
}

