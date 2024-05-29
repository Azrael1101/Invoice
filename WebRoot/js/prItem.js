/*** 
 *	檔案: buShop.js
 *	說明: 類別代號,抽成率維護
 */
 
//關閉DEBUG
vat.debug.disable();
//設定全域變數
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_master = 2;
//var vnB_Disable_Brand = 3;
var vnB_master2 = 4;

//CEAP 主要函數
function outlineBlock(){
	//alert('123abc');
  	formInitial();//初始化
  	buttonLine();//按鈕列表
  	headerInitial();//單頭列
	doFormAccessControl();//控制權限
	
	
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","主檔資料"      ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);
		//vat.tabm.createButton(0 ,"xTab2","品牌預設"      ,"vatBlock_Disable_Brand" ,"images/disable_brand_dark.gif" ,"images/disable_brand_light.gif", false);     
	}
	kweMaster();
	//kweDisableBrand();
	
}

//開始初始畫
function formInitial(){
		 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { //document.forms[0]["#" ].value 回傳網頁端的值
          brandCode  			: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          loginDepartment  	    : document.forms[0]["#loginDepartment"   ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
          vat.bean.init(  
             function(){ //進入action-->performInitial 初始畫method
                    return "process_object_name=prItemAction&process_object_method_name=performInitial"; 
         },{
             other: true
        }
        );
        
	   
  }
  
}
//按鈕初始化
function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Pr_Item:search:20141125.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
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
//單頭初始化
function headerInitial(){ 
	var alldepartment       = vat.bean("alldepartment");
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"請採驗-商品主檔", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.itemNo", type:"LABEL", value:"品號<font color='red'>*</font>"}]},
				{items:[{name:"#F.itemNo", type:"TEXT", bind:"itemNo", size:25, maxLen:25 }]},
				{items:[{name:"#L.department", type:"LABEL" , value:"部門"}]},
				{items:[{name:"#F.department", type:"SELECT", bind:"department",init:alldepartment , size:25, maxLen:25 }]},
				{items:[{name:"#L.enable", type:"LABEL" , value:"狀態"}]},
				{items:[{name:"#F.enable", type:"CHECKBOX",  bind:"enable" },
						{name:"#L.enable", type:"LABEL",value:"起用?", size:15 },
						{name:"#F.itemId", type:"HIDDEN", bind:"itemId"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.itemName", type:"LABEL" , value:"品名"}]},
				{items:[{name:"#F.itemName", type:"TEXT", bind:"itemName", size:25, maxLen:25 }]},
				{items :[{name : "#L.createdBy", type : "LABEL", value : "填單人員<font color='red'></font>"}]},
		{items :[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25,mode:"READONLY"},
				 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}]},
				{items:[{name:"#L.creationDate", type:"LABEL" , value:"建立日期"}]},
				{items:[{name:"#F.creationDate", type:"TEXT", bind:"creationDate",mode:"READONLY", size:15}]}
				
			]}
			
		], 	
		 
		beginService:"",
		closeService:""			
	});
}
//主檔資料
function kweMaster(){
	var isPartialShipments = [["", "", false], ["請選擇","含", "未"],["","1","2"]];
	var allproject    = vat.bean("allproject"); 
	var allsystem     = vat.bean("allsystem"); 
vat.block.create(vnB_master, {
	id: "vatBlock_Master", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[
	{row_style:"", cols:[
	 				{items:[{name:"#L.specInfo", type:"LABEL",  value:"商品規格"}]},	 
	 				{items:[{name:"#F.specInfo", type:"TEXT", bind:"specInfo", size:200 }],td:" colSpan=8"}]},
	{row_style : "", 
			cols : [{items : [{name : "#L.itemBrand", type : "LABEL", value : "商品品牌<font color='red'>*</font>"}]},
					{items : [{name : "#F.itemBrand", type : "TEXT", bind : "itemBrand", size:25}]},
					{items :[{name:"#L.supplier",	type:"LABEL", 	value:"供應商<font color='red'>*</font>" }]},
					{items :[{name:"#F.supplier",	type:"TEXT", 	bind:"supplier",  size:10 },//eChange: function(){ changeSupplierName("supplierCode"); },
							{name:"#B.supplierCode",	value:"選取" ,type:"PICKER" ,
	 									 		openMode:"open", src:"./images/start_node_16.gif",
	 									 		service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 		left:0, right:0, width:1024, height:768,	
	 									 		serviceAfterPick:function(){doAfterPickerFunctionProcess("supplierCode");}},
	 						{name:"#F.addressBookId", 		type:"TEXT",  	bind:"addressBookId",back:false, mode:"HIDDEN"},	
	 						{name:"#L.supplierName", 		type:"LABEL", 	value:"" },				 		
							{name:"#F.supplierName", 		type:"TEXT",  	bind:"supplierName", back:false, mode:"READONLY"}]},	
	 				{items : [{name : "#L.isTax", type : "LABEL", value : "稅別<font color='red'>*</font>"}]},
					{items : [{name : "#F.isTax", type : "SELECT", bind : "isTax",init:isPartialShipments,size:25}]}]},
	     {row_style : "", 
			cols : [{items : [{name : "#L.groupNo", type : "LABEL", value : "商品群組"}]},
					{items : [{name : "#F.groupNo", type : "TEXT", bind : "groupNo", size:25}]},
					{items : [{name : "#L.groupName",  type:"LABEL",  value:"商品名稱"}]},	 
	 				{items : [{name : "#F.groupName",  type:"TEXT",   bind:"groupName",size:25}],td:" colSpan=5"}
	 				]},
	 	{row_style : "", 
			cols : [{items : [{name : "#L.reUnitPrice",  type:"LABEL",  value:"商品價格(高價)"}]},	 
	 				{items : [{name : "#F.reUnitPrice",  type:"TEXT",   bind:"reUnitPrice",size:25}]},
	 				{items : [{name : "#L.purUnitPrice",  type:"LABEL",  value:"商品採購(低價)"}]},	 
	 				{items : [{name : "#F.purUnitPrice",  type:"TEXT",   bind:"purUnitPrice",size:25}],td:" colSpan=5"}
	 				]}
			
			],	
		beginService:"",
		closeService:""
	});
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

// 送出的返回
function createRefreshForm(){
	 vat.item.setValueByName("#L.currentRecord", "0");
     vat.item.setValueByName("#L.maxRecord", "0");
     vat.bean().vatBeanPicker.result = null; 
	refreshForm("");
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
/*function doSubmit(formAction){
	//跳出視窗'是、否'
	var alertMessage ="是否確定送出?";
	//選擇'是'執行Action裡面的performTransaction 送出 
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction 		= formAction;
		vat.block.submit(function(){
				return "process_object_name=buShopAction"+
				"&process_object_method_name=performTransaction";
			},{
				bind:true, link:true, other:true 
			}
		);
	}
}*/
// 送出,暫存按鈕
function doSubmit(formAction){
	
	var alertMessage ="是否確定送出?";
	
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction 		= formAction;
		vat.block.submit(function(){
				return "process_object_name=prItemAction"+
				"&process_object_method_name=performTransaction";
			},{
				bind:true, link:true, other:true 
			}
		);
	}
}
// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	//alert("進入Picker");
	if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].itemId;
		  refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}
}
// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].itemId;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].itemId;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].itemId;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].itemId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"            ].value = code; 
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=prItemAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll();
				doFormAccessControl();
     	}}
    );	
}
function saveBeforeAjxService()
{   alert("saveBeforeAjxService");
	
		var processString = "process_object_name=buShopMainService&process_object_method_name=getAJAXPageData" + 
		                    "&shopCode=" + document.forms[0]["#formId"            ].value+"&shopCode="+vat.item.getValueByName("#F.itemCode");
		                    //alert(processString); 
		return processString;	
								
}
// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter()
{
    return true;
}
// 第一次載入 重新整理
function loadBeforeAjxService(div)
{   alert("---------loadBeforeAjxService:" + vat.bean().vatBeanOther.formId);
	
		var processString = "process_object_name=buShopMainService&process_object_method_name=getAJAXPageData" + 
		                    "&itemCode=" + document.forms[0]["#formId"            ].value+"&shopCode="+vat.item.getValueByName("#F.itemCode");
		                    //alert(processString); 
		return processString;	
								
}
// 依formId鎖form
function doFormAccessControl(){
/*
	var formId = vat.bean().vatBeanOther.formId;
//	alert(typeof formId);
	if( formId != "" ){
		vat.item.setAttributeByName("#F.commissionRate", "readOnly", true);
		
	}else{
		vat.item.setAttributeByName("#F.commissionRate", "readOnly", false);
	}
	
	var enable = vat.item.getValueByName("#F.enable"); 
//	alert("enable = " + enable);
	if(enable == "Y" ){
		vat.item.setValueByName("#F.enable", "N");
	}else{
		vat.item.setValueByName("#F.enable", "Y");
	}
*/		
}
// 取得指定連動的類別下拉
function changeCategory(){
	var parentCategory = vat.item.getValueByName("#F.category01");
	

    vat.ajax.XHRequest(
    {
        post:"process_object_name=prItemService"+
                 "&process_object_method_name=getAJAXCategory"+
                 "&category01=" + parentCategory,
        find: function changeRequestSuccess(oXHR){ 
	var allsystem = eval(vat.ajax.getValue("allsystem", oXHR.responseText));
	allsystem[0][0] = "#F.category02";
	vat.item.SelectBind(allsystem); 
        }   
    }); 
} 
// 供應商picker 回來執行
function doAfterPickerFunctionProcess( code ){
	//do picker back something
	//alert(vat.item.setValueByName("#F.addressBookId", vat.bean().vatBeanPicker.result[0].addressBookId));
	if(vat.bean().vatBeanPicker.result !== null){
	 if( "supplierCode" == code ){
			vat.item.setValueByName("#F.addressBookId", vat.bean().vatBeanPicker.result[0].addressBookId); 
			changeSupplierName("addressBookId");
		}
	}
}
// 動態改變廠商供應商名稱
function changeSupplierName( code ){
	vat.ajax.XHRequest({
		post:"process_object_name=buSupplierWithAddressViewService"+
                  "&process_object_method_name=getAJAXSupplierName"+
                  "&brandCode=" +  vat.bean().vatBeanOther.brandCode + 
                  "&addressBookId=" + ( "addressBookId" === code ? vat.item.getValueByName("#F.addressBookId") : "" )+
                  "&supplierCode=" + ( "supplierCode" === code ? vat.item.getValueByName("#F.supplierCode") : "" ),
              
		find: function change(oXHR){ 
         		vat.item.setValueByName("#F.supplierCode", vat.ajax.getValue("supplierCode", oXHR.responseText));
         		vat.item.setValueByName("#F.supplierName", vat.ajax.getValue("supplierName", oXHR.responseText) );
		},
		fail: function changeError(){
         		vat.item.setValueByName("#F.supplierName", "查無此廠商供應商");
		}   
	});	
}