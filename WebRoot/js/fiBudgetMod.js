/*** 
 *	檔案: fiBudgetApply.js
 *	說明：採購預算申請
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;
var vSelect1 = "";

function outlineBlock(){
	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","金額統計" ,"vatAmountDiv"	,"images/tab_total_amount_dark.gif" ,"images/tab_total_amount_light.gif", false);
		vat.tabm.createButton(0 ,"xTab5","簽核資料" ,"vatApprovalDiv","images/tab_approval_data_dark.gif","images/tab_approval_data_light.gif",document.forms[0]["#processId"].value==""?"none":"inline");
	}
	lineInitial();
	  kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
             vat.item.getValueByName("#F.orderTypeCode"), 
             vat.item.getValueByName("#F.orderNo"),
             document.forms[0]["#loginEmployeeCode"].value );
             
	doFormAccessControl();
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
				return "process_object_name=fiBudgetModHeadService&process_object_method_name=executeInitial"; 
	    	},{								
	    		other: true
    	});
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
	 	{items:[
	 	
	 	        {name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Fi_Budget_Mod:search:20091106.page",    // ?orderTypeCode="+vat.bean().vatBeanOther.orderTypeCode
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SIGNING")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"saveSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.lock"    	   , type:"IMG"    ,value:"凍結",   src:"./images/button_lock_data.gif", eClick:'loadBudget()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.unlock"      , type:"IMG"    ,value:"解除查詢",   src:"./images/button_unlock_data.gif", eClick:'disableBudget()'},
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
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

// 採購預算申請主檔
function headerInitial(){
	var itemTypes = vat.bean("itemTypes");
	var budgetYearLists = vat.bean("budgetYearLists");
	var months = vat.bean("months");
	var branch =  vat.bean("branch");
	if(branch == "2"){
		itemTypes[0][2] = true;
	}
		
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"採購預算申請功能維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"預算單號" }]},
				{items:[{name:"#F.orderNo", 			type:"TEXT", 	bind:"orderNo", size:20, mode:"READONLY"}]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", size:6, mode:"HIDDEN"},
	 					{name:"#F.brandName", 			type:"TEXT",  	bind:"brandName", size:6, mode:"READONLY"},
	 			   		{name:"#F.headId", 				type:"TEXT",  	bind:"headId", back:false, mode:"HIDDEN"}]},
				{items:[{name:"#L.totalBudget", 		type:"LABEL", 	value:"總預算"}]},	 
	 			{items:[{name:"#F.totalBudget", 		type:"TEXT",  	bind:"totalBudget", size:15, mode:"READONLY"}]},
	 			{items:[{name:"#L.status", 				type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"TEXT", 	bind:"status", size:15, mode:"HIDDEN"},
						{name:"#F.statusName", 			type:"TEXT",  bind:"statusName", back:false, mode:"READONLY"}]} 
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.budgetYear", 			type:"LABEL", 	value:"預算年度"}]},	 
	 			{items:[{name:"#F.budgetYear", 			type:"SELECT",    bind:"budgetYear", init:budgetYearLists}]},
	 			{items:[{name:"#L.budgetMonth", 		type:"LABEL", 	value:"預算月"}]},
				{items:[{name:"#F.budgetMonth", 		type:"SELECT", 	bind:"budgetMonth", init:months, mode:branch == "2"?"":"READONLY"}]},
				{items:[{name:"#L.totalForecastAmount", type:"LABEL", 	value:"總預測"}]},
				{items:[{name:"#F.totalForecastAmount", type:"TEXT", 	bind:"totalForecastAmount", mode:"READONLY"}]},
				{items:[{name:"#L.lastUpdateBy",		type:"LABEL", 	value:"最近修改人員" }]},
	 			{items:[{name:"#F.lastUpdateBy",		type:"TEXT", 	bind:"lastUpdatedBy", mode:"HIDDEN"},
						{name:"#F.lastUpdatedByName",	type:"TEXT", 	bind:"lastUpdatedByName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
			    {items:[{name:"#L.orderTypeCode", 		type:"LABEL", 	value:"單別" }]},
				{items:[{name:"#F.orderTypeCode", 		type:"TEXT", 	bind:"orderTypeCode", size:15, mode:"READONLY"}]},
				{items:[{name:"#L.itemType", 			type:"LABEL", 	value:"業種"}]},
				{items:[{name:"#F.itemType", 			type:"SELECT", 	bind:"itemType", init:itemTypes, mode:branch == "2"?"":"READONLY", eChange:"changeItemType()"}]},
				{items:[{name:"#L.description", 		type:"LABEL", 	value:"說明"}]},	 
	 			{items:[{name:"#F.description", 		type:"TEXT",  	bind:"description", size:30}]},
	 			{items:[{name:"#L.lastUpdateDate", 		type:"LABEL", 	value:"最近修改日期"}]},	 
	 			{items:[{name:"#F.lastUpdateDate", 		type:"TEXT",  	bind:"lastUpdateDate", size:15, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
			    
			]}
		], 	
		beginService:"",
		closeService:""			
	});
}

function lineInitial(){
	var category01s = vat.bean("category01s");
	var category02s = vat.bean("category02s");
	var modifyTypes = vat.bean("modifyTypes");
	var Vstatus = vat.item.getValueByName("#F.status");
	var vbCanGridDelete = true; 
	var vbCanGridAppend = true; 
	var vbCanGridModify = true;
	if("SIGNING" == Vstatus || "FINISH" == Vstatus){
		vbCanGridDelete = false; 
		vbCanGridAppend = false; 
		vbCanGridModify = false;
	}
	
	var showT2 = "HIDDEN";
	var showT2Name = "HIDDEN";
	var showT1 = "";
	if("T2" == vat.item.getValueByName("#F.brandCode")){
		showT2 = "";
		showT2Name = "readOnly";
		showT1 = "shift";
	}
	
	vat.item.make(vnB_Detail, "indexNo"				, {type:"IDX" 	, view: "fixed" , desc:"序號" });
	vat.item.make(vnB_Detail, "modifyType"			, {type:"SELECT", view: "fixed" , desc:"類型",init:modifyTypes,mode:"hidden"});
	vat.item.make(vnB_Detail, "itemBrandCode"		, {type:"TEXT" 	, view: "fixed" , size:10, maxLen:20, desc:"商品品牌", eChange:"changeItemData()", mode:showT2});
	vat.item.make(vnB_Detail, "itemBrandName"		, {type:"TEXT" 	, view: "" , size:10, maxLen:20, desc:"品牌名稱", mode:showT2Name});
	vat.item.make(vnB_Detail, "categoryTypeCode1"	, {type:"SELECT", view: "" , desc:"大類", init:category01s, mode:showT2Name, eChange:"changeCategoryType()"});
	vat.item.make(vnB_Detail, "categoryTypeCode2"	, {type:"SELECT", view: "" , desc:"中類", init:category02s, mode:showT2Name});
	vat.item.make(vnB_Detail, "budgetAmount"		, {type:"NUMM" 	, view: "" , size:12, desc:"預算金額",mask:"C",mode:"readOnly"});//新增千分位符號-Jerome
	vat.item.make(vnB_Detail, "adjustAmount"		, {type:"NUMM" 	, view: "" , size:12, desc:"申請/調整金額", mask:"C",mode:""});
	vat.item.make(vnB_Detail, "forecastAmount"		, {type:"NUMB" 	, view: showT1 , size:8, desc:"業績預測金額", mask:"C",mode:showT2Name});
	vat.item.make(vnB_Detail, "forecastAdjustAmount", {type:"NUMB" 	, view: showT1 , size:8, desc:"業績預測調整金額", mask:"C",mode:showT2});
	vat.item.make(vnB_Detail, "reserve1"			, {type:"TEXT" 	, view: showT1 , size:20, desc:"備註",maxLen:20,mode:"hidden" });
	vat.item.make(vnB_Detail, "lineId"				, {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord"		, {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord"		, {type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Detail, "message"				, {type:"MSG", desc:"訊息"});
	
	vat.block.pageLayout(vnB_Detail, {
										id: "vatAmountDiv", 
										pageSize: 10,											
				            			canGridDelete : vbCanGridDelete,
										canGridAppend : vbCanGridAppend,
										canGridModify : vbCanGridModify,						
										appendBeforeService : "",//"appendBeforeService()",
										appendAfterService  : "",//"appendAfterService()",
										loadBeforeAjxService: "loadBeforeAjxService()",
										loadSuccessAfter    : "loadSuccessAfter()",						
										eventService        : "eventService()",   
										saveBeforeAjxService: "saveBeforeAjxService()",
										saveSuccessAfter    : "saveSuccessAfter()"
										});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
	
}


function changeItemData() {  
  var vLineId                 = vat.item.getGridLine();
  var vItemCode               = vat.item.getGridValueByName("itemBrandCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  vat.item.setGridValueByName("itemBrandCode",vLineId, vItemCode);
	vat.ajax.XHRequest(
	    {
	        post:"process_object_name=fiBudgetModHeadService"+
	                    "&process_object_method_name=executeFindItemBrandName"+
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&itemBrandCode=" + vItemCode,
	        find: function changeItemDataSuccess(oXHR){
	        	vat.item.setGridValueByName("itemBrandName" ,vLineId, vat.ajax.getValue("ItemBrandName", oXHR.responseText));
	       	}   
	   });
}       


// 第一次載入 重新整理
function loadBeforeAjxService(){
   // alert("loadBeforeAjxService");	
		var processString = "process_object_name=fiBudgetModHeadService&process_object_method_name=getAJAXLinePageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") ;
		return processString;	
}


// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(){
  //alert("saveBeforeAjxService");
  		var processString = "process_object_name=fiBudgetModHeadService&process_object_method_name=updateOrSaveAJAXPageLinesData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") ;
		return processString;	
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
  //alert("loadSuccessAfter");	
	var branch =  vat.bean("branch");
	if(branch == "2"){
		changeItemType();
	}
	//alert("loadSuccessAfter");	
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(){
 		vat.ajax.XHRequest(
	    {
	        post:"process_object_name=fiBudgetModHeadService"+
	                    "&process_object_method_name=executeBudgetTotal"+
	                    "&headId=" + vat.item.getValueByName("#F.headId"),
	        find: function saveSuccessAfter(oXHR){
	        vat.item.setValueByName("#F.totalBudget" ,vat.ajax.getValue("TotalBudget", oXHR.responseText));
	        vat.item.setValueByName("#F.totalForecastAmount" ,vat.ajax.getValue("TotalForecastAmount", oXHR.responseText));
	       }   
	   });
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
function doPageDataSave(){

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


function doSubmit(formAction){
	var alertMessage ="是否確定?";
		if("SIGNING" == formAction){
			alertMessage = "是否確定送出?";
		}else if ("SAVE" == formAction){
		 	alertMessage = "是否確定暫存?";
		}else if("SUBMIT_BG" == formAction){
			alertMessage = "是否確定背景送出?";
		}else if ("VOID" == formAction){
		 	alertMessage = "是否確定作廢?";
		}
		
		if(confirm(alertMessage)){
		    var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');
		    var processId             = document.forms[0]["#processId"      ].value;
		    var beforeChangeStatus	  = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		    var employeeCode          = document.forms[0]["#loginEmployeeCode" ].value;
		    var assignmentId          = document.forms[0]["#assignmentId"      ].value;
		    var loginBrandCode        = document.forms[0]["#loginBrandCode"      ].value;
		    var approvalResult        = vat.item.getValueByName("#F.approvalResult");
		    var budgetYear            = vat.item.getValueByName("#F.budgetYear").replace(/^\s+|\s+$/, '');
		    var budgetMonth           = vat.item.getValueByName("#F.budgetMonth").replace(/^\s+|\s+$/, '');
		    var itemType         	  = vat.item.getValueByName("#F.itemType").replace(/^\s+|\s+$/, '');
		    if(approvalResult == true){
		    	approvalResult = "true";
		    }else{
		    	approvalResult = "false";
		    	formAction = "REJECT";
		    }
		    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
			var organizationCode 	  = "TM";
			vat.block.pageDataSave(vnB_Detail, {  
	        				funcSuccess:function(){
								vat.ajax.XHRequest({
					        		post:"process_object_name=fiBudgetModHeadService"+
					                    "&process_object_method_name=executeBudgetTotal"+
					                    "&headId=" + vat.item.getValueByName("#F.headId"),
					        		find: function dosubmitSuccess(oXHR){
					        			vat.item.setValueByName("#F.totalBudget" ,vat.ajax.getValue("TotalBudget", oXHR.responseText));
					        			vat.item.setValueByName("#F.totalForecastAmount" ,vat.ajax.getValue("TotalForecastAmount", oXHR.responseText));
					        			vat.bean().vatBeanOther ={}
										vat.bean().vatBeanOther.formId          	= formId;
										vat.bean().vatBeanOther.loginBrandCode    	= loginBrandCode;
										vat.bean().vatBeanOther.beforeChangeStatus	= beforeChangeStatus;
										vat.bean().vatBeanOther.assignmentId      	= assignmentId;
										vat.bean().vatBeanOther.processId       	= processId;
										vat.bean().vatBeanOther.formAction      	= formAction;
										vat.bean().vatBeanOther.approvalResult  	= approvalResult;
										vat.bean().vatBeanOther.approvalComment 	= approvalComment;
										vat.bean().vatBeanOther.employeeCode		= employeeCode;
										vat.bean().vatBeanOther.organizationCode	= organizationCode;
										if("SUBMIT_BG" == formAction){
											vat.block.submit(function(){return "process_object_name=fiBudgetModAction"+
											"&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
										}else{
											vat.block.submit(function(){return "process_object_name=fiBudgetModAction"+
										    "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
										}
					       			}   
					   			});	        				
			          		}
			});
		}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=FI_BUDGET_MOD" +
		"&levelType=ERROR" +
        "&processObjectName=fiBudgetModHeadService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
	vat.bean().vatBeanOther.formAction = "SIGNING";
    vat.block.submit(function(){return "process_object_name=fiBudgetModAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

function createRefreshForm(){
	refreshForm("");
}

function loadBudget(){
	var budgetYear = vat.item.getValueByName("#F.budgetYear");
	var budgetMonth = vat.item.getValueByName("#F.budgetMonth");
	var itemType = vat.item.getValueByName("#F.itemType");
	if(budgetYear == ""){
		alert('預算年度不可為空');
	}else if(budgetMonth == ""){
		alert('預算月份不可為空');
	}else if(itemType == ""){
		alert('業種不可為空');
	}else{
		vat.ajax.XHRequest(
		    {
		        asyn:false,
		        post:"process_object_name=fiBudgetModHeadService"+
		                    "&process_object_method_name=executeFindBudget"+
		                    "&headId=" + vat.item.getValueByName("#F.headId") + 
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
		                    "&budgetYear=" + budgetYear +
		                    "&budgetMonth=" + budgetMonth +
		                    "&itemType=" + itemType,
		        find: function loadBudgetSuccess(oXHR){
		        vat.item.setValueByName("#F.totalBudget" ,vat.ajax.getValue("TotalBudget", oXHR.responseText));
		        vat.item.setValueByName("#F.totalForecastAmount" ,vat.ajax.getValue("TotalForecastAmount", oXHR.responseText));
		        doPageRefresh();
		        //saveSuccessAfter();
		       }   
		   })
		    vat.item.setStyleByName("#B.submitBG", "display", "inline");
	  		vat.item.setStyleByName("#B.submit"	 , "display", "inline");
	  		vat.item.setStyleByName("#B.message" , "display", "inline");
	  		vat.item.setStyleByName("#B.unlock"	 , "display", "inline");
	  		vat.item.setStyleByName("#B.lock"	 , "display", "none");
	  		vat.item.setAttributeByName("#F.budgetYear","readOnly",true);
	  		vat.item.setAttributeByName("#F.budgetMonth","readOnly",true);
	  		vat.item.setAttributeByName("#F.itemType","readOnly",true);
	  		vat.item.setGridAttributeByName("itemBrandCode", "readOnly", false);
	  		vat.item.setGridAttributeByName("reserve1", "readOnly", false);
	  		vat.item.setGridAttributeByName("adjustAmount", "readOnly", false);
	  		vat.item.setGridAttributeByName("forecastAdjustAmount", "readOnly", false);
	  		var branch =  vat.bean("branch");
	  		if(branch == "2"){
	  			vat.item.setGridAttributeByName("categoryTypeCode1", "readOnly", false);
				vat.item.setGridAttributeByName("categoryTypeCode2", "readOnly", true);
			}
	}
}

function disableBudget(){
	vat.ajax.XHRequest(
	    {
	        asyn:false,
	        post:"process_object_name=fiBudgetModHeadService"+
	                    "&process_object_method_name=executeDisableBudget"+
	                    "&headId=" + vat.item.getValueByName("#F.headId"),
	        find: function disableBudgetSuccess(oXHR){
	        	vat.item.setValueByName("#F.totalBudget" , 0);
		        vat.item.setValueByName("#F.totalForecastAmount" , 0);
	        doPageRefresh();
	       }   
	   })
	    vat.item.setStyleByName("#B.submitBG", "display", "none");
  		vat.item.setStyleByName("#B.submit"	 , "display", "none");
  		vat.item.setStyleByName("#B.message" , "display", "none");
  		vat.item.setStyleByName("#B.unlock"	 , "display", "none");
  		vat.item.setStyleByName("#B.lock"	 , "display", "inline");
  		vat.item.setAttributeByName("#F.budgetYear","readOnly",false);
  		var branch =  vat.bean("branch");
  		if(branch == "2"){
  			vat.item.setAttributeByName("#F.budgetMonth","readOnly",false);
  			vat.item.setAttributeByName("#F.itemType","readOnly",false);
  			vat.item.setGridAttributeByName("categoryTypeCode1", "readOnly", true);
			vat.item.setGridAttributeByName("categoryTypeCode2", "readOnly", true);
  		}
  		vat.item.setGridAttributeByName("itemBrandCode", "readOnly", true);
  		vat.item.setGridAttributeByName("reserve1", "readOnly", true);
  		vat.item.setGridAttributeByName("adjustAmount", "readOnly", true);
  		vat.item.setGridAttributeByName("forecastAdjustAmount", "readOnly", true);
}

function doPageRefresh(){
    vat.block.pageRefresh(vnB_Detail);
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	//alert('doAfterPickerProcess');
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
	  	vat.item.setStyleByName("#B.save"		, "display", "none");
  		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
  		vat.item.setStyleByName("#B.submit"	, "display", "none");
	
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
	var firstRecordNumber =vat.bean().vatBeanOther.firstRecordNumber;
	var lastRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	var currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber;
	vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,	
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          firstRecordNumber		: firstRecordNumber,
          currentRecordNumber 	: currentRecordNumber,
	      lastRecordNumber    	: lastRecordNumber
        };    
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.bean().vatBeanOther.loginEmployeeCode = document.forms[0]["#loginEmployeeCode"].value;
	vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	
	vat.block.submit(
		function(){
			return "process_object_name=fiBudgetModHeadService&process_object_method_name=executeInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
				//disableBudget();
				doPageRefresh();
				doFormAccessControl();
     	}});
}

function doFormAccessControl(){
	var Vstatus = vat.item.getValueByName("#F.status");
	var orderNo = vat.item.getValueByName("#F.orderNo");
	var VprocessId = document.forms[0]["#processId"].value;
	vat.item.setStyleByName("#B.new"	, "display", "inline");
	vat.item.setStyleByName("#B.search"	, "display", "inline");
	vat.item.setStyleByName("#B.lock"	, "display", "inline");
	vat.item.setStyleByName("#B.unlock"	, "display", "none");
	vat.item.setStyleByName("#B.void"	, "display", "none");
	vat.item.setStyleByName("#B.message", "display", "inline");
	vat.item.setStyleByName("#B.save"	, "display", "inline");
	vat.item.setStyleByName("#B.submit"	, "display", "none");
	vat.item.setStyleByName("#B.submitBG"	, "display", "none");
	vat.item.setGridAttributeByName("itemBrandCode", "readOnly", true);
	vat.item.setGridAttributeByName("reserve1", "readOnly", true);
	vat.item.setGridAttributeByName("categoryTypeCode1", "readOnly", true);
	vat.item.setGridAttributeByName("categoryTypeCode2", "readOnly", true);
	vat.item.setGridAttributeByName("adjustAmount", "readOnly", true);
	vat.item.setGridAttributeByName("forecastAdjustAmount", "readOnly", true);
	vat.item.setAttributeByName("#F.budgetYear","readOnly",false);
	var branch =  vat.bean("branch");
	if(branch == "2"){
		vat.item.setAttributeByName("#F.budgetMonth","readOnly",false);
		vat.item.setAttributeByName("#F.itemType","readOnly",false);
	}
	
	if(VprocessId != ""){
		vat.item.setStyleByName("#B.new"	, "display", "none");
		vat.item.setStyleByName("#B.search"	, "display", "none");
		if("SIGNING" == Vstatus || "FINISH" == Vstatus){
			vat.item.setStyleByName("#B.save"	, "display", "none");
			vat.item.setStyleByName("#B.lock"	, "display", "none");
			vat.item.setStyleByName("#B.message", "display", "none");
			vat.item.setStyleByName("#B.submit"	, "display", "inline");
			vat.item.setAttributeByName("#F.budgetYear","readOnly",true);
	  		vat.item.setAttributeByName("#F.budgetMonth","readOnly",true);
			vat.item.setAttributeByName("#F.itemType","readOnly",true);
			vat.item.setAttributeByName("#F.description","readOnly",true);
		}else if("SAVE" == Vstatus || "REJECT" == Vstatus){
			vat.item.setStyleByName("#B.void"	, "display", "inline");
		}
  	}else{
  		if(vat.bean().vatBeanPicker.result != null){
  			vat.item.setStyleByName("#B.save"	, "display", "none");
  			vat.item.setStyleByName("#B.lock"	, "display", "none");
			vat.item.setStyleByName("#B.unlock"	, "display", "none");
			vat.item.setStyleByName("#B.message", "display", "none");
			vat.item.setAttributeByName("#F.budgetYear","readOnly",true);
	  		vat.item.setAttributeByName("#F.budgetMonth","readOnly",true);
			vat.item.setAttributeByName("#F.itemType","readOnly",true);
			vat.item.setAttributeByName("#F.description","readOnly",true);
	  	}
  	}
  	if(VprocessId!="" || orderNo.indexOf("TMP") == -1){
		vat.tabm.displayToggle(0, "xTab5", true, false, false);
     	refreshWfParameter( vat.item.getValueByName("#F.brandCode"), 
     		   				vat.item.getValueByName("#F.orderTypeCode"),
     		   				vat.item.getValueByName("#F.orderNo" ) );
		vat.block.pageDataLoad(102, vnCurrentPage = 1); 
	}else{
		vat.tabm.displayToggle(0, "xTab5", false, false, false);
	}
}

function changeItemType(){
//alert("changeItemType");	
	if(vSelect1 == ""){
		var itemTypes = vat.bean("itemTypes");
	  	vSelect1 = new Array(itemTypes[2].length);
		for(i = 0; i<vSelect1.length; i++){
			vat.ajax.XHRequest({ 
				post:"process_object_name=fiBudgetModHeadService"+
	            		"&process_object_method_name=findAJAXItemType"+
	            		"&brandCode=" + document.forms[0]["#loginBrandCode"].value + 
	                	"&itemType=" + itemTypes[2][i],
	            asyn:false,                      
				find: function change(oXHR){
					vSelect1[i] = eval( vat.ajax.getValue("Category01s", oXHR.responseText));
	           }
	        });
		}
		var gridData = vat.block.getGridData(vnB_Detail);	  
		for(var i = 0; i<gridData[i+1].size; i++){
			var itemType = vat.item.getValueByName("#F.itemType");
			var selectIndex = vat.item.SelectGetIndex("#F.itemType");
			var categoryTypeCode1 = vSelect1[selectIndex];
			var vName = vat.item.nameMake("categoryTypeCode1", i+1);
			alert(selectIndex+"=="+categoryTypeCode1);
			var selectedValue = gridData[i+1][4];
			categoryTypeCode1[0][0] = vName;
			categoryTypeCode1[0][1] = selectedValue;
		   	categoryTypeCode1[0][2] = true;
		   	vat.item.SelectBind(categoryTypeCode1);
		   	changeCategoryTypeCode(i+1);
		}
	}
	
	
	
}

function changeCategoryType(){
	var vLineId                 = vat.item.getGridLine();
	//changeCategoryTypeCode(vLineId);
}

function changeCategoryTypeCode(vLineId){
	var vCategoryTypeCode1               = vat.item.getGridValueByName("categoryTypeCode1", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
		vat.ajax.XHRequest({ 
			post:"process_object_name=fiBudgetModHeadService"+
            		"&process_object_method_name=findAJAXCategoryType"+
            		"&brandCode=" + document.forms[0]["#loginBrandCode"].value + 
                	"&category01=" + vCategoryTypeCode1,
            asyn:false,                      
			find: function change(oXHR){
				// 重新設定 哪一列的下拉
				var categoryTypeCode2 = eval( vat.ajax.getValue("Category02s", oXHR.responseText));
					var vName = vat.item.nameMake("categoryTypeCode2", vLineId);
					var gridData = vat.block.getGridData(vnB_Detail);
					//alert("selectedValue="+gridData);
					//alert("vLineId="+vLineId);
					var selectedValue = gridData[vLineId][5];
					categoryTypeCode2[0][0] = vName;
					categoryTypeCode2[0][1] = selectedValue;
	   				categoryTypeCode2[0][2] = true;
	   				vat.item.SelectBind(categoryTypeCode2);	   				
           }
        });
}