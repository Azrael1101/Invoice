
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
// 搜尋初始化
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

     vat.bean().vatBeanOther = 
  	    {
  	     loginDepartment    : document.forms[0]["#loginDepartment"    	].value,
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,	
  	     currentRecordNumber 	: 0,
	     lastRecordNumber    	: 0
	    };
  /*vat.bean.init(function(){
		return "process_object_name=buPurchaseAction&process_object_method_name=performSearchInitial";
   		},{other: true});*/
   
 /* vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                   funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail=2 , vnCurrentPage = 1);}
			                    });*/
}
}

// 可搜尋的欄位
function headerInitial(){
	//var allCategroyTypes    = vat.bean("allCategroyTypes");
    //var allproject    = vat.bean("allproject");
 	var alldepartment       = vat.bean("alldepartment");
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"商品主檔查詢作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.itemNo", type:"LABEL", value:"品號<font color='red'>*</font>"}]},
				{items:[{name:"#F.itemNo", type:"TEXT", bind:"itemNo", size:25, maxLen:25 },
						{name:"SPACE"          , type:"LABEL"  , value:"　"}]},
				{items:[{name:"#L.department", type:"LABEL" , value:"部門"}]},
				{items:[{name:"#F.department", type:"SELECT", bind:"department",init:alldepartment , size:25, maxLen:25 }]},
				{items:[{name:"#L.enable", type:"LABEL" , value:"狀態"}]},
				{items:[{name:"#F.enable", type:"CHECKBOX",  bind:"enable" },
						{name:"#L.enable", type:"LABEL",value:"停用?", size:15 },
						{name:"#F.itemId", type:"HIDDEN", bind:"itemId"}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.itemName", type:"LABEL" , value:"品名"}]},
				{items:[{name:"#F.itemName", type:"TEXT", bind:"itemName", size:25, maxLen:25 }]},
				{items:[{name:"#L.createdByName", type:"LABEL" , value:"建檔人員"}]},
				{items:[{name:"#F.createdByName", type:"TEXT", bind:"createdByName", size:25,mode:"READONLY", maxLen:25 }]},
				{items:[{name:"#L.creationDate", type:"LABEL" , value:"建立日期"}]},
				{items:[{name:"#F.creationDate", type:"TEXT", bind:"creationDate",mode:"READONLY", size:15}]}
				
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
 
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;
 
   	vat.item.make(vnB_Detail, "checked"           , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"  ,                     desc:"序號"      });
	//vat.item.make(vnB_Detail, "abnormal"		    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"異常"      });
	vat.item.make(vnB_Detail, "itemCode"	    	, {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"品號"}      );
	vat.item.make(vnB_Detail, "itemName"			, {type:"TEXT" , size:15, maxLen:20, mode:"READONLY", desc:"品名" });
	vat.item.make(vnB_Detail, "specInfo"		    , {type:"TEXT" ,  maxLen:20, mode:"READONLY", desc:"商品規格"      });
	vat.item.make(vnB_Detail, "supplierCode"	    , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"供應商"      });
	vat.item.make(vnB_Detail, "itemId"  			, {type:"ROWID"   });
	//vat.item.make(vnB_Detail, "advanceInput"  	    , {type:"hidden"  , value:"轉派" ,desc:"功能" , src:"images/button_advance_input.gif"  , eClick:"advanceInput()" ,mode:"CONTACT" != userType ? "HIDDEN" : ""} );
		vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["itemId"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()"
														});

}


// 查詢點下執行
function loadBeforeAjxService(){
   //alert("載入之前");	
   
	var processString = "process_object_name=prItemService&process_object_method_name=getAJAXBuAdCustSearchPageData" + 
	                  	"&brandCode"       + "=" + vat.item.getValueByName("#F.brandCode"     ) +
	                  	"&orderTypeCode"   + "=" + vat.item.getValueByName("#F.orderTypeCode" ) +
	                  	"&notshowotherGroup"        + "=" + vat.item.getValueByName("#F.notshowotherGroup"             ) +
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
	//alert("存檔之前");
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=prItemService&process_object_method_name=saveSearchResult";
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
    		                     function(){ return "process_object_name=prItemService&process_object_method_name=getSearchSelection";
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
		 vat.item.setAttributeByName("#F.department", "readOnly", true);
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