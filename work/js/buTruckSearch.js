
var vnB_Detail = 2;
var vnB_Header = 1;
function outlineBlock(){

  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();

}

// 搜尋初始化
function searchInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = {
			loginBrandCode : document.forms[0]["#loginBrandCode"].value,   	
			loginEmployeeCode : document.forms[0]["#loginEmployeeCode"].value,	
			vatPickerId : document.forms[0]["#vatPickerId"].value
		};
	}
}


// 可搜尋的欄位
function headerInitial(){
    var allStatus = [["","",true],
                 ["暫存","簽核完成"],
                 ["SAVE","FINISH"]];             
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"保卡資料查詢作業", rows:[
			{row_style:"", cols:[
	 			{items:[{name:"#L.headId",		type:"LABEL", 	value:"單號"}]},
				{items:[{name:"#F.headId",		type:"TEXT",	size:20, maxLen:25},
						{name:"#L.status"     , type:"LABEL"  , value:" 狀態 " },
						{name:"#F.status"     , type:"SELECT" ,  bind:"status", size:12, init:allStatus}]}
			]},  
	 		{row_style:"", cols:[
	 			{items:[{name:"#L.truckCode",		type:"LABEL", 	value:"保卡代碼"}]},
				{items:[{name:"#F.truckCode",		type:"TEXT",	size:20,maxLen:25}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.truckDriver",	type:"LABEL", 	value:"司機" }]},
				{items:[{name:"#F.truckDriver", 	type:"TEXT", 	size:20 ,maxLen:25}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.freightName",	type:"LABEL", 	value:"貨運公司名稱"}]},
			 	{items:[{name:"#F.freightName", 	type:"TEXT", 	size:20,maxLen:25}]}
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
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"doClear()"},
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

function detailInitial(){
  
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "checked"         , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"  ,                     desc:"序號"      });
    vat.item.make(vnB_Detail, "headId"			, {type:"TEXT" , size:9, maxLen:10, mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "truckCode"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"保卡代碼"      });
	vat.item.make(vnB_Detail, "truckDriver" 	, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"司機"      });
	vat.item.make(vnB_Detail, "freightName"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"貨運公司名稱"      });
	vat.item.make(vnB_Detail, "statusName" 		    , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "lastUpDatedByName"	, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新人員"      });
	vat.item.make(vnB_Detail, "lastUpDateDate"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"更新日期"   });
	//vat.item.make(vnB_Detail, "countryCode"      	, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});

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
function loadBeforeAjxService(){
   // alert("loadBeforeAjxService");	
   //var nItemLine 			 = vat.item.getGridLine();
	var processString = "process_object_name=buTruckService&process_object_method_name=getAJAXBuTruckSearchPageData" + 
	                 	"&truckCode="		+ vat.item.getValueByName("#F.truckCode")+
						"&truckDriver="		+ vat.item.getValueByName("#F.truckDriver")+
						"&headId="			+ vat.item.getValueByName("#F.headId")+
						"&status="			+ vat.item.getValueByName("#F.status")+
             			"&freightName="		+ vat.item.getValueByName("#F.freightName");

                                                                            
	return processString;											
}

function saveSuccessAfter(){

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
		processString = "process_object_name=buTruckService&process_object_method_name=saveBuTruckSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=buTruckService&process_object_method_name=getSearchSelection";
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
function doClear(){
	vat.item.setValueByName("#F.truckCode"        ,"");
	vat.item.setValueByName("#F.truckDriver"      ,"");
	vat.item.setValueByName("#F.freightName"	  ,"");
	vat.item.setValueByName("#F.headId"			  ,"");

}