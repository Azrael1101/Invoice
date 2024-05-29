
var afterSavePageProcess = "";
var vnB_Detail = 2;
var vnB_Header = 1;
function outlineBlock(){
 	
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();

}

// 搜尋初始化
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,	
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     	].value,
  	     vatPickerId        : document.forms[0]["#vatPickerId"       	].value,
  	     taxType			: document.forms[0]["#taxType"     			].value,
  	     adjustmentType		: document.forms[0]["#adjustmentType"		].value
	    };
	    
		vat.bean.init(	
	  		function(){
				return "process_object_name=imGeneralAdjustmentAction&process_object_method_name=performSearchInitial"; 
	    	},{								
	    		other: true
    	}); 
  }
}

// 可搜尋的欄位
function headerInitial(){
	var taxType  = vat.bean().vatBeanOther.taxType;
	var allOrderTypeCodes = vat.bean("allOrderTypeCodes");
	var allTaxTypes = vat.bean("allTaxTypes");
	var allStatus; 
	if( taxType == "F" ){
		allStatus =  [["","","true"],						 
                 ["暫存", "簽核中", "簽核完成","結案","作廢"],
                 ["SAVE","SIGNING","FINISH","FINISH","VOID"]];
	}else{
		allStatus =  [["","","true"],						 
                 ["暫存", "簽核完成","結案","作廢"],
                 ["SAVE","FINISH","FINISH","VOID"]];
	}	                 
                 
	var allAdjustmentTypes = vat.bean("allAdjustmentTypes");
	
	var vatPickerId = vat.bean().vatBeanOther.vatPickerId;
	var orderTypeCodeMode = "READONLY";
	if(vatPickerId != null && vatPickerId != "" ){
		orderTypeCodeMode = "READONLY";
	}else{
		if( vat.bean().vatBeanOther.loginBrandCode.indexOf("T2") <= -1){
			orderTypeCodeMode = "READONLY";
		}else{
			orderTypeCodeMode = "";
		}
	}  
	
vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"調整單查詢作業", rows:[  
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.orderTypeCode", 		type:"LABEL"  , value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 		type:"SELECT" , mode:orderTypeCodeMode, init:allOrderTypeCodes , size:1}]},
				{items:[{name:"#L.orderNo", 			type:"LABEL"  , value:"調整單號"}]},
				{items:[{name:"#F.orderNo", 			type:"TEXT"   , size:20}]},
			 	{items:[{name:"#L.adjustmentDate", 		type:"LABEL"  , value:"調整日期"}]},
				{items:[{name:"#F.adjustmentDateStart",	type:"DATE"   , size:10},
						{name:"#L.between", 			type:"LABEL"  , value:" 至 "},
						{name:"#F.adjustmentDateEnd", 	type:"DATE"   , size:10}]},
				{items:[{name:"#L.status", 				type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"SELECT", 	bind:"status", init:allStatus, size:10 }]}	
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.taxType", 			type:"LABEL", 	value:"稅別" }]},
				{items:[{name:"#F.taxType", 			type:"SELECT",  init:allTaxTypes, mode:"READONLY" , size:10 }]},
				{items:[{name:"#L.adjustmentType", 		type:"LABEL", 	value:"調整類別" }]},
				{items:[{name:"#F.adjustmentType", 		type:"SELECT", 	init:allAdjustmentTypes, mode:"READONLY" , size:10 }]},
				{items:[{name:"#L.declarationNo", 		type:"LABEL", 	value:"報關單號" }]},
				{items:[{name:"#F.declarationNo", 		type:"TEXT",  	size:20 }], td:" colSpan=3"} 		
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

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
	 			{name:"#B.view"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
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
         vbSelectionType = "NONE";
    }

    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "orderNo"         , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"調整單號"      });
	vat.item.make(vnB_Detail, "adjustmentDate"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"調整日期"      });
	vat.item.make(vnB_Detail, "declarationNo"    , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"報關單號"      });
	vat.item.make(vnB_Detail, "statusName"     	, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "lastUpdateDate"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail, "orderTypeCode"   , {type:"TEXT", mode:"HIDDEN"});
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
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
   // alert("loadBeforeAjxService");	
   
	var processString = "process_object_name=imGeneralAdjustmentService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
                      "&orderTypeCode"          + "=" + vat.item.getValueByName("#F.orderTypeCode"     ) +     
	                  "&orderNo"          		+ "=" + vat.item.getValueByName("#F.orderNo"     ) +
	                  "&adjustmentDateStart"    + "=" + vat.item.getValueByName("#F.adjustmentDateStart"       ) +
	                  "&adjustmentDateEnd"    	+ "=" + vat.item.getValueByName("#F.adjustmentDateEnd"       ) +
//	                  "&sourceOrderNo"        	+ "=" + vat.item.getValueByName("#F.sourceOrderNo"   ) +
	                  "&adjustmentType"        	+ "=" + vat.item.getValueByName("#F.adjustmentType"     ) +
	                  "&status"          		+ "=" + vat.item.getValueByName("#F.status"        ) +
	                  "&declarationNo"         	+ "=" + vat.item.getValueByName("#F.declarationNo"  );     
                                                                            
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
		processString = "process_object_name=imGeneralAdjustmentService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imGeneralAdjustmentService&process_object_method_name=getSearchSelection";
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
	vat.item.setValueByName("#F.adjustmentDateStart", "");
	vat.item.setValueByName("#F.adjustmentDateEnd", "");
	vat.item.setValueByName("#F.status", "");
//	vat.item.setValueByName("#F.taxType", "");
	vat.item.setValueByName("#F.adjustmentType", "");
	vat.item.setValueByName("#F.declarationNo", "");
}

// 開啟視窗
function openModifyPage(){

    var nItemLine = vat.item.getGridLine();

    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode"     );

	var page = "/erp/Im_GeneralAdjustment:create:20091026.page";
	if( vat.bean().vatBeanOther.loginBrandCode.indexOf("T2") <= -1){
		page = "/erp/Im_GeneralAdjustment:create:20091026.page";
	}else{
		if(orderTypeCode == "MEG" || orderTypeCode == "MEF" || orderTypeCode == "MEP"){
			page = "/erp/Im_GeneralAdjustment:offShoreIslandcreate:20100413.page";
		}else if(orderTypeCode == "ACE") {
			page = "/erp/Im_GeneralAdjustment:create:20161006.page";
		}else{
			page = "/erp/Im_GeneralAdjustment:create:20091026.page";
		}		
	}

	if(!(vFormId == "" || vFormId == "0")){
    var url = page + "?formId=" + vFormId + "&orderTypeCode="+orderTypeCode; 
	
     sc=window.open(url, '調整單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
    } 
}