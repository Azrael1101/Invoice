vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Detail = 2;
var vnB_Detail1 = 3;

var sqlString = "clala";

function outlineBlock(){
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();
  	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");

		vat.tabm.createButton(0 ,"xTab1","明細檔資料","vatDetailDiv" ,"images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false);    
		vat.tabm.createButton(0 ,"xTab2","明細匯入" ,"vatDetailDiv1","","", false);

	}
	
  kweImDetail();
  kweImDetail1();

  vat.tabm.displayToggle(0, "xTab2", false, false, false);

  //doFormAccessControl();
}

function kweImSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value
  	    // userType 			: document.forms[0]["#userType"  ].value  
	    };
	    vat.bean.init(	
	  		function(){
				return "process_object_name=buCustomerModAction&process_object_method_name=searchPerformInitial"; 
	    	},{
	    		other: true
    	});
  }
}

function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-left-width: 0px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		//{name:"#B.export"	   , type:"IMG"      ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"matchExport"	   , type:"LABEL"    ,value:"　"},
	 			//{name:"#B.view"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.export2"	   , type:"IMG"      ,value:"明細匯入",   src:"./images/button_detail_import.gif", eClick:'importFormData()'},
	 			{name:"matchExport2"	   , type:"LABEL"    ,value:"　"}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 4px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function kweImHeader(){ 
var selectYorN =       [["", false, true], ["啟用", "停用"], ["Y", "N"]];
var allVipType = vat.bean("allVipType");
var allCustomerTypeCode = [["", false, true], ["1-自然人","2-法人 "], ["1", "2"]];//類型
var allGender = [["", false, true], ["男","女"], ["M", "F"]];
var allCountry = vat.bean("allCountry");
var allMonth = [["", false, true], ["1","2","3","4","5","6","7","8","9","10","11","12"], ["1","2","3","4","5","6","7","8","9","10","11","12"]];
var allDay = [["", false, true], ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"], ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"]];

	vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='0'",
	title:"會員資料查詢作業", rows:[  
		{row_style:"", cols:[
				{items:[{name:"#L.customerCode", type:"LABEL", value:"會員編號"}]},
				{items:[{name:"#F.customerCode", type:"TEXT",size:30}]},
				{items:[{name:"#L.vipTypeCode", type:"LABEL" , value:"會員類別"}]},
	 		    {items:[{name:"#F.vipTypeCode", type:"SELECT",init:allVipType}]},
				 	{items:[{name:"#L.vipStartDate", type:"LABEL", value:"起始日"}]},
					{items:[{name:"#F.vipStartDate", type:"DATE" }]}
	 		    ]},

		{row_style:"", cols:[
				{items:[{name:"#L.identityCode", type:"LABEL", value:"身份證明代碼"}]},
				{items:[{name:"#F.identityCode", type:"TEXT",size:30}]},
				{items:[{name:"#L.enable", type:"LABEL", value:"啟用狀態"}]},
				{items:[{name:"#F.enable", type:"SELECT", init:selectYorN }]},
					{items:[{name:"#L.vipEndDate", type:"LABEL", value:"到期日"}]},
					{items:[{name:"#F.vipEndDate", type:"DATE"}]}
				]},		
		{row_style:"", cols:[
				{items:[{name:"#L.customerCName", type:"LABEL", value:"會員姓名"}]},
				{items:[{name:"#F.customerCName", type:"TEXT",size:30 }]},
				{items:[{name:"#L.countryCode", type:"LABEL", value:"國別"}]},
				{items:[{name:"#F.countryCode", type:"SELECT",init:allCountry}]},
				{items:[{name:"#L.applyDate", type:"LABEL", value:"申請日"}]},
				{items:[{name:"#F.applyDate", type:"DATE"}]}
				]},						
		{row_style:"", cols:[
				{items:[{name:"#L.customerEName", type:"LABEL", value:"英文名稱"}]},
				{items:[{name:"#F.customerEName", type:"TEXT",size:30 }]},
				{items:[{name:"#L.gender", type:"LABEL", value:"性別"}]},
				{items:[{name:"#F.gender", type:"SELECT",init:allGender }]},
				{items:[{name:"#L.birthday", type:"LABEL", value:"生日"}]},
				{items:[{name:"#F.birthdayYear", type:"TEXT"},
						{name:"#L.birthdayYear", type:"LABEL", value:"年"},
				 		{name:"#F.birthdayMonth", type:"SELECT",init:allMonth},
				 		{name:"#F.birthdayMonth", type:"LABEL", value:"月"},
				 		{name:"#F.birthdayDate", type:"SELECT",init:allDay},
				 		{name:"#F.birthdayDate", type:"LABEL", value:"日"}]}

				]}	,	 
			],	 
		beginService:"",
		closeService:""			
	});
}

function kweImDetail(){
var selectYorN = [["", true, false], ["請選擇","啟用", "停用"], ["","Y", "N"]];
var allVipType = vat.bean("allVipType");
var vatPickerId =vat.bean().vatBeanOther.vatPickerId; 
	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;
    var vbSelectionType = "CHECK"; 

	vat.item.make(vnB_Detail,"checkbox",{type:"XBOX",mode:"HIDDEN"});
	vat.item.make(vnB_Detail,"indexNo",{type:"IDX",desc:"序號"});
	vat.item.make(vnB_Detail,"customerCode",{type:"TEXT",size:15,mode:"READONLY",desc:"會員編號"});
	vat.item.make(vnB_Detail,"vipTypeCode",{type:"SELECT",mode:"READONLY", init:allVipType,size:15,desc:"會員類別"});
	vat.item.make(vnB_Detail,"aIdentityCode",{type:"TEXT",size:15,mode:"READONLY",desc:"身分識別代碼"});
	vat.item.make(vnB_Detail,"aChineseName",{type:"TEXT",size:15,mode:"READONLY",desc:"會員姓名"});
	vat.item.make(vnB_Detail,"aEnglishName",{type:"TEXT",size:18,mode:"READONLY",desc:"英文名稱"});
	vat.item.make(vnB_Detail,"vipStartDate",{type:"TEXT",size:18,mode:"READONLY",desc:"起始日"});
	vat.item.make(vnB_Detail,"vipEndDate",{type:"TEXT",size:18,mode:"READONLY",desc:"到期日"});
	vat.item.make(vnB_Detail,"applyDate",{type:"TEXT",size:14,mode:"READONLY",desc:"申請日"});
	vat.item.make(vnB_Detail,"enable",{type:"SELECT", init:selectYorN,size:4,mode:"READONLY",desc:"啟用狀態"});

	
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["id.customerCode"],
//														pickAllService		: function (){return selectAll();},
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														//loadSuccessAfter    : "loadSuccessAfter()",						
														saveBeforeAjxService: "saveBeforeAjxService()",
														//saveSuccessAfter    : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);

}
function kweImDetail1(){

	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = false;


	
	
	vat.block.pageLayout(vnB_Detail1, {
											id                  : "vatDetailDiv1"
														});
	//vat.block.pageDataLoad(vnB_Detail1, vnCurrentPage = 0);

}
function loadBeforeAjxService(){
   //alert("loadBeforeAjxService");
	var processString = "process_object_name=buCustomerModService&process_object_method_name=getAJAXSearchPageData"  
	                  +"&customerCode" 	 + "=" + vat.item.getValueByName("#F.customerCode") 
	                  +"&enable"    	 + "=" + vat.item.getValueByName("#F.enable") 	  
	                  +"&identityCode"   + "=" + vat.item.getValueByName("#F.identityCode") 	                  
	                  +"&vipTypeCode" 	 + "=" + vat.item.getValueByName("#F.vipTypeCode") 
	                  +"&gender"		 + "=" + vat.item.getValueByName("#F.gender")
	                  +"&vipStartDate"	 + "=" + vat.item.getValueByName("#F.vipStartDate")
	                  +"&vipEndDate" 	 + "=" + vat.item.getValueByName("#F.vipEndDate")
	                  +"&applicationDate" 	 + "=" + vat.item.getValueByName("#F.applyDate") 
	                  +"&chineseName"	 + "=" + vat.item.getValueByName("#F.customerCName")
	                  +"&englishName"	 + "=" + vat.item.getValueByName("#F.customerEName")
	                  +"&birthdayYear" 	 + "=" + vat.item.getValueByName("#F.birthdayYear")
	                  +"&birthdayMonth"  + "=" + vat.item.getValueByName("#F.birthdayMonth")
	                  +"&birthdayDate" 	 + "=" + vat.item.getValueByName("#F.birthdayDate")
	                  +"&countryCode" 	 + "=" + vat.item.getValueByName("#F.countryCode")
	                  +"&brandCode" 	 + "=" + document.forms[0]["#loginBrandCode"].value;    
                                                                            
	return processString;											
}
function saveBeforeAjxService()
{   //alert("saveBeforeAjxService");
	
		var processString = "process_object_name=buCustomerModService&process_object_method_name=saveSearchResult" ;
		                    //alert(processString); 
		return processString;					
}





//	取得SAVE要執行的JS FUNCTION
					

function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope; }, 
			                    {other: true 
			                     ,funcSuccess: function() { 
			                     	//alert("123");
			                     	vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });			                   
}

// 清除
function resetForm(){

}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}

function doClosePicker(){

	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=buCustomerModService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
				
}


// 開啟視窗
function openModifyPage(){
    var nItemLine = vat.item.getGridLine();

    var vCustomerCode = vat.item.getGridValueByName("customerCode", nItemLine);
	//開啟buCUstomer.js
	if(!(vCustomerCode == "" || vCustomerCode == "0")){
    var url = "/erp/Bu_Customer:create:20160714mod.page?customerCode=" + vCustomerCode + "&category=master"; 

     sc=window.open(url, '會員資料維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
	}
}

// 鎖欄位
function doFormAccessControl(){

}
function importFormData(){
	var width    = "600";
    var height   = "400";

  

    	var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=BU_CUSTOMER_IMPORT" +
			"&importFileType=XLS" +
	        "&processObjectName=buCustomerModService" +
	        "&processObjectMethodName=executeImportCustomer" +
	        "&arguments=" + document.forms[0]["#loginBrandCode"].value +
	        "{$}" + document.forms[0]["#loginEmployeeCode" ].value +
	        "&parameterTypes=STRING{$}STRING" +
	        "&blockId=" + vnB_Detail1,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}