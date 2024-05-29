/***
 *	檔案：cmBlockDeclaration.js
 *	說明：報關單鎖定功能
 *  <pre>
 *  	Created by Weichun
 *  	All rights reserved.
 *  </pre>
 */

var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock() {
    searchInitial();
    kweButtonLine();
    headerInitial();
    detailInitial();
    doFormAccessControl();
    if (vat.bean().vatBeanOther.formId != "" && vat.bean().vatBeanOther.formId != undefined) vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

// 搜尋初始化
function searchInitial(){

  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther =
  	    {
  	     loginBrandCode     	: document.forms[0]["#loginBrandCode"    	].value,
  	     loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" 	].value,
  	     orderTypeCode        	: document.forms[0]["#orderTypeCode"       	].value,
  	     formId        			: document.forms[0]["#formId"       		].value,
  	     processId          	: document.forms[0]["#processId"         ].value, 
  	     assignmentId       	: document.forms[0]["#assignmentId"      ].value
	    };

		vat.bean.init(
	  		function(){
				return "process_object_name=cmBlockDeclarationAction&process_object_method_name=performInitial";
	    	},{
	    		other: true
    	});
  }
}

// 可搜尋的欄位
function headerInitial() {
    var allOrderTypeCodes = vat.bean("allOrderTypeCodes");
    vat.block.create(vnB_Header, {
        id: "vatBlock_Head",
        generate: true,
        table: "cellspacing='1' class='default' border='0' cellpadding='2'",
        title: "報關單庫存鎖定作業",
        rows: [
	 		{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 	type:"LABEL"  , value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 	type:"SELECT" , bind:"orderTypeCode", size:20, init:allOrderTypeCodes, mode:"READONLY"}]},
			 	{items:[{name:"#L.orderNo", 		type:"LABEL"  , value:"單號"}]},
				{items:[{name:"#F.orderNo", 		type:"TEXT"   , bind:"orderNo", size:25, mode:"READONLY"},
	  		 			{name:"#F.headId", 			type:"TEXT"   , bind:"headId", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.status", 			type:"LABEL"  ,	value:"單據狀態" }]},
				{items:[{name:"#F.status", 			type:"TEXT"   , bind:"status", size:12, mode:"HIDDEN"},
	  		 			{name:"#F.statusName", 		type:"TEXT"   , bind:"statusName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.createdBy", 		type:"LABEL"  , value:"填單人員" }]},
				{items:[{name:"#F.createdBy", 		type:"TEXT"   , bind:"createdBy", mode:"HIDDEN" },
	  		 			{name:"#F.createdByName", 	type:"TEXT"   , bind:"createdByName", back:false, mode:"READONLY"}]}
			]}
		],
		beginService:"",
		closeService:""
	});

}

function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	            {name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  ,
	 									 openMode:"open",
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,
	 									 serviceAfterPick:function(){/*afterImportSuccess();*/}},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,
	 			{name:"#B.first"        , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 		//	{name:"SPACE"           , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 		//	{name:"SPACE"           , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"         , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 		//	{name:"SPACE"           , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"         , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 2, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:"/"},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 2, mode:"READONLY" },
	 			{name:"SPACE"           , type:"LABEL"  ,value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:function(){return ""}	,
		closeService:function(){return ""}
	});

}

function detailInitial() {

    var vbCanGridDelete = true;
    var vbCanGridAppend = true;
    var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "indexNo"         	, {type:"IDX"  , desc:"序號"});
    //vat.item.make(vnB_Detail, "customsWarehouseCode", {type:"TEXT" , size:8, view:"", mode:"READONLY", desc:"海關關別"      });
	vat.item.make(vnB_Detail, "declarationNo"       , {type:"TEXT" , size:16, view:"", mode:"", desc:"報關單號", eChange:"showItemInfo()"});
	vat.item.make(vnB_Detail, "declarationSeq"  	, {type:"TEXT" , size: 4, view:"", mode:"", desc:"報關序號", eChange:"showItemInfo()"});
	vat.item.make(vnB_Detail, "declDate"       		, {type:"DATE" , size:12, view:"", mode:"READONLY", desc:"報關日期"      });
	//vat.item.make(vnB_Detail, "importDate"    		, {type:"DATE" , size:12, view:"", mode:"READONLY", desc:"進口日期"      });
	//vat.item.make(vnB_Detail, "warehouseInDate"    	, {type:"DATE" , size:12, view:"", mode:"READONLY", desc:"進倉日期"      });
	vat.item.make(vnB_Detail, "orderNo"    			, {type:"TEXT" , size:12, view:"", mode:"READONLY", desc:"進貨單號"      });
	vat.item.make(vnB_Detail, "customsItemCode"    	, {type:"TEXT" , size:18, view:"", mode:"", desc:"品號", eChange:"showItemInfo()"});
	vat.item.make(vnB_Detail, "currentOnHandQty"    , {type:"NUMB" , size:10, view:"", mode:"READONLY", desc:"可被鎖定的有效庫存"		});
	vat.item.make(vnB_Detail, "blockOnHandQuantity" , {type:"NUMB" , size: 6, view:"", desc:"報單鎖定庫存"});
	vat.item.make(vnB_Detail, "description" 		, {type:"TEXT" , size:20, view:"", desc:"備註"});
	vat.item.make(vnB_Detail, "lindId" 				, {type:"TEXT" , size: 1, view:"", mode:"HIDDEN"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["declarationNo","declarationSeq","customsItemCode"],
														canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});

}

//建立新資料按鈕
function createNewForm() {
    if (confirm("此作業將清除原先輸入資料，請確認是否執行?")) {
        refreshForm("");
    }
}

// 刷新頁面
function refreshForm(vsfunctionCode) {

    document.forms[0]["#formId"].value = vsfunctionCode;
    document.forms[0]["#processId"].value = "";
    document.forms[0]["#assignmentId"].value = "";
    vat.bean().vatBeanOther.formId 		 = document.forms[0]["#formId"].value;
    vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
    vat.block.submit(
    function () {
        return "process_object_name=cmBlockDeclarationAction&process_object_method_name=performInitial";
    }, {
        other: true,
        funcSuccess: function () {
            vat.item.bindAll();
            //vat.tabm.displayToggle(0, "xTab2", vat.item.getValueByName("#F.orderStatus") != "SAVE" && vat.item.getValueByName("#F.orderStatus") != "UNCONFIRMED", false, false);
			vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
            //doFormAccessControl();
        }
    });
}

//依狀態鎖form
function doFormAccessControl() {
    var status = vat.item.getValueByName("#F.status");
    var orderNoPrefix = vat.item.getValueByName("#F.orderNo").substring(0, 3);
    var processId = null;
    if (vat.bean().vatBeanOther.processId != null) {
        processId = vat.bean().vatBeanOther.processId;
    }

    vat.item.setAttributeByName("vatBlock_Head", "readOnly", true);
    //vat.item.setAttributeByName("vatDetailDiv", "readOnly", false, true, true);
    //===========================buttonLine==============================
    vat.item.setStyleByName("#B.new", "display", "inline");
    vat.item.setStyleByName("#B.submit", "display", "inline");
    vat.item.setStyleByName("#B.save", "display", "inline");
    vat.item.setStyleByName("#B.void", "display", "inline");
    vat.item.setStyleByName("#B.message", "display", "inline");

    // 流程內
    if (processId != null && processId != 0) { // 從待辦事項進入
        vat.item.setStyleByName("#B.new", "display", "none");
    } else { // 查詢回來
        if (orderNoPrefix == "TMP") {

        } else {
        	vat.item.setStyleByName("#B.new", "display", "none");
            vat.item.setStyleByName("#B.submit", "display", "none");
            vat.item.setStyleByName("#B.save", "display", "none");
            vat.item.setStyleByName("#B.void", "display", "none");
            vat.item.setStyleByName("#B.message", "display", "none");
            vat.item.setStyleByName("#B.import", "display", "none");
            vat.item.setAttributeByName("vatBlock_Head", "readOnly", true);
            vat.item.setAttributeByName("vatDetailDiv", "readOnly", true);
        }
    }

    if (status == "FINISH" || status == "VOID" || status == "CLOSE") {
        vat.item.setStyleByName("#B.new", "display", "none");
        vat.item.setStyleByName("#B.submit", "display", "none");
        vat.item.setStyleByName("#B.save", "display", "none");
        vat.item.setStyleByName("#B.message", "display", "none");
        vat.item.setStyleByName("#B.import", "display", "none");
        vat.item.setAttributeByName("vatBlock_Head", "readOnly", true);
        vat.item.setAttributeByName("vatDetailDiv", "readOnly", true);
    }
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		//alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		//vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

//第一次載入 重新整理
function loadBeforeAjxService(){
	var processString = "";
	processString = "process_object_name=cmBlockDeclarationService&process_object_method_name=getAJAXPageData" + 
	                    "&headId=" + vat.item.getValueByName("#F.headId");;
	return processString;		
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveSuccessAfter(){

}

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

function saveBeforeAjxService() {
    var processString = "process_object_name=cmBlockDeclarationService&process_object_method_name=updateAJAXPageLinesData"
    				  + "&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status");
    return processString;
}

// 查詢按下後
function doSearch() {
    vat.block.submit(function () {
        return "process_object_name=tmpAjaxSearchDataService" + "&process_object_method_name=deleteByTimeScope&timeScope=" + vat.bean().vatBeanOther.timeScope
    },
    {
        other: true,
        funcSuccess: function () {
            vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
        }
    });
}

// 按下存檔後
function doSubmit(formAction) {
    var alertMessage = "是否確定送出?";
    var formId = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');
    var processId = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
    var status = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
    var employeeCode = vat.bean().vatBeanOther.loginEmployeeCode.replace(/^\s+|\s+$/, '');
    var inProcessing = !(processId == null || processId == "" || processId == 0);
    var orderNoPrefix = vat.item.getValueByName("#F.orderNo").substring(0, 3);
    var processString = "";

    if ("SUBMIT" == formAction) {
        alertMessage = "是否確定送出？";
    } else if ("SAVE" == formAction) {
        alertMessage = "是否確定暫存？";
    } else if ("VOID" == formAction) {
        alertMessage = "是否確定作廢？";
    }

    if (confirm(alertMessage)) {
        if ((orderNoPrefix == "TMP" && status == "SAVE") || status == "UNCONFIRMED" || (inProcessing && (status == "SAVE" || status == "SIGNING" || status == "REJECT"))) {
            
        	vat.block.pageDataSave(vnB_Detail, {
                saveSuccessAfter: function () {
                    vat.bean().vatBeanOther.action = formAction;
                    vat.block.submit(function () {
                        return "process_object_name=cmBlockDeclarationAction&process_object_method_name=performTransaction";;
                    }, {
                        bind: true,
                        link: true,
                        other: true,
                        funcSuccess: function () {
                            //vat.block.pageRefresh(vnB_Detail);
                        }
                    });
                }
            });
        	
        }
    }
}

function closeWindows(closeType) {
    var isExit = true;
    if ("CONFIRM" == closeType) {
        isExit = confirm("是否確認離開?");
    }
    if (isExit) window.top.close();

}

//sql明細匯出excel
function doExport(){
    var url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName=CM_ON_HAND" +
              "&fileType=XLS" +
              "&processObjectName=cmDeclarationOnHandService" +
              "&processObjectMethodName=getAJAXExportData" +
              "&brandCode=" + vat.bean().vatBeanOther.loginBrandCode +
              "&customsWarehouseCode=" + vat.item.getValueByName("#F.customsWarehouseCode") +
	          "&declarationNo=" + vat.item.getValueByName("#F.declarationNo") +
	          "&declarationSeqStart=" + vat.item.getValueByName("#F.declarationSeqStart") +
	          "&remainDays=" + vat.item.getValueByName("#F.remainDays") +
			  "&isOverZero=" + vat.item.getValueByName("#F.isOverZero") +
			  "&showZeroStock=" + vat.item.getValueByName("#F.showZeroStock") +
			  "&showNegativeStock=" + vat.item.getValueByName("#F.showNegativeStock") +
	          "&customsItemCode=" + vat.item.getValueByName("#F.customsItemCode") +
	          "&customsItemCodes=" + vat.item.getValueByName("#F.customsItemCodes") +
	          "&category01=" + vat.item.getValueByName("#F.category01") +
	          "&itemBrand=" + vat.item.getValueByName("#F.itemBrand") +
	          "&warehouseInDate=" + vat.item.getValueByName("#F.warehouseInDate");
    var width = "200";
    var height = "30";
    window.open(url, '報單可用庫存匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

}

//匯入資料
function importFormData(){
    var beanName = "CM_BLOCK_ON_HAND";
    var suffix ="";
	suffix =
		"&importBeanName=" + beanName +
		"&importFileType=XLS" +
        "&processObjectName=cmBlockDeclarationService" +
        "&processObjectMethodName=executeImportMovementItems" +
        "&arguments=" +vat.item.getValueByName("#F.headId")  +
        "&parameterTypes=LONG" +
        "&blockId=" + vnB_Detail;
	return suffix;
}

function showItemInfo() {
    var vLineId			= vat.item.getGridLine();
    var declarationNo 	= vat.item.getGridValueByName("declarationNo", vLineId);
    var declarationSeq 	= vat.item.getGridValueByName("declarationSeq", vLineId);
    var customsItemCode = vat.item.getGridValueByName("customsItemCode", vLineId);
    var brandCode 		= vat.bean().vatBeanOther.loginBrandCode;
    if (declarationNo != "" && declarationSeq != "" && customsItemCode != "") {
        vat.ajax.XHRequest({
            post: "process_object_name=cmBlockDeclarationService" 
            	+ "&process_object_method_name=getAJAXItemData" 
            	+ "&declarationNo=" + declarationNo 
            	+ "&declarationSeq=" + declarationSeq 
            	+ "&customsItemCode=" + customsItemCode 
            	+ "&brandCode=" + brandCode,
            find: function change(oXHR) {
                vat.item.setGridValueByName("declDate", vLineId, vat.ajax.getValue("declDate", oXHR.responseText));
                vat.item.setGridValueByName("orderNo", vLineId, vat.ajax.getValue("orderNo", oXHR.responseText));
                vat.item.setGridValueByName("currentOnHandQty", vLineId, vat.ajax.getValue("currentOnHandQty", oXHR.responseText));
            },
            fail: function changeError() {
                vat.item.setGridValueByName("declDate", vLineId, "");
                vat.item.setGridValueByName("orderNo", vLineId, "");
                vat.item.setGridValueByName("currentOnHandQty", vLineId, vat.ajax.getValue("currentOnHandQty", oXHR.responseText));
            }
        });
    }
}

//報單鎖定作業匯出明細
function exportFormData(){

	var url = "/erp/jsp/ExportFormData.jsp" +
              "?exportBeanName=CM_BLOCK_ON_HAND" +
              "&fileType=XLS" +
              "&processObjectName=cmBlockDeclarationService" +
              "&processObjectMethodName=findById" +
              "&gridFieldName=cmBlockDeclarationItems" +
              "&arguments=" + vat.item.getValueByName("#F.headId") +
              "&parameterTypes=LONG";

    var width = "200";
    var height = "30";
    vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : function(){
    			window.open(url, '報單鎖定作業匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});

}