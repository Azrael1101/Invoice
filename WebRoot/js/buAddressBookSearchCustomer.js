
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
			return "process_object_name=buAddressBookAction&process_object_method_name=performSearchCustomerInitial"; 
		},{other: true});
	}
}

// 可搜尋的欄位
function headerInitial(){ 

var allType = [["","",true],["自然人","法人"],["1","2"]];
var allCustomerType = vat.bean("allCustomerType");
var allMonth = vat.bean("allMonth");
var allDay = vat.bean("allDay");
var allVIPType = vat.bean("allVIPType");
var allGender = [["","",true],["男","女"],["M","F"]];	

vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"客戶資料查詢作業", rows:[  
	 		{row_style:"", cols:[
				{items:[{name:"#L.type", 				type:"LABEL", 	value:"類別"}]},
				{items:[{name:"#F.type", 				type:"SELECT", 	size:1, init:allType}]},
			 	{items:[{name:"#L.identityCode", 		type:"LABEL", 	value:"身份證明代號"}]},
				{items:[{name:"#F.identityCode", 		type:"TEXT", 	size:20, maxLen:30 }]},
				{items:[{name:"#L.chineseName", 		type:"LABEL", 	value:"姓名" }]},
				{items:[{name:"#F.chineseName", 		type:"TEXT",  	size:15 }]}	
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.birthday", 		type:"LABEL", 	value:"生日" }]},
				{items:[{name:"#F.birthdayYear", 		type:"TEXT",	size:2},
						{name:"#L.birthdayYear", 		type:"LABEL", 	value:"年" },
						{name:"#F.birthdayMonth", 		type:"SELECT",	size:1, init:allMonth},
						{name:"#L.birthdayMonth", 		type:"LABEL", 	value:"月" },
						{name:"#F.birthdayDay", 		type:"SELECT",	size:1, init:allDay},
						{name:"#L.birthdayDay", 		type:"LABEL", 	value:"日" }]},
				{items:[{name:"#L.gender", 				type:"LABEL", 	value:"性別" }]},
				{items:[{name:"#F.gender", 				type:"SELECT",  init:allGender }]},
				{items:[{name:"#L.englishName", 		type:"LABEL", 	value:"英文名稱" }]},
				{items:[{name:"#F.englishName", 		type:"TEXT",  	size:15}]}	  		
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customerTypeCode", 	type:"LABEL", 	value:"客戶類別" }]},
				{items:[{name:"#F.customerTypeCode", 	type:"SELECT",	size:1, init:allCustomerType}]},
				{items:[{name:"#L.customerCode", 		type:"LABEL", 	value:"客戶代號" }]},
				{items:[{name:"#F.customerCode", 		type:"TEXT",  	size:15 }]},
				{items:[{name:"#L.vipTypeCode", 		type:"LABEL", 	value:"VIP類別" }]},
				{items:[{name:"#F.vipTypeCode", 		type:"SELECT",  size:1, init:allVIPType }]}	  		
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.vipStartDate", 		type:"LABEL", 	value:"會員啟始日" }]},
				{items:[{name:"#F.vipStartDate", 		type:"DATE"}]},
				{items:[{name:"#L.vipEndDate", 			type:"LABEL", 	value:"會員到期日" }]},
				{items:[{name:"#F.vipEndDate", 			type:"DATE"}]},
				{items:[{name:"#L.applicationDate", 	type:"LABEL", 	value:"申請日期" }]},
				{items:[{name:"#F.applicationDate", 	type:"DATE"}]} 		
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
    vat.item.make(vnB_Detail, "indexNo"         		, {type:"IDX"  , desc:"序號"});
	vat.item.make(vnB_Detail, "type"            		, {type:"TEXT" , size:15, mode:"HIDDEN", desc:"類別"      });
	vat.item.make(vnB_Detail, "typeName"            	, {type:"TEXT" , size:4, mode:"READONLY", desc:"類別"      });
	vat.item.make(vnB_Detail, "identityCode"    		, {type:"TEXT" , size:10, mode:"READONLY", desc:"統一編號"      });
	vat.item.make(vnB_Detail, "chineseName"     		, {type:"TEXT" , size:10, mode:"READONLY", desc:"姓名"      });
	vat.item.make(vnB_Detail, "customerCode"			, {type:"TEXT" , size:18, mode:"READONLY", desc:"客戶代號"      });
	vat.item.make(vnB_Detail, "vipTypeCode"     		, {type:"TEXT" , size:18, mode:"READONLY", desc:"客戶類型"      });
	vat.item.make(vnB_Detail, "vipTypeName"     		, {type:"TEXT" , size:8, mode:"READONLY", desc:"客戶類型"      });
	vat.item.make(vnB_Detail, "vipStartDate"     		, {type:"TEXT" , size:12, mode:"READONLY", desc:"會員啟始日"      });
	vat.item.make(vnB_Detail, "vipEndDate"     			, {type:"TEXT" , size:12, mode:"READONLY", desc:"會員到期日"      });
	vat.item.make(vnB_Detail, "lastUpdateDate"  		, {type:"TEXT" , size:10, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail, "addressBookId"      		, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["customerCode"],
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
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
	var processString = "process_object_name=buAddressBookService&process_object_method_name=getAJAXSearchCustomerPageData" + 
						"&loginBrandCode=" 	+ vat.bean().vatBeanOther.loginBrandCode +     
		                "&type=" 			+ vat.item.getValueByName("#F.type"        ) +     
		                "&identityCode=" 	+ vat.item.getValueByName("#F.identityCode"         ) +     
		                "&chineseName=" 	+ vat.item.getValueByName("#F.chineseName"           ) + 
		                "&englishName=" 	+ vat.item.getValueByName("#F.englishName")+
		                "&gender=" 			+ vat.item.getValueByName("#F.gender"    ) +     
						"&birthdayYear=" 	+ vat.item.getValueByName("#F.birthdayYear"      ) + 
						"&birthdayMonth=" 	+ vat.item.getValueByName("#F.birthdayMonth"      ) +
						"&birthdayDay=" 	+ vat.item.getValueByName("#F.birthdayDay"      ) +
						"&customerTypeCode="+ vat.item.getValueByName("#F.customerTypeCode"      ) +
						"&customerCode=" 	+ vat.item.getValueByName("#F.customerCode"      ) +
						"&vipTypeCode=" 	+ vat.item.getValueByName("#F.vipTypeCode"      ) +
						"&vipStartDate=" 	+ vat.item.getValueByName("#F.vipStartDate"      ) +
						"&vipEndDate=" 		+ vat.item.getValueByName("#F.vipEndDate"      ) +
						"&applicationDate=" + vat.item.getValueByName("#F.applicationDate"      ) ;        
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
		processString = "process_object_name=buAddressBookService&process_object_method_name=saveSearchCustomerResult";
	}
	return processString;
}								

// 檢視按下後的動作
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=buAddressBookAction&process_object_method_name=performSearchCustomerSelection";
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
	vat.item.setValueByName("#F.type", "");
    vat.item.setValueByName("#F.identityCode", "");
    vat.item.setValueByName("#F.chineseName", "");
    vat.item.setValueByName("#F.supplierTypeCode", "");
    vat.item.setValueByName("#F.supplierCode", "");
    vat.item.setValueByName("#F.categoryCode", "");
    vat.item.setValueByName("#F.customsBroker", "");
    vat.item.setValueByName("#F.agent", "");
    vat.item.setValueByName("#F.commissionRateStart", "");
    vat.item.setValueByName("#F.commissionRateEnd", "");
}