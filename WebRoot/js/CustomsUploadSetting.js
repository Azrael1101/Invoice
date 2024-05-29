/*** 
 *	檔案: CustomsUploadSetting.js
 *	說明：表單明細
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Setting = 2

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
  	settingInfoInitial();
	//doFormAccessControl();
}
function formInitial(){ 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId                : document.forms[0]["#formId"].value,
          //companyCode  		: "",      
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
        
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=customsUploadSettingAction&process_object_method_name=performInitial"; 
	    	},{
	    		other:true
	    	}
	    );
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
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:"closeWindows('CONFIRM')"},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:"doSubmit('SUBMIT')"},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){ 
		var uploadWays = [["","schedule",false],["即時","排程"],["realTime","schedule"]];
		var uploadSwitchs = [["","",false],["開啟","關閉"],["Y","N"]];
		var beginingTime = [["","",false],["0","1","2","3","4","5","6","7","8","9","10","11","12",
										"13","14","15","16","17","18","19","20","21","22","23"],
										["0","1","2","3","4","5","6","7","8","9","10","11","12",
										"13","14","15","16","17","18","19","20","21","22","23"]];
		var endingTime = [["","",false],["0","1","2","3","4","5","6","7","8","9","10","11","12",
										"13","14","15","16","17","18","19","20","21","22","23","24"],
										["0","1","2","3","4","5","6","7","8","9","10","11","12",
										"13","14","15","16","17","18","19","20","21","22","23","24"]];
		
		vat.block.create( vnB_Header, { //vnB_Header = 
			id: "vatBlock_Head", generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"海關上傳設定", 
			rows:[
				{row_style:"", cols:[
					{items:[{name:"#L.uploadWay", type:"LABEL", value:"上傳方式<font color='red'>*</font>" }]},
					{items:[{name:"#F.uploadWay", type:"SELECT", bind:"uploadWay" , size:25, maxLen:25 , init:uploadWays ,eChange:"isEnter()"}]},
				  	{items:[{name:"#L.enable", type:"LABEL", value:"啟閉開關<font color='red'>*</font>"}]},
					{items:[{name:"#F.enable", type:"SELECT", bind:"enable", size:25, maxLen:25, init:uploadSwitchs }]}			
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.beginingTime", type:"LABEL", value:"開啟時間<font color='red'>*</font>" }]},
					{items:[{name:"#F.beginingTime", type:"SELECT", bind:"beginingTime" , size:25, maxLen:25 , init:beginingTime }]},
				  	{items:[{name:"#L.endingTime", type:"LABEL", value:"結束時間<font color='red'>*</font>"}]},
					{items:[{name:"#F.endingTime", type:"SELECT", bind:"endingTime", size:25, maxLen:25, init:endingTime }]}					
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.beginingTime", type:"LABEL", value:"間隔時間(單位:小時)<font color='red'>*</font>" }]},
					{items:[{name:"#F.hours", type:"TEXT", bind:"hours", size:25, maxLen:25 }]},
				  	{items:[{name:"#L.hoursAgo", type:"LABEL", value:"確認幾小時前之資料<font color='red'>*</font>"}]},
					{items:[{name:"#F.hoursAgo", type:"TEXT", bind:"hoursAgo", size:25, maxLen:25 }]}				
				]}
			], 	
			beginService:"",
			closeService:""			
		});
}

function settingInfoInitial(){ 
		
		vat.block.create( vnB_Setting, { //vnB_Header = 
			id: "vatBlock_Setting", generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"現行設定", 
			rows:[
				{row_style:"", cols:[
					{items:[{name:"#L.way", type:"LABEL", value:"方式" }]},	
					{items:[{name:"#L.uploadWay", type:"LABEL", value:"開關" }]},	
					{items:[{name:"#L.uploadWay", type:"LABEL", value:"開啟時間" }]},	
					{items:[{name:"#L.uploadWay", type:"LABEL", value:"結束時間" }]},	
					{items:[{name:"#L.uploadWay", type:"LABEL", value:"間隔" }]},
					{items:[{name:"#L.uploadWay", type:"LABEL", value:"幾小時以前" }]}			
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.schedule", type:"LABEL", value:"排程" }]},
					{items:[{name:"#F.scheduleEnable", type:"TEXT", bind:"scheduleEnable" , size:25, maxLen:25  }]},
					{items:[{name:"#F.scheduleBeginingTime", type:"TEXT", bind:"scheduleBeginingTime" , size:25, maxLen:25  }]},
					{items:[{name:"#F.scheduleEndingTime", type:"TEXT", bind:"scheduleEndingTime" , size:25, maxLen:25  }]},
					{items:[{name:"#F.scheduleHours", type:"TEXT", bind:"scheduleHours", size:25, maxLen:25 }]},
					{items:[{name:"#F.scheduleHoursAgo", type:"TEXT", bind:"scheduleHoursAgo", size:25, maxLen:25 }]}					
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.realTime", type:"LABEL", value:"即時" }]},
					{items:[{name:"#F.realTimeEnable", type:"TEXT", bind:"realTimeEnable" , size:25, maxLen:25  }]},
					{items:[{name:"#F.realTimeBeginingTime", type:"TEXT", bind:"realTimeBeginingTime" , size:25, maxLen:25  }]},
					{items:[{name:"#F.realTimeEndingTime", type:"TEXT", bind:"realTimeEndingTime" , size:25, maxLen:25  }]},
					{items:[{name:"#F.realTimeHours", type:"TEXT", bind:"realTimeHours", size:25, maxLen:25 }]},
					{items:[{name:"#F.realTimeHoursAgo", type:"TEXT", bind:"realTimeHoursAgo", size:25, maxLen:25 }]}						
				]}	
			], 	
			beginService:"",
			closeService:""			
		});
}

// 動態設定公司代號為大寫
function changeCompanyCode(){
	var companyCode = vat.item.getValueByName("#F.companyCode").replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setValueByName("#F.companyCode", companyCode);
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

// 送出的返回
function createRefreshForm(){

vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
	refreshForm("");
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
	var beginingTime = parseInt(vat.item.getValueByName("#F.beginingTime"));
	var endingTime = parseInt(vat.item.getValueByName("#F.endingTime"));
	
	if(beginingTime < endingTime)
	{
		if(confirm(alertMessage)){
			//vat.bean().vatBeanOther.formAction = formAction;
			vat.block.submit(function(){
					return "process_object_name=customsUploadSettingAction"+
					"&process_object_method_name=performTransaction";
				},{
					bind:true, link:true, other:true 
				}
			);
		}
	}else{
		alert("「開啟時間」需小於「結束時間」");
	}
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].companyCode;
		  refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	//vat.bean().vatBeanOther.companyCode       = companyCode;
	document.forms[0]["#formId"].value = code;
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
	vat.block.submit(
		function(){
			return "process_object_name=customsUploadSettingAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				doFormAccessControl();
     	}}
    );	
}

// 依formId鎖form
function doFormAccessControl(){
    var formId = vat.bean().vatBeanOther.formId;
    var form    = vat.bean("form");
    if(vat.bean().vatBeanPicker.result!=null){
    //alert("1");
        //vat.item.setAttributeByName("#F.exchangeRateType", "readOnly", true);
        vat.item.setAttributeByName("#F.companyCode", "readOnly", true);
       
    }
    else{
    //alert("2");
        //vat.item.setValueByName("#F.exchangeRate", "");
       // vat.item.setAttributeByName("#F.exchangeRateType", "readOnly", false);
        vat.item.setAttributeByName("#F.companyCode", "readOnly", false);
        //vat.item.setAttributeByName("#F.sourceCurrency", "readOnly", false);    
        //vat.item.setAttributeByName("#F.againstCurrency", "readOnly", false);   
        //vat.item.setAttributeByName("#F.beginDate", "readOnly", false);
    }
    
    var enable = vat.item.getValueByName("#F.enable");
    if(enable == "Y"){
        vat.item.setValueByName("#F.enable", "N");
    }
    else{
        vat.item.setValueByName("#F.enable", "Y");
    }
}

function isEnter(){
	var uploadWay;
	uploadWay = vat.item.getValueByName("#F.uploadWay");
	
	if(uploadWay == "realTime"){
		vat.item.setAttributeByName("#F.beginingTime", "readOnly", true);	
		vat.item.setAttributeByName("#F.endingTime", "readOnly", true);
		vat.item.setAttributeByName("#F.hours", "readOnly", true);
	}
	
	if(uploadWay == "schedule"){
		vat.item.setAttributeByName("#F.beginingTime", "readOnly", false);	
		vat.item.setAttributeByName("#F.endingTime", "readOnly", false);
		vat.item.setAttributeByName("#F.hours", "readOnly", false);
	}
}


