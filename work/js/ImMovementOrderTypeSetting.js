/*** 
 *	檔案: ImMovementOrderTypeSetting.js
 *	說明: 調撥單檔設定
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
                    return "process_object_name=imMovementOrderTypeSettingAction&process_object_method_name=performInitial"; 
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
		title:"調撥單設定作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", type:"LABEL", value:"品牌/單別<font color='red'>*</font>"}]},
				{items:[{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", size:25, maxLen:25 }]},
					   
				{items:[{name:"#L.name", type:"LABEL" , value:"字彙值<font color='red'>*</font>"}]},
				{items:[{name:"#F.name", type:"TEXT", bind:"name", size:25, maxLen:25 }]},
				{items:[{name:"#L.deliveryWarehouses", type:"LABEL" , value:"轉出庫"}]},
				{items:[{name:"#F.deliveryWarehouses", type:"TEXT", bind:"deliveryWarehouses", size:25, maxLen:25 }]},
				{items:[{name:"#L.arrivalWarehouses", type:"LABEL" , value:"轉入庫"}]},
				{items:[{name:"#F.arrivalWarehouses", type:"TEXT", bind:"arrivalWarehouses", size:25, maxLen:25 }]}
			]},
			{row_style:"", cols:[
	 
			 {items:[{name:"#L.itemCategorymode", type:"LABEL" , value:"是否需業種子類"}]},
			 {items:[{name:"#F.itemCategorymode", type:"SELECT", bind:"itemCategorymode", size:25, maxLen:25 }]},
    		 {items:[{name:"#L.overOricount", type:"LABEL" , value:"實出量大於預出量"}]},
	 		 {items:[{name:"#F.overOricount", type:"SELECT", bind:"overOricount", size:25, maxLen:25 }]},
	 		 {items:[{name:"#L.typeOfIMV", type:"LABEL" , value:"調撥模式"}]},
	 		 {items:[{name:"#F.typeOfIMV", type:"SELECT", bind:"typeOfIMV", size:25, maxLen:25 }]},
			 {items:[{name:"#L.checkItemcategory", type:"LABEL" , value:"檢核業種"}]},
			 {items:[{name:"#F.checkItemcategory", type:"SELECT", bind:"checkItemcategory", size:25, maxLen:25 }]}
			]},
			{row_style:"", cols:[
	 
			 {items:[{name:"#L.bevoid", type:"LABEL" , value:"作廢模式"}]},
			 {items:[{name:"#F.bevoid", type:"SELECT", bind:"bevoid", size:25, maxLen:25 }]},
    		 {items:[{name:"#L.enable", type:"LABEL" , value:"啟用"}]},
			 {items:[{name:"#F.enable", type:"SELECT", bind:"enable", size:25, maxLen:25 }]},
			 {items:[{name:"#L.showOricount", type:"LABEL" , value:"呈現預出量"}]},
			 {items:[{name:"#F.showOricount", type:"SELECT", bind:"showOricount", size:25, maxLen:25 }]},
			 {items:[{name:"#L.createdBy", type:"LABEL" , value:"建立人員"}]},
			 {items:[{name:"#F.createdBy", type:"TEXT", bind:"createdBy", size:25, mode:"readOnly"  }]}
		   ]},
		   {row_style:"", cols:[
	 
			 {items:[{name:"#L.creationDate", type:"LABEL" , value:"建立日期"}]},
			 {items:[{name:"#F.creationDate", type:"TEXT", bind:"creationDate", size:25,mode:"readOnly"  }]},
   		     {items:[{name:"#L.lastUpdatedBy"  , type:"LABEL", value:"最近修改人員"}]},
			 {items:[{name:"#F.lastUpdatedBy"  , type:"TEXT", bind:"lastUpdatedBy", mode:"READONLY", size:12},
	                 {name:"#F.lastUpdatedByName", type:"TEXT", bind:"lastUpdatedByName", mode:"READONLY", size:12}]},
			 {items:[{name:"#L.lastUpdateDate", type:"LABEL", value:"最近修改日期"}]},
			 {items:[{name:"#F.lastUpdateDate", type:"DATE",bind:"lastUpdateDate", size:12, mode:"READONLY" }]},
			 {items:[{name:"#L.description", type:"LABEL", value:"說明", row:2, col: 30}], td:" rowSpan=2"},
			 {items:[{name:"#F.description", type:"TEXTAREA", bind:"description", size:50 , row:2, col: 35}], td:" rowSpan=2"}
	 		]}
		 
	 	
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
				return "process_object_name=imMovementOrderTypeSettingAction" + "&process_object_method_name=performTransaction";
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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].OrderTypeCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].OrderTypeCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].OrderTypeCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].OrderTypeCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].OrderTypeCode;
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
			return "process_object_name=imMovementOrderTypeSettingAction&process_object_method_name=performInitial"; 
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
 