
var afterSavePageProcess = "";
var vnB_Detail = 2;
var vnB_Header = 1;
function outlineBlock(){
 
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();
  
  doFormAccessControl();
}

// 搜尋初始化
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     	: document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" 	].value,	
  	     vatPickerId        	: document.forms[0]["#vatPickerId"       	].value,
  	     warehouseCode  		: document.forms[0]["#warehouseCode" 		].value,
  	     taxType				: document.forms[0]["#taxType" 				].value,
  	     adjustmentType			: document.forms[0]["#adjustmentType" 		].value,
  	     declarationNo			: document.forms[0]["#declarationNo" 		].value,
  	     declarationSeqStart	: document.forms[0]["#declarationSeqStart" 	].value,
  	     declarationSeqEnd		: document.forms[0]["#declarationSeqEnd" 	].value,
  	     customsItemCode		: document.forms[0]["#customsItemCode" 	].value,
  	     isAutoLoad				: document.forms[0]["#isAutoLoad" 	].value
	    };
	    
		vat.bean.init(	
	  		function(){
				return "process_object_name=cmDeclarationOnHandExtentionService&process_object_method_name=executeSearchInitial"; 
	    	},{								
	    		other: true
    	}); 
  }
}

// 可搜尋的欄位
function headerInitial(){
	 var allCustomsWarehouseCodes = vat.bean("allCustomsWarehouseCodes");
	 var allItemCategorys = vat.bean("allItemCategorys");
vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"報關單展延作業", rows:[  
	 		{row_style:"", cols:[
				{items:[{name:"#L.declarationNo", 			type:"LABEL"  , value:"報關單號"}]},
				{items:[{name:"#F.declarationNo", 			type:"TEXT"   , bind:"declarationNo", size:20}]},
			 	{items:[{name:"#L.declarationSeq", 			type:"LABEL"  , value:"報關序號"}]},
				{items:[{name:"#F.declarationSeqStart", 	type:"NUMB"   , bind:"declarationSeqStart", size:10},
						{name:"#L.between", 				type:"LABEL"  , value:" 至 "},
						{name:"#F.declarationSeqEnd", 		type:"NUMB"   , bind:"declarationSeqEnd", size:10}]},
				{items:[{name:"#L.customsWarehouseCode", 	type:"LABEL", 	value:"海關庫別" }]},
				{items:[{name:"#F.customsWarehouseCode", 	type:"SELECT", 	bind:"customsWarehouseCode" ,init:allCustomsWarehouseCodes  }]},
				{items:[{name:"#L.remainDays", 				type:"LABEL", 	value:"剩餘天數 <= " }]},
				{items:[{name:"#F.remainDays", 				type:"NUMB", 	bind:"remainDays" },
						{name:"#F.isOverZero",				type:"CHECKBOX", bind:"isOverZero"},
						{name:"#L.isOverZero",				type:"LABEL", 	value:"是否剩餘天數大於0"}]}
//				{items:[{name:"#L.brandCode", 				type:"LABEL"  , value:"品牌"}]},
//				{items:[{name:"#F.brandCode", 				type:"TEXT"   , size:20, mode:"READONLY"},
//						{name:"#F.brandName", 				type:"TEXT",  	bind:"brandName",  back:false ,size:8, mode:"READONLY"}]} , td:" colSpan=3"
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customsItemCode", 		type:"LABEL", 	value:"品號" }]},
				{items:[{name:"#F.customsItemCode", 		type:"TEXT", 	bind:"customsItemCode", size:20 },
						{name:"#I.append",					type:"IMG" , value:"新增", src:"./images/16x16/Add.png", eClick: function(){ changeAppend();} },
						{name:"#I.decrease",				type:"IMG" , value:"減少", src:"./images/16x16/Remove.png", eClick: function(){ changeDecrease();} },
						{name:"#L.br", 						type:"LABEL", 	value:"<br>" },
						{name:"#F.customsItemCodes",		type:"TEXTAREA" ,  bind:"customsItemCodes", mode:"READONLY", row:2, col: 20}]}, 
				{items:[{name:"#L.category01", 				type:"LABEL", 	value:"大類" }]},
				{items:[{name:"#F.category01", 				type:"SELECT", 	bind:"category01", init:allItemCategorys }]},
				{items:[{name:"#L.itemBrand", 				type:"LABEL", 	value:"商品品牌" }]},
				{items:[{name:"#F.itemBrand", 				type:"TEXT", 	bind:"itemBrand", eChange: function(){ changeCategoryCodeName("ItemBrand"); }},
						{name:"#B.itemBrand",	value:"選取" ,type:"PICKER" ,
	 									 		openMode:"open", src:"./images/start_node_16.gif",
	 									 		service:"Im_Item:searchCategory:20100119.page", 
	 									 		left:0, right:0, width:1024, height:768,
	 									 		servicePassData:function(){ return doPassData("ItemBrand"); },	
	 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("itemBrand");}},
	 					{name:"#F.itemBrandName",	type:"TEXT", 	bind:"itemBrandName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.warehouseInDate", 		type:"LABEL", 	value:"進倉日期" }]},
				{items:[{name:"#F.warehouseInDate", 		type:"DATE", 	bind:"warehouseInDate" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#F.showZeroStock", 		type:"CHECKBOX", 	bind:"showZeroStock" },
						{name:"#L.showZeroStock", 		type:"LABEL", 	value:"顯示可用庫存量為零之記錄" }], td:" colSpan=2"},
				{items:[{name:"#F.showNegativeStock", 	type:"CHECKBOX", 	bind:"showNegativeStock" },
						{name:"#L.showNegativeStock", 	type:"LABEL", 	value:"顯示可用庫存量為負之記錄" }], td:" colSpan=2"},
				{items:[{name:"#L.fileNo", 	type:"LABEL", 	value:"文號" }]},
				{items:[{name:"#F.fileNo", 	type:"TEXT",    size:30, 	bind:"fileNo" }], td:" colSpan=3"}
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function buttonLine(){
	var vsViewFunction = vat.bean().vatBeanOther.vatPickerId == "" ? "": "doClosePicker()";

    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.export"	   , type:"IMG"      ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"matchExport"	   , type:"LABEL"    ,value:"　"},
	 			{name:"#B.view"        , type:"IMG"        , value:"檢視",   src:"./images/button_view.gif"  , eClick:vsViewFunction},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.extention"        , type:"IMG"      , value:"展延",   src:"./images/button_extention.gif"  , eClick:'extention()'}	 	 		
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	if("" == vat.bean().vatBeanOther.vatPickerId){ 
		vat.item.setStyleByName("#B.view" , "display", "none");
	}	
}

function addItem(){
	//alert("addItem");
}

function detailInitial(){
  	
  	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;
	
	var vatPickerId = vat.bean().vatBeanOther.vatPickerId;   
 	var vbSelectionType = "CHECK";    
 	 
    //if(vatPickerId!= null && vatPickerId!= ""){
   		vat.item.make(vnB_Detail, "chkbox"            , {type:"XBOX", desc:"勾選" ,eClick:'addItem()' });
   	//}
   	//vat.item.make(vnB_Detail, "chkbox", {type:"XBOX" , size:8, view:"", desc:"勾選"      });
    vat.item.make(vnB_Detail, "indexNo"         	, {type:"IDX"  , desc:"序號", 	view:"fixed"      });
    vat.item.make(vnB_Detail, "customsWarehouseCode", {type:"TEXT" , size:8, view:"", mode:"READONLY", desc:"海關關別"      });
	vat.item.make(vnB_Detail, "declarationNo"       , {type:"TEXT" , size:15, view:"", mode:"READONLY", desc:"報關單號"      });
	vat.item.make(vnB_Detail, "declarationSeq"  	, {type:"TEXT" , size:3, view:"", mode:"READONLY", desc:"報關序號"      });
	vat.item.make(vnB_Detail, "remainDays"    		, {type:"NUMB" , size:12, view:"", mode:"READONLY", desc:"剩餘天數"		});
	vat.item.make(vnB_Detail, "declDate"       		, {type:"DATE" , size:12, view:"", mode:"READONLY", desc:"報關日期"      });
	vat.item.make(vnB_Detail, "importDate"    		, {type:"DATE" , size:12, view:"", mode:"READONLY", desc:"進口日期"      });
	vat.item.make(vnB_Detail, "warehouseInDate"    	, {type:"DATE" , size:12, view:"", mode:"READONLY", desc:"進倉日期"      });
	vat.item.make(vnB_Detail, "customsItemCode"    	, {type:"TEXT" , size:18, view:"", mode:"READONLY", desc:"品號"      });
	//vat.item.make(vnB_Detail, "extenQty"    , {type:"NUMB" , size:14, view:"",  desc:"展延申請數量"		});
	vat.item.make(vnB_Detail, "currentOnHandQty"    , {type:"NUMB" , size:8, view:"", mode:"READONLY", desc:"目前有效庫存"		});
	vat.item.make(vnB_Detail, "originalDate"    	, {type:"DATE" , size:12, view:"shift", mode:"READONLY", desc:"原D8日期"		});
	vat.item.make(vnB_Detail, "qty"    				, {type:"NUMB" , size:12, view:"shift", mode:"READONLY", desc:"進貨數量"		});
	vat.item.make(vnB_Detail, "expiryDate"    		, {type:"NUMB" , size:12, view:"shift", mode:"READONLY", desc:"屆期日"		});
	vat.item.make(vnB_Detail, "itemCName"    		, {type:"TEXT" , size:18, view:"shift", mode:"READONLY",alter:true, desc:"品名"		});
	vat.item.make(vnB_Detail, "unitPrice"    		, {type:"NUMB" , size:12, view:"shift", mode:"READONLY", desc:"售價"		});
	vat.item.make(vnB_Detail, "categoryType"    	, {type:"TEXT" , size:8, view:"shift", mode:"READONLY", desc:"業種"		});
	vat.item.make(vnB_Detail, "category01"    		, {type:"TEXT" , size:12, view:"shift", mode:"READONLY", desc:"大類"		});
	vat.item.make(vnB_Detail, "category01Name"    	, {type:"TEXT" , size:18, view:"shift", mode:"READONLY", desc:"大類名稱"	});
	vat.item.make(vnB_Detail, "category02"    		, {type:"TEXT" , size:12, view:"shift", mode:"READONLY", desc:"中類"		});
	vat.item.make(vnB_Detail, "category02Name"    	, {type:"TEXT" , size:18, view:"shift", mode:"READONLY", desc:"中類名稱"	});
	vat.item.make(vnB_Detail, "category03"    		, {type:"TEXT" , size:12, view:"shift", mode:"READONLY", desc:"小類"		});
	vat.item.make(vnB_Detail, "itemBrand"    		, {type:"TEXT" , size:12, view:"shift", mode:"READONLY", desc:"商品品牌"		});
	vat.item.make(vnB_Detail, "itemBrandName"    	, {type:"TEXT" , size:18, view:"shift", mode:"READONLY", desc:"商品品牌名稱"		});
	vat.item.make(vnB_Detail, "supplierCode"    	, {type:"TEXT" , size:12, view:"shift", mode:"READONLY", desc:"廠商代瑪"		});
	vat.item.make(vnB_Detail, "supplierName"    	, {type:"TEXT" , size:18, view:"shift", mode:"READONLY",alter:true, desc:"廠商名稱"		});
	
//	vat.item.make(vnB_Detail, "headId"      		, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["brandCode","customsWarehouseCode","declarationNo","declarationSeq","importDate"],
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});

}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
   // alert("loadBeforeAjxService");	
   
	var processString = "process_object_name=cmDeclarationOnHandExtentionService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +    
                      "&taxType"         		+ "=" + vat.bean().vatBeanOther.taxType +    					
  	    		 	  "&adjustmentType"         + "=" + vat.bean().vatBeanOther.adjustmentType +  	
	                  "&declarationNo"      	+ "=" + vat.item.getValueByName("#F.declarationNo"     ) +
	                  "&declarationSeqStart"    + "=" + vat.item.getValueByName("#F.declarationSeqStart"       ) +
	                  "&declarationSeqEnd"    	+ "=" + vat.item.getValueByName("#F.declarationSeqEnd"       ) +
	                  "&customsItemCode"       	+ "=" + vat.item.getValueByName("#F.customsItemCode"   ) +
	                  "&category01"       		+ "=" + vat.item.getValueByName("#F.category01") + 
					  "&itemBrand"       		+ "=" + vat.item.getValueByName("#F.itemBrand") +
					  "&warehouseInDate"       	+ "=" + vat.item.getValueByName("#F.warehouseInDate") +
					  "&remainDays"       		+ "=" + vat.item.getValueByName("#F.remainDays") +
					  "&isOverZero"       		+ "=" + vat.item.getValueByName("#F.isOverZero") +
					  "&isOverZeroStock"		+ "=" + vat.item.getValueByName("#F.isOverZeroStock") +
					  "&customsItemCodes"       + "=" + vat.item.getValueByName("#F.customsItemCodes") +
	                  "&customsWarehouseCode"   + "=" + vat.item.getValueByName("#F.customsWarehouseCode"  );     
                                                                            
	return processString;											
}

function saveSuccessAfter(){
	//alert("saveSuccessAfter");
}

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=cmDeclarationOnHandExtentionService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作
function doClosePicker(){
   vat.block.pageSearch(vnB_Detail, {
   		funcSuccess : 
   		    function(){
   		      vat.block.submit(
   		                     function(){ return "process_object_name=cmDeclarationOnHandExtentionService&process_object_method_name=getSearchSelection";
   		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
   			}
   }); 
}

// 查詢按下後
function doSearch(){
var sco = vat.block.getValue(vnB_Detail = 2, "timeScope");
//alert(sco);
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
}

// 展延按下後
function extention(){
var fileNo = vat.item.getValueByName("#F.fileNo");
var timeScope = vat.block.getValue(vnB_Detail = 2, "timeScope");
//alert(timeScope);
var allowExtention = false;
	if(fileNo != ""){
		allowExtention = true;
	}
    if(allowExtention){
    	if(confirm("請確認是否針對此查詢中的報單執行展延動作!")){
    		vat.block.submit(function(){return "process_object_name=cmDeclarationOnHandExtentionService"+
			                    "&process_object_method_name=updateDeclarationExtention&fileNo="+ fileNo + "&timeScope="+timeScope }, 
			                    {bind:true, link:true, other:true, picker:true, isPicker:true, blockId:vnB_Detail, 
			                     funcSuccess: function() {
			                     	alert("報單展延完成");
			                     	//vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);
			                     }
			                    });
    	}
    }else{
    	alert("請輸入文號");
    }
}

function doExtention(){
var fileNo = vat.item.getValueByName("#F.fileNo");
var timescop = vat.bean().vatBeanOther.timeScope;
alert(timescop);
var allowExtention = false;
	if(fileNo != ""){
		allowExtention = true;
	}
    if(allowExtention){
    	vat.ajax.XHRequest({
		post:"process_object_name=cmDeclarationOnHandExtentionService"+
                  "&process_object_method_name=updateExtention"+
                  "&fileNo=" + fileNo +
                  "&timeScope="+vat.bean().vatBeanOther.timeScope,
                  
		find: function change(oXHR){ 
				alert("success"+oXHR.responseText);
         		//vat.item.setValueByName(name, vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		alert("error");
         		//vat.item.setValueByName(name, "查無此類別");
		}   
		});	
    }else{
    	alert("請輸入文號");
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

// 清除
function resetForm(){
	vat.item.setValueByName("#F.declarationNo", "");
	vat.item.setValueByName("#F.declarationSeqStart", "");
	vat.item.setValueByName("#F.declarationSeqEnd", "");
	vat.item.setValueByName("#F.isOverZero", "Y");
	vat.item.setValueByName("#F.showZeroStock", "N");
	vat.item.setValueByName("#F.showNegativeStock", "N");
	vat.item.setValueByName("#F.remainDays", "");
	vat.item.setValueByName("#F.customsItemCodes", "");
	vat.item.setValueByName("#F.customsItemCode", "");
	vat.item.setValueByName("#F.category01", "");
	vat.item.setValueByName("#F.itemBrand", "");
	vat.item.setValueByName("#F.warehouseInDate", "");
	vat.item.setValueByName("#F.customsWarehouseCode", "");
	vat.item.setValueByName("#F.fileNo", "");
	location.reload();
}

// 傳參數
function doPassData(id){
	var suffix = "";
	switch(id){
		case "ItemBrand":
			suffix += "&categoryType="+escape("ItemBrand");
			break;
	}
	return suffix;
}

// 品牌 picker 回來執行
function doAfterPickerFunctionProcess(key){
	//do picker back something
	switch(key){
		case "itemBrand":
			if(typeof vat.bean().vatBeanPicker.categoryResult != "undefined"){
		    	vat.item.setValueByName("#F.itemBrand", vat.bean().vatBeanPicker.categoryResult[0]["id.categoryCode"]); 
				changeCategoryCodeName("ItemBrand");

			}
		break;
		
	}
}

// 動態改變商品類別名稱
function changeCategoryCodeName(code){
	var condition = "", name ="";
	
	switch(code){
		case "ItemBrand":
			condition =  "&categoryCode=" + ( "ItemBrand" === code ? vat.item.getValueByName("#F.itemBrand") : "" );
			name = "#F.itemBrandName";
		break;
	}
	
	vat.ajax.XHRequest({
		post:"process_object_name=imItemCategoryService"+
                  "&process_object_method_name=getAJAXCategoryName"+
                  "&brandCode=" + vat.bean().vatBeanOther.loginBrandCode + 
                  "&categoryType=" + code + condition,
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName(name, vat.ajax.getValue("categoryName", oXHR.responseText));
		},
		fail: function changeError(){
         		vat.item.setValueByName(name, "查無此類別");
		}   
	});	
}

//sql明細匯出excel
function doExport(){
    var url = "/erp/jsp/ExportFormView.jsp" + 
              "?exportBeanName=CM_ON_HAND" + 
              "&fileType=XLS" + 
              "&processObjectName=cmDeclarationOnHandExtentionService" + 
              "&processObjectMethodName=getAJAXExportData" + 
              "&brandCode=" + vat.bean().vatBeanOther.loginBrandCode + 
              "&customsWarehouseCode=" + vat.item.getValueByName("#F.customsWarehouseCode") + 
	          "&declarationNo=" + vat.item.getValueByName("#F.declarationNo") + 
	          "&declarationSeqStart=" + vat.item.getValueByName("#F.declarationSeqStart") + 
	          "&remainDays=" + vat.item.getValueByName("#F.remainDays") +
			  "&isOverZero=" + vat.item.getValueByName("#F.isOverZero") +
			  "&showZeroStock=" + vat.item.getValueByName("#F.showZeroStock") +
			  "&showNegativeStock=" + vat.item.getValueByName("#F.showNegativeStock") +
	          "&customsItemCode=" + vat.item.getValueByName("#F.customsItemCode") +
	          "&customsItemCodes=" + vat.item.getValueByName("#F.customsItemCodes") + 
	          "&category01=" + vat.item.getValueByName("#F.category01") + 
	          "&itemBrand=" + vat.item.getValueByName("#F.itemBrand") + 
	          "&warehouseInDate=" + vat.item.getValueByName("#F.warehouseInDate");
    var width = "200";
    var height = "30";  
    window.open(url, '報單可用庫存匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

}

// 依狀態鎖form
function doFormAccessControl(){
	vat.item.setValueByName("#F.isOverZero", "Y");
	vat.item.setValueByName("#F.showZeroStock", "N");
	vat.item.setValueByName("#F.showNegativeStock", "N");
	
	var isAutoLoad = vat.bean().vatBeanOther.isAutoLoad;
	var customsItemCode = vat.item.getValueByName("#F.customsItemCode");
	if("Y" == isAutoLoad){
		if("" != customsItemCode ){
			vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
		}
	}
}

// 新增多品號查詢條件
function changeAppend(){
	var customsItemCode = vat.item.getValueByName("#F.customsItemCode").replace(/^\s+|\s+$/, '');
	if("" != customsItemCode){
		vat.item.setValueByName("#F.customsItemCode", "");
		var customsItemCodes = vat.item.getValueByName("#F.customsItemCodes");
		var count = customsItemCodes.split(","); 
		if( "" == customsItemCodes && count.length == 1){
			customsItemCodes = customsItemCodes + customsItemCode;
		}else{
			customsItemCodes = customsItemCodes + "," + customsItemCode;
		}
		vat.item.setValueByName("#F.customsItemCodes", customsItemCodes);
	}
}
// 減少多品號查詢條件
function changeDecrease(){
	var customsItemCode = vat.item.getValueByName("#F.customsItemCode").replace(/^\s+|\s+$/, '');
	if("" != customsItemCode){
		vat.item.setValueByName("#F.customsItemCode", "");
		var customsItemCodes = vat.item.getValueByName("#F.customsItemCodes");
//		var pattern = /^customsItemCodes$/;// 找出相同品號
//		alert("pattern = " + pattern);
		var array = customsItemCodes.split(",");
		for(var i = 0; i < array.length; i++){
			if(array[i] == customsItemCode){
				array.splice(i,1);
				break;
			}
		}
//		customsItemCodes.replace(customsItemCode, "");
		vat.item.setValueByName("#F.customsItemCodes", array);
	}
}