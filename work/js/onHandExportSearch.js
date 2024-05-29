
vat.debug.disable();
var afterSavePageProcess = "";

function kweOnHandExportBlock(){
  kweOnHandExportInitial();
  kweOnHandExportHeader();
  kweOnHandExportButtonLine();
}

function kweOnHandExportButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search1"      , type:"IMG"      ,value:"舊條碼",   src:"./images/button_barcode_print_old.gif", eClick:"doSearch('1')"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.search2"      , type:"IMG"      ,value:"送出",   src:"./images/button_barcode_print_new.gif", eClick:"doSearch('2')"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function kweOnHandExportHeader(){ 
var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");

vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"庫存轉條碼", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.wareHouseCode"            , type:"LABEL"  , value:"倉庫"}]},	 
	 {items:[{name:"#F.wareHouseCode"            , type:"SELECT" ,  bind:"type", size:1, init:allDeliveryWarehouses}]},		 
	 {items:[{name:"#L.transactionDate"          , type:"LABEL"  , value:"庫存日期"}]},	 
	 {items:[{name:"#F.transactionDate"          , type:"DATE"   ,  bind:"transactionDate", size:12}
	 		 ]}	 
	 ]}],
		beginService:"",
		closeService:""			
	});
}

/*
	判斷是否要關閉LINE
*/
function checkEnableLine() {
	return true;
}

function kweOnHandExportInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {brandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value
	    };
     vat.bean.init(function(){
		return "process_object_name=imTransationService&process_object_method_name=executeInitial"; 
   		},{other: true});
  }
}

function doSearch(type){
	var wCode = document.forms[0]["#F.wareHouseCode"].value;
	var tDate = document.forms[0]["#F.transactionDate"].value;
	var bCode = document.forms[0]["#loginBrandCode"].value;
	
	if("" == wCode){
		alert('請輸入庫別');
	}else if("" == tDate){
		alert('請輸入庫存日期');
	}else if ( '1' == type){
		window.open("/erp/jsp/StandardExport.jsp?exportBeanName=ONHAND_ITEM&fileType=TXT&transactionDate="+tDate+"&wareHouseCode="+wCode+"&brandCode="+bCode,'匯出','menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');	
	}else if ( '2' == type){
		window.open("/erp/jsp/StandardExport.jsp?exportBeanName=ONHAND_ITEM_T1&fileType=TXT&transactionDate="+tDate+"&wareHouseCode="+wCode+"&brandCode="+bCode,'匯出','menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');	
	}
}

function doClosePicker(){
	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=buAddressBookService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

function doView(){
	   vat.block.pageSearch(2, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=buAddressBookService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:2} );
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
  processString = "process_object_name=buAddressBookServiceService&process_object_method_name=updateAllSearchData";
  return processString;
}