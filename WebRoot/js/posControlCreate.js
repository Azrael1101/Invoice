/*** 
 *	檔案: posControl.js
 *	說明：POS_CONTROL新增/修改
 *	修改：Kenny
 *  <pre>
 *  	Created by Kenny
 *  	All rights reserved.
 *  </pre>
 */



vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
  	formDataInitial();
	kweButtonLine();
  	headerInitial();

}

function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = 
		{
			loginBrandCode		:	document.forms[0]["#loginBrandCode" ].value,
			loginEmployeeCode	:	document.forms[0]["#loginEmployeeCode" ].value
    	}; 	    
		vat.bean.init(function(){
			return "process_object_name=PosControlAction&process_object_method_name=performInitial"; 
		},{other: true});
		vat.item.bindAll();
	}
}

function kweButtonLine(){
	vat.block.create(vnB_Button, {id: "vatBlock_Button", generate: true,	
	title:"", rows:[	 
	{row_style:"", cols:[
	 	{items:[	 	        
	 			{name:"#B.search"      , type:"IMG"      , value:"送出",  src:"./images/button_submit.gif", eClick:"doSubmit()"},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.e xit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'}	 	 		
	 	 		],
	 				td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
	 			]}
				//{name:"#B.message"  	, type:"IMG"    ,value:"訊息提示"	,   src:"./images/button_message_prompt.gif", eClick:'showMessage()'}
	 	],
		beginService:"",
		closeService:""			
	});
}

function headerInitial(){
	var executeType =        [["","",true], ["A-早上開機", "P-晚上清機", "M-手動按鈕", "R-指定時間", "F-固定頻率", "T-即時"], ["A", "P", "M", "R", "F", "T"]]; 	
	var UDType =        [["","",true], ["U-上傳", "D-下傳"], ["U", "D"]];
	var bCodeId = vat.bean("bCodeId");
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"POS_CONTROL維護作業", 
		rows:[
			{row_style:"", cols:[
	 			{items:[{name:"#L.brandCode"			, type:"LABEL"	, value:"品牌"}]},	
				{items:[{name:"#F.brandCode"			, type:"SELECT"	, bind:"brandCode",init:bCodeId }]},
			    {items:[{name:"#L.downloadFunction"			, type:"LABEL", value:"類型"}]},
				{items:[{name:"#F.downloadFunction"			, type:"SELECT", bind:"downloadFunction"}]},
				{items:[{name:"#L.posMachineCode"			, type:"LABEL", value:"機台號碼"}]},
				{items:[{name:"#F.posMachineCode"			, type:"SELECT", bind:"posMachineCode"}]},
				{items:[{name:"#L.executeType"			, type:"LABEL", value:"執行時機類型"}]},
				{items:[{name:"#F.executeType"			, type:"SELECT", bind:"executeType", init:executeType}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.timeout"			, type:"LABEL", value:"TIMEOUT"}]},
				{items:[{name:"#F.timeout"			, type:"TEXT", bind:"timeout"}]},
				{items:[{name:"#L.transfer"			, type:"LABEL", value:"上下傳方向"}]},
				{items:[{name:"#F.transfer"			, type:"SELECT", bind:"transfer", init:UDType}]},
				{items:[{name:"#L.indexNo"			, type:"LABEL", value:"執行順序"}]},
				{items:[{name:"#F.indexNo"			, type:"TEXT", bind:"indexNo"}]},
				{items:[{name:"#L.frequency"			, type:"LABEL", value:"執行頻率"}]},
				{items:[{name:"#F.frequency"			, type:"TEXT", bind:"frequency"}]},
				{items:[{name:"#F.isUpdate"			, type:"TEXT",mode:"HIDDEN", bind:"isUpdate"}]}
			]},
				{row_style:"", cols:[
				{items:[{name:"#L.remark"			, type:"LABEL", value:"備註",row:2}]},
				{items:[{name:"#F.remark"			, type:"TEXTAREA", bind:"remark",size:30 , row:2, col: 50}],td:" colSpan=7"}
			]}
		], 	
		beginService:"",
		closeService:function(){closeHeader();}	
	});	  	
}

function closeHeader(){
vat.ajax.XHRequest({ 
	post:"process_object_name=posDUService"+
          		"&process_object_method_name=findDownloadCommon"+
          		"&brandCode=" + document.forms[0]["#loginBrandCode" ].value,
          asyn:false,                      
	find: function change(oXHR){
		vat.item.SelectBind(eval(vat.ajax.getValue("allDownloadFunction", oXHR.responseText)),{ itemName : "#F.downloadFunction" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allShop"			, oXHR.responseText)),{ itemName : "#F.shopCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allBuShopMachine"	, oXHR.responseText)),{ itemName : "#F.posMachineCode" });
		vat.item.bindAll();
		}
	});
		parseURL();
}

function loadSuccessAfter(){}

function doSubmit(){
	
	var alertMessage ="是否確定送出?";
	
	if(confirm(alertMessage)){
		vat.block.submit(function(){
				return "process_object_name=PosControlAction"+
				"&process_object_method_name=performPosCtrlTransaction";
			},{
				bind:true, link:true, other:true ,
				funcSuccess:function(){
					var check = vat.item.getValueByName("#F.isUpdate");
					if(check == ""){
						clearForm();
					}else{
						window.close();
					}
     			}
			}
		);
	}
}

function clearForm(){
		vat.item.setValueByName("#F.downloadFunction", "");
		vat.item.setValueByName("#F.executeType", "");
		vat.item.setValueByName("#F.posMachineCode", "");
		vat.item.setValueByName("#F.timeout", "");
		vat.item.setValueByName("#F.transfer", "");
		vat.item.setValueByName("#F.indexNo", "");
		vat.item.setValueByName("#F.frequency", "");
		vat.item.setValueByName("#F.remark", "");
}

function parseURL(){

	var strUrl = location.search;  
	var getPara, ParaVal;  
	var aryPara = [];  
	var brandCode = "";
	if (strUrl.indexOf("?") != -1) {  
		var getSearch = strUrl.split("?");  
    getPara = getSearch[1].split("&");  
    for (i = 0; i < getPara.length; i++) {  
		ParaVal = getPara[i].split("=");
		if(ParaVal[0] =="brandCode"){
			vat.item.setValueByName("#F.brandCode" ,ParaVal[1]);
			vat.item.setAttributeByName("#F.brandCode", "readOnly", true);
		}else if(ParaVal[0] =="dataType"){
			vat.item.setValueByName("#F.downloadFunction" ,ParaVal[1]);
			vat.item.setAttributeByName("#F.downloadFunction", "readOnly", true);
		}else if(ParaVal[0] =="posMachineCode"){
			vat.item.setValueByName("#F.posMachineCode" ,ParaVal[1]);
			vat.item.setAttributeByName("#F.posMachineCode", "readOnly", true);
		}else if(ParaVal[0] =="timeout"){
			vat.item.setValueByName("#F.timeout" ,ParaVal[1]);
		}else if(ParaVal[0] =="transfer"){
			vat.item.setValueByName("#F.transfer" ,ParaVal[1]);
		}else if(ParaVal[0] =="indexNo"){
			vat.item.setValueByName("#F.indexNo" ,ParaVal[1]);
		}else if(ParaVal[0] =="frequency"){
			vat.item.setValueByName("#F.frequency" ,ParaVal[1]);
		}else if(ParaVal[0] =="remark"){
			vat.item.setValueByName("#F.remark" ,ParaVal[1]);
		}else if(ParaVal[0] =="executeType"){
			vat.item.setValueByName("#F.executeType" ,ParaVal[1]);
		}else if(ParaVal[0] == "isUpdate"){
			vat.item.setValueByName("#F.isUpdate",ParaVal[1]);
		}
    }

   }  

}