
var vnB_Button = 0;
var vnB_Condition = 1;
var vnB_Header = 2;
var result;
function outlineBlock(){
  searchInitial();
  conditionInitial();
  buttonLine();
  headerInitial();

}

// 搜尋初始化
function searchInitial(){ 
	
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
     vat.bean().vatBeanOther = 
  	    {
  	     loginBrandCode     : document.forms[0]["#loginBrandCode"    ].value,   	
  	     loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" ].value,	
  	     orderTypeCode      : document.forms[0]["#orderTypeCode"     ].value,
  	     formId        		: document.forms[0]["#formId"       ].value,
  	     eanCode			: "",
  	     currentRecordNumber : 0,
	   	 lastRecordNumber    : 0  
	    };
	vat.bean.init(	
  		function(){
			return "process_object_name=imItemAction&process_object_method_name=performEanCodeSearchInitial"; 
    	},{
    		other: true
   	});   
  }
}

// 可搜尋的欄位
function conditionInitial(){ 
	
vat.block.create( vnB_Header, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"<FONT SIZE=4>國際碼查詢作業</FONT>", rows:[  
	 		{row_style:"", cols:[
				{items:[{name:"#L.eanCodeCondition", 		type:"LABEL"  , value:"<FONT SIZE=4>請刷條碼</FONT>"}]},
				{items:[{name:"#F.eanCodeCondition", 		type:"TEXT"   , size:20},
						{name:"#F.itemCodeBack", 			type:"TEXT"   , size:20, mode:"READONLY"}]}	
			]}
		], 	 
		beginService:"",
		closeService:""			
	});

}

function buttonLine(){
	var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
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
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}
	
// 主檔
function headerInitial(){ 
	
	var allEnbles = [["", true, false], ["", "啟用", "停用"], ["","Y", "N"]];
	var allTaxRadio = [["", true, false], ["完稅", "保稅"], ["P", "F"]];
    
	
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"", 
		rows:[
			{row_style:"", cols:[
			    {items:[{name:"#L.brandCode", 		type:"LABEL", 	value:"<FONT SIZE=4>品牌</FONT>"}]},	
				{items:[{name:"#F.brandCode", 		type:"TEXT", 	bind:"brandCode", mode:"HIDDEN" },
			            {name:"#F.brandName", 		type:"TEXT",	style:"font-size: 12pt; ",	bind:"brandName", back:false, mode:"READONLY"}]},
			    {items:[{name:"#L.enable", 			type:"LABEL", 	value:"<FONT SIZE=4>狀態</FONT>"}]},       
			    {items:[{name:"#F.enable", 			type:"SELECT",	style:"font-size: 12pt; ", 	bind:"enable", init:allEnbles, 	mode:"READONLY" }]} 		 	
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.eanCode", 	type:"LABEL", 	value:"<FONT SIZE=4>國際條碼</FONT>"}]},
				{items:[{name:"#F.eanCode", 	type:"TEXT",	style:"font-size: 12pt; ",	size:16, 	bind:"eanCode", mode:"READONLY"}],td:" colSpan=3"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.itemCode", 	type:"LABEL", 	value:"<FONT SIZE=4>品號</FONT>"}]},
				{items:[{name:"#F.itemCode", 	type:"TEXT",	style:"font-size: 12pt; ",	size:16, 	bind:"itemCode", mode:"READONLY"}],td:" colSpan=3"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.unitPrice", 	type:"LABEL", 	value:"<FONT SIZE=4>售價</FONT>"}]},
				{items:[{name:"#F.unitPrice", 	type:"TEXT",	style:"font-size: 12pt; ", size:16, 	bind:"unitPrice", back:false, mode:"READONLY"}],td:" colSpan=3"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.taxType", 	type:"LABEL", 	value:"<FONT SIZE=4>稅別</FONT>"}]},
				{items:[{name:"#F.taxType", 	type:"SELECT",	style:"font-size: 12pt; ",	size:16, 	bind:"taxType", init:allTaxRadio, back:false, mode:"READONLY"}],td:" colSpan=3"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.itemBrand", 	type:"LABEL", 	value:"<FONT SIZE=4>商品品牌</FONT>"}]},
				{items:[{name:"#F.itemBrand", 	type:"TEXT",	size:16, 	bind:"itemBrand", back:false, mode:"READONLY"},
						{name:"#F.itemBrandName", 	type:"TEXT",	style:"font-size: 12pt; ",	size:16, 	bind:"itemBrandName", back:false, mode:"READONLY"}]},
				{items:[{name:"#L.category02", 	type:"LABEL", 	value:"<FONT SIZE=4>中類</FONT>"}]},
				{items:[{name:"#F.category02", 	type:"TEXT",	style:"font-size: 12pt; ",	size:16, 	bind:"category02", back:false, mode:"READONLY"},
						{name:"#F.category02Name", 	type:"TEXT",	style:"font-size: 12pt; ",	size:16, 	bind:"category02Name", back:false, mode:"READONLY"}]}		
			]},
			{row_style:"", cols:[
			    {items:[{name:"#L.createBy",		type:"LABEL", 	value:"<FONT SIZE=4>建檔人員</FONT>" }]},
				{items:[{name:"#F.createBy",		type:"TEXT", 	bind:"createBy", mode:"HIDDEN"},
					{name:"#F.createByName",		type:"TEXT",	style:"font-size: 12pt; ", 	bind:"createByName", back:false, mode:"READONLY"}]},
			    {items:[{name:"#L.creationDate",	type:"LABEL", 	value:"<FONT SIZE=4>建檔日期</FONT>" }]},
				{items:[{name:"#F.creationDate",	type:"DATE",	style:"font-size: 12pt; ", 	bind:"creationDate", mode:"READONLY"}]} 
			]},
			{row_style:"", cols:[
			    {items:[{name:"#L.lastUpdatedBy",		type:"LABEL", 	value:"<FONT SIZE=4>修改人員</FONT>" }]},
				{items:[{name:"#F.lastUpdatedBy",		type:"TEXT", 	bind:"lastUpdatedBy", mode:"HIDDEN"},
					{name:"#F.lastUpdatedByName",		type:"TEXT",	style:"font-size: 12pt; ", 	bind:"lastUpdatedByName", back:false, mode:"READONLY"}]},
			    {items:[{name:"#L.lastUpdateDate",	type:"LABEL", 	value:"<FONT SIZE=4>修改日期</FONT>" }]},
				{items:[{name:"#F.lastUpdateDate",	type:"DATE",	style:"font-size: 12pt; ", 	bind:"lastUpdateDate", mode:"READONLY"}]} 
			]}		
		], 	
		beginService:"",
		closeService:""			
	});
}	
		
// 查詢按下後
function doSearch(){
	var eanCodeCondition = vat.item.getValueByName("#F.eanCodeCondition").replace(/^\s+|\s+$/, '');
	var brandCode = vat.bean().vatBeanOther.loginBrandCode;
	
	var rows = new Array();
	if(eanCodeCondition == ""){ 
		alert('條碼不可為空');
	}else{
		vat.ajax.XHRequest({ 
				post:"process_object_name=imItemService"+
	            		"&process_object_method_name=getAJAXEanCode"+
	            		"&eanCodeCondition="+ eanCodeCondition + 
	                	"&brandCode=" + brandCode,   
	                	       
				find: function changeRequestSuccess(oXHR){
					if( "" !== vat.ajax.getValue("errorMsg", oXHR.responseText)   ){
						alert(vat.ajax.getValue("errorMsg", oXHR.responseText));
					}else{
						var keys = vat.ajax.getValue("keys", oXHR.responseText);
						if("" != keys){
							rows = keys.split("{S}");
							result = new Array(rows.length);
							for(var i = 0; i < rows.length; i++){
								var key = rows[i].split(",");
								result[i] = {
									itemCode:key[0], // itemCode
									eanCode:key[1] // eanCode
								};
							}
						}
						
						var vsMaxSize = rows.length;
					    if( vsMaxSize === 0){
					    	alert("您輸入條件查無資料");
					    	vat.bean().vatBeanOther.formId				= "";
					    	vat.bean().vatBeanOther.eanCode 			= "";
					  		vat.bean().vatBeanOther.firstRecordNumber   = 0;
						  	vat.bean().vatBeanOther.lastRecordNumber    = 0;
						  	vat.bean().vatBeanOther.currentRecordNumber = 0;
						  	refreshForm();
						}else{
							vat.bean().vatBeanOther.formId				= result[0]['itemCode'];
						  	vat.bean().vatBeanOther.firstRecordNumber   = 1;
						  	vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
						  	vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
						  	vat.bean().vatBeanOther.eanCode 			= result[0]['eanCode'];//;
						  	refreshForm(); // vsItemId
						}
						vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
						vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
					}
				}
		});
	}
}


function gotoFirst(){
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
//	        vsHeadId = result[vat.bean().vatBeanOther.currentRecordNumber -1];
			vat.bean().vatBeanOther.formId				= result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['itemCode'];
			vat.bean().vatBeanOther.eanCode 			= result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['eanCode'];
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm();
	    }else{
	  	    alert("目前已在第一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoForward(){
    if(vat.bean().vatBeanOther.firstRecordNumber > 0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
//	  	    vsHeadId = result[vat.bean().vatBeanOther.currentRecordNumber -1];
	  	   vat.bean().vatBeanOther.formId				= result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['itemCode'];
			vat.bean().vatBeanOther.eanCode 			= result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['eanCode'];
	  	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	    refreshForm();
	    }else{
	  	    alert("目前已在第一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoNext(){	
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
//	  	    vsHeadId = result[vat.bean().vatBeanOther.currentRecordNumber -1];
	  	    vat.bean().vatBeanOther.formId				= result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['itemCode'];
			vat.bean().vatBeanOther.eanCode 			= result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['eanCode'];
	  	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	    refreshForm();
	    }else{
	  	   alert("目前已在最後一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
	}
}

function gotoLast(){
    if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	    if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	        vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
//	        vsHeadId = result[vat.bean().vatBeanOther.currentRecordNumber -1];
	        vat.bean().vatBeanOther.formId				= result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['itemCode'];
			vat.bean().vatBeanOther.eanCode 			= result[vat.bean().vatBeanOther.currentRecordNumber -1 ]['eanCode'];
	        vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	        refreshForm();
	    }else{
	  	    alert("目前已在最後一筆資料");
	    }
	}else{
		alert("無資料可供翻頁");
	}
}

// 刷新頁面
function refreshForm(){   

	 vat.bean().vatBeanOther = { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: vat.bean().vatBeanOther.formId,
          eanCode				: vat.bean().vatBeanOther.eanCode,
          firstRecordNumber		: vat.bean().vatBeanOther.firstRecordNumber,
          currentRecordNumber 	: vat.bean().vatBeanOther.currentRecordNumber,
	      lastRecordNumber    	: vat.bean().vatBeanOther.lastRecordNumber
     };
	vat.block.submit(
		function(){
				return "process_object_name=imItemAction&process_object_method_name=performEanCodeSearchInitial"; 
	   	},{ other: true, 
     		funcSuccess:function(){
     	   	vat.item.bindAll();
     	}}
    );
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}

// 清除
function resetForm(){
	vat.item.setValueByName("#F.eanCodeCondition", "");
	vat.bean().vatBeanOther.eanCode = "";
	vat.bean().vatBeanOther.formId = "";
	vat.bean().vatBeanOther.firstRecordNumber = 0;
	vat.bean().vatBeanOther.currentRecordNumber = 0;
	vat.bean().vatBeanOther.lastRecordNumber = 0;
	refreshForm();
	vat.item.setValueByName("#L.currentRecord", "");
	vat.item.setValueByName("#L.maxRecord"    , "");
}