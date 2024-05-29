/***
 *	檔案: soDeliveryInventoryCounts.js
 *	說明：表單明細
 *	修改：Lara
 *  <pre>
 *  	Created by Lara
 *  	All rights reserved.
 *  </pre>
 */

vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function kweBlock(){

  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     orderTypeCode      : "DZC",
	     formId             : document.forms[0]["#formId"            ].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	 };
	 vat.bean.init(function(){
	 return "process_object_name=soDeliveryInventoryService&process_object_method_name=executeInitial";
     },{other: true});
     
 	 //vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
  	 //vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);	
  }

  kweButtonLine();
  kweHeader();
  
  if (typeof vat.tabm != 'undefined') {
	  vat.tabm.createTab(0, "vatTabSpan", "H", "float");
	  vat.tabm.createButton(0 ,"xTab1","明細檔資料" ,"vnB_Detail"    ,"images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false);  
  }
  	
  kweDetail();	
  
  vat.item.setValueByName("#F.downloadFlag","N");
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
	 									 service:"So_Delivery:inventoryCountsSearch:20111129.page",
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"}, 			
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示"   ,   src:"./images/button_message_prompt.gif"  , eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.download"    , type:"IMG"    ,value:"庫存下載",   src:"./images/button_download.gif"  ,eClick:'doAction("INS")'},
	 			{name:"#B.cancel"      , type:"IMG"    ,value:"重下庫存",   src:"./images/button_cancel_download.gif"  ,eClick:'doAction("DEL")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.produceIC"   , type:"IMG" ,value:"產生盤點檔",   src:"./images/button_generate_ic_file.gif" , eClick:'produceICFile()'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.listReport"  , type:"IMG" ,value:"盤點清單",   src:"./images/button_inventory_list.gif" , eClick:'openReportWindow("1")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.differReport", type:"IMG" ,value:"盤點差異表",   src:"./images/button_inventory_differ.gif" , eClick:'openReportWindow()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.countsReport", type:"IMG" ,value:"盤點表",   src:"./images/button_inventory.gif" , eClick:'openReportWindow("3")'},
	 			
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
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

function kweHeader(){

var allOrderTypes = vat.bean("allOrderTypes");
var allStoreArea  = vat.bean("allStoreArea");
var allRange = vat.bean("allRange");
var isImportedFiles = [["", true, false], ["否","是 "], ["N","Y"]];

vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"盤點單維護作業", rows:[  
	{row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別<font color='red'>*</font>"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", back:false, size:1, mode:"READONLY",  init:allOrderTypes}]},		 
	 {items:[{name:"#L.orderNo", type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.orderNo", type:"TEXT"  ,  bind:"orderNo", back:false, size:20, mode:"READONLY"}]},	 
	 {items:[{name:"#L.countsDate", type:"LABEL" , value:"盤點日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.countsDate", type:"DATE", bind:"countsDate", size:1, eChange:'changeCountsType()'}]},		 		 	 		 	 		 
	 {items:[{name:"#L.status"      , type:"LABEL" , value:"狀態"}]},	 		 
	 {items:[{name:"#F.status"      , type:"TEXT"  ,  bind:"status",  mode:"HIDDEN"},
	 		 {name:"#F.downloadFlag"   , type:"TEXT" ,  size:4, bind:"downloadFlag", mode:"HIDDEN"},
	  		 {name:"#F.statusName"  , type:"TEXT"  ,  bind:"statusName", size:4, back:false, mode:"READONLY"},
	 		 {name:"#F.headId", type:"TEXT"  ,  bind:"headId", back:false, size:2, mode:"READONLY"},
	 		 {name:"#F.brandCode", type:"TEXT"  ,  bind:"brandCode", back:false, size:2, mode:"HIDDEN"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.storeArea"   , type:"LABEL" , value:"存放地點"}]},
	 {items:[{name:"#F.storeArea"   , type:"SELECT"  ,  bind:"storeArea",  init:allStoreArea}]},
	 {items:[{name:"#L.range"   , type:"LABEL" , value:"列"}]},
	 {items:[{name:"#F.range"   , type:"SELECT"  ,  bind:"range",  init:allRange}]},
	 {items:[{name:"#L.flightDate", type:"LABEL" , value:"回程日期"}]},
	 {items:[{name:"#F.flightDate", type:"DATE", bind:"flightDate", size:1, eChange:''}]},
	 {items:[{name:"#L.storageCode", type:"LABEL" , value:"儲位"}]},
	 {items:[{name:"#F.storageCodeStart", type:"TEXT"  ,  bind:"storageCodeStart",  size:6, maxLen:5},
	 		 {name:"#L.between"       , type:"LABEL" , value:"至"},
	 		 {name:"#F.storageCodeEnd", type:"TEXT"  ,  bind:"storageCodeEnd", size:6, maxLen:5}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.countsId"   , type:"LABEL" , value:"盤點代號<font color='red'>*</font>"}]},
	 {items:[{name:"#F.countsId", type:"TEXT"  ,  bind:"countsId", size:20}]},
	 {items:[{name:"#L.superintendentCode", type:"LABEL", value:"匯入人員<font color='red'>*</font>"}]},
	 {items:[{name:"#F.superintendentCode", type:"TEXT", bind:"superintendentCode", size:6, eChange:"getEmployeeInfo()"},
	         {name:"#F.superintendentName", type:"TEXT", bind:"superintendentName", mode:"READONLY",  size:10}]},
	 {items:[{name:"#L.isImportedFile"   , type:"LABEL" , value:"已匯入盤點檔"}]},
	 {items:[{name:"#F.isImportedFile"   , type:"SELECT"  ,  bind:"isImportedFile", mode:"READONLY", init:isImportedFiles}]},
	 {items:[{name:"#L.importedTimes"   , type:"LABEL" , value:"盤點次數"}]},
	 {items:[{name:"#F.importedTimes"   , type:"TEXT"  ,  bind:"importedTimes", mode:"READONLY"}]}]},		 	  		 
	 {row_style:"", cols:[
	 {items:[{name:"#L.createdBy", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.createdBy", type:"TEXT", bind:"createdBy", mode:"HIDDEN", size:1},
	         {name:"#F.createdByName", type:"TEXT", bind:"createdByName", mode:"READONLY", size:12}]},	 	 	    	
	 {items:[{name:"#L.lastUpdateDate", type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.lastUpdateDate", type:"TEXT",   bind:"lastUpdateDate", mode:"READONLY", size:10}], td:" colSpan=5"}]}	 
	], 	 
		beginService:"",
		closeService:""			
	});
}

function kweDetail(){
    var statusTmp = vat.item.getValueByName("#F.status");
    var brandCode = vat.item.getValueByName("#F.brandCode");
	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = false;	
	// set column
    vat.item.make(vnB_Detail, "indexNo", {type:"IDX" , desc:"序號"});
	vat.item.make(vnB_Detail, "deliveryOrderNo", {type:"TEXT", size:30, maxLen:20, desc:"入提單號", mode:"READONLY"});
	vat.item.make(vnB_Detail, "storageCodeSys", {type:"TEXT", size:20, maxLen:20, desc:"系統儲位", mode:"READONLY"});
	vat.item.make(vnB_Detail, "bagCountsSys", {type:"TEXT", size:10, maxLen:20, desc:"系統袋數", mode:"READONLY"});
	vat.item.make(vnB_Detail, "storageCode", {type:"TEXT", size:20, maxLen:20, desc:"盤點儲位", mode:"READONLY"});
	vat.item.make(vnB_Detail, "bagCounts", {type:"TEXT", size:10, maxLen:20, desc:"盤點袋數", mode:"READONLY"});
	vat.item.make(vnB_Detail, "lastUpdateByName", {type:"TEXT", size:10, maxLen:20, desc:"盤點人員", mode:"READONLY"});	
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息", mode:"HIDDEN"});
	vat.block.pageLayout(vnB_Detail, {
									id                  : "vatDetailDiv",
									pageSize            : 10,
			                        canGridDelete       : vbCanGridDelete,
									canGridAppend       : vbCanGridAppend,
									canGridModify       : vbCanGridModify,	
									loadBeforeAjxService: "loadBeforeAjxService()",
									loadSuccessAfter    : "pageLoadSuccess()",	
									saveBeforeAjxService: "saveBeforeAjxService()",
									saveSuccessAfter    : "saveSuccessAfter()"
									});
									
	vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);
	doFormAccessControl();
}

function getEmployeeInfo() {
    
    if ("" !=vat.item.getValueByName("#F.superintendentCode")) {
        vat.item.setValueByName("#F.superintendentCode",vat.item.getValueByName("#F.superintendentCode").toUpperCase());

        vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.superintendentCode");
        vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=getEmployeeInfo";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("" != vat.bean().vatBeanOther.employeeName ){
                    vat.item.setValueByName("#F.superintendentName", vat.bean().vatBeanOther.employeeName);
                    vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.superintendentCode");
                    vat.bean().vatBeanOther.executeEmployeeName=vat.item.getValueByName("#F.superintendentName");
			     }else{
			     　　vat.item.setValueByName("#F.superintendentName", "");
			     	alert("員工代號錯誤，請重新輸入！");
	 				vat.form.item.setFocus( "#F.superintendentCode" );			     	
			     }
		     }
		});
    }else{
    	vat.item.setValueByName("#F.superintendentName","");
    }
}

function loadBeforeAjxService(){
	var processString = "process_object_name=soDeliveryInventoryService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId")
	//alert("After loadBeforeAjxService");
	return processString;
}

function pageLoadSuccess(){}

function saveBeforeAjxService(){
	var processString = "";
	//alert("saveBeforeAjxService");
	processString = "process_object_name=soDeliveryInventoryService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status")+ "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode;;

	return processString;
}

function saveSuccessAfter(){}

function doSubmit(formAction){
	var vsAllowSubmit         = true;
	var alertMessage          ="是否確定送出?";
	var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');;
    var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');;
    var employeeCode          = vat.bean().vatBeanOther.loginEmployeeCode.replace(/^\s+|\s+$/, '');;
    
    if(confirm(alertMessage)){
    	var vsBrandCode = vat.item.getValueByName("#F.brandCode");
		var vsStatus = vat.item.getValueByName("#F.status");

		if (formAction=="SUBMIT"){
	    	if(vat.item.getValueByName("#F.countsId")== ""){
	    		alert("請輸入盤點代號！");
	    		vsAllowSubmit = false;
	    	}
	    	if(vat.item.getValueByName("#F.superintendentCode")==""){
	    		alert("請輸入盤點人員！");
	    		vsAllowSubmit = false;
	    	}
    	}

    	if(vsAllowSubmit){
			  vat.bean().vatBeanOther.formId          = formId;
			  vat.bean().vatBeanOther.status          = status;
			  vat.bean().vatBeanOther.formAction      = formAction;
  	  	      vat.bean().vatBeanOther.executeEmployee =  vat.item.getValueByName("#F.superintendentCode");
	          processString = "process_object_name=soDeliveryInventoryAction&process_object_method_name=performTransaction";
			  vat.block.submit(function(){return processString;},{
		                    bind:true, link:true, other:true,
		                    funcSuccess:function(){
		                    	refreshForm(formId);
				        	}}
			  );	
		}
    }
}

function doAction(vAction){

   	var Sstorage = vat.item.getValueByName("#F.storageCodeStart");
   	var Estorage = vat.item.getValueByName("#F.storageCodeEnd");  
   	
	if ((Sstorage != "" && Estorage == "") ||  (Sstorage == "" && Estorage != ""))
	{
		alert("請輸入儲位起迄！");
	}
	else
	{
		var message = "";
			
		vat.bean().vatBeanOther.subOrderTypeCode = "DZN";
		//vat.bean().vatBeanOther.subOrderNo = vat.item.getValueByName("#F.orderNo" );
		vat.bean().vatBeanOther.flightDate = vat.item.getValueByName("#F.flightDate" );
		vat.bean().vatBeanOther.storeArea = vat.item.getValueByName("#F.storeArea");
		vat.bean().vatBeanOther.range = vat.item.getValueByName("#F.range" );
		vat.bean().vatBeanOther.storageCodeStart = vat.item.getValueByName("#F.storageCodeStart" );
		vat.bean().vatBeanOther.storageCodeEnd = vat.item.getValueByName("#F.storageCodeEnd" );
		vat.bean().vatBeanOther.doAction = vAction;
		vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId" );
		
		vat.block.submit(function(){return "process_object_name=soDeliveryInventoryService"+
	            "&process_object_method_name=saveLine";},  {other:true,picker:false,
	     funcSuccess: function() {		
	     	message = vat.bean().vatBeanOther.message;
	     	if (message != ""){
	     		alert(message);} 
	     	else{
	     		if (vAction=="INS"){
	     			vat.item.setValueByName("#F.downloadFlag","Y");}
	     		else{
	     			vat.item.setValueByName("#F.downloadFlag","N");
		     		vat.item.setValueByName("#F.flightDate","");
					vat.item.setValueByName("#F.storeArea","");
					vat.item.setValueByName("#F.range","");
					vat.item.setValueByName("#F.storageCodeStart","");
					vat.item.setValueByName("#F.storageCodeEnd","");}
					
	     			vat.block.pageRefresh(vnB_Detail);
	     			}
	     			
	     			doFormAccessControl();
	     		}		     
			});
	}
}

function refreshForm(vsHeadId) {
    document.forms[0]["#formId"].value = vsHeadId;

    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
    vat.block.submit(
    function () {
        return "process_object_name=soDeliveryInventoryService&process_object_method_name=executeInitial";
    }, {
        other: true,
        funcSuccess: function () {
            vat.item.bindAll();
            vat.block.pageRefresh(vnB_Detail);
            doFormAccessControl();   
        }
    });
}

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
	    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}

}

function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	refreshForm("");
	 }
}

function createRefreshForm(){
	vat.item.setValueByName("#F.headId","");
	refreshForm("");
}

function tempRedirect()
{
}

function doAfterPickerProcess(){
	//alert("doAfterPickerProcess")
	if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize == 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;

		  refreshForm(vsHeadId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" +
		"?programId=SO_DELIVERY" +
		"&levelType=ERROR" +
        "&processObjectName=soDeliveryInventoryService" +
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

//列印
function openReportWindow() {
	//vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.loginBrandCode");
	vat.bean().vatBeanOther.executeEmployeeCode = vat.bean().vatBeanOther.loginEmployeeCode;
   	vat.bean().vatBeanOther.startOrderNo =vat.item.getValueByName("#F.orderNo");
   	vat.bean().vatBeanOther.endOrderNo   =vat.item.getValueByName("#F.orderNo");
   	
   	vat.block.submit(function () {
	        return "process_object_name=soDeliveryInventoryService" + "&process_object_method_name=getReportConfig";
	    }, {
	        other: true,
	        funcSuccess: function () {
	            eval(vat.bean().vatBeanOther.reportUrl);
	        }
	    });
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();

}

function doFormAccessControl(){
	var vsFormStatus = vat.item.getValueByName("#F.status");
	
    vat.item.setStyleByName("#B.new"         , "display", "inline");
	vat.item.setStyleByName("#B.search"      , "display", "inline");
	vat.item.setStyleByName("#B.download"    , "display", "none");
	vat.item.setStyleByName("#B.cancel"      , "display", "none");
	vat.item.setStyleByName("#B.exit"        , "display", "inline");
	vat.item.setStyleByName("#B.submit"      , "display", "inline");
	vat.item.setStyleByName("#B.save"        , "display", "inline");
	vat.item.setStyleByName("#B.void"        , "display", "inline");
	vat.item.setStyleByName("#B.message"     , "display", "inline");
	vat.item.setStyleByName("#B.differReport" , "display", "inline");
	
	var vStatus = true;
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
	if (vsFormStatus=="SAVE"){
		vat.item.setStyleByName("#B.void"        , "display", "none");
		vat.item.setStyleByName("#B.differReport" , "display", "none");
		
		if (vat.item.getValueByName("#F.downloadFlag")=="" || vat.item.getValueByName("#F.downloadFlag")=="N"){
			vat.item.setStyleByName("#B.download"  , "display", "inline");
			vat.item.setStyleByName("#B.cancel"    , "display", "none");
			}else{
			vat.item.setStyleByName("#B.download"  , "display", "none");
			vat.item.setStyleByName("#B.cancel"    , "display", "inline");
		}
			
		vStatus = vat.item.getValueByName("#F.downloadFlag")=="" || vat.item.getValueByName("#F.downloadFlag")=="N"?false:true;
		
		vat.item.setAttributeByName("#F.countsDate", "readOnly", false);
	    vat.item.setAttributeByName("#F.countsId", "readOnly", false);
	    vat.item.setAttributeByName("#F.superintendentCode", "readOnly", false);
	}
	else if (vsFormStatus=="COUNTING"){
		vat.item.setStyleByName("#B.differReport" , "display", "none");
        vat.item.setStyleByName("#B.save"        , "display", "none");
		vat.item.setStyleByName("#B.void"        , "display", "none");
		
		vat.item.setAttributeByName("#F.countsDate", "readOnly", false);
	    vat.item.setAttributeByName("#F.countsId", "readOnly", true);
	    vat.item.setAttributeByName("#F.superintendentCode", "readOnly", true);
	}
	else if (vsFormStatus=="COUNT_FINISH" || vsFormStatus=="VOID"){
		vat.item.setStyleByName("#B.submit"      , "display", "none");
		vat.item.setStyleByName("#B.save"        , "display", "none");
		vat.item.setStyleByName("#B.void"        , "display", "none");
		vat.item.setStyleByName("#B.message"     , "display", "none");
		
		vat.item.setAttributeByName("#F.countsDate", "readOnly", true);
	    vat.item.setAttributeByName("#F.countsId", "readOnly", true);
	    vat.item.setAttributeByName("#F.superintendentCode", "readOnly", true);
	    
		if (vsFormStatus=="VOID")
			vat.item.setStyleByName("#B.differReport" , "display", "none");
	}
	
	vat.item.setAttributeByName("#F.storeArea", "readOnly", vStatus);
	vat.item.setAttributeByName("#F.range", "readOnly", vStatus);
	vat.item.setAttributeByName("#F.flightDate", "readOnly", vStatus);
	vat.item.setAttributeByName("#F.storageCodeStart", "readOnly", vStatus);
	vat.item.setAttributeByName("#F.storageCodeEnd", "readOnly", vStatus);
}
