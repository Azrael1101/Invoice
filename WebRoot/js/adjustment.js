function checkWareHouseNames(){
	var isOk= true;
	var wareHouseNames = $('div[@id^=wareHouseNames]');
	for(var j = 0; j< wareHouseNames.length; j++){
		if('查無庫號' == wareHouseNames[j].innerText){
			isOk = false;
		}
	}
	if(!isOk){
		alert('庫號輸入有誤，請重新輸入');
	}
	return isOk;
}
function kweAdjustmentBlock(){
  kweAdjustmentInitial();
  headerInitial();
  
  if (typeof vat.tabm != 'undefined') {
	 vat.tabm.createTab(0, "vatTabSpan", "H", "float");

  vat.tabm.createButton(0,"xTab1","商品資料"   ,"vatItemDiv"                   ,"images/tab_item_data_dark.gif"            ,"images/tab_item_data_light.gif" , false, "doPageRefresh()");
  vat.tabm.createButton(0,"xTab2","簽核資料"   ,"vatApprovalDiv"             ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif", vat.item.getValueByName("#F.status") == "SAVE" || vat.item.getValueByName("#F.status") == "UNCONFIRMED" ? "none" : "inline");
  vat.tabm.createButton(0,"xTab3","動態加會簽","vatDynamicApprovalDiv","images/tab_dynamic_approval_dark.gif","images/tab_dynamic_approval_light.gif", "none");
}
  detailInitialPAP();
  kweWfBlock()

}
function kweAdjustmentInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   
  	 loginBrandCode	    = document.forms[0]["#loginBrandCode"    ].value;  	
  	 loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" ].value;	  
  	 orderTypeCode      = document.forms[0]["#orderTypeCode"     ].value; 
     processId          = document.forms[0]["#processId"         ].value;       
	 formId             = document.forms[0]["#formId"            ].value;
	 assignmentId       = document.forms[0]["#assignmentId"      ].value;
	 vat.bean().vatBeanOther={
	 					loginBrandCode:loginBrandCode,					
					  	processId : processId,
					  	loginEmployeeCode : loginEmployeeCode,
					  	orderTypeCode:orderTypeCode,
					    formId :formId,
					    assignmentId : assignmentId
	 }	
	
     vat.bean.init(function(){
		return "process_object_name=imPriceAdjustmentService&process_object_method_name=executeInitial"; 
     },{other: true});
  }
  
}
function detailInitialPAP() {//商品訂價
	var statusTmp = vat.item.getValueByName("#F.status");
	var orderType = vat.item.getValueByName("#F.orderTypeCode");
	
	var modeType="mode";
	if(""==orderType){
		orderType="PAP";
	}
	
	var varCanDataDelete = false;
	var varCanDataAppend = false;
	var varCanDataModify = false;
	if( statusTmp == "SAVE" || statusTmp == "REJECT" || statusTmp == "UNCONFIRMED" ){
		varCanDataDelete = true;
		varCanDataAppend = true;
		varCanDataModify = true;		
	}
	

	vat.item.make(1, "indexNo", {type:"IDX", desc:"序號"}); 
	if(orderType=="PAP"){
		vat.item.make(1, "category01", {type:"TEXT", size:20, maxLen:20, desc:"系列", mode:"READONLY"});
		vat.item.make(1, "itemCode", {type:"TEXT", size:16, maxLen:20, desc:"品號", onchange:"onChangeItemCode()"});//    
		vat.item.make(1, "itemCName", {type:"TEXT", size:20, maxLen:20, desc:"中文名稱", mode:"READONLY"});                                   
		vat.item.make(1, "foreignCost", {type:"NUMB", size:8, maxLen:20, desc:"原幣成本", mode:"READONLY"}); 
		vat.item.make(1, "localCost", {type:"NUMB", size:8, maxLen:20, desc:"台幣成本",onchange:"calculateLineRate()"});//   
		vat.item.make(1, "unitPrice", {type:"NUMB", size:8, maxLen:20, desc:"送簽價格", onchange:"calculateLineRate()"});//
		vat.item.make(1, "costRate", {type:"NUMB", size:8, maxLen:20, desc:"成本率", mode:"READONLY"});
		vat.item.make(1, "priceId", {type:"NUMB", size:8, maxLen:20, desc:"ID", mode:"READONLY"});
	}else{
		vat.item.make(1, "itemCode", {type:"TEXT", size:16, maxLen:20, desc:"品號", onchange:"onChangeItemCode()"});    
		vat.item.make(1, "itemCName", {type:"TEXT", size:20, maxLen:20, desc:"中文名稱", mode:"READONLY"});                                   
		vat.item.make(1, "originalQuotationPrice", {type:"NUMB", size:8, maxLen:20, desc:"廠商報價(原)", mode:"READONLY"}); 
		vat.item.make(1, "newQuotationPrice", {type:"NUMB", size:8, maxLen:20, desc:"廠商報價(新)",onchange:"calculateLineRate()"});  
		vat.item.make(1, "originalPrice", {type:"NUMB", size:8, maxLen:20, desc:"原價", mode:"READONLY"});   
		vat.item.make(1, "unitPrice", {type:"NUMB", size:8, maxLen:20, desc:"送簽價格", onchange:"calculateLineRate()"});
		vat.item.make(1, "grossProfitRate", {type:"NUMB", size:8, maxLen:20, desc:"毛利率差異", mode:"READONLY"});
	}
	                                   
	vat.item.make(1, "lineId", {type:"ROWID"});
	vat.item.make(1, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});                                         
	//vat.item.make(1, "isDeleteRecord", {type:"DEL", desc:"刪除"});                                                          
	vat.item.make(1, "message", {type:"MSG", desc:"訊息"});                                                                  
                                                        
	vat.block.pageLayout(1, {	id: "vatItemDiv",
								pageSize: 10,
								searchKey           : [""],
								selectionType       : "NONE",
								indexType           : "NONE",
	                            canGridDelete:varCanDataDelete,
								canGridAppend:varCanDataAppend,
								canGridModify:varCanDataModify,														
							    beginService: "",
								closeService: "",
							    itemMouseinService  : "",
								itemMouseoutService : "",
							    appendBeforeService : "kwePageAppendBeforeMethod()",
							    appendAfterService  : "kwePageAppendAfterMethod()",
								deleteBeforeService : "",
								deleteAfterService  : "",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "kwePageLoadSuccess()",
								loadFailureAfter    : "",
								eventService        : "changeRelationData",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()",
								saveFailureAfter    : ""
														});
	vat.block.pageDataLoad(1, vnCurrentPage = 1);
	/**/
}
function headerInitial(){
var allOrderTypes = vat.bean("allOrderTypes");
var allPriceTypes = vat.bean("allPriceTypes");
var allCurrencys = vat.bean("allCurrencys");
// 
vat.block.create(vnB_Header = 0, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"商品變價/新品訂價維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L_orderType", type:"LABEL" , value:"單別"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode",init:allOrderTypes}]},		 
	 {items:[{name:"#L_orderNo"  , type:"LABEL" , value:"單號"},
	 		{name:"#formId"   , type:"TEXT"  ,  bind:"formId", back:false, size:10, mode:"HIDDEN" }]},
	 {items:[{name:"#F.orderNo"  , type:"TEXT"  ,  bind:"orderNo", size:20, mode:"READONLY"}
	 		,{name:"#F.headId"   , type:"TEXT"  ,  bind:"headId", back:false, size:10, mode:"HIDDEN" }]},
	 {items:[{name:"#L_enableDate", type:"LABEL",  value:"啟用日期"}]},
	 {items:[{name:"#F.enableDate", type:"DATE",  bind:"enableDate", size:1}]},
	 {items:[{name:"#L_priceType", type:"LABEL",  value:"價格類型"}]},
	 {items:[{name:"#F.priceType", type:"SELECT",  bind:"priceType", size:1,init:allPriceTypes}]},		 
	 {items:[{name:"#L_brandCode", type:"LABEL" , value:"品牌"}]},
	 {items:[{name:"#F.brandCode", type:"TEXT"  ,  bind:"brandCode", size:8, mode:"HIDDEN"},
	 		  {name:"#F.brandName", type:"TEXT"  ,  bind:"brandName", size:8, mode:"READONLY"}]},
	 {items:[{name:"#L_status"   , type:"LABEL" , value:"狀態"}]},	 		 
	 {items:[{name:"#F.statusName", type:"TEXT"  ,  bind:"statusName", size:8, mode:"READONLY"},
	 		{name:"#F.status"   , type:"TEXT"  ,  bind:"status", size:12, mode:"HIDDEN"}]}		   
	 ]},
	 {row_style:"", cols:[
	 {items:[{name:"#L_description", type:"LABEL", value:"說明"}]},
	 {items:[{name:"#F.description" , type:"TEXT",   bind:"description", size:50, maxLen:200,desc:"一般說明"}], td:" colSpan=7"},
	 {items:[{name:"#L_inputFormEmployee", type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.employeeName" , type:"TEXT",   bind:"employeeName",  mode:"READONLY", size:12}]},	 
	 {items:[{name:"#L_inputFormDate" , type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.inputFormDate" , type:"TEXT",   bind:"inputFormDate", mode:"READONLY", size:12}]}]}		
	 ],	 
	 beginService:"",
	 closeService:""			
	});
	
    vat.block.create(vnB_Header = 2, {
	id: "vatBlock_secondHead",generate: true, table:"cellspacing='1' class='' border='0' cellpadding='2'",
	title:"", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L_supplierCode", type:"LABEL" , value:"廠商代號"}]},	 
	 {items:[{name:"#F.supplierCode", type:"TEXT",  bind:"supplierCode",size:20}]},
	 {items:[{name:"#F.supplierName", type:"TEXT",size:20}]},
	 {items:[{name:"#L_salesPeriod"  , type:"LABEL" , value:"銷售期間"}]},
	 {items:[{name:"#F.salesPeriod"  , type:"TEXT"  ,  bind:"salesPeriod", size:12}]},			 
	 {items:[{name:"#L_currencyCode", type:"LABEL",  value:"幣別"}]},
	 {items:[{name:"#F.currencyCode", type:"SELECT",  bind:"currencyCode", size:1,init:allCurrencys,onchange:"setValueAndKey()"}]},//
	 {items:[{name:"#L_exchangeRate", type:"LABEL",  value:"匯率"}]},
	 {items:[{name:"#F.exchangeRate", type:"TEXT",  bind:"exchangeRate", size:8}]},
	 {items:[{name:"#L_ratio", type:"LABEL" , value:"比例"}]},
	 {items:[{name:"#F.ratio", type:"TEXT"  ,  bind:"ratio", size:8}]}]}],
	 
	 beginService:"",
	 closeService:""			
	});
	//{name:"#F.brandName", type:"TEXT"  ,  bind:"brandName", size:12, mode:"READONLY"}
	//{name:"#F.statusName"   , type:"TEXT"  ,  bind:"status", size:12, mode:"READONLY"}
}
function setValueAndKey(){
	var currencyCode = vat.item.getValueByName("#F.currencyCode");
	var processString = "process_object_name=poPurchaseOrderHeadService&process_object_method_name=getAJAXExchangeRateByCurrencyCode&currencyCode=" + currencyCode + "&organizationCode=TM" ;
	vat.ajax.startRequest(processString,  function() { 
  	if (vat.ajax.handleState()){
    	vat.item.setValueByName("#F.exchangeRate" ,vat.ajax.getValue("ExchangeRate", vat.ajax.xmlHttp.responseText));
   
  		}
	} );
}

/*
	LINE ITEM CODE CHANGE
*/
function onChangeItemCode() {
	//alert("execute onChangeItemCode s");
	var nItemLine = vat.item.getGridLine();
	//alert( "test ->" + nItemLine ) ;
	//alert("be nItemLine=" + nItemLine );
	var sItemCode = vat.item.getGridValueByName( "itemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var sLineId = vat.item.getGridValueByName("lineId",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	//alert(vat.item.getGridValueByName( "itemCode",nItemLine));
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	if (sItemCode != "") {
		var processString = "process_object_name=imPriceAdjustmentService&process_object_method_name=getAJAXLineData&brandCode=" +vat.item.getValueByName("#F.brandCode")+"&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode") + "&itemCode=" + sItemCode +"&lineId="+sLineId;
		processString += "&head_id="+vat.item.getValueByName("#F.headId")+"&exchangeRate="+vat.item.getValueByName("#F.exchangeRate")+"&ratio="+vat.item.getValueByName("#F.ratio")+"&priceType="+vat.item.getValueByName("#F.priceType") ;
		//processString +='&vatBean={FormBean:{"lineId":"1234","typeCode":"1","priceId":"456"},Others:{"test1":"ttt","test2":"hello"}}';測試用json字串
		//alert(processString);
		//alert("execute onChangeItemCode " + document.forms[0]["#form.orderTypeCode"].value);										   
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					if(vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText) != '查無資料'){
						if("PAP"==orderTypeCode){
							vat.item.setGridValueByName("catgory01",nItemLine, vat.ajax.getValue("Category13",vat.ajax.xmlHttp.responseText));
							vat.item.setGridValueByName("unitPrice", nItemLine,  vat.ajax.getValue("UnitPrice", vat.ajax.xmlHttp.responseText));
							vat.item.setGridValueByName("foreignCost",nItemLine,  vat.ajax.getValue("ForeignCost", vat.ajax.xmlHttp.responseText));
							vat.item.setGridValueByName("localCost",nItemLine,  vat.ajax.getValue("LocalCost", vat.ajax.xmlHttp.responseText));
							vat.item.setGridValueByName("itemCName",nItemLine,  vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText));
							vat.item.setGridValueByName("costRate",nItemLine,  vat.ajax.getValue("CostRate", vat.ajax.xmlHttp.responseText));
						}else{
							vat.item.setGridValueByName("originalPrice",nItemLine, vat.ajax.getValue("OrginalPrice",vat.ajax.xmlHttp.responseText));
							vat.item.setGridValueByName("originalQuotationPrice",nItemLine, vat.ajax.getValue("OrginalQuotationPrice",vat.ajax.xmlHttp.responseText));	
							vat.item.setGridValueByName("unitPrice", nItemLine,  vat.ajax.getValue("UnitPrice", vat.ajax.xmlHttp.responseText));
							vat.item.setGridValueByName("itemCName",nItemLine,  vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText));
							vat.item.setGridValueByName("grossProfitRate",nItemLine,  vat.ajax.getValue("GrossProfitRate", vat.ajax.xmlHttp.responseText));							
						}						
					}else{
						alert('查無資料，請重新輸入');
						vat.item.setGridValueByName("itemCode",sLineId, sItemCode);
					}					
				}
				//calculateTotalAmount();
				//checkCloseHead();
			}
		});
	} else {
	}

}
function calculateLineRate() {
	var nItemLine = vat.item.getGridLine();                                         
	var processString="";
	var orderTypeCodeForm = vat.item.getValueByName("#F.orderTypeCode");
if(orderTypeCodeForm=="PAJ"){
	  var originalPrice = vat.item.getGridValueByName("originalPrice", nItemLine);//parseInt(document.forms[0]["#form.imPriceLists["+lineId+"].originalPrice"].value);  
		var exchangeRate = vat.item.getValueByName("#F.exchangeRate");
		var originalQuotationPrice = vat.item.getGridValueByName("originalQuotationPrice",nItemLine);
		var unitPrice = vat.item.getGridValueByName("unitPrice",nItemLine); 
		var newQuotationPrice = vat.item.getGridValueByName("newQuotationPrice",nItemLine);
		processString = "process_object_name=imPriceAdjustmentService&process_object_method_name=getAJAXGrossProfitRate";
		processString += "&originalPrice="+originalPrice+"&originalQuotationPrice="+originalQuotationPrice+"&exchangeRate="+exchangeRate+"&newQuotationPrice="+newQuotationPrice;
		processString += "&unitPrice="+unitPrice;
  
	} else if(orderTypeCodeForm =="PAP"){
	 
	  var localCost = vat.item.getGridValueByName("localCost",nItemLine);  
	  var unitPrice = vat.item.getGridValueByName("unitPrice",nItemLine);
	  alert(localCost+"--"+unitPrice);   
	     processString = "process_object_name=imPriceAdjustmentService&process_object_method_name=getAJAXCalcCostRate&localCost="+localCost;
	     processString +="&unitPrice="+unitPrice;    
	}
	 vat.ajax.startRequest(processString,  function() { 
	    if (vat.ajax.handleState()){
	
	      if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
	      		if(orderTypeCodeForm == "PAP"){
	      			vat.item.setGridValueByName("costRate",nItemLine,vat.ajax.getValue("CostRate", vat.ajax.xmlHttp.responseText+"%"));	
	      		}else{
	      			vat.item.setGridValueByName("grossProfitRate",nItemLine,vat.ajax.getValue("GrossProfitRate", vat.ajax.xmlHttp.responseText));
	      		}
	      }else{
	      		if(orderTypeCodeForm == "PAP"){
	      			vat.item.setGridValueByName("costRate",nItemLine,"0%");
	      		}else{
	      			vat.item.setGridValueByName("grossProfitRate",nItemLine,"0");
	      		}
	      }
	  }
	} );
}

function kwePageSaveMethod(){}					


function kwePageSaveSuccess(){
	// alert("更新成功");
}

function kwePageLoadSuccess(){
	// alert("載入成功");	
}

function kwePageAppendBeforeMethod(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}

function kwePageAppendAfterMethod(){
	// return alert("新增完畢");
}
/*
	載入LINE資料
*/
function loadBeforeAjxService() {
	//alert("execute loadBeforeAjxService s");
	var processString = "process_object_name=imPriceAdjustmentService&process_object_method_name=getAJAXPageData&headId=" + vat.item.getValueByName("#F.headId");
	//alert("return " + processString);
	return processString;
}

/*
	載入LINE資料 SUCCESS
*/
function loadSuccessAfter() {
}
/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=imPriceAdjustmentService&process_object_method_name=updateAJAXPageLinesData" + "&headId=" + vat.item.getValueByName("#F.headId")+ "&status=" + vat.item.getValueByName("#F.status") + "&brandCode=" + vat.item.getValueByName("#F.brandCode")  + "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") ;
	}
	return processString;
}
/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
	vat.block.pageRefresh(0);
	var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
	if (errorMsg === "") {
		if ("saveHandler" == afterSavePageProcess) {	
			executeCommandHandler("main", "saveHandler");
		} else if ("submitHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "submitHandler");
		}else if ("voidHandler" == afterSavePageProcess) {
			executeCommandHandler("main", "voidHandler");
		}else if("executeExport"==afterSavePageProcess){
			exportFormData();
		}else if("executeImport"==afterSavePageProcess){
			importFormData();
		}
	}else{
		alert("錯誤訊息： " + errorMsg);
	}
	afterSavePageProcess = "";
}
/*
	暫存 SAVE HEAD && LINE
*/
function doSaveHandler() {
	//alert("doSaveHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			vat.block.pageDataSave(0);
			afterSavePageProcess = "saveHandler";
		}
	}
}
/*
	送出SUBMIT HEAD && LINE
*/
function doSubmitHandler() {
	//alert("doSubmitHandler");
	if (confirm("確認是否送出?")) {
		if (checkEnableLine()) {
			//save line
			vat.block.pageDataSave(0);
			afterSavePageProcess = "submitHandler";
		}
	}
}
/*
	作廢VOID HEAD && LINE
*/
function doVoidHandler() {
	if (confirm("是否確認作廢?")) {		
		//save line
		vat.block.pageDataSave(0);
		afterSavePageProcess = "voidHandler";		
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
	    var formId = vat.item.getValueByName("#formId").replace(/^\s+|\s+$/, '');;
	    var processId = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
	    var status = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');;
	    var employeeCode = vat.bean().vatBeanOther.loginEmployeeCode.replace(/^\s+|\s+$/, '');;
	    var headId = vat.item.getValueByName("#F.headId");
	    var inProcessing   = !(processId == null || processId == ""  || processId == 0);
	    var orderNoPrefix         = vat.item.getValueByName("#F.orderNo").substring(0,3);
	    var approvalResult = vat.item.getValueByName("#F.approvalResult");
	    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	    //alert("formId=" + formId);
	    //alert("status=" + status+"-"+employeeCode+"p:"+processId);
	    
	    //alert("processId=" + processId);
	    
	   if((orderNoPrefix == "TMP" &&  status == "SAVE") || status == "UNCONFIRMED" ||
		   (inProcessing   && (status == "SAVE"  || status == "SIGNING" || status == "REJECT" ))){
	       vat.block.pageDataSave(1, 
			    {  funcSuccess:function(){
					  vat.bean().vatBeanOther.status = status;
					  vat.bean().vatBeanOther.processId = processId;
					  vat.bean().vatBeanOther.formAction = formAction;
					  vat.bean().vatBeanOther.loginEmployeeCode = employeeCode;
					  vat.bean().vatBeanOther.approvalResult = approvalResult;
					  vat.bean().vatBeanOther.approvalComment = approvalComment;					
					  //alert(approvalResult);
					  vat.block.submit(function(){return "process_object_name=imPriceAdjustmentAction"+
			                    "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
			        
		          	}
	          	}
	          );
	    }else{
	    	alert("您的表單已加入待辦事件，請從待辦事件選取後，再次送出!");
	    }
    } 
    
}

function changeFormStatus(formId, processId, status){
    var formStatus = "UNKNOW";
    if(formId == null){
        formStatus = "SIGNING";
    }else if(processId != null){
        if(status == "SAVE" || status == "REJECT"){
            formStatus = "SIGNING";
        }  
    }
    return formStatus;
}
/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {

	return true ;
}
function appendBeforeService() {
	
	return true;
}
function appendAfterService() {

}
function doBeforePicker(){
	vat.formD.pageDataSave(1);
}
function doPageDataSave(){
    vat.block.pageDataSave(1);
}

function doPageRefresh(){
    vat.block.pageRefresh(1);
}
/*
	匯出
*/
function doExport() {
	//save line
	afterSavePageProcess = "executeExport";
	vat.block.pageSearch(1);	
}

/*
	匯入
*/
function doImport() {
	//save line
	afterSavePageProcess = "executeImport";
	vat.block.pageSearch(1);	
}
function exportFormData(){
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IM_PRICE_ADJUSTMENT" + 
              "&fileType=XLS" + 
              "&processObjectName=imPriceAdjustmentService" + 
              "&processObjectMethodName=findById" + 
              "&gridFieldName=imPriceLists" + 
              "&arguments=" + vat.item.getValueByName("#F.headId") + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '定價變價單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function importFormData(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=IM_PRICE_ADJUSTMENT" +
		"&importFileType=XLS" +
        "&processObjectName=imPriceAdjustmentService" + 
        "&processObjectMethodName=executeImportPriceLists" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG" +
        "&blockId=1",
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	refreshForm("");
	 }
}
function refreshForm(vsHeadId){
	document.forms[0]["#processId"         ].value = "";       
	document.forms[0]["#formId"            ].value = vsHeadId;       	
	document.forms[0]["#assignmentId"      ].value = "";     
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"            ].value;	
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	//alert("currentRecordNumber:"+vat.bean().vatBeanOther.currentRecordNumber);
	vat.block.submit(
		function(){
			return "process_object_name=imPriceAdjustmentService&process_object_method_name=executeInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     		             vat.item.bindAll();
     		             vat.block.pageDataLoad(1, vnCurrentPage = 1);
     	}});
	

}