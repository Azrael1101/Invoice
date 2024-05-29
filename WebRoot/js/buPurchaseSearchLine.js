
var afterSavePageProcess = "";
var vnB_Detail = 2;
var vnB_Header = 1;
var vnB_Text = 3;
function outlineBlock(){
 	
  searchInitial();
  headerInitial();
  buttonLine();
  detailInitial();
  Initial();
  //textLine();
  doFormAccessControl();
}
// 搜尋初始化
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){

     vat.bean().vatBeanOther = 
  	    {
  	    //orderTypeCode : document.forms[0]["#orderTypeCode"].value,
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,	
  	     vatPickerId        : document.forms[0]["#vatPickerId"       	].value,
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"        ].value,
  	     currentRecordNumber 	: 0,
	     lastRecordNumber    	: 0
  	    // orderTypeCode        : orderTypeCode
  	    //orderTypeCode : document.forms[0]["#orderTypeCode"].value
	    };
	/*vat.bean.init(function(){
		return "process_object_name=buPurchaseAction&process_object_method_name=performSearchInitial";
   		},{other: true});*/
  }
}

// 可搜尋的欄位
function headerInitial(){
	//var allCategroyTypes    = vat.bean("allCategroyTypes");
    //var allproject    = vat.bean("allproject");
 	var alldepartment    = vat.bean("alldepartment");
    var allstatus   = [[true,true,true,true], ["完成","未完成","不處理"],["完成","未完成","不處理"]];
	var allOrderTypes   = [[true, true,true,true,true,true], ["PRC-請採驗","IRQ-需求單","OSP-開店工程","BPR-新人","TAS-任務","LOA-權限"], ["PRC","IRQ","OSP","BPR","TAS","LOA"]];
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"工作計畫回報", 
	 			rows:[
				{row_style:"", cols:[

	
				{items:[{name:"#L.startDate"     , type:"LABEL" , value:"需求日期"}]},
	            {items:[{name:"#F.startDate"     , type:"DATE"  ,  bind:"startDate", size:13},
						{name:"#L.between"       , type:"LABEL" , value:"至"},
	 		 			{name:"#F.endDate"       , type:"DATE"  ,  bind:"endDate"  , size:13}]},
						
					
				{items:[{name:"#L.status", type:"LABEL" , value:"狀態"}]},
				{items:[{name:"#F.status", type:"SELECT", bind:"status", size:25, maxLen:25,init:allstatus }]},
			
				{items:[{name:"#L.incharge", type:"LABEL", value:"執行人<font color='red'>*</font>"}]},
				{items:[{name:"#F.incharge", type:"TEXT", bind:"incharge", size:10, maxLen:25 }]}

			]}
			
				
			
		], 	
		 
		beginService:"",
		closeService:""			
	});
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
	 	 		{name:"#B.export"      , type:"IMG"      , value:"明細匯出",  src:"./images/button_detail_export.gif",  eClick:'doSubmit("EXPORT")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.week"        , type:"IMG"      , value:"本周",  src:"./images/thisweek.GIF", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.day"         , type:"IMG"      , value:"本日",  src:"./images/today.GIF", eClick:"doSearch()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"#B.aaa"         , type:"IMG"      , value:"查詢",src:"./images/icon_a02.GIF",eClick:"advanceInput()"}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}

	 			  
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

function detailInitial(){
  
  var vbCanGridDelete = true;
  var vbCanGridAppend = true;
  var vbCanGridModify = true;

   // vat.item.make(vnB_Detail, "checked"         , {type:"XBOX"});
    vat.item.make(vnB_Detail, "button"  	    , {type:"button" , size:7,  value:"回報" ,desc:"功能"      });
    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"                                      , desc:"序號"      });
	vat.item.make(vnB_Detail, "incharge"	    , {type:"HIDDEN" , size:5, maxLen:10, mode:"READONLY", desc:"執行人員"      });
	vat.item.make(vnB_Detail, "specInfo"  	    , {type:"TEXT" , size:10, maxLen:50,mode:"READONLY", desc:"工作項目"      });
	vat.item.make(vnB_Detail, "status"  	    , {type:"TEXT" , size:5, maxLen:50, mode:"READONLY", desc:"狀態"      });
	vat.item.make(vnB_Detail, "mon"				, {type:"TEXT" , size:5, maxLen:1, mode:"READONLY", desc:"一　　　　二　　　　三　　　　四　　　　五　　　　六　　　　日"});
	vat.item.make(vnB_Detail, "tue"				, {type:"HIDDEN" , size:5, maxLen:1, mode:"READONLY", desc:"二"});
	vat.item.make(vnB_Detail, "wed"				, {type:"HIDDEN" , size:5, maxLen:1, mode:"READONLY", desc:"三"});
	vat.item.make(vnB_Detail, "thu"				, {type:"HIDDEN" , size:5, maxLen:1, mode:"READONLY", desc:"四"});
	vat.item.make(vnB_Detail, "fri"				, {type:"HIDDEN" , size:5, maxLen:1, mode:"READONLY", desc:"五"});
	vat.item.make(vnB_Detail, "sat"				, {type:"HIDDEN" , size:5, maxLen:1, mode:"READONLY", desc:"六"});
	vat.item.make(vnB_Detail, "sun"				, {type:"HIDDEN" , size:5, maxLen:1, mode:"READONLY", desc:"日"});

	vat.item.make(vnB_Detail, "lineId"  , {type:"ROWID"   });
	
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["lineId"],
														selectionType       : "CHECK",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "saveSuccessAfter()",
														blockId             : vnB_Detail
														});
														

}
// 可搜尋的欄位
function Initial(){
	vat.block.create( vnB_Header, {
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"", 
	 			rows:[
				{row_style:"", cols:[	
				{items:[
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#F.total1", type:"TEXT", bind:"total1", size:3, maxLen:25},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#F.total2", type:"TEXT", bind:"total2", size:3, maxLen:25 },
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#F.total3", type:"TEXT", bind:"total3", size:3, maxLen:25},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#F.total4", type:"TEXT", bind:"total4", size:3, maxLen:25 },
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#F.total5", type:"TEXT", bind:"total5", size:3, maxLen:25},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#F.total6", type:"TEXT", bind:"total6", size:3, maxLen:25 },
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"SPACE"          , type:"LABEL"    , value:"　"},
				{name:"#F.total7", type:"TEXT", bind:"total7", size:3, maxLen:25}]}
				
		

			]}
			
				
			
		], 	
		 
		beginService:"",
		closeService:""			
	});
}
function textLine(){
    vat.block.create(vnB_Text = 3, {
	id: "vatBlock_Text", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[			  
	 {row_style:"", cols:[
	 
	 	{items:[{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name : "#F.total1", type : "NUMM", bind : "total1", size :10, maxLen : 25},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 		 	{name : "#F.total2", type : "TEXT", bind : "total2", size :10, maxLen : 25},
	 		 	{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name : "#F.total3", type : "TEXT", bind : "total3", size :10, maxLen : 25},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name : "#F.total4", type : "TEXT", bind : "total4", size :10, maxLen : 25},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name : "#F.total5", type : "TEXT", bind : "total5", size :10, maxLen : 25},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name : "#F.total6", type : "TEXT", bind : "total6", size :10, maxLen : 25},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 			{name : "#F.total7", type : "TEXT", bind : "total7", size :10, maxLen : 25}
	 			],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}		  
	  ], 	 
		beginService:"",
		closeService:""			
	});
}

// 載入成功後
function loadSuccessAfter(){
 alert("載入成功");	
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
   alert("載入之前");	
   
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXBuPurchaseSearchLinePageData" + 
	                  "&status"       + "=" + vat.item.getValueByName("#F.status"   ) +
	                  "&total1"       + "=" + vat.item.getValueByName("#F.total1"   ) +
	                  "&incharge"     + "=" + vat.item.getValueByName("#F.incharge" );
	              //alert("processString="+processString);     
                                                                            
	return processString;											
}

function saveSuccessAfter(){
 alert("存檔成功");
 	var processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXHeadTotalAmount" ;	
}

//	判斷是否要關閉LINE
function checkEnableLine() {
	return true;
}

//	取得SAVE要執行的將搜尋結果存到暫存檔
function saveBeforeAjxService() {
	alert("存檔之前");
	var processString = "";
	if (checkEnableLine()) {
		processString = "process_object_name=buPurchaseService&process_object_method_name=saveSearchResult1";
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
    		                     function(){ return "process_object_name=buPurchaseService&process_object_method_name=getSearchSelection";
    		                               }, { bind:true, link:true, other:true, picker:true, isPicker:true, blockId:vnB_Detail} );
    			}}); 
}

// 查詢按下後
function doSearch(){

	var nItemLine        = vat.item.getGridLine();
	var mon              = vat.item.getGridValueByName("mon",nItemLine);
	var total1           = vat.item.getValueByName("total1",nItemLine);
	
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
			           

	var total1          =(parseFloat(mon) + parseFloat(mon));

	vat.item.setValueByName("total1", total1);
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
    vat.item.setValueByName("#F.startDate", "");
    vat.item.setValueByName("#F.endDate"  , "");
  //vat.item.setValueByName("#F.discount" , "");
    vat.item.setValueByName("#F.status"   , "");
}
function doFormAccessControl(){
	vat.item.setStyleByName("#B.view" , "display", "none");
}
function advanceInput(){
			var nItemLine = vat.item.getGridLine();
			var vLineId = vat.item.getGridValueByName("lineId", nItemLine).replace(/^\s+|\s+$/, '');
			var returnData = window.showModalDialog(
					"BU_PURCHASE:search:workingcondition.page" +
					"?headId=" + vat.item.getValueByName("#F.headId") +
					"&lineId=" + vLineId,"",
					"dialogHeight:300px;dialogWidth:500px;dialogTop:0;dialogLeft:0px;,scrollbars=no");
					
				
	
		}	


