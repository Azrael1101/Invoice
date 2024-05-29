/**
 * 	商品序號
 */
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){
	formInitial();
	buttonLine();
	headerInitial();
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0,"xTab1","明細資料"   ,"vatItemDiv"                   ,"images/tab_item_data_dark.gif"            ,"images/tab_item_data_light.gif" , false, "doPageRefresh()");
	}
	detailInitial();
	doFormAccessControl();           
}

function formInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	 loginBrandCode	    = document.forms[0]["#loginBrandCode"    ].value;  	
  	 loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" ].value;	  
	 formId             = document.forms[0]["#formId"            ].value;
	 itemPicker         = document.forms[0]["#itemPicker"        ].value;
	 vat.bean().vatBeanOther = {
		loginBrandCode:		loginBrandCode,					
	  	loginEmployeeCode : loginEmployeeCode,
	    formId :			formId,
	    itemPicker : itemPicker
	 };
     vat.bean.init(function(){
		return "process_object_name=imItemAction&process_object_method_name=performSerialInitial"; 
     },{other: true});
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
	 	{items:[{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_Item:searchSerial:20100404.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 servicePassData:function(){ return doPassData("buttonLine");},   
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.import" 	   , type:"IMG"    ,value:"明細匯入",   src:"./images/button_detail_import.gif" , eClick:'importFormData()'},
	 			{name:"#B.export" 	   , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:'exportFormData()'},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){
	
	vat.block.create(vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"商品序號維護作業", rows:[  
			{row_style:"", cols:[
			 	{items:[{name:"#L.itemCode", type:"LABEL" , value:"品號"}]},	 
			 	{items:[{name:"#F.itemCode", type:"TEXT",  bind:"itemCode", mode:"READONLY", size:40},
			 			{name:"#F.formId"   , type:"TEXT"  ,  bind:"formId", back:false, size:10, mode:"HIDDEN" },
			 		 	{name:"#F.itemId"   , type:"TEXT"  ,  bind:"itemId", back:false, size:10, mode:"HIDDEN" }]},	
			 	{items:[{name:"#L_brandCode", type:"LABEL" , value:"品牌"}]},
			 	{items:[{name:"#F.brandCode", type:"TEXT"  ,  bind:"brandCode", size:8, mode:"HIDDEN"},
			 			{name:"#F.brandName", type:"TEXT"  ,  bind:"brandName", size:8, mode:"READONLY"}]}  
			]},
			{row_style:"", cols:[
				{items:[{name:"#L_createdBy", type:"LABEL", value:"填單人員"}]},
				{items:[{name:"#F.createdBy" , type:"TEXT",   bind:"createdBy",  mode:"HIDDEN", size:12},
			 			{name:"#F.createdByName" , type:"TEXT",   bind:"createdByName",  mode:"READONLY", size:12}]},	 
				{items:[{name:"#L_creationDate" , type:"LABEL", value:"填單日期"}]},
		 		{items:[{name:"#F.creationDate" , type:"TEXT",   bind:"creationDate", mode:"READONLY", size:12}]}
			]}		
		 
		],	 
		 beginService:"",
		 closeService:""			
	});
}

// 序號
function detailInitial() {
	
	vat.item.make(vnB_Detail, "indexNo", {type:"IDX", desc:"序號"}); 
	vat.item.make(vnB_Detail, "isUsed", {type:"CHECKBOX", size:1, desc:"是否使用過", mode:"READONLY"});      
	vat.item.make(vnB_Detail, "serial", {type:"TEXT", size:20, maxLen:20 ,alter:true, desc:"序號"});    
	
	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});
	vat.item.make(vnB_Detail, "isLockRecord", {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});                                         
	vat.item.make(vnB_Detail, "isDeleteRecord", {type:"DEL", desc:"刪除"});                                                          
	vat.item.make(vnB_Detail, "message", {type:"MSG", desc:"訊息"});    
	                                                              
	vat.block.pageLayout(vnB_Detail, {	id: "vatItemDiv",
							pageSize: 10,
	                        canGridDelete: true,
							canGridAppend: true,
							canGridModify: true,														
						    appendBeforeService : "appendBeforeService()",
						    appendAfterService  : "appendAfterService()",
							loadBeforeAjxService: "loadBeforeAjxService()",
							loadSuccessAfter    : "loadSuccessAfter()",
							eventService        : "changeRelationData",   
							saveBeforeAjxService: "saveBeforeAjxService()",
							saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1); 
}

function doPageRefresh(){
    vat.block.pageSearch(vnB_Detail);
}
// 載入明細後
function loadSuccessAfter(){
	// alert("載入成功");	
}

// 新增空白頁
function appendBeforeService(){
	// return confirm("你確定要新增嗎?"); 
	return true;
}

// 新增空白頁之後
function appendAfterService(){
	// return alert("新增完畢");
}

//	LINE資料 第一次載入或更新後的讀取
function loadBeforeAjxService() {
	
	var processString = "process_object_name=imItemService&process_object_method_name=getAJAXSerialPageData" +
			"&itemId=" + vat.item.getValueByName("#F.itemId");
	return processString;
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=imItemService&process_object_method_name=updateAJAXSerialPageLinesData" + 
						"&itemId=" + vat.item.getValueByName("#F.itemId")+ 
						"&itemCode=" + vat.item.getValueByName("#F.itemCode")+ 
						"&brandCode=" + vat.item.getValueByName("#F.brandCode")+ 
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode;
		
	}
	return processString;
}

// 取得存檔成功後要執行的JS FUNCTION
function saveSuccessAfter() {
}

function doSubmit(formAction){

	if(confirm("是否確定送出?")){
//		var formId = vat.item.getValueByName("#F.formId").replace(/^\s+|\s+$/, '');;
//	    var itemId = vat.item.getValueByName("#F.itemId");
	    
		if(formAction == "SUBMIT"){
			vat.block.pageDataSave(vnB_Detail,{  
				funcSuccess:function(){
					vat.bean().vatBeanOther.formAction = formAction;
					vat.bean().vatBeanOther.loginEmployeeCode = document.forms[0]["#loginEmployeeCode" ].value;
						  				
					vat.block.submit(
						function(){return "process_object_name=imItemAction"+
							"&process_object_method_name=performSerialTransaction";
						},{
							bind:true, link:true, other:true,
							funcSuccess:function(){
								vat.block.pageRefresh(vnB_Detail);
							}
						}
					);
				}
		   	});
	    }
    } 
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {

	return true ;
}

// 明細匯入
function importFormData(){
	var exportBeanName = "IM_ITEM_SERIAL";
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/fileUpload:standard:2.page" +
		"?importBeanName=" + exportBeanName +
		"&importFileType=XLS" +
        "&processObjectName=imItemService" + 
        "&processObjectMethodName=executeImportSerial" +
        "&arguments=" + vat.item.getValueByName("#F.itemId") +"{$}" +
        			    vat.bean().vatBeanOther.loginEmployeeCode +
        "&parameterTypes=LONG{$}STRING" +
        "&blockId="+ vnB_Detail,
		"FileUpload",
		'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 匯出
function exportFormData(){
	var exportBeanName = "IM_ITEM_SERIAL";
    var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=" + exportBeanName +
              "&fileType=XLS" + 
              "&processObjectName=imItemService" + 
              "&processObjectMethodName=findByItemId" + 
              "&gridFieldName=imItemSerials" + 
              "&arguments=" + vat.item.getValueByName("#F.itemId") + 
              "&parameterTypes=LONG";
    
    var width = "200";
    var height = "30";
    window.open(url, '商品序號匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "buttonLine":
			var itemId = vat.item.getValueByName("#F.itemId"); 
			suffix += "&itemId="+escape(itemId); 
			break;
	}
	return suffix;
}

// 供應商picker 回來執行
function doAfterPickerFunctionProcess(){
	//do picker back something
	if(typeof vat.bean().vatBeanPicker.result != "undefined"){
    	vat.item.setValueByName("#F.addressBookId", vat.bean().vatBeanPicker.result[0].addressBookId); 
		changeSupplierName("addressBookId");
	}
}

// 定變價 line picker 回來執行
function doAfterPickerLineFunctionProcess(id){
	if( typeof vat.bean().vatBeanPicker.result != "undefined" ){
//		if( vat.bean().vatBeanPicker.result[0].priceId != "" ){
//			alert( "vat.bean().vatBeanPicker.result.length =" + vat.bean().vatBeanPicker.result.length);
//			alert("doAfterPickerLineFunctionProcess before");
			vat.block.pageSearch(vnB_Detail);
//			alert("doAfterPickerLineFunctionProcess after");
//			onChangeItemCode(vat.bean().vatBeanPicker.result[0].priceId, id);
//		}
	}
}


// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(typeof vat.bean().vatBeanPicker.result != "undefined"){
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

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 送出後更新
function createRefreshForm(itemId){
	refreshForm(itemId);
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

// 刷新頁面
function refreshForm(vsHeadId){
	document.forms[0]["#formId"            ].value = vsHeadId;       	
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"            ].value;	
	vat.block.submit(
		function(){
			return "process_object_name=imItemAction&process_object_method_name=performSerialInitial"; 
     	},{other      : true, 
     		funcSuccess:function(){
 		   		vat.item.bindAll();
 		   		vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
 		             
 		      	doFormAccessControl();
     	}});
	

}

// 依狀態鎖form
function doFormAccessControl(){
	var formId = vat.bean().vatBeanOther.formId;
 	var itemPicker = vat.bean().vatBeanOther.itemPicker;
	// 初始化
	//======================<detail>=============================================
	vat.block.canGridModify([vnB_Detail], true,true,true);
	//=======================<buttonLine>========================================
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.import", 	"display", "inline");
	//===========================================================================
	
	if( formId != null && formId != 0 ){ //查詢出來
		if(itemPicker == "true"){ // 從商品主檔點開的
			vat.item.setStyleByName("#B.search", 	"display", "none");
		}else{ // 一般查詢回來
		}
	}
//	else{
//		// 建立新明細資料
//		vat.item.setStyleByName("#B.submit", 	"display", "inline"); 
//		vat.block.canGridModify([vnB_Detail], false,false,false);
//	}
	
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
