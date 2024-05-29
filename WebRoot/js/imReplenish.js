/*** 
 *	檔案: imReplenish.js 自動補貨
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
var vnB_DetailCalendar = 2;
var vnB_DetailDisplay = 3;
var vnB_DetailLimition = 4;


var activeTab = 2;

function outlineBlock(){

  	formInitial();
	buttonLine();
 	headerInitial();

	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0,"xTab1","日曆明細資料"   ,"vatDetailCalendarDiv"                   ,"images/tab_calendar_dark.gif"            ,"images/tab_calendar_light.gif" , false, "doPageRefresh("+vnB_DetailCalendar+")");
		vat.tabm.createButton(0,"xTab2","展示明細資料"   ,"vatDetailDisplayDiv"                   ,"images/tab_display_dark.gif"            ,"images/tab_display_light.gif" , false, "doPageRefresh("+vnB_DetailDisplay+")");
		vat.tabm.createButton(0,"xTab3","限制明細資料"   ,"vatDetailLimitionDiv"                   ,"images/tab_limititem_dark.gif"            ,"images/tab_limititem_light.gif" , false, "doPageRefresh("+vnB_DetailLimition+")");
		
	}
  	
  	detailCalendarInitial();  // Calendar
 	detailDisplayInitial();   // Display
  	detailLimitionInitial();  // Limition
  	
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
				return "process_object_name=imReplenishAction&process_object_method_name=performInitial"; 
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
	 									 service:"Im_Replenish:search:20100706.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 servicePassData:function(){ return doPassData("buttonLine");},
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 	 		{name:"#B.match"       , type:"IMG"    ,value:"匹配",   src:"./images/button_lock_data.gif", eClick:'doMatch()'},
	 			{name:"#B.unMatch"     , type:"IMG"    ,value:"解除匹配",   src:"./images/button_unlock_data.gif", eClick:'doUnMatch()'},
	 	 		{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
	 	 		{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
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
		title:"自動補貨維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseCode", 	type:"LABEL", 	value:"庫別<font color='red'>*</font>"}]},
				{items:[{name:"#F.warehouseCode", 	type:"SELECT",	size:16, 	bind:"warehouseCode", init:allWarehouses }]},
				{items:[{name:"#L.warehouseArea", 		type:"LABEL", 	value:"倉庫區域<font color='red'>*</font>"}]},
				{items:[{name:"#F.warehouseArea", 		type:"TEXT",	size:16, 	bind:"warehouseArea"},
						{name:"#F.headId", 			type:"TEXT"  ,  bind:"headId", back:false, mode:"HIDDEN"}]},	
			    {items:[{name:"#L.brandCode", 		type:"LABEL", 	value:"品牌"}]},	
				{items:[{name:"#F.brandCode", 		type:"TEXT", 	bind:"brandCode", mode:"HIDDEN" },
			            {name:"#F.brandName", 		type:"TEXT",	bind:"brandName", back:false, mode:"READONLY"}]}		 	
			]},
			{row_style:"", cols:[
			    {items:[{name:"#L.createBy",		type:"LABEL", 	value:"建檔人員" }]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",		type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
			    {items:[{name:"#L.creationDate",	type:"LABEL", 	value:"建檔日期" }]},
				{items:[{name:"#F.creationDate",	type:"TEXT", 	bind:"creationDate", mode:"READONLY"}],td:" colSpan=3"} 			
			]}	
		], 	
		beginService:"",
		closeService:""			
	});
}

//	日曆明細資料
function detailCalendarInitial(){
	vat.item.make(vnB_DetailCalendar, "indexNo"  			, {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_DetailCalendar, "replenishDate"		, {type:"DATE" , bind:"replenishDate", size:1, desc:"日期" });
	vat.item.make(vnB_DetailCalendar, "replenishType"		, {type:"TEXT" , bind:"replenishType",size:10, desc:"日期類別"});
	vat.item.make(vnB_DetailCalendar, "lowStockQuantity"	, {type:"NUMM" , bind:"lowStockQuantity",size:10, desc:"最低配貨量"});
	vat.item.make(vnB_DetailCalendar, "highStockQuantity"	, {type:"NUMM" , bind:"highStockQuantity",size:10, desc:"最高配貨量"});
	
	vat.item.make(vnB_DetailCalendar, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_DetailCalendar, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_DetailCalendar, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_DetailCalendar, "message"         , {type:"MSG"  , desc:"訊息"});
	
	vat.block.pageLayout(vnB_DetailCalendar, {
		id: "vatDetailCalendarDiv",
		pageSize: 10,											
		canGridDelete : true,
		canGridAppend : true,
		canGridModify : true,						
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_DetailCalendar+")",
		loadSuccessAfter    : "loadSuccessAfter()",						
		eventService        : "eventService()",   
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_DetailCalendar+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_DetailCalendar+")"
	});
	vat.block.pageDataLoad(vnB_DetailCalendar, vnCurrentPage = 1);
}	

// 陳列明細
function detailDisplayInitial(){
    
    vat.item.make(vnB_DetailDisplay, "indexNo"  		, {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_DetailDisplay, "displayItemCode"	, {type:"TEXT" , bind:"itemCode", size:13, maxLen:13, desc:"品號", eChange:"changeItemCode('display')" });
	vat.item.make(vnB_DetailDisplay, "displayItemCName"	, {type:"TEXT" , bind:"itemCName",size:10, desc:"品名",mode:"READONLY", alter:true});
	vat.item.make(vnB_DetailDisplay, "displayQuantity"	, {type:"NUMM" , bind:"displayQuantity",size:10, desc:"陳列數量"});
	
	vat.item.make(vnB_DetailDisplay, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_DetailDisplay, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_DetailDisplay, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_DetailDisplay, "message"         , {type:"MSG"  , desc:"訊息"});
	
	vat.block.pageLayout(vnB_DetailDisplay, {
		id: "vatDetailDisplayDiv",
		pageSize: 10,											
		canGridDelete : true,
		canGridAppend : true,
		canGridModify : true,						
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_DetailDisplay+")",
		loadSuccessAfter    : "loadSuccessAfter()",						
		eventService        : "eventService()",   
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_DetailDisplay+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_DetailDisplay+")"
	});
	vat.block.pageDataLoad(vnB_DetailDisplay, vnCurrentPage = 1);
}

// 限制明細資料
function detailLimitionInitial(){
	vat.item.make(vnB_DetailLimition, "indexNo"  		, {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_DetailLimition, "limitionItemCode"	, {type:"TEXT" , bind:"itemCode", size:13, maxLen:13, desc:"品號", eChange:"changeItemCode('Limition')"});
	vat.item.make(vnB_DetailLimition, "limitionItemCName"	, {type:"TEXT" , bind:"itemCName",size:10, desc:"品名",mode:"READONLY", alter:true});
//	vat.item.make(vnB_DetailLimition, "category00"		, {type:"TEXT" , bind:"category00",size:10, desc:"商品類別",mode:"HIDDEN"});
//	vat.item.make(vnB_DetailLimition, "category00Name"	, {type:"TEXT" , bind:"category00Name",size:10, desc:"商品類別名稱",mode:"HIDDEN"});
//	vat.item.make(vnB_DetailLimition, "category01"		, {type:"TEXT" , bind:"category01",size:10, desc:"大類",mode:"HIDDEN"});
//	vat.item.make(vnB_DetailLimition, "category01Name"	, {type:"TEXT" , bind:"category01Name",size:10, desc:"大類名稱",mode:"HIDDEN"});
//	vat.item.make(vnB_DetailLimition, "category02"		, {type:"TEXT" , bind:"category02",size:10, desc:"中類",mode:"HIDDEN"});
//	vat.item.make(vnB_DetailLimition, "category02Name"	, {type:"TEXT" , bind:"category02Name",size:10, desc:"中類名稱",mode:"HIDDEN"});
	vat.item.make(vnB_DetailLimition, "limitionItemBrand"	, {type:"TEXT" , bind:"itemBrand",size:10, desc:"商品品牌", eChange: function(){ changeCategoryCodeName("ItemBrand"); }});
	vat.item.make(vnB_DetailLimition, "reserve1"		, {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 		           service:"Im_Item:searchCategory:20100119.page",
	 									 			       left:0, right:0, width:1024, height:768,	
	 									 				   servicePassData:function(x){ return doPassData( "itemBrand", x ); },	
	 									 		           serviceAfterPick:function(id){doAfterPickerFunctionProcess("itemBrand",id); } });
	vat.item.make(vnB_DetailLimition, "limitionItemBrandName"	, {type:"TEXT" , bind:"itemBrandName",size:10, desc:"商品品牌名稱",mode:"READONLY"});
	
	vat.item.make(vnB_DetailLimition, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_DetailLimition, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_DetailLimition, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_DetailLimition, "message"         , {type:"MSG"  , desc:"訊息"});
	
	vat.block.pageLayout(vnB_DetailLimition, {
		id: "vatDetailLimitionDiv",
		pageSize: 10,											
		canGridDelete : true,
		canGridAppend : true,
		canGridModify : true,						
		appendBeforeService : "appendBeforeService()",
		appendAfterService  : "appendAfterService()",
		loadBeforeAjxService: "loadBeforeAjxService("+vnB_DetailLimition+")",
		loadSuccessAfter    : "loadSuccessAfter()",						
		eventService        : "eventService()",   
		saveBeforeAjxService: "saveBeforeAjxService("+vnB_DetailLimition+")",
		saveSuccessAfter    : "saveSuccessAfter("+vnB_DetailLimition+")"
	});
	vat.block.pageDataLoad(vnB_DetailLimition, vnCurrentPage = 1);
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
	vat.block.submit(
		function(){
				return "process_object_name=imReplenishAction&process_object_method_name=performInitial"; 
	   	},{ other: true, 
     		funcSuccess:function(){
     	   	vat.item.bindAll();
     	   	
     	    vat.block.pageDataLoad(vnB_DetailCalendar, vnCurrentPage = 1);
			vat.block.pageDataLoad(vnB_DetailDisplay, vnCurrentPage = 1);
			vat.block.pageDataLoad(vnB_DetailLimition, vnCurrentPage = 1);
					
			
     	    
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
		  vsHeadId = result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(vsHeadId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
		
	}
}	
	
// 傳參數
function doPassData(id, x){
	var suffix = "";
	switch(id){
		case "buttonLine":
			break;
		case "itemBrand":
			var vLineId	= vat.item.getGridLine(x);
			suffix += "&categoryType="+escape("ItemBrand")+"&categoryCode="+escape(vat.item.getGridValueByName("limitionItemBrand", vLineId));
			break;	
	}
	return suffix;
}				
	
// picker 回來執行
function doAfterPickerFunctionProcess(key, id){
	switch(key){
		case "itemBrand":
			if(typeof vat.bean().vatBeanPicker.categoryResult != "undefined"){
				var vLineId	= vat.item.getGridLine(id);
		    	vat.item.setGridValueByName("limitionItemBrand", vLineId, vat.bean().vatBeanPicker.categoryResult[0]["id.categoryCode"]); 
				changeCategoryCodeName("ItemBrand", vLineId);
			}
		break;
	}
}
	
function doPageRefresh(div){
	activeTab = div;
}
	
// 第一次載入 重新整理
function loadBeforeAjxService(div){
	var	processString = "process_object_name=imReplenishService&process_object_method_name=getAJAXPageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId")+
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
		                    "&div=" + div;
		return processString;				
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
	var processString = "process_object_name=imReplenishService&process_object_method_name=updateAJAXPageLinesData" + 
			"&headId=" + vat.item.getValueByName("#F.headId") + 
			"&brandCode=" +  vat.bean().vatBeanOther.loginBrandCode + 
			"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value +
			"&div=" + div;
			
		return processString;
}   

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){ 
//	     vat.block.pageRefresh(div);
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
    vat.block.pageRefresh(0);
} 

// 新增空白頁
function appendBeforeService(){
	return true;
}    

// 新增空白頁成功後
function appendAfterService(){
} 

// 自定事件
function eventService(){
} 
	
	
	// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}
	if(confirm(alertMessage)){
	    var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		
			vat.block.pageDataSave(activeTab, {  funcSuccess:function(){
				vat.bean().vatBeanOther.formAction = formAction;
					
						vat.block.submit(function(){
								return "process_object_name=imReplenishAction"+
				                    "&process_object_method_name=performTransaction";
				            },{
				            	bind:true, link:true, other:true,
				            	funcSuccess:function(){
						        	vat.block.pageRefresh(activeTab);
						    	}
						    }
						);
					
			}});	      
	}
}

// 尋找匹配
function doMatch(){
	var warehouseCode = vat.item.getValueByName("#F.warehouseCode");
	var warehouseArea = vat.item.getValueByName("#F.warehouseArea");
	if(warehouseCode == ""){
		alert('庫別不可為空');
	}else{
		vat.ajax.XHRequest({
			post:"process_object_name=imReplenishService"+
		                    "&process_object_method_name=findMatch"+
		                    "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
		                    "&warehouseCode=" + warehouseCode +
		                    "&warehouseArea=" + warehouseArea,
			find: function loadBudgetSuccess(oXHR){
			        var isNew = vat.ajax.getValue("isNew", oXHR.responseText);
			         	if( isNew == "Y" ){
			         		if(!confirm("是否要新增?")){
			         			return;
			         		}
			         	}
			         		vat.ajax.XHRequest({
						        post:"process_object_name=imReplenishService"+
						                    "&process_object_method_name=executeMatch"+
						                    "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
						                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
						                    "&warehouseCode=" + warehouseCode +
						                    "&warehouseArea=" + warehouseArea,
						        find: function loadBudgetSuccess(oXHR){
							        vat.item.setValueByName("#F.headId" ,vat.ajax.getValue("headId", oXHR.responseText));
							        vat.block.pageRefresh(vnB_DetailCalendar);
							        vat.block.pageRefresh(vnB_DetailDisplay);
							        vat.block.pageRefresh(vnB_DetailLimition);
							  		doFormAccessControl("match");
						       }   
							})
			}   
		})
	}
}


// 解除匹配
function doUnMatch(){
	// 清除
	vat.bean().vatBeanOther.formId	= "";
	document.forms[0]["#formId"].value	= "";
	vat.item.setValueByName("#F.headId", "");
	
	// 初始化筆數
	vat.item.setValueByName("#L.currentRecord", "0");
	vat.item.setValueByName("#L.maxRecord"    , "0");
    vat.bean().vatBeanPicker.replenishResult = null;
   
 	vat.block.pageRefresh(vnB_DetailCalendar);
    vat.block.pageRefresh(vnB_DetailDisplay); 				
    vat.block.pageRefresh(vnB_DetailLimition); 	
    				
    doFormAccessControl();
}

function gotoFirst(){
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	        vsHeadId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm(vsHeadId);
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
	  	    vsHeadId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	    refreshForm(vsHeadId);
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
	  	    vsHeadId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	    refreshForm(vsHeadId);
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
	        vsHeadId = vat.bean().vatBeanPicker.replenishResult[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm(vsHeadId);
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

// 匯入
function importFormData(){
	var importBeanName, gridFieldName;
	if(activeTab == vnB_DetailCalendar){
		importBeanName = "REPLENISH_CALENDAR";
		gridFieldName = "executeImportCalendarLists";
	}else if(activeTab == vnB_DetailDisplay){
		importBeanName = "REPLENISH_DISPLAY";
		gridFieldName = "executeImportDisplayLists";
	}else if(activeTab == vnB_DetailLimition){
		importBeanName = "REPLENISH_DIMITION";
		gridFieldName = "executeImportLimitionLists";
	}

	var width = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
    
    	var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=" + importBeanName +
			"&importFileType=XLS" +
	        "&processObjectName=imReplenishService" + 
	        "&processObjectMethodName=" + gridFieldName +
	        "&arguments=" + headId +
	        "&parameterTypes=LONG" +
	        "&blockId=" + activeTab,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 匯出
function exportFormData(){
	var exportBeanName, gridFieldName;
	if(activeTab == vnB_DetailCalendar){
		exportBeanName = "REPLENISH_CALENDAR";
		gridFieldName = "imReplenishCalendarLines";
	}else if(activeTab == vnB_DetailDisplay){
		exportBeanName = "REPLENISH_DISPLAY";
		gridFieldName = "imReplenishDisplayLines";
	}else if(activeTab == vnB_DetailLimition){
		exportBeanName = "REPLENISH_DIMITION";
		gridFieldName = "imReplenishLimitionLines";
	}
	
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=" + exportBeanName +
              "&fileType=XLS" + 
              "&processObjectName=imReplenishService" + 
              "&processObjectMethodName=executeFind" + 
              "&gridFieldName=" + gridFieldName + 
              "&arguments=" + vat.item.getValueByName("#F.headId") + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '自動補貨匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 明細匯入
function importFormData(){
	var importBeanName, processObjectMethodName;
	if(activeTab == vnB_DetailCalendar){
		importBeanName = "REPLENISH_CALENDAR";
		processObjectMethodName = "executeImportCalendarLists";
	}else if(activeTab == vnB_DetailDisplay){
		importBeanName = "REPLENISH_DISPLAY";
		processObjectMethodName = "executeImportDisplayLists";
	}else if(activeTab == vnB_DetailLimition){
		importBeanName = "REPLENISH_DIMITION";
		processObjectMethodName = "executeImportLimitionLists";
	}

	var width = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=" + importBeanName +
		"&importFileType=XLS" +
        "&processObjectName=imReplenishService" + 
        "&processObjectMethodName=" + processObjectMethodName + 
        "&arguments=" + headId +
        "&parameterTypes=LONG" +
        "&blockId=" + activeTab,
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 欄位鎖定
function doFormAccessControl(match){
	var formId 	= vat.bean().vatBeanOther.formId;
	
  	// 初始化
 	vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true, true);
 	vat.block.canGridModify([vnB_DetailCalendar, vnB_DetailDisplay, vnB_DetailLimition], false,true,true);
 	
	vat.item.setStyleByName("#B.new", 		"display", "none");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "none"); 
	vat.item.setStyleByName("#B.unMatch", 	"display", "none");
	vat.item.setStyleByName("#B.match", 	"display", "inline");
	
 	if(formId != ""){// 查詢回來
		vat.item.setStyleByName("#B.submit", 	"display", "none"); 
		vat.item.setStyleByName("#B.save", 		"display", "none"); 
		vat.item.setStyleByName("#B.import", 	"display", "inline"); 
		vat.item.setStyleByName("#B.match", 	"display", "none");
		vat.item.setStyleByName("#B.unMatch", 	"display", "inline");
		vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
		vat.block.canGridModify([vnB_DetailCalendar, vnB_DetailDisplay, vnB_DetailLimition], true,true,true);
	}else{
		if( match == "match" ){	// 匹配
			//======================<header>=============================================
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			//=======================<buttonLine>========================================
			vat.item.setStyleByName("#B.submit", 	"display", "inline"); 
			vat.item.setStyleByName("#B.unMatch"	, "display", "inline");
		  	vat.item.setStyleByName("#B.match"	 	, "display", "none");
		  	vat.item.setStyleByName("#B.import", 	"display", "inline");
			vat.item.setStyleByName("#B.export", 	"display", "inline");
			//=======================<detail>========================================
			vat.block.canGridModify([vnB_DetailCalendar, vnB_DetailDisplay, vnB_DetailLimition], true,true,true);
		} 
	}
 		
}


// 動態撈出品號
function changeItemCode(detail){
	var nItemLine = vat.item.getGridLine();
	var sItemCode = vat.item.getGridValueByName(detail+"ItemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	
	var processString = "process_object_name=imItemService&process_object_method_name=getAJAXItemCode"+ 
						"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
						"&itemCode=" + sItemCode + 
						"&priceType=1";
	vat.ajax.XHRequest({	
		post:processString,					 	
		find: function change(oXHR){ 
			vat.item.setGridValueByName(detail+"ItemCode",nItemLine, vat.ajax.getValue("itemCode",oXHR.responseText));
			vat.item.setGridValueByName(detail+"ItemCName",nItemLine, vat.ajax.getValue("itemCName",oXHR.responseText));
		}
	});						

	vat.item.setGridValueByName(detail+"ItemBrand", nItemLine, "");
	vat.item.setGridValueByName(detail+"ItemBrandName", nItemLine, "");
}

// 動態改變商品類別名稱
function changeCategoryCodeName(code, vLineId){
	var condition = "", name ="";
	var nItemLine = typeof vLineId != "undefined" ? vLineId : vat.item.getGridLine();
	var sItemBrand = vat.item.getGridValueByName("limitionItemBrand",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setGridValueByName("limitionItemBrand", nItemLine, sItemBrand);
	
	switch(code){
		case "ItemBrand":
			condition =  "&categoryCode=" + ( "ItemBrand" === code ? sItemBrand : "" );
			name = "limitionItemBrandName";
		break;
	}
	
	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXCategoryName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&categoryType=" + code + condition,
                  
		find: function change(oXHR){ 
         		vat.item.setGridValueByName(name, nItemLine, vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		vat.item.setGridValueByName(name, nItemLine, "查無此類別");
		}   
	});
	
	vat.item.setGridValueByName("limitionItemCode", nItemLine, "");
	vat.item.setGridValueByName("limitionItemCName", nItemLine, "");
		
}

// 送出後反回更新前端
function createRefreshForm(){
	refreshForm("");
}
