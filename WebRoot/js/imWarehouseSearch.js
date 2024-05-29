
var afterSavePageProcess = "";
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
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     	: document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode	  	: document.forms[0]["#loginEmployeeCode" 	].value,
  	     customsWarehouseCode 	: document.forms[0]["#customsWarehouseCode" 	].value,	
  	     vatPickerId        	: document.forms[0]["#vatPickerId"       	].value
	    };
	    
		vat.bean.init(	
	  		function(){
				return "process_object_name=imWarehouseAction&process_object_method_name=performSearchInitial"; 
	    	},{								
	    		other: true
    	}); 
  }
}

// 可搜尋的欄位
function headerInitial(){
	var allImWarehouseTypes = vat.bean("allImWarehouseTypes");
	var allBuLocations = vat.bean("allBuLocations");
	var allCategoryCodes = [["","","true"],
                 ["專櫃", "倉庫"],
                 ["S","W"]];
    var alltaxTypeCode = [["","","true"],
                 ["完稅及免稅", "免稅", "完稅"],
                 ["M","T","F"]];             
vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"倉庫查詢作業", rows:[  
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.warehouseCode", 		type:"LABEL"  , value:"倉庫代號"}]},
				{items:[{name:"#F.warehouseCode", 		type:"TEXT" , size:10},
						{name:"#F.enable", 				type:"CHECKBOX" },
						{name:"#F.enableName", 			type:"LABEL", value:" 停用? "}]},
				{items:[{name:"#L.warehouseName", 		type:"LABEL", 	value:"倉庫名稱" }]},
				{items:[{name:"#F.warehouseName", 		type:"TEXT", 	size:20 }], td:" colSpan=3"},			
			 	{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", mode:"READONLY" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.storage",				type:"LABEL", 	value:"庫別" }]},
				{items:[{name:"#F.storage",				type:"TEXT",bind:"storage", 	size:10 }]},
				{items:[{name:"#L.storageArea", 		type:"LABEL", 	value:"儲區"}]},
				{items:[{name:"#F.storageArea",			type:"TEXT", 	size:10}]},
				{items:[{name:"#L.storageBin", 			type:"LABEL", 	value:"儲位"}]},
				{items:[{name:"#F.storageBin",			type:"TEXT",	size:10}]},
				{items:[{name:"#L.warehouseManager", 	type:"LABEL", 	value:"倉管人員"}]},
				{items:[{name:"#F.warehouseManager",	type:"TEXT", 	size:10}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseTypeId",			type:"LABEL", 	value:"類別" }]},
				{items:[{name:"#F.warehouseTypeId",			type:"SELECT", 	init:allImWarehouseTypes, size:1 }]},
				{items:[{name:"#L.categoryCode", 			type:"LABEL", 	value:"型態"}]},
				{items:[{name:"#F.categoryCode",			type:"SELECT", 	init:allCategoryCodes, size:1}]},
				{items:[{name:"#L.locationId", 				type:"LABEL", 	value:"地點"}]},
				{items:[{name:"#F.locationId",				type:"SELECT", 	init:allBuLocations, size:1}]},
				{items:[{name:"#L.taxTypeCode", 			type:"LABEL", 	value:"稅別"}]},
				{items:[{name:"#F.taxTypeCode",				type:"SELECT", 	init:alltaxTypeCode,size:1}]}
			]}
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
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:""},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.update"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "checked"         	, {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"         	, {type:"IDX"  ,                     desc:"序號"      });
    vat.item.make(vnB_Detail, "warehouseId"         , {type:"hidden"  ,                     desc:"庫別代號"      });
	vat.item.make(vnB_Detail, "warehouseCode"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"庫別"      });
	vat.item.make(vnB_Detail, "warehouseName"  		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"名稱"      });
	vat.item.make(vnB_Detail, "warehouseTypeName"	, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"類別"      });
	vat.item.make(vnB_Detail, "storage"     		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"庫號"      });
	vat.item.make(vnB_Detail, "storageArea"     	, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"儲區"      });
	vat.item.make(vnB_Detail, "storageBin"     		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"儲位"      });
	vat.item.make(vnB_Detail, "categoryName"     	, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"型態"      });
	vat.item.make(vnB_Detail, "locationName"     	, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"地點"      });
	vat.item.make(vnB_Detail, "taxTypeName"     	, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"稅別"      });
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["warehouseId","warehouseCode"],
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
		vat.item.setStyleByName("#B.update" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.update" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
   //alert("loadBeforeAjxService");	
   
	var processString = "process_object_name=imWarehouseService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&warehouseCode"         	+ "=" + vat.item.getValueByName("#F.warehouseCode"     ) +
	                  "&enable"    				+ "=" + vat.item.getValueByName("#F.enable"       ) +
	                  "&warehouseName"    		+ "=" + vat.item.getValueByName("#F.warehouseName"       ) +
	                  "&storage"    			+ "=" + vat.item.getValueByName("#F.storage"       ) +
	                  "&storageArea"    		+ "=" + vat.item.getValueByName("#F.storageArea"   ) +
	                  "&storageBin"    			+ "=" + vat.item.getValueByName("#F.storageBin"   ) +
	                  "&warehouseTypeId"    	+ "=" + vat.item.getValueByName("#F.warehouseTypeId"   ) +    
	                  "&categoryCode"    		+ "=" + vat.item.getValueByName("#F.categoryCode"   ) +  
	                  "&locationId"    			+ "=" + vat.item.getValueByName("#F.locationId"   ) +
	                  "&taxTypeCode"    		+ "=" + vat.item.getValueByName("#F.taxTypeCode"   ) +
	                  "&warehouseManager"       + "=" + vat.item.getValueByName("#F.warehouseManager"        ); 
	                  
                                                                            
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
	//alert("saveBeforeAjxService");
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=imWarehouseService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
//alert("doClosePicker");
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imWarehouseService&process_object_method_name=getSearchSelection";
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