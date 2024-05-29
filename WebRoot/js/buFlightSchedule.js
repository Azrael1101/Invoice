/***
 *	檔案: soDelivery.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Search = 5;
var vnB_Header = 1;
var vnB_master = 2;
var vnB_Detail = 3;
var vnB_Log    = 4;
var vnB_So_Head =1;

function kweBlock(){
   if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
	     flightDate         : document.forms[0]["#flightDate"        ].value,
	     flightType         : document.forms[0]["#flightType"        ].value
	 };
  }
  kweButtonLine();
  kweHeader();
  kweDetail();

 
}

function kweCreateBlock(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
	     formId             : document.forms[0]["#formId"            ].value,
	     flightType         : document.forms[0]["#flightType"        ].value
	 };
  }
  kweCreateButtonLine();
  kweCreateHeader();
  kweCreateInitial();
 
}

function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    var allProcessType = [["","",false],
                 ["手動","自動"],
                 ["MANUAL","AUTO"]];     
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 3px; border-left-width: 0px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:'openModifyPage("CREATE")'},				 
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , eClick:"doSearch()"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},                
	            {name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  ,
	 									 openMode:"open",
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,
	 									 serviceAfterPick:function(){afterImportSuccess()}},
				{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#F.reportList"  , type:"SELECT" },
	 			{name:"#B.print"       , type:"IMG"    ,value:"列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]},
	  ],
		beginService:function(){return ""}	,
		closeService:function(){return ""}
	});
		
	
	vat.item.setValueByName("#F.processMode"      ,vat.bean().vatBeanOther.processMode);
	vat.item.setValueByName("#F.processType"      ,vat.bean().vatBeanOther.loginProcessType);
}

function kweHeader(){
var allStatus = [["","",true],
                 ["已到","準時","取消","改時"],
                 ["已到","準時","取消","改時"]];
var allTerminals = [["","",true],
                 ["第一航廈","第二航廈"],
                 ["1","2"]];
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"航班資料查詢", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.flightDate"  , type:"LABEL" , value:"日期"},
	         {name:"#F.updateFlag"  , type:"TEXT"  ,  size:4, bind:"updateFlag",mode:"HIDDEN"}]},
	 {items:[{name:"#F.flightDate"  , type:"DATE"  ,  bind:"flightDate"}]},
	 {items:[{name:"#L.flightNo"    , type:"LABEL" , value:"班機代號"}]},
	 {items:[{name:"#F.flightNo"    , type:"TEXT"  ,  bind:"flightNo", mask:"CCCCCC"}]},
	 {items:[{name:"#L.flightCompany", type:"LABEL", value:"航空公司"}]},
	 {items:[{name:"#F.flightCompany", type:"TEXT" ,  bind:"flightCompany"}]},
	 {items:[{name:"#L.terminal"    , type:"LABEL" , value:"航廈"}]},
	 {items:[{name:"#F.terminal"    , type:"SELECT",  bind:"terminal", init:allTerminals}]},
	 {items:[{name:"#L.flightType"  , type:"LABEL" , value:"出/入境"}]},
	 {items:[{name:"#F.flightType"  , type:"TEXT"  ,  bind:"flightType", size:4, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.departure"   , type:"LABEL" , value:"出發地"}]},
	 {items:[{name:"#F.departure"   , type:"TEXT"  ,  bind:"departure", size:20}]},	 
	 {items:[{name:"#L.scheduleTime", type:"LABEL" , value:"預計時間"}]},
	 {items:[{name:"#F.scheduleTime", type:"TEXT"  ,  bind:"scheduleTime", size:5}]},
	 {items:[{name:"#L.actualTime"  , type:"LABEL" , value:"實際時間"}]},
	 {items:[{name:"#F.actualTime"  , type:"TEXT"  ,  bind:"actualTime", size:5}]},
	 {items:[{name:"#L.gate"        , type:"LABEL" , value:"登機門"}]},
	 {items:[{name:"#F.gate"        , type:"TEXT"  ,  bind:"gate", size:5}]},
	 {items:[{name:"#L.status"      , type:"LABEL" , value:"狀態"}]},
	 {items:[{name:"#F.status"      , type:"SELECT",  bind:"status", size:4, init:allStatus}]}]}
	  ],
		beginService:"",
		closeService:"doCheckStore"
	});

}

function kweDetail(){
  
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = false;

    var allTerminals = [["","",true],
                 ["第一航廈","第二航廈"],
                 ["1","2"]];
    var vnB_Detail = 3;
    var vatPickerId ="";
    vat.item.make(vnB_Detail, "indexNo"       , {type:"IDX"  ,                     desc:"序號"       });
	vat.item.make(vnB_Detail, "flightDate"    , {type:"TEXT" , size: 7, maxLen:10, mode:"READONLY", desc:"日期"   });
	vat.item.make(vnB_Detail, "flightNo"      , {type:"TEXT" , size: 6, maxLen: 8, mode:"READONLY", desc:"班機代號"});
	vat.item.make(vnB_Detail, "flightCompany" , {type:"TEXT" , size:12, maxLen:20, mode:"READONLY", desc:"航空公司"});
	vat.item.make(vnB_Detail, "terminal"      , {type:"SELECT" , size: 4, maxLen:12, mode:"READONLY", desc:"航廈", init:allTerminals});
	vat.item.make(vnB_Detail, "departure"     , {type:"TEXT" , size:11, maxLen:12, mode:"READONLY", desc:"出發地"}); 
	vat.item.make(vnB_Detail, "scheduleTime"  , {type:"TEXT" , size: 4, maxLen:5 , mode:"READONLY", desc:"預計時間"});
	vat.item.make(vnB_Detail, "actualTime"    , {type:"TEXT" , size: 4, maxLen:5 , mode:"READONLY", desc:"實際時間"});
	vat.item.make(vnB_Detail, "gate"          , {type:"TEXT" , size: 4, maxLen:2 , mode:"READONLY", desc:"登機門"});
	vat.item.make(vnB_Detail, "flightType"    , {type:"TEXT" , size: 4, maxLen:1 , mode:"HIDDEN", desc:"出/入境"   });
	vat.item.make(vnB_Detail, "status"        , {type:"TEXT" , size: 6, maxLen:6 , mode:"READONLY", desc:"狀態"  });
	vat.item.make(vnB_Detail, "headId"        , {type:"ROWID"});
	vat.item.make(vnB_Detail, "process"       , {type:"BUTTON", view: "fixed", desc:"", value:"刪除",  eClick:"doDelete()"});	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														indexType           : "AUTO",
														selectionType       : "NONE",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                        closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "pageLoadSuccess()",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "3",
													    indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage('UPDATE')} }
														});
}

function kweInitial(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	 vat.item.setValueByName("#F.flightDate",vat.bean().vatBeanOther.flightDate);
	 vat.item.setValueByName("#F.flightType",vat.bean().vatBeanOther.flightType);
     vat.bean.init(function(){
		return "process_object_name=buFlightScheduleService&process_object_method_name=executeInitial";
     },{other: true});
     vat.item.setValueByName("#F.flightDate",vat.bean().vatBeanOther.flightDate);
     vat.item.setValueByName("#F.updateFlag","N");
     vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
	 doFormAccessControl();
  }
}

function doSearch(){
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
	doFormAccessControl();
}
function loadBeforeAjxService(){
	var processString = "process_object_name=buFlightScheduleService&process_object_method_name=getAJAXPageData" +
	                    "&flightDate=" + vat.item.getValueByName("#F.flightDate")+
	                    "&flightNo=" + vat.item.getValueByName("#F.flightNo") +
	                    "&flightCompany=" + vat.item.getValueByName("#F.flightCompany")  +
	                    "&terminal=" + vat.item.getValueByName("#F.terminal")  +
	                    "&flightType=" + vat.item.getValueByName("#F.flightType")  +
	                    "&departure=" + vat.item.getValueByName("#F.departure")  +
	                    "&scheduleTime=" + vat.item.getValueByName("#F.scheduleTime")  +
	                    "&actualTime=" + vat.item.getValueByName("#F.actualTime")  +
	                    "&gate=" + vat.item.getValueByName("#F.gate")  +
	                    "&status=" + vat.item.getValueByName("#F.status") ;
	//alert(    processString);                
	return processString;
}



function doFormAccessControl(){
    var allProcess = null;
    var vsFormId = vat.item.getValueByName("#F.headId");
    var vsStatus = vat.item.getValueByName("#F.status");
	vat.item.setAttributeByName("vatDetailDiv"   , "readOnly", true);


}

function pageLoadSuccess(){
	//alert("kwePageLoadSuccess");
	//countTotalQuantity();
	//getTheFirstItemCategory();

}

function pageLoadLogSuccess(){
	//alert("kwePageLoadSuccess");
	//countTotalQuantity();
	//getTheFirstItemCategory();

}

function changeUpdateFlag(){
	if("Y" == vat.item.getValueByName("#F.updateFlag")) {
		vat.item.setValueByName("#F.updateFlag","N");
	}else{
	   // if(""!=vat.item.getValueByName("#F.employeeCode") && ""!=vat.item.getValueByName("#F.employeeName"))
			vat.item.setValueByName("#F.updateFlag","Y");
	//	else
		//	alert("請輸入修改人員工號");
	}
	doFormAccessControl();
}


function getEmployeeInfo() {
    
    if ("" !=vat.item.getValueByName("#F.employeeCode")) {
        vat.item.setValueByName("#F.employeeCode",vat.item.getValueByName("#F.employeeCode").toUpperCase());

        vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
        vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=getEmployeeInfo";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("" != vat.bean().vatBeanOther.employeeName ){
                    vat.item.setValueByName("#F.employeeName", vat.bean().vatBeanOther.employeeName);
                    vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
                    vat.bean().vatBeanOther.executeEmployeeName=vat.item.getValueByName("#F.employeeName");
			     }else{
			    	vat.item.setValueByName("#F.employeeName", "");
			     	alert("員工代號錯誤，請重新輸入！");
	 				vat.form.item.setFocus( "#F.employeeCode" );			     	
			     }
		     }
		});
    }else{
    	vat.item.setValueByName("#F.employeeName","");
    }
}



function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	processString = "process_object_name=soDeliveryService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status");


	return processString;
}

function saveSuccessAfter() {

}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" +
		"?programId=IM_MOVEMENT" +
		"&levelType=ERROR" +
        "&processObjectName=imMovementService" +
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function refreshForm(vsHeadId) {
    document.forms[0]["#formId"].value = vsHeadId;

    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
    vat.bean().vatBeanOther.updateForm = document.forms[0]["#formId"].value == "" ? "N" : "Y";
    vat.block.submit(
    function () {
        return "process_object_name=soDeliveryService&process_object_method_name=executeInitial";
    }, {
        other: true,
        funcSuccess: function () {
            vat.item.bindAll();
            vat.block.pageRefresh(vnB_Detail);
            vat.block.pageRefresh(vnB_Log);
            doFormAccessControl();   
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
function openModifyPage(vsType){
	var vFormId="";
	var width  = "600";
    var height = "600";
	if("UPDATE"==vsType){
    	var nItemLine = vat.item.getGridLine();
   	    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    }
    	 
   	var url = "/erp/Bu_FlightSchedule:create:20110112.page?formId=" + vFormId+"&flightType="+vat.item.getValueByName("#F.flightType");
	sc=window.open(url, '航班資料維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
	//sc.moveTo(0,0);
	//sc.resizeTo(screen.availWidth,screen.availHeight);
	
}


function kweCreateInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.item.setValueByName("#F.headId",vat.bean().vatBeanOther.formId);
     vat.item.setValueByName("#F.flightType",vat.bean().vatBeanOther.flightType);
     var flightTypeName = "O" == vat.bean().vatBeanOther.flightType?"出境":"入境";
     vat.item.setValueByName("#F.flightTypeName",flightTypeName);
     vat.bean.init(function(){
		return "process_object_name=buFlightScheduleService&process_object_method_name=executeCreateInitial"; 
   		},{other: true});
     vat.item.bindAll();
  }
}

function kweCreateButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.submit"      , type:"IMG"    , value:"送出",  src:"./images/button_submit.gif", eClick:"doSubmit()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"    , value:"清除",  src:"./images/button_reset.gif", eClick:"doClear()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  , value:"　"}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
}

function kweCreateHeader(){ 
var allStatus = [["","",true],
                 ["已到","準時","取消","時間更改"],
                 ["已到","準時","取消","時間更改"]];
var allTerminals = [["","",true],
                 ["第一航廈","第二航廈"],
                 ["1","2"]];
                 
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"航班資料維護", rows:[
	 {row_style:"", cols:[   
	 {items:[{name:"#L.flightType"    , type:"LABEL" , value:"出/入境"}]},
	 {items:[{name:"#F.flightType"    , type:"TEXT"  ,  bind:"flightType", size:4, mode:"READONLY"},
	         {name:"#F.flightTypeName", type:"TEXT", size:4, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.flightDate"  , type:"LABEL" , value:"日期"},
	         {name:"#F.updateFlag"  , type:"TEXT"  ,  size:4, mode:"HIDDEN"}]},
	 {items:[{name:"#F.flightDate"  , type:"DATE"  ,  bind:"flightDate"},
	         {name:"#F.headId"      , type:"TEXT"  ,  size:4, bind:"headId",back:false ,mode:"READONLY"}]}]},
	 {row_style:"", cols:[        
	 {items:[{name:"#L.flightNo"    , type:"LABEL" , value:"班機代號"}]},
	 {items:[{name:"#F.flightNo"    , type:"TEXT"  ,  bind:"flightNo", mask:"CCCCCC"}]}]},
	 {row_style:"", cols:[   
	 {items:[{name:"#L.flightCompany", type:"LABEL", value:"航空公司"}]},
	 {items:[{name:"#F.flightCompany", type:"TEXT" ,  bind:"flightCompany" ,size:20}]}]},
	 {row_style:"", cols:[   
	 {items:[{name:"#L.terminal"    , type:"LABEL" , value:"航廈"}]},
	 {items:[{name:"#F.terminal"    , type:"SELECT",  bind:"terminal", init:allTerminals}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.departure"   , type:"LABEL" , value:"出發地"}]},
	 {items:[{name:"#F.departure"   , type:"TEXT"  ,  bind:"departure", size:20}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.scheduleTime", type:"LABEL" , value:"預計時間"}]},
	 {items:[{name:"#F.scheduleTime", type:"TEXT"  ,  bind:"scheduleTime", size:5}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.actualTime"  , type:"LABEL" , value:"實際時間"}]},
	 {items:[{name:"#F.actualTime"  , type:"TEXT"  ,  bind:"actualTime", size:5}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.gate"        , type:"LABEL" , value:"登機門"}]},
	 {items:[{name:"#F.gate"        , type:"TEXT"  ,  bind:"gate", size:5}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.status"      , type:"LABEL" , value:"狀態"}]},
	 {items:[{name:"#F.status"      , type:"SELECT",  bind:"status", size:4, init:allStatus}]}]}
	  ],
		beginService:"",
		closeService:"doCheckStore"
	});
}


function doSubmit(){
	var vsAllowSubmit         = true;
	var alertMessage          ="是否確定送出?";
	var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');
    var processString 	      ="";


	if(confirm(alertMessage)){
	    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
		var vsAllowSubmit = true;
    	if(vsAllowSubmit){
    	
  	  	      vat.bean().vatBeanOther.formId =  formId;
	          processString = "process_object_name=buFlightScheduleAction&process_object_method_name=performTransaction";
			  vat.block.submit(function(){return processString;},{
		                    bind:true, link:true, other:true,
		                    funcSuccess:function(){
				        
				        	}}
			  );
        }
	}
}



//調撥單匯出明細
function exportFormData(){
    //alert("export to xml file...")
	var beanName = "BU_FLIGHT_SCHEDULE";
	var url;
    	url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&processObjectName=buFlightScheduleService" +
              "&processObjectMethodName=findViewByMap" +
	          "&flightDate=" + vat.item.getValueByName("#F.flightDate")+
	          "&flightNo=" + vat.item.getValueByName("#F.flightNo") +
	          "&flightCompany=" + vat.item.getValueByName("#F.flightCompany")  +
	          "&terminal=" + vat.item.getValueByName("#F.terminal")  +
	          "&flightType=" + vat.item.getValueByName("#F.flightType")  +
	          "&departure=" + vat.item.getValueByName("#F.departure")  +
	          "&scheduleTime=" + vat.item.getValueByName("#F.scheduleTime")  +
	          "&actualTime=" + vat.item.getValueByName("#F.actualTime")  +
	          "&gate=" + vat.item.getValueByName("#F.gate")  +
	          "&status=" + vat.item.getValueByName("#F.status") +
              "&gridFieldName=buFlightSchedule" 
    var width = "200";
    var height = "30";
    vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : function(){
    			window.open(url, '班機時刻匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});

}
function importFormData(){
    var beanName = "BU_FLIGHT_SCHEDULE";
    var suffix ="";
	suffix =
		"&importBeanName="+ beanName +
		"&importFileType=XLS" +
        "&processObjectName=buFlightScheduleService" +
        "&processObjectMethodName=executeImport" +
        "&arguments=" +vat.item.getValueByName("#F.flightType") +
        "&parameterTypes=STRING" +
        "&blockId=" + vnB_Detail
		//'menubar=no,resizable=no,scrollbars=no,status=no,left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2;

	return suffix;

}

function afterImportSuccess(){
//	alert("FindTheFirstItemCategory");
	//refreshHeadData();
}
function doDelete(){
	var nItemLine = vat.item.getGridLine();
   	var vFormId = vat.item.getGridValueByName("headId", nItemLine);
   	if(true){
    	
  	  	      vat.bean().vatBeanOther.formId =  vFormId;
	          processString = "process_object_name=buFlightScheduleService&process_object_method_name=deleteFlightScheduleByHeadId";
			  vat.block.submit(function(){return processString;},{
		                    bind:true, link:true, other:true,
		                    funcSuccess:function(){
				        		vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
				        	}}
			  );
        }
  
}

function openReportWindow(){ 
    vat.bean().vatBeanOther.flightDate  = vat.item.getValueByName("#F.flightDate");
    vat.bean().vatBeanOther.flightNo = vat.item.getValueByName("#F.flightNo");
    vat.bean().vatBeanOther.flightCompany = vat.item.getValueByName("#F.flightCompany");
    vat.bean().vatBeanOther.terminal = vat.item.getValueByName("#F.terminal");
    vat.bean().vatBeanOther.flightType = vat.item.getValueByName("#F.flightType");
    vat.bean().vatBeanOther.departure = vat.item.getValueByName("#F.departure");
    vat.bean().vatBeanOther.scheduleTime = vat.item.getValueByName("#F.scheduleTime");
    vat.bean().vatBeanOther.actualTime = vat.item.getValueByName("#F.actualTime");
    vat.bean().vatBeanOther.gate = vat.item.getValueByName("#F.gate");
    vat.bean().vatBeanOther.status = vat.item.getValueByName("#F.status");

	vat.block.submit(
		function(){return "process_object_name=buFlightScheduleService"+
					"&process_object_method_name=getReportConfig";},{other:true,
                 			funcSuccess:function(){
					eval(vat.bean().vatBeanOther.reportUrl);
		}}
	); 
}