vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_vatT2 = 2;

function outlineBlock(){

  	formInitial();
	buttonLine();
	headerInitial();
	if (typeof vat.tabm != 'undefined') 
	{
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		
	}
	
	      vat.tabm.createButton(0 ,"xTab1","所屬調度人員" ,"vnB_vatT2" ,"images/tab_standard_price_dark.gif" ,"images/tab_standard_price_light.gif", "", "");
	      kweImVatT1();
	
	doFormAccessControl();
	//var formId = vat.bean().vatBeanOther.formId;
}


function formInitial(){ 

//alert( document.forms[0]["#loginBrandCode" ].value);
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined')
	{

		vat.bean().vatBeanOther =
		{
			loginBrandCode		: document.forms[0]["#loginBrandCode" ].value,
          	loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,

          	formId             	: document.forms[0]["#formId"            ].value,
          	//warehouseId             	: document.forms[0]["#warehouseId"            ].value,
          	currentRecordNumber : 0,
	      	lastRecordNumber    : 0
        };

 	   	vat.bean.init
	   	(	
	   	   function()
	  		{
	  		return "process_object_name=imWarehouseAction&process_object_method_name=performImWareHouseInitial"; 
	    	},{
	    		other: true
	    	}
	    );
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
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 service:"Im_Warehouse:search:20091112.page", 
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          	, type:"LABEL" ,value:"　"},
	 			{name:"#B.export"      , type:"IMG"    ,value:"明細匯出",   src:"./images/button_detail_export.gif" , eClick:"exportFormData('S')"},
	 			{name:"SPACE"          	, type:"LABEL" ,value:"　"},
	 	 		{name:"#B.import" 	   ,type:"PICKER" , value:"明細匯入",  src:"./images/button_detail_import.gif"  , 
	 									 openMode:"open", 
	 									 service:"/erp/fileUpload:standard:2.page",
	 									 servicePassData:function(){ return importFormData(); },
	 									 left:0, right:0, width:600, height:400,	
	 									 serviceAfterPick:function(){
	 									 	changeSupplierCode();
	 									 	//refreshForm("");
	 									 }},
	 	 		{name:"SPACE"          	, type:"LABEL" ,value:"　"},
	 	 		{name:"#B.exit"        	, type:"IMG"   ,value:"離開", 	src:"./images/button_exit.gif",		eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          	, type:"LABEL" ,value:"　"},
	 			{name:"#B.submit"      	, type:"IMG"   ,value:"送出", 	src:"./images/button_submit.gif",	eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   	, type:"LABEL" ,value:"　"},
	 			{name:"#B.first"       	, type:"IMG"   ,value:"第一筆", 	src:"./images/play-first.png", 		eClick:"gotoFirst()"},
	 			{name:"SPACE"          	, type:"LABEL" ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"   ,value:"上一筆", 	src:"./images/play-back.png", 		eClick:"gotoForward()"},
	 			{name:"SPACE"          	, type:"LABEL" ,value:"　"},
	 			{name:"#B.next"        	, type:"IMG"   ,value:"下一筆",  src:"./images/play.png", 			eClick:"gotoNext()"},
	 			{name:"SPACE"          	, type:"LABEL" ,value:"　"},
		 		{name:"#B.last"    		, type:"IMG"   ,value:"最後一筆",	src:"./images/play-forward.png" , 	eClick:"gotoLast()"},
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

function headerInitial(){ 
	//下拉式選單
	var customsWarehouseCode = vat.bean("allCustomsWarehouseCodes");
	var wareHouseArea = vat.bean("wareHouseArea");
	var warehouseTypeId = vat.bean("warehouseTypeId");
	var warehouseTaxArea = vat.bean("allTaxAreaCodes");
	var allContractCodes = vat.bean("allContractCodes");
	var locationId = vat.bean("buLocations");
	var allBusinessTypeCodes = vat.bean("allBusinessTypeCodes");
	var categoryCode = [["", true, false], ["專櫃", "倉庫"], ["S", "W"]];
	var taxTypeCode = [["", true, false], ["完稅及免稅", "完稅", "免稅"], ["M", "T", "F"]];
	
	//表頭主要畫面
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"庫存資料檔維護", 
		rows:
		[
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseCode", type:"LABEL", value:"倉儲代碼<font color='red'>*</font>"}]},
				{items:[{name:"#F.warehouseCode", type:"TEXT", bind:"warehouseCode", size:12, maxLen:12 },
						{name:"#F.enable", type:"CHECKBOX",  bind:"enable" },
						{name:"#L.enable", type:"LABEL",value:"停用?", size:10 },
						{name:"#F.warehouseId", type:"hidden", bind:"warehouseId", size:12, maxLen:12 }
				]},
                {items:[{name:"#L.warehouseName", type:"LABEL", value:"倉庫名稱<font color='red'>*</font>"}]},
                {items:[{name:"#F.warehouseName", type:"TEXT", bind:"warehouseName", size:25, maxLen:25 }]},
                {items:[{name:"#L.warehouseManager", type:"LABEL", value:"倉管人員<font color='red'>*</font>"}]},
                {items:[{name:"#F.warehouseManager", type:"TEXT", bind:"warehouseManager", size:10, maxLen:12 , eChange:'getEmployeeInfo("warehouseManager")'},
                        {name:"#F.warehouseManagerName", type:"TEXT", bind:"warehouseManagerName", mode:"READONLY"}
                ]},
				{items:[{name:"#L.brandName", type:"LABEL", value:"品　　牌"}]},
				{items:[{name:"#F.brandCode",       type:"TEXT",    bind:"brandCode", mode:"HIDDEN" },
                        {name:"#F.brandName",       type:"TEXT",    bind:"brandName", back:false, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.storage", type:"LABEL", value:"庫別<font color='red'>*</font>"}]},
				{items:[{name:"#F.storage", type:"TEXT", bind:"storage", size:10, maxLen:25 }]},
				{items:[{name:"#L.storageArea", type:"LABEL", value:"儲區<font color='red'>*</font>"}]},
				{items:[{name:"#F.storageArea", type:"TEXT", bind:"storageArea", size:10, maxLen:12 }]},
				{items:[{name:"#L.storageBin", type:"LABEL", value:"儲位<font color='red'>*</font>"}]},
				{items:[{name:"#F.storageBin", type:"TEXT", bind:"storageBin", size:10, maxLen:12 }]},
                {items:[{name:"#L.warehouseCapacity", type:"LABEL", value:"儲位大小<font color='red'>*</font>"}]},
                {items:[{name:"#F.warehouseCapacity", type:"TEXT", bind:"warehouseCapacity", size:10, maxLen:25 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseTypeId", type:"LABEL", value:"類別<font color='red'>*</font>"}]},
				{items:[{name:"#F.warehouseTypeId", type:"SELECT", bind:"warehouseTypeId", init:warehouseTypeId, size:10, maxLen:25 }]},
				{items:[{name:"#L.categoryCode", type:"LABEL", value:"倉庫類型<font color='red'>*</font>"}]},
				{items:[{name:"#F.categoryCode", type:"SELECT", bind:"categoryCode", init:categoryCode, size:10, maxLen:12 }]},
				{items:[{name:"#L.locationId", type:"LABEL", value:"地點<font color='red'>*</font>"}]},
				{items:[{name:"#F.locationId", type:"SELECT", bind:"locationId", init:locationId }]},
                {items:[{name:"#L.taxTypeCode", type:"LABEL", value:"稅別<font color='red'>*</font>"}]},
                {items:[{name:"#F.taxTypeCode", type:"SELECT", bind:"taxTypeCode", init:taxTypeCode, size:10, maxLen:4 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customsWarehouseCode", type:"LABEL", value:"海關倉庫代號<font color='red'>*</font>"}]},
				{items:[{name:"#F.customsWarehouseCode", type:"SELECT", bind:"customsWarehouseCode",init:customsWarehouseCode }]},
				{items:[{name:"#L.warehouseArea", type:"LABEL", value:"倉庫區域<font color='red'>*</font>"}]},
				{items:[{name:"#F.warehouseArea", type:"TEXT", bind:"warehouseArea" }]},
				{items:[{name:"#L.ioArea", type:"LABEL", value:"出入境<font color='red'>*</font>"}]},
				{items:[{name:"#F.ioArea", type:"TEXT", bind:"ioArea", size:10, maxLen:12 }]},
                {items:[{name:"#L.contractCode", type:"LABEL", value:"合約代號<font color='red'>*</font>"}]},
                {items:[{name:"#F.contractCode", type:"SELECT", bind:"contractCode", init:allContractCodes }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.companyCode", type:"LABEL", value:"公司代號<font color='red'>*</font>"}]},
				{items:[{name:"#F.companyCode", type:"TEXT", bind:"companyCode", size:10, maxLen:25 }]},
				{items:[{name:"#L.taxAreaCode", type:"LABEL", value:"報稅區域<font color='red'>*</font>"}]},
				{items:[{name:"#F.taxAreaCode", type:"SELECT", bind:"taxAreaCode", init:warehouseTaxArea}]},
				{items:[{name:"#L.businessTypeCode", type:"LABEL", value:"營業項目<font color='red'>*</font>"}]},
				{items:[{name:"#F.businessTypeCode", type:"SELECT", bind:"businessTypeCode",init:allBusinessTypeCodes }]},
                {items:[{name:"#L.allowMinusStock", type:"LABEL", value:"允許負庫存<font color='red'>*</font>"}]},
                {items:[{name:"#F.allowMinusStock", type:"TEXT", bind:"allowMinusStock", init:taxTypeCode, size:10, maxLen:4 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.financeSalesType", type:"LABEL", value:"財務業種<font color='red'>*</font>"}]},
                {items:[{name:"#F.financeSalesType", type:"TEXT", bind:"financeSalesType", init:taxTypeCode, size:10, maxLen:4 }]},
                {items:[{name:"#L.customsArea", type:"LABEL", value:"海關結帳報表區域<font color='red'>*</font>"}]},
                {items:[{name:"#F.customsArea", type:"TEXT", bind:"customsArea", init:taxTypeCode, size:10, maxLen:4 }]}
			]}
		], 
		beginService:"",
		closeService:""			
	});
}

function kweImVatT1()
{
	
	var vbCanGridDelete = true;
	var vbCanGridAppend = true;
	var vbCanGridModify = true;
  
  	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
  	vat.item.make(vnB_vatT2, "indexNo", {type:"IDX"  , desc:"序號" });
 	vat.item.make(vnB_vatT2, "enable", {type:"CHECKBOX" , size:12, maxLen:12, desc:"啟用"});
	vat.item.make(vnB_vatT2, "id.employeeCode", {type:"TEXT" , size:10, maxLen:6, desc:"工號"  ,eChange: function(){ changeEmployCodeByName(); } });
	vat.item.make(vnB_vatT2, "employeeName", {type:"TEXT" , mode:"READONLY", size:10, desc:"姓名"});
	//vat.item.make(vnB_vatT2, "isDeleteRecord"            , {type:"DEL"  , desc:"刪除"});
	
	vat.block.pageLayout(vnB_vatT2, {
						id: "vnB_vatT2",
						pageSize: 10,											
	           			canGridDelete : vbCanGridDelete,
						canGridAppend : vbCanGridAppend,
						canGridModify : vbCanGridModify,						
						appendBeforeService : "appendBeforeService()",
						saveBeforeAjxService: "saveBeforeAjxService("+vnB_vatT2+")",
						loadBeforeAjxService: "loadBeforeAjxService("+vnB_vatT2+")",
						saveSuccessAfter    : "saveSuccessAfter("+vnB_vatT2+")"
						});
	vat.block.pageDataLoad(vnB_vatT2, vnCurrentPage = 1);
}

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div)
{
    return true;
}

// 新增空白頁
function appendBeforeService()
{
	return true;
}

// 第一次載入 重新整理
function loadBeforeAjxService(div)
{   
    var processString = "process_object_name=imWarehouseService&process_object_method_name=getAJAXPageData" + 
		                    "&warehouseCode="+vat.item.getValueByName("#F.warehouseCode") +
		                    "&warehouseId="+vat.item.getValueByName("#F.warehouseId");
		                    //alert(processString); 
	return processString;	
								
}

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div)
{
  // alert("進入saveBeforeAjxService:"+ vat.item.getValueByName("#F.warehouseCode"));
	
	   //updateAJAXPageLinesData
            var processString = "process_object_name=imWarehouseService&process_object_method_name=updateOrSaveAJAXPageLinesData" + 
                                "&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode") +
                                "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value +
                                "&warehouseId=" + document.forms[0]["#formId" ].value +
                                "&enable=" + vat.item.getValueByName("#F.enable") ;
                                
            return processString;
		
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
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	
	if(confirm(alertMessage))
	{
	   vat.block.pageDataSave(vnB_vatT2, {  funcSuccess:function(){
		    vat.bean().vatBeanOther.formAction = formAction;
			vat.block.submit(function(){
				return "process_object_name=imWarehouseAction&process_object_method_name=performImWareHouseTransaction";
			},{
			bind:true, link:true, other:true,
			funcSuccess: function () {
               
            } 
		});
		
	}
	});
	}
	
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	//alert("進入Picker");
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
          var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].warehouseId;
         //alert("warehouseCode:"+code);
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
        var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].warehouseId;
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
        var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].warehouseId;
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
        var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].warehouseId;
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
        var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].warehouseId;
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
            return "process_object_name=imWarehouseAction&process_object_method_name=performImWareHouseInitial"; 
        },{other      : true, 
           funcSuccess:function(){
                vat.item.bindAll();
                vat.block.pageRefresh(vnB_vatT2); 
                
                //loadBeforeAjxService(code); 
                doFormAccessControl();
        }}
    );
     
}

// 依formId鎖form
function doFormAccessControl(){
	var formId = vat.bean().vatBeanOther.formId;
	var enable = vat.item.getValueByName("#F.enable"); 
	
	if( formId != "" )
	{
		//alert("yes");
		vat.item.setAttributeByName("#F.warehouseCode", "readOnly", true);
		vat.item.setAttributeByName("vnB_vatT2", "readOnly", false);
		//vat.block.canGridModify(vnB_vatT2, true);
		//vat.block.canGridModify(vnB_vatT2, true);
		vat.item.setGridStyleByName("employeeCode", "readOnly", false);
		vat.item.setGridStyleByName("employeeName", "readOnly", false);
		vat.item.setGridStyleByName("enable", "readOnly", false);
	}
	else
	{
	    //alert("no");
		//vat.item.setAttributeByName("#F.warehouseCode", "readOnly", false);
		vat.item.setAttributeByName("vnB_vatT2", "readOnly", true);
		//vat.item.setStyleByName("vnB_vatT2","display","none");
		//vat.item.setGridStyleByName("employeeCode", "display", "none");
		//vat.item.setStyleByName("vnB_vatT2","display","");
		//vat.item.setStyleByName("vnB_vatT2","readOnly",true);
		//vat.block.canGridModify(vnB_vatT2, false);
		
		//vat.item.setGridStyleByName("employeeCode", "readOnly", true);
		//vat.item.setGridStyleByName("employeeName", "readOnly", true);
		//vat.item.setGridStyleByName("enable", "readOnly", true);
	}
	
	if(enable == "Y"){
		vat.item.setValueByName("#F.enable", "N");
	}
	else{
		vat.item.setValueByName("#F.enable", "Y");
	}		
}

function changeEmployCodeByName()
{
    var same = 0;//0:不同　1：相同
	var nItemLine = vat.item.getGridLine();
	var vemployeeCode = vat.item.getGridValueByName("id.employeeCode"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
		
	for(var i=1 ; i<nItemLine ; i++){
		var employee = vat.item.getGridValueByName("id.employeeCode"	, i).replace(/^\s+|\s+$/, '').toUpperCase();
		if(vemployeeCode  == employee){
			vat.item.setGridValueByName("id.employeeCode", nItemLine, "");
			same = 1;
		}
	}
	if(same == 0){
	
		vat.item.setGridValueByName("id.employeeCode", nItemLine, vemployeeCode);
		
		vat.ajax.XHRequest(
       	{
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + vemployeeCode,
           find: function changeSuperintendentRequestSuccess(oXHR){ 
               vat.item.setGridValueByName("employeeName", nItemLine, vat.ajax.getValue("EmployeeName", oXHR.responseText));
               vat.item.setGridValueByName("status", nItemLine, "未驗證");
        }   
       });
      
     }	
}

function exportFormData(arg){
     //alert("export to xml file...")
	var beanName = "IM_WAREHOUSE_EMPLOYEE";
	
	
	// var customer = vat.utils.escape(RTrim(vat.item.getValueByName("#F.customerName")));
	
	//var customer = (RTrim(vat.item.getValueByName("#F.customerName")));
	var url;
    	url = "/erp/jsp/ExportFormView.jsp" +
              "?exportBeanName="+ beanName +
              "&fileType=XLS" +
              "&processObjectName=imWarehouseService" +
              "&processObjectMethodName=findViewByMap" +
                      "&warehouseCode"         + "=" + vat.item.getValueByName("#F.warehouseCode") +     
	                  "&QueryType=ExcelExport" 
    var width = "200";
    var height = "30";
    url = encodeURI(encodeURI(url));
    // alert(url + "/customer:" + customer + "/");
    vat.block.pageSearch(vnB_vatT2, {
    		funcSuccess : function(){
    			// alert('success for excel export');
    			window.open(url, '庫別人員匯出', 'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2)}});

}

// 明細匯入
function importFormData(){
	//alert(vat.item.getValueByName("#F.warehouseCode"));
	
	var exportBeanName =  "IM_WAREHOUSE_EMPLOYEE_IMPORT";
	//var exportBeanName =  "IM_WAREHOUSE_EMPLOYEE";
	var width = "600";
    var height = "400";
    
	//window.open(
		//"/erp/fileUpload:standard:2.page" +
	var returnData = "&importBeanName=" + exportBeanName +
		"&importFileType=XLS" +
        "&processObjectName=imWarehouseService" + 
        "&processObjectMethodName=executeImportImwareHouseEmps" +
        "&arguments=" + vat.item.getValueByName("#F.warehouseCode") +
        "&parameterTypes=String" +
        "&blockId="+ vnB_vatT2;
        
		///"FileUpload",
		//'menubar=no,resizable=no,scrollbars=no,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
	return 	returnData;
	
}

function getEmployeeInfo(vsEmployee) {
    if ("" != vsEmployee, vat.item.getValueByName("#F." + vsEmployee)) {
        vat.item.setValueByName("#F." + vsEmployee, vat.item.getValueByName("#F." + vsEmployee).toUpperCase());
        var processString = "process_sql_code=FindEmployeeChineseName&employeeCode=" + vat.item.getValueByName("#F." + vsEmployee);
        vat.ajax.startRequest(processString, function () {
            if (vat.ajax.handleState()) {
                if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
                    vat.item.setValueByName("#F." + vsEmployee + "Name", vat.ajax.getValue("CHINESE_NAME", vat.ajax.xmlHttp.responseText));
                } else {
                    vat.item.setValueByName("#F." + vsEmployee + "Name", "");
                    alert("查無此員工代號");
                }
            }
        });
    }
 function changeSupplierCode(){
 
 alert("匯入成功");
 }
}
