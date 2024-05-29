/*** 
 *	檔案: singlePostingTally.js
 *	說明：POS單筆過帳作業
 *	修改：joey
 *  <pre>
 *  	Created by Joey
 *  	All rights reserved.
 *  </pre>
 */



vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
  	formDataInitial();
	kweButtonLine();
  	headerInitial();
}

function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = 
		{
			loginBrandCode		:	document.forms[0]["#loginBrandCode" ].value,
			loginEmployeeCode	:	document.forms[0]["#loginEmployeeCode" ].value
    	}; 
	    
		vat.bean.init(function(){
			return "process_object_name=posDUAction&process_object_method_name=performInitial"; 
		},{other: true});
		vat.item.bindAll();
	}
}

function kweButtonLine(){
	vat.block.create(vnB_Button, {id: "vatBlock_Button", generate: true,	
	title:"", rows:[	 
	{row_style:"", cols:[
	 	{items:[	 	        
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("FINISH")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'}],
	 				td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	 			//{name:"SPACE"       	, type:"LABEL"  ,value:"　"}		,
				//{name:"#B.message"  	, type:"IMG"    ,value:"訊息提示"	,   src:"./images/button_message_prompt.gif", eClick:'showMessage()'}
	 	],
		beginService:"",
		closeService:""			
	});
}

function headerInitial(){ 	
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"POS資料上傳作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.uploadFunction"		, type:"LABEL"	, value:"上傳類別"}]},
	 			{items:[{name:"#F.uploadFunction"		, type:"SELECT"	, bind:"uploadFunction"}]},
	 			{items:[{name:"#L.brandCode"			, type:"LABEL"	, value:"品牌"}]},	
				{items:[{name:"#F.brandCode"			, type:"TEXT"	, bind:"brandCode", mode:"HIDDEN" },
			            {name:"#F.brandName"			, type:"TEXT"	, bind:"brandName", mode:"READONLY"}]},
			            {items:[{name:"#L.createdBy"			, type:"LABEL", value:"下傳人員"}]},
				{items:[{name:"#F.createdBy"			, type:"TEXT", bind:"createdBy", mode:"HIDDEN", size:6},
						{name:"#F.createdByName"		, type:"TEXT", bind:"createdByName", mode:"READONLY", size:6}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.posMachineCode"		, type:"LABEL"	, value:"機台號碼"}]},
				{items:[{name:"#F.posMachineCode"		, type:"SELECT"	, bind:"posMachineCode"}]},
				{items:[{name:"#L.transactionSeqNo"		, type:"LABEL"	, value:"交易序號(TD)"}]},
	 			{items:[{name:"#F.transactionSeqNo"		, type:"TEXT"	, bind:"transactionSeqNo", size:20}], td:" colSpan=3"}
			]}
		], 	
		beginService:"",
		closeService:function(){closeHeader();}	
	});	  	
}

function closeHeader(){
vat.ajax.XHRequest({ 
	post:"process_object_name=posDUService"+
          		"&process_object_method_name=findUploadCommon"+
          		"&brandCode=" + document.forms[0]["#loginBrandCode" ].value,
          asyn:false,                      
	find: function change(oXHR){
		vat.item.SelectBind(eval(vat.ajax.getValue("allUploadFunction", oXHR.responseText)),{ itemName : "#F.uploadFunction" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allBuShopMachine"	, oXHR.responseText)),{ itemName : "#F.posMachineCode" });
		vat.item.bindAll();
		}
	});
}

	// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
		
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.formAction = formAction;
	    vat.block.submit(function(){return "process_object_name=posDUAction"+
			"&process_object_method_name=performUploadTransaction";}, {bind:true, link:true, other:true
			, funcSuccess:function(){//vat.block.resetForm();
			}});
	}	
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

