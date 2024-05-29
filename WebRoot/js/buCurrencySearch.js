/*** 
 *	檔案: buCurrencySearch.js
 *	說明：幣別查詢作業
 */

var vnB_Detail = 2;
var vnB_Header = 1;

function outlineBlock(){
	searchInitial();
	headerInitial();
	buttonLine();
	detailInitial();
}

// 搜尋初始化
function searchInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = {
			loginBrandCode : document.forms[0]["#loginBrandCode"].value,   	
			loginEmployeeCode : document.forms[0]["#loginEmployeeCode"].value,	
			vatPickerId : document.forms[0]["#vatPickerId"].value
		};
	}
}

// 可搜尋的欄位
function headerInitial(){   

	vat.block.create(
		vnB_Header,
		{
			id : "vatBlock_Head", 
			generate : true,
			table : "cellspacing='1' class='default' border='0' cellpadding='2'",
			title : "幣別資料查詢作業", 
			rows : [  
				{
					row_style : "",
					cols : [
						{items : [{name : "#L.currencyCode", type : "LABEL", value:"幣別代碼"}]},
						{items : [{name : "#F.currencyCode", type : "TEXT", size:20}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items:[{name : "#L.currencyCName",	type : "LABEL", value:"中文名稱"}]},
						{items:[{name : "#F.currencyCName", type  :"TEXT", size:20}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.currencyEName", type : "LABEL", value : "英文名稱"}]},
						{items : [{name : "#F.currencyEName", type : "TEXT", size : 20}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.orders", type : "LABEL", value : "排序"}]},
						{items : [{name : "#F.orders", type : "TEXT", size : 20}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.exChangeCode", type : "LABEL", value : "幣別代碼"}]},
						{items : [{name : "#F.exChangeCode", type : "TEXT", size : 20}]}
					]
				}
			], 	 
			beginService : "",
			closeService : ""		
		}
	);
}

function buttonLine(){
    vat.block.create(
    	vnB_Button = 0, 
    	{
			id : "vatBlock_Button", 
			generate : true,
			table : "cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
			title : "", 
			rows : [  
				{
					row_style : "", 
					cols : [
								{
									items : [
										{name : "#B.search", type : "IMG", value : "查詢", src : "./images/button_find.gif", eClick : "doSearch()"},
							 			{name : "SPACE", type : "LABEL", value : "　"},
							 			{name : "#B.clear", type : "IMG", value : "清除", src : "./images/button_reset.gif", eClick : "resetForm()"},
							 			{name : "SPACE", type : "LABEL", value :"　"},
							 	 		{name : "#B.exit", type : "IMG", value : "離開", src : "./images/button_exit.gif", eClick : "closeWindows('CONFIRM')"},
							 	 		{name : "SPACE", type : "LABEL", value : "　"},
							 			{name : "#B.view", type : "IMG", value : "檢視", src : "./images/button_view.gif", eClick : "doClosePicker()"}
						  			],
					 				td : "style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
					 			}
					]
				}
	  		], 	 
			beginService : "",
			closeService : ""		
		}
	);
}

function detailInitial(){
	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = true;

	vat.item.make(vnB_Detail, "checked", {type : "XBOX"});
	vat.item.make(vnB_Detail, "indexNo", {type : "IDX", desc : "序號"});
	vat.item.make(vnB_Detail, "currencyCode", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "幣別代碼"});
	vat.item.make(vnB_Detail, "currencyCName", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "中文名稱"});
	vat.item.make(vnB_Detail, "currencyEName", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "英文名稱"});
	vat.item.make(vnB_Detail, "exChangeCode", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "找零幣別"});
	vat.item.make(vnB_Detail, "orders", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "排序"});
	vat.item.make(vnB_Detail, "description", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "說明"});
	vat.item.make(vnB_Detail, "lastUpdatedBy", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "更新人員"});
	vat.item.make(vnB_Detail, "lastUpdateDate", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "更新日期"});
	
	vat.block.pageLayout(
		vnB_Detail, 
		{
			id : "vatDetailDiv",
			pageSize : 10,
			searchKey : ["currencyCode"],
			selectionType : "CHECK",
			indexType : "AUTO",
			canGridDelete : vbCanGridDelete,
			canGridAppend : vbCanGridAppend,
			canGridModify : vbCanGridModify,	
			loadBeforeAjxService : "loadBeforeAjxService()",	
			loadSuccessAfter : "loadSuccessAfter()", 						
			saveBeforeAjxService : "saveBeforeAjxService()",
			saveSuccessAfter : "saveSuccessAfter()"
		}
	);
}

// 載入成功後
function loadSuccessAfter(){
	if(vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	   vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}
	else{
		//vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.view" , "display", "inline");  
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
	// alert("loadBeforeAjxService");
	var processString = "process_object_name=buBasicDataService&process_object_method_name=getAJAXBuCurrencySearchPageData" + 
		"&currencyCode" + "=" + vat.item.getValueByName("#F.currencyCode") +
		"&currencyCName" + "=" + vat.item.getValueByName("#F.currencyCName") +
		"&currencyEName" + "=" + vat.item.getValueByName("#F.currencyEName") +
		"&exChangeCode" + "=" + vat.item.getValueByName("#F.exChangeCode") +
		"&orders" + "=" + vat.item.getValueByName("#F.orders") ; 
	                                                                            
	return processString;											
}

function saveSuccessAfter(){
}

//	判斷是否要關閉LINE
function checkEnableLine(){
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService(){
	//alert("saveBeforeAjxService");
	var processString = "";
	if(checkEnableLine()){
		processString = "process_object_name=buBasicDataService&process_object_method_name=saveBuCurrencySearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
	vat.block.pageSearch(
		vnB_Detail, 
		{
	    	funcSuccess : function(){
	    		vat.block.submit(
					function(){
						return "process_object_name=buBasicDataService&process_object_method_name=getSearchSelection";
					},
					{bind : true, link : false, other : true, picker : true, isPicker : true, blockId : vnB_Detail} 
	    		);
	    	}
	    }
	); 
}

// 查詢按下後
function doSearch(){
	vat.block.submit(
		function(){
			return "process_object_name=tmpAjaxSearchDataService" +
				   "&process_object_method_name=deleteByTimeScope&timeScope=" + vat.bean().vatBeanOther.timeScope;
		}, 
		{
			other : true,
			funcSuccess : function(){
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
			}
		}
	);
}

function closeWindows(closeType){
	var isExit = true;
	if("CONFIRM" == closeType){
		isExit = confirm("是否確認離開?");
	}
	if(isExit)
		window.top.close();
}

function resetForm(){
    vat.item.setValueByName("#F.currencyCode", "");
    vat.item.setValueByName("#F.currencyCName", "");
    vat.item.setValueByName("#F.currencyEName", "");
}

