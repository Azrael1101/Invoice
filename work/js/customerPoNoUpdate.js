/*** 
 *	檔案: customerPoNoUpdate.js
 *	說明：售貨單更新
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;
var isChangeNumber = "false";

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
  	
  	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		vat.tabm.createButton(0 ,"xTab1","明細檔" ,"vatDetailDiv"        		,"images/tab_detail_data_dark.gif"      	,"images/tab_detail_data_light.gif", false, "doPageDataSave("+vnB_Detail+")" );
	}
  	
  	detailInitial();
	doFormAccessControl();
}

function doPageDataSave(div){
    vat.block.pageRefresh(div); 
}


function formInitial(){ 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          userType				: document.forms[0]["#userType" ].value
        };
        
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=soSalesOrderMainAction&process_object_method_name=performCustomerPoNoInitial"; 
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
	 	{items:[
	 			{name:"#B.search"      	, type:"IMG"      , value:"查詢",  src:"./images/button_find.gif"	, eClick:"doSearch()"},
	 			{name:"SPACE"          	, type:"LABEL"    , value:"　"},
	 			{name:"#B.changeNumber" , type:"IMG"      , value:"換號碼",  src:"./images/button_change_number.gif"	, eClick:"changNumber()"},
	 			{name:"SPACE"          	, type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}	

function headerInitial(){ 
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"銷售售貨單更新維護作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL",  value:"品牌" }]},
				{items:[{name:"#L.brandName", type:"TEXT", bind:"brandName", mode:"READONLY" },
						{name:"#F.brandCode", type:"TEXT", bind:"brandCode", mode:"HIDDEN", back:false }]},
				{items:[{name:"#L.loginEmployeeCode", type:"LABEL", value:"登入人員" }]},
				{items:[{name:"#F.loginEmployeeCode", type:"TEXT", bind:"loginEmployeeCode", back:false, mode:"HIDDEN" },
						{name:"#F.loginEmployeeName", type:"TEXT", bind:"loginEmployeeName", back:false, mode:"READONLY" }]}		
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.salesOrderDate", type:"LABEL", value:"交易日期<font color='red'>*</font>" }]},
				{items:[{name:"#F.salesOrderDate", type:"DATE", bind:"salesOrderDate", eChange:"changeCustomerPoNo()", back:false }]},
				{items:[{name:"#L.posMachineCode", type:"LABEL", value:"機台<font color='red'>*</font>" }]},
				{items:[{name:"#F.posMachineCode", type:"TEXT", bind:"posMachineCode", eChange:"changeCustomerPoNo()", back:false }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.customerPoNoStart", type:"LABEL", value:"售貨單起(舊)<font color='red'>*</font>"}]},
				{items:[{name:"#F.customerPoNoStart", type:"TEXT", bind:"customerPoNoStart", eChange:"changeCustomerPoNo('changeEnd')", size:20, back:false },
						{name:"#F.between", type:"LABEL", value:" 至 " },
						{name:"#F.customerPoNoEnd", type:"TEXT", bind:"customerPoNoEnd", eChange:"changeCustomerPoNo()", size:20, back:false},
						{name:"#F.memo", type:"TEXT", bind:"memo", mode:"READONLY", size:100, back:false }],td:" colSpan=3"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.accessNumber", type:"LABEL", value:"增減數字<font color='red'>*</font>" }]},
				{items:[{name:"#F.accessNumber", type:"NUMB", bind:"accessNumber", back:false, eChange:"changeAccessNumber()" }]},
				{items:[{name:"#L.prefixCustomerPoNo", type:"LABEL", value:"換字軌" }]},
				{items:[{name:"#F.prefixCustomerPoNo", type:"TEXT", bind:"prefixCustomerPoNo", eChange:"changePrefixCustomerPoNo()", back:false },
						{name:"#L.maxPoNoLength", type:"LABEL", value:"售貨單號最大長度 : " },
						{name:"#F.maxPoNoLength", type:"TEXT", bind:"maxPoNoLength", mode:"READONLY", back:false, eChange:"changeMaxPoNoLength()" }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.newCustomerPoNoStart", type:"LABEL", value:"售貨單起(新)"}]},
				{items:[{name:"#F.newCustomerPoNoStart", type:"TEXT", bind:"newCustomerPoNoStart", mode:"READONLY", size:20, back:false },
						{name:"#F.between", type:"LABEL", value:" 至  " },
						{name:"#F.newCustomerPoNoEnd", type:"TEXT", bind:"newCustomerPoNoEnd", mode:"READONLY", size:20, back:false},
						{name:"#F.errorMemo", type:"TEXT", bind:"errorMemo", mode:"READONLY", size:100, back:false}],td:" colSpan=3"}
			]}
			
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

// 銷貨明細檔
function detailInitial(){
	 
	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;    
	 
	vat.item.make(vnB_Detail, "indexNo"         		, {type:"IDX"});
	vat.item.make(vnB_Detail, "orderTypeCode"   		, {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"單別"      });
	vat.item.make(vnB_Detail, "orderNo"     			, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"單號"      });
	vat.item.make(vnB_Detail, "salesOrderDate"  		, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:" 交易日期"      });
	vat.item.make(vnB_Detail, "customerPoNo"     		, {type:"TEXT" , size:10, maxLen:20, mode:"READONLY", desc:"售貨單號"      });
	vat.item.make(vnB_Detail, "defaultWarehouseCode"	, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:"庫別"      });
	vat.item.make(vnB_Detail, "posMachineCode"     		, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:"機台號碼"      });
	vat.item.make(vnB_Detail, "statusName"     			, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "lastUpdatedBy"     		, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:"最後更新人員"      });
	vat.item.make(vnB_Detail, "lastUpdateDate"     		, {type:"TEXT" , size:8, maxLen:20, mode:"READONLY", desc:"更新日期"      });
	vat.item.make(vnB_Detail, "headId"          		, {type:"ROWID"});
	
	vat.block.pageLayout(vnB_Detail, {
														id: "vatDetailDiv",
														pageSize: 10,	
														searchKey           : ["headId"],
														selectionType       : "NONE",
														indexType           : "AUTO",										
								            			canGridDelete : false,
														canGridAppend : false,
														canGridModify : false,						
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail+")",
														loadSuccessAfter    : "loadSuccessAfter("+vnB_Detail+")",						
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Detail+")",
														saveSuccessAfter    : "saveSuccessAfter()",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});
//	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

// 查詢
function loadBeforeAjxService(){
	var processString = "process_object_name=soSalesOrderMainService&process_object_method_name=getAJAXSearchCustomerPoNoPageData" +
						"&brandCode=" + vat.item.getValueByName("#F.brandCode") +
						"&salesOrderDate=" + vat.item.getValueByName("#F.salesOrderDate") +
						"&posMachineCode=" + vat.item.getValueByName("#F.posMachineCode") +
						"&customerPoNoStart=" + vat.item.getValueByName("#F.customerPoNoStart") +
						"&customerPoNoEnd=" + vat.item.getValueByName("#F.customerPoNoEnd") +
						"&isChangeNumber=" + isChangeNumber +
						"&accessNumber=" + vat.item.getValueByName("#F.accessNumber") +
						"&prefixCustomerPoNo=" + vat.item.getValueByName("#F.prefixCustomerPoNo")+
						"&maxPoNoLength=" + vat.item.getValueByName("#F.maxPoNoLength");
	return processString;											
}

// saveSuccessAfter 之後呼叫 載入資料成功
function loadSuccessAfter(div){
	if( vat.block.getGridObject(div).dataCount == vat.block.getGridObject(div).pageSize &&
	    vat.block.getGridObject(div).lastIndex == 1){
		alert("您輸入條件查無資料");
	}
} 

// 第一頁 翻到前或後頁 最後一頁	
function saveBeforeAjxService(div){
	var processString = "process_object_name=generateBarCodeService&process_object_method_name=updateAJAXPageLinesData";
	return processString;
} 

// 存檔成功後呼叫,主要為了tab切換相關計算
function saveSuccessAfter(div){ 
	vat.block.pageRefresh(div); 
} 

// 送出的返回
function createRefreshForm(){
	refreshForm("");
}


// 打開增減數字
function changeCustomerPoNo(changeEnd){
	var customerPoNoStart = vat.item.getValueByName("#F.customerPoNoStart").replace(/^\s+|\s+$/, '').toUpperCase();
	var customerPoNoEnd = vat.item.getValueByName("#F.customerPoNoEnd").replace(/^\s+|\s+$/, '').toUpperCase();
	var salesOrderDate = vat.item.getValueByName("#F.salesOrderDate");
	var posMachineCode = vat.item.getValueByName("#F.posMachineCode");
	
	if( changeEnd == "changeEnd" ){
		customerPoNoEnd = customerPoNoStart;
	}
	
	vat.item.setValueByName("#F.customerPoNoStart", customerPoNoStart);
	vat.item.setValueByName("#F.customerPoNoEnd", customerPoNoEnd);
	if( customerPoNoStart != "" && customerPoNoEnd != "" && salesOrderDate != "" && posMachineCode != ""){
		vat.item.setAttributeByName("#F.accessNumber", "readOnly", false); 
		vat.item.setAttributeByName("#F.prefixCustomerPoNo", "readOnly", false); 
		vat.ajax.XHRequest({
	          post:"process_object_name=soSalesOrderMainService"+
	                   "&process_object_method_name=getAJAXMemo"+
	                   "&customerPoNoStart=" + customerPoNoStart +
	                   "&customerPoNoEnd=" + customerPoNoEnd +
	                   "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
	                   "&salesOrderDate=" + salesOrderDate +
	                   "&posMachineCode=" + posMachineCode,
	          find: function change(oXHR){ 
	          	vat.item.setValueByName("#F.memo", vat.ajax.getValue("memo", oXHR.responseText));
	          	vat.item.setValueByName("#F.errorMemo", vat.ajax.getValue("errorMemo", oXHR.responseText));
	          	
	          	if( vat.item.getValueByName("#F.accessNumber") != "" ){
	          		changeAccessNumber();
	          	}
	          }  
		});	
		
	}else if( customerPoNoStart == "" || customerPoNoEnd == "" || salesOrderDate == "" || posMachineCode == "" ){
		vat.item.setValueByName("#F.newCustomerPoNoStart", "");
	    vat.item.setValueByName("#F.newCustomerPoNoEnd", "");
	    vat.item.setValueByName("#F.accessNumber", "");
	    vat.item.setValueByName("#F.prefixCustomerPoNo", ""); 
	    vat.item.setValueByName("#F.memo", ""); 
	    vat.item.setValueByName("#F.errorMemo", ""); 
	    vat.item.setAttributeByName("#F.prefixCustomerPoNo", "readOnly", true); 
	}
}

// 增減數字
function changeAccessNumber(){
	var accessNumber = vat.item.getValueByName("#F.accessNumber");
	var maxPoNoLength = vat.item.getValueByName("#F.maxPoNoLength");
	if(accessNumber != ""){
		vat.ajax.XHRequest({
	          post:"process_object_name=soSalesOrderMainService"+
	                   "&process_object_method_name=getAJAXCustomerPoNo"+
	                   "&customerPoNoStart=" + vat.item.getValueByName("#F.customerPoNoStart") +
	                   "&customerPoNoEnd=" + vat.item.getValueByName("#F.customerPoNoEnd") +
	                   "&accessNumber=" + accessNumber+
	                   "&maxPoNoLength=" +maxPoNoLength,
	          find: function change(oXHR){ 
	          	vat.item.setValueByName("#F.newCustomerPoNoStart", vat.ajax.getValue("newCustomerPoNoStart", oXHR.responseText));
	          	vat.item.setValueByName("#F.newCustomerPoNoEnd", vat.ajax.getValue("newCustomerPoNoEnd", oXHR.responseText));
	          	var newCustomerPoNoStart = vat.item.getValueByName("#F.newCustomerPoNoStart");
	          	var newCustomerPoNoEnd = vat.item.getValueByName("#F.newCustomerPoNoEnd");
//	          	if(newCustomerPoNoStart != "" && newCustomerPoNoEnd != "" ){
	          		
//	          	}
	          	changePrefixCustomerPoNo();
	          }  
		});	
	}else{
		vat.item.setValueByName("#F.newCustomerPoNoStart", "");
	    vat.item.setValueByName("#F.newCustomerPoNoEnd", "");
	    vat.item.setValueByName("#F.prefixCustomerPoNo", ""); 
//	    vat.item.setAttributeByName("#F.prefixCustomerPoNo", "readOnly", true); 
	}
}


// 字軌改變
function changePrefixCustomerPoNo(){
	var prefixCustomerPoNo = vat.item.getValueByName("#F.prefixCustomerPoNo").replace(/^\s+|\s+$/, '').toUpperCase();
	var newCustomerPoNoStart = vat.item.getValueByName("#F.newCustomerPoNoStart");
	var newCustomerPoNoEnd = vat.item.getValueByName("#F.newCustomerPoNoEnd");
	var customerPoNoStart = vat.item.getValueByName("#F.customerPoNoStart");
	var customerPoNoEnd = vat.item.getValueByName("#F.customerPoNoEnd");
	var accessNumber = vat.item.getValueByName("#F.accessNumber");
	
	vat.item.setValueByName("#F.prefixCustomerPoNo", prefixCustomerPoNo);
	
	var maxPoNoLength = vat.item.getValueByName("#F.maxPoNoLength");
	
		vat.ajax.XHRequest({
	          post:"process_object_name=soSalesOrderMainService"+
	                   "&process_object_method_name=getAJAXPrefixCustomerPoNo"+
	                   "&newCustomerPoNoStart=" + newCustomerPoNoStart +
	                   "&newCustomerPoNoEnd=" + newCustomerPoNoEnd+
	                   "&prefixCustomerPoNo=" + prefixCustomerPoNo+
	                   "&customerPoNoStart=" + customerPoNoStart+
	                   "&customerPoNoEnd=" + customerPoNoEnd+
	                   "&accessNumber=" + accessNumber +
	                   "&maxPoNoLength=" +maxPoNoLength,
	          find: function change(oXHR){ 
	          	vat.item.setValueByName("#F.newCustomerPoNoStart", vat.ajax.getValue("newCustomerPoNoStart", oXHR.responseText));
	          	vat.item.setValueByName("#F.newCustomerPoNoEnd", vat.ajax.getValue("newCustomerPoNoEnd", oXHR.responseText));
	          	
	          }  
		});	
	
}

// 改變最大長度
function changeMaxPoNoLength(){
	var maxPoNoLength = vat.item.getValueByName("#F.maxPoNoLength");
	// 設定預設最大長度
	vat.item.setAttributeByName("#F.customerPoNoStart", "maxLength", maxPoNoLength);
	vat.item.setAttributeByName("#F.customerPoNoEnd", "maxLength", maxPoNoLength);
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
	
	if(confirm(alertMessage)){
		var msg = checkData();
		if(msg == "" ){
			vat.bean().vatBeanOther.formAction 		= formAction;
			vat.block.submit(function(){
					return "process_object_name=soSalesOrderMainAction"+
					"&process_object_method_name=performCustomerPoNoUpdate";
				},{
					bind:true, link:true, other:true//,
					//funcSuccess:function(){
					//	vat.block.pageRefresh(vnB_Detail);
					//}
				} 
			);
		}else{
			alert(msg);
		}
	}
}

//js 檢查
function checkData(){
	var msg = "";
	// 交易日期 售貨單起(舊)起迄 增減數字
	var salesOrderDate =  vat.item.getValueByName("#F.salesOrderDate");
	var customerPoNoStart =  vat.item.getValueByName("#F.customerPoNoStart");
	var customerPoNoEnd =  vat.item.getValueByName("#F.customerPoNoEnd");
	var accessNumber =  vat.item.getValueByName("#F.accessNumber");
	var posMachineCode =  vat.item.getValueByName("#F.posMachineCode");
	var newCustomerPoNoStart = vat.item.getValueByName("#F.newCustomerPoNoStart");
	var newCustomerPoNoEnd = vat.item.getValueByName("#F.newCustomerPoNoEnd");
	var prefixCustomerPoNo = vat.item.getValueByName("#F.prefixCustomerPoNo");
	var maxPoNoLength = vat.item.getValueByName("#F.maxPoNoLength");
	/**
     * 從後面數來找到不等於數字的索引
     * @param doNumber
     * @return
     */
    function getPrefixIndex(customerPoNo){
		var split = 0;
		for (var i = customerPoNo.length-1 ; i > 0; i--) {
			var reTmp = new RegExp("\\d", "g");
			if(!reTmp.test(customerPoNo.substring(i-1, i))){
				split = i;
				break;
			}
		}
		return split;
    };
	
	var split = getPrefixIndex(customerPoNoStart); // 分隔點
	if("" != prefixCustomerPoNo){
		split = prefixCustomerPoNo.length; 
	} 
	var numberStr = maxPoNoLength - split;   // 取得剩餘的數字長度
	
	if( salesOrderDate == "" ){
		msg += "請輸入交易日期";
	}
	if( posMachineCode == "" ){
		msg += "\n請輸入機台";
	}
	if( customerPoNoStart == "" ){
		msg += "\n請輸入售貨單起";
	}
	if( customerPoNoEnd == "" ){
		msg += "\n請輸入售貨單迄";
	}
	if( accessNumber == "" && prefixCustomerPoNo == ""){
		msg += "\n請輸入增減數字";
	}
	
//	var re = /^[A-Z]{"+split+"}\\d{"+numberStr+"}$/;
	var re = new RegExp("^[A-Z]{"+split+"}\\d{"+numberStr+"}$", "g");
	if(!re.test(newCustomerPoNoStart)){
		msg += "\n"+newCustomerPoNoStart+"售貨單起前"+split+"碼必須為英文字母，後"+numberStr+"碼為數字";
	}
	var re2 = new RegExp("^[A-Z]{"+split+"}\\d{"+numberStr+"}$", "g");
	if(!re2.test(newCustomerPoNoEnd)){
		msg += "\n"+newCustomerPoNoEnd+"售貨單迄前"+split+"碼必須為英文字母，後"+numberStr+"碼為數字";
	}
	
	if( customerPoNoStart != ""  && customerPoNoEnd != ""  ){
		if(customerPoNoStart.substring(0,split) != customerPoNoEnd.substring(0,split)){
			msg += "\n售貨單起迄前"+split+"碼必須相同";
		}
	}
	
	return msg;
}

// 刷新頁面
function refreshForm(code){
	
	vat.block.submit(
		function(){
			return "process_object_name=buBasicDataAction&process_object_method_name=performBuCountryInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				doFormAccessControl();
     	}}
    );	
}

// 鎖form
function doFormAccessControl(){
	var maxPoNoLength = vat.item.getValueByName("#F.maxPoNoLength");
	var userType = document.forms[0]["#userType" ].value;
	// 設定預設最大長度
	vat.item.setAttributeByName("#F.customerPoNoStart", "maxLength", maxPoNoLength);
	vat.item.setAttributeByName("#F.customerPoNoEnd", "maxLength", maxPoNoLength);
	
	// 增減數字,字軌唯讀
	vat.item.setAttributeByName("#F.accessNumber", "readOnly", true); 
	vat.item.setAttributeByName("#F.prefixCustomerPoNo", "readOnly", true); 
	
	// 若含maxLen則可以設定最大長度
	if("maxLen" == userType ){
		vat.item.setAttributeByName("#F.maxPoNoLength", "readOnly", false); 
	}
	
}

//查詢
function doSearch(){
	isChangeNumber = "false";
	vat.block.pageRefresh(vnB_Detail);
}

//換號碼
function changNumber(){
	isChangeNumber = "true";
	vat.block.pageRefresh(vnB_Detail);
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.salesOrderDate", "");
	vat.item.setValueByName("#F.posMachineCode", "");
	vat.item.setValueByName("#F.customerPoNoStart", "");
	vat.item.setValueByName("#F.customerPoNoEnd", "");
	vat.item.setValueByName("#F.accessNumber", "");
	vat.item.setValueByName("#F.newCustomerPoNoStart", "");
	vat.item.setValueByName("#F.newCustomerPoNoEnd", "");
	vat.item.setValueByName("#F.prefixCustomerPoNo", "");
	
	vat.item.setValueByName("#F.memo", ""); 
	vat.item.setValueByName("#F.errorMemo", ""); 
	vat.item.setAttributeByName("#F.accessNumber", "readOnly", true);
	vat.item.setAttributeByName("#F.prefixCustomerPoNo", "readOnly", true); 
}

// 開啟視窗
function openModifyPage(){

    var nItemLine = vat.item.getGridLine();

    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
	var orderTypeCode = vat.item.getGridValueByName("orderTypeCode", nItemLine);
	
	if(!(vFormId == "" || vFormId == "0")){
    var url = "/erp/So_SalesOrder:create:20100101.page?formId=" + vFormId + "&orderTypeCode="+orderTypeCode; 

     sc=window.open(url, '銷貨單維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
    } 
}
