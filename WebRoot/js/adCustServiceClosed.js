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

function kweImBlock(){
  kweImModifyInitial();
  kweButtonLine();
  kweImHeader();
  


}


function kweImModifyInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {loginBrandCode       : document.forms[0]["#loginBrandCode"   ].value,   	
  	     loginEmployeeCode    : document.forms[0]["#loginEmployeeCode"].value,	  
  	     headId               : document.forms[0]["#formId"           ].value
	    };
     vat.bean.init(function(){
     	return "process_object_name=adCustomerServiceService&process_object_method_name=executedoClose"; 
   	    },{other: true});
  }
 
}
function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.submit"      , type:"IMG"      , value:"關檔",  src:"./images/ad_close.gif", eClick:"doSubmit()"},
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

function kweImHeader(){ 

vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"關檔作業", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#L.status"            , type:"LABEL"  , value:"狀態"}]},	 
	 	{items:[{name:"#F.status"            , type:"TEXT" ,  bind:"status", size:50,mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	    {items:[{name:"#L.createdBy", type:"LABEL", value:"客服人員<font color='red'></font>"}]},
		{items:[{name:"#F.createdBy", type:"TEXT", bind:"createdBy", size:10 ,mode:"READONLY" ,maxLen:25,mask:"Aaaaaaaa"},
				{name:"#F.createdByName", type:"TEXT", bind:"createdByName", size:10 ,mode:"READONLY" ,maxLen:25 }],td:" colSpan=4"}]}
	/*{row_style:"", cols:[					
		{items:[{name:"#L.priority"            , type:"LABEL"  , value:"需求優先順序"}]},	 
	    {items:[{name:"#F.priority"            , type:"NUMM" ,  bind:"priority", size:10}]}]}*/		
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
	// vat.item.setValueByName("#F.no"        , vat.bean().vatBeanOther.no);
	// vat.item.setValueByName("#F.rqInChargeCode"         , vat.bean().vatBeanOther.rqInChargeCode);
	// vat.item.setValueByName("#F.otherGroup" , vat.bean().vatBeanOther.otherGroup);

	
	 
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

	//var headId       = vat.bean().vatBeanOther.headId;
    var status = vat.item.getValueByName("#F.status");
    var createdBy = vat.item.getValueByName("#F.createdBy");
    var createdByName = vat.item.getValueByName("#F.createdByName");

	if(confirm("是否確定關檔?")){
	
	
		//  vat.bean().vatBeanOther.headId          = headId;
		  	vat.bean().vatBeanOther.status              = status;
		   	vat.bean().vatBeanOther.createdBy 			= createdBy;
		    vat.bean().vatBeanOther.createdByName   	= createdByName;
		    //vat.bean().vatBeanOther.priority   = priority;
		 
		  vat.block.submit(function(){return "process_object_name=adCustomerServiceAction&process_object_method_name=doClose";},{
	                    bind:false, link:false, other:true}  );
		}
}
function eChangeCreatedBy() {
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByCreatedBy" +
						"&createdBy="  + vat.item.getValueByName("#F.createdBy")+
						"&createdByName="  + vat.item.getValueByName("#F.createdByName");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.createdBy", vat.ajax.getValue("createdBy", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.createdByName", vat.ajax.getValue("createdByName", vat.ajax.xmlHttp.responseText))
		}
	});
}