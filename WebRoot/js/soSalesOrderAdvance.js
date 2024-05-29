/*** 
 *	檔案: imMovementSearch.js
 *	說明：表單明細
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
var afterSavePageProcess = "";

function kweImBlock(){
  kweModifyInitial();
  kweButtonLine();
  kweImHeader();
  if(document.forms[0]["#enable"   ].value == "N")
  	vat.item.setAttributeByName("vatHeadDiv","readOnly",true,true,true);
}


function kweModifyInitial(){ 
  if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = 
  	    {
			loginBrandCode		: document.forms[0]["#loginBrandCode"   ].value,   	
			loginEmployeeCode   : document.forms[0]["#loginEmployeeCode"].value,	  
			lineId				: document.forms[0]["#lineId"           ].value,
			headId				: document.forms[0]["#headId"           ].value,
			warehouseCode		: document.forms[0]["#warehouseCode"    ].value,
			taxType				: document.forms[0]["#taxType"          ].value,
			taxRate				: document.forms[0]["#taxRate"          ].value,
			discountRate		: document.forms[0]["#discountRate"     ].value
	    };
     vat.bean.init(function(){
     	return "process_object_name=soSalesOrderMainAction&process_object_method_name=performInitialAdvance"; 
   	    },{other: true});
  }
 
}
function kweButtonLine(){
    vat.block.create(vnB_Button = 0, {
	id: "vatBlock_Button", generate: true,
	table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.submit"      , type:"IMG"      , value:"送出",  src:"./images/button_submit.gif", eClick:"doSubmit()"},
	 			{name:"SPACE"          , type:"LABEL"    , value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"      , value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"    , value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});  
}

function kweImHeader(){ 
	var brandCode = document.forms[0]["#loginBrandCode"   ].value;
vat.block.create(vnB_Header = 1, {
	id: "vatHeadDiv", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"銷貨單明細資料", rows:[  
	 {row_style:"", cols:[
		 {items:[{name:"#L.itemCode"				, type:"LABEL"  , value:"品號"}]},	 
		 {items:[{name:"#F.itemCode"       			, type:"TEXT" 	, bind:"itemCode"	, size:14, eChange:"changeAdvanceData(1)"},
		 		 {name:"#F.itemCName"       		, type:"TEXT" 	, bind:"itemCName"	, size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.warehouseCode"			, type:"LABEL"  , value:"庫別"}]},
		 {items:[{name:"#F.warehouseCode"			, type:"TEXT"  	, bind:"warehouseCode", size:14, mode:"readOnly"},
		 		 {name:"#F.warehouseName"			, type:"TEXT"  	, bind:"warehouseName", mode:"readOnly"}]},
		 {items:[{name:"#L.serviceItemPrice"		, type:"LABEL"  , value:"服務性商品單價"}]},
		 {items:[{name:"#F.serviceItemPrice"		, type:"NUMM"  	, bind:"serviceItemPrice", size:14, eChange:"changeAdvanceData(2)"}]}
	 ]},
		{row_style:brandCode=="T2"?"":" style='display:none'", cols:[
		 {items:[{name:"#L.originalForeignUnitPrice", type:"LABEL"  , value:"原幣單價"}]},	 
		 {items:[{name:"#F.originalForeignUnitPrice", type:"NUMM" 	, bind:"originalForeignUnitPrice", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.originalForeignSalesAmt"	, type:"LABEL"  , value:"原幣金額"}]},
		 {items:[{name:"#F.originalForeignSalesAmt"	, type:"NUMM"  	, bind:"originalForeignSalesAmt", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.deductionForeignAmount"	, type:"LABEL"  , value:"原幣折讓金額"}]},
		 {items:[{name:"#F.deductionForeignAmount"	, type:"NUMM"  	, bind:"deductionForeignAmount", size:14, mode:"readOnly"}]}
	 ]},
	 {row_style:"", cols:[
		 {items:[{name:"#L.originalUnitPrice"		, type:"LABEL"  , value:"單價"}]},	 
		 {items:[{name:"#F.originalUnitPrice"		, type:"NUMM" 	, bind:"originalUnitPrice", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.originalSalesAmount"		, type:"LABEL"  , value:"金額"}]},
		 {items:[{name:"#F.originalSalesAmount"		, type:"NUMM"  	, bind:"originalSalesAmount", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.deductionAmount"			, type:"LABEL"  , value:"折讓金額"}]},
		 {items:[{name:"#F.deductionAmount"			, type:"NUMM"  	, bind:"deductionAmount", size:14, eChange:"changeAdvanceData(2)"}]}
	 ]},
	 {row_style:"", cols:[
		 {items:[{name:"#L.currentOnHandQty"		, type:"LABEL"  , value:"庫存量"}]},	 
		 {items:[{name:"#F.currentOnHandQty"		, type:"NUMM" 	, bind:"currentOnHandQty", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.vipPromotionCode"		, type:"LABEL"  , value:"VIP類別代號"}]},
		 {items:[{name:"#F.vipPromotionCode"		, type:"TEXT"  	, bind:"vipPromotionCode", size:14},
		 		 {name:"#F.vipPromotionName"		, type:"TEXT"  	, bind:"vipPromotionName", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.promotionCode"			, type:"LABEL"  , value:"活動代號"}]},
		 {items:[{name:"#F.promotionCode"			, type:"TEXT"  	, bind:"promotionCode", size:14, eChange:"changeAdvanceData(2)"},
		 		 {name:"#F.promotionName"			, type:"TEXT"  	, bind:"promotionName", size:14, mode:"readOnly"}]}
	 ]},
	 {row_style:"", cols:[
		 {items:[{name:"#L.discountRate"			, type:"LABEL"  , value:"折扣率"}]},	 
		 {items:[{name:"#F.discountRate"			, type:"NUMM" 	, bind:"discountRate", size:14, eChange:"changeAdvanceData(2)"},
		 		 {name:"#L.%"						, type:"LABEL"  , value:" %"}]},
		 {items:[{name:"#L.taxType"					, type:"LABEL"  , value:"稅別"}]},
		 {items:[{name:"#F.taxType"					, type:"SELECT" , bind:"taxType", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.taxRate"					, type:"LABEL"  , value:"稅率"}]},
		 {items:[{name:"#F.taxRate"					, type:"NUMM"  	, bind:"taxRate", size:14, mode:"readOnly"},
		 		 {name:"#L.%"						, type:"LABEL"  , value:" %"}]}
	 ]},
		{row_style:brandCode=="T2"?"":" style='display:none'", cols:[
		 {items:[{name:"#L.actualForeignUnitPrice"	, type:"LABEL"  , value:"折扣後原幣單價"}]},	 
		 {items:[{name:"#F.actualForeignUnitPrice"	, type:"NUMM" 	, bind:"actualForeignUnitPrice", size:14, eChange:"changeAdvanceData(3)"}]},
		 {items:[{name:"#L.actualForeignSalesAmt"	, type:"LABEL"  , value:"折扣後原幣金額"}]},
		 {items:[{name:"#F.actualForeignSalesAmt"	, type:"NUMM"  	, bind:"actualForeignSalesAmt", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.taxForeignAmount"		, type:"LABEL"  , value:"原幣稅金"}]},
		 {items:[{name:"#F.taxForeignAmount"		, type:"NUMM"  	, bind:"taxForeignAmount", size:14, mode:"readOnly"}]}
	 ]},
	 {row_style:"", cols:[
		 {items:[{name:"#L.actualUnitPrice"			, type:"LABEL"  , value:"折扣後單價"}]},	 
		 {items:[{name:"#F.actualUnitPrice"			, type:"NUMM" 	, bind:"actualUnitPrice", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.actualSalesAmount"		, type:"LABEL"  , value:"折扣後金額"}]},
		 {items:[{name:"#F.actualSalesAmount"		, type:"NUMM"  	, bind:"actualSalesAmount", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.taxAmount"				, type:"LABEL"  , value:"稅金"}]},
		 {items:[{name:"#F.taxAmount"				, type:"NUMM"  	, bind:"taxAmount", size:14, mode:"readOnly"}]}
	 ]},
	 {row_style:"", cols:[
		 {items:[{name:"#L.quantity"				, type:"LABEL"  , value:"數量"}]},	 
		 {items:[{name:"#F.quantity"				, type:"NUMM" 	, bind:"quantity", size:14, eChange:"changeAdvanceData(2)"}]},
		 {items:[{name:"#L.depositCode"				, type:"LABEL"  , value:"訂金單代號"}]},
		 {items:[{name:"#F.depositCode"				, type:"TEXT"  	, bind:"depositCode", size:14}]},
		 {items:[{name:"#L.watchSerialNo"			, type:"LABEL"  , value:"手錶序號"}]},
		 {items:[{name:"#F.watchSerialNo"			, type:"TEXT"  	, bind:"watchSerialNo", size:14}]}
	 ]},
	 {row_style:"", cols:[
		 {items:[{name:"#L.isUseDeposit"			, type:"LABEL"  , value:"訂金支付"}]},	 
		 {items:[{name:"#F.isUseDeposit"			, type:"SELECT" , bind:"isUseDeposit", size:14}]},
		 {items:[{name:"#L.supplierItemCode"		, type:"LABEL"  , value:"原廠編號"}]},
		 {items:[{name:"#F.supplierItemCode"		, type:"TEXT"  	, bind:"supplierItemCode", size:14, mode:"readOnly"}]}
	 ]},
	{row_style:brandCode=="T2"?"":" style='display:none'", cols:[
		 {items:[{name:"#L.importCurrencyCode"		, type:"LABEL"  , value:"進貨幣別"}]},	 
		 {items:[{name:"#F.importCurrencyCode"		, type:"TEXT" 	, bind:"importCurrencyCode", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.standardPurchaseCost"	, type:"LABEL"  , value:"上月期末加權平均成本"}]},
		 {items:[{name:"#F.standardPurchaseCost"	, type:"NUMM"  	, bind:"standardPurchaseCost", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.importCost"				, type:"LABEL"  , value:"進貨成本"}]},
		 {items:[{name:"#F.importCost"				, type:"NUMM"  	, bind:"importCost", size:14, mode:"readOnly"}]}
	 ]},
	{row_style:brandCode=="T2"?"":" style='display:none'", cols:[
		 {items:[{name:"#L.importDeclNo"			, type:"LABEL"  , value:"進口報單單號"}]},	 
		 {items:[{name:"#F.importDeclNo"			, type:"TEXT" 	, bind:"importDeclNo", size:14, eChange:"changeDeclNo()"}]},
		 {items:[{name:"#L.importDeclDate"			, type:"LABEL"  , value:"進口報單日期"}]},
		 {items:[{name:"#F.importDeclDate"			, type:"DATE"  	, bind:"importDeclDate", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.importDeclSeq"			, type:"LABEL"  , value:"進口報單項次"}]},
		 {items:[{name:"#F.importDeclSeq"			, type:"TEXT"  	, bind:"importDeclSeq", size:14}]}
	 ]},
	{row_style:brandCode=="T2"?"":" style='display:none'", cols:[
		 {items:[{name:"#L.usedIdentification"		, type:"LABEL"  , value:"使用身份"}]},	 
		 {items:[{name:"#F.usedIdentification"		, type:"TEXT" 	, bind:"usedIdentification", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.usedCardId"				, type:"LABEL"  , value:"使用卡號"}]},
		 {items:[{name:"#F.usedCardId"				, type:"TEXT"  	, bind:"usedCardId", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.usedCardType"			, type:"LABEL"  , value:"使用卡別"}]},
		 {items:[{name:"#F.usedCardType"			, type:"TEXT"  	, bind:"usedCardType", size:14, mode:"readOnly"}]}
	 ]},
	{row_style:brandCode=="T2"?"":" style='display:none'", cols:[
		 {items:[{name:"#L.usedDiscountRate"		, type:"LABEL"  , value:"使用折扣率"}]},	 
		 {items:[{name:"#F.usedDiscountRate"		, type:"TEXT" 	, bind:"usedDiscountRate", size:14, mode:"readOnly"}]},
		 {items:[{name:"#L.itemDiscountType"		, type:"LABEL"  , value:"商品折扣類型"}]},
		 {items:[{name:"#F.itemDiscountType"		, type:"TEXT"  	, bind:"itemDiscountType", size:14, mode:"readOnly"}]}
	 ]},
	 {row_style:"", cols:[
		 {items:[{name:"#F.isServiceItem"	, type:"TEXT" 	, bind:"isServiceItem", size:14, mode:"hidden"},
		 		 {name:"#F.discount"		, type:"TEXT" 	, bind:"discount", size:14, mode:"hidden"},
		 		 {name:"#F.discountType"	, type:"TEXT" 	, bind:"discountType", size:14, mode:"hidden"},
		 		 {name:"#F.vipDiscount"		, type:"TEXT" 	, bind:"vipDiscount", size:14, mode:"hidden"},
		 		 {name:"#F.vipDiscountType"	, type:"TEXT" 	, bind:"vipDiscountType", size:14, mode:"hidden"},
		 		 {name:"#F.allowMinusStock"	, type:"TEXT" 	, bind:"allowMinusStock", size:14, mode:"hidden"},
		 		 {name:"#F.allowWholeSale"	, type:"TEXT" 	, bind:"allowWholeSale", size:14, mode:"hidden"},
		 		 {name:"#F.importDeclType"	, type:"TEXT" 	, bind:"importDeclType", size:14, mode:"hidden"}]}
	 ]}
	  ], 	 
		beginService:"",
		closeService:function(){closeHead();}
	});
}

function closeHead(){
	vat.item.SelectBind([["","N",false],["否","是"],["N","Y"]], { itemName : "#F.isUseDeposit" });
	vat.ajax.XHRequest({ 
		post:"process_object_name=soSalesOrderMainService"+
	          		"&process_object_method_name=findInitialCommonAdvance",
	          asyn:false,                      
		find: function change(oXHR){
			vat.item.SelectBind(eval(vat.ajax.getValue("allTaxType", oXHR.responseText)),{ itemName : "#F.taxType" });
		}
	});
	vat.item.bindAll();
}

function changeDeclNo(){
    var vDeclNo = vat.item.getValueByName("#F.importDeclNo").replace(/^\s+|\s+$/, '').toUpperCase();
    alert('vDeclNo = ' + vDeclNo);
    vat.item.getValueByName("#F.importDeclNo", vDeclNo);
    vat.ajax.XHRequest(
    {
        asyn:false,
        post:"process_object_name=soSalesOrderMainService"+
                    "&process_object_method_name=executeFindCM"+
                    "&exportDeclNo=" + vDeclNo,
        find: function getOriginalDeliverySuccess(oXHR){
        	vat.item.setValueByName("#F.importDeclDate" ,vat.ajax.getValue("ExportDeclDate", oXHR.responseText));
        	vat.item.setValueByName("#F.importDeclType" ,vat.ajax.getValue("ExportDeclType", oXHR.responseText));
       }   
   });
}

function changeAdvanceData(actionId){
	var vOriginalUnitPrice = parseFloat(vat.item.getValueByName("#F.originalUnitPrice"));
	if(isNaN(vOriginalUnitPrice))
		vOriginalUnitPrice = 0;
	var vExportExchangeRate = document.forms[0]["#exportExchangeRate"].value;
	var vActualForeignUnitPrice = parseFloat(vat.item.getValueByName("#F.actualForeignUnitPrice"));
	if(isNaN(vActualForeignUnitPrice))
		vActualForeignUnitPrice = 0;
	var vActualUnitPrice = vActualForeignUnitPrice * vExportExchangeRate;
	if(isNaN(vActualUnitPrice))
		vActualUnitPrice = 0;
	var vDeductionAmount = parseFloat(vat.item.getValueByName("#F.deductionAmount"));
	if(isNaN(vDeductionAmount))
		vDeductionAmount = 0;
	var vDiscountRate = 100; 
	if(actionId == 3)
		vDiscountRate = (vActualUnitPrice+vDeductionAmount)*100/vOriginalUnitPrice;
	else
		vDiscountRate = parseFloat(vat.item.getValueByName("#F.discountRate"));
	if(isNaN(vDiscountRate))
		vDiscountRate = 100;
	vDiscountRate = Math.round(vDiscountRate*100)/100;
	vat.item.setValueByName("#F.discountRate", vDiscountRate);
    var vBrandCode = document.forms[0]["#loginBrandCode"].value;
    var vOrderTypeCode = document.forms[0]["#orderTypeCode"].value;
    var vPriceType = document.forms[0]["#priceType"].value;
    var vShopCode = document.forms[0]["#shopCode"].value;
    var vCustomerType = document.forms[0]["#customerType"].value;
    var vVipType = document.forms[0]["#vipType"].value;
    var vWarehouseEmployee= document.forms[0]["#warehouseEmployee"].value;
    var vWarehouseManager= document.forms[0]["#warehouseManager"].value;
    var vSalesDate= document.forms[0]["#salesDate"].value;
    var vItemCode = vat.item.getValueByName("#F.itemCode").replace(/^\s+|\s+$/, '').toUpperCase();
    var vWarehouseCode = vat.item.getValueByName("#F.warehouseCode");
    var vQuantity = Math.round(vat.item.getValueByName("#F.quantity"));
    var vPromotionCode= vat.item.getValueByName("#F.promotionCode");
	var vVipPromotionCode= vat.item.getValueByName("#F.vipPromotionCode");
	var vTaxType= vat.item.getValueByName("#F.taxType");
    var vTaxRate= vat.item.getValueByName("#F.taxRate"); 
    var vServiceItemPrice = vat.item.getValueByName("#F.serviceItemPrice").replace(/^\s+|\s+$/, '');   
    if(vTaxType == "1" || vTaxType == "2"){
        vTaxRate = "0.0";
    }
    if(isNaN(vServiceItemPrice)){
        alert("服務性商品單價必須為數值！");
    }else if(isNaN(vActualForeignUnitPrice)){
        alert("折扣後原幣單價！");
    }else if(isNaN(vDeductionAmount)){
        alert("折讓金額必須為數值！");
    }else if(isNaN(vDiscountRate)){
        alert("折扣率必須為數值！");
    }else if(isNaN(vQuantity)){
        alert("數量欄位必須為數值！");
    }else if(isNaN(vTaxRate)){
        alert("稅率欄位必須為數值！");
    }else{   
        vat.ajax.XHRequest(
        {
            post:"process_object_name=soSalesOrderMainService" +
            "&process_object_method_name=getAJAXItemData" +
            "&brandCode=" + vBrandCode +
            "&orderTypeCode=" + vOrderTypeCode +
            "&priceType=" + vPriceType + 
            "&shopCode=" + vShopCode +
            "&customerType=" + vCustomerType +
            "&vipType=" + vVipType +
            "&itemCode=" + vItemCode +
            "&warehouseCode=" + vWarehouseCode +
            "&quantity=" + vQuantity +
            "&deductionAmount=" + vDeductionAmount +
            "&discountRate=" + vDiscountRate +
            "&promotionCode=" + vPromotionCode +
            "&vipPromotionCode=" + vVipPromotionCode +                         
            "&warehouseManager=" + vWarehouseManager +
            "&warehouseEmployee=" + vWarehouseEmployee +
            "&originalUnitPrice=" + vServiceItemPrice +
            "&salesDate=" + vSalesDate +
            "&taxType=" + vTaxType +
            "&taxRate=" + vTaxRate +
         	"&exportExchangeRate=" + vExportExchangeRate +
         	"&actualForeignUnitPrice=" + vActualForeignUnitPrice +
            "&actionId=" + actionId,                                                 
            find: function changeAdvanceItemDataRequestSuccess(oXHR){
            	vat.item.setValueByName("#F.itemCode", vat.ajax.getValue("ItemCode", oXHR.responseText));
            	vat.item.setValueByName("#F.itemCName", vat.ajax.getValue("ItemCName", oXHR.responseText));
            	vat.item.setValueByName("#F.warehouseCode", vat.ajax.getValue("WarehouseCode", oXHR.responseText));
                vat.item.setValueByName("#F.warehouseName", vat.ajax.getValue("WarehouseName", oXHR.responseText));
                vat.item.setValueByName("#F.originalUnitPrice", vat.ajax.getValue("OriginalUnitPrice", oXHR.responseText));
                vat.item.setValueByName("#F.actualUnitPrice", vat.ajax.getValue("ActualUnitPrice", oXHR.responseText));
                vat.item.setValueByName("#F.currentOnHandQty", vat.ajax.getValue("CurrentOnHandQty", oXHR.responseText));
                vat.item.setValueByName("#F.quantity", vat.ajax.getValue("Quantity", oXHR.responseText));
                vat.item.setValueByName("#F.originalSalesAmount", vat.ajax.getValue("OriginalSalesAmount", oXHR.responseText));
                vat.item.setValueByName("#F.actualSalesAmount", vat.ajax.getValue("ActualSalesAmount", oXHR.responseText));
                vat.item.setValueByName("#F.deductionAmount", vat.ajax.getValue("DeductionAmount", oXHR.responseText));
                vat.item.setValueByName("#F.discountRate", vat.ajax.getValue("DiscountRate", oXHR.responseText));
                vat.item.setValueByName("#F.promotionCode", vat.ajax.getValue("PromotionCode", oXHR.responseText));
                vat.item.setValueByName("#F.promotionName", vat.ajax.getValue("PromotionName", oXHR.responseText));
                vat.item.setValueByName("#F.discount", vat.ajax.getValue("Discount", oXHR.responseText));
                vat.item.setValueByName("#F.discountType", vat.ajax.getValue("DiscountType", oXHR.responseText));
                vat.item.setValueByName("#F.vipPromotionCode", vat.ajax.getValue("VipPromotionCode", oXHR.responseText));
                vat.item.setValueByName("#F.vipPromotionName", vat.ajax.getValue("VipPromotionName", oXHR.responseText));
                vat.item.setValueByName("#F.vipDiscount", vat.ajax.getValue("VipDiscount", oXHR.responseText));
                vat.item.setValueByName("#F.vipDiscountType", vat.ajax.getValue("VipDiscountType", oXHR.responseText));
                vat.item.setValueByName("#F.taxType", vat.ajax.getValue("TaxType", oXHR.responseText));
                vat.item.setValueByName("#F.taxRate", vat.ajax.getValue("TaxRate", oXHR.responseText));
                vat.item.setValueByName("#F.isServiceItem", vat.ajax.getValue("IsServiceItem", oXHR.responseText));
                vat.item.setValueByName("#F.taxAmount", vat.ajax.getValue("TaxAmount", oXHR.responseText));
                vat.item.setValueByName("#F.originalForeignUnitPrice", vat.ajax.getValue("OriginalForeignUnitPrice", oXHR.responseText));
	            vat.item.setValueByName("#F.actualForeignUnitPrice", vat.ajax.getValue("ActualForeignUnitPrice", oXHR.responseText));
	            vat.item.setValueByName("#F.originalForeignSalesAmt", vat.ajax.getValue("OriginalForeignSalesAmt", oXHR.responseText));
	            vat.item.setValueByName("#F.actualForeignSalesAmt", vat.ajax.getValue("ActualForeignSalesAmt", oXHR.responseText));
	            vat.item.setValueByName("#F.deductionForeignAmount", vat.ajax.getValue("DeductionForeignAmount", oXHR.responseText));
				vat.item.setValueByName("#F.taxForeignAmount", vat.ajax.getValue("TaxForeignAmount", oXHR.responseText));
				vat.item.setValueByName("#F.importCost", vat.ajax.getValue("ImportCost", oXHR.responseText));
	            vat.item.setValueByName("#F.importCurrencyCode", vat.ajax.getValue("ImportCurrencyCode", oXHR.responseText));
                vat.item.setValueByName("#F.supplierItemCode", vat.ajax.getValue("SupplierItemCode", oXHR.responseText));
                vat.item.setValueByName("#F.standardPurchaseCost", vat.ajax.getValue("StandardPurchaseCost", oXHR.responseText));
                vat.item.setValueByName("#F.itemDiscountType", vat.ajax.getValue("ItemDiscountType", oXHR.responseText));
                vat.item.setValueByName("#F.allowMinusStock", vat.ajax.getValue("AllowMinusStock", oXHR.responseText));
                vat.item.setValueByName("#F.allowWholeSale", "N");
                if(vat.item.getValueByName("#F.isServiceItem") !== "Y"){
                    vat.item.setValueByName("#F.serviceItemPrice", "");
                }else{
	                vat.item.setValueByName("#F.promotionCode", "");
	                vat.item.setValueByName("#F.promotionName", "");
	                vat.item.setValueByName("#F.discount", "");
	                vat.item.setValueByName("#F.discountType", "");
                }
            }
        });
    }
}

function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit)
    	window.top.close();
    	
}


function doSubmit(){
	if(confirm("是否確定送出?")){
		  vat.block.submit(function(){return "process_object_name=soSalesOrderMainAction&process_object_method_name=performTransactionAdvance";},
		  					{
								bind:true, link:false, other:true, 
	                    		funcSuccess: function() {	window.top.close();}
	                    	});
	}
}