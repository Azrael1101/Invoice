 /*** 
 *	檔案: buSupplier.js
 *	說明: 類別代號,抽成率維護
 */
  

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vatApprovalDiv	= 3;

function outlineBlock(){ 
 	formInitial();
  	buttonLine();
  	headerInitial();
  	
  	 if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","主檔資料"   ,"vatBlock_Master"  		,"images/tab_master_data_dark.gif"    		,"images/tab_master_data_light.gif", false);
		vat.tabm.createButton(0 ,"xTab3","客戶需求資料"   ,"vatBlock_Master2"  		,"images/tab_customer_attachment_dark.png"    		,"images/tab_customer_attachment_light.png", false);  
		vat.tabm.createButton(0, "xTab4", "簽核資料", "vatApprovalDiv", "images/tab_approval_data_dark.gif", "images/tab_approval_data_light.gif");
//		vat.tabm.createButton(0 ,"xTab2","明細檔資料" ,"vatDetailDiv"        		,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false);
  }
	kweMaster();
	kweWfBlock( vat.item.getValueByName("#F.brandCode"), 
            	"SPM", 
             	vat.item.getValueByName("#F.headId"),
             	document.forms[0]["#loginEmployeeCode"].value );
	doFormAccessControl();
}
			
//初始化
function formInitial(){
 	
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
       {  
          brandCode  			: document.forms[0]["#loginBrandCode"    ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          loginDepartment  	    : document.forms[0]["#loginDepartment"   ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId      	: document.forms[0]["#assignmentId"      ].value,
          category		      	: document.forms[0]["#category"      	 ].value, 
          isRequestment	      	: document.forms[0]["#isRequestment"   	 ].value,      	  
          beforeStatus			: "beforestatus",
	      nextStatus			: "nextstatus",
	 //    approvalResult		: "approvalResult",
	 //    approvalComment		: "approvalComment",       
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        }; 
          vat.bean.init(  
             function(){
                    return "process_object_name=buSupplierModAction&process_object_method_name=performInitial"; 
         },{
             other: true
        }
        );
  }
  vat.tabm.displayToggle(0, "xTab4", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
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
	 									 service:"Bu_Supplier_Mod:search:20131121.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 	//		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.openReportWindow",type:"IMG",value:"廠商資料變更", src:"./images/changeData.jpg", eClick:'reconfirmImmovement()'},
	 		    //{name:"SPACE",            type:"LABEL", value:"　"}
	 		    {name:"#B.openReportWindow2",type:"IMG",value:"廠商資料卡", src:"./images/supplierData.jpg", eClick:'reconfirmSupplier()'},
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
	//alert(document.forms[0]["#category"].value);
	var allSupplierTypeCode = [["", "", "請選擇"], ["1-自然人","2-法人 "], ["1", "2"]];
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:document.forms[0]["#category"].value=="new"?"供應商資料建立作業":"供應商資料變更作業", 
		rows:[{row_style:"", cols:[
				{items:[{name:"#L.supplierCode", type:"LABEL", value:"廠商代號<font color='red'>*</font>"}]},
				{items:[{name:"#F.supplierCode", type:"TEXT", bind:"supplierCode",eChange:"onChangeSupplierCode()" }]},
				{items:[{name:"#L.enable", type:"LABEL" , value:"啟用?"}]},
				{items:[{name:"#F.enable", type:"XBOX", bind:"enable"},
				        {name:"#F.addressBookId", type:"TEXT", bind:"addressBookId",mode:"HIDDEN"}]},
				{items:[{name:"#L.identityCode", type:"LABEL" , value:"統一編號<font color='red'>*</font>"}]},
				{items:[{name:"#F.identityCode", type:"TEXT", bind:"identityCode"}]},
				{items:[{name:"#L.type", type:"LABEL" , value:"類別"}]},
				{items:[{name:"#F.type", type:"SELECT", bind:"type",init:allSupplierTypeCode}]},
				{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},
	 			{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode",mode:"readOnly" },
	 			{name:"#F.headId", type:"TEXT", bind:"headId",mode:"HIDDEN"}]},
				//{items:[{name:"#L.headId", type:"LABEL" , value:"headID",mode:"HIDDEN"}]},
				{items:[{name:"#L.status", type:"LABEL" , value:"狀態"}]},
	 		    {items:[{name:"#F.status",             type:"TEXT",   bind:"status", mode:"HIDDEN"},
	  					{name:"#F.statusName",         type:"TEXT",   bind:"statusName",          mode:"READONLY",  back:false},
	  					{name:"#F.category08",         type:"TEXT",   bind:"category08",          mode:"HIDDEN"},
	  					{name:"#F.department", type:"TEXT", bind:"department", mode:"HIDDEN",size:25, maxLen:25 }]},
	  			{items:[{name:"#L.creationDate", type:"LABEL" , value:"填單日期"}]},
	 		    {items:[{name:"#F.creationDate", type:"TEXT",   bind:"creationDate", mode:"READONLY"}]}
	 		    
	 		    ]},
	  					
				{row_style:"", cols:[
					 {items:[{name:"#L.chineseName", type:"LABEL", value:"公司名稱"}]},
					 {items:[{name:"#F.chineseName", type:"TEXT",size:60,maxLen:100, bind:"chineseName", eChange:"onChangeCompanyName()", mode:"readonly"}],td:" colSpan=3"},
					 {items:[{name:"#L.englishName", type:"LABEL", value:"英文名稱"}]},
					 {items:[{name:"#F.englishName", type:"TEXT",size:60,maxLen:100, bind:"englishName",mode:"readonly"}],td:" colSpan=3"},
					 {items:[{name:"#L.shortName", type:"LABEL", value:"簡稱"}]},
					 {items:[{name:"#F.shortName", type:"TEXT", bind:"shortName",mode:"readonly"}]},
					 {items:[{name:"#L.superintendent", type:"LABEL", value:"負責人"}]},
					 {items:[{name:"#F.superintendent", type:"TEXT", bind:"superintendent",mode:"readonly", maxLen:50}]},
					 {items:[{name:"#L.createdBy", type:"LABEL" , value:"填單人員" }]},
	 		   		 {items:[{name:"#F.createdBy", type:"TEXT",   bind:"createdBy", mode:"READONLY"}]}
				]}			 
			], 	
		 
		beginService:"",
		closeService:""			
	});
}

function kweMaster(){
var allSupplierType = vat.bean("allSupplierType");     
var allCategoryCode = vat.bean("allCategoryCode");     
var allInvoiceDeliveryType = vat.bean("allInvoiceDeliveryType"); 
var allSourceCurrency   = vat.bean("allSourceCurrency");
var allInvoiceType      = vat.bean("allInvoiceType");
var allpaymentTermCode      = vat.bean("allpaymentTermCode");
var allItemCategory      = vat.bean("allCategroyTypes");
var allsupplierTypeCode = vat.bean("allsupplierTypeCode");  
var allTradeTeam = vat.bean("allTradeTeam");  
var allTaxType = vat.bean("allTaxType");  
var allCountry = vat.bean("allCountry");

vat.block.create(vnB_Master, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[{row_style:"", cols:[
				{items:[{name:"#L.supplierTypeCode", type:"LABEL" , value:"廠商類別"}]},
				{items:[{name:"#F.supplierTypeCode", type:"SELECT", bind:"supplierTypeCode",init:allSupplierType}]},
				{items:[{name:"#L.categoryCode", type:"LABEL" , value:"廠商類型"}]},
				{items:[{name:"#F.categoryCode", type:"SELECT", bind:"categoryCode",init:allCategoryCode }]},
				{items:[{name:"#L.countryCode", type:"LABEL" , value:"國別"}]},
	 			{items:[{name:"#F.countryCode", type:"SELECT", bind:"countryCode",init:allCountry,mode:"readOnly" }]},
	 			{items:[{name:"#L.categoryType", type:"LABEL", value:"業種"}]},
				{items:[{name:"#F.categoryType", type:"SELECT", bind:"categoryType",init:allItemCategory },
						{name:"SPACE"          , type:"LABEL"  ,value:"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"},
						{name:"#L.consign", type:"LABEL", value:"寄賣"},
						{name:"#F.consign", type:"XBOX", bind:"consign" }]}
			]},
			{row_style:"", cols:[
				
				 {items:[{name:"#L.taxType", type:"LABEL", value:"稅別"}]},
				 {items:[{name:"#F.taxType", type:"SELECT", bind:"taxType",init:allTaxType, eChange:"onChangeTaxType()" }]},
				 {items:[{name:"#L.taxRate", type:"LABEL", value:"稅率"}]},
				 {items:[{name:"#F.taxRate", type:"TEXT", bind:"taxRate" },
				 		 {name:"#L.taxRate1",type:"LABEL",  value:"%"}]},
				 {items:[{name:"#L.currencyCode", type:"LABEL", value:"使用幣別"}]},
				 {items:[{name:"#F.currencyCode", type:"SELECT", bind:"currencyCode",init:allSourceCurrency}]},
				 {items:[{name:"#L.priceTermCode", type:"LABEL", value:"價格條件"}]},
				 {items:[{name:"#F.priceTermCode", type:"SELECT", bind:"priceTermCode",init:allTradeTeam }]}
				
			]},


			 {row_style:"", cols:[

				 {items:[{name:"#L.remark3", type:"LABEL", value:"聯絡人"}]},
				 {items:[{name:"#F.remark3", type:"TEXT", bind:"remark3",size:"40",maxLen:40 }]},
			 	 {items:[{name:"#L.companyAddress", type:"LABEL", value:"公司地址"}]},
				 {items:[{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				 		 {name:"#L.city", type:"LABEL", value:"縣市  " },
						 {name:"#F.city", type:"TEXT", bind:"city" },
						 {name:"SPACE"          , type:"LABEL"  ,value:"　"},
				 		 {name:"#L.area", type:"LABEL", value:"鄉鎮市區  " },
				 		 {name:"#F.area", type:"TEXT", bind:"area" },
				 		 {name:"SPACE"          , type:"LABEL"  ,value:"　"},
				 		 {name:"#L.zipCode", type:"LABEL", value:"郵遞區號  " },
				 		 {name:"#F.zipCode", type:"TEXT", bind:"zipCode" },
				 		 {name:"SPACE"          , type:"LABEL"  ,value:"　"},
						 {name:"#F.companyAddress", type:"TEXT", bind:"companyAddress",size:"50",maxLen:150 }],td:" colSpan=5"}				 

			 ]},
			 {row_style:"", cols:[
			 	 {items:[{name:"#L.bankName", type:"LABEL", value:"銀行名稱"}]},
				 {items:[{name:"#F.bankName", type:"TEXT", bind:"bankName",size:"70",maxLen:100}],td:" colSpan=3"},
				 {items:[{name:"#L.branchCode", type:"LABEL", value:"分行別"}]},
				 {items:[{name:"#F.branchCode", type:"TEXT", bind:"branchCode",maxLen:100 }]},
				 {items:[{name:"#L.accepter", type:"LABEL", value:"受款人"}]},
				 {items:[{name:"#F.accepter", type:"TEXT", bind:"accepter", size:"45",maxLen:100  }]}
			 ]},
			 {row_style:"", cols:[
			 	 {items:[{name:"#L.accountCode", type:"LABEL", value:"帳號"}]},
				 {items:[{name:"#F.accountCode", type:"TEXT", bind:"accountCode", size:"70",maxLen:100 }],td:" colSpan=3"},
				 {items:[{name:"#L.acceptEMail", type:"LABEL", value:"匯款通知mail"}]},
				 {items:[{name:"#F.acceptEMail", type:"TEXT", bind:"acceptEMail",maxLen:100,size:"80" }],td:" colSpan=5"}
			 ]},
			 {row_style:"", cols:[
			 	 {items:[{name:"#L.category09", type:"LABEL", value:"合約起日"}]},
				 {items:[{name:"#F.category09", type:"DATE", bind:"category09" }]},
				 {items:[{name:"#L.category10", type:"LABEL", value:"合約迄日"}]},
				 {items:[{name:"#F.category10", type:"DATE", bind:"category10"}]},

				 {items:[{name:"#L.companyEmail", type:"LABEL", value:"電子信箱"}]},
				 {items:[{name:"#F.companyEmail", type:"TEXT", bind:"companyEmail" ,size:"45",maxLen:100}],td:" colSpan=3"}
			 	 

			 ]},
			 {row_style:"", cols:[
			 
				 {items:[{name:"#L.companyFax1", type:"LABEL", value:"傳真一"}]},
				 {items:[{name:"#F.companyFax1", type:"TEXT", bind:"companyFax1", maxLen:50 }]},
				 {items:[{name:"#L.companyFax2", type:"LABEL", value:"傳真二"}]},
				 {items:[{name:"#F.companyFax2", type:"TEXT", bind:"companyFax2", maxLen:50 }]},
				 				 {items:[{name:"#L.telDay", type:"LABEL", value:"電話(日)"}]},
				 {items:[{name:"#F.telDay", type:"TEXT", bind:"telDay", maxLen:50 }]},
				 {items:[{name:"#L.telNight", type:"LABEL", value:"電話(夜)"}]},
				 {items:[{name:"#F.telNight", type:"TEXT", bind:"telNight", maxLen:50 }]}				 
			 ]},
			 {row_style:"", cols:[
			 	 {items:[{name:"#L.customsBroker", type:"LABEL", value:"報關行"}]},
				 {items:[{name:"#F.customsBroker", type:"TEXT", bind:"customsBroker" }]},
				 {items:[{name:"#L.agent", type:"LABEL", value:"代理商"}]},
				 {items:[{name:"#F.agent", type:"TEXT", bind:"agent" }]},
				 {items:[{name:"#L.commissionRate", type:"LABEL", value:"佣金比率"}]},
				 {items:[{name:"#F.commissionRate", type:"TEXT", bind:"commissionRate" }]},
				 {items:[{name:"#L.paymentTermCode", type:"LABEL", value:"付款條件"}]},
				 {items:[{name:"#F.paymentTermCode", type:"SELECT", bind:"paymentTermCode",init:allpaymentTermCode }]}
			 ]},

		    {row_style:"", cols:[
			 	 {items:[{name:"#L.invoiceDeliveryCode", type:"LABEL", value:"發票交付方式"}]},
				 {items:[{name:"#F.invoiceDeliveryCode", type:"SELECT", bind:"invoiceDeliveryCode",init:allInvoiceDeliveryType }]},
			 				{items:[{name:"#L.invoiceTypeCode", type:"LABEL", value:"發票類型"}]},
				{items:[{name:"#F.invoiceTypeCode", type:"SELECT", bind:"invoiceTypeCode",init:allInvoiceType }]},
				 {items:[{name:"#L.billStyleCode", type:"LABEL", value:"帳單格式"}]},
				 {items:[{name:"#F.billStyleCode", type:"TEXT", bind:"billStyleCode" }]},
				 {items:[{name:"#L.grade", type:"LABEL", value:"等級"}]},
				 {items:[{name:"#F.grade", type:"TEXT", bind:"grade" }]}
			 ]},
			 
		  	 {row_style:"", cols:[
				 {items:[{name:"#L.other", type:"LABEL", value:"其他配合事項"}]},
				 {items:[{name:"#F.other", type:"TEXT", bind:"other",maxLen:100,size:"80" }],td:" colSpan=7"}
			 ]},
			  		 
			 {row_style:"", cols:[
				 {items:[{name:"#L.remark1", type:"LABEL", value:"備註一"}]},
				 {items:[{name:"#F.remark1", type:"TEXT", bind:"remark1",maxLen:100,size:"80" }],td:" colSpan=7"}
			 ]},
			 {row_style:"", cols:[
				 {items:[{name:"#L.remark2", type:"LABEL", value:"備註二"}]},
				 {items:[{name:"#F.remark2", type:"TEXT", bind:"remark2",maxLen:100,size:"80" }],td:" colSpan=7"}
			]},
			 {row_style:"", cols:[
				 				 
			]}		
		
 	 	],
		beginService:"",
		closeService:""
	});
	
vat.block.create(vnB_Master, {
	id: "vatBlock_Master2", table:"cellspacing='0' class='default'  border='0' cellpadding='3'", 
	rows:[{row_style:"", cols:[
				{items:[{name:"#L.classify", type:"LABEL" , value:"類別"}], td:"style='background-color:#990000; color:#fff; '"},
				{items:[{name:"#L.contactPerson", type:"LABEL" , value:"聯絡人"}], td:"colSpan=2 style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.tel", type:"LABEL" , value:"電話"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.fax", type:"LABEL" , value:"傳真"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.email", type:"LABEL" , value:"電子信箱"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.buttonList01", type:"LABEL" , value:""}], td:"style='background-color:#990000; color:#fff;'"}
			]},
			
			{row_style:"", cols:[
				{items:[{name:"#L.classify_1", type:"LABEL" , value:"業務窗口"}]},
				{items:[{name:"#F.contactPerson1", type:"TEXT" , bind:"contactPerson1" , size:10, maxLen:20}],td:"colSpan=2"},
				{items:[{name:"#F.tel1", type:"TEXT" , bind:"tel1" , size:20, maxLen:50}]},
				{items:[{name:"#F.fax1", type:"TEXT" , bind:"fax1" , size:20, maxLen:50}]},
				{items:[{name:"#F.EMail1", type:"TEXT" , bind:"EMail1" ,size:40, maxLen:100}]}
			]},

			{row_style:"", cols:[
				{items:[{name:"#L.classify_2", type:"LABEL" , value:"財務窗口"}]},
				{items:[{name:"#F.contactPerson2", type:"TEXT", bind:"contactPerson2" , size:10, maxLen:20}],td:"colSpan=2"},
				{items:[{name:"#F.tel2", type:"TEXT" , bind:"tel2" , size:20, maxLen:50}]},
				{items:[{name:"#F.fax2", type:"TEXT" , bind:"fax2" , size:20, maxLen:50}]},
				{items:[{name:"#F.EMail2", type:"TEXT" , bind:"EMail2" , size:40, maxLen:100}]},
				{items:[{name:"#B.copyinfo1"      , type:"button"    ,value:"同上", eClick:'copyInfo("classify_2")'}]}
			]},
			
			{row_style:"", cols:[
				{items:[{name:"#L.classify_3", type:"LABEL" , value:"倉庫窗口"}]},
				{items:[{name:"#F.contactPerson3", type:"TEXT" , bind:"contactPerson3" , size:10, maxLen:20}],td:"colSpan=2"},
				{items:[{name:"#F.tel3", type:"TEXT" , bind:"tel3" , size:20, maxLen:50}]},
				{items:[{name:"#F.fax3", type:"TEXT" , bind:"fax3" , size:20, maxLen:50}]},
				{items:[{name:"#F.EMail3", type:"TEXT" , bind:"EMail3" , size:40, maxLen:100}]},
				{items:[{name:"#B.copyinfo2"      , type:"button"    ,value:"同上", eClick:'copyInfo("classify_3")'}]}
			]},						

			{row_style:"", cols:[
				{items:[{name:"#L.classify_4", type:"LABEL" , value:"其他窗口"}]},
				{items:[{name:"#F.contactPerson4", type:"TEXT" , bind:"contactPerson4" , size:10, maxLen:20}],td:"colSpan=2"},
				{items:[{name:"#F.tel4", type:"TEXT" , bind:"tel4" , size:20, maxLen:50}]},
				{items:[{name:"#F.fax4", type:"TEXT" , bind:"fax4" , size:20, maxLen:50}]},
				{items:[{name:"#F.EMail4", type:"TEXT" ,bind:"EMail4", size:40, maxLen:100}]},
				{items:[{name:"#B.copyinfo3"      , type:"button"    ,value:"同上", eClick:'copyInfo("classify_4")'}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.address_0", type:"LABEL" , value:"類別"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.city", type:"LABEL" , value:"縣市"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.area", type:"LABEL" , value:"鄉鎮市區"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.zipCode", type:"LABEL" , value:"郵遞區號"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.address", type:"LABEL" , value:"地址"}], td:"colSpan=3 style='background-color:#990000; color:#fff;'"}
			]},		
			{row_style:"", cols:[
				{items:[{name:"#L.address_1", type:"LABEL" , value:"通訊地址"}]},
				{items:[{name:"#F.city1", type:"TEXT" , bind:"city1" , size:10, maxLen:50}]},
				{items:[{name:"#F.area1", type:"TEXT" , bind:"area1" , size:10, maxLen:50}]},
				{items:[{name:"#F.zipCode1", type:"TEXT" , bind:"zipCode1" , size:20, maxLen:50}]},
				{items:[{name:"#F.address1", type:"TEXT" , bind:"address1" , size:60, maxLen:150}],td:"colSpan=2"},
				{items:[{name:"#B.copyinfo4"      , type:"button"    ,value:"同主檔地址", eClick:'copyInfo("address_1")'}]}
			]},

			{row_style:"", cols:[
				{items:[{name:"#L.address_2", type:"LABEL" , value:"發票地址"}]},
				{items:[{name:"#F.city2", type:"TEXT" , bind:"city2" , size:10, maxLen:50}]},
				{items:[{name:"#F.area2", type:"TEXT" , bind:"area2" , size:10, maxLen:50}]},
				{items:[{name:"#F.zipCode2", type:"TEXT" , bind:"zipCode2" , size:20, maxLen:50}]},
				{items:[{name:"#F.address2", type:"TEXT" , bind:"address2" , size:60, maxLen:150}],td:"colSpan=2"},
				{items:[{name:"#B.copyinfo5"      , type:"button"    ,value:"同上", eClick:'copyInfo("address_2")'}]}
			]},
			
			{row_style:"", cols:[
				{items:[{name:"#L.address_3", type:"LABEL" , value:"送貨地址"}]},
				{items:[{name:"#F.city3", type:"TEXT" , bind:"city3" , size:10, maxLen:50}]},
				{items:[{name:"#F.area3", type:"TEXT" , bind:"area3", size:10, maxLen:50}]},
				{items:[{name:"#F.zipCode3", type:"TEXT" , bind:"zipCode3" , size:20, maxLen:50}]},
				{items:[{name:"#F.address3", type:"TEXT" , bind:"address3" , size:60, maxLen:150}],td:"colSpan=2"},
				{items:[{name:"#B.copyinfo6"      , type:"button"    ,value:"同上", eClick:'copyInfo("address_3")'}]}
			]},						

			{row_style:"", cols:[
				{items:[{name:"#L.address_4", type:"LABEL" , value:"其他地址"}]},
				{items:[{name:"#F.city4", type:"TEXT" , bind:"city4" , size:10, maxLen:50}]},
				{items:[{name:"#F.area4", type:"TEXT" , bind:"area4" , size:10, maxLen:50}]},
				{items:[{name:"#F.zipCode4", type:"TEXT" , bind:"zipCode4" , size:20, maxLen:50}]},
				{items:[{name:"#F.address4", type:"TEXT" , bind:"address4" , size:60, maxLen:50}],td:"colSpan=2"},
				{items:[{name:"#B.copyinfo7"      , type:"button"    ,value:"同上", eClick:'copyInfo("address_4")'}]}
			]}								
		
 	 	], 	 	
		beginService:"",
		closeService:""
	});	
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
	//alert(document.forms[0]["#formId"].value);
	var formId               = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var processId            = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var status               = vat.item.getValueByName("#F.status");
    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	var approvalResult  = vat.item.getValueByName("#F.approvalResult");
    if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
    var formStatus = status;
    if("SAVE" == formAction){
        formStatus = "SAVE";
    }else if("SUBMIT" == formAction){
        formStatus = changeFormStatus(formId, processId, status, approvalResult);
    }else if("VOID" == formAction){
        formStatus = "VOID";
    }	
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	 	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	 	}	 	
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction = formAction;
	    vat.bean().vatBeanOther.approvalResult = approvalResult;	
	    vat.bean().vatBeanOther.approvalComment = approvalComment;
	    vat.bean().vatBeanOther.formStatus = formStatus;				
		vat.block.submit
		(function(){
				return "process_object_name=buSupplierModAction" + "&process_object_method_name=performTransaction";
			},
			{bind:true, link:true, other:true}
		);
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
	document.forms[0]["#formId"].value = code; 
	//vat.item.setValueByName("#F.headId", code);
	document.forms[0]["#processId"         ].value = "";
	document.forms[0]["#assignmentId"      ].value = "";
	
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
//	vat.bean().vatBeanOther.supplierCode = supplierCode;
//	vat.bean().vatBeanOther.brandCode = brandCode;
		
	vat.block.submit(
		function(){
			return "process_object_name=buSupplierModAction&process_object_method_name=performInitial"; 
     	},{other: true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 				
	 		      	refreshWfParameter(vat.item.getValueByName("#F.brandCode"), "SPM", vat.item.getValueByName("#F.headId"));	        		
	        		//alert('refresh');
	        		vat.block.pageRefresh(102);	        			        		
	        	// 判斷錯誤導致簽核資料消失所以MARK起來		        		 				
				doFormAccessControl();
				vat.tabm.displayToggle(0, "xTab4", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
     		}
     	}
    );	
}
//列印差異報表
function reconfirmImmovement(supplierCode){
var randomString = randomWord(true, 10, 10);
		if(confirm('廠商資料—變更')){	
			url = "http://10.1.94.161:8080/crystal/t2/Sup0017.rpt?cypto="+randomString +"&prompt0="+vat.item.getValueByName("#F.headId");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
//列印差異報表
function reconfirmSupplier(supplierCode){

var randomString = randomWord(true, 10, 10);
		if(confirm('產生廠商資料卡')){	
			url = "http://10.1.94.161:8080/crystal/t2/BU0020.rpt?cypto="+randomString +"&prompt0="+vat.item.getValueByName("#F.headId");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
//列印評核報表
function reconfirmImmovement2(supplierCode){
var randomString = randomWord(true, 10, 10);
		if(confirm('廠商評核表列印')){	
			url = "http://10.1.94.161:8080/crystal/t2/Assessmente0109.rpt?cypto="+randomString +"&prompt0="+vat.item.getValueByName("#F.supplierCode");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
/* 指定下一個狀態 */
function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == "" || status == "UNCONFIRMED"){
        formStatus = "SIGNING";
    }else if(processId != null && processId != "" && processId != 0){
    	if(status == "SAVE" || status == "REJECT"){
			formStatus = "SIGNING";
        }
        if( status == "REJECT"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }else if (status == "SIGNING"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }else{
        	formStatus = status;
        }
    }else if(status == "FINISH"){
    	formStatus = "FINISH";
    }else{
    	formStatus = "SIGNING";
    }
    return formStatus;
}
// 依formId鎖form
function doFormAccessControl(){
	var brandCode 		= vat.item.getValueByName("#F.brandCode");
	var status          = vat.item.getValueByName("#F.status");
	var processId 		= vat.bean().vatBeanOther.processId;
	var formId 			= vat.bean().vatBeanOther.formId;
	var canBeClaimed	= document.forms[0]["#canBeClaimed"].value;
	var processId     	= document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');	
	var loginDepartment	= vat.item.getValueByName("#F.department");
	var isRequestment   = "Y"===document.forms[0]["#isRequestment"].value;

	//alert('doFormAccessControl'+ canBeClaimed);
	
	if(canBeClaimed == "true")
		vat.item.setStyleByName("#B.submit",    "display", "none");
	
	if(status == "SIGNING"||status=="FINISH"){
	
	vat.item.setAttributeByName("vnB_Master", "readOnly", true);
	vat.item.setAttributeByName("vnB_Header",  "readOnly", true);
	
//	vat.item.setAttributeByName("#F.invoiceTypeCode", "readOnly", true);
//	vat.item.setAttributeByName("#F.supplierTypeCode", "readOnly", true);
//	vat.item.setAttributeByName("#F.supplierCode", "readOnly", true);
	}
	//alert('if1111');
	//======================<button>=============================================
	if(formId != ""){
        // 流程內
        if( processId != null && processId != 0 ){ //從待辦事項進入

            if( status == "SAVE" || status == "REJECT"){  
      

                	
	                vat.item.setStyleByName("#B.submit" , "display", "inline");
	                if(isRequestment)
	                {
	       				vat.item.setStyleByName("#B.void" , "display", "inline");
	       				vat.item.setStyleByName("#B.save" , "display", "inline");
	       			}
	       			else
	       			{
	       				vat.item.setStyleByName("#B.void" , "display", "none");
	       				vat.item.setStyleByName("#B.save" , "display", "none");
	       			}
	       			vat.item.setStyleByName("#B.openReportWindow", "display", "inline"); 


            }else if(status == "SIGNING"){
            	vat.item.setStyleByName("#B.void" , "display", "none");
       			vat.item.setStyleByName("#B.save" , "display", "none");
       			vat.item.setStyleByName("#B.openReportWindow", "display", "inline");
            }
        }else{
            // 查詢回來
            if( status == "SAVE" || status == "REJECT"){
                vat.item.setStyleByName("#B.submit",    "display", "none"); 

	                vat.item.setStyleByName("#B.save",      "display", "none"); 
	                vat.item.setStyleByName("#B.openReportWindow", "display", "inline");
	      			vat.item.setStyleByName("#B.void" , "display", "none");      			
	      			vat.item.setStyleByName("#B.search" , "display", "inline");

				}else if(status == "SIGNING"){
					vat.item.setStyleByName("#B.openReportWindow", "display", "inline");
      				vat.item.setStyleByName("#B.void" , "display", "none");
      				vat.item.setStyleByName("#B.submit",    "display", "none");
       				vat.item.setStyleByName("#B.save" , "display", "none");
       				vat.item.setStyleByName("#B.search" , "display", "inline");
				}else{
					 vat.item.setStyleByName("#B.openReportWindow", "display", "inline");
       				 vat.item.setStyleByName("#B.void" , "display", "none");
       				 vat.item.setStyleByName("#B.save" , "display", "none");
       				 vat.item.setStyleByName("#B.submit" , "display", "none");
				}
			}
		}

	/*if(status=="SIGNING"){
       vat.item.setStyleByName("#B.openReportWindow", "display", "inline");
       vat.item.setStyleByName("#B.void" , "display", "none");
       vat.item.setStyleByName("#B.save" , "display", "none");
       vat.item.setStyleByName("#B.search" , "display", "inline");
       }*/
       
     /*if(status=="FINISH"){
       vat.item.setStyleByName("#B.openReportWindow", "display", "inline");
       vat.item.setStyleByName("#B.void" , "display", "none");
       vat.item.setStyleByName("#B.save" , "display", "none");
       vat.item.setStyleByName("#B.submit" , "display", "none");
       }*/
       
     
       // 初始化
	//======================<header>=============================================
	vat.item.setAttributeByName("#F.supplierCode", "readOnly", true);
	vat.item.setAttributeByName("#F.addressBookId", "readOnly", true);
	vat.item.setAttributeByName("#F.supplierTypeCode", "readOnly", false);
	vat.item.setAttributeByName("#F.categoryCode", "readOnly", false);
	vat.item.setAttributeByName("#F.invoiceTypeCode", "readOnly", false);
	vat.item.setAttributeByName("#F.taxType", "readOnly", false);
	vat.item.setAttributeByName("#F.taxRate", "readOnly", false);
	vat.item.setAttributeByName("#F.currencyCode", "readOnly", false);
	vat.item.setAttributeByName("#F.invoiceDeliveryCode", "readOnly", false);
	vat.item.setAttributeByName("#F.paymentTermCode", "readOnly", false);
	vat.item.setAttributeByName("#F.billStyleCode", "readOnly", false);
	vat.item.setAttributeByName("#F.grade", "readOnly", false);
	vat.item.setAttributeByName("#F.consign", "readOnly", false);
	vat.item.setAttributeByName("#F.acceptEMail", "readOnly", false);
	vat.item.setAttributeByName("#F.other", "readOnly", false);
	vat.item.setAttributeByName("#F.customsBroker", "readOnly", false);
	vat.item.setAttributeByName("#F.agent", "readOnly", false);
	vat.item.setAttributeByName("#F.commissionRate", "readOnly", false);
	vat.item.setAttributeByName("#F.priceTermCode", "readOnly", false);
	vat.item.setAttributeByName("#F.bankName", "readOnly", false);
	vat.item.setAttributeByName("#F.branchCode", "readOnly", false);
	vat.item.setAttributeByName("#F.accountCode", "readOnly", false);
	vat.item.setAttributeByName("#F.accepter", "readOnly", false);
	vat.item.setAttributeByName("#F.contactPerson1", "readOnly", false);
	vat.item.setAttributeByName("#F.tel1", "readOnly", false);
	vat.item.setAttributeByName("#F.fax1", "readOnly", false);		
	vat.item.setAttributeByName("#F.address1", "readOnly", false);
	vat.item.setAttributeByName("#F.address2", "readOnly", false);
	vat.item.setAttributeByName("#F.EMail1", "readOnly", false);
	vat.item.setAttributeByName("#F.remark1", "readOnly", false);
	vat.item.setAttributeByName("#F.remark2", "readOnly", false);
	vat.item.setAttributeByName("#F.chineseName", "readOnly", false);
	vat.item.setAttributeByName("#F.englishName", "readOnly", false);
	vat.item.setAttributeByName("#F.superintendent", "readOnly", false);
	vat.item.setAttributeByName("#F.shortName", "readOnly", false);
	vat.item.setAttributeByName("#F.countryCode", "readOnly", false);

	vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
	vat.item.setStyleByName("#B.copyinfo1", "display", "inline");
    vat.item.setStyleByName("#B.copyinfo2", "display", "inline");
    vat.item.setStyleByName("#B.copyinfo3", "display", "inline");
    vat.item.setStyleByName("#B.copyinfo4", "display", "inline");
    vat.item.setStyleByName("#B.copyinfo5", "display", "inline");
    vat.item.setStyleByName("#B.copyinfo6", "display", "inline");
    vat.item.setStyleByName("#B.copyinfo7", "display", "inline");
	if( status == "SIGNING" || status == "FINISH" || status == "CLOSE"  || status == "VOID" ){
		//======================<header>=============================================
		vat.item.setAttributeByName("#F.supplierCode", "readOnly", true);
		vat.item.setAttributeByName("#F.addressBookId", "readOnly", true);
		vat.item.setAttributeByName("#F.chineseName", "readOnly", true);
		vat.item.setAttributeByName("#F.englishName", "readOnly", true);
		vat.item.setAttributeByName("#F.superintendent", "readOnly", true);
		vat.item.setAttributeByName("#F.shortName", "readOnly", true);
		//======================<master>=============================================
		vat.item.setAttributeByName("#F.supplierTypeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.categoryCode", "readOnly", true);
		vat.item.setAttributeByName("#F.invoiceTypeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.taxType", "readOnly", true);
		vat.item.setAttributeByName("#F.taxRate", "readOnly", true);
		vat.item.setAttributeByName("#F.currencyCode", "readOnly", true);
		vat.item.setAttributeByName("#F.invoiceDeliveryCode", "readOnly", true);
		vat.item.setAttributeByName("#F.paymentTermCode", "readOnly", true);
		vat.item.setAttributeByName("#F.billStyleCode", "readOnly", true);
		vat.item.setAttributeByName("#F.grade", "readOnly", true);
		vat.item.setAttributeByName("#F.consign", "readOnly", true);
		vat.item.setAttributeByName("#F.acceptEMail", "readOnly", true);
		vat.item.setAttributeByName("#F.other", "readOnly", true);
		
		vat.item.setAttributeByName("#F.customsBroker", "readOnly", true);
		vat.item.setAttributeByName("#F.agent", "readOnly", true);
		vat.item.setAttributeByName("#F.commissionRate", "readOnly", true);
		vat.item.setAttributeByName("#F.priceTermCode", "readOnly", true);
		vat.item.setAttributeByName("#F.bankName", "readOnly", true);
		vat.item.setAttributeByName("#F.branchCode", "readOnly", true);
		vat.item.setAttributeByName("#F.accountCode", "readOnly", true);
		vat.item.setAttributeByName("#F.accepter", "readOnly", true);
		vat.item.setAttributeByName("#F.contactPerson1", "readOnly", true);
		vat.item.setAttributeByName("#F.tel1", "readOnly", true);
		vat.item.setAttributeByName("#F.fax1", "readOnly", true);
		vat.item.setAttributeByName("#F.contactPerson2", "readOnly", true);
		vat.item.setAttributeByName("#F.tel2", "readOnly", true);
		vat.item.setAttributeByName("#F.fax2", "readOnly", true);	
		vat.item.setAttributeByName("#F.contactPerson3", "readOnly", true);
		vat.item.setAttributeByName("#F.tel3", "readOnly", true);
		vat.item.setAttributeByName("#F.fax3", "readOnly", true);	
		vat.item.setAttributeByName("#F.contactPerson4", "readOnly", true);
		vat.item.setAttributeByName("#F.tel4", "readOnly", true);
		vat.item.setAttributeByName("#F.fax4", "readOnly", true);								
		vat.item.setAttributeByName("#F.address1", "readOnly", true);
		vat.item.setAttributeByName("#F.address2", "readOnly", true);
		vat.item.setAttributeByName("#F.address3", "readOnly", true);
		vat.item.setAttributeByName("#F.address4", "readOnly", true);		
		vat.item.setAttributeByName("#F.EMail1", "readOnly", true);
		vat.item.setAttributeByName("#F.EMail2", "readOnly", true);
		vat.item.setAttributeByName("#F.EMail3", "readOnly", true);
		vat.item.setAttributeByName("#F.EMail4", "readOnly", true);
		vat.item.setAttributeByName("#F.remark1", "readOnly", true);
		vat.item.setAttributeByName("#F.remark2", "readOnly", true);
		vat.item.setAttributeByName("#F.chineseName", "readOnly", true);
		vat.item.setAttributeByName("#F.englishName", "readOnly", true);
		vat.item.setAttributeByName("#F.superintendent", "readOnly", true);
		vat.item.setAttributeByName("#F.shortName", "readOnly", true);
		vat.item.setAttributeByName("#F.countryCode", "readOnly", true);
		vat.item.setAttributeByName("#F.category01", "readOnly", true);
		vat.item.setAttributeByName("#F.category02", "readOnly", true);
		vat.item.setAttributeByName("#F.category03", "readOnly", true);
		vat.item.setAttributeByName("#F.category04", "readOnly", true);
		vat.item.setAttributeByName("#F.category05", "readOnly", true);
		vat.item.setAttributeByName("#F.category06", "readOnly", true);
		vat.item.setAttributeByName("#F.category07", "readOnly", true);
		vat.item.setAttributeByName("#F.category08", "readOnly", true);
		vat.item.setAttributeByName("#F.category09", "readOnly", true);
		vat.item.setAttributeByName("#F.category10", "readOnly", true);
		vat.item.setAttributeByName("#F.category11", "readOnly", true);
		vat.item.setAttributeByName("#F.category12", "readOnly", true);
		vat.item.setAttributeByName("#F.category13", "readOnly", true);
		vat.item.setAttributeByName("#F.category14", "readOnly", true);
		vat.item.setAttributeByName("#F.category15", "readOnly", true);
		vat.item.setAttributeByName("#F.category16", "readOnly", true);
		vat.item.setAttributeByName("#F.category17", "readOnly", true);
		vat.item.setAttributeByName("#F.category18", "readOnly", true);
		vat.item.setAttributeByName("#F.category19", "readOnly", true);
		vat.item.setAttributeByName("#F.category20", "readOnly", true);
		vat.item.setAttributeByName("#F.category20", "readOnly", true);
		vat.item.setAttributeByName("#F.remark3", "readOnly", true);	
		vat.item.setAttributeByName("#F.companyAddress", "readOnly", true);	
		vat.item.setAttributeByName("#F.companyEmail", "readOnly", true);	
		vat.item.setAttributeByName("#F.companyFax1", "readOnly", true);		
		vat.item.setAttributeByName("#F.companyFax2", "readOnly", true);	
		vat.item.setAttributeByName("#F.telDay", "readOnly", true);
		vat.item.setAttributeByName("#F.telNight", "readOnly", true);
		vat.item.setAttributeByName("#F.categoryType", "readOnly", true);
		vat.item.setAttributeByName("#F.city", "readOnly", true);	
		vat.item.setAttributeByName("#F.city1", "readOnly", true);	
		vat.item.setAttributeByName("#F.city2", "readOnly", true);
		vat.item.setAttributeByName("#F.city3", "readOnly", true);
		vat.item.setAttributeByName("#F.city4", "readOnly", true);
		vat.item.setAttributeByName("#F.area", "readOnly", true);	
		vat.item.setAttributeByName("#F.area1", "readOnly", true);	
		vat.item.setAttributeByName("#F.area2", "readOnly", true);
		vat.item.setAttributeByName("#F.area3", "readOnly", true);
		vat.item.setAttributeByName("#F.area4", "readOnly", true);
		vat.item.setAttributeByName("#F.zipCode1", "readOnly", true);	
		vat.item.setAttributeByName("#F.zipCode2", "readOnly", true);
		vat.item.setAttributeByName("#F.zipCode3", "readOnly", true);
		vat.item.setAttributeByName("#F.zipCode4", "readOnly", true);
		vat.item.setAttributeByName("#F.companyName", "readOnly", true);	
		if(status !== "SIGNING"){
			vat.item.setStyleByName("#B.new",       "display", "none");
	        vat.item.setStyleByName("#B.search",    "display", "none");
	        vat.item.setStyleByName("#B.copyinfo1", "display", "none");
	       	vat.item.setStyleByName("#B.copyinfo2", "display", "none");
	       	vat.item.setStyleByName("#B.copyinfo3", "display", "none");
	       	vat.item.setStyleByName("#B.copyinfo4", "display", "none");
	       	vat.item.setStyleByName("#B.copyinfo5", "display", "none");
	       	vat.item.setStyleByName("#B.copyinfo6", "display", "none");
	       	vat.item.setStyleByName("#B.copyinfo7", "display", "none");
       	}							
		//===========================================================================
		
	}
	if(processId!="" ){
		vat.tabm.displayToggle(0, "xTab4", true, false, false);
     	refreshWfParameter( vat.item.getValueByName("#F.brandCode"),"SPM",vat.item.getValueByName("#F.headId" ) );
		vat.block.pageDataLoad(102, vnCurrentPage = 1); 
	}else{
		vat.tabm.displayToggle(0, "xTab4", false, false, false);
	}
	
//Maco 供應商編號.供應商統編編輯權限
	//alert(status+"  /  "+document.forms[0]["#category"].value+"  /  "+loginDepartment);
	vat.item.setAttributeByName("#F.identityCode", "readOnly", true);
    vat.item.setAttributeByName("#F.supplierCode", "readOnly", true);
    vat.item.setAttributeByName("#F.enable", "readOnly", true);
    vat.item.setAttributeByName("#F.category09", "readOnly", true);
    vat.item.setAttributeByName("#F.category10", "readOnly", true);
	if(document.forms[0]["#category"].value=="change")
	{
//變更時-所有人皆可輸入供應商編號
		vat.item.setAttributeByName("#F.supplierCode", "readOnly", false);
		if(loginDepartment=="122")
		{
//變更時-只有商管可輸入供應商統編
			
       		vat.item.setAttributeByName("#F.identityCode", "readOnly", false);
       		vat.item.setAttributeByName("#F.enable", "readOnly", false);
       		vat.item.setAttributeByName("#F.category09", "readOnly", false);
    		vat.item.setAttributeByName("#F.category10", "readOnly", false);
     	}
	}
	else if(document.forms[0]["#category"].value=="new"||document.forms[0]["#category"].value=="sign")
	{
		vat.item.setAttributeByName("#F.identityCode", "readOnly", false);
//新增時-僅有商管可以變更供應商編號與統編
		if(loginDepartment=="122")
		{
			
       		vat.item.setAttributeByName("#F.supplierCode", "readOnly", false);
       		vat.item.setAttributeByName("#F.enable", "readOnly", false);
       		vat.item.setAttributeByName("#F.category09", "readOnly", false);
    		vat.item.setAttributeByName("#F.category10", "readOnly", false);
		}
	}

}

/* 執行送出 */
function execSubmitAction(formAction){

	var loginEmployeeCode= document.forms[0]["#loginEmployeeCode"].value;
	var formId               = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var processId            = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var status               = vat.item.getValueByName("#F.status");
   	var assignmentId         = vat.item.getValueByName("#assignmentId");
    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	var approvalResult  = vat.item.getValueByName("#F.approvalResult");
    if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
    var formStatus = status;
    if("SAVE" == formAction){
        formStatus = "SAVE";
    }else if("SUBMIT" == formAction){
        formStatus = changeFormStatus(formId, processId, status, approvalResult);
    }else if("VOID" == formAction){
        formStatus = "VOID";
    }	
    alert("approvalResult = " + approvalResult);
	vat.bean().vatBeanOther={
	//		beforeStatus: beforeStatus,
	//      nextStatus: nextStatus,
	        loginEmployeeCode: loginEmployeeCode,
	        assignmentId: assignmentId,
	        processId: processId,
	        approvalResult: approvalResult,
	        approvalComment: approvalComment
        };
        
	if ("SUBMIT" == formAction){
		vat.block.submit(function(){return "process_object_name=buSupplierModAction"+
            	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
	}
	
}

/* 變更供應商設定相關資料 */
function onChangeSupplierCode(status){

	//MACO 只有在變更時使用者輸入供應商編號時自動帶入相關資訊
	if(vat.item.getValueByName("#F.status") == "SAVE"&&vat.bean().vatBeanOther.category=="change")
	{
	var supplierCode = vat.item.getValueByName("#F.supplierCode").toUpperCase();
	var processString = "process_object_name=buSupplierModService&process_object_method_name=getAJAXFormDataBySupplier"+
						"&supplierCode="  + supplierCode +
						"&organizationCode=TM"+
						"&brandCode="     + vat.item.getValueByName("#F.brandCode") ;
	vat.ajax.startRequest(processString,  function() { 
		if (vat.ajax.handleState()){
		if(vat.item.getValueByName("#F.status") == "SAVE"){
				vat.item.setValueByName("#F.supplierTypeCode", vat.ajax.getValue("SupplierTypeCode", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.categoryCode",     vat.ajax.getValue("CategoryCode",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.identityCode",     vat.ajax.getValue("IdentityCode",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("PaymentTermCode", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.priceTermCode",   vat.ajax.getValue("PriceTermCode",   vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.taxType",         vat.ajax.getValue("TaxType",         vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.taxRate",         vat.ajax.getValue("TaxRate",         vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.invoiceTypeCode", vat.ajax.getValue("InvoiceTypeCode", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.invoiceDeliveryCode", vat.ajax.getValue("InvoiceDeliveryCode",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.enable",		  vat.ajax.getValue("Enable",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.billStyleCode",   vat.ajax.getValue("BillStyleCode",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.grade",   		  vat.ajax.getValue("Grade",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.consign",   	  vat.ajax.getValue("Consign",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.acceptEMail",      vat.ajax.getValue("AcceptEMail",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.other",   		  vat.ajax.getValue("Other",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.customsBroker",   vat.ajax.getValue("CustomsBroker",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.agent",   	   	  vat.ajax.getValue("Agent",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.commissionRate",  vat.ajax.getValue("CommissionRate",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.priceTermCode",  vat.ajax.getValue("PriceTermCode",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.createdBy",  	vat.ajax.getValue("CreatedBy",vat.ajax.xmlHttp.responseText));				
			    vat.item.setValueByName("#F.creationDate",  vat.ajax.getValue("CreationDate",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.addressBookId",  vat.ajax.getValue("AddressBookId",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.bankName", 		 vat.ajax.getValue("BankName",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.branchCode",  	 vat.ajax.getValue("BranchCode",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.accountCode",  	 vat.ajax.getValue("AccountCode",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.accepter",  	 vat.ajax.getValue("Accepter",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.contactPerson1", vat.ajax.getValue("ContactPerson1",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.tel1", 			 vat.ajax.getValue("Tel1",vat.ajax.xmlHttp.responseText));				
				vat.item.setValueByName("#F.fax1", 			 vat.ajax.getValue("Fax1",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.area1", 		 vat.ajax.getValue("Area1",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.contactPerson2", vat.ajax.getValue("ContactPerson2",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.tel2", 			 vat.ajax.getValue("Tel2",vat.ajax.xmlHttp.responseText));				
				vat.item.setValueByName("#F.fax2", 			 vat.ajax.getValue("Fax2",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.area2", 		 vat.ajax.getValue("Area2",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.contactPerson3", vat.ajax.getValue("ContactPerson3",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.tel3", 			 vat.ajax.getValue("Tel3",vat.ajax.xmlHttp.responseText));				
				vat.item.setValueByName("#F.fax3", 			 vat.ajax.getValue("Fax3",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.area3", 		 vat.ajax.getValue("Area3",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.contactPerson4", vat.ajax.getValue("ContactPerson4",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.tel4", 			 vat.ajax.getValue("Tel4",vat.ajax.xmlHttp.responseText));				
				vat.item.setValueByName("#F.fax4", 			 vat.ajax.getValue("Fax4",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.area4", 		 vat.ajax.getValue("Area4",vat.ajax.xmlHttp.responseText));																						
				vat.item.setValueByName("#F.address1", 		 vat.ajax.getValue("Address1",vat.ajax.xmlHttp.responseText));		
				vat.item.setValueByName("#F.address2", 		 vat.ajax.getValue("Address2",vat.ajax.xmlHttp.responseText));	
				vat.item.setValueByName("#F.address3", 		 vat.ajax.getValue("Address3",vat.ajax.xmlHttp.responseText));	
				vat.item.setValueByName("#F.address4", 		 vat.ajax.getValue("Address4",vat.ajax.xmlHttp.responseText));						
				vat.item.setValueByName("#F.EMail1", 		 vat.ajax.getValue("EMail1",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.EMail2", 		 vat.ajax.getValue("EMail2",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.EMail3", 		 vat.ajax.getValue("EMail3",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.EMail4", 		 vat.ajax.getValue("EMail4",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.remark1", 		 vat.ajax.getValue("Remark1",vat.ajax.xmlHttp.responseText));				
				vat.item.setValueByName("#F.remark2", 		 vat.ajax.getValue("Remark2",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.area", 			 vat.ajax.getValue("Area",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.zipCode", 		 vat.ajax.getValue("ZipCode",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.zipCode1", 		 vat.ajax.getValue("ZipCode1",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.zipCode2", 		 vat.ajax.getValue("ZipCode2",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.zipCode3", 		 vat.ajax.getValue("ZipCode3",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.zipCode4", 		 vat.ajax.getValue("ZipCode4",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.city", 		 	 vat.ajax.getValue("City",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.city1", 		 vat.ajax.getValue("City1",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.city2", 		 vat.ajax.getValue("City2",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.city3", 		 vat.ajax.getValue("City3",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.city4", 		 vat.ajax.getValue("City4",vat.ajax.xmlHttp.responseText));												
				vat.item.setValueByName("#F.chineseName", 		 vat.ajax.getValue("ChineseName",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.englishName", 		 vat.ajax.getValue("EnglishName",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.superintendent",     vat.ajax.getValue("Superintendent",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.shortName",     vat.ajax.getValue("ShortName",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.countryCode",     vat.ajax.getValue("CountryCode",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category01",     vat.ajax.getValue("CateGory01",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category02",     vat.ajax.getValue("CateGory02",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category03",     vat.ajax.getValue("CateGory03",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category04",     vat.ajax.getValue("CateGory04",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category05",     vat.ajax.getValue("CateGory05",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category06",     vat.ajax.getValue("CateGory06",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category07",     vat.ajax.getValue("CateGory07",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category08",     "change");
				vat.item.setValueByName("#F.category09",     vat.ajax.getValue("CateGory09",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category10",     vat.ajax.getValue("CateGory10",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category11",     vat.ajax.getValue("CateGory11",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category12",     vat.ajax.getValue("CateGory12",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category13",     vat.ajax.getValue("CateGory13",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category14",     vat.ajax.getValue("CateGory14",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category15",     vat.ajax.getValue("CateGory15",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category16",     vat.ajax.getValue("CateGory16",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category17",     vat.ajax.getValue("CateGory17",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category18",     vat.ajax.getValue("CateGory18",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category19",     vat.ajax.getValue("CateGory19",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.category20",     vat.ajax.getValue("CateGory20",vat.ajax.xmlHttp.responseText));	
				vat.item.setValueByName("#F.companyAddress",     vat.ajax.getValue("CompanyAddress",vat.ajax.xmlHttp.responseText));						
				vat.item.setValueByName("#F.telDay",     vat.ajax.getValue("TelDay",vat.ajax.xmlHttp.responseText));				
				vat.item.setValueByName("#F.telNight",     vat.ajax.getValue("TelNight",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.companyFax1",     vat.ajax.getValue("CompanyFax1",vat.ajax.xmlHttp.responseText));				
				vat.item.setValueByName("#F.companyFax2",     vat.ajax.getValue("CompanyFax2",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.companyEmail",     vat.ajax.getValue("CompanyEmail",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.remark3",     vat.ajax.getValue("Remark3",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.categoryType",     vat.ajax.getValue("CategoryType",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.type",     vat.ajax.getValue("Type",vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.companyName",     vat.ajax.getValue("companyName",vat.ajax.xmlHttp.responseText));								
				} 
  			}
		} );
	}
}
	
	/* 供應商picker 回來執行 */
function doAfterPickerSupplier( X, opType ){
	if(vat.bean().vatBeanPicker.result !== null){
		for(i=0 ; i<vat.bean().vatBeanPicker.result.length ; i++ ){
			vat.ajax.XHRequest({ 
				post:"process_object_name=buSupplierModService&process_object_method_name=getAJAXFormDataBySupplier"+
								"&addressBookId="  + vat.bean().vatBeanPicker.result[i].addressBookId +
								"&organizationCode=TM"+
								"&brandCode="      + vat.item.getValueByName("#F.brandCode"),
								
			          asyn:false,                      
				find: function doAfterPickerSupplierReturn(oXHR){
									// imReceiveHead
							vat.item.setValueByName("#F.supplierCode",    vat.ajax.getValue("SupplierCode",    oXHR.responseText));
							vat.item.setValueByName("#F.currencyCode",    vat.ajax.getValue("CurrencyCode",    oXHR.responseText));
							vat.item.setValueByName("#F.supplierTypeCode",vat.ajax.getValue("supplierTypeCode",oXHR.responseText));
							vat.item.setValueByName("#F.categoryType",    vat.ajax.getValue("categoryType",    oXHR.responseText));
							vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("PaymentTermCode", oXHR.responseText));
							vat.item.setValueByName("#F.tradeTeam",       vat.ajax.getValue("PriceTermCode",   oXHR.responseText));
							vat.item.setValueByName("#F.taxType",         vat.ajax.getValue("TaxType",         oXHR.responseText));
							vat.item.setValueByName("#F.taxRate",         vat.ajax.getValue("TaxRate",         oXHR.responseText));
							vat.item.setValueByName("#F.identityCode",    vat.ajax.getValue("IdentityCode",    oXHR.responseText));
						
					}
			});
		}
	}
}
	
function onChangeCompanyName(){
	var chineseName = vat.item.getValueByName("#F.chineseName");
	//alert(chineseName);
	//vat.item.setAttributeByName("#F.currencyCode", "readOnly", false);
	vat.item.setValueByName("#F.companyName", chineseName);
}
function onChangeTaxType(){

	var taxType = vat.item.getValueByName("#F.TaxType");
	if(taxType==="3")
	{
		vat.item.setValueByName("#F.TaxRate", "5");
	}
	else
	{
		vat.item.setValueByName("#F.TaxRate", "0");
	}
}

//隨機產生亂數字串
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
function copyInfo(copyType){

	var sorceContactPerson="";
	var sorceTel="";
	var sorceFax="";
	var sorceEMail="";
	var sorceCity="";
	var sorceArea="";
	var sorceZipCode="";
	var sorceAddress="";
	var targetContactPerson="";
	var targetTel="";
	var targetFax="";
	var targetEMail="";
	var targetCity="";
	var targetArea="";
	var targetZipCode="";
	var targetAddress="";
	if(copyType == 'address_1'){
	
		sorceCity 	 = vat.item.getValueByName("#F.city");
		sorceArea 	 = vat.item.getValueByName("#F.area");
		sorceZipCode = vat.item.getValueByName("#F.zipCode");
		sorceAddress = vat.item.getValueByName("#F.companyAddress");
		targetCity="#F.city1";
		targetArea="#F.area1";
		targetZipCode="#F.zipCode1";
		targetAddress="#F.address1";

	}
	else if(copyType == 'address_2'){
		sorceCity 	 = vat.item.getValueByName("#F.city1");
		sorceArea 	 = vat.item.getValueByName("#F.area1");
		sorceZipCode = vat.item.getValueByName("#F.zipCode1");
		sorceAddress = vat.item.getValueByName("#F.address1");
		targetCity="#F.city2";
		targetArea="#F.area2";
		targetZipCode="#F.zipCode2";
		targetAddress="#F.address2";
	}
	else if(copyType == 'address_3'){
		sorceCity 	 = vat.item.getValueByName("#F.city1");
		sorceArea 	 = vat.item.getValueByName("#F.area1");
		sorceZipCode = vat.item.getValueByName("#F.zipCode1");
		sorceAddress = vat.item.getValueByName("#F.address1");
		targetCity="#F.city3";
		targetArea="#F.area3";
		targetZipCode="#F.zipCode3";
		targetAddress="#F.address3";
	}
	else if(copyType == 'address_4'){
		sorceCity 	 = vat.item.getValueByName("#F.city1");
		sorceArea 	 = vat.item.getValueByName("#F.area1");
		sorceZipCode = vat.item.getValueByName("#F.zipCode1");
		sorceAddress = vat.item.getValueByName("#F.address1");
		targetCity="#F.city4";
		targetArea="#F.area4";
		targetZipCode="#F.zipCode4";
		targetAddress="#F.address4";
	}
	else if(copyType == 'classify_1'){
		sorceContactPerson 	 = vat.item.getValueByName("#F.contactPerson1");
		sorceTel 			 = vat.item.getValueByName("#F.tel1");
		sorceFax 			 = vat.item.getValueByName("#F.fax1");
		sorceEMail 			 = vat.item.getValueByName("#F.EMail1");
		targetContactPerson  = "#F.contactPerson1";
		targetTel 			 = "#F.tel1";
		targetFax 			 = "#F.fax1";
		targetEMail 		 = "#F.EMail1";
	}
	else if(copyType == 'classify_2'){

		sorceContactPerson 	 = vat.item.getValueByName("#F.contactPerson1");
		sorceTel 			 = vat.item.getValueByName("#F.tel1");
		sorceFax 			 = vat.item.getValueByName("#F.fax1");
		sorceEMail 			 = vat.item.getValueByName("#F.EMail1");
		targetContactPerson  = "#F.contactPerson2";
		targetTel 			 = "#F.tel2";
		targetFax 			 = "#F.fax2";
		targetEMail 		 = "#F.EMail2";

	}
	else if(copyType == 'classify_3'){
		sorceContactPerson 	 = vat.item.getValueByName("#F.contactPerson1");
		sorceTel 			 = vat.item.getValueByName("#F.tel1");
		sorceFax 			 = vat.item.getValueByName("#F.fax1");
		sorceEMail 			 = vat.item.getValueByName("#F.EMail1");
		targetContactPerson  = "#F.contactPerson3";
		targetTel 			 = "#F.tel3";
		targetFax 			 = "#F.fax3";
		targetEMail 		 = "#F.EMail3";
	}
	else if(copyType == 'classify_4'){
		sorceContactPerson 	 = vat.item.getValueByName("#F.contactPerson1");
		sorceTel 			 = vat.item.getValueByName("#F.tel1");
		sorceFax 			 = vat.item.getValueByName("#F.fax1");
		sorceEMail 			 = vat.item.getValueByName("#F.EMail1");
		targetContactPerson  = "#F.contactPerson4";
		targetTel 			 = "#F.tel4";
		targetFax 			 = "#F.fax4";
		targetEMail 		 = "#F.EMail4";
	}
	
	var processString = "process_object_name=buSupplierModService&process_object_method_name=getInfoAsTop"+
						"&copyType="  			+ copyType +
						"&sorceContactPerson="	+ sorceContactPerson +
						"&sorceTel="     		+ sorceTel +
						"&sorceFax="     		+ sorceFax +
						"&sorceEMail="   		+ sorceEMail +
						"&sorceCity="   		+ sorceCity +
						"&sorceArea="   		+ sorceArea +
						"&sorceZipCode=" 		+ sorceZipCode +
						"&sorceAddress=" 		+ sorceAddress;
	vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				if(copyType.indexOf("address_")<=-1 ){
					vat.item.setValueByName(targetContactPerson	, vat.ajax.getValue("sorceContactPerson", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetTel			, vat.ajax.getValue("sorceTel", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetFax			, vat.ajax.getValue("sorceFax", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetEMail			, vat.ajax.getValue("sorceEMail", vat.ajax.xmlHttp.responseText));
				}
				else{
					//alert(targetCity+targetArea+targetZipCode+targetAddress);
					vat.item.setValueByName(targetCity			, vat.ajax.getValue("sorceCity", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetArea			, vat.ajax.getValue("sorceArea", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetZipCode		, vat.ajax.getValue("sorceZipCode", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetAddress		, vat.ajax.getValue("sorceAddress", vat.ajax.xmlHttp.responseText));
				}
  			}
		} );

}