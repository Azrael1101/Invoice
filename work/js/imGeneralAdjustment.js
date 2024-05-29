/***
 *	檔案: imGeneralAdjustment.js
 *	說明：一般調整單
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_Detail = 3;
var vnB_Amount = 4;
var vnB_Detail2 = 5;

//for 儲位用
var vatStorageDetail = 202;
var enableStorage = false;

function outlineBlock(){
	var taxType = "";
	var adjustmentType = "";

 	formInitial();
	buttonLine();
  	headerInitial();
	detailInitial();		
}

// 初始化
function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        {
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          userType       		: document.forms[0]["#userType"      ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(
	  		function(){	  		
				return "process_object_name=imGeneralAdjustmentAction&process_object_method_name=performInitial";
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
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"#B.save2"	    , type:"IMG"    ,value:"存檔",   src:"./images/button_save_data.gif", eClick:'doSubmit("ARCHIVE")'},	// 此存檔按紐是給T2保稅轉完稅與報廢單第一次送出後扣庫存後，可做存檔修改明細成本的部份
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"#B.declaration" , type:"IMG"    ,value:"核銷報單",   src:"./images/button_declaration.gif" , eClick:'execExtendItemInfo()'},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:"",
		closeService:""
	});
}
// 調整單主檔
function headerInitial(){
	var allTaxTypes = vat.bean("allTaxTypes");
	var allOrderTypes = vat.bean("allOrderTypes");
	var allAdjustmentTypes = vat.bean("allAdjustmentTypes");
	var allWarehouseCodes = vat.bean("allWarehouseCodes");
	var allCompose   = [[true,true], ["拆貨","併貨"],["DISTRIBUTE","COMBINE"]];//DISTRIBUTE COMBINE
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var isMostWrite = "<font color='red'>*</font>";
	if( brandCode.indexOf("T2") <= -1 ){
		isMostWrite = "";
	}

	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"一般調整功能維護作業",
		rows:[
			{row_style:"", cols:[
				
			]},
			{row_style:"", cols:[
				
			]},
			{row_style:"", cols:[
				
			]},
			{row_style:"", cols:[
				
			]}			
		],
		beginService:"",
		closeService:""
	});
}

function checkBox(){
	//alert(vat.item.getValueByName("#F.unBlockOnHand"));
}


// 調整單明細
function detailInitial(){
	var status = vat.item.getValueByName("#F.status");
	var taxType 	= vat.item.getValueByName("#F.taxType");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var isOpen = true;var vbCanGridDelete = isOpen;
	var vbCanGridAppend = isOpen;
	var vbCanGridModify = isOpen;

	vat.item.make(vnB_Detail, "indexNo"				, {type:"IDX", 	view:"fixed", desc:"序號" });
	vat.item.make(vnB_Detail, "itemCode"			, {type:"TEXT", view:"fixed", size:20, maxLen:23, desc:"品號", eChange:"changeLineData()"          	});
	vat.item.make(vnB_Detail, "searchItem2"	        , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif",
	 									 		            	service:"Im_OnHand:search:20091224.page",
	 									 			            left:0, right:0, width:1024, height:768,
	 									 						servicePassData:function(x){ return doPassData( "itemCode", x ); },
	 									 						serviceBeforePick:function(){ return doBeforePick("itemCode"); },
	 									 		                serviceAfterPick:function(id){doAfterPickerFunctionProcess("itemCode",id); } });

	vat.item.make(vnB_Detail, "originalDeclarationNo"	, {type:"TEXT", view:shiftMode , desc:"報單單號", size:15, eChange:"changeLineData()" , mode:taxTypeMode });
	vat.item.make(vnB_Detail, "searchItem"			, {type:"PICKER", view:shiftMode , desc:"", openMode:"open", src:"./images/start_node_16.gif", mode:taxTypeMode,
	 									 			service:"Cm_DeclarationOnHand:search:20091103.page",
	 									 			left:0, right:0, width:1024, height:768,
	 									 			servicePassData:function(x){ return doPassData("detailLine",x); },
	 									 			serviceBeforePick:function(){ return doBeforePick("originalDeclarationNo"); },
	 									 			serviceAfterPick:function(id){doAfterPickerFunctionProcess("detailLine",id); } });


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


/* 計算 單筆 LINE 合計的部份 */
function calculateCost() {
//alert("caculate");	
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var nItemLine             = vat.item.getGridLine();
	var difQuantity           = vat.item.getGridValueByName("difQuantity",nItemLine);	
	var unitCost              = vat.item.getGridValueByName("unitCost",nItemLine);
	var amount         		  = (parseFloat(unitCost) * parseFloat(difQuantity)).toFixed(2);
	if(orderTypeCode == "ICA"){		
			vat.item.setGridValueByName("amount", nItemLine, amount);			
		}
}

// 金額統計
function amountInitial(){
	vat.block.create(vnB_Amount, {
		id: "vatAmountDiv", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.totalQuantity", 		type:"LABEL", 	value:"數量總額"}]},
				{items:[{name:"#F.totalQuantity", 		type:"NUMM", 	bind:"totalQuantity" ,size:15, back:false,mode:"READONLY" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.totalAmount", 	type:"LABEL", 	value:"總成本總額"}]},
				{items:[{name:"#F.totalAmount", 	type:"NUMM", 	bind:"totalAmount" ,size:15, back:false,mode:"READONLY" }]}
			]}
	 	],
		beginService:"",
		closeService:""
	});

}

// 第一次載入 重新整理
function loadBeforeAjxService(div){
//    alert(div);
	if( vnB_Detail === div ){
		var processString = "process_object_name=imGeneralAdjustmentService&process_object_method_name=getAJAXPageData" +
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
		                    "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode") +
		                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")+
		                    "&taxType=" + vat.item.getValueByName("#F.taxType");
		return processString;
	}
}

// 第一頁 翻到前或後頁 最後一頁
function saveBeforeAjxService(div){
//    alert("saveBeforeAjxService");

	if( vnB_Detail === div ){
		var processString = "process_object_name=imGeneralAdjustmentService&process_object_method_name=updateAJAXPageLinesData" +
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value+
		                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode")+
		                    "&adjustmentType=" + vat.item.getValueByName("#F.adjustmentType")+
		                    "&status=" + vat.item.getValueByName("#F.status")+
		                    "&userType=" + vat.bean().vatBeanOther.userType;
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
//	alert("eventService");
}


// tab切換 存檔
function doPageDataSave(div){

	if(vnB_Master===div){
//		vat.block.pageDataSave(vnB_Detail); //存檔vnB_Detail
	}else if(vnB_Detail===div){
//		vat.block.pageDataSave(vnB_Master); //存檔vnB_Master
	}else if(vnB_Amount===div){
		vat.block.pageDataSave( vnB_Detail ,{
			funcSuccess:function(){
				getTotal();
			}
		});
	}
}

// 金額統計
function getTotal(){
	vat.ajax.XHRequest({
           post:"process_object_name=imGeneralAdjustmentService"+
                    "&process_object_method_name=getAJAXCountTotalAmount"+
                    "&headId=" + vat.item.getValueByName("#F.headId"),
           find: function change(oXHR){
           		vat.item.setValueByName("#F.totalQuantity", vat.ajax.getValue("totalQuantity", oXHR.responseText) );
           		vat.item.setValueByName("#F.totalAmount", vat.ajax.getValue("totalAmount", oXHR.responseText) );
           },
           fail: function changeError(){
          		vat.item.setValueByName("#F.totalQuantity", "0.0" );
           		vat.item.setValueByName("#F.totalAmount", "0.0" );
           }
    });
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

// 傳參數
function doPassData(id, x){
	var suffix = "";
	var taxType = vat.item.getValueByName("#F.taxType");
	var adjustmentType = vat.item.getValueByName("#F.adjustmentType");
	switch(id){
		case "buttonLine":
			var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
//			alert("adjustmentType = " + adjustmentType);
			suffix += "&orderTypeCode="+escape(orderTypeCode)+"&taxType="+escape(taxType)+"&adjustmentType="+escape(adjustmentType);
			break;
		case "itemCode":
		  var vLineId	      = vat.item.getGridLine(x);
		  var vItemCode       = vat.item.getGridValueByName("itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
		  var vLotNo          = vat.item.getGridValueByName("lotNo"   , vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
		  var vWarehouseCode  = vat.item.getValueByName("#F.defaultWarehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();
//		  var vItemCategory   = vat.item.getValueByName("#F.itemCategory").replace(/^\s+|\s+$/, '').toUpperCase();
		  //alert("LineId:"+vLineId);
		  suffix += "&taxType="+escape(taxType)+
		            "&startItemCode="+escape(vItemCode)+
		            "&endItemCode="+escape(vItemCode)+
		            "&startWarehouseCode="+escape(vWarehouseCode)+
		            "&endWarehouseCode="+escape(vWarehouseCode)+
		            "&startLotNo="+escape(vLotNo)+
		            "&endLotNo="+escape(vLotNo)+
		            "&isReadOnlyControl=Y" +
		            "&showZero=N";
//		            "&itemCategory="+escape(vItemCategory);
			break;
		case "detailLine":
			var vLineId	= vat.item.getGridLine(x);

			var warehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
			var itemCode = vat.item.getGridValueByName("itemCode", vLineId);
			var originalDeclarationNo = vat.item.getGridValueByName("originalDeclarationNo", vLineId);
			var originalDeclarationSeq = vat.item.getGridValueByName("originalDeclarationSeq", vLineId);

			suffix += "&warehouseCode="+escape(warehouseCode)+"&taxType="+escape(taxType)+"&adjustmentType="+escape(adjustmentType)+"&customsItemCode="+escape(itemCode)+"&declarationNo="+escape(originalDeclarationNo)+"&declarationSeqStart="+originalDeclarationSeq+"&declarationSeqEnd="+originalDeclarationSeq;
			break;
	}
//	alert(suffix);
	return suffix;
}

// 選取picker前的click
function doBeforePick(id){
	// 單頭庫別與調整類別
	var adjustmentType = vat.item.getValueByName("#F.adjustmentType");
	var warehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
	var brandCode = vat.item.getValueByName("#F.brandCode");
	switch(id){
		case "itemCode":
		case "originalDeclarationNo":
			if( brandCode.indexOf("T2") > -1 ){

				if( adjustmentType == ""   ){
					alert("請選擇調整類別");
					return false;
				}
			}
			if( warehouseCode == "" ){
				alert("請選擇庫別");
				return false;
			}
			break;
	}
	return true;
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(typeof vat.bean().vatBeanPicker.result != 'undefined'){
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

// 送出,暫存按鈕
function doSubmit(formAction){

	var alertMessage ="是否確定送出?";
	
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	
	if(orderTypeCode == "APF"){
		var sourceOrderNo = vat.item.getValueByName("#F.aafSourceOrderNo");
	
		if(sourceOrderNo==null || sourceOrderNo ==""){
			return alert("請輸入AAF單號");
		}
	}	
	
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){

		// 避免暫存後執行流程時不知道跑哪個流程，
		// 與權限控制判斷調整類別被鎖定是互相搭配
		var brandCode = vat.item.getValueByName("#F.brandCode");
		if( brandCode.indexOf("T2") > -1 ){
			var adjustmentType = vat.item.getValueByName("#F.adjustmentType");
			if("" == adjustmentType || null == adjustmentType){
				return alert("請輸入調整類別");
			}
		}
		alertMessage = "是否確定暫存?";
	}else if("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}else if("ARCHIVE" == formAction){
		var userType = vat.bean().vatBeanOther.userType;
		if("CREATOR"== userType){
			alertMessage = "是否確定存檔?";
		}else{
			alertMessage = "您無此權限";
		}
	}
	if(confirm(alertMessage)){
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, '');
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		var approvalResult        = getApprovalResult();
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
		var orderNoPrefix         = vat.item.getValueByName("#F.orderNo").substring(0,3);

		if(( orderNoPrefix == "TMP" &&  status == "SAVE" ) ||
		(inProcessing   && (status == "SAVE" || status == "REJECT" || status == "FINISH" || status == "SIGNING" )) ){

			vat.block.pageDataSave( vnB_Detail ,{
				funcSuccess:function(){
					vat.bean().vatBeanOther.formAction 		= formAction;
		  			vat.bean().vatBeanOther.beforeStatus	= status;
		  			vat.bean().vatBeanOther.processId       = processId;
	  				vat.bean().vatBeanOther.approvalResult  = approvalResult;
	  				vat.bean().vatBeanOther.approvalComment =  approvalComment;

				    if("SUBMIT" == formAction){
						vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentAction"+
				                    "&process_object_method_name=performTransaction";},{
				                    bind:true, link:true, other:true,
				                    funcSuccess:function(){
						        		vat.block.pageRefresh(vnB_Detail);
						        	}}
						);
			        }
		      	}
	      	});
		}
	}
}

// 取得簽核結果
function getApprovalResult(){
	var taxType				= vat.item.getValueByName("#F.taxType");
	var adjustmentType		= vat.item.getValueByName("#F.adjustmentType");

	if(taxType == "F" && (adjustmentType == "41" || adjustmentType == "51") ){ // 報廢保稅
		if(vat.item.getValueByName("#F.status") == "SIGNING"){
			return vat.item.getValueByName("#F.approvalResult").toString();
		}else{
			return "true";
		}
	}else{
		return "true";
	}
}

// 動態改變一筆商品資料 onHand 和 cmOnHand
function changeLineData( pickerLine ){

	var brandCode			= vat.item.getValueByName("#F.brandCode");
	var taxType				= vat.item.getValueByName("#F.taxType");
	var adjustmentType		= vat.item.getValueByName("#F.adjustmentType");
	var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");

	var vLineId				= typeof pickerLine === "undefined" ? vat.item.getGridLine() : pickerLine ;
	var itemCode			= vat.item.getGridValueByName("itemCode", vLineId);
	var originalDeclarationNo	= vat.item.getGridValueByName("originalDeclarationNo", vLineId);
	var originalDeclarationSeq	= vat.item.getGridValueByName("originalDeclarationSeq", vLineId);
	var unitCost			= vat.item.getGridValueByName("unitCost", vLineId);
	var difQuantity			= vat.item.getGridValueByName("difQuantity", vLineId);

//	if ( originalDeclarationNo != "" && originalDeclarationSeq != "" ){
//	if( declarationNo != "" && (declarationNoMemo == "" || declarationNoMemo == null )){
	if( (  brandCode.indexOf("T2") <= -1 && defaultWarehouseCode == "" ) || ( brandCode.indexOf("T2") > -1 && (defaultWarehouseCode == "" || adjustmentType == "") ) ){
		vat.item.setGridValueByName("itemCode", vLineId, "" );
		vat.item.setGridValueByName("itemCName", vLineId, "");
		vat.item.setGridValueByName("originalDeclarationNo", vLineId, "" );
		vat.item.setGridValueByName("originalDeclarationSeq", vLineId, "" );
		vat.item.setGridValueByName("warehouseCode", vLineId, "");
		vat.item.setGridValueByName("customsItemCode", vLineId, "");
		vat.item.setGridValueByName("localUnitCost", vLineId, "0.0");
		

		if( brandCode.indexOf("T2") > -1 && adjustmentType == "" ){
			return alert("請選擇調整類別");
		}
		if( defaultWarehouseCode == "" ){
			return alert("請選擇庫別");
		}
	}else{
		if( isRunAjax(taxType,itemCode,originalDeclarationNo,originalDeclarationSeq ,unitCost,difQuantity) ){
			vat.ajax.XHRequest(
		       {
		           post:"process_object_name=imGeneralAdjustmentService"+
		                    "&process_object_method_name=getAJAXImItem"+
		                    "&brandCode=" + brandCode +
		                    "&taxType=" + taxType +
		                    "&adjustmentType=" + adjustmentType +
		                    "&itemCode=" + itemCode +
		                    "&originalDeclarationNo=" + originalDeclarationNo +
		                    "&originalDeclarationSeq=" + originalDeclarationSeq +
		                    "&defaultWarehouseCode=" + defaultWarehouseCode +
		                    "&unitCost=" + unitCost +
		                    "&difQuantity=" + difQuantity,
		           find: function change(oXHR){
		           		vat.item.setGridValueByName("itemCode", vLineId, vat.ajax.getValue("itemCode", oXHR.responseText));
		          		vat.item.setGridValueByName("itemCName", vLineId, vat.ajax.getValue("itemCName", oXHR.responseText));
		          		vat.item.setGridValueByName("lotNo", vLineId, vat.ajax.getValue("lotNo", oXHR.responseText));
		          		vat.item.setGridValueByName("unitCost", vLineId, vat.ajax.getValue("unitCost", oXHR.responseText));
		          		vat.item.setGridValueByName("localUnitCost", vLineId, vat.ajax.getValue("localUnitCost", oXHR.responseText));
						vat.item.setGridValueByName("warehouseCode", vLineId, vat.ajax.getValue("warehouseCode", oXHR.responseText));

						vat.item.setGridValueByName("originalDeclarationNo", vLineId, vat.ajax.getValue("originalDeclarationNo", oXHR.responseText));
						vat.item.setGridValueByName("originalDeclarationSeq", vLineId, vat.ajax.getValue("originalDeclarationSeq", oXHR.responseText));
						vat.item.setGridValueByName("originalDeclarationDate", vLineId, vat.ajax.getValue("originalDeclarationDate", oXHR.responseText));
						vat.item.setGridValueByName("customsItemCode", vLineId, vat.ajax.getValue("customsItemCode", oXHR.responseText));
						
		           },
		           fail: function changeError(){
		          		vat.item.setGridValueByName("itemCode", vLineId, "");
				     	vat.item.setGridValueByName("itemCName", vLineId, "");
				     	vat.item.setGridValueByName("lotNo", vLineId, "");
				     	vat.item.setGridValueByName("localUnitCost", vLineId, "0.0");
						vat.item.setGridValueByName("amount", vLineId, "0.0");
						vat.item.setGridValueByName("warehouseCode", vLineId, "");
						vat.item.setGridValueByName("difQuantity", vLineId, "0");
						vat.item.setGridValueByName("originalDeclarationNo", vLineId, "");
						vat.item.setGridValueByName("originalDeclarationSeq", vLineId, "");
						vat.item.setGridValueByName("originalDeclarationDate", vLineId, "");
						vat.item.setGridValueByName("boxNo", vLineId, "");
						vat.item.setGridValueByName("weight", vLineId, "0.0");
						vat.item.setGridValueByName("customsItemCode", vLineId, "");
		           }
		       });
		  }else{
		  	changeItemMessage(vLineId,taxType,itemCode,defaultWarehouseCode,originalDeclarationNo,originalDeclarationSeq);
		  }
	}

}

// 依稅別判別是否跑ajax
function isRunAjax(taxType,itemCode,originalDeclarationNo,originalDeclarationSeq ){
	if(taxType == "F"){
		return itemCode != "" || (originalDeclarationNo != "" && originalDeclarationSeq != "");
	}else{
		return itemCode != "";
	}
}
// 改變訊息
function changeItemMessage(vLineId,taxType,itemCode,defaultWarehouseCode,originalDeclarationNo,originalDeclarationSeq){
	if(taxType == "F"){
		if( itemCode != "" || originalDeclarationNo != "" || originalDeclarationSeq != "" ){
	  		if( originalDeclarationNo == "" && originalDeclarationSeq != "" ){
//		     	alert("請輸入報關單1");
		     	vat.item.setGridValueByName("itemCName", vLineId, "請輸入報關單");
	  		}else if( originalDeclarationSeq == "" && originalDeclarationNo != "" ){
//	  			alert("請輸入報單項次");
		     	vat.item.setGridValueByName("itemCName", vLineId, "請輸入報單項次");
	  		}else{
//	  			alert("請輸入報關單2");
		     	vat.item.setGridValueByName("itemCName", vLineId, "請輸入報關單");
	  		}
	  	}else{
	     	vat.item.setGridValueByName("itemCName", vLineId, "");
	     	vat.item.setGridValueByName("lotNo", vLineId, "");
	     	vat.item.setGridValueByName("warehouseCode", vLineId, "");
	  	}
	  	vat.item.setGridValueByName("itemCode", vLineId, itemCode);
//  	vat.item.setGridValueByName("localUnitCost", vLineId, "0.0");
//  	vat.item.setGridValueByName("amount", vLineId, "0.0");
//		vat.item.setGridValueByName("warehouseCode", vLineId, defaultWarehouseCode);
//		vat.item.setGridValueByName("difQuantity", vLineId, "0");
		vat.item.setGridValueByName("originalDeclarationNo", vLineId, originalDeclarationNo);
		vat.item.setGridValueByName("originalDeclarationSeq", vLineId, originalDeclarationSeq);
//		vat.item.setGridValueByName("boxNo", vLineId, "");
//		vat.item.setGridValueByName("weight", vLineId, "0.0");
//		vat.item.setGridValueByName("customsItemCode", vLineId, "");
	}else{
		vat.item.setGridValueByName("itemCName", vLineId, "");
		vat.item.setGridValueByName("localUnitCost", vLineId, "0.0");
		vat.item.setGridValueByName("warehouseCode", vLineId, "");
	}
}

// 改變庫別
function changeWarehouseCode(){
	vat.block.pageDataSave( vnB_Detail ,{
		funcSuccess:function(){
			vat.ajax.XHRequest({
	        	post:"process_object_name=imGeneralAdjustmentService"+
	                    "&process_object_method_name=updateAJAXWarehouseCode" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&defaultWarehouseCode=" + vat.item.getValueByName("#F.defaultWarehouseCode"),
	       		find: function change(oXHR){
					vat.block.pageRefresh(vnB_Detail);
	        	},
	       			fail: function changeError(){
	        	}
			});
		}
    });
}
// 改變調整類別
function changeAdjustmentType(){
	var adjustmentType		= vat.item.getValueByName("#F.adjustmentType");
	var taxType				= vat.item.getValueByName("#F.taxType");
	var brandCode 			= vat.bean().vatBeanOther.loginBrandCode;
	if( adjustmentType == "" ){
		vat.item.setAttributeByName("#F.declarationNo", 	"readOnly", true);
		vat.item.setAttributeByName("#F.declarationDate", 	"readOnly", true);
		vat.item.setAttributeByName("#F.declarationType", 	"readOnly", true);
		vat.item.setValueByName("#F.declarationNo", "");
		vat.item.setValueByName("#F.declarationDate", "");
		vat.item.setValueByName("#F.declarationType", "");
		vat.item.setValueByName("#H.isAdjustCost", "N");
		vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
		vat.item.setStyleByName("#B.declaration", 	"display", "inline");
		vat.item.setStyleByName("#B.sendCustoms", 		"display", "none");
		vat.item.setStyleByName("#B.sendCancel", 		"display", "none");
		checkCustomsStatus();
		if(taxType == "F"){
			vat.tabm.displayToggle(0, "xTab1", true);
		}else{
			if(adjustmentType == "51" ){ // 保稅轉完稅
				vat.tabm.displayToggle(0, "xTab1", true);				
			}else{
				vat.tabm.displayToggle(0, "xTab1", false);
			}
		}
		return;
	}

	if( taxType == "P" ){
		switch(adjustmentType){
			case "51":	//	保稅轉完稅
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
				//vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", false);
				//vat.item.setValueByName("#F.unBlockOnHand", "Y");
				vat.item.setValueByName("#H.isAdjustCost", "Y");
				vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
				if( brandCode.indexOf("T2") > -1 ){
					vat.tabm.displayToggle(0, "xTab1", true);
				}else{
					vat.tabm.displayToggle(0, "xTab1", false);
				}
				break;
			case "81":	//	調成本
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
				vat.item.setValueByName("#F.declarationNo", "");
				//vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", true);
				//vat.item.setValueByName("#F.unBlockOnHand", "N");
				vat.item.setValueByName("#F.declarationDate", "");
				vat.item.setValueByName("#F.declarationType", "");
				vat.item.setValueByName("#H.isAdjustCost", "Y");
				vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
				vat.tabm.displayToggle(0, "xTab1", false);
				break;
			case "31":	//	商檢抽樣
			case "61":	//	領用除帳
			case "99":	//	負報單
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
				vat.item.setValueByName("#F.declarationNo", "");
				//vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", true);
				//vat.item.setValueByName("#F.unBlockOnHand", "N");
				vat.item.setValueByName("#F.declarationDate", "");
				vat.item.setValueByName("#F.declarationType", "");
				vat.item.setValueByName("#H.isAdjustCost", "N");
				vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
				vat.tabm.displayToggle(0, "xTab1", false);
				break;
			case "11":	//	盤盈
			case "12":	//	盤虧

			case "41":	//	報廢
				break;
		}
	}else if( taxType == "F" ) {
	  vat.tabm.displayToggle(0, "xTab1", true);
		switch(adjustmentType){
			case "11":	//	盤盈
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
				vat.item.setStyleByName("#B.declaration", 	"display", "none");
				vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", true);
				vat.item.setValueByName("#F.unBlockOnHand", "N");				
				checkCustomsStatus(adjustmentType);
				break;
			case "12":	//	盤虧
				vat.item.setAttributeByName("#F.declarationNo", 	"readOnly", false);
				vat.item.setStyleByName("#B.declaration", 	"display", "inline");
				vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", true);
				vat.item.setValueByName("#F.unBlockOnHand", "N");
				checkCustomsStatus(adjustmentType);
				break;
			case "41":	//	報廢
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
				vat.item.setStyleByName("#B.declaration", 	"display", "inline");
				vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", false);
				vat.item.setValueByName("#F.unBlockOnHand", "Y");
				vat.item.setValueByName("#H.isAdjustCost", "Y");
				vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
				checkCustomsStatus(adjustmentType);
				break;
			case "81":	//	調成本
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
				vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", true);
				vat.item.setValueByName("#F.unBlockOnHand", "N");
				vat.item.setValueByName("#H.isAdjustCost", "Y");
				vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
				vat.item.setStyleByName("#B.declaration", 	"display", "none");
				checkCustomsStatus(adjustmentType);
				break;
			case "51":	//	保稅轉完稅
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
				vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", false);
				vat.item.setValueByName("#F.unBlockOnHand", "Y");
				vat.item.setValueByName("#H.isAdjustCost", "Y");
				vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
				vat.item.setStyleByName("#B.declaration", 	"display", "inline");
				checkCustomsStatus(adjustmentType);
				break;
			case "99":	//	負報單
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
				vat.item.setStyleByName("#B.declaration", 	"display", "none");
				vat.item.setAttributeByName("#F.unBlockOnHand", "readOnly", true);
				vat.item.setValueByName("#F.unBlockOnHand", "N");
				vat.item.setValueByName("#H.isAdjustCost", "N");
				vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
				checkCustomsStatus(adjustmentType);
				break;
			case "31":	//	商檢抽樣
			case "61":	//	領用除帳
			    vat.item.setStyleByName("#B.declaration", 	"display", "inline");
				break;
		}
	}
	changeAmountReadOnly(); // 是否鎖line的總成本

	if( brandCode.indexOf("T2") > -1 ){
		changeWarehouse();	// 連動庫別
	}
/*	vat.block.pageDataSave( vnB_Detail ,{
		funcSuccess:function(){
			vat.ajax.XHRequest({
	        	post:"process_object_name=imGeneralAdjustmentService"+
	                    "&process_object_method_name=updateAJAXImItemByAdjustmentType" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&adjustmentType=" + vat.item.getValueByName("#F.adjustmentType"),
	       		find: function change(oXHR){
					vat.block.pageRefresh(vnB_Detail);
	        	},
	       			fail: function changeError(){
	        	}
			});
		}
    });*/
}

// 將有關的欄位替換
function changeCmDeclarationData(){
	//alert("changeRelationData");
	var declarationNo = vat.item.getValueByName("#F.declarationNo").replace(/^\s+|\s+$/, '').toUpperCase();


	vat.ajax.XHRequest(
       {
           post:"process_object_name=imGeneralAdjustmentService"+
                    "&process_object_method_name=getAJAXByCmDeclarationData"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
                    "&declarationNo=" + vat.item.getValueByName("#F.declarationNo"),
           find: function change(oXHR){
           		vat.item.setValueByName("#F.declarationNo", vat.ajax.getValue("declarationNo", oXHR.responseText) );
           		vat.item.setValueByName("#F.declarationNoMemo", vat.ajax.getValue("declarationNoMemo", oXHR.responseText) );
           		vat.item.setValueByName("#F.declarationDate", vat.ajax.getValue("declarationDate", oXHR.responseText) );
           		vat.item.setValueByName("#F.declarationType", vat.ajax.getValue("declarationType", oXHR.responseText) );
           		vat.item.setValueByName("#F.adjustmentDate", vat.ajax.getValue("declarationDate", oXHR.responseText) );
           		vat.item.setValueByName("#F.dischargeDate", vat.ajax.getValue("declarationDate", oXHR.responseText) );
           },
           fail: function changeError(){
          		vat.item.setValueByName("#F.declarationNo", sourceOrderNo );
           		vat.item.setValueByName("#F.declarationNoMemo", "查無此進貨單" );
           		vat.item.setValueByName("#F.declarationDate", "" );
           		vat.item.setValueByName("#F.declarationType", "" );

           }
       });
}

// 是否鎖line的總成本
function changeAmountReadOnly(){
	var isAdjustCostCheckBox = vat.item.getValueByName("#F.isAdjustCostCheckBox");

	if(isAdjustCostCheckBox == "Y"){
		vat.item.setGridAttributeByName("amount", "readOnly", false);
		vat.item.setValueByName("#H.isAdjustCost", "Y");
	}else{
		vat.item.setGridAttributeByName("amount", "readOnly", true);
		vat.item.setValueByName("#H.isAdjustCost", "N");
	}
}

// 依單別,調整類型,連動庫別
function changeWarehouse(){
	vat.ajax.XHRequest({
		post:"process_object_name=imGeneralAdjustmentService"+
        		"&process_object_method_name=getAJAXWarehouse"+
          		"&brandCode=" + vat.item.getValueByName("#F.brandCode") +
              	"&adjustmentType=" + vat.item.getValueByName("#F.adjustmentType") +
              	"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +
              	"&taxType=" + vat.item.getValueByName("#F.taxType") +
              	"&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode,
      	find: function change(oXHR){

           	var allWarehouseCodes = eval(vat.ajax.getValue("allWarehouseCodes", oXHR.responseText));
          	allWarehouseCodes[0][0] = "#F.defaultWarehouseCode";
			vat.item.SelectBind(allWarehouseCodes);
      	},
     	fail: function changeError(){
//     		alert("error");
      	}
	});
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
	document.forms[0]["#formId"            ].value = code;
	vat.item.setValueByName("#F.headId", code);
	document.forms[0]["#processId"         ].value = "";
	document.forms[0]["#assignmentId"      ].value = "";
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=imGeneralAdjustmentAction&process_object_method_name=performInitial";
     	},{other      : true,
     	   funcSuccess:function(){
     			vat.item.bindAll()
				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);

				var taxType = vat.item.getValueByName("#F.taxType");
				var adjustmentType = vat.item.getValueByName("#F.adjustmentType");
				if(taxType == "F" && (adjustmentType == "41" || adjustmentType == "51" ) ){ // 報廢,保稅轉完稅
	 		      	refreshWfParameter(vat.item.getValueByName("#F.brandCode"),
	        			vat.item.getValueByName("#F.orderTypeCode"),
	        			vat.item.getValueByName("#F.orderNo"));
	        		vat.block.pageRefresh(102);
	        		vat.tabm.displayToggle(0, "xTab3", vat.item.getValueByName("#F.status") != "SAVE" && vat.item.getValueByName("#F.status") != "UNCONFIRMED", false, false);
				}
				doFormAccessControl();

     	}});


}
// 上傳海關
function updateCustomsStatus(tranStatus){
	//alert("adv");
		//alert(tranStatus);
		if(confirm("是否要送出?")){
			vat.bean().vatBeanOther =
        	{ 
          		loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          		loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          		formId             	: document.forms[0]["#formId"            ].value,
          		orderTypeCode         : document.forms[0]["#orderTypeCode"     ].value,
          		tranStatus         : tranStatus,
          		customsWarehouse	:'FW',
          		headId : vat.item.getValueByName("#F.headId")
        	};
   			vat.bean.init(	
	  			function(){
						return "process_object_name=imGeneralAdjustmentService&process_object_method_name=updateCustomsStatus"; 
	    		},{link:true, other: true,
	    			funcSuccess:function(){
					window.top.close();
					}
    			}
    		);	
		}
		
}
// do picker 回來的事件
function doAfterPickerFunctionProcess(code,id){
	//do picker back something
	var vLineId	= vat.item.getGridLine(id);
	switch(code){
		case "itemCode":
			if(typeof vat.bean().vatBeanPicker.imOnHandResult != 'undefined'){
				var adjustmentType		= vat.item.getValueByName("#F.adjustmentType");
				var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");
				var brandCode = vat.item.getValueByName("#F.brandCode");

//				if( defaultWarehouseCode == "" || adjustmentType == "" ){
//					if( adjustmentType == "" ){
//						return alert("請選擇調整類別");
//					}else if( defaultWarehouseCode == "" ){
//						return alert("請選擇庫別");
//					}
//				}else{
					vat.block.pageRefresh(vnB_Detail);

					vat.bean().vatBeanOther.lineId = vLineId;
			  		vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
			  		vat.bean().vatBeanOther.brandCode = vat.item.getValueByName("#F.brandCode");
			  		vat.bean().vatBeanOther.taxType = vat.item.getValueByName("#F.taxType");
					vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentService"+
					            "&process_object_method_name=updatePickerData";},
					     {other:true,picker:true, funcSuccess: function() {vat.block.pageRefresh(vnB_Detail);}});
//				}
			}
		break;
		case "detailLine":
			if(typeof vat.bean().vatBeanPicker.cmOnHandResult != 'undefined'){
		    	vat.item.setGridValueByName("originalDeclarationNo", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationNo"]);
		    	vat.item.setGridValueByName("originalDeclarationSeq", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["declarationSeq"]);
				vat.item.setGridValueByName("originalDeclarationDate", vLineId, vat.bean().vatBeanPicker.cmOnHandResult[0]["importDate"]);
				
				changeLineData(vLineId);
			}
		break;
	}
}

// 依狀態鎖form
function doFormAccessControl(){
	var status 			= vat.item.getValueByName("#F.status");
	var processId 		= vat.bean().vatBeanOther.processId;
	var formId 			= vat.bean().vatBeanOther.formId;
	var orderNoPrefix 	= vat.item.getValueByName("#F.orderNo").substring(0,3);
	var orderTypeCode 	= vat.item.getValueByName("#F.orderTypeCode");
	var taxType 	  	= vat.item.getValueByName("#F.taxType");
	var adjustmentType 	= vat.item.getValueByName("#F.adjustmentType");
	var brandCode 		= vat.item.getValueByName("#F.brandCode");
	var isAdjustCost 	= vat.item.getValueByName("#H.isAdjustCost");

	if("Y" == isAdjustCost){
		vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
	}else{
		vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
	}
	
	checkCustomsStatus(adjustmentType);

	
	if(taxType == "F"){
		vat.tabm.displayToggle(0, "xTab1", true);
	}else{
		if(adjustmentType == "51" ){ // 保稅轉完稅
			vat.tabm.displayToggle(0, "xTab1", true);			
		}else{
			vat.tabm.displayToggle(0, "xTab1", false);			
		}
	}
	// 初始化
	//======================<header>=============================================
	if( brandCode.indexOf("T2") > -1){
		vat.item.setAttributeByName("#F.adjustmentType", "readOnly", false);
	}else{
		vat.item.setAttributeByName("#F.adjustmentType", "readOnly", true);
	}
	vat.item.setAttributeByName("#F.remark1", "readOnly", false);
	lockColumn(orderTypeCode, taxType, adjustmentType);
	vat.item.setAttributeByName("#F.isAdjustCostCheckBox", "readOnly", false);
	vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", false);

	vat.item.setAttributeByName("#F.approvalResult", "readOnly", true);
	vat.item.setAttributeByName("#F.approvalComment", "readOnly", true);

	//======================<master>=============================================
	vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", false);
	vat.item.setAttributeByName("#F.dischargeDate", "readOnly", false);
	vat.item.setAttributeByName("#F.boxQty", "readOnly", false);
	//======================<detail>=============================================
//	vat.item.setGridAttributeByName("itemCode", "readOnly", false);
//	vat.item.setGridAttributeByName("lotNo", "readOnly", false);
//	vat.item.setGridAttributeByName("difQuantity", "readOnly", false);

/*	switch(taxType){
		case "F":
			vat.item.setGridAttributeByName("searchItem2", "readOnly", false);
			vat.item.setGridAttributeByName("searchItem", "readOnly", false);
			break;
		case "P":
			vat.item.setGridAttributeByName("searchItem2", "readOnly", false);
			break;
	}*/

	vat.block.canGridModify([vnB_Detail], true,true,true);
	vat.item.setAttributeByName("vatDetailDiv", "readOnly", false,true,true);
	//=======================<buttonLine>========================================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "inline");
	vat.item.setStyleByName("#B.save", 		"display", "inline");
	vat.item.setStyleByName("#B.save2", 	"display", "none");
	vat.item.setStyleByName("#B.void", 		"display", "none");
	vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	vat.item.setStyleByName("#B.import", 	"display", "inline");
	vat.item.setStyleByName("#B.print", 	"display", "inline");
	if("T2B" == brandCode ){
		vat.item.setStyleByName("#B.export_other", 	"display", "inline");
	}else{
		vat.item.setStyleByName("#B.export_other", 	"display", "none");
	}
	//===========================================================================

	if(orderNoPrefix == "TMP" ){
		vat.item.setStyleByName("#B.print", 	"display", "none");
	}

	//for 儲位用
	if(enableStorage){
		vat.item.setStyleByName("#B.storageExport"   , "display", "inline");
		vat.item.setStyleByName("#B.storageImport"   , "display", "inline");
	}else{
		vat.item.setStyleByName("#B.storageExport"   , "display", "none");
		vat.item.setStyleByName("#B.storageImport"   , "display", "none");
	}
	
	if(formId != ""){
		if( processId != null && processId != 0 ){ //從待辦事項進入
			vat.item.setStyleByName("#B.new", 		"display", "none");
			vat.item.setStyleByName("#B.search", 	"display", "none");

			// 避免T2第一次暫存起流程Ａ，駁回後選別的調整類別而錯亂流程，固把它鎖起來
			// 與按暫存紐檢查調整類別為必填是互相搭配
			if( brandCode.indexOf("T2") > -1){
				if( adjustmentType != "" ){
					vat.item.setAttributeByName("#F.adjustmentType", "readOnly", true);
				}
			}

			if( status == "SAVE" || status == "REJECT" ){
				vat.item.setStyleByName("#B.void", 		"display", "inline");
			}else if( status == "SIGNING" ){
				var userType = vat.bean().vatBeanOther.userType;
				if("CREATOR" == userType){ // 若為起單人則可存檔
					vat.item.setStyleByName("#B.save2", 	"display", "inline");
				}

				vat.item.setAttributeByName("#F.approvalResult", "readOnly", false);
				vat.item.setAttributeByName("#F.approvalComment", "readOnly", false);
			}
		}else{
			// 查詢回來
			if( status == "SAVE" || status == "REJECT"){
				vat.item.setStyleByName("#B.submit", 	"display", "none");
				vat.item.setStyleByName("#B.save", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 	"display", "none");
				vat.item.setStyleByName("#B.message", 	"display", "none");
				vat.item.setStyleByName("#B.import", 	"display", "none");

				//======================<header>=============================================
				vat.item.setAttributeByName("#F.sourceOrderTypeCode", "readOnly", true);
				vat.item.setAttributeByName("#F.adjustmentType", "readOnly", true);
				vat.item.setAttributeByName("#F.remark1", "readOnly", true);
				vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
				vat.item.setAttributeByName("#F.isAdjustCostCheckBox", "readOnly", true);
				vat.item.setAttributeByName("#F.defaultWarhouseCode", "readOnly", true);
				//======================<master>=============================================
				vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", true);
				vat.item.setAttributeByName("#F.dischargeDate", "readOnly", true);
				vat.item.setAttributeByName("#F.boxQty", "readOnly", true);
				//======================<detail>=============================================
/*				vat.item.setGridAttributeByName("itemCode", "readOnly", true);
				vat.item.setGridAttributeByName("lotNo", "readOnly", true);
				vat.item.setGridAttributeByName("amount", "readOnly", true);
				vat.item.setGridAttributeByName("difQuantity", "readOnly", true);
				vat.item.setGridAttributeByName("originalDeclarationNo", "readOnly", true);
				vat.item.setGridAttributeByName("originalDeclarationSeq", "readOnly", true);
				vat.item.setGridAttributeByName("boxNo", "readOnly", true);
				vat.item.setGridAttributeByName("weight", "readOnly", true);

				switch(taxType){
					case "F":
						vat.item.setGridAttributeByName("searchItem2", "readOnly", true);
						vat.item.setGridAttributeByName("searchItem", "readOnly", true);
						break;
					case "P":
						vat.item.setGridAttributeByName("searchItem2", "readOnly", true);
						break;
				}*/
				vat.block.canGridModify([vnB_Detail], false,false,false);
				vat.item.setAttributeByName("vatDetailDiv", "readOnly", true);
			}
		}
	}

	if( status == "SIGNING" || status == "FINISH" || status == "CLOSE"  || status == "VOID" ){
		//======================<header>=============================================
		vat.item.setAttributeByName("#F.adjustmentType", "readOnly", true);
		vat.item.setAttributeByName("#F.remark1", "readOnly", true);
		vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
		vat.item.setAttributeByName("#F.isAdjustCostCheckBox", "readOnly", true);
		vat.item.setAttributeByName("#F.defaultWarehouseCode", "readOnly", true);
		vat.item.setAttributeByName("#F.sourceOrderTypeCode", "readOnly", true);
		vat.item.setAttributeByName("#F.declarationDate", "readOnly", true);
		vat.item.setAttributeByName("#F.declarationType", "readOnly", true);
		//======================<master>=============================================
		vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", false);
		vat.item.setAttributeByName("#F.dischargeDate", "readOnly", true);
		vat.item.setAttributeByName("#F.boxQty", "readOnly", true);
		//======================<detail>=============================================
/*		vat.item.setGridAttributeByName("itemCode", "readOnly", true);
		vat.item.setGridAttributeByName("lotNo", "readOnly", true);
		vat.item.setGridAttributeByName("amount", "readOnly", true);
		vat.item.setGridAttributeByName("difQuantity", "readOnly", true);
		vat.item.setGridAttributeByName("originalDeclarationNo", "readOnly", true);
		vat.item.setGridAttributeByName("originalDeclarationSeq", "readOnly", true);
		vat.item.setGridAttributeByName("boxNo", "readOnly", true);
		vat.item.setGridAttributeByName("weight", "readOnly", true);
		switch(taxType){
			case "F":
				vat.item.setGridAttributeByName("searchItem2", "readOnly", true);
				vat.item.setGridAttributeByName("searchItem", "readOnly", true);
				break;
			case "P":
				vat.item.setGridAttributeByName("searchItem2", "readOnly", true);
				break;
		}*/
		vat.block.canGridModify([vnB_Detail], true,false,false);
		vat.item.setAttributeByName("vatDetailDiv", "readOnly", true);
		//=======================<buttonLine>========================================

		if( status == "SIGNING" ){ // || status == "FINISH"
			vat.item.setStyleByName("#B.submit", 		"display", "inline");
			vat.item.setStyleByName("#B.submitBG", 		"display", "none");
			vat.item.setStyleByName("#B.message", 		"display", "none");
			vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
			vat.item.setAttributeByName("#F.declarationDate", "readOnly", false);
			vat.item.setAttributeByName("#F.declarationType", "readOnly", false);
			if(taxType = "F" && (adjustmentType == "41" || adjustmentType == "51")  ){	// 報廢和保稅轉完稅
				if("CREATOR" == userType){
					vat.item.setStyleByName("#B.message", 		"display", "inline");
					vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
				}

				if( processId != null && processId != 0 && (adjustmentType == "41" || adjustmentType == "51") ){
					if("CREATOR" == userType){
						vat.item.setStyleByName("#B.message", 		"display", "inline");
						vat.item.setAttributeByName("#F.isAdjustCostCheckBox", "readOnly", false);

						var isAdjustCostCheckBox = vat.item.getValueByName("#F.isAdjustCostCheckBox");
						if(isAdjustCostCheckBox == "Y"){
							vat.item.setGridAttributeByName("amount", "readOnly", false); // 保稅轉完稅之流程中簽核中可以改總成本
						}else{
							vat.item.setGridAttributeByName("amount", "readOnly", true);
						}
						
						vat.item.setAttributeByName("#F.adjustmentDate", "readOnly", false);
						vat.item.setAttributeByName("#F.dischargeDate", "readOnly", false);
					}
				}
			}
		}else{
			vat.item.setStyleByName("#B.submit", 		"display", "none");
			vat.item.setStyleByName("#B.submitBG", 		"display", "none");
			vat.item.setStyleByName("#B.message", 		"display", "none");
		}
		vat.item.setStyleByName("#B.save", 			"display", "none");
		vat.item.setStyleByName("#B.declaration", 	"display", "none");
		vat.item.setStyleByName("#B.import", 		"display", "none");
		//===========================================================================
	}
	
//	vat.item.setGridAttributeByName("searchItem", "readOnly", true);
}

// 依稅別和調整類別鎖欄位
function lockColumn(orderTypeCode, taxType, adjustmentType){

	vat.item.setStyleByName("#B.declaration", 	"display", "none");	// 核銷報單
	switch(taxType){
		case "F":	// 保稅
			switch(adjustmentType){
				case "11":	// 盤盈
				case "61":	// 領用除帳(非賣品領用)
				case "99":	// 負報單
					vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);	//	單頭報關單
//					vat.item.setValueByName("#H.isAdjustCost", "N");
//					vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
					break;
				case "12":	// 盤虧
					vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);	//	單頭報關單
					vat.item.setStyleByName("#B.declaration", 	"display", "inline"); 	// 核銷展報單
//					vat.item.setValueByName("#H.isAdjustCost", "N");
//					vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
					break;

				case "51":	// 保稅轉完稅
					vat.item.setStyleByName("#B.declaration", 	"display", "inline");	// 核銷報單
				case "81":	// 調成本
					vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
					vat.item.setAttributeByName("#F.declarationDate", "readOnly", false);
					vat.item.setAttributeByName("#F.declarationType", "readOnly", false);
//					vat.item.setValueByName("#H.isAdjustCost", "Y");
//					vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
					break;
//				case "31":	// 商檢抽樣
				case "41":	// 報廢
					vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
					vat.item.setAttributeByName("#F.declarationDate", "readOnly", false);
					vat.item.setAttributeByName("#F.declarationType", "readOnly", false);
					vat.item.setStyleByName("#B.declaration", 	"display", "inline"); 	// 核銷展報單
//					vat.item.setValueByName("#H.isAdjustCost", "N");
//					vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
					break;
			}

//			vat.item.setGridAttributeByName("searchItem", "readOnly", false);

			break;
		case "P": // 完稅
			switch(adjustmentType){
				case "11":	// 盤盈
				case "12":	// 盤虧
//				case "31":	// 商檢抽樣
				case "41":	// 報廢
				case "61":	// 領用除帳
//				case "99":	// 負報單
					vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
//					vat.item.setValueByName("#H.isAdjustCost", "N");
//					vat.item.setValueByName("#F.isAdjustCostCheckBox", "N");
					break;
				case "81":	// 調成本
					vat.item.setAttributeByName("#F.declarationNo", "readOnly", true);
//					vat.item.setValueByName("#H.isAdjustCost", "Y");
//					vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
					break;
				case "51":	// 保稅轉完稅
					vat.item.setAttributeByName("#F.declarationNo", "readOnly", false);
//					vat.item.setValueByName("#H.isAdjustCost", "Y");
//					vat.item.setValueByName("#F.isAdjustCostCheckBox", "Y");
					break;
			}
//			vat.item.setGridAttributeByName("searchItem", "readOnly", true);
			break;
	}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" +
		"?programId=IM_GENERAL_ADJUSTMENT" +
		"&levelType=ERROR" +
        "&processObjectName=imGeneralAdjustmentService" +
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
    vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentAction"+
        "&process_object_method_name=performTransactionForBackGround";}, {bind:true, link:true, other:true});
}

// 展報單
function execExtendItemInfo(){
	vat.block.pageDataSave( vnB_Detail ,{
		funcSuccess:function(){
		    vat.bean().vatBeanOther.processObjectName = "imGeneralAdjustmentService";
		    vat.bean().vatBeanOther.searchMethodName = "executeFind";
		    vat.bean().vatBeanOther.tableType = "IM_ADJUSTMENT";
		    vat.bean().vatBeanOther.searchKey = vat.item.getValueByName("#F.headId");
		    vat.bean().vatBeanOther.subEntityBeanName = "imAdjustmentLines";
		    vat.bean().vatBeanOther.itemFieldName = "itemCode";
		    vat.bean().vatBeanOther.warehouseCodeFieldName = "warehouseCode";
		    vat.bean().vatBeanOther.declTypeFieldName = "moreOrLessType";			// 找無
		    vat.bean().vatBeanOther.declNoFieldName = "originalDeclarationNo";
		    vat.bean().vatBeanOther.declSeqFieldName = "originalDeclarationSeq";
		    vat.bean().vatBeanOther.declDateFieldName = "originalDeclarationDate";
		    vat.bean().vatBeanOther.lotFieldName = "lotNo";
		    vat.bean().vatBeanOther.qtyFieldName = "difQuantity";
		    vat.block.submit(function(){return "process_object_name=appExtendItemInfoService"+
		            "&process_object_method_name=executeExtendItem";}, {other:true, funcSuccess: function() {vat.block.pageRefresh(vnB_Detail);}});
		}
	});
}

// 匯入
function importFormData(){
	var width = "600";
    var height = "400";
    var headId = vat.item.getValueByName("#F.headId");
    var taxType = vat.item.getValueByName("#F.taxType");

    var defaultWarehouseCode = vat.item.getValueByName("#F.defaultWarehouseCode");

    if( defaultWarehouseCode == "" ){
    	return alert("請選擇庫別");
    }

    if( taxType == "F" ){
    	var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=IM_GENERAL_F_SQL" +
			"&importFileType=XLS" +
	        "&processObjectName=imGeneralAdjustmentService" +
	        "&processObjectMethodName=executeImportLists" +
	        "&arguments=" + headId + "{$}" +
	        				defaultWarehouseCode +
	        "&parameterTypes=LONG{$}STRING" +
	        "&blockId=" + vnB_Detail,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
    }else if( taxType == "P" ){
    	var returnData = window.open(
			"/erp/fileUpload:standard:2.page" +
			"?importBeanName=IM_GENERAL_P" +
			"&importFileType=XLS" +
	        "&processObjectName=imGeneralAdjustmentService" +
	        "&processObjectMethodName=executeImportLists" +
	        "&arguments=" + headId + "{$}" +
	        				defaultWarehouseCode +
	        "&parameterTypes=LONG{$}STRING" +
	        "&blockId=" + vnB_Detail,
			"FileUpload",
			'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
    }
}

// 匯出
function exportFormData(){

	var taxType = vat.item.getValueByName("#F.taxType");

	var importBeanName = "P" == taxType ? "IM_GENERAL_P" : "IM_GENERAL_F_SQL";

    var headId = vat.item.getValueByName("#F.headId");
    var condition = ( "P" == taxType ? "&arguments=" + headId + "&parameterTypes=LONG" : "&headId=" + headId )

    var url = "/erp/jsp/" + ("P" == taxType ? "ExportFormData.jsp" : "ExportFormView.jsp") +
              "?exportBeanName=" + importBeanName +
              "&fileType=XLS" +
              "&processObjectName=imGeneralAdjustmentService" +
              "&processObjectMethodName=" +   ("P" == taxType ? "executeFind&gridFieldName=imAdjustmentLines" : "getAJAXFExportData")+
              condition;

    var width = "200";
    var height = "30";
    window.open(url, '調整單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// T2A 匯出其他
function exportFormData_other(){
	var brandCode = vat.item.getValueByName("#F.brandCode");

	var importBeanName = "IM_GENERAL_T2A_SQL";

    var headId = vat.item.getValueByName("#F.headId");
    var condition = "&headId=" + headId;

    var url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName=" + importBeanName +
              "&fileType=XLS" +
              "&processObjectName=imGeneralAdjustmentService" +
              "&processObjectMethodName=getAJAXFExportDataOther"+
              condition;

    var width = "200";
    var height = "30";
    window.open(url, '調整單匯出其它', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 票據列印
function openReportWindow(type){
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=imGeneralAdjustmentService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
			                    	//alert(vat.bean().vatBeanOther.reportUrl);
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);

	if("AFTER_SUBMIT"==type) createRefreshForm();
}

//檢查是否有來源單號
function checkSourceOrderNo(){
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var sourceOrderNo = vat.item.getValueByName("#F.aafSourceOrderNo");
	var sourceOrderTypeCode = vat.item.getValueByName("#F.aafSourceOrderTypeCode");
	vat.ajax.XHRequest({
		post:"process_object_name=imGeneralAdjustmentService"+
	                    "&process_object_method_name=findSourceOrderNoForAJAX"+
	                    "&brandCode=" + brandCode+
	                    "&sourceOrderNo=" + sourceOrderNo+
	                    "&sourceOrderTypeCode=" + sourceOrderTypeCode ,
	    find: function checkSourceOrderNoRequestSuccess(oXHR){
	    	var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
	    	if(errorMsg != "" ){
	    		alert(errorMsg);
	    		vat.item.setValueByName("#F.aafSourceOrderNo", "");
	    	}	    	
	    	//alert(sourceOrderNo);
	    }                
	})	
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

function checkCustomsStatus(type){
		var statusHidden = vat.item.getValueByName("#F.customsStatusHidden");
		var cusStatusHidden = statusHidden.substring(0, 1);	
	
	if(type =="51"||type =="41"){
		vat.item.setStyleByName("#B.sendCustoms", 		"display", "inline");
		vat.item.setStyleByName("#B.sendCancel", 		"display", "inline");


	}else{
	  if(vat.item.getValueByName("#F.orderTypeCode")=='AEG'||vat.item.getValueByName("#F.orderTypeCode")=='MEF'){
	    vat.item.setStyleByName("#B.sendCustoms", 		"display", "inline");
		vat.item.setStyleByName("#B.sendCancel", 		"display", "inline");
		vat.item.setValueByName("#F.customsStatus", "");
	  }else{
	    vat.item.setStyleByName("#B.sendCustoms", 		"display", "none");
		vat.item.setStyleByName("#B.sendCancel", 		"display", "none");
		vat.item.setValueByName("#F.customsStatus", "");
	  }
	}
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