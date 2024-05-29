
/*** 
 *	檔案: currencyList.js
 *	說明：表單明細
 *	
 */
vat.debug.disable();
var afterSavePageProcess = "";
var headId;
var vnB_Button = 2;                                                                                                                                                                                                                                                                                                                                                                                                                              

function kweImBlock(){

  kweImModifyInitial();
  kweButtonLine();
  kweImHeader();
  //setDefaltValue();
  
}


function kweButtonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
			    {name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}


function kweImModifyInitial(){ 
  
     vat.bean().vatBeanOther = 
  	    {loginBrandCode       : "T2",   	
  	     loginEmployeeCode    : "T96085"
	    };
     vat.bean.init(function(){
     	return "process_object_name=buBasicDataAction&process_object_method_name=performBuCurrencyInitial"; 
   	    },{other: true});
  
}

function kweImHeader(){
vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"匯率清單", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#L.rateDateTime", 	type:"LABEL" , 	value:"匯率時間"}]},
		{items:[{name:"#F.rateDateTime", 	type:"TEXT" ,  mode:"READONLY" ,	bind:"rateDateTime", dec:0}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.ntRate", 	type:"LABEL" , 	value:"台幣"}]},
		{items:[{name:"#F.NTD", 	type:"TEXT"  ,	bind:"NTD", dec:0}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.dlRate", 	type:"LABEL" , 	value:"美金"}]},
		{items:[{name:"#F.USD", 	type:"TEXT" ,	bind:"USD", dec:0}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.yiRate", 	type:"LABEL" , 	value:"日幣"}]},
		{items:[{name:"#F.JPY", 	type:"TEXT" ,	bind:"JPY", dec:0}]}
	 ]},
	 {row_style:"", cols:[
	 	{items:[{name:"#L.rmbRate", 	type:"LABEL" , 	value:"人民幣"}]},
		{items:[{name:"#F.CNY", 	type:"TEXT" ,	bind:"CNY", dec:0}]}
	 ]}
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


function savePromotionCode(promotionCode){
	
	if(confirm("是否確定送出?")){
		if("" == headId){
			alert("ERROR");
		}else{
		  vat.bean().vatBeanOther.headId = headId;
		  vat.bean().vatBeanOther.promotionCode = promotionCode;
		 
		  vat.block.submit(function(){return "process_object_name=soDepartmentOrderService&process_object_method_name=updatePromotion";},{
	                    bind:true, link:true, other:true}  );
	      window.top.close();
		}
	}
}

	// 送出,暫存按鈕
function submitDo(formAction){
	var alertMessage ="是否確定送出?";
	if("FINISH" == formAction){
		alertMessage = "是否確定送出?";
	}else if("SAVE" == formAction){
		alertMessage = "是否確定暫存?";    
	}else if("VOID" == formAction){
		alertMessage = "是否確定作廢?";
	}else if("SUBMIT_BG" == formAction){
		alertMessage = "是否確定背景送出?";
	}
	if(confirm(alertMessage)){
	    var formId 				  = vat.bean().vatBeanOther.formId.replace(/^\s+|\s+$/, ''); 
		var processId             = vat.bean().vatBeanOther.processId.replace(/^\s+|\s+$/, '');
		var inProcessing          = !(processId === null || processId === ""  || processId === 0);
		var status                = vat.item.getValueByName("#F.status").replace(/^\s+|\s+$/, '');
	    var approvalComment       = vat.item.getValueByName("#F.approvalComment");
	    	      
		//initialVaTBeanOther();
		//vat.bean().vatBeanOther.formAction = formAction;
		//vat.bean().vatBeanOther.category06 = vat.item.getValueByName("#F.category06");	   
		/*vat.block.submit(
			function(){
				return "process_object_name=soDepartmentOrderService"+
				"&process_object_method_name=performTransaction"; 
			},{bind:true, link:true, other:true,
				funcSuccess:function(){
					vat.block.pageRefresh(0);
				}
			}
		);*/
	}
}
