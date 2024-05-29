vat.debug.disable();

var vnB_Button = 0;
var vnB_Master = 1;
var vnB_Detail = 2;
var vnB_Total = 6;
var vnB_POS = 5;
var vnB_function = 7;
var vnB_mz = 8;

var ptrQty = "";
var ptr = 1;
var cursor ="";
var oldPtr = 1;
var color = "";
var changePage = false;
var lastPage = 1;
//共用方法變數d
var vPos_Function;
var vPos_Img;
var vPos_Comment;
var isInsert;
var nowStep;//0:未刷商品  1:小計前明細編輯 2:小計後凍結明細
var submitType = "production";

function outlineBlock(){

  	formInitial();
	
	document.write('<table border="0" width="60%" height="100%">');
 	document.write('<tr height="10%" ><td  colspan="7" >');
	
	buttonLineTag();//功能區
  	
  	document.write('</td></tr>');
 	document.write('<tr height="10%" >');
  	document.write('<td  colspan="7" >');
 	
 	headerInitial();
 	
 	document.write('</td>');
 	document.write('</tr>');
   //--------------------------
   	
 	document.write('<tr height="10%">');
   	document.write('<td colspan="4" width="5px">');
   	
   	detailInitial(); 
   	posBorcode();
	
	document.write('</td>');
	document.write('<td colspan="3">');
	document.write('<div id = "menu01">');
						
	totalInitial();
			
	document.write('</div>');
	document.write('<div id = "menu02" style="display:none;">');
	
	document.write('</div>');
	document.write('<div id = "menu03" style="display:none;">');
	
	document.write('</div>');
	document.write('</td>');
	document.write('</tr>');
   //--------------------------detail
//------------------------------------------------------------------------------button
	document.write('<tr>');
  	document.write('<td colspan="4" >');

	functionInitial();	
  	
  	document.write('</td>');
  	document.write('<td colspan="3">');
  	
  	mzInitial();
  	
  	document.write('</td>');
  	document.write('</tr>');
	document.write('</table>');
	
	//functionLable('1');
    nowStep = 0;
    //eChangeEmployee();
	
	
}

function formInitial(){ 
   	 
   	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: 'T2',
          loginEmployeeCode  	: 'T96085',
          formId             	: '0',
          superintendentCode	: 'T96085', 
          salesType				: '1', 
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
        
	   	vat.bean.init(	
	  		function(){
					return "process_object_name=soDepartmentOrderAction&process_object_method_name=performPosMainInitial"; 
	    	},{
	    		other: true
	    	}
	    );
	 
  	vPos_Function ="";
  	vPos_Img = "";
  	vPos_Comment = "";
}
/**輸入區塊**/
function posBorcode(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_POS, {
	id: "vatBlock_Pos", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-left-width: 0px; border-color: #ffffff;'",
	title:"", rows:[
	 {row_style:"", cols:[
	 	{items:[{name:"#L.left",	type:"LABEL" , 	value:"購買額度尚欠25000",size:"40"}]},
	 	{items:[{name:"#F.posBroCode"	, eBlur:"focusPosBar()"		, type:"TEXT"    ,size:"58"}],td:"style='background-color:#FFFFFF; border-width: 1px;'"}]},
	 	
	  ],
		beginService:"",
		closeService:""
	});
}

function setDefaltValue(){
	vat.item.setValueByName("#F.shopCode" , "F9900");
	vat.item.setValueByName("#F.machineCode" , "A8");
	vat.item.setValueByName("#F.supxlier" , "T17888");
	vat.item.setValueByName("#F.transDate" , "2020/10/10");
	vat.item.setValueByName("#F.invoiceNo" , "DF12XOXOOOO");
	vat.item.setValueByName("#F.enterpriceCode" , "12371287");
	vat.item.setValueByName("#F.transactionNo" , "DZN00001XXXX");
	vat.item.setValueByName("#F.customerNo" , "1039284");
	vat.item.setValueByName("#F.appCode" , "09XXXXXXXXX");
}

/**銷售主檔區塊**/
function headerInitial(){ 
	var branchCode = vat.bean("branchCode");	
	vat.block.create(vnB_Master, {
	id: "vatMasterDiv", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.shopCode", 						type:"LABEL" , 	value:"店別"}]},
				{items:[{name:"#F.shopCode", 						type:"TEXT",  mode:"READONLY" ,	bind:"shopCode" },
						{name:"#F.headId",				type:"TEXT",  mode:"READONLY", 	bind:"headId" },
						{name:"#F.formId",				type:"TEXT",  mode:"HIDDEN", 	bind:"formId" },
						{name:"#F.salesoOrderId",				type:"TEXT",  mode:"HIDDEN", 	bind:"salesoOrderId" }]},
				{items:[{name:"#L.machineCode", 					type:"LABEL" , 	value:"機號"}]},
				{items:[{name:"#F.machineCode", 					type:"TEXT",  mode:"READONLY" ,	bind:"machineCode" }]},
				{items:[{name:"#L.superintendentCode", 				type:"LABEL" , 	value:"收銀員"}]},
				{items:[{name:"#F.superintendentCode", 				type:"DATE",  mode:"READONLY" , 	bind:"superintendentCode"}]},
				{items:[{name:"#L.salesOrderDate", 				type:"LABEL" , 	value:"交易日期"}]},
				{items:[{name:"#F.salesOrderDate",				type:"DATE", 	bind:"salesOrderDate", 				size:25, maxLen:25 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.invoiceNo", 				type:"LABEL" , 	value:"發票號碼"}]},
				{items:[{name:"#F.invoiceNo", 				type:"TEXT",  mode:"READONLY", 	bind:"invoiceNo" },
						{name:"#F.customerName", 				type:"TEXT",  mode:"READONLY" },
						{name:"#F.salesType", 					type:"TEXT",  mode:"HIDDEN" ,	bind:"salesType" },
						{name:"#F.useYear", 					type:"TEXT",  mode:"HIDDEN" ,	bind:"useYear" }]},
				{items:[{name:"#L.shopCode", 					type:"LABEL" , 	value:"統編"}]},
				{items:[{name:"#F.enterpriceCode",		 			type:"TEXT",  mode:"READONLY" ,	bind:"shopCode" }]},
				{items:[{name:"#L.transactionNo", 			type:"LABEL" , 	value:"交易序號"}]},
				{items:[{name:"#F.transactionNo", 			type:"TEXT",  mode:"READONLY" ,	bind:"transactionNo"},
						{name:"#F.salesEmployeeName",	 		type:"TEXT" , mode:"READONLY"}
				]},
				{items:[{name:"#L.remark1", 				type:"LABEL" , 	value:"折扣代碼（滿額贈xa"}]},
				{items:[{name:"#F.remark1",				type:"TEXT", 	bind:"remark1", 				size:25, maxLen:25 }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.brandCode", 				type:"LABEL" , 	value:"事業體代碼"}]},
				{items:[{name:"#F.brandCode", 				type:"TEXT",  mode:"READONLY", 	bind:"brandCode" },
						{name:"#F.brandName", 				type:"TEXT",  mode:"READONLY" ,	bind:"brandName"},
						{name:"#F.customerName", 			type:"TEXT",  mode:"READONLY" ,	bind:"customerName" }]},
				{items:[{name:"#L.appCode", 				type:"LABEL" , 	value:"APP代號"}]},
				{items:[{name:"#F.appCode", 				type:"TEXT",  mode:"READONLY", 	bind:"customerCode" }],td:" colSpan=5"}
			]}
		],
		beginService:"",
		closeService:""	
	});
}
///**現金:cash信用卡:card禮券:coupon**/			
//payType(payType)

function functionInitial(){ 
	var branchCode = vat.bean("branchCode");	
	vat.block.create(vnB_function, {
	id: "vatFunctionDiv", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"快捷鍵",rows:[
			{row_style:"", cols:[//(category (1:折扣 2:折讓),discountType("SINGEL") ,executeWay ("AUTO","MANUAL"))
				{items:[{name:"#L.discount", 				type:"LABEL" , 	value:"折扣折讓(小計前)"}]},
				{items:[{name:"#B.discount"      , type:"IMG"    ,value:"單筆折讓",   src:"./images/departmentPos/discount01.png", eClick:'doDiscount(2,"SINGEL","AUTO")'}]},
				{items:[{name:"#B.qty"      , type:"IMG"    ,value:"數量",   src:"./images/departmentPos/quantity.png", eClick:'detailModify("quantity",1)'}],td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.payment"    , type:"LABEL"  ,value:"付款"}]},
				{items:[{name:"#B.cash"    , type:"IMG"    ,value:"現金",   src:"./images/departmentPos/cash.png", eClick:'payType("cash")'}]},
				{items:[{name:"#B.card"          , type:"IMG"    ,value:"信用卡",   src:"./images/departmentPos/card.png", eClick:'payType("card")'}]},
				{items:[{name:"#B.certificate"      , type:"IMG"    ,value:"禮券",   src:"./images/departmentPos/certificate.png", eClick:'payType("groupon")'}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.count", 				type:"LABEL" , 	value:"小計/總計/結帳"}]},
				{items:[{name:"#B.totalCount"      , type:"IMG"    ,value:"小計",   src:"./images/departmentPos/total.png", eClick:'executeTotalCountPage()'}]},
				{items:[{name:"#B.cancelTotal"      , type:"IMG"    ,value:"取消小記",   src:"./images/departmentPos/cancelTotal.png", eClick:'cancelTotalCountPage()'}]},
				{items:[{name:"#B.salesBtn"      , type:"IMG"    ,value:"結帳",   src:"./images/departmentPos/sales.png", eClick:'doSubmit("FINISH")'}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.countToal", 				type:"LABEL" , 	value:"折扣折讓(ALL)"}]},
				{items:[{name:"#B.all_discount"      , type:"IMG"    ,value:"全部折扣",   src:"./images/departmentPos/all_discount.png", eClick:'doDiscount(1,"TOTAL","AUTO")'}]},
				{items:[{name:"#B.all_discount01"      , type:"IMG"    ,value:"全部折讓",   src:"./images/departmentPos/all_discount01.png", eClick:'doDiscount(2,"TOTAL","AUTO")'}]},
				{items:[{name:"#B.promotionFull"      , type:"IMG"    ,value:"滿額贈",   src:"./images/departmentPos/promotionFull.png", eClick:'openPromotionList()'}]}
			]}
		],
		beginService:"",
		closeService:""	
	});
}

function openPromotionList(){
	var headId = vat.item.getValueByName("#F.headId");
	var url = "/erp_dev20201030/promotionList.jsp?headId=" + headId;
	window.showModalDialog(url ,"滿額贈清單","dialogHeight:450px;dialogWidth:1100px;dialogTop:100px;dialogLeft:100px;");
    
}


function mzInitial(){ 
	var branchCode = vat.bean("branchCode");	
	vat.block.create(vnB_mz, {
	id: "vatMzDiv", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"離島額度區塊",rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.totalOriginalSalesAmount2", 	type:"LABEL" , 	value:"原銷售總額"}]},
				{items:[{name:"#F.totalOriginalSalesAmount2", 	type:"TEXT",  mode:"READONLY" , dec:0 ,	bind:"totalOriginalSalesAmount2"},
						{name:"#F.needAmount2", 	type:"TEXT",  mode:"READONLY" , dec:0 ,	bind:"needAmount2"}]},
						{items:[{name:"#L.balance2", 	type:"LABEL" , 	value:"<B>剩餘未付金額<B>"}]},
						{items:[{name:"#F.balance2", 	type:"TEXT" ,  mode:"READONLY" , dec:0}],td:" colSpan=3"}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.payment_cash2", 	type:"LABEL" , 	value:"現金"}]},
				{items:[{name:"#F.payment_cash2", 	type:"TEXT" ,  mode:"READONLY" ,	bind:"paymentCash2", dec:0}]},
				{items:[{name:"#L.payment_card2", 	type:"LABEL" , 	value:"信用卡"}]},
				{items:[{name:"#F.payment_card2", 	type:"TEXT",  mode:"READONLY" ,	bind:"paymentCard2", dec:0}]},
				{items:[{name:"#L.payment_groupon2", 	type:"LABEL" , 	value:"禮券"}]},
				{items:[{name:"#F.payment_groupon2", 	type:"TEXT",  mode:"READONLY" ,	bind:"paymentGroupon2", dec:0}]}	
			]}
		],
		beginService:"",
		closeService:""	
	});
}

/**小計**/
function totalInitial(){
    vat.block.create(vnB_Total, {
		id: "vatTotalDiv", table:"cellspacing='0' class='default' border='0' cellpadding='0'  height='100%'",	
			rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.totalOriginalSalesAmount", 	type:"LABEL" , 	value:"原銷售總額"}]},
				{items:[{name:"#F.totalOriginalSalesAmount", 	type:"TEXT",  mode:"READONLY" , dec:0 ,	bind:"totalOriginalSalesAmount"},
						{name:"#F.needAmount", 	type:"TEXT",  mode:"READONLY" , dec:0 ,	bind:"needAmount"}]},
						{items:[{name:"#L.balance", 	type:"LABEL" , 	value:"<B>剩餘未付金額<B>"}]},
						{items:[{name:"#F.balance", 	type:"TEXT" ,  mode:"READONLY" , dec:0}],td:" colSpan=3"}
		 	]},
			{row_style:"", cols:[
				{items:[{name:"#L.payment_cash", 	type:"LABEL" , 	value:"現金"}]},
				{items:[{name:"#F.payment_cash", 	type:"TEXT" ,  mode:"READONLY" ,	bind:"paymentCash", dec:0}]},
				{items:[{name:"#L.payment_card", 	type:"LABEL" , 	value:"信用卡"}]},
				{items:[{name:"#F.payment_card", 	type:"TEXT",  mode:"READONLY" ,	bind:"paymentCard", dec:0}]},
				{items:[{name:"#L.payment_groupon", 	type:"LABEL" , 	value:"禮券"}]},
				{items:[{name:"#F.payment_groupon", 	type:"TEXT",  mode:"READONLY" ,	bind:"paymentGroupon", dec:0}]}	
			]}
	 	 ],
			beginService:"",
			closeService:""//function(){closeTotal();}			
		});
}
/**面板選擇頁籤**/
function buttonLineTag(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_ButtonTag", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-left-width: 0px; border-color: #ffffff;' height='100%'",	
	title:"", rows:[  
		{row_style:"", cols:[
			{items : [{name:"#B.011"      	, type:"IMG" , value:vPos_Comment["011"],  src:"./images/departmentPos/sale_on.png",  eClick:vPos_Function["011"]},
					  {name:"#B.012"      	, type:"IMG" , value:vPos_Comment["012"],  src:"./images/departmentPos/search_off.png",  eClick:vPos_Function["012"]},
					  {name:"#B.021"      	, type:"IMG" , value:vPos_Comment["021"],  src:"./images/departmentPos/sale_off.png",  eClick:vPos_Function["021"]},
					  {name:"#B.022"      	, type:"IMG" , value:vPos_Comment["022"],  src:"./images/departmentPos/sale_off.png",  eClick:vPos_Function["022"]},
					  {name:"#B.031"      	, type:"IMG" , value:vPos_Comment["031"],  src:"./images/departmentPos/sale_off.png",  eClick:vPos_Function["031"]},
					  {name:"#B.032"      	, type:"IMG" , value:vPos_Comment["032"],  src:"./images/departmentPos/sale_off.png",  eClick:vPos_Function["032"]}]}	
		]},
		], 
		beginService:"",
		closeService:""				
	});
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

/**服務商品面板**/
function buttonLineZZ(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_ButtonZZ", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-left-width: 0px; border-color: #ffffff;' height='100%'",	
	title:"", rows:[
	  {row_style:"", cols:[
	 	{items:[
	 		{name:"#B.311"      	, type:"IMGPAY" , value:vPos_Comment["311"],  src:vPos_Img["311"],  eClick:vPos_Function["311"]},	 			
			{name:"#B.312"      	, type:"IMGPAY" , value:vPos_Comment["312"],  src:vPos_Img["312"],  eClick:vPos_Function["312"]},
			{name:"#B.313"      	, type:"IMGPAY" , value:vPos_Comment["313"],  src:vPos_Img["313"],  eClick:vPos_Function["313"]}
	 	],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	{row_style:"", cols:[
	 	{items:[
	 		{name:"#B.321"      	, type:"IMGPAY" , value:vPos_Comment["321"],  src:vPos_Img["321"],  eClick:vPos_Function["321"]},	 			
			{name:"#B.322"      	, type:"IMGPAY" , value:vPos_Comment["322"],  src:vPos_Img["322"],  eClick:vPos_Function["322"]},
			{name:"#B.323"      	, type:"IMGPAY" , value:vPos_Comment["323"],  src:vPos_Img["323"],  eClick:vPos_Function["323"]}
	 	],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	 			  
	{row_style:"", cols:[
		{items:[
			{name:"#B.331"      	, type:"IMGPAY" , value:vPos_Comment["331"],  src:vPos_Img["331"],  eClick:vPos_Function["331"]},	 			
			{name:"#B.332"      	, type:"IMGPAY" , value:vPos_Comment["332"],  src:vPos_Img["332"],  eClick:vPos_Function["332"]},
			{name:"#B.333"      	, type:"IMGPAY" , value:vPos_Comment["333"],  src:vPos_Img["333"],  eClick:vPos_Function["333"]}
		],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	{row_style:"", cols:[
		{items:[
			{name:"#B.341"      	, type:"IMGPAY" , value:vPos_Comment["341"],  src:vPos_Img["341"],  eClick:vPos_Function["341"]},	 			
			{name:"#B.342"      	, type:"IMGPAY" , value:vPos_Comment["342"],  src:vPos_Img["342"],  eClick:vPos_Function["342"]},
			{name:"#B.343"      	, type:"IMGPAY" , value:vPos_Comment["343"],  src:vPos_Img["343"],  eClick:vPos_Function["343"]}
		],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]}
	  ], 
		beginService:"",
		closeService:""			
	});
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}
/**主面板**/
function buttonLine(){
 	var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-left-width: 0px; border-color: #ffffff;' height='100%'",	
	title:"", rows:[  
	
	{row_style:"", cols:[
	 	{items:[
	 		{name:"#B.111"      	, type:"IMGPAY" , value:vPos_Comment["111"],  src:vPos_Img["111"],  eClick:vPos_Function["111"]},	 			
			{name:"#B.112"      	, type:"IMGPAY" , value:vPos_Comment["112"],  src:vPos_Img["112"],  eClick:vPos_Function["112"]},
			{name:"#B.113"      	, type:"IMGPAY" , value:vPos_Comment["113"],  src:vPos_Img["113"],  eClick:vPos_Function["113"]}
	 	],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	{row_style:"", cols:[
	 	{items:[
	 		{name:"#B.121"      	, type:"IMGPAY" , value:vPos_Comment["121"],  src:vPos_Img["121"],  eClick:vPos_Function["121"]},	 			
			{name:"#B.122"      	, type:"IMGPAY" , value:vPos_Comment["122"],  src:vPos_Img["122"],  eClick:vPos_Function["122"]},
			{name:"#B.123"      	, type:"IMGPAY" , value:vPos_Comment["123"],  src:vPos_Img["123"],  eClick:vPos_Function["123"]}
	 	],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	 			  
	{row_style:"", cols:[
		{items:[
			{name:"#B.131"      	, type:"IMGPAY" , value:vPos_Comment["131"],  src:vPos_Img["131"],  eClick:vPos_Function["131"]},	 			
			{name:"#B.132"      	, type:"IMGPAY" , value:vPos_Comment["132"],  src:vPos_Img["132"],  eClick:vPos_Function["132"]},
			{name:"#B.133"      	, type:"IMGPAY" , value:vPos_Comment["133"],  src:vPos_Img["133"],  eClick:vPos_Function["133"]}
		],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	{row_style:"", cols:[
		{items:[
			{name:"#B.141"      	, type:"IMGPAY" , value:vPos_Comment["141"],  src:vPos_Img["141"],  eClick:vPos_Function["141"]},	 			
			{name:"#B.142"      	, type:"IMGPAY" , value:vPos_Comment["142"],  src:vPos_Img["142"],  eClick:vPos_Function["142"]},
			{name:"#B.143"      	, type:"IMGPAY" , value:vPos_Comment["143"],  src:vPos_Img["143"],  eClick:vPos_Function["143"]}
		],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}
/**主面板2**/
function buttonLineMain2(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_ButtonMain2", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 0px; border-left-width: 0px; border-color: #ffffff;' height='100%'",	
	title:"", rows:[
	  {row_style:"", cols:[
	 	{items:[
	 		{name:"#B.211"      	, type:"IMGPAY" , value:vPos_Comment["211"],  src:vPos_Img["211"],  eClick:vPos_Function["211"]},	 			
			{name:"#B.212"      	, type:"IMGPAY" , value:vPos_Comment["212"],  src:vPos_Img["212"],  eClick:vPos_Function["212"]},
			{name:"#B.213"      	, type:"IMGPAY" , value:vPos_Comment["213"],  src:vPos_Img["213"],  eClick:vPos_Function["213"]}
	 	],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	{row_style:"", cols:[
	 	{items:[
	 		{name:"#B.221"      	, type:"IMGPAY" , value:vPos_Comment["221"],  src:vPos_Img["221"],  eClick:vPos_Function["221"]},	 			
			{name:"#B.222"      	, type:"IMGPAY" , value:vPos_Comment["222"],  src:vPos_Img["222"],  eClick:vPos_Function["222"]},
			{name:"#B.223"      	, type:"IMGPAY" , value:vPos_Comment["223"],  src:vPos_Img["223"],  eClick:vPos_Function["223"]}
	 	],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	 			  
	{row_style:"", cols:[
		{items:[
			{name:"#B.231"      	, type:"IMGPAY" , value:vPos_Comment["231"],  src:vPos_Img["231"],  eClick:vPos_Function["231"]},	 			
			{name:"#B.232"      	, type:"IMGPAY" , value:vPos_Comment["232"],  src:vPos_Img["232"],  eClick:vPos_Function["232"]},
			{name:"#B.233"      	, type:"IMGPAY" , value:vPos_Comment["233"],  src:vPos_Img["233"],  eClick:vPos_Function["233"]}
		],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]},
	{row_style:"", cols:[
		{items:[
			{name:"#B.241"      	, type:"IMGPAY" , value:vPos_Comment["241"],  src:vPos_Img["241"],  eClick:vPos_Function["241"]},	 			
			{name:"#B.242"      	, type:"IMGPAY" , value:vPos_Comment["242"],  src:vPos_Img["242"],  eClick:vPos_Function["242"]},
			{name:"#B.243"      	, type:"IMGPAY" , value:vPos_Comment["243"],  src:vPos_Img["243"],  eClick:vPos_Function["243"]}
		],td:" background-color:#FFEE99; border-color:#FFEE99; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}

function detailInitial(){

	var varCanGridDelete = true;
	var varCanGridAppend = true;
	var varCanGridModify = true;
	
	// set column
    vat.item.make(vnB_Detail, "indexNo", 					{type:"IDX"  , size: 1, desc:"NO"});
    vat.item.make(vnB_Detail, "lineId", 					{type:"TEXT" , view: "fixed" , mode:"HIDDEN" , desc:"LineId"});
	vat.item.make(vnB_Detail, "itemCode", 					{type:"TEXT" , size:12, view: "fixed", alter:true, maxLen:20, desc:"品號", mode:"READONLY" ,eChange:"changeItemData()"});
	vat.item.make(vnB_Detail, "itemCName", 					{type:"TEXT" , size:5, view: "fixed",alter:true, maxLen:20, desc:"品名", mode:"READONLY"});
	vat.item.make(vnB_Detail, "originalUnitPrice", 			{type:"NUMM", size: 2, view: "", maxLen:12, desc:"原價", dec:0, mode:"READONLY"});
	vat.item.make(vnB_Detail, "discountRate", 				{type:"NUMM", size: 1, view: "", maxLen: 6, desc:"折扣率", dec:0,mode:"READONLY"});
	vat.item.make(vnB_Detail, "quantity", 					{type:"NUMM", size: 1, view: "", maxLen: 8, desc:"數量", dec:0,mode:"READONLY"});
	vat.item.make(vnB_Detail, "originalSalesAmount", 		{type:"NUMM", size: 3, view: "fixed", maxLen:20, mode:"HIDDEN", desc:"金額", dec:0});
	vat.item.make(vnB_Detail, "actualSalesAmount", 			{type:"NUMM", size: 3, view: "", maxLen:20, desc:"實際售價", dec:0, mode:"READONLY"});
	vat.item.make(vnB_Detail, "discountAmount", 			{type:"NUMM", size: 3, view: "", mode:"HIDDEN", maxLen: 8, desc:"折讓金額", dec:0});
	vat.item.make(vnB_Detail, "isLockRecord", 				{type:"CHECK", desc:"鎖定", mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "isDeleteRecord", 			{type:"DEL", desc:"刪除",mode:"HIDDEN"});
	vat.item.make(vnB_Detail, "reserve1", 					{type:"TEXT", size: 3,desc:"備註",mode:"READONLY"});
	vat.item.make(vnB_Detail, "reserve2", 					{type:"TEXT", size: 1, desc:"選取",mode:"READONLY"});

	vat.block.pageLayout(vnB_Detail, {
								id: "vatDetailDiv", 
								pageSize: 10,			
	                            canGridDelete:varCanGridDelete,
								canGridAppend:varCanGridAppend,
								canGridModify:varCanGridModify,
							    appendBeforeService : "",
							    appendAfterService  : "",
							    //indexType	: "AUTO",
								loadBeforeAjxService: "loadBeforeAjxService()",
								loadSuccessAfter    : "loadSuccessAfter()",
								eventService        : "", 
								saveBeforeAjxService: "saveBeforeAjxService()",
								saveSuccessAfter    : "",
								cursor:"Y"
								});
	vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = 1);
}

function saveSuccessAfter(){
	alert("countTotal success");
}

/**商品資訊連動**/
function changeItemData(){
var nItemLine = vat.item.getGridLine();
var vItemCode = vat.item.getGridValueByName("itemCode"	, nItemLine).replace(/^\s+|\s+$/, '').toUpperCase();
vat.ajax.XHRequest(
               {
                   post:"process_object_name=soDepartmentOrderService" +
                        "&process_object_method_name=getAJAXItemInfoData" +
                        "&totaleActualSalesAmount="  + vat.item.getValueByName("#F.totalOriginalSalesAmount") +
                        "&itemCode="  + vItemCode +
                        "&brandCode=" + vat.item.getValueByName("#F.brandCode") +
                        "&shopCode=" + vat.item.getValueByName("#F.shopCode") +
                        "&nItemLine=" + nItemLine,
					find: function changeItemDataRequestSuccess(oXHR)
					{
						
						vat.item.setGridValueByName("itemCode", nItemLine, vat.ajax.getValue("itemCode", oXHR.responseText));
						vat.item.setGridValueByName("itemCName", nItemLine, vat.ajax.getValue("itemCName", oXHR.responseText));
						if(vItemCode.indexOf("ZZ")<=-1){
							vat.item.setGridValueByName("quantity", nItemLine, vat.ajax.getValue("quantity", oXHR.responseText)); 
							vat.item.setGridValueByName("originalUnitPrice", nItemLine, vat.ajax.getValue("originalUnitPrice", oXHR.responseText)); 
							vat.item.setGridValueByName("actualSalesAmount", nItemLine, vat.ajax.getValue("actualSalesAmount", oXHR.responseText));
						} 	
						vat.item.setGridValueByName("discountRate", nItemLine, vat.ajax.getValue("discountRate", oXHR.responseText)); 	
						vat.item.setValueByName("#F.totalOriginalSalesAmount" , vat.ajax.getValue("totaleActualSalesAmount", oXHR.responseText)); 		
                   }
               });     
}
/****函數宣告****/

//送出後返回
function createNewForm(){
	var headId = vat.item.getValueByName("#F.headId");
		if(submitType === 'production'){

			importPrint(headId,2,'SUBMIT');
		}
		else if(submitType === 'test'){

			submitType = 'production';
			importPrint1(headId,2,'SUBMIT');
		}
		else{
			alert("列印時發生錯誤，錯誤原因:列印參數錯誤:submitType="+submitType);
		}

	
	

	//openLoginPos();
}
// 明細載入
function loadBeforeAjxService(){
	var processString = "process_object_name=soDepartmentOrderService&process_object_method_name=getAJAXPageData" +
	  					"&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	                    "&headId=" + vat.item.getValueByName("#F.headId");
	return processString;
}
// 更新總額
function loadSuccessAfter(){
	vat.ajax.XHRequest(
    {
		post:"process_object_name=soDepartmentOrderService"+
                    "&process_object_method_name=findTotalOriginalSalesAmount"+
					"&brandCode=T2" +
	                "&headId=" + vat.item.getValueByName("#F.headId"),
		find: function checkItemCode(oXHR){
    		vat.item.setValueByName("#F.totalOriginalSalesAmount" , vat.ajax.getValue("totaleActualSalesAmount", oXHR.responseText)); 	
    		if(changePage){
				insertNewItem();
			}
    	}   
	});
	
	if(isInsert==="insert"){
		isInsert="";
		insertNewDataByBar();
	}
	
}

// 更新payment
function savePayment(){
	vat.ajax.XHRequest(
    {
		post:"process_object_name=soDepartmentOrderService"+
                    "&process_object_method_name=updatePayment"+
					"&brandCode=T2" +
	                "&headId=" + vat.item.getValueByName("#F.headId"),
		find: function checkItemCode(oXHR){
    		/*vat.item.setValueByName("#F.totalOriginalSalesAmount" , vat.ajax.getValue("totaleActualSalesAmount", oXHR.responseText)); 	
    		if(changePage){
				insertNewItem();
			}*/
			alert("更新完成");
    	}   
	});
	
}
/**頁面切換**/
function executeChangePage(type){
	if(type==='add'){

		changePage = true;
		vat.block.pageAdd(vnB_Detail);
		ptr = 1;
	}/*
	else if(){
		changePage = true;
		vat.block.pageBackward(vnB_Detail);
				ptr = 1;
	}
	else if(type==='pageForward'){
	vat.block.pageForward(vnB_Detail);
	
	}else*/
}
/**插入新商品**/
function insertNewItem(){
				var itemCode = vat.item.getValueByName("#F.posBroCode");
				var edValue = document.getElementById("#F.posBroCode");
			    var s = edValue.value;
				var headId = vat.item.getValueByName("#F.headId");
				var itemId = vat.item.nameMake("itemCode", ptr);
				var ptr_Qty = ptr;
				var voPageThere = document.getElementById(vat.block.pageThereIdMake(vnB_Detail));
				var OnChange_Qty = document.getElementById(itemId);
				vat.item.setGridValueByName("indexNo",ptr_Qty,((voPageThere.value-1)*10+ptr_Qty));
				vat.item.setGridValueByName("itemCode",ptr_Qty,itemCode);
				vat.item.setGridValueByName("quantity",ptr,1.0);
				OnChange_Qty.fireEvent('onchange');
				if(isZZ == true){
					vat.item.setGridValueByName("originalUnitPrice",ptr,amount);
					vat.item.setGridValueByName("quantity",ptr,count);
					vat.item.setGridValueByName("actualSalesAmount",ptr,amount*count);
					vat.item.setGridValueByName("discountRate",ptr,100);
					if(allDiscount===""){
						//alert("BS");
					}else{
						vat.item.setValueByName("#F.posBroCode",allDiscount);
						doDiscount("1","TOTAL","AUTO");
					}
					vat.block.pageDataSave(vnB_Detail);
					amount = 0;
					count = 0;
					isZZ = false;
					allDiscount = "";
				}
				
				vat.item.setValueByName("#F.posBroCode","");
				ptr++;
				oldPtr = ptr;
				changePage = false;
}
//明細存檔
function saveBeforeAjxService() {
	processString = "process_object_name=soDepartmentOrderService"+
					"&process_object_method_name=updateAJAXPageLinesData" + 
					"&headId=" + vat.item.getValueByName("#F.headId") + 
					"&status=" + vat.item.getValueByName("#F.status");
	return processString;
}

/*功能鍵面板顯示控制*/
function controlPanel(panelType){

	document.getElementById("mainMenu").style.display = "none";
	document.getElementById("zzMenu").style.display = "none";
	document.getElementById("mainMenu2").style.display = "none";

	if(panelType==='zz'){
    	document.getElementById("zzMenu").style.display = "";
    }
    else if(panelType==='main2'){
   		document.getElementById("mainMenu2").style.display = "";
    }
    else{
 	    document.getElementById("mainMenu").style.display = "";
	}
}

vat.form.keydown = function(){
	var i, ret = false;
	var nowPage;
	if (event.altKey){
		event.returnValue = false;	// disable ALT key
		vat.debug("user", "請不要按下 alt 鍵");
	}else{
		switch(event.keyCode){
		case 13://ENTER
			if(stepConfig("isExecute",0,1))
			{
				if(stepConfig("isChange",1,1)){
					nowStep=1;
				}
				/*
				if(document.getElementById(vat.block.pageThereIdMake(vnB_Detail)).value != lastPage)
				{
					isInsert = "insert";
					vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = lastPage);
					
				}*/
				//else{
					insertNewDataByBar();
				//}
			


	
		}
		else{
			alert(itemInsert+stepErrorMsg);
		}

		break;
		
		/*	
		case 8:	//BackSpace
			if (typeof event.srcElement.id !== "string")
				event.returnValue = false;
			break;
		case 38://Up
			event.returnValue = false;
			if (typeof vat.formD != 'undefined'){
				vat.formD.lineMove(event.srcElement.id, -1);
			}
			break;
		case 40://Down
			event.returnValue = false;
			if (typeof vat.formD != 'undefined'){
				vat.formD.lineMove(event.srcElement.id, +1);			
			}	
			break;						
		case 33://PageUp
			event.returnValue = false;
			if (typeof vat.formD != 'undefined'){		
				vat.formD.pageBackward();
			}	
			break;						
		case 34://PageDown
			event.returnValue = false;
			if (typeof vat.formD != 'undefined'){			
				vat.formD.pageForward();
			}	
			break;

		case 81://Q
			var quantity = vat.item.nameMake("quantity", ptr-1);			
			var itemCode = vat.item.getValueByName("#F.posBroCode");
			var ptr_Qty = ptr-1;
			var OnChange_Qty = document.getElementById(quantity);
			vat.item.setGridValueByName("quantity",ptr_Qty,itemCode);
			resetBarCode();
			OnChange_Qty.fireEvent('onchange');
			break;
		case 65://A
			//alert("小計");
			showTotalCountPage();
			resetForm();
			break;
			
		case 90://Z
			//alert("結帳");
			doSubmit("SIGNING");
			resetForm();
			break;
		
		case 68://D
			
			var quantity = vat.item.nameMake("discountRate", ptr-1);
			var itemCode = vat.item.getValueByName("#F.posBroCode");
			var ptr_Qty = ptr-1;
			var OnChange_Qty = document.getElementById(quantity);
			vat.item.setGridValueByName("discountRate",ptr_Qty,itemCode);
			OnChange_Qty.fireEvent('onchange');
			resetBarCode();
			break;
		*/	
		}
	}
}


/**尋找商品**/
function findItem(){
	findItemId(ptr);
	
	var itemCode = vat.item.getValueByName("#F.posBroCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var brandCode = "T2";
	
	vat.ajax.XHRequest(
       	{
			post:"process_object_name=soDepartmentOrderService"+
                    "&process_object_method_name=checkItem"+
                    "&brandCode=" + brandCode + 
                    "&itemCode=" + itemCode,
			find: function checkItemCode(oXHR){
           		vCheckItemcode = vat.ajax.getValue("imItemPO", oXHR.responseText);
           		checkItem(vCheckItemcode);
           	}   
		});
		
}

/**商品指標**/
function findItemId(ptr){

	var itemId = vat.item.nameMake("itemCode", ptr);
	var quantity = vat.item.nameMake("quantity", ptr);
	ptrItemName = itemId;
	ptrQty = quantity;
}

/**商品輸入檢核**/
function checkItem(vItem){
	if(vItem===""){
		alert("查無商品");
		vat.item.setValueByName("#F.posBroCode","");
	}else{
		//alert(vItem);
		ptr = oldPtr;
		if(ptr>1){
			if(vat.item.getGridValueByName("itemCode",ptr-1)===""){
				ptr = ptr-1;
			}
		}	

		if(ptr>10){
			var itemCNameValue = vat.item.getGridValueByName("itemCName",ptr-1);
			if(itemCNameValue===""){
				alert("系統忙碌中，請重新操作");
				vat.item.setValueByName("#F.posBroCode","");
				return;
			}
			executeChangePage('add');
			lastPage++;
		}else if(document.getElementById(vat.block.pageThereIdMake(vnB_Detail)).value != lastPage){
			//isInsert = "insert";
			changePage = true;
			vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = lastPage);
		}		
		if(!changePage){
			insertNewItem();
		}

	}	
}
/**鎖定pos bar輸入**/
function focusPosBar(){
	var posBarCode = document.getElementById("#F.posBroCode");
	window.setTimeout(function(){posBarCode.focus();},300);
	posBarCode.focus();
}



function insertNewDataByBar(){
				var ret = false;
				findItem();
				event.returnValue = false;
				if (event.shiftKey){
					vat.form.item.move(-1);
					event.returnValue = ret;
				}
				else{
					if (vat.form.item.result.length > 0 && (vat.form.item.list instanceof Array && vat.form.item.list.length > 0)){
						for (i=0; i<vat.form.item.result.length; i++){
							if (typeof HTML_backgroundColor === "string"){
							}
							if (vat.form.item.list[vat.form.item.currentIndex].name === vat.form.item.result[i]){
								ret = true;
							}
						}
					}
					for (i=0; i<vat.form.item.list.length; i++){
						if (typeof vat.form.item.list[i].style.backgroundColor === "string"){
							if (typeof vat.form.item.list[i].datatype === "string" && vat.form.item.list[i].datatype !== "DATE"){
								if (vat.form.item.list[i].readOnly && typeof vat.form.item.list[i].HTML_backgroundColor === "string") 
										vat.form.item.list[i].style.backgroundColor = vat.form.item.list[i].HTML_backgroundColor;  
								else 
									vat.form.item.list[i].style.removeAttribute("backgroundColor");
							}
						}	
					}
					if (ret){
						//** currentIndex + 1, 是因為 ceap 到最後再按 enter 會加一行(submit), 所以先加一再存起來, reload 後就會到下一個欄位
						vat.message.bind.setValue("current.item", vat.form.item.currentIndex + 1);
					}else{
						vat.form.item.move(+1);
				}
				event.returnValue = ret;
			}	
}
//確認員工資訊
function eChangeEmployee() {
    vat.item.setValueByName("#F.superintendentCode", vat.item.getValueByName("#F.superintendentCode").replace(/^\s+|\s+$/, ''));
    if(vat.item.getValueByName("#F.superintendentCode") !== ""){
		vat.ajax.XHRequest(
       	{
			post:"process_object_name=buEmployeeWithAddressViewService"+
                    "&process_object_method_name=findbyBrandCodeAndEmployeeCodeForAJAX"+
                    "&brandCode=" + document.forms[0]["#loginBrandCode" ].value + 
                    "&employeeCode=" + vat.item.getValueByName("#F.superintendentCode"),
			find: function changeSuperintendentRequestSuccess(oXHR){
           		if(vat.ajax.getValue("EmployeeName", oXHR.responseText)!= "查無此員工資料" && vat.ajax.getValue("EmployeeName", oXHR.responseText)!=""){
					vat.item.setValueByName("#F.superintendentCode", vat.ajax.getValue("EmployeeCode", oXHR.responseText));
           			vat.item.setValueByName("#F.salesEmployeeName", vat.ajax.getValue("EmployeeName", oXHR.responseText));
				}
				else{
					vat.item.setValueByName("#F.superintendentCode", "");
					vat.item.setValueByName("#F.salesEmployeeName", "");
				}
			}   
		});
	}else{
        vat.item.setValueByName("#F.salesEmployeeName", "");
        
	}
}



function importPrint1(headId,times,vCategory){
	var brandCode = document.forms[0]["#loginBrandCode"].value;
	//var headId = vat.item.getValueByName("#F.headId");
			 
	vat.ajax.XHRequest(
	{
        post:"process_object_name=soDepartmentOrderService" +
             "&process_object_method_name=importPrint" +
             "&brandCode=" + brandCode +
             "&headId=" + headId ,
		find: function posPrint(oXHR){
				var lResult;
				var COM = vat.ajax.getValue("ComNum", oXHR.responseText);

			//變數	
				var PrinterId		= vat.ajax.getValue("PrinterId", oXHR.responseText);
				var Company			= vat.ajax.getValue("CompanyName", oXHR.responseText) + "\n";
				var Title			= vat.ajax.getValue("Title", oXHR.responseText) + "\n";
				var ShopName		= vat.ajax.getValue("ShopName", oXHR.responseText) + "\n";
				var Address 		= vat.ajax.getValue("CompanyAddress", oXHR.responseText) + "\n";
				var CompanyId 		= "統編: " + vat.ajax.getValue("CompanyID", oXHR.responseText) + "\n";
				var CompanyTel		= "電話: " + vat.ajax.getValue("CompanyTel", oXHR.responseText) + "\n";
				var SaleDateTime	= vat.ajax.getValue("SaleDateTime", oXHR.responseText) + "\n";
				var Page 			= vat.ajax.getValue("Pg", oXHR.responseText);
				var Ty 				= vat.ajax.getValue("TY", oXHR.responseText);
				var CashierId 		= vat.ajax.getValue("CashierID", oXHR.responseText);
				var SalerId 		= vat.ajax.getValue("SalerID", oXHR.responseText);
				var SubTotal 		= vat.ajax.getValue("Sum", oXHR.responseText);
				var ReceivedMoney 	= vat.ajax.getValue("ReceivedMoney", oXHR.responseText);
				var CreditCard 		= vat.ajax.getValue("CreditCard", oXHR.responseText);
				var Voucher 		= vat.ajax.getValue("Voucher", oXHR.responseText);	
				var TotalSum 		= vat.ajax.getValue("TotalSum", oXHR.responseText);
				var Void 			= vat.ajax.getValue("Void", oXHR.responseText);
				
				
				var ItemId 					= vat.ajax.getValue("ItemID", oXHR.responseText);
				var ItemIdArray 			= ItemId.split(",");
				var Quantity 				= vat.ajax.getValue("Quantity", oXHR.responseText);
				var QuantityArray 			= Quantity.split(",");
				var UnitPrice 				= vat.ajax.getValue("UnitPrice", oXHR.responseText);
				var UnitPriceArray 			= UnitPrice.split(",");
				var ItemPrice 				= vat.ajax.getValue("ItemPrices", oXHR.responseText);
				var ItemPriceArray 			= ItemPrice.split(",");
				var Categories 				= vat.ajax.getValue("Categories", oXHR.responseText);
				var CategoriesArray			= Categories.split(",");
				var ItemName 				= vat.ajax.getValue("ItemNames", oXHR.responseText);
				var ItemNameArray 			= ItemName.split(",");
				var Discount  				= vat.ajax.getValue("Discount", oXHR.responseText);
				var DiscountArray  			= Discount.split(",");
				var DiscountPrice 			= vat.ajax.getValue("DiscountPrice", oXHR.responseText);	
				var DiscountPriceArray  	= DiscountPrice.split(",");
				var Remark 					= vat.ajax.getValue("Remark", oXHR.responseText);
				var RemarkArray 			= Remark.split(",");
				
				//alert('印單測試');
				lResult = document.getElementById("clsInvoice").OpenPrint(PrinterId, "0000");
			//主體
				//var repeatTimes = 2;
				var times = 2;
				var c = 0;
				var p = document.getElementById("clsInvoice").Initialize();
				var devideLine = "=============================================";

					for(c=0;c<times;c++){
					
					var printTxt = "";
					var printTxtHead = "";
					printTxtHead = printTxtHead + Company+ ShopName+ Address+ CompanyId+ Title+ CompanyTel+ SaleDateTime;
					//document.getElementById("clsInvoice").PrintTextUseEncoding("文字", 0, 0, 0,"Big5");
					document.getElementById("clsInvoice").Align(1);
					document.getElementById("clsInvoice").PrintTextUseEncoding("\n", 0, 0, 0,"Big5");
					document.getElementById("clsInvoice").PrintTextUseEncoding(printTxtHead, 0, 0, 0,"Big5");
					//LKPOSALL.PrintText("\n", 1, 0, 0);
					//LKPOSALL.PrintText(printTxtHead, 1, 0, 0);
/*					LKPOSALL.PrintText("\n", 1, 0, 0);
					LKPOSALL.PrintText(Company, 1, 0, 0);
					LKPOSALL.PrintText(Title, 1, 0, 0);
					LKPOSALL.PrintText(Address, 1, 0, 0);
					LKPOSALL.PrintText(CompanyId, 1, 0, 0);
					LKPOSALL.PrintText(CompanyTel, 1, 0, 0);
					LKPOSALL.PrintText(SaleDateTime, 1, 0, 0);*/
					var TyString = "TY: " + Ty;
//排版
					while (TyString.length < 40 - Page.length)
					{
						TyString = " " + TyString;
					}
					var PageTy = "VIP: " + Page + TyString + "\n";
					printTxt = printTxt + PageTy ;
					//LKPOSALL.PrintText(PageTy, 0, 0, 0);
					var SalerString = "SALES: " + SalerId;
					
//排版
					while(SalerString.length < 37 - CashierId.length)
					{
						SalerString = " " + SalerString;
					}
					var CashierSales = "SALENO: " + CashierId + SalerString + "\n";
					printTxt = printTxt + CashierSales + devideLine;
					//LKPOSALL.PrintText(CashierSales, 0, 0, 0);
					//LKPOSALL.PrintText("=============================================", 0, 0, 0);
					
				//明細
					for (i=0; i<ItemIdArray.length; i++)
					{
//排版
						while(UnitPriceArray[i].length< 23 - ItemIdArray[i].length)
						{
							UnitPriceArray[i] = " "+UnitPriceArray[i];
						}
//排版
						while(QuantityArray[i].length< 9)
						{
							QuantityArray[i] = " "+QuantityArray[i];
						}
//排版
						while(ItemPriceArray[i].length< 21-QuantityArray[i].length)
						{
							ItemPriceArray[i] = " "+ItemPriceArray[i];
						}		
						var ItemIdString = "\n" + ItemIdArray[i] + UnitPriceArray[i] + QuantityArray[i] + ItemPriceArray[i] + "T\n";
						printTxt = printTxt + ItemIdString;
						//LKPOSALL.PrintText(ItemIdString, 0, 0, 0);
				
						if (DiscountArray.length > 0)
						{
							if (DiscountArray[i].length > 0)
							{
								var DiscountString = " Discount=(" + DiscountArray[i] + "% OFF)";
//排版
								while(DiscountPriceArray[i].length < 27-DiscountArray[i].length)
								{
									DiscountPriceArray[i] = " "+DiscountPriceArray[i];
								}
								var DiscoutPriceString = DiscountString + DiscountPriceArray[i] + "T\n";
								printTxt = printTxt + DiscoutPriceString;
								//LKPOSALL.PrintText(DiscoutPriceString, 0, 0, 0);
							}
						}
//排版
						while (CategoriesArray[i].length < 8)
						{
							CategoriesArray[i] = CategoriesArray[i] + " ";
						}
						var CategoryTemp = "(" + CategoriesArray[i] + ")" + ItemNameArray[i];	
//排版	
						while (CategoryTemp.length < 34)
						{
							CategoryTemp = CategoryTemp + " ";
						}		
						var CategoryString = CategoryTemp + "\n";
						printTxt = printTxt + CategoryString;
						//LKPOSALL.PrintText(CategoryString, 0, 0, 0);
				
						if (Remark[i] !== null)
						{
							if (RemarkArray[i].length > 0)
							{
//排版
								while(RemarkArray[i].length < 44)
								{
									RemarkArray[i] = RemarkArray[i] + " ";
								}
								var RemarkString = RemarkArray[i] + "\n";
								printTxt = printTxt + RemarkString;
								//LKPOSALL.PrintText(RemarkString, 0, 0, 0);				
							}
						}		
					}
					printTxt = printTxt + devideLine + "\n";
					//LKPOSALL.PrintText("=============================================\n", 0, 0, 0);
//排版
					while (SubTotal.length < 36)
					{
						SubTotal = " " + SubTotal;
					}
					var SubTotalString = "小  計 = " + SubTotal + "\n";
					printTxt = printTxt + SubTotalString + devideLine+"\n"+"付費方式：\n";
					//LKPOSALL.PrintText(SubTotalString, 0, 0, 0);
					//LKPOSALL.PrintText("=============================================\n付費方式：\n", 0, 0, 0);
					if (ReceivedMoney !== "")
					{
//排版
						while(ReceivedMoney.length < 34)
						{
							ReceivedMoney = " " + ReceivedMoney;
						}
						var ReceivedMoneyString = "  現  金   " + ReceivedMoney + "\n";
						printTxt = printTxt + ReceivedMoneyString;
						//LKPOSALL.PrintText(ReceivedMoneyString, 0, 0, 0);
					}
					if (CreditCard !== "")
					{
//排版
						while(CreditCard.length < 34)
						{
							CreditCard = " " + CreditCard;
						}
						var CreditCardString = "  信用卡   " + CreditCard + "\n";
						printTxt = printTxt + CreditCardString;
						//LKPOSALL.PrintText(CreditCardString, 0, 0, 0);
					}
					if (Voucher !== "")
					{
//排版
						while(Voucher.length < 34)
						{
							Voucher = " " + Voucher;
						}
						var VoucherString = "  禮  卷   " + Voucher + "\n";
						printTxt = printTxt + VoucherString;
						//LKPOSALL.PrintText(VoucherString, 0, 0, 0);
					}
//排版
					while(TotalSum.length < 36)
					{
						TotalSum = " " + TotalSum; 
					}
					var TotalSumString = "合  計 = " + TotalSum + "\n";
					printTxt = printTxt + TotalSumString + "\n\n\n\n";
					//LKPOSALL.PrintText(TotalSumString, 0, 0, 0);	
					//LKPOSALL.PrintText("\n\n", 0, 0, 0);

					document.getElementById("clsInvoice").Align(0);
					document.getElementById("clsInvoice").PrintTextUseEncoding(printTxt, 0, 0, 0,"Big5");
					//LKPOSALL.PrintText(printTxt, 0, 0, 0);
				}

				document.getElementById("clsInvoice").Feed(50);
				//alert(document.getElementById("clsInvoice").ClosePrint());
				//alert("列印完成!");
				if(document.getElementById("clsInvoice").ClosePrint()== true){
					if(vCategory==='SUBMIT'){
						window.setTimeout(function(){openLoginPos();},1500);	
					}
					else{
						vat.item.setValueByName("#F.posBroCode","");
					}
				}
				
	    }
	});       
}

function importPrint(headId,times,vCategory){
	var brandCode = "T1BS";
	//var headId = vat.item.getValueByName("#F.headId");
			 
	vat.ajax.XHRequest(
	{
        post:"process_object_name=soDepartmentOrderService" +
             "&process_object_method_name=importPrint" +
             "&brandCode=" + brandCode +
             "&headId=" + headId ,
		find: function posPrint(oXHR){
				var lResult;
				var COM = vat.ajax.getValue("ComNum", oXHR.responseText);

			//變數	
				var Company			= vat.ajax.getValue("CompanyName", oXHR.responseText) + "\n";
				var Title			= vat.ajax.getValue("Title", oXHR.responseText) + "\n";
				var ShopName		= vat.ajax.getValue("ShopName", oXHR.responseText) + "\n";
				var Address 		= vat.ajax.getValue("CompanyAddress", oXHR.responseText) + "\n";
				var CompanyId 		= "統編: " + vat.ajax.getValue("CompanyID", oXHR.responseText) + "\n";
				var CompanyTel		= "電話: " + vat.ajax.getValue("CompanyTel", oXHR.responseText) + "\n";
				var SaleDateTime	= vat.ajax.getValue("SaleDateTime", oXHR.responseText) + "\n";
				var Page 			= vat.ajax.getValue("Pg", oXHR.responseText);
				var Ty 				= vat.ajax.getValue("TY", oXHR.responseText);
				var CashierId 		= vat.ajax.getValue("CashierID", oXHR.responseText);
				var SalerId 		= vat.ajax.getValue("SalerID", oXHR.responseText);
				var SubTotal 		= vat.ajax.getValue("Sum", oXHR.responseText);
				var ReceivedMoney 	= vat.ajax.getValue("ReceivedMoney", oXHR.responseText);
				var CreditCard 		= vat.ajax.getValue("CreditCard", oXHR.responseText);
				var Voucher 		= vat.ajax.getValue("Voucher", oXHR.responseText);	
				var TotalSum 		= vat.ajax.getValue("TotalSum", oXHR.responseText);
				var Void 			= vat.ajax.getValue("Void", oXHR.responseText);
				
				
				var ItemId 					= vat.ajax.getValue("ItemID", oXHR.responseText);
				var ItemIdArray 			= ItemId.split(",");
				var Quantity 				= vat.ajax.getValue("Quantity", oXHR.responseText);
				var QuantityArray 			= Quantity.split(",");
				var UnitPrice 				= vat.ajax.getValue("UnitPrice", oXHR.responseText);
				var UnitPriceArray 			= UnitPrice.split(",");
				var ItemPrice 				= vat.ajax.getValue("ItemPrices", oXHR.responseText);
				var ItemPriceArray 			= ItemPrice.split(",");
				var Categories 				= vat.ajax.getValue("Categories", oXHR.responseText);
				var CategoriesArray			= Categories.split(",");
				var ItemName 				= vat.ajax.getValue("ItemNames", oXHR.responseText);
				var ItemNameArray 			= ItemName.split(",");
				var Discount  				= vat.ajax.getValue("Discount", oXHR.responseText);
				var DiscountArray  			= Discount.split(",");
				var DiscountPrice 			= vat.ajax.getValue("DiscountPrice", oXHR.responseText);	
				var DiscountPriceArray  	= DiscountPrice.split(",");
				var Remark 					= vat.ajax.getValue("Remark", oXHR.responseText);
				var RemarkArray 			= Remark.split(",");
				
				//alert('印單測試');
				lResult = LKPOSALL.OpenPort(COM, 115200);
			//主體
				//var repeatTimes = 2;
				var c = 0;
				var p = LKPOSALL.PrinterSts();
				var devideLine = "=============================================";

					for(c=0;c<times;c++){
					
					var printTxt = "";
					var printTxtHead = "";
					printTxtHead = printTxtHead + Company+ ShopName+ Address+ CompanyId+ Title+ CompanyTel+ SaleDateTime;
					LKPOSALL.PrintText("\n", 1, 0, 0);
					LKPOSALL.PrintText(printTxtHead, 1, 0, 0);
/*					LKPOSALL.PrintText("\n", 1, 0, 0);
					LKPOSALL.PrintText(Company, 1, 0, 0);
					LKPOSALL.PrintText(Title, 1, 0, 0);
					LKPOSALL.PrintText(Address, 1, 0, 0);
					LKPOSALL.PrintText(CompanyId, 1, 0, 0);
					LKPOSALL.PrintText(CompanyTel, 1, 0, 0);
					LKPOSALL.PrintText(SaleDateTime, 1, 0, 0);*/
					var TyString = "TY: " + Ty;
//排版
					while (TyString.length < 40 - Page.length)
					{
						TyString = " " + TyString;
					}
					var PageTy = "VIP: " + Page + TyString + "\n";
					printTxt = printTxt + PageTy ;
					//LKPOSALL.PrintText(PageTy, 0, 0, 0);
					var SalerString = "SALES: " + SalerId;
					
//排版
					while(SalerString.length < 37 - CashierId.length)
					{
						SalerString = " " + SalerString;
					}
					var CashierSales = "SALENO: " + CashierId + SalerString + "\n";
					printTxt = printTxt + CashierSales + devideLine;
					//LKPOSALL.PrintText(CashierSales, 0, 0, 0);
					//LKPOSALL.PrintText("=============================================", 0, 0, 0);
					
				//明細
					for (i=0; i<ItemIdArray.length; i++)
					{
//排版
						while(UnitPriceArray[i].length< 23 - ItemIdArray[i].length)
						{
							UnitPriceArray[i] = " "+UnitPriceArray[i];
						}
//排版
						while(QuantityArray[i].length< 9)
						{
							QuantityArray[i] = " "+QuantityArray[i];
						}
//排版
						while(ItemPriceArray[i].length< 21-QuantityArray[i].length)
						{
							ItemPriceArray[i] = " "+ItemPriceArray[i];
						}		
						var ItemIdString = "\n" + ItemIdArray[i] + UnitPriceArray[i] + QuantityArray[i] + ItemPriceArray[i] + "T\n";
						printTxt = printTxt + ItemIdString;
						//LKPOSALL.PrintText(ItemIdString, 0, 0, 0);
				
						if (DiscountArray.length > 0)
						{
							if (DiscountArray[i].length > 0)
							{
								var DiscountString = " Discount=(" + DiscountArray[i] + "% OFF)";
//排版
								while(DiscountPriceArray[i].length < 27-DiscountArray[i].length)
								{
									DiscountPriceArray[i] = " "+DiscountPriceArray[i];
								}
								var DiscoutPriceString = DiscountString + DiscountPriceArray[i] + "T\n";
								printTxt = printTxt + DiscoutPriceString;
								//LKPOSALL.PrintText(DiscoutPriceString, 0, 0, 0);
							}
						}
//排版
						while (CategoriesArray[i].length < 8)
						{
							CategoriesArray[i] = CategoriesArray[i] + " ";
						}
						var CategoryTemp = "(" + CategoriesArray[i] + ")" + ItemNameArray[i];	
//排版	
						while (CategoryTemp.length < 34)
						{
							CategoryTemp = CategoryTemp + " ";
						}		
						var CategoryString = CategoryTemp + "\n";
						printTxt = printTxt + CategoryString;
						//LKPOSALL.PrintText(CategoryString, 0, 0, 0);
				
						if (Remark[i] !== null)
						{
							if (RemarkArray[i].length > 0)
							{
//排版
								while(RemarkArray[i].length < 44)
								{
									RemarkArray[i] = RemarkArray[i] + " ";
								}
								var RemarkString = RemarkArray[i] + "\n";
								printTxt = printTxt + RemarkString;
								//LKPOSALL.PrintText(RemarkString, 0, 0, 0);				
							}
						}		
					}
					printTxt = printTxt + devideLine + "\n";
					//LKPOSALL.PrintText("=============================================\n", 0, 0, 0);
//排版
					while (SubTotal.length < 36)
					{
						SubTotal = " " + SubTotal;
					}
					var SubTotalString = "小  計 = " + SubTotal + "\n";
					printTxt = printTxt + SubTotalString + devideLine+"\n"+"付費方式：\n";
					//LKPOSALL.PrintText(SubTotalString, 0, 0, 0);
					//LKPOSALL.PrintText("=============================================\n付費方式：\n", 0, 0, 0);
					if (ReceivedMoney !== "")
					{
//排版
						while(ReceivedMoney.length < 34)
						{
							ReceivedMoney = " " + ReceivedMoney;
						}
						var ReceivedMoneyString = "  現  金   " + ReceivedMoney + "\n";
						printTxt = printTxt + ReceivedMoneyString;
						//LKPOSALL.PrintText(ReceivedMoneyString, 0, 0, 0);
					}
					if (CreditCard !== "")
					{
//排版
						while(CreditCard.length < 34)
						{
							CreditCard = " " + CreditCard;
						}
						var CreditCardString = "  信用卡   " + CreditCard + "\n";
						printTxt = printTxt + CreditCardString;
						//LKPOSALL.PrintText(CreditCardString, 0, 0, 0);
					}
					if (Voucher !== "")
					{
//排版
						while(Voucher.length < 34)
						{
							Voucher = " " + Voucher;
						}
						var VoucherString = "  禮  卷   " + Voucher + "\n";
						printTxt = printTxt + VoucherString;
						//LKPOSALL.PrintText(VoucherString, 0, 0, 0);
					}
//排版
					while(TotalSum.length < 36)
					{
						TotalSum = " " + TotalSum; 
					}
					var TotalSumString = "合  計 = " + TotalSum + "\n";
					printTxt = printTxt + TotalSumString + "\n\n\n\n";
					//LKPOSALL.PrintText(TotalSumString, 0, 0, 0);	
					//LKPOSALL.PrintText("\n\n", 0, 0, 0);

					
					LKPOSALL.PrintText(printTxt, 0, 0, 0);
				}

				
				p = LKPOSALL.OutputCompletePrinting(2500);
				//alert("列印完成!");
				if(LKPOSALL.ClosePort()===0){
					if(vCategory==='SUBMIT'){
						window.setTimeout(function(){openLoginPos();},1500);	
					}
					else{
						vat.item.setValueByName("#F.posBroCode","");
					}
				}
				
	    }
	});       
}

/**當日銷售**/
function calculateTodaySales1(){
	//alert('calculateTodaySales1');
	var brandCode = document.forms[0]["#loginBrandCode"].value;
	var shopCode = document.forms[0]["#loginEmployeeCode"].value;


	vat.ajax.XHRequest(
       	{
			post:"process_object_name=soDepartmentOrderService"+
                    "&process_object_method_name=findTodaySales"+
                    "&brandCode=" + brandCode + 
                    "&shopCode=" + shopCode,
			find: function checkItemCode(oXHR){
			
			//顯示結果

				//if(document.getElementById("clsInvoice").ClosePrint()===true){
					alert("[本日銷售]   總筆數:"+vat.ajax.getValue("totalCount", oXHR.responseText)+"    總金額:"+vat.ajax.getValue("totalAmount", oXHR.responseText));
//資料準備
				if(confirm("是否列印當日銷售?"))
				{
           		var COM = vat.ajax.getValue("ComNum", oXHR.responseText);
           		var printTxt = "";
				var printTxtHead = "";
				var devideLine = "=============================================";
				var PrinterId		= vat.ajax.getValue("PrinterId", oXHR.responseText);
				//var p = LKPOSALL.PrinterSts();

				var lResult = document.getElementById("clsInvoice").OpenPrint(PrinterId, "0000");
				
           		var Company			= vat.ajax.getValue("CompanyName", oXHR.responseText) + "\n";
				var Title			= vat.ajax.getValue("Title", oXHR.responseText) + "\n";
				var ShopName		= vat.ajax.getValue("ShopName", oXHR.responseText) + "\n";
				var Address 		= vat.ajax.getValue("CompanyAddress", oXHR.responseText) + "\n";
				var CompanyId 		= "統編: " + vat.ajax.getValue("CompanyID", oXHR.responseText) + "\n";
				var CompanyTel		= "電話: " + vat.ajax.getValue("CompanyTel", oXHR.responseText) + "\n";
				var SaleDateTime	= "列印日期: " + vat.ajax.getValue("SaleDateTime", oXHR.responseText) + "\n";
				
				
           		var SubTotalCount  = vat.ajax.getValue("totalCount", oXHR.responseText);
           		var SubTotalAmount = vat.ajax.getValue("totalAmount", oXHR.responseText);

					/*
						** document.getElementById("clsInvoice").OpenPrint("印表機機碼", "配對碼");				//印表機配對
						** document.getElementById("clsInvoice").Initialize();									//初始化
						** document.getElementById("clsInvoice").Align(1);										//排版置中(0:左 1:中 2:右)
						** document.getElementById("clsInvoice").PrintTextUseEncoding("文字", 0, 0, 0,"Big5");	//列印文字
						** document.getElementById("clsInvoice").Feed(50);										//留空間
						** document.getElementById("clsInvoice").ClosePrint();									//關閉連線
						**
					*/
				var p = document.getElementById("clsInvoice").Initialize();
				
				//printTxtHead = printTxtHead + Company+ ShopName+ Address+ CompanyId+ Title+ CompanyTel+ SaleDateTime;
				printTxtHead = printTxtHead + devideLine +"\n" + Title + devideLine +"\n\n" + Company + ShopName + Address + CompanyTel + SaleDateTime + devideLine +"\n\n\n";

				document.getElementById("clsInvoice").Align(1);	

				document.getElementById("clsInvoice").PrintTextUseEncoding("\n", 0, 0, 0,"Big5");		

				document.getElementById("clsInvoice").PrintTextUseEncoding(printTxtHead, 0, 0, 0,"Big5");		
				//LKPOSALL.PrintText("\n", 1, 0, 0);
				//LKPOSALL.PrintText(printTxtHead, 1, 0, 0);
           		
//開啟連線           		
           		
//列印排版           		
				while (SubTotalCount.length < 36)
				{
					SubTotalCount = " " + SubTotalCount;
				}
				var SubTotalCount = "總筆數  = " + SubTotalCount + "\n";
				
				printTxt = printTxt + SubTotalCount + "\n";
				
				while (SubTotalAmount.length < 36)
				{
					SubTotalAmount = " " + SubTotalAmount;
				}
				var SubTotalAmount = "總金額  = " + SubTotalAmount + "\n";
				
				printTxt = printTxt + SubTotalAmount + "\n\n\n";

				//LKPOSALL.PrintText(printTxt, 0, 0, 0);
           		document.getElementById("clsInvoice").Align(0);	
				document.getElementById("clsInvoice").PrintTextUseEncoding(printTxt, 0, 0, 0,"Big5");
           		document.getElementById("clsInvoice").Feed(50);
//單據列印           		
				//p = LKPOSALL.OutputCompletePrinting(2500);
				//document.getElementById("clsInvoice").ClosePrint();
				
				}

				//}
           	}   
		});

		vat.item.setValueByName("#F.posBroCode","");
}

function importMovePrint(headId,times){
	var brandCode = document.forms[0]["#loginBrandCode"].value;
	//var headId = vat.item.getValueByName("#F.headId");
			 
	vat.ajax.XHRequest(
	{
        post:"process_object_name=soDepartmentOrderService" +
             "&process_object_method_name=importMovePrint" +
             "&brandCode=" + brandCode +
             "&headId=" + headId ,
		find: function posPrint(oXHR){
		}
	});

}
function importPrint3(headId,times,vCategory){
	var brandCode = "T1BS";
	//var headId = vat.item.getValueByName("#F.headId");
			 
	vat.ajax.XHRequest(
	{
        post:"process_object_name=soDepartmentOrderService" +
             "&process_object_method_name=importPrint" +
             "&brandCode=" + brandCode +
             "&headId=" + headId ,
		find: function posPrint(oXHR){
				var lResult;
				var COM = vat.ajax.getValue("ComNum", oXHR.responseText);

			//變數	
				var Company			= vat.ajax.getValue("CompanyName", oXHR.responseText) + "\n";
				var Title			= vat.ajax.getValue("Title", oXHR.responseText) + "\n";
				var ShopName		= vat.ajax.getValue("ShopName", oXHR.responseText) + "\n";
				var Address 		= vat.ajax.getValue("CompanyAddress", oXHR.responseText) + "\n";
				var CompanyId 		= "統編: " + vat.ajax.getValue("CompanyID", oXHR.responseText) + "\n";
				var CompanyTel		= "電話: " + vat.ajax.getValue("CompanyTel", oXHR.responseText) + "\n";
				var SaleDateTime	= vat.ajax.getValue("SaleDateTime", oXHR.responseText) + "\n";
				var Page 			= vat.ajax.getValue("Pg", oXHR.responseText);
				var Ty 				= vat.ajax.getValue("TY", oXHR.responseText);
				var CashierId 		= vat.ajax.getValue("CashierID", oXHR.responseText);
				var SalerId 		= vat.ajax.getValue("SalerID", oXHR.responseText);
				var SubTotal 		= vat.ajax.getValue("Sum", oXHR.responseText);
				var ReceivedMoney 	= vat.ajax.getValue("ReceivedMoney", oXHR.responseText);
				var CreditCard 		= vat.ajax.getValue("CreditCard", oXHR.responseText);
				var Voucher 		= vat.ajax.getValue("Voucher", oXHR.responseText);	
				var TotalSum 		= vat.ajax.getValue("TotalSum", oXHR.responseText);
				var Void 			= vat.ajax.getValue("Void", oXHR.responseText);
				
				
				var ItemId 					= vat.ajax.getValue("ItemID", oXHR.responseText);
				var ItemIdArray 			= ItemId.split(",");
				var Quantity 				= vat.ajax.getValue("Quantity", oXHR.responseText);
				var QuantityArray 			= Quantity.split(",");
				var UnitPrice 				= vat.ajax.getValue("UnitPrice", oXHR.responseText);
				var UnitPriceArray 			= UnitPrice.split(",");
				var ItemPrice 				= vat.ajax.getValue("ItemPrices", oXHR.responseText);
				var ItemPriceArray 			= ItemPrice.split(",");
				var Categories 				= vat.ajax.getValue("Categories", oXHR.responseText);
				var CategoriesArray			= Categories.split(",");
				var ItemName 				= vat.ajax.getValue("ItemNames", oXHR.responseText);
				var ItemNameArray 			= ItemName.split(",");
				var Discount  				= vat.ajax.getValue("Discount", oXHR.responseText);
				var DiscountArray  			= Discount.split(",");
				var DiscountPrice 			= vat.ajax.getValue("DiscountPrice", oXHR.responseText);	
				var DiscountPriceArray  	= DiscountPrice.split(",");
				var Remark 					= vat.ajax.getValue("Remark", oXHR.responseText);
				var RemarkArray 			= Remark.split(",");
				
				//alert('印單測試');
				lResult = "";
			//主體
			
				var c = 0;
				var p = "";
				var devideLine = "=============================================";

					for(c=0;c<times;c++){
					
					var printTxt = "";
					var printTxtHead = "";
					printTxtHead = printTxtHead + Company+ ShopName+ Address+ CompanyId+ Title+ CompanyTel+ SaleDateTime;
					printText("\n", 1, 0, 0);
					printText(printTxtHead, 1, 0, 0);
					var TyString = "TY: " + Ty;
//排版
					while (TyString.length < 40 - Page.length)
					{
						TyString = " " + TyString;
					}
					var PageTy = "VIP: " + Page + TyString + "\n";
					printTxt = printTxt + PageTy ;
					
					var SalerString = "SALES: " + SalerId;
					
//排版
					while(SalerString.length < 37 - CashierId.length)
					{
						SalerString = " " + SalerString;
					}
					var CashierSales = "SALENO: " + CashierId + SalerString + "\n";
					printTxt = printTxt + CashierSales + devideLine;
					
					
				//明細
					for (i=0; i<ItemIdArray.length; i++)
					{
//排版
						while(UnitPriceArray[i].length< 23 - ItemIdArray[i].length)
						{
							UnitPriceArray[i] = " "+UnitPriceArray[i];
						}
//排版
						while(QuantityArray[i].length< 9)
						{
							QuantityArray[i] = " "+QuantityArray[i];
						}
//排版
						while(ItemPriceArray[i].length< 21-QuantityArray[i].length)
						{
							ItemPriceArray[i] = " "+ItemPriceArray[i];
						}		
						var ItemIdString = "\n" + ItemIdArray[i] + UnitPriceArray[i] + QuantityArray[i] + ItemPriceArray[i] + "T\n";
						printTxt = printTxt + ItemIdString;
						
				
						if (DiscountArray.length > 0)
						{
							if (DiscountArray[i].length > 0)
							{
								var DiscountString = " Discount=(" + DiscountArray[i] + "% OFF)";
//排版
								while(DiscountPriceArray[i].length < 27-DiscountArray[i].length)
								{
									DiscountPriceArray[i] = " "+DiscountPriceArray[i];
								}
								var DiscoutPriceString = DiscountString + DiscountPriceArray[i] + "T\n";
								printTxt = printTxt + DiscoutPriceString;
								
							}
						}
//排版
						while (CategoriesArray[i].length < 8)
						{
							CategoriesArray[i] = CategoriesArray[i] + " ";
						}
						var CategoryTemp = "(" + CategoriesArray[i] + ")" + ItemNameArray[i];	
//排版	
						while (CategoryTemp.length < 34)
						{
							CategoryTemp = CategoryTemp + " ";
						}		
						var CategoryString = CategoryTemp + "\n";
						printTxt = printTxt + CategoryString;
						
				
						if (Remark[i] !== null)
						{
							if (RemarkArray[i].length > 0)
							{
//排版
								while(RemarkArray[i].length < 44)
								{
									RemarkArray[i] = RemarkArray[i] + " ";
								}
								var RemarkString = RemarkArray[i] + "\n";
								printTxt = printTxt + RemarkString;
											
							}
						}		
					}
					printTxt = printTxt + devideLine + "\n";
					
//排版
					while (SubTotal.length < 36)
					{
						SubTotal = " " + SubTotal;
					}
					var SubTotalString = "小  計 = " + SubTotal + "\n";
					printTxt = printTxt + SubTotalString + devideLine+"\n"+"付費方式：\n";
					
					if (ReceivedMoney !== "")
					{
//排版
						while(ReceivedMoney.length < 34)
						{
							ReceivedMoney = " " + ReceivedMoney;
						}
						var ReceivedMoneyString = "  現  金   " + ReceivedMoney + "\n";
						printTxt = printTxt + ReceivedMoneyString;
					}
					if (CreditCard !== "")
					{
//排版
						while(CreditCard.length < 34)
						{
							CreditCard = " " + CreditCard;
						}
						var CreditCardString = "  信用卡   " + CreditCard + "\n";
						printTxt = printTxt + CreditCardString;
					}
					if (Voucher !== "")
					{
//排版
						while(Voucher.length < 34)
						{
							Voucher = " " + Voucher;
						}
						var VoucherString = "  禮  卷   " + Voucher + "\n";
						printTxt = printTxt + VoucherString;
					}
//排版
					while(TotalSum.length < 36)
					{
						TotalSum = " " + TotalSum; 
					}
					var TotalSumString = "合  計 = " + TotalSum + "\n";
					printTxt = printTxt + TotalSumString + "\n\n\n\n";
					
					printText(printTxt, 0, 0, 0);
				}

	    }
	});       
}

