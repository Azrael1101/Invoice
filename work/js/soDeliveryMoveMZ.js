/***
 *	檔案: soDelivery.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Search = 5;
var vnB_Header = 1;
var vnB_Detail = 3;

function kweBlock(){
   if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     orderTypeCode      : vat.item.getValueByName("#F.orderTypeCode") == "" ? "DVM":vat.item.getValueByName("#F.orderTypeCode"),
	     processId          : document.forms[0]["#processId"         ].value,
	     assignmentId       : document.forms[0]["#assignmentId"      ].value,
	     formId             : document.forms[0]["#formId"            ].value,
	     updateForm         : document.forms[0]["#formId"].value==""?"N":"Y",
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	 };
  }

  kweButtonLine();
  kweHeader();
  kweBarCodeLine()
  if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","明細檔資料" ,"vatDetailDiv"    ,"images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false);  
		//vat.tabm.createButton(0 ,"xTab2","簽核資料"   ,"vatApprovalDiv"  ,"images/tab_approval_data_dark.gif","images/tab_approval_data_light.gif",vat.item.getValueByName("#F.status") == "SAVE"? "none" : "inline");
  }
  
  kweDetail();
}


function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 3px; border-left-width: 0px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[
	 			{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},	
	 			{name:"#B.lock"        , type:"IMG"    ,value:"鎖定"   ,   src:"./images/button_lock.gif"  , eClick:"changeUpdateFlag()"},
	 			{name:"#B.unlock"        , type:"IMG"    ,value:"解鎖"   ,   src:"./images/button_unlock.gif"  ,eClick:"changeUpdateFlag()"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  ,
	 									 openMode:"open",
	 									 service:"So_Delivery:searchMove:20160613.page",
	 									 servicePassData:function(x){ return doPassHeadData(x); },
	 									 left:0, right:0, width:1024, height:768,
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},
	            {name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  ,
                                         openMode:"open", 
                                         service:"/erp/fileUpload:standard:2.page",
                                         servicePassData:function(x){ return importFormData(); },
                                         left:0, right:0, width:600, height:400,    
                                         serviceAfterPick:function(){afterImportSuccess()}},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/v2/icon_b02.gif"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/v2/icon_b03.gif"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/v2/icon_b06.gif"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/v2/icon_b07.gif" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 2, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:"/"},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 2, mode:"READONLY" },
				{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#F.reportList"  , type:"SELECT" },
	 			{name:"#B.print"       , type:"BUTTON"    ,value:"列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("MANUAL")'}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
	 			  ]}
	  ],
		beginService:function(){return ""}	,
		closeService:function(){return ""}
	});

}



function kweHeader(){
var vsRowStyle= "T2" == vat.bean("loginBrandCode")?"":" style= 'display:none;'";
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"入境提貨庫存移轉維護作業", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode", type:"LABEL" , value:"單別"}]},
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", size:1, mode:"READONLY"}]},
	 {items:[{name:"#L.orderNo"      , type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.orderNo"      , type:"TEXT"  , bind:"orderNo",back:false,size:14, mode:"READONLY"},
	         {name:"#F.updateFlag"   , type:"TEXT" ,  size:4, bind:"updateFlag",mode:"HIDDEN"},
	         {name:"#F.brandCode"    , type:"TEXT" ,  bind:"brandCode",mode:"HIDDEN"},
	 		 {name:"#F.headId"      , type:"NUMB"  ,  size:4, bind:"headId" , back:false, mode:"READONLY"}]},
	 {items:[{name:"#L.orderDate"   , type:"LABEL" , value:"申請日期"}]},
	 {items:[{name:"#F.orderDate"   , type:"DATE"  ,  bind:"orderDate"}]},
	 {items:[{name:"#L.status"      , type:"LABEL" , value:"狀態"}]},
	 {items:[{name:"#F.status"      , type:"TEXT"  ,  bind:"status",  mode:"HIDDEN"},
	  		 {name:"#F.statusName"  , type:"TEXT"  ,  bind:"statusName", size:4, back:false, mode:"READONLY"},
	  		 {name:"#F.processId"   , type:"TEXT"  ,  bind:"processId" , size:4, back:false, mode:"READONLY"}]}]},
	 {row_style:vsRowStyle, cols:[
	 {items:[{name:"#L.deliveryStoreArea"   , type:"LABEL" , value:"轉出庫別"}]},
	 {items:[{name:"#F.deliveryStoreArea"   , type:"SELECT"  ,  bind:"deliveryStoreArea", eChange:"changeStoreArea()"}]},
	 {items:[{name:"#L.arrivalStoreArea"   , type:"LABEL" , value:"轉入庫別"}]},
	 {items:[{name:"#F.arrivalStoreArea"   , type:"SELECT"  ,  bind:"arrivalStoreArea", eChange:"changeStoreArea()"}]},
	 {items:[{name:"#L.moveEmployee"    , type:"LABEL", value:"轉貨人員"}]},
	 {items:[{name:"#F.moveEmployee"    , type:"TEXT",   bind:"moveEmployee", size:4, eChange:"getEmployeeInfo()"},
	 	     {name:"#F.moveEmployeeName", type:"TEXT",   bind:"moveEmployeeName", size:6, mode:"READONLY"}]},
	 {items:[{name:"#L.createdBy"      , type:"LABEL", value:"建檔人員"}]},
 	 {items:[{name:"#F.createdByName"      , type:"TEXT",   bind:"createdByName", mode:"READONLY", size:12}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.bagCounts", type:"LABEL", value:"袋數"}]},
	 {items:[{name:"#L.bagCounts1", type:"LABEL", value:"大:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts1", type:"NUMB" , bind:"bagCounts1", mode:"READONLY",size:4, eChange:'totalBagCount()'},
			 {name:"#L.bagCounts2", type:"LABEL", value:"&nbsp&nbsp&nbsp;中:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts2", type:"NUMB",  bind:"bagCounts2", mode:"READONLY", size:4, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts3", type:"LABEL", value:"&nbsp&nbsp&nbsp;小:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts3", type:"NUMB",  bind:"bagCounts3", mode:"READONLY", size:4, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts7", type:"LABEL", value:"&nbsp&nbsp&nbsp;小小袋:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts7", type:"NUMB",  bind:"bagCounts7", mode:"READONLY", size:4, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts4", type:"LABEL", value:"&nbsp&nbsp&nbsp;拉桿箱:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts4", type:"NUMB",  bind:"bagCounts4", mode:"READONLY", size:4, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts5", type:"LABEL", value:"&nbsp&nbsp&nbsp;折收袋:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts5", type:"NUMB",  bind:"bagCounts5", mode:"READONLY", size:4, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts6", type:"LABEL", value:"&nbsp&nbsp&nbsp;其他:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts6", type:"NUMB",  bind:"bagCounts6", mode:"READONLY", size:4, eChange:'totalBagCount()'}],td:" colSpan=5"},
	 {items:[{name:"#L.lastDt"    , type:"LABEL", value:"填單日期"}]},
 	 {items:[{name:"#F.lastDt"    , type:"TEXT",   bind:"lastUpdateDate", mode:"READONLY", size:12}]}]}
	  ],
		beginService:"",
		closeService:""
	});

}

function kweBarCodeLine(){
var allActions = [["","",false],
                 ["新增","移除"],
                 ["CREATE","REMOVE"]];
vat.block.create(vnB_Search, {
	id: "vatBlock_Search", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='5' style='border-right-width: 0px; border-bottom-width: 5px; border-left-width: 0px; border-color: #ffffff;'",
	title:"", 
	rows:[
	 {row_style:"background-color: #990000", cols:[
	 {items:[{name:"#L.actionType"     , type:"LABEL", value:"<FONT SIZE=4><B>執行項目</B></FONT>"}], 
	          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
	 {items:[{name:"#F.actionType"     , type:"SELECT" , style:"font-size: 30px; size:2", init:allActions}],
	          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"}, 
	 {items:[{name:"#L.searchNo"     , type:"LABEL", value:"&nbsp&nbsp&nbsp;<FONT SIZE=4><BOLD>單號</BOLD></FONT>&nbsp&nbsp&nbsp;"}], 
	          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
	 {items:[{name:"#F.searchNo"     , type:"TEXT" ,mask:"CCCCCCCCCCCCC", bind:"searchNo", size:13, maxLen:13,  style:"font-size: 20pt; height:40px",  eChange:'searchDelivery("searchNo")'}], 
	          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
	 {items:[{name:"#B.findDelivery" , type:"BUTTON"  ,value:"執行",  size:14, eClick:'searchDelivery("searchButton")'}
	         ],  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"}]} 		  
	  ],
		beginService:"",
		closeService:""
	});
}
function kweDetail(){
  
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    var vnB_Detail = 3;
    vat.item.make(vnB_Detail, "indexNo"           , {type:"IDX"  ,                     desc:"序號"       });
    vat.item.make(vnB_Detail, "deliveryOrderType" , {type:"TEXT" , size: 4, maxLen:4,  desc:"入提單別" , mode:"HIDDEN" });
	vat.item.make(vnB_Detail, "deliveryOrderNo"   , {type:"TEXT" , size:14, maxLen:20, mode:"READONLY", desc:"入提單號" ,eChange:"getDeliveryInfo()"  });
	vat.item.make(vnB_Detail, "lineBagCounts1"    , {type:"NUMB" , size: 4, maxLen:4, mode:"READONLY", desc:"大"   });
	vat.item.make(vnB_Detail, "lineBagCounts2"    , {type:"NUMB" , size: 4, maxLen:4, mode:"READONLY", desc:"中"});
	vat.item.make(vnB_Detail, "lineBagCounts3"    , {type:"NUMB" , size: 4, maxLen:4, mode:"READONLY", desc:"小"});
	vat.item.make(vnB_Detail, "lineBagCounts7"    , {type:"NUMB" , size: 4, maxLen:4, mode:"READONLY", desc:"小小袋"});
	vat.item.make(vnB_Detail, "lineBagCounts4"    , {type:"NUMB" , size: 4, maxLen:4, mode:"READONLY", desc:"拉桿箱"});
	vat.item.make(vnB_Detail, "lineBagCounts5"    , {type:"NUMB" , size: 4, maxLen:4, mode:"READONLY", desc:"折收袋"}); 
	vat.item.make(vnB_Detail, "lineBagCounts6"    , {type:"NUMB" , size: 4, maxLen:4, mode:"READONLY", desc:"其他"});
	vat.item.make(vnB_Detail, "deliveryStoreArea"    , {type:"TEXT" , size: 4, maxLen:4, mode:"READONLY", desc:"轉出庫別"});
	vat.item.make(vnB_Detail, "deliveryStoreCode"    , {type:"TEXT" , size: 4, maxLen:6, mode:"READONLY", desc:"轉出儲位"});
	vat.item.make(vnB_Detail, "arrivalStoreArea"    , {type:"TEXT" , size: 4, maxLen:4, mode:"READONLY", desc:"轉入庫別"});
	vat.item.make(vnB_Detail, "arrivalStoreCode"    , {type:"TEXT" , size: 4, maxLen:6,  desc:"轉入儲位", style:"text-transform:uppercase;"});
	vat.item.make(vnB_Detail, "lineId"            , {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord"              , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
    vat.item.make(vnB_Detail, "isDeleteRecord"            , {type:"DEL"  , desc:"刪除", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "message"                   , {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                        closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "pageLoadSuccess()",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "3"
														});
}



function kweInitial(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

     vat.bean.init(function(){
		return "process_object_name=soDeliveryMoveService&process_object_method_name=executeInitial";
     },{other: true});

     vat.item.SelectBind(vat.bean("allOrderTypes")         ,{ itemName : "#F.orderTypeCode" });
     vat.item.SelectBind(vat.bean("allDeliveryStoreAreas") ,{ itemName : "#F.deliveryStoreArea" });
     vat.item.SelectBind(vat.bean("allArrivalStoreAreas")  ,{ itemName : "#F.arrivalStoreArea" });
     vat.item.SelectBind(vat.bean("allReportList")         ,{ itemName : "#F.reportList" });
     vat.item.bindAll();
     if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);	  	
	 }
	 if(typeof vat.bean().vatBeanOther.executeEmployee != 'undefined'){
	 	vat.item.setValueByName("#F.employeeCode",vat.bean().vatBeanOther.executeEmployee);
	 	vat.item.setValueByName("#F.employeeName",vat.bean().vatBeanOther.executeEmployeeName);
	 }
     vat.item.setValueByName("#F.updateFlag","N");
     vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     vat.tabm.displayToggle(0, "xTab2", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false)
     changeStoreArea();
	 doFormAccessControl();

  }
}

function loadBeforeAjxService(){
	var processString = "process_object_name=soDeliveryMoveService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")
	//alert("After loadBeforeAjxService");
	return processString;
}


function doFormAccessControl(){
    var vsOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
  	var vsFormStatus    = vat.item.getValueByName("#F.status");
  	var vsOrderNoPrefix = vat.item.getValueByName("#F.orderNo").substring(0, 3);
  	var vsProcessId     = vat.bean().vatBeanOther.processId;
	vat.item.setStyleByName("#B.new"         , "display", "none");
	vat.item.setStyleByName("#B.search"      , "display", "none");
	vat.item.setStyleByName("#B.exit"        , "display", "none");
	vat.item.setStyleByName("#B.submit"      , "display", "none");
	vat.item.setStyleByName("#B.save"        , "display", "none");
	vat.item.setStyleByName("#B.void"        , "display", "none");
	vat.item.setStyleByName("#B.lock"        , "display", "none");
	vat.item.setStyleByName("#B.unlock"      , "display", "none");
	vat.item.setStyleByName("#F.reportList"  , "display", "inline");
	vat.item.setStyleByName("#B.print"       , "display", "inline");
	vat.item.setStyleByName("#B.message"     , "display", "inline");
	vat.item.setStyleByName("#B.export"      , "display", "none");
	vat.item.setStyleByName("#B.import"      , "display", "none");
	vat.item.setStyleByName("#B.first"       , "display", "inline");
	vat.item.setStyleByName("#B.forward"     , "display", "inline");
	vat.item.setStyleByName("#B.next"        , "display", "inline");
	vat.item.setStyleByName("#B.last"        , "display", "inline");
	vat.item.setAttributeByName("#F.deliveryStoreArea", "readOnly", false);
	vat.item.setAttributeByName("#F.arrivalStoreArea" , "readOnly", false);
	vat.item.setAttributeByName("#F.orderDate", "readOnly", false);
	vat.item.setGridAttributeByName("arrivalStoreCode"    , "readOnly", false);
	
	if( vsFormStatus == "CLOSE" ||  vsFormStatus == "VOID" || ((vsProcessId == 0 || vsProcessId == null) & vsFormStatus == "SAVE" & vsOrderNoPrefix != "TMP" & vat.item.getValueByName("#F.processId") != "")){
		vat.item.setStyleByName("#B.new"         , "display", "inline");
		vat.item.setStyleByName("#B.search"      , "display", "inline");
		vat.item.setStyleByName("#B.exit"        , "display", "inline");
		vat.item.setAttributeByName("#F.deliveryStoreArea", "readOnly", true);
		vat.item.setAttributeByName("#F.arrivalStoreArea" , "readOnly", true);
		vat.item.setAttributeByName("#F.moveEmployee"     , "readOnly", true);		
		vat.item.setAttributeByName("#F.searchNo", "readOnly", true);
		vat.item.setAttributeByName("#F.actionType", "readOnly", true);
		vat.item.setAttributeByName("#F.orderDate", "readOnly", true);
		vat.item.setGridAttributeByName("deliveryOrderNo"    , "readOnly", true);
		vat.item.setGridAttributeByName("arrivalStoreCode"    , "readOnly", true);
		
		if (vsFormStatus == "VOID"){
			vat.item.setStyleByName("#B.print"         , "display", "none");
		}
	}
	else
	{
		if(  vsFormStatus == "SAVE"   ){
			vat.item.setStyleByName("#B.new"         , "display", "inline");
			vat.item.setStyleByName("#B.search"      , "display", "inline");
			vat.item.setStyleByName("#B.exit"        , "display", "inline");
			vat.item.setStyleByName("#B.submit"      , "display", "inline");
			vat.item.setStyleByName("#B.save"        , "display", "inline");
			//vat.item.setStyleByName("#B.import"      , "display", "inline");
			if (vsOrderNoPrefix != "TMP")
				vat.item.setStyleByName("#B.void"        , "display", "inline");
				
			vat.item.setAttributeByName("#F.deliveryStoreArea"   , "readOnly", false);
			vat.item.setAttributeByName("#F.arrivalStoreArea"    , "readOnly", false);		
			vat.item.setAttributeByName("#F.orderDate"           , "readOnly", false);
			vat.item.setAttributeByName("#F.moveEmployee"        , "readOnly", false);	
			if (vat.item.getValueByName("#F.updateFlag")=="Y"){
				vat.item.setAttributeByName("#F.deliveryStoreArea", "readOnly", true);
				vat.item.setAttributeByName("#F.arrivalStoreArea" , "readOnly", true);
				vat.item.setAttributeByName("#F.moveEmployee"     , "readOnly", true);		
				vat.item.setAttributeByName("#F.searchNo", "readOnly", false);
				vat.item.setAttributeByName("#F.actionType", "readOnly", false);
				vat.item.setStyleByName("#B.unlock"        , "display", "inline");
				vat.item.setAttributeByName("#F.reportList"  , "readOnly", "true");
			}else{
				vat.item.setAttributeByName("#F.searchNo"  , "readOnly", true);
				vat.item.setAttributeByName("#F.actionType", "readOnly", true);
				vat.item.setStyleByName("#B.lock"          , "display", "inline");
				vat.item.setAttributeByName("#F.reportList"  , "readOnly", "false");
			}
		}
	}
}

function pageLoadSuccess(){
	//alert("kwePageLoadSuccess");
	//countTotalQuantity();
	//getTheFirstItemCategory();
}

function pageLoadLogSuccess(){
	//alert("kwePageLoadSuccess");
	//countTotalQuantity();
	//getTheFirstItemCategory();

}

function changeUpdateFlag(){
	if("Y" == vat.item.getValueByName("#F.updateFlag")) {
		vat.item.setValueByName("#F.updateFlag","N");
	}else{
	    if(""!=vat.item.getValueByName("#F.employeeCode") && ""!=vat.item.getValueByName("#F.employeeName"))
			vat.item.setValueByName("#F.updateFlag","Y");
		else
			alert("請輸入修改人員工號");
	}
	doFormAccessControl();
}



function totalBagCount(){
	var totalCounts = parseInt(vat.item.getValueByName("#F.bagCounts1"),10) + 
	                  parseInt(vat.item.getValueByName("#F.bagCounts2"),10) +
	                  parseInt(vat.item.getValueByName("#F.bagCounts3"),10) +
	                  parseInt(vat.item.getValueByName("#F.bagCounts4"),10) +
	                  parseInt(vat.item.getValueByName("#F.bagCounts5"),10) +
	                  parseInt(vat.item.getValueByName("#F.bagCounts6"),10) +
	                  parseInt(vat.item.getValueByName("#F.bagCounts7"),10) ;
	vat.item.setValueByName("#F.totalBagCounts",totalCounts);
}

function doPassHeadData(x){
  var suffix = "";
  var vbrandCode     = vat.item.getValueByName("#F.vbrandCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vOrderTypeCode = vat.item.getValueByName("#F.OrderTypeCode").replace(/^\s+|\s+$/, '').toUpperCase();
  suffix += "&brandCode="+escape(vbrandCode)+
  			"&orderTypeCode="+escape(vOrderTypeCode);

  return suffix;
}



function getEmployeeInfo() {
    
    if ("" !=vat.item.getValueByName("#F.moveEmployee")) {
        vat.item.setValueByName("#F.moveEmployee",vat.item.getValueByName("#F.moveEmployee").toUpperCase());

        vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.moveEmployee");
        vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=getEmployeeInfo";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("" != vat.bean().vatBeanOther.employeeName ){
                    vat.item.setValueByName("#F.moveEmployeeName", vat.bean().vatBeanOther.employeeName);
                    vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.moveEmployee");
                    vat.bean().vatBeanOther.executeEmployeeName=vat.item.getValueByName("#F.moveEmployeeName");
			     }else{
			     　　vat.item.setValueByName("#F.moveEmployeeName", "");
			     	alert("員工代號錯誤，請重新輸入！");
	 				vat.form.item.setFocus( "#F.moveEmployee" );			     	
			     }
		     }
		});
    }else{
    	vat.item.setValueByName("#F.moveEmployeeName","");
    }
}

function changeFlightDate(){
	vat.item.setValueByName("#F.scheduleDeliveryDate",vat.item.getValueByName("#F.flightDate"));
}

function doSubmit(formAction){
	var vsAllowSubmit         = true;
	var alertMessage          ="是否確定送出?";
	var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');;
    var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');;
    var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');;
    var employeeCode          = vat.bean().vatBeanOther.loginEmployeeCode.replace(/^\s+|\s+$/, '');;
    var orderNoPrefix         = vat.item.getValueByName("#F.orderNo").substring(0,3);
    var approvalResult        = getApprovalResult();
    var processString 	      ="";
    var totalBagCounts        =0;
    totalBagCount();
    
	if(confirm(alertMessage)){
	    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
		var vsStatus = vat.item.getValueByName("#F.status");
		var vsLockFlag = vat.item.getValueByName("#F.lockFlag");
		var vsAllowSubmit = true;
		if("SUBMIT" == formAction){
			//if(vat.item.getValueByName("#F.deliveryStoreArea")== vat.item.getValueByName("#F.arrivalStoreArea")){
			//	alert("轉出庫別不可與轉入庫別相同");
			//	vsAllowSubmit = false;
			//}
			if(vat.item.getValueByName("#F.moveEmployee")== ""){
				alert("員工代號空白，請重新輸入");
				vsAllowSubmit = false;
			}
			totalBagCounts= 
			parseInt(vat.item.getValueByName("#F.bagCounts1"),10 )+
			parseInt(vat.item.getValueByName("#F.bagCounts2"),10 )+
			parseInt(vat.item.getValueByName("#F.bagCounts3"),10 )+
			parseInt(vat.item.getValueByName("#F.bagCounts4"),10 )+
			parseInt(vat.item.getValueByName("#F.bagCounts5"),10 )+
			parseInt(vat.item.getValueByName("#F.bagCounts6"),10 )+
			parseInt(vat.item.getValueByName("#F.bagCounts7"),10 );
			
			if( 0 == totalBagCounts){
				alert("總提貨袋數不可為「0」");
				vsAllowSubmit = false;
			}
		}
    	if(vsAllowSubmit){
    	      vat.block.pageDataSave(vnB_Detail,
			    {  funcSuccess:function(){
			    	  vat.bean().vatBeanOther.processId       = processId;
			          vat.bean().vatBeanOther.formId          = formId;
					  vat.bean().vatBeanOther.status          = status;
					  vat.bean().vatBeanOther.formAction      = formAction;
		  	  	      vat.bean().vatBeanOther.executeEmployee =  vat.item.getValueByName("#F.moveEmployee");
		  	  	      vat.bean().vatBeanOther.approvalResult  = approvalResult;
	  				  vat.bean().vatBeanOther.approvalComment = "";
	  				  //alert("performTransaction");
			          processString = "process_object_name=soDeliveryMoveAction&process_object_method_name=performTransaction";
					  vat.block.submit(function(){return processString;},{
				                    bind:true, link:true, other:true,
				                    funcSuccess:function(){
	   	 								refreshForm(formId);
						        	}}
					  );
			  	}});	  
		
        }
	}
}

function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	processString = "process_object_name=soDeliveryMoveService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status")+ "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode;;


	return processString;
}

function saveSuccessAfter() {

}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" +
		"?programId=SO_DELIVERY" +
		"&levelType=ERROR" +
        "&processObjectName=soDeliveryMoveService" +
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function initSoDetail(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

     vat.bean.init(function(){
		return "process_object_name=soDeliveryService&process_object_method_name=executeInitialSoDetail";
     },{other: true});

     vat.item.SelectBind(vat.bean("allOrderTypes")         ,{ itemName : "#F.orderTypeCode" });
     vat.item.SelectBind(vat.bean("allSalesFaultReasons")  ,{ itemName : "#F.salesFaultReason" });
     vat.item.SelectBind(vat.bean("allDeliverFaultReasons"),{ itemName : "#F.deliverFaultReason" });
     vat.item.SelectBind(vat.bean("allItemCategories")     ,{ itemName : "#F.itemCategory" });
     vat.item.SelectBind(vat.bean("allFlightAreas")        ,{ itemName : "#F.flightArea" });
     vat.item.SelectBind(vat.bean("allStoreAreas")         ,{ itemName : "#F.storeArea" });
     //vat.item.SelectBind(vat.bean("allReportList"),{ itemName : "#F.reportList" });
     vat.item.bindAll();
     if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	 }
     vat.item.setValueByName("#F.updateFlag","N");
     vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
	 doFormAccessControl();
  }
}
function getLastPage(vGrid){
	var	vLastPage = Math.floor(vat.block.getGridObject(vGrid).lastIndex / vat.block.getGridObject(vGrid).pageSize) + 1;
 	return vLastPage;
}

function addBlackLists(){
        
    var vBrandCode = vat.item.getValueByName("#F.brandCode");
    var vName =vat.item.getValueByName("#F.customerName");	   
    //alert(vName);
    var vPassportNo = vat.item.getValueByName("#F.passportNo");
    if(""!=vPassportNo){
		var returnData = window.showModalDialog(
			"So_Delivery:blackList:20101201.page"+
			"?brandCode=" + vBrandCode +"&"+
			"customerName=" + vName +"&"+
			"passportNo=" + vPassportNo
			,"",
			"dialogHeight:500px;dialogWidth:700px;dialogTop:100px;dialogLeft:100px;");
	}else{
		alert("護照號碼空白，不可加入黑名單");
	}
}
function checkBlackList(){
	//alert("護照號碼:"+vat.item.getValueByName("#F.passportNo"));
    if("" != vat.item.getValueByName("#F.passportNo")){
   		 vat.bean().vatBeanOther.passportNo= vat.item.getValueByName("#F.passportNo");
         vat.block.submit(function(){return "process_object_name=soDeliveryBlackListService"+
		            "&process_object_method_name=isBlackListByPassportNo";},  {other:true,picker:false,
		 funcSuccess: function() {
		 		 vat.item.setValueByName("#F.isBlackList", vat.bean().vatBeanOther.isBlackList);
			     doFormAccessControl();
		     }
		});
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

/*
//列印
function openReportWindow() {
    vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.reportFunctionCode = vat.item.getValueByName("#F.reportList");
    vat.bean().vatBeanOther.orderNo = vat.item.getValueByName("#F.orderNo");
    vat.block.submit(function () {
        return "process_object_name=soDeliveryMoveService" + "&process_object_method_name=getReportConfig";
    }, {
        other: true,
        funcSuccess: function () {
            //vat.item.setValueByName("#F.remark2", vat.bean().vatBeanOther.reportUrl);
            eval(vat.bean().vatBeanOther.reportUrl);

           
        }
    });
   
}*/

function getDeliveryInfo(){
    var nItemLine = vat.item.getGridLine();
    var vDeliveryOrderType = "DZN";//vat.item.getGridValueByName("deliveryOrderType", nItemLine);
    var vDeliveryOrderNo = vat.item.getGridValueByName("deliveryOrderNo", nItemLine).toUpperCase();
    vat.item.setGridValueByName("deliveryOrderNo", nItemLine,vDeliveryOrderNo);
    var vLineBagCounts1=0;
    var vLineBagCounts2=0;
    var vLineBagCounts3=0;
    var vLineBagCounts4=0;
    var vLineBagCounts5=0;
    var vLineBagCounts6=0;
    var vLineBagCounts7=0;
    var vAllow=true;
    if (nItemLine >1){
    	if(vDeliveryOrderNo == vat.item.getGridValueByName("deliveryOrderNo", nItemLine-1)){
    		alert("單號重覆("+vDeliveryOrderNo+")，請重新輸入");
    		vAllow = false;
    	}
    } 
    
    if(vAllow && vDeliveryOrderNo != ""){
    	vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
    	vat.bean().vatBeanOther.deliveryStoreArea = vat.item.getValueByName("#F.deliveryStoreArea");
    	vat.bean().vatBeanOther.deliveryOrderType = vDeliveryOrderType;
    	vat.bean().vatBeanOther.deliveryOrderNo = vDeliveryOrderNo;
	    vat.block.submit(function () {
	        return "process_object_name=soDeliveryMoveService" + "&process_object_method_name=getSoDeliveryInfo";
	   		 }, {
	        other: true,
	        funcSuccess: function () {
				 vLineBagCounts1 = parseInt(vat.bean().vatBeanOther.lineBagCounts1,10);
				 vLineBagCounts2 = parseInt(vat.bean().vatBeanOther.lineBagCounts2,10);
				 vLineBagCounts3 = parseInt(vat.bean().vatBeanOther.lineBagCounts3,10);
				 vLineBagCounts4 = parseInt(vat.bean().vatBeanOther.lineBagCounts4,10);
				 vLineBagCounts5 = parseInt(vat.bean().vatBeanOther.lineBagCounts5,10);
				 vLineBagCounts6 = parseInt(vat.bean().vatBeanOther.lineBagCounts6,10);
				 vLineBagCounts7 = parseInt(vat.bean().vatBeanOther.lineBagCounts7,10);
				 
				 vat.item.setGridValueByName("lineBagCounts1", nItemLine, vLineBagCounts1);
				 vat.item.setGridValueByName("lineBagCounts2", nItemLine, vLineBagCounts2);
				 vat.item.setGridValueByName("lineBagCounts3", nItemLine, vLineBagCounts3);
				 vat.item.setGridValueByName("lineBagCounts4", nItemLine, vLineBagCounts4);
				 vat.item.setGridValueByName("lineBagCounts5", nItemLine, vLineBagCounts5);
				 vat.item.setGridValueByName("lineBagCounts6", nItemLine, vLineBagCounts6);
				 vat.item.setGridValueByName("lineBagCounts7", nItemLine, vLineBagCounts7);
	           	 vat.item.setValueByName("#F.bagCounts1", parseInt(vat.item.getValueByName("#F.bagCounts1"),10)+vLineBagCounts1);
	           	 vat.item.setValueByName("#F.bagCounts2", parseInt(vat.item.getValueByName("#F.bagCounts2"),10)+vLineBagCounts2);
	           	 vat.item.setValueByName("#F.bagCounts3", parseInt(vat.item.getValueByName("#F.bagCounts3"),10)+vLineBagCounts3);
	           	 vat.item.setValueByName("#F.bagCounts4", parseInt(vat.item.getValueByName("#F.bagCounts4"),10)+vLineBagCounts4);
	           	 vat.item.setValueByName("#F.bagCounts5", parseInt(vat.item.getValueByName("#F.bagCounts5"),10)+vLineBagCounts5);
	           	 vat.item.setValueByName("#F.bagCounts6", parseInt(vat.item.getValueByName("#F.bagCounts6"),10)+vLineBagCounts6);
	           	 vat.item.setValueByName("#F.bagCounts7", parseInt(vat.item.getValueByName("#F.bagCounts7"),10)+vLineBagCounts7);
	             totalBagCounts();
	             vat.item.setGridValueByName("message", nItemLine,  vat.bean().vatBeanOther.message);
	        }
	    });
	     vat.item.setGridValueByName("message", nItemLine,  vat.bean().vatBeanOther.message);
    }else{
  		 vat.item.setGridValueByName("lineBagCounts1", nItemLine, 0);
		 vat.item.setGridValueByName("lineBagCounts2", nItemLine, 0);
		 vat.item.setGridValueByName("lineBagCounts3", nItemLine, 0);
		 vat.item.setGridValueByName("lineBagCounts4", nItemLine, 0);
		 vat.item.setGridValueByName("lineBagCounts5", nItemLine, 0);
		 vat.item.setGridValueByName("lineBagCounts6", nItemLine, 0);
		 vat.item.setGridValueByName("lineBagCounts7", nItemLine, 0);
       	 vat.item.setValueByName("#F.bagCounts1", parseInt(vat.item.getValueByName("#F.bagCounts1"),10)-parseInt(vat.item.getGridValueByName("lineBagCounts1", nItemLine),10));
       	 vat.item.setValueByName("#F.bagCounts2", parseInt(vat.item.getValueByName("#F.bagCounts2"),10)-parseInt(vat.item.getGridValueByName("lineBagCounts2", nItemLine),10));
       	 vat.item.setValueByName("#F.bagCounts3", parseInt(vat.item.getValueByName("#F.bagCounts3"),10)-parseInt(vat.item.getGridValueByName("lineBagCounts3", nItemLine),10));
       	 vat.item.setValueByName("#F.bagCounts4", parseInt(vat.item.getValueByName("#F.bagCounts4"),10)-parseInt(vat.item.getGridValueByName("lineBagCounts4", nItemLine),10));
       	 vat.item.setValueByName("#F.bagCounts5", parseInt(vat.item.getValueByName("#F.bagCounts5"),10)-parseInt(vat.item.getGridValueByName("lineBagCounts5", nItemLine),10));
       	 vat.item.setValueByName("#F.bagCounts6", parseInt(vat.item.getValueByName("#F.bagCounts6"),10)-parseInt(vat.item.getGridValueByName("lineBagCounts6", nItemLine),10));
       	 vat.item.setValueByName("#F.bagCounts7", parseInt(vat.item.getValueByName("#F.bagCounts7"),10)-parseInt(vat.item.getGridValueByName("lineBagCounts7", nItemLine),10));

		 totalBagCounts();
    }
    
}
function totalBagCounts(){
	vat.item.setValueByName("#F.totalBagCounts",
		parseInt(vat.item.getValueByName("#F.bagCounts1"),10)+
		parseInt(vat.item.getValueByName("#F.bagCounts2"),10)+
		parseInt(vat.item.getValueByName("#F.bagCounts3"),10)+
		parseInt(vat.item.getValueByName("#F.bagCounts4"),10)+
		parseInt(vat.item.getValueByName("#F.bagCounts5"),10)+
		parseInt(vat.item.getValueByName("#F.bagCounts6"),10)+
		parseInt(vat.item.getValueByName("#F.bagCounts7"),10));
}

function changeStoreArea(){
	vat.block.pageDataSave( vnB_Detail ,{
		saveSuccessAfter:function(){
			vat.ajax.XHRequest({
		           post:"process_object_name=soDeliveryMoveService"+
		                    "&process_object_method_name=updateStoreArea"+
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&deliveryStoreArea=" + vat.item.getValueByName("#F.deliveryStoreArea")+
		                    "&arrivalStoreArea=" + vat.item.getValueByName("#F.arrivalStoreArea"),
		           find: function change(oXHR){ 
		           		vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
		           }
			});
		}
	});

}		
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	refreshForm("");
	 }
}

function createRefreshForm(){
	vat.item.setValueByName("#F.headId","");
	refreshForm("");
}

function refreshForm(vsHeadId) {
    document.forms[0]["#formId"].value = vsHeadId;
    
    if (vsHeadId==""){
    	document.forms[0]["#processId"].value = "";
    	document.forms[0]["#assignmentId"].value = "";
    }

    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
    vat.bean().vatBeanOther.processId = document.forms[0]["#processId"].value;
    vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"].value;
    
    vat.block.submit(
    function () {
        return "process_object_name=soDeliveryMoveService&process_object_method_name=executeInitial";
    }, {
        other: true,
        funcSuccess: function () {
            vat.item.bindAll();
            vat.block.pageRefresh(vnB_Detail);
            doFormAccessControl();   
            if (""==vat.item.getValueByName("#F.moveEmployee"))
            	vat.item.setValueByName("#F.moveEmployeeName","");
            	
            vat.tabm.displayToggle(0, "xTab2", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false)
        }
    });
}
//列印
function openReportWindow(vsType) {
	var vbAllow = true;
    if(vbAllow){
	    vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.loginBrandCode");
	    vat.bean().vatBeanOther.executeEmployeeCode = vat.bean().vatBeanOther.loginEmployeeCode;
	    vat.bean().vatBeanOther.reportFunctionCode = vat.item.getValueByName("#F.reportList");
	    if(vsType =="MANUAL"){
	    	vat.bean().vatBeanOther.startOrderNo =vat.item.getValueByName("#F.orderNo");
	    	vat.bean().vatBeanOther.endOrderNo   =vat.item.getValueByName("#F.orderNo");
	    }else{
    	    vat.bean().vatBeanOther.startOrderNo =vat.bean().vatBeanOther.orderNo;
	        vat.bean().vatBeanOther.endOrderNo = vat.bean().vatBeanOther.orderNo;
	    }
	   
	    vat.bean().vatBeanOther.startOrderDate = "";
	    vat.bean().vatBeanOther.endOrderDate =  "";
	    vat.bean().vatBeanOther.moveEmployee =  "";
	    vat.bean().vatBeanOther.deliveryStoreArea =  "";
	    vat.bean().vatBeanOther.arrivalStoreArea =  "";
	    vat.bean().vatBeanOther.deliveryOrderNo =  "";
	    vat.bean().vatBeanOther.status =  "";
	    vat.block.submit(function () {
	        return "process_object_name=soDeliveryMoveService" + "&process_object_method_name=getReportConfig";
	    }, {
	        other: true,
	        funcSuccess: function () {
	            //vat.item.setValueByName("#F.remark2", vat.bean().vatBeanOther.reportUrl);
	            eval(vat.bean().vatBeanOther.reportUrl);
	
	           
	        }
	    });
	}
   
}
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();

}

function changeUpdateFlag(){
	var vsAllowSubmit = true;
	if("Y" == vat.item.getValueByName("#F.updateFlag")) {
		vat.item.setValueByName("#F.updateFlag","N");
	}else{
		//if(vat.item.getValueByName("#F.deliveryStoreArea")== vat.item.getValueByName("#F.arrivalStoreArea")){
		//	alert("轉出庫別不可與轉入庫別相同");
		//	vsAllowSubmit = false;
		//}
		if(""==vat.item.getValueByName("#F.moveEmployee") || ""==vat.item.getValueByName("#F.moveEmployeeName")){
			alert("請輸入修改人員工號");
			vsAllowSubmit = false;
		}
	    if(vsAllowSubmit){
			vat.item.setValueByName("#F.updateFlag","Y");
			vat.form.item.setFocus( "#F.searchNo" );	
		}	
	}
	doFormAccessControl();
}



function searchDelivery(triggeredBy){
	vat.item.setValueByName("#F.searchNo",vat.item.getValueByName("#F.searchNo").toUpperCase());
    var vsSearchNo   = vat.item.getValueByName("#F.searchNo");
    var vsActionType= vat.item.getValueByName("#F.actionType");
    var vsAllow = false;
    var pageCount=1;
	if(vsSearchNo.length > 0 ){
	    vsAllow = true;
	}else{
	    vsAllow = false;
		if (triggeredBy == "searchButton" )
			alert("未輸入提貨單號，請重新輸入!");
	}
	if(vsAllow){
		vat.block.pageDataSave(vnB_Detail,{
			funcSuccess:function(){
		  		vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
		  		vat.bean().vatBeanOther.orderTypeCode = "DMP";
		  		vat.bean().vatBeanOther.deliveryOrderNo = vsSearchNo;
		  		vat.bean().vatBeanOther.deliveryStoreArea = vat.item.getValueByName("#F.deliveryStoreArea");
		  		vat.bean().vatBeanOther.arrivalStoreArea = vat.item.getValueByName("#F.arrivalStoreArea");
		  		vat.bean().vatBeanOther.moveEmployee = vat.item.getValueByName("#F.moveEmployee");
				vat.bean().vatBeanOther.actionType = vsActionType;
				vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
			
				vat.block.submit(function(){return "process_object_name=soDeliveryMoveAction"+
		            "&process_object_method_name=saveMoveLine";},  {other:true,picker:false,
		     	 funcSuccess: function() {
 				 vLineBagCounts1 = parseInt(vat.bean().vatBeanOther.bagCounts1,10);
				 vLineBagCounts2 = parseInt(vat.bean().vatBeanOther.bagCounts2,10);
				 vLineBagCounts3 = parseInt(vat.bean().vatBeanOther.bagCounts3,10);
				 vLineBagCounts4 = parseInt(vat.bean().vatBeanOther.bagCounts4,10);
				 vLineBagCounts5 = parseInt(vat.bean().vatBeanOther.bagCounts5,10);
				 vLineBagCounts6 = parseInt(vat.bean().vatBeanOther.bagCounts6,10);
				 vLineBagCounts7 = parseInt(vat.bean().vatBeanOther.bagCounts7,10);
	           	 vat.item.setValueByName("#F.bagCounts1", vLineBagCounts1);
	           	 vat.item.setValueByName("#F.bagCounts2", vLineBagCounts2);
	           	 vat.item.setValueByName("#F.bagCounts3", vLineBagCounts3);
	           	 vat.item.setValueByName("#F.bagCounts4", vLineBagCounts4);
	           	 vat.item.setValueByName("#F.bagCounts5", vLineBagCounts5);
	           	 vat.item.setValueByName("#F.bagCounts6", vLineBagCounts6);
	           	 vat.item.setValueByName("#F.bagCounts7", vLineBagCounts7);
	           	 vat.form.item.setFocus( "#F.searchNo" );
	           	 recordCounts = parseInt(vat.bean().vatBeanOther.recordCounts);
	           	 pageCount = Math.floor((recordCounts  /10))+1;
	           	 vat.block.pageCount(vnB_Detail,pageCount);
	           	 vat.block.pageThere(vnB_Detail,pageCount);
	           	 vat.block.pageRefresh(vnB_Detail);
	         //  	 vat.block.pageDataLoad(pageCount);
		     }
		});
			}
		});
	
	
	


	}
}


function tempRedirect(){}

function afterImportSuccess(){
    //refreshHeadData();
}

function getApprovalResult(){
	return "true";
}