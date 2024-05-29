
vat.debug.disable();
var afterSavePageProcess = "";

function kweSupplierBlock(){
  kweSupplierSearchInitial();
  kweSupplierHeader();
  kweSupplierButtonLine();
  kweSupplierDetail();
}

function kweSupplierButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:""},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.update"      , type:"IMG"   , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doView()"},
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

function kweSupplierHeader(){ 
var allSupplierType=vat.bean("allSupplierType");
var allSupplierClass=vat.bean("allSupplierClass");
//var allDeliveryWarehouses = vat.bean("allDeliveryWarehouses");
//var allArrivalWarehouses  = vat.bean("allArrivalWarehouses");
var allOrderType =[["","","true"],
                 ["請選擇","自然人","法人"],
                 [,1,2]];
var allStatus = [["","","true"],
                 ["暫存中","簽核中","待轉出","待轉入","簽核完成","結案","待確認"],
                 ["SAVE","SIGNING","WAIT_OUT","WAIT_IN","FINISH","CLOSE","UNCONFIRMED"]];
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"供應商資料查詢作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.type"            , type:"LABEL"  , value:"類別"}]},	 
	 {items:[{name:"#F.type"            , type:"SELECT" ,  bind:"type", size:1, init:allOrderType}]},		 
	 {items:[{name:"#L.identityCode"                , type:"LABEL"  , value:"身份證明代號"}]},	 
	 {items:[{name:"#F.identityCode"                , type:"TEXT"   ,  bind:"identityCode", size:6}]},
	 {items:[{name:"#L.chineseName"                , type:"LABEL"  , value:"姓名"}]},	 
	 {items:[{name:"#F.chineseName"                , type:"TEXT"   ,  bind:"chineseName", size:6}]}	 
	 ]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.supplierTypeCode"            , type:"LABEL"  , value:"廠商類別"}]},	 
	 {items:[{name:"#F.supplierTypeCode"            , type:"SELECT" ,  bind:"supplierTypeCode", size:1, init:allSupplierType}]},		 
	 {items:[{name:"#L.supplierCode"                , type:"LABEL"  , value:"廠商代號"}]},	 
	 {items:[{name:"#F.supplierCode"                , type:"TEXT"   ,  bind:"supplierCode", size:12}]},
	 {items:[{name:"#L.categoryCode"                , type:"LABEL"  , value:"廠商類型"}]},	 
	 {items:[{name:"#F.categoryCode"                , type:"SELECT"   ,  bind:"categoryCode", size:1,init:allSupplierClass}]}	 
	 ]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.customsBroker"                  , type:"LABEL"  , value:"報關行"}]},
	 {items:[{name:"#F.customsBroker"             , type:"TEXT"   ,  bind:"customsBroker", size:12}]},		 
	 {items:[{name:"#L.agent"                  , type:"LABEL"  , value:"代理商"}]},
	 {items:[{name:"#F.agent"             , type:"TEXT"   ,  bind:"agent", size:12}]},
	 {items:[{name:"#L.submitDate"             , type:"LABEL" , value:"佣金比例"}]},
	 {items:[{name:"#F.commissionRate_Start"        , type:"TEXT"  ,  bind:"commissionRate_Start", size:8},
	         {name:"#L.between"                  , type:"LABEL" , value:"%至"},
	         {name:"#F.commissionRate_End"          , type:"TEXT"  ,  bind:"commissionRate_End", size:8},
	         {name:"#L.endBetween"                  , type:"LABEL" , value:"%"}], td:" colSpan=3"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});

}



function kweSupplierDetail(){
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

  var vnB_Detail = 2;
    vat.item.make(vnB_Detail, "checkbox"                  , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "type"             		  , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"類別"      });
	vat.item.make(vnB_Detail, "identityCode"              , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"身份證明代號"      });
	vat.item.make(vnB_Detail, "chineseName"               , {type:"TEXT" , size: 20, maxLen:20, mode:"READONLY", desc:"姓名"   });
	vat.item.make(vnB_Detail, "supplierTypeCode"          , {type:"TEXT" , size:8, maxLen:8, mode:"READONLY", desc:"廠商類別"   });
	vat.item.make(vnB_Detail, "supplierCode"              , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"廠商代號"   });
	vat.item.make(vnB_Detail, "categoryCode"              , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"廠商類型"   });
	vat.item.make(vnB_Detail, "customsBroker"             , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"報關行"   });
	vat.item.make(vnB_Detail, "lastUpdateDate"            , {type:"DATE" , size:12, maxLen:12, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail, "addressBookId"             , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"流水號PK"   }); 
	vat.item.make(vnB_Detail, "addressBookId"                    , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["addressBookId","supplierCode"],
														pickAllService		: "selectAll",
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
}
function saveSuccessAfter(){}

function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
    //vat.item.setValueByName("#F.orderTypeCode","IMV");
	//vat.item.setValueByName("#F.deliveryWarehouseCode","T1BS99");
	//vat.item.setValueByName("#F.arrivalWarehouseCode" ,"T1BS08");
	//vat.item.setValueByName("#F.status"               ,"CLOSE");
	var processString = "process_object_name=buAddressBookService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&type"                   + "=" + vat.item.getValueByName("#F.type"        ) +     
	                  "&identityCode"           + "=" + vat.item.getValueByName("#F.identityCode"         ) +     
	                  "&chineseName"            + "=" + vat.item.getValueByName("#F.chineseName"           ) + 
	                  "&supplierTypeCode"       + "=" + vat.item.getValueByName("#F.supplierTypeCode")+
	                  "&supplierCode"           + "=" + vat.item.getValueByName("#F.supplierCode"    ) +     
					  "&categoryCode"           + "=" + vat.item.getValueByName("#F.categoryCode"      ) + 
					  "&customsBroker"        	+ "=" + vat.item.getValueByName("#F.customsBroker"      ) +
					  "&agent"        		    + "=" + vat.item.getValueByName("#F.agent"      ) +
					  "&commissionRateStart"    + "=" + vat.item.getValueByName("#F.commissionRate_Start"      ) +
					  "&commissionRateEnd"      + "=" + vat.item.getValueByName("#F.commissionRate_End"      )  ;                                                      
	//alert(	processString);
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
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=buAddressBookService&process_object_method_name=saveSearchResult";
		//alert(processString);
	}
	
	return processString;
}								



/*
	PICKER 之前要先RUN LINE SAVE
*/
function doBeforePicker(){
    vat.block.pageDataSave(0);
}



function kweSupplierSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	  
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     ].value
	    };
     vat.bean.init(function(){
		return "process_object_name=buAddressBookService&process_object_method_name=executeSearchInitial"; 
   		},{other: true});
     
   
   
  }
 
}

function doSearch(){
    //alert("searchService");	
   // vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
   // alert("timeScope:"+vat.bean().timeScope);
   
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1);}
			                    });
	/*		                    
	if( vat.block.getGridObject().dataCount == 0){
	
	}		             
	*/      
	//vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1)
}

function doClosePicker(){
    //alert("doView");
	//vat.bean().vatBeanPicker.xxx = 1;

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