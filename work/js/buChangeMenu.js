/*** 
 *	檔案: buChangeMenu.js
 *	說明：更換Menu作業
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
function formInitial(){ 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
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
	 	{items:[{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}	

	function headerInitial(){ 
		vat.block.create( vnB_Header, {
			id: "vatBlock_Head", generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"更換Menu權限資料維護作業", 
			rows:[
				{row_style:"", cols:[
				
					{items:[{name:"#L.employeeRole", type:"LABEL", value:"Menu權限工號<font color='red'>*</font>" }]},
					{items:[{name:"#F.employeeRole", type:"TEXT", bind:"employeeRole" , size:25, maxLen:25 , eChange:"changeEmployeeRole()" }]}
				]}
			], 	
			beginService:"",
			closeService:""			
		});
	}

// 動態設定工號為大寫
function changeEmployeeRole(){
	var companyCode = vat.item.getValueByName("#F.employeeRole").replace(/^\s+|\s+$/, '').toUpperCase();
	vat.item.setValueByName("#F.employeeRole", employeeRole);
}

// 送出的返回
function createRefreshForm(){
 
	formInitial();
}

// 送出,暫存按鈕
function doSubmit(formAction){
	//alert("doSubmit " + formAction);
	var alertMessage ="是否確定送出?";
	if(confirm(alertMessage)){
		
		vat.block.submit(function(){
				return "process_object_name=buBasicDataAction"+
				"&process_object_method_name=performChangeMenuTransaction";
			},{
				bind:true, link:true, other:true 
			}
		);
	}
}

// 刷新頁面
function refreshForm(companyCode){
	vat.bean().vatBeanOther.companyCode       = companyCode;
	
	vat.block.submit(
		function(){
			return "process_object_name=buBasicDataAction&process_object_method_name=performChangeMenuInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				doFormAccessControl();
     	}}
    );	
}

// 依formId鎖form
function doFormAccessControl(){
    var formId = vat.bean().vatBeanOther.formId;
    var form    = vat.bean("form");
    if(vat.bean().vatBeanPicker.result!=null){
    //alert("1");
        vat.item.setAttributeByName("#F.companyCode", "readOnly", true);
    }
    else{
    //alert("2");
        vat.item.setAttributeByName("#F.companyCode", "readOnly", false);
    }
    
    var enable = vat.item.getValueByName("#F.enable");
    if(enable == "Y"){
        vat.item.setValueByName("#F.enable", "N");
    }
    else{
        vat.item.setValueByName("#F.enable", "Y");
    }
}


