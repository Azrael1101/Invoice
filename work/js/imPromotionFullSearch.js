vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Header = 1;
var vnB_Detail = 2;

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
  	     vatPickerId        : document.forms[0]["#vatPickerId"          ].value,
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"].value
	    };
	    
	    vat.bean.init(	
	  		function(){
				return "process_object_name=imPromotionFullAction&process_object_method_name=performSearchInitial";
				  	},{								
	    		other: true
    	}); 
   		
  }
}

//可搜尋的欄位
//先以手動方式輸入資料
function headerInitial(){

var allOrderTypes = vat.bean("allOrderTypes");
var allStatus = [["","","true"],
                 ["暫存","作廢","簽核中","簽核完成","結案"],
                 ["SAVE","VOID","SIGNING","FINISH","CLOSE"]];

var startDate = getStartDate();
 
vat.block.create( 
		vnB_Header, 
		{
			id: "vatBlock_Head", 
			generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"滿額贈查詢作業", rows:[  
	 			{row_style:"", cols:[
	 	{items:[{name:"#L.orderTypeCode",      type:"LABEL",  value:"單別"}]},	 
	 	{items:[{name:"#F.orderTypeCode",      type:"SELECT", bind:"orderTypeCode",  size:1, mode:"READONLY", init:allOrderTypes}]},
	 	 	{items:[{name:"#L.orderNo",       type:"LABEL", value:"促銷單號"}]},
	 	{items:[{name:"#F.startOrderNo",  type:"TEXT",  bind:"startOrderNo",    size:20},
				{name:"#L.between",       type:"LABEL", value:" 至 "},
	 		 	{name:"#F.endOrderNo",    type:"TEXT",  bind:"endOrderNo",      size:20}]},		 
	    {items:[{name:"#L.status",             type:"LABEL",  value:"狀態"}]},	 		 
	 	{items:[{name:"#F.status",             type:"SELECT", bind:"status",    size:12, init:allStatus}]}]},

	 {row_style:"", cols:[
	 	{items:[{name:"#L.superintendentCode", type:"LABEL",  value:"負責人員"}]},
	 	{items:[{name:"#F.superintendentCode", type:"TEXT",   bind:"superintendentCode", size:12, mask:"CCCCCC", onchange:"onChangeSuperintendent()" },
	 		 	{name:"#F.superintendentName", type:"TEXT",   bind:"superintendentName", size:12, mode:"READONLY" }]},
	    {items:[{name:"#L.Date",          type:"LABEL",  value:"活動日期"}]},
	 	{items:[{name:"#F."+startDate,     type:"DATE",   bind:startDate,    size:1},
	         	{name:"#L.between",       type:"LABEL",  value:" 至 "},
	         	{name:"#F.endDate",       type:"DATE",   bind:"endDate",      size:1}],td:" colSpan=3"}]}

	 ],  
			beginService:"",
			closeService:""			
		}
	);
	
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
	 			{name:"#B.picker",      type:"IMG",      value:"檢視", src:"./images/button_view.gif",  eClick:"doClosePicker()"}],
	 			
	 			 td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	doFormAccessControl();
}



function detailInitial(){
	var allOrderTypes = vat.bean("allOrderTypes");
    var vCanGridDelete = false;
    var vCanGridAppend = false;
    var vCanGridModify = true;
    var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
  	var vbSelectionType = "CHECK";
    if(vatPickerId != null && vatPickerId != ""){
    	vat.item.make(vnB_Detail, "checkbox", {type:"XBOX"});
    	vbSelectionType = "CHECK";    
    }else{
    	vbSelectionType = "NONE";
    }
    
    vat.item.make(vnB_Detail, "indexNo",            {type:"IDX", view:"fixed",    desc:"序號" });
	vat.item.make(vnB_Detail, "orderTypeCode",      {type:"TEXT", view:"fixed",   size:3, mode:"READONLY", desc:"單別"});
	vat.item.make(vnB_Detail, "orderNo",            {type:"TEXT", view:"",   size:12, mode:"READONLY", desc:"促銷單號"});
	vat.item.make(vnB_Detail, "promotionCode",      {type:"TEXT", view:"",   size:12, mode:"READONLY", desc:"活動代號"});
	vat.item.make(vnB_Detail, "description",        {type:"TEXT", view:"",   size:20, mode:"READONLY", desc:"活動說明"});
	vat.item.make(vnB_Detail, "inChargeName",       {type:"TEXT", view:"",   size:20, mode:"READONLY", desc:"負責人員"});
	vat.item.make(vnB_Detail, "beginDate",          {type:"TEXT", view:"",   size:10, mode:"READONLY", desc:"開始日期"});
	vat.item.make(vnB_Detail, "endDate",            {type:"TEXT", view:"",   size:10, mode:"READONLY", desc:"結束日期"});
	
	vat.item.make(vnB_Detail, "statusName",         {type:"TEXT", view:"",   size:10, mode:"READONLY", desc:"狀態"});
	
		vat.item.make(vnB_Detail, "headId",             {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
									  id                  : "vatDetailDiv",
									  pageSize            : 10,
									  searchKey           : ["headId"],
									  //pickAllService	  : "selectAll",
									  selectionType       : vbSelectionType,
									  indexType           : "AUTO",
								      canGridDelete       : vCanGridDelete,
									  canGridAppend       : vCanGridAppend,
									  canGridModify       : vCanGridModify,	
									  loadBeforeAjxService: "loadBeforeAjxService()",
									  loadSuccessAfter    : "loadSuccessAfter()", 	
									  saveBeforeAjxService: "saveBeforeAjxService()",
									  saveSuccessAfter    : "saveSuccessAfter()",
									  blockId             : "2",
									  indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
									 });
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function loadBeforeAjxService(){
	var startDate = getStartDate();
	var processString = "process_object_name=imPromotionFullService&process_object_method_name=getAJAXSearchPageData" + 
                        "&brandCode="          + document.forms[0]["#loginBrandCode"].value +     
	                    "&orderTypeCode="      + vat.item.getValueByName("#F.orderTypeCode") +
	                    "&inCharge="           + vat.item.getValueByName("#F.superintendentCode") +
	                    "&status="             + vat.item.getValueByName("#F.status") + 
	                    "&startOrderNo="       + vat.item.getValueByName("#F.startOrderNo") +     
	                    "&endOrderNo="         + vat.item.getValueByName("#F.endOrderNo") +         
					    "&startDate="          + vat.item.getValueByName("#F."+startDate) +
					    "&endDate="            + vat.item.getValueByName("#F.endDate") ;
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
	processString = "process_object_name=imPromotionFullService&process_object_method_name=saveSearchResult";
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {

}	

function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);}
			                    });
}

function doClosePicker(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=imPromotionFullAction&process_object_method_name=performSearchSelection";
    		}, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
	}}); 
}

function doView(){
	vat.block.pageSearch(2, {funcSuccess : function(){
		vat.block.submit(
			function(){ return "process_object_name=imPromotionFullAction&process_object_method_name=performSearchSelection";
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

function resetForm(){
	var startDate = getStartDate();
    vat.item.setValueByName("#F.superintendentCode", "");
    vat.item.setValueByName("#F.superintendentName", "");  
    vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.itemCategory", "");
    vat.item.setValueByName("#F.startOrderNo", "");
    vat.item.setValueByName("#F.endOrderNo", "");  
    vat.item.setValueByName("#F."+startDate, "");
    vat.item.setValueByName("#F.endDate", "");
    vat.item.setValueByName("#F.itemCode", "");
}

/* */
function onChangeSuperintendent() {

	var vSuperintendentCode = vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, '').toUpperCase();
    vat.item.setValueByName("#F.superintendentCode", vSuperintendentCode);
    if(vSuperintendentCode !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + document.forms[0]["#loginBrandCode"].value + 
                    "&employeeCode=" + vSuperintendentCode,
           find: function changeSuperintendentRequestSuccess(oXHR){              
               //vat.item.setValueByName("#F.inCharge", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
         vat.item.setValueByName("#F.superintendentName", "");
    }
    
    function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
    if(null != vFormId && "" != vFormId && 0 != vFormId){
    	var url = "/erp/Im_PromotionFull:create:20190422.page?formId=" + vFormId;	
		sc=window.open(url, '滿額贈維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
		sc.resizeTo((screen.availWidth),(screen.availHeight));
		sc.moveTo(0,0);
	}
}
}

function doFormAccessControl(){
    if("" == vat.bean().vatBeanOther.vatPickerId) {
		vat.item.setStyleByName("#B.picker" , "display", "none");
	}else{
	    vat.item.setStyleByName("#B.picker" , "display", "inline");
	}
}

// 取得起始日期
function getStartDate(){
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	var startDate = "startDate";
	if("T2" == brandCode ){
		startDate = "beginDate";
	}
	return startDate;
}

//轉大寫
function upperCase(id){
	var str = vat.item.getValueByName(id);
	if(str != null && str.length > 0){
		vat.item.setValueByName(id, str.toUpperCase());
	}
}

