vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
	doFormAccessControl();		
}

function formInitial(){ 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          orderTypeCode      : document.forms[0]["#orderTypeCode"].value,
          formId             : document.forms[0]["#formId"].value,
          //companyCode  		: "",      
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
        
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=imReceiveExpenseAction&process_object_method_name=performInitial"; 
	    	},{
	    		bind:true, link:true, other:true
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
	 		//	{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 		//							 openMode:"open", 
	 		//							 service:"Bu_Company:search:20131024.page", 
	 		//							 left:0, right:0, width:1024, height:768,	
	 		//							 serviceAfterPick:function(){doAfterPickerProcess();}},
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
	//vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	//vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

	function headerInitial(){ 
	var allExpenseCode = vat.bean("allExpenseCode");
	var allOrderType   = [[true,true], ["EIF-保稅進貨單","EIP-完稅進貨單"],["EIF","EIP"]];
	var allOrderTypeCode   = vat.bean("allOrderType");
		vat.block.create( vnB_Header, { //vnB_Header = 
			id: "vatBlock_Head", generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"進貨費用調整作業", 
			rows:[
				{row_style:"", cols:[
					{items:[{name:"#L.orderTypeCode", type:"LABEL", value:"類別<font color='red'>*</font>" }]},
					{items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode" , size:10, maxLen:25,init:allOrderType }]},
				  	{items:[{name:"#L.orderNo", type:"LABEL", value:"單號<font color='red'>*</font>" }]},
					{items:[{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:25, maxLen:40  }]},
					//{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},
	 				//{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:22, maxLen:25,mode:"readOnly" }]},							
				  	{items:[{name:"#L.supplierCode", type:"LABEL", value:"廠商代號<font color='red'>*</font>" }]},
					{items:[{name:"#F.supplierCode",     type:"TEXT",   bind:"supplierCode",     size:12, mask:"CCCCCCCCCCCCCCCCCCCC", eChange:"onChangeSupplierCode()" },
							{name:"#B.supplierCode",	 type:"PICKER", value:"PICKER", src:"./images/start_node_16.gif",
	 									 		 openMode:"open", 
	 									 		 service:"Bu_AddressBook:searchSupplier:20091011.page", 
	 									 		 left:0, right:0, width:1024, height:768,	
	 									 		 serviceAfterPick:function(X){doAfterPickerSupplier();}}]},
				  			{items:[{name:"#L.supplierName", type:"LABEL" , value:"廠商品稱"}]},
	 						{items:[{name:"#F.supplierName", type:"TEXT", bind:"supplierName", size:30, maxLen:30,mode:"readOnly" }]},
	 						{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌"}]},
	 						{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:22, maxLen:25,mode:"readOnly" }]}						
				]},
				
				{row_style:"", cols:[
					{items:[{name:"#L.expenseCode", type:"LABEL", value:"費用類型" }]},
					{items:[{name:"#F.expenseCode", type:"SELECT", bind:"expenseCode",init:allExpenseCode, size:25, maxLen:40 }]},  	
				  	{items:[{name:"#L.foreignAmount", type:"LABEL", value:"原幣金額"}]},
					{items:[{name:"#F.foreignAmount", type:"TEXT", bind:"foreignAmount", size:15, maxLen:25 }]},					
				  	{items:[{name:"#L.localAmount", type:"LABEL", value:"台幣金額"}]},
					{items:[{name:"#F.localAmount", type:"TEXT", bind:"localAmount", size:5, maxLen:25 }]},
					{items:[{name:"#L.taxAmount", type:"LABEL", value:"營業額"}]},
					{items:[{name:"#F.taxAmount", type:"TEXT", bind:"taxAmount", size:5, maxLen:25 }]},
					{items:[{name:"#L.billDate", type:"LABEL", value:"帳款日期<font color='red'>*</font>"}]},
					{items:[{name:"#F.billDate", type:"DATE", bind:"billDate", size:15, maxLen:25 }]}		
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
		vat.item.setValueByName("#L.maxRecord"    , "0");
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
	if(confirm(alertMessage)){
	var orderNo = vat.item.getValueByName("#F.orderNo");

	vat.bean().vatBeanOther.formAction = formAction;
		vat.block.submit(function(){
				return "process_object_name=imReceiveExpenseAction"+
				"&process_object_method_name=performTransaction";
			},{
				bind:true, link:true, other:true 
			}
		);
	}
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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].companyCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].companyCode;
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
function refreshForm(companyCode){
	vat.bean().vatBeanOther.companyCode       = companyCode;
	
	vat.block.submit(
		function(){
			return "process_object_name=imReceiveExpenseAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				doFormAccessControl();
     	}}
    );	
}
/* 供應商picker 回來執行 */
function doAfterPickerSupplier(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=imReceiveExpenseMainService&process_object_method_name=getAJAXFormDataBySupplier"+
							"&addressBookId="  + vat.bean().vatBeanPicker.result[0].addressBookId +
							"&organizationCode=TM"+
							"&brandCode="      + vat.item.getValueByName("#F.brandCode") + 
							"&orderDate="      + vat.item.getValueByName("#F.orderDate") ;
		vat.ajax.startRequest(processString,  function() { 
			if (vat.ajax.handleState()){
				vat.item.setValueByName("#F.supplierCode",    vat.ajax.getValue("SupplierCode",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
	
  			}
		} );
	}
}
function doAfterPickerSupplier2(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.supplierCode", vat.bean().vatBeanPicker.result[0].supplierCode);
    	vat.item.setValueByName("#F.supplierName", vat.bean().vatBeanPicker.result[0].supplierName);
	}
}

/* 設定供應商資料 */
function onChangeSupplierCode() {

	var processString = "process_object_name=poPurchaseOrderHeadMainService&process_object_method_name=getAJAXFormDataBySupplier" +
						"&supplierCode="  + vat.item.getValueByName("#F.supplierCode") + 
						"&organizationCode=TM"+
						"&brandCode="     + vat.item.getValueByName("#F.brandCode")  + 
						"&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode") ;
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.supplierName",    vat.ajax.getValue("SupplierName",    vat.ajax.xmlHttp.responseText));
		}
	});
}


// 依formId鎖form
function doFormAccessControl(){
    var formId = vat.bean().vatBeanOther.formId;
    var form    = vat.bean("form");
    if(vat.bean().vatBeanPicker.result!=null){
    //alert("1");
        //vat.item.setAttributeByName("#F.exchangeRateType", "readOnly", true);
        vat.item.setAttributeByName("#F.companyCode", "readOnly", true);
       
    }
    else{
    //alert("2");
        //vat.item.setValueByName("#F.exchangeRate", "");
       // vat.item.setAttributeByName("#F.exchangeRateType", "readOnly", false);
        vat.item.setAttributeByName("#F.companyCode", "readOnly", false);
        //vat.item.setAttributeByName("#F.sourceCurrency", "readOnly", false);    
        //vat.item.setAttributeByName("#F.againstCurrency", "readOnly", false);   
        //vat.item.setAttributeByName("#F.beginDate", "readOnly", false);
    }
    
    var enable = vat.item.getValueByName("#F.enable");
    if(enable == "Y"){
        vat.item.setValueByName("#F.enable", "N");
    }
    else{
        vat.item.setValueByName("#F.enable", "Y");
    }
}









