vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail8 =26;
var vnB_master = 2;
var isSelectDetail=false;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
  	doLockOrder('');
  	//doFormAccessControl();///呼叫doFormAccessControl method存取權限
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");

//MACO EOS 20151212
		//vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);    
		vat.tabm.createButton(0 ,"xTab18","明細檔資料" ,"vatDetailDiv8","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", "doPageDataSave()");
		
	}
	//kweMaster();
	kweDetail8();
	//doFormAccessControl();
}

function formInitial(){

	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          brandCode  		    : document.forms[0]["#loginBrandCode"    ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode" 	 ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId      	: document.forms[0]["#assignmentId"      ].value,
          userType      	    : document.forms[0]["#userType"          ].value,
          beforeStatus			: "beforestatus",
	      nextStatus			: "nextstatus",
	      approvalResult        : "",
	      approvalComment		:"",
          status                :document.forms[0]["#status" ].value===""?"SAVE":document.forms[0]["#status" ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=poEosAction&process_object_method_name=performSearchDetailInitial"; 
	    	          },{
	    		other: true
	    	            }
	    );
	 		//vat.item.SelectBind(vat.bean("department"),{ itemName : "#F.department" ,selected : vat.bean().vatBeanOther.department});
  }
}

function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
	 			{name:"#B.searchList"  , type:"IMG"    ,value:"明細查詢",   src:"./images/detail_search.gif" , eClick:'doSearch()'},
				{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"doExport()"},
				{name:"#B.exportPage"      , type:"IMG"    ,value:"單頁明細匯出",   src:"./images/export_one_page.jpg" , eClick:"doExportPage()"},
				{name:"#B.lockOrder"      , type:"IMG"    ,value:"鎖定送單",   src:"./images/lockSubmit.png" , eClick:"doLockOrder('Y')"},
				{name:"#B.unlockOrder"      , type:"IMG"    ,value:"解除鎖定",   src:"./images/unlockSubmit.png" , eClick:"doLockOrder('N')"},
				{name:"#B.unlock"      , type:"IMG"    ,value:"凍結",   src:"./images/button_unlock_data.gif" , eClick:"doUnlock()"},
				{name:"#B.doSelectSubmit"	   , type:"IMG"    ,value:"明細送出",   src:"./images/button_detail_submit.gif", eClick:'doSelectSubmit()'}],
	 			
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	vat.item.setStyleByName("#B.doSelectSubmit","display", "none");
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}
















function headerInitial(){
//MACO EOS 20151212
         
	var orderTypeCode       = vat.item.getValueByName("#orderTypeCode");
 	var allShop		        = vat.bean("allShop");
 	var allItemCategory		= vat.bean("allItemCategory");
	var allWarehouse		= [["","",true], ["外倉","內倉"], ["9900","6200"]];
 	var allstatus           = [["","",false], ["待出貨", "已出貨","有效單據","作廢"],["SIGNING","FINISH","","VOID"]];
 	var allEnable           = [["","",false], ["已出貨", "待出貨","全部"],["Y","N",""]];
 	var allTaxType          = [["","",true], [ "F-保稅", "P-完稅"],["F","P"]];


	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"EOS單查詢作業", rows:[  
		 {row_style:"", cols:[
		 {items:[{name:"#L.orderTypeCode"            , type:"LABEL"  , value:"單別"}]},	 		 
		 {items:[{name:"#F.orderTypeCode"            , type:"TEXT" ,  bind:"orderTypeCode", size:12, mode:"READONLY"}]},
		 {items:[{name:"#L.orderNo"            		 , type:"LABEL"  , value:"單號"}]},	 		 
		 {items:[{name:"#F.orderNo"            		 , type:"TEXT" ,  bind:"orderNo", size:12}]},
		 {items:[{name:"#L.warehouseCode" 			 , type:"LABEL" , value:"來源庫別"}]},	 
		 {items:[{name:"#F.warehouseCode"    		 , type:"SELECT",  bind:"warehouseCode", size:20,mode:"READONLY", init:allWarehouse}]}, 
		 {items:[{name:"#L.status"                   , type:"LABEL"  , value:"單據狀態"}]},	 		 
		 {items:[{name:"#F.status"                   , type:"SELECT" ,  bind:"status", size:12, init:allstatus}]},
		 {items:[{name:"#L.requestTimeScope"		 , type:"LABEL" , value:"需求日期"}]},
	 	 {items:[{name:"#F.startDate"       		 , type:"DATE"  ,  bind:"startDate", size:12},
	    		{name:"#L.between"					 , type:"LABEL" , value:"至"},
	    		{name:"#F.endDate"					 , type:"DATE"  ,  bind:"endDate", size:12}], td:" colSpan=3"}]},

		 {row_style:"", cols:[
		 {items:[{name:"#L.categoryItem"             , type:"LABEL"  , value:"業種"}]},	 		 
		 {items:[{name:"#F.categoryItem"             , type:"SELECT" ,  bind:"categoryItem", size:12, init:allItemCategory}]},
		 {items:[{name:"#L.isTax"               	 , type:"LABEL"  , value:"稅別"}]},	 
		 {items:[{name:"#F.isTax"               	 , type:"SELECT"   ,  bind:"isTax", size:6,init:allTaxType}]},
		 {items:[{name:"#L.shopCode"    			 , type:"LABEL" , value:"需求店別"}]},	 
		 {items:[{name:"#F.shopCode"    			 , type:"SELECT",  bind:"shopCode", size:20, init:allShop}]}, 


		 {items:[{name:"#L.enable"                   , type:"LABEL"  , value:"狀態"}]},	 		 
		 {items:[{name:"#F.enable"                   , type:"SELECT" ,  bind:"enable", size:12, init:allEnable}]},
		 {items:[{name:"#L.brandCode"                , type:"LABEL"  , value:"品牌"}]},	 
		 {items:[{name:"#F.brandCode"                , type:"TEXT"   ,  bind:"brandCode", size:6, mode:"READONLY"}]},
		 {items:[{name:"#L.isLock"                	 , type:"LABEL"  , value:"鎖定狀態"}]},	
		 {items:[
		 		 {name:"#L.9900isLock"               , type:"LABEL"  , value:"總倉"},
		 		 {name:"#F.9900isLock"               , type:"XBOX"   ,  bind:"9900isLock", size:6, mode:"READONLY"},
		 		 {name:"#L.6200isLock"               , type:"LABEL"  , value:"內倉"},
		 		 {name:"#F.6200isLock"               , type:"XBOX"   ,  bind:"6200isLock", size:6, mode:"READONLY"}]}
		 ]}
		
	
		  ], 
		  
		beginService:"",
		closeService:function(){defaultValue();}		
	});
	
}

function kweDetail8(){
		var allStatus = [[true,true], ["待出貨", "已出貨"],["N","Y"]];
		vbCanGridDelete = true;
		vbCanGridAppend = true;
		vbCanGridModify = true;
	 /*
	  * itemNo				:			品號	
	  * specInfo			:			預留欄位
	  * itemName			:			品名
	  * supplier			:			來源庫
	  * purTotalAmount		:			來源庫存
	  * quantity			:			需求量
	  * shopCode			:			店別
	  * reTotalAmount		:			店庫存
	  */
	//vat.item.make(vnB_Detail8, "checked"         , {type:"XBOX"});
    vat.item.make(vnB_Detail8, "indexNo"                   , {type:"IDX"   ,                     desc:"序號"          });
    vat.item.make(vnB_Detail8, "time"                  , {type:"TEXT"  , size:18, maxLen:20, desc:"訂貨時間", mode:"READONLY"});
    vat.item.make(vnB_Detail8, "lineId"                  , {type:"NUM"  , size:18, maxLen:20, desc:"流水編碼", mode:"HIDDEN"});
    vat.item.make(vnB_Detail8, "orderNo"                  , {type:"TEXT" , size:12, maxLen:20, desc:"單號"	, mode:"READONLY" });
	vat.item.make(vnB_Detail8, "itemNo"                  , {type:"TEXT"  , size:16, maxLen:20, desc:"品號", mode:"READONLY" , mask:"CCCCCCCCCCCC", eChange:"changeItemCode()"});
	vat.item.make(vnB_Detail8, "itemName"                  , {type:"TEXT" , size:12, maxLen:20, desc:"品名"	, mode:"READONLY"	, alter:true });
	//vat.item.make(vnB_Detail8, "itemBrandCode"                  , {type:"TEXT" , size:12, maxLen:20, desc:"品牌"	, mode:"READONLY" });
	vat.item.make(vnB_Detail8, "purTotalAmount"       , {type:"NUMM" , size: 4, maxLen:12, desc:"來源庫存"	, dec:0	, mode:"READONLY"});
	vat.item.make(vnB_Detail8, "quantity"          , {type:"NUMM" , size: 4, maxLen:12, desc:"需求量"	, mode:"READONLY", dec:0});
	vat.item.make(vnB_Detail8, "boxCapacity"          , {type:"NUMM" , size: 4, maxLen:12, desc:"基本量"	, mode:"READONLY", dec:0});
	vat.item.make(vnB_Detail8, "bｏｘ"          , {type:"NUMM" , size: 4, maxLen:12, desc:"裝箱量"	, mode:"READONLY", dec:0});
	vat.item.make(vnB_Detail8, "itemCategory"          , {type:"TEXT" , size: 8, maxLen:12, desc:"業種"	, mode:"READONLY", dec:0});
	vat.item.make(vnB_Detail8, "enable"          , {type:"SELECT" , size: 4, maxLen:12, desc:"出貨狀態"	, init:allStatus, mode:"READONLY",  dec:0});
	vat.item.make(vnB_Detail8, "enableButton"          , {type:"BUTTON" , size: 4, maxLen:12, value:"送出"	, mode:"READONLY", dec:0,eClick:'controler()'});
	vat.block.pageLayout(vnB_Detail8, {
														id: "vatDetailDiv8",
														pageSize: 10,
														searchKey     : ["itemCode"],
								                        canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,
														//beginService: "",
														//selectionType       : "CHECK",
														indexType	: "AUTO",
								                        //closeService: function(){kweImInitial();},
														//appendBeforeService : "kwePageAppendBeforeMethod()",
														//appendAfterService  : "kwePageAppendAfterMethod()",
														loadBeforeAjxService: "loadBeforeAjxService()",
														//loadSuccessAfter    : "kwePageLoadSuccess()",
														//eventService        : "assignOriginalQtyToArrival()",
														saveBeforeAjxService: "saveBeforeAjxService()"
														//saveSuccessAfter    : "saveSuccessAfter()"
														});
}

function loadBeforeAjxService(){
	var processString = "process_object_name=poEosService&process_object_method_name=getAJAXSearchDetailPageData"  
	                  +"&warehouseCode" 	 + "=" + vat.item.getValueByName("#F.warehouseCode") 
	                  +"&orderNo"    	     + "=" + vat.item.getValueByName("#F.orderNo") 
	                  +"&categoryItem"    	 + "=" + vat.item.getValueByName("#F.categoryItem") 	  
	                  +"&shopCode"      	 + "=" + vat.item.getValueByName("#F.shopCode") 	                  
	                  +"&status" 			 + "=" + vat.item.getValueByName("#F.status") 
	                  +"&enable" 			 + "=" + vat.item.getValueByName("#F.enable") 
	                  +"&isTax" 			 + "=" + vat.item.getValueByName("#F.isTax") 
	                  +"&brandCode"			 + "=" + document.forms[0]["#loginBrandCode"    ].value
	                  +"&startDate"			 + "=" + vat.item.getValueByName("#F.startDate")
	                  +"&endDate" 			 + "=" + vat.item.getValueByName("#F.endDate");    
                                                                            
	return processString;											
}
function saveBeforeAjxService()
{   //alert("saveBeforeAjxService");
	
		var processString = "process_object_name=poEosService&process_object_method_name=updateAJAXDetailPageLinesData" 
							+"&headId=" + vat.item.getValueByName("#F.headId")
		                    +"&brandCode=" + vat.item.getValueByName("#F.brandCode")
		                    +"&shopCode=" + vat.item.getValueByName("#F.warehouseCode")
		                    +"&status=" + vat.item.getValueByName("#F.status");
		                    //alert(processString); 
		return processString;					
}

function changeItemCode(){


var nItemLine = vat.item.getGridLine();
var vemployeeCode = vat.item.getGridValueByName("itemNo"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	
vat.item.setGridValueByName("itemNo", nItemLine, vemployeeCode);
		
		vat.ajax.XHRequest(
       	{
           post:"process_object_name=poEosService"+
			"&process_object_method_name=findInfoByItemCode"+
			 "&itemNo=" + itemNo,
           find: function changeSuperintendentRequestSuccess(oXHR){ 

					
					
					
               vat.item.setGridValueByName("itemNo", nItemLine, vat.ajax.getValue("itemNo", oXHR.responseText));
               vat.item.setGridValueByName("itemName", nItemLine, vat.ajax.getValue("itemName", oXHR.responseText));
               vat.item.setGridValueByName("boxCapacity", nItemLine, vat.ajax.getValue("boxCapacity", oXHR.responseText));
               vat.item.setGridValueByName("purTotalAmount", nItemLine, vat.ajax.getValue("purTotalAmount", oXHR.responseText));
               vat.item.setGridValueByName("boxCapacity", nItemLine, vat.ajax.getValue("boxCapacity", oXHR.responseText));
               vat.item.setGridValueByName("quantity", nItemLine, vat.ajax.getValue("quantity", oXHR.responseText));
               vat.item.setGridValueByName("box", nItemLine, vat.ajax.getValue("box", oXHR.responseText));
               vat.item.setGridValueByName("reTotalAmount", nItemLine, vat.ajax.getValue("reTotalAmount", oXHR.responseText));
        }   
       });
}
function changeDesWarehous(){
	vat.ajax.XHRequest({
		post:"process_object_name=poEosService&process_object_method_name=findWarehouseByShop"
                  + "&department=" +  vat.item.getValueByName("#F.department"),


		find: function change(oXHR){
				vat.item.setValueByName("#F.no", vat.ajax.getValue("no", oXHR.responseText));
         		vat.item.setValueByName("#F.warehouseCode", vat.ajax.getValue("warehouseCode", oXHR.responseText));
         		//vat.block.pageRefresh(vnB_Detail8);
		},
		fail: function changeError(){

         		alert("無對應庫別");
		}  
       });

}
function changeQty()
{

	var nItemLine = vat.item.getGridLine();
	var vQty = vat.item.getGridValueByName("quantity"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vBoxCapacity = vat.item.getGridValueByName("boxCapacity"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vPurTotalAmount = vat.item.getGridValueByName("purTotalAmount"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	//alert("vQty:"+vQty+"vBoxCapacity:"+vBoxCapacity+"box:"+vQty/vBoxCapacity);
	if(parseFloat(vQty)>parseFloat(vPurTotalAmount))
	{
		alert("需求量不可大於來源庫存");
		vat.item.setGridValueByName("quantity", nItemLine, 0);
	}
	else
	{
		vat.item.setGridValueByName("box", nItemLine, parseFloat(vQty)/parseFloat(vBoxCapacity));
	}
}

function doExport(){
	var beanName = "BU_PURCHASE_EOS_EXPORT";
	
	
	// var customer = vat.utils.escape(RTrim(vat.item.getValueByName("#F.customerName")));
	
	//var customer = (RTrim(vat.item.getValueByName("#F.customerName")));
	var url;
    	url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&processObjectName=poEosService" +
              "&processObjectMethodName=exportEosDetail" 
					  +"&warehouseCode" 	 + "=" + vat.item.getValueByName("#F.warehouseCode") 
					  +"&orderNo"    	 + "=" + vat.item.getValueByName("#F.orderNo") 
					  +"&categoryItem"    	 + "=" + vat.item.getValueByName("#F.categoryItem") 
	                  +"&shopCode"           + "=" + vat.item.getValueByName("#F.shopCode") 	                  
	                  +"&status" 			 + "=" + vat.item.getValueByName("#F.status") 
	                  +"&enable" 			 + "=" + vat.item.getValueByName("#F.enable")
	                  +"&isTax" 			 + "=" + vat.item.getValueByName("#F.isTax") 
	                  +"&brandCode"			 + "=" + document.forms[0]["#loginBrandCode"    ].value
	                  +"&startDate"			 + "=" + vat.item.getValueByName("#F.startDate")
	                  +"&endDate" 			 + "=" + vat.item.getValueByName("#F.endDate") 
	                  +"&function"			 + "=" + "1"
    var width = "200";
    var height = "30";
    url = encodeURI(encodeURI(url));
    var lock9900 = vat.item.getValueByName("#F.9900isLock");
	var lock6200 = vat.item.getValueByName("#F.6200isLock");
	var vDoWarehouse = document.forms[0]["#userType"].value;
					
    if((lock9900 === "N" && vDoWarehouse === "9900")||(lock6200 === "N" && vDoWarehouse === "6200")){
		var alertMessage2 ="目前訂貨單送出功能並未鎖定，是否鎖定?";
		if(confirm(alertMessage2)){
			doLockOrder("Y");
		}
		else{
    		vat.block.pageSearch(vnB_Detail8, {
    		funcSuccess : function(){
    			// alert('success for excel export');
    			window.open(url, '資料匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});

		}
	}
	else{
    // alert(url + "/customer:" + customer + "/");
    vat.block.pageSearch(vnB_Detail8, {
    		funcSuccess : function(){
    			// alert('success for excel export');
    			window.open(url, '資料匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});
	}
}
function doExportPage(){
	var beanName = "BU_PURCHASE_EOS_EXPORT_PAGE";
	
	
	// var customer = vat.utils.escape(RTrim(vat.item.getValueByName("#F.customerName")));
	
	//var customer = (RTrim(vat.item.getValueByName("#F.customerName")));
	var url;
    	url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&function=onePage" +
              "&processObjectName=poEosService" +
              "&processObjectMethodName=exportEosDetail" 
					  +"&warehouseCode" 	 + "=" + vat.item.getValueByName("#F.warehouseCode") 
					  +"&categoryItem"    	 + "=" + vat.item.getValueByName("#F.categoryItem") 
	                  +"&shopCode"           + "=" + vat.item.getValueByName("#F.shopCode") 
	                  +"&enable"           + "=" + vat.item.getValueByName("#F.enable") 
	                  +"&isTax"           + "=" + vat.item.getValueByName("#F.isTax")	 
	                  +"&orderNo"           + "=" + vat.item.getValueByName("#F.orderNo")
	                  +"&status" 			 + "=" + vat.item.getValueByName("#F.status") 
	                  +"&brandCode"			 + "=" + document.forms[0]["#loginBrandCode"    ].value
	                  +"&startDate"			 + "=" + vat.item.getValueByName("#F.startDate")
	                  +"&endDate" 			 + "=" + vat.item.getValueByName("#F.endDate")
	                  +"&function"			 + "=" + "2" 
    var width = "200";
    var height = "30";
    url = encodeURI(encodeURI(url));
    var lock9900 = vat.item.getValueByName("#F.9900isLock");
	var lock6200 = vat.item.getValueByName("#F.6200isLock");
	var vDoWarehouse = document.forms[0]["#userType"].value;
					
    if((lock9900 === "N" && vDoWarehouse === "9900")||(lock6200 === "N" && vDoWarehouse === "6200")){
		var alertMessage2 ="目前訂貨單送出功能並未鎖定，是否鎖定?";
		if(confirm(alertMessage2)){
			doLockOrder("Y");
		}
		else{
    		vat.block.pageSearch(vnB_Detail8, {
    		funcSuccess : function(){
    			// alert('success for excel export');
    			window.open(url, '資料匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});
		}
	}
	else{
    // alert(url + "/customer:" + customer + "/");
    vat.block.pageSearch(vnB_Detail8, {
    		funcSuccess : function(){
    			// alert('success for excel export');
    			window.open(url, '資料匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});
	}
}
function doSearch(){
	var lock9900 = vat.item.getValueByName("#F.9900isLock");
	var lock6200 = vat.item.getValueByName("#F.6200isLock");
	var vDoWarehouse = document.forms[0]["#userType"].value;
    if((lock9900 === "N" && vDoWarehouse === "9900")||(lock6200 === "N" && vDoWarehouse === "6200")){
		var alertMessage2 ="目前訂貨單送出功能並未鎖定，是否鎖定?";
		if(confirm(alertMessage2)){
			doLockOrder("Y");
		}
		else{
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail8 , vnCurrentPage = 1);}
			                    });
		}
	}
	else{
    // alert(url + "/customer:" + customer + "/");
     vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail8 , vnCurrentPage = 1);}
			                    });
	}

} 

function controler()
{
	var nItemLine = vat.item.getGridLine();
	var vLoginBrandCode = document.forms[0]["#loginBrandCode" 	 ].value;
	//var vOrderTypeCode = document.forms[0]["#orderTypeCode" 	 ].value;
	var vOrderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var vOrderNo = vat.item.getGridValueByName("orderNo" , nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vItemNo = vat.item.getGridValueByName("itemNo" , nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vEnable = vat.item.getGridValueByName("enable" , nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var vLineId = vat.item.getGridValueByName("lineId" , nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var alertMessage ="是否確定送出?";
	if(confirm(alertMessage))
	{
			if(vEnable === "Y")
			{
				alert("此項明細已送出,請勿重複送出");
			}
			else
			{
				vEnable = "Y";
				vat.item.setGridValueByName("enable", nItemLine, "Y");

				vat.ajax.XHRequest({
				post:"process_object_name=poEosService&process_object_method_name=updateStatus"+
						  "&brandCode=" +  vLoginBrandCode+
		                  "&orderTypeCode=" +  vOrderTypeCode+
		                  "&orderNo=" +  vOrderNo+
		                   "&itemNo=" +  vItemNo+
		                    "&enable=" +  vEnable+
		                    "&lineId=" +  vLineId ,
		
		
				find: function change(oXHR){
		         		vat.block.pageRefresh(vnB_Detail8);
				},
				fail: function changeError(){
						vat.block.pageRefresh(vnB_Detail8);
				}  
		       });
	       	}

	}
}


function doLockOrder(vIsLock)//鎖定營業送單 20170623 Maco
{
	var vDoWarehouse = document.forms[0]["#userType"].value;
	var vLoginEmployeeCode = document.forms[0]["#loginEmployeeCode" ].value;
	vat.ajax.XHRequest(
    {
    	post:"process_object_name=poEosService&process_object_method_name=updateLockStatus"+
    	 "&loginEmployeeCode=" + vLoginEmployeeCode+
		 "&warehouse=" + vDoWarehouse+
		 "&lock=" + vIsLock,
          find: function changeSuperintendentRequestSuccess(oXHR)
          { 
              vat.item.setValueByName("#F.9900isLock", vat.ajax.getValue("lock9900", oXHR.responseText));
              vat.item.setValueByName("#F.6200isLock", vat.ajax.getValue("lock6200", oXHR.responseText));
              if(vIsLock==="Y")
              	alert("訂貨單功能送出已鎖定，待明細送出後自動解鎖，或手動解除鎖定");
              else if(vIsLock==="N")
              	alert("訂貨單功能送出已透過手動解除鎖定");

	      }   
    });
}
function defaultValue(){
	vat.item.setValueByName("#F.status","SIGNING"); 
	vat.item.setValueByName("#F.enable","N"); 

}

function doUnlock()
{
	vat.item.setStyleByName("#B.doSelectSubmit","display", "inline");
	vat.item.setStyleByName("#B.unlock","display", "none");
}
function doSelectSubmit()
{
	var lock9900 = vat.item.getValueByName("#F.9900isLock");
	var lock6200 = vat.item.getValueByName("#F.6200isLock");
	var vDoWarehouse = document.forms[0]["#userType"].value;
	var force    = false;
	
	
	var alertMessage ="是否確定送出?";
	if(confirm(alertMessage))
	{
		if((lock9900 === "N" && vDoWarehouse === "9900")||(lock6200 === "N" && vDoWarehouse === "6200")){
			var alertMessage2 ="目前訂貨單送出功能並未鎖定，是否繼續執行送出?";
			if(confirm(alertMessage2)){
				force = true;
			}
		}
		else{
			force = true;
		}
		if(force === true){
				vat.ajax.XHRequest({
				post:"process_object_name=poEosService&process_object_method_name=updateAllDetailStatus"
					  +"&categoryItem" 	 	 + "=" + vat.item.getValueByName("#F.categoryItem") 
					  +"&orderNo" 			 + "=" + vat.item.getValueByName("#F.orderNo")
	                  +"&warehouseCode" 	 + "=" + vat.item.getValueByName("#F.warehouseCode") 
	                  +"&shopCode"       	 + "=" + vat.item.getValueByName("#F.shopCode") 	                  
	                  +"&status" 			 + "=" + vat.item.getValueByName("#F.status") 
	                  +"&isTax" 			 + "=" + vat.item.getValueByName("#F.isTax") 
	                  +"&brandCode"			 + "=" + document.forms[0]["#loginBrandCode"].value
	                  +"&startDate"			 + "=" + vat.item.getValueByName("#F.startDate")
	                  +"&endDate" 			 + "=" + vat.item.getValueByName("#F.endDate"),

		
				find: function change(oXHR){
						vat.item.setValueByName("#F."+vDoWarehouse+"isLock", vat.ajax.getValue("lock"+vDoWarehouse, oXHR.responseText));
		         		vat.block.pageRefresh(vnB_Detail8);
		         		alert('批次明細送出完成!並自動解除鎖定');
				},
				fail: function changeError(){
						vat.block.pageRefresh(vnB_Detail8);
						alert('送出錯誤!');
				}  
		       });
		
		}
		
	}
}
function defaultValue(){
	vat.item.setValueByName("#F.status","SIGNING"); 
	vat.item.setValueByName("#F.enable","N"); 

}