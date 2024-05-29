vat.debug.disable();
var afterSavePageProcess = "";

var vnB_Button		= 0;
var vatHeadDiv		= 1;
var vatMasterDiv	= 2; 
var vatDetailDiv	= 3;
var vatAmountDiv	= 4;
var vatApprovalDiv	= 5;
var PurchaseCurrencyCode = "";

/* 初始化 */
function outlineBlock(){
	formDataInitial();
	buttonLine(); 
	headerInitial();
	//closeAmount(); 
	var status = vat.bean("status");
 	if(typeof vat.tabm != 'undefined') {
 	    vat.tabm.createTab(0, "vatTabSpan", "L", "float");                                                                                                                                                                                                                                                           
	    vat.tabm.createButton(0, "xTab1", "主檔資料", "vatMasterDiv",   "images/tab_master_data_dark.gif",   "images/tab_master_data_light.gif"); 
		vat.tabm.createButton(0, "xTab2", "明細資料", "vatDetailDiv",   "images/tab_detail_data_dark.gif",   "images/tab_detail_data_light.gif");
		vat.tabm.createButton(0, "xTab3", "金額統計", "vatAmountDiv",   "images/tab_total_amount_dark.gif",  "images/tab_total_amount_light.gif", "", "showTotalCountPage()");
 		vat.tabm.createButton(0, "xTab4", "簽核資料", "vatApprovalDiv", "images/tab_approval_data_dark.gif", "images/tab_approval_data_light.gif");    
	}
	masterInitial();
	//changeCategoryType();
	detailInitial();
	amountInitial();
	//changeCategoryType();
	//alert("one change")
	//changeCategory01();	
	kweWfBlock( vat.item.getValueByName("#F.brandCode"), 
            	document.forms[0]["#orderTypeCode"].value, 
             	vat.item.getValueByName("#F.orderNo"),
             	document.forms[0]["#loginEmployeeCode"].value );
}


/* 畫面欄位資料內容初始化 */
function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#formId"].value != '[binding]'){
        vat.bean().vatBeanOther = 
  	    {brandCode     		: document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode 		: document.forms[0]["#orderTypeCode"].value,
	     formId        		: document.forms[0]["#formId"].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	    };     
        vat.bean.init(function(){
		return "process_object_name=poPurchaseMainAction&process_object_method_name=performInitial"; 
        },{other: true});
    }
}


/* 重新抓取頁面資料 */
function refreshForm(vsHeadId){
	document.forms[0]["#formId"].value    		= vsHeadId;	
	vat.bean().vatBeanOther.brandCode     		= document.forms[0]["#loginBrandCode"].value;	
  	vat.bean().vatBeanOther.loginEmployeeCode  	= document.forms[0]["#loginEmployeeCode"].value;	  
  	vat.bean().vatBeanOther.orderTypeCode 		= document.forms[0]["#orderTypeCode"].value;    
	vat.bean().vatBeanOther.formId        		= document.forms[0]["#formId"].value;
	//alert("00");
	vat.block.submit(
		function(){
			return "process_object_name=poPurchaseMainAction&process_object_method_name=performInitial";  
     	},{other: true, 
     	   funcSuccess:function(){
     	   		vat.item.bindAll();     	   		     	   	
     	   		closeAmount();
     	   		changeCategoryType();     	   		  	
     	   		//changeCategory01();
     	       	vat.block.pageDataLoad( vatDetailDiv,  vnCurrentPage = 1);  
     	       	   	     	       	       	
     	  }}
    );
}


/* 畫出上方按鈕與定義按鈕功能 */
function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
	    
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new",           type:"IMG",     value:"新增",  src:"./images/button_create.gif",         eClick:"createRefreshForm()"},
	 		  //{name:"SPACE",            type:"LABEL",   value:"　"},
	 			{name:"#B.search" ,       type:"PICKER",  value:"查詢",  src:"./images/button_find.gif", 
	 									  openMode:"open", 
										  servicePassData:function()
										  	{return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode")},
	 									  service:"Po_PurchaseOrder:search:20091226.page",
	 									  left:0, right:0, width:1024, height:768,	
	 									  serviceAfterPick:function(){doAfterPickerProcess()}},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},
	 	 		{name:"#B.exit",          type:"IMG",   value:"離開",    src:"./images/button_exit.gif",           eClick:'closeWindows("CONFIRM")'},
	 	 	  //{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.submit",        type:"IMG",   value:"送出",    src:"./images/button_submit.gif",         eClick:'doSubmit("SUBMIT")'},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.save",          type:"IMG",   value:"暫存",    src:"./images/button_save.gif",           eClick:'doSubmit("SAVE")'},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.void",          type:"IMG",   value:"作廢",    src:"./images/button_void.gif",           eClick:'doSubmit("VOID")'},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.close",        type:"IMG",   value:"結案",    src:"./images/button_close.gif",         eClick:'doSubmit("CLOSE")'},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"}, 	
	 			{name:"#B.submitBg",      type:"IMG",   value:"背景送出", src:"./images/button_submit_background.gif",  eClick:'doSubmit("SUBMIT_BG")'},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},		
	 			{name:"#B.message",       type:"IMG",   value:"訊息提示", src:"./images/button_message_prompt.gif",  eClick:'showMessage()'},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.listReport",    type:"BUTTON",value:"費用單列印",    eClick:'listReport()'},	//, src:"./images/button_form_print.gif"
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},
	 		  	{name:"#B.openReportWindow",type:"IMG",value:"單據列印", src:"./images/button_form_print.gif", eClick:'openReportWindow()'},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.export",        type:"IMG",   value:"明細匯出",  src:"./images/button_detail_export.gif",  eClick:'doSubmit("EXPORT")'},
	 		  //{name:"SPACE",            type:"LABEL", value:"　"},
	 		  //{name:"#B.import",        type:"IMG",   value:"明細匯入",  src:"./images/button_detail_import.gif", eClick:'doSubmit("IMPORT")'},
	 		    {name:"#B.import", 		  type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  , 
	 									 openMode:"open", 
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,	
	 									 serviceAfterPick:function(){afterImportSuccess()}},
	 		    {name:"SPACE",            type:"LABEL", value:"　"},
	 			{name:"#B.first",         type:"IMG",   value:"第一筆",   src:"./images/play-first.png",   eClick:"gotoFirst()"},
	 			{name:"#B.forward",       type:"IMG",   value:"上一筆",   src:"./images/play-back.png",    eClick:"gotoForward()"},
	 			{name:"#B.next",          type:"IMG",   value:"下一筆",   src:"./images/play.png",         eClick:"gotoNext()"},
	 			{name:"#B.last",          type:"IMG",   value:"最後一筆",  src:"./images/play-forward.png", eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB",  bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE",            type:"LABEL", value:" / "},
	 			{name:"#L.maxRecord",     type:"NUMB",  bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

/* 畫出單頭與定義單頭欄位 */
function headerInitial(){
	vat.block.create(vatHeadDiv, {
		id: "vatHeadDiv", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"採購單維護作業", rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.orderType",          type:"LABEL",  value:"單別<font color='red'>*</font>"}]},	 
	 		{items:[{name:"#F.orderTypeCode",      type:"SELECT", bind:"orderTypeCode",      mode:"READONLY"}]},		 
	 		{items:[{name:"#L.orderNo",            type:"LABEL",  value:"單號"}]},
			{items:[{name:"#F.orderNo",            type:"TEXT",   bind:"orderNo",    size:20, mode:"READONLY", back:false},
	 		 		{name:"#F.headId",             type:"TEXT",   bind:"headId",     size:10, mode:"READONLY",   back:false }]},
	 		{items:[{name:"#L.BrandCode",          type:"LABEL",  value:"品牌"}]},
	 		{items:[{name:"#F.brandCode",          type:"TEXT",   bind:"brandCode",  size:8,  mode:"HIDDEN"},
	 				{name:"#F.brandName",          type:"TEXT",   bind:"brandName",           mode:"READONLY",  back:false}]},
			{items:[{name:"#L.Status",             type:"LABEL",  value:"狀態"}]},	
			{items:[{name:"#F.status",             type:"TEXT",   bind:"status",     size:12, mode:"HIDDEN"},
	  				{name:"#F.statusName",         type:"TEXT",   bind:"statusName",          mode:"READONLY",  back:false}]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.supplierCode",       type:"LABEL",  value:"廠商代號<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.supplierCode",       type:"TEXT",   bind:"supplierCode", size:16, mask:"CCCCCCCCCCCCCCCC", onchange:"onChangeSupplierCode()" },
					{name:"#B.supplierCode",	   type:"PICKER", value:"PICKER", src:"./images/start_node_16.gif",
	 									 		   openMode:"open", 
	 									 		   service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 		   left:0, right:0, width:1024, height:768,	
	 									 		   serviceAfterPick:function(){doAfterPickerSupplier();}},
	 				{name:"#L.supplierName",       type:"LABEL",  value:"<br>"},
	 		 		{name:"#F.supplierName",       type:"TEXT",   bind:"supplierName",       size:30, mode:"READONLY"}]},
	 		{items:[{name:"#L.superintendentCode", type:"LABEL",  value:"採購負責人<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.superintendentCode", type:"TEXT",   bind:"superintendentCode", size:12, mask:"CCCCCC", onchange:"onChangeSuperintendent()" },
	 				{name:"#B.superintendentCode", type:"PICKER", value:"查詢",  src:"./images/start_node_16.gif",
	 									 			openMode:"open", 
	 									 			service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			serviceAfterPick:function(){doAfterPickerEmp()}},
	 		 		{name:"#F.superintendentName", type:"TEXT",   bind:"superintendentName", size:12, mode:"READONLY"}]},
	 		{items:[{name:"#L.CreatedBy",          type:"LABEL",  value:"填單人員"}]},
	 		{items:[{name:"#F.createdBy",          type:"SELECT",   bind:"createdBy",          size:12, mode:""}]},	
	 		{items:[{name:"#L.CreationDate",       type:"LABEL",  value:"填單日期"}]},
	 		{items:[{name:"#F.creationDate",       type:"TEXT",   bind:"creationDate",       size:12, mode:"READONLY" }]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.purchaseOrderDate",  type:"LABEL",  value:"採購日期<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.purchaseOrderDate",  type:"DATE",   bind:"purchaseOrderDate",  size:1 }]},
	 		{items:[{name:"#L.purchaseType",       type:"LABEL",  value:"採購類型<font color='red'>*</font>"}]},	 
	 		{items:[{name:"#F.purchaseType",       type:"SELECT", bind:"purchaseType",       size:12}], td:" colSpan=5"}]}
	 		],
	 beginService:"",
	 closeService:""			
	});
}

/* 畫出單身與定義單身欄位 */
function masterInitial(){
	var isPartialShipments = [[true, true], ["是", "否"], ["Y", "N"]];
	var branchCode      = vat.bean("branchCode");
	var budgetType      = vat.bean("budgetType");		// 預算扣除方式 P:UnitPrice/C:Cost/T:整筆扣除
	var budgetCheckType = vat.bean("budgetCheckType");	// 預算扣除類別 Y:year/M:month
	var brandCode       = vat.item.getValueByName("#F.brandCode");
	var categoryFibudgetLine 	= vat.bean("categoryFibudgetLine");
	var itemBrandCode   = vat.bean("itemBrandCode");
	var allBudgetYearList = vat.bean("allBudgetYearList");
	var allBudgetMonthList = vat.bean("allBudgetMonthList");
	var allFibudgetLineInit = vat.bean("allFibudgetLineInit");
	var allItemCategory = vat.bean("allItemCategory");     
	var styleT1 = "";
	var styleT2 = " style='display:none'";
	var styleforT1 = brandCode != "T2"?" style='display:none'":"" ;
	var quotationCodeL = "廠商報價單號";
	var reserve1L      = "國外訂單單號";
	if ( branchCode=="2" ){
		styleT1 = " style='display:none'";
		styleT2 = "";
		quotationCodeL = "PI No.";
		reserve1L      = "PO No.";
	}
	
	var isTotalBudget = "HIDDEN";
	if( budgetType=="T" ){
		isTotalBudget = "";
	}
	
	var isMonthBudget = "READONLY";
	if(budgetCheckType=="M"){
		isMonthBudget = "";
	}			
	vat.block.create(vatMasterDiv, {
		id: "vatMasterDiv",generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.invoiceTypeCode", type:"LABEL",  value:"發票類型"}]},	 
	 		{items:[{name:"#F.invoiceTypeCode", type:"SELECT", bind:"invoiceTypeCode", size:12, mode:"READONLY" }]},		 
	 		{items:[{name:"#L.taxType",         type:"LABEL",  value:"稅別"}]},	 
			{items:[{name:"#F.taxType",         type:"SELECT", bind:"taxType",         size:12,     mode:"READONLY" }]},
			{items:[{name:"#L.taxRate",         type:"LABEL",  value:"稅率"}]},	 
	 		{items:[{name:"#F.taxRate",         type:"NUMM",   bind:"taxRate",         size:12, dec:4 }]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.countryCode",     type:"LABEL",  value:"國別"}]},	 
	 		{items:[{name:"#F.countryCode",     type:"SELECT", bind:"countryCode",    size:12}]},		 
	 		{items:[{name:"#L.currencyCode",    type:"LABEL",  value:"幣別"}]},	 
	 		{items:[{name:"#F.currencyCode",    type:"SELECT", bind:"currencyCode",   size:12, onchange:"changeCurrencyCode()"}]},
	 		{items:[{name:"#L.exchangeRate",    type:"LABEL",  value:"匯率"}]},	 
	 		{items:[{name:"#F.exchangeRate",    type:"NUMM",   bind:"exchangeRate",   size:12, dec:4 },
	 		 		{name:"#L.currencyName",    type:"LABEL",  value:"台幣"}]}]},
		 {row_style:"", cols:[
	 		{items:[{name:"#L.quotationCode",   type:"LABEL",  value:quotationCodeL}]},	 
	 		{items:[{name:"#F.quotationCode",   type:"TEXT",   bind:"quotationCode",   size:30 }]},		 
	 		{items:[{name:"#L.contactPerson",   type:"LABEL",  value:"廠商聯絡窗口"}]},	 
	 		{items:[{name:"#F.contactPerson",   type:"TEXT",   bind:"contactPerson",   size:30 }]},
	 		{items:[{name:"#L.paymentTermCode", type:"LABEL",  value:"付款條件"}]},	 
	 		{items:[{name:"#F.paymentTermCode", type:"SELECT", bind:"paymentTermCode", size:12}]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.defaultWarehouseCode", type:"LABEL",  value:"入庫庫別"}]},	 
	 		{items:[{name:"#F.defaultWarehouseCode", type:"SELECT", bind:"defaultWarehouseCode", size:12}]},		 
	 		{items:[{name:"#L.isPartialShipment",    type:"LABEL",  value:"分批到貨"}]},	 //到貨方式
	 		{items:[{name:"#F.isPartialShipment",    type:"SELECT", bind:"isPartialShipment", size:1,  init:isPartialShipments }]},
	 		{items:[{name:"#L.paymentTermCode1",     type:"LABEL",  value:"付款條件二"}]},	 
	 		{items:[{name:"#F.paymentTermCode1",     type:"SELECT", bind:"paymentTermCode1", size:12}]}]},
		{row_style:"", cols:[
	 		{items:[{name:"#L.budgetYear",          type:"LABEL",  value:"預算年月<font color='red'>$</font>"}],td:"style='background-color:yellow; border-color:yellow; border-width: 1px;'"},	 
	 		{items:[{name:"#F.budgetYear",          type:"SELECT", bind:"budgetYear",init:allBudgetYearList,  size:8 , onchange:"changeCategoryType()"},
	 				{name:"#L.Year",                type:"LABEL",  value:" 年 "},
	 		 		{name:"#F.budgetMonth",         type:"SELECT", bind:"budgetMonth",init:allBudgetMonthList, size:4 , onchange:"changeCategoryType()" },
	 		 		{name:"#L.Month",               type:"LABEL",  value:" 月 "}]},
	 		{items:[{name:"#L.poOrderNo",            type:"LABEL",  value:reserve1L}]},	 
	 		{items:[{name:"#F.poOrderNo",            type:"TEXT",   bind:"poOrderNo",   size:30}]},
	 		{items:[{name:"#L.scheduleReceiptDate", type:"LABEL",  value:"預計到貨日"}]},	 
	 		{items:[{name:"#F.scheduleReceiptDate", type:"DATE",   bind:"scheduleReceiptDate", size:12}]}]},
		{row_style:"", cols:[
			{items:[{name:"#L.categoryType",  type:"LABEL",  value:"業種子類<font color='red'>$</font>"}],td:"style='background-color:yellow; border-color:red; border-width: 1px;'"},	 
	 		{items:[{name:"#F.categoryType",  type:"SELECT", bind:"categoryType",init:allItemCategory,  size:12, onchange:"changeCategoryType()"}]},
	 		{items:[{name:"#L.itemBrandCode",  type:"LABEL",  value:"商品品牌<font color='red'>$</font>"}],td:"style='background-color:yellow; border-color:yellow; border-width: 1px;'"},	 
	 		{items:[{name:"#F.itemBrandCode",  type:"SELECT", bind:"budgetLineId",init:allFibudgetLineInit,  size:12 ,mode:styleforT1, onchange:"changeCategory01()" },
	 				{name:"#L.lotControl",	type:"LABEL", 	value:"<font color='red'>※黃色部分為影響預算</font>"},
				    {name:"#F.lotControl",	type:"SELECT", bind:"lotControl",mode:"HIDDEN"}],td:"colSpan=3"}]},		 	
	 		//{items:[{name:"#L.asignedBudget", type:"LABEL",  value:"指定預算"}]},	 
	 		//{items:[{name:"#F.asignedBudget", type:"NUMM",   bind:"asignedBudget", size:12, mode:isTotalBudget ,mode:"HIDDEN"}], td:" colSpan=1"}]},
	 	{row_style:"", cols:[			
	 		{items:[{name:"#L.category01",  type:"LABEL",  value:"大類<font color='red'>$</font>"}],td:"style='background-color:yellow; border-color:red; border-width: 1px;'"},	 
	 		{items:[{name:"#F.category01",  type:"SELECT", bind:"category01",init:categoryFibudgetLine, size:12 ,mode:styleforT1 }]},
	 		{items:[{name:"#L.asignedBudget", type:"LABEL",  value:"指定預算"}]},	 
	 		{items:[{name:"#F.asignedBudget", type:"NUMM",   bind:"asignedBudget", size:12, mode:isTotalBudget ,mode:"HIDDEN"}], td:" colSpan=1"}]},
	 		//{items:[{name:"#L.category07",  type:"LABEL",  value:"中類<font color='red'>$</font>"}]},	 
	 		//{items:[{name:"#F.category07",  type:"SELECT", bind:"category07",  size:12 ,mode:styleforT1 }],td:" colSpan=3"}]},
		 {row_style:styleT2, cols:[
	 		{items:[{name:"#L.purchaseAssist", type:"LABEL",  value:"採購助理"}]},	 
	 		{items:[{name:"#F.purchaseAssist", type:"SELECT", bind:"purchaseAssist", size:12}]},
	 		{items:[{name:"#L.purchaseMember", type:"LABEL",  value:"採購人員"}]},	 
	 		{items:[{name:"#F.purchaseMember", type:"SELECT", bind:"purchaseMember", size:12, onchange:"changePurchaseMember()" }]},
	 		{items:[{name:"#L.purchaseMaster", type:"LABEL",  value:"採購主管"}]},	 
			{items:[{name:"#F.purchaseMaster", type:"SELECT", bind:"purchaseMaster", size:12}]}]},
		{row_style:"", cols:[
	 		{items:[{name:"#L.reserve1",       type:"LABEL", value:"備註一"}]},
	 		{items:[{name:"#F.reserve1",       type:"TEXT",  bind:"reserve1",	size:85, maxLen:100 }], td:" colSpan=2"},
			{items:[{name:"#L.reserve2",       type:"LABEL", value:"備註二"}]},
	 		{items:[{name:"#F.reserve2",       type:"TEXT",   bind:"reserve2",	size:85, maxLen:100 }], td:" colSpan=2"}]},
		{row_style:"", cols:[
	 		{items:[{name:"#L.tradeTermCode",  type:"LABEL",  value:"價格條件"}]},
	 		{items:[{name:"#F.tradeTermCode",  type:"TEXT",   bind:"tradeTermCode",	size:85, maxLen:100 }], td:" colSpan=2"},
			{items:[{name:"#L.packaging",      type:"LABEL",  value:"包裝方式"}]},
	 		{items:[{name:"#F.packaging",      type:"TEXT",   bind:"packaging",	size:85, maxLen:100 }], td:" colSpan=2"}]},
	 	{row_style:"" , cols:[
	 		{items:[{name:"#L.sourceOrderNo",	type:"LABEL",  value:"來源單號"}]},	 
	 		{items:[{name:"#F.sourceOrderNo",	type:"TEXT",   bind:"sourceOrderNo",	size:85, maxLen:100 }], td:" colSpan=2"},
 	 		{items:[{name:"#L.salesPeriod",	type:"LABEL",  value:"銷售期間"}]},//新增銷售區間-Jerome	 
	 		{items:[{name:"#F.salesPeriod",	type:"TEXT",   bind:"salesPeriod",	size:85, maxLen:100 }], td:" colSpan=2"}]}
 	 	],
		beginService:"",
		closeService:""			
	});
}

/* 畫出明細與定義明細欄位 */
function detailInitial() {
	var statusTmp  = vat.item.getValueByName("#F.status");
	var branchCode = vat.bean("branchCode");
	
	var CanGridDelete = true;
	var CanGridAppend = true;
	var CanGridModify = true;		
	
	vat.item.make(vatDetailDiv, "indexNo",                 {type:"IDX",  view:"fixed", desc:"序號"});
	vat.item.make(vatDetailDiv, "category02",              {type:"TEXT", view:"", 	size:4, desc:"中類", mode:branchCode == "2"?"READONLY":"HIDDEN"});      
	vat.item.make(vatDetailDiv, "category02Name",          {type:"TEXT", view:"", 	size:6, desc:"中類名稱", mode:branchCode == "2"?"READONLY":"HIDDEN"});                                                            
	vat.item.make(vatDetailDiv, "itemCode",                {type:"TEXT", view:"fixed",   size:16, desc:"品號",	onchange:"onChangeItemCode()"});      
	vat.item.make(vatDetailDiv, "itemCName",               {type:"TEXT", view:"",   size:20, desc:"品名",	mode:"READONLY"});                    
	vat.item.make(vatDetailDiv, "stockOnHandQty",          {type:"NUMM", view:"",   size:4, desc:"庫存量",	mode:"READONLY", dec:0});                 
	vat.item.make(vatDetailDiv, "unitPrice",               {type:"NUMM", view:"",   size:8, desc:"零售價",	mode:"READONLY", dec:2});  
	//vat.item.make(vatDetailDiv, "lastForeignUnitCost", 	   {type:"NUMM", view:"shift", size:10,desc:"上次外幣進貨價", mode:"READONLY", dec:6});
	vat.item.make(vatDetailDiv, "lastLocalUnitCost",   	   {type:"NUMM", view:"",   size:6, desc:"進貨成本", mode:"READONLY", dec:2});                        
	vat.item.make(vatDetailDiv, "quantity",                {type:"NUMM", view:"",   size:4,	desc:"數量",		onchange:"calculateLineAmount('1')", dec:0});    
	vat.item.make(vatDetailDiv, "foreignUnitCost",         {type:"NUMM", view:"",	size:6, desc:"原幣單價",	onchange:"calculateLineAmount('2')", dec:6});
	vat.item.make(vatDetailDiv, "localUnitCost",		   {type:"NUMM", view:"",	size:6, desc:"台幣單價",	mode:"READONLY", dec:6});
	vat.item.make(vatDetailDiv, "foreignPurchaseAmount",   {type:"NUMM", view:"shift", 	size:8, desc:"原幣總價",	mode:"READONLY", dec:2});
	vat.item.make(vatDetailDiv, "localPurchaseAmount",     {type:"NUMM", view:"shift",	size:8, desc:"台幣總價",	mode:"READONLY", dec:2}); 	                  
	vat.item.make(vatDetailDiv, "unitPriceAmount",         {type:"NUMM", view:"shift", size:8, desc:"零售總價",	mode:"READONLY", dec:2});
	vat.item.make(vatDetailDiv, "scheduleReceiptDate",     {type:"DATE", view:"shift", size:6, desc:"修改到貨日", mode:branchCode == "2"?"HIDDEN":""});
	vat.item.make(vatDetailDiv, "actualPurchaseQuantity",  {type:"NUMM", view:"shift", size:4, desc:"最終採購量",	mode:"READONLY", dec:0});
	vat.item.make(vatDetailDiv, "receiptedQuantity",  	   {type:"NUMM", view:"shift", size:4, desc:"已到量",	mode:"READONLY", dec:0});
	vat.item.make(vatDetailDiv, "outstandQuantity",  	   {type:"NUMM", view:"shift", size:4, desc:"未交量",	mode:"READONLY", dec:0});
	vat.item.make(vatDetailDiv, "outstandAmount",		   {type:"NUMM", view:"shift", size:8, desc:"未交金額",	mode:"READONLY"});
	vat.item.make(vatDetailDiv, "returnedQuantity",  	   {type:"NUMM", view:"shift", size:4, desc:"已退貨量",	mode:"READONLY", dec:0});
	vat.item.make(vatDetailDiv, "returnedAmount",		   {type:"NUMM", view:"shift", size:8, desc:"已退貨金額",	mode:"READONLY"});
	vat.item.make(vatDetailDiv, "purchaseUnit",  		   {type:"TEXT", view:"shift", size:4, desc:"單位", mode:branchCode == "2"?"READONLY":"HIDDEN"});
	vat.item.make(vatDetailDiv, "itemBrand",           	   {type:"TEXT", view:"shift", size:6, desc:"商品品牌", mode:branchCode == "2"?"READONLY":"HIDDEN"});
	vat.item.make(vatDetailDiv, "supplierItemCode",    	   {type:"TEXT", view:"shift", size:16,desc:"廠商貨號", mode:branchCode == "2"?"READONLY":"HIDDEN"});
	vat.item.make(vatDetailDiv, "margin",              	   {type:"NUMM", view:"shift", size:6, desc:"毛利率", mode:branchCode == "2"?"READONLY":"HIDDEN", dec:2});
	vat.item.make(vatDetailDiv, "nextPriceAdjustDate", 	   {type:"DATE", view:"shift", size:8, desc:"下次變價日", mode:branchCode == "2"?"READONLY":"HIDDEN"});
	vat.item.make(vatDetailDiv, "nextAdjustPrice", 		   {type:"NUMM", view:"shift", size:8, desc:"下次變價價格", mode:branchCode == "2"?"READONLY":"HIDDEN", dec:0});
	vat.item.make(vatDetailDiv, "minPurchaseQuantity",     {type:"NUMM", view:"shift", size:6, desc:"最低採購數量", mode:branchCode == "2"?"READONLY":"HIDDEN"});
	vat.item.make(vatDetailDiv, "maxPurchaseQuantity",     {type:"NUMM", view:"shift", size:6, desc:"最高採購數量", mode:branchCode == "2"?"READONLY":"HIDDEN"});
	vat.item.make(vatDetailDiv, "remark",              	   {type:"TEXT", view:"shift", size:40,desc:"備註"});
	vat.item.make(vatDetailDiv, "lineId",                  {type:"ROWID",view:"none"});              
	vat.item.make(vatDetailDiv, "isDeleteRecord",          {type:"DEL",	 view:"fixed", desc:"刪除"});
	vat.item.make(vatDetailDiv, "detailClose",             {type:"BUTTON",	view:"shift", size:30, desc:"明細結案", value:"明細結案", eClick:"doDetailClose()", mode:branchCode == "2"?"HIDDEN":""});
	vat.block.pageLayout( vatDetailDiv, {
			id: "vatDetailDiv",
			pageSize:10, 
			OptselectAll:true,
		    //gridOverflow: "scroll",         //** 設定 Data Grid 有 scroll bar
		    canGridDelete:CanGridDelete,
			canGridAppend:CanGridAppend,
			canGridModify:CanGridModify,
			appendBeforeService :"appendBeforeService()",
			loadBeforeAjxService:"loadBeforeAjxService()", 
			saveBeforeAjxService:"saveBeforeAjxService()", 
			saveSuccessAfter    :"saveSuccessAfter()", 
			saveFailureAfter    :""});
			
	vat.block.pageDataLoad( vatDetailDiv, vnCurrentPage = 1);
}

/* 畫出金額統計與定義金額統計欄位 */
function amountInitial(){
	vat.block.create(vatAmountDiv, {
		id: "vatAmountDiv", generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.totalForeignPurchaseAmount", type:"LABEL", value:"採購金額(原幣)"}]},
	 		{items:[{name:"#L.localSalesAmount",           type:"LABEL", value:"採購金額(NT$)"}]},		 
	 		{items:[{name:"#L.taxAmount",                  type:"LABEL", value:"稅額"}]},
	 		{items:[{name:"#L.totalUnitPriceAmount",       type:"LABEL", value:"零售價總額"}]}]},	 
	 	{row_style:"", cols:[
			{items:[{name:"#F.totalForeignPurchaseAmount", type:"NUMM",  bind:"totalForeignPurchaseAmount", size:20, mode:"READONLY", dec:4}]},
	 		{items:[{name:"#F.totalLocalPurchaseAmount",   type:"NUMM",  bind:"totalLocalPurchaseAmount",   size:20, mode:"READONLY", dec:2}]},	 
	 		{items:[{name:"#F.taxAmount",                  type:"NUMM",  bind:"taxAmount", 					size:20, mode:"READONLY", dec:2}]},
	 		{items:[{name:"#F.totalUnitPriceAmount",       type:"NUMM",  bind:"totalUnitPriceAmount", 		size:20, mode:"READONLY", dec:2}]}]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.totalProductCounts",         type:"LABEL", value:"本次採購數量"}]},
	 		{items:[{name:"#L.curBudgetYear",              type:"LABEL", value:"總預算年度"}]},
	 		{items:[{name:"#L.curBudgetMonth",             type:"LABEL", value:"總預算月份"}]},
	 		{items:[{name:"#L.totalBudget",                type:"LABEL", value:"總預算金額"}
	 			    /*{name:"#L.itemBrandCode",              type:"LABEL", value:"<font color='red'>(※商品品牌※)</font>"},*/
	 				/*{name:"#F.itemBrandCode",              type:"SELECT",bind:"budgetLineId",size:20, mode:"READONLY"}*/]}
	 		]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#F.totalProductCounts",         type:"NUMM",  bind:"totalProductCounts",   size:20, mode:"READONLY"}]},
	 		{items:[{name:"#F.budgetYear1",                type:"NUMB",  bind:"budgetYear1",          size:20, mode:"READONLY", dec:0, back:false}]},
	 		{items:[{name:"#F.budgetmonth1",               type:"NUMM",  bind:"budgetmonth1",         size:20, mode:"READONLY", dec:0, back:false}]},
	 		{items:[{name:"#F.totalBudget",                type:"NUMM",  bind:"totalBudget",          size:20, mode:"READONLY", dec:2}]}
	 		]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.totalUsedPriceAmount",       type:"LABEL", value:"本次使用預算"}]},
	 		{items:[{name:"#L.totalReceiptedAmount",       type:"LABEL",  value:"本次進貨金額"}]},
	 		{items:[{name:"#L.totalOutstandAmount",		   type:"LABEL",  value:"本次未交金額"}]},
	 		{items:[{name:"#L.totalReturnedAmount",		   type:"LABEL",  value:"本次已退金額"}]}
	 		]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#F.totalUsedPriceAmount",       type:"NUMM",  bind:"totalUsedPriceAmount", size:20, mode:"READONLY", dec:2}]},
	 		{items:[{name:"#F.totalReceiptedAmount",       type:"NUMM",  bind:"totalReceiptedAmount", size:20, mode:"READONLY", dec:2}]},
	 		{items:[{name:"#F.totalOutstandAmount",        type:"NUMM",  bind:"totalOutstandAmount",  size:20, mode:"READONLY", dec:2}]},
	 		{items:[{name:"#F.totalReturnedAmount",        type:"NUMM",  bind:"totalReturnedAmount",  size:20, mode:"READONLY", dec:2}]}
	 		]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.totalAppliedBudget",         type:"LABEL", value:"已採購預算"}]},
			{items:[{name:"#L.totalSigningBudget",         type:"LABEL", value:"簽核中預算"}]},
			{items:[{name:"#L.totalReturnedBudget",        type:"LABEL", value:"已退回預算"}]},
	 		{items:[{name:"#L.totalRemainderBudget",       type:"LABEL", value:"剩餘預算"}]}]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#F.totalAppliedBudget",         type:"NUMM",  bind:"totalAppliedBudget",   size:20, mode:"READONLY", dec:2}]},		 	 
	 		{items:[{name:"#F.totalSigningBudget",         type:"NUMM",  bind:"totalSigningBudget",   size:20, mode:"READONLY", dec:2}]},
	 		{items:[{name:"#F.totalReturnedBudget",		   type:"NUMM",  bind:"totalReturnedBudget",   size:20, mode:"READONLY", dec:2}]},	 
	 		{items:[{name:"#F.totalRemainderBudget",       type:"NUMM",  bind:"totalRemainderBudget", size:20, mode:"READONLY", dec:2}]}]}
 	 	],
		beginService:"",
		closeService:function(){closeAmount();}
	});
}

/* 第二次詳細資料初始化 */
function closeAmount(){
	vat.ajax.XHRequest({ 
	post:"process_object_name=poPurchaseOrderHeadMainService"+
          		"&process_object_method_name=findInitialCommon"+
          		"&headId=" + vat.item.getValueByName("#F.headId")+
          		 	"&categoryType=" + vat.item.getValueByName("#F.categoryType")+                    
                    "&budgetYear=" + vat.item.getValueByName("#F.budgetYear")+
                    "&budgetMonth=" + vat.item.getValueByName("#F.budgetMonth"),
          asyn:false,                      
	find: function change(oXHR){
		vat.item.SelectBind(eval(vat.ajax.getValue("allCreatedBy"		, oXHR.responseText)),{ itemName : "#F.createdBy" });		
		//vat.item.SelectBind(eval(vat.ajax.getValue("allBudgetYearList"	, oXHR.responseText)),{ itemName : "#F.budgetYear" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPurchaseAssist"	, oXHR.responseText)),{ itemName : "#F.purchaseAssist" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPurchaseMember"	, oXHR.responseText)),{ itemName : "#F.purchaseMember" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPurchaseMaster"	, oXHR.responseText)),{ itemName : "#F.purchaseMaster" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allWarehouse"		, oXHR.responseText)),{ itemName : "#F.defaultWarehouseCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentTerm"		, oXHR.responseText)),{ itemName : "#F.paymentTermCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPaymentTerm"		, oXHR.responseText)),{ itemName : "#F.paymentTermCode1" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allCountryCode"		, oXHR.responseText)),{ itemName : "#F.countryCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allCurrencyCode"	, oXHR.responseText)),{ itemName : "#F.currencyCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allInvoiceCode"		, oXHR.responseText)),{ itemName : "#F.invoiceTypeCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allTaxType"			, oXHR.responseText)),{ itemName : "#F.taxType" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allPurchaseType"	, oXHR.responseText)),{ itemName : "#F.purchaseType" });
		//vat.item.SelectBind(eval(vat.ajax.getValue("allBudgetYearList"	, oXHR.responseText)),{ itemName : "#F.budgetYear" });
		//vat.item.SelectBind(eval(vat.ajax.getValue("allBudgetMonthList"	, oXHR.responseText)),{ itemName : "#F.budgetMonth" });
		//vat.item.SelectBind(eval(vat.ajax.getValue("allItemCategory"	, oXHR.responseText)),{ itemName : "#F.categoryType" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allOrderTypes"		, oXHR.responseText)),{ itemName : "#F.orderTypeCode" });		
		//vat.item.SelectBind(eval(vat.ajax.getValue("allFibudgetLine"	, oXHR.responseText)),{ itemName : "#F.itemBrandCode" });
		//vat.item.SelectBind(eval(vat.ajax.getValue("categoryFibudgetLine", oXHR.responseText)),{ itemName : "#F.category01" });
		//vat.item.setValueByName("#F.itemBrandCode",    vat.ajax.getValue("allFibudgetLine",    oXHR.responseText));
		//vat.item.SelectBind(eval(vat.ajax.getValue("allFibudgetLine"	, oXHR.responseText)),{ itemName : "#F.itemBrand" });				
			var categoryType = vat.item.getValueByName("#F.categoryType")
			var year = vat.item.getValueByName("#F.budgetYear")
			var moth = vat.item.getValueByName("#F.budgetMonth")
			//alert(F.itemBrandCode);
				 if(categoryType == vat.ajax.getValue("allItemCategory",    oXHR.responseText)){						 				
					vat.item.setValueByName("#F.categoryType",    vat.ajax.getValue("allItemCategory",    oXHR.responseText));					
					//changeCategoryType();
					}
								
				//if(itemBrandCode = vat.ajax.getValue("allFibudgetLine"	, oXHR.responseText) !== null){					
					//changeCategoryType();
					//alert("請確定:");
				//}
				//alert(vat.item.getValueByName("#F.itemBrandCode"));
				//alert("請確定現在預算條件是否為 預算年:"+vat.item.getValueByName("#F.budgetYear")+" 預算月:"+vat.item.getValueByName("#F.budgetMonth")+" 業種子類:"+vat.item.getValueByName("#F.categoryType")+" 商品品牌:"+vat.item.getValueByName("#F.itemBrandCode"));		
				//alert(vat.item.getValueByName("#F.categoryType"));
				//if(null != itemBrandCode /*== vat.ajax.getValue("allFibudgetLine", oXHR.responseText)*/){
				//	vat.item.setValueByName("#F.itemBrandCode",    vat.ajax.getValue("allFibudgetLine",    oXHR.responseText));						
				//	}
									
		vat.item.bindAll();
		doFormAccessControl();
			
		}
	});
}



/* 明細商品品號異動更新 */
function onChangeItemCode() {
	var nItemLine = vat.item.getGridLine();
	var sItemCode = vat.item.getGridValueByName("itemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setGridValueByName("itemCode", nItemLine, sItemCode);
	
	if (vat.item.getGridValueByName("quantity",nItemLine)=="undefined"){
		vat.item.setGridValueByName("quantity",nItemLine,0);
	}
	if (vat.item.getGridValueByName("foreignPurchaseAmount", nItemLine)=="undefined"){
		vat.item.setGridValueByName("foreignPurchaseAmount", nItemLine,0);
	}
	if (vat.item.getGridValueByName("unitPriceAmount", nItemLine)=="undefined"){
		vat.item.setGridValueByName("unitPriceAmount", nItemLine,0);
	}
	if (sItemCode != "") {
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXLineData" + 
							"&branchCode="        + vat.bean("branchCode")+
							"&brandCode="         + vat.item.getValueByName("#F.brandCode") + 
							"&orderTypeCode="     + vat.item.getValueByName("#F.orderTypeCode") +
							"&itemCode="          + sItemCode +
							"&purchaseOrderDate=" + vat.item.getValueByName("#F.purchaseOrderDate") +
							"&exchangeRate=" + vat.item.getValueByName("#F.exchangeRate") +
							"&quantity=" + vat.item.getGridValueByName("quantity",nItemLine);					   
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					if(vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText) != '查無資料'){
						vat.item.setGridValueByName("unitPrice",           	nItemLine, vat.ajax.getValue("UnitPrice", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("foreignUnitCost",     	nItemLine, vat.ajax.getValue("ForeignUnitCost", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("localUnitCost",     	nItemLine, vat.ajax.getValue("LocalUnitCost", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("foreignPurchaseAmount",nItemLine, vat.ajax.getValue("ForeignPurchaseAmount", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("localPurchaseAmount",	nItemLine, vat.ajax.getValue("LocalPurchaseAmount", vat.ajax.xmlHttp.responseText));
						//vat.item.setGridValueByName("lastForeignUnitCost", 	nItemLine, vat.ajax.getValue("LastForeignUnitCost", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("lastLocalUnitCost",   	nItemLine, vat.ajax.getValue("LastLocalUnitCost", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("itemCName",           	nItemLine, vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("stockOnHandQty",      	nItemLine, vat.ajax.getValue("StockOnHandQty", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("itemBrand",           	nItemLine, vat.ajax.getValue("ItemBrand", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("supplierItemCode",    	nItemLine, vat.ajax.getValue("SupplierItemCode", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("margin",              	nItemLine, vat.ajax.getValue("Margin", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("maxPurchaseQuantity", 	nItemLine, vat.ajax.getValue("MaxPurchaseQty", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("minPurchaseQuantity", 	nItemLine, vat.ajax.getValue("MinPurchaseQty", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("nextPriceAdjustDate", 	nItemLine, vat.ajax.getValue("NextPriceAdjustDate", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("nextAdjustPrice", 		nItemLine, vat.ajax.getValue("NextAdjustPrice", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("category02", 			nItemLine, vat.ajax.getValue("Category02", vat.ajax.xmlHttp.responseText));
						vat.item.setGridValueByName("category02Name", 		nItemLine, vat.ajax.getValue("Category02Name", vat.ajax.xmlHttp.responseText));
					}else{
						alert('查無資料，請重新輸入');
						vat.item.setGridValueByName("itemCode", nItemLine, '');
					}					
				}
			if(nItemLine == "1" && vat.block.pageThere(vatDetailDiv) == "1")
				changeSupplierCode();
			}
		});
	}
}


/* 計算 單筆 LINE 合計的部份 */
function calculateLineAmount( chgType ) {
	var exchangeRate          = vat.item.getValueByName("#F.exchangeRate");
	var nItemLine             = vat.item.getGridLine();
	var quantity              = vat.item.getGridValueByName("quantity",nItemLine);
	if (vat.item.getGridValueByName("quantity",nItemLine)==""){
		quantity = 0;
	}
	var foreignUnitCost       = vat.item.getGridValueByName("foreignUnitCost",nItemLine);
	var localUnitCost         = (parseFloat(foreignUnitCost) * parseFloat(exchangeRate)).toFixed(2);
	var unitPrice             = vat.item.getGridValueByName("unitPrice",nItemLine);
	var foreignPurchaseAmount = (parseFloat(quantity) * parseFloat(foreignUnitCost)).toFixed(2);;
	var localPurchaseAmount   = (parseFloat(quantity) * parseFloat(localUnitCost)).toFixed(2);;
	var unitPriceAmount       = parseFloat(quantity) * parseFloat(unitPrice);
	vat.item.setGridValueByName("quantity",				nItemLine, quantity);
	vat.item.setGridValueByName("foreignPurchaseAmount",nItemLine, foreignPurchaseAmount);
	vat.item.setGridValueByName("localUnitCost", 		nItemLine, localUnitCost);
	vat.item.setGridValueByName("localPurchaseAmount", 	nItemLine, localPurchaseAmount);
	vat.item.setGridValueByName("unitPriceAmount",		nItemLine, unitPriceAmount);
	if (chgType == "1")  {
		vat.item.setGridValueByName("actualPurchaseQuantity", nItemLine, quantity);
	}
	var receiptedQuantity = vat.item.getGridValueByName("receiptedQuantity",nItemLine);
	if (receiptedQuantity==""){
		receiptedQuantity = 0;
		vat.item.setGridValueByName("receiptedQuantity", nItemLine, receiptedQuantity);
	}
	var outstandAmount = localPurchaseAmount * (quantity - receiptedQuantity);
	vat.item.setGridValueByName("outstandAmount", nItemLine, outstandAmount);
	vat.item.setGridValueByName("outstandQuantity", nItemLine, vat.item.getGridValueByName("actualPurchaseQuantity",nItemLine));
}


/* 判斷是否要關閉LINE */
function checkEnableLine() {
	return true ;
}

/* 開放新增明細 */
function appendBeforeService(){
	return true;
}

/* 載入LINE資料 */
function loadBeforeAjxService() {
	//alert("loadBeforeAjxService");
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXPageData" +
						"&headId=" + vat.item.getValueByName("#F.headId") +
						"&status=" + vat.item.getValueByName("#F.status")  + 
						"&exchangeRate="  + vat.item.getValueByName("#F.exchangeRate");
	return processString;
}

/* 取得SAVE要執行的JS FUNCTION */
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
	var processString = "";
	var exchangeRate = 0;
	//if (checkEnableLine()) {
		processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=updateAJAXPageLinesData" + 
						"&headId="        + vat.item.getValueByName("#F.headId") + 
						"&status="        + vat.item.getValueByName("#F.status") + 
						"&brandCode="     + vat.item.getValueByName("#F.brandCode")  + 
						"&exchangeRate="  + vat.item.getValueByName("#F.exchangeRate") + 
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") ;
						vat.ajax.startRequest(processString, function (){
												
			});
	//}
	return processString;
}


/* 按下單據送出 */
function doSubmit(formAction){
	//alert("doSubmit " + formAction);
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){		
	    alertMessage = "是否確定送出?";	    					
	}else if ("SUBMIT_BG" == formAction){
	 	alertMessage = "是否確定背景送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("EXPORT" == formAction){
	 	alertMessage = "是否確定匯出作業?";
	}else if ("IMPORT" == formAction){
	 	alertMessage = "是否確定匯入作業?";
	}else if ("EXTEND" == formAction){
	 	alertMessage = "是否執行取報單程序?";
	}else if ("CLOSE" == formAction){
	 	alertMessage = "是否確定採購單結案?";
	}
		
	if(confirm(alertMessage)){
	    if("SAVE" == formAction){
	    	afterSavePageProcess = "SAVE";
            
        }else if("SUBMIT" == formAction){
        	afterSavePageProcess = "SUBMIT";
            
        }else if("SUBMIT_BG" == formAction){
        	afterSavePageProcess = "SUBMIT_BG";
            
        }else if("VOID" == formAction){
        	afterSavePageProcess = "VOID";
        	
        }else if("EXPORT" == formAction){
        	afterSavePageProcess = "EXPORT";
        	
        }else if("IMPORT" == formAction){
        	afterSavePageProcess = "IMPORT";
        	
        }else if ("EXTEND" == formAction){
        	afterSavePageProcess = "EXTEND"; 
        
        }else if("CLOSE" == formAction){
        	afterSavePageProcess = "CLOSE";
            
        }
        //alert("doSubmit afterSavePageProcess "+ afterSavePageProcess);
        vat.block.pageSearch(3);	// save & refresh PoPurchaseOrderLine Page / after save success call saveSuccessAfter()
	}
}


/* 取得存檔成功後要執行的JS FUNCTION */
function saveSuccessAfter() {
	//alert("saveSuccessAfter ->" + afterSavePageProcess );
	var status               = vat.item.getValueByName("#F.status");	
	
		if( "SAVE" == afterSavePageProcess ) {
			execSubmitAction("SAVE");
			
		}else if( "SUBMIT" == afterSavePageProcess ){	
			execSubmitAction("SUBMIT");		
				
		}else if( "SUBMIT_BG" == afterSavePageProcess ){
			execSubmitAction("SUBMIT_BG");
			
		}else if( "VOID" == afterSavePageProcess ){
			execSubmitAction("VOID");
		
		} else if ("EXPORT" == afterSavePageProcess) {
			exportFormData();

		}else if ("IMPORT" == afterSavePageProcess) {
			importFormData();
			
		} else if ("totalCount" == afterSavePageProcess){
		//alert("count"+vat.item.getValueByName("#F.budgetYear"));
			var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=updateAJAXHeadTotalAmount" + 
								"&headId="       + vat.item.getValueByName("#F.headId") + 
								"&taxRate="      + vat.item.getValueByName("#F.taxRate") +
								"&exchangeRate=" + vat.item.getValueByName("#F.exchangeRate") + 
								"&purchaseType=" + vat.item.getValueByName("#F.purchaseType") +
								"&budgetYear="   + vat.item.getValueByName("#F.budgetYear") +
								"&budgetMonth="  + vat.item.getValueByName("#F.budgetMonth") +
								"&category01="  + vat.item.getValueByName("#F.category01") +//加入大類
								"&itemBrandCode="  + vat.item.getValueByName("#F.itemBrandCode") +//加入品牌
								"&categoryType=" + vat.item.getValueByName("#F.categoryType") ;
			vat.ajax.startRequest(processString, function (){
				if (vat.ajax.handleState()) {
					var exchangeRate = parseInt(vat.item.getValueByName("#F.exchangeRate"),10);
					if(isNaN(exchangeRate))
						exchangeRate = 1;
					vat.item.setValueByName("#F.totalLocalPurchaseAmount", vat.ajax.getValue("TotalLocalPurchaseAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalForeignPurchaseAmount", vat.ajax.getValue("TotalForeignPurchaseAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.taxAmount", vat.ajax.getValue("TaxAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalReceiptedAmount", vat.ajax.getValue("TotalReceiptedAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalOutstandAmount", vat.ajax.getValue("TotalOutstandAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalReturnedAmount", vat.ajax.getValue("TotalReturnedAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalProductCounts", vat.ajax.getValue("TotalProductCounts",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalBudget", vat.ajax.getValue("TotalBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalAppliedBudget", vat.ajax.getValue("TotalAppliedBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalRemainderBudget", vat.ajax.getValue("TotalRemainderBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalSigningBudget", vat.ajax.getValue("TotalSigningBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalReturnedBudget", vat.ajax.getValue("TotalReturnedBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalUnitPriceAmount", vat.ajax.getValue("TotalUnitPriceAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalUsedPriceAmount", vat.ajax.getValue("TotalUsedPriceAmount",   vat.ajax.xmlHttp.responseText));
					var errorMessage = vat.ajax.getValue("ErrorMessage", vat.ajax.xmlHttp.responseText)
					if( errorMessage!="NONE" ){
						alert(errorMessage);
					}					
				}									
			});
		}else if( "CLOSE" == afterSavePageProcess ){
			execSubmitAction("CLOSE");
			
		}
	afterSavePageProcess = "" ;	
}


/* 金額再存一次 */
function afterSubmit(){
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=updateAJAXHeadTotalAmount" + 
								"&headId="       + vat.item.getValueByName("#F.headId") + 
								"&taxRate="      + vat.item.getValueByName("#F.taxRate") +
								"&exchangeRate=" + vat.item.getValueByName("#F.exchangeRate") + 
								"&purchaseType=" + vat.item.getValueByName("#F.purchaseType") +
								"&budgetYear="   + vat.item.getValueByName("#F.budgetYear") +
								"&budgetMonth="  + vat.item.getValueByName("#F.budgetMonth") +
								"&category01="  + vat.item.getValueByName("#F.category01") +//加入大類
								"&itemBrandCode="  + vat.item.getValueByName("#F.itemBrandCode") +//加入品牌
								"&categoryType=" + vat.item.getValueByName("#F.categoryType") ;
			vat.ajax.startRequest(processString, function (){
				if (vat.ajax.handleState()) {
					var exchangeRate = parseInt(vat.item.getValueByName("#F.exchangeRate"),10);
					if(isNaN(exchangeRate))
						exchangeRate = 1;
					vat.item.setValueByName("#F.totalLocalPurchaseAmount", vat.ajax.getValue("TotalLocalPurchaseAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalForeignPurchaseAmount", vat.ajax.getValue("TotalForeignPurchaseAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.taxAmount", vat.ajax.getValue("TaxAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalReceiptedAmount", vat.ajax.getValue("TotalReceiptedAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalOutstandAmount", vat.ajax.getValue("TotalOutstandAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalReturnedAmount", vat.ajax.getValue("TotalReturnedAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalProductCounts", vat.ajax.getValue("TotalProductCounts",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalBudget", vat.ajax.getValue("TotalBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalAppliedBudget", vat.ajax.getValue("TotalAppliedBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalRemainderBudget", vat.ajax.getValue("TotalRemainderBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalSigningBudget", vat.ajax.getValue("TotalSigningBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalReturnedBudget", vat.ajax.getValue("TotalReturnedBudget",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalUnitPriceAmount", vat.ajax.getValue("TotalUnitPriceAmount",   vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.totalUsedPriceAmount", vat.ajax.getValue("TotalUsedPriceAmount",   vat.ajax.xmlHttp.responseText));
					var errorMessage = vat.ajax.getValue("ErrorMessage", vat.ajax.xmlHttp.responseText)
					if( errorMessage!="NONE" ){
						alert(errorMessage);
					}					
				}									
			});
}


/* 顯示合計的頁面 */
function showTotalCountPage() {
	//alert("showTotalCountPage");
	vat.item.setValueByName("#F.budgetYear1",  vat.item.getValueByName("#F.budgetYear") );
	vat.item.setValueByName("#F.budgetMonth1", vat.item.getValueByName("#F.budgetMonth") );
	afterSavePageProcess = "totalCount";
	vat.block.pageSearch(3);
}


/* 變更匯率 */
function changeCurrencyCode(){
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXExchangeRateByCurrencyCode" +
						"&currencyCode=" + vat.item.getValueByName("#F.currencyCode") + 
						"&organizationCode=TM" ;
	vat.ajax.startRequest(processString,  function() { 
	  if (vat.ajax.handleState())
	  	vat.item.setValueByName("#F.exchangeRate", vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText));
	 });
}


/* 設定供應商資料 */
function onChangeSupplierCode() {
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplier" +
						"&supplierCode="  + vat.item.getValueByName("#F.supplierCode") + 
						"&organizationCode=TM"+
						"&brandCode="     + vat.item.getValueByName("#F.brandCode")  + 
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") ;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.countryCode",     vat.ajax.getValue("CountryCode",     vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.tradeTermCode",   vat.ajax.getValue("PriceTermCodeName",   vat.ajax.xmlHttp.responseText));//wade
			vat.item.setValueByName("#F.contactPerson",   vat.ajax.getValue("ContactPerson",   vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.taxType",         vat.ajax.getValue("TaxType",         vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.taxRate",         vat.ajax.getValue("TaxRate",         vat.ajax.xmlHttp.responseText));
			vat.item.setValueByName("#F.exchangeRate",    vat.ajax.getValue("ExchangeRate",    vat.ajax.xmlHttp.responseText));
			var defaultWarehouseCode_value = vat.ajax.getValue("DefaultWarehouseCode", vat.ajax.xmlHttp.responseText);
			if( ("" != defaultWarehouseCode_value) && (null !=defaultWarehouseCode_value) && ( "EPP"==vat.item.getValueByName("#F.orderTypeCode") ) ){
				vat.item.setValueByName("#F.defaultWarehouseCode", vat.ajax.getValue("DefaultWarehouseCode", vat.ajax.xmlHttp.responseText));
			}else{
				if("EPP"==vat.item.getValueByName("#F.orderTypeCode")){
					vat.item.setValueByName("#F.defaultWarehouseCode", "PCD00");	
				}
			}
			
			//如果新的供應商不等於原有的
			var categoryType = vat.item.getValueByName("#F.categoryType")
			if(categoryType != vat.ajax.getValue("CategoryType",    vat.ajax.xmlHttp.responseText)){
				vat.item.setValueByName("#F.categoryType",    vat.ajax.getValue("CategoryType",    vat.ajax.xmlHttp.responseText));
				changeCategoryType();
			}
			//讀取完供應商
			if(null != PurchaseCurrencyCode && "" != PurchaseCurrencyCode){
				vat.item.setValueByName("#F.currencyCode", PurchaseCurrencyCode);
				changeCurrencyCode();
			}
		}
	});
}

/* 更換採購單負責人 */
function onChangeSuperintendent() {
	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySuperintendent" +
						"&superintendentCode="  + vat.item.getValueByName("#F.superintendentCode") ;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("superintendentName", vat.ajax.xmlHttp.responseText))
		}
	});
}

/* 指定下一個狀態 */
function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == ""){
       	formStatus = "SIGNING";
    }else if(processId != null && processId != ""){
        if(status == "SAVE" || status == "REJECT"){
            formStatus = "SIGNING";
        }else if(approvalResult == "true"){
            formStatus = status;
        }else if(approvalResult == "false"){
            formStatus = "REJECT";
        }
    }
    return formStatus;
}

/* 執行送出 */
function execSubmitAction(actionId){

	var branchCode       = vat.bean("branchCode");
	var loginEmployeeCode= document.forms[0]["#loginEmployeeCode"].value;
	var typeCode         = vat.bean("typeCode");
	
	var formId               = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var processId            = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    	
    var status               = vat.item.getValueByName("#F.status");
    var orderTypeCode        = vat.item.getValueByName("#F.orderTypeCode");
	var countryCode          = vat.item.getValueByName("#F.countryCode");
    var currencyCode         = vat.item.getValueByName("#F.currencyCode");
    var exchangeRate         = vat.item.getValueByName("#F.exchangeRate");
    var quotationCode        = vat.item.getValueByName("#F.quotationCode");
    var contactPerson        = vat.item.getValueByName("#F.contactPerson");
    var paymentTermCode      = vat.item.getValueByName("#F.paymentTermCode");
    var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
    var isPartialShipment    = vat.item.getValueByName("#F.isPartialShipment");
    var paymentTermCode1     = vat.item.getValueByName("#F.paymentTermCode1");
    var budgetYear           = vat.item.getValueByName("#F.budgetYear");
    var reserve1             = vat.item.getValueByName("#F.reserve1");
    var scheduleReceiptDate  = vat.item.getValueByName("#F.scheduleReceiptDate");
    var reserve2             = vat.item.getValueByName("#F.reserve2");
    var reserve3             = vat.item.getValueByName("#F.reserve3");
   	var tradeTermCode        = vat.item.getValueByName("#F.tradeTermCode");
   	var packaging            = vat.item.getValueByName("#F.packaging");
   	var assignmentId         = vat.item.getValueByName("#assignmentId");
	
   	var invoiceTypeCode      = ""; 
    var taxType              = "";
    var taxRate              = "";
    
    if("POL"==orderTypeCode){	//不是國外進貨單時才要抓
    	invoiceTypeCode = vat.item.getValueByName("#F.invoiceTypeCode");
    	taxType         = vat.item.getValueByName("#F.taxType");
    	taxRate         = vat.item.getValueByName("#F.taxRate");	
    }
    
    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	var approvalResult  = vat.item.getValueByName("#F.approvalResult");
    if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
    
	var formStatus = status;
	if("SAVE" == actionId){
		formStatus = "SAVE";
	}else if("SUBMIT" == actionId){
		formStatus = changeFormStatus(formId, processId, status, approvalResult);
	}else if("SUBMIT_BG" == actionId){
		formStatus = "SIGNING";
	}else if("VOID" == actionId){
		formStatus = "VOID";
	}else if("CLOSE" == actionId){
		formStatus = "CLOSE";
	}

	vat.bean().vatBeanOther={
			beforeChangeStatus: status,
	        formStatus: formStatus,
	        loginEmployeeCode: loginEmployeeCode,
	        employeeCode: loginEmployeeCode,
	        assignmentId: assignmentId,
	        processId: processId,
	        approvalResult: approvalResult,
	        approvalComment: approvalComment
        };
	if ("SUBMIT_BG" == actionId){
		vat.block.submit(function(){return "process_object_name=poPurchaseMainAction"+
                "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
	}else{		
		vat.block.submit(function(){return "process_object_name=poPurchaseMainAction"+
            	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true, 
     	   funcSuccess:function(){     	      	
     	   		afterSubmit();	       	       	
     	  }});
	}	
}

/* 明細匯出 */
function exportFormData(){
    var headId = vat.item.getValueByName("#F.headId");
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=PO_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=poPurchaseOrderHeadMainService" + 
              "&processObjectMethodName=findByIdForExport" + 
              "&gridFieldName=poPurchaseOrderLines" + 
              "&arguments=" + headId + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '採購單明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

/* 明細匯入 */
function importFormData(){
    var headId = vat.item.getValueByName("#F.headId");
    var exchangeRate = vat.item.getValueByName("#F.exchangeRate");
	var suffix = 
			"&importBeanName=PO_ITEM" +
			"&importFileType=XLS" +
        	"&processObjectName=poPurchaseOrderHeadMainService" + 
        	"&processObjectMethodName=executeImportPoLists" +
        	"&arguments=" + headId + "{$}" + exchangeRate + 
        	"&parameterTypes=LONG{$}DOUBLE" + 
	        "&blockId=3";
		return suffix;
}

/* 訊息提示 */
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=PO_POPURCHASE_ORDER_HEAD" +
		"&levelType=ERROR" +
        "&processObjectName=poPurchaseOrderHeadMainService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + 
		',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

/* 背景送出 */
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=poPurchaseMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

/* 業種更新 */
function changeCategoryType(){
//alert(" 業種更新"+vat.item.getValueByName("#F.itemBrandCode"));
    if(vat.item.getValueByName("#F.categoryType") !== ""){    
       vat.ajax.XHRequest(
       {
           post:"process_object_name=poPurchaseOrderHeadMainService"+
                    "&process_object_method_name=getPurchaseEmployeeForAJAX"+
                    "&categoryType=" + vat.item.getValueByName("#F.categoryType")+
                    "&headId=" + vat.item.getValueByName("#F.headId")+
                    "&budgetYear=" + vat.item.getValueByName("#F.budgetYear")+
                    "&budgetMonth=" + vat.item.getValueByName("#F.budgetMonth")+
                    "&version30=Y",
           find: function changeShopCodeRequestSuccess(oXHR){ 
				vat.item.SelectBind(eval(vat.ajax.getValue("allPurchaseAssist"	, oXHR.responseText)),{ itemName : "#F.purchaseAssist" });
				vat.item.SelectBind(eval(vat.ajax.getValue("allPurchaseMember"	, oXHR.responseText)),{ itemName : "#F.purchaseMember" });
				vat.item.SelectBind(eval(vat.ajax.getValue("allPurchaseMaster"	, oXHR.responseText)),{ itemName : "#F.purchaseMaster" });
				vat.item.SelectBind(eval(vat.ajax.getValue("allFibudgetLine"	, oXHR.responseText)),{ itemName : "#F.itemBrandCode" });
				/*if(null != vat.ajax.getValue("allFibudgetLine",    oXHR.responseText)){						 				
					vat.item.setValueByName("#F.itemBrandCode",    vat.ajax.getValue("allFibudgetLine",    oXHR.responseText));	
						vat.item.bindAll();								
					}*/
				//vat.item.bindAll();
				//alert(vat.item.getValueByName("#F.budgetYear")));							
				//alert("商品品牌allFibudgetLine="+vat.ajax.getValue("allFibudgetLine"	, oXHR.responseText));
				//alert("allPurchaseMaster="+vat.ajax.getValue("allPurchaseMaster"	, oXHR.responseText));
				//vat.item.bindAll();				
				changeCategory01();
				//alert("業種更新完成"+vat.item.getValueByName("#F.itemBrandCode"));
				
           }   
       });
    }
}
function changeCategory01(){
//alert(" 大類更新");
    if(vat.item.getValueByName("#F.categoryType") !== ""){    
    //alert(vat.item.getValueByName("#F.categoryType")+vat.item.getValueByName("#F.itemBrandCode"));
       vat.ajax.XHRequest(
       {
           post:"process_object_name=poPurchaseOrderHeadMainService"+
          		"&process_object_method_name=findInitialCategory01"+
          		"&categoryType=" + vat.item.getValueByName("#F.categoryType")+
          		"&budgetYear=" + vat.item.getValueByName("#F.budgetYear")+
          		"&budgetMonth=" + vat.item.getValueByName("#F.budgetMonth")+
          		"&itemBrandCode=" + vat.item.getValueByName("#F.itemBrandCode")+          		
          		"&headId=" + vat.item.getValueByName("#F.headId"),
          find: function change(oXHR){
		
		//vat.item.SelectBind(eval(vat.ajax.getValue("category01s"		, oXHR.responseText)),{ itemName : "#F.category01" });
		//vat.item.setValueByName("#F.category01",    vat.ajax.getValue("categoryFibudgetLine",    oXHR.responseText))
		vat.item.SelectBind(eval(vat.ajax.getValue("categoryFibudgetLine"	, oXHR.responseText)),{ itemName : "#F.category01" });
		//alert("大類"+vat.ajax.getValue("categoryFibudgetLine",    oXHR.responseText));
		//vat.item.bindAll();
		}
	});
    }
}
function changeCategory021(){//
//alert('進入changeCategory01');
	vat.ajax.XHRequest({ 
	post:"process_object_name=poPurchaseOrderHeadMainService"+
          		"&process_object_method_name=findInitialCategory01"+
          		"&categoryType=" + vat.item.getValueByName("#F.categoryType")+
          		"&budgetYear=" + vat.item.getValueByName("#F.budgetYear")+
          		"&budgetMonth=" + vat.item.getValueByName("#F.budgetMonth")+
          		"&itemBrandCode=" + vat.item.getValueByName("#F.itemBrandCode")+          		
          		"&headId=" + vat.item.getValueByName("#F.headId"),
          asyn:false,                      
	find: function change(oXHR){
		
		//vat.item.SelectBind(eval(vat.ajax.getValue("category01s"		, oXHR.responseText)),{ itemName : "#F.category01" });
		//vat.item.setValueByName("#F.category01",    vat.ajax.getValue("categoryFibudgetLine",    oXHR.responseText))
		vat.item.SelectBind(eval(vat.ajax.getValue("categoryFibudgetLine"	, oXHR.responseText)),{ itemName : "#F.category01" });
		//alert("大類"+vat.ajax.getValue("categoryFibudgetLine",    oXHR.responseText));
		//vat.item.bindAll();
		}
	});
}

/* 供應商更新 */
function changeSupplierCode(){
	var vItemCode = vat.item.getGridValueByName("itemCode", 1);
	var brandCode = document.forms[0]["#loginBrandCode" ].value
	if(vItemCode != "" && brandCode == "T2"){
		vat.ajax.XHRequest({ 
			post:"process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getSupplierCode" + 
	                "&brandCode=" + brandCode+
	                "&itemCode=" + vItemCode,
            asyn:false,                      
			find: function change(oXHR){
				if(vat.ajax.getValue("SupplierCode", oXHR.responseText) != ""){
					vat.item.setValueByName("#F.supplierCode"	,vat.ajax.getValue("SupplierCode", oXHR.responseText));
					PurchaseCurrencyCode = vat.ajax.getValue("PurchaseCurrencyCode", oXHR.responseText);
					onChangeSupplierCode();
				}
           	}
		});
	}
}

/* 採購主管更新 */
function changePurchaseMember(){
	var branchCode = vat.bean("branchCode");
	if(branchCode=="2"){	// T2
		vat.item.setValueByName("#F.superintendentCode", vat.item.getValueByName("#F.purchaseMember"));
		onChangeSuperintendent()
    }
}

/* 查詢檢視後 */
function doAfterPickerProcess(){
    if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    //alert(vat.bean().vatBeanPicker.result.length);
	    if( vsMaxSize == 0){
	  	    vat.bean().vatBeanOther.firstRecordNumber   = 0;
		    vat.bean().vatBeanOther.lastRecordNumber    = 0;
		    vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		    vat.bean().vatBeanOther.firstRecordNumber   = 1;
		    vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		 
		    refreshForm(vsHeadId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}


/* 供應商picker 回來執行 */
function doAfterPickerSupplier(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplier"+
							"&addressBookId="  + vat.bean().vatBeanPicker.result[0].addressBookId +
							"&organizationCode=TM"+
							"&brandCode="      + vat.item.getValueByName("#F.brandCode") + 
							"&orderDate="      + vat.item.getValueByName("#F.orderDate") ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.supplierCode",    vat.ajax.getValue("SupplierCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.countryCode",     vat.ajax.getValue("CountryCode",     vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.exchangeRate",    vat.ajax.getValue("ExchangeRate",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.tradeTermCode", vat.ajax.getValue("PriceTermCodeName", vat.ajax.xmlHttp.responseText));//wade
				var defaultWarehouseCode_value = vat.ajax.getValue("DefaultWarehouseCode", vat.ajax.xmlHttp.responseText);
				if( ("" != defaultWarehouseCode_value) && (null !=defaultWarehouseCode_value) && ( "EPP"==vat.item.getValueByName("#F.orderTypeCode") ) ){
					vat.item.setValueByName("#F.defaultWarehouseCode", vat.ajax.getValue("DefaultWarehouseCode", vat.ajax.xmlHttp.responseText));
				}else{
					if("EPP"==vat.item.getValueByName("#F.orderTypeCode")){
						vat.item.setValueByName("#F.defaultWarehouseCode", "PCD00");	
					}
				}
				//vat.item.setValueByName("#F.tradeTeam",       vat.ajax.getValue("TradeTeam",       vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}


/* 採購負責人picker 回來執行 */
function doAfterPickerEmp(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataByEmp"+
							"&employeeCode="  + vat.bean().vatBeanPicker.result[0].employeeCode ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.superintendentCode", vat.bean().vatBeanPicker.result[0].employeeCode );
				vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("employeeName", vat.ajax.xmlHttp.responseText));
  			}
		} );
	}
}

/* 單筆明細結案 */
function doDetailClose(){
	var nItemLine = vat.item.getGridLine();
	var itemCode = vat.item.getGridValueByName("itemCode",nItemLine);
	var receiptedQuantity = parseInt(vat.item.getGridValueByName("receiptedQuantity",nItemLine), 10);
	var actualPurchaseQuantity = parseInt(vat.item.getGridValueByName("actualPurchaseQuantity",nItemLine), 10);
	
	if(receiptedQuantity != actualPurchaseQuantity){
	    if(confirm("商品"+itemCode+"，是否進行結案？")){
	    	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=updateCloseLine" +
							"&headId=" + vat.item.getValueByName("#F.headId") + 
							"&lineId=" + vat.item.getGridValueByName("lineId",nItemLine) +
							"&purchaseType=" + vat.item.getValueByName("#F.purchaseType");
			vat.ajax.startRequest(processString,  function() { 
				if (vat.ajax.handleState()){
					vat.item.setGridValueByName("actualPurchaseQuantity", nItemLine, vat.ajax.getValue("ActualPurchaseQuantity", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("outstandQuantity", nItemLine, vat.ajax.getValue("OutstandQuantity", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("outstandAmount", nItemLine, vat.ajax.getValue("OutstandAmount", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("returnedQuantity", nItemLine, vat.ajax.getValue("ReturnedQuantity", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("returnedAmount", nItemLine, vat.ajax.getValue("ReturnedAmount", vat.ajax.xmlHttp.responseText));
					alert("商品"+itemCode+"，已結案完成！");
				}
			});
		}
	}else{
		alert("商品"+itemCode+"，不需要結案！");
	}
}

/* 確定建立新資料 */
function createRefreshForm(){
    if(confirm("是否確定建立新資料？")){
        vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;
    	refreshForm("");
	 }
}

/* 報表列印 */
function listReport(){
	var processString;
		processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getReportURL" + 
							"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
							"&brandCode="     + vat.item.getValueByName("#F.brandCode") +
							"&employeeCode="  + vat.bean("employeeCode") +
							"&orderNo="       + vat.item.getValueByName("#F.orderNo")+
							"&reportName=po0102.rpt";

	vat.ajax.startRequest(processString,  function() {
		if (vat.ajax.handleState()){
			var returnURL = vat.ajax.getValue("returnURL", vat.ajax.xmlHttp.responseText);
			returnURL = returnURL.substring(0,returnURL.indexOf("','"));
			window.open(returnURL);
  			}
	 } );
}

/* 報表列印 */
function openReportWindow(type){ 
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
		vat.block.submit(
					function(){return "process_object_name=poPurchaseOrderHeadMainService"+
								"&process_object_method_name=getReportConfig";},{other:true,
                    			funcSuccess:function(){
								eval(vat.bean().vatBeanOther.reportUrl);
					}}
		);   
     if("AFTER_SUBMIT"==type) refreshForm();//因為調撥單在送出後要直接列印報表，所以要有這行
}

/* 離開 */
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
      window.top.close();
  }  	
}

/* 頁面顯示控制 */
function doFormAccessControl(){
	var processId 		= document.forms[0]["#processId"].value;
	var orderNo       	= vat.item.getValueByName("#F.orderNo");
    var formStatus    	= vat.item.getValueByName("#F.status");
    var brandCode    	= vat.item.getValueByName("#F.brandCode");
    var allowApproval 	= document.forms[0]["#allowApproval"].value;
    var canBeMod 		= document.forms[0]["#canBeMod"].value; 
    
    if(allowApproval == "N"){
    	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
    }
    
    if(vat.item.getValueByName("#F.orderNo").indexOf("TMP")){
    	vat.item.setAttributeByName("#F.createdBy", "readOnly", true);
    }
    
	if( vat.item.getValueByName("#F.status")=="SAVE" && vat.item.getValueByName("#F.orderNo").indexOf("TMP") != -1){
		vat.tabm.displayToggle(0, "xTab4", false);
	}else{
		vat.tabm.displayToggle(0, "xTab4", true, false, false);
     	refreshWfParameter( vat.item.getValueByName("#F.brandCode"), 
     		   				vat.item.getValueByName("#F.orderTypeCode"),
     		   				vat.item.getValueByName("#F.orderNo" ) );
		vat.block.pageDataLoad(102, vnCurrentPage = 1); 
	}
	
	if(processId != ""){
		vat.item.setStyleByName("#B.new",         "display", "none");
		vat.item.setStyleByName("#B.search",      "display", "none");
		if(formStatus!="SAVE" && formStatus!="REJECT"){
			vat.item.setAttributeByName("vatHeadDiv", "readOnly", true, true, true);
			vat.item.setAttributeByName("vatMasterDiv",  "readOnly", true, true, true);
			vat.item.setStyleByName("#B.submitBG"	, "display", "none");	
			vat.item.setStyleByName("#B.save"		, "display", "none");
			vat.item.setStyleByName("#B.void"		, "display", "none");
			vat.item.setStyleByName("#B.import"		, "display", "none");
			vat.item.setStyleByName("#B.message"	, "display", "none");
			vat.block.canGridModify( [vatDetailDiv], false, false, false );
			if(formStatus=="" || formStatus=="VOID" || formStatus=="CLOSE" ){
				vat.item.setStyleByName("#B.submit"	, "display", "none");
			}
		}
	}else{
	//除了TMP的SAVE單可以修改，其餘一率唯讀
		if(formStatus=="SAVE" && orderNo.indexOf("TMP") != -1){
			vat.item.setAttributeByName("vatHeadDiv", "readOnly", false, true, true);
			vat.item.setAttributeByName("vatMasterDiv",  "readOnly", false, true, true);
			vat.item.setStyleByName("#B.void"		, "display", "none");
			vat.item.setStyleByName("#B.submit"		, "display", "inline");
			vat.item.setStyleByName("#B.submitBG"	, "display", "inline");	
			vat.item.setStyleByName("#B.save"		, "display", "inline");
			vat.item.setStyleByName("#B.message"	, "display", "inline");
			vat.item.setStyleByName("#B.import"		, "display", "inline");
			vat.block.canGridModify( [vatDetailDiv], true, true, true );
		}else{
			vat.item.setAttributeByName("vatHeadDiv", "readOnly", true, true, true);
			vat.item.setAttributeByName("vatMasterDiv",  "readOnly", true, true, true);
			vat.item.setStyleByName("#B.submit"		, "display", "none");
			vat.item.setStyleByName("#B.submitBG"	, "display", "none");	
			vat.item.setStyleByName("#B.save"		, "display", "none");
			vat.item.setStyleByName("#B.void"		, "display", "none");
			vat.item.setStyleByName("#B.message"	, "display", "none");
			vat.item.setStyleByName("#B.import"		, "display", "none");
			vat.block.canGridModify( [vatDetailDiv], false, false, false);
		}
	}
	
	if(formStatus=="FINISH"&&canBeMod=="Y"){
		vat.item.setStyleByName("#B.close"		, "display", "inline");
		vat.block.canGridModify( [vatDetailDiv], true, false, false);
		vat.item.setGridAttributeByName("itemCode", "readOnly", true);
		vat.item.setGridAttributeByName("quantity", "readOnly", true);
		vat.item.setGridAttributeByName("foreignUnitCost", "readOnly", true);
		//vat.item.setGridAttributeByName("actualPurchaseQuantity", "readOnly", true);
		vat.item.setGridAttributeByName("detailClose", "disabled", false);
	}else{
		vat.item.setStyleByName("#B.close"		, "display", "none");
		vat.item.setGridAttributeByName("itemCode", "readOnly", false);
		vat.item.setGridAttributeByName("quantity", "readOnly", false);
		vat.item.setGridAttributeByName("foreignUnitCost", "readOnly", false);
		//vat.item.setGridAttributeByName("actualPurchaseQuantity", "readOnly", false);
		vat.item.setGridAttributeByName("detailClose", "disabled", true);
	}
	
    vat.block.pageRefresh(vatDetailDiv); 
}

/* 匯入成功後 */
function afterImportSuccess(){
	refreshHeadData();
}

/* 匯入成功後 */
function refreshHeadData(){	
	changeSupplierCode();
}