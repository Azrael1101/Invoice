	
/*** 
 *	檔案: imReplenishBasicParameterSearch.js 自動補貨基本參數查詢
 *	說明：表單明細
 *	修改：david
 *  <pre>
 *  	Created by david
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
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
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value  
	    };
	vat.bean.init(	
  		function(){
			return "process_object_name=imReplenishAction&process_object_method_name=performBasicParameterSearchInitial"; 
    	},{
    		other: true
   	});   
  }
}

// 可搜尋的欄位
function headerInitial(){ 
	var allWarehouses = vat.bean("allWarehouses");
	
vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"基本參數查詢作業", rows:[  
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.type", 		type:"LABEL"  , value:"參數類別"}]},
				{items:[{name:"#F.type", 		type:"TEXT", bind:"type"}]},
				{items:[{name:"#L.brandCode", 		type:"LABEL"  , value:"品牌"}]},
				{items:[{name:"#F.brandCode", 		type:"TEXT",mode:"HIDDEN", bind:"brandCode" },
						{name:"#F.brandName", 		type:"TEXT", bind:"brandName"  , size:20, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
	 			{items:[{name:"#L.parameter", 		type:"LABEL"  , value:"參數"}]},
				{items:[{name:"#F.parameter", 		type:"NUMB", bind:"type"}]},
				{items:[{name:"#L.value", 			type:"LABEL"  , value:"參數值"}]},
				{items:[{name:"#F.value", 			type:"NUMB", bind:"type"}]}
				
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
	 			{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}],
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

    vat.item.make(vnB_Detail, "indexNo"         	, {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "type"   				, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"參數類別"      });
	vat.item.make(vnB_Detail, "parameter"   		, {type:"NUMB" , size:18, maxLen:20, mode:"READONLY", desc:"參數"      });
	vat.item.make(vnB_Detail, "value"   			, {type:"NUMB" , size:18, maxLen:20, mode:"READONLY", desc:"參數值"      });
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["id.type", "id.brandCode", "id.parameter"],
														selectionType       : vbSelectionType,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
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

// 查詢點下執行
function loadBeforeAjxService(){
   
	var processString = "process_object_name=imReplenishService&process_object_method_name=getAJAXBasicParameterSearchPageData" + 
                      "&loginBrandCode"         + "=" + vat.bean().vatBeanOther.loginBrandCode +     
                      "&type"         			+ "=" + vat.item.getValueByName("#F.type" )+
                      "&parameter=" + vat.item.getValueByName("#F.parameter" ) +
                      "&value=" + vat.item.getValueByName("#F.value" );     
                                                                            
	return processString;											
}

// 明細存檔成功後
function saveSuccessAfter(){
}

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=imReplenishService&process_object_method_name=saveBasicParameterSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=imReplenishService&process_object_method_name=getBasicParameterSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

// 查詢按下後
function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
}


function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.type", "");
	vat.item.setValueByName("#F.parameter", "");
	vat.item.setValueByName("#F.value", "");
}

// 開啟視窗
function openModifyPage(){

    var nItemLine = vat.item.getGridLine();

    var vFormId = vat.item.getGridValueByName("headId", nItemLine);

	if(!(vFormId == "" || vFormId == "0")){
    var url = "/erp/Im_Replenish:create:20100607.page?formId=" + vFormId; 

     sc=window.open(url, '基本參數維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
    } 
}