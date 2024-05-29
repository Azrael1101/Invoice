
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
		vat.bean().vatBeanOther = {
  			loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     	loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	
  	     	vatPickerId        : document.forms[0]["#vatPickerId"       ].value  
	    };
		vat.bean.init(function(){
			return "process_object_name=buAddressBookAction&process_object_method_name=performSearchSupplierInitial"; 
		},{other: true});
	}
}

// 可搜尋的欄位
function headerInitial(){ 

var allType = vat.bean("allType");
var allSupplierType = vat.bean("allSupplierType");
var allSupplierClass = vat.bean("allSupplierClass");
var allOrderBy = [["","supplierCode","true"],["廠商代號","更新日期"],["supplierCode","lastUpdateDate"]];	
	
vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"供應商資料查詢作業", rows:[  
	 		{row_style:"", cols:[
				{items:[{name:"#L.type", 				type:"LABEL", 	value:"類別"}]},
				{items:[{name:"#F.type", 				type:"SELECT", 	size:1, init:allType}]},
			 	{items:[{name:"#L.identityCode", 		type:"LABEL", 	value:"統一編號"}]},
				{items:[{name:"#F.identityCode", 		type:"TEXT", 	size:20, maxLen:30 }]},
				{items:[{name:"#L.chineseName", 		type:"LABEL", 	value:"姓名" }]},
				{items:[{name:"#F.chineseName", 		type:"TEXT",  	size:20 }]},
				{items:[{name:"#L.brandName", 			type:"LABEL", 	value:"品牌" }]},
				{items:[{name:"#F.brandName", 			type:"TEXT",  	bind:"brandName", size:20, mode:"READONLY" }]}		
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.supplierTypeCode", 	type:"LABEL", 	value:"廠商類別" }]},
				{items:[{name:"#F.supplierTypeCode", 	type:"SELECT",	size:1, init:allSupplierType}]},
				{items:[{name:"#L.supplierCode", 		type:"LABEL", 	value:"廠商代號" }]},
				{items:[{name:"#F.supplierCode", 		type:"TEXT",  	size:20 }]},
				{items:[{name:"#L.categoryCode", 		type:"LABEL", 	value:"廠商類型" }]},
				{items:[{name:"#F.categoryCode", 		type:"SELECT",  size:1, init:allSupplierClass }]},
				{items:[{name:"#L.orderBy", 			type:"LABEL", 	value:"排序" }]},
				{items:[{name:"#F.orderBy", 			type:"SELECT",  size:20, init:allOrderBy}]}		  		
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customsBroker", 		type:"LABEL", 	value:"報關行" }]},
				{items:[{name:"#F.customsBroker", 		type:"TEXT", 	size:20 }]},
				{items:[{name:"#L.agent", 				type:"LABEL", 	value:"代理商" }]},
				{items:[{name:"#F.agent", 				type:"TEXT",  	size:20 }]},
				{items:[{name:"#L.commissionRate", 		type:"LABEL", 	value:"佣金比率" }]},
				{items:[{name:"#F.commissionRateStart",	type:"NUMB",  	size:4 },
					{name:"#L.between",	type:"LABEL",  	value:"% 至" },
					{name:"#F.commissionRateEnd",		type:"NUMB",  	size:4 },
					{name:"#L.endBetween",	type:"LABEL",  	value:"%" }],td:"colspan='3'"}	  		
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
	 			{name:"#B.update"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
	 			
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

    vat.item.make(vnB_Detail, "checked"         		, {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"         		, {type:"IDX"  ,                     desc:"序號"      });
//	vat.item.make(vnB_Detail, "type"            		, {type:"TEXT" , size:15, mode:"HIDDEN", desc:"類別"      });
	vat.item.make(vnB_Detail, "typeName"            	, {type:"TEXT" , size:4, mode:"READONLY", desc:"類別"      });
	vat.item.make(vnB_Detail, "identityCode"    		, {type:"TEXT" , size:10, mode:"READONLY", desc:"統一編號"      });
	vat.item.make(vnB_Detail, "chineseName"     		, {type:"TEXT" , size:30, mode:"READONLY", desc:"姓名"      });
//	vat.item.make(vnB_Detail, "supplierTypeCode"		, {type:"TEXT" , size:18, mode:"READONLY", desc:"廠商類別"      });
	vat.item.make(vnB_Detail, "supplierTypeCodeName"	, {type:"TEXT" , size:8, mode:"READONLY", desc:"廠商類別"      });
	vat.item.make(vnB_Detail, "supplierCode"     		, {type:"TEXT" , size:8, mode:"READONLY", desc:"廠商代號"      });
//	vat.item.make(vnB_Detail, "categoryCode"     		, {type:"TEXT" , size:18, mode:"READONLY", desc:"廠商類型"      });
	vat.item.make(vnB_Detail, "categoryCodeName"     	, {type:"TEXT" , size:8, mode:"READONLY", desc:"廠商類型"      });
	vat.item.make(vnB_Detail, "customsBroker"     		, {type:"TEXT" , size:12, mode:"READONLY", desc:"報關行"      });
	vat.item.make(vnB_Detail, "lastUpdateDate"  		, {type:"TEXT" , size:10, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail, "addressBookId"      		, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["addressBookId"],
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
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.update" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.update" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
   // alert("loadBeforeAjxService");	
   
	var processString = "process_object_name=buAddressBookService&process_object_method_name=getAJAXSearchSupplierPageData" + 
                     "&loginBrandCode=" 		+ vat.bean().vatBeanOther.loginBrandCode +     
	                  "&type=" 					+ vat.item.getValueByName("#F.type"        ) +     
	                  "&identityCode=" 			+ vat.item.getValueByName("#F.identityCode"         ) +     
	                  "&chineseName=" 			+ vat.item.getValueByName("#F.chineseName"           ) + 
	                  "&supplierTypeCode=" 		+ vat.item.getValueByName("#F.supplierTypeCode")+
	                  "&supplierCode=" 			+ vat.item.getValueByName("#F.supplierCode"    ) +     
					  "&categoryCode=" 			+ vat.item.getValueByName("#F.categoryCode"      ) + 
					  "&customsBroker=" 		+ vat.item.getValueByName("#F.customsBroker"      ) +
					  "&agent=" 				+ vat.item.getValueByName("#F.agent"      ) +
					  "&commissionRateStart=" 	+ vat.item.getValueByName("#F.commissionRateStart"      ) +
					  "&commissionRateEnd=" 	+ vat.item.getValueByName("#F.commissionRateEnd"      )  +
					  "&orderBy="				+ vat.item.getValueByName("#F.orderBy"      );        
                                                                            
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
		processString = "process_object_name=buAddressBookService&process_object_method_name=saveSearchSupplierResult";
	}
	return processString;
}								

// 檢視按下後的動作
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=buAddressBookAction&process_object_method_name=performSearchSupplierSelection";
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
	vat.item.setValueByName("#F.type", "1");
    vat.item.setValueByName("#F.identityCode", "");
    vat.item.setValueByName("#F.chineseName", "");
    vat.item.setValueByName("#F.supplierTypeCode", "");
    vat.item.setValueByName("#F.supplierCode", "");
    vat.item.setValueByName("#F.categoryCode", "");
    vat.item.setValueByName("#F.customsBroker", "");
    vat.item.setValueByName("#F.agent", "");
    vat.item.setValueByName("#F.commissionRateStart", "");
    vat.item.setValueByName("#F.commissionRateEnd", "");
    vat.item.setValueByName("#F.orderBy", "supplierCode");
}