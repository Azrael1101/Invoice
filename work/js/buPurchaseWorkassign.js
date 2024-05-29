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
  	     processId            : document.forms[0]["#processId"].value,
  	     orderTypeCode        : document.forms[0]["#orderTypeCode"].value,
  	     assignmentId		  : document.forms[0]["#assignmentId"].value,
  	     domain               : document.forms[0]["#domain"].value,
  	     headId               : document.forms[0]["#formId"           ].value,
  	     beforeStatus		  : "beforestatus",
	     nextStatus			  : "nextstatus",
	     approvalResult       : "",
	     approvalComment	  :""
	    };
     vat.bean.init(function(){
     	return "process_object_name=buPurchaseService&process_object_method_name=executeTaskassign"; 
   	    },{other: true});
  }
 
}
function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.submit"      , type:"IMG"      , value:"送出",  src:"./images/button_submit.gif", eClick:'doSubmit("REPLAN")'},
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
	title:"工作分派", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#L.no"            , type:"LABEL"  , value:"主旨"}]},	 
	 	{items:[{name:"#F.no"            , type:"TEXT" ,  bind:"no", size:50,mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	    {items:[{name:"#L.rqInChargeCode", type:"LABEL", value:"處理人員<font color='red'></font>"}]},
		{items:[{name:"#F.rqInChargeCode", type:"TEXT", bind:"rqInChargeCode", size:10, maxLen:25,mask:"Aaaaaaaa",eChange:"eChangerqInChargeCode()"},
				{name:"#B.rqInChargeCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee2();}},
				{name:"#F.otherGroup", type:"TEXT", bind:"otherGroup", size:10 ,mode:"READONLY" ,maxLen:25 }],td:" colSpan=4"}]},
	{row_style:"", cols:[					
		{items:[{name:"#L.priority"            , type:"LABEL"  , value:"需求優先順序"}]},	 
	    {items:[{name:"#F.priority"            , type:"NUMM" ,  bind:"priority", size:10}]}]},
	{row_style:"", cols:[					
		{items:[{name:"#L.status"            , type:"LABEL"  , value:"狀態"}]},	 
	    {items:[{name:"#F.status"            , type:"TEXT" ,  bind:"status", size:10,mode:"READONLY"},
	    		{name:"#F.assignmentId"      , type:"hidden" ,  bind:"assignmentId", size:5,mode:"READONLY"},
	    		{name:"#F.processId"         , type:"TEXT" ,  bind:"processId", size:5,mode:"READONLY"}]}]}			
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


function doSubmit(formAction){
	//var headId       = vat.bean().vatBeanOther.headId;
    var no = vat.item.getValueByName("#F.no");
    var rqInChargeCode = vat.item.getValueByName("#F.rqInChargeCode");
    var otherGroup = vat.item.getValueByName("#F.otherGroup");
    var priority = vat.item.getValueByName("#F.priority");
    var processId = vat.item.getValueByName("#F.processId");
    var approvalComment = vat.item.getValueByName("#F.approvalComment");
	var approvalResult  = vat.item.getValueByName("#F.approvalResult");
    var formStatus = status;
    if("REPLAN" == formAction){
        formStatus = "REPLAN";
    }
    if("REPLAN" == formAction){
	    alertMessage = "是否確定轉派?";
		/*if("" == processId){
			alert("沒有流程ID");
		}*/
		if("" == no){
			alert("請輸入主旨");
		}else{
		//  vat.bean().vatBeanOther.headId          = headId;
		  	vat.bean().vatBeanOther.no              = no;
		   	vat.bean().vatBeanOther.rqInChargeCode = rqInChargeCode;
		    vat.bean().vatBeanOther.otherGroup   = otherGroup;
		    vat.bean().vatBeanOther.priority   = priority;
		    vat.bean().vatBeanOther.processId   = processId;
		    	vat.bean().vatBeanOther.formAction = formAction;
	    		vat.bean().vatBeanOther.approvalResult = approvalResult;	
	    		vat.bean().vatBeanOther.approvalComment = approvalComment;
	    		vat.bean().vatBeanOther.formStatus = formStatus;	
		 
		  vat.block.submit(function(){return "process_object_name=buPurchaseAction&process_object_method_name=assignTask";},{
	                    bind:false, link:false, other:true}  );
		}
	
	
	}
}
function doAfterPickerEmployee2(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.rqInChargeCode", vat.bean().vatBeanPicker.result[0].employeeCode);
    	vat.item.setValueByName("#F.otherGroup", vat.bean().vatBeanPicker.result[0].chineseName);
	}
}
function eChangerqInChargeCode() {

	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByrqInChargeCode" +
						"&rqInChargeCode="  + vat.item.getValueByName("#F.rqInChargeCode");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.otherGroup", vat.ajax.getValue("otherGroup", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.rqInChargeCode", vat.ajax.getValue("rqInChargeCode", vat.ajax.xmlHttp.responseText))
	
		}
	});
}