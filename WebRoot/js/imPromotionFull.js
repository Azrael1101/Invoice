vat.debug.enable();
var afterSavePageProcess = "";
var afterSaveShopPageProcess = "";
var afterSaveCustomerPageProcess = "";
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;
var vnB_Shop_All = 3;
var vnB_Shop_Detail = 4;
var vnB_Customer_All = 5;
var vnB_Customer_Detail = 6;
var vnB_AmountDiv   = 7;
var vnB_ApprovalDiv = 8;
var afterSavePageProcess = "";





function outlineBlock(){
 
	formInitial();
	buttonLine();
	headerInitial();
	if (typeof vat.tabm != 'undefined') {
      vat.tabm.createTab(0, "vatTabSpan", "H", "float");      
      vat.tabm.createButton(0, "xTab1","大類資料"    ,"vatItemDiv"              ,"images/tab_item_data_dark.gif"            ,"images/tab_item_data_light.gif", false);
      vat.tabm.createButton(0, "xTab5","簽核資料"    ,"vatApprovalDiv"          ,"images/tab_approval_data_dark.gif"        ,"images/tab_approval_data_light.gif", false);
  	} 
	detailInitial();
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
	           vat.item.getValueByName("#F.orderTypeCode"), 
	           vat.item.getValueByName("#F.orderNo"),
	           document.forms[0]["#loginEmployeeCode"].value );
	doFormAccessControl();
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#loginBrandCode"].value != "[binding]"){
		vat.bean().vatBeanOther ={
			loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
			loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,
			orderTypeCode      : document.forms[0]["#orderTypeCode"].value,
			formId             : document.forms[0]["#formId"].value,
			processId          : document.forms[0]["#processId"].value,
	        activityStatus     : document.forms[0]["#activityStatus"].value,
	        assignmentId       : document.forms[0]["#assignmentId"].value,
	        formStatus         : "",
	        approvalResult     : "",
			beforeChangeStatus : ""
		};
		vat.bean.init(function(){
			return "process_object_name=imPromotionFullAction&process_object_method_name=performInitial"; 
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
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_PromotionFull:search:20190422.page",
	 									 servicePassData:function(){ return doPassHeadData(); },
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
 
  }
  
  
function headerInitial(){
	var allOrderTypes = vat.bean("allOrderTypes");
	var isDiscount = [["", true, false], ["否", "是"], ["N", "Y"]];
	var allTypes = [["", true, false], ["折扣", "折讓"], ["discountRate", "discountAmount"]];
	vat.block.create(vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"滿額贈維護作業", rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderType", type:"LABEL" , value:"單別<font color='red'>*</font>"}]},	 
		 		{items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", mode:"READONLY", init:allOrderTypes}]},
		 		{items:[{name:"#L.orderNo"  , type:"LABEL" , value:"單號"}]},
		 		{items:[{name:"#F.orderNo"  , type:"TEXT"  ,  bind:"orderNo", size:20, mode:"READONLY"},
		 				{name:"#F.headId"   , type:"TEXT"  ,  bind:"headId", size:10, mode:"READONLY"}]},
		 		{items:[{name:"#L.brandCode", type:"LABEL", value:"品牌<font color='red'>*</font>"}]},
		 		{items:[{name:"#F.brandCode"  , type:"TEXT" , bind:"brandCode", size:12, maxLen:10, mode:"HIDDEN"},
		 				{name:"#F.brandName"  , type:"TEXT" , bind:"brandName", size:12, maxLen:10, mode:"READONLY"}]},
		 		{items:[{name:"#L.status"   , type:"LABEL" , value:"狀態"}]},	 		 
		 		{items:[{name:"#F.status"   , type:"TEXT"  ,  bind:"status", size:12, mode:"HIDDEN"},
		        		{name:"#F.statusName"   , type:"TEXT"  ,  bind:"statusName", size:12, mode:"READONLY"}]}
		    ]},
		    {row_style:"", cols:[
		    	{items:[{name:"#L.promotionCode", type:"LABEL", value:"活動代號<font color='red'>*</font>"}]},
		 		{items:[{name:"#F.promotionCode"  , type:"TEXT" , bind:"promotionCode", size:12, maxLen:10}]},
			 	{items:[{name:"#L.promotionName", type:"LABEL", value:"活動說明"}]},
		 		{items:[{name:"#F.promotionName" , type:"TEXT",   bind:"promotionName", size:30, maxLen:200}]},
		 		{items:[{name:"#L.createdBy", type:"LABEL", value:"建檔人員"}]},
				{items:[{name:"#F.createdBy" , type:"TEXT",   bind:"createdBy",  mode:"READONLY", size:12}]},
				{items:[{name:"#L.creationDate" , type:"LABEL", value:"建檔日期"}]},
	 	 		{items:[{name:"#F.creationDate" , type:"TEXT",   bind:"creationDate", mode:"READONLY", size:12}]}
	 	 	]},
		    {row_style:"", cols:[
		    	{items:[{name:"#L.buyAmount", type:"LABEL", value:"購滿金額"}]},
		 		{items:[{name:"#F.buyAmount" , type:"TEXT",   bind:"buyAmount" , size:12}]},
		 		{items:[{name:"#L.discount" , type:"LABEL", value:"折讓金額"}]},
	 	 		{items:[{name:"#F.discount" , type:"NUMB",   bind:"discount", size:12}]},
	 	 		{items:[{name:"#L.enable", type:"LABEL",  value:"是否參與VIP" }]},
				{items:[{name:"#F.enable", type:"SELECT", bind:"enable" , init:isDiscount}]},
				{items:[{name:"#L.inCharge", type:"LABEL", value:"負責人員"}]},
		 		{items:[{name:"#F.inCharge", type:"TEXT" ,  bind:"inCharge", size:10, maxLen:10, onchange:"changeSuperintendent()"},
		 				{name:"#F.inChargeName", type:"TEXT", bind:"inChargeName", size:10, maxLen:10, mode:"READONLY"}]}
			]},
		    {row_style:"", cols:[
		    	{items:[{name:"#L.beginDate" , type:"LABEL", value:"開始日期"}]},
	 	 		{items:[{name:"#F.beginDate" , type:"DATE",   bind:"beginDate", mode:"", size:12}]},
	 	 		{items:[{name:"#L.endDate" , type:"LABEL", value:"結束日期"}]},
	 			{items:[{name:"#F.endDate" , type:"DATE",   bind:"endDate", mode:"", size:12}]},
	 			{items:[{name:"#L.type" , type:"LABEL", value:"折扣/折讓"}]},
	 			{items:[{name:"#F.type" , type:"SELECT",   bind:"type", init:allTypes, onchange:"changeField()"}]},
	 			{items:[{name:"#L.description" , type:"LABEL", value:"註解"}]},
	 			{items:[{name:"#F.description" , type:"TEXT",   bind:"description", mode:"", size:80, maxLen:80}]}
			]},
	 	],	 
		 
		beginService:"",
		closeService:""	
			
	});
}
 
 
 function detailInitial(){
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;
	
	// set column
	vat.item.make(vnB_Detail, "indexNo", {type:"IDX", view:"fixed" , desc:"序號"}); 	
	vat.item.make(vnB_Detail, "vipDiscountCode", {type:"TEXT", view:"fixed", size:10, maxLen:10, desc:"類別代號", mode:"READONLY"});    		
	vat.item.make(vnB_Detail, "discountRate", {type:"NUMB", view:"", size: 10, maxLen: 10, desc:"折扣"});
	vat.item.make(vnB_Detail, "isJoin", {type:"CHECKBOX", view:"", size: 10, maxLen: 10, desc:"是否參與"});	
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});  
	//vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	//vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", view:"fixed", desc:"刪除"});
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnB_Detail, {
	                            id: "vatItemDiv",	
	                            pageSize: 10,
	                            //canGridDelete:varCanGridDelete,
								//canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,														
							    //beginService: "",
								//closeService: "",
							    appendBeforeService : "appendBeforeMethod()",
							    appendAfterService  : "appendAfterMethod()",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "pageLoadSuccess()", 
								loadFailureAfter    : "",
								//eventService        : "",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()",
								saveFailureAfter    : ""
														});
}

function loadBeforeAjxService(){
    var processString = "process_object_name=imPromotionFullService&process_object_method_name=getAJAXPageData" + 
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&formStatus=" + vat.item.getValueByName("#F.status") ;

																					
	return processString;											
    
}

function pageLoadSuccess(){
	// alert("載入成功");	
}

function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imPromotionFullService&process_object_method_name=updateAJAXPageLinesData" + 
					"&headId=" + vat.item.getValueByName("#F.headId")  +
					"&status=" + vat.item.getValueByName("#F.status")  ;
	return processString;
}

function saveSuccessAfter() {
	if( "SAVE" == afterSavePageProcess ) {	
		execSubmitAction("SAVE");
	}else if( "SUBMIT" == afterSavePageProcess ){
		execSubmitAction("SUBMIT");
	}else if( "SUBMIT_BG" == afterSavePageProcess ){
		execSubmitAction("SUBMIT_BG");
	}else if( "VOID" == afterSavePageProcess ){
		execSubmitAction("VOID");
	}
	afterSavePageProcess = "";	
}

function execSubmitAction(actionId){
	var formId    = vat.bean().vatBeanOther.formId;
	var processId = vat.bean().vatBeanOther.processId;
	var approvalResult  = vat.item.getValueByName("#F.approvalResult");
	var formStatus = status;
	var status = vat.item.getValueByName("#F.status");
	if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
	if("SAVE" == actionId){
		formStatus = "SAVE";
	}else if("SUBMIT" == actionId){
		formStatus = changeFormStatus(formId, processId, status, approvalResult);
	}else if("SUBMIT_BG" == actionId){
		formStatus = "SIGNING";
	}else if("VOID" == actionId){
		formStatus = "VOID";
	}
	vat.bean().vatBeanOther.formStatus = formStatus;
	vat.bean().vatBeanOther.beforeChangeStatus = status;
	vat.bean().vatBeanOther.approvalResult = approvalResult;
	vat.bean().vatBeanOther.approvalComment = vat.item.getValueByName("#F.approvalComment");
	
	if ("SUBMIT_BG" == actionId){
		vat.block.submit(function(){return "process_object_name=imPromotionFullAction"+
                "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
	}else{
		vat.block.submit(function(){return "process_object_name=imPromotionFullAction"+
            	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
	}
}


  
  
  
  /* 離開 */
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}

function doSubmit(formAction){
	var status = vat.item.getValueByName("#F.status");
	
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("SUBMIT_BG" == formAction){
	 	alertMessage = "是否確定背景送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("EXPORT" == formAction){
	 	alertMessage = "是否確定匯出作業?";
	}else if ("IMPORT" == formAction){
	 	alertMessage = "是否確定匯入作業?";
	}else if ("EXTEND" == formAction){
	 	alertMessage = "是否執行取報單程序?";
	}else if ("EXPENSE" == formAction){
	 	alertMessage = "是否確定重新分攤費用?";
	}

	if(confirm(alertMessage)){
	    if("SAVE" == formAction){
	    	afterSavePageProcess = "SAVE";
        }else if("SUBMIT" == formAction){
        	afterSavePageProcess = "SUBMIT";
        }else if("SUBMIT_BG" == formAction){
        	afterSavePageProcess = "SUBMIT_BG";
        }else if("VOID" == formAction){
        	afterSavePageProcess = "VOID";
        }else if("EXPORT" == formAction){
        	afterSavePageProcess = "EXPORT";
        }else if("IMPORT" == formAction){
        	afterSavePageProcess = "IMPORT";
        }else if ("EXTEND" == formAction){
        	afterSavePageProcess = "EXTEND"; 
        }else if ("EXPENSE" == formAction){
        	afterSavePageProcess = "EXPENSE"; 
        }
        vat.block.pageSearch(vnB_Detail);	// save & refresh ImReceiveItem Page / after save success call saveSuccessAfter()
	}
	/*var formId    = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var status    = vat.item.getValueByName("#F.status");
        
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){		
	    alertMessage = "是否確定送出?";	    					
	}else if ("SUBMIT_BG" == formAction){
	 	alertMessage = "是否確定背景送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("EXPORT" == formAction){
	 	alertMessage = "是否確定匯出作業?";
	}else if ("IMPORT" == formAction){
	 	alertMessage = "是否確定匯入作業?";
	}else if ("EXTEND" == formAction){
	 	alertMessage = "是否執行取報單程序?";
	}else if ("CLOSE" == formAction){
	 	alertMessage = "是否確定採購單結案?";
	}
	
	var formStatus = status;
    if("SAVE" == formAction){
        formStatus = "SAVE";
    }else if("SUBMIT" == formAction){
        formStatus = changeFormStatus(formId, processId, status, approvalResult);
    }else if("VOID" == formAction){
        formStatus = "VOID";
    }	
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.formAction = formAction;
		vat.bean().vatBeanOther.formStatus = formStatus;
		vat.bean().vatBeanOther.beforeChangeStatus = status;
		vat.block.submit(function(){
				return "process_object_name=imPromotionFullAction"+
				"&process_object_method_name=performTransaction";
			},{
				bind:true, link:true, other:true 				
			}
		);
	}*/
}


function changeSuperintendent(){

    var vInCharge = vat.item.getValueByName("#F.inCharge").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.inCharge", vInCharge);
    if(vInCharge !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vInCharge,
           find: function changeSuperintendentRequestSuccess(oXHR){              
               //vat.item.setValueByName("#F.inCharge", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setValueByName("#F.inChargeName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
         vat.item.setValueByName("#F.inChargeName", "");
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

/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
    if (confirm("是否確認暫存?")) {		
	    //save line
	    afterSavePageProcess = "saveHandler";
		vat.block.pageSearch(vnB_Detail);
		doSubmit("SAVE");				
	}
}

/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	
	//save line
	afterSavePageProcess = "submitHandler";	
	vat.block.pageSearch(vnB_Detail);
	doSubmit("SUBMIT");

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

// 刷新頁面
function refreshForm(code){
	document.forms[0].reset();
	//document.forms[0]["#formId"].value = code;
	//vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.formId = code;
	vat.block.submit(
		function(){
			return "process_object_name=imPromotionFullAction&process_object_method_name=performInitial"; 
		},
		{
			other : true,
			funcSuccess : function(){
				vat.item.bindAll(); 
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
				refreshWfParameter(vat.item.getValueByName("#F.brandCode"), vat.item.getValueByName("#F.orderTypeCode"),  vat.item.getValueByName("#F.orderNo"));	
				getEmployeeName(document.forms[0]["#loginEmployeeCode"].value);
				doFormAccessControl();
			}
		}
	);	
}

function doPassHeadData(){
  
  var suffix = "";
  var brandCode = vat.item.getValueByName("#F.brandCode");
  var employeeCode = document.forms[0]["#loginEmployeeCode"].value;    
  var orderTypeCode  = vat.item.getValueByName("#F.OrderTypeCode").replace(/^\s+|\s+$/, '').toUpperCase();
  suffix += "&loginBrandCode="+escape(brandCode)+
            "&orderTypeCode="+escape(orderTypeCode)+
            "&loginEmployeeCode="+escape(employeeCode);

  return suffix;
}

function doFormAccessControl(){
	var vType = vat.item.getValueByName("#F.type");
	var vStatus = vat.item.getValueByName("#F.status");
	var vsProcessId     = vat.bean().vatBeanOther.processId;
  	var vsOrderNoPrefix = vat.item.getValueByName("#F.orderNo").substring(0, 3);
	var formId = vat.bean().vatBeanOther.formId;
	
	if((vsProcessId == null || vsProcessId == "") && vsOrderNoPrefix != "TMP"){
		vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
		vat.item.setAttributeByName("#F.promotionName", "readOnly", true);
		vat.item.setAttributeByName("#F.buyAmount", "readOnly", true);
		vat.item.setAttributeByName("#F.discount", "readOnly", true);
		vat.item.setAttributeByName("#F.enable", "readOnly", true);
		vat.item.setAttributeByName("#F.beginDate", "readOnly", true);
		vat.item.setAttributeByName("#F.endDate", "readOnly", true);
		vat.item.setAttributeByName("#F.type", "readOnly", true);
		vat.item.setAttributeByName("#F.description", "readOnly", true);
		vat.item.setGridAttributeByName("isJoin", "readOnly", true);
		vat.item.setGridAttributeByName("discountRate", "readOnly", true);
		vat.item.setStyleByName("#B.submit", "display", "none");
		vat.item.setStyleByName("#B.save", "display", "none");
		vat.item.setStyleByName("#B.void", "display", "none");
	}else{
		if(vStatus == "SAVE" || vStatus == "REJECT"){
			if(vType == "discountRate"){
				vat.item.setAttributeByName("#F.discount", "readOnly", true);
				//vat.item.setAttributeByName("#F.enable", "readOnly", true);
				vat.item.setGridAttributeByName("isJoin", "readOnly", true);
				vat.item.setGridAttributeByName("discountRate", "readOnly", false);
			}else if(vType == "discountAmount"){
				vat.item.setAttributeByName("#F.discount", "readOnly", false);
				//vat.item.setAttributeByName("#F.enable", "readOnly", false);
				vat.item.setGridAttributeByName("isJoin", "readOnly", false);
				vat.item.setGridAttributeByName("discountRate", "readOnly", true);
			}
			/*
			if(formId != null && formId != ""){
				vat.item.setAttributeByName("#F.type", "readOnly", true);
			}*/
		}else if(vStatus == "SIGNING" || vStatus == "FINISH" || vStatus == "VOID"){
			vat.item.setAttributeByName("#F.promotionCode", "readOnly", true);
			vat.item.setAttributeByName("#F.promotionName", "readOnly", true);
			vat.item.setAttributeByName("#F.buyAmount", "readOnly", true);
			vat.item.setAttributeByName("#F.discount", "readOnly", true);
			vat.item.setAttributeByName("#F.enable", "readOnly", true);
			vat.item.setAttributeByName("#F.beginDate", "readOnly", true);
			vat.item.setAttributeByName("#F.endDate", "readOnly", true);
			vat.item.setAttributeByName("#F.type", "readOnly", true);
			vat.item.setAttributeByName("#F.description", "readOnly", true);
			vat.item.setGridAttributeByName("isJoin", "readOnly", true);
			vat.item.setGridAttributeByName("discountRate", "readOnly", true);
		}
	}
}

function changeField(){
	var vHeadId = vat.item.getValueByName("#F.headId");
	var vType = vat.item.getValueByName("#F.type");
	vat.ajax.XHRequest({
		post:"process_object_name=imPromotionFullService&process_object_method_name=updateField"+
				"&headId=" + vHeadId + 
				"&type=" + vType ,
		find: function change(oXHR){
			var enable = vat.ajax.getValue("enable" , oXHR.responseText);
			var discount = vat.ajax.getValue("discount" , oXHR.responseText);
			vat.item.setValueByName("#F.enable", enable);
			vat.item.setValueByName("#F.discount", discount);
			vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
		}
	});
	
	doFormAccessControl();
}

function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == ""){
        formStatus = "SIGNING";
    }else if(processId != null && processId != "" && processId != 0){
        if(status == "SAVE" || status == "REJECT"){
            formStatus = "SIGNING";
        }else if(status == "SIGNING"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }  
    }
    return formStatus;
}
  

  