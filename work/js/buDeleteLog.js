/*** 
 *	檔案: buDeleteLog.js
 *	說明：清除LOG
 *	修改：Mark
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
				return "process_object_name=buBasicDataAction&process_object_method_name=performBuDeleteLogInitial"; 
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
           
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"清除LOG作業", 
		rows:[
			 
			{row_style:"", cols:[
				{items:[{name:"#L.cleanTable", 		type:"LABEL", 	value:"清除的資料<font color='red'>*</font>"}]},
				{items:[{name:"#F.cleanTable", 		type:"TEXT", 	bind:"cleanTable",size:30, maxLen:50, eChange:"getCleanDate()"}]},
				{items:[{name:"#L.cleanDay", 		type:"LABEL", 	value:"清除天數<font color='red'>*</font>"}]},
				{items:[{name:"#F.cleanDay", 		type:"NUMM", 	bind:"cleanDay",size:10}]},
				{items:[{name:"#L.cleanDate", 		type:"LABEL", 	value:"清除起始日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.cleanDate", 		type:"TEXT",    back:false, mode:"READONLY", 	bind:"cleanDate",size:10}]},
				{items:[{name:"#L.employeeCode", 	type:"LABEL", 	value:"登入人員"}]},
				{items:[{name:"#F.employeeCode", 	type:"TEXT", 	bind:"employeeCode", mode:"HIDDEN", back:false},
						{name:"#F.employeeName", 	type:"TEXT", 	bind:"employeeName", mode:"READONLY", back:false}]},
				{items:[{name : "#F.enable", type : "CHECKBOX",  bind : "enable"}, {name : "#L.enable", type : "LABEL", value : "啟用?", size : 10}], td : "colSpan=2"	}		 
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

function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
	if("SIGNING" == formAction){
		alertMessage = "是否確定送出?";
	}			
	if(confirm(alertMessage)){		
		vat.block.submit(function(){return "process_object_name=buBasicDataAction"+
			"&process_object_method_name=performBuDeleteLogTransaction";},{
			bind:true, link:true, other:true,
			funcSuccess:function(){
				window.top.close();
			}}
		);		
    } 
}

// 依狀態鎖form
function doFormAccessControl( match ){

	//initialFormAccessControl();
	
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.cleanTable", "");
	vat.item.setValueByName("#F.cleanDay", "");
	vat.item.setValueByName("#F.cleanDate", "");
}

//取得初始日
function getCleanDate(){
	
	var cleanTable = vat.item.getValueByName("#F.cleanTable");
	//alert(cleanTable);
	if(cleanTable !== ""){
        vat.ajax.XHRequest(
        {
           post:"process_object_name=buBasicDataService"+
                    "&process_object_method_name=findCleanDateByPropertyForAJAX"+
                    "&cleanTable=" + vat.item.getValueByName("#F.cleanTable"),                      
           find: function changePromotionCodeRequestSuccess(oXHR){
 				vat.item.setValueByName("#F.cleanDate", vat.ajax.getValue("cleanDate", oXHR.responseText)); 				          		 
           }
        });
    }
}
