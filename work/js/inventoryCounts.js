vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){
    formDataInitial();
    buttonLine();
    headerInitial();
    if(typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
	    vat.tabm.createTab(0, "vatTabSpan", "H", "float");                                                                                                                                   
		vat.tabm.createButton(0 , "xTab1", "明細資料" , "vnB_Detail", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif",false);
		//vat.tabm.createButton(0 ,"xTab5","簽核資料" ,"vatApprovalDiv","images/tab_approval_data_dark.gif","images/tab_approval_data_light.gif",false);                                                                                                                        
    }
    detailInitial();
    doFormAccessControl();
}

function formDataInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' &&
      document.forms[0]["#formId"].value != '[binding]'){
  	  vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"].value,   
	     formId             : document.forms[0]["#formId"].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	    }; 
	    
      vat.bean.init(function(){
		return "process_object_name=imInventoryCountsAction&process_object_method_name=performInitial"; 
      },{other: true});
  }
       vat.item.bindAll();
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
	 									 servicePassData:function()
										  	{return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode")}, 
	 									 service:"Im_InventoryCounts:search:20090818.page",
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
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"}, 			
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示"   ,   src:"./images/button_message_prompt.gif"  , eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.produceIC"   , type:"IMG" ,value:"產生盤點檔",   src:"./images/button_generate_ic_file.gif" , eClick:'produceICFile()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.listReport"  , type:"IMG" ,value:"盤點清單",   src:"./images/button_inventory_list.gif" , eClick:'openReportWindow("1")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.differReport", type:"IMG" ,value:"盤點差異表",   src:"./images/button_inventory_differ.gif" , eClick:'openReportWindow("2")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.countsReport", type:"IMG" ,value:"盤點表",   src:"./images/button_inventory.gif" , eClick:'openReportWindow("3")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.detailSave"  , type:"IMG" ,value:"明細存檔",   src:"./images/button_save_data.gif" , eClick:'detailSave()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
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
	
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

function headerInitial(){
var allOrderTypes = vat.bean("allOrderTypes");
var allCountsType = vat.bean("allCountsType");
var isImportedFileOpt = [["", true, true], ["是 ", "否"], ["Y", "N"]];
 
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"盤點單維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別<font color='red'>*</font>"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", back:false, size:1, mode:"READONLY", init:allOrderTypes}]},		 
	 {items:[{name:"#L.orderNo", type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.orderNo", type:"TEXT"  ,  bind:"orderNo", back:false, size:20, mode:"READONLY"}]},	 
	 {items:[{name:"#L.countsDate", type:"LABEL" , value:"實際盤點日<font color='red'>*</font>"}]},
	 {items:[{name:"#F.countsDate", type:"DATE", bind:"countsDate", size:1, eChange:'changeCountsType()'}]},		 		 	 		 
	 {items:[{name:"#L.brandCode"   , type:"LABEL" , value:"品牌"}]},	 
	 {items:[{name:"#F.brandCode"   , type:"TEXT"  ,  bind:"brandCode", back:false, size:1, mode:"READONLY"},
	 		 {name:"#F.brandName"   , type:"TEXT"  ,  bind:"brandName", back:false, size:10, mode:"READONLY"}]},	 		 
	 {items:[{name:"#L.status"      , type:"LABEL" , value:"狀態"}]},	 		 
	 {items:[{name:"#F.status"      , type:"TEXT"  ,  bind:"status", size:6, mode:"HIDDEN"},
	  		 {name:"#F.statusName"  , type:"TEXT"  ,  bind:"statusName", size:6, back:false, mode:"READONLY"},
	 		 {name:"#F.headId", type:"TEXT"  ,  bind:"headId", back:false, size:2, mode:"READONLY"}]}]},	  		 
	 {row_style:"", cols:[
	 {items:[{name:"#L.inventoryDate", type:"LABEL" , value:"庫存日期"}]},
	 {items:[{name:"#F.inventoryDate", type:"DATE", bind:"inventoryDate", size:1}]},
	 {items:[{name:"#L.warehouseCode", type:"LABEL" , value:"庫別<font color='red'>*</font>"}]},
	 {items:[{name:"#F.warehouseCode", type:"TEXT", bind:"warehouseCode", size:8, maxLen:15, eChange:'changeWarehouseCode()'},
	         {name:"#F.warehouseName", type:"TEXT", bind:"warehouseName", back:false, size:6, mode:"READONLY"}]},	 	 
	 {items:[{name:"#L.countsType", type:"LABEL" , value:"盤點方式<font color='red'>*</font>"}]},
	 {items:[{name:"#F.countsType", type:"SELECT", bind:"countsType", init:allCountsType, eChange:'changeCountsType()'}]},	
	 {items:[{name:"#L.lastUpdatedBy", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.lastUpdatedBy", type:"TEXT", bind:"lastUpdatedBy", mode:"HIDDEN", size:1},
	         {name:"#F.lastUpdatedByName", type:"TEXT", bind:"lastUpdatedByName", mode:"READONLY", size:10}]},	 	 	    	
	 {items:[{name:"#L.lastUpdateDate", type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.lastUpdateDate", type:"TEXT",   bind:"lastUpdateDate", mode:"READONLY", size:10}]}]}, 	 
 	 {row_style:"", cols:[
	 {items:[{name:"#L.countsId", type:"LABEL" , value:"盤點代號<font color='red'>*</font>"}]},
	 {items:[{name:"#F.countsId", type:"TEXT", bind:"countsId", size:15, maxLen:15}]},
	 {items:[{name:"#L.countsLotNo", type:"LABEL" , value:"盤點批號"}]},
	 {items:[{name:"#F.countsLotNo", type:"TEXT", bind:"countsLotNo", size:15, maxLen:15}]},	 
	 {items:[{name:"#L.superintendentCode", type:"LABEL" , value:"盤點人<font color='red'>*</font>"}]},
	 {items:[{name:"#F.superintendentCode", type:"TEXT", bind:"superintendentCode", size:8, maxLen:10, eChange:'changeSuperintendent()'},
	         {name:"#F.superintendentName", type:"TEXT", bind:"superintendentName", size:6, mode:"READONLY"}]},	          	 
	 {items:[{name:"#L.actualSuperintendentCode", type:"LABEL", value:"實盤人"}]},
	 {items:[{name:"#F.actualSuperintendentCode", type:"TEXT", bind:"actualSuperintendentCode", size:8, maxLen:10, eChange:'changeActualSuperintendent()'},
	         {name:"#F.actualSuperintendentName", type:"TEXT", bind:"actualSuperintendentName", size:6, mode:"READONLY"}]},	         
	 {items:[{name:"#L.countsType", type:"LABEL" , value:"已匯入盤點檔"}]},
	 {items:[{name:"#F.isImportedFile", type:"SELECT", bind:"isImportedFile", mode:"READONLY", init:isImportedFileOpt}]}]},	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.taxName"      , type:"LABEL"  ,  value:"稅別"}]},/////////////////////////
	 {items:[{name:"#F.taxName"  , type:"TEXT"  ,  bind:"taxName", back:false, mode:"READONLY"},
	 		 {name:"#F.taxCode"  , type:"TEXT"  ,  bind:"taxCode", back:false, mode:"HIDDEN"}]},	  	 
	 {items:[{name:"#L.description", type:"LABEL" , value:"說明"}]},
	 {items:[{name:"#F.description", type:"TEXT", bind:"description", size:100, maxLen:100}], td:" colSpan=5"},	         
	 {items:[{name:"#L.importedTimes", type:"LABEL" , value:"盤點次數"}]},
	 {items:[{name:"#F.importedTimes", type:"TEXT", bind:"importedTimes", mode:"READONLY", size:10, maxLen:10}]}]}
	], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
    
    var statusTmp = vat.item.getValueByName("#F.status");
    var brandCode = vat.item.getValueByName("#F.brandCode");
    var isImportedFileTmp = vat.item.getValueByName("#F.isImportedFile");
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;	
	var T2Mode = false;
	if("T2" == brandCode)
		T2Mode = true;
	// set column
	
	var canBeMod = document.forms[0]["#canBeMod"].value;
	
	
    vat.item.make(vnB_Detail, "indexNo", {type:"IDX" , desc:"序號"});
	vat.item.make(vnB_Detail, "itemCode", {type:"TEXT", size:20, maxLen:20, desc:"品號", mode:"READONLY"});
	vat.item.make(vnB_Detail, "itemName", {type:"TEXT", size:30, maxLen:20, desc:"品名", mode:"READONLY"});
	vat.item.make(vnB_Detail, "eanCode", {type:"TEXT", size:20, maxLen:20, desc:"國際碼", mode:"READONLY"});
	vat.item.make(vnB_Detail, "countsQty", {type:"TEXT", size:10, maxLen:12, desc:"盤點數量", mode:""});
	vat.item.make(vnB_Detail, "countsQtyFinal", {type:"TEXT", size:10, maxLen:12, desc:"最終盤點數量", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "description", {type:"TEXT", size:60, maxLen:60, desc:"說明", mode:"HIDDEN"});	
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除", mode:"Y"==canBeMod?"":"HIDDEN"});
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnB_Detail, {	pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,														
							    appendBeforeService : "appendBeforeMethod()",
							    appendAfterService  : "appendAfterMethod()",
								loadBeforeAjxService: "loadBeforeAjxService()",
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function changeCountsType(){
    var countsType = vat.item.getValueByName("#F.countsType");
    var countsDate = vat.item.getValueByName("#F.countsDate");
    if(countsType == 1){
        vat.item.setValueByName("#F.countsLotNo", "");
        vat.item.setValueByName("#F.inventoryDate", countsDate);
        vat.item.setAttributeByName("#F.countsLotNo", "readOnly", true);
        vat.item.setAttributeByName("#F.inventoryDate", "readOnly", true);
    }else{
        vat.item.setAttributeByName("#F.countsLotNo", "readOnly", false);
        vat.item.setAttributeByName("#F.inventoryDate", "readOnly", false);  
    }
}

function changeSuperintendent(){
    var superintendentTmp = vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, '').toUpperCase();    
    vat.item.setValueByName("#F.superintendentCode", superintendentTmp);   
    
    if(superintendentTmp !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + superintendentTmp,
           find: function changeSuperintendentRequestSuccess(oXHR){
               vat.item.setValueByName("#F.superintendentCode", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setValueByName("#F.superintendentName", "");
    }
}

function changeActualSuperintendent(){
    var actualSuperintendentTmp = vat.item.getValueByName("#F.actualSuperintendentCode").replace(/^\s+|\s+$/, '').toUpperCase();    
    vat.item.setValueByName("#F.actualSuperintendentCode", actualSuperintendentTmp);   
    if(actualSuperintendentTmp !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + actualSuperintendentTmp,
           find: function changeSuperintendentRequestSuccess(oXHR){
               vat.item.setValueByName("#F.actualSuperintendentCode", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setValueByName("#F.actualSuperintendentName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setValueByName("#F.actualSuperintendentName", "");
    }
}

function changeWarehouseCode(){
    var warehouseCode = vat.item.getValueByName("#F.warehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();    
    vat.item.setValueByName("#F.warehouseCode", warehouseCode);   
    if(warehouseCode !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=imWarehouseService"+
                    "&process_object_method_name=findByBrandCodeAndWarehouseCode"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&warehouseCode=" + warehouseCode,
           find: function changeWarehouseRequestSuccess(oXHR){
               vat.item.setValueByName("#F.warehouseCode", vat.ajax.getValue("WarehouseCode", oXHR.responseText));
               vat.item.setValueByName("#F.warehouseName", vat.ajax.getValue("WarehouseName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setValueByName("#F.warehouseName", "");
    }
}

function loadBeforeAjxService(){
	var processString = "process_object_name=imInventoryCountsService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&formStatus=" + vat.item.getValueByName("#F.status");
																					
	return processString;										
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imInventoryCountsService&process_object_method_name=updateAJAXPageLinesData" +
	                "&headId=" + vat.item.getValueByName("#F.headId") +
	                "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
	                "&status=" + vat.item.getValueByName("#F.status") +
	                "&canBeMod=" + document.forms[0]["#canBeMod"].value;
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {

}

function appendBeforeMethod(){
	//return true;
}

function appendAfterMethod(){
    // return alert("新增完畢");
}

function pageLoadSuccess(){
	// alert("載入成功");	
}

function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
			alertMessage = "是否確定背景送出?";
	}
		
	if(confirm(alertMessage)){
	    var isImportedFile = vat.item.getValueByName("#F.isImportedFile");
	    var status = vat.item.getValueByName("#F.status");
	    var processId = document.forms[0]["#processId"].value;	    
	    var formStatus = "";
	    if("SAVE" == formAction){
            formStatus = "SAVE";
        }else if("SUBMIT" == formAction || "SUBMIT_BG" == formAction){
            if(status == "COUNTING" && "Y" == isImportedFile){
                formStatus = "COUNT_FINISH";          
            }else{
                formStatus = "COUNTING"; 
            }         
        }else if("VOID" == formAction){
            formStatus = "VOID";
        }
		
			var assignmentId          = document.forms[0]["#assignmentId"      ].value;
		    var approvalResult        = vat.item.getValueByName("#F.approvalResult");
		    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
		    if(approvalResult == true){
		    	approvalResult = "true"
		    }else{
		    	approvalResult = "false"
		    }
		    	
		vat.block.pageDataSave(vnB_Detail, 
			        {funcSuccess:function(){
			            vat.bean().vatBeanOther.beforeChangeStatus = status;
			            vat.bean().vatBeanOther.formStatus = formStatus;
			            vat.bean().vatBeanOther.processId = processId;
			            vat.bean().vatBeanOther.formId    = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');
					    vat.bean().vatBeanOther.assignmentId	= assignmentId;
					    vat.bean().vatBeanOther.approvalResult	= approvalResult;
					    vat.bean().vatBeanOther.approvalComment	= approvalComment;
			            if("SUBMIT_BG" == formAction){
				      		vat.block.submit(function(){return "process_object_name=imInventoryCountsAction"+
				                "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
				    	  }else{				 
					    	vat.block.submit(function(){return "process_object_name=imInventoryCountsAction"+
			                	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
			            }              
     	            }}
	    );
	}
}

function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	refreshForm("");
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;
      	vat.bean().vatBeanOther.firstRecordNumber = 0;
	 }
}

function refreshForm(vsHeadId){   
	document.forms[0]["#formId"].value = vsHeadId;	
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;	
	vat.block.submit(
		function(){
			return "process_object_name=imInventoryCountsAction&process_object_method_name=performInitial"; 
     	},{other: true, 
     	   funcSuccess:function(){
     	       vat.item.bindAll();
     		   vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     		   doFormAccessControl();
               if(vsHeadId == ""){
                   changeCountsType();
               }
     	  }}
    );
}

function resetForm(){
    refreshForm("");
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
      window.top.close();
  }  	
}

function showMessage(){
	var width = "600";
    var height = "400";  
	window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_INVENTORY" +
		"&levelType=ERROR" +
        "&processObjectName=imInventoryCountsService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imInventoryCountsAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

function produceICFile(){
    var width = "600";
    var height = "400";  
	window.open(
		"/erp/jsp/ExportImCounts.jsp" + 
		"?exportBeanName=ICF" +
		"&headId=" + vat.item.getValueByName("#F.headId"),
		"盤點單匯出",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function openReportWindow(reportType){
    window.open(
		"/erp/jsp/InventoryCountsReport.jsp" + 
		"?reportType=" + reportType +
		"&headId=" + vat.item.getValueByName("#F.headId") +
        "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
        "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +        
        "&countsId=" + vat.item.getValueByName("#F.countsId") + 
        "&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode") +
        "&countsLotNo=" + vat.item.getValueByName("#F.countsLotNo") +
        "&countsType=" + vat.item.getValueByName("#F.countsType"),
		"IVR" + reportType,
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=1024,height=768,left=0,top=0');
}

function doFormAccessControl(){
    //=======================================================
    vat.item.setStyleByName("#B.submit", "display", "inline");
    vat.item.setStyleByName("#B.submitBG", "display", "inline");	
	vat.item.setStyleByName("#B.save", "display", "inline");
    vat.item.setStyleByName("#B.void", "display", "none");
    vat.item.setStyleByName("#B.message", "display", "inline");
    vat.item.setStyleByName("#B.produceIC", "display", "inline");
    vat.item.setStyleByName("#B.listReport", "display", "inline");
	vat.item.setStyleByName("#B.differReport", "display", "inline");
    vat.item.setStyleByName("#B.countsReport", "display", "inline");
    //=======================================================
    var formStatus = vat.item.getValueByName("#F.status");
    var orderNo = vat.item.getValueByName("#F.orderNo");
    var isImportedFile = vat.item.getValueByName("#F.isImportedFile");
    var processId = document.forms[0]["#processId"].value;
    var countsType = vat.item.getValueByName("#F.countsType");
    var canBeMod = document.forms[0]["#canBeMod"].value;
	var formId = document.forms[0]["#formId"].value

    vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true, true);
	vat.block.canGridModify( [vnB_Detail], true, true, true);

	//查詢開新視窗
	if(formId != ""){
		vat.item.setStyleByName("#B.new", "display", "none");
		vat.item.setStyleByName("#B.search", "display", "none");
	}
	    	
    if(formStatus == "SAVE"){

		vat.item.setStyleByName("#B.produceIC", "display", "none");
		vat.item.setStyleByName("#B.listReport", "display", "none");
		vat.item.setStyleByName("#B.differReport", "display", "none");
		vat.item.setStyleByName("#B.countsReport", "display", "none");

        //剛起單

        //工作清單回來
		if(processId != ""){
			vat.item.setStyleByName("#B.void", "display", "inline");
		}

        //查詢回來
        if(orderNo.indexOf("TMP") == -1 && processId == ""){
        	vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
        	vat.block.canGridModify( [vnB_Detail], false, false, false);
			vat.item.setStyleByName("#B.save", "display", "none");
   			vat.item.setStyleByName("#B.submit", "display", "none");
   			vat.item.setStyleByName("#B.submitBG", "display", "none");
   			vat.item.setStyleByName("#B.message", "display", "none");
		}

	}else if( formStatus == "COUNTING"){
		vat.item.setStyleByName("#B.save", "display", "none");

		//工作清單回來
		if(processId != ""){
			vat.item.setStyleByName("#B.void", "display", "inline");
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			vat.item.setAttributeByName("#F.actualSuperintendentCode", "readOnly", false);
			vat.block.canGridModify( [vnB_Detail], true, true, true);
			if(isImportedFile == "Y"){
        		vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			}

		//查詢回來
	    }else{
	    	vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
	    	vat.item.setStyleByName("#B.submit", "display", "none");
            vat.item.setStyleByName("#B.submitBG", "display", "none");
			vat.item.setStyleByName("#B.void", "display", "none");
            vat.item.setStyleByName("#B.message", "display", "none");
            vat.item.setStyleByName("#B.listReport", "display", "none");
	    	vat.item.setStyleByName("#B.differReport", "display", "none");
	    	vat.item.setStyleByName("#B.countsReport", "display", "none");
	    	
	    }

        if(countsType != "1"){
        	vat.item.setStyleByName("#B.produceIC", "display", "none");
        }

	}else{

		//查詢回來
		vat.item.setAttributeByName("vatBlock_Head",         "readOnly", true, true, true);
		vat.block.canGridModify( [vnB_Detail], false, false, false);
		vat.item.setStyleByName("#B.submit", "display", "none");
		vat.item.setStyleByName("#B.submitBG", "display", "none");	
		vat.item.setStyleByName("#B.save", "display", "none");
		vat.item.setStyleByName("#B.void", "display", "none");
		vat.item.setStyleByName("#B.message", "display", "none");
		vat.item.setStyleByName("#B.produceIC", "display", "none");

		//商控修改
		if("Y" == canBeMod){
	    	vat.block.canGridModify( [vnB_Detail], true, true, true);
	    	vat.item.setGridAttributeByName("itemCode", "readOnly", false);
    	}else{
    		vat.item.setGridAttributeByName("itemCode", "readOnly", true);
    	}
	}
    vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);
}

function detailSave(){
	if(confirm("是否要進行明細存檔")){
		vat.block.pageDataSave(vnB_Detail, {  
 			funcSuccess:function(){
				vat.ajax.XHRequest({
					post:"process_object_name=imInventoryCountsService&process_object_method_name=deleteLines"+
						"&headId=" + vat.item.getValueByName("#F.headId") +
						"&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value,
					find: function onChangeItemCodeSuccess(oXHR){ 
						vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);
		        	}   
				});
			}
		});
	}
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
    if(vat.bean().vatBeanOther.firstRecordNumber > 0) {
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

function doAfterPickerProcess(){
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