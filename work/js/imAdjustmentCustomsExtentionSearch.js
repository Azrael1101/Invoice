
var afterSavePageProcess = "";
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){
 	
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();

}

// 搜尋初始化
function searchInitial(){ 
	//alert("searchInitial");	
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,	
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     	].value,
  	     vatPickerId        : document.forms[0]["#vatPickerId"       	].value,
  	     isSpecial          : document.forms[0]["#isSpecial"            ].value
	    };
	  	
		vat.bean.init(	
	  		function(){
				return "process_object_name=imAdjustmentHeadAction&process_object_method_name=performExtentionSearchInitial"; 
	    	},{								
	    		other: true
    	}); 
  }
}

// 可搜尋的欄位
function headerInitial(){
	var allStatus =  [["","","true"],						 
                 ["暫存", "簽核完成","結案","作廢"],
                 ["SAVE","FINISH","FINISH","VOID"]];
	
	//var vatPickerId = vat.bean().vatBeanOther.vatPickerId;
	var isSpecial = vat.bean("isSpecial");
	
	vat.block.create( vnB_Header , {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"報單展延查詢作業", rows:[  
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.orderTypeCode", 		type:"LABEL"  , value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 		type:"TEXT"   , bind:"orderTypeCode" , mode:"READONLY"},
				        {name:"#L.isSpecial", 		    type:"LABEL"   , value:isSpecial=="Y"?"特殊展延單":"一般展延單"},
				        {name:"#F.isSpecial", 		    type:"TEXT"   , bind:"isSpecial" , mode:"HIDDEN"}]},
				{items:[{name:"#L.orderNo", 			type:"LABEL"  , value:"單號"}]},
				{items:[{name:"#F.orderNo", 			type:"TEXT"   , size:20}]},
				{items:[{name:"#L.status", 				type:"LABEL"  , value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"SELECT" , bind:"status", init:allStatus, size:10 }]}	
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.fileNo", 			type:"LABEL"  ,	value:"文號" }]},
				{items:[{name:"#F.fileNo", 			type:"TEXT"	  , size:20  }]},
				{items:[{name:"#L.creationDate",	type:"LABEL"  , value:"建檔日期"}]},
				{items:[{name:"#F.createDateStart",	type:"DATE"   , size:10},
						{name:"#L.between", 		type:"LABEL"  , value:" 至 "},
						{name:"#F.createDateEnd", 	type:"DATE"   , size:10}], td:" colSpan=3"}
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function buttonLine(){
	
	//vsViewFunction = vat.bean().vatBeanOther.vatPickerId==""?"doView()":"doClosePicker()";
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
	 			{name:"#B.picker",      type:"IMG",      value:"檢視", src:"./images/button_view.gif",  eClick:"doClosePicker()"}],
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
   /* if(vatPickerId != null && vatPickerId != ""){
 		vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
		vbSelectionType = "CHECK";    
    }else{
		vat.item.setStyleByName("#B.view" , "display", "none");
		vbSelectionType = "NONE";
    }
*/	
	vat.item.make(vnB_Detail, "checked"       	, {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"  , desc:"序號"      });
    vat.item.make(vnB_Detail, "orderNo"         , {type:"TEXT" , size:12, maxLen:16, mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "fileNo"    		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"文號"      });
	vat.item.make(vnB_Detail, "statusName"     	, {type:"TEXT" , size:4, maxLen:6, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "creationDate"  , {type:"TEXT" , size:10, maxLen:14, mode:"READONLY", desc:"建檔日期"   });
	vat.item.make(vnB_Detail, "headId"      	, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId","orderNo"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});

}

// 載入成功後
function loadSuccessAfter(){
//alert("loadSuccessAfter");

	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
   
	var processString = "process_object_name=imAdjustmentHeadService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
                      "&orderTypeCode"          + "=" + vat.item.getValueByName("#F.orderTypeCode"     ) +     
	                  "&orderNo"          		+ "=" + vat.item.getValueByName("#F.orderNo"     ) +
	                  "&createDateStart"	    + "=" + vat.item.getValueByName("#F.createDateStart"       ) +
	                  "&createDateEnd"    		+ "=" + vat.item.getValueByName("#F.createDateEnd"       ) +
	                  "&status"          		+ "=" + vat.item.getValueByName("#F.status"        ) +
	                  "&fileNo"         	    + "=" + vat.item.getValueByName("#F.fileNo"  ) +
	                  "&isSpecial"         	    + "=" + vat.item.getValueByName("#F.isSpecial"  );     
                                                                            
	return processString;											
}

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
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
		processString = "process_object_name=imAdjustmentHeadService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
//alert("doClosePicker");

var nItemLine = vat.item.getGridLine();
var vFormId = vat.item.getGridValueByName("headId", nItemLine);
//alert("vFormId=" + vFormId);
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imAdjustmentHeadService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}


function doView(){
	//alert("doView");
	   vat.block.pageSearch(2, {
    		funcSuccess :
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imAdjustmentHeadService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
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
	vat.item.setValueByName("#F.createDateStart", "");
	vat.item.setValueByName("#F.createDateEnd", "");
	vat.item.setValueByName("#F.status", "");
	vat.item.setValueByName("#F.fileNo", "");
}

/*
function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(!(vFormId == "" || vFormId == "0")){
	    var url = "/erp/Po_PurchaseOrder:create:20091226.page?formId=" + vFormId+"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value; 
	     sc=window.open(url, '報單展延單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	     sc.resizeTo((screen.availWidth),(screen.availHeight));
	     sc.moveTo(0,0);
	}
	
}
*/