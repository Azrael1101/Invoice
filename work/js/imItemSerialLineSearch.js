
vat.debug.disable();

var vnB_Header = 1;
var vnB_Detail = 2;
function outlineBlock(){
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();
  
  doFormAccessControl();
}	

function searchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	    	loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	    	loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
  	    	itemCode		   : document.forms[0]["#itemCode" ].value,
  	    	isUsed			   : document.forms[0]["#isUsed" ].value,
  	    	serial			   : document.forms[0]["#serial" ].value,
  	    	vatPickerId	   : document.forms[0]["#vatPickerId" ].value	  
	    };
     vat.bean.init(function(){
		return "process_object_name=imItemAction&process_object_method_name=performSerialSearchInitial"; 
   		},{other: true});
  }
}

function headerInitial(){ 
	 var selectYorN = [["", true, false], ["<請選擇>","是", "否"], ["","Y", "N"]];
	
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"商品序號查詢作業", rows:[  
		 {row_style:"", cols:[
			 {items:[{name:"#L.itemCode"   		, type:"LABEL"  , value:"品號"}]},	 
			 {items:[{name:"#F.itemCode"        , type:"TEXT" ,  bind:"itemCode", size:40}]},		 
			 {items:[{name:"#L.serial"          , type:"LABEL"  , value:"序號"}]},	 		 
			 {items:[{name:"#F.serial"          , type:"TEXT" ,  bind:"serial", size:12}]},
			 {items:[{name:"#L.isUsed"     		, type:"LABEL"  , value:"是否使用過"}]},	 
			 {items:[{name:"#F.isUsed"    		, type:"SELECT", init:selectYorN,  bind:"isUsed"}]} 
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

    vat.item.make(vnB_Detail, "checkbox"          , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"           , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "itemCode"          , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"品號"      });
	vat.item.make(vnB_Detail, "serial"            , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"序號"      });
	vat.item.make(vnB_Detail, "isUsedName"        , {type:"TEXT" , size:12, maxLen:12, mode:"READONLY", desc:"是否使用過"   }); 
	vat.item.make(vnB_Detail, "lastUpdateDate"    , {type:"TEXT" , size: 8, maxLen:12, mode:"READONLY", desc:"更新日期"      });
	vat.item.make(vnB_Detail, "lineId"      	  , {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail, {
		id                  : "vatDetailDiv",
		pageSize            : 10,
		searchKey           : ["lineId","serial"],
		selectionType       : "CHECK",
		indexType           : "AUTO",
		canGridDelete       : vbCanGridDelete,
		canGridAppend       : vbCanGridAppend,
		canGridModify       : vbCanGridModify,	
		loadBeforeAjxService: "loadBeforeAjxService()",
		loadSuccessAfter    : "loadSuccessAfter()", 
		saveBeforeAjxService: "saveBeforeAjxService()",
		saveSuccessAfter : "saveSuccessAfter()"
	});
}

// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.picker" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.picker" , "display", "inline");
	}
}

function saveSuccessAfter(){
		
}

function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
	var processString = "process_object_name=imItemService&process_object_method_name=getAJAXSerialSearchPageData" + 
                      "&loginBrandCode=" 	+ vat.bean().vatBeanOther.loginBrandCode +     
	                  "&itemCode=" 			+ vat.item.getValueByName("#F.itemCode"        ) +     
	                  "&isUsed=" 			+ vat.item.getValueByName("#F.isUsed"         ) +     
	                  "&serial=" 			+ vat.item.getValueByName("#F.serial"           )+     
	                  "&isHead=false";                                                      
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
		processString = "process_object_name=imItemService&process_object_method_name=saveSerialLineSearchResult";
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
    		                     function(){ return "process_object_name=imItemService&process_object_method_name=getSerialSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
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

// 清除
function resetForm(){
    vat.item.setValueByName("#F.itemCode", "");
    vat.item.setValueByName("#F.serial", "");
    vat.item.setValueByName("#F.isUsed", "N");
}

// 鎖form
function doFormAccessControl(){
	var itemCode = document.forms[0]["#itemCode" ].value;  
	var isUsed = document.forms[0]["#isUsed" ].value;     
	var serial = document.forms[0]["#serial" ].value;   
	
	vat.item.setAttributeByName("#F.itemCode", "readOnly", false);
	vat.item.setAttributeByName("#F.isUsed", "readOnly", false);
	vat.item.setAttributeByName("#F.serial", "readOnly", false);
	
	if(itemCode != ""){
		vat.item.setValueByName("#F.itemCode", itemCode);  
	}
	if(isUsed == "Y" || isUsed == "N"){
		vat.item.setValueByName("#F.isUsed", isUsed);  
		vat.item.setAttributeByName("#F.isUsed", "readOnly", true);
	}
	if(serial != ""){
		vat.item.setValueByName("#F.serial", serial);  
	}
}
