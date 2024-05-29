/***
 *	檔案: imMovement.js
 *	說明：表單明細
 *	修改：Mac
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_master = 2;
var vnB_Detail = 3;
//for 儲位用
var vatStorageDetail = 202;
var enableStorage = false;


function kweImBlock(){

   if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     orderTypeCode      : vat.item.getValueByName("#F.orderTypeCode") == "" ? document.forms[0]["#orderTypeCode"].value:vat.item.getValueByName("#F.orderTypeCode"),
         processId          : document.forms[0]["#processId"         ].value,
	     formId             : document.forms[0]["#formId"            ].value,
	     assignmentId       : document.forms[0]["#assignmentId"      ].value,
	     activityStatus     : document.forms[0]["#activityStatus"    ].value,
	     updateForm         : document.forms[0]["#formId"].value==""?"N":"Y",
	     itemCategory       : vat.item.getValueByName("#F.itemCategory"),
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	 };
  }
 // kweImInitial()
  kweButtonLine();
  kweImHeader();

  if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","主檔資料"   ,"vatBlock_Master"  			,"images/tab_master_data_dark.gif"    		,"images/tab_master_data_light.gif", false);
		vat.tabm.createButton(0 ,"xTab2","明細檔資料" ,"vatDetailDiv"        		,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false);
  //	vat.tabm.createButton(0 ,"xTab3","其他附件"   ,"vatAttachmentDataDiv"  ,"images/tab_other_attachment_dark.gif"   ,"images/tab_other_attachment_light.gif", "none");
		vat.tabm.createButton(0 ,"xTab5","簽核資料"   ,"vatApprovalDiv"        ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif",vat.item.getValueByName("#F.status") == "SAVE" || vat.item.getValueByName("#F.status") == "UNCONFIRMED"? "none" : "inline");
  //	vat.tabm.createButton(0 ,"xTab6","動態加會簽" ,"vatDynamicApprovalDiv"	,"images/tab_dynamic_approval_dark.gif"   ,"images/tab_dynamic_approval_light.gif", "none");
  
        //for 儲位用
 		enableStorage = "T2" == document.forms[0]["#loginBrandCode"    ].value;
		if(enableStorage){
  			vat.tabm.createButton(0, "xTab6", "儲位資料", "vatStorageDiv", "images/tab_storage_detail_dark.gif", "images/tab_storage_detail_light.gif", "", "reloadStorageDetail()");
  		}
  }
  
 kweImMaster();
  kweImDetail();
  kweWfBlock(vat.item.getValueByName("#F.brandCode"),
             vat.item.getValueByName("#F.orderTypeCode"),
             vat.item.getValueByName("#F.orderNo"),
             vat.bean("loginEmployeeCode") );

	//for 儲位用
	if(enableStorage){
		kweStorageBlock(vnB_Detail);
	}
    
}

function kweImInitial(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   // alert(vat.item.getValueByName("#F.orderTypeCode") == "" ? document.forms[0]["#orderTypeCode"].value:vat.item.getValueByName("#F.orderTypeCode"));
  	// alert("itemCategory:"+vat.item.getValueByName("#F.itemCategory"));

     vat.bean.init(function(){
		return "process_object_name=imMovementMainService&process_object_method_name=executeInitial";
     },{other: true});

     vat.item.SelectBind(vat.bean("allOrderTypes"),{ itemName : "#F.orderTypeCode" });
     vat.item.SelectBind(vat.bean("allItemCategories"),{ itemName : "#F.itemCategory" });
     vat.item.SelectBind(vat.bean("allFixSuppliers"),{ itemName : "#F.allFixSuppliers" });
     
     vat.item.SelectBind(vat.bean("allDeliveryWarehouses"),{ itemName : "#F.deliveryWarehouseCode" });
    vat.item.SelectBind(vat.bean("allArrivalWarehouses"),{ itemName : "#F.arrivalWarehouseCode" });
    vat.item.SelectBind(vat.bean("allArrivalStore"),{ itemName : "#F.arrivalStoreCode" }); // 轉入店別
     vat.item.SelectBind(vat.bean("allReportList"),{ itemName : "#F.reportList" });
     vat.item.bindAll();
     if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	 }
	 if(vat.item.getValueByName("#F.brandCode").indexOf("T2") > -1){
	 	vat.item.setValueByName("#F.arrivalDate",  vat.item.getValueByName("#F.deliveryDate")) ;
	 }
	 if("T2" != vat.item.getValueByName("#F.brandCode")){ // 轉入店別只提供T2使用
	 	vat.item.setAttributeByName("#F.arrivalStoreCode" , "disabled", true);
	 }

     vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     refreshWfParameter(vat.item.getValueByName("#F.brandCode"), vat.item.getValueByName("#F.orderTypeCode"),  vat.item.getValueByName("#F.orderNo"));
 	 vat.tabm.displayToggle(0, "xTab5", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false)
	 doFormAccessControl();
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
	 			//{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  ,
	 									 openMode:"open",
	 									 service:"Im_Movement:search:20090720.page",
	 									 servicePassData:function(x){ return doPassHeadData(x); },
	 									 left:0, right:0, width:1024, height:768,
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.declaration" , type:"IMG"    ,value:"取原報單",   src:"./images/button_declaration.gif" , eClick:"execExtendItemInfo()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	            {name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  ,
	 									 openMode:"open",
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,
	 									 serviceAfterPick:function(){afterImportSuccess()}},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,
	 			//for 儲位用
	 			{name:"#B.storageExport", 	type:"IMG"    ,value:"儲位匯出",   src:"./images/button_storage_export.gif" , eClick:'exportStorageFormData()'},
	 			{name:"#B.storageImport",	type:"PICKER" , value:"儲位匯入",  src:"./images/button_storage_import.gif"  , 
						 openMode:"open", 
						 service:"/erp/fileUpload:standard:2.page",
						 servicePassData:function(x){ return importStorageFormData(); },
						 left:0, right:0, width:600, height:400,	
						 serviceAfterPick:function(){}},
				{name:"#B.diversityReport", 	type:"IMG"    ,value:"覆核差異表", src:"./images/button_submit_background.gif"   ,eClick:'openDiversityWindow("")'},
				{name:"#B.deleteImStorageItems", 	type:"IMG"    ,value:"清除儲位明細",   src:"./images/del_detail.png" , eClick:'deleteImStorageItems()'},
				//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,		 
	 			
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendCustoms"        , type:"IMG"    ,value:"送簽海關"  ,src:"./images/send_customs1.jpg",   eClick:'chgStatus("NF13");'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendBack"        , type:"IMG"    ,value:"點建進倉",src:"./images/send_customs2.jpg"    , eClick:'chgStatus("NF14");'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			{name:"#B.sendCancel"        , type:"IMG"    ,value:"運回",src:"./images/send_customs3.jpg"    , eClick:'chgStatus("cancel");'},
	 			{name:"voidSpace"      , type:"LABEL"  ,value:"　"},
	 			
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"} ,
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 2, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:"/"},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 2, mode:"READONLY" },
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#F.reportList"  , type:"SELECT"},
	 			{name:"#B.print"       , type:"BUTTON"    ,value:"列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:function(){return ""}	,
		closeService:function(){return ""}
	});

}

function chgStatus(tranStatus){
     //alert("!!"+tranStatus);
     if(confirm("是否要送出?")){
	     vat.ajax.XHRequest({
	          post:"process_object_name=imMovementMainService"+
	                   "&process_object_method_name=updateStatus"+
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

function kweImHeader(){
//var allOrderTypes=vat.bean("allOrderTypes");
//var allItemCategories=vat.bean("allItemCategories");
var vsRowStyle= vat.bean("loginBrandCode").indexOf("T2") > -1 ? "" : " style= 'display:none;'";
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"調撥單維護作業", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode", type:"LABEL" , value:"單別"}]},
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", size:1, mode:"READONLY", eChange:'kweImInitial()'}]},
	 {items:[{name:"#L.orderNo"     , type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.orderNo"     , type:"TEXT"  ,  bind:"orderNo",back:false,size:20, mode:"READONLY"},
	 		 {name:"#F.headId"      , type:"TEXT"  ,  bind:"headId", back:false, mode:"READONLY"}
	 		 		//for 儲位用
					,{name:"#F.storageHeadId",   type:"TEXT",   bind:"storageHeadId",    back:false, mode:"READONLY" }
	 				]},
	 {items:[{name:"#L.brandCode"   , type:"LABEL" , value:"品牌"}]},
	 {items:[{name:"#F.brandCode"   , type:"TEXT"  ,  bind:"brandCode", size:6, mode:"HIDDEN"},
	 		 {name:"#F.brandName"   , type:"TEXT"  ,  bind:"brandName", back:false, size:12, mode:"READONLY"}]},
	 {items:[{name:"#L.status"      , type:"LABEL" , value:"狀態"}]},
	 {items:[{name:"#F.status"      , type:"TEXT"  ,  bind:"status", size:12, mode:"HIDDEN"},
	  		 {name:"#F.statusName"  , type:"TEXT"  ,  bind:"statusName", back:false, mode:"READONLY"}]}]},
	 {row_style:vsRowStyle, cols:[
	 {items:[{name:"#L.taxType"     , type:"LABEL", value:"稅別"}]},
	 {items:[{name:"#F.taxType"     , type:"TEXT",   bind:"taxType", size:3, mode:"READONLY"},
	 		 {name:"#F.taxTypeName" , type:"TEXT" ,  bind:"taxTypeName", back:false, size:12, mode:"READONLY"}]},
	 {items:[{name:"#L.itemCategory", type:"LABEL", value:"業種"}]},
	 {items:[{name:"#F.itemCategory", type:"SELECT",  bind:"itemCategory", size:1, mode:"",eChange:'changeItemCategory()'}]},
	 {items:[{name:"#L.cmMovementNo", type:"LABEL", value:"移倉單號"}]},
	 {items:[{name:"#F.cmMovementNo", type:"TEXT",   bind:"cmMovementNo", size:20,  mode:"READONLY"}]},
	 {items:[{name:"#L.boxCount"    , type:"LABEL", value:"總箱數"}]},
	 {items:[{name:"#F.boxCount"    , type:"NUMB",   bind:"boxCount", size:4, mode:"READONLY"},
 			 {name:"#B.boxCount"    , type:"IMG" ,value:"件數計算",   src:"./images/arrowdown.png" ,eClick:'showTotalCountPage()'}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.packedBy"    , type:"LABEL", value:"揀貨人員"}]},
	 {items:[{name:"#F.packedBy"    , type:"TEXT",   bind:"packedBy", size:6,  mode:"",eChange:'getEmployeeInfo("packedBy")'},
	         {name:"#F.packedByName", type:"TEXT",   bind:"packedByName", size:6,  mode:"READONLY"}]},
	 {items:[{name:"#L.comfirmedBy" , type:"LABEL", value:"複核人員"}]},
	 {items:[{name:"#F.comfirmedBy" , type:"TEXT",   bind:"comfirmedBy", size:6,  mode:"",eChange:'getEmployeeInfo("comfirmedBy")'},
	         {name:"#F.comfirmedByName", type:"TEXT",bind:"comfirmedByName", size:6,  mode:"READONLY"}]},
	 {items:[{name:"#L.comfirmedBy" , type:"LABEL", value:"收貨人員"}]},
	 {items:[{name:"#F.receiptedBy"    , type:"TEXT",  bind:"receiptedBy", size:6,  mode:"",eChange:'getEmployeeInfo("receiptedBy")'},
	         {name:"#F.receiptedByName", type:"TEXT",  bind:"receiptByName", size:6,  mode:"READONLY"}]},
	 {items:[{name:"#L.itemNo"      , type:"LABEL", value:"總項次"}]},
	 {items:[{name:"#F.itemCount"   , type:"NUMB",  size:1, bind:"itemCount",  mode:"READONLY"},
	         {name:"#L./"           , type:"LABEL", value:"／", size:1},
	         {name:"#F.itemCount1"  , type:"NUMB",  size:1, mode:"READONLY"},
	         //{name:"#L./"           , type:"LABEL", value:"／", size:1},
	         //{name:"#F.itemCount2"  , type:"NUMB",  size:1, mode:"READONLY"},
 			 {name:"#B.itemCount"   , type:"IMG" ,value:"件數計算",   src:"./images/arrowdown.png" ,eClick:'showTotalCountPage()'}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark1"     , type:"LABEL", value:"備註一"}]},
	 {items:[{name:"#F.remark1"     , type:"TEXT",   bind:"remark1", size:30, maxLen:700, desc:"放一般備註內容"}], td:" colSpan=1"},
	 {items:[{name:"#L.sendRpNo"      , type:"LABEL", value:"送修申請書號碼"}]},
 	 {items:[{name:"#F.sendRpNo"      , type:"TEXT",   bind:"sendRpNo", mode:"READONLY", size:20}]},
	 {items:[{name:"#L.createdBy"    , type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.createdBy"    , type:"TEXT",   bind:"createdBy",  mode:"HIDDEN", size:12},
	 	     {name:"#F.createdByName", type:"TEXT",   bind:"createdByName",  mode:"READONLY", size:12}]},
	 {items:[{name:"#L.lastDt"      , type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.lastDt"      , type:"TEXT",   bind:"lastUpdateDate", mode:"READONLY", size:12}]}]}
 	
	  ],
		beginService:"",
		closeService:"doCheckStore"
	});

}

function kweImMaster(){
//var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
//var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
//var vsFormStatus = vat.item.getValueByName("#F.status");

//if( vsFormStatus == "SAVE" || vsFormStatus == "REJECT" || vsFormStatus == "UNCONFIRMED"){
//		vWarehouseModeSelect = "";
//		vWarehouseModeEdit = "HIDDEN";
//		vWarehouseModeEditName = "HIDDEN";
//}else{
//		vWarehouseModeSelect = "HIDDEN";
//		vWarehouseModeEdit = "";
//		vWarehouseModeEditName = "READONLY";
//}

vat.block.create(vnB_master, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryWarehouseCode"    , type:"LABEL" , value:"轉出倉庫<font color='red'>*</font>"}]},
	 {items:[{name:"#F.deliveryWarehouseCode"    , type:"SELECT",  bind:"deliveryWarehouseCode", size:12,  eChange:'changeWarehouseCode("delivery")'},
		//	 {name:"#F.deliveryWarehouseCodeText", type:"TEXT"  ,  bind:"deliveryWarehouseCode", size:12},
	 	//	 {name:"#F.deliveryWarehouseName"    , type:"TEXT"  ,  bind:"deliveryWarehouseName", size:12},
	 		 {name:"#F.customsDeliveryWarehouseCode" , type:"TEXT",bind:"customsDeliveryWarehouseCode", size:1, mode:"READONLY"},
	 		 {name:"#F.allowMinusStock"          , type:"TEXT",    bind:"allowMinusStock", size:1, mode:"HIDDEN"}]},
	 {items:[{name:"#L.deliveryDate"             , type:"LABEL" , value:"轉出日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.deliveryDate"             , type:"DATE"  ,  bind:"deliveryDate", size:12,  eChange:'changeDeliveryDate()'}]},
	 {items:[{name:"#L.deliveryContactPerson"    , type:"LABEL" , value:"倉管人員"}]},
	 {items:[{name:"#F.deliveryContactPerson"    , type:"TEXT"  ,  bind:"deliveryContactPerson"    , size:12, mode:"READONLY"},
	 		 {name:"#F.deliveryContactPersonName", type:"TEXT"  ,  bind:"deliveryContactPersonName", back:false, size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryAddress", type:"LABEL", value:"出貨地址"}]},
	 {items:[{name:"#L.deliveryCity"   , type:"LABEL", value:"城市:&nbsp&nbsp;"},
	 		 {name:"#F.deliveryCity"   , type:"TEXT" , bind:"deliveryCity", size:10},
			 {name:"#L.deliveryArea"   , type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
	 		 {name:"#F.deliveryArea"   , type:"TEXT",  bind:"deliveryArea", size:10},
	 		 {name:"#L.deliveryZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
	 		 {name:"#F.deliveryZipCode", type:"TEXT",  bind:"deliveryZipCode", size:5},
 			 {name:"#F.deliveryAddress", type:"TEXT",  bind:"deliveryAddress", size:70, maxLen:200}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.arrivalWarehouseCode"    , type:"LABEL" , value:"轉入庫別<font color='red'>*</font>"}]},
	 {items:[{name:"#F.arrivalWarehouseCode"    , type:"SELECT",  bind:"arrivalWarehouseCode", size:12, eChange:'changeWarehouseCode("arrival")'},
			// {name:"#F.arrivalWarehouseCodeText", type:"TEXT",    bind:"arrivalWarehouseCode", size:12},
	 		// {name:"#F.arrivalWarehouseName"    , type:"TEXT",    bind:"arrivalWarehouseName", size:12},
	 		 {name:"#F.customsArrivalWarehouseCode" , type:"TEXT",bind:"customsArrivalWarehouseCode", size:1, mode:"READONLY"}]},
	 {items:[{name:"#L.arrivalDate"             , type:"LABEL" , value:"轉入日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.arrivalDate"             , type:"DATE"  , bind:"arrivalDate", size:12}]},
	 {items:[{name:"#L.arrivalContactPerson"    , type:"LABEL" , value:"倉管人員"}]},
	 {items:[{name:"#F.arrivalContactPerson"    , type:"TEXT"  , bind:"arrivalContactPerson", size:12, mode:"READONLY"},
	 		 {name:"#F.arrivalContactPersonName", type:"TEXT"  , bind:"arrivalContactPersonName", size:12, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.arrivalStoreCode"    	, type:"LABEL" , value:"廠商代碼"}]},
	 {items:[{name:"#F.allFixSuppliers"    	, type:"SELECT", bind:"rpSupplier", size:12, eChange:'getFixAddr()'},
			 {name:"#F.customsArrivalStoreCode" , type:"TEXT"  , bind:"customsArrivalStoreCode", size:1, mode:"READONLY"}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.arrivalAddress", type:"LABEL", value:"維修地址"}]},
	 {items:[{name:"#L.arrivalCity"   , type:"LABEL", value:"城市:&nbsp&nbsp;"},
	 		 {name:"#F.arrivalCity"   , type:"TEXT" ,  bind:"arrivalCity", size:10},
	 		 {name:"#L.arrivalArea"   , type:"LABEL", value:"&nbsp&nbsp&nbsp;鄉鎮區別:&nbsp&nbsp;"},
	 		 {name:"#F.arrivalArea"   , type:"TEXT",   bind:"arrivalArea", size:10},
	 		 {name:"#L.arrivalZipCode", type:"LABEL", value:"&nbsp&nbsp&nbsp;郵遞區號:&nbsp&nbsp;"},
	 		 {name:"#F.arrivalZipCode", type:"TEXT",   bind:"arrivalZipCode", size:5},
 			 {name:"#F.arrivalAddress", type:"TEXT",   bind:"arrivalAddress", size:70, maxLen:200}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.originalOrderTypeCode", type:"LABEL", value:"來源單別"}]},
	 {items:[{name:"#F.originalOrderTypeCode", type:"TEXT",   bind:"originalOrderTypeCode", size:13}]},
	 {items:[{name:"#L.originalOrderNo"      , type:"LABEL", value:"來源單號"}]},
 	 {items:[{name:"#F.originalOrderNo"      , type:"TEXT",   bind:"originalOrderNo"      , size:20}], td:" colSpan=3"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.transport"     , type:"LABEL", value:"運輸方式"}]},
	 {items:[{name:"#F.transport"     , type:"TEXT",   bind:"transport", size:100, maxLen:200, desc:""}], td:" colSpan=5"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark2"       , type:"LABEL", value:"備註二"}]},
	 {items:[{name:"#F.remark2"       , type:"TEXT",   bind:"remark2", size:100, maxLen:200, desc:""}], td:" colSpan=5"}]},
 	 {row_style:"" ,cols:[
 	 {items:[{name:"#L.whComfirmedBy"      , type:"LABEL", value:"倉儲-覆核人員"}]},
 	 {items:[{name:"#F.whComfirmedBy"      , type:"TEXT",   bind:"whComfirmedBy", mode:"READONLY", size:12 }]},
 	 {items:[{name:"#L.whComfirmedDate"      , type:"LABEL", value:"倉儲-覆核日期"}]},
 	 {items:[{name:"#F.whComfirmedDate"      , type:"TEXT",   bind:"whComfirmedDate", mode:"READONLY", size:12}]},
 	 {items:[{name:"#L.whComfirmedTimes"      , type:"LABEL", value:"倉儲-覆核次數"}]},
 	 {items:[{name:"#F.whComfirmedTimes"      , type:"TEXT",   bind:"whComfirmedTimes", mode:"READONLY", size:12}], td:" colSpan=3"}
 	 ]}
 	 	],
		beginService:"",
		closeService:""
	});

}

function kweImDetail(){
//  var vsBrandCode     = vat.item.getValueByName("#F.brandCode");
//  var vsFormStatus    = vat.item.getValueByName("#F.status");
//  var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
//  var vbCanGridDelete = false;
//  var vbCanGridAppend = false;
//  var vbCanGridModify = false;
//  var detailId = "vatDetailDiv";
//  if( vsFormStatus == "SAVE" || vsFormStatus == "REJECT" || vsFormStatus == "UNCONFIRMED"){
		vbCanGridDelete = true;
		vbCanGridAppend = true;
		vbCanGridModify = true;
//  }

    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"   ,                     desc:"序號"          });
    vat.item.make(vnB_Detail, "boxNo"                     , {type:"NUMM"  , size:3, maxLen:10,  desc:"箱號"         , mode:""});
	vat.item.make(vnB_Detail, "itemCode"                  , {type:"TEXT"  , size:18, maxLen:20, desc:"品號"         , mask:"CCCCCCCCCCCC", eChange:"changeItemData()"});
    vat.item.make(vnB_Detail, "searchItem"	              , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif",
	 									 		            	service:"Im_OnHand:search:20091224.page",
	 									 			            left:0, right:0, width:1024, height:768,
	 									 						servicePassData:function(x){ return doPassLineData(x); },
	 									 		                serviceAfterPick:function(id){doLineAfterPickerProcess(id); } });
	vat.item.make(vnB_Detail, "itemName"                  , {type:"TEXT" , size:12, maxLen:20, desc:"品名"	, mode:"READONLY"	, alter:true });
	
	vat.item.make(vnB_Detail, "deliveryWarehouseCode"     , {type:"TEXT" , size: 5, maxLen:20, desc:"轉出庫"	, mode:"READONLY"});
	vat.item.make(vnB_Detail, "lotNo"                     , {type:"TEXT" , size:12, maxLen:15, desc:"批號"	, mask:"CCCCCCCCCCCC", eChange:"changeItemData()"});
	vat.item.make(vnB_Detail, "stockOnHandQuantity"       , {type:"NUMM" , size: 4, maxLen:12, desc:"可用量"	, dec:0	, mode:"READONLY"});
	vat.item.make(vnB_Detail, "originalDeliveryQuantity"  , {type:"NUMM" , size: 4, maxLen:12, desc:"預出量"	, dec:0 ,  eChange:"checkQuantity()"});
	vat.item.make(vnB_Detail, "deliveryQuantity"          , {type:"NUMM" , size: 4, maxLen:12, desc:"實出量"	, dec:0	,  eChange:"checkQuantity()"});
	vat.item.make(vnB_Detail, "whComfirmedQuantity"          , {type:"NUMM" , size: 4, maxLen:12, desc:"覆核量"	, dec:0, mode:"READONLY"	,  eChange:""});
	vat.item.make(vnB_Detail, "arrivalWarehouseCode"      , {type:"TEXT" , size: 4, maxLen:20, desc:"轉入庫"	, mode:"READONLY"});
	vat.item.make(vnB_Detail, "arrivalQuantity"           , {type:"NUMM" , size: 4, maxLen:12, desc:"轉入量"	, dec:0});
	vat.item.make(vnB_Detail, "originalDeclarationNo"     , {type:"TEXT" , size:14, maxLen:14, desc:"原報單單號"  });
	vat.item.make(vnB_Detail, "searchDeclaration"		  , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif", //mode:"HIDDEN",
	 									 			service:"Cm_DeclarationOnHand:search:20091103.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			servicePassData:function(x){ return doPassDeclData(x); },
	 									 			serviceAfterPick:function(id){doDeclAfterPickerProcess(id); } });
	vat.item.make(vnB_Detail, "originalDeclarationSeq"    , {type:"NUMB" , size: 3, maxLen:4,  desc:"項次"    });
	vat.item.make(vnB_Detail, "originalDeclarationDate"   , {type:"DATE" , size: 5,  desc:"報單日期"     , mode:"READONLY"});

	vat.item.make(vnB_Detail, "F11"                       , {type:"ROWID"});
	//vat.item.make(vnB_Detail, "isReturn"              , {type:"CHECKBOX", desc:"退回"});
	vat.item.make(vnB_Detail, "isLockRecord"              , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
    vat.item.make(vnB_Detail, "isDeleteRecord"            , {type:"DEL"  , desc:"刪除"});
    //vat.item.make(vnB_Detail, "isReturn"              , {type:"CHECKBOX", desc:"退回"});
	vat.item.make(vnB_Detail, "message"                   , {type:"MSG"  , desc:"訊息"});
	vat.item.make(vnB_Detail, "unit"                  , {type:"TEXT" , size:5, maxLen:5, desc:"單位"	, mode:"READONLY"});
	vat.block.pageLayout(vnB_Detail, {
														id: "vatDetailDiv",
														pageSize: 10,
								                        canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,
														beginService: "",
								                        closeService: function(){kweImInitial();},
														appendBeforeService : "kwePageAppendBeforeMethod()",
														appendAfterService  : "kwePageAppendAfterMethod()",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "kwePageLoadSuccess()",
														eventService        : "assignOriginalQtyToArrival()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
}

function kwePageSaveMethod(){}


function kwePageSaveSuccess(){
	//alert("更新成功");
}


function kwePageLoadSuccess(){
	//$.unblockUI();
	countTotalQuantity();
	vat.block.pageDataSave(vnB_Detail);

}

function kwePageAppendBeforeMethod(){
	 return confirm("你確定要新增嗎?");
	return true;
}

function kwePageAppendAfterMethod(){
	 return alert("新增完畢");
}


function loadBeforeAjxService(){

	/*$.blockUI({
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
    });*/
 //alert("loadBeforeAjxService");
	var processString = "process_object_name=imMovementMainService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
	                    "&deliveryWarehouseCode=" + vat.item.getValueByName("#F.deliveryWarehouseCode") +
	                    "&arrivalWarehouseCode=" + vat.item.getValueByName("#F.arrivalWarehouseCode") +
	                    "&deliveryWarehouseManager=" + vat.item.getValueByName("#F.deliveryContactPerson") +
						"&arrivalWarehouseManager=" + vat.item.getValueByName("#F.arrivalContactPerson") +
						"&taxType=" + vat.item.getValueByName("#F.taxType") +
						"&itemCategory=" + vat.item.getValueByName("#F.itemCategory") +
						"&customsDeliveryWarehouseCode=" + vat.item.getValueByName("#F.customsDeliveryWarehouseCode") +
						"&customsArrivalWarehouseCode=" + vat.item.getValueByName("#F.customsArrivalWarehouseCode") +
						"&customsArrivalStoreCode=" + vat.item.getValueByName("#F.customsArrivalStoreCode") + // 轉入店別
	                    "&status=" + vat.item.getValueByName("#F.status");
	//alert("After loadBeforeAjxServiceEnd:"+processString);
	return processString;
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	return true;
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=imMovementMainService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status");
		//alert(processString);
	}

	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
   //alert("saveSuccessAfter");
   // vat.block.pageRefresh(vnB_Detail, {funcSuccess:function(){
        //alert("afterSavePageProcess:"+afterSavePageProcess);
		//var errorMsg = vat.utils.errorMsgResponse(vat.ajax.xmlHttp.responseText);
		var errorMsg="";
		if (errorMsg === "") {
			if ("saveHandler" == afterSavePageProcess) {
				executeCommandHandler("main", "saveHandler");
			} else if ("submitHandler" == afterSavePageProcess) {
				executeCommandHandler("main", "submitHandler");
			} else if ("voidHandler" == afterSavePageProcess) {
				executeCommandHandler("main", "voidHandler");
			} else if ("copyHandler" == afterSavePageProcess) {
				executeCommandHandler("main", "copyHandler");
			} else if ("executeExport" == afterSavePageProcess) {
				//vat.block.pageRefresh(vnB_Detail);
				executeCommandHandlerNoBlock("main","exportDataHandler");
			} else if ("pageRefresh" == afterSavePageProcess) {
				//vat.block.pageRefresh(vnB_Detail);
			} else if ("countTotalQuantity" == afterSavePageProcess) {
				countTotalQuantity();
			}else if ("changeRelationData" == afterSavePageProcess) {
			    //alert(afterSavePageProcess);
			    var processString = "process_object_name=imMovementMainService&process_object_method_name=updateItemRelationData" +
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
		                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
		                    "&deliveryWarehouseCode=" + vat.item.getValueByName("#F.deliveryWarehouseCode") +
		                    "&arrivalWarehouseCode=" + vat.item.getValueByName("#F.arrivalWarehouseCode") +
		                    "&deliveryWarehouseManager=" + vat.item.getValueByName("#F.deliveryContactPerson") +
							"&arrivalWarehouseManager=" + vat.item.getValueByName("#F.arrivalContactPerson") +
							"&taxType=" + vat.item.getValueByName("#F.taxType") +
							"&itemCategory=" + vat.item.getValueByName("#F.itemCategory") +
							"&customsDeliveryWarehouseCode=" + vat.item.getValueByName("#F.customsDeliveryWarehouseCode") +
							"&customsArrivalWarehouseCode=" + vat.item.getValueByName("#F.customsArrivalWarehouseCode") +
		                    "&customsArrivalStoreCode=" + vat.item.getValueByName("#F.customsArrivalStoreCode") +  // 轉入店別
		                    "&status=" + vat.item.getValueByName("#F.status");
				
		        vat.ajax.startRequest(processString, function () {
					if (vat.ajax.handleState()) {
				  		vat.block.pageRefresh(vnB_Detail);
				  		//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
					}
				});
			}
		} else {
			alert("錯誤訊息： " + errorMsg);
		}
		afterSavePageProcess = "";
	//}});


}


/*
	顯示合計的頁面
*/
function showTotalCountPage() {
    //save line
    vat.block.pageDataSave(vnB_Detail);
    afterSavePageProcess = "countTotalQuantity";
}

/*
	匯出
 */
function doExport() {
	//save line
	//alert("doExport");
	vat.block.pageDataSave(vnB_Detail);
	afterSavePageProcess = "executeExport";
}

function doPageDataSave(){
    //alert("doPageDataSave");
   // vat.block.pageDataSave(vnB_Detail);
    vat.block.pageSearch(vnB_Detail);
}

function doPageRefresh(){
    //alert("doPageRefresh");
    vat.block.pageRefresh(vnB_Detail);
}


function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();

}


function changeItemData(lineId) {

  var vsItemCategoryMode      = vat.bean().vatBeanOther.itemCategoryMode;
  var vsMovementType          = vat.bean().vatBeanOther.movementType;
  var vLineId                 = null == lineId ? vat.item.getGridLine():lineId;
  var vIndexNo                = vat.item.getGridValueByName("indexNo"  , vLineId);
  var vItemCode               = vat.item.getGridValueByName("itemCode" , vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  var vLotNo                  = vat.item.getGridValueByName("lotNo"    , vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  var vDeliveryWarehouseCode  = vat.item.getValueByName("#F.deliveryWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vArrivalWarehouseCode   = vat.item.getValueByName("#F.arrivalWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vDiffCategory           = false;
  var vItemLimitField         = vat.bean().vatBeanOther.itemLimitField == undefined?"":vat.bean().vatBeanOther.itemLimitField ;
  var vItemLimitOpt           = vat.bean().vatBeanOther.itemLimitOpt   == undefined?"":vat.bean().vatBeanOther.itemLimitOpt;
  var vItemLimitValue         = vat.bean().vatBeanOther.itemLimitValue == undefined?"":vat.bean().vatBeanOther.itemLimitValue;
  //Halert("vItemLimitation:"+ vItemLimitField+"/"+vItemLimitOpt+"/"+vItemLimitValue);
  var vProcessSqlName         = vLotNo==""?"FindItemInformationWithoutLotNo":"FindItemInformationWithLotNo";
  var isCorrectItemCategory   = true;
  var isCorrectTaxType        = true;
  var isCorrectItemLimitation = true;
  var isLimitString           = false
  var vDeliveryQuantity       = 0;
  vat.item.setGridValueByName("itemCode",vLineId, vItemCode);
  vat.item.setGridValueByName("deliveryWarehouseCode",vLineId, vDeliveryWarehouseCode);
  vat.item.setGridValueByName("arrivalWarehouseCode",vLineId , vArrivalWarehouseCode);
  if(vsMovementType == "ACCEPT" && vsItemCategoryMode == "Y"){
  	vLotNo = "000000000000";
  }

  var vProcessString =	"process_sql_code="+vProcessSqlName +"&"+
   										 	"brandCode=" + vat.item.getValueByName("#F.brandCode")+ "&"+
   											"itemCode="+vItemCode+"&"+
   											"warehouseCode="+ vDeliveryWarehouseCode +"&"+
   											(vLotNo==""?"":"lotNo="+vLotNo+"&")+
   											"warehouseManager="+ vat.item.getValueByName("#F.deliveryContactPerson")+"&"+
   											"employeeCode="+  vat.bean().vatBeanOther.loginEmployeeCode;


	vat.ajax.startRequest(vProcessString,  function() {
     if (vat.ajax.handleState()){
         if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
         	 if(vLotNo=="")
          	    vat.item.setGridValueByName("lotNo"                  ,vLineId, vat.ajax.getValue("LOT_NO" , vat.ajax.xmlHttp.responseText));

	         if("Y" == vsItemCategoryMode){
	            //檢查此商品是否為選則的業種
	            if(1 == vIndexNo){
	            	vsDiffCategory = vat.item.getValueByName("#F.itemCategory") !=vat.ajax.getValue("ITEM_CATEGORY", vat.ajax.xmlHttp.responseText);
	            	vat.item.setValueByName("#F.itemCategory",vat.ajax.getValue("ITEM_CATEGORY", vat.ajax.xmlHttp.responseText))
	            	if(vsDiffCategory) changeItemCategory();
	            	isCorrectItemCategory = true;
	            }else{
		            if(vat.item.getValueByName("#F.itemCategory") == "")
		               isCorrectItemCategory = true;
		            else
		               isCorrectItemCategory = vat.item.getValueByName("#F.itemCategory") == vat.ajax.getValue("ITEM_CATEGORY", vat.ajax.xmlHttp.responseText)?true:false;
		        }
		        //檢查稅別是否正確
		       	isCorrectTaxType = vat.item.getValueByName("#F.TaxType") == vat.ajax.getValue("IS_TAX", vat.ajax.xmlHttp.responseText)?true:false;
		        //檢查此單別是否允計此類別品號
		        if(vItemLimitOpt ==""){
		        	isCorrectItemLimitation = true;
		        }else{
		          //alert(vat.ajax.getValue(vItemLimitField, vat.ajax.xmlHttp.responseText));
		          //alert(vat.ajax.getValue("CATEGORY03", vat.ajax.xmlHttp.responseText));
		          //alert(vItemLimitValue.indexOf(vat.ajax.getValue(vItemLimitField, vat.ajax.xmlHttp.responseText)));

		        	isLimitString = vItemLimitValue.indexOf(vat.ajax.getValue(vItemLimitField, vat.ajax.xmlHttp.responseText))!=-1;
		        	if("IN" == vItemLimitOpt.toUpperCase()){
		        		isCorrectItemLimitation = isLimitString?true:false;
		        	}else if("NOTIN" == vItemLimitOpt.toUpperCase()){
		        	 	isCorrectItemLimitation = isLimitString?false:true;
		        	}else{
		        		isCorrectItemLimitation = true;
		        	}
		        }
	        }
	        if(isCorrectItemCategory && isCorrectTaxType && isCorrectItemLimitation){
	       	 	vat.item.setGridValueByName("itemCode"               ,vLineId, vat.ajax.getValue("ITEM_CODE", vat.ajax.xmlHttp.responseText));
  	      	 	vat.item.setGridValueByName("itemName"               ,vLineId, vat.ajax.getValue("ITEM_C_NAME", vat.ajax.xmlHttp.responseText));
    	   		vat.item.setGridValueByName("deliveryWarehouseCode"  ,vLineId, vDeliveryWarehouseCode);

            	vat.item.setGridValueByName("stockOnHandQuantity"    ,vLineId, vat.ajax.getValue("CURRENT_ON_HAND_QTY", vat.ajax.xmlHttp.responseText));
         	}else{
         		if(!isCorrectItemCategory && !isCorrectTaxType)
         			vat.item.setGridValueByName("itemName"               ,vLineId,  "商品業種及稅別錯誤");
         		else if(!isCorrectItemCategory)
         			vat.item.setGridValueByName("itemName"               ,vLineId,  "商品業種錯誤");
         		else if(!isCorrectTaxType)
         			vat.item.setGridValueByName("itemName"               ,vLineId,  "商品稅別錯誤");
         		else if(!isCorrectItemLimitation)
         			vat.item.setGridValueByName("itemName"               ,vLineId,  "商品類別錯誤，本單據"+
         			("IN" == vItemLimitOpt.toUpperCase()?"僅能":"不允許")+"輸入"+vItemLimitValue+"類型之商品");

            	vat.item.setGridValueByName("stockOnHandQuantity"    ,vLineId,  0.0);
         	}
         }else{
            vat.item.setGridValueByName("itemName"               ,vLineId,  "查無資料");
            vat.item.setGridValueByName("stockOnHandQuantity"    ,vLineId,  0.0);
         }
     }
  } )
}



/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
}


function openDiversityWindow(){
   
   url = "http://10.1.94.161:8080/crystal/t2/IM0614.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+ vat.item.getValueByName("#F.orderNo")+"&prompt2=&prompt3=&prompt4=&prompt5=&prompt6=&prompt7=&prompt8=&prompt9=&prompt10=";
   window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
   //return url;
  
}


//列印
function openReportWindow(type) {
alert(vat.item.getValueByName("#F.brandCode"));
    var barCodeType = "05_ImMovementHead";
    vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.reportFunctionCode = vat.item.getValueByName("#F.reportList");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    var exportBeanName = barCodeType.substring(3, barCodeType.length);
    if (vat.bean().vatBeanOther.reportFunctionCode == vat.item.getValueByName("#F.brandCode") + "_Z_BARCODE") {
        var url = "/erp/jsp/ExportBarCode.jsp" + "?fileType=TXT" + "&processObjectName=generateBarCodeService"
        	+ "&processObjectMethodName=executeMatch"
        	+ "&brandCode=" + vat.bean().vatBeanOther.brandCode
        	+ "&barCodeType=" + barCodeType
        	+ "&orderTypeCode=" + vat.bean().vatBeanOther.orderTypeCode
        	+ "&orderNo=" + vat.item.getValueByName("#F.orderNo")
        	+ "&orderNoEnd=" + vat.item.getValueByName("#F.orderNo")
        	+ "&orderBy="
        	+ "&price="
        	+ "&category="
        	+ "&category01="
        	+ "&category02="
        	+ "&warehouseCode="
       		+ "&taxType=" + "&startDate="
        	+ "&endDate=" + "&supplierCode="
        	+ "&timeScope=" + "&showZero"
        	+ "&exportBeanName="
        	+ exportBeanName;
        var width = "200";
        var height = "30";
        window.open(url, '條碼子系統', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width) / 2 + ',top=' + (screen.availHeight - height) / 2);
    } else if (vat.bean().vatBeanOther.reportFunctionCode.indexOf("_PRINT_BARCODE")>-1) {
    	var exportType = "IMM_ITEM";
    	if( "T1BS" == vat.bean().vatBeanOther.brandCode || "T1CO" == vat.bean().vatBeanOther.brandCode )
    		exportType = "IMM_ITEM_T1";
        url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName=" + exportType +
              "&fileType=TXT" +
              "&processObjectName=imMovementMainService" +
              "&processObjectMethodName=exportBarCode" +
              "&exportType=barcode" +
              "&headId=" + vat.item.getValueByName("#F.headId") +
              "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
              "&orderTypeCode=" + vat.bean().vatBeanOther.orderTypeCode ;
        var width ='200';
        var height = '30';
        window.open(url, '調撥單條碼單檔案列印', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
    } else {
        if ("AFTER_SUBMIT" != type) vat.bean().vatBeanOther.orderNo = vat.item.getValueByName("#F.orderNo");
        vat.block.submit(function () {
            return "process_object_name=imMovementMainService" + "&process_object_method_name=getReportConfig";
        }, {
            other: true,
            funcSuccess: function () {
                //vat.item.setValueByName("#F.remark2", vat.bean().vatBeanOther.reportUrl);
                eval(vat.bean().vatBeanOther.reportUrl);

                if ("AFTER_SUBMIT" == type) {
                    if ("Y" == vat.bean().vatBeanOther.updateForm) {
                        closeWindows("");
                    } else createRefreshForm();
                }
            }
        });
    }
}

function changeRelationData(){

    afterSavePageProcess = "changeRelationData";
   // alert("aa");
    vat.block.pageDataSave(vnB_Detail);
 //   doPageDataSave();

}


function changeWarehouseCode(warehouseType) {
	//alert(vat.bean().vatBeanOther.itemCategoryMode);
	//vat.block.pageRefresh(vnB_Detail);
//	if("delivery" == warehouseType)
//		vat.item.setValueByName("#F.deliveryWarehouseCodeText",vat.item.getValueByName("#F.deliveryWarehouseCode"));
//	else
//		vat.item.setValueByName("#F.arrivalWarehouseCodeText",vat.item.getValueByName("#F.arrivalWarehouseCode"));

	changeItemCategory();

	if(vat.item.getValueByName("#F.deliveryWarehouseCode")== vat.item.getValueByName("#F.arrivalWarehouseCode")){
		if(warehouseType == "delivery")
	  		alert("您輸入的「轉出庫別」與轉入庫別相同，請重新輸入");
	  	else
	  		alert("您輸入的「轉入庫別」與轉出庫別相同，請重新輸入");
	}
}


function doSubmit(formAction){
	var vsAllowSubmit         = true;
	var alertMessage          ="是否確定送出?";
	var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');;
    var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');;
    var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');;
    var employeeCode          = vat.bean().vatBeanOther.loginEmployeeCode.replace(/^\s+|\s+$/, '');;
    var originalOrderTypeCode = vat.item.getValueByName("#F.originalOrderTypeCode").replace(/^\s+|\s+$/, '');;
    var originalOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode").replace(/^\s+|\s+$/, '');;
    var inProcessing          = !(processId == null || processId == ""  || processId == 0);
    var orderNoPrefix         = vat.item.getValueByName("#F.orderNo").substring(0,3);
    var approvalResult        = vat.item.getValueByName("#F.approvalResult");
    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
    var fixSuppliers =  vat.item.getValueByName("#F.allFixSuppliers");
    var processString 	      ="";
    countTotalQuantity();
	if("SUBMIT" == formAction || "SUBMIT_BG" == formAction){
		if(approvalResult)
			alertMessage = "是否確定送出?";
		else
			alertMessage = "是否確定駁回此單據?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("SUBMIT_BG" == formAction){
	 	alertMessage = "是否確定背景送出?";
	}


	if(confirm(alertMessage)){
	    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
		var vsStatus = vat.item.getValueByName("#F.status");
		var vsComfirmedBy = vat.item.getValueByName("#F.comfirmedBy");
		var vsPackedBy = vat.item.getValueByName("#F.packedBy");
		var vsReceiptedBy = vat.item.getValueByName("#F.receiptedBy");
		var arrivalWarehouse = vat.item.getValueByName("#F.arrivalWarehouseCode");
		var vsAllowSubmit = true;
	    vat.block.pageDataSave(vnB_Detail,
		    {  funcSuccess:function(){
		    	//countTotalQuantity();
				if("SUBMIT" == formAction || "SUBMIT_BG" == formAction){
					vsAllowSubmit = checkHeadLineWarehouse();
					if("T2" == vsBrandCode){
						if("" == vsPackedBy && "SAVE" == vsStatus){
							alert("請輸入揀貨人員工號");
							vsAllowSubmit = false;
						}
						if("WAIT_OUT" == vsStatus){
							if("" == vsComfirmedBy){
								alert("請輸入複核人員工號");
								vsAllowSubmit = false;
							}else if(vsComfirmedBy == vsPackedBy){
								alert("請輸入複核人員不可與揀貨人員相同");
								vsAllowSubmit = false;
							}
						}
						if("WAIT_IN" == vsStatus){
							//alert(vat.item.getValueByName("#F.receiptedBy"));
							if("" == vsReceiptedBy){
								alert("請輸入收貨人員工號");
								vsAllowSubmit = false;
							}else if(vsReceiptedBy == vsPackedBy){
								alert("請輸入收貨人員不可與揀貨人員相同");
								vsAllowSubmit = false;
							}
						}
					}
					if(vat.item.getValueByName("#F.deliveryWarehouseCode")== vat.item.getValueByName("#F.arrivalWarehouseCode")){
	  					alert("您輸入的「轉出/入庫別」與轉入庫別相同，請重新輸入");
	  					vsAllowSubmit = false;
					}
					if(arrivalWarehouse == ""){
						alert("轉入庫不得為空!!");
						vsAllowSubmit = false;
					}
				}

				if(!inProcessing && "VOID" == formAction){
					if(status == "FINISH" ||(status == "WAIT_IN"  &&
						vat.item.getValueByName("#F.customsDeliveryWarehouseCode") != vat.item.getValueByName("#F.customsArrivalWarehouseCode"))){
						vsAllowSubmit = true;
					}else
						vsAllowSubmit = false;
		 		}else{
					if(!((orderNoPrefix == "TMP" &&  status == "SAVE") || status == "UNCONFIRMED" ||
						   (inProcessing   && (status == "SAVE"  || status == "SIGNING" || status == "REJECT" || status == "WAIT_OUT" || status == "WAIT_IN")) ||
						   ("TT" == originalOrderTypeCode && status == "WAIT_IN"))){
						alert("您的表單已加入待辦事件，請從待辦事件選取後，再次送出!");
						vsAllowSubmit = false;

					}
				}
//				alert(vat.item.getValueByName("#F.itemCount"));
//		    	if(vat.item.getValueByName("#F.itemCount") == 0){
//					alert("總數量為零不可送出!");
//					vsAllowSubmit = false;
//				}
		    	if(vsAllowSubmit){

				  vat.bean().vatBeanOther.status          = status;
				  vat.bean().vatBeanOther.processId       = processId;
				  vat.bean().vatBeanOther.formAction      = formAction;
				  vat.bean().vatBeanOther.approvalResult  = approvalResult;
				  vat.bean().vatBeanOther.approvalComment = approvalComment;
                  vat.bean().vatBeanOther.fixSuppliers = fixSuppliers;

				  //alert(approvalResult);

			    // alert("prepare exec");
			      if("SUBMIT_BG" == formAction){ //背景送出
			      	vat.bean().vatBeanOther.action = "SAVE";
			      	processString = "process_object_name=imMovementMainAction&process_object_method_name=getOrderNo";
			      }else if("VOID" == formAction){ //作廢
			        //alert("VOID");
			      	vat.bean().vatBeanOther.action = "VOID";
			      	if(!inProcessing)
			      		processString = "process_object_name=imMovementMainAction&process_object_method_name=voidMovement";
			      	else
			      		processString = "process_object_name=imMovementMainAction&process_object_method_name=performTransaction";

//			      	if("WAIT_OUT"==vsStatus||"WAIT_IN"==vsStatus){   //作廢，並恢復庫存
//			      		processString = "process_object_name=imMovementAction&process_object_method_name=voidMovement";
//			      	}else if("REJECT"==vsStatus||"SAVE"==vsStatus){  //僅作廢，不用恢復庫存
//			      		processString = "process_object_name=imMovementAction&process_object_method_name=performTransaction";
//			      	}
//


//			      }else if("VOID" == formAction &&("REJECT"==vsStatus||"SAVE"==vsStatus)){
//			      	vat.bean().vatBeanOther.action = "VOID";

			      }else{
			      	//alert(document.forms[0]["#orderTypeCode"     ].value);
			      	if(approvalResult){
			      		if(status == "WAIT_IN" && vat.item.getValueByName("#F.arrivalDate") == ""){
			      			// 如果單據是『待轉入』狀態，轉入日期為空，則自動帶入系統日期 by Weichun 2012.03.26
			      			var d = new Date();
			      			var str = d.format('yyyy/M/d');
			      			vat.item.setValueByName("#F.arrivalDate", str);
			      		}
				      	vat.bean().vatBeanOther.action = "SAVE";   //送出 核准
				         
				      	    processString = "process_object_name=imMovementMainAction&process_object_method_name=performTransaction";
				      	
					}else{
						vat.bean().vatBeanOther.action = "REJECT"; //送出 駁回
						processString = "process_object_name=imMovementMainAction&process_object_method_name=voidMovement";
					}

		          }
				  vat.block.submit(function(){return processString;},{
			                    bind:true, link:true, other:true,
			                    funcSuccess:function(){
					        	//	vat.block.pageRefresh(vnB_Detail);
					        	}}
				  );
	          	}
          	}
       	  }
       );
	}
}

function kweFormClear(){

    vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     ].value,
         processId          : "",
	     formId             : "",
	     assignmentId       : ""
	    };

 	 vat.block.submit(function(){return "process_object_name=imMovementMainService"+
			                    "&process_object_method_name=executeInitial";}, {other:true});

}

function doSearch(){
	window.showModalDialog("Im_Movement:search:20090720.page?orderTypeCode="+ vat.bean().vatBeanOther.orderTypeCode , "",
		"dialogHeight:768px;dialogWidth:1024px;dialogTop:250px;dialogLeft:100px;status:no;");
}

function getOriginalDeliveryQuantityTitle(){
  var vsOriginalDeliveryQuantityTitle = "";
  if(vat.bean().vatBeanOther.isThreePointMoving == true)
		vsOriginalDeliveryQuantityTitle = "預計轉出量";
  return vsOriginalDeliveryQuantityTitle;
}


function getOriginalDeliveryQuantityStyle(vsFormStatus){
  var vsOriginalDeliveryQuantityStyle ="";
  //alert(vat.bean().vatBeanOther.showOriginalDeliveryQty);
  if(vsFormStatus == "SAVE"  ||  vsFormStatus == "REJECT"  || vsFormStatus == "UNCONFIRMED"  ||
	 vsFormStatus == "FINISH"  || vsFormStatus == "CLOSE" || vsFormStatus == "SIGNING" ){
	 if( vat.bean().vatBeanOther.showOriginalDeliveryQty =="Y"){
	 	 vsOriginalDeliveryQuantityStyle = "inline";
	 	 if(vat.bean().vatBeanOther.isThreePointMoving == true )
	 	 	vat.item.setGridAttributeByName("originalDeliveryQuantity" , "readOnly", false);
	 	 else
	 	 	vat.item.setGridAttributeByName("originalDeliveryQuantity" , "readOnly", true);
	 }else{
	 	if(vat.bean().vatBeanOther.isThreePointMoving == true )
	 	   vsOriginalDeliveryQuantityStyle = "inline";
	 	else
	 	   vsOriginalDeliveryQuantityStyle = "none";
	}
  }else if(vsFormStatus == "WAIT_OUT" ){
	vsOriginalDeliveryQuantityStyle = "inline";
  }else if(vsFormStatus == "WAIT_IN"){
	vsOriginalDeliveryQuantityStyle = "none";
  }else{
	vsOriginalDeliveryQuantityStyle = "inline";
  }
  return vsOriginalDeliveryQuantityStyle;
}

function getDeliveryQuantityStyle(vsFormStatus){
  if(vsFormStatus == "SAVE"  ||  vsFormStatus == "SIGNING"  || vsFormStatus == "UNCONFIRMED" ){
    if(vat.bean().vatBeanOther.isThreePointMoving == true)
  		  vsDeliveryQuantityStyle = "none";
  	 else
  	    vsDeliveryQuantityStyle = "inline";
  }else{
  	vsDeliveryQuantityStyle = "inline";
  }
  return vsDeliveryQuantityStyle;
}


function getArrivalQuantityStyle(vsFormStatus){
  if(vsFormStatus == "WAIT_IN"){
  	vsDeliveryQuantityStyle = "inline";
  }else{
  	vsDeliveryQuantityStyle = "none";
  }
  return vsDeliveryQuantityStyle;
}

function checkQuantity(){
  var vLineId                   = vat.item.getGridLine();
  var vDeliveryQuantity         = vat.item.getGridValueByName("deliveryQuantity"         , vLineId);
  var vOriginalDeliveryQuantity = vat.item.getGridValueByName("originalDeliveryQuantity" , vLineId);
  var vStockOnHandQuantity      = vat.item.getGridValueByName("stockOnHandQuantity"      , vLineId);
  var vsOrderTypeCode           = vat.item.getValueByName("#F.orderTypeCode");
  var vsFormStatus              = vat.item.getValueByName("#F.status");
  var allowMinusStock  	        = vat.item.getValueByName("#F.allowMinusStock" );
  //vat.item.setGridValueByName("arrivalQuantity" , vLineId, vDeliveryQuantity);
  if(!("Y" == allowMinusStock)){
  	if(vat.bean().vatBeanOther.isThreePointMoving ){
    	if(vsFormStatus == "SAVE" || vsFormStatus == "REJECT" ||vsFormStatus == "UNCONFIRMED"){
        	if( parseInt(vStockOnHandQuantity) < parseInt(vOriginalDeliveryQuantity)){
         		alert("預計出庫量("+vOriginalDeliveryQuantity+")不可大於目前可用數量("+ vStockOnHandQuantity+")");
      	 	}
     	}else{
     		if("N" == vat.bean().vatBeanOther.allowMoreQty){
       			if( parseInt(vOriginalDeliveryQuantity) < parseInt(vDeliveryQuantity)){
        	 		alert("實際出庫量("+vDeliveryQuantity+")不可大於預計出庫量("+ vOriginalDeliveryQuantity+")");
        		}
       		}
     	}
  	}else{
     	if( parseInt(vStockOnHandQuantity) < parseInt(vDeliveryQuantity)){
       		alert("出庫數量("+vDeliveryQuantity+")不可大於目前可用數量("+ vStockOnHandQuantity+")");
     	}
  	}
  }
  if( parseInt(vDeliveryQuantity) < 0 ){
       alert("出庫數量("+vDeliveryQuantity+")不可小於「0」");
  }
}

function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	refreshForm("");
	 }
}

//調撥單匯出明細
function exportFormData(){
    alert("export to xml file...:"+vat.item.getValueByName("#F.brandCode"))
	var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var beanName = "";
	var url;
	if( vat.item.getValueByName("#F.brandCode").indexOf("T2") > -1){ // for T2
	    alert("&brandCode2222222=" + vat.item.getValueByName("#F.brandCode"));
		beanName = vat.bean().vatBeanOther.isThreePointMoving ?"T2_IM_MOVEMENT_ITEM_3":"T2_IM_MOVEMENT_ITEM_2";
    	url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&processObjectName=imMovementMainService" +
              "&processObjectMethodName=exportExcelDetail" +
              "&gridFieldName=imMovementItems" +
              "&headId=" + vat.item.getValueByName("#F.headId")
              "&brandCode=" + vat.item.getValueByName("#F.brandCode");
	}else{ // for T2以外
		alert("&brandCode=" + vat.item.getValueByName("#F.brandCode"));
		beanName = vat.bean().vatBeanOther.isThreePointMoving ?"T1_IM_MOVEMENT_ITEM_3":"T1_IM_MOVEMENT_ITEM_2";
		url = "/erp/jsp/ExportFormData.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&processObjectName=imMovementMainService" +
              "&processObjectMethodName=findById" +
              "&gridFieldName=imMovementItems" +
              "&arguments=" + vat.item.getValueByName("#F.headId") +
              "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
              "&parameterTypes=LONG";
	}

    var width = "200";
    var height = "30";
    vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : function(){
    			window.open(url, '調撥單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});

}

function importFormData(){
	var vsOrderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");
    var beanName = "";
    var suffix ="";
	if( vat.item.getValueByName("#F.brandCode").indexOf("T2") > -1 ){
		beanName = vat.bean().vatBeanOther.isThreePointMoving ?"T2_IM_MOVEMENT_ITEM_3":"T2_IM_MOVEMENT_ITEM_2";
	}else{
		beanName = vat.bean().vatBeanOther.isThreePointMoving ?"T1_IM_MOVEMENT_ITEM_3":"T1_IM_MOVEMENT_ITEM_2";
	}

	suffix =
		"&importBeanName="+ beanName +
		"&importFileType=XLS" +
        "&processObjectName=imMovementMainService" +
        "&processObjectMethodName=executeImportMovementItems" +
        "&arguments=" +vat.item.getValueByName("#F.headId")  +
        "&parameterTypes=LONG" +
        "&blockId=" + vnB_Detail
		//'menubar=no,resizable=no,scrollbars=no,status=no,left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2;

	return suffix;

}


function doFormAccessControl(){
       
  	var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
  	var vsFormStatus    = vat.item.getValueByName("#F.status");
  	var vsProcessId     = vat.bean().vatBeanOther.processId;
  	var vsActivityStatus= vat.bean().vatBeanOther.activityStatus;
  	var vsOrderNoPrefix = vat.item.getValueByName("#F.orderNo").substring(0, 3);
	var vsInProcessVoidMode   = vat.bean().vatBeanOther.voidMode.substring(0,1);
	var vsNotInProcessVoidMode= vat.bean().vatBeanOther.voidMode.substring(1,2);
	var vbCheckEmployeeLineStyle = true;
	vat.item.setStyleByName("#B.new"         , "display", "inline");
	vat.item.setStyleByName("#B.search"      , "display", "inline");
	vat.item.setStyleByName("#B.exit"        , "display", "inline");
	vat.item.setStyleByName("#B.submit"      , "display", "inline");
	vat.item.setStyleByName("#B.save"        , "display", "inline");
	vat.item.setStyleByName("#B.void"        , "display", "none");
	vat.item.setStyleByName("#B.void1"        , "display", "inline");
	vat.item.setStyleByName("#B.void2"        , "display", "inline");
	vat.item.setStyleByName("#F.reportList"  , "display", "inline");
	vat.item.setStyleByName("#B.print"       , "display", "inline");
	vat.item.setStyleByName("#B.submitBG"    , "display", "inline");
	vat.item.setStyleByName("#B.message"     , "display", "inline");
	vat.item.setStyleByName("#B.declaration" , "display", "inline");
	vat.item.setStyleByName("#B.export"      , "display", "inline");
	vat.item.setStyleByName("#B.import"      , "display", "inline");
	vat.item.setStyleByName("#B.first"       , "display", "inline");
	vat.item.setStyleByName("#B.forward"     , "display", "inline");
	vat.item.setStyleByName("#B.next"        , "display", "inline");
	vat.item.setStyleByName("#B.last"        , "display", "inline");
	vat.item.setStyleByName("#B.sendCustoms"        , "display", "inline");
	vat.item.setStyleByName("#B.sendBack"        , "display", "inline");
	vat.item.setStyleByName("#B.sendCancel"        , "display", "inline");
	vat.item.setAttributeByName("#F.itemCategory", "readOnly", false);
	vat.item.setAttributeByName("#F.deliveryWarehouseCode", "readOnly", false);
	vat.item.setAttributeByName("#F.arrivalWarehouseCode" , "readOnly", false);
	vat.item.setAttributeByName("#F.deliveryDate", "readOnly", false);
	vat.item.setAttributeByName("#F.arrivalDate" , "readOnly", false);
	vat.item.setAttributeByName("#F.packedBy"    , "readOnly", false);
	vat.item.setAttributeByName("#F.comfirmedBy" , "readOnly", false);
	vat.item.setAttributeByName("#F.receiptedBy" , "readOnly", false);
	vat.item.setAttributeByName("#F.originalOrderTypeCode"    , "readOnly", true);
 	vat.item.setAttributeByName("#F.originalOrderNo"          , "readOnly", true);
	vat.item.setGridAttributeByName("boxNo"    , "readOnly", false);
	vat.item.setGridAttributeByName("itemCode" , "readOnly", false);
	vat.item.setGridAttributeByName("lotNo"    , "readOnly", false);
	vat.item.setGridAttributeByName("originalDeliveryQuantity" , "readOnly", false);
	vat.item.setGridAttributeByName("deliveryQuantity", "readOnly", false);
	vat.item.setGridAttributeByName("arrivalQuantity" , "readOnly", true);
	vat.item.setGridAttributeByName("originalDeclarationNo" , "readOnly", false);
	vat.item.setGridAttributeByName("originalDeclarationSeq" , "readOnly", false);

	vat.block.canGridModify(vnB_Detail, true);
	changeDeclarationStyle();
    
    if(vsFormStatus=="FINISH"||vsFormStatus=="CLOSE"){
       vat.item.setStyleByName("#B.diversityReport"         , "display", "inline");
    }else{
       vat.item.setStyleByName("#B.diversityReport"         , "display", "none"); 
    }
    
	//for 儲位用1
	if(enableStorage){
		vat.item.setStyleByName("#B.storageExport"   , "display", "inline");
		vat.item.setStyleByName("#B.storageImport"   , "display", "inline");
	}else{
		vat.item.setStyleByName("#B.storageExport"   , "display", "none");
		vat.item.setStyleByName("#B.storageImport"   , "display", "none");
	}
	
	if(  vsFormStatus != ""   ){

		if(vsProcessId == 0 ||vsProcessId == null || vsActivityStatus != "WF_ACT_WAIT" ){//使用Picker或從查詢功能選入
			if(vsOrderNoPrefix=="TMP" ||  vsFormStatus == "UNCONFIRMED"){
				vat.item.setStyleByName("#B.void"        , "display", "none");
				vat.item.setStyleByName("#F.reportList"  , "display", "none");
				vat.item.setStyleByName("#B.print"       , "display", "none");
				if("T2" == vat.item.getValueByName("#F.brandCode") &&
				   "WCF" != vat.item.getValueByName("#F.orderTypeCode") &&
				   "WCP" != vat.item.getValueByName("#F.orderTypeCode")){
					vat.item.setAttributeByName("#F.originalOrderTypeCode"    , "readOnly", false);
 			    	vat.item.setAttributeByName("#F.originalOrderNo"          , "readOnly", false);
 			    }
				vbCheckEmployeeLineStyle = true;

			}else{
				vat.item.setStyleByName("#B.submit"      , "display", "none");
				vat.item.setStyleByName("#B.save"        , "display", "none");
				vat.item.setStyleByName("#B.submitBG"    , "display", "none");
				vat.item.setStyleByName("#B.declaration" , "display", "none");
				vat.item.setStyleByName("#B.import"      , "display", "none");

				vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
				vat.item.setAttributeByName("vatBlock_Master", "readOnly", true);
				vat.item.setAttributeByName("vatDetailDiv"   , "readOnly", true);
				vat.block.canGridModify(vnB_Detail, false);
				vat.item.setAttributeByName("#F.approvalResult","readOnly", true);
				 if(vsFormStatus == "WAIT_OUT" || vsFormStatus == "WAIT_IN" || vsFormStatus == "SIGNING" ||
		 		    vsFormStatus == "FINISH"  || vsFormStatus == "CLOSE" ){
		 			vat.item.setGridStyleByName("stockOnHandQuantity", "display", "none");
		 			if("Y" == vsNotInProcessVoidMode && (vat.item.getValueByName("#F.cmMovementNo") == "" || vat.item.getValueByName("#F.cmMovementNo") == null ) ){
						if((vsFormStatus == "WAIT_IN" && vat.item.getValueByName("#F.customsDeliveryWarehouseCode") != vat.item.getValueByName("#F.customsArrivalWarehouseCode"))){
							vat.item.setStyleByName("#B.void", "display", "inline");
						}else
							vat.item.setStyleByName("#B.void", "display", "none");
		 			}else{
		 				vat.item.setStyleByName("#B.void" , "display", "none");
		 			}
		 		}
		 		vbCheckEmployeeLineStyle = false;
			}
		}else{ //從待辦事項進入

			vbCheckEmployeeLineStyle = true;
            
			if(vsFormStatus == "SAVE" || vsFormStatus == "UNCONFIRMED"  || vsFormStatus == "REJECT" ){
				vat.item.setAttributeByName("#F.deliveryDate", "readOnly", false );
		  		vat.item.setAttributeByName("#F.arrivalDate" , "readOnly", true);
		  		vat.item.setAttributeByName("#F.packedBy"    , "readOnly", false);
				vat.item.setAttributeByName("#F.comfirmedBy" , "readOnly", true);
		  		vat.item.setAttributeByName("#F.receiptedBy" , "readOnly", true);
		  		vat.item.setStyleByName("#B.void" , "display", "inline");
				if(vat.bean().vatBeanOther.isThreePointMoving == true)
					vat.item.setGridAttributeByName("deliveryQuantity", "readOnly", true);
				else
					vat.item.setGridAttributeByName("deliveryQuantity", "readOnly", false);
			}else if( vsFormStatus == "WAIT_OUT" || vsFormStatus == "WAIT_IN" || vsFormStatus == "SIGNING"  )	{

				vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
				vat.item.setAttributeByName("vatBlock_Master", "readOnly", true);
				vat.item.setAttributeByName("vatDetailDiv"   , "readOnly", true);
				vat.item.setStyleByName("#B.new"         , "display", "none");
				vat.item.setStyleByName("#B.search"      , "display", "none");
				vat.item.setStyleByName("#B.save"        , "display", "none");

				vat.item.setStyleByName("#B.submitBG"    , "display", "none");
				vat.item.setStyleByName("#B.declaration" , "display", "none");
				vat.item.setStyleByName("#B.import"      , "display", "none");
				vat.item.setStyleByName("#B.first"       , "display", "none");
				vat.item.setStyleByName("#B.forward"     , "display", "none");
				vat.item.setStyleByName("#B.next"        , "display", "none");
				vat.item.setStyleByName("#B.last"        , "display", "none");
				vat.item.setStyleByName("#B.void"        , "display", "none");
				vat.item.setAttributeByName("#F.approvalResult","readOnly", true);
				if( vsFormStatus == "WAIT_OUT"){
					vat.item.setAttributeByName("#F.deliveryDate", "readOnly", false );
		  		  	vat.item.setAttributeByName("#F.arrivalDate" , "readOnly", true);
		  		  	vat.item.setAttributeByName("#F.comfirmedBy" , "readOnly", false);
		  		  	vat.item.setAttributeByName("#F.packedBy"    , "readOnly", true);
		  		  	vat.item.setAttributeByName("#F.receiptedBy" , "readOnly", true);
				    vat.item.setGridAttributeByName("deliveryQuantity", "readOnly", false);
				    //vat.item.setGridAttributeByName("arrivalQuantity" , "readOnly", true);
		 		   	//vat.item.setGridStyleByName("stockOnHandQuantity" , "display", "none");

		 		   	if(("B" == vsInProcessVoidMode || "O" == vsInProcessVoidMode))
		 		   		vat.item.setAttributeByName("#F.approvalResult","readOnly", false);
		 		}else if( vsFormStatus == "WAIT_IN"){
		 			vat.item.setAttributeByName("#F.receiptedBy" , "readOnly", false);
		 			vat.item.setAttributeByName("#F.arrivalDate" , "readOnly", false);
		 			vat.item.setAttributeByName("#F.packedBy"    , "readOnly", true);
					vat.item.setAttributeByName("#F.deliveryDate", "readOnly", true );
					vat.item.setAttributeByName("#F.comfirmedBy" , "readOnly", true);
				    vat.item.setGridAttributeByName("deliveryQuantity", "readOnly", true);
				    //vat.item.setGridAttributeByName("arrivalQuantity" , "readOnly", true);
				    vat.block.canGridModify(vnB_Detail, false);

				    if(("B" == vsInProcessVoidMode || "I" == vsInProcessVoidMode))
		 		   		vat.item.setAttributeByName("#F.approvalResult","readOnly", false);
		 		}else if( vsFormStatus == "SIGNING"){
		 		   		vat.item.setStyleByName("#B.void"   , "display", "none");
		 		   		//vat.item.setStyleByName("#B.submit" , "display", "none");
		 		}
		 		vat.item.setGridStyleByName("stockOnHandQuantity", "display", "none");

			}else{
			    
				vat.item.setStyleByName("#B.save"   , "display", "none");
				vat.item.setStyleByName("#B.void"   , "display", "none");
				vat.item.setStyleByName("#B.submit" , "display", "none");
				vat.item.setStyleByName("#B.import" , "display", "none");
			}
		}
		if(vat.item.getValueByName("#F.brandCode").indexOf("T2") > -1) {
		 	if("WCF" != vat.item.getValueByName("#F.orderTypeCode") &&
		       "WCP" != vat.item.getValueByName("#F.orderTypeCode")){
		     	vat.item.setAttributeByName("#F.arrivalDate" , "readOnly", true);
		    }
		 	vat.item.setAttributeByName("#F.originalOrderTypeCode"    , "readOnly", false);
 			vat.item.setAttributeByName("#F.originalOrderNo"          , "readOnly", false);
 		}
	}
	if(vbCheckEmployeeLineStyle){
		if("SAVE"==vsFormStatus ||"REJECT"==vsFormStatus || "VOID"==vsFormStatus){
			vat.item.setAttributeByName("#F.packedBy", "readOnly", false);
			vat.item.setAttributeByName("#F.comfirmedBy", "readOnly", true);
			vat.item.setAttributeByName("#F.receiptedBy", "readOnly", true);
			//vat.item.setAttributeByName("#F.approvalResult","readOnly", true);
		}else if("WAIT_OUT"==vsFormStatus){
			vat.item.setAttributeByName("#F.packedBy", "readOnly", true);
			vat.item.setAttributeByName("#F.comfirmedBy", "readOnly", false);
			vat.item.setAttributeByName("#F.receiptedBy", "readOnly", true);
		}else if("WAIT_IN"==vsFormStatus){
			vat.item.setAttributeByName("#F.packedBy", "readOnly", true);
			vat.item.setAttributeByName("#F.comfirmedBy", "readOnly", true);
			vat.item.setAttributeByName("#F.receiptedBy", "readOnly", false);
			//vat.item.setAttributeByName("#F.approvalResult","readOnly", true);
		}else{
			vat.item.setAttributeByName("#F.packedBy", "readOnly", true);
			vat.item.setAttributeByName("#F.comfirmedBy", "readOnly", true);
			vat.item.setAttributeByName("#F.receiptedBy", "readOnly", true);
			//vat.item.setAttributeByName("#F.approvalResult","readOnly", true);
		}
	}
	vat.item.setGridStyleByName("originalDeliveryQuantity", "display", getOriginalDeliveryQuantityStyle(vsFormStatus));
	vat.item.setGridStyleByName("deliveryQuantity"        , "display", getDeliveryQuantityStyle(vsFormStatus));
	vat.item.setGridStyleByName("arrivalQuantity"         , "display", getArrivalQuantityStyle(vsFormStatus));
    vat.item.setGridStyleByName("unit"         , "display", "inline");
    
	if("Y" != vat.bean().vatBeanOther.itemCategoryMode){
		vat.item.setAttributeByName("#F.itemCategory", "readOnly", true);
	}
	if(vat.item.getValueByName("#F.brandCode").indexOf("T2") == -1){
	 	vat.item.setGridStyleByName("boxNo" , "display", "none");
	}

	// 單別為WFF WGF WHF& WFP WGP WHP才需要輸入"轉入店別"
	var orderTypeShow = vat.item.getValueByName("#F.orderTypeCode");
	if("WFF" != orderTypeShow && "WFP" != orderTypeShow && "WGF" != orderTypeShow && "WGP" != orderTypeShow && "WHF" != orderTypeShow && "WHP" != orderTypeShow){
		vat.item.setAttributeByName("#F.arrivalStoreCode", "readOnly", "true");
	}else{
		// 轉入庫別不是"D6100"，無須輸入轉入店別
		var arrivalWarehouse = vat.item.getValueByName("#F.arrivalWarehouseCode");
		if("D6100" != arrivalWarehouse){
			vat.item.setAttributeByName("#F.arrivalStoreCode", "readOnly", true);
		}
	}
   
}

function doAfterPickerProcess(){
	//alert("doAfterPickerProcess")
	if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
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

function refreshForm(vsHeadId) {
    document.forms[0]["#formId"].value = vsHeadId;
    document.forms[0]["#processId"].value = "";
    document.forms[0]["#assignmentId"].value = "";

    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
    vat.bean().vatBeanOther.updateForm = document.forms[0]["#formId"].value == "" ? "N" : "Y";
    vat.bean().vatBeanOther.processId = document.forms[0]["#processId"].value;
    vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"].value;
    vat.bean().vatBeanOther.itemCategory = vat.item.getValueByName("#F.itemCategory");
    //alert("itemCategory:"+vat.item.getValueByName("#F.itemCategory"));
    //alert("currentRecordNumber:"+vat.bean().vatBeanOther.currentRecordNumber);
    //alert("formId:"+vat.bean().vatBeanOther.formId +" processId:"+vat.bean().vatBeanOther.processId +" assignmentId:"+vat.bean().vatBeanOther.assignmentId);
    vat.block.submit(

    function () {
        return "process_object_name=imMovementMainService&process_object_method_name=executeInitial";
    }, {
        other: true,
        funcSuccess: function () {
            vat.item.bindAll();
            refreshWfParameter(vat.item.getValueByName("#F.brandCode"), vat.item.getValueByName("#F.orderTypeCode"), vat.item.getValueByName("#F.orderNo"));
            vat.block.pageRefresh(vnB_Detail);
            doFormAccessControl();
            vat.tabm.displayToggle(0, "xTab5", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false)
        }
    });
}

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoNext(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1;
	  	vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    vsHeadId = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(vsHeadId);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}

}

// 展報單
function execExtendItemInfo() {
    //隱藏核銷報單按鈕 by Weichun 2011.03.15
    vat.item.setStyleByName("#B.declaration", "display", "none");
    /*
    $.blockUI({
        message: '<font size=4 color="#000000"><b>展報單，請稍後...</b></font>',
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
    */
    
    var vsItemCount = 0;
    var vsStatus = vat.item.getValueByName("#F.status");
    var vsHeadId = vat.item.getValueByName("#F.headId");
    var vsItemCountField = "DELIVERY_QUANTITY";
    vat.block.pageDataSave(vnB_Detail, {
    	asyn: true,
        saveSuccessAfter: function () {
    		//alert("do....it....");
            processString = "process_sql_code=FindMovementItemCount&headId=" + vsHeadId;
            vat.ajax.startRequest(processString, function () {
                if (vat.ajax.handleState()) {
                    if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
                        if ("SAVE" == vsStatus || "UNCONFIRMED" == vsStatus) {
                            if (vat.bean().vatBeanOther.isThreePointMoving == true) vsItemCountField = "ORIGINAL_DELIVERY_QUANTITY";
                            else vsItemCountField = "DELIVERY_QUANTITY";
                        } else if ("WAIT_OUT" == vsStatus) {
                            vsItemCountField = "DELIVERY_QUANTITY";
                        } else if ("WAIT_IN" == vsStatus) {
                            vsItemCountField = "ARRIVAL_QUANTITY";
                        }
                        vsItemCount = vat.ajax.getValue(vsItemCountField, vat.ajax.xmlHttp.responseText);
						//alert(vsItemCount);
                        if (null != vsItemCount && 0 != vsItemCount) {
                            vat.bean().vatBeanOther.processObjectName = "imMovementMainService";
                            vat.bean().vatBeanOther.searchMethodName = "findById";
                            vat.bean().vatBeanOther.tableType = "IM_MOVEMENT";
                            vat.bean().vatBeanOther.searchKey = vat.item.getValueByName("#F.headId");
                            vat.bean().vatBeanOther.subEntityBeanName = "imMovementItems";
                            vat.bean().vatBeanOther.itemFieldName = "itemCode";
                            vat.bean().vatBeanOther.warehouseCodeFieldName = "deliveryWarehouseCode";
                            vat.bean().vatBeanOther.declTypeFieldName = "";
                            vat.bean().vatBeanOther.declNoFieldName = "originalDeclarationNo";
                            vat.bean().vatBeanOther.declSeqFieldName = "originalDeclarationSeq";
                            vat.bean().vatBeanOther.declDateFieldName = "originalDeclarationDate";
                            vat.bean().vatBeanOther.lotFieldName = "lotNo";
                            vat.bean().vatBeanOther.qtyFieldName = vat.bean().vatBeanOther.isThreePointMoving == true ? "originalDeliveryQuantity" : "deliveryQuantity";
                            vat.block.submit(function () {
                                return "process_object_name=appExtendItemInfoService" + "&process_object_method_name=executeExtendItemWithBlock";
                            }, {
                                other: true,
                                funcSuccess: function () {
                                    vat.block.pageRefresh(vnB_Detail);
                                    //alert("完成展報單！");
                                    vat.item.setStyleByName("#B.declaration", "display", "inline");
                                    //$.unblockUI();
                                }
                            });
                        } else {
                            alert("尚未輸入轉出數量，無法核銷報單");
                        }
                    } else {
                        alert("尚未輸入轉出數量，無法核銷報單");
                    }
                }
            });
        }
    });

}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" +
		"?programId=IM_MOVEMENT" +
		"&levelType=ERROR" +
        "&processObjectName=imMovementMainService" +
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}



function getEmployeeInfo(vsEmployee) {
    if ("" != vsEmployee, vat.item.getValueByName("#F." + vsEmployee)) {
        vat.item.setValueByName("#F." + vsEmployee, vat.item.getValueByName("#F." + vsEmployee).toUpperCase());
        var processString = "process_sql_code=FindEmployeeChineseName&employeeCode=" + vat.item.getValueByName("#F." + vsEmployee);
        vat.ajax.startRequest(processString, function () {
            if (vat.ajax.handleState()) {
                if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
                    vat.item.setValueByName("#F." + vsEmployee + "Name", vat.ajax.getValue("CHINESE_NAME", vat.ajax.xmlHttp.responseText));
                } else {
                    vat.item.setValueByName("#F." + vsEmployee + "Name", "");
                    alert("查無此員工代號");
                }
            }
        });
    }
}

function createRefreshForm(){
	vat.item.setValueByName("#F.headId","");
	refreshForm("");
}

function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=imMovementMainAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

function changeItemCategory(){

	vat.block.pageDataSave( vnB_Detail ,{
		loadSuccessAfter:function(){
  		vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
  		vat.bean().vatBeanOther.itemCategory = vat.item.getValueByName("#F.itemCategory");
  		vat.bean().vatBeanOther.deliveryWarehouseCode = vat.item.getValueByName("#F.deliveryWarehouseCode");
  		vat.bean().vatBeanOther.arrivalWarehouseCode = vat.item.getValueByName("#F.arrivalWarehouseCode");
  		vat.bean().vatBeanOther.arrivalStoreCode = vat.item.getValueByName("#F.allFixSuppliers");
  		
		vat.block.submit(function(){return "process_object_name=imMovementMainService"+
		            "&process_object_method_name=changeItemCategory";},  {other:true,picker:false,
		     funcSuccess: function() {
			     vat.item.setValueByName("#F.deliveryContactPerson"        , vat.bean().vatBeanOther.deliveryWarehouseMangager);
				 vat.item.setValueByName("#F.deliveryContactPersonName"    , vat.bean().vatBeanOther.deliveryWarehouseMangagerName);
				 vat.item.setValueByName("#F.deliveryCity",vat.bean().vatBeanOther.deliveryCity);
				 vat.item.setValueByName("#F.deliveryArea",vat.bean().vatBeanOther.deliveryArea);
				 vat.item.setValueByName("#F.deliveryZipCode",vat.bean().vatBeanOther.deliveryZipCode);
				 vat.item.setValueByName("#F.deliveryAddress",vat.bean().vatBeanOther.deliveryAddress);
				 vat.item.setValueByName("#F.customsDeliveryWarehouseCode" , vat.bean().vatBeanOther.customsDeliveryWarehouseCode);

			     vat.item.setValueByName("#F.arrivalContactPerson"         , vat.bean().vatBeanOther.arrivalWarehouseMangager);
				 vat.item.setValueByName("#F.arrivalContactPersonName"     , vat.bean().vatBeanOther.arrivalWarehouseMangagerName);
				 vat.item.setValueByName("#F.arrivalCity",vat.bean().vatBeanOther.arrivalCity);
				 vat.item.setValueByName("#F.arrivalArea",vat.bean().vatBeanOther.arrivalArea);
				 vat.item.setValueByName("#F.arrivalZipCode",vat.bean().vatBeanOther.arrivalZipCode);
				 vat.item.setValueByName("#F.arrivalAddress",vat.bean().vatBeanOther.deliveryAddress);
				 vat.item.setValueByName("#F.customsArrivalWarehouseCode"  , vat.bean().vatBeanOther.customsArrivalWarehouseCode);
				 vat.item.setValueByName("#F.allowMinusStock"              , vat.bean().vatBeanOther.allowMinusStock);

				 vat.item.setValueByName("#F.customsArrivalStoreCode" , vat.bean().vatBeanOther.customsArrivalStoreCode);
				 changeRelationData();

		     }
		});
		changeDeclarationStyle();
	}});

	
}

function getFixAddr(){

	vat.block.pageDataSave( vnB_Detail ,{
		loadSuccessAfter:function(){
  		vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
  		vat.bean().vatBeanOther.itemCategory = vat.item.getValueByName("#F.itemCategory");
  		vat.bean().vatBeanOther.itemCategory = vat.item.getValueByName("#F.itemCategory");
  		vat.bean().vatBeanOther.fixSupplier = vat.item.getValueByName("#F.allFixSuppliers");
  		vat.bean().vatBeanOther.arrivalWarehouseCode = vat.item.getValueByName("#F.arrivalWarehouseCode");
  		vat.bean().vatBeanOther.arrivalStoreCode = vat.item.getValueByName("#F.arrivalStoreCode");
  		
		vat.block.submit(function(){return "process_object_name=imMovementMainService"+
		            "&process_object_method_name=getFixAddr";},  {other:true,picker:false,
		     funcSuccess: function() {
			     

			    alert(vat.bean().vatBeanOther.customsArrivalWarehouseCode);
				 vat.item.setValueByName("#F.arrivalCity",vat.bean().vatBeanOther.arrivalCity);
				 vat.item.setValueByName("#F.arrivalArea",vat.bean().vatBeanOther.arrivalArea);
				 vat.item.setValueByName("#F.arrivalZipCode",vat.bean().vatBeanOther.arrivalZipCode);
				 vat.item.setValueByName("#F.arrivalAddress",vat.bean().vatBeanOther.arrivalAddress);
				// vat.item.setValueByName("#F.customsArrivalWarehouseCode"  , vat.bean().vatBeanOther.customsArrivalWarehouseCode);
				 

				
				 changeRelationData();

		     }
		});
		changeDeclarationStyle();
	}});

	
}

function changeDeclarationStyle(){
	var vsType="";
     
     
    if(vat.item.getValueByName("#F.orderTypeCode")!="WMF") {
        if("P" == vat.item.getValueByName("#F.taxType") ||vat.item.getValueByName("#F.customsDeliveryWarehouseCode") == vat.item.getValueByName("#F.customsArrivalWarehouseCode")){
		    vsType="none";
	     }
    }else{
		vsType="inline";
	}
	//vat.item.setGridStyleByName("boxNo"                  , "display", vsType);

	vat.item.setStyleByName("#B.declaration" , "display", vsType);
	vat.item.setGridStyleByName("originalDeclarationNo"  , "display", vsType);
	vat.item.setGridStyleByName("originalDeclarationSeq" , "display", vsType);
	vat.item.setGridStyleByName("originalDeclarationDate", "display", vsType);
	vat.item.setGridStyleByName("searchDeclaration", "display", vsType);
}

function checkHeadLineWarehouse(){
  var result  = true;
  var vLineId = 1;
  var vDeliveryWarehouseCode = vat.item.getGridValueByName("deliveryWarehouseCode", vLineId);
  var vArrivalWarehouseCode = vat.item.getGridValueByName("arrivalWarehouseCode"  , vLineId);
  if(vDeliveryWarehouseCode !=vat.item.getValueByName("#F.deliveryWarehouseCode")) {
  	alert("單頭的轉出庫別("+vat.item.getValueByName("#F.deliveryWarehouseCode")+")與單身的轉出庫別("+vDeliveryWarehouseCode+")不同");
  	result = false;
  }
  if(vArrivalWarehouseCode !=vat.item.getValueByName("#F.arrivalWarehouseCode")) {
  	alert("單頭的轉出庫別("+vat.item.getValueByName("#F.arrivalWarehouseCode")+")與單身的轉出庫別("+vArrivalWarehouseCode+")不同");
  	result = false;
  }
  return result;
}

function countTotalQuantity(){

    var vsHeadId = vat.item.getValueByName("#F.headId") ;
    var vsStatus = vat.item.getValueByName("#F.status") ;
    var vsItemCountField="DELIVERY_QUANTITY";
	var processString = "process_sql_code=FindMovementBoxCount&headId="+vsHeadId;
	var vsItemCount = 0;
	vat.ajax.startRequest(processString,  function() {
	  if (vat.ajax.handleState()){
	    if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
	  	    vat.item.setValueByName("#F.boxCount", vat.ajax.getValue("BOX_COUNT", vat.ajax.xmlHttp.responseText));
	    }else{
	      vat.item.setValueByName("#F.boxCount", 0);
	    }
	    processString = "process_sql_code=FindMovementItemCount&headId="+vsHeadId;
		vat.ajax.startRequest(processString,  function() {
		  if (vat.ajax.handleState()){
		    if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){

				if("SAVE" == vsStatus){
					if(vat.bean().vatBeanOther.isThreePointMoving == true)
						vsItemCountField ="ORIGINAL_DELIVERY_QUANTITY";
					else
						vsItemCountField ="DELIVERY_QUANTITY";
	  		    }else if ("WAIT_OUT" == vsStatus){
	  		    	vsItemCountField ="DELIVERY_QUANTITY";
	  		    }else if ("WAIT_IN" == vsStatus){
	  		    	vsItemCountField ="ARRIVAL_QUANTITY";
	  		    }
	  		   // alert("countTotalQuantity..."+vsItemCountField);
	  		    vsItemCount = vat.ajax.getValue(vsItemCountField, vat.ajax.xmlHttp.responseText);
	  		    vat.item.setValueByName("#F.itemCount", "null" == vsItemCount?0:vsItemCount);

			    if("WAIT_IN" == vsStatus || "ORIGINAL_DELIVERY_QUANTITY" == vsItemCountField){
			    	vsItemCount = vat.ajax.getValue("DELIVERY_QUANTITY", vat.ajax.xmlHttp.responseText);
			    	vat.item.setValueByName("#F.itemCount1", "null" == vsItemCount?0:vsItemCount);
	  		    }else{
	  		    	vsItemCount = vat.ajax.getValue("ORIGINAL_DELIVERY_QUANTITY", vat.ajax.xmlHttp.responseText);
			    	vat.item.setValueByName("#F.itemCount1", "null" == vsItemCount?0:vsItemCount);
	  		    }

		    }else{
		      vat.item.setValueByName("#F.itemCount" , 0);
		      vat.item.setValueByName("#F.itemCount1", 0);
		    }
		  }
		} );
		}
	} );

}


function refreshHeadData(){
	var vsItemCategoryMode = vat.bean().vatBeanOther.itemCategoryMode;
    var vsHeadId = vat.item.getValueByName("#F.headId") ;
    var vsBrandCode = vat.item.getValueByName("#F.brandCode") ;
    var vsOriginalItemCategory =vat.item.getValueByName("#F.itemCategory") ;
    var vsNewItemCategory ="";
	var processString = "process_sql_code=FindTheFirstItemCategory&headId="+vsHeadId+"&brandCode="+vsBrandCode;
	vat.ajax.startRequest(processString,  function() {
		if (vat.ajax.handleState()){
		    if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
		    	vsNewItemCategory = vat.ajax.getValue("ITEM_CATEGORY", vat.ajax.xmlHttp.responseText);
		    	if(vsNewItemCategory != vsOriginalItemCategory &&  "Y" == vsItemCategoryMode){
		  	    		vat.item.setValueByName("#F.itemCategory", vsNewItemCategory);
		  	    }
		  	    vat.item.setValueByName("#F.deliveryWarehouseCode", vat.ajax.getValue("DELIVERY_WAREHOUSE_CODE", vat.ajax.xmlHttp.responseText));
		  	   // vat.item.setValueByName("#F.deliveryWarehouseCodeText", vat.ajax.getValue("DELIVERY_WAREHOUSE_CODE", vat.ajax.xmlHttp.responseText));
		  	    vat.item.setValueByName("#F.arrivalWarehouseCode", vat.ajax.getValue("ARRIVAL_WAREHOUSE_CODE", vat.ajax.xmlHttp.responseText));
		  	  //  vat.item.setValueByName("#F.arrivalWarehouseCodeText", vat.ajax.getValue("ARRIVAL_WAREHOUSE_CODE", vat.ajax.xmlHttp.responseText));
		  	    changeItemCategory();
		    }

		}
	} );
}

function doLineAfterPickerProcess(id){
	//do picker back something
	var i = 0;

	if(vat.bean().vatBeanPicker.result !== null){

		var vLineId	= vat.item.getGridLine(id);
        var vIndexNo= parseInt(vat.item.getGridValueByName("indexNo"  , vLineId));
  		vat.bean().vatBeanOther.lineId = vIndexNo;
  		vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
  		vat.bean().vatBeanOther.deliveryWarehouseCode = vat.item.getValueByName("#F.deliveryWarehouseCode");
  		vat.bean().vatBeanOther.arrivalWarehouseCode = vat.item.getValueByName("#F.arrivalWarehouseCode");
  		//alert(vat.bean().vatBeanOther.lineId );
		vat.block.submit(function(){return "process_object_name=imMovementMainService"+
		            "&process_object_method_name=updatePickerData";},
		     {other:true,picker:true, funcSuccess: function() {vat.block.pageRefresh(vnB_Detail);}});

	}
}

function doPassLineData(x){
  vat.block.pageDataSave( vnB_Detail, {refresh:false});
  var suffix = "";
  var vLineId	      = vat.item.getGridLine(x);
  var vItemCode       = vat.item.getGridValueByName("itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  var vLotNo          = vat.item.getGridValueByName("lotNo"   , vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  var vTaxType 		  = vat.item.getValueByName("#F.taxType");
  var vWarehouseCode  = vat.item.getValueByName("#F.deliveryWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vItemCategory   = vat.item.getValueByName("#F.itemCategory").replace(/^\s+|\s+$/, '').toUpperCase();

  //alert("LineId:"+vLineId);
  suffix += "&taxType="+escape(vTaxType)+
            "&startItemCode="+escape(vItemCode)+
            "&endItemCode="+escape(vItemCode)+
            "&startWarehouseCode="+escape(vWarehouseCode)+
            "&endWarehouseCode="+escape(vWarehouseCode)+
            "&startLotNo="+escape(vLotNo)+
            "&endLotNo="+escape(vLotNo)+
            "&showZero=N"+
            "&itemCategory="+escape(vItemCategory);
 // alert(suffix);
  return suffix;
}

function doPassHeadData(){

  var suffix = "";
  var vOrderTypeCode         = vat.item.getValueByName("#F.OrderTypeCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vDeliveryWarehouseCode = vat.item.getValueByName("#F.deliveryWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vArrivalWarehouseCode  = vat.item.getValueByName("#F.arrivalWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vItemCategory   = vat.item.getValueByName("#F.itemCategory").replace(/^\s+|\s+$/, '').toUpperCase();
  suffix += "&orderTypeCode="+escape(vOrderTypeCode)+
        //   "&deliveryWarehouseCode="+escape(vDeliveryWarehouseCode)+
        //    "&arrivalWarehouseCode="+escape(vArrivalWarehouseCode)+
            "&itemCategory="+escape(vItemCategory);

  return suffix;
}

function modifyDeliveryDate(){
    var headId = vat.item.getValueByName("#F.headId");
    var nItemLine = vat.item.getGridLine();
    var vOrderTypeCode = vat.item.getGridValueByName("orderTypeCode", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
    var vOrderNo = vat.item.getGridValueByName("orderNo", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vDeliveryDate = vat.item.getGridValueByName("deliveryDate", nItemLine).replace(/^\s+|\s+$/, '');

	var obj = document.getElementById("vatBeginDiv");
	if (obj){
		obj.filters[0].enabled = true;
		obj.filters[0].opacity = 0.60;
	}

	var returnData = window.showModalDialog(
		"Im_Movement:modifyDeliveryDate:20100116.page"+
		"?headId=" + headId+
		"&brandCode ="+ vBrandCode+
		"&orderTypeCode ="+ vOrderTypeCode+
		"&vOrderNo ="+ vOrderNo+
		"&vDeliveryDate ="+ vDeliveryDate
		,"",
		"dialogHeight:600px;dialogWidth:1060px;dialogTop:100px;dialogLeft:100px;status:no;");

}

// 自訂事件
function assignOriginalQtyToArrival() {
  // alert("eeeeeee");
    if (confirm("是否確定執行『自訂事件』？")) {
        if (vat.item.getValueByName("#F.status") == "SAVE" || vat.item.getValueByName("#F.status") == "WAIT_OUT") {
            vat.block.submit(function () {
                return "process_object_name=imMovementMainService" + "&process_object_method_name=updateOriginalQtyToArrival";
            }, {
                other: true,
                picker: false,
                funcSuccess: function () {
                    vat.block.pageRefresh(vnB_Detail);
                }
            });
        } else {
            alert("狀態必須在待轉出，方可使用本功能");
        }
    }
}

function doPassDeclData(x){
  vat.block.pageDataSave( vnB_Detail);
  var suffix = "";
  var vLineId	      = vat.item.getGridLine(x);
  var vItemCode       = vat.item.getGridValueByName("itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  var vWarehouseCode  = vat.item.getValueByName("#F.deliveryWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vTaxType 		  = vat.item.getValueByName("#F.taxType");
  var vDeclarationNo  = vat.item.getGridValueByName("#F.originalDeclarationNo", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
  var vDeclarationSeq = vat.item.getGridValueByName("#F.originalDeclarationSeq", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();

  //alert("LineId:"+vLineId);
  suffix += "&taxType="+escape(vTaxType)+
            "&customsItemCode="+escape(vItemCode)+
            "&warehouseCode="+escape(vWarehouseCode)+
            "&declarationNo="+escape(vDeclarationNo)+
            "&declarationSeqStart="+escape(vDeclarationSeq)+
            "&declarationSeqEnd="+escape(vDeclarationSeq)+
            "&adjustmentType=61";
 // alert(suffix);
  return suffix;
}

function doDeclAfterPickerProcess(id){
	//do picker back something
	if(vat.bean().vatBeanPicker.cmOnHandResult != null ){
		var vLineId	= vat.item.getGridLine(id);
    	vat.item.setGridValueByName("originalDeclarationNo", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationNo"]);
    	vat.item.setGridValueByName("originalDeclarationSeq", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationSeq"]);
    	vat.item.setGridValueByName("originalDeclarationDate", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["importDate"]);
	}
}

function findFirstRecordItemCategory(){

  var vHeadId  = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '').toUpperCase();
  var vBrandCode  = vat.item.getValueByName("#F.brandCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vProcessString =	"process_sql_code=findMoveFirstRecord&headId=" + vHeadId+ "&brandCode=" + vBrandCode;
  vat.ajax.startRequest(vProcessString,  function() {
     if (vat.ajax.handleState()){
         if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
            vat.item.setValueByName("#F.itemCategory",vat.ajax.getValue("ITEM_CATEGORY", vat.ajax.xmlHttp.responseText))

         }
     }
  } )
}

function changeDeliveryDate(){
	 if(vat.item.getValueByName("#F.brandCode").indexOf("T2") > -1 ){
	 	vat.item.setValueByName("#F.arrivalDate", vat.item.getValueByName("#F.deliveryDate"));
	 }
}

function afterImportSuccess(){
//	alert("FindTheFirstItemCategory");
	refreshHeadData();
}

// format日期格式
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}