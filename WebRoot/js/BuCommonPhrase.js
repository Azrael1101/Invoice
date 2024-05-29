/*** 
 *	檔案: BuCommonPhrase.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Detail = 1;
var vnB_Button = 0;
function outlineBlock(){
  kweImInitial();
  buttonLine();//按鈕列表
  kweImHeader();

                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");                                                                                                                                                                                                                                          
		vat.tabm.createButton(0 ,"xTab1","設定檔明細" ,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", "none" , "");
  		kweImDetail();
	
		vat.tabm.displayToggle(0, "xTab1", true);
		
	//vat.tabm.displayToggle(0, "xTab", false);
	//vat.tabm.displayToggle(0, "vatDetailDiv", true);
	//vat.tab.displayToggle("xTab", false);
	//alert(document.getElementById("vatTabSpan").style.display);
	//document.getElementById("vatTabSpan").style.display = "inline";
	//vat.item.setStyleByName("vatDetailDiv" , "display" , "inline");
	
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
	 									 service:"Bu_CommonPhrase:search:20160909.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}
function kweImHeader(){ 

vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"常用字彙維護作業", 
	rows:[  
	 	{row_style:"", cols:[
			{items:[{name:"#L.headCode", type:"LABEL" , value:"常用字彙類別代碼"}]},
			{items:[{name:"#F.headCode", type:"TEXT",  bind:"headCode" , size:20 , mode:"" , eChange:"changeHeadCode()"},
					{name:"#L.enable", type:"LABEL", value:"啟用"},
					{name:"#F.enable", type:"XBOX",  bind:"enable" , size:20 , mode:"" }],td:" colSpan=3"},
			{items:[{name:"#L.name"  , type:"LABEL" , value:"常用字彙類別名稱"}]},
			{items:[{name:"#F.name"  , type:"TEXT"  , bind:"name" , size:20, mode:""}],td:" colSpan=3"}
	 	]},

	 	{row_style:"", cols:[
		 	{items:[{name:"#L.description"  , type:"LABEL", value:"說明"}]},
		 	{items:[{name:"#F.description"  , type:"TEXT",   bind:"description", size:200, maxLen:100, desc:"此字彙之說明"}],td:" colSpan=7"}
	 	]},
	 	{row_style:"", cols:[
		 	{items:[{name:"#L.reserve"  , type:"LABEL", value:"預留欄位"}]},
		 	{items:[{name:"#L.reserve1", type:"LABEL", value:"1."},
		 			{name:"#F.reserve1"  , type:"TEXT",   bind:"reserve1", size:20},
		 			{name:"#L.reserve2", type:"LABEL", value:"2."},
		 			{name:"#F.reserve2"  , type:"TEXT",   bind:"reserve2", size:20},
		 			{name:"#L.reserve3", type:"LABEL", value:"3."},
		 			{name:"#F.reserve3"  , type:"TEXT",   bind:"reserve3", size:20},
		 			{name:"#L.reserve4", type:"LABEL", value:"4."},
		 			{name:"#F.reserve4"  , type:"TEXT",   bind:"reserve4", size:20},
		 			{name:"#L.reserve5", type:"LABEL", value:"5."},
		 			{name:"#F.reserve5"  , type:"TEXT",   bind:"reserve5", size:20}],td:" colSpan=7"}
	 	]},
	    {row_style:"", cols:[
	 		{items:[{name:"#L.createdBy"  , type:"LABEL", value:"建檔人員"}]},
		 	{items:[{name:"#F.createdBy"  , type:"TEXT",   bind:"createdBy", size:20, mode:"READONLY"}]},
		 	{items:[{name:"#L.creationDate"  , type:"LABEL", value:"建檔日期"}]},
		 	{items:[{name:"#F.creationDate"  , type:"TEXT",   bind:"creationDate", size:20, mode:"READONLY"}]},
		 	{items:[{name:"#L.lastUpdatedBy"  , type:"LABEL", value:"最後更新人員"}]},
		 	{items:[{name:"#F.lastUpdatedBy"  , type:"TEXT",   bind:"lastUpdatedBy", size:20, mode:"READONLY"}]},
		 	{items:[{name:"#L.lastUpdateDate"  , type:"LABEL", value:"最後更新日期"}]},
		 	{items:[{name:"#F.lastUpdateDate"  , type:"TEXT",   bind:"lastUpdateDate", size:20, mode:"READONLY"}]}
	 	]}

  ], 	 
		beginService:"",
		closeService:""			
	});

}


function kweImInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  
  	vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value

        };
//  	 vat.bean().loginBrandCode     = document.forms[0]["#loginBrandCode"    ].value;  	
// 	 vat.bean().loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" ].value;	  
//	 	 vat.bean().formId             = document.forms[0]["#formId"            ].value;	
	 	 
     vat.bean.init(function(){
		 return "process_object_name=buCommonPhraseAction&process_object_method_name=performMainInitial"; 
     },{other: true , bind: true});
  }
 
}

function execSubmitAction(formAction){

	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}
	if(confirm(alertMessage)){
	    var formId                = vat.bean().formId.replace(/^\s+|\s+$/, '');;
	    var employeeCode          = vat.bean().loginEmployeeCode.replace(/^\s+|\s+$/, '');;
	    var brandCode          = vat.bean().loginBrandCode.replace(/^\s+|\s+$/, '');;
				  vat.block.submit(function(){return "process_object_name=buCommonPhraseAction"+
			      "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});		      
	}
}

function kweImDetail(){
  var vbCanGridDelete = true;
  var vbCanGridAppend = true;
  var vbCanGridModify = true;
  var allEnableStatus = [["","",false], ["啟用","未啟用"], ["Y","N"]];   

  vat.item.make(vnB_Detail, "indexNo"               , {type:"IDX"  , view:"fixed" , desc:"序號"});
  vat.item.make(vnB_Detail, "lineCode"           , {type:"TEXT" , view:"fixed" , size:15, maxLen:20 , desc:"代碼"	});
	vat.item.make(vnB_Detail, "name"                , {type:"TEXT" , view:"fixed" , size:15, maxLen:50 , desc:"名稱"	});
	vat.item.make(vnB_Detail, "des"                	, {type:"TEXT" , view:"fixed" , size:15, maxLen:20 , desc:"敘述"	});
	vat.item.make(vnB_Detail, "enable"          	, {type:"SELECT" , view:"fixed" , desc:"啟用狀態", init:allEnableStatus});
	//vat.item.make(vnB_Detail, "enable"          	, {type:"TEXT" , view:"fixed" , desc:"啟用狀態"});
	vat.item.make(vnB_Detail, "attribute1"          , {type:"TEXT" , view:""	  , size:15, maxLen:1000 , desc:"屬性一"});
	vat.item.make(vnB_Detail, "attribute2"          , {type:"TEXT" , view:""	  , size:15, maxLen:1000 , desc:"屬性二"});
	vat.item.make(vnB_Detail, "attribute3"          , {type:"TEXT" , view:""	  , size:15, maxLen:200 , desc:"屬性三"});
	vat.item.make(vnB_Detail, "attribute4"          , {type:"TEXT" , view:""	  , size:15, maxLen:200 , desc:"屬性四"});
	vat.item.make(vnB_Detail, "attribute5"          , {type:"TEXT" , view:""	  , size:15, maxLen:200 , desc:"屬性五"});
	vat.item.make(vnB_Detail, "parameter1"          , {type:"TEXT" , view:"shift" , size:15, maxLen:200, desc:"參數一"});
	vat.item.make(vnB_Detail, "parameter2"          , {type:"TEXT" , view:"shift" , size:15, maxLen:200, desc:"參數二"});
	vat.item.make(vnB_Detail, "parameter3"          , {type:"TEXT" , view:"shift" , size:15, maxLen:200, desc:"參數三"});
	vat.item.make(vnB_Detail, "parameter4"          , {type:"TEXT" , view:"shift" , size:15, maxLen:200, desc:"參數四"});
	vat.item.make(vnB_Detail, "parameter5"          , {type:"TEXT" , view:"shift" , size:15, maxLen:200, desc:"參數五"});
	vat.item.make(vnB_Detail, "reserve1"          , {type:"TEXT" , view:"shift"   , size:15, maxLen:200, desc:"保留一"});
	vat.item.make(vnB_Detail, "reserve2"          , {type:"TEXT" , view:"shift"   , size:15, maxLen:200, desc:"保留二"});
	vat.item.make(vnB_Detail, "reserve3"          , {type:"TEXT" , view:"shift"   , size:15, maxLen:200, desc:"保留三"});
	vat.item.make(vnB_Detail, "reserve4"          , {type:"TEXT" , view:"shift"   , size:15, maxLen:200, desc:"保留四"});
	vat.item.make(vnB_Detail, "reserve5"          , {type:"TEXT" , view:"shift"   , size:15, maxLen:200, desc:"保留五"});
	vat.item.make(vnB_Detail, "createdBy"          	, {type:"TEXT" , view:"shift" , mode:"HIDDEN", desc:"產生人員"});
	vat.item.make(vnB_Detail, "creationDate"        , {type:"TEXT" , view:"shift" , mode:"HIDDEN", desc:"產生日期"});
	vat.item.make(vnB_Detail, "lastUpdatedBy"       , {type:"TEXT" , view:"shift" , mode:"HIDDEN", desc:"最後更新人員"});
	vat.item.make(vnB_Detail, "lastUpdateDate"      , {type:"TEXT" , view:"shift" , mode:"HIDDEN", desc:"最後更新日期"});
	//vat.item.make(vnB_Detail, "extend"							, {type:"BUTTON", desc:"進階輸入", value:"進階輸入",  eClick:"advanceInput()"});
	vat.block.pageLayout(vnB_Detail, {
														id: "vatDetailDiv",
														pageSize: 10,
								           				canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "",						
														eventService        : "",   
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
			//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}
// 送出的返回
function createRefreshForm(){
	 vat.item.setValueByName("#L.currentRecord", "0");
     vat.item.setValueByName("#L.maxRecord", "0");
     vat.bean().vatBeanPicker.result = null; 
	refreshForm("");
}
function changeHeadCode(){
//alert(vat.item.getValueByName("#F.headCode"));
	//MACO 只有在變更時使用者輸入供應商編號時自動帶入相關資訊
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
	
	

	if(vat.item.getValueByName("#F.status") == "SAVE")//&&vat.bean().vatBeanOther.category=="change"
	{

		var headCode = vat.item.getValueByName("#F.headCode");
		var processString = "process_object_name=buCommonPhraseService&process_object_method_name=getAJAXFormDataByHeadCode"+
							"&headCode="     + vat.item.getValueByName("#F.headCode") ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){

				var alertMessage ="已有相同類別代碼是否載入資料?";
				if(confirm(alertMessage))
					{
						vat.item.setValueByName("#F.name", vat.ajax.getValue("name", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.description",   vat.ajax.getValue("description", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.enable", vat.ajax.getValue("enable", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve1",      vat.ajax.getValue("reserve1",  	 vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve2",    	vat.ajax.getValue("reserve2",    vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve3", 		vat.ajax.getValue("reserve3",	 vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve4",  	vat.ajax.getValue("reserve4",    vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve5",   	vat.ajax.getValue("reserve5",    vat.ajax.xmlHttp.responseText));
						vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
					}
 
	  		}
		} );
	}
	else{
		var headCode = vat.item.getValueByName("#F.headCode");
		var processString = "process_object_name=buCommonPhraseService&process_object_method_name=getAJAXFormDataByHeadCode"+
							"&headCode="     + vat.item.getValueByName("#F.headCode") ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				if(vat.ajax.getValue("enable", vat.ajax.xmlHttp.responseText)!==""){
				var alertMessage ="已有相同類別代碼是否載入資料?";
				if(confirm(alertMessage))
					{
						vat.item.setValueByName("#F.name", vat.ajax.getValue("name", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.description",   vat.ajax.getValue("description", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.enable",   vat.ajax.getValue("enable", vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve1",      vat.ajax.getValue("reserve1",  	 vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve2",    	vat.ajax.getValue("reserve2",    vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve3", 		vat.ajax.getValue("reserve3",	 vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve4",  	vat.ajax.getValue("reserve4",    vat.ajax.xmlHttp.responseText));
						vat.item.setValueByName("#F.reserve5",   	vat.ajax.getValue("reserve5",    vat.ajax.xmlHttp.responseText));
						vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
					}
				}
	  		}
		} );}
}

function loadBeforeAjxService(){
	var processString = "process_object_name=buCommonPhraseService&process_object_method_name=getAJAXPageData" + 
	                    "&headCode=" + vat.item.getValueByName("#F.headCode")
	return processString;			
}

function saveBeforeAjxService(){
	var processString = "";
		processString = "process_object_name=buCommonPhraseService&process_object_method_name=updateAJAXPageLinesData" + 
		"&headCode=" + vat.item.getValueByName("#F.headCode");
	return processString;
}

function saveSuccessAfter(){

}

function appendBeforeService(){
	return true;
}

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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headCode;
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
	//alert(document.forms[0]["#formId"].value);
    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;  

    vat.block.submit(
        function()
        {
       		 //alert(code);
            return "process_object_name=buCommonPhraseAction&process_object_method_name=performMainInitial"; 
        },
        {
        		other : true , bind: true,
           funcSuccess:function()
           {
                vat.item.bindAll();
                vat.block.pageRefresh(vnB_Detail);
                //changeHeadCode();                
                //doFormAccessControl();
        }}
    );
     
}

// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";

		if(confirm(alertMessage))
		{
			vat.block.pageDataSave(vnB_Detail, 
			{
				funcSuccess:function()
		   		{
				    vat.bean().vatBeanOther.formAction = formAction;
					vat.block.submit(function()
					{
		
						return "process_object_name=buCommonPhraseAction&process_object_method_name=performMainTransaction";
		
					},{bind:true, link:true, other:true,
					funcSuccess: function ()
					{
						//vat.block.pageRefresh(vnB_Detail);

					}});	
				}
			});
		}
}