/*** 
 *	檔案: imLocaton.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by david
 *  	All rights reserved.
 *  </pre>
 */

vat.debug.disable();
var afterSavePageProcess = "";

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;
var vnB_Total = 4;

function kweImBlock(){
  kweInitial();
  kweButtonLine();
  kweHeader();
    if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");                                                                                                                                                                                                                                          
		vat.tabm.createButton(0 ,"xTab3","明細資料" ,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false , ""); 
      	vat.tabm.createButton(0 ,"xTab5","簽核資料" ,"vatApprovalDiv","images/tab_approval_data_dark.gif","images/tab_approval_data_light.gif",document.forms[0]["#processId"].value==""?"none":"inline");
  	}
  kweDetail();
  kweWfBlock(document.forms[0]["#brandCode" ].value, 
             "CDL", 
             document.forms[0]["#formId"            ].value,
             document.forms[0]["#loginEmployeeCode"].value );

	//簽核意見寫死為Y             
  //vat.item.setAttributeByName("#F.approvalResult","disabled",true);           
  	doFormAccessControl();
}

function kweInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#brandCode" ].value,
          //organizationCode  	: document.forms[0]["#organizationCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          employeeCode			: document.forms[0]["#loginEmployeeCode" ].value
        };
   	vat.bean.init(	
  		function(){
				return "process_object_name=cmDeclarationLogService&process_object_method_name=executeInitial"; 
    	},{
    		other: true
    	}
    );
  }
}


function kweHeader(){
	var allOrderTypeCode =        [["", true], ["AIF-進貨短溢裝單"], ["AIF"]]; 
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"報關單維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{ name:"#L.declNo", type:"LABEL", value:"報關單號" }]},
				{items:[{ name:"#F.declNo", type:"TEXT", bind:"declNo", size:20, eChange:"changeDeclNo()"},
						{ name:"#F.declType", type:"TEXT", bind:"declType", size:10, mode:"READONLY"},
						{ name:"#F.identify", type:"TEXT", bind:"identify", size:10, mode:"READONLY"}
						]},
				{items:[{ name:"#L.status", type:"LABEL", value:"狀態" }]},
				{items:[{ name:"#F.status", type:"TEXT", bind:"status", mode:"HIDDEN"},
						{ name:"#F.statusName", type:"TEXT", bind:"statusName", size:10, back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{ name:"#L.createdBy", type:"LABEL" , value:"填單人員" }]},
		 		{items:[{name:"#F.createdBy"  	, type:"TEXT", bind:"createdBy", mode:"HIDDEN"},
		 				{name:"#F.createdByName", type:"TEXT",   bind:"createdByName", size:20, mode:"READONLY"}]},
		 		{items:[{ name:"#L.creationDate", type:"LABEL" , value:"填單日期" }]},
			 	{items:[{ name:"#F.creationDate", type:"TEXT", bind:"creationDate", mode:"READONLY", size:20 }]}
			]},
			{row_style:"", cols:[
				{items:[{ name:"#L.orderTypeCode", type:"LABEL" , value:"單別" },
				{ name:"#F.statusType", type:"TEXT", bind:"statusType", mode:"HIDDEN"}				
				]},
				{items:[{name:"#F.orderTypeCode", type:"SELECT", init:allOrderTypeCode, mode:"READONLY"}]},
		 		{items:[{ name:"#L.orderNo", type:"LABEL" , value:"單號" }]},
		 		{items:[{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:20, eChange:"changeSourceOrderNo()"}]}
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

function kweDetail(){
var modifyTypes = vat.bean("modifyTypes");
	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;

	if(vat.item.getValueByName("#F.status")!= "SAVE"){
		varCanGridDelete = false;
		varCanGridAppend = false;
		varCanGridModify = false;
	}

    vat.item.make(vnB_Detail, "indexNo", {type:"IDX" , desc:"項次"});
	vat.item.make(vnB_Detail, "itemNo", {type:"NUMB", size:6, maxLen:6, desc:"項次", mode:"hidden"});
	vat.item.make(vnB_Detail, "modType", {type:"SELECT", size:10, maxLen:10, desc:"修改類型", init:modifyTypes, mode:"readOnly"});
	vat.item.make(vnB_Detail, "prdtNo", {type:"TEXT", size: 8, maxLen:12, desc:"料號", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "descrip", {type:"TEXT", size: 20, maxLen:500, desc:"貨名", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "brand", {type:"TEXT", size: 8, desc:"廠牌", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "model", {type:"TEXT", size: 8, desc:"型號", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "spec", {type:"TEXT", size: 8, desc:"規格", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "NWght", {type:"NUMB", size: 8, maxLen: 8, desc:"淨重", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "qty", {type:"NUMB", size: 8, maxLen:20, desc:"數量", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "unit", {type:"TEXT", size: 8, maxLen:20, desc:"單位", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "ODeclNo", {type:"TEXT", size: 8, maxLen:20, desc:"原進倉報單號碼", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "OItemNo", {type:"TEXT", size: 8, maxLen:20, desc:"原報單項次", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "custValueAmt", {type:"NUMB", size:10, desc:"總完稅價格"});
	vat.item.make(vnB_Detail, "remark", {type:"TEXT", maxLen:200, desc:"備註", eChange:"changeModify()"});
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});

	vat.block.pageLayout(vnB_Detail, {
								id: "vatDetailDiv",	
								pageSize: 10,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,									
							    appendBeforeService : "kweSoPageAppendBeforeMethod()",
							    appendAfterService  : "",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "loadSuccessAfter()",
								eventService        : "",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()"
								});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function kweSoPageAppendBeforeMethod(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}

function kweSoPageAppendAfterMethod(){
	// return alert("新增完畢");
}

function loadBeforeAjxService(){
	var processString = "process_object_name=cmDeclarationLogService&process_object_method_name=getAJAXPageData" + 
	                    "&identify=" + vat.item.getValueByName("#F.identify");
	return processString;											
}

function loadSuccessAfter(){

}
function changeModify(){
  var vLineId                 = vat.item.getGridLine();
  if(vat.item.getGridValueByName("modType",vLineId) == "S")
  	vat.item.setGridValueByName("modType",vLineId, "M");
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
//alert(vat.item.getValueByName("#F.status"));
	var processString = "process_object_name=cmDeclarationLogService&process_object_method_name=updateAJAXPageLinesData" + 
						"&identify=" + vat.item.getValueByName("#F.identify") +
						"&employeeCode=" + document.forms[0]["#loginEmployeeCode" ].value +  
						"&status=" + vat.item.getValueByName("#F.status");
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
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
	 			{name:"#B.new"         , type:"IMG"    ,value:"建立新資料",   src:"./images/button_create.gif"  ,eClick:'createRefreshForm()'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SIGNING")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.message"    , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'}	 			
				],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	
}	

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}

function doSubmit(formAction){
	var alertMessage ="是否確定?";

		if("SIGNING" == formAction){
			alertMessage = "是否確定送出?";
		}else if ("SAVE" == formAction){
		 	alertMessage = "是否確定暫存?";
		}else if("SUBMIT_BG" == formAction){
			alertMessage = "是否確定背景送出?";
		}
		
		if(confirm(alertMessage)){
		    var formId                = vat.item.getValueByName("#F.identify").replace(/^\s+|\s+$/, '');
		    var processId             = document.forms[0]["#processId"      ].value;
		    var beforeChangeStatus	  = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		    var employeeCode          = document.forms[0]["#loginEmployeeCode" ].value;
		    var assignmentId          = document.forms[0]["#assignmentId"      ].value;
		    var approvalResult        = vat.item.getValueByName("#F.approvalResult");
		    var brandCode 			  = document.forms[0]["#brandCode" ].value;
		    if(approvalResult == true){
		    	approvalResult = "true"
		    }else{
		    	approvalResult = "false"
		    }
		    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
			var organizationCode 	  = "TM";
			      vat.block.pageDataSave(vnB_Detail, 
				    {  funcSuccess:function(){
				    	  vat.bean().vatBeanOther.formId          	= formId;
						  vat.bean().vatBeanOther.beforeChangeStatus= beforeChangeStatus;
						  vat.bean().vatBeanOther.assignmentId      = assignmentId;
						  vat.bean().vatBeanOther.processId       	= processId;
						  vat.bean().vatBeanOther.formAction      	= formAction;
						  vat.bean().vatBeanOther.approvalResult  	= approvalResult;
						  vat.bean().vatBeanOther.approvalComment 	= approvalComment;
						  vat.bean().vatBeanOther.employeeCode		= employeeCode;
						  vat.bean().vatBeanOther.organizationCode	= organizationCode;
						  vat.bean().vatBeanOther.brandCode	= brandCode;
						  if("SUBMIT_BG" == formAction){
				      		vat.block.submit(function(){return "process_object_name=cmDeclarationLogAction"+
				                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
				    	  }else{
						  	vat.block.submit(function(){return "process_object_name=cmDeclarationLogAction"+
				                    "&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
				          }
			          	}
		          	}
		          );
		}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=CM_DECLARATION_LOG" +
		"&levelType=ERROR" +
        "&processObjectName=cmDeclarationLogService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.identify") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
	vat.bean().vatBeanOther.formAction = "SIGNING";
    vat.block.submit(function(){return "process_object_name=cmDeclarationLogAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

function createRefreshForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
		vat.bean().vatBeanOther.formId       = "";	
		vat.block.submit(
			function(){
				return "process_object_name=cmDeclarationLogService&process_object_method_name=executeInitial"; 
	     	},{other      : true, 
	     	   funcSuccess:function(){
	     		             vat.item.bindAll();
	     		             vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
	     		             doFormAccessControl();
	     	}});
	}
}

/*
function doPageDataSave(){
    vat.block.pageDataSave(vnB_Detail);
}
*/

function doFormAccessControl(){
	
	var aifOrderNo = document.forms[0]["#aifOrderNo" ].value;
	if(aifOrderNo==""){
	
	}else{
		changeSourceOrderNo();
	}
	
	
	var Vstatus = vat.item.getValueByName("#F.status");
	var VstatusType = vat.item.getValueByName("#F.statusType");
	var vsProcessId     = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
	var canBeClaimed	= document.forms[0]["#canBeClaimed"].value;
	vat.item.setStyleByName("#B.save"		, "display", "none");
	vat.item.setStyleByName("#B.submit"		, "display", "none");
	vat.item.setStyleByName("#B.submitBG"	, "display", "none");
	vat.item.setGridAttributeByName("prdtNo", "readOnly", true);
	vat.item.setGridAttributeByName("descrip", "readOnly", true);
	vat.item.setGridAttributeByName("brand", "readOnly", true);
	vat.item.setGridAttributeByName("model", "readOnly", true);
	vat.item.setGridAttributeByName("spec", "readOnly", true);
	vat.item.setGridAttributeByName("NWght", "readOnly", true);
	vat.item.setGridAttributeByName("qty", "readOnly", true);
	vat.item.setGridAttributeByName("unit", "readOnly", true);
	vat.item.setGridAttributeByName("ODeclNo", "readOnly", true);
	vat.item.setGridAttributeByName("OItemNo", "readOnly", true);
	vat.item.setGridAttributeByName("custValueAmt", "readOnly", true);
	vat.item.setGridAttributeByName("remark", "readOnly", true);
	
	if("SIGNING" == Vstatus){
		vat.item.setStyleByName("#B.submit"		, "display", "inline");
	}
    if("SIGNING" == Vstatus || "FINISH" == Vstatus){
    	vat.item.setStyleByName("#B.new"		, "display", "none");
  		vat.item.setStyleByName("#B.save"		, "display", "none");
  		vat.item.setStyleByName("#B.submitBG"	, "display", "none");
  		vat.item.setStyleByName("#B.message"	, "display", "none");
  	}
  	
  	if(vsProcessId == 0 || vsProcessId == null){
  	
  	}else{
  		if("SIGNING" == Vstatus){
			vat.item.setStyleByName("#B.submit"		, "display", "inline");
			vat.item.setAttributeByName("#F.declNo", "readOnly", true);
			vat.item.setAttributeByName("#F.orderNo", "readOnly", true);			
		}
		if("SAVE" == Vstatus){
			vat.item.setStyleByName("#B.save"		, "display", "inline");
			vat.item.setStyleByName("#B.submit"		, "display", "inline");
			vat.item.setGridAttributeByName("prdtNo", "readOnly", false);
			vat.item.setGridAttributeByName("descrip", "readOnly", false);
			vat.item.setGridAttributeByName("brand", "readOnly", false);
			vat.item.setGridAttributeByName("model", "readOnly", false);
			vat.item.setGridAttributeByName("spec", "readOnly", false);
			vat.item.setGridAttributeByName("NWght", "readOnly", false);
			if(VstatusType=="2"){
				vat.item.setGridAttributeByName("qty", "readOnly", true);
			}else{
				vat.item.setGridAttributeByName("qty", "readOnly", false);
			}					
			vat.item.setGridAttributeByName("unit", "readOnly", false);
			vat.item.setGridAttributeByName("ODeclNo", "readOnly", false);
			vat.item.setGridAttributeByName("OItemNo", "readOnly", false);
			vat.item.setGridAttributeByName("custValueAmt", "readOnly", false);
			vat.item.setGridAttributeByName("remark", "readOnly", false);
			vat.item.setStyleByName("#B.submit", "display", "inline");
			vat.item.setStyleByName("#B.save", "display", "inline");
			vat.item.setAttributeByName("#F.orderNo", "readOnly", true);
		}
  	}
	if(canBeClaimed == "true"){
  		vat.item.setStyleByName("#B.submit",    "display", "none");
	}
}

function changeDeclNo(){
    vat.item.setValueByName("#F.declNo" , vat.item.getValueByName("#F.declNo").replace(/^\s+|\s+$/, ''));
    var FstatusType = vat.item.getValueByName("#F.statusType");
    var DstatusType = document.forms[0]["#statusType" ].value;
    var LastStatusType;
    if(FstatusType!=""){
    	LastStatusType = FstatusType;
    }
    if(DstatusType!=""){
    	LastStatusType = DstatusType;
    }
    if(vat.item.getValueByName("#F.declNo") !== ""){
        vat.ajax.XHRequest(
		{
        post:"process_object_name=cmDeclarationLogService"+
                    "&process_object_method_name=updateDeclByDeclNoForAJAX"+
                    "&declNo=" + vat.item.getValueByName("#F.declNo")+
                    "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value+
                    "&identify=" + vat.item.getValueByName("#F.identify")+
                    "&statusType=" + LastStatusType,                      
		find: function changePromotionCodeRequestSuccess(oXHR){
				var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
				if(errorMsg != "" ){
					alert(errorMsg);
					vat.item.setGridAttributeByName("prdtNo", "readOnly", true);
					vat.item.setGridAttributeByName("descrip", "readOnly", true);
					vat.item.setGridAttributeByName("brand", "readOnly", true);
					vat.item.setGridAttributeByName("model", "readOnly", true);
					vat.item.setGridAttributeByName("spec", "readOnly", true);
					vat.item.setGridAttributeByName("NWght", "readOnly", true);
					vat.item.setGridAttributeByName("qty", "readOnly", true);
					vat.item.setGridAttributeByName("unit", "readOnly", true);
					vat.item.setGridAttributeByName("ODeclNo", "readOnly", true);
					vat.item.setGridAttributeByName("OItemNo", "readOnly", true);
					vat.item.setGridAttributeByName("custValueAmt", "readOnly", true);
					vat.item.setGridAttributeByName("remark", "readOnly", true);
					vat.item.setStyleByName("#B.submit"		, "display", "none");
					vat.item.setStyleByName("#B.save"		, "display", "none");
					vat.item.setAttributeByName("#F.orderNo", "readOnly", false);					
				}else{
					vat.item.setGridAttributeByName("prdtNo", "readOnly", false);
					vat.item.setGridAttributeByName("descrip", "readOnly", false);
					vat.item.setGridAttributeByName("brand", "readOnly", false);
					vat.item.setGridAttributeByName("model", "readOnly", false);
					vat.item.setGridAttributeByName("spec", "readOnly", false);
					vat.item.setGridAttributeByName("NWght", "readOnly", false);
					if(document.forms[0]["#statusType" ].value=="2"||vat.item.getValueByName("#F.statusType")=="2"){
						vat.item.setGridAttributeByName("qty", "readOnly", true);
					}else{
						vat.item.setGridAttributeByName("qty", "readOnly", false);
					}					
					vat.item.setGridAttributeByName("unit", "readOnly", false);
					vat.item.setGridAttributeByName("ODeclNo", "readOnly", false);
					vat.item.setGridAttributeByName("OItemNo", "readOnly", false);
					vat.item.setGridAttributeByName("custValueAmt", "readOnly", false);
					vat.item.setGridAttributeByName("remark", "readOnly", false);
					vat.item.setStyleByName("#B.submit", "display", "inline");
					vat.item.setStyleByName("#B.save", "display", "inline");
					vat.item.setAttributeByName("#F.orderNo", "readOnly", true);
				}
				vat.item.setValueByName("#F.declType" , vat.ajax.getValue("DeclType", oXHR.responseText));
				vat.item.setValueByName("#F.orderNo" , "");
			    vat.block.pageRefresh(vnB_Detail);
        }
        });
    }
}

function changeSourceOrderNo(){
    
    var aifOrderNo = document.forms[0]["#aifOrderNo" ].value;
    if(aifOrderNo==""){
		vat.item.setValueByName("#F.orderNo" , vat.item.getValueByName("#F.orderNo").replace(/^\s+|\s+$/, ''));    
    	var orderNo = vat.item.getValueByName("#F.orderNo");
    }else{
		var orderNo = aifOrderNo;
		vat.item.setValueByName("#F.orderNo" , orderNo);
	}  
    
    var FstatusType = vat.item.getValueByName("#F.statusType");
    var DstatusType = document.forms[0]["#statusType" ].value;
    var LastStatusType;
    if(FstatusType!=""){
    	LastStatusType = FstatusType;
    }
    if(DstatusType!=""){
    	LastStatusType = DstatusType;
    }    
    if(orderNo !== ""){
        vat.ajax.XHRequest(
		{
        post:"process_object_name=cmDeclarationLogService"+
                    "&process_object_method_name=updateDeclNoForAifOrderAJAX"+
                    "&orderNo=" + orderNo +
                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")+
                    "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value+
                    "&identify=" + vat.item.getValueByName("#F.identify")+
                    "&statusType=" + LastStatusType,                      
		find: function changePromotionCodeRequestSuccess(oXHR){
				var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
				if(errorMsg != "" ){
					alert(errorMsg);
					vat.item.setGridAttributeByName("prdtNo", "readOnly", true);
					vat.item.setGridAttributeByName("descrip", "readOnly", true);
					vat.item.setGridAttributeByName("brand", "readOnly", true);
					vat.item.setGridAttributeByName("model", "readOnly", true);
					vat.item.setGridAttributeByName("spec", "readOnly", true);
					vat.item.setGridAttributeByName("NWght", "readOnly", true);
					vat.item.setGridAttributeByName("qty", "readOnly", true);
					vat.item.setGridAttributeByName("unit", "readOnly", true);
					vat.item.setGridAttributeByName("ODeclNo", "readOnly", true);
					vat.item.setGridAttributeByName("OItemNo", "readOnly", true);
					vat.item.setGridAttributeByName("custValueAmt", "readOnly", true);
					vat.item.setGridAttributeByName("remark", "readOnly", true);
					vat.item.setStyleByName("#B.submit"		, "display", "none");
					vat.item.setStyleByName("#B.save"		, "display", "none");
					vat.item.setAttributeByName("#F.declNo", "readOnly", false);					
				}else{
					vat.item.setGridAttributeByName("prdtNo", "readOnly", false);
					vat.item.setGridAttributeByName("descrip", "readOnly", false);
					vat.item.setGridAttributeByName("brand", "readOnly", false);
					vat.item.setGridAttributeByName("model", "readOnly", false);
					vat.item.setGridAttributeByName("spec", "readOnly", false);
					vat.item.setGridAttributeByName("NWght", "readOnly", false);
					vat.item.setGridAttributeByName("qty", "readOnly", true);
					vat.item.setGridAttributeByName("unit", "readOnly", false);
					vat.item.setGridAttributeByName("ODeclNo", "readOnly", false);
					vat.item.setGridAttributeByName("OItemNo", "readOnly", false);
					vat.item.setGridAttributeByName("custValueAmt", "readOnly", false);
					vat.item.setGridAttributeByName("remark", "readOnly", false);
					vat.item.setStyleByName("#B.submit"		, "display", "inline");
					vat.item.setStyleByName("#B.save"		, "display", "inline");
					vat.item.setAttributeByName("#F.declNo", "readOnly", true);
				}
				vat.item.setValueByName("#F.declNo" , vat.ajax.getValue("DeclNo", oXHR.responseText));
				vat.item.setValueByName("#F.declType" , vat.ajax.getValue("DeclType", oXHR.responseText));
			    vat.block.pageRefresh(vnB_Detail);
        }
        });
    }
}