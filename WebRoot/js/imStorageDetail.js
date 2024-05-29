/*
 *	檔案:imStorageDetail.js
 *	說明：
 *	修改：
 *  <pre>
 *  	All rights reserved.
 *  </pre>
 */

var storageDetailKey = "ImStorageItem";
var delivery = true;
var arrival = true;

function kweStorageBlock(){
	
	vat.tabm.createDivision("vatStorageDiv");
	kweStorage();
	vat.tabm.endDivision();
	if( "SAVE" == vat.item.getValueByName(vat.bean().vatBeanOther.storageStatus) ){
		vat.block.canGridModify( vatStorageDetail, true, true, true );
	}else{
		vat.block.canGridModify( vatStorageDetail, false, false, false );
	}
	
	if("MOVE" == vat.bean().vatBeanOther.storageTransactionType){
		//nothing
	}else{
		delivery = false;
	}
	
	//vat.item.setGridAttributeByName("ImStorageItemdeliveryWarehouseCode", "readOnly", !delivery);
	vat.item.setGridAttributeByName("ImStorageItemdeliveryStorageCode", "readOnly", !delivery);
	//vat.item.setGridAttributeByName("ImStorageItemarrivalWarehouseCode", "readOnly", !arrival);
	vat.item.setGridAttributeByName("ImStorageItemarrivalStorageCode", "readOnly", !arrival);
	vat.block.pageDataLoad(vatStorageDetail, vnCurrentPage = 1);
	
}


// 儲位單明細
function kweStorage(){
	
	vat.item.make(vatStorageDetail, storageDetailKey + "indexNo"				, {type:"IDX" , desc:"序號" });
	vat.item.make(vatStorageDetail, storageDetailKey + "itemCode"				, {type:"TEXT", size:16, desc:"品號", onchange:"onChangeStorageItemCode()"});
	vat.item.make(vatStorageDetail, storageDetailKey + "itemCName"				, {type:"TEXT", size:24, desc:"品名", mode:"READONLY" });
	vat.item.make(vatStorageDetail, storageDetailKey + "storageInNo"			, {type:"NUMB" , size:8, desc:"進倉日" });
	vat.item.make(vatStorageDetail, storageDetailKey + "storageLotNo"			, {type:"NUMB" , size:8, desc:"批號效期" });
	vat.item.make(vatStorageDetail, storageDetailKey + "deliveryWarehouseCode"	, {type:"TEXT" , size:5, desc:"轉出庫", mode:"READONLY" });
	vat.item.make(vatStorageDetail, storageDetailKey + "deliveryStorageCode"	, {type:"NUMB" , size:4, desc:"轉出儲位"});
	vat.item.make(vatStorageDetail, "searchStroage"	              , {type:"PICKER", desc:"", openMode:"open", src:"./images/start_node_16.gif",
	 									 		            	service:"Im_Storage:onHand:20111011.page",
	 									 			            left:0, right:0, width:1024, height:768,
	 									 						servicePassData:function(x){ return doPassLineData(x); },
	 									 		                serviceAfterPick:function(xx){doLineAfterPickerProcess(xx); } });
	vat.item.make(vatStorageDetail, storageDetailKey + "storageQuantity"		, {type:"NUMB", size:3, desc:"數量"});
	vat.item.make(vatStorageDetail, storageDetailKey + "arrivalWarehouseCode"	, {type:"TEXT" , size:5, desc:"轉入庫", mode:"READONLY" });
	vat.item.make(vatStorageDetail, storageDetailKey + "arrivalStorageCode"		, {type:"NUMB" , size:4, desc:"轉入儲位"});
	vat.item.make(vatStorageDetail, storageDetailKey + "storageLineId"          , {type:"HIDDEN"});
	vat.item.make(vatStorageDetail, storageDetailKey + "isLockRecord"    		, {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vatStorageDetail, storageDetailKey + "isDeleteRecord"  		, {type:"DEL"  , desc:"刪除"});
	vat.item.make(vatStorageDetail, storageDetailKey + "message"         		, {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vatStorageDetail, {
							id: "vatStorageDetailDiv",
							pageSize: 10,											
	            			canGridDelete : true,
							canGridAppend : true,
							canGridModify : true,
							appendBeforeService : "appendBeforeStorageService()",
							appendAfterService  : "appendAfterStorageService()",
							loadBeforeAjxService: "loadBeforeAjxStorageService()",
							loadSuccessAfter    : "loadSuccessStorageAfter()",						
							eventService        : "eventStorageService('Y')",   
							saveBeforeAjxService: "saveBeforeAjxStorageService()",
							saveSuccessAfter    : "saveSuccessStorageAfter()"
						});
}


// 刷新讀取明細
function reloadStorageDetail(){
	vat.block.pageRefresh(vatStorageDetail);
}


// 第一次載入 重新整理
function loadBeforeAjxStorageService(){
	var processString = "process_object_name=imStorageService&process_object_method_name=getAJAXPageData" + 
	                    "&storageHeadId=" + vat.item.getValueByName("#F.storageHeadId");
	return processString;	
}


// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxStorageService(){
	var processString = "process_object_name=imStorageService&process_object_method_name=updateAJAXPageData" + 
	                    "&storageHeadId=" + vat.item.getValueByName("#F.storageHeadId") +
	                    "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode;
	return processString;	
}


// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessStorageAfter(){ 

} 


// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessStorageAfter(){

} 


// 新增空白頁
function appendBeforeStorageService(){
	return true;
}    


// 新增空白頁成功後
function appendAfterStorageService(){

}


// 輸入明細品號
function onChangeStorageItemCode() {
	var deliveryWarehouse = vat.bean().vatBeanOther.deliveryWarehouse;
	var arrivalWarehouse = vat.bean().vatBeanOther.arrivalWarehouse;
	var nItemLine = vat.item.getGridLine();
	var sItemCode = vat.item.getGridValueByName(storageDetailKey + "itemCode",nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	
	vat.item.setGridValueByName(storageDetailKey + "itemCode", nItemLine, sItemCode);
	//vat.item.setGridValueByName(storageDetailKey + "storageInNo", nItemLine, "00000000");
	//vat.item.setGridValueByName(storageDetailKey + "storageLotNo", nItemLine, "00000000");
	
	if("MOVE" == vat.bean().vatBeanOther.storageTransactionType){
		//vat.item.setGridValueByName(storageDetailKey + "deliveryStorageCode", nItemLine, "00000000");
		vat.item.setGridValueByName(storageDetailKey + "arrivalStorageCode", nItemLine, "00000000");
	}else{
		vat.item.setGridValueByName(storageDetailKey + "arrivalStorageCode", nItemLine, "00000000");
	}
	
	if (sItemCode != "") {
		var processString = "process_object_name=imStorageService&process_object_method_name=getAJAXItemCode" + 
							"&brandCode="         + vat.bean().vatBeanOther.form.brandCode + 
							"&itemCode="          + sItemCode;					   
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					vat.item.setGridValueByName(storageDetailKey + "itemCName", nItemLine, vat.ajax.getValue("ItemCName", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName(storageDetailKey + "deliveryWarehouseCode", nItemLine, vat.item.getValueByName(deliveryWarehouse));
					vat.item.setGridValueByName(storageDetailKey + "arrivalWarehouseCode", nItemLine, vat.item.getValueByName(arrivalWarehouse));
				}
			}
		});
	}
}


// 按下自定事件刷新明細
function eventStorageService(isClean){
	alert("執行自動重新匹配儲位中");
	vat.block.pageDataSave(vatStorageDetail,{  
		funcSuccess:function(){
		var deliveryWarehouse = vat.bean().vatBeanOther.deliveryWarehouse;
		var arrivalWarehouse = vat.bean().vatBeanOther.arrivalWarehouse;
		var storageStatus = vat.bean().vatBeanOther.storageStatus;
		var warehouseInDate = vat.item.getValueByName("#F.warehouseInDate");

		var processString = "process_object_name=imStorageAction&process_object_method_name=executeAJAXImStorageItem" + 
							"&storageHeadId="	+ vat.item.getValueByName("#F.storageHeadId") + 
							"&headId="			+ vat.item.getValueByName("#F.headId") +
							"&beanHead="		+ vat.bean().vatBeanOther.beanHead + 
							"&beanItem="		+ vat.bean().vatBeanOther.beanItem + 
							"&quantity="		+ vat.bean().vatBeanOther.quantity +
							"&storageTransactionType="	+ vat.bean().vatBeanOther.storageTransactionType + 
							"&deliveryWarehouseCode=" + vat.item.getValueByName(deliveryWarehouse) +
							"&arrivalWarehouseCode=" + vat.item.getValueByName(arrivalWarehouse) +
							"&pickOrderTypeCode=" + vat.item.getValueByName("#F.originalOrderTypeCode") +
							"&pickOrderNo=" + vat.item.getValueByName("#F.originalOrderNo") +
							"&storageStatus=" + vat.item.getValueByName(storageStatus) +
							"&warehouseInDate=" + warehouseInDate +
							"&isClean=" + isClean;
	
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					if("" != vat.ajax.getValue("errorMsg", vat.ajax.xmlHttp.responseText))
						alert(vat.ajax.getValue("errorMsg", vat.ajax.xmlHttp.responseText));
					else
						vat.block.pageDataLoad(vatStorageDetail, vnCurrentPage = 1);
				}
			}
		});
	}});
}

function deleteImStorageItems(){
   
   var processString = "process_object_name=imStorageAction&process_object_method_name=deleteImStorageItem" + 
							"&storageHeadId="	+ vat.item.getValueByName("#F.storageHeadId") + 
							"&headId="			+ vat.item.getValueByName("#F.headId");
	
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
					if("" != vat.ajax.getValue("errorMsg", vat.ajax.xmlHttp.responseText))
						alert(vat.ajax.getValue("errorMsg", vat.ajax.xmlHttp.responseText));
					else{
						
						vat.block.pageDataLoad(vatStorageDetail, vnCurrentPage = 1);
				    }
				}
			}
		});
   
}
// 按下明細Picker
function doPassLineData(x){
	vat.block.pageDataSave( vatStorageDetail, {refresh:false});

	var suffix = "";
	var vLineId	      = vat.item.getGridLine(x);
	var vItemCode       = vat.item.getGridValueByName(storageDetailKey + "itemCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();
	var vDeliveryWarehouseCode       = vat.item.getGridValueByName(storageDetailKey + "deliveryWarehouseCode", vLineId).replace(/^\s+|\s+$/, '').toUpperCase();

	//alert("LineId:"+vLineId);
	suffix += "&itemCode="+escape(vItemCode)+
				"&warehouseCode="+escape(vDeliveryWarehouseCode);
	//alert("suffix = " + suffix);
	return suffix;
}


// 明細Picker回傳
function doLineAfterPickerProcess(xx){
	//alert('doLineAfterPickerProcess');
	var vLineId	      = vat.item.getGridLine(xx);
	//alert('vLineId = ' + vLineId);
	
	if(vat.bean().vatBeanPicker.imOnHandResult != null){
		vat.item.setGridValueByName(storageDetailKey+"itemCode", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].id_itemCode);
		vat.item.setGridValueByName(storageDetailKey+"storageLotNo", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].id_storageLotNo);
		vat.item.setGridValueByName(storageDetailKey+"storageInNo", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].id_storageInNo);
		if("MOVE" == vat.bean().vatBeanOther.storageTransactionType)
			vat.item.setGridValueByName(storageDetailKey+"deliveryStorageCode", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].id_storageCode);
		vat.item.setGridValueByName(storageDetailKey+"arrivalStorageCode", vLineId, "00000000");
		vat.item.setGridValueByName(storageDetailKey+"itemCName", vLineId, vat.bean().vatBeanPicker.imOnHandResult[0].itemCName);
	}
}

// 匯出
function exportStorageFormData(){
	//alert('exportStorageFormData');
	var url = "/erp/jsp/ExportFormData.jsp" + 
              "?exportBeanName=IM_STORAGE" +
              "&fileType=XLS" + 
              "&processObjectName=imStorageService" + 
              "&processObjectMethodName=getBeanByHeadId" + 
              "&gridFieldName=imStorageItems" + 
              "&arguments=" + vat.item.getValueByName("#F.storageHeadId") +
              "&parameterTypes=LONG";
    var width = "200";
    var height = "30";
    window.open(url, '儲位單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}


// 匯入
function importStorageFormData(){
	//alert('importStorageFormData');
	var width = "600";
    var height = "400";
    return "&importBeanName=IM_STORAGE" + 
		"&importFileType=XLS" +
        "&processObjectName=imStorageService" + 
        "&processObjectMethodName=executeImportLists" +
        "&arguments=" + vat.item.getValueByName("#F.storageHeadId") + (("" == vat.item.getValueByName("#F.warehouseInDate"))?"":"{$}" + vat.item.getValueByName("#F.warehouseInDate")) +  
        "&parameterTypes=LONG"+ (("" == vat.item.getValueByName("#F.warehouseInDate"))?"":"{$}DATE") + 
        "&blockId=" + vatStorageDetail;
}


// 匯入結束
function afterImportStorageSuccess(){
	onChangeStorageWarehouseCode();
}
