/*** 
 *	檔案: posControl.js
 *	說明：pos機商品鎖定維護作業
 *	修改：Kenny
 *  <pre>
 *  	Created by Kenny
 *  	All rights reserved.
 *  </pre>
 */



vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;
var vnB_Detail2 = 3;
var vnB_Detail3 = 4;
var vnB_Detail4 = 5;
function outlineBlock(){
  	formDataInitial();
	kweButtonLine();
  	headerInitial();
  	    if (typeof vat.tabm != 'undefined') {
        vat.tabm.createTab(0, "vatTabSpan", "H", "float");
        vat.tabm.createButton(0, "xTab1", "已鎖定商品", "vatDetailDiv", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", true, "");
        vat.tabm.createButton(0, "xTab2", "鎖中類", "vatDetailDiv2", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", true, "");
		vat.tabm.createButton(0, "xTab3", "鎖品牌", "vatDetailDiv3", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", true, "");        
		vat.tabm.createButton(0, "xTab4", "鎖商品", "vatDetailDiv4", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", true, "");
    }
	detailInitial();
	detailInitial2();
	detailInitial3();
	detailInitial4();
	vat.tabm.displayToggle(0, "xTab2", false, false, false);
    vat.tabm.displayToggle(0, "xTab3", false, false, false);
    vat.tabm.displayToggle(0, "xTab4", false, false, false);
}

function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = 
		{
			loginBrandCode		:	document.forms[0]["#loginBrandCode" ].value,
			loginEmployeeCode	:	document.forms[0]["#loginEmployeeCode" ].value
    	}; 	    
		vat.bean.init(function(){
			return "process_object_name=machineSaleAction&process_object_method_name=performInitial"; 
		},{other: true});
		vat.item.bindAll();
	}
}

function kweButtonLine(){
	vat.block.create(vnB_Button, {id: "vatBlock_Button", generate: true,	
	title:"", rows:[	 
	{row_style:"", cols:[
	 	{items:[
	 		 	{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 		 	{name:"SPACE"       	, type:"LABEL"  ,value:"　"},	 	        
	 	 		{name:"#B.e xit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},	 	 		
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"},
	 			{name:"#B.lkItem"      , type:"BUTTON"    ,value:"鎖定商品", eClick:'doLockSubmit()'},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.unlockItem"      , type:"BUTTON"    ,value:"解鎖商品", eClick:'doLockSubmit()'}
	 	 		],
	 				td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
	 			]}
				//{name:"#B.message"  	, type:"IMG"    ,value:"訊息提示"	,   src:"./images/button_message_prompt.gif", eClick:'showMessage()'}
	 	],
		beginService:"",
		closeService:""			
	});
			vat.item.setStyleByName("#B.lkItem","display","none");
}

function headerInitial(){ 	
	var LKType =        [["1-查詢已鎖定商品","D"], ["1-查詢已鎖定商品", "2-查詢未鎖定商品"], ["D", "A"]];
	var category = vat.bean("category01");
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"POS機商品鎖定維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.lockType"			, type:"LABEL", value:"功能"}]},
				{items:[{name:"#F.lockType"			, type:"SELECT", bind:"lockType",init:LKType,eChange:function(){changeFunction();}}]},
				{items:[{name:"#L.category01"			, type:"LABEL"	, value:"大類"}]},	
				{items:[{name:"#F.category01"			, type:"SELECT"	, bind:"category01",init:category ,eChange:function(){changeCategory("01","02");}}]},
	 			{items:[{name:"#L.category02"			, type:"LABEL"	, value:"中類"}]},	
				{items:[{name:"#F.category02"			, type:"SELECT"	, bind:"category02",eChange:function(){changeItemBrand();} }]},
			    {items:[{name:"#L.ItemBrand"			, type:"LABEL", value:"商品品牌"}]},
				{items:[{name:"#F.ItemBrand"			, type:"SELECT", bind:"itemBrand"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.posMachineCode"			, type:"LABEL", value:"機台號碼"}]},
				{items:[{name:"#F.posMachineCode"			, type:"SELECT", bind:"posMachineCode"}]},
				{items:[{name:"#L.ItemCode"			, type:"LABEL", value:"品號"}]},
				{items:[{name:"#F.ItemCode"			, type:"TEXT", bind:"itemCode"}]}
			]}
		], 	
		beginService:"",
		closeService:function(){closeHeader();}	
	});	  	
}

function closeHeader(){
vat.ajax.XHRequest({ 
	post:"process_object_name=posDUService"+
          		"&process_object_method_name=findDownloadCommon"+
          		"&brandCode=" + document.forms[0]["#loginBrandCode" ].value,
          asyn:false,                      
	find: function change(oXHR){
		vat.item.SelectBind(eval(vat.ajax.getValue("allBuShopMachine"	, oXHR.responseText)),{ itemName : "#F.posMachineCode" });
		vat.item.bindAll();
		}
	});
}



function detailInitial(){
  	
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    var vbSelectionType = "CHECK";
	if(vatPickerId != null && vatPickerId != ""){
         vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
         vbSelectionType = "CHECK";
    }else{
         vbSelectionType = "NONE";
    }
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "checked"         , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "category01"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"大類"      });
	vat.item.make(vnB_Detail, "category02"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"中類"      });
	vat.item.make(vnB_Detail, "itemBrand"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"品牌"      });
	vat.item.make(vnB_Detail, "itemCode"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"品號"      });
	vat.item.make(vnB_Detail, "posMachineCode"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"機台號碼"      });
	vat.item.make(vnB_Detail, "lastUpdateBy", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail, "lastUpdateDate"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail, "category01"      	, {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["category01","classId","itemBrand","itemCode","machineCode"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete        : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}

function detailInitial2(){
  	
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    var vbSelectionType = "CHECK";
	if(vatPickerId != null && vatPickerId != ""){
         vat.item.make(vnB_Detail2, "checkbox" , {type:"XBOX"});
         vbSelectionType = "CHECK";
    }else{
         vbSelectionType = "NONE";
    }
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail2, "checked2"         , {type:"XBOX"});
    vat.item.make(vnB_Detail2, "indexNo2"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail2, "category01_2"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"大類"      });
	vat.item.make(vnB_Detail2, "category02_2"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"中類"      });
	vat.item.make(vnB_Detail2, "lastUpdateBy2", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail2, "lastUpdateDate2"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail2, "id.categoryCode"      	, {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail2, {
														id                  : "vatDetailDiv2",
														pageSize            : 10,
														searchKey           : ["id.categoryCode","parentCategoryCode"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete        : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}

function detailInitial3(){
  	
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    var vbSelectionType = "CHECK";
	if(vatPickerId != null && vatPickerId != ""){
         vat.item.make(vnB_Detail3, "checkbox" , {type:"XBOX"});
         vbSelectionType = "CHECK";
    }else{
         vbSelectionType = "NONE";
    }
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail3, "checked3"         , {type:"XBOX"});
    vat.item.make(vnB_Detail3, "indexNo3"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail3, "category01_3"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"大類"      });
	vat.item.make(vnB_Detail3, "category02_3"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"中類"      });
	vat.item.make(vnB_Detail3, "itemBrand3"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"品牌"      });
	vat.item.make(vnB_Detail3, "itemBrand"      	, {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail3, {
														id                  : "vatDetailDiv3",
														pageSize            : 10,
														searchKey           : ["itemBrand","category01","category02"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete        : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}

function detailInitial4(){
  	
  	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
    var vbSelectionType = "CHECK";
	if(vatPickerId != null && vatPickerId != ""){
         vat.item.make(vnB_Detail4, "checkbox" , {type:"XBOX"});
         vbSelectionType = "CHECK";
    }else{
         vbSelectionType = "NONE";
    }
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail4, "checked4"         , {type:"XBOX"});
    vat.item.make(vnB_Detail4, "indexNo4"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail4, "category01_4"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"大類"      });
	vat.item.make(vnB_Detail4, "category02_4"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"中類"      });
	vat.item.make(vnB_Detail4, "itemBrand4"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"品牌"      });
	vat.item.make(vnB_Detail4, "itemCode4"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"品號"      });
	vat.item.make(vnB_Detail4, "lastUpdateBy4", {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail4, "lastUpdateDate4"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	vat.item.make(vnB_Detail4, "itemCode"      	, {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail4, {
														id                  : "vatDetailDiv4",
														pageSize            : 10,
														searchKey           : ["itemCode","itemBrand","category02","category01"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete        : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : ""
														});

}
function saveBeforeAjxService() {
	var lockType = vat.item.getValueByName("#F.lockType");
	var category01 = vat.item.getValueByName("#F.category01");
	var category02 = vat.item.getValueByName("#F.category02");
	var itemBrand = vat.item.getValueByName("#F.itemBrand");
	var itemCode = vat.item.getValueByName("#F.itemCode");
	var processString = "";
	if(lockType == "A"){
		if(category01 != ""&&category02 == ""&&itemBrand == ""&&itemCode == ""){
			processString = "process_object_name=machineSaleService&process_object_method_name=saveCategory02Result";
	    }else if(category01 != ""&&category02 != ""&&itemBrand == ""&&itemCode == ""){
			processString = "process_object_name=machineSaleService&process_object_method_name=saveCategoryResult";
	    }else if(category01 != ""&&category02 != ""&&itemBrand != ""&&itemCode == ""){
			processString = "process_object_name=machineSaleService&process_object_method_name=saveItemBrandResult";
	    }else if(itemCode != ""){
			processString = "process_object_name=machineSaleService&process_object_method_name=saveItemCodeResult";
        }
	}else if(lockType == "D"){
			processString = "process_object_name=machineSaleService&process_object_method_name=saveLockedItemResult";
	}
	return processString;
}

function loadBeforeAjxService(){
	
	var func = vat.item.getValueByName("#F.lockType");
	var processString = "";

	if(func == "D"){
		processString = "process_object_name=machineSaleService&process_object_method_name=getLockedSearch";
	}else if(func == "A"){
		processString = "process_object_name=machineSaleService&process_object_method_name=getUnlockedSearch";
	} 
					processString = processString + "&category01="+vat.item.getValueByName("#F.category01") +
										   "&category02="+vat.item.getValueByName("#F.category02") +
										   "&itemBrand="+vat.item.getValueByName("#F.itemBrand") +
										   "&posMachineCode="+vat.item.getValueByName("#F.posMachineCode") +
										   "&ItemCode="+vat.item.getValueByName("#F.itemCode") +
										   "&brandCode="+vat.bean().vatBeanOther.loginBrandCode;
										                                               
	return processString;											
}

function loadSuccessAfter(){
	var lockType = vat.item.getValueByName("#F.lockType");
	var category01 = vat.item.getValueByName("#F.category01");
	var category02 = vat.item.getValueByName("#F.category02");
	var itemBrand = vat.item.getValueByName("#F.itemBrand");
	var itemCode = vat.item.getValueByName("#F.itemCode");
	var tab = 2;
	if(lockType == "A"){
		if(category01 != ""&&category02 == ""&&itemBrand == ""&&itemCode == ""){
	 		tab = 3;
	 		vat.tabm.displayToggle(0, "xTab2", true, false, false);
			vat.tabm.displayToggle(0, "xTab1", false, false, false);
			vat.tabm.displayToggle(0, "xTab3", false, false, false);
			vat.tabm.displayToggle(0, "xTab4", false, false, false);
	    }else if(category01 != ""&&category02 != ""&&itemBrand == ""&&itemCode == ""){
			tab = 4;
			vat.tabm.displayToggle(0, "xTab3", true, false, false);
			vat.tabm.displayToggle(0, "xTab1", false, false, false);
			vat.tabm.displayToggle(0, "xTab2", false, false, false);
			vat.tabm.displayToggle(0, "xTab4", false, false, false);
	    }else if(category01 != ""&&category02 != ""&&itemBrand != ""&&itemCode == ""){
			tab = 5;
			vat.tabm.displayToggle(0, "xTab4", true , false, false);
			vat.tabm.displayToggle(0, "xTab1", false, false, false);
			vat.tabm.displayToggle(0, "xTab2", false, false, false);
			vat.tabm.displayToggle(0, "xTab3", false, false, false);
	    }else if(category01 != ""&&category02 != ""&&itemBrand != ""&&itemCode != ""){
	   		tab = 5;
	   		vat.tabm.displayToggle(0, "xTab4", true , false, false);
	   		vat.tabm.displayToggle(0, "xTab1", false, false, false);
			vat.tabm.displayToggle(0, "xTab2", false, false, false);
			vat.tabm.displayToggle(0, "xTab3", false, false, false);
	    }
	    else if(category01 == ""&&category02 == ""&&itemBrand == ""&&itemCode != "")
	    {
            tab = 5;
            vat.tabm.displayToggle(0, "xTab4", true , false, false);
            vat.tabm.displayToggle(0, "xTab1", false, false, false);
            vat.tabm.displayToggle(0, "xTab2", false, false, false);
            vat.tabm.displayToggle(0, "xTab3", false, false, false);
	    }
	}else{
			tab=2;
	   		vat.tabm.displayToggle(0, "xTab1", true , false, false);
			vat.tabm.displayToggle(0, "xTab2", false , false, false);
			vat.tabm.displayToggle(0, "xTab3", false , false, false);
			vat.tabm.displayToggle(0, "xTab4", false , false, false);
	}
	if( vat.block.getGridObject(tab).dataCount == vat.block.getGridObject(tab).pageSize &&
	    vat.block.getGridObject(tab).lastIndex == 1){
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}
}

function doSearch(){
	var category01 = vat.item.getValueByName("#F.category01");
	var lockItem = vat.item.getValueByName("#F.lockType");
	//if(lockItem == "A"){
	//	if(category01 == ""){
	//		alert("請選擇大類");
	//		return;
	//	}
	//}
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {
			                     	var lockType = vat.item.getValueByName("#F.lockType");
									var category01 = vat.item.getValueByName("#F.category01");
									var category02 = vat.item.getValueByName("#F.category02");
									var itemBrand = vat.item.getValueByName("#F.itemBrand");
									var itemCode = vat.item.getValueByName("#F.itemCode");
									if(lockType == "A"){
										if(category01 != ""&&category02 == ""&&itemBrand == ""&&itemCode == ""){
											 vat.block.pageDataLoad(vnB_Detail2 , vnCurrentPage = 1);
									    }else if(category01 != ""&&category02 != ""&&itemBrand == ""&&itemCode == ""){
											 vat.block.pageDataLoad(vnB_Detail3 , vnCurrentPage = 1);
									    }else if(category01 != ""&&category02 != ""&&itemBrand != ""&&itemCode == ""){
											 vat.block.pageDataLoad(vnB_Detail4 , vnCurrentPage = 1);
									    }else if(category01 != ""&&category02 != ""&&itemBrand != ""&&itemCode != ""){
											 vat.block.pageDataLoad(vnB_Detail4 , vnCurrentPage = 1);
									    }else if(category01 == ""&&category02 == ""&&itemBrand == ""&&itemCode != ""){
									         vat.block.pageDataLoad(vnB_Detail4 , vnCurrentPage = 1);  
									    }
								  }else{
								  		vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);
								  }
								  }
			                    });
}
function changeFunction(){
	var func = vat.item.getValueByName("#F.lockType");
	if(func == "D"){
		vat.item.setStyleByName("#B.lkItem","display","none");
		vat.item.setStyleByName("#B.unlockItem","display","");
		vat.item.setValueByName("#F.category01","");
		vat.item.setValueByName("#F.category02","");
		vat.item.setValueByName("#F.itemBrand","");
		vat.item.setValueByName("#F.itemCode","");
		vat.item.setValueByName("#F.posMachineCode","");
	}else if(func == "A"){
		vat.item.setStyleByName("#B.lkItem","display","");
		vat.item.setStyleByName("#B.unlockItem","display","none");
		vat.item.setValueByName("#F.category01","");
		vat.item.setValueByName("#F.category02","");
		vat.item.setValueByName("#F.itemBrand","");
		vat.item.setValueByName("#F.itemCode","");
		vat.item.setValueByName("#F.posMachineCode","");
	}
}


function changeCategory(parentCategoryType, toggleCategoryType){
	var parentCategory = vat.item.getValueByName("#F.category"+parentCategoryType);
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
 	vat.item.setValueByName("#F.itemBrand","");
	vat.item.setValueByName("#F.itemCode","");
	vat.item.setValueByName("#F.posMachineCode","");
    vat.ajax.XHRequest(
    {
        post:"process_object_name=imItemCategoryService"+
                 "&process_object_method_name=getAJAXCategory"+
                 "&category=" + parentCategory +
                 "&categoryType=CATEGORY" + toggleCategoryType +
                 "&brandCode=" + brandCode,
        find: function changeRequestSuccess(oXHR){ 
	var allCategory = eval(vat.ajax.getValue("allCategory", oXHR.responseText));
        		allCategory[0][0] = "#F.category"+toggleCategoryType;
	vat.item.SelectBind(allCategory); 
        }   
    });
}
function doLockSubmit(){
	var lockType = vat.item.getValueByName("#F.lockType");
	var category01 = vat.item.getValueByName("#F.category01");
	var category02 = vat.item.getValueByName("#F.category02");
	var itemBrand = vat.item.getValueByName("#F.itemBrand");
	var itemCode = vat.item.getValueByName("#F.itemCode");

	//if(lockType == "A"){
		//if(category01 == ""){
		//	alert("請選擇大類");
		//	return;
		//}
	//}
		if(lockType == "A"){
			if(category01 != ""&&category02 == ""&&itemBrand == ""&&itemCode == ""){
				vat.block.pageSearch(vnB_Detail2, {
		    		funcSuccess : 
		    		    function(){
		    		      vat.block.submit(
		    		                     function(){ return "process_object_name=machineSaleAction&process_object_method_name=performLockItem";
		    		                               }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:vnB_Detail2} );
		    			}}); 
		    }else if(category01 != ""&&category02 != ""&&itemBrand == ""&&itemCode == ""){
				vat.block.pageSearch(vnB_Detail3, {
		    		funcSuccess : 
		    		    function(){
		    		      vat.block.submit(
		    		                     function(){ return "process_object_name=machineSaleAction&process_object_method_name=performLockItem";
		    		                               }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:vnB_Detail3} );
		    			}}); 
		    }else if(category01 != ""&&category02 != ""&&itemBrand != ""&&itemCode == ""){
				vat.block.pageSearch(vnB_Detail4, {
		    		funcSuccess : 
		    		    function(){
		    		      vat.block.submit(
		    		                     function(){ return "process_object_name=machineSaleAction&process_object_method_name=performLockItem";
		    		                               }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:vnB_Detail4} );
		    			}}); 
		    }else if(category01 != ""&&category02 != ""&&itemBrand != ""&&itemCode != ""){
				vat.block.pageSearch(vnB_Detail4, {
		    		funcSuccess : 
		    		    function(){
		    		      vat.block.submit(
		    		                     function(){ return "process_object_name=machineSaleAction&process_object_method_name=performLockItem";
		    		                               }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:vnB_Detail4} );
		    			}}); 
		    }else if(category01 == ""&&category02 == ""&&itemBrand == ""&&itemCode != ""){
                vat.block.pageSearch(vnB_Detail4, {
                    funcSuccess : 
                        function(){
                          vat.block.submit(
                                         function(){ return "process_object_name=machineSaleAction&process_object_method_name=performLockItem";
                                                   }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:vnB_Detail4} );
                        }}); 
            }
		    
	}else if(lockType = "D"){
					vat.block.pageSearch(vnB_Detail, {
		    		funcSuccess : 
		    		    function(){
		    		      vat.block.submit(
		    		                     function(){ return "process_object_name=machineSaleAction&process_object_method_name=performLockItem";
		    		                               }, { bind:true, link:false, other:true, picker:true, isPicker:true, blockId:vnB_Detail} );
		    			}});
	}
}
function changeItemBrand(){
	var category = vat.item.getValueByName("#F.category02");
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	vat.item.setValueByName("#F.itemCode","");
	vat.item.setValueByName("#F.posMachineCode","");
	vat.ajax.XHRequest(
	    {
	        post:"process_object_name=machineSaleService"+
	                 "&process_object_method_name=getAJAXItemBrand"+
	                 "&category=" + category +
	                 "&brandCode=" + brandCode,
	        find: function changeRequestSuccess(oXHR){ 
		var itemBrandList = eval(vat.ajax.getValue("itemBrandList", oXHR.responseText));
	        		itemBrandList[0][0] = "#F.ItemBrand";
		vat.item.SelectBind(itemBrandList); 
	        }   
	    });
}