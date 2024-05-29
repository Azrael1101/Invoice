/*** 
 *	檔案: BuCommonPhrase.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Detail = 1;
var vnB_master = 2; 
  
function kweImBlock(){
  kweSearchInitial();
  kweImHeader();
  kweButtonLine();
  if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab2","Line" ,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false , "");
  }
	kweImDetail();
}

function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
 			{name:"#B.search"  		, type:"IMG"   , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
 			{name:"SPACE"         , type:"LABEL"    , value:"　"},
 			{name:"#B.clear"      , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:""},
 			{name:"SPACE"         , type:"LABEL"    , value:"　"},
 	 		{name:"#B.exit"       , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
 	 		{name:"SPACE"         , type:"LABEL"    , value:"　"},
 			{name:"#B.picker"     , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"},
 			{name:"SPACE"         , type:"LABEL"    , value:"　"}
		],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

 
function kweImHeader(){
var vaOpt0 = [["", "", "Y"], ["自然人 ", "法人"], ["1", "2"]];
var vaOpt1 = [["", "", "Y"], ["是 ", "否"], ["Y", "N"]];
var vaOpt2 = [["", "", "Y"], ["男 ", "女"], ["M", "F"]];
var month = [["", "", "Y"], ["01","02","03","04","05","06","07","08","09","10","11","12"], ["1","2","3","4","5","6","7","8","9","10","11","12"]];
var day = [["", "", "Y"], ["01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"], ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"]];
var brandCodes = vat.bean("brandCodes");
var alldepartment    = vat.bean("alldepartment");
vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"員工資料查詢", 
	rows:[
	 	{row_style:"", cols:[
			{items:[{name:"#L.type", type:"LABEL" , value:"類別"}]},
			{items:[{name:"#F.type", type:"SELECT",  bind:"type", init:vaOpt0}]},
			{items:[{name:"#L.identityCode", type:"LABEL" , value:"身分證明代號"}]},
			{items:[{name:"#F.identityCode", type:"TEXT",  bind:"identityCode" , size:20}]},
			{items:[{name:"#L.chineseName", type:"LABEL" , value:"姓名"}]},
			{items:[{name:"#F.chineseName", type:"TEXT",  bind:"chineseName" , size:20}]}
	 	]},
	 	{row_style:"", cols:[
		 	{items:[{name:"#L.birthday", type:"LABEL" , value:"生日"}]},
			{items:[{name:"#F.birthdayYear", type:"TEXT",  bind:"birthdayYear" , size:4 , maxLen:4},
							{name:"#F.birthdayYear", type:"LABEL", value:"年"},
							{name:"#F.birthdayMonth", type:"SELECT",  bind:"birthdayMonth", init:month},
							{name:"#F.birthdayMonth", type:"LABEL", value:"月"},
							{name:"#F.birthdayDay", type:"SELECT",  bind:"birthdayDay", init:day},
							{name:"#F.birthdayDay", type:"LABEL", value:"日"}
							]},
			{items:[{name:"#L.gender", type:"LABEL" , value:"性別"}]},
			{items:[{name:"#F.gender", type:"SELECT",  bind:"gender" , size:20 , init:vaOpt2}]},
			{items:[{name:"#L.englishName", type:"LABEL" , value:"英文"}]},
			{items:[{name:"#F.englishName", type:"TEXT",  bind:"englishName" , size:20 }]}
	 	]},
	 	{row_style:"", cols:[
		 	{items:[{name:"#L.employeeCode", type:"LABEL" , value:"工號"}]},
			{items:[{name:"#F.employeeCode", type:"TEXT",  bind:"employeeCode" , size:20 }]},
			{items:[{name:"#L.employeePosition", type:"LABEL" , value:"職稱"}]},
			{items:[{name:"#F.employeePosition", type:"TEXT",  bind:"employeePosition" }]},
			{items:[{name:"#L.isDepartmentManager", type:"LABEL" , value:"部門主管"}]},
			{items:[{name:"#F.isDepartmentManager", type:"SELECT",  bind:"isDepartmentManager" ,init:vaOpt1}]}
	 	]},
	 	{row_style:"", cols:[
		 	{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},
			{items:[{name:"#F.brandCode", type:"SELECT",  bind:"brandCode" , size:20 ,init:brandCodes}]},
			{items:[{name:"#L.arriveDateB", type:"LABEL" , value:"到職日起"}]},
			{items:[{name:"#F.arriveDateB", type:"DATE", size:20}]},
			{items:[{name:"#L.arriveDateE", type:"LABEL" , value:"到職日迄"}]},
			{items:[{name:"#F.arriveDateE", type:"DATE", size:20}]}
	 	]},
	 	{row_style:"", cols:[
		 	{items:[{name:"#L.employeeDepartment", type:"LABEL" , value:"部門"}]},
			{items:[{name:"#F.employeeDepartment", type:"SELECT",  bind:"employeeDepartment" , size:20 ,init:alldepartment}],td:" colSpan=5"}
	 	]}
  ], 	 
		beginService:"",
		closeService:""			
	});

}


function kweImDetail(){
    var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;

	vat.item.make(vnB_Detail, "checked"     	, {type:"XBOX"});
  	vat.item.make(vnB_Detail, "indexNo"			, {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_Detail, "employeeCode"	, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"工號" });
	vat.item.make(vnB_Detail, "chineseName" 	, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"中文姓名" });
	vat.item.make(vnB_Detail, "englishName" 	, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"英文姓名" });
	vat.block.pageLayout(vnB_Detail, {
												id                  	: "vatDetailDiv",
												pageSize            	: 10,
												searchKey           	: ["employeeCode","chineseName"],
												selectionType       	: "CHECK",
												indexType           	: "AUTO",
						            			canGridDelete       	: vbCanGridDelete,
												canGridAppend       	: vbCanGridAppend,
												canGridModify       	: vbCanGridModify,	
												loadBeforeAjxService	: "loadBeforeAjxService()",
												saveBeforeAjxService	: "saveBeforeAjxService()",
												saveSuccessAfter		: "saveSuccessAfter()"
												});
}


function doPageDataSave(){
    //alert("doPageDataSave");
    vat.block.pageDataSave(vnB_Detail);
}

function doPageRefresh(){
    //alert("doPageRefresh");
    vat.block.pageRefresh(0);
}


/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
}

function kweSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value
        };
    vat.bean.init(function(){
		return "process_object_name=buEmployeeService&process_object_method_name=executeSearchInitial"; 
     },{other: true});
  }
}

function kweFormClear(){ 
 	 //vat.bean().loginBrandCode     = document.forms[0]["#loginBrandCode"    ].value;  	
   //vat.bean().loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" ].value;	  
	 //vat.bean().formId             = "";	
 	 vat.block.submit(function(){return "process_object_name=imMovementService"+
			                    "&process_object_method_name=executeInitial";}, {other:true});
}

/*
	空白頁
*/
function appendBeforeService(){
	return true;
}

/*
	載入LINE資料
*/
function loadBeforeAjxService(){
	//alert(vat.item.getValueByName("#F.gender"));
		processString = "process_object_name=buEmployeeService&process_object_method_name=getAJAXPageData"+
		"&department"     						+ "=" + vat.item.getValueByName("#F.department"     ) +
		"&type"     						+ "=" + vat.item.getValueByName("#F.type"     ) +
		"&identityCode"         + "=" + vat.item.getValueByName("#F.identityCode"        	) +
		"&chineseName"          + "=" + vat.item.getValueByName("#F.chineseName"        	) +
		"&birthdayYear"         + "=" + vat.item.getValueByName("#F.birthdayYear"        	) +
		"&birthdayMonth"        + "=" + vat.item.getValueByName("#F.birthdayMonth"        ) +
		"&birthdayDay"          + "=" + vat.item.getValueByName("#F.birthdayDay"        	) +
		"&gender"          			+ "=" + vat.item.getValueByName("#F.gender"        				) +
		"&englishName"          + "=" + vat.item.getValueByName("#F.englishName"        	) +
		"&employeeCode"         + "=" + vat.item.getValueByName("#F.employeeCode"        	) +
		"&employeePosition"     + "=" + vat.item.getValueByName("#F.employeePosition"     ) +
		"&isDepartmentManager"  + "=" + vat.item.getValueByName("#F.isDepartmentManager"  ) +
		"&brandCode"          	+ "=" + vat.item.getValueByName("#F.brandCode"        		) +
		"&arriveDateB"          + "=" + vat.item.getValueByName("#F.arriveDateB"        	) +
		"&arriveDateE"          + "=" + vat.item.getValueByName("#F.arriveDateE"        	) 
	return processString;			
}


/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
		processString = "process_object_name=buEmployeeService&process_object_method_name=saveSearchResult"
	return processString;
}

function saveSuccessAfter(){
		//alert('update ok');
}

function getEmployeeNameByLine(){
	var nItemLine = vat.item.getGridLine();
	var vemployeeCode = vat.item.getGridValueByName("employeeCode"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setGridValueByName("employeeCode", nItemLine, vemployeeCode);
	vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vemployeeCode,
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               vat.item.setGridValueByName("employeeName", nItemLine, vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });		
}


function doSearch(){
		//alert("searchService");	
    //vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
    //alert("timeScope:"+vat.bean().vatBeanOther.timeScope);
	  vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail = 1, vnCurrentPage = 1);}
			                    });			                   
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}
function doClosePicker(){
	//alert('送出');
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=buEmployeeService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
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
