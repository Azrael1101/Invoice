/*** 
 *	檔案: buExchangeRate.js
 *	說明：匯率維護作業
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
	doFormAccessControl();
}

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	
		vat.bean().vatBeanOther = {
		    organizationCode : document.forms[0]["#organizationCode"].value,
		    sourceCurrency : "",
		    againstCurrency : "",
		    beginDate : "",
		    lastUpdatedBy: "",
		    lastUpdateDate: "",
			loginBrandCode : document.forms[0]["#loginBrandCode"].value,
			loginEmployeeCode : document.forms[0]["#loginEmployeeCode"].value,
			formId : document.forms[0]["#formId"].value,
			currentRecordNumber : 0,
			lastRecordNumber : 0
		};
	     
		vat.bean.init(
			function(){
			    return "process_object_name=buBasicDataAction&process_object_method_name=performBuExchangeInitial"; 
			},
			{other : true}
		);
		//vat.item.SelectBind(vat.bean("allExchangeRateType")         ,{ itemName : "#F.exchangeRateType" });
		//vat.item.bindAll();
	}
}

function buttonLine(){
	var vsMaxRecord = 0;
	var vsCurrentRecord = 0;
    vat.block.create(
    	vnB_Button, 
    	{
			id : "vatBlock_Button", 
			generate : true,
			table : "cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
			title : "", 
			rows : [  
				 {
				 	row_style : "", 
				 	cols : [
					 	{
					 		items : [
					 			{name : "#B.new", type : "IMG", value : "新增", src : "./images/button_create.gif", eClick : "createNewForm()"},
					 			{name : "SPACE", type : "LABEL", value : "　"},
					 			{
					 				name : "#B.search", type :"PICKER", value : "查詢", src : "./images/button_find.gif", openMode : "open",
					 				servicePassData:function()
										  	{return "&organizationCode="+document.forms[0]["#organizationCode"].value;},
					 				service : "Bu_ExchangeRate:search:20131021.page", left : 0, right : 0, width : 1024, height : 768,	
					 				serviceAfterPick : function(){doAfterPickerProcess();}
					 			},
					 			{name : "SPACE", type : "LABEL", value : "　"},
					 	 		{name : "#B.exit", type : "IMG", value : "離開", src : "./images/button_exit.gif", eClick : "closeWindows('CONFIRM')"},
					 	 		{name : "SPACE", type : "LABEL", value : "　"},
					 			{name : "#B.submit", type : "IMG", value : "送出", src : "./images/button_submit.gif", eClick : "doSubmit('SUBMIT')"},
					 			{name : "submitSpace", type : "LABEL", value :"　"},
					 			{name : "#B.first", type : "IMG", value : "第一筆", src : "./images/play-first.png", eClick : "gotoFirst()"},
					 			{name : "SPACE", type : "LABEL", value : "　"},
					 			{name : "#B.forward", type : "IMG", value : "上一筆", src : "./images/play-back.png", eClick : "gotoForward()"},
					 			{name : "SPACE", type : "LABEL", value : "　"},
					 			{name : "#B.next", type : "IMG", value : "下一筆", src : "./images/play.png", eClick : "gotoNext()"},
					 			{name : "SPACE", type : "LABEL", value : "　"},
					 			{name : "#B.last", type : "IMG", value : "最後一筆", src : "./images/play-forward.png", eClick : "gotoLast()"},
					 			{name : "#L.currentRecord", type : "NUMB", bind : vsCurrentRecord, size : 4, mode :"READONLY"},
					 			{name : "SPACE", type : "LABEL", value : " / "},
					 			{name : "#L.maxRecord", type : "NUMB", bind : vsMaxRecord, size : 4, mode : "READONLY"}
					 			
					 	   ],
					 	   td : "style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
					 	}
					]
				 }
	  		], 	 
			beginService : "",
			closeService : ""			
		}
	);
	//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){
    var allExchangeRateType    = vat.bean("allExchangeRateType");
    var allSourceCurrency      = vat.bean("allSourceCurrency");
    var allAgainstCurrency     = vat.bean("allAgainstCurrency");
	var againstT2Currency           = [[true], ["台幣"],["NTD"]];
	
	if(document.forms[0]["#organizationCode"].value=='TM'){
	vat.block.create( 
		vnB_Header,
		{ //vnB_Header = 
			id : "vatBlock_Head", 
			generate : true,
			table : "cellspacing='1' class='default' border='0' cellpadding='2' style='width:33%'",
			title : "匯率維護作業",
			rows : [
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.organizationCode", type : "LABEL", value : "匯率別<font color='red'>*</font>"}]},
						{items : [{name : "#F.organizationCode", type:"SELECT", bind : "organizationCode",init:allExchangeRateType, mode : "READONLY"}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.sourceCurrency", type : "LABEL", value : "來源幣別<font color='red'>*</font>"}]},
						{items : [{name : "#F.sourceCurrency", type : "SELECT", bind : "sourceCurrency",init:allSourceCurrency}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.againstCurrency", type : "LABEL", value : "目標幣別<font color='red'>*</font>"}]},
						{items : [{name : "#F.againstCurrency", type : "SELECT", bind : "againstCurrency",init:allAgainstCurrency}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.exchangeRate", type : "LABEL", value : "匯率<font color='red'>*</font>"}]},
						{items : [{name : "#F.exchangeRate", type : "NUMB", bind : "exchangeRate", size : 7}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.beginDate", type : "LABEL", value : "啟用日期<font color='red'>*</font>"}]},
						{items : [{name : "#F.beginDate", type : "DATE", bind : "beginDate", size : 2}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.brandCode", type : "LABEL", value : "品牌<font color='red'>*</font>"}]},
						{items : [{name : "#F.brandCode", type : "TEXT",value:"T2", size : 2, maxLen : 2,mode : "READONLY"}]}
					]
				},
		        {row_style:"", cols:[			
					{items:[{name:"#L.lastUpdatedBy", type:"LABEL", value:"修改人員" }]},
					{items:[{name:"#F.lastUpdatedBy", type:"TEXT", bind:"lastUpdatedBy", size:5,mode:"readOnly" },
							{name:"#F.lastUpdatedByName", type:"TEXT", bind:"lastUpdateByName", size:5,mode:"readOnly" }]}
				]},
				{row_style:"", cols:[			
			        {items:[{name:"#L.lastUpdateDate", type:"LABEL" , value:"修改日期"}]},
			    	{items:[{name:"#F.lastUpdateDate", type:"TEXT",  bind:"lastUpdateDate" , size:10 , mode:"readOnly"  }]}
				]},
					{row_style:"", cols:[
					{items:[{name:"#L.note", type:"LABEL" , value:"<font color='red'>*</font>為必填欄位，請務必填寫。"}],td:" colSpan=4"}
					]}		
			], 	
			beginService:"",
			closeService:""			
		});
	  }else{
	  
	   vat.block.create( 
		vnB_Header,
		{ //vnB_Header = 
			id : "vatBlock_Head", 
			generate : true,
			table : "cellspacing='1' class='default' border='0' cellpadding='2' style='width:33%'",
			title : "匯率維護作業",
			rows : [
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.organizationCode", type : "LABEL", value : "匯率別<font color='red'>*</font>"}]},
						{items : [{name : "#F.organizationCode", type:"SELECT", bind : "organizationCode",init:allExchangeRateType, mode : "READONLY"}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.sourceCurrency", type : "LABEL", value : "來源幣別<font color='red'>*</font>"}]},
						{items : [{name : "#F.sourceCurrency", type : "SELECT", bind : "sourceCurrency",init:allSourceCurrency}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.againstCurrency", type : "LABEL", value : "目標幣別<font color='red'>*</font>"}]},
						{items : [{name : "#F.againstCurrency", type : "SELECT", bind : "againstCurrency",init:againstT2Currency}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.exchangeRate", type : "LABEL", value : "匯率<font color='red'>*</font>"}]},
						{items : [{name : "#F.exchangeRate", type : "NUMB", bind : "exchangeRate", size : 7}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.beginDate", type : "LABEL", value : "啟用日期<font color='red'>*</font>"}]},
						{items : [{name : "#F.beginDate", type : "DATE", bind : "beginDate", size : 2}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.brandCode", type : "LABEL", value : "品牌<font color='red'>*</font>"}]},
						{items : [{name : "#F.brandCode", type : "TEXT",value:"T2", size : 2, maxLen : 2,mode : "READONLY"}]}
					]
				},
		        {row_style:"", cols:[			
					{items:[{name:"#L.lastUpdatedBy", type:"LABEL", value:"修改人員" }]},
					{items:[{name:"#F.lastUpdatedBy", type:"TEXT", bind:"lastUpdatedBy", size:5,mode:"readOnly" },
							{name:"#F.lastUpdatedByName", type:"TEXT", bind:"lastUpdateByName", size:5,mode:"readOnly" }]}
				]},
				{row_style:"", cols:[			
			        {items:[{name:"#L.lastUpdateDate", type:"LABEL" , value:"修改日期"}]},
			    	{items:[{name:"#F.lastUpdateDate", type:"TEXT",  bind:"lastUpdateDate" , size:10 , mode:"readOnly"  }]}
				]},
					{row_style:"", cols:[
					{items:[{name:"#L.note", type:"LABEL" , value:"<font color='red'>*</font>為必填欄位，請務必填寫。"}],td:" colSpan=4"}
					]}		
			], 	
			beginService:"",
			closeService:""			
		});
	  
	  }	
	}

// 建立新資料按鈕	
function createNewForm(){
	if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
		vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord", "0");
		vat.bean().vatBeanPicker.result = null;  
		refreshForm("","","","");
	}
}

// 送出的返回
function createRefreshForm(){
     vat.item.setValueByName("#L.currentRecord", "0");
     vat.item.setValueByName("#L.maxRecord", "0");
     vat.bean().vatBeanPicker.result = null;  
	 refreshForm("","","","");
}

// 離開按鈕按下
function closeWindows(closeType){
	var isExit = true;
	if("CONFIRM" == closeType){
		isExit = confirm("是否確認離開?");
	}
	if(isExit){
		window.top.close();
	}
}

// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage = "是否確定送出?";
	
	if(vat.item.getValueByName("#F.organizationCode")!=""){
	
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.formAction = formAction;
		vat.block.submit(
			function(){
				return "process_object_name=buBasicDataAction" + "&process_object_method_name=performBuExchangeRateTransaction";
			},
			{bind : true, link : true, other : true}
		);
	}
	 }else{
	    alert("請輸入匯率別!!");
	 }
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result != null){
		var vsMaxSize = vat.bean().vatBeanPicker.result.length;
		if(vsMaxSize === 0){
			vat.bean().vatBeanOther.firstRecordNumber = 0;
			vat.bean().vatBeanOther.lastRecordNumberm= 0;
			vat.bean().vatBeanOther.currentRecordNumber = 0;
		}
		else{
			vat.bean().vatBeanOther.firstRecordNumber = 1;
			vat.bean().vatBeanOther.lastRecordNumber = vsMaxSize ;
			vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
			var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber - 1];
			//alert(code["id.beginDate"]);
			//alert((code).id.beginDate);
			refreshForm(code["id.organizationCode"],code["id.sourceCurrency"],code["id.againstCurrency"],code["id.beginDate"]);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord", vat.bean().vatBeanOther.lastRecordNumber);
	}
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber > 0){
		if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
			vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
			var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber - 1];
			vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
			refreshForm(code["id.organizationCode"],code["id.sourceCurrency"],code["id.againstCurrency"],code["id.beginDate"]);
		}
		else{
			alert("目前已在第一筆資料");
		}
	}
	else{
		alert("無資料可供翻頁");
	}
}

// 上一筆
function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber > 0){
		if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
			vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
			var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber - 1];
			vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
			refreshForm(code["id.organizationCode"],code["id.sourceCurrency"],code["id.againstCurrency"],code["id.beginDate"]);
		}
		else{
			alert("目前已在第一筆資料");
		}
	}
	else{
		alert("無資料可供翻頁");
	}
}

// 下一筆
function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber > 0){
		if(vat.bean().vatBeanOther.currentRecordNumber + 1 <= vat.bean().vatBeanOther.lastRecordNumber){
			vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
			var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber - 1];
			vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
			refreshForm(code["id.organizationCode"],code["id.sourceCurrency"],code["id.againstCurrency"],code["id.beginDate"]);
		}
		else{
			alert("目前已在最後一筆資料");
		}
	}
	else{
		alert("無資料可供翻頁");
	}
}

// 最後一筆
function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber > 0){
		if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
			vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
			var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber - 1];
			vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
			refreshForm(code["id.organizationCode"],code["id.sourceCurrency"],code["id.againstCurrency"],code["id.beginDate"]);
		}
		else{
			alert("目前已在最後一筆資料");
		}
	}
	else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(organizationCode,sourceCurrency,againstCurrency,beginDate){

	organizationCode = document.forms[0]["#organizationCode"].value;
	//vat.bean().vatBeanOther.organizationCode = organizationCode;
	vat.bean().vatBeanOther.sourceCurrency = sourceCurrency;
	vat.bean().vatBeanOther.againstCurrency = againstCurrency;
	vat.bean().vatBeanOther.beginDate = beginDate;
		
	vat.block.submit(
		function(){
			return "process_object_name=buBasicDataAction&process_object_method_name=performBuExchangeInitial"; 
		},
		{other : true,
			funcSuccess : function(){
				vat.item.bindAll(); 
				doFormAccessControl();
			}
		}
	);	
}

// 依formId鎖form
function doFormAccessControl(){
	var formId = vat.bean().vatBeanOther.formId;
	var form    = vat.bean("form");
	if(vat.bean().vatBeanPicker.result!=null){
	//alert("1");
		vat.item.setAttributeByName("#F.organizationCode", "readOnly", true);
		vat.item.setAttributeByName("#F.sourceCurrency", "readOnly", true);	
		vat.item.setAttributeByName("#F.againstCurrency", "readOnly", true);	
		vat.item.setAttributeByName("#F.beginDate", "readOnly", true);			
	}
	else{
	//alert("2");
	 // vat.item.setAttributeByName("#F.exchangeRateType", "readOnly", false);
		//vat.item.setAttributeByName("#F.organizationCode", "readOnly", false);
		vat.item.setAttributeByName("#F.sourceCurrency", "readOnly", false);	
		vat.item.setAttributeByName("#F.againstCurrency", "readOnly", false);	
		vat.item.setAttributeByName("#F.beginDate", "readOnly", false);
	}
	
	var enable = vat.item.getValueByName("#F.enable");
	if(enable == "Y"){
		vat.item.setValueByName("#F.enable", "N");
	}
	else{
		vat.item.setValueByName("#F.enable", "Y");
	}
}
