/*** 
 *	檔案: posControl.js
 *	說明：POS_CONTROL查詢維護
 *	修改：Kenny
 *  <pre>
 *  	Created by Kenny
 *  	All rights reserved.
 *  </pre>
 */



vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;
var vnB_Detail = 2;
function outlineBlock(){
  	formDataInitial();
	kweButtonLine();
  	headerInitial();
  	    if (typeof vat.tabm != 'undefined') {
        vat.tabm.createTab(0, "vatTabSpan", "H", "float");
        vat.tabm.createButton(0, "xTab1", "CONTROL明細", "vatDetailDiv", "images/tab_detail_data_dark.gif", "images/tab_detail_data_light.gif", true, "");
    }
  	detailInitial();

}

function formDataInitial(){
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = 
		{
			loginBrandCode		:	document.forms[0]["#loginBrandCode" ].value,
			loginEmployeeCode	:	document.forms[0]["#loginEmployeeCode" ].value
    	}; 
	    
		vat.bean.init(function(){
			return "process_object_name=PosControlAction&process_object_method_name=performInitial"; 
		},{other: true});
		vat.item.bindAll();
	}
}

function kweButtonLine(){
	vat.block.create(vnB_Button, {id: "vatBlock_Button", generate: true,
	title:"", rows:[	 
	{row_style:"", cols:[
	 	{items:[	 	        
	 			{name:"#B.search"      , type:"IMG"      , value:"查詢",  src:"./images/button_find.gif", eClick:"doSearch()"},
	 			{name:"SPACE"       	, type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'}	 	 		
	 	 		],
	 				td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}
	 			]}
				//{name:"#B.message"  	, type:"IMG"    ,value:"訊息提示"	,   src:"./images/button_message_prompt.gif", eClick:'showMessage()'}
	 	],
		beginService:"",
		closeService:""			
	});
}

function headerInitial(){ 	
	var bCodeId = vat.bean("bCodeId");
	vat.block.create( vnB_Header, { 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"POS_CONTROL查詢維護", 
		rows:[
			{row_style:"", cols:[
	 			{items:[{name:"#L.brandCode"			, type:"LABEL"	, value:"品牌"}]},	
				{items:[{name:"#F.brandCode"			, type:"SELECT"	, bind:"brandCode",init:bCodeId }]},
			    {items:[{name:"#L.downloadFunction"			, type:"LABEL", value:"類型"}]},
				{items:[{name:"#F.downloadFunction"			, type:"SELECT", bind:"downloadFunction"}]},
				{items:[{name:"#L.posMachineCode"			, type:"LABEL", value:"機台號碼"}]},
				{items:[{name:"#F.posMachineCode"			, type:"SELECT", bind:"posMachineCode"}]}
			]}
		], 	
		beginService:"",
		closeService:function(){closeHeader();}	
	});	  	
}

function closeHeader(){
vat.ajax.XHRequest({ 
	post:"process_object_name=posDUService"+
          		"&process_object_method_name=findDownloadCommon"+
          		"&brandCode=" + document.forms[0]["#loginBrandCode" ].value,
          asyn:false,                      
	find: function change(oXHR){
		vat.item.SelectBind(eval(vat.ajax.getValue("allDownloadFunction", oXHR.responseText)),{ itemName : "#F.downloadFunction" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allShop"			, oXHR.responseText)),{ itemName : "#F.shopCode" });
		vat.item.SelectBind(eval(vat.ajax.getValue("allBuShopMachine"	, oXHR.responseText)),{ itemName : "#F.posMachineCode" });
		vat.item.bindAll();
		}
	});
}
function detailInitial(){
  var vatPickerId =vat.bean().vatBeanOther.vatPickerId;
  var vbCanGridDelete = false;
  var vbCanGridAppend = false;
  var vbCanGridModify = true;

    vat.item.make(vnB_Detail, "indexNo"         , {type:"IDX"  ,                     desc:"序號"      });
	vat.item.make(vnB_Detail, "brandCode"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"品牌"      });
	vat.item.make(vnB_Detail, "dataType"		, {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"類型"      });
	vat.item.make(vnB_Detail, "executeType"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"執行時機類型"   });
	vat.item.make(vnB_Detail, "frequence"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"執行頻率"   });
	vat.item.make(vnB_Detail, "index"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"執行順序"   });
	vat.item.make(vnB_Detail, "machineCode"  , {type:"TEXT" , size:18, maxLen:20, mode:"READONLY", desc:"機台"   });
	vat.item.make(vnB_Detail, "timeout"  , {type:"TEXT" , size:18, maxLen:20, mode:"HIDDEN", desc:"TIMEOUT"   });
	vat.item.make(vnB_Detail, "transfer"  , {type:"TEXT" , size:18, maxLen:20, mode:"HIDDEN", desc:"上下傳方向"   });
	vat.item.make(vnB_Detail, "remark"  , {type:"TEXT" , size:18, maxLen:20, mode:"HIDDEN", desc:"備註"   });		
	vat.item.make(vnB_Detail, "brandCode"      	, {type:"ROWID"});
	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
														searchKey           : ["brandCode","dataType","machineCode"],
														selectionType       : "",
														indexType           : "AUTO",
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
														loadBeforeAjxService: "loadBeforeAjxService()",	
														loadSuccessAfter    : "loadSuccessAfter()", 						
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "",
														indicate            : function(){if(vatPickerId != null && vatPickerId != ""){return false}else{ openModifyPage()} }
														});

}


function loadBeforeAjxService(){
	var processString = "process_object_name=posControlService&process_object_method_name=getSearchData" + 
	                  "&brandCode"       	+ "=" + vat.item.getValueByName("#F.brandCode"   ) +
	                  "&downloadFunction"    	+ "=" + vat.item.getValueByName("#F.downloadFunction"  ) +
	                  "&machineCode"    	+ "=" + vat.item.getValueByName("#F.posMachineCode"  );    
                                                                            
	return processString;
}

function doSearch(){
    vat.block.submit(function(){return "process_object_name=tmpAjaxSearchDataService"+
			                    "&process_object_method_name=deleteByTimeScope&timeScope="+ vat.bean().vatBeanOther.timeScope }, 
			                    {other: true, 
			                     funcSuccess: function() {vat.block.pageDataLoad(vnB_Detail , vnCurrentPage = 1);}
			                    });
}

function openModifyPage(){
	

    var nItemLine = vat.item.getGridLine();
	
	var brandCode = vat.item.getGridValueByName("brandCode",nItemLine);
	var dataType = vat.item.getGridValueByName("dataType",nItemLine);
    var machineCode = vat.item.getGridValueByName("machineCode", nItemLine);
    var timeout = vat.item.getGridValueByName("timeout", nItemLine);
    var transfer = vat.item.getGridValueByName("transfer", nItemLine);
    var indexNo = vat.item.getGridValueByName("indexNo", nItemLine);
    var frequence = vat.item.getGridValueByName("frequence", nItemLine);
    var remark = vat.item.getGridValueByName("remark", nItemLine);
    var executeType = vat.item.getGridValueByName("executeType", nItemLine);
	if(!(brandCode == "" && dataType == "" && machineCode == "")){
    var url = "/erp/Pos_Du:create:20111207.page?brandCode=" + brandCode + 
    			"&dataType="+dataType+
    			"&posMachineCode="+machineCode+
    			"&timeout="+timeout+
    			"&transfer="+transfer+
    			"&indexNo="+indexNo+
    			"&frequency="+frequence+
    			"&remark="+remark+
    			"&executeType="+executeType+
    			"&isUpdate=true"; 
	
	sc=window.open(url, 'POS_CONTROL維護作業', 'menubar=no,resizable=no,scrollbars=no,status=no,resizable=yes,scrollbars=yes');

     sc.resizeTo((screen.availWidth),(screen.availHeight));

     sc.moveTo(0,0);
	}
}
function loadSuccessAfter(){

	if( vat.block.getGridObject(2).dataCount == vat.block.getGridObject(2).pageSize &&
	    vat.block.getGridObject(2).lastIndex == 1){
		vat.item.setStyleByName("#B.view" , "display", "none");
		alert("您輸入條件查無資料");
	}else{
		vat.item.setStyleByName("#B.view" , "display", "inline");
	}
}