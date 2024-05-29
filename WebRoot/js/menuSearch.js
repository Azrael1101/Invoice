var vnB_Header 	= 0; vnB_Header_Id 	= "vatBlock_Head";
var vnB_Button 	= 1; vnB_Button_Id 	= "vatBlock_Button";
var vnB_Detail1 = 2; vnB_Detail1_Id = "vatDetailDiv1";
var vnB_Detail2 = 3; vnB_Detail2_Id = "vatDetailDiv2";
var vnB_Detail3 = 4; vnB_Detail3_Id = "vatDetailDiv3";
var vnB_Master 	= 5; vnB_Master_Id 	= "vatMasterDiv";

function kweImBlock(){
	kweInitial();
	kweHeader();
	kweButtonLine();
	createDetailDiv(vnB_Detail1, vnB_Detail1_Id);
	createDetailDiv(vnB_Detail2, vnB_Detail2_Id);
	createDetailDiv(vnB_Detail3, vnB_Detail3_Id);
	
}

function kweInitial(){
	initialVatBeanCode();
	
	vat.bean.init(
		function(){
			return "process_object_name=siMenuMainAction&process_object_method_name=performInitial"; 
		},{
			other: true
	});
}

function initialVatBeanCode(){
	vat.bean().vatBeanOther = {
		loginBrandCode  	: "T2",
        loginEmployeeCode  	: "T96085"
	};
}

function kweHeader(){
	var allMenus = vat.bean("allMenus");
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"MENU選單查詢作業", 
		rows:[{
				row_style:"", cols:[
				{items:[{ name:"#L.allMenus",   	 		type:"LABEL", value:"全部選單" }]},
				{items:[{ name:"#F.allMenus",   	 		type:"SELECT", bind:"allMenus", size:10, init:allMenus}]},
				{items:[{ name:"#L.nameSearchKeyWord",     	type:"LABEL", value:"全部選單關鍵字篩選" }]},
				{items:[{ name:"#F.nameSearchKeyWord",     	type:"TEXT", bind:"nameSearchKeyWord", size:20}]}
			]},
			{
				row_style:"", cols:[
				{items:[{ name:"#L.menuId1",   	 			type:"LABEL", value:"明細1選單代號" }]},
				{items:[{ name:"#F.menuId1",   	 			type:"TEXT", bind:"menuId1", size:10, mode:"READONLY"}]},
				{items:[{ name:"#L.menuName1",     	 		type:"LABEL", value:"明細1選單名稱" }]},
				{items:[{ name:"#F.menuName1",     	 		type:"TEXT", bind:"menuName1", size:20, mode:"READONLY"}]}
			]},
			{
				row_style:"", cols:[
				{items:[{ name:"#L.menuId2",   	 			type:"LABEL", value:"明細2選單代號" }]},
				{items:[{ name:"#F.menuId2",   	 			type:"TEXT", bind:"menuId2", size:10, mode:"READONLY"}]},
				{items:[{ name:"#L.menuName2",     	 		type:"LABEL", value:"明細2選單代號" }]},
				{items:[{ name:"#F.menuName2",     	 		type:"TEXT", bind:"menuName2", size:20, mode:"READONLY"}]}
			]},
			{
				row_style:"", cols:[
				{items:[{ name:"#L.menuId3",   	 			type:"LABEL", value:"明細3選單代號" }]},
				{items:[{ name:"#F.menuId3",   	 			type:"TEXT", bind:"menuId3", size:10, mode:"READONLY"}]},
				{items:[{ name:"#L.menuName3",     	 		type:"LABEL", value:"明細3選單代號" }]},
				{items:[{ name:"#F.menuName3",     	 		type:"TEXT", bind:"menuName3", size:20, mode:"READONLY"}]}
			]}
		],beginService:"",closeService:""			
	});
}

function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
				{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"}
//	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
//	 			{name:"#B.d1"         , type:"IMG"    ,value:"顯示明細1",   src:"./images/button_create.gif"  ,eClick:"showView('"+vnB_Detail1_Id+"')"},
//	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
//				{name:"#B.d2"         , type:"IMG"    ,value:"顯示明細2",   src:"./images/button_create.gif"  ,eClick:"showView('"+vnB_Detail2_Id+"')"},
//	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
//	 	 		{name:"#B.m"        , type:"IMG"    ,value:"顯示詳情",   src:"./images/button_exit.gif"  ,eClick:"showView('"+vnB_Master_Id+"')"}			
				],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function doSearch(){
	alert("doSearch");
	vat.item.setValueByName("#F.menuId1", vat.item.getValueByName("#F.allMenus"));
	vat.item.setValueByName("#F.menuId2", "");
	vat.item.setValueByName("#F.menuId3", "");
	vat.item.setValueByName("#F.menuName1", "");
	vat.item.setValueByName("#F.menuName2", "");
	vat.item.setValueByName("#F.menuName3", "");
	vat.block.pageRefresh(vnB_Detail1);
	vat.block.pageRefresh(vnB_Detail2);
	vat.block.pageRefresh(vnB_Detail3);
}

function createDetailDiv(divNum, idStr){
	var varCanGridModify = false;
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	
	vat.item.make(divNum, "", 			{type:"IDX" , desc:"", mode:"READONLY"});
	vat.item.make(divNum, "menuId",    	{type:"TEXT", desc:"選單代號", mode:"HIDDEN"});
	vat.item.make(divNum, "name",     	{type:"TEXT", desc:"選單名稱", mode:"READONLY"});
	vat.item.make(divNum, "showDetail", {type:"BUTTON", desc:"", value:"詳", eClick:"showDetail("+divNum+")"});
	vat.item.make(divNum, "extend" ,    {type:"BUTTON", desc:"", value:"➜", eClick:"extend("+divNum+")"});
	

	vat.block.pageLayout(divNum, {
								id: idStr,	
								pageSize: 10,
								canGridModify:varCanGridModify,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								loadBeforeAjxService: "loadBeforeAjxService("+divNum+")",
								loadSuccessAfter    : "", 
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "",
								indexType			: "AUTO"
								});
	document.getElementById(idStr).style.width="33.3%";
	document.getElementById(idStr).style.maxWidth="33.3";
	document.getElementById(idStr).style.styleFloat="left";
}

function loadBeforeAjxService(detailNum){
	alert(detailNum);
	var menuId;

	if(detailNum === vnB_Detail1){
		menuId = vat.item.getValueByName("#F.menuId1");
	}else if(detailNum === vnB_Detail2){
		menuId = vat.item.getValueByName("#F.menuId2");
	}else if(detailNum === vnB_Detail3){
		menuId = vat.item.getValueByName("#F.menuId3");
	}else{
		alert("無法載入此明細"+detailNum);
		return;
	}
	var processString = "process_object_name=siMenuMainService&process_object_method_name=getAJAXPageData"+
						"&menuId=" + menuId;
	return processString;
}

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

function saveBeforeAjxService(){
	var processString = "";
	if(checkEnableLine()){
		processString = "process_object_name=siMenuMainService&process_object_method_name=saveSearchResult";
	}
	return processString;
}

function extend(detailNum){
	var nItemLine = vat.item.getGridLine();
	var id = "vatF#B" + detailNum + "A#Y" + nItemLine;
	var menuId = document.getElementById(id+"#X2").value;
	if( menuId !== "" ){
		if(detailNum === vnB_Detail1){
			vat.item.setValueByName("#F.menuId2", menuId);
			vat.block.pageRefresh(vnB_Detail2);
		}else if(detailNum === vnB_Detail2){
			vat.item.setValueByName("#F.menuId3", menuId);
			vat.block.pageRefresh(vnB_Detail3);
		}else if(detailNum === vnB_Detail3){
			vat.item.setValueByName("#F.menuId1", vat.item.getValueByName("#F.menuId2"));
			vat.item.setValueByName("#F.menuId2", vat.item.getValueByName("#F.menuId3"));
			vat.item.setValueByName("#F.menuId3", menuId);
			vat.block.pageRefresh(vnB_Detail1);
			vat.block.pageRefresh(vnB_Detail2);
			vat.block.pageRefresh(vnB_Detail3);
		}
	}else{
		alert("無法展開");
		return;	
	}	
}


