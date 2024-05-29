﻿/***
 *	檔案: cmMovement.js
 *	說明：移倉單
 *  <pre>
 *  	Created by david
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
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","移倉單主檔" ,"vatMasterDiv"        		,"images/tab_master_data_dark.gif"      	,"images/tab_master_data_light.gif", false, "doPageDataSave("+vnB_Master+")");
		vat.tabm.createButton(0 ,"xTab2","移倉單明細檔" ,"vatDetailDiv"        	,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false, "doPageDataSave("+vnB_Detail+")");
		vat.tabm.createButton(0,"xTab3","簽核資料"   ,"vatApprovalDiv"             ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif", vat.item.getValueByName("#F.status") == "SAVE" || vat.item.getValueByName("#F.status") == "UNCONFIRMED" ? "none" : "inline");
  	}
  	kweMaster();
	kweDetail();

	kweWfBlock(vat.item.getValueByName("#F.brandCode"),
	           vat.item.getValueByName("#F.orderTypeCode"),
	           vat.item.getValueByName("#F.orderNo"),
	           document.forms[0]["#loginEmployeeCode"].value );
	checkCustomsStatus();
	doFormAccessControl();
	 
	 
	if(vat.item.getValueByName("#F.orderTypeCode")=='RMK'||vat.item.getValueByName("#F.orderTypeCode")=='RMM'||vat.item.getValueByName("#F.orderTypeCode")=='RMW'){
	   checkUploadControl();
	}else if(vat.item.getValueByName("#F.orderTypeCode") == 'RMV'||vat.item.getValueByName("#F.orderTypeCode") == 'RVM'||vat.item.getValueByName("#F.orderTypeCode") == 'RWD'||vat.item.getValueByName("#F.orderTypeCode")=='RDW'||vat.item.getValueByName("#F.orderTypeCode")=='RKW'||vat.item.getValueByName("#F.orderTypeCode")=='RWK'){
	   checkUploadControl1();
	}
	getCustomsDesc();
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
				return "process_object_name=cmMovementAction&process_object_method_name=performInitial";
	    	},{
	    		other: true
    	});
  	}
}

function checkCustomsStatus(){
	//alert(vat.item.getValueByName("#F.customsStatusHidden"));
	var statusHidden = vat.item.getValueByName("#F.customsStatusHidden");
	var cusStatusHidden = statusHidden.substring(0, 1);
	//alert("cusStatusHidden:"+cusStatusHidden);
	if(cusStatusHidden !== ""){
	 	if(cusStatusHidden == "A"){
	 		vat.item.setValueByName("#F.customsStatus", "待上傳");
	 	}
	 	if(cusStatusHidden == "N"){
	 		vat.item.setValueByName("#F.customsStatus", "已上傳成功");
	 	}
	 	if(cusStatusHidden == "E"){
	 		vat.item.setValueByName("#F.customsStatus", "上傳錯誤");
	 	}
	 }else{
	    
	 	vat.item.setValueByName("#F.customsStatus", "未上傳");
	 }
}

function getCustomsDesc(){
	//alert(vat.item.getValueByName("#F.customsStatusHidden"));
	var customsDesc = "";
	if(vat.item.getValueByName("#F.customsStatusHidden")!==""){
		vat.ajax.XHRequest({
          post:"process_object_name=imMovementMainService"+
                   "&process_object_method_name=getCustomsProcessResponse"+
                   "&customsStatus=" + vat.item.getValueByName("#F.customsStatusHidden"),
          find: function change(oXHR){
          		//alert("success");
          		var response = vat.ajax.getValue("response" , oXHR.responseText);
          		vat.item.setValueByName("#F.customsDesc", response);
          		//alert(response);
          },
          fail: function changeError(){
          		//alert("fail");
          }
		});
	}else{
		//alert("NULL");
	}
}

function checkUploadControl(){
	var status = vat.item.getValueByName("#F.status");
	var tranRecordStatus = vat.item.getValueByName("#F.tranRecordStatus");
	var orderTypeCode = vat.item.getValueByName("#F.orderType");
	var customsStatus = vat.item.getValueByName("#F.customsStatusHidden");
	var tranAllowUpload = vat.item.getValueByName("#F.tranAllowUpload");
	var cStatus = customsStatus.substring(0, 1);
	var RecordStatus = tranRecordStatus.substring(0, 4);
	
	if((status === "SIGNING") && RecordStatus !== "DF15" && (customsStatus === "" || cStatus === "E")){
		vat.item.setStyleByName("#B.sendCustoms","display","inline");
	}else if(status === "FINISH" && ((customsStatus !== "N14" && cStatus === "N" && RecordStatus !== "DF15") || ( cStatus === "E" && tranRecordStatus === "DF15")) && (orderTypeCode == "RSF" || orderTypeCode == "RMK" || orderTypeCode == "RMM"|| orderTypeCode == "RWD"|| orderTypeCode == "RDW"|| orderTypeCode == "RMV"|| orderTypeCode == "RVM")){
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendBack","display","inline");
	}else if(((status === "FINISH" && customsStatus === "N23" && tranAllowUpload === "") || (status === "FINISH" && cStatus === "E" && tranAllowUpload === "E"  && tranRecordStatus !== "DF10"&& tranRecordStatus !== "NF10"))){
		vat.item.setStyleByName("#B.sendCancel","display","inline");
		vat.item.setStyleByName("#B.sendBack","display","none");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
	}else{
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendBack","display","none");
		vat.item.setStyleByName("#B.sendCancel","display","none");
	}
	if((status === "FINISH" && (customsStatus === "N13" || customsStatus === "N24") && tranAllowUpload === "" && tranRecordStatus === "DF10")){
		vat.item.setStyleByName("#B.sendCancel","display","inline");
		vat.item.setStyleByName("#B.sendBack","display","inline");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
	}
	if((status === "FINISH" && (customsStatus === "N13" || customsStatus === "N24") && tranAllowUpload === "N24" && tranRecordStatus === "DF15")){
		vat.item.setStyleByName("#B.sendCancel","display","inline");
		vat.item.setStyleByName("#B.sendBack","display","inline");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
	}
	vat.item.setStyleByName("#B.sendBack3","display","none");
	
}

function checkUploadControl1(){
	var status = vat.item.getValueByName("#F.status");
	var tranRecordStatus = vat.item.getValueByName("#F.tranRecordStatus");
	var orderTypeCode = vat.item.getValueByName("#F.orderType");
	var customsStatus = vat.item.getValueByName("#F.customsStatusHidden");
	var tranAllowUpload = vat.item.getValueByName("#F.tranAllowUpload");
	var cStatus = customsStatus.substring(0, 1);
	var RecordStatus = tranRecordStatus.substring(0, 4);
	var nfcStatus = vat.item.getValueByName("#F.cStatus");
	
	if((status === "SIGNING") && RecordStatus !== "NF14" && (customsStatus === "" || cStatus === "E")){
		vat.item.setStyleByName("#B.sendCustoms","display","inline");
	}else if(status === "FINISH" && ((customsStatus !== "N14" && cStatus === "N" && RecordStatus !== "NF14") || ( cStatus === "E" && tranRecordStatus === "NF14")) && (orderTypeCode == "WMF" || orderTypeCode == "RWD"|| orderTypeCode == "RDW"|| orderTypeCode == "RMV"|| orderTypeCode == "RVM")){
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendBack","display","inline");
	}else if(((status === "FINISH" && customsStatus === "N23" && tranAllowUpload === "") || (status === "FINISH" && cStatus === "E" && tranAllowUpload === "E" && tranRecordStatus !== "NF10"))){
		vat.item.setStyleByName("#B.sendCancel","display","inline");
		vat.item.setStyleByName("#B.sendBack","display","none");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
	}else{
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendBack","display","none");
		vat.item.setStyleByName("#B.sendCancel","display","none");
	}
	if((status === "FINISH" && (customsStatus === "N13" || customsStatus === "N24") && tranAllowUpload === "" && tranRecordStatus === "NF10")){
		vat.item.setStyleByName("#B.sendCancel","display","inline");
		vat.item.setStyleByName("#B.sendBack","display","inline");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
	}
	if((status === "FINISH" && (customsStatus === "N13" || customsStatus === "N24") && tranAllowUpload === "N24" && tranRecordStatus === "NF14")){
		vat.item.setStyleByName("#B.sendCancel","display","inline");
		vat.item.setStyleByName("#B.sendBack","display","inline");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
	}
	if(orderTypeCode==="RWD"||orderTypeCode==="RDW"||orderTypeCode==="RWK"||orderTypeCode==="RKW"||orderTypeCode==="RMV"){
	    vat.item.setStyleByName("#B.sendCancel","display","none");
		vat.item.setStyleByName("#B.sendBack3","display","none");
		vat.item.setStyleByName("#B.sendCustoms","display","none");
		vat.item.setStyleByName("#B.sendCancel1","display","none");
		vat.item.setStyleByName("#B.sendCustoms1","display","none");
		vat.item.setStyleByName("#B.updateApplicationNo","display","none");
	}
	
	if(nfcStatus=='O'){
	  vat.item.setStyleByName("#B.sendBack1","display","inline");
	  vat.item.setStyleByName("#B.sendCancel1","display","inline");
	  vat.item.setStyleByName("#B.sendBack","display","none");
	}else if(nfcStatus=='I'){
	  vat.item.setStyleByName("#B.sendBack1","display","none");
	  vat.item.setStyleByName("#B.sendBack","display","none");
	}else{
	  vat.item.setStyleByName("#B.sendBack1","display","none");
	  vat.item.setStyleByName("#B.sendBack","display","none");
	}
}

flag=0;

function showButton(){
   
	var status = vat.item.getValueByName("#F.status");
	var tranRecordStatus = vat.item.getValueByName("#F.tranRecordStatus");
	var orderTypeCode = vat.item.getValueByName("#F.orderType");
	var customsStatus = vat.item.getValueByName("#F.customsStatusHidden");
	var tranAllowUpload = vat.item.getValueByName("#F.tranAllowUpload");
	var cStatus = customsStatus.substring(0, 1);
	var RecordStatus = tranRecordStatus.substring(0, 4);
	var nfcStatus = vat.item.getValueByName("#F.cStatus");
   
	if(flag==0){
		flag=1;  
	}else if(flag==1){
		flag=0; 
	} 
    
    var orderTypeCode = vat.item.getValueByName("#F.orderType");
    var status = vat.item.getValueByName("#F.status");
    if(orderTypeCode==="RWD"||orderTypeCode==="RDW"||orderTypeCode==="RWK"||orderTypeCode==="RKW"|| orderTypeCode == "RMV"|| orderTypeCode == "RVM"|| orderTypeCode == "RMM"|| orderTypeCode == "RMK"|| orderTypeCode == "RMW"){
		if(flag==1){
			if(status=='SIGNING'){
				if(customsStatus==''){
					vat.item.setStyleByName("#B.sendCustoms1","display","inline");
				}
			}else{
				vat.item.setStyleByName("#B.sendCustoms1","display","none");   
			}

			if(status!='SAVE'){
				if(customsStatus=='N13'){
			        vat.item.setStyleByName("#B.sendCancel1","display","inline");
			        vat.item.setStyleByName("#B.sendBack3","display","inline");
				    vat.item.setStyleByName("#B.sendBack1","display","none");
				    vat.item.setStyleByName("#B.sendBack","display","none");
				}else if(customsStatus=='N23'&cStatus === "O"){
				    vat.item.setStyleByName("#B.sendCancel1","display","inline");
			        vat.item.setStyleByName("#B.sendBack3","display","none");
				    vat.item.setStyleByName("#B.sendBack1","display","inline");
				    vat.item.setStyleByName("#B.sendBack","display","none");
				}else{	
				    vat.item.setStyleByName("#B.sendBack3","display","none");
				    vat.item.setStyleByName("#B.sendBack1","display","none");
				    vat.item.setStyleByName("#B.updateApplicationNo","display","none");
				    vat.item.setStyleByName("#B.sendBack","display","none");
				    vat.item.setStyleByName("#B.sendCancel1","display","none");
				}
				if(orderTypeCode == "RMM"|| orderTypeCode == "RMK"|| orderTypeCode == "RMW"){
					vat.item.setStyleByName("#B.sendBack1","display","inline");
				}
			}
		
		}else if(flag==0){
			if(status!='SAVE'){
			    vat.item.setStyleByName("#B.sendBack3","display","none");
				vat.item.setStyleByName("#B.sendCancel1","display","none");
				vat.item.setStyleByName("#B.sendBack1","display","none");
				vat.item.setStyleByName("#B.sendBack","display","none");
				vat.item.setStyleByName("#B.sendCustoms1","display","none");
				vat.item.setStyleByName("#B.updateApplicationNo","display","none");
		 	}
			if(status=='SAVE'){
		        if(customsStatus==''){
		          vat.item.setStyleByName("#B.sendCancel1","display","none");
		          vat.item.setStyleByName("#B.sendCustoms1","display","none");
		        }
	     	}else{
		       vat.item.setStyleByName("#B.sendCancel1","display","none");
		       vat.item.setStyleByName("#B.sendCustoms1","display","none");   
			}
		}
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
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  ,
	 									 openMode:"open",
	 									 service:"Cm_Movement:search:20091001.page",    // ?orderTypeCode="+vat.bean().vatBeanOther.orderTypeCode
	 									 left:0, right:0, width:1024, height:768,
	 									 servicePassData:function(){ return doPassData("buttonLine");},
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"saveSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.transferPrint"	, type:"IMG"    ,value:"單據列印(運送單)",   src:"./images/button_transport.gif" , eClick:'openTransferReportWindow("")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.reducePrint"       , type:"IMG"    ,value:"單據列印(縮小清單)",   src:"./images/button_reduce_list.gif" , eClick:'openReportReduceWindow("")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.message"    , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	            {name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  ,
	 									 openMode:"open",
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,
	 									 serviceAfterPick:function(){afterImportSuccess()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit1"      , type:"IMG"    ,value:"儲存移倉申請書號碼",   src:"./images/saveWhNo.jpg", eClick:'doSaveWhNo()'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.errHandle"        , type:"IMG"    ,value:"異常處理",src:"./images/button_process.gif"    , eClick:'showButton()'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendCustoms"        , type:"IMG"    ,value:"移倉許可",   src:"./images/send_customs_mz.jpg"  ,eClick:'chgStatus("DF10")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendCancel"        , type:"IMG"    ,value:"註銷上傳",src:"./images/send_customs3.jpg"    , eClick:'chgStatus("cancel")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendCustoms1"        , type:"IMG"    ,value:"移倉許可",   src:"./images/send_customs_in.jpg"  ,eClick:'chgStatus("NF10")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendBack1"        , type:"IMG"    ,value:"點驗進倉",   src:"./images/send_customs2.jpg"  ,eClick:'chgStatus("NF14I")'},	 									 
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendBack3"        , type:"IMG"    ,value:"點驗出倉",   src:"./images/send_customs4.jpg"  ,eClick:'chgStatus("NF14O")'},	 									 
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendCancel1"        , type:"IMG"    ,value:"註銷上傳",src:"./images/send_customs3.jpg"    , eClick:'chgStatus("cancel")'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.txtExportOut"        , type:"IMG"    ,value:"TXT文字檔OUT",src:"./images/txt_export_out.jpg"    , eClick:'executeTxt("OUT")'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.updateApplicationNo"        , type:"IMG"    ,value:"重取申請書號碼",src:"./images/updateApplicationNo.jpg"    , eClick:'updateApplicationNo("CM")'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
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
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

// 移倉單主檔
function kweHeader(){
	var allOrderTypeCodes = vat.bean("allOrderTypeCodes");
	var allTaxTypes = vat.bean("allTaxTypes");

	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"移倉單功能維護作業",
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 		type:"LABEL", 	value:"單別"}]},
				{items:[{name:"#F.orderTypeCode", 		type:"SELECT", 	bind:"orderTypeCode", init:allOrderTypeCodes, size:15, mode:"READONLY"},
	 		 			{name:"#F.orderType"   , type:"TEXT"  ,  bind:"orderTypeCode", size:6, mode:"HIDDEN"},
						{name:"#F.headId", 				type:"TEXT",  	bind:"headId", back:false, mode:"HIDDEN"}]},
				{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"移倉單號" }]},
				{items:[{name:"#F.orderNo", 			type:"TEXT", 	bind:"orderNo", size:10, mode:"READONLY",size:20}]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", size:6, mode:"HIDDEN"},
	 					{name:"#F.brandName", 			type:"TEXT"  ,  bind:"brandName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.status", 				type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 				type:"TEXT", 	bind:"status", size:15, mode:"HIDDEN"},
						{name:"#F.statusName", 			type:"TEXT",  bind:"statusName", back:false, mode:"READONLY"},
						{name:"#F.customsDesc"      , type:"TEXT"  , size:35 , mode:"READONLY"},
	  		 			{name:"#F.tranRecordStatus"  , type:"TEXT"  ,  bind:"tranRecordStatus", mode:"HIDDEN"},
	  		 			{name:"#F.tranAllowUpload"  , type:"TEXT"  ,  bind:"tranAllowUpload", mode:"HIDDEN"},
	  		 			{name:"#F.customsStatus", 				type:"TEXT", mode:"READONLY"},
	  		 			{name:"#F.customsStatusHidden", 				type:"TEXT",  bind:"customsStatus", mode:"HIDDEN"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.deliveryCustomsWarehouse",	type:"LABEL", 	value:"撥出海關倉" }]},
				{items:[{name:"#F.deliveryCustomsWarehouse",	type:"TEXT", 	bind:"deliveryCustomsWarehouse", size:18, mode:"READONLY" }]},
				{items:[{name:"#L.arrivalCustomsWarehouse",		type:"LABEL", 	value:"撥入海關倉" }]},
				{items:[{name:"#F.arrivalCustomsWarehouse",		type:"TEXT", 	bind:"arrivalCustomsWarehouse", size:18, mode:"READONLY" }]},
				{items:[{name:"#L.createBy",					type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",					type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
						{name:"#F.createByName",				type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.creationDate",				type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",				type:"TEXT", 	bind:"creationDate", mode:"READONLY"}]}

			]},
			{row_style:"", cols:[
				{items:[{name:"#L.taxType", 		type:"LABEL", 	value:"稅別"}]},
	 			{items:[{name:"#F.taxType", 		type:"SELECT",  bind:"taxType",  init:allTaxTypes,	size:15, mode:"READONLY" }]},
	 			{items:[{name:"#L.remark1",			type:"LABEL", 	value:"備註" }]},
				{items:[{name:"#F.remark1",			type:"TEXT", 	bind:"remark1", size:40 }], td:" colSpan=3"},
				{items:[{name:"#L.moveWhNo"      , type:"LABEL", value:"移倉申請書號碼"}]},
 	 			{items:[{name:"#F.moveWhNo"      , type:"TEXT",   bind:"moveWhNo", mode:"READONLY", size:20}]},
 	 			{items:[{name:"#F.cStatus"      , type:"TEXT",   bind:"cStatus", mode:"HIDDEN"}]}
			]}
		],

		beginService:"",
		closeService:""
	});
}

// 移倉單主檔2
function kweMaster(){

	var allCustomsAreas = vat.bean("allCustomsAreas");
	var allSealTypes = vat.bean("allSealTypes");
	var allCarTypes = vat.bean("allCarTypes");
	var allExpenseTypes = vat.bean("allExpenseTypes");
	var allCarNos = vat.bean("allCarNos");

	var taxType = vat.item.getValueByName("#F.taxType");
	var transferValue = vat.bean("transfer") == ''  || vat.bean("transfer") == null ? "4C" : vat.bean("transfer"); // 運送單類型
	var ownerValue = vat.bean("owner") == '' || vat.bean("owner") == null ? "采盟股份有限公司" : vat.bean("owner"); // 貨主名稱
	var startStationValue = "";
	var toStationValue = "";
	if("RWD" == vat.item.getValueByName("#F.orderTypeCode")){
		startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;采盟保稅倉庫;CD198" : vat.bean("startStation"); // 起運站名
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;桃園機場;免稅商店" : vat.bean("toStation"); // 運往站名
	}else if( "RMM" == vat.item.getValueByName("#F.orderTypeCode")){
		startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;自用保稅倉庫;AD910" : vat.bean("startStation"); // 起運站名
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;福澳港;行政旅運大樓" : vat.bean("toStation"); // 運往站名
	}else if( "RMK" == vat.item.getValueByName("#F.orderTypeCode")){
		startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;自用保稅倉庫;AD910" : vat.bean("startStation"); // 起運站名
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;南竿機場;免稅購物商店" : vat.bean("toStation"); // 運往站名
	}else if( "RWK" == vat.item.getValueByName("#F.orderTypeCode")){
		startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;自用保稅倉庫;CD198" : vat.bean("startStation"); // 起運站名
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;小港機場;免稅購物商店" : vat.bean("toStation"); // 運往站名
	}else if( "RAP" == vat.item.getValueByName("#F.orderTypeCode")){
		startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;南竿機場;免稅購物商店" : vat.bean("startStation"); // 起運站名
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;福澳港;行政旅運大樓" : vat.bean("toStation"); // 運往站名
	}else if( "RPA" == vat.item.getValueByName("#F.orderTypeCode")){
		startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;福澳港;行政旅運大樓" : vat.bean("startStation"); // 起運站名
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;南竿機場;免稅購物商店" : vat.bean("toStation"); // 運往站名
	}else if( "RMV" == vat.item.getValueByName("#F.orderTypeCode")){
		startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;自用保稅倉庫;CD198" : vat.bean("startStation"); // 起運站名
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;福澳港;行政旅運大樓" : vat.bean("toStation"); // 運往站名
	}else if( "RVM" == vat.item.getValueByName("#F.orderTypeCode")){
		startStationValue = vat.bean("startStation") == '' || vat.bean("startStation") == null ? "采盟（股）;福澳港;行政旅運大樓" : vat.bean("startStation"); // 起運站名
		toStationValue = vat.bean("toStation") == '' || vat.bean("toStation") == null ? "采盟（股）;自用保稅倉庫;CD198" : vat.bean("toStation"); // 運往站名
	}

	var vehicleStationValue = vat.bean("vehicleStation") == '' || vat.bean("vehicleStation") == null ? "" : vat.bean("vehicleStation"); // 車行名稱
	var vehicleNoValue =  vat.bean("vehicleNo") == '' || vat.bean("vehicleNo") == null ? "" : vat.bean("vehicleNo"); // 車號
	var driverLicenceValue =  vat.bean("driverLicence") == '' || vat.bean("driverLicence") == null ? "" : vat.bean("driverLicence"); // 駕照號碼
	var trackValue =  vat.bean("track") == '' || vat.bean("track") == null ? "保稅貨物" : vat.bean("track"); // 落地追蹤

	var transderNo = vat.bean("transferOrderNo") =='' || vat.bean("transferOrderNo") == null ? '' : vat.bean("transferOrderNo"); // 運送單號
	var passNoValue =  vat.bean("passNo") == '' || vat.bean("passNo") == null ? transderNo : vat.bean("passNo"); // 放行單號
	var airplaneNoValue = vat.bean("airplaneNo"); // 航機班次
	var masterNoValue = vat.bean("masterNo"); // 主號
	var secondNoValue = vat.bean("secondNo"); // 分號
	var clearanceValue = vat.bean("clearance"); // 通關方式
	var releaseValue = vat.bean("release"); // 放行附帶條件
	var defaultCheck = "<font color='red'>*</font>";

	var leaveTimeTValue =  vat.bean("leaveTimeT") == '' || vat.bean("leaveTimeT") == null ? "" : vat.bean("leaveTimeT"); // 出站時間
	var arriveTimeTValue =  vat.bean("arriveTimeT") == '' || vat.bean("arriveTimeT") == null ? "" : vat.bean("arriveTimeT"); // 到站時間
	if( "P" == taxType ){
		defaultCheck = "";
	}
	if("RVM" == vat.item.getValueByName("#F.orderTypeCode")||"RMV" == vat.item.getValueByName("#F.orderTypeCode") ||"RWD" == vat.item.getValueByName("#F.orderTypeCode") || "RMK" == vat.item.getValueByName("#F.orderTypeCode")
	|| "RAP" == vat.item.getValueByName("#F.orderTypeCode")|| "RPA" == vat.item.getValueByName("#F.orderTypeCode")|| "RMM" == vat.item.getValueByName("#F.orderTypeCode") || "RWK" == vat.item.getValueByName("#F.orderTypeCode")){
		vat.block.create( vnB_Master, {
		id: "vatMasterDiv", table:"cellspacing='1' class='default' border='0' cellpadding='3'",
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.deliveryDate", 	type:"LABEL", 	value:"移倉日期<font color='red'>*</font>" }]},
				{items:[{name:"#F.deliveryDate", 	type:"Date", 	bind:"deliveryDate", size:15 }], td:" colSpan=4"},
				{items:[{name:"#L.deliveryDate", 	type:"LABEL", 	value:"上傳海關的移倉日期，以按下上傳按鈕後的十分鐘為準。" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customsArea",		type:"LABEL", 	value:"關別"+defaultCheck }]},
				{items:[{name:"#F.customsArea",		type:"SELECT", 	bind:"customsArea", init:allCustomsAreas }]},
	 		 	{items:[{name:"#L.passNo",			type:"LABEL", 	value:"放行單號"+defaultCheck }]},
				{items:[{name:"#F.passNo",			type:"TEXT", 	bind:"passNo", size:15, init:passNoValue/*, mode:"READONLY"*/ }]},
				{items:[{name:"#L.applicantCode",	type:"LABEL", 	value:"申請人<font color='red'>*</font>" }]},
				{items:[{name:"#F.applicantCode",	type:"TEXT", 	bind:"applicantCode", size:15, eChange: function(){ changeEmployNameByCode("#F.applicantCode"); } },
						{name:"#B.applicantCode",	value:"選取" ,type:"PICKER" , size:15 , src:"./images/start_node_16.gif",
	 									 			openMode:"open",
	 									 			service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			serviceAfterPick:function(){doAfterPickerFunctionProcess("#F.applicantCode");} },
	 					{name:"#F.applicantName", 	type:"TEXT",  	bind:"applicantName", back:false, mode:"READONLY"} ]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.sealType",		type:"LABEL", 	value:"封條類別"+defaultCheck }]},
				{items:[{name:"#F.sealType",		type:"SELECT", 	bind:"sealType", init:allSealTypes }]},
				{items:[{name:"#L.sealNo",			type:"LABEL", 	value:"封條號碼"+defaultCheck }]},
				{items:[{name:"#F.sealNo",			type:"TEXT", 	bind:"sealNo", size:15}]},
				{items:[{name:"#L.deliverymanCode",	type:"LABEL", 	value:"主管<font color='red'>*</font>" }]},
				{items:[{name:"#F.deliverymanCode",	type:"TEXT", 	bind:"deliverymanCode", size:15, eChange: function(){ changeEmployNameByCode("#F.deliverymanCode"); } },
						{name:"#B.deliverymanCode",	value:"選取" ,type:"PICKER" , size:15 , src:"./images/start_node_16.gif",
	 									 			openMode:"open",
	 									 			service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			serviceAfterPick:function(){doAfterPickerFunctionProcess("#F.deliverymanCode");} },
	 					{name:"#F.deliverymanName", type:"TEXT",  	bind:"deliverymanName", back:false, mode:"READONLY"} ]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.carType",			type:"LABEL", 	value:"保卡類別"+defaultCheck }]},
				{items:[{name:"#F.carType",			type:"SELECT", 	bind:"carType", init:allCarTypes}]},
				{items:[{name:"#L.carNo",			type:"LABEL", 	value:"保卡號碼"+defaultCheck }]},
				{items:[{name:"#F.carNo",			type:"TEXT", 	bind:"carNo" , size:15, eChange:"changeValue()"}]}, // , init:allCarNos
				//{items:[{name:"#F.carNo",			type:"SELECT", 	bind:"carNo" , init:allCarNos ,size:15, eChange:"changeValue()"}]}, // , init:allCarNos
				{items:[{name:"#L.driverCode",		type:"LABEL", 	value:"司機人<font color='red'>*</font>" }]},
				{items:[{name:"#F.driverCode",		type:"TEXT", 	bind:"driverCode", size:15 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.expenseType",		type:"LABEL", 	value:"規費類別"+defaultCheck }]},
				{items:[{name:"#F.expenseType",		type:"SELECT", 	bind:"expenseType", init:allExpenseTypes}]},
				{items:[{name:"#L.expenseNo",		type:"LABEL", 	value:"規費單號"+defaultCheck }]},
				{items:[{name:"#F.expenseNo",		type:"TEXT", 	bind:"expenseNo", size:15}]},
				{items:[{name:"#L.expenseAmount",	type:"LABEL", 	value:"規費金額"+defaultCheck }]},
				{items:[{name:"#F.expenseAmount",	type:"NUMB", 	bind:"expenseAmount", size:15 , maxLen:4 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.transfer",		type:"LABEL", 	value:"運送裝箱"+defaultCheck }]},
				{items:[{name:"#F.transfer",		type:"TEXT", 	bind:"transfer", size:4, init:transferValue},
					    {name:"#L.transferNote"   , type:"LABEL", 	value:"1.進口 2.轉運 3.轉口 4.出口<BR>A.重櫃 B空櫃 C.非櫃裝"}]},
				{items:[{name:"#L.owner",			type:"LABEL", 	value:"貨主名稱"+defaultCheck }]},
				{items:[{name:"#F.owner",			type:"TEXT", 	bind:"owner", size:15, init:ownerValue}]},
				{items:[{name:"#L.driverLicence",	type:"LABEL", 	value:"司機駕照號碼"+defaultCheck }]},
				{items:[{name:"#F.driverLicence",	type:"TEXT", 	bind:"driverLicence", size:15, init:driverLicenceValue}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.startStation",	type:"LABEL", 	value:"起運站名"+defaultCheck }]},
				{items:[{name:"#F.startStation",	type:"TEXT", 	bind:"startStation", size:40 , init:startStationValue}]},
				{items:[{name:"#L.toStation",		type:"LABEL", 	value:"運往站名"+defaultCheck }]},
				{items:[{name:"#F.toStation",		type:"TEXT", 	bind:"toStation", size:40, init:toStationValue}]},
				{items:[{name:"#L.vehicleNo",		type:"LABEL", 	value:"車號"+defaultCheck }]},
				{items:[{name:"#F.vehicleNo",		type:"TEXT", 	bind:"vehicleNo", size:15 , init:vehicleNoValue}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.leaveTimeT",		type:"LABEL", 	value:"出站時間" }]},
				{items:[{name:"#F.leaveTimeT",		type:"TEXT", 	bind:"leaveTimeT", size:20},
						{name:"#L.leaveTimeTNote", 	type:"LABEL", 	value:" 時間格式：YYYMMDDhhmm",size:11, maxLen:11, init:leaveTimeTValue}], td:" colSpan=2"},
				{items:[{name:"#L.arriveTimeT",		type:"LABEL", 	value:"進站時間" }]},
				{items:[{name:"#F.arriveTimeT",		type:"TEXT", 	bind:"arriveTimeT", size:20},
						{name:"#L.arriveTimeTNote", type:"LABEL", 	value:" 時間格式：YYYMMDDhhmm",size:11, maxLen:11, init:arriveTimeTValue}], td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.vehicleStation",	type:"LABEL", 	value:"車行名稱"+defaultCheck }]},
				{items:[{name:"#F.vehicleStation",	type:"TEXT", 	bind:"vehicleStation", size:40, init:vehicleStationValue}]},
				{items:[{name:"#L.track",			type:"LABEL", 	value:"落地追蹤"+defaultCheck }]},
				{items:[{name:"#F.track",			type:"TEXT", 	bind:"track", size:15, init:trackValue}]},
				{items:[{name:"#L.transferOrderNo",	type:"LABEL", 	value:"運送單號"+defaultCheck }]},
				{items:[{name:"#F.transferOrderNo",	type:"TEXT", 	bind:"transferOrderNo", size:15, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.airplaneNo",		type:"LABEL", 	value:"航機班次" }]},
				{items:[{name:"#F.airplaneNo",		type:"TEXT", 	bind:"airplaneNo", size:15, init:airplaneNoValue}]},
				{items:[{name:"#L.masterNo",		type:"LABEL", 	value:"主號" }]},
				{items:[{name:"#F.masterNo",		type:"TEXT", 	bind:"masterNo", size:10 , init:masterNoValue}]},
				{items:[{name:"#L.secondNo",		type:"LABEL", 	value:"分號" }]},
				{items:[{name:"#F.secondNo",		type:"TEXT", 	bind:"secondNo", size:10, init:secondNoValue}]}

			]},
			{row_style:"", cols:[
				{items:[{name:"#L.clearance",		type:"LABEL", 	value:"通關方式" }]},
				{items:[{name:"#F.clearance",		type:"TEXT", 	bind:"clearance", size:10, init:clearanceValue}]},
				{items:[{name:"#L.release",			type:"LABEL", 	value:"放行附帶條件" }]},
				{items:[{name:"#F.release",			type:"TEXT", 	bind:"release", size:10, init:releaseValue}], td:" colSpan=3"}

			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customsOutTime",		type:"LABEL", 	value:"點驗出倉時間" }]},
				{items:[{name:"#F.customsOutTime",		type:"TEXT", 	bind:"customsOutTime", mode:"readOnly", size:20}], td:" colSpan=2"},
				
				{items:[{name:"#L.customsInTime",		type:"LABEL", 	value:"點驗進倉時間" }]},
				{items:[{name:"#F.customsInTime",		type:"TEXT", 	bind:"customsInTime", mode:"readOnly", size:20}], td:" colSpan=2"}
			]}
		],

		beginService:"",
		closeService:""
		});
	}else{
		vat.block.create( vnB_Master, {
		id: "vatMasterDiv", table:"cellspacing='1' class='default' border='0' cellpadding='3'",
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.deliveryDate", 	type:"LABEL", 	value:"移倉日期<font color='red'>*</font>" }]},
				{items:[{name:"#F.deliveryDate", 	type:"Date", 	bind:"deliveryDate", size:15 }], td:" colSpan=4"}				
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customsArea",		type:"LABEL", 	value:"關別"+defaultCheck }]},
				{items:[{name:"#F.customsArea",		type:"SELECT", 	bind:"customsArea", init:allCustomsAreas }]},
	 		 	{items:[{name:"#L.passNo",			type:"LABEL", 	value:"放行單號"+defaultCheck }]},
				{items:[{name:"#F.passNo",			type:"TEXT", 	bind:"passNo", size:15, init:passNoValue/*, mode:"READONLY"*/ }]},
				{items:[{name:"#L.applicantCode",	type:"LABEL", 	value:"申請人<font color='red'>*</font>" }]},
				{items:[{name:"#F.applicantCode",	type:"TEXT", 	bind:"applicantCode", size:15, eChange: function(){ changeEmployNameByCode("#F.applicantCode"); } },
						{name:"#B.applicantCode",	value:"選取" ,type:"PICKER" , size:15 , src:"./images/start_node_16.gif",
	 									 			openMode:"open",
	 									 			service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			serviceAfterPick:function(){doAfterPickerFunctionProcess("#F.applicantCode");} },
	 					{name:"#F.applicantName", 	type:"TEXT",  	bind:"applicantName", back:false, mode:"READONLY"} ]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.sealType",		type:"LABEL", 	value:"封條類別"+defaultCheck }]},
				{items:[{name:"#F.sealType",		type:"SELECT", 	bind:"sealType", init:allSealTypes }]},
				{items:[{name:"#L.sealNo",			type:"LABEL", 	value:"封條號碼"+defaultCheck }]},
				{items:[{name:"#F.sealNo",			type:"TEXT", 	bind:"sealNo", size:15}]},
				{items:[{name:"#L.deliverymanCode",	type:"LABEL", 	value:"主管<font color='red'>*</font>" }]},
				{items:[{name:"#F.deliverymanCode",	type:"TEXT", 	bind:"deliverymanCode", size:15, eChange: function(){ changeEmployNameByCode("#F.deliverymanCode"); } },
						{name:"#B.deliverymanCode",	value:"選取" ,type:"PICKER" , size:15 , src:"./images/start_node_16.gif",
	 									 			openMode:"open",
	 									 			service:"Bu_AddressBook:searchEmployee:20090811.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			serviceAfterPick:function(){doAfterPickerFunctionProcess("#F.deliverymanCode");} },
	 					{name:"#F.deliverymanName", type:"TEXT",  	bind:"deliverymanName", back:false, mode:"READONLY"} ]}
			]},//oooooooo
			{row_style:"", cols:[
				{items:[{name:"#L.carType",			type:"LABEL", 	value:"保卡類別"+defaultCheck }]},
				{items:[{name:"#F.carType",			type:"SELECT", 	bind:"carType", init:allCarTypes}]},
				{items:[{name:"#L.carNo",			type:"LABEL", 	value:"保卡號碼"+defaultCheck }]},
				{items:[{name:"#F.carNo",			type:"TEXT", 	bind:"carNo", size:15}]}, // , init:allCarNos
				//{items:[{name:"#F.carNo",			type:"SELECT", 	bind:"carNo" , init:allCarNos ,size:15, eChange:"changeValue()"}]}, // , init:allCarNos
				{items:[{name:"#L.driverCode",		type:"LABEL", 	value:"司機人<font color='red'>*</font>" }]},
				{items:[{name:"#F.driverCode",		type:"TEXT", 	bind:"driverCode", size:15 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.expenseType",		type:"LABEL", 	value:"規費類別"+defaultCheck }]},
				{items:[{name:"#F.expenseType",		type:"SELECT", 	bind:"expenseType", init:allExpenseTypes}]},
				{items:[{name:"#L.expenseNo",		type:"LABEL", 	value:"規費單號"+defaultCheck }]},
				{items:[{name:"#F.expenseNo",		type:"TEXT", 	bind:"expenseNo", size:15}]},
				{items:[{name:"#L.expenseAmount",	type:"LABEL", 	value:"規費金額"+defaultCheck }]},
				{items:[{name:"#F.expenseAmount",	type:"NUMB", 	bind:"expenseAmount", size:15 , maxLen:4 }]}
			]}
		],

		beginService:"",
		closeService:""
		});
	}

}




// 移倉單明細(調撥單主檔s)
function kweDetail(){
	var allBuOrderTypes = vat.bean("allBuOrderTypes");

	var status = vat.item.getValueByName("#F.status");
//	alert(status);

	var isOpen = true;
	var vbCanGridDelete = isOpen;
	var vbCanGridAppend = isOpen;
	var vbCanGridModify = isOpen;

//  if( status == "SIGNING" || status == "FINISH" || status == "VOID" || status == "CLOSE" ){
//		isOpen = false;
//	}

	vat.item.make(vnB_Detail, "indexNo"				, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_Detail, "orderTypeCode"		, {type:"SELECT" , size:10, desc:"調撥單單別", init:allBuOrderTypes , eChange:"changeLineData()"   });
	vat.item.make(vnB_Detail, "orderNo"				, {type:"TEXT" , size:20, maxLen:23, desc:"調撥單單號(裝箱單號)", eChange:"changeLineData()"          	});

	vat.item.make(vnB_Detail, "searchItem", {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif",
	 									 			service:"Im_Movement:search:20090720.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			servicePassData:function(x){ return doPassData("vnB_Detail",x); },
	 									 			serviceAfterPick:function(id){doAfterPickerLineFunctionProcess(id); } });

	vat.item.make(vnB_Detail, "statusName"				, {type:"TEXT" , size:10, desc:"狀態", mode:"READONLY"    	});
	vat.item.make(vnB_Detail, "deliveryWarehouseName"	, {type:"TEXT" , size:12, desc:"出庫倉", mode:"READONLY"     	});
	vat.item.make(vnB_Detail, "arrivalWarehouseName"	, {type:"TEXT" , size:12, desc:"入庫倉", mode:"READONLY"     	});

	vat.item.make(vnB_Detail, "imCreatedByName"		, {type:"TEXT" , desc:"新增人員", mode:"READONLY" });
	vat.item.make(vnB_Detail, "imCreationDate"		, {type:"TEXT" , desc:"新增日期", mode:"READONLY" });
	vat.item.make(vnB_Detail, "imLastUpdatedByName"	, {type:"TEXT" , desc:"修改人員", mode:"READONLY" });
	vat.item.make(vnB_Detail, "imLastUpdateDate"	, {type:"TEXT" , desc:"修改日期", mode:"READONLY" });

	vat.item.make(vnB_Detail, "F11"          	, {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_Detail, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_Detail, "message"         , {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vnB_Detail, {
														id: "vatDetailDiv",
														pageSize: 10,
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail+")",
														loadSuccessAfter    : "loadSuccessAfter()",
														eventService        : "eventService()",
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Detail+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_Detail+")"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

// 第一次載入 重新整理
function loadBeforeAjxService(div){
//    alert(div);
	if( vnB_Detail === div ){
		var processString = "process_object_name=cmMovementService&process_object_method_name=getAJAXPageData" +
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
		                    "&status=" + vat.item.getValueByName("#F.status") +
		                    "&taxType=" + vat.item.getValueByName("#F.taxType");


		return processString;
	}
}

// 第一頁 翻到前或後頁 最後一頁
function saveBeforeAjxService(div){
//    alert("saveBeforeAjxService");
	if( vnB_Detail === div ){
		var processString = "process_object_name=cmMovementService&process_object_method_name=updateAJAXPageLinesData" +
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value +
		                    "&status="+ vat.item.getValueByName("#F.status");
		return processString;
	}
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


// tab切換 存檔
function doPageDataSave(div){
	var status = vat.item.getValueByName("#F.status");
	if(status == "SAVE" ){
		if(vnB_Master===div){
			vat.block.pageSearch(vnB_Detail); //存檔vnB_Detail
		}else if(vnB_Detail===div){
			vat.block.pageRefresh(div);
		}
	}
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

function refreshCusForm(){
    refreshForm("NF");
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
		  vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(vsfunctionCode);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
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

function doSaveWhNo(){
    
    vat.bean().vatBeanOther.moveWhNo 		= vat.item.getValueByName("#F.moveWhNo");
    vat.block.submit(function(){return "process_object_name=cmMovementAction"+
	      "&process_object_method_name=saveWhNo";}, { bind:true, link:true, other:true,
				                    funcSuccess:function(){
				                        vat.block.pageRefresh(vnB_Detail);
				                    }});
	
}

// 送出,暫存按鈕
function doSubmit(formAction){

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
    
    if("SUBMIT" == formAction){
	    if(vat.item.getValueByName("#F.orderType")=='RHV'|vat.item.getValueByName("#F.orderType")=='RHD'|vat.item.getValueByName("#F.orderType")=='RDH'|vat.item.getValueByName("#F.orderType")=='RHK'|vat.item.getValueByName("#F.orderType")=='RKH'){
		    if(vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '')=='SAVE'|vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '')=='SIGNING'){
		       if(vat.item.getValueByName("#F.moveWhNo")==''){ 
			        alert("請填入完稅移倉申請書號碼！");
			        return;
		       } 
		    }
	    }
    }

	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){
		alertMessage = "是否確定暫存?";
	}else if("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}

	if(confirm(alertMessage)){
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, '');
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var orderNoPrefix		  = vat.item.getValueByName("#F.orderNo").substring(0,3);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var approvalResult        = getApprovalResult();
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");

	    var formStatus = status;
		if("SAVE" == formAction){
	        formStatus = "SAVE";
	    }else if("SUBMIT" == formAction || "SUBMIT_BG" == formAction ){
	        formStatus = changeFormStatus(formId, processId, status, formAction);
	    }else if("VOID" == formAction){
	        formStatus = "VOID";
	    }
//	    alert( "approvalResult = " + approvalResult + "\napprovalComment = " + approvalComment );

	    if((orderNoPrefix == "TMP" &&  status == "SAVE") || status == "UNCONFIRMED" ||
			(inProcessing   && (status == "SAVE"  || status == "SIGNING" || status == "REJECT" ))){
			vat.block.pageDataSave( vnB_Detail ,{
				funcSuccess:function(){
					vat.bean().vatBeanOther.formAction 		= formAction;
					vat.bean().vatBeanOther.formStatus 		= formStatus;
		  			vat.bean().vatBeanOther.processId       = processId;
	  				vat.bean().vatBeanOther.approvalResult  = approvalResult;
	  				vat.bean().vatBeanOther.approvalComment =  approvalComment;

				    if("SUBMIT_BG" == formAction){
				      	vat.block.submit(function(){return "process_object_name=cmMovementAction"+
				                    "&process_object_method_name=getOrderNo";}, {bind:true, link:true, other:true});
				    }else{
						vat.block.submit(function(){return "process_object_name=cmMovementAction"+
				                    "&process_object_method_name=performTransaction";},{
				                    bind:true, link:true, other:true,
				                    funcSuccess:function(){
				                        vat.block.pageRefresh(vnB_Detail);
				                    }}
				                   
						);
			        }
		      	}
	      	});
		}else{
	    	alert("您的表單已加入待辦事件，請從待辦事件選取後，再次送出!");
	    }

	}
}

// 取得簽核結果
function getApprovalResult(){
	if(vat.item.getValueByName("#F.status") == "SIGNING"){
		return vat.item.getValueByName("#F.approvalResult").toString();
	}else{
		return "true";
	}
}

// 動態改變調撥單資料
function changeLineData(){
	var vLineId                   = vat.item.getGridLine();
	var orderNo = vat.item.getGridValueByName("orderNo", vLineId);

	if( orderNo != null && orderNo != "" ){

		vat.ajax.XHRequest(
	       {
	           post:"process_object_name=cmMovementService"+
	                    "&process_object_method_name=getAJAXImMovementData"+
	                    "&orderNo=" + orderNo +
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                    "&deliveryCustomsWarehouse=" + vat.item.getValueByName("#F.deliveryCustomsWarehouse") +
	                    "&arrivalCustomsWarehouse=" + vat.item.getValueByName("#F.arrivalCustomsWarehouse") +
	                    "&taxType=" + vat.item.getValueByName("#F.taxType") +
	                    "&orderTypeCode=" + vat.item.getGridValueByName("orderTypeCode", vLineId ),
	           find: function change(oXHR){
	           		vat.item.setGridValueByName("orderNo", vLineId, vat.ajax.getValue("orderNo", oXHR.responseText));
//          		vat.item.setGridValueByName("orderTypeCode", vLineId, vat.ajax.getValue("orderTypeCode", oXHR.responseText));
	          		vat.item.setGridValueByName("statusName", vLineId, vat.ajax.getValue("statusName", oXHR.responseText));
					vat.item.setGridValueByName("deliveryWarehouseName", vLineId, vat.ajax.getValue("deliveryWarehouseName", oXHR.responseText));
					vat.item.setGridValueByName("arrivalWarehouseName", vLineId, vat.ajax.getValue("arrivalWarehouseName", oXHR.responseText));
					vat.item.setGridValueByName("imCreatedByName", vLineId, vat.ajax.getValue("imCreatedByName", oXHR.responseText));
					vat.item.setGridValueByName("imCreationDate", vLineId, vat.ajax.getValue("imCreationDate", oXHR.responseText));
					vat.item.setGridValueByName("imLastUpdatedByName", vLineId, vat.ajax.getValue("imLastUpdatedByName", oXHR.responseText));
					vat.item.setGridValueByName("imLastUpdateDate", vLineId, vat.ajax.getValue("imLastUpdateDate", oXHR.responseText));
	           },
	           fail: function changeError(){
	          			vat.item.setGridValueByName("statusName", vLineId, "查無此調撥單");
	           }
	       });
	}else{ // 清空
		vat.item.setGridValueByName("statusName", vLineId, "");
		vat.item.setGridValueByName("deliveryWarehouseName", vLineId, "");
		vat.item.setGridValueByName("arrivalWarehouseName", vLineId, "");
		vat.item.setGridValueByName("imCreatedByName", vLineId, "");
		vat.item.setGridValueByName("imCreationDate", vLineId, "");
		vat.item.setGridValueByName("imLastUpdatedByName", vLineId, "");
		vat.item.setGridValueByName("imLastUpdateDate", vLineId, "");
	}
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsfunctionCode);
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
	  	vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsfunctionCode);
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
	  	vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsfunctionCode);
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
	    vsfunctionCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsfunctionCode);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(vsfunctionCode){
    
    var status = vat.item.getValueByName("#F.status");
	var tranRecordStatus = vat.item.getValueByName("#F.tranRecordStatus");
	var orderTypeCode = vat.item.getValueByName("#F.orderType");
	var customsStatus = vat.item.getValueByName("#F.customsStatusHidden");
	var tranAllowUpload = vat.item.getValueByName("#F.tranAllowUpload");
	var cStatus = customsStatus.substring(0, 1);
	var RecordStatus = tranRecordStatus.substring(0, 4);
    //alert(vsfunctionCode);
    //alert(status);
    //alert(customsStatus);
    //alert(tranAllowUpload);
    //alert(tranRecordStatus);
    if(vsfunctionCode=='NF'){
	    if(orderTypeCode=='RWD'||orderTypeCode=='RWK'||orderTypeCode=='RDW'||orderTypeCode=='RKW'||orderTypeCode=='RMV'||orderTypeCode=='RVM'){
		    if(status === "SAVE" & (customsStatus === "" ) & tranAllowUpload === "" && tranRecordStatus === ""){
		       chgStatus("NF10");
			}else if(status === "SIGNING" & (customsStatus === "N13")  & tranAllowUpload === "I" && tranRecordStatus === ""){
			   //alert("ss");
			   chgStatus("NF14O");
			}else if(status === "FINISH" & customsStatus === "N23" & cStatus==="O" & tranAllowUpload === "I" && tranRecordStatus === "NF14"){
			   chgStatus("NF14I");
			}
		}
	}
	document.forms[0]["#formId"            ].value = document.forms[0]["#formId"].value;
	document.forms[0]["#processId"         ].value = "";
	document.forms[0]["#assignmentId"      ].value = "";
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=cmMovementAction&process_object_method_name=performInitial";
     	},{other      : true,
     	   funcSuccess:function(){
     			vat.item.bindAll();
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);

				refreshWfParameter(vat.item.getValueByName("#F.brandCode"),
	           					 vat.item.getValueByName("#F.orderTypeCode"),
	           					 vat.item.getValueByName("#F.orderNo"));
				vat.block.pageRefresh(102);
	        	vat.tabm.displayToggle(0, "xTab3", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
				checkCustomsStatus();
				doFormAccessControl();
				checkUploadControl();
				getCustomsDesc();
     	}});


}

// 申請人,發貨人picker 回來執行
function doAfterPickerFunctionProcess(code){
	//do picker back something
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName(code, vat.bean().vatBeanPicker.result[0].employeeCode);
		changeEmployNameByCode(code);
	}
}

// 傳參數
function doPassData(div,x){
	var suffix = "";
	switch(div){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
			suffix += "&orderTypeCode="+escape(orderTypeCode);
			break;
		case "vnB_Detail":
			var vLineId	= vat.item.getGridLine(x);
			var orderTypeCode = vat.item.getGridValueByName("orderTypeCode", vLineId );
			var deliveryDate = vat.item.getValueByName("#F.deliveryDate");
			var arrivalCustomsWarehouse = vat.item.getValueByName("#F.arrivalCustomsWarehouse");
			suffix += "&startDeliveryDate="+deliveryDate
			+"&endDeliveryDate="+deliveryDate
			+"&orderTypeCode="+escape(orderTypeCode)
			+"&status="+escape("WAIT_IN")
			+"&customsWarehouseCode="+escape(arrivalCustomsWarehouse);
			break;
	}
	return suffix;
}

// 申請人,發貨人picker 回來執行
function doAfterPickerLineFunctionProcess(id){
//	alert("doAfterPickerLineFunctionProcess");
	var vLineId	= vat.item.getGridLine(id);

	vat.bean().vatBeanOther.headId = vat.item.getValueByName("#F.headId");
	vat.bean().vatBeanOther.lineId = vLineId;

	vat.block.pageSearch( vnB_Detail ,{
		funcSuccess:function(){
			vat.block.submit(
		    	function(){ return "process_object_name=cmMovementService&process_object_method_name=updateCmMovementLine";
		    	}, { other:true, picker:true, funcSuccess: function() {vat.block.pageRefresh(vnB_Detail);}} );
		}
	})
/*	vat.ajax.XHRequest({
          post:"process_object_name=cmMovementService"+
                   "&process_object_method_name=updateCmMovementLine"+
                   "&brandCode=" + document.forms[0]["#loginBrandCode"    ].value,
          bind:true, link:false, other:false, picker:true, isPicker:true,
          find: function change(oXHR){
          },
          fail: function changeError(){
          }
	});	*/

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
	var orderNoPrefix	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	var processId = null;
	if( vat.bean().vatBeanOther.processId != null ){
		processId	= vat.bean().vatBeanOther.processId;
	}

	// 初始化
	//====================================================================
/*	vat.item.setAttributeByName("#F.remark1", "readOnly", false);
	vat.item.setAttributeByName("#F.deliveryDate", "readOnly", false);
	vat.item.setAttributeByName("#F.customsArea", "readOnly", false);
	vat.item.setAttributeByName("#F.passNo", "readOnly", false);
	vat.item.setAttributeByName("#F.applicantCode", "readOnly", false);
	vat.item.setAttributeByName("#F.sealType", "readOnly", false);
	vat.item.setAttributeByName("#F.sealNo", "readOnly", false);
	vat.item.setAttributeByName("#F.carType", "readOnly", false);
	vat.item.setAttributeByName("#F.carNo", "readOnly", false);
	vat.item.setAttributeByName("#F.driverCode", "readOnly", false);
	vat.item.setAttributeByName("#F.deliverymanCode", "readOnly", false);
	vat.item.setAttributeByName("#F.expenseType", "readOnly", false);
	vat.item.setAttributeByName("#F.expenseNo", "readOnly", false);
	vat.item.setAttributeByName("#F.expenseAmount", "readOnly", false);

	vat.item.setAttributeByName("#B.applicantCode", "readOnly", false);
	vat.item.setAttributeByName("#B.deliverymanCode", "readOnly", false);*/
	vat.item.setAttributeByName("vatBlock_Head", "readOnly", false, true, true);
	vat.item.setAttributeByName("vatMasterDiv", "readOnly", false, true, true);

	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true);
	//===========================line========================================
/*	vat.item.setGridAttributeByName("orderNo", "readOnly", false);
	vat.item.setGridAttributeByName("orderTypeCode", "readOnly", false);
	vat.item.setGridAttributeByName("searchItem", "readOnly", false);*/
	vat.block.canGridModify([vnB_Detail], true,true,true);
	//===========================buttonLine==============================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.print", 	"display", "inline");
	vat.item.setStyleByName("#B.transferPrint", "display", "inline");
	vat.item.setStyleByName("#B.reducePrint", 	"display", "inline");
	vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	vat.item.setStyleByName("#B.submit1", 	"display", "none");
	
	
	// RWD以及RMK才有貨櫃（物）運送單的按鈕
	if("RPA" != vat.item.getValueByName("#F.orderTypeCode") && "RAP" != vat.item.getValueByName("#F.orderTypeCode") && "RMM" != vat.item.getValueByName("#F.orderTypeCode") 
	   && "RVM" != vat.item.getValueByName("#F.orderTypeCode") && "RMV" != vat.item.getValueByName("#F.orderTypeCode") && "RWD" != vat.item.getValueByName("#F.orderTypeCode") 
	   && "RMK" != vat.item.getValueByName("#F.orderTypeCode")&& "RMM" != vat.item.getValueByName("#F.orderTypeCode")&& "RWK" != vat.item.getValueByName("#F.orderTypeCode")
	   && "RKW" != vat.item.getValueByName("#F.orderTypeCode")){
		vat.item.setStyleByName("#B.transferPrint", "display", "none");
	}
	if("RVM" != vat.item.getValueByName("#F.orderTypeCode") && "RMV" != vat.item.getValueByName("#F.orderTypeCode") && "RMM" != vat.item.getValueByName("#F.orderTypeCode")&&"RMK" != vat.item.getValueByName("#F.orderTypeCode")&&"RWD" != vat.item.getValueByName("#F.orderTypeCode")&&"RDW" != vat.item.getValueByName("#F.orderTypeCode")){
		vat.item.setStyleByName("#F.customsStatus", "display", "none");
		vat.item.setAttributeByName("#F.customsStatus", "HIDDEN", false);
	}
	
	
	if(orderNoPrefix == "TMP" ){
		vat.item.setStyleByName("#B.print", 	"display", "none");
		vat.item.setStyleByName("#B.transferPrint", "display", "none");
		vat.item.setStyleByName("#B.reducePrint", 	"display", "none");
	}

	// 流程內
	if( processId != null && processId != 0 ){ //從待辦事項進入
		vat.item.setStyleByName("#B.new", 		"display", "none");
		vat.item.setStyleByName("#B.search", 	"display", "none");
		if( status == "SAVE" || status == "REJECT"){
			vat.item.setStyleByName("#B.void", 		"display", "inline");
		}else if( status == "SIGNING" ){
			vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
			vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
		}
	}else{ // 查詢回來
		if(orderNoPrefix == "TMP" ){

		}else{
			vat.item.setStyleByName("#B.submit", 	"display", "none");
			vat.item.setStyleByName("#B.save", 		"display", "none");
			vat.item.setStyleByName("#B.submitBG", 	"display", "none");
			vat.item.setStyleByName("#B.message", 	"display", "none");
			vat.item.setStyleByName("#B.import", 	"display", "none");
			vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
			vat.item.setAttributeByName("vatMasterDiv", "readOnly", true, true, true);
			vat.block.canGridModify([vnB_Detail], false,false,false);
		}
	}


	if( status == "SIGNING" || status == "FINISH" || status == "VOID" || status == "CLOSE" ){

/*		vat.item.setAttributeByName("#F.remark1", "readOnly", true);
		vat.item.setAttributeByName("#F.deliveryDate", "readOnly", true);
		vat.item.setAttributeByName("#F.customsArea", "readOnly", true);
		vat.item.setAttributeByName("#F.passNo", "readOnly", true);
		vat.item.setAttributeByName("#F.applicantCode", "readOnly", true);
		vat.item.setAttributeByName("#F.sealType", "readOnly", true);
		vat.item.setAttributeByName("#F.sealNo", "readOnly", true);
		vat.item.setAttributeByName("#F.carType", "readOnly", true);
		vat.item.setAttributeByName("#F.carNo", "readOnly", true);
		vat.item.setAttributeByName("#F.driverCode", "readOnly", true);
		vat.item.setAttributeByName("#F.deliverymanCode", "readOnly", true);
		vat.item.setAttributeByName("#F.expenseType", "readOnly", true);
		vat.item.setAttributeByName("#F.expenseNo", "readOnly", true);
		vat.item.setAttributeByName("#F.expenseAmount", "readOnly", true);

		vat.item.setAttributeByName("#B.applicantCode", "readOnly", true);
		vat.item.setAttributeByName("#B.deliverymanCode", "readOnly", true); */
		vat.item.setAttributeByName("vatBlock_Head", "readOnly", true, true, true);
		vat.item.setAttributeByName("vatMasterDiv", "readOnly", true, true, true);

/*		vat.item.setGridAttributeByName("orderNo", "readOnly", true);
		vat.item.setGridAttributeByName("orderTypeCode", "readOnly", true);
		vat.item.setGridAttributeByName("searchItem", "readOnly", true);*/
		vat.block.canGridModify([vnB_Detail], false,false,false);



		if( status == "SIGNING" && ( processId != ""  ) ){
			vat.item.setStyleByName("#B.submit", 		"display", "inline");
		}else{
			vat.item.setStyleByName("#B.submit", 		"display", "none");
		}
		
		vat.item.setStyleByName("#B.save"		, "display", "none");
		vat.item.setStyleByName("#B.submitBG" 	, "display", "none");
		vat.item.setStyleByName("#B.message" 	, "display", "none");
		vat.item.setStyleByName("#B.import"		, "display", "none");
		
	 }
		if("RHV" == vat.item.getValueByName("#F.orderTypeCode")||"RDH" == vat.item.getValueByName("#F.orderTypeCode")||"RHD" == vat.item.getValueByName("#F.orderTypeCode")||"RHK" == vat.item.getValueByName("#F.orderTypeCode")||"RKH" == vat.item.getValueByName("#F.orderTypeCode")){
		  
		  if(status == "FINISH"){
			  vat.item.setStyleByName("#B.submit1", 	"display", "inline");   
		  }else{
			  vat.item.setStyleByName("#B.submit1", 	"display", "none");
		  }
		  vat.item.setAttributeByName("#F.moveWhNo", "readOnly", false);
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

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=cmMovementAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

// 票據列印
function openReportWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=cmMovementService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
//			                    	alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}
							}
	);
	if("AFTER_SUBMIT"==type) createRefreshForm();
}

// 列印（貨櫃（物）運送單）
function openTransferReportWindow(type){
	//alert("do it.....");
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type)
    	vat.bean().vatBeanOther.transferOrderNo  = vat.item.getValueByName("#F.transferOrderNo");

    if("T2" == vat.item.getValueByName("#F.brandCode")){
    	// 更新列印時間
    	vat.block.submit(function(){
    		return "process_object_name=cmMovementService" + "&process_object_method_name=updateCmTransferPrintTime";}, {bind:true, link:true, other:true});
		vat.bean().vatBeanOther.reportFileName  = "IM0184.rpt";
		vat.block.submit(function(){return "process_object_name=cmMovementService"+
									"&process_object_method_name=getTransferReduceReportConfig";},{other:true,
				                    funcSuccess:function(){
				                    	//alert(vat.bean().vatBeanOther.reportUrl);
						        		eval(vat.bean().vatBeanOther.reportUrl);
						        	}}
		);
	}
	//alert("列印～～");
	if("AFTER_SUBMIT"==type) createRefreshForm();
}


// 票據列印(縮小清單)
function openReportReduceWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type)
    	vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");

    if("T2" == vat.item.getValueByName("#F.brandCode")){
		vat.bean().vatBeanOther.reportFileName  = "IM0183.rpt";
		vat.block.submit(function(){return "process_object_name=cmMovementService"+
									"&process_object_method_name=getReduceReportConfig";},{other:true,
				                    funcSuccess:function(){
				                    	//alert(vat.bean().vatBeanOther.reportUrl);
						        		eval(vat.bean().vatBeanOther.reportUrl);
						        	}}
		);
	}

	if("AFTER_SUBMIT"==type) createRefreshForm();
}
/*
function changeValue() { // 保卡號碼更改，即更改車行、車號、司機名字、駕照號碼
    var carNoValue = vat.item.getValueByName("#F.carNo");
    if (carNoValue.indexOf("076") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "聯安");
        vat.item.setValueByName("#F.vehicleNo", "013-AR");
        vat.item.setValueByName("#F.driverCode", "蘇志彬");
        vat.item.setValueByName("#F.driverLicence", "F123467894");
    } else if (carNoValue.indexOf("233") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "大昇");
        vat.item.setValueByName("#F.vehicleNo", "195-AV");
        vat.item.setValueByName("#F.driverCode", "");
        vat.item.setValueByName("#F.driverLicence", "");
    } else if (carNoValue.indexOf("238") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "大昇");
        vat.item.setValueByName("#F.vehicleNo", "222-AV");
        vat.item.setValueByName("#F.driverCode", "蘇翊軒");
        vat.item.setValueByName("#F.driverLicence", " A120043646");
    }else if (carNoValue.indexOf("1191") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "大昇");
        vat.item.setValueByName("#F.vehicleNo", "013-AR");
        vat.item.setValueByName("#F.driverCode", "蘇志彬");
        vat.item.setValueByName("#F.driverLicence", "F123467894");
    }else if (carNoValue.indexOf("073") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "大昇貨運有限公司");
        vat.item.setValueByName("#F.vehicleNo", "958-AQ");
        vat.item.setValueByName("#F.driverCode", "林東昌");
        vat.item.setValueByName("#F.driverLicence", "G120612423");
    }else if (carNoValue.indexOf("1580") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "大昇貨運有限公司");
        vat.item.setValueByName("#F.vehicleNo", "065-9A");
        vat.item.setValueByName("#F.driverCode", "童文進");
        vat.item.setValueByName("#F.driverLicence", "H121077689");
    }else if (carNoValue.indexOf("1597") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "大昇貨運有限公司");
        vat.item.setValueByName("#F.vehicleNo", "QAA-138");
        vat.item.setValueByName("#F.driverCode", "蘇志彬");
        vat.item.setValueByName("#F.driverLicence", "F123467894");
    }else if (carNoValue.indexOf("1596") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "大昇貨運有限公司");
        vat.item.setValueByName("#F.vehicleNo", "QAA-136");
        vat.item.setValueByName("#F.driverCode", "林銘華");
        vat.item.setValueByName("#F.driverLicence", "H121079567");
    }else if (carNoValue.indexOf("1609") > -1) {
        vat.item.setValueByName("#F.vehicleStation", "大昇貨運有限公司");
        vat.item.setValueByName("#F.vehicleNo", "QAA-139");
        vat.item.setValueByName("#F.driverCode", "吳韋寬");
        vat.item.setValueByName("#F.driverLicence", "C120057446");
    }else {
        vat.item.setValueByName("#F.vehicleStation", "");
        vat.item.setValueByName("#F.vehicleNo", "");
        vat.item.setValueByName("#F.driverCode", "");
        vat.item.setValueByName("#F.driverLicence", "");
    }
}
*/
//移倉單匯入功能
function importFormData(){
	var vsOrderTypeCode  = vat.item.getValueByName("#F.orderTypeCode");
    var beanName = "";
    var suffix ="";
	if( vat.item.getValueByName("#F.brandCode").indexOf("T2") > -1 ){
		beanName = "T2_CM_MOVEMENT_ITEM";
	}

	suffix =
		"&importBeanName="+ beanName +
		"&importFileType=XLS" +
        "&processObjectName=cmMovementService" +
        "&processObjectMethodName=executeImportCmMovementLines" +
        "&arguments=" +vat.item.getValueByName("#F.headId")  +
        "&parameterTypes=LONG" +
        "&blockId=" + vnB_Detail
		//'menubar=no,resizable=no,scrollbars=no,status=no,left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2;

	return suffix;

}

//移倉單匯出明細
function exportFormData(){
    //alert("export to xml file...")
	var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var url;
	if( vat.item.getValueByName("#F.brandCode").indexOf("T2") > -1){ // for T2
    	url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName=T2_CM_MOVEMENT_ITEM" +
              "&fileType=XLS" +
              "&processObjectName=cmMovementService" +
              "&processObjectMethodName=exportExcelDetail" +
              "&gridFieldName=cmMovementItems" +
              "&headId=" + vat.item.getValueByName("#F.headId") +
              "&status=" + vat.item.getValueByName("#F.status") +
              "&taxType=" + vat.item.getValueByName("#F.taxType") +
              "&brandCode=" + vat.item.getValueByName("#F.brandCode");

	}

    var width = "200";
    var height = "30";
    vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : function(){
    			window.open(url, '移倉單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});

}

function executeTxt(status){
//alert("executeTxt");
	var url;
	url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName=" + vat.item.getValueByName("#F.orderNo") +
              "&fileType=TXT" +
              "&status="+ status + //IN OR OUT
              "&processObjectName=cmMovementService" +
              "&processObjectMethodName=exportExcelDetail" +
              "&exportType=pTxt" +
              "&headId=" + vat.item.getValueByName("#F.headId") +
              "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
              "&orderTypeCode=" + vat.bean().vatBeanOther.orderTypeCode ;
     var width ='200';
     var height = '30';
     window.open(url, '調撥單檔案列印', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
	
}

function chgStatus(tranStatus){
     
     //alert(tranStatus);
     var str = "";
     if(tranStatus=='NF14O'){
        str = "點驗出倉並";
     }else if(tranStatus=='NF14I'){
        str = "點驗進倉並";
     }else if(tranStatus=='cancel'){
        str = '註銷並';
     }
     
     
     if(confirm("此單目前將"+str+"送簽海關,請問是否要送出?")){
     	vat.ajax.XHRequest({
          post:"process_object_name=cmMovementService"+
                   "&process_object_method_name=updateCustomsStatus"+
                   "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
		                    "&status=" + tranStatus,
          find: function change(oXHR){
          		alert("已送簽 請稍候~");
          		
          },
          fail: function changeError(){
          		
          }
		});
		alert("已將單據送簽海關");
		window.top.close();
     
     }
}

function allowOrCheck(type){
    var customsStatus = vat.item.getValueByName("#F.customsStatusHidden");
    var tranRecordStatus = vat.item.getValueByName("#F.tranRecordStatus");
    var phase = "";
    var Status = false;
    var errMsg = "";
    //alert(type);
	/*if (type=="ALLOW"){
		if (((customsStatus==""||customsStatus==null) && tranRecordStatus=="") || (customsStatus=="E" && tranRecordStatus=="DF10")) {
			Status = true;
			phase = "DF10"
		}
		else
		{
			Status = false;
			errMsg = "無法進行移倉許可！";			
		}
		
	}
	else
	{
		if ((customsStatus=="A" && tranRecordStatus=="DF10") || (customsStatus=="E" && tranRecordStatus=="DF15"))
		{
			Status = true;
			phase = "DF15"
		}
		else
		{
			Status = false;
			errMsg = "無法進行點驗進倉！";
		}
	}*/
	
	//alert(Status);
	
	if (true)
	{	//alert(phase);
		//alert(vat.item.getValueByName("#F.headId"));
		vat.bean().vatBeanOther.headId  = vat.item.getValueByName("#F.headId");
        vat.bean().vatBeanOther.tranRecordStatus = phase;

		vat.block.submit(function(){return "process_object_name=cmMovementService"+
				                    "&process_object_method_name=updateCustomsStatus";},{
				                    bind:true, link:true, other:true,
				                    funcSuccess:function(){
						        		alert("上傳狀態更新成功！");
						        	}}
						);
	}
	else
	{
		//alert(errMsg);
	}
}



function updateApplicationNo(model){

	//var deliverySwitch =  vat.item.getValueByName("#F.deliverySwitch");


	   	var alertMessage ="是否確定重取單號?";
		if(confirm(alertMessage))
		{
		   vat.ajax.XHRequest({
		          asyn:false,
		          post:"process_object_name=uploadControlService"+
		                   "&process_object_method_name=updateApplicationNo"
		                   	+"&model="+model
							+"&headId="+vat.item.getValueByName("#F.headId")
							+"&loginEmployeeCode="+ document.forms[0]["#loginEmployeeCode" ].value,
		          find: function change(oXHR){ 
		          		//alert("test");
		          	alert("重取完畢");
		          	//vat.block.pageRefresh(vatDetailDiv);
		          	refreshForm(vat.item.getValueByName("#F.headId"));
		          } 
			});	
		}

}

function changeValue() { // 保卡號碼更改，即更改車行、車號、司機名字、駕照號碼
    vat.block.pageDataSave( vnB_Detail ,{
		loadSuccessAfter:function(){
  		vat.bean().vatBeanOther.carNo = vat.item.getValueByName("#F.carNo");
		vat.block.submit(function(){return "process_object_name=cmMovementService"+
		            "&process_object_method_name=changeTruckCode";},  {other:true,picker:false,
		     funcSuccess: function() {
		      	 //vat.item.setValueByName("#F.carNo"       	    ,"保卡號碼"+ vat.bean().vatBeanOther.carNo);
		      	 vat.item.setValueByName("#F.carNo"       	    ,vat.bean().vatBeanOther.truckCode);
			     vat.item.setValueByName("#F.driverCode"        , vat.bean().vatBeanOther.truckDriver);
				 vat.item.setValueByName("#F.vehicleStation"    , vat.bean().vatBeanOther.freightName);
				 vat.item.setValueByName("#F.vehicleNo"		    ,vat.bean().vatBeanOther.truckNumber);
				 vat.item.setValueByName("#F.driverLicence"		,vat.bean().vatBeanOther.truckDriverId);
		     }
		});
	saveBeforeAjxService(vnB_Detail);
	}});
    
}

function txtExport(){
	vat.ajax.XHRequest({
		post:"process_object_name=cmMovementService"+
			"&process_object_method_name=txtExport"+
                   "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode"),
		find: function change(oXHR){ 
			
		}
	});
}