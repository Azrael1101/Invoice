/*** 
 *	檔案: exportDBF.js
 *	說明：匯出海關光碟DBF
 */
vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){

 	formInitial(); 
	buttonLine(); 
  	headerInitial();
	
	doFormAccessControl();
}

// 初始化
function formInitial(){  
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value
        };
   		vat.bean.init(	
	  		function(){
				return "process_object_name=exportAction&process_object_method_name=performDBFInitial"; 
	    	},{								
	    		other: true
    	});
  	}
}

function buttonLine(){
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"}
	 			
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}	

// 目標主檔
function headerInitial(){ 
var allBudgetYearLists =  vat.bean("allBudgetYearLists");
var allMonths = vat.bean("allMonths");
var allExportTypes = vat.bean("allExportTypes");

var allCustomer =  [["","","","","true"],						 
           ["FW", "FD", "HD", "VD" ],
           ["FW", "FD", "HD", "VD" ]];
           
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"海關光碟DBF產生作業", 
		rows:[
			 
			{row_style:"", cols:[
				{items:[{name:"#L.year", 			type:"LABEL", 	value:"年<font color='red'>*</font>"}]},
				{items:[{name:"#F.year", 			type:"SELECT", 	bind:"year", init:allBudgetYearLists, size:1, back:false}]},
				{items:[{name:"#L.month", 			type:"LABEL", 	value:"月<font color='red'>*</font>"}]},
				{items:[{name:"#F.month", 			type:"SELECT", 	bind:"month", init:allMonths, size:1, back:false}]},
				{items:[{name:"#L.customerWarehouse", 		type:"LABEL", 	value:"關別<font color='red'>*</font>"}]},
				{items:[{name:"#F.customerWarehouse", 		type:"SELECT", 	bind:"customerWarehouse", init:allCustomer, size:25, back:false}]} 
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.exportType", 		type:"LABEL", 	value:"匯出類型<font color='red'>*</font>"}]},
				{items:[{name:"#F.exportType", 		type:"SELECT", 	bind:"exportType", init:allExportTypes, size:1, back:false}]},
				{items:[{name:"#L.employeeCode", 	type:"LABEL", 	value:"登入人員"}]},
				{items:[{name:"#F.employeeCode", 	type:"TEXT", 	bind:"employeeCode", mode:"HIDDEN", back:false},
						{name:"#F.employeeName", 	type:"TEXT", 	bind:"employeeName", mode:"READONLY", back:false}]},
				{items:[{name:"#L.brandName", 	type:"LABEL", 	value:"品牌"}]},
				{items:[{name:"#F.brandName", 	type:"TEXT", 	bind:"brandName", mode:"READONLY", back:false},
						{name:"#F.brandCode", 	type:"TEXT", 	bind:"brandCode", mode:"HIDDEN", back:false}],td:" colSpan=3"}
			]}
		], 	
		 
		beginService:"",
		closeService:""			
	});
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

// 查詢主檔headId
function doSearch(){
	var barCodeType = vat.item.getValueByName("#F.barCodeType");
	var orderNo = vat.item.getValueByName("#F.orderNo");
	var orderNoEnd = vat.item.getValueByName("#F.orderNoEnd");
	var brandCode = vat.item.getValueByName("#F.brandCode");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	var price = vat.item.getValueByName("#F.price");
	var category = vat.item.getValueByName("#F.category");
	var orderBy = vat.item.getValueByName("#F.orderBy");
	var taxType = vat.item.getValueByName("#F.taxType");
	var warehouseCode = vat.item.getValueByName("#F.warehouseCode");
	
	
	if(orderTypeCode == "" && barCodeType != "01_ImItem" ){ 
		alert('單別不可為空');
	}else if(orderNo == "" && barCodeType != "01_ImItem" ){
		alert('單號不可為空');
	}else{
		vat.ajax.XHRequest({ 
				post:"process_object_name=generateBarCodeService"+
	            		"&process_object_method_name=getAJAXHeadId"+
	            		"&barCodeType="+ barCodeType + 
	                	"&orderNo=" + orderNo + 
	                	"&orderNoEnd=" + orderNoEnd + 
	                	"&brandCode=" + brandCode +
	                	"&orderTypeCode="+ orderTypeCode +
	                	"&taxType=" + taxType +
	                	"&warehouseCode=" + warehouseCode,   
	                	       
				find: function changeRequestSuccess(oXHR){
					if( "" !== vat.ajax.getValue("errorMsg", oXHR.responseText)   ){
						alert(vat.ajax.getValue("errorMsg", oXHR.responseText));
						
					}else{
						vat.item.setValueByName("#F.headId", vat.ajax.getValue("headId", oXHR.responseText));
						vat.item.setValueByName("#F.headIds", vat.ajax.getValue("headIds", oXHR.responseText));
						vat.block.pageRefresh(activeTab, vnCurrentPage = 1);  
					}
				}
		});
	}
}

function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SIGNING" == formAction){
		alertMessage = "是否確定送出?";
	}
		
	if(confirm(alertMessage)){
		var msg = doCheck();
		if(msg != ""){
			alert(msg);
		}else{
			vat.block.submit(function(){return "process_object_name=exportAction"+
				"&process_object_method_name=performDBFExport";},{
				bind:true, link:true, other:true
				}
			);
		}
    } 
}

// js 檢核
function doCheck(){
	var year = vat.item.getValueByName("#F.year");
	var month = vat.item.getValueByName("#F.month");
	var customerWarehouse = vat.item.getValueByName("#F.customerWarehouse");
	
	var msg = "";
	if(year == ""){
		msg += "請選擇年份\n"
	}
	if(month == ""){
		msg += "請選擇月份\n"
	}
	if(customerWarehouse == ""){
		msg += "請選擇關別\n"
	}
	return msg;
}

// 初始化 form
function initialFormAccessControl(){
	vat.item.setStyleByName("#B.search", 			"display", "none");
	vat.item.setStyleByName("#B.barCodeExport", 	"display", "none");
	vat.item.setStyleByName("#B.match", 			"display", "inline");
	vat.item.setStyleByName("#B.unMatch", 			"display", "none");
	
	
	vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", true);	
	vat.item.setAttributeByName("#F.orderNo", "readOnly", true);	
	vat.item.setAttributeByName("#F.orderNoEnd", "readOnly", true);	
	vat.item.setAttributeByName("#F.orderBy", "readOnly", true);
	vat.item.setAttributeByName("#F.price", "readOnly", true);
	vat.item.setAttributeByName("#F.category", "readOnly", true);
	vat.item.setAttributeByName("#F.category02", "readOnly", true);
	vat.item.setAttributeByName("#F.warehouseCode", "readOnly", true);
	vat.item.setAttributeByName("#B.warehouseCode", "readOnly", true);
	vat.item.setAttributeByName("#F.taxType", "readOnly", true);
	vat.item.setValueByName("#F.orderNoEnd", "");
	vat.item.setValueByName("#F.orderBy", "");
	vat.item.setValueByName("#F.price", "");
	vat.item.setValueByName("#F.category", "");
	vat.item.setValueByName("#F.warehouseCode", "");
	vat.item.setValueByName("#F.category", "");
	
}

// 依狀態鎖form
function doFormAccessControl( match ){

	//initialFormAccessControl();
	
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.year", "");
	vat.item.setValueByName("#F.month", "");
	vat.item.setValueByName("#F.customerWarehouse", "");
	vat.item.setValueByName("#F.exportType", "");
}