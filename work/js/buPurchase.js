vat.debug.disable();
var vnB_Button = 0;
var vnB_Header = 1;
var vnB_master = 2;
var vnB_Detail = 3;
var vnB_master1 = 4;
var vnB_Detail2 =6;
var vnB_master2 = 7;
var vnB_Detail3 =8;
var vnB_Detail4 =9;
var vnB_Detail5 =10;
var vnB_Detail6 =11;
//var vatApprovalDiv= 5;
var vnB_master3 =12;
var activeTab = 3;
var vnB_master111=111;
var vnB_vat7 =13;
var vnB_Shop_All=15;
var vnB_Brand = 20;
var vnB_Warehouse = 21;
var vnB_Shop = 22;
var vnB_ItemCategory = 23;

var vnB_Productcost = 24;
var vnB_Post = 25; 

//var vnB_Productcost = 24;



function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
  	//doFormAccessControl();///呼叫doFormAccessControl method存取權限
	if (typeof vat.tabm != 'undefined') {
		vat.tabm.createTab(0, "vatTabSpan", "H", "float");
		if(vat.item.getValueByName("#F.orderTypeCode")=="IRQ")
		{
		vat.tabm.createButton(0 ,"xTab1","主檔資料","vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false); 
		vat.tabm.createButton(0 ,"xTab8","任務"   ,"vatDetailDiv5"   ,"images/tab_task_dark.GIF","images/tab_task_light.GIF", "doPageDataSave()");
		vat.tabm.createButton(0, "xTab9","需求人員附件",	"vatT2Div" ,"images/tab_creator_attachment_dark.gif" ,"images/tab_creator_attachment_light.gif", false);     
		vat.tabm.createButton(0, "xTab15", "簽核資料", "vatApprovalDiv", "images/tab_approval_data_dark.gif", "images/tab_approval_data_light.gif");
		}
		if(vat.item.getValueByName("#F.orderTypeCode")=="PRC")
		{
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false); 
		vat.tabm.createButton(0 ,"xTab2","採購明細"     ,"vatDetailDiv"    ,"images/tab_po_detail_dark.gif","images/tab_po_detail_light.gif", "doPageDataSave(3)");
		}
		if(vat.item.getValueByName("#F.orderTypeCode")=="OSP")
		{
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false); 
		vat.tabm.createButton(0 ,"xTab3","派工主檔"     ,"vatBlock_Master1" ,"images/tab_main_assgin_dark.GIF" ,"images/tab_main_assgin_light.GIF", false); 
		vat.tabm.createButton(0 ,"xTab4","派工"        ,"vatDetailDiv2" ,"images/tab_assgin_dark.GIF","images/tab_assign_light.GIF","doPageDataSave()");
		}
		if(vat.item.getValueByName("#F.orderTypeCode")=="TASK")
		{
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false); 
		vat.tabm.createButton(0 ,"xTab8","任務"        ,"vatDetailDiv5" ,"images/tab_task_dark.GIF","images/tab_task_light.GIF", "doPageDataSave()");
		}
		//kwe權限
	   if(vat.item.getValueByName("#type")=="1"&vat.item.getValueByName("#F.orderTypeCode")=="LOA")
		{
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);    
        vat.tabm.createButton(0 ,"xTab6","KWE申請"     ,"vatDetailDiv3" ,"images/tab_kwe_dark.GIF","images/tab_kwe_light.GIF");
        vat.tabm.createButton(0 ,"xTab10","品牌申請"    ,"vatBrandDetailDiv" ,"images/tab_brand_detail_dark.GIF","images/tab_brand_detail_light.GIF",false,"doPageDataSave("+vnB_Brand+")");
       	vat.tabm.createButton(0 ,"xTab11","庫別申請"    ,"vatWarehouseDetailDiv" ,"images/tab_warehouse_detail_dark.GIF","images/tab_warehouse_detail_light.GIF",false,"doPageDataSave("+vnB_Warehouse+")");
		vat.tabm.createButton(0 ,"xTab12","店別申請"    ,"vatShopDetailDiv" ,"images/tab_shop_detail_dark.GIF","images/tab_shop_detail_light.GIF",false,"doPageDataSave("+vnB_Shop+")");
		//vat.tabm.createButton(0 ,"xTab13","業種申請"    ,"vatItemCategoryDetailDiv" ,"images/tab_Catrgory_detail_dark.GIF","images/tab_Category_detail_light.GIF",false,"doPageDataSave("+vnB_ItemCategory+")");
		//vat.tabm.createButton(0 ,"xTab14","成本申請"    ,"vatProductcostDetailDiv" ,"images/tab_productcost_detail_dark.GIF","images/tab_productcost_detail_light.GIF");
		}
		//email權限
	   if(vat.item.getValueByName("#type")=="2"&vat.item.getValueByName("#F.orderTypeCode")=="LOA")
		{
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);    
	  	vat.tabm.createButton(0 ,"xTab5","EMAIL主檔"    ,"vatBlock_Master2" ,"images/tab_main_email_dark.GIF" ,"images/tab_main_email_light.GIF", false); 
        vat.tabm.createButton(0 ,"xTab9","EMAIL通訊錄"  ,"vatDetailDiv6" ,"images/tab_email_dark.GIF","images/tab_email_light.GIF");
		}
		//共用槽權限
	   if(vat.item.getValueByName("#type")=="3"&vat.item.getValueByName("#F.orderTypeCode")=="LOA")
		{
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);    
	   	vat.tabm.createButton(0 ,"xTab7","電腦權限申請"   ,"vatDetailDiv4" ,"images/tab_fireServer_dark.GIF","images/tab_fireServer_light.GIF");
		}
		//新人槽權限
	   if(vat.item.getValueByName("#type")=="4"&vat.item.getValueByName("#F.orderTypeCode")=="LOA")
		{
		vat.tabm.createButton(0 ,"xTab1","主檔資料"     ,"vatBlock_Master" ,"images/tab_master_data_dark.gif" ,"images/tab_master_data_light.gif", false);    
		vat.tabm.createButton(0 ,"xTab2","採購明細"     ,"vatDetailDiv"    ,"images/tab_po_detail_dark.gif","images/tab_po_detail_light.gif");
		//vat.tabm.createButton(0 ,"xTab3","派工主檔"     ,"vatBlock_Master1" ,"images/tab_main_assgin_dark.GIF" ,"images/tab_main_assgin_light.GIF", false); 
		//vat.tabm.createButton(0 ,"xTab4","派工"        ,"vatDetailDiv2" ,"images/tab_assgin_dark.GIF","images/tab_assign_light.GIF","doPageDataSave()");
        vat.tabm.createButton(0 ,"xTab5","EMAIL主檔"    ,"vatBlock_Master2" ,"images/tab_main_email_dark.GIF" ,"images/tab_main_email_light.GIF", false); 
        vat.tabm.createButton(0 ,"xTab9","EMAIL通訊錄"  ,"vatDetailDiv6" ,"images/tab_email_dark.GIF","images/tab_email_light.GIF");
        vat.tabm.createButton(0 ,"xTab6","KWE申請"   ,"vatDetailDiv3" ,"images/tab_kwe_dark.GIF","images/tab_kwe_light.GIF");
       	vat.tabm.createButton(0 ,"xTab7","電腦權限申請"   ,"vatDetailDiv4" ,"images/tab_fireServer_dark.GIF","images/tab_fireServer_light.GIF");
		}
		
	}
	if(vat.item.getValueByName("#F.orderTypeCode")=="IRQ"){
		kweMaster();
		kweDetail5();
		kweDetail7();
		kweWfBlock( vat.item.getValueByName("#F.brandCode"), 
            	vat.item.getValueByName("#F.orderTypeCode"), 
             	vat.item.getValueByName("#F.orderNo"),
             	document.forms[0]["#loginEmployeeCode"].value );
	}
	if(vat.item.getValueByName("#F.orderTypeCode")=="PRC"){
		kweMaster();
		kweDetail();
	}
	if(vat.item.getValueByName("#F.orderTypeCode")=="OSP"){
		kweMaster();
		kweMaster1();
		kweDetail2();
	}
	if(vat.item.getValueByName("#F.orderTypeCode")=="TASK"){
		kweMaster();
		kweDetail5();
	}
	//kwe權限
	if(vat.item.getValueByName("#type")=="1"&vat.item.getValueByName("#F.orderTypeCode")=="LOA"){
		
		kweMaster();
        //kweDetail3();
        menuSetBlock();
        //menuBrandBlock();
        //kweBrandDetail();
        //kweWarehouseDetail();
        //kweShopDetail();
        //kweItemCategoryDetail();
        //kweProductcostDetail();
        //kweDetail3();
       	//kweBrandDetail();
       	//kweWarehouseDetail();
       	//kweShopDetail();
        //kweItemCategoryDetail();
        //kweProductcostDetail();

	}
	//email權限
		if(vat.item.getValueByName("#type")=="2"&vat.item.getValueByName("#F.orderTypeCode")=="LOA"){
		kweMaster();
    	kweMaster2();
    	kweDetail6();
	}
	//供用槽權限
	if(vat.item.getValueByName("#type")=="3"&vat.item.getValueByName("#F.orderTypeCode")=="LOA"){
	   kweMaster();
       kweDetail4();
	}
	//新人權限
	if(vat.item.getValueByName("#type")=="4"&vat.item.getValueByName("#F.orderTypeCode")=="LOA"){
	   kweMaster();
	   kweDetail();
       kweMaster2();
       //kweDetail3();
       menuSetBlock();
       kweDetail4();
       kweDetail6();
	}
	
	doFormAccessControl();///呼叫doFormAccessControl method存取權限
    //ApprovalInitial();
  //簽核頁面
  /*kweWfBlock( vat.item.getValueByName("#F.brandCode"), 
            	vat.item.getValueByName("#F.orderTypeCode"), 
             	vat.item.getValueByName("#F.orderNo"),
             	document.forms[0]["#loginEmployeeCode"].value );*/
}

function kweDetail(){
    var isPartialShipments = [[true, true], ["未","含"], ["未","含"]];
    var allitemName     = vat.bean("allitemName");
    var vbCanGridDelete = true;
    var vbCanGridAppend = true;
    var vbCanGridModify = true;
    var vnB_Detail = 3;
    vat.item.make(vnB_Detail, "lineId"              , {type:"hidden"  ,                       desc:"lineId"                                        });
    vat.item.make(vnB_Detail, "indexNo"             , {type:"IDX"     ,                       desc:"序號"                                          });
    vat.item.make(vnB_Detail, "categoryCode"        , {type:"hidden"  ,                       desc:"單種分類" ,value:"PR"                           });
	vat.item.make(vnB_Detail, "itemNo"              , {type:"TEXT"    , size: 10, maxLen:50,  desc:"品號"    ,eChange:"onChangeItemNo()"           });//,eChange:"onChangeItemCode()"
	vat.item.make(vnB_Detail, "itemName"            , {type:"SELECT"    , size: 15,  			  desc:"品名"      ,init:allitemName,onchange:"onChangeItemName()"                                     });
	vat.item.make(vnB_Detail, "specInfo"            , {type:"TEXT"    , size:15,              desc:"規格"                                          });
	vat.item.make(vnB_Detail, "quantity"        	, {type:"NUMM"    , size: 5,              desc:"數量"    ,onchange:"calculateLineAmount()"     });//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail, "taxEnable"        	, {type:"SELECT"  , size: 1,              desc:"含/未稅" ,init:isPartialShipments              });//,onchange:"calculateLinetaxAmount()"
	vat.item.make(vnB_Detail, "reUnitPrice"         , {type:"NUMM"    , size: 6,              desc:"請購單價" ,onchange:"calculateLineAmount()"    });//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail, "reTotalAmount"       , {type:"NUMM"    , size: 6,              desc:"請購金額" ,mode:"READONLY"                     });
	vat.item.make(vnB_Detail, "purUnitPrice"        , {type:"NUMM"    , size: 6 ,             desc:"採購單價" ,onchange:"calculateLineAmount()"    });//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail, "purTotalAmount"      , {type:"NUMM"    , size: 6,              desc:"採購金額" ,mode:"READONLY"                     });
	vat.item.make(vnB_Detail, "supplier"            , {type:"TEXT"    , size: 10,             desc:"供應商"                                        });
	vat.item.make(vnB_Detail, "yearBudget"          , {type:"TEXT"    , size: 10,             desc:"年度預算(項次)"                                       });
	vat.item.make(vnB_Detail, "isDeleteRecord"      , {type:"DEL"     ,	 view:"fixed",        desc:"刪除"                                         });

	vat.block.pageLayout(vnB_Detail, {
														id                  : "vatDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                        //closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail+")",
														//loadSuccessAfter    : "",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "",
													    blockId             : "3"
														});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

//派工
function kweDetail2(){
  
	var allfix    = vat.bean("allfix");
    var vbCanGridDelete = true;
    var vbCanGridAppend = true;
    var vbCanGridModify = true;
    var vnB_Detail2 = 6;
    vat.item.make(vnB_Detail2, "lineId"          , {type:"hidden"  ,                     desc:"lineId"       });
    vat.item.make(vnB_Detail2, "indexNo"         , {type:"IDX"  ,   view:"fixed",                  desc:"序號"       });
    vat.item.make(vnB_Detail2, "categoryCode"   ,  {type:"hidden"  ,                     desc:"單種分類" ,value:"OSP"   });
	vat.item.make(vnB_Detail2, "itemNo"          , {type:"hidden" , size: 10, maxLen:50,  desc:"品號"   });//,eChange:"onChangeItemCode()"
	vat.item.make(vnB_Detail2, "itemName"        , {type:"TEXT" , size: 20,  desc:"項目", init:allfix });
	vat.item.make(vnB_Detail2, "specInfo"        , {type:"TEXT" ,   size:30,  view:"fixed",    desc:"說明"});
	vat.item.make(vnB_Detail2, "quantity"        , {type:"TEXT" , size: 10,  desc:"議價時數",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail2, "taxEnable"       , {type:"hidden" , size: 1,  desc:"含/未稅"});//,onchange:"calculateLinetaxAmount()"
	vat.item.make(vnB_Detail2,"reUnitPrice"      , {type:"hidden" , size: 6,  desc:"請購單價",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail2, "reTotalAmount"   , {type:"hidden" , size: 6, desc:"請購金額", mode:"READONLY"});
	vat.item.make(vnB_Detail2, "purUnitPrice"    , {type:"hidden" , size: 6 , desc:"採購單價",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail2, "purTotalAmount"  , {type:"hidden" , size: 6,  desc:"採購金額", mode:"READONLY"});
	vat.item.make(vnB_Detail2, "supplier"        , {type:"hidden" , size: 30,  desc:"其他"   });
	vat.item.make(vnB_Detail2, "yearBudget"          , {type:"hidden"    , size: 10,             desc:"年度預算(項次)"                                       });
	vat.item.make(vnB_Detail2, "isDeleteRecord"  , {type:"hidden" , desc:"刪除"});
	
	vat.item.make(vnB_Detail2, "shopCode",           {type:"TEXT",   view:"",size: 10,  desc:"店號"});
	vat.item.make(vnB_Detail2, "posMachineCode"  , {type:"TEXT",   view:"",size: 10,  desc:"機台"});
	vat.item.make(vnB_Detail2, "suppilerCode"  , {type:"TEXT",   view:"",size: 10,  desc:"廠商代號"});
    vat.item.make(vnB_Detail2, "suppilerName"         , {type:"TEXT",   view:"",size: 10,  desc:"廠商名稱"});
    vat.item.make(vnB_Detail2, "assignMenuDateStart" , {type:"DATE",   view:"shift",size: 10,  desc:"派工日期(起)"});
    vat.item.make(vnB_Detail2," assignMenuTimeStart" , {type:"TIME",   view:"shift",size: 10,  desc:"派工時間(起)"});
	vat.item.make(vnB_Detail2, "supportNo", {type:"TEXT",   view:"shift",size: 10,  desc:"支援單號"});
	vat.item.make(vnB_Detail2, "executeDateStart", {type:"TEXT",   view:"shift",size: 10,  desc:"處理日期(起)"});
	vat.item.make(vnB_Detail2, "executeTimeStart"        , {type:"DATE",   view:"shift",size: 10,  desc:"處理時間(起)"});
	vat.item.make(vnB_Detail2, "executeDateEnd", {type:"TEXT",   view:"shift",size: 10,  desc:"處理日期(迄)"});
	vat.item.make(vnB_Detail2, "executeTimeEnd", {type:"TEXT",   view:"shift",size: 10,  desc:"處理時間(迄)"});
	vat.item.make(vnB_Detail2, "executeMemo"         , {type:"TEXT",   view:"shift",size: 10,  desc:"處理說明"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail2, "executeInCharge"      , {type:"DATE",   view:"shift",size: 10,  desc:"處理人員"});//,onchange:"calculateLinetaxAmount()"

	
	vat.block.pageLayout(vnB_Detail2, {
														id                  : "vatDetailDiv2",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail2+")",
														//loadSuccessAfter    : "",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "",
													    blockId             : "3"
														});
}
//EMAIL
function kweDetail6(){
	var allfix    = vat.bean("allfix");
    var vbCanGridDelete = true;
    var vbCanGridAppend = true;
    var vbCanGridModify = true;
    var vnB_Detail6 = 11;
    vat.item.make(vnB_Detail6, "lineId"          , {type:"hidden" ,                     desc:"lineId"       });
    vat.item.make(vnB_Detail6, "indexNo"         , {type:"IDX"    ,   view:"fixed",                  desc:"序號"       });
    vat.item.make(vnB_Detail6, "categoryCode"    ,  {type:"hidden"  ,                     desc:"單種分類" ,value:"TASK"   });
	vat.item.make(vnB_Detail6, "itemNo"          , {type:"hidden" , size: 10, maxLen:50,  desc:"品號"   });//,eChange:"onChangeItemCode()"
	vat.item.make(vnB_Detail6, "itemName"        , {type:"hidden" , size: 20,  desc:"項目", init:allfix });
	vat.item.make(vnB_Detail6, "specInfo"        , {type:"TEXT"   ,   size:30,  view:"fixed",    desc:"群組編號"});
	vat.item.make(vnB_Detail6, "specInfo"        , {type:"TEXT"   ,   size:30,  view:"fixed",    desc:"群組名稱"});
	vat.item.make(vnB_Detail6, "quantity"        , {type:"hidden" , size: 10,  desc:"議價時數",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail6, "taxEnable"       , {type:"hidden" , size: 1,  desc:"含/未稅"});//,onchange:"calculateLinetaxAmount()"
	vat.item.make(vnB_Detail6, "reUnitPrice"     , {type:"hidden" , size: 6,  desc:"請購單價",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail6, "reTotalAmount"   , {type:"hidden" , size: 6, desc:"請購金額", mode:"READONLY"});
	vat.item.make(vnB_Detail6, "purUnitPrice"    , {type:"hidden" , size: 6 , desc:"採購單價",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail6, "purTotalAmount"  , {type:"hidden" , size: 6,  desc:"採購金額", mode:"READONLY"});
	vat.item.make(vnB_Detail6, "supplier"        , {type:"hidden" , size: 30,  desc:"其他"   });
	vat.item.make(vnB_Detail6, "yearBudget"          , {type:"hidden"    , size: 10,             desc:"年度預算(項次)"                                       });
	vat.item.make(vnB_Detail6, "isDeleteRecord"  , {type:"hidden" , desc:"刪除"});
	
	vat.item.make(vnB_Detail6, "shopCode",           {type:"TEXT",   view:"",size: 10,  desc:"組織單位"});
	vat.item.make(vnB_Detail6, "adMemberCode"                  , {type:"TEXT" , size: 6,  desc:"群組成員代號", mode:"READONLY"});
	vat.item.make(vnB_Detail6, "posMachineCode"  , {type:"TEXT",   view:"",size: 10,  desc:"群組成員"});
	vat.item.make(vnB_Detail6, "suppilerCode"  , {type:"TEXT",   view:"",size: 10,  desc:"異動"});
	
	vat.block.pageLayout(vnB_Detail6, {
														id                  : "vatDetailDiv6",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail6+")",
														//loadSuccessAfter    : "",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "",
													    blockId             : "3"
														});
}

/*function kweBrandDetail(){
 
	var allfix    = vat.bean("allfix");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    //vat.item.make(vnB_Brand, "employeeCode"                    , {type:"hidden"  					,                     desc:"employeeCode"       });
    vat.item.make(vnB_Brand, "lineId"                   , {type:"hidden"  	, size: '1%'	,                     desc:"lineId"       });
    vat.item.make(vnB_Brand, "indexNo"                   , {type:"IDX"  	, size: '1%'	,                     desc:"序號"       });
   	vat.item.make(vnB_Brand, "enable"        			 , {type:"checkbox" , size: '1%'	,  desc:"啟用"});
    vat.item.make(vnB_Brand, "brandCode"        		 , {type:"TEXT" 	, size: '97%'	,  desc:"品牌代號"});
    vat.item.make(vnB_Brand, "type"        				 , {type:"TEXT" 	, size: '1%'	,value:"brand", desc:"頁籤分類"});
	//vat.item.make(vnB_Brand, "brandName"            	 , {type:"TEXT" 	, size: '98%'	,  desc:"品牌名稱", mode:"READONLY" });
	
	
	vat.block.pageLayout(vnB_Brand, {
														id                  : "vatBrandDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Brand+")",
														//loadSuccessAfter    : "loadSuccessAfter()",	
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Brand+")",
														//saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "20"
														});	
														

	//vat.block.pageDataLoad(vnB_Brand, vnCurrentPage = 1);											


}*/
function kweWarehouseDetail(){

	//var allfix    = vat.bean("allfix");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    
    
    vat.item.make(vnB_Warehouse, "lineId"                   , {type:"HIDDEN"  ,                     desc:"lineId"       });
    vat.item.make(vnB_Warehouse, "indexNo"                   , {type:"IDX"  , size: '1%',                     desc:"序號"       });
    vat.item.make(vnB_Warehouse, "enable"        , {type:"checkbox" , size: '1%',  desc:"啟用"});
	vat.item.make(vnB_Warehouse, "warehouseCode"            , {type:"TEXT" , size: '98%',  desc:"庫別名稱" });
	vat.item.make(vnB_Warehouse, "type"        				 , {type:"TEXT" 	, size: '1%'	,value:"warehouse",  desc:"頁籤分類"});
	

	vat.block.pageLayout(vnB_Warehouse, {
														id                  : "vatWarehouseDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Warehouse+")",
														//loadSuccessAfter    : "loadSuccessAfter()",	
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Warehouse+")",
														//saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "21"
														});	
														

	//vat.block.pageDataLoad(vnB_Warehouse, vnCurrentPage = 1);											

}
function kweShopDetail(){
  
	var allfix    = vat.bean("allfix");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    vat.item.make(vnB_Shop, "lineId"                   , {type:"hidden"  ,                     desc:"lineId"       });
    vat.item.make(vnB_Shop, "indexNo"                   , {type:"IDX"  , size: '1%',                     desc:"序號"       });
    vat.item.make(vnB_Shop, "enable"        , {type:"checkbox" , size: '1%',  desc:"啟用"});
	vat.item.make(vnB_Shop, "shopCode"            , {type:"TEXT" , size: '98%',  desc:"店別名稱" });
	vat.item.make(vnB_Shop, "type"            , {type:"TEXT" , size: '1%',value:"shop",  desc:"頁籤分類" });
	
	
	vat.block.pageLayout(vnB_Shop, {
														id                  : "vatShopDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Shop+")",
														//loadSuccessAfter    : "loadSuccessAfter()",	
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_Shop+")",
														//saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "22"
														});	
														


	//vat.block.pageDataLoad(vnB_Shop, vnCurrentPage = 1);											

}
function kweItemCategoryDetail(){
  
	var allfix    = vat.bean("allfix");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    vat.item.make(vnB_ItemCategory, "lineId"                   , {type:"hidden"  ,                     desc:"lineId"       });
    vat.item.make(vnB_ItemCategory, "indexNo"                   , {type:"IDX"  , size: '1%',                     desc:"序號"       });
    vat.item.make(vnB_ItemCategory, "enable"        , {type:"checkbox" , size: '1%',  desc:"啟用"});
	vat.item.make(vnB_ItemCategory, "categoryCode"            , {type:"TEXT" , size: '98%',  desc:"業種代號" });
	vat.item.make(vnB_ItemCategory, "type"            , {type:"TEXT" , size: '1%',value:"category",  desc:"頁籤分類" });
	
	vat.block.pageLayout(vnB_ItemCategory, {
														id                  : "vatItemCategoryDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_ItemCategory+")",
														//loadSuccessAfter    : "loadSuccessAfter()",	
														saveBeforeAjxService: "saveBeforeAjxService("+vnB_ItemCategory+")",
														//saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "23"
														});	
														

	//vat.block.pageDataLoad(vnB_Detail3, vnCurrentPage = 1);											

}
/*function kweProductcostDetail(){
  
	var allfix    = vat.bean("allfix");
    var vbCanGridDelete = false;
    var vbCanGridAppend = false;
    var vbCanGridModify = true;
    vat.item.make(vnB_Productcost, "menuId"                   , {type:"hidden"  ,                     desc:"menuId"       });
    vat.item.make(vnB_Productcost, "indexNo"                   , {type:"IDX"  , size: '1%',                     desc:"序號"       });
    vat.item.make(vnB_Productcost, "enable"        , {type:"checkbox" , size: '1%',  desc:"啟用"});
	vat.item.make(vnB_Productcost, "name"            , {type:"TEXT" , size: '98%',  desc:"成本名稱", mode:"READONLY" });
	vat.item.make(vnB_Productcost, "enable"        , {type:"SELECT" , size: '1%',  desc:"成本分類"});
	
	
	vat.block.pageLayout(vnB_Productcost, {
														id                  : "vatProductcostDetailDiv",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														//loadBeforeAjxService: "kweloadBeforeAjxService()",
														//loadSuccessAfter    : "loadSuccessAfter()",	
														//saveBeforeAjxService: "saveBeforeAjxService()",
														//saveSuccessAfter    : "saveSuccessAfter()",
													    blockId             : "8"
														});	
														
	vat.block.pageDataLoad(vnB_Detail11, vnCurrentPage = 1);											
}*/
function kweDetail4(){
  
	var allfix    = vat.bean("allfix");
    var vbCanGridDelete = true;
    var vbCanGridAppend = true;
    var vbCanGridModify = true;
    var vnB_Detail4 = 9;
    vat.item.make(vnB_Detail4, "lineId"                   , {type:"hidden"  ,                     desc:"lineId"       });
    vat.item.make(vnB_Detail4, "indexNo"                  , {type:"IDX"  ,                     desc:"序號"       });
    vat.item.make(vnB_Detail4, "categoryCode"             , {type:"hidden"  ,                     desc:"單種分類" ,value:"叫修單"      });
    vat.item.make(vnB_Detail4, "adGroupCode"             , {type:"SELECT"  ,                     desc:"群組編號"      });
	vat.item.make(vnB_Detail4, "itemNo"                   , {type:"hidden" , size: 10, maxLen:50,  desc:"品號" });//,eChange:"onChangeItemCode()"
	vat.item.make(vnB_Detail4, "itemName"                 , {type:"TEXT" , size: 30,  desc:"申請名稱", init:allfix });
	vat.item.make(vnB_Detail4, "specInfo"                 , {type:"TEXT" , size:30,  desc:"資料名稱"});
	vat.item.make(vnB_Detail4, "quantity"        , {type:"hidden" , size: 10,  desc:"標準時數",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail4, "taxEnable"        , {type:"hidden" , size: 1,  desc:"含/未稅"});//,onchange:"calculateLinetaxAmount()"
	vat.item.make(vnB_Detail4, "reUnitPrice"                 , {type:"hidden" , size: 6,  desc:"請購單價",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail4, "reTotalAmount"                  , {type:"hidden" , size: 6, desc:"請購金額", mode:"READONLY"});
	vat.item.make(vnB_Detail4, "purUnitPrice"                 , {type:"hidden" , size: 6 , desc:"採購單價",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail4, "purTotalAmount"                  , {type:"hidden" , size: 6,  desc:"採購金額", mode:"READONLY"});
	vat.item.make(vnB_Detail4, "supplier"                    , {type:"hidden" , size: 30,  desc:"其他"   });
	vat.item.make(vnB_Detail4, "yearBudget"          , {type:"hidden"    , size: 10,             desc:"年度預算(項次)"                                       });
	vat.item.make(vnB_Detail4, "isDeleteRecord",          {type:"hidden",	 view:"fixed", desc:"刪除"});
	
	vat.item.make(vnB_Detail4, "adMemberCode"                  , {type:"TEXT" , size: 6,  desc:"群組成員代號", mode:"READONLY"});
	vat.item.make(vnB_Detail4, "adMemberName"                    , {type:"TEXT" , size: 30,  desc:"群組成員名稱"   });
	vat.item.make(vnB_Detail4, "enable",          {type:"TEXT",	 desc:"異動"});
	
	vat.block.pageLayout(vnB_Detail4, {
														id                  : "vatDetailDiv4",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail4+")",
														//loadSuccessAfter    : "",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "",
													    blockId             : "3"
														});
}

function kweDetail5(){
    var allstatus   = [[true,true,true,true], ["FINISH","UNFINISH","UNEXCUTE","VOID"],["完成","未完成","未處理","作廢"]];
    //var alltaskType   = [[true,true,true,true], ["SA","EXEC","TEST","GOLIFE"],["SA","EXEC","TEST","GOLIFE"]];
    var alltaskType   = vat.bean("alltaskType");
    var alltaskTypeNo   = vat.bean("alltaskTypeNo");
	var allfix    = vat.bean("allfix");
	var loginDepartment   = vat.item.getValueByName("#loginDepartment");
    var vbCanGridDelete = true;
    var vbCanGridAppend = true;
    var vbCanGridModify = true;
    var vnB_Detail5 = 10;
    vat.item.make(vnB_Detail5, "lineId"          , {type:"hidden"  ,                     desc:"lineId"       });
    vat.item.make(vnB_Detail5, "indexNo"         , {type:"IDX"  ,   view:"fixed",                  desc:"序號"       });
    vat.item.make(vnB_Detail5, "categoryCode"    , {type:"hidden"  ,                     desc:"單種分類" ,value:"TASK"   });
	vat.item.make(vnB_Detail5, "itemNo"          , {type:"hidden" , size: 10, maxLen:50,  desc:"品號"   });//,eChange:"onChangeItemCode()"
	vat.item.make(vnB_Detail5, "itemName"        , {type:"hidden" , size: 20,  desc:"申請名稱", init:allfix });
	vat.item.make(vnB_Detail5, "specInfo"        , {type:"TEXT" , 	size: 40,    desc:"工作項目"});
	vat.item.make(vnB_Detail5, "quantity"        , {type:"hidden" , size: 10,  desc:"標準時數",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail5, "taxEnable"       , {type:"hidden" , size: 1,  desc:"含/未稅"});//,onchange:"calculateLinetaxAmount()"
	vat.item.make(vnB_Detail5, "reUnitPrice"     , {type:"hidden" , size: 6,  desc:"請購單價",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail5, "reTotalAmount"   , {type:"hidden" , size: 6,  desc:"請購金額", mode:"READONLY"});
	vat.item.make(vnB_Detail5, "purUnitPrice"    , {type:"hidden" , size: 6 , desc:"採購單價",onchange:"calculateLineAmount()"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail5, "purTotalAmount"  , {type:"hidden" , size: 6,  desc:"採購金額", mode:"READONLY"});
	vat.item.make(vnB_Detail5, "supplier"        , {type:"hidden" , size: 30,  desc:"其他"   });
	vat.item.make(vnB_Detail5, "yearBudget"      , {type:"hidden"    , size: 10,             desc:"年度預算(項次)"                                       });
	vat.item.make(vnB_Detail5, "isDeleteRecord"  , {type:"hidden" , desc:"刪除"});
	
	vat.item.make(vnB_Detail5, "shopCode",           {type:"hidden",   view:"",size: 10,  desc:"店號"});
	vat.item.make(vnB_Detail5, "posMachineCode"  , {type:"hidden",   view:"",size: 10,  desc:"機台"});
	vat.item.make(vnB_Detail5, "suppilerCode"  , {type:"hidden",   view:"",size: 10,  desc:"廠商代號"});
    vat.item.make(vnB_Detail5, "suppilerName"         , {type:"hidden",   view:"",size: 10,  desc:"廠商名稱"});
    vat.item.make(vnB_Detail5, "assignMenuDateStart" , {type:"hidden",   view:"shift",size: 10,  desc:"派工日期(起)"});
    vat.item.make(vnB_Detail5," assignMenuTimeStart" , {type:"hidden",   view:"shift",size: 10,  desc:"派工時間(起)"});
	vat.item.make(vnB_Detail5, "supportNo", {type:"hidden",   view:"shift",size: 10,  desc:"支援單號"});
	vat.item.make(vnB_Detail5, "executeDateStart", {type:"hidden",   view:"shift",size: 10,  desc:"處理日期(起)"});
	vat.item.make(vnB_Detail5, "executeTimeStart"        , {type:"hidden",   view:"shift",size: 10,  desc:"處理時間(起)"});
	vat.item.make(vnB_Detail5, "executeDateEnd", {type:"hidden",   view:"shift",size: 10,  desc:"處理日期(迄)"});
	vat.item.make(vnB_Detail5, "executeTimeEnd", {type:"hidden",   view:"shift",size: 10,  desc:"處理時間(迄)"});
	vat.item.make(vnB_Detail5, "executeMemo"         , {type:"hidden",   view:"shift",size: 10,  desc:"處理說明"});//,onchange:"calculateLineAmount()"
	vat.item.make(vnB_Detail5, "executeInCharge"      , {type:"hidden",   view:"shift",size: 10,  desc:"處理人員"});//,onchange:"calculateLinetaxAmount()"
	
	vat.item.make(vnB_Detail5, "taskType"         ,{type:"SELECT",  size: 8,  desc:"階段分類",init:alltaskType,mode:"103" != loginDepartment ? "HIDDEN" : ""});
    vat.item.make(vnB_Detail5, "taskTypeNo" ,      {type:"SELECT",  size: 10,  desc:"執行階段",init:alltaskTypeNo});
    vat.item.make(vnB_Detail5, "taskDate" ,      {type:"DATE",  size: 5,  desc:"執行日期"});
	vat.item.make(vnB_Detail5, "execHours", 	   {type:"NUMM",   size:  5,maxLen:4	,  desc:"執行時數",mode:"103" != loginDepartment ? "HIDDEN" : ""});
	vat.item.make(vnB_Detail5, "executeTimeStart", {type:"TIME",   size: 5,maxLen:4,  desc:"開始時間",mode:"103" != loginDepartment ? "HIDDEN" : ""});
	vat.item.make(vnB_Detail5, "status",           {type:"SELECT",  size: 5,  desc:"進度",init:allstatus});
	vat.item.make(vnB_Detail5, "taskInchargeCode", {type:"TEXT",  size: 6,  desc:"代號",eChange:"eChangetasktaskInchargeCode()"});
	vat.item.make(vnB_Detail5, "taskInchargeName", {type:"TEXT",   size: 10,  desc:"執行人"});
	vat.item.make(vnB_Detail5, "displaySort"      ,{type:"LABEL",  size: 3,  desc:"排序",mode:"103" != loginDepartment ? "HIDDEN" : ""});
	vat.item.make(vnB_Detail5, "testInChargeCode", {type:"BUTTON",  size: 5, value:"進階輸入", desc:"進階輸入",mode:"103" != loginDepartment ? "HIDDEN" : ""});
	vat.item.make(vnB_Detail5, "isDeleteRecord"      , {type:"DEL"     ,	 view:"fixed",        desc:"刪除"                                         });
	vat.block.pageLayout(vnB_Detail5, {
														id                  : "vatDetailDiv5",
														pageSize            : 10,
								                        canGridDelete       : vbCanGridDelete,
														canGridAppend       : vbCanGridAppend,
														canGridModify       : vbCanGridModify,	
								                       // closeService        : function(){kweInitial();},
														loadBeforeAjxService: "loadBeforeAjxService("+vnB_Detail5+")",
														//loadSuccessAfter    : "",	
														saveBeforeAjxService: "saveBeforeAjxService()",
														saveSuccessAfter    : "",
													    blockId             : "3"
														});
   vat.block.pageDataLoad(vnB_Detail5, vnCurrentPage = 1);
}

//需求人員附件上傳
function kweDetail7(){
	 vat.item.make(vnB_vat7, "indexNo" , 	{type:"IDX"  , mode:"READONLY", desc:"序號"});
	 vat.item.make(vnB_vat7, "fileName", 	{type:"TEXT" , mode:"READONLY", size:18, bind:"fileName" ,maxLen:10, desc:"檔案名稱",eClick:function(id){openFile(id);}});
	 vat.item.make(vnB_vat7, "description", {type:"TEXT" , mode:"READONLY", desc:"說明"});
	 vat.item.make(vnB_vat7, "createdBy",   {type:"TEXT" , mode:"READONLY", desc:"上傳人員"});
	 vat.item.make(vnB_vat7, "contentType", {type:"TEXT" , mode:"READONLY", desc:"檔案類型"});
	 vat.item.make(vnB_vat7, "typeName", {type:"TEXT" , mode:"READONLY", desc:"類型"});
	 
	
	 vat.item.make(vnB_vat7, "physicalName", {type:"HIDDEN"  , desc:"實體檔案位置"});
	 vat.item.make(vnB_vat7, "physicalPath", {type:"HIDDEN"  , desc:"實體檔案路徑"});
	 vat.item.make(vnB_vat7, "isDeleteRecord", {type:"DEL"  , desc:"刪除"});
	 vat.item.make(vnB_vat7, "headId", {type:"HIDDEN"  , desc:"主鍵"});

	 vat.block.pageLayout(vnB_vat7, {
	              id: "vatT2Div",
	              pageSize: 10,           
	              canGridDelete : true,
	              canGridAppend : false,
	              canGridModify : false,    
	              //appendBeforeService : "appendBeforeService()",
	              //appendAfterService  : "appendAfterService()",
	              loadBeforeAjxService: "loadBeforeAjxService("+vnB_vat7+")",
	              //loadSuccessAfter    : "loadSuccessAfter()",      
	              //eventService        : "eventService()",   
	              saveBeforeAjxService: "saveBeforeAjxService()",
	              saveSuccessAfter    : ""
	
	              });
	 vat.block.pageDataLoad(vnB_vat7, vnCurrentPage = 1);
}


function formInitial(){

	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginDepartment  	    : document.forms[0]["#loginDepartment"   ].value,//===""?"103":document.forms[0]["#loginDepartment" ].value,
          brandCode  		    : document.forms[0]["#loginBrandCode"    ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          orderTypeCode         : document.forms[0]["#orderTypeCode" 	 ].value,
          processId          	: document.forms[0]["#processId"         ].value,
          assignmentId      	: document.forms[0]["#assignmentId"      ].value,
          domain      			: document.forms[0]["#domain"      ].value,
          type      	        : document.forms[0]["#type"      ].value,
          //userType      	    : document.forms[0]["#userType"          ].value,
          beforeStatus			: "beforestatus",
	      nextStatus			: "nextstatus",
	      approvalResult        : "",
	      approvalComment		:"",
          status                :document.forms[0]["#status" ].value===""?"SAVE":document.forms[0]["#status" ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=buPurchaseAction&process_object_method_name=performInitial"; 
	    	          },{
	    		other: true
	    	            }
	    );
	 		//vat.item.SelectBind(vat.bean("department"),{ itemName : "#F.department" ,selected : vat.bean().vatBeanOther.department});
  }
}




function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.new"         , type:"IMG"    , value:"新增",  src:"./images/button_create.gif", eClick:"createNewForm()"},
	 			{name:"SPACE"          , type:"LABEL"  , value:"　"},
	 			{name:"#B.search"      , type:"PICKER" , value:"查詢",  src:"./images/button_find.gif"  , 
	 									 openMode:"open", 
	 									 servicePassData:function()
										  	{return "&orderTypeCode="+vat.item.getValueByName("#F.orderTypeCode");},
	 									 service:"BU_PURCHASE:search:20131106.page",//+document.forms[0]["#orderTypeCode" ].value,
	 									 left:0, right:0, width:1024, height:768,	
	 									 serviceAfterPick:function(){doAfterPickerProcess();}},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.save"        , type:"IMG"    ,value:"暫存"   ,   src:"./images/button_save.gif"  , eClick:'doSubmit("SAVE")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.void"        , type:"IMG"    ,value:"作廢"   ,   src:"./images/button_void.gif"  , eClick:'doSubmit("VOID")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.update"      , type:"IMG"    ,value:"更新"   ,   src:"./images/update.gif"  , eClick:'doSubmit("PLAN")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.stop"        , type:"IMG"    ,value:"暫停"   ,   src:"./images/button_stop.gif"  , eClick:'doSubmit("SUSPEND")'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.repeat"      , type:"IMG"    ,value:"回復"   , src:"./images/repeat.gif"  , eClick:"getUserRepeat()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.getMenu"     , type:"IMG"    ,value:"取得主管選單資料",   src:"./images/getMaMenu.jpg", eClick:"getOriginalData('boss')"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.attachFile"  , type:"IMG"    ,value:"插入附件",   src:"./images/button_attachment.gif", eClick:'doUpload()'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print"       , type:"IMG"    ,value:"單據列印",   src:"./images/print_plz_get.png" , eClick:'reconfirmImmovement()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print0"       , type:"IMG"    ,value:"單據列印",   src:"./images/pay_button.gif" , eClick:'reconfirmImmovement0()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print1"       , type:"IMG"    ,value:"單據列印",   src:"./images/print_fix.png" , eClick:'reconfirmImmovement1()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print2"       , type:"IMG"    ,value:"需求單列印",   src:"./images/print_require.png" , eClick:'Irqprint()'},
	 			//{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.print3"       , type:"IMG"    ,value:"權限申請單列印",   src:"./images/print_require.png" , eClick:'printMenuApplication()'},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.first"       , type:"IMG"    ,value:"第一筆"  ,   src:"./images/play-first.png"   , eClick:"gotoFirst()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.forward"      , type:"IMG"    ,value:"上一筆",    src:"./images/play-back.png"     , eClick:"gotoForward()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.next"        , type:"IMG"    ,value:"下一筆",   src:"./images/play.png"           , eClick:"gotoNext()"},
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.last"        , type:"IMG"    ,value:"最後一筆",   src:"./images/play-forward.png" , eClick:"gotoLast()"},
	 			{name:"SPACE"           , type:"LABEL" ,value:"　　"},
	 			{name:"#L.currentRecord", type:"NUMB"  ,bind: vsCurrentRecord, size: 4, mode:"READONLY"},
	 			{name:"SPACE"           , type:"LABEL" ,value:" / "},
	 			{name:"#L.maxRecord"    , type:"NUMB"  ,bind: vsMaxRecord    , size: 4, mode:"READONLY" }],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){

    var ifCostControl=[["","",false],
                 ["是","否"],
                 ["Y","N"]];
    var ifWareHouseControl=[["","",false],
                 ["是","否"],
                 ["Y","N"]];             
	var orderTypeCode       = vat.item.getValueByName("#orderTypeCode");
    var type                = vat.item.getValueByName("#type");
    var userType    		= vat.bean("userType");
 	var allCategroyTypes    = vat.bean("allCategroyTypes");
 	var allproject          = vat.bean("allproject");
 	var alldepartment       = vat.bean("alldepartment");
 	var allstatus           = [[true, true,true,true,true], ["暫存", "執行中", "結案","作廢"],["SAVE","PLAN","FINISH","VOID"]];
 	var allstatusLoa           = [[true,true], ["暫存", "簽核完成"],["SAVE","FINISH"]];
 	var vsCostStyle=type !="1"?" style= 'display:none;'":""; 

   if(vat.item.getValueByName("#orderTypeCode")=="IRQ"||vat.item.getValueByName("#userType")=="CONTACT"){
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"需求單建立作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌/<font color='red'></font>"},
						{name:"#L.orderTypeCode", type:"LABEL", value:"單別/<font color='red'></font>"},
						{name:"#L.orderNo", type:"LABEL" , value:"單號<font color='red'></font>"}]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:3, maxLen:20, mode:"READONLY"},
						{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", size:3, maxLen:20,mode:"READONLY" },
						{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:15, maxLen:20,mode:"READONLY" },
						{name:"#F.headId"     , type:"hidden"  ,  size:4, bind:"headId" , back:false, mode:"READONLY"},
						{name:"#F.role"     , type:"hidden"  ,  size:4, bind:"role" , mode:"READONLY"}]},
				{items:[{name:"#L.requestCode", type:"LABEL", value:"需求人員<font color='red'>*</font>"}]},
				{items:[{name:"#F.requestCode", type:"TEXT",bind:"requestCode", size:10, maxLen:25,eChange:"eChangeRequest()" },
				 		{name:"#B.requestCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.request"   , type:"TEXT", bind:"request", size:5, maxLen:25,mode:"READONLY", eChange:"eChangeRequestCode()"},
						{name:"#F.lastUpdatedBy"   , type:"hidden", bind:"lastUpdatedBy", size:5, maxLen:25,mode:"READONLY"}]},
						
				{items:[{name:"#L.contractTel",     type:"LABEL",  value:"聯絡電話/分機<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.contractTel",         type:"NUMM",   bind:"contractTel",         size:10}]},
				{items:[{name:"#L.department", type:"LABEL" , value:"需求部門"}]},
				{items:[{name:"#F.department", type:"SELECT", bind:"department", size:25, maxLen:25,init:alldepartment }]}]},
			{row_style:"", cols:[
				{items : [{name : "#L.no", type : "LABEL", value : "主旨<font color='red'>*</font>"}]},
				{items : [{name : "#F.no", type : "TEXT", bind : "no", size : 50, maxLen : 25}],td:" colSpan=3"},
				{items : [{name : "#L.requestDate", type : "LABEL", value : "需求日期<font color='red'></font>"}]},
				{items : [{name : "#F.requestDate", type : "DATE", bind : "requestDate", size : 10, maxLen : 25}]},
				{items : [{name:"#L.status", type:"LABEL", value:"狀態"}]},
				{items : [{name:"#F.status", type:"TEXT", bind:"status", size:25, maxLen:25,mode:"HIDDEN",eChange:"doFormAccessControl()"},
				          {name:"#F.statusName",         type:"TEXT",   bind:"statusName",          mode:"READONLY",  back:false}]}
				]}
			/*{row_style:vsCostStyle, cols:[//("1" == type && "LOA" == orderTypeCode) ? "" : 
				{items:[{name:"#L.applicant", type:"LABEL", value:"申請人<font color='red'>*</font>"}]},
				{items:[{name:"#F.applicant", type:"TEXT", bind:"applicant", size:10, maxLen:25, eChange:"eChangeApplicant()" },
				 		{name:"#B.applicant",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.applicantName"   , type:"TEXT", bind:"applicantName", size:5, maxLen:25,mode:"READONLY"}]},
						
				{items:[{name:"#L.applicant0", type:"LABEL", value:"同"}]},
				{items:[{name:"#F.applicant0", type:"TEXT", bind:"applicant0", size:10, maxLen:25, eChange:"eChangeApplicant()" },
				 		{name:"#B.applicant0",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.applicantName0"   , type:"TEXT", bind:"applicantName0", size:5, maxLen:25,mode:"READONLY"}]},
			
				]},*/
		], 	
		beginService:"",
		closeService:""			
	});
}
 if(vat.item.getValueByName("#orderTypeCode")=="PRC"){
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"請採驗作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌/<font color='red'></font>"},
						{name:"#L.orderTypeCode", type:"LABEL", value:"單別/<font color='red'></font>"},
						{name:"#L.orderNo", type:"LABEL" , value:"單號<font color='red'></font>"}]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:3, maxLen:20, mode:"READONLY"},
						{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", size:3, maxLen:20,mode:"READONLY" },
						{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:15, maxLen:20,mode:"READONLY" },
						{name:"#F.headId"     , type:"hidden"  ,  size:4, bind:"headId" , back:false, mode:"READONLY"},
						{name:"#F.role"     , type:"hidden"  ,  size:4, bind:"role" , mode:"READONLY"}]},
						
				{items:[{name:"#L.requestCode", type:"LABEL", value:"需求人員<font color='red'>*</font>"}]},
				{items:[{name:"#F.requestCode", type:"TEXT", bind:"requestCode", size:10, maxLen:25, mask:"Aaaaaaaaaa"  ,eChange:"eChangeRequest()" },
				 		{name:"#B.requestCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.request"   , type:"TEXT", bind:"request", size:5, maxLen:25,mode:"READONLY"},
						{name:"#F.lastUpdatedBy"   , type:"hidden", bind:"lastUpdatedBy", size:5, maxLen:25,mode:"READONLY"}]},
						
				{items:[{name:"#L.contractTel",     type:"LABEL",  value:"聯絡電話/分機<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.contractTel",         type:"NUMM",   bind:"contractTel",         size:10}]},
				{items:[{name:"#L.department", type:"LABEL" , value:"需求部門"}]},
				{items:[{name:"#F.department", type:"SELECT", bind:"department", size:25, maxLen:25,init:alldepartment }]}]},
			{row_style:"", cols:[
				{items : [{name : "#L.no", type : "LABEL", value : "主旨<font color='red'>*</font>"}]},
				{items : [{name : "#F.no", type : "TEXT", bind : "no", size : 50, maxLen : 25}],td:" colSpan=3"},
				{items : [{name : "#L.requestDate", type : "LABEL", value : "需求日期<font color='red'></font>"}]},
				{items : [{name : "#F.requestDate", type : "DATE", bind : "requestDate", size : 10, maxLen : 25}]},
				{items : [{name:"#L.status", type:"LABEL", value:"狀態"}]},
				{items : [{name:"#F.status", type:"TEXT", bind:"status", size:25, maxLen:25,mode:"HIDDEN",eChange:"doFormAccessControl()"},
				          {name:"#F.statusName",         type:"TEXT",   bind:"statusName",          mode:"READONLY",  back:false}]}
				
				
				]},
			{row_style:vsCostStyle, cols:[//("1" == type && "LOA" == orderTypeCode) ? "" : 
						{items:[{name:"#L.applicant", type:"LABEL", value:"申請人w<font color='red'>*</font>"}]},
						{items:[{name:"#F.applicant", type:"TEXT", bind:"applicant", size:10, maxLen:25, eChange:"eChangeApplicant()" },
				 		{name:"#B.applicant",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.applicantName"   , type:"TEXT", bind:"applicantName", size:5, maxLen:25,mode:"READONLY"}]},
						
						{items:[{name:"#L.applicant0", type:"LABEL", value:"同"}]},
						{items:[{name:"#F.applicant0", type:"TEXT", bind:"applicant0", size:10, maxLen:25, eChange:"eChangeApplicant()" },
				 		{name:"#B.applicant0",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.applicantName0"   , type:"TEXT", bind:"applicantName0", size:5, maxLen:25,mode:"READONLY"}]}
				]}
		], 
		beginService:"",
		closeService:""			
	});
}
if(vat.item.getValueByName("#orderTypeCode")=="LOA"){

	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"權限申請作業", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", type:"LABEL" , value:"品牌/<font color='red'></font>"},
						{name:"#L.orderTypeCode", type:"LABEL", value:"單別/<font color='red'></font>"},
						{name:"#L.orderNo", type:"LABEL" , value:"單號<font color='red'></font>"}]},
				{items:[{name:"#F.brandCode", type:"TEXT", bind:"brandCode", size:3, maxLen:3, mode:"READONLY"},
						{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", size:3, maxLen:3,mode:"READONLY" },
						{name:"#F.orderNo", type:"TEXT", bind:"orderNo", size:15, maxLen:25,mode:"READONLY" },
						{name:"#F.headId"     , type:"hidden"  ,  size:4, bind:"headId" , back:false, mode:"READONLY"}]},
				{items:[{name:"#L.requestCode", type:"LABEL", value:"需求人員<font color='red'>*</font>"}]},
				{items:[{name:"#F.requestCode", type:"TEXT", bind:"requestCode", size:10, maxLen:25, mask:"Aaaaaaaaaa"  ,eChange:"eChangeRequest()" },
				 		{name:"#B.requestCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.request"   , type:"TEXT", bind:"request", size:5, maxLen:25,mode:"READONLY"},
						{name:"#F.lastUpdatedBy"   , type:"hidden", bind:"lastUpdatedBy", size:5, maxLen:25,mode:"READONLY"}]},
						
				{items:[{name:"#L.contractTel",     type:"LABEL",  value:"聯絡電話/分機<font color='red'>*</font>"}]},	 
	 			{items:[{name:"#F.contractTel",         type:"NUMM",   bind:"contractTel",         size:10}]},
				{items:[{name:"#L.department", type:"LABEL" , value:"需求部門"}]},
				{items:[{name:"#F.department", type:"SELECT", bind:"department", size:25, maxLen:25,init:alldepartment }]}]},
			{row_style:"", cols:[
				{items : [{name : "#L.no", type : "LABEL", value : "主旨<font color='red'>*</font>"}]},
				{items : [{name : "#F.no", type : "TEXT", bind : "no", size : 50, maxLen : 25}],td:" colSpan=3"},
				{items : [{name : "#L.requestDate", type : "LABEL", value : "需求日期<font color='red'></font>"}]},
				{items : [{name : "#F.requestDate", type : "DATE", bind : "requestDate", size : 10, maxLen : 25}]},
				{items : [{name:"#L.status", type:"LABEL", value:"狀態"}]},
                {items : [{name:"#F.status", type:"SELECT", bind:"status", size:25, maxLen:25,init:allstatusLoa,eChange:"doFormAccessControl()"}]}

				
				
				]},
			{row_style:vsCostStyle, cols:[//("1" == type && "LOA" == orderTypeCode) ? "" : 
						{items:[{name:"#L.applicant", type:"LABEL", value:"申請人<font color='red'>*</font>"}]},
						{items:[{name:"#F.applicant", type:"TEXT", bind:"applicant", size:10, maxLen:25, eChange:"getEmployeeRole('applicant')" },
				 		{name:"#B.applicant",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.applicantName"   , type:"TEXT", bind:"applicantName", size:5, maxLen:25,mode:"READONLY"}]},
						
						{items:[{name:"#L.applicant0", type:"LABEL", value:"同" }]},
						{items:[{name:"#F.applicant0", type:"TEXT", bind:"applicant0", size:10 ,mode:"READONLY" ,  maxLen:25,eChange:"eChangeApplicant()" },
				 		//{name:"#B.applicant0",	value:"選取" ,type:"PICKER" ,
												//openMode:"open", src:"./images/start_node_16.gif",
		 									 	//service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	//left:0, right:0, width:1024, height:768,	
		 									 	//serviceAfterPick:function(){doAfterPickerEmployee();}},
						{name:"#F.applicantName0"   , type:"TEXT", bind:"applicantName0", size:5, maxLen:25,mode:"READONLY"}]},
				{items : [{name:"#L.qa", type:"LABEL", value:"職稱"}]},
				{items : [{name:"#F.role", type:"TEXT", bind:"role", size:25, maxLen:25 ,mode:"READONLY" }],td:" colSpan=1"},
				{items : [{name:"#L.empRole", type:"LABEL", value:"職稱(異動)"}]},
				{items : [{name:"#F.empRole", type:"TEXT", value:""}]}
				]},
		{row_style:vsCostStyle, cols:[//("1" == type && "LOA" == orderTypeCode) ? "" : 
				{items : [{name:"#L.1", type:"LABEL", value:"庫別分類"}]},
				{items : [{name:"#F.1", type:"TEXT", size:25, maxLen:25 ,mode:"READONLY" }]},
				{items : [{name:"#L.2", type:"LABEL", value:"員工角色"}]},
				{items : [{name:"#F.2", type:"TEXT", bind:"employeeRole"  ,size:25, maxLen:25 ,mode:"READONLY" }]},
				{items : [{name:"#L.3", type:"LABEL", value:"成本(可視)"}]},
				{items : [{name:"#F.3", type:"SELECT", bind:"costControl" ,size:25, maxLen:25 , init:ifCostControl}]},
				{items : [{name:"#L.4", type:"LABEL", value:"庫別(可視)"}]},
				{items : [{name:"#F.4", type:"SELECT", bind:"warehouseControl", size:25, maxLen:25, init:ifWareHouseControl}]}
				]}
				
		
		], 	
		beginService:"",
		closeService:""			
	});
}
}
function kweMaster(){
 		 var allproject    = vat.bean("allproject"); 
 		 var allsystem     = vat.bean("allsystem"); 
 		 var allCategroyTypese    = vat.bean("allCategroyTypes");
 		 var allsource     = [[true,true,true,true], ["口頭","EMAIL","LINE","電話"],["口頭","EMAIL","LINE","電話"]];
 		 var status        = vat.item.getValueByName("#F.status")===null ? "SAVE" : vat.item.getValueByName("#F.status");
 		 var orderTypeCode = vat.item.getValueByName("#orderTypeCode");
 		 var loginDepartment      = vat.item.getValueByName("#loginDepartment");
 		 var type      = vat.item.getValueByName("#type");
      	 var vsCostStyle=loginDepartment!="103"?" style= 'display:none;'":"";//loginDepartment!="103"?" style= 'display:none;'":"";//orderTypeCode!="IRQ"?" style= 'display:none;'":"";//status=="SAVE"?" style= 'display:none;'":""; // 因權限問題

 		if(vat.bean().vatBeanOther.loginDepartment ==="103" ){ 	

	vat.block.create(vnB_master, {
	id: "vatBlock_Master", generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'", rows:[
	{row_style:"", cols:[	
		{items:[{name:"#L.categoryItem"  , type:"LABEL" , value:"項目/<font color='red'>*</font>"},
				{name:"#L.categorySystem", type:"LABEL" , value:"系統<font color='red'>*</font>"}]},
				
		{items:[{name:"#F.categoryItem", type:"SELECT", bind:"categoryItem",init:allproject ,eChange:function(){changeCategory();}},
				{name:"#F.categorySystem", type:"SELECT", bind:"categorySystem",init:allsystem,eChange:function(){changeInCategory();}}], td:" colSpan=4"}]},
	{row_style : "", cols:[	
		{items:[{name : "#L.description", type : "LABEL", value : "說明", row : 6, col : 50}], td:"rowSpan=4"},
		{items:[{name : "#F.description", type : "TEXTAREA", bind : "description",  row : 5, col : 50}], td: "rowSpan=4"}]},
	{row_style : "", cols:[	
		{items:[{name : "#L.depManager", type : "LABEL", value : "需求主管<font color='red'>*</font>"}]},
		{items:[{name : "#F.depManager", type : "TEXT", bind : "depManager", size :10, maxLen : 25,  mask:"Aaaaaaaaaa",eChange:"eChangedepManager()"},
				{name : "#B.depManager",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee1();}},
			    {name : "#F.depManagerName", type : "TEXT", bind : "depManagerName", size:10, maxLen : 25,mode:"READONLY"}]}]},
	{row_style : "", cols:[	
		{items :[{name : "#L.createdBy", type : "LABEL", value : "填單人員<font color='red'></font>"}]},
		{items :[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25,mode:"READONLY"},
				 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}]}]},
	{row_style : "", cols:[	
		{items :[{name : "#L.creationDate", type : "LABEL", value : "填單日期<font color='red'></font>"}]},
		{items :[{name:"#F.creationDate", type:"DATE", bind:"creationDate", size:10, maxLen:25 ,mode:"READONLY"}]}]},
		
   {row_style:"", cols:[
		{items:[{name:"#L.categoryGroup", type:"LABEL" , value:"分類/<font color='red'></font>"},
				{name:"#L.projectId", type:"LABEL" , value:"需求代號<font color='red'></font>"}
				]},
		{items:[{name:"#F.categoryGroup", type:"SELECT", bind:"categoryGroup",init:allCategroyTypese,style:vsCostStyle},//eChange:"changeItemSubcategory()" 
				{name:"#F.projectId", type:"TEXT",bind:"projectId",mask:"CCCCCCCC",size:8,style:vsCostStyle},
				{name:"#L.special", type:"LABEL" , value:"特殊<font color='red'></font>"},
				{name:"#F.special"	, type:"CHECKBOX",biind:"special",style:vsCostStyle}]},
		{items:[{name:"#L.requestSource", type:"LABEL",  value:"需求來源"}]},	 
	 	{items:[{name:"#F.requestSource", type:"SELECT", bind:"requestSource",init:allsource}]}]},
	{row_style:"", cols:[ 
	 	{items:[{name:"#L.estimateStartDare"          , type:"LABEL" , value:"預計日期/"},
	 			{name:"#L.priority"          , type:"LABEL" , value:"順序/"},
	 			{name:"#L.totalHours"          , type:"LABEL" , value:"總時數"}]},
	 	{items:[{name:"#F.estimateStartDare"     , type:"DATE"  ,  bind:"estimateStartDare", size:13,style:vsCostStyle},
				{name:"#L.between"                  , type:"LABEL" , value:"至"},
	 		 	{name:"#F.estimateEndDare"       , type:"DATE"  ,  bind:"estimateEndDare"  , size:13},
	 		 	{name:"#F.priority"       , type:"TEXT"  ,  bind:"priority"  , size:5},
	 		 	{name:"#F.totalHours"       , type:"TEXT"  ,  bind:"totalHours"  , size:5}]},
	 		 
	 	{items:[{name:"#L.rqInChargeCode", type:"LABEL", value:"處理人員<font color='red'></font>"}]},
		{items:[{name:"#F.rqInChargeCode", type:"TEXT", bind:"rqInChargeCode",  mask:"Aaaaaaaaaa"  ,size:10, maxLen:25,eChange:'eChangerqInChargeCode()'},
				{name : "#B.rqInChargeCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee2();}},
				{name:"#F.otherGroup", type:"TEXT", bind:"otherGroup", size:10 ,mode:"READONLY" ,maxLen:25 }],td:" colSpan=4"}]},		
	{row_style:"", cols:[
	 	{items:[{name:"#L.processDescription", type:"LABEL",  value:"處理說明"}]},	 
	 	{items:[{name:"#F.processDescription", type:"TEXT", bind:"processDescription", size:150,maxLen:400}],td:" colSpan=3"}]},
	{row_style:"", cols:[
	 	{items:[{name:"#L.hisstoryInfo", type:"LABEL",  value:"歷程"}]},	 
	 	{items:[{name:"#F.hisstoryInfo", type:"TEXT", bind:"hisstoryInfo", size:150,mode:"READONLY" }],td:" colSpan=3"}]}
			],			
			beginService:function(){return "";},
			closeService:function(){return "";}
		});
		
	}else{

	vat.block.create(vnB_master, {
	id: "vatBlock_Master", generate: true, table:"cellspacing='0' class='default' border='0' cellpadding='3'", rows:[
	{row_style:"", cols:[	
		{items:[{name:"#L.categoryItem", type:"LABEL" , value:"項目/<font color='red'>*</font>"},
				{name:"#L.categorySystem", type:"LABEL" , value:"系統<font color='red'>*</font>"}]},
		{items:[{name:"#F.categoryItem", type:"SELECT", bind:"categoryItem",init:allproject ,eChange:function(){changeCategory();}},
				{name:"#F.categorySystem", type:"SELECT", bind:"categorySystem",init:allsystem,eChange:function(){changeInCategory();}}], td:" colSpan=4"}]},
	{row_style : "", cols:[	
		{items:[{name : "#L.description", type : "LABEL", value : "說明", row : 6, col : 50}], td:"rowSpan=4"},
		{items:[{name : "#F.description", type : "TEXTAREA", bind : "description", size : 50, row : 5, col : 50}], td: "rowSpan=4"}]},
	{row_style : "", cols:[	
		{items:[{name : "#L.depManager", type : "LABEL", value : "需求主管<font color='red'>*</font>"}]},
		{items:[{name : "#F.depManager", type : "TEXT", bind : "depManager", size :10, mask:"Aaaaaaaaaa",maxLen : 25,eChange:'eChangedepManager()'},
				{name : "#B.depManager",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee1();}},
			    {name : "#F.depManagerName", type : "TEXT", bind : "depManagerName", size :10, maxLen : 25,mode:"READONLY"}]}]},
	{row_style : "", cols:[	
		{items :[{name : "#L.createdBy", type : "LABEL", value : "填單人員<font color='red'></font>"}]},
		{items :[{name : "#F.createdBy", type : "TEXT", bind : "createdBy", size : 10, maxLen : 25,mode:"READONLY"},
				 {name : "#F.createdByName", type : "TEXT", bind : "createdByName", size : 10, maxLen : 25,mode:"READONLY"}]}]},
	{row_style : "", cols:[	
		{items :[{name : "#L.creationDate", type : "LABEL", value : "填單日期<font color='red'></font>"}]},
		{items :[{name:"#F.creationDate", type:"DATE", bind:"creationDate", size:10, maxLen:25 ,mode:"READONLY"}]}]},
	{row_style:vsCostStyle, cols:[
		{items:[{name:"#L.categoryGroup", type:"LABEL" , value:"分類<font color='red'></font>"}]},
		{items:[{name:"#F.categoryGroup", type:"SELECT", bind:"categoryGroup",init:allCategroyTypese,style:vsCostStyle}//eChange:"changeItemSubcategory()" 
							]},
		{items:[{name:"#L.requestSource", type:"LABEL",  value:"需求來源"}]},	 
	 	{items:[{name:"#F.requestSource", type:"SELECT", bind:"requestSource",init:allsource}]}]},
	{row_style:vsCostStyle, cols:[ 
	 	{items:[{name:"#L.estimateStartDare"          , type:"LABEL" , value:"預計日期"}]},
	 	{items:[{name:"#F.estimateStartDare"     , type:"DATE"  ,  bind:"estimateStartDare", size:13,style:vsCostStyle},
				{name:"#L.between"                  , type:"LABEL" , value:"至"},
	 		 	{name:"#F.estimateEndDare"       , type:"DATE"  ,  bind:"estimateEndDare"  , size:13}]},	 
	 	{items:[{name:"#L.rqInChargeCode", type:"LABEL", value:"處理人員<font color='red'></font>"}]},
		{items:[{name:"#F.rqInChargeCode", type:"TEXT", bind:"rqInChargeCode",  mask:"Aaaaaaaaaa"  ,size:10, maxLen:25,eChange:"eChangerqInChargeCode()"},
				{name : "#B.rqInChargeCode",	value:"選取" ,type:"PICKER" ,
												openMode:"open", src:"./images/start_node_16.gif",
		 									 	service:"Bu_AddressBook:searchEmployee:20090811.page",
		 									 	left:0, right:0, width:1024, height:768,	
		 									 	serviceAfterPick:function(){doAfterPickerEmployee2();}},
				{name:"#F.otherGroup", type:"TEXT", bind:"otherGroup", size:10 ,mode:"READONLY" ,maxLen:25 }],td:" colSpan=4"}]},		
	{row_style:vsCostStyle, cols:[
	 	{items:[{name:"#L.processDescription", type:"LABEL",  value:"處理說明"}]},	 
	 	{items:[{name:"#F.processDescription", type:"TEXT", bind:"processDescription", size:150,maxLen:400}],td:" colSpan=3"}]},
	{row_style:vsCostStyle, cols:[
	 	{items:[{name:"#L.hisstoryInfo", type:"LABEL",  value:"歷程"}]},	 
	 	{items:[{name:"#F.hisstoryInfo", type:"TEXT", bind:"hisstoryInfo", size:150,mode:"READONLY" }],td:" colSpan=3"}]}
			],	
			beginService:function(){return "";}	,
			closeService:function(){return "";}
	});
	}
}
	function kweMaster1(){
           var allshops    = vat.bean("allshops"); 
           var allmachine    = vat.bean("allmachine");    
vat.block.create(vnB_master1, {

	id: "vatBlock_Master1", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[  
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.fixUnit", type:"LABEL",  value:"報修(專櫃)單位"}]},	 
	 		{items:[{name:"#F.fixUnit", type:"SELECT", bind:"fixUnit",init:allshops, size:12}]},		 
	 		{items:[{name:"#L.fixMachine",         type:"LABEL",  value:"報修(專櫃)機台"}]},	 
			{items:[{name:"#F.fixMachine",         type:"SELECT", bind:"fixMachine", init:allmachine,        size:12}]},
			{items:[{name:"#L.contractPerson",         type:"LABEL",  value:"聯絡人員"}]},	 
	 		{items:[{name:"#F.contractPerson",         type:"TEXT",   bind:"contractPerson",         size:12}]},
	 		{items:[{name:"#L.contractTel",         type:"LABEL",  value:"聯絡電話"}]},	 
	 		{items:[{name:"#F.contractTel",         type:"NUMM",   bind:"contractTel",         size:12}]}
	 		]},
	 	{row_style:"", cols:[
			{items:[{name:"#L.fixAddress",         type:"LABEL",  value:"報修(專櫃)地址"}]},	 
			{items:[{name:"#F.fixAddress",         type:"TEXT", bind:"fixAddress",  size:200}],td:" colSpan=7"}
	 		]},	
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.dispatchDate", type:"LABEL",  value:"派工日期"}]},	 
	 		{items:[{name:"#F.dispatchDate", type:"DATE", bind:"dispatchDate", size:12}]},		 
	 		{items:[{name:"#L.dispatchTime",         type:"LABEL",  value:"派工時間(起)"}]},	 
			{items:[{name:"#F.dispatchTime",         type:"TIME", bind:"dispatchTime",         size:12}]},
			{items:[{name:"#L.processStartTime", type:"LABEL",  value:"處理時間(起)"}]},	 
	 		{items:[{name:"#F.processStartTime", type:"TIME", bind:"processStartTime", size:12}]},		 
	 		{items:[{name:"#L.processEndTime",         type:"LABEL",  value:"處理時間(迄)"}]},	 
			{items:[{name:"#F.processEndTime",         type:"TIME", bind:"processEndTime",         size:12}]}]},		
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.processDescription", type:"LABEL",  value:"處理說明", row : 5, col : 40}]},	 
	 		{items:[{name:"#F.processDescription", type:"TEXTAREA", bind:"processDescription", size:745, row : 5, col : 125}],td:" colSpan=7"}]}
 	 	],
		beginService:"",
		closeService:""
	});
	}
	function kweMaster2(){
           
vat.block.create(vnB_master1, {

	id: "vatBlock_Master2", table:"cellspacing='0' class='default' border='0' cellpadding='3'",
	rows:[  
	 	{row_style:"", cols:[
			{items:[{name:"#L.contractPerson",         type:"LABEL",  value:"申請對內EMAIL(Outlook)"}]},	
			{items:[{name:"#L.enName",         type:"LABEL",  value:"英文名"}]},	 
	 		{items:[{name:"#F.enName",         type:"TEXT",   bind:"enName",         size:12}]},
	 		{items:[{name:"#L.enFristName",         type:"LABEL",  value:"英文姓"}]},	 
	 		{items:[{name:"#F.enFristName",         type:"TEXT",   bind:"enFristName",         size:12}]}		
	 		]},
	 	{row_style:"", cols:[
	 		{items:[{name:"#L.dispatchDate", type:"LABEL",  value:"申請對外EMAIL"}]},	 
	 		{items:[{name:"#L.enName", type:"LABEL",  value:"英文名"}]},	 
	 		{items:[{name:"#F.enName", type:"TEXT", bind:"enName", size:12}]},		 
	 		{items:[{name:"#L.enFristName",         type:"LABEL",  value:"英文姓"}]},	 
			{items:[{name:"#F.enFristName",         type:"TEXT", bind:"enFristName",         size:12}]}
	 		]}
 	 	],
		beginService:"",
		closeService:""
	});
	}

// 建立新資料按鈕	
function createNewForm(){
    if(confirm("此作業將清除原先輸入資料，請確認是否執行?")){
    	vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
    	refreshForm("");
	 }
}

// 送出的返回
function createRefreshForm(){

        vat.item.setValueByName("#L.currentRecord", "0");
		vat.item.setValueByName("#L.maxRecord"    , "0");
      	vat.bean().vatBeanPicker.result = null;  
	refreshForm("");
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

// 送出,暫存按鈕
function doSubmit(formAction){
    var nextStatus = "";
    var domain               = document.forms[0]["#domain"].value.replace(/^\s+|\s+$/, '');
	var formId               = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var processId            = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
    var status               = vat.item.getValueByName("#F.status");
    var orderTypeCode        = vat.item.getValueByName("#F.orderTypeCOode");
    var approvalComment      = vat.item.getValueByName("#F.approvalComment");
	var approvalResult       = vat.item.getValueByName("#F.approvalResult");
	var alertMessage ="是否確定送出?";
  if(vat.item.getValueByName("#F.orderTypeCOode")=="IRQ"){
  if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
    var formStatus = status;
    if("SAVE" == formAction){
        formStatus = "SAVE";
    }else if("SUBMIT" == formAction){
        formStatus = changeFormStatus(formId, processId, status, approvalResult);
    }else if("SUSPEND" == formAction){
        formStatus = "SUSPEND";
    }else if("PLAN" == formAction){
        formStatus = "PLAN";
    }else if("SIGNING" == formAction){
        formStatus = "SIGNING";
    }else if("REPLAN" == formAction){
        formStatus = "REPLAN";
    }else if("VOID" == formAction){
        formStatus = "VOID";
    }	
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}else if ("SUSPEND" == formAction){
	 	alertMessage = "是否確定暫停?";
	}else if ("UPADTE" == formAction){
	 	alertMessage = "是否確定更新?";
	}else if ("REPLAN" == formAction){
	 	alertMessage = "是否確定轉派?";
	 	}
	}
  else{ 
	
	if(approvalResult == true){
    	approvalResult = "true"
    }else{
    	approvalResult = "false"
    }
    var formStatus = status;
    if("SAVE" == formAction){
        formStatus = "SAVE";
    }else if("SUBMIT" == formAction){
        formStatus = changeFormStatusPRC(formId, processId, status, approvalResult);
    }else if("SIGNING" == formAction){
        formStatus = "SIGNING";
    }else if("VOID" == formAction){
        formStatus = "VOID";
    }	
	var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}else if ("VOID" == formAction){
	 	alertMessage = "是否確定作廢?";
	}else if ("SAVE" == formAction){
	 	alertMessage = "是否確定暫存?";
	}
	 		}
	  	if(confirm(alertMessage)){
		 if (vat.item.getValueByName("#F.orderTypeCode") == "PRC"){
		 				vat.bean().vatBeanOther.formAction = formAction;
	    				vat.bean().vatBeanOther.approvalResult = approvalResult;	
	    				vat.bean().vatBeanOther.approvalComment = approvalComment;
	    				vat.bean().vatBeanOther.formStatus = formStatus;	
			   vat.block.pageDataSave(vnB_Detail　　,
			   {  	
			      funcSuccess:function(){
			   			 vat.bean().vatBeanOther.formAction = formAction;
					     vat.block.submit(function(){
						 	return "process_object_name=buPurchaseAction&process_object_method_name=performTransaction";
					     }, 
					     {
					            bind:true, link:true, other:true,
				  					funcSuccess: function () {
		               
		                             } 
				          });
			
			       }
			    });  
	      }else if (vat.item.getValueByName("#F.orderTypeCode") == "IRQ"){
	        			vat.bean().vatBeanOther.formAction = formAction;
	    				vat.bean().vatBeanOther.approvalResult = approvalResult;	
	    				vat.bean().vatBeanOther.approvalComment = approvalComment;
	    				vat.bean().vatBeanOther.formStatus = formStatus;	
		        vat.block.pageDataSave(vnB_Detail5　　,
		        {  	
		           funcSuccess:function(){
			   			  vat.bean().vatBeanOther.formAction = formAction;
					      vat.block.submit(function(){
						 	 return "process_object_name=buPurchaseAction&process_object_method_name=performTransaction";
					      },
					      {
					              bind:true, link:true, other:true,
				  				      funcSuccess: function () {
		               
		                              } 
				           });
		
		             }
		          });
	        }else if (vat.item.getValueByName("#F.orderTypeCode")=="TAS"){
	              vat.block.pageDataSave(vnB_Detail5　　,
	              {  	
	                 funcSuccess:function(){
				   			vat.bean().vatBeanOther.formAction = formAction;
						    vat.block.submit(function(){
				 	           return "process_object_name=buPurchaseAction&process_object_method_name=performTransaction";
			                },
			                {
			                        bind:true, link:true, other:true,
		  							  funcSuccess: function () {
               
                                  	  } 
		                    });
	
	                  }
	                });
	           }else if (vat.item.getValueByName("#F.orderTypeCode")=="OSP"){
	                 vat.block.pageDataSave(vnB_Detail2　　,
	                 {  	
	                    funcSuccess:function(){
					   		   vat.bean().vatBeanOther.formAction = formAction;
							   vat.block.submit(function(){
								  return "process_object_name=buPurchaseAction&process_object_method_name=performTransaction";
							   }, 
							   {
							           bind:true, link:true, other:true,
						  				 funcSuccess: function () {
				               
				                         } 
						       });
	                     }
	                  });

	       }else if (vat.item.getValueByName("#F.orderTypeCode")=="LOA"){
					   			 vat.block.pageDataSave(vnB_Detail3,
					   			  {  	
	                      		funcSuccess:function(){
	                      		 vat.bean().vatBeanOther.formStatus = "";	
					   			 vat.bean().vatBeanOther.formAction = formAction;
					   			  //alert(formAction);
							     vat.block.submit(function(){
								 	return "process_object_name=buPurchaseAction&process_object_method_name=performTransaction";
							     }, 
							     {
							            bind:true, link:true, other:true,
						  				   funcSuccess: function () {
				                                 
				                           } 
						         });
					
					       }
					    });
	                 
	              }else if (vat.item.getValueByName("#F.orderTypeCode")=="BPR"){
	                    vat.block.pageDataSave(vnB_Detail　　,
	                    {  	
	                       funcSuccess:function(){
	                             
					   			  vat.bean().vatBeanOther.formAction = formAction;
							      vat.block.submit(function(){
								 	 return "process_object_name=buPurchaseAction&process_object_method_name=performTransaction";
							      }, 
							      {
							             bind:true, link:true, other:true,
						  					funcSuccess: function () {
				               
				                            } 
						          });
					
					        }
					     });
				    }
				    
}}
function saveBeforeAjxService(div) {
	var processString = "";

    if(div==8){
    processString = "process_object_name=buPurchaseServiceSub&process_object_method_name=updateAJAXPageLinesDataAdDetail" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
		"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
    }else{
	processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXPageLinesData" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
		"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
	}



	if (div == vnB_Brand){
	    processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXPageLinesDataKweBrand" + 
			           "&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
			           "&brandCode=" + vat.item.getValueByName("#F.brandCode")+ 
			           "&requestCode=" + vat.item.getValueByName("#F.requestCode")+ 
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
	}
	else if (div == vnB_Warehouse){
	    processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXPageLinesDataKweWare" + 
			           "&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
			           "&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode")+ 
			             "&requestCode=" + vat.item.getValueByName("#F.requestCode")+ 
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
	}
	else if (div == vnB_Shop){
	    processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXPageLinesDataKweShop" + 
			           "&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
			           "&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode")+ 
			             "&requestCode=" + vat.item.getValueByName("#F.requestCode")+ 
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
	}
	else if (div == vnB_ItemCategory){
	    processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXPageLinesDataKweCategory" + 
			           "&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
			           "&warehouseCode=" + vat.item.getValueByName("#F.warehouseCode")+ 
			             "&requestCode=" + vat.item.getValueByName("#F.requestCode")+ 
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
	}
	else{
		processString = "process_object_name=buPurchaseService&process_object_method_name=updateAJAXPageLinesData" +
						"&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
						"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
    }
	                

	return processString;
}

function saveBeforeAjxAdDetailService() {
	
    processString = "process_object_name=buPurchaseServiceSub&process_object_method_name=updateAJAXPageLinesDataAdDetail" +
		"&headId=" + vat.item.getValueByName("#F.headId") + "&categoryCode=" + vat.item.getValueByName("#F.categoryCode")+ 
		"&loginEmployeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode + "&status=" + vat.item.getValueByName("#F.status");
    
	return processString;

}
//
function kweloadBeforeAjxService(){
	var processString = "process_object_name=siMenuService&process_object_method_name=getAJAXPageData" +
	                    "&brandCode=" + vat.bean().vatBeanOther.brandCode +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode +
	                    "&applicant=" + vat.item.getValueByName("#F.applicant");
	return processString;		
}


function loadBeforeAjxService(div){
	
	var type = vat.item.getValueByName("type");

	var processString = "";
	
	if (div == vnB_vat7){
	    processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXAttachment" + 
			            "&parentHeadId=" + vat.item.getValueByName("#F.headId")  + 
		                "&parentOrderType=" + vat.item.getValueByName("#F.orderTypeCode") +
		                "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value + 
		                "&ownerType=creator";

	}else if(div == 8){
	   
	   //vat.item.getValueByName("#F.headId")  document.forms[0]["#formId"].value
	    processString = "process_object_name=buPurchaseServiceSub&process_object_method_name=getAJAXPageDataAddetail" +
	                    "&headId=" +  vat.item.getValueByName("#F.headId") +
	            		"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode");
	}else if (div == vnB_Brand)
	{
		
		var processString = "process_object_name=buPurchaseServiceSub&process_object_method_name=getAJAXPageDataAddetailBrand" +
	                    "&brandCode=" + vat.bean().vatBeanOther.brandCode +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&type=" + vat.item.getValueByName("#F.type") +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode ;
	    
	}
	else if (div == vnB_Warehouse)
	{
		var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXPageDataWare" +
	                   
	                     "&warehouseCode=" + vat.bean().vatBeanOther.warehouseCode +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&type=" + vat.item.getValueByName("#F.type") +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode ;
	}
	else if (div == vnB_Shop)
	{
		var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXPageDataShop" + 
	                     "&shopCode=" + vat.bean().vatBeanOther.shopCode +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&type=" + vat.item.getValueByName("#F.type") +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode ;
	}
	else if (div == vnB_ItemCategory)
	{
		var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXPageDataCategory" +
	                    "&brandCode=" + vat.bean().vatBeanOther.brandCode +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	                    "&type=" + vat.item.getValueByName("#F.type") +
	                    "&employeeCode=" + vat.bean().vatBeanOther.loginEmployeeCode ;
	}
	else{

		processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXPageData" +
	                    "&headId=" + vat.item.getValueByName("#F.headId") +
	            		"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&orderTypeCode=" + vat.item.getValueByName("#F.orderTypeCode");
    }
	                
	return processString;
}


// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
    //alert("do after");
	if(vat.bean().vatBeanPicker.result != null){
	    var vsMaxSize = vat.bean().vatBeanPicker.result.length;
	    if( vsMaxSize === 0){
	  	  vat.bean().vatBeanOther.firstRecordNumber   = 0;
		  vat.bean().vatBeanOther.lastRecordNumber    = 0;
		  vat.bean().vatBeanOther.currentRecordNumber = 0;
		}else{
		  vat.bean().vatBeanOther.firstRecordNumber   = 1;
		  vat.bean().vatBeanOther.lastRecordNumber    = vsMaxSize ;
		  vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].headId;
		  
		  refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
	}

}

// 到第一筆
function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 上一筆
function gotoForward(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber - 1;
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在第一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 下一筆
function gotoNext(){	
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber +1 <= vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.currentRecordNumber + 1; 
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}

// 最後一筆
function gotoLast(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if (vat.bean().vatBeanOther.currentRecordNumber != vat.bean().vatBeanOther.lastRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.lastRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].headId;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}
// 刷新頁面
function refreshForm(code){
	document.forms[0]["#formId"].value = code; 
	vat.bean().vatBeanOther.formId = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=buPurchaseAction&process_object_method_name=performInitial"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
     			vat.block.pageRefresh(vnB_Detail);
     			//vat.block.pageRefresh(vnB_Detail2);
     			vat.block.pageRefresh(vnB_Detail5);
     			//vat.block.pageRefresh(vnB_vat7);
     			//vat.block.pageRefresh(vnB_master);
     			vat.block.pageRefresh(vnB_Detail3);
     			//vat.block.pageRefresh(2);
     	        //kweMaster();
     			doFormAccessControl();
				
     	}}
    );
    	
}
/* 報表列印 */
function openReportWindow(type){ 
    vat.bean().vatBeanOther.brandCode  = vat.item.getValueByName("#F.brandCode");
    vat.bean().vatBeanOther.orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
	if("AFTER_SUBMIT"!=type) vat.bean().vatBeanOther.orderNo  = vat.item.getValueByName("#F.orderNo");  //因為調撥單在送出後要直接列印報表，所以要有這行
		vat.block.submit(
					function(){return "process_object_name=buPurchaseService"+
								"&process_object_method_name=getReportConfig";},{other:true,
                    			funcSuccess:function(){
								eval(vat.bean().vatBeanOther.reportUrl);
					}}
		);   
   if("AFTER_SUBMIT"==type) refreshForm();//因為調撥單在送出後要直接列印報表，所以要有這行
    }


    function reconfirmImmovement(orderTypeCode,orderNo){
		if(confirm('是否要列印請採驗')){	
			url = "http://10.1.94.161:8080/crystal/t2/pu1209.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
	
	function reconfirmImmovement0(orderTypeCode,orderNo){
		if(confirm('是否要列請款單')){	
			url = "http://10.1.94.161:8080/crystal/t2/pu1210.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url;
	}
	
	function reconfirmImmovement1(orderTypeCode,orderNo){
		if(confirm('是否要列報修單')){	
			url1 = "http://10.1.94.161:8080/crystal/t2/IRD.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo");
			window.open(url1,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url1;
	}
	
	function Irqprint(orderTypeCode,orderNo,brandCode){
		if(confirm('是否要列印需求單')){	
			url2 = "http://10.1.94.161:8080/crystal/t2/IRQ.rpt?prompt0="+vat.item.getValueByName("#F.orderTypeCode")+"&prompt1="+vat.item.getValueByName("#F.orderNo")+"&prompt2="+vat.item.getValueByName("#F.brandCode");
			window.open(url2,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url2;
	}
	
	function printMenuApplication(headId){
		if(confirm('是否要列印權限申請單')){	
			url2 = "http://10.1.94.161:8080/crystal/t2/KWE1.rpt?prompt0="+vat.item.getValueByName("#F.headId");
			window.open(url2,'CC', 'menubar=yes,resizable=yes,scrollbars=yes,status=yes,width=1024,height=768,left=0,top=0');
		}
		return url2;
	}

// 取得指定連動的類別下拉
function changeCategory(){
	var parentCategory = vat.item.getValueByName("#F.categoryItem");
	

    vat.ajax.XHRequest(
    {
        post:"process_object_name=buPurchaseService"+
                 "&process_object_method_name=getAJAXCategory"+
                 "&categoryItem=" + parentCategory,
        find: function changeRequestSuccess(oXHR){ 
	var allsystem = eval(vat.ajax.getValue("allsystem", oXHR.responseText));
	allsystem[0][0] = "#F.categorySystem";
	vat.item.SelectBind(allsystem); 
        }   
    }); 
}
// 取得指定連動的類別下拉
function changeInCategory(){
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXInChargeCode" +
						"&categorySystem=" + vat.item.getValueByName("#F.categorySystem");
						//eChangerqInChargeCode();
	vat.ajax.startRequest(processString,  function() { 
	  if (vat.ajax.handleState())
			vat.item.setValueByName("#F.otherGroup", vat.ajax.getValue("otherGroup", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.rqInChargeCode", vat.ajax.getValue("rqInChargeCode", vat.ajax.xmlHttp.responseText))
	 });
	
}
function onChangeItemNo() {
	var itemNo = vat.item.getValueByName("#F.itemNo");
	
	var nItemLine = vat.item.getGridLine();
	var sItemNo = vat.item.getGridValueByName("itemNo",nItemLine).toUpperCase();
	
	
	vat.item.setGridValueByName( "itemNo",		nItemLine, sItemNo);
	vat.item.setGridValueByName( "itemName",		nItemLine, 0);
	vat.item.setGridValueByName( "specInfo",		nItemLine, 0);
	vat.item.setGridValueByName( "purUnitPrice",nItemLine, 0);
	vat.item.setGridValueByName( "reUnitPrice",nItemLine, 0);	
	vat.item.setGridValueByName( "supplier",  nItemLine, 0);


	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXLineData" +
							"&itemNo="        + sItemNo	;
						
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				 {
				 		
				 //	vat.item.setGridValueByName("itemNo",nItemLine, vat.ajax.getValue("itemNo", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("itemName",nItemLine, vat.ajax.getValue("itemName", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("specInfo",   nItemLine, vat.ajax.getValue("specInfo", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("purUnitPrice",   nItemLine, vat.ajax.getValue("purUnitPrice", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("reUnitPrice",   nItemLine, vat.ajax.getValue("reUnitPrice", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("supplier",      nItemLine, vat.ajax.getValue("supplier",    vat.ajax.xmlHttp.responseText));
					
					
						//changeItemCategory();
				}
			}
		});
	} 

function onChangeItemName() {
	var itemName = vat.item.getValueByName("#F.itemName");
	
	var nItemLine = vat.item.getGridLine();
	var sItemName = vat.item.getGridValueByName("itemName",nItemLine).toUpperCase();
	
	vat.item.setGridValueByName( "itemName",		nItemLine, sItemName);
	vat.item.setGridValueByName( "quantity",		nItemLine, 0);

	

		var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXLineData1" +
							"&itemName="        + sItemName	;
						
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				 {
				 	
				 //	vat.item.setGridValueByName("itemNo",nItemLine, vat.ajax.getValue("itemNo", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("itemName",nItemLine, vat.ajax.getValue("itemName", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("quantity",   nItemLine, vat.ajax.getValue("quantity", vat.ajax.xmlHttp.responseText));
						//changeItemCategory();
				}
			}
		});
	} 
	
	/* 指定下一個狀態 */
function changeFormStatus(formId, processId, status, approvalResult){
    var nextStatus = "";
    if(formId == null || formId == ""){
       	nextStatus = "SIGNING";
    }else if(processId != null && processId != ""){
        if(status == "SAVE" || status == "REJECT"){
            nextStatus = "SIGNING";
        }else if(status == "SIGNING"){
            nextStatus = "PLAN";
        }else if(status == "PLAN"){
            nextStatus = "FINISH";
        }else if(approvalResult == "true"){
            nextStatus = status;
        }else if(approvalResult == "false"){
            nextStatus = "REJECT";
        }
    }
    return nextStatus;
}
	/* 指定下一個狀態 */
function changeFormStatusPRC(formId, processId, status, approvalResult){
    var nextStatus = "";
    if(formId == null || formId == ""){
       	nextStatus = "SIGNING";
    }else if(processId != null && processId != ""){
        if(status == "SAVE" || status == "REJECT"){
            nextStatus = "SIGNING";
        }else if(status == "SIGNING"){
            nextStatus = "PURCHASE";
        }else if(status == "PURCHASE"){
            nextStatus = "FINISH";
        }else if(approvalResult == "true"){
            nextStatus = status;
        }else if(approvalResult == "false"){
            nextStatus = "REJECT";
        }
    }
    return nextStatus;
}
// 依formId鎖form

function doFormAccessControl(){
	var formId = vat.bean().vatBeanOther.formId;
	var processId            = document.forms[0]["#processId"].value.replace(/^\s+|\s+$/, '');
	var orderTypeCode   = vat.item.getValueByName("#F.orderTypeCode");
	var status   = vat.item.getValueByName("#F.status");
	var loginDepartment   = vat.item.getValueByName("#loginDepartment");
    
	    if(orderTypeCode=='LOA'&&vat.item.getValueByName("#loginDepartment")=="103"|document.forms[0]["#loginEmployeeCode" ].value=='T76927'){
	       vat.item.setStyleByName("#B.submit", "display", "inline");
	    }else if(orderTypeCode=='LOA'&&vat.item.getValueByName("#loginDepartment")!="103"|document.forms[0]["#loginEmployeeCode" ].value!='T76927'){
	       vat.item.setStyleByName("#B.submit", "display", "none");
	    }
	
     	 //vat.item.setAttributeByName("vatBlock_aaa", "display:none", true);
     	if(vat.item.getValueByName("#loginDepartment")!="103")
     	{
     	 vat.tabm.displayToggle(0 ,"xTab8",false ,false, false);
     	 vat.item.setAttributeByName("#F.status", "readOnly", true);
     	 vat.item.setAttributeByName("#F.department", "readOnly", true);
     	}
       if(vat.item.getValueByName("#F.loginDepartment")=="103")
     	{
     	 vat.tabm.displayToggle(0 ,"xTab8",true ,false, false);
     	 vat.item.setAttributeByName("#F.status", "readOnly", false);
     	 vat.item.setAttributeByName("#F.department", "readOnly", false);
     	}
     	
     	if(vat.item.getValueByName("#F.status")=="FINISH"&&vat.item.getValueByName("#loginDepartment")=="103")
     	{
     	 vat.tabm.displayToggle(0 ,"xTab8",true ,false, false);
     	 vat.item.setAttributeByName("#F.requestCode", "readOnly", false);
     	 vat.item.setAttributeByName("#F.contractTel", "readOnly", false);
     	 vat.item.setAttributeByName("#F.no", "readOnly", false);
     	 vat.item.setAttributeByName("#F.requestDate", "readOnly", false);
     	 vat.item.setAttributeByName("#F.categoryItem", "readOnly", false);
     	 vat.item.setAttributeByName("#F.categorySystem", "readOnly", false);
     	 vat.item.setAttributeByName("#F.description", "readOnly", false);
     	 vat.item.setAttributeByName("#F.depManager", "readOnly", false);
     	 vat.item.setAttributeByName("#F.categoryGroup", "readOnly", false);
     	 vat.item.setAttributeByName("#F.requestSource", "readOnly", false);
     	 vat.item.setAttributeByName("#F.estimateStartDare", "readOnly", false);
     	 vat.item.setAttributeByName("#F.estimateEndDare", "readOnly", false);
     	 vat.item.setAttributeByName("#F.rqInChargeCode", "readOnly", false);
     	 vat.item.setAttributeByName("#F.otherGroup", "readOnly", false);
     	 vat.item.setAttributeByName("#F.processDescription", "readOnly", false);
     	 //vat.item.setAttributeByName("#F.hisstoryInfo", "readOnly", false);
     	 vat.item.setAttributeByName("#F.rqInChargeCode", "readOnly", false);
     	}
     	if(vat.item.getValueByName("#F.status")=="PLAN"&&vat.item.getValueByName("#loginDepartment")=="103")
     	{
     	 vat.tabm.displayToggle(0 ,"xTab8",true ,false, false);
     	 vat.item.setAttributeByName("#F.requestCode", "readOnly", false);
     	 vat.item.setAttributeByName("#F.contractTel", "readOnly", false);
     	 vat.item.setAttributeByName("#F.no", "readOnly", false);
     	 vat.item.setAttributeByName("#F.requestDate", "readOnly", false);
     	 vat.item.setAttributeByName("#F.categoryItem", "readOnly", false);
     	 vat.item.setAttributeByName("#F.categorySystem", "readOnly", false);
     	 vat.item.setAttributeByName("#F.description", "readOnly", false);
     	 vat.item.setAttributeByName("#F.depManager", "readOnly", false);
     	 vat.item.setAttributeByName("#F.categoryGroup", "readOnly", false);
     	 vat.item.setAttributeByName("#F.requestSource", "readOnly", false);
     	 vat.item.setAttributeByName("#F.estimateStartDare", "readOnly", false);
     	 vat.item.setAttributeByName("#F.estimateEndDare", "readOnly", false);
     	 vat.item.setAttributeByName("#F.rqInChargeCode", "readOnly", false);
     	 vat.item.setAttributeByName("#F.otherGroup", "readOnly", false);
     	 vat.item.setAttributeByName("#F.processDescription", "readOnly", false);
     	 //vat.item.setAttributeByName("#F.hisstoryInfo", "readOnly", false);
     	 vat.item.setAttributeByName("#F.rqInChargeCode", "readOnly", false);
     	 vat.item.setStyleByName("#B.stop"  , "display", "inline");
     	 vat.item.setStyleByName("#B.save"  , "display", "none");
     	}
     if(vat.item.getValueByName("#F.status")=="PLAN"&&vat.item.getValueByName("#loginDepartment")!="103")
     	{
     	 vat.item.setGridAttributeByName("specInfo", "readOnly", true);
     	 vat.item.setGridAttributeByName("finishDate","readOnly", true);
     	// vat.item.setGridAttributeByName("executeTimeStart", "readOnly", true);
     	 vat.item.setGridAttributeByName("status","readOnly", true);
     	 vat.item.setGridAttributeByName("taskInchargeCode", "readOnly", true);
     	 vat.item.setGridAttributeByName("taskInchargeName", "readOnly", true);
     	 vat.item.setAttributeByName("#F.requestCode", "readOnly", true);
     	 vat.tabm.displayToggle(0 ,"xTab8",true ,false, false);
     	 vat.item.setAttributeByName("#F.requestCode", "readOnly", true);
     	 vat.item.setAttributeByName("#F.contractTel", "readOnly", true);
     	 vat.item.setAttributeByName("#F.no", "readOnly", true);
     	 vat.item.setAttributeByName("#F.requestDate", "readOnly", true);
     	 vat.item.setAttributeByName("#F.categoryItem", "readOnly", true);
     	 vat.item.setAttributeByName("#F.categorySystem", "readOnly", true);
     	 vat.item.setAttributeByName("#F.description", "readOnly", true);
     	 vat.item.setAttributeByName("#F.depManager", "readOnly", true);
     	 vat.item.setAttributeByName("#F.categoryGroup", "readOnly", true);
     	 vat.item.setAttributeByName("#F.requestSource", "readOnly", true);
     	 vat.item.setAttributeByName("#F.estimateStartDare", "readOnly", true);
     	 vat.item.setAttributeByName("#F.estimateEndDare", "readOnly", true);
     	 vat.item.setAttributeByName("#F.rqInChargeCode", "readOnly", true);
     	 vat.item.setAttributeByName("#F.otherGroup", "readOnly", true);
     	 vat.item.setAttributeByName("#F.processDescription", "readOnly", true);
     	 //vat.item.setAttributeByName("#F.hisstoryInfo", "readOnly", true);
     	 vat.item.setAttributeByName("#F.rqInChargeCode", "readOnly", true);
     	 vat.item.setStyleByName("#B.submit", "display", "none");
     	 vat.item.setStyleByName("#B.stop"  , "display", "none");
     	 vat.item.setStyleByName("#B.save"  , "display", "none");
     	}
     if(vat.item.getValueByName("#F.status")=="FINISH"&&vat.item.getValueByName("#loginDepartment")!="103")
     	{
     	 vat.tabm.displayToggle(0 ,"xTab8",true ,false, false);
     	 vat.item.setGridAttributeByName("specInfo", "readOnly", true);
     	 vat.item.setGridAttributeByName("finishDate", "readOnly", true);
     	 vat.item.setGridAttributeByName("executeTimeStart", "readOnly", true);
     	 vat.item.setGridAttributeByName("status", "readOnly", true);
     	 vat.item.setGridAttributeByName("taskInchargeCode", "readOnly", true);
     	 vat.item.setGridAttributeByName("taskInchargeName", "readOnly", true);
     	 vat.item.setAttributeByName("#F.requestCode", "readOnly", true);
     	 vat.item.setAttributeByName("#F.contractTel", "readOnly", true);
     	 vat.item.setAttributeByName("#F.no", "readOnly", true);
     	 vat.item.setAttributeByName("#F.requestDate", "readOnly", true);
     	 vat.item.setAttributeByName("#F.categoryItem", "readOnly", true);
     	 vat.item.setAttributeByName("#F.categorySystem", "readOnly", true);
     	 vat.item.setAttributeByName("#F.description", "readOnly", true);
     	 vat.item.setAttributeByName("#F.depManager", "readOnly", true);
     	 vat.item.setAttributeByName("#F.categoryGroup", "readOnly", true);
     	 vat.item.setAttributeByName("#F.requestSource", "readOnly", true);
     	 vat.item.setAttributeByName("#F.estimateStartDare", "readOnly", true);
     	 vat.item.setAttributeByName("#F.estimateEndDare", "readOnly", true);
     	 vat.item.setAttributeByName("#F.rqInChargeCode", "readOnly", true);
     	 vat.item.setAttributeByName("#F.otherGroup", "readOnly", true);
     	 vat.item.setAttributeByName("#F.processDescription", "readOnly", true);
     	 //vat.item.setAttributeByName("#F.hisstoryInfo", "readOnly", true);
     	 vat.item.setAttributeByName("#F.rqInChargeCode", "readOnly", true);
     	 vat.item.setStyleByName("#B.submit", "display", "none");
     	 vat.item.setStyleByName("#B.stop"  , "display", "none");
     	 vat.item.setStyleByName("#B.save"  , "display", "none");
     	 vat.item.setStyleByName("#B.replan"  , "display", "none");
     	}
     	if(vat.item.getValueByName("#F.status")=="SAVE"&&vat.item.getValueByName("#loginDepartment")!="103")
     	{
     	 vat.item.setStyleByName("#B.stop"  , "display", "none");
     	 vat.item.setStyleByName("#B.replan"  , "display", "none");
     	 vat.item.setStyleByName("#B.update"  , "display", "none");
     	}
     	if(vat.item.getValueByName("#F.status")=="SAVE"&&vat.item.getValueByName("#loginDepartment")=="103")
     	{
     	 vat.item.setStyleByName("#B.stop"  , "display", "none");
     	 vat.item.setStyleByName("#B.replan"  , "display", "none");
     	 vat.item.setStyleByName("#B.update"  , "display", "none");
     	}
     	if(vat.item.getValueByName("#F.status")=="SUSPEND"&&vat.item.getValueByName("#loginDepartment")=="103")
     	{
     	 vat.item.setStyleByName("#B.stop"  , "display", "none");
     	 vat.item.setStyleByName("#B.save"  , "display", "none");
     	 vat.item.setStyleByName("#B.submit", "display", "none");
     	}
     	if(vat.item.getValueByName("#F.orderTypeCode")=="IRQ")
   		{
		vat.item.setStyleByName("#B.print2", "display", "inline");
		vat.item.setStyleByName("#B.print", "display", "none");
		vat.item.setStyleByName("#B.print0", "display", "none");
		vat.item.setStyleByName("#B.print1", "display", "none");
		vat.item.setStyleByName("#B.print3", "display", "none");
		vat.item.setStyleByName("#B.getMenu", "display", "none");
     	}
     	if(vat.item.getValueByName("#F.orderTypeCode")=="PRC")
   		{
		vat.item.setStyleByName("#B.print2", "display", "none");
		vat.item.setStyleByName("#B.print", "display", "inline");
		vat.item.setStyleByName("#B.print1", "display", "none");
		vat.item.setStyleByName("#B.print3", "display", "none");
		vat.item.setStyleByName("#B.getMenu", "display", "none");
     	}
     	if(vat.item.getValueByName("#F.status")=="SIGNING"&&vat.item.getValueByName("#F.orderTypeCode")=="IRQ")
   		{
		vat.item.setStyleByName("#B.print2", "display", "inline");
		vat.item.setStyleByName("#B.print", "display", "none");
		vat.item.setStyleByName("#B.print0", "display", "none");
		vat.item.setStyleByName("#B.print1", "display", "none");
		vat.item.setStyleByName("#B.print3", "display", "none");
		vat.item.setStyleByName("#B.getMenu", "display", "none");
     	}
     	if(vat.item.getValueByName("#F.status")=="SIGNING"&&vat.item.getValueByName("#F.orderTypeCode")=="PRC")
   		{
		vat.item.setStyleByName("#B.print2", "display", "none");
		vat.item.setStyleByName("#B.print", "display", "inline");
		vat.item.setStyleByName("#B.print1", "display", "none");
		vat.item.setStyleByName("#B.print3", "display", "none");
		vat.item.setStyleByName("#B.getMenu", "display", "none");
		vat.item.setStyleByName("#B.stop"  , "display", "none");
     	vat.item.setStyleByName("#B.replan"  , "display", "none");
     	vat.item.setStyleByName("#B.save"  , "display", "none");
     	}
     	if(vat.item.getValueByName("#F.status")=="PURCHASE")
   		{
		vat.item.setStyleByName("#B.print2", "display", "none");
		vat.item.setStyleByName("#B.print", "display", "inline");
		vat.item.setStyleByName("#B.print1", "display", "none");
		vat.item.setStyleByName("#B.print3", "display", "none");
		vat.item.setStyleByName("#B.getMenu", "display", "none");
		vat.item.setStyleByName("#B.stop"  , "display", "none");
     	vat.item.setStyleByName("#B.replan"  , "display", "none");
     	vat.item.setStyleByName("#B.save"  , "display", "none");
     	}
     	if(vat.item.getValueByName("#F.orderTypeCode")=="LOA")
   		{
   		vat.item.setStyleByName("#B.attachFile", "display", "none");
		vat.item.setStyleByName("#B.print2", "display", "none");
		vat.item.setStyleByName("#B.print", "display", "none");
		vat.item.setStyleByName("#B.print0", "display", "none");
		vat.item.setStyleByName("#B.print1", "display", "none");
		vat.item.setStyleByName("#B.print3", "display", "inline");
		vat.item.setStyleByName("#B.getMenu", "display", "inline");
     	}
     	 if(orderTypeCode!='LOA'&&vat.item.getValueByName("#F.status")=="FINISH"&&vat.item.getValueByName("#loginDepartment")=="103")
     	 {
     	 vat.item.setStyleByName("#B.stop"  , "display", "none");
     	 vat.item.setStyleByName("#B.save"  , "display", "none");
     	 vat.item.setStyleByName("#B.submit"  , "display", "none");
     	 vat.item.setStyleByName("#B.replan"  , "display", "none");
     	 }
     	 if(vat.item.getValueByName("#F.status")=="REPLAN"&&vat.item.getValueByName("#loginDepartment")=="103")
     	 {
     	 vat.item.setStyleByName("#B.stop"  , "display", "none");
     	 vat.item.setStyleByName("#B.save"  , "display", "none");
     	 //vat.item.setStyleByName("#B.submit"  , "display", "none");
     	 vat.item.setStyleByName("#B.replan"  , "display", "none");
     	 }
     	 if(vat.item.getValueByName("#F.status")=="REPLAN"&&vat.item.getValueByName("#loginDepartment")!="103")
     	 {
     	 vat.item.setStyleByName("#B.stop"  , "display", "none");
     	 vat.item.setStyleByName("#B.save"  , "display", "none");
     	 //vat.item.setStyleByName("#B.submit"  , "display", "none");
     	 vat.item.setStyleByName("#B.replan"  , "display", "none");
     	 }
     	 	if(formId != ""){
		// 流程內
		if( processId != null && processId != 0 ){ //從待辦事項進入
			vat.item.setStyleByName("#B.new", 		"display", "none");
			vat.item.setStyleByName("#B.search", 	"display", "none");
			if( status == "SAVE" || status == "REJECT"){
				vat.item.setStyleByName("#B.void", 		"display", "inline"); 
			}
		}else{
			// 查詢回來
			if( status == "SAVE" || status == "REJECT"){
				vat.item.setStyleByName("#B.submit", 	"display", "none"); 
				vat.item.setStyleByName("#B.save", 		"display", "inline"); 
				//======================<header>=============================================
				//======================<detail>=============================================
/*				if(orderTypeCode=="PAP"){
					vat.item.setGridAttributeByName("localCost", "readOnly", true); 
				}else if(orderTypeCode=="PAJ"){
					vat.item.setGridAttributeByName("newQuotationPrice", "readOnly", true);
				}
				vat.item.setGridAttributeByName("itemCode", "readOnly", true); 
				vat.item.setGridAttributeByName("searchItem", "readOnly", true);
				vat.item.setGridAttributeByName("unitPrice", "readOnly", true);
				vat.item.setGridAttributeByName("isDeleteRecord", "readOnly", true);*/
				
				vat.block.canGridModify([vnB_Detail], false,false,false);
			}	
		}
	}
}
/* 計算 單筆 LINE 合計的部份 */
function calculateLineAmount() {
	//var purTotalAmount     = vat.item.getValueByName("#F.purTotalAmount");
	var nItemLine            = vat.item.getGridLine();
	var quantity             = vat.item.getGridValueByName("quantity",nItemLine);
	var purTotalAmount       = vat.item.getGridValueByName("purTotalAmount",nItemLine);
	var reTotalAmount        = vat.item.getGridValueByName("reTotalAmount",nItemLine);

	if (vat.item.getGridValueByName("quantity",nItemLine)==""){
		quantity = 0;
	}
	var purUnitPrice        = vat.item.getGridValueByName("purUnitPrice",nItemLine);
	var reUnitPrice         = vat.item.getGridValueByName("reUnitPrice",nItemLine);
	var purTotalAmount      =(parseFloat(quantity) * parseFloat(purUnitPrice));
	var reTotalAmount       =(parseFloat(quantity) * parseFloat(reUnitPrice));
	vat.item.setGridValueByName("quantity",				nItemLine, quantity);
	vat.item.setGridValueByName("purTotalAmount",		nItemLine, purTotalAmount);
	vat.item.setGridValueByName("reTotalAmount",		nItemLine, reTotalAmount);
	
}
function eChangeRequest() {
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByRequest" +
						"&requestCode="  + vat.item.getValueByName("#F.requestCode")+
						"&brandCode="    + vat.item.getValueByName("#F.brandCode");;
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.request", vat.ajax.getValue("request", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.requestCode", vat.ajax.getValue("requestCode", vat.ajax.xmlHttp.responseText))
			vat.item.setValueByName("#F.department", vat.ajax.getValue("department", vat.ajax.xmlHttp.responseText))
		}
	});
}
function getEmployeeRole(vsEmployee) {
    if ("" != vsEmployee, vat.item.getValueByName("#F." + vsEmployee)) {
        vat.item.setValueByName("#F." + vsEmployee, vat.item.getValueByName("#F." + vsEmployee).toUpperCase());
        var processString = "process_sql_code=FindEmployeeRole&employeeCode=" + vat.item.getValueByName("#F." + vsEmployee);
        vat.ajax.startRequest(processString, function () {
            if (vat.ajax.handleState()) {
                if (vat.ajax.found(vat.ajax.xmlHttp.responseText)) {
                    vat.item.setValueByName("#F.role", vat.ajax.getValue("EMPLOYEE_ROLE", vat.ajax.xmlHttp.responseText));
                } else {
                    //vat.item.setValueByName("#F." + vsEmployee + "Name", "");
                    //alert("查無此員工代號");if
                    var processString = "process_object_name=buPurchaseService&process_object_method_name=addEmployeeRole" +
						"&depManager="  + vat.item.getValueByName("#F.depManager");
					
					vat.ajax.startRequest(processString, function () {
						if (vat.ajax.handleState()) {
							vat.item.setValueByName("#F.depManagerName", vat.ajax.getValue("depManagerName", vat.ajax.xmlHttp.responseText))
							vat.item.setValueByName("#F.depManager", vat.ajax.getValue("depManager", vat.ajax.xmlHttp.responseText))
						}
					});
                }
            }
        });
    }
}

function eChangedepManager() {
   // alert(vat.item.getValueByName("#loginDepartment"));
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByDepManager" +
						"&depManager="  + vat.item.getValueByName("#F.depManager");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
		//alert("1:"+document.forms[0]["#loginDepartment"   ].value);
		//alert("2:"+vat.ajax.getValue("dept", vat.ajax.xmlHttp.responseText));
		//&&document.forms[0]["#loginDepartment"   ].value!='103'
			if( (document.forms[0]["#loginDepartment"   ].value!='103'&&vat.ajax.getValue("dept", vat.ajax.xmlHttp.responseText)!=document.forms[0]["#loginDepartment"   ].value)){
			  alert("主管部門別與登入者部門別不符  請重新輸入~");
			}else if(document.forms[0]["#loginDepartment"   ].value=='103'){
			   
			   vat.item.setValueByName("#F.depManagerName", vat.ajax.getValue("depManagerName", vat.ajax.xmlHttp.responseText))
			   vat.item.setValueByName("#F.depManager", vat.ajax.getValue("depManager", vat.ajax.xmlHttp.responseText))
			}else{
			   vat.item.setValueByName("#F.depManagerName", vat.ajax.getValue("depManagerName", vat.ajax.xmlHttp.responseText))
			   vat.item.setValueByName("#F.depManager", vat.ajax.getValue("depManager", vat.ajax.xmlHttp.responseText))
		    }
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

function eChangetasktaskInchargeCode() {
	var taskInchargeCode = vat.item.getValueByName("#F.taskInchargeCode");
	var nItemLine = vat.item.getGridLine();
	var sTaskInchargeCode = vat.item.getGridValueByName("taskInchargeCode",nItemLine).toUpperCase();
	
	vat.item.setGridValueByName( "taskInchargeCode",		nItemLine, sTaskInchargeCode);
	vat.item.setGridValueByName( "taskInchargeName",		nItemLine, 0);

	//alert("taskInchargeCode="+sTaskInchargeCode);
	//alert("nItemLine="+nItemLine);
	//alert("itemNo="+sItemNo);

		var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXLineData1" +
							"&taskInchargeCode="        + sTaskInchargeCode	;
	//alert("processString="+processString);					
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				 {
				 	//alert("processg="+vat.ajax.handleState());	
					//	vat.item.setGridValueByName("itemNo",nItemLine, vat.ajax.getValue("itemNo", vat.ajax.xmlHttp.responseText));
					//vat.item.setGridValueByName("taskInchargeCode",nItemLine, vat.ajax.getValue("taskInchargeCode", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("taskInchargeName",   nItemLine, vat.ajax.getValue("taskInchargeName", vat.ajax.xmlHttp.responseText));
					//changeItemCategory();
				}
			}
		});
	} 

function eChangeRequestCode() {
	var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXFormDataByRequestCode" +
						"&request="  + vat.item.getValueByName("#F.request");
					
	vat.ajax.startRequest(processString, function () {
		if (vat.ajax.handleState()) {
			vat.item.setValueByName("#F.requestCode", vat.ajax.getValue("requestCode", vat.ajax.xmlHttp.responseText))
	
		}
	});
}
function doAfterPickerEmployee(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.requestCode", vat.bean().vatBeanPicker.result[0].employeeCode);
    	vat.item.setValueByName("#F.request", vat.bean().vatBeanPicker.result[0].chineseName);
	}

}
function doAfterPickerEmployee1(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.depManager", vat.bean().vatBeanPicker.result[0].employeeCode);
    	vat.item.setValueByName("#F.depManagerName", vat.bean().vatBeanPicker.result[0].chineseName);
	}
}
function doAfterPickerEmployee2(){
	if(vat.bean().vatBeanPicker.result !== null){
    	vat.item.setValueByName("#F.rqInChargeCode", vat.bean().vatBeanPicker.result[0].employeeCode);
    	vat.item.setValueByName("#F.otherGroup", vat.bean().vatBeanPicker.result[0].chineseName);
	}
}


function openFile(id){
	var line 		 = vat.item.getGridLine(id);
	var fileName 	 = vat.item.getGridValueByName("fileName",line);
	
	if(fileName!== null && fileName!==""){
		window.open("jsp/openFile.jsp?fileName="+vat.item.getGridValueByName("physicalName",line)+
		"&filePath="+vat.item.getGridValueByName("physicalPath",line)+"&name="+vat.item.getGridValueByName("fileName",line));
	}
}

function doUpload(){
	var ownerType = "creator",typeValue="";  // vat.item.getValueByName("#F.status")
	var status = vat.item.getValueByName("#F.status");
	var orderTypeCode = vat.item.getValueByName("#F.orderTypeCode");
		
	if(status !== "SAVE" ){
		ownerType="incharge";
	}

	if((status == "ASSIGN" && orderTypeCode =="IRWK")){ // 覆蓋回去
		ownerType="creator"; 
	}

	// 告知跳出視窗預設下拉為測試案例
	if("IRT" == orderTypeCode || ("IRQ" == orderTypeCode && "TESTING" == status) ){
		typeValue = "testCase";
	}	
		
		
	var width = "400";
    var height = "200";
	window.open("jsp/AdRequestFileUpload.jsp?headId=" + vat.item.getValueByName("#F.headId") + 
		                    "&loginEmployeeCode=" + document.forms[0]["#loginEmployeeCode" ].value + 
		                    "&orderType=" + orderTypeCode+
		                    "&ownerType=" + ownerType+
		                    "&typeValue=" + typeValue,
		                    "uploads",
		                    'menubar=no,resizable=yes,scrollbars=yes,status=no,width='+width+',height='+height+',left=' + (screen.availWidth - width)/2 +' ,top=' + (screen.availHeight - height)/2);

}

function doPageRefresh(){
    
    vat.block.pageRefresh(vnB_Detail3);
}

function saveSuccessAfter() {
   //alert("uiui");
}

function kwePageLoadSuccess(){

}
function doPageDataSave(div){
//	var status = vat.item.getValueByName("#F.status");
//	if(status == "SAVE"){
	var requestCode = vat.item.getValueByName("#F.requestCode");
	if(requestCode != "" ){
		if(div == vnB_Brand){
			activeTab = vnB_Brand;
		}else if(div == vnB_Warehouse){
			activeTab = vnB_Warehouse;
		}else if(div == vnB_Shop){
			activeTab = vnB_Shop;
		}else if(div == vnB_ItemCategory){
			activeTab = vnB_ItemCategory;
		}
	}	
//	}
}
function getUserRepeat(){

	alert("回復成功");
   
   vat.ajax.XHRequest({
          asyn:false,
          post:"process_object_name=buPurchaseService"+
                   "&process_object_method_name=getMailList"+
                   "&headId=" + vat.item.getValueByName("#F.headId")+
                   "&requestCode=" + vat.item.getValueByName("#F.requestCode"),
          find: function change(oXHR){ 
          		//alert("test");
          	  vat.block.pageDataLoad( vnB_Detail, vnCurrentPage = 1);	
          } 
	});	
	
}
function onChangeItemName(){
	var itemName = vat.item.getValueByName("#F.itemName");
	
	var nItemLine = vat.item.getGridLine();
	var sItemName = vat.item.getGridValueByName("itemName",nItemLine).toUpperCase();
	
	vat.item.setGridValueByName( "itemName",		nItemLine, sItemName);
	vat.item.setGridValueByName( "itemNo",		nItemLine, sItemName);
	vat.item.setGridValueByName( "specInfo",		nItemLine, 0);
	vat.item.setGridValueByName( "purUnitPrice",nItemLine, 0);
	vat.item.setGridValueByName( "reUnitPrice",nItemLine, 0);	
	vat.item.setGridValueByName( "supplier",  nItemLine, 0);

	

		var processString = "process_object_name=buPurchaseService&process_object_method_name=getAJAXLineDataItemName" +
							"&itemName="        + sItemName	;
						
		vat.ajax.startRequest(processString, function () {
			if (vat.ajax.handleState()) {
				 {
				 	
					vat.item.setGridValueByName("itemNo",nItemLine, vat.ajax.getValue("itemNo", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("specInfo",   nItemLine, vat.ajax.getValue("specInfo", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("purUnitPrice",   nItemLine, vat.ajax.getValue("purUnitPrice", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("reUnitPrice",   nItemLine, vat.ajax.getValue("reUnitPrice", vat.ajax.xmlHttp.responseText));
					vat.item.setGridValueByName("supplier",      nItemLine, vat.ajax.getValue("supplier",    vat.ajax.xmlHttp.responseText));
				}
			}
		});
	} 