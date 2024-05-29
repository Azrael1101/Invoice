/***
 *	檔案: imMovementMainSearch.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";

function kweImBlock(){
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();
  kweImDetail();

}


function kweImSearchInitial(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	 var orderTypeCode = "";
  	 if(document.forms[0]["#orderTypeCode"].value == null || document.forms[0]["#orderTypeCode"].value == ""){
  		// 新增T2A的品牌
  	 	orderTypeCode = "T2" == document.forms[0]["#loginBrandCode"].value ? "WTF": "T2A"==document.forms[0]["#loginBrandCode"].value ? "ETR" : "IMV";
  	 }else{
  	 	orderTypeCode = document.forms[0]["#orderTypeCode"].value;
  	 }
     vat.bean().vatBeanOther =
  	    {loginBrandCode       : document.forms[0]["#loginBrandCode"       ].value,
  	     loginEmployeeCode    : document.forms[0]["#loginEmployeeCode"    ].value,
  	     orderTypeCode        : orderTypeCode,
  	     deliveryWarehouseCode: document.forms[0]["#deliveryWarehouseCode"].value,
  	     arrivalWarehouseCode : document.forms[0]["#arrivalWarehouseCode" ].value,
  	     startDeliveryDate    : document.forms[0]["#startDeliveryDate"    ].value,
  	     endDeliveryDate      : document.forms[0]["#endDeliveryDate"      ].value,
  	     vatPickerId          : document.forms[0]["#vatPickerId"          ].value,
  	     status               : document.forms[0]["#status"               ].value
	    };
     vat.bean.init(function(){
		return "process_object_name=imMovementMainService&process_object_method_name=executeSearchInitial";
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
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"doClear()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.view"        , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"
	 			                       , eClick:vsViewFunction}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:"",
		closeService:""
	});
	if(null== vat.bean().vatBeanOther.vatPickerId || "" == vat.bean().vatBeanOther.vatPickerId)
		vat.item.setStyleByName("#B.view" , "display", "none");
}

function kweImHeader(){
//var vsRowStyle= "T2" == vat.bean("loginBrandCode")?"":" style= 'display:none;'";
var vsRowStyle= "T2" == vat.bean("loginBrandCode")?"":"HIDDEN";
var vsCmMovementName= "T2" == vat.bean("loginBrandCode")?"移倉單號":"";
var allOrderTypes=vat.bean("allOrderTypes");
var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
var allStatus = [["","",true],
                 ["暫存中","簽核中","待轉出","待轉入","簽核完成","結案","未確認","作廢"],
                 ["SAVE","SIGNING","WAIT_OUT","WAIT_IN","FINISH","CLOSE","UNCONFIRMED","VOID"]];
var allSortSeq = [["","",false],
                 ["由大到小","由小到大"],
                 ["desc","asc"]];

var allSortKey = [["","",false],
                 ["最後更新日","單號","轉出日期","轉入日期","轉出庫別","轉入庫別","來源單別","來源單號","件數","狀態"],
                 ["lastUpdateDate","orderNo","deliveryDate","arrivalDate","deliveryWarehouseCode","arrivalWarehouseCode","originalOrderTypeCode","originalOrderNo","itemCount","status"]];
var vOrderTypeCode = vat.bean().vatBeanOther.orderTypeCode;
var vOrderTypeMode = "";
if(vOrderTypeCode == null || vOrderTypeCode == ""){
	vOrderTypeMode="";
}
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"調撥單查詢作業", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode"            , type:"LABEL"  , value:"單別"}]},
	 {items:[{name:"#F.orderTypeCode"            , type:"SELECT" ,  bind:"orderTypeCode", mode:vOrderTypeMode, init:allOrderTypes, eChange:"changeOrderType()"}]},
	 {items:[{name:"#L.orderNo"                  , type:"LABEL"  , value:"單號"}]},
	 {items:[{name:"#F.startOrderNo"             , type:"TEXT"   ,  bind:"startOrderNo", size:12},
			 {name:"#L.between"                  , type:"LABEL"  , value:"至"},
	 		 {name:"#F.endOrderNo"               , type:"TEXT"   ,  bind:"endOrderNo"  , size:12}]},
	 {items:[{name:"#L.status"                   , type:"LABEL"  , value:"狀態"}]},
	 {items:[{name:"#F.status"                   , type:"SELECT" ,  bind:"status", size:12, init:allStatus}]},
	 {items:[{name:"#L.brandCode"                , type:"LABEL"  , value:"品牌"}]},
	 {items:[{name:"#F.brandCode"                , type:"TEXT"   ,  bind:"brandCode", size:6, mode:"HIDDEN"},
	 		 {name:"#F.brandName"                , type:"TEXT"   ,  bind:"brandName", back:false, size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryWarehouseCode"    , type:"LABEL" , value:"轉出倉庫"}]},
	 {items:[{name:"#F.deliveryWarehouseCode"    , type:"SELECT",  bind:"deliveryWarehouseCode", size:20, init:allDeliveryWarehouses}]},
	 {items:[{name:"#L.deliveryDate"             , type:"LABEL" , value:"轉出日期"}]},
	 {items:[{name:"#F.startDeliveryDate"        , type:"DATE"  ,  bind:"startDeliveryDate", size:12},
	         {name:"#L.between"                  , type:"LABEL" , value:"至"},
	         {name:"#F.endDeliveryDate"          , type:"DATE"  ,  bind:"endDeliveryDate", size:12}], td:" colSpan=3"},
	 {items:[{name:"#L.sortKey"                  , type:"LABEL" , value:"排序欄位"}]},
	 {items:[{name:"#F.sortKey"                  , type:"SELECT", bind:"sortKey", init:allSortKey}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.arrivalWarehouseCode"     , type:"LABEL" , value:"轉入倉庫"}]},
	 {items:[{name:"#F.arrivalWarehouseCode"     , type:"SELECT",  bind:"arrivalWarehouseCode", size:20, init:allArrivalWarehouses}]},
	 {items:[{name:"#L.arrivalDate"              , type:"LABEL" , value:"轉入日期"}]},
	 {items:[{name:"#F.startArrivalDate"         , type:"DATE"  ,  bind:"startArrivalDate", size:12},
	         {name:"#L.between"                  , type:"LABEL" , value:"至"},
	         {name:"#F.endArrivalDate"           , type:"DATE"  ,  bind:"endArrivalDate", size:12}], td:" colSpan=3"},
	 {items:[{name:"#L.sortSeq"                  , type:"LABEL" , value:"排序方向"}]},
	 {items:[{name:"#F.sortSeq"                  , type:"SELECT", bind:"sortSeq", init:allSortSeq}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.packedBy"                 , type:"LABEL" , value:"揀貨人員"}]},
	 {items:[{name:"#F.packedBy"                 , type:"TEXT"  ,  bind:"packedBy" , size:6,eChange:'getEmployeeInfo("packedBy")'},
	         {name:"#F.packedByName"             , type:"TEXT",   bind:"packedByName", size:6,  mode:"READONLY"}]},
	 {items:[{name:"#L.comfirmedBy"              , type:"LABEL" , value:"複核人員"}]},
	 {items:[{name:"#F.comfirmedBy"              , type:"TEXT"  ,  bind:"comfirmedBy", size:6,eChange:'getEmployeeInfo("comfirmedBy")'},
	         {name:"#F.comfirmedByName"          , type:"TEXT",bind:"comfirmedByName", size:6,  mode:"READONLY"}]},
	 {items:[{name:"#L.receiptedBy"              , type:"LABEL" , value:"收貨人員"}]},
	 {items:[{name:"#F.receiptedBy"              , type:"TEXT"  ,  bind:"receiptedBy", size:6,eChange:'getEmployeeInfo("receiptedBy")'},
	         {name:"#F.receiptedByName"          , type:"TEXT",  bind:"receiptByName", size:6,  mode:"READONLY"}]},
	 {items:[{name:"#L.cmMovement"               , type:"LABEL" , value:vsCmMovementName}]},
	 {items:[{name:"#F.cmMovement"               , type:"TEXT"  ,  bind:"cmMovement", size:20, mode:vsRowStyle}], td:" colSpan=3"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.originalOrderTypeCode"    , type:"LABEL" , value:"來源單別"}]},
	 {items:[{name:"#F.originalOrderTypeCode"    , type:"TEXT"  ,  bind:"originalOrderTypeCode", size:5}]},
	 {items:[{name:"#L.originalOrderNo"          , type:"LABEL" , value:"來源單號"}]},
	 {items:[{name:"#F.originalStartOrderNo"     , type:"TEXT"  ,  bind:"originalStartOrderNo", size:13},
			 {name:"#L.between"                  , type:"LABEL" , value:"至"},
	 		 {name:"#F.originalEndOrderNo"       , type:"TEXT"  ,  bind:"originalEndOrderNo"  , size:13}]},
	 {items:[{name:"#L.itemCode"              	 , type:"LABEL" , value:"品號"}]},
	 {items:[{name:"#F.itemCode"              	 , type:"TEXT"  ,  bind:"itemCode", size:20}]},
	 {items:[{name:"#L.description"              , type:"LABEL" , value:"備註"}]},
	 {items:[{name:"#F.description"              , type:"TEXT"  ,  bind:"description", size:20}]}]}
	  ],
		beginService:"",
		closeService:""
	});
	 vat.item.setValueByName("#F.orderTypeCode"        , vat.bean().vatBeanOther.orderTypeCode);
	 vat.item.setValueByName("#F.deliveryWarehouseCode", vat.bean().vatBeanOther.deliveryWarehouseCode);
	 vat.item.setValueByName("#F.arrivalWarehouseCode" , vat.bean().vatBeanOther.arrivalWarehouseCode);
	 vat.item.setValueByName("#F.startDeliveryDate"    , vat.bean().vatBeanOther.startDeliveryDate);
	 vat.item.setValueByName("#F.endDeliveryDate"      , vat.bean().vatBeanOther.endDeliveryDate);
     vat.item.setAttributeByName("#F.status", "readOnly", "" !=  vat.bean().vatBeanOther.status);
	 vat.item.setAttributeByName("#F.startDeliveryDate", "readOnly", "" !=  vat.bean().vatBeanOther.startDeliveryDate);
	 vat.item.setAttributeByName("#F.endDeliveryDate", "readOnly", "" !=  vat.bean().vatBeanOther.endDeliveryDate);


}



function kweImDetail(){
	var shiftMode = "shift";
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
  	var vbSelectionType = "CHECK";
    var vnB_Detail = 2;
    if(vatPickerId != null && vatPickerId != ""){
    	vat.item.make(vnB_Detail, "checkbox"                  , {type:"XBOX"});
    	vbSelectionType = "CHECK";
    }else{
    	vbSelectionType = "NONE";
    }
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  , view:"fixed",                     desc:"序號"       });
	vat.item.make(vnB_Detail, "orderTypeCode"             , {type:"TEXT" , view:"fixed", size: 3, maxLen:20, mode:"READONLY", desc:"單別"   });
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" , view:"fixed", size:11, maxLen:20, mode:"READONLY", desc:"單號"   });
	vat.item.make(vnB_Detail, "deliveryDate"              , {type:"DATE" , view:"fixed", size: 8, maxLen:20, mode:"READONLY", desc:"轉出日期"});
	vat.item.make(vnB_Detail, "deliveryWarehouseCode"     , {type:"TEXT" , view:"fixed", size: 4, maxLen:12, mode:"READONLY", desc:"轉出庫別"});
	vat.item.make(vnB_Detail, "arrivalDate"               , {type:"DATE" , view:"fixed", size:12, maxLen:12, mode:"READONLY", desc:"轉入日期"});
	vat.item.make(vnB_Detail, "arrivalWarehouseCode"      , {type:"TEXT" , view:"fixed", size: 4, maxLen:12, mode:"READONLY", desc:"轉入庫別"});
	vat.item.make(vnB_Detail, "originalOrderTypeCode"     , {type:"TEXT" , view:"", size: 5, maxLen:12, mode:"READONLY", desc:"來源單別"});
	vat.item.make(vnB_Detail, "orignialOrderNo"           , {type:"TEXT" , view:"", size:13, maxLen:20, mode:"READONLY", desc:"來源單號"});
	vat.item.make(vnB_Detail, "itemCount"                 , {type:"NUMM" , view:"", size: 8, maxLen:20, mode:"READONLY", dec:0, desc:"件數", bind:"itemCount"});
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , view:"fixed", size: 8, maxLen:12, mode:"READONLY", desc:"狀態"   });
	vat.item.make(vnB_Detail, "remark1"       		  	  , {type:"TEXT" , view:shiftMode, size: 10, maxLen: 12, mode:"READONLY", desc:"備註"});
	vat.item.make(vnB_Detail, "headId"                    , {type:"ROWID", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "modifyDeliveryDate"        , {type:"BUTTON", view: "fixed", desc:"修改日期", value:"修改日期", src:"images/button_advance_input.gif", eClick:"modifyDeliveryDate()"});

	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
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
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "2",
													    indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});

   if("" ==  vat.bean().vatBeanOther.status){
		 vat.item.setGridStyleByName("modifyDeliveryDate"  , "display", "inline");
   }else{
	 	 vat.item.setGridStyleByName("modifyDeliveryDate"  , "display", "none");
   }
}


function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");
    //vat.item.setValueByName("#F.orderTypeCode","IMV");
	//vat.item.setValueByName("#F.deliveryWarehouseCode","T1BS99");
	//vat.item.setValueByName("#F.arrivalWarehouseCode" ,"T1BS08");
	//vat.item.setValueByName("#F.status"               ,"CLOSE");
	var vCmMovementIsNotNull = ("" ==  vat.bean().vatBeanOther.status?"N":"Y");
	var processString = "process_object_name=imMovementMainService&process_object_method_name=getAJAXSearchPageData" +
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +
	                  "&orderTypeCode"          + "=" + vat.item.getValueByName("#F.orderTypeCode"        ) +
	                  "&startOrderNo"           + "=" + vat.item.getValueByName("#F.startOrderNo"         ) +
	                  "&endOrderNo"             + "=" + vat.item.getValueByName("#F.endOrderNo"           ) +
	                  "&deliveryWarehouseCode"  + "=" + vat.item.getValueByName("#F.deliveryWarehouseCode") +
	                  "&startDeliveryDate"      + "=" + vat.item.getValueByName("#F.startDeliveryDate"    ) +
					  "&endDeliveryDate"        + "=" + vat.item.getValueByName("#F.endDeliveryDate"      ) +
	                  "&arrivalWarehouseCode"   + "=" + vat.item.getValueByName("#F.arrivalWarehouseCode" ) +
                      "&startArrivalDate"       + "=" + vat.item.getValueByName("#F.startArrivalDate"     ) +
                      "&endArrivalDate"         + "=" + vat.item.getValueByName("#F.endArrivalDate"       ) +
                      "&status"                 + "=" + vat.item.getValueByName("#F.status"               ) +
                      "&originalOrderTypeCode"  + "=" + vat.item.getValueByName("#F.originalOrderTypeCode") +
                      "&originalStartOrderNo"   + "=" + vat.item.getValueByName("#F.originalStartOrderNo" ) +
                      "&originalEndOrderNo"     + "=" + vat.item.getValueByName("#F.originalEndOrderNo"   ) +
                      "&itemCode"               + "=" + vat.item.getValueByName("#F.itemCode"             ) +
                      "&packedBy"               + "=" + vat.item.getValueByName("#F.packedBy"             ) +
                      "&comfirmedBy"            + "=" + vat.item.getValueByName("#F.comfirmedBy"          ) +
                      "&receiptedBy"            + "=" + vat.item.getValueByName("#F.receiptedBy"          ) +
                      "&cmMovement"             + "=" + vat.item.getValueByName("#F.cmMovement"           ) +
                      "&originalStartOrderNo"   + "=" + vat.item.getValueByName("#F.originalStartOrderNo" ) +
                      "&originalEndOrderNo"     + "=" + vat.item.getValueByName("#F.originalEndOrderNo"   ) +
                      "&itemCode"               + "=" + vat.item.getValueByName("#F.itemCode"             ) +
                      "&cmMovementIsNotNull"    + "=" + vCmMovementIsNotNull +
					  "&description" 			+ "=" + vat.item.getValueByName("#F.description"          );

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
    //alert("sss");
	var processString = "";
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=imMovementMainService&process_object_method_name=saveSearchResult";
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



function doSearch(){
    //alert("searchService");
   // vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
   // alert("timeScope:"+vat.bean().timeScope);

    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope },
			                    {other: true,
			                     funcSuccess:
			                        function() {
			                                    vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
			                                   }
			                    });

}

function pageLoadSuccess(){
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


function doClosePicker(){
    //alert("doClosePicker");
	//vat.bean().vatBeanPicker.xxx = 1;

	   vat.block.pageSearch(2, {
    		funcSuccess :
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imMovementMainService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}});
}

function doView(){
	//alert("doView");
	   vat.block.pageSearch(2, {
    		funcSuccess :
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imMovementMainService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}});
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();

}

function selectAll(){
  var processString = "";
  var isProcessing = false;
  //alert(vat.bean().vatBeanOther.isAllClick);
  if("N" == vat.bean().vatBeanOther.isAllClick ||
     typeof vat.bean().vatBeanOther.isAllClick == "undefined"){
  	if(confirm("本功能僅協助勾選前100筆之查詢結果，是否執行?"))
  		isProcessing = true;
  }else{
  	isProcessing = true;
  }
  if(isProcessing)
  	processString = "process_object_name=imMovementMainService&process_object_method_name=updateAllSearchData";
  return processString;

}

function doClear(){
	vat.item.setValueByName("#F.startOrderNo"         ,"");
	vat.item.setValueByName("#F.endOrderNo"           ,"");
	vat.item.setValueByName("#F.deliveryWarehouseCode","");
	vat.item.setValueByName("#F.startDeliveryDate"    ,"");
	vat.item.setValueByName("#F.endDeliveryDate"      ,"");
	vat.item.setValueByName("#F.arrivalWarehouseCode" ,"");
	vat.item.setValueByName("#F.startArrivalDate"     ,"");
	vat.item.setValueByName("#F.endArrivalDate"       ,"");
	vat.item.setValueByName("#F.originalOrderTypeCode","");
	vat.item.setValueByName("#F.startOriginalOrderNo" ,"");
	vat.item.setValueByName("#F.itemCode"             ,"");
}

function modifyDeliveryDate(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
	var returnData = window.showModalDialog(
		"Im_Movement:modifyDeliveryDate:20100116.page"+
		"?formId=" + vFormId ,"",
		"dialogHeight:300px;dialogWidth:400px;dialogTop:100px;dialogLeft:100px;");
}

function getEmployeeInfo(vsEmployee){
	if("" !=  vsEmployee,vat.item.getValueByName("#F."+vsEmployee)){
	    vat.item.setValueByName("#F."+ vsEmployee,vat.item.getValueByName("#F."+vsEmployee).toUpperCase());
		var processString = "process_sql_code=FindEmployeeChineseName&employeeCode="+vat.item.getValueByName("#F."+vsEmployee) ;
		vat.ajax.startRequest(processString,  function() {
		  if (vat.ajax.handleState()){
		    if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
		      vat.item.setValueByName("#F."+ vsEmployee +"Name", vat.ajax.getValue("CHINESE_NAME", vat.ajax.xmlHttp.responseText));
		    }else{
		      vat.item.setValueByName("#F."+ vsEmployee +"Name", "");
		      alert("查無此員工代號");
		    }
		  }
		} );
	}
}



function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(null != vFormId && "" != vFormId && 0 != vFormId){
    	var url = "/erp/Im_Movement:create:20090703.1.page?formId=" + vFormId;
		sc=window.open(url, '調撥單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.resizeTo((screen.availWidth),(screen.availHeight));
		sc.moveTo(0,0);
	}
}

function changeOrderType(){
	vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode") ;
    vat.block.submit(
    	function(){
    		return "process_object_name=tmpAjaxSearchDataService"+
			       "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope },
			       {other: true,
			       	funcSuccess:
			       	function() {
					vat.block.submit(
						function(){
							return "process_object_name=imMovementMainService&process_object_method_name=getAJAXWarehouseInfoByOrderType";
				     	},{other : true,
				     	   funcSuccess:function(){
				     	   		vat.item.SelectBind(vat.bean("allDeliveryWarehouses"),{ itemName : "#F.deliveryWarehouseCode" });
				 				vat.item.SelectBind(vat.bean("allArrivalWarehouses") ,{ itemName : "#F.arrivalWarehouseCode" });
				     		    vat.block.pageRefresh(2);
				     }});

			       }

	 });


}