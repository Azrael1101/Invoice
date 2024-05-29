/***
 *	檔案: imOnHandSearch.js
 *	說明：庫存明細查詢
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var action = "nextPage";
var onHandTimeScope = "";

function kweImBlock(){
  var voNow = new Date();
  onHandTimeScope = voNow.getTime().toString();
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();
  kweImDetail();

}


function kweImSearchInitial(){
  var timeScope;
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value,
  	     taxType            : document.forms[0]["#taxType"           ].value,
  	     startItemCode      : document.forms[0]["#startItemCode"     ].value,
  	     endItemCode        : document.forms[0]["#endItemCode"       ].value,
  	     startWarehouseCode : document.forms[0]["#startWarehouseCode"].value,
  	     endWarehouseCode   : document.forms[0]["#endWarehouseCode"  ].value,
  	     startLotNo         : document.forms[0]["#startLotNo"        ].value,
  	     endLotNo           : document.forms[0]["#endLotNo"          ].value,
  	     showZero           : document.forms[0]["#showZero"          ].value,
  	     itemCategory       : document.forms[0]["#itemCategory"      ].value,
  	     isReadOnlyControl  : document.forms[0]["#isReadOnlyControl"].value == null || document.forms[0]["#isReadOnlyControl"].value =="" ?"N":document.forms[0]["#isReadOnlyControl"].value
	    };

     vat.bean.init(function(){
		return "process_object_name=imItemOnHandViewService&process_object_method_name=executeSearchInitialNew";
   		},{other: true});
  }

}

function kweButtonLine(){
	vsViewFunction = vat.bean().vatBeanOther.vatPickerId==""?"doView()":"doClosePicker()";
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif", eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'}
	 	 		//{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			//{name:"#B.exportXLS"   , type:"IMG"      , value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData('XLS')"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.exportTXT"   , type:"IMG"      , value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData('TXT')"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.print"       , type:"IMG"      , value:"條碼列印",   src:"./images/button_barcode_print.gif" , eClick:"barCodePrint()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.view"        , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif", eClick:vsViewFunction}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:"",
		closeService:""
	});
	/*
	if("" == vat.bean().vatBeanOther.vatPickerId)
		vat.item.setStyleByName("#B.view" , "display", "none");

	vat.item.setStyleByName("#B.exportXLS" , "display", "none"); // 未查詢前，不提供匯出功能
	vat.item.setStyleByName("#B.exportTXT" , "display", "none"); // 未查詢前，不提供匯出功能

	if("T1BS" != vat.bean().vatBeanOther.loginBrandCode && "T1CO" != vat.bean().vatBeanOther.loginBrandCode && "T1GS" != vat.bean().vatBeanOther.loginBrandCode)
		vat.item.setStyleByName("#B.print" , "display", "none"); // 各品牌單庫庫存轉條碼 for 百貨*/
}

function kweImHeader(){
var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
var allItemCategories     = vat.bean("allItemCategories");
var allCategory01 = vat.bean("allCategory01");
var allCategory02 = vat.bean("allCategory02");
var allCategory03 = vat.bean("allCategory03");
var allCategory07 = vat.bean("allCategory07");
var allCategory09 = vat.bean("allCategory09");
var allCategory13 = vat.bean("allCategory13");
var allItemBrands = vat.bean("allItemBrands");
var allTaxRadio = (document.forms[0]["#loginBrandCode"].value).indexOf("T2") <= -1 ? [["", true, true], ["含稅", "未稅"], ["P", "F"]] : [["", true, true], ["完稅", "保稅"], ["P", "F"]];
var isItemCodeOrEanCode = [["", "itemCode", false], ["品號", "國際碼"], ["itemCode", "eanCode"]];
var vsRowStyle= vat.bean().vatBeanOther.loginBrandCode.indexOf("T2") > -1 ?"":" style= 'display:none;'";


vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"可用庫存查詢作業", rows:[
	 {row_style:"", cols:[
		 {items:[{name:"#F.itemCodeOrEanCode"        , type:"SELECT"  , bind:"itemCodeOrEanCode", init:isItemCodeOrEanCode},
		 		 {name:"#L.likeMark"                 , type:"LABEL"  , value:"%"}], td:" rowSpan=2"},
		 {items:[{name:"#F.startItemCode"            , type:"TEXT"   ,  bind:"startItemCode", size:20},
				 {name:"#L.between"                  , type:"LABEL"  , value:"至"},
		 		 {name:"#F.endItemCode"              , type:"TEXT"   ,  bind:"endItemCode"  , size:20},
		 		 {name:"#F.itemCodePicker"           , type:"IMG"    , value:"加入品號",  src:"./images/16x16/Add.png", eClick:"doAppend()"},
		 		 {name:"#F.itemCodeRemover"          , type:"IMG"    , value:"移除品號",  src:"./images/16x16/Remove.png", eClick:"doRemove()"}]},
		 {items:[{name:"#L.itemCName"                , type:"LABEL"  , value:"品名"}], td:" rowSpan=2"},
		 {items:[{name:"#F.itemCName"                , type:"TEXT" ,  bind:"itemCName", size:40}], td:" rowSpan=2"},
		 {items:[{name:"#L.brandCode"                , type:"LABEL"  , value:"品牌"}], td:" rowSpan=2"},
		 {items:[{name:"#F.brandCode"                , type:"TEXT"   ,  bind:"brandCode", size:6, mode:"HIDDEN"},
		 		 {name:"#F.brandName"                , type:"TEXT"   ,  bind:"brandName", back:false, size:12, mode:"READONLY"}], td:" rowSpan=2"}]},
	 {row_style:"", cols:[
	 	 {items:[{name:"#F.itemCodeList"             , type:"TEXTAREA"   ,  bind:"itemCodeList", size:30 , row:2, col: 40, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
		 {items:[{name:"#L.warehouseCode"            , type:"LABEL" , value:"庫別"}]},
		 {items:[{name:"#F.startWarehouseCode"       , type:"SELECT"  ,  bind:"startWarehouseCode", init:allDeliveryWarehouses},
		         {name:"#L.between"                  , type:"LABEL" , value:"至"},
		         {name:"#F.endWarehouseCode"         , type:"SELECT"  ,  bind:"endWarehouseCode", init:allArrivalWarehouses}]},
	 	 {items:[{name:"#L.LotNo"                    , type:"LABEL" , value:"批號"}]},
		 {items:[{name:"#F.startLotNo"               , type:"TEXT"  ,  bind:"startLotNo", size:12},
		         {name:"#L.between"                  , type:"LABEL" , value:"至"},
		         {name:"#F.endLotNo"                 , type:"TEXT"  ,  bind:"endLotNo", size:12}]},
		 {items:[{name:"#L.itemCategory"             , type:"LABEL" , value:"業種子類"}]},
		 {items:[{name:"#F.itemCategory"             , type:"SELECT"  ,  bind:"itemCategory", size:12, init:allItemCategories}]}]},
	 {row_style:"", cols:[
		 {items:[{name:"#L.category01"               , type:"LABEL" , value:"大類"}]},
		 {items:[{name:"#F.category01"               , type:"SELECT"  ,  bind:"category01", size:12,init:allCategory01, eChange:"changeCategory()", size:25}]},
	 	 {items:[{name:"#L.category02"               , type:"LABEL" , value:"中類"}]},
		 {items:[{name:"#F.category02"               , type:"SELECT"  ,  bind:"category02", size:12,init:allCategory02, eChange:"changeCategory()", size:25}]},
		 {items:[{name:"#L.category03"               , type:"LABEL" , value:"小類"}]},
		 {items:[{name:"#F.category03"               , type:"SELECT"  ,  bind:"category03", size:12,init:allCategory03, size:25}]}]},
	 {row_style:vsRowStyle, cols:[
		 {items:[{name:"#L.itemBrand"                , type:"LABEL" , value:"商品品牌"}]},
		 {items:[{name:"#F.itemBrand"				 , type:"SELECT", 	bind:"itemBrand",init:allItemBrands}]},
		 {items:[{name:"#F.showZero"                 , type:"CHECKBOX" , bind:"showZero", value:"N"},
	             {name:"#L.showZero"                 , type:"LABEL"    , value:"顯示可用庫存量為零之記錄<BR>"},
	         	 {name:"#F.showMinus"                , type:"CHECKBOX" , bind:"showMinus", value:"N"},
	         	 {name:"#L.showMinus"                , type:"LABEL"    , value:"顯示可用庫存量為負之記錄"}], td:" colSpan=4"}]}
	  ],
		beginService:"",
		closeService:""
	});
	vat.item.setValueByName("#F.brandCode", vat.bean().vatBeanOther.loginBrandCode);
	vat.item.setValueByName("#F.startItemCode", vat.bean().vatBeanOther.startItemCode);
	vat.item.setValueByName("#F.endItemCode"    , vat.bean().vatBeanOther.endItemCode);
	vat.item.setValueByName("#F.startWarehouseCode", vat.bean().vatBeanOther.startWarehouseCode);
	vat.item.setValueByName("#F.endWarehouseCode"    , vat.bean().vatBeanOther.endWarehouseCode);
	vat.item.setValueByName("#F.startLotNo", vat.bean().vatBeanOther.startLotNo);
	vat.item.setValueByName("#F.endLotNo"    , vat.bean().vatBeanOther.endLotNo);
	vat.item.setValueByName("#F.showZero", vat.bean().vatBeanOther.showZero);
	vat.item.setValueByName("#F.showMinus", vat.bean().vatBeanOther.showMinus);
	vat.item.setValueByName("#F.itemCategory", vat.bean().vatBeanOther.itemCategory);
	vat.item.setValueByName("#F.taxType", vat.bean().vatBeanOther.taxType);
	if( "Y" == vat.bean("isReadOnlyControl") ){
		if( null != vat.bean().vatBeanOther.startWarehouseCode && "" != vat.bean().vatBeanOther.startWarehouseCode)
			vat.item.setAttributeByName("#F.startWarehouseCode", "readOnly", true);
		if( null != vat.bean().vatBeanOther.endWarehouseCode && "" != vat.bean().vatBeanOther.endWarehouseCode)
			vat.item.setAttributeByName("#F.endWarehouseCode", "readOnly", true);
	}
}

function kweImDetail(){

	var shiftMode = "shift";

	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = true;
	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
	var vbSelectionType = vatPickerId== null || vatPickerId== "" ?"NONE":"CHECK";
	var vnB_Detail = 2;
    if(vatPickerId!= null && vatPickerId!= ""){
   		vat.item.make(vnB_Detail, "checkbox"            , {type:"XBOX" });
   	}
    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"  , size: 1, view:"fixed", desc:"序號"      });
	vat.item.make(vnB_Detail, "brandCode"           , {type:"HIDDEN" , size: 0, maxLen: 0, mode:"HIDDEN"  , desc:"品牌"});
	vat.item.make(vnB_Detail, "itemCode"            , {type:"TEXT" , view:"fixed", size:20, maxLen:20, mode:"READONLY", desc:"品號"});
	vat.item.make(vnB_Detail, "itemCName"           , {type:"TEXT" , view:"fixed", size:30, maxLen:22, mode:"READONLY", desc:"中文名稱"});
	vat.item.make(vnB_Detail, "warehouseCode"       , {type:"TEXT" ,  size: 3, maxLen: 3, mode:"READONLY", desc:"庫別"});
	vat.item.make(vnB_Detail, "warehouseName"       , {type:"TEXT" , view:"", size: 12, maxLen:12, mode:"READONLY", desc:"庫別名稱"});
	vat.item.make(vnB_Detail, "unitPrice"           , {type:"TEXT" , view:"", size: 12, maxLen:12, mode:"READONLY", desc:"售價"});
	vat.item.make(vnB_Detail, "lotNo"               , {type:"HIDDEN" , view:"", size:10, maxLen:10, mode:"READONLY", desc:"批號"});
	vat.item.make(vnB_Detail, "inUncommitQty"       , {type:"HIDDEN" , view:shiftMode, size: 1, maxLen:1, mode:"READONLY", desc:"採購未結"});
	vat.item.make(vnB_Detail, "moveUncommitQty"     , {type:"HIDDEN" , view:shiftMode, size: 1, maxLen:1, mode:"READONLY", desc:"調撥未結"});
	vat.item.make(vnB_Detail, "otherUncommitQty"    , {type:"HIDDEN" , view:shiftMode, size: 1, maxLen:1, mode:"READONLY", desc:"調整未結"});
	vat.item.make(vnB_Detail, "currentOnHandQty"    , {type:"NUMB" , size: 7, maxLen: 7, mode:"READONLY", desc:"庫存量"});
	vat.item.make(vnB_Detail, "headId"              , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["id.brandCode","id.itemCode","id.warehouseCode","id.lotNo"],
														//pickAllService		: function (){return ""},
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "pageLoadSuccess()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()}}
														});
}


function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");
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
	
	var processString = "process_object_name=imItemOnHandViewService&process_object_method_name=executeAJAXSearchPageDataNew" +
                      "&loginBrandCode"     + "=" + vat.bean().vatBeanOther.loginBrandCode +
                      
                      "&itemCodeOrEanCode"  + "=" + vat.item.getValueByName("#F.itemCodeOrEanCode"    ) +
	                  "&startItemCode"      + "=" + vat.item.getValueByName("#F.startItemCode"        ) +
	                  "&endItemCode"        + "=" + vat.item.getValueByName("#F.endItemCode"          ) +
	                  "&itemCodeList"       + "=" + vat.item.getValueByName("#F.itemCodeList"         ) + // 多品號
	                  "&itemCName"          + "=" + vat.item.getValueByName("#F.itemCName"            ) +
	                  "&startWarehouseCode" + "=" + vat.item.getValueByName("#F.startWarehouseCode"   ) +
	                  "&endWarehouseCode"   + "=" + vat.item.getValueByName("#F.endWarehouseCode"     ) +
					  "&startLotNo"         + "=" + vat.item.getValueByName("#F.startLotNo"           ) +
	                  "&endLotNo"           + "=" + vat.item.getValueByName("#F.endLotNo"             ) +
	                  "&showZero"           + "=" + vat.item.getValueByName("#F.showZero"             ) +
	                  "&showMinus"          + "=" + vat.item.getValueByName("#F.showMinus"            ) + // 顯示負庫存
	                  "&itemCategory"       + "=" + vat.item.getValueByName("#F.itemCategory"         ) +
	                  "&warehouseEmployee"  + "=" + vat.bean("allDeliveryWarehouses"                  ) +
	                  "&itemBrand"          + "=" + vat.item.getValueByName("#F.itemBrand"            ) +
	                  "&action"             + "=" + action                                              +
	                  "&onHandTimeScope"          + "=" + onHandTimeScope;
	                  //"&category01"         + "=" + vat.item.getValueByName("#F.category01"           ) +
	                  //"&category02"         + "=" + vat.item.getValueByName("#F.category02"           ) +
	                  //"&category03"         + "=" + vat.item.getValueByName("#F.category03"           ) +
	                  //"&category04"         + "=" + vat.item.getValueByName("#F.category04"           ) +
	                  //"&category07"         + "=" + vat.item.getValueByName("#F.category07"           ) +
	                  //"&category09"         + "=" + vat.item.getValueByName("#F.category09"           ) +
	                  //"&category13"         + "=" + vat.item.getValueByName("#F.category13"           ) +	                  
	                  //"&category17"         + "=" + vat.item.getValueByName("#F.category17"           ) +
	                  //"&taxType"            + "=" + vat.item.getValueByName("#F.taxType"              ) ;
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
		processString = "process_object_name=imItemOnHandViewService&process_object_method_name=saveSearchResult";
		//alert(processString);
	}

	return processString;
}



/*
 PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
}

function saveSuccessAfter() {
}

function doSearch() {
	action = "search" //更改為查詢
    //alert("searchService");
    //vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
    //alert("timeScope:"+vat.bean().timeScope);
    //saveImOnHandIntoTmp();
    vat.block.submit(function () {
        return "process_object_name=tmpAjaxSearchDataService" + "&process_object_method_name=deleteByTimeScope&timeScope=" + vat.bean().vatBeanOther.timeScope
    }, {
        other: true,
        funcSuccess: function () {
            vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
        }
    });
}

function pageLoadSuccess(){
	action = "nextPage"; //更改為翻頁
	$.unblockUI();
	
	//alert();
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		vat.item.setStyleByName("#B.view" , "display", "none");
		vat.item.setStyleByName("#B.exportXLS" , "display", "none");
		vat.item.setStyleByName("#B.exportTXT" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
	    if("" == vat.bean().vatBeanOther.vatPickerId)
			vat.item.setStyleByName("#B.view" , "display", "none");
		else
			vat.item.setStyleByName("#B.view" , "display", "inline");

		vat.item.setStyleByName("#B.exportXLS" , "display", "inline"); // 查詢有值，才顯示匯出按鈕
		vat.item.setStyleByName("#B.exportTXT" , "display", "inline"); // 查詢有值，才顯示匯出按鈕
	}
}

function doClosePicker() {
    //alert("doView");
    //vat.bean().vatBeanPicker.xxx = 1;
    vat.block.pageSearch(2, {
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
                blockId: 2
            });
        }
    });
}

function doView() {

    vat.block.pageSearch(2, {
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
                blockId: 2
            });
        }
    });
}

function closeWindows(closeType) {
    var isExit = true;
    if ("CONFIRM" == closeType) {
        isExit = confirm("是否確認離開?");
    }
    if (isExit) window.top.close();

}

function selectAll(){
  //vatIsAllClick
  processString = "process_object_name=imItemOnHandViewService&process_object_method_name=updateAllSearchData"
  return processString;

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

function changeCategory(){
	vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
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

//可用庫存XLS匯出明細
function exportFormData(fileType){
	
	var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	
	var beanName = "IM_ITEM_ON_HAND"; // standard_ie.properties檔中對應的key

    var url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=" + fileType +
              "&processObjectName=imItemOnHandViewService" +
              "&processObjectMethodName=getImItemOnHandData" +
              "&loginBrandCode=" + vat.bean().vatBeanOther.loginBrandCode +
              "&startItemCode=" + vat.item.getValueByName("#F.startItemCode") +
              "&endItemCode=" + vat.item.getValueByName("#F.endItemCode") +
              "&itemCodeList=" + vat.item.getValueByName("#F.itemCodeList") + // 多品號
              "&itemCName=" + vat.item.getValueByName("#F.itemCName") +
              "&startWarehouseCode=" + vat.item.getValueByName("#F.startWarehouseCode") +
              "&endWarehouseCode=" + vat.item.getValueByName("#F.endWarehouseCode") +
              "&startLotNo=" + vat.item.getValueByName("#F.startLotNo") +
              "&endLotNo=" + vat.item.getValueByName("#F.endLotNo") +
              "&showZero=" + vat.item.getValueByName("#F.showZero") +
              "&showMinus=" + vat.item.getValueByName("#F.showMinus") + // 顯示負庫存
              "&itemCategory=" + vat.item.getValueByName("#F.itemCategory") +
              "&itemBrand=" + vat.item.getValueByName("#F.itemBrand") +
              "&category01=" + vat.item.getValueByName("#F.category01") +
              "&category02=" + vat.item.getValueByName("#F.category02") +
              "&category03=" + vat.item.getValueByName("#F.category03") +
              "&category04=" + vat.item.getValueByName("#F.category04") +
              "&category07=" + vat.item.getValueByName("#F.category07") + 
              "&category09=" + vat.item.getValueByName("#F.category09") +
              "&category13=" + vat.item.getValueByName("#F.category13") +
              "&category17=" + vat.item.getValueByName("#F.category17") +
              "&taxType=" + vat.item.getValueByName("#F.taxType");

    var width = "200";
    var height = "30";
    window.open(url, '可用庫存匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

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

// 清除
function resetForm(){
	document.forms[0].reset();
}

// 各品牌單庫庫別轉條碼
function barCodePrint() {
    if (vat.item.getValueByName("#F.startWarehouseCode") == '') {
        alert("條碼列印請輸入庫別！");
        return;
    }
    url = "/erp/jsp/ExportFormView.jsp" + "?exportBeanName=IM_ITEM_ON_HAND_BARCODE"
    	+ "&fileType=TXT"
    	+ "&processObjectName=imItemOnHandViewService"
    	+ "&processObjectMethodName=exportBarCode"
    	+ "&exportType=barcode"
    	+ "&loginBrandCode=" + vat.bean().vatBeanOther.loginBrandCode
    	+ "&warehouseCode=" + vat.item.getValueByName("#F.startWarehouseCode");
    var width = '200';
    var height = '30';
    window.open(url, '可用庫存條碼單檔案列印', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width) / 2 + ',top=' + (screen.availHeight - height) / 2);
}

function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vItemCode = vat.item.getGridValueByName("itemCode", nItemLine);
    var vWarehouseCode = vat.item.getGridValueByName("warehouseCode", nItemLine);
    //var vOrderTypeCode = vat.bean().vatBeanOther.orderTypeCode;
    //if(null != vFormId && "" != vFormId && 0 != vFormId){
    //	var url = "/erp/Im_OnHand:create:20170912.page?formId=" + vFormId;
	//	sc=window.open(url, '庫存查詢作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	//	sc.resizeTo((screen.availWidth),(screen.availHeight));
	//	sc.moveTo(0,0);
	//}
	var url = "/erp/Im_OnHand:create:20170912.page?itemCode="+vItemCode+"&warehouseCode="+vWarehouseCode;
		sc=window.open(url, '庫存查詢作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.resizeTo((screen.availWidth),(screen.availHeight));
		sc.moveTo(0,0);
}