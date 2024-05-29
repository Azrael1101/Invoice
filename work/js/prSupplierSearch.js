/*** 
 *	檔案: imItemDiscount.js
 *	說明：
 *	修改：Boris
 *  <pre>
 *  	Created by Boris
 *  	All rights reserved.
 *  </pre>
 */
 

var afterSavePageProcess = "";
var vnB_Detail = 2;
var vnB_Header = 1;
function outlineBlock(){
 	
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();

}

// 搜尋初始化
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value
  	     //vatPickerId        : document.forms[0]["#vatPickerId"       	].value
	    };
	    vat.bean.init(	
	  		function(){
				return "process_object_name=imItemDiscountAction&process_object_method_name=performPrSupplierSearchInitial";
				  	},{								
	    		other: true
    	}); 
   		
  }
}

//可搜尋的欄位
//先以手動方式輸入資料
function headerInitial(){
	//var allVipTypeCodes = vat.bean("allVipTypeCodes"); // 折扣卡別下拉選單，在 service.executeInitial() 中取得 DAO 的值  
	//var allItemDiscountTypes = vat.bean("allItemDiscountTypes"); // 商品折扣類型下拉選單，在 service.executeInitial() 中取得 DAO 的值 
	
	vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"資訊廠商查詢作業", rows:[  
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.orderNo", 	type:"LABEL", 	value:"單號"}]},
	 			{items:[{name:"#F.orderNo", 	type:"TEXT",  	bind:"orderNo", size:15}]},
	 			{items:[{name:"#L.supplierNo", 	type:"LABEL", 	value:"廠商編號"}]},	 
	 			{items:[{name:"#F.supplierNo", 	type:"TEXT",  	bind:"supplierNo", size:6}]},
				{items:[{name:"#L.supplier", 		type:"LABEL"  , value:"廠商名稱"}]},
				{items:[{name:"#F.supplier", 		type:"TEXT" , bind:"supplier", size:12, maxLen:12}]},
				{items:[{name:"#L.unifiedUnmbering", 	type:"LABEL"  , value:"統一編號"}]},
				{items:[{name:"#F.unifiedUnmbering", 	type:"TEXT" , bind:"unifiedUnmbering", size:8, maxLen:8}]}
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function buttonLine(){
	vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", 
	rows:[  
		{row_style:"", cols:[
			{items:[
				{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#B.update"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
			
			],
			td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
		]}
	], 	 
		beginService:"",
		closeService:""			
	});
}
	
function detailInitial(){
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
  var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    vat.item.make(vnB_Detail, "checked"       	 	 , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"       	 	 , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "orderNo"			 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "supplierNo"  	 	 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"廠商代碼"      });
	vat.item.make(vnB_Detail, "supplier"	 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"廠商名稱"      });
	vat.item.make(vnB_Detail, "unifiedUnmbering"     		 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"統一編號"      });
	
	
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["supplierNo"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								           				canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
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
   
	var processString = "process_object_name=prSupplierService&process_object_method_name=getAJAXSearchPageData" + 
	                  "&orderNo"       	+ "=" + vat.item.getValueByName("#F.orderNo"   		) +
	                  "&supplierNo"    	+ "=" + vat.item.getValueByName("#F.supplierNo"       	) +
	                  "&supplier"    	+ "=" + vat.item.getValueByName("#F.supplier"       	) +
	                  "&unifiedUnmbering"   + "=" + vat.item.getValueByName("#F.unifiedUnmbering"  	);                                                                            
	return processString;											
}

function saveSuccessAfter(){
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
		processString = "process_object_name=prSupplierService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=prSupplierService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

// 查詢按下後
function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope ;}, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
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
    vat.item.setValueByName("#F.orderNo", "");
	vat.item.setValueByName("#F.supplierNo", "");
	vat.item.setValueByName("#F.supplier", "");
	vat.item.setValueByName("#F.unifiedUnmbering", "");
}

function openModifyPage(){
	

    var nItemLine = vat.item.getGridLine();
	var brandCode = vat.item.getGridValueByName("brandCode",nItemLine);
	var vipTypeCode = vat.item.getGridValueByName("vipTypeCode",nItemLine);
    var itemDiscountType = vat.item.getGridValueByName("itemDiscountType", nItemLine);
    var beginDate = vat.item.getGridValueByName("beginDate",nItemLine);
    var endDate = vat.item.getGridValueByName("endDate",nItemLine);
    var discount = vat.item.getGridValueByName("discount",nItemLine);
    var createdBy = vat.item.getGridValueByName("createdBy",nItemLine);
    var creationDate = vat.item.getGridValueByName("creationDate",nItemLine);
     var enable = vat.item.getGridValueByName("enable",nItemLine);
	if(!(brandCode == "" && vipTypeCode == "" && itemDiscountType == "")){
    var url = "/erp/Im_ItemDiscount:create:20111207.page?brandCode=" + brandCode + 
    				"&vipTypeCode="+vipTypeCode+
    				"&itemDiscountType="+itemDiscountType+
    				"&beginDate="+beginDate+
    				"&endDate="+endDate+
    				"&discount="+discount+
    				"&createdBy="+createdBy+
    				"&creationDate="+creationDate+
    				"&enable="+enable+
    				"&isUpdate=true"; 
    				
	sc=window.open(url, '商品折扣維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
	}
}