/*** 
 *	檔案: siGroup.js
 *	說明：選單群組維護作業
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
  	//kweDetail();
	//doFormAccessControl();
}

function formInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = {
			brandCode  		    : document.forms[0]["#loginBrandCode"    ].value,
			loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
			currentRecordNumber 	: 0,
			lastRecordNumber    	: 0
        };
	   	vat.bean.init(
	  		function(){
				return "process_object_name=siGroupAction&process_object_method_name=performInitial"; 
	    	},
	    	{other: true}
	    );
  }
}

function buttonLine(){
	var vsMaxRecord = 0;
	var vsCurrentRecord = 0;
    vat.block.create(
    	vnB_Button, 
    	{
			id : "vatBlock_Button", 
			generate : true,
			table : "cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
			title : "", 
			rows : [  
				 {
				 	row_style : "", 
				 	cols : [
					 	{
					 		items : [
					 	 		{name:"#B.search" ,       type:"PICKER",  value:"查詢",  src:"./images/button_find.gif", 
	 									  openMode:"open", 
										  servicePassData:function(){
										  	//return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode")
										  },
	 									  service:"./Si:Si_Group:search.page",
	 									  left:0, right:0, width:1024, height:768,	
	 									  serviceAfterPick:function(){doAfterPickerProcess()}},
					 	 		
					 	 		{name : "SPACE", type : "LABEL", value : "　"},
					 	 		{name : "#B.clear",       type:"IMG",      value:"清除", src:"./images/button_reset.gif", eClick:"resetForm()"},
					 	 		{name:"SPACE",          type:"LABEL",    value:"　"},
	 	 						{name:"#B.exit",        type:"IMG",      value:"離開", src:"./images/button_exit.gif",  eClick:'closeWindows("CONFIRM")'},
					 	 		{name : "SPACE", type : "LABEL", value : "　"},
					 			{name : "#B.submit", type : "IMG", value : "送出", src : "./images/button_submit.gif", eClick : "doSubmit('SUBMIT')"}
					 	   ],
					 	   td : "style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
					 	}
					]
				 }
	  		], 	 
			beginService : "",
			closeService : ""			
		}
	);
}

function headerInitial(){
	vat.block.create(vnB_Header, {
		id: "vnB_Header", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"選單群組維護作業", rows:[  
		 	{row_style:"", cols:[
			 		{items:[{name:"#L.brandCode",          type:"LABEL",  value:"品牌<font color='red'>*</font>"}]},
			 		{items:[
			 			{name:"#F.brandCode",          type:"TEXT",   bind:"brandCode",    size:10},
			 			{name:"#F.pkString",          	   type:"TEXT",   bind:"pkString",    mode:"HIDDEN"}
			 		]},
			 		{items:[{name:"#L.groupCode",       type:"LABEL",  value:"群組代號<font color='red'>*</font>"}]},
					{items:[{name:"#F.groupCode",       type:"TEXT",   bind:"groupCode", size:30}]},
			 		{items:[{name:"#L.groupName",          	   type:"LABEL",  value:"群組名稱<font color='red'>*</font>"}]},	 
			 		{items:[{name:"#F.groupName",      		   type:"TEXT",   bind:"groupName", size:30}]}
		  		]
		  	},
		 	{row_style:"", cols:[
			 		{items:[{name:"#L.createdBy",          type:"LABEL",  value:"填單人員"}]},
			 		{items:[{name:"#F.createdBy",          type:"TEXT", bind:"createdBy",          size:12, mode:"READONLY"}], td:" colSpan=6"}
		 		]
		 	}
	 	],
	 beginService:"",
	 closeService:""			
	});
}

function doSubmit(){
	vat.block.submit(function(){return "process_object_name=siGroupAction"+
            	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
}

function doAfterPickerProcess(){
	vat.item.setValueByName("#F.brandCode", vat.bean().vatBeanPicker.result[0].brandCode);
	vat.item.setValueByName("#F.groupCode", vat.bean().vatBeanPicker.result[0].groupCode);
	vat.item.setValueByName("#F.groupName", vat.bean().vatBeanPicker.result[0].groupName);
	vat.item.setValueByName("#F.pkString",  vat.bean().vatBeanPicker.result[0].brandCode + "," + vat.bean().vatBeanPicker.result[0].groupCode);
}

function resetForm(){
	vat.item.setValueByName("#F.brandCode", "");
	vat.item.setValueByName("#F.groupCode", "");
	vat.item.setValueByName("#F.groupName", "");
	vat.item.setValueByName("#F.pkString", "");
}

//離開
function closeWindows(closeType){
	var isExit = true;
	if("CONFIRM" == closeType) isExit = confirm("是否確認離開?");
	if(isExit) window.top.close();
}

