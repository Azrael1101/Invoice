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

          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
        
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=posDUAction&process_object_method_name=performInitial"; 
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
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Bu_Country:search:20091117.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			
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
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){ 
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"AutoUD補送作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode"}]},
				{items:[{name:"#L.shopCode", type:"LABEL", value:"專櫃代碼<font color='red'>*</font>" }]},
				{items:[{name:"#F.shopCode"	, type:"SELECT"	, bind:"shopCode", eChange:"changeShopCode()"}]},  
				{items:[{name:"#L.store", type:"LABEL", value:"機台代碼<font color='red'>*</font>" }]},
				{items:[{name:"#F.store" , type:"SELECT"	, bind:"store"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.transactionNoStart", type:"LABEL", value:"交易序號<font color='red'>*</font>"}]},
				{items:[{name:"#F.transactionNoStart", type:"TEXT", bind:"transactionNoStart"},
						{name:"#L.transactionNoEnd", type:"LABEL", value:" 到 "},
						{name:"#F.transactionNoEnd", type:"TEXT", bind:"transactionNoEnd"}]},

				{items:[{name:"#L.transactionDate", type:"LABEL" , value:"交易日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.transactionDate", type:"DATE", bind:"transactionDate"}]}
			]}
			
		], 	
		beginService:"",
		closeService:function(){closeHeader();}		
	});
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
	
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction 		= formAction;
		vat.block.submit(function(){
				return "process_object_name=posDUAction"+
				"&process_object_method_name=performResend";
			},{
				bind:true, link:true, other:true 
			}
		);
	}
}

// 當picker按下檢視回來時,執行
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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].countryCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].countryCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].countryCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].countryCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].countryCode;
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
	document.forms[0]["#formId"            ].value = code; 
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=buBasicDataAction&process_object_method_name=performBuCountryInitial"; 
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
//	alert(typeof formId);
	if( formId != "" ){
		vat.item.setAttributeByName("#F.countryCode", "readOnly", true);
		
	}else{
		vat.item.setAttributeByName("#F.countryCode", "readOnly", false);
	}
	
	var enable = vat.item.getValueByName("#F.enable"); 
//	alert("enable = " + enable);
	if(enable == "Y" ){
		vat.item.setValueByName("#F.enable", "N");
	}else{
		vat.item.setValueByName("#F.enable", "Y");
	}
		
}
function changeShopCode(){
	var shopCode = vat.item.getValueByName("#F.shopCode").replace(/^\s+|\s+$/, '');
    vat.item.setValueByName("#F.shopCode", shopCode);
       vat.ajax.XHRequest(
       {
           post:"process_object_name=posDUService"+
                    "&process_object_method_name=getShopMachineForAJAX"+
                    "&shopCode=" + shopCode +
                    "&brandCode=" + document.forms[0]["#loginBrandCode" ].value,
           find: function changeShopCodeRequestSuccess(oXHR){ 
               	vat.item.SelectBind(eval(vat.ajax.getValue("allBuShopMachine", oXHR.responseText)),{ itemName : "#F.store" });
           }   
       });
}
function closeHeader(){
vat.ajax.XHRequest({ 
	post:"process_object_name=posDUService"+
          		"&process_object_method_name=findDownloadCommon"+
          		"&brandCode=" + document.forms[0]["#loginBrandCode" ].value,
          asyn:false,                      
	find: function change(oXHR){
		vat.item.SelectBind(eval(vat.ajax.getValue("allShop"			, oXHR.responseText)),{ itemName : "#F.shopCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allBuShopMachine"	, oXHR.responseText)),{ itemName : "#F.store" });
		vat.item.bindAll();
		}
	});
}