/*** 
 *	檔案: buGoalDeploy.js
 *	說明： 目標設定
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Master = 2;
var vnB_DeployDetail = 3;

function outlineBlock(){

 	formInitial(); 
	buttonLine();
  	headerInitial();
	if (typeof vat.tabm != 'undefined') {                                                                                                                                                                                                                                                           
		vat.tabm.createTab(0, "vatTabSpan", "H", "float"); 
		vat.tabm.createButton(0 ,"xTab1","人員設定明細檔" 	,"vatDeployDetailDiv"    ,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false, "doPageDataSave("+vnB_DeployDetail+")");  
		vat.tabm.createButton(0 ,"xTab2","簽核資料"   	,"vatApprovalDiv"        ,"images/tab_approval_data_dark.gif"      ,"images/tab_approval_data_light.gif",vat.item.getValueByName("#F.flowStatus") == "SAVE" || vat.item.getValueByName("#F.flowStatus") == "UNCONFIRMED"? "none" : "inline");
 	}  
	detailDeployInitial();
	
	kweWfBlock(vat.item.getValueByName("#F.brandCode"), 
            	"BGD", 
             	vat.item.getValueByName("#F.reserve1"),
             	document.forms[0]["#loginEmployeeCode"].value );
             	
	doFormAccessControl();
}

// 初始化
function formInitial(){  
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          processId          	: document.forms[0]["#processId"         ].value, 
          assignmentId       	: document.forms[0]["#assignmentId"      ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=buGoalAction&process_object_method_name=performDeployInitial"; 
	    	},{								
	    		other: true
    	});
    	
  	}
}

function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Bu_Goal:searchDeploy:20091203.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"#B.edit"        , type:"IMG"    ,value:"修改",   src:"./images/button_edit.gif", eClick:'doEdit()'},
	 			{name:"#B.match"       , type:"IMG"    ,value:"匹配",   src:"./images/button_lock_data.gif", eClick:'doMatch()'},
	 			{name:"#B.unMatch"     , type:"IMG"    ,value:"解除匹配",   src:"./images/button_unlock_data.gif", eClick:'doUnMatch()'},
	 			{name:"#B.save"		   , type:"IMG"    ,value:"暫存",   src:"./images/button_save.gif", eClick:'doSubmit("SAVE")'},
	 			{name:"#B.submitBG"    , type:"IMG"    ,value:"背景送出",   src:"./images/button_submit_background.gif", eClick:'doSubmit("SUBMIT_BG")'},
				{name:"#B.message"     , type:"IMG"    ,value:"訊息提示",   src:"./images/button_message_prompt.gif", eClick:'showMessage()'},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/button_form_print.gif" , eClick:'openReportWindow("")'}, 
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

// 目標設定主檔
function headerInitial(){ 
	var allShopCodes = vat.bean("allShopCodes");
	var allDepartments = vat.bean("allDepartments");
	var allYears = vat.bean("allYears");
	var allMonths = vat.bean("allMonths");
	
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"專櫃業績預測人員設定作業", 
		rows:[	// 
			{row_style:"", cols:[
				{items:[{name:"#L.department", 			type:"LABEL", 	value:"部門<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.department", 			type:"SELECT", 	bind:"department", init:allDepartments,	size:1, eChange:"changeShopCode()"}]},
				{items:[{name:"#L.shopCode", 			type:"LABEL", 	value:"專櫃代號<font color='red'>*</font>"}]},
				{items:[{name:"#F.shopCode", 			type:"SELECT", 	bind:"shopCode", init:allShopCodes, size:10},
						{name:"#F.headId", 				type:"TEXT",  	bind:"headId", back:false, mode:"READONLY"},
						{name:"#F.reserve1", 			type:"TEXT",  	bind:"reserve1", mode:"HIDDEN"},
						{name:"#F.reserve2", 			type:"TEXT",  	bind:"reserve2", mode:"HIDDEN"}]},
				{items:[{name:"#L.brandCode", 			type:"LABEL", 	value:"品牌"}]},	 
	 			{items:[{name:"#F.brandCode", 			type:"TEXT",  	bind:"brandCode", mode:"HIDDEN"},
	 					{name:"#F.brandName", 			type:"TEXT",  	bind:"brandName", size:8, mode:"READONLY"}]},
				{items:[{name:"#L.flowStatus", 			type:"LABEL", 	value:"狀態" }]},
				{items:[{name:"#F.flowStatus", 			type:"TEXT", 	bind:"flowStatus", size:15, mode:"HIDDEN"},
					{name:"#F.flowStatusName", 			type:"TEXT",  bind:"flowStatusName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.year", 				type:"LABEL", 	value:"年<font color='red'>*</font>" }]},
				{items:[{name:"#F.year", 				type:"SELECT", 	bind:"year", init:allYears, size:10}]},
				{items:[{name:"#L.month", 				type:"LABEL", 	value:"月<font color='red'>*</font>" }]},
				{items:[{name:"#F.month", 				type:"SELECT", 	bind:"month", init:allMonths, size:10 }]}, 
				{items:[{name:"#L.createBy",		type:"LABEL", 	value:"填單人員" }]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",		type:"TEXT", 	bind:"createByName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.creationDate",		type:"LABEL", 	value:"填單日期" }]},
				{items:[{name:"#F.creationDate",		type:"TEXT", 	bind:"creationDate", mode:"READONLY"}]}	
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.originalGoal", 	type:"LABEL", 	value:"原簽核目標總額"}]},	 
	 			{items:[{name:"#F.originalGoal", 	type:"NUMB", 	bind:"originalGoal", mode:"READONLY", size:10 }]},
	 			{items:[{name:"#L.actualGoal", 		type:"LABEL", 	value:"修正目標總額"}]},	 
	 			{items:[{name:"#F.actualGoal", 		type:"NUMB", 	bind:"actualGoal", mode:"READONLY", eChange:"changeLineSigningAmount()",	size:10 }]},
	 			{items:[{name:"#L.lastMonthGoal", 	type:"LABEL", 	value:"上月銷售金額"}]},	 
	 			{items:[{name:"#F.lastMonthGoal", 	type:"NUMB", 	bind:"lastMonthGoal", mode:"READONLY",	size:10 },
	 					{name:"#S.space", 			type:"LABEL", 	value:" " },
	 					{name:"#F.percent", 		type:"NUMB", 	bind:"percent", eChange:"changePercent()" ,	back:false, size:3, maxLen:3 }, 
	 					{name:"#L.percent", 		type:"LABEL", 	value:"%" }]},
	 			{items:[{name:"#L.lastYearGoal", 	type:"LABEL", 	value:"去年同月銷售金額"}]},	 
	 			{items:[{name:"#F.lastYearGoal", 	type:"NUMB", 	bind:"lastYearGoal", mode:"READONLY",	size:10 }]}
	 			
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

// 目標設定
function detailDeployInitial(){
	var allItemCategorys = vat.bean("allItemCategorys");
	var allItemSubcategorys = vat.bean("allItemSubcategorys");
	var allWorkTypes = vat.bean("allWorkTypes");
	var allItemBrands = vat.bean("allItemBrands");

	var status = vat.item.getValueByName("#F.flowStatus");
	
	var isOpen = true;
	if( status == "SIGNING" ){
		isOpen = false;	
	}
	
	var vbCanGridDelete = isOpen;
	var vbCanGridAppend = isOpen;
	var vbCanGridModify = isOpen;
  
	vat.item.make(vnB_DeployDetail, "indexNo"			, {type:"IDX" , desc:"序號" });
	
	vat.item.make(vnB_DeployDetail, "itemCategory"		, {type:"SELECT" 	, size:1, desc:"業種", init:allItemCategorys, mode:"READONLY" 		});	
	vat.item.make(vnB_DeployDetail, "itemSubcategory"	, {type:"SELECT" 	, size:1, desc:"業種子類", init:allItemSubcategorys, mode:"READONLY"	});
	vat.item.make(vnB_DeployDetail, "itemBrand"			, {type:"SELECT" 	, size:1, desc:"商品品牌", init:allItemBrands, mode:"READONLY"	});
	
	vat.item.make(vnB_DeployDetail, "employeeCode"		, {type:"TEXT" 		, size:20, maxLen:23, desc:"員工代號", mode:"READONLY"           	});
	vat.item.make(vnB_DeployDetail, "workType"			, {type:"SELECT" 	, size:20, desc:"班別" , init:allWorkTypes, mode:"READONLY"      });
	vat.item.make(vnB_DeployDetail, "goalAmount"		, {type:"NUMB" 		, size:10, desc:"業績目標", mode:"READONLY"   	});
	vat.item.make(vnB_DeployDetail, "signingAmount"		, {type:"NUMB" 		, size:10, desc:"修正目標", mode:"READONLY", eChange:"changeOriginalGoal()"   });	
	
	vat.item.make(vnB_DeployDetail, "lineId"          , {type:"HIDDEN"});
	vat.item.make(vnB_DeployDetail, "isLockRecord"    , {type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
  	vat.item.make(vnB_DeployDetail, "isDeleteRecord"  , {type:"DEL"  , desc:"刪除"});
	vat.item.make(vnB_DeployDetail, "message"         , {type:"MSG"  , desc:"訊息"});
	vat.block.pageLayout(vnB_DeployDetail, {
														id: "vatDeployDetailDiv", 
														pageSize: 10,											
								            			canGridDelete : vbCanGridDelete,
														canGridAppend : vbCanGridAppend,
														canGridModify : vbCanGridModify,						
														appendBeforeService : "appendBeforeService()",
														appendAfterService  : "appendAfterService()",
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_DeployDetail+")",
														loadSuccessAfter    : "loadSuccessAfter()",						
														eventService        : "eventService()",   
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_DeployDetail+")",
														saveSuccessAfter    : "saveSuccessAfter("+vnB_DeployDetail+")"
														});
	vat.block.pageDataLoad(vnB_DeployDetail, vnCurrentPage = 1);
}

// 第一次載入 重新整理
function loadBeforeAjxService(div){
//    alert(div);	
	if( vnB_DeployDetail === div ){
		var processString = "process_object_name=buGoalService&process_object_method_name=getAJAXDeployPageData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId");
		return processString;	
	}							
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
//    alert("saveBeforeAjxService"); 

	if( vnB_DeployDetail === div ){
		var processString = "process_object_name=buGoalService&process_object_method_name=updateAJAXDeployPageLinesData" + 
		                    "&headId=" + vat.item.getValueByName("#F.headId") +
		                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value;
		return processString;	
	}		
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){ 
//	vat.block.pageRefresh(div); 
} 

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(){
//    alert("loadSuccessAfter");	
} 

// 新增空白頁
function appendBeforeService(){
//    alert("appendBeforeService");	 
	return true;
}    

// 新增空白頁成功後
function appendAfterService(){
//    alert("appendAfterService");	
} 

function eventService(){
//	alert("eventService");
} 
 

// tab切換 存檔
function doPageDataSave(div){

	if(vnB_DeployDetail===div){
		vat.block.pageDataSave(div); //存檔vnB_DeployDetail 
	}
}
	
// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
    	refreshForm("");
	 }
}

// 送出後返回呼叫的js
function createRefreshForm(){
	refreshForm("");
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
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

// 送出,暫存按鈕
function doSubmit(formAction){
	
	var headId = vat.item.getValueByName("#F.headId");
	if( headId == "" || headId === null ){
		alert("請先點選 match 鈕");
		return;
	}
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){
		alertMessage = "是否確定暫存?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}
	if(confirm(alertMessage)){
		var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.flowStatus").replace(/^\s+|\s+$/, '');
		var approvalResult        = getApprovalResult();  
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
		if( ( status == "SAVE" ) || (inProcessing   && (status == "SAVE" || status == "SIGNING" || status == "REJECT" )) ){
			vat.block.pageDataSave( vnB_DeployDetail ,{ 
				funcSuccess:function(){
					vat.bean().vatBeanOther.formAction 		= formAction;
		  			vat.bean().vatBeanOther.beforeStatus	= status;	
		  			vat.bean().vatBeanOther.processId       = processId;
	  				vat.bean().vatBeanOther.approvalResult  = approvalResult;
	  				vat.bean().vatBeanOther.approvalComment =  approvalComment;
					
				    if("SUBMIT_BG" == formAction){
				      	vat.block.submit(function(){return "process_object_name=buGoalAction"+
				                    "&process_object_method_name=getOrderNoDeploy";}, {bind:true, link:true, other:true});
				    }else{
						vat.block.submit(function(){return "process_object_name=buGoalAction"+
				                    "&process_object_method_name=performTransactionDeploy";},{
				                    bind:true, link:true, other:true,
				                    funcSuccess:function(){
						        		vat.block.pageRefresh(vnB_DeployDetail);
						        	}}
						);
			        } 
		      	}
	      	});
		}
	}
}

// 修改明細 將明細打開
function doEdit(){

	vat.ajax.XHRequest({
          post:"process_object_name=buGoalService"+
                   "&process_object_method_name=updateDeployHead"+
                   "&brandCode=" + document.forms[0]["#loginBrandCode"    ].value + 
                   "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
                   "&headId=" + vat.item.getValueByName("#F.headId"),
          find: function change(oXHR){ 
          		vat.item.setValueByName("#F.flowStatus", vat.ajax.getValue("flowStatus", oXHR.responseText));
				vat.item.setValueByName("#F.flowStatusName", vat.ajax.getValue("flowStatusName", oXHR.responseText));
//				vat.item.setValueByName("#F.headId", vat.ajax.getValue("headId", oXHR.responseText));
//				vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
          } 
	});	
	doFormAccessControl("edit");
}

// 取得簽核結果
function getApprovalResult(){
	if(vat.item.getValueByName("#F.flowStatus") == "SIGNING"){
		return vat.item.getValueByName("#F.approvalResult").toString();
	}else{
		return "true";
	}
}

// 尋找匹配
function doMatch(){
	var department = vat.item.getValueByName("#F.department");
	var shopCode = vat.item.getValueByName("#F.shopCode");
	var year = vat.item.getValueByName("#F.year");
	var month = vat.item.getValueByName("#F.month");
	var actualGoal = vat.item.getValueByName("#F.actualGoal");
	if(department == ""){
		alert('部門不可為空');
	}else if(shopCode == ""){
		alert('專櫃代號不可為空');
	}else if( year == "" ){
		alert('年不可為空');
	}else if( month == "" ){
		alert('月不可為空');
	}else{
		vat.ajax.XHRequest(
		    {
		        post:"process_object_name=buGoalService"+
		                    "&process_object_method_name=executeMatchBuGoalDeploy"+
		                    "&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
		                    "&headId=" + vat.item.getValueByName("#F.headId") + 
		                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
		                    "&department=" + department +
		                    "&shopCode=" + shopCode +
		                    "&year=" + year +
		                    "&month=" + month +
		                    "&actualGoal=" + actualGoal,
		        find: function loadBudgetSuccess(oXHR){
		        	vat.item.setValueByName("#F.reserve1" ,vat.ajax.getValue("reserve1", oXHR.responseText));
			        vat.item.setValueByName("#F.flowStatus" ,vat.ajax.getValue("flowStatus", oXHR.responseText));
			        vat.item.setValueByName("#F.flowStatusName" ,vat.ajax.getValue("flowStatusName", oXHR.responseText));
			        vat.item.setValueByName("#F.headId" ,vat.ajax.getValue("headId", oXHR.responseText));
			        vat.item.setValueByName("#F.originalGoal" ,vat.ajax.getValue("originalGoal", oXHR.responseText));
			        vat.item.setValueByName("#F.actualGoal" ,vat.ajax.getValue("actualGoal", oXHR.responseText));
			        vat.item.setValueByName("#F.lastMonthGoal" ,vat.ajax.getValue("lastMonthGoal", oXHR.responseText));
			        vat.item.setValueByName("#F.lastYearGoal" ,vat.ajax.getValue("lastYearGoal", oXHR.responseText));
			        vat.item.setValueByName("#F.reserve2" ,vat.ajax.getValue("reserve2", oXHR.responseText));
			        
			        if( vat.item.getValueByName("#F.headId") != "" ){
			        	 vat.block.pageRefresh(vnB_DeployDetail);
			  			doFormAccessControl("match");
			        }else{
			        	alert("查無此定義檔\n部門: "+department+" 專櫃代號: " + shopCode + ",\n請先設定定義檔");
			        }
			       
		       }   
		   })
		   
	}
}

// 解除匹配
function doUnMatch(){
	// 清除
	vat.bean().vatBeanOther.formId       = "";
	document.forms[0]["#formId"].value	= "";
	vat.item.setValueByName("#F.headId" , 		"");
	
	// 初始化筆數
	vat.item.setValueByName("#L.currentRecord", "0");
	vat.item.setValueByName("#L.maxRecord"    , "0");
    vat.bean().vatBeanPicker.result = null;
    
 	vat.block.pageRefresh(vnB_DeployDetail);
    
    vat.item.setValueByName("#F.percent", 			""); 		
    vat.item.setValueByName("#F.reserve1", 			"");
    vat.item.setValueByName("#F.flowStatus", 		"SAVE");
    vat.item.setValueByName("#F.flowStatusName",	"暫存");
    vat.item.setValueByName("#F.originalGoal",		"0");
    vat.item.setValueByName("#F.actualGoal",		"0");
    vat.item.setValueByName("#F.lastMonthGoal",		"0");
    vat.item.setValueByName("#F.lastYearGoal",		"0");
    doFormAccessControl();
}

// 動態改變專櫃下拉
function changeShopCode(){
	vat.ajax.XHRequest({
    	post:"process_object_name=buShopService"+
                   "&process_object_method_name=getAJAXShop"+
                   "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                   "&department=" + vat.item.getValueByName("#F.department"), 
    	find: function change(oXHR){
			var allShop = eval(vat.ajax.getValue("allShop", oXHR.responseText));
	        allShop[0][0] = "#F.shopCode";
			vat.item.SelectBind(allShop); 
    	}   
	});
}

// 改變簽核目標金額
function changeLineSigningAmount(){
	var actualGoal = vat.item.getValueByName("#F.actualGoal");
	if( actualGoal != "" ){
		vat.ajax.XHRequest({
	        post:"process_object_name=buGoalService"+
	                    "&process_object_method_name=updateAJAXSigningAmount"+
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&actualGoal=" + actualGoal,
	        find: function loadBudgetSuccess(oXHR){
//	        	vat.item.setValueByName("#F.originalGoal" ,vat.ajax.getValue("originalGoal", oXHR.responseText));
	        	vat.item.setValueByName("#F.actualGoal" ,vat.ajax.getValue("actualGoal", oXHR.responseText));
		        vat.block.pageRefresh(vnB_DeployDetail);
	        }   
		})
	}
}

// 改變趴數
function changePercent(){
	var percent = vat.item.getValueByName("#F.Percent");
	var lastMonthGoal = vat.item.getValueByName("#F.lastMonthGoal");
	if( percent != "" ){
		vat.ajax.XHRequest({
	        post:"process_object_name=buGoalService"+
	                    "&process_object_method_name=updateAJAXPercent"+
	                    "&headId=" + vat.item.getValueByName("#F.headId") + 
	                    "&percent=" + percent +
	                    "&lastMonthGoal=" + lastMonthGoal,
	        find: function loadBudgetSuccess(oXHR){
//	        	vat.item.setValueByName("#F.originalGoal" ,vat.ajax.getValue("originalGoal", oXHR.responseText));
	        	vat.item.setValueByName("#F.actualGoal" ,vat.ajax.getValue("actualGoal", oXHR.responseText));
		        vat.block.pageRefresh(vnB_DeployDetail);
	        }   
		})
	}
}

// 改變原始目標金額
function changeOriginalGoal(){
	var vLineId	= vat.item.getGridLine();
	var signingAmount	= vat.item.getGridValueByName("signingAmount", vLineId);
	vat.block.pageDataSave( vnB_DeployDetail ,{ 
			funcSuccess:function(){
				vat.ajax.XHRequest({
				        post:"process_object_name=buGoalService"+
				                    "&process_object_method_name=updateAJAXOriginalGoal"+
				                    "&headId=" + vat.item.getValueByName("#F.headId") + 
				                    "&signingAmount=" + signingAmount,
				        find: function loadBudgetSuccess(oXHR){
				        	vat.item.setValueByName("#F.actualGoal" ,vat.ajax.getValue("actualGoal", oXHR.responseText));
				        	vat.item.setGridValueByName("signingAmount" , vLineId,vat.ajax.getValue("signingAmount", oXHR.responseText));
				        }   
				})
			}
	})
}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 上一筆
function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 下一筆
function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 最後一筆
function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 票據列印
function openReportWindow(type){
	var year = vat.item.getValueByName("#F.year");
	var month = vat.item.getValueByName("#F.month");
	var shopCode = vat.item.getValueByName("#F.shopCode");
	
	if( year == null || year == ""){
		return alert("請選擇年份");
	}else if(month == null || month == ""){
		return alert("請選擇月份");
	}else if(shopCode == null || shopCode == ""){
		return alert("請選擇專櫃代號");	
	}
	vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = "BGD";
    vat.bean().vatBeanOther.year = year;
    vat.bean().vatBeanOther.month = month;
    vat.bean().vatBeanOther.shopCode = shopCode;
    if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");
	vat.block.submit(function(){return "process_object_name=buGoalService"+
								"&process_object_method_name=getReportConfig";},{other:true,
			                    funcSuccess:function(){
					        		eval(vat.bean().vatBeanOther.reportUrl);
					        	}}
	);	

	if("AFTER_SUBMIT"==type) createRefreshForm();
}

// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"            ].value = code; 
	document.forms[0]["#processId"         ].value = "";   
	document.forms[0]["#assignmentId"      ].value = "";    
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.bean().vatBeanOther.processId    = document.forms[0]["#processId"         ].value;     
	vat.bean().vatBeanOther.assignmentId = document.forms[0]["#assignmentId"      ].value;
	vat.block.submit(
		function(){
			return "process_object_name=buGoalAction&process_object_method_name=performDeployInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				vat.block.pageDataLoad(vnB_DeployDetail, vnCurrentPage = 1);
				
				var allShopCodes = vat.bean("allShopCodes");
				allShopCodes[0][0] = "#F.shopCode";
				vat.item.SelectBind(allShopCodes); 
				
				doFormAccessControl();
     	}});
 	
    
}

// 依狀態鎖form
function doFormAccessControl( match ){
	var status 		= vat.item.getValueByName("#F.flowStatus");
	var processId	= vat.bean().vatBeanOther.processId;
	var formId 		= vat.bean().vatBeanOther.formId;
	var reserve2 	= vat.item.getValueByName("#F.reserve2");
	// 初始化
	//======================<header>=============================================
	vat.item.setAttributeByName("#F.department", "readOnly", false);
	vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
	vat.item.setAttributeByName("#F.year", "readOnly", false);
	vat.item.setAttributeByName("#F.month", "readOnly", false);
	vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
	vat.item.setAttributeByName("#F.actualGoal", "readOnly", true);
	vat.item.setAttributeByName("#F.percent", "readOnly", true);
	//======================<detail>=============================================
	vat.item.setGridAttributeByName("itemCategory", "readOnly", true); 
	vat.item.setGridAttributeByName("itemSubcategory", "readOnly", true);
	vat.item.setGridAttributeByName("employeeCode", "readOnly", true);
	vat.item.setGridAttributeByName("workType", "readOnly", true);
	vat.item.setGridAttributeByName("goalAmount", "readOnly", true);
	vat.item.setGridAttributeByName("signingAmount", "readOnly", true);
	//=======================<buttonLine>========================================
	vat.item.setStyleByName("#B.new", 		"display", "inline");
	vat.item.setStyleByName("#B.search", 	"display", "inline");
	vat.item.setStyleByName("#B.submit", 	"display", "none");
	vat.item.setStyleByName("#B.save", 		"display", "none");
	vat.item.setStyleByName("#B.submitBG", 	"display", "none");
	vat.item.setStyleByName("#B.message", 	"display", "inline");
	vat.item.setStyleByName("#B.match", 	"display", "inline");
	vat.item.setStyleByName("#B.unMatch", 	"display", "none");
	vat.item.setStyleByName("#B.edit", 		"display", "none");
	//===========================================================================

	// picker Or process
	if( formId != null && formId != "" ){
		vat.item.setAttributeByName("#F.department", "readOnly", true);
		vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
		vat.item.setAttributeByName("#F.year", "readOnly", true);
		vat.item.setAttributeByName("#F.month", "readOnly", true);
		
		if( processId != null && processId != 0 ){ //從待辦事項進入
			vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
			vat.item.setAttributeByName("#F.actualGoal", "readOnly", true);
			vat.item.setAttributeByName("#F.percent", "readOnly", true);
			vat.item.setStyleByName("#B.new", 		"display", "none");
			vat.item.setStyleByName("#B.search", 	"display", "none");
			vat.item.setStyleByName("#B.match", 	"display", "none");
			vat.item.setStyleByName("#B.unMatch", 	"display", "none");
			if( status == "SAVE" || status == "REJECT" ){
				vat.item.setStyleByName("#B.submitBG"	, "display", "inline");
		  		vat.item.setStyleByName("#B.submit"	 	, "display", "inline");
		  		vat.item.setStyleByName("#B.save" 		, "display", "inline");
				vat.item.setAttributeByName("#F.actualGoal", "readOnly", false);
				vat.item.setAttributeByName("#F.percent", "readOnly", false);
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",false);
			}else if( status == "SIGNING" ){
				vat.item.setStyleByName("#B.submit", 		"display", "inline");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
		  		vat.item.setStyleByName("#B.save" 		, "display", "none");
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",true);
			}
		}else{	// picker
			vat.item.setStyleByName("#B.unMatch"	, "display", "inline");
	  		vat.item.setStyleByName("#B.match"	 	, "display", "none");
			if( (status == "SAVE" || status == "REJECT" ) && reserve2 == "Y"){
				vat.item.setStyleByName("#B.submitBG"	, "display", "none");
		  		vat.item.setStyleByName("#B.submit"	 	, "display", "none");
		  		vat.item.setStyleByName("#B.save" 		, "display", "none");
		  		vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
		  		vat.item.setAttributeByName("#F.actualGoal", "readOnly", true);
				vat.item.setAttributeByName("#F.percent", "readOnly", true);
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",true);
			}else if(status == "SAVE" && reserve2 == "N"){
				vat.item.setStyleByName("#B.submitBG"	, "display", "inline");
		  		vat.item.setStyleByName("#B.submit"	 	, "display", "inline");
		  		vat.item.setStyleByName("#B.save" 		, "display", "inline");
		  		vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
		  		vat.item.setAttributeByName("#F.actualGoal", "readOnly", false);
				vat.item.setAttributeByName("#F.percent", "readOnly", false);
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",false);
			}else if( status == "SIGNING"  ){
				vat.item.setStyleByName("#B.submit", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
		  		vat.item.setStyleByName("#B.save" 		, "display", "none");
		  		vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
		  		vat.item.setAttributeByName("#F.actualGoal", "readOnly", true);
				vat.item.setAttributeByName("#F.percent", "readOnly", true);
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",true);
			}else if( status == "FINISH"){
				vat.item.setStyleByName("#B.submit", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
		  		vat.item.setStyleByName("#B.save", 			"display", "none");
		  		vat.item.setStyleByName("#B.edit", 			"display", "inline");
		  		//===========================<detail>================================================
				vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
		  		vat.item.setAttributeByName("#F.actualGoal", "readOnly", true);
				vat.item.setAttributeByName("#F.percent", "readOnly", true);
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",true);
				//=========================================================================================
			}
		}
	}else{	// 維護進入點
		if( match == "match" ){	// 匹配
			vat.item.setAttributeByName("#F.department", "readOnly", true);
			vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
			vat.item.setAttributeByName("#F.year", "readOnly", true);
			vat.item.setAttributeByName("#F.month", "readOnly", true);
			vat.item.setStyleByName("#B.unMatch"	, "display", "inline");
		  	vat.item.setStyleByName("#B.match"	 	, "display", "none");
		  	if( (status == "SAVE" || status == "REJECT" ) && reserve2 == "Y"){
				vat.item.setStyleByName("#B.submitBG"	, "display", "none");
		  		vat.item.setStyleByName("#B.submit"	 	, "display", "none");
		  		vat.item.setStyleByName("#B.save" 		, "display", "none");
		  		vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
		  		vat.item.setAttributeByName("#F.actualGoal", "readOnly", true);
				vat.item.setAttributeByName("#F.percent", "readOnly", true);
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",true);
			}else if(status == "SAVE" && reserve2 == "N"){
				vat.item.setStyleByName("#B.submitBG"	, "display", "inline");
		  		vat.item.setStyleByName("#B.submit"	 	, "display", "inline");
		  		vat.item.setStyleByName("#B.save" 		, "display", "inline");
		  		vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
		  		vat.item.setAttributeByName("#F.actualGoal", "readOnly", false);
				vat.item.setAttributeByName("#F.percent", "readOnly", false);
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",false);
			}else if( status == "SIGNING" ){
				vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
				vat.item.setAttributeByName("#F.actualGoal", "readOnly", true);
				vat.item.setAttributeByName("#F.percent", "readOnly", true);
				vat.item.setStyleByName("#B.submit", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
		  		vat.item.setStyleByName("#B.save" 		, "display", "none");
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",true);
			}else if( status == "FINISH" ){
				vat.item.setStyleByName("#B.submit", 		"display", "none");
				vat.item.setStyleByName("#B.submitBG", 		"display", "none");
		  		vat.item.setStyleByName("#B.save", 			"display", "none");
		  		vat.item.setStyleByName("#B.edit", 			"display", "inline");
		  		//===========================<detail>================================================
				vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
		  		vat.item.setAttributeByName("#F.actualGoal", "readOnly", true);
				vat.item.setAttributeByName("#F.percent", "readOnly", true);
		  		vat.item.setGridAttributeByName("signingAmount","readOnly",true);
				//=========================================================================================
			}
		}else if(match == "edit"){
			vat.item.setStyleByName("#B.submit", 	"display", "inline");
			vat.item.setStyleByName("#B.save", 		"display", "inline");
			vat.item.setStyleByName("#B.submitBG", 	"display", "inline");
			vat.item.setStyleByName("#B.unMatch", 	"display", "inline");
		  	vat.item.setStyleByName("#B.match", 	"display", "none");
			vat.item.setStyleByName("#B.edit", 		"display", "none");
			//======================<header>=============================================
			vat.item.setAttributeByName("#F.department", "readOnly", true);
			vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
			vat.item.setAttributeByName("#F.year", "readOnly", true);
			vat.item.setAttributeByName("#F.month", "readOnly", true);
			//======================<deteil>=============================================
			vat.item.setAttributeByName("#F.originalGoal", "readOnly", true);
			vat.item.setAttributeByName("#F.actualGoal", "readOnly", false);
			vat.item.setAttributeByName("#F.percent", "readOnly", false);
		  	vat.item.setGridAttributeByName("signingAmount","readOnly",false);
		}
	}
	if(status == "SIGNING"){
		vat.tabm.displayToggle(0, "xTab2", true);
		refreshWfParameter(vat.item.getValueByName("#F.brandCode"), 
       					 	   "BGD", 
       					 	   vat.item.getValueByName("#F.reserve1"));
	 	vat.block.pageDataLoad(102, vnCurrentPage = 1);
	}else if(status == "SAVE"){
		vat.tabm.displayToggle(0, "xTab2", false);
	}
}

//	訊息提示
function showMessage(){
	var width = "600";
    var height = "400";
	var returnData = window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=BU_GOAL_DEPLOY" + 
		"&levelType=ERROR" +
        "&processObjectName=buGoalService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

// 後端背景送出執行
function execSubmitBgAction(){
    vat.block.submit(function(){return "process_object_name=buGoalAction"+
        "&process_object_method_name=performTransactionForBackGroundDeploy";}, {bind:true, link:true, other:true});
}
