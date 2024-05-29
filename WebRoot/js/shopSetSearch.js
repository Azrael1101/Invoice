
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
			loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
			loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,	
			vatPickerId        : document.forms[0]["#vatPickerId"       	].value,
			orderTypeCode      : document.forms[0]["#orderTypeCode" ].value,
			brandCode  		: document.forms[0]["#loginBrandCode" ].value,
			userType  		    : document.forms[0]["#userType" ].value,
			currentRecordNumber 	: 0,
			lastRecordNumber    	: 0
	    };
	    
		vat.bean.init(function(){
			return "process_object_name=buPurchaseAction&process_object_method_name=performSearchInitial";
   		},{
   			other: true
   		});
   
 /* vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                   funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail=2 , vnCurrentPage = 1);}
			                    });*/
	}
}

// 可搜尋的欄位
function headerInitial(){
	var allCategroyTypese    = [[true, true,true,true,true,true],  ["需求","專案","例行","異常","準備"],["TASK_REQ","TASK_PRJ","TASK_MA","TASK_EX","TASK_PRE"]];
    //var allproject    = vat.bean("allproject");
    var userType      = vat.item.getValueByName("#userType");
    var vsCostStyle=userType!="CONTACT"?" style= 'display:none;'":""; 
    var vsCostStyle1=userType=="CONTACT"?" style= 'display:none;'":""; 
 	var alldepartment    = vat.bean("alldepartment");
    var allstatus   = [[true, true,true,true,true,true],  ["暫存", "計畫", "結案","暫停","作廢"],["SAVE","PLAN","FINISH","SUSPEND","VOID"]];
	var allOrderTypes   = [[false, false,false,false,false,false], ["IRQ-需求單","PRC-請採驗","OSP-開店工程","BPR-新人","TAS-任務","LOA-權限","CSF-客服","WOA-庫別","SOA-店別"], ["IRQ","PRC","OSP","BPR","TAS","LOA","CSF","WOA","SOA"]];
	if(vat.bean().vatBeanOther.userType ==="CONTACT" ){ 
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"需求單查詢作業", 
	 		rows:[
				{row_style:"", cols:[

				{items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別/<font color='red'></font>"},
						{name:"#L.orderNo", type:"LABEL" , value:"單號<font color='red'></font>"}]},
				{items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode",size:10, maxLen:20,init:allOrderTypes},
						{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:12, maxLen:20,mode:"READYONLY" }]},

				{items:[{name:"#L.Date",          type:"LABEL", value:"需求日期"}]},
	 			{items:[{name:"#F.startDate",     type:"DATE",  bind:"startDate", size:1},
	         			{name:"#L.between",       type:"LABEL", value:" 至 "},
	         			{name:"#F.endDate",       type:"DATE",  bind:"endDate",   size:1}]},
				{items:[{name:"#L.department", type:"LABEL" , value:"需求部門"}]},
				{items:[{name:"#F.department", type:"SELECT", bind:"department", size:25, maxLen:25,init:alldepartment }]},
			    {items:[{name:"#L.requestCode", type:"LABEL", value:"需求人員<font color='red'></font>"}]},
				{items:[{name:"#F.requestCode", type:"TEXT", bind:"requestCode", size:10, maxLen:25, mask:"Aaaaaaaaaa"  ,eChange:"eChangeRequest()" },
				 		{name:"#B.requestCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.request"   , type:"TEXT", bind:"request", size:5, maxLen:25,mode:"READONLY", eChange:"eChangeRequestCode()"}],td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items :[{name : "#L.no", type : "LABEL", value : "主旨說明<font color='red'></font>"}]},
				{items :[{name : "#F.no", type : "TEXT", bind : "no", size : 50, maxLen : 25}],td:" colSpan=3"},
				{items :[{name : "#L.status", type:"LABEL", value:"狀態"}]},
				{items :[{name : "#F.status", type:"SELECT", bind:"status", size:25, maxLen:25,init:allstatus }]},
				{items :[{name : "#L.createdBy", type : "LABEL", value : "填單人員<font color='red'></font>"}]},
				{items :[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25, mask:"Aaaaaaaaaa", eChange:"eChangeCreatedBy()"},
				 		 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}]}
				 	 ]}, 	
			{row_style:"", cols:[
				{items:[{name:"#L.rqInChargeCode", type:"LABEL", value:"處理人員<font color='red'></font>"}]},
				{items:[{name:"#F.rqInChargeCode", type:"TEXT", bind:"rqInChargeCode", size:10, maxLen:25, mask:"Aaaaaaaaaa",eChange:"eChangerqInChargeCode()" },
				 		{name:"#B.rqInChargeCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.otherGroup"   , type:"TEXT", bind:"otherGroup", size:5, maxLen:25,mode:"READONLY"}]},
				{items:[{name:"#L.categoryGroup", type:"LABEL" , value:"分類<font color='red'></font>"}]},
				{items:[{name:"#F.categoryGroup", type:"SELECT", bind:"categoryGroup",init:allCategroyTypese}],td:" colSpan=8"}]},	 	  	 	  	 
			{row_style:"", cols:[
				{items:[{name:"#F.notshowotherGroup"            , type:"CHECKBOX" , value:"Y"},
	         			{name:"#L.notshowotherGroup"             , type:"LABEL"    , value:"顯示未分派人員<BR>"}], td:" colSpan=9"}]}   	
		], 	 
		beginService:"",
		closeService:""			
	});
	 }  // vat.item.setValueByName("#F.notshowotherGroup", vat.bean().vatBeanOther.notshowotherGroup);
	 
	 if(vat.bean().vatBeanOther.userType !="CONTACT" ){ 
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"需求單查詢作業", 
	 		rows:[
				{row_style:"", cols:[

				{items:[{name:"#L.orderTypeCode", type:"LABEL", value:"單別/<font color='red'></font>"},
						{name:"#L.orderNo", type:"LABEL" , value:"單號<font color='red'></font>"}]},
				{items:[{name:"#F.orderTypeCode", type:"SELECT", bind:"orderTypeCode",size:10, maxLen:20,init:allOrderTypes},
						{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:12, maxLen:20,mode:"READYONLY" }]},

				{items:[{name:"#L.Date",          type:"LABEL", value:"需求日期"}]},
	 			{items:[{name:"#F.startDate",     type:"DATE",  bind:"startDate", size:1},
	         			{name:"#L.between",       type:"LABEL", value:" 至 "},
	         			{name:"#F.endDate",       type:"DATE",  bind:"endDate",   size:1}]},
	         	{items:[{name:"#L.department", type:"LABEL" , value:"需求部門"}]},
				{items:[{name:"#F.department", type:"SELECT", bind:"department", size:25, maxLen:25,init:alldepartment }]},
			    {items:[{name:"#L.requestCode", type:"LABEL", value:"需求人員<font color='red'></font>"}]},
				{items:[{name:"#F.requestCode", type:"TEXT", bind:"requestCode", size:10, maxLen:25, mask:"Aaaaaaaaaa"  ,eChange:"eChangeRequest()" },
				 		{name:"#B.requestCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.request"   , type:"TEXT", bind:"request", size:5, maxLen:25,mode:"READONLY", eChange:"eChangeRequestCode()"}],td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items :[{name : "#L.no", type : "LABEL", value : "主旨說明<font color='red'></font>"}]},
				{items :[{name : "#F.no", type : "TEXT", bind : "no", size : 50, maxLen : 25}],td:" colSpan=3"},
				{items :[{name : "#L.status", type:"LABEL", value:"狀態"}]},
				{items :[{name : "#F.status", type:"SELECT", bind:"status", size:25, maxLen:25,init:allstatus }]},
				{items :[{name : "#L.createdBy", type : "LABEL", value : "填單人員<font color='red'></font>"}]},
				{items :[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25, mask:"Aaaaaaaaaa", eChange:"eChangeCreatedBy()"},
				 		 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}]}
				 	 ]}, 	
			{row_style:"", cols:[
				{items:[{name:"#L.rqInChargeCode", type:"LABEL", value:"處理人員<font color='red'></font>"}]},
				{items:[{name:"#F.rqInChargeCode", type:"TEXT", bind:"rqInChargeCode", size:10, maxLen:25, mask:"Aaaaaaaaaa",eChange:"eChangerqInChargeCode()" },
				 		{name:"#B.rqInChargeCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.otherGroup"   , type:"TEXT", bind:"otherGroup", size:5, maxLen:25,mode:"READONLY"}],td:" colSpan=9"}
				 	 ]},	 	  	 	  	 
			{row_style:"", cols:[
				{items:[{name:"#F.notshowotherGroup"            , type:"CHECKBOX" , value:"N"},
	         			{name:"#L.notshowotherGroup"             , type:"LABEL"    , value:"顯示未分派人員<BR>"}], td:" colSpan=9"}]}   	
		], 	 
		beginService:"",
		closeService:""			
	});
	 } 
}

function buttonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.clear"       , type:"IMG"      , value:"清除",  src:"./images/button_reset.gif"  , eClick:"resetForm()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.picker"      , type:"IMG"      , value:"檢視",   src:"./images/button_view.gif"  , eClick:"doClosePicker()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.work"        , type:"IMG"      , value:"計算",   src:"./images/calculation.gif"  , eClick:"getCalculationData()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.order"       , type:"IMG"      , value:"重新排序",   src:"./images/orderByNew.GIF"  , eClick:"getOrderByNew()"}],

	 			
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
    
    if(vat.item.getValueByName("#userType")!="CONTACT")
    {
     //vat.item.make(vnB_Detail, "checked"             , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"  ,                     desc:"序號"      });
	//vat.item.make(vnB_Detail, "abnormal"		    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"異常"      });
	vat.item.make(vnB_Detail, "orderTypeCode"	    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"單別"}      );
	vat.item.make(vnB_Detail, "orderNo"			    , {type:"TEXT" , size:20, maxLen:20, mode:"READONLY", desc:"單號" });
	vat.item.make(vnB_Detail, "requestDate"		    , {type:"HIDDEN" ,  maxLen:20, mode:"READONLY", desc:"需求日期"      });
	vat.item.make(vnB_Detail, "depName"  	   	    , {type:"HIDDEN" , size:10, maxLen:50, mode:"READONLY", desc:"需求部門" ,init:alldepartment});
	vat.item.make(vnB_Detail, "request"	            , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"需求人員"      });
	vat.item.make(vnB_Detail, "contractTel"	        , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"分機"      });
	vat.item.make(vnB_Detail, "no"  	            , {type:"TEXT" , size:15, maxLen:50, mode:"READONLY", desc:"主旨" , alter:true      });
	vat.item.make(vnB_Detail, "description"  	    , {type:"HIDDEN" , size:15, maxLen:50, mode:"READONLY", desc:"說明" , alter:true      });
	vat.item.make(vnB_Detail, "status"  	        , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "otherGroup"	        , {type:"TEXT" , size:3, maxLen:10, mode:"READONLY", desc:"處理人員"      });
	vat.item.make(vnB_Detail, "priority"	        , {type:"TEXT" , size:3, maxLen:10, mode:"READONLY", desc:"順序"      });
	vat.item.make(vnB_Detail, "exctueStartDate"	    , {type:"TEXT" , size:3, maxLen:10, mode:"READONLY", desc:"預計完成日"      });
	vat.item.make(vnB_Detail, "totalHours"	        , {type:"TEXT" , size:3, maxLen:10, mode:"READONLY", desc:"時數加總"      });
	//vat.item.make(vnB_Detail, "taskInchanrgeCode"	, {type:"TEXT" , size:10, maxLen:10, mode:"READONLY", desc:"進度"      });
	vat.item.make(vnB_Detail, "headId"  , {type:"ROWID"   });
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
														//saveSuccessAfter    : "saveSuccessAfter()",
														blockId             : vnB_Detail,
													    indicate            : 
										function(){
											if(vatPickerId != null && vatPickerId != ""){
												return false}else{ openModifyPage()}
										}
										
                        });
                        
                         if("103" ==  vat.bean().vatBeanOther.loginDepartment){
		 vat.item.setGridStyleByName("advanceInput"  , "display", "inline");
   }else{
	 	 vat.item.setGridStyleByName("advanceInput"  , "display", "none");
   }
    }
  if(vat.item.getValueByName("#userType")==="CONTACT")
  {
   //vat.item.make(vnB_Detail, "checked"            , {type:"XBOX"});
    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"  ,  size:2,              desc:"序號"      });
	//vat.item.make(vnB_Detail, "abnormal"		    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY", desc:"異常"      });
	vat.item.make(vnB_Detail, "orderTypeCode"	    , {type:"TEXT" , size:5, maxLen:20, mode:"READONLY"   , desc:"單別"}      );//,mode:"CONTACT" === userType ? "HIDDEN" : ""
	vat.item.make(vnB_Detail, "orderNo"			    , {type:"TEXT" , size:20, maxLen:20, mode:"READONLY"  , desc:"單號" });//,mode:"CONTACT" === userType ? "HIDDEN" : ""
	vat.item.make(vnB_Detail, "requestDate"		    , {type:"TEXT" ,  maxLen:20, mode:"READONLY"		  , desc:"需求日期"      });
	vat.item.make(vnB_Detail, "depName"  	   	    , {type:"SELECT" , maxLen:50, mode:"READONLY", desc:"需求部門" ,init:alldepartment});
	vat.item.make(vnB_Detail, "request"	            , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY"   , desc:"需求人員"      });
	vat.item.make(vnB_Detail, "contractTel"	        , {type:"HIDDEN" , size:3, maxLen:20, mode:"READONLY" , desc:"分機"      });
	vat.item.make(vnB_Detail, "no"  	            , {type:"TEXT" , size:15, maxLen:50, mode:"READONLY"  , desc:"主旨"   , alter:true      });
	vat.item.make(vnB_Detail, "description"  	    , {type:"HIDDEN" , size:15, maxLen:50, mode:"READONLY", desc:"說明"   , alter:true      });
	vat.item.make(vnB_Detail, "status"  	    , {type:"TEXT" , size:3, maxLen:20, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "otherGroup"	        , {type:"TEXT" , size:3, maxLen:10, mode:"READONLY", desc:"處理人員"      });
	vat.item.make(vnB_Detail, "priority"	        , {type:"TEXT" , size:3, maxLen:10, mode:"READONLY", desc:"順序"      });
	vat.item.make(vnB_Detail, "exctueStartDate"	    , {type:"TEXT" , size:3, maxLen:10, mode:"READONLY", desc:"預計完成日"      });
	vat.item.make(vnB_Detail, "totalHours"	        , {type:"TEXT" , size:3, maxLen:10, mode:"READONLY", desc:"時數加總"      });
	//vat.item.make(vnB_Detail, "taskInchanrgeCode"	, {type:"TEXT" , size:10, maxLen:10, mode:"READONLY", desc:"進度"      });
	vat.item.make(vnB_Detail, "headId"  , {type:"ROWID"   });
	vat.item.make(vnB_Detail, "advanceInput"  	    , {type:"BUTTON"  , value:"轉派" ,desc:"功能" , src:"images/button_advance_input.gif"  , eClick:"advanceInput()" ,mode:"CONTACT" != userType ? "HIDDEN" : ""} );
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["headId","orderNo"],
														indexType           : "AUTO",
														selectionType       : vbSelectionType,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "()",	
														//loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														//saveSuccessAfter    : "saveSuccessAfter()",
														blockId             : vnB_Detail,
														indicate            : 
										function(){
											if(vatPickerId != null && vatPickerId != ""){
												return false}else{ openModifyPage()}
										}
                        });
                        
                         if("103" ==  vat.bean().vatBeanOther.loginDepartment){
		 vat.item.setGridStyleByName("advanceInput"  , "display", "inline");
   }else{
	 	 vat.item.setGridStyleByName("advanceInput"  , "display", "none");
   }
  }
   vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}



// 載入成功後
function loadSuccessAfter(){
 //alert("載入成功");	
	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		//vat.item.setGridAttributeByName("checkbox", "readOnly", true);
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
	    //vat.item.setGridAttributeByName("checkbox", "readOnly", false);
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}

// 查詢點下執行
function loadBeforeAjxService(){
   //alert("載入之前:"+document.forms[0]["#loginBrandCode" ].value);	
   
	var processString = "process_object_name=shopSetService&process_object_method_name=getAJAXShopSetSearchPageData" + 
	                  	"&brandCode"       + "=" + document.forms[0]["#loginBrandCode" ].value +
	                  	"&orderTypeCode"   + "=" + vat.item.getValueByName("#F.orderTypeCode" ) +
	                  	"&notshowotherGroup"        + "=" + vat.item.getValueByName("#F.notshowotherGroup"             ) +
	                  	"&orderNo"    	   + "=" + vat.item.getValueByName("#F.orderNo"       );
	               //alert("processString="+processString);     
                                                                            
	return processString;											
}

function saveSuccessAfter(){
 //alert("存檔成功");	
}

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	//alert("存檔之前");
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=buPurchaseService&process_object_method_name=saveSearchResult";
	}
	return processString;
}								

// 檢視按下後的動作 
function doClosePicker(){
	vat.block.pageSearch(vnB_Detail, {
		funcSuccess : function(){
			vat.block.submit(function(){ 
				return "process_object_name=shopSetService&process_object_method_name=getSearchSelection";
			},{
				bind:true, link:false, other:false, picker:true, isPicker:true, blockId:vnB_Detail
			});
    	}
    }); 
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
    vat.item.setValueByName("#F.orderNo", "");
  	vat.item.setValueByName("#F.startDate", "");
    vat.item.setValueByName("#F.endDate", "");
  	vat.item.setValueByName("#F.no", "");
    vat.item.setValueByName("#F.createdBy", "");
   	vat.item.setValueByName("#F.rqInChargeCode", "");
    vat.item.setValueByName("#F.status", "");
}
function doFormAccessControl(){
	var userType   = vat.item.getValueByName("userType");
	var orderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");
	var status   = vat.item.getValueByName("#F.status");
	var department   = vat.item.getValueByName("#F.department");
	var loginDepartment   = vat.item.getValueByName("#loginDepartment");
	vat.item.setStyleByName("#B.view" , "display", "none");
	
	if(vat.item.getValueByName("#loginDepartment")!="103"&&document.forms[0]["#loginEmployeeCode"].value!='T76927')
	{
	
		 vat.item.setAttributeByName("#F.status", "readOnly", true);
		 vat.item.setAttributeByName("#F.orderTypeCode", "readOnly", true);
		 vat.item.setAttributeByName("#F.department", "readOnly", true);
		 vat.item.setStyleByName("#B.work", "display", "none");
		 vat.item.setStyleByName("#B.order", "display", "none");
	}
	if(document.forms[0]["#loginEmployeeCode"].value=='T76927'||vat.item.getValueByName("#loginDepartment")=="103")
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
	    var url = "/erp/BU_PURCHASE:create:20131030.page?formId=" + vFormId
	    		  +"&orderTypeCode=" + document.forms[0]["#orderTypeCode"].value
	    		  +"&userType=" + document.forms[0]["#userType"].value; 
	     sc=window.open(url, '需求單作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');
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
function advanceInput(){
    var nItemLine = vat.item.getGridLine();
    var vFormId = vat.item.getGridValueByName("headId", nItemLine);
	var returnData = window.showModalDialog(
		"BU_PURCHASE:workassign:20140317.page"+
		"?formId=" + vFormId ,"",
		"dialogHeight:450px;dialogWidth:650px;dialogTop:150px;dialogLeft:150px;");
}
function getCalculationData(){

	alert("計算成功");
   
   vat.ajax.XHRequest({
          asyn:false,
          post:"process_object_name=buPurchaseService"+
                   "&process_object_method_name=callCalTaskWork"+
                   "&rqInChargeCode=" + (vat.item.getValueByName("#F.rqInChargeCode")="","ALL",vat.item.getValueByName("#F.rqInChargeCode")),
          find: function change(oXHR){ 
          		vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);	
          } 
	});	
	
}
function getOrderByNew(){

	alert("排序成功");
   
   vat.ajax.XHRequest({
          asyn:false,
          post:"process_object_name=buPurchaseService"+
                   "&process_object_method_name=updateOrderByNew"+
                   "&headId=" + vat.item.getValueByName("#F.headId")+
                   "&department=" + vat.item.getValueByName("#F.department")+
                   "&categoryGroup=" + vat.item.getValueByName("#F.categoryGroup")+
                   "&rqInChargeCode=" + vat.item.getValueByName("#F.rqInChargeCode"),
          find: function change(oXHR){ 
          		//alert("test");
          	  vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);	
          } 
	});	
	
}
// 刷新頁面
function refreshForm(code){
//alert("sss:"+code);
	document.forms[0]["#formId"].value = code; 
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=buPurchaseAction&process_object_method_name=performSearchInitial"
			 
     	},{other      : true, 
     	   funcSuccess:function(){
     	   //alert("oop");
     			vat.item.bindAll(); 
     			//alert("oop1");
     			vat.block.pageRefresh(vnB_Detail);
    
     	}}
    );
    	
}
