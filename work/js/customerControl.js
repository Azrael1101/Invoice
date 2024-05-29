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

function stop() {
    window.clearInterval(intervalID);
}


/* 初始化 */
function outlineBlock(){
	formDataInitial();
	headerInitial();

	var status = vat.bean("status");
 	if(typeof vat.tabm != 'undefined') {
 	    vat.tabm.createTab(0, "vatTabSpan", "L", "float"); 
 	    vat.tabm.createButton(0, "xTab2", "明細資料", "vatDetailDiv",   "images/tab_detail_data_dark.gif",   "images/tab_detail_data_light.gif");
	}
	detailInitial();
	
}


/* 畫面欄位資料內容初始化 */
function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#formId"].value != '[binding]'){
        vat.bean().vatBeanOther = 
  	    {brandCode     		: document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode 		: document.forms[0]["#orderTypeCode"].value,
	     formId        		: document.forms[0]["#formId"].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	    };     
        vat.bean.init(function(){
		return "process_object_name=customsControlAction&process_object_method_name=performInitial";
        },{other: true});
    }
}


/* 重新抓取頁面資料 */
function refreshForm(vsHeadId){
	document.forms[0]["#formId"].value    		= vsHeadId;	
	vat.bean().vatBeanOther.brandCode     		= document.forms[0]["#loginBrandCode"].value;	
  	vat.bean().vatBeanOther.loginEmployeeCode  	= document.forms[0]["#loginEmployeeCode"].value;	  
  	vat.bean().vatBeanOther.orderTypeCode 		= document.forms[0]["#orderTypeCode"].value;    
	vat.bean().vatBeanOther.formId        		= document.forms[0]["#formId"].value;
	
	vat.block.submit(
		function(){
			return "process_object_name=customsControlAction&process_object_method_name=performInitial";  
     	},{other: true, 
     	   funcSuccess:function(){
     	   		vat.item.bindAll();
     	   		changeCategoryType();
     	   		closeAmount();
     	       	vat.block.pageDataLoad( vatDetailDiv,  vnCurrentPage = 1);
     	  }}
    );
}


/* 畫出單頭與定義單頭欄位 */
function headerInitial(){
	vat.block.create(vatHeadDiv, {
		id: "vatHeadDiv", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"海關維護作業", rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.BrandCode",          type:"LABEL",  value:"品牌"}]},
	 		{items:[{name:"#F.brandCode",          type:"TEXT",   bind:"brandCode",  size:8,  mode:"HIDDEN"},
	 				{name:"#F.brandName",          type:"TEXT",   bind:"brandName",           mode:"READONLY",  back:false}]},
	 		{items:[{name:"#L.superintendentCode", type:"LABEL",  value:"登入人員<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.superintendentCode", type:"TEXT",   bind:"superintendentCode", size:12,mode:"READONLY", mask:"CCCCCC", onchange:"onChangeSuperintendent()" },
	 				{name:"#B.superintendentCode", type:"PICKER", value:"查詢",  src:"./images/start_node_16.gif",
	 									 			openMode:"open", 
	 									 			service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			serviceAfterPick:function(){doAfterPickerEmp()}},
	 		 		{name:"#F.superintendentName", type:"TEXT",   bind:"superintendentName", size:12, mode:"READONLY"}]},
	 		{items:[{name:"#L.tensec", type:"LABEL",  value:"<font color='red'>※本頁面每10秒刷新※</font>"}]}
	 		]}
	 		],
	 beginService:"",
	 closeService:""			
	});
}

/* 畫出明細與定義明細欄位 */
function detailInitial() {
	var branchCode = vat.bean("branchCode");
	
	var CanGridDelete = true;
	var CanGridAppend = true;
	var CanGridModify = true;		
	
	vat.item.make(vatDetailDiv, "indexNo",             {type:"IDX",   desc:"序號"});
	vat.item.make(vatDetailDiv, "orderTypeCode",       {type:"TEXT",	size:4, desc:"單別", mode:"HIDDEN"});
	vat.item.make(vatDetailDiv, "name",       {type:"TEXT",	size:4, desc:"單別", mode:branchCode == "2"?"READONLY":"HIDDEN"});
	     
	vat.item.make(vatDetailDiv, "status",              {type:"IMAGE", src:"", size:8, desc:"狀態", 
																	eClick: function (x){
																		if (typeof(x.value) === "string"){ 
																			if (x.value === "OFF"){
																				x.value = "ON"
																				x.src="./images/on.gif";
																			}else if (x.value === "ON"){
																				x.value = "OFF"
																				x.src="./images/off.gif";
																			}	
																		}		
																		xx(x,x.value);																   
				          												return false;
																	},
																	bindSuccessAfter:function (y){
																	
																	if (typeof(y.value) === "string"){ 
																		if (y.value === "ON") y.src="./images/on.gif";
																		else if (y.value === "OFF") y.src="./images/off.gif";
																	}		   			 
																	}});
	
	vat.item.make(vatDetailDiv, "allowDate", 			   {type:"DATE", size:8, desc:"日期" });	                                                              
	vat.item.make(vatDetailDiv, "lotNumber",               {type:"TEXT",  size:20, desc:"批次",mode:"READONLY"});	 
	vat.item.make(vatDetailDiv, "sended",               	   {type:"TEXT",   size:20, desc:"已上傳",mode:"READONLY"});	                                    
	vat.item.make(vatDetailDiv, "waitToSend",           {type:"TEXT",  size:20, desc:"未上傳",mode:"READONLY"});              
	
	vat.block.pageLayout( vatDetailDiv, {
			id: "vatDetailDiv",
			pageSize:10, 
			OptselectAll:true,
		    canGridDelete:CanGridDelete,
			canGridAppend:CanGridAppend,
			canGridModify:CanGridModify,
			//appendBeforeService :"appendBeforeService()",
			loadBeforeAjxService:"loadBeforeAjxService()", 
			//saveBeforeAjxService:"saveBeforeAjxService()",
			//saveSuccessAfter    :"saveSuccessAfter()", 
			bindSuccessAfter:"bindSuccessAfter()",
			saveFailureAfter    :""
			});
			
	vat.block.pageDataLoad(vatDetailDiv, vnCurrentPage = 1);
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
	intervalID = window.setInterval(loadBeforeAjxService, 10000);
}

/* 載入LINE資料 */
function loadBeforeAjxService() {
	//alert("loadBeforeAjxService");
	var processString = "process_object_name=custmsControlService&process_object_method_name=updateAJAXPageData" +
						"&headId=" + vat.item.getValueByName("#F.headId") ;
						
	vat.block.pageDataLoad(vatDetailDiv, vnCurrentPage = 1);
	return processString;
}

/* 取得SAVE要執行的JS FUNCTION */
function saveBeforeAjxService() {
	alert("saveBeforeAjxService");
	var processString = "";
	var exchangeRate = 0;
	//if (checkEnableLine()) {
		processString = "process_object_name=custmsControlService&process_object_method_name=updateAJAXPageLinesData" + 
						"&indexNo="        + vat.item.getValueByName("#F.indexNo") + 
						"&status="        + vat.item.getValueByName("#F.status")  ;
	//}
	return processString;
}

function gotoStatus(){
alert("this");
  var status       = vat.bean("status");
  if(status="ON"){
  
  }
  return false;
}


/* 取得存檔成功後要執行的JS FUNCTION */
function saveSuccessAfter() {
	alert("saveSuccessAfter ->" + afterSavePageProcess );
	afterSavePageProcess = "" ;	
}


/* 指定下一個狀態 */
function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == ""){
       	formStatus = "SIGNING";
    }else if(processId != null && processId != ""){
        if(status == "SAVE" || status == "REJECT"){
            formStatus = "SIGNING";
        }else if(approvalResult == "true"){
            formStatus = status;
        }else if(approvalResult == "false"){
            formStatus = "REJECT";
        }
    }
    return formStatus;
}

/* 明細匯出 */
function exportFormData(){
    var headId = vat.item.getValueByName("#F.headId");
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=PO_ITEM" + 
              "&fileType=XLS" + 
              "&processObjectName=poPurchaseOrderHeadMainService" + 
              "&processObjectMethodName=findByIdForExport" + 
              "&gridFieldName=poPurchaseOrderLines" + 
              "&arguments=" + headId + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '採購單明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

/* 明細匯入 */
function importFormData(){
    var headId = vat.item.getValueByName("#F.headId");
    var exchangeRate = vat.item.getValueByName("#F.exchangeRate");
	var suffix = 
			"&importBeanName=PO_ITEM" +
			"&importFileType=XLS" +
        	"&processObjectName=poPurchaseOrderHeadMainService" + 
        	"&processObjectMethodName=executeImportPoLists" +
        	"&arguments=" + headId + "{$}" + exchangeRate + 
        	"&parameterTypes=LONG{$}DOUBLE" + 
	        "&blockId=3";
		return suffix;
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


/* 查詢檢視後 */
function doAfterPickerProcess(){
    if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    //alert(vat.bean().vatBeanPicker.result.length);
	    if( vsMaxSize == 0){
	  	    vat.bean().vatBeanOther.firstRecordNumber   = 0;
		    vat.bean().vatBeanOther.lastRecordNumber    = 0;
		    vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		    vat.bean().vatBeanOther.firstRecordNumber   = 1;
		    vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		 
		    refreshForm(vsHeadId);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}


/* 確定建立新資料 */
function createRefreshForm(){
    if(confirm("是否確定建立新資料？")){
        vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;
    	refreshForm("");
	 }
}

/* 報表列印 */
function listReport(){
	var processString;
		processString = "process_object_name=imReceiveHeadMainService&process_object_method_name=getReportURL" + 
							"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
							"&brandCode="     + vat.item.getValueByName("#F.brandCode") +
							"&employeeCode="  + vat.bean("employeeCode") +
							"&orderNo="       + vat.item.getValueByName("#F.orderNo")+
							"&reportName=po0102.rpt";

	vat.ajax.startRequest(processString,  function() {
		if (vat.ajax.handleState()){
			var returnURL = vat.ajax.getValue("returnURL", vat.ajax.xmlHttp.responseText);
			returnURL = returnURL.substring(0,returnURL.indexOf("','"));
			window.open(returnURL);
  			}
	 } );
}

/* 報表列印 */
function openReportWindow(type){ 
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
		vat.block.submit(
					function(){return "process_object_name=poPurchaseOrderHeadMainService"+
								"&process_object_method_name=getReportConfig";},{other:true,
                    			funcSuccess:function(){
								eval(vat.bean().vatBeanOther.reportUrl);
					}}
		);   
     if("AFTER_SUBMIT"==type) refreshForm();//因為調撥單在送出後要直接列印報表，所以要有這行
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

/* 頁面顯示控制 */
function doFormAccessControl(){
	var processId 		= document.forms[0]["#processId"].value;
	var orderNo       	= vat.item.getValueByName("#F.orderNo");
    var formStatus    	= vat.item.getValueByName("#F.status");
    var brandCode    	= vat.item.getValueByName("#F.brandCode");
    var allowApproval 	= document.forms[0]["#allowApproval"].value;
    var canBeMod 		= document.forms[0]["#canBeMod"].value; 
    
    if(allowApproval == "N"){
    	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
    }
    
    if(vat.item.getValueByName("#F.orderNo").indexOf("TMP")){
    	vat.item.setAttributeByName("#F.createdBy", "readOnly", true);
    }
    
	if( vat.item.getValueByName("#F.status")=="SAVE" && vat.item.getValueByName("#F.orderNo").indexOf("TMP") != -1){
		vat.tabm.displayToggle(0, "xTab4", false);
	}else{
		vat.tabm.displayToggle(0, "xTab4", true, false, false);
     	refreshWfParameter( vat.item.getValueByName("#F.brandCode"), 
     		   				vat.item.getValueByName("#F.orderTypeCode"),
     		   				vat.item.getValueByName("#F.orderNo" ) );
		vat.block.pageDataLoad(102, vnCurrentPage = 1); 
	}
	
	if(processId != ""){
		vat.item.setStyleByName("#B.new",         "display", "none");
		vat.item.setStyleByName("#B.search",      "display", "none");
		if(formStatus!="SAVE" && formStatus!="REJECT"){
			vat.item.setAttributeByName("vatHeadDiv", "readOnly", true, true, true);
			vat.item.setAttributeByName("vatMasterDiv",  "readOnly", true, true, true);
			vat.item.setStyleByName("#B.submitBG"	, "display", "none");	
			vat.item.setStyleByName("#B.save"		, "display", "none");
			vat.item.setStyleByName("#B.void"		, "display", "none");
			vat.item.setStyleByName("#B.import"		, "display", "none");
			vat.item.setStyleByName("#B.message"	, "display", "none");
			vat.block.canGridModify( [vatDetailDiv], false, false, false );
			if(formStatus=="" || formStatus=="VOID" || formStatus=="CLOSE" ){
				vat.item.setStyleByName("#B.submit"	, "display", "none");
			}
		}
	}else{
	//除了TMP的SAVE單可以修改，其餘一率唯讀
		if(formStatus=="SAVE" && orderNo.indexOf("TMP") != -1){
			vat.item.setAttributeByName("vatHeadDiv", "readOnly", false, true, true);
			vat.item.setAttributeByName("vatMasterDiv",  "readOnly", false, true, true);
			vat.item.setStyleByName("#B.void"		, "display", "none");
			vat.item.setStyleByName("#B.submit"		, "display", "inline");
			vat.item.setStyleByName("#B.submitBG"	, "display", "inline");	
			vat.item.setStyleByName("#B.save"		, "display", "inline");
			vat.item.setStyleByName("#B.message"	, "display", "inline");
			vat.item.setStyleByName("#B.import"		, "display", "inline");
			vat.block.canGridModify( [vatDetailDiv], true, true, true );
		}else{
			vat.item.setAttributeByName("vatHeadDiv", "readOnly", true, true, true);
			vat.item.setAttributeByName("vatMasterDiv",  "readOnly", true, true, true);
			vat.item.setStyleByName("#B.submit"		, "display", "none");
			vat.item.setStyleByName("#B.submitBG"	, "display", "none");	
			vat.item.setStyleByName("#B.save"		, "display", "none");
			vat.item.setStyleByName("#B.void"		, "display", "none");
			vat.item.setStyleByName("#B.message"	, "display", "none");
			vat.item.setStyleByName("#B.import"		, "display", "none");
			vat.block.canGridModify( [vatDetailDiv], false, false, false);
		}
	}
	
	if(formStatus=="FINISH"&&canBeMod=="Y"){
		vat.item.setStyleByName("#B.close"		, "display", "inline");
		vat.block.canGridModify( [vatDetailDiv], true, false, false);
		vat.item.setGridAttributeByName("itemCode", "readOnly", true);
		vat.item.setGridAttributeByName("quantity", "readOnly", true);
		vat.item.setGridAttributeByName("foreignUnitCost", "readOnly", true);
		//vat.item.setGridAttributeByName("actualPurchaseQuantity", "readOnly", true);
		vat.item.setGridAttributeByName("detailClose", "disabled", false);
	}else{
		vat.item.setStyleByName("#B.close"		, "display", "none");
		vat.item.setGridAttributeByName("itemCode", "readOnly", false);
		vat.item.setGridAttributeByName("quantity", "readOnly", false);
		vat.item.setGridAttributeByName("foreignUnitCost", "readOnly", false);
		//vat.item.setGridAttributeByName("actualPurchaseQuantity", "readOnly", false);
		vat.item.setGridAttributeByName("detailClose", "disabled", true);
	}
	
    vat.block.pageRefresh(vatDetailDiv); 
}

/* 匯入成功後 */
function afterImportSuccess(){
	refreshHeadData();
}

/* 匯入成功後 */
function refreshHeadData(){	
	changeSupplierCode();
}


function xx(x,status){
    var str = x.id;
    //alert(str+",,,,,,"+str.substring(10,11));
    var vatDetailD = str.substring(10,11);
    var orderTypeCode = vat.item.getGridValueByName("orderTypeCode", vatDetailD);
    //alert(orderTypeCode);
    //str = str.replace('X3', 'X2');
    //alert(str);
    //otc = document.getElementById(str);
    //alert(otc.val);
    loginEmployeeCode  = document.forms[0]["#loginEmployeeCode"].value
    //alert(orderTypeCode+ ""+ status);
    vat.block.submit(function () {
            return "process_object_name=custmsControlService&process_object_method_name=updateperformTransaction&orderTypeCode="+orderTypeCode
            +"&status="+status + "&loginEmployeeCode="+loginEmployeeCode;
        }, {
            //other: true,
            funcSuccess: function () {
               
                
            }
        });
}