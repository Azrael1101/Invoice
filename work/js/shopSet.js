vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_master = 2;
var vnB_ShopSet = 25;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);    
		vat.tabm.createButton(0 ,"xTab12","專櫃申請"    ,"vatShopDetailDiv" ,"images/tab_shop_detail_dark.GIF","images/tab_shop_detail_light.GIF");
	}
	
	kweMaster();
	kweShopDetail();
	//doFormAccessControl();///呼叫doFormAccessControl method存取權限
    //ApprovalInitial();
  //簽核頁面
  /*kweWfBlock( vat.item.getValueByName("#F.brandCode"), 
            	vat.item.getValueByName("#F.orderTypeCode"), 
             	vat.item.getValueByName("#F.orderNo"),
             	document.forms[0]["#loginEmployeeCode"].value );*/
}

function formInitial(){

	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther =
		{ 
			loginDepartment  	    : document.forms[0]["#loginDepartment"   ].value,//===""?"103":document.forms[0]["#loginDepartment" ].value,
			brandCode  		    : document.forms[0]["#loginBrandCode"    ].value,
			loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
			formId             	: document.forms[0]["#formId"            ].value,
			//orderTypeCode         : document.forms[0]["#orderTypeCode" 	 ].value,
			orderTypeCode         : "SOA",
			processId          	: document.forms[0]["#processId"         ].value,
			assignmentId      	: document.forms[0]["#assignmentId"      ].value,
			type      	        : document.forms[0]["#type"      ].value,
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
				return "process_object_name=shopSetAction&process_object_method_name=performInitial"; 
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
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 servicePassData:function()
										  	{return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode")},
	 									 service:"Shop_Set:search:20170816.page",//+document.forms[0]["#orderTypeCode" ].value,
	 									 servicePassData:function(x){ return doPassHeadData(x); },
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.signing"      , type:"IMG"    ,value:"簽核",   src:"./images/play-first.png", eClick:'doSubmit("SIGNING")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.attachFile"	, type:"IMG"    ,value:"插入附件",   src:"./images/button_attachment.gif", eClick:'doUpload()'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.getMenu"      , type:"IMG"    ,value:"取得主管選單資料",   src:"./images/getMaMenu.jpg", eClick:"getOriginalShopData('boss')"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print3"       , type:"IMG"    ,value:"權限申請單列印",   src:"./images/print_require.png" , eClick:'printMenuApplication()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print4"       , type:"IMG"    ,value:"權限申請單列印-異動",   src:"./images/button_attachment.gif" , eClick:"printMenuApplication('isUpdate')"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"SPACE"           , type:"LABEL" ,value:"　　"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}	

function headerInitial(){

    var ifCostControl=[["","",false],
                 ["是","否"],
                 ["Y","N"]];
    var ifWareHouseControl=[["","",false],
                 ["是","否"],
                 ["Y","N"]];             
	var orderTypeCode       = vat.item.getValueByName("#orderTypeCode");
    var type                = vat.item.getValueByName("#type");
    var userType    		= vat.bean("userType");
 	var allCategroyTypes    = vat.bean("allCategroyTypes");
 	var allproject          = vat.bean("allproject");
 	var alldepartment       = vat.bean("alldepartment");
 	var allstatus           = [[true, true,true,true,true], ["暫存", "執行中", "結案","作廢"],["SAVE","PLAN","FINISH","VOID"]];
 	var allstatusLoa           = [[true,true,true], ["暫存", "簽核中","簽核完成"],["SAVE","SIGNING","FINISH"]];
 	var purpose           = [[true,true], ["新增", "修改"],["NEW","MODIFY"]];
 	var vsCostStyle=type !="1"?" style= 'display:none;'":""; ;
		vat.block.create( vnB_Header, { //vnB_Header = 
			id: "vatBlock_Head", generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"權限申請作業", 
			rows:[
				{row_style:"", cols:[
					{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌/<font color='red'></font>"},
							{name:"#L.orderTypeCode", type:"LABEL", value:"單別/<font color='red'></font>"},
							{name:"#L.orderNo", type:"LABEL" , value:"單號<font color='red'></font>"}]},
					{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:3, maxLen:3, mode:"READONLY"},
							{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", size:3, maxLen:3,mode:"READONLY" },
							{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:25, maxLen:25,mode:"READONLY" },
							{name:"#F.headId"     , type:"TEXT"  ,  size:4, bind:"headId" , back:false, mode:"READONLY"}]},
					{items : [{name:"#L.mod", type:"LABEL", value:"模組"}]},
					{items : [{name:"#F.mod", type:"SELECT", bind:"costControl" ,size:25, maxLen:25 , init:ifCostControl}]},
							
					{items:[{name:"#L.contractTel",     type:"LABEL",  value:"聯絡電話/分機<font color='red'>*</font>"}]},	 
		 			{items:[{name:"#F.contractTel",         type:"NUMM",   bind:"contractTel",         size:10}]},
					{items:[{name:"#L.department", type:"LABEL" , value:"需求部門"}]},
					{items:[{name:"#F.department", type:"SELECT", bind:"department", size:25, maxLen:25,init:alldepartment }]}]},
				{row_style:"", cols:[
					{items : [{name : "#L.no", type : "LABEL", value : "主旨<font color='red'>*</font>"}]},
					{items : [{name : "#F.no", type : "TEXT", bind:"no",  size : 30,mode:"READONLY"}],td:" colSpan=3"},
					{items : [{name : "#L.requestDate", type : "LABEL", value : "需求日期<font color='red'></font>"}]},
					{items : [{name : "#F.requestDate", type : "DATE", bind : "requestDate", size : 10, maxLen : 25}]},
					{items : [{name:"#L.status", type:"LABEL", value:"狀態"}]},
	                {items : [{name:"#F.status", type:"SELECT", bind:"status", size:25, maxLen:25,init:allstatusLoa,eChange:"doFormAccessControl()"}]}
	
					
					
					]},
				{row_style:"", cols:[
							{items:[{name:"#L.applicant", type:"LABEL", value:"申請人<font color='red'>*</font>"}]},
							{items:[{name:"#F.applicant", type:"TEXT", bind:"applicant", size:10, maxLen:25, eChange:"getEmployeeRole('applicant'),getApplicantName(),eChangedepApplicant()" },
					 		{name:"#B.applicant",	value:"選取" ,type:"PICKER" ,
													openMode:"open", src:"./images/start_node_16.gif",
			 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
			 									 	left:0, right:0, width:1024, height:768,	
			 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
							{name:"#F.applicantName"   , type:"TEXT", bind:"applicantName", size:5, maxLen:25,mode:"READONLY"}]},
							{items : [{name:"#L.COST", type:"LABEL", value:"成本(可視)"}]},
							{items : [{name:"#F.COST", type:"SELECT", bind:"costControl" ,size:25, maxLen:25 , init:ifCostControl}]},
							{items : [{name:"#L.WAREHOUSE", type:"LABEL", value:"庫別(可視)"}]},
							{items : [{name:"#F.WAREHOUSE", type:"SELECT", bind:"warehouseControl", size:25, maxLen:25, init:ifWareHouseControl}]},
							{items:[{name:"#L.requestCode", type:"LABEL", value:"需求人員<font color='red'>*</font>"}]},
							{items:[{name:"#F.requestCode", type:"TEXT", bind:"requestCode", size:10, maxLen:25, mask:"Aaaaaaaaaa"  ,eChange:"eChangeRequest()" },
					 		{name:"#B.requestCode",	value:"選取" ,type:"PICKER" ,
													openMode:"open", src:"./images/start_node_16.gif",
			 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
			 									 	left:0, right:0, width:1024, height:768,	
			 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
							{name:"#F.request"   , type:"TEXT", bind:"request", size:5, maxLen:25,mode:"READONLY"},
							{name:"#F.lastUpdatedBy"   , type:"hidden", bind:"lastUpdatedBy", size:5, maxLen:25,mode:"READONLY"},
							{name:"#F.role", type:"TEXT", bind:"role", size:25, maxLen:25 ,mode:"HIDDEN" }]}
					]}
			], 	
			beginService:"",
			closeService:""			
	});
}

function kweMaster(){
	var allproject    = [[true], ["店別"],["店別"]]; 
	var allsystem     = [[true], ["權限申請"],["權限申請"]];  
	var allCategroyTypese    = vat.bean("allCategroyTypes");
	var allsource     = [[true,true,true,true], ["口頭","EMAIL","LINE","電話"],["口頭","EMAIL","LINE","電話"]];
 	var status        = vat.item.getValueByName("#F.status")===null ? "SAVE" : vat.item.getValueByName("#F.status");
 	var orderTypeCode = vat.item.getValueByName("#orderTypeCode");
 	var loginDepartment      = vat.item.getValueByName("#loginDepartment");
 	var type      = vat.item.getValueByName("#type");
	var vsCostStyle=loginDepartment!="103"?" style= 'display:none;'":"";//loginDepartment!="103"?" style= 'display:none;'":"";//orderTypeCode!="IRQ"?" style= 'display:none;'":"";//status=="SAVE"?" style= 'display:none;'":""; // 因權限問題

	if(vat.bean().vatBeanOther.loginDepartment ==="103" ){ 	
		vat.block.create(vnB_master, {
			id: "vatBlock_Master", generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'", rows:[
				{row_style:"", cols:[	
					{items:[{name:"#L.categoryItem"  , type:"LABEL" , value:"項目/<font color='red'>*</font>"},
							{name:"#L.categorySystem", type:"LABEL" , value:"系統<font color='red'>*</font>"}]},
				
					{items:[{name:"#F.categoryItem", type:"SELECT", bind:"categoryItem",init:allproject ,eChange:function(){changeCategory();}},
							{name:"#F.categorySystem", type:"SELECT", bind:"categorySystem",init:allsystem,eChange:function(){changeInCategory();}}], td:" colSpan=4"}]},
				{row_style : "", cols:[	
					{items:[{name : "#L.description", type : "LABEL", value : "說明", row : 6, col : 50}], td:"rowSpan=4"},
					{items:[{name : "#F.description", type : "TEXTAREA", bind : "description",  row : 5, col : 50}], td: "rowSpan=4"}]},
				{row_style : "", cols:[	
					{items:[{name : "#L.depManager", type : "LABEL", value : "授權者<font color='red'>*</font>"}]},
					{items:[{name : "#F.depManager", type : "TEXT", bind : "depManager", size :10, maxLen : 25,  mask:"Aaaaaaaaaa",eChange:"eChangedepManager()"},
							{name : "#B.depManager",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee1();}},
			    			{name : "#F.depManagerName", type : "TEXT", bind : "depManagerName", size:10, maxLen : 25,mode:"READONLY"}]}]},
				{row_style: "", cols:[	
					{items:[{name : "#L.createdBy", type : "LABEL", value : "填單人員<font color='red'></font>"}]},
					{items:[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25,mode:"READONLY"},
									 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}]}]},
				{row_style: "", cols:[	
					{items:[{name : "#L.creationDate", type : "LABEL", value : "填單日期<font color='red'></font>"}]},
					{items:[{name:"#F.creationDate", type:"DATE", bind:"creationDate", size:10, maxLen:25 ,mode:"READONLY"}]}]},
		
				{row_style:"", cols:[
					{items:[{name:"#L.categoryGroup", type:"LABEL" , value:"分類/<font color='red'></font>"},
							{name:"#L.projectId", type:"LABEL" , value:"需求代號<font color='red'></font>"}]},
					{items:[{name:"#F.categoryGroup", type:"SELECT", bind:"categoryGroup",init:allCategroyTypese,style:vsCostStyle},//eChange:"changeItemSubcategory()" 
							{name:"#F.projectId", type:"NUMM",bind:"projectId",size:5,style:vsCostStyle},
							{name:"#L.special", type:"LABEL" , value:"特殊<font color='red'></font>"},
							{name:"#F.special"	, type:"CHECKBOX",biind:"special",style:vsCostStyle}]},
					{items:[{name:"#L.requestSource", type:"LABEL",  value:"需求來源"}]},	 
					{items:[{name:"#F.requestSource", type:"SELECT", bind:"requestSource",init:allsource}]}]},
				{row_style:"", cols:[ 
				 	{items:[{name:"#L.estimateStartDare"          , type:"LABEL" , value:"預計日期/"},
				 			{name:"#L.priority"          , type:"LABEL" , value:"順序/"},
				 			{name:"#L.totalHours"          , type:"LABEL" , value:"總時數"}]},
				 	{items:[{name:"#F.estimateStartDare"     , type:"DATE"  ,  bind:"estimateStartDare", size:13,style:vsCostStyle},
							{name:"#L.between"                  , type:"LABEL" , value:"至"},
				 		 	{name:"#F.estimateEndDare"       , type:"DATE"  ,  bind:"estimateEndDare"  , size:13},
				 		 	{name:"#F.priority"       , type:"TEXT"  ,  bind:"priority"  , size:5},
				 		 	{name:"#F.totalHours"       , type:"TEXT"  ,  bind:"totalHours"  , size:5}]},
				 		 
				 	{items:[{name:"#L.rqInChargeCode", type:"LABEL", value:"處理人員<font color='red'></font>"}]},
					{items:[{name:"#F.rqInChargeCode", type:"TEXT", bind:"rqInChargeCode"  ,size:10, maxLen:25,eChange:'eChangerqInChargeCode()'},
							{name : "#B.rqInChargeCode",	value:"選取" ,type:"PICKER" ,
															openMode:"open", src:"./images/start_node_16.gif",
					 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
					 									 	left:0, right:0, width:1024, height:768,	
					 									 	serviceAfterPick:function(){doAfterPickerEmployee2();}},
							{name:"#F.otherGroup", type:"TEXT", bind:"otherGroup", size:10 ,mode:"READONLY" ,maxLen:25 }],td:" colSpan=4"}]},		
				{row_style:"", cols:[
				 	{items:[{name:"#L.processDescription", type:"LABEL",  value:"處理說明"}]},	 
				 	{items:[{name:"#F.processDescription", type:"TEXT", bind:"processDescription", size:150,maxLen:400}],td:" colSpan=3"}]},
				{row_style:"", cols:[
				 	{items:[{name:"#L.hisstoryInfo", type:"LABEL",  value:"歷程"}]},	 
				 	{items:[{name:"#F.hisstoryInfo", type:"TEXT", bind:"hisstoryInfo", size:150,mode:"READONLY" }],td:" colSpan=3"}]}
			],			
			beginService:function(){return ""}	,
			closeService:function(){return ""}
		});
		
	}else{

	vat.block.create(vnB_master, {
	id: "vatBlock_Master", generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'", rows:[
	{row_style:"", cols:[	
		{items:[{name:"#L.categoryItem", type:"LABEL" , value:"項目/<font color='red'>*</font>"},
				{name:"#L.categorySystem", type:"LABEL" , value:"系統<font color='red'>*</font>"}]},
		{items:[{name:"#F.categoryItem", type:"SELECT", bind:"categoryItem",init:allproject ,eChange:function(){changeCategory();}},
				{name:"#F.categorySystem", type:"SELECT", bind:"categorySystem",init:allsystem,eChange:function(){changeInCategory();}}], td:" colSpan=4"}]},
	{row_style : "", cols:[	
		{items:[{name : "#L.description", type : "LABEL", value : "說明", row : 6, col : 50}], td:"rowSpan=4"},
		{items:[{name : "#F.description", type : "TEXTAREA", bind : "description", size : 50, row : 5, col : 50}], td: "rowSpan=4"}]},
	{row_style : "", cols:[	
		{items:[{name : "#L.depManager", type : "LABEL", value : "授權者<font color='red'>*</font>"}]},
		{items:[{name : "#F.depManager", type : "TEXT", bind : "depManager", size :10, mask:"Aaaaaaaaaa",maxLen : 25,eChange:'eChangedepManager()'},
				{name : "#B.depManager",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee1();}},
			    {name : "#F.depManagerName", type : "TEXT", bind : "depManagerName", size :10, maxLen : 25,mode:"READONLY"}]}]},
	{row_style : "", cols:[	
		{items :[{name : "#L.createdBy", type : "LABEL", value : "填單人員<font color='red'></font>"}]},
		{items :[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25,mode:"READONLY"},
				 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}]}]},
	{row_style : "", cols:[	
		{items :[{name : "#L.creationDate", type : "LABEL", value : "填單日期<font color='red'></font>"}]},
		{items :[{name:"#F.creationDate", type:"DATE", bind:"creationDate", size:10, maxLen:25 ,mode:"READONLY"}]}]},
	{row_style:vsCostStyle, cols:[
		{items:[{name:"#L.categoryGroup", type:"LABEL" , value:"分類<font color='red'></font>"}]},
		{items:[{name:"#F.categoryGroup", type:"SELECT", bind:"categoryGroup",init:allCategroyTypese,style:vsCostStyle}//eChange:"changeItemSubcategory()" 
							]},
		{items:[{name:"#L.requestSource", type:"LABEL",  value:"需求來源"}]},	 
	 	{items:[{name:"#F.requestSource", type:"SELECT", bind:"requestSource",init:allsource}]}]},
	{row_style:vsCostStyle, cols:[ 
	 	{items:[{name:"#L.estimateStartDare"          , type:"LABEL" , value:"預計日期"}]},
	 	{items:[{name:"#F.estimateStartDare"     , type:"DATE"  ,  bind:"estimateStartDare", size:13,style:vsCostStyle},
				{name:"#L.between"                  , type:"LABEL" , value:"至"},
	 		 	{name:"#F.estimateEndDare"       , type:"DATE"  ,  bind:"estimateEndDare"  , size:13}]},	 
	 	{items:[{name:"#L.rqInChargeCode", type:"LABEL", value:"處理人員<font color='red'></font>"}]},
		{items:[{name:"#F.rqInChargeCode", type:"TEXT", bind:"rqInChargeCode",  mask:"Aaaaaaaaaa"  ,size:10, maxLen:25,eChange:"eChangerqInChargeCode()"},
				{name : "#B.rqInChargeCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee2();}},
				{name:"#F.otherGroup", type:"TEXT", bind:"otherGroup", size:10 ,mode:"READONLY" ,maxLen:25 }],td:" colSpan=4"}]},		
	{row_style:vsCostStyle, cols:[
	 	{items:[{name:"#L.processDescription", type:"LABEL",  value:"處理說明"}]},	 
	 	{items:[{name:"#F.processDescription", type:"TEXT", bind:"processDescription", size:150,maxLen:400}],td:" colSpan=3"}]},
	{row_style:vsCostStyle, cols:[
	 	{items:[{name:"#L.hisstoryInfo", type:"LABEL",  value:"歷程"}]},	 
	 	{items:[{name:"#F.hisstoryInfo", type:"TEXT", bind:"hisstoryInfo", size:150,mode:"READONLY" }],td:" colSpan=3"}]}
			],	
			beginService:function(){return ""}	,
			closeService:function(){return ""}
	});
	}
}

function kweShopDetail(){
  
	//var allfix    = vat.bean("allfix");
	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
    var vbCanGridModify = true;
    
    vat.item.make(vnB_ShopSet, "indexNo"                   , {type:"IDX"  , size: '1%',                     desc:"序號"       });
    vat.item.make(vnB_ShopSet, "enable"        , {type:"checkbox" , size: '1%',  desc:"啟用"});
    vat.item.make(vnB_ShopSet, "counterCode"            , {type:"TEXT" , size: '30%',  desc:"專櫃代碼" ,mode:"READONLY"});
	vat.item.make(vnB_ShopSet, "counterName"            , {type:"TEXT" , size: '30%',  desc:"專櫃名稱" ,mode:"READONLY"});
	//vat.item.make(vnB_ShopSet, "warehouseId"            , {type:"TEXT" ,  desc:"庫別Id" });
	vat.item.make(vnB_ShopSet, "brandCode"            , {type:"TEXT" ,  desc:"品牌" ,mode:"READONLY"});
	vat.item.make(vnB_ShopSet, "lineId"                   , {type:"HIDDEN"  ,                     desc:"lineId"       });
	
	

	vat.block.pageLayout(vnB_ShopSet, {
														id                  : "vatShopDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                        loadBeforeAjxService: "loadBeforeAjxService()",
								                        eventService        : "selectAllCounters()",
														//loadSuccessAfter    : "loadSuccessAfterAdDetail()",	
														saveBeforeAjxService: "saveBeforeAjxShopService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "25"
														});	
														

	vat.block.pageDataLoad(vnB_ShopSet, vnCurrentPage = 1);															
}

function checkIfCost(elem){
  alert(elem.id);
}

/**
// 自訂事件
function selectAllWarehouses() {
    
    if (confirm("是否確定執行『全選』？")) {
        if (vat.item.getValueByName("#F.status") == "SAVE") {
            vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
            vat.block.submit(function () {
               return "process_object_name=buPurchaseServiceSub" + "&process_object_method_name=updateAllHouses";
            }, {
                other: true,
                picker: false,
                funcSuccess: function () {
                    vat.block.pageRefresh(vnB_ShopSet);
                }
            });
        } else {
            alert("狀態必須在暫存，方可使用本功能");
        }
    }
}
**/

// 送出,暫存按鈕
function doSubmit(formAction){
	var formId               = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
	var processId            = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
	var status               = vat.item.getValueByName("#F.status");
	var orderTypeCode        = vat.item.getValueByName("#F.orderTypeCOode");
	var approvalComment      = vat.item.getValueByName("#F.approvalComment");
	var approvalResult       = vat.item.getValueByName("#F.approvalResult");
	var alertMessage ="是否確定送出?";
	
	if(approvalResult == true){
		approvalResult = "true"
	}else{
		approvalResult = "false"
	}
	var formStatus = status;
	if("SAVE" == formAction){
		formStatus = "SAVE";
	}else if("SUBMIT" == formAction){
		formStatus = changeFormStatusPRC(formId, processId, status, approvalResult);
	}else if("SIGNING" == formAction){
		formStatus = "SIGNING";
	}else if("VOID" == formAction){
		formStatus = "VOID";
	}else if("SIGNING" == formAction){
		formStatus = "SIGNING";
	}	
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if ("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if ("SAVE" == formAction){ 
		alertMessage = "是否確定暫存?";
	}else if ("SIGNING" == formAction){
		alertMessage = "是否確定送簽?";
	}
	
	if(confirm(alertMessage)){
		vat.block.pageDataSave(vnB_ShopSet,{  	
			funcSuccess:function(){
				vat.bean().vatBeanOther.formStatus = formStatus;	
				vat.bean().vatBeanOther.formAction = formAction;
				//alert(formAction);
				vat.block.submit(function(){
					return "process_object_name=shopSetAction&process_object_method_name=performTransaction";
				},{
					bind:true, link:true, other:true,
					funcSuccess: function () {                            
					} 
				});	
			}
		});			    
	}
}

	/* 指定下一個狀態 */
function changeFormStatusPRC(formId, processId, status, approvalResult){
    var nextStatus = "";
    if(formId == null || formId == ""){
       	nextStatus = "SIGNING";
    }else if(processId != null && processId != ""){
        if(status == "SAVE" || status == "REJECT"){
            nextStatus = "SIGNING";
        }else if(status == "SIGNING"){
            nextStatus = "PURCHASE";
        }else if(status == "PURCHASE"){
            nextStatus = "FINISH";
        }else if(approvalResult == "true"){
            nextStatus = status;
        }else if(approvalResult == "false"){
            nextStatus = "REJECT";
        }
    }
    return nextStatus;
}

// 刷新頁面
function refreshForm(code){
   
	document.forms[0]["#formId"].value = code; 
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=shopSetAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     	  
     			vat.item.bindAll(); 
     			vat.block.pageRefresh(vnB_ShopSet);
     			doFormAccessControl();
				
     	}}
    );
    	
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

// 建立新資料按鈕	
function loadSuccessAfterAdDetail(){
    var menuName;
    for(var i=1;i<=10;i++){
    //alert(document.getElementById("vatF#B8A#Y"+i+"#X6").value.length);
     if(document.getElementById("vatF#B8A#Y"+i+"#X5").value.length==0){
       /*
       var menuId = document.getElementById("menuId#"+i);
       var lineId = document.getElementById("lineId#"+i);
       var indexNo = document.getElementById("indexNo#"+i);
       var enable = document.getElementById("enable#"+i);
       
       var url = document.getElementById("url#"+i);
       var brandCode = document.getElementById("brandCode#"+i);
       var cost = document.getElementById("cost#"+i);
       var categoryCode = document.getElementById("categoryCode#"+i);
       var wareHouseCode = document.getElementById("wareHouseCode#"+i);
       menuId.style.backgroundColor = "lightblue";
       lineId.style.backgroundColor = "lightblue";
       indexNo.style.backgroundColor = "lightblue";
       enable.style.backgroundColor = "lightblue";
       url.style.backgroundColor = "lightblue";
       brandCode.style.backgroundColor = "lightblue";
       cost.style.backgroundColor = "lightblue";
       categoryCode.style.backgroundColor = "lightblue";
       wareHouseCode.style.backgroundColor = "lightblue";
       */
     //alert(document.getElementById("vatF#B8A#Y"+i+"#X6").value);
      // if(document.getElementById("vatF#B8A#Y"+i+"#X6").value==""||document.getElementById("vatF#B8A#Y"+i+"#X6").value==null){
         document.getElementById("vatF#B8A#Y"+i+"#X4").style.color="#FF0000";
         //vat.item.setAttributeByName("vatF#B8A#Y"+i+"#X2", "readOnly", true);
         
       //}
     }else{
        document.getElementById("vatF#B8A#Y"+i+"#X4").style.color="#000000";
        //vat.item.setAttributeByName("vatF#B8A#Y"+i+"#X2", "readOnly", false); 
        
     }
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


function saveBeforeAjxShopService() {
	               //alert("eeeeeeee");
    processString = "process_object_name=shopSetService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
		"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
    
	return processString;

}
/*
function kweloadBeforeAjxService(){
	var processString = "process_object_name=siMenuService&process_object_method_name=getAJAXPageData" +
	                    "&brandCode=" + vat.bean().vatBeanOther.brandCode +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
	                    "&applicant=" + vat.item.getValueByName("#F.applicant");
	return processString;		
}
*/
/*
function loadBeforeAjxService(div){
	alert("After loadBeforeAjxService:"+div);
	
	var processString = "";
	
	if (div == vnB_vat7){
	    processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXAttachment" + 
			            "&parentHeadId=" + vat.item.getValueByName("#F.headId")  + 
		                "&parentOrderType=" + vat.item.getValueByName("#F.orderTypeCode") +
		                "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value + 
		                "&ownerType=creator";
	}else if(div == 8){
	    alert("fffffffff");
	    processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXPageDataAddetail" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	            		"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode");
	}else{
	   
		processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	            		"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode");
    }
	                
	return processString;
}
*/

function loadBeforeAjxService(){
	var processString = "";
	var processString = "process_object_name=shopSetService&process_object_method_name=getAJAXPageDataShop" +
	                   
	                     "&shopCode=" + vat.bean().vatBeanOther.warehouseCode +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&type=" + vat.item.getValueByName("#F.type") +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode ;
	return processString;
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


/* 報表列印 */
function openReportWindow(type){ 
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
		vat.block.submit(
					function(){return "process_object_name=buPurchaseService"+
								"&process_object_method_name=getReportConfig";},{other:true,
                    			funcSuccess:function(){
								eval(vat.bean().vatBeanOther.reportUrl);
					}}
		);   
   if("AFTER_SUBMIT"==type) refreshForm();//因為調撥單在送出後要直接列印報表，所以要有這行
}


function reconfirmImmovement(orderTypeCode,orderNo){
		if(confirm('是否要列印請採驗')){	
			url = "http://10.1.98.161:8080/crystal/t2/pu1209.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
	
	function reconfirmImmovement1(orderTypeCode,orderNo){
		if(confirm('是否要列報修單')){	
			url1 = "http://10.1.98.161:8080/crystal/t2/IRD.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url1,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url1;
	}
	
	function reconfirmImmovement2(orderTypeCode,orderNo){
		if(confirm('是否要列印需求單')){	
			url2 = "http://10.1.98.161:8080/crystal/t2/IRQ.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url2,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url2;
	}



function getEmployeeRole(vsEmployee) {

    if ("" != vsEmployee, vat.item.getValueByName("#F." + vsEmployee)) {
        vat.item.setValueByName("#F." + vsEmployee, vat.item.getValueByName("#F." + vsEmployee).toUpperCase());
        var processString = "process_sql_code=FindEmployeeRole&employeeCode=" + vat.item.getValueByName("#F." + vsEmployee);
        vat.ajax.startRequest(processString, function () {
            if (vat.ajax.handleState()) {
            
                if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
                    vat.item.setValueByName("#F.role", vat.ajax.getValue("EMPLOYEE_ROLE", vat.ajax.xmlHttp.responseText));
                    vat.item.setValueByName("#F.COST", vat.ajax.getValue("COST_CONTROL", vat.ajax.xmlHttp.responseText));
                    vat.item.setValueByName("#F.WAREHOUSE", vat.ajax.getValue("WAREHOUSE_CONTROL", vat.ajax.xmlHttp.responseText));
                    
                } else {
                    vat.item.setValueByName("#F." + vsEmployee + "Name", "");
                    alert("查無此員工代號");
                }
            }
        });
    }
}

function getOriginalShopData(roleCode){

//alert("frfr:"+roleCode);
var getEmpCode = "";
var getBoseeCode = "";
  
  if(vat.item.getValueByName("#F.applicant")!=""){
      getEmpCode = vat.item.getValueByName("#F.applicant");
    if(roleCode!=""){
       if(vat.item.getValueByName("#F.depManager")==""){
          alert("請先輸入部門主管工號");
       }else{
          getBoseeCode = vat.item.getValueByName("#F.depManager");
       }
    }
  }else{
    alert("請先輸入申請人工號");
  }
	//alert(getBoseeCode);
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=shopSetService"+
                    "&process_object_method_name=executeCopyOriShop"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + getEmpCode +
                    "&bossCode=" + getBoseeCode +
                    "&headId=" + vat.item.getValueByName("#F.headId"),
            
        find: function getOriginalDeliverySuccess(oXHR){
        	
            doPageRefreshShop();
       }  
   }); 
}

function getApplicantName() {
	var processString = "process_object_name=shopSetService&process_object_method_name=getAJAXFormDataByRequest" +
						"&requestCode="  + vat.item.getValueByName("#F.applicant");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.applicantName", vat.ajax.getValue("request", vat.ajax.xmlHttp.responseText))
		}
	});
}

function eChangedepManager() {
   // alert(vat.item.getValueByName("#loginDepartment"));
	var processString = "process_object_name=shopSetService&process_object_method_name=getAJAXFormDataByDepManager" +
						"&depManager="  + vat.item.getValueByName("#F.depManager");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.depManagerName", vat.ajax.getValue("depManagerName", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.depManager", vat.ajax.getValue("depManager", vat.ajax.xmlHttp.responseText))

		    
		}
	});
}

function eChangedepApplicant() {
	var processString = "process_object_name=shopSetService&process_object_method_name=getAJAXFormDataByDepManager" +
						"&depManager="  + vat.item.getValueByName("#F.applicant");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
		
			if(document.forms[0]["#loginDepartment"   ].value=='103'){
			   
			   vat.item.setValueByName("#F.applicantName", vat.ajax.getValue("depManagerName", vat.ajax.xmlHttp.responseText))
			   vat.item.setValueByName("#F.applicant", vat.ajax.getValue("depManager", vat.ajax.xmlHttp.responseText))
			   vat.item.setValueByName("#F.no", "異動 "+vat.item.getValueByName("#F.applicant")+" KWE權限");
			}else{
			   vat.item.setValueByName("#F.applicantName", vat.ajax.getValue("depManagerName", vat.ajax.xmlHttp.responseText))
			   vat.item.setValueByName("#F.applicant", vat.ajax.getValue("depManager", vat.ajax.xmlHttp.responseText))
			   vat.item.setValueByName("#F.no", "異動  "+vat.item.getValueByName("#F.applicant")+" KWE權限");
		    }
		    
		}
	});
}

function doPassHeadData(){

	var suffix = "";
	var vOrderTypeCode         = vat.item.getValueByName("#F.OrderTypeCode").replace(/^\s+|\s+$/, '').toUpperCase();
  
  
	suffix += "&orderTypeCode="+escape(vOrderTypeCode);

	return suffix;
}

function doPageRefreshShop(){

    vat.block.pageRefresh(vnB_ShopSet);
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
    //alert("do after");
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

function saveSuccessAfter() {
  
}

function kwePageLoadSuccess(){
}