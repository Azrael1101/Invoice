/*** 
 *	檔案: buShop.js
 *	說明: 類別代號,抽成率維護
 */
 
//關閉DEBUG
vat.debug.disable();
//設定全域變數
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_master = 2;
var vnB_master1 = 3;
var vnB_master2 = 4;
var vnB_master3 = 5;
/**
72	formInitial();
96	buttonLine();
124	headerInitial();
164	kweMaster();
212	kweMaster1();
248	kweMaster2();
280	kweMaster3();







**/
//程式進入點
function outlineBlock()
{
	
  	formInitial();//初始化
  	//alert(document.forms[0]["#formId"            ].value);
  	buttonLine();//按鈕列表
  	headerInitial();//單頭列
	

	if (typeof vat.tabm != 'undefined')
	{
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","主檔資料"      ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);    
		vat.tabm.createButton(0 ,"xTab2","合約主檔" 		,"vatBlock_Master1" ,"images/tab_bushop_main_dark.GIF" ,"images/tab_bushop_main_light.GIF", false);
   		vat.tabm.createButton(0 ,"xTab3","專櫃人員主檔" 	,"vatBlock_Master2" ,"images/tab_bushop_employee_dark.GIF" ,"images/tab_shop_employee_light.GIF", true);
		vat.tabm.createButton(0 ,"xTab4","專櫃機台主檔" 	,"vatBlock_Master3" ,"images/tab_machine_dark.jpg" ,"images/tab_machine_light.jpg", true);
	}
	kweMaster();
	kweMaster1();
	kweMaster2();
	kweMaster3();
	doFormAccessControl();//控制權限
}

//開始初始化
function formInitial(){
		 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { //document.forms[0]["#" ].value 回傳網頁端的值
          brandCode  			: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
          vat.bean.init(  
             function(){ //進入action-->performInitial 初始method
                    return "process_object_name=buShopAction&process_object_method_name=performInitial"; 
         },{
             other: true , bind: true
        }
        );
        
	   
  }
  
}
//按鈕初始化
function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Bu_Shop:search:20151031.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export1"      , type:"IMG"    ,value:"人員明細匯出",   src:"./images/employee_export.jpg" , eClick:"doExport('Employee')"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export2"      , type:"IMG"    ,value:"機台明細匯出",   src:"./images/machine_export.jpg" , eClick:"doExport('Machine')"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import1"      , type:"IMG"    ,value:"人員明細匯入",   src:"./images/employee_import.jpg" , eClick:"importFormData('Employee')"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import2"      , type:"IMG"    ,value:"機台明細匯入",   src:"./images/machine_import.jpg" , eClick:"importFormData('Machine')"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},	
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	
//單頭初始化
function headerInitial(){ 

var allWarehouse        = vat.bean("allWarehouse");
var allMigrationType = [["", true, false], ["出境","入境"], ["E", "I"]];
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"專櫃資料維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.shopCode", type:"LABEL", value:"專櫃代號<font color='red'>*</font>"}]},
				{items:[{name:"#F.shopCode", type:"TEXT", bind:"shopCode", size:20, maxLen:25, eChange:"changeShopCode()" },
						{name:"#F.enable", type:"CHECKBOX",  bind:"enable" },
						{name:"#L.enable", type:"LABEL",value:"啟用", size:15 }]},
				{items:[{name:"#L.shopCName", type:"LABEL" , value:"專櫃中文名稱"}]},
				{items:[{name:"#F.shopCName", type:"TEXT", bind:"shopCName", size:25, maxLen:25 }]},
				{items:[{name:"#L.shopEName", type:"LABEL" , value:"專櫃英文名稱"}]},
				{items:[{name:"#F.shopEName", type:"TEXT", bind:"shopEName", size:20, maxLen:25 }]},
				{items:[{name:"#L.migrationType", type:"LABEL" , value:"出/入境"}]},
				{items:[{name:"#F.migrationType", type:"SELECT", bind:"migrationType", init:allMigrationType,size:20, maxLen:25 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.salesWarehouseCode", type:"LABEL" , value:"銷售倉庫代號<font color='red'>*</font>"}]},
				{items:[{name:"#F.salesWarehouseCode", type:"TEXT", bind:"salesWarehouseCode",eChange:"changeWarehouse()",size:25, maxLen:25 },
						{name:"#F.salesWarehouseName", type:"TEXT", bind:"salesWarehouseName",size:25,mode:"READONLY" }]},
				{items:[{name:"#L.place", type:"LABEL" , value:"百貨買場代號"}]},
				{items:[{name:"#F.place", type:"TEXT", bind:"place", size:20, maxLen:25 }]},
				{items:[{name:"#L.departmentName", type:"LABEL" , value:"百貨公司名稱"}]},
				{items:[{name:"#F.departmentName", type:"TEXT", bind:"departmentName", size:20, maxLen:25 }]},
				{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode",mode:"READONLY", size:15}]}
				
			]}
			
		], 	
		 
		beginService:"",
		closeService:""			
	});
}
//主檔資料
function kweMaster(){
var allshopType      = vat.bean("allshopType");
var allshopStyle      = vat.bean("allshopStyle");
var allshopLevel      = vat.bean("allshopLevel");
var allShopSalesType = [["", false, true], ["百貨-不配報單","T2免稅-配報單","馬祖完稅-不配報單"], ["1", "2","3"]];
vat.block.create(vnB_master, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[{row_style : "", 
			cols : [{items : [{name : "#L.shopType", type : "LABEL", value : "專櫃類別"}]},
					{items : [{name : "#F.shopType", type : "SELECT", bind : "shopType",init:allshopType, size:20}]},
					{items : [{name : "#L.shopStyle",  type:"LABEL",  value:"專櫃型態"}]},	 
	 				{items : [{name : "#F.shopStyle",  type:"SELECT",   bind:"shopStyle",init:allshopStyle,size:20}]},
	 				{items : [{name : "#L.shopLevel", type : "LABEL", value : "專櫃級別"}]},
					{items : [{name : "#F.shopLevel", type : "SELECT", bind : "shopLevel",init:allshopLevel,size:20}]},
					{items : [{name : "#L.shopSalesType", type : "LABEL", value : "專櫃銷售型態<font color='red'>*</font>"}]},
					{items : [{name : "#F.shopSalesType", type : "SELECT", bind : "shopSalesType",init:allShopSalesType,size:20}]}]},
		 {row_style : "", 
			cols : [{items : [{name : "#L.averageEmployee", type : "LABEL", value : "平均站櫃人數"}]},
					{items : [{name : "#F.averageEmployee", type : "TEXT", bind : "averageEmployee", size:20}]},
					{items : [{name : "#L.incharge",  type:"LABEL",  value:"專櫃主管"}]},	 
	 				{items : [{name : "#F.incharge",  type:"TEXT",   bind:"incharge",size:20}]},
	 				{items : [{name : "#L.contractPerson", type : "LABEL", value : "聯絡人員"}]},
					{items : [{name : "#F.contractPerson", type : "TEXT", bind : "contractPerson",size:20}]},
					{items : [{name : "#L.tel", type : "LABEL", value : "電話"}]},
					{items : [{name : "#F.tel", type : "TEXT", bind : "tel",size:25}]}]},
		 {row_style : "", 
			cols : [{items : [{name : "#L.area", type : "LABEL", value : "區域"}]},
					{items : [{name : "#F.area", type : "TEXT", bind : "area", size:20}]},
					{items : [{name : "#L.system",  type:"LABEL",  value:"體系/業種"}]},	 
	 				{items : [{name : "#F.system",  type:"TEXT",   bind:"system",size:20}]},
	 				{items : [{name : "#L.marketArea", type : "LABEL", value : "商圈"}]},
					{items : [{name : "#F.marketArea", type : "TEXT", bind : "marketArea",size:30}]},
					{items : [{name : "#L.locationId", type : "LABEL", value : "地點代號"}]},
					{items : [{name : "#F.locationId", type : "TEXT", bind : "locationId",size:15}]}]},
		{row_style : "", 
			cols : [{items : [{name : "#L.supplierCode", type : "LABEL", value : "廠商代號"}]},
					{items : [{name : "#F.supplierCode", type : "TEXT", bind : "supplierCode", size:20}]},
					{items : [{name : "#L.guiCode",  type:"LABEL",  value:"統一編號"}]},	 
	 				{items : [{name : "#F.guiCode",  type:"TEXT",   bind:"guiCode",size:20}]},
	 				{items : [{name : "#L.scheduleDate",  type:"LABEL",  value:"預計啟用日"}]},	 
	 				{items : [{name : "#F.scheduleDate",  type:"DATE",   bind:"scheduleDate",size:20}]},
	 				{items : [{name : "#L.tsubo", type : "LABEL", value : "坪數"}]},
					{items : [{name : "#F.tsubo", type : "TEXT", bind : "tsubo",size:15}]}]},
	 	{row_style : "", 
			cols : [
	 				{items : [{name : "#L.shopAddress", type : "LABEL", value : "專櫃地址"}]},
	 				{items : [{name : "#F.shopAddress", type : "TEXT", bind : "shopAddress",size:100}],td:"colSpan=7"}]}
 	 	],
		beginService:"",
		closeService:""
	});
}
//合約資料
function kweMaster1(){
var allbillType      = vat.bean("allbillType");
var allsalesBonusType      = vat.bean("allsalesBonusType");
	vat.block.create(vnB_master1, {
	id: "vatBlock_Master1", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.contractBeginDate", type:"LABEL",  value:"合約生效日"}]},	 
	 		{items:[{name:"#F.contractBeginDate", type:"DATE", bind:"contractBeginDate", size:20}]},		 
	 		{items:[{name:"#L.contractEndDate",   type:"LABEL",  value:"合約終止日"}]},	 
			{items:[{name:"#F.contractEndDate",   type:"DATE", bind:"contractEndDate", size:20}]},
			{items:[{name:"#L.salesBonusType",         type:"LABEL",  value:"業績認定方式"}]},	 
	 		{items:[{name:"#F.salesBonusType",         type:"SELECT",   bind:"salesBonusType",init:allsalesBonusType, size:20}]},
	 		{items:[{name:"#L.invoiceDay",         type:"LABEL",  value:"發票日"}]},	 
	 		{items:[{name:"#F.invoiceDay",         type:"NUMM",   bind:"invoiceDay", size:20}]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.collectionDay", type:"LABEL",  value:"收款日"}]},	 
	 		{items:[{name:"#F.collectionDay", type:"TEXT", bind:"collectionDay", size:20}]},		 
	 		{items:[{name:"#L.billDay",   type:"LABEL",  value:"對帳日"}]},	 
			{items:[{name:"#F.billDay",   type:"TEXT", bind:"billDay", size:20}]},
			{items:[{name:"#L.billType",         type:"LABEL",  value:"包底方式"}]},	 
	 		{items:[{name:"#F.billType",         type:"SELECT",   bind:"billType",init:allbillType, size:20}]},
	 		{items:[{name:"#L.billAmount",         type:"LABEL",  value:"包底金額"}]},	 
	 		{items:[{name:"#F.billAmount",         type:"NUMM",   bind:"billAmount", size:20}]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.billTel", type:"LABEL",  value:"對帳電話"}]},	 
	 		{items:[{name:"#F.billTel", type:"TEXT", bind:"billTel", size:20}]},		 
	 		{items:[{name:"#L.billAddress",   type:"LABEL",  value:"帳單地址"}]},	 
			{items:[{name:"#F.billAddress",   type:"TEXT", bind:"billAddress", size:100}],td:"colSpan=5"}]}		

 	 	],
		beginService:"",
		closeService:""
	});
}
//專櫃人員主檔
function kweMaster2()
{
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
  
  	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
  	vat.item.make(vnB_master2, "indexNo", {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_master2, "employeeCode", {type:"TEXT" , size:10, maxLen:6, desc:"工號",eChange: function(){ changeEmployCodeByName();}});
	vat.item.make(vnB_master2, "employeeName", {type:"TEXT" ,  size:10, desc:"姓名"});
	vat.item.make(vnB_master2, "enable", {type:"CHECKBOX" , size:12, maxLen:12, desc:"啟用"});
	
	vat.block.pageLayout(vnB_master2,
						{
							id: "vatBlock_Master2",
							pageSize: 10,
							////selectionType :  ["indexNo","employeeCode","employeeName","enable"],
							indexType	: "AUTO",
							canGridDelete : vbCanGridDelete,
							canGridAppend : vbCanGridAppend,
							canGridModify : vbCanGridModify,
							//appendBeforeService : "appendBeforeService()",
							//appendAfterService  : "appendAfterService()",
							loadBeforeAjxService: "loadBeforeAjxService("+vnB_master2+")",
							//loadSuccessAfter    : "loadSuccessAfter("+vnB_master2+")",
							//eventService        : "eventService()",
							saveBeforeAjxService: "saveBeforeAjxService("+vnB_master2+")"
							//saveSuccessAfter    : "saveSuccessAfter("+vnB_master2+")"

	});
	//vat.block.pageDataLoad(vnB_master2, vnCurrentPage = 1);
}
//機台主檔
function kweMaster3(){
  var allStatus = [["","true",""],["AUTO-自動","MANUAL-手動"],["AUTO","MANUAL"]]; 
		vbCanGridDelete = true;
		vbCanGridAppend = true;
		vbCanGridModify = true;
	 /*
	  * itemNo				:			品號	
	  * specInfo			:			預留欄位
	  * itemName			:			品名
	  * supplier			:			來源庫
	  * purTotalAmount		:			來源庫存
	  * quantity			:			需求量
	  * shopCode			:			店別
	  * reTotalAmount		:			店庫存
	  */

    vat.item.make(vnB_master3, "indexNo"		, {type:"IDX"   ,  desc:"序號"  });
    vat.item.make(vnB_master3, "posMachineCode" , {type:"TEXT"  , size:18, maxLen:20, desc:"機台編號"});
	vat.item.make(vnB_master3, "uploadType"		, {type:"SELECT"  , size:18, maxLen:20, desc:"上傳型態", init:allStatus});
    
	vat.item.make(vnB_master3, "lastOrderNo" 	, {type:"TEXT" , size:12, maxLen:20, desc:"最後銷售單號"	, mode:"READONLY"});
	vat.item.make(vnB_master3, "enable"     	, {type:"CHECKBOX" , size: 5, maxLen:20, desc:"啟用"	});
	vat.item.make(vnB_master3, "printerId"     	, {type:"TEXT" , size: 5, maxLen:20, desc:"印表機硬體位址"	});
	vat.block.pageLayout(vnB_master3, {
														id: "vatBlock_Master3",
														pageSize: 10,

								                        canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,
														beginService: "",
														indexType	: "AUTO",
								                        //closeService: function(){kweImInitial();},
														//appendBeforeService : "kwePageAppendBeforeMethod()",
														//appendAfterService  : "kwePageAppendAfterMethod()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_master3+")",
														//loadSuccessAfter    : "kwePageLoadSuccess()",
														//eventService        : "assignOriginalQtyToArrival()",
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_master3+")"
														//saveSuccessAfter    : "saveSuccessAfter()"
	});
	//vat.block.pageDataLoad(vnB_master3, vnCurrentPage = 1);
}




// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
    	refreshForm("");
	 }
}

// 送出的返回
function createRefreshForm(){
	 vat.item.setValueByName("#L.currentRecord", "0");
     vat.item.setValueByName("#L.maxRecord", "0");
     vat.bean().vatBeanPicker.result = null; 
	refreshForm("");
}

// 離開按鈕按下
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}




// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if(!(vat.item.getValueByName("#F.shopCode") ==="" ||
		vat.item.getValueByName("#F.salesWarehouseCode") ==="" ||
		vat.item.getValueByName("#F.shopSalesType") ==="")){
		if(confirm(alertMessage))
		{
			vat.block.pageDataSave(vnB_master3, 
			{
		   		funcSuccess:function()
		   		{
		   			vat.block.pageDataSave(vnB_master2, 
					{
						 funcSuccess:function()
		   				{
				    		vat.bean().vatBeanOther.formAction = formAction;
							vat.block.submit(function()
								{
		
									return "process_object_name=buShopAction&process_object_method_name=performTransaction";
		
								},
								{
									bind:true, link:true, other:true,
									funcSuccess: function ()
									{
		
										vat.block.pageRefresh(vnB_master3);
										vat.block.pageRefresh(vnB_master2);
									} 
								});	
						}
					});
				}
			});
		}
	}else{
		alert("必要欄位未填入");
	}
}
// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
//alert("進入Picker");
	if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
			//alert("TEST");
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].shopCode;
		  refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}


// 刷新頁面
function refreshForm(code)
{
	

	document.forms[0]["#formId"].value = code; 
    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;  

    vat.block.submit(
        function()
        {
       		 //alert(code);
            return "process_object_name=buShopAction&process_object_method_name=performInitial"; 
        },
        {
        		other : true , bind: true,
           funcSuccess:function()
           {
                vat.item.bindAll();
                vat.block.pageRefresh(vnB_master2);
                vat.block.pageRefresh(vnB_master3); 
                changeWarehouse();                
                doFormAccessControl();
        }}
    );
     
}

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter()
{
    return true;
}
// 第一次載入 重新整理
function loadBeforeAjxService(div)
{   //alert("---------loadBeforeAjxService:" + vat.bean().vatBeanOther.formId);
	
		var processString = "process_object_name=buShopMainService&process_object_method_name=getAJAXMachinePageData" 
		                    +"&formId=" + document.forms[0]["#formId"].value
		                    +"&div=" + div
		                    +"&shopCode="+vat.item.getValueByName("#F.shopCode");
		                    //alert(processString); 
		return processString;							
}

function saveBeforeAjxService(div)
{  // alert("saveBeforeAjxService");
	
		var processString = "process_object_name=buShopMainService&process_object_method_name=updateOrSaveAJAXPageLinesData" 
		                    +"&formId=" + document.forms[0]["#formId"].value
		                    +"&div=" + div
		                    +"&shopCode="+vat.item.getValueByName("#F.shopCode");
		                    //alert(processString); 
		return processString;					
}
// 依formId鎖form
function doFormAccessControl(){
/*
	var formId = vat.bean().vatBeanOther.formId;
//	alert(typeof formId);
	if( formId != "" ){
		vat.item.setAttributeByName("#F.commissionRate", "readOnly", true);
		
	}else{
		vat.item.setAttributeByName("#F.commissionRate", "readOnly", false);
	}
	
	var enable = vat.item.getValueByName("#F.enable"); 
//	alert("enable = " + enable);
	if(enable == "Y" ){
		vat.item.setValueByName("#F.enable", "N");
	}else{
		vat.item.setValueByName("#F.enable", "Y");
	}
*/		
}
function changeEmployCodeByName()
{

      var same = 0;//0:不同　1：相同
	var nItemLine = vat.item.getGridLine();
	var vemployeeCode = vat.item.getGridValueByName("employeeCode"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
		
	for(var i=1 ; i<nItemLine ; i++){
		var employee = vat.item.getGridValueByName("employeeCode"	, i).replace(/^\s+|\s+$/, '').toUpperCase();
		if(vemployeeCode  == employee){
			vat.item.setGridValueByName("employeeCode", nItemLine, "");
			same = 1;
		}
	}
	if(same == 0){
	
		vat.item.setGridValueByName("employeeCode", nItemLine, vemployeeCode);
		
		vat.ajax.XHRequest(
       	{
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vemployeeCode,
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               vat.item.setGridValueByName("employeeName", nItemLine, vat.ajax.getValue("EmployeeName", oXHR.responseText));
               vat.item.setGridValueByName("status", nItemLine, "未驗證");
        }   
       });
      
     }	
}

function changeWarehouse() {
	var processString = "process_object_name=buShopMainService&process_object_method_name=getWarehouseNameByCode" +
						"&brandCode="  + vat.item.getValueByName("#F.brandCode") +
						"&salesWarehouseCode="  + vat.item.getValueByName("#F.salesWarehouseCode");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.salesWarehouseName", vat.ajax.getValue("salesWarehouseName", vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.salesWarehouseCode", vat.ajax.getValue("salesWarehouseCode", vat.ajax.xmlHttp.responseText));
		}
	});
}
function changeShopCode() {
	var processString = "process_object_name=buShopMainService&process_object_method_name=getShopByCode" +
						"&brandCode="  + vat.item.getValueByName("#F.brandCode") +
						"&shopCode="  + vat.item.getValueByName("#F.shopCode");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			if(vat.ajax.getValue("message", vat.ajax.xmlHttp.responseText)==="店別已存在，請勿重複建置"){
				alert(vat.ajax.getValue("message", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.shopCode", vat.ajax.getValue("shopCode", vat.ajax.xmlHttp.responseText));
			}
		}
	});
}


function doExport(functionType){
	if(vat.item.getValueByName("#F.shopCode")!==null){
		if(functionType==='Employee'){
			var beanName = "BU_SHOP_EMPLOYEE_EXPORT";
			var div = vnB_master2;
			var processObjectName = "buShopMainService";
			var processObjectMethodName = "exportShopDetail";
			var parameter =  "&shopCode" 	 + "=" + vat.item.getValueByName("#F.shopCode") 
						    +"&brandCode"    	 + "=" + vat.item.getValueByName("#F.brandCode") 
		                    +"&function"			 + "=" + functionType;
		}
		else if(functionType==='Machine'){
			var beanName = "BU_SHOP_MACHINE_EXPORT";
			var div = vnB_master3;
			var processObjectName = "buShopMainService";
			var processObjectMethodName = "exportShopDetail";
			var parameter =  "&shopCode" 	 + "=" + vat.item.getValueByName("#F.shopCode") 
						    +"&brandCode"    	 + "=" + vat.item.getValueByName("#F.brandCode") 
		                    +"&function"			 + "=" + functionType;
		}
		
		
		// var customer = vat.utils.escape(RTrim(vat.item.getValueByName("#F.customerName")));
		
		//var customer = (RTrim(vat.item.getValueByName("#F.customerName")));
		var url;
	    	url = "/erp/jsp/ExportFormView.jsp" +
	              "?exportBeanName="+ beanName +
	              "&fileType=XLS" +
	              "&processObjectName=" + processObjectName +
	              "&processObjectMethodName=" + processObjectMethodName + parameter
						  
	    var width = "200";
	    var height = "30";
	    url = encodeURI(encodeURI(url));

	    vat.block.pageSearch(div, {
	    		funcSuccess : function(){
	    			window.open(url, '資料匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});

	    
    }
    else{
    	alert('專櫃代碼尚未填入');
    }
}


// 明細匯入
function importFormData(functionType){
	var width = "600";
    var height = "400";
    var shopCode = vat.item.getValueByName("#F.shopCode");
	var div;
	var importBeanName;
	var processObjectName;
	var processObjectMethodName ;
	var parameter;






    if(functionType==='Employee'){
	    div = vnB_master2;
	    importBeanName = 'BU_SHOP_EMPLOYEE';
	    processObjectName = 'buShopMainService';
	    processObjectMethodName = 'executeImportShopDetail';
	    parameter = "&arguments=" + shopCode + "{$}" + document.forms[0]["#loginEmployeeCode" ].value + "{$}" + functionType +
	        			"&parameterTypes=STRING" + "{$}" + "STRING"+ "{$}" + "STRING"+
	        			"&blockId=" + div;
	}
	else if(functionType==='Machine')
	{
		div = vnB_master3;
		importBeanName = 'BU_SHOP_MACHINE';
	    processObjectName = 'buShopMainService';
	    processObjectMethodName = 'executeImportShopDetail';
	    parameter = "&arguments=" + shopCode + "{$}" + document.forms[0]["#loginEmployeeCode" ].value + "{$}" + functionType +
	        			"&parameterTypes=STRING" + "{$}" + "STRING"+ "{$}" + "STRING"+
	        			"&blockId=" + div;
    }

	
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=" + importBeanName +
		"&importFileType=XLS" +
        "&processObjectName=" + processObjectName +
        "&processObjectMethodName=" +processObjectMethodName +parameter,"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}