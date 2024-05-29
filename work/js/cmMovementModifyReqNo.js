/*** 
 *	檔案: imMovementSearch.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";

function kweCmBlock(){
  kweCmModifyInitial();
  kweButtonLine();
  kweCmHeader();
  
}


function kweCmModifyInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {loginBrandCode       : document.forms[0]["#loginBrandCode"   ].value,   	
  	     loginEmployeeCode    : document.forms[0]["#loginEmployeeCode"].value,	  
  	     headId               : document.forms[0]["#formId"           ].value
	    };
     vat.bean.init(function(){
     	return "process_object_name=cmMovementService&process_object_method_name=executeModifyInitial"; 
   	    },{other: true});
  }
 
}
function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.submit"      , type:"IMG"      , value:"送出",  src:"./images/button_submit.gif", eClick:"doSubmit()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"doClear"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});  
	
}

function kweCmHeader(){ 
var allOrderTypes=vat.bean("allOrderTypes");
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"調撥單日期修改作業(轉出/入日期)", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderTypeCode"            , type:"LABEL"  , value:"單別"}]},	 
	 {items:[{name:"#F.orderTypeCode"            , type:"SELECT" ,  bind:"orderTypeCode", mode:"READONLY", size:1, init:allOrderTypes}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.orderNo"                  , type:"LABEL"  , value:"單號"}]},
	 {items:[{name:"#F.orderNo"                  , type:"TEXT"   ,  bind:"orderNo" , mode:"READONLY" , size:20}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.originalOrderNo"          , type:"LABEL" , value:"移倉申請書號碼"}]},	 
	 {items:[{name:"#F.moveWhNo"          , type:"TEXT"  ,  bind:"moveWhNo", size:30}]}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	 vat.item.setValueByName("#F.orderTypeCode"        , vat.bean().vatBeanOther.orderTypeCode);
	 vat.item.setValueByName("#F.orderNo"              , vat.bean().vatBeanOther.orderNo);
	 vat.item.setValueByName("#F.moveWhNo"      , vat.bean().vatBeanOther.moveWhNo);
	 
}



function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}


function doSubmit(){
	
	var headId       = vat.bean().vatBeanOther.headId;
    var moveWhNo = vat.item.getValueByName("#F.moveWhNo");
	if(confirm("是否確定送出?")){
	
		
		  vat.bean().vatBeanOther.headId          = headId;
		  vat.bean().vatBeanOther.moveWhNo       = moveWhNo;
		 
		  vat.block.submit(function(){return "process_object_name=cmMovementAction&process_object_method_name=modifyReqNo";},{
	                    bind:false, link:false, other:true}  );
		
	
	
	}
}