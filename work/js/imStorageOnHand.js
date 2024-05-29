vat.debug.disable();

var vnB_Button = 0;
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
  	    {
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value,
  	     startItemCode      : document.forms[0]["#itemCode"          ].value,
  	     endItemCode		: document.forms[0]["#itemCode"          ].value,
  	     warehouseCode      : document.forms[0]["#warehouseCode"     ].value,
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value
	    };
     vat.bean.init(function(){
		return "process_object_name=imStorageAction&process_object_method_name=performOnHandInitial"; 
   		},{other: true});
  }
}

function headerInitial(){ 
	var allWarehouses = vat.bean("allWarehouses");
	var allItemCategories = vat.bean("allItemCategories");
	var allCategory01 = vat.bean("allCategory01");
	var allCategory02 = vat.bean("allCategory02");
	var allCategory03 = vat.bean("allCategory03");
	var allCategory07 = vat.bean("allCategory07");
	var allCategory09 = vat.bean("allCategory09");
	var allCategory13 = vat.bean("allCategory13");
	var allTaxRadio = (document.forms[0]["#loginBrandCode"].value).indexOf("T2") <= -1 ? [["", true, true], ["含稅", "未稅"], ["P", "F"]] : [["", true, true], ["完稅", "保稅"], ["P", "F"]];
	
	vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"儲位帳查詢作業", rows:[
		{row_style:"", cols:[
			{items:[{name:"#L.itemCode"					, type:"LABEL"  , value:"品號"}], td:" rowSpan=2"},
			{items:[{name:"#F.startItemCode"			, type:"TEXT"   , bind:"startItemCode", size:20},
					{name:"#L.between"					, type:"LABEL"  , value:"至"},
					{name:"#F.endItemCode"				, type:"TEXT"   , bind:"endItemCode"  , size:20},
					{name:"#F.itemCodePicker"			, type:"IMG"    , value:"加入品號",  src:"./images/16x16/Add.png", eClick:"doAppend()"},
					{name:"#F.itemCodeRemover"			, type:"IMG"    , value:"移除品號",  src:"./images/16x16/Remove.png", eClick:"doRemove()"}], td:" colSpan=3"},
			{items:[{name:"#L.itemCName"				, type:"LABEL"	, value:"品名"}], td:" rowSpan=2"},
			{items:[{name:"#F.itemCName"				, type:"TEXT"	,  bind:"itemCName", size:30}], td:" rowSpan=2"},
			{items:[{name:"#L.brandCode"				, type:"LABEL"	, value:"品牌"}], td:" rowSpan=2"},
			{items:[{name:"#F.brandCode"				, type:"TEXT"	,  bind:"brandCode", size:6, mode:"HIDDEN"},
					{name:"#F.brandName"				, type:"TEXT"	,  bind:"brandName", back:false, size:12, mode:"READONLY"}], td:" rowSpan=2"}
		]},
		{row_style:"", cols:[
			{items:[{name:"#F.itemCodeList"				, type:"TEXTAREA"	,  bind:"itemCodeList", size:30 , row:2, col: 40, mode:"READONLY"}], td:" colSpan=3"}
		]},
		{row_style:"", cols:[
			{items:[{name:"#L.warehouseCode"			, type:"LABEL"		, value:"庫別"}]},
			{items:[{name:"#F.warehouseCode"       		, type:"SELECT"		, bind:"warehouseCode", init:allWarehouses}], td:" colSpan=5"},
			{items:[{name:"#F.showZero"                 , type:"CHECKBOX"	, value:"N"},
					{name:"#L.showZero"                 , type:"LABEL"		, value:"顯示可用庫存量為零之記錄<BR>"},
					{name:"#F.showMinus"                , type:"CHECKBOX"	, value:"N"},
					{name:"#L.showMinus"                , type:"LABEL"		, value:"顯示可用庫存量為負之記錄"}], td:" colSpan=2"}
		]},
		{row_style:"", cols:[
			{items:[{name:"#L.category01"				 , type:"LABEL", 	value:"大類"}]},
			{items:[{name:"#F.category01"				 , type:"SELECT", 	bind:"category01",init:allCategory01, eChange:"changeCategory()", size:25}], td:" colSpan=3"},
			{items:[{name:"#L.category02"				 , type:"LABEL", 	value:"中類"}]},
			{items:[{name:"#F.category02"				 , type:"SELECT", 	bind:"category02",init:allCategory02, eChange:"changeCategory()", size:10}]},
			{items:[{name:"#L.category03"				 , type:"LABEL", 	value:"小類"}]},
			{items:[{name:"#F.category03"				 , type:"SELECT", 	bind:"category03",init:allCategory03}]}
		]},
		{row_style:"", cols:[
			{items:[{name:"#L.category07"				 , type:"LABEL", 	value:"性別"}]},
			{items:[{name:"#F.category07"				 , type:"SELECT", 	bind:"category07", init:allCategory07}]},
			{items:[{name:"#L.category04"				 , type:"LABEL", 	value:"尺寸"}]},
			{items:[{name:"#F.category04"				 , type:"TEXT", 	bind:"category04", size:12}]},
			{items:[{name:"#L.category09"				 , type:"LABEL", 	value:"款式"}]},
			{items:[{name:"#F.category09"				 , type:"TEXT", 	bind:"category09", size:12}]},
			{items:[{name:"#L.category13"				 , type:"LABEL", 	value:"系列"}]},
			{items:[{name:"#F.category13"				 , type:"TEXT", 	bind:"category13", size:12}]}
		]},
		{row_style:"", cols:[
			{items:[{name:"#L.isTax"                  	, type:"LABEL" , value:"稅別"}]},
			{items:[{name:"#F.isTax"                  	, type:"SELECT",  bind:"isTax", init:allTaxRadio}]},
			{items:[{name:"#L.category17"               , type:"LABEL" , value:"廠商"}]},
			{items:[{name:"#F.category17"               , type:"TEXT"  ,  bind:"category17"}]},
			{items:[{name:"#L.itemCategory"             , type:"LABEL" , value:"業種子類"}]},
			{items:[{name:"#F.itemCategory"             , type:"SELECT"  ,  bind:"itemCategory", size:12, init:allItemCategories}]},
			{items:[{name:"#L.itemBrand"                , type:"LABEL" , value:"商品品牌"}]},
			{items:[{name:"#F.itemBrand"                , type:"TEXT"  ,  bind:"itemBrand", size:12},
					{name:"#F.itemBrandPicker"          , type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif",
	 									 		            	service:"Im_Item:searchCategory:20100119.page",
	 									 			            left:0, right:0, width:1024, height:768,
	 									 						servicePassData:function(){ return "&categoryType=ItemBrand"; },
	 									 		                serviceAfterPick:function(){doAfterPickerProcess(); }},
					{name:"#F.itemBrandName"            , type:"TEXT"  ,  bind:"itemBrandName", size:12, mode:"READONLY"}]}
		]},
		{row_style:"", cols:[
			{items:[{name:"#L.storageCode"				, type:"LABEL" , value:"儲位"}]},
		 	{items:[{name:"#F.storageCode"				, type:"TEXT"  ,  bind:"storageCode", size:12}]},
		 	{items:[{name:"#L.storageInNo"				, type:"LABEL" , value:"進倉日"}]},
		 	{items:[{name:"#F.storageInNo"				, type:"TEXT"  ,  bind:"storageInNo", size:12}]},
		 	{items:[{name:"#L.storageLotNo"				, type:"LABEL" , value:"批號效期"}]},
			{items:[{name:"#F.storageLotNo"				, type:"TEXT"  ,  bind:"storageLotNo", size:12}], td:" colSpan=3"}
		]}
	  ],
		beginService:"",
		closeService:""
	});

}

function buttonLine(){
	var vsViewFunction = vat.bean().vatBeanOther.vatPickerId==""?"doView()":"doClosePicker()";
    vat.block.create(vnB_Button, {
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
	 	 		{name:"#B.export"	   , type:"IMG"      , value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"SPACE"          , type:"LABEL"	 , value:"　"},
	 			{name:"#B.view"        , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif", eClick:vsViewFunction}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function doClosePicker() {
    vat.block.pageSearch(vnB_Detail, {
        funcSuccess: function () {
            vat.block.submit(
            function () {
                return "process_object_name=imItemOnHandViewService&process_object_method_name=getSearchSelection";
            }, {
                bind: true,
                link: false,
                other: false,
                picker: true,
                isPicker: true,
                blockId: vnB_Detail
            });
        }
    });
}

function doView() {
    vat.block.pageSearch(vnB_Detail, {
        funcSuccess: function () {
            vat.block.submit(
            function () {
                return "process_object_name=imItemOnHandViewService&process_object_method_name=getSearchSelection";
            }, {
                bind: true,
                link: false,
                other: false,
                picker: true,
                isPicker: true,
                blockId: vnB_Detail
            });
        }
    });
}

function detailInitial(){
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
	var vbSelectionType = "CHECK";    
    if(vatPickerId != null && vatPickerId != ""){
		vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
		vbSelectionType = "CHECK";    
    }else{
		vat.item.setStyleByName("#B.view" , "display", "none");
		vbSelectionType = "NONE";
    }
    
    vat.item.make(vnB_Detail, "indexNo"			, {type:"IDX"  , desc:"序號"      });
    vat.item.make(vnB_Detail, "id.brandCode"	, {type:"TEXT" , size:10, maxLen:20, mode:"HIDDEN", desc:"品牌代號"      });
	vat.item.make(vnB_Detail, "id.itemCode"		, {type:"TEXT" , size:16, maxLen:20, mode:"READONLY", desc:"品號"      });
	vat.item.make(vnB_Detail, "itemCName"		, {type:"TEXT" , size:25, maxLen:20, mode:"READONLY", desc:"品名"      });
	vat.item.make(vnB_Detail, "id.warehouseCode", {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"庫別"      });
	vat.item.make(vnB_Detail, "id.storageLotNo" , {type:"TEXT" , size:10, maxLen:12, mode:"READONLY", desc:"批號效期"   });
	vat.item.make(vnB_Detail, "id.storageInNo"	, {type:"TEXT" , size:10, maxLen:12, mode:"READONLY", desc:"進倉日"	});
	vat.item.make(vnB_Detail, "id.storageCode"	, {type:"TEXT" , size:15, maxLen:12, mode:"READONLY", desc:"儲位"   });
	vat.item.make(vnB_Detail, "currentOnHandQty", {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"可用數量"      });
	vat.item.make(vnB_Detail, "blockQty"		, {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"預扣數量"      });
	vat.block.pageLayout(vnB_Detail, {
									id                  : "vatDetailDiv",
									pageSize            : 10,
									searchKey           : ["id.brandCode","id.warehouseCode","id.itemCode","id.storageLotNo","id.storageInNo","id.storageCode","itemCName"],
									selectionType       : vbSelectionType,
									indexType           : "AUTO",
			                        canGridDelete       : vbCanGridDelete,
									canGridAppend       : vbCanGridAppend,
									canGridModify       : vbCanGridModify,	
									loadBeforeAjxService: "loadBeforeAjxService()",
									loadSuccessAfter    : "loadSuccessAfter()", 
									saveBeforeAjxService: "saveBeforeAjxService()",
									saveSuccessAfter 	: "saveSuccessAfter()"
									});
}

// 載入成功後
function loadSuccessAfter(){

	$.unblockUI();
	
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		alert("您輸入條件查無資料");
	}
}

function saveSuccessAfter(){
		
}

function loadBeforeAjxService(){

    $.blockUI({
            message: $('#domMessage'),
            overlayCSS: { // 遮罩的css設定
                backgroundColor: '#eee'
            },
            css: { // 遮罩訊息的css設定
                border : '3px solid #aaa',
                width: '30%',
                left : '35%',
                backgroundColor : 'white',
                opacity : '0.9' //透明度，值在0~1之間
            }
	});
	
	var startItemCode = vat.item.getValueByName("#F.startItemCode"        );
	vat.item.setValueByName("#F.startItemCode", startItemCode.replace(/^\s+|\s+$/, '').toUpperCase());
	
	var endItemCode = vat.item.getValueByName("#F.endItemCode"        );
	vat.item.setValueByName("#F.endItemCode", endItemCode.replace(/^\s+|\s+$/, '').toUpperCase());
	
	var itemCodeList = vat.item.getValueByName("#F.itemCodeList"        );
	vat.item.setValueByName("#F.itemCodeList", itemCodeList.replace(/^\s+|\s+$/, '').toUpperCase());
	
	var itemCName = vat.item.getValueByName("#F.itemCName"        );
	vat.item.setValueByName("#F.itemCName", itemCName.replace(/^\s+|\s+$/, '').toUpperCase());
	
	var category17 = vat.item.getValueByName("#F.category17"        );
	vat.item.setValueByName("#F.category17", category17.replace(/^\s+|\s+$/, '').toUpperCase());
	
	var itemBrand = vat.item.getValueByName("#F.itemBrand"        );
	vat.item.setValueByName("#F.itemBrand", itemBrand.replace(/^\s+|\s+$/, '').toUpperCase());
	
	var processString = "process_object_name=imStorageService&process_object_method_name=getAJAXOnHandPageData" +
                      	"&loginBrandCode"   + "=" + vat.bean().vatBeanOther.loginBrandCode +
                      	"&startItemCode"    + "=" + vat.item.getValueByName("#F.startItemCode"		) + 
                      	"&endItemCode"      + "=" + vat.item.getValueByName("#F.endItemCode"		) + 
                      	"&itemCodeList"		+ "=" + vat.item.getValueByName("#F.itemCodeList"		) +
	                  	"&itemCName"		+ "=" + vat.item.getValueByName("#F.itemCName"          ) + 
                      	"&warehouseCode"    + "=" + vat.item.getValueByName("#F.warehouseCode"		) +
                      	"&showZero"         + "=" + vat.item.getValueByName("#F.showZero"           ) +
	                  	"&showMinus"        + "=" + vat.item.getValueByName("#F.showMinus"          ) + // 顯示負庫存 
	                  	"&itemCategory"     + "=" + vat.item.getValueByName("#F.itemCategory"       ) +
	                  	"&itemBrand"        + "=" + vat.item.getValueByName("#F.itemBrand"          ) +
	                  	"&category01"       + "=" + vat.item.getValueByName("#F.category01"         ) +
	                  	"&category02"       + "=" + vat.item.getValueByName("#F.category02"         ) +
	                  	"&category03"       + "=" + vat.item.getValueByName("#F.category03"         ) +
	                  	"&category04"       + "=" + vat.item.getValueByName("#F.category04"         ) +
	                  	"&category07"       + "=" + vat.item.getValueByName("#F.category07"         ) +
	                  	"&category09"       + "=" + vat.item.getValueByName("#F.category09"         ) +
	                  	"&category13"       + "=" + vat.item.getValueByName("#F.category13"         ) +	                  
	                  	"&category17"       + "=" + vat.item.getValueByName("#F.category17"         ) +
	                  	"&isTax"            + "=" + vat.item.getValueByName("#F.isTax"              ) +
	                  	"&storageCode"      + "=" + vat.item.getValueByName("#F.storageCode"		) + 
                      	"&storageInNo"      + "=" + vat.item.getValueByName("#F.storageInNo"		) + 
                      	"&storageLotNo"     + "=" + vat.item.getValueByName("#F.storageLotNo"		) ;
	                  
	return processString;									
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "process_object_name=imStorageService&process_object_method_name=saveOnHandSearchResult";
	return processString;	
}								

function doSearch(){
    vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);
}

//sql明細匯出excel
function doExport(){
    var url = 	"/erp/jsp/ExportFormView.jsp" + 
              	"?exportBeanName=IM_STORAGE_ON_HAND_SQL" + 
              	"&fileType=XLS" + 
              	"&processObjectName=imStorageService" + 
              	"&processObjectMethodName=getAJAXOnHandExportData" + 
	           	"&loginBrandCode"   + "=" + vat.bean().vatBeanOther.loginBrandCode +
                "&startItemCode"    + "=" + vat.item.getValueByName("#F.startItemCode"		) + 
                "&endItemCode"      + "=" + vat.item.getValueByName("#F.endItemCode"		) + 
                "&itemCodeList"		+ "=" + vat.item.getValueByName("#F.itemCodeList"		) +
	            "&itemCName"		+ "=" + vat.item.getValueByName("#F.itemCName"          ) + 
                "&warehouseCode"    + "=" + vat.item.getValueByName("#F.warehouseCode"		) +
                "&showZero"         + "=" + vat.item.getValueByName("#F.showZero"           ) +
	            "&showMinus"        + "=" + vat.item.getValueByName("#F.showMinus"          ) + // 顯示負庫存 
	            "&itemCategory"     + "=" + vat.item.getValueByName("#F.itemCategory"       ) +
	            "&itemBrand"        + "=" + vat.item.getValueByName("#F.itemBrand"          ) +
	            "&category01"       + "=" + vat.item.getValueByName("#F.category01"         ) +
	            "&category02"       + "=" + vat.item.getValueByName("#F.category02"         ) +
	            "&category03"       + "=" + vat.item.getValueByName("#F.category03"         ) +
	            "&category04"       + "=" + vat.item.getValueByName("#F.category04"         ) +
	            "&category07"       + "=" + vat.item.getValueByName("#F.category07"         ) +
	            "&category09"       + "=" + vat.item.getValueByName("#F.category09"         ) +
	            "&category13"       + "=" + vat.item.getValueByName("#F.category13"         ) +	                  
	            "&category17"       + "=" + vat.item.getValueByName("#F.category17"         ) +
	            "&isTax"            + "=" + vat.item.getValueByName("#F.isTax"              ) +
	            "&storageCode"      + "=" + vat.item.getValueByName("#F.storageCode"		) + 
                "&storageInNo"      + "=" + vat.item.getValueByName("#F.storageInNo"		) + 
                "&storageLotNo"     + "=" + vat.item.getValueByName("#F.storageLotNo"		) ;
                      	
    var width = "200";
    var height = "30";  
    window.open(url, '儲位匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

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
	vat.item.setValueByName("#F.warehouseCode", "");
	vat.item.setValueByName("#F.storageCode", "");
	vat.item.setValueByName("#F.storageInNo", "");
	vat.item.setValueByName("#F.storageLotNo", "");
	vat.item.setValueByName("#F.startItemCode", "");
	vat.item.setValueByName("#F.endItemCode", "");
	vat.item.setValueByName("#F.itemCodeList", "");
}

//將多品號以逗號分隔送到後端
function doAppend() {
    var itemCode = vat.item.getValueByName("#F.startItemCode");
    var itemCodeList = vat.item.getValueByName("#F.itemCodeList");
    if (itemCode != '') {
        if (itemCodeList == '')
        	vat.item.setValueByName("#F.itemCodeList", itemCode);
        else
        	vat.item.setValueByName("#F.itemCodeList", itemCodeList + "," + itemCode);
        vat.item.setValueByName("#F.startItemCode", '');
        vat.item.setValueByName("#F.endItemCode", '');
    }
}

// 移除多品號中的值
function doRemove() {
    var itemCode = vat.item.getValueByName("#F.startItemCode");
    var itemCodeList = vat.item.getValueByName("#F.itemCodeList");
    if (itemCode != '') {
        if (itemCodeList != '') {
            var sIndex = itemCodeList.indexOf(itemCode);
            while (sIndex > -1) {
                itemCodeList = itemCodeList.replace(itemCode, "");
                itemCodeList = itemCodeList.replace(",,", ",");
                if (itemCodeList.indexOf(",") == 0) // 第一個字元是逗號，移除逗號
                	itemCodeList = itemCodeList.substring(1);
                if (itemCodeList.lastIndexOf(",") == itemCodeList.length - 1) // 最後的字元是逗號，則移除逗號
                	itemCodeList = itemCodeList.substring(0, itemCodeList.length - 1);

                sIndex = itemCodeList.indexOf(itemCode)
            }
            vat.item.setValueByName("#F.itemCodeList", itemCodeList);
        }
        vat.item.setValueByName("#F.startItemCode", '');
        vat.item.setValueByName("#F.endItemCode", '');
    }
}


function changeCategory(){
	vat.bean().vatBeanOther.brandCode = vat.bean().vatBeanOther.loginBrandCode;
 	vat.bean().vatBeanOther.category01 = vat.item.getValueByName("#F.category01");
 	vat.bean().vatBeanOther.category02 = vat.item.getValueByName("#F.category02");
 	vat.bean().vatBeanOther.category07 = vat.item.getValueByName("#F.category07");
 	vat.bean().vatBeanOther.category09 = vat.item.getValueByName("#F.category09");
 	vat.bean().vatBeanOther.category13 = vat.item.getValueByName("#F.category13");
 	
	vat.block.submit(function(){return "process_object_name=imItemCategoryService"+
	            "&process_object_method_name=getAJAXItemCategoryRelatedList";},  {other:true,picker:false,
	     funcSuccess: function() {
	        vat.item.SelectBind(vat.bean("allCategory01"),{ itemName : "#F.category01" , selected : vat.bean().vatBeanOther.category01});
    		vat.item.SelectBind(vat.bean("allCategory02"),{ itemName : "#F.category02" , selected : vat.bean().vatBeanOther.category02});
    		vat.item.SelectBind(vat.bean("allCategory03"),{ itemName : "#F.category03" });
    		vat.item.SelectBind(vat.bean("allCategory07"),{ itemName : "#F.category07"  });
    		vat.item.SelectBind(vat.bean("allCategory09"),{ itemName : "#F.category09"  });
    		vat.item.SelectBind(vat.bean("allCategory13"),{ itemName : "#F.category13" });
	     }
	});
}

function doAfterPickerProcess(){
	//alert("doAfterPickerProcess")
	if(vat.bean().vatBeanPicker.categoryResult != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.categoryResult.length;
	    if( vsMaxSize != 0){
		  vat.item.setValueByName("#F.itemBrand", vat.bean().vatBeanPicker.categoryResult[0]["id.categoryCode"]);
		  changeCategoryCodeName("ItemBrand");
		}
	}
}

// 動態改變商品類別名稱
function changeCategoryCodeName(code){
	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXCategoryName"+
                  "&brandCode=" + vat.bean().vatBeanOther.loginBrandCode +
                  "&categoryType=itemBrand"+
                  "&categoryCode=" +  vat.item.getValueByName("#F.itemBrand") ,


		find: function change(oXHR){
				//alert(vat.ajax.getValue("categoryName", oXHR.responseText));
         		vat.item.setValueByName("#F.itemBrandName", vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.itemBrandName", "查無此類別");
		}
	});
}