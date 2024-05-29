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
//var afterSavePageProcess = "";

function outlineBlock(){
  kweButtonLine();
  kweImMainInitial();
  kweImHeader();
  kweSaleQty();
  kweOnHandModifyInformation();
  kweImDailyDetail();
  //vat.block.pageRefresh(vnB_Detail);

}


function kweImMainInitial(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     itemCode           : document.forms[0]["#itemCode" ].value,
  	     warehouseCode      : document.forms[0]["#warehouseCode" ].value,
  	     isCurrentMonth      : ""
  	     //vatPickerId        : document.forms[0]["#vatPickerId"       ].value,
  	     //taxType            : document.forms[0]["#taxType"           ].value,
  	     //startItemCode      : document.forms[0]["#startItemCode"     ].value,
  	     //endItemCode        : document.forms[0]["#endItemCode"       ].value,
  	     //startWarehouseCode : document.forms[0]["#startWarehouseCode"].value,
  	     //endWarehouseCode   : document.forms[0]["#endWarehouseCode"  ].value,
  	     //startLotNo         : document.forms[0]["#startLotNo"        ].value,
  	     //endLotNo           : document.forms[0]["#endLotNo"          ].value,
  	     //showZero           : document.forms[0]["#showZero"          ].value,
  	     //itemCategory       : document.forms[0]["#itemCategory"      ].value,
  	     //isReadOnlyControl  : document.forms[0]["#isReadOnlyControl"].value == null || document.forms[0]["#isReadOnlyControl"].value =="" ?"N":document.forms[0]["#isReadOnlyControl"].value
	    };


     vat.bean.init(function(){
		return "process_object_name=imItemOnHandViewAction&process_object_method_name=performInitial";
   		},{other: true});
  }

}

function kweButtonLine(){
	//vsViewFunction = vat.bean().vatBeanOther.vatPickerId==""?"doView()":"doClosePicker()";
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"        , type:"IMG"      , value:"查詢",   src:"./images/button_find.gif", eClick:'doSearch()'},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:"",
		closeService:""
	});

}

function kweImHeader(){
	vat.block.create(vnB_Header = 1, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"可用庫存查詢作業", rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.itemCode"                  , type:"LABEL"  , value:"品號"}]},
				{items:[{name:"#F.itemCode"                  , type:"TEXT"  , bind:"itemCode", mode:"READONLY",size:20}], td: "colSpan=3"},
				{items:[{name:"#L.itemCName"                , type:"LABEL"  , value:"品名"}]},
				{items:[{name:"#F.itemCName"                , type:"TEXT" ,  bind:"itemCName", mode:"READONLY",size:40}], td:" colSpan=3"}]},
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseCode"            , type:"LABEL" , value:"庫別"}]},
				{items:[{name:"#F.warehouseCode"            , type:"TEXT" ,  bind:"warehouseCode", mode:"READONLY",size:20},
				        {name:"#F.warehouseName"            , type:"TEXT" ,  bind:"warehouseName", mode:"READONLY",size:20}], td:" colSpan=3"},
				
				{items:[{name:"#L.lotNo"                    , type:"LABEL" , value:"批號"}]},
				{items:[{name:"#F.lotNo"                    , type:"TEXT" ,  bind:"lotNo", mode:"READONLY",size:20}]},
				{items:[{name:"#L.itemBrand"                , type:"LABEL" , value:"商品品牌"}]},
				{items:[{name:"#F.itemBrand"                , type:"TEXT" ,  bind:"itemBrand", mode:"READONLY",size:20},
				        {name:"#F.itemBrandName"                , type:"TEXT" ,  bind:"itemBrandName", mode:"READONLY",size:20}]}]},
			{row_style:"", cols:[
				{items:[{name:"#L.category01"            , type:"LABEL" , value:"大類"}]},
				{items:[{name:"#F.category01"            , type:"TEXT" ,  bind:"category01", mode:"READONLY",size:20},
				        {name:"#F.category01Name"            , type:"TEXT" ,  bind:"category01Name", mode:"READONLY",size:20}], td:" colSpan=3"},
				{items:[{name:"#L.category02"                    , type:"LABEL" , value:"中類"}]},
				{items:[{name:"#F.category02"                    , type:"TEXT" ,  bind:"category02", mode:"READONLY",size:20},
				        {name:"#F.category02Name"                    , type:"TEXT" ,  bind:"category02Name", mode:"READONLY",size:20}]},
				{items:[{name:"#L.category03"                , type:"LABEL" , value:"小類"}]},
				{items:[{name:"#F.category03"                , type:"TEXT" ,  bind:"category03", mode:"READONLY",size:20},
				        {name:"#F.category03Name"                , type:"TEXT" ,  bind:"category03Name", mode:"READONLY",size:20}]}]},
			{row_style:"", cols:[
				{items:[{name:"#L.category07"            , type:"LABEL" , value:"性別"}]},
				{items:[{name:"#F.category07"            , type:"TEXT" ,  bind:"category07", mode:"READONLY",size:20}]},
		 		{items:[{name:"#L.category04"            , type:"LABEL" , value:"尺寸"}]},
		 		{items:[{name:"#F.category04"            , type:"TEXT" ,  bind:"category04", mode:"READONLY",size:20}]},
		 		{items:[{name:"#L.category09"                    , type:"LABEL" , value:"款式"}]},
		 		{items:[{name:"#F.category09"                    , type:"TEXT" ,  bind:"category09", mode:"READONLY",size:20}]},
		 		{items:[{name:"#L.category13"                , type:"LABEL" , value:"系列"}]},
		 		{items:[{name:"#F.category13"                , type:"TEXT" ,  bind:"category13", mode:"READONLY",size:20}]}]},
		 	{row_style:"", cols:[
		 		{items:[{name:"#L.boxCapacity"            , type:"LABEL" , value:"每箱數量"}]},
		 		{items:[{name:"#F.boxCapacity"            , type:"TEXT" ,  bind:"boxCapacity", mode:"READONLY",size:20}]},
		 		{items:[{name:"#L.supplierItemCode"            , type:"LABEL" , value:"原廠貨號"}]},
		 		{items:[{name:"#F.supplierItemCode"            , type:"TEXT" ,  bind:"supplierItemCode", mode:"READONLY",size:20}]},
		 		{items:[{name:"#L.itemEName"                    , type:"LABEL" , value:"其他品名"}]},
		 		{items:[{name:"#F.itemEName"                    , type:"TEXT" ,  bind:"itemEName", mode:"READONLY",size:30}]},
		 		{items:[{name:"#L.unitPrice"                , type:"LABEL" , value:"售價"}]},
		 		{items:[{name:"#F.unitPrice"                , type:"TEXT" ,  bind:"unitPrice", mode:"READONLY",size:6}]}]},
			{row_style:"", cols:[
		 		{items:[{name:"#L.taxType"            , type:"LABEL" , value:"稅別"}]},
		 		{items:[{name:"#F.taxType"            , type:"TEXT" ,  bind:"taxType", mode:"READONLY",size:20}]},
		 		{items:[{name:"#L.category17"            , type:"LABEL" , value:"廠商"}]},
		 		{items:[{name:"#F.category17"            , type:"TEXT" ,  bind:"category17", mode:"READONLY",size:20},
		 		        {name:"#F.supplierName"            , type:"TEXT" ,  bind:"supplierName",mode:"READONLY", size:20}]},
		 		{items:[{name:"#L.itemCategory"                    , type:"LABEL" , value:"業種子類"}]},
		 		{items:[{name:"#F.itemCategory"                    , type:"TEXT" ,  bind:"itemCategory", mode:"READONLY",size:20},
		 			    {name:"#F.itemCategoryName"                    , type:"TEXT" ,  bind:"itemCategoryName", mode:"READONLY",size:20}]},
		 		{items:[{name:"#L.brandCode"                , type:"LABEL" , value:"品牌"}]},
		 		{items:[{name:"#F.brandCode"                , type:"TEXT" ,  bind:"brandCode", size:6, mode:"HIDDEN"},
		 				{name:"#F.brandName"                , type:"TEXT" ,  bind:"brandName", back:"false", size:12, mode:"READONLY"}]}]},
		 	{row_style:"", cols:[
		 		{items:[{name:"#L.currentOnHandQty"            , type:"LABEL" , value:"庫存量"}]},
		 		{items:[{name:"#F.currentOnHandQty"            , type:"TEXT" ,  bind:"currentOnHandQty",mode:"READONLY", size:20}], td:"colspan=3"},
		 		{items:[{name:"#L.isMoveIn"            , type:"LABEL" , value:"是否進貨"}]},
		 		{items:[{name:"#F.isMoveIn"            , type:"TEXT" ,  bind:"isMoveIn", mode:"READONLY",size:20}], td:"colspan=3"}]}
		],
		beginService:"",
		closeService:""
	});
}

function kweSaleQty(){
	vat.block.create(vnB_Header = 1, {
		id: "vatBlock_SaleQty", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"銷售量", rows:[
		 	{row_style:"", cols:[
		 		{items:[{name:"#L.dSubTotal"            , type:"LABEL" , value:"當日銷售量"}]},
		 		{items:[{name:"#F.dSubTotal"            , type:"TEXT" ,  bind:"totalD", mode:"READONLY",size:20}], td:"colspan=3"},
		 		{items:[{name:"#L.mSubTotal"            , type:"LABEL" , value:"當月銷售量"}]},
		 		{items:[{name:"#F.mSubTotal"            , type:"TEXT" ,  bind:"totalM", mode:"READONLY",size:20}]},
		 		{items:[{name:"#L.ySubTotal"            , type:"LABEL" , value:"當年銷售量"}]},
		 		{items:[{name:"#F.ySubTotal"            , type:"TEXT" ,  bind:"totalY", mode:"READONLY",size:20}]}]}
		],
		beginService:"",
		closeService:""
	});
}


function kweOnHandModifyInformation(){
	var today = new Date();
	var year = parseInt(today.getFullYear());
	var monthString = ("0" + (today.getMonth()+1));
	var month = monthString.substr(monthString.length-2);
	var dayString = ("0" + (today.getDate()));
	var day = dayString.substr(dayString.length-2);
	var years = [["",year,false],[year-1,year,year+1],[year-1,year,year+1]];
	var months = [["",month,false],["01","02","03","04","05","06","07","08","09","10","11","12"],["01","02","03","04","05","06","07","08","09","10","11","12"]];
	var beginningDays = [["","01",false],["01"],["01"]];
	var endingDays = [["",day,false],["01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"],
									 ["01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"]];
	vat.block.create(vnB_Header = 1, {
		id: "vatBlock_ModifyQty", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"庫存異動資訊", rows:[
		 	{row_style:"", cols:[
		 		{items:[{name:"#L.beginning"            , type:"LABEL" , value:"日期區間，開始於"}]},
		 		{items:[{name:"#F.beginningYear"            , type:"SELECT" ,  bind:"beginningYear", size:10, init:years},
		 			    {name:"#L.beginningYear"            , type:"LABEL" , value:"年"},
		 			    {name:"#F.beginningMonth"            , type:"SELECT" ,  bind:"beginningMonth", size:10, init:months},
		 			    {name:"#L.beginningMonth"            , type:"LABEL" , value:"月"},
		 			    {name:"#F.beginningDay"            , type:"SELECT" ,  bind:"beginningDay", size:10, init:beginningDays, mode:"READONLY"},
		 			    {name:"#L.beginningDay"            , type:"LABEL" , value:"日"},
		 			    {name:"#L.end"            , type:"LABEL" , value:"，至"},
		 			    {name:"#F.endingYear"            , type:"SELECT" ,  bind:"endingYear", size:10, init:years},
		 			    {name:"#L.endingYear"            , type:"LABEL" , value:"年"},
		 			    {name:"#F.endingMonth"            , type:"SELECT" ,  bind:"endingMonth", size:10, init:months},
		 			    {name:"#L.endingMonth"            , type:"LABEL" , value:"月"},
		 			    {name:"#F.endingDay"            , type:"SELECT" ,  bind:"endingDay", size:10, init:endingDays},
		 			    {name:"#L.endingDay"            , type:"LABEL" , value:"日"}]},
		 		{items:[{name:"#L.beginningOnHandQuantity"            , type:"LABEL" , value:"期初庫存："}]},
		 		{items:[{name:"#F.beginningOnHandQuantity"            , type:"TEXT" , bind:"beginningOnHandQuantity", size:20, mode:"READONLY"}]}
		 	 ]}
		 	 
		],
		beginService:"",
		closeService:""
	});
}

function kweImDailyDetail(){
	//var shiftMode = "shift";
	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = true;
	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
	var vbSelectionType = vatPickerId== null || vatPickerId== "" ?"NONE":"CHECK";
	var vnB_Detail = 2;

    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"  , size: 1, view:"fixed", desc:"序號"      });
    vat.item.make(vnB_Detail, "transationDate"      , {type:"TEXT" , view:"", size:7, maxLen:7, mode:"READONLY", desc:"日期"});
	vat.item.make(vnB_Detail, "year"                , {type:"HIDDEN" , size: 0, maxLen: 0, mode:"HIDDEN"  , desc:"異動年"});
	vat.item.make(vnB_Detail, "month"               , {type:"HIDDEN" , size: 0, maxLen: 0, mode:"HIDDEN"  , desc:"異動月"});
	vat.item.make(vnB_Detail, "day"                 , {type:"HIDDEN" , size: 0, maxLen: 0, mode:"HIDDEN"  , desc:"異動日"});
	vat.item.make(vnB_Detail, "itemCode"            , {type:"HIDDEN" , size: 0, maxLen: 0, mode:"HIDDEN"  , desc:"品號"});
	vat.item.make(vnB_Detail, "warehouseCode"       , {type:"HIDDEN" , size: 0, maxLen: 0, mode:"HIDDEN"  , desc:"庫別代號"});
	vat.item.make(vnB_Detail, "brandCode"           , {type:"HIDDEN" , size: 0, maxLen: 0, mode:"HIDDEN"  , desc:"品牌"});
	vat.item.make(vnB_Detail, "orderTypeCode"       , {type:"TEXT" ,  size: 3, maxLen: 3, mode:"READONLY", desc:"單別"});
	vat.item.make(vnB_Detail, "name"                , {type:"TEXT" , view:"", size: 20, maxLen:20, mode:"READONLY", desc:"單別名稱"});
	vat.item.make(vnB_Detail, "beginningOnHandQuantity"               , {type:"HIDDEN" , size:0, maxLen:0, mode:"HIDDEN", desc:"期初庫存"});
	vat.item.make(vnB_Detail, "dayTotal"            , {type:"TEXT" , view:"", size:5, maxLen:5, mode:"READONLY", desc:"日總量"});
	vat.item.make(vnB_Detail, "sum"                 , {type:"TEXT" , view:"", size: 1, maxLen:1, mode:"READONLY", desc:"累計變動"});
	vat.item.make(vnB_Detail, "currentOnHand"       , {type:"TEXT" , view:"", size: 1, maxLen:1, mode:"READONLY", desc:"剩餘庫存"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["id.year","id.month","id.day","id.itemCode","id.orderTypeCode"],
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
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);	
}


function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");
    /*
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
	});*/
	var today = new Date();
	var year = parseInt(today.getFullYear());
	var monthString = ("0" + (today.getMonth()+1));
	var month = monthString.substr(monthString.length-2);

	var processString = "process_object_name=imItemOnHandViewService&process_object_method_name=getAJAXSearchPageDataOnHandModify" +
                      "&loginBrandCode"     + "=" +  vat.bean().vatBeanOther.loginBrandCode         +
                      "&itemCode"           + "=" +  vat.bean().vatBeanOther.itemCode               +
	                  "&warehouseCode"      + "=" +  vat.bean().vatBeanOther.warehouseCode          +
	                  "&beginningYear"      + "=" +  vat.item.getValueByName("#F.beginningYear")    +
	                  "&beginningMonth"     + "=" +  vat.item.getValueByName("#F.beginningMonth")   +
	                  "&endingYear"         + "=" +  vat.item.getValueByName("#F.endingYear")       +
	                  "&endingMonth"        + "=" +  vat.item.getValueByName("#F.endingMonth")      +
	                  "&endingDay"          + "=" +  vat.item.getValueByName("#F.endingDay")        ;
	                  //"&brandCode"     + "=" + document.forms[0]["#brandCode" ].value ;
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
    //alert("searchService");
    //vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
    //alert("timeScope:"+vat.bean().timeScope);
    updateBeginingOnHand();
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
	
	//$.unblockUI();
	
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setStyleByName("#B.view" , "display", "none");
		//vat.item.setStyleByName("#B.exportXLS" , "display", "none");
		//vat.item.setStyleByName("#B.exportTXT" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
	    //if("" == vat.bean().vatBeanOther.vatPickerId)
			//vat.item.setStyleByName("#B.view" , "display", "none");
		//else
			//vat.item.setStyleByName("#B.view" , "display", "inline");

		//vat.item.setStyleByName("#B.exportXLS" , "display", "inline"); // 查詢有值，才顯示匯出按鈕
		//vat.item.setStyleByName("#B.exportTXT" , "display", "inline"); // 查詢有值，才顯示匯出按鈕
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
    var vBrandCode = vat.item.getGridValueByName("brandCode", nItemLine);
    var vOrderTypeCode = vat.item.getGridValueByName("orderTypeCode", nItemLine);
    var vYear = vat.item.getGridValueByName("year", nItemLine);
    var vMonth = vat.item.getGridValueByName("month", nItemLine);
    var vDay = vat.item.getGridValueByName("day", nItemLine);
    //var vOrderTypeCode = vat.bean().vatBeanOther.orderTypeCode;
    //if(null != vFormId && "" != vFormId && 0 != vFormId){
    //	var url = "/erp/Im_OnHand:create:20170912.page?formId=" + vFormId;
	//	sc=window.open(url, '庫存查詢作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	//	sc.resizeTo((screen.availWidth),(screen.availHeight));
	//	sc.moveTo(0,0);
	//}
	var url = "/erp/Im_OnHand:create:20171105.page?itemCode="      + vItemCode      + 
												  "&warehouseCode="+ vWarehouseCode +
												  //"&brandCode="    + vBrandCode     +
												  "&orderTypeCode="+ vOrderTypeCode +
												  "&year="         + vYear          +
												  "&month="        + vMonth         +
												  "&day="          + vDay           ;
		sc=window.open(url, '單據查詢作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.resizeTo((screen.availWidth),(screen.availHeight));
		sc.moveTo(0,0);
}

function updateBeginingOnHand(){

    var searchKey = vat.item.getValueByName("#F.posBroCode").replace(/^\s+|\s+$/, '').toUpperCase();
    var brandCode = document.forms[0]["#loginBrandCode"].value;

    vat.ajax.XHRequest(
          {
            post: "process_object_name=imItemOnHandViewService&process_object_method_name=getBeginningOnHand"+
                   "&loginBrandCode"     + "=" +  vat.bean().vatBeanOther.loginBrandCode   +
                      "&itemCode"           + "=" +  vat.bean().vatBeanOther.itemCode         +
	                  "&warehouseCode"      + "=" +  vat.bean().vatBeanOther.warehouseCode    +
	                  "&year"               + "=" +  vat.item.getValueByName("#F.beginningYear")       +
	                  "&month"              + "=" +  vat.item.getValueByName("#F.beginningMonth")      ,
            find: function checkItemCode(oXHR){
                   vat.item.setValueByName("#F.beginningOnHandQuantity",vat.ajax.getValue("beginningOnHand", oXHR.responseText));

               }   
        });

}
