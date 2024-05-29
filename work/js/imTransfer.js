 /*** 
 *	檔案: imTransfer.js
 *	說明：1追加單專案
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
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          warehouseCodeApply   	: document.forms[0]["#warehouseCodeApply"            ].value

        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=imTransferService&process_object_method_name=executeInitial_transfer"; 
	    	},{								
	    		other: true
    	});
    	//vat.form.item.setFocus( "#F.warehouseCodeApply" );
    	//changeWHCName();
  	}
}

function buttonLine(){
         var vsMaxRecord     = 0;
         var vsCurrentRecord = 0;
         vat.block.create(vnB_Button, {
         id: "vatBlock_Button", generate: true,
         table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-left-width: 0px; border-color: #fffffd;'",	
	
         
         title:"", rows:[
           {row_style:"", cols:[
              {items:[
                       //{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  ,eClick:"doSearch()",
                                                 //serviceAfterPick:function(){doAfterPickerProcess();}},
                                                 //{name:"SPACE"          , type:"LABEL"  ,value:"　"},
                                                 
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_Transfer:search:20110921.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},

                                                 
                                                 
                                                 
                       {name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
                       {name:"SPACE"          , type:"LABEL"  ,value:"　"}
                       
               ]}
           ]}
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
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='0' class='default' border='0' cellpadding='0'",
		title:"追加單維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseCodeApply", 		type:"LABEL", 	value:"所在櫃位<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.warehouseCodeApply", 		type:"SELECT",  	bind:"warehouseCodeApply",init:allWarehouse }]},
				{items:[{name:"#L.applyBy",     type:"LABEL", 	value:"輸入人員<font color='red'>*</font>"}]},
	 			{items:[{name:"#F.applyBy", 	type:"TEXT",    bind:"applyBy",	size:10 , mode:"READONLY"},
	 	                {name:"#F.loginEmployeeName",    type:"TEXT",    bind:"loginEmployeeName", mode:"READONLY", size:10}]},	
	 	        {items:[{name:"#L.loginBrandCode",  type:"LABEL",   value:"品牌別<font color='red'>*</font>" }]},
                {items:[{name:"#F.loginBrandCode",  type:"TEXT",    bind:"loginBrandCode" , size:10 , mode:"READONLY", init:dateValue}]} 
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.itemCode", 			type:"LABEL", 	value:"品      號<font color='red'>*</font>"}]},
				{items:[{name:"#F.itemCode", 			type:"Text", 	bind:"itemCode", size:22 , maxLen:15,eChange:"changeItemData()" }]},
				{items:[{name:"#F.itemName",		type:"TEXT", bind:"itemName",size:24,maxLen:40, mode:"READONLY"}],td:" colSpan=2"},
				{items:[{name:"#L.quantityApply",          type:"LABEL", 	value:"追加數量<font color='red'>*</font>"}]},
				{items:[{name:"#F.quantityApply",          type:"NUMB", 	bind:"quantityApply", size:5 ,maxLen:5,eChange:"checkQuantity()"}]}
			]},
            {row_style:"", cols:[
  				{items:[{name:"#L.reasonApply",        type:"LABEL",   value:"追加原因"}]},
                {items:[{name:"#F.reasonApply",        type:"SELECT",    bind:"reasonApply", init:allReasons }]},
				{items:[{name:"#L.remark1",     type:"LABEL", value:"備註"}]},
                {items:[{name:"#F.remark1",     type:"TEXT",   bind:"remark1", size:24 }]},
	 			{items:[{name:"#L.applyDate",		type:"LABEL", 	value:"追加日期<font color='red'>*</font>" }]},
				{items:[{name:"#F.applyDate",		type:"DATE", 	bind:"applyDate" , size:12 , mode:"READONLY", init:dateValue}]},
				{items:[{name:"#F.status",          type:"TEXT",    bind:"status" , size:12 , mode:"HIDDEN", init:"SAVE"}]},
			    {items:[{name:"#F.brandCode",          type:"TEXT",    bind:"brandCode" , size:12 , mode:"HIDDEN"}]},
			    
			    {items:[{name:"#F.createdBy",          type:"TEXT",    bind:"createdBy" , size:12 , mode:"HIDDEN"}]},
   			    {items:[{name:"#F.creationDate",          type:"TEXT",    bind:"creationDate" , size:12 , mode:"HIDDEN" ,init:dateValue}]},
			    {items:[{name:"#F.lastUpdateBy",          type:"TEXT",    bind:"lastUpdateBy" , size:12 , mode:"HIDDEN"}]},
			    {items:[{name:"#F.lastUpdateDate",          type:"TEXT",    bind:"lastUpdateDate" , size:12 , mode:"HIDDEN",init:dateValue}]}
			    
			    
			    
			    
			      					
            ]},
             {row_style:"", cols:[
             
             
             
                {items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:'doSubmit("SUBMIT")'}],td:" colSpan=3"},
                {items:[{name:"#B.clear",       type:"IMG",      value:"清除", src:"./images/button_reset.gif", eClick:"resetForm()"}],td:" colSpan=3"}
                
                
                
                
            ]}
	     ],
		beginService:"",
		closeService:""			
	});
}
function amountInitial(){
	var vbCanGridDelete = true; 
	var vbCanGridAppend = true; 
	var vbCanGridModify = true; 
	vat.item.make(vnB_Amount, "indexNo"				, {type:"IDX" , desc:"序號" });
	vat.item.make(vnB_Amount, "applyDate"		, {type:"TEXT" , size:10, desc:"追加日期", mode:"READONLY"});
	vat.item.make(vnB_Amount, "itemCode"		, {type:"TEXT" , size:16, desc:"品    號", mode:"READONLY"});
	vat.item.make(vnB_Amount, "itemName"		, {type:"TEXT" , size:30, desc:"簡    稱", mode:"READONLY" });
	//vat.item.make(vnB_Amount, "warehouseCodeProcess1"		, {type:"TEXT" , size:30, desc:"庫存", mode:"READONLY" });
	vat.item.make(vnB_Amount, "quantityApply"  , {type:"NUMB" , desc:"追加數量", mode:"READONLY" });
	vat.item.make(vnB_Amount, "applyBy"        , {type:"TEXT" , desc:"輸入人員", mode:"READONLY" });
	vat.item.make(vnB_Amount, "reasonApply"    , {type:"TEXT" , size:10, desc:"追加原因", mode:"READONLY" });
	vat.item.make(vnB_Amount, "remark1"			, {type:"TEXT" , size:20, desc:"備   註", mode:"READONLY"});
    //vat.item.make(vnB_Amount, "isDeleteRecord", 			{type:"DEL", desc:"刪除"});
	
	
	vat.item.make(vnB_Amount, "lineId"         , {type:"TEXT" , size:20, desc:"追加單序號", mode:"HIDDEN"});
	vat.item.make(vnB_Amount, "brandCode"         , {type:"TEXT" ,size:10, desc:"品牌", mode:"HIDDEN"});
	vat.item.make(vnB_Amount, "orderTypeCode"         , {type:"TEXT" ,size:10, desc:"單別", mode:"HIDDEN"});
	
	
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
    //alert("loadBeforeAjxService");	
	//alert(vat.item.getValueByName("#F.warehouseCodeApply"));
	var processString = "process_object_name=imTransferService&process_object_method_name=getAJAXPageData" +
                        "&warehouseCodeApply=" + vat.item.getValueByName("#F.warehouseCodeApply") +
                        "&brandCode=" + document.forms[0]["#loginBrandCode" ].value; // HEAD_ID
                        
	
	
		
		return processString;	
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(){
  		var processString = "process_object_name=imTransferService&process_object_method_name=updateAJAXPageLinesData";
		return processString;	
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(){
	//alert('saveSuccessAfter'); 
	//vat.block.pageRefresh(div); 
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
//	alert("eventService");
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


//送出
// 送出,暫存按鈕
function doSubmit(formAction) {
var vsAllowSubmit         = true;
var remark1 = vat.item.getValueByName("#F.remark1").replace(/^\s+|\s+$/, '');
    //var alertMessage ="是否確定送出?";
    checkQuantity();
    //if(confirm(alertMessage)){
    vat.bean().vatBeanOther.formAction = formAction;
    vat.block.submit(function () {
        return "process_object_name=imTransferAction" + "&process_object_method_name=performTransaction1";
    }, {
        bind: true,
        link: true,
        other: true, 
           funcSuccess:function(){
               resetForm();
               vat.block.pageDataLoad(vnB_Amount, vnCurrentPage = 1);
          }}
    );
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
	//document.forms[0]["#formId"            ].value = code; 
	//document.forms[0]["#processId"         ].value = "";   
	//document.forms[0]["#assignmentId"      ].value = "";    
	//vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	//vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	//vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
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

function checkQuantity() {
         var vQtyApply = vat.item.getValueByName("#F.quantityApply");
         if (parseInt(vQtyApply, 10)<=0){
            alert("追加數量不可為零,請重新輸入!!")
            vat.form.item.setFocus( "#F.quantityApply" );
            return; 
         }  
}

//讀取品號簡稱
function changeItemData() {
    var vItemCode = vat.item.getValueByName("#F.itemCode");
    //alert(vItemCode);
    var vProcessSqlName = "FindItemInformationTransferLotNo";
    //alert(vProcessSqlName);
    var vProcessString = "process_sql_code=" + vProcessSqlName + "&" + "itemCode=" + vItemCode;
    vat.ajax.XHRequest({
        post: vProcessString,
        find: function change(oXHR) {
        var vitemName = vat.item.getValueByName("#F.itemName");
        var vitemName = vat.item.getValueByName("#F.itemName");
                var vitemName = vat.item.getValueByName("#F.itemName");
            if (oXHR.responseText != "" && oXHR.responseText != null) {
                 vat.item.setValueByName("#F.itemName", vat.ajax.getValue("ITEM_C_NAME", oXHR.responseText));
                 
            }
            else{
                vat.item.setValueByName("#F.itemName", "查無品號!!");
                alert("品號輸入錯誤,請重新輸入品號!!")
            }
        },
        fail: function () {
            vat.item.setValueByName("#F.itemName", "查無品號!!");
            alert("品號輸入錯誤,請重新輸入品號!!")
        },
        lose: function () {
            vat.item.setValueByName("#F.itemName", "查無品號!!");
            alert("品號輸入錯誤,請重新輸入品號!!")
        }
    });

}

//讀取櫃號中文簡稱



//清除欄位-------------------------------------------------------
function resetForm(){
     vat.item.setValueByName("#F.itemCode", "");
     vat.item.setValueByName("#F.itemName", "");
     vat.item.setValueByName("#F.quantityApply", "");
     //vat.item.setValueByName("#F.reasonApply", "0");
     //vat.item.setValueByName("#F.remark1", "");
     vat.form.item.setFocus( "#F.itemCode" );
}

//---------------------------------------------------------------

// 查詢按下後
function doSearch(){
    vat.block.pageDataLoad(vnB_Amount, vnCurrentPage = 1);
}