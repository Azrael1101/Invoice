/*** 
 *	檔案: buGoalCommissionException.js
 *	說明: 類別代號,抽成率維護
 */
 

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
	//alert('123abc');
  	formInitial();
  	buttonLine();
  	headerInitial();
	//doFormAccessControl();
}

//初始化
function formInitial(){
 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
          vat.bean.init(  
             function(){
                    return "process_object_name=buGoalCommissionExceptionAction&process_object_method_name=performExInitial"; 
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
	 									 service:"Bu_Commission_Exception:search:20131105.page", 
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
		title:"例外抽成維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.commissionType", type:"LABEL", value:"商品類別<font color='red'>*</font>"}]},
				{items:[{name:"#F.commissionType", type:"TEXT", bind:"commissionType", size:25, maxLen:25 }]},
				{items:[{name:"#L.shopCode", type:"LABEL" , value:"店別<font color='red'>*</font>"}]},
				{items:[{name:"#F.shopCode", type:"TEXT", bind:"shopCode", size:25, maxLen:25 }]},
				{items:[{name:"#L.itemBrand", type:"LABEL" , value:"商品品牌"}]},
				{items:[{name:"#F.itemBrand", type:"TEXT", bind:"itemBrand", size:25, maxLen:25 }]},
				{items:[{name:"#L.commissionRate", type:"LABEL" , value:"抽成率<font color='red'>*</font>"}]},
				{items:[{name:"#F.commissionRate", type:"TEXT", bind:"commissionRate", size:25, maxLen:25 }]}
			]},
			{row_style:"", cols:[
			 {items:[{name:"#L.category01", type:"LABEL" , value:"分類一"}]},
			 {items:[{name:"#F.category01", type:"TEXT", bind:"category01", size:25, maxLen:25 }]},
    		 {items:[{name:"#L.category02", type:"LABEL" , value:"分類二(類別代號)"}]},
	 		 {items:[{name:"#F.category02", type:"TEXT", bind:"category02", size:25, maxLen:25 }]},
	 		 {items:[{name:"#L.category03", type:"LABEL" , value:"分類三"}]},
	 		 {items:[{name:"#F.category03", type:"TEXT", bind:"category03", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category04", type:"LABEL" , value:"分類四"}]},
			 {items:[{name:"#F.category04", type:"TEXT", bind:"category04", size:25, maxLen:25 }]}
			]},
			{row_style:"", cols:[
	 
			 {items:[{name:"#L.category05", type:"LABEL" , value:"分類五"}]},
			 {items:[{name:"#F.category05", type:"TEXT", bind:"category05", size:25, maxLen:25 }]},
    		 {items:[{name:"#L.category06", type:"LABEL" , value:"分類六"}]},
			 {items:[{name:"#F.category06", type:"TEXT", bind:"category06", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category07", type:"LABEL" , value:"分類七"}]},
			 {items:[{name:"#F.category07", type:"TEXT", bind:"category07", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category08", type:"LABEL" , value:"分類八(材質)"}]},
			 {items:[{name:"#F.category08", type:"TEXT", bind:"category08", size:25, maxLen:25 }]}
		   ]},
		   {row_style:"", cols:[
	 
			 {items:[{name:"#L.category09", type:"LABEL" , value:"分類九"}]},
			 {items:[{name:"#F.category09", type:"TEXT", bind:"category09", size:25, maxLen:25 }]},
   		     {items:[{name:"#L.category10", type:"LABEL" , value:"分類十"}]},
			 {items:[{name:"#F.category10", type:"TEXT", bind:"category10", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category11", type:"LABEL" , value:"分類十一"}]},
			 {items:[{name:"#F.category11", type:"TEXT", bind:"category11", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category12", type:"LABEL" , value:"分類十二"}]},
			 {items:[{name:"#F.category12", type:"TEXT", bind:"category12", size:25, maxLen:25 }]}
	 		]},
	 		{row_style:"", cols:[
	 
			 {items:[{name:"#L.category13", type:"LABEL" , value:"分類十三"}]},
			 {items:[{name:"#F.category13", type:"TEXT", bind:"category13", size:25, maxLen:25 }]},
    		 {items:[{name:"#L.category14", type:"LABEL" , value:"分類十四"}]},
			 {items:[{name:"#F.category14", type:"TEXT", bind:"category14", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category15", type:"LABEL" , value:"分類十五"}]},
			 {items:[{name:"#F.category15", type:"TEXT", bind:"category15", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category16", type:"LABEL" , value:"分類十六"}]},
			 {items:[{name:"#F.category16", type:"TEXT", bind:"category16", size:25, maxLen:25 }]}
		   ]},
		   {row_style:"", cols:[
	
			 {items:[{name:"#L.category17", type:"LABEL" , value:"分類十七"}]},
	   		 {items:[{name:"#F.category17", type:"TEXT", bind:"category17", size:25, maxLen:25 }]},
    		 {items:[{name:"#L.category18", type:"LABEL" , value:"分類十八"}]},
			 {items:[{name:"#F.category18", type:"TEXT", bind:"category18", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category19", type:"LABEL" , value:"分類十九"}]},
			 {items:[{name:"#F.category19", type:"TEXT", bind:"category19", size:25, maxLen:25 }]},
			 {items:[{name:"#L.category20", type:"LABEL" , value:"分類二十"}]},
			 {items:[{name:"#F.category20", type:"TEXT", bind:"category20", size:25, maxLen:25 }]}
	 		]}
	 	/*	
	 	,
		 {row_style:"", cols:[
		 
			 {items:[{name:"#L.lastUpdatedBy"  , type:"LABEL", value:"最近修改人員"}]},
			 {items:[{name:"#F.lastUpdatedBy"  , type:"TEXT",   bind:"lastUpdatedBy", mode:"READONLY", size:12},
	                 {name:"#F.lastUpdatedByName", type:"TEXT",   bind:"lastUpdatedByName", mode:"READONLY", size:12}]},
			 {items:[{name:"#L.lastUpdateDate", type:"LABEL", value:"最近修改日期"}]},
			 {items:[{name:"#F.lastUpdateDate", type:"TEXT",  bind:"lastUpdateDate" , size:15 , mode:"readOnly"  }]}
			 ]}*/
	 	
		], 	
		 
		beginService:"",
		closeService:""			
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
	 vat.item.setValueByName("#L.currentRecord", "0");
     vat.item.setValueByName("#L.maxRecord", "0");
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
	
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction = formAction;
		vat.block.submit
		(function(){
				return "process_object_name=buGoalCommissionExceptionAction" + "&process_object_method_name=performBuGoalCommissionExTransaction";
			},
			{bind:true, link:true, other:true}
		);
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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].lineId;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].lineId;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].lineId;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].lineId;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].lineId;
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
	document.forms[0]["#formId"].value = code; 
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;	
	vat.block.submit(
		function(){
			return "process_object_name=buGoalCommissionExceptionAction&process_object_method_name=performExInitial"; 
     	},{other: true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				doFormAccessControl();
     		}
     	}
    );	
}

// 依formId鎖form
function doFormAccessControl(){
/*
	var formId = vat.bean().vatBeanOther.formId;
//	alert(typeof formId);
	if( formId != "" ){
		vat.item.setAttributeByName("#F.commissionRate", "readOnly", true);
		
	}else{
		vat.item.setAttributeByName("#F.commissionRate", "readOnly", false);
	}
	
	var enable = vat.item.getValueByName("#F.enable"); 
//	alert("enable = " + enable);
	if(enable == "Y" ){
		vat.item.setValueByName("#F.enable", "N");
	}else{
		vat.item.setValueByName("#F.enable", "Y");
	}
*/		
}
 