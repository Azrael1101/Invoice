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
var vnB_master = 2;
var vnB_Detail = 3;
var vnB_Log    = 4;
var vnB_So_Head =1;

function kweBlock(){
   if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   
  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     orderTypeCode      : vat.item.getValueByName("#F.orderTypeCode") == "" ? document.forms[0]["#orderTypeCode"].value:vat.item.getValueByName("#F.orderTypeCode"),
	     formId             : document.forms[0]["#formId"            ].value,
  	     processMode        : document.forms[0]["#processMode" ].value,
  	     loginProcessType   : document.forms[0]["#processType" ].value,
	     updateForm         : document.forms[0]["#formId"].value==""?"N":"Y", 
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	 };
	 
  }
  //var userType			  = document.forms[0]["#userType"      ].value;
  /*  
  if( userType=="SERVICE" ){
      alert(userType);
  }
  */
  kweButtonLine();
  kweSearch();
  kweHeader();
  

  if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","主檔資料"   ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);
		vat.tabm.createButton(0 ,"xTab2","明細檔資料" ,"vatDetailDiv"    ,"images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false);  
		if (vat.bean().vatBeanOther.loginProcessType!="SEARCH")
		  vat.tabm.createButton(0 ,"xTab3","異動資料"   ,"vatLogDiv"       ,"images/tab_data_log_dark.gif"        ,"images/tab_data_log_light.gif", false);
		else
		  vat.tabm.createButton(0 ,"xTab3","異動資料"   ,"vatLogDiv"       ,"images/tab_data_log_dark.gif"        ,"images/tab_data_log_light.gif", "none");
  }
  kweMaster();
  kweDetail();
  
  kweLog();
 
}


function kweButtonLine(){

    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    var allProcessType = [["","",false],
                 ["手動","自動"],
                 ["MANUAL","AUTO"]];
    if(vat.bean().vatBeanOther.processMode =="" || vat.bean().vatBeanOther.loginProcessType ==""){            
	    vat.block.create(vnB_Button, {
		id: "vatBlock_Button", generate: true,
		table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 3px; border-left-width: 0px; border-color: #ffffff;'",
		title:"", rows:[
		 {row_style:"", cols:[
		 	{items:[
		 			{name:"#B.edit"        , type:"IMG"    , value:"修改",  src:"./images/button_edit.gif", eClick:"changeUpdateFlag()"},	
		 			{name:"#B.unedit"      , type:"IMG"    , value:"取消修改",  src:"./images/button_unedit.gif", eClick:"changeUpdateFlag()"},
		 			{name:"#B.new"         , type:"IMG"    , value:"建檔",  src:"./images/button_new.gif", eClick:"changeNew()"},	
		 			{name:"#B.unnew"       , type:"IMG"    , value:"取消建檔",  src:"./images/button_unnew.gif", eClick:"changeNew()"},					 
		 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  ,
		 									 openMode:"open",
		 									 service:"So_Delivery:search:20101126.page",
		 									 servicePassData:function(x){ return doPassHeadData(x); },
		 									 left:0, right:0, width:1024, height:768,
		 									 serviceAfterPick:function(){doAfterPickerProcess()}},
		 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
		 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
		 			{name:"#B.createmark"  , type:"IMG"    ,value:"建檔註記"   ,   src:"./images/button_createmark.gif"  , eClick:'changeStatus("W_CREATE")'},
		 			{name:"#B.close"       , type:"IMG"    ,value:"結案"   ,   src:"./images/button_close.gif"  , eClick:'changeStatus("CLOSE")'},
		 			//{name:"#B.cancel"      , type:"IMG"    ,value:"取消"   ,   src:"./images/button_cancel.gif"  , eClick:'changeStatus("CANCEL")'},
		 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'changeStatus("VOID")'},
		 			{name:"#B.return"      , type:"IMG"    ,value:"退貨"   ,   src:"./images/button_return.gif"  , eClick:'changeStatus("RETURN")'},		
		 			{name:"#B.wReturn"     , type:"IMG"    ,value:"待銷退"   ,   src:"./images/button_w_return.gif"  , eClick:'changeStatus("W_RETURN")'},
		 			{name:"#B.refund"      , type:"IMG"    ,value:"已退款"   ,   src:"./images/button_refund.gif"  , eClick:'changeStatus("REFUND")'},
		 			{name:"#B.addBlacklist", type:"IMG"    ,value:"加入常客",   src:"./images/button_blacklist.gif"   ,
		 									 openMode:"open",
		 									 service:"So_Delivery:blackList:20101201.page",
		 									 servicePassData:function(x){ return doPassBlackData(x); },
		 									 left:0, right:0, width:1024, height:768,
		 									 serviceAfterPick:function(){doAfterPickerBlackProcess()}},
					{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
		 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},               
                    {name:"SPACE"          , type:"LABEL"  ,value:"　"},
                    {name:"#B.print"       , type:"IMG"    ,value:"提領證明",   src:"./images/button_certificate.gif" , eClick:'openReportWindow()'},
                    {name:"#B.printView"   , type:"IMG"    ,value:"畫面列印",   src:"./images/button_print.gif" 		 , eClick:'printView()'},
                    {name:"SPACE"          , type:"LABEL"  ,value:"　"},
		 			{name:"#B.unusual"     , type:"IMG"    ,value:"異常儲位",   src:"./images/button_unusual.gif"      , eClick:'doUpdateStorageCode()'},
		 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/v2/icon_b02.gif"   , eClick:"gotoFirst()"},
		 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/v2/icon_b03.gif"     , eClick:"gotoForward()"},
		 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/v2/icon_b06.gif"           , eClick:"gotoNext()"},
		 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/v2/icon_b07.gif" , eClick:"gotoLast()"},
		 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 2, mode:"READONLY"},
		 			{name:"SPACE"           , type:"LABEL" ,value:"/"},
		 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 2, mode:"READONLY" },
					{name:"SPACE"          , type:"LABEL"  ,value:"　"}
		 			//{name:"#F.reportList"  , type:"SELECT" },
		 			],
		 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]},
		  ],
			beginService:function(){return ""}	,
			closeService:function(){return ""}
		});
		
	}else{
	    vat.block.create(vnB_Button, {
		id: "vatBlock_Button", generate: true,
		table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 3px; border-left-width: 0px; border-color: #ffffff;'",
		title:"", rows:[
		 {row_style:"", cols:[
		 	{items:[{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
		 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
		 			{name:"#B.processType" , type:"IMG", value:"處理", src:"./images/button_process.gif" , eClick:"doProcess()"}],
		 			 td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"},
		 	{items:[{name:"#L.processType"     , type:"LABEL", value:"處理項目："},
		            {name:"#F.processType"     , type:"SELECT", bind:"process"},
		            {name:"#L.processMode"              , type:"LABEL"  ,value:"&nbsp&nbsp&nbsp;執行模式："},
		 	        {name:"#F.processMode"              , type:"SELECT"  ,  bind:"processMode", size:20, init:allProcessType}]
		 	        ,  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
		 	        }]},
		  ],
			beginService:function(){return ""}	,
			closeService:function(){return ""}
		});
	}
	vat.item.setValueByName("#F.processMode"      ,vat.bean().vatBeanOther.processMode);
	vat.item.setValueByName("#F.processType"      ,vat.bean().vatBeanOther.loginProcessType);
}

function kweSearch(){
var allStatus = [["","",true],
                 ["待取貨","待建檔","待提領","結案","作廢","取消","退貨","待銷退","已退款"],
                 ["W_PICK","W_CREATE","W_DELIVERY","CLOSE","VOID","CANCEL","RETURN","W_RETURN","REFUND"]];
if(vat.bean().vatBeanOther.loginProcessType=="SEARCH"){
vat.block.create(vnB_Search, {
    id: "vatBlock_Search", generate: true,
    table:"cellspacing='1' class='default' border='0' cellpadding='5' style='border-right-width: 0px; border-bottom-width: 5px; border-left-width: 0px; border-color: #ffffff;'",
    title:"", 
    rows:[
         {row_style:"background-color: #990000", cols:[     
         {items:[{name:"#L.searchNo"     , type:"LABEL", value:"&nbsp&nbsp&nbsp;<FONT SIZE=4><BOLD>單號</BOLD></FONT>&nbsp&nbsp&nbsp;"}
                 ],  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
         {items:[{name:"#F.searchNo"     , type:"TEXT" ,mask:"CCCCCCCCCCCCC", bind:"searchNo", size:13, maxLen:13,  style:"font-size: 20pt; height:40px",  eChange:'searchDelivery("searchNo")'}], 
                  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
                // {name:"#F.buffer"     , type:"TEXT" , size:1,  maxLen:1,  eFocus:'setNoFocus()'},
                // {name:"#L.searchStatus" , type:"LABEL", value:"&nbsp&nbsp&nbsp;狀態：&nbsp&nbsp&nbsp;"},
                // {name:"#F.searchStatus" , type:"SELECT" , bind:"searchStatus", init:allStatus},
         {items:[{name:"#B.findDelivery" , type:"BUTTON"  ,value:"查詢",  size:14, eClick:'searchDelivery("searchButton")'}
                 ],  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},                          
         {items:[{name:"#L.searchstorageCode"     , type:"LABEL", value:"&nbsp&nbsp&nbsp;<FONT SIZE=4><BOLD>儲位</BOLD></FONT>&nbsp&nbsp&nbsp;"}
                 ],  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
         {items:[ {name:"#F.searchstorageCode"     , type:"TEXT" , bind:"storageCode", mode:"READONLY", style:"font-size: 25pt; height:40px; color:FF0000; font-weight:bold;"}], 
                  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"}]}   
          ],
        beginService:"",
        closeService:""
    });
}
else
{
	vat.block.create(vnB_Search, {
		id: "vatBlock_Search", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='5' style='border-right-width: 0px; border-bottom-width: 5px; border-left-width: 0px; border-color: #ffffff;'",
		title:"", 
		rows:[
		 {row_style:"background-color: #990000", cols:[
		 {items:[{name:"#L.searchNo"     , type:"LABEL", value:"&nbsp&nbsp&nbsp;<FONT SIZE=4><BOLD>單號</BOLD></FONT>&nbsp&nbsp&nbsp;"}], 
		          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
		 {items:[{name:"#F.searchNo"     , type:"TEXT" ,mask:"CCCCCCCCCCCCC", bind:"searchNo", size:13, maxLen:13,  style:"font-size: 20pt; height:40px",  eChange:'searchDelivery("searchNo")'}], 
		          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
		        // {name:"#F.buffer"     , type:"TEXT" , size:1,  maxLen:1,  eFocus:'setNoFocus()'},
		        // {name:"#L.searchStatus" , type:"LABEL", value:"&nbsp&nbsp&nbsp;狀態：&nbsp&nbsp&nbsp;"},
		        // {name:"#F.searchStatus" , type:"SELECT" , bind:"searchStatus", init:allStatus},
		 {items:[{name:"#B.findDelivery" , type:"BUTTON"  ,value:"查詢",  size:14, eClick:'searchDelivery("searchButton")'}
		         ],  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
	
		 {items:[{name:"#L.employeeCode" , type:"LABEL", value:"<FONT SIZE=4><BOLD>工號</BOLD></FONT>"}], 
		          td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 	 {items:[{name:"#F.employeeCode" , type:"TEXT" ,mask:"CCCCCCC", bind:"employeeCode", size:4, style:"font-size: 20pt; height:40px",eChange:'getEmployeeInfo()'},
		 		 {name:"#F.employeeName" , type:"TEXT" ,mask:"AAAAAAAAAAAAAAAAAAAA", bind:"employeeName", size:4, style:"font-size: 20pt; height:40px",mode:"READONLY"}], 
		 		  td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
		 {items:[{name:"#L.pDeliveryDate" , type:"LABEL", value:"<FONT SIZE=4><BOLD>提貨日</BOLD></FONT>"}], 
		          td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 	 {items:[{name:"#F.pDeliveryDate" , type:"DATE" ,  size:4, style:"font-size: 20pt; height:40px"}], 
		 		  td:"style='background-color:#FFFF33; border-color:#FFFF33;'"},
	 	 {items:[{name:"#G.lock"        , type:"IMG"    ,value:"",    src:"./images/lock.png"},
		 		  {name:"#G.unlock"      , type:"IMG"    ,value:"",    src:"./images/unlock.png"}], 
		 		  td:"style='background-color:#FFFF33; border-color:#FFFF33;'"}]} 		  
		  ],
			beginService:"",
			closeService:""
		});
	    vat.item.setStyleByName("#G.lock"        , "display", "none");
	    vat.item.setStyleByName("#G.unlock"      , "display", "none");
    }
}

function kweHeader(){
//var allStorageCode = [["","",false],["00000","XXXXX"],["00000","XXXXX"]];
var vsRowStyle= "T2" == vat.bean("loginBrandCode")?"":" style= 'display:none;'";
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"入境提貨維護作業", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderNo"      , type:"LABEL" , value:"單號"}]},
	 {items:[{name:"#F.orderNo"      , type:"TEXT"  ,mask:"CCCCCCCCCCCCC", bind:"orderNo",back:false,size:14, mode:"READONLY"}, 
	         {name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode", mode:"HIDDEN"},
	         {name:"#F.brandCode"    , type:"TEXT" ,  bind:"brandCode",mode:"HIDDEN"},
	         {name:"#F.updateFlag"   , type:"TEXT" ,  size:4, bind:"updateFlag",mode:"HIDDEN"},
	 		 {name:"#F.headId"      , type:"NUMB"  ,  size:6, bind:"headId", back:false, mode:"READONLY" }]}, 
	 {items:[{name:"#L.orderDate"   , type:"LABEL" , value:"申請日期"}]},
	 {items:[{name:"#F.orderDate"   , type:"DATE"  ,  bind:"orderDate"}]},
	 {items:[{name:"#L.storeArea"   , type:"LABEL" , value:"存放地點"}]},
	 {items:[{name:"#F.storeArea"   , type:"SELECT"  ,  bind:"storeArea"}]},
	 {items:[{name:"#L.storageCode"   , type:"LABEL" , value:"儲位"}]},
	 {items:[//{name:"#F.storageCodeRewrite"   , type:"SELECT"  ,  bind:"storageCode", init:allStorageCode, mode:"HIDDEN"},
	 		 {name:"#F.storageCode"   , type:"TEXT"  ,  bind:"storageCode", mode:"READONLY"}]},
	 {items:[{name:"#L.status"      , type:"LABEL" , value:"狀態"}]},
	 {items:[{name:"#F.status"      , type:"TEXT"  ,  bind:"status",  mode:"HIDDEN"},
	  		 {name:"#F.statusName"  , type:"TEXT"  ,  bind:"statusName", size:4, back:false, mode:"READONLY"},
	  		 {name:"#F.lockFlag"    , type:"TEXT"  ,  bind:"lockFlag", back:false, mode:"HIDDEN"},
	  		 {name:"#F.lockFlagName", type:"TEXT"  ,  bind:"lockFlagName", size:4, back:false, mode:"HIDDEN"}]}]},
	 {row_style:vsRowStyle, cols:[
	 {items:[{name:"#L.customerName", type:"LABEL", value:"客戶姓名<font color='red'>*</font>"}]},
	 {items:[{name:"#F.customerName", type:"TEXT"  ,mask:"CCCCCCCCCCCCCCCCCCC",  bind:"customerName", size:20},
	         {name:"#F.customerCode", type:"TEXT",   bind:"customerCode", size:10, mode:"HIDDEN"}]},
	 {items:[{name:"#L.contactInfo" , type:"LABEL", value:"連絡電話<font color='red'>*</font>"}]},
	 {items:[{name:"#F.contactInfo" , type:"TEXT",   bind:"contactInfo", size:20, eChange:"checkBlackListTel()"},
 			 {name:"#F.isTelBlackList" , type:"TEXT",   bind:"isTelBlackList", size:4, mode:"HIDDEN" },
	         {name:"#B.greenTelBall", type:"IMG" ,src:"./images/greenBall.png"  },
	         {name:"#B.blackTelBall", type:"IMG" ,src:"./images/blackBall.png"  }]},
	 {items:[{name:"#L.passportNo"  , type:"LABEL", value:"護照號碼<font color='red'>*</font>"}]},
	 {items:[{name:"#F.passportNo"  , type:"TEXT" ,mask:"CCCCCCCCCCCCCCCCCCC",   bind:"passportNo", size:20, eChange:"checkBlackListPassport()"},
	  		 {name:"#F.isPassportBlackList" , type:"TEXT",   bind:"isPassportBlackList", size:4, mode:"HIDDEN" },
	         {name:"#B.greenPassportBall", type:"IMG" ,src:"./images/greenBall.png"  },
	         {name:"#B.blackPassportBall", type:"IMG" ,src:"./images/blackBall.png"  }], td:" colSpan=3"},
	 {items:[{name:"#L.totalBagCounts"    , type:"LABEL", value:"總袋數"}]},
	 {items:[{name:"#F.totalBagCounts"    , type:"NUMB",   bind:"totalBagCounts", size:4, mode:"READONLY"},
 			 {name:"#B.totalBagCounts"    , type:"IMG" ,value:"件數計算",   src:"./images/arrowdown.png" ,eClick:'showTotalCountPage()'}]}]},
	 {row_style:"", cols:[

	 {items:[{name:"#L.flightNo"    , type:"LABEL", value:"回程班機<font color='red'>*</font>"}]},
	 {items:[{name:"#F.flightNo"    , type:"TEXT",mask:"CCCCCC",   bind:"flightNo", size:6}]},
	 {items:[{name:"#L.flightDate"  , type:"LABEL", value:"回程日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.flightDate"  , type:"TEXT", size:8, maxLen:10, bind:"flightDate", eChange:'parseDate("#F.flightDate","AFTER_TODAY")'}]},
	 //{items:[{name:"#L.flightArea"  , type:"LABEL", value:"回程區域"}]},
	// {items:[{name:"#F.flightArea"  , type:"SELECT", bind:"flightArea"}]},
	 {items:[{name:"#L.scheduleDeliveryDate" , type:"LABEL", value:"預計提領日<font color='red'>*</font>"}]},
	 {items:[{name:"#F.scheduleDeliveryDate" , type:"TEXT", size:8, maxLen:10, bind:"scheduleDeliveryDate", eChange:'parseDate("#F.scheduleDeliveryDate"","AFTER_TODAY")'}], td:" colSpan=3"},
	 {items:[{name:"#L.deliveryDate"   , type:"LABEL", value:"結案日期"}]},
	 {items:[{name:"#F.deliveryDate"   , type:"DATE",   bind:"deliveryDate", mode:"READONLY", size:12}]}]}
	  ],
		beginService:"",
		closeService:"doCheckStore"
	});

}
function kweMaster(){
var allAffidavits = [["","",false],
                 ["無","有"],
                 ["N","Y"]];
var allAffidavits1 = [["","",false],
                 ["無","有"],
                 ["N","Y"]];                 
var allUpdateTypes = [["","","","",false],
                 ["請選擇","代領人","特殊提領要求","其他"],
                 ["","behalf","special","other"]];                 
vat.block.create(vnB_master, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.shopCode"    , type:"LABEL" , value:"店 別<font color='red'>*</font>"}]},
	 {items:[{name:"#F.shopCode"    , type:"TEXT",  bind:"shopCode", size:8,  eChange:'changeShopCode()'},
	         {name:"#F.shopName"    , type:"TEXT",  bind:"shopName", size:12,  mode:"READONLY"}]},
	 {items:[{name:"#L.itemCategory", type:"LABEL" , value:"業種<font color='red'>*</font>"}]},
	 {items:[{name:"#F.itemCategory", type:"SELECT"  ,  bind:"itemCategory", size:12, mode:"READONLY"}]},
	 {items:[{name:"#L.bagBarCode" , type:"LABEL" , value:"束袋條碼"}]},
	 {items:[{name:"#F.bagBarCode" , type:"TEXT", bind:"bagBarCode", size:30, maxLen:30, desc:""}], td:" colSpan=3"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.breakable" , type:"LABEL" , value:"商品類型"}]},
	 {items:[{name:"#F.breakable" , type:"CHECKBOX",  bind:"breakable", size:12},
	         {name:"#L.breakable" , type:"LABEL", value:"&nbsp&nbsp&nbsp;易碎品&nbsp&nbsp;"},
	         {name:"#F.valuable"  , type:"CHECKBOX",  bind:"valuable", size:12},
	         {name:"#L.valuable"  , type:"LABEL", value:"&nbsp&nbsp&nbsp;貴重品&nbsp&nbsp;"}]},
     {items:[{name:"#L.expiryDate" , type:"LABEL" , value:"最近到期效期"}]},
	 {items:[{name:"#F.expiryDate" , type:"TEXT", bind:"expiryDate", eChange:'parseDate("#F.expiryDate","NORMAL")'}]},
     {items:[{name:"#L.contactOverTime"       ,    type:"LABEL", value:"逾期聯絡說明"}], td:" colSpan=1.5"},
	 {items:[{name:"#F.contactOverTime" , type:"TEXT", bind:"contactOverTime", size:90, maxLen:200 }], td:" colSpan=3"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.bagCounts", type:"LABEL", value:"袋數"}]},
	 {items:[{name:"#L.bagCounts1", type:"LABEL", value:"大:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts1", type:"NUMB" , bind:"bagCounts1", size:5, eChange:'totalBagCount()'},
			 {name:"#L.bagCounts2", type:"LABEL", value:"&nbsp&nbsp&nbsp;中:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts2", type:"NUMB",  bind:"bagCounts2", size:5, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts3", type:"LABEL", value:"&nbsp&nbsp&nbsp;小:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts3", type:"NUMB",  bind:"bagCounts3", size:5, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts7", type:"LABEL", value:"&nbsp&nbsp&nbsp;小小袋:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts7", type:"NUMB",  bind:"bagCounts7", size:5, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts4", type:"LABEL", value:"&nbsp&nbsp&nbsp;拉桿箱:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts4", type:"NUMB",  bind:"bagCounts4", size:5, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts5", type:"LABEL", value:"&nbsp&nbsp&nbsp;折收袋:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts5", type:"NUMB",  bind:"bagCounts5", size:5, eChange:'totalBagCount()'},
	 		 {name:"#L.bagCounts6", type:"LABEL", value:"&nbsp&nbsp&nbsp;其他:&nbsp&nbsp;"},
	 		 {name:"#F.bagCounts6", type:"NUMB",  bind:"bagCounts6", size:5, eChange:'totalBagCount()'}], td:" colSpan=7"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.salesFaultReason" , type:"LABEL" , value:"營業異常"}]},
	 {items:[{name:"#F.salesFaultReason" , type:"SELECT", bind:"SFaultReason"}]},
	 {items:[{name:"#L.deliverFaultReason" , type:"LABEL" , value:"提貨異常"}]},
	 {items:[{name:"#F.deliverFaultReason" , type:"SELECT", bind:"DFaultReason"}]},
     {items:[{name:"#L.affidavit" , type:"LABEL" , value:"提領證明"}]},
	 {items:[{name:"#F.affidavit" , type:"SELECT", bind:"affidavit", init:allAffidavits}], td:" colSpan=3"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.soDelUpdateHeadCode"    , type:"LABEL", value:"更正單編號"}]},
	 {items:[{name:"#F.soDelUpdateHeadCode"    , type:"TEXT", bind:"soDelUpdateHeadCode"}
	         ]},
	 {items:[{name:"#L.createdBy1"    , type:"LABEL", value:"更正單主旨一"}]},
	 {items:[{name:"#L.oriDate"    , type:"LABEL", value:"<font size='0.5'>原日期:</font>"},
	 {name:"#F.oriDate"    , type:"TEXT" ,bind:"oriDate",size:"8", eChange:'parseDate("#F.oriDate","")'},
	 {name:"#L.oriFlight"    , type:"LABEL", value:"<font size='0.5'>原班機:</font>"},
	 {name:"#F.oriFlight"    , type:"TEXT" , bind:"oriFlight",size:"5"}]},
	 {items:[{name:"#L.madeBy1"       ,    type:"LABEL", value:"更正單主旨二"}]},
	 {items:[{name:"#L.updateType", bind:"updateType", init:allUpdateTypes      , type:"SELECT"},
	 {name:"#F.updateContent", bind:"updateContent"   , type:"TEXT" , size:90, maxLen:200}],td:" colSpan=3"}
	 
	]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.expiryReturnNo"   , type:"LABEL" , value:"逾期銷退單號"}]},
	 {items:[{name:"#F.expiryReturnNo"   , type:"TEXT", bind:"expiryReturnNo", maxLen:9 ,mask:"CCCCCCCCC"}]},
	 {items:[{name:"#L.expiryReturnMemo" , type:"LABEL" , value:"逾期銷退說明"}]},
	 {items:[{name:"#F.expiryReturnMemo" , type:"TEXT", bind:"expiryReturnMemo", size:80, maxLen:200}], td:" colSpan=7"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark1"     , type:"LABEL", value:"建檔補充說明"}]},
	 {items:[{name:"#F.remark1"     , type:"TEXT",   bind:"remark1", size:150, maxLen:250, desc:"放一般備註內容"}], td:" colSpan=7"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark2"      , type:"LABEL", value:"提領狀況說明"}]},
	 {items:[{name:"#F.remark2"      , type:"TEXT",   bind:"remark2", size:150, maxLen:250, desc:""}], td:" colSpan=7"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.contactInfo_cs"      , type:"LABEL", value:"客服聯絡資訊"}
	         ]},
	 //{items:[{name:"#L.contactInfo_cs"      , type:"LABEL", value:"客服聯絡資訊"}]},
	 {items:[{name:"#F.contactInfo_cs"      , type:"TEXT",   bind:"contactInfo_cs", size:150, maxLen:250, desc:""}], td:" colSpan=7"}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.cs_note"      , type:"LABEL", value:"客服備註資訊"},
	         {name:"#F.groupCode"   , type:"TEXT" ,  size:4, bind:"groupCode",mode:"HIDDEN"}]},
	 {items:[{name:"#F.cs_Note"      , type:"TEXT",   bind:"cs_Note", mode:"READONLY" , size:210, maxLen:250, desc:""}], td:" colSpan=7"}]},
     {row_style:"", cols:[
	 {items:[{name:"#L.createdBy"    , type:"LABEL", value:"填單人員"}]},
	 {items:[{name:"#F.createdBy"    , type:"TEXT",   bind:"createdBy", mode:"READONLY", size:6},
	         {name:"#F.createdByName", type:"TEXT",   bind:"createdByName", mode:"READONLY", size:12}]},
	 {items:[{name:"#L.createdBy"    , type:"LABEL", value:"填單日期"}]},
	 {items:[{name:"#F.creationDate" , type:"DATE",   bind:"creationDate", back:false,mode:"READONLY", size:12}]},
	 {items:[{name:"#L.madeBy"       ,    type:"LABEL", value:"建檔人員"}]},
	 {items:[{name:"#F.madeBy"        , type:"TEXT",   bind:"madeBy", mode:"READONLY", size:6},
	         {name:"#F.madeByName"   , type:"TEXT",   bind:"madeByName", mode:"READONLY", size:12}]},
	 {items:[{name:"#L.makeDate"      , type:"LABEL", value:"建檔日期"}]},
	 {items:[{name:"#F.makeDate"      , type:"DATE",   bind:"makeDate", back:false,mode:"READONLY", size:12}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.lastUpdatedBy"  , type:"LABEL", value:"最近修改人員"}]},
	 {items:[{name:"#F.lastUpdatedBy"  , type:"TEXT",   bind:"lastUpdatedBy", mode:"READONLY", size:6},
	         {name:"#F.lastUpdatedByName", type:"TEXT",   bind:"lastUpdatedByName", mode:"READONLY", size:12}]},
	 {items:[{name:"#L.lastUpdateDate", type:"LABEL", value:"最近修改日期"}]},
	 {items:[{name:"#F.lastUpdateDate", type:"DATE",   bind:"lastUpdateDate", back:false,mode:"READONLY", size:12}]},
	 {items:[{name:"#L.deliveredBy"  , type:"LABEL", value:"結案人員"}]},
	 {items:[{name:"#F.deliveredBy"  , type:"TEXT",   bind:"deliveredBy", mode:"READONLY", size:6},
	         {name:"#F.deliveredByName", type:"TEXT",   bind:"deliveredByName", mode:"READONLY", size:1}], td:" colSpan=3"}]}
 	 	],
		beginService:"",
		closeService:""
	});
}

function kweDetail(){
  
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = false;
    var vnB_Detail = 3;
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"       });
	vat.item.make(vnB_Detail, "salesOrderDate"            , {type:"DATE" , size: 3, maxLen:20, mode:"READONLY", desc:"交易日期"   });
	vat.item.make(vnB_Detail, "posMachineCode"            , {type:"TEXT" , size: 4, maxLen:06, mode:"READONLY", desc:"機號"   });
	vat.item.make(vnB_Detail, "customerPoNo"              , {type:"TEXT" , size:20, maxLen:20, mode:"READONLY", desc:"銷售單號"});
	vat.item.make(vnB_Detail, "transactionSeqNo"          , {type:"TEXT" , size:20, maxLen:20, mode:"READONLY", desc:"交易序號"});
	vat.item.make(vnB_Detail, "superintendentCode"        , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"營業人員"});
	vat.item.make(vnB_Detail, "superintendentName"        , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"營業姓名"});
	vat.item.make(vnB_Detail, "countryCode"               , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"國籍"}); 
	vat.item.make(vnB_Detail, "breakable"                 , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"易碎品"});
	vat.item.make(vnB_Detail, "valuable"                  , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"貴重品"});
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"   });
	vat.item.make(vnB_Detail, "salesOrderId"              , {type:"TEXT", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "lineId"                    , {type:"ROWID"});
	vat.item.make(vnB_Detail, "detailItem"                , {type:"BUTTON", view: "fixed", desc:"", value:"商品明細", src:"images/button_advance_input.gif", eClick:"openSoDetail()"});
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "pageLoadSuccess()",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "3"
														});
}

function kweLog(){
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = false;
    var vnB_Log = 4;
    vat.item.make(vnB_Log, "indexNo"   , {type:"IDX"    ,                     desc:"序號"       });
	vat.item.make(vnB_Log, "logDate"   , {type:"DATE"   , desc:"異動日期"   });
	vat.item.make(vnB_Log, "logType"   , {type:"TEXT"   , size:6, desc:"類型"   });
	vat.item.make(vnB_Log, "logAction" , {type:"TEXT"   , desc:"動作"});
	vat.item.make(vnB_Log, "logLevel"  , {type:"TEXT"   , desc:"等級"});
	vat.item.make(vnB_Log, "message"   , {type:"TEXT"   , size:70, desc:"訊息", alter:true}); 
	vat.item.make(vnB_Log, "createrName" , {type:"TEXT" , size:8, desc:"執行人員"}); 
	vat.item.make(vnB_Log, "creationDate", {type:"TEXT" , size:18, desc:"執行日期"}); 
	vat.item.make(vnB_Log, "lineId"    , {type:"ROWID"});
	vat.block.pageLayout(vnB_Log, {
														id                  : "vatLogDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxLogService()",
														closeService        : function(){kweInitial();},
														loadSuccessAfter    : "pageLoadLogSuccess()",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "4"
														});
}


function openSoDetail(){
    var nItemLine = vat.item.getGridLine();
    var vSalesOrderId = vat.item.getGridValueByName("salesOrderId", nItemLine);
    if("" ==  vSalesOrderId){
    	alert("無單據代號")
    }else{
	    var vBrandCode = vat.item.getValueByName("#F.brandCode");
	    var vSalesOrderDate = vat.item.getGridValueByName("salesOrderDate", nItemLine);
	    var vPosMachineCode = vat.item.getGridValueByName("posMachineCode", nItemLine);
	    var vCustomerPoNo =  vat.item.getGridValueByName("customerPoNo", nItemLine).substring(0, 10) ;
		var returnData = window.showModalDialog(
			"So_Delivery:searchSoItem:20101014.page"+
			"?salesOrderId="  + vSalesOrderId +"&"+
			"brandCode=" + vBrandCode +"&"+
			"salesOrderDate=" + vSalesOrderDate +"&"+
			"posMachineCode=" + vPosMachineCode +"&"+
			"customerPoNo="   + vCustomerPoNo 
			,"",
			"dialogHeight:500px;dialogWidth:700px;dialogTop:100px;dialogLeft:100px;");
	}
}

function kweInitial(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther.deliveryDate = "";
	 vat.bean().vatBeanOther.passportNo = "";
     vat.bean.init(function(){
		return "process_object_name=soDeliveryService&process_object_method_name=executeInitial";
     },{other: true});
//alert('deliveryDate:'+vat.bean().vatBeanOther.deliveryDate);
     vat.item.SelectBind(vat.bean("allOrderTypes")         ,{ itemName : "#F.orderTypeCode" });
     vat.item.SelectBind(vat.bean("allSalesFaultReasons")  ,{ itemName : "#F.salesFaultReason" });
     vat.item.SelectBind(vat.bean("allDeliverFaultReasons"),{ itemName : "#F.deliverFaultReason" });
     vat.item.SelectBind(vat.bean("allItemCategories")     ,{ itemName : "#F.itemCategory" });
     //vat.item.SelectBind(vat.bean("allFlightAreas")        ,{ itemName : "#F.flightArea" });
     vat.item.SelectBind(vat.bean("allStoreAreas")         ,{ itemName : "#F.storeArea" });
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
	 if(typeof vat.bean().vatBeanOther.processType != 'undefined'){
	 	vat.item.setValueByName("#F.processType",vat.bean().vatBeanOther.processType);
	 }
     vat.item.setValueByName("#F.updateFlag","N");
     vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     vat.block.pageDataLoad(vnB_Log,  getLastPage(vnB_Log));
	 doFormAccessControl();
	 
	 vat.form.item.setFocus( "#F.searchNo" );
	 
     if (vat.item.getValueByName("#F.status")=="W_CREATE" && vat.item.getValueByName("#F.storageCode")=="")
     {
        vat.item.setValueByName("#F.storageCode","00000");
     }
  }
}

function loadBeforeAjxService(){
	var processString = "process_object_name=soDeliveryService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
	                    "&shopCode=" + vat.item.getValueByName("#F.shopCode") ;
	//alert("After loadBeforeAjxService");
	return processString;
}

function loadBeforeAjxLogService(){
	var processString = "process_object_name=soDeliveryService&process_object_method_name=getAJAXPageLogData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
	                    "&shopCode=" + vat.item.getValueByName("#F.shopCode") ;
	//alert("After loadBeforeAjxService");
	return processString;
}

function doFormAccessControl(){
    var allProcess = null;
    var vsFormId = vat.item.getValueByName("#F.headId");
    var vsStatus = vat.item.getValueByName("#F.status");
    vat.item.setStyleByName("#G.lock"        , "display", "none");
    vat.item.setStyleByName("#G.unlock"      , "display", "none");
    var userType			  = document.forms[0]["#userType"      ].value;
   
    if(userType=="SERVICE_SEARCH_SODEL"&&vat.bean().vatBeanOther.groupCode=="SERVICE_SEARCH_SODEL"){
    //alert("預設全縣");
    
	
	vat.item.setAttributeByName("#F.cs_Note"    , "readOnly", false);
	            
	
    }else{
    
  //vat.item.setStyleByName("#F.processType" , "display", "inline");
  //vat.item.setStyleByName("#B.import"      , "display", "inline");
  //vat.item.setStyleByName("#B.print"       , "display", "none");	
  //vat.item.setStyleByName("#B.findDelivery", "display", "none");	
   	
	vat.item.setStyleByName("#B.search"      , "display", "none");	
	vat.item.setStyleByName("#B.addBlacklist", "display", "none");
	vat.item.setStyleByName("#B.edit"        , "display", "none");
	vat.item.setStyleByName("#B.unedit"      , "display", "none");
	vat.item.setStyleByName("#B.new"         , "display", "none");
	vat.item.setStyleByName("#B.unnew"       , "display", "none");
	vat.item.setStyleByName("#B.submit"      , "display", "none");
	vat.item.setStyleByName("#B.createmark"  , "display", "none");
	vat.item.setStyleByName("#B.processType" , "display", "none");
	vat.item.setStyleByName("#B.close"       , "display", "none");
	vat.item.setStyleByName("#B.void"        , "display", "none");
	//vat.item.setStyleByName("#B.cancel"      , "display", "none");
	vat.item.setStyleByName("#B.return"      , "display", "none");
	vat.item.setStyleByName("#B.wReturn"     , "display", "none");
	vat.item.setStyleByName("#B.refund"      , "display", "none");
	vat.item.setStyleByName("#B.export"      , "display", "none");
	vat.item.setStyleByName("#F.reportList"  , "display", "none");
	vat.item.setStyleByName("#B.unusual"     , "display", "none");
	
	vat.item.setStyleByName("#B.greenPassportBall", "display", "none");
	vat.item.setStyleByName("#B.blackPassportBall", "display", "none");
	vat.item.setStyleByName("#B.greenTelBall"     , "display", "none");
	vat.item.setStyleByName("#B.blackTelBall"     , "display", "none");
	vat.item.setStyleByName("#B.blacklist"        , "display", "none");
	vat.item.setStyleByName("#B.print"            , "display", "none");
	vat.item.setAttributeByName("#F.searchNo"    , "readOnly", false);
	vat.item.setAttributeByName("#F.searchStatus", "readOnly", false);
	vat.item.setAttributeByName("vatBlock_Head"  , "readOnly", true);
	vat.item.setAttributeByName("vatBlock_Master", "readOnly", true);
	vat.item.setAttributeByName("vatDetailDiv"   , "readOnly", true);
	vat.item.setAttributeByName("vatLogDiv"      , "readOnly", true);
	
	
	
	if (vat.bean().vatBeanOther.loginProcessType=="SEARCH"){
	   vat.item.setStyleByName("#B.exit", "display", "inline");
       vat.item.setStyleByName("#B.print", "display", "inline");
       vat.item.setStyleByName("#B.message", "display", "none");
	}else if(vsFormId == 0 ||vsFormId == null ){
		vat.item.setStyleByName("#B.search"      , "display", "inline");	
		if(vat.bean().vatBeanOther.processMode=="AUTO"){
		 	vat.item.setStyleByName("#B.findDelivery", "display", "none");
		}else
			vat.item.setStyleByName("#B.findDelivery", "display", "inline");
		vat.item.setAttributeByName("#F.processType" , "readOnly", "true");
		vat.item.setAttributeByName("#F.processMode" , "readOnly", "true");	
	}else{
		if("W_DELIVERY" == vsStatus)
			vat.item.setStyleByName("#B.print"            , "display", "inline");
	    if("Y" == vat.item.getValueByName("#F.updateFlag")){
	    	if("W_PICK"  == vsStatus||"W_CREATE"   == vsStatus||"W_DELIVERY" == vsStatus||"W_RETURN" == vsStatus){//待銷退可編輯
			  if(userType!="SERVICE_SEARCH_SODEL"){
				vat.item.setAttributeByName("#F.storeArea"   , "readOnly", true);
				vat.item.setAttributeByName("#F.customerCode", "readOnly", false);
				vat.item.setAttributeByName("#F.customerName", "readOnly", false);
				vat.item.setAttributeByName("#F.contactInfo" , "readOnly", false);
				vat.item.setAttributeByName("#F.passportNo"  , "readOnly", false);
				vat.item.setAttributeByName("#F.flightNo"    , "readOnly", false);
				vat.item.setAttributeByName("#F.flightDate"  , "readOnly", false);
				//vat.item.setAttributeByName("#F.flightArea"  , "readOnly", false);
				vat.item.setAttributeByName("#F.scheduleDeliveryDate", "readOnly", false);
				//vat.item.setAttributeByName("#F.shopCode"    , "readOnly", false);
				vat.item.setAttributeByName("#F.bagCounts1"  , "readOnly", false);
				vat.item.setAttributeByName("#F.bagCounts2"  , "readOnly", false);
				vat.item.setAttributeByName("#F.bagCounts3"  , "readOnly", false);
				vat.item.setAttributeByName("#F.bagCounts4"  , "readOnly", false);
				vat.item.setAttributeByName("#F.bagCounts5"  , "readOnly", false);
				vat.item.setAttributeByName("#F.bagCounts6"  , "readOnly", false);
				vat.item.setAttributeByName("#F.bagCounts7"  , "readOnly", false);
				vat.item.setAttributeByName("#F.expiryDate"  , "readOnly", false);
				vat.item.setAttributeByName("#F.affidavit"  , "readOnly", false);
			    vat.item.setAttributeByName("#F.bagBarCode"  , "readOnly", false);
				vat.item.setAttributeByName("#F.remark1"     , "readOnly", false);
				vat.item.setAttributeByName("#F.remark2"     , "readOnly", false);
				//vat.item.setAttributeByName("#F.breakable"   , "readOnly", false);
				//vat.item.setAttributeByName("#F.valuable"    , "readOnly", false);
				vat.item.setAttributeByName("#F.searchNo"    , "readOnly", true);
				vat.item.setAttributeByName("#F.searchStatus", "readOnly", true);
				vat.item.setAttributeByName("#F.expiryReturnNo"  , "readOnly", false);
				vat.item.setAttributeByName("#F.expiryReturnMemo"  , "readOnly", false);
				//steve
				vat.item.setAttributeByName("#F.oriFlight"  , "readOnly", false);
				vat.item.setAttributeByName("#F.oriDate"  , "readOnly", false);
				vat.item.setAttributeByName("#F.sodelUpdateHeadCode"  , "readOnly", false);
				vat.item.setAttributeByName("#F.updateContent"  , "readOnly", false);
				vat.item.setAttributeByName("#F.contactInfo_cs"  , "readOnly", false);
				vat.item.setAttributeByName("#L.updateType"  , "readOnly", false);
				vat.item.setAttributeByName("#F.contactOvertime"  , "readOnly", false);
				//steve
			  }else{
			    if("W_PICK"  == vsStatus||"W_CREATE"   == vsStatus){
			       //alert("W_PICK");
			       vat.item.setAttributeByName("#F.cs_Note"  , "readOnly", true);
			    }else{
			       
			       vat.item.setAttributeByName("#F.cs_Note"  , "readOnly", false);  //如果是create
			    }
			  }	
			}
			 if(userType=="SERVICE_SEARCH_SODEL"){
			    if("W_PICK"  == vsStatus||"W_CREATE"   == vsStatus){
			       vat.item.setAttributeByName("#F.cs_Note"  , "readOnly", true);
			    }else{
			       vat.item.setAttributeByName("#F.cs_Note"  , "readOnly", false);
			    }
			 }else{
			vat.item.setAttributeByName("#F.salesFaultReason", "readOnly", false);
		    vat.item.setAttributeByName("#F.deliverFaultReason"  , "readOnly", false);	
			}
			if("W_PICK"  == vsStatus){
			    vat.item.setStyleByName("#B.void"       , "display", "inline");
			    //vat.item.setStyleByName("#B.cancel"     , "display", "inline");
	        }else if("W_CREATE"  == vsStatus){
	        	vat.item.setStyleByName("#B.unnew"       , "display", "inline");
	        	vat.item.setStyleByName("#B.submit"      , "display", "inline");
	        	vat.item.setStyleByName("#B.unusual"     , "display", "none");
	        	
	        	//if ("00000"==vat.item.getValueByName("#F.storageCode") || ""==vat.item.getValueByName("#F.storageCode"))
	        	//{
	        	//	document.forms[0]["#F.storageCode"].style.display="none";
	        	//	document.forms[0]["#F.storageCodeRewrite"].style.display="inline";
	        	//	vat.item.setAttributeByName("#F.storageCodeRewrite"  , "readOnly", false);
	        	//}
			}else if("W_DELIVERY"  == vsStatus||"CLOSE"  == vsStatus||"VOID"   == vsStatus|| "RETURN" == vsStatus||
			         "CANCEL" == vsStatus||"W_RETURN" == vsStatus||"REFUND" == vsStatus){
	        	vat.item.setStyleByName("#B.unedit"      , "display", "inline");
	        	vat.item.setStyleByName("#B.submit"      , "display", "inline");
			}
		}else{
		 if(vat.bean().vatBeanOther.processMode !="" && vat.bean().vatBeanOther.loginProcessType!=""){   
			vat.item.setStyleByName("#B.processType"      , "display", "inline");
		    vat.item.setAttributeByName("#F.processType" , "readOnly", "true");
		    vat.item.setAttributeByName("#F.processMode" , "readOnly", "true");
		 }
		 if(vat.bean().vatBeanOther.processMode=="AUTO")
		 	vat.item.setStyleByName("#B.findDelivery", "display", "none");	
		 		
			vat.item.setStyleByName("#B.search"      , "display", "inline");
			vat.item.setStyleByName("#B.addBlacklist", "display", "inline");
			
			
			if(userType=="SERVICE_SEARCH_SODEL"){
			alert("SERVICE_SEARCH_SODEL");
			   vat.item.setStyleByName("#B.edit"       , "display", "inline");
			   vat.item.setStyleByName("#B.search"       , "display", "inline");
			   vat.item.setStyleByName("#B.exit"       , "display", "inline");
			   vat.item.setStyleByName("#B.createmark" , "display", "none");
			    vat.item.setStyleByName("#B.void"       , "display", "none");
			    vat.item.setStyleByName("#B.new"        , "display", "none");
			    vat.item.setStyleByName("#B.close"       , "display", "none");
			    vat.item.setStyleByName("#B.unusual"     , "display", "none");
			    
			}else{
			if("W_PICK" == vsStatus){
			    vat.item.setStyleByName("#B.createmark" , "display", "inline");
			    vat.item.setStyleByName("#B.void"       , "display", "inline");
			    //vat.item.setStyleByName("#B.cancel"     , "display", "inline");
		    }else if("W_CREATE" == vsStatus){
		        vat.item.setStyleByName("#B.new"        , "display", "inline");
			    vat.item.setStyleByName("#B.void"       , "display", "inline");
			    //vat.item.setStyleByName("#B.cancel"     , "display", "inline");
			    
			    if ("XXXXX"==vat.item.getValueByName("#F.storageCode"))
			    	vat.item.setStyleByName("#B.unusual"     , "display", "none");
			    else
			    	vat.item.setStyleByName("#B.unusual"     , "display", "inline");
			    
            	//document.forms[0]["#F.storageCode"].style.display="inline";
       			//document.forms[0]["#F.storageCodeRewrite"].style.display="none";
       			//vat.item.setValueByName("#F.storageCodeRewrite","00000");
			}else if("W_DELIVERY" == vsStatus){
		        vat.item.setStyleByName("#B.edit"       , "display", "inline");
		        vat.item.setStyleByName("#B.close"      , "display", "inline");
			    vat.item.setStyleByName("#B.void"       , "display", "inline");
			    //vat.item.setStyleByName("#B.cancel"     , "display", "inline");
			    vat.item.setStyleByName("#B.return"     , "display", "inline");
			    vat.item.setStyleByName("#B.wReturn"    , "display", "inline");
			}else if("CLOSE"  == vsStatus||"VOID"   == vsStatus||"CANCEL" == vsStatus||"RETURN" == vsStatus||"REFUND" == vsStatus){
		        vat.item.setStyleByName("#B.edit"       , "display", "inline");
			}else if("W_RETURN" == vsStatus){
				vat.item.setStyleByName("#B.edit"       , "display", "inline");
			    vat.item.setStyleByName("#B.refund"     , "display", "inline");
			    vat.item.setStyleByName("#B.close"     , "display", "inline");     //待銷退加入結案steve
		    
		    }
		    }
			vat.item.setAttributeByName("#F.processType"     , "readOnly", "false");
		    vat.item.setAttributeByName("#F.processMode" , "readOnly", "false");
		    vat.item.setStyleByName("#B.process"     , "display", "inline");
		}
	}
	
	if (vat.bean().vatBeanOther.loginProcessType!="SEARCH"){
		if("Y" == vat.item.getValueByName("#F.isPassportBlackList")){
		    vat.item.setStyleByName("#B.blackPassportBall"   , "display", "inline");
		}else{
			if(vsFormId != 0 && vsFormId != null  ){
				vat.item.setStyleByName("#B.addBlacklist"      , "display", "inline");
				vat.item.setStyleByName("#B.greenPassportBall" , "display", "inline");
			}
		}
		if("Y" == vat.item.getValueByName("#F.isTelBlackList")){
		    vat.item.setStyleByName("#B.blackTelBall"   , "display", "inline");
		}else{
			if(vsFormId != 0 && vsFormId != null  ){
			   if("SERVICE_SEARCH_SODEL"==document.forms[0]["#userType"].value){
				  //alert("service");
				  vat.item.setStyleByName("#B.addBlacklist"  , "display", "none");
			    }else{
			      //alert("Noservice");
			      vat.item.setStyleByName("#B.addBlacklist"  , "display", "inline");
			    }
			    vat.item.setStyleByName("#B.greenTelBall" , "display", "inline");
			}
		}
	}
	//alert(	vat.item.getValueByName("#F.status"));	
	if(	vat.bean().vatBeanOther.loginProcessType == ""){	
		if("W_PICK" == vsStatus)	
			 allProcess = [["","",true],
	                      ["建檔註記","作廢","取消"],
	                      ["W_CREATE","VOID","CANCEL"]];	
		else if("W_CREATE" == vsStatus)	
			 allProcess = [["","",true],
	                      ["作廢","取消"],
	                      ["VOID","CANCEL"]];
		else if("W_DELIVERY" == vsStatus)	
		     allProcess = [["","",true],
	                      ["結案","作廢","退貨","取消","待銷退","已退款"],
	                      ["CLOSE","VOID","RETURN","CANCEL","W_RETURN","REFUND"]];	
	    else
			allProcess="";
	}else{
		allProcess = [["","",false],
	                      ["建檔註記","結案","作廢","退貨","取消","待銷退","已退款"],
	                      ["W_CREATE","CLOSE","VOID","RETURN","CANCEL","W_RETURN","REFUND"]];	
	}
	vat.item.SelectBind(allProcess , { itemName : "#F.processType" });
	if(	vat.bean().vatBeanOther.loginProcessType == ""){	
		if(typeof vat.bean().vatBeanOther.processType != 'undefined')
			vat.item.setValueByName("#F.processType",vat.bean().vatBeanOther.processType);
    }else{
    	vat.item.setValueByName("#F.processType",vat.bean().vatBeanOther.loginProcessType);
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
	    if(""!=vat.item.getValueByName("#F.employeeCode") && ""!=vat.item.getValueByName("#F.employeeName")){
			vat.item.setValueByName("#F.updateFlag","Y");
		}else{
			alert("請輸入修改人員工號");
		}	
	}
	doFormAccessControl();
}

function changeNew(){
	if("Y" == vat.item.getValueByName("#F.updateFlag")) {
		
		vat.item.setValueByName("#F.updateFlag","N");
	 
		//changeStatus("W_CREATE");
	}else{
	    if(""!=vat.item.getValueByName("#F.employeeCode") && ""!=vat.item.getValueByName("#F.employeeName")){
			vat.item.setValueByName("#F.updateFlag","Y");
			//changeStatus("W_DELIVERY");
		}else{
			alert("請輸入修改人員工號");
		}	
	}
	doFormAccessControl();
}

function searchDelivery(triggeredBy){

    vat.item.setValueByName("#F.searchNo",vat.item.getValueByName("#F.searchNo").toUpperCase());
    var vsSearchNo     = vat.item.getValueByName("#F.searchNo");
    var vsSearchStatus = vat.item.getValueByName("#F.searchStatus");
    var vsAllow = false;
	if(vsSearchNo.length > 0 ){
	    vsAllow = true;
	}else{
	    vsAllow = false;
		if (triggeredBy == "searchButton")
			alert("查詢單號為空白，請重新輸入!");
	}
   	if(vsSearchNo.substring(0, 3)!="DKP" & vsSearchNo.substring(0, 3)!="DZN" & vsSearchNo.substring(0, 2)!="TD"){
   		vat.item.setValueByName("#F.searchNo",vsSearchNo.substring(0,10));
   		vsSearchNo=vat.item.getValueByName("#F.searchNo");
   		//alert(vsSearchNo);
   	}
	if(vsAllow){
  		vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
  		vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
  		vat.bean().vatBeanOther.searchNo = vsSearchNo;
		vat.bean().vatBeanOther.searchStatus = vsSearchStatus;
		vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=findHeadIdBySearchKey";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("0" != vat.bean().vatBeanOther.formId ){
		         
			     	vat.item.setValueByName("#F.headId"        , vat.bean().vatBeanOther.formId);
			     	if("AUTO" == vat.item.getValueByName("#F.processMode") && "" != vat.item.getValueByName("#F.processType")){
			     		if (triggeredBy == "searchButton"){	
			     			
			     			kweInitial();
			     		}else{
			     		    
			     			doProcess();
			     		}	
			     	}else{
			     	
			     	vat.item.setValueByName("#F.pDeliveryDate","");
			     	vat.item.setValueByName("#F.deliveryDate"    , "");
			     	    
			     	    kweInitial();
			     	}
			     }else{
			     	alert("查無此單據("+vsSearchNo+")資料");
			     }
		     }
		});
	
	}
	
	vat.form.item.setFocus( "#F.searchNo" );
}

function changeShopCode(){
    var vsShopCode  = vat.item.getValueByName("#F.shopCode");
    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
    var vsAllow = false;
	if(vsShopCode.length >= 0 ){
	    vsAllow = true;
	}else{
	    vsAllow = false;
	}
	if(vsAllow){
  		vat.bean().vatBeanOther.brandCode = vsBrandCode;
  		vat.bean().vatBeanOther.shopCode = vsShopCode;
		vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=findShopByCode";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("" != vat.bean().vatBeanOther.shopName ){
			     	vat.item.setValueByName("#F.shopName"        , vat.bean().vatBeanOther.shopName);
			     	vat.item.setValueByName("#F.itemCategory"    , vat.bean().vatBeanOther.itemCategory);
			     }else{
			     	alert("查無此店別("+vsShopCode+")資料");
			     }
		     }
		});
	
	}
}

function changeStatus(vsStatus){
    var vsFormId  = vat.item.getValueByName("#F.headId");
    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
    var vsAllow = false;
	if(vsFormId != "" ){
	    vsAllow = true;
	}else{
	    vsAllow = false;
	    alert("請先查詢出提貨單後，再執行此作業");
	}
	if("" == vat.item.getValueByName("#F.employeeCode")){
		alert("請輸入執行人員工號");
		vat.form.item.setFocus( "#F.employeeCode" );
		vsAllow = false;
	}
	if(("CLOSE"==vsStatus ||"REFUND"==vsStatus) && "" == vat.item.getValueByName("#F.pDeliveryDate")){
		alert("請輸入提貨日");
		vat.form.item.setFocus( "#F.pDeliveryDate" );
		vsAllow = false;
	}
	if(("RETURN"==vsStatus ||"REFUND"==vsStatus) && "" == vat.item.getValueByName("#F.pDeliveryDate")){
		alert("請輸入提貨日");
		vat.form.item.setFocus( "#F.pDeliveryDate" );
		vsAllow = false;//Henry
	}
	if(("W_RETURN"==vsStatus ||"REFUND"==vsStatus )&& "" == vat.item.getValueByName("#F.expiryReturnNo")){
		alert("請輸入逾期銷退單號");
		vat.form.item.setFocus( "#F.expiryReturnNo" );
		vsAllow = false;
	}

	
	if (vsStatus == "VOID" && vsAllow)
	    vsAllow = confirm("是否確認「作廢」?");
	if (vsStatus == "RETURN" && vsAllow)
	    vsAllow = confirm("是否確認「退貨」?");    
	if (vsStatus == "REFUND" && vsAllow)
	    vsAllow = confirm("是否確認「已退款」?");    
	if(vsAllow){
  		vat.bean().vatBeanOther.formId = vsFormId;
  		vat.bean().vatBeanOther.status = vsStatus;
  		vat.bean().vatBeanOther.deliveryNo      = vat.item.getValueByName("#F.orderNo");
  		vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
  		vat.bean().vatBeanOther.processType     = vat.item.getValueByName("#F.processType");
  		vat.bean().vatBeanOther.pDeliveryDate   = vat.item.getValueByName("#F.pDeliveryDate");
  		vat.bean().vatBeanOther.mode ="MANUAL";
		vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=updateStatusByHeadId";},  {other:true,picker:false,
		     funcSuccess: function() {
		     	if("SUCCESS"== vat.bean().vatBeanOther.logLevel){
		     	
				     vat.item.setValueByName("#F.status"        , vat.bean().vatBeanOther.status);
				     vat.item.setValueByName("#F.statusName"    , vat.bean().vatBeanOther.statusName);
				     vat.item.setValueByName("#F.deliveryDate"    , vat.bean().vatBeanOther.deliveryDate);
				     vat.item.setValueByName("#F.storageCode"        , "");
				     if("W_CREATE"==vsStatus){
				     
				     	vat.item.setValueByName("#F.storeArea"     , vat.bean().vatBeanOther.storeArea);
				     	vat.item.setValueByName("#F.storeAreaName" , vat.bean().vatBeanOther.storeAreaName);
				     }else if("CLOSE"==vsStatus){
				   
				     	vat.item.setValueByName("#F.storageCode"        , vat.bean().vatBeanOther.storageCode);
				     }
				     doFormAccessControl();
					 vat.block.pageDataLoad(vnB_Log,  getLastPage(vnB_Log));
				}
		     }
		});
	}
}
function changeLockFlag(vsLockFlag){
    var vsFormId  = vat.item.getValueByName("#F.headId");
    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
    var vsAllow = false;

	if(vsFormId != "" ){
	    vsAllow = true;
	}else{
	    vsAllow = false;
	    alert("請先查詢出提貨單後，再執行此作業");
	}
    if("" == vat.item.getValueByName("#F.employeeCode") &&
       "" == vat.item.getValueByName("#F.employeeName")){
		alert("請輸入執行人員工號");
		vat.form.item.setFocus( "#F.employeeCode" );
		vsAllow = false;
	}
	if(vsAllow){
  		vat.bean().vatBeanOther.formId = vsFormId;
  		vat.bean().vatBeanOther.lockFlag = vsLockFlag;
  		vat.bean().vatBeanOther.executeEmployee =  vat.item.getValueByName("#F.employeeCode");
		vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=updateLockFlag";},  {other:true,picker:false,
		     funcSuccess: function() {
			     vat.item.setValueByName("#F.lockFlag"        , vat.bean().vatBeanOther.lockFlag);
			     vat.item.setValueByName("#F.lockFlagName"    , vat.bean().vatBeanOther.lockFlagName);
			     doFormAccessControl();			 
				 vat.block.pageDataLoad(vnB_Log,  getLastPage(vnB_Log));
		     }
		});
	
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
}

function doPassBlackData(x){

  var suffix = "";
  var vBrandCode = vat.item.getValueByName("#F.brandCode").replace(/^\s+|\s+$/, '').toUpperCase();
  var vName =vat.item.getValueByName("#F.customerName");
  var vPassportNo = vat.item.getValueByName("#F.passportNo").replace(/^\s+|\s+$/, '').toUpperCase();
  //alert(vName);
  suffix += "&brandCode="+escape(vBrandCode)+
            "&customerName="+vat.utils.escape(vName)+
            "&passportNo="+escape(vPassportNo);

  return suffix;
}

function doAfterPickerBlackProcess(){
}
function getEmployeeInfo() {
    
    if ("" !=vat.item.getValueByName("#F.employeeCode")) {
    
        vat.item.setValueByName("#F.employeeCode",vat.item.getValueByName("#F.employeeCode").toUpperCase());

        vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
        vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=getEmployeeInfo";},  {other:true,picker:false,
		     funcSuccess: function() {
		        
		         if("" != vat.bean().vatBeanOther.employeeName){
		            vat.item.setValueByName("#F.groupCode", vat.bean().vatBeanOther.groupCode);
                    vat.item.setValueByName("#F.employeeName", vat.bean().vatBeanOther.employeeName);
                    vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
                    vat.bean().vatBeanOther.executeEmployeeName=vat.item.getValueByName("#F.employeeName");
                    //alert(vat.item.getValueByName("#F.groupCode"));
                   
                    
			     }else if(checkVal(vat.bean().vatBeanOther.employeeName)){
			           vat.item.setValueByName("#F.employeeName","");
			     	vat.item.setValueByName("#F.employeeCode","");
			     	
			     	alert("員工代號錯誤，清空後, 請重新輸入！");
			     	
	 				vat.form.item.setFocus( "#F.employeeCode" );
			     }else if("undefined" != vat.bean().vatBeanOther.employeeName){
			        vat.item.setValueByName("#F.employeeName","");
			     	vat.item.setValueByName("#F.employeeCode","");
			     	
			     	alert("員工代號錯誤，清空後, 請重新輸入！");
			     	
	 				vat.form.item.setFocus( "#F.employeeCode" );
			     }else{
			     　　	vat.item.setValueByName("#F.employeeName","");
			     	vat.item.setValueByName("#F.employeeCode","");
			     	
			     	alert("員工代號錯誤，清空後, 請重新輸入！");
			     	
	 				vat.form.item.setFocus( "#F.employeeCode" );			     	
			     }
		     }
		});
    }else{
    	vat.item.setValueByName("#F.employeeName","");
    }
}

/*
 * 輸入字串後，如果是數字或a-z ,A-Z，則回傳 true
 */
function checkVal( str ) {
    var regExp = /^[\d|a-zA-Z]+$/;
    if (regExp.test(str))
        return true;
    else
        return false;
}



function changeFlightDate(){
 	vat.item.setValueByName("#F.scheduleDeliveryDate",parseDate("#F.flightDate"));
}

function parseDate(field, checkType){
 	vat.bean().vatBeanOther.parseDate = vat.item.getValueByName(field);
 	vat.bean().vatBeanOther.checkType = checkType;
	vat.block.submit(function(){return "process_object_name=soDeliveryService"+
	            "&process_object_method_name=parseDate";},  {other:true,picker:false,
	     funcSuccess: function() {
	     	if(""!=vat.bean().vatBeanOther.afterParseDate){
	     		vat.item.setValueByName(field,vat.bean().vatBeanOther.afterParseDate);
	     		if(field == "#F.flightDate"){
	     			vat.item.setValueByName("#F.scheduleDeliveryDate",vat.bean().vatBeanOther.afterParseDate);
	     		}
			}
	     }
	});
	
}

function changeScheduleDeliveryDate(){
 	vat.bean().vatBeanOther.parseFlightDate = vat.item.getValueByName("#F.scheduleDeliveryDate");
	vat.block.submit(function(){return "process_object_name=soDeliveryService"+
	            "&process_object_method_name=parseDate";},  {other:true,picker:false,
	     funcSuccess: function() {
			vat.item.setValueByName("#F.scheduleDeliveryDate",vat.bean().vatBeanOther.parseFlightDate);
	     }
	});
}


function doSubmit(formAction){
	var vsAllowSubmit         = true;
	var alertMessage          ="是否確定送出?";
	var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');;
    var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');;
    var employeeCode          = vat.bean().vatBeanOther.loginEmployeeCode.replace(/^\s+|\s+$/, '');;
    var orderNoPrefix         = vat.item.getValueByName("#F.orderNo").substring(0,3);
    var processString 	      ="";
    totalBagCount();
	
	if(confirm(alertMessage)){
	    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
		var vsStatus = vat.item.getValueByName("#F.status");
		var vsLockFlag = vat.item.getValueByName("#F.lockFlag");
		var vsAllowSubmit = true;
		if("LOCK" == vsLockFlag){
		    alert("此單據已鎖定，不可修改資料");
			vsAllowSubmit = false;
		}	
		if("" == vat.item.getValueByName("#F.employeeCode")){
			alert("請輸入修改人員工號");
			vat.form.item.setFocus( "#F.employeeCode" );
			vsAllowSubmit = false;
		}
		if("" == vat.item.getValueByName("#F.customerName")){
			alert("請輸入客戶姓名");
			vat.form.item.setFocus( "#F.customerName" );
			vsAllowSubmit = false;
		}
		if("" == vat.item.getValueByName("#F.contactInfo")){
			alert("請輸入連絡電話");
			vat.form.item.setFocus( "#F.contactInfo" );
			vsAllowSubmit = false;
		}
			
		if("" == vat.item.getValueByName("#F.passportNo")){
			alert("請輸入護照號碼");
			vat.form.item.setFocus( "#F.passportNo" );
			vsAllowSubmit = false;
		}	
		
		if("" == vat.item.getValueByName("#F.flightDate")){
			alert("請輸入回程日期");
			vat.form.item.setFocus( "#F.flightDate" );
			vsAllowSubmit = false;
		}	
		if("" == vat.item.getValueByName("#F.flightNo")){
			alert("請輸入回程班機");
			vat.form.item.setFocus( "#F.flightNo" );
			vsAllowSubmit = false;
		}
		if("" == vat.item.getValueByName("#F.scheduleDeliveryDate")){
			alert("請輸入預計提領日");
			vat.form.item.setFocus( "#F.scheduleDeliveryDate" );
			vsAllowSubmit = false;
		}
		if("" == vat.item.getValueByName("#F.totalBagCounts") || 0 == vat.item.getValueByName("#F.totalBagCounts")){
			alert("總提貨袋數不可為「0」");
			vsAllowSubmit = false;
		}
		
		/*if("" == vat.item.getValueByName("#F.deliveryDate") ){
			alert("結案日期不可為空白");
			vsAllowSubmit = false;
		}*/
		
		//if(6!=vat.item.getValueByName("#F.soDelUpdateHeadCode").length){
		  //  alert("請確認更正單號 , 更正單編號須為6碼");
		   // vsAllowSubmit = false;
        //}
		

    	if(vsAllowSubmit){
    		  if("W_CREATE" == vsStatus){
    		  	vat.item.setValueByName("#F.storageCode" , vat.item.getValueByName("#F.storageCode"));}
    		  	
			  vat.item.setValueByName("#F.updateFlag","N");
			// changeStatus("W_DELIVERY");
			  vat.bean().vatBeanOther.status          = 'W_DELIVERY';
			  vat.bean().vatBeanOther.formAction      = formAction;
  	  	      vat.bean().vatBeanOther.executeEmployee =  vat.item.getValueByName("#F.employeeCode");
  	  	      vat.bean().vatBeanOther.userType =  document.forms[0]["#userType"      ].value;
	          processString = "process_object_name=soDeliveryAction&process_object_method_name=performTransaction";
			  vat.block.submit(function(){return processString;},{
		                    bind:true, link:true, other:true,
		                    funcSuccess:function(){
				        	//	vat.block.pageRefresh(vnB_Detail);
				        	if("" == vat.bean().vatBeanOther.employeeName ){
				        	    vat.item.setValueByName("#F.employeeName","");
			     	            vat.item.setValueByName("#F.employeeCode","");
			     	            vat.form.item.setFocus( "#F.employeeCode" );	
				        	}
				        	
				        	}}
			  );
        }
	}
}

function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	processString = "process_object_name=soDeliveryService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&status=" + vat.item.getValueByName("#F.status");


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
        "&processObjectName=soDeliveryService" +
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
     //vat.item.SelectBind(vat.bean("allFlightAreas")        ,{ itemName : "#F.flightArea" });
     vat.item.SelectBind(vat.bean("allStoreAreas")         ,{ itemName : "#F.storeArea" });
     //vat.item.SelectBind(vat.bean("allReportList"),{ itemName : "#F.reportList" });
     vat.item.bindAll();
     if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	 }
     vat.item.setValueByName("#F.updateFlag","N");
     vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
     vat.block.pageDataLoad(vnB_Log, vnCurrentPage = 1);
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
function checkBlackListPassport(){
	//alert("護照號碼:"+vat.item.getValueByName("#F.passportNo"));
    if("" != vat.item.getValueByName("#F.passportNo")){
   		 vat.bean().vatBeanOther.passportNo= vat.item.getValueByName("#F.passportNo");
         vat.block.submit(function(){return "process_object_name=soDeliveryBlackListService"+
		            "&process_object_method_name=isBlackListByPassportNo";},  {other:true,picker:false,
		 funcSuccess: function() {
		 		 vat.item.setValueByName("#F.isPassportBlackList", vat.bean().vatBeanOther.isBlackList);
			     doFormAccessControl();
		     }
		});
	}
}


function checkBlackListTel(){
	//alert("護照號碼:"+vat.item.getValueByName("#F.contactInfo"));
    if("" != vat.item.getValueByName("#F.contactInfo")){
   		 vat.bean().vatBeanOther.customerInfo= vat.item.getValueByName("#F.contactInfo");
         vat.block.submit(function(){return "process_object_name=soDeliveryBlackListService"+
		            "&process_object_method_name=isBlackListByTel";},  {other:true,picker:false,
		 funcSuccess: function() {
		 		 vat.item.setValueByName("#F.isTelBlackList", vat.bean().vatBeanOther.isTelBlackList);
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


function refreshForm(vsHeadId) {
    document.forms[0]["#formId"].value = vsHeadId;

    vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
    vat.bean().vatBeanOther.updateForm = document.forms[0]["#formId"].value == "" ? "N" : "Y";
    vat.block.submit(
    function () {
        return "process_object_name=soDeliveryService&process_object_method_name=executeInitial";
    }, {
        other: true,
        funcSuccess: function () {
            vat.item.bindAll();
            vat.block.pageRefresh(vnB_Detail);
            vat.block.pageRefresh(vnB_Log);
            doFormAccessControl();   
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

//列印畫面
function printView() {
	if (!window.print){alert("列印功能暫時停用，請按 Ctrl-P 來列印"); return;}
	window.print();
}
function MM_openBrWindow(theURL,winName,features) {
	window.open(theURL,winName,features);
}
//列印
function openReportWindow() {
    //vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
    //vat.bean().vatBeanOther.reportFunctionCode = vat.item.getValueByName("#F.reportList");
    //vat.bean().vatBeanOther.orderNo = vat.item.getValueByName("#F.orderNo");
    
    if (vat.item.getValueByName("#F.orderNo")!=""){
	    vat.bean().vatBeanOther.executeEmployeeCode= vat.item.getValueByName("#F.employeeCode");
	    vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.loginBrandCode");
	    vat.bean().vatBeanOther.reportFunctionCode = "T2_W00SO1004";//vat.item.getValueByName("#F.reportList");
	    vat.bean().vatBeanOther.orderNo = vat.item.getValueByName("#F.orderNo");
	    vat.bean().vatBeanOther.startOrderDate = "";//vat.item.getValueByName("#F.startOrderDate");
	    vat.bean().vatBeanOther.endOrderDate = "";//vat.item.getValueByName("#F.endOrderDate");
	    vat.bean().vatBeanOther.customerName = "";//vat.item.getValueByName("#F.customerName");
	    vat.bean().vatBeanOther.contactInfo = "";//vat.item.getValueByName("#F.contactInfo");
	    vat.bean().vatBeanOther.passportNo = "";//vat.item.getValueByName("#F.passportNo");
	    vat.bean().vatBeanOther.startDeliveryDate = "";//vat.item.getValueByName("#F.startDeliveryDate");
	    vat.bean().vatBeanOther.endDeliveryDate = "";//vat.item.getValueByName("#F.endDeliveryDate");
	    vat.bean().vatBeanOther.flightNo = "";//vat.item.getValueByName("#F.flightNo");
	    //vat.bean().vatBeanOther.flightArea = vat.item.getValueByName("#F.flightArea");
	    vat.bean().vatBeanOther.startFlightDate = "";//vat.item.getValueByName("#F.startFlightDate");
	    vat.bean().vatBeanOther.endFlightDate = "";//vat.item.getValueByName("#F.endFlightDate");
	    vat.bean().vatBeanOther.storeArea = "";//vat.item.getValueByName("#F.storeArea");
	    vat.bean().vatBeanOther.shopCode = "";//vat.item.getValueByName("#F.shopCode");
	    vat.bean().vatBeanOther.status = "";//vat.item.getValueByName("#F.status");
	    vat.bean().vatBeanOther.lockFlag = "";//vat.item.getValueByName("#F.lockFlag");
	    vat.bean().vatBeanOther.valuable = "";//vat.item.getValueByName("#F.valuable");
	    vat.bean().vatBeanOther.breakable = "";//vat.item.getValueByName("#F.breakable");
	    vat.bean().vatBeanOther.startScheduleDeliveryDate = "";//vat.item.getValueByName("#F.startScheduleDeliveryDate");
	    vat.bean().vatBeanOther.endScheduleDeliveryDate = "";//vat.item.getValueByName("#F.endScheduleDeliveryDate");
	    vat.bean().vatBeanOther.customerPoNo = "";//vat.item.getValueByName("#F.customerPoNo");
	    vat.bean().vatBeanOther.terminal = "";//vat.item.getValueByName("#F.terminal");
	    vat.bean().vatBeanOther.sortKey = "";//vat.item.getValueByName("#F.sortKey");
	    vat.bean().vatBeanOther.sortSeq = "";//vat.item.getValueByName("#F.sortSeq");
	    
	    
	    vat.block.submit(function () {
	        return "process_object_name=soDeliveryService" + "&process_object_method_name=getReportConfig";
	    }, {
	        other: true,
	        funcSuccess: function () {
	            //vat.item.setValueByName("#F.remark2", vat.bean().vatBeanOther.reportUrl);
	            eval(vat.bean().vatBeanOther.reportUrl);
	
	           
	        }
	    });
    }
    else{
    	alert("無單號可列印！");
    	vat.form.item.setFocus( "#F.searchNo" );
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

function doProcess(){
    //alert("doProcess");
	var vsProcess      = vat.item.getValueByName("#F.processType");
	vat.bean().vatBeanOther.processType= vsProcess;
	var vsEmployeeCode = vat.item.getValueByName("#F.employeeCode");
    var vFormId        = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');
    var vDeliveryDate  = vat.item.getValueByName("#F.pDeliveryDate");
    var vAllow=true;
    // alert(vat.item.getValueByName("#F.process"));
    if("" ==  vsProcess ){
	    vAllow = false;
		alert("未選擇要處理的項目，請重新選擇");
		vat.form.item.setFocus( "#F.processType" );	
	}	
    if("" ==  vsEmployeeCode ){
    	vAllow = false;
		alert("請輸入執行人員工號");
		vat.form.item.setFocus( "#F.employeeCode" );	
	}	
	if( vFormId =="" || vFormId == "0"){
		 vAllow = false;
		alert("查無此張單號，請重新查詢");
	}
	if( vsProcess =="CLOSE" && vDeliveryDate == ""){
		vAllow = false;
		alert("請輸入提領日期");		
		vat.form.item.setFocus( "#F.startDeliveryDate" );	
	}

	if(vAllow){
		vat.bean().vatBeanOther.executeEmployee= vsEmployeeCode;
		vat.bean().vatBeanOther.status= vsProcess;
		vat.bean().vatBeanOther.formId= vFormId;
		vat.bean().vatBeanOther.deliveryNo= vOrderNo;
		vat.bean().vatBeanOther.pDeliveryDate= vDeliveryDate;
		vat.bean().vatBeanOther.mode= vat.item.getValueByName("#F.processMode");
	    vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		                    "&process_object_method_name=updateStatusByHeadId" }, 
		                    {other: true, 
		                     funcSuccess: function() {
		                    	kweInitial();
		                    }
		 });
	}
}

function doUpdateStorageCode(){

	var vsAllowSubmit = true;
	
	if(confirm("是否設定異常儲位?")){
		if("" == vat.item.getValueByName("#F.employeeCode")){
			alert("請輸入修改人員工號");
			vat.form.item.setFocus( "#F.employeeCode" );
			vsAllowSubmit = false;
		}
		
		if (vsAllowSubmit){
			vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
			vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
		    vat.block.submit(
		    function () {
		        return "process_object_name=soDeliveryService&process_object_method_name=updateStorageCode";
		    }, {
		        other: true,
		        funcSuccess: function () {
					kweInitial();
		            doFormAccessControl();
		            alert("設定成功！");   
		        }
		    });
	    }
    }
}

