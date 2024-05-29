vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Header = 1;
var vnB_Detail = 2;


function outlineBlock(){
    formDataInitial();
    headerInitial();
    buttonLine();
    detailInitial();
}

function formDataInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' &&
      document.forms[0]["#loginBrandCode"].value != '[binding]'){
  	  vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value
	    }; 
	    
      vat.bean.init(function(){
		return "process_object_name=soShopDailyAction&process_object_method_name=performSearchInitial"; 
      },{other: true});
  }
       vat.item.bindAll();
}

function headerInitial(){
var brandCode = document.forms[0]["#loginBrandCode"].value; 
var allShops = vat.bean("allShops");
var salesUnit = "專櫃";
if("T2" == brandCode){
    salesUnit = "機台";
}
                 
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"POS每日資料查詢作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.shopCode", type:"LABEL", value:salesUnit}]},	 
	 {items:[{name:"#F.shopCode", type:"SELECT",  bind:"shopCode", size:1, init:allShops}]},	 		 
	 {items:[{name:"#L.countsDate", type:"LABEL" , value:"銷售日期"}]},
	 {items:[{name:"#F.startSalesDate", type:"DATE", bind:"startCountsDate", size:1},
	         {name:"#L.between", type:"LABEL", value:" 至 "},
	         {name:"#F.endSalesDate", type:"DATE",  bind:"endCountsDate", size:1}]}]}
	 ], 	 
		beginService:"",
		closeService:""			
	});
}

function buttonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'}],
	 	 		//{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			//{name:"#B.update"      , type:"PICKER"   , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doView()"},
	 			//{name:"SPACE"          , type:"LABEL"    , value:"　"},	 		
	 			//{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}],
	 			/*{name:"SPACE"          , type:"LABEL"    , value:"　"},	 		
	 			{name:"#F.selectedAll" , type:"CHECKBOX" , value:"N"},
	 			{name:"#L.selectedAll" , type:"LABEL"    , value:"選擇全部"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},	 		
	 			{name:"#F.clearAll" , type:"CHECKBOX"    , value:"N"},
	 			{name:"#L.clearAll" , type:"LABEL"       , value:"清除全部"}],*/
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){

  var brandCode = document.forms[0]["#loginBrandCode"].value; 
  var salesUnit = "專櫃";
  var visitorCountLabel = "來客數";
  if("T2" == brandCode){
    salesUnit = "機台";
    visitorCountLabel = "交易筆數";
  }
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
  //vat.item.make(vnB_Detail, "checkbox"                  , {type:"XBOX"});
  vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"      });
  vat.item.make(vnB_Detail, "id.shopCode"                  , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:salesUnit});
  vat.item.make(vnB_Detail, "id.salesDate"                 , {type:"TEXT" , size: 8, maxLen:20, mode:"READONLY", desc:"銷售日期"});
  vat.item.make(vnB_Detail, "visitorCount"              , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:visitorCountLabel});
  vat.item.make(vnB_Detail, "totalActualSalesAmount"    , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"營收彙總金額"});
  //vat.item.make(vnB_Detail, "lastUpdateDate"            , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"更新日期"});
  vat.block.pageLayout(vnB_Detail, {
									  id                  : "vatDetailDiv",
									  pageSize            : 10,
									  searchKey           : ["id.shopCode","id.salesDate"],
									  selectionType       : "NONE",
									  indexType           : "AUTO",
								      canGridDelete       : vbCanGridDelete,
									  canGridAppend       : vbCanGridAppend,
									  canGridModify       : vbCanGridModify,	
									  loadBeforeAjxService: "loadBeforeAjxService()",
									  loadSuccessAfter    : "loadSuccessAfter()", 	
									  saveBeforeAjxService: "saveBeforeAjxService()",
									  saveSuccessAfter    : "saveSuccessAfter()"
									 });
}

// 查詢點下執行
function loadBeforeAjxService(){
   
	var processString = "process_object_name=soShopDailyHeadMainService&process_object_method_name=getAJAXSearchPageData" + 
                       "&brandCode=" + document.forms[0]["#loginBrandCode"].value +
                       "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
                       "&shopCode=" + vat.item.getValueByName("#F.shopCode")+ 
                       "&startSalesDate=" + vat.item.getValueByName("#F.startSalesDate") +
                       "&endSalesDate=" + vat.item.getValueByName("#F.endSalesDate");	                   
                                                                            
	return processString;											
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(vnB_Detail).dataCount == vat.block.getGridObject(vnB_Detail).pageSize &&
	    vat.block.getGridObject(vnB_Detail).lastIndex == 1){
		vat.item.setStyleByName("#B.picker" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
		vat.item.setStyleByName("#B.picker" , "display", "inline");
	}
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=soShopDailyHeadMainService&process_object_method_name=saveSearchResult";
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {

}

// 查詢按下後
function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
}



function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}

function resetForm(){
    vat.item.setValueByName("#F.shopCode", "");
    vat.item.setValueByName("#F.countsDate", "");
    vat.item.setValueByName("#F.startSalesDate", "");
    vat.item.setValueByName("#F.endSalesDate", ""); 
    doSearch();
}

// 檢視按下後的動作 
function doClosePicker(){
    vat.block.pageSearch(vnB_Detail, {
        funcSuccess : 
            function(){
    		    vat.block.submit(
    		        function(){ return "process_object_name=soShopDailyAction&process_object_method_name=performSearchSelection";
    		        }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    		}}); 
}