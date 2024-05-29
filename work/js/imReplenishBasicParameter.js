/*** 
 *	檔案: imReplenishBasicParameter.js 自動補貨基本參數
 *	說明：表單明細
 *	修改：david
 *  <pre>
 *  	Created by david
 *  	All rights reserved.
 *  </pre>
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

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	 	vat.bean().vatBeanOther = { 
        	loginBrandCode  	: document.forms[0]["#loginBrandCode" ].value,
        	loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
        	formId             	: document.forms[0]["#formId"            ].value,	
        	currentRecordNumber : 0,
	   		lastRecordNumber    : 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imReplenishAction&process_object_method_name=performBasicParameterInitial"; 
	    	},{
	    		other: true
    	});
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
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_Replenish:searchParameter:20110113.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

// 主檔
function headerInitial(){ 
	var allWarehouses = vat.bean("allWarehouses");
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"自動補貨基本參數維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.type", 		type:"LABEL", 	value:"參數類別<font color='red'>*</font>"}]},
				{items:[{name:"#F.type", 		type:"TEXT",	size:16, 	bind:"type", eChange:"changeIsExistence()"},
						{name:"#F.message", 	type:"TEXT",	size:16, 	bind:"message", back:false, mode:"READONLY"}]},	
			    {items:[{name:"#L.parameter",	type:"LABEL", 	value:"參數<font color='red'>*</font>" }]},
				{items:[{name:"#F.parameter",	type:"NUMM", 	bind:"parameter", eChange:"changeIsExistence()"}]}, 			
				{items:[{name:"#L.value",	type:"LABEL", 	value:"參數值<font color='red'>*</font>" }]},
				{items:[{name:"#F.value",	type:"NUMM", 	bind:"value"}]},
				{items:[{name:"#L.brandCode", 		type:"LABEL", 	value:"品牌"}]},	
				{items:[{name:"#F.brandCode", 		type:"TEXT", 	bind:"brandCode", mode:"HIDDEN" },
			            {name:"#F.brandName", 		type:"TEXT",	bind:"brandName", back:false, mode:"READONLY"}]}	 			        	 	
			]}	
		], 	
		beginService:"",
		closeService:""			
	});
}


// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.bean().vatBeanPicker.replenishResult = null;  
     	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
    	refreshForm("");
	 }
}

// 刷新頁面
function refreshForm(vsHeadId){   
	document.forms[0]["#formId"            ].value = vsHeadId;
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;
	
	if("" == vsHeadId){
		 vat.bean().vatBeanOther.type = "";
		 vat.bean().vatBeanOther.parameter = "";
	}
	
	vat.block.submit(
		function(){
				return "process_object_name=imReplenishAction&process_object_method_name=performBasicParameterInitial"; 
	   	},{ other: true, 
     		funcSuccess:function(){
     	   	vat.item.bindAll();
     	    
     	    doFormAccessControl();
     	  }}
    );
}

// picker 檢視回來作用的事件
function doAfterPickerProcess(){
	if(typeof vat.bean().vatBeanPicker.replenishResult != "undefined"){
		result = vat.bean().vatBeanPicker.replenishResult;
	    var vsMaxSize = result.length;
	    if( vsMaxSize === 0){
	  	vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		  
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;

		  vat.bean().vatBeanOther.type = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.type'];
		  vat.bean().vatBeanOther.parameter = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
		  vat.bean().vatBeanOther.formId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
		  
		  vat.item.setValueByName("#F.type", 		  vat.bean().vatBeanOther.type);
		  vat.item.setValueByName("#F.parameter"    , vat.bean().vatBeanOther.parameter);
		  
		  refreshForm();
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
		
	}
}	
	
	// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}
	if(confirm(alertMessage)){
	    var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		
				vat.bean().vatBeanOther.formAction = formAction;
					
						vat.block.submit(function(){
								return "process_object_name=imReplenishAction"+
				                    "&process_object_method_name=performBasicParameterTransaction";
				            },{
				            	bind:true, link:true, other:true
						    }
						);
					
	}
}


function gotoFirst(){
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	        vat.bean().vatBeanOther.type = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.type'];
		    vat.bean().vatBeanOther.parameter = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
		    vat.bean().vatBeanOther.formId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm();
	    }else{
	  	    alert("目前已在第一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoForward(){
    if(vat.bean().vatBeanOther.firstRecordNumber > 0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	     vat.bean().vatBeanOther.type = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.type'];
		    vat.bean().vatBeanOther.parameter = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
		    vat.bean().vatBeanOther.formId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
	  	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	    refreshForm();
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
	  	     vat.bean().vatBeanOther.type = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.type'];
		    vat.bean().vatBeanOther.parameter = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
		    vat.bean().vatBeanOther.formId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
	  	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	    refreshForm();
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
	        vat.bean().vatBeanOther.type = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.type'];
		    vat.bean().vatBeanOther.parameter = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
		    vat.bean().vatBeanOther.formId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.parameter'];
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm();
	    }else{
	  	    alert("目前已在最後一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
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

// 是否已存在定義檔
function changeIsExistence(){
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
 	var parameter = vat.item.getValueByName("#F.parameter");
 	var type = vat.item.getValueByName("#F.type");
 	
 	if("" != parameter && "" != type){
 		vat.ajax.XHRequest({
	        post:"process_object_name=imReplenishService"+
	                 "&process_object_method_name=getAJAXIsExistence"+
	                 "&brandCode=" + brandCode +
	                 "&type=" + type +
	                 "&parameter=" + parameter,
	        find: function changeRequestSuccess(oXHR){ 
	        	vat.item.setValueByName("#F.value", vat.ajax.getValue("value", oXHR.responseText));
	        	vat.item.setValueByName("#F.message", vat.ajax.getValue("message", oXHR.responseText));
	        }   
	    });
 	}else{
 		vat.item.setValueByName("#F.message", "");
 	}
}

// 欄位鎖定
function doFormAccessControl(match){
	var formId 	= vat.bean().vatBeanOther.formId;
	
  	// 初始化
 	vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true, true);
 	
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline"); 
 	if(formId != ""){
		vat.item.setStyleByName("#B.submit", 	"display", "inline"); 
		vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
		vat.item.setAttributeByName("#F.value","readOnly",false);
	}
}

// 送出後反回更新前端
function createRefreshForm(){
//	refreshForm("");
}
