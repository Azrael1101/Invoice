 /*** 
 *	檔案: buCustomer.js
 *	說明: 類別代號,抽成率維護
 */
 

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;


function outlineBlock(){ 
 	formInitial();
  	buttonLine();
  	headerInitial();
  	
  	 if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","主檔資料"   ,"vatBlock_Master"  		,"images/tab_master_data_dark.gif"    		,"images/tab_master_data_light.gif", false);
		vat.tabm.createButton(0 ,"xTab2","客戶需求資料"   ,"vatBlock_Master2"  		,"images/tab_customer_attachment_dark.png"    		,"images/tab_customer_attachment_light.png", false);  
	
	}
	kweMaster();
	kweMaster2();	//vat.tabm.displayToggle(0, "xTab2", true);
	
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
          customerCode	      	: document.forms[0]["#customerCode"    	 ].value,      	  
          beforeStatus			: "beforestatus",
	      nextStatus			: "nextstatus",
	 //    approvalResult		: "approvalResult",
	 //    approvalComment		: "approvalComment",       
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        }; 
          vat.bean.init(  
             function(){
                    return "process_object_name=buCustomerModAction&process_object_method_name=performInitial"; 
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
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createRefreshForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			/*{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Bu_Supplier_Mod:search:20131121.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},*/
	 			{name:"#B.unlock", type:"IMG"    ,value:"取消凍結",   src:"./images/button_unlock_data.gif", eClick:'unlock()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"}/*,
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }*/],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){ 
var allCustomerTypeCode = [["", false, false], ["1-自然人","2-法人 "], ["1", "2"]];//類型
var allVipType = vat.bean("allVipType"); //會員類別
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"客戶資料維護作業", 
		rows:[{row_style:"", cols:[
				{items:[{name:"#L.customerCode", type:"LABEL", value:"會員編號<font color='red'>*</font>"}]},
				{items:[{name:"#F.customerCode", type:"TEXT", bind:"customerCode",size:25,eChange:"onChangeCustomerCode('customerCode')"},
						{name:"#L.enable", type:"LABEL", value:"啟用?"},
						{name:"#F.enable", type:"XBOX", bind:"enable" }]},
				{items:[{name:"#L.identityCode", type:"LABEL", value:"身份證明代碼<font color='red'>*</font>"}]},
				//MACO 自然人or法人(帶入統編)
				{items:[{name:"#F.identityCode", type:"TEXT", bind:"identityCode",size:25,eChange:"onChangeCustomerCode('identityCode')"},
						{name:"#F.addressBookId", type:"TEXT", bind:"addressBookId",mode:"hidden" },
						{name:"#F.headId", type:"TEXT", bind:"headId",mode:"hidden" },
						{name:"#F.customerTypeCode", type:"SELECT",   bind:"customerTypeCode",init:allCustomerTypeCode,eChange:"onChangeCustomerTypeCode()"}],td:" colSpan=3"},
	  			{items:[{name:"#L.vipTypeCode", type:"LABEL" , value:"會員類別<font color='red'>*</font>"}]},
	 		    {items:[{name:"#F.vipTypeCode", type:"SELECT",   bind:"vipTypeCode",init:allVipType}]},
	 		    {items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},
	 		    {items:[{name:"#F.brandCode", type:"TEXT",   bind:"brandCode",mode:"ReadOnly"}]},
	 		    {items:[{name:"#L.loginEmployeeCode", type:"LABEL" , value:"填單人員"}]},
	 		    {items:[{name:"#F.loginEmployeeCode", type:"TEXT",   bind:"loginEmployeeCode",mode:"ReadOnly"}]}
	 		    
	 		    ]},
	  					
				{row_style:"", cols:[
					{items:[{name:"#L.chineseName", type:"LABEL", value:"會員姓名<font color='red'>*</font>"}]},
					{items:[{name:"#F.chineseName", type:"TEXT", bind:"chineseName" ,size:40}]},
					{items:[{name:"#L.englishName", type:"LABEL", value:"英文名稱"}]},
					{items:[{name:"#F.englishName", type:"TEXT", bind:"englishName",size:40 }],td:" colSpan=3"},
					{items:[{name:"#L.vipStartDate", type:"LABEL", value:"起始日<font color='red'>*</font>"}]},
					{items:[{name:"#F.vipStartDate", type:"DATE", bind:"vipStartDate" }]},
					{items:[{name:"#L.vipEndDate", type:"LABEL", value:"到期日<font color='red'>*</font>"}]},
					{items:[{name:"#F.vipEndDate", type:"DATE", bind:"vipEndDate" }]},
					{items:[{name:"#L.applicationDate", type:"LABEL", value:"申請日"}]},
					{items:[{name:"#F.applicationDate", type:"DATE", bind:"applicationDate" }]}/*,
		 		    {items:[{name:"#L.todayDate", type:"LABEL", value:"填表日期<font color='red'>*</font>"}]},
					{items:[{name:"#F.todayDate", type:"DATE", bind:"creationDate" ,mode:"ReadOnly"}]}*/
				]}			 
			], 	
		 
		beginService:"",
		closeService:""			
	});
}

function kweMaster(){
var allGender = [["", false, true], ["男","女"], ["M", "F"]];
var allYearIncomeType = vat.bean("allYearIncomeType");//年收入
var allJobType = vat.bean("allJobType");//行業別
var allEducsteType = vat.bean("allEducsteType");//學歷
var allInvoiceType = vat.bean("allInvoiceType");  //發票形式
var allCategroyTypes = vat.bean("allCategroyTypes");  
var allInvoiceDeliveryType = vat.bean("allInvoiceDeliveryType");//發票交付方式  
var allSourceCurrency = vat.bean("allSourceCurrency");  //幣別
var allpaymentTermCode = vat.bean("allpaymentTermCode");  //付款條件
var allTradeTeam = vat.bean("allTradeTeam"); //
var allTaxType = vat.bean("allTaxType");//稅別
var allCountry = vat.bean("allCountry");//國別
var allShop = vat.bean("allShop"); //店別
var allCity = vat.bean("allCity");
var allArea = vat.bean("allArea");
var allZipCode = vat.bean("allZipCode");
var allMonth = [["", "", true], ["1","2","3","4","5","6","7","8","9","10","11","12"], ["1","2","3","4","5","6","7","8","9","10","11","12"]];
var allDay = [["", "", true], ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"], ["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"]];

vat.block.create(vnB_Master, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
			rows:[{row_style:"", cols:[
	  	 		 {items:[{name:"#L.category07", type:"LABEL", value:"申請櫃別<font color='red'>*</font>"}]},
				 {items:[{name:"#F.category07", type:"SELECT", bind:"category07",init:allShop}]},
				 {items:[{name:"#L.shortName", type:"LABEL", value:"簡稱"}]},
				 {items:[{name:"#F.shortName", type:"TEXT", bind:"shortName"}]},

				 {items:[{name:"#L.countryCode", type:"LABEL", value:"國別"}]},
				 {items:[{name:"#F.countryCode", type:"SELECT", bind:"countryCode",init:allCountry}]},
				 {items:[{name:"#L.birthday", type:"LABEL", value:"生日<font color='blue'>#</font>"}]},
				 {items:[{name:"#F.birthdayYear", type:"TEXT", bind:"birthdayYear"},
						 {name:"#L.birthdayYear", type:"LABEL", value:"年"},
				 		 {name:"#F.birthdayMonth", type:"SELECT", bind:"birthdayMonthString",init:allMonth},
				 		 {name:"#F.birthdayMonth", type:"LABEL", value:"月"},
				 		 {name:"#F.birthdayDay", type:"SELECT", bind:"birthdayDayString",init:allDay},
				 		 {name:"#F.birthdayDay", type:"LABEL", value:"日"}],td:" colSpan=3"}
			 ]},
			// {row_style:"", cols:[
			//	 {items:[{name:"#L.category01", type:"LABEL", value:"行業別"}]},
			//	 {items:[{name:"#F.category01", type:"SELECT", bind:"category01",init:allEducsteType}]},
			//	 {items:[{name:"#L.category02", type:"LABEL", value:"教育程度"}]},
			//	 {items:[{name:"#F.category02", type:"SELECT", bind:"category02",init:allJobType }]},
			//	 {items:[{name:"#L.category03", type:"LABEL", value:"收入"}]},
			//	 {items:[{name:"#F.category03", type:"SELECT", bind:"category03",init:allYearIncomeType}]},
			//	 {items:[{name:"#L.paymentTermCode", type:"LABEL", value:"付款條件"}]},
			//	 {items:[{name:"#F.paymentTermCode", type:"SELECT", bind:"paymentTermCode",init:allpaymentTermCode}],td:" colSpan=3	"}
			// ]},
			 {row_style:"", cols:[
			 	 {items:[{name:"#L.gender", type:"LABEL", value:"性別"}]},
				 {items:[{name:"#F.gender", type:"SELECT", bind:"gender",init:allGender }]},
			     {items:[{name:"#L.tel1", type:"LABEL", value:"電話(日)"}]},
				 {items:[{name:"#F.tel1", type:"TEXT", bind:"tel1",size:25,maxLen:"40"}]},
				 {items:[{name:"#L.tel2", type:"LABEL", value:"電話(夜)"}]},
				 {items:[{name:"#F.tel2", type:"TEXT", bind:"tel2",size:25,maxLen:"40"}]},
				 {items:[{name:"#L.mobilePhone", type:"LABEL", value:"行動電話<font color='blue'>#</font>"}]},
				 {items:[{name:"#F.mobilePhone", type:"TEXT", bind:"mobilePhone",size:25,maxLen:"40" }]},
				 {items:[{name:"#L.EMail", type:"LABEL", value:"電子信箱"}]},
				 {items:[{name:"#F.EMail", type:"TEXT", bind:"EMail",size:40,maxLen:"40"}]}

			 ]},	
 
			 {row_style:"", cols:[
			//	 {items:[{name:"#L.invoiceTypeCode", type:"LABEL", value:"發票類型"}]},
			//	 {items:[{name:"#F.invoiceTypeCode", type:"SELECT", bind:"invoiceTypeCode",init:allInvoiceType }]},
			//	 {items:[{name:"#L.invoiceDeliveryCode", type:"LABEL", value:"發票交付方式"}]},
			//	 {items:[{name:"#F.invoiceDeliveryCode", type:"SELECT", bind:"invoiceDeliveryCode",init:allInvoiceDeliveryType}]},
				 {items:[{name:"#L.cityA", type:"LABEL", value:"城市(選取)"}]},
				 {items:[{name:"#F.cityA", type:"SELECT",init:allCity, eChange:"onChangeCity()"}]},
				 {items:[{name:"#L.areaA", type:"LABEL", value:"地區(選取)"}]},
				 {items:[{name:"#F.areaA", type:"SELECT",init:allArea, eChange:"onChangeArea()"}]},
				 {items:[{name:"#L.address", type:"LABEL", value:"地址<font color='blue'>#</font>"}]},
				 {items:[{name:"#L.city", type:"LABEL", value:"城市"},
				 		 {name:"#F.city", type:"TEXT", bind:"city"},
				 		 {name:"#L.area", type:"LABEL", value:"地區"},
				 		 {name:"#F.area", type:"TEXT", bind:"area"},
				 		 {name:"#L.zipCode", type:"LABEL", value:"郵遞區號"},
						 {name:"#F.zipCode", type:"TEXT", bind:"zipCode"},
				 		 {name:"#F.address", type:"TEXT", bind:"address",size:70,maxLen:"150"}],td:" colSpan=5"}
			 ]},
			/* {row_style:"", cols:[
				 {items:[{name:"#L.stockCredits", type:"LABEL", value:"期初信用額度"}]},
				 {items:[{name:"#F.stockCredits", type:"TEXT", bind:"stockCredits"}]},
				 {items:[{name:"#L.adjCredits", type:"LABEL", value:"加減信用額度"}]},
				 {items:[{name:"#F.adjCredits", type:"TEXT", bind:"adjCredits"}]},
				 {items:[{name:"#L.totalUncommitCredits", type:"LABEL", value:"總異動額度"}]},
				 {items:[{name:"#F.totalUncommitCredits", type:"TEXT", bind:"totalUncommitCredits"}],td:" colSpan=5"}
			 ]},*/
			 {row_style:"", cols:[
				 {items:[{name:"#L.deliveryAddress", type:"LABEL", value:"宅配地址"}]},
				 {items:[{name:"#F.deliveryAddress", type:"TEXT", bind:"deliveryAddress",size:150,maxLen:"150"}],td:" colSpan=9"}
			 ]},
			 {row_style:"", cols:[
				 {items:[{name:"#L.remark1", type:"LABEL", value:"備註一"}]},
				 {items:[{name:"#F.remark1", type:"TEXT", bind:"remark1",size:150,maxLen:"150"}],td:" colSpan=9"}
			 ]},
			  		 
			 {row_style:"", cols:[
				 {items:[{name:"#L.remark2", type:"LABEL", value:"備註二"}]},
				 {items:[{name:"#F.remark2", type:"TEXT", bind:"remark2",size:150,maxLen:"150"}],td:" colSpan=9"}			 
			]},
			{row_style:"", cols:[
				 {items:[{name:"#L.remark3", type:"LABEL", value:"異動說明<font color='red'>*</font>"}]},
				 {items:[{name:"#F.remark3", type:"TEXT", bind:"remark3",size:150,maxLen:"150"}],td:" colSpan=9"}			 
			]},	
			{row_style:"", cols:[
				 {items:[{name:"#L.remark3", type:"LABEL", value:"&nbsp&nbsp<font color='red'>*</font><font color='green'>&nbsp必填欄位</font>&nbsp&nbsp&nbsp&nbsp&nbsp<font color='blue'>#</font><font color='green'>&nbsp為非T2品牌必填欄位</font>"}],td:" colSpan=10"}			 
			]}		
		
 	 	],
		beginService:"",
		closeService:""
	});
	
}



// 建立新資料按鈕	
/*
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
    	refreshForm("");
	 }
}*/

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
    	window.close();
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










// 送出,暫存按鈕
function doSubmit(formAction){
vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
//alert(vat.item.getValueByName("#F.headId"));

			var alertMessage ="是否確定送出?";
			if(confirm(alertMessage)){
				/*if(vat.item.getValueByName("#F.remark3")!=="")
				{*/
				
					vat.block.submit
					(function(){
							return "process_object_name=buCustomerModAction" + "&process_object_method_name=performTransaction";
						},
						{bind:true, link:true, other:true}
					);
				/*}
				else
				{
					alert("請於異動說明填入本次異動原因");
				}*/
			}
}

function onChangeCity(){

    vat.ajax.XHRequest(
    {
        post:"process_object_name=buCustomerModService"+
                 "&process_object_method_name=getAJAXCityCategory"+
                 "&city1="  + vat.item.getValueByName("#F.cityA"),
        find: function changeRequestSuccess(oXHR){ 
					var allArea = eval(vat.ajax.getValue("allArea", oXHR.responseText));
        			allArea[0][0] = "#F.areaA";
					vat.item.SelectBind(allArea); 
					vat.item.setValueByName("#F.city", vat.ajax.getValue("city", oXHR.responseText));
        }   
    });
}

function onChangeArea(){


	var processString = "process_object_name=buCustomerModService&process_object_method_name=getAJAXAreaCategory"
						+"&city1="  + vat.item.getValueByName("#F.cityA")
						+"&area1="  + vat.item.getValueByName("#F.areaA")

	vat.ajax.startRequest(processString,  function()
	{ 
		if (vat.ajax.handleState())
		{
					vat.item.setValueByName("#F.area", vat.ajax.getValue("area", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.zipCode", vat.ajax.getValue("zipCode", vat.ajax.xmlHttp.responseText));
		

  		}
	});

}

//
function onChangeCustomerCode(changeData){

	if((vat.item.getValueByName("#F.customerCode")!=="")){
		var processString = "process_object_name=buCustomerModService&process_object_method_name=getAJAXFormDataByCustomer"
							+"&brandCode="  + vat.item.getValueByName("#F.brandCode")
							+"&customerCode="  + vat.item.getValueByName("#F.customerCode")
							+"&identityCode="  + vat.item.getValueByName("#F.identityCode")
							+"&changeData="  + changeData;
		vat.ajax.startRequest(processString,  function()
		{ 
			if (vat.ajax.handleState())
			{
				if(vat.ajax.getValue("orderType", vat.ajax.xmlHttp.responseText)==="old")
				{
					var alertMessage;
					if(changeData==="customerCode"){
						alertMessage ="客戶代碼已存在，是否讀取顧客資訊?";
					}
					else{
						alertMessage ="通訊錄資料已存在，是否讀取顧客資訊?";
					}
					if(confirm(alertMessage)){
						if("customerCode"===changeData){
							vat.item.setValueByName("#F.customerTypeCode", vat.ajax.getValue("customerTypeCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.brandCode", vat.ajax.getValue("brandCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.customerCode", vat.ajax.getValue("customerCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.vipStartDate", vat.ajax.getValue("vipStartDate", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.vipEndDate", vat.ajax.getValue("vipEndDate", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.vipType", vat.ajax.getValue("vipTypeCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.vipTypeCode", vat.ajax.getValue("vipTypeCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.applicationDate", vat.ajax.getValue("applicationDate", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.taxRate", vat.ajax.getValue("taxRate", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.taxType", vat.ajax.getValue("taxType", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.currencyCode", vat.ajax.getValue("currencyCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.category07", vat.ajax.getValue("category07", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.category01", vat.ajax.getValue("category01", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.category02", vat.ajax.getValue("category02", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.category03", vat.ajax.getValue("category03", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("paymentTermCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.remark1", vat.ajax.getValue("remark1", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.remark2", vat.ajax.getValue("remark2", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.remark3", vat.ajax.getValue("remark3", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.deliveryAddress", vat.ajax.getValue("deliveryAddress", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.stockCredits", vat.ajax.getValue("stockCredits", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.adjCredits", vat.ajax.getValue("adjCredits", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.totalUncommitCredits", vat.ajax.getValue("totalUncommitCredits", vat.ajax.xmlHttp.responseText));
							
							
							//法人使用欄位 Maco 2017.02.15 Maco
							vat.item.setValueByName("#F.taxType", vat.ajax.getValue("taxType", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.taxRate", vat.ajax.getValue("taxRate", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.currencyCode", vat.ajax.getValue("currencyCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.invoiceTypeCode", vat.ajax.getValue("invoiceTypeCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.paymentTermCode", vat.ajax.getValue("paymentTermCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.city1", vat.ajax.getValue("city1", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.area1", vat.ajax.getValue("area1", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.zipCode1", vat.ajax.getValue("zipCode1", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.address1", vat.ajax.getValue("address1", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.city2", vat.ajax.getValue("city2", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.area2", vat.ajax.getValue("area2", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.zipCode2", vat.ajax.getValue("zipCode2", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.address2", vat.ajax.getValue("address2", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.city3", vat.ajax.getValue("city3", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.area3", vat.ajax.getValue("area3", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.zipCode3", vat.ajax.getValue("zipCode3", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.address3", vat.ajax.getValue("address3", vat.ajax.xmlHttp.responseText));
						}
							vat.item.setValueByName("#F.addressBookId", vat.ajax.getValue("addressBookId", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.identityCode", vat.ajax.getValue("identityCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.chineseName", vat.ajax.getValue("chineseName", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.englishName", vat.ajax.getValue("englishName", vat.ajax.xmlHttp.responseText));
							//vat.item.setValueByName("#F.type", vat.ajax.getValue("type", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.shortName", vat.ajax.getValue("shortName", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.gender", vat.ajax.getValue("gender", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.countryCode", vat.ajax.getValue("countryCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.birthdayYear", vat.ajax.getValue("birthdayYear", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.birthdayMonth", vat.ajax.getValue("birthdayMonth", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.birthdayDay", vat.ajax.getValue("birthdayDay", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.tel1", vat.ajax.getValue("tel1", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.tel2", vat.ajax.getValue("tel2", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.mobilePhone", vat.ajax.getValue("mobilePhone", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.city", vat.ajax.getValue("city", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.area", vat.ajax.getValue("area", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.zipCode", vat.ajax.getValue("zipCode", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.address", vat.ajax.getValue("address", vat.ajax.xmlHttp.responseText));
							vat.item.setValueByName("#F.EMail", vat.ajax.getValue("EMail", vat.ajax.xmlHttp.responseText));
							//法人使用欄位 Maco 2017.02.15 Maco
							vat.item.setValueByName("#F.contractPerson", vat.ajax.getValue("contractPerson", vat.ajax.xmlHttp.responseText));
												
							//查出資料後,鎖住重要欄位 Daniel 2020.07.17 Daniel
							vat.item.setAttributeByName("#F.customerCode", "readOnly", true);
							vat.item.setAttributeByName("#F.identityCode", "readOnly", true);
						}
						onChangeCustomerTypeCode();
					}
	  		}
		});
	}
}
function refreshForm(code)
{

	document.forms[0]["#formId"].value = code; 
	document.forms[0]["#category"].value = ""; 
    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;  
    vat.bean().vatBeanOther.category = document.forms[0]["#category"].value;  

    vat.block.submit(
        function()
        {
            return "process_object_name=buCustomerModAction&process_object_method_name=performInitial"; 
        },
        {
        		other      : true, 
           funcSuccess:function()
           {
                vat.item.bindAll();
                vat.item.setValueByName("#F.cityA", "");
                vat.item.setValueByName("#F.areaA", "");
                
                //建立新資料後,清除遺漏欄位 Daniel 2020.07.17 Daniel
                vat.item.setValueByName("#F.customerCode", "");
                
 				doFormAccessControl();

        }}
    );
     
}
function doFormAccessControl()
{
	var category = vat.bean().vatBeanOther.category;
//	alert(typeof formId);
	if( category != "" )
	{
	/**欄位**/
		vat.item.setAttributeByName("#F.customerCode", "readOnly", true);
		vat.item.setAttributeByName("#F.enable", "readOnly", true);
		vat.item.setAttributeByName("#F.identityCode", "readOnly", true);
		vat.item.setAttributeByName("#F.vipTypeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.brandCode", "readOnly", true);
		vat.item.setAttributeByName("#F.loginEmployeeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.chineseName", "readOnly", true);
		vat.item.setAttributeByName("#F.englishName", "readOnly", true);
		vat.item.setAttributeByName("#F.vipStartDate", "readOnly", true);
		vat.item.setAttributeByName("#F.vipEndDate", "readOnly", true);
		vat.item.setAttributeByName("#F.applicationDate", "readOnly", true);
		vat.item.setAttributeByName("#F.category07", "readOnly", true);
		vat.item.setAttributeByName("#F.shortName", "readOnly", true);
		vat.item.setAttributeByName("#F.countryCode", "readOnly", true);
		vat.item.setAttributeByName("#F.birthdayYear", "readOnly", true);
		vat.item.setAttributeByName("#F.birthdayMonth", "readOnly", true);
		vat.item.setAttributeByName("#F.birthdayDay", "readOnly", true);
		vat.item.setAttributeByName("#F.gender", "readOnly", true);
		vat.item.setAttributeByName("#F.tel1", "readOnly", true);
		vat.item.setAttributeByName("#F.tel2", "readOnly", true);
		vat.item.setAttributeByName("#F.mobilePhone", "readOnly", true);
		vat.item.setAttributeByName("#F.EMail", "readOnly", true);
		vat.item.setAttributeByName("#F.cityA", "readOnly", true);
		vat.item.setAttributeByName("#F.areaA", "readOnly", true);
		vat.item.setAttributeByName("#F.city", "readOnly", true);
		vat.item.setAttributeByName("#F.area", "readOnly", true);
		vat.item.setAttributeByName("#F.zipCode", "readOnly", true);
		vat.item.setAttributeByName("#F.address", "readOnly", true);
		vat.item.setAttributeByName("#F.deliveryAddress", "readOnly", true);
		vat.item.setAttributeByName("#F.remark1", "readOnly", true);
		vat.item.setAttributeByName("#F.remark2", "readOnly", true);
		vat.item.setAttributeByName("#F.remark3", "readOnly", true);
		vat.item.setStyleByName("#B.submit","display", "none");
		vat.item.setStyleByName("#B.unlock","display", "inline");
		
		
		
		
		vat.item.setAttributeByName("#F.customerTypeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.taxType", "readOnly", true);
		vat.item.setAttributeByName("#F.taxRate", "readOnly", true);
		vat.item.setAttributeByName("#F.currencyCode", "readOnly", true);
		vat.item.setAttributeByName("#F.invoiceTypeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.contractPerson", "readOnly", true);
		vat.item.setAttributeByName("#F.paymentTermCode", "readOnly", true);
		vat.item.setAttributeByName("#F.zipCode1", "readOnly", true);
		vat.item.setAttributeByName("#F.city1", "readOnly", true);
		vat.item.setAttributeByName("#F.area1", "readOnly", true);
		vat.item.setAttributeByName("#F.address1", "readOnly", true);
		vat.item.setAttributeByName("#F.zipCode2", "readOnly", true);
		vat.item.setAttributeByName("#F.city2", "readOnly", true);
		vat.item.setAttributeByName("#F.area2", "readOnly", true);
		vat.item.setAttributeByName("#F.address2", "readOnly", true);
		vat.item.setAttributeByName("#F.zipCode3", "readOnly", true);
		vat.item.setAttributeByName("#F.city3", "readOnly", true);
		vat.item.setAttributeByName("#F.area3", "readOnly", true);
		vat.item.setAttributeByName("#F.address3", "readOnly", true);

		vat.item.setStyleByName("#B.copyinfo4","display", "none");
		vat.item.setStyleByName("#B.copyinfo5","display", "none");
		vat.item.setStyleByName("#B.copyinfo6","display", "none");
		/*
		vat.item.setStyleByName("#B.searchList","display", "none");
		vat.item.setStyleByName("#B.itemBrand","display", "none");
		vat.item.setStyleByName("#B.depManager","display", "none");
		vat.item.setStyleByName("#B.requestCode","display", "none");
		vat.item.setStyleByName("#B.submit","display", "none");
		vat.item.setStyleByName("#B.save","display", "none");
		vat.item.setStyleByName("#B.void","display", "none");
*/
		
	}
	else{
		vat.item.setAttributeByName("#F.customerCode", "readOnly", false);
		vat.item.setAttributeByName("#F.enable", "readOnly", false);
		vat.item.setAttributeByName("#F.identityCode", "readOnly", false);
		vat.item.setAttributeByName("#F.vipTypeCode", "readOnly", false);


		vat.item.setAttributeByName("#F.chineseName", "readOnly", false);
		vat.item.setAttributeByName("#F.englishName", "readOnly", false);
		vat.item.setAttributeByName("#F.vipStartDate", "readOnly", false);
		vat.item.setAttributeByName("#F.vipEndDate", "readOnly", false);
		vat.item.setAttributeByName("#F.applicationDate", "readOnly", false);
		vat.item.setAttributeByName("#F.category07", "readOnly", false);
		vat.item.setAttributeByName("#F.shortName", "readOnly", false);
		vat.item.setAttributeByName("#F.countryCode", "readOnly", false);
		vat.item.setAttributeByName("#F.birthdayYear", "readOnly", false);
		vat.item.setAttributeByName("#F.birthdayMonth", "readOnly", false);
		vat.item.setAttributeByName("#F.birthdayDay", "readOnly", false);
		vat.item.setAttributeByName("#F.gender", "readOnly", false);
		vat.item.setAttributeByName("#F.tel1", "readOnly", false);
		vat.item.setAttributeByName("#F.tel2", "readOnly", false);
		vat.item.setAttributeByName("#F.mobilePhone", "readOnly", false);
		vat.item.setAttributeByName("#F.EMail", "readOnly", false);
		vat.item.setAttributeByName("#F.cityA", "readOnly", false);
		vat.item.setAttributeByName("#F.areaA", "readOnly", false);
		vat.item.setAttributeByName("#F.city", "readOnly", false);
		vat.item.setAttributeByName("#F.area", "readOnly", false);
		vat.item.setAttributeByName("#F.zipCode", "readOnly", false);
		vat.item.setAttributeByName("#F.address", "readOnly", false);
		vat.item.setAttributeByName("#F.deliveryAddress", "readOnly", false);
		vat.item.setAttributeByName("#F.remark1", "readOnly", false);
		vat.item.setAttributeByName("#F.remark2", "readOnly", false);
		vat.item.setAttributeByName("#F.remark3", "readOnly", false);
		
		
		vat.item.setAttributeByName("#F.customerTypeCode", "readOnly", false);
		vat.item.setAttributeByName("#F.taxType", "readOnly", false);
		vat.item.setAttributeByName("#F.taxRate", "readOnly", false);
		vat.item.setAttributeByName("#F.currencyCode", "readOnly", false);
		vat.item.setAttributeByName("#F.invoiceTypeCode", "readOnly", false);
		vat.item.setAttributeByName("#F.contractPerson", "readOnly", false);
		vat.item.setAttributeByName("#F.paymentTermCode", "readOnly", false);
		vat.item.setAttributeByName("#F.zipCode1", "readOnly", false);
		vat.item.setAttributeByName("#F.city1", "readOnly", false);
		vat.item.setAttributeByName("#F.area1", "readOnly", false);
		vat.item.setAttributeByName("#F.address1", "readOnly", false);
		vat.item.setAttributeByName("#F.zipCode2", "readOnly", false);
		vat.item.setAttributeByName("#F.city2", "readOnly", false);
		vat.item.setAttributeByName("#F.area2", "readOnly", false);
		vat.item.setAttributeByName("#F.address2", "readOnly", false);
		vat.item.setAttributeByName("#F.zipCode3", "readOnly", false);
		vat.item.setAttributeByName("#F.city3", "readOnly", false);
		vat.item.setAttributeByName("#F.area3", "readOnly", false);
		vat.item.setAttributeByName("#F.address3", "readOnly", false);

		vat.item.setStyleByName("#B.copyinfo4","display", "inline");
		vat.item.setStyleByName("#B.copyinfo5","display", "inline");
		vat.item.setStyleByName("#B.copyinfo6","display", "inline");
		
		
		vat.item.setStyleByName("#B.submit","display", "inline");
		vat.item.setStyleByName("#B.unlock","display", "none");
		vat.item.setStyleByName("xTab2" , "display", "none");
		
	}
	onChangeCustomerTypeCode();
}
function kweMaster2(){
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
	id: "vatBlock_Master2", table:"cellspacing='0' class='default'  border='0' cellpadding='3'", 
	rows:[
		{row_style:"", cols:[
				 {items:[{name:"#L.taxType", type:"LABEL", value:"稅別"}]},
				 {items:[{name:"#F.taxType", type:"SELECT", bind:"taxType",init:allTaxType, eChange:"onChangeTaxType()" }]},
				 				 {items:[{name:"#L.taxRate", type:"LABEL", value:"稅率"}]},
				 {items:[{name:"#F.taxRate", type:"TEXT", bind:"taxRate" },
				 		 {name:"#L.taxRate1",type:"LABEL",  value:"%"}]},
			     {items:[{name:"#L.currencyCode", type:"LABEL", value:"使用幣別"}]},
				 {items:[{name:"#F.currencyCode", type:"SELECT", bind:"currencyCode",init:allSourceCurrency}]},
				 
			 	 {items:[{name:"#L.invoiceTypeCode", type:"LABEL", value:"發票類型"}]},
			 	 {items:[{name:"#F.invoiceTypeCode", type:"SELECT", bind:"invoiceTypeCode",init:allInvoiceType }]}


			 ]},
			 
		{row_style:"", cols:[
		
				 {items:[{name:"#L.contractPerson", type:"LABEL", value:"聯絡人"}]},
				 {items:[{name:"#F.contractPerson", type:"TEXT", bind:"contractPerson" }]},
				 {items:[{name:"#L.paymentTermCode", type:"LABEL", value:"付款條件"}]},
				 {items:[{name:"#F.paymentTermCode", type:"SELECT", bind:"paymentTermCode",init:allpaymentTermCode }],td:"colSpan=5"}

		]},	
		{row_style:"", cols:[

				{items:[{name:"#L.divider", type:"LABEL" , value:"--"}],td:"colSpan=8"}]},	
	/*
	{row_style:"", cols:[
				{items:[{name:"#L.classify", type:"LABEL" , value:"類別"}], td:"style='background-color:#990000; color:#fff; '"},
				{items:[{name:"#L.contactPerson", type:"LABEL" , value:"聯絡人"}], td:"colSpan=2 style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.tel", type:"LABEL" , value:"電話"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.fax", type:"LABEL" , value:"傳真"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.email", type:"LABEL" , value:"電子信箱"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.buttonList01", type:"LABEL" , value:""}], td:"colSpan=3 style='background-color:#990000; color:#fff;'"}
			]},
			
			{row_style:"", cols:[
				{items:[{name:"#L.classify_1", type:"LABEL" , value:"業務窗口"}]},
				{items:[{name:"#F.contactPerson1", type:"TEXT" , bind:"contactPerson1" , size:10, maxLen:20}],td:"colSpan=2"},
				{items:[{name:"#F.tel1", type:"TEXT" , bind:"tel1" , size:20, maxLen:50}]},
				{items:[{name:"#F.fax1", type:"TEXT" , bind:"fax1" , size:20, maxLen:50}]},
				{items:[{name:"#F.EMail1", type:"TEXT" , bind:"EMail1" ,size:40, maxLen:100}],td:"colSpan=3"}
			]},

			{row_style:"", cols:[
				{items:[{name:"#L.classify_2", type:"LABEL" , value:"財務窗口"}]},
				{items:[{name:"#F.contactPerson2", type:"TEXT", bind:"contactPerson2" , size:10, maxLen:20}],td:"colSpan=2"},
				{items:[{name:"#F.tel2", type:"TEXT" , bind:"tel2" , size:20, maxLen:50}]},
				{items:[{name:"#F.fax2", type:"TEXT" , bind:"fax2" , size:20, maxLen:50}]},
				{items:[{name:"#F.EMail2", type:"TEXT" , bind:"EMail2" , size:40, maxLen:100}]},
				{items:[{name:"#B.copyinfo1"      , type:"button"    ,value:"同上", eClick:'copyInfo("classify_2")'}],td:"colSpan=3"}
			]},
			
			{row_style:"", cols:[
				{items:[{name:"#L.classify_3", type:"LABEL" , value:"倉庫窗口"}]},
				{items:[{name:"#F.contactPerson3", type:"TEXT" , bind:"contactPerson3" , size:10, maxLen:20}],td:"colSpan=2"},
				{items:[{name:"#F.tel3", type:"TEXT" , bind:"tel3" , size:20, maxLen:50}]},
				{items:[{name:"#F.fax3", type:"TEXT" , bind:"fax3" , size:20, maxLen:50}]},
				{items:[{name:"#F.EMail3", type:"TEXT" , bind:"EMail3" , size:40, maxLen:100}]},
				{items:[{name:"#B.copyinfo2"      , type:"button"    ,value:"同上", eClick:'copyInfo("classify_3")'}],td:"colSpan=3"}
			]},						

			{row_style:"", cols:[
				{items:[{name:"#L.classify_4", type:"LABEL" , value:"其他窗口"}]},
				{items:[{name:"#F.contactPerson4", type:"TEXT" , bind:"contactPerson4" , size:10, maxLen:20}],td:"colSpan=2"},
				{items:[{name:"#F.tel4", type:"TEXT" , bind:"tel4" , size:20, maxLen:50}]},
				{items:[{name:"#F.fax4", type:"TEXT" , bind:"fax4" , size:20, maxLen:50}]},
				{items:[{name:"#F.EMail4", type:"TEXT" ,bind:"EMail4", size:40, maxLen:100}]},
				{items:[{name:"#B.copyinfo3"      , type:"button"    ,value:"同上", eClick:'copyInfo("classify_4")'}],td:"colSpan=3"}
			]},*/
			{row_style:"", cols:[
				{items:[{name:"#L.address_0", type:"LABEL" , value:"類別"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.city", type:"LABEL" , value:"縣市"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.area", type:"LABEL" , value:"鄉鎮市區"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.zipCode", type:"LABEL" , value:"郵遞區號"}], td:"style='background-color:#990000; color:#fff;'"},
				{items:[{name:"#L.address", type:"LABEL" , value:"地址"}], td:"colSpan=5 style='background-color:#990000; color:#fff;'"}
			]},		
			{row_style:"", cols:[
				{items:[{name:"#L.address_1", type:"LABEL" , value:"通訊地址"}]},
				{items:[{name:"#F.city1", type:"TEXT" , bind:"city1" , size:10, maxLen:50}]},
				{items:[{name:"#F.area1", type:"TEXT" , bind:"area1" , size:10, maxLen:50}]},
				{items:[{name:"#F.zipCode1", type:"TEXT" , bind:"zipCode1" , size:20, maxLen:50}]},
				{items:[{name:"#F.address1", type:"TEXT" , bind:"address1" , size:60, maxLen:150}],td:"colSpan=2"},
				{items:[{name:"#B.copyinfo4"      , type:"button"    ,value:"同主檔地址", eClick:'copyInfo("address_1")'}],td:"colSpan=3"}
			]},

			{row_style:"", cols:[
				{items:[{name:"#L.address_2", type:"LABEL" , value:"發票地址"}]},
				{items:[{name:"#F.city2", type:"TEXT" , bind:"city2" , size:10, maxLen:50}]},
				{items:[{name:"#F.area2", type:"TEXT" , bind:"area2" , size:10, maxLen:50}]},
				{items:[{name:"#F.zipCode2", type:"TEXT" , bind:"zipCode2" , size:20, maxLen:50}]},
				{items:[{name:"#F.address2", type:"TEXT" , bind:"address2" , size:60, maxLen:150}],td:"colSpan=2"},
				{items:[{name:"#B.copyinfo5"      , type:"button"    ,value:"同上", eClick:'copyInfo("address_2")'}],td:"colSpan=3"}
			]},
			
			{row_style:"", cols:[
				{items:[{name:"#L.address_3", type:"LABEL" , value:"送貨地址"}]},
				{items:[{name:"#F.city3", type:"TEXT" , bind:"city3" , size:10, maxLen:50}]},
				{items:[{name:"#F.area3", type:"TEXT" , bind:"area3", size:10, maxLen:50}]},
				{items:[{name:"#F.zipCode3", type:"TEXT" , bind:"zipCode3" , size:20, maxLen:50}]},
				{items:[{name:"#F.address3", type:"TEXT" , bind:"address3" , size:60, maxLen:150}],td:"colSpan=2"},
				{items:[{name:"#B.copyinfo6"      , type:"button"    ,value:"同上", eClick:'copyInfo("address_3")'}],td:"colSpan=3"}
			]}						
/*,
			{row_style:"", cols:[
				{items:[{name:"#L.address_4", type:"LABEL" , value:"其他地址"}]},
				{items:[{name:"#F.city4", type:"TEXT" , bind:"city4" , size:10, maxLen:50}]},
				{items:[{name:"#F.area4", type:"TEXT" , bind:"area4" , size:10, maxLen:50}]},
				{items:[{name:"#F.zipCode4", type:"TEXT" , bind:"zipCode4" , size:20, maxLen:50}]},
				{items:[{name:"#F.address4", type:"TEXT" , bind:"address4" , size:60, maxLen:50}],td:"colSpan=2"},
				{items:[{name:"#B.copyinfo7"      , type:"button"    ,value:"同上", eClick:'copyInfo("address_4")'}],td:"colSpan=3"}
			]}		*/						
		
 	 	], 	 	
		beginService:"",
		closeService:""
	});	
}

function onChangeCustomerTypeCode(){
	
	if(vat.item.getValueByName("#F.customerTypeCode")=="2"){

		vat.item.setStyleByName("xTab2" , "display", "inline");
	}
	else{

		vat.item.setStyleByName("xTab2" , "display", "none");
	}
}
function unlock(){
	document.forms[0]["#category"].value = ""; 
	vat.bean().vatBeanOther.category = document.forms[0]["#category"].value;  

	doFormAccessControl();
	
	//查出資料後,鎖住重要欄位 Daniel 2020.07.17 Daniel
	vat.item.setAttributeByName("#F.customerCode", "readOnly", true);
	vat.item.setAttributeByName("#F.identityCode", "readOnly", true);
}
// 開啟視窗
function createNewForm(){
	//開啟buCUstomer.js
	if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    var url = "/erp/Bu_Customer:create:20160714mod.page?"; 

     sc=window.open(url, '會員資料維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
     sc.resizeTo((screen.availWidth),(screen.availHeight));
     sc.moveTo(0,0);

 
	}
}
function copyInfo(copyType){


	var sorceCity="";
	var sorceArea="";
	var sorceZipCode="";
	var sorceAddress="";

	var targetCity="";
	var targetArea="";
	var targetZipCode="";
	var targetAddress="";
	if(copyType == 'address_1'){
	
		sorceCity 	 = vat.item.getValueByName("#F.city");
		sorceArea 	 = vat.item.getValueByName("#F.area");
		sorceZipCode = vat.item.getValueByName("#F.zipCode");
		sorceAddress = vat.item.getValueByName("#F.address");
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

	
	var processString = "process_object_name=buCustomerModService&process_object_method_name=getInfoAsTop"+
						"&sorceCity="   		+ sorceCity +
						"&sorceArea="   		+ sorceArea +
						"&sorceZipCode=" 		+ sorceZipCode +
						"&sorceAddress=" 		+ sorceAddress;
	vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){

					//alert(targetCity+targetArea+targetZipCode+targetAddress);
					vat.item.setValueByName(targetCity			, vat.ajax.getValue("sorceCity", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetArea			, vat.ajax.getValue("sorceArea", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetZipCode		, vat.ajax.getValue("sorceZipCode", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName(targetAddress		, vat.ajax.getValue("sorceAddress", vat.ajax.xmlHttp.responseText));

  			}
		} );

}
