/*** 
 *	檔案: buGoal.js
 *	說明：目標設定/人員設定
 */
vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_PercentDetail = 2;
var vnB_EmployeeDetail = 3;

var activeTab = 2;
var afterSavePageProcess = "";
var vsName;

function outlineBlock(){

 	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float"); 
		vat.tabm.createButton(0 ,"xTab1","目標設定明細檔" ,"vatGoalDetailDiv"        	,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false, "doPageDataSave("+vnB_PercentDetail+")");
		vat.tabm.createButton(0 ,"xTab2","人員設定明細檔" ,"vatEmployeeDetailDiv"      ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false,  "doPageDataSave("+vnB_EmployeeDetail+")");
		vat.tabm.createButton(0 ,"xTab3","簽核資料"   	,"vatApprovalDiv"        ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif",vat.item.getValueByName("#F.status") == "SAVE" || vat.item.getValueByName("#F.status") == "UNCONFIRMED"? "none" : "inline");  
 	}  
	percentDetailInitial();
	employeeDetailInitial();
	
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
            	"BGB", 
             	vat.item.getValueByName("#F.reserve1"),
             	document.forms[0]["#loginEmployeeCode"].value );

	doFormAccessControl();
}

// 初始化
function formInitial(){  
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"].value,
          processId             : document.forms[0]["#processId"].value,
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=buGoalAction&process_object_method_name=performInitial"; 
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
	 									 service:"Bu_Goal:search:20091203.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.edit"        , type:"IMG"    ,value:"修改",   src:"./images/button_edit.gif", eClick:'doEdit()'},
	 			{name:"#B.match"       , type:"IMG"    ,value:"匹配",   src:"./images/button_lock_data.gif", eClick:'doMatch()'},
	 			{name:"#B.unMatch"     , type:"IMG"    ,value:"解除匹配",   src:"./images/button_unlock_data.gif", eClick:'doUnMatch()'},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
	 			{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
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
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

// 目標主檔
function headerInitial(){ 
	var allShopCodes = vat.bean("allShopCodes");
	var allDepartments = vat.bean("allDepartments");
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"專櫃業績目標預測設定作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.department", 			type:"LABEL", 	value:"部門<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.department", 			type:"SELECT",  bind:"department", init:allDepartments, size:1, eChange:"changeShopCode()"},
					{name:"#F.headId", 					type:"TEXT",  	bind:"headId", back:false, mode:"HIDDEN"},
					{name:"#F.reserve1", 				type:"TEXT",  	bind:"reserve1", mode:"HIDDEN"},	// TMP_暫存
					{name:"#F.reserve2", 				type:"TEXT",  	bind:"reserve2", mode:"HIDDEN"}]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", mode:"HIDDEN"},
	 					{name:"#F.brandName", 			type:"TEXT",  	bind:"brandName", size:8, mode:"READONLY"}]},
	 			{items:[{name:"#L.status",				type:"LABEL", 	value:"狀態" }]},
	 			{items:[{name:"#F.status",				type:"TEXT", 	bind:"status", mode:"HIDDEN" },
	 					{name:"#F.statusName",			type:"TEXT", 	bind:"statusName", back:false, mode:"READONLY" }]}		
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.shopCode", 			type:"LABEL", 	value:"專櫃代號<font color='red'>*</font>"}]},
				{items:[{name:"#F.shopCode", 			type:"SELECT", 	bind:"shopCode", init:allShopCodes , size:1}]},
				{items:[{name:"#L.createBy",		type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",			type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.creationDate",		type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",		type:"TEXT", 	bind:"creationDate", mode:"READONLY"}]}
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

// 目標佔比明細
function percentDetailInitial(){
	var allItemCategorys = vat.bean("allItemCategorys");
//	var allItemSubcategorys = vat.bean("allItemSubcategorys");
//	var allItemBrands = vat.bean("allItemBrands");
	var allWorkTypes = vat.bean("allWorkTypes");
	
	
	var headId = vat.item.getValueByName("#F.headId");
	
	var status = vat.item.getValueByName("#F.status");
	var isOpen = true; 
//	if( status == "SIGNING" || status == "FINISH" ){
//		isOpen = false;	
//	}
	
	// 所有業種
	vsName = new Array(allItemCategorys[2].length);
	for(i = 0; i<vsName.length; i++){
		vat.ajax.XHRequest({
	        post:"process_object_name=imItemCategoryService"+
	                 "&process_object_method_name=getAJAXCategory"+
	                 "&category=" + allItemCategorys[2][i] +
	                 "&categoryType=ITEM_CATEGORY" +
	                 "&brandCode=" + vat.item.getValueByName("#F.brandCode"),
	        asyn:false,         
	        find: function change(oXHR){ 
				vsName[i] = eval( vat.ajax.getValue("allCategory", oXHR.responseText));
	        }   
	    }); 
	
/*		vat.ajax.XHRequest({ 
			post:"process_object_name=buGoalService"+
            		"&process_object_method_name=findAJAXItemSubcategory"+
            		"&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                	"&itemCategory=" + allItemCategorys[2][i],
            asyn:false,                      
			find: function change(oXHR){
				// 重新設定 哪一列的下拉
				vsName[i] = eval( vat.ajax.getValue("allItemSubcategory", oXHR.responseText));
				//alert(vsName[i]);
           }
        }); */
	}
		
	var vbCanGridDelete = isOpen;//false;
	var vbCanGridAppend = isOpen;//false;
	var vbCanGridModify = isOpen;//false;
  
	vat.item.make(vnB_PercentDetail, "indexNo"					, {type:"IDX" , desc:"序號" });
	
	vat.item.make(vnB_PercentDetail, "itemCategory"				, {type:"SELECT" , size:1, desc:"業種", init:allItemCategorys, eChange:"changeItemSubcategory("+vnB_PercentDetail+")"	});	
	vat.item.make(vnB_PercentDetail, "itemCategoryPercent"		, {type:"NUMB" , size:6, maxLen:12, desc:"佔比%"	});	
	vat.item.make(vnB_PercentDetail, "itemSubcategory"			, {type:"SELECT" , size:1, desc:"業種子類"	}); // , init:allItemSubcategorys
	vat.item.make(vnB_PercentDetail, "itemSubcategoryPercent"	, {type:"NUMB" , size:6, maxLen:12, desc:"佔比%"	});	
	vat.item.make(vnB_PercentDetail, "itemBrand"				, {type:"TEXT" , size:10, desc:"商品品牌", eChange:"changeItemBrand("+vnB_PercentDetail+")" });
	vat.item.make(vnB_PercentDetail, "searchItemBrand"			, {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 							service:"Im_Item:searchCategory:20100119.page",
	 									 							left:0, right:0, width:1024, height:768,
	 									 							servicePassData:function(){ return doPassData("ItemBrand"); },		
	 									 							serviceAfterPick:function(id){doAfterPickerFunctionProcess(vnB_PercentDetail, id,"itemBrand"); } });  
	
	vat.item.make(vnB_PercentDetail, "itemBrandName"			, {type:"TEXT" , size:15, desc:"品牌名稱", mode:"READONLY"});	
	vat.item.make(vnB_PercentDetail, "itemBrandPercent"			, {type:"NUMB" , size:6, maxLen:12, desc:"佔比%"	});
	
	vat.item.make(vnB_PercentDetail, "workType"					, {type:"SELECT" , size:1, desc:"班別", init:allWorkTypes	});	
	vat.item.make(vnB_PercentDetail, "workTypePercent"			, {type:"NUMB" , size:6, maxLen:12, desc:"佔比%"	});	
	
	vat.item.make(vnB_PercentDetail, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_PercentDetail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_PercentDetail, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_PercentDetail, "message"         , {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vnB_PercentDetail, {
														id: "vatGoalDetailDiv", 
														pageSize: 10,											
								            			canGridDelete : true,
														canGridAppend : true,
														canGridModify : true,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_PercentDetail+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_PercentDetail+")",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_PercentDetail+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_PercentDetail+")"
														});
	vat.block.pageDataLoad(vnB_PercentDetail, vnCurrentPage = 1);

}


// 人員設定明細
function employeeDetailInitial(){
	var allItemCategorys = vat.bean("allItemCategorys");
	var allItemSubcategorys = vat.bean("allItemSubcategorys");
//	var allItemBrands = vat.bean("allItemBrands");
	var allWorkTypes = vat.bean("allWorkTypes");
	
	var status = vat.item.getValueByName("#F.status");
//	var isOpen = true;
//	if( status == "SIGNING" || status == "FINISH" ){
//		isOpen = false;	
//	}
	
//	var vbCanGridDelete = isOpen;
//	var vbCanGridAppend = isOpen;
//	var vbCanGridModify = isOpen;
  
	vat.item.make(vnB_EmployeeDetail, "indexNo"					, {type:"IDX" , desc:"序號" });
	
	vat.item.make(vnB_EmployeeDetail, "employeeItemCategory"	, {type:"SELECT" , size:1, desc:"業種", init:allItemCategorys, eChange:function(){ changeItemSubcategory(vnB_EmployeeDetail);}	});	
	vat.item.make(vnB_EmployeeDetail, "employeeItemSubcategory"	, {type:"SELECT" , size:1, desc:"業種子類", init:allItemSubcategorys	});
	vat.item.make(vnB_EmployeeDetail, "employeeItemBrand"		, {type:"TEXT" , size:10, desc:"商品品牌", eChange:"changeItemBrand("+vnB_EmployeeDetail+")" });
	vat.item.make(vnB_EmployeeDetail,  "searchEmployeeItemBrand"	, {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 							service:"Im_Item:searchCategory:20100119.page",
	 									 							left:0, right:0, width:1024, height:768,
	 									 							servicePassData:function(){ return doPassData("ItemBrand"); },		
	 									 							serviceAfterPick:function(id){doAfterPickerFunctionProcess(vnB_EmployeeDetail, id, "itemBrand"); } });  
	
	vat.item.make(vnB_EmployeeDetail, "employeeItemBrandName"	, {type:"TEXT" , size:10, desc:"品牌名稱", mode:"READONLY"});
	
	vat.item.make(vnB_EmployeeDetail, "employeeCode"			, {type:"TEXT" , size:10, desc:"工號", eChange:"changeEmployee()" 	});	
	vat.item.make(vnB_EmployeeDetail, "employeeName"			, {type:"TEXT" , size:10, desc:"姓名", mode:"READONLY" 	});	
	
	vat.item.make(vnB_EmployeeDetail, "employeeWorkType"		, {type:"SELECT" , size:1, desc:"班別", init:allWorkTypes	});	 
	
	vat.item.make(vnB_EmployeeDetail, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_EmployeeDetail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_EmployeeDetail, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_EmployeeDetail, "message"         , {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vnB_EmployeeDetail, {
														id: "vatEmployeeDetailDiv", 
														pageSize: 10,											
								            			canGridDelete : true,
														canGridAppend : true,
														canGridModify : true,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_EmployeeDetail+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_EmployeeDetail+")",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_EmployeeDetail+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_EmployeeDetail+")"
														});
	vat.block.pageDataLoad(vnB_EmployeeDetail, vnCurrentPage = 1);
}

// 第一次載入 重新整理
function loadBeforeAjxService(div){
//    alert(div);	
	var processString = "";
	if( vnB_PercentDetail === div ){
		processString = "process_object_name=buGoalService&process_object_method_name=getAJAXPercentPageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") + 
		                    "&loginBrandCode=" + vat.bean().vatBeanOther.loginBrandCode;
		return processString;	
	}else if( vnB_EmployeeDetail === div ){
		processString = "process_object_name=buGoalService&process_object_method_name=getAJAXEmployeePageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&loginBrandCode=" + vat.bean().vatBeanOther.loginBrandCode;
		return processString;	
	}									
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
//    alert("saveBeforeAjxService"); 
	var processString = "";
	if( vnB_PercentDetail === div ){
		processString = "process_object_name=buGoalService&process_object_method_name=updateAJAXPercentPageLinesData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value +
		                    "&status=" + vat.item.getValueByName("#F.status");
	}else if( vnB_EmployeeDetail === div ){
		processString = "process_object_name=buGoalService&process_object_method_name=updateAJAXEmployeePageLinesData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value +
		                    "&status=" + vat.item.getValueByName("#F.status");
	}	
	return processString;			
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){ 
//	vat.block.pageRefresh(div); 
/*	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg === "") {
		if ("saveHandler" == afterSavePageProcess) {	
		} else if ("submitHandler" == afterSavePageProcess) {
			doSubmit();
		
		} else if ("copyHandler" == afterSavePageProcess) {
		} else if ("pageRefresh" == afterSavePageProcess) {
		} else if ("totalCount" == afterSavePageProcess) {
		}else if ("changeRelationData" == afterSavePageProcess) {
		}
	} else {
		alert("錯誤訊息： " + errorMsg);
	}
	afterSavePageProcess = "";*/
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(div){
	if( div == vnB_PercentDetail){
		selectBind("itemCategory","itemSubcategory", div);
  	}else if( div == vnB_EmployeeDetail){
  		selectBind("employeeItemCategory","employeeItemSubcategory", div);
  	}
} 

// bind line
function selectBind(pId, cId, div){
	for(i = 0; i<vat.block.$box[div].lines; i++){
		
		if(vat.item.getGridValueByName(pId, i) != ""){ // 明細若有值
				
			var gridData = vat.block.getGridData(div);
			var defaultData;
			if( div == vnB_PercentDetail){
				defaultData = gridData[i][3];  // 塞入佔比的業種子類
				
			}else if( div == vnB_EmployeeDetail){
			 	defaultData = gridData[i][2];	// 塞入員工的業種子類
		  	}
			var vName = vat.item.nameMake(cId, i);
			var selectIndex = vat.item.SelectGetIndex(vat.item.nameMake(pId, i));
			var allItemSubcategory = vsName[selectIndex-1];
	       	allItemSubcategory[0][0] = vName;
	       	allItemSubcategory[0][1] = defaultData;
	       		
   			allItemSubcategory[0][2] = true;
   			if(allItemSubcategory[1][0] != "<請選擇>"){
	   			allItemSubcategory[1].unshift("<請選擇>");
				allItemSubcategory[2].unshift("");
			} 
			vat.item.SelectBind(allItemSubcategory);
		}else{ // 明細沒值塞預設值
			var vName = vat.item.nameMake(cId, i);
			vat.item.SelectBind([[vName,"",true],[],[]]);
		}
	}      
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
function doPageDataSave(div){
	var headId = vat.item.getValueByName("#F.headId");
	if( headId != "" || headId != 0 ){
		if(vnB_PercentDetail==div){
//			vat.block.pageSearch(div);
			vat.block.pageDataSave(div); //存檔vnB_PercentDetail
			vat.block.pageDataSave(vnB_EmployeeDetail)
			
		}else if(vnB_EmployeeDetail==div){
//			vat.block.pageSearch(div);
			vat.block.pageDataSave(div); //存檔vnB_EmployeeDetail 
			vat.block.pageDataSave(vnB_PercentDetail)
		}
	}
	if(vnB_PercentDetail==div){
		activeTab = vnB_PercentDetail;
	}else if(vnB_EmployeeDetail==div){
		activeTab = vnB_EmployeeDetail;
	}
}
	
// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "ItemBrand":
			suffix += "&categoryType="+escape("ItemBrand");
			break;
	}
//	alert(suffix);
	return suffix;
}
	
// picker 回來執行
function doAfterPickerFunctionProcess(div, id, key){
	//do picker back something
	var vlineId = vat.item.getGridLine(id);
	var itemBrand;
	if(activeTab == vnB_PercentDetail){
		itemBrand = "itemBrand";
	}else if(activeTab == vnB_EmployeeDetail){
		itemBrand = "employeeItemBrand";
	}
	switch(key){
		case "itemBrand":
			if(typeof vat.bean().vatBeanPicker.categoryResult != "undefined"){
		    	vat.item.setGridValueByName(itemBrand, vlineId, vat.bean().vatBeanPicker.categoryResult[0]["id.categoryCode"]); 
				changeItemBrand(div,id);
			}
		break;
	}
}	

// 動態改變商品類別名稱
function changeCategoryCodeName(code){
	var condition = "", name ="";
	
	switch(code){
		case "ItemBrand":
			condition =  "&categoryCode=" + ( "ItemBrand" === code ? vat.item.getValueByName("#F.itemBrand") : "" );
			if(activeTab == vnB_PercentDetail){
				name = "itemBrandName";
			}else if(activeTab == vnB_EmployeeDetail){
				name = "employeeItemBrandName";
			}
		break;
	}
	
	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXCategoryName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&categoryType=" + code + condition,
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName(name, vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		vat.item.setValueByName(name, "查無此類別");
		}   
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

// 送出後的返回執行
function createRefreshForm(){
	refreshForm("");
}

// 動態改變專櫃下拉
function changeShopCode(){
	vat.ajax.XHRequest({
    	post:"process_object_name=buShopService"+
                   "&process_object_method_name=getAJAXShop"+
                   "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                   "&department=" + vat.item.getValueByName("#F.department"), 
    	find: function change(oXHR){
			var allShop = eval(vat.ajax.getValue("allShop", oXHR.responseText));
	        allShop[0][0] = "#F.shopCode";
			vat.item.SelectBind(allShop); 
    	}   
	});
}

// 工號改變
function changeEmployee(){
	var vLineId				= vat.item.getGridLine();
//	alert("vLineId = " + vLineId);
	var employeeCode = vat.item.getGridValueByName("employeeCode", vLineId);
	
	vat.ajax.XHRequest(
       {
           post:"process_object_name=buGoalService"+
                    "&process_object_method_name=getAJAXEmployee"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + employeeCode, 
           find: function change(oXHR){ 
           		vat.item.setGridValueByName("employeeCode", vLineId, vat.ajax.getValue("employeeCode", oXHR.responseText) );
           		vat.item.setGridValueByName("employeeName", vLineId, vat.ajax.getValue("employeeName", oXHR.responseText) );
           		vat.item.setGridValueByName("employeeWorkType", vLineId, vat.ajax.getValue("employeeWorkType", oXHR.responseText) );
           },
           fail: function changeError(){
          		vat.item.setGridValueByName("employeeCode", vLineId, sourceOrderNo );
           		vat.item.setGridValueByName("employeeName", vLineId, "" );
           		vat.item.setGridValueByName("employeeWorkType", vLineId, "" );
           		
           }   
       });
}

// 連動改變業種子類
function changeItemSubcategory(div){
	var vLineId				= vat.item.getGridLine();
//	alert("vLineId = " + vLineId);
	if(vnB_PercentDetail===div){
/*		vat.ajax.XHRequest({
			post:"process_object_name=buGoalService"+
            		"&process_object_method_name=findAJAXItemSubcategory"+
            		"&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                	"&itemCategory=" + vat.item.getGridValueByName("itemCategory", vLineId),                      
			find: function change(oXHR){
				// 重新設定 哪一列的下拉
				var vsName = vat.item.nameMake("itemSubcategory", vLineId);
				var allItemSubcategory = eval( vat.ajax.getValue("allItemSubcategory", oXHR.responseText));
           		allItemSubcategory[0][0] = vsName;
//           		alert( "vsName =" + vsName + "\nallItemSubcategory = " + allItemSubcategory);
				vat.item.SelectBind(allItemSubcategory); 
           }
        });*/
        var parentCategory = vat.item.getGridValueByName("itemCategory", vLineId);
		var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	 
	    vat.ajax.XHRequest(
	    {
	        post:"process_object_name=imItemCategoryService"+
	                 "&process_object_method_name=getAJAXCategory"+
	                 "&category=" + parentCategory +
	                 "&categoryType=ITEM_CATEGORY" +
	                 "&brandCode=" + brandCode,
	        find: function changeRequestSuccess(oXHR){ 
	        	var vsName = vat.item.nameMake("itemSubcategory", vLineId);
				var allCategory = eval(vat.ajax.getValue("allCategory", oXHR.responseText));
	        	allCategory[0][0] = vsName;
				vat.item.SelectBind(allCategory); 
				
				vat.item.setGridValueByName("itemCategoryPercent", vLineId, "100");
				vat.item.setGridValueByName("itemSubcategoryPercent", vLineId, "100");
				vat.item.setGridValueByName("itemBrandPercent", vLineId, "100");
				vat.item.setGridValueByName("workTypePercent", vLineId, "100");  
	        }   
	    });
        
        
	}else if(vnB_EmployeeDetail===div){
		/*vat.ajax.XHRequest({
			post:"process_object_name=buGoalService"+
            		"&process_object_method_name=findAJAXEmployeeItemSubcategory"+
            		"&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                	"&employeeItemCategory=" + vat.item.getGridValueByName("employeeItemCategory", vLineId),                      
			find: function change(oXHR){
				// 重新設定 哪一列的下拉
				var vtName = vat.item.nameMake("employeeItemSubcategory", vLineId);
				
				var allItemSubcategory = eval( vat.ajax.getValue("allItemSubcategory", oXHR.responseText));
           		allItemSubcategory[0][0] = vtName;
				vat.item.SelectBind(allItemSubcategory); 
           }
        });*/
        var parentCategory = vat.item.getGridValueByName("employeeItemCategory", vLineId);
		var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	 
	    vat.ajax.XHRequest(
	    {
	        post:"process_object_name=imItemCategoryService"+
	                 "&process_object_method_name=getAJAXCategory"+
	                 "&category=" + parentCategory +
	                 "&categoryType=ITEM_CATEGORY" +
	                 "&brandCode=" + brandCode,
	        find: function changeRequestSuccess(oXHR){ 
	        	var vsName = vat.item.nameMake("employeeItemSubcategory", vLineId);
				var allCategory = eval(vat.ajax.getValue("allCategory", oXHR.responseText));
	        	allCategory[0][0] = vsName;
				vat.item.SelectBind(allCategory); 
	        }   
	    });
	}     
}

// 取得商品品牌名稱
function changeItemBrand(div,id){
	var vLineId	  = typeof id != "undefined" ? vat.item.getGridLine(id) : vat.item.getGridLine();
	var brandCode =	vat.item.getValueByName("#F.brandCode");
	
	switch(div){
		case vnB_PercentDetail:
			var itemBrand = vat.item.getGridValueByName("itemBrand", vLineId);
			if( itemBrand != "" ){
	
				vat.ajax.XHRequest({
					post:"process_object_name=buGoalService"+
		            		"&process_object_method_name=findAJAXItemBrand"+
		            		"&brandCode=" + brandCode + 
		                	"&itemBrand=" + itemBrand,                      
					find: function change(oXHR){
						vat.item.setGridValueByName("itemBrandName", vLineId, vat.ajax.getValue("itemBrandName", oXHR.responseText) );
						vat.item.setGridValueByName("itemBrand", vLineId, vat.ajax.getValue("itemBrand", oXHR.responseText));
		           	}
		        });
			}else{
				vat.item.setGridValueByName("itemBrandName", vLineId, "" );
			}
			break;
		case vnB_EmployeeDetail:
			var employeeItemBrand = vat.item.getGridValueByName("employeeItemBrand", vLineId);
			if( employeeItemBrand != "" ){
				vat.ajax.XHRequest({
					post:"process_object_name=buGoalService"+
		            		"&process_object_method_name=findAJAXItemBrand"+
		            		"&brandCode=" + brandCode + 
		                	"&itemBrand=" + employeeItemBrand,                      
					find: function change(oXHR){
						vat.item.setGridValueByName("employeeItemBrandName", vLineId, vat.ajax.getValue("itemBrandName", oXHR.responseText) );
						vat.item.setGridValueByName("employeeItemBrand", vLineId, vat.ajax.getValue("itemBrand", oXHR.responseText));
		           	}
		        });
		    }else{
		    	vat.item.setGridValueByName("employeeItemBrandName", vLineId, "" );
		    }    
			break;
			
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

function doSubmitHandler(formAction){
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}
	
	if (confirm(alertMessage)) {		
		vat.bean().vatBeanOther.formAction 		= formAction;
		if("SUBMIT" == formAction){
			vat.block.pageDataSave(activeTab)
			afterSavePageProcess = "submitHandler";	
		}
	}
}

// 送出,暫存按鈕
function doSubmit(formAction){
	// 取得簽核結果
	function getApprovalResult(){
		if(vat.item.getValueByName("#F.status") == "SIGNING"){
			return vat.item.getValueByName("#F.approvalResult").toString();
		}else{
			return "true";
		}
	}

	var headId = vat.item.getValueByName("#F.headId");
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){
		alertMessage = "是否確定暫存?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}
	
	if(confirm(alertMessage)){
		var formId	= vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var approvalResult        = getApprovalResult();  
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
//		alert("formId = " + formId);
//		alert("headId = " + headId);

		if( ( status == "SAVE" || status == "FINISH" ) || (inProcessing   && (status == "SAVE" || status == "SIGNING" || status == "REJECT" )) ){
		    if(headId != ""){
				vat.block.pageDataSave( activeTab ,{ 
					funcSuccess:function(){
						vat.bean().vatBeanOther.formAction 		= formAction;
						vat.bean().vatBeanOther.formAction 		= formAction;
			  			vat.bean().vatBeanOther.beforeStatus	= status;	
			  			vat.bean().vatBeanOther.processId       = processId;
		  				vat.bean().vatBeanOther.approvalResult  = approvalResult;
		  				vat.bean().vatBeanOther.approvalComment =  approvalComment;
		  				
				        if("SUBMIT_BG" == formAction){
				      		vat.block.submit(function(){return "process_object_name=buGoalAction"+
				                    "&process_object_method_name=getOrderNoBasic";}, {bind:true, link:true, other:true});
					    }else{
							vat.block.submit(function(){return "process_object_name=buGoalAction"+
					                    "&process_object_method_name=performTransaction";},{
					                    bind:true, link:true, other:true,
					                    funcSuccess:function(){
							        		vat.block.pageRefresh(activeTab);
							        	}}
							);
				        }
			      	}
		      	});
			}
		}else{
			alert("您的表單已加入待辦事件，請從待辦事件選取後，再次送出!");
		}	
	}
}

// 尋找匹配
function doMatch(){
	var department = vat.item.getValueByName("#F.department");
	var shopCode = vat.item.getValueByName("#F.shopCode");
	if(department == ""){
		alert('部門不可為空');
	}else if(shopCode == ""){
		alert('專櫃代號不可為空');
	}else{
		vat.ajax.XHRequest(
		    {
		        post:"process_object_name=buGoalService"+
		                    "&process_object_method_name=executeMatchBuGoal"+
		                    "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
		                    "&department=" + department +
		                    "&shopCode=" + shopCode,
		        find: function loadBudgetSuccess(oXHR){
			        vat.item.setValueByName("#F.headId" ,vat.ajax.getValue("headId", oXHR.responseText));
			        vat.item.setValueByName("#F.status" ,vat.ajax.getValue("formStatus", oXHR.responseText));
			        vat.item.setValueByName("#F.statusName" ,vat.ajax.getValue("statusName", oXHR.responseText));
			        vat.item.setValueByName("#F.reserve2" ,vat.ajax.getValue("reserve2", oXHR.responseText));
//			        alert("status = " + vat.item.getValueByName("#F.status"));
			        vat.block.pageRefresh(vnB_PercentDetail);
			        vat.block.pageRefresh(vnB_EmployeeDetail);
			        
			  		doFormAccessControl("match");
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
	vat.item.setValueByName("#F.status", "");
	vat.item.setValueByName("#F.statusName", "暫存");
	
	
	// 初始化筆數
	vat.item.setValueByName("#L.currentRecord", "0");
	vat.item.setValueByName("#L.maxRecord"    , "0");
    vat.bean().vatBeanPicker.result = null;
    
 	vat.block.pageRefresh(vnB_PercentDetail);
    vat.block.pageRefresh(vnB_EmployeeDetail); 				
     				
    doFormAccessControl();
}

// 修改明細 將明細打開
function doEdit(){

	vat.ajax.XHRequest({
          post:"process_object_name=buGoalService"+
                   "&process_object_method_name=updateHead"+
                   "&brandCode=" + document.forms[0]["#loginBrandCode"    ].value + 
                   "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
                   "&headId=" + vat.item.getValueByName("#F.headId"),
          find: function change(oXHR){ 
          		vat.item.setValueByName("#F.status", vat.ajax.getValue("formStatus", oXHR.responseText));
				vat.item.setValueByName("#F.statusName", vat.ajax.getValue("statusName", oXHR.responseText));
          } 
	});	

	doFormAccessControl("edit");
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
			return "process_object_name=buGoalAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				vat.block.pageDataLoad(vnB_PercentDetail, vnCurrentPage = 1);
				vat.block.pageDataLoad(vnB_EmployeeDetail, vnCurrentPage = 1);
				
  		        refreshWfParameter(vat.item.getValueByName("#F.brandCode"), 
         					 	   "BGB", 
         					 	   vat.item.getValueByName("#F.reserve1"));
				vat.block.pageRefresh(102);		 
	        	vat.tabm.displayToggle(0, "xTab3", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
				
				
				var allShopCodes = vat.bean("allShopCodes");
				allShopCodes[0][0] = "#F.shopCode";
				vat.item.SelectBind(allShopCodes); 
				
				doFormAccessControl();
     	}});
 
}

// 依狀態鎖form
function doFormAccessControl( match ){
	var formId 		= vat.bean().vatBeanOther.formId;
	var status 		= vat.item.getValueByName("#F.status");
	var processId	= vat.bean().vatBeanOther.processId;
	var reserve2 	= vat.item.getValueByName("#F.reserve2"); // 是否流程內 for 條件凍結用
	// 初始化
	//======================<header>=============================================
//	vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
//	vat.item.setAttributeByName("#F.department", "readOnly", false);
	
	vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true, true);
	
	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true);
	//======================<detail>=============================================
	vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], false,false,false);

//	vat.item.setGridAttributeByName("itemCategory", "readOnly", true); 
//	vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", true);
//	vat.item.setGridAttributeByName("itemSubcategory", "readOnly", true);
//	vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", true);
//	vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//	vat.item.setGridAttributeByName("searchItemBrand", "readOnly", true);
//	vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", true);
	
//	vat.item.setGridAttributeByName("workType", "readOnly", true);
//	vat.item.setGridAttributeByName("workTypePercent", "readOnly", true);
	
//	vat.item.setGridAttributeByName("employeeCode", "readOnly", true);
//	vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", true);
//	vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", true);
//	vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", true);
//	vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", true);
//	vat.item.setGridAttributeByName("employeeWorkType", "readOnly", true);
	//=======================<buttonLine>========================================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "none");
	vat.item.setStyleByName("#B.save", 		"display", "none");
	vat.item.setStyleByName("#B.submitBG", 	"display", "none");
	vat.item.setStyleByName("#B.message", 	"display", "none");
	vat.item.setStyleByName("#B.match", 	"display", "inline");
	vat.item.setStyleByName("#B.unMatch", 	"display", "none");
	vat.item.setStyleByName("#B.import", 	"display", "none");
	vat.item.setStyleByName("#B.export", 	"display", "none");
	vat.item.setStyleByName("#B.edit", 		"display", "none");
	//===========================================================================
	
	if( formId != ""  ){
		//======================<header>=============================================
//		vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
//		vat.item.setAttributeByName("#F.department", "readOnly", true);
		vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
	
		//===========================================================================
		if( processId != null && processId != 0 ){ //從待辦事項進入
//			alert("從待辦事項進入");
			vat.item.setStyleByName("#B.new", 		"display", "none");
			vat.item.setStyleByName("#B.search", 	"display", "none");
			vat.item.setStyleByName("#B.match", 	"display", "none");
			vat.item.setStyleByName("#B.unMatch", 	"display", "none");
			vat.item.setStyleByName("#B.export", 	"display", "inline");
			if( status == "SAVE" || status == "REJECT" ){
				//=======================<buttonLine>========================================
		  		vat.item.setStyleByName("#B.submit"	 	, "display", "inline");
		  		vat.item.setStyleByName("#B.submitBG"	, "display", "inline");
		  		vat.item.setStyleByName("#B.message" 	, "display", "inline");
		  		vat.item.setStyleByName("#B.save" 		, "display", "inline");
		  		//===========================<目標佔比detail>================================================
//				vat.item.setGridAttributeByName("itemCategory", "readOnly", false); 
//				vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", false);
//				vat.item.setGridAttributeByName("itemSubcategory", "readOnly", false);
//				vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", false);
//				vat.item.setGridAttributeByName("workType", "readOnly", false);
//				vat.item.setGridAttributeByName("workTypePercent", "readOnly", false);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("searchItemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", false);
				//===========================<人員設定detail>================================================
//				vat.item.setGridAttributeByName("employeeCode", "readOnly", false);
//				vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", false);
//				vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", false);		
//				vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", false);	
//				vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("employeeWorkType", "readOnly", false);
				
				vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], true,true,true);
				//=========================================================================================
			}else if( status == "SIGNING" ){
				//=======================<buttonLine>========================================
				vat.item.setStyleByName("#B.submit", 		"display", "inline");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
				vat.item.setStyleByName("#B.message", 		"display", "none");
		  		vat.item.setStyleByName("#B.save", 			"display", "none");
		  		//===========================<目標佔比detail>================================================
//				vat.item.setGridAttributeByName("itemCategory", "readOnly", true); 
//				vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategory", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("workType", "readOnly", true);
//				vat.item.setGridAttributeByName("workTypePercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("searchItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", true);
				//===========================<人員設定detail>================================================
//				vat.item.setGridAttributeByName("employeeCode", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", true);	
//				vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", true);	
//				vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeWorkType", "readOnly", true);
				
				vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], false,false,false);
				//==========================<簽核detail>====================================================
				vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
				vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
			}
		}else{	// picker
			vat.item.setStyleByName("#B.unMatch"	, "display", "inline");
	  		vat.item.setStyleByName("#B.match"	 	, "display", "none");
	  		//======================<header>=============================================
			vat.item.setStyleByName("#B.export", 	"display", "inline");
//			vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
//			vat.item.setAttributeByName("#F.department", "readOnly", true);
			
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			//========================================================================
			if( ( status == "SAVE" && reserve2 == "N" ) ){
				//=======================<buttonLine>========================================
		  		vat.item.setStyleByName("#B.submit"	 	, "display", "inline");
		  		vat.item.setStyleByName("#B.submitBG"	, "display", "inline");
		  		vat.item.setStyleByName("#B.message" 	, "display", "inline");
		  		vat.item.setStyleByName("#B.save" 		, "display", "inline");
				//===========================<目標佔比detail>================================================
//				vat.item.setGridAttributeByName("itemCategory", "readOnly", false); 
//				vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", false);
//				vat.item.setGridAttributeByName("itemSubcategory", "readOnly", false);
//				vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", false);
//				vat.item.setGridAttributeByName("workType", "readOnly", false);
//				vat.item.setGridAttributeByName("workTypePercent", "readOnly", false);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", false);
				//===========================<人員設定detail>================================================
//				vat.item.setGridAttributeByName("employeeCode", "readOnly", false);
//				vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", false);
//				vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", false);	
//				vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", false);	
				
				vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], true,true,true);
				//=========================================================================================
			}else if( (status == "SAVE" && reserve2 == "Y" ) || status == "REJECT" || status == "SIGNING"  ){
				vat.item.setStyleByName("#B.submit", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
				vat.item.setStyleByName("#B.message", 		"display", "none");
		  		vat.item.setStyleByName("#B.save", 			"display", "none");
		  		//===========================<目標佔比detail>================================================
//				vat.item.setGridAttributeByName("itemCategory", "readOnly", true); 
//				vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategory", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("workType", "readOnly", true);
//				vat.item.setGridAttributeByName("workTypePercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("searchItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", true);
				//===========================<人員設定detail>================================================
//				vat.item.setGridAttributeByName("employeeCode", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", true);	
//				vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", true);	
//				vat.item.setGridAttributeByName("employeeWorkType", "readOnly", true);
				
				vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], false,false,false);
				//=========================================================================================
			}else if( status == "FINISH"){
				vat.item.setStyleByName("#B.submit", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
				vat.item.setStyleByName("#B.message", 		"display", "none");
		  		vat.item.setStyleByName("#B.save", 			"display", "none");
		  		vat.item.setStyleByName("#B.import", 		"display", "none");
		  		vat.item.setStyleByName("#B.edit", 			"display", "inline");
		  		//===========================<目標佔比detail>================================================
//				vat.item.setGridAttributeByName("itemCategory", "readOnly", true); 
//				vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategory", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("workType", "readOnly", true);
//				vat.item.setGridAttributeByName("workTypePercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("searchItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", true);
				//===========================<人員設定detail>================================================
//				vat.item.setGridAttributeByName("employeeCode", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", true);	
//				vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeWorkType", "readOnly", true);	
				
				vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], false,false,false);
				//=========================================================================================
			}
		}

	}else{	// 維護進入點
		if( match == "match" ){	// 匹配
			//======================<header>=============================================
//			vat.item.setAttributeByName("#F.department", "readOnly", true);
//			vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
			
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			//=======================<buttonLine>========================================
			vat.item.setStyleByName("#B.unMatch"	, "display", "inline");
		  	vat.item.setStyleByName("#B.match"	 	, "display", "none");
		  	vat.item.setStyleByName("#B.import", 	"display", "inline");
			vat.item.setStyleByName("#B.export", 	"display", "inline");
		  	//===========================================================================
			if( (status == "SAVE" && reserve2 == "N") ){ // 只有一開始進來暫存狀態和FINISH 可以送出
		  		vat.item.setStyleByName("#B.submit"	 	, "display", "inline");
		  		vat.item.setStyleByName("#B.submitBG"	, "display", "inline");
		  		vat.item.setStyleByName("#B.message" 	, "display", "inline");
		  		vat.item.setStyleByName("#B.save" 		, "display", "inline");
		  		//===========================<目標佔比detail>================================================
//				vat.item.setGridAttributeByName("itemCategory", "readOnly", false); 
//				vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", false);
//				vat.item.setGridAttributeByName("itemSubcategory", "readOnly", false);
//				vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", false);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("workType", "readOnly", false);
//				vat.item.setGridAttributeByName("workTypePercent", "readOnly", false);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("searchItemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", false);
				//===========================<人員設定detail>================================================
//				vat.item.setGridAttributeByName("employeeCode", "readOnly", false);
//				vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", false);
//				vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", false);		
//				vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", false);
//				vat.item.setGridAttributeByName("employeeWorkType", "readOnly", false);
				
				vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], true,true,true);
				//=========================================================================================
			}else if( (status == "SAVE" && reserve2 == "Y")   ){	// 還在流程裡的狀態(簽核,暫存)
				vat.item.setStyleByName("#B.submit", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
				vat.item.setStyleByName("#B.message", 		"display", "none");
		  		vat.item.setStyleByName("#B.save", 			"display", "none");
		  		vat.item.setStyleByName("#B.import", 		"display", "none");
		  		vat.item.setStyleByName("#B.edit", 			"display", "none");
		  		//===========================<目標佔比detail>================================================
//				vat.item.setGridAttributeByName("itemCategory", "readOnly", true); 
//				vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategory", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("workType", "readOnly", true);
//				vat.item.setGridAttributeByName("workTypePercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("searchItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", true);
				//===========================<人員設定detail>================================================
//				vat.item.setGridAttributeByName("employeeCode", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", true);	
//				vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeWorkType", "readOnly", true);	
				
				vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], false,false,false);
				//=========================================================================================
			}else if( status == "SIGNING" || status == "FINISH"  ){	// 還在流程裡的狀態(簽核,暫存)
				vat.item.setStyleByName("#B.submit", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
				vat.item.setStyleByName("#B.message", 		"display", "none");
		  		vat.item.setStyleByName("#B.save", 			"display", "none");
		  		vat.item.setStyleByName("#B.import", 		"display", "none");
		  		if( status == "FINISH" ){
		  			vat.item.setStyleByName("#B.edit", 			"display", "inline");
		  		}
		  		//===========================<目標佔比detail>================================================
//				vat.item.setGridAttributeByName("itemCategory", "readOnly", true); 
//				vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategory", "readOnly", true);
//				vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("workType", "readOnly", true);
//				vat.item.setGridAttributeByName("workTypePercent", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("searchItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", true);
				//===========================<人員設定detail>================================================
//				vat.item.setGridAttributeByName("employeeCode", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", true);	
//				vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", true);
//				vat.item.setGridAttributeByName("employeeWorkType", "readOnly", true);	
				
				vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], false,false,false);
				//=========================================================================================
			}
		}else if( match == "edit" ){
			vat.item.setStyleByName("#B.submit", 	"display", "inline");
			vat.item.setStyleByName("#B.save", 		"display", "inline");
			vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
			vat.item.setStyleByName("#B.message", 	"display", "inline");
			vat.item.setStyleByName("#B.unMatch", 	"display", "inline");
		  	vat.item.setStyleByName("#B.match", 	"display", "none");
		  	vat.item.setStyleByName("#B.import", 	"display", "inline");
			vat.item.setStyleByName("#B.export", 	"display", "inline");
			vat.item.setStyleByName("#B.edit", 		"display", "none");
			//======================<header>=============================================
//			vat.item.setAttributeByName("#F.department", "readOnly", true);
//			vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
			
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			//===========================<目標佔比detail>================================================
//			vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], true,true,true);
			vat.block.pageRefresh(vnB_PercentDetail);
	    	vat.block.pageRefresh(vnB_EmployeeDetail); 		
//		    vat.item.setGridAttributeByName("itemCategory", "readOnly", false); 
//			vat.item.setGridAttributeByName("itemCategoryPercent", "readOnly", false);
//			vat.item.setGridAttributeByName("itemSubcategory", "readOnly", false);
//			vat.item.setGridAttributeByName("itemSubcategoryPercent", "readOnly", false);
//			vat.item.setGridAttributeByName("itemBrand", "readOnly", false);
//			vat.item.setGridAttributeByName("workType", "readOnly", false);
//			vat.item.setGridAttributeByName("workTypePercent", "readOnly", false);
//			vat.item.setGridAttributeByName("itemBrand", "readOnly", false);
//			vat.item.setGridAttributeByName("searchItemBrand", "readOnly", false);
//			vat.item.setGridAttributeByName("itemBrandPercent", "readOnly", false);
			//===========================<人員設定detail>================================================
//			vat.item.setGridAttributeByName("employeeCode", "readOnly", false);
//			vat.item.setGridAttributeByName("employeeItemCategory", "readOnly", false);
//			vat.item.setGridAttributeByName("employeeItemSubcategory", "readOnly", false);		
//			vat.item.setGridAttributeByName("employeeItemBrand", "readOnly", false);
//			vat.item.setGridAttributeByName("searchEmployeeItemBrand", "readOnly", false);
//			vat.item.setGridAttributeByName("employeeWorkType", "readOnly", false);	
			
			vat.block.canGridModify([vnB_PercentDetail, vnB_EmployeeDetail], true,true,true);
		}
	}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=BU_GOAL_BASIC" + 
		"&levelType=ERROR" +
        "&processObjectName=buGoalService" + 
        "&processObjectMethodName=getIdentificationBasic" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=buGoalAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

// 佔比明細匯入
function importFormData(){
	var importBeanName, processObjectMethodName;
	if(activeTab == vnB_PercentDetail){
		importBeanName = "BU_GOAL_PERCENT";
		processObjectMethodName = "executeImportPercentLists";
	}else if(activeTab == vnB_EmployeeDetail){
		importBeanName = "BU_GOAL_EMPLOYEE";
		processObjectMethodName = "executeImportEmployeeLists";
	}

	var width = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=" + importBeanName +
		"&importFileType=XLS" +
        "&processObjectName=buGoalService" + 
        "&processObjectMethodName=" + processObjectMethodName + 
        "&arguments=" + headId +
        "&parameterTypes=LONG" +
        "&blockId=" + activeTab,
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 明細匯出
function exportFormData(){
	var exportBeanName, gridFieldName;
	if(activeTab == vnB_PercentDetail){
		exportBeanName = "BU_GOAL_PERCENT"; 
		gridFieldName = "buGoalPercentLines";
	}else if(activeTab == vnB_EmployeeDetail){
		exportBeanName = "BU_GOAL_EMPLOYEE";
		gridFieldName = "buGoalEmployeeLines";
	}
	
	var width = "200";
    var height = "30";
    var headId = vat.item.getValueByName("#F.headId");
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=" + exportBeanName +
              "&fileType=XLS" + 
              "&processObjectName=buGoalHeadDAO" + 
              "&processObjectMethodName=findById" + 
              "&gridFieldName=" + gridFieldName + 
              "&arguments=" + headId + 
              "&parameterTypes=LONG";
    
    window.open(url, '業績目標匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}