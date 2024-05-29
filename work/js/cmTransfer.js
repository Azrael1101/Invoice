/***
 *	檔案: cmTransfer.js
 *	說明：貨櫃（物）運送單
 *  <pre>
 *  	Created by Weichun
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;

function kweImBlock(){
 	kweInitial();
	kweButtonLine();
  	kweHeader();

	doFormAccessControl();
}

// 初始化
function kweInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        {
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(
	  		function(){
				return "process_object_name=cmTransferAction&process_object_method_name=performInitial";
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
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			//{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  ,
	 			//						 openMode:"open",
	 			//						 service:"Cm_Movement:search:20110406.page",    // ?orderTypeCode="+vat.bean().vatBeanOther.orderTypeCode
	 			//						 left:0, right:0, width:1024, height:768,
	 			//						 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"saveSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.reverter"	   , type:"IMG"    ,value:"反確認",   src:"./images/button_uncomfirm.gif", eClick:'doSubmit("REVERTER")'},
	 			{name:"reverterSpace"  , type:"LABEL"  ,value:"　"},
	 			{name:"#B.transferPrint"	, type:"IMG"    ,value:"單據列印(運送單)",   src:"./images/button_transport.gif" , eClick:'openTransferReportWindow("")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"}], td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:"",
		closeService:""
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

// 運送單主檔
function kweHeader(){
	var transferOrderNoValue = vat.bean("transferOrderNo");
	var transferValue = vat.bean("transfer") == ''  || vat.bean("transfer") == null ? "4C" : vat.bean("transfer"); // 運送單類型
	var ownerValue = vat.bean("owner") == '' || vat.bean("owner") == null ? "采盟股份有限公司" : vat.bean("owner"); // 貨主名稱
	var startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;南崁保稅倉庫;CD198" : vat.bean("startStation"); // 起運站名
	var toStationValue = "";
	if(transferOrderNoValue.indexOf("TMA")>-1)
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;桃園機場;免稅商店" : vat.bean("toStation"); // 運往站名

	var vehicleStationValue = vat.bean("vehicleStation") == '' || vat.bean("vehicleStation") == null ? "" : vat.bean("vehicleStation"); // 車行名稱
	var vehicleNoValue =  vat.bean("vehicleNo") == '' || vat.bean("vehicleNo") == null ? "" : vat.bean("vehicleNo"); // 車號
	var driverLicenceValue =  vat.bean("driverLicence") == '' || vat.bean("driverLicence") == null ? "" : vat.bean("driverLicence"); // 駕照號碼
	var trackValue =  vat.bean("track") == '' || vat.bean("track") == null ? "保稅貨物" : vat.bean("track"); // 落地追蹤

	//var leaveBoxNoteValue =  vat.bean("leaveBoxNote") == null ? "CTN" : vat.bean("leaveBoxNote"); // 放行總量：箱單位
	//var leaveQuantityNoteValue =  vat.bean("leaveQuantityNote") == null ? "PCS" : vat.bean("leaveQuantityNote"); // 放行總量：件單位
	//var truckBoxNoteValue =  vat.bean("truckBoxNote") == null ? "CTN" : vat.bean("truckBoxNote"); // 本櫃車數量：箱單位
	//var truckQuantityNoteValue =  vat.bean("truckQuantityNote") == null ? "PCS" : vat.bean("truckQuantityNote"); // 本櫃車數量：件單位

	var leaveTimeTValue =  vat.bean("leaveTimeT") == '' || vat.bean("leaveTimeT") == null ? "" : vat.bean("leaveTimeT"); // 出站時間
	var arriveTimeTValue =  vat.bean("arriveTimeT") == '' || vat.bean("arriveTimeT") == null ? "" : vat.bean("arriveTimeT"); // 到站時間

	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"貨櫃（物）運送單功能維護作業",
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.transferOrderNo",	type:"LABEL", 	value:"運送單號" }]},
				{items:[{name:"#F.transferOrderNo", type:"TEXT", 	bind:"transferOrderNo",size:20, init:transferOrderNoValue, mode:"READONLY"}]},
				{items:[{name:"#L.transfer",		type:"LABEL", 	value:"運送裝箱" }]},
				{items:[{name:"#F.transfer",		type:"TEXT", 	bind:"transfer", size:4, init:transferValue},
					    {name:"#L.transferNote", 	type:"LABEL", 	value:"<BR>1.進口 2.轉運 3.轉口 4.出口<BR>A.重櫃 B空櫃 C.非櫃裝",size:30}]},
				{items:[{name:"#L.createBy",		type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
						{name:"#F.createByName",	type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.status", 			type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 			type:"TEXT", 	bind:"status", size:15, mode:"HIDDEN"},
						{name:"#F.statusName", 		type:"TEXT",  bind:"statusName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.airplaneNo",		type:"LABEL", 	value:"航機班次" }]},
				{items:[{name:"#F.airplaneNo",		type:"TEXT", 	bind:"airplaneNo", size:15}]},
				{items:[{name:"#L.masterNo",		type:"LABEL", 	value:"主號" }]},
				{items:[{name:"#F.masterNo",		type:"TEXT", 	bind:"masterNo", size:10 }]},
				{items:[{name:"#L.secondNo",		type:"LABEL", 	value:"分號" }]},
				{items:[{name:"#F.secondNo",		type:"TEXT", 	bind:"secondNo", size:10}]},
				{items:[{name:"#L.owner",			type:"LABEL", 	value:"貨主名稱" }]},
				{items:[{name:"#F.owner",			type:"TEXT", 	bind:"owner", size:15, init:ownerValue}]}

			]},
			{row_style:"", cols:[
				{items:[{name:"#L.clearance",		type:"LABEL", 	value:"通關方式" }]},
				{items:[{name:"#F.clearance",		type:"TEXT", 	bind:"clearance", size:10}]},
				{items:[{name:"#L.release",			type:"LABEL", 	value:"放行附帶條件" }]},
				{items:[{name:"#F.release",			type:"TEXT", 	bind:"release", size:10}]},
				{items:[{name:"#L.startStation",	type:"LABEL", 	value:"起運站名" }]},
				{items:[{name:"#F.startStation",	type:"TEXT", 	bind:"startStation", size:32, init:startStationValue}]},
				{items:[{name:"#L.toStation",		type:"LABEL", 	value:"運往站名" }]},
				{items:[{name:"#F.toStation",		type:"TEXT", 	bind:"toStation", size:32, init:toStationValue}]}

			]},
			{row_style:"", cols:[
				{items:[{name:"#L.leaveTimeT",		type:"LABEL", 	value:"出站時間" }]},
				{items:[{name:"#F.leaveTimeT",		type:"TEXT", 	bind:"leaveTimeT", size:20},
						{name:"#L.leaveTimeTNote", 	type:"LABEL", 	value:" 時間格式：YYYMMDDhhmm",size:15, maxLen:11, init:leaveTimeTValue}], td:" colSpan=3"},
				{items:[{name:"#L.arriveTimeT",		type:"LABEL", 	value:"進站時間" }]},
				{items:[{name:"#F.arriveTimeT",		type:"TEXT", 	bind:"arriveTimeT", size:20},
						{name:"#L.arriveTimeTNote", 	type:"LABEL", 	value:" 時間格式：YYYMMDDhhmm",size:15, maxLen:11, init:arriveTimeTValue}], td:" colSpan=3"}
			]},
			{row_style:"", cols:[
	 			{items:[{name:"#L.leaveBox",		type:"LABEL", 	value:"放行總數量" }]},
				{items:[{name:"#F.leaveBox",		type:"TEXT", 	bind:"leaveBox", size:8},
					    {name:"#F.leaveBoxNote", 	type:"TEXT", 	bind:"leaveBoxNote", size:5}]},
				{items:[{name:"#L.leaveQuantity",	type:"LABEL", 	value:"放行總數量" }]},
				{items:[{name:"#F.leaveQuantity",	type:"TEXT", 	bind:"leaveQuantity", size:8},
					    {name:"#F.leaveQuantityNote", 	type:"TEXT", 	bind:"leaveQuantityNote", size:5}]},
	 			{items:[{name:"#L.truckBox",		type:"LABEL", 	value:"本櫃車數量" }]},
				{items:[{name:"#F.truckBox",		type:"TEXT", 	bind:"truckBox", size:7},
					    {name:"#F.truckBoxNote", 	type:"TEXT", 	bind:"truckBoxNote", size:5}]},
				{items:[{name:"#L.truckQuantity",	type:"LABEL", 	value:"本櫃車數量" }]},
				{items:[{name:"#F.truckQuantity",	type:"TEXT", 	bind:"truckQuantity", size:7},
					    {name:"#F.truckQuantityNote", 	type:"TEXT", 	bind:"truckQuantityNote", size:5}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.orderNo", 		type:"LABEL", 	value:"貨櫃（物）標記號碼以及型態"}]},
	 			{items:[{name:"#F.orderNo", 		type:"TEXT",  bind:"orderNo",  size:25, maxLen:40}]},
	 			{items:[{name:"#L.carNo", 			type:"LABEL", 	value:"保卡號碼"}]},
	 			{items:[{name:"#F.carNo", 			type:"TEXT",  bind:"carNo",  size:15}]},
				{items:[{name:"#L.sealNo",			type:"LABEL", 	value:"封條號碼" }]},
				{items:[{name:"#F.sealNo",			type:"TEXT", 	bind:"sealNo", size:25, maxLen:40}]},
	 			{items:[{name:"#L.vehicleStation",	type:"LABEL", 	value:"車行名稱" }]},
				{items:[{name:"#F.vehicleStation",	type:"TEXT", 	bind:"vehicleStation", size:25, init:vehicleStationValue}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.vehicleNo",		type:"LABEL", 	value:"車號" }]},
				{items:[{name:"#F.vehicleNo",		type:"TEXT", 	bind:"vehicleNo", size:15 , init:vehicleNoValue}]},
				{items:[{name:"#L.driverCode",		type:"LABEL", 	value:"司機姓名" }]},
				{items:[{name:"#F.driverCode",		type:"TEXT", 	bind:"driverCode", size:15 }]},
				{items:[{name:"#L.driverLicence",	type:"LABEL", 	value:"司機駕照號碼" }]},
				{items:[{name:"#F.driverLicence",	type:"TEXT", 	bind:"driverLicence", size:15, init:driverLicenceValue}]},
	 			{items:[{name:"#L.track",			type:"LABEL", 	value:"落地追蹤" }]},
				{items:[{name:"#F.track",			type:"TEXT", 	bind:"track", size:15, init:trackValue}], td:" colSpan=7"}
			]}
		],

		beginService:"",
		closeService:""
	});
}



// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){
//	vat.block.pageRefresh(div);
}

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
//    alert("loadSuccessAfter");
//	vat.item.setGridAttributeByName("objectCode", "readOnly", true);
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


// 建立新資料按鈕
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;
    	refreshForm("");
	 }
}

function createRefreshForm(){
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
function doSubmit(formAction) {

    var leaveTimeT = vat.item.getValueByName("#F.leaveTimeT");
    if (leaveTimeT.length > 0 && leaveTimeT.length != 11) {
        alert("出站日期格式錯誤，請重新輸入！");
        return;
    }
    var arriveTimeT = vat.item.getValueByName("#F.arriveTimeT");
    if (arriveTimeT.length > 0 && arriveTimeT.length != 11) {
        alert("進站日期格式錯誤，請重新輸入！");
        return;
    }

	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){
		alertMessage = "是否確定暫存?";
	}else if("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if("REVERTER" == formAction){
		alertMessage = "是否確定執行反確認?";
	}

	if(confirm(alertMessage)){
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, '');
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var orderNoPrefix		  = vat.item.getValueByName("#F.transferOrderNo").substring(0,3);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
	    var formStatus = status;
		if("SAVE" == formAction){
	        formStatus = "SAVE";
	    }else if("SUBMIT" == formAction || "SUBMIT_BG" == formAction ){
	        formStatus = changeFormStatus(formId, processId, status, formAction);
	    }else if("VOID" == formAction){
	        formStatus = "VOID";
	    }

		if("REVERTER" == formAction){ // 反確認
			vat.bean().vatBeanOther.formAction = formAction;
	    	vat.block.submit(function(){
				return "process_object_name=cmTransferAction"+ "&process_object_method_name=executeReverter";
					},{bind:true, link:true, other:true}
			);
	    }else if((orderNoPrefix == "TMP" &&  status == "SAVE") || status == "UNCONFIRMED" ||
			(inProcessing && (status == "SAVE"  || status == "SIGNING" || status == "REJECT" ))){
	    	vat.bean().vatBeanOther.formAction = formAction;
			vat.block.submit(function(){
				return "process_object_name=cmTransferAction"+ "&process_object_method_name=performTransaction";
					},{bind:true, link:true, other:true}
			);
		}else{
	    	alert("您的表單已加入待辦事件，請從待辦事件選取後，再次送出!");
	    }

	}
}


// 刷新頁面
function refreshForm(vsfunctionCode){
	document.forms[0]["#formId"            ].value = vsfunctionCode.substring(3);
	document.forms[0]["#orderTypeCode"     ].value = vsfunctionCode.substring(0,3);
	document.forms[0]["#processId"         ].value = "";
	document.forms[0]["#assignmentId"      ].value = "";
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.orderTypeCode= document.forms[0]["#orderTypeCode"].value;
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=cmTransferAction&process_object_method_name=performInitial";
     	},{other      : true,
     	   funcSuccess:function(){
     			vat.item.bindAll();
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);

				refreshWfParameter(vat.item.getValueByName("#F.brandCode"),
	           					   vat.item.getValueByName("#F.orderTypeCode"),
	           					   vat.item.getValueByName("#F.orderNo"));
				vat.block.pageRefresh(102);
	        	vat.tabm.displayToggle(0, "xTab3", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);

				doFormAccessControl();
     	}});


}

// 動態改變員工名稱
function changeEmployNameByCode(code){
	vat.ajax.XHRequest({
          post:"process_object_name=cmMovementService"+
                   "&process_object_method_name=getAJAXEmployName"+
                   "&brandCode=" + document.forms[0]["#loginBrandCode"    ].value +
                   "&applicantCode=" + ( "#F.applicantCode" === code ? vat.item.getValueByName("#F.applicantCode") : "" )+
                   "&deliverymanCode=" + ( "#F.deliverymanCode" === code ? vat.item.getValueByName("#F.deliverymanCode") : "" ),
          find: function change(oXHR){
          		if( "#F.applicantCode" === code ){
          			vat.item.setValueByName("#F.applicantCode", vat.ajax.getValue("applicantCode", oXHR.responseText));
          			vat.item.setValueByName("#F.applicantName", vat.ajax.getValue("applicantName", oXHR.responseText));
          		} else if( "#F.deliverymanCode" === code ){
          			vat.item.setValueByName("#F.deliverymanCode", vat.ajax.getValue("deliverymanCode", oXHR.responseText));
          			vat.item.setValueByName("#F.deliverymanName", vat.ajax.getValue("deliverymanName", oXHR.responseText));
          		}
          },
          fail: function changeError(){
          		vat.item.setValueByName(code, "查無此員工");
          }
	});

}

// 依狀態鎖form
function doFormAccessControl(){
	var status 		= vat.item.getValueByName("#F.status");
	var orderNoPrefix	= vat.item.getValueByName("#F.transferOrderNo").substring(0,3);
	var processId = null;
	if( vat.bean().vatBeanOther.processId != null ){
		processId	= vat.bean().vatBeanOther.processId;
	}

	vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true, true);

	//===========================buttonLine==============================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.print", 	"display", "inline");
	vat.item.setStyleByName("#B.reducePrint", 	"display", "inline");
	vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	if(orderNoPrefix == "TMP" ){
		vat.item.setStyleByName("#B.transferPrint", "display", "none");
	}

	if(status != "FINISH" && status != "SIGNING"){
		vat.item.setStyleByName("#B.reverter", 	"display", "none");
	}
	// 流程內
	if( processId != null && processId != 0 ){ //從待辦事項進入
		vat.item.setStyleByName("#B.new", 		"display", "none");
		vat.item.setStyleByName("#B.search", 	"display", "none");
	}else{ // 查詢回來
		if(orderNoPrefix == "TMP" ){

		}else{
			vat.item.setStyleByName("#B.submit", 	"display", "none");
			vat.item.setStyleByName("#B.save", 		"display", "none");
			vat.item.setStyleByName("#B.message", 	"display", "none");
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
		}
	}

	if( status == "FINISH" || status == "VOID" || status == "CLOSE" ){
		vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
		vat.item.setStyleByName("#B.submit", 		"display", "none");
		vat.item.setStyleByName("#B.save"		, "display", "none");
		vat.item.setStyleByName("#B.message" 	, "display", "none");
	}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" +
		"?programId=CM_MOVEMENT" +
		"&levelType=ERROR" +
        "&processObjectName=cmMovementService" +
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// change formStatus
function changeFormStatus(formId, processId, status, formAction){
    var formStatus = "";
    if(formId === null || formId === "" ){
        formStatus = "SAVE";
    }else if(processId !== null && processId !== "" && processId !== 0){
        if(status == "SAVE" ){
            formStatus = "FINISH";
        }
    }
    return formStatus;
}

// 列印（貨櫃（物）運送單）
function openTransferReportWindow(type){
	//alert("do it.....");
	vat.bean().vatBeanOther.brandCode  = "T2";
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.transferOrderNo").substring(0,3);
    if("AFTER_SUBMIT"!=type)
    	vat.bean().vatBeanOther.transferOrderNo  = vat.item.getValueByName("#F.transferOrderNo");

    // 更新列印時間
    vat.block.submit(function(){
    		return "process_object_name=cmTransferService" + "&process_object_method_name=updateCmTransferPrintTime";}, {bind:true, link:true, other:true});
	vat.bean().vatBeanOther.reportFileName  = "IM0186.rpt";
	vat.block.submit(function(){return "process_object_name=cmMovementService"+
									"&process_object_method_name=getTransferReduceReportConfig";},{other:true,
				                    funcSuccess:function(){
				                    	//alert(vat.bean().vatBeanOther.reportUrl);
						        		eval(vat.bean().vatBeanOther.reportUrl);
						        	}
									}
		);

	//alert("列印～～");
	if("AFTER_SUBMIT"==type) createRefreshForm();
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].transferOrderNo;
		  refreshForm(vsfunctionCode);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}