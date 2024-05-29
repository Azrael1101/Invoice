/*** 
 *	檔案: fiBudgetApply.js
 *	說明：採購預算申請
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Amount = 2;

function outlineBlock(){
	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","金額統計" ,"vatAmountDiv"        	,"images/tab_total_amount_dark.gif"      	,"images/tab_total_amount_light.gif", false);
	}
	amountInitial();
//	doFormAccessControl();
}

// 初始化
function formInitial(){  
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,	
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=fiBudgetHeadService&process_object_method_name=executeInitial"; 
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
	 	{items:[
	 	
	 	        //{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Fi_Budget:search:20091101.page",    // ?orderTypeCode="+vat.bean().vatBeanOther.orderTypeCode
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			
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

// 採購預算申請主檔
function headerInitial(){ 
	var itemTypes = vat.bean("itemTypes");
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"採購預算查詢作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", 		type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 		type:"TEXT",  	bind:"brandCode", size:6, mode:"HIDDEN"},
	 					{name:"#F.brandName", 		type:"TEXT",  	bind:"brandName", size:10, mode:"READONLY"},
	 					{name:"#F.headId", 			type:"TEXT",  	bind:"headId", back:false, mode:"HIDDEN"}]},
				{items:[{name:"#L.budgetYear", 		type:"LABEL", 	value:"預算年度"}]},	 
	 			{items:[{name:"#F.budgetYear", 		type:"TEXT",    bind:"budgetYear",	size:15 , mode:"READONLY"}]},
	 			{items:[{name:"#L.budgetMonth",		type:"LABEL", 	value:"月" }]},
				{items:[{name:"#F.budgetMonth",		type:"TEXT", 	bind:"budgetMonth" , size:20 , mode:"READONLY"}]}	 					
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.itemType", 			type:"LABEL", 	value:"業種"}]},
				{items:[{name:"#F.itemType", 			type:"SELECT", 	bind:"itemType", init:itemTypes,mode:"READONLY"}]},
				{items:[{name:"#L.totalBudget",		type:"LABEL", 	value:"總預算" }]},
				{items:[{name:"#F.totalBudget",		type:"TEXT", 	bind:"totalBudget" , size:20 , mode:"READONLY"}]},
				{items:[{name:"#L.totalForecastAmount", type:"LABEL", 	value:"總預測"}]},
				{items:[{name:"#F.totalForecastAmount", type:"TEXT", 	bind:"totalForecastAmount", mode:"READONLY"}]}				
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.lastUpdateBy",	type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.lastUpdateBy",	type:"TEXT", 	bind:"lastUpdatedBy", mode:"HIDDEN"},
						{name:"#F.lastUpdatedByName",		type:"TEXT", 	bind:"lastUpdatedByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.lastUpdateDate",	type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.lastUpdateDate",	type:"TEXT", 	bind:"lastUpdateDate", mode:"READONLY"}]}
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

function amountInitial(){
	
	var category01s = vat.bean("category01s");
	var category02s = vat.bean("category02s");
	var vbCanGridDelete = true; 
	var vbCanGridAppend = true; 
	var vbCanGridModify = true; 
	var showT2 = "READONLY";
	if("T2" != vat.item.getValueByName("#F.brandCode"))
	showT2 = "HIDDEN";
	vat.item.make(vnB_Amount, "indexNo"				, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_Amount, "itemBrandCode"		, {type:"TEXT" , size:10, desc:"商品品牌", mode:"HIDDEN"	});
	vat.item.make(vnB_Amount, "itemBrandName"		, {type:"TEXT" , size:10, desc:"商品品牌", mode:showT2	});
	vat.item.make(vnB_Amount, "categoryTypeCode1"	, {type:"SELECT" , size:10, desc:"大類", mode:"READONLY", init:category01s, mode:showT2});
	vat.item.make(vnB_Amount, "categoryTypeCode2"	, {type:"SELECT" , size:10, desc:"中類", mode:"READONLY", init:category02s, mode:showT2});
	vat.item.make(vnB_Amount, "budgetAmount"		, {type:"NUMM" , size:15, desc:"預算金額", mode:"READONLY"});
	vat.item.make(vnB_Amount, "forecastAmount"		, {type:"NUMM" , size:15, desc:"業績預測金額", mode:"READONLY"});
	vat.item.make(vnB_Amount, "signingAmount"		, {type:"NUMM" , size:15, desc:"簽核中金額", mode:"READONLY" });
	vat.item.make(vnB_Amount, "poActualAmount"		, {type:"NUMM" , size:15, desc:"採購已用金額", mode:"READONLY" });
	vat.item.make(vnB_Amount, "receiveActualAmount"	, {type:"NUMM" , size:15, desc:"進貨已用金額", mode:"READONLY" });
	vat.item.make(vnB_Amount, "adjustActualAmount"	, {type:"NUMM" , size:15, desc:"調整金額", mode:"READONLY" });
	vat.item.make(vnB_Amount, "poReturnAmount"		, {type:"NUMM" , size:15, desc:"採購退還金額", mode:"READONLY" });
	vat.item.make(vnB_Amount, "reserve1"			, {type:"TEXT" , size:10, desc:"備註", mode:"READONLY"});
	
	vat.block.pageLayout(vnB_Amount, {
														id: "vatAmountDiv", 
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "",//"appendBeforeService()",
														appendAfterService  : "",//"appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Amount, vnCurrentPage = 1);
}

// 第一次載入 重新整理
function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
		var processString = "process_object_name=fiBudgetHeadService&process_object_method_name=getAJAXLinePageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") ;
		return processString;	
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(){
   //alert("saveBeforeAjxService");
   
  		var processString = "process_object_name=fiBudgetHeadService&process_object_method_name=updateOrSaveAJAXPageLinesData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") ;
		                    //"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value;
		return processString;	
	
			
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(){
	//alert('saveSuccessAfter'); 
	//vat.block.pageRefresh(div); 
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
//    alert("loadSuccessAfter");	
//	vat.item.setGridAttributeByName("objectCode", "readOnly", true);
} 

// 新增空白頁
function appendBeforeService(){
//    alert("appendBeforeService");	 
	return true;
}    

// 新增空白頁成功後
function appendAfterService(){
//    alert("appendAfterService");	
} 

function eventService(){
//	alert("eventService");
} 
 

// tab切換 存檔
function doPageDataSave(){

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

function createRefreshForm(){
	refreshForm("");
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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
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


// 到第一筆
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

// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"            ].value = code; 
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = "";    
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=fiBudgetHeadService&process_object_method_name=executeInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				vat.block.pageDataLoad(vnB_Amount, vnCurrentPage = 1);
				//doFormAccessControl();
     	}});
 	
    
}
