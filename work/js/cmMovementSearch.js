
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
	vat.bean.init(
  		function(){
			return "process_object_name=cmMovementAction&process_object_method_name=performSearchInitial";
    	},{
    		other: true
   	});
  }
}

// 可搜尋的欄位
function kweHeader(){
	var allStatus = [["","","true"],
                 ["暫存","駁回", "簽核中", "簽核完成","作廢"],
                 ["SAVE","REJECT", "SIGNING", "FINISH","VOID"]];

	var allOrderTypeCodes = vat.bean("allOrderTypeCodes");

	var vatPickerId = vat.bean().vatBeanOther.vatPickerId;
	var orderTypeCodeMode = "READONLY";
	if(vatPickerId != null && vatPickerId != ""){
		orderTypeCodeMode = "READONLY";
	}else{
		orderTypeCodeMode = "";
	}

vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"移倉單查詢作業", rows:[
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.orderTypeCode", 		type:"LABEL"  , value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 		type:"SELECT", bind:"orderTypeCode", mode:orderTypeCodeMode, init:allOrderTypeCodes}]},
				{items:[{name:"#L.orderNo", 		type:"LABEL"  , value:"移倉單號"}]},
				{items:[{name:"#F.orderNo", 		type:"TEXT"   , size:20}]},
			 	{items:[{name:"#L.deliveryDate", 	type:"LABEL"  , value:"移倉日期"}]},
				{items:[{name:"#F.deliveryDateStart", 	type:"DATE"   , size:10},
						{name:"#L.between", 		type:"LABEL"  , value:" 至 "},
						{name:"#F.deliveryDateEnd", 	type:"DATE"   , size:10}]},
				{items:[{name:"#L.status", 			type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 			type:"SELECT", 	bind:"status", init:allStatus, size:10 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.passNo", 			type:"LABEL", 	value:"放行單號" }]},
				{items:[{name:"#F.passNo", 			type:"TEXT", 	bind:"passNo", size:20 }]},
				{items:[{name:"#L.customsArea", 	type:"LABEL", 	value:"關別" }]},
				{items:[{name:"#F.customsArea", 	type:"TEXT", 	bind:"customsArea", size:10 }]},
				{items:[{name:"#L.sealNo", 			type:"LABEL", 	value:"封條號碼" }]},
				{items:[{name:"#F.sealNo", 			type:"TEXT", 	bind:"sealNo", size:10 }]},
				{items:[{name:"#L.transferOrderNo", type:"LABEL", 	value:"運送單號" }]},
				{items:[{name:"#F.transferOrderNo", type:"TEXT", 	bind:"transferOrderNo", size:20 }]}
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
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
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

    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "orderNo"         , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"移倉單號"      });
	vat.item.make(vnB_Detail, "deliveryDate"    , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"移倉日期"      });
	vat.item.make(vnB_Detail, "passNo"     		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"放行單號"      });
	vat.item.make(vnB_Detail, "customsArea"     , {type:"TEXT" , size:10, maxLen:10, mode:"READONLY", desc:"關別"      });
	vat.item.make(vnB_Detail, "transferOrderNo" , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"運送單號"      });
	vat.item.make(vnB_Detail, "statusName"     	, {type:"TEXT" , size:12, maxLen:20, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "customsStatusName"   , {type:"TEXT" , size:26, maxLen:20, mode:"READONLY", desc:"海關狀態"      });
	vat.item.make(vnB_Detail, "moveWhNo"     	, {type:"TEXT" , size:22, maxLen:22, mode:"READONLY", desc:"移倉申請書號碼"      });
	vat.item.make(vnB_Detail, "lastUpdateDate"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail, "orderTypeCode"  , {type:"TEXT" , mode:"HIDDEN", desc:"單別"   });
	vat.item.make(vnB_Detail, "headId"      	, {type:"ROWID"});

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
function loadBeforeAjxService(){
   // alert("loadBeforeAjxService");

	var processString = "process_object_name=cmMovementService&process_object_method_name=getAJAXSearchPageData" +
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +
                      "&orderTypeCode"         + "=" + vat.item.getValueByName("#F.orderTypeCode" ) +
	                  "&orderNo"          		+ "=" + vat.item.getValueByName("#F.orderNo"     ) +
	                  "&deliveryDateStart"      + "=" + vat.item.getValueByName("#F.deliveryDateStart"       ) +
	                  "&deliveryDateEnd"      	+ "=" + vat.item.getValueByName("#F.deliveryDateEnd"       ) +
	                  "&passNo"          		+ "=" + vat.item.getValueByName("#F.passNo"   ) +
	                  "&customsArea"          	+ "=" + vat.item.getValueByName("#F.customsArea"     ) +
	                  "&status"          		+ "=" + vat.item.getValueByName("#F.status"        ) +
	                  "&sealNo"          		+ "=" + vat.item.getValueByName("#F.sealNo"  )+
	                  "&transferOrderNo"        + "=" + vat.item.getValueByName("#F.transferOrderNo"  );

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
		processString = "process_object_name=cmMovementService&process_object_method_name=saveSearchResult";
	}
	return processString;
}

// 檢視按下後的動作
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess :
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=cmMovementService&process_object_method_name=getSearchSelection";
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

// 清除
function resetForm(){
	vat.item.setValueByName("#F.orderNo", "");
    vat.item.setValueByName("#F.deliveryDateStart", "");
    vat.item.setValueByName("#F.deliveryDateEnd", "");
    vat.item.setValueByName("#F.passNo", "");
    vat.item.setValueByName("#F.customsArea", "");
    vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.sealNo", "");
}

// 開啟視窗
function openModifyPage(){

    var nItemLine = vat.item.getGridLine();

    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
	var orderTypeCode = vat.item.getGridValueByName("orderTypeCode", nItemLine);

	if(!(vFormId == "" || vFormId == "0")){
    var url = "/erp/Cm_Movement:create:20091001.page?formId=" + vFormId + "&orderTypeCode="+orderTypeCode;

     sc=window.open(url, '移倉單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
    }
}