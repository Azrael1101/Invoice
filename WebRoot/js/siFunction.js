/*** 
 *	檔案: imLocaton.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by david
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Detail = 1;


function kweImBlock(){
  kweImInitial();
  kweButtonLine();
  kweImHeader();

	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");                                                                                                                                                                                                                                          
		vat.tabm.createButton(0 ,"xTab1","明細檔資料" ,"vatDetailDiv"        		,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", "none", "doPageRefresh()");                                                                                                                                                
		
  }  
	kweImDetail();
}

function kweImInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: "",	
          currentRecordNumber : 0,
	     		lastRecordNumber    : 0
        };
   	vat.bean.init(	
  		function(){
				return "process_object_name=siFunctionService&process_object_method_name=executeInitial"; 
    	},{
    		other: true
    	});
       		 vat.item.setAttributeByName("#F.functionCode","readOnly",false);  
  }
 
}

function kweImHeader(){ 
	
	var allObjectTypes = vat.bean("allObjectTypes");
	
	vat.block.create( 0, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"系統功能維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{ name:"#L.functionCode", type:"LABEL", value:"功能代號<font color='red'>*</font>"}]},
				{items:[{ name:"#F.functionCode", type:"TEXT", bind:"functionCode", eChange:"changeFunctionName()",size:20 }]},
				{items:[{ name:"#L.functionName", type:"LABEL", value:"功能名稱" }]},
				{items:[{ name:"#F.functionName", type:"TEXT", bind:"functionName", size:20 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.creationDate", type:"LABEL" , value:"建立日期" }]},
				{items:[{name:"#F.creationDate", type:"TEXT", bind:"creationDate", mode:"READONLY", size:40 }]},
				{items:[{name:"#L.createdBy", type:"LABEL" , value:"建立人員" }]},
				{items:[{name:"#F.createdBy"  	, type:"HIDDEN", bind:"createdBy"},
		 						{name:"#F.createdByName", type:"TEXT",   bind:"createdByName", size:20, mode:"READONLY"}
		 						]}
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});

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
	 									 service:"/erp/Si:Si_Function_Object:search20090813.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
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
	
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function kweImDetail(){

	var allObjectTypes  = vat.bean("allObjectTypes");
//	alert( allObjectTypes );
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
  
  vat.item.make(vnB_Detail, "indexNo"					, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_Detail, "objectCode"			, {type:"TEXT" , size:15, maxLen:20, desc:"元件代號"          	});
	vat.item.make(vnB_Detail, "objectName"			, {type:"TEXT" , size:18, maxLen:20, desc:"元件名稱"          	});
	vat.item.make(vnB_Detail, "objectType"			, {type:"SELECT", init:allObjectTypes, size: 8, maxLen:20, desc:"元件類型"  });
	vat.item.make(vnB_Detail, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  vat.item.make(vnB_Detail, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_Detail, "message"         , {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vnB_Detail, {
														id: "vatDetailDiv",
														pageSize: 10,											
								            canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function loadBeforeAjxService(){
//    alert("loadBeforeAjxService");	// 第一次載入 重新整理
	var processString = "process_object_name=siFunctionObjectService&process_object_method_name=getAJAXPageData" + 
	                    "&functionCode=" + vat.item.getValueByName("#F.functionCode") + 
	                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value; 
	                    
	return processString;				
								
}

function saveBeforeAjxService(){
//    alert("saveBeforeAjxService"); 第一頁 翻到前或後頁 最後一頁	
	var processString = "process_object_name=siFunctionObjectService&process_object_method_name=updateOrSaveAJAXPageLinesData" + 
		"&functionCode=" + vat.item.getValueByName("#F.functionCode");
	return processString;
} 

function saveSuccessAfter(){ 
//    alert("saveSuccessAfter");	// 按下一頁上一頁等按鈕背後存檔成功後
	vat.block.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg === "") {
		if ("changeRelationData" == afterSavePageProcess) {
			//alert("呼叫了");
		    var processString = "process_object_name=siFunctionObjectService&process_object_method_name=updateObjectData" + 
	                    "&functionCode=" + vat.item.getValueByName("#F.functionCode") + 
	                    "&functionName=" + vat.item.getValueByName("#F.functionName");
	                    
	      vat.ajax.startRequest(processString, function () {
				if (vat.ajax.handleState()) {
			  	vat.block.pageRefresh(0);
				}
			});	
		}
	} else {
		alert("錯誤訊息： " + errorMsg);
	}
	afterSavePageProcess = "";			
} 

function loadSuccessAfter(){
//    alert("loadSuccessAfter");	// saveSuccessAfter 之後呼叫 載入資料成功
//	vat.item.setGridAttributeByName("objectCode", "readOnly", true);
} 


function appendBeforeService(){
//    alert("appendBeforeService");	 // 新增空白頁
	return true;
}    

function appendAfterService(){
//    alert("appendAfterService");	// appendBeforeService 新增空白頁成功後
} 

function eventService(){
//    alert("eventService");	 // 欄位向前 向後
/*
	afterSavePageProcess = "changeRelationData";
  vat.block.pageDataSave(1);
  loadBeforeAjxService();
*/  
} 

function doPageRefresh(){
    //alert("doPageRefresh");
    vat.block.pageRefresh(0);
}

function changeFunctionName() {  
	var processString = "process_object_name=siFunctionService" + "&" +
											"process_object_method_name=getAJAXExistFunctionName" + "&" +
											"functionCode=" + vat.item.getValueByName("#F.functionCode");
	
	vat.ajax.startRequest(processString,  function() { 
	  if (vat.ajax.handleState()){
	    if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
	      var functionName = vat.ajax.getValue("functionName", vat.ajax.xmlHttp.responseText);
//	      alert("ajax返回" + result );
	      if(functionName !== ""){
	      	vat.item.setValueByName("#F.functionName"    ,functionName );
	      }
	    }
	  }
	} );
	vat.block.pageDataLoad(1, vnCurrentPage = 1); // grid載入刷新關聯資料
}
	
function execSubmitAction(formAction){
	var functionCode = vat.item.getValueByName("#F.functionCode").replace(/^\s+|\s+$/, '');
	if( functionCode === "" ) {
		alert( "功能代碼欄不能為空值" );
		return;
	}
	
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}
	
	if(confirm(alertMessage)){
		if( functionCode !== "" ){
		
		  	//vat.bean.set( "formAction", formAction );
			  vat.bean().vatBeanOther.formAction = formAction;
			  vat.block.pageDataSave( vnB_Detail ,{ 
			  	funcSuccess:function(){
					  vat.block.submit(
					  	function(){		
					  		return "process_object_name=siFunctionAction"+
			                 "&process_object_method_name=performTransaction";
			        },{
			        	bind:true, 
			        	link:true, 
			        	other:true
			        }
			      );
	      	}
	      });
		} 
		
	}
}
function kweSiInitialLine(){
 		 vat.item.setAttributeByName("#F.functionCode","readOnly",true);
 		 vat.tabm.displayToggle(0, "xTab1", true);
 		 vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
      vat.bean().vatBeanOther.formId       = "";
    	vat.bean.init(	
  		function(){
				return "process_object_name=siFunctionService&process_object_method_name=executeInitial"; 
    	},{
    		other: true
    	});
    	vat.item.bindAll();
    	vat.item.setAttributeByName("#F.functionCode","readOnly",false);
    	vat.tabm.displayToggle(0, "xTab1", false);
    	vat.item.setValueByName("#L.currentRecord", "0");
			vat.item.setValueByName("#L.maxRecord"    , "0");
	 }
}

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
		  vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].functionCode;
		  refreshForm(vsfunctionCode);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
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

function doSubmit(formAction){
	var functionCode = vat.item.getValueByName("#F.functionCode").replace(/^\s+|\s+$/, '');
	if( functionCode === "" ) {
		alert( "功能代碼欄不能為空值" );
		return;
	}
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}
	
	if(confirm(alertMessage)){
		if( functionCode !== "" ){
		
		  	//vat.bean.set( "formAction", formAction );
			  vat.bean().vatBeanOther.formAction = formAction;
			  vat.block.pageDataSave( vnB_Detail ,{ 
			  	funcSuccess:function(){
					  vat.block.submit(
					  	function(){		
					  		return "process_object_name=siFunctionAction"+
			                 "&process_object_method_name=performTransaction";
			        },{
			        	bind:true, 
			        	link:true, 
			        	other:true
			        }
			      );
	      	}
	      });
		} 
		
	}
}

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].functionCode;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsfunctionCode);
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
	  	vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].functionCode;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsfunctionCode);
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
	  	vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].functionCode;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsfunctionCode);
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
	    vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].functionCode;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsfunctionCode);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function refreshForm(vsfunctionCode){
	document.forms[0]["#formId"            ].value = vsfunctionCode;       	 
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.block.submit(
		function(){
			return "process_object_name=siFunctionService&process_object_method_name=executeInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     		 vat.item.bindAll();
     		 vat.item.setAttributeByName("#F.functionCode","readOnly",true);
     		 vat.tabm.displayToggle(0, "xTab1", true);
     		 vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     	}});
}
