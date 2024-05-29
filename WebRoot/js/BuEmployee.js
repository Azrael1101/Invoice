/*** 
 *	檔案: BuCommonPhrase.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Detail = 1;
var vnB_master = 2; 
  
function kweImBlock(){
  kweImInitial();
  kweButtonLine();
  kweImHeader();
  if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","主檔資料"   ,"vatBlock_Master"  			,"images/tab_master_data_dark.gif"    		,"images/tab_master_data_light.gif", false, "doPageDataSave()");                                                                                                                                                                                                                                          
		vat.tabm.createButton(0 ,"xTab2","Line" ,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false , "");
  }
  kweImMaster();
	kweImDetail();
}
 
function kweButtonLine(){
		var vsMaxRecord     = 0;
		var vsCurrentRecord = 0;
   
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
		{row_style:"", cols:[
			{items:[
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'execSubmitAction("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 2, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 2, mode:"READONLY" }
			],td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
		]}
	], 	 	 
		beginService:"",
		closeService:""			
	});
}

function kweImHeader(){ 
vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"員工資料設定", 
	rows:[
	 	{row_style:"", cols:[
			{items:[{name:"#L.employeeCode", type:"LABEL" , value:"工號"}]},
			{items:[{name:"#F.employeeCode", type:"TEXT",  bind:"employeeCode", size:20, back:false, mode:"readOnly"},
							{name:"#F.remark2"    ,  type:" ",bind:"remark2", size:12,mode:"readOnly"},
							{name:"#L.remark2", 			type:"LABEL" , value:" 專櫃人員"}]},
			{items:[{name:"#L.employeePosition", type:"LABEL" , value:"職稱"}]},
			{items:[{name:"#F.employeePosition", type:"TEXT",  bind:"employeePosition" , size:20 , mode:"readOnly"}]}
	 	]},
	 	{row_style:"", cols:[
		 	{items:[{name:"#L.arriveDate", type:"LABEL" , value:"到職日"}]},
			{items:[{name:"#F.arriveDate", type:"DATE",  bind:"arriveDate" , size:20 , mode:"readOnly"}]},
			{items:[{name:"#L.leaveDate", type:"LABEL" , value:"離職日"}]},
			{items:[{name:"#F.leaveDate", type:"DATE",  bind:"leaveDate" , size:20 , mode:"readOnly"  }]}
	 	]},
	 	{row_style:"", cols:[
		 	{items:[{name:"#L.remark1"  , type:"LABEL", value:"備註"}]},
		 	{items:[{name:"#F.remark1"  , type:"TEXT",   bind:"remark1", size:20, mode:"readOnly"}] , td:"colSpan=3"}
	 	]}
  ], 	 
		beginService:"",
		closeService:""			
	});

}

function kweImMaster(){
var vaOpt1 = [["", "", ""], ["是 ", "否"], ["Y", "N"]];
vat.block.create(vnB_master, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
	rows:[  
		{row_style:"", cols:[ 					 
			{items:[{name:"#L.loginName", type:"LABEL", value:"系統帳號"}]},
			{items:[{name:"#F.loginName"   , type:"TEXT" ,  bind:"loginName", size:20}]},
			{items:[{name:"#L.isDepartmentManager", type:"LABEL", value:"部門主管"}]},
			{items:[{name:"#F.isDepartmentManager"   , type:"SELECT" ,  bind:"isDepartmentManager", size:20, init:vaOpt1}]}
		]},
		{row_style:"", cols:[
			{items:[{name:"#L.reportLoginName", type:"LABEL", value:"BI登入帳號"}]},
			{items:[{name:"#F.reportLoginName"   , type:"TEXT" ,  bind:"reportLoginName", size:20}]},
			{items:[{name:"#L.reportPassword", type:"LABEL", value:"BI登入密碼"}]},
			{items:[{name:"#F.reportPassword"   , type:"TEXT" ,  bind:"reportPassword", size:20}]}
		]}, 					 
		{row_style:"", cols:[
			{items:[{name:"#L.remark3"     , type:"LABEL", value:"備註"}]},
			{items:[{name:"#F.remark3"     , type:"TEXT",   bind:"remark3", size:150, maxLen:200}],td:"colSpan=3"}
		]}
	],
		beginService:"",
		closeService:""			
	});
	
}

function kweImDetail(){
  var vbCanGridDelete = true;
  var vbCanGridAppend = true;
  var vbCanGridModify = true;

  vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX" , desc:"序號"});  
	vat.item.make(vnB_Detail, "isSelected"			, {type:"CHECKBOX", size:15 , desc:"鎖定"});  
	vat.item.make(vnB_Detail, "brandCode"     	, {type:"TEXT"  , size:15, desc:"品牌代碼",mode:"READONLY"});
	vat.item.make(vnB_Detail, "brandName"     	, {type:"TEXT"  , size:15, desc:"品牌名稱",mode:"READONLY"});
	vat.item.make(vnB_Detail, "attribute1"    	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "attribute2"    	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "attribute3"    	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "attribute4"    	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "attribute5"    	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "createdBy"     	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "creationDate"  	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "lastUpdatedBy" 	, {type:"HIDDEN"});
	vat.item.make(vnB_Detail, "lastUpdateDate"	, {type:"HIDDEN"});
	vat.block.pageLayout(vnB_Detail, {
														id: "vatDetailDiv",
														pageSize: 10,
								            canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "",
														appendAfterService  : "",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "",						
														eventService        : "",   
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
			vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function kweImInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value
        };
    //vat.bean.init(function(){
		//return "process_object_name=buEmployeeService&process_object_method_name=executeInitial"; 
     //},{other: true});
  }
}

function kweInitialChild(){
		vat.item.setAttributeByName("#F.employeeCode","readOnly",true);
		//vat.tabm.displayToggle(0, "xTab1", true);
		//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function doPageDataSave(){
    //alert("doPageDataSave");
    vat.block.pageDataSave(vnB_Detail);
}

function doPageRefresh(){
    //alert("doPageRefresh");
    vat.block.pageRefresh(0);
}


/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
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
		  vsformId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].employeeCode;
		  vat.bean().vatBeanOther.formId = vsformId;
		  refreshForm();
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}

function refreshForm(){
	vat.block.submit(
		function(){
			return "process_object_name=buEmployeeService&process_object_method_name=executeInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     		             vat.item.bindAll();
     		             vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
			}});
}


function execSubmitAction(formAction){
	vat.bean().formAction = formAction;
	var alertMessage ="是否確定送出?";

	if(confirm(alertMessage)){
		doPageDataSave();
	}
}

function kweFormClear(){ 
 	 //vat.bean().loginBrandCode     = document.forms[0]["#loginBrandCode"    ].value;  	
   //vat.bean().loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" ].value;	  
	 //vat.bean().formId             = "";	
 	 vat.block.submit(function(){return "process_object_name=imMovementService"+
			                    "&process_object_method_name=executeInitial";}, {other:true});
}

/*
	空白頁
*/
function appendBeforeService(){
	return true;
}

/*
	載入LINE資料
*/
function loadBeforeAjxService(){
	var processString = "process_object_name=buEmployeeBrandService&process_object_method_name=getAJAXPageData" + 
	                    "&employeeCode=" + vat.item.getValueByName("#F.employeeCode")
	                    //"&employeeCode=T48351"
	return processString;			
}


/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
		processString = "process_object_name=buEmployeeBrandService&process_object_method_name=updateAJAXPageLinesData" + 
	                    "&employeeCode=" + vat.item.getValueByName("#F.employeeCode")+
	                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value
	return processString;
}

function saveSuccessAfter(){
	//alert('update ok');
		if(vat.bean().formAction == "SUBMIT"){
		//alert('submit');
					vat.bean().formAction = "";
				  vat.block.submit(function(){return "process_object_name=buEmployeeAction"+
			      "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
		}
				      
}

function getEmployeeNameByLine(){
	var nItemLine = vat.item.getGridLine();
	var vemployeeCode = vat.item.getGridValueByName("employeeCode"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setGridValueByName("employeeCode", nItemLine, vemployeeCode);
	vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vemployeeCode,
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               vat.item.setGridValueByName("employeeName", nItemLine, vat.ajax.getValue("EmployeeName", oXHR.responseText));
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

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  vsformId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].employeeCode;
		  vat.bean().vatBeanOther.formId = vsformId;
		  refreshForm();
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
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
		  vsformId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].employeeCode;
		  vat.bean().vatBeanOther.formId = vsformId;
		  refreshForm();
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
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
		  vsformId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].employeeCode;
		  vat.bean().vatBeanOther.formId = vsformId;
		  refreshForm();
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
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
		  vsformId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].employeeCode;
		  vat.bean().vatBeanOther.formId = vsformId;
		  refreshForm();
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}