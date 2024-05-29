var vnB_Header = 1;
var vnB_Button = 2;
var vnB_Detail = 3;

function kweImBlock(){
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();
  
//  doFormAccessControl();
}

function searchInitial(){ 
	initialVatBeanCode();
	vat.bean.init(
		function(){
			return "process_object_name=ommChannelAction&process_object_method_name=executeSearchInitial";
		},{
			other: true
	});
  
}

function initialVatBeanCode(){
	vat.bean().vatBeanOther = {
		loginBrandCode  	: "T2",
        loginEmployeeCode  	: "T96085",
        sysSno             	: "",
		vatPickerId			: ""
	};
}

function headerInitial(){
	var channelType = vat.bean("channelType");
	var categoryType = vat.bean("categoryType");
//	var channelType =   [["", true] , ["ERP", "POS", "EC", "APP"] , ["ERP", "POS", "EC", "APP"]]; 
//	var categoryType =  [["", true] , ["菸酒巧克力", "精品", "化妝品", "台產品", "3C影音圖書"] , ["菸酒巧克力", "精品", "化妝品", "台產品", "3C影音圖書"]];
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"通道查詢作業", 
		rows:[  
		 		{row_style:"", cols:[
					{items:[{name:"#L.channelType", 			type:"LABEL",       value:"通道類別"}]},
					{items:[{name:"#F.channelType", 			type:"SELECT",      bind:"channelType",  init:channelType,  size:20}]},
				 	{items:[{name:"#L.categoryType", 			type:"LABEL",       value:"業種類別"}]},
					{items:[{name:"#F.categoryType", 			type:"SELECT",      bind:"categoryType", init:categoryType, size:10}]},						
					{items:[{name:"#L.enable", 					type:"LABEL", 	    value:"是否啟用" }]},
					{items:[{name:"#F.enable", 					type:"CHECKBOX", 	bind:"enable",       init:"Y"  }]}
				]}
		], 	 
		beginService:"",
		closeService:""			
	});
}

function buttonLine(){
	var vsViewFunction = vat.bean().vatBeanOther.vatPickerId == "" ? "": "doClosePicker()";

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
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
//	if("" == vat.bean().vatBeanOther.vatPickerId){ 
//		vat.item.setStyleByName("#B.view" , "display", "none");
//	}	
}

function detailInitial(){

	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;
  	
  	var vatPickerId = vat.bean().vatBeanOther.vatPickerId;
    var vbSelectionType = vatPickerId== null || vatPickerId== "" ?"NONE":"CHECK";   
    
    vat.item.make(vnB_Detail, "indexNO"     	, {type:"IDX"  					, mode:"READONLY" 	, desc:"序號"		 });
	vat.item.make(vnB_Detail, "channelType"     , {type:"TEXT" 		, size:20	, mode:"READONLY"	, desc:"通道類別"      });
	vat.item.make(vnB_Detail, "categoryTypeName", {type:"TEXT"					, mode:"READONLY"  	, desc:"業種類別名稱"   });
	vat.item.make(vnB_Detail, "categoryType"    , {type:"TEXT" 	    , size:20	, mode:"HIDDEN"		, desc:"業種類別"      });
	vat.item.make(vnB_Detail, "enable"          , {type:"CHECKBOX"  , size:10	, mode:"READONLY"	, desc:"是否啟用"      });
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["channelType","categoryType"],
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														indicate			: function(){openModifyPage()}
														});

}

function doSearch(){
	alert("doSearch");
	vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
}

function closeWindows(closeType){
	alert("closeWindows");
  	var isExit = true ;
  	if("CONFIRM" == closeType){
		isExit = confirm("是否確認離開?");
  	}
  	if(isExit)
    	window.top.close();  	
}

// 清除
function resetForm(){
	alert("resetForm");
	vat.item.setValueByName("#F.channelType", "");
	vat.item.setValueByName("#F.categoryType", "");
	vat.item.setValueByName("#F.enable", "Y");
}

// 載入成功後
function loadSuccessAfter(){
	alert("loadSuccessAfter");
}

// 查詢點下執行
function loadBeforeAjxService(){
	alert("loadBeforeAjxService");
	
	var processString = "process_object_name=ommChannelService&process_object_method_name=getAJAXSearchPageData" + 
						"&channelType" 		+ "=" + vat.item.getValueByName("#F.channelType") + 
						"&categoryType" 	+ "=" + vat.item.getValueByName("#F.categoryType") + 
						"&enable" 			+ "=" + vat.item.getValueByName("#F.enable");
	return processString;
}

function saveSuccessAfter(){
	alert("saveSuccessAfter");
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService(){
	alert("saveBeforeAjxService");
	var processString = "";
	return processString;
}

// 雙擊明細後，開啟一張修改的異動單
function openModifyPage(){
	alert("openModifyPage");
	var nItemLine = vat.item.getGridLine();
	var vChannelType = vat.item.getGridValueByName("channelType", nItemLine);
	var vCategoryType = vat.item.getGridValueByName("categoryType", nItemLine);
	var vJsName = "ommChannel";
	if(null != vChannelType && null != vCategoryType){
		
//		vCategoryType = transformCategoryType(vCategoryType);
		
		var url = "/erp_dev20201030/tm.jsp?" + 
				  "vChannelType"   + "=" + vChannelType  +
				  "&vCategoryType" + "=" + vCategoryType +
				  "&vJsName"	   + "=" + vJsName;
		
		sc=window.open(url, '調撥單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.resizeTo((screen.availWidth),(screen.availHeight));
		sc.moveTo(0,0);
	}
}

// 轉換業種代碼
//function transformCategoryType(categoryTypeName){
//	let categoryTypeCode = "";
//	let map = new Map();
//	map.set("巧克力菸酒", "T");
//	map.set("精品", "E");
//	map.set("化妝品", "C");
//	map.set("台產品", "F");
//	map.set("3C影音圖書", "D");
//	
//	if(map.has(categoryTypeName)){
//		categoryTypeCode = map.get(categoryTypeName);
//	}
//}
