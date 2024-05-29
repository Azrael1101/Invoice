var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

var isDebugModel = false;

function kweImBlock(){
	kweInitial();
  	kweButtonLine();
  	kweHeader();
  	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");                                                                                                                                                                                                                                          
		vat.tabm.createButton(0 ,"xTab3","明細資料" ,"vatDetailDiv","images/tab_detail_data_dark.gif","images/tab_detail_data_light.gif", false , ""); 
  	}
  	kweDetail();
	document.getElementById("vatDetailDiv").firstChild.style.width="50%";
	doFormAccessControl();
}

function kweInitial(){
	initialVatBeanCode();
	
	vat.bean.init(
		function(){
			return "process_object_name=ommChannelAction&process_object_method_name=performInitial"; 
		},{
			other: true
	});
}

function initialVatBeanCode(){
	vat.bean().vatBeanOther = {
		loginBrandCode  	: "T2",
        loginEmployeeCode  	: "T96085",
        sysSno             	: "",
		channelType         : vChannelType,
		categoryType      	: vCategoryType
	};
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
	 			{name:"#B.new"         , type:"IMG"    ,value:"建立新資料",   src:"./images/button_create.gif"  ,eClick:'createRefreshForm()'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'}	 			
				],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function kweHeader(){

	var channelType =   [["", " ", true] , ["ERP", "POS", "EC", "APP"] , ["ERP", "POS", "EC", "APP"]]; 
	var categoryType =  [["", " ", true] , ["菸酒巧克力-T", "精品-E", "化妝品-C", "台產品-F", "3C影音圖書-D"] , ["T", "E", "C", "F", "D"]]; 
	
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"通道建立作業", 
		rows:[{
				row_style:"", cols:[
				{items:[{ name:"#L.orderTypeCode",   	 	type:"LABEL", value:"單別" }]},
				{items:[{ name:"#F.orderTypeCode",   	 	type:"TEXT", bind:"orderTypeCode", size:10, mode:"READONLY"}]},
				{items:[{ name:"#L.orderNo",     	 	 	type:"LABEL", value:"單號" }]},
				{items:[{ name:"#F.orderNo",     	 		type:"TEXT", bind:"orderNo", size:20, mode:"READONLY"},
						{ name:"#F.sysSno",     	 		type:"TEXT", bind:"sysSno", mode:"READONLY"}]},
				{items:[{ name:"#L.status",     	 	 	type:"LABEL", value:"狀態" }]},
				{items:[{ name:"#F.status",     	  	 	type:"TEXT", bind:"status", mode:"HIDDEN"},
						{ name:"#F.statusName",     	  	type:"TEXT", bind:"statusName", mode:"READONLY"},
						{ name:"#F.sysSign",     	  		type:"TEXT", bind:"sysSign", mode:"HIDDEN"}]},
				{items:[{ name:"#L.sysModifierAmail",    	type:"LABEL", value:"最後異動人員" }]},
				{items:[{ name:"#F.sysModifierAmail",    	type:"TEXT", bind:"sysModifierAmail", mode:"HIDDEN"},
						{ name:"#F.sysModifierAmailName",   type:"TEXT", bind:"sysModifierAmailName", mode:"READONLY"}]}
			]},{
				row_style:"", cols:[
				{items:[{ name:"#L.channelType",     	 	type:"LABEL", value:"通道類別"}]},
				{items:[{ name:"#F.channelType",         	type:"SELECT", bind:"channelType", size:10, init:channelType, eChange:"changeDetail()"}]},
				{items:[{ name:"#L.categoryType",    	 	type:"LABEL", value:"業種類別" }]},
				{items:[{ name:"#F.categoryType",    	 	type:"SELECT", bind:"categoryType", init:categoryType, eChange:"changeDetail()"}]},
				{items:[{ name:"#L.enable",     	 	 	type:"LABEL", value:"是否啟用" }]},
				{items:[{ name:"#F.enable",     	 	 	type:"CHECKBOX", bind:"enable"}]},
				{items:[{ name:"#L.sysLastUpdateTime",  	type:"LABEL", value:"最後異動時間" }]},
				{items:[{ name:"#F.sysLastUpdateTime",  	type:"TEXT", bind:"sysLastUpdateTime", mode:"HIDDEN"},
						{ name:"#F.sysLastUpdateTimeStr",  	type:"TEXT", bind:"sysLastUpdateTimeStr", size:20, mode:"READONLY"}]}
			]}
		],beginService:"",closeService:""			
	});
}

function kweDetail(){
	var varCanGridModify = true;
	var varCanGridDelete = false;
	var varCanGridAppend = false;

    vat.item.make(vnB_Detail, "columnIndex", 	{type:"IDX" , desc:"項次", mode:"READONLY"});
    vat.item.make(vnB_Detail, "columnCode", 	{type:"TEXT", size:40, desc:"欄位名稱<br>測試換行", mode:"READONLY"});
	vat.item.make(vnB_Detail, "columnName",     {type:"TEXT", size:60, desc:"欄位代號<br>測試換行2", mode:"READONLY"});
	vat.item.make(vnB_Detail, "enable",         {type:"CHECKBOX", size:10, desc:"啟用"});
//	vat.item.make(vnB_Detail, "lineId", {type:"ROWID"});

	vat.block.pageLayout(vnB_Detail, {
								id: "vatDetailDiv",	
								pageSize: 10,
								canGridModify:varCanGridModify,
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
							    appendBeforeService : "",
							    appendAfterService  : "",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "loadSuccessAfter()",
								eventService        : "",   
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "saveSuccessAfter()"
//								indexType			: "AUTO"
								});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function loadBeforeAjxService(){
	if(isDebugModel)
		alert("loadBeforeAjxService");
	var sysSno = vat.item.getValueByName("#F.sysSno");
	var processString = "process_object_name=ommChannelTXFService&process_object_method_name=getAJAXPageData" + 
						"&sysSno=" + sysSno;
	
	return processString;											
}

function loadSuccessAfter(){
	if(isDebugModel)
		alert("loadSuccessAfter");

}


/*
	取得SAVE要執行的JS FUNCTION
*/
function saveBeforeAjxService() {
	if(isDebugModel)
		alert("saveBeforeAjxService");
	
	var sysSno = vat.item.getValueByName("#F.sysSno");
	
	var processString = "process_object_name=ommChannelTXFService&process_object_method_name=updateAJAXPageLinesData"
						+ "&sysSno=" + sysSno;
	return processString;
}

/*
	取得存檔成功後要執行的JS FUNCTION
*/
function saveSuccessAfter() {
	if(isDebugModel)
		alert("saveSuccessAfter");
}

// 建立新資料
function createRefreshForm(){
	if(isDebugModel)
		alert("createRefreshForm");
}	

// 離開
function closeWindows(closeType){
	if(isDebugModel)
		alert("closeWindows");
  	var isExit = true ;
  	if("CONFIRM" == closeType){
		isExit = confirm("是否確認離開?");
 	}
  	if(isExit){
    	window.top.close();
   	}
}

// 暫存,送出
function doSubmit(formAction){
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("SUBMIT_BG" == formAction){
	 	alertMessage = "是否確定背景送出?";
	}
	
	if(confirm(alertMessage)){
		var formStatus = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
		
		if(isDebugModel)
			alert("formAction="+formAction+";formStatus="+formStatus);
			
		//saveBeforeAjxService、loadBeforeAjxService; 若funcSuccess成功,於最後執行loadSuccessAfter、saveSuccessAfter
		vat.block.pageDataSave(
			vnB_Detail, 
			{ funcSuccess:function(){
				vat.bean().vatBeanOther.formAction = formAction;
				vat.bean().vatBeanOther.formStatus = formStatus;
//				alert("funcSuccess");
				
				processString = "process_object_name=ommChannelAction&process_object_method_name=performTransaction";
				
//				alert("submit開始");
					vat.block.submit(
					function(){
//						alert("submit中1");
						return processString;},
					{ bind:true, link:true, other:true, funcSuccess:function(){
						
//						if(isDebugModel)
							alert("submit中2");
							
						refreshTXFHeadData();
						vat.block.pageRefresh(vnB_Detail);
						
					}}
				);
//				alert("submit結束");				
			}}			
		);
//		alert("submit失敗");
	}
}

function refreshTXFHeadData(){
	
	if(isDebugModel)
		alert("refreshHeadData");
	
	var sysSno = vat.item.getValueByName("#F.sysSno");
	
	vat.ajax.XHRequest(
       {
           post:"process_object_name=ommChannelTXFService&process_object_method_name=refreshTXFHeadData"+
                "&sysSno=" + sysSno,
           find: function change(oXHR){ 
	
				if(isDebugModel)
					alert("存檔成功後,更新單頭資料");
					
				var formOrderNo = vat.ajax.getValue("formOrderNo", oXHR.responseText);
				var formStatus = vat.ajax.getValue("formStatus", oXHR.responseText);
				var formStatusName = vat.ajax.getValue("formStatusName", oXHR.responseText);
				vat.item.setValueByName("#F.orderNo", formOrderNo);
				vat.item.setValueByName("#F.status", formStatus);
				vat.item.setValueByName("#F.statusName", formStatusName);
				
				doFormAccessControl();
				
           }   
       });
}

function changeDetail(){
	var sysSno = vat.item.getValueByName("#F.sysSno");
	var channelType = vat.item.getValueByName("#F.channelType");
	var categoryType = vat.item.getValueByName("#F.categoryType");
	
	
	if(isDebugModel)
		alert("channelType = " + channelType + "; categoryType = " + categoryType);
		
//	if(sysSno!=="" && channelType!=="" &&  categoryType!== "" ){
	if(sysSno!==""){
		
		vat.ajax.XHRequest(
       {
           post:"process_object_name=ommChannelAction&process_object_method_name=getDetailByCondition"+
                "&sysSno=" + sysSno +
                "&channelType=" + channelType+
                "&categoryType=" + categoryType,
           find: function change(oXHR){ 
				if(isDebugModel)
					alert("後端返回成功");
					
				var errorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
				
				if(errorMsg !== ""){
					alert(errorMsg);
					//復原查詢失敗前的條件
					var channelTypeRes = vat.ajax.getValue("channelTypeRes", oXHR.responseText);
					var categoryTypeRes = vat.ajax.getValue("categoryTypeRes", oXHR.responseText);
					vat.item.setValueByName("#F.channelType", channelTypeRes);
					vat.item.setValueByName("#F.categoryType", categoryTypeRes);
				}else{
					doPageRefresh();
				}
				
           }   
       });
	}
//	}else{
//		if(isDebugModel)
//			alert("資料有缺,不進後端");
//		vat.item.bindAll();
//	}
	
	
}

function doPageRefresh(){
	if(isDebugModel)
		alert("doPageRefresh");
    vat.block.pageRefresh(vnB_Detail);
}

// 訊息提示
function showMessage(){
	if(isDebugModel)
		alert("showMessage");
}

// 頁面控制
function doFormAccessControl(){
	var status = vat.item.getValueByName("#F.status");
	var orderNo = vat.item.getValueByName("#F.orderNo");
	var sysSign = vat.item.getValueByName("#F.sysSign");
	
	if( status === "SAVE"){
		alert("SAVE");
		if( sysSign === "N" ){
			alert("異動單(新增)");
			//單頭
			vat.item.setAttributeByName("#F.channelType" , "readOnly", false);
			vat.item.setAttributeByName("#F.categoryType", "readOnly", false);
			vat.item.setAttributeByName("#F.enable"      , "readOnly", false);
			
			//單身
			vat.block.canGridModify([vnB_Detail], true, false, false);
			
		}else{
			alert("異動單(修改)");
			//單頭
			vat.item.setAttributeByName("#F.channelType" , "readOnly", true);
			vat.item.setAttributeByName("#F.categoryType", "readOnly", true);
			vat.item.setAttributeByName("#F.enable"      , "readOnly", false);
			
			//單身
			vat.block.canGridModify([vnB_Detail], true, false, false);
			
		}
		
	}else if( status === "FINISH"){
		alert("FINISH");
		//單頭
		vat.item.setAttributeByName("#F.channelType" , "readOnly", true);
		vat.item.setAttributeByName("#F.categoryType", "readOnly", true);
		vat.item.setAttributeByName("#F.enable"      , "readOnly", true);
		
		//單身
		vat.block.canGridModify([vnB_Detail], false, false, false);
		
		//按鈕
		vat.item.setStyleByName("#B.save"  , 	"display",  "none");
		vat.item.setStyleByName("#B.submit", 	"display",  "none");
		
	}
	
}