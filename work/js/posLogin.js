/*** 
 *	檔案: posLogin_offline.js
 *	說明: pos2.0登入畫面
 */
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_version_header = 2;
var vnB_loginInfo_header = 3;
var vnB_messageInof_header = 4;
var vnB_machineInfo_header = 5;
var vnB_transferStatusInfo_header = 6;
var step=0;
var appCodePre="";
var versionInfo;

function outlineBlock(){

	formInitial();
	versionInfoInitial();
	
	document.write("<table class='defaultNone' border='0' cellpadding='3' cellspacing='0'><tr><td>");
  	
  	loginInfoInital();
  	document.write("<marquee scrolldelay='100'><span style='color:#0000ff;font-size:20pt'>2020/01/01 系統即將更新重新啟動</span></marquee>");
  	
  	

  	vat.tabm.createTab(0, "vatTabSpan", "H", "float");
	vat.tabm.createButton(0 ,"xTab8","訊息" ,"vatMaster2Div" ,"images/tab_specification_dark.gif" ,"images/tab_specification_light.gif", false, "doPageDataSave(4)");
	vat.tabm.createButton(0 ,"xTab2","促銷" ,"vatT3Div" ,"images/tab_standard_price_dark.gif" ,"images/tab_standard_price_light.gif",false, "doPageDataSave(3)");  
	
	messageInfoInital();
  	
  	document.write("</td><td>");
  	
  	machineInfoInital();
	transferStatusInfoInital();
  	
	document.write("</td></tr></table>");
	
	//setDefaltValue();
	setHeaderValue();
}

function setDefaltValue(){
	vat.item.setValueByName("#F.versionName" , "國際完稅");
	vat.item.setValueByName("#F.versionNo" , "V3.0");
	
//	vat.item.setValueByName("#F.employCode" , "20XXOO");
//	vat.item.setValueByName("#F.employName" , "陳ＸＸ");
//	vat.item.setValueByName("#F.superintendentCode" , "20XXOO");
//	vat.item.setValueByName("#F.superintendentName" , "林ＯＯ");
	vat.item.setValueByName("#F.sendWay" , "現場取貨");
	vat.item.setValueByName("#F.groupCategory" , "ＢＲ自組團");
	vat.item.setValueByName("#F.phoneNo" , "09XXXXXXXXX");
	vat.item.setValueByName("#F.leaderName" , "劉建宏");
	vat.item.setValueByName("#F.appCode" , "17APP");
	vat.item.setValueByName("#F.salesNo" , "DF1515XXX))");
	vat.item.setValueByName("#F.bkItemPs" , "酒類附贈紙袋20個");
	vat.item.setValueByName("#F.sendItemPs" , "收貨人：ＸＸＸ，請於下午送到，才有在家，不然壞掉你陪。");
	vat.item.setValueByName("#F.status" , "登入中");
	
	vat.item.setValueByName("#F.msgInfo" , "2020/01/01 系統即將更新重新啟動");
	vat.item.setValueByName("#F.promotionInfo" , "2020/01/01 全店打8折");
	
	vat.item.setValueByName("#F.machineNo" , "A9");
	vat.item.setValueByName("#F.transDate" , "2020/01/01");
	vat.item.setValueByName("#F.exchangeRateDate" , "2020/01/01");
	
}

function formInitial(){ 
	vat.bean().vatBeanOther = {
		loginBrandCode  		: 'T1BS',
		loginEmployeeCode  	    : 'T96085',
		formId             	    : 0,
		currentRecordNumber 	: 0,
		lastRecordNumber    	: 0
	 };
	 
	 vat.bean.init(	
	  		function(){
				return "process_object_name=soDepartmentOrderAction&process_object_method_name=performLoginInitial"; 
	    	},{
	    		other: true
    	});
    versionInfo = vat.bean().vatBeanOther.form;
    
}

function setHeaderValue(){
	if( null !== versionInfo ){
		var divTag = document.getElementById("vnB_loginInfo_header");
	    var tableTag = divTag.firstChild;
		var tBodyTag = tableTag.firstChild;
		var tdTagTmp = tBodyTag.firstChild;
		while(tBodyTag.firstChild){
			tBodyTag.removeChild(tBodyTag.firstChild);
		}
		tBodyTag.appendChild(tdTagTmp);
    
	    for(var i=0;i<versionInfo.length;i++){
	    
	    	var trTag = document.createElement("TR");
	    	var tdField1Tag = document.createElement("TD");//L1
	    	var tdField2Tag = document.createElement("TD");//R1
	    
	    	var field = versionInfo[i];
	    	//LABEL
	    	var desLabel = document.createElement("SPAN");
				desLabel.setAttribute("id", "#L."+field.displayCode);
				desLabel.setAttribute("name", "#L."+field.displayCode);
				desLabel.setAttribute("class", "defaultField");
				desLabel.setAttribute("type", "LABEL");
				desLabel.setAttribute("size", "20");
				//desLabel.setAttribute("value", field.displayName);
				desLabel.innerHTML = field.displayName + " : ";
				
			var desInput = document.createElement("INPUT");
				desInput.setAttribute("id",field.displayCode );
				desInput.setAttribute("name", field.displayCode );
				desInput.setAttribute("class", "defaultField");
				desInput.setAttribute("datatype", "TEXT");
				desInput.setAttribute("size", "20");
				desInput.setAttribute("value",field.displayName);
			
			tdField1Tag.appendChild(desLabel);
			tdField1Tag.appendChild(desInput);
			trTag.appendChild(tdField1Tag);
			trTag.appendChild(tdField2Tag);
			tBodyTag.appendChild(trTag);
			
	    }
		
	}

}


function versionInfoInitial(){
	vat.block.create(vnB_version_header, {
		id: "vnB_version_header", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"版本資訊block", rows:[  
		 	{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"版本名稱" }]},
				{items:[{name:"#F.versionName", 		type:"TEXT", 	bind:"versionName", mode:"READONLY" ,value:"" },
				        {name:"#F.versionUnusedHidden", 	type:"hidden", 	bind:"enablePrice" , back:false }]},
				{items:[{name:"#L.versionNo",			type:"LABEL", 	value:"版本編號" }]},
				{items:[{name:"#F.versionNo", 			type:"TEXT", 	bind:"versionNo",mode:"READONLY",value:"" },
				        {name:"#F.versionUnusedHidden", 	type:"hidden", 	bind:"enablePrice" , back:false }]}
			]}
	 	],
	 beginService:"",
	 closeService:""			
	});
}

function loginInfoInital(){

	var allSendWay =  [["", true, false], ["1.現場取貨", "2.宅配取貨"], ["1", "2"]];
	var allGroupCategory =  [["", true, false], ["BR-自組團", "VR-虛擬團"], ["1", "2"]];

	vat.block.create(vnB_loginInfo_header, {
		id: "vnB_loginInfo_header", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"登入資訊block", rows:[  
		 	/*{row_style:"", cols:[
			 	{items:[{name:"#L.employCode",			type:"LABEL", 	value:"營業員" }]},
				{items:[{name:"#F.employCode", 		type:"TEXT", 	bind:"employCode", eChange:"changeEmployeeCode('employee')" },
				        {name:"#F.employName", 	type:"TEXT", 	bind:"employName" , back:false , mode:"READONLY"}]},
				{items:[{name:"#L.superintendentCode",			type:"LABEL", 	value:"收銀員" }]},
				{items:[{name:"#F.superintendentCode", 			type:"TEXT", 	bind:"superintendentCode", eChange:"changeEmployeeCode('superintendent')" },
				        {name:"#F.superintendentName", 	type:"TEXT", 	bind:"superintendentName" , back:false,mode:"READONLY"  }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.sendWay",			type:"LABEL", 	value:"送貨方式" }]},
				{items:[{name:"#F.sendWay", 		type:"SELECT", 	bind:"sendWay",init:allSendWay }]},
				{items:[{name:"#L.groupCategory",			type:"LABEL", 	value:"團別" }]},
				{items:[{name:"#F.groupCategory", 		type:"SELECT", 	bind:"groupCategory",init:allGroupCategory}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.phoneNo",			type:"LABEL", 	value:"會員手機" }]},
				{items:[{name:"#F.phoneNo", 		type:"TEXT", 	bind:"phoneNo",size:40 ,maxLen:60}]},
				{items:[{name:"#L.leaderName",			type:"LABEL", 	value:"領隊" }]},
				{items:[{name:"#F.leaderName", 		type:"TEXT", 	bind:"leaderName"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.appCode",			type:"LABEL", 	value:"APP代號" }]},
				{items:[{name:"#B.appBtn"        , type:"IMG"    ,value:"17APP",   src:"./images/17app.png", eClick:'openAppCode()'},
						{name:"#F.appCode", 		type:"TEXT", 	bind:"appCode", mode:"READONLY" }]},
				{items:[{name:"#L.status",			type:"LABEL", 	value:"銷貨單" }]},
				{items:[{name:"#F.salesNo", 		type:"TEXT", 	bind:"", mode:"READONLY" ,value:""},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"備貨備註" }]},
				{items:[{name:"#F.bkItemPs", 		type:"TEXT", 	bind:"", mode:"READONLY" ,value:"",size:61 ,maxLen:100},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }],td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"送貨備註" }]},
				{items:[{name:"#F.sendItemPs", 		type:"TEXT", 	bind:"", mode:"READONLY" ,value:"",size:61 ,maxLen:100},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }],td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.status", 		type:"TEXT", 	bind:"", mode:"READONLY" ,value:"",size:61 ,maxLen:100},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }],td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"" }]},
				{items:[{name:"#F.enable", 		type:"hidden", 	bind:"", mode:"READONLY" ,value:""},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }]},
				{items:[{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("FINISH")'}]},
				{items:[{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'}]}
			]}*/
	 	],
	 beginService:"",
	 closeService:""			
	});
}

function showAppCode(){
	vat.item.setStyleByName("#B.import", "display", "none");
}

function messageInfoInital(){
	vat.block.create(vnB_messageInof_header, {
		id: "vnB_messageInof_header", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"訊息促銷block", rows:[  
		 	{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"訊息" }]},
				{items:[{name:"#F.msgInfo", 		type:"TEXT", 	bind:"versionName", mode:"READONLY" ,value:"" ,size:61 ,maxLen:100},
				        {name:"#F.versionUnusedHidden", 	type:"hidden", 	bind:"enablePrice" , back:false }]},
				{items:[{name:"#L.versionNo",			type:"LABEL", 	value:"促銷" }]},
				{items:[{name:"#F.promoteInfo", 			type:"TEXT", 	bind:"versionNo",mode:"READONLY",value:"",size:61 ,maxLen:100 },
				        {name:"#F.versionUnusedHidden", 	type:"hidden", 	bind:"enablePrice" , back:false }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"訊息" }]},
				{items:[{name:"#F.enable", 		type:"TEXT", 	bind:"", mode:"READONLY" ,value:"",size:61 ,maxLen:100},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }],td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"訊息" }]},
				{items:[{name:"#F.enable", 		type:"TEXT", 	bind:"", mode:"READONLY" ,value:"",size:61 ,maxLen:100},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }],td:" colSpan=2"}
			]}
	 	],
	 beginService:"",
	 closeService:""			
	});
}

function machineInfoInital(){
	vat.block.create(vnB_machineInfo_header, {
		id: "vnB_machineInfo_header", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"機台資訊block", rows:[  
		 	{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"機號" }]},
				{items:[{name:"#F.machineNo", 		type:"TEXT", 	bind:"versionName", mode:"READONLY" ,value:"15" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"交易日期" }]},
				{items:[{name:"#F.transDate", 		type:"TEXT", 	bind:"", mode:"READONLY" ,value:""},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"匯率日期" }]},
				{items:[{name:"#F.exchangeRateDate", 		type:"TEXT", 	bind:"", mode:"READONLY" ,value:""},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"" }]},
				{items:[{name:"#F.enable", 		type:"TEXT", 	bind:"", mode:"hidden" ,value:""},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"" }]},
				{items:[{name:"#F.enable", 		type:"TEXT", 	bind:"", mode:"hidden" ,value:""},
				        {name:"#F.enablePrice", 	type:"hidden", 	bind:"enablePrice" , back:false }]}
			]}
	 	],
	 beginService:"",
	 closeService:""			
	});
}

function transferStatusInfoInital(){
	vat.block.create(vnB_transferStatusInfo_header, {
		id: "vnB_transferStatusInfo_header", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"更新狀態block", rows:[  
		 	{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"系統更新" }]},
				{items:[{name:"#L.versionNameSuccess", 		type:"LABEL",value:" 成功 " },
				        {name:"#L.versionFail", 	type:"LABEL",value:" 失敗 " }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.status",			type:"LABEL", 	value:"匯率更新" }]},
				{items:[{name:"#F.floatmst", 	type:"TEXT", 	bind:"floatmst" },
						{name:"#F.exchangeRateUpdated", 	type:"IMG", src:"./images/16x16/Ball (Green).png" },
				        {name:"#F.exchangeRateUpdate", 		type:"IMG", src:"./images/16x16/Ball (Red).png" }]}
			]},
			{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"VIP更新" }]},
				{items:[{name:"#F.cusmst", 	type:"TEXT", 	bind:"cusmst" },
						{name:"#F.vipUpdate", 		type:"IMG", src:"./images/16x16/Ball (Green).png" },
				        {name:"#F.versionUnusedHidden", 		type:"IMG", src:"./images/16x16/Ball (Red).png"  }]}
			]},
			{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"商品主檔更新" }]},
				{items:[{name:"#F.plumst", 	type:"TEXT", 	bind:"plumst" },
						{name:"#F.itemUpdate", 		type:"IMG", src:"./images/16x16/Ball (Green).png" },
				        {name:"#F.versionUnusedHidden", 		type:"IMG", src:"./images/16x16/Ball (Red).png" }]}
			]},
			{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"商品促銷更新" }]},
				{items:[{name:"#F.promotion", 	type:"TEXT", 	bind:"promotion" },
						{name:"#F.promotionUpdate", 		type:"IMG", src:"./images/16x16/Ball (Green).png" },
				        {name:"#F.versionUnusedHidden", 		type:"IMG", src:"./images/16x16/Ball (Red).png" }]}
			]},
			{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"國際碼更新" }]},
				{items:[{name:"#F.eanStore", 	type:"TEXT", 	bind:"eanStore" },
						{name:"#F.eanCodeUpdate", 		type:"IMG", src:"./images/16x16/Ball (Green).png"},
				        {name:"#F.versionUnusedHidden", 		type:"IMG", src:"./images/16x16/Ball (Red).png"  }]}
			]},
			{row_style:"", cols:[
			 	{items:[{name:"#L.versionName",			type:"LABEL", 	value:"滿額贈更新" }]},
				{items:[{name:"#F.combine", 	type:"TEXT", 	bind:"combine" },
						{name:"#F.fullUpdate", 		type:"IMG", src:"./images/16x16/Ball (Green).png" },
				        {name:"#F.versionUnusedHidden", 		type:"IMG", src:"./images/16x16/Ball (Red).png"  }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#B.submit"      , type:"IMG"    ,value:"清機",   src:"./images/clean_machine.png", eClick:'doSubmit("FINISH")'}]},
				{items:[{name:"#B.exit"        , type:"IMG"    ,value:"班結",   src:"./images/schedule.png"  ,eClick:'closeWindows("CONFIRM")'}]}
			]}
	 	],
	 beginService:"",
	 closeService:""			
	});
}

//確認年齡層輸入
function eChangeSalesType() {

	var vSalesType = vat.item.getValueByName("#F.salesType");
	openPos();
}
//確認員工資訊
function eChangeEmployee() {
    vat.item.setValueByName("#F.salesEmployeeCode", vat.item.getValueByName("#F.salesEmployeeCode").replace(/^\s+|\s+$/, ''));
    vat.item.setValueByName("#F.salesEmployeeCode", vat.ajax.getValue("EmployeeCode", "T96085"));
    vat.item.setValueByName("#F.salesEmployeeName", vat.ajax.getValue("EmployeeName", "XXX"));
    step=1;//進入下一階段
}
//進入頁面游標預設工號登入位置
function focusEmployeeCode(){
	var employeeCode = document.getElementById("#F.salesEmployeeCode");
	employeeCode.focus();
}
//連至POS頁面帶入參數s
function doPassHeadData(){
  var suffix = "&orderTypeCode=SOP";
  return suffix;
}

vat.form.keydown = function(){
	var i, ret = false;
	if (event.altKey){
		event.returnValue = false;	// disable ALT key
		vat.debug("user", "請不要按下 alt 鍵");
	}else{
		switch(event.keyCode){
		case 13:		

			loginExecute();
			break;

		}
	}
}

function loginExecute(){
			if(step===0){
				eChangeEmployee();//驗證員工編號，若正確進入下一步驟
			}
			else if(step===1){
				eChangeSalesType();

			}
			else{
				alert("系統錯誤，請重啟頁面，若問題仍持續存在，請連繫資訊人員!"+step);
			}
}

// 離開按鈕按下
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}

function doSubmit(){
	var employeeName = vat.item.getValueByName("#F.employName");
	var superintendentName = vat.item.getValueByName("#F.superintendentName");
	
	if((null == employeeName || "" === employeeName )&&(null == superintendentName || "" === superintendentName)){
		alert("請先登入");
	}else{
		openPos();
	}
	
}

// 員工編號
function changeEmployeeCode(type){
	var employeeCode = vat.item.getValueByName("#F.employCode").replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setValueByName("#F.employCode" , employeeCode);
	//superintendent
	var superintendentCode = vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, '')
	var brandCode = "T1GS";
	var pattern = new RegExp("[^a-zA-Z0-9\\d\\s]+"); 
	 
	if( pattern.test(employeeCode) == true ){
    	alert("工號不可有特殊符號，請重新輸入!");
   	}else if(employeeCode.indexOf(" ") > -1 ){
   		alert("工號不可含有空白，請重新輸入!");
   	}else if(employeeCode.length > 6 ){
   		alert("工號不可大於6碼");
   	}else{
		vat.ajax.XHRequest({ 
			post:"process_object_name=soDepartmentOrderService"+
            		"&process_object_method_name=getAJAXEmployeeName"+
            		"&employeeCode="+ employeeCode +  
            		"&superintendentCode="+ superintendentCode +  
            		"&codeType="+ type +  
                	"&brandCode=" + brandCode,                      
			find: function changeRequestSuccess(oXHR){
				var empCName = vat.ajax.getValue("employeeCName", oXHR.responseText);
				var empEName = vat.ajax.getValue("employeeEName", oXHR.responseText);
				var supCName = vat.ajax.getValue("superintendentCName", oXHR.responseText);
				var supEName = vat.ajax.getValue("superintendentEName", oXHR.responseText);
				if(type === "employee"){
					vat.item.setValueByName("#F.employName" , empCName + "  "+ empEName);
				} else if(type === "superintendent") {
					vat.item.setValueByName("#F.superintendentName" ,  supCName + "  "+ supEName);
				}
				
           }
        });
	}

}

function openAppCode(){
	var appcode = prompt("請輸入APP/VIP代碼", "");
	if(null != appcode && "" !== appcode){
		if(confirm("請確認代碼為：" + appcode)){
			vat.item.setValueByName("#F.appCode" , appcode);
			appCodePre = appcode;
		}
	}
}

function openPos()
{
	var employeeCode = "T96085";
	var salesType ="";
	var url = "posSalesPage_offline.jsp?appCodePre=" + appCodePre ;
	
	var newwin = window.open(url, '_blank', 'fullscreen, height=2736, width=1824,titlebar=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes');
    newwin.resizeTo((screen.availWidth),(screen.availHeight));
    newwin.focus();
	//window.top.close();
}