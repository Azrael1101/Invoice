
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
    vat.bean().vatBeanOther = 
	{
		brandCode     	: document.forms[0]["#loginBrandCode"    ].value,   	
		orderTypeCode	: document.forms[0]["#orderTypeCode"     ].value,
		vatPickerId		: document.forms[0]["#vatPickerId"       ].value  
    };
    
    vat.bean.init(function(){
	return "process_object_name=imStorageAction&process_object_method_name=performSearchInitial";
  		},{other: true});
}

function headerInitial(){ 
	var allOrderTypes=vat.bean("allOrderTypes");
	var allStatus = [["","","true"],
                 ["暫存", "簽核中", "待轉入","簽核完成", "結案", "駁回", "作廢"],
                 ["SAVE","SIGNING","WAIT_IN","FINISH", "CLOSE", "REJECT" ,"VOID"]];
                 
	vat.block.create(vnB_Header = 1, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"儲位查詢作業", rows:[  
			 {row_style:"", cols:[
				 {items:[{name:"#L.orderTypeCode"            , type:"LABEL"  , value:"單別"}]},	 
				 {items:[{name:"#F.orderTypeCode"            , type:"SELECT" ,  bind:"orderTypeCode" , mode:"READONLY", init:allOrderTypes}]},		 
				 {items:[{name:"#L.status"                   , type:"LABEL"  , value:"狀態"}]},	 		 
				 {items:[{name:"#F.status"                   , type:"SELECT" ,  bind:"status", size:12, init:allStatus}]},
				 {items:[{name:"#L.employeeCode"             , type:"LABEL"  , value:"填單人員"}]},	 
				 {items:[{name:"#F.employeeCode"             , type:"TEXT"   ,  bind:"employeeCode", size:6}]}
			 ]},
			 {row_style:"", cols:[
				 {items:[{name:"#L.orderNo"                 , type:"LABEL"  , value:"單號"}]},
				 {items:[{name:"#F.orderNo"             	, type:"TEXT"   ,  bind:"orderNo", size:20}]},		 
				 {items:[{name:"#L.startDate"        		, type:"LABEL" , value:"異動日期"}]},
				 {items:[{name:"#F.startDate"        		, type:"DATE"  ,  bind:"startDate", size:12},
				         {name:"#L.between"                 , type:"LABEL" , value:"至"},
				         {name:"#F.endDate"          		, type:"DATE"  ,  bind:"endDate", size:12}], td:" colSpan=3"}
			 ]},
			 {row_style:"", cols:[
				 {items:[{name:"#L.sourceOrderTypeCode"    	, type:"LABEL"  , value:"來源單別"}]},
				 {items:[{name:"#F.sourceOrderTypeCode" 	, type:"TEXT"   ,  bind:"sourceOrderTypeCode", size:20}]},		 
				 {items:[{name:"#L.sourceOrderNo"        	, type:"LABEL" , value:"來源單號"}]},
				 {items:[{name:"#F.sourceOrderNo"        	, type:"TEXT"  ,  bind:"sourceOrderNo", size:12}], td:" colSpan=3"}				         
			 ]}	         
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
	vat.item.make(vnB_Detail, "orderTypeCode"             , {type:"TEXT" , size:8, 	maxLen:20, mode:"READONLY", desc:"單別"      });
	vat.item.make(vnB_Detail, "orderNo"                   , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "sourceOrderTypeCode"       , {type:"TEXT" , size:8, 	maxLen:12, mode:"READONLY", desc:"來源單別"   });
	vat.item.make(vnB_Detail, "sourceOrderNo"             , {type:"TEXT" , size:15, maxLen:12, mode:"READONLY", desc:"來源單號"	});
	vat.item.make(vnB_Detail, "transactionDate"           , {type:"DATE" , size:12, maxLen:12, mode:"READONLY", desc:"異動日期"   }); 
	vat.item.make(vnB_Detail, "status"                    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "storageHeadId"                    , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["storageHeadId"],
//														pickAllService		: "selectAll",
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",
														loadSuccessAfter    : "loadSuccessAfter()", 
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter 	: "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		vat.item.setStyleByName("#B.picker" , "display", "none");
		vat.item.setStyleByName("#B.export" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
		vat.item.setStyleByName("#B.picker" , "display", "inline");
		vat.item.setStyleByName("#B.export" , "display", "inline");
	}
}

function saveSuccessAfter(){
		
}

function loadBeforeAjxService(){
	var processString = "process_object_name=imStorageService&process_object_method_name=getAJAXSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.brandCode +     
	                  "&orderTypeCode"          + "=" + vat.item.getValueByName("#F.orderTypeCode"        ) +     
	                  "&orderNo"           		+ "=" + vat.item.getValueByName("#F.orderNo"         ) +     
	                  "&startDate"              + "=" + vat.item.getValueByName("#F.startDate"    ) +     
					  "&endDate"        		+ "=" + vat.item.getValueByName("#F.endDate"      ) +    
					  "&sourceOrderTypeCode"    + "=" + vat.item.getValueByName("#F.sourceOrderTypeCode"    ) +     
					  "&sourceOrderNo"        	+ "=" + vat.item.getValueByName("#F.sourceOrderNo"      ) +    
					  "&employeeCode"        	+ "=" + vat.item.getValueByName("#F.employeeCode"      ) +
	                  "&status"                 + "=" + vat.item.getValueByName("#F.status"               ) ;                                                      
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
		processString = "process_object_name=imStorageService&process_object_method_name=saveSearchResult";
	}
	
	return processString;
}								


function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+ 
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope;  }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);} 
			                    });
}

function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imStorageService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

//sql明細匯出excel
function doExport(){
    var url = "/erp/jsp/ExportFormView.jsp" + 
              "?exportBeanName=IM_STORAGE_SQL" + 
              "&fileType=XLS" + 
              "&processObjectName=imStorageService" + 
              "&processObjectMethodName=getAJAXSearchExportData" + 
              "&brandCode" 				+ "=" + vat.bean().vatBeanOther.brandCode +     
	          "&orderTypeCode"     		+ "=" + vat.item.getValueByName("#F.orderTypeCode"   ) +     
	          "&orderNo"   				+ "=" + vat.item.getValueByName("#F.orderNo" ) +     
              "&startDate"   			+ "=" + vat.item.getValueByName("#F.startDate"         ) +
              "&endDate"   				+ "=" + vat.item.getValueByName("#F.endDate"         ) +
              "&sourceOrderTypeCode"    + "=" + vat.item.getValueByName("#F.sourceOrderTypeCode"    ) +     
			  "&sourceOrderNo"        	+ "=" + vat.item.getValueByName("#F.sourceOrderNo"      ) +    
              "&employeeCode"   		+ "=" + vat.item.getValueByName("#F.employeeCode"         ) +
              "&status"   				+ "=" + vat.item.getValueByName("#F.status"         );
              
    var width = "200";
    var height = "30";  
    window.open(url, '儲位單匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);

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
    vat.item.setValueByName("#F.orderNo", "");
    vat.item.setValueByName("#F.startDate", "");
    vat.item.setValueByName("#F.endDate", "");
    vat.item.setValueByName("#F.sourceOrderTypeCode", "");
    vat.item.setValueByName("#F.sourceOrderNo", "");
    vat.item.setValueByName("#F.itemCode", "");
   
}

// 開啟視窗
function openModifyPage(){

    var nItemLine = vat.item.getGridLine();

    var vFormId = vat.item.getGridValueByName("storageHeadId", nItemLine);
	var orderTypeCode = vat.item.getGridValueByName("orderTypeCode", nItemLine);
	var page = "/erp/Im_Storage:create:20110331.page";

	if(!(vFormId == "" || vFormId == "0")){
    var url = page + "?formId=" + vFormId + "&orderTypeCode="+ orderTypeCode; 
	
     sc=window.open(url, '儲位單', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
    } 
}