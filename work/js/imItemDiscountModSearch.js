/*** 
 *	檔案: imItemDiscouuntMod.js
 *	說明: 商品折扣維護作
 */
 
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
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,
  	     formId           	: document.forms[0]["#formId"            	].value,
  	     vatPickerId        : document.forms[0]["#vatPickerId"       	].value
	    };
	    
	    vat.bean.init(	
	  		function(){
				return "process_object_name=imItemDiscountAction&process_object_method_name=performImItemDiscountformInitial";
				  	},{								
	    		other: true
    	}); 
   		
  }
}

//可搜尋的欄位
//先以手動方式輸入資料
function headerInitial(){
	
	var allVipTypeCodes = vat.bean("allVipTypeCodes"); // 折扣卡別下拉選單，在 service.executeInitial() 中取得 DAO 的值  
	var allItemDiscountTypes = vat.bean("allItemDiscountTypes"); // 商品折扣類型下拉選單，在 service.executeInitial() 中取得 DAO 的值 
	
	vat.block.create( 
		vnB_Header, 
		{
			id: "vatBlock_Head", 
			generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"商品折扣查詢作業", 
			rows:[  
	 			{
	 			row_style:"", 
	 			cols:[
	 				{items:[{name:"#F.headId",           type:"TEXT"   , bind:"headId",     size:10, mode:"hidden" }]},
	 				{items:[{name:"#L.brandCode", 		 type:"LABEL"  , value:"商品折品牌"}]},	 
	 				{items:[{name:"#F.loginBrandCode", 	 type:"TEXT"   , bind:"loginBrandCode", size:6, mode:"READONLY"}]},
					{items:[{name:"#L.vipTypeCode", 	 type:"LABEL"  , value:"商品折扣卡別"}]},
					{items:[{name:"#F.vipTypeCode", 	 type:"SELECT" , bind:"vipTypeCode", size:12, maxLen:12, init: allVipTypeCodes}]},
					{items:[{name:"#L.itemDiscountType", type:"LABEL"  , value:"商品折扣類型"}]},
					{items:[{name:"#F.itemDiscountType", type:"SELECT" , bind:"itemDiscountCode", size:12, maxLen:12, init: allItemDiscountTypes}]}
				]
				}
			], 	 
			beginService:"",
			closeService:""			
		}
	);

}

function buttonLine(){
    vat.block.create(
    	vnB_Button = 0, 
    	{
			id: "vatBlock_Button", 
			generate: true,
			table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
			title:"", 
			rows:[  
	 			{
	 				row_style:"", 
	 				cols:[
	 						{
	 							items:[
	 								{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 								{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 								{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 								{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 							{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 							{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 								{name:"#B.view"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
	 							],
	 			  				td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
	 			  			}
	 			  	]
	 			  }
	  		], 	 
			beginService:"",
			closeService:""			
		}
	);
}
	
function detailInitial(){
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
  
    vat.item.make(vnB_Detail, "checked"       	 	 , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"       	 	 , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "orderNo"			 	 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"單號"});
	vat.item.make(vnB_Detail, "brandCode"			 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"商品折扣品牌"});
	vat.item.make(vnB_Detail, "vipTypeCode"  	 	 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"折扣卡別"});
	vat.item.make(vnB_Detail, "itemDiscountType"	 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"商品折扣類型"});
	vat.item.make(vnB_Detail, "beginDate"     		 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"折扣起始時間"});
	vat.item.make(vnB_Detail, "endDate"     		 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"折扣結束時間"});
	vat.item.make(vnB_Detail, "discount"     		 , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"折扣"});
	vat.item.make(vnB_Detail, "status"				 , {type:"TEXT",  size:12, mode:"READONLY", desc:"狀態" });	
	vat.item.make(vnB_Detail, "createdBy"     	     , {type:"TEXT" , size:18, maxLen:20, mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "creationDate"     	 , {type:"TEXT" , size:18, maxLen:20, mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "enable"     	   		 , {type:"TEXT" , size:18, maxLen:20, mode:"HIDDEN", desc:"啟用"});
	vat.item.make(vnB_Detail, "headId"      		 , {type:"ROWID"});
		
	vat.block.pageLayout(
		vnB_Detail, 
			{
				id                  : "vatDetailDiv",
				pageSize            : 10,
				searchKey           : ["headId"],
				selectionType       : "CHECK",
				indexType           : "AUTO",
				canGridDelete       : vbCanGridDelete,
				canGridAppend       : vbCanGridAppend,
				canGridModify       : vbCanGridModify,	
				loadBeforeAjxService: "loadBeforeAjxService()",	
				loadSuccessAfter    : "loadSuccessAfter()", 						
				saveBeforeAjxService: "saveBeforeAjxService()",
				saveSuccessAfter    : "saveSuccessAfter()"
			}
	);

}

// 載入成功後
function loadSuccessAfter(){
	//alert("loadSuccessAfter");
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	   	vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
   //alert("loadBeforeAjxService");	
   
	var processString = "process_object_name=imItemDiscountService&process_object_method_name=getAJAXSearchModPageData" + 
	                  	"&brandCode"       	+ "=" + vat.item.getValueByName("#F.brandCode"   		) +
	                  	"&vipTypeCode"    	+ "=" + vat.item.getValueByName("#F.vipTypeCode"       	) +
	                  	"&itemDiscountType" + "=" + vat.item.getValueByName("#F.itemDiscountType"  	);                                                                            
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
		processString = "process_object_name=imItemDiscountService&process_object_method_name=saveSearchModResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
	vat.block.pageSearch(
		vnB_Detail, 
		{
	    	funcSuccess : function(){
	    		vat.block.submit(
					function(){
						return "process_object_name=imItemDiscountService&process_object_method_name=getSearchSelection";
					},
					{bind : true, link : false, other : true, picker : true, isPicker : true, blockId : vnB_Detail} 
	    		);
	    	}
	    }
	); 
}

// 查詢按下後
function doSearch(){
    vat.block.submit(
    	function(){
    		return "process_object_name=tmpAjaxSearchDataService"+
			   	   "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope ;
		}, 
		{
			other: true, 
			funcSuccess: function() {
				vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);
			}
		}
	);
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
    vat.item.setValueByName("#F.vipTypeCode", "");
	vat.item.setValueByName("#F.itemDiscountType", "");
}
