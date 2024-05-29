/*** 
 *	檔案: BuShop.js
 *	說明: 抽成率搜尋維護
 */
var afterSavePageProcess = "";
var vnB_Detail = 2;
var vnB_Header = 1;
function outlineBlock(){
 	
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();
  doFormAccessControl();
  
  
  
}

// 搜尋初始化 
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,	
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"       	].value,
  	     userType	        : document.forms[0]["#userType"      	 	].value
	    };
	    vat.bean.init(	
	  	function()
	  	{
				return "process_object_name=poEosAction&process_object_method_name=performSearchInitial"; 
	   	},
	   	{
	    	other: true
	    });
  }
}
// 可搜尋的欄位
function headerInitial(){
	var allOrderTypes=vat.bean("allOrderTypes");
	var allShop = vat.bean("allShop");
	var allWarehouse = [["","","true"],
	                 ["F9900","P9900","D6200"],
	                 ["F9900","P9900","D6200"]];
	var allStatus = [["","","true"],
	                 ["暫存中","待出貨","已出貨","作廢"],
	                 ["SAVE","SIGNING","FINISH","VOID"]];          
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"EOS單查詢作業", rows:[  
		 {row_style:"", cols:[
		 {items:[{name:"#L.brandCode"                , type:"LABEL"  , value:"品牌"}]},	 
		 {items:[{name:"#F.brandCode"                , type:"TEXT"   ,  bind:"brandCode", size:6, mode:"READONLY"}]},
		 {items:[{name:"#L.orderTypeCode"            , type:"LABEL"  , value:"單別"}]},	 
		 {items:[{name:"#F.orderTypeCode"            , type:"TEXT" ,  bind:"orderTypeCode", mode:"READONLY", size:1}]},		 
		 {items:[{name:"#L.status"                   , type:"LABEL"  , value:"狀態"}]},	 		 
		 {items:[{name:"#F.status"                   , type:"SELECT" ,  bind:"status", size:12, init:allStatus},
		 {name:"#F.userType"                 , type:"TEXT" ,  bind:"userType", size:12, mode:"HIDDEN"}]},
 		 {items:[{name:"#L.shopCode"    			 , type:"LABEL" , value:"需求店別"}]},	 
		 {items:[{name:"#F.shopCode"    			 , type:"SELECT",  bind:"shopCode", size:20, init:allShop}]}]},
 
		 {row_style:"", cols:[
 
		 {items:[{name:"#L.orderNo"                  , type:"LABEL"  , value:"單號"}]},
		 {items:[{name:"#F.startOrderNo"             , type:"TEXT"   ,  bind:"startOrderNo", size:20},
				 {name:"#L.between"                  		 , type:"LABEL"  , value:"至"},
		 		 {name:"#F.endOrderNo"               		 , type:"TEXT"   ,  bind:"endOrderNo"  , size:20}], td:" colSpan=3"},
		 {items:[{name:"#L.requestTimeScope"		 , type:"LABEL" , value:"需求日期"}]},
	 	 {items:[{name:"#F.startDate"       		 , type:"DATE"  ,  bind:"startDate", size:12},
	    		{name:"#L.between"					 , type:"LABEL" , value:"至"},
	    		{name:"#F.endDate"					 , type:"DATE"  ,  bind:"endDate", size:12}], td:" colSpan=3"}]}
	
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
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.view"        , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
	 			],
	 			 td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
                    //刪除的垃圾桶
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

	vat.item.make(vnB_Detail, "checked"         , {type:"XBOX"});
	vat.item.make(vnB_Detail, "indexNo"                 	, {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "headId"             , {type:"TEXT" , size:15, maxLen:20, mode:"HIDDEN", desc:"單頭編號"      });
	vat.item.make(vnB_Detail, "orderTypeCode"             , {type:"TEXT" , size:15, maxLen:20, mode:"HIDDEN", desc:"單別"      });
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "shopCode"     , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"需求店別"   });
	vat.item.make(vnB_Detail, "request"     , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"訂貨人員"   });
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"      });
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId"],
														selectionType       : "CHECK",
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

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.shopCode", "");
	//vat.item.setValueByName("#F.salesWarehouseCode", "");
	vat.item.setValueByName("#F.enable", "");
}


// 查詢點下執行
function loadBeforeAjxService(){

	var processString = "process_object_name=poEosService&process_object_method_name=getAJAXSearchPageData"  
	                  +"&orderTypeCode" 	 + "=" + vat.item.getValueByName("#F.orderTypeCode") 
	                  +"&startOrderNo"       + "=" + vat.item.getValueByName("#F.startOrderNo") 	                  
	                  +"&endOrderNo" 		 + "=" + vat.item.getValueByName("#F.endOrderNo") 
	                  +"&status" 			 + "=" + vat.item.getValueByName("#F.status") 
	                  +"&brandCode"			 + "=" + vat.item.getValueByName("#F.brandCode")
	                  +"&shopCode" 			 + "=" + vat.item.getValueByName("#F.shopCode")
	                  +"&userType1"		 	 + "=" + vat.item.getValueByName("#F.userType")
	                  +"&startDate"			 + "=" + vat.item.getValueByName("#F.startDate")
	                  +"&endDate" 			 + "=" + vat.item.getValueByName("#F.endDate");    
                                                                            
	return processString;											
}

function saveSuccessAfter(){
}

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=poEosService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 

function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=poEosService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
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

function doFormAccessControl(){
	vat.item.setStyleByName("#B.view" , "display", "none");
}
