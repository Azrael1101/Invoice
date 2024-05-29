/*** 
 *	檔案: imItemDiscouuntMod.js
 *	說明: 商品折扣維護作
 */
 
var vnB_Button = 0; 
var vnB_Header = 1;


function outlineBlock(){
	
	searchInitial();
 	buttonLine();	
	headerInitial();
	//parseURL();
}

// 搜尋初始化
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,  	     
  	     vipTypeCode  		: document.forms[0]["#vipTypeCode" 			].value,  	     
  	     itemDiscountType  	: document.forms[0]["#itemDiscountType" 	].value,
  	     formId           	: document.forms[0]["#formId"            	].value,
  	     currentRecordNumber : 0,
		 lastRecordNumber : 0
  	     
  	     };
	    vat.bean.init(	
	  		function(){
				return "process_object_name=imItemDiscountAction&process_object_method_name=performImItemDiscountformInitial";
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
	 			{items:[{name:"#L.orderType",          type:"LABEL",  value:"單別"}]},	 
	 			{items:[{name:"#F.orderTypeCode",      type:"TEXT", bind:"orderTypeCode",      mode:"READONLY"}]},		 
	 			{items:[{name:"#L.orderNo",            type:"LABEL",  value:"單號"}]},
				{items:[{name:"#F.orderNo",            type:"TEXT",   bind:"orderNo",    size:20, mode:"READONLY", back:false},
	 		 			{name:"#F.headId",             type:"TEXT",   bind:"headId",     size:10, mode:"READONLY",   back:false }],td:"colSpan=3"},
	 			{items:[{name:"#L.brandCode", 		   type:"LABEL", 	value:"商品折扣品牌"}]},	 
	 			{items:[{name:"#F.brandCode",          type:"TEXT",   bind:"brandCode",  size:8,  mode:"HIDDEN"},
	 					{name:"#F.brandName",          type:"TEXT",   bind:"brandName",           mode:"READONLY",  back:false}]},
				{items:[{name:"#L.Status",             type:"LABEL",  value:"狀態"}]},	
				{items:[{name:"#F.status",             type:"TEXT",   bind:"status",     size:12, mode:"HIDDEN"},
	  					{name:"#F.statusName",         type:"TEXT",   bind:"statusName",          mode:"READONLY",  back:false}]}	  			
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.vipTypeCode", 		type:"LABEL"  , value:"商品折扣卡別"}]},
				{items:[{name:"#F.vipTypeCode", 		type:"SELECT" , bind:"vipTypeCode", size:12, maxLen:12, init: allVipTypeCodes, onchange:"finditemdata()" }]},
				{items:[{name:"#L.itemDiscountCode", 	type:"LABEL"  , value:"商品折扣類型"}]},
				{items:[{name:"#F.itemDiscountCode", 	type:"SELECT" , bind:"itemDiscountType", size:12, maxLen:12, init: allItemDiscountTypes , onchange:"finditemdata()"}],td:"colSpan=5"},
				{items:[{name:"#L.createBy"				, type:"LABEL"	, value:"更新人員"}]},
				{items:[{name:"#F.lastUpdateBy",		type:"TEXT", 	bind:"lastUpdatedBy", mode:"HIDDEN"},
						{name:"#F.lastUpdatedByName",	type:"TEXT", 	bind:"lastUpdatedByName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.discount", 	type:"LABEL", 	value:"折扣比率"}]},	 
	 			{items:[{name:"#F.discount", 	type:"TEXT",  	bind:"discount", size:6 , maxLen:3}]},
	 			{items:[{name:"#L.beginDate"			, type:"LABEL"	, value:"起始日期"}]},
	 			{items:[{name:"#F.beginDate"			, type:"DATE"	, bind:"beginDate"}]},  
				{items:[{name:"#L.endDate"				, type:"LABEL"	, value:"結束日期"}]},
				{items:[{name:"#F.endDate"				, type:"DATE"	, bind:"endDate"}]}	,
				{items:[{name:"#L.enable"				, type:"LABEL"	, value:"啟用"}]},
				{items:[{name:"#F.enable"				, type:"CHECKBOX"	, bind:"enable"}]}	,
				{items:[{name:"#L.createDate"				, type:"LABEL"	, value:"更新日期"}]},
				{items:[{name:"#F.createDate"				, type:"TEXT"	, bind:"createDate",mode:"READONLY"}]}]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function buttonLine(){
	var vsMaxRecord = 0;
	var vsCurrentRecord = 0;
    vat.block.create(
    	vnB_Button, 
    	{
			id: "vatBlock_Button", 
			generate: true,
			table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
			title:"", 
			rows:[  
	 			{
	 				row_style:"", 
	 				cols:[
	 					{
	 						items:[	
	 							{
					 				name : "#B.search", type :"PICKER", value : "查詢", src : "./images/button_find.gif", openMode : "open",
					 				service : "Im_ItemDiscount:search:20160922.page", left : 0, right : 0, width : 1024, height : 768,	
					 				serviceAfterPick : function(){doAfterPickerProcess();}
					 			},
	 							{name : "SPACE"           , type:"LABEL"  ,value:"　"},
	 							{name : "#B.submit"       , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 	 						{name : "SPACE"           , type:"LABEL"  ,value:"　"},
	 	 						{name : "#B.exit"         , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 						{name : "SPACE"	  		  , type:"LABEL"  ,value:"　"},
					 			{name : "#B.first"		  , type:"IMG"	  ,value:"第一筆", src : "./images/play-first.png", eClick : "gotoFirst()"},
					 			{name : "SPACE"			  , type:"LABEL"  ,value:"　"},
					 			{name : "#B.forward"	  , type:"IMG"    ,value:"上一筆", src : "./images/play-back.png", eClick : "gotoForward()"},
					 			{name : "SPACE"   		  , type:"LABEL"  ,value:"　"},
					 			{name : "#B.next"		  , type:"IMG"    ,value:"下一筆",  src : "./images/play.png", eClick : "gotoNext()"},
					 			{name : "SPACE"    		  , type:"LABEL"  ,value:"　"},
					 			{name : "#B.last"		  , type:"IMG"	  ,value:"最後一筆", src : "./images/play-forward.png", eClick : "gotoLast()"},
					 			{name : "#L.currentRecord", type:"NUMB"   ,bind:vsCurrentRecord, size : 4, mode :"READONLY"},
					 			{name : "SPACE"			  , type:"LABEL"  ,value:" / "},
					 			{name : "#L.maxRecord"    , type:"NUMB"   ,bind:vsMaxRecord, size : 4, mode : "READONLY"}
	 	 					],
	 			  			td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
	 			  		}
	 			  ]
	 			 }
	  		], 	 
			beginService:"",
			closeService:""			
		}
	);
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


	
function doSubmit(formAction){

	var formId    = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var status    = vat.item.getValueByName("#F.status");
        
	var alertMessage ="是否確定送出?";
	var formStatus = status;
    if("SAVE" == formAction){
        formStatus = "SAVE";
    }else if("SUBMIT" == formAction){
        //formStatus = changeFormStatus(formId, processId, status, approvalResult);
    }else if("VOID" == formAction){
        formStatus = "VOID";
    }	
    var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.formAction = formAction;
		vat.bean().vatBeanOther.formStatus = formStatus;
		vat.block.submit(function(){
				return "process_object_name=imItemDiscountAction"+
				"&process_object_method_name=performModTransaction";
			},{
				bind:true, link:true, other:true 				
			}
		);
	}
}

/* 指定下一個狀態 */
function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == "" || status == "UNCONFIRMED"){
        formStatus = "SIGNING";
    }else if(processId != null && processId != "" && processId != 0){
    	if(status == "SAVE" || status == "REJECT"){
			formStatus = "SIGNING";
        }
        if( status == "REJECT"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }else if (status == "SIGNING"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }else{
        	formStatus = status;
        }
    }else if(status == "FINISH"){
    	formStatus = "FINISH";
    }else{
    	formStatus = "SIGNING";
    }
    return formStatus;
}

/* 下拉撈資料 */
function finditemdata(){
		//	alert("111");
	var processString = "process_object_name=imItemDiscountService&process_object_method_name=getAJAXFindDataByItemDiscount" +
						"&loginBrandCode=" + vat.item.getValueByName("#F.brandCode") +
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

// 送出的返回
function createRefreshForm(){
     vat.bean().vatBeanPicker.result = null; 
     vat.item.setValueByName("#L.currentRecord", "0");
     vat.item.setValueByName("#L.maxRecord", "0");
	 refreshForm("");
}


// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"            ].value = code; 
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=imItemDiscountAction&process_object_method_name=performImItemDiscountformInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				//doFormAccessControl();權限控制
     	}}
    );	
}


// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result != null){
		var vsMaxSize = vat.bean().vatBeanPicker.result.length;
		if(vsMaxSize === 0){
			vat.bean().vatBeanOther.firstRecordNumber = 0;
			vat.bean().vatBeanOther.lastRecordNumberm= 0;
			vat.bean().vatBeanOther.currentRecordNumber = 0;
		}
		else{
			vat.bean().vatBeanOther.firstRecordNumber = 1;
			vat.bean().vatBeanOther.lastRecordNumber = vsMaxSize ;
			vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
			var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber - 1].headId;
			refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord", vat.bean().vatBeanOther.lastRecordNumber);
	}
}

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}
/*
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
*/