 /*** 
 *	檔案: buCustomer.js
 *	說明: 類別代號,抽成率維護
 */
 

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){ 
 	formInitial();
  	headerInitial();
  	kweButtonLine();
	kweImDetail1();
	focusPosBar();
	//doFormAccessControl();
}
/**鎖定pos bar輸入**/
function focusPosBar(){
	var posBarCode = document.getElementById("#F.itemCode");
	window.setTimeout(function(){
			posBarCode.focus();
	},1000);
}			
//初始化
function formInitial(){
 	
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
       {  
          brandCode  			: document.forms[0]["#loginBrandCode"    ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          loginDepartment  	    : document.forms[0]["#loginDepartment"   ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId      	: document.forms[0]["#assignmentId"      ].value,
          category		      	: document.forms[0]["#category"      	 ].value, 
          customerCode	      	: document.forms[0]["#customerCode"    	 ].value,   
          superintendentCode	: document.forms[0]["#superintendentCode"    	 ].value,      	  
          beforeStatus			: "beforestatus",
	      nextStatus			: "nextstatus",
	 //    approvalResult		: "approvalResult",
	 //    approvalComment		: "approvalComment",       
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        }; 
          vat.bean.init(  
             function(){
                    return "process_object_name=soDepartmentOrderAction&process_object_method_name=performGoodsSearchInitial"; 
         },{
             other: true
        } 
        );
  }

}


function headerInitial(){ 
var searchWays = [["","other",false],["無","依顏色","依系列"],["other","byColor","bySeries"]];
var isShowOwn = [["","",false],["是","否"],["Y","N"]];
var allWarehouses = vat.bean("allWarehouses");
//var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
//var allItemCategories     = vat.bean("allItemCategories");
var allCategory01 = vat.bean("allCategory01");
var allCategory02 = vat.bean("allCategory02");
var allCategory03 = vat.bean("allCategory03");
var allCategory13 = vat.bean("allCategory13");
//var allCategory07 = vat.bean("allCategory07");
//var allCategory09 = vat.bean("allCategory09");
//var allCategory13 = vat.bean("allCategory13");
//var allItemBrands = vat.bean("allItemBrands");
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"商品快速查詢", 
		rows:[
		{row_style:"", cols:[
				{items:[{name:"#L.itemCode" , type:"LABEL"   , value:"品號"}]},
	 		    {items:[{name:"#F.itemCode" , type:"TEXT"	 ,  eBlur:"" ,size:30,maxLen:30, bind:"itemCode",eChange:"changeItemCode()"}]},
	 		    {items:[{name:"#L.brandCode" , type:"LABEL"  , value:"品牌"}]},
	 		    {items:[{name:"#F.brandCode" , type:"TEXT"	 ,   bind:"brandCode",size:30,maxLen:30}]},
	 		    //{items:[{name:"#F.ffff" , type:"IMG"  , src:"file://10.5.100.100/script/img1.gif"}], td:"rowSpan=6"}
	 		    {items:[{name:"#F.itemImage" , type:"IMG"  , src:"./images/departmentPos/itemImage/"+vat.bean().vatBeanOther.brandCode+".GIF"}], td:"rowSpan=6"}
	 		    //{items:[{name:"#F.itemImage" , type:"IMG"  , src:"\\localhost\img\T1GS\GSJ80823P.gif"}], td:"rowSpan=6"}
	 		    ]},
	 	{row_style:"", cols:[
				{items:[{name:"#L.itemCName" , type:"LABEL"  , value:"品名"}]},
	 		    {items:[{name:"#F.itemCName" , type:"TEXT"	 ,   bind:"itemCName",size:30,maxLen:30}]},
	 		    {items:[{name:"#L.category01" , type:"LABEL" , 	value:"大類"}]},
				 {items:[{name:"#F.category01"	, type:"SELECT", 	init:allCategory01,bind:"category01"}]}
	 		    ]},
	 	{row_style:"", cols:[
				{items:[{name:"#L.warehouseCode" , type:"LABEL"  , value:"庫別"}]},
	 		    {items:[{name:"#F.warehouseCode" , type:"SELECT"	 ,   init:allWarehouses,bind:"itemCName"},
	 		    		{name:"#F.isShowOwn" , type:"CHECKBOX", bind:"isShowOwn", eClick:"refreshDetail()"},
	 		    		{name:"#L.isShowOwn" , type:"LABEL", value:"是否只顯示本店庫存"}]},
	 		    {items:[{name:"#L.category02" , type:"LABEL" , 	value:"中類"}]},
				 {items:[{name:"#F.category02" , type:"SELECT", 	init:allCategory02, bind:"category02"}]}
	 		    ]},
	 	{row_style:"", cols:[
				{items:[{name:"#L.currentOnHandQty" , type:"LABEL"  , value:"庫存"}]},
	 		    {items:[{name:"#F.currentOnHandQty" , type:"TEXT"	 ,bind:"currentOnHandQty"}]},
	 		    {items:[{name:"#L.category03" , type:"LABEL" , 	value:"小類"}]},
				 {items:[{name:"#F.category03" , type:"SELECT", 	init:allCategory03,bind:"category03"}]}
	 		    ]},
	 	{row_style:"", cols:[
				{items:[{name:"#L.unitPirce" , type:"LABEL"  , value:"價格"}]},
	 		    {items:[{name:"#F.unitPirce" , type:"TEXT"	,bind:"unitPirce"}]},
	 		     {items:[{name:"#L.category13" , type:"LABEL" , 	value:"系列"}]},
				 {items:[{name:"#F.category13" , type:"SELECT" , 	init:allCategory13 , bind:"category13"}]}
				 ]},
		{row_style:"", cols:[
				{items:[{name:"#L.searchWay" , type:"LABEL"  , value:"相關商品查詢"}]},
				{items:[{name:"#F.searchWay" , type:"SELECT"  , bind:"searchWay", init:searchWays}], td:"colSpan=3"}
				//{items:[{name:"#F.searchWay" , type:"SELECT"  , bind:"searchWay", init:searchWays, eChange:"showCategory()"}], td:"colSpan=3"}
				/*{items:[{name:"#F.searchWay" , type:"SELECT"  , bind:"searchWay", init:searchWays, eChange:"showCategory()"},
						{name:"#L.category01Advance" , type:"LABEL"  , value:"大類", mode:"HIDDEN"},
						{name:"#F.category01Advance" , type:"SELECT"  , bind:"category01Advance", init:allCategory01, mode:"HIDDEN"},
						{name:"#L.category02Advance" , type:"LABEL"  , value:"中類", mode:"HIDDEN"},
						{name:"#F.category02Advance" , type:"SELECT"  , bind:"category02Advance", init:allCategory02, mode:"HIDDEN"},
						{name:"#L.category03Advance" , type:"LABEL"  , value:"小類", mode:"HIDDEN"},
						{name:"#F.category03Advance" , type:"SELECT"  , bind:"category03Advance", init:allCategory03, mode:"HIDDEN"}], td:"colSpan=3"}*/
				 ]}
		],
		 
		beginService:"",
		closeService:""					
	});
	vat.item.setAttributeByName("#F.itemImage", "height", 180);
}
function kweButtonLine(){

    vat.block.create(vnB_Button, {
	id: "vnB_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif", eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:"",
		closeService:""
	});

}
// 離開按鈕按下
function closeWindows(closeType){
   	window.close();
}
function kweImDetail1(){

	var shiftMode = "shift";

	var vbCanGridDelete = false;
	var vbCanGridAppend = false;
	var vbCanGridModify = true;
	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
	var vbSelectionType = vatPickerId== null || vatPickerId== "" ?"NONE":"CHECK";
	var vnB_Detail = 2;
    if(vatPickerId!= null && vatPickerId!= ""){
   		vat.item.make(vnB_Detail, "checkbox"            , {type:"XBOX" });
   	}
    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"  , size: 1, view:"fixed", desc:"序號"      });
	vat.item.make(vnB_Detail, "brandCode"           , {type:"TEXT" , size: 0, maxLen: 0, mode:"HIDDEN"  , desc:"品牌"});
	vat.item.make(vnB_Detail, "itemCode"            , {type:"TEXT" , view:"fixed", size:20, maxLen:20, mode:"READONLY", desc:"品號"});
	vat.item.make(vnB_Detail, "itemCName"           , {type:"TEXT" , view:"fixed", size:25, maxLen:22, mode:"READONLY", desc:"中文名稱"});
	vat.item.make(vnB_Detail, "warehouseCode"       , {type:"TEXT" ,  size: 6, maxLen: 6, mode:"READONLY", desc:"庫別"});
	vat.item.make(vnB_Detail, "warehouseName"       , {type:"TEXT" , view:"", size: 8, maxLen:8, mode:"READONLY", desc:"庫別名稱"});
	vat.item.make(vnB_Detail, "lotNo"               , {type:"HIDDEN" , view:"", size:10, maxLen:10, mode:"READONLY", desc:"批號"});
	vat.item.make(vnB_Detail, "itemBrand"           , {type:"TEXT" , view:"", size: 3, maxLen: 3, mode:"HIDDEN", desc:"品牌"});
	vat.item.make(vnB_Detail, "itemBrandName"       , {type:"TEXT" , view:"", size: 8, maxLen:12, mode:"HIDDEN", desc:"品牌名稱"});
	vat.item.make(vnB_Detail, "unitPrice"     	    , {type:"NUMB" , view:"", size: 6, maxLen: 6, mode:"READONLY", desc:"售價"});
	vat.item.make(vnB_Detail, "stockOnHandQty"      , {type:"HIDDEN" , view:"", size: 1, maxLen:1, mode:"READONLY", desc:"昨日結餘"});
	vat.item.make(vnB_Detail, "outUncommitQty"      , {type:"HIDDEN" , view:"", size: 1, maxLen:1, mode:"READONLY", desc:"銷貨未結"});
	vat.item.make(vnB_Detail, "inUncommitQty"       , {type:"HIDDEN" , view:shiftMode, size: 1, maxLen:1, mode:"READONLY", desc:"採購未結"});
	vat.item.make(vnB_Detail, "moveUncommitQty"     , {type:"HIDDEN" , view:shiftMode, size: 1, maxLen:1, mode:"READONLY", desc:"調撥未結"});
	vat.item.make(vnB_Detail, "otherUncommitQty"    , {type:"HIDDEN" , view:shiftMode, size: 1, maxLen:1, mode:"READONLY", desc:"調整未結"});
	vat.item.make(vnB_Detail, "currentOnHandQty"    , {type:"NUMB" , size: 7, maxLen: 7, mode:"READONLY", desc:"庫存量"});
	vat.item.make(vnB_Detail, "category01"       	, {type:"TEXT" , view:shiftMode, size: 4, maxLen: 4, mode:"READONLY", desc:"大類"});
	vat.item.make(vnB_Detail, "category01Name"      , {type:"TEXT" , view:shiftMode, size: 8, maxLen:12, mode:"READONLY", desc:"大類名稱"});
	vat.item.make(vnB_Detail, "category02"       	, {type:"TEXT" , view:shiftMode, size: 4, maxLen: 4, mode:"READONLY", desc:"中類"});
	vat.item.make(vnB_Detail, "category021Name"     , {type:"TEXT" , view:shiftMode, size: 8, maxLen:12, mode:"READONLY", desc:"中類名稱"});
	vat.item.make(vnB_Detail, "category03"       	, {type:"TEXT" , view:shiftMode, size: 4, maxLen: 4, mode:"READONLY", desc:"小類"});
	vat.item.make(vnB_Detail, "category03Name"      , {type:"TEXT" , view:shiftMode, size:12, maxLen:12, mode:"READONLY", desc:"小類名稱"});
	vat.item.make(vnB_Detail, "category17"       	, {type:"TEXT" , view:shiftMode, size: 6, maxLen: 6, mode:"READONLY", desc:"廠商代號"});
	vat.item.make(vnB_Detail, "supplierName"        , {type:"TEXT" , view:shiftMode, size:12, maxLen:12, mode:"READONLY", desc:"廠商名稱"});
	vat.item.make(vnB_Detail, "supplierItemCode"    , {type:"TEXT" , view:shiftMode, size:12, maxLen:12, mode:"READONLY", desc:"原廠貨號"});
	vat.item.make(vnB_Detail, "itemEName"    	    , {type:"TEXT" , view:shiftMode, size:12, maxLen:12, mode:"READONLY", desc:"其他品名"});
	vat.item.make(vnB_Detail, "boxCapacity"    	    , {type:"NUMB" , view:shiftMode, size: 4, maxLen: 4, mode:"READONLY", desc:"每箱數量"});
	vat.item.make(vnB_Detail, "category07"       	, {type:"TEXT" , view:shiftMode, size: 4, maxLen: 4, mode:"READONLY", desc:"性別"});
	vat.item.make(vnB_Detail, "category09"       	, {type:"TEXT" , view:shiftMode, size: 4, maxLen: 4, mode:"READONLY", desc:"款式"});
	vat.item.make(vnB_Detail, "category13"       	, {type:"TEXT" , view:shiftMode, size: 4, maxLen: 4, mode:"READONLY", desc:"系列"});
	vat.item.make(vnB_Detail, "headId"              , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["id.brandCode","id.itemCode","id.warehouseCode","id.lotNo"],
														//pickAllService		: function (){return ""},
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,
														loadBeforeAjxService: "loadBeforeAjxService()",
														//loadSuccessAfter    : "pageLoadSuccess()",
														saveBeforeAjxService: "saveBeforeAjxService()"//,
														//saveSuccessAfter    : "saveSuccessAfter()"
														});
}

function loadBeforeAjxService(){
	/*
    //alert("loadBeforeAjxService");
    $.blockUI({
            message: $('#domMessage'),
            overlayCSS: { // 遮罩的css設定
                backgroundColor: '#eee'
            },
            css: { // 遮罩訊息的css設定
                border : '3px solid #aaa',
                width: '30%',
                left : '35%',
                backgroundColor : 'white',
                opacity : '0.9' //透明度，值在0~1之間
            }
	});
alert();*/
	var processString = "process_object_name=soDepartmentOrderService&process_object_method_name=getAJAXSearchPageData" +
                      "&loginBrandCode"   	+ "=" + vat.item.getValueByName("#F.brandCode"    		  ) +
                      "&employeeCode="     + document.forms[0]["#loginEmployeeCode"].value              +
                      "&itemCode"  			+ "=" + vat.item.getValueByName("#F.itemCode"    		  ) +
                      "&itemCName"  		+ "=" + vat.item.getValueByName("#F.itemCName"    		  ) +
					  "&warehouseCode"      + "=" + vat.item.getValueByName("#F.warehouseCode"        ) +
	                  "&category01"         + "=" + vat.item.getValueByName("#F.category01"           ) +
	                  "&category02"         + "=" + vat.item.getValueByName("#F.category02"           ) +
	                  "&category03"         + "=" + vat.item.getValueByName("#F.category03"           ) +
	                  "&category01Advance"  + "=" + vat.item.getValueByName("#F.category01Advance"           ) +
	                  "&category02Advance"  + "=" + vat.item.getValueByName("#F.category02Advance"           ) +
	                  "&category03Advance"  + "=" + vat.item.getValueByName("#F.category03Advance"           ) +
	                  "&category13"         + "=" + vat.item.getValueByName("#F.category13"           ) +
	                  "&searchWay"          + "=" + vat.item.getValueByName("#F.searchWay"            ) +
	                  "&superintendentCode" + "=" + vat.bean().vatBeanOther.superintendentCode +
	                  "&isShowOwn"          + "=" + vat.item.getValueByName("#F.isShowOwn"            );
	return processString;
}

function changeItemCode(){

	var brandCode = document.forms[0]["#loginBrandCode"    ].value;

	vat.item.setValueByName("#F.searchWay", "other");
		var processString = "process_object_name=soDepartmentOrderService&process_object_method_name=getAJAXFormDataByItemCode"
							+"&brandCode="  + vat.item.getValueByName("#F.brandCode")
							+"&employeeCode="  + document.forms[0]["#loginEmployeeCode"].value
							+"&itemCode="  + vat.item.getValueByName("#F.itemCode");
		vat.ajax.startRequest(processString,  function(){ 
				/*pro.setProperty("itemCode",AjaxUtils.getPropertiesValue((String)item[0],  ""));
					pro.setProperty("warehouseCode",AjaxUtils.getPropertiesValue((String)item[1],  ""));
					pro.setProperty("category01",AjaxUtils.getPropertiesValue((String)item[2],  ""));
					pro.setProperty("category02",AjaxUtils.getPropertiesValue((String)item[3],  ""));
					pro.setProperty("category03",AjaxUtils.getPropertiesValue((String)item[4],  ""));
					pro.setProperty("unitPirce",AjaxUtils.getPropertiesValue((String)item[5],  ""));
					pro.setProperty("currentOnHandQty",AjaxUtils.getPropertiesValue((String)item[6],  ""));
					pro.setProperty("itemCName",AjaxUtils.getPropertiesValue((String)item[7],  ""));*/
					vat.item.setValueByName("#F.itemCode", vat.item.getValueByName("#F.itemCode"));
					//vat.item.setValueByName("#F.itemBrand",vat.ajax.getValue("itemCode", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.warehouseCode", vat.ajax.getValue("warehouseCode", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.category01", vat.ajax.getValue("category01", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.category02", vat.ajax.getValue("category02", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.category03", vat.ajax.getValue("category03", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.category13", vat.ajax.getValue("category13", vat.ajax.xmlHttp.responseText));
					//vat.item.setValueByName("#F.unitPirce", vat.ajax.getValue("unitPirce", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.currentOnHandQty", vat.ajax.getValue("currentOnHandQty", vat.ajax.xmlHttp.responseText));
					vat.item.setValueByName("#F.itemCName", vat.ajax.getValue("itemCName", vat.ajax.xmlHttp.responseText));
					vat.item.setAttributeByName("#F.itemImage", "src", "./images/departmentPos/itemImage/"+vat.ajax.getValue("imageFileName", vat.ajax.xmlHttp.responseText)+".gif");
					//vat.item.setAttributeByName("#F.itemImage", "src", "./images/departmentPos/itemImage/BSCTDBKT9534.gif");
					//vat.item.setAttributeByName("#F.itemImage", "height", 180);
					//vat.item.setAttributeByName("#F.itemImage", "style", "float:center");
					vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
		});

}
function saveBeforeAjxService() {

	var processString = "";

	processString = "process_object_name=soDepartmentOrderService&process_object_method_name=saveSearchResult";


	return processString;
}
function doSearch() {

    //alert("searchService");
    //vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
    //alert("timeScope:"+vat.bean().timeScope);
    vat.block.submit(function () {
        return "process_object_name=tmpAjaxSearchDataService" + "&process_object_method_name=deleteByTimeScope&timeScope=" + vat.bean().vatBeanOther.timeScope
    }, {
        other: true,
        funcSuccess: function () {
            vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
            focusPosBar();
        }
    });

}
function resetForm(){
	document.forms[0].reset();
	vat.item.setAttributeByName("#F.itemImage", "src", "./images/departmentPos/itemImage/"+vat.bean().vatBeanOther.brandCode+".GIF");
}

function refreshDetail(){
	vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);
}
/*
function showCategory(){
	//alert("sdfsdf");
	//vat.item.setAttributeByName("#F.category01Advance", "display", "inline");
	//vat.item.setAttributeByName("#F.category02Advance", "display", "inline");
	//vat.item.setAttributeByName("#F.category03Advance", "display", "inline");
	var category01AdvanceLabel = document.getElementById("#L.category01Advance");
	var category01Advance = document.getElementById("#F.category01Advance");
	var category02Advance = document.getElementById("#F.category02Advance");
	var category03Advance = document.getElementById("#F.category03Advance");
	var vSearchWay = vat.item.getValueByName("#F.searchWay");
	if(vSearchWay != "other"){
		category01Advance.style.display = "inline";
		category02Advance.style.display = "inline";
		category03Advance.style.display = "inline";			
	}else{
		category01AdvanceLabel.style.display = "none";
		//category01Advance.style.display = "none";
		//category02Advance.style.display = "none";
		//category03Advance.style.display = "none";			
	}

}*/



