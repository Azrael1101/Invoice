/***
 *	檔案：cmBlockDeclarationSearch.js
 *	說明：報關單鎖定查詢頁面
 *  <pre>
 *  	Created by Weichun
 *  	All rights reserved.
 *  </pre>
 */

var afterSavePageProcess = "";
var vnB_Detail = 2;
var vnB_Header = 1;
function kweImBlock(){

  kweSearchInitial();
  kweHeader();
  kweButtonLine();
  kweDetail();

}

// 搜尋初始化
function kweSearchInitial(){

  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther =
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     ].value,
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value
	    };
  }
}

// 可搜尋的欄位
function kweHeader() {
    var allStatus = [
        ["", "", "true"],
        ["暫存", "簽核中", "簽核完成"],
        ["SAVE", "SIGNING", "FINISH"]
    ];

    var allOrderTypeCodes = [
        ["", "", "true"],
        ["CMB"],
        ["CMB"]
    ];

    var allOrderBy = [
        ["", "", "true"],
        ["單號", "狀態", "最後更新日"],
        ["orderNo", "status", "lastUpdateDate"]
    ];
    var vatPickerId = vat.bean().vatBeanOther.vatPickerId;
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"報關單庫存鎖定作業查詢", rows:[
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.orderTypeCode", 	type:"LABEL"  , value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 	type:"SELECT", bind:"orderTypeCode", init:allOrderTypeCodes}]},
				{items:[{name:"#L.orderNo", 		type:"LABEL"  , value:"單號"}]},
				{items:[{name:"#F.orderNo", 		type:"TEXT"   , size:20}]},
			 	{items:[{name:"#L.status", 			type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 			type:"SELECT", 	bind:"status", init:allStatus, size:10 }]},
				{items:[{name:"#L.orderBy", 		type:"LABEL", 	value:"排序" }]},
				{items:[{name:"#F.orderBy", 		type:"SELECT", 	bind:"orderBy", init:allOrderBy, size:10 }]}
			]}
		],
		beginService:"",
		closeService:""
	});

}

function kweButtonLine(){
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
	 			{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8p                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 x;'"}]}
	  ],
		beginService:"",
		closeService:""
	});
}

function kweDetail(){

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

    vat.item.make(vnB_Detail, "indexNo"       	, {type:"IDX"  , size:2,             desc:"序號" });
    vat.item.make(vnB_Detail, "orderTypeCode"	, {type:"TEXT" , size:5,  maxLen:15, mode:"READONLY", desc:"單別" });
	vat.item.make(vnB_Detail, "orderNo" 		, {type:"TEXT" , size:15, maxLen:15, mode:"READONLY", desc:"單號" });
	vat.item.make(vnB_Detail, "statusName"     	, {type:"TEXT" , size:10, maxLen:10, mode:"READONLY", desc:"單據狀態" });
	vat.item.make(vnB_Detail, "lastUpdatedBy"   , {type:"TEXT" , size:6,  maxLen:6,  mode:"READONLY", desc:"最後更新人員" });
	vat.item.make(vnB_Detail, "lastUpdateDate"  , {type:"DATE" , size:10, maxLen:10, mode:"READONLY", desc:"最後更新日期" });
	vat.item.make(vnB_Detail, "headId" 			, {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId"],
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
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

// 查詢點下執行
function loadBeforeAjxService() {
    // alert("loadBeforeAjxService");
    var processString = "process_object_name=cmBlockDeclarationService&process_object_method_name=getAJAXSearchPageData" 
    				  + "&loginBrandCode" + "=" + vat.bean().vatBeanOther.loginBrandCode 
    				  + "&orderTypeCode" + "=" + vat.item.getValueByName("#F.orderTypeCode") 
    				  + "&orderNo" + "=" + vat.item.getValueByName("#F.orderNo") 
    				  + "&status" + "=" + vat.item.getValueByName("#F.status");
    				  + "&orderBy" + "=" + vat.item.getValueByName("#F.orderBy");

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
		processString = "process_object_name=cmBlockDeclarationService&process_object_method_name=saveSearchResult";
	}
	return processString;
}

// 檢視按下後的動作
function doClosePicker(){
	vat.block.pageSearch(vnB_Detail, {
		funcSuccess :
			function(){
				vat.block.submit(
    		    	function(){ return "process_object_name=cmBlockDeclarationService&process_object_method_name=getSearchSelection";
    		                  },
    		                  { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail}
    		    );
    		}
    	}
	);
}

// 查詢按下後
function doSearch(){
	if(vat.item.getValueByName("#F.orderNo")!= "" && vat.item.getValueByName("#F.orderTypeCode") == ""){
		alert("請輸入單別！");
		return;
	}
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

// 清除
function resetForm(){
    vat.item.setValueByName("#F.orderTypeCode", "");
	vat.item.setValueByName("#F.orderNo", "");
    vat.item.setValueByName("#F.status", "");
}

// 開啟視窗
function openModifyPage(){

    var nItemLine = vat.item.getGridLine();
    var headId = vat.item.getGridValueByName("headId", nItemLine);
    var orderTypeCode = vat.item.getGridValueByName("orderTypeCode", nItemLine);
	if(!(headId == "" || headId == "0")){
    	var url = "/erp/Cm_DeclarationOnHand:Create:20111219.page?formId=" + headId + "&orderTypeCode="+orderTypeCode;
		//alert(url);
		sc = window.open(url, "報關單鎖定作業", 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	    sc.resizeTo((screen.availWidth),(screen.availHeight));
		sc.moveTo(0,0);
    }
}