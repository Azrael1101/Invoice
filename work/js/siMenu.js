/*** 
 *	檔案: siMenu.js
 *	說明：選單維護作業
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
				return "process_object_name=siMenuAction&process_object_method_name=performInitial"; 
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
										  servicePassData:function()
										  	{return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode")},
	 									  service:"./Si:Si_Menu:search.page",
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
		title:"選單維護作業", rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.name",          	   type:"LABEL",  value:"選單名稱<font color='red'>*</font>"},
	 				{name:"#F.menuId", 			   type:"HIDDEN", bind:"menuId"}]},	 
	 		{items:[{name:"#F.name",      		   type:"TEXT",   bind:"name", size:30}]},		 
	 		{items:[{name:"#L.functionCode",       type:"LABEL",  value:"選單代號"}]},
			{items:[{name:"#F.functionCode",       type:"TEXT",   bind:"functionCode",    size:30}]},
			
			
			{items:[{name:"#L.reserve1",           type:"LABEL",  value:"報表類型(BI or CC)"}]},	
			{items:[{name:"#F.reserve1",           type:"TEXT",   bind:"reserve1", size:12}]},
			
	 		{items:[{name:"#L.brandCode",          type:"LABEL",  value:"品牌"}]},
	 		{items:[{name:"#F.brandCode",          type:"TEXT",   bind:"brandCode",    size:10, mode:"READONLY"}]}
	  		]
	  	},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.url",       type:"LABEL",  value:"網址<font color='red'>*</font>"}]},
	 		{items:[{name:"#F.url",       type:"TEXT",   bind:"url", size:70}]},
	 		{items:[{name:"#L.parentMenuId", type:"LABEL",  value:"上層選單"}]},
	 		
	 		{items:[
	 				{name:"#F.parentMenuName", type:"TEXT",   bind:"parentMenuName", size:30, mask:"CCCCCC"},
	 				{name:"#B.parentMenuName", type:"PICKER", value:"查詢",  src:"./images/start_node_16.gif",
	 									 			openMode:"open", 
	 									 			service:"./Si:Si_Menu:search.page",
	 									 			left:0, right:0, width:1024, height:768,	
	 									 			serviceAfterPick:function(){doAfterPickerMenu()}},
	 		 		{name:"#F.parentMenuId", type:"TEXT",   bind:"parentMenuId", size:12, mode:"READONLY"}
	 			   ]
	 		},
	 		
	 		{items:[{name:"#L.createdBy",          type:"LABEL",  value:"填單人員"}]},
	 		{items:[{name:"#F.createdBy",          type:"TEXT", bind:"createdBy", size:12, mode:"READONLY"}], td:" colSpan=3"}
	 	]}
	 	
	 		],
	 beginService:"",
	 closeService:""			
	});
}

function doSubmit(){
	vat.block.submit(function(){return "process_object_name=siMenuAction"+
            	"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true});
}

function doAfterPickerMenu(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=siMenuService&process_object_method_name=getAJAXFormDataByMenuId" +
							"&menuId="  + vat.bean().vatBeanPicker.result[0].menuId;			
		vat.ajax.startRequest(processString,  function(){
			if(vat.ajax.handleState()){
				vat.item.setValueByName("#F.parentMenuId", vat.ajax.getValue("menuId",    vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.parentMenuName", vat.ajax.getValue("name",    vat.ajax.xmlHttp.responseText));
  			}
		});
	}
}

function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result !== null){
		var processString = "process_object_name=siMenuService&process_object_method_name=getAJAXFormDataByMenuId" +
							"&menuId="  + vat.bean().vatBeanPicker.result[0].menuId;			
		vat.ajax.startRequest(processString,  function(){
			if(vat.ajax.handleState()){
				vat.item.setValueByName("#F.menuId", vat.ajax.getValue("menuId", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.name", vat.ajax.getValue("name", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.functionCode", vat.ajax.getValue("functionCode", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.reserve1", vat.ajax.getValue("reserve1", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.url", vat.ajax.getValue("url", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.parentMenuId", vat.ajax.getValue("parentMenuId", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.parentMenuName", vat.ajax.getValue("parentMenuName", vat.ajax.xmlHttp.responseText));
				vat.item.setValueByName("#F.createdBy", vat.ajax.getValue("updatedBy", vat.ajax.xmlHttp.responseText));
  			}
		});
	}
}

function resetForm(){
	vat.item.setValueByName("#F.menuId", "0");
	vat.item.setValueByName("#F.name", "");
	vat.item.setValueByName("#F.functionCode", "");
	vat.item.setValueByName("#F.reserve1", "");
	vat.item.setValueByName("#F.url", "");
	vat.item.setValueByName("#F.parentMenuId", "");
	vat.item.setValueByName("#F.parentMenuName", "");
}

//離開
function closeWindows(closeType){
	var isExit = true;
	if("CONFIRM" == closeType) isExit = confirm("是否確認離開?");
	if(isExit) window.top.close();
}
