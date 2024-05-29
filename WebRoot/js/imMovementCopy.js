/***
 *	檔案 : imMovementCopy.js
 *	說明： 調撥單單據複製
 *  <pre>
 *  	Created by Weichun
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_master = 2;
var vnB_Detail = 3;

// 頁面initial
function kweImBlock() {
    if (typeof document.forms[0]["#loginBrandCode"] != 'undefined') {
        vat.bean().vatBeanOther = {
            loginBrandCode: document.forms[0]["#loginBrandCode"].value,
            loginEmployeeCode: document.forms[0]["#loginEmployeeCode"].value,
            currentRecordNumber: 0,
            lastRecordNumber: 0
        };

        vat.bean.init(
        function () {
            return "process_object_name=imMovementService&process_object_method_name=executeCopyInitial";
        }, {
            other: true
        });
    }

    kweButtonLine();
    kweImHeader();
    kweImDetail()
    vat.item.SelectBind(vat.bean("allOrderTypes"), {itemName: "#F.preOrderTypeCode"});
    vat.item.SelectBind(vat.bean("allOrderTypes"), {itemName: "#F.orderTypeCode"});
    vat.item.bindAll();
}

// 按鈕列
function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif"  , eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.submit"       , type:"IMG"     , value:"送出",  src:"./images/button_submit.gif", eClick:"doSubmit()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:function(){return ""}	,
		closeService:function(){return ""}
	});
}

// 單頭資料
function kweImHeader(){
var vsRowStyle= "T2" == vat.bean("loginBrandCode")?"":" style= 'display:none;'";
var allStatus = [["","","true"],
                 ["暫存中","簽核中","待轉出","待轉入","簽核完成","結案","待確認"],
                 ["SAVE","SIGNING","WAIT_OUT","WAIT_IN","FINISH","CLOSE","UNCONFIRMED"]];
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"調撥單單據複製作業", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#L.deliveryDate", 	type:"LABEL" ,  value:"選擇單據轉出日期"}]},
	 	{items:[{name:"#F.deliveryDate", 	type:"DATE"  ,  bind:"deliveryDate", size:12}]},
	 	{items:[{name:"#L.reserveDate" , 	type:"LABEL" ,  value:"預產生單據轉出日期"}]},
	 	{items:[{name:"#F.reserveDate" , 	type:"DATE"  ,  bind:"reserveDate", size:12}]}]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.orderTypeCode", 	type:"LABEL" , value:"選擇單據單別"}]},
	 	{items:[{name:"#F.orderTypeCode", 	type:"SELECT",  bind:"orderTypeCode", size:1}]},
	 	{items:[{name:"#L.preOrderTypeCode",type:"LABEL" , value:"預產生單據單別"}]},
	 	{items:[{name:"#F.preOrderTypeCode",type:"SELECT",  bind:"preOrderTypeCode", size:1}]}]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.orderNo", 		type:"LABEL" , value:"選擇單據單號"}]},
	 	{items:[{name:"#F.orderNo", 		type:"TEXT"  ,  bind:"orderNo", size:20, eChange:'checkStatus()'}]},
	 	{items:[{name:"#L.deliverWarehouseCode"     , type:"LABEL" , value:"預產生轉出倉庫"}]},
	 	{items:[{name:"#F.deliverWarehouseCode"     , type:"TEXT"  , bind:"deliverWarehouseCode" , size:20 }]}]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.status", 			type:"LABEL" ,  value:"選擇單據狀態"}]},
	 	{items:[{name:"#F.status", 			type:"SELECT",  bind:"status", size:1, init:allStatus}]},
	 	{items:[{name:"#L.arrivalWarehouseCode"     , type:"LABEL" , value:"預產生轉入倉庫"}]},
	 	{items:[{name:"#F.arrivalWarehouseCode"     , type:"TEXT"  , bind:"arrivalWarehouseCode",size:20}]}]}
 	 ],
		beginService:"",
		closeService:""
	});
}

// 明細資料
function kweImDetail(){

    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true; // 是否可勾選detail
  	//var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
  	var vbSelectionType = "CHECK";
    var vnB_Detail = 2;
    vat.item.make(vnB_Detail, "checkbox"                  , {type:"XBOX"});
    vbSelectionType = "CHECK";

    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"       });
	vat.item.make(vnB_Detail, "orderTypeCode"             , {type:"TEXT" , size: 3, maxLen:20, mode:"READONLY", desc:"單別"   });
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" , size:11, maxLen:20, mode:"READONLY", desc:"單號"   });
	vat.item.make(vnB_Detail, "deliveryDate"              , {type:"DATE" , size: 8, maxLen:20, mode:"READONLY", desc:"轉出日期"});
	vat.item.make(vnB_Detail, "deliveryWarehouseCode"     , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"轉出庫別"});
	vat.item.make(vnB_Detail, "arrivalDate"               , {type:"DATE" , size:12, maxLen:12, mode:"READONLY", desc:"轉入日期"});
	vat.item.make(vnB_Detail, "arrivalWarehouseCode"      , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"轉入庫別"});
	vat.item.make(vnB_Detail, "originalOrderTypeCode"     , {type:"TEXT" , size: 5, maxLen:12, mode:"READONLY", desc:"來源單別"});
	vat.item.make(vnB_Detail, "orignialOrderNo"           , {type:"TEXT" , size:13, maxLen:20, mode:"READONLY", desc:"來源單號"});
	vat.item.make(vnB_Detail, "itemCount"                 , {type:"NUMM" , size: 8, maxLen:20, mode:"READONLY", dec:0, desc:"件數", bind:"itemCount"});
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"});
	vat.item.make(vnB_Detail, "remark1"               	  , {type:"TEXT" , size: 8, maxLen:12, mode:"HIDDEN", desc:"remark1"});
	vat.item.make(vnB_Detail, "headId"                    , {type:"ROWID"});

	vat.block.pageLayout(vnB_Detail, {	id                  : "vatDetailDiv",
									  	pageSize            : 10,
										searchKey           : ["headId"],
										pickAllService		: function (){return selectAll();},
										selectionType       : vbSelectionType,
										indexType           : "AUTO",
								        canGridDelete       : vbCanGridDelete,
										canGridAppend       : vbCanGridAppend,
										canGridModify       : vbCanGridModify,
										loadBeforeAjxService: "loadBeforeAjxService()",
										loadSuccessAfter    : "pageLoadSuccess()",
										saveBeforeAjxService: "saveBeforeAjxService()",
										saveSuccessAfter    : "saveSuccessAfterDo()",
										blockId             : "2",
										indicate            : "" // 雙擊打開編輯視窗
									 });

}

function loadBeforeAjxService(){

	var vCmMovementIsNotNull = ("" ==  vat.bean().vatBeanOther.status?"N":"Y");
	var processString = "process_object_name=imMovementService&process_object_method_name=searchAJAXSearchPageData" +
                      	"&loginBrandCode" + "=" + vat.bean().vatBeanOther.loginBrandCode +
	                  	"&orderTypeCode"  + "=" + vat.item.getValueByName("#F.orderTypeCode"      ) + // 單別
	                  	"&orderNo"        + "=" + vat.item.getValueByName("#F.orderNo"         	  ) + // 單號
	                 	"&status"         + "=" + vat.item.getValueByName("#F.status"          	  ) + // 狀態
	                  	"&deliveryDate"   + "=" + vat.item.getValueByName("#F.deliveryDate"		  )  // 轉出日期

	return processString;
}

// 儲存資料到temp table
function saveBeforeAjxService() {
    var processString = "";
    processString = "process_object_name=imMovementService&process_object_method_name=saveSearchResult";
    return processString;
}

function pageLoadSuccess() {
    if (vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize && vat.block.getGridObject(2).lastIndex == 1) {
        alert("您輸入條件查無資料");
    }
}


// 查詢功能
function doSearch() {
	afterSavePageProcess = "";
    vat.block.submit(function () {
        return "process_object_name=tmpAjaxSearchDataService" + "&process_object_method_name=deleteByTimeScope&timeScope=" + vat.bean().vatBeanOther.timeScope
    }, {
        other: true,
        funcSuccess: function () {
            vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
        }
    });

}

// 單據狀態
function checkStatus() {
	vat.bean().vatBeanOther.orderNo = vat.item.getValueByName("#F.orderNo");
	vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
  	vat.bean().vatBeanOther.orderNo = vat.item.getValueByName("#F.orderNo");
    vat.block.submit(function () {
        return "process_object_name=imMovementService&process_object_method_name=getImStatus";
    }, {
        other: true,
        picker: false,
        funcSuccess: function () {
            vat.item.setValueByName("#F.status", vat.bean().vatBeanOther.status);
            vat.item.setValueByName("#F.statusCName", vat.bean().vatBeanOther.statusCName);

        }
    });
}

// 全選
function selectAll() {
	//alert("全選");
    var processString = "";
    var isProcessing = false;
    //alert(vat.bean().vatBeanOther.isAllClick);
    if ("N" == vat.bean().vatBeanOther.isAllClick || typeof vat.bean().vatBeanOther.isAllClick == "undefined") {
        if (confirm("本功能僅協助勾選前100筆之查詢結果，是否執行?")) isProcessing = true;
    } else {
        isProcessing = true;
    }
    if (isProcessing)
    	processString = "process_object_name=imMovementService&process_object_method_name=updateAllSelected";
    return processString;

}

// 儲存完畢後執行的動作
function saveSuccessAfterDo() {
    if (afterSavePageProcess == 'submit') { // 如果執行的動作是submit，才作以下的動作

        vat.bean().vatBeanOther.timeScope = vat.block.getValue(vnB_Detail = 2, "timeScope");
        vat.bean().vatBeanOther.searchKey = vat.block.getValue(vnB_Detail = 2, "searchKey");

        vat.bean().vatBeanOther.reserveDate = vat.item.getValueByName("#F.reserveDate");
        vat.bean().vatBeanOther.preOrderTypeCode = vat.item.getValueByName("#F.preOrderTypeCode");
        vat.bean().vatBeanOther.deliverWarehouseCode = vat.item.getValueByName("#F.deliverWarehouseCode");
        vat.bean().vatBeanOther.arrivalWarehouseCode = vat.item.getValueByName("#F.arrivalWarehouseCode");

        vat.block.submit(function () {
            return "process_object_name=imMovementAction&process_object_method_name=performCopyTransaction";
        }, {
            other: true,
            picker: false,
            funcSuccess: function () {}
        });
    }
}

// 執行送出
function doSubmit() {
    if(!confirm("確認是否送出")){
    	return;
    }
    if (vat.item.getValueByName("#F.reserveDate") == "") {
        alert("請輸入預產生的單據轉出日期！");
        return;
    }
    if (vat.item.getValueByName("#F.preOrderTypeCode") == "") {
        alert("請輸入預產生單據單別！");
        return;
    }
    if (vat.item.getValueByName("#F.deliverWarehouseCode") == "") {
        alert("請輸入預產生的轉出庫！");
        return;
    }
    if (vat.item.getValueByName("#F.arrivalWarehouseCode") == "") {
        alert("請輸入預產生的轉入庫！");
        return;
    }
    vat.block.pageSearch(vnB_Detail); // 儲存資料
    afterSavePageProcess = "submit"; // 修改執行的功能

}