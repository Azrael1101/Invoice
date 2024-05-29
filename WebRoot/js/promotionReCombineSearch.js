vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){
    formDataInitial();
    headerInitial();
    buttonLine();
    detailInitial();
}

function formDataInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	 vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     vatPickerId : document.forms[0]["#vatPickerId"].value
	    }; 

     vat.bean.init(function(){
		return "process_object_name=imPromotionReCombineMainAction&process_object_method_name=performCombineSearchInitial"; 
     },{other: true});

  }
}

function headerInitial(){ 

var allEnable =        [["", true, false], ["全部", "啟用", "停用"], ["","Y", "N"]];
var brandCode = vat.bean().vatBeanOther.loginBrandCode; 

vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"組合促銷活動查詢作業", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#L.combineCode",  type:"LABEL",  value:"組合代號"}]},	 
	 	{items:[{name:"#F.combineCode",  type:"TEXT", bind:"combineCode", size:20, onchange:"upperCase('#F.combineCode')"}],td:""},	 	
	 	{items:[{name:"#L.enableDate",   type:"LABEL",  value:"起/停用日期"}]},
	 	{items:[{name:"#F.enableDate",   type:"DATE",   bind:"enableDate",    size:1}]},
	 	{items:[{name:"#L.enable",       type:"LABEL",  value:"狀態"}]},	 		 
	 	{items:[{name:"#F.enable",       type:"SELECT", bind:"enable",    size:12, init:allEnable, onchange:"changeEnable()"}]}]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.itemBrand",    type:"LABEL",  value:"商品品牌"}]},	 
	 	{items:[{name:"#F.itemBrand",    type:"TEXT", bind:"itemBrand", size:20, onchange:"upperCase('#F.itemBrand')"}],td:""},
	 	{items:[{name:"#L.category02",   type:"LABEL",  value:"商品中類"}]},	 
	 	{items:[{name:"#F.category02",   type:"TEXT", bind:"category02", size:20, onchange:"upperCase('#F.category02')"}],td:""},
	 	{items:[{name:"#L.foreignCategory",       type:"LABEL",  value:"國外類別"}]},	 		 
	 	{items:[{name:"#F.foreignCategory",       type:"TEXT", bind:"foreignCategory",    size:20}]}]}
	 	], 
	 
		beginService:"",
		closeService:""			
	});
}

function buttonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search",      type:"IMG",      value:"查詢", src:"./images/button_find.gif",  eClick:"doSearch()"},
	 			{name:"SPACE",          type:"LABEL",    value:"　"},
	 			{name:"#B.clear",       type:"IMG",      value:"清除", src:"./images/button_reset.gif", eClick:"resetForm()"},
	 			{name:"SPACE",          type:"LABEL",    value:"　"},
	 	 		{name:"#B.exit",        type:"IMG",      value:"離開", src:"./images/button_exit.gif",  eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE",          type:"LABEL",    value:"　"},
	 			//{name:"#B.update",      type:"PICKER", value:"檢視", src:"./images/button_view.gif",  eClick:"doView()"},
	 			//{name:"SPACE",          type:"LABEL",  value:"　"},	 		
	 			{name:"#B.picker",      type:"IMG",      value:"檢視", src:"./images/button_view.gif",  eClick:"doClosePicker()"}],
	 			/*
	 			{name:"SPACE",          type:"LABEL",    value:"　"},	 		
	 			{name:"#F.selectedAll", type:"CHECKBOX", value:"N"},
	 			{name:"#L.selectedAll", type:"LABEL",    value:"選擇全部"},
	 			{name:"SPACE",          type:"LABEL",    value:"　"},	 		
	 			{name:"#F.clearAll",    type:"CHECKBOX", value:"N"},
	 			{name:"#L.clearAll",    type:"LABEL",    value:"清除全部"}]}]}
	 			*/
	 			 td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	doFormAccessControl();
}

function detailInitial(){
	var vCanGridDelete = false;
    var vCanGridAppend = false;
    var vCanGridModify = true;
    var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
  	var vbSelectionType = "CHECK";
    if(vatPickerId !== null && vatPickerId !== ""){
    	vat.item.make(vnB_Detail, "checkbox", {type:"XBOX"});
    	vbSelectionType = "CHECK";    
    }else{
    	vbSelectionType = "NONE";
    }
    
    vat.item.make(vnB_Detail, "indexNo",            {type:"IDX", view:"fixed",    desc:"序號" });
    vat.item.make(vnB_Detail, "hidden",             {type:"hidden", view:"",   size:12, mode:"READONLY", desc:"hidden"});
    vat.item.make(vnB_Detail, "combineCode",        {type:"TEXT", view:"",   size:12, mode:"READONLY", desc:"組合代號"});
	vat.item.make(vnB_Detail, "combineQuantity",    {type:"TEXT", view:"",   size:12, mode:"READONLY", desc:"組合數量"});
	vat.item.make(vnB_Detail, "combinePrice",       {type:"TEXT", view:"",   size:20, mode:"READONLY", desc:"組合價格"});
	vat.item.make(vnB_Detail, "itemBrand",          {type:"TEXT", view:"",   size:20, mode:"READONLY", desc:"商品品牌"});
	vat.item.make(vnB_Detail, "category02",         {type:"TEXT", view:"",   size:10, mode:"READONLY", desc:"商品中類"});
	vat.item.make(vnB_Detail, "foreignCategory",    {type:"TEXT", view:"",   size:20, mode:"READONLY", desc:"國外類別"});
	vat.item.make(vnB_Detail, "unitPrice",          {type:"TEXT", view:"",   size:10, mode:"READONLY", desc:"商品價格"});
	vat.item.make(vnB_Detail, "enableDate",         {type:"TEXT", view:"",   size:10, mode:"READONLY", desc:"啟用日期"});
	vat.item.make(vnB_Detail, "endDate",            {type:"TEXT", view:"",   size:10, mode:"READONLY", desc:"停用日期"});
	vat.item.make(vnB_Detail, "enable",             {type:"TEXT", view:"",   size:10, mode:"READONLY", desc:"狀態"});
	vat.item.make(vnB_Detail, "headId",             {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
									  id                  : "vatDetailDiv",
									  pageSize            : 10,
									  searchKey           : ["headId"],
									  selectionType       : vbSelectionType,
									  indexType           : "AUTO",
									  canGridDelete       : vCanGridDelete,
									  canGridAppend       : vCanGridAppend,
									  canGridModify       : vCanGridModify,	
									  loadBeforeAjxService: "loadBeforeAjxService()",
									  loadSuccessAfter    : "loadSuccessAfter()", 	
									  saveBeforeAjxService: "saveBeforeAjxService()",
									  saveSuccessAfter    : "saveSuccessAfter()",
									  blockId             : "2"
									  });
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}


function loadBeforeAjxService(){
	var processString = "process_object_name=imPromotionReCombineMainService&process_object_method_name=getAJAXSearchCombinePageData" + 
                        "&brandCode="          + document.forms[0]["#loginBrandCode"].value +     
	                    "&enable="             + vat.item.getValueByName("#F.enable") + 
	                    "&enableDate="         + vat.item.getValueByName("#F.enableDate") +     
	                    "&combineCode="        + vat.item.getValueByName("#F.combineCode") + 
					    "&itemBrand="          + vat.item.getValueByName("#F.itemBrand") + 
					    "&category02="         + vat.item.getValueByName("#F.category02").replace(/^\s+|\s+$/, '') +
					    "&foreignCategory="    + vat.item.getValueByName("#F.foreignCategory");
	return processString;										
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(vnB_Detail).dataCount == vat.block.getGridObject(vnB_Detail).pageSize &&
	    vat.block.getGridObject(vnB_Detail).lastIndex == 1){
		vat.item.setStyleByName("#B.picker" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    doFormAccessControl();
	}
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	processString = "process_object_name=imPromotionReCombineMainService&process_object_method_name=saveSearchCombineResult";
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {

}								

function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope; }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);}
			                    });
}

function doClosePicker(){
	vat.bean().vatBeanOther.headId   = document.forms[0]["#headId"].value;
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=imPromotionReCombineMainAction&process_object_method_name=performSearchCombineSelection";
    		}, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:2} );
	}}); 
}

function doView(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=imPromotionReCombineMainAction&process_object_method_name=performSearchCombineSelection";
			}, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
	}}); 
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
  	window.top.close();
  }  
}

function resetForm(){
	
	vat.item.setValueByName("#F.enable", "");
    vat.item.setValueByName("#F.enableDate", "");  
    vat.item.setValueByName("#F.combineCode", "");
    vat.item.setValueByName("#F.itemBrand", "");
    vat.item.setValueByName("#F.category02", "");
    vat.item.setValueByName("#F.foreignCategory", "");    
}

function doFormAccessControl(){
	var vsEnable = vat.item.getValueByName("#F.enable");
	
    if("" == vat.bean().vatBeanOther.vatPickerId || vsEnable==="Y") {
		vat.item.setStyleByName("#B.picker" , "display", "inline");
	}else{
	    vat.item.setStyleByName("#B.picker" , "display", "none");
	}
}

//轉大寫
function upperCase(id){
	var str = vat.item.getValueByName(id);
	if(str != null && str.length > 0){
		vat.item.setValueByName(id, str.toUpperCase());
	}
}

function changeEnable(){
	vat.block.pageSearch(vnB_Detail);	
}