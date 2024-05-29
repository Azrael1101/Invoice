 /*** 
 *	檔案: imTransfer.js
 *	說明：追加單專案-後台維護作業
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Amount = 2;
var vnB_Shop_All = 3;

function outlineBlock(){
	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","追加明細資料" ,"vatAmountDiv"        	,"images/tab_item_data_dark.gif"      	,"images/tab_item_data_light.gif", false);
	}
	amountInitial();
}

// 初始化
function formInitial(){  
		if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' && document.forms[0]["#formId"].value != '[binding]'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,          
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId        		: document.forms[0]["#formId"].value,
          orderNo        		: document.forms[0]["#orderNo"].value,
          processId             : document.forms[0]["#processId"].value,
          assignmentId          : document.forms[0]["#assignmentId"].value,      
          orderTypeCode:"ITF"
         
   
         
         
         
         
         
         
         
         
         
         
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imTransferService&process_object_method_name=executeInitial_transfer1"; 
	    	},{								
	    		other: true
    	});
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
                {name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},	 									 
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.transferPrint"	, type:"IMG"    ,value:"單據列印",   src:"./images/button_print.gif" , eClick:'openReportWindow()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"}, 
                {name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
                {name:"SPACE"          , type:"LABEL"  ,value:"　"}],
                td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
                
               
       
         ],
         beginService:"",
         closeService:"" 
         }); 
}
// 追加單主檔
function headerInitial(){ 
	var itemTypes = vat.bean("itemTypes");
	var dateValue = vat.bean("date");
    var allReasons = vat.bean("allReasons");
    var allWarehouse = vat.bean("allWarehouse");
    var typeCode        = vat.bean("typeCode");
    var orderNo       = vat.bean("orderNo");
    
//var allStatus = [["","",true],
//                 ["暫存中","簽核中","待轉出","待轉入","簽核完成","結案","未確認","作廢"],
//                 ["SAVE","SIGNING","WAIT_OUT","WAIT_IN","FINISH","CLOSE","UNCONFIRMED","VOID"]];
                 
var allStatus = [["","",true],
                 ["待核准","已核准"],                 
                 ["SAVE","FINISH"]];
                
                 
var allSortKey = [["","",false],
                 ["最後更新日","單號","轉出日期","轉入日期","轉出庫別","轉入庫別","來源單別","來源單號","件數","狀態"],
                 ["lastUpdateDate","orderNo","deliveryDate","arrivalDate","deliveryWarehouseCode","arrivalWarehouseCode","originalOrderTypeCode","originalOrderNo","itemCount","status"]];
var allSortSeq = [["","",false],
                 ["由大到小","由小到大"],
                 ["desc","asc"]];
    
	vat.block.create( vnB_Header= 2, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"追加單後台維護作業", 
		rows:[
			{row_style:"", cols:[
			        {items:[{name:"#L.orderTypeCode", 			type:"LABEL", 	value:"單別<font color='red'>*</font>"}]},
			        {items:[{name:"#F.orderTypeCode", 			type:"Text", 	bind:"orderTypeCode", size:22 , maxLen:15 }]},
			        {items:[{name:"#L.warehouseCodeApply", 		type:"LABEL", 	value:"篩選櫃位<font color='red'>*</font>"}]},	 
		 			{items:[{name:"#F.warehouseCodeApply", 		type:"SELECT",  bind:"warehouseCodeApply",init:allWarehouse }]},
		 		    {items:[{name:"#L.status"                 , type:"LABEL"  , value:"狀態"}]},
		            {items:[{name:"#F.status"                 , type:"SELECT" , bind:"status",  init:allStatus}]},
		 	        {items:[{name:"#L.loginBrandCode",  type:"LABEL",   value:"品牌別<font color='red'>*</font>" }]},
	                {items:[{name:"#F.loginBrandCode",  type:"TEXT",    bind:"loginBrandCode" , size:10 , mode:"READONLY", init:dateValue}]} 				
				]}
				
//				{row_style:"", cols:[
				
//					{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"單號<font color='red'>*</font>"}]},
//					{items:[{name:"#F.orderNo", 			type:"Text", 	bind:"orderNo", size:22 , maxLen:15 }]},
					
//    		        {items:[{name:"#L.applyDate"              , type:"LABEL" , value:"期間日期"}]},
//		            {items:[{name:"#F.applyDate"         , type:"DATE"  ,  bind:"applyDate", size:12  },
//		                    {name:"#L.between"                  , type:"LABEL" , value:"至"},
//               	        {name:"#F.applyDate"           , type:"DATE"  ,  bind:"applyDate", size:12}], td:" colSpan=3"},
                	        
	                 	        
                	        
                	        
                	        
                	        
//					{items:[{name:"#L.applyBy",     type:"LABEL", 	value:"配貨人員<font color='red'>*</font>"}]},
//		 			{items:[{name:"#F.applyBy", 	type:"TEXT",    bind:"applyBy",	size:10 , mode:"READONLY"},
//		 	                {name:"#F.loginEmployeeName",    type:"TEXT",    bind:"loginEmployeeName", mode:"READONLY", size:10}]}
//				]},
//	            {row_style:"", cols:[
//	  				{items:[{name:"#L.reasonApply",        type:"LABEL",   value:"追加原因"}]},
//	                {items:[{name:"#F.reasonApply",        type:"SELECT",    bind:"reasonApply", init:allReasons }]},
//	                {items:[{name:"#L.sortKey"                  , type:"LABEL" , value:"排序欄位"}]},
//		            {items:[{name:"#F.sortKey"                  , type:"SELECT", bind:"sortKey", init:allSortKey}]},
//		            {items:[{name:"#L.sortSeq"    ,        type:"LABEL" ,  value:"排序方向"}]},
//		            {items:[{name:"#F.sortSeq"    ,        type:"SELECT",    bind:"sortSeq",     init:allSortSeq}]},
		            
		            //{items:[{name:"#L.applyDate"      	, type:"LABEL",  value:"填單日期"}]},
 	                //{items:[{name:"#F.applyDate"      	, type:"DATE",   bind:"applyDate", mode:"READONLY", size:12}]}
 	  
		            
//		 			{items:[{name:"#L.applyDate",		type:"LABEL", 	value:"配貨日期<font color='red'>*</font>" }]},
//					{items:[{name:"#F.applyDate",		type:"DATE", 	bind:"applyDate" , size:12 , mode:"READONLY", init:dateValue}]}
					
	            //]}

,
{row_style:"", cols:[
//		 			{items:[{name:"#L.applyDate",		type:"LABEL", 	value:"配貨日期<font color='red'>*</font>" }]},
//					{items:[{name:"#F.applyDate",		type:"DATE", 	bind:"applyDate" , size:12 ,  init:dateValue}]}

					{items:[{name:"#L.orderNo", 			type:"LABEL", 	value:"單號<font color='red'>*</font>"}]},
					{items:[{name:"#F.orderNo", 			type:"Text", 	bind:"orderNo", size:22 ,mode:"READONLY", maxLen:15 }]}




]}


		     ],
		beginService:"",
		closeService:""			
	});
}
function amountInitial(){
    var shiftMode = "shift";
	var vbCanGridDelete = true; 
	var vbCanGridAppend = true; 
	var vbCanGridModify = true; 
	
    vat.item.make(vnB_Amount, "indexNo"                 ,{type:"IDX" ,size:2, view:"fixed",  desc:"序號"       });
    vat.item.make(vnB_Amount, "applyDate"		        ,{type:"TEXT" ,size:7,  view:"fixed", desc:"日期",    mode:"READONLY" });
    vat.item.make(vnB_Amount, "warehouseCodeApply"		,{type:"TEXT" ,size:4,  view:"fixed", desc:"專櫃",    mode:"READONLY" });
	vat.item.make(vnB_Amount, "itemCode"		        ,{type:"TEXT" ,size:13, view:"fixed", desc:"品    號",  mode:"READONLY" });
	vat.item.make(vnB_Amount, "itemName"		        ,{type:"TEXT" ,size:10, view:"fixed",      desc:"簡    稱",  mode:"READONLY" });
	vat.item.make(vnB_Amount, "quantityApply"           ,{type:"NUMB" ,size:2,  view:"",      desc:"追量",    mode:"READONLY" });
	vat.item.make(vnB_Amount, "stockSalesqty"           ,{type:"NUMB" ,size:2,  view:"",      desc:"總銷售",    mode:"READONLY" });
	vat.item.make(vnB_Amount, "stockOnHandqty"          ,{type:"NUMB" ,size:3,  view:"",      desc:"庫存",    mode:"READONLY" });	
	vat.item.make(vnB_Amount, "unCommitqty"             ,{type:"NUMB" ,size:3,  view:"",      desc:"在途",    mode:"READONLY" });	
	vat.item.make(vnB_Amount, "warehouseCodeProcess1"   ,{type:"NUMB", size:3,  view:"", desc:"99",     mode:"READONLY" });		
	vat.item.make(vnB_Amount, "quantityProcess1"        ,{type:"NUMB" ,size:3,  view:"", desc:"核量" ,values:""          });	
	vat.item.make(vnB_Amount, "warehouseCodeProcess2"   ,{type:"NUMB" ,size:3,  view:"", desc:"FG",     mode:"READONLY" });	
	vat.item.make(vnB_Amount, "quantityProcess2"        ,{type:"NUMB" ,size:3,  view:"", desc:"FG核量"                   });		
	vat.item.make(vnB_Amount, "reasonApply"             ,{type:"TEXT" ,size:3,  view:"", desc:"原因",    mode:"READONLY" });
	vat.item.make(vnB_Amount, "remark1"			        ,{type:"TEXT" ,size:3,  view:"", desc:"備   註",  mode:"READONLY" });
    vat.item.make(vnB_Amount, "orderNo"                 ,{type:"TEXT" ,         view:"fixed",size:12, desc:"追加單單號", mode:"HIDDEN"});
    vat.item.make(vnB_Amount, "lineId"                  ,{type:"TEXT" ,         view:"fixed",size:10, desc:"追加單序號", mode:"HIDDEN"});
	vat.block.pageLayout(vnB_Amount, {
														id: "vatAmountDiv", 
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "",//"appendBeforeService()",
														appendAfterService  : "",//"appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Amount, vnCurrentPage = 1);
}

// 第一次載入 重新整理
function loadBeforeAjxService(){
	var processString = "process_object_name=imTransferService&process_object_method_name=getAJAXImTransferSearchPageData" +
	                    "&orderNo=" + vat.item.getValueByName("#F.orderNo") +                        
                       //"&itemCode=" + vat.item.getValueByName("#F.itemCode") +
                        "&loginBrandCode=" + vat.item.getValueByName("#F.loginBrandCode") +
                        "&warehouseCodeApply=" + vat.item.getValueByName("#F.warehouseCodeApply")+
                        "&status=" + vat.item.getValueByName("#F.status");
                        //"&formId=" + vat.bean().vatBeanOther.formId;
                     
		return processString;	
}
function doSearch(){
    vat.block.submit(function(){
    	return "process_object_name=tmpAjaxSearchDataService"+
				"&process_object_method_name=deleteByTimeScope="+ vat.bean().vatBeanOther.timeScope; 
		},{
        	other: true,
         	funcSuccess:
            function() {
				vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
			}
        });
}

// 送出,暫存按鈕
function doSubmit(formAction){


	var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');	
	var approvalResult        = getApprovalResult();
	var alertMessage ="是否確定送出?";
	
	
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}
      
	if(confirm(alertMessage))
	{
	    if("SAVE" == formAction){
	    	afterSavePageProcess = "SAVE";
        }
        
	vat.block.pageDataSave(vnB_Amount,{  funcSuccess:function(){
		vat.block.submit(function(){
				return "process_object_name=imTransferAction"+"&process_object_method_name=performImTransferTransaction";
				
			},{
				bind:true, link:true, other:true ,
				funcSuccess:function(){
						        					vat.block.pageRefresh(vnB_Amount);
						        				}
			}
		);
	
	}} );
		
		
	}
	
	
	
	
}


function getApprovalResult(){
	return "true";
}


// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(){
		if( "SAVE" == afterSavePageProcess ) {
			doActualSubmit("SAVE");
		}else if( "SUBMIT" == afterSavePageProcess ){
			doActualSubmit("SUBMIT");
			var processString = "process_object_name=imTransferService&process_object_method_name=updateAJAXimTransfer" +
								"&quantityProcess1="       + vat.item.getValueByName("#F.quantityProcess1") ;
			vat.ajax.startRequest(processString, function () {
				if (vat.ajax.handleState()) {
					if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
						vat.item.setValueByName("#F.quantityProcess1",    	vat.ajax.getValue("quantityProcess1", vat.ajax.xmlHttp.responseText)) ;
					}
				}
			});
		}
	afterSavePageProcess = "" ;	
} 
// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(){
  		var processString = "process_object_name=imTransferService&process_object_method_name=updateAJAXImTransferlist";
		return processString;	
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
} 

// 新增空白頁
function appendBeforeService(){
	return true;
}    







// 新增空白頁成功後
function appendAfterService(){
} 

function eventService(){
} 
 

// tab切換 存檔
function doPageDataSave(){
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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(code);
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
// 刷新頁面
function refreshForm(code){
	vat.block.submit(
		function(){
			return "process_object_name=imTransferService&process_object_method_name=executeInitial_transfer"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				vat.block.pageDataLoad(vnB_Amount, vnCurrentPage = 1);
				//doFormAccessControl();
     	}});
    }
    
function refreshForm(vsHeadId) {
	document.forms[0]["#processId"].value = "";
	document.forms[0]["#assignmentId"].value = "";

	vat.bean().vatBeanOther.processId = document.forms[0]["#processId"].value;
    	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"].value;
}
    
    
    
function checkQuantity() {
         var vQtyApply = vat.item.getValueByName("#F.quantityApply");
         if (parseInt(vQtyApply, 10)<=0){
            alert("追加數量不可為零,請重新輸入!!");
            vat.form.item.setFocus( "#F.quantityApply" );
            return; 
         }  
}

//讀取品號簡稱
function changeItemData() {
    var vItemCode = vat.item.getValueByName("#F.itemCode");
    var vProcessSqlName = "FindItemInformationTransferLotNo";
    var vProcessString = "process_sql_code=" + vProcessSqlName + "&" + "itemCode=" + vItemCode;
    vat.ajax.XHRequest({
        post: vProcessString,
        find: function change(oXHR) {
        var vitemName = vat.item.getValueByName("#F.itemName");
            if (oXHR.responseText !== "" && oXHR.responseText !== null) {
                 vat.item.setValueByName("#F.itemName", vat.ajax.getValue("ITEM_C_NAME", oXHR.responseText));
            }
            else{
                vat.item.setValueByName("#F.itemName", "查無品號!!");
                alert("品號輸入錯誤,請重新輸入品號!!");
            }
        },
        fail: function () {
            vat.item.setValueByName("#F.itemName", "查無品號!!");
            alert("品號輸入錯誤,請重新輸入品號!!");
        },
        lose: function () {
            vat.item.setValueByName("#F.itemName", "查無品號!!");
            alert("品號輸入錯誤,請重新輸入品號!!");
        }
    });

}

//清除欄位-------------------------------------------------------
function resetForm(){
     vat.item.setValueByName("#F.itemCode", "");
     vat.item.setValueByName("#F.itemName", "");
     vat.item.setValueByName("#F.quantityApply", "");
     //vat.item.setValueByName("#F.reasonApply", "0");
     //vat.item.setValueByName("#F.remark1", "");
     vat.form.item.setFocus( "#F.itemCode" );
}
// 查詢按下後--
function doSearch(){
    vat.block.pageDataLoad(vnB_Amount, vnCurrentPage = 1);
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
// 列印（貨櫃（物）運送單）
function openReportWindow(){ 
  	var brandCode = vat.item.getValueByName("#F.loginBrandCode");
    vat.bean().vatBeanOther.brandCode  = brandCode;
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
	                                     
	
	
	vat.bean().vatBeanOther.reportFileName = "im0107.rpt";
		vat.block.submit(
				function(){return "process_object_name=imTransferService"+
							"&process_object_method_name=getReportConfig";},{other:true,funcSuccess:function()
				                {
				                  eval(vat.bean().vatBeanOther.reportUrl);
				                 }
	                      }
	                    );
}


