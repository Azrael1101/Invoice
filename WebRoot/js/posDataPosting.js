vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Header = 1;
var vnB_Detail = 2;
var vnB_Post = 3;

function outlineBlock(){
    formDataInitial();
    headerInitial();
    buttonLine();
    detailInitial();
    postInitial();
}

function formDataInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' &&
      document.forms[0]["#loginBrandCode"].value != '[binding]'){
  	  vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value
	    }; 
	    
      vat.bean.init(function(){
		return "process_object_name=soPostingTallyAction&process_object_method_name=performSearchInitial"; 
      },{other: true});
  }
       vat.item.bindAll();
}

function headerInitial(){
  var brandCode = document.forms[0]["#loginBrandCode"].value; 
  var allShops = vat.bean("allShops");
if(document.forms[0]["#loginBrandCode"].value!=='T2')
{

	var allBatch = [["", "", ""], [ "99"],  [ "99"]];
}else
{
var allBatch = vat.bean("allBatch");
}
  var currentDate = vat.bean("currentDate");
  var salesUnit = "專櫃";
  if("T2" == brandCode){
    salesUnit = "機台";
  }

  var statusOpt = [["", "", false], ["請選擇", "未過帳", "已過帳"], ["", "N", "Y"]];
                 
vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"POS資料過帳作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},	 
	 {items:[{name:"#F.brandCode", type:"TEXT",  mode:"READONLY", init:brandCode}]},
	 {items:[{name:"#L.transactionBeginDate", type:"LABEL" , value:"交易起始日期"}]},
	 {items:[{name:"#F.transactionBeginDate", type:"DATE", bind:"transactionBeginDate", size:1, init:currentDate}, 
	         {name:"#L.until", type:"LABEL" , value:"&nbsp;&nbsp; 至 &nbsp;&nbsp"},
	         {name:"#F.transactionEndDate", type:"DATE", bind:"transactionEndDate", size:1, init:currentDate}]},
	 {items:[{name:"#L.salesUnit", type:"LABEL", value:salesUnit}]},	 
	 {items:[{name:"#F.salesUnit", type:"SELECT",  bind:"salesUnit", size:1, init:allShops}]},
	 {items:[{name:"#L.batch", type:"LABEL", value:"班次"}]},	 
	 {items:[{name:"#F.batch", type:"SELECT",  bind:"batch", size:1,init:allBatch}]}, 	
	 {items:[{name:"#L.status", type:"LABEL" , value:"狀態"}]},
	 {items:[{name:"#F.status", type:"SELECT", bind:"status", size:1, init:statusOpt}]}]}
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
	 			{name:"#B.export"	   , type:"IMG"      ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"matchExport"	   , type:"LABEL"    ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){

  var brandCode = document.forms[0]["#loginBrandCode"].value; 
  var salesUnit = "專櫃代號";
  if("T2" == brandCode){
      salesUnit = "機台代號";
  }  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
  vat.item.make(vnB_Detail, "indexNo"              , {type:"IDX"  , size:10, desc:"序號"      });
  vat.item.make(vnB_Detail, "difference"           , {type:"IMG" , size:15, mode:"READONLY", desc:"差異", src:"images/empty.gif"});
  vat.item.make(vnB_Detail, "postingStatus"        , {type:"TEXT" , size: 6, mode:"READONLY", desc:"狀態"});
  vat.item.make(vnB_Detail, "unit"                 , {type:"TEXT" , size: 6, mode:"READONLY", desc:salesUnit});
  vat.item.make(vnB_Detail, "salesDate"            , {type:"TEXT" , size:10, mode:"READONLY", desc:"交易日期"});
  vat.item.make(vnB_Detail, "schedule"            , {type:"TEXT" , size:10, mode:"READONLY", desc:"班次"});
  vat.item.make(vnB_Detail, "transactionAmountS"   , {type:"NUMB" , size:10, mode:"READONLY", desc:"交易筆數<br>(POS轉入)"});
  vat.item.make(vnB_Detail, "posImportAmtS"        , {type:"NUMB" , size:10, mode:"READONLY", desc:"交易金額<br>(POS轉入)"});
  vat.item.make(vnB_Detail, "transactionAmountD"   , {type:"NUMB" , size:10, mode:"READONLY", desc:"交易筆數<br>(已過帳)"});
  vat.item.make(vnB_Detail, "posImportAmtD"        , {type:"NUMB" , size:10, mode:"READONLY", desc:"交易金額<br>(已過帳)"});
  vat.item.make(vnB_Detail, "actualTransactionAmount", {type:"NUMB" , size:10, mode:"READONLY", desc:"交易筆數<br>(營業輸入)"});
  vat.item.make(vnB_Detail, "actualSalesAmt"       , {type:"NUMB" , size:10, mode:"READONLY", desc:"營收彙總金額<br>(營業輸入)"});
  vat.item.make(vnB_Detail, "differenceAmt"        , {type:"NUMB" , size:10, mode:"READONLY", desc:"差異金額"});
  vat.item.make(vnB_Detail, "accountPage"        , {type:"BUTTON" , size:10, value:"展示結帳條", desc:"結帳條", eClick:'openAccountPage()'});
  vat.block.pageLayout(vnB_Detail, {
									  id                  : "vatDetailDiv",
									  pageSize            : 10,
									  searchKey           : ["salesUnit","transactionDate"],
									  selectionType       : "NONE",
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

function postInitial(){
   
    var brandCode = document.forms[0]["#loginBrandCode"].value;
    var allShops = vat.bean("allShops");
    var currentDate = vat.bean("currentDate"); 
    var allBatch = vat.bean("allBatch");
    var salesUnitLabel = "過帳專櫃";
    if("T2" == brandCode){
        salesUnitLabel = "過帳機台";
    }   
    
    vat.block.create(vnB_Post, {
	id: "vatBlock_Post", table:"cellspacing='10' class='' border='0' cellpadding='2'",
	title:"", rows:[
	 {row_style:"", cols:[
	 {items:[{name:"#L.postingSalesUnitBegin", type:"LABEL", value:salesUnitLabel}]},
	 {items:[{name:"#F.postingSalesUnitBegin", type:"SELECT", bind:"postingSalesUnitBegin", size:1, init:allShops},
	 		 {name:"#L.until", type:"LABEL" , value:"&nbsp;&nbsp; 至 &nbsp;&nbsp"}]},
	 {items:[{name:"#F.postingSalesUnitEnd", type:"SELECT", bind:"postingSalesUnitEnd", size:1, init:allShops}]},	 
	 {items:[{name:"#L.batch1", type:"LABEL" , value:"班次"}]},
	 {items:[{name:"#F.batch1", type:"SELECT", bind:"batch",size:1,mode:"readonly",init:allBatch}]},
	 {items:[{name:"#L.postingTransactionDate", type:"LABEL" , value:"交易日期"}]},
	 {items:[{name:"#F.postingTransactionDate", type:"DATE", bind:"postingTransactionDate", size:1, init:currentDate}]},
	 {items:[{name:"#B.posting"     , type:"IMG"      , value:"過帳",  src:"./images/button_posting.gif", eClick:"execPosting()"},
	 		 {name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 	 {name:"#B.dePosting"   , type:"IMG"      , value:"反過帳", src:"./images/button_unposting.gif"  , eClick:'execAntiPosting()'}, 
	         {name:"SPACE"          , type:"LABEL"    , value:"　"},
	         {name:"#B.message"   , type:"IMG"      , value:"訊息提示", src:"./images/button_message_prompt.gif"  , eClick:'showMessage()'}]}]}], 
	         
	         
	 beginService:"",
	 closeService:""			
	});
	 		
}

// 查詢點下執行
function loadBeforeAjxService(){
	var processString = "process_object_name=soPostingTallyService&process_object_method_name=getAJAXSearchPageData" + 
                       "&brandCode=" + document.forms[0]["#loginBrandCode"].value +
                       "&batch=" + vat.item.getValueByName("#F.batch")+
                       "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
                       "&transactionBeginDate=" + vat.item.getValueByName("#F.transactionBeginDate")+
                       "&transactionEndDate=" + vat.item.getValueByName("#F.transactionEndDate")+                      
                       "&salesUnit=" + vat.item.getValueByName("#F.salesUnit")+
                       "&status=" + vat.item.getValueByName("#F.status");	                   
                                                                            
	return processString;											
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(vnB_Detail).dataCount == vat.block.getGridObject(vnB_Detail).pageSize &&
	    vat.block.getGridObject(vnB_Detail).lastIndex == 1){
		alert("您輸入的條件查無資料！");
	}
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
  var processString = "";
  processString = "process_object_name=soPostingTallyService&process_object_method_name=saveSearchResult";
  return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {

}

// 查詢按下後
function doSearch(){
   vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
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
    formDataInitial();
    vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function execPosting(){
    var brandCode = document.forms[0]["#loginBrandCode"].value; 
    var salesUnitType = "S";
    if("T2" == brandCode){
      salesUnitType = "M";
    }  
	var alertMessage ="是否確定執行過帳作業?";		
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.orderTypeCode = "SOP";
	    vat.bean().vatBeanOther.status = "N";
	    vat.bean().vatBeanOther.postingSalesUnitBegin = vat.item.getValueByName("#F.postingSalesUnitBegin");
	    vat.bean().vatBeanOther.postingSalesUnitEnd = vat.item.getValueByName("#F.postingSalesUnitEnd");
	    vat.bean().vatBeanOther.postingTransactionDate = vat.item.getValueByName("#F.postingTransactionDate");
	    vat.bean().vatBeanOther.salesUnitType = salesUnitType;
	    vat.bean().vatBeanOther.batch =  vat.item.getValueByName("#F.batch");
	    vat.bean().vatBeanOther.timeScope = vat.block.$box[vnB_Post].timeScope;
	      		    
	    vat.block.submit(function(){return "process_object_name=soPostingTallyAction"+
			                	"&process_object_method_name=execPosting";}, {other:true});
	}
}

function execAntiPosting(){
    var brandCode = document.forms[0]["#loginBrandCode"].value; 
    var salesUnitType = "S";
    if("T2" == brandCode){
      salesUnitType = "M";
    }  

	var alertMessage ="是否確定執行反過帳作業?";		
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.orderTypeCode = "SOP";
	    vat.bean().vatBeanOther.status = "Y";
	   // vat.bean().vatBeanOther.postingSalesUnit = vat.item.getValueByName("#F.postingSalesUnit");
	   	vat.bean().vatBeanOther.postingSalesUnitBegin = vat.item.getValueByName("#F.postingSalesUnitBegin");
	    vat.bean().vatBeanOther.postingSalesUnitEnd = vat.item.getValueByName("#F.postingSalesUnitEnd");
	    vat.bean().vatBeanOther.postingTransactionDate = vat.item.getValueByName("#F.postingTransactionDate");
	    vat.bean().vatBeanOther.batch =  vat.item.getValueByName("#F.batch");
	    vat.bean().vatBeanOther.salesUnitType = salesUnitType;
	    vat.bean().vatBeanOther.timeScope = vat.block.$box[vnB_Post].timeScope;
	    	    		    
	    vat.block.submit(function(){return "process_object_name=soPostingTallyAction"+
			                	"&process_object_method_name=execAntiPosting";}, {other:true});
	}
}

function showMessage(){
   
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=SO_POSTING_TALLY" +
		"&levelType=ERROR" +
        "&identification=" + vat.block.$box[vnB_Post].timeScope,
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

//sql明細匯出excel
function doExport(){
    var url = "/erp/jsp/ExportFormView.jsp" + 
              "?exportBeanName=" + vat.bean().vatBeanOther.loginBrandCode + "_POS_DATA_POSTING_SQL" + 
              "&fileType=XLS" + 
              "&processObjectName=soPostingTallyService" + 
              "&processObjectMethodName=getAJAXExportData" + 
              "&brandCode" 			+ "=" + vat.bean().vatBeanOther.loginBrandCode +
              "&employeeCode=" + document.forms[0]["#loginEmployeeCode"].value +
              "&batch=" + vat.item.getValueByName("#F.batch")+
              "&transactionBeginDate=" + vat.item.getValueByName("#F.transactionBeginDate")+
              "&transactionEndDate=" + vat.item.getValueByName("#F.transactionEndDate")+                      
              "&salesUnit=" + vat.item.getValueByName("#F.salesUnit")+
              "&status=" + vat.item.getValueByName("#F.status");
              
    var width = "200";
    var height = "30";  
    window.open(url, '過帳匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

}




function openAccountPage()
{

	var nItemLine = vat.item.getGridLine();
	var machineCode = vat.item.getGridValueByName("unit", nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
	var batch = vat.item.getValueByName("#F.batch");
	var postionDate = vat.item.getGridValueByName("salesDate", nItemLine).replace(/\//g, '').toUpperCase();
	var rootPath = postionDate + "/";
	var fileName = postionDate + "-" + machineCode + "-" + batch;
	var fileType = ".jpg";
	//alert("開啟結帳條"+rootPath+fileName+fileType);
	var WA;
 	WA=window.open("http://10.2.99.87:9090//amount.html?"
 	+"rootPath=" + rootPath
 	+"&fileName=" + fileName
 	+"&fileType="+fileType,
 	"","toolbar=no,location=no,directories=no,width=700,height=400");
	//WA=window.open("\OpenImg.html");
	//WA=window.open("D:\CHECKOUT\20151202\20151202-M6-1.jpg");
}










