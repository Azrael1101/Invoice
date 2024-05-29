vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail8 =26;
var vnB_SingleSubmit =27;
var vnB_master = 2;
var isSelectDetail=false;

function outlineBlock(){
  	formInitial();
  	buttonLine();

  	headerInitial();
  	singleSubmit();
  	//doFormAccessControl();///呼叫doFormAccessControl method存取權限
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");

//MACO EOS 20151212
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);    
		vat.tabm.createButton(0 ,"xTab18","明細檔資料" ,"vatDetailDiv8","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", "doPageDataSave()");
		
	}
	
	kweMaster();
	kweDetail8();
	doFormAccessControl();
}

function formInitial(){

	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          brandCode  		    : document.forms[0]["#loginBrandCode"    ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode" 	 ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId      	: document.forms[0]["#assignmentId"      ].value,
          userType      	    : document.forms[0]["#userType"          ].value,
          beforeStatus			: "beforestatus",
	      nextStatus			: "nextstatus",
	      approvalResult        : "",
	      approvalComment		:"",
          status                :document.forms[0]["#status" ].value===""?"SAVE":document.forms[0]["#status" ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=poEosAction&process_object_method_name=performInitial"; 
	    	          },{
	    		other: true
	    	            }
	    );
	 		//vat.item.SelectBind(vat.bean("department"),{ itemName : "#F.department" ,selected : vat.bean().vatBeanOther.department});
  }
}

function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif", openMode:"open", service:"Po_Eos:search:20151217.page",servicePassData:function(x){ return doPassHeadData(x); },left:0, right:0, width:1024, height:768,	serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.openReportWindow",type:"IMG",value:"單據列印", src:"./images/button_form_print.gif", eClick:'reconfirmList()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.export"	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			//{name:"matchSpace"	   , type:"LABEL"  ,value:"　"},
				//{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
	 			//{name:"matchSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.searchList"  , type:"IMG"    ,value:"明細查詢",   src:"./images/detail_search.gif" , eClick:'doSearch()'},
	 			{name:"matchSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit1"      , type:"IMG"    ,value:"倉庫送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SHIP")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,
	 			{name:"#B.forward"     , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 2, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:"/"},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 2, mode:"READONLY" },
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"}],
	 			
	 			  td:"style='background-color:#eeeeee;; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}
function singleSubmit(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_SingleSubmit, {
	id: "vnB_SingleSubmit", generate: true,
	table:"cellspacing='1' class='default' border='1' cellpadding='2'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items : [{name : "#L.itemCode1", type : "LABEL", value : "單筆送出"}]},
		{items : [{name : "#L.itemCode1", type : "LABEL", value : "品號"},
		{name : "#F.itemCode1", type : "TEXT",mode:'READONLY',size:20,eChange:"changeItemCode1()"},
		{name : "#L.sorceWarehouse1", type : "LABEL", value : "來源庫存"},
		{name : "#F.sorceWarehouse1", type : "TEXT",mode:'READONLY'},
		{name : "#L.warehouseQty1", type : "LABEL", value : "剩餘庫存"},
		{name : "#F.warehouseQty1", type : "TEXT",mode:'READONLY'},		
		{name : "#L.boxCapacity1", type : "LABEL", value : "基本量"},
		{name : "#F.boxCapacity1", type : "TEXT",mode:'READONLY'},
		{name : "#L.requestQty1", type : "LABEL", value : "需求量"},
		{name : "#F.requestQty1", type : "TEXT",mode:'READONLY'}]},	
	 	{items : [{name : "#B.submit2"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSingleInsert()'},
	 	{name : "#F.msg1", type : "TEXT",size:60,maxLen:60,mode:'READONLY'}]}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}















function headerInitial(){

//MACO EOS 20151212
    var ifCostControl=[["","",false],
                 ["是","否"],
                 ["Y","N"]];
    var ifWareHouseControl=[["","",false],
                 ["是","否"],
                 ["Y","N"]];             
	//var orderTypeCode       = vat.item.getValueByName("#orderTypeCode");
    var userType = document.forms[0]["#userType"].value;
 	//var allCategroyTypes    = vat.bean("allCategroyTypes");
 	var allproject          = vat.bean("allproject");
 	var allShop		        = vat.bean("allShop");
 	var allWarehouse        = vat.bean("allWarehouse");
 	var allstatus           = [[true, true,true,true,true], ["暫存", "待出貨", "已出貨","作廢"],["SAVE","SIGNING","FINISH","VOID"]];
 	var allstatusLoa        = [[true,true], ["暫存", "簽核完成"],["SAVE","FINISH"]];
 	var allStorage			= [[true,true,true,true,true], ["請選擇","A區", "非A區", "第三儲區"],["","A區","非A區","第三儲區"]];
 	var allSortCondition	= [[true,true,true,true,true], ["請選擇","0-商品金額","1-品號", "2-商品品牌", "3-中類"],["","model.unitPrice","model.itemCode","model.itemBrand","model.category02"]];
 	var allSortType			= [[true,true], ["請選擇","0-由小到大", "1-由大到小"],["","asc","desc"]];
 	var allCategory02 		= vat.bean("allCategory02");
 	//var vsCostStyle=type !="1"?" style= 'display:none;'":""; 

	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"EOS維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌/<font color='red'></font>"},
						{name:"#L.orderTypeCode", type:"LABEL", value:"單別/<font color='red'></font>"},
						{name:"#L.orderNo", type:"LABEL" , value:"單號<font color='red'></font>"}]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:3, maxLen:20, mode:"READONLY"},
						{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", size:3, maxLen:20,mode:"READONLY" },
						{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:30, maxLen:35,mode:"READONLY" },
						{name:"#F.headId"     , type:"TEXT"  ,  size:4, bind:"headId" , mode:"READONLY"}]},
						
				{items:[{name:"#L.requestCode", type:"LABEL", value:"需求人員<font color='red'>*</font>"}]},
				{items:[{name:"#F.requestCode", type:"TEXT", bind:"requestCode", size:10, maxLen:25,eChange:"eChangeRequest()" },
				 		{name:"#B.requestCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.request"   , type:"TEXT", bind:"request", size:5, maxLen:25,mode:"READONLY"},
						{name:"#F.lastUpdatedBy"   , type:"hidden", bind:"lastUpdatedBy", size:5, maxLen:25,mode:"READONLY"}]},
						
				{items : [{name : "#L.department", type : "LABEL", value : "店別<font color='red'>*</font>"}]},
				{items : [{name : "#F.department", type:"SELECT", bind:"department",init:allShop,eChange:"changeDesWarehous()"},
						  {name : "#F.warehouseCode", type:"TEXT", bind:"warehouseCode",init:allWarehouse,mode:"HIDDEN"},
						  {name : "#F.storage", type:"SELECT",init:allWarehouse,bind:"otherGroup",init:allStorage,mode:"READONLY"}]}]},
			{row_style:"", cols:[
				//{items : [{name : "#L.no", type : "LABEL", value : "主旨<font color='red'>*</font>"}]},
				//{items : [{name : "#F.no", type : "TEXT", bind : "no", size : 50, maxLen : 25}],td:" colSpan=3"},
				{items : [{name : "#L.no", type : "LABEL", value : "主旨<font color='red'>*</font>"}]},
				{items : [{name : "#F.no", type : "TEXT", bind : "no",mode:"READONLY", size : 50, maxLen : 25}]},
				//{items : [{name : "#L.itemCategory", type : "LABEL", value : "業種<font color='red'></font>"}]},
				//{items : [{name : "#F.itemCategory", type : "SELECT", bind : "itemCategory",init:allCategroyTypes, size : 10, maxLen : 25}]},
				{items : [{name : "#L.requestDate", type : "LABEL", value : "需求日期<font color='red'></font>"}]},
				{items : [{name : "#F.requestDate", type : "DATE", bind : "requestDate",mode:"READONLY", size : 10, maxLen : 25}]},
				{items : [{name:"#L.status", type:"LABEL", value:"狀態"}]},
				{items : [{name:"#F.status", type:"TEXT", bind:"status", size:25, maxLen:25,mode:"HIDDEN"},
				          {name:"#F.statusName",         type:"TEXT",   bind:"statusName",          mode:"READONLY",  back:false}]}
				
				
				]},
				//篩選/排序條件 2016.06.20 MACO
			{row_style:"", cols:[

				{items : [{name : "#L.filter", type : "LABEL", value : "<font color='bule'>篩選條件</font>",size : 10}],td:"style='background-color:yellow;'"},
				{items : [{name : "#L.filterA", type : "LABEL", value : "  <font color='bule'>中類</font>"},
						  {name : "#F.filterA", type : "SELECT",init:allCategory02,mode:'READONLY'},
						  {name : "#L.filterB", type : "LABEL", value : "  /  <font color='bule'>商品品牌</font>"},
						  {name : "#F.filterB", type : "TEXT",mask:'CCCCCC',mode:'READONLY'},
						  {name : "#L.filterC", type : "LABEL", value : "  /  <font color='bule'>庫存</font>"},
						  {name : "#F.filterC1", type : "TEXT",mode:'READONLY'},
						  {name : "#L.tilde", type : "LABEL", value : "~"},
						  {name : "#F.filterC2", type : "TEXT",mode:'READONLY'},
						  {name : "#L.filterD", type : "LABEL", value : "  /  <font color='bule'>金額$</font>"},
						  {name : "#F.filterD1", type : "TEXT",mode:'READONLY'},
						  {name : "#L.tilde", type : "LABEL", value : "~<font color='bule'>$</font>"},
						  {name : "#F.filterD2", type : "TEXT",mode:'READONLY'},
						   {name : "#B.submitFilter"      , type:"IMG"    ,value:"送出",src:"./images/16x16/Search.png", eClick:"doFilter()"}
						  ],td:"colSpan=4, style='background-color:yellow;'"},
						  
				{items : [{name : "#L.sortCondition", type : "LABEL", value : "  <font color='bule'>排序條件</font>"},
						  {name : "#F.sortCondition", type : "SELECT",init:allSortCondition,mode:'READONLY'},
						  {name : "#L.sortType", type : "LABEL", value : "  <font color='bule'>排序方式</font>"},
						  {name : "#F.sortType", type : "SELECT",init:allSortType,mode:'READONLY'},
						  {name : "#B.submitFilter1"      , type:"IMG"    ,value:"送出",src:"./images/16x16/Search.png", eClick:"doFilter()"}],td:"style='background-color:yellow;'"}
				
				
				]}
			/*{row_style:"", cols:[//("1" == type && "LOA" == orderTypeCode) ? "" : 
						{items:[{name:"#L.applicant", type:"LABEL", value:"申請人<font color='red'>*</font>"}]},
						{items:[{name:"#F.applicant", type:"TEXT", bind:"applicant", size:10, maxLen:25, eChange:"eChangeApplicant()" },
						{name:"#F.applicantName"   , type:"TEXT", bind:"applicantName", size:5, maxLen:25,mode:"READONLY"}]},
						/*
				 		{name:"#B.applicant",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
		 									 	{name:"#F.applicantName"   , type:"TEXT", bind:"applicantName", size:5, maxLen:25,mode:"READONLY"}]},
		 				
						
						
						{items:[{name:"#L.applicant0", type:"LABEL", value:"同"}]},
						{items:[{name:"#F.applicant0", type:"TEXT", bind:"applicant0", size:10, maxLen:25, eChange:"eChangeApplicant()" },
				 		{name:"#B.applicant0",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.applicantName0"   , type:"TEXT", bind:"applicantName0", size:5, maxLen:25,mode:"READONLY"}]}
				]}*/
		], 
		beginService:"",
		closeService:""			
	});
	
}


function kweMaster()
{
 		 var allproject    = vat.bean("allproject"); 
 		 var allsystem     = vat.bean("allsystem"); 
 		 var allCategroyTypes    = vat.bean("allCategroyTypes");
		 var allCategory01 = vat.bean("allCategory01");
		 var allCategory02 = vat.bean("allCategory02");
		 var allCategory03 = vat.bean("allCategory03");
 		 var status        = vat.item.getValueByName("#F.status")===null ? "SAVE" : vat.item.getValueByName("#F.status");
 		 var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
 		 var loginDepartment      = vat.item.getValueByName("#loginDepartment");
 		 var type      = vat.item.getValueByName("#type");
		 var taxType=[["","",false], ["F-保稅","P-完稅"], ["F","P"]];   

	vat.block.create(vnB_master, {
	id: "vatBlock_Master", generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'", rows:[
	{row_style:"", cols:[	
	
		{items:[{name:"#L.itemCategory"  , type : "LABEL" , value :"業種/"},
				{name:"#L.category01"	 , type : "LABEL" , value :"大類/"},
				{name:"#L.category02"  	 , type : "LABEL" , value :"中類/"},
				{name:"#L.isTax"  	 	 , type : "LABEL" , value :"稅別<font color='red'>*</font>"}]},
		{items:[{name:"#F.itemCategory"  , type : "SELECT", bind  :"itemCategory",init:allCategroyTypes, size : 10, maxLen : 25},
				{name:"#F.category01"    , type : "SELECT", bind  :"category01",init:allCategory01, eChange:"changeCategory()", size:25},
				{name:"#F.category02"    , type : "SELECT", bind  :"category02",init:allCategory02,  size:10},
				{name:"#I.append1"		 , type:"IMG" , value:"新增", src:"./images/16x16/Add.png", eClick: function(){ changeAppend('category02');} },
				{name:"#I.decrease1"		 , type:"IMG" , value:"減少", src:"./images/16x16/Remove.png", eClick: function(){ changeDecrease('category02');} },
				{name:"#F.isTax"    	 , type : "SELECT", bind  :"isTax",init:taxType, size:10}]},
		{items:[{name:"#L.itemBrand"	 , type : "LABEL" , value : "商品品牌<font color='red'>*</font>"}]},
		{items:[{name:"#F.itemBrand"	 , type : "TEXT"  , bind  :"itemBrand",mask:'CCCCCC', eChange: function(){ changeCategoryCodeName("ItemBrand");}},
				{name:"#B.itemBrand",	value:"選取" ,type:"PICKER" ,
			 									 		openMode:"open", src:"./images/start_node_16.gif",
			 									 		service:"Im_Item:searchCategory:20100119.page", 
			 									 		left:0, right:0, width:1024, height:768,
			 									 		servicePassData:function(){ return doPassData("ItemBrand"); },	
			 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("itemBrand");}},
		 		{name:"#F.itemBrandName",	type:"TEXT", 	bind:"itemBrandName", back:false, mode:"READONLY"},
		 		{name:"#I.append2"		 , type:"IMG" , value:"新增", src:"./images/16x16/Add.png", eClick: function(){ changeAppend('itemBrand');} },
				{name:"#I.decrease2"		 , type:"IMG" , value:"減少", src:"./images/16x16/Remove.png", eClick: function(){ changeDecrease('itemBrand');} }
			 									 		 ]}]},
	{row_style : "", cols:[	
		{items:[{name:"#L.category02s"	 , type : "LABEL" , value : "中類候選清單<font color='red'>*</font>"}]},
		{items:[{name:"#F.category02s"    , type : "TEXT", bind  :"category02s",mode:"READONLY", size:150,maxLen:150}]},
		{items:[{name : "#L.depManager", type : "LABEL", value : "需求主管"}]},
		{items:[{name : "#F.depManager", type : "TEXT", bind : "depManager", size :10, maxLen : 25,  mask:"Aaaaaaaaaa",eChange:"eChangedepManager()"},
				{name : "#B.depManager",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee1();}},
			    {name : "#F.depManagerName", type : "TEXT", bind : "depManagerName", size:10, maxLen : 25,mode:"READONLY"}]}]},
	{row_style : "", cols:[	
		{items:[{name:"#L.itemBrands"	 , type : "LABEL" , value : "品牌候選清單<font color='red'>*</font>"}]},
		{items:[{name:"#F.itemBrands"    , type : "TEXT", bind  :"itemBrands",mode:"READONLY", size:150,maxLen:150}]},
		{items :[{name : "#L.createdBy", type : "LABEL", value : "填單人員<font color='red'></font>"}]},
		{items :[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25,mode:"READONLY"},
				 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}]}]},
	//{row_style : "", cols:[	]}
	],
			
			beginService:function(){return "";},
			closeService:function(){return "";}
		});
}

function kweDetail8(){
		var isShip =[["","",false], ["尚未出貨","已出貨"], ["N","Y"]];  
		var userType = document.forms[0]["#userType"].value; 
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

    vat.item.make(vnB_Detail8, "indexNo"                   , {type:"IDX"   ,                     desc:"序號"          });
    vat.item.make(vnB_Detail8, "lineId"                  , {type:"NUM"  , size:18, maxLen:20, desc:"流水編碼", mode:"HIDDEN"});
	vat.item.make(vnB_Detail8, "itemNo"                  , {type:"TEXT"  , size:18, maxLen:20, desc:"品號", mode:"READONLY" , mask:"CCCCCCCCCCCC", eChange:"changeItemCode()"});
    //vat.item.make(vnB_Detail8, "specInfo"	              , {type:"PICKER", desc:"",mode:"" != vat.item.getValueByName("#F.shopCode") ? "" : "ReadOnly", openMode:"open", src:"./images/start_node_16.gif",
	vat.item.make(vnB_Detail8, "specInfo"	              , {type:"PICKER", desc:"",openMode:"open", src:"./images/start_node_16.gif",mode:"HIDDEN",
	 									 		            	service:"Im_OnHand:search:20091224.page",
	 									 			            left:0, right:0, width:1024, height:768,
	 									 						servicePassData:function(x){ return doPassLineData(x); },
	 									 		                serviceAfterPick:function(id){doLineAfterPickerProcess(id); } });
	vat.item.make(vnB_Detail8, "itemName"                  , {type:"TEXT" , size:12, maxLen:20, desc:"品名"	, mode:"READONLY"	, alter:true });
	vat.item.make(vnB_Detail8, "supplier"     , {type:"TEXT" , size: 5, maxLen:20, desc:"來源庫"	, mode:"READONLY"});
	vat.item.make(vnB_Detail8, "purTotalAmount"       , {type:"NUMM" , size: 4, maxLen:12, desc:"來源庫存"	, dec:0	, mode:"READONLY"});
	vat.item.make(vnB_Detail8, "totalQty"       , {type:"NUMM" , size: 4, maxLen:12, desc:"已有需求"	, dec:0	, mode:"READONLY"});
	vat.item.make(vnB_Detail8, "boxCapacity"          , {type:"NUMM" , size: 4, maxLen:12, desc:"基本量"	, mode:"READONLY", dec:0});
	vat.item.make(vnB_Detail8, "quantity"          , {type:"NUMM" , size: 4, maxLen:12, desc:"需求量"	,eChange:"changeQty()", dec:0});
	vat.item.make(vnB_Detail8, "box"          , {type:"NUMM" , size: 4, maxLen:12, desc:"箱數"	, mode:"HIDDEN", dec:0});
	vat.item.make(vnB_Detail8, "unitPrice"     , {type:"NUMM" , size: 4, maxLen:12, desc:"售價"	, mode:"READONLY", dec:0});
	vat.item.make(vnB_Detail8, "shopCode"      , {type:"TEXT" , size: 4, maxLen:20, desc:"目的庫別"	, mode:"READONLY"});
	vat.item.make(vnB_Detail8, "reTotalAmount"       , {type:"NUMM" , size: 4, maxLen:12, desc:"店庫存"	, dec:0	, mode:"READONLY"});
	vat.item.make(vnB_Detail8, "enable"       , {type:"SELECT" , size: 4, maxLen:12, desc:"已出貨"	,init:isShip, dec:0, mode:"READONLY"	});
	vat.item.make(vnB_Detail8, "checkButton"       , {type:"BUTTON" , size: 4, maxLen:12, value:"已出貨"	,mode:"HIDDEN", dec:0,eClick:'controler()'	});
	vat.block.pageLayout(vnB_Detail8, {
														id: "vatDetailDiv8",
														pageSize: 10,
														searchKey     : ["itemCode"],
								                        canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,
														beginService: "",
														indexType	: "AUTO",
								                        //closeService: function(){kweImInitial();},
														//appendBeforeService : "kwePageAppendBeforeMethod()",
														//appendAfterService  : "kwePageAppendAfterMethod()",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()",
														//eventService        : "assignOriginalQtyToArrival()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
}

function eChangeRequest() {
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByRequest" +
						"&requestCode="  + vat.item.getValueByName("#F.requestCode");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.request", vat.ajax.getValue("request", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.requestCode", vat.ajax.getValue("requestCode", vat.ajax.xmlHttp.responseText))
		}
	});
}
function loadSuccessAfter(){
		$.unblockUI();
}
function saveSuccessAfter(){
		$.unblockUI();
}
function doFilter(){
//vat.block.pageRefresh(vnB_Detail8,vnCurrentPage = 1);
vat.block.pageDataSave(vnB_Detail8,
				function(){
					vat.block.pageDataLoad(vnB_Detail8 , vnCurrentPage = 1);
				});
}

function doLineAfterPickerProcess(id){
	//do picker back something
	//alert("TEST");
	var i = 0;

	if(vat.bean().vatBeanPicker.result !== null){
		
		var vLineId	= vat.item.getGridLine(id);
		//alert(id+"   "+vLineId);
        var vIndexNo= parseInt(vat.item.getGridValueByName("indexNo"  , vLineId));
  		vat.bean().vatBeanOther.lineId = vLineId;
  		vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
  		vat.bean().vatBeanOther.deliveryWarehouseCode = vat.item.getValueByName("#F.deliveryWarehouseCode");
  		vat.bean().vatBeanOther.arrivalWarehouseCode = vat.item.getValueByName("#F.arrivalWarehouseCode");
  		//alert(vat.bean().vatBeanOther.lineId );
		vat.block.submit(
						function()
						{
							return "process_object_name=poEosService&process_object_method_name=updatePickerData";
						},{
							other:true,picker:true, funcSuccess:
							function()
							{
								//alert("readyToReflash");
								doFormAccessControl();
								vat.item.setAttributeByName("#F.countryCode", "readOnly", true);
								vat.block.pageRefresh(vnB_Detail8);
							}});

	}
}

function doPassLineData(x){
//alert("TEST1");
 // vat.block.pageDataSave( vnB_Detail8, {refresh:false});
 isSelectDetail = true;
  var suffix = "";
  var vLineId	      = vat.item.getGridLine(x);
  var vItemCode       = vat.item.getGridValueByName("itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();;
  //var vWarehouseCode  = vat.item.getValueByName("#F.sorceWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vWarehouseCode  = vat.item.getValueByName("#F.warehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vShopCode  = vat.item.getValueByName("#F.department").replace(/^\s+|\s+$/, '').toUpperCase();
  var vItemCategory   = vat.item.getValueByName("#F.itemCategory").replace(/^\s+|\s+$/, '').toUpperCase();
  var vCategory01   = vat.item.getValueByName("#F.category01").replace(/^\s+|\s+$/, '').toUpperCase();
  var vCategory02   = vat.item.getValueByName("#F.category02").replace(/^\s+|\s+$/, '').toUpperCase();
  var vCategory03   = vat.item.getValueByName("#F.category03").replace(/^\s+|\s+$/, '').toUpperCase();
 // alert("LineId:"+vLineId+"   vShopCode:"+vShopCode+"   vWarehouseCode"+vWarehouseCode);
  suffix += "&startItemCode="+escape(vItemCode)+
            "&startWarehouseCode="+escape(vWarehouseCode)+
            "&endWarehouseCode="+escape(vWarehouseCode)+
            "&category01="+escape(vCategory01)+
            "&category02="+escape(vCategory02)+
            "&category03="+escape(vCategory03)+
            "&itemCategory="+escape(vItemCategory)+
            "&isReadOnlyControl=Y";
  return suffix;
}

/*
function loadBeforeAjxService()
{ 
		var formId=vat.bean().vatBeanOther.formId;
		var processString = "process_object_name=poEosService&process_object_method_name=getAJAXPageData" 
		                    +"&headId=" + vat.item.getValueByName("#F.headId")
		                    +"&brandCode=" + vat.item.getValueByName("#F.brandCode")
		                    +"&shopCode=" + vat.item.getValueByName("#F.department");
		                    //alert(processString); 
		return processString;		
		 // alert("---------loadBeforeAjxService:" + vat.bean().vatBeanOther.formId);					
}
*/
function saveBeforeAjxService()
{  // alert("saveBeforeAjxService");
				$.blockUI({
	        message: '<font size=4 color="#000000"><b>儲存頁面資訊，請稍後...</b></font>',
	        overlayCSS: { // 遮罩的css設定
	            backgroundColor: '#eee'
	        },
	        css: { // 遮罩訊息的css設定
	            border: '3px solid #aaa',
	            width: '30%',
	            left: '35%',
	            backgroundColor: 'white',
	            opacity: '0.9' //透明度，值在0~1之間
	        }
    	});
		var processString = "process_object_name=poEosService&process_object_method_name=updateAJAXPageLinesData" 
							+"&headId=" + vat.item.getValueByName("#F.headId")
		                    +"&brandCode=" + vat.item.getValueByName("#F.brandCode")
		                    +"&shopCode=" + vat.item.getValueByName("#F.warehouseCode")
		                    +"&status=" + vat.item.getValueByName("#F.status");
		                    //alert(processString); 
		return processString;					
}





function doSubmit(formAction)
{
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var status        = vat.item.getValueByName("#F.status");
	if(formAction==="SUBMIT")
	{
		var alertMessage ="是否確定送出?";
	}
	if(formAction==="SHIP")
	{
		var alertMessage ="是否確定出貨?";
	}
	else if(formAction==="VOID")
	{
		var alertMessage ="是否確定作廢?";
	}
	else if(formAction==="SAVE")
	{
		var alertMessage ="是否確定暫存?";
	}
	
	if(confirm(alertMessage))
	{
	
		changeStatus(status,formAction);
		if(vat.item.getValueByName("#F.orderTypeCode")=="EOS")
		{
			if(vat.item.getValueByName("#F.requestCode")!=="")
			{
				vat.bean().vatBeanOther.formAction = formAction;
				vat.block.pageDataSave(vnB_Detail8,
				{  
		 			funcSuccess:function()
		 			{
		 				//alert("DO");
						vat.block.submit(
						function()
						{
							return "process_object_name=poEosAction&process_object_method_name=performTransaction"
							+"&headId=" + vat.item.getValueByName("#F.headId");
						},
						{
							bind:true, link:true, other:true,
							funcSuccess: function ()
							{
								vat.block.pageRefresh(vnB_Detail8);
		               
		            		} 
						});
					}
				});
			}
			else
			{
				alert("請輸入需求人員工號!");
			}
		}

	}
}

function  changeStatus(nowStatus,formAction){

		vat.ajax.XHRequest(
       	{
           post:"process_object_name=poEosService"+
			"&process_object_method_name=findNewStatus"+
			"&headId=" + vat.item.getValueByName("#F.headId")+
			"&nowStatus=" + nowStatus+
			"&formAction=" + formAction,
           find: function changeSuperintendentRequestSuccess(oXHR){ 

              vat.item.setValueByName("#F.status",vat.ajax.getValue("newStatus", oXHR.responseText));

        }   
       });



}

function createNewForm(){
	createRefreshForm("")
}
function createRefreshForm(code){

	vat.item.setValueByName("#F.headId","");
	refreshForm("");
	vat.tabm.displayToggle(0, "xTab1", true);//初始化完畢直接切換至主檔頁簽
}
function refreshForm(code)
{

	document.forms[0]["#formId"].value = code; 
    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;  

    vat.block.submit(
        function()
        {
            return "process_object_name=poEosAction&process_object_method_name=performInitial"; 
        },
        {
        		other      : true, 
           funcSuccess:function()
           {
                vat.item.bindAll();
                vat.item.setAttributeByName("#F.itemCategory", "readOnly", false);
			    vat.item.setAttributeByName("#F.category01", "readOnly", false);
			    vat.item.setAttributeByName("#F.category02", "readOnly", false);
			    vat.item.setAttributeByName("#F.category03", "readOnly", false);
			    vat.item.setAttributeByName("#F.department", "readOnly", false);
			    vat.item.setAttributeByName("#F.isTax", "readOnly", false);
			    vat.item.setAttributeByName("#F.itemBrand", "readOnly", false);
			    vat.item.setAttributeByName("#F.storage", "readOnly", true);
			     vat.item.setAttributeByName("#F.requestCode", "readOnly", false);
			    if(code==="")
			    {
			    	vat.item.setValueByName("#F.itemCategory","");
			    	vat.item.setValueByName("#F.category01","");
			    	vat.item.setValueByName("#F.category02","");
			    	vat.item.setValueByName("#F.category03","");
			    	vat.item.setValueByName("#F.department","");
			   		vat.item.setValueByName("#F.itemBrand","");
			    	vat.item.setValueByName("#F.storage","");
			    }
			    vat.item.setValueByName("#F.filterA","");
			    vat.item.setValueByName("#F.filterB","");
			    vat.item.setValueByName("#F.filterC1","");
			    vat.item.setValueByName("#F.filterC2","");
			    vat.item.setValueByName("#F.filterD1","");
			    vat.item.setValueByName("#F.filterD2","");
			    vat.item.setValueByName("#F.sortCondition","");
			    vat.item.setValueByName("#F.sortType","");
			    vat.item.setValueByName("#F.itemCode1","");
			    vat.item.setValueByName("#F.requestQty1","");
			    vat.item.setValueByName("#F.msg1","");
			    vat.item.setValueByName("#F.sortCondition","");
			    vat.item.setValueByName("#F.warehouseQty1","");
			    vat.item.setValueByName("#F.sorceWarehouse1","");
			    vat.item.setValueByName("#F.boxCapacity1","");
			          
			    
			    
			    
			    vat.item.setStyleByName("#B.searchList","display", "inline");
			    vat.item.setStyleByName("#B.itemBrand","display", "inline");
			    vat.item.setStyleByName("#B.depManager","display", "inline");
			    vat.item.setStyleByName("#B.requestCode","display", "inline");
			    
			    vat.item.setStyleByName("#B.submitFilter","display", "none");
				vat.item.setStyleByName("#B.submitFilter1","display", "none");
				vat.item.setStyleByName("#B.submit2","display", "none");
				
								
				vat.item.setAttributeByName("#F.filterA", "readOnly", true);
				vat.item.setAttributeByName("#F.filterB", "readOnly", true);
				vat.item.setAttributeByName("#F.filterC1", "readOnly", true);
				vat.item.setAttributeByName("#F.filterC2", "readOnly", true);
				vat.item.setAttributeByName("#F.filterD1", "readOnly", true);
				vat.item.setAttributeByName("#F.filterD2", "readOnly", true);
				vat.item.setAttributeByName("#F.filterD2", "readOnly", true);
				vat.item.setAttributeByName("#F.sortCondition", "readOnly", true);
				vat.item.setAttributeByName("#F.sortType", "readOnly", true);
				
				
					 	
				
				vat.item.setAttributeByName("#F.itemCode1", "readOnly", true);
				vat.item.setAttributeByName("#F.requestQty1", "readOnly", true);
			    
			    
                vat.block.pageRefresh(vnB_Detail8); 
				//alert('123');
				doFormAccessControl();

        }}
    );
     
}
function changeItemCode(){


var nItemLine = vat.item.getGridLine();
var vemployeeCode = vat.item.getGridValueByName("itemNo"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	
vat.item.setGridValueByName("itemNo", nItemLine, vemployeeCode);
		
		vat.ajax.XHRequest(
       	{
           post:"process_object_name=poEosService"+
			"&process_object_method_name=findInfoByItemCode"+
			 "&itemNo=" + itemNo,
           find: function changeSuperintendentRequestSuccess(oXHR){ 

					
					
					
               vat.item.setGridValueByName("itemNo", nItemLine, vat.ajax.getValue("itemNo", oXHR.responseText));
               vat.item.setGridValueByName("itemName", nItemLine, vat.ajax.getValue("itemName", oXHR.responseText));
               vat.item.setGridValueByName("boxCapacity", nItemLine, vat.ajax.getValue("boxCapacity", oXHR.responseText));
               vat.item.setGridValueByName("purTotalAmount", nItemLine, vat.ajax.getValue("purTotalAmount", oXHR.responseText));
               vat.item.setGridValueByName("boxCapacity", nItemLine, vat.ajax.getValue("boxCapacity", oXHR.responseText));
               vat.item.setGridValueByName("quantity", nItemLine, vat.ajax.getValue("quantity", oXHR.responseText));
               vat.item.setGridValueByName("box", nItemLine, vat.ajax.getValue("box", oXHR.responseText));
               vat.item.setGridValueByName("reTotalAmount", nItemLine, vat.ajax.getValue("reTotalAmount", oXHR.responseText));
        }   
       });
}
function changeDesWarehous(){
	vat.ajax.XHRequest({
		post:"process_object_name=poEosService&process_object_method_name=findWarehouseByShop"
                  + "&department=" +  vat.item.getValueByName("#F.department"),


		find: function change(oXHR){
				vat.item.setValueByName("#F.no", vat.ajax.getValue("no", oXHR.responseText));
         		vat.item.setValueByName("#F.warehouseCode", vat.ajax.getValue("warehouseCode", oXHR.responseText));
         		if(vat.item.getValueByName("#F.warehouseCode")==='31250')
         			vat.item.setAttributeByName("#F.storage", "readOnly", false);
         		else
         			vat.item.setAttributeByName("#F.storage", "readOnly", true);
         			//vat.item.setValueByName("#F.storage","A區");
         			
         		//vat.block.pageRefresh(vnB_Detail8);
		},
		fail: function changeError(){

         		alert("無對應庫別");
		}  
       });

}
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		// alert(code);
		}
		
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
		
	}
	  refreshForm(code);
}
function changeCategory(){
	vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
 	vat.bean().vatBeanOther.category01 = vat.item.getValueByName("#F.category01");
 	vat.bean().vatBeanOther.category02 = vat.item.getValueByName("#F.category02");

	vat.block.submit(function(){return "process_object_name=imItemCategoryService"+
	            "&process_object_method_name=getAJAXItemCategoryRelatedList";},  {other:true,picker:false,
	     funcSuccess: function() {
	        vat.item.SelectBind(vat.bean("allCategory01"),{ itemName : "#F.category01" , selected : vat.bean().vatBeanOther.category01});
    		vat.item.SelectBind(vat.bean("allCategory02"),{ itemName : "#F.category02" , selected : vat.bean().vatBeanOther.category02});
    		vat.item.SelectBind(vat.bean("allCategory02"),{ itemName : "#F.filterA"    , selected : vat.bean().vatBeanOther.category02});
    		vat.item.SelectBind(vat.bean("allCategory03"),{ itemName : "#F.category03" });
	     }
	});
}
function changeQty()
{

	var nItemLine = vat.item.getGridLine();
	var vQty = vat.item.getGridValueByName("quantity"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vBoxCapacity = vat.item.getGridValueByName("boxCapacity"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vPurTotalAmount = vat.item.getGridValueByName("purTotalAmount"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	//alert("vQty:"+vQty+"vBoxCapacity:"+vBoxCapacity+"box:"+vQty/vBoxCapacity);
	if(parseFloat(vQty)>parseFloat(vPurTotalAmount))
	{
		alert("需求量不可大於來源庫存");
		vat.item.setGridValueByName("quantity", nItemLine, 0);
	}
	else
	{
		if((vQty/vBoxCapacity)%1==0)
		{
			vat.item.setGridValueByName("box", nItemLine, parseFloat(vQty)/parseFloat(vBoxCapacity));
		}
		else
		{
			alert("輸入錯誤，基本量:"+vBoxCapacity+"/需求量:"+vQty);
			vat.item.setGridValueByName("quantity", nItemLine, 0);
		}
	}

		
	
}


function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoNext(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1;
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}

}
function eChangedepManager() {
   // alert(vat.item.getValueByName("#loginDepartment"));
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByDepManager" +
						"&depManager="  + vat.item.getValueByName("#F.depManager");
					
	vat.ajax.startRequest(processString, function () {

			   
			   vat.item.setValueByName("#F.depManagerName", vat.ajax.getValue("depManagerName", vat.ajax.xmlHttp.responseText))
			   vat.item.setValueByName("#F.depManager", vat.ajax.getValue("depManager", vat.ajax.xmlHttp.responseText))
	});
}
function doExport(){
	var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var beanName = ""; // standard_ie.properties檔中對應的key
	beanName = "BU_PURCHASE_EOS_EXPORT";
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var headId = vat.item.getValueByName("#F.headId");


    var url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&processObjectName=poEosService" +
              "&processObjectMethodName=exportExcelDetail" +
              "&brandCode=" + brandCode +
	          "&headId=" + headId;

    var width = "200";
    var height = "30";




		vat.block.pageDataSave( vnB_Detail8 ,{
			asyn: false,
			funcSuccess:function(){
			}
		});
	
	window.open(url, '匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function importFormData(){
	var width    = "600";
    var height   = "400";
    var headId   = vat.item.getValueByName("#F.headId");
	var shopCode =  vat.item.getValueByName("#F.department");
  

    	var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=BU_PURCHASE_EOS" +
			"&importFileType=XLS" +
	        "&processObjectName=poEosService" +
	        "&processObjectMethodName=executeImportLists" +
	        "&arguments=" + headId + "{$}" + shopCode +
	        "&parameterTypes=LONG{$}STRING" +
	        "&blockId=" + vnB_Detail8,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function doFormAccessControl()
{
	var formId = vat.bean().vatBeanOther.formId;
//	alert(typeof formId);
	if( formId != "" )
	{
		vat.item.setAttributeByName("#F.department", "readOnly", true);
		vat.item.setAttributeByName("#F.itemCategory", "readOnly", true);
		vat.item.setAttributeByName("#F.category01", "readOnly", true);
		vat.item.setAttributeByName("#F.category02", "readOnly", true);
		vat.item.setAttributeByName("#F.category03", "readOnly", true);
		vat.item.setAttributeByName("#F.department", "readOnly", true);
		vat.item.setAttributeByName("#F.isTax", "readOnly", true);
		vat.item.setAttributeByName("#F.itemBrand", "readOnly", true);
		vat.item.setAttributeByName("#F.storage", "readOnly", true);
		vat.item.setStyleByName("#B.searchList","display", "none");
		vat.item.setStyleByName("#B.itemBrand","display", "none");
		vat.item.setStyleByName("#B.depManager","display", "none");
		vat.item.setStyleByName("#B.requestCode","display", "none");
		vat.item.setStyleByName("#B.submit","display", "none");
		vat.item.setStyleByName("#B.save","display", "none");
		vat.item.setStyleByName("#B.void","display", "none");
		vat.item.setStyleByName("#B.submit1","display", "none");
		vat.block.canGridModify(vnB_Detail8, false);
		if(document.forms[0]["#userType"].value === 'warehouse' && vat.item.getValueByName("#F.status")=== 'SIGNING')
		{
			vat.item.setStyleByName("#B.submit1","display", "inline");
		}
		
	}
	else
	{
		vat.item.setStyleByName("#B.submit1","display", "none");
		if(isSelectDetail)
		{
			vat.item.setAttributeByName("#F.department", "readOnly", true);
			vat.item.setAttributeByName("#F.itemCategory", "readOnly", true);
			vat.item.setAttributeByName("#F.applicant", "readOnly", true);
		}
		else
		{
			vat.item.setAttributeByName("#F.department", "readOnly", false);
			vat.item.setAttributeByName("#F.itemCategory", "readOnly", false);
			vat.item.setAttributeByName("#F.applicant", "readOnly", false);

		}
	}
	
	vat.item.setStyleByName("#B.submitFilter","display", "none");
	vat.item.setStyleByName("#B.submitFilter1","display", "none");
	vat.item.setStyleByName("#B.submit2","display", "none");	
}



function loadBeforeAjxService()
{ 
	
			$.blockUI({
	        message: '<font size=4 color="#000000"><b>頁面載入中，請稍後...</b></font>',
	        overlayCSS: { // 遮罩的css設定
	            backgroundColor: '#eee'
	        },
	        css: { // 遮罩訊息的css設定
	            border: '3px solid #aaa',
	            width: '30%',
	            left: '35%',
	            backgroundColor: 'white',
	            opacity: '0.9' //透明度，值在0~1之間
	        }
    	});
//可用庫存清單查詢條件
  var vHeadId  		= vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '').toUpperCase();
  var vBrandCode 	= vat.item.getValueByName("#F.brandCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vWarehouseCode= vat.item.getValueByName("#F.warehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vShopCode  	= vat.item.getValueByName("#F.department").replace(/^\s+|\s+$/, '').toUpperCase();
  var vItemCategory = vat.item.getValueByName("#F.itemCategory").replace(/^\s+|\s+$/, '').toUpperCase();
  var vCategory01   = vat.item.getValueByName("#F.category01").replace(/^\s+|\s+$/, '').toUpperCase();
  var vCategory02   = vat.item.getValueByName("#F.category02").replace(/^\s+|\s+$/, '').toUpperCase();
  var vCategory02s  = vat.item.getValueByName("#F.category02s").replace(/^\s+|\s+$/, '').toUpperCase();
  var vCategory03   = vat.item.getValueByName("#F.category03").replace(/^\s+|\s+$/, '').toUpperCase();
  var vItemBrand    = vat.item.getValueByName("#F.itemBrand").replace(/^\s+|\s+$/, '').toUpperCase();
  var vItemBrands    = vat.item.getValueByName("#F.itemBrands").replace(/^\s+|\s+$/, '').toUpperCase();
  var status        = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '').toUpperCase();
  var isTax   		= vat.item.getValueByName("#F.isTax").replace(/^\s+|\s+$/, '').toUpperCase();
//排序篩選條件
  var vFilterA  	= vat.item.getValueByName("#F.filterA").replace(/^\s+|\s+$/, '').toUpperCase();
  var vFilterB		= vat.item.getValueByName("#F.filterB").replace(/^\s+|\s+$/, '').toUpperCase();
  var vFilterC1		= vat.item.getValueByName("#F.filterC1").replace(/^\s+|\s+$/, '').toUpperCase();
  var vFilterC2  	= vat.item.getValueByName("#F.filterC2").replace(/^\s+|\s+$/, '').toUpperCase();
  var vFilterD1		= vat.item.getValueByName("#F.filterD1").replace(/^\s+|\s+$/, '').toUpperCase();
  var vFilterD2  	= vat.item.getValueByName("#F.filterD2").replace(/^\s+|\s+$/, '').toUpperCase();
  var vSortCondition= vat.item.getValueByName("#F.sortCondition");
  var vSortType   	= vat.item.getValueByName("#F.sortType").replace(/^\s+|\s+$/, '').toUpperCase();

  
		var formId=vat.bean().vatBeanOther.formId;
		var processString = "process_object_name=poEosService"+
		                   "&process_object_method_name=getEosList"
		                    +"&headId=" 			+ vHeadId
		                    +"&brandCode=" 			+ vBrandCode
							+"&warehouseCode="		+ vWarehouseCode
							+"&department="			+ vShopCode
							+"&itemCategory="		+ vItemCategory
							+"&category01="			+ vCategory01
							+"&category02="			+ vCategory02
							+"&category02s="		+ vCategory02s
							+"&category03="			+ vCategory03
							+"&itemBrand="			+ vItemBrand
							+"&itemBrands="			+ vItemBrands
							+"&status=" 			+ status
							+"&isTax="				+ isTax
							+"&filterA="			+ vFilterA
							+"&filterB="			+ vFilterB
							+"&filterC1="			+ vFilterC1
							+"&filterC2="			+ vFilterC2
							+"&filterD1="			+ vFilterD1
							+"&filterD2="			+ vFilterD2
							+"&sortCondition="		+ vSortCondition
							+"&sortType="			+ vSortType;

		return processString;		

}

function doSearch(){
	if(vat.item.getValueByName("#F.itemCategory")!=""
	&&vat.item.getValueByName("#F.requestCode")!=""
	&&vat.item.getValueByName("#F.category01")!=""
	//&&vat.item.getValueByName("#F.category02")!=""
	&&vat.item.getValueByName("#F.department")!=""
	&&vat.item.getValueByName("#F.isTax")!=""
	&&vat.item.getValueByName("#F.request")!="unknow"
	
	//&&vat.item.getValueByName("#F.itemBrand")!=""
	){
	if(!(vat.item.getValueByName("#F.department")=="31250"&&vat.item.getValueByName("#F.storage")==""))
	{
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {
			                     
			                     	vat.block.pageDataLoad(vnB_Detail8 , vnCurrentPage = 1);
			                     	vat.item.setAttributeByName("#F.itemCategory", "readOnly", true);
			                     	vat.item.setAttributeByName("#F.requestCode", "readOnly", true);
			                     	vat.item.setAttributeByName("#F.category01", "readOnly", true);
			                     	vat.item.setAttributeByName("#F.category02", "readOnly", true);
			                     	vat.item.setAttributeByName("#F.category03", "readOnly", true);
			                     	vat.item.setAttributeByName("#F.department", "readOnly", true);
			                     	vat.item.setAttributeByName("#F.isTax", "readOnly", true);
			                     	vat.item.setAttributeByName("#F.itemBrand", "readOnly", true);
			                     	vat.item.setAttributeByName("#F.storage", "readOnly", true);
			                     	vat.item.setStyleByName("#B.searchList","display", "none");
			                     	vat.item.setStyleByName("#B.itemBrand","display", "none");
			                     	vat.item.setStyleByName("#B.depManager","display", "none");
			                     	vat.item.setStyleByName("#B.requestCode","display", "none");
			                     	vat.item.setStyleByName("#B.submitFilter","display", "inline");
			                     	vat.item.setStyleByName("#B.submitFilter1","display", "inline");
			                     	vat.item.setStyleByName("#B.submit2","display", "inline");

				
			                     	vat.item.setAttributeByName("#F.filterA", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.filterB", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.filterC1", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.filterC2", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.filterD1", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.filterD2", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.filterD2", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.sortCondition", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.sortType", "readOnly", false);
			                     	
			                     	
	 	

			                     	vat.item.setAttributeByName("#F.itemCode1", "readOnly", false);
			                     	vat.item.setAttributeByName("#F.requestQty1", "readOnly", false);
									vat.block.canGridModify(vnB_Detail8, true);
									vat.tabm.displayToggle(0, "xTab18", true);//按下後直接切換至明細頁簽
			                     	
			                     	
			                     	}
			                    });
		}
		else
		{
			alert("3125請填入儲區後再操作");
		} 
	}
	else
	{
		//alert("業種/大類/中類/稅別/商品品牌/店別未填入，請填入後再操作");
		alert("業種/大類/稅別/店別/需求人員未填入，請填入後再操作");
	}
}

function doPassData(id){
	var suffix = "";
	switch(id){
		case "serialPicker":
			suffix += "&formId="+escape(vat.item.getValueByName("#H.itemId"))+"&itemPicker=true"; 
			break;
		case "CATEGORY13":
			suffix += "&categoryType="+escape("CATEGORY13");
			break;
		case "ItemBrand":
			suffix += "&categoryType="+escape("ItemBrand");
			break;
	}
//	alert(suffix);
	return suffix;
}

// picker 回來執行
function doAfterPickerFunctionProcess(key){
	//do picker back something

	switch(key){
		case "itemBrand":
		
			if(typeof vat.bean().vatBeanPicker.result != "undefined"){
			   	vat.item.setValueByName("#F.itemBrand", vat.bean().vatBeanPicker.result[0]["id.categoryCode"]); 
				changeCategoryCodeName("ItemBrand");

			}
		break;
		
	}
	
}
// 動態改變商品類別名稱
function changeCategoryCodeName(code){
	var condition = "", name ="";

	switch(code){
		case "ItemBrand":
			
			condition =  "&categoryCode=" + ( "ItemBrand" === code ? vat.item.getValueByName("#F.itemBrand") : "" );
			name = "#F.itemBrandName";
		break;
	}
	
	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXCategoryName"+
                  "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                  "&categoryType=" + code + condition,
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName(name, vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		vat.item.setValueByName(name, "查無此類別");
		}   
	});	
}


function controler()
{
	var nItemLine = vat.item.getGridLine();
	var vLoginBrandCode = document.forms[0]["#loginBrandCode" 	 ].value;
	var vOrderTypeCode = document.forms[0]["#orderTypeCode" 	 ].value;
	var vOrderNo = vat.item.getValueByName("#F.orderNo");
	var vItemNo = vat.item.getGridValueByName("itemNo" , nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vEnable = vat.item.getGridValueByName("enable" , nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vLineId = vat.item.getGridValueByName("lineId" , nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var alertMessage ="是否確定送出?";
	if(confirm(alertMessage))
	{
			if(vEnable === "Y")
			{
				alert("此項明細已送出,請勿重複送出");
			}
			else
			{
				vEnable = "Y";
				vat.item.setGridValueByName("enable", nItemLine, "Y");

				vat.ajax.XHRequest({
				post:"process_object_name=poEosService&process_object_method_name=updateStatus"+
						  "&brandCode=" +  vLoginBrandCode+
		                  "&orderTypeCode=" +  vOrderTypeCode+
		                  "&orderNo=" +  vOrderNo+
		                   "&itemNo=" +  vItemNo+
		                    "&enable=" +  vEnable+
		                    "&lineId=" +  vLineId ,
		
		
				find: function change(oXHR){
		         		vat.block.pageRefresh(vnB_Detail8);
				},
				fail: function changeError(){
						vat.block.pageRefresh(vnB_Detail8);
				}  
		       });
	       	}

	}
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
/*
function changeAppend(category){
	if(category==='category02'){
		   vat.ajax.XHRequest({
		          asyn:false,
		          post:"process_object_name=poEosService"+
		                   "&process_object_method_name=updateCategory02s"
		                    +"&function="+ "append"
							+"&category02="+ vat.item.getValueByName("#F.category02")
							+"&category02s="+ vat.item.getValueByName("#F.category02s"),
		          find: function change(oXHR){ 
		          		//alert("test");
					vat.item.setValueByName("#F.category02s",vat.ajax.getValue("category02s" , oXHR.responseText));
		          } 
			});	
	}
	else if(category==='itemBrand')
	{
		   vat.ajax.XHRequest({
		          asyn:false,
		          post:"process_object_name=poEosService"+
		                   "&process_object_method_name=updateItemBrands"
		                   	+"&function="+ "append"
							+"&itemBrand="+ vat.item.getValueByName("#F.itemBrand")
							+"&itemBrands="+ vat.item.getValueByName("#F.itemBrands"),
		          find: function change(oXHR){ 
		          		//alert("test");
					vat.item.setValueByName("#F.itemBrands",vat.ajax.getValue("itemBrands" , oXHR.responseText));	
		          } 
			});	
	}
}
*/
function changeAppend(category){

	var insertInput = vat.item.getValueByName("#F."+category).replace(/^\s+|\s+$/, '');

	if("" != insertInput){
		
		var inputList = vat.item.getValueByName("#F."+category+"s");
		var count = inputList.split(","); 
		//alert(count);
		if( "" == inputList && count.length == 1)
		{
			inputList = inputList + "'" + insertInput + "'";
		}
		else
		{
		
			if(insertInput==='A02')
			{
				alert("A02-雪茄為特殊中類，請單獨叫貨");
			}
			else if(inputList.match('A02')!=null)
			{
				alert("A02-雪茄為特殊中類，請單獨叫貨");
			}
			else
			{
				inputList = inputList + ",'" + insertInput+ "'";
			}
			
		}
		vat.item.setValueByName("#F."+category, "");
		vat.item.setValueByName("#F."+category+"s", inputList);
	}
}
function changeDecrease(category){
	
	var insertInput = vat.item.getValueByName("#F."+category).replace(/^\s+|\s+$/, '');

	if("" != insertInput){

		var inputList = vat.item.getValueByName("#F."+category+"s");
		
		var array = inputList.split(",");
		for(var i = 0; i < array.length; i++){
			if(array[i] ==("'"+ insertInput+"'")){
				array.splice(i,1);
				//break;
			}
		}
		vat.item.setValueByName("#F."+category+"s", array);
		vat.item.setValueByName("#F."+category, "");
	}
}

function changeItemCode1(){

		   vat.ajax.XHRequest({
		          asyn:false,
		          post:"process_object_name=poEosService"+
		                   "&process_object_method_name=updateSingleInsert"
		                   +"&function=search"
							+"&itemCode="+ vat.item.getValueByName("#F.itemCode1")
							+"&itemCategory="+ vat.item.getValueByName("#F.itemCategory")
							+"&category01="+ vat.item.getValueByName("#F.category01")
							+"&category02="+ vat.item.getValueByName("#F.category02s")
							+"&isTax="+ vat.item.getValueByName("#F.isTax")
							+"&warehouseCode="+ vat.item.getValueByName("#F.warehouseCode")
							+"&brandCode="+ vat.item.getValueByName("#F.brandCode"),
		          find: function change(oXHR){ 
		          		//alert("test");
						vat.item.setValueByName("#F.sorceWarehouse1",vat.ajax.getValue("sorceWarehouse" , oXHR.responseText));
						vat.item.setValueByName("#F.boxCapacity1",vat.ajax.getValue("boxCapacity" , oXHR.responseText));
						vat.item.setValueByName("#F.warehouseQty1",vat.ajax.getValue("warehouseQty" , oXHR.responseText));
						vat.item.setValueByName("#F.msg1",vat.ajax.getValue("msg1" , oXHR.responseText));
		          } 
			});	
}


//單筆送出
function doSingleInsert(){
	var isOverQty = parseFloat(vat.item.getValueByName("#F.warehouseQty1")) < parseFloat(vat.item.getValueByName("#F.requestQty1"));
	var isFloat = parseFloat(vat.item.getValueByName("#F.requestQty1"))/parseFloat(vat.item.getValueByName("#F.boxCapacity1"))%1==0;
	
	//alert(isOverQty);
	if(!isOverQty)
	{
	if(vat.item.getValueByName("#F.requestQty1")!=""
		||vat.item.getValueByName("#F.warehouseQty1")!=""
		||vat.item.getValueByName("#F.sorceWarehouse1")!=""
		||vat.item.getValueByName("#F.boxCapacity1")!=""){
		
			if(isFloat)
			{
			var request=0;
			var itemCode="";
			var lineId = 0;
			$.blockUI({
	        message: '<font size=4 color="#000000"><b>資料送出中，請稍候</b></font>',
	        overlayCSS: { // 遮罩的css設定
	            backgroundColor: '#eee'
	        },
	        css: { // 遮罩訊息的css設定
	            border: '3px solid #aaa',
	            width: '30%',
	            left: '35%',
	            backgroundColor: 'white',
	            opacity: '0.9' //透明度，值在0~1之間
	        }
    	});	

		 				vat.ajax.XHRequest(
		 				{
				          asyn:false,
				          post:"process_object_name=poEosService"+
				                   "&process_object_method_name=updateSingleInsert"
				                   +"&function=insert"
				                   +"&headId="+ vat.item.getValueByName("#F.headId")
									+"&itemCode="+ vat.item.getValueByName("#F.itemCode1")
									+"&itemCategory="+ vat.item.getValueByName("#F.itemCategory")
									+"&category01="+ vat.item.getValueByName("#F.category01")
									+"&category02="+ vat.item.getValueByName("#F.category02s")
									+"&isTax="+ vat.item.getValueByName("#F.isTax")
									+"&status="+ vat.item.getValueByName("#F.status")
									+"&warehouseCode="+ vat.item.getValueByName("#F.warehouseCode")
									+"&itemBrand="+ vat.item.getValueByName("#F.itemBrands")
									+"&quantity="+ vat.item.getValueByName("#F.requestQty1")
									+"&brandCode="+ vat.item.getValueByName("#F.brandCode"),
				          find: function change(oXHR)
				          { 
				          		
								vat.item.setValueByName("#F.sorceWarehouse1","");
								vat.item.setValueByName("#F.warehouseQty1","");
								vat.item.setValueByName("#F.requestQty1","");
								vat.item.setValueByName("#F.boxCapacity1","");
								vat.item.setValueByName("#F.itemCode1","");
								vat.item.setValueByName("#F.msg1",vat.ajax.getValue("msg1" , oXHR.responseText));
								request = vat.ajax.getValue("requestQty" , oXHR.responseText);
								itemCode = vat.ajax.getValue("itemCode" , oXHR.responseText);
								lineId = vat.ajax.getValue("lineId" , oXHR.responseText);

		          		  } 

			});
			//alert(itemCode+" "+request+"已送出");		
			setTimeout(function(){changeQty1(request,itemCode,lineId)},1500);
		}
		else
		{
			alert("輸入錯誤，基本量:"+vat.item.getValueByName("#F.boxCapacity1")+"/需求量:"+vat.item.getValueByName("#F.requestQty1"));
		}
	}
	else{
		alert("請輸入品號/需求量");		
	}
	}
	else{
		alert("庫存不足，請重新輸入");
	}
}

function changeQty1(request,itemCode,lineId){
		 									//alert(itemCode+" "+request+"已送出");		
								for(var i=1 ; i<=10 ; i++)
								{
									//alert(vat.ajax.getValue("itemCode" , oXHR.responseText)+" "+request+"已送出");
									if(itemCode=== vat.item.getGridValueByName("itemNo", i))
									{
										
										if(request!=0)
										{
											vat.item.setGridValueByName("quantity", i,request);
											vat.item.setGridValueByName("lineId", i,lineId);
										}
									}
								}
		 						$.unblockUI();
		 			}
		 			
		 			
function doPassHeadData(){
  var suffix = "&userType="+document.forms[0]["#userType"].value+"&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode");

  return suffix;
}
		 			
		 			
function reconfirmList(){
var randomString = randomWord(true, 10, 10);
		if(confirm('EOS訂貨單列印')){	
			url = "http://10.1.94.161:8080/crystal/t2/test/crm9999.rpt?cypto="+randomString +"&prompt0="+vat.item.getValueByName("#F.headId");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
	
	function randomWord(randomFlag, min, max){
    var str = "",
        range = min,
        arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
 

    if(randomFlag){
        range = Math.round(Math.random() * (max-min)) + min;
    }
    for(var i=0; i<range; i++){
        pos = Math.round(Math.random() * (arr.length-1));
        str += arr[pos];
    }
    return str;
}


function refreshFromWithHeadId(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber  <= vat.bean().vatBeanOther.lastRecordNumber){
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber-1].headId;
	  	refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}

}