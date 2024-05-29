/*** 
 *	檔案: company.js
 *	說明：表單明細
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
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId                : document.forms[0]["#formId"].value,
          //companyCode  		: "",      
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
        
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=buCompanyAction&process_object_method_name=performBuCompanyInitial"; 
	    	},{
	    		bind:true, link:true, other:true
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
	 									 service:"Bu_Company:search:20170807.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:"closeWindows('CONFIRM')"},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:"doSubmit('SUBMIT')"},
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
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

	function headerInitial(){ 
		vat.block.create( vnB_Header, { //vnB_Header = 
			id: "vatBlock_Head", generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"公司基本資料維護作業", 
			rows:[
				{row_style:"", cols:[
					{items:[{name:"#L.companyCode", type:"LABEL", value:"公司代號<font color='red'>*</font>" }]},
					{items:[{name:"#F.companyCode", type:"TEXT", bind:"companyCode" , size:25, maxLen:25 , eChange:"changeCompanyCode()" }]},
				  	{items:[{name:"#L.companyName", type:"LABEL", value:"公司名稱<font color='red'>*</font>"}]},
					{items:[{name:"#F.companyName", type:"TEXT", bind:"companyName", size:25, maxLen:25 }]}			
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.businessMasterName", type:"LABEL" , value:"營業人名稱"}]},
					{items:[{name:"#F.businessMasterName", type:"TEXT", bind:"businessMasterName", size:25, maxLen:25 }]},
					{items:[{name:"#L.guiCode", type:"LABEL" , value:"統一編號<font color='red'>*</font>"}]},
					{items:[{name:"#F.guiCode", type:"TEXT", bind:"guiCode", size:25, maxLen:8 }]}					
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.taxRegisterNo", type:"LABEL" , value:"稅籍編號"}]},
					{items:[{name:"#F.taxRegisterNo", type:"TEXT", bind:"taxRegisterNo", size:25, maxLen:25 }]},
					{items:[{name:"#L.registerMasterName", type:"LABEL" , value:"申報人名稱"}]},
					{items:[{name:"#F.registerMasterName", type:"TEXT", bind:"registerMasterName", size:25, maxLen:25 }]}
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.masterName", type:"LABEL" , value:"負責人"}]},
					{items:[{name:"#F.masterName", type:"TEXT", bind:"masterName", size:25, maxLen:25 }]},
					{items:[{name:"#L.accreditee", type:"LABEL" , value:"受文者"}]},
					{items:[{name:"#F.accreditee", type:"TEXT", bind:"accreditee", size:25, maxLen:25 }]}
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.tel", type:"LABEL" , value:"電話"}]},
					{items:[{name:"#F.tel", type:"TEXT", bind:"tel", size:25, maxLen:25 }]},
					{items:[{name:"#L.address", type:"LABEL" , value:"地址"}]},
					{items:[{name:"#F.address", type:"TEXT", bind:"address", size:25, maxLen:60 }]}			
	            ]},
				{row_style:"", cols:[
					{items:[{name:"#L.reportTitle", type:"LABEL" , value:"報表抬頭"}]},
					{items:[{name:"#F.reportTitle", type:"TEXT", bind:"reportTitle", size:25, maxLen:60 }] ,td:" colSpan=3"    }				
				]},
		       /* {row_style:"", cols:[			
					{items:[{name:"#L.createdBy", type:"LABEL", value:"建檔人員" }]},
					{items:[{name:"#F.createdBy", type:"TEXT", bind:"createdBy", size:25,mode:"readOnly" },
							{name:"#F.createdByName", type:"TEXT", bind:"createdByName", size:25,mode:"readOnly" }]},
			        {items:[{name:"#L.creationDate", type:"LABEL" , value:"建檔日期"}]},
			    	{items:[{name:"#F.creationDate", type:"TEXT",  bind:"creationDate" , size:20 , mode:"readOnly"  }]}
				]},*/
		        {row_style:"", cols:[			
					{items:[{name:"#L.lastUpdatedBy", type:"LABEL", value:"修改人員" }]},
					{items:[{name:"#F.lastUpdatedBy", type:"TEXT", bind:"lastUpdatedBy", size:25,mode:"readOnly" },
							{name:"#F.lastUpdateByName", type:"TEXT", bind:"lastUpdateByName", size:25,mode:"readOnly" }]},
					
			        {items:[{name:"#L.lastUpdateDate", type:"LABEL" , value:"修改日期"}]},
			    	{items:[{name:"#F.lastUpdateDate", type:"TEXT",  bind:"lastUpdateDate" , size:20 , mode:"readOnly"  }]}
				]},
					{row_style:"", cols:[
					{items:[{name:"#L.note", type:"LABEL" , value:"<font color='red'>*</font>為必填欄位，請務必填寫。"}],td:" colSpan=4"}
					]}		
			], 	
			beginService:"",
			closeService:""			
		});
	}

// 動態設定公司代號為大寫
function changeCompanyCode(){
	var companyCode = vat.item.getValueByName("#F.companyCode").replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setValueByName("#F.companyCode", companyCode);
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
	var alertMessage ="是否確定送出?";
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.formAction = formAction;
		vat.block.submit(function(){
				return "process_object_name=buCompanyAction"+
				"&process_object_method_name=performBuCompanyTransaction";
			},{
				bind:true, link:true, other:true 
			}
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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].companyCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	//vat.bean().vatBeanOther.companyCode       = companyCode;
	document.forms[0]["#formId"].value = code;
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
	vat.block.submit(
		function(){
			return "process_object_name=buCompanyAction&process_object_method_name=performBuCompanyInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				doFormAccessControl();
     	}}
    );	
}

// 依formId鎖form
function doFormAccessControl(){
    var formId = vat.bean().vatBeanOther.formId;
    var form    = vat.bean("form");
    if(vat.bean().vatBeanPicker.result!=null){
    //alert("1");
        //vat.item.setAttributeByName("#F.exchangeRateType", "readOnly", true);
        vat.item.setAttributeByName("#F.companyCode", "readOnly", true);
       
    }
    else{
    //alert("2");
        //vat.item.setValueByName("#F.exchangeRate", "");
       // vat.item.setAttributeByName("#F.exchangeRateType", "readOnly", false);
        vat.item.setAttributeByName("#F.companyCode", "readOnly", false);
        //vat.item.setAttributeByName("#F.sourceCurrency", "readOnly", false);    
        //vat.item.setAttributeByName("#F.againstCurrency", "readOnly", false);   
        //vat.item.setAttributeByName("#F.beginDate", "readOnly", false);
    }
    
    var enable = vat.item.getValueByName("#F.enable");
    if(enable == "Y"){
        vat.item.setValueByName("#F.enable", "N");
    }
    else{
        vat.item.setValueByName("#F.enable", "Y");
    }
}


