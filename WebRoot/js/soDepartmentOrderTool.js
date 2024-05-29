/**
***百貨POS按鈕功能
***/
var errorMsg = "輸入錯誤，請再試一次，若問題仍持續發生，請聯絡系統人員";
var stepErrorMsg = "操作錯誤，請確認執行步驟";
var exeDetailModify = "單項商品資料變更";
var manualPmo = "手動折扣/折讓";
var doTotalCount = "小計";
var returnTotalCount = "取消小計";
var setPayType = "付款方式";
var exeSubmit = "結帳";
var itemInsert = "商品輸入";

var isZZ = false;
var amount;
var count;
var allDiscount = "";
/**
***退貨
***贈品
***數量
***/
function detailModify(category,newQuantity){ 
	if(stepConfig("isExecute",1,1))
	{
	   	var quantity = vat.item.nameMake(category, ptr-1);
	   	var vOriginalUnitPrice = vat.item.getGridValueByName('originalUnitPrice',ptr-1);
	   	var vDiscountRate = vat.item.getGridValueByName('discountRate',ptr-1);
		var vIsInt;
	   	
	   	if(newQuantity!== -1){
	   		if(vat.item.getValueByName("#F.posBroCode") !== "")
				newQuantity = vat.item.getValueByName("#F.posBroCode");
		}
		else{
			newQuantity = "-1";
		}
		vIsInt = isInt(newQuantity);//輸入整數判斷
		//alert();
		if((newQuantity != 0 || category !=='quantity') && vIsInt){
			var ptr_Qty = ptr-1;
			var OnChange_Qty = document.getElementById(quantity);
			vat.item.setGridValueByName(category,ptr_Qty,newQuantity);
			//OnChange_Qty.fireEvent('onchange');
			//showTotalCountPage();//回傳參數
			//resetForm();
			var newActualSalesAmount = vOriginalUnitPrice * (vDiscountRate/100) * newQuantity;
			vat.item.setGridValueByName('actualSalesAmount',ptr-1,newActualSalesAmount);
		}
		else{
			alert("輸入錯誤，數量為0或未輸入數量");		
		}
	
	
	
		//vat.item.getValueByName("#F.actualSalesAmount");
		
		vat.item.setValueByName("#F.posBroCode","");
		showTotalCountPage();//回傳參數
		if(stepConfig("isChange",1,1)){
			nowStep=1;
		}
	}
	else{
		alert(exeDetailModify+stepErrorMsg);
	}
}
/**
***折扣
***折讓//category1:折扣 2:折讓
***全部折扣
***全部折讓
//discountType 'SINGEL'
**/
function doDiscount(category,discountType,executeWay){
	if(stepConfig("isExecute",2,2))
	{
		var vPtr;
		//alert(vnCurrentPage);
		vPtr = (discountType === 'SINGEL')?ptr-1:0;
		var vAmount = vat.item.getValueByName("#F.posBroCode");
		var vIsInt = isInt(vAmount);
		if((vAmount !== "") && vIsInt)
		{
			vat.block.pageDataSave(vnB_Detail, 
			{  
				funcSuccess:function(){
					//alert(vPtr);
					vat.ajax.XHRequest(
				    {
				    	post:"process_object_name=soDepartmentOrderService" +
				                        "&process_object_method_name=executeDiscount" +
				                        "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
					                    "&headId=" + vat.item.getValueByName("#F.headId")+
					                    "&vAmount=" + vAmount +
					                    "&category=" + category+
					                    "&executeWay=" + executeWay+
					                    "&ptr=" + vat.item.getGridValueByName("indexNo",vPtr),
						find: function changeItemDataRequestSuccess(oXHR)
						{
							vat.block.pageRefresh(vnB_Detail);
				        }
				    });
				}
			});
		}
		else{
			alert("輸入錯誤,請輸入折扣率/折讓金額");
		}
		vat.item.setValueByName("#F.posBroCode","");
		if(stepConfig("isChange",1,1)){
			nowStep=1;
		}
	}
	else{
		alert(manualPmo+stepErrorMsg);
	}
}
/**
***小計
**/
function executeTotalCountPage() {
	vHeadId = vat.item.getValueByName("#F.headId");
	vCustomerCode = vat.item.getValueByName("#F.customerCode");
	if(stepConfig("isExecute",1,1)){
		//showTotalCountPage();
		vat.block.pageDataSave(vnB_Detail,{
			funcSuccess:function(){
				vat.ajax.XHRequest({
					post:"process_object_name=soDepartmentOrderService"+
		               		 "&process_object_method_name=findMemberCondition"+
		               		 "&headId=" + vHeadId+
		               		 "&customerCode="+vCustomerCode,
			        find: function change(oXHR){
					    var vAttach = vat.ajax.getValue("attach" , oXHR.responseText);					    
					    if(vAttach == "Y"){
					    	//alert("達成");
					    	if(confirm("已達金額門檻，是否建立會員資料")){
						    	if(vat.item.getValueByName("#F.customerCode")===""){
									openVIPCreateWindow();					    	
						    	}		
					    	}			    	
						}
						
						if(stepConfig("isChange",2,2)){
							nowStep=2;
						}
						//alert("小計");
						if(vat.item.getValueByName("#F.balance")===""){
							vat.item.setValueByName("#F.balance",vat.item.getValueByName("#F.totalOriginalSalesAmount"));
						}
		
					},
					fail: function changeError(){
			          		alert("fail");
			        }
				});
			}
		});
		/*
			if(stepConfig("isChange",2,2)){
				nowStep=2;
			}
			//alert("小計");
			if(vat.item.getValueByName("#F.balance")==="")
			{
				vat.item.setValueByName("#F.balance",vat.item.getValueByName("#F.totalOriginalSalesAmount"));
			}*/
		
	}
	else{
		alert(doTotalCount+stepErrorMsg);
	}
}

function executeExchangeRate(){
	vHeadId = vat.item.getValueByName("#F.headId");
	if(stepConfig("isExecute",1,1)){
		//showTotalCountPage();
		vat.block.pageDataSave(vnB_Detail,{
			funcSuccess:function(){
				vat.ajax.XHRequest({
					post:"process_object_name=soDepartmentOrderService"+
		               		 "&process_object_method_name=findMemberCondition"+
		               		 "&headId=" + vHeadId+
		               		 "&customerCode="+vCustomerCode,
			        find: function change(oXHR){
					    var vAttach = vat.ajax.getValue("attach" , oXHR.responseText);					    
					    if(vAttach == "Y"){
					    	//alert("達成");
					    	if(confirm("已達金額門檻，是否建立會員資料")){
						    	if(vat.item.getValueByName("#F.customerCode")===""){
									openVIPCreateWindow();					    	
						    	}		
					    	}			    	
						}
						
						if(stepConfig("isChange",2,2)){
							nowStep=2;
						}
						//alert("小計");
						if(vat.item.getValueByName("#F.balance")===""){
							vat.item.setValueByName("#F.balance",vat.item.getValueByName("#F.totalOriginalSalesAmount"));
						}
						
		
					},
					fail: function changeError(){
			          		alert("fail");
			        }
				});
			}
		});
		
		
	}else{
		alert(doTotalCount+stepErrorMsg);
	}
	
}
/*
***取消小計
**/
function cancelTotalCountPage() {

	if(stepConfig("isExecute",2,2))
	{
		vat.item.setValueByName("#F.payment_cash","");
		vat.item.setValueByName("#F.payment_card","");
		vat.item.setValueByName("#F.payment_groupon","");
		vat.item.setValueByName("#F.balance","");
		nowStep=1;
		alert("取消小計");
	}
	else{
		alert(returnTotalCount+stepErrorMsg);
	}
}

/**現金:cash信用卡:card禮券:coupon**/		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      	
function payType(payType){
	var vnowStep = nowStep;
	if(stepConfig("isExecute",2,2))
	{
		var payAmount = vat.item.getValueByName("#F.posBroCode");
		var customerPoNo = vat.item.getValueByName("#F.headId");
		
		
		if(payAmount==null||payAmount==""){
		
			var vCashNeed = vat.item.getValueByName("#F.balance");
			vCashNeed = vCashNeed + vat.item.getValueByName("#F.payment_"+payType);


			vat.item.setValueByName("#F.payment_"+payType,vCashNeed);
			vCashNeed = vat.item.getValueByName("#F.totalOriginalSalesAmount") - vat.item.getValueByName("#F.payment_card") - vat.item.getValueByName("#F.payment_groupon") - vat.item.getValueByName("#F.payment_cash");
			vat.item.setValueByName("#F.balance",vCashNeed);

		}
		else{
		
			var vCashNeed = vat.item.getValueByName("#F.balance");
			
			vCashNeed = vCashNeed + vat.item.getValueByName("#F.payment_"+payType);
			if(payType == "thirdParty"){
				openReportWindow("SO_Department_Order","main","thirdParty20181115","");
			}
			//alert(vCashNeed-payAmount);
			//alert(vCashNeed +" "+vat.item.getValueByName("#F.payment_card") +" "+ vat.item.getValueByName("#F.payment_groupound")+" "+vat.item.getValueByName("#F.payment_cash"));
			if((vCashNeed-payAmount) >= 0){
				vat.item.setValueByName("#F.payment_"+payType,payAmount);
				vCashNeed = vat.item.getValueByName("#F.totalOriginalSalesAmount") 
							- vat.item.getValueByName("#F.payment_card") 
							- vat.item.getValueByName("#F.payment_groupon") 
							- vat.item.getValueByName("#F.payment_cash")
							- vat.item.getValueByName("#F.payment_thirdParty");
				vat.item.setValueByName("#F.balance",vCashNeed);
				
			}
			else{
				alert("金額輸入錯誤,支付金額"+payAmount+"元,超過結帳餘額"+vCashNeed+"元");
			}
		}
		/*
		if(payAmount==null||payAmount==""||payType==='cash'){
	
			if(payType==='cash')
			{
				var vCashNeed = vat.item.getValueByName("#F.totalOriginalSalesAmount") - vat.item.getValueByName("#F.payment_card") - vat.item.getValueByName("#F.payment_groupon");
				//alert(vCashNeed +" "+vat.item.getValueByName("#F.payment_card") +" "+ vat.item.getValueByName("#F.payment_groupon")+" "+vat.item.getValueByName("#F.payment_cash"));
				vat.item.setValueByName("#F.payment_"+payType,vCashNeed);
				vCashNeed = vat.item.getValueByName("#F.totalOriginalSalesAmount") - vat.item.getValueByName("#F.payment_card") - vat.item.getValueByName("#F.payment_groupon") - vat.item.getValueByName("#F.payment_cash");
				vat.item.setValueByName("#F.balance",vCashNeed);
				if(stepConfig("isChange",2,2)){
					nowStep=3;
				}
				doSubmit("SIGNING");

	
			}
			else{
				alert("請輸入金額");//若無輸入金額則彈出提示
			}
		}else{
			var vCashNeed = vat.item.getValueByName("#F.balance");
			
			vCashNeed = vCashNeed + vat.item.getValueByName("#F.payment_"+payType);
			
			//alert(vCashNeed-payAmount);
			//alert(vCashNeed +" "+vat.item.getValueByName("#F.payment_card") +" "+ vat.item.getValueByName("#F.payment_groupound")+" "+vat.item.getValueByName("#F.payment_cash"));
			if((vCashNeed-payAmount) >= 0){
				vat.item.setValueByName("#F.payment_"+payType,payAmount);
				vCashNeed = vat.item.getValueByName("#F.totalOriginalSalesAmount") - vat.item.getValueByName("#F.payment_card") - vat.item.getValueByName("#F.payment_groupon") - vat.item.getValueByName("#F.payment_cash");
				vat.item.setValueByName("#F.balance",vCashNeed);
				
			}
			else{
				alert("金額輸入錯誤,支付金額"+payAmount+"元,超過結帳餘額"+vCashNeed+"元");
			}
		}
		vat.item.setValueByName("#F.posBroCode","");
		showTotalCountPage();//回傳參數

	}
	else{
		if(payType==='cash'){
			alert(exeSubmit+stepErrorMsg);
		}
		else{
			alert(setPayType+stepErrorMsg);
		}*/

		vat.item.setValueByName("#F.posBroCode","");
		showTotalCountPage();//回傳參數
	}
	else{
		alert(setPayType+stepErrorMsg);//流程錯誤提示
	}
}
/**按鈕面板(主頁面:mainMenu/服務性商品:zzMenu/查詢頁面:searchMenu**/
function functionLable(buttonDivCode){
//隱藏所有面板
	for(var i=1;i<=3;i++){
		for(var j=1;j<=2;j++){
			vat.item.setStyleByName("#B.0"+i+j,"display", "none");//BUTTON關閉
		}
		document.getElementById("menu0"+i).style.display = "none";//MENU關閉
	}
//顯示選擇面板
	document.getElementById("menu0"+buttonDivCode).style.display = "";//MENU開啟
	for(var i=1;i<=3;i++)
	{
		if(i==buttonDivCode){
			var bn = "#B.0"+i+"2";
				//alert(bn);
				vat.item.setStyleByName(bn,"display", "inline");//BUTTON開啟
		}
		else{
			var bn = "#B.0"+i+"1";
				//alert(bn);
				vat.item.setStyleByName(bn,"display", "inline");//BUTTON開啟
		}
	}
}

/**服務性商品**/								
function ZZFunction(zzType){
	if(stepConfig("isExecute",0,1))
	{
		//var amount;
		//var count;
		var zzItem = zzType+vat.item.getValueByName("#F.brandCode");
		vat.item.setValueByName("#F.posBroCode",zzItem);
	
		var addInDetail = false; //必須輸入金額與數量才會寫入
		if(amount = prompt('請輸入金額','0'))
		{
			if(count  = prompt('請輸入數量','1'))
			{
				addInDetail = true;
				isZZ = true;
			}
		}
		if(addInDetail===true){
			//商品加入明細
			checkItem(zzType+vat.item.getValueByName("#F.brandCode"));
			//寫入欄位資訊
			//vat.item.setGridValueByName("originalUnitPrice",ptr-1,amount);
			//vat.item.setGridValueByName("quantity",ptr-1,count);
			//vat.item.setGridValueByName("actualSalesAmount",ptr-1,amount*count);
			//vat.block.pageDataSave(vnB_Detail);
		} else {
			vat.item.setValueByName("#F.posBroCode","");
		}
		if(stepConfig("isChange",1,1)){
			nowStep=1;
		}
	}
	else{
		alert(itemInsert+stepErrorMsg);
	}
}
	
/**取消結帳**/
/*function closeWindows(){
	if(confirm("是否確認離開?")){
		   	openLoginPos();
	}
}*/

/**回到登入頁面**/
function openLoginPos()
{
	var url = "posLogin_offline.jsp?";
	var newwin = window.open(url, "", "toolbar=no,location=no,directories=no,width=690,height=510");
    //newwin.resizeTo((screen.availWidth),(screen.availHeight));
    newwin.moveTo((screen.availWidth/2-690/2),(screen.availHeight/2-510/2));
    //newwin.moveTo((screen.availWidth/2-350),(screen.availHeight/2-250));
	window.top.close();
}
/**刪除明細**/
function detailDelete(){
	if(stepConfig("isExecute",1,1))
	{
		var upperStep = 1;
		var qPtr = ptr - 1;
		var vOldPage = vnCurrentPage;
		vat.block.pageDataSave(vnB_Detail, 
		{
			funcSuccess:function(){
			if(confirm("是否確認刪除?")){
					vat.ajax.XHRequest(
				       	{
							post:"process_object_name=soDepartmentOrderService"+
				                    "&process_object_method_name=executeDetailDelete"+
				                    "&headId=" + vat.item.getValueByName("#F.headId") + 
				                    "&indexNo=" + vat.item.getGridValueByName("indexNo",qPtr),
							find: function deleteSucess(oXHR){
									
									var vIndexNo = vat.ajax.getValue("indexNo", oXHR.responseText);
									var nowPage = Math.ceil(vIndexNo/10);
									//alert("刪除完成"+vIndexNo+"/"+vOldPage);
									//alert("刪除完成");
									
					           		if(vIndexNo%10 === 0){//若刪除後明細數可被頁數整除,則更新回前頁
					           			if(vIndexNo==0){
					           				nowPage = 1;
					           				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = nowPage);
											ptr=1;
											oldPtr = ptr+1;
											upperStep = 0;
											lastPage--;
					           			}
					           			else{
					           				vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = nowPage);
											ptr=10;
											oldPtr = ptr+1;
										}
									}
									 
									else//若不可整除留在原頁面
									{
										vat.block.pageDataLoad(vnB_Detail, vnCurrentPage = nowPage);
						           		ptr=vIndexNo%10+1;
										oldPtr = ptr;
									}
					           		//pageDataLoad
					           		//showTotalCountPage();//回傳參數
				           	}   
						});
				}
			}
		});
		if(stepConfig("isChange",upperStep,upperStep)){
			nowStep=1;
		}
	}
	else{
		alert(exeDetailModify+stepErrorMsg);
	}	
}

/**備註**/
function reMark(){
	if(stepConfig("isExecute",1,1))
	{
		var str;
		if(str=prompt("備註","")){
			//alert(str);
			var quantity = vat.item.nameMake("reserve1", ptr-1);			
			var ptr_Qty = ptr-1;
			vat.item.setGridValueByName("reserve1",ptr_Qty,str);
		}else{
			alert("取消輸入");
		}
		if(stepConfig("isChange",1,1)){
			nowStep=1;
		}
	}
	else{
		alert(exeDetailModify+stepErrorMsg);
	}
}

/**尋找會員**/
function findVIP(){

	var searchKey = vat.item.getValueByName("#F.posBroCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var brandCode = "T2";//document.forms[0]["#loginBrandCode"].value;
	if(searchKey!==0){
	vat.ajax.XHRequest(
       	{
			post:"process_object_name=soDepartmentOrderService"+
                    "&process_object_method_name=findVIP"+
                    "&brandCode=" + brandCode + 
                    "&searchKey=" + searchKey ,
			find: function checkItemCode(oXHR){
           		vat.item.setValueByName("#F.customerCode",vat.ajax.getValue("customerCode", oXHR.responseText));
           		vat.item.setValueByName("#F.customerName",vat.ajax.getValue("customerName", oXHR.responseText));
				
           		if(vat.ajax.getValue("customerCode", oXHR.responseText) === ''){
					var doSearch = confirm("查無會員資料,是否確認開啟詳細查詢?");
					if(doSearch){
						//openPicker("Bu_AddressBook:searchCustomer:20100101.page");
						openReportWindow('Bu_AddressBook','searchCustomer','20171122','');
					}
           		} 
           		else{
           			alert("會員編號:"+vat.item.getValueByName("#F.customerCode")+"  姓名:"+vat.item.getValueByName("#F.customerName")+"  資訊已成功加入");
           		}
           	}   
		});
	}
	else{}
		vat.item.setValueByName("#F.posBroCode","");
}

/**當日銷售**/
function calculateTodaySales(){

	var searchKey = vat.item.getValueByName("#F.posBroCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var brandCode = document.forms[0]["#loginBrandCode"].value;
	var shopCode = document.forms[0]["#loginEmployeeCode"].value;
	if(searchKey!==0){
	vat.ajax.XHRequest(
       	{
			post:"process_object_name=soDepartmentOrderService"+
                    "&process_object_method_name=findTodaySales"+
                    "&brandCode=" + brandCode + 
                    "&shopCode=" + shopCode,
			find: function checkItemCode(oXHR){
//資料準備

				
           		var COM = vat.ajax.getValue("ComNum", oXHR.responseText);
           		var printTxt = "";
				var printTxtHead = "";
				var devideLine = "=============================================";
				var p = LKPOSALL.PrinterSts();
				var lResult = LKPOSALL.OpenPort(COM, 115200);
				
           		var Company			= vat.ajax.getValue("CompanyName", oXHR.responseText) + "\n";
				var Title			= vat.ajax.getValue("Title", oXHR.responseText) + "\n";
				var ShopName		= vat.ajax.getValue("ShopName", oXHR.responseText) + "\n";
				var Address 		= vat.ajax.getValue("CompanyAddress", oXHR.responseText) + "\n";
				var CompanyId 		= "統編: " + vat.ajax.getValue("CompanyID", oXHR.responseText) + "\n";
				var CompanyTel		= "電話: " + vat.ajax.getValue("CompanyTel", oXHR.responseText) + "\n";
				var SaleDateTime	= "列印日期: " + vat.ajax.getValue("SaleDateTime", oXHR.responseText) + "\n";
				
				
           		var SubTotalCount  = vat.ajax.getValue("totalCount", oXHR.responseText);
           		var SubTotalAmount = vat.ajax.getValue("totalAmount", oXHR.responseText);

				
				
				//printTxtHead = printTxtHead + Company+ ShopName+ Address+ CompanyId+ Title+ CompanyTel+ SaleDateTime;
				printTxtHead = printTxtHead + devideLine +"\n" + Title + devideLine +"\n\n" + Company + ShopName + Address + CompanyTel + SaleDateTime + devideLine +"\n\n\n";
				LKPOSALL.PrintText("\n", 1, 0, 0);
				LKPOSALL.PrintText(printTxtHead, 1, 0, 0);
           		
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
				
				LKPOSALL.PrintText(printTxt, 0, 0, 0);
           		
           		
//單據列印           		
				p = LKPOSALL.OutputCompletePrinting(2500);
//顯示結果
				if(LKPOSALL.ClosePort()===0){
					alert("[本日銷售]   總筆數:"+vat.ajax.getValue("totalCount", oXHR.responseText)+"    總金額:"+vat.ajax.getValue("totalAmount", oXHR.responseText));
				}
           	}   
		});
	}
	else{}
		vat.item.setValueByName("#F.posBroCode","");
}


/**判斷輸入欄位是否為整數**/
function isInt(s){

	return ( s.search(/^-?[0-9]+$/) == 0);
}
/**POS執行階段**/
function stepConfig(func,lowerStep,upperStep){
	var isAllow =false;
	if(func === "isExecute"){//判斷是否執行Function
		isAllow = (upperStep >= nowStep && lowerStep<= nowStep);
	}
	else if(func === "isChange"){//更新nowStep
		isAllow = (lowerStep > nowStep);
	}
	else{
		alert(errorMsg);
	}
	return isAllow;
}
/**更新付款資訊**/
function showTotalCountPage() {


		vat.block.pageDataSave(vnB_Detail, 
		{  
			funcSuccess:function(){
//				afterSavePageProcess = "totalCount";
				vat.block.pageRefresh(vnB_Detail);
			}
		});

}
/**重印**/
function rePrint(){
	var headId = vat.item.getValueByName("#F.posBroCode");
	if(headId != null && headId != ""){
		//importPrint(headId,1,'REPRINT');
	}else{
		alert("請輸入交易序號!!");
	}
}

/**重印**/
function rePrint1(){
	var headId = vat.item.getValueByName("#F.posBroCode");
	if(headId != null && headId != ""){
		importPrint1(headId,1,'REPRINT');
	}else{
		alert("請輸入交易序號!!");
	}
}

function doSale(){


	if(stepConfig("isExecute",2,2)){
			nowStep=3;
			doSubmit("SIGNING");
	}
	else{
		alert(exeSubmit+stepErrorMsg+nowStep);
	}
		
}

function doSale1(){

	submitType = 'test';
	if(stepConfig("isExecute",2,2)){
			nowStep=3;
			doSubmit("SIGNING");
	}
	else{
		alert(exeSubmit+stepErrorMsg+nowStep);
	}
		
}
//**固定取號
function getDiscountCode(){

	var snosys = vat.item.getValueByName("#F.sysno");
	var grouponCode = vat.item.getValueByName("#F.grouponCode");
	var requestURL = "http://10.99.50.183:8888/springbootdemo/buCouponLogs";
	var dataJSON = {
	  "appCustomerCode": "appCustomerCode",
	  "brandCode": "T1GS",
	  "couponCode": "api_test_grouponCode" ,
	  "couponNo": "",
	  "customerCode": "T96085",
	  "customerPoNo": "T96085",
	  "expiryDate": "2021-05-24T09:15:40.202Z",
	  "posMachineCode": "A9",
	  "receiver": "T96085",
	  "recodeType": "1",
	  "shopCode": "12350",
	  "status": "take",
	  "supplierCode": "T96085",
	  "sysNo": snosys
	};
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		    alert("httpResponse 4 & 200");
	    }
	};
	xhttp.open("POST", requestURL, true);
	xhttp.setRequestHeader("Content-Type", "application/json");
  	xhttp.send(JSON.stringify(dataJSON));
	
}

/**結帳**/			
function doSubmit(formAction){

	var alertMessage ="是否確定送出?";
	if(confirm(alertMessage)){
		vat.block.pageDataSave(vnB_Detail, 
		{  
			funcSuccess:function(){
			    vat.bean().vatBeanOther.formAction = formAction;
				vat.block.submit(function(){
						return "process_object_name=soDepartmentOrderAction"+
						"&process_object_method_name=performPosMainTransaction";
					},{
						bind:true, link:true, other:true 
					});
				var headId = vat.item.getValueByName("#F.headId");
				//getDiscountCode(); //get groupon code by api
				importPrint3(headId,2,'SUBMIT');
				showTotalCountPage();
			}
		});
	}else{
		nowStep=2;
	}
	openLoginPos();
}

function openReportWindow(arg1,arg2,arg3,arg4)
{
	var employeeCode = vat.item.getValueByName("#F.salesEmployeeCode");
	var salesType = vat.item.getValueByName("#F.salesType");
	var url = arg1+":"+arg2+":"+arg3+".page?"+arg4;
	//alert(screen.availWidth+"=="+screen.availHeight);
	var VsCustomer = window.showModalDialog(url, '', 'dialogWidth:1280px;dialogHeight:760px;dialogTop:0px;dialogLeft:0px;status:no;');
	//var VsCustomer = window.open(url, '_blank', 'height=760, width=1280,titlebar=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes');
    //VsCustomer.focus();
	if(arg2==="searchCustomer"){
		//alert(VsCustomer);
		childclose(VsCustomer);
	}else if(arg3==="thirdParty20181115"){
		executePay(VsCustomer);
	}
}

function openVIPCreateWindow()
{
	//var customerCode = vat.item.getValueByName("#F.posBroCode");
	//var employeeCode = vat.item.getValueByName("#F.salesEmployeeCode");
	//var salesType = vat.item.getValueByName("#F.salesType");
	var url = "SO_Department_Order:main:newCustomer20170918.page?";
	//if(customerCode!==""){
	//	url = url+"customerCode="+customerCode;
	//}
	var VsCustomer = window.showModalDialog(url, '', 'dialogWidth:700px;dialogHeight:550px;dialogTop:0px;dialogLeft:0px;status:no;');
	if(VsCustomer != null){
		//alert("建立的會員編號為:"+VsCustomer);
		vat.item.setValueByName("#F.posBroCode",VsCustomer);
		findVIP();
	}

}

function openGoodSearchWindow()
{
	var customerCode = vat.item.getValueByName("#F.posBroCode");
	var employeeCode = vat.item.getValueByName("#F.salesEmployeeCode");
	var salesType = vat.item.getValueByName("#F.salesType");
	var url = "SO_Department_Order:main:goodSearch20180106.page?";
	if(customerCode!==""){
		url = url+"customerCode="+customerCode;
	}
	var VsCustomer = window.showModalDialog(url, '', 'dialogWidth:700px;dialogHeight:760px;dialogTop:0px;dialogLeft:0px;status:no;');

}

function childclose(result){
	vat.item.setValueByName("#F.posBroCode"    , result);
	findVIP();
	vat.item.setValueByName("#F.posBroCode","");
}


/**單據作廢**/
function executeVoidOrder(){

		var vHeadId = vat.item.getValueByName("#F.posBroCode");
		var vEmployeeCode = vat.item.getValueByName("#F.superintendentCode");
		var vShopCode = vat.item.getValueByName("#F.shopCode");
		var vIsInt = isInt(vHeadId);
		if((vHeadId !== "") && vIsInt)
		{
			var alertMessage ="是否確定單據作廢?";
			if(confirm(alertMessage)){
				vat.ajax.XHRequest(
			    {
			    	post:"process_object_name=soDepartmentOrderService" +
			                        "&process_object_method_name=executeAJAXAntiConfirm" +
				                    "&headId=" + vHeadId+
				                    "&shopCode=" + vShopCode+
			                    	"&employeeCode=" + vEmployeeCode +
				                    "&organizationCode=" + "TM",
					find: function executeVoidOrderSuccess(oXHR)
					{
						alert(vat.ajax.getValue("resultMsg", oXHR.responseText));
						//vat.block.pageRefresh(vnB_Detail);
			        }
			    });
			}
		}
		else{
			alert("輸入錯誤,請輸入單據序號");
		}
		vat.item.setValueByName("#F.posBroCode","");
}


function executePay(thirdPartyWay){
	var payAmount = vat.item.getValueByName("#F.posBroCode");
	var customerPoNo = vat.item.getValueByName("#F.headId");
	if(thirdPartyWay != "" && thirdPartyWay != null){
		if(userPayNo  = prompt('請刷條碼','1')){
			vat.ajax.XHRequest({
	    		post:"process_object_name=soDepartmentOrderService"+
               		 "&process_object_method_name=thirdPartyPayment"+
               		 "&payType=" + thirdPartyWay+
                	 "&payAmount=" + payAmount+
                	 "&brandCode=" + vat.item.getValueByName("#F.brandCode")+
	            	 "&posMachineCode=" + vat.item.getValueByName("#F.shopCode")+
	            	 "&customerPoNo=" + customerPoNo+
	            	 "&userPayNo=" + userPayNo,
	                   		 
	         	find: function change(oXHR){
			    	var responseCode = vat.ajax.getValue("responseCode" , oXHR.responseText);
			       	var responseDesc = vat.ajax.getValue("responseDesc" , oXHR.responseText);
					alert(responseDesc);
			        if(responseCode == "0001"){
			        	reCheck(thirdPartyWay, userPayNo, payAmount, customerPoNo);
			        }else{
				        vat.item.setValueByName("#F.paymentThirdPartyWay",vat.ajax.getValue("thirdPartyWay" , oXHR.responseText));
				       	vat.item.setValueByName("#F.thirdPartyName",vat.ajax.getValue("thirdPartyName" , oXHR.responseText));
				       	vat.item.setValueByName("#F.userPayNo",userPayNo);
			        }

				},
				fail: function changeError(){
	          		alert("fail");
	          	}
			});
		}
	}
}

function reCheck(vThirdPartyWay, vUserPayNo, vPayAmount, vCustomerPoNo){
	var vBrandCode = vat.item.getValueByName("#F.brandCode")
	var vPosMachineCode = vat.item.getValueByName("#F.shopCode");
	if(vThirdPartyWay != "" && vThirdPartyWay != null){
		if(vUserPayNo != "" && vUserPayNo != null){
			vat.ajax.XHRequest({
	    		post:"process_object_name=soDepartmentOrderService"+
               		 "&process_object_method_name=thirdPartyPayment"+
               		 "&payType=" + vThirdPartyWay+
                	 "&payAmount=" + vPayAmount+
                	 "&brandCode=" + vBrandCode+
	            	 "&posMachineCode=" + vPosMachineCode+
	            	 "&customerPoNo=" + vCustomerPoNo+
	            	 "&userPayNo=" + vUserPayNo+
	            	 "&reCheckId=" + vCustomerPoNo,
	                   		 
	         	find: function change(oXHR){
			    	var responseCode = vat.ajax.getValue("responseCode" , oXHR.responseText);
			       	var responseDesc = vat.ajax.getValue("responseDesc" , oXHR.responseText);
			       	vat.item.setValueByName("#F.paymentThirdPartyWay",vat.ajax.getValue("thirdPartyWay" , oXHR.responseText));
			       	vat.item.setValueByName("#F.thirdPartyName",vat.ajax.getValue("thirdPartyName" , oXHR.responseText));
			       	vat.item.setValueByName("#F.userPayNo",userPayNo);
			        alert(responseDesc);

				},
				fail: function changeError(){
	          		alert("fail");
	          	}
			});
		}
	}
}

function checkBirthday(zzType){
	if(stepConfig("isExecute",0,1)){
		var vBrandCode = vat.item.getValueByName("#F.brandCode");
		var vCustomerCode = vat.item.getValueByName("#F.customerCode");
		var vSuperintendentCode = vat.item.getValueByName("#F.superintendentCode");
		var vShopCode = vat.item.getValueByName("#F.shopCode");
		var vHeadId = vat.item.getValueByName("#F.headId");
		if(vCustomerCode != null && vCustomerCode != ""){
			vat.block.pageDataSave(vnB_Detail,{
				funcSuccess: function(){
					vat.ajax.XHRequest({
							post:"process_object_name=soDepartmentOrderService"+
				               		 "&process_object_method_name=executeBirthdayCoupon"+
				               		 "&headId=" + vHeadId +
				               		 "&brandCode=" + vBrandCode +
				               		 "&customerCode=" + vCustomerCode +
				               		 "&superintendentCode=" + vSuperintendentCode +
				               		 "&shopCode=" + vShopCode,
					                   		 
					        find: function change(oXHR){
								var vCheckBirthdayItemcode = vat.ajax.getValue("itemCode", oXHR.responseText);
								var vOriginalUnitPrice = vat.ajax.getValue("originalUnitPrice", oXHR.responseText);
								var vAllDiscount = vat.ajax.getValue("allDiscount", oXHR.responseText);
								var vErrorMsg = vat.ajax.getValue("errorMsg", oXHR.responseText);
								var vUseYear = vat.ajax.getValue("useYear", oXHR.responseText);
								var vQuantity = -1;
								var vActualSalesAmount = vOriginalUnitPrice * vQuantity;
								
								//alert(vCheckBirthdayItemcode);
								if(vErrorMsg == null || vErrorMsg == ""){
									vat.item.setValueByName("#F.useYear",vUseYear);
									vat.item.setValueByName("#F.posBroCode",vCheckBirthdayItemcode);
									
									amount = vOriginalUnitPrice;
									count = vQuantity;
									allDiscount = vAllDiscount;
									isZZ = true;
									
	           						checkItem(vCheckBirthdayItemcode);
	           						/*
									vat.item.setGridValueByName("originalUnitPrice",ptr-1,vOriginalUnitPrice);
									vat.item.setGridValueByName("quantity",ptr-1,vQuantity);
									vat.item.setGridValueByName("actualSalesAmount",ptr-1,vActualSalesAmount);
									vat.item.setGridValueByName("discountRate",ptr-1,100);
									
									if(vAllDiscount===""){
										//alert("BS");
									}else{
										vat.item.setValueByName("#F.posBroCode",vAllDiscount);
										doDiscount("1","TOTAL","AUTO");
									}
									vat.block.pageDataSave(vnB_Detail);
									*/
								}else{
									alert(vErrorMsg);
								}
							},
							fail: function changeError(){
					          	alert("fail");
					        }
					});
				}
			});
		}else{
			alert("請輸入會員代號");
		}
	}else{
		alert(itemInsert+stepErrorMsg);
	}
}


/**尋找會員**/
/*
function executeVoidOrder(){

	var searchKey = vat.item.getValueByName("#F.posBroCode").replace(/^\s+|\s+$/, '').toUpperCase();
	var brandCode = document.forms[0]["#loginBrandCode"].value;
	if(searchKey!==0){
	vat.ajax.XHRequest(
       	{
			post:"process_object_name=soDepartmentOrderService"+
                    "&process_object_method_name=findVIP"+
                    "&brandCode=" + brandCode + 
                    "&searchKey=" + searchKey,
			find: function checkItemCode(oXHR){
           		vat.item.setValueByName("#F.customerCode",vat.ajax.getValue("customerCode", oXHR.responseText));
           		vat.item.setValueByName("#F.customerName",vat.ajax.getValue("customerName", oXHR.responseText));
				
           		if(vat.ajax.getValue("customerCode", oXHR.responseText) === ''){
					var doSearch = confirm("查無會員資料,是否確認開啟詳細查詢?");
					if(doSearch){
						//openPicker("Bu_AddressBook:searchCustomer:20100101.page");
						openReportWindow('Bu_AddressBook','searchCustomer','20100101');
					}
           		} 
           		else{
           			alert("會員編號:"+vat.item.getValueByName("#F.customerCode")+"  姓名:"+vat.item.getValueByName("#F.customerName")+"  資訊已成功加入");
           		}
           	}   
		});
	}
	else{}
		vat.item.setValueByName("#F.posBroCode","");
}*/