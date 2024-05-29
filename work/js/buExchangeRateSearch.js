/*** 
 *	檔案: buExchangeRateSearch.js 
 *	說明：匯率查詢作業
 */

var vnB_Detail = 2;
var vnB_Header = 1;

function outlineBlock(){
   /* 
    if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = {
			loginBrandCode : document.forms[0]["#loginBrandCode"].value,   	
			loginEmployeeCode : document.forms[0]["#loginEmployeeCode"].value,	
			vatPickerId : document.forms[0]["#vatPickerId"].value
			
		};
	}
*/
	headerInitial();
	buttonLine();
	detailInitial();
	searchInitial();
	doFormAccessControl();
}

// 搜尋初始化
function searchInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	vat.bean().vatBeanOther = {
			loginBrandCode    : document.forms[0]["#loginBrandCode"   ].value,   	
			loginEmployeeCode : document.forms[0]["#loginEmployeeCode"].value,
			organizationCode  : document.forms[0]["#organizationCode"].value,
			vatPickerId       : document.forms[0]["#vatPickerId"      ].value
		};
     vat.bean.init(function(){
		return "process_object_name=buBasicDataService&process_object_method_name=executeExSearchInitial"; 
   		},{other: true});
   //vat.item.SelectBind(vat.bean("allOrderTypes")         ,{ itemName : "#F.orderTypeCode" });
     vat.item.SelectBind(vat.bean("allExchangeRateType")  ,{ itemName : "#F.organizationCode" });
     vat.item.SelectBind(vat.bean("allSourceCurrency"),{ itemName : "#F.sourceCurrency" });
     vat.item.SelectBind(vat.bean("allAgainstCurrency"),{ itemName : "#F.againstCurrency" });
  // vat.item.SelectBind(vat.bean("allExchangeRate")  ,{ itemName : "#F.exchangeRate" });
  // vat.item.SelectBind(vat.bean("allBeginDate")  ,{ itemName : "#F.brandCode" });
  // vat.item.SelectBind(vat.bean("allExchangeRateType")  ,{ itemName : "#F.exchangeRateType" });
     vat.item.bindAll();
     //vat.form.item.setFocus( "#F.orderNo" );
  }
}

// 可搜尋的欄位
function headerInitial(){   
    
     
    var allExchangeRateType   = vat.bean("allExchangeRateType");
    var allSourceCurrency     = vat.bean("allSourceCurrency");
    var allAgainstCurrency    = vat.bean("allAgainstCurrency");
  //var allExchangeRate       = vat.bean("allExchangeRate");
  //var allBeginDate          = vat.bean("allBeginDate");
	vat.block.create(
		vnB_Header,
		{
			id : "vatBlock_Head", 
			generate : true,
			table : "cellspacing='1' class='default' border='0' cellpadding='2'",
			title : "匯率資料查詢作業", 
			rows : [
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.organizationCode", type : "LABEL", value : "匯率別<font color='red'>*</font>"}]},
						{items : [{name : "#F.organizationCode", type : "SELECT", bind : "organizationCode",init:allExchangeRateType, mode : "READONLY"}]},
						{items : [{name : "#L.sourceCurrency", type : "LABEL", value : "來源幣別<font color='red'>*</font>"}]},
						{items : [{name : "#F.sourceCurrency", type : "SELECT", bind : "sourceCurrency"}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.againstCurrency", type : "LABEL", value : "目標幣別<font color='red'>*</font>"}]},
						{items : [{name : "#F.againstCurrency", type : "SELECT", bind : "againstCurrency"}]},
						{items : [{name : "#L.exchangeRate", type : "LABEL", value : "匯率<font color='red'>*</font>"}]},
						{items : [{name : "#F.exchangeRate", type : "TEXT", bind : "exchangeRate", size : 7, maxLen : 7}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.beginDate", type : "LABEL", value : "啟用日期<font color='red'>*</font>"}]},
						{items : [{name : "#F.beginDate", type : "DATE", bind : "beginDate", size : 2}]},
						{items : [{name : "#L.brandCode", type : "LABEL", value : "啟用日期<font color='red'>*</font>"}]},
						{items : [{name : "#F.brandCode", type : "TEXT", size : 7, maxLen : 7}]}
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
	var vbCanGridAppend = true;
	var vbCanGridModify = true;

	vat.item.make(vnB_Detail, "checked", {type : "XBOX"});
	vat.item.make(vnB_Detail, "indexNo", {type : "IDX", desc : "序號"});
	vat.item.make(vnB_Detail, "organizationCode", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "匯率別"});
	vat.item.make(vnB_Detail, "sourceCurrency", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "來源幣別"});
	vat.item.make(vnB_Detail, "againstCurrency", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "目標幣別"});
	vat.item.make(vnB_Detail, "exchangeRate", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "匯率"});
	vat.item.make(vnB_Detail, "beginDate", {type : "TEXT", size : 18, maxLen : 20, mode : "READONLY", desc : "啟用日期"});
	vat.item.make(vnB_Detail, "lastUpdatedBy", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"  });
	vat.item.make(vnB_Detail, "lastUpdateDate"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	
	
	vat.block.pageLayout(
		vnB_Detail, 
		{
			id : "vatDetailDiv",
			pageSize : 10,
			searchKey : ["id.organizationCode","id.sourceCurrency","id.againstCurrency","exchangeRate","id.beginDate"],
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

	 //alert("loadBeforeAjxService");
	var processString = "process_object_name=buBasicDataService&process_object_method_name=getAJAXBuExchangeRateSearchPageData" + 
		"&organizationCode" + "=" + document.forms[0]["#organizationCode"].value +
		"&sourceCurrency" + "=" + vat.item.getValueByName("#F.sourceCurrency") +
		"&againstCurrency" + "=" + vat.item.getValueByName("#F.againstCurrency")+
		"&exchangeRate" + "=" + vat.item.getValueByName("#F.exchangeRate") +
		"&beginDate" + "=" + vat.item.getValueByName("#F.beginDate");
		 
	                                                                            
	return processString;											
}

function saveSuccessAfter(){
}

//	判斷是否要關閉LINE
function checkEnableLine(){
	return true;
}
//  清除
function resetForm(){
    vat.item.setValueByName("#F.organizationCode", "");
    vat.item.setValueByName("#F.sourceCurrency", "");
    vat.item.setValueByName("#F.againstCurrency", "");
    vat.item.setValueByName("#F.exchangeRate", "");
    vat.item.setValueByName("#F.beginDate", "");
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService(){

   //alert("saveBeforeAjxService");
	var processString = "";
	if(checkEnableLine()){
		processString = "process_object_name=buBasicDataService&process_object_method_name=saveBuExchangeRateSearchResult";
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
					{bind : true, link : true, other : true, picker : true, isPicker : true, blockId : vnB_Detail} 
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

function doFormAccessControl(){
	vat.item.setStyleByName("#B.view" , "display", "none");
}

