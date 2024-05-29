
var afterSavePageProcess = "";
var vnB_Detail = 2;
var vnB_Header = 1;
function outlineBlock(){
 	
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();
  doFormAccessControl();
}
// 搜尋初始化1
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

     vat.bean().vatBeanOther = 
  	    {
  	     loginDepartment    : document.forms[0]["#loginDepartment"    	].value,
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,	
  	     vatPickerId        : document.forms[0]["#vatPickerId"       	].value,
  	     orderTypeCode      : document.forms[0]["#orderTypeCode" ].value,
  	     brandCode  		: document.forms[0]["#loginBrandCode" ].value,
  	     userType  		    : document.forms[0]["#userType" ].value,
  	     currentRecordNumber 	: 0,
	     lastRecordNumber    	: 0
	    };
  
        }
        vat.bean.init(function(){
		return "process_object_name=adCustomerServiceAction&process_object_method_name=performSearchInitial";
   		},{other: true});
}

// 可搜尋的欄位
function headerInitial(){

 	var alldepartment    = vat.bean("alldepartment");
    var allstatus   = [[true, true,true,true,true,true],  ["暫存", "結案", "關檔","作廢"],["SAVE","FINISH","CLOSE","VOID"]];
	var allOrderTypes   = [[false, false,false,false,false,false], ["IRQ-需求單","PRC-請採驗","OSP-開店工程","BPR-新人","TAS-任務","LOA-權限"], ["IRQ","PRC","OSP","BPR","TAS","LOA"]];
	var allcategoryType			= vat.bean("allcategoryType");
	var allcustomerRequest      = vat.bean("allcustomerRequest"); 
	var allexceptional			= [["", "", false], ["請選擇","有", "無"],["","1","2"]];
	var allCategroyType      	= vat.bean("allCategroyType"); 
	var allnationality          = vat.bean("allnationality"); 
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"客服單查詢作業", 
	 		rows:[
				{row_style:"", cols:[
                {items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌/<font color='red'></font>"},
						{name:"#L.orderTypeCode", type:"LABEL", value:"單別/<font color='red'></font>"},
						{name:"#L.orderNo", type:"LABEL" , value:"單號<font color='red'></font>"}]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:5, maxLen:20, mode:"READONLY"},
						{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", size:5, maxLen:20,mode:"READONLY" },
						{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:20, maxLen:20 },
						{name:"#F.headId"      , type:"TEXT"  ,  size:4, bind:"headId" , back:false, mode:"READONLY"}]},
				{items:[{name:"#L.requestCode", type:"LABEL", value:"顧客名稱<font color='red'>*</font>"}]},
				{items:[{name:"#F.requestCode", type:"TEXT", bind:"requestCode", size:10, maxLen:25 }]},
                {items:[{name:"#L.requestDate",     type:"LABEL",  value:"立案日期<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.requestDate",     type:"Date",   bind:"requestDate",         size:10},
	 			        {name:"#L.between"                  , type:"LABEL" , value:"至"},
	                    {name:"#F.requestDate1"          , type:"DATE"  ,  bind:"requestDate1", size:10}]},
				{items:[{name:"#L.status", type:"LABEL" , value:"狀態"}]},
				{items:[{name:"#F.status", type:"SELECT", bind:"status", size:25,init:allstatus, maxLen:25}]}
		
			]},
			{row_style:"", cols:[
	 			{items:[{name:"#L.customerLastName", type:"LABEL",  value:"顧客姓"},
	 					{name:"#L.customerFristName",         type:"LABEL",  value:"/顧客名"}]},	 
	 			{items:[{name:"#F.customerLastName", bind:"customerLastName",type:"TEXT",   size:1 ,maxLen:1 },
	 					{name:"#F.customerFristName",    bind:"customerFristName" , type:"TEXT", size:12}]},
	 			{items:[{name : "#L.createdBy", type : "LABEL", value : "客服人員<font color='red'></font>"}]},
				{items:[{name : "#F.createdBy", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25}]},
				{items:[{name : "#L.itemBrand", type : "LABEL", value : "商品品牌<font color='red'></font>"}]},
				{items:[{name : "#F.itemBrand", type : "TEXT", bind : "itemBrand", size : 30, maxLen : 25}]},
				{items:[{name : "#L.itemCode", type : "LABEL", value : "商品品號<font color='red'></font>"}]},
				{items:[{name : "#F.itemCode", type : "TEXT", bind : "itemCode", size : 30, maxLen : 25}]} 
			]},
	 		{row_style:"", cols:[
	 			{items:[{name : "#L.createdBy", type : "LABEL", value : "部門"}]},
				{items:[{name:"#F.department", type:"SELECT", bind:"department", size:25, maxLen:25,init:alldepartment }]},
				{items:[{name : "#L.categoryType", type : "LABEL", value : "業種"}]},
				{items:[{name : "#F.categoryType", type : "SELECT", bind : "categoryType",init:allcategoryType, size : 30, maxLen : 25}]},
				{items:[{name:"#L.warehuseCode",     type:"LABEL",  value:"店別"}]},	 
	 			{items:[{name:"#F.warehuseCode",     type:"TEXT",   bind:"warehuseCode",         size:10}]},
	 			{items:[{name:"#L.customerRequest",     type:"LABEL",  value:"顧客訴求"}]},	 
	 			{items:[{name:"#F.customerRequest",     type:"Select",   bind:"customerRequest",init:allcustomerRequest, size:10}]}			 
			]},
	 		{row_style:"", cols:[
	 			{items:[{name : "#L.exceptional", type : "LABEL", value : "異常單"}]},
				{items:[{name : "#F.exceptional", type : "SELECT", bind : "exceptional",init:allexceptional, size : 10, maxLen : 25}]},
				{items:[{name : "#L.nationality", type : "LABEL", value : "國籍"}]},
				{items:[{name : "#F.nationality", type : "SELECT", bind : "nationality", init:allnationality, size : 30, maxLen : 25}]},
				{items:[{name:"#L.saleOrderDate",     type:"LABEL",  value:"購買日期<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.saleOrderDate",     type:"TEXT",   bind:"saleOrderDate",         size:10}],td:" colSpan=4"}		
			]}
		], 	 
		beginService:"",
		closeService:""			
	});
	   // vat.item.setValueByName("#F.notshowotherGroup", vat.bean().vatBeanOther.notshowotherGroup);
}

function buttonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif"   , eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
  var alldepartment    = vat.bean("alldepartment");
  var status    = vat.bean("status");
  var userType      = vat.item.getValueByName("#userType");
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
 
    
    var vnB_Detail = 2;
    var vatPickerId =vat.bean().vatBeanOther.vatPickerId;    
	var vbSelectionType = "CHECK";   
    if(vatPickerId != null && vatPickerId != ""){
		vat.item.make(vnB_Detail, "checkbox" , {type:"XBOX"});
		vbSelectionType = "CHECK";    
    }else{
		vat.item.setStyleByName("#B.picker" , "display", "none");
		vbSelectionType = "NONE";
    }
    
    if(vat.item.getValueByName("#userType")==="CONTACT")
    {
    //vat.item.make(vnB_Detail, "checked"           , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"  ,                     desc:"序號"      });
	//vat.item.make(vnB_Detail, "abnormal"		    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"異常"      });
	vat.item.make(vnB_Detail, "orderTypeCode"	    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"單別"}      );
	vat.item.make(vnB_Detail, "orderNo"			    , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"單號" });
	vat.item.make(vnB_Detail, "requestDate"		    , {type:"TEXT" ,  maxLen:20, mode:"READONLY", desc:"立案日期"      });
	vat.item.make(vnB_Detail, "requestCode"	        , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"顧客名稱"      });
	vat.item.make(vnB_Detail, "description"  	    , {type:"TEXT" , size:15, maxLen:50, mode:"READONLY", desc:"案件描述" , alter:true      });
	vat.item.make(vnB_Detail, "status"  	        , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "createdByName"  	    , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"客服人員"      });
	vat.item.make(vnB_Detail, "customerLastName"  	, {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"顧客姓"      });
	vat.item.make(vnB_Detail, "customerFristName"  	, {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"顧客名"      });
	vat.item.make(vnB_Detail, "itemBrand"  			, {type:"TEXT" , size:20, maxLen:20, mode:"READONLY", desc:"商品品牌"      });
	vat.item.make(vnB_Detail, "isClose"				, {type:"BUTTON" , size:10, maxLen:10, desc:"關檔" , value:"關檔"    , eClick:"advanceInput()"  });
	vat.item.make(vnB_Detail, "headId"  			, {type:"ROWID"   });
	//vat.item.make(vnB_Detail, "advanceInput"  	    , {type:"hidden"  , value:"轉派" ,desc:"功能" , src:"images/button_advance_input.gif"  , eClick:"advanceInput()" ,mode:"CONTACT" != userType ? "HIDDEN" : ""} );
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId","orderNo"],
														indexType           : "AUTO",
														selectionType       : vbSelectionType,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														//loadSuccessAfter  : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														//saveSuccessAfter  : "saveSuccessAfter()",
														blockId             : vnB_Detail,
														indicate            : 
										function(){
											if(vatPickerId != null && vatPickerId != ""){
												return false}else{ openModifyPage()}
										}
                        });

	}
	    if(vat.item.getValueByName("#userType")!="CONTACT")
    {
    //vat.item.make(vnB_Detail, "checked"           , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"  ,                     desc:"序號"      });
	//vat.item.make(vnB_Detail, "abnormal"		    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"異常"      });
	vat.item.make(vnB_Detail, "orderTypeCode"	    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"單別"}      );
	vat.item.make(vnB_Detail, "orderNo"			    , {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"單號" });
	vat.item.make(vnB_Detail, "requestDate"		    , {type:"TEXT" ,  maxLen:20, mode:"READONLY", desc:"立案日期"      });
	vat.item.make(vnB_Detail, "requestCode"	        , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"顧客名稱"      });
	vat.item.make(vnB_Detail, "description"  	    , {type:"TEXT" , size:15, maxLen:50, mode:"READONLY", desc:"案件描述" , alter:true      });
	vat.item.make(vnB_Detail, "status"  	        , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "createdByName"  	    , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"客服人員"      });
	vat.item.make(vnB_Detail, "customerLastName"  	, {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"顧客姓"      });
	vat.item.make(vnB_Detail, "customerFristName"  	, {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"顧客名"      });
	vat.item.make(vnB_Detail, "itemBrand"  			, {type:"TEXT" , size:20, maxLen:20, mode:"READONLY", desc:"商品品牌"      });
	vat.item.make(vnB_Detail, "isClose"				, {type:"HIDDEN" , size:10, maxLen:10, desc:"關檔" , value:"關檔"    , eClick:"advanceInput()"  });
	vat.item.make(vnB_Detail, "headId"  			, {type:"ROWID"   });
	//vat.item.make(vnB_Detail, "advanceInput"  	    , {type:"hidden"  , value:"轉派" ,desc:"功能" , src:"images/button_advance_input.gif"  , eClick:"advanceInput()" ,mode:"CONTACT" != userType ? "HIDDEN" : ""} );
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId","orderNo"],
														indexType           : "AUTO",
														selectionType       : vbSelectionType,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														//loadSuccessAfter  : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														//saveSuccessAfter  : "saveSuccessAfter()",
														blockId             : vnB_Detail,
														indicate            : 
										function(){
											if(vatPickerId != null && vatPickerId != ""){
												return false}else{ openModifyPage()}
										}
                        });

	}
}


// 查詢點下執行
function loadBeforeAjxService(){
   //alert("載入之前:"+vat.item.getValueByName("#F.requestDate"     ));	
   
	var processString = "process_object_name=adCustomerServiceService&process_object_method_name=getAJAXBuAdCustSearchPageData" + 
	                  	"&brandCode"       + "=" + vat.item.getValueByName("#F.brandCode"     ) +
	                  	"&requestDate"       + "=" + vat.item.getValueByName("#F.requestDate"     ) +
	                  	"&requestDate1"       + "=" + vat.item.getValueByName("#F.requestDate1"     ) +
	                  	"&orderTypeCode"   + "=" + vat.item.getValueByName("#F.orderTypeCode" ) +
	                  	"&notshowotherGroup"        + "=" + vat.item.getValueByName("#F.notshowotherGroup"             ) +
	                  	"&createdByName"        + "=" + vat.item.getValueByName("#F.createdBy"             ) +
	                  	"&orderNo"    	   + "=" + vat.item.getValueByName("#F.orderNo"       );
	               //alert("processString="+processString);     
                                                                            
	return processString;											
}

/*function saveSuccessAfter(){
 //alert("存檔成功");	
}*/

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=adCustomerServiceService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
	//alert('123abc');
	   vat.block.pageSearch(vnB_Detail, {
    		funcSuccess : 
    		    function(){
    		      vat.block.submit(
    		                     function(){ return "process_object_name=adCustomerServiceService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

// 查詢按下後
function doSearch(){

    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail=2 , vnCurrentPage = 1);}
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

function resetForm(){
    vat.item.setValueByName("#F.brandCode", "");
  //vat.item.setValueByName("#F.discount", "");
    vat.item.setValueByName("#F.oderTypeCode", "");
}
function doFormAccessControl(){
	var userType   = vat.item.getValueByName("userType");
	var orderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");
	var status   = vat.item.getValueByName("#F.status");
	var department   = vat.item.getValueByName("#F.department");
	var loginDepartment   = vat.item.getValueByName("#loginDepartment");
	vat.item.setStyleByName("#B.view" , "display", "none");
	
	if(vat.item.getValueByName("#loginDepartment")!="103")
	{
		// vat.item.setAttributeByName("#F.status", "readOnly", true);
		 vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", true);
		 
	}
	if(vat.item.getValueByName("#loginDepartment")=="103")
	{
		// vat.item.setAttributeByName("#F.status", "readOnly", true);
		 vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", true);
	}
}

function openModifyPage(){

    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
	// alert("openModifyPage:"+vFormId);
    if(!(vFormId == "" || vFormId == "0")){
	    var url = "AD_Customer_Service:create:20140226.page?formId=" + vFormId
	    		  +"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value
	    		  +"&userType=" + document.forms[0]["#userType"].value; 
	     sc=window.open(url, '客服單作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
	     sc.resizeTo((screen.availWidth),(screen.availHeight));
	     sc.moveTo(0,0);
	}
}
function eChangeRequest() {
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByRequest" +
						"&requestCode="  + vat.item.getValueByName("#F.requestCode");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.request", vat.ajax.getValue("request", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.requestCode", vat.ajax.getValue("requestCode", vat.ajax.xmlHttp.responseText))
		}
	});
}
function eChangeCreatedBy() {
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByCreatedBy" +
						"&createdBy="  + vat.item.getValueByName("#F.createdBy");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.createdBy", vat.ajax.getValue("createdBy", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.createdByName", vat.ajax.getValue("createdByName", vat.ajax.xmlHttp.responseText))
		}
	});
}
function advanceInput(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
	var returnData = window.showModalDialog(
		"AD_Customer_Service:close:20140723.page"+
		"?formId=" + vFormId ,"",
		"dialogHeight:300px;dialogWidth:400px;dialogTop:100px;dialogLeft:100px;");
}