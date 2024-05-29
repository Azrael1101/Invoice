/*** 
 *	檔案: exprotErpToPos.js
 *	說明：產生檔案至POS後台
 *	修改：Mark
 */
 
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){

 	formInitial(); 
	buttonLine(); 
  	headerInitial();
	
	doFormAccessControl();
}

// 初始化
function formInitial(){  
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=exportAction&process_object_method_name=performPOSInitial"; 
	    	},{								
	    		other: true
    	});
  	}
}

function buttonLine(){
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"}
	 			
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}	

// 目標主檔
function headerInitial(){
 
var allExportTypes = [ ["","","true"], ["商品主檔", "促銷檔", "國際碼", "客戶", "專櫃人員", "組合單主檔" ] , [ "3", "4", "6", "8", "1", "9" ] ];
           
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"POS檔產生作業", 
		rows:[
			 
			{row_style:"", cols:[
				{items:[{name:"#L.exportType", 		type:"LABEL", 	value:"匯出類型<font color='red'>*</font>"}]},
				{items:[{name:"#F.exportType", 		type:"SELECT", 	bind:"exportType", init:allExportTypes, size:1, back:false}]},
				{items:[{name:"#L.startDate", 			type:"LABEL", 	value:"起始日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.startDate", 			type:"date", 	bind:"startDate",size:10}]},
				{items:[{name:"#L.endDate", 			type:"LABEL", 	value:"結束日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.endDate", 			type:"date", 	bind:"endDate",size:10}]},
				{items:[{name:"#L.employeeCode", 	type:"LABEL", 	value:"登入人員"}]},
				{items:[{name:"#F.employeeCode", 	type:"TEXT", 	bind:"employeeCode", mode:"HIDDEN", back:false},
						{name:"#F.employeeName", 	type:"TEXT", 	bind:"employeeName", mode:"READONLY", back:false}]} 
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.txt", 		type:"LABEL", 	value:"商品主檔與國際碼日期需選擇<font color='red'>啟用日期的前一天</font>，其他的就選擇啟用日"}],td:" colSpan=8"} 
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
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

function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SIGNING" == formAction){
		alertMessage = "是否確定送出?";
	}
	var exportType = vat.item.getValueByName("#F.exportType");
	var startDate = vat.item.getValueByName("#F.startDate");
	var endDate = vat.item.getValueByName("#F.endDate");
	if(exportType<=0){
		alert("請選擇匯出類型");
		return;
	}
	if(startDate<=0){
		alert("請選擇開始日期");
		return;
	}
	if(endDate<=0){
		alert("請選擇結束日期");
		return;
	}		
	if(confirm(alertMessage)){		
		vat.block.submit(function(){return "process_object_name=exportService"+
			"&process_object_method_name=exportErpToPos";},{
			bind:true, link:true, other:true,
			funcSuccess:function(){
				window.top.close();
			}}
		);		
    } 
}

// 依狀態鎖form
function doFormAccessControl( match ){

	//initialFormAccessControl();
	
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.exportType", "");
	vat.item.setValueByName("#F.startDate", "");
	vat.item.setValueByName("#F.endDate", "");
}