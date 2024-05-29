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
var vnB_Data = 1;
var vnB_Detail =2;
var vnB_Log=4;
function kweBlock(){
   if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

  	vat.bean().vatBeanOther =
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	     processMode        : document.forms[0]["#processMode" ].value,
  	     formId             : "",
  	     orderTypeCode      : vat.item.getValueByName("#F.orderTypeCode") == "" ? "DZN":vat.item.getValueByName("#F.orderTypeCode"),
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	 };
  }


  kweSearch();
}




function kweSearch(){
var allActions = [["","",false],
                 ["補單","修改","移除"],
                 ["CREATE","UPDATE","REMOVE"]];
	kweButtonLine();


	vat.block.create(vnB_Data, {
	id: "vatBlock_Data", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='5' style='border-right-width: 0px; border-left-width: 0px; border-bottom-width: 5px;  border-color: #ffffff;'",
	title:"", 
	rows:[
	 {row_style:"background-color: #990000", cols:[
	 {items:[{name:"#L.action"     , type:"LABEL", value:"<FONT SIZE=4><B>執行項目</B></FONT>"}], 
	          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
	 {items:[{name:"#F.action"     , type:"SELECT" , style:"font-size: 30px; size:2", init:allActions}], 
	          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
	 {items:[{name:"#L.employeeCode" , type:"LABEL", value:"<FONT SIZE=4><B>人員工號</B></FONT>"}], 
	          td:"style='background-color:#FFDF00; border-color:#FFDF00; '"},
 	 {items:[{name:"#F.employeeCode" , type:"TEXT" , bind:"employeeCode", size:4, style:"font-size: 20pt; height:40px",eChange:'getEmployeeInfo()'},
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>&nbsp&nbsp&nbsp;</font>"},
	 		 {name:"#F.employeeName" , type:"TEXT" , bind:"employeeName", size:4, style:"font-size: 20pt; height:40px",mode:"READONLY"},
	 		 {name:"#F.updateFlag"   , type:"TEXT" , bind:"updateFlag",mode:"HIDDEN"}], 
	 		  td:"style='background-color:#FFDF00; border-color:#FFDF00;'"}, 
     {items:[{name:"#B.eLock"        , type:"IMG"    , value:"鎖定"   ,  src:"./images/button_lock.gif"  , eClick:'changeUpdateFlag("LOCK")'},	
	 		 {name:"#B.uneLock"      , type:"IMG"    , value:"取消鎖定",  src:"./images/button_unlock.gif", eClick:'changeUpdateFlag("UNLOCK")'}],
	 		   td:"style='background-color:#FFDF00; border-color:#FFDF00;'"}]},
	 {row_style:"background-color: #990000", cols:[
	 {items:[{name:"#L.searchNo"     , type:"LABEL", value:"<FONT SIZE=4><B>提貨單號</B></FONT>"}], 
	          td:"style='background-color:#FFDF00; border-color:#FFDF00;'"},
	 {items:[{name:"#F.searchNo"     , type:"TEXT" , size:13, maxLen:20,  style:"font-size: 20pt; height:40px",  eChange:"searchDelivery()"},
	         {name:"#L.block"        , type:"LABEL", value:"　　"},	 	
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>說明:</font>"},
	 		 {name:"#F.searchDesc"   , type:"TEXT" , mode:"READONLY", size:6,  style:"background-color: #FFDF00;"},
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>&nbsp&nbsp&nbsp;狀態:</font>"},
	 		 {name:"#F.statusName"   , type:"TEXT" , mode:"READONLY", size:6,  style:"background-color: #FFDF00;"},
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>／</font>"},
	 		 {name:"#F.lockFlagName" , type:"TEXT" , mode:"READONLY", size:1,  style:"background-color: #FFDF00;"},
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>&nbsp&nbsp&nbsp;</font>"},
	 		 {name:"#F.headId"       , type:"TEXT" , mode:"READONLY"  , size:4,  style:"background-color: #FFDF00;"}],
	          td:"colSpan='3' style='background-color:#FFDF00; border-color:#FFDF00;'"}, 
     {items:[{name:"#B.dLock"        , type:"IMG"    , value:"鎖定"   ,  src:"./images/button_lock.gif"  , eClick:'changeUpdateFlag("LOCK")'},	
	 		 {name:"#B.undLock"      , type:"IMG"    , value:"取消鎖定",  src:"./images/button_unlock.gif", eClick:'changeUpdateFlag("UNLOCK")'}],
	 		   td:"style='background-color:#FFDF00; border-color:#FFDF00;'"}]},
	 {row_style:"background-color: #990000", cols:[
	 {items:[{name:"#L.invoiceNo"     , type:"LABEL", value:"<FONT SIZE=4><B>銷售單號</B></FONT>"}], 
	          td:"style='background-color:#FFDF00; border-color:#FFDF00; "},
	 {items:[{name:"#F.invoiceNo"    , type:"TEXT" , size:13, maxLen:10, style:"font-size: 20pt; height:40px", mask:"CCCCCCCCCC", eChange:"searchCustomerPoNo()"},
	         {name:"#L.block"        , type:"LABEL", value:"　　"},
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>說明:</font>"},
	 		 {name:"#F.invoiceDesc"  , type:"TEXT" , mode:"READONLY", size:6,  style:"background-color: #FFDF00;"},
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>&nbsp&nbsp&nbsp;提貨單號:</font>"},
	 		 {name:"#F.deliveryNo"   , type:"TEXT" , mode:"READONLY", size:15,  style:"background-color: #FFDF00;"},	 		 
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>&nbsp&nbsp&nbsp;銷售日期:</font>"},
	 		 {name:"#F.salesOrderDate", type:"TEXT" , mode:"READONLY", size:8,  style:"background-color: #FFDF00;"},	 		 
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>&nbsp&nbsp&nbsp;店別:</font>"},
	 		 {name:"#F.shopCode"     , type:"TEXT" , mode:"READONLY", size:5,  style:"background-color: #FFDF00;"},	 		 
	 		 {name:"#L.block"        , type:"LABEL", value:"<font color='red'>&nbsp&nbsp&nbsp;機檯:</font>"},
	 		 {name:"#F.posMachineCode", type:"TEXT" , mode:"READONLY", size:4,  style:"background-color: #FFDF00;"}], 
	          td:"colSpan='4' style='background-color:#FFDF00; border-color:#FFDF00;'"}]}
//	 {row_style:"background-color: #FFDF00", cols:[
//	 {items:[{name:"#L.description"  , type:"LABEL" ,value:"<B>說明</B><BR>"},
//	         {name:"#F.description"  , type:"TEXTAREA" ,  bind:"description", size:40 , row:15, col: 60, mode:"READONLY"}], 
//	          td:"colSpan='2'"}]}
	  ],
	  
		beginService:"",
		closeService:""
	});
	
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","明細檔資料" ,"vatDetailDiv"    ,"images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false);  
		vat.tabm.createButton(0 ,"xTab2","異動資料"   ,"vatLogDiv"       ,"images/tab_data_log_dark.gif"   ,"images/tab_data_log_light.gif", false);  
  	}
    kweDetail();
    kweLog();
	//vat.item.setValueByName("#F.description"  ,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	vat.item.setValueByName("#F.updateFlag"  ,"N");
	doFormAccessControl();
}

function kweDetail(){
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = false;
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"       });
	vat.item.make(vnB_Detail, "salesOrderDate"            , {type:"DATE" , size: 3, maxLen:20, mode:"READONLY", desc:"交易日期"   });
	vat.item.make(vnB_Detail, "posMachineCode"            , {type:"TEXT" , size: 4, maxLen:06, mode:"READONLY", desc:"機號"   });
	vat.item.make(vnB_Detail, "customerPoNo"              , {type:"TEXT" , size:20, maxLen:20, mode:"READONLY", desc:"交易序號"});
	vat.item.make(vnB_Detail, "superintendentCode"        , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"營業人員"});
	vat.item.make(vnB_Detail, "superintendentName"        , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"營業姓名"});
	vat.item.make(vnB_Detail, "countryCode"               , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"國籍"}); 
	vat.item.make(vnB_Detail, "breakable"                 , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"易碎品"});
	vat.item.make(vnB_Detail, "valuable"                  , {type:"TEXT" , size: 4, maxLen:12, mode:"READONLY", desc:"貴重品"});
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"   });
	vat.item.make(vnB_Detail, "salesOrderId"              , {type:"TEXT", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "lineId"                    , {type:"ROWID"});
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
    vat.item.make(vnB_Log, "indexNo"   , {type:"IDX"    ,                     desc:"序號"       });
	vat.item.make(vnB_Log, "logDate"   , {type:"DATE"   , desc:"異動日期"   });
	vat.item.make(vnB_Log, "logType"   , {type:"TEXT"   , desc:"類型" , size:6  });
	vat.item.make(vnB_Log, "logAction" , {type:"TEXT"   , desc:"動作"});
	vat.item.make(vnB_Log, "logLevel"  , {type:"TEXT"   , desc:"等級"});
	vat.item.make(vnB_Log, "message"   , {type:"TEXT"   , desc:"訊息",size:70, alter:true}); 
	vat.item.make(vnB_Log, "createrName" , {type:"TEXT" , desc:"執行人員", size:8}); 
	vat.item.make(vnB_Log, "creationDate", {type:"TEXT" , desc:"執行日期", size:18}); 
	vat.item.make(vnB_Log, "lineId"    , {type:"ROWID"});
	vat.block.pageLayout(vnB_Log, {
														id                  : "vatLogDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxLogService()",
														//closeService        : function(){kweInitial();},
														loadSuccessAfter    : "pageLoadLogSuccess()",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "4"
														});
}
function loadBeforeAjxService(){
	var processString = "process_object_name=soDeliveryService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&brandCode=T2" + 
	                    "&orderTypeCode=DNZ"  +
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
	//alert("After loadBeforeAjxLogService");
	return processString;
}
function kweButtonLine(){
	var allProcessType = [["","",false],
                 ["手動","自動"],
                 ["MANUAL","AUTO"]];
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-right-width: 0px;  border-left-width: 0px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 
	 	{items:[ {name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("MANUAL")'},
	 	         {name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'}
	 		   ],  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"},
	 	{items:[{name:"#L.processMode"              , type:"LABEL"  ,value:"執行模式："},
	 	        {name:"#F.processMode"              , type:"SELECT"  ,  bind:"processMode", size:20, init:allProcessType}]
	 	        ,  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
	 	        }]},
	  ],
		beginService:function(){return ""}	,
		closeService:function(){return ""}
	});
	vat.item.setValueByName("#F.processMode"      ,vat.bean().vatBeanOther.processMode);
}



function pageLoadSuccess(){
	//alert("kwePageLoadSuccess");
	//countTotalQuantity();
	//getTheFirstItemCategory();

}

function searchDelivery(){
	vat.item.setValueByName("#F.searchNo",vat.item.getValueByName("#F.searchNo").toUpperCase());
    var vsSearchNo     = vat.item.getValueByName("#F.searchNo");
    var vsSearchStatus = vat.item.getValueByName("#F.searchStatus");
    var vsAllow = false;
	if(vsSearchNo.length > 0 ){
		if(checkDeliveryNo(vsSearchNo))
		 		vsAllow = true;
		 	else{
		 		vsAllow = false;
		 		alert("提貨單號格式錯誤");
		}
	    	
	}else{
	    vsAllow = false;
	}
    vat.item.setValueByName("#F.searchDesc"  ,"");
	vat.item.setValueByName("#F.statusName"  ,"");
	vat.item.setValueByName("#F.lockFlagName","");
	vat.item.setValueByName("#F.headId", "");
	
	if(vsAllow){
  		vat.bean().vatBeanOther.brandCode = vat.bean().vatBeanOther.loginBrandCode;
  		//vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
  		vat.bean().vatBeanOther.searchNo = vsSearchNo;
		vat.bean().vatBeanOther.searchStatus = vsSearchStatus;
		vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=findDeliveryByDeliveryNo";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("" != vat.bean().vatBeanOther.formId ){
		     	    vat.item.setValueByName("#F.headId", vat.bean().vatBeanOther.formId);
			     	vat.item.setValueByName("#F.searchDesc" , "己建檔");
			     	vat.item.setValueByName("#F.statusName" , vat.bean("statusName"));
			     	vat.item.setValueByName("#F.lockFlagName" , vat.bean("lockFlagName"));
					
			     }else{
			        vat.item.setValueByName("#F.headId", "");
			        vat.item.setValueByName("#F.searchDesc"  ,"無資料");
					vat.item.setValueByName("#F.statusName"  ,"");
					vat.item.setValueByName("#F.lockFlagName","");
				
			     }
		     	 
		     	 vat.block.pageDataLoad(vnB_Detail,getLastPage(vnB_Detail));
		     	 vat.block.pageDataLoad(vnB_Log,getLastPage(vnB_Log));
		     	 vat.form.item.setFocus( "#F.searchNo" );
		     	 changeUpdateFlag("LOCK")
		     }
		});
	
	}

	
}


function searchCustomerPoNo(){
	vat.item.setValueByName("#F.invoiceNo",vat.item.getValueByName("#F.invoiceNo").toUpperCase());
    var vsSearchNo     =vat.item.getValueByName("#F.invoiceNo");
    var vsSearchStatus = vat.item.getValueByName("#F.searchStatus");
    var vsAllow = false;
	if(vsSearchNo.length > 0 ){
	 	if(vsSearchNo.substring(0, 3)!="DZN")
	 		vsAllow = true;
	 	else{
	 		vsAllow = false;
	 		alert("銷售單號格式錯誤");
	 	}
	    	
	}else{
	    vsAllow = false;
	}
	
	
	vat.item.setValueByName("#F.invoiceDesc" , "");
	vat.item.setValueByName("#F.deliveryNo" ,  "");		

	if(vsAllow){
  		vat.bean().vatBeanOther.brandCode = vat.bean().vatBeanOther.loginBrandCode;
  		//vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
  		vat.bean().vatBeanOther.searchNo = vsSearchNo;
		vat.bean().vatBeanOther.searchStatus = vsSearchStatus;
		vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=findSalesOrderByCustomerPoNo";},  {other:true,picker:false,
		     funcSuccess: function() {
		     
		         if("" != vat.bean().vatBeanOther.CustomerDeliveryNo ){
			     		vat.item.setValueByName("#F.invoiceDesc" , "己建檔");
						vat.item.setValueByName("#F.deliveryNo" ,  vat.bean("CustomerDeliveryNo"));	
						vat.item.setValueByName("#F.salesOrderDate" ,  vat.bean("salesOrderDate"));	
						vat.item.setValueByName("#F.shopCode" ,  vat.bean("shopCode"));	
						vat.item.setValueByName("#F.posMachineCode" ,  vat.bean("posMachineCode"));	
			     }else{
			     		vat.item.setValueByName("#F.invoiceDesc" , "無資料");
						vat.item.setValueByName("#F.deliveryNo" ,  "");		
						vat.item.setValueByName("#F.salesOrderDate" , "");	
						vat.item.setValueByName("#F.shopCode" , "");	
						vat.item.setValueByName("#F.posMachineCode" , "");	
			     }
			    // vat.form.item.setFocus( "#F.invoiceNo" );
   				if("AUTO"==vat.bean().vatBeanOther.processMode)
   				 	doSubmit("MANUAL");
   				 	
   				 
		     }
		});
	
	}
	
	
}
function getEmployeeInfo() {
    
    if ("" !=vat.item.getValueByName("#F.employeeCode")) {
        vat.item.setValueByName("#F.employeeCode",vat.item.getValueByName("#F.employeeCode").toUpperCase());

        vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
        vat.block.submit(function(){return "process_object_name=soDeliveryService"+
		            "&process_object_method_name=getEmployeeInfo";},  {other:true,picker:false,
		     funcSuccess: function() {
		         if("" != vat.bean().vatBeanOther.employeeName ){
                    vat.item.setValueByName("#F.employeeName", vat.bean().vatBeanOther.employeeName);
                    vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
                    vat.bean().vatBeanOther.executeEmployeeName=vat.item.getValueByName("#F.employeeName");
					changeUpdateFlag("LOCK");                    
			     }else{
			     　　vat.item.setValueByName("#F.employeeName", "");
			     	alert("員工代號錯誤，請重新輸入！");
	 				vat.form.item.setFocus( "#F.employeeCode" );			     	
			     }
		     }
		});
    }else{
    	vat.item.setValueByName("#F.employeeName","");
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
		"?programId=IM_MOVEMENT" +
		"&levelType=ERROR" +
        "&processObjectName=imMovementService" +
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


function getLastPage(vGrid){
	var	vLastPage = Math.floor(vat.block.getGridObject(vGrid).lastIndex / vat.block.getGridObject(vGrid).pageSize) + 1;
 	return vLastPage;
}

function changeUpdateFlag(type){
 
  if(type == "LOCK"){
	if("N" == vat.item.getValueByName("#F.updateFlag")) {
        //鎖定
		if(vat.item.getValueByName("#F.employeeCode") != "" && vat.item.getValueByName("#F.employeeName")!= "" ){
			vat.item.setValueByName("#F.updateFlag","E");
			vat.bean().vatBeanOther.executeEmployee =  vat.item.getValueByName("#F.employeeCode");
			vat.bean().vatBeanOther.executeAction =  vat.item.getValueByName("#F.executeAction");
			vat.form.item.setFocus( "#F.searchNo" );
		}else{
		    vat.form.item.setFocus( "#F.employeeCode" );
			alert("請輸入執行人員工號");
		}	
	}else if("E" == vat.item.getValueByName("#F.updateFlag")){
	    //鎖定DeliveryNo
	    if(vat.item.getValueByName("#F.searchNo") != "" && vat.item.getValueByName("#F.searchNo")!= "" ){
	    	vat.item.setValueByName("#F.updateFlag","D");
	    	vat.bean().vatBeanOther.deliveryNo =  vat.item.getValueByName("#F.searchNo");
	    	vat.form.item.setFocus( "#F.invoiceNo" );
	    }else{
	       vat.form.item.setFocus( "#F.searchNo" );
	       alert("請輸入執行報單單號");
	    }
	}else{
		vat.item.setValueByName("#F.updateFlag","N");
		//vat.form.item.setFocus( "#F.employeeCode" );
	    
	}
  }else{
	if("D" == vat.item.getValueByName("#F.updateFlag")) {
        //鎖定
		vat.item.setValueByName("#F.updateFlag","E");
		vat.form.item.setFocus( "#F.searchNo" );
	}else if("E" == vat.item.getValueByName("#F.updateFlag")){
		vat.item.setValueByName("#F.updateFlag","N");
		vat.form.item.setFocus( "#F.employeeCode" );
	}else{
		vat.item.setValueByName("#F.updateFlag","N");
	}
  }
  vat.bean().vatBeanOther.updateFlag = vat.item.getValueByName("#F.updateFlag");
  doFormAccessControl();
}


function doFormAccessControl(){
    var vsFormId = vat.item.getValueByName("#F.headId");
	//vat.item.setAttributeByName("vatBlock_Employee" , "readOnly", true);
	//vat.item.setAttributeByName("vatBlock_Data"     , "readOnly", true);
	vat.item.setStyleByName("#B.close"   , "display", "inline");
	vat.item.setStyleByName("#B.elock"   , "display", "inline");
	vat.item.setStyleByName("#B.unelock" , "display", "inline");
	vat.item.setStyleByName("#B.dlock"   , "display", "inline");
	vat.item.setStyleByName("#B.undlock" , "display", "inline");
	vat.item.setStyleByName("#B.submit"  , "display", "inline");
	vat.item.setAttributeByName("#F.processMode"  , "readOnly", true);
    if("N" == vat.item.getValueByName("#F.updateFlag")){
    	vat.item.setAttributeByName("#F.searchNo"      , "readOnly", true);
		vat.item.setAttributeByName("#F.invoiceNo"     , "readOnly", true);
	    vat.item.setAttributeByName("#F.employeeCode"  , "readOnly", false);
		vat.item.setAttributeByName("#F.action"        , "readOnly", false);
		vat.item.setStyleByName("#B.unelock", "display", "none");
		vat.item.setStyleByName("#B.undlock", "display", "none");
		vat.item.setStyleByName("#B.dlock"  , "display", "none");
		vat.item.setStyleByName("#B.submit" , "display", "none");
		//vat.form.item.setFocus( "#F.employeeCode" );
	}else if("E" == vat.item.getValueByName("#F.updateFlag")){
	    vat.item.setAttributeByName("#F.action"  , "readOnly", true);
		vat.item.setAttributeByName("#F.employeeCode" , "readOnly", true);	    
		vat.item.setAttributeByName("#F.invoiceNo" , "readOnly", true);
		vat.item.setAttributeByName("#F.searchNo"  , "readOnly", false);
		vat.item.setStyleByName("#B.elock"   , "display", "none");
		vat.item.setStyleByName("#B.undlock" , "display", "none");
		vat.item.setAttributeByName("elock"     , "readOnly", false);
		vat.item.setStyleByName("#B.submit" , "display", "none");
		///vat.form.item.setFocus( "#F.invoiceNo" );
	}else if("D" == vat.item.getValueByName("#F.updateFlag")){
	    vat.item.setAttributeByName("#F.action"  , "readOnly", true);
		vat.item.setAttributeByName("#F.employeeCode" , "readOnly", true);
	    vat.item.setAttributeByName("#F.searchNo"  , "readOnly", true);
		vat.item.setAttributeByName("#F.invoiceNo" , "readOnly", false);
		vat.item.setStyleByName("#B.elock"   , "display", "none");
		vat.item.setStyleByName("#B.unelock" , "display", "none");
		vat.item.setStyleByName("#B.dlock"   , "display", "none");
	}
	
}

function pageLoadLogSuccess(){
	//alert("kwePageLoadSuccess");
	//countTotalQuantity();
	//getTheFirstItemCategory();

}
function doSubmit(vsType){
	var vsAction = vat.item.getValueByName("#F.action");
	var vsDeliveryNo=vat.item.getValueByName("#F.searchNo");
	var vsCustomerPoNo=vat.item.getValueByName("#F.invoiceNo").substring(0,10);
	vat.bean().vatBeanOther.searchNo = vsDeliveryNo;
  	vat.bean().vatBeanOther.deliveryNo = vsDeliveryNo;
	vat.bean().vatBeanOther.customerPoNo = vsCustomerPoNo;
	vat.bean().vatBeanOther.executeEmployee = vat.item.getValueByName("#F.employeeCode");
	var vAllow = true;
	vat.bean().vatBeanOther.action = vsAction;
	if("" != vat.item.getValueByName("#F.deliveryNo")){
		if(vat.item.getValueByName("#F.deliveryNo") != vat.item.getValueByName("#F.searchNo")){
			alert("此入提單號已存在於"+vat.item.getValueByName("#F.deliveryNo")+"中，無法建檔，請重新輸入。");
			vAllow = false;
		}
	}
	if(vAllow && vsDeliveryNo != "" && vsCustomerPoNo != ""&&"D" == vat.item.getValueByName("#F.updateFlag")){
		vat.bean().vatBeanOther.spType = vsAction =="REMOVE"?"DeleteLineData":"CustomerPoNo";
		processString = "process_object_name=soDeliveryAction&process_object_method_name=updateDeliveryDataBySp";
	    vat.block.submit(function(){return processString;},{
                    bind:false, link:false, other:true,
                    funcSuccess:function(){
                        if("0" != vat.bean().vatBeanOther.formId ){
                        		vat.item.setValueByName("#F.headId", vat.bean().vatBeanOther.formId )
                        }else{
                        		vat.item.setValueByName("#F.headId", "" )
                        }
                              vat.block.pageDataLoad(vnB_Detail,1);
     		           vat.block.pageDataLoad(vnB_Log,1);
		        	
		 }}
	    );
	}else{
		if(vsType=="MANUAL")
			alert("提貨單號或POS單號不可為空白");
	}
	
}
function checkDeliveryNo(vsDeliveryNo){
	var vsResult=false;
	var vsCheckNo="";
	var vsValue =0;
	var vsCheckNo ="";
	var vsYear=0;
	var vsMonth =0;
	var vsSerial1=0;
	var vsSerial2=0;
	var vsSerial3=0;
	if(vsDeliveryNo.length ==13){
		//0123456789012
		//DZN0110000062
   		if(vsDeliveryNo.substr(0, 3)=="DZN"){
			vsYear   = parseInt(vsDeliveryNo.substr(3, 1),10);	
			vsMonth  = parseInt(vsDeliveryNo.substr(4, 2),10);	
			vsSerial1= parseInt(vsDeliveryNo.substr(6, 2),10);	
			vsSerial2= parseInt(vsDeliveryNo.substr(8, 2),10);	
			vsSerial3= parseInt(vsDeliveryNo.substr(10, 2),10);	
			vsCheckNo= vsDeliveryNo.substr(12, 1);	
			
			vsValue=parseInt((vsSerial1+vsSerial2+vsSerial3)/(vsYear+vsMonth+vsSerial3)*1000,10).toString();
			if(vsCheckNo == vsValue.substr(vsValue.length-1,1))
				vsResult=true;
			
		}
	}
	return vsResult;
}
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();

}