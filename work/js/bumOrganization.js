/**
 * bumOrganization.js
 * 組織
 * 
 */
var vnB_Detail4 = 5; vnB_Detail4_Id = "vatDetailDiv4";
var vnB_Detail3 = 4; vnB_Detail3_Id = "vatDetailDiv3";
var vnB_Detail2 = 3; vnB_Detail2_Id = "vatDetailDiv2";
var vnB_Detail = 2; vnB_Detail_Id = "vatDetailDiv";
var vnB_Header = 1;
function kweImBlock(){
 	
  kweImSearchInitial();
  kweImHeader();
  kweButtonLine();

  createKweImDetail(vnB_Detail, vnB_Detail_Id);
  createKweImDetail(vnB_Detail2, vnB_Detail2_Id);
  createKweImDetail(vnB_Detail3, vnB_Detail3_Id);
//  kweImDetail();
//  kweImDetail2();
//  kweImDetail3()
  kweImDetail4()
}

// 搜尋初始化
function kweImSearchInitial(){ 
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode  		: "T2",
         loginEmployeeCode  	: "T96085",
         sysSno             	: ""
	    };
	    vat.bean.init(	
	  		function(){
				return "process_object_name=bumOrganizationAction&process_object_method_name=performInitial"; 
	    	},{								
	    		other: true
    	});
}

// 可搜尋的欄位
function kweImHeader(){ 
	var allBumOrganizationTree = vat.bean("allBumOrganizationTree");
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"組織", rows:[  
		 		{row_style:"", cols:[
					{items:[{name:"#L.organizationType", type:"LABEL"  , value:"組織類型"}]},
					{items:[{name:"#F.organizationType", type:"SELECT"   , size:20, init:allBumOrganizationTree}]},
				 	{items:[{name:"#L.organizationName", type:"LABEL"  , value:"組織名稱"}]},
					{items:[{name:"#F.organizationName", type:"TEXT"   , size:10}]},
					{items:[{name:"#L.ownNode", type:"LABEL", value:"點選單位名稱"}]},
					{items:[{name:"#F.ownNode", type:"TEXT",mode:"Hidden"},
							{name:"#F.businessUnitName", type:"TEXT",mode:"ReadOnly"}]}
					
				]},
				{row_style:"", cols:[
					{items:[{name:"#L.detailNode1", type:"LABEL"  , value:"組織明細節點1"}]},
					{items:[{name:"#F.detailNode1", type:"TEXT"   , size:20}]},
				 	{items:[{name:"#L.detailNode2", type:"LABEL"  , value:"組織明細節點2"}]},
					{items:[{name:"#F.detailNode2", type:"TEXT"   , size:10}]},
					{items:[{name:"#L.detailNode3", type:"LABEL"  , value:"組織明細節點3"}]},
					{items:[{name:"#F.detailNode3", type:"TEXT"   , size:10}]}
				]}
				
			], 	 
			beginService:"",
			closeService:""			
		});

}

function kweButtonLine(){
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
	 			{name:"#B.update"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function createKweImDetail(detailNum, detailId){
	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;

    vat.item.make(detailNum, "indexNo"          , {type:"IDX"  ,                     desc:"項次"      });
//	vat.item.make(detailNum, "ownNode"          , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"節點代號"});
//	vat.item.make(detailNum, "businessUnitType" , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"商業單位類別"});
	vat.item.make(detailNum, "sysSno" , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"單位代號"});
	vat.item.make(detailNum, "businessUnitName" , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"單位名稱"});
//	vat.item.make(detailNum, "ownNode"      	,{type:"ROWID"});
	vat.item.make(detailNum, "showDetail"		, {type:"BUTTON", view: "fixed", desc:"", value:"檢視", src:"images/button_advance_input.gif", eClick:"showDetail("+detailNum+")"});
	vat.item.make(detailNum, "chooseRight"      , {type:"BUTTON", view: "fixed", desc:"", value:"➜", src:"images/button_advance_input.gif", eClick:"chooseRight("+detailNum+")"});
	vat.block.pageLayout(detailNum, {
														id                  : detailId,
														pageSize            : 10,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService("+detailNum+")",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});
var detail = document.getElementById(detailId);
detail.style.width = "33.3%";
detail.style.maxWidth = "33.3%";
detail.style.styleFloat = "left";
detail.style.height = "380px";
}

function kweImDetail(){
  
  	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "indexNo"          , {type:"IDX"  ,                     desc:"項次"      });
	vat.item.make(vnB_Detail, "ownNode"          , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"節點代號"});
	vat.item.make(vnB_Detail, "businessUnitType" , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"商業單位類別"});
	vat.item.make(vnB_Detail, "businessUnitName" , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"商業單位名稱"});
	vat.item.make(vnB_Detail, "ownNode"      	 , {type:"ROWID"});
	vat.item.make(vnB_Detail, "chooseRight"      , {type:"BUTTON", view: "fixed", desc:"", value:"➜", src:"images/button_advance_input.gif", eClick:"chooseRight("+vnB_Detail+")"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail+")",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});
var detail = document.getElementById('vatDetailDiv');
detail.style.width = "33.3%";
detail.style.maxWidth = "33.3%";
detail.style.styleFloat = "left";
detail.style.height = "380px";
}

function kweImDetail2(){
  
  	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;

    vat.item.make(vnB_Detail2, "indexNo"          , {type:"IDX"  ,                     desc:"項次"      });
	vat.item.make(vnB_Detail2, "ownNode"          , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"節點代號"});
	vat.item.make(vnB_Detail2, "businessUnitType" , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"商業單位類別"});
	vat.item.make(vnB_Detail2, "businessUnitName" , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"商業單位名稱"});
	vat.item.make(vnB_Detail2, "ownNode" 		  , {type:"ROWID"});
	vat.item.make(vnB_Detail2, "chooseRight"      , {type:"BUTTON", view: "fixed", desc:"", value:"➜", src:"images/button_advance_input.gif", eClick:"chooseRight("+vnB_Detail2+")"});
	vat.block.pageLayout(vnB_Detail2, {
														id                  : "vatDetailDiv2",
														pageSize            : 10,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail2+")",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
var detail2 = document.getElementById('vatDetailDiv2');
detail2.style.width = "33.3%";
detail2.style.maxWidth = "33.3%";
detail2.style.styleFloat = "left";
detail2.style.height = "380px";
}

function kweImDetail3(){
  
  	var vbCanGridDelete = false;
  	var vbCanGridAppend = false;
  	var vbCanGridModify = true;

    vat.item.make(vnB_Detail3, "indexNo"          , {type:"IDX"  ,                     desc:"項次"      });
	vat.item.make(vnB_Detail3, "parentNode"       , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"節點代號"});
	vat.item.make(vnB_Detail3, "businessUnitType" , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"商業單位類別"});
	vat.item.make(vnB_Detail3, "businessUnitName" , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"商業單位名稱"});
	vat.item.make(vnB_Detail3, "businessUnitType" , {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail3, {
														id                  : "vatDetailDiv3",
														pageSize            : 10,
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail3+")",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});
var detail3 = document.getElementById('vatDetailDiv3');
detail3.style.width = "33.3%";
detail3.style.maxWidth = "33.3%";
detail3.style.styleFloat = "left";
detail3.style.height = "380px";
}
function kweImDetail4(){
		vat.block.create(vnB_Detail4, {
		id: "vatDetailDiv4", table:"cellspacing='1' class='default' border='0' cellpadding='3'",
		title:"商業單位內容",
			rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.businessUnitTypeDetail"    , type:"LABEL" , value:"商業單位類別"}]},
				{items:[{name:"#F.businessUnitTypeDetail"    , type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.sysModifierAmailDetail" , type:"LABEL" , value:"最後建檔人員"}]},
				{items:[{name:"#F.sysModifierAmailDetail" , type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
		 	{row_style:"", cols:[
				{items:[{name:"#L.businessUnitNameDetail"    , type:"LABEL" , value:"商業單位名稱"}]},	 
				{items:[{name:"#F.businessUnitNameDetail"    , type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]},
				{items:[{name:"#L.sysLastUpdateTimeDetail"	, type:"LABEL" , value:"最後建檔日期"}]},
				{items:[{name:"#F.sysLastUpdateTimeDetail"	, type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.memberTypeDetail", type:"LABEL" , value:"成員類別"}]},
				{items:[{name:"#F.memberTypeDetail", type:"NUMM"  , dec:4 , size:12, mode:"READONLY"}],td:" colSpan=3"}
		 	]}
			
	 	 ],
			beginService:"",
			closeService:""			
		});
		
var detail4 = document.getElementById('vatDetailDiv4');
detail4.style.width = "100%";
detail4.style.maxWidth = "100%";
detail4.style.styleFloat = "left";
detail4.style.height = "200px";
}




// 載入成功後
function loadSuccessAfter(){
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.update" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.update" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(detail){
   // alert("loadBeforeAjxService");
	var detailNode = "";	
	if(detail == vnB_Detail){
		detailNode = vat.item.getValueByName("#F.detailNode1");
	}else if(detail == vnB_Detail2){
		detailNode = vat.item.getValueByName("#F.detailNode2");
	}else if(detail == vnB_Detail3){
		detailNode = vat.item.getValueByName("#F.detailNode3");
	}
	var processString = "process_object_name=bumOrganizationService&process_object_method_name=getAJAXSearchPageData" + 
		                  	"&pSysSno" + "=" + detailNode;
	return processString;
}

function saveSuccessAfter(){
}

//向右展開功能-查詢
function chooseRight(line){

	var nItemLine = vat.item.getGridLine();
	var detailId = "vatF#B" + line + "A#Y" + nItemLine;	
	var vOwnNode = document.getElementById(detailId + "#X2").value;
    alert("選擇的節點 = " + vOwnNode);
    if("" !== vOwnNode){
    	if(line == vnB_Detail){
			vat.item.setValueByName("#F.detailNode2",vOwnNode);
			vat.block.pageRefresh(vnB_Detail2);
		}else if(line == vnB_Detail2){
			vat.item.setValueByName("#F.detailNode3",vOwnNode);
			vat.block.pageRefresh(vnB_Detail3);
		}else if(line == vnB_Detail3){
			vat.item.setValueByName("#F.detailNode1", vat.item.getValueByName("#F.detailNode2"));
			vat.item.setValueByName("#F.detailNode2", vat.item.getValueByName("#F.detailNode3"));
			vat.item.setValueByName("#F.detailNode3",vOwnNode);
			vat.block.pageRefresh(vnB_Detail);
			vat.block.pageRefresh(vnB_Detail2);
			vat.block.pageRefresh(vnB_Detail3);
		}
	}else{
		alert("請先查詢並選擇資料");
	}
	
	
//	vat.ajax.XHRequest(
//    {
//        post:"process_object_name=bumOrganizationService" +
//                    "&process_object_method_name=getAJAXSearchPageData2" +
//                     "&ownNode=" + vOwnNode,
//                                                  
//        find: function requestSuccess(oXHR){ 
//        	alert("成功!");
//            vat.item.setGridValueByName("parentNode", vnB_Detail2, vat.ajax.getValue("parentNode", oXHR.responseText));	
//		    vat.item.setGridValueByName("businessUnitType", vnB_Detail2, vat.ajax.getValue("businessUnitType", oXHR.responseText));
//		    vat.item.setGridValueByName("businessUnitName", vnB_Detail2, vat.ajax.getValue("businessUnitName", oXHR.responseText)); 
//        }
//    }); 
	
}

function openModifyPage(){
    var nItemLine = vat.item.getGridLine();
    
}



//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	//alert("saveBeforeAjxService");
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

// 查詢按下後
function doSearch(){
	var organizationType = vat.item.getValueByName("#F.organizationType");
	if( organizationType === "" ){
		alert("請先選擇組織類型");
		return;
	}
	vat.item.setValueByName("#F.detailNode1", organizationType);
	vat.block.pageRefresh(vnB_Detail);
	vat.block.pageRefresh(vnB_Detail2);
	vat.block.pageRefresh(vnB_Detail3);
}

function showDetail(detailNum){
	var nItemLine = vat.item.getGridLine();
	var detailId = "vatF#B" + detailNum + "A#Y" + nItemLine;
	var vOwnNode = document.getElementById(detailId + "#X2").value;
	
	if("" !== vOwnNode){
    	vat.ajax.XHRequest(
       {
           post:"process_object_name=bumOrganizationService&process_object_method_name=getDetailBySysSno"+
                "&sysSno=" + vOwnNode,
           find: function change(oXHR){ 
				var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
				if(errorMsg !== ""){
					alert(errorMsg);
				}else{
					alert("查詢詳細資料成功");
					var businessUnitNameDetail = vat.ajax.getValue("businessUnitNameDetail", oXHR.responseText);
					vat.item.setValueByName("#F.businessUnitNameDetail", businessUnitNameDetail);
				}
           }   
       });
	}else{
		alert("無法檢視,請先查詢並選擇資料");
	}
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
	vat.item.setValueByName("#F.lcNoStart", "");
    vat.item.setValueByName("#F.lcNoEnd", "");
    vat.item.setValueByName("#F.lcDateStart", "");
    vat.item.setValueByName("#F.lcDateEnd", "");
    vat.item.setValueByName("#F.supplierCode", "");
    vat.item.setValueByName("#F.addressBookId", "");
    vat.item.setValueByName("#F.supplierName", "");
    vat.item.setValueByName("#F.poNoStart", "");
    vat.item.setValueByName("#F.poNoEnd", "");
    vat.item.setValueByName("#F.openingBank", "");
    vat.item.setValueByName("#F.status", ""); 
}

