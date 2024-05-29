/*** 
 *	檔案: imItemSearch.js
 *	說明：商品資料查詢作業查詢
 *	修改：Joe
 *  <pre>
 *  	Created by Joe
 *  	All rights reserved.
 *  </pre>
 */
 
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Detail = 2;

var sqlString = "clala";

function kweImBlock(){
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();
  kweImDetail();
  
  doFormAccessControl();
}

function kweImSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value,
  	     userType 			: document.forms[0]["#userType"  ].value  
	    };
	    vat.bean.init(	
	  		function(){
				return "process_object_name=imItemService&process_object_method_name=executeSearchInitial"; 
	    	},{
	    		other: true
    	});
  }
}

function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-left-width: 0px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.export"	   , type:"IMG"      ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"matchExport"	   , type:"LABEL"    ,value:"　"}
	 			//{name:"#B.view"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 4px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function kweImHeader(){ 
    var selectYorN =       [["", true, false], ["啟用", "停用"], ["Y", "N"]];
    var logical    =       [["", true, false], ["AND", "OR"], ["and", "or"]];
    var condition  =       [["", true, false], ["like", "=", ">", "<", ">=", "<=", "<>"], ["like", "=", ">", "<", ">=", "<=", "<>"]];
    var allCategory01 = vat.bean("allCategory01");
    var allCategory02 = vat.bean("allCategory02");
    var allCategory03 = vat.bean("allCategory03");
    
    var brandCode = vat.bean().vatBeanOther.loginBrandCode;
    var merage = " colSpan=3";
    var itemBrand = "";
    var isHiddenItemBrand = "HIDDEN";
    if( brandCode.indexOf("T2") > -1 ){
    	merage = ""; 
    	itemBrand = "商品品牌";
    	isHiddenItemBrand = "";
    }
    
    var allCategoryItem =  [["", true, false], [vat.bean("category04CName"),vat.bean("category05CName"),vat.bean("category06CName"),
                            vat.bean("category07CName"),vat.bean("category08CName"),vat.bean("category09CName"),vat.bean("category10CName"),
                            vat.bean("category11CName"),vat.bean("category12CName"),vat.bean("category13CName"),vat.bean("category14CName"),
                            vat.bean("category15CName"),vat.bean("category16CName"),vat.bean("category17CName"),vat.bean("category18CName"),
                            vat.bean("category19CName") ], ["category04","category05","category06","category07","category08","category09",
                            "category10","category11","category12","category13","category14","category15","category16","category17",
                            "category18","category19","category20"]];
    
	vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='0'",
	title:"商品資料查詢作業", rows:[  
	 		{row_style:"", cols:[
	 		     {items:[{name:"#L.itemCode", 			type:"LABEL", 	value:"商品品號"}]},
				 {items:[{name:"#F.startItemCode", 		type:"TEXT", 	bind:"startItemCode", size:12 , eChange:"changeItemCode()"},
				         //{name:"#L.itemCodeTo", 		type:"LABEL", 	value:" 至 "},
	 		             {name:"#F.endItemCode", 		type:"TEXT", 	bind:"endItemCode", size:12,mode:"HIDDEN" },
	 		             {name:"#I.append",					type:"IMG" , value:"新增", src:"./images/16x16/Add.png", eClick: function(){ changeAppend();} },
						{name:"#I.decrease",				type:"IMG" , value:"減少", src:"./images/16x16/Remove.png", eClick: function(){ changeDecrease();} },
						{name:"#L.br", 						type:"LABEL", 	value:"<br>" },
						{name:"#F.itemCodes",		type:"TEXTAREA" ,  bind:"itemCodes", mode:"READONLY", row:1, col: 20}]}, 
	 		     {items:[{name:"#L.itemName", 			type:"LABEL", 	value:"商品名稱"}]},
				 {items:[{name:"#F.itemName", 			type:"TEXT", 	bind:"itemName", size:25}]},        
	 		     {items:[{name:"#L.enable", 			type:"LABEL", 	value:"狀態"}]},
				 {items:[{name:"#F.enable", 			type:"SELECT", 	bind:"enable",init:selectYorN, size:10}]},
				 {items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"},
				         {name:"#F.brandCode", 			type:"hidden", 	bind:"brandCode"}]},
				 {items:[{name:"#F.itemBrandName", 		type:"TEXT", 	bind:"itemBrandName",mode:"READONLY", size:10}]}
			]},
		   {row_style:"", cols:[
	 		     {items:[{name:"#L.category17", 		type:"LABEL", 	value:"製造商/供應商"}]},
				 {items:[{name:"#F.category17", 		type:"TEXT", 	bind:"category17", size:12}]},
				 {items:[{name:"#L.category13", 		type:"LABEL", 	value:"系列"}]},
				 {items:[{name:"#F.category13", 		type:"TEXT", 	bind:"category13", size:12}]},
	 		     {items:[{name:"#L.supplierItemCode", 	type:"LABEL", 	value:"原廠貨號"}]},
				 {items:[{name:"#F.supplierItemCode", 	type:"TEXT", 	bind:"supplierItemCode", maxLen:40, size:12}]},
				 {items:[{name:"#L.itemLevel", 			type:"LABEL", 	value:"商品等級"}]},
				 {items:[{name:"#F.itemLevel", 			type:"TEXT", 	bind:"itemLevel", size:12}]}
			]},
		   {row_style:"", cols:[
	 		     {items:[{name:"#L.category01", 		type:"LABEL", 	value:"大類"}]},
				 {items:[{name:"#F.category01", 		type:"SELECT", 	bind:"category01",init:allCategory01, eChange:function(){changeCategory("01", "02");}, size:25}]},
	 		     {items:[{name:"#L.category02", 		type:"LABEL", 	value:"中類"}]},
				 {items:[{name:"#F.category02", 		type:"SELECT", 	bind:"category02",init:allCategory02, eChange:function(){changeCategory("02", "03");}, size:10}]},
				 {items:[{name:"#L.category03", 		type:"LABEL", 	value:"小類"}]},
				 {items:[{name:"#F.category03", 		type:"SELECT", 	bind:"category03",init:allCategory03}]},
				 {items:[{name:"#L.itemBrand", 		type:"LABEL", 	value:itemBrand}]}, //),  getColumn(brandCode, 
				 {items:[{name:"#F.itemBrand", 		type:"TEXT", 	bind:"itemBrand", size:12, mode:isHiddenItemBrand}]}  //)  getColumn(brandCode, 
			]},
		   {row_style:( brandCode.indexOf("T2") > -1 ? "" : " style= 'display:none;'"), cols:[ 
		   		 {items:[{name:"#L.itemEName", 			type:"LABEL", 	value:"其他品名"}]},
				 {items:[{name:"#F.itemEName", 			type:"TEXT", 	bind:"itemEName", size:12} ]}, 
	 		     {items:[{name:"#L.category05", 		type:"LABEL", 	value:"年份"}]},
				 {items:[{name:"#F.category05", 		type:"SELECT", 	bind:"category05",init:vat.bean("allCategory05")}]},
	 		     {items:[{name:"#L.category06", 		type:"LABEL", 	value:"季別"}]},
				 {items:[{name:"#F.category06", 		type:"SELECT", 	bind:"category06",init:vat.bean("allCategory06")}]},
				 {items:[{name:"#L.category11", 		type:"LABEL", 	value:"款式編號"}]},
				 {items:[{name:"#F.category11", 		type:"TEXT", 	bind:"category11", size:12} ]}
			]},
		   {row_style:( brandCode.indexOf("T2") > -1 ? "" : " style= 'display:none;'"), cols:[ 
	 		     {items:[{name:"#L.foreignCategory", 	type:"LABEL", 	value:"國外類別"}]},
				 {items:[{name:"#F.foreignCategory", 	type:"TEXT", 	bind:"foreignCategory" }],td:"colSpan=7"}
			]}
			/*,
			{row_style:" style='display:none'", cols:[
	 		     {items:[{name:"#L.advQuery", 			type:"LABEL", 	value:"進階查詢"}]},
				 {items:[{name:"#F.logicalOperator", 	type:"SELECT", 	bind:"logicalOperator",init:logical},
				         {name:"#F.categoryType", 		type:"SELECT", 	bind:"categoryType",init:allCategoryItem},
				         {name:"#F.operator", 			type:"SELECT", 	bind:"operator",init:condition},
				         {name:"#F.categoryValue", 		type:"TEXT", 	bind:"categoryValue"} ]}
			]},
			{row_style:" style='display:none'", cols:[
	 		     {items:[{name:"#L.findString", 		type:"LABEL", 	value:"查詢條件"}]},
				 {items:[{name:"#F.findString", 		type:"TEXT", 	bind:"findString", size:25 , mode:"READONLY"}],td:"colSpan=5"}
			]},
			{row_style:" style='display:none'", cols:[
	 		     {items:[{name:"#L.sql", 				type:"LABEL", 	value:"SQL"}]},
				 {items:[{name:"#F.sql", 				type:"TEXT", 	bind:"sqlString", size:50}],td:"colSpan=5"}
	 		]}*/
		 ], 	 
		beginService:"",
		closeService:""			
	});
}

function kweImDetail(){
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	var itemEName = "英文名稱";
	if( brandCode.indexOf("T2") > -1){
		itemEName = "其他品名";
	}

    var allCategory01 = vat.bean("allCategory01");
    var allCategory02 = vat.bean("allCategory02");
  
	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;

	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
    var vbSelectionType = "CHECK";
       	
	if(vatPickerId != null && vatPickerId != ""){
         vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
         vbSelectionType = "CHECK";    
    }else{
         vbSelectionType = "NONE";
    }
    
    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"});
	vat.item.make(vnB_Detail, "itemCode"        , {type:"TEXT" , size:15, view:"fixed", mode:"READONLY", desc:"品號"      });
	vat.item.make(vnB_Detail, "itemCName"       , {type:"TEXT" , size:18,alter:true, view:"", mode:"READONLY", desc:"中文名稱"      });
	vat.item.make(vnB_Detail, "itemEName"       , {type:"TEXT" , size:18,alter:true, view:"", mode:"READONLY", desc:itemEName      });
	vat.item.make(vnB_Detail, "category01"      , {type:"SELECT" , size:18, view:"", mode:"READONLY", desc:"大類 " ,init:allCategory01      });
	vat.item.make(vnB_Detail, "category02"      , {type:"SELECT"  , size:18, view:"", mode:"READONLY", desc:"中類",init:allCategory02      });
	vat.item.make(vnB_Detail, "supplierItemCode", {type:"TEXT"  , size:14, view:"", mode:"READONLY", desc:"原廠貨號"      });
//	vat.item.make(vnB_Detail, "itemLevel"       , {type:"TEXT"  , size:6, view:"", mode:"READONLY", desc:"商品等級"      });
	vat.item.make(vnB_Detail, "enableName"      , {type:"TEXT"  , size:4, view:"", view:"", mode:"READONLY", desc:"狀態"      });
	if( brandCode.indexOf("T2") > -1){
		vat.item.make(vnB_Detail, "unitPrice"      , {type:"NUMB"  , size:8, view:"shift", mode:"READONLY", desc:"售價"      });
		vat.item.make(vnB_Detail, "salesUnit"      , {type:"TEXT"  , size:6, view:"shift", mode:"READONLY", desc:"交易單位"      });
		vat.item.make(vnB_Detail, "purchaseUnit"   , {type:"TEXT"  , size:6, view:"shift", mode:"READONLY", desc:"進貨單位"      });
		vat.item.make(vnB_Detail, "priceLastUpdateDate"   , {type:"DATE", size:4, view:"shift", mode:"READONLY", desc:"最近調價日"      });
		vat.item.make(vnB_Detail, "itemBrand"      , {type:"TEXT"  , size:8, view:"shift", mode:"READONLY", desc:"商品品牌"      });
		vat.item.make(vnB_Detail, "itemBrandName"  , {type:"TEXT"  , size:18, view:"shift", mode:"READONLY", desc:"品牌名稱"      });
		vat.item.make(vnB_Detail, "category17"   , {type:"TEXT"  , size:12, view:"shift", mode:"READONLY", desc:"廠商代碼"      });
		vat.item.make(vnB_Detail, "supplierName"   , {type:"TEXT"  , size:18, view:"shift", mode:"READONLY",alter:true, desc:"廠商名稱"      });
		vat.item.make(vnB_Detail, "isTax"   	   , {type:"TEXT"  , size:4, view:"shift", mode:"READONLY", desc:"稅別"      });
    }
	vat.item.make(vnB_Detail, "itemId"          , {type:"ROWID"});
	
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["itemId"],
//														pickAllService		: function (){return selectAll();},
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()",						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
//														blockId             : "2"
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);

}

// 全選
function selectAll(){
//  alert("selectAll");
  var processString = "process_object_name=imItemService&process_object_method_name=updateAllSearchData";
  return processString;
  
}

function saveSuccessAfter(){
	 //alert("更新成功");
}

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

function loadBeforeAjxService(){
	
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	var condition = "";
	if( brandCode.indexOf("T2") > -1){ 
		condition = "&itemBrand=" + vat.item.getValueByName("#F.itemBrand") + "&category05=" + vat.item.getValueByName("#F.category05") +
					"&category06=" + vat.item.getValueByName("#F.category06")+ "&itemEName=" + vat.item.getValueByName("#F.itemEName")+
					"&category11=" + vat.item.getValueByName("#F.category11")+ "&foreignCategory=" + vat.item.getValueByName("#F.foreignCategory");
	}
	
	var processString = "process_object_name=imItemService&process_object_method_name=getAJAXSearchPageData" + 
                      "&brandCode" 			+ "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&startItemCode"     	+ "=" + vat.item.getValueByName("#F.startItemCode"   ) +     
	                  "&endItemCode"   		+ "=" + vat.item.getValueByName("#F.endItemCode" ) +     
	                  "&itemCodes"   		+ "=" + vat.item.getValueByName("#F.itemCodes" ) +
                      "&enable"   			+ "=" + vat.item.getValueByName("#F.enable"         ) +
                      "&itemName"   		+ "=" + vat.item.getValueByName("#F.itemName"         ) +
                      "&supplierItemCode"   + "=" + vat.item.getValueByName("#F.supplierItemCode"         ) +
                      "&itemLevel"   		+ "=" + vat.item.getValueByName("#F.itemLevel"         ) +
                      "&category01="	   + vat.item.getValueByName("#F.category01"         ) +
                      "&category02="	   + vat.item.getValueByName("#F.category02"         ) + 
                      "&category03="	   + vat.item.getValueByName("#F.category03"         ) +
                      "&category13="       + vat.item.getValueByName("#F.category13"         ) +
                      "&category17="       + vat.item.getValueByName("#F.category17"         ) + condition;
                                                                            
	//alert(processString);
	return processString;											
}



//	判斷是否要關閉LINE

function checkEnableLine() {
	return true;
}


//	取得SAVE要執行的JS FUNCTION
function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=imItemService&process_object_method_name=saveSearchResult"+
						"&brandCode="+vat.bean().vatBeanOther.loginBrandCode;
	}
	return processString;
}								

function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope; }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });			                   
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.startItemCode", "");
    vat.item.setValueByName("#F.enable", "Y");
    vat.item.setValueByName("#F.itemName", "");
    vat.item.setValueByName("#F.supplierItemCode", "");
    vat.item.setValueByName("#F.itemLevel", "");
    vat.item.setValueByName("#F.category01", "");
    vat.item.setValueByName("#F.category02", "");
    vat.item.setValueByName("#F.category03", "");
    
    if(vat.bean().vatBeanOther.loginBrandCode.indexOf("T2") > -1){
    	vat.item.setValueByName("#F.itemBrand", "");
    	vat.item.setValueByName("#F.category05", "");
    	vat.item.setValueByName("#F.category06", "");
    	vat.item.setValueByName("#F.category11", "");
    	vat.item.setValueByName("#F.foreignCategory", "");
    	vat.item.setValueByName("#F.itemEName", "");
    }
    vat.item.setValueByName("#F.itemCodes", "");
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}

function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imItemService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
				
}

function changeItemCode(){
	var SItemCode = vat.item.getValueByName("#F.startItemCode").replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setValueByName("#F.startItemCode" , SItemCode)
	var EItemCode = vat.item.getValueByName("#F.endItemCode").replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setValueByName("#F.endItemCode" , EItemCode)
}

// 取得指定連動的類別下拉
function changeCategory(parentCategoryType, toggleCategoryType){
	var parentCategory = vat.item.getValueByName("#F.category"+parentCategoryType);
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
 
    vat.ajax.XHRequest(
    {
        post:"process_object_name=imItemCategoryService"+
                 "&process_object_method_name=getAJAXCategory"+
                 "&category=" + parentCategory +
                 "&categoryType=CATEGORY" + toggleCategoryType +
                 "&brandCode=" + brandCode,
        find: function changeRequestSuccess(oXHR){ 
	var allCategory = eval(vat.ajax.getValue("allCategory", oXHR.responseText));
        		allCategory[0][0] = "#F.category"+toggleCategoryType;
	vat.item.SelectBind(allCategory); 
        }   
    });
} 	 	

function getColumn(brandCode, col){
	if( brandCode.indexOf("T2") > -1 ){
		return col;
	}else{
		return {};
	} 
}

// 開啟視窗
function openModifyPage(){

    var nItemLine = vat.item.getGridLine();

    var vFormId = vat.item.getGridValueByName("itemId", nItemLine);
	var userType = vat.bean().vatBeanOther.userType;
	if(!(vFormId == "" || vFormId == "0")){
    var url = "/erp/Im_Item:create:20091106.page?formId=" + vFormId + "&isOppositePicker=true"; 
	
	if("ITEM"==userType){
		url = url + "&userType="+userType;
	}

     sc=window.open(url, '商品主檔維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
	}
}

//sql明細匯出excel
function doExport(){
	var condition = "";
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	if( brandCode.indexOf("T2") > -1){
		condition = "&itemBrand=" + vat.item.getValueByName("#F.itemBrand") + "&category05=" + vat.item.getValueByName("#F.category05") +
					"&category06=" + vat.item.getValueByName("#F.category06") + "&itemEName=" + vat.item.getValueByName("#F.itemEName")+
					"&category11=" + vat.item.getValueByName("#F.category11")+ "&foreignCategory=" + vat.item.getValueByName("#F.foreignCategory");;
	}

    var url = "/erp/jsp/ExportFormView.jsp" + 
              "?exportBeanName=IM_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=imItemService" + 
              "&processObjectMethodName=getAJAXExportData" + 
              "&brandCode" 			+ "=" + vat.bean().vatBeanOther.loginBrandCode +     
	          "&startItemCode"     	+ "=" + vat.item.getValueByName("#F.startItemCode"   ) +     
	          "&endItemCode"   		+ "=" + vat.item.getValueByName("#F.endItemCode" ) +     
	          "&itemCodes"   		+ "=" + vat.item.getValueByName("#F.itemCodes" ) +
              "&enable"   			+ "=" + vat.item.getValueByName("#F.enable"         ) +
              "&itemName"   		+ "=" + vat.item.getValueByName("#F.itemName"         ) +
              "&supplierItemCode"   + "=" + vat.item.getValueByName("#F.supplierItemCode"         ) +
              "&itemLevel"   		+ "=" + vat.item.getValueByName("#F.itemLevel"         ) +
              "&category01="	   + vat.item.getValueByName("#F.category01"         ) +
              "&category02="	   + vat.item.getValueByName("#F.category02"         ) + 
              "&category03="	   + vat.item.getValueByName("#F.category03"         ) +
              "&category13="       + vat.item.getValueByName("#F.category13"         ) +
              "&category17="       + vat.item.getValueByName("#F.category17"         ) + condition;
              
    var width = "200";
    var height = "30";  
    window.open(url, '商品主檔匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

}

// 鎖欄位
function doFormAccessControl(){
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	if( brandCode.indexOf("T2") <= -1){
		vat.item.setStyleByName("#B.export", 		"display", "none");
	}
}

// 新增多品號查詢條件
function changeAppend(){
	var startItemCode = vat.item.getValueByName("#F.startItemCode").replace(/^\s+|\s+$/, '');
	if("" != startItemCode){
		vat.item.setValueByName("#F.startItemCode", "");
		var itemCodes = vat.item.getValueByName("#F.itemCodes");
		var count = itemCodes.split(","); 
		if( "" == itemCodes && count.length == 1){
			itemCodes = itemCodes + startItemCode;
		}else{
			itemCodes = itemCodes + "," + startItemCode;
		}
		vat.item.setValueByName("#F.itemCodes", itemCodes);
	}
}
// 減少多品號查詢條件
function changeDecrease(){
	var startItemCode = vat.item.getValueByName("#F.startItemCode").replace(/^\s+|\s+$/, '');
	if("" != startItemCode){
		vat.item.setValueByName("#F.startItemCode", "");
		var itemCodes = vat.item.getValueByName("#F.itemCodes");
		
		var array = itemCodes.split(",");
		for(var i = 0; i < array.length; i++){
			if(array[i] == startItemCode){
				array.splice(i,1);
				break;
			}
		}
		vat.item.setValueByName("#F.itemCodes", array);
	}
}