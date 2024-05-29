/*** 
 *	檔案: buCountry.js
 *	說明：表單明細
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
}

function formInitial(){ 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value
        };
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=dailyCloseMainManager&process_object_method_name=executeInitial"; 
	    	},{
	    		other: true
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
	 	{items:[
	 	 		{name:"#B.exit"			, type:"IMG"    , value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"			, type:"LABEL"  , value:"　"},
	 			{name:"#B.clear"		, type:"IMG"	, value:"清除",  src:"./images/button_reset.gif"	, eClick:"resetForm()"},
	 			{name:"SPACE"          	, type:"LABEL"	, value:"　"},
	 	 		{name:"#B.submit"		, type:"IMG"	, value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 	 		{name:"SPACE"			, type:"LABEL"	, value:"　"},		
	 			{name:"#B.message"		, type:"IMG"	, value:"訊息提示", src:"./images/button_message_prompt.gif",  eClick:'showMessage()'},
	 			{name:"submitSpace"		, type:"LABEL"	, value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}	

function headerInitial(){ 
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"日關帳維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL", value:"品牌<font color='red'>*</font>" }]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", mode:"READONLY"},
						{name:"#F.brandName", type:"TEXT", bind:"brandName", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.dailyCloseDate", type:"LABEL", value:"關帳日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.dailyCloseDate", type:"DATE", bind:"dailyCloseDate"}]}
			]}
		],
		beginService:"",
		closeService:""			
	});
}

//清除
function resetForm(){
	vat.item.bindAll();
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
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction 		= formAction;
		vat.block.submit(function(){
				return "process_object_name=dailyCloseMainManager"+
				"&process_object_method_name=performTransaction";
			},{
				bind:true, link:true, other:true 
			}
		);
	}
}

function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=DAILY_CLOSE_MAIN_MANAGER" +
		"&levelType=ERROR" +
        "&processObjectName=dailyCloseMainManager" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.brandCode") +
        "&parameterTypes=STRING",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + 
		',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}
