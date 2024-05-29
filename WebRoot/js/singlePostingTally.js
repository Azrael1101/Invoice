/*** 
 *	檔案: singlePostingTally.js
 *	說明：POS單筆過帳作業
 *	修改：joey
 *  <pre>
 *  	Created by Joey
 *  	All rights reserved.
 *  </pre>
 */



vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;


//var activeTab = 1;

function outlineBlock(){

  	formDataInitial();
	kweButtonLine();
  	headerInitial();
    //doFormAccessControl();
}

function initialVaTBeanCode(){
	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          shopCode        		: document.forms[0]["#shopCode"].value,   
	      salesDate       		: document.forms[0]["#salesDate"].value,	 
          orderTypeCode        	: document.forms[0]["#orderTypeCode"].value, 
	      orderNo      			: document.forms[0]["#orderNo"].value,
	      
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
}

function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){ // &&	document.forms[0]["#shopCode"].value != '[binding]' && document.forms[0]["#salesDate"].value != '[binding]'
			vat.bean().vatBeanOther = 
				{loginBrandCode     : document.forms[0]["#loginBrandCode"].value,   	
				loginEmployeeCode  : document.forms[0]["#loginEmployeeCode"].value
	    		}; 
	    
      vat.bean.init(function(){
		return "process_object_name=soPostingTallyAction&process_object_method_name=performInitial"; 
      },{other: true});
	}
      vat.item.bindAll();
}

function kweButtonLine(){
	vat.block.create(vnB_Button, {id: "vatBlock_Button", generate: true,	
	title:"", rows:[	 
	{row_style:"", cols:[
	 	{items:[	 	        
	 	 		{name:"#B.submit"      , type:"IMG"    ,value:"過帳",   src:"./images/button_posting.gif", eClick:'doSubmit("FINISH")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.rollBack"      , type:"IMG"    ,value:"反過帳",   src:"./images/button_unposting.gif", eClick:'doSubmit("ROLLBACK")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	 	],
		beginService:"",
		closeService:""			
	});
}

function headerInitial(){ 	
	var brandCode = document.forms[0]["#loginBrandCode"].value;
	var allShops = vat.bean("allShops");
	var allOrderTypes = vat.bean("allOrderTypes");
	var salesUnit = "專櫃";
	if("T2" == brandCode){
	    salesUnit = "機台";
	}
		
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"POS業績單筆過帳作業", 
		rows:[
			{row_style:"", cols:[
	 			{items:[{name:"#L.brandCode"				, type:"LABEL"	, value:"品牌"}]},	
				{items:[{name:"#F.brandCode"				, type:"TEXT"	, bind:"brandCode", mode:"HIDDEN" },
			            {name:"#F.brandName"				, type:"TEXT"	, bind:"brandName", mode:"READONLY"}]},
	 			{items:[{name:"#L.orderTypeCode"            , type:"LABEL"  , value:"單別"}]},	 
	 			{items:[{name:"#F.orderTypeCode"            , type:"SELECT" , bind:"orderTypeCode", init:allOrderTypes, size:12, mode:"READONLY"}]},		 
	 			{items:[{name:"#L.orderNo"                  , type:"LABEL"  , value:"單號"}]},
				{items:[{name:"#F.orderNo"             		, type:"TEXT"   , bind:"orderNo", size:16}]},
				{items:[{name:"#L.shopCode"					, type:"LABEL"	, value:salesUnit}]},
	 			{items:[{name:"#F.shopCode"					, type:"SELECT"	, bind:"shopCode", size:1, init:allShops}]},  
				{items:[{name:"#L.salesDate"				, type:"LABEL"	, value:"銷售日期"}]},
				{items:[{name:"#F.salesDate"				, type:"DATE"	, bind:"salesDate", size:1}]}]}
		], 	
		beginService:"",
		closeService:""			
	});	  	
}

function doFormAccessControl(){

    var shopCode = vat.bean().vatBeanOther.shopCode;//"].value.replace(/^\s+|\s+$/, '');
	var salesDate = vat.bean().vatBeanOther.salesDate;//"].value.replace(/^\s+|\s+$/, '');
	if(shopCode != "" && salesDate != ""){
	    vat.item.setAttributeByName("#F.shopCode", "readOnly", true);
	    vat.item.setAttributeByName("#F.salesDate", "readOnly", true);
	}else{
	    vat.item.setAttributeByName("#F.shopCode", "readOnly", false);
	    vat.item.setAttributeByName("#F.salesDate", "readOnly", false);
	}
}

//新增資料
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
      	vat.bean().vatBeanPicker.result = null;  
    	formDataInitial();
	 }
}

	// 送出,暫存按鈕
function doSubmit(formAction){
	var alertMessage ="是否確定送出?";
		
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.formAction = formAction;
	    vat.block.submit(function(){return "process_object_name=soPostingTallyAction"+
			"&process_object_method_name=performTransaction";}, {bind:true, link:true, other:true
			, funcSuccess:function(){vat.block.resetForm();
			}});
	}	
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