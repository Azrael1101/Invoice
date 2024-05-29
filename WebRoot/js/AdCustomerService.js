/*** 
 *	檔案: buGoalItemCount.js
 *	說明：商品折數,抽成率維護
 */
 
 /*** 
 *	檔案: buCountry.js
 *	說明：表單明細
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_master = 2;
var vnB_master2 = 3;
var vnB_master3 = 4;
var vnB_Detail = 5;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
 	doFormAccessControl();
		
		
		if (typeof vat.tabm != 'undefined') {
				vat.tabm.createTab(0, "vatTabSpan", "H", "float");
				vat.tabm.createButton(0 ,"xTab1","服務資訊"     ,"vatBlock_Master" ,"images/tab_CS_dark.GIF" ,"images/tab_CS_light.GIF", false);
				vat.tabm.createButton(0 ,"xTab2","顧客資訊"     ,"vatBlock_Master1" ,"images/tab_CU_dark.GIF" ,"images/tab_CU_light.GIF", false); 
				vat.tabm.createButton(0 ,"xTab3","購買資訊"     ,"vatBlock_Master2" ,"images/tab_PR_dark.GIF" ,"images/tab_PR_light.GIF", false);
				vat.tabm.createButton(0 ,"xTab4","購買資訊"     ,"vatDetailDiv" ,"images/tab_PRESS_dark.GIF" ,"images/tab_PRESS_light.GIF", false); 
											}
				kweMaster();	
				kweMaster2();
				kweMaster3();
				kweDetail();							
       

}												

function formInitial(){
 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          brandCode  		    : document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          orderTypeCode  	    : document.forms[0]["#orderTypeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId      	: document.forms[0]["#assignmentId"      ].value,
          beforeStatus			: "beforestatus",
	      nextStatus			: "nextstatus",
	      approvalResult        : "",
	      approvalComment		:"",
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=adCustomerServiceAction&process_object_method_name=performInitial"; 
	    	},{
	    		other: true
	    	}
	    );
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
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									servicePassData:function()//我要帶什麼樣的參數到什麼service
										  	{return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode")},
	 									 service:"AD_Customer_Service:search:20140331.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			//{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print"       , type:"IMG"    ,value:"客服列印",   src:"./images/button_form_print.gif" , eClick:'reconfirmImmovement()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print2"       , type:"IMG"    ,value:"個資版" ,   src:"./images/adcustprint.png" , eClick:'reconfirmImmovement2()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);

}



function headerInitial(){ 
	var allstatus   = [[true, true,true,true,true,true],  ["暫存", "結案", "關檔","作廢"],["SAVE","FINISH","CLOSE","VOID"]];
	var alltitle	= [["", "", false], ["先生","小姐", "Mr.","Ms."],["先生","小姐", "Mr.","Ms."]];
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"客服單", 
		rows:[
	{row_style:"", cols:[

				{items:[{name:"#L.brandCode"    , type:"LABEL" , value:"品牌/<font color='red'></font>"},
						{name:"#L.orderTypeCode", type:"LABEL" , value:"單別/<font color='red'></font>"},
						{name:"#L.orderNo"      , type:"LABEL" , value:"單號<font color='red'></font>"}]},
				{items:[{name:"#F.brandCode",     type:"TEXT", bind:"brandCode", size:3, maxLen:3, mode:"READONLY"},
						{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", size:3, maxLen:3,mode:"READONLY" },
						{name:"#F.orderNo",       type:"TEXT", bind:"orderNo", size:25, maxLen:25,mode:"READONLY" },
						{name:"#F.headId"      ,  type:"TEXT", bind:"headId" ,  size:10, back:false, mode:"READONLY"}]},
				{items:[{name:"#L.requestCode",   type:"LABEL", value:"顧客名稱<font color='red'>*</font>"}]},
				{items:[{name:"#F.requestCode",   type:"TEXT", bind:"requestCode", size:25, maxLen:25,mode:"READONLY" },{name:"#F.title", bind:"title" ,style:"background-color=yellow" , type:"SELECT" ,init:alltitle}]},
				{items:[{name:"#L.requestDate",   type:"LABEL",  value:"立案日期<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.requestDate",   type:"Date",   bind:"requestDate",         size:10}]},
	 			{items:[{name:"#L.endDate",   type:"LABEL",  value:"結案日期<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.endDate",   type:"Date",   bind:"endDate",         size:10}]},
				{items:[{name:"#L.status", type:"LABEL" , value:"狀態"}]},
				{items:[{name:"#F.status", type:"SELECT", bind:"status",size:25, maxLen:25,init:allstatus}]}
				
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}



function kweMaster(){
	var allmaintainGiven 		= vat.bean("allmaintainGiven");
	var allmaintainReceive      = vat.bean("allmaintainReceive");
	var allexceptional			= [["", "", false], ["請選擇","有", "無"],["","1","2"]];
	var allrefound  			= [["", "", false], ["請選擇","有", "無"],["","1","2"]];
	var allclosed     			= vat.bean("allclosed"); 
	var allcustomerRequest      = vat.bean("allcustomerRequest"); 
	var allrequestSource      	= vat.bean("allrequestSource"); 
	var allCategroyTypes      	= vat.bean("allCategroyTypes"); 
	var allproject    			= vat.bean("allproject"); 
 	var allsystem     			= vat.bean("allsystem"); 
    var alldepartment   		= vat.bean("alldepartment");
    var allmainTainExpense      = vat.bean("allmainTainExpense");
    var allcategoryType			= vat.bean("allcategoryType");
	vat.block.create(vnB_master, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3'", rows:[
	{row_style:"", cols:[	
		{items:[{name:"#L.categoryGroup"   ,type:"LABEL" 		,value:"類型/<font color='red'></font>"},
				{name:"#L.categoryItem"    ,type:"LABEL" 		,value:"原因/<font color='red'></font>"},
				{name:"#L.categorySystem"  ,type:"LABEL" 		,value:"層級<font color='red'></font>"}]},
		{items:[{name:"#F.categoryGroup"   ,type:"SELECT"		,bind :"categoryGroup" ,init:allCategroyTypes,eChange:function(){changeCategory();}},
				{name:"#F.categoryItem"    ,type:"SELECT" 		,bind :"categoryItem"  ,init:allproject},
				{name:"#F.categorySystem"  ,type:"SELECT"		,bind :"categorySystem",init:allsystem},
				{name:"#F.itemCategory"  ,type:"hidden"		    ,bind :"itemCategory"  ,init:allsystem,value:"CSF"}],td:"colSpan=6"}]},
	{row_style : "", cols:[	
		{items:[{name : "#L.description"   ,type:"LABEL"        , value : "案件描述", row : 6, col : 50}], td:"rowSpan=4"},
		{items:[{name : "#F.description"   ,type:"TEXT"     , bind : "description", size : 198, maxLen : 87 }], td: "rowSpan=4"}]},
	{row_style : "", cols:[	
		{items:[{name : "#L.depManager", type : "LABEL", value : "客服主管<font color='red'>*</font>"}]},
		{items:[{name : "#F.depManager", type : "TEXT", bind : "depManager", size :10,mask:"Aaaaaaaaaa" ,maxLen : 25,eChange:"eChangedepManager()"},
				{name : "#B.depManager",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee1();}},
			    {name : "#F.depManagerName", type : "TEXT", bind : "depManagerName", size :10, maxLen : 25,mode:"READONLY"}],td:" colSpan=4"}]},
	{row_style : "", cols:[	
		{items :[{name : "#L.createdBy", type : "LABEL", value : "客服人員<font color='red'></font>"}]},
		{items :[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25,eChange:"getEmployeeInfo('createdBy')"},
				 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}],td:" colSpan=4"}]},
	{row_style : "", cols:[	
		{items :[{name : "#L.creationDate", type : "LABEL", value : "填單日期<font color='red'></font>"}]},
		{items :[{name:"#F.creationDate", type:"DATE", bind:"creationDate", size:10, maxLen:25 ,mode:"READONLY"}],td:" colSpan=4"}]},
	{row_style:"", cols:[
		{items:[{name:"#L.department", type:"LABEL" , value:"部門/<font color='red'></font>"},
				{name:"#L.categoryType", type:"LABEL" , value:"業種<font color='red'></font>"},
				{name:"#L.categoryTypeOther", type:"LABEL" , value:"備註<font color='red'></font>"}
				]},//{name:"#L.brandCode", type:"LABEL" , value:"品牌<font color='red'></font>"}
		{items:[{name:"#F.department", type:"SELECT", bind:"department",init:alldepartment,eChange:function(){changeDepCategory();}},//eChange:"changeItemSubcategory()" 
				{name:"#F.categoryType", type:"SELECT",bind:"categoryType",init:allcategoryType},
				{name:"#F.categoryTypeOther", type:"TEXT",bind:"categoryTypeOther"}
				]},//{name:"#F.brandCode", type:"TEXT", bind:"brandCode"}
		{items:[{name:"#L.requestSource", type:"LABEL",  value:"通報來源"}]},	 
	 	{items:[{name:"#F.requestSource",type:"SELECT", bind:"requestSource",init:allrequestSource},
	 			{name:"#F.requestSourceOther", type:"TEXT", bind:"requestSourceOther"}],td:" colSpan=4"}]},
	{row_style:"", cols:[ 
	 	{items:[{name:"#L.customerRequest"          , type:"LABEL" , value:"顧客訴求"}]},
	 	{items:[{name:"#F.customerRequest"     , type:"SELECT"  ,bind:"customerRequest",init:allcustomerRequest ,size:13},
	 			{name:"#F.customerRequestOther"       , type:"TEXT", bind:"customerRequestOther"  , size:6, maxLen:4}]},
	 	{items:[{name:"#L.closed", type:"LABEL", value:"結案方式<font color='red'></font>"}]},
		{items:[{name:"#F.closed", type:"SELECT",bind:"closed", size:10,init:allclosed,maxLen:25}
				],td:" colSpan=4"}]},
				
	{row_style:"", cols:[
	 	{items:[{name:"#L.refound"          , type:"LABEL" , value:"退款金額"}]},
	 	{items:[{name:"#F.refound"     		, type:"SELECT" ,bind:"refound",init:allrefound, size:13},
	 			{name:"#F.refoundOther"     , type:"TEXT"  ,  bind:"refoundOther"  , size:13}]},
	 	{items:[{name:"#L.exceptional"		, type:"LABEL"	, value:"異常單<font color='red'></font>"}]},
		{items:[{name:"#F.exceptional"		, type:"SELECT", bind:"exceptional",init:allexceptional,size:10, maxLen:25},
				{name:"#F.exceptionalOther" , type:"TEXT"  ,  bind:"exceptionalOther"  , size:13}],td:" colSpan=4"}]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.maintainReceive"   , type:"LABEL" , value:"維修收件"}]},
	 	{items:[{name:"#F.maintainReceive"   , type:"SELECT" ,bind:"maintainReceive",init:allmaintainReceive , size:13}]},
	 	{items:[{name:"#L.maintainGiven"     , type:"LABEL" , value:"維修取件"}]},
	 	{items:[{name:"#F.maintainGiven"     , type:"SELECT" ,bind:"maintainGiven" ,init:allmaintainGiven, size:13}]},
	 	{items:[{name:"#L.mainTainExpense"	 , type:"LABEL", value:"維修費用"}]},
		{items:[{name:"#F.mainTainExpense"	 , type:"SELECT", bind:"mainTainExpense",size:10,init:allmainTainExpense, maxLen:25},
				{name:"#F.closedOther", type:"TEXT",bind:"closedOther"}
		]}]},
	{row_style:"", cols:[
	 	{items:[{name:"#L.autoMail"   , type:"LABEL" , value:"Mail to"}]},
	 	{items:[{name:"#F.autoMail"   , type:"TEXT" ,bind:"autoMail",mask:"Aaaaaaa",size:50},
	 			{name:"#L.autoMail"   , type:"LABEL" , value:"@tasameng.com.tw"}],td:" colSpan=5"}]}
			],	

		beginService: "",
		closeService: ""
	});
	}
	
function kweMaster2(){
    var allcustomerSex          = vat.bean("allcustomerSex"); 
    var allnationality          = vat.bean("allnationality"); 
    var allvipType              = vat.bean("allvipType");
	vat.block.create(vnB_master2, {

	id: "vatBlock_Master1", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.customerLastName", type:"LABEL",  value:"顧客姓"}]},	 
	 		{items:[{name:"#F.customerLastName", bind:"customerLastName",type:"TEXT",   size:20 ,maxLen:20,eChange:"modReqCode()" }]},		 
	 		{items:[{name:"#L.customerFristName",         type:"LABEL",  value:"顧客名"}]},	 
			{items:[{name:"#F.customerFristName",    bind:"customerFristName" , type:"TEXT", size:20 ,maxLen:20}]},
			{items:[{name:"#L.customerSex",         type:"LABEL",  value:"性別"}]},	 
	 		{items:[{name:"#F.customerSex",    bind:"customerSex",type:"SELECT",init:allcustomerSex, size:12}]}
	 		]},
	 		
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.nationality", type:"LABEL",  value:"國籍"}]},	 
	 		{items:[{name:"#F.nationality", bind:"nationality",type:"SELECT", init:allnationality,size:12,eChange:"onCustomerName()"},
	 				{name:"#F.nationalityOther", bind:"nationalityOther",type:"TEXT",  size:20 , maxLen:20}]},		 
	 		{items:[{name:"#L.contactMobile",         type:"LABEL",  value:"聯絡電話-M"}]},	 
			{items:[{name:"#F.contactMobile",       bind:"contactMobile",  type:"TEXT",  size:12}]},
			{items:[{name:"#L.contactHome",         type:"LABEL",  value:"聯絡電話-H"}]},	 
	 		{items:[{name:"#F.contactHome",        bind:"contactHome",  type:"TEXT", size:12}]}
	 		]},
	 		
	 	{row_style:"", cols:[
			{items:[{name:"#L.contactWay",         type:"LABEL",  value:"聯絡方式"}]},	 
			{items:[{name:"#F.contactWay",         type:"TEXT", bind:"contactWay",  size:150 ,maxLen:100}],td:" colSpan=7"}
	 		]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.email",         type:"LABEL",  value:"郵件地址"}]},	 
			{items:[{name:"#F.email",         type:"TEXT", bind:"email",  size:150 ,maxLen:100}],td:" colSpan=7"}
	 		]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.VIPType", type:"LABEL",  value:"貴賓卡"}]},	 
	 		{items:[{name:"#F.VIPType", type:"SELECT",bind:"VIPType",init:allvipType , size:12}]},		 
	 		{items:[{name:"#L.customerCode",         type:"LABEL",  value:"會員編號"}]},	 
			{items:[{name:"#F.customerCode",     bind:"customerCode"    ,type:"TEXT", mask:"Aaaaaaaaaa" ,size:12}],td:" colSpan=6"}
	 		]}
	 
 	 	],
		beginService:"",
		closeService:""
	});
	}

function modReqCode(){

    vat.item.setValueByName("#F.requestCode"    , vat.item.getValueByName("#F.customerLastName"));

}
	
function kweMaster3(){
    var allpaymentType              = vat.bean("allpaymentType");
    var allShopMachines				= vat.bean("allShopMachines");
    //var allWarehouse				= vat.bean("allWarehouse");
    var alltype              		= vat.bean("alltype");
    var alldiscount                 = vat.bean("alldiscount");
    var isPurchaseDate               = [["", "", false], ["請選擇","有", "無"],["","1","2"]];
    var allisDelivery               = [["", "", false], ["請選擇","有", "無"],["","1","2"]];
    var allisCustomerPoNo     		= [["", "", false], ["請選擇","紅單","白單","無"],["","1","2","3"]];
    var allisGiht     				= [["", "", false], ["請選擇","有", "無"],["","1","2"]];
	vat.block.create(vnB_master3, {

	id: "vatBlock_Master2", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.customerPoNo", type:"LABEL",  value:"銷貨單號"}]},	 
	 		{items:[{name:"#F.customerPoNo", bind:"customerPoNo",type:"TEXT", size:12,eChange:"onChangePoNo()"}]},		 
	 		{items:[{name:"#L.salesOrderDate",         type:"LABEL",  value:"購買日期"}]},	 
			{items:[{name:"#F.salesOrderDate",     bind:"saleOrderDate"  ,  type:"TEXT",  size:12}]},
			{items:[{name:"#L.posMachineCode",         type:"LABEL",  value:"機台"}]},	 
	 		{items:[{name:"#F.posMachineCode",      bind:"posMachineCode" ,  type:"TEXT"}]},
	 		{items:[{name:"#L.warehuseCode",         type:"LABEL",  value:"庫別"}]},	 
	 		{items:[{name:"#F.warehuseCode",     bind:"warehuseCode"   , type:"TEXT"}],td:" colSpan=2"}
	 		]},
	 		
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.itemCode", type:"LABEL",  value:"品號/"},
	 				{name:"#L.itemBrand", type:"LABEL", value:"品牌代號"}]},	 
	 		{items:[{name:"#F.itemCode", bind:"itemCode",type:"TEXT",  size:12,eChange:"onChangeItemCode()"},
	 				{name:"#F.itemBrand", bind:"itemBrand",type:"TEXT",  size:5,mode:"READONLY"}]},		 
	 		{items:[{name:"#L.itemName",  type:"LABEL",  value:"品名"}]},	 
			{items:[{name:"#F.itemName",   bind:"itemName"   ,   type:"TEXT" ,mode:"READONLY",  size:20}]},
			{items:[{name:"#L.monetaryAmount",         type:"LABEL",  value:"購買金額"}]},	 
	 		{items:[{name:"#F.monetaryAmount",     bind:"actualSalesAmount"  ,  type:"NUMM",   size:12},
	 				{name:"#F.monetaryAmountOther",     bind:"actualSalesAmountOther"  ,  type:"NUMM",   size:12}]},
	 		{items:[{name:"#L.isGiht",         type:"LABEL",  value:"贈品"}]},	 
	 		{items:[{name:"#F.isGiht",         type:"SELECT",   bind:"isGiht",init:allisGiht}],td:" colSpan=2"}
	 		]},
	 		
	 	{row_style:"", cols:[
			{items:[{name:"#L.paymentType",         type:"LABEL",  value:"付款方式"}]},	 
			{items:[{name:"#F.paymentType",         type:"SELECT", bind:"paymentType", size:12,init:allpaymentType,eChange:function(){changePayCategory();}},
					{name:"#F.paymentKind",         type:"SELECT", bind:"paymentKind",  size:12,init:alltype}],td:" colSpan=4"},
			{items:[{name:"#L.discount",         type:"LABEL",   value:"折扣"}]},	 
			{items:[{name:"#F.discount",         type:"SELECT",bind:"discount" ,init:alldiscount},
					{name:"#F.discountOther",         type:"TEXT", bind:"discountOther",  size:12}],td:" colSpan=4"}
	 		]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.isDelivery"		, type:"LABEL",  value:"入提"},
	 				{name:"#L.isDeliveryDate"   , type:"LABEL",  value:"/提貨日期"}]},	 
	 		{items:[{name:"#F.isDeliveryDate", bind:"deliveryDate",type:"TEXT"}],td:" colSpan=4"},		 
	 		{items:[{name:"#L.isCustomerPoNo",         type:"LABEL",  value:"銷貨單種類"}]},	 
			{items:[{name:"#F.isCustomerPoNo",     bind:"isCustomerPoNo",    type:"SELECT", init:allisCustomerPoNo}]},
			{items:[{name:"#L.superintendentCode",         type:"LABEL",  value:"責任人員"}]},	 
	 		{items:[{name:"#F.superintendentCode",         type:"TEXT",   bind:"superintendentCode", size:12}]}
	 		]}
 	 	],
		beginService:"",
		closeService:""
	});
	}



function checkLength(which){
  var vLineId                   = vat.item.getGridLine();
  var memo = vat.item.getGridValueByName("memo" , vLineId);
  
  var max = 130;
  if(memo.length>max)
    memo = memo.substring(0,max);
    var curr = max - memo.length;
    //alert(curr);
    vat.item.setGridValueByName("txtCount", vLineId, curr);
  
}




	
function kweDetail(){
    var vbCanGridDelete = true;
    var vbCanGridAppend = true;
    var vbCanGridModify = true;
    
    
    vat.item.make(vnB_Detail, "indexNo"                  , {type:"IDX"   	  , size: '2%'         ,desc:"序號"       });
    vat.item.make(vnB_Detail, "recordDate"              , {type:"TEXT"   ,size:'10'   ,desc:"日期"});
	vat.item.make(vnB_Detail, "memo"             		 , {type:"TEXT"   ,size:'290'  ,desc:"處理內容記錄",maxLen:"130" ,eFocus:function(){checkLength(this);},eChange:function(){checkLength(this);}});
	vat.item.make(vnB_Detail, "isDeleteRecord"           , {type:"DEL"                 ,desc:"刪除"});
	vat.item.make(vnB_Detail, "txtCount"             		 , {type:"TEXT"     ,desc:"剩餘字數", mode:"READONLY" });
	vat.item.make(vnB_Detail, "lineId"                   , {type:"HIDDEN"              ,desc:"lineId"       });
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                        loadBeforeAjxService: "loadBeforeAjxService()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														blockId             : "5"
														});               
														vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);		
}

function loadBeforeAjxService(){
	//alert("載入之後");
	
	var processString = "process_object_name=adCustomerServiceService&process_object_method_name=getAJAXPageData" +
	                    "&brandCode=" + vat.bean().vatBeanOther.brandCode +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode");
	return processString;		
}
function saveBeforeAjxService() {
		//alert("存檔之後");
	var processString = "";

    processString = "process_object_name=adCustomerServiceService&process_object_method_name=updateAJAXPageLinesDataAdCust" +
						"&headId=" + vat.item.getValueByName("#F.headId")+
						"&status=" + vat.item.getValueByName("#F.status")+
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode;

	return processString;
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
		vat.item.setValueByName("#L.maxRecord"    , "0");
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

	var formId               = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var processId            = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var status               = vat.item.getValueByName("#F.status");
    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	var approvalResult  = vat.item.getValueByName("#F.approvalResult");
	var alertMessage ="是否確定送出?";
		var alertMessage ="是否確定送出?";
   /* if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }*/
    var formStatus = status;
    if("SUBMIT" == formAction){
        formStatus = "SAVE";
    }else if("VOID" == formAction){
        formStatus = "VOID";
    }	
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}
	
	
	
	
	if(confirm(alertMessage)){
	   /// vat.bean().vatBeanOther.formAction = formAction;
	    				vat.bean().vatBeanOther.formAction = formAction;
	    				vat.bean().vatBeanOther.approvalResult = approvalResult;	
	    				vat.bean().vatBeanOther.approvalComment = approvalComment;
	    				vat.bean().vatBeanOther.formStatus = formStatus;	
	                    vat.block.pageDataSave(vnB_Detail　　,
	                    {  	
	                       funcSuccess:function(){
							      vat.block.submit(function(){
							        vat.bean().vatBeanOther.formAction = formAction;
								 	 return "process_object_name=adCustomerServiceAction&process_object_method_name=performTransaction";
							      }, 
							      {
							             bind:true, link:true, other:true,
						  					funcSuccess: function () {
				               
				                            } 
						          });
					
					        }
					     });
				    }
				    
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){

	if(vat.bean().vatBeanPicker.result != null){
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
		  refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}

}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 上一筆
function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 下一筆
function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 最後一筆
function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"            ].value = code; 
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=adCustomerServiceAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
     			vat.block.pageRefresh(vnB_Detail);
				doFormAccessControl();
     	}}
    );
    	
}

// 依formId鎖form

function doFormAccessControl(){
	//alert("doFormAccessControl");
	var orderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");
	var status   		= vat.item.getValueByName("#F.status");
	var loginEmployeeCode   		= document.forms[0]["#loginEmployeeCode"].value;
     	
     	
     			// 查詢回來
			if( status == "CLOSE"&(loginEmployeeCode=="T28276"||loginEmployeeCode=="T11614")){
				vat.item.setStyleByName("#B.submit", 	"display", "inline"); 
				vat.item.setStyleByName("#B.void", 	"display", "inline"); 
				vat.item.setAttributeByName("#F.status", "readOnly", false);
				
				
			}else if(status == "CLOSE"){
			   
			     vat.item.setAttributeByName("#F.categoryGroup"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.categoryItem"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.categorySystem"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.requestDate"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.description"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.depManager"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.depManagerName"    , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.createdBy"    		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.createdByName"     , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.creationDate"      , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.department"        , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.categoryType"      , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.requestSource"     , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.requestSourceOther", "readOnly", true);
		     	 vat.item.setAttributeByName("#F.customerRequest"   , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.closed"			, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.closedOther"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.refound"			, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.refoundOther"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.exceptional"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.exceptionalOther"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.maintainReceive"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.maintainGiven"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.mainTainExpense"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.customerLastName"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.customerFristName"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.customerSex"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.nationality"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.nationalityOther"  , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.contactMobile"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.contactHome"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.contactWay"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.email"				, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.VIPType"			, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.customerCode"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.customerPoNo"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.salesOrderDate"    , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.posMachineCode"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.warehuseCode"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.itemCode"			, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.itemName"			, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.monetaryAmount"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.isGiht"			, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.paymentType"	    , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.paymentKind"	    , "readOnly", true);
		     	 vat.item.setAttributeByName("#F.discount"			, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.discountOther"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.isDelivery"		, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.isDeliveryDate"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.isCustomerPoNo"	, "readOnly", true);
		     	 vat.item.setAttributeByName("#F.superintendentCode", "readOnly", true);
		     	 vat.item.setGridAttributeByName("executeDate"		, "readOnly", true);
		     	 vat.item.setGridAttributeByName("memo"				, "readOnly", true);
		     	 vat.item.setStyleByName("#B.submit", 	"display", "none"); 
				 vat.item.setStyleByName("#B.void", 	"display", "none"); 
				 vat.item.setAttributeByName("#F.status", "readOnly", true);
			     vat.block.canGridModify([vnB_Detail], false,false,false);
			}	
}
function eChangedepManager() {
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByDepManager" +
						"&depManager="  + vat.item.getValueByName("#F.depManager");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.depManagerName", vat.ajax.getValue("depManagerName", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.depManager", vat.ajax.getValue("depManager", vat.ajax.xmlHttp.responseText))
		}
	});
}

function getEmployeeInfo(vsEmployee) {
    if ("" != vsEmployee, vat.item.getValueByName("#F." + vsEmployee)) {
        vat.item.setValueByName("#F." + vsEmployee, vat.item.getValueByName("#F." + vsEmployee).toUpperCase());
        var processString = "process_sql_code=FindEmployeeChineseName&employeeCode=" + vat.item.getValueByName("#F." + vsEmployee);
        vat.ajax.startRequest(processString, function () {
            if (vat.ajax.handleState()) {
                if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
                    vat.item.setValueByName("#F." + vsEmployee + "Name", vat.ajax.getValue("CHINESE_NAME", vat.ajax.xmlHttp.responseText));
                } else {
                    vat.item.setValueByName("#F." + vsEmployee + "Name", "");
                    alert("查無此員工代號");
                }
            }
        });
    }
}

function onCustomerName(){
    var customerLastName = vat.item.getValueByName("#F.customerLastName").replace(/^\s+|\s+$/, '').toUpperCase();
    var nationality = vat.item.getValueByName("#F.nationality").replace(/^\s+|\s+$/, '').toUpperCase();  
    //alert("customerLastName="+customerLastName);
    //alert("nationality~~"+nationality);
    if(vat.item.getValueByName("#F.customerLastName")=="")
    {
    vat.item.setValueByName("#F.requestCode", customerLastName);
    vat.item.setValueByName("requestCode",null);
    }
	else if (vat.item.getValueByName("#F.customerSex")=="1"&vat.item.getValueByName("#F.customerLastName")!=""&vat.item.getValueByName("#F.nationality")=="1")
	{
    var customerLastName = vat.item.getValueByName("#F.customerLastName").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.requestCode", customerLastName+"先生");
    vat.item.setValueByName("requestCode",customerLastName);
    }
    else if (vat.item.getValueByName("#F.customerSex")=="2"&vat.item.getValueByName("#F.customerLastName")!=""&vat.item.getValueByName("#F.nationality")=="1")
	{
    var customerLastName = vat.item.getValueByName("#F.customerLastName").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.requestCode", customerLastName+"小姐");
    vat.item.setValueByName("requestCode",customerLastName);
    }
    else if (vat.item.getValueByName("#F.customerSex")=="1"&vat.item.getValueByName("#F.customerLastName")!=""&vat.item.getValueByName("#F.nationality")!="1")
	{
    var customerLastName = vat.item.getValueByName("#F.customerLastName").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.requestCode", "Mr."+customerLastName);
    vat.item.setValueByName("requestCode",customerLastName);
    }
    else if (vat.item.getValueByName("#F.customerSex")=="2"&vat.item.getValueByName("#F.customerLastName")!=""&vat.item.getValueByName("#F.nationality")!="1")
	{
    var customerLastName = vat.item.getValueByName("#F.customerLastName").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.requestCode", "Ms."+customerLastName);
    vat.item.setValueByName("requestCode",customerLastName);
    }
    else if (vat.item.getValueByName("#F.customerSex")=="")
	{
    var customerLastName = vat.item.getValueByName("#F.customerLastName").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.requestCode", customerLastName);
    vat.item.setValueByName("requestCode",null);
    }
    else if (vat.item.getValueByName("#F.nationality")=="")
	{
    var customerLastName = vat.item.getValueByName("#F.customerLastName").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.requestCode", customerLastName);
    vat.item.setValueByName("requestCode",null);
    }			
}
// 取得指定連動的類別下拉
function changeCategory(){
	var parentCategory = vat.item.getValueByName("#F.categoryGroup");

    vat.ajax.XHRequest(
    {
        post:"process_object_name=adCustomerServiceService"+
                 "&process_object_method_name=getAJAXCategory"+
                 "&categoryGroup=" + parentCategory,
        find: function changeRequestSuccess(oXHR){ 
	var allproject = eval(vat.ajax.getValue("allproject" , oXHR.responseText));
	var allsystem  = eval(vat.ajax.getValue("allsystem"  , oXHR.responseText));
	//alert("allproject="+allproject);
    allproject[0][0] = "#F.categoryItem";
	vat.item.SelectBind(allproject); 
	allsystem[0][0] = "#F.categorySystem";
	vat.item.SelectBind(allsystem); 
        }   
    });   
}
// 取得指定連動的類別下拉
function changeDepCategory(){
        vat.ajax.XHRequest(
        {
			post:"process_object_name=adCustomerServiceService"+
            		"&process_object_method_name=getAJAXDepCategory"+
                	"&department=" + vat.item.getValueByName("#F.department"), 
                            
			find: function changeRequestSuccess(oXHR){
				//alert("department==="+vat.item.getValueByName("#F.department"));         
           		var allcategoryType = eval(vat.ajax.getValue("allcategoryType", oXHR.responseText));
           		 // alert("allcategoryType===="+allcategoryType);
           allcategoryType[0][0] = "#F.categoryType";
				vat.item.SelectBind(allcategoryType); 
           }
        });
}
// 取得指定連動的類別下拉
function changePayCategory(){
	var parentCategory = vat.item.getValueByName("#F.paymentType");
	

    vat.ajax.XHRequest(
    {
        post:"process_object_name=adCustomerServiceService"+
                 "&process_object_method_name=getAJAXPayCategory"+
                 "&paymentType=" + parentCategory,
        find: function changeRequestSuccess(oXHR){ 
	var alltype = eval(vat.ajax.getValue("alltype" , oXHR.responseText));
	//alert("allcategoryType="+allcategoryType);
    alltype[0][0] = "#F.paymentKind";
	vat.item.SelectBind(alltype);
        }   
    });   
}
function onChangePoNo(){
	//alert('123');

	var customerPoNo = vat.item.getValueByName("#F.customerPoNo").toUpperCase();
	 
	var processString = "process_object_name=adCustomerServiceService&process_object_method_name=getAJAXFormDataByAdCust"+
						"&customerPoNo="  + customerPoNo +
						"&brandCode="     + vat.item.getValueByName("#F.brandCode") ;
	vat.ajax.startRequest(processString,  function() { 
		if (vat.ajax.handleState()){
		if(vat.item.getValueByName("#F.status") == "SAVE"){
		        str = vat.ajax.getValue("salesOrderDate", vat.ajax.xmlHttp.responseText).substring(0,10);
		        str = str.replace(/\-/ig,'/');
		        vat.item.setValueByName("#F.salesOrderDate", str);
				vat.item.setValueByName("#F.posMachineCode",     vat.ajax.getValue("posMachineCode",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.superintendentCode",     vat.ajax.getValue("superintendentCode",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.warehuseCode",    vat.ajax.getValue("warehuseCode",    vat.ajax.xmlHttp.responseText));
				} 
  			}
		} );
}
function onChangeItemCode(){
	//alert('123');
	var customerPoNo = vat.item.getValueByName("#F.customerPoNo").toUpperCase();

	var itemCode = vat.item.getValueByName("#F.itemCode").toUpperCase();
	 
	var processString = "process_object_name=adCustomerServiceService&process_object_method_name=getAJAXFormDataByItemCode"+
						"&itemCode="  		 + itemCode +
						"&customerPoNo="     + vat.item.getValueByName("#F.customerPoNo")+
						"&brandCode="        + vat.item.getValueByName("#F.brandCode") ;
	vat.ajax.startRequest(processString,  function() { 
		if (vat.ajax.handleState()){
		if(vat.item.getValueByName("#F.status") == "SAVE"){
				vat.item.setValueByName("#F.itemName"      , vat.ajax.getValue("itemName", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.itemBrand"     , vat.ajax.getValue("itemBrand", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.monetaryAmount", vat.ajax.getValue("monetaryAmount",   vat.ajax.xmlHttp.responseText));
				//vat.item.setValueByName("#F.warehuseCode",    vat.ajax.getValue("warehuseCode",    vat.ajax.xmlHttp.responseText));
				} 
  			}
		} );
}

function onChangeCustomCode(){
	//alert('123');

	var customerCode = vat.item.getValueByName("#F.customerCode").toUpperCase();
	 
	var processString = "process_object_name=adCustomerServiceService&process_object_method_name=getAJAXFormDataByCustomCode"+
						"&customerCode="  + customerCode +
						"&brandCode="     + vat.item.getValueByName("#F.brandCode") ;
	vat.ajax.startRequest(processString,  function() { 
		if (vat.ajax.handleState()){
		if(vat.item.getValueByName("#F.status") == "SAVE"){
				vat.item.setValueByName("#F.contactMobile", vat.ajax.getValue("contactMobile", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.contactHome",     vat.ajax.getValue("contactHome",   vat.ajax.xmlHttp.responseText));
				//vat.item.setValueByName("#F.contactHome",     vat.ajax.getValue("contactHome",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.email",     vat.ajax.getValue("email",   vat.ajax.xmlHttp.responseText));
				//vat.item.setValueByName("#F.warehuseCode",    vat.ajax.getValue("warehuseCode",    vat.ajax.xmlHttp.responseText));
				} 
  			}
		} );
}
	/* 指定下一個狀態 */
function changeFormStatus(formId, processId, status, approvalResult){
    var nextStatus = "";
    if(formId == null || formId == ""){
       	nextStatus = "SIGNING";
    }else if(processId != null && processId != ""){
        if(status == "SAVE" || status == "REJECT"){
            nextStatus = "SIGNING";
        }else if(approvalResult == "true"){
            nextStatus = status;
        }else if(approvalResult == "false"){
            nextStatus = "REJECT";
        }
    }
    return nextStatus;
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

function reconfirmImmovement(orderTypeCode,orderNo){
        
        var randomNo = randomWord(true,10,10);

		if(confirm('是否要列單據')){	
			url = "http://10.1.94.161:8080/crystal/t2/crm.rpt?cryp="+randomNo+"&prompt0="+vat.item.getValueByName("#F.brandCode")+"&prompt1="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt2="+vat.item.getValueByName("#F.orderNo");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
function reconfirmImmovement2(orderTypeCode,orderNo){

        var randomNo = randomWord(true,10,10);
        
		if(confirm('是否要列單據')){	
			url = "http://10.1.94.161:8080/crystal/t2/crm_2.rpt?cryp="+randomNo+"&prompt0="+vat.item.getValueByName("#F.brandCode")+"&prompt1="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt2="+vat.item.getValueByName("#F.orderNo");
			//url = "https://10.1.94.161/crystal/t2/crm_2.rpt?prompt0=CSF&prompt1=201408220001&prompt2=T2";
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
 