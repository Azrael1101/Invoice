/*** 
 *	檔案: ImAdjustmentCustomsExtention.js
 *	說明: 報單展延
 */
 
//var afterSavePageProcess = "";

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;



function outlineBlock(){
 
  formInitial();
  buttonLine();
  headerInitial();
  if(typeof vat.tabm != 'undefined') {
 	    vat.tabm.createTab(0, "vatTabSpan", "L", "float");
		vat.tabm.createButton(0, "xTab2", "明細資料", "vnB_Detail",   "images/tab_detail_data_dark.gif",   "images/tab_detail_data_light.gif");
		
	}
  detailInitial();
//  doFormAccessControl();
}

// 搜尋初始化
function formInitial(){ 
 //alert("formInitial");
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     	: document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" 	].value,	
  	     orderTypeCode			: document.forms[0]["#orderTypeCode"        ].value,
  	     formId           		: document.forms[0]["#formId"            	].value,
  	     processId          	: document.forms[0]["#processId"         	].value,
  	     assignmentId       	: document.forms[0]["#assignmentId"      	].value,
  	     special                : document.forms[0]["#special"              ].value,
  	     currentRecordNumber	: 0,
	     lastRecordNumber   	: 0
  	     };
	    
		vat.bean.init(	
	  		function(){
				return "process_object_name=imAdjustmentHeadAction&process_object_method_name=performExtentionInitial"; 
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
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.search"	   , type :"PICKER", value : "查詢", src : "./images/button_find.gif", 
									   openMode : "open",
					 				   service : "Im_GeneralAdjustment:search:20161020.page",
					 				   servicePassData:function(x){ return doPassHeadData(x); }, 
					 				   left : 0, right : 0, width : 1024, height : 768,	
					 				   serviceAfterPick : function(){doAfterPickerProcess();}
					 				   },	 			
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.import"      , type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  ,
	 									 openMode:"open",
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(x){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,
	 									 serviceBeforePick:function(){ return doBeforePick(); },
	 									 serviceAfterPick:function(){afterImportSuccess();}},
				{name:"#B.first"       	, type:"IMG"   ,value:"第一筆", 	src:"./images/play-first.png", 		eClick:"gotoFirst()"},
	 			{name:"SPACE"          	, type:"LABEL" ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"   ,value:"上一筆", 	src:"./images/play-back.png", 		eClick:"gotoForward()"},
	 			{name:"#B.next"        	, type:"IMG"   ,value:"下一筆",  src:"./images/play.png", 			eClick:"gotoNext()"},
	 			{name:"SPACE"          	, type:"LABEL" ,value:"　"},
		 		{name:"#B.last"    		, type:"IMG"   ,value:"最後一筆",	src:"./images/play-forward.png" , 	eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
}

// 可搜尋的欄位
function headerInitial(){

	var allCustomsWarehouseCodes = vat.bean("allCustomsWarehouseCodes");
	
	var extentionTimeType = [["", " ", true], ["月", "天"], ["month", "day"]];

	var isSpecial = vat.bean("isSpecial");
	
vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"報關單展延作業", rows:[  
	 		{row_style:"", cols:[
				{items:[{name:"#L.orderTypeCode", 	type:"LABEL", 	value:"單別"}]},
				{items:[{name:"#F.orderTypeCode",   type:"TEXT", 	bind:"orderTypeCode", mode:"READONLY"},
				        {name:"#L.isSpecial", 	    type:"LABEL",   value:isSpecial=="Y"?"特殊展延單":"一般展延單"}]},
				{items:[{name:"#L.orderNo",         type:"LABEL", 	value:"單號"}]},
				{items:[{name:"#F.orderNo",         type:"TEXT",   	bind:"orderNo",    size:20, mode:"READONLY", 	back:false},
	 		 			{name:"#F.headId",          type:"TEXT",   	bind:"headId",     size:10, mode:"READONLY",   	back:false }]},
	 			{items:[{name:"#L.createBy", 		type:"LABEL", 	value:"建檔人員"}]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
						{name:"#F.createByName",	type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.Status",          type:"LABEL",   value:"狀態"}]},	
				{items:[{name:"#F.status",          type:"TEXT",    bind:"status",     size:12, mode:"HIDDEN"},
	  					{name:"#F.statusName",      type:"TEXT",    bind:"statusName",          mode:"READONLY",  back:false}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.creationDate",	type:"LABEL", 	value:"建檔日期"}]},
				{items:[{name:"#F.creationDate", 	type:"TEXT", 	bind:"creationDate",	mode:"READONLY"}]},				
				{items:[{name:"#L.remark1", 		type:"LABEL", 	value:"備註" }]},
				{items:[{name:"#F.remark1", 		type:"TEXT", 	size:60,    bind:"remark1" }]},
				{items:[{name:"#L.fileNo", 			type:"LABEL", 	value:"文號" }]},
				{items:[{name:"#F.fileNo", 			type:"TEXT",    size:60, 	bind:"fileNo" }], td:" colSpan=4"}
			
			]},		
			{row_style:"", cols:[
				{items:[{name:"#L.customsWarehouseCode",   type:"LABEL", 	 value:"關別"}]},
				{items:[{name:"#F.customsWarehouseCode",   type:"TEXT", 	 bind:"customsWarehouseCode",                        mode:"READONLY"}]},
				{items:[{name:"#L.extentionTime",          type:"LABEL", 	 value:"展延時間<font color='red'>*</font>"}]},
				{items:[{name:"#F.extentionTime",          type:"TEXT",   	 bind:"extentionTime",    size:20,                                                 mode:"Y"==isSpecial?"":"READONLY", eChange:"changeTime()" },
	 		 			{name:"#F.extentionTimeType",      type:"SELECT",    bind:"extentionTimeType",     size:10,          init:extentionTimeType,           mode:"Y"==isSpecial?"":"READONLY", eChange:"changeTime()"}], td:" colSpan=5"},
	 		 	{items:[{name:"#F.isSpecial", 	           type:"TEXT", 	 bind:"isSpecial",	                             init:isSpecial,                   mode:"HIDDEN"}]}
			]}
		], 	 
		beginService:"",
		closeService:""			
	});
		
//	doFormAccessControl2();
	

}

/* 畫出明細與定義明細欄位 */
function detailInitial(){
  //	var headId  = vat.item.getValueByName("#F.headId");
  	
  	var vbCanGridDelete = true;
  	var vbCanGridAppend = true;
  	var vbCanGridModify = true;
	
	vat.item.make(vnB_Detail, "indexNo"         	  , {type:"IDX"  , desc:"序號"});
    vat.item.make(vnB_Detail, "originalDeclarationNo" , {type:"TEXT" , size:15, mode:"READONLY", desc:"報關單號"});
	vat.item.make(vnB_Detail, "originalDeclarationSeq", {type:"TEXT" , size:3,  mode:"READONLY", desc:"項次"});
	vat.item.make(vnB_Detail, "reserve2"              , {type:"TEXT" , size:15, mode:"READONLY", desc:"原D8報關單號"});
	vat.item.make(vnB_Detail, "reserve3"			  , {type:"TEXT" , size:3,  mode:"READONLY", desc:"原D8報關單項次"});
	vat.item.make(vnB_Detail, "customsItemCode"    	  , {type:"TEXT" , size:18, mode:"READONLY", desc:"品號"});
	vat.item.make(vnB_Detail, "itemCName"    		  , {type:"TEXT" , size:22, mode:"READONLY", desc:"品名"});	
	vat.item.make(vnB_Detail, "orgImportDate"    	  , {type:"DATE" , size:12, mode:"READONLY", desc:"進倉日"});
	vat.item.make(vnB_Detail, "expiryDate"			  , {type:"DATE" , size:12, mode:"READONLY", desc:"屆期日" });
	vat.item.make(vnB_Detail, "extensionDate"		  , {type:"DATE" , size:12, mode:"READONLY", desc:"展延日期"});
	vat.item.make(vnB_Detail, "unit" 				  , {type:"TEXT" , size:3,  mode:"READONLY", desc:"單位"});
	vat.item.make(vnB_Detail, "qty"  	    		  , {type:"NUMM" , size:10, mode:"READONLY", desc:"原進倉數量"});
	vat.item.make(vnB_Detail, "reserve1"      		  , {type:"NUMM" , size:10, mode:"READONLY", desc:"存貨數量"});
	vat.item.make(vnB_Detail, "lineId"				  , {type:"ROWID", view:"none"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vnB_Detail",
														pageSize            : 10,
														canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadSuccessAfter    : "loadSuccessAfter()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														loadBeforeAjxService: "loadBeforeAjxService()",	
														saveSuccessAfter    : "saveSuccessAfter()"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);

}

/* 取得存檔成功後要執行的JS FUNCTION */
function saveSuccessAfter() {
	//alert("saveSuccessAfter");
}

// 載入成功後
function loadSuccessAfter(){
	//alert("loadSuccessAfter");
	
}

function afterImportSuccess(){
	refreshHeadData();
}

function refreshHeadData(){
	alert("refreshHeadData");
	
	var vsHeadId = vat.item.getValueByName("#F.headId") ;
	var vsNewCmDeclarationItemCustomsWarehouseCode = "";
	var processString = "process_sql_code=FindTheFirstCmDeclarationItemCustomsWarehouseCode&headId="+vsHeadId;
	vat.ajax.startRequest(processString,  function() {
		if (vat.ajax.handleState()){
			if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
				vsNewCmDeclarationItemCustomsWarehouseCode = vat.ajax.getValue("CUSTOMS_WAREHOUSE_CODE", vat.ajax.xmlHttp.responseText);
				vat.item.setValueByName("#F.customsWarehouseCode", vsNewCmDeclarationItemCustomsWarehouseCode);	
			}
		}
	} );

	
}


function changeTime(){
	
	var headId = vat.item.getValueByName("#F.headId");
	var extentionTime = vat.item.getValueByName("#F.extentionTime") ;
	var extentionTimeType = vat.item.getValueByName("#F.extentionTimeType") ;
	
//	alert("時間被改變" + headId +" ; "+ extentionTime + " ; " + extentionTimeType);
//	
//	var processString = "process_object_name=imAdjustmentHeadService&process_object_method_name=changeExtentionTime";
	
	
	if(headId!=="" &&  extentionTime!== "" && extentionTimeType!==""){
		alert("展延時間改變，請記得重新匯入明細");
//		vat.ajax.XHRequest(
//       {
//           post:"process_object_name=imAdjustmentHeadService"+
//                    "&process_object_method_name=changeExtentionTime"+
//                    "&headId=" + headId+
//                    "&extentionTime=" + extentionTime+
//                    "&extentionTimeType=" + extentionTimeType,
//           find: function change(oXHR){ 
//				alert("後端返回成功");
//
//				
//           }   
//       });
	
	}else{
//		alert("資料有缺,不進後端");
	}
	

}

// 查詢點下執行
function loadBeforeAjxService(){
	//alert("loadBeforeAjxService");
    var processString = "process_object_name=imAdjustmentHeadService&process_object_method_name=getAJAXExtentionPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +
                      "&headId="                +vat.item.getValueByName("#F.headId");
               
	return processString;											
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
	
		processString = "process_object_name=imAdjustmentHeadService&process_object_method_name=updateAJAXExtentionPageLinesData" + 
						"&headId="        		+ document.forms[0]["#formId" ].value  + 
						"&status="        		+ vat.item.getValueByName("#F.status") +
						"&orderTypeCode="		+ vat.item.getValueByName("#F.orderTypeCode") ;
						vat.ajax.startRequest(processString, function (){
												
			});
	//}
	return processString;
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

// 送出的返回
function createRefreshForm(){
    if(confirm("是否確定建立新資料？")){
        vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;
    	refreshForm("");
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

/* 按下單據送出 */
function doSubmit(formAction){
	//alert("doSubmit " + formAction);
	
		var alertMessage ="是否確定送出?";
 	
	if("SUBMIT" == formAction){	
		
	    alertMessage = "是否確定送出?";	    					
	}else if ("SAVE" == formAction){
		formStatus = "SAVE";
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
		formStatus = "VOID";
	 	alertMessage = "是否確定作廢?";
	}else if ("EXPORT" == formAction){
		formStatus = "EXPORT";
	 	alertMessage = "是否確定匯出作業?";
	}else if ("IMPORT" == formAction){
		formStatus = "IMPORT";
	 	alertMessage = "是否確定匯入作業?";
	}
	
	if(confirm(alertMessage)){
	
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var assignmentId          = vat.item.getValueByName("#assignmentId");
		var approvalResult        = vat.item.getValueByName("#F.approvalResult");
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
		
		if(status == "SAVE" || status == "FINISH" ){
			vat.bean().vatBeanOther.formAction = formAction;
			vat.block.pageDataSave(vnB_Detail,
				{  
		 			funcSuccess:function()
		 			{
					vat.bean().vatBeanOther.formAction 		= formAction;
		  			vat.bean().vatBeanOther.beforeStatus	= status;
		  			vat.bean().vatBeanOther.processId       = processId;
	  				vat.bean().vatBeanOther.approvalResult  = approvalResult;
	  				vat.bean().vatBeanOther.approvalComment =  approvalComment;
						
			
			
			vat.block.submit(function(){
				return "process_object_name=imAdjustmentHeadAction"+
				             "&process_object_method_name=performExtentionTransaction";},
				{
					bind:true, link:true, other:true 				
				}
		);
		}
				});
		}
	}
}

// 特殊展延單,選取picker前的click
function doBeforePick(){

	var special = vat.item.getValueByName("#F.isSpecial");
	
	
	//判別"不是"特殊單
	if(!"Y"==special){
		alert("這是一般展延單");
		return true;
	}else{
	
		var extentionTime = vat.item.getValueByName("#F.extentionTime").replace(/^\s+|\s+$/, '');
		var extentionTimeType = vat.item.getValueByName("#F.extentionTimeType");
		
		if("" === extentionTime){
			alert("未輸入時間");
			return false;
		}else if("month" === extentionTimeType){
			alert("欄位驗證測試:展延時間為"+extentionTime+"月份");
			return true;
		}else if("day"   === extentionTimeType){
			alert("欄位驗證測試:展延時間為"+extentionTime+"天數");
			return true;
		}else{
			alert("未選擇時間類別");
			return false;
		}
	}
	return false;
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	//alert("doAfterPickerProcess");
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

// 刷新頁面
	function refreshForm(code){	
	//alert("refreshForm");
		document.forms[0]["#formId"            ].value = code;
		//vat.item.setValueByName("#F.headId", code);
		document.forms[0]["#processId"         ].value = "";
		document.forms[0]["#assignmentId"      ].value = "";
		vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;
		vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"].value;
		vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"].value;
				
		vat.block.submit(
			function(){
				return "process_object_name=imAdjustmentHeadAction&process_object_method_name=performExtentionInitial";
     		},{other      : true,
     	   	funcSuccess:function(){
     				vat.item.bindAll();
					vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
					doFormAccessControl();				
	     	}});	
	}		


/* 明細匯出 */
function exportFormData(){

    var headId = vat.item.getValueByName("#F.headId");
    var brandCode =  vat.bean().vatBeanOther.loginBrandCode ;
    var url = "/erp/jsp/ExportFormView.jsp" + 
              "?exportBeanName=IM_CUSTOMSEXTENTION_EXPORT" + 
              "&fileType=XLS" + 
              "&processObjectName=imAdjustmentHeadService" + 
              "&processObjectMethodName=exportExcelDetail" + 
              "&gridFieldName=imAdjusrmentLines" + 
              "&headId=" + headId + 
              "&brandCode=" + brandCode ;
    
    var width = "200";
    var height = "30";
    window.open(url, '報單展延明細匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

/* 查詢 */
function doPassHeadData(x){
  var suffix = "";
  var isSpecial = vat.item.getValueByName("#F.isSpecial");
  suffix += "&isSpecial="+escape(isSpecial);

  return suffix;
}

/* 明細匯入 */
function importFormData(){
    
    //alert("匯入");
    var headId = vat.item.getValueByName("#F.headId");
    
    //特殊展延單
    var extentionTime = vat.item.getValueByName("#F.extentionTime");
	var extentionTimeType = vat.item.getValueByName("#F.extentionTimeType");
	var special = vat.item.getValueByName("#F.isSpecial");
    
//   	var suffix = 
//			"&importBeanName=IM_CUSTOMSEXTENTION_IMPORT" +
//			"&importFileType=XLS" +
//        	"&processObjectName=imAdjustmentHeadService" + 
//        	"&processObjectMethodName=executeImportExtentionLine" +
//        	"&arguments=" + headId  +
//        	"&parameterTypes=LONG" +
//        	"&blockId=" + vnB_Detail;
	
	var suffix = 
			"&importBeanName=IM_CUSTOMSEXTENTION_IMPORT" +
			"&importFileType=XLS" +
        	"&processObjectName=imAdjustmentHeadService" + 
        	"&processObjectMethodName=executeImportExtentionLine";
        	
    
    //特殊展延單 , headId、extentionTime、extentionTimeType、special
    if("Y"!==special){
    	suffix += "&arguments=" + headId  + "{$}"  + "12"   +  "{$}"  +  "month"   +  "{$}"  +    "N"     +
        		  "&parameterTypes=LONG"  + "{$}"  + "INT"  +  "{$}"  +  "STRING"  +  "{$}"  +  "String"  +
        		  "&blockId=" + vnB_Detail;
    }else{
        suffix += "&arguments=" + headId  + "{$}"  + extentionTime  +  "{$}"  +  extentionTimeType  +  "{$}"  +    "Y"     +
        		  "&parameterTypes=LONG"  + "{$}"  +     "INT"      +  "{$}"  +       "STRING"      +  "{$}"  +  "String"  +
        		  "&blockId=" + vnB_Detail;
    }   
    
		return suffix;
}

/* 頁面顯示控制 */
function doFormAccessControl(){
	var processId 		= document.forms[0]["#processId"].value;
	var formStatus    	= vat.item.getValueByName("#F.status");
   	var orderNo       	= vat.item.getValueByName("#F.orderNo");    
 	
 	if(orderNo.indexOf("TMP") != -1){
		vat.item.setAttributeByName("vnB_Header", "readOnly", false, true, true);
		
		vat.item.setStyleByName("#B.void"		, "display", "none");
		vat.item.setStyleByName("#B.submit"		, "display", "inline");
		vat.item.setStyleByName("#B.save"		, "display", "inline");
		vat.item.setStyleByName("#B.export" 	, "display", "inline");
		vat.item.setStyleByName("#B.import"		, "display", "inline");
		//vat.block.canGridModify( [vnB_Detail], true, true, true );
	}else{
		vat.item.setAttributeByName("vnB_Header", "readOnly", true, true, true);
		vat.item.setAttributeByName("#F.fileNo", "readOnly", true);
		vat.item.setAttributeByName("#F.remark1", "readOnly", true);
		vat.item.setAttributeByName("#F.customsWarehouseCode", "readOnly", true);
		vat.item.setAttributeByName("#F.extentionTime", "readOnly", true);
		vat.item.setAttributeByName("#F.extentionTimeType", "readOnly", true);
		
		vat.item.setStyleByName("#B.submit"		, "display", "none");
		vat.item.setStyleByName("#B.save"		, "display", "none");
		vat.item.setStyleByName("#B.void"		, "display", "none");
		vat.item.setStyleByName("#B.export"	    , "display", "none");
		vat.item.setStyleByName("#B.import"		, "display", "none");
		//vat.block.canGridModify( [vnB_Detail], false, false, false);
	}
		vat.block.pageRefresh(vnB_Detail);	 
}
