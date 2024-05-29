/*** 
 *	檔案: imItemDiscouunt.js
 *	說明: 商品折扣維護作
 */
 
var vnB_Header = 1;


function outlineBlock(){
	searchInitial();
 	buttonLine();	
	headerInitial();
	parseURL();
}

// 搜尋初始化
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,
  	     sendBrandCode  	: document.forms[0]["#sendBrandCode" 		].value,
  	     vipTypeCode  		: document.forms[0]["#vipTypeCode" 			].value,
  	     isOppositePicker  	: document.forms[0]["#isOppositePicker" 	].value,
  	     itemDiscountType  	: document.forms[0]["#itemDiscountType" 	].value
  	     //vatPickerId        : document.forms[0]["#vatPickerId"       	].value
	    };
	    vat.bean.init(	
	  		function(){
				return "process_object_name=imItemDiscountAction&process_object_method_name=performInitial";
				  	},{								
	    		other: true
    	}); 
   		
  }
}

//可搜尋的欄位
//先以手動方式輸入資料
function headerInitial(){
	var allVipTypeCodes = vat.bean("allVipTypeCodes"); // 折扣卡別下拉選單，在 service.executeInitial() 中取得 DAO 的值  
	var allItemDiscountTypes = vat.bean("allItemDiscountTypes"); // 商品折扣類型下拉選單，在 service.executeInitial() 中取得 DAO 的值 
	var loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" 	].value;
	vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"商品折扣維護作業", rows:[  
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.brandCode", 	type:"LABEL", 	value:"商品折扣品牌"}]},	 
	 			{items:[{name:"#F.loginBrandCode", 	type:"TEXT",  	bind:"loginBrandCode", size:6, mode:"READONLY"}]},
				{items:[{name:"#L.vipTypeCode", 		type:"LABEL"  , value:"商品折扣卡別"}]},
				{items:[{name:"#F.vipTypeCode", 		type:"SELECT" , bind:"vipTypeCode", size:12, maxLen:12, init: allVipTypeCodes, onchange:"finditemdata()" }]},
				{items:[{name:"#L.itemDiscountType", 	type:"LABEL"  , value:"商品折扣類型"}]},
				{items:[{name:"#F.itemDiscountType", 	type:"SELECT" , bind:"itemDiscountCode", size:12, maxLen:12, init: allItemDiscountTypes}],td:"colSpan=3"},
				{items:[{name:"#L.createBy"				, type:"LABEL"	, value:"更新人員"}]},
				{items:[{name:"#F.createBy"				, type:"TEXT"	, bind:"createBy",init:loginEmployeeCode, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.discount", 	type:"LABEL", 	value:"折扣比率"}]},	 
	 			{items:[{name:"#F.discount", 	type:"TEXT",  	bind:"discount", size:6}]},
	 			{items:[{name:"#L.beginDate"			, type:"LABEL"	, value:"起始日期"}]},
	 			{items:[{name:"#F.beginDate"			, type:"DATE"	, bind:"beginDate"}]},  
				{items:[{name:"#L.endDate"				, type:"LABEL"	, value:"結束日期"}]},
				{items:[{name:"#F.endDate"				, type:"DATE"	, bind:"endDate"}]}	,
				{items:[{name:"#L.enable"				, type:"LABEL"	, value:"啟用"}]},
				{items:[{name:"#F.enable"				, type:"CHECKBOX"	, bind:"enable"}]}	,
				{items:[{name:"#L.createDate"				, type:"LABEL"	, value:"更新日期"}]},
				{items:[{name:"#F.createDate"				, type:"TEXT"	, bind:"createDate",mode:"READONLY"}]},
				{items:[{name:"#F.isUpdate"				, type:"TEXT"	, bind:"isUpdate",mode:"HIDDEN"}]}
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function buttonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[	
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit()'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
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


	
function doSubmit(){
	var alertMessage ="是否確定送出?";
	
	if(confirm(alertMessage)){
		vat.block.submit(function(){
				return "process_object_name=imItemDiscountAction"+
				"&process_object_method_name=performDiscountCreateTransaction";
			},{
				bind:true, link:true, other:true ,
				funcSuccess:function(){
				var check = vat.item.getValueByName("#F.isUpdate");
				if(check == ""){
					clearForm();
				}else{
					window.close();
				}
     	}
			}
		);
	}
}

/* 下拉撈資料 */
function finditemdata(){
			
	var processString = "process_object_name=ImItemDiscountService&process_object_method_name=getAJAXFindDataByItemDiscount" +
						"&loginBrandCode=T2" +
						"&vipTypeCode=" + vat.item.getValueByName("#F.vipTypeCode") + 
						"&itemDiscountCode=" + vat.item.getValueByName("#F.itemDiscountCode");
	vat.ajax.startRequest(processString,  function() { 
	  if (vat.ajax.handleState())
	  	vat.item.setValueByName("#F.discount", vat.ajax.getValue("discount", vat.ajax.xmlHttp.responseText));
	  	vat.item.setValueByName("#F.beginDate", vat.ajax.getValue("beginDate", vat.ajax.xmlHttp.responseText));
	  	vat.item.setValueByName("#F.endDate", vat.ajax.getValue("endDate", vat.ajax.xmlHttp.responseText));
		vat.item.setValueByName("#F.enable", vat.ajax.getValue("enable", vat.ajax.xmlHttp.responseText));
		vat.item.setValueByName("#F.createDate", vat.ajax.getValue("createDate", vat.ajax.xmlHttp.responseText));			
	 });
}

function clearForm(){
			vat.item.setValueByName("#F.vipTypeCode", "");
			vat.item.setValueByName("#F.itemDiscountType", "");
			vat.item.setValueByName("#F.discount", "");
			vat.item.setValueByName("#F.beginDate", "");
			vat.item.setValueByName("#F.endDate", "");
			vat.item.setValueByName("#F.enable", "");
}

function parseURL(){

	var strUrl = location.search;  
	var getPara, ParaVal;  
	var aryPara = [];
	var sendBrandCode ;
	var vipTypeCode;
  	var isOppositePicker;
  	var itemDiscountType; 
	if (strUrl.indexOf("?") != -1) {  
		var getSearch = strUrl.split("?");  
    getPara = getSearch[1].split("&");
    for (i = 0; i < getPara.length; i++) {  
      ParaVal = getPara[i].split("=");
      if(ParaVal[0] == "vipTypeCode"){
      		vat.item.setValueByName("#F.vipTypeCode", ParaVal[1]);
      		vat.item.setAttributeByName("#F.vipTypeCode", "readOnly", true);
      }else  if(ParaVal[0] == "itemDiscountType"){
      		vat.item.setValueByName("#F.itemDiscountType", ParaVal[1]);
      		vat.item.setAttributeByName("#F.itemDiscountType", "readOnly", true);
      }else  if(ParaVal[0] == "discount"){
      		vat.item.setValueByName("#F.discount", ParaVal[1]);
      }else  if(ParaVal[0] == "beginDate"){
      		vat.item.setValueByName("#F.beginDate", ParaVal[1]);
      }else  if(ParaVal[0] == "endDate"){
      		vat.item.setValueByName("#F.endDate", ParaVal[1]);
      }else  if(ParaVal[0] == "createdBy"){
      		vat.item.setValueByName("#F.createdBy", ParaVal[1]);
      }else  if(ParaVal[0] == "creationDate"){
      		vat.item.setValueByName("#F.creationDate", ParaVal[1]);
      }else  if(ParaVal[0] == "enable"){
      		vat.item.setValueByName("#F.enable", ParaVal[1]);
      }else if(ParaVal[0] == "isUpdate"){
      		vat.item.setValueByName("#F.isUpdate", ParaVal[1]);
      }
    }  
   }  

}