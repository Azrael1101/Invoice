/*** 
 *	檔案: cmCustomsDeclaration.js
 *	說明：表單明細
 *	修改：Joe
 *  <pre>
 *  	Created by Joe
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 5;
var vnB_vatT2 = 2;
var vnB_vatT3 = 3;
var vnB_vatT4 = 4;
var vnB_Detail = 6;

function kweImBlock(){
  	kweImInitial();
	kweButtonLine();
  	kweImHeader();
	var processId = document.forms[0]["#processId"         ].value;
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");         
		vat.tabm.createButton(0 ,"xTab1","主檔資料" ,"vatMasterDiv" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false, "");         
		vat.tabm.createButton(0 ,"xTab2","報單明細檔T2" ,"vatT2Div" ,"images/tab_declarationT2_dark.gif" ,"images/tab_declarationT2_light.gif", "inline", "doPageDataSave()");                                                                                                                                                                                                                       
		vat.tabm.createButton(0 ,"xTab3","報單明細檔T3" ,"vatT3Div" ,"images/tab_declarationT3_dark.gif" ,"images/tab_declarationT3_light.gif", "inline", "doPageDataSave()" );                                                                                                    
		vat.tabm.createButton(0 ,"xTab4","報單明細檔T4" ,"vatT4Div" ,"images/tab_declarationT4_dark.gif" ,"images/tab_declarationT4_light.gif", "inline", "doPageDataSave()" );
		vat.tabm.createButton(0 ,"xTab6","報單修改紀錄" ,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", "inline", ""); 
  	}
  	
  	kweMaster();  
	kweImVatT2();
	kweImVatT3();
	kweImVatT4();
	kweDetail();
	doFormAccessControl();
}

function kweImInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,	
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          currentRecordNumber : 0,
	      lastRecordNumber    : 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=cmDeclarationAction&process_object_method_name=performInitial"; 
	    	},{
	    		other: true
    	});
  	}
}

function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
	 			{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Cm_CustomsDeclaration:search:20091010.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("WAIT_IN")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			/*
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},	 			
				{name:"#B.message"    , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			*/
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
}

// 主檔
function kweImHeader(){ 
	var allCurrencyCodes = vat.bean("allCurrencyCodes");
	vat.block.create( 0, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"海關進出倉訊息建立作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.declNo", 		type:"LABEL", 	value:"報關單號<font color='red'>*</font>"}]},
				{items:[{name:"#F.declNo", 		type:"TEXT", 	bind:"declNo", back:false, size:24},
						{name:"#F.headId", 		type:"TEXT",  	bind:"headId", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.msgFun", 		type:"LABEL", 	value:"異動別" }]},
				{items:[{name:"#F.msgFun", 		type:"TEXT", 	bind:"msgFun", size:12 }]},
				{items:[{name:"#L.bondNo", 		type:"LABEL", 	value:"海關監管編號" }]},
				{items:[{name:"#F.bondNo", 		type:"TEXT", 	bind:"bondNo", size:20 }]},
	 			{items:[{name:"#L.status", 		type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 		type:"TEXT", 	bind:"status", size:24, mode:"HIDDEN"},
				    	{name:"#F.statusName", 	type:"TEXT"  ,  bind:"statusName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.strType", 	type:"LABEL", 	value:"進出倉別"}]},	 
	 			{items:[{name:"#F.strType", 	type:"TEXT",  	bind:"strType", size:12},
	 					{name:"#L.strTypeDesc", type:"LABEL",	value:"　1:進倉　2:出倉", size:12}]},
			    {items:[{name:"#L.boxNo",		type:"LABEL", 	value:"報關行箱號" }]},
				{items:[{name:"#F.boxNo",		type:"TEXT", 	bind:"boxNo"}]},
				{items:[{name:"#L.declType",	type:"LABEL", 	value:"報單類別" }]},
				{items:[{name:"#F.declType",	type:"TEXT", 	bind:"declType" , size:24}]},		
				{items:[{name:"#L.employeeName",type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.employeeName",type:"TEXT", 	bind:"employeeName", mode:"READONLY"}]}
			]}	
		], 	
		beginService:"",
		closeService:""			
	});
}

function kweMaster(){

vat.block.create(vnB_Master, {
	id: "vatMasterDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		rows:[
			{row_style:"", cols:[
			    {items:[{name:"#L.importDate",	type:"LABEL", 	value:"進口日期" }]},
				{items:[{name:"#F.importDate",	type:"DATE", 	bind:"importDate" ,size:10 }]}, 
	 			{items:[{name:"#L.declDate",	type:"LABEL", 	value:"報關日期" }]},
				{items:[{name:"#F.declDate",	type:"DATE", 	bind:"declDate", size:10}]},
				{items:[{name:"#L.stgPlace",	type:"LABEL", 	value:"存放處所" }]},
				{items:[{name:"#F.stgPlace",	type:"TEXT", 	bind:"stgPlace"}]},
				{items:[{name:"#L.rlsTime",			type:"LABEL", 	value:"放行時間" }]},
				{items:[{name:"#F.rlsTime",			type:"DATE", 	bind:"rlsTime" , size:10}]}
		    ]},
			{row_style:"", cols:[
				{items:[{name:"#L.rlsPkg",		type:"LABEL", 	value:"放行件數" }]},
				{items:[{name:"#F.rlsPkg",		type:"TEXT", 	bind:"rlsPkg"}]},
				{items:[{name:"#L.extraCond",			type:"LABEL", 	value:"放行附帶條件" }]},
				{items:[{name:"#F.extraCond",			type:"TEXT", 	bind:"extraCond"}]},
				{items:[{name:"#L.lastUpdateDate",			type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.lastUpdateDate",			type:"TEXT", 	bind:"lastUpdateDate" , mode:"READONLY"}]},
				{items:[{name:"#L.pkgUnit",	type:"LABEL", 	value:"件數單位" }]},
				{items:[{name:"#F.pkgUnit",	type:"TEXT", 	bind:"pkgUnit"}]} 
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.gWgt",			type:"LABEL", 	value:"總重量" }]},
				{items:[{name:"#F.gWgt",			type:"TEXT", 	bind:"GWgt"}]},
				{items:[{name:"#L.vesselSign",		type:"LABEL", 	value:"船舶呼號" }]},
				{items:[{name:"#F.vesselSign",		type:"TEXT", 	bind:"vesselSign"}]},
				{items:[{name:"#L.voyageNo",			type:"LABEL", 	value:"航次" }]},
				{items:[{name:"#F.voyageNo",			type:"TEXT", 	bind:"voyageNo"}]},
				{items:[{name:"#L.shipCode",			type:"LABEL", 	value:"船公司代碼" }]},
				{items:[{name:"#F.shipCode",			type:"TEXT", 	bind:"shipCode"}]} 
			]},
		  	{row_style:"", cols:[
				{items:[{name:"#L.exporter",	type:"LABEL", 	value:"貨物輸出人" }]},
				{items:[{name:"#F.exporter",	type:"TEXT", 	bind:"exporter"}]},
				{items:[{name:"#L.clearType",			type:"LABEL", 	value:"通關方式" }]},
				{items:[{name:"#F.clearType",			type:"TEXT", 	bind:"clearType"}]},
				{items:[{name:"#L.refBillNo",		type:"LABEL", 	value:"參考單號" }]},
				{items:[{name:"#F.refBillNo",		type:"TEXT", 	bind:"refBillNo"}]},
				{items:[{name:"#L.inbondNo",			type:"LABEL", 	value:"進倉保稅業者代碼" }]},
				{items:[{name:"#F.inbondNo",			type:"TEXT", 	bind:"inbondNo"}]}
			]},
		 	{row_style:"", cols:[
		 	    {items:[{name:"#L.outbondNo",			type:"LABEL", 	value:"出倉保稅業者代碼" }]},
				{items:[{name:"#F.outbondNo",			type:"TEXT", 	bind:"outbondNo"}]},
				{items:[{name:"#L.shipPort",			type:"LABEL", 	value:"起運口岸" }]},
				{items:[{name:"#F.shipPort",			type:"TEXT", 	bind:"shipPort"}]},
				{items:[{name:"#L.countryCode",		type:"LABEL", 	value:"賣方國家代碼" }]},
				{items:[{name:"#F.countryCode",		type:"TEXT", 	bind:"countryCode"}]},
				{items:[{name:"#L.exportDate",			type:"LABEL", 	value:"國外出口日期" }]},
				{items:[{name:"#F.exportDate",			type:"DATE", 	bind:"exportDate"}]}
		 	]},
		 	{row_style:"", cols:[
				{items:[{name:"#L.mawbNo",	type:"LABEL", 	value:"提單主號" }]},
				{items:[{name:"#F.mawbNo",	type:"TEXT", 	bind:"mawbNo"}]},
				{items:[{name:"#L.hawbNo",			type:"LABEL", 	value:"提單分號" }]},
				{items:[{name:"#F.hawbNo",			type:"TEXT", 	bind:"hawbNo"}]},
				{items:[{name:"#L.originalDeclNo",		type:"LABEL", 	value:"原出口報單號碼" }]},
				{items:[{name:"#F.originalDeclNo",		type:"TEXT", 	bind:"originalDeclNo"}]},
				{items:[{name:"#L.buyPayName",			type:"LABEL", 	value:"買方名稱" }]},
				{items:[{name:"#F.buyPayName",			type:"TEXT", 	bind:"buyPayName"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.salePayName",	type:"LABEL", 	value:"賣方名稱" }]},
				{items:[{name:"#F.salePayName",	type:"TEXT", 	bind:"salePayName"}]},
				{items:[{name:"#L.buyBan",			type:"LABEL", 	value:"買方統一編號" }]},
				{items:[{name:"#F.buyBan",			type:"TEXT", 	bind:"buyBan"}]},
				{items:[{name:"#L.buyBfNo",		type:"LABEL", 	value:"買方海關監管編" }]},
				{items:[{name:"#F.buyBfNo",		type:"TEXT", 	bind:"buyBfNo"}]},
				{items:[{name:"#L.saleBfNo",			type:"LABEL", 	value:"賣方海關監管編" }]},
				{items:[{name:"#F.saleBfNo",			type:"TEXT", 	bind:"saleBfNo"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.relBfNo",	type:"LABEL", 	value:"原出口保稅廠商" }]},
				{items:[{name:"#F.relBfNo",	type:"TEXT", 	bind:"relBfNo"}]},
				{items:[{name:"#L.exWhBan",			type:"LABEL", 	value:"出倉保稅倉庫業" }]},
				{items:[{name:"#F.exWhBan",			type:"TEXT", 	bind:"exWhBan"}]},
				{items:[{name:"#L.imWhBan",		type:"LABEL", 	value:"進倉保稅倉庫業" }]},
				{items:[{name:"#F.imWhBan",		type:"TEXT", 	bind:"imWhBan"}]},
				{items:[{name:"#L.fobValue",			type:"LABEL", 	value:"離岸價格(台幣)" }]},
				{items:[{name:"#F.fobValue",			type:"TEXT", 	bind:"fobValue"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.originalFobValue",	type:"LABEL", 	value:"離岸價格(外幣)" }]},
				{items:[{name:"#F.originalFobValue",	type:"TEXT", 	bind:"originalFobValue"}]},
				{items:[{name:"#L.cifValue",			type:"LABEL", 	value:"起岸價格（台幣）" }]},
				{items:[{name:"#F.cifValue",			type:"TEXT", 	bind:"cifValue"}]},
				{items:[{name:"#L.originalCifValue",		type:"LABEL", 	value:"起岸價格（外幣）" }]},
				{items:[{name:"#F.originalCifValue",		type:"TEXT", 	bind:"originalCifValue"}]},
				{items:[{name:"#L.currencyCode",			type:"LABEL", 	value:"幣別" }]},
				{items:[{name:"#F.currencyCode",			type:"TEXT", 	bind:"currencyCode"}]}
			]},		
			{row_style:"", cols:[
				{items:[{name:"#L.exchangeRate",	type:"LABEL", 	value:"外幣匯率" }]},
				{items:[{name:"#F.exchangeRate",	type:"TEXT", 	bind:"exchangeRate"}]},
				{items:[{name:"#L.freightFee",			type:"LABEL", 	value:"運費" }]},
				{items:[{name:"#F.freightFee",			type:"TEXT", 	bind:"freightFee"}]},
				{items:[{name:"#L.insuranceFee",		type:"LABEL", 	value:"保險費" }]},
				{items:[{name:"#F.insuranceFee",		type:"TEXT", 	bind:"insuranceFee"}]},
				{items:[{name:"#L.additionFee",			type:"LABEL", 	value:"應加費用" }]},
				{items:[{name:"#F.additionFee",			type:"TEXT", 	bind:"additionFee"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.deductionFee",	type:"LABEL", 	value:"應減費用" }]},
				{items:[{name:"#F.deductionFee",	type:"TEXT", 	bind:"deductionFee"}]},
				{items:[{name:"#L.NWgt",			type:"LABEL", 	value:"總淨重" }]},
				{items:[{name:"#F.NWgt",			type:"TEXT", 	bind:"NWgt"}]},
				{items:[{name:"#L.moaType1",		type:"LABEL", 	value:"稅費金額代碼1" }]},
				{items:[{name:"#F.moaType1",		type:"TEXT", 	bind:"moaType1"}]},
				{items:[{name:"#L.dutyAmt1",			type:"LABEL", 	value:"稅費金額1" }]},
				{items:[{name:"#F.dutyAmt1",			type:"TEXT", 	bind:"dutyAmt1"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.moaType2",	type:"LABEL", 	value:"稅費金額代碼2" }]},
				{items:[{name:"#F.moaType2",	type:"TEXT", 	bind:"moaType2"}]},
				{items:[{name:"#L.dutyAmt2",			type:"LABEL", 	value:"稅費金額2" }]},
				{items:[{name:"#F.dutyAmt2",			type:"TEXT", 	bind:"dutyAmt2"}]},
				{items:[{name:"#L.moaType3",		type:"LABEL", 	value:"稅費金額代碼3" }]},
				{items:[{name:"#F.moaType3",		type:"TEXT", 	bind:"moaType3"}]},
				{items:[{name:"#L.dutyAmt3",			type:"LABEL", 	value:"稅費金額3" }]},
				{items:[{name:"#F.dutyAmt3",			type:"TEXT", 	bind:"dutyAmt3"}]}
			]},	
			{row_style:"", cols:[
				{items:[{name:"#L.moaType4",	type:"LABEL", 	value:"稅費金額代碼4" }]},
				{items:[{name:"#F.moaType4",	type:"TEXT", 	bind:"moaType4"}]},
				{items:[{name:"#L.dutyAmt4",			type:"LABEL", 	value:"稅費金額4" }]},
				{items:[{name:"#F.dutyAmt4",			type:"TEXT", 	bind:"dutyAmt4"}]},
				{items:[{name:"#L.moaType5",	type:"LABEL", 	value:"稅費金額代碼5" }]},
				{items:[{name:"#F.moaType5",	type:"TEXT", 	bind:"moaType5"}]},
				{items:[{name:"#L.dutyAmt5",			type:"LABEL", 	value:"稅費金額5" }]},
				{items:[{name:"#F.dutyAmt5",			type:"TEXT", 	bind:"dutyAmt5"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.moaType6",	type:"LABEL", 	value:"稅費金額代碼6" }]},
				{items:[{name:"#F.moaType6",	type:"TEXT", 	bind:"moaType6"}]},
				{items:[{name:"#L.dutyAmt6",			type:"LABEL", 	value:"稅費金額6" }]},
				{items:[{name:"#F.dutyAmt6",			type:"TEXT", 	bind:"dutyAmt6"}]},
				{items:[{name:"#L.moaType7",	type:"LABEL", 	value:"稅費金額代碼7" }]},
				{items:[{name:"#F.moaType7",	type:"TEXT", 	bind:"moaType7"}]},
				{items:[{name:"#L.dutyAmt7",			type:"LABEL", 	value:"稅費金額7" }]},
				{items:[{name:"#F.dutyAmt7",			type:"TEXT", 	bind:"dutyAmt7"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.dutyAmt",	type:"LABEL", 	value:"進口稅金額" }]},
				{items:[{name:"#F.dutyAmt",	type:"TEXT", 	bind:"dutyAmt"}]},
				{items:[{name:"#L.totalDuty",			type:"LABEL", 	value:"稅費合計" }]},
				{items:[{name:"#F.totalDuty",			type:"TEXT", 	bind:"totalDuty"}]},
				{items:[{name:"#L.dutyDase",		type:"LABEL", 	value:"營業稅稅基" }]},
				{items:[{name:"#F.dutyDase",		type:"TEXT", 	bind:"dutyDase"}]},
				{items:[{name:"#L.sendDate",			type:"LABEL", 	value:"送出日期" }]},
				{items:[{name:"#F.sendDate",			type:"DATE", 	bind:"sendDate"}]}
			]}			
 	 ],
		beginService:"",
		closeService:""			
	});
}

	
	// 報單明細檔
function kweImVatT2(){
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;

    vat.item.make(vnB_vatT2, "indexNo"  		, {type:"IDX"  , view:"fixed" , desc:"項次" });
	vat.item.make(vnB_vatT2, "itemNo"			, {type:"TEXT" , view:"", size:4, desc:"項次",mode:"HIDDEN"});
	vat.item.make(vnB_vatT2, "buyCommNo"		, {type:"TEXT" , view:"", size:15, desc:"買方料號"});
	vat.item.make(vnB_vatT2, "descrip"		    , {type:"TEXT" , view:"", size:20, desc:"貨名"});
	vat.item.make(vnB_vatT2, "unit" 		    , {type:"TEXT" , view:"", size:8, desc:"單位"});
	vat.item.make(vnB_vatT2, "spec"		        , {type:"TEXT" , view:"", size:8, desc:"規格"});
	vat.item.make(vnB_vatT2, "brand"		    , {type:"TEXT" , view:"", size:8, desc:"廠牌"});
	vat.item.make(vnB_vatT2, "model"		    , {type:"TEXT" , view:"", size:8, desc:"型號"});
	vat.item.make(vnB_vatT2, "NWght"		    , {type:"NUMB" , view:"", size:4, desc:"淨種"});
	vat.item.make(vnB_vatT2, "qty"		        , {type:"NUMB" , view:"", size:4, desc:"數量"});
	vat.item.make(vnB_vatT2, "unitPrice"		, {type:"NUMB" , view:"", size:6, desc:"單價"});
	vat.item.make(vnB_vatT2, "ODeclNo"		    , {type:"TEXT" , view:"shift", size:16, desc:"原報單號碼"});
	vat.item.make(vnB_vatT2, "OItemNo"		    , {type:"NUMB" , view:"shift", size:4, desc:"原項次"});
	vat.item.make(vnB_vatT2, "code"				, {type:"TEXT" , view:"shift", size:6, desc:"商品分類號列"});
	vat.item.make(vnB_vatT2, "produceCountry"	, {type:"TEXT" , view:"shift", size:6, desc:"生產國別"});
	vat.item.make(vnB_vatT2, "permitNo"			, {type:"TEXT" , view:"shift", size:12, desc:"簽審機關輸入許可證號碼"});
	vat.item.make(vnB_vatT2, "itemId"           , {type:"ROWID"});

	vat.block.pageLayout(vnB_vatT2, {
								id: "vatT2Div",
								pageSize: 10,											
		            			canGridDelete : vbCanGridDelete,
								canGridAppend : vbCanGridAppend,
								canGridModify : vbCanGridModify,						
								appendBeforeService : "appendBeforeService()",
								loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT2+")",
								loadSuccessAfter    : "loadSuccessAfter()",						
								eventService        : "eventService()",   
								saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT2+")",
								saveSuccessAfter    : "saveSuccessAfter()"
								});
}

	// 車身號碼檔
function kweImVatT3(){
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
	
    vat.item.make(vnB_vatT3, "indexNo"  		, {type:"IDX"  , desc:"序號" });
	vat.item.make(vnB_vatT3, "itemNo"			, {type:"TEXT" , size:12, maxLen:12, desc:"報單項次" });
	vat.item.make(vnB_vatT3, "vehicleNo"	    , {type:"TEXT" , size:12, maxLen:12, desc:"車身號碼" });
	vat.item.make(vnB_vatT3, "itemId"          ,  {type:"HIDDEN"});
	vat.block.pageLayout(vnB_vatT3, {
														id: "vatT3Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT3+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT3+")",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
}
	
	// 貨櫃資料檔
function kweImVatT4(){
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
	
    vat.item.make(vnB_vatT4, "indexNo"  		, {type:"IDX"  , desc:"序號" });
    vat.item.make(vnB_vatT4, "contrNo"	        , {type:"TEXT" , size:12, maxLen:12, desc:"貨櫃號碼"});
	vat.item.make(vnB_vatT4, "contrType"	    , {type:"TEXT" , size:12, maxLen:12, desc:"貨櫃種類"});
	vat.item.make(vnB_vatT4, "transMode"	    , {type:"TEXT" , size:12, maxLen:12, desc:"車身號裝運方式"});
	vat.item.make(vnB_vatT4, "itemId"          ,  {type:"HIDDEN"});
	vat.block.pageLayout(vnB_vatT4, {
														id: "vatT4Div",
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT4+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT4+")",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
}	
	
function kweDetail(){
	var modifyTypes = vat.bean("modifyTypes");
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	var varCanGridModify = false;
	
    vat.item.make(vnB_Detail, "indexNo1", 		{type:"IDX" 	, view:"fixed", desc:"序號", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "itemNo1", 		{type:"NUMB"	, view:"fixed", size:6, maxLen:6, desc:"項次"});
	vat.item.make(vnB_Detail, "modType1", 		{type:"SELECT"	, view:"", init:modifyTypes, desc:"修改類型"});
	vat.item.make(vnB_Detail, "prdtNo1", 		{type:"TEXT"	, view:"", size: 16, maxLen:20, desc:"料號"});
	vat.item.make(vnB_Detail, "descrip1", 		{type:"TEXT"	, view:"", size: 80, desc:"貨名"});
	vat.item.make(vnB_Detail, "brand1", 		{type:"TEXT"	, view:"", size: 12, desc:"廠牌"});
	vat.item.make(vnB_Detail, "model1", 		{type:"TEXT"	, view:"shift", size: 12, desc:"型號"});
	vat.item.make(vnB_Detail, "spec1", 			{type:"TEXT"	, view:"shift", size: 12, desc:"規格"});
	vat.item.make(vnB_Detail, "NWght1", 		{type:"NUMB"	, view:"shift", size: 12, maxLen: 12, desc:"淨重"});
	vat.item.make(vnB_Detail, "qty1", 			{type:"NUMB"	, view:"shift", size: 12, maxLen:20, desc:"數量"});
	vat.item.make(vnB_Detail, "unit1", 			{type:"NUMB"	, view:"shift", size: 12, maxLen:20, desc:"單位"});
	vat.item.make(vnB_Detail, "ODeclNo1", 		{type:"TEXT"	, view:"shift", size: 12, maxLen:20, desc:"原進倉報單號碼"});
	vat.item.make(vnB_Detail, "OItemNo1", 		{type:"TEXT"	, view:"shift", size: 12, maxLen:20, desc:"原報單項次修改"});
	vat.item.make(vnB_Detail, "lineId1", 		{type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord1", 	{type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord1",{type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Detail, "message1", 		{type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnB_Detail, {
								id: "vatDetailDiv",	
								pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,									

								loadBeforeAjxService: "loadBeforeAjxServiceLog()",
								loadSuccessAfter    : "",
								eventService        : "",   
								saveBeforeAjxService: "saveBeforeAjxServiceLog()",
								saveSuccessAfter    : ""
								});
}

function loadBeforeAjxServiceLog(){
	var processString = "process_object_name=cmDeclarationLogService&process_object_method_name=getAJAXPageData" + 
	                    "&identify=" + vat.item.getValueByName("#F.headId");
	return processString;											
}
	
function saveBeforeAjxServiceLog(){
	processString = "process_object_name=wfApprovalResultService"+
					"&process_object_method_name=updateAJAXPageData";
	return processString;
}
// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.bean().vatBeanPicker.result = null;
    	vat.bean().vatBeanOther.firstRecordNumber = 0;
     	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
		var brandCode = document.forms[0]["#loginBrandCode" ].value;
		if(brandCode != "T2"){
			vat.tabm.displayToggle(0, "xTab6", false);
		}
		vat.item.setAttributeByName("#F.declNo"  , "readOnly", false);
    	refreshForm("");
	 }
}

function refreshForm(vsHeadId){
	document.forms[0]["#formId"            ].value = vsHeadId;
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=cmDeclarationAction&process_object_method_name=performInitial"; 
     	},{other: true, 
     	   funcSuccess:function(){
     	       vat.item.bindAll();
     	       vat.block.pageDataLoad(vnB_vatT2, vnCurrentPage = 1);
     	       vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     	       vat.block.pageDataLoad(vnB_vatT3, vnCurrentPage = 1);
     	       vat.block.pageDataLoad(vnB_vatT4, vnCurrentPage = 1);
     	       doFormAccessControl();
     	  }}
    );
    
}

function resetForm(){
    refreshForm("");
}	

function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
		result = vat.bean().vatBeanPicker.result;
	    var vsMaxSize = result.length;
	   
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  vsHeadId = result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(vsHeadId);
		  showDatails();
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}	
	
function doPageDataSave(div){
		vat.block.pageSearch(vnB_vatT2, {  
			funcSuccess:function(){
				vat.block.pageSearch(vnB_vatT3,{  
					funcSuccess:function(){
						vat.block.pageSearch(vnB_vatT4,{  
							funcSuccess:function(){
								vat.block.pageRefresh(vnB_Detail,{  
									funcSuccess:function(){
									}
								});
							}
						});		
					}
				});
			}
		});	
}
	
	
	// 第一次載入 重新整理
function loadBeforeAjxService(div){
    //alert("loadBeforeAjxService:"+div);	
	var processString = "";
		processString = "process_object_name=cmDeclarationHeadService&process_object_method_name=getAJAXVatT2PageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId")+
		                    "&tab="+div;
		return processString;				
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
   	//alert("saveBeforeAjxService"); 
	var processString = "";
	processString = "process_object_name=cmDeclarationHeadService&process_object_method_name=updateOrSaveAJAXVatT2PageLinesData" + 
			"&headId=" + vat.item.getValueByName("#F.headId") + 
			"&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value+
			"&tab="+div;
	return processString;
}   

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(){ 
	     //alert("saveSuccessAfter");
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
    //alert("loadSuccessAfter");
    vat.block.pageRefresh(0);
} 

// 新增空白頁
function appendBeforeService(){
//    alert("appendBeforeService");	 
	return true;
}    

// 新增空白頁成功後
function appendAfterService(){
//    alert("appendAfterService");	
} 

function eventService(){
} 
	
	// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("WAIT_IN" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){
		alertMessage = "是否確定暫存?";
	}else if("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}
	
	if(confirm(alertMessage)){
	    var formId 				  = document.forms[0]["#formId"            ].value; 
		var processId             = document.forms[0]["#processId"         ].value;
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var approvalResult        = "true"; // vat.item.getValueByName("#F.approvalResult"); 
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
		vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,	
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0,
		  formAction 			: formAction,
		  formStatus 			: status,
	  	  approvalResult  		: approvalResult,
	  	  approvalComment 		: approvalComment
        };
		if("SUBMIT_BG" == formAction){
	      	vat.block.submit(function(){return "process_object_name=imLetterOfCreditAction"+
	                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
	    }else{
			vat.block.submit(function(){return "process_object_name=cmDeclarationAction"+
	                    "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
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
    if(vat.bean().vatBeanOther.firstRecordNumber > 0) {
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

function showMessage(){
	var width = "600";
    var height = "400";  
	window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=CM_DECLARATION" +
		"&levelType=ERROR" +
        "&processObjectName=cmDeclarationHeadService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}	

function doFormAccessControl(){

	var headId = vat.item.getValueByName("#F.headId");
    var formStatus = vat.item.getValueByName("#F.status");
    var processId = vat.bean().vatBeanOther.processId;
    var brandCode = document.forms[0]["#loginBrandCode" ].value;
    var canBeClaimed	= document.forms[0]["#canBeClaimed"].value;
	var canBeMod	= document.forms[0]["#canBeMod"].value;
	vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", false, true, true);
	vat.item.setAttributeByName("vatMasterDiv"  , "readOnly", false, true, true);
	vat.block.canGridModify( [vnB_vatT2], true, true, true );
	vat.block.canGridModify( [vnB_Detail], true, true, true );
	vat.block.canGridModify( [vnB_vatT3], true, true, true );
	vat.block.canGridModify( [vnB_vatT4], true, true, true );
		
	if(canBeClaimed == "true"){
		vat.item.setStyleByName("#B.submit",    "display", "none");
	}
	if(processId != ""){   
		vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
		vat.item.setAttributeByName("vatMasterDiv"  , "readOnly", true);
		vat.item.setStyleByName("#B.search"	, "display", "none");
		vat.item.setStyleByName("#B.new"	, "display", "none");
	}
	
	if(brandCode == "T2" && "Y" != canBeMod){
		vat.item.setStyleByName("#B.new"	, "display", "none");
		vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true, true, true);
		vat.item.setAttributeByName("vatMasterDiv"  , "readOnly", true, true, true);
		
		if(processId == ""){
			vat.item.setStyleByName("#B.submit"	, "display", "none");
			vat.item.setAttributeByName("#F.strType", "readOnly", true);
			vat.block.canGridModify( [vnB_vatT2], false, false, false );	
		}else{
			vat.item.setAttributeByName("#F.strType", "readOnly", false);
			vat.block.canGridModify( [vnB_vatT2], true, false, false );
		}
		
		//vat.block.canGridModify( [vnB_vatT2], true, false, false );
		vat.block.canGridModify( [vnB_Detail], false, false, false );
		vat.block.canGridModify( [vnB_vatT3], false, false, false );
		vat.block.canGridModify( [vnB_vatT4], false, false, false );
	}else{
		if("0" == headId || "" == headId){
			vat.block.canGridModify( [vnB_vatT2], false, false, false );
			vat.block.canGridModify( [vnB_Detail], false, false, false );
			vat.block.canGridModify( [vnB_vatT3], false, false, false );
			vat.block.canGridModify( [vnB_vatT4], false, false, false );
		}
	}
	   vat.block.pageDataLoad(vnB_vatT2, vnCurrentPage = 1);
       vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
       vat.block.pageDataLoad(vnB_vatT3, vnCurrentPage = 1);
       vat.block.pageDataLoad(vnB_vatT4, vnCurrentPage = 1);
	
}

function showDatails(){
	var brandCode = document.forms[0]["#loginBrandCode" ].value;
	document.forms[0]["#formId"].value = vat.bean("formId");
	vat.item.setAttributeByName("#F.declNo"  , "readOnly", true);
	vat.item.setValueByName("#F.headId" , vat.bean("formId"));
	vat.item.setValueByName("#F.status" , vat.bean("status"));
	vat.item.setValueByName("#F.statusName" , vat.bean("statusName"));
	if(brandCode == "T2"){
		vat.tabm.displayToggle(0, "xTab6", true, false, false);
	}
	doFormAccessControl();
}