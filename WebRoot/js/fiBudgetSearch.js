+/*** 
 *	檔案: cmCustomsDeclarationSearch.js
 *	說明：海關進出倉查詢
 *	修改：Joe
 *  <pre>
 *  	Created by Joe
 *  	All rights reserved.
 *  </pre>
 */
 
vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Detail = 1;

function kweImBlock(){
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();
  kweImDetail();

}

function kweImSearchInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	
  	     vatPickerId        : document.forms[0]["#vatPickerId"       ].value  
	    };
	    vat.bean.init(function(){
			return "process_object_name=fiBudgetHeadAction&process_object_method_name=performSearchInitial"; 
		},{other: true});
  }
}

function kweButtonLine(){
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
	 			{name:"#B.view"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"},
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

function kweImHeader(){ 
	var date = new Date();
	var currentYear = date.getFullYear();
	var years = new Array();
	var config = ["","","true"];
	var initYear = new Array();

	for(var i = 2007; i <= currentYear; i++){
		years.push(i);
	}
	initYear.push(config);
	initYear.push(years);
	initYear.push(years);
	
	//var initYear = [["","","true"],
    //             ["2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"],
    //             ["2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"]];
                 
	var initMonth = [["","","true"],
                 ["1","2","3","4","5","6","7","8","9","10","11","12"],
                 ["1","2","3","4","5","6","7","8","9","10","11","12"]];
    var allStatus = [["","",true],["暫存","簽核中","簽核完成"],["SAVE","SIGNING","FINISH"]];
	var branch = "";
	var allItemTypes = vat.bean("allItemTypes");
	var allItemBrands = vat.bean("allItemBrands");
	if(document.forms[0]["#loginBrandCode"    ].value != "T2"){
		branch ="READONLY";
	}
	vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"預算 查詢作業", rows:[  
	 		{row_style:"", cols:[
	 		     {items:[{name:"#L.budgetYear", 			type:"LABEL", 	value:"預算年度"}]},	 
	 			 {items:[{name:"#F.budgetYear", 			type:"SELECT",  bind:"budgetYear", init:initYear,	size:15}], td:"colspan=3"},
	 		
	 		     {items:[{name:"#L.budgetMonth", 			type:"LABEL", 	value:"月份"}]},	 
	 			 {items:[{name:"#F.budgetMonth", 			type:"SELECT",  bind:"budgetMonth", init:initMonth,	size:15, mode:branch}]}//,
	 		
	 		     //{items:[{name:"#L.brandCode", 		type:"LABEL", 	value:"品牌"}]},
				 //{items:[{name:"#F.brandCode", 		type:"TEXT", 	bind:"brandCode", size:25}]}//,
				 
				 //{items:[{name:"#L.itemCName", 		type:"LABEL", 	value:"大類別"}]},
				 //{items:[{name:"#F.itemCName", 		type:"TEXT", 	bind:"itemCName", size:25}]}				  
			]},
			{row_style:"", cols:[
	 		     {items:[{name:"#L.itemType", 			type:"LABEL", 	value:"業種"}]},	 
	 			 {items:[{name:"#F.itemType", 			type:"SELECT",  bind:"itemType", init:allItemTypes,	size:15}]},
	 		
	 		     {items:[{name:"#L.itemBrandCode", 			type:"LABEL", 	value:"商品品牌"}]},	 
	 			 {items:[{name:"#F.itemBrandCode", 			type:"SELECT",  bind:"itemBrandCode", init:allItemBrands,	size:15}]},//,
	 		
	 		     {items:[{name:"#L.status", 		type:"LABEL", 	value:"狀態"}]},
				 {items:[{name:"#F.status", 		type:"SELECT", 	bind:"status", init:allStatus, size:25}]}//,
				 
				 //{items:[{name:"#L.itemCName", 		type:"LABEL", 	value:"大類別"}]},
				 //{items:[{name:"#F.itemCName", 		type:"TEXT", 	bind:"itemCName", size:25}]}				  
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function kweImDetail(){
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "checked"          , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"           , {type:"IDX"});
	vat.item.make(vnB_Detail, "itemTypeName"          , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"業種"      });
	vat.item.make(vnB_Detail, "budgetYear"           , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"預算年度"      });
	vat.item.make(vnB_Detail, "budgetMonth"           , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"月份"      });
	vat.item.make(vnB_Detail, "brandCode"       , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"品牌"      });
	//vat.item.make(vnB_Detail, "itemCName"          , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"大類別"      });
	vat.item.make(vnB_Detail, "headId"             , {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId"],
														pickAllService			: "selectAll",
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,		
														loadSuccessAfter    : "kwePageLoadSuccess()",						
														loadBeforeAjxService: "loadBeforeAjxService()",
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														blockId             : vnB_Detail
														});
	//vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function kwePageSaveMethod(){}					


function saveSuccessAfter(){
	 //alert("更新成功");
}

function kwePageLoadSuccess(){
	 if( vat.block.getGridObject(vnB_Detail).dataCount == vat.block.getGridObject(vnB_Detail).pageSize &&
	    vat.block.getGridObject(vnB_Detail).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}



function loadBeforeAjxService(){
    //alert("loadBeforeAjxService");	
    //vat.item.setValueByName("#F.orderTypeCode","IMV");
	//vat.item.setValueByName("#F.status"               ,"CLOSE");
	var processString = "process_object_name=fiBudgetHeadService&process_object_method_name=getAJAXSearchPageDataNew" + 
	                  "&budgetYear"  + "=" + vat.item.getValueByName("#F.budgetYear"  ) +     
	                  "&budgetMonth" + "=" + vat.item.getValueByName("#F.budgetMonth" ) +     
                      "&brandCode"   + "=" + document.forms[0]["#loginBrandCode"    ].value +
                      "&itemType" + "=" + vat.item.getValueByName("#F.itemType" ) + 
                      "&status" + "=" + vat.item.getValueByName("#F.status" ) + 
                      "&itemBrandCode" + "=" + vat.item.getValueByName("#F.itemBrandCode" ) ; 
	return processString;											
}



//	判斷是否要關閉LINE

function checkEnableLine() {
	return true;
}


//	取得SAVE要執行的JS FUNCTION

function saveBeforeAjxService() {
	var processString = "";
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=fiBudgetHeadService&process_object_method_name=saveSearchResult";
//		alert(processString); 
	}
	return processString;
}								


//	顯示合計的頁面

function showTotalCountPage() {
    //save line
    //alert("showTotalCountPage");
    vat.block.pageDataSave(0);
    afterSavePageProcess = "totalCount";	
}


function doSearch(){
    //alert("searchService");	
    //vat.bean().timeScope  = vat.block.getValue(vnB_Detail = 2, "timeScope");
    //alert("timeScope:"+vat.bean().timeScope);
   
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
			                   
	//vat.block.pageDataLoad(vnB_Detail = 2, vnCurrentPage = 1)
}


function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}

function doClosePicker(){
    //alert("doView");
	//vat.bean().vatBeanPicker.xxx = 1;

	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=fiBudgetHeadService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
    //alert("doView2");
				
}

