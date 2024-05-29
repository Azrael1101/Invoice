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
		return "process_object_name=soShopDailyAction&process_object_method_name=performMaintainInitial"; 
      },{other: true});
  }
       vat.item.bindAll();
}

function headerInitial(){
var brandCode = document.forms[0]["#loginBrandCode"].value; 
var allShops = vat.bean("allShops");
var lastSalesDate = vat.bean("lastSalesDate");

if(document.forms[0]["#loginBrandCode"].value!=='T2')
{

	var allBatch = [["", "", ""], [ "99"],  [ "99"]];
}else
{
var allBatch = vat.bean("allBatch");
}
var allIncharge = [[true, true, true], [ "桃園","馬祖南竿","高雄","馬祖福澳"],  [ "T2F03","T2F04","T2F05","T2F08"]];
var salesUnit = "專櫃";
if("T2" == brandCode){
    salesUnit = "機台";
}
                 
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"POS每日資料維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.shopCode", type:"LABEL", value:salesUnit}]},	 
	 {items:[{name:"#F.shopCode", type:"SELECT",  bind:"shopCode", size:1, init:allShops}]},	 		 
	 {items:[{name:"#L.countsDate", type:"LABEL" , value:"銷售日期"}]},
	 {items:[{name:"#F.startSalesDate", type:"DATE", bind:"startSalesDate", size:1, init:lastSalesDate},
	         {name:"#L.between", type:"LABEL", value:" 至 "},
	         {name:"#F.endSalesDate", type:"DATE",  bind:"endSalesDate", size:1, init:lastSalesDate}]},
	 {items:[{name:"#L.batch", type:"LABEL", value:"班次"}]},	 
	 {items:[{name:"#F.batch", type:"SELECT",  bind:"batch", size:1,init:allBatch}]},  
	 {items:[{name:"#L.incharge", type:"LABEL", value:"地區"}]},	 
	 {items:[{name:"#F.incharge", type:"SELECT",  bind:"incharge", size:1,init:allIncharge}]},   
	 {items:[{name:"#L.totalActualSalesAmount", type:"LABEL", value:"總營收彙總筆數"}]},
	    {items:[{name:"#F.totalActualSalesCount", type:"NUMM", mode:"READONLY", size:12}]},   
	 {items:[{name:"#L.totalActualSalesAmount", type:"LABEL", value:"總營收彙總金額"}]},	 
	 {items:[{name:"#F.totalActualSalesAmount", type:"NUMM", mode:"READONLY", size:12}]}]}
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
	 	{items:[
	 	        {name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createDailyData()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 	        {name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.save"        , type:"IMG"      , value:"存檔",  src:"./images/button_save_data.gif"  , eClick:"doSaveHandler()"}],
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
  //vat.item.make(vnB_Detail, "checkbox"                , {type:"XBOX"});
  vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"      });
  vat.item.make(vnB_Detail, "id.shopCode"               , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:salesUnit});
  vat.item.make(vnB_Detail, "id.salesDate"              , {type:"TEXT" , size: 8, maxLen:20, mode:"READONLY", desc:"銷售日期"});
  vat.item.make(vnB_Detail, "visitorCount"              , {type:"NUMM"  , size:12, maxLen:8, desc:visitorCountLabel});
  vat.item.make(vnB_Detail, "actualSalesAmount"         , {type:"NUMM"  , size:12, maxLen:8, desc:"營收彙總金額"});
  vat.item.make(vnB_Detail, "lastUpdatedBy"             , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"更新人員"});
  vat.item.make(vnB_Detail, "lastUpdateTime"            , {type:"TEXT" , size:20, maxLen:15, mode:"READONLY", desc:"更新日期"});
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
                       "&endSalesDate=" + vat.item.getValueByName("#F.endSalesDate")+
                       "&incharge=" + vat.item.getValueByName("#F.incharge");	                   
                                                                            
	return processString;											
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(vnB_Detail).dataCount == vat.block.getGridObject(vnB_Detail).pageSize &&
	    vat.block.getGridObject(vnB_Detail).lastIndex == 1){
		vat.item.setValueByName("#F.totalActualSalesAmount"	,0);
		alert("您輸入條件查無資料");
	}else{ 
			vat.ajax.XHRequest({ 
				post:"process_object_name=soShopDailyHeadMainService&process_object_method_name=getTotalAccount" + 
		                "&brandCode=" + document.forms[0]["#loginBrandCode"].value +
                       "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
                       "&shopCode=" + vat.item.getValueByName("#F.shopCode")+ 
                       "&startSalesDate=" + vat.item.getValueByName("#F.startSalesDate") +
                       "&batch=" + vat.item.getValueByName("#F.batch") +
                       "&endSalesDate=" + vat.item.getValueByName("#F.endSalesDate"),
	            asyn:false,                      
				find: function change(oXHR){
				if(vat.ajax.getValue("TotalActualSalesAmount", oXHR.responseText) != "")
					vat.item.setValueByName("#F.totalActualSalesAmount"	,vat.ajax.getValue("TotalActualSalesAmount", oXHR.responseText));
	           	else
	           		vat.item.setValueByName("#F.totalActualSalesAmount"	,0);
	           		
	           	if(vat.ajax.getValue("TotalActualSalesCount", oXHR.responseText) != "")
					vat.item.setValueByName("#F.totalActualSalesCount"	,vat.ajax.getValue("TotalActualSalesCount", oXHR.responseText));
	           	else
	           		vat.item.setValueByName("#F.totalActualSalesCount"	,0);
	           	}
			});
	}
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=soShopDailyHeadMainService&process_object_method_name=updateAJAXPageLinesData&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value;
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
    //alert("存檔成功！");
}

// 建立每日資料按下後
function createDailyData(){

    vat.block.submit(function(){return "process_object_name=soShopDailyAction"+
			                    "&process_object_method_name=performMaintainSearch";}, 
			                    {bind:true, other: true});			                    	                
}

// 查詢按下後
function doSearch(){
   vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
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
    formDataInitial();
    vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);  
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

/*
   SAVE DATA
*/
function doSaveHandler() {   
    vat.block.pageSearch(vnB_Detail, {
        funcSuccess : 
            function(){alert("存檔成功！");}
    }); 
}