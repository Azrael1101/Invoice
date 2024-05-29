
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

function searchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	  
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     ].value,
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value  
	    };
     vat.bean.init(function(){
		return "process_object_name=imPriceAdjustmentMainService&process_object_method_name=executeSearchInitial"; 
   		},{other: true});
  }
}

function headerInitial(){ 
var allOrderTypes=vat.bean("allOrderTypes");
var allStatus = [["","","true"],
                 ["暫存", "簽核中", "簽核完成", "結案", "駁回", "作廢"],
                 ["SAVE","SIGNING","FINISH", "CLOSE", "REJECT" ,"VOID"]];
 
var brandCode = vat.bean().vatBeanOther.loginBrandCode;
var submitDate = "送簽日期";
if( brandCode.indexOf("T2") > -1){
	submitDate = "生效日期";
}
                 
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"商品變價及送簽查詢作業",  
		rows:[	 {row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode"            , type:"LABEL"  , value:"單別"}]},	 
	 {items:[{name:"#F.orderTypeCode"            , type:"SELECT" ,  bind:"orderTypeCode" , mode:"READONLY", init:allOrderTypes}]},		 
	 {items:[{name:"#L.status"                   , type:"LABEL"  , value:"狀態"}]},	 		 
	 {items:[{name:"#F.status"                   , type:"SELECT" ,  bind:"status", size:12, init:allStatus}]},
	 {items:[{name:"#L.orderNo"                  , type:"LABEL"  , value:"單號"}]},
	 {items:[{name:"#F.startOrderNo"             , type:"TEXT"   ,  bind:"startOrderNo", size:20},
			 {name:"#L.between"                  , type:"LABEL"  , value:"至"},
	 		 {name:"#F.endOrderNo"               , type:"TEXT"   ,  bind:"endOrderNo"  , size:20}]}
	 
	 	 
	 ]},	 {row_style:"", cols:[
	 {items:[{name:"#L.supplierCode"            , type:"LABEL"  , value:"廠商代號"}]},	 
	 {items:[{name:"#F.supplierCode", type:"TEXT",  bind:"supplierCode", eChange: function(){ changeSupplierName("supplierCode"); } ,size:20},
	{name:"#B.supplierCode",	value:"選取" ,type:"PICKER" ,
			 									 		openMode:"open", src:"./images/start_node_16.gif",
			 									 		service:"Bu_AddressBook:searchSupplier:20091011.page", 
			 									 		left:0, right:0, width:1024, height:768,	
			 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess();} },
				 	{name:"#F.addressBookId", 		type:"TEXT",  	bind:"addressBookId", back:false, mode:"HIDDEN"},								 		
					{name:"#F.supplierName", type:"TEXT",  	bind:"supplierName", back:false ,size:30, mode:"READONLY"}], td:" colSpan=3"},	

	 {items:[{name:"#L.submitDate"             , type:"LABEL" , value:submitDate}]},	 
	 {items:[{name:"#F.startDate"        , type:"DATE"  ,  bind:"startDate", size:12},
	         {name:"#L.between"                  , type:"LABEL" , value:"至"},
	         {name:"#F.endDate"          , type:"DATE"  ,  bind:"endDate", size:12}], td:" colSpan=3"}
	 
	 	 
	 ]},
{row_style:"", cols:[
	 {items:[{name:"#L.itemCode"                   , type:"LABEL"  , value:"品號"}]},	 		 
	 {items:[{name:"#F.itemCode"                   , type:"TEXT" ,  bind:"itemCode", size:40}], td:" colSpan=3"},
	 {items:[{name:"#L.employeeCode"                , type:"LABEL"  , value:"填單人員"}]},	 
	 {items:[{name:"#F.employeeCode"                , type:"TEXT"   ,  bind:"employeeCode", size:6}]}]}
	 
	 
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
	 	 		{name:"#B.export"	   , type:"IMG"      ,value:"明細匯出",   src:"./images/button_detail_export.gif", eClick:'doExport()'},
	 			{name:"matchExport"	   , type:"LABEL"    ,value:"　"},
	 			{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}
	 			
	 			],
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

	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
    var vbSelectionType = "CHECK";    
	if(vatPickerId != null && vatPickerId != ""){
         vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
         vbSelectionType = "CHECK";    
    }else{
         vbSelectionType = "NONE";
    }

    vat.item.make(vnB_Detail, "indexNo"                   , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "orderTypeCode"             , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"單別"      });
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "description"               , {type:"TEXT" , size: 20, maxLen:20, mode:"READONLY", desc:"說明"   });
	vat.item.make(vnB_Detail, "priceType"                 , {type:"TEXT" , size:8, maxLen:8, mode:"READONLY", desc:"價格類型"   });
	vat.item.make(vnB_Detail, "enableDate"                , {type:"DATE" , size:12, maxLen:12, mode:"READONLY", desc:"啟用日期"   }); 
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "supplierCode"              , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"廠商代號"   });
	vat.item.make(vnB_Detail, "supplierName"              , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY",alter:true, desc:"廠商名稱"	});
	vat.item.make(vnB_Detail, "headId"                    , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId"],
//														pickAllService		: "selectAll",
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()", 
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.picker" , "display", "none");
		vat.item.setStyleByName("#B.export" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.picker" , "display", "inline");
		vat.item.setStyleByName("#B.export" , "display", "inline");
	}
}

function saveSuccessAfter(){
		
}

function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
//    alert("employeeCode = " + vat.item.getValueByName("#F.employeeCode"      ) );
	var processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
	                  "&orderTypeCode"          + "=" + vat.item.getValueByName("#F.orderTypeCode"        ) +     
	                  "&startOrderNo"           + "=" + vat.item.getValueByName("#F.startOrderNo"         ) +     
	                  "&endOrderNo"             + "=" + vat.item.getValueByName("#F.endOrderNo"           ) +    
	                  "&startDate"              + "=" + vat.item.getValueByName("#F.startDate"    ) +     
					  "&endDate"        		+ "=" + vat.item.getValueByName("#F.endDate"      ) +     
					  "&supplierCode"        	+ "=" + vat.item.getValueByName("#F.supplierCode"      ) +     
					  "&employeeCode"        	+ "=" + vat.item.getValueByName("#F.employeeCode"      ) +
					  "&itemCode"         		+ "=" + vat.item.getValueByName("#F.itemCode"        ) +  
	                  "&status"                 + "=" + vat.item.getValueByName("#F.status"               ) ;                                                      
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
		processString = "process_object_name=imPriceAdjustmentMainService&process_object_method_name=saveSearchResult";
		//alert(processString);
	}
	
	return processString;
}								


function doSearch(){
    //alert("searchService");	
   
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+ 
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope;  }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);} 
			                    });
}

function doClosePicker(){
    //alert("doClosePicker");
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imPriceAdjustmentMainService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:2} );
    			}}); 
}

//sql明細匯出excel
function doExport(){
    var url = "/erp/jsp/ExportFormView.jsp" + 
              "?exportBeanName=IM_PRICE_ADJUSTMENT_SQL" + 
              "&fileType=XLS" + 
              "&processObjectName=imPriceAdjustmentMainService" + 
              "&processObjectMethodName=getAJAXExportData" + 
              "&brandCode" 			+ "=" + vat.bean().vatBeanOther.loginBrandCode +     
	          "&orderTypeCode"     	+ "=" + vat.item.getValueByName("#F.orderTypeCode"   ) +     
	          "&startOrderNo"   	+ "=" + vat.item.getValueByName("#F.startOrderNo" ) +     
	          "&endOrderNo"   		+ "=" + vat.item.getValueByName("#F.endOrderNo" ) +
              "&startDate"   		+ "=" + vat.item.getValueByName("#F.startDate"         ) +
              "&endDate"   			+ "=" + vat.item.getValueByName("#F.endDate"         ) +
              "&employeeCode"   	+ "=" + vat.item.getValueByName("#F.employeeCode"         ) +
              "&status"   			+ "=" + vat.item.getValueByName("#F.status"         );
              
    var width = "200";
    var height = "30";  
    window.open(url, '定變價匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

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

// 清除
function resetForm(){
	vat.item.setValueByName("#F.status", "");
    vat.item.setValueByName("#F.employeeCode", "");
    vat.item.setValueByName("#F.startOrderNo", "");
    vat.item.setValueByName("#F.endOrderNo", "");
    vat.item.setValueByName("#F.startDate", "");
    vat.item.setValueByName("#F.endDate", "");
}

// 開啟視窗
function openModifyPage(){

    var nItemLine = vat.item.getGridLine();

    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
	var orderTypeCode = vat.item.getGridValueByName("orderTypeCode", nItemLine);
	var userType = document.forms[0]["#userType"].value;
	var page = "/erp/Im_ItemPriceAdjustment:create:20091201.page";
	if(vat.bean().vatBeanOther.loginBrandCode.indexOf("T2") <= -1){
		page = "/erp/Im_ItemPriceAdjustment:create:20091201.page";
	}else{
		page = "/erp/Im_ItemPriceAdjustment:createT2:20100104.page";
	}

	if(!(vFormId == "" || vFormId == "0")){
    var url = page + "?formId=" + vFormId + "&orderTypeCode="+ orderTypeCode + "&userType="+userType; 
	
     sc=window.open(url, '定變價維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
    } 
}

function changeSupplierName( code ){
//	alert( code + "\n" + vat.item.getValueByName("#F.addressBookId") +"\n" + vat.item.getValueByName("#F.supplierCode") );

	vat.ajax.XHRequest({
		post:"process_object_name=buSupplierWithAddressViewService"+
                  "&process_object_method_name=getAJAXSupplierName"+
                  "&brandCode=" + document.forms[0]["#loginBrandCode"    ].value + 
                  "&addressBookId=" + ( "addressBookId" === code ? vat.item.getValueByName("#F.addressBookId") : "" )+
                  "&supplierCode=" + ( "supplierCode" === code ? vat.item.getValueByName("#F.supplierCode") : "" ),
                  
		find: function change(oXHR){ 
         		vat.item.setValueByName("#F.supplierCode", vat.ajax.getValue("supplierCode", oXHR.responseText));
         		vat.item.setValueByName("#F.supplierName", vat.ajax.getValue("supplierName", oXHR.responseText) );
         		vat.item.setValueByName("#F.currencyCode", vat.ajax.getValue("currencyCode", oXHR.responseText) );
         		//setExchangeRate();
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.supplierName", "查無此供應商");
		}   
	});	
}

function doAfterPickerFunctionProcess(){
	//do picker back something
	if(typeof vat.bean().vatBeanPicker.result != "undefined"){
    	vat.item.setValueByName("#F.addressBookId", vat.bean().vatBeanPicker.result[0].addressBookId); 
		changeSupplierName("addressBookId");
	}
}