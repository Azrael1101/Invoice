var vnB_Header = 0; vnB_Header_Id = "vatBlock_Head";
var vnB_Button = 1; vnB_Button_Id = "vatBlock_Button";
var vnB_Detail1 = 2; vnB_Detail1_Id = "vatDetailDiv1";
var vnB_Detail2 = 3; vnB_Detail2_Id = "vatDetailDiv2";
var vnB_Master = 4; vnB_Master_Id = "vatMasterDiv";

var NO_FOCUS_CODE = -1;	

var currItemLine = 0;

//0.控制目前是否顯示(true=顯示,false=不顯示);1.明細編號;2.明細ID;3.當前聚焦資訊(物件)
var  displayControl = [[true, true, false], 
					  [vnB_Detail1, vnB_Detail2, vnB_Master],
					  [vnB_Detail1_Id, vnB_Detail2_Id, vnB_Master_Id],
					  [createFocusInfoObj(1,NO_FOCUS_CODE), createFocusInfoObj(1,NO_FOCUS_CODE)]];
		

//聚焦背景色碼	
var focusBackgroundColor = "#00E3E3";

//聚焦文字色碼
var focusFontColor = "#FFFFFF";
			
var pathArr = [];
var btnIndex = 0;

//建立路徑按鈕
function createPathButton(buttonName){
	var btn = document.createElement("button");
	btn.name = btnIndex++;
	btn.style.marginRight="15px";
	btn.style.backgroundColor="#555555";
	btn.style.color="white";
	btn.style.border="2px solid #555555"
	btn.innerText = buttonName;
	return btn;
}

//增加一個按鈕到路徑Bar
function addButtonToBar(buttonObj){
	document.getElementById("pathBtnBar").appendChild(buttonObj);
}

//聚焦物件
function createFocusInfoObj(focusPage, focusCol){
	var obj = {};
	obj.focusPage = focusPage;
	obj.focusCol = focusCol;
	//重置預設狀態
	obj.resetObj = function reset(){
		obj.focusPage = 1;
		obj.focusCol = NO_FOCUS_CODE;
	}
	return obj;
}

//建立一個節點物件
function createNode(level, node){
//	alert(level, node);
	var obj = {};
	obj.level = level;
	obj.node = node;
	return obj;
}

//更新路徑陣列(若傳入的階層，未出現於陣列中，會在陣列最後加入一個新的階層; 若有出現，則更新節點名稱，並捨去後面的階層)
function updatePathArr(vlevel, vnode){
	var length = pathArr.length;
	for(var x = 0 ; x < length ; x++ ){
		if( vlevel === pathArr[x].level ){
			pathArr[x].node = vnode;
			document.getElementById("pathBtnBar").childNodes[x].innerText = vlevel + " - " + vnode;
			if( x !== length -1 ){
				pathArr.splice(x+1, length - x-1);
			}
			return;
		}
	}
	pathArr.push(createNode(vlevel,vnode));
	addButtonToBar(createPathButton(vlevel+" - "+vnode));
}

//重新設定路徑按鈕列
function resetPathButtonBar(){
	var pathBtnBar = document.getElementById("pathBtnBar");
	pathBtnBar.innerHTML = "";
	btnIndex = 0;
	var length = pathArr.length;
	for(var x = 0 ; x < length ; x++ ){
		addButtonToBar(createPathButton(pathArr[x].level + " - " + pathArr[x].node));
	}
	
}

//取得完整路徑(文字)
function getPath(){
	var path = "";
	for(var x = 0 ; x < pathArr.length ; x++){
		if(pathArr[x]){
			if(x === 0){
				path += pathArr[x].level + "-" + pathArr[x].node;
			}else{
				path += " > " + pathArr[x].level + "-" + pathArr[x].node;
			}
		}
	}
	vat.item.setValueByName("#F.path", path);
}

//點擊路徑按鈕，要執行的方法
function onClickPathBtn(clickStr){
	var clickNum = Number(clickStr);
	var length = pathArr.length;
	if(length == 0){
		alert("目前無按鈕");
	}else if(0 < length && 0 <= clickNum){
//		clickNum;
//		alert("clickNum = " + clickNum + "; length = " + length);
		if(clickNum !== length - 1){
			pathArr.splice(clickNum+1, length - clickNum-1);
			resetPathButtonBar();
			getPath();
			
			vat.item.setValueByName("#F.categoryLevelCode", pathArr[clickNum].level);
			vat.item.setValueByName("#F.categoryNodeCode", pathArr[clickNum].node);
			vat.item.setValueByName("#F.categoryLevelCode2", "");
			vat.item.setValueByName("#F.categoryNodeCode2", "");
			vat.block.pageRefresh(vnB_Detail1);
			vat.block.pageRefresh(vnB_Detail2);
			
		}	
	}
}

//傳入明細代號,找出當前聚焦的頁數
function findFocusPage(detialNum){
	var length = displayControl.length;
	var focusPage;
	for(var i = 0 ; i < length ; i++){
		if(displayControl[1][i] === detialNum){
			focusPage = displayControl[3][i].focusPage;
			break;
		}
	}
	return focusPage;
}

//傳入明細代號,找出當前聚焦的列數
function findFocusCol(detialNum){
	var length = displayControl.length;
	var focusCol;
	for(var i = 0 ; i < length ; i++){
		if(displayControl[1][i] === detialNum){
			focusCol = displayControl[3][i].focusCol;
			break;
		}
	}
	return focusCol;
}

//輸入明細的編號,取消目前的聚焦,但不移除陣列中聚焦的紀錄
function cancelDetialFocusCol(detialNum){
	//先寫死10頁
	for(var i = 1; i <= 10 ; i++){
		if(i%2 === 1){
			changeBackgroundColorById("vatFD{"+detialNum+"}TR#" + i, "#dedede");
		}else if(i%2 === 0){
			changeBackgroundColorById("vatFD{"+detialNum+"}TR#" + i, "#efefef");
		}
	}
}

//輸入明細的編號,想聚焦的頁數,想聚焦的列數
function changeDetialFocusCol(detialNum, focusColNum){
	var length = displayControl.length;
//	var focusInfoObj = createFocusInfoObj(focusPageNum, focusColNum);
	for(var i = 0 ; i < length ; i++){
		if(displayControl[1][i] === detialNum){
			if(displayControl[3][i].focusCol !== NO_FOCUS_CODE && displayControl[3][i].focusCol%2 === 1){
				changeBackgroundColorById("vatFD{"+detialNum+"}TR#" + displayControl[3][i].focusCol, "#dedede");
			}else if(displayControl[3][i].focusCol !== NO_FOCUS_CODE && displayControl[3][i].focusPage%2 === 0){
				changeBackgroundColorById("vatFD{"+detialNum+"}TR#" + displayControl[3][i].focusCol, "#efefef");
			}
			displayControl[3][i].focusCol = focusColNum;
			
			if(focusColNum !== NO_FOCUS_CODE ){
				changeBackgroundColorById("vatFD{"+detialNum+"}TR#" + focusColNum, focusBackgroundColor);
				break;
			}
			
		}
	}
}

//將指定id的標籤改變背景顏色(包含自己及所有內部子標籤)
function changeBackgroundColorById(idStr, colorCode){
	var stackArr = new Array(); //宣告一個陣列，將此id的標籤內部全部走訪一遍
	var targetElement = document.getElementById(idStr);
	targetElement.style.backgroundColor = colorCode;
	
	var childrenNode = targetElement.childNodes;
	stackArr.unshift(childrenNode.length);
//	alert(!!stackArr[0]);
	while(!!stackArr[0]){
		childrenNode[stackArr[0]-1].style.backgroundColor = colorCode; //td
		childrenNode[stackArr[0]-1].children[0].style.backgroundColor = colorCode; //input
		stackArr[0] = stackArr[0] - 1;
		if(stackArr[0] === 0){
			stackArr.pop();
		}
	}
}

function kweImBlock(){
	kweInitial();
	kweHeader();
	kweButtonLine();
	
	document.getElementById("pathBtnBar").attachEvent('onclick', function () {
		if(event.srcElement.tagName === "BUTTON"){
			var index = event.srcElement.name;
//			alert(index);
//			alert("按鈕名稱為" + event.srcElement.innerText);
			onClickPathBtn(index);
		}
	}, false);	
	
	
	createDetailDiv(vnB_Detail1, vnB_Detail1_Id);
	createDetailDiv(vnB_Detail2, vnB_Detail2_Id);
	
	document.getElementById(vnB_Detail1_Id).style.width="49.5%";
	document.getElementById(vnB_Detail1_Id).style.maxWidth="49.5%";
	document.getElementById(vnB_Detail1_Id).style.styleFloat="left";
	
	
	document.getElementById(vnB_Detail2_Id).style.width="49.5%";
	document.getElementById(vnB_Detail2_Id).style.maxWidth="49.5%";
	document.getElementById(vnB_Detail2_Id).style.styleFloat="left";
	
	kweMaster();
	
	document.getElementById(vnB_Master_Id).style.width="49.5%";
	document.getElementById(vnB_Master_Id).style.maxWidth="49.5%";
	document.getElementById(vnB_Master_Id).style.styleFloat="right";
	
	executeDisplayControl();
	
	
}

function kweInitial(){
	initialVatBeanCode();
	
	vat.bean.init(
		function(){
			return "process_object_name=imItemCategoryLevelAction&process_object_method_name=performInitial"; 
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
	
	var allCategoryLevelCodes = vat.bean("allCategoryLevelCodes");

	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"商品分類結構查詢作業", 
		rows:[{
				row_style:"", cols:[
				{items:[{ name:"#L.allCategoryLevelCodes",   	 	type:"LABEL", value:"階層選單" }]},
				{items:[{ name:"#F.allCategoryLevelCodes",   	 	type:"SELECT", bind:"allCategoryLevelCodes", size:10, init:allCategoryLevelCodes}]},
				{items:[{ name:"#L.categoryLevelCodeKeyWord",     	type:"LABEL", value:"階層關鍵字查詢" }]},
				{items:[{ name:"#F.categoryLevelCodeKeyWord",     	type:"TEXT", bind:"categoryLevelCodeKeyWord", size:20}]}
			]},
			{
				row_style:"", cols:[
				{items:[{ name:"#L.categoryLevelCode",   	 	type:"LABEL", value:"明細1階層" }]},
				{items:[{ name:"#F.categoryLevelCode",   	 	type:"TEXT", bind:"categoryLevelCode", size:10, mode:"READONLY"}]},
				{items:[{ name:"#L.categoryNodeCode",     	 	type:"LABEL", value:"明細1節點" }]},
				{items:[{ name:"#F.categoryNodeCode",     	 	type:"TEXT", bind:"categoryNodeCode", size:20, mode:"READONLY"}]}
			]},
			{
				row_style:"", cols:[
				{items:[{ name:"#L.categoryLevelCode2",   	 	type:"LABEL", value:"明細2階層" }]},
				{items:[{ name:"#F.categoryLevelCode2",   	 	type:"TEXT", bind:"categoryLevelCode2", size:10, mode:"READONLY"}]},
				{items:[{ name:"#L.categoryNodeCode2",     	 	type:"LABEL", value:"明細2節點" }]},
				{items:[{ name:"#F.categoryNodeCode2",     	 	type:"TEXT", bind:"categoryNodeCode2", size:20, mode:"READONLY"}]}
			]},
			{
				row_style:"", cols:[
				{items:[{ name:"#L.path",   	 	type:"LABEL", value:"路徑" }]},
				{items:[{ name:"#F.path",   	 	type:"TEXT", bind:"path", size:50, mode:"READONLY"}]},
				{items:[{ name:"#L.brandCode",   	 	type:"LABEL", value:"品牌" }]},
				{items:[{ name:"#F.brandCode",   	 	type:"TEXT", bind:"brandCode", size:10, mode:"READONLY"}]}
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
				{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
//	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.d1"         , type:"IMG"    ,value:"顯示明細1",   src:"./images/button_create.gif"  ,eClick:"showView('"+vnB_Detail1_Id+"')"},
//	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
				{name:"#B.d2"         , type:"IMG"    ,value:"顯示明細2",   src:"./images/button_create.gif"  ,eClick:"showView('"+vnB_Detail2_Id+"')"},
//	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.m"        , type:"IMG"    ,value:"顯示詳情",   src:"./images/button_exit.gif"  ,eClick:"showView('"+vnB_Master_Id+"')"}			
				],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]},
	 {row_style:"", cols:[
	 	{items:[
						
				],
	 			  td:"id='pathBtnBar' style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function doSearch(){
//	alert("doSearch");
	vat.item.setValueByName("#F.categoryLevelCode", vat.item.getValueByName("#F.allCategoryLevelCodes"));
	vat.item.setValueByName("#F.categoryNodeCode", "");
	vat.item.setValueByName("#F.categoryLevelCode2", "");
	vat.item.setValueByName("#F.categoryNodeCode2", "");
	displayControl[3][0].resetObj;
	displayControl[3][1].resetObj;
	vat.block.pageRefresh(vnB_Detail1);
	vat.block.pageRefresh(vnB_Detail2);
	pathArr = [];
	document.getElementById("pathBtnBar").innerHTML = "";
	
//	pathArr.push(createNode(vat.item.getValueByName("#F.categoryLevelCode"), vat.item.getValueByName("#F.categoryNodeCode")));
	updatePathArr(vat.item.getValueByName("#F.categoryLevelCode"), vat.item.getValueByName("#F.categoryNodeCode"));
	getPath();
	
	
//	changeDetialFocusCol(vnB_Detail1, NO_FOCUS_CODE);
//	changeDetialFocusCol(vnB_Detail2, NO_FOCUS_CODE);
}

function createDetailDiv(divNum, idStr){
	var varCanGridModify = true;
	var varCanGridDelete = false;
	var varCanGridAppend = false;
	
	vat.item.make(divNum, "", 	{type:"IDX" , desc:"項次", mode:"READONLY"});
	vat.item.make(divNum, "categoryLevelCode",    {type:"TEXT", desc:"階層代號", mode:"HIDDEN"});
	vat.item.make(divNum, "categoryNodeName",     {type:"TEXT", desc:"節點名稱", size:12, mode:"READONLY"});
	vat.item.make(divNum, "categoryNodeCode",     {type:"TEXT", desc:"節點代號", mode:"HIDDEN"});
	vat.item.make(divNum, "pickDetail"      ,     {type:"BUTTON", desc:"檢視", value:"檢視", eClick:"showDetail("+divNum+")"});
	vat.item.make(divNum, "enterChildLevel" ,     {type:"BUTTON", desc:"展開", value:"展開", eClick:"showChildLevel("+divNum+")"});
	

	vat.block.pageLayout(divNum, {
								id: idStr,	
								pageSize: 10,
								canGridModify:varCanGridModify,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
							    appendBeforeService : "",
							    appendAfterService  : "",
								loadBeforeAjxService: "loadBeforeAjxService("+divNum+")",
								loadSuccessAfter    : "loadSuccessAfter("+divNum+")",
								eventService        : "",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "",
								indexType			: "AUTO"
								});
	vat.block.pageDataLoad(divNum, vnCurrentPage = 1);
}

function loadBeforeAjxService(detial){
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var categoryLevelCode;
	var categoryNodeCode;
	
	if(detial === vnB_Detail1){
		categoryLevelCode = vat.item.getValueByName("#F.categoryLevelCode");
	 	categoryNodeCode = vat.item.getValueByName("#F.categoryNodeCode");		
	}else if(detial === vnB_Detail2){
		categoryLevelCode = vat.item.getValueByName("#F.categoryLevelCode2");
	 	categoryNodeCode = vat.item.getValueByName("#F.categoryNodeCode2");
	}
	
	
	
//	alert("loadBeforeAjxService..." + categoryLevelCode +";"+categoryNodeCode);
	
	var processString = "process_object_name=imItemCategoryLevelService&process_object_method_name=getAJAXPageData"+
						"&categoryLevelCode=" + categoryLevelCode +
						"&categoryNodeCode=" + categoryNodeCode + 
						"&brandCode=" + brandCode;
	return processString;
}

function loadSuccessAfter(detial){
	//檢查此頁面是否需要聚焦
	var currentDetialPage = Number(document.getElementById("vatFD{"+detial+"}pageThere").value);
	var focusDetialPage =  Number(findFocusPage(detial));
//	if(currItemLine !== 0){
//		if(detail === vnB_Detail1){
//			
//			
//			
//			changeDetialFocusCol(vnB_Detail1,nItemLine);
//			
//			
//		}else if(detail === vnB_Detail2){
//			
//			
//	//		changeDetialFocusCol(vnB_Detail1,nItemLine);
//			changeDetialFocusCol(vnB_Detail2,nItemLine);
//			
//			displayControl[3].shift();
//			displayControl[3].push(createFocusInfoObj(1,NO_FOCUS_CODE));
//			
//			
//		}
//	}
//	alert(currentDetialPage !== focusDetialPage);
	if(currentDetialPage !== focusDetialPage){
		cancelDetialFocusCol(detial);
	}else{
//		alert(detial+ ";"+findFocusCol(detial));
		cancelDetialFocusCol(detial);
		changeDetialFocusCol(detial, findFocusCol(detial));
	}
	
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
		processString = "process_object_name=imItemCategoryLevelService&process_object_method_name=saveImItemCategorySearchResult";
	}
	return processString;
}



function kweMaster(){
	vat.block.create(vnB_Master, {
		id: vnB_Master_Id, 
		table:"cellspacing='0' class='default' border='0' cellpadding='3'",	
		title:"節點屬性資訊",
			rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.categoryNodeNameDetail"    , type:"LABEL" , value:"節點名稱"}]},
				{items:[{name:"#F.categoryNodeNameDetail"    , type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.categoryNodeCodeDetail"    , type:"LABEL" , value:"節點代號"}]},
				{items:[{name:"#F.categoryNodeCodeDetail"    , type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.categoryLevelCodeDetail"	, type:"LABEL" , value:"階層代號"}]},
				{items:[{name:"#F.categoryLevelCodeDetail"	, type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.pCategoryNodeNameDetail"	, type:"LABEL" , value:"父節點名稱"}]},
				{items:[{name:"#F.pCategoryNodeNameDetail"	, type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.pCategoryNodeCodeDetail"	, type:"LABEL" , value:"父節點代號"}]},
				{items:[{name:"#F.pCategoryNodeCodeDetail"	, type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.pCategoryLevelNameDetail" , type:"LABEL" , value:"父階層名稱"}]},
				{items:[{name:"#F.pCategoryLevelNameDetail" , type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.cCategoryNodeNumDetail" 	, type:"LABEL" , value:"子節點數量"}]},
				{items:[{name:"#F.cCategoryNodeNumDetail" 	, type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.cCategoryLevelNameDetail"	, type:"LABEL" , value:"子階層名稱"}]},	 
				{items:[{name:"#F.cCategoryLevelNameDetail"	, type:"TEXT"  , dec:4 , size:12, mode:"READONLY"}]}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#B.enter", type:"IMG" , value:"查詢", src:"./images/button_find.gif", eClick:"find()"}], td:" colSpan=2 align=center"}
		 	]}
	 	 ],
			beginService:"",
			closeService:""			
		});
}



function showTotalCountPage(){
	alert("showTotalCountPage");
}


function showDetail(detail){
//	alert("showDetail");
	var nItemLine = vat.item.getGridLine();
	var id = "vatF#B" + detail + "A#Y" + nItemLine;
	var categoryLevelCode = document.getElementById(id+"#X2").value;
	var categoryNodeCode = document.getElementById(id+"#X4").value;
	var brandCode = vat.item.getValueByName("#F.brandCode");
	if(categoryLevelCode === "" || categoryNodeCode === ""){
		alert("無法檢視明細");
		return;
	}
//	alert(categoryLevelCode + categoryNodeCode);
	
	vat.ajax.XHRequest(
       {
           post:"process_object_name=imItemCategoryLevelService&process_object_method_name=getDetailByCategoryNodeCode"+
                "&categoryNodeCode=" + categoryNodeCode +
				"&categoryLevelCode=" + categoryLevelCode + 
				"&brandCode=" + brandCode,
           find: function change(oXHR){ 
					
				var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
				
				if(errorMsg !== ""){
					alert(errorMsg);
					
				}else{
//					alert("查詢詳細資料成功");
					var categoryNodeNameDetail = vat.ajax.getValue("categoryNodeNameDetail", oXHR.responseText);
					var categoryNodeCodeDetail = vat.ajax.getValue("categoryNodeCodeDetail", oXHR.responseText);
					var categoryLevelCodeDetail = vat.ajax.getValue("categoryLevelCodeDetail", oXHR.responseText);
					var pCategoryNodeNameDetail = vat.ajax.getValue("pCategoryNodeNameDetail", oXHR.responseText);
					var pCategoryNodeCodeDetail = vat.ajax.getValue("pCategoryNodeCodeDetail", oXHR.responseText);
					var pCategoryLevelNameDetail = vat.ajax.getValue("pCategoryLevelNameDetail", oXHR.responseText);
					var cCategoryNodeNumDetail = vat.ajax.getValue("cCategoryNodeNumDetail", oXHR.responseText);
					var cCategoryLevelNameDetail = vat.ajax.getValue("cCategoryLevelNameDetail", oXHR.responseText);
					vat.item.setValueByName("#F.categoryNodeNameDetail", categoryNodeNameDetail);
					vat.item.setValueByName("#F.categoryNodeCodeDetail", categoryNodeCodeDetail);
					vat.item.setValueByName("#F.categoryLevelCodeDetail", categoryLevelCodeDetail);
					vat.item.setValueByName("#F.pCategoryNodeNameDetail", pCategoryNodeNameDetail);
					vat.item.setValueByName("#F.pCategoryNodeCodeDetail", pCategoryNodeCodeDetail);
					vat.item.setValueByName("#F.pCategoryLevelNameDetail", pCategoryLevelNameDetail);
					vat.item.setValueByName("#F.cCategoryNodeNumDetail", cCategoryNodeNumDetail);
					vat.item.setValueByName("#F.cCategoryLevelNameDetail", cCategoryLevelNameDetail);
					
					for(var x = 0 ; x < displayControl[0].length ; x++ ){
						if(displayControl[1][x] === detail){
							displayControl[0][x] = true;
						}else{
							displayControl[0][x] = false;
						}
						
					}
					displayControl[0][displayControl[0].length-1] = true;
					executeDisplayControl();
				}
				
           }   
       });
}

function showChildLevel(detail){
//	alert("showChildLevel,detail=" +detail);
	//第n項展開，必須更新n+1
	var nItemLine = vat.item.getGridLine();
	currItemLine = nItemLine;
	var id = "vatF#B" + detail + "A#Y" + nItemLine;
	var categoryLevelCode = document.getElementById(id+"#X2").value;
	var categoryNodeCode = document.getElementById(id+"#X4").value;
	if(categoryLevelCode === "" || categoryNodeCode === ""){
		alert("無法展開");
		return;
	}
	if(detail === vnB_Detail1){
		vat.item.setValueByName("#F.categoryLevelCode2", categoryLevelCode);
		vat.item.setValueByName("#F.categoryNodeCode2", categoryNodeCode);
		
		displayControl[3][0] = createFocusInfoObj(Number(document.getElementById("vatFD{"+vnB_Detail1+"}pageThere").value),nItemLine);
		cancelDetialFocusCol(detail);
		changeDetialFocusCol(vnB_Detail1,nItemLine);
		
		vat.block.pageDataLoad(vnB_Detail2, vnCurrentPage = 1);
		
	}else if(detail === vnB_Detail2){
		vat.item.setValueByName("#F.categoryLevelCode", vat.item.getValueByName("#F.categoryLevelCode2"));
		vat.item.setValueByName("#F.categoryNodeCode", vat.item.getValueByName("#F.categoryNodeCode2"));
		vat.item.setValueByName("#F.categoryLevelCode2", categoryLevelCode);
		vat.item.setValueByName("#F.categoryNodeCode2", categoryNodeCode);
		
		
//		changeDetialFocusCol(vnB_Detail1,nItemLine);
		displayControl[3][1] = createFocusInfoObj(Number(document.getElementById("vatFD{"+vnB_Detail2+"}pageThere").value),nItemLine);
		displayControl[3].push(createFocusInfoObj(1,NO_FOCUS_CODE));
		displayControl[3].shift();
		
		
//		changeDetialFocusCol(vnB_Detail2,nItemLine);
		
		vat.block.pageDataLoad(vnB_Detail1, vnCurrentPage = Number(document.getElementById("vatFD{"+vnB_Detail2+"}pageThere").value));
		vat.block.pageDataLoad(vnB_Detail2, vnCurrentPage = 1);
		
	}
//	pathArr.push(createNode(categoryLevelCode, categoryNodeCode));
	updatePathArr(categoryLevelCode, categoryNodeCode);
	document.getElementById("pathBtn");
	getPath();
	
	for(var x = 0 ; x < displayControl[0].length ; x++ ){
		displayControl[0][x] = true;
	}
	displayControl[0][displayControl[0].length-1] = false;
	executeDisplayControl();
	
	
	
}

function showView(id){
	var display = document.getElementById(id).style.display;
	if(display === "" || display === "inline"){
		display = "none";
	}else if(display === "none"){
		display = "inline";
	}
	document.getElementById(id).style.display = display;
	
}

function executeDisplayControl(){
//	alert(displayControl[0].length);
	for(var x = 0 ; x < displayControl[0].length ; x++ ){
		if(displayControl[0][x]){
			document.getElementById(displayControl[2][x]).style.display = "inline";
		}else{
			document.getElementById(displayControl[2][x]).style.display = "none";
		}
	}
}

function getIdPreFix(){
	
}

function find(){
	alert("find");
}