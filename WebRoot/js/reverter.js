vat.debug.disable();
var afterSavePageProcess = "";
/*20160707 Maco 開放商控使用促銷變價反確認
====category====
	user:使用者
	mis:資訊部
================
*/
var vnB_Button     = 0;
var vatBlock_Head  = 1;
var vatMasterDiv   = 2; 
var vatDetailDiv   = 3;
var vatAmountDiv   = 4;
var vatApprovalDiv = 5;

function outlineBlock(){
	formDataInitial();
	buttonLine(); 
	headerInitial();
	doFormAccessControl();
}


/* initial */
function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#formId"].value != '[binding]'){
        vat.bean().vatBeanOther = 
  	    {brandCode     		: document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode 		: document.forms[0]["#orderTypeCode"].value,
	     typeCode        	: document.forms[0]["#typeCode"].value,
	     category        	: document.forms[0]["#category"].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	    };    
        vat.bean.init(function(){
		return "process_object_name=reverterAction&process_object_method_name=performInitial"; 
        },{other: true});
    }
}

/* function button */
function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
	    
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"SPACE",            type:"LABEL", value:"　"},
	 	 		{name:"#B.exit",          type:"IMG",   value:"離開",    src:"./images/button_exit.gif",           eClick:'closeWindows("CONFIRM")'},
	 	 	  	{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.submit",        type:"IMG",   value:"送出",    src:"./images/button_submit.gif",         eClick:'doSubmit()'}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function headerInitial(){
	var allOrderType    = vat.bean("allOrderType");
	var allOrderTypes	= vat.bean("allOrderTypes");
	vat.block.create(vatBlock_Head, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"反轉維護作業", rows:[
		{row_style:"", cols:[
	 		{items:[{name:"#L.typeCode",			type:"LABEL",  value:"類別<font color='red'>*</font>"}]},	 
	 		{items:[{name:"#F.typeCode",			type:"SELECT", bind:"typeCode", init:allOrderType, eChange:"changeTypeCode()"}]},
	 		{items:[{name:"#L.brandCode",			type:"LABEL",  value:"品牌"}]},
	 		{items:[{name:"#F.brandCode",			type:"TEXT",   bind:"brandCode",  size:8,  mode:"HIDDEN"},
	 				{name:"#F.brandName",			type:"TEXT",   bind:"brandName",           mode:"READONLY"}]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.orderTypeCode",		type:"LABEL",  value:"單別<font color='red'>*</font>"}]},	 
	 		{items:[{name:"#F.orderTypeCode",		type:"SELECT", bind:"orderTypeCode", init:allOrderTypes, eChange:"changeOrderNo()"}]},		 
	 		{items:[{name:"#L.orderNo",				type:"LABEL",  value:"單號"}]},
			{items:[{name:"#F.orderNo",				type:"TEXT",   bind:"orderNo", size:20, eChange:"changeOrderNo()"},
					{name:"#F.headId",				type:"TEXT",   bind:"headId", size:20, mode:"READONLY"}]}]},
	  	{row_style:"", cols:[
	 		{items:[{name:"#L.lastUpdatedBy",		type:"LABEL",  value:"流程起始人"}]},	 
	 		{items:[{name:"#F.lastUpdatedBy",		type:"TEXT", bind:"lastUpdatedBy", eChange:"changeLastUpdatedBy()"},
	 				{name:"#F.lastUpdatedByName",	type:"TEXT", bind:"lastUpdatedByName", size:20, mode:"READONLY"}]},		 
	 		{items:[{name:"#L.status",				type:"LABEL",  value:"狀態"}]},
			{items:[{name:"#F.status",				type:"TEXT",   bind:"status", mode:"READONLY"},
					{name:"#F.statusName",			type:"TEXT",   bind:"statusName", mode:"READONLY"}]}]}
	  	],
	 beginService:"",
	 closeService:""			
	});
}

function changeTypeCode(){
	vat.ajax.XHRequest({ 
		post:"process_object_name=reverterService"+
	          		"&process_object_method_name=changeTypeCode"+
	          		"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	          		"&typeCode=" + vat.item.getValueByName("#F.typeCode"),
	          asyn:false,                      
		find: function change(oXHR){
				vat.item.SelectBind(eval(vat.ajax.getValue("allOrderTypes", oXHR.responseText)),{ itemName : "#F.orderTypeCode" });
				changeOrderNo();
			}
	});
}

function changeOrderNo(){
	vat.item.setValueByName("#F.headId", "");
	vat.item.setValueByName("#F.lastUpdatedBy", "");
	vat.item.setValueByName("#F.lastUpdatedByName", "");
	vat.item.setValueByName("#F.status", "");
	vat.item.setValueByName("#F.statusName", "");
	if(vat.item.getValueByName("#F.orderNo") != ""){
		vat.ajax.XHRequest({ 
			post:"process_object_name=reverterService"+
		          		"&process_object_method_name=changeOrderNo"+
		          		"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
		          		"&typeCode=" + vat.item.getValueByName("#F.typeCode")+
		          		"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")+
		          		"&orderNo=" + vat.item.getValueByName("#F.orderNo"),
		          asyn:false,                      
			find: function change(oXHR){
					var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
					if(errorMsg != "")
						alert(errorMsg);
					vat.item.setValueByName("#F.headId", vat.ajax.getValue("headId", oXHR.responseText));
					vat.item.setValueByName("#F.lastUpdatedBy", vat.ajax.getValue("lastUpdatedBy", oXHR.responseText));
					vat.item.setValueByName("#F.lastUpdatedByName", vat.ajax.getValue("lastUpdatedByName", oXHR.responseText));
					vat.item.setValueByName("#F.status", vat.ajax.getValue("formStatus", oXHR.responseText));
					vat.item.setValueByName("#F.statusName", vat.ajax.getValue("formStatusName", oXHR.responseText));
				}
		});
	}
	/*
	else{
		vat.item.setValueByName("#F.headId", "");
		vat.item.setValueByName("#F.lastUpdatedBy", "");
		vat.item.setValueByName("#F.lastUpdatedByName", "");
		vat.item.setValueByName("#F.status", "");
		vat.item.setValueByName("#F.statusName", "");
	}
	*/
}

function changeLastUpdatedBy(){
    vat.item.setValueByName("#F.lastUpdatedBy", vat.item.getValueByName("#F.lastUpdatedBy").replace(/^\s+|\s+$/, ''));
    if(vat.item.getValueByName("#F.lastUpdatedBy") !== ""){
		vat.ajax.XHRequest(
       	{
			post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vat.item.getValueByName("#F.lastUpdatedBy"),
			find: function changeSuperintendentRequestSuccess(oXHR){
           		vat.item.setValueByName("#F.lastUpdatedBy", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
           		vat.item.setValueByName("#F.lastUpdatedByName", vat.ajax.getValue("EmployeeName", oXHR.responseText)); 
			}   
		});
	}else{
        vat.item.setValueByName("#F.superintendentName", "");
	}
}

function doSubmit(){
    if(vat.item.getValueByName("#F.headId") == "") {
    	return;
    }
	var alertMessage ="是否確定送出?";
		if(confirm(alertMessage)){
			vat.block.submit(function(){return "process_object_name=reverterAction"+
		    "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
		}
}

function doFormAccessControl(){
		var typeCode = document.forms[0]["#typeCode"].value;
		var orderTypeCode = document.forms[0]["#orderTypeCode"].value;
		if(typeCode != ""){
          	vat.item.setAttributeByName("#F.typeCode", "readOnly", true);
          	if(orderTypeCode != ""){
				vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", true);
        	}	
        }
		
}