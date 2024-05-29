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
          userType           	: document.forms[0]["#userType"].value,
          formId             	: document.forms[0]["#formId"            ].value
        };
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=dailyCloseManager&process_object_method_name=executeInitial"; 
	    	},{
	    		other: true
	    	}
	    );
  }
  doFormAccessControl();
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
	 			{name:"#B.clear"		, type:"IMG"	, value:"清除",   src:"./images/button_reset.gif"	, eClick:"resetForm()"},
	 			{name:"SPACE"          	, type:"LABEL"	, value:"　"},
	 	 		{name:"#B.submit"		, type:"IMG"	, value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          	, type:"LABEL"	, value:"　"},
	 	 		//{name:"#B.transCmAudit"	, type:"IMG"	, value:"拋保稽", src:"./images/btn.png", eClick:'doSubmit("TRANSFER")'},	 	 		
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
var mode = [["","",false],["否","是"],["N","Y"]];
if(vat.bean().vatBeanOther.userType=="CLOSE"){
	if(vat.bean().vatBeanOther.loginBrandCode==="T2")
	{
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
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.schedule", type:"LABEL", value:"班別<font color='red'>*</font>" }]},
					{items:[{name:"#F.schedule", type:"TEXT", bind:"schedule" }]}
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.enforce", type:"LABEL", value:"強制送出" }]},
					{items:[{name:"#F.enforce", type:"XBOX", bind:"enforce", value:"N" }]}
				]}
				//{row_style:"", cols:[
				//	{items:[{name:"#L.dailyBalanceDate", type:"LABEL", value:"結帳日期"}]},
				//	{items:[{name:"#F.dailyBalanceDate", type:"DATE", bind:"dailyBalanceDate"}]}
				//]},
				//{row_style:"", cols:[
				//	{items:[{name:"#L.transCmAudit", type:"LABEL", value:"是否拋保稽"}]},
				//	{items:[{name:"#F.transCmAudit", type:"SELECT", bind:"transCmAudit", init:mode}]}
				//]}
			],
			beginService:"",
			closeService:""			
			});
		}
    	else
    	{
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
					{items:[{name:"#F.dailyCloseDate", type:"DATE", bind:"dailyCloseDate"}]},
					{items:[{name:"#F.schedule", type:"TEXT", bind:"schedule" , mode:"HIDDEN" }]},
					{items:[{name:"#F.enforce", type:"XBOX" , bind:"enforce", mode:"HIDDEN" }]}
				]}
				//{row_style:"", cols:[
				//	{items:[{name:"#L.dailyBalanceDate", type:"LABEL", value:"結帳日期"}]},
				//	{items:[{name:"#F.dailyBalanceDate", type:"DATE", bind:"dailyBalanceDate"}]}
				//]},
				//{row_style:"", cols:[
				//	{items:[{name:"#L.transCmAudit", type:"LABEL", value:"是否拋保稽"}]},
				//	{items:[{name:"#F.transCmAudit", type:"SELECT", bind:"transCmAudit", init:mode}]}
				//]}
			],
			beginService:"",
			closeService:""			
			});
		}
	}else if(vat.bean().vatBeanOther.userType=="BALANCE"){
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"日結帳維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL", value:"品牌<font color='red'>*</font>" }]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", mode:"READONLY"},
						{name:"#F.brandName", type:"TEXT", bind:"brandName", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.dailyCloseDate", type:"LABEL", value:"關帳日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.dailyCloseDate", type:"DATE", bind:"dailyCloseDate",mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.dailyBalanceDate", type:"LABEL", value:"結帳日期"}]},
				{items:[{name:"#F.dailyBalanceDate", type:"DATE", bind:"dailyBalanceDate"/*,mode:"READONLY"*/},
				{name:"#F.enforce", type:"XBOX" , bind:"enforce", mode:"HIDDEN" }]}
			]}
			//{row_style:"", cols:[
			//	{items:[{name:"#L.transCmAudit", type:"LABEL", value:"是否拋保稽"}]},
			//	{items:[{name:"#F.transCmAudit", type:"SELECT", bind:"transCmAudit", init:mode}]}
			//]}
		],
		beginService:"",
		closeService:""			
		});
	}else if(vat.bean().vatBeanOther.userType=="AUDIT"){
	var allCmAuditOrderTypeCode    = vat.bean("allCmAuditOrderTypeCode"); 
	var allCmAuditCustomsWarehouse    = vat.bean("allCmAuditCustomsWarehouse"); 
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"保稽維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL", value:"品牌<font color='red'>*</font>" }]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", mode:"READONLY"},
						{name:"#F.brandName", type:"TEXT", bind:"brandName", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customsWarehouseCode", type:"LABEL", value:"關別<font color='red'>*</font>" }]},
				{items:[{name:"#F.customsWarehouseCode", type:"SELECT", bind:"customsWarehouseCode",init:allCmAuditCustomsWarehouse}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別<font color='red'>*</font>" }]},
				{items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode",init:allCmAuditOrderTypeCode}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.dailyCloseDate", type:"LABEL", value:"日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.dailyCloseDate", type:"DATE", bind:"dailyCloseDate"}]}
			]},
			//{row_style:"", cols:[
			//	{items:[{name:"#L.dailyBalanceDate", type:"LABEL", value:"結帳日期"}]},
			//	{items:[{name:"#F.dailyBalanceDate", type:"DATE", bind:"dailyBalanceDate"}]}
			//]}
			{row_style:"", cols:[
				{items:[{name:"#L.transCmAudit", type:"LABEL", value:"是否轉保稽"}]},
				{items:[{name:"#F.transCmAudit", type:"SELECT", bind:"transCmAudit", init:mode},
						{name:"#F.enforce", type:"XBOX" , bind:"enforce", mode:"HIDDEN" }]}
			]}
		],
		beginService:"",
		closeService:""			
		});
	}
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
  //alert(formAction);
var transCmAudit = vat.item.getValueByName("#F.transCmAudit");
	var alertMessage ="是否確定送出?";
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction 	= formAction;
		vat.block.submit(function(){
				return "process_object_name=dailyCloseMainManager"+
				"&process_object_method_name=performTransaction";
			},{
				bind:true, link:true, other:true 
			}
		);
		vat.item.setStyleByName("#B.submit", 		"display", "none");
		//alert(transCmAudit);
		/*		if(transCmAudit=="Y"){
		vat.ajax.XHRequest({ 
				post:"process_object_name=dailyCloseMainManager&process_object_method_name=performAction"+
								"&transCmAudit="      + vat.item.getValueByName("#F.transCmAudit") +								
								"&brandCode="      + vat.item.getValueByName("#F.brandCode"),								
			          asyn:false,                      
				find: function doAfterPickerSupplierReturn(oXHR){
													
					}
			});
		}*/
	}
}

// 送出,暫存按鈕
/*function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	var strPath="";
	
	
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction 		= formAction;
	   
	    if (formAction=="SUBMIT"){
	    	strPath = "process_object_name=dailyBalanceManager"+
				"&process_object_method_name=performAction1";
	    }
	    else
	    {
	    	 strPath = "process_object_name=dailyCloseMainManager"+
				"&process_object_method_name=performCmTransaction";
	    }
	    else{
	    vat.ajax.XHRequest({ 
				post:"process_object_name=buSupplierModService&process_object_method_name=getAJAXFormDataBySupplier"+
								"&addressBookId="  + vat.bean().vatBeanPicker.result[i].addressBookId +
								"&organizationCode=TM"+
								"&brandCode="      + vat.item.getValueByName("#F.brandCode"),
								
			          asyn:false,                      
				find: function doAfterPickerSupplierReturn(oXHR){
									// imReceiveHead
							vat.item.setValueByName("#F.supplierCode",    vat.ajax.getValue("SupplierCode",    oXHR.responseText));
							vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    oXHR.responseText));
							vat.item.setValueByName("#F.supplierTypeCode",vat.ajax.getValue("supplierTypeCode",oXHR.responseText));

					}
			});
	    
	    }
	     alert(formAction);
	    
		vat.block.submit(function(){
				return strPath;
			},{
				bind:true, link:true, other:true 
			}
		);
	}
}*/

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





// 依formId鎖form
function doFormAccessControl(){
	var brandCode 		= vat.item.getValueByName("#F.brandCode");
	var formId 			= vat.bean().vatBeanOther.formId;
	var userType        = vat.bean("userType");
	//alert(userType);
	
       // 初始化
	//======================<header>=============================================
	if(userType == "CLOSE"){
		//vat.item.setGridAttributeByName("dailyBalanceDate", "disabled", true);
		
	}else if(userType == "BALANCE"){
		
	}else if(userType == "AUDIT"){
	
	} 
	
}





