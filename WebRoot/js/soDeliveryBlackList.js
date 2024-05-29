/*** 
 *	檔案: soDeliverySearch.js
 *	說明：入提單查詢
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";

function kweBlock(){
  var sName = "";
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
  	 if("" != document.forms[0]["#customerName"].value){
     	var winParent = window.opener.document;
     	sName = winParent.getElementById("#F.customerName").value;
     }
     vat.bean().vatBeanOther = 
  	    {loginBrandCode       : document.forms[0]["#loginBrandCode"       ].value,   	
  	     brandCode            : document.forms[0]["#loginBrandCode"       ].value,  
  	     loginEmployeeCode    : document.forms[0]["#loginEmployeeCode"    ].value,
  	     customerName         : sName,
  	     passportNo           : document.forms[0]["#passportNo"           ].value,
  	     customerCode         : document.forms[0]["#customerCode"         ].value,
  	     executeEmployeeCode  : document.forms[0]["#executeEmployeeCode"  ].value,
  	     formId               : document.forms[0]["#formId"               ].value
	    };
  }
  kweButtonLine();
  kweHeader();
  kweInitial();

}


function kweInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean.init(function(){
		return "process_object_name=soDeliveryBlackListService&process_object_method_name=executeInitial"; 
   		},{other: true});
     vat.item.bindAll();
  }
 
}
function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.submit"      , type:"IMG"    , value:"送出",  src:"./images/button_submit.gif", eClick:"doSubmit()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"    , value:"清除",  src:"./images/button_reset.gif", eClick:"doClear()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  , value:"　"}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	
}

function kweHeader(){ vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"常客名單建檔作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.brandCode"                , type:"LABEL"  , value:"品牌"}]},	 		 
	 {items:[{name:"#F.brandCode"                , type:"TEXT" ,  bind:"brandCode", size:80, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.customerName"             , type:"LABEL"  , value:"姓名"}]},
	 {items:[{name:"#F.customerName"             , type:"TEXT"   ,  bind:"name", size:14},
	 	     {name:"#F.formId"                   , type:"TEXT"   ,  bind:"headId", mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.passportNo"               , type:"LABEL" , value:"護照號碼"}]},
	 {items:[{name:"#F.passportNo"               , type:"TEXT"  ,  bind:"passportNo",size:20}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.tel"                       , type:"LABEL" , value:"電話"}]},
	 {items:[{name:"#F.tel"                      , type:"TEXT"  ,  bind:"tel",size:20}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.reason1"                  , type:"LABEL"  , value:"原因一"}]},
	 {items:[{name:"#F.reason1"                  , type:"TEXT"  ,  bind:"reason1", size:60}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.reason2"                   , type:"LABEL"  , value:"原因二"}]},	 		 
	 {items:[{name:"#F.reason2"                   , type:"TEXT" ,  bind:"reason2", size:60}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark1"                   , type:"LABEL"  , value:"備註一"}]},	 		 
	 {items:[{name:"#F.remark1"                   , type:"TEXT" ,  bind:"remark1", size:80}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.remark2"                   , type:"LABEL"  , value:"備註二"}]},	 		 
	 {items:[{name:"#F.remark2"                   , type:"TEXT" ,  bind:"remark2", size:80}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.createdBy"                , type:"LABEL"  , value:"建檔人員"}]},	 		 
	 {items:[{name:"#F.createdBy"                , type:"TEXT" ,  bind:"createdBy", size:20, mode:"READONLY"},
	         {name:"#F.createdName"              , type:"TEXT" ,  bind:"createdName", size:20, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.creationDate"             , type:"LABEL"  , value:"建檔日期"}]},	 		 
	 {items:[{name:"#F.creationDate"              , type:"TEXT" ,  bind:"creationDate", size:20, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.lastUpdatedBy"             , type:"LABEL"  , value:"修改人員"}]},	 		 
	 {items:[{name:"#F.lastUpdatedBy"             , type:"TEXT" ,  bind:"lastUpdatedBy", size:20, mode:"READONLY"},
	         {name:"#F.lastUpdatedName"           , type:"TEXT" ,  bind:"lastUpdatedName", size:20, mode:"READONLY"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.lastUpdateDate"            , type:"LABEL"  , value:"更新日期"}]},	 		 
	 {items:[{name:"#F.lastUpdateDate"            , type:"TEXT" ,  bind:"lastUpdateDate", size:20, mode:"READONLY"}]}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}







function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}


function doClear(){
	vat.item.setValueByName("#F.orderNo" ,"");
	vat.item.setValueByName("#F.startOrderDate" ,"");
	vat.item.setValueByName("#F.endOrderDate","");
	vat.item.setValueByName("#F.startDeliveryDate" ,"");
	vat.item.setValueByName("#F.endDeliveryDate" ,"");
	vat.item.setValueByName("#F.startScheduleDeliveryDate" ,"");
	vat.item.setValueByName("#F.endScheduleDeliveryDate" ,"");
	vat.item.setValueByName("#F.startFlightDate" ,"");
	vat.item.setValueByName("#F.endFlightDate","");
	vat.item.setValueByName("#F.customerPoNo" ,"");
	vat.item.setValueByName("#F.status" ,"");
	vat.item.setValueByName("#F.updateFlag" ,"");
	vat.item.setValueByName("#F.customerName" ,"");
	vat.item.setValueByName("#F.contactInfo" ,"");
	vat.item.setValueByName("#F.possportNo" ,"");	
	vat.item.setValueByName("#F.shopCode" ,"");
	vat.item.setValueByName("#F.stortArea" ,"");
	vat.item.setValueByName("#F.flightArea" ,"");
	vat.item.setValueByName("#F.flightNo" ,"");	
	vat.item.setValueByName("#F.valuable" ,"");
}



function getEmployeeInfo(vsEmployee){
	if("" !=  vsEmployee,vat.item.getValueByName("#F."+vsEmployee)){
	    vat.item.setValueByName("#F."+ vsEmployee,vat.item.getValueByName("#F."+vsEmployee).toUpperCase());
		var processString = "process_sql_code=FindEmployeeChineseName&employeeCode="+vat.item.getValueByName("#F."+vsEmployee) ;
		vat.ajax.startRequest(processString,  function() { 
		  if (vat.ajax.handleState()){
		    if(vat.ajax.found(vat.ajax.xmlHttp.responseText)){
		      vat.item.setValueByName("#F."+ vsEmployee +"Name", vat.ajax.getValue("CHINESE_NAME", vat.ajax.xmlHttp.responseText));	    
		    }else{
		      vat.item.setValueByName("#F."+ vsEmployee +"Name", "");	  
		      alert("查無此員工代號");
		    }
		  }
		} );
	}
}


function doSubmit(){
	var vsAllowSubmit         = true;
	var alertMessage          ="是否確定送出?";
	var formId                = vat.item.getValueByName("#F.headId").replace(/^\s+|\s+$/, '');
    var processString 	      ="";


	if(confirm(alertMessage)){
	    var vsBrandCode = vat.item.getValueByName("#F.brandCode");
		var vsAllowSubmit = true;

		if("" == vat.item.getValueByName("#F.customerName")){
			alert("請輸入姓名");
			vat.form.item.setFocus( "#F.customerName" );
			vsAllowSubmit = false;
		}
		if("" == vat.item.getValueByName("#F.passportNo")){
			alert("請輸入護照號碼");
			vat.form.item.setFocus( "#F.passportNo" );
			vsAllowSubmit = false;
		}	

    	if(vsAllowSubmit){
    	
  	  	      vat.bean().vatBeanOther.executeEmployee =  vat.item.getValueByName("#F.employeeCode");
  	  	      vat.bean().vatBeanOther.name =  vat.item.getValueByName("#F.customerName");
  	  	      vat.bean().vatBeanOther.passportNo =  vat.item.getValueByName("#F.passportNo");
	          processString = "process_object_name=soDeliveryBlackListService&process_object_method_name=save";
			  vat.block.submit(function(){return processString;},{
		                    bind:true, link:true, other:true,
		                    funcSuccess:function(){
				        
				        	}}
			  );
        }
	}
}
