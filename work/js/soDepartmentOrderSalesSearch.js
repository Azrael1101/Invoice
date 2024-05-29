 
vat.debug.disable();
var vnB_Header = 0;
var vnB_Detail = 1;
var vnB_Button = 2;


function outlineBlock(){
  	formInitial();
  	headerInitial();
  	buttonLine();
  	detailInitial();
}
function formInitial(){ 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	vat.bean().vatBeanOther = { 
			loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
			loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
			formId                : document.forms[0]["#formId"].value,
			customerCode  		: document.forms[0]["#customerCode"].value,      
			currentRecordNumber 	: 0,
			lastRecordNumber    	: 0
        };
        
	   	vat.bean.init(	
	  		function(){
				return "process_object_name=soDepartmentOrderAction&process_object_method_name=performSalesSearchInitial"; 
	    	},{
				other:true
	    	}
	    );
	}
 
}

function buttonLine(){

    vat.block.create(vnB_Button, {
	id: "vnB_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif", eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif", eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ],
		beginService:"",
		closeService:""
	});

}	

function headerInitial(){ 
	vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"銷貨單查詢作業", rows:[  
			{row_style:"", cols:[
	 		    {items:[{name:"#L.customerCode",	type:"LABEL", value:"會員代號"}]},
				{items:[{name:"#F.customerCode",	type:"TEXT", size:20, bind:"customerCode", mode:"READONLY"}]},
				{items:[{name:"#L.customerName",	type:"LABEL", value:"會員姓名"}]},
				{items:[{name:"#F.customerName",	type:"TEXT", size:20, bind:"customerName", mode:"READONLY"}]},
				{items:[{name:"#L.tel",	type:"LABEL", value:"會員電話"}]},
				{items:[{name:"#F.tel",	type:"TEXT", size:10, bind:"tel", mode:"READONLY"}]},
				{items:[{name:"#L.brandCode",	type:"LABEL", value:"品牌"}]},
				{items:[{name:"#F.brandCode",	type:"TEXT", size:10, bind:"brandCode", mode:"HIDDEN"},
						{name:"#F.brandName",	type:"TEXT", size:15, bind:"brandName", mode:"READONLY"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.itemCode",	type:"LABEL", value:"品號"}]},
				{items:[{name:"#F.itemCode",	type:"TEXT", size:20}]},
				{items:[{name:"#L.itemCName",	type:"LABEL", value:"品名"}]},
				{items:[{name:"#F.itemCName",	type:"TEXT", size:20}]},
				{items:[{name:"#L.salesOrderBeginDate",	type:"LABEL", value:"銷貨日期"}]},
				{items:[{name:"#F.salesOrderBeginDate",	type:"DATE", size:20},
						{name:"#L.salesOrderEndDate",	type:"LABEL", value:" 至 "},
						{name:"#F.salesOrderEndDate",	type:"DATE", size:20}], td:"colspan=3"}
			]}
		], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
  
	var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
	var vatPickerId =vat.bean().vatBeanOther.vatPickerId;   
	var vbSelectionType = "CHECK";    
    if(vatPickerId != null && vatPickerId != ""){
		vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
		vbSelectionType = "CHECK";    
    }else{
		vat.item.setStyleByName("#B.view" , "display", "none");
		vbSelectionType = "NONE";
    }

    vat.item.make(vnB_Detail, "indexNo"         		, {type:"IDX"  , size: 1, view:"fixed", desc:"序號"      });
	vat.item.make(vnB_Detail, "customerPoNo"   		, {type:"TEXT" , size:20 , view:"fixed" , mode:"READONLY", desc:"原訂單編號"      });
	vat.item.make(vnB_Detail, "salesOrderDate"  		, {type:"TEXT" , size:10 , view:"fixed" , mode:"READONLY", desc:" 銷貨日期"      });
	vat.item.make(vnB_Detail, "superintendentName"  	, {type:"TEXT" , size:6 , view:"" , mode:"READONLY", desc:"負責人員"      });
	vat.item.make(vnB_Detail, "customerCode"  			, {type:"TEXT" , size:15 , view:"fixed" , mode:"READONLY", desc:"會員代號"      });
	vat.item.make(vnB_Detail, "itemCode"  	        	, {type:"TEXT" , size:13 , view:"fixed" , mode:"READONLY", desc:"品號"      });
	vat.item.make(vnB_Detail, "itemCName"  	        	, {type:"TEXT" , size:15 , view:"fixed" , mode:"READONLY", desc:"品名"      });
	vat.item.make(vnB_Detail, "unitPrice"  	        	, {type:"TEXT" , size:8 , view:"fixed" , mode:"READONLY", desc:"單價"      });
	vat.item.make(vnB_Detail, "discountRate"  	        , {type:"TEXT" , size:8 , view:"fixed" , mode:"READONLY", desc:"折扣"      });
	vat.item.make(vnB_Detail, "discountPrice"  	        , {type:"TEXT" , size:8 , view:"fixed" , mode:"READONLY", desc:"折扣金額"      });
	vat.item.make(vnB_Detail, "orderNo"  	        , {type:"TEXT" , size:15 , view:"fixed" , mode:"HIDDEN", desc:"單號"      });
	vat.item.make(vnB_Detail, "orderTypeCode"  	        , {type:"TEXT" , size:15 , view:"fixed" , mode:"HIDDEN", desc:"單別"      });
	vat.item.make(vnB_Detail, "brandCode"  	        , {type:"TEXT" , size:15 , view:"fixed" , mode:"HIDDEN", desc:"品牌"      });
	vat.item.make(vnB_Detail, "indexNo"  	        , {type:"TEXT" , size:15 , view:"fixed" , mode:"HIDDEN", desc:"索引"      });
	vat.block.pageLayout(vnB_Detail, {
									id                  : "vatDetailDiv",
									pageSize            : 10,
									indexType           : "AUTO",
									selectionType       : vbSelectionType,
									searchKey           : ["id.orderNo", "id.orderTypeCode", "id.brandCode", "id.indexNo"],		
									loadSuccessAfter    : "loadSuccessAfter()",						
									loadBeforeAjxService: "loadBeforeAjxService()",
									saveBeforeAjxService: "saveBeforeAjxService()",
									saveSuccessAfter    : "saveSuccessAfter()"
                        });
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);

}

function loadSuccessAfter(){

}

function loadBeforeAjxService(){
	
	var processString = "process_object_name=soDepartmentOrderService&process_object_method_name=getAJAXSalesSearchPageData" + 
						"&itemCode=" 			+ vat.item.getValueByName("#F.itemCode") +
						"&itemName=" 			+ vat.item.getValueByName("#F.itemName") +
                     	"&salesOrderBeginDate=" + vat.item.getValueByName("#F.salesOrderBeginDate") +
                     	"&salesOrderEndDate=" 	+ vat.item.getValueByName("#F.salesOrderEndDate") +
                      	"&brandCode=" + vat.item.getValueByName("#F.brandCode") ;

	return processString;			
}

function checkEnableLine() {
	return true;
}

function saveBeforeAjxService(){
	//alert("sdfsdf");
	var processString = "";
	//alert("saveBeforeAjxService");
	if (checkEnableLine()) {
		processString = "process_object_name=soDepartmentOrderService&process_object_method_name=saveSalesSearchResult";
		//alert(processString);
	}
	return processString;
}
function saveSuccessAfter(){

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


// 刷新頁面
function refreshForm(code){
	//vat.bean().vatBeanOther.companyCode       = companyCode;
	document.forms[0]["#formId"].value = code;
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;
	vat.block.submit(
		function(){
			return "process_object_name=buCompanyAction&process_object_method_name=performBuCompanyInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				doFormAccessControl();
     	}}
    );	
}

function doSearch() {
    vat.block.submit(function () {
        return "process_object_name=tmpAjaxSearchDataService" + "&process_object_method_name=deleteByTimeScope&timeScope=" + vat.bean().vatBeanOther.timeScope
    }, {
        other: true,
        funcSuccess: function () {
            vat.block.pageDataLoad(vnB_Detail = 1, vnCurrentPage = 1);
        }
    });
}


