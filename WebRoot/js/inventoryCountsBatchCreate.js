/*** 
 *	檔案: inventoryCountsBatchCreate.js
 *	說明：盤點單批次建立
 *	修改：Jeremy
 *  <pre>
 *  	Created by jeremy
 *  	All rights reserved.
 *  </pre>
 */

vat.debug.disable();
var afterSavePageProcess = "";

var vnB_Button = 0;
var vnB_Header = 1;
var si_Menu_List;

function kweImBlock(){
  kweImInitial();
  kweButtonLine();
  kweImHeader();
}

function kweImInitial(){
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
	vat.bean().vatBeanOther =
        { 
          loginBrandCode  	 : document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,
          orderTypeCode      : document.forms[0]["#orderTypeCode"].value,
          formId             : document.forms[0]["#formId"            ].value
	};
      
   	vat.bean.init(	
  		function(){
				return "process_object_name=imInventoryCountsBatchCreateService&process_object_method_name=executeInitial"; 
    	},{
    		other: true
    	}
    );    
  }
}

function kweNewInitial(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
			si_Menu_List = null;
    	vat.item.setValueByName("#L.currentRecord", "0");
			vat.item.setValueByName("#L.maxRecord"    , "0");
    	refreshForm("");
	 }
}

function kweImHeader(){
	var allOrderTypeCodes = vat.bean("allOrderTypeCodes");
	
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"盤點單批次建立",
		rows:[
			{row_style:"", cols:[		
	 			{items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別"}]},	 
	 			{items:[{name:"#F.orderTypeCode", type:"SELECT",  bind:"orderTypeCode", init:allOrderTypeCodes, mode:"READONLY"}]},
				{items:[{name:"#L.countsDate", type:"LABEL" , value:"實際盤點日<font color='red'>*</font>"}]},
				{items:[{name:"#F.countsDate", type:"DATE", bind:"countsDate", size:1}]},	 			
				{items:[{ name:"#L.brandName", type:"LABEL", value:"品牌" }] },
				{items:[
						{name:"#F.brandCode"   , type:"TEXT"  ,  bind:"brandCode", size:8, mode:"HIDDEN"},
						{ name:"#F.brandName", type:"TEXT", bind:"brandName", back:false, size:20, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[		
				{items:[{name:"#L.countsId", type:"LABEL" , value:"盤點代號<font color='red'>*</font>"}]},
	 			{items:[{name:"#F.countsId", type:"TEXT", bind:"countsId", size:15, maxLen:15}]},
	 			{items:[{name:"#L.superintendentCode", type:"LABEL" , value:"盤點人<font color='red'>*</font>"}]},
	 			{items:[{name:"#F.superintendentCode", type:"TEXT", bind:"superintendentCode", size:15, maxLen:15, eChange:'changeSuperintendent()'},
	         			{name:"#F.superintendentName", type:"TEXT", bind:"superintendentName", size:12, mode:"READONLY"}]},	          	 
	 			{items:[{name:"#L.actualSuperintendentCode", type:"LABEL", value:"實盤人"}]},
	 			{items:[{name:"#F.actualSuperintendentCode", type:"TEXT", bind:"actualSuperintendentCode", size:15, maxLen:15, eChange:'changeActualSuperintendent()'},
	         			{name:"#F.actualSuperintendentName", type:"TEXT", bind:"actualSuperintendentName", size:12, mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.warehouseCode", type:"LABEL" , value:"庫別<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.warehouseCode", type:"TEXT", bind:"warehouseCode", size:15, maxLen:15, eChange:'changeWarehouseCode()'},
						/*{name:"#B.searchWarehouse", value:"選取庫別" ,type:"PICKER" , 
	 									 openMode:"open", 
	 									 service:"/erp/Im_Warehouse:search:1.page", 
	 									 left:0, right:0, width:1024, height:768,
	 									 serviceAfterPick:function(){doAfterPickerWarehouseProcess();} },*/	 			
	         			{name:"#F.warehouseName", type:"TEXT", bind:"warehouseName", back:false, mode:"READONLY"}]},
	 			{items:[{name:"#L.serialNo", type:"LABEL" , value:"流水號<font color='red'>*</font>"}]},
	 			{items:[{name:"#F.serialNo_begin", type:"TEXT",  bind:"serialNo_begin", size:15},
			 			{name:"#L.between", type:"LABEL", value:" 至 "},
	 		 			{name:"#F.serialNo_end", type:"TEXT",  bind:"serialNo_end"  , size:15}], 
	 		 	 td:" colSpan=3"}]},
			{row_style:"", cols:[
				{items:[{name:"#L.description", type:"LABEL" , value:"說明"}]},				
	 			{items:[{name:"#F.description", type:"TEXT",  bind:"description", size:100, maxLen:100}],
	 			 td:" colSpan=5"
	 			}  
			]}
		],
		
		beginService:"",
		closeService:""			
	});
}

function kweButtonLine(){
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"kweNewInitial()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.picker"      , type:"IMG"    , value:"暫存",   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'}
	 			],	 			
	 	td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}	


function changeWarehouseCode(){
    var warehouseCode = vat.item.getValueByName("#F.warehouseCode").replace(/^\s+|\s+$/, '').toUpperCase();    
    vat.item.setValueByName("#F.warehouseCode", warehouseCode);   
    if(warehouseCode !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=imWarehouseService"+
                    "&process_object_method_name=findByBrandCodeAndWarehouseCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&warehouseCode=" + warehouseCode,
           find: function changeWarehouseRequestSuccess(oXHR){
               vat.item.setValueByName("#F.warehouseCode", vat.ajax.getValue("WarehouseCode", oXHR.responseText));
               vat.item.setValueByName("#F.warehouseName", vat.ajax.getValue("WarehouseName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setValueByName("#F.warehouseName", "");
    }
}

function changeSuperintendent(){
    var superintendentTmp = vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, '').toUpperCase();    
    vat.item.setValueByName("#F.superintendentCode", superintendentTmp);   
    
    if(superintendentTmp !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + superintendentTmp,
           find: function changeSuperintendentRequestSuccess(oXHR){
               vat.item.setValueByName("#F.superintendentCode", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setValueByName("#F.superintendentName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setValueByName("#F.superintendentName", "");
    }
}

function changeActualSuperintendent(){
    var actualSuperintendentTmp = vat.item.getValueByName("#F.actualSuperintendentCode").replace(/^\s+|\s+$/, '').toUpperCase();    
    vat.item.setValueByName("#F.actualSuperintendentCode", actualSuperintendentTmp);   
    
    if(actualSuperintendentTmp !== ""){
       vat.ajax.XHRequest(
       {
           post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + vat.item.getValueByName("#F.brandCode") + 
                    "&employeeCode=" + actualSuperintendentTmp,
           find: function changeSuperintendentRequestSuccess(oXHR){
               vat.item.setValueByName("#F.actualSuperintendentCode", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
               vat.item.setValueByName("#F.actualSuperintendentName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
           }   
       });
    }else{
        vat.item.setValueByName("#F.actualSuperintendentName", "");
    }
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}

function doSubmit(formAction){

	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}
	
	if(confirm(alertMessage)){
		
		var orderTypeCode			= vat.item.getValueByName("#F.orderTypeCode").replace(/^\s+|\s+$/, ''); 
    	var countsDate				= vat.item.getValueByName("#F.countsDate").replace(/^\s+|\s+$/, '');
		var brandCode				= vat.item.getValueByName("#F.brandCode").replace(/^\s+|\s+$/, ''); 	    
    	var countsId				= vat.item.getValueByName("#F.countsId").replace(/^\s+|\s+$/, '');
    	var superintendentCode		= vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, '');    	
		var actualSuperintendentCode= vat.item.getValueByName("#F.actualSuperintendentCode").replace(/^\s+|\s+$/, '');
		var warehouseCode			= vat.item.getValueByName("#F.warehouseCode").replace(/^\s+|\s+$/, '');
		var serialNo_begin			= vat.item.getValueByName("#F.serialNo_begin").replace(/^\s+|\s+$/, '');
		var serialNo_end			= vat.item.getValueByName("#F.serialNo_end").replace(/^\s+|\s+$/, '');
		var description				= vat.item.getValueByName("#F.description").replace(/^\s+|\s+$/, '');
		var status					= vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');     	

		vat.bean().vatBeanOther.formAction = formAction;
		
	    var formStatus = "";
	    if("SAVE" == formAction){
            formStatus = "SAVE";
        }else if("SUBMIT" == formAction){
            formStatus = "COUNTING";
        }

		vat.bean().vatBeanOther.formStatus = formStatus; 
		vat.block.submit(function(){return "process_object_name=imInventoryCountsAction"+
			                			   "&process_object_method_name=performTransactionBatch";}, {bind:true, link:true, other:true});		                              
    
	}
}

function doAfterPickerWarehouseProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
		vat.item.setValueByName("#F.warehouseCode", vat.bean().vatBeanPicker.result[0].functionCode);
		changeFunctionName();
	}
}

function doAfterPickerParentMenuIDProcess(){
	if(vat.bean().vatBeanPicker.si_Menu_List !== null){
		vat.item.setValueByName("#F.parentMenuId", vat.bean().vatBeanPicker.si_Menu_List[0].menuId);
	}
}

function showMessage(){
	var width = "600";
    var height = "400";  
	window.open(
		"/erp/jsp/ShowProgramLog.jsp" + 
		"?programId=IM_INVENTORY" +
		"&levelType=ERROR" +
        "&processObjectName=imInventoryCountsService" + 
        "&processObjectMethodName=getIdentification" +
        "&arguments=" + vat.item.getValueByName("#F.headId") +
        "&parameterTypes=LONG",
		"Message",
		'menubar=no,resizable=yes,scrollbars=yes,status=no,width=' + width + ',height=' + height + ',left=' + (screen.availWidth - width)/2 + ',top=' + (screen.availHeight - height)/2);
}

function resetForm(){   
	vat.item.setValueByName("#F.countsDate", "");
	vat.item.setValueByName("#F.countsId", "");
	vat.item.setValueByName("#F.superintendentCode", "");
	vat.item.setValueByName("#F.actualSuperintendentCode", "");
	vat.item.setValueByName("#F.superintendentName", "");
	vat.item.setValueByName("#F.actualSuperintendentName", "");
	vat.item.setValueByName("#F.warehouseCode", "");
	vat.item.setValueByName("#F.warehouseName", "");
	vat.item.setValueByName("#F.serialNo_begin", "");
	vat.item.setValueByName("#F.serialNo_end", "");
	vat.item.setValueByName("#F.description", "");
}


