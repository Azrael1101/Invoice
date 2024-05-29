vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Header = 1;
function outlineBlock(){
    formDataInitial();
    buttonLine(); 
    headerInitial();
    doFormAccessControl(); 
}

function formDataInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined' &&
      document.forms[0]["#shopCodeKey"].value != '[binding]' && document.forms[0]["#salesDateKey"].value != '[binding]'){
  	  vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value,	  
  	     shopCodeKey        : document.forms[0]["#shopCodeKey"].value,   
	     salesDateKey       : document.forms[0]["#salesDateKey"].value,
	     currentRecordNumber: 0,
	     lastRecordNumber   : 0
	    }; 
	    
      vat.bean.init(function(){
		return "process_object_name=soShopDailyAction&process_object_method_name=performInitial"; 
      },{other: true});
  }
      vat.item.bindAll();
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
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"So_SalesOrder:searchShopDailyData:20091129.page",
	 									 left:0, right:0, width:1024, height:768,
	 									 servicePassData:function(){ return doPassData();}, 	
	 									 serviceAfterPick:function(){doAfterPickerProcess()}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 		//	{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

function headerInitial(){

var brandCode = document.forms[0]["#loginBrandCode"].value;
var allShops = vat.bean("allShops");

var salesUnit = "專櫃<font color='red'>*</font>";
var visitorCountLabel = "來客數";
if("T2" == brandCode){
    salesUnit = "機台<font color='red'>*</font>";
    visitorCountLabel = "交易筆數";
}


vat.block.create(vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"width = '300' cellspacing='1' class='brown' border='1' cellpadding='2'",
	title:"POS每日資料維護作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.brandCode", type:"LABEL", value:"品牌"}]},	 
	 {items:[{name:"#F.brandCode", type:"TEXT", size:1, mode:"READONLY", init:brandCode}]}]},		  		 
	 {row_style:"", cols:[
	 {items:[{name:"#L.shopCode", type:"LABEL", value:salesUnit}]},
	 {items:[{name:"#F.shopCode", type:"SELECT", bind:"shopCode", back:false, size:1, init:allShops}]}]},  
 	 {row_style:"", cols:[
	 {items:[{name:"#L.salesDate", type:"LABEL", value:"銷售日期<font color='red'>*</font>"}]},
	 {items:[{name:"#F.salesDate", type:"DATE", bind:"salesDate", back:false, size:1, mode:"READONLY"}]}]},		 	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.visitorCount", type:"LABEL",  value:visitorCountLabel}]},
	 {items:[{name:"#F.visitorCount", type:"NUMB",  bind:"visitorCount", size:10, maxLen:8}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.totalActualSalesAmount", type:"LABEL", value:"營收彙總金額<font color='red'>*</font>"}]},
	 {items:[{name:"#F.totalActualSalesAmount", type:"NUMB", bind:"totalActualSalesAmount", size:10, maxLen:8}]}]},
	 
	 {row_style:"", cols:[
	 {items:[{name:"#L.bulletin", type:"LABEL", value:"<font color='red'>*</font>為必填欄位，請務必填寫"}], td:" colSpan=2"}]}	    
	], 	 
		beginService:"",
		closeService:""			
	});
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
    refreshForm("", "", "1");
}

function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	refreshForm("", "", "2");
	 }
}

function refreshForm(vShopCode, vSaleDate, vFlag){
	document.forms[0]["#shopCodeKey"].value = vShopCode;
	document.forms[0]["#salesDateKey"].value = vSaleDate;		
	vat.bean().vatBeanOther.shopCodeKey = document.forms[0]["#shopCodeKey"].value;
	vat.bean().vatBeanOther.salesDateKey = document.forms[0]["#salesDateKey"].value;
	var lastInputShopCode = vat.item.getValueByName("#F.shopCode");
	var lastInputSalesDate = vat.item.getValueByName("#F.salesDate");	
	vat.block.submit(
		function(){
			return "process_object_name=soShopDailyAction&process_object_method_name=performInitial";  
     	},{other: true, 
     	   funcSuccess:function(){
     	       vat.item.bindAll();    		  
     		   doFormAccessControl();
     		   if("1" == vFlag){
     		       vat.item.setValueByName("#F.shopCode", lastInputShopCode);
     		       vat.item.setValueByName("#F.salesDate", lastInputSalesDate);
     		       vat.item.setValueByName("#F.visitorCount", "");
     		       vat.item.setValueByName("#F.totalActualSalesAmount", "");
     		   }
     	  }}
    );
}

function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
		
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.shopCodeKey = document.forms[0]["#shopCodeKey"].value;
	    vat.bean().vatBeanOther.salesDateKey = document.forms[0]["#salesDateKey"].value;	    
	    vat.block.submit(function(){return "process_object_name=soShopDailyAction"+
			                	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
	}
}

function doFormAccessControl(){

    var shopCodeKey = document.forms[0]["#shopCodeKey"].value.replace(/^\s+|\s+$/, '');
	var salesDateKey = document.forms[0]["#salesDateKey"].value.replace(/^\s+|\s+$/, '');
	if(shopCodeKey != "" && salesDateKey != ""){
	    vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
	    vat.item.setAttributeByName("#F.salesDate", "readOnly", true);
	}else{
	    vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
	    vat.item.setAttributeByName("#F.salesDate", "readOnly", false);
	}
}

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var shopCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.shopCode'];
		var salesDate = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.salesDate'];  
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(shopCode, salesDate);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	var shopCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.shopCode'];
		var salesDate = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.salesDate'];  
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(shopCode, salesDate);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	var shopCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.shopCode'];
		var salesDate = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.salesDate'];  
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(shopCode, salesDate);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    var shopCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.shopCode'];
		var salesDate = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.salesDate']; 
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(shopCode, salesDate);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

function doPassData(){
    var suffix = "";
	var brandCode = document.forms[0]["#loginBrandCode"].value;  	
  	var employeeCode = document.forms[0]["#loginEmployeeCode"].value;	
    suffix += "&loginBrandCode="+escape(brandCode)+"&loginEmployeeCode="+escape(employeeCode); 
	return suffix;
}

 // 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var shopCode = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.shopCode'];
		  var salesDate = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['id.salesDate'];
		  refreshForm(shopCode, salesDate);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}