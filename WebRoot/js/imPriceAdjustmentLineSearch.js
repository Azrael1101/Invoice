
vat.debug.disable();
var afterSavePageProcess = "";


var vnB_Header = 1;
var vnB_Detail = 2;
function outlineBlock(){
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();
}

function searchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     ].value,	 	  
  	     priceStatus      	: document.forms[0]["#priceStatus"     	 ].value,	  
  	     priceType      	: document.forms[0]["#priceType"     	 ].value,
  	     headId				: document.forms[0]["#headId"     	 	 ].value,
  	     exchangeRate		: document.forms[0]["#exchangeRate"      ].value,
  	     ratio				: document.forms[0]["#ratio"     	 	 ].value,
  	     itemCategory		: document.forms[0]["#itemCategory"      ].value,
  	     supplierCode		: document.forms[0]["#supplierCode"      ].value
	    };	
     vat.bean.init(function(){
		return "process_object_name=imPriceAdjustmentMainService&process_object_method_name=executePriceSearchInitial"; 
   		},{other: true});
  }
}

function headerInitial(){ 
	var allPriceTypes = vat.bean("allPriceTypes");
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	
	var allItemCategory = brandCode.indexOf("T2") > -1 ? vat.bean("allItemCategory") : "";
	var allCategory01 = brandCode.indexOf("T2") > -1 ? vat.bean("allCategory01") : "";
	var allBuSupplier = brandCode.indexOf("T2") > -1 ? vat.bean("allBuSupplier") : ""; 
	
	
	var vsRowStyle= vat.bean().vatBeanOther.loginBrandCode.indexOf("T2") <= -1 ? " style= 'display:none;'":"";
		
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"商品資料查詢作業", rows:[  
	 	{row_style:"", cols:[
		 	{items:[{name:"#L.startItemCode", 	type:"LABEL",  	value:"商品品號"}]},
			{items:[{name:"#F.startItemCode", 	type:"TEXT", 	bind:"startItemCode", size:20},
//				 	{name:"#L.between", 		type:"LABEL",  	value:" 至 "},
//		 		 	{name:"#F.endItemCode", 	type:"TEXT", 	bind:"endItemCode", size:20}]},
					{name:"#B.itemCode", type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", 
	 									 			service:"Im_Item:search:20091106.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			serviceAfterPick:function(){doAfterPickerProcess(); } },
	 				{name:"#F.itemId", 		type:"TEXT",  	bind:"itemId", back:false, mode:"HIDDEN"}]},
		 	{items:[{name:"#L.itemName", 		type:"LABEL", 	value:"商品名稱" }]},
			{items:[{name:"#F.itemName", 		type:"TEXT", 	bind:"itemName", size:20 }]},	 	
 		 	{items:[{name:"#L.priceType", 		type:"LABEL", 	value:"價格類別"}]},
		 	{items:[{name:"#F.priceType", 		type:"SELECT", 	bind:"priceType" , mode:"READONLY" ,init:allPriceTypes, size:10}]},		
		 	{items:[{name:"#L.brandName", 		type:"LABEL", 	value:"品牌" }]},
			{items:[{name:"#F.brandName", 		type:"TEXT", 	bind:"brandName", mode:"READONLY" , size:10 }]}	
		 ]},
		{row_style:vsRowStyle, cols:[
			{items:[{name:"#L.itemCategory", 	type:"LABEL", 	value:"業種子類" }]},
			{items:[{name:"#F.itemCategory", 	type:"SELECT", 	bind:"itemCategory", init:allItemCategory, mode:"READONLY", size:20 }]},
			{items:[{name:"#L.category01", 	type:"LABEL", 	value:"大類" }]},
			{items:[{name:"#F.category01", 	type:"SELECT", 	bind:"category01", eChange:changeCategory02, init:allCategory01, size:1 }]},
			{items:[{name:"#L.category02", 	type:"LABEL", 	value:"中類" }]},
			{items:[{name:"#F.category02", 	type:"SELECT", 	bind:"category02", size:1 }]},
			{items:[{name:"#L.itemBrand", 		type:"LABEL", 	value:"商品品牌" }]},
			{items:[{name:"#F.itemBrand", 		type:"TEXT", 	bind:"itemBrand", size:20 }]}
		  ]},
		{row_style:vsRowStyle, cols:[
			{items:[{name:"#L.supplierCode", 	type:"LABEL", 	value:"廠商代號" }]},
			{items:[{name:"#F.supplierCode", 	type:"SELECT", 	bind:"supplierCode", init:allBuSupplier, mode:"READONLY", size:20 }],td:"colSpan=7"}
		  ]}
		  
		],  	 
		beginService:"",
		closeService:""			
	});

}


// 取得預期要顯示欄位
function getColumn(brandCode, column){
	if( brandCode.indexOf("T2") > -1){
		return column;
	}else{
		return {};
	}
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
	 			{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
	 			
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
  var brandCode = vat.bean().vatBeanOther.loginBrandCode;
  var orderTypeCode = vat.bean().vatBeanOther.orderTypeCode;
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "checkbox"                , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"                 , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "itemCode"             	, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"品號"      });
	vat.item.make(vnB_Detail, "itemCName"               , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"中文名稱"      });
	vat.item.make(vnB_Detail, "itemEName"             	, {type:"TEXT" , size: 20, maxLen:20, mode:"READONLY", desc:"英文名稱"   });
	vat.item.make(vnB_Detail, "itemUnit"               	, {type:"TEXT" , size:8, maxLen:8, mode:"READONLY", desc:"庫存單位"   });
	vat.item.make(vnB_Detail, "priceType"              	, {type:"NUMB" , size:12, maxLen:12, mode:"READONLY", desc:"價格類別"   }); 
	
	if( brandCode.indexOf("T2") > -1 ){
		vat.item.make(vnB_Detail, "purchaseAmount"	, {type:"NUMB" , size:12, maxLen:12, mode:"READONLY", desc:"成本"   });
	}else {
		vat.item.make(vnB_Detail, "supplierQuotationPrice"	, {type:"NUMB" , size:12, maxLen:12, mode:"READONLY", desc:"成本"   });
	}
	
	 
	
	
	vat.item.make(vnB_Detail, "unitPrice"               , {type:"NUMB" , size:12, maxLen:12, mode:"READONLY", desc:"價格"   }); 
	
	if("PAP" == orderTypeCode ){
		vat.item.make(vnB_Detail, "priceEnableName"   	, {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"      });
	}else if("PAJ" == orderTypeCode){
		vat.item.make(vnB_Detail, "beginDate"         	, {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"啟用日期"      });
	}
	vat.item.make(vnB_Detail, "priceId"                  , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["priceId"],
														pickAllService		: function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()", 
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter 	: "saveSuccessAfter()",
														blockId             : "2"
														});
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.picker" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.picker" , "display", "inline");
	}
}

function saveSuccessAfter(){
		
}

function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
    var brandCode = vat.bean().vatBeanOther.loginBrandCode;
    var condition = "";
    if(brandCode.indexOf("T2") > -1){
    	condition = "&itemBrand=" + vat.item.getValueByName("#F.itemBrand") +   
    				"&category01=" + vat.item.getValueByName("#F.category01")+
    				"&category02=" + vat.item.getValueByName("#F.category02")+
    				"&itemCategory=" + vat.item.getValueByName("#F.itemCategory");   
    }
    
	var processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXLineSearchPageData" + 
                      "&loginBrandCode=" + brandCode +  
                      "&orderTypeCode=" + vat.bean().vatBeanOther.orderTypeCode +     
	                  "&startItemCode=" + vat.item.getValueByName("#F.startItemCode"         ) +     
//	                  "&endItemCode=" + vat.item.getValueByName("#F.endItemCode"           ) + 
	                  "&itemName=" + vat.item.getValueByName("#F.itemName"           ) +   
	                  "&priceType=" + vat.item.getValueByName("#F.priceType"    ) + condition +
	                  "&priceStatus=" + vat.bean().vatBeanOther.priceStatus + 
	                  "&supplierCode=" + vat.item.getValueByName("#F.supplierCode");                                                      
	//alert(	processString);
	return processString;											
}


/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	return true;
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=saveLineSearchResult" +
		"&orderTypeCode=" + vat.bean().vatBeanOther.orderTypeCode+
		"&brandCode=" + vat.bean().vatBeanOther.loginBrandCode;
	}
	
	return processString;
}								


function doSearch(){
    //alert("searchService");	
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+ 
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope;  }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);} 
			                    });
}

function doClosePicker(){
    //alert("doClosePicker");
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imPriceAdjustmentMainService&process_object_method_name=updateSearchSelection";
    		                               }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
  	window.top.close();
  }  	
    	
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.startItemCode", "");
    vat.item.setValueByName("#F.endItemCode", "");
    vat.item.setValueByName("#F.itemName", "");
    vat.item.setValueByName("#F.itemId", "");
    var brandCode = vat.bean().vatBeanOther.loginBrandCode;
    if( brandCode.indexOf("T2") > -1){
    	 vat.item.setValueByName("#F.category01", "");
    	 vat.item.setValueByName("#F.itemBrand", "");
    }
    
}

// 全選
function selectAll(){
//  alert("selectAll");
  var processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=updateAllSearchData";
  return processString;
  
}

// 動態改變中類
function changeCategory02(){
	var category01 = vat.item.getValueByName("#F.category01");
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
 
    vat.ajax.XHRequest(
    {
        post:"process_object_name=imItemCategoryService"+
                 "&process_object_method_name=getAJAXCategory"+
                 "&category=" + category01 +
                 "&categoryType=CATEGORY02" +
                 "&brandCode=" + brandCode,
        find: function changeRequestSuccess(oXHR){ 
	var allCategory = eval(vat.ajax.getValue("allCategory", oXHR.responseText));
        		allCategory[0][0] = "#F.category02";
	vat.item.SelectBind(allCategory); 
        }   
    });
}

// picker 回來執行的事件
function doAfterPickerProcess(){
	if(typeof vat.bean().vatBeanPicker.result != "undefined"){
		vat.item.setValueByName("#F.itemId", vat.bean().vatBeanPicker.result[0].itemId); 
		changeItemName("itemId");
	}

}

// 動態改變商品名稱
function changeItemName( code ){
//	alert( code + "\n" + vat.item.getValueByName("#F.addressBookId") +"\n" + vat.item.getValueByName("#F.supplierCode") );

	vat.ajax.XHRequest({
		post:"process_object_name=imItemService"+
                  "&process_object_method_name=getAJAXItemName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&itemId=" + ( "itemId" === code ? vat.item.getValueByName("#F.itemId") : "" )+
                  "&itemCode=" + ( "itemCode" === code ? vat.item.getValueByName("#F.itemCode") : "" ),
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName("#F.startItemCode", vat.ajax.getValue("itemCode", oXHR.responseText) );
         		vat.item.setValueByName("#F.itemName", vat.ajax.getValue("itemName", oXHR.responseText) );
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.itemName", "");
		}   
	});	
}