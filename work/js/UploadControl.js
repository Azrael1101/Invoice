vat.debug.disable();
var afterSavePageProcess = "";

var vnB_Button		= 0;
var vatHeadDiv		= 1;
var vatMasterDiv	= 2; 
var vatDetailDiv	= 3;
var vatAmountDiv	= 4;
var vatApprovalDiv	= 5; 
var PurchaseCurrencyCode = "";
var intervalID;
var intervalID2;


/*
ROLE:
==============================
Department	|	value	|
============|===========|
 反確者		| SU		|
 商控		| BC		|
 查核		| AC		|
 入提		| ED		|
  副理		| MA		|
==============================
}*/

/* 初始化 */
function outlineBlock(){
	formDataInitial();
	headerInitial();
	if(document.forms[0]["#department"].value==="SU"||document.forms[0]["#department"].value==="MA")
	{
		buttonLine();
	}
	var status = vat.bean("status");
 	if(typeof vat.tabm != 'undefined')
	{
 	    vat.tabm.createTab(0, "vatTabSpan", "L", "float"); 
		vat.tabm.createButton(0, "xTab2", "明細資料", "vatDetailDiv",   "images/tab_detail_data_dark.gif",   "images/tab_detail_data_light.gif");
	}

	detailInitial();

}
function buttonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"     			 , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"         			 , type:"LABEL"    , value:"　"},
	 			{name:"#B.undoAC"     			 , type:"IMG"      , value:"查核反確認",  src:"./images/button_ac_undo.jpg"  , eClick:"unDo('1')"},
	 			{name:"SPACE"         			 , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.undoED"     			 , type:"IMG"      , value:"入提反確認",   src:"./images/button_ed_undo.jpg"  , eClick:"unDo('2')"},
	 	 		{name:"SPACE"          			 , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.deliverySwitch"     	 , type:"IMG"      , value:"入提開關",   src:"./images/deliverySwitch.jpg"  ,mode:"MA" != document.forms[0]["#department"].value ? "HIDDEN" : "", eClick:"deliverySwitch()"},
	 	 		{name:"SPACE"          			 , type:"LABEL"    , value:"　"},

				
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"}
	 			],
	 			 td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

/* 畫面欄位資料內容初始化 */
function formDataInitial()
{
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#formId"].value != '[binding]'){
        vat.bean().vatBeanOther = 
        {
			brandCode			: document.forms[0]["#loginBrandCode"].value,   	
			loginEmployeeCode	: document.forms[0]["#loginEmployeeCode"].value,	  
			orderTypeCode		: document.forms[0]["#orderTypeCode"].value,
			formId				: document.forms[0]["#formId"].value,
			department			: document.forms[0]["#department"].value,
			customsWarehouseCode: document.forms[0]["#customsWarehouseCode"].value,
	     	currentRecordNumber: 0,
	     	lastRecordNumber   : 0
	    };     
        vat.bean.init(function()
        {
			return "process_object_name=uploadControlAction&process_object_method_name=performInitial";
        },
        {
        	other: true
        });
    }
}

/* 畫出單頭與定義單頭欄位 */
function headerInitial()
{	
	var allBatch = vat.bean("allBatch");
	var role = document.forms[0]["#department"].value;
	vat.block.create(vatHeadDiv,
	{
		id: "vatHeadDiv", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"海關維護作業",
		rows:
		[{
			row_style:"",
			cols:
			[
				{items:[{name:"#F.department",type:"TEXT",   bind:"department",  size:8,  mode:"READONLY"}]},
	 			{items:[{name:"#L.schedual",type:"LABEL",  value:"班次"}]},
	 			//{items:[{name:"#F.schedual",type:"TEXT",   bind:"schedual",  size:8,  mode:"READONLY"}]},
	 			{items:[{name:"#F.schedual",type:"SELECT",   bind:"schedual",  size:8 ,init:allBatch,mode:"SU" != role ? "READONLY" : ""}]},
	 			
	 			{items:[{name:"#L.schedualDate",type:"LABEL",  value:"日期"}]},
	 			//{items:[{name:"#F.schedualDate",type:"DATE",   bind:"schedualDate",  size:8,  mode:"READONLY"}]},
	 			{items:[{name:"#F.schedualDate",type:"DATE",   bind:"schedualDate",  size:8 ,mode:"SU" != role ? "READONLY" : ""}]},
	 			{items:[{name:"#L.deliverySwitch"		 , type:"LABEL"		,value:"提貨上傳開啟", size:10 }]},
	 	 		{items:[{name:"#F.deliverySwitch"		 , type:"CHECKBOX",  bind:"deliverySwitch",  mode:"READONLY" }]},
	 	 		{items:[{name:"#L.loginEmployeeCode",type:"LABEL",  value:"登入人員"}]},
	 	 		{items:[{name:"#F.loginEmployeeCode",type:"TEXT",   bind:"loginEmployeeCode",  size:8,  mode:"READONLY"}]}
	 		]
	 	}],
	 beginService:"",
	 closeService:""			
	});
}

function controler(flag)
{
	var nItemLine = vat.item.getGridLine();
	var vStatusByAC = vat.item.getGridValueByName("statusByAC"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vStatusByED = vat.item.getGridValueByName("statusByED"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vStatusByBC = vat.item.getGridValueByName("statusByBC"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vShedualDate = vat.item.getGridValueByName("schedualDate", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vSchedual = vat.item.getGridValueByName("schedual"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vOrderTypeCode = vat.item.getGridValueByName("orderTypeCode"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var deliverySwitch = vat.item.getValueByName("#F.deliverySwitch");

	var alertMessage ="是否確定送出?";
	if(confirm(alertMessage))
	{
			$.blockUI({
	        message: '<font size=4 color="#000000"><b>執行中請稍後...</b></font>',
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
		if(flag === 1&&document.forms[0]["#department"].value == "AC")
		{
			if(vStatusByAC === "OFF")
			{
				vStatusByAC = "ON";
				vat.item.setGridValueByName("statusByAC", nItemLine, "ON");
				//alert("開啟本班次開關");
				controlClick(flag);
	
				vat.ajax.XHRequest({
				post:"process_object_name=uploadControlService&process_object_method_name=updateStatus"+
		                  "&schedual=" +  vSchedual +
		                  "&schedualDate=" + vShedualDate +
		                  "&statusByBC=" +  vStatusByBC +
		                  "&statusByAC=" +  vStatusByAC +
		                  "&statusByED=" +  vStatusByED +
		                  "&orderTypeCode=" +  vOrderTypeCode +
		                  "&department=" +  vat.item.getValueByName("#F.department") +
		                  "&loginEmployeeCode=" +  vat.item.getValueByName("#F.loginEmployeeCode") +
		                  "&flag=" + flag,
		
		
				find: function change(oXHR){
						$.unblockUI();
		         		vat.block.pageRefresh(vatDetailDiv);
				},
				fail: function changeError(){
				$.unblockUI();
				}  
		       });
	       	}
			else if(vStatusByAC === "ON")
			{
				$.unblockUI();
				//vStatusByAC = "OFF";
				//vat.item.setGridValueByName("statusByAC", nItemLine, "OFF");
				//alert("關閉本班次開關");
				//controlClick(flag);
			}
			else
			{
				$.unblockUI();
				alert("無資料");
			}
			
		}
		else if(flag ===2&&document.forms[0]["#department"].value == "ED")
		{
				if(vStatusByED === "OFF")
				{
					vStatusByED = "ON";
					vat.item.setGridValueByName("statusByED", nItemLine, "ON");
					//alert("開啟本班次開關");
					controlClick(flag);
		
					vat.ajax.XHRequest({
						post:"process_object_name=uploadControlService&process_object_method_name=updateStatus"+
				                  "&schedual=" +  vSchedual +
				                  "&schedualDate=" + vShedualDate +
				                  "&statusByBC=" +  vStatusByBC +
				                  "&statusByAC=" +  vStatusByAC +
				                  "&statusByED=" +  vStatusByED +
				                  "&orderTypeCode=" +  vOrderTypeCode +
				                  "&department=" +  vat.item.getValueByName("#F.department") +
				                  "&loginEmployeeCode=" +  vat.item.getValueByName("#F.loginEmployeeCode") +
				                  "&flag=" + flag,
				
				
						find: function change(oXHR){
								$.unblockUI();
				         		vat.block.pageRefresh(vatDetailDiv);
						},
						fail: function changeError(){
							$.unblockUI();
						}  
		       		});
				}
			else if(vStatusByED === "ON")
			{
				$.unblockUI();
				//vStatusByED = "OFF";
				//vat.item.setGridValueByName("statusByED", nItemLine, "OFF");
				//alert("關閉本班次開關");
				//controlClick(flag);
			}
			else
			{
				$.unblockUI();
				alert("無資料");
			}
		}
		else if(flag ===3&&document.forms[0]["#department"].value == "BC")
		{
			//alert(vOrderTypeCode.indexOf("SOP"));
			if((vStatusByAC === "ON" && (vStatusByED === "ON" || deliverySwitch ==="N")) || vOrderTypeCode.indexOf("SOP")<=-1 )
			{
				if (vStatusByBC === "OFF")
				{
					vStatusByBC = "ON";
					vat.item.setGridValueByName("statusByBC", nItemLine, "ON");
					//alert("開啟本班次開關");
					//showUploadMessage();
					vat.ajax.XHRequest({
						post:"process_object_name=uploadControlService&process_object_method_name=updateperformTransaction"+
								  "&brandCode=" + document.forms[0]["#loginBrandCode"].value +
				                  "&schedual=" +  vSchedual +
				                  "&department=" + vat.item.getValueByName("#F.department") +
				                  "&schedualDate=" + vShedualDate +
				                  "&statusByBC=" +  vStatusByBC +
				                  "&statusByAC=" +  vStatusByAC +
				                  "&statusByED=" +  vStatusByED +
				                  "&orderTypeCode=" +  vOrderTypeCode +
				                  "&department=" +  vat.item.getValueByName("#F.department") +
				                  "&loginEmployeeCode=" +  vat.item.getValueByName("#F.loginEmployeeCode") +
				                  "&flag=" + flag,
				
				
						find: function change(oXHR){
							var message = vat.ajax.getValue("message" , oXHR.responseText);
							$.unblockUI();
							vat.block.pageRefresh(vatDetailDiv);
						},
						fail: function changeError(oXHR){
							var message = vat.ajax.getValue("message" , oXHR.responseText);
							$.unblockUI();
						}  
		       		});
							//alert(statusControl.value);
							//statusControl.src="./images/button_off.png";
				}
			}
			else
			{
					$.unblockUI();
					alert("查核/入提尚未確認，無法上傳");
			}
		}
		else
		{
			$.unblockUI();
			alert("你無權使用此開關");
		}
	}

}
function controlClick(flag) {

	//alert(flag);

       

}
//	訊息提示
function showUploadMessage(){
	var width = "300";
    var height = "200";
	var returnData = window.open(
		"/erp/jsp/UploadShowProgramLog.jsp" +
		"?programId=Upload" +
		"&levelType=ERROR" +
		"&identification=IslandOrInternationUpload" +
        "&processObjectName=imMovementMainService" +
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}



/* 畫出明細與定義明細欄位 */
function detailInitial()
{

	var branchCode = vat.bean("branchCode");
	var department = vat.bean("department");
	var CanGridDelete = true;
	var CanGridAppend = true;
	var CanGridModify = true;

	vat.item.make(vatDetailDiv, "indexNo", {type:"IDX",   desc:"序號"});
	vat.item.make(vatDetailDiv, "schedualDate",{type:"DATE",  desc:"日期", mode:"READONLY" });  
	vat.item.make(vatDetailDiv, "schedual", {type:"TEXT", desc:"班次", mode:"READONLY"});
	vat.item.make(vatDetailDiv, "orderTypeCode", {type:"TEXT", desc:"單別", mode:"READONLY"});
	vat.item.make(vatDetailDiv, "orderAmount",{type:"TEXT",   desc:"等待上傳單據",mode:"READONLY"});
	

	
	if(document.forms[0]["#department"].value==="BC"||document.forms[0]["#department"].value==="SU"||document.forms[0]["#department"].value==="AC")
	{
		vat.item.make(vatDetailDiv, "statusByAC",{type:"TEXT",   desc:"查核確認",mode:"READONLY"});
		vat.item.make(vatDetailDiv, "ACcheck",{type:"BUTTON",   desc:"查核確認", value:"確認",eClick:'controler(1)'});
		vat.item.make(vatDetailDiv, "lastUpdatByAC",{type:"TEXT",   desc:"查核最後執行人",mode:"READONLY"});
	}
	else
	{
		vat.item.make(vatDetailDiv, "statusByAC",{type:"TEXT",   desc:"查核確認",mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "ACcheck",{type:"BUTTON",   desc:"查核確認",mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "lastUpdatByAC",{type:"TEXT",   desc:"查核最後執行人",mode:"HIDDEN"});
	}
	if(vat.item.getValueByName("#F.deliverySwitch") === "Y"){
		if(document.forms[0]["#department"].value==="BC"||document.forms[0]["#department"].value==="SU"||document.forms[0]["#department"].value==="ED")
		{
			vat.item.make(vatDetailDiv, "statusByED",{type:"TEXT",   desc:"入提確認",mode:"READONLY"});
			vat.item.make(vatDetailDiv, "EDcheck",{type:"BUTTON",   desc:"入提確認", value:"確認",eClick:'controler(2)'});
			vat.item.make(vatDetailDiv, "lastUpdatByED",{type:"TEXT",   desc:"入提最後執行人",mode:"READONLY"});
		}
		else
		{
			vat.item.make(vatDetailDiv, "statusByED",{type:"TEXT",   desc:"入提確認",mode:"HIDDEN"});
			vat.item.make(vatDetailDiv, "EDcheck",{type:"BUTTON",   desc:"入提確認",mode:"HIDDEN"});
			vat.item.make(vatDetailDiv, "lastUpdatByED",{type:"TEXT",   desc:"入提最後執行人",mode:"HIDDEN"});
		}
	}else{
		
		vat.item.make(vatDetailDiv, "statusByED",{type:"TEXT",   desc:"入提確認",mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "EDcheck",{type:"BUTTON",   desc:"入提確認",mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "lastUpdatByED",{type:"TEXT",   desc:"入提最後執行人",mode:"HIDDEN"});
		
	}
	if(document.forms[0]["#department"].value==="BC"||document.forms[0]["#department"].value==="SU")
	{
		vat.item.make(vatDetailDiv, "statusByBC",{type:"TEXT",   desc:"商控確認",mode:"READONLY"});
		vat.item.make(vatDetailDiv, "BCcheck",{type:"BUTTON",   desc:"商控確認", value:"確認",eClick:'controler(3)'});
		vat.item.make(vatDetailDiv, "lastUpdatedByBC",{type:"TEXT",   desc:"商控最後執行人",mode:"READONLY"});

	}
	else
	{
		vat.item.make(vatDetailDiv, "statusByBC",{type:"TEXT",   desc:"商控確認",mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "BCcheck",{type:"BUTTON",   desc:"商控確認", value:"確認",mode:"HIDDEN"});
		vat.item.make(vatDetailDiv, "lastUpdatedByBC",{type:"TEXT",   desc:"商控最後執行人",mode:"HIDDEN"});
	}

	vat.block.pageLayout( vatDetailDiv,{
										id: "vatDetailDiv",
										pageSize:10, 
										indexType	: "AUTO",
										OptselectAll:true,
										canGridDelete:CanGridDelete,
										canGridAppend:CanGridAppend,
										canGridModify:CanGridModify,
										//appendBeforeService :"appendBeforeService()",
										loadBeforeAjxService:"loadBeforeAjxService()",
										saveBeforeAjxService:"saveBeforeAjxService()",
										//saveSuccessAfter    :"saveSuccessAfter()", 
										bindSuccessAfter:"bindSuccessAfter()"
										//saveFailureAfter    :""
									});
	vat.block.pageDataLoad(vatDetailDiv, vnCurrentPage = 1);
}

function xx(statusControl,status)
{

}
function refreshForm()
{

  

    vat.block.submit(
        function()
        {
            return "process_object_name=uploadControlAction&process_object_method_name=performInitial";
        },
        {
        		other      : true, 
           funcSuccess:function()
           {
                vat.item.bindAll();
                vat.block.pageRefresh(vatDetailDiv); 
        }}
    );
     
}
function statusControl(department){

}

function saveBeforeAjxService() {
	//alert("saveBeforeAjxService取得佔存");
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=uploadControlService&process_object_method_name=saveSearchResult";
	}
	return processString;
}












/* 判斷是否要關閉LINE */
function checkEnableLine() {
	return true ;
}

/* 開放新增明細 */
function appendBeforeService(){
	return true;
}

window.onbeforeunload = function () {
	stop();
}

function bindSuccessAfter() {
	//intervalID = window.setInterval(loadBeforeAjxService, 10000);
}

/* 載入LINE資料 */
function loadBeforeAjxService() {
	var processString = "process_object_name=uploadControlService&process_object_method_name=getAJAXPageData"
						+"&schedualDate="+ vat.item.getValueByName("#F.schedualDate") 
						+"&schedual=" + vat.item.getValueByName("#F.schedual")
						+"&department="+vat.item.getValueByName("#F.department")
						+"&brandCode=" +document.forms[0]["#loginBrandCode"].value
						+"&loginEmployeeCode=" + vat.item.getValueByName("#F.loginEmployeeCode")
						+"&customsWarehouseCode=" + document.forms[0]["#customsWarehouseCode"].value ;
						
	vat.block.pageDataLoad(vatDetailDiv, vnCurrentPage = 1);
	return processString;
}

function gotoStatus(){
//alert("this");
  var status       = vat.bean("status");
  if(status="ON"){
  
  }
  return false;
}


/* 取得存檔成功後要執行的JS FUNCTION */
function saveSuccessAfter() {
	//alert("saveSuccessAfter ->" + afterSavePageProcess );
	afterSavePageProcess = "" ;	
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



/* 匯入成功後 */
function afterImportSuccess(){
	refreshHeadData();
}

/* 匯入成功後 */
function refreshHeadData(){	
	changeSupplierCode();
}


function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vatDetailDiv , vnCurrentPage = 1);}
			                    });
}



/*function unDo(flag)
{
	if(document.forms[0]["#department"].value==="SU") 
	{
	  vat.block.submit(
        function()
        {
            return "process_object_name=uploadControlService&process_object_method_name=updateStatusTransaction";
        },
        {
        		other      : true, 
           funcSuccess:function()
           {
                vat.item.bindAll();
                vat.block.pageRefresh(vatDetailDiv); 
        }}
    );
	
	
	
	

	}
	else
	{
		alert("您無權使用該按鈕");
	}
}*/
function unDo(flag){

	if(document.forms[0]["#department"].value==="SU") 
	{
	   	var alertMessage ="是否確定送出?";
		if(confirm(alertMessage))
		{
		   vat.ajax.XHRequest({
		          asyn:false,
		          post:"process_object_name=uploadControlService"+
		                   "&process_object_method_name=updateStatusTransaction"
		                    +"&flag="+ flag
							+"&schedual="+ vat.item.getValueByName("#F.schedual")
							+"&schedualDate="+ vat.item.getValueByName("#F.schedualDate")
							+"&loginEmployeeCode="+ vat.item.getValueByName("#F.loginEmployeeCode"),
		          find: function change(oXHR){ 
		          		//alert("test");
		          	alert("反確認已完成");
		          	vat.block.pageRefresh(vatDetailDiv);
		          } 
			});	
		}
	}
	else
	{
		alert("您無權使用該按鈕");
	}
}
function deliverySwitch(){

	var deliverySwitch =  vat.item.getValueByName("#F.deliverySwitch");

	if(document.forms[0]["#department"].value==="MA") 
	{
	   	var alertMessage ="是否確定送出?";
		
		if(deliverySwitch==="Y")
	   	{
	   		alert("入提確認關閉");
	   		deliverySwitch="N";
	   		vat.item.setValueByName("#F.deliverySwitch","N");
	   	}
	   	else
	   	{
	   		alert("入提確認開啟");
	   		deliverySwitch="Y";
	   		vat.item.setValueByName("#F.deliverySwitch","Y");
	   	}
		
		if(confirm(alertMessage))
		{
		   vat.ajax.XHRequest({
		          asyn:false,
		          post:"process_object_name=uploadControlService"+
		                   "&process_object_method_name=updateDeliverySwitch"
							+"&deliverySwitch="+deliverySwitch
							+"&loginEmployeeCode="+ vat.item.getValueByName("#F.loginEmployeeCode"),
		          find: function change(oXHR){ 
		          		//alert("test");
		          	
		          	//vat.block.pageRefresh(vatDetailDiv);
		          } 
			});	
		}
		
	}
	else
	{
		alert("您無權使用");
	}
}