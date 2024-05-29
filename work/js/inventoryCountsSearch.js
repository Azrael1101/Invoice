vat.debug.disable();
var afterSavePageProcess = "";

function outlineBlock(){
    formDataInitial();
    headerInitial();
    buttonLine();
    detailInitial();
}

function formDataInitial(){ 
  if(typeof document.forms[0]["#brandCode"] != 'undefined'){
  	 vat.bean().vatBeanOther = 
  	    {brandCode     : document.forms[0]["#brandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"].value,
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value,
  	     canBeMod			: document.forms[0]["#canBeMod"].value
	    }; 
	    
     vat.bean.init(function(){
		return "process_object_name=imInventoryCountsAction&process_object_method_name=performSearchInitial"; 
     },{other: true});
  }
}

function headerInitial(){ 
var allOrderTypes = [["","","true"],
                 ["F盤點單","P盤點單"],
                 ["ICF","ICP"]];
var allCountsType = vat.bean("allCountsType");
var allStatus = [["","","true"],
                 ["暫存","作廢","盤點中","簽核完成"],
                 ["SAVE","VOID","COUNTING","FINISH"]];
                 
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"盤點單查詢作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別"}]},	 
	 {items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", size:1, init:allOrderTypes}]},		 
	 {items:[{name:"#L.orderNo", type:"LABEL", value:"單號"}]},
	 {items:[{name:"#F.startOrderNo", type:"TEXT",  bind:"startOrderNo", size:20},
			 {name:"#L.between", type:"LABEL", value:" 至 "},
	 		 {name:"#F.endOrderNo", type:"TEXT",  bind:"endOrderNo"  , size:20}]},
	 {items:[{name:"#L.status", type:"LABEL", value:"狀態"}]},	 		 
	 {items:[{name:"#F.status", type:"SELECT",  bind:"status", size:12, init:allStatus}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.warehouseCode", type:"LABEL" , value:"庫別"}]},	 
	 {items:[{name:"#F.warehouseCode", type:"TEXT", bind:"warehouseCode", size:15, maxLen:15, eChange:'changeWarehouseCode()'},
	         {name:"#F.warehouseName", type:"TEXT", bind:"warehouseName", back:false, mode:"READONLY"}]},	 		 
	 {items:[{name:"#L.countsDate", type:"LABEL" , value:"盤點日"}]},
	 {items:[{name:"#F.startCountsDate", type:"DATE", bind:"startCountsDate", size:1},
	         {name:"#L.between", type:"LABEL", value:" 至 "},
	         {name:"#F.endCountsDate", type:"DATE",  bind:"endCountsDate", size:1}]},	         
	 {items:[{name:"#L.countsId", type:"LABEL" , value:"盤點代號"}]},
	 {items:[{name:"#F.countsId", type:"TEXT", bind:"countsId", size:15, maxLen:15}]}]},	         
	 {row_style:"", cols:[
	 {items:[{name:"#L.countsType", type:"LABEL" , value:"盤點方式"}]},
	 {items:[{name:"#F.countsType", type:"SELECT", bind:"countsType", init:allCountsType}]},
	 {items:[{name:"#L.countsLotNo", type:"LABEL" , value:"盤點批號"}]},
	 {items:[{name:"#F.startCountsLotNo", type:"TEXT",  bind:"startCountsLotNo", size:15},
			 {name:"#L.between", type:"LABEL", value:" 至 "},
	 		 {name:"#F.endCountsLotNo", type:"TEXT",  bind:"endCountsLotNo"  , size:15}], td:" colSpan=3"}]}
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
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},	 		
	 			{name:"#F.selectedAll" , type:"CHECKBOX" , value:"N"},
	 			{name:"#L.selectedAll" , type:"LABEL"    , value:"選擇全部"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},	 		
	 			{name:"#F.clearAll" , type:"CHECKBOX"    , value:"N"},
	 			{name:"#L.clearAll" , type:"LABEL"       , value:"清除全部"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){

  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
  var vnB_Detail = 2;
  
  var vatPickerId = vat.bean().vatBeanOther.vatPickerId;
  var vbSelectionType = vatPickerId== null || vatPickerId== "" ?"NONE":"CHECK";
  
  	if(vatPickerId!= null && vatPickerId!= ""){
   		vat.item.make(vnB_Detail, "checkbox"            , {type:"XBOX" });
   	}
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "orderTypeCode"             , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"單別"});
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"單號"});
	vat.item.make(vnB_Detail, "countsDate"                , {type:"TEXT" , size: 8, maxLen:20, mode:"READONLY", desc:"盤點日期"});
	vat.item.make(vnB_Detail, "actualCountsDate"          , {type:"TEXT" , size: 8, maxLen:20, mode:"READONLY", desc:"實際盤點日期"});
	vat.item.make(vnB_Detail, "lastImportDate"            , {type:"TEXT" , size: 8, maxLen:20, mode:"READONLY", desc:"匯入日期"});
	vat.item.make(vnB_Detail, "countsId"                  , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"盤點代號"});
	vat.item.make(vnB_Detail, "countsLotNo"               , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"盤點批號"});
	vat.item.make(vnB_Detail, "countsTypeName"            , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"盤點方式"});
	vat.item.make(vnB_Detail, "warehouseCode"             , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"庫別"});
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"狀態"});
	vat.item.make(vnB_Detail, "headId"                    , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
									  id                  : "vatDetailDiv",
									  pageSize            : 10,
									  searchKey           : ["headId"],
									  pickAllService		: "selectAll",
									  selectionType       : vbSelectionType,
									  indexType           : "AUTO",
								      canGridDelete       : vbCanGridDelete,
									  canGridAppend       : vbCanGridAppend,
									  canGridModify       : vbCanGridModify,	
									  loadBeforeAjxService: "loadBeforeAjxService()",
									  saveBeforeAjxService: "saveBeforeAjxService()",
									  saveSuccessAfter    : "",
									  indicate            : 
										function(){
											if(vatPickerId != null && vatPickerId != ""){
												return false}else{ openModifyPage()}
										}
									});
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function loadBeforeAjxService(){
	var processString = "process_object_name=imInventoryCountsService&process_object_method_name=getAJAXSearchPageData" + 
                      "&brandCode=" + document.forms[0]["#brandCode"].value +     
	                  "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") +     
	                  "&startOrderNo=" + vat.item.getValueByName("#F.startOrderNo").replace(/^\s+|\s+$/, '') +     
	                  "&endOrderNo=" + vat.item.getValueByName("#F.endOrderNo").replace(/^\s+|\s+$/, '') +                       
	                  "&status=" + vat.item.getValueByName("#F.status") +     
	                  "&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode").replace(/^\s+|\s+$/, '').toUpperCase() +                       
					  "&startCountsDate=" + vat.item.getValueByName("#F.startCountsDate") +
					  "&endCountsDate=" + vat.item.getValueByName("#F.endCountsDate") +
					  "&countsId=" + vat.item.getValueByName("#F.countsId").replace(/^\s+|\s+$/, '') +
					  "&countsType=" + vat.item.getValueByName("#F.countsType") +
					  "&startCountsLotNo=" + vat.item.getValueByName("#F.startCountsLotNo").replace(/^\s+|\s+$/, '') +
					  "&endCountsLotNo=" + vat.item.getValueByName("#F.endCountsLotNo").replace(/^\s+|\s+$/, '');
					  
	return processString;											
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	return true;
}

/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=imInventoryCountsService&process_object_method_name=saveSearchResult";
	}
	
	return processString;
}								

/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(2);
}

function doSearch(){
   
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);}
			                    });
}

function doClosePicker(){

	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imInventoryCountsAction&process_object_method_name=performSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

function doView(){

	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imInventoryCountsAction&process_object_method_name=performSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}
function selectAll(){
  //vatIsAllClick
  processString = "process_object_name=imInventoryCountsService&process_object_method_name=updateAllSearchData";
  return processString;
  
}

function resetForm(){
    vat.item.setValueByName("#F.startOrderNo", "");
    vat.item.setValueByName("#F.endOrderNo", "");
    vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.warehouseCode", ""); 
    vat.item.setValueByName("#F.startCountsDate", "");
    vat.item.setValueByName("#F.endCountsDate", "");
    
    vat.item.setValueByName("#F.countsId", "");
    vat.item.setValueByName("#F.countsType", "");
    vat.item.setValueByName("#F.startCountsLotNo", "");
    vat.item.setValueByName("#F.endCountsLotNo", "");
}

function changeWarehouseCode(){
    var warehouseCode = vat.item.getValueByName("#F.warehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();    
    vat.item.setValueByName("#F.warehouseCode", warehouseCode);   
    if(warehouseCode !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=imWarehouseService"+
                    "&process_object_method_name=findByBrandCodeAndWarehouseCode"+
                    "&brandCode=" + document.forms[0]["#brandCode"].value + 
                    "&warehouseCode=" + warehouseCode,
           find: function changeWarehouseRequestSuccess(oXHR){
               vat.item.setValueByName("#F.warehouseCode", vat.ajax.getValue("WarehouseCode", oXHR.responseText));
               vat.item.setValueByName("#F.warehouseName", vat.ajax.getValue("WarehouseName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setValueByName("#F.warehouseName", "");
    }
}

function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    var canBeMod = vat.bean().vatBeanOther.canBeMod;
    
    if(!(vFormId == "" || vFormId == "0")){
	    var url = "/erp/Im_InventoryCounts:create:20090818.page?formId=" + vFormId + "&canBeMod=" + canBeMod; 
	     sc=window.open(url, '盤點單', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	     sc.resizeTo((screen.availWidth),(screen.availHeight));
	     sc.moveTo(0,0);
	}
}